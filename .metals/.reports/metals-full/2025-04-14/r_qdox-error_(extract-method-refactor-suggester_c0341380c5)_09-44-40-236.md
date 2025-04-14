error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1611.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1611.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1611.java
text:
```scala
w@@hile (_termPositionsQueue.peek() != null && target > _termPositionsQueue.peek().doc())

package org.apache.lucene.index;

/**
 * Copyright 2004 The Apache Software Foundation
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.util.PriorityQueue;


/**
 * Describe class <code>MultipleTermPositions</code> here.
 *
 * @author Anders Nielsen
 * @version 1.0
 */
public class MultipleTermPositions
    implements TermPositions
{
    private static final class TermPositionsQueue
	extends PriorityQueue
    {
	TermPositionsQueue(List termPositions)
	    throws IOException
	{
	    initialize(termPositions.size());

	    Iterator i = termPositions.iterator();
	    while (i.hasNext())
	    {
		TermPositions tp = (TermPositions)i.next();
		if (tp.next())
		    put(tp);
	    }
	}

	final TermPositions peek()
	{
	    return (TermPositions)top();
	}

	public final boolean lessThan(Object a, Object b)
	{
	    return ((TermPositions)a).doc() < ((TermPositions)b).doc();
	}
    }

    private static final class IntQueue
    {
	private int _arraySize = 16;

	private int _index = 0;
	private int _lastIndex = 0;

	private int[] _array = new int[_arraySize];

	final void add(int i)
	{
	    if (_lastIndex == _arraySize)
		growArray();

	    _array[_lastIndex++] = i;
	}

	final int next()
	{
	    return _array[_index++];
	}

	final void sort()
	{
	    Arrays.sort(_array, _index, _lastIndex);
	}

	final void clear()
	{
	    _index = 0;
	    _lastIndex = 0;
	}

	final int size()
	{
	    return (_lastIndex-_index);
	}

	private void growArray()
	{
	    int[] newArray = new int[_arraySize*2];
	    System.arraycopy(_array, 0, newArray, 0, _arraySize);
	    _array = newArray;
	    _arraySize *= 2;
	}
    }

    private int _doc;
    private int _freq;

    private TermPositionsQueue _termPositionsQueue;
    private IntQueue _posList;

    /**
     * Creates a new <code>MultipleTermPositions</code> instance.
     *
     * @param indexReader an <code>IndexReader</code> value
     * @param terms a <code>Term[]</code> value
     * @exception IOException if an error occurs
     */
    public MultipleTermPositions(IndexReader indexReader, Term[] terms)
	throws IOException
    {
	List termPositions = new LinkedList();

	for (int i=0; i<terms.length; i++)
	    termPositions.add(indexReader.termPositions(terms[i]));

	_termPositionsQueue = new TermPositionsQueue(termPositions);
	_posList = new IntQueue();
    }

    public final boolean next()
	throws IOException
    {
	if (_termPositionsQueue.size() == 0)
	    return false;

	_posList.clear();
	_doc = _termPositionsQueue.peek().doc();

	TermPositions tp;
	do
	{
	    tp = _termPositionsQueue.peek();

	    for (int i=0; i<tp.freq(); i++)
		_posList.add(tp.nextPosition());

	    if (tp.next())
		_termPositionsQueue.adjustTop();
	    else
	    {
		_termPositionsQueue.pop();
		tp.close();
	    }
	}
	while (_termPositionsQueue.size() > 0 && _termPositionsQueue.peek().doc() == _doc);

	_posList.sort();
	_freq = _posList.size();

	return true;
    }

    public final int nextPosition()
    {
	return _posList.next();
    }

    public final boolean skipTo(int target)
	throws IOException
    {
	while (target > _termPositionsQueue.peek().doc())
	{
	    TermPositions tp = (TermPositions)_termPositionsQueue.pop();

	    if (tp.skipTo(target))
		_termPositionsQueue.put(tp);
	    else
		tp.close();
	}

	return next();
    }

    public final int doc()
    {
	return _doc;
    }

    public final int freq()
    {
	return _freq;
    }

    public final void close()
	throws IOException
    {
	while (_termPositionsQueue.size() > 0)
	    ((TermPositions)_termPositionsQueue.pop()).close();
    }

    /** Not implemented.
     * @throws UnsupportedOperationException
     */
    public void seek(Term arg0)
	throws IOException
    {
	throw new UnsupportedOperationException();
    }

    /** Not implemented.
     * @throws UnsupportedOperationException
     */
    public void seek(TermEnum termEnum) throws IOException {
      throw new UnsupportedOperationException();
    }

    /** Not implemented.
     * @throws UnsupportedOperationException
     */
    public int read(int[] arg0, int[] arg1)
	throws IOException
    {
	throw new UnsupportedOperationException();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1611.java