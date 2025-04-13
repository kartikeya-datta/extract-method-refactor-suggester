error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17947.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17947.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17947.java
text:
```scala
i@@f (fm.getName().equals(refColName.getName()))

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

import org.apache.openjpa.jdbc.identifier.DBIdentifier;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.kernel.ObjectIdStateManager;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.util.InternalException;

/**
 * Handler for embedded object id fields.
 *
 * @author Abe White
 * @nojavadoc
 * @since 0.4.0
 */
public class ObjectIdValueHandler
    extends EmbedValueHandler {

    private Object[] _args = null;

    /**
     * @deprecated
     */
    public Column[] map(ValueMapping vm, String name, ColumnIO io,
        boolean adapt) {
        DBDictionary dict = vm.getMappingRepository().getDBDictionary();
        DBIdentifier colName = DBIdentifier.newColumn(name, dict != null ? dict.delimitAll() : false);
        return map(vm, colName, io, adapt);
    }

    public Column[] map(ValueMapping vm, DBIdentifier name, ColumnIO io,
        boolean adapt) {
        List cols = new ArrayList();
        List args = new ArrayList();
        setMapsIdCol(vm);
        super.map(vm, name, io, adapt, cols, args);

        vm.setColumns((Column[]) cols.toArray(new Column[cols.size()]));
        _args = args.toArray();
        return vm.getColumns();
    }
    
    private void setMapsIdCol(ValueMapping vm) {
        if (!(vm instanceof FieldMapping)) 
            return;
        if (!((FieldMapping)vm).hasMapsIdCols())
            return;
        ClassMapping embeddedMeta = (ClassMapping)((FieldMapping)vm).getValue().getEmbeddedMetaData();
        if (embeddedMeta == null)
            return;
        List mapsIdColList = ((FieldMapping)vm).getValueInfo().getMapsIdColumns();
        if (mapsIdColList.size() > 0 ) {
            setMapsIdCols(mapsIdColList, embeddedMeta);
            return;
        } 

        FieldMapping[] fmds = embeddedMeta.getFieldMappings();
        for (int i = 0; i < fmds.length; i++) {
            mapsIdColList = fmds[i].getValueInfo().getMapsIdColumns();
            if (mapsIdColList.size() == 0)
                continue;
            ClassMapping embeddedMeta1 = (ClassMapping)fmds[i].getEmbeddedMetaData();
            if (embeddedMeta1 != null) 
                setMapsIdCols(mapsIdColList, embeddedMeta1);
            else 
                setMapsIdCols(mapsIdColList, fmds[i]);
        }
    }
    
    private void setMapsIdCols(List cols, ClassMapping cm) {
        for (int i = 0; i < cols.size(); i++) {
            DBIdentifier refColName = ((Column)cols.get(i)).getTargetIdentifier();
            FieldMapping fm = getReferenceField(cm, refColName);
            if (fm != null) {
                List colList1 = new ArrayList();
                colList1.add(cols.get(i));
                fm.setMapsIdCols(true);
                fm.getValueInfo().setMapsIdColumns(colList1);
            }
        }
    }

    private void setMapsIdCols(List cols, FieldMapping fm) {
        if (cols.size() == 1) {
            fm.setMapsIdCols(true);
            fm.getValueInfo().setMapsIdColumns(cols);
            return;
        }
            
        for (int i = 0; i < cols.size(); i++) {
            DBIdentifier refColName = ((Column)cols.get(i)).getTargetIdentifier();
            if (isReferenceField(fm, refColName)) {
                List colList1 = new ArrayList();
                colList1.add(cols.get(i));
                fm.setMapsIdCols(true);
                fm.getValueInfo().setMapsIdColumns(colList1);
            }
        }
    }
    
    private FieldMapping getReferenceField(ClassMapping cm, DBIdentifier refColName) {
        FieldMapping[] fmds = cm.getFieldMappings();
        for (int i = 0; i < fmds.length; i++) {
            if (isReferenceField(fmds[i], refColName))
                return fmds[i];
        }
        return null;
    }
    
    private boolean isReferenceField(FieldMapping fm, DBIdentifier refColName) {
        List cols = fm.getValueInfo().getColumns();
        if (cols.size() == 0) {
            if (fm.getName().equals(refColName))
                return true;                
        } else {
            if (((Column)cols.get(0)).getIdentifier().equals(refColName))
                return true;
        } 
        return false;
    }

    public Object getResultArgument(ValueMapping vm) {
        return _args;
    }

    public Object toDataStoreValue(ValueMapping vm, Object val,
        JDBCStore store) {
        OpenJPAStateManager sm = (val == null) ? null
            : new ObjectIdStateManager(val, null, vm);
        Column[] cols = vm.getColumns();
        Object rval = null;
        if (cols.length > 1)
            rval = new Object[cols.length];
        return super.toDataStoreValue(sm, vm, store, cols, rval, 0);
    }

    public Object toObjectValue(ValueMapping vm, Object val) {
        if (val == null)
            return null;

        OpenJPAStateManager sm = new ObjectIdStateManager(null, null, vm);
        try {
            super.toObjectValue(sm, vm, val, null, null, vm.getColumns(), 0);
        } catch (SQLException se) {
            // shouldn't be possible
            throw new InternalException(se);
        }
        return sm.getManagedInstance();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17947.java