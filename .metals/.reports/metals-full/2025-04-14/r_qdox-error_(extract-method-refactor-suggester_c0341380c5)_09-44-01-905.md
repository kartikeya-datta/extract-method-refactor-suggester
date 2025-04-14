error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/933.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/933.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/933.java
text:
```scala
S@@tring[] lines = console.getItems().toArray();

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
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tests.utils.GdxTest;

public class GamepadTest extends GdxTest {
	String descriptor;
	Skin skin;
	Table ui;
	Stage stage;
	ScrollPane scrollPane;
	List<String> console;

	@Override
	public void create () {
		setupUi();

		// print the currently connected controllers to the console
		print("Controllers: " + Controllers.getControllers().size);
		int i = 0;
		for (Controller controller : Controllers.getControllers()) {
			print("#" + i++ + ": " + controller.getName());
		}
		if (Controllers.getControllers().size == 0) print("No controllers attached");

		// setup the listener that prints events to the console
		Controllers.addListener(new ControllerListener() {
			public int indexOf (Controller controller) {
				return Controllers.getControllers().indexOf(controller, true);
			}

			@Override
			public void connected (Controller controller) {
				print("connected " + controller.getName());
				int i = 0;
				for (Controller c : Controllers.getControllers()) {
					print("#" + i++ + ": " + c.getName());
				}
			}

			@Override
			public void disconnected (Controller controller) {
				print("disconnected " + controller.getName());
				int i = 0;
				for (Controller c : Controllers.getControllers()) {
					print("#" + i++ + ": " + c.getName());
				}
				if (Controllers.getControllers().size == 0) print("No controllers attached");
			}

			@Override
			public boolean buttonDown (Controller controller, int buttonIndex) {
				print("#" + indexOf(controller) + ", button " + buttonIndex + " down");
				return false;
			}

			@Override
			public boolean buttonUp (Controller controller, int buttonIndex) {
				print("#" + indexOf(controller) + ", button " + buttonIndex + " up");
				return false;
			}

			@Override
			public boolean axisMoved (Controller controller, int axisIndex, float value) {
				print("#" + indexOf(controller) + ", axis " + axisIndex + ": " + value);
				return false;
			}

			@Override
			public boolean povMoved (Controller controller, int povIndex, PovDirection value) {
				print("#" + indexOf(controller) + ", pov " + povIndex + ": " + value);
				return false;
			}

			@Override
			public boolean xSliderMoved (Controller controller, int sliderIndex, boolean value) {
				print("#" + indexOf(controller) + ", x slider " + sliderIndex + ": " + value);
				return false;
			}

			@Override
			public boolean ySliderMoved (Controller controller, int sliderIndex, boolean value) {
				print("#" + indexOf(controller) + ", y slider " + sliderIndex + ": " + value);
				return false;
			}

			@Override
			public boolean accelerometerMoved (Controller controller, int accelerometerIndex, Vector3 value) {
				// not printing this as we get to many values
				return false;
			}
		});
	}

	void print (String message) {
		String[] lines = console.getItems().toArray(String.class);
		String[] newLines = new String[lines.length + 1];
		System.arraycopy(lines, 0, newLines, 0, lines.length);
		newLines[newLines.length - 1] = message;
		console.setItems(newLines);
		scrollPane.invalidate();
		scrollPane.validate();
		scrollPane.setScrollPercentY(1.0f);
	}

	void clear () {
		console.setItems(new String[0]);
	}

	private void setupUi () {
		// setup a tiny ui with a console and a clear button.
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();
		ui = new Table();
		ui.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		console = new List(skin);
		scrollPane = new ScrollPane(console);
		scrollPane.setScrollbarsOnTop(true);
		TextButton clear = new TextButton("Clear", skin);
		ui.add(scrollPane).expand(true, true).fill();
		ui.row();
		ui.add(clear).expand(true, false).fill();
		stage.addActor(ui);
		clear.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				clear();
			}
		});
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize (int width, int height) {
		ui.setSize(width, height);
		ui.invalidate();
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/933.java