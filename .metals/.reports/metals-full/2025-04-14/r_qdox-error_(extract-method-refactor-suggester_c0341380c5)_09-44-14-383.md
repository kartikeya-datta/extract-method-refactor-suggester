error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3638.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3638.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3638.java
text:
```scala
t@@his.fontColorUnselected.set(fontColorUnselected);

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** A list of string items.
 * 
 * <h2>Functionality</h2> A list displays textual items and highlights the current selection. A {@link SelectionListener} can be
 * registered with the list to listen to selection changes. Items have an index in the list, the top item having the index 0.
 * 
 * <h2>Layout</h2> The (preferred) width and height of a List are derrived from the bounding box around all list items. Use
 * {@link #setPrefSize(int, int)} to programmatically change the size to your liking. In case the width and height you set are to
 * small for the contained text you will see artifacts. The patch highlighting the current selection will have the width of the
 * List, either determined as explained above or set programmatically.
 * 
 * <h2>Style</h2> A List is a {@link Widget} a text rendered for each list item via a {@link BitmapFont} and {@link Color} as well
 * as a {@link NinePatch} highlighting the current selection and a second Color used for the text of the currently selected item.
 * The highlighting NinePatch is rendered beneath the selected item. The style is defined via an instance of {@link ListStyle},
 * which can be done either programmatically or via a {@link Skin}.</p>
 * 
 * A List's style definition in an XML skin file should look like this:
 * 
 * <pre>
 * {@code 
 * <list name="styleName"
 *       font="fontName"
 *       fontColorUnselected="colorName"
 *       fontColorSelected="colorName" 
 *       selected="selectedPatch"/>
 * }
 * </pre>
 * 
 * <ul>
 * <li>The <code>name</code> attribute defines the name of the style which you can later use with
 * {@link Skin#newList(String, String[], String)}.</li>
 * <li>The <code>fontName</code> attribute references a {@link BitmapFont} by name, to be used for render the items</li>
 * <li>The <code>fontColorUnselected</code> attribute references a {@link Color} by name, to be used for render unselected items</li>
 * <li>The <code>fontColorSelected</code> attribute references a {@link Color} by name, to be used to render the selected item</li>
 * <li>The <code>selected</code> attribute references a {@link NinePatch} by name, to be used to render the highlight behind the
 * selected item</li>
 * </ul>
 * 
 * @author mzechner */
public class List extends Widget {
	protected ListStyle style;
	protected String[] items;
	protected float itemHeight = 0;
	protected float textOffsetX = 0;
	protected float textOffsetY = 0;
	protected int selected = 0;
	protected SelectionListener listener;
	protected float prefWidth, prefHeight;

	public List (String[] items, Skin skin) {
		this(items, skin.getStyle(ListStyle.class), null);
	}

	public List (String[] items, ListStyle style) {
		this(items, style, null);
	}

	/** Creates a new List. The width and height is determined from the bounding box around all items.
	 * @param items the items
	 * @param style the {@link ListStyle}
	 * @param name the name */
	public List (String[] items, ListStyle style, String name) {
		super(name);
		setStyle(style);
		setItems(items);
		layout();
	}

	/** Sets the style of this widget.
	 * @param style */
	public void setStyle (ListStyle style) {
		this.style = style;
		if (items != null) setItems(items);
	}

	@Override
	public void layout () {
	}

	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		final BitmapFont font = style.font;
		final NinePatch selectedPatch = style.selectedPatch;
		final Color fontColorSelected = style.fontColorSelected;
		final Color fontColorUnselected = style.fontColorUnselected;

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float posY = height;
		for (int i = 0; i < items.length; i++) {
			if (selected == i) {
				selectedPatch.draw(batch, x, y + posY - itemHeight, Math.max(prefWidth, width), itemHeight);
				font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
			} else {
				font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a
					* parentAlpha);
			}
			font.draw(batch, items[i], x + textOffsetX, y + posY - textOffsetY);
			posY -= itemHeight;
		}
	}

	@Override
	public boolean touchDown (float x, float y, int pointer) {
		if (pointer != 0) return false;
		selected = (int)((height - y) / itemHeight);
		selected = Math.max(0, selected);
		selected = Math.min(items.length - 1, selected);
		if (listener != null) listener.selected(this, selected, items[selected]);
		return true;
	}

	@Override
	public void touchUp (float x, float y, int pointer) {
	}

	@Override
	public void touchDragged (float x, float y, int pointer) {
	}

	@Override
	public Actor hit (float x, float y) {
		return x >= 0 && x < width && y >= 0 && y < height ? this : null;
	}

	/** Defines a list style, see {@link List}
	 * @author mzechner */
	public static class ListStyle {
		public BitmapFont font;
		public Color fontColorSelected = new Color(1, 1, 1, 1);
		public Color fontColorUnselected = new Color(1, 1, 1, 1);
		public NinePatch selectedPatch;

		private ListStyle () {
		}

		public ListStyle (BitmapFont font, Color fontColorSelected, Color fontColorUnselected, NinePatch selectedPatch) {
			this.font = font;
			this.fontColorSelected.set(fontColorSelected);
			this.fontColorUnselected.set(this.fontColorUnselected);
			this.selectedPatch = selectedPatch;
		}
	}

	/** Interface for listening to selection changes.
	 * @author mzechner */
	public interface SelectionListener {
		public void selected (List list, int selectedIndex, String selection);
	}

	/** @return the index of the currently selected item. The top item has an index of 0. */
	public int getSelectedIndex () {
		return selected;
	}

	public void setSelectedIndex (int index) {
		selected = index;
	}

	/** @return the text of the currently selected item or null if the list is empty */
	public String getSelection () {
		if (items.length == 0) return null;
		return items[selected];
	}

	/** @param index sets the selected item */
	public void setSelection (int index) {
		if (index < 0 || index >= items.length) throw new GdxRuntimeException("Index must be > 0 and < #items");
		selected = index;
	}

	public int setSelection (String item) {
		selected = -1;
		for (int i = 0, n = items.length; i < n; i++) {
			if (items[i].equals(item)) {
				selected = i;
				break;
			}
		}
		return selected;
	}

	/** Sets the items of this list.
	 * @param items the items. */
	public void setItems (String[] items) {
		if (items == null) throw new IllegalArgumentException("items cannot be null.");
		this.items = items;
		selected = 0;

		final BitmapFont font = style.font;
		final NinePatch selectedPatch = style.selectedPatch;
		prefWidth = 0;
		prefHeight = 0;

		for (int i = 0; i < items.length; i++) {
			String item = items[i];
			TextBounds bounds = font.getBounds(item);
			prefWidth = Math.max(bounds.width, prefWidth);
		}

		itemHeight = font.getCapHeight() - font.getDescent() * 2;
		itemHeight += selectedPatch.getTopHeight() + selectedPatch.getBottomHeight();
		prefWidth += selectedPatch.getLeftWidth() + selectedPatch.getRightWidth();
		prefHeight = items.length * itemHeight;
		textOffsetX = selectedPatch.getLeftWidth();
		textOffsetY = selectedPatch.getTopHeight() - font.getDescent();
	}

	public String[] getItems () {
		return items;
	}

	public float getPrefWidth () {
		return prefWidth;
	}

	public float getPrefHeight () {
		return prefHeight;
	}

	/** Sets the {@link SelectionListener} of this list.
	 * @param listener the listener or null */
	public void setSelectionListener (SelectionListener listener) {
		this.listener = listener;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3638.java