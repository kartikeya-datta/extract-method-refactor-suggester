error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9449.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9449.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9449.java
text:
```scala
public O@@bject clone()throws CloneNotSupportedException

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.util.collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class implements the <tt>Set</tt> interface, backed by a ConcurrentHashMap instance.
 * 
 * @author Matt Tucker
 * @param <E>
 */
public class ConcurrentHashSet<E> extends AbstractSet<E>
	implements
		Set<E>,
		Cloneable,
		java.io.Serializable
{
	/** */
	private static final long serialVersionUID = 1L;

	private transient ConcurrentHashMap<E, Object> map;

	// Dummy value to associate with an Object in the backing Map
	private static final Object PRESENT = new Object();

	/**
	 * Constructs a new, empty set; the backing <tt>ConcurrentHashMap</tt> instance has default
	 * initial capacity (16) and load factor (0.75).
	 */
	public ConcurrentHashSet()
	{
		map = new ConcurrentHashMap<E, Object>();
	}

	/**
	 * Constructs a new set containing the elements in the specified collection. The
	 * <tt>ConcurrentHashMap</tt> is created with default load factor (0.75) and an initial capacity
	 * sufficient to contain the elements in the specified collection.
	 * 
	 * @param c
	 *            the collection whose elements are to be placed into this set.
	 * @throws NullPointerException
	 *             if the specified collection is null.
	 */
	public ConcurrentHashSet(final Collection<? extends E> c)
	{
		map = new ConcurrentHashMap<E, Object>(Math.max((int)(c.size() / .75f) + 1, 16));
		addAll(c);
	}

	/**
	 * Constructs a new, empty set; the backing <tt>ConcurrentHashMap</tt> instance has the
	 * specified initial capacity and the specified load factor.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the hash map.
	 * @param loadFactor
	 *            the load factor of the hash map.
	 * @throws IllegalArgumentException
	 *             if the initial capacity is less than zero, or if the load factor is nonpositive.
	 */
	public ConcurrentHashSet(final int initialCapacity, final float loadFactor)
	{
		map = new ConcurrentHashMap<E, Object>(initialCapacity, loadFactor, 16);
	}

	/**
	 * Constructs a new, empty set; the backing <tt>HashMap</tt> instance has the specified initial
	 * capacity and default load factor, which is <tt>0.75</tt>.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the hash table.
	 * @throws IllegalArgumentException
	 *             if the initial capacity is less than zero.
	 */
	public ConcurrentHashSet(final int initialCapacity)
	{
		map = new ConcurrentHashMap<E, Object>(initialCapacity);
	}

	/**
	 * 
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<E> iterator()
	{
		return map.keySet().iterator();
	}

	/**
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size()
	{
		return map.size();
	}

	/**
	 * 
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	/**
	 * 
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(final Object o)
	{
		return map.containsKey(o);
	}

	/**
	 * 
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(final E o)
	{
		return map.put(o, PRESENT) == null;
	}

	/**
	 * 
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object o)
	{
		return map.remove(o) == PRESENT;
	}

	/**
	 * 
	 * @see java.util.AbstractCollection#clear()
	 */
	@Override
	public void clear()
	{
		map.clear();
	}

	/**
	 * 
	 * @see java.lang.Object#clone()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object clone()
	{
		try
		{
			ConcurrentHashSet<E> newSet = (ConcurrentHashSet<E>)super.clone();
			newSet.map.putAll(map);
			return newSet;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * 
	 * @param s
	 * @throws java.io.IOException
	 */
	private void writeObject(final java.io.ObjectOutputStream s) throws java.io.IOException
	{
		s.defaultWriteObject();
		s.writeInt(map.size());

		for (E key : map.keySet())
		{
			s.writeObject(key);
		}
	}

	/**
	 * Re-constitute the <tt>HashSet</tt> instance from a stream.
	 * 
	 * @param inputStream
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void readObject(final ObjectInputStream inputStream) throws ClassNotFoundException,
		IOException
	{
		inputStream.defaultReadObject();

		map = new ConcurrentHashMap<E, Object>();

		int size = inputStream.readInt();
		for (int i = 0; i < size; i++)
		{
			E e = (E)inputStream.readObject();
			map.put(e, PRESENT);
		}
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9449.java