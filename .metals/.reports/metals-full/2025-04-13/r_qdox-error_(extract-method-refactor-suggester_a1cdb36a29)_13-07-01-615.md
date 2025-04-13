error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2568.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2568.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2568.java
text:
```scala
S@@tring property = "derby.storage.indexStats.debug.keepDisposableStats";

/*

   Derby - Class org.apache.derbyTesting.functionTests.tests.store.KeepDisposableStatsPropertyTest

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.apache.derbyTesting.functionTests.tests.store;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.derbyTesting.junit.BaseJDBCTestCase;
import org.apache.derbyTesting.junit.IndexStatsUtil;
import org.apache.derbyTesting.junit.JDBC;
import org.apache.derbyTesting.junit.SystemPropertyTestSetup;
import org.apache.derbyTesting.junit.Utilities;

/**
 * Tests that the debug property used to revert to the previous behavior for
 * dealing with disposable index cardinality statistics works.
 */
public class KeepDisposableStatsPropertyTest
    extends BaseJDBCTestCase {

    public KeepDisposableStatsPropertyTest(String name) {
        super(name);
    }

    /** Verifies the behavior when the property is set to {@code true}. */
    public void testPropertyFalse()
            throws SQLException {
        assertOnSCUI(false);
    }

    /** Verifies the behavior when the property is set to {@code true}. */
    public void testPropertyTrue()
            throws SQLException {
        assertOnSCUI(true);
    }

    /** Verifies that the default for the property is {@code false}. */
    public void testPropertyDefault()
            throws SQLException {
        assertOnSCUI(false);
    }

    /** Runs the real test case. */
    private void assertOnSCUI(boolean keepDisposable)
            throws SQLException {
        IndexStatsUtil stats = new IndexStatsUtil(openDefaultConnection());
        // Create table.
        String TAB = "STAT_SCUI";
        dropTable(TAB);
        Statement stmt = createStatement();
        stmt.executeUpdate("create table " + TAB +
                " (id int not null, val int)");
        stats.assertNoStatsTable(TAB);
        PreparedStatement psIns = prepareStatement(
                "insert into " + TAB + " values (?,?)");
        setAutoCommit(false);
        for (int i=0; i < 20; i++) {
            psIns.setInt(1, i);
            psIns.setInt(2, i);
            psIns.executeUpdate();
        }
        commit();
        setAutoCommit(true);
        stmt.executeUpdate("alter table " + TAB + " add constraint PK_" + TAB +
                " primary key(id)");
        stats.assertTableStats(TAB, keepDisposable ? 1  : 0);
        stmt.executeUpdate(
                "create unique index UNIQ_IDX_" + TAB + " ON " + TAB + "(val)");
        stats.assertTableStats(TAB, keepDisposable ? 2  : 0);
        PreparedStatement ps = prepareStatement(
                "call SYSCS_UTIL.SYSCS_UPDATE_STATISTICS('APP', ?, ?)");
        // Update stats for all indexes.
        ps.setString(1, TAB);
        ps.setNull(2, Types.VARCHAR);
        ps.execute();
        stats.assertTableStats(TAB, keepDisposable ? 2  : 0);

        // Update stats for one specific index.
        ps.setString(2, "UNIQ_IDX_" + TAB);
        ps.execute();
        stats.assertTableStats(TAB, keepDisposable ? 2  : 0);

        // Drop statistics.
        stmt.execute("call SYSCS_UTIL.SYSCS_DROP_STATISTICS(" +
                "'APP', '" + TAB + "', null)");
        stats.assertNoStatsTable(TAB);

        // Update and assert again, this time in the reverse order.
        ps.execute();
        stats.assertTableStats(TAB, keepDisposable ? 1  : 0);
        ps.setNull(2, Types.VARCHAR);
        ps.execute();
        stats.assertTableStats(TAB, keepDisposable ? 2  : 0);
        IndexStatsUtil.IdxStats[] oldStats = stats.getStatsTable(TAB);
        
        // Insert more data to trigger an automatic update
        setAutoCommit(false);
        for (int i=20; i < 2000; i++) {
            psIns.setInt(1, i);
            psIns.setInt(2, i);
            psIns.executeUpdate();
        }
        commit();
        setAutoCommit(true);
        JDBC.assertDrainResultsHasData(
                stmt.executeQuery("select count(*) from " + TAB));
        prepareStatement("select * from " + TAB + " where id = ?"); 
        Utilities.sleep(200);
        IndexStatsUtil.IdxStats[] newStats = stats.getStatsTable(TAB);
        assertEquals(oldStats.length, newStats.length);
        for (int i=0; i < oldStats.length; i++) {
            assertEquals(keepDisposable, newStats[i].after(oldStats[i]));
        }

        // Cleanup
        dropTable(TAB);
        stats.release();
    }

    /**
     * Returns a suite where the test is run without specifying the property
     * (use the default value), explicitly setting it to {@code true}, and
     * explicitly setting it to {@code false}.
     */
    public static Test suite() {
        String property = "derby.storage.indexStats.debug.forceOldBehavior";
        TestSuite suite = new TestSuite("KeepDisposableStatsPropertyTestSuite");
        // Test the default (expected to be false).
        suite.addTest(
                new KeepDisposableStatsPropertyTest("testPropertyDefault"));

        // Test setting the property explicitly to true.
        Properties propsOn = new Properties();
        propsOn.setProperty(property, "true");
        TestSuite suiteOn = new TestSuite("Do KeepDisposableStats");
        suiteOn.addTest(
                new KeepDisposableStatsPropertyTest("testPropertyTrue"));
        suite.addTest(new SystemPropertyTestSetup(suiteOn, propsOn, true));

        // Test setting the property explicitly to false.
        Properties propsOff = new Properties();
        propsOff.setProperty(property, "false");
        TestSuite suiteOff = new TestSuite("Don't KeepDisposableStats");
        suiteOff.addTest(
                new KeepDisposableStatsPropertyTest("testPropertyFalse"));
        suite.addTest(new SystemPropertyTestSetup(suiteOff, propsOff, true));

        return suite;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2568.java