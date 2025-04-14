error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7738.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7738.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7738.java
text:
```scala
V@@ector2 size = scaling.apply(regionWidth, regionHeight, width, height);


package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;

/** Displays a {@link TextureRegion} or {@link NinePatch}, scaled various way within the widgets bounds. The preferred size is the
 * actual size of the region or ninepatch. Only when using a TextureRegion will the actor's scale, rotation, and origin be used
 * when drawing.
 * @author Nathan Sweet */
public class Image extends Widget {
	private TextureRegion region;
	private NinePatch patch;
	private Scaling scaling;
	private int align = Align.CENTER;
	private float imageX, imageY, imageWidth, imageHeight;
	private ClickListener clickListener;

	/** Creates an image with no region or patch, stretched, and aligned center. */
	public Image () {
		this((TextureRegion)null);
	}

	/** Creates an image stretched, and aligned center. */
	public Image (Texture texture) {
		this(new TextureRegion(texture));
	}

	/** Creates an image aligned center. */
	public Image (Texture texture, Scaling scaling) {
		this(new TextureRegion(texture), scaling);
	}

	public Image (Texture texture, Scaling scaling, int align) {
		this(new TextureRegion(texture), scaling, align);
	}

	public Image (Texture texture, Scaling scaling, int align, String name) {
		this(new TextureRegion(texture), scaling, align, name);
	}

	/** Creates an image stretched, and aligned center.
	 * @param region May be null. */
	public Image (TextureRegion region) {
		this(region, Scaling.stretch, Align.CENTER, null);
	}

	/** Creates an image aligned center.
	 * @param region May be null. */
	public Image (TextureRegion region, Scaling scaling) {
		this(region, scaling, Align.CENTER, null);
	}

	/** @param region May be null. */
	public Image (TextureRegion region, Scaling scaling, int align) {
		this(region, scaling, align, null);
	}

	/** @param region May be null. */
	public Image (TextureRegion region, Scaling scaling, int align, String name) {
		super(name);
		setRegion(region);
		this.scaling = scaling;
		this.align = align;
		pack();
	}

	/** Creates an image stretched, and aligned center.
	 * @param patch May be null. */
	public Image (NinePatch patch) {
		this(patch, Scaling.stretch, Align.CENTER, null);
	}

	/** Creates an image aligned center.
	 * @param patch May be null. */
	public Image (NinePatch patch, Scaling scaling) {
		this(patch, scaling, Align.CENTER, null);
	}

	/** @param patch May be null. */
	public Image (NinePatch patch, Scaling scaling, int align) {
		this(patch, scaling, align, null);
	}

	/** @param patch May be null. */
	public Image (NinePatch patch, Scaling scaling, int align, String name) {
		super(name);
		setPatch(patch);
		this.scaling = scaling;
		this.align = align;
		pack();
	}

	public void layout () {
		float regionWidth, regionHeight;
		if (patch != null) {
			regionWidth = patch.getTotalWidth();
			regionHeight = patch.getTotalHeight();
		} else if (region != null) {
			regionWidth = region.getRegionWidth();
			regionHeight = region.getRegionHeight();
		} else
			return;

		Vector2 size = scaling.apply(regionWidth, regionHeight, width * scaleX, height * scaleY);
		imageWidth = size.x;
		imageHeight = size.y;

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
		validate();

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (patch != null)
			patch.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		else if (region != null) {
			if (scaleX == 1 && scaleY == 1 && rotation == 0)
				batch.draw(region, x + imageX, y + imageY, imageWidth, imageHeight);
			else
				batch.draw(region, x + imageX, y + imageY, originX - imageX, originY - imageY, imageWidth, imageHeight, scaleX,
					scaleY, rotation);
		}
	}

	/** @param region May be null. */
	public void setRegion (TextureRegion region) {
		if (region != null) {
			if (this.region == region) return;
			if (getPrefWidth() != region.getRegionWidth() || getPrefHeight() != region.getRegionHeight()) invalidateHierarchy();
		} else {
			if (getPrefWidth() != 0 || getPrefHeight() != 0) invalidateHierarchy();
		}
		this.region = region;
		patch = null;
	}

	public TextureRegion getRegion () {
		return region;
	}

	/** @param patch May be null. */
	public void setPatch (NinePatch patch) {
		if (patch != null) {
			if (this.patch == patch) return;
			if (getPrefWidth() != patch.getTotalWidth() || getPrefHeight() != patch.getTotalHeight()) invalidateHierarchy();
		} else {
			if (getPrefWidth() != 0 || getPrefHeight() != 0) invalidateHierarchy();
		}
		this.patch = patch;
		region = null;
	}

	public NinePatch getPatch () {
		return patch;
	}

	public void setScaling (Scaling scaling) {
		if (scaling == null) throw new IllegalArgumentException("scaling cannot be null.");
		this.scaling = scaling;
	}

	public void setAlign (int align) {
		this.align = align;
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
		return clickListener != null;
	}

	public void touchUp (float x, float y, int pointer) {
		if (hit(x, y) == null) return;
		if (clickListener != null) clickListener.click(this, x, y);
	}

	public void touchDragged (float x, float y, int pointer) {
	}

	public void setClickListener (ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public float getImageX () {
		return imageX;
	}

	public float getImageY () {
		return imageY;
	}

	public float getImageWidth () {
		return imageWidth;
	}

	public float getImageHeight () {
		return imageHeight;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7738.java