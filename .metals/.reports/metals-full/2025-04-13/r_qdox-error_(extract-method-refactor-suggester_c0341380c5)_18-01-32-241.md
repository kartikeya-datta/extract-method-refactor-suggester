error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18341.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18341.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18341.java
text:
```scala
a@@m.addFile(findTestPath("testfiles/TestAuth.txt"));

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

package org.apache.jmeter.protocol.http.control;

import java.net.URL;

import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.testelement.property.CollectionProperty;

public class TestAuthManager extends JMeterTestCase {
        public TestAuthManager(String name) {
            super(name);
        }

        public void testHttp() throws Exception {
            assertTrue(AuthManager.isSupportedProtocol(new URL("http:")));
        }

        public void testHttps() throws Exception {
            assertTrue(AuthManager.isSupportedProtocol(new URL("https:")));
        }

        public void testFile() throws Exception {
            AuthManager am = new AuthManager();
            CollectionProperty ao = am.getAuthObjects();
            assertEquals(0, ao.size());
            am.addFile("testfiles/TestAuth.txt");
            assertEquals(9, ao.size());
            Authorization at;
            at = am.getAuthForURL(new URL("http://a.b.c/"));
            assertEquals("login", at.getUser());
            assertEquals("password", at.getPass());
            at = am.getAuthForURL(new URL("http://a.b.c:80/")); // same as above
            assertEquals("login", at.getUser());
            assertEquals("password", at.getPass());
            at = am.getAuthForURL(new URL("http://a.b.c:443/"));// not same
            assertNull(at);
            at = am.getAuthForURL(new URL("http://a.b.c/1"));
            assertEquals("login1", at.getUser());
            assertEquals("password1", at.getPass());
            assertEquals("", at.getDomain());
            assertEquals("", at.getRealm());
            at = am.getAuthForURL(new URL("http://d.e.f/"));
            assertEquals("user", at.getUser());
            assertEquals("pass", at.getPass());
            assertEquals("domain", at.getDomain());
            assertEquals("realm", at.getRealm());
            at = am.getAuthForURL(new URL("https://j.k.l/"));
            assertEquals("jkl", at.getUser());
            assertEquals("pass", at.getPass());
            at = am.getAuthForURL(new URL("https://j.k.l:443/"));
            assertEquals("jkl", at.getUser());
            assertEquals("pass", at.getPass());
            at = am.getAuthForURL(new URL("https://l.m.n/"));
            assertEquals("lmn443", at.getUser());
            assertEquals("pass", at.getPass());
            at = am.getAuthForURL(new URL("https://l.m.n:443/"));
            assertEquals("lmn443", at.getUser());
            assertEquals("pass", at.getPass());
            at = am.getAuthForURL(new URL("https://l.m.n:8443/"));
            assertEquals("lmn8443", at.getUser());
            assertEquals("pass", at.getPass());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18341.java