error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3290.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3290.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3290.java
text:
```scala
(@@row, field.getColumns()[0], ob);

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

import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.ValueMappingInfo;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.sql.PostgresDictionary;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.Row;
import org.apache.openjpa.jdbc.sql.RowManager;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.JavaTypes;

/**
 * Direct mapping from a stream value to a column.
 *
 * @author Ignacio Andreu
 * @since 1.1.0
 */
public class LobFieldStrategy extends AbstractFieldStrategy {

    private int fieldType;
    private boolean isBlob;

    public void map(boolean adapt) {
        assertNotMappedBy();
        field.mapJoin(adapt, false);
        field.getKeyMapping().getValueInfo().assertNoSchemaComponents
            (field.getKey(), !adapt);
        field.getElementMapping().getValueInfo().assertNoSchemaComponents
            (field.getElement(), !adapt);
        field.setStream(true);
        ValueMappingInfo vinfo = field.getValueInfo();
        vinfo.assertNoJoin(field, true);
        vinfo.assertNoForeignKey(field, !adapt);
        Column tmpCol = new Column();
        tmpCol.setName(field.getName());
        tmpCol.setType(fieldType);
        tmpCol.setJavaType(field.getTypeCode());
        
        tmpCol.setSize(-1);

        Column[] cols = vinfo.getColumns(field, field.getName(),
            new Column[]{ tmpCol }, field.getTable(), adapt);

        field.setColumns(cols);
        field.setColumnIO(vinfo.getColumnIO());
        field.mapConstraints(field.getName(), adapt);
        field.mapPrimaryKey(adapt);
    }

    public Boolean isCustomInsert(OpenJPAStateManager sm, JDBCStore store) {
        return null;
    }

    public void delete(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        Select sel = createSelect(sm, store);
        store.getDBDictionary().deleteStream(store, sel);
    }
    
    public void insert(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        Object ob = toDataStoreValue(sm.fetchObjectField
            (field.getIndex()), store);
        Row row = field.getRow(sm, store, rm, Row.ACTION_INSERT);
        if (field.getColumnIO().isInsertable(0, ob == null)) {
            Select sel = createSelect(sm, store);
            if (isBlob) {
                store.getDBDictionary().insertBlobForStreamingLoad
                    (row, field.getColumns()[0], store, ob, sel);
            } else {
                store.getDBDictionary().insertClobForStreamingLoad
                    (row, field.getColumns()[0], ob);
            }
        }
    }

    public void customInsert(OpenJPAStateManager sm, JDBCStore store)
        throws SQLException {
        Object ob = toDataStoreValue(sm.fetchObjectField
            (field.getIndex()), store);
        if (field.getColumnIO().isInsertable(0, ob == null)) {
            if (ob != null) {
                Select sel = createSelect(sm, store);
                if (isBlob) {
                    store.getDBDictionary().updateBlob
                        (sel, store, (InputStream)ob);
                } else {
                    store.getDBDictionary().updateClob
                        (sel, store, (Reader)ob);
                }
            }
        }
    }
    
    public Boolean isCustomUpdate(OpenJPAStateManager sm, JDBCStore store) {
        return null;
    }

    public void update(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        Object ob = toDataStoreValue(sm.fetchObjectField
            (field.getIndex()), store);
        if (field.getColumnIO().isUpdatable(0, ob == null)) {
            Row row = field.getRow(sm, store, rm, Row.ACTION_UPDATE);
            Select sel = createSelect(sm, store);
            if (isBlob) {
                store.getDBDictionary().insertBlobForStreamingLoad
                    (row, field.getColumns()[0], store, ob, sel);
            } else {
                store.getDBDictionary().insertClobForStreamingLoad
                    (row, field.getColumns()[0], sel);
            }
        }
    }

    public void customUpdate(OpenJPAStateManager sm, JDBCStore store)
        throws SQLException {
        Object ob = toDataStoreValue(sm.fetchObjectField
                (field.getIndex()), store);
        if (field.getColumnIO().isUpdatable(0, ob == null)) {
            if (ob != null) {
                Select sel = createSelect(sm, store);
                if (isBlob) {
                    store.getDBDictionary().updateBlob
                        (sel, store, (InputStream)ob);
                } else {
                    store.getDBDictionary().updateClob
                        (sel, store, (Reader)ob);
                }
            }
        }
    }

    public int supportsSelect(Select sel, int type, OpenJPAStateManager sm,
        JDBCStore store, JDBCFetchConfiguration fetch) {
        if (type == Select.TYPE_JOINLESS && sel.isSelected(field.getTable()))
            return 1;
        return 0;
    }

    public int select(Select sel, OpenJPAStateManager sm, JDBCStore store,
        JDBCFetchConfiguration fetch, int eagerMode) {
        sel.select(field.getColumns()[0], field.join(sel));
        return 1;
    }

    public void load(OpenJPAStateManager sm, JDBCStore store,
        JDBCFetchConfiguration fetch, Result res) throws SQLException {
        Column col = field.getColumns()[0];
        if (res.contains(col)) {
            if (isBlob) {
                sm.storeObject(field.getIndex(), res.getLOBStream(store, col));
            } else {
                sm.storeObject(field.getIndex(), res.getCharacterStream(col));
            }
        }
    }

    protected void assertNotMappedBy() {
        if (field != null && field.getMappedBy() != null)
            throw new UnsupportedOperationException();
    }

    public void setFieldMapping(FieldMapping owner) {
        field = owner;
        if (owner.getElementMapping().getMappingRepository().getDBDictionary()
            instanceof PostgresDictionary) {
            fieldType = Types.INTEGER;
            isBlob = true;
            field.setTypeCode(JavaTypes.INT);
        } else {
            if (owner.getType().isAssignableFrom(InputStream.class)) {
                isBlob = true;
                fieldType = Types.BLOB;
            } else if (owner.getType().isAssignableFrom(Reader.class)) {
                isBlob = false;
                fieldType = Types.CLOB;
            }
        }
    }

    private Select createSelect(OpenJPAStateManager sm, JDBCStore store) {
        Select sel = store.getSQLFactory().newSelect();
        sel.select(field.getColumns()[0]);
        sel.selectPrimaryKey(field.getDefiningMapping());
        sel.wherePrimaryKey
            (sm.getObjectId(), field.getDefiningMapping(), store);
        sel.setLob(true);
        return sel;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3290.java