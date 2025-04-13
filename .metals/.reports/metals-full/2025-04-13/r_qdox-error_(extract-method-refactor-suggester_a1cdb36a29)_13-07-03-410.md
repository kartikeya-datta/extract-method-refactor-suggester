error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18007.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18007.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18007.java
text:
```scala
i@@f (type != TYPE_TRANSACTIONAL && type != TYPE_CONTIGUOUS)

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
import java.sql.SQLException;
import javax.sql.DataSource;

import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.schema.SchemaGroup;
import org.apache.openjpa.jdbc.sql.SQLExceptions;
import org.apache.openjpa.kernel.StoreContext;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.OpenJPAException;
import org.apache.openjpa.util.StoreException;

/**
 * Abstract sequence implementation. Handles obtaining the proper
 * connection to used based on whether the sequence is transactional and
 * whether a second datasource is configured.
 *
 * @author Abe White
 */
public abstract class AbstractJDBCSeq
    implements JDBCSeq {

    protected int type = TYPE_DEFAULT;
    protected Object current = null;

    /**
     * Records the sequence type.
     */
    public void setType(int type) {
        this.type = type;
    }

    public Object next(StoreContext ctx, ClassMetaData meta) {
        JDBCStore store = getStore(ctx);
        try {
            current = nextInternal(store, (ClassMapping) meta);
            return current;
        } catch (OpenJPAException ke) {
            throw ke;
        } catch (SQLException se) {
            throw SQLExceptions.getStore(se, store.getDBDictionary());
        } catch (Exception e) {
            throw new StoreException(e);
        }
    }

    public Object current(StoreContext ctx, ClassMetaData meta) {
        JDBCStore store = getStore(ctx);
        try {
            return currentInternal(store, (ClassMapping) meta);
        } catch (OpenJPAException ke) {
            throw ke;
        } catch (SQLException se) {
            throw SQLExceptions.getStore(se, store.getDBDictionary());
        } catch (Exception e) {
            throw new StoreException(e);
        }
    }

    public void allocate(int additional, StoreContext ctx, ClassMetaData meta) {
        JDBCStore store = getStore(ctx);
        try {
            allocateInternal(additional, store, (ClassMapping) meta);
        } catch (OpenJPAException ke) {
            throw ke;
        } catch (SQLException se) {
            throw SQLExceptions.getStore(se, store.getDBDictionary());
        } catch (Exception e) {
            throw new StoreException(e);
        }
    }

    /**
     * No-op.
     */
    public void addSchema(ClassMapping mapping, SchemaGroup group) {
    }

    /**
     * No-op.
     */
    public void close() {
    }

    /**
     * Return the next sequence object.
     */
    protected abstract Object nextInternal(JDBCStore store,
        ClassMapping mapping)
        throws Exception;

    /**
     * Return the current sequence object. By default returns the last
     * sequence value used, or null if no sequence values have been requested
     * yet. Default implementation is not threadsafe.
     */
    protected Object currentInternal(JDBCStore store, ClassMapping mapping)
        throws Exception {
        return current;
    }

    /**
     * Allocate additional sequence values. Does nothing by default.
     */
    protected void allocateInternal(int additional, JDBCStore store,
        ClassMapping mapping)
        throws Exception {
    }

    /**
     * Extract the store from the given context.
     */
    private JDBCStore getStore(StoreContext ctx) {
        return (JDBCStore) ctx.getStoreManager().getInnermostDelegate();
    }

    /**
     * Return the connection to use based on the type of sequence. This
     * connection will automatically be closed; do not close it.
     */
    protected Connection getConnection(JDBCStore store)
        throws SQLException {
        if (type == TYPE_TRANSACTIONAL || type == TYPE_CONTIGUOUS)
            return store.getConnection();

        JDBCConfiguration conf = store.getConfiguration();
        DataSource ds = conf.getDataSource2(store.getContext());
        Connection conn = ds.getConnection();
        if (conn.getAutoCommit())
            conn.setAutoCommit(false);
        return conn;
    }

    /**
     * Close the current connection.
     */
    protected void closeConnection(Connection conn) {
        if (conn == null)
            return;

        try {
            if (type == TYPE_TRANSACTIONAL || type == TYPE_CONTIGUOUS)
                conn.commit();
        } catch (SQLException se) {
            throw SQLExceptions.getStore(se);
        } finally {
            try { conn.close(); } catch (SQLException se) {}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18007.java