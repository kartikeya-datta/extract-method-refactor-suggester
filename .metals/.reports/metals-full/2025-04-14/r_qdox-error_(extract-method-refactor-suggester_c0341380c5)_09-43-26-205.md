error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9020.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9020.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9020.java
text:
```scala
s@@etBackground((isPressed && style.down != null) ? style.down : style.up);


package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;

/** A button is a {@link Table} with a checked state and additional {@link ButtonStyle style} fields for pressed, unpressed, and
 * checked. Being a table, a button can contain any other actors.
 * <p>
 * The preferred size of the button is determined by the background ninepatch and the button contents.
 * @author Nathan Sweet */
public class Button extends Table {
	private ButtonStyle style;
	ClickListener listener;
	boolean isChecked;
	ButtonGroup buttonGroup;

	public Button (Skin skin) {
		this(skin.getStyle(ButtonStyle.class), null);
		pack();
	}

	public Button (ButtonStyle style) {
		this(style, null);
		pack();
	}

	public Button (TextureRegion region) {
		this(new ButtonStyle(new NinePatch(region), null, null, 0f, 0f, 0f, 0f));
	}

	public Button (TextureRegion regionUp, TextureRegion regionDown) {
		this(new ButtonStyle(new NinePatch(regionUp), new NinePatch(regionDown), null, 0f, 0f, 0f, 0f));
	}

	public Button (TextureRegion regionUp, TextureRegion regionDown, TextureRegion regionChecked) {
		this(new ButtonStyle(new NinePatch(regionUp), new NinePatch(regionDown), new NinePatch(regionChecked), 0f, 0f, 0f, 0f));
	}

	public Button (NinePatch patch) {
		this(new ButtonStyle(patch, null, null, 0f, 0f, 0f, 0f));
	}

	public Button (NinePatch patchUp, NinePatch patchDown) {
		this(new ButtonStyle(patchUp, patchDown, null, 0f, 0f, 0f, 0f));
	}

	public Button (NinePatch patchUp, NinePatch patchDown, NinePatch patchChecked) {
		this(new ButtonStyle(patchUp, patchDown, patchChecked, 0f, 0f, 0f, 0f));
	}

	public Button (Actor child, Skin skin) {
		this(child, skin.getStyle(ButtonStyle.class));
		pack();
	}

	public Button (Actor child, ButtonStyle style) {
		this(style, null);
		add(child);
		pack();
	}

	public Button (ButtonStyle style, String name) {
		super(null, null, name);
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		setStyle(style);

		super.setClickListener(new ClickListener() {
			public void click (Actor actor, float x, float y) {
				boolean newChecked = !isChecked;
				setChecked(newChecked);
				// Don't fire listener if isChecked wasn't changed.
				if (newChecked == isChecked && listener != null) listener.click(actor, x, y);
			}
		});
	}

	public void setChecked (boolean isChecked) {
		if (buttonGroup != null && !buttonGroup.canCheck(this, isChecked)) return;
		this.isChecked = isChecked;
	}

	public boolean isChecked () {
		return isChecked;
	}

	public void setStyle (ButtonStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this.style = style;
		setBackground(isPressed ? style.down : style.up);
		invalidateHierarchy();
	}

	/** Returns the button's style. Modifying the returned style may not have an effect until {@link #setStyle(ButtonStyle)} is
	 * called. */
	public ButtonStyle getStyle () {
		return style;
	}

	/** @param listener May be null. */
	public void setClickListener (ClickListener listener) {
		this.listener = listener;
	}

	public void draw (SpriteBatch batch, float parentAlpha) {
		float offsetX = 0, offsetY = 0;
		if (isPressed) {
			setBackground(style.down == null ? style.up : style.down);
			offsetX = style.pressedOffsetX;
			offsetY = style.pressedOffsetY;
		} else {
			if (style.checked == null)
				setBackground(style.up);
			else
				setBackground(isChecked ? style.checked : style.up);
			offsetX = style.unpressedOffsetX;
			offsetY = style.unpressedOffsetY;
		}
		validate();
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			child.x += offsetX;
			child.y += offsetY;
		}
		super.draw(batch, parentAlpha);
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			child.x -= offsetX;
			child.y -= offsetY;
		}
	}

	public float getPrefWidth () {
		float width = getTableLayout().getPrefWidth();
		if (style.up != null) width = Math.max(width, style.up.getTotalWidth());
		if (style.down != null) width = Math.max(width, style.down.getTotalWidth());
		if (style.checked != null) width = Math.max(width, style.checked.getTotalWidth());
		return width;
	}

	public float getPrefHeight () {
		float height = getTableLayout().getPrefHeight();
		if (style.up != null) height = Math.max(height, style.up.getTotalHeight());
		if (style.down != null) height = Math.max(height, style.down.getTotalHeight());
		if (style.checked != null) height = Math.max(height, style.checked.getTotalHeight());
		return height;
	}

	public float getMinWidth () {
		return getPrefWidth();
	}

	public float getMinHeight () {
		return getPrefHeight();
	}

	/** The style for a button, see {@link Button}.
	 * @author mzechner */
	static public class ButtonStyle {
		/** Optional. */
		public NinePatch down, up, checked;
		/** Optional. */
		public float pressedOffsetX, pressedOffsetY;
		/** Optional. */
		public float unpressedOffsetX, unpressedOffsetY;

		public ButtonStyle () {
		}

		public ButtonStyle (NinePatch up, NinePatch down, NinePatch checked, float pressedOffsetX, float pressedOffsetY,
			float unpressedOffsetX, float unpressedOffsetY) {
			this.down = down;
			this.up = up;
			this.checked = checked;
			this.pressedOffsetX = pressedOffsetX;
			this.pressedOffsetY = pressedOffsetY;
			this.unpressedOffsetX = unpressedOffsetX;
			this.unpressedOffsetY = unpressedOffsetY;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9020.java