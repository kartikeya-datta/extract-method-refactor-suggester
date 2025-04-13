error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2883.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2883.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2883.java
text:
```scala
private b@@oolean tokenized = true;

package org.apache.lucene.document;

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

import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexableFieldType;
import org.apache.lucene.search.NumericRangeQuery; // javadocs
import org.apache.lucene.util.NumericUtils;

/**
 * Describes the properties of a field.
 */
public class FieldType implements IndexableFieldType {

  /** Data type of the numeric value
   * @since 3.2
   */
  public static enum NumericType {INT, LONG, FLOAT, DOUBLE}

  private boolean indexed;
  private boolean stored;
  private boolean tokenized;
  private boolean storeTermVectors;
  private boolean storeTermVectorOffsets;
  private boolean storeTermVectorPositions;
  private boolean omitNorms;
  private IndexOptions indexOptions = IndexOptions.DOCS_AND_FREQS_AND_POSITIONS;
  private DocValues.Type docValueType;
  private NumericType numericType;
  private boolean frozen;
  private int numericPrecisionStep = NumericUtils.PRECISION_STEP_DEFAULT;

  public FieldType(FieldType ref) {
    this.indexed = ref.indexed();
    this.stored = ref.stored();
    this.tokenized = ref.tokenized();
    this.storeTermVectors = ref.storeTermVectors();
    this.storeTermVectorOffsets = ref.storeTermVectorOffsets();
    this.storeTermVectorPositions = ref.storeTermVectorPositions();
    this.omitNorms = ref.omitNorms();
    this.indexOptions = ref.indexOptions();
    this.docValueType = ref.docValueType();
    this.numericType = ref.numericType();
    // Do not copy frozen!
  }
  
  public FieldType() {
  }

  private void checkIfFrozen() {
    if (frozen) {
      throw new IllegalStateException("this FieldType is already frozen and cannot be changed");
    }
  }

  /**
   * Prevents future changes. Note, it is recommended that this is called once
   * the FieldTypes's properties have been set, to prevent unintentional state
   * changes.
   */
  public void freeze() {
    this.frozen = true;
  }
  
  public boolean indexed() {
    return this.indexed;
  }
  
  public void setIndexed(boolean value) {
    checkIfFrozen();
    this.indexed = value;
  }

  public boolean stored() {
    return this.stored;
  }
  
  public void setStored(boolean value) {
    checkIfFrozen();
    this.stored = value;
  }

  public boolean tokenized() {
    return this.tokenized;
  }
  
  public void setTokenized(boolean value) {
    checkIfFrozen();
    this.tokenized = value;
  }

  public boolean storeTermVectors() {
    return this.storeTermVectors;
  }
  
  public void setStoreTermVectors(boolean value) {
    checkIfFrozen();
    this.storeTermVectors = value;
  }

  public boolean storeTermVectorOffsets() {
    return this.storeTermVectorOffsets;
  }
  
  public void setStoreTermVectorOffsets(boolean value) {
    checkIfFrozen();
    this.storeTermVectorOffsets = value;
  }

  public boolean storeTermVectorPositions() {
    return this.storeTermVectorPositions;
  }
  
  public void setStoreTermVectorPositions(boolean value) {
    checkIfFrozen();
    this.storeTermVectorPositions = value;
  }
  
  public boolean omitNorms() {
    return this.omitNorms;
  }
  
  public void setOmitNorms(boolean value) {
    checkIfFrozen();
    this.omitNorms = value;
  }

  public IndexOptions indexOptions() {
    return this.indexOptions;
  }
  
  public void setIndexOptions(IndexOptions value) {
    checkIfFrozen();
    this.indexOptions = value;
  }

  public void setDocValueType(DocValues.Type type) {
    checkIfFrozen();
    docValueType = type;
  }
  
  @Override
  public DocValues.Type docValueType() {
    return docValueType;
  }

  public void setNumericType(NumericType type) {
    checkIfFrozen();
    numericType = type;
  }

  /** NumericDataType; if
   *  non-null then the field's value will be indexed
   *  numerically so that {@link NumericRangeQuery} can be
   *  used at search time. */
  public NumericType numericType() {
    return numericType;
  }

  public void setNumericPrecisionStep(int precisionStep) {
    checkIfFrozen();
    if (precisionStep < 1) {
      throw new IllegalArgumentException("precisionStep must be >= 1 (got " + precisionStep + ")");
    }
    this.numericPrecisionStep = precisionStep;
  }

  /** Precision step for numeric field. */
  public int numericPrecisionStep() {
    return numericPrecisionStep;
  }

  /** Prints a Field for human consumption. */
  @Override
  public final String toString() {
    StringBuilder result = new StringBuilder();
    if (stored()) {
      result.append("stored");
    }
    if (indexed()) {
      if (result.length() > 0)
        result.append(",");
      result.append("indexed");
      if (tokenized()) {
        if (result.length() > 0)
          result.append(",");
        result.append("tokenized");
      }
      if (storeTermVectors()) {
        if (result.length() > 0)
          result.append(",");
        result.append("termVector");
      }
      if (storeTermVectorOffsets()) {
        if (result.length() > 0)
          result.append(",");
        result.append("termVectorOffsets");
      }
      if (storeTermVectorPositions()) {
        if (result.length() > 0)
          result.append(",");
        result.append("termVectorPosition");
      }
      if (omitNorms()) {
        result.append(",omitNorms");
      }
      if (indexOptions != IndexOptions.DOCS_AND_FREQS_AND_POSITIONS) {
        result.append(",indexOptions=");
        result.append(indexOptions);
      }
      if (numericType != null) {
        result.append(",numericType=");
        result.append(numericType);
        result.append(",numericPrecisionStep=");
        result.append(numericPrecisionStep);
      }
    }
    if (docValueType != null) {
      result.append(",docValueType=");
      result.append(docValueType);
    }
    
    return result.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2883.java