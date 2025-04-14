error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15598.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15598.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15598.java
text:
```scala
i@@nt idx = 0;

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

import java.sql.*;
import java.util.*;

import org.apache.openjpa.lib.util.*;

import org.apache.openjpa.kernel.*;
import org.apache.openjpa.util.*;
import org.apache.openjpa.meta.*;
import org.apache.openjpa.jdbc.meta.*;
import org.apache.openjpa.jdbc.kernel.*;
import org.apache.openjpa.jdbc.schema.*;
import org.apache.openjpa.jdbc.sql.*;

/**
 * <p>Mapping for a collection of values in a separate table controlled by a
 * {@link ValueHandler}.</p>
 *
 * @author Abe White
 * @since 0.4.0, 1.1.0
 */
public class HandlerCollectionTableFieldStrategy
    extends StoreCollectionFieldStrategy
    implements LRSCollectionFieldStrategy {

    private static final Localizer _loc = Localizer.forPackage
        (HandlerCollectionTableFieldStrategy.class);

    private Column[] _cols = null;
    private ColumnIO _io = null;
    private boolean _load = false;
    private boolean _lob = false;
    private boolean _embed = false;

    public FieldMapping getFieldMapping() {
        return field;
    }

    public ClassMapping[] getIndependentElementMappings(boolean traverse) {
        return ClassMapping.EMPTY_MAPPINGS;
    }

    public Column[] getElementColumns(ClassMapping elem) {
        return _cols;
    }

    public ForeignKey getJoinForeignKey(ClassMapping elem) {
        return field.getJoinForeignKey();
    }

    public void selectElement(Select sel, ClassMapping elem, JDBCStore store,
        JDBCFetchConfiguration fetch, int eagerMode, Joins joins) {
        sel.select(_cols, joins);
    }

    public Object loadElement(OpenJPAStateManager sm, JDBCStore store,
        JDBCFetchConfiguration fetch, Result res, Joins joins)
        throws SQLException {
        return HandlerStrategies.loadObject(field.getElementMapping(),
            sm, store, fetch, res, joins, _cols, _load);
    }

    protected Joins join(Joins joins, ClassMapping elem) {
        return join(joins, false);
    }

    public Joins joinElementRelation(Joins joins, ClassMapping elem) {
        return joinRelation(joins, false, false);
    }

    protected Proxy newLRSProxy() {
        return new LRSProxyCollection(this);
    }

    public void map(boolean adapt) {
        if (field.getTypeCode() != JavaTypes.COLLECTION
            && field.getTypeCode() != JavaTypes.ARRAY)
            throw new MetaDataException(_loc.get("not-coll", field));

        assertNotMappedBy();
        field.getValueInfo().assertNoSchemaComponents(field, !adapt);
        field.getKeyMapping().getValueInfo().assertNoSchemaComponents
            (field.getKey(), !adapt);

        ValueMapping elem = field.getElementMapping();
        if (elem.getHandler() == null)
            throw new MetaDataException(_loc.get("no-handler", elem));

        field.mapJoin(adapt, true);
        _io = new ColumnIO();
        _cols = HandlerStrategies.map(elem, "element", _io, adapt);

        FieldMappingInfo finfo = field.getMappingInfo();
        Column orderCol = finfo.getOrderColumn(field, field.getTable(), adapt);
        field.setOrderColumn(orderCol);
        field.setOrderColumnIO(finfo.getColumnIO());
        field.mapPrimaryKey(adapt);
    }

    public void initialize() {
        for (int i = 0; !_lob && i < _cols.length; i++)
            _lob = _cols[i].isLob();

        ValueMapping elem = field.getElementMapping();
        _embed = elem.getEmbeddedMetaData() != null;
        _load = elem.getHandler().objectValueRequiresLoad(elem);
    }

    public void insert(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        insert(sm, store, rm, sm.fetchObject(field.getIndex()));
    }

    private void insert(OpenJPAStateManager sm, JDBCStore store, RowManager rm,
        Object vals)
        throws SQLException {
        Collection coll;
        if (field.getTypeCode() == JavaTypes.ARRAY)
            coll = JavaTypes.toList(vals, field.getElement().getType(),
                false);
        else
            coll = (Collection) vals;
        if (coll == null || coll.isEmpty())
            return;

        Row row = rm.getSecondaryRow(field.getTable(), Row.ACTION_INSERT);
        row.setForeignKey(field.getJoinForeignKey(), field.getJoinColumnIO(),
            sm);

        ValueMapping elem = field.getElementMapping();
        Column order = field.getOrderColumn();
        boolean setOrder = field.getOrderColumnIO().isInsertable(order, false);
        int idx = (setOrder && order != null) ? order.getBase() : 0;
        for (Iterator itr = coll.iterator(); itr.hasNext(); idx++) {
            HandlerStrategies.set(elem, itr.next(), store, row, _cols,
                _io, true);
            if (setOrder)
                row.setInt(order, idx);
            rm.flushSecondaryRow(row);
        }
    }

    public void update(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
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
            insert(sm, store, rm, obj);
            return;
        }

        // delete the removes
        ValueMapping elem = field.getElementMapping();
        Collection rem = ct.getRemoved();
        if (!rem.isEmpty()) {
            Row delRow = rm.getSecondaryRow(field.getTable(),
                Row.ACTION_DELETE);
            delRow.whereForeignKey(field.getJoinForeignKey(), sm);
            for (Iterator itr = rem.iterator(); itr.hasNext();) {
                HandlerStrategies.where(elem, itr.next(), store, delRow,
                    _cols);
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
                HandlerStrategies.set(elem, itr.next(), store, addRow, _cols,
                    _io, true);
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

    public int supportsSelect(Select sel, int type, OpenJPAStateManager sm,
        JDBCStore store, JDBCFetchConfiguration fetch) {
        // can't do any combined select with lobs, since they don't allow
        // select distinct.  cant select eager parallel on embedded, because
        // during parallel result processing the owning sm won't be available
        // for each elem
        if (_lob || (_embed && type == Select.EAGER_PARALLEL))
            return 0;
        return super.supportsSelect(sel, type, sm, store, fetch);
    }

    public Object toDataStoreValue(Object val, JDBCStore store) {
        return HandlerStrategies.toDataStoreValue(field.getElementMapping(),
            val, _cols, store);
    }

    public Joins join(Joins joins, boolean forceOuter) {
        return field.join(joins, forceOuter, true);
    }

    public Joins joinRelation(Joins joins, boolean forceOuter,
        boolean traverse) {
        if (traverse)
            HandlerStrategies.assertJoinable(field.getElementMapping());
        return joins;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15598.java