error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3709.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3709.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3709.java
text:
```scala
final class Ints {

p@@ackage org.apache.lucene.codecs.lucene40.values;

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

import org.apache.lucene.codecs.DocValuesConsumer;
import org.apache.lucene.index.DocValues.Source;
import org.apache.lucene.index.DocValues.Type;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Counter;
import org.apache.lucene.util.IOUtils;

/**
 * Stores ints packed and fixed with fixed-bit precision.
 * 
 * @lucene.experimental
 */
public final class Ints {
  protected static final String CODEC_NAME = "Ints";
  protected static final int VERSION_START = 0;
  protected static final int VERSION_CURRENT = VERSION_START;

  private Ints() {
  }
  
  public static DocValuesConsumer getWriter(Directory dir, String id, Counter bytesUsed,
      Type type, IOContext context) throws IOException {
    return type == Type.VAR_INTS ? new PackedIntValues.PackedIntsWriter(dir, id,
        bytesUsed, context) : new IntsWriter(dir, id, bytesUsed, context, type);
  }

  public static DocValues getValues(Directory dir, String id, int numDocs,
      Type type, IOContext context) throws IOException {
    return type == Type.VAR_INTS ? new PackedIntValues.PackedIntsReader(dir, id,
        numDocs, context) : new IntsReader(dir, id, numDocs, context, type);
  }
  
  private static Type sizeToType(int size) {
    switch (size) {
    case 1:
      return Type.FIXED_INTS_8;
    case 2:
      return Type.FIXED_INTS_16;
    case 4:
      return Type.FIXED_INTS_32;
    case 8:
      return Type.FIXED_INTS_64;
    default:
      throw new IllegalStateException("illegal size " + size);
    }
  }
  
  private static int typeToSize(Type type) {
    switch (type) {
    case FIXED_INTS_16:
      return 2;
    case FIXED_INTS_32:
      return 4;
    case FIXED_INTS_64:
      return 8;
    case FIXED_INTS_8:
      return 1;
    default:
      throw new IllegalStateException("illegal type " + type);
    }
  }


  static class IntsWriter extends FixedStraightBytesImpl.Writer {
    private final DocValuesArray template;

    public IntsWriter(Directory dir, String id, Counter bytesUsed,
        IOContext context, Type valueType) throws IOException {
      this(dir, id, CODEC_NAME, VERSION_CURRENT, bytesUsed, context, valueType);
    }

    protected IntsWriter(Directory dir, String id, String codecName,
        int version, Counter bytesUsed, IOContext context, Type valueType) throws IOException {
      super(dir, id, codecName, version, bytesUsed, context);
      size = typeToSize(valueType);
      this.bytesRef = new BytesRef(size);
      bytesRef.length = size;
      template = DocValuesArray.TEMPLATES.get(valueType);
    }
    
    protected void add(int docID, long v) throws IOException {
      template.toBytes(v, bytesRef);
      add(docID, bytesRef);
    }

    @Override
    public void add(int docID, IndexableField docValue) throws IOException {
      add(docID, docValue.numericValue().longValue());
    }
    
    @Override
    protected void setMergeBytes(Source source, int sourceDoc) {
      final long value = source.getInt(sourceDoc);
      template.toBytes(value, bytesRef);
    }
    
    @Override
    protected boolean tryBulkMerge(DocValues docValues) {
      // only bulk merge if value type is the same otherwise size differs
      return super.tryBulkMerge(docValues) && docValues.type() == template.type();
    }
  }
  
  final static class IntsReader extends FixedStraightBytesImpl.FixedStraightReader {
    private final DocValuesArray arrayTemplate;

    IntsReader(Directory dir, String id, int maxDoc, IOContext context, Type type)
        throws IOException {
      super(dir, id, CODEC_NAME, VERSION_CURRENT, maxDoc,
          context, type);
      arrayTemplate = DocValuesArray.TEMPLATES.get(type);
      assert arrayTemplate != null;
      assert type == sizeToType(size);
    }

    @Override
    public Source load() throws IOException {
      final IndexInput indexInput = cloneData();
      try {
        return arrayTemplate.newFromInput(indexInput, maxDoc);
      } finally {
        IOUtils.close(indexInput);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3709.java