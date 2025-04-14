error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2576.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2576.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2576.java
text:
```scala
c@@am.position.mul(30);

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

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.tests.utils.GdxTest;

public class IsoCamTest extends GdxTest {
	private static final int TARGET_WIDTH = 480;
	private static final float UNIT_TO_PIXEL = TARGET_WIDTH * 0.15f;
	Texture texture;
	OrthographicCamera cam;
	SpriteBatch batch;
	final Sprite[][] sprites = new Sprite[10][10];
	final Matrix4 matrix = new Matrix4();

	@Override
	public void create () {
		texture = new Texture(Gdx.files.internal("data/badlogicsmall.jpg"));
		float unitsOnX = (float)Math.sqrt(2) * TARGET_WIDTH / (UNIT_TO_PIXEL);
		float pixelsOnX = Gdx.graphics.getWidth() / unitsOnX;
		float unitsOnY = Gdx.graphics.getHeight() / pixelsOnX;
		cam = new OrthographicCamera(unitsOnX, unitsOnY, 25);
		cam.position.scl(30);
		cam.near = 1;
		cam.far = 1000;
		matrix.setToRotation(new Vector3(1, 0, 0), 90);

		for (int z = 0; z < 10; z++) {
			for (int x = 0; x < 10; x++) {
				sprites[x][z] = new Sprite(texture);
				sprites[x][z].setPosition(x, z);
				sprites[x][z].setSize(1, 1);
			}
		}

		batch = new SpriteBatch();

		Gdx.input.setInputProcessor(new IsoCamController(cam));
	}

	@Override
	public void dispose () {
		texture.dispose();
		batch.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		cam.update();

		batch.setProjectionMatrix(cam.combined);
		batch.setTransformMatrix(matrix);
		batch.begin();
		for (int z = 0; z < 10; z++) {
			for (int x = 0; x < 10; x++) {
				sprites[x][z].draw(batch);
			}
		}
		batch.end();

		checkTileTouched();
	}

	final Plane xzPlane = new Plane(new Vector3(0, 1, 0), 0);
	final Vector3 intersection = new Vector3();
	Sprite lastSelectedTile = null;

	private void checkTileTouched () {
		if (Gdx.input.justTouched()) {
			Ray pickRay = cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());
			Intersector.intersectRayPlane(pickRay, xzPlane, intersection);
			System.out.println(intersection);
			int x = (int)intersection.x;
			int z = (int)intersection.z;
			if (x >= 0 && x < 10 && z >= 0 && z < 10) {
				if (lastSelectedTile != null) lastSelectedTile.setColor(1, 1, 1, 1);
				Sprite sprite = sprites[x][z];
				sprite.setColor(1, 0, 0, 1);
				lastSelectedTile = sprite;
			}
		}
	}

	public class IsoCamController extends InputAdapter {
		final Plane xzPlane = new Plane(new Vector3(0, 1, 0), 0);
		final Vector3 intersection = new Vector3();
		final Vector3 curr = new Vector3();
		final Vector3 last = new Vector3(-1, -1, -1);
		final Vector3 delta = new Vector3();
		final Camera camera;

		public IsoCamController (Camera camera) {
			this.camera = camera;
		}

		@Override
		public boolean touchDragged (int x, int y, int pointer) {
			Ray pickRay = camera.getPickRay(x, y);
			Intersector.intersectRayPlane(pickRay, xzPlane, curr);

			if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
				pickRay = camera.getPickRay(last.x, last.y);
				Intersector.intersectRayPlane(pickRay, xzPlane, delta);
				delta.sub(curr);
				camera.position.add(delta.x, 0, delta.z);
			}
			last.set(x, y, 0);
			return false;
		}

		@Override
		public boolean touchUp (int x, int y, int pointer, int button) {
			last.set(-1, -1, -1);
			return false;
		}
	}

	@Override
	public boolean needsGL20 () {
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2576.java