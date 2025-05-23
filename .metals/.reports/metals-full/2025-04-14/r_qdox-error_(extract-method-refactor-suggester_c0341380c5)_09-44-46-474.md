error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2181.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2181.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2181.java
text:
```scala
.@@getProperty(PropertyKey.getKey(CalendarListController.PROP_FILTERED)));

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.calendar.ui.list;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.miginfocom.ashape.AShapeComponent;
import com.miginfocom.ashape.AShapeUtil;
import com.miginfocom.ashape.interaction.AShapeAnimatorBroker;
import com.miginfocom.ashape.interaction.CompositeBroker;
import com.miginfocom.ashape.interaction.DefaultInteractionBroker;
import com.miginfocom.ashape.interaction.InteractionBroker;
import com.miginfocom.ashape.interaction.InteractionListener;
import com.miginfocom.ashape.interaction.Interactor;
import com.miginfocom.ashape.interaction.MouseKeyInteractor;
import com.miginfocom.ashape.layout.AShapeLayout;
import com.miginfocom.ashape.layout.RowAShapeLayout;
import com.miginfocom.ashape.shapes.AShape;
import com.miginfocom.ashape.shapes.ContainerAShape;
import com.miginfocom.ashape.shapes.DrawAShape;
import com.miginfocom.ashape.shapes.FillAShape;
import com.miginfocom.ashape.shapes.ImageAShape;
import com.miginfocom.ashape.shapes.RootAShape;
import com.miginfocom.ashape.shapes.TextAShape;
import com.miginfocom.calendar.category.Category;
import com.miginfocom.calendar.category.CategoryInteractionBroker;
import com.miginfocom.calendar.category.CategoryStaticInteractor;
import com.miginfocom.util.MigUtil;
import com.miginfocom.util.PropertyKey;
import com.miginfocom.util.gfx.GfxUtil;
import com.miginfocom.util.gfx.RoundRectangle;
import com.miginfocom.util.gfx.ShapeGradientPaint;
import com.miginfocom.util.gfx.geometry.AbsRect;
import com.miginfocom.util.gfx.geometry.AlignRect;
import com.miginfocom.util.gfx.geometry.PlaceRect;
import com.miginfocom.util.gfx.geometry.numbers.AtEnd;
import com.miginfocom.util.gfx.geometry.numbers.AtFraction;
import com.miginfocom.util.gfx.geometry.numbers.AtRefNumber;
import com.miginfocom.util.gfx.geometry.numbers.AtStart;

public class PrettyRenderer extends DefaultTreeCellRenderer {
	protected Interactor[][] interactors = new Interactor[0][]; // Always

	// same
	// length as
	// above

	private static String CHECK_BASE64_IMAGE = "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAGCAYAAAAPDoR2AAAABGdBTUEAAK/INwWK6QAAABl0RVh0\n"
			+ "U29mdHdhcmUAQWRvYmUgSW1hZ2VSZWFkeXHJZTwAAABISURBVHjaYmTADiqAmBObhAcQ/wDiNyBO\n"
			+ "HhDXQiUcgPgjEP8H4nSQwGMoZxkQv4CyM2HG2EON+A/Fuej2OALxVSAuQhYECDAAyuIQJ6AbgRMA\n"
			+ "AAAASUVORK5CYII=";

	private static BufferedImage checkImage = null;

	private static BufferedImage halfCheckImage = null;

	private static Font labelFontCateg = UIManager.getFont("Label.font").deriveFont(Font.BOLD);

	private static Font labelFontNoCateg = UIManager.getFont("Label.font");

	protected final InteractionListener iListener;

	private final RootAShape rootAShape;

	private final AShapeComponent shapeComp;

	private final AShape catSelectedShape;

	private final TextAShape textAShape;

	public PrettyRenderer(InteractionListener iListener) {
		this.iListener = iListener;

		rootAShape = new RootAShape();

		ShapeGradientPaint bgPaint = new ShapeGradientPaint(new Color(255, 255,
				255), new Color(245, 245, 245), 270, 0.7f, 0.3f, false);
		Paint outlinePaint = new Color(200, 200, 200);
		Color textColor = new Color(50, 50, 50);
		Shape shape = new RoundRectangle(0, 0, 12, 12, 6, 6);

		PlaceRect buttonRect = new AlignRect(new AtFraction(0f),
				new AtFraction(0.5f));
		FillAShape buttBgAShape = new FillAShape("treeCheckBox", shape,
				buttonRect, bgPaint, Boolean.TRUE);
		buttBgAShape.setAttribute(AShape.A_REPORT_HIT_AREA, Boolean.TRUE);
		buttBgAShape.setAttribute(AShape.A_MOUSE_CURSOR, Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));

		DrawAShape buttOutlineAShape = new DrawAShape("treeCheckBoxOutline",
				shape, AbsRect.FILL, outlinePaint, new BasicStroke(1),
				Boolean.TRUE);
		buttOutlineAShape.setAttribute(AShape.A_SHAPE, AShape.A_INHERIT);
		buttOutlineAShape.setAttribute(AShape.A_REPORT_HIT_AREA, Boolean.TRUE);

		PlaceRect textRect = new AbsRect(new AtStart(0), new AtStart(0),
				new AtEnd(0), new AtEnd(0));
		textAShape = new TextAShape("treeCatText",
				CategoryStaticInteractor.TEMPL_CATEGORY_NAME, textRect,
				TextAShape.TYPE_SINGE_LINE, labelFontNoCateg, textColor,
				new AtStart(3), new AtFraction(0.5f), Boolean.TRUE);
		textAShape.setAttribute(AShape.A_REPORT_HIT_AREA, Boolean.TRUE);
		textAShape.setAttribute(AShape.A_MOUSE_CURSOR, Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));

		buttBgAShape.addSubShape(buttOutlineAShape);

		AtRefNumber[] sizes = new AtRefNumber[] { null, new AtFraction(1f) };
		AShapeLayout layout = new RowAShapeLayout(SwingConstants.HORIZONTAL,
				sizes);
		ContainerAShape boxLayoutAShape = new ContainerAShape("boxLayout",
				AbsRect.FILL, layout);

		PlaceRect calSelRect = new AlignRect(new AtFraction(0.5f),
				new AtFraction(0.5f));
		catSelectedShape = new ImageAShape("catSelectedShape", (Image) null,
				calSelRect);

		buttBgAShape.addSubShape(catSelectedShape);

		boxLayoutAShape.addSubShape(buttBgAShape);
		boxLayoutAShape.addSubShape(textAShape);

		rootAShape.addSubShape(boxLayoutAShape);

		AShapeUtil.enableMouseOverCursor(rootAShape);
		AShapeUtil.enableMouseOverState(buttOutlineAShape);

		Integer button = new Integer(MouseEvent.BUTTON1);

		AShapeUtil.addMouseFireEvent(textAShape,
				MouseKeyInteractor.MOUSE_CLICK, "categorizeOnPressed", false,
				false, button);
		AShapeUtil.addMouseFireEvent(buttBgAShape,
				MouseKeyInteractor.MOUSE_CLICK, "selectedCheckPressed", false,
				false, button);

		AShapeUtil.addEnterExitOverride(buttOutlineAShape, buttOutlineAShape,
				AShape.A_PAINT, Color.BLACK, false, true);
		AShapeUtil.addEnterExitOverride(textAShape, textAShape,
				AShape.A_UNDERLINE_HEIGHT, new Integer(1), false, true);

		AShapeUtil.addMouseEventBlock(buttOutlineAShape, false, null);

		shapeComp = new AShapeComponent();
		shapeComp.setShape(rootAShape, false);
		shapeComp.setPreferredSize(new Dimension(110, 16));

		if (checkImage == null) {
			checkImage = GfxUtil.getImageFromString(CHECK_BASE64_IMAGE, null);
			halfCheckImage = GfxUtil.getCrossImage(null, checkImage, null,
					Transparency.TRANSLUCENT, 0.3f, false);
		}
	}

	public Component getTreeCellRendererComponent(final JTree tree,
			Object value, boolean selected, boolean expanded, boolean leaf,
			final int row, boolean hasFocus) {
		if (row < 0 || row >= tree.getRowCount()
 value instanceof Category == false)
			return super.getTreeCellRendererComponent(tree, value, selected,
					expanded, leaf, row, hasFocus);

		Category category = (Category) value;

		if (interactors.length != tree.getRowCount())
			interactors = new Interactor[tree.getRowCount()][]; // Only
		// happens
		// when the
		// tree has
		// changed
		// it's
		// size.

		if (interactors[row] == null) {
			InteractionBroker compositeBroker = new CompositeBroker(
					new InteractionBroker[] {
							new CategoryInteractionBroker(category),
							new DefaultInteractionBroker(tree, iListener),
							AShapeAnimatorBroker.getInstance() });

			interactors[row] = new Interactor[] {
					new MouseKeyInteractor(value, compositeBroker, rootAShape
							.getInteractions()),
					new CategoryStaticInteractor(category) };

			for (int i = 0; i < interactors[row].length; i++) {
				interactors[row][i]
						.addOverrideListener(new PropertyChangeListener() {
							public void propertyChange(PropertyChangeEvent evt) {
								tree.repaint(tree.getRowBounds(row));
							}
						});
			}
		}

		rootAShape.setInteractors(interactors[row]);

		shapeComp.setRelativeToComponent(tree);

		BufferedImage check = null;
		Object o = category.getPropertyDeep(Category.PROP_IS_HIDDEN, this);
		if (o == this) {
			check = halfCheckImage;
		} else if (MigUtil.isTrue(o) == false) {
			check = checkImage;
		}
		catSelectedShape.setAttribute(AShape.A_IMAGE, check);

		boolean isCatCateg = MigUtil.isTrue(category
				.getProperty(PropertyKey.getKey(CalendarTreeController.PROP_FILTERED)));
		textAShape.setAttribute(AShape.A_FONT, isCatCateg ? labelFontCateg
				: labelFontNoCateg);

		return shapeComp;
	}

	public Interactor[][] getInteractors() {
		return interactors;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2181.java