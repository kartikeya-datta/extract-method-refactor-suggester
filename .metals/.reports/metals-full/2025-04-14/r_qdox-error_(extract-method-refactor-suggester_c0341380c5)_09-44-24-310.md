error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1502.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1502.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1502.java
text:
```scala
s@@crollPane.setOverscroll(false, false);

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
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.tests.utils.GdxTest;

public class InterpolationTest extends GdxTest {
	static private final String[] interpolators = new String[] {"bounce", "bounceIn", "bounceOut", "circle", "circleIn",
		"circleOut", "elastic", "elasticIn", "elasticOut", "exp10", "exp10In", "exp10Out", "exp5", "exp5In", "exp5Out", "fade",
		"linear", "pow2", "pow2In", "pow2Out", "pow3", "pow3In", "pow3Out", "pow4", "pow4In", "pow4Out", "pow5", "pow5In",
		"pow5Out", "sine", "sineIn", "sineOut", "swing", "swingIn", "swingOut"};

	private Stage stage;
	private Table root;
	private List list;
	private ShapeRenderer renderer;
	Vector2 position = new Vector2(300, 20);
	Vector2 targetPosition = new Vector2(position);
	Vector2 temp = new Vector2();
	float timer;

	public void create () {
		renderer = new ShapeRenderer();

		stage = new Stage(0, 0, true);
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
			public boolean touchDown (int x, int y, int pointer, int button) {
				Vector2 current = getCurrentPosition();
				position.set(current);
				targetPosition.set(x - 10, Gdx.graphics.getHeight() - y - 10);
				timer = 0;
				return true;
			}
		}));

		root = new Table();
		stage.addActor(root);
		root.pad(10).top().left();

		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		list = new List(interpolators, skin);
		ScrollPane scrollPane = new ScrollPane(list, skin);
		scrollPane.setOverscroll(false);
		scrollPane.setFadeScrollBars(false);
		root.add(scrollPane).expandY().fillY().prefWidth(110);
	}

	public void resize (int width, int height) {
		stage.setViewport(width, height, true);
		root.setSize(width, height);
		root.invalidate();
	}

	public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		int steps = 100;
		int size = 200;
		int x = Gdx.graphics.getWidth() / 2 - size / 2;
		int y = Gdx.graphics.getHeight() / 2 - size / 2;

		renderer.setProjectionMatrix(stage.getCamera().combined);

		renderer.begin(ShapeType.Box);
		renderer.box(x, y, 0, size, size, 0);
		renderer.end();

		Interpolation interpolation = getInterpolation();
		float lastX = x, lastY = y;
		renderer.begin(ShapeType.Line);
		for (int i = 0; i <= steps; i++) {
			float alpha = i / (float)steps;
			float lineX = x + size * alpha;
			float lineY = y + size * interpolation.apply(alpha);
			renderer.line(lastX, lastY, lineX, lineY);
			lastX = lineX;
			lastY = lineY;
		}
		renderer.end();

		timer += Gdx.graphics.getDeltaTime();
		Vector2 current = getCurrentPosition();
		renderer.begin(ShapeType.FilledRectangle);
		renderer.filledRect(current.x, current.y, 20, 20);
		renderer.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	Vector2 getCurrentPosition () {
		temp.set(targetPosition);
		temp.sub(position);
		temp.mul(getInterpolation().apply(Math.min(1, timer / 1f)));
		temp.add(position);
		return temp;
	}

	private Interpolation getInterpolation () {
		try {
			return (Interpolation)Interpolation.class.getField(list.getSelection()).get(null);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1502.java