error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9058.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9058.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9058.java
text:
```scala
L@@ist<HTTPFileArg> newHTTPFileArgs = new LinkedList<HTTPFileArg>();

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

package org.apache.jmeter.protocol.http.util;

import java.util.List;
import java.util.LinkedList;
import junit.framework.TestCase;

import org.apache.jmeter.testelement.property.PropertyIterator;

public class TestHTTPFileArgs extends TestCase {
    public TestHTTPFileArgs(String name) {
        super(name);
    }

    public void testConstructors() throws Exception {
        HTTPFileArgs files = new HTTPFileArgs();
        assertEquals(0, files.getHTTPFileArgCount());
    }

    public void testAdding() throws Exception {
        HTTPFileArgs files = new HTTPFileArgs();
        assertEquals(0, files.getHTTPFileArgCount());
        files.addHTTPFileArg("hede");
        assertEquals(1, files.getHTTPFileArgCount());
        assertEquals("hede", ((HTTPFileArg) files.iterator().next().getObjectValue()).getPath());
        HTTPFileArg file = new HTTPFileArg("hodo");
        files.addHTTPFileArg(file);
        assertEquals(2, files.getHTTPFileArgCount());
        PropertyIterator iter = files.iterator();
        assertEquals("hede", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("hodo", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        files.addEmptyHTTPFileArg();
        assertEquals(3, files.getHTTPFileArgCount());
        iter = files.iterator();
        assertEquals("hede", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("hodo", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
    }

    public void testSetHTTPFileArgs() throws Exception {
        List newHTTPFileArgs = new LinkedList();
        newHTTPFileArgs.add(new HTTPFileArg("hede"));
        HTTPFileArgs files = new HTTPFileArgs();
        files.setHTTPFileArgs(newHTTPFileArgs);
        assertEquals(1, files.getHTTPFileArgCount());
        assertEquals("hede", ((HTTPFileArg) files.iterator().next().getObjectValue()).getPath());
    }

    public void testRemoving() throws Exception {
        HTTPFileArgs files = new HTTPFileArgs();
        assertEquals(0, files.getHTTPFileArgCount());
        files.addHTTPFileArg("hede");
        assertEquals(1, files.getHTTPFileArgCount());
        files.clear();
        assertEquals(0, files.getHTTPFileArgCount());
        files.addHTTPFileArg("file1");
        files.addHTTPFileArg("file2");
        files.addHTTPFileArg("file3");
        HTTPFileArg file = new HTTPFileArg("file4");
        files.addHTTPFileArg(file);
        files.addHTTPFileArg("file5");
        files.addHTTPFileArg("file6");
        assertEquals(6, files.getHTTPFileArgCount());
        files.removeHTTPFileArg("file3");
        assertEquals(5, files.getHTTPFileArgCount());
        PropertyIterator iter = files.iterator();
        assertEquals("file1", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file2", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file4", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file5", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file6", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        files.removeHTTPFileArg(file);
        assertEquals(4, files.getHTTPFileArgCount());
        iter = files.iterator();
        assertEquals("file1", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file2", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file5", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file6", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        files.removeHTTPFileArg(new HTTPFileArg("file5"));
        assertEquals(3, files.getHTTPFileArgCount());
        iter = files.iterator();
        assertEquals("file1", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file2", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file6", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        files.removeHTTPFileArg(1);
        assertEquals(2, files.getHTTPFileArgCount());
        iter = files.iterator();
        assertEquals("file1", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        assertEquals("file6", ((HTTPFileArg) iter.next().getObjectValue()).getPath());
        files.removeAllHTTPFileArgs();
        assertEquals(0, files.getHTTPFileArgCount());
    }

    public void testToString() throws Exception {
        HTTPFileArgs files = new HTTPFileArgs();
        files.addHTTPFileArg("file1");
        files.addHTTPFileArg("file2");
        files.addHTTPFileArg("file3");
        assertEquals("file1&file2&file3", files.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9058.java