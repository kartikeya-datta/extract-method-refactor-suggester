error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9950.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9950.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9950.java
text:
```scala
public c@@har[] computeUniqueKey(boolean isLeaf) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

public final class BaseTypeBinding extends TypeBinding {

	public char[] simpleName;
	private char[] constantPoolName;

	BaseTypeBinding(int id, char[] name, char[] constantPoolName) {

		this.tagBits |= IsBaseType;
		this.id = id;
		this.simpleName = name;
		this.constantPoolName = constantPoolName;
	}

	/**
	 * int -> I
	 */
	public char[] computeUniqueKey(boolean withAccessFlags) {
		return constantPoolName();
	}
	
	/* Answer the receiver's constant pool name.
	*/
	public char[] constantPoolName() {

		return constantPoolName;
	}

	public PackageBinding getPackage() {

		return null;
	}

	/* Answer true if the receiver type can be assigned to the argument type (right)
	*/
	public final boolean isCompatibleWith(TypeBinding right) {

		if (this == right)
			return true;
		if (!right.isBaseType())
			return this == NullBinding;

		switch (right.id) {
			case T_boolean :
			case T_byte :
			case T_char :
				return false;
			case T_double :
				switch (id) {
					case T_byte :
					case T_char :
					case T_short :
					case T_int :
					case T_long :
					case T_float :
						return true;
					default :
						return false;
				}
			case T_float :
				switch (id) {
					case T_byte :
					case T_char :
					case T_short :
					case T_int :
					case T_long :
						return true;
					default :
						return false;
				}
			case T_long :
				switch (id) {
					case T_byte :
					case T_char :
					case T_short :
					case T_int :
						return true;
					default :
						return false;
				}
			case T_int :
				switch (id) {
					case T_byte :
					case T_char :
					case T_short :
						return true;
					default :
						return false;
				}
			case T_short :
				return (id == T_byte);
		}
		return false;
	}

	public static final boolean isNarrowing(int left, int right) {

		//can "left" store a "right" using some narrowing conversion
		//(is left smaller than right)
		switch (left) {
			case T_boolean :
				return right == T_boolean;
			case T_char :
			case T_byte :
				if (right == T_byte)
					return true;
			case T_short :
				if (right == T_short)
					return true;
				if (right == T_char)
					return true;
			case T_int :
				if (right == T_int)
					return true;
			case T_long :
				if (right == T_long)
					return true;
			case T_float :
				if (right == T_float)
					return true;
			case T_double :
				if (right == T_double)
					return true;
			default :
				return false;
		}
	}
	/**
	 * T_null is acting as an unchecked exception
	 * @see org.eclipse.jdt.internal.compiler.lookup.TypeBinding#isUncheckedException(boolean)
	 */
	public boolean isUncheckedException(boolean includeSupertype) {
		return this == NullBinding;
	}
	public static final boolean isWidening(int left, int right) {

		//can "left" store a "right" using some widening conversion
		//(is left "bigger" than right)
		switch (left) {
			case T_boolean :
				return right == T_boolean;
			case T_char :
				return right == T_char;
			case T_double :
				if (right == T_double)
					return true;
			case T_float :
				if (right == T_float)
					return true;
			case T_long :
				if (right == T_long)
					return true;
			case T_int :
				if (right == T_int)
					return true;
				if (right == T_char)
					return true;
			case T_short :
				if (right == T_short)
					return true;
			case T_byte :
				if (right == T_byte)
					return true;
			default :
				return false;
		}
	}
	/**
	 * @see org.eclipse.jdt.internal.compiler.lookup.Binding#kind()
	 */
	public int kind() {
		return Binding.BASE_TYPE;
	}
	public char[] qualifiedSourceName() {
		return simpleName;
	}

	public char[] readableName() {
		return simpleName;
	}

	public char[] shortReadableName() {
		return simpleName;
	}

	public char[] sourceName() {
		return simpleName;
	}

	public String toString() {
		return new String(constantPoolName) + " (id=" + id + ")"; //$NON-NLS-1$ //$NON-NLS-2$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9950.java