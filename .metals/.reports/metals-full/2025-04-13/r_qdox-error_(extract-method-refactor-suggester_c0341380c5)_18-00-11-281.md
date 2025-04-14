error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8094.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8094.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8094.java
text:
```scala
s@@et.elementTable = new Object[length];

package org.eclipse.jdt.internal.compiler.util;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.Enumeration;
import org.eclipse.jdt.internal.compiler.*;

/**
 * Set of Objects
 */
public final class ObjectSet implements Cloneable {
	
	private Object[] elementTable;
	private int elementSize; // number of elements in the table
	private int threshold;
	
	public ObjectSet() {
		this(13);
	}

	public ObjectSet(int size) {

		this.elementSize = 0;
		this.threshold = size; // size represents the expected number of elements
		int extraRoom = (int) (size * 1.75f);
		if (this.threshold == extraRoom)
			extraRoom++;
		this.elementTable = new Object[extraRoom];
	}

	public Object clone() throws CloneNotSupportedException {
		
		ObjectSet set = (ObjectSet)super.clone();
		set.elementSize = this.elementSize;
		set.threshold = this.threshold;
		
		int length = this.elementTable.length;
		set.elementTable = new Object[length][];
		System.arraycopy(this.elementTable, 0, set.elementTable, 0, length);
		
		return set;
	}
	
	public boolean contains(Object element) {
		
		int length = elementTable.length;
		int index = (element.hashCode() & 0x7FFFFFFF) % length;
		Object currentElement;
		while ((currentElement = elementTable[index]) != null) {
			if (currentElement.equals(element)) return true;
			index = (index + 1) % length;
		}
		return false;
	}

	public boolean add(Object element) {

		int length = this.elementTable.length;
		int index = (element.hashCode() & 0x7FFFFFFF) % length;
		Object currentElement;
		while ((currentElement = this.elementTable[index]) != null) {
			if (currentElement.equals(element)) return false;
			index = (index + 1) % length;
		}
		this.elementTable[index] = element;

		// assumes the threshold is never equal to the size of the table
		if (++elementSize > threshold)
			rehash();
		return true;
	}

	public void addAll(Object[] elements) {

		for (int i = 0, length = elements.length; i < length; i++){
			add(elements[i]);
		}
	}

	public void addAll(ObjectSet set) {

		for (int i = 0, length = set.elementTable.length; i < length; i++){
			Object item = set.elementTable[i];
			if (item != null) add(item);
		}
	}

	public void copyInto(Object[] targetArray){
		
		int index = 0;
		for (int i = 0, length = this.elementTable.length; i < length; i++){
			if (elementTable[i] != null){
				targetArray[index++] = this.elementTable[i];
			}
		}
	}

	public Enumeration elements(){
		
		return new Enumeration(){
			int index = 0;
			int count = 0;
			public boolean hasMoreElements(){
				return this.count < ObjectSet.this.elementSize;
			}
			public Object nextElement(){
				do {
					Object current = ObjectSet.this.elementTable[index++];
					if (current != null){
						count++;
						return current;
					}
				} while(this.index < ObjectSet.this.elementTable.length);
				return null;
			}	
		};
	}
	
	public boolean isEmpty(){
		return this.elementSize == 0;
	}

	public boolean remove(Object element) {

		int hash = element.hashCode();
		int length = this.elementTable.length;
		int index = (hash & 0x7FFFFFFF) % length;
		Object currentElement;
		while ((currentElement = elementTable[index]) != null) {
			if (currentElement.equals(element)){
				this.elementTable[index] = null;
				this.elementSize--;
				
				// local rehash - find the last matching element with same hashcode
				// and move it in place of the current one if any
				int next = index, lastMatching = -1;
				while (this.elementTable[next = (next + 1) % length] != null && next != index) {
					if (this.elementTable[next].hashCode() == hash){
						lastMatching = next;
					}
				}
				if (lastMatching > 0){
					this.elementTable[index] = this.elementTable[lastMatching];
					this.elementTable[lastMatching] = null;
				}
				return true;
			}
			index = (index + 1) % length;
		}
		return false;
	}

	public void removeAll() {
		
		for (int i = this.elementTable.length; --i >= 0;)
			this.elementTable[i] = null;
		this.elementSize = 0;
	}
	
	private void rehash() {

		ObjectSet newSet = new ObjectSet(elementSize * 2);
		// double the number of expected elements
		Object currentElement;
		for (int i = elementTable.length; --i >= 0;)
			if ((currentElement = elementTable[i]) != null)
				newSet.add(currentElement);

		this.elementTable = newSet.elementTable;
		this.threshold = newSet.threshold;
	}

	public int size() {
		return this.elementSize;
	}
	
	public String toString() {
		
		String s = "["; //$NON-NLS-1$
		Object object;
		int count = 0;
		for (int i = 0, length = elementTable.length; i < length; i++)
			if ((object = elementTable[i]) != null){
				if (count++ > 0) s += ", "; //$NON-NLS-1$
				s += elementTable[i]; 	
			}
		return s + "]";//$NON-NLS-1$
	}
	public String toDebugString() {
		String s = "#[\n"; //$NON-NLS-1$
		Object object;
		int count = 0;
		for (int i = 0, length = elementTable.length; i < length; i++){
			s += "\t"+i+"\t";//$NON-NLS-1$//$NON-NLS-2$
			object = elementTable[i];
			if (object == null){
				s+= "-\n";//$NON-NLS-1$
			} else {
				s+= object.toString()+ "\t#"+object.hashCode() +"\n";//$NON-NLS-1$//$NON-NLS-2$
			}
		}
		return s + "]";//$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8094.java