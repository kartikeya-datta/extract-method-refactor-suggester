error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3947.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3947.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3947.java
text:
```scala
i@@f (keycode == Keys.SPACE) {

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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.tests.box2d.ApplyForce;
import com.badlogic.gdx.tests.box2d.BodyTypes;
import com.badlogic.gdx.tests.box2d.Box2DTest;
import com.badlogic.gdx.tests.box2d.Bridge;
import com.badlogic.gdx.tests.box2d.Cantilever;
import com.badlogic.gdx.tests.box2d.Chain;
import com.badlogic.gdx.tests.box2d.CharacterCollision;
import com.badlogic.gdx.tests.box2d.CollisionFiltering;
import com.badlogic.gdx.tests.box2d.ContinuousTest;
import com.badlogic.gdx.tests.box2d.DebugRendererTest;
import com.badlogic.gdx.tests.box2d.OneSidedPlatform;
import com.badlogic.gdx.tests.box2d.Prismatic;
import com.badlogic.gdx.tests.box2d.Pyramid;
import com.badlogic.gdx.tests.box2d.SimpleTest;
import com.badlogic.gdx.tests.box2d.SphereStack;
import com.badlogic.gdx.tests.box2d.VaryingRestitution;
import com.badlogic.gdx.tests.box2d.VerticalStack;
import com.badlogic.gdx.tests.utils.GdxTest;

public class Box2DTestCollection extends GdxTest implements InputProcessor {
	private final Box2DTest[] tests = {new DebugRendererTest(), new CollisionFiltering(), new Chain(), new Bridge(),
		new SphereStack(), new Cantilever(), new ApplyForce(), new ContinuousTest(), new Prismatic(), new CharacterCollision(),
		new BodyTypes(), new SimpleTest(), new Pyramid(), new OneSidedPlatform(), new VerticalStack(), new VaryingRestitution()};

	private int testIndex = 0;

	private Application app = null;

	@Override public void render () {
		tests[testIndex].render();
	}

	@Override public void create () {
		if (this.app == null) {
			this.app = Gdx.app;
			Box2DTest test = tests[testIndex];
			test.create();
		}

		Gdx.input.setInputProcessor(this);
	}

	@Override public boolean keyDown (int keycode) {
		if (keycode == Keys.KEYCODE_SPACE) {
			app.log("TestCollection", "disposing test '" + tests[testIndex].getClass().getName());
			tests[testIndex].dispose();
			testIndex++;
			if (testIndex >= tests.length) testIndex = 0;
			Box2DTest test = tests[testIndex];
			test.create();
			app.log("TestCollection", "created test '" + tests[testIndex].getClass().getName());
		} else {
			tests[testIndex].keyDown(keycode);
		}

		return false;
	}

	@Override public boolean keyTyped (char character) {
		tests[testIndex].keyTyped(character);
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		tests[testIndex].keyUp(keycode);
		return false;
	}

	@Override public boolean touchDown (int x, int y, int pointer, int button) {
		tests[testIndex].touchDown(x, y, pointer, button);
		return false;
	}

	@Override public boolean touchDragged (int x, int y, int pointer) {
		tests[testIndex].touchDragged(x, y, pointer);
		return false;
	}

	@Override public boolean touchUp (int x, int y, int pointer, int button) {
		tests[testIndex].touchUp(x, y, pointer, button);
		return false;
	}

	@Override public boolean needsGL20 () {
		return false;
	}

	@Override public boolean touchMoved (int x, int y) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3947.java