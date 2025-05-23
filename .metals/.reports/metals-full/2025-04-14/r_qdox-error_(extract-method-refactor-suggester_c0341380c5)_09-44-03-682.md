error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5182.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5182.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5182.java
text:
```scala
c@@ircle(x, y, radius, (int)(6 * (float)Math.cbrt(radius)));

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

package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Renders points, lines, rectangles, filled rectangles and boxes. This class is not meant to be used for performance sensitive
 * applications but more oriented towards debugging.</p>
 * 
 * This class works with OpenGL ES 1.x and 2.0. In its base configuration a 2D orthographic projection with the origin in the
 * lower left corner is used. Units are given in screen pixels.</p>
 * 
 * To change the projection properties use the {@link #setProjectionMatrix(Matrix4)} method. Usually the {@link Camera#combined}
 * matrix is set via this method. If the screen orientation or resolution changes, the projection matrix might have to be adapted
 * as well.</p>
 * 
 * Shapes are rendered in batches to increase performance. The standard use-pattern looks as follows:
 * 
 * <pre>
 * {@code
 * camera.update();
 * shapeRenderer.setProjectionMatrix(camera.combined);
 * 
 * shapeRenderer.begin(ShapeType.Line);
 * shapeRenderer.color(1, 1, 0, 1);
 * shapeRenderer.line(x, y, x2, y2);
 * shapeRenderer.line(x3, y3, x4, y4);
 * shapeRenderer.end();
 * 
 * shapeRenderer.begin(ShapeType.Box);
 * shapeRenderer.color(0, 1, 0, 1);
 * shapeRenderer.box(x, y, z, width, height, depth);
 * shapeRenderer.end();
 * }
 * </pre>
 * 
 * The class has a second matrix called the transformation matrix which is used to rotate, scale and translate shapes in a more
 * flexible manner. This mechanism works much like matrix operations in OpenGL ES 1.x. The following example shows how to rotate a
 * rectangle around its center using the z-axis as the rotation axis and placing it's center at (20, 12, 2):
 * 
 * <pre>
 * shapeRenderer.begin(ShapeType.Rectangle);
 * shapeRenderer.identity();
 * shapeRenderer.translate(20, 12, 2);
 * shapeRenderer.rotate(0, 0, 1, 90);
 * shapeRenderer.rect(-width / 2, -height / 2, width, height);
 * shapeRenderer.end();
 * </pre>
 * 
 * Matrix operations all use postmultiplication and work just like glTranslate, glScale and glRotate. The last transformation
 * specified will be the first that is applied to a shape (rotate then translate in the above example).
 * 
 * The projection and transformation matrices are a state of the ShapeRenderer, just like the color and will be applied to all
 * shapes until they are changed.
 * 
 * @author mzechner */
public class ShapeRenderer {
	/** Shape types to be used with {@link #begin(ShapeType)}.
	 * @author mzechner */
	public enum ShapeType {
		Point(GL10.GL_POINTS), //
		Line(GL10.GL_LINES), //
		Rectangle(GL10.GL_LINES), //
		FilledRectangle(GL10.GL_TRIANGLES), //
		Box(GL10.GL_LINES), //
		Circle(GL10.GL_LINES), //
		FilledCircle(GL10.GL_TRIANGLES); //

		private final int glType;

		ShapeType (int glType) {
			this.glType = glType;
		}

		public int getGlType () {
			return glType;
		}
	}

	ImmediateModeRenderer renderer;
	boolean matrixDirty = false;
	Matrix4 projView = new Matrix4();
	Matrix4 transform = new Matrix4();
	Matrix4 combined = new Matrix4();
	Matrix4 tmp = new Matrix4();
	Color color = new Color(1, 1, 1, 1);
	ShapeType currType = null;

	public ShapeRenderer () {
		if (Gdx.graphics.isGL20Available())
			renderer = new ImmediateModeRenderer20(false, true, 0);
		else
			renderer = new ImmediateModeRenderer10();
		projView.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/** Sets the {@link Color} to be used by shapes.
	 * @param color */
	public void setColor (Color color) {
		this.color.set(color);
	}

	/** Sets the {@link Color} to be used by shapes.
	 * @param r
	 * @param g
	 * @param b
	 * @param a */
	public void setColor (float r, float g, float b, float a) {
		this.color.set(r, g, b, a);
	}

	/** Sets the projection matrix to be used for rendering. Usually this will be set to {@link Camera#combined}.
	 * @param matrix */
	public void setProjectionMatrix (Matrix4 matrix) {
		projView.set(matrix);
		matrixDirty = true;
	}

	public void setTransformMatrix (Matrix4 matrix) {
		transform.set(matrix);
		matrixDirty = true;
	}

	/** Sets the transformation matrix to identity. */
	public void identity () {
		transform.idt();
		matrixDirty = true;
	}

	/** Multiplies the current transformation matrix by a translation matrix.
	 * @param x
	 * @param y
	 * @param z */
	public void translate (float x, float y, float z) {
		transform.translate(x, y, z);
		matrixDirty = true;
	}

	/** Multiplies the current transformation matrix by a rotation matrix.
	 * @param angle angle in degrees
	 * @param axisX
	 * @param axisY
	 * @param axisZ */
	public void rotate (float axisX, float axisY, float axisZ, float angle) {
		transform.rotate(axisX, axisY, axisZ, angle);
		matrixDirty = true;
	}

	/** Multiplies the current transformation matrix by a scale matrix.
	 * @param scaleX
	 * @param scaleY
	 * @param scaleZ */
	public void scale (float scaleX, float scaleY, float scaleZ) {
		transform.scale(scaleX, scaleY, scaleZ);
		matrixDirty = true;
	}

	/** Starts a new batch of shapes. All shapes within the batch have to have the type specified. E.g. if {@link ShapeType#Point}
	 * is specified, only call #point().
	 * 
	 * The call to this method must be paired with a call to {@link #end()}.
	 * 
	 * In case OpenGL ES 1.x is used, the projection and modelview matrix will be modified.
	 * 
	 * @param type the {@link ShapeType}. */
	public void begin (ShapeType type) {
		if (currType != null) throw new GdxRuntimeException("Call end() before beginning a new shape batch");
		currType = type;
		if (matrixDirty) {
			combined.set(projView);
			Matrix4.mul(combined.val, transform.val);
			matrixDirty = false;
		}
		if (renderer instanceof ImmediateModeRenderer10) {
			Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
			Gdx.gl10.glLoadMatrixf(combined.val, 0);
			Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
			Gdx.gl10.glLoadIdentity();
			((ImmediateModeRenderer10)renderer).begin(currType.getGlType());
		} else {
			((ImmediateModeRenderer20)renderer).begin(combined, currType.getGlType());
		}
	}

	/** Draws a point. The {@link ShapeType} passed to begin has to be {@link ShapeType#Point}.
	 * @param x
	 * @param y
	 * @param z */
	public void point (float x, float y, float z) {
		if (currType != ShapeType.Point) throw new GdxRuntimeException("Must call begin(ShapeType.Point)");
		checkDirty();
		checkFlush(1);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, z);
	}

	/** Draws a line. The {@link ShapeType} passed to begin has to be {@link ShapeType#Line}.
	 * @param x
	 * @param y
	 * @param z
	 * @param x2
	 * @param y2
	 * @param z2 */
	public void line (float x, float y, float z, float x2, float y2, float z2) {
		if (currType != ShapeType.Line) throw new GdxRuntimeException("Must call begin(ShapeType.Line)");
		checkDirty();
		checkFlush(2);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, z);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x2, y2, z2);
	}

	/** Draws a line in the x/y plane. The {@link ShapeType} passed to begin has to be {@link ShapeType#Line}.
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2 */
	public void line (float x, float y, float x2, float y2) {
		if (currType != ShapeType.Line) throw new GdxRuntimeException("Must call begin(ShapeType.Line)");
		checkDirty();
		checkFlush(2);
		if (currType != ShapeType.Line) throw new GdxRuntimeException("Must call begin(ShapeType.Line)");
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x2, y2, 0);
	}

	/** Draws a rectangle in the x/y plane. The x and y coordinate specify the bottom left corner of the rectangle. The
	 * {@link ShapeType} passed to begin has to be {@link ShapeType#Rectangle}.
	 * @param x
	 * @param y
	 * @param width
	 * @param height */
	public void rect (float x, float y, float width, float height) {
		if (currType != ShapeType.Rectangle) throw new GdxRuntimeException("Must call begin(ShapeType.Rectangle)");
		checkDirty();
		checkFlush(8);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y, 0);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, 0);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y + height, 0);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y + height, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, 0);
	}

	/** Draws a filled rectangle in the x/y plane. The x and y coordinate specify the bottom left corner of the rectangle. The
	 * {@link ShapeType} passed to begin has to be {@link ShapeType#FilledRectangle}.
	 * @param x
	 * @param y
	 * @param width
	 * @param height */
	public void filledRect (float x, float y, float width, float height) {
		if (currType != ShapeType.FilledRectangle) throw new GdxRuntimeException("Must call begin(ShapeType.FilledRectangle)");
		checkDirty();
		checkFlush(8);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, 0);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y + height, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, 0);
	}

	/** Draws a box. The x, y and z coordinate specify the bottom left front corner of the rectangle. The {@link ShapeType} passed
	 * to begin has to be {@link ShapeType#Box}.
	 * @param x
	 * @param y
	 * @param width
	 * @param height */
	public void box (float x, float y, float z, float width, float height, float depth) {
		if (currType != ShapeType.Box) throw new GdxRuntimeException("Must call begin(ShapeType.Box)");
		checkDirty();
		checkFlush(16);

		depth = -depth;
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, z);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y, z);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y, z);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y, z + depth);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y, z + depth);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, z + depth);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, z + depth);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, z);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, z);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y + height, z);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y + height, z);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, z);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, z);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, z + depth);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, z + depth);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y + height, z + depth);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y + height, z + depth);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y + height, z);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y, z);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, z);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y, z + depth);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x + width, y + height, z + depth);

		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y, z + depth);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x, y + height, z + depth);
	}

	/** Calls {@link #circle(float, float, float, int)} by estimating the number of segments needed for a smooth circle. */
	public void circle (float x, float y, float radius) {
		circle(x, y, radius, (int)(4 * (float)Math.sqrt(radius)));
	}

	public void circle (float x, float y, float radius, int segments) {
		if (segments <= 0) throw new IllegalArgumentException("segments must be >= 0.");
		if (currType != ShapeType.Circle) throw new GdxRuntimeException("Must call begin(ShapeType.Circle)");
		checkDirty();
		checkFlush(segments * 2 + 2);

		float angle = 2 * 3.1415926f / segments;
		float cos = MathUtils.cos(angle);
		float sin = MathUtils.sin(angle);
		float cx = radius, cy = 0;
		for (int i = 0; i <= segments; i++) {
			renderer.color(color.r, color.g, color.b, color.a);
			renderer.vertex(x + cx, y + cy, 0);
			float temp = cx;
			cx = cos * cx - sin * cy;
			cy = sin * temp + cos * cy;
			renderer.color(color.r, color.g, color.b, color.a);
			renderer.vertex(x + cx, y + cy, 0);
		}
	}

	/** Calls {@link #filledCircle(float, float, float, int)} by estimating the number of segments needed for a smooth circle. */
	public void filledCircle (float x, float y, float radius) {
		filledCircle(x, y, radius, (int)(4 * (float)Math.sqrt(radius)));
	}

	public void filledCircle (float x, float y, float radius, int segments) {
		if (segments <= 0) throw new IllegalArgumentException("segments must be >= 0.");
		if (currType != ShapeType.FilledCircle) throw new GdxRuntimeException("Must call begin(ShapeType.FilledCircle)");
		checkDirty();
		checkFlush(segments * 3 + 3);

		int inc = 360 / segments;
		float angle = 2 * 3.1415926f / segments;
		float cos = MathUtils.cos(angle);
		float sin = MathUtils.sin(angle);
		float cx = radius, cy = 0;
		for (int i = 0; i <= segments; i++) {
			renderer.color(color.r, color.g, color.b, color.a);
			renderer.vertex(x, y, 0);
			renderer.color(color.r, color.g, color.b, color.a);
			renderer.vertex(x + cx, y + cy, 0);
			float temp = cx;
			cx = cos * cx - sin * cy;
			cy = sin * temp + cos * cy;
			renderer.color(color.r, color.g, color.b, color.a);
			renderer.vertex(x + cx, y + cy, 0);
		}
	}

	private void checkDirty () {
		if (!matrixDirty) return;
		ShapeType type = currType;
		end();
		begin(type);
	}

	private void checkFlush (int newVertices) {
		if (renderer.getMaxVertices() - renderer.getNumVertices() >= newVertices) return;
		ShapeType type = currType;
		end();
		begin(type);
	}

	/** Finishes the batch of shapes and ensures they get rendered. */
	public void end () {
		renderer.end();
		currType = null;
	}

	public void dispose () {
		renderer.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5182.java