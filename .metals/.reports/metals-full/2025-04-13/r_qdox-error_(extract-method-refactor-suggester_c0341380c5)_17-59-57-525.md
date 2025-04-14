error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/802.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/802.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/802.java
text:
```scala
"g3d/head.g3db",@@ "g3d/knight.g3dj", "g3d/knight.g3db", "g3d/ship.obj", "g3d/teapot.g3db"

package com.badlogic.gdx.tests.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.StringBuilder;

public abstract class BaseG3dHudTest extends BaseG3dTest {
	protected Stage hud;
	protected Skin skin;
	protected Label fpsLabel;
	protected CollapsableWindow modelsWindow;
	protected final StringBuilder stringBuilder = new StringBuilder();

	protected  String models[] = new String[] {
		"car.obj", "cube.obj", "scene.obj", "scene2.obj", "sphere.obj", "wheel.obj", 
		"g3d/head.g3db", "g3d/knight.g3dj", "g3d/knight.g3db", "g3d/teapot.g3db"
	};

	@Override
	public void create () {
		super.create();

		createHUD();

		Gdx.input.setInputProcessor(new InputMultiplexer(this, hud, inputController));
	}
	
	private void createHUD() {
		hud = new Stage();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		modelsWindow = new CollapsableWindow("Models", skin);
		final List list = new List(models, skin);
		list.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if (!modelsWindow.isCollapsed() && getTapCount() == 2) {
					onModelClicked(list.getSelection());
					modelsWindow.collapse();
				}
			}
		});
		modelsWindow.row();
		modelsWindow.add(list);
		modelsWindow.pack();
		modelsWindow.pack();
		modelsWindow.setY(Gdx.graphics.getHeight()-modelsWindow.getHeight());
		modelsWindow.collapse();
		hud.addActor(modelsWindow);
		fpsLabel = new Label("FPS: 999", skin);
		hud.addActor(fpsLabel);
	}

	protected abstract void onModelClicked(final String name);
	
	protected void getStatus(final StringBuilder stringBuilder) {
		stringBuilder.append("FPS: ").append(Gdx.graphics.getFramesPerSecond());
		if (loading)
			stringBuilder.append(" loading...");
	}
	
	@Override
	public void render () {
		super.render();
		
		stringBuilder.setLength(0);
		getStatus(stringBuilder);
		fpsLabel.setText(stringBuilder);
		hud.act(Gdx.graphics.getDeltaTime());
		hud.draw();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		skin.dispose();
		skin = null;
	}
	
	/** Double click title to expand/collapse */
	public static class CollapsableWindow extends Window {
		private boolean collapsed;
		private float collapseHeight = 20f;
		private float expandHeight;
		public CollapsableWindow (String title, Skin skin) {
			super(title, skin);
			addListener(new ClickListener() {
				@Override
				public void clicked (InputEvent event, float x, float y) {
					if (getTapCount() == 2 && getHeight() - y <= getPadTop() && y < getHeight() && x > 0 && x < getWidth())
						toggleCollapsed();
				}
			});
		}
		public void expand() {
			if (!collapsed) return;
			setHeight(expandHeight);
			setY(getY()-expandHeight+collapseHeight);
			collapsed = false;
		}
		public void collapse() {
			if (collapsed) return;
			expandHeight = getHeight();
			setHeight(collapseHeight);
			setY(getY()+expandHeight-collapseHeight);
			collapsed = true;
		}
		public void toggleCollapsed() {
			if (collapsed)
				expand();
			else
				collapse();
		}
		public boolean isCollapsed() {
			return collapsed;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/802.java