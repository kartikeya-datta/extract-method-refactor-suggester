error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9233.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9233.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9233.java
text:
```scala
t@@his(min, max, steps, skin.get(SliderStyle.class));

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
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ActorEvent;
import com.badlogic.gdx.scenes.scene2d.ActorListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

// BOZO - Add snapping to the knob.

/** A slider is a horizontal indicator that allows a user to set a value. The slider his a range (min, max) and a stepping between
 * each value the slider represents.
 * <p>
 * The preferred height of a slider is determined by the larger of the knob and background. The preferred width of a slider is
 * 140, a relatively arbitrary size.
 * @author mzechner
 * @author Nathan Sweet */
public class Slider extends Widget {
	private SliderStyle style;
	private float min, max, steps;
	private float value;
	private float sliderPos;
	boolean isDragging;

	public Slider (Skin skin) {
		this(0, 100, 100, skin);
	}

	public Slider (float min, float max, float steps, Skin skin) {
		this(min, max, steps, skin.getStyle(SliderStyle.class));
	}

	/** Creates a new slider. It's width is determined by the given prefWidth parameter, its height is determined by the maximum of
	 * the height of either the slider {@link NinePatch} or slider handle {@link TextureRegion}. The min and max values determine
	 * the range the values of this slider can take on, the steps parameter specifies the distance between individual values. E.g.
	 * min could be 4, max could be 10 and steps could be 0.2, giving you a total of 30 values, 4.0 4.2, 4.4 and so on.
	 * @param min the minimum value
	 * @param max the maximum value
	 * @param steps the step size between values
	 * @param style the {@link SliderStyle} */
	public Slider (float min, float max, float steps, SliderStyle style) {
		if (min > max) throw new IllegalArgumentException("min must be > max: " + min + " > " + max);
		if (steps < 0) throw new IllegalArgumentException("steps must be > 0: " + steps);
		setStyle(style);
		this.min = min;
		this.max = max;
		this.steps = steps;
		this.value = min;
		setWidth(getPrefWidth());
		setHeight(getPrefHeight());

		addListener(new ActorListener() {
			public boolean touchDown (ActorEvent event, float x, float y, int pointer, int button) {
				if (pointer != 0) return false;
				isDragging = true;
				calculatePositionAndValue(x);
				return true;
			}

			public void touchUp (ActorEvent event, float x, float y, int pointer, int button) {
				isDragging = false;
				calculatePositionAndValue(x);
			}

			public void touchDragged (ActorEvent event, float x, float y, int pointer) {
				calculatePositionAndValue(x);
			}
		});
	}

	public void setStyle (SliderStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this.style = style;
		invalidateHierarchy();
	}

	/** Returns the slider's style. Modifying the returned style may not have an effect until {@link #setStyle(SliderStyle)} is
	 * called. */
	public SliderStyle getStyle () {
		return style;
	}

	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		final Drawable knob = style.knob;
		final Drawable slider = style.slider;

		Color color = getColor();
		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		sliderPos = (value - min) / (max - min) * (width - knob.getMinWidth());
		sliderPos = Math.max(0, sliderPos);
		sliderPos = Math.min(width - knob.getMinWidth(), sliderPos);

		slider.draw(batch, x, y + (int)((height - slider.getMinHeight()) * 0.5f), width, slider.getMinHeight());
		knob.draw(batch, x + sliderPos, y + (int)((height - knob.getMinHeight()) * 0.5f), knob.getMinWidth(), knob.getMinHeight());
	}

	void calculatePositionAndValue (float x) {
		final Drawable knob = style.knob;

		float width = getWidth();

		float oldPosition = sliderPos;
		float oldValue = value;
		sliderPos = x - knob.getMinWidth() / 2;
		sliderPos = Math.max(0, sliderPos);
		sliderPos = Math.min(width - knob.getMinWidth(), sliderPos);
		value = min + (max - min) * (sliderPos / (width - knob.getMinWidth()));
		if (oldValue != value && fire(new ChangeEvent())) {
			sliderPos = oldPosition;
			value = oldValue;
		}
	}

	/** Returns true if the slider is being dragged. */
	public boolean isDragging () {
		return isDragging;
	}

	public float getValue () {
		return (float)Math.floor(value / steps) * steps;
	}

	public void setValue (float value) {
		if (value < min || value > max) throw new IllegalArgumentException("value must be >= min and <= max: " + value);
		this.value = value;
	}

	/** Sets the range of this slider. The slider's current value is reset to min. */
	public void setRange (float min, float max) {
		if (min >= max) throw new IllegalArgumentException("min must be < max");
		this.min = min;
		this.max = max;
		float oldValue = value;
		this.value = min;
		if (oldValue != value) fire(new ChangeEvent());
	}

	public float getPrefWidth () {
		return 140;
	}

	public float getPrefHeight () {
		return Math.max(style.knob.getMinHeight(), style.slider.getMinHeight());
	}

	/** The style for a slider, see {@link Slider}.
	 * @author mzechner
	 * @author Nathan Sweet */
	static public class SliderStyle {
		/** The slider background, stretched only in the x direction. */
		public Drawable slider;
		/** Centered vertically on the background. */
		public Drawable knob;

		public SliderStyle () {
		}

		public SliderStyle (Drawable slider, Drawable knob) {
			this.slider = slider;
			this.knob = knob;
		}

		public SliderStyle (SliderStyle style) {
			this.slider = style.slider;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9233.java