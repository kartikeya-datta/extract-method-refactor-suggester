error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9267.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9267.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9267.java
text:
```scala
public b@@oolean containsAll(Collection<?> c) {

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.util;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Simple {@link List} wrapper class that allows for elements to be
 * automatically populated as they are requested. This is particularly
 * useful for data binding to {@link List Lists}, allowing for elements
 * to be created and added to the {@link List} in a "just in time" fashion.
 *
 * <p>Note: This class is not thread-safe. To create a thread-safe version,
 * use the {@link java.util.Collections#synchronizedList} utility methods.
 *
 * <p>Inspired by {@code LazyList} from Commons Collections.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
@SuppressWarnings("serial")
public class AutoPopulatingList<E> implements List<E>, Serializable {

	/**
	 * The {@link List} that all operations are eventually delegated to.
	 */
	private final List<E> backingList;

	/**
	 * The {@link ElementFactory} to use to create new {@link List} elements
	 * on demand.
	 */
	private final ElementFactory<E> elementFactory;


	/**
	 * Creates a new {@code AutoPopulatingList} that is backed by a standard
	 * {@link ArrayList} and adds new instances of the supplied {@link Class element Class}
	 * to the backing {@link List} on demand.
	 */
	public AutoPopulatingList(Class<? extends E> elementClass) {
		this(new ArrayList<E>(), elementClass);
	}

	/**
	 * Creates a new {@code AutoPopulatingList} that is backed by the supplied {@link List}
	 * and adds new instances of the supplied {@link Class element Class} to the backing
	 * {@link List} on demand.
	 */
	public AutoPopulatingList(List<E> backingList, Class<? extends E> elementClass) {
		this(backingList, new ReflectiveElementFactory<E>(elementClass));
	}

	/**
	 * Creates a new {@code AutoPopulatingList} that is backed by a standard
	 * {@link ArrayList} and creates new elements on demand using the supplied {@link ElementFactory}.
	 */
	public AutoPopulatingList(ElementFactory<E> elementFactory) {
		this(new ArrayList<E>(), elementFactory);
	}

	/**
	 * Creates a new {@code AutoPopulatingList} that is backed by the supplied {@link List}
	 * and creates new elements on demand using the supplied {@link ElementFactory}.
	 */
	public AutoPopulatingList(List<E> backingList, ElementFactory<E> elementFactory) {
		Assert.notNull(backingList, "Backing List must not be null");
		Assert.notNull(elementFactory, "Element factory must not be null");
		this.backingList = backingList;
		this.elementFactory = elementFactory;
	}


	@Override
	public void add(int index, E element) {
		this.backingList.add(index, element);
	}

	@Override
	public boolean add(E o) {
		return this.backingList.add(o);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.backingList.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.backingList.addAll(index, c);
	}

	@Override
	public void clear() {
		this.backingList.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.backingList.contains(o);
	}

	@Override
	public boolean containsAll(Collection c) {
		return this.backingList.containsAll(c);
	}

	/**
	 * Get the element at the supplied index, creating it if there is
	 * no element at that index.
	 */
	@Override
	public E get(int index) {
		int backingListSize = this.backingList.size();
		E element = null;
		if (index < backingListSize) {
			element = this.backingList.get(index);
			if (element == null) {
				element = this.elementFactory.createElement(index);
				this.backingList.set(index, element);
			}
		}
		else {
			for (int x = backingListSize; x < index; x++) {
				this.backingList.add(null);
			}
			element = this.elementFactory.createElement(index);
			this.backingList.add(element);
		}
		return element;
	}

	@Override
	public int indexOf(Object o) {
		return this.backingList.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return this.backingList.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return this.backingList.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.backingList.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return this.backingList.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return this.backingList.listIterator(index);
	}

	@Override
	public E remove(int index) {
		return this.backingList.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		return this.backingList.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.backingList.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.backingList.retainAll(c);
	}

	@Override
	public E set(int index, E element) {
		return this.backingList.set(index, element);
	}

	@Override
	public int size() {
		return this.backingList.size();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return this.backingList.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return this.backingList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.backingList.toArray(a);
	}


	@Override
	public boolean equals(Object other) {
		return this.backingList.equals(other);
	}

	@Override
	public int hashCode() {
		return this.backingList.hashCode();
	}


	/**
	 * Factory interface for creating elements for an index-based access
	 * data structure such as a {@link java.util.List}.
	 */
	public interface ElementFactory<E> {

		/**
		 * Create the element for the supplied index.
		 * @return the element object
		 * @throws ElementInstantiationException if the instantiation process failed
		 * (any exception thrown by a target constructor should be propagated as-is)
		 */
		E createElement(int index) throws ElementInstantiationException;
	}


	/**
	 * Exception to be thrown from ElementFactory.
	 */
	public static class ElementInstantiationException extends RuntimeException {

		public ElementInstantiationException(String msg) {
			super(msg);
		}
	}


	/**
	 * Reflective implementation of the ElementFactory interface,
	 * using {@code Class.newInstance()} on a given element class.
	 * @see Class#newInstance()
	 */
	private static class ReflectiveElementFactory<E> implements ElementFactory<E>, Serializable {

		private final Class<? extends E> elementClass;

		public ReflectiveElementFactory(Class<? extends E> elementClass) {
			Assert.notNull(elementClass, "Element clas must not be null");
			Assert.isTrue(!elementClass.isInterface(), "Element class must not be an interface type");
			Assert.isTrue(!Modifier.isAbstract(elementClass.getModifiers()), "Element class cannot be an abstract class");
			this.elementClass = elementClass;
		}

		@Override
		public E createElement(int index) {
			try {
				return this.elementClass.newInstance();
			}
			catch (InstantiationException ex) {
				throw new ElementInstantiationException("Unable to instantiate element class [" +
						this.elementClass.getName() + "]. Root cause is " + ex);
			}
			catch (IllegalAccessException ex) {
				throw new ElementInstantiationException("Cannot access element class [" +
						this.elementClass.getName() + "]. Root cause is " + ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9267.java