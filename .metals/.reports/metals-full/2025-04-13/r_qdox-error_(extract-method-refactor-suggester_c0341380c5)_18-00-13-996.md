error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5477.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5477.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5477.java
text:
```scala
L@@ist<StaticTextBox> inlines = LayoutUtils.createGeneratedInlines(context,

/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.vex.core.internal.layout;

import java.util.List;

import org.eclipse.wst.xml.vex.core.internal.core.IntRange;
import org.eclipse.wst.xml.vex.core.internal.css.Styles;
import org.eclipse.wst.xml.vex.core.internal.provisional.dom.I.VEXElement;

/**
 * Implements a Block
 */
public class BlockPseudoElementBox extends AbstractBox implements BlockBox {

	private VEXElement pseudoElement;
	private BlockBox parent;
	private ParagraphBox para;

	private int marginTop;
	private int marginBottom;

	public BlockPseudoElementBox(LayoutContext context, VEXElement pseudoElement,
			BlockBox parent, int width) {

		this.pseudoElement = pseudoElement;
		this.parent = parent;

		Styles styles = context.getStyleSheet().getStyles(pseudoElement);

		this.marginTop = styles.getMarginTop().get(width);
		this.marginBottom = styles.getMarginBottom().get(width);

		int leftInset = styles.getMarginLeft().get(width)
				+ styles.getBorderLeftWidth()
				+ styles.getPaddingLeft().get(width);
		int rightInset = styles.getMarginRight().get(width)
				+ styles.getBorderRightWidth()
				+ styles.getPaddingRight().get(width);

		int childWidth = width - leftInset - rightInset;
		List inlines = LayoutUtils.createGeneratedInlines(context,
				pseudoElement);
		this.para = ParagraphBox.create(context, pseudoElement, inlines,
				childWidth);

		this.para.setX(0);
		this.para.setY(0);
		this.setWidth(width - leftInset - rightInset);
		this.setHeight(this.para.getHeight());
	}

	/**
	 * Provide children for {@link AbstractBox#paint}.
	 * 
	 * @see org.eclipse.wst.xml.vex.core.internal.layout.Box#getChildren()
	 */
	public Box[] getChildren() {
		return new Box[] { this.para };
	}

	/**
	 * @see org.eclipse.wst.xml.vex.core.internal.layout.Box#getElement()
	 */
	public VEXElement getElement() {
		return this.pseudoElement;
	}

	/**
	 * @see org.eclipse.wst.xml.vex.core.internal.layout.BlockBox#getFirstLine()
	 */
	public LineBox getFirstLine() {
		throw new IllegalStateException();
	}

	/**
	 * @see org.eclipse.wst.xml.vex.core.internal.layout.BlockBox#getLastLine()
	 */
	public LineBox getLastLine() {
		throw new IllegalStateException();
	}

	/**
	 * @see org.eclipse.wst.xml.vex.core.internal.layout.BlockBox#getLineEndOffset(int)
	 */
	public int getLineEndOffset(int offset) {
		throw new IllegalStateException();
	}

	/**
	 * @see org.eclipse.wst.xml.vex.core.internal.layout.BlockBox#getLineStartOffset(int)
	 */
	public int getLineStartOffset(int offset) {
		throw new IllegalStateException();
	}

	public int getMarginBottom() {
		return this.marginBottom;
	}

	public int getMarginTop() {
		return this.marginTop;
	}

	/**
	 * @see org.eclipse.wst.xml.vex.core.internal.layout.BlockBox#getNextLineOffset(org.eclipse.wst.xml.vex.core.internal.layout.LayoutContext,
	 *      int, int)
	 */
	public int getNextLineOffset(LayoutContext context, int offset, int x) {
		throw new IllegalStateException();
	}

	/**
	 * Returns this box's parent.
	 */
	public BlockBox getParent() {
		return this.parent;
	}

	/**
	 * @see org.eclipse.wst.xml.vex.core.internal.layout.BlockBox#getPreviousLineOffset(org.eclipse.wst.xml.vex.core.internal.layout.LayoutContext,
	 *      int, int)
	 */
	public int getPreviousLineOffset(LayoutContext context, int offset, int x) {
		throw new IllegalStateException();
	}

	public IntRange layout(LayoutContext context, int top, int bottom) {
		return null;
	}

	public void invalidate(boolean direct) {
		throw new IllegalStateException(
				"invalidate called on a non-element BlockBox");
	}

	/**
	 * Draw boxes before painting our child.
	 * 
	 * @see org.eclipse.wst.xml.vex.core.internal.layout.Box#paint(org.eclipse.wst.xml.vex.core.internal.layout.LayoutContext,
	 *      int, int)
	 */
	public void paint(LayoutContext context, int x, int y) {
		this.drawBox(context, x, y, this.getParent().getWidth(), true);
		super.paint(context, x, y);
	}

	public void setInitialSize(LayoutContext context) {
		// NOP - size calculated in the ctor
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5477.java