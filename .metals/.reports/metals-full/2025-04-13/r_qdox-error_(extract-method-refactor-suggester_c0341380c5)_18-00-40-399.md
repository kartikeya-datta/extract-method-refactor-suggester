error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2599.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2599.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,13]

error in qdox parser
file content:
```java
offset: 13
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2599.java
text:
```scala
{10, 3, 1, 4}@@, // 10.3.1.4 (Aug 1, 2007 / SVN 561794)

/*

Derby - Class org.apache.derbyTesting.functionTests.tests.upgradeTests._Suite

Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/
package org.apache.derbyTesting.functionTests.tests.upgradeTests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.derbyTesting.junit.BaseTestCase;
import org.apache.derbyTesting.junit.JDBC;

/**
 * Run the full upgrade suite. This is the only
 * way to run tests in this package.
 * A set of tests are run against a number of
 * previous releases, see the static OLD_VERSIONS
 * field in this class.
 * 
 * Each test against the old release consists of following phases:
   
    <OL>
    <LI> Create database with the <B>old</B> release.
    <LI> Boot the database with the <B>new</B> release in soft upgrade mode.
    Try to execute functionality that is not allowed in soft upgrade.
    <LI> Boot the database with the <B>old</B> release to ensure the
    database can be booted by the old release after soft upgrade.
    <LI> Boot the database with the <B>new</B> release in hard upgrade mode,
    specifying the upgrade=true attribute.
    <LI> Boot the database with the <B>old</B> release to ensure the
    database can not be booted by the old release after hard upgrade.
    </OL>
    The class PhaseChanger is the decorator that sets up the
    fixtures to run in a given phase.

    <P>
    The test fixtures themseleves are in JUnit test classes
    that are sub-classes of UpgradeChange. The set of fixtures
    in BasicSetup is general setup and the changes per release
    are in classes of the form Changes10_1 etc.
    
    <P>
    The class UpgradeRun hooks up the test fixtures for a set
    of runs against a single old release into a single suite.
    Each fixture is run multiple times, once per phase against
    each old release.
    
    @see UpgradeRun
    @see UpgradeChange
 */
public class _Suite extends BaseTestCase {
    
    /**
     * Property that indicates the location of the
     * old releases.
     */
    static final String OLD_RELEASE_PATH_PROPERTY =
        "derbyTesting.oldReleasePath";
    
    /**
     * The saved location in svn at apache for older releases for testing
     */
    static final String OLD_JAR_URL =
        "https://svn.apache.org/repos/asf/db/derby/jars";
    
    /**
     * List of the versions to test against.
     * The tests look for the jar files in each releasae
     * in the folder:
     * ${derbyTesting.oldReleasePath}/M.m.f.p
     * 
     * If derbyTesting.oldReleasePath is not set then it is assumed the files can
     * be accessed from the svn repository at apache. If this location is
     * not available, then the test will fail.
     * 
     * If the property is set, but ${derbyTesting.oldReleasePath}/M.m.f.p does not exist
     * for a specific release then those sets of tests will be skipped.
     * 
     * One can also set derbyTesting.oldReleasePath to a checked out
     * version of the jars from the Apache svn repo. E.g.
     * 
     * cd $HOME
     * mkdir derby_upgrade
     * cd derby_upgrade
     * svn co https://svn.apache.org/repos/asf/db/derby/jars
     * 
     * Then set derbyTesting.oldReleasePath as:
     *   -DderbyTesting.oldReleasePath=$HOME/derby_upgrade/jars
     * when running tests.
     */
    private static final int[][] OLD_VERSIONS =
    {
        {10, 0, 2, 1}, // 10.0.2.1 (incubator release)
        {10, 1, 1, 0}, // 10.1.1.0 (Aug 3, 2005 / SVN 208786)
        {10, 1, 2, 1}, // 10.1.2.1 (Nov 18, 2005 / SVN 330608)
        {10, 1, 3, 1}, // 10.1.3.1 (Jun 30, 2006 / SVN 417277)
        {10, 2, 1, 6}, // 10.2.1.6 (Oct 02, 2006 / SVN 452058)
        {10, 2, 2, 0}, // 10.2.2.0 (Dec 12, 2006 / SVN 485682)
        {10, 3, 1, 4}, // 10.3.1.4 (Aug 30, 2007 / SVN 571336)
    };

    /**
     * Use suite method instead.
     */
    private _Suite(String name) {
        super(name);
    }
    
    public static Test suite() {
        
        TestSuite suite = new TestSuite("Upgrade Suite");       

        for (int i = 0; i < OLD_VERSIONS.length; i++) {
            // JSR169 support was only added with 10.1, so don't
            // run 10.0 to later upgrade if that's what our jvm is supporting.
            if (!(JDBC.vmSupportsJSR169() && 
                (OLD_VERSIONS[i][0]==10) && (OLD_VERSIONS[i][1]==0)))
                suite.addTest(UpgradeRun.suite(OLD_VERSIONS[i]));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2599.java