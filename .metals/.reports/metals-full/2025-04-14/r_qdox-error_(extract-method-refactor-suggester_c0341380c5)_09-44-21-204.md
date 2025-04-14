error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5128.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5128.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,28]

error in qdox parser
file content:
```java
offset: 28
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5128.java
text:
```scala
public static final native i@@nt /*long*/ getenv (byte[] env);

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

public class C extends Platform {

	static {
		if ("Linux".equals (System.getProperty ("os.name")) && "motif".equals (Platform.PLATFORM)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			try {
				Library.loadLibrary ("libXm.so.2", false); //$NON-NLS-1$
			} catch (Throwable ex) {}
		}
		Library.loadLibrary ("swt"); //$NON-NLS-1$
	}

	public static final int PTR_SIZEOF = PTR_sizeof ();

public static final native void free (int /*long*/ ptr);
public static final native int /*long*/ getenv (byte[] wcsToMbcs);
public static final native int /*long*/ malloc (int /*long*/ size);
public static final native void memmove (int /*long*/ dest, byte[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, char[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, double[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, float[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, int[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, long[] src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, short[] src, int /*long*/ size);
public static final native void memmove (byte[] dest, char[] src, int /*long*/ size);
public static final native void memmove (byte[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (int /*long*/ dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (char[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (double[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (float[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (int[] dest, byte[] src, int /*long*/ size);
public static final native void memmove (short[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (int[] dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove (long[] dest, int /*long*/ src, int /*long*/ size);
public static final native int /*long*/ memset (int /*long*/ buffer, int c, int /*long*/ num);
public static final native int PTR_sizeof ();
public static final native int strlen (int /*long*/ s);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5128.java