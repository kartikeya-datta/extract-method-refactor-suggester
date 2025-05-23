error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9917.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9917.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9917.java
text:
```scala
s@@prite.getTextureRegion().flip(false, true);

/*
 * Copyright (c) 2008-2010, Matthias Mann
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution. * Neither the name of Matthias Mann nor
 * the names of its contributors may be used to endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.badlogic.gdx.twl.renderer;

import com.badlogic.gdx.graphics.Sprite;
import com.badlogic.gdx.graphics.Texture;

import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.renderer.AnimationState;
import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.renderer.SupportsDrawRepeat;

/**
 * @author Nathan Sweet <misc@n4te.com>
 * @author Matthias Mann
 */
class GdxImage implements Image, SupportsDrawRepeat {
	private final GdxRenderer renderer;
	private final Sprite sprite;
	private final Color tintColor;
	private final int width;
	private final int height;
	private final boolean tile;

	public GdxImage (GdxRenderer renderer, Texture texture, int srcX, int srcY, int srcWidth, int srcHeight, Color color,
		boolean tile) {
		this.renderer = renderer;
		width = Math.abs(srcWidth);
		height = Math.abs(srcHeight);
		this.tintColor = color == null ? Color.WHITE : color;
		this.tile = tile;
		sprite = new Sprite(texture, srcX, srcY, srcWidth, srcHeight);
		sprite.flip(false, true);
	}

	public GdxImage (GdxImage image, Color tintColor) {
		renderer = image.renderer;
		width = image.width;
		height = image.height;
		sprite = image.sprite;
		this.tintColor = tintColor;
		tile = image.tile;
	}

	public void draw (AnimationState as, int x, int y) {
		sprite.setColor(tintColor.getRedFloat(), tintColor.getGreenFloat(), tintColor.getBlueFloat(), tintColor.getAlphaFloat());
		if (tile) {
			drawTiled(x, y, width, height);
		} else {
			sprite.setBounds(x, y, width, height);
			sprite.draw(renderer.spriteBatch);
		}
	}

	public void draw (AnimationState as, int x, int y, int width, int height) {
		sprite.setColor(tintColor.getRedFloat(), tintColor.getGreenFloat(), tintColor.getBlueFloat(), tintColor.getAlphaFloat());
		if (tile) {
			drawTiled(x, y, width, height);
		} else {
			sprite.setBounds(x, y, width, height);
			sprite.draw(renderer.spriteBatch);
		}
	}

	public void draw (AnimationState as, int x, int y, int width, int height, int repeatCountX, int repeatCountY) {
		sprite.setColor(tintColor.getRedFloat(), tintColor.getGreenFloat(), tintColor.getBlueFloat(), tintColor.getAlphaFloat());
		if (repeatCountX * this.width != width || repeatCountY * this.height != height)
			drawRepeatSlow(x, y, width, height, repeatCountX, repeatCountY);
		else
			drawRepeat(x, y, repeatCountX, repeatCountY);
	}

	private void drawRepeatSlow (int x, int y, int width, int height, int repeatCountX, int repeatCountY) {
		while (repeatCountY > 0) {
			int rowHeight = height / repeatCountY;

			int cx = 0;
			int xi = 0;
			while (xi < repeatCountX) {
				int nx = ++xi * width / repeatCountX;
				sprite.setBounds(x + cx, y, nx - cx, rowHeight);
				sprite.draw(renderer.spriteBatch);
				cx = nx;
			}

			y += rowHeight;
			height -= rowHeight;
			repeatCountY--;
		}
	}

	private void drawRepeat (int x, int y, int repeatCountX, int repeatCountY) {
		final int w = width;
		final int h = height;
		while (repeatCountY-- > 0) {
			int curX = x;
			int cntX = repeatCountX;
			while (cntX-- > 0) {
				sprite.setBounds(curX, y, w, h);
				sprite.draw(renderer.spriteBatch);
				curX += w;
			}
			y += h;
		}
	}

	private void drawTiled (int x, int y, int width, int height) {
		int repeatCountX = width / this.width;
		int repeatCountY = height / this.height;

		drawRepeat(x, y, repeatCountX, repeatCountY);

		int drawnX = repeatCountX * this.width;
		int drawnY = repeatCountY * this.height;
		int restWidth = width - drawnX;
		int restHeight = height - drawnY;
		if (restWidth > 0 || restHeight > 0) {
			if (restWidth > 0 && repeatCountY > 0) drawClipped(x + drawnX, y, restWidth, this.height, 1, repeatCountY);
			if (restHeight > 0) {
				if (repeatCountX > 0) drawClipped(x, y + drawnY, this.width, restHeight, repeatCountX, 1);
				if (restWidth > 0) drawClipped(x + drawnX, y + drawnY, restWidth, restHeight, 1, 1);
			}
		}
	}

	private void drawClipped (int x, int y, int width, int height, int repeatCountX, int repeatCountY) {
		while (repeatCountY-- > 0) {
			int y1 = y + height;
			int x0 = x;
			int cx = repeatCountX;
			while (cx-- > 0) {
				int x1 = x0 + width;
				sprite.setBounds(x0, y, x1, y1);
				sprite.draw(renderer.spriteBatch);
				x0 = x1;
			}
			y = y1;
		}
	}

	public Image createTintedVersion (Color color) {
		if (color == null) throw new IllegalArgumentException("color cannot be null.");
		color = tintColor.multiply(color);
		if (tintColor.equals(color)) return this;
		return new GdxImage(this, color);
	}

	public int getWidth () {
		return width;
	}

	public int getHeight () {
		return height;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9917.java