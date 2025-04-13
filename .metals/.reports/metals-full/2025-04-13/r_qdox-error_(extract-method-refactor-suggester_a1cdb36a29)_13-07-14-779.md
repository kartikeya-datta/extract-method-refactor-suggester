error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7946.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7946.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7946.java
text:
```scala
public b@@oolean resizable = true;

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.layout;

import org.eclipse.swt.SWT;

/**
 * TrimLayoutData can be attached to a control in a TrimLayout to configure how
 * TrimLayout will arrange the control. TrimLayoutData can override
 * the control's preferred size. This is useful for attaching trim objects with
 * badly-behaved computeSize implementations. TrimLayoutData can also specify whether 
 * the control should be resized with the layout.
 * <p>
 * To create a fixed-size control based on its preferred size, use:
 * <code>
 * new TrimLayoutData(false, SWT.DEFAULT, SWT.DEFAULT)
 * </code> 
 * </p>
 * 
 * <p> 
 * To create a resizable control that will be resized according to the available
 * space in the layout, use:
 * <code>
 * new TrimLayoutData();
 * </code>
 * </p>
 * 
 * <p>
 * To create a control with a predetermined fixed size (that overrides the preferred size
 * of the control, use:
 * <code>
 * new TrimLayoutData(false, someFixedWidthInPixels, someFixedHeightInPixels);
 * </code> 
 * </p>
 *
 * @since 3.0  
 */
public class TrimLayoutData {
    /**
     * Width of the control (or SWT.DEFAULT if the control's preferred width should be used)
     */
    int widthHint = SWT.DEFAULT;

    /**
     * Height of the control (or SWT.DEFAULT if the control's preferred height should be used)
     */
    int heightHint = SWT.DEFAULT;

    /**
     * Flag indicating whether the control should resize with the window. Note that 
     * available space is always divided equally among all resizable controls on the 
     * same side of the layout, regardless of their preferred size.
     */
    boolean resizable = true;

    /**
     * Creates a default TrimLayoutData. The default trim layout data is resizable.
     */
    public TrimLayoutData() {
    }

    /**
     * Creates a TrimLayoutData with user-specified parameters.
     * 
     * @param resizable if true, the control will be resized with the layout. If there
     * is more than one resizable control on the same side of the layout, the available
     * space will be divided equally among all the controls.
     * 
     * @param widthHint overrides the preferred width of the control (pixels). If SWT.DEFAULT,
     * then the control's preferred width will be used. This has no effect for 
     * horizontally resizable controls.
     *  
     * @param heightHint overrides the preferred height of the control (pixels). If SWT.DEFAULT,
     * then the control's preferred height will be used. This has no effect for 
     * vertically resizable controls.
     */
    public TrimLayoutData(boolean resizable, int widthHint, int heightHint) {
        this.widthHint = widthHint;
        this.heightHint = heightHint;
        this.resizable = resizable;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7946.java