error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14796.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14796.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14796.java
text:
```scala
public v@@oid testBug50799() throws Exception {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

/*
 * Created on Jul 16, 2003
 *
 */
package org.apache.jmeter.testelement;

import junit.framework.TestCase;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.LoginConfig;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.sampler.DebugSampler;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;

public class PackageTest extends TestCase {
    public PackageTest(String arg0) {
        super(arg0);
    }

    // Test needs to run in this package in order to give access to AbstractTestElement.addProperty() 
    public void DISABLEDtestBug50799() throws Exception {
        HeaderManager headerManager = new HeaderManager();
        headerManager.add(new Header("1stLevelTestHeader", "testValue1"));
        HeaderManager headerManager2 = new HeaderManager();
        headerManager2.add(new Header("2ndLevelTestHeader", "testValue2"));

        DebugSampler debugSampler = new DebugSampler();
        debugSampler.addProperty(new StringProperty("name", "DebugSampler_50799"));
        debugSampler.setRunningVersion(true);
        assertTrue(debugSampler.getProperty("HeaderManager.headers") instanceof NullProperty);
        debugSampler.addTestElement(headerManager);
        assertFalse(debugSampler.getProperty("HeaderManager.headers") instanceof NullProperty);
        assertEquals(debugSampler.getProperty("HeaderManager.headers").getStringValue() ,"[1stLevelTestHeader	testValue1]");

        debugSampler.addTestElement(headerManager2);
        assertEquals(debugSampler.getProperty("HeaderManager.headers").getStringValue() ,"[1stLevelTestHeader	testValue1, 2ndLevelTestHeader	testValue2]");
        assertEquals(2, ((CollectionProperty)debugSampler.getProperty("HeaderManager.headers")).size());
        
        headerManager.recoverRunningVersion();
        headerManager2.recoverRunningVersion();
        debugSampler.recoverRunningVersion();

        assertEquals(1, headerManager.size());
        assertEquals(1, headerManager2.size());
        assertEquals(0, ((CollectionProperty)debugSampler.getProperty("HeaderManager.headers")).size());
        assertEquals(new Header("1stLevelTestHeader", "testValue1"), headerManager.get(0));
        assertEquals(new Header("2ndLevelTestHeader", "testValue2"), headerManager2.get(0));
    }

    public void testRecovery() throws Exception {
        ConfigTestElement config = new ConfigTestElement();
        config.addProperty(new StringProperty("name", "config"));
        config.setRunningVersion(true);
        LoginConfig loginConfig = new LoginConfig();
        loginConfig.setUsername("user1");
        loginConfig.setPassword("pass1");
        assertTrue(config.getProperty("login") instanceof NullProperty);
        // This test should work whether or not all Nulls are equal
        assertEquals(new NullProperty("login"), config.getProperty("login"));
        config.addProperty(new TestElementProperty("login", loginConfig));
        assertEquals(loginConfig.toString(), config.getPropertyAsString("login"));
        config.recoverRunningVersion();
        assertTrue(config.getProperty("login") instanceof NullProperty);
        assertEquals(new NullProperty("login"), config.getProperty("login"));
    }

    public void testArguments() throws Exception {
        Arguments args = new Arguments();
        args.addArgument("arg1", "val1", "=");
        TestElementProperty prop = new TestElementProperty("args", args);
        ConfigTestElement te = new ConfigTestElement();
        te.addProperty(prop);
        te.setRunningVersion(true);
        Arguments config = new Arguments();
        config.addArgument("config1", "configValue", "=");
        TestElementProperty configProp = new TestElementProperty("args", config);
        ConfigTestElement te2 = new ConfigTestElement();
        te2.addProperty(configProp);
        te.addTestElement(te2);
        assertEquals(2, args.getArgumentCount());
        assertEquals("config1=configValue", args.getArgument(1).toString());
        te.recoverRunningVersion();
        te.addTestElement(te2);
        assertEquals(2, args.getArgumentCount());
        assertEquals("config1=configValue", args.getArgument(1).toString());

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14796.java