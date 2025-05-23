error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4072.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4072.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4072.java
text:
```scala
public A@@ctor hit (float x, float y) {


package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;

/** An on-screen joystick. The movement area of the joystick is circular, centered on the touchpad, and its size determined by the
 * smaller touchpad dimension.
 * <p>
 * The preferred size of the touchpad is determined by the background.
 * <p>
 * {@link ChangeEvent} is fired when the touchpad knob is moved. Cancelling the event will move the knob to where it was
 * previously.
 * @author Josh Street */
public class Touchpad extends Widget {
	private TouchpadStyle style;
	boolean touched;
	private float deadzoneRadius;
	private final Circle padBounds = new Circle(0, 0, 0);
	private final Circle deadzoneBounds = new Circle(0, 0, 0);
	private final Vector2 knobPosition = new Vector2();
	private final Vector2 knobPercent = new Vector2();

	/** @param deadzoneRadius The distance in pixels from the center of the touchpad required for the knob to be moved. */
	public Touchpad (float deadzoneRadius, Skin skin) {
		this(deadzoneRadius, skin.get(TouchpadStyle.class));
	}

	/** @param deadzoneRadius The distance in pixels from the center of the touchpad required for the knob to be moved. */
	public Touchpad (float deadzoneRadius, Skin skin, String styleName) {
		this(deadzoneRadius, skin.get(styleName, TouchpadStyle.class));
	}

	/** @param deadzoneRadius The distance in pixels from the center of the touchpad required for the knob to be moved. */
	public Touchpad (float deadzoneRadius, TouchpadStyle style) {
		if (deadzoneRadius < 0) throw new IllegalArgumentException("deadzoneRadius must be > 0");
		this.deadzoneRadius = deadzoneRadius;

		knobPosition.set(getWidth() / 2f, getHeight() / 2f);

		setStyle(style);
		setWidth(getPrefWidth());
		setHeight(getPrefHeight());

		addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (touched) return false;
				touched = true;
				calculatePositionAndValue(x, y, false);
				return true;
			}

			@Override
			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				calculatePositionAndValue(x, y, false);
			}

			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				touched = false;
				calculatePositionAndValue(x, y, true);
			}
		});
	}

	void calculatePositionAndValue (float x, float y, boolean isTouchUp) {
		float oldPositionX = knobPosition.x;
		float oldPositionY = knobPosition.y;
		float oldPercentX = knobPercent.x;
		float oldPercentY = knobPercent.y;
		knobPosition.set(getWidth() / 2f, getHeight() / 2f);
		knobPercent.set(0f, 0f);
		if (!isTouchUp) {
			if (!deadzoneBounds.contains(x, y)) {
				knobPercent.set(x - padBounds.x, y - padBounds.y).nor();
				if (padBounds.contains(x, y)) {
					knobPosition.set(x, y);
				} else {
					knobPosition.set(knobPercent).mul(padBounds.radius).add(padBounds.x, padBounds.y);
				}
			}
		}
		if (oldPercentX != knobPercent.x || oldPercentY != knobPercent.y) {
			ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
			if (fire(changeEvent)) {
				knobPercent.set(oldPercentX, oldPercentY);
				knobPosition.set(oldPositionX, oldPositionY);
			}
			Pools.free(changeEvent);
		}
	}

	public void setStyle (TouchpadStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null");
		this.style = style;
		invalidateHierarchy();
	}

	/** Returns the touchpad's style. Modifying the returned style may not have an effect until {@link #setStyle(TouchpadStyle)} is
	 * called. */
	public TouchpadStyle getStyle () {
		return style;
	}

	@Override
	public Actor hit (float x, float y, boolean touchable) {
		return padBounds.contains(x, y) ? this : null;
	}

	@Override
	public void layout () {
		// Recalc pad and deadzone bounds
		float radius = Math.min(getWidth(), getHeight()) / 2;
		if (style.knob != null) radius -= Math.max(style.knob.getMinWidth(), style.knob.getMinHeight()) / 2;
		padBounds.set(getWidth() / 2f, getHeight() / 2f, radius);
		deadzoneBounds.set(getWidth() / 2f, getHeight() / 2f, deadzoneRadius);
		// Recalc pad values and knob position
		knobPosition.set(getWidth() / 2f, getHeight() / 2f);
		knobPercent.set(0, 0);
	}

	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		validate();

		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);

		float x = getX();
		float y = getY();
		float w = getWidth();
		float h = getHeight();

		final Drawable bg = style.background;
		if (bg != null) bg.draw(batch, x, y, w, h);

		final Drawable knob = style.knob;
		if (knob != null) {
			x += knobPosition.x - knob.getMinWidth() / 2f;
			y += knobPosition.y - knob.getMinHeight() / 2f;
			knob.draw(batch, x, y, knob.getMinWidth(), knob.getMinHeight());
		}
	}

	@Override
	public float getPrefWidth () {
		return style.background != null ? style.background.getMinWidth() : 0;
	}

	@Override
	public float getPrefHeight () {
		return style.background != null ? style.background.getMinHeight() : 0;
	}

	public boolean isTouched () {
		return touched;
	}

	/** @param deadzoneRadius The distance in pixels from the center of the touchpad required for the knob to be moved. */
	public void setDeadzone (float deadzoneRadius) {
		if (deadzoneRadius < 0) throw new IllegalArgumentException("deadzoneRadius must be > 0");
		this.deadzoneRadius = deadzoneRadius;
		invalidate();
	}

	/** Returns the x-position of the knob relative to the center of the widget. The positive direction is right. */
	public float getKnobX () {
		return knobPosition.x;
	}

	/** Returns the y-position of the knob relative to the center of the widget. The positive direction is up. */
	public float getKnobY () {
		return knobPosition.y;
	}

	/** Returns the x-position of the knob as a percentage from the center of the touchpad to the edge of the circular movement
	 * area. The positive direction is right. */
	public float getKnobPercentX () {
		return knobPercent.x;
	}

	/** Returns the y-position of the knob as a percentage from the center of the touchpad to the edge of the circular movement
	 * area. The positive direction is up. */
	public float getKnobPercentY () {
		return knobPercent.y;
	}

	/** The style for a {@link Touchpad}.
	 * @author Josh Street */
	public static class TouchpadStyle {
		/** Stretched in both directions. Optional. */
		public Drawable background;

		/** Optional. */
		public Drawable knob;

		public TouchpadStyle () {
		}

		public TouchpadStyle (Drawable background, Drawable knob) {
			this.background = background;
			this.knob = knob;
		}

		public TouchpadStyle (TouchpadStyle style) {
			this.background = style.background;
			this.knob = style.knob;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4072.java