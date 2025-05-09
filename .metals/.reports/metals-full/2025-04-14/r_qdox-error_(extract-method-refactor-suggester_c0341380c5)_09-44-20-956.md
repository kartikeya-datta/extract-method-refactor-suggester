error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9542.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9542.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9542.java
text:
```scala
S@@tring name = this.name != null ? this.name : getClass().getName();

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

package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.utils.PooledLinkedList;

/** <p>
 * An Actor is part of a {@link Stage} or a {@link Group} within a Stage. It has a position, a rectangular size given as width and
 * height, a rotation angle, a scale in x and y and an origin relative to the position which is used for rotation and scaling.
 * </p>
 * 
 * <p>
 * The position of an Actor is coincident with its unrotated, unscaled bottom left corner.
 * </p>
 * 
 * <p>
 * An Actor can be a child of a Group or the Stage. The object it belongs to is called the Actor's parent. An Actor's position is
 * always relative to the bottom left corner of its parent.
 * </p>
 * 
 * <p>
 * Every Actor must have a unique name within a Stage.
 * </p>
 * 
 * <p>
 * An Actor can receive touch events when its {@link #touchable} attribute is set to true. The Stage will delegate touch events to
 * an Actor in this case, calling its {@link #touchDown(float, float, int)}, {@link #touchUp(float, float, int)} and
 * {@link #touchDragged(float, float, int)} methods. The coordinates passed to an Actor will be in the Actor's coordinate system,
 * easing the pain of intersection testing. The coordinate system has it's origin at the Actor's rotated and scaled bottom left
 * corner, with the x-axis pointing to the right and the y-axis pointing to the left.
 * </p>
 * 
 * <p>
 * An Actor can be intersection tested via a call this its {@link #hit(float, float)} method. The coordinates given are again in
 * the Actor's coordinate system.
 * </p>
 * 
 * <p>
 * An Actor might render itself when its {@link #draw(SpriteBatch, float)} method is called. The projection and transform matrices
 * are setup so that an Actor can simply call the
 * {@link SpriteBatch#draw(com.badlogic.gdx.graphics.Texture, float, float, float, float, float, float, float, float, float, int, int, int, int, boolean, boolean)}
 * method to render himself. Using a {@link Sprite} instance is also an option. An Actor might decide to not render itself at all
 * or chose another way to render itself. For the later it has to call {@link SpriteBatch#end()} first, setup up all render states
 * needed to render itself, render itself and then call {@link SpriteBatch#begin()} again so that the rendering for other Actor's
 * is undisturbed. You have to know what you do if you want to try this.
 * </p>
 * 
 * <p>
 * An Actor can be controlled by {@link Action}s. An Action will modify some of the attributes of an Actor such as its position or
 * rotation. Actions can be chained and make for rather sophisticated time based behaviour.
 * <p>
 * 
 * @author mzechner */
public abstract class Actor {
	public Group parent;
	public final String name;
	public boolean touchable = true;
	public boolean visible = true;

	public float x;
	public float y;
	public float width;
	public float height;
	public float originX;
	public float originY;
	public float scaleX = 1;
	public float scaleY = 1;
	public float rotation;
	public final Color color = new Color(1, 1, 1, 1);

	protected PooledLinkedList<Action> actions = new PooledLinkedList<Action>(10);
	private boolean toRemove;

	protected Stage stage;

	/** Creates an actor without a name. */
	public Actor () {
		this.name = null;
	}

	public Actor (String name) {
		this.name = name;
	}

	/** Draws the Actor. The spriteBatch is configured so that the Actor can draw in its parents coordinate system. The parent's
	 * alpha is passed to the method in order for the Actor to multiply it with its own alpha. This will allow {@link FadeIn} and
	 * other Actions to have an effect even if they are only set on the parent of the Actor.
	 * 
	 * @param batch the spritebatch to render with
	 * @param parentAlpha the parent's alpha value. */
	public abstract void draw (SpriteBatch batch, float parentAlpha);

	public boolean touchDown (float x, float y, int pointer) {
		return false;
	}

	public void touchUp (float x, float y, int pointer) {
	}

	public void touchDragged (float x, float y, int pointer) {
	}

	public boolean touchMoved (float x, float y) {
		return false;
	}

	public boolean scrolled (int amount) {
		return false;
	}

	public boolean keyDown (int keycode) {
		return false;
	}

	public boolean keyUp (int keycode) {
		return false;
	}

	public boolean keyTyped (char character) {
		return false;
	}

	public abstract Actor hit (float x, float y);

	/** Transforms the given point in stage coordinates to the Actor's local coordinate system.
	 * @param point the point */
	public void toLocalCoordinates (Vector2 point) {
		if (parent == null) return;
		parent.toLocalCoordinates(point);
		Group.toChildCoordinates(this, point.x, point.y, point);
	}

	/** Removes this actor from the Stage */
	public void remove () {
		if (parent != null) parent.removeActor(this);
	}

	public void act (float delta) {
		actions.iter();
		Action action;

		while ((action = actions.next()) != null) {
			action.act(delta);
			if (action.isDone()) {
				action.finish();
				actions.remove();
			}
		}
	}

	/** Adds an {@link Action} to the Actor. Actions will be automatically performed in the order added to the Actor and will be
	 * removed when they are done.
	 * 
	 * @param action the action */
	public void action (Action action) {
		action.setTarget(this);
		actions.add(action);
	}

	/** Clears all actions of this Actor. */
	public void clearActions () {
		actions.clear();
	}

	@Override
	public String toString () {
		String name = this.name != null ? this.name : getClass().getSimpleName();
		if (name.equals("")) name = getClass().getName();
		return name + " pos=" + x + "," + y + " origin=" + originX + "," + originY + " size=" + width + "," + height;
	}

	/** Marks the {@link Actor} to be removed by its parent.
	 * <p>
	 * The actual removal happens in the {@link Group#act(float)} method of the parent and after the parent has called
	 * {@link #act(float)} on this {@link Actor}.
	 * 
	 * @param remove whether the parent is supposed to remove this {@link Actor} */
	public void markToRemove (final boolean remove) {
		toRemove = remove;
	}

	/** States if this actor is to be removed by its parent.
	 * 
	 * @return <code>true</code> when the actor is to be removed or <code>false</code> otherwise */
	public boolean isMarkedToRemove () {
		return toRemove;
	}

	/** Returns the stage that this actor is currently in, or null if not in a stage. */
	public Stage getStage () {
		return stage;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9542.java