error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3815.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3815.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3815.java
text:
```scala
b@@its.and(chain[i].bits(reader));

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

package org.apache.lucene.spatial;

import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.DocIdBitSet;

/**
 * 
 * Provide a serial chain filter, passing the bitset in with the
 * index reader to each of the filters in an ordered fashion.
 * 
 * Based off chain filter, but with some improvements to allow a narrowed down
 * filtering. Traditional filter required iteration through an IndexReader.
 * 
 * By implementing the ISerialChainFilter class, you can create a bits(IndexReader reader, BitSet bits)
 * @see org.apache.lucene.search.ISerialChainFilter
 * 
 */
public class SerialChainFilter extends Filter {

  /**
   * $Id: SerialChainFilter.java 136 2008-12-17 16:16:38Z ryantxu $
   */
  private static final long serialVersionUID = 1L;
  private Filter chain[];
  public static final int SERIALAND = 1;
  public static final int SERIALOR = 2;
  public static final int AND = 3;  // regular filters may be used first
  public static final int OR = 4;    // regular filters may be used first
  public static final int DEFAULT = SERIALOR;
  
  private int actionType[];
  
  public SerialChainFilter(Filter chain[]){
    this.chain = chain;
    this.actionType = new int[] {DEFAULT};
  }
  
  public SerialChainFilter(Filter chain[], int actionType[]){
    this.chain= chain;
    this.actionType = actionType;
  }
  
  /* (non-Javadoc)
   * @see org.apache.lucene.search.Filter#bits(org.apache.lucene.index.IndexReader)
   */
  @Override
  public BitSet bits(IndexReader reader) throws IOException {
	  return ((DocIdBitSet)getDocIdSet(reader)).getBitSet();
  }
  
  
  /* (non-Javadoc)
   * @see org.apache.lucene.search.Filter#getDocIdSet(org.apache.lucene.index.IndexReader)
   */
  @Override
  public DocIdSet getDocIdSet(IndexReader reader) throws CorruptIndexException, IOException {
    
    BitSet bits = new BitSet(reader.maxDoc());
    int chainSize = chain.length;
    int actionSize = actionType.length;
    int i = 0;
    
    /**
     * taken from ChainedFilter, first and on an empty bitset results in 0
     */
    if (actionType[i] == AND){
       try {
        bits = (BitSet) ((DocIdBitSet)chain[i].getDocIdSet(reader)).getBitSet().clone();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      ++i;
    }
    
    for( ; i < chainSize; i++) {
    
      int action = (i < actionSize)? actionType[i]: DEFAULT;
    
      switch (action){
      
      case (SERIALAND):
        try {
            bits.and(((ISerialChainFilter) chain[i]).bits(reader, bits));
          } catch (CorruptIndexException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        break;
      case (SERIALOR):
        try {
            bits.or(((ISerialChainFilter) chain[i]).bits(reader,bits));
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        break;
      case (AND):
        bits.and(((DocIdBitSet)chain[i].getDocIdSet(reader)).getBitSet());
        break;
      case (OR):
        bits.or(((DocIdBitSet)chain[i].getDocIdSet(reader)).getBitSet());
        break;
      }
    }
    return new DocIdBitSet(bits);
  }

  /**
   * @return the chain
   */
  Filter[] getChain() {
    return chain;
  }

  /**
   * @return the actionType
   */
  int[] getActionType() {
    return actionType;
  }

  /** 
   * Returns true if <code>o</code> is equal to this.
   * 
   * @see org.apache.lucene.search.RangeFilter#equals
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SerialChainFilter)) return false;
    SerialChainFilter other = (SerialChainFilter) o;

    if (this.chain.length != other.getChain().length ||
      this.actionType.length != other.getActionType().length)
      return false;
    
    for (int i = 0; i < this.chain.length; i++) {
      if (this.actionType[i] != other.getActionType()[i]  ||
        (!this.chain[i].equals(other.getChain()[i])))
        return false;
    }
    return true;
  }
    
  /** 
   * Returns a hash code value for this object.
   * 
   * @see org.apache.lucene.search.RangeFilter#hashCode
   */
  @Override
  public int hashCode() {
    if (chain.length == 0)
      return 0;

    int h = chain[0].hashCode() ^ new Integer(actionType[0]).hashCode(); 
    for (int i = 1; i < this.chain.length; i++) {
      h ^= chain[i].hashCode();
      h ^= new Integer(actionType[i]).hashCode();
    }
    return h;
  }
  
  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("SerialChainFilter(");
    for (int i = 0; i < chain.length; i++) {
      switch(actionType[i]) {
        case (SERIALAND): buf.append("SERIALAND"); break;
        case (SERIALOR):  buf.append("SERIALOR");  break;
        case (AND):       buf.append("AND");       break;
        case (OR):        buf.append("OR");        break;
        default:          buf.append(actionType[i]);
      }
      buf.append(" " + chain[i].toString() + " ");
    }
    return buf.toString().trim() + ")";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3815.java