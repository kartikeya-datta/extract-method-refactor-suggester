error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7180.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7180.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7180.java
text:
```scala
s@@tage.getViewport().update(width, height, true);

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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.Array;

/** Test switch of scroll bars + knobs from right to left, and bottom to top */
public class ScrollPaneScrollBarsTest extends GdxTest {
	private Stage stage;
	Array<ScrollPane> scrollPanes = new Array<ScrollPane>();
	boolean doFade = true;
	boolean doOnTop = true;
	private Table bottomLeft, bottomRight, topLeft, topRight, horizOnlyTop, horizOnlyBottom, vertOnlyLeft, vertOnlyRight;

	public void create () {
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		float btnWidth = 200;
		float btnHeight = 40;
		stage = new Stage();
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		Gdx.input.setInputProcessor(stage);

		final TextButton fadeBtn = new TextButton("Fade: " + doFade, skin);
		fadeBtn.setSize(btnWidth, btnHeight);
		fadeBtn.setPosition(0, height - fadeBtn.getHeight());
		stage.addActor(fadeBtn);
		fadeBtn.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				doFade = !doFade;
				fadeBtn.setText("Fade: " + doFade);
				for (ScrollPane pane : scrollPanes) {
					pane.setFadeScrollBars(doFade);
				}
			}
		});

		final TextButton onTopBtn = new TextButton("ScrollbarsOnTop: " + doOnTop, skin);
		onTopBtn.setSize(btnWidth, btnHeight);
		onTopBtn.setPosition(0 + fadeBtn.getWidth() + 20, height - onTopBtn.getHeight());
		stage.addActor(onTopBtn);
		onTopBtn.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				doOnTop = !doOnTop;
				onTopBtn.setText("ScrollbarOnTop: " + doOnTop);
				onTopBtn.invalidate();
				for (ScrollPane pane : scrollPanes) {
					pane.setScrollbarsOnTop(doOnTop);
				}
			}
		});

		// Gdx.graphics.setVSync(false);

		float gap = 8;
		float x = gap;
		float y = gap;
		float contWidth = width / 2 - gap * 1.5f;
		float contHeight = height / 4.5f - gap * 1.25f;

		bottomLeft = new Table();
		bottomLeft.setPosition(x, y);
		bottomLeft.setSize(contWidth, contHeight);
		stage.addActor(bottomLeft);

		bottomRight = new Table();
		bottomRight.setSize(contWidth, contHeight);
		x = bottomLeft.getX() + bottomLeft.getWidth() + gap;
		bottomRight.setPosition(x, y);
		stage.addActor(bottomRight);

		topLeft = new Table();
		topLeft.setSize(contWidth, contHeight);
		x = bottomLeft.getX();
		y = bottomLeft.getY() + bottomLeft.getHeight() + gap;
		topLeft.setPosition(x, y);
		stage.addActor(topLeft);

		topRight = new Table();
		topRight.setSize(contWidth, contHeight);
		x = bottomRight.getX();
		y = topLeft.getY();
		topRight.setPosition(x, y);
		stage.addActor(topRight);

		horizOnlyTop = new Table();
		horizOnlyTop.setSize(contWidth, contHeight);
		x = topRight.getX();
		y = topRight.getY() + topRight.getHeight() + gap;
		horizOnlyTop.setPosition(x, y);
		stage.addActor(horizOnlyTop);

		horizOnlyBottom = new Table();
		horizOnlyBottom.setSize(contWidth, contHeight);
		x = topLeft.getX();
		y = topLeft.getY() + topLeft.getHeight() + gap;
		horizOnlyBottom.setPosition(x, y);
		stage.addActor(horizOnlyBottom);

		vertOnlyLeft = new Table();
		vertOnlyLeft.setSize(contWidth, contHeight);
		x = horizOnlyBottom.getX();
		y = horizOnlyBottom.getY() + horizOnlyBottom.getHeight() + gap;
		vertOnlyLeft.setPosition(x, y);
		stage.addActor(vertOnlyLeft);

		vertOnlyRight = new Table();
		vertOnlyRight.setSize(contWidth, contHeight);
		x = horizOnlyTop.getX();
		y = horizOnlyTop.getY() + horizOnlyTop.getHeight() + gap;
		vertOnlyRight.setPosition(x, y);
		stage.addActor(vertOnlyRight);

		Table bottomLeftTable = new Table();
		Table bottomRightTable = new Table();
		Table topLeftTable = new Table();
		Table topRightTable = new Table();
		Table horizOnlyTopTable = new Table();
		Table horizOnlyBottomTable = new Table();
		Table vertOnlyLeftTable = new Table();
		Table vertOnlyRightTable = new Table();

		final ScrollPane bottomLeftScroll = new ScrollPane(bottomLeftTable, skin);
		bottomLeftScroll.setScrollBarPositions(true, false);

		final ScrollPane bottomRightScroll = new ScrollPane(bottomRightTable, skin);
		bottomRightScroll.setScrollBarPositions(true, true);

		final ScrollPane topLeftScroll = new ScrollPane(topLeftTable, skin);
		topLeftScroll.setScrollBarPositions(false, false);

		final ScrollPane topRightScroll = new ScrollPane(topRightTable, skin);
		topRightScroll.setScrollBarPositions(false, true);

		final ScrollPane horizOnlyTopScroll = new ScrollPane(horizOnlyTopTable, skin);
		horizOnlyTopScroll.setScrollBarPositions(false, true);

		final ScrollPane horizOnlyBottomScroll = new ScrollPane(horizOnlyBottomTable, skin);
		horizOnlyBottomScroll.setScrollBarPositions(true, true);

		final ScrollPane vertOnlyLeftScroll = new ScrollPane(vertOnlyLeftTable, skin);
		vertOnlyLeftScroll.setScrollBarPositions(true, false);

		final ScrollPane vertOnlyRightScroll = new ScrollPane(vertOnlyRightTable, skin);
		vertOnlyRightScroll.setScrollBarPositions(true, true);

		ScrollPane[] panes = new ScrollPane[] {bottomLeftScroll, bottomRightScroll, topLeftScroll, topRightScroll,
			horizOnlyTopScroll, horizOnlyBottomScroll, vertOnlyLeftScroll, vertOnlyRightScroll};
		for (ScrollPane pane : panes) {
			scrollPanes.add(pane);
		}

		Table[] tables = new Table[] {bottomLeftTable, bottomRightTable, topLeftTable, topRightTable, horizOnlyTopTable,
			horizOnlyBottomTable, vertOnlyLeftTable, vertOnlyRightTable};
		for (Table t : tables) {
			t.pad(10).defaults().expandX().space(4);
		}

		horizOnlyTopTable
			.add(new Label("HORIZONTAL-ONLY-TOP verify HORIZONTAL scroll bar is on the TOP and properly aligned", skin));
		horizOnlyBottomTable.add(new Label(
			"HORIZONTAL-ONLY-BOTTOM verify HORIZONTAL scroll bar is on the BOTTOM and properly aligned", skin));

		for (int i = 0; i < 12; i++) {
			bottomLeftTable.row();
			bottomRightTable.row();
			topLeftTable.row();
			topRightTable.row();

			bottomLeftTable.add(new Label(i
				+ " BOTTOM-LEFT verify scroll bars are on the BOTTOM and the LEFT, and are properly aligned", skin));
			bottomRightTable.add(new Label(i
				+ " BOTTOM-RIGHT verify scroll bars are on the BOTTOM and the RIGHT, and are properly aligned", skin));
			topLeftTable.add(new Label(i + " TOP-LEFT verify scroll bars are on the TOP and the LEFT, and are properly aligned",
				skin));
			topRightTable.add(new Label(i + " TOP-RIGHT verify scroll bars are on the TOP and the RIGHT, and are properly aligned",
				skin));

			vertOnlyLeftTable.row();
			vertOnlyRightTable.row();

			vertOnlyLeftTable.add(new Label("VERT-ONLY-LEFT", skin));
			vertOnlyRightTable.add(new Label("VERT-ONLY-RIGHT", skin));
		}

		bottomLeft.add(bottomLeftScroll).expand().fill().colspan(4);
		bottomRight.add(bottomRightScroll).expand().fill().colspan(4);
		topLeft.add(topLeftScroll).expand().fill().colspan(4);
		topRight.add(topRightScroll).expand().fill().colspan(4);
		horizOnlyTop.add(horizOnlyTopScroll).expand().fill().colspan(4);
		horizOnlyBottom.add(horizOnlyBottomScroll).expand().fill().colspan(4);
		vertOnlyLeft.add(vertOnlyLeftScroll).expand().fill().colspan(4);
		vertOnlyRight.add(vertOnlyRightScroll).expand().fill().colspan(4);
	}

	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		Table.drawDebug(stage);
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height);
		// Gdx.gl.glViewport(100, 100, width - 200, height - 200);
		// stage.setViewport(800, 600, false, 100, 100, width - 200, height - 200);
	}

	public void dispose () {
		stage.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7180.java