error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17278.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17278.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17278.java
text:
```scala
s@@ql).getMessage());

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.jdbc.kernel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.Row;
import org.apache.openjpa.jdbc.sql.RowImpl;
import org.apache.openjpa.jdbc.sql.SQLExceptions;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.util.OpenJPAException;
import org.apache.openjpa.util.OptimisticException;

/**
 * Basic prepared statement manager implementation.
 *
 * @author Abe White
 */
class PreparedStatementManagerImpl 
    implements PreparedStatementManager {

    private final static Localizer _loc = Localizer
        .forPackage(PreparedStatementManagerImpl.class);

    private final JDBCStore _store;
    private final Connection _conn;
    private final DBDictionary _dict;

    // track exceptions
    private final Collection _exceptions = new LinkedList();

    /**
     * Constructor. Supply connection.
     */
    public PreparedStatementManagerImpl(JDBCStore store, Connection conn) {
        _store = store;
        _dict = store.getDBDictionary();
        _conn = conn;
    }

    public Collection getExceptions() {
        return _exceptions;
    }

    public void flush(RowImpl row) {
        try {
            flushInternal(row);
        } catch (SQLException se) {
            _exceptions.add(SQLExceptions.getStore(se, _dict));
        } catch (OpenJPAException ke) {
            _exceptions.add(ke);
        }
    }

    /**
     * Flush the given row.
     */
    private void flushInternal(RowImpl row) throws SQLException {
        // can't batch rows with auto-inc columns
        Column[] autoAssign = null;
        if (row.getAction() == Row.ACTION_INSERT)
            autoAssign = row.getTable().getAutoAssignedColumns();

        // prepare statement
        String sql = row.getSQL(_dict);
        PreparedStatement stmnt = _conn.prepareStatement(sql);

        // setup parameters and execute statement
        row.flush(stmnt, _dict, _store);
        try {
            int count = stmnt.executeUpdate();
            if (count != 1) {
                Object failed = row.getFailedObject();
                if (failed != null)
                    _exceptions.add(new OptimisticException(failed));
                else if (row.getAction() == Row.ACTION_INSERT)
                    throw new SQLException(_loc.get(
                        "update-failed-no-failed-obj", String.valueOf(count),
                        sql));
            }
        } catch (SQLException se) {
            throw SQLExceptions.getStore(se, row.getFailedObject(), _dict);
        } finally {
            try {
                stmnt.close();
            } catch (SQLException se) {
            }
        }

        // set auto assign values
        if (autoAssign != null && autoAssign.length > 0
            && row.getPrimaryKey() != null) {
            OpenJPAStateManager sm = row.getPrimaryKey();
            ClassMapping mapping = (ClassMapping) sm.getMetaData();
            Object val;
            for (int i = 0; i < autoAssign.length; i++) {
                val = _dict.getGeneratedKey(autoAssign[i], _conn);
                mapping.assertJoinable(autoAssign[i]).setAutoAssignedValue(sm,
                    _store, autoAssign[i], val);
            }
        }
    }

    public void flush() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17278.java