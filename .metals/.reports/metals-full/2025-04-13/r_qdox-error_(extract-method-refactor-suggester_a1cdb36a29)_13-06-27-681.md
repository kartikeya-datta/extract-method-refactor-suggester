error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/179.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/179.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/179.java
text:
```scala
t@@ = paths.get(currentPath).locate(tmpV.set(x, Gdx.graphics.getHeight()-y));

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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.Array;

/** @author Xoppa */
public class PathTest extends GdxTest {
	int SAMPLE_POINTS = 100;
	float SAMPLE_POINT_DISTANCE = 1f/SAMPLE_POINTS;
	
	SpriteBatch spriteBatch;
	ImmediateModeRenderer10 renderer;
	Sprite obj;
	Array<Path<Vector2>> paths = new Array<Path<Vector2>>();
	int currentPath = 0;
	float t;
	float speed = 0.3f;
	float wait = 0f;

	@Override
	public boolean needsGL20 () {
		return false;
	}
	
	@Override
	public void create () {
		renderer = new ImmediateModeRenderer10();
		spriteBatch = new SpriteBatch();
		obj = new Sprite(new Texture(Gdx.files.internal("data/badlogicsmall.jpg")));
		
		float w = Gdx.graphics.getWidth() - obj.getWidth();
		float h = Gdx.graphics.getHeight() - obj.getHeight();
		
		paths.add(new Bezier<Vector2>(new Vector2(0,0), new Vector2(w, h)));
		paths.add(new Bezier<Vector2>(new Vector2(0,0), new Vector2(0, h), new Vector2(w, h)));
		paths.add(new Bezier<Vector2>(new Vector2(0,0), new Vector2(w, 0), new Vector2(0, h), new Vector2(w, h)));
		
		Vector2 cp[] = new Vector2[]{
			new Vector2(0, 0), new Vector2(w * 0.25f, h * 0.5f), new Vector2(0, h), new Vector2(w*0.5f, h*0.75f),
			new Vector2(w, h), new Vector2(w * 0.75f, h * 0.5f), new Vector2(w, 0), new Vector2(w*0.5f, h*0.25f)
		};
		paths.add(new BSpline<Vector2>(cp, 3, true));
		
		paths.add(new CatmullRomSpline<Vector2>(cp, true));
		
		Gdx.input.setInputProcessor(this);
	}
	
	final Vector2 tmpV = new Vector2();
	@Override
	public void render () {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (wait > 0)
			wait -= Gdx.graphics.getDeltaTime();
		else {
			t += speed * Gdx.graphics.getDeltaTime();
			while (t >= 1f) {
				currentPath = (currentPath + 1) % paths.size;
				t -= 1f;
			}
			
			paths.get(currentPath).valueAt(tmpV, t);
			obj.setPosition(tmpV.x, tmpV.y);
		}
			
		spriteBatch.begin();
		
		renderer.begin(GL10.GL_LINE_STRIP);
		float val = 0f;
		while (val <= 1f) {
			renderer.color(0f, 0f, 0f, 1f);
			paths.get(currentPath).valueAt(/*out:*/tmpV, val);
			renderer.vertex(tmpV.x, tmpV.y, 0);
			val += SAMPLE_POINT_DISTANCE;
		}
		renderer.end();
		
		obj.draw(spriteBatch);
		spriteBatch.end();
	}
	
	private void touch(int x, int y) {
		t = paths.get(currentPath).approximate(tmpV.set(x, Gdx.graphics.getHeight()-y));
		paths.get(currentPath).valueAt(tmpV, t);
		obj.setPosition(tmpV.x, tmpV.y);
		wait = 0.2f;		
	}
	
	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		touch(screenX, screenY);
		return super.touchUp(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		touch(screenX, screenY);
		return super.touchDragged(screenX, screenY, pointer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/179.java