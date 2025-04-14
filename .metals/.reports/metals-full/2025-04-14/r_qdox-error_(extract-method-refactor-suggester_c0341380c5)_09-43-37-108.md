error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14308.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14308.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14308.java
text:
```scala
protected R@@esult _res = null;

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
package org.apache.openjpa.jdbc.kernel;

import java.sql.SQLException;

import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.SQLExceptions;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.jdbc.sql.SelectExecutor;
import org.apache.openjpa.lib.rop.ResultObjectProvider;
import org.apache.openjpa.util.StoreException;

/**
 * Abstract provider implementation wrapped around a {@link Select}.
 *
 * @author Abe White
 * @nojavadoc
 */
public abstract class SelectResultObjectProvider
    implements ResultObjectProvider {

    private final SelectExecutor _sel;
    private final JDBCStore _store;
    private final JDBCFetchConfiguration _fetch;
    private Result _res = null;
    private int _size = -1;
    private Boolean _ra = null;

    /**
     * Constructor.
     *
     * @param sel the select to execute
     * @param store the store to delegate loading to
     * @param fetch the fetch configuration, or null for the default
     */
    public SelectResultObjectProvider(SelectExecutor sel, JDBCStore store,
        JDBCFetchConfiguration fetch) {
        _sel = sel;
        _store = store;
        _fetch = fetch;
    }

    public SelectExecutor getSelect() {
        return _sel;
    }

    public JDBCStore getStore() {
        return _store;
    }

    public JDBCFetchConfiguration getFetchConfiguration() {
        return _fetch;
    }

    public Result getResult() {
        return _res;
    }

    public boolean supportsRandomAccess() {
        if (_ra == null) {
            boolean ra;
            if (_res != null) {
                try {
                    ra = _res.supportsRandomAccess();
                } catch (SQLException se) {
                    throw SQLExceptions.getStore(se, _store.getDBDictionary());
                }
            } else
                ra = _sel.supportsRandomAccess(_fetch.getReadLockLevel() > 0);
            _ra = (ra) ? Boolean.TRUE : Boolean.FALSE;
        }
        return _ra.booleanValue();
    }

    public void open()
        throws SQLException {
        _res = _sel.execute(_store, _fetch);
    }

    public boolean next()
        throws SQLException {
        return _res.next();
    }

    public boolean absolute(int pos)
        throws SQLException {
        return _res.absolute(pos);
    }

    public int size()
        throws SQLException {
        if (_size == -1) {
            // if res is null, don't cache size
            if (_res == null)
                return Integer.MAX_VALUE;

            switch (_fetch.getLRSSize()) {
                case LRSSizes.SIZE_UNKNOWN:
                    _size = Integer.MAX_VALUE;
                    break;
                case LRSSizes.SIZE_LAST:
                    if (supportsRandomAccess())
                        _size = _res.size();
                    else
                        _size = Integer.MAX_VALUE;
                    break;
                default: // query
                    _size = _sel.getCount(_store);
            }
        }
        return _size;
    }

    /**
     * Allow subclasses that know the size to set it; otherwise we calculate
     * it internally.
     */
    protected void setSize(int size) {
        if (_size == -1)
            _size = size;
    }

    public void reset()
        throws SQLException {
        close();
        open();
    }

    public void close() {
        if (_res != null) {
            _res.close();
            _res = null;
        }
    }

    public void handleCheckedException(Exception e) {
        if (e instanceof SQLException)
            throw SQLExceptions.getStore((SQLException) e,
                _store.getDBDictionary());
        throw new StoreException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14308.java