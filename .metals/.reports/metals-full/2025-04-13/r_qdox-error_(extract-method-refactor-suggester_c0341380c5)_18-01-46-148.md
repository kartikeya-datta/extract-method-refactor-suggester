error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/160.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/160.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/160.java
text:
```scala
public static final C@@omparator<Object> termComparator = new Comparator<Object>() {

package org.apache.lucene.store.instantiated;

/**
 * Copyright 2006 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.util.Comparator;

import org.apache.lucene.index.Term;

/**
 * A term in the inverted index, coupled to the documents it occurs in.
 *
 * @see org.apache.lucene.index.Term
 */
public class InstantiatedTerm
    implements Serializable {

  private static final long serialVersionUID = 1l;

  public static final Comparator<InstantiatedTerm> comparator = new Comparator<InstantiatedTerm>() {
    public int compare(InstantiatedTerm instantiatedTerm, InstantiatedTerm instantiatedTerm1) {
      return instantiatedTerm.getTerm().compareTo(instantiatedTerm1.getTerm());
    }
  };

  public static final Comparator termComparator = new Comparator() {
    public int compare(Object o, Object o1) {
      return ((InstantiatedTerm)o).getTerm().compareTo((Term)o1);
    }
  };
  
  private Term term;

  /**
   * index of term in InstantiatedIndex
   * @see org.apache.lucene.store.instantiated.InstantiatedIndex#getOrderedTerms() */
  private int termIndex;

  /**
   * @return Term associated with this entry of the index object graph
   */
  public Term getTerm() {
    return term;
  }

  InstantiatedTerm(String field, String text) {
    this.term = new Term(field, text);
  }

//  this could speed up TermDocs.skipTo even more
//  private Map</** document number*/Integer, /** index in associatedDocuments */Integer> associatedDocumentIndexByDocumentNumber = new HashMap<Integer, Integer>();
//
//  public Map</** document number*/Integer, /** index in associatedDocuments */Integer> getAssociatedDocumentIndexByDocumentNumber() {
//    return associatedDocumentIndexByDocumentNumber;
//  }

  /** Ordered by document number */
  private InstantiatedTermDocumentInformation[] associatedDocuments;

  /**
   * Meta data per document in which this term is occurring.
   * Ordered by document number.
   *
   * @return Meta data per document in which this term is occurring.
   */
  public InstantiatedTermDocumentInformation[] getAssociatedDocuments() {
    return associatedDocuments;
  }


  /**
   * Meta data per document in which this term is occurring.
   * Ordered by document number.
   *
   * @param associatedDocuments meta data per document in which this term is occurring, ordered by document number
   */
  void setAssociatedDocuments(InstantiatedTermDocumentInformation[] associatedDocuments) {
    this.associatedDocuments = associatedDocuments;
  }

  /**
   * Finds index to the first beyond the current whose document number is
   * greater than or equal to <i>target</i>, -1 if there is no such element.
   *
   * @param target the document number to match
   * @return -1 if there is no such element
   */
  public int seekCeilingDocumentInformationIndex(int target) {
    return seekCeilingDocumentInformationIndex(target, 0, getAssociatedDocuments().length);
  }

  /**
   * Finds index to the first beyond the current whose document number is
   * greater than or equal to <i>target</i>, -1 if there is no such element.
   *
   * @param target the document number to match
   * @param startOffset associated documents index start offset
   * @return -1 if there is no such element
   */
  public int seekCeilingDocumentInformationIndex(int target, int startOffset) {
    return seekCeilingDocumentInformationIndex(target, startOffset, getAssociatedDocuments().length);
  }

  /**
   * Finds index to the first beyond the current whose document number is
   * greater than or equal to <i>target</i>, -1 if there is no such element.
   *
   * @param target the document number to match
   * @param startOffset associated documents index start offset
   * @param endPosition associated documents index end position
   * @return -1 if there is no such element
   */
  public int seekCeilingDocumentInformationIndex(int target, int startOffset, int endPosition) {

    int pos = binarySearchAssociatedDocuments(target, startOffset, endPosition - startOffset);

//    int pos = Arrays.binarySearch(getAssociatedDocuments(), target, InstantiatedTermDocumentInformation.doumentNumberIntegerComparator);

    if (pos < 0) {
      pos = -1 - pos;
    }
    if (getAssociatedDocuments().length <= pos) {
      return -1;
    } else {
      return pos;
    }
  }

  public int binarySearchAssociatedDocuments(int target) {
    return binarySearchAssociatedDocuments(target, 0);
  }

  public int binarySearchAssociatedDocuments(int target, int offset) {
    return binarySearchAssociatedDocuments(target, offset, associatedDocuments.length - offset);
  }

  /**
   * @param target value to search for in the array
   * @param offset index of the first valid value in the array
   * @param length number of valid values in the array
   * @return index of an occurrence of key in array, or -(insertionIndex + 1) if key is not contained in array (<i>insertionIndex</i> is then the index at which key could be inserted).
   */
  public int binarySearchAssociatedDocuments(int target, int offset, int length) {

    // implementation originally from http://ochafik.free.fr/blog/?p=106

    if (length == 0) {
      return -1 - offset;
    }
    int min = offset, max = offset + length - 1;
    int minVal = getAssociatedDocuments()[min].getDocument().getDocumentNumber();
    int maxVal = getAssociatedDocuments()[max].getDocument().getDocumentNumber();


    int nPreviousSteps = 0;

    for (; ;) {

      // be careful not to compute key - minVal, for there might be an integer overflow.
      if (target <= minVal) return target == minVal ? min : -1 - min;
      if (target >= maxVal) return target == maxVal ? max : -2 - max;

      assert min != max;

      int pivot;
      // A typical binarySearch algorithm uses pivot = (min + max) / 2.
      // The pivot we use here tries to be smarter and to choose a pivot close to the expectable location of the key.
      // This reduces dramatically the number of steps needed to get to the key.
      // However, it does not work well with a logarithmic distribution of values, for instance.
      // When the key is not found quickly the smart way, we switch to the standard pivot.
      if (nPreviousSteps > 2) {
        pivot = (min + max) >> 1;
        // stop increasing nPreviousSteps from now on
      } else {
        // NOTE: We cannot do the following operations in int precision, because there might be overflows.
        //       long operations are slower than float operations with the hardware this was tested on (intel core duo 2, JVM 1.6.0).
        //       Overall, using float proved to be the safest and fastest approach.
        pivot = min + (int) ((target - (float) minVal) / (maxVal - (float) minVal) * (max - min));
        nPreviousSteps++;
      }

      int pivotVal = getAssociatedDocuments()[pivot].getDocument().getDocumentNumber();

      // NOTE: do not store key - pivotVal because of overflows
      if (target > pivotVal) {
        min = pivot + 1;
        max--;
      } else if (target == pivotVal) {
        return pivot;
      } else {
        min++;
        max = pivot - 1;
      }
      maxVal = getAssociatedDocuments()[max].getDocument().getDocumentNumber();
      minVal = getAssociatedDocuments()[min].getDocument().getDocumentNumber();
    }
  }


  /**
   * Navigates to the view of this occurrences of this term in a specific document. 
   *
   * This method is only used by InstantiatedIndex(IndexReader) and
   * should not be optimized for less CPU at the cost of more RAM.
   *
   * @param documentNumber the n:th document in the index
   * @return view of this term from specified document
   */
  public InstantiatedTermDocumentInformation getAssociatedDocument(int documentNumber) {
    int pos = binarySearchAssociatedDocuments(documentNumber);
    return pos < 0 ? null : getAssociatedDocuments()[pos];
  }

  public final String field() {
    return term.field();
  }

  public final String text() {
    return term.text();
  }

  @Override
  public String toString() {
    return term.toString();
  }


  public int getTermIndex() {
    return termIndex;
  }

  public void setTermIndex(int termIndex) {
    this.termIndex = termIndex;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/160.java