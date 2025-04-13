error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1279.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1279.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1279.java
text:
```scala
s@@uite.addTest(ExistsWithSubqueriesTest.suite());

/*

   Derby - Class org.apache.derbyTesting.functionTests.tests.lang._Suite

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License
*/
package org.apache.derbyTesting.functionTests.tests.lang;

import org.apache.derbyTesting.functionTests.suites.XMLSuite;
import org.apache.derbyTesting.functionTests.tests.nist.NistScripts;
import org.apache.derbyTesting.junit.BaseTestCase;
import org.apache.derbyTesting.junit.JDBC;

import junit.framework.Test; 
import junit.framework.TestSuite;

/**
 * Suite to run all JUnit tests in this package:
 * org.apache.derbyTesting.functionTests.tests.lang
 * <P>
 * All tests are run "as-is", just as if they were run
 * individually. Thus this test is just a collection
 * of all the JUNit tests in this package (excluding itself).
 * While the old test harness is in use, some use of decorators
 * may be required.
 *
 */
public class _Suite extends BaseTestCase  {

	/**
	 * Use suite method instead.
	 */
	private _Suite(String name) {
		super(name);
	}

	public static Test suite() {

		TestSuite suite = new TestSuite("lang");
        
        // DERBY-1315 and DERBY-1735 need to be addressed
        // before re-enabling this test as it's memory use is
        // different on different vms leading to failures in
        // the nightly runs.
        // suite.addTest(largeCodeGen.suite());

        suite.addTest(AnsiTrimTest.suite());
        suite.addTest(CreateTableFromQueryTest.suite());
        suite.addTest(DatabaseClassLoadingTest.suite());
        suite.addTest(DynamicLikeOptimizationTest.suite());
        suite.addTest(ExistsWithSetOpsTest.suite());
        suite.addTest(GrantRevokeTest.suite());
        suite.addTest(GroupByExpressionTest.suite());
		suite.addTest(LangScripts.suite());
        suite.addTest(MathTrigFunctionsTest.suite());
        suite.addTest(PrepareExecuteDDL.suite());
        suite.addTest(RoutineSecurityTest.suite());
        suite.addTest(RoutineTest.suite());
        suite.addTest(SQLAuthorizationPropTest.suite());
        suite.addTest(StatementPlanCacheTest.suite());
        suite.addTest(StreamsTest.suite());
        suite.addTest(TimeHandlingTest.suite());
        suite.addTest(TriggerTest.suite());
        suite.addTest(VTITest.suite());
        suite.addTest(SysDiagVTIMappingTest.suite());
        suite.addTest(UpdatableResultSetTest.suite());
        suite.addTest(CurrentOfTest.suite());
	    suite.addTest(CursorTest.suite());
        suite.addTest(CastingTest.suite());
        suite.addTest(ScrollCursors2Test.suite());
        suite.addTest(NullIfTest.suite());
        suite.addTest(InListMultiProbeTest.suite());
        suite.addTest(SecurityPolicyReloadingTest.suite());
        suite.addTest(CurrentOfTest.suite());
        suite.addTest(UnaryArithmeticParameterTest.suite());
        suite.addTest(HoldCursorTest.suite());
        suite.addTest(ShutdownDatabaseTest.suite());
        suite.addTest(StalePlansTest.suite());
        suite.addTest(SystemCatalogTest.suite());
        suite.addTest(ForBitDataTest.suite());
        suite.addTest(DistinctTest.suite());
        suite.addTest(GroupByTest.suite());
        suite.addTest(UpdateCursorTest.suite());
        suite.addTest(CoalesceTest.suite());
        suite.addTest(ProcedureInTriggerTest.suite());
	    suite.addTest(ForUpdateTest.suite());
        suite.addTest(CollationTest.suite());
        suite.addTest(CollationTest2.suite());
        suite.addTest(ScrollCursors1Test.suite());
        suite.addTest(SimpleTest.suite());
        suite.addTest(GrantRevokeDDLTest.suite());
        suite.addTest(ReleaseCompileLocksTest.suite());
        suite.addTest(ErrorCodeTest.suite());
        suite.addTest(TimestampArithTest.suite());
        suite.addTest(SpillHashTest.suite());
        suite.addTest(CaseExpressionTest.suite());
        suite.addTest(AggregateClassLoadingTest.suite());

        // Add the XML tests, which exist as a separate suite
        // so that users can "run all XML tests" easily.
        suite.addTest(XMLSuite.suite());
         
        // Add the NIST suite in from the nist package since
        // it is a SQL language related test.
        suite.addTest(NistScripts.suite());
        
        // Add the java tests that run using a master
        // file (ie. partially converted).
        suite.addTest(LangHarnessJavaTest.suite());
        		
		// Tests that are compiled using 1.4 target need to
		// be added this way, otherwise creating the suite
		// will throw an invalid class version error
		if (JDBC.vmSupportsJDBC3() || JDBC.vmSupportsJSR169())
		{
		}
        suite.addTest(ResultSetsFromPreparedStatementTest.suite());

        // tests that do not run with JSR169
        if (JDBC.vmSupportsJDBC3())  
        {
            // test uses triggers interwoven with other tasks
            // triggers may cause a generated class which calls 
            // java.sql.DriverManager, which will fail with JSR169.
            // also, test calls procedures which use DriverManager
            // to get the default connection.
            suite.addTest(GrantRevokeDDLTest.suite());
        }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1279.java