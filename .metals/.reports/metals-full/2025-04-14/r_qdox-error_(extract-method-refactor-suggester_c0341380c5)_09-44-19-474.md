error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/70.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/70.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/70.java
text:
```scala
i@@f (isIBMJVM()) {

/*
 * Derby - Class org.apache.derbyTesting.functionTests.tests.engine.LockInterruptTest
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.derbyTesting.functionTests.tests.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.CleanDatabaseTestSetup;
import org.apache.derbyTesting.junit.DatabasePropertyTestSetup;
import org.apache.derbyTesting.junit.JDBC;
import org.apache.derbyTesting.junit.SystemPropertyTestSetup;
import org.apache.derbyTesting.junit.TestConfiguration;

/**
 * Test that other threads are able to proceed if one thread is interrupted
 * when it's waiting for a lock. Regression test case for DERBY-4711.
 */
public class LockInterruptTest extends BaseJDBCTestCase {
    /** SQLState for exception thrown because of interrupts. */
    private final static String INTERRUPTED = "08000";

    /** Lock timeout in seconds used in this test. */
    private final static int LOCK_TIMEOUT = 60;

    /** Deadlock timeout in seconds used in this test. */
    private final static int DEADLOCK_TIMEOUT = LOCK_TIMEOUT / 2;

    public LockInterruptTest(String name) {
        super(name);
    }

    public static Test suite() {
        
        if (! isSunJVM()) {
            // DERBY-4463 test fails on IBM VM 1.5.
            // It's fixed in IBM VM 1.6 SR9 and above.
            // Remove this condition when that issue is solved in IBM VM 1.5 SR13.
            if (getSystemProperty("java.version").startsWith("1.5.0"))
            {
                println("Test skipped for this VM, cf. DERBY-4463");
                return new TestSuite("empty LockInterruptTest");
            }
        }
        
        // Only run in embedded mode since we cannot interrupt the engine
        // thread from the network client.
        Test test = TestConfiguration.embeddedSuite(LockInterruptTest.class);

        // Set the lock timeout to a known value so that we know what to
        // expect for timeouts.
        test = DatabasePropertyTestSetup.setLockTimeouts(
                test, DEADLOCK_TIMEOUT, LOCK_TIMEOUT);
        Properties syspros = new Properties();
        //Derby-4856 interrupt error create thread dump and diagnostic
        //info. Add property to avoid the information.
        syspros.put("derby.stream.error.extendedDiagSeverityLevel", "50000");
        test = new SystemPropertyTestSetup(test, syspros, true);
        

        return new CleanDatabaseTestSetup(test);
    }

    public void testInterruptLockWaiter() throws Exception {
        setAutoCommit(false);
        Statement s = createStatement();
        s.executeUpdate("create table derby4711(x int)");
        commit();

        // Obtain a table lock in order to block the waiter threads.
        s.executeUpdate("lock table derby4711 in share mode");

        // Create first waiter thread.
        Waiter t1 = new Waiter();
        t1.start();
        Thread.sleep(2000); // give t1 time to become the first waiter

        // Create second waiter thread.
        Waiter t2 = new Waiter();
        t2.start();
        Thread.sleep(2000); // give t2 time to enter the wait queue

        // Now that the queue of waiters has been set up, interrupt the
        // first thread and give the interrupt a little time to do its work.
        t1.interrupt();
        Thread.sleep(1000);

        // Release the table lock to allow the waiters to proceed.
        commit();

        // Wait for the threads to complete before checking their state.
        t1.join();
        t2.join();

        // The first thread should fail because it was interrupted.
        Throwable e1 = t1.throwable;
        assertNotNull("First thread should fail because of interrupt", e1);
        if (!(e1 instanceof SQLException)) {
            fail("Unexpected exception from first thread", e1);
        }
        assertSQLState(INTERRUPTED, (SQLException) e1);

        if (hasInterruptibleIO()) {
            println("Skipping assert for t1.InterruptFlagSetOnThrow due " +
                    " to interruptible IO.");
            println("This is default on Solaris/Sun Java <= 1.6, use " +
                    "-XX:-UseVMInterruptibleIO if available.");
            // The flag will may get swallowed
        } else {
            assertTrue(t1.InterruptFlagSetOnThrow);
        }

        // The second thread should be able to complete successfully.
        Throwable e2 = t2.throwable;
        if (e2 != null) {
            fail("Unexpected exception from second thread", e2);
        }

        // And the second thread should be able to complete in less time than
        // the deadlock timeout (before DERBY-4711, it would wait for a
        // timeout before obtaining the lock, even if the lock was available
        // long before).
        if (t2.elapsedTime >= DEADLOCK_TIMEOUT * 1000) {
            fail("Second thread needed " + t2.elapsedTime +
                 " ms to complete. Probably stuck waiting for a lock.");
        }

        // Expect that the second thread managed to insert a row.
        JDBC.assertSingleValueResultSet(
                s.executeQuery("select * from derby4711"), "1");
    }

    /**
     * Thread class that opens a new connection and attempts to insert a
     * row into a table.
     */
    private class Waiter extends Thread {
        private final Connection c;
        private final PreparedStatement ps;

        private Throwable throwable;
        private boolean InterruptFlagSetOnThrow;
        private long elapsedTime;

        private Waiter() throws SQLException {
            c = openDefaultConnection();
            ps = c.prepareStatement("insert into derby4711 values 1");
        }

        public void run() {
            try {
                runWaiter();
            } catch (Throwable t) {
                throwable = t;
                InterruptFlagSetOnThrow = interrupted(); // clears also
            }
        };

        private void runWaiter() throws SQLException {
            long start = System.currentTimeMillis();
            try {
                ps.executeUpdate();
            } finally {
                ps.close();
                c.close();
            }
            elapsedTime = System.currentTimeMillis() - start;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/70.java