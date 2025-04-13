error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1244.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1244.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1244.java
text:
```scala
final F@@ile base = _TestUtil.getTempDir("fsResourceLoaderBase").getAbsoluteFile();

package org.apache.lucene.analysis.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

public class TestFilesystemResourceLoader extends LuceneTestCase {
  
  private void assertNotFound(ResourceLoader rl) throws Exception {
    try {
      IOUtils.closeWhileHandlingException(rl.openResource("/this-directory-really-really-really-should-not-exist/foo/bar.txt"));
      fail("The resource does not exist, should fail!");
    } catch (IOException ioe) {
      // pass
    }
    try {
      rl.newInstance("org.apache.lucene.analysis.FooBarFilterFactory", TokenFilterFactory.class);
      fail("The class does not exist, should fail!");
    } catch (RuntimeException iae) {
      // pass
    }
  }
  
  private void assertClasspathDelegation(ResourceLoader rl) throws Exception {
    // try a stopwords file from classpath
    CharArraySet set = WordlistLoader.getSnowballWordSet(
      new InputStreamReader(rl.openResource("org/apache/lucene/analysis/snowball/english_stop.txt"), IOUtils.CHARSET_UTF_8),
      TEST_VERSION_CURRENT
    );
    assertTrue(set.contains("you"));
    // try to load a class; we use string comparison because classloader may be different...
    assertEquals("org.apache.lucene.analysis.en.KStemFilterFactory",
        rl.newInstance("org.apache.lucene.analysis.en.KStemFilterFactory", TokenFilterFactory.class).getClass().getName());
    // theoretically classes should also be loadable:
    IOUtils.closeWhileHandlingException(rl.openResource("java/lang/String.class"));
  }
  
  public void testBaseDir() throws Exception {
    final File base = _TestUtil.getTempDir("fsResourceLoaderBase");
    try {
      base.mkdirs();
      Writer os = new OutputStreamWriter(new FileOutputStream(new File(base, "template.txt")), IOUtils.CHARSET_UTF_8);
      try {
        os.write("foobar\n");
      } finally {
        IOUtils.closeWhileHandlingException(os);
      }
      
      ResourceLoader rl = new FilesystemResourceLoader(base);
      assertEquals("foobar", WordlistLoader.getLines(rl.openResource("template.txt"), IOUtils.CHARSET_UTF_8).get(0));
      // Same with full path name:
      String fullPath = new File(base, "template.txt").toString();
      assertEquals("foobar",
          WordlistLoader.getLines(rl.openResource(fullPath), IOUtils.CHARSET_UTF_8).get(0));
      assertClasspathDelegation(rl);
      assertNotFound(rl);
      
      // now use RL without base dir:
      rl = new FilesystemResourceLoader();
      assertEquals("foobar",
          WordlistLoader.getLines(rl.openResource(new File(base, "template.txt").toString()), IOUtils.CHARSET_UTF_8).get(0));
      assertClasspathDelegation(rl);
      assertNotFound(rl);
    } finally {
      _TestUtil.rmDir(base);
    }
  }
  
  public void testDelegation() throws Exception {
    ResourceLoader rl = new FilesystemResourceLoader(null, new StringMockResourceLoader("foobar\n"));
    assertEquals("foobar", WordlistLoader.getLines(rl.openResource("template.txt"), IOUtils.CHARSET_UTF_8).get(0));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1244.java