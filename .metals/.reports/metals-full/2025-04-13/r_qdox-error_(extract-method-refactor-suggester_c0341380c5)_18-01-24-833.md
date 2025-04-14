error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6223.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6223.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6223.java
text:
```scala
i@@f (scaleX == 1 && scaleY == 1 && rotation == 0)

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
package com.badlogic.gdx.scenes.scene2d.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Layout;

/**
 * A simple Button {@link Actor}, useful for simple UIs
 * 
 * @author mzechner
 * 
 */
public class Button extends Actor implements Layout {
	public interface ClickListener {
		public void clicked (Button button);
	}

	public TextureRegion pressedRegion;
	public TextureRegion unpressedRegion;
	public ClickListener clickListener;
	public boolean pressed = false;
	protected int pointer = -1;

	/**
	 * Creates a new Button instance with the given name.
	 * @param name the name
	 */
	public Button (String name) {
		super(name);
		this.pressedRegion = new TextureRegion();
		this.unpressedRegion = new TextureRegion();
	}

	/**
	 * Creates a new Button instance with the given name, using the complete supplied texture for displaying the pressed and
	 * unpressed state of the button.
	 * @param name the name
	 * @param texture the {@link Texture}
	 */
	public Button (String name, Texture texture) {
		super(name);
		originX = texture.getWidth() / 2.0f;
		originY = texture.getHeight() / 2.0f;
		width = texture.getWidth();
		height = texture.getHeight();
		pressedRegion = new TextureRegion(texture);
		unpressedRegion = new TextureRegion(texture);
	}

	public Button (String name, TextureRegion region) {
		this(name, region, region);
	}

	public Button (String name, TextureRegion unpressedRegion, TextureRegion pressedRegion) {
		super(name);
		width = Math.abs(unpressedRegion.getRegionWidth());
		height = Math.abs(unpressedRegion.getRegionHeight());
		originX = width / 2.0f;
		originY = height / 2.0f;
		this.unpressedRegion = new TextureRegion(unpressedRegion);
		this.pressedRegion = new TextureRegion(pressedRegion);
	}

	@Override protected void draw (SpriteBatch batch, float parentAlpha) {
		TextureRegion region = pressed ? pressedRegion : unpressedRegion;
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (region.getTexture() != null) {
			if (scaleX == 0 && scaleY == 0 && rotation == 0)
				batch.draw(region, x, y, width, height);
			else
				batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
		}
	}

	@Override protected boolean touchDown (float x, float y, int pointer) {
		if(pressed) return false;

		boolean result = x > 0 && y > 0 && x < width && y < height;
		pressed = result;
		
		if (pressed) {
			parent.focus(this, pointer);
			this.pointer = pointer;
		}
		return result;
	}

	@Override protected boolean touchUp (float x, float y, int pointer) {
		if (!pressed) return false;

		if(pointer == this.pointer) {
			parent.focus(null, pointer);
		}
		pressed = false;
		if (clickListener != null) clickListener.clicked(this);
		return true;
	}

	@Override protected boolean touchDragged (float x, float y, int pointer) {
		return pressed;
	}

	public Actor hit (float x, float y) {
		return x > 0 && y > 0 && x < width && y < height ? this : null;
	}

	public void layout () {
	}

	public void invalidate () {
	}

	public float getPrefWidth () {
		return unpressedRegion.getRegionWidth() * scaleX;
	}

	public float getPrefHeight () {
		return unpressedRegion.getRegionHeight() * scaleY;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6223.java