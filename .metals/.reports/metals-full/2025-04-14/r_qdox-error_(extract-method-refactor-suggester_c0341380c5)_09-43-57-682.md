error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16605.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16605.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16605.java
text:
```scala
public L@@ist createChildren(LayoutContext context) {

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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.xml.vex.core.internal.VEXCorePlugin;
import org.eclipse.wst.xml.vex.core.internal.core.Drawable;
import org.eclipse.wst.xml.vex.core.internal.core.Graphics;
import org.eclipse.wst.xml.vex.core.internal.core.Rectangle;
import org.eclipse.wst.xml.vex.core.internal.css.CSS;
import org.eclipse.wst.xml.vex.core.internal.css.StyleSheet;
import org.eclipse.wst.xml.vex.core.internal.css.Styles;
import org.eclipse.wst.xml.vex.core.internal.dom.Element;

/**
 * A block box corresponding to a DOM Element. Block boxes lay their children
 * out stacked top to bottom. Block boxes correspond to the
 * <code>display: block;</code> CSS property.
 */
public class BlockElementBox extends AbstractBlockBox {

	/** hspace btn. list-item bullet and block, as fraction of font-size */
	static final float BULLET_SPACE = 0.5f;

	/** vspace btn. list-item bullet and baseine, as fraction of font-size */
	// private static final float BULLET_LIFT = 0.1f;
	/** number of boxes created since VM startup, for profiling */
	private static int boxCount;

	BlockBox beforeMarker;

	/**
	 * Class constructor. This box's children are not created here but in the
	 * first call to layout. Instead, we estimate the box's height here based on
	 * the given width.
	 * 
	 * @param context
	 *            LayoutContext used for this layout.
	 * @param parent
	 *            This box's parent box.
	 * @param element
	 *            Element to which this box corresponds.
	 */
	public BlockElementBox(LayoutContext context, BlockBox parent,
			Element element) {
		super(context, parent, element);
	}

	/**
	 * Returns the number of boxes created since VM startup. Used for profiling.
	 */
	public static int getBoxCount() {
		return boxCount;
	}

	public int getEndOffset() {
		return this.getElement().getEndOffset();
	}

	public int getStartOffset() {
		return this.getElement().getStartOffset() + 1;
	}

	public boolean hasContent() {
		return true;
	}

	public void paint(LayoutContext context, int x, int y) {

		super.paint(context, x, y);

		if (this.beforeMarker != null) {
			this.beforeMarker.paint(context, x + this.beforeMarker.getX(), y
					+ this.beforeMarker.getY());
		}
	}

	protected int positionChildren(LayoutContext context) {

		int repaintStart = super.positionChildren(context);

		Styles styles = context.getStyleSheet().getStyles(this.getElement());
		if (this.beforeMarker != null) {
			int x = -this.beforeMarker.getWidth()
					- Math.round(BULLET_SPACE * styles.getFontSize());
			int y = this.getFirstLineTop(context);
			LineBox firstLine = this.getFirstLine();
			if (firstLine != null) {
				y += firstLine.getBaseline()
						- this.beforeMarker.getFirstLine().getBaseline();
			}

			this.beforeMarker.setX(x);
			this.beforeMarker.setY(y);
		}

		return repaintStart;
	}

	public String toString() {
		return "BlockElementBox: <" + this.getElement().getName() + ">" + "[x="
				+ this.getX() + ",y=" + this.getY() + ",width="
				+ this.getWidth() + ",height=" + this.getHeight() + "]";
	}

	// ===================================================== PRIVATE

	/**
	 * Lays out the children as vertically stacked blocks. Runs of text and
	 * inline elements are wrapped in DummyBlockBox's.
	 */
	protected List createChildren(LayoutContext context) {

		long start = 0;
		if (VEXCorePlugin.getInstance().isDebugging()) {
			start = System.currentTimeMillis();
		}

		Element element = this.getElement();
		int width = this.getWidth();

		List childList = new ArrayList();

		StyleSheet ss = context.getStyleSheet();

		// element and styles for generated boxes
		Element genElement;
		Styles genStyles;

		// :before content
		List beforeInlines = null;
		genElement = context.getStyleSheet()
				.getBeforeElement(this.getElement());
		if (genElement != null) {
			genStyles = ss.getStyles(genElement);
			if (genStyles.getDisplay().equals(CSS.INLINE)) {
				beforeInlines = new ArrayList();
				beforeInlines.addAll(LayoutUtils.createGeneratedInlines(
						context, genElement));
			} else {
				childList.add(new BlockPseudoElementBox(context, genElement,
						this, width));
			}
		}

		// :after content
		Box afterBlock = null;
		List afterInlines = null;
		genElement = context.getStyleSheet().getAfterElement(this.getElement());
		if (genElement != null) {
			genStyles = context.getStyleSheet().getStyles(genElement);
			if (genStyles.getDisplay().equals(CSS.INLINE)) {
				afterInlines = new ArrayList();
				afterInlines.addAll(LayoutUtils.createGeneratedInlines(context,
						genElement));
			} else {
				afterBlock = new BlockPseudoElementBox(context, genElement,
						this, width);
			}
		}

		int startOffset = element.getStartOffset() + 1;
		int endOffset = element.getEndOffset();
		childList.addAll(createBlockBoxes(context, startOffset, endOffset,
				width, beforeInlines, afterInlines));

		if (afterBlock != null) {
			childList.add(afterBlock);
		}

		Styles styles = context.getStyleSheet().getStyles(this.getElement());
		if (styles.getDisplay().equals(CSS.LIST_ITEM)
				&& !styles.getListStyleType().equals(CSS.NONE)) {
			this.createListMarker(context);
		}

		if (VEXCorePlugin.getInstance().isDebugging()) {
			long end = System.currentTimeMillis();
			if (end - start > 10) {
				System.out.println("BEB.layout for "
						+ this.getElement().getName() + " took "
						+ (end - start) + "ms");
			}
		}

		return childList;
	}

	/**
	 * Creates a marker box for this primary box and puts it in the beforeMarker
	 * field.
	 */
	private void createListMarker(LayoutContext context) {

		Styles styles = context.getStyleSheet().getStyles(this.getElement());

		InlineBox markerInline;
		String type = styles.getListStyleType();
		if (type.equals(CSS.NONE)) {
			return;
		} else if (type.equals(CSS.CIRCLE)) {
			markerInline = createCircleBullet(this.getElement(), styles);
		} else if (type.equals(CSS.SQUARE)) {
			markerInline = createSquareBullet(this.getElement(), styles);
		} else if (isEnumeratedListStyleType(type)) {
			String item = this.getItemNumberString(type);
			markerInline = new StaticTextBox(context, this.getElement(), item
					+ ".");
		} else {
			markerInline = createDiscBullet(this.getElement(), styles);
		}

		this.beforeMarker = ParagraphBox.create(context, this.getElement(),
				new InlineBox[] { markerInline }, Integer.MAX_VALUE);

	}

	/**
	 * Returns a Drawable that draws a circle-style list item bullet.
	 */
	private static InlineBox createCircleBullet(Element element, Styles styles) {
		final int size = Math.round(0.5f * styles.getFontSize());
		final int lift = Math.round(0.1f * styles.getFontSize());
		Drawable drawable = new Drawable() {
			public void draw(Graphics g, int x, int y) {
				g.setLineStyle(Graphics.LINE_SOLID);
				g.setLineWidth(1);
				g.drawOval(x, y - size - lift, size, size);
			}

			public Rectangle getBounds() {
				return new Rectangle(0, -size - lift, size, size);
			}
		};
		return new DrawableBox(drawable, element);
	}

	/**
	 * Returns a Drawable that draws a disc-style list item bullet.
	 */
	private static InlineBox createDiscBullet(Element element, Styles styles) {
		final int size = Math.round(0.5f * styles.getFontSize());
		final int lift = Math.round(0.1f * styles.getFontSize());
		Drawable drawable = new Drawable() {
			public void draw(Graphics g, int x, int y) {
				g.fillOval(x, y - size - lift, size, size);
			}

			public Rectangle getBounds() {
				return new Rectangle(0, -size - lift, size, size);
			}
		};
		return new DrawableBox(drawable, element);
	}

	/**
	 * Returns a Drawable that draws a square-style list item bullet.
	 */
	private static InlineBox createSquareBullet(Element element, Styles styles) {
		final int size = Math.round(0.5f * styles.getFontSize());
		final int lift = Math.round(0.1f * styles.getFontSize());
		Drawable drawable = new Drawable() {
			public void draw(Graphics g, int x, int y) {
				g.setLineStyle(Graphics.LINE_SOLID);
				g.setLineWidth(1);
				g.drawRect(x, y - size - lift, size, size);
			}

			public Rectangle getBounds() {
				return new Rectangle(0, -size - lift, size, size);
			}
		};
		return new DrawableBox(drawable, element);
	}

	/**
	 * Returns the vertical distance from the top of this box to the top of its
	 * first line.
	 */
	int getFirstLineTop(LayoutContext context) {
		Styles styles = context.getStyleSheet().getStyles(this.getElement());
		int top = styles.getBorderTopWidth() + styles.getPaddingTop().get(0);
		Box[] children = this.getChildren();
		if (children != null && children.length > 0
				&& children[0] instanceof BlockElementBox) {
			return top
					+ ((BlockElementBox) children[0]).getFirstLineTop(context);
		} else {
			return top;
		}
	}

	/**
	 * Returns the item number of this box. The item number indicates the
	 * ordinal number of the corresponding element amongst its siblings starting
	 * with 1.
	 */
	private int getItemNumber() {
		Element element = this.getElement();
		Element parent = element.getParent();

		if (parent == null) {
			return 1;
		}

		int item = 1;
		Element[] children = parent.getChildElements();
		for (int i = 0; i < children.length; i++) {
			if (children[i] == element) {
				return item;
			}
			if (children[i].getName().equals(element.getName())) {
				item++;
			}
		}

		throw new IllegalStateException();
	}

	private String getItemNumberString(String style) {
		int item = getItemNumber();
		if (style.equals(CSS.DECIMAL_LEADING_ZERO)) {
			if (item < 10) {
				return "0" + Integer.toString(item);
			} else {
				return Integer.toString(item);
			}
		} else if (style.equals(CSS.LOWER_ALPHA)
 style.equals(CSS.LOWER_LATIN)) {
			return this.getAlpha(item);
		} else if (style.equals(CSS.LOWER_ROMAN)) {
			return this.getRoman(item);
		} else if (style.equals(CSS.UPPER_ALPHA)
 style.equals(CSS.UPPER_LATIN)) {
			return this.getAlpha(item).toUpperCase();
		} else if (style.equals(CSS.UPPER_ROMAN)) {
			return this.getRoman(item).toUpperCase();
		} else {
			return Integer.toString(item);
		}
	}

	private String getAlpha(int n) {
		final String alpha = "abcdefghijklmnopqrstuvwxyz";
		return String.valueOf(alpha.charAt((n - 1) % 26));
	}

	private String getRoman(int n) {
		final String[] ones = { "", "i", "ii", "iii", "iv", "v", "vi", "vii",
				"viii", "ix" };
		final String[] tens = { "", "x", "xx", "xxx", "xl", "l", "lx", "lxx",
				"lxxx", "xc" };
		final String[] hundreds = { "", "c", "cc", "ccc", "cd", "d", "dc",
				"dcc", "dccc", "cm" };
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n / 1000; i++) {
			sb.append("m");
		}
		sb.append(hundreds[(n / 100) % 10]);
		sb.append(tens[(n / 10) % 10]);
		sb.append(ones[n % 10]);
		return sb.toString();
	}

	private static boolean isEnumeratedListStyleType(String s) {
		return s.equals(CSS.ARMENIAN) || s.equals(CSS.CJK_IDEOGRAPHIC)
 s.equals(CSS.DECIMAL) || s.equals(CSS.DECIMAL_LEADING_ZERO)
 s.equals(CSS.GEORGIAN) || s.equals(CSS.HEBREW)
 s.equals(CSS.HIRAGANA) || s.equals(CSS.HIRAGANA_IROHA)
 s.equals(CSS.KATAKANA) || s.equals(CSS.KATAKANA_IROHA)
 s.equals(CSS.LOWER_ALPHA) || s.equals(CSS.LOWER_GREEK)
 s.equals(CSS.LOWER_LATIN) || s.equals(CSS.LOWER_ROMAN)
 s.equals(CSS.UPPER_ALPHA) || s.equals(CSS.UPPER_LATIN)
 s.equals(CSS.UPPER_ROMAN);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16605.java