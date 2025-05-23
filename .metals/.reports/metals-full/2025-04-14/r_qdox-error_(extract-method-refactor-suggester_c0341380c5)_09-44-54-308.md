error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1292.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1292.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1292.java
text:
```scala
i@@f (visualPressedTime > TimeUtils.nanoTime()) return true;

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

package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.TimeUtils;

/** Detects mouse over, mouse or finger touch presses, and clicks on an actor. A touch must go down over the actor and is
 * considered pressed as long as it is over the actor or within the {@link #setTapSquareSize(float) tap square}. This behavior
 * makes it easier to press buttons on a touch interface when the initial touch happens near the edge of the actor. Double clicks
 * can be detected using {@link #getTapCount()}. Any touch (not just the first) will trigger this listener. While pressed, other
 * touch downs are ignored.
 * @author Nathan Sweet */
public class ClickListener extends InputListener {
	/** Time in seconds {@link #isVisualPressed()} reports true after a press resulting in a click is released. */
	static public float visualPressedDuration = 0.1f;

	private float tapSquareSize = 14, touchDownX = -1, touchDownY = -1;
	private int pressedPointer = -1;
	private int pressedButton = -1;
	private int button;
	private boolean pressed, over, cancelled;
	private float visualPressedTime;
	private long tapCountInterval = (long)(0.4f * 1000000000l);
	private int tapCount;
	private long lastTapTime;

	/** Create a listener where {@link #clicked(InputEvent, float, float)} is only called for left clicks.
	 * @see #ClickListener(int) */
	public ClickListener () {
	}

	/** @see #setButton(int) */
	public ClickListener (int button) {
		this.button = button;
	}

	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		if (pressed) return false;
		if (pointer == 0 && this.button != -1 && button != this.button) return false;
		pressed = true;
		pressedPointer = pointer;
		pressedButton = button;
		touchDownX = x;
		touchDownY = y;
		visualPressedTime = TimeUtils.nanoTime() + visualPressedDuration * 1000000000;
		return true;
	}

	public void touchDragged (InputEvent event, float x, float y, int pointer) {
		if (pointer != pressedPointer || cancelled) return;
		pressed = isOver(event.getListenerActor(), x, y);
		if (pressed && pointer == 0 && button != -1 && !Gdx.input.isButtonPressed(button)) pressed = false;
		if (!pressed) {
			// Once outside the tap square, don't use the tap square anymore.
			invalidateTapSquare();
		}
	}

	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		if (pointer == pressedPointer) {
			if (!cancelled) {
				boolean touchUpOver = isOver(event.getListenerActor(), x, y);
				// Ignore touch up if the wrong mouse button.
				if (touchUpOver && pointer == 0 && this.button != -1 && button != this.button) touchUpOver = false;
				if (touchUpOver) {
					long time = TimeUtils.nanoTime();
					if (time - lastTapTime > tapCountInterval) tapCount = 0;
					tapCount++;
					lastTapTime = time;
					clicked(event, x, y);
				}
			}
			pressed = false;
			pressedPointer = -1;
			pressedButton = -1;
			cancelled = false;
		}
	}

	public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
		if (pointer == -1 && !cancelled) over = true;
	}

	public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
		if (pointer == -1 && !cancelled) over = false;
	}

	/** If a touch down is being monitored, the drag and touch up events are ignored until the next touch up. */
	public void cancel () {
		if (pressedPointer == -1) return;
		cancelled = true;
		pressed = false;
	}

	public void clicked (InputEvent event, float x, float y) {
	}

	/** Returns true if the specified position is over the specified actor or within the tap square. */
	public boolean isOver (Actor actor, float x, float y) {
		Actor hit = actor.hit(x, y, true);
		if (hit == null || !hit.isDescendantOf(actor)) return inTapSquare(x, y);
		return true;
	}

	public boolean inTapSquare (float x, float y) {
		if (touchDownX == -1 && touchDownY == -1) return false;
		return Math.abs(x - touchDownX) < tapSquareSize && Math.abs(y - touchDownY) < tapSquareSize;
	}

	/** Returns true if a touch is within the tap square. */
	public boolean inTapSquare () {
		return touchDownX != -1;
	}

	/** The tap square will not longer be used for the current touch. */
	public void invalidateTapSquare () {
		touchDownX = -1;
		touchDownY = -1;
	}

	/** Returns true if a touch is over the actor or within the tap square. */
	public boolean isPressed () {
		return pressed;
	}

	/** Returns true if a touch is over the actor or within the tap square or has been very recently. This allows the UI to show a
	 * press and release that was so fast it occurred within a single frame. */
	public boolean isVisualPressed () {
		if (pressed) return true;
		if (visualPressedTime <= 0) return false;
		if (visualPressedTime > TimeUtils.nanoTime() && Gdx.graphics.isContinuousRendering()) return true;
		visualPressedTime = 0;
		return false;
	}

	/** Returns true if the mouse or touch is over the actor or pressed and within the tap square. */
	public boolean isOver () {
		return over || pressed;
	}

	public void setTapSquareSize (float halfTapSquareSize) {
		tapSquareSize = halfTapSquareSize;
	}

	public float getTapSquareSize () {
		return tapSquareSize;
	}

	/** @param tapCountInterval time in seconds that must pass for two touch down/up sequences to be detected as consecutive taps. */
	public void setTapCountInterval (float tapCountInterval) {
		this.tapCountInterval = (long)(tapCountInterval * 1000000000l);
	}

	/** Returns the number of taps within the tap count interval for the most recent click event. */
	public int getTapCount () {
		return tapCount;
	}

	public float getTouchDownX () {
		return touchDownX;
	}

	public float getTouchDownY () {
		return touchDownY;
	}

	/** The button that initially pressed this button or -1 if the button is not pressed. */
	public int getPressedButton () {
		return pressedButton;
	}

	/** The pointer that initially pressed this button or -1 if the button is not pressed. */
	public int getPressedPointer () {
		return pressedPointer;
	}

	/** @see #setButton(int) */
	public int getButton () {
		return button;
	}

	/** Sets the button to listen for, all other buttons are ignored. Default is {@link Buttons#LEFT}. Use -1 for any button. */
	public void setButton (int button) {
		this.button = button;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1292.java