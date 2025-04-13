error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/581.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/581.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/581.java
text:
```scala
p@@ublic class ClientConnectionPoolDataSource extends ClientDataSource

/*

   Derby - Class org.apache.derby.jdbc.ClientConnectionPoolDataSource

   Copyright (c) 2001, 2005 The Apache Software Foundation or its licensors, where applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package org.apache.derby.jdbc;

import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;
import org.apache.derby.client.am.LogWriter;
import org.apache.derby.client.am.SqlException;

/**
 * ClientConnectionPoolDataSource is a factory for PooledConnection objects.
 * An object that implements this interface
 * will typically be registered with a naming service that is based on the
 * Java Naming and Directory Interface (JNDI). Use
 * ClientConnectionPoolDataSource if your application runs under
 * JDBC3.0 or JDBC2.0, that is, on the following Java Virtual Machines:
 * <p/>
 * <UL>
 * <LI> JDBC 3.0 - Java 2 - JDK 1.4, J2SE 5.0
 * <LI> JDBC 2.0 - Java 2 - JDK 1.2,1.3
 * </UL>
 */
public class ClientConnectionPoolDataSource extends ClientBaseDataSource 
                                           implements ConnectionPoolDataSource {
    private static final long serialVersionUID = -539234282156481377L;
    public static final String className__ = "org.apache.derby.jdbc.ClientConnectionPoolDataSource";

    public ClientConnectionPoolDataSource() {
        super();
    }

    // ---------------------------interface methods-------------------------------

    // Attempt to establish a physical database connection that can be used as a pooled connection.
    public PooledConnection getPooledConnection() throws SQLException {
        try
        {
            LogWriter dncLogWriter = super.computeDncLogWriterForNewConnection("_cpds");
            if (dncLogWriter != null) {
                dncLogWriter.traceEntry(this, "getPooledConnection");
            }
            PooledConnection pooledConnection = getPooledConnectionX(dncLogWriter, this, getUser(), getPassword());
            if (dncLogWriter != null) {
                dncLogWriter.traceExit(this, "getPooledConnection", pooledConnection);
            }
            return pooledConnection;
        }
        catch ( SqlException se )
        {
            throw se.getSQLException();
        }
    }

    // Standard method that establishes the initial physical connection using CPDS properties.
    public PooledConnection getPooledConnection(String user, String password) throws SQLException {
        try
        {
            LogWriter dncLogWriter = super.computeDncLogWriterForNewConnection("_cpds");
            if (dncLogWriter != null) {
                dncLogWriter.traceEntry(this, "getPooledConnection", user, "<escaped>");
            }
            PooledConnection pooledConnection = getPooledConnectionX(dncLogWriter, this, user, password);
            if (dncLogWriter != null) {
                dncLogWriter.traceExit(this, "getPooledConnection", pooledConnection);
            }
            return pooledConnection;
        }
        catch ( SqlException se )
        {
            throw se.getSQLException();
        }
    }

    //  method that establishes the initial physical connection
    // using DS properties instead of CPDS properties.
    private PooledConnection getPooledConnectionX(LogWriter dncLogWriter, 
                        ClientBaseDataSource ds, String user, 
                        String password) throws SQLException {
            return ClientDriver.getFactory().newClientPooledConnection(ds,
                    dncLogWriter, user, password);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/581.java