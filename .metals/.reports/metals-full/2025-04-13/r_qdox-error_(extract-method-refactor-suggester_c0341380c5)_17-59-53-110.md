error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1086.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1086.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1086.java
text:
```scala
final N@@umericField.DataType numericType = field.fieldType().numericType();

package org.apache.lucene.codecs.simpletext;

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

import org.apache.lucene.codecs.StoredFieldsWriter;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IOUtils;

/**
 * Writes plain-text stored fields.
 * <p>
 * <b><font color="red">FOR RECREATIONAL USE ONLY</font></B>
 * @lucene.experimental
 */
public class SimpleTextStoredFieldsWriter extends StoredFieldsWriter {
  private int numDocsWritten = 0;
  private final Directory directory;
  private final String segment;
  private IndexOutput out;
  
  final static String FIELDS_EXTENSION = "fld";
  
  final static BytesRef TYPE_STRING = new BytesRef("string");
  final static BytesRef TYPE_BINARY = new BytesRef("binary");
  final static BytesRef TYPE_INT    = new BytesRef("int");
  final static BytesRef TYPE_LONG   = new BytesRef("long");
  final static BytesRef TYPE_FLOAT  = new BytesRef("float");
  final static BytesRef TYPE_DOUBLE = new BytesRef("double");

  final static BytesRef END     = new BytesRef("END");
  final static BytesRef DOC     = new BytesRef("doc ");
  final static BytesRef NUM     = new BytesRef("  numfields ");
  final static BytesRef FIELD   = new BytesRef("  field ");
  final static BytesRef NAME    = new BytesRef("    name ");
  final static BytesRef TYPE    = new BytesRef("    type ");
  final static BytesRef VALUE   = new BytesRef("    value ");
  
  private final BytesRef scratch = new BytesRef();
  
  public SimpleTextStoredFieldsWriter(Directory directory, String segment, IOContext context) throws IOException {
    this.directory = directory;
    this.segment = segment;
    boolean success = false;
    try {
      out = directory.createOutput(IndexFileNames.segmentFileName(segment, "", FIELDS_EXTENSION), context);
      success = true;
    } finally {
      if (!success) {
        abort();
      }
    }
  }

  @Override
  public void startDocument(int numStoredFields) throws IOException {
    write(DOC);
    write(Integer.toString(numDocsWritten));
    newLine();
    
    write(NUM);
    write(Integer.toString(numStoredFields));
    newLine();
    
    numDocsWritten++;
  }

  @Override
  public void writeField(FieldInfo info, IndexableField field) throws IOException {
    write(FIELD);
    write(Integer.toString(info.number));
    newLine();
    
    write(NAME);
    write(field.name());
    newLine();
    
    write(TYPE);
    final NumericField.DataType numericType = field.numericDataType();

    if (numericType != null) {
      switch (numericType) {
        case INT:
          write(TYPE_INT);
          newLine();
          
          write(VALUE);
          write(Integer.toString(field.numericValue().intValue()));
          newLine();
          
          break;
        case LONG:
          write(TYPE_LONG);
          newLine();
          
          write(VALUE);
          write(Long.toString(field.numericValue().longValue()));
          newLine();
          
          break;
        case FLOAT:
          write(TYPE_FLOAT);
          newLine();
          
          write(VALUE);
          write(Float.toString(field.numericValue().floatValue()));
          newLine();
          
          break;
        case DOUBLE:
          write(TYPE_DOUBLE);
          newLine();
          
          write(VALUE);
          write(Double.toString(field.numericValue().doubleValue()));
          newLine();
          
          break;
        default:
          assert false : "Should never get here";
      }
    } else { 
      BytesRef bytes = field.binaryValue();
      if (bytes != null) {
        write(TYPE_BINARY);
        newLine();
        
        write(VALUE);
        write(bytes);
        newLine();
      } else if (field.stringValue() == null) {
        throw new IllegalArgumentException("field " + field.name() + " is stored but does not have binaryValue, stringValue nor numericValue");
      } else {
        write(TYPE_STRING);
        newLine();
        
        write(VALUE);
        write(field.stringValue());
        newLine();
      }
    }
  }

  @Override
  public void abort() {
    try {
      close();
    } catch (IOException ignored) {}
    IOUtils.deleteFilesIgnoringExceptions(directory, IndexFileNames.segmentFileName(segment, "", FIELDS_EXTENSION));
  }

  @Override
  public void finish(int numDocs) throws IOException {
    if (numDocsWritten != numDocs) {
      throw new RuntimeException("mergeFields produced an invalid result: docCount is " + numDocs 
          + " but only saw " + numDocsWritten + " file=" + out.toString() + "; now aborting this merge to prevent index corruption");
    }
    write(END);
    newLine();
  }

  @Override
  public void close() throws IOException {
    try {
      IOUtils.close(out);
    } finally {
      out = null;
    }
  }
  
  private void write(String s) throws IOException {
    SimpleTextUtil.write(out, s, scratch);
  }
  
  private void write(BytesRef bytes) throws IOException {
    SimpleTextUtil.write(out, bytes);
  }
  
  private void newLine() throws IOException {
    SimpleTextUtil.writeNewline(out);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1086.java