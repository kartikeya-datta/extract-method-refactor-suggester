error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1905.java
text:
```scala
protected b@@yte[] newBuffer(int size) {

package org.apache.lucene.store;

/**
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

import java.io.IOException;
import java.util.HashMap;

import org.apache.lucene.util.LuceneTestCase;

/** Test huge RAMFile with more than Integer.MAX_VALUE bytes. */
public class TestHugeRamFile extends LuceneTestCase {
  
  private static final long MAX_VALUE = (long) 2 * (long) Integer.MAX_VALUE;

  /** Fake a huge ram file by using the same byte buffer for all 
   * buffers under maxint. */
  private static class DenseRAMFile extends RAMFile {
    private long capacity = 0;
    private HashMap<Integer,byte[]> singleBuffers = new HashMap<Integer,byte[]>();
    @Override
    byte[] newBuffer(int size) {
      capacity += size;
      if (capacity <= MAX_VALUE) {
        // below maxint we reuse buffers
        byte buf[] = singleBuffers.get(Integer.valueOf(size));
        if (buf==null) {
          buf = new byte[size]; 
          //System.out.println("allocate: "+size);
          singleBuffers.put(Integer.valueOf(size),buf);
        }
        return buf;
      }
      //System.out.println("allocate: "+size); System.out.flush();
      return new byte[size];
    }
  }
  
  /** Test huge RAMFile with more than Integer.MAX_VALUE bytes. (LUCENE-957) */
  public void testHugeFile() throws IOException {
    DenseRAMFile f = new DenseRAMFile();
    // output part
    RAMOutputStream out = new RAMOutputStream(f);
    byte b1[] = new byte[RAMOutputStream.BUFFER_SIZE];
    byte b2[] = new byte[RAMOutputStream.BUFFER_SIZE / 3];
    for (int i = 0; i < b1.length; i++) {
      b1[i] = (byte) (i & 0x0007F);
    }
    for (int i = 0; i < b2.length; i++) {
      b2[i] = (byte) (i & 0x0003F);
    }
    long n = 0;
    assertEquals("output length must match",n,out.length());
    while (n <= MAX_VALUE - b1.length) {
      out.writeBytes(b1,0,b1.length);
      out.flush();
      n += b1.length;
      assertEquals("output length must match",n,out.length());
    }
    //System.out.println("after writing b1's, length = "+out.length()+" (MAX_VALUE="+MAX_VALUE+")");
    int m = b2.length;
    long L = 12;
    for (int j=0; j<L; j++) {
      for (int i = 0; i < b2.length; i++) {
        b2[i]++;
      }
      out.writeBytes(b2,0,m);
      out.flush();
      n += m;
      assertEquals("output length must match",n,out.length());
    }
    out.close();
    // input part
    RAMInputStream in = new RAMInputStream(f);
    assertEquals("input length must match",n,in.length());
    //System.out.println("input length = "+in.length()+" % 1024 = "+in.length()%1024);
    for (int j=0; j<L; j++) {
      long loc = n - (L-j)*m; 
      in.seek(loc/3);
      in.seek(loc);
      for (int i=0; i<m; i++) {
        byte bt = in.readByte();
        byte expected = (byte) (1 + j + (i & 0x0003F));
        assertEquals("must read same value that was written! j="+j+" i="+i,expected,bt);
      }
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1905.java