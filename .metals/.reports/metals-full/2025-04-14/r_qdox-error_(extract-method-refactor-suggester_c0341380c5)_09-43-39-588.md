error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/454.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/454.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/454.java
text:
```scala
S@@tring segmentSuffix) {

package org.apache.lucene.codecs.lucene40;

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

import java.io.IOException;

import org.apache.lucene.codecs.NormsFormat;
import org.apache.lucene.codecs.PerDocConsumer;
import org.apache.lucene.codecs.PerDocProducer;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.DocValues.Type;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.PerDocWriteState;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.store.CompoundFileDirectory; // javadocs

/**
 * Lucene 4.0 Norms Format.
 * <p>
 * Files:
 * <ul>
 *   <li><tt>.nrm.cfs</tt>: {@link CompoundFileDirectory compound container}</li>
 *   <li><tt>.nrm.cfe</tt>: {@link CompoundFileDirectory compound entries}</li>
 * </ul>
 * Norms are implemented as DocValues, so other than file extension, norms are 
 * written exactly the same way as {@link Lucene40DocValuesFormat DocValues}.
 * 
 * @see Lucene40DocValuesFormat
 * @lucene.experimental
 */
public class Lucene40NormsFormat extends NormsFormat {
  private final static String NORMS_SEGMENT_SUFFIX = "nrm";
  
  @Override
  public PerDocConsumer docsConsumer(PerDocWriteState state) throws IOException {
    return new Lucene40NormsDocValuesConsumer(state, NORMS_SEGMENT_SUFFIX);
  }

  @Override
  public PerDocProducer docsProducer(SegmentReadState state) throws IOException {
    return new Lucene40NormsDocValuesProducer(state, NORMS_SEGMENT_SUFFIX);
  }

  /**
   * Lucene 4.0 PerDocProducer implementation that uses compound file.
   * 
   * @see Lucene40DocValuesFormat
   */
  public static class Lucene40NormsDocValuesProducer extends Lucene40DocValuesProducer {

    public Lucene40NormsDocValuesProducer(SegmentReadState state,
        String segmentSuffix) throws IOException {
      super(state, segmentSuffix);
    }

    @Override
    protected boolean canLoad(FieldInfo info) {
      return info.hasNorms();
    }

    @Override
    protected Type getDocValuesType(FieldInfo info) {
      return info.getNormType();
    }

    @Override
    protected boolean anyDocValuesFields(FieldInfos infos) {
      return infos.hasNorms();
    }
    
  }
  
  /**
   * Lucene 4.0 PerDocConsumer implementation that uses compound file.
   * 
   * @see Lucene40DocValuesFormat
   * @lucene.experimental
   */
  public static class Lucene40NormsDocValuesConsumer extends Lucene40DocValuesConsumer {

    public Lucene40NormsDocValuesConsumer(PerDocWriteState state,
        String segmentSuffix) throws IOException {
      super(state, segmentSuffix);
    }

    @Override
    protected DocValues getDocValuesForMerge(AtomicReader reader, FieldInfo info)
        throws IOException {
      return reader.normValues(info.name);
    }

    @Override
    protected boolean canMerge(FieldInfo info) {
      return info.hasNorms();
    }

    @Override
    protected Type getDocValuesType(FieldInfo info) {
      return info.getNormType();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/454.java