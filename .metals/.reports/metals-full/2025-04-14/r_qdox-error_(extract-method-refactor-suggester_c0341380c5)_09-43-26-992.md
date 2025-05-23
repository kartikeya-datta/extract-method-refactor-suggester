error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7697.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7697.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7697.java
text:
```scala
s@@kin = new Skin(Gdx.files.internal("data/uiskin.json"));

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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;

public class InterpolationTest extends GdxTest {

	private Stage stage;
	private Skin skin;
	private Table table;
	private List list;
	private String interpolationNames[], selectedInterpolation;
	private ShapeRenderer renderer;

	private float graphSize = 400, steps = graphSize / 2, time = 0, duration = 2.5f;

	private Vector2 startPosition = new Vector2(), targetPosition = new Vector2(), position = new Vector2();

	/** resets {@link #startPosition} and {@link #targetPosition} */
	private void resetPositions() {
		startPosition.set(stage.getWidth() - stage.getWidth() / 5f, stage.getHeight() - stage.getHeight() / 5f);
		targetPosition.set(startPosition.x, stage.getHeight() / 5f);
	}

	/** @return the {@link #position} with the {@link #selectedInterpolation interpolation} applied */
	private Vector2 getPosition(float time) {
		position.set(targetPosition);
		position.sub(startPosition);
		position.scl(getInterpolation(selectedInterpolation).apply(time / duration));
		position.add(startPosition);
		return position;
	}

	/** @return the {@link #selectedInterpolation selected} interpolation */
	private Interpolation getInterpolation(String name) {
		try {
			return (Interpolation) Interpolation.class.getField(name).get(null);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void create() {
		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		renderer = new ShapeRenderer();

		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage();
		resetPositions();

		table = new Table();
		table.setFillParent(true);

		Field[] interpolationFields = ClassReflection.getFields(Interpolation.class);

		// see how many fields are actually interpolations (for safety; other fields may be added with future)
		int interpolationMembers = 0;
		for(int i = 0; i < interpolationFields.length; i++)
			if(Interpolation.class.isAssignableFrom(interpolationFields[i].getDeclaringClass()))
				interpolationMembers++;

		// get interpolation names
		interpolationNames = new String[interpolationMembers];
		for(int i = 0; i < interpolationFields.length; i++)
			if(Interpolation.class.isAssignableFrom(interpolationFields[i].getDeclaringClass()))
				interpolationNames[i] = interpolationFields[i].getName();
		selectedInterpolation = interpolationNames[0];

		list = new List(interpolationNames, skin);
		list.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selectedInterpolation = list.getSelection();
				time = 0;
				resetPositions();
			}

		});

		table.add(new ScrollPane(list)).expand().fillY().left();
		stage.addActor(table);

		Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {

			@Override
			public boolean scrolled(int amount) {
				if(!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
					return false;
				duration -= amount / 15f;
				duration = MathUtils.clamp(duration, 0, Float.POSITIVE_INFINITY);
				return true;
			}

		}, stage, new InputAdapter() {

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if(!Float.isNaN(time)) // if "walking" was interrupted by this touch down event
					startPosition.set(getPosition(time)); // set startPosition to the current position
				targetPosition.set(stage.screenToStageCoordinates(targetPosition.set(screenX, screenY)));
				time = 0;
				return true;
			}

		}));
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float bottomLeftX = Gdx.graphics.getWidth() / 2 - graphSize / 2, bottomLeftY = Gdx.graphics.getHeight() / 2 - graphSize / 2;

		// only show up to two decimals
		String text = String.valueOf(duration);
		if(text.length() > 4)
			text = text.substring(0, text.lastIndexOf('.') + 3);
		text = "duration: " + text + " s (ctrl + scroll to change)";
		stage.getSpriteBatch().begin();
		list.getStyle().font.draw(stage.getSpriteBatch(), text, bottomLeftX + graphSize / 2 - list.getStyle().font.getBounds(text).width / 2, bottomLeftY + graphSize + list.getStyle().font.getLineHeight());
		stage.getSpriteBatch().end();

		renderer.begin(ShapeType.Line);
		renderer.rect(bottomLeftX, bottomLeftY, graphSize, graphSize); // graph bounds
		float lastX = bottomLeftX, lastY = bottomLeftY;
		for(float step = 0; step <= steps; step++) {
			Interpolation interpolation = getInterpolation(selectedInterpolation);
			float percent = step / steps;
			float x = bottomLeftX + graphSize * percent, y = bottomLeftY + graphSize * interpolation.apply(percent);
			renderer.line(lastX, lastY, x, y);
			lastX = x;
			lastY = y;
		}
		time += Gdx.graphics.getDeltaTime();
		if(time > duration) {
			time = Float.NaN; // stop "walking"
			startPosition.set(targetPosition); // set startPosition to targetPosition for next click
		}
		// draw time marker
		renderer.line(bottomLeftX + graphSize * time / duration, bottomLeftY, bottomLeftX + graphSize * time / duration, bottomLeftY + graphSize);
		// draw path
		renderer.setColor(Color.GRAY);
		renderer.line(startPosition, targetPosition);
		renderer.setColor(Color.WHITE);
		renderer.end();

		// draw the position
		renderer.begin(ShapeType.Filled);
		if(!Float.isNaN(time)) // don't mess up position if time is NaN
			getPosition(time);
		renderer.circle(position.x, position.y, 7);
		renderer.end();

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height);
		table.invalidateHierarchy();

		stage.getCamera().update();
		renderer.setProjectionMatrix(stage.getCamera().combined);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public boolean needsGL20() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7697.java