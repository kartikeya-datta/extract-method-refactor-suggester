error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1113.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1113.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1113.java
text:
```scala
d@@oc.add(new TextField("id", i + "", Field.Store.YES));

package org.apache.lucene.index;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.IOException;
import java.util.EnumSet;
import java.util.Random;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.ByteDocValuesField;
import org.apache.lucene.document.DerefBytesDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleDocValuesField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.document.IntDocValuesField;
import org.apache.lucene.document.LongDocValuesField;
import org.apache.lucene.document.PackedLongDocValuesField;
import org.apache.lucene.document.ShortDocValuesField;
import org.apache.lucene.document.SortedBytesDocValuesField;
import org.apache.lucene.document.StraightBytesDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DocValues.Source;
import org.apache.lucene.index.DocValues.Type;
import org.apache.lucene.store.ByteArrayDataOutput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;

public class TestTypePromotion extends LuceneTestCase {

  private static EnumSet<Type> INTEGERS = EnumSet.of(Type.VAR_INTS,
      Type.FIXED_INTS_16, Type.FIXED_INTS_32,
      Type.FIXED_INTS_64, Type.FIXED_INTS_8);

  private static EnumSet<Type> FLOATS = EnumSet.of(Type.FLOAT_32,
      Type.FLOAT_64, Type.FIXED_INTS_8);

  private static EnumSet<Type> UNSORTED_BYTES = EnumSet.of(
      Type.BYTES_FIXED_DEREF, Type.BYTES_FIXED_STRAIGHT,
      Type.BYTES_VAR_STRAIGHT, Type.BYTES_VAR_DEREF);

  private static EnumSet<Type> SORTED_BYTES = EnumSet.of(
      Type.BYTES_FIXED_SORTED, Type.BYTES_VAR_SORTED);
  
  public Type randomValueType(EnumSet<Type> typeEnum, Random random) {
    Type[] array = typeEnum.toArray(new Type[0]);
    return array[random.nextInt(array.length)];
  }
  
  private static enum TestType {
    Int, Float, Byte
  }

  private void runTest(EnumSet<Type> types, TestType type)
      throws CorruptIndexException, IOException {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    int num_1 = atLeast(200);
    int num_2 = atLeast(200);
    int num_3 = atLeast(200);
    long[] values = new long[num_1 + num_2 + num_3];
    Type[] sourceType = new Type[num_1 + num_2 + num_3];
    index(writer,
        randomValueType(types, random()), values, sourceType, 0, num_1);
    writer.commit();
    
    index(writer,
        randomValueType(types, random()), values, sourceType, num_1, num_2);
    writer.commit();
    
    if (random().nextInt(4) == 0) {
      // once in a while use addIndexes
      writer.forceMerge(1);
      
      Directory dir_2 = newDirectory() ;
      IndexWriter writer_2 = new IndexWriter(dir_2,
          newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
      index(writer_2,
          randomValueType(types, random()), values, sourceType, num_1 + num_2, num_3);
      writer_2.commit();
      writer_2.close();
      if (rarely()) {
        writer.addIndexes(dir_2);
      } else {
        // do a real merge here
        IndexReader open = maybeWrapReader(DirectoryReader.open(dir_2));
        writer.addIndexes(open);
        open.close();
      }
      dir_2.close();
    } else {
      index(writer,
          randomValueType(types, random()), values, sourceType, num_1 + num_2, num_3);
    }

    writer.forceMerge(1);
    writer.close();
    assertValues(type, dir, values, sourceType);
    dir.close();
  }

  
  private void assertValues(TestType type, Directory dir, long[] values, Type[] sourceType)
      throws CorruptIndexException, IOException {
    DirectoryReader reader = DirectoryReader.open(dir);
    assertEquals(1, reader.getSequentialSubReaders().length);
    IndexReaderContext topReaderContext = reader.getTopReaderContext();
    AtomicReaderContext[] children = topReaderContext.leaves();
    assertEquals(1, children.length);
    DocValues docValues = children[0].reader().docValues("promote");
    Source directSource = docValues.getDirectSource();
    for (int i = 0; i < values.length; i++) {
      int id = Integer.parseInt(reader.document(i).get("id"));
      String msg = "id: " + id + " doc: " + i;
      switch (type) {
      case Byte:
        BytesRef bytes = directSource.getBytes(i, new BytesRef());
        long value = 0;
        switch(bytes.length) {
        case 1:
          value = bytes.bytes[bytes.offset];
          break;
        case 2:
          value = ((bytes.bytes[bytes.offset] & 0xFF) << 8) | (bytes.bytes[bytes.offset+1] & 0xFF);
          break;
        case 4:
          value = ((bytes.bytes[bytes.offset] & 0xFF) << 24)  | ((bytes.bytes[bytes.offset+1] & 0xFF) << 16)
 ((bytes.bytes[bytes.offset+2] & 0xFF) << 8) | (bytes.bytes[bytes.offset+3] & 0xFF);
          break;
        case 8:
          value =  (((long)(bytes.bytes[bytes.offset] & 0xff) << 56) | ((long)(bytes.bytes[bytes.offset+1] & 0xff) << 48) |
                  ((long)(bytes.bytes[bytes.offset+2] & 0xff) << 40) | ((long)(bytes.bytes[bytes.offset+3] & 0xff) << 32) |
                  ((long)(bytes.bytes[bytes.offset+4] & 0xff) << 24) | ((long)(bytes.bytes[bytes.offset+5] & 0xff) << 16) |
                  ((long)(bytes.bytes[bytes.offset+6] & 0xff) <<  8) | ((long)(bytes.bytes[bytes.offset+7] & 0xff)));
          break;
          
        default:
          fail(msg + " bytessize: " + bytes.length);
        }
        
        assertEquals(msg  + " byteSize: " + bytes.length, values[id], value);
        break;
      case Float:
          if (sourceType[id] == Type.FLOAT_32
 sourceType[id] == Type.FLOAT_64) {
            assertEquals(msg, values[id],
                Double.doubleToRawLongBits(directSource.getFloat(i)));
          } else {
            assertEquals(msg, values[id], directSource.getFloat(i), 0.0d);
          }
        break;
      case Int:
        assertEquals(msg, values[id], directSource.getInt(i));
        break;
      default:
        break;
      }

    }
    docValues.close();
    reader.close();
  }

  public void index(IndexWriter writer,
      Type valueType, long[] values, Type[] sourceTypes, int offset, int num)
      throws CorruptIndexException, IOException {
    final Field valField;

    if (VERBOSE) {
      System.out.println("TEST: add docs " + offset + "-" + (offset+num) + " valType=" + valueType);
    }

    switch(valueType) {
    case VAR_INTS:
      valField = new PackedLongDocValuesField("promote", (long) 0);
      break;
    case FIXED_INTS_8:
      valField = new ByteDocValuesField("promote", (byte) 0);
      break;
    case FIXED_INTS_16:
      valField = new ShortDocValuesField("promote", (short) 0);
      break;
    case FIXED_INTS_32:
      valField = new IntDocValuesField("promote", 0);
      break;
    case FIXED_INTS_64:
      valField = new LongDocValuesField("promote", (byte) 0);
      break;
    case FLOAT_32:
      valField = new FloatDocValuesField("promote", 0f);
      break;
    case FLOAT_64:
      valField = new DoubleDocValuesField("promote", 0d);
      break;
    case BYTES_FIXED_STRAIGHT:
      valField = new StraightBytesDocValuesField("promote", new BytesRef(), true);
      break;
    case BYTES_VAR_STRAIGHT:
      valField = new StraightBytesDocValuesField("promote", new BytesRef(), false);
      break;
    case BYTES_FIXED_DEREF:
      valField = new DerefBytesDocValuesField("promote", new BytesRef(), true);
      break;
    case BYTES_VAR_DEREF:
      valField = new DerefBytesDocValuesField("promote", new BytesRef(), false);
      break;
    case BYTES_FIXED_SORTED:
      valField = new SortedBytesDocValuesField("promote", new BytesRef(), true);
      break;
    case BYTES_VAR_SORTED:
      valField = new SortedBytesDocValuesField("promote", new BytesRef(), false);
      break;
    default:
      throw new IllegalStateException("unknown Type: " + valueType);
    }

    for (int i = offset; i < offset + num; i++) {
      Document doc = new Document();
      doc.add(new Field("id", i + "", TextField.TYPE_STORED));
      sourceTypes[i] = valueType;
      switch (valueType) {
      case VAR_INTS:
        // TODO: can we do nextLong()?
        values[i] = random().nextInt();
        valField.setLongValue(values[i]);
        break;
      case FIXED_INTS_16:
        // TODO: negatives too?
        values[i] = random().nextInt(Short.MAX_VALUE);
        valField.setShortValue((short) values[i]);
        break;
      case FIXED_INTS_32:
        values[i] = random().nextInt();
        valField.setIntValue((int) values[i]);
        break;
      case FIXED_INTS_64:
        values[i] = random().nextLong();
        valField.setLongValue(values[i]);
        break;
      case FLOAT_64:
        final double nextDouble = random().nextDouble();
        values[i] = Double.doubleToRawLongBits(nextDouble);
        valField.setDoubleValue(nextDouble);
        break;
      case FLOAT_32:
        final float nextFloat = random().nextFloat();
        values[i] = Double.doubleToRawLongBits(nextFloat);
        valField.setFloatValue(nextFloat);
        break;
      case FIXED_INTS_8:
        values[i] = (byte) i;
        valField.setByteValue((byte)values[i]);
        break;
      case BYTES_FIXED_DEREF:
      case BYTES_FIXED_SORTED:
      case BYTES_FIXED_STRAIGHT:
        values[i] = random().nextLong();
        byte bytes[] = new byte[8];
        ByteArrayDataOutput out = new ByteArrayDataOutput(bytes, 0, 8);
        out.writeLong(values[i]);
        valField.setBytesValue(new BytesRef(bytes));
        break;
      case BYTES_VAR_DEREF:
      case BYTES_VAR_SORTED:
      case BYTES_VAR_STRAIGHT:
        byte lbytes[] = new byte[8];
        ByteArrayDataOutput lout = new ByteArrayDataOutput(lbytes, 0, 8);
        final int len;
        if (random().nextBoolean()) {
          values[i] = random().nextInt();
          lout.writeInt((int)values[i]);
          len = 4;
        } else {
          values[i] = random().nextLong();
          lout.writeLong(values[i]);
          len = 8;
        }
        valField.setBytesValue(new BytesRef(lbytes, 0, len));
        break;

      default:
        fail("unexpected value " + valueType);
      }
      if (VERBOSE) {
        System.out.println("  doc " + i + " has val=" + valField);
      }
      doc.add(valField);
      writer.addDocument(doc);
      if (random().nextInt(10) == 0) {
        writer.commit();
      }
    }
  }

  public void testPromoteBytes() throws IOException {
    runTest(UNSORTED_BYTES, TestType.Byte);
  }
  
  public void testSortedPromoteBytes() throws IOException {
    runTest(SORTED_BYTES, TestType.Byte);
  }

  public void testPromoteInteger() throws IOException {
    runTest(INTEGERS, TestType.Int);
  }

  public void testPromotFloatingPoint() throws CorruptIndexException,
      IOException {
    runTest(FLOATS, TestType.Float);
  }
  
  public void testMergeIncompatibleTypes() throws IOException {
    Directory dir = newDirectory();
    IndexWriterConfig writerConfig = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()));
    writerConfig.setMergePolicy(NoMergePolicy.NO_COMPOUND_FILES); // no merges until we are done with adding values
    IndexWriter writer = new IndexWriter(dir, writerConfig);
    int num_1 = atLeast(200);
    int num_2 = atLeast(200);
    long[] values = new long[num_1 + num_2];
    Type[] sourceType = new Type[num_1 + num_2];
    index(writer,
        randomValueType(INTEGERS, random()), values, sourceType, 0, num_1);
    writer.commit();
    
    if (random().nextInt(4) == 0) {
      // once in a while use addIndexes
      Directory dir_2 = newDirectory() ;
      IndexWriter writer_2 = new IndexWriter(dir_2,
                       newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
      index(writer_2,
          randomValueType(random().nextBoolean() ? UNSORTED_BYTES : SORTED_BYTES, random()), values, sourceType, num_1, num_2);
      writer_2.commit();
      writer_2.close();
      if (random().nextBoolean()) {
        writer.addIndexes(dir_2);
      } else {
        // do a real merge here
        IndexReader open = DirectoryReader.open(dir_2);
        writer.addIndexes(open);
        open.close();
      }
      dir_2.close();
    } else {
      index(writer,
          randomValueType(random().nextBoolean() ? UNSORTED_BYTES : SORTED_BYTES, random()), values, sourceType, num_1, num_2);
      writer.commit();
    }
    writer.close();
    writerConfig = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()));
    if (writerConfig.getMergePolicy() instanceof NoMergePolicy) {
      writerConfig.setMergePolicy(newLogMergePolicy()); // make sure we merge to one segment (merge everything together)
    }
    writer = new IndexWriter(dir, writerConfig);
    // now merge
    writer.forceMerge(1);
    writer.close();
    DirectoryReader reader = DirectoryReader.open(dir);
    assertEquals(1, reader.getSequentialSubReaders().length);
    IndexReaderContext topReaderContext = reader.getTopReaderContext();
    AtomicReaderContext[] children = topReaderContext.leaves();
    DocValues docValues = children[0].reader().docValues("promote");
    assertNotNull(docValues);
    assertValues(TestType.Byte, dir, values, sourceType);
    assertEquals(Type.BYTES_VAR_STRAIGHT, docValues.getType());
    reader.close();
    dir.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1113.java