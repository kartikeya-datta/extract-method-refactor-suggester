error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5264.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5264.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5264.java
text:
```scala
u@@i.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30.0f));

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.Repeat;
import com.badlogic.gdx.scenes.scene2d.actions.RotateBy;
import com.badlogic.gdx.scenes.scene2d.actions.RotateTo;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.LinearGroup;
import com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener;
import com.badlogic.gdx.scenes.scene2d.actors.LinearGroup.LinearGroupLayout;
import com.badlogic.gdx.tests.utils.GdxTest;

public class UITest extends GdxTest implements InputProcessor {
	Texture uiTexture;
	Texture badlogic;
	Stage ui;

	// Font font;

	@Override public void create () {
// Gdx.input.setCatchBackKey(true);

		// font = Gdx.graphics.newFont( "Droid Sans", 20, FontStyle.Plain );

		uiTexture = new Texture(Gdx.files.internal("data/ui.png"));

		badlogic = new Texture(Gdx.files.internal("data/badlogic.jpg"));

		ui = new Stage(480, 320, false);
		TextureRegion blendRegion = new TextureRegion(uiTexture, 0, 0, 64, 32);
		TextureRegion blendDownRegion = new TextureRegion(uiTexture, -1, -1, 64, 32);
		TextureRegion rotateRegion = new TextureRegion(uiTexture, 64, 0, 64, 32);
		TextureRegion rotateDownRegion = new TextureRegion(uiTexture, 63, -1, 64, 32);
		TextureRegion scaleRegion = new TextureRegion(uiTexture, 64, 32, 64, 32);
		TextureRegion scaleDownRegion = new TextureRegion(uiTexture, 63, 31, 64, 32);
		TextureRegion buttonRegion = new TextureRegion(uiTexture, 0, 64, 64, 32);
		TextureRegion buttonDownRegion = new TextureRegion(uiTexture, -1, 63, 64, 32);

		Image img1 = new Image("image1", new TextureRegion(badlogic, 0, 0, 256, 256));
		img1.width = img1.height = 64;
		img1.originX = img1.originY = 32;
		img1.action(Sequence.$(FadeOut.$(1), FadeIn.$(1), Delay.$(MoveTo.$(100, 100, 1), 2), ScaleTo.$(0.5f, 0.5f, 1),
			FadeOut.$(0.5f), Delay.$(Parallel.$(RotateTo.$(360, 1), FadeIn.$(1), ScaleTo.$(1, 1, 1)), 1)));
		ui.addActor(img1);

		Image img2 = new Image("image2", new TextureRegion(badlogic, 0, 0, 256, 256));
		img2.width = img2.height = 64;
		img2.originX = img2.originY = 32;
		img2.action(Repeat.$(Sequence.$(MoveBy.$(50, 0, 1), MoveBy.$(0, 50, 1), MoveBy.$(-50, 0, 1), MoveBy.$(0, -50, 1)), 3));
		ui.addActor(img2);

		Button button = new Button("button", buttonRegion, buttonDownRegion);
		button.action(Parallel.$(Sequence.$(FadeOut.$(2), FadeIn.$(2)),
			Sequence.$(ScaleTo.$(0.1f, 0.1f, 1.5f), ScaleTo.$(1.0f, 1.0f, 1.5f))));
		button.clickListener = new ClickListener() {

			@Override public void clicked (Button button) {
				if (Gdx.input.supportsOnscreenKeyboard()) Gdx.input.setOnscreenKeyboardVisible(true);
			}
		};
		ui.addActor(button);

		// Label label = new Label( "label", font, "text input: " );
		// label.x = 10; label.y = Gdx.graphics.getHeight() - 20;
		// ui.addActor(label);

		LinearGroup linear = new LinearGroup("linear", 64, 32 * 3, LinearGroupLayout.Vertical);
		linear.x = 200;
		linear.y = 150;
		linear.scaleX = linear.scaleY = 0;
		linear.addActor(new Button("blend", blendRegion, blendDownRegion));
		linear.addActor(new Button("scale", scaleRegion, scaleDownRegion));
		linear.addActor(new Button("rotate", rotateRegion, rotateDownRegion));
		linear.action(Parallel.$(ScaleTo.$(1, 1, 2), RotateTo.$(720, 2)));
		ui.addActor(linear);

		LinearGroup linearh = new LinearGroup("linearh", 64 * 3, 32, LinearGroupLayout.Horizontal);
		linearh.x = 500;
		linearh.y = 10;
		linearh.addActor(new Button("blendh", blendRegion, blendDownRegion));
		linearh.addActor(new Button("scaleh", scaleRegion, scaleDownRegion));
		linearh.addActor(new Button("rotateh", rotateRegion, rotateDownRegion));
		linearh.action(MoveTo.$(100, 10, 1.5f));
		ui.addActor(linearh);

		// Group.enableDebugging( "data/debug.png" );
		Gdx.input.setInputProcessor(this);
	}

	@Override public void render () {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		ui.act(Gdx.graphics.getDeltaTime());
		ui.draw();
	}

	@Override public boolean keyDown (int keycode) {
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		// Label label = ((Label)ui.findActor( "label" ));
		// if( character == '\b' ) {
		// if( label.text.length() > "text input: ".length() )
		// label.text = label.text.substring( 0, label.text.length()-1 );
		// } else {
		// label.text += character;
		// }
		return false;
	}

	@Override public boolean touchDown (int x, int y, int pointer, int button) {
		ui.touchDown(x, y, pointer, button);
		return false;
	}

	Vector2 point = new Vector2();

	@Override public boolean touchUp (int x, int y, int pointer, int button) {
		if (!ui.touchUp(x, y, pointer, button)) {
			Actor actor = ui.findActor("image1");
			if (actor != null) {
				ui.toStageCoordinates(x, y, point);
				actor.clearActions();
				actor.action(MoveTo.$(point.x, point.y, 2));
				actor.action(RotateBy.$(90, 2));
				if (actor.scaleX == 1.0f)
					actor.action(ScaleTo.$(0.5f, 0.5f, 2));
				else
					actor.action(ScaleTo.$(1f, 1f, 2));
			}
		}
		return false;
	}

	@Override public boolean touchDragged (int x, int y, int pointer) {
		ui.touchDragged(x, y, pointer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5264.java