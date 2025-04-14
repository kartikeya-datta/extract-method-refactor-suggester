error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5051.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5051.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5051.java
text:
```scala
m@@esh = new Mesh(true, chunk.vertices.length / 3, chunk.indices.length, new VertexAttribute(


package com.badlogic.gdx.tests;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.tests.utils.GdxTest;

public class TerrainTest implements GdxTest {
	ImmediateModeRenderer renderer;
	TerrainChunk chunk;
	Mesh mesh;
	PerspectiveCamera camera;
	Vector3 intersection = new Vector3();
	boolean intersected = false;
	long lastTime = System.nanoTime();

	@Override public void surfaceCreated () {
		if (chunk == null) {
			renderer = new ImmediateModeRenderer();

			chunk = new TerrainChunk(32, 32, 4);

			Random rand = new Random();
			int len = chunk.vertices.length;
			for (int i = 3; i < len; i += 4)
				chunk.vertices[i] = Color.toFloatBits(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), 255);

			mesh = new Mesh(true, false, chunk.vertices.length / 3, chunk.indices.length, new VertexAttribute(
				VertexAttributes.Usage.Position, 3, "a_position"), new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4,
				"a_color"));

			mesh.setVertices(chunk.vertices);
			mesh.setIndices(chunk.indices);

			camera = new PerspectiveCamera();
			camera.getPosition().set(0, 5, 5);
			camera.getDirection().set(0, 0, 0).sub(camera.getPosition()).nor();
			camera.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			camera.setNear(0.5f);
			camera.setFar(300);
			camera.setFov(67);
		}
	}

	@Override public void surfaceChanged (int width, int height) {

	}

	@Override public void render () {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		camera.setMatrices();
		gl.glColor4f(1, 1, 1, 1);
		mesh.render(GL10.GL_TRIANGLES);

		if (intersected) {
			gl.glPointSize(10);
			renderer.begin(GL10.GL_POINTS);
			renderer.color(1, 0, 0, 1);
			renderer.vertex(intersection.x, intersection.y, intersection.z);
			renderer.end();
		}

		handleInput(Gdx.input, Gdx.graphics.getDeltaTime());

		if (System.nanoTime() - lastTime > 1000000000) {
			Gdx.app.log("TerrainTest", "fps: " + Gdx.graphics.getFramesPerSecond());
			lastTime = System.nanoTime();
		}
	}

	private void handleInput (Input input, float delta) {
		if (input.isTouched()) {
			Ray ray = camera.getPickRay(input.getX(), input.getY());
			if (Intersector.intersectRayTriangles(ray, chunk.vertices, chunk.indices, 4, intersection)) intersected = true;
		} else {
			intersected = false;
		}

		if (input.isKeyPressed(Keys.KEYCODE_W)) camera.getPosition().z -= delta;
		if (input.isKeyPressed(Keys.KEYCODE_S)) camera.getPosition().z += delta;
		if (input.isKeyPressed(Keys.KEYCODE_A)) camera.getPosition().x -= delta;
		if (input.isKeyPressed(Keys.KEYCODE_D)) camera.getPosition().x += delta;
		if (input.isKeyPressed(Keys.KEYCODE_Q)) camera.getPosition().y += delta;
		if (input.isKeyPressed(Keys.KEYCODE_E)) camera.getPosition().y -= delta;
	}

	@Override public void dispose () {

	}

	final static class TerrainChunk {
		public final byte[] heightMap;
		public final short width;
		public final short height;
		public final float[] vertices;
		public final short[] indices;
		public final int vertexSize;

		public TerrainChunk (int width, int height, int vertexSize) {
			if ((width + 1) * (height + 1) > Short.MAX_VALUE)
				throw new IllegalArgumentException("Chunk size too big, (width + 1)*(height+1) must be <= 32767");

			this.heightMap = new byte[(width + 1) * (height + 1)];
			this.width = (short)width;
			this.height = (short)height;
			this.vertices = new float[heightMap.length * vertexSize];
			this.indices = new short[width * height * 6];
			this.vertexSize = vertexSize;

			buildIndices();
			buildVertices();
		}

		public void buildVertices () {
			int heightPitch = height + 1;
			int widthPitch = width + 1;

			int idx = 0;
			int hIdx = 0;
			int inc = vertexSize - 3;

			for (int z = 0; z < heightPitch; z++) {
				for (int x = 0; x < widthPitch; x++) {
					vertices[idx++] = x;
					vertices[idx++] = heightMap[hIdx++];
					vertices[idx++] = z;
					idx += inc;
				}
			}
		}

		private void buildIndices () {
			int idx = 0;
			short pitch = (short)(width + 1);
			short i1 = 0;
			short i2 = 1;
			short i3 = (short)(1 + pitch);
			short i4 = pitch;

			short row = 0;

			for (int z = 0; z < height; z++) {
				for (int x = 0; x < width; x++) {
					indices[idx++] = i1;
					indices[idx++] = i2;
					indices[idx++] = i3;

					indices[idx++] = i3;
					indices[idx++] = i4;
					indices[idx++] = i1;

					i1++;
					i2++;
					i3++;
					i4++;
				}

				row += pitch;
				i1 = row;
				i2 = (short)(row + 1);
				i3 = (short)(i2 + pitch);
				i4 = (short)(row + pitch);
			}
		}
	}

	@Override public boolean needsGL20 () {
		// TODO Auto-generated method stub
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5051.java