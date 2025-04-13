error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2388.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2388.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2388.java
text:
```scala
i@@nt idx = (setOrder && order != null) ? order.getBase() : 0;

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
import java.util.Collection;
import java.util.Iterator;

import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.FieldMappingInfo;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.ValueMappingInfo;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ForeignKey;
import org.apache.openjpa.jdbc.sql.Joins;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.Row;
import org.apache.openjpa.jdbc.sql.RowManager;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.kernel.StoreContext;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.JavaTypes;
import org.apache.openjpa.util.ChangeTracker;
import org.apache.openjpa.util.MetaDataException;
import org.apache.openjpa.util.Proxies;
import org.apache.openjpa.util.Proxy;

/**
 * Maps a set of related objects through an association table.
 *
 * @author Abe White
 */
public abstract class RelationToManyTableFieldStrategy
    extends StoreCollectionFieldStrategy {

    private static final Localizer _loc = Localizer.forPackage
        (RelationToManyTableFieldStrategy.class);

    protected ClassMapping[] getIndependentElementMappings(boolean traverse) {
        return (traverse)
            ? field.getElementMapping().getIndependentTypeMappings()
            : ClassMapping.EMPTY_MAPPINGS;
    }

    protected ForeignKey getJoinForeignKey(ClassMapping elem) {
        return field.getJoinForeignKey();
    }

    protected void selectElement(Select sel, ClassMapping elem,
        JDBCStore store, JDBCFetchConfiguration fetch, int eagerMode,
        Joins joins) {
        sel.select(elem, field.getElementMapping().getSelectSubclasses(),
            store, fetch, eagerMode, joins);
    }

    protected Object loadElement(OpenJPAStateManager sm, JDBCStore store,
        JDBCFetchConfiguration fetch, Result res, Joins joins)
        throws SQLException {
        ClassMapping elem = res.getBaseMapping();
        if (elem == null)
            elem = field.getElementMapping().getIndependentTypeMappings()[0];
        return res.load(elem, store, fetch, joins);
    }

    protected Joins join(Joins joins, ClassMapping elem) {
        return join(joins, false);
    }

    protected Joins joinElementRelation(Joins joins, ClassMapping elem) {
        ValueMapping vm = field.getElementMapping();
        return joins.joinRelation(field.getName(), vm.getForeignKey(elem), 
            elem, vm.getSelectSubclasses(), false, false);
    }

    public void map(boolean adapt) {
        field.getValueInfo().assertNoSchemaComponents(field, !adapt);
        field.getKeyMapping().getValueInfo().assertNoSchemaComponents
            (field.getKey(), !adapt);

        ValueMapping elem = field.getElementMapping();
        if (elem.getTypeCode() != JavaTypes.PC || elem.isEmbeddedPC())
            throw new MetaDataException(_loc.get("not-elem-relation", field));

        // check for named inverse
        FieldMapping mapped = field.getMappedByMapping();
        ValueMappingInfo vinfo = elem.getValueInfo();
        boolean criteria = vinfo.getUseClassCriteria();
        if (mapped != null) {
            if (mapped.getElement().getTypeCode() != JavaTypes.PC)
                throw new MetaDataException(_loc.get("not-inv-relation-coll",
                    field, mapped));
            field.getMappingInfo().assertNoSchemaComponents(field, !adapt);
            vinfo.assertNoSchemaComponents(elem, !adapt);
            mapped.resolve(mapped.MODE_META | mapped.MODE_MAPPING);

            if (!mapped.isMapped() || mapped.isSerialized())
                throw new MetaDataException(_loc.get("mapped-by-unmapped",
                    field, mapped));

            field.setJoinForeignKey(mapped.getElementMapping().
                getForeignKey(field.getDefiningMapping()));
            elem.setForeignKey(mapped.getJoinForeignKey());
            elem.setUseClassCriteria(criteria);
            field.setOrderColumn(mapped.getOrderColumn());
            return;
        }

        field.mapJoin(adapt, true);
        if (elem.getTypeMapping().isMapped()) {
            ForeignKey fk = vinfo.getTypeJoin(elem, "element", false, adapt);
            elem.setForeignKey(fk);
            elem.setColumnIO(vinfo.getColumnIO());
        } else
            RelationStrategies.mapRelationToUnmappedPC(elem, "element", adapt);
        elem.setUseClassCriteria(criteria);
        elem.mapConstraints("element", adapt);

        FieldMappingInfo finfo = field.getMappingInfo();
        Column orderCol = finfo.getOrderColumn(field, field.getTable(), adapt);
        field.setOrderColumn(orderCol);
        field.setOrderColumnIO(finfo.getColumnIO());
        field.mapPrimaryKey(adapt);
    }

    public void insert(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        if (field.getMappedBy() == null)
            insert(sm, rm, sm.fetchObject(field.getIndex()));
    }

    private void insert(OpenJPAStateManager sm, RowManager rm, Object vals)
        throws SQLException {
        Collection coll = toCollection(vals);
        if (coll == null || coll.isEmpty())
            return;

        Row row = rm.getSecondaryRow(field.getTable(), Row.ACTION_INSERT);
        row.setForeignKey(field.getJoinForeignKey(), field.getJoinColumnIO(),
            sm);

        ValueMapping elem = field.getElementMapping();
        StoreContext ctx = sm.getContext();
        Column order = field.getOrderColumn();
        boolean setOrder = field.getOrderColumnIO().isInsertable(order, false);
        int idx = 0;
        OpenJPAStateManager esm;
        for (Iterator itr = coll.iterator(); itr.hasNext(); idx++) {
            esm = RelationStrategies.getStateManager(itr.next(), ctx);
            elem.setForeignKey(row, esm);
            if (setOrder)
                row.setInt(order, idx);
            rm.flushSecondaryRow(row);
        }
    }

    public void update(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        if (field.getMappedBy() != null)
            return;

        Object obj = sm.fetchObject(field.getIndex());
        ChangeTracker ct = null;
        if (obj instanceof Proxy) {
            Proxy proxy = (Proxy) obj;
            if (Proxies.isOwner(proxy, sm, field.getIndex()))
                ct = proxy.getChangeTracker();
        }

        // if no fine-grained change tracking then just delete and reinsert
        if (ct == null || !ct.isTracking()) {
            delete(sm, store, rm);
            insert(sm, rm, obj);
            return;
        }

        StoreContext ctx = store.getContext();
        ValueMapping elem = field.getElementMapping();
        OpenJPAStateManager esm;

        // delete the removes
        Collection rem = ct.getRemoved();
        if (!rem.isEmpty()) {
            Row delRow = rm.getSecondaryRow(field.getTable(),
                Row.ACTION_DELETE);
            delRow.whereForeignKey(field.getJoinForeignKey(), sm);

            for (Iterator itr = rem.iterator(); itr.hasNext();) {
                esm = RelationStrategies.getStateManager(itr.next(), ctx);
                elem.whereForeignKey(delRow, esm);
                rm.flushSecondaryRow(delRow);
            }
        }

        // insert the adds
        Collection add = ct.getAdded();
        if (!add.isEmpty()) {
            Row addRow = rm.getSecondaryRow(field.getTable(),
                Row.ACTION_INSERT);
            addRow.setForeignKey(field.getJoinForeignKey(),
                field.getJoinColumnIO(), sm);

            int seq = ct.getNextSequence();
            Column order = field.getOrderColumn();
            boolean setOrder = field.getOrderColumnIO().isInsertable(order,
                false);
            for (Iterator itr = add.iterator(); itr.hasNext(); seq++) {
                esm = RelationStrategies.getStateManager(itr.next(), ctx);
                elem.setForeignKey(addRow, esm);
                if (setOrder)
                    addRow.setInt(order, seq);
                rm.flushSecondaryRow(addRow);
            }
            if (order != null)
                ct.setNextSequence(seq);
        }
    }

    public void delete(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        Row row = rm.getAllRows(field.getTable(), Row.ACTION_DELETE);
        row.whereForeignKey(field.getJoinForeignKey(), sm);
        rm.flushAllRows(row);
    }

    public Object toDataStoreValue(Object val, JDBCStore store) {
        return RelationStrategies.toDataStoreValue(field.getElementMapping(),
            val, store);
    }

    public Joins join(Joins joins, boolean forceOuter) {
        return field.join(joins, forceOuter, true);
    }

    public Joins joinRelation(Joins joins, boolean forceOuter,
        boolean traverse) {
        ValueMapping elem = field.getElementMapping();
        ClassMapping[] clss = elem.getIndependentTypeMappings();
        if (clss.length != 1) {
            if (traverse)
                throw RelationStrategies.unjoinable(elem);
            return joins;
        }
        if (forceOuter)
            return joins.outerJoinRelation(field.getName(),
                elem.getForeignKey(clss[0]), clss[0], 
                elem.getSelectSubclasses(), false, false);
        return joins.joinRelation(field.getName(), elem.getForeignKey(clss[0]),
            clss[0], elem.getSelectSubclasses(), false, false);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2388.java