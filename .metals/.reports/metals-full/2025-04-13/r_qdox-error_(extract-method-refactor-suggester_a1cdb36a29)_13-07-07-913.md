error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5415.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5415.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5415.java
text:
```scala
private final static S@@tring M1_NAME = "M1"; //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.keys;

import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.swt.SWT;

/**
 * <p>
 * Instances of <code>ModifierKey</code> represent the four keys on the
 * keyboard recognized by convention as 'modifier keys', those keys typically
 * pressed in combination with themselves and/or a 'natural key'.
 * </p>
 * <p>
 * <code>ModifierKey</code> objects are immutable. Clients are not permitted
 * to extend this class.
 * </p>
 * <p>
 * <em>EXPERIMENTAL</em>
 * </p>
 * 
 * @since 3.0
 */
public final class ModifierKey extends Key {

	/**
	 * An internal map used to lookup instances of <code>ModifierKey</code>
	 * given the formal string representation of a modifier key.
	 */
	static SortedMap modifierKeysByName = new TreeMap();

	/**
	 * The name of the 'Alt' key.
	 */
	private final static String ALT_NAME = "ALT"; //$NON-NLS-1$

	/**
	 * The single static instance of <code>ModifierKey</code> which
	 * represents the 'Alt' key.
	 */
	public final static ModifierKey ALT = new ModifierKey(ALT_NAME);

	/**
	 * The name of the 'Command' key.
	 */
	private final static String COMMAND_NAME = "COMMAND"; //$NON-NLS-1$

	/**
	 * The single static instance of <code>ModifierKey</code> which
	 * represents the 'Command' key.
	 */
	public final static ModifierKey COMMAND = new ModifierKey(COMMAND_NAME);

	/**
	 * The name of the 'Ctrl' key.
	 */
	private final static String CTRL_NAME = "CTRL"; //$NON-NLS-1$

	/**
	 * The single static instance of <code>ModifierKey</code> which
	 * represents the 'Ctrl' key.
	 */
	public final static ModifierKey CTRL = new ModifierKey(CTRL_NAME);

	/**
	 * The name of the 'M1' key.
	 */
	public final static String M1_NAME = "M1"; //$NON-NLS-1$	

	/**
	 * The name of the 'M2' key.
	 */
	private final static String M2_NAME = "M2"; //$NON-NLS-1$

	/**
	 * The name of the 'M3' key.
	 */
	private final static String M3_NAME = "M3"; //$NON-NLS-1$

	/**
	 * The name of the 'M4' key.
	 */
	private final static String M4_NAME = "M4"; //$NON-NLS-1$

	/**
	 * The name of the 'Shift' key.
	 */
	private final static String SHIFT_NAME = "SHIFT"; //$NON-NLS-1$	

	/**
	 * The single static instance of <code>ModifierKey</code> which
	 * represents the 'Shift' key.
	 */
	public final static ModifierKey SHIFT = new ModifierKey(SHIFT_NAME);

	static {
		modifierKeysByName.put(ModifierKey.ALT.toString(), ModifierKey.ALT);
		modifierKeysByName.put(ModifierKey.COMMAND.toString(), ModifierKey.COMMAND);
		modifierKeysByName.put(ModifierKey.CTRL.toString(), ModifierKey.CTRL);
		modifierKeysByName.put(ModifierKey.SHIFT.toString(), ModifierKey.SHIFT);
		modifierKeysByName.put(M1_NAME, "carbon".equals(SWT.getPlatform()) ? ModifierKey.COMMAND : ModifierKey.CTRL); //$NON-NLS-1$
		modifierKeysByName.put(M2_NAME, ModifierKey.SHIFT);
		modifierKeysByName.put(M3_NAME, ModifierKey.ALT);
		modifierKeysByName.put(M4_NAME, "carbon".equals(SWT.getPlatform()) ? ModifierKey.CTRL : ModifierKey.COMMAND); //$NON-NLS-1$
	}

	/**
	 * Constructs an instance of <code>ModifierKey</code> given a name.
	 * 
	 * @param name
	 *            The name of the key, must not be null.
	 */
	private ModifierKey(String name) {
		super(name);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5415.java