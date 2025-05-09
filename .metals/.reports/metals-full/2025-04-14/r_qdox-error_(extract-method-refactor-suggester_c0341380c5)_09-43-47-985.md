error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8780.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8780.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8780.java
text:
```scala
o@@ut.y = x / child.scaleY - (child.y - child.originY);

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.scenes.scene2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

/**
 * A group is an Actor that contains other Actors (also other Groups which are Actors).
 * 
 * @author mzechner
 * 
 */
public class Group extends Actor {
	public static Texture debugTexture;
	public static boolean debug = false;

	private final Matrix4 tmp4 = new Matrix4();
	private final Matrix4 oldBatchTransform = new Matrix4();
	private final Matrix3 transform;
	private final Matrix3 scenetransform = new Matrix3();

	private final List<Actor> children; // TODO O(n) delete, baaad.
	private final List<Actor> immutableChildren;
	private final List<Group> groups; // TODO O(n) delete, baad.
	private final List<Group> immutableGroups;
	private final Map<String, Actor> namesToActors;

	public Actor lastTouchedChild;
	public Actor focusedActor = null;

	/**
	 * Creates a new Group with the given name.
	 * @param name the name of the group
	 */
	public Group (String name) {
		super(name);
		this.transform = new Matrix3();
		this.children = new ArrayList<Actor>();
		this.immutableChildren = Collections.unmodifiableList(this.children);
		this.groups = new ArrayList<Group>();
		this.immutableGroups = Collections.unmodifiableList(this.groups);
		this.namesToActors = new HashMap<String, Actor>();
	}

	private void updateTransform () {
		transform.idt();
		if (originX != 0 || originY != 0) transform.setToTranslation(originX, originY);
		if (rotation != 0) transform.mul(scenetransform.setToRotation(rotation));
		if (scaleX != 1 || scaleY != 1) transform.mul(scenetransform.setToScaling(scaleX, scaleY));
		if (originX != 0 || originY != 0) transform.mul(scenetransform.setToTranslation(-originX, -originY));
		if (x != 0 || y != 0) {
			transform.getValues()[6] += x;
			transform.getValues()[7] += y;
		}

		if (parent != null) {
			scenetransform.set(parent.scenetransform);
			scenetransform.mul(transform);
		} else {
			scenetransform.set(transform);
		}
	}

	protected void act (float delta) {
		super.act(delta);

		int len = children.size();
		for (int i = 0; i < len; i++) {
			Actor child = children.get(i);
			child.act(delta);
		}
	}

	@Override protected void render (SpriteBatch batch) {
		updateTransform();
		tmp4.set(scenetransform);

		if (debug && debugTexture != null && parent != null)
			batch.draw(debugTexture, x, y, originX, originY, width == 0 ? 200 : width, height == 0 ? 200 : height, scaleX, scaleY,
				rotation, 0, 0, debugTexture.getWidth(), debugTexture.getHeight(), Color.WHITE, false, false);

		batch.end();
		oldBatchTransform.set(batch.getTransformMatrix());
		batch.setTransformMatrix(tmp4);
		batch.begin();

		int len = children.size();
		for (int i = 0; i < len; i++)
			children.get(i).render(batch);

		batch.end();
		batch.setTransformMatrix(oldBatchTransform);
		batch.begin();
	}

	final Vector2 point = new Vector2();

	static final Vector2 xAxis = new Vector2();
	static final Vector2 yAxis = new Vector2();
	static final Vector2 p = new Vector2();
	static final Vector2 ref = new Vector2();

	/**
	 * Transforms the coordinates given in the child's parent coordinate system to the
	 * child {@link Actor}'s coordinate system. 
	 * @param child the child Actor
	 * @param x the x-coordinate in the Group's coordinate system
	 * @param y the y-coordinate in the Group's coordinate system
	 * @param out the output {@link Vector2}
	 */
	public static void toChildCoordinates (Actor child, float x, float y, Vector2 out) {
		if (child.rotation == 0) {
			if (child.scaleX == 1 && child.scaleY == 1) {
				out.x = x - child.x;
				out.y = y - child.y;
			} else {
				if (child.originX == 0 && child.originY == 0) {
					out.x = (x - child.x) / child.scaleX;
					out.y = (y - child.y) / child.scaleY;
				} else {
					out.x = x / child.scaleX - (child.x - child.originX);
					out.x = x / child.scaleX - (child.x - child.originX);
				}
			}
		} else {
			final float cos = (float)Math.cos((float)Math.toRadians(child.rotation));
			final float sin = (float)Math.sin((float)Math.toRadians(child.rotation));

			if (child.scaleX == 1 && child.scaleY == 1) {
				if (child.originX == 0 && child.originY == 0) {
					float tox = x - child.x;
					float toy = y - child.y;

					out.x = tox * cos + toy * sin;
					out.y = tox * -sin + toy * cos;
				} else {
					final float worldOriginX = child.x + child.originX;
					final float worldOriginY = child.y + child.originY;
					float fx = -child.originX;
					float fy = -child.originY;

					float x1 = cos * fx - sin * fy;
					float y1 = sin * fx + cos * fy;
					x1 += worldOriginX;
					y1 += worldOriginY;

					float tox = x - x1;
					float toy = y - y1;

					out.x = tox * cos + toy * sin;
					out.y = tox * -sin + toy * cos;
				}
			} else {
				if (child.originX == 0 && child.originY == 0) {
					float tox = x - child.x;
					float toy = y - child.y;

					out.x = tox * cos + toy * sin;
					out.y = tox * -sin + toy * cos;

					out.x /= child.scaleX;
					out.y /= child.scaleY;
				} else {
					float srefX = child.originX * child.scaleX;
					float srefY = child.originY * child.scaleY;

					final float worldOriginX = child.x + child.originX;
					final float worldOriginY = child.y + child.originY;
					float fx = -srefX;
					float fy = -srefY;

					float x1 = cos * fx - sin * fy;
					float y1 = sin * fx + cos * fy;
					x1 += worldOriginX;
					y1 += worldOriginY;

					float tox = x - x1;
					float toy = y - y1;

					out.x = tox * cos + toy * sin;
					out.y = tox * -sin + toy * cos;

					out.x /= child.scaleX;
					out.y /= child.scaleY;
				}
			}
		}
	}

	@Override protected boolean touchDown (float x, float y, int pointer) {
		if (!touchable) return false;

		if (debug) Gdx.app.log("Group", name + ": " + x + ", " + y);

		if (focusedActor != null) {
			point.x = x;
			point.y = y;
			focusedActor.toLocalCoordinates(point);
			focusedActor.touchDown(point.x, point.y, pointer);
			return true;
		}

		int len = children.size() - 1;
		for (int i = len; i >= 0; i--) {
			Actor child = children.get(i);
			if (!child.touchable) continue;

			toChildCoordinates(child, x, y, point);

			if (child.touchDown(point.x, point.y, pointer)) {
				if (child instanceof Group)
					lastTouchedChild = ((Group)child).lastTouchedChild;
				else
					lastTouchedChild = child;
				return true;
			}
		}

		return false;
	}

	@Override protected boolean touchUp (float x, float y, int pointer) {
		if (!touchable) return false;

		if (focusedActor != null) {
			point.x = x;
			point.y = y;
			focusedActor.toLocalCoordinates(point);
			focusedActor.touchUp(point.x, point.y, pointer);
			return true;
		}

		int len = children.size() - 1;
		for (int i = len; i >= 0; i--) {
			Actor child = children.get(i);
			if (!child.touchable) continue;

			toChildCoordinates(child, x, y, point);

			if (child.touchUp(point.x, point.y, pointer)) return true;
		}
		return false;
	}

	@Override protected boolean touchDragged (float x, float y, int pointer) {
		if (!touchable) return false;

		if (focusedActor != null) {
			point.x = x;
			point.y = y;
			focusedActor.toLocalCoordinates(point);
			focusedActor.touchDragged(point.x, point.y, pointer);
			return true;
		}

		int len = children.size() - 1;
		for (int i = len; i >= 0; i--) {
			Actor child = children.get(i);
			if (!child.touchable) continue;

			toChildCoordinates(child, x, y, point);

			if (child.touchDragged(point.x, point.y, pointer)) return true;
		}
		return false;
	}

	public Actor hit (float x, float y) {
		int len = children.size() - 1;
		for (int i = len; i >= 0; i--) {
			Actor child = children.get(i);

			toChildCoordinates(child, x, y, point);

			Actor hit = child.hit(point.x, point.y);
			if (hit != null) {
				return hit;
			}
		}
		return null;
	}

	/**
	 * Adds an {@link Actor} to this Group. The order Actors are added
	 * is reversed for hit testing and rendering.
	 * @param actor the Actor
	 */
	public void addActor (Actor actor) {
		children.add(actor);
		if (actor instanceof Group) groups.add((Group)actor);
		namesToActors.put(actor.name, actor);
		actor.parent = this;
	}

	/**
	 * Removes an {@link Actor} from this Group. 
	 * @param actor
	 */
	public void removeActor (Actor actor) {
		children.remove(actor);
		if (actor instanceof Group) groups.remove((Group)actor);
		namesToActors.remove(actor.name);
	}

	/**
	 * Finds the {@link Actor} with the given name in this Group and
	 * its children.
	 * @param name the name of the Actor
	 * @return the Actor or null
	 */
	public Actor findActor (String name) {
		Actor actor = namesToActors.get(name);
		if (actor == null) {
			int len = groups.size();
			for (int i = 0; i < len; i++) {
				actor = groups.get(i).findActor(name);
				if (actor != null) return actor;
			}
		}

		return actor;
	}

	/**
	 * @return all child {@link Actor}s
	 */
	public List<Actor> getActors () {
		return immutableChildren;
	}

	/**
	 * @return all child {@link Group}s
	 */
	public List<Group> getGroups () {
		return immutableGroups;
	}

	/**
	 * Sets the focus to the given child {@link Actor}. All subsequent touch events
	 * will be passed to this child Actor. To unset the focus simply pass null.
	 * 
	 * @param actor the Actor
	 */
	public void focus (Actor actor) {
		focusedActor = actor;
		if (parent != null) parent.focus(actor);
	}

	public static void enableDebugging (String debugTextureFile) {
		debugTexture = Gdx.graphics.newTexture(Gdx.files.getFileHandle(debugTextureFile, FileType.Internal), TextureFilter.Linear,
			TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		debug = true;
	}

	public static void disableDebugging () {
		if (debugTexture != null) debugTexture.dispose();
		debug = false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8780.java