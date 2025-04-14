error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9906.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9906.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9906.java
text:
```scala
J@@DBCFetchConfiguration fetch);

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

import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.Joins;
import org.apache.openjpa.jdbc.sql.SQLFactory;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.StoreContext;
import org.apache.openjpa.util.Id;

/**
 * Represents the JDBC store.
 *
 * @author Abe White
 * @since 4.0
 */
public interface JDBCStore {

    /**
     * Current persistence context.
     */
    public StoreContext getContext();

    /**
     * Return the configuration for this runtime.
     */
    public JDBCConfiguration getConfiguration();

    /**
     * Return the dictionary in use.
     */
    public DBDictionary getDBDictionary();

    /**
     * Return the SQL factory for this runtime.
     */
    public SQLFactory getSQLFactory();

    /**
     * If the lock manager in use is a {@link JDBCLockManager}, return it.
     */
    public JDBCLockManager getLockManager();

    /**
     * Return a SQL connection to the database.
     * The <code>close</code> method should always be called on the connection
     * to free any resources it is using. When appropriate, the close
     * method is implemented as a no-op.
     */
    public Connection getConnection();

    /**
     * Return the current default fetch configuration.
     */
    public JDBCFetchConfiguration getFetchConfiguration();

    /**
     * Create a new datastore identity object from the given id value and
     * mapping.
     */
    public Id newDataStoreId(long id, ClassMapping mapping, boolean subs);

    /**
     * Find the object with the given oid. Convenience method on top of
     * the store's persistence context.
     *
     * @param vm the mapping holding this oid, or null if not applicable
     */
    public Object find(Object oid, ValueMapping vm,
        JDBCFetchState fetchState);

    /**
     * Makes sure all subclasses of the given type are loaded in the JVM.
     * This is usually done automatically.
     */
    public void loadSubclasses(ClassMapping mapping);

    /**
     * Add WHERE conditions to the given select limiting the returned results
     * to the given mapping type, possibly including subclasses.
     *
     * @return true if the mapping was joined down to its base class
     * in order to add the conditions
     */
    public boolean addClassConditions(Select sel, ClassMapping mapping,
        boolean subs, Joins joins);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9906.java