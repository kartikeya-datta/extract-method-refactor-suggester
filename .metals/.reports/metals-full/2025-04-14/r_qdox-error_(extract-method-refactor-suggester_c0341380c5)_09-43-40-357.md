error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/18.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/18.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/18.java
text:
```scala
public static v@@oid main(String[] args)

/*
 *
 * Derby - Class org.apache.derbyTesting.functionTests.tests.i18n.LocalizedDisplayScriptTest
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

package org.apache.derbyTesting.functionTests.tests.i18n;

import java.util.Properties;
import java.util.TimeZone;

import org.apache.derby.iapi.tools.i18n.LocalizedResource;
import org.apache.derbyTesting.functionTests.util.ScriptTestCase;
import org.apache.derbyTesting.junit.JDBC;
import org.apache.derbyTesting.junit.SystemPropertyTestSetup;
import org.apache.derbyTesting.junit.TestConfiguration;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * LocalizedDisplayScriptTest runs the ij script LocalizedDisplay.sql
 * and compares the output to a canon file in the standard master package.
 * <BR>
 * Its suite() method returns a set of tests where each test is an instance of
 * this class for an individual script wrapped in a clean database decorator.
 * <BR>
 * It can also be used as a command line program
 *
 */
public final class LocalizedDisplayScriptTest extends ScriptTestCase {

    private static TimeZone defaultTimeZone;
    
    /**
     * Run LocalizedDisplay.sql 
     * <code>
     * example
     * java org.apache.derbyTesting.functionTests.tests.i18n.LocalizedSuite
     * </code>
     */
    public static void main()
    {
        junit.textui.TestRunner.run(getSuite());
    }

    /**
     * Return the suite that runs the Localized script.
     */
    public static Test suite() {
        
        TestSuite suite = new TestSuite("LocalizedDisplay");

        // This test will fail with JSR169, because lack of support for 
        // rs.getBigDecimal() will prevent the localization of numeric and
        // decimal datatypes, and this test includes a decimal datatype
        // (See DERBY-470).
        if (JDBC.vmSupportsJSR169())
            return suite;
        TestSuite localizedEmbeddedTests = new TestSuite("LocalizedDisplay:embedded");
        localizedEmbeddedTests.addTest(getSuite());
        Test embeddedrun = TestConfiguration.singleUseDatabaseDecorator(localizedEmbeddedTests);
        // add the client test
        suite.addTest(embeddedrun);

        // It's not working to have both embedded and client run in the same
        // setting as the database doesn't get deleted until after the suite is done.
        // The second run will go against the already created & encoded database,
        // resulting in localized display by default, and thus a diff with the
        // master.
        // Set up the script's run with the network client
        TestSuite localizedTests = new TestSuite("LocalizedDisplay:client");
        localizedTests.addTest(getSuite());
        Test client = TestConfiguration.clientServerDecorator(
            TestConfiguration.singleUseDatabaseDecorator(localizedTests));
        // add the client test
        suite.addTest(client);

        return suite;
    }

    /*
     * A single JUnit test that runs a single Localized script.
     */
    private LocalizedDisplayScriptTest(String localizedTest){
        super(localizedTest, "EUC_JP");
    }

    /**
     * Return a localized test based on the script name. 
     * The test is surrounded in a decorator that sets up the
     * desired properties which is wrapped in a decorator
     * that cleans the database.
     */
    private static Test getSuite() {
        TestSuite suite = new TestSuite("localized Display");
        Properties uiProps = new Properties();
        uiProps.put("derby.ui.locale","es_AR");
        uiProps.put("derby.ui.codeset","EUC_JP");
        suite.addTest(new SystemPropertyTestSetup(
                new LocalizedDisplayScriptTest("LocalizedDisplay"), uiProps));
        return getIJConfig(suite);
    }
    
    /**
     * Set up the test environment.
     */
    protected void setUp() {
        // the canon contains time columns, which would display localized -
        // and so cause errors. Thus, run this with timezone PST.
        defaultTimeZone = TimeZone.getDefault(); 
        TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles")); 
        LocalizedResource.resetLocalizedResourceCache();
    }
    
    public void tearDown() throws Exception {
        TimeZone.setDefault(defaultTimeZone); 
        LocalizedResource.resetLocalizedResourceCache();
        super.tearDown();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/18.java