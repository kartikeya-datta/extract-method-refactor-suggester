error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1518.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1518.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[22,1]

error in qdox parser
file content:
```java
offset: 926
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1518.java
text:
```scala
class DropDatabaseSetup extends BaseTestSetup {

/*
 *
 * Derby - Class org.apache.derbyTesting.junit.DropDatabaseSetup
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 */
p@@ackage org.apache.derbyTesting.junit;

import java.io.File;
import java.security.AccessController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import junit.extensions.TestSetup;
import junit.framework.Test;

/**
 * Shutdown and drop the database identified by the logical
 * name passed in when creating this decorator.
 *
 */
class DropDatabaseSetup extends TestSetup {

    final String logicalDBName;
    DropDatabaseSetup(Test test, String logicalDBName) {
        super(test);
        this.logicalDBName = logicalDBName;
     }
    
    /**
     * Shutdown the database and then remove all of its files.
     */
    protected void tearDown() throws Exception {
        
        TestConfiguration config = TestConfiguration.getCurrent();
        
        // Ensure the database is booted
        // since that is what shutdownDatabase() requires.
        boolean shutdown;
        try {
            config.openConnection(logicalDBName).close();
            shutdown = true;
        } catch (SQLException e) {
            // If the database cannot be booted due
            // to some restrictions such as authentication
            // or encrypted (ie here we don't know the 
            // correct authentication tokens, then it's
            // ok since we just want it shutdown anyway!
            if ("XJ040".equals(e.getSQLState()))
            {
                shutdown = false;
            }
            else
            {
                throw e;
            }
        }
        if (shutdown)
        {
            DataSource ds = JDBCDataSource.getDataSourceLogical(logicalDBName);
            JDBCDataSource.shutdownDatabase(ds);
        }

        removeDatabase();
    }

    void removeDatabase()
    {
        TestConfiguration config = TestConfiguration.getCurrent();
        String dbName = config.getPhysicalDatabaseName(logicalDBName);
        dbName = dbName.replace('/', File.separatorChar);
        String dsh = BaseTestCase.getSystemProperty("derby.system.home");
        if (dsh == null) {
            fail("not implemented");
        } else {
            dbName = dsh + File.separator + dbName;
        }
        removeDirectory(dbName);
    }


    static void removeDirectory(String path)
    {
        final File dir = new File(path);
        AccessController.doPrivileged(new java.security.PrivilegedAction() {

            public Object run() {
                removeDir(dir);
                return null;
            }
        });
        
    }

    private static void removeDir(File dir) {
        
        // Check if anything to do!
        // Database may not have been created.
        if (!dir.exists())
            return;

        String[] list = dir.list();

        // Some JVMs return null for File.list() when the
        // directory is empty.
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File entry = new File(dir, list[i]);

                if (entry.isDirectory()) {
                    removeDir(entry);
                } else {
                    assertTrue(entry.getPath(), entry.delete());
                }
            }
        }

        assertTrue(dir.getPath(), dir.delete());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1518.java