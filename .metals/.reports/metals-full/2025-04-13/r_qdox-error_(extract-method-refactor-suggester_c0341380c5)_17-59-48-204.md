error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6033.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6033.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[52,1]

error in qdox parser
file content:
```java
offset: 2770
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6033.java
text:
```scala
public class CyclicBufferList extends AbstractList implements List {

/*
 * ============================================================================
 *                   The Apache Software License, Version 1.1
 * ============================================================================
 *
 *    Copyright (C) 1999 The Apache Software Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modifica-
 * tion, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of  source code must  retain the above copyright  notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include  the following  acknowledgment:  "This product includes  software
 *    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
 *    Alternately, this  acknowledgment may  appear in the software itself,  if
 *    and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "log4j" and  "Apache Software Foundation"  must not be used to
 *    endorse  or promote  products derived  from this  software without  prior
 *    written permission. For written permission, please contact
 *    apache@apache.org.
 *
 * 5. Products  derived from this software may not  be called "Apache", nor may
 *    "Apache" appear  in their name,  without prior written permission  of the
 *    Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software  consists of voluntary contributions made  by many individuals
 * on  behalf of the Apache Software  Foundation.  For more  information on the
 * Apache Software Foundation, please see <http://www.apache.org/>.
 *
 */

p@@ackage org.apache.log4j.chainsaw;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * CyclicBuffer implementation that is Object generic, and implements the List interface.
 *
 * Original CyclicBuffer @author Ceki G&uuml;lc&uuml;
 *
 * This implementation (although there's very little change) @author Paul Smith <psmith@apache.org>
 *
 */
class CyclicBufferList extends AbstractList implements List {
  Object[] ea;
  int first;
  int last;
  int numElems;
  int maxSize;

  /**
     Instantiate a new CyclicBuffer of at most <code>maxSize</code> events.

     The <code>maxSize</code> argument must a positive integer.

     @param maxSize The maximum number of elements in the buffer.
  */
  public CyclicBufferList(int maxSize) {
    if (maxSize < 1) {
      throw new IllegalArgumentException(
        "The maxSize argument (" + maxSize + ") is not a positive integer.");
    }
    this.maxSize = maxSize;
    clear();
  }

  public CyclicBufferList() {
    this(5000);
  }

  /**
   * Check if the given index is in range.  If not, throw an appropriate
   * runtime exception.
   */
  private void RangeCheck(int index) {
    if ((index >= ea.length) || (index < 0)) {
      throw new IndexOutOfBoundsException(
        "Index: " + index + ", Size: " + ea.length);
    }
  }

  /**
   * Removes the element at the specified position in this list.
   * Shifts any subsequent elements to the left (subtracts one from their
   * indices).
   *
   * @param index the index of the element to removed.
   * @return the element that was removed from the list.
   * @throws    IndexOutOfBoundsException if index out of range <tt>(index
   *      &lt; 0 || index &gt;= size())</tt>.
   */
  public Object remove(int index) {
    Object oldValue = ea[index];

    List list = new ArrayList(Arrays.asList(ea));
    list.remove(index);
    ea = list.toArray(ea);
    numElems = ea.length;
    
    numElems--;
    if (--last <= 0) {
      last = numElems;
    }

    if (first == maxSize) {
      first = 0;
    }
    return oldValue;
  }

  public Object set(int index, Object element) {
    Object previous = ea[index];
    ea[index] = element;

    return previous;
  }

  /**
     Add an <code>event</code> as the last event in the buffer.

   */
  public boolean add(Object event) {
    ea[last] = event;

    if (++last == maxSize) {
      last = 0;
    }

    if (numElems < maxSize) {
      numElems++;
    } else if (++first == maxSize) {
      first = 0;
    }

    return true;
  }

  /**
     Get the <i>i</i>th oldest event currently in the buffer. If
     <em>i</em> is outside the range 0 to the number of elements
     currently in the buffer, then <code>null</code> is returned.


  */
  public Object get(int i) {
    if ((i < 0) || (i >= numElems)) {
      return null;
    }

    return ea[(first + i) % maxSize];
  }

  public int getMaxSize() {
    return maxSize;
  }

  /**
     Get the oldest (first) element in the buffer. The oldest element
     is removed from the buffer.
  */
  public Object get() {
    Object r = null;

    if (numElems > 0) {
      numElems--;
      r = ea[first];
      ea[first] = null;

      if (++first == maxSize) {
        first = 0;
      }
    }

    return r;
  }

  /**
     Get the number of elements in the buffer. This number is
     guaranteed to be in the range 0 to <code>maxSize</code>
     (inclusive).
  */
  public int size() {
    return numElems;
  }

  /**
     Resize the cyclic buffer to <code>newSize</code>.

     @throws IllegalArgumentException if <code>newSize</code> is negative.
   */
  public void resize(int newSize) {
    if (newSize < 0) {
      throw new IllegalArgumentException(
        "Negative array size [" + newSize + "] not allowed.");
    }

    if (newSize == numElems) {
      return; // nothing to do
    }

    Object[] temp = new Object[newSize];

    int loopLen = (newSize < numElems) ? newSize : numElems;

    for (int i = 0; i < loopLen; i++) {
      temp[i] = ea[first];
      ea[first] = null;

      if (++first == numElems) {
        first = 0;
      }
    }

    ea = temp;
    first = 0;
    numElems = loopLen;
    maxSize = newSize;

    if (loopLen == newSize) {
      last = 0;
    } else {
      last = loopLen;
    }
  }
  /* (non-Javadoc)
   * @see java.util.Collection#clear()
   */
  public void clear() {
    ea = new Object[maxSize];
    first = 0;
    last = 0;
    numElems = 0;
    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6033.java