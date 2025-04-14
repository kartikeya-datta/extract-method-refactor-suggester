error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6688.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6688.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6688.java
text:
```scala
f@@loat height = this.height * scaleY;


package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Image extends Widget {
	protected TextureRegion region;
	protected NinePatch patch;
	protected final Scaling scaling;
	protected int align = Align.CENTER;
	protected float imageX, imageY, imageWidth, imageHeight;

	public Image (TextureRegion region) {
		this(region, Scaling.none, null);
	}

	public Image (TextureRegion region, Scaling scaling) {
		this(region, scaling, null);
	}

	public Image (TextureRegion region, Scaling scaling, int align) {
		this(region, scaling, align, null);
	}

	public Image (TextureRegion region, Scaling scaling, String name) {
		this(region, scaling, Align.CENTER, null);
	}

	public Image (TextureRegion region, Scaling scaling, int align, String name) {
		setRegion(region);
		this.scaling = scaling;
		this.align = align;
		pack();
	}

	public Image (NinePatch patch) {
		this(patch, Scaling.none, null);
	}

	public Image (NinePatch patch, Scaling scaling) {
		this(patch, scaling, null);
	}

	public Image (NinePatch patch, Scaling scaling, int align) {
		this(patch, scaling, align, null);
	}

	public Image (NinePatch patch, Scaling scaling, String name) {
		this(patch, scaling, Align.CENTER, null);
	}

	public Image (NinePatch patch, Scaling scaling, int align, String name) {
		setPatch(patch);
		this.scaling = scaling;
		this.align = align;
		pack();
	}

	public void layout () {
		if (!invalidated) return;
		invalidated = false;

		float regionWidth, regionHeight;
		if (patch != null) {
			regionWidth = patch.getTotalWidth();
			regionHeight = patch.getTotalHeight();
		} else if (region != null) {
			regionWidth = region.getRegionWidth();
			regionHeight = region.getRegionHeight();
		} else
			return;

		float width = this.width * scaleX;
		float height = this.width * scaleY;

		switch (scaling) {
		case fill: {
			float widgetRatio = height / width;
			float regionRatio = regionHeight / regionWidth;
			float scale = regionRatio > widgetRatio ? width / regionWidth : height / regionHeight;
			imageWidth = regionWidth * scale;
			imageHeight = regionHeight * scale;
			break;
		}
		case fit: {
			float widgetRatio = height / width;
			float regionRatio = regionHeight / regionWidth;
			float scale = regionRatio < widgetRatio ? width / regionWidth : height / regionHeight;
			imageWidth = regionWidth * scale;
			imageHeight = regionHeight * scale;
			break;
		}
		case stretch:
			imageWidth = width;
			imageHeight = height;
			break;
		case stretchX:
			imageWidth = width;
			imageHeight = regionHeight;
			break;
		case stretchY:
			imageWidth = regionWidth;
			imageHeight = height;
			break;
		case none:
			imageWidth = regionWidth;
			imageHeight = regionHeight;
			break;
		}

		if ((align & Align.LEFT) != 0)
			imageX = 0;
		else if ((align & Align.RIGHT) != 0)
			imageX = (int)(width - imageWidth);
		else
			imageX = (int)(width / 2 - imageWidth / 2);

		if ((align & Align.TOP) != 0)
			imageY = (int)(height - imageHeight);
		else if ((align & Align.BOTTOM) != 0)
			imageY = 0;
		else
			imageY = (int)(height / 2 - imageHeight / 2);
	}

	public void draw (SpriteBatch batch, float parentAlpha) {
		if (invalidated) layout();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (patch != null)
			patch.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		else if (region != null) {
			if (scaleX == 1 && scaleY == 1 && rotation == 0)
				batch.draw(region, x + imageX, y + imageY, imageWidth, imageHeight);
			else
				batch.draw(region, x + imageX, y + imageY, originX, originY, imageWidth, imageHeight, scaleX, scaleY, rotation);
		}
	}

	/** @param region May be null. */
	public void setRegion (TextureRegion region) {
		if (region != null) {
			if (getPrefWidth() != region.getRegionWidth() || getPrefHeight() != region.getRegionHeight()) invalidateHierarchy();
		} else {
			if (getPrefWidth() != 0 || getPrefHeight() != 0) invalidateHierarchy();
		}
		this.region = region;
		patch = null;
	}

	/** @param patch May be null. */
	public void setPatch (NinePatch patch) {
		if (patch != null) {
			if (getPrefWidth() != patch.getTotalWidth() || getPrefHeight() != patch.getTotalHeight()) invalidateHierarchy();
		} else {
			if (getPrefWidth() != 0 || getPrefHeight() != 0) invalidateHierarchy();
		}
		this.patch = patch;
		region = null;
	}

	public float getMinWidth () {
		return 0;
	}

	public float getMinHeight () {
		return 0;
	}

	public float getPrefWidth () {
		if (region != null) return region.getRegionWidth();
		if (patch != null) return patch.getTotalWidth();
		return 0;
	}

	public float getPrefHeight () {
		if (region != null) return region.getRegionHeight();
		if (patch != null) return patch.getTotalHeight();
		return 0;
	}

	public boolean touchDown (float x, float y, int pointer) {
		return false;
	}

	public void touchUp (float x, float y, int pointer) {
	}

	public void touchDragged (float x, float y, int pointer) {
	}

	static public enum Scaling {
		fill, fit, stretch, stretchX, stretchY, none
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6688.java