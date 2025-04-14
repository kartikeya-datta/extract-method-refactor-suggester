error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15295.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15295.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15295.java
text:
```scala
g@@etEmbeddedIdCols(fmds[i], cols);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.jdbc.meta.strats;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.Embeddable;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.FieldStrategy;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.ValueMappingImpl;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.kernel.ObjectIdStateManager;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.kernel.StateManagerImpl;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.util.MetaDataException;

/**
 * Base class for embedded value handlers.
 *
 * @author Abe White
 * @since 0.4.0
 */
public abstract class EmbedValueHandler
    extends AbstractValueHandler {

    private static final Localizer _loc = Localizer.forPackage
        (EmbedValueHandler.class);

    /**
     * Maps embedded value and gathers columns and arguments into given lists.
     */
    protected void map(ValueMapping vm, String name, ColumnIO io,
        boolean adapt, List cols, List args) {
        // have to resolve embedded value to collect its columns
        vm.getEmbeddedMapping().resolve(vm.MODE_META | vm.MODE_MAPPING);

        // gather columns and result arguments
        FieldMapping[] fms = vm.getEmbeddedMapping().getFieldMappings();
        Column[] curCols;
        Object[] curArgs;
        ColumnIO curIO;
        for (int i = 0; i < fms.length; i++) {
            if (fms[i].getManagement() != FieldMapping.MANAGE_PERSISTENT)
                continue;
            FieldStrategy strat = fms[i].getStrategy();
            
            if (!(strat instanceof Embeddable))
                throw new MetaDataException(_loc.get("not-embeddable",
                    vm, fms[i]));
            
            ValueMapping val = fms[i].getValueMapping();
            if (val.getEmbeddedMapping() != null)
                map(val, name, io, adapt, cols, args);
            
            curCols = ((Embeddable) strat).getColumns();
            curIO = ((Embeddable) strat).getColumnIO();
            for (int j = 0; j < curCols.length; j++) {
                io.setInsertable(cols.size(), curIO.isInsertable(j, false));
                io.setNullInsertable(cols.size(),
                    curIO.isInsertable(j, true));
                io.setUpdatable(cols.size(), curIO.isUpdatable(j, false));
                io.setNullUpdatable(cols.size(), curIO.isUpdatable(j, true));
                cols.add(curCols[j]);
            }

            curArgs = ((Embeddable) fms[i].getStrategy()).getResultArguments();
            if (curCols.length == 1)
                args.add(curArgs);
            else if (curCols.length > 1)
                for (int j = 0; j < curCols.length; j++)
                    args.add((curArgs == null) ? null
                        : ((Object[]) curArgs)[j]);
        }
    }

    /**
     * Helper to convert an object value to its datastore equivalent.
     *
     * @param em state manager for embedded object
     * @param vm owning value
     * @param store store manager
     * @param cols embedded columns
     * @param rval return array if multiple columns
     * @param idx index in columns array to start
     */
    protected Object toDataStoreValue(OpenJPAStateManager em, ValueMapping vm,
            JDBCStore store, Column[] cols, Object rval, int idx) {
        
        // This is a placeholder to hold the value generated in 
        // toDataStoreValue1. When this method is called from 
        // ElementEmbedValueHandler or ObjectIdValueHandler, 
        // if the dimension of cols > 1, rval is an array of the 
        // same dimension. If the dimension of cols is 1, rval is null.
        // If rval is not null, it is an array of objects and this array
        // will be populated in toDatastoreValue1. If rval is null,
        // a new value will be added to rvals in toDataStoreValue1
        // and return to the caller.
        List rvals = new ArrayList();
        if (rval != null)
            rvals.add(rval);
        
        toDataStoreValue1(em, vm, store, cols, rvals, idx);
        return rvals.get(0);
    }    
    
    protected int toDataStoreValue1(OpenJPAStateManager em, ValueMapping vm,
        JDBCStore store, Column[] cols, List rvals, int idx) {
        // set rest of columns from fields
        FieldMapping[] fms = vm.getEmbeddedMapping().getFieldMappings();
        Object cval;
        Column[] ecols;
        Embeddable embed;
        for (int i = 0; i < fms.length; i++) {
            if (fms[i].getManagement() != FieldMapping.MANAGE_PERSISTENT)
                continue;
            
            // This recursive code is mainly to deal with situations
            // where an entity contains a collection of embeddableA.
            // The embeddableA element in the collection contains an 
            // embeddableB. The parameter vm to toDataStoreValue is 
            // embeddableA. If some field in embeddableA is of type 
            // embeddableB, recursive call is required to populate the 
            // value for embeddableB.
            ValueMapping val = fms[i].getValueMapping();
            if (val.getEmbeddedMapping() != null) {
                cval = (em == null) ? null : em.fetch(i);
                if (cval instanceof PersistenceCapable) {
                    OpenJPAStateManager embedSm = (OpenJPAStateManager)
                        ((PersistenceCapable)cval).pcGetStateManager();
                    idx = toDataStoreValue1(embedSm, val, store, cols, rvals, idx);
                } else if (cval instanceof ObjectIdStateManager) {
                    idx = toDataStoreValue1((ObjectIdStateManager)cval, val, store, cols, rvals, idx);
                } else if (cval == null) {
                    idx = toDataStoreValue1(null, val, store, cols, rvals, idx);
                }
            }
            
            embed = (Embeddable) fms[i].getStrategy();
            ecols = embed.getColumns();
            if (ecols.length == 0)
                continue;

            cval = (em == null) ? null : em.fetch(i);
            cval = embed.toEmbeddedDataStoreValue(cval, store);
            if (cols.length == 1) {
                // rvals is empty
                rvals.add(cval); // save the return value
            } else if (ecols.length == 1) {
                Object rval = rvals.get(0);
                ((Object[]) rval)[idx++] = cval;
            } else {
                Object rval = rvals.get(0);
                System.arraycopy(cval, 0, rval, idx, ecols.length);
                idx += ecols.length;
            }
        }
        return idx;
    }

    /**
     * Helper to convert a datastore value to its object equivalent.
     *
     * @param em state manager for embedded object
     * @param vm owning value
     * @param val datastore value
     * @param store optional store manager
     * @param fetch optional fetch configuration
     * @param cols embedded columns
     * @param idx index in columns array to start
     */
    protected void toObjectValue(OpenJPAStateManager em, ValueMapping vm,
            Object val, JDBCStore store, JDBCFetchConfiguration fetch,
            Column[] cols, int idx)
            throws SQLException {
        toObjectValue1(em, vm, val, store, fetch, cols, idx);
    }    
    
    protected int toObjectValue1(OpenJPAStateManager em, ValueMapping vm,
        Object val, JDBCStore store, JDBCFetchConfiguration fetch,
        Column[] cols, int idx)
        throws SQLException {
        FieldMapping[] fms = vm.getEmbeddedMapping().getFieldMappings();
        Embeddable embed;
        Object cval;
        Column[] ecols;
        for (int i = 0; i < fms.length; i++) {
            if (fms[i].getManagement() != FieldMapping.MANAGE_PERSISTENT)
                continue;

            ValueMapping vm1 = fms[i].getValueMapping();
            OpenJPAStateManager em1 = null;
            
            embed = (Embeddable) fms[i].getStrategy();
            if (vm1.getEmbeddedMapping() != null) {
                if (em instanceof StateManagerImpl) {
                em1 = store.getContext().embed(null, null, em, vm1);
                idx = toObjectValue1(em1, vm1, val, store, fetch, cols, idx);
                } else if (em instanceof ObjectIdStateManager) {
                    em1 = new ObjectIdStateManager(null, null, vm1);
                    idx = toObjectValue1(em1, vm1, val, store, null, getColumns(fms[i]), idx);
                }
                cval = em1.getManagedInstance();
            } else {
                ecols = embed.getColumns();
                if (ecols.length == 0)
                    cval = null;
                else if (idx == 0 && ecols.length == cols.length)
                    cval = val;
                else if (ecols.length == 1)
                    cval = ((Object[]) val)[idx++];
                else {
                    cval = new Object[ecols.length];
                    System.arraycopy(val, idx, cval, 0, ecols.length);
                    idx += ecols.length;
                }
            }

            if (store != null && em instanceof StateManagerImpl)
                embed.loadEmbedded(em, store, fetch, cval);
            else {
                if (!(em instanceof ObjectIdStateManager))
                cval = embed.toEmbeddedObjectValue(cval);
                em.store(fms[i].getIndex(), cval);
            }
        }
        return idx;
    }
    private Column[] getColumns(FieldMapping fm) {
        List<Column> colList = new ArrayList<Column>();
        getEmbeddedIdCols(fm, colList);
        Column[] cols = new Column[colList.size()];
        int i = 0;
        for (Column col : colList) {
            cols[i++] = col;
        }
        return cols;
    }
    
    public static void getEmbeddedIdCols(FieldMapping fmd, List cols) {
        ClassMapping embed = fmd.getEmbeddedMapping();
        FieldMapping[] fmds = embed.getFieldMappings();
        for (int i = 0; i < fmds.length; i++) {
            if (fmds[i].getValue().getEmbeddedMetaData() == null) {
                getIdColumns(fmds[i], cols);
            } else {
                getEmbeddedIdCols(fmd, cols);
            }
        }
    }
    
    public static void getIdColumns(FieldMapping fmd, List cols) {
        Column[] pkCols =  ((ValueMappingImpl)fmd.getValue()).getColumns();
        for (int j = 0; j < pkCols.length; j++) {
            Column newCol = new Column();
            newCol.setName(pkCols[j].getName());
            cols.add(newCol);
        }
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15295.java