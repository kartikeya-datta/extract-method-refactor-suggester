error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2005.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2005.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2005.java
text:
```scala
r@@eturn getTopHeight() + getBottomHeight() + patches[MIDDLE_CENTER].getRegionHeight();

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
package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class NinePatch {
	public static final int TOP_LEFT = 0;
	public static final int TOP_CENTER = 1;
	public static final int TOP_RIGHT = 2;
	public static final int MIDDLE_LEFT = 3;
	public static final int MIDDLE_CENTER = 4;
	public static final int MIDDLE_RIGHT = 5;
	public static final int BOTTOM_LEFT = 6;
	public static final int BOTTOM_CENTER = 7;
	public static final int BOTTOM_RIGHT = 8;

	TextureRegion[] patches;

	private NinePatch () {
	}
	
	public NinePatch (Texture texture, int left, int right, int top, int bottom) {
		this(new TextureRegion(texture), left, right, top, bottom);
	}

	public NinePatch (TextureRegion region, int left, int right, int top, int bottom) {
		int middleWidth = region.getRegionWidth() - left - right;
		int middleHeight = region.getRegionHeight() - top - bottom;
		this.patches = new TextureRegion[] {new TextureRegion(region, 0, 0, left, top),
			new TextureRegion(region, left, 0, middleWidth, top), new TextureRegion(region, left + middleWidth, 0, right, top),
			new TextureRegion(region, 0, top, left, middleHeight), new TextureRegion(region, left, top, middleWidth, middleHeight),
			new TextureRegion(region, left + middleWidth, top, right, middleHeight),
			new TextureRegion(region, 0, top + middleHeight, left, bottom),
			new TextureRegion(region, left, top + middleHeight, middleWidth, bottom),
			new TextureRegion(region, left + middleWidth, top + middleHeight, right, bottom),};
	}

	public NinePatch (TextureRegion... patches) {
		if (patches.length != 9) throw new IllegalArgumentException("NinePatch needs nine TextureRegions");
		this.patches = patches;
		checkValidity();
	}

	private void checkValidity () {
		if (patches[BOTTOM_LEFT].getRegionWidth() != patches[TOP_LEFT].getRegionWidth()
 patches[BOTTOM_LEFT].getRegionWidth() != patches[MIDDLE_LEFT].getRegionWidth()) {
			throw new GdxRuntimeException("Left side patches must have the same width");
		}

		if (patches[BOTTOM_RIGHT].getRegionWidth() != patches[TOP_RIGHT].getRegionWidth()
 patches[BOTTOM_RIGHT].getRegionWidth() != patches[MIDDLE_RIGHT].getRegionWidth()) {
			throw new GdxRuntimeException("Right side patches must have the same width");
		}

		if (patches[BOTTOM_LEFT].getRegionHeight() != patches[BOTTOM_CENTER].getRegionHeight()
 patches[BOTTOM_LEFT].getRegionHeight() != patches[BOTTOM_RIGHT].getRegionHeight()) {
			throw new GdxRuntimeException("Bottom patches must have the same height");
		}

		if (patches[TOP_LEFT].getRegionHeight() != patches[TOP_CENTER].getRegionHeight()
 patches[TOP_LEFT].getRegionHeight() != patches[TOP_RIGHT].getRegionHeight()) {
			throw new GdxRuntimeException("Top patches must have the same height");
		}
	}

	public void draw (SpriteBatch batch, float x, float y, float width, float height) {
		float widthTopBottom = width - (patches[TOP_LEFT].getRegionWidth() + patches[TOP_RIGHT].getRegionWidth());
		float heightLeftRight = height - (patches[TOP_LEFT].getRegionHeight() + patches[BOTTOM_LEFT].getRegionHeight());
		float widthCenter = widthTopBottom;

		// bottom patches
		batch.draw(patches[BOTTOM_LEFT], x, y, patches[BOTTOM_LEFT].getRegionWidth(), patches[BOTTOM_LEFT].getRegionHeight());
		batch.draw(patches[BOTTOM_CENTER], x + patches[BOTTOM_LEFT].getRegionWidth(), y, widthCenter,
			patches[BOTTOM_CENTER].getRegionHeight());
		batch.draw(patches[BOTTOM_RIGHT], x + patches[BOTTOM_LEFT].getRegionWidth() + widthTopBottom, y,
			patches[BOTTOM_RIGHT].getRegionWidth(), patches[BOTTOM_RIGHT].getRegionHeight());

		y += patches[BOTTOM_LEFT].getRegionHeight();
		// center patches
		batch.draw(patches[MIDDLE_LEFT], x, y, patches[MIDDLE_LEFT].getRegionWidth(), heightLeftRight);
		batch.draw(patches[MIDDLE_CENTER], x + patches[MIDDLE_LEFT].getRegionWidth(), y, widthCenter, heightLeftRight);
		batch.draw(patches[MIDDLE_RIGHT], x + patches[MIDDLE_LEFT].getRegionWidth() + widthTopBottom, y,
			patches[MIDDLE_RIGHT].getRegionWidth(), heightLeftRight);

		// top patches
		y += heightLeftRight;
		batch.draw(patches[TOP_LEFT], x, y, patches[TOP_LEFT].getRegionWidth(), patches[TOP_LEFT].getRegionHeight());
		batch.draw(patches[TOP_CENTER], x + patches[TOP_LEFT].getRegionWidth(), y, widthCenter,
			patches[TOP_CENTER].getRegionHeight());
		batch.draw(patches[TOP_RIGHT], x + patches[TOP_LEFT].getRegionWidth() + widthTopBottom, y,
			patches[TOP_RIGHT].getRegionWidth(), patches[TOP_RIGHT].getRegionHeight());
	}

	public float getLeftWidth () {
		return patches[TOP_LEFT].getRegionWidth();
	}

	public float getRightWidth () {
		return patches[TOP_RIGHT].getRegionWidth();
	}

	public float getTopHeight () {
		return patches[TOP_RIGHT].getRegionHeight();
	}

	public float getBottomHeight () {
		return patches[BOTTOM_RIGHT].getRegionHeight();
	}

	public float getTotalHeight () {
		return getTopHeight() + getBottomHeight() + patches[MIDDLE_LEFT].getRegionHeight();
	}

	public float getTotalWidth () {
		return getLeftWidth() + getRightWidth() + patches[MIDDLE_CENTER].getRegionWidth();
	}

	public TextureRegion[] getPatches () {
		return patches;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2005.java