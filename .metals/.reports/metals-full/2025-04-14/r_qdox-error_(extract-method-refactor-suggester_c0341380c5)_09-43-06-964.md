error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/28.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/28.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/28.java
text:
```scala
s@@tage = new Stage();

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

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.tests.utils.GdxTest;

public class ActionTest extends GdxTest implements Runnable {
	Stage stage;
	Texture texture;

	@Override
	public void create () {
		stage = new Stage(480, 320, true);
		texture = new Texture(Gdx.files.internal("data/badlogic.jpg"), false);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		final Image img = new Image(new TextureRegion(texture));
		img.setSize(100, 100);
		img.setOrigin(50, 50);
		img.setPosition(100, 100);

		img.addAction(forever(sequence(delay(1.0f), new Action() {
			public boolean act (float delta) {
				System.out.println(1);
				img.clearActions();
				return true;
			}
		})));

		// img.action(Forever.$(Sequence.$(ScaleTo.$(1.1f,
		// 1.1f,0.3f),ScaleTo.$(1f, 1f, 0.3f))));
		// img.action(Forever.$(Parallel.$(RotateTo.$(1, 1))));
		// img.action(Delay.$(RotateBy.$(45, 2),
		// 1).setCompletionListener(this));
// // Action actionMoveBy = MoveBy.$(30, 0, 0.5f).setCompletionListener(
// // new OnActionCompleted() {
// //
// // @Override
// // public void completed(Action action) {
// // System.out.println("move by complete");
// // }
// // });
// //
// // Action actionDelay = Delay.$(actionMoveBy, 1).setCompletionListener(
// // new OnActionCompleted() {
// //
// // @Override
// // public void completed(Action action) {
// // System.out.println("delay complete");
// // }
// // });
// //
// // img.action(actionDelay);
//
// // img.action(Repeat.$(Sequence.$(MoveBy.$(50, 0, 1), MoveBy.$(0, 50, 1), MoveBy.$(-50, 0, 1), MoveBy.$(0, -50, 1)), 3));
// // img.action(Sequence.$(FadeOut.$(1),
// // FadeIn.$(1),
// // Delay.$(MoveTo.$(100, 100, 1), 2),
// // ScaleTo.$(0.5f, 0.5f, 1),
// // FadeOut.$(0.5f),
// // Delay.$(Parallel.$( RotateTo.$(360, 1),
// // FadeIn.$(1),
// // ScaleTo.$(1, 1, 1)), 1)));
// // OnActionCompleted listener = new OnActionCompleted() {
// // @Override public void completed (Action action) {
// // img.action(Parallel.$(Sequence.$(FadeOut.$(2), FadeIn.$(2)),
// // Sequence.$(ScaleTo.$(0.1f, 0.1f, 1.5f), ScaleTo.$(1.0f, 1.0f, 1.5f))).setCompletionListener(this));
// // }
// // };
// //
// // img.action(Parallel.$(Sequence.$(FadeOut.$(2), FadeIn.$(2)),
// // Sequence.$(ScaleTo.$(0.1f, 0.1f, 1.5f), ScaleTo.$(1.0f, 1.0f, 1.5f))).setCompletionListener(listener));
//
// // img.action(
// // Sequence.$(
// // Parallel.$(RotateBy.$(180, 2), ScaleTo.$(1.4f, 1.4f, 2), FadeTo.$(0.7f, 2)),
// // Parallel.$(RotateBy.$(180, 2), ScaleTo.$(1.0f, 1.0f, 2), FadeTo.$(1.0f, 2)),
// // Remove.$()
// // )
// // );
// //
// // Action action = Repeat.$(Sequence.$(
// // MoveBy.$(8, 0, 0.5f),
// // MoveBy.$(0, 8, 0.5f),
// // MoveBy.$(-8, 0, 0.5f),
// // MoveBy.$(0, -8, 0.5f)), 20);
// // Action action2 = action.copy();
// // img.action(action2);
//
// // float scale = 1;
// // float showDuration = 1;
// // ScaleTo scaleCountdown = ScaleTo.$(scale * 1.0f, scale * 1.0f, 1.0f);
// // scaleCountdown.setInterpolator(DecelerateInterpolator.$(3.0f));
// // Parallel parallel = Parallel.$(scaleCountdown);
// // // Sequence.$(FadeIn.$(0.25f), Delay.$(FadeOut.$(0.25f), 0.5f)));
// // Sequence cdAnim = Sequence.$(Delay.$(parallel, showDuration), Remove.$());
// // cdAnim.setCompletionListener(this);
// // img.action(cdAnim);
// //
// // Delay delay = Delay.$(MoveBy.$(100, 100, 1).setCompletionListener(this), 1);
// // delay.setCompletionListener(this);
// // img.action(Sequence.$(delay).setCompletionListener(this));

		stage.addActor(img);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void run () {
		System.out.println("completed action");
	}

	@Override
	public void dispose () {
		stage.dispose();
		texture.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/28.java