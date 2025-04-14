error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4846.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4846.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4846.java
text:
```scala
r@@eturn this.elements[i];

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.util;

public final class ObjectVector {
	
	static int INITIAL_SIZE = 10;

	public int size;
	int maxSize;
	Object[] elements;
	
	public ObjectVector() {
		this(INITIAL_SIZE);
	}
	
	public ObjectVector(int initialSize) {
		this.maxSize = initialSize > 0 ? initialSize : INITIAL_SIZE;
		this.size = 0;
		this.elements = new Object[this.maxSize];
	}

	public void add(Object newElement) {

		if (this.size == this.maxSize) // knows that size starts <= maxSize
			System.arraycopy(this.elements, 0, (this.elements = new Object[this.maxSize *= 2]), 0, this.size);
		this.elements[this.size++] = newElement;
	}

	public void addAll(Object[] newElements) {

		if (this.size + newElements.length >= this.maxSize) {
			maxSize = this.size + newElements.length; // assume no more elements will be added
			System.arraycopy(this.elements, 0, (this.elements = new Object[this.maxSize]), 0, this.size);
		}
		System.arraycopy(newElements, 0, this.elements, size, newElements.length);
		this.size += newElements.length;
	}

	public void addAll(ObjectVector newVector) {

		if (this.size + newVector.size >= this.maxSize) {
			maxSize = this.size + newVector.size; // assume no more elements will be added
			System.arraycopy(this.elements, 0, (this.elements = new Object[this.maxSize]), 0, this.size);
		}
		System.arraycopy(newVector.elements, 0, this.elements, size, newVector.size);
		this.size += newVector.size;
	}

	/**
	 * Identity check
	 */
	public boolean containsIdentical(Object element) {

		for (int i = this.size; --i >= 0;)
			if (element == this.elements[i])
				return true;
		return false;
	}

	/**
	 * Equality check
	 */
	public boolean contains(Object element) {

		for (int i = this.size; --i >= 0;)
			if (element.equals(this.elements[i]))
				return true;
		return false;
	}

	public void copyInto(Object[] targetArray){
		
		this.copyInto(targetArray, 0);
	}
	
	public void copyInto(Object[] targetArray, int index){
		
		System.arraycopy(this.elements, 0, targetArray, index, this.size);
	}	
	
	public Object elementAt(int index) {

		return this.elements[index];
	}

	public Object find(Object element) {

		for (int i = this.size; --i >= 0;)
			if (element.equals(this.elements[i]))
				return element;
		return null;
	}

	public Object remove(Object element) {

		// assumes only one occurrence of the element exists
		for (int i = this.size; --i >= 0;)
			if (element.equals(this.elements[i])) {
				// shift the remaining elements down one spot
				System.arraycopy(this.elements, i + 1, this.elements, i, --this.size - i);
				this.elements[this.size] = null;
				return element;
			}
		return null;
	}

	public void removeAll() {
		
		for (int i = this.size; --i >= 0;)
			this.elements[i] = null;
		this.size = 0;
	}

	public int size(){
		
		return this.size;
	}
	
	public String toString() {
		
		String s = ""; //$NON-NLS-1$
		for (int i = 0; i < this.size; i++)
			s += this.elements[i].toString() + "\n"; //$NON-NLS-1$
		return s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4846.java