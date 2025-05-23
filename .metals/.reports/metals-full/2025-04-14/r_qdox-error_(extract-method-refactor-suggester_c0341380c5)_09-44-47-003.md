error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3629.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3629.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3629.java
text:
```scala
t@@his(font, font.usesIntegerPositions());

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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.NumberUtils;

/** Caches glyph geometry for a BitmapFont, providing a fast way to render static text. This saves needing to compute the location
 * of each glyph each frame.
 * @author Nathan Sweet
 * @author Matthias Mann */
public class BitmapFontCache implements Disposable {
	private final BitmapFont font;
	private float[] vertices = new float[0];
	private int idx;
	private float x, y;
	private float color = Color.WHITE.toFloatBits();
	private final Color tmpColor = new Color(Color.WHITE);
	private final TextBounds textBounds = new TextBounds();
	private boolean integer = true;

	public BitmapFontCache (BitmapFont font) {
		this(font, true);
	}

	/** Creates a new BitmapFontCache
	 * @param font the font to use
	 * @param integer whether to use integer positions and sizes. */
	public BitmapFontCache (BitmapFont font, boolean integer) {
		this.font = font;
		this.integer = integer;
	}

	/** Sets the position of the text, relative to the position when the cached text was created.
	 * @param x The x coordinate
	 * @param y The y coodinate */
	public void setPosition (float x, float y) {
		translate(x - this.x, y - this.y);
	}

	/** Sets the position of the text, relative to its current position.
	 * @param xAmount The amount in x to move the text
	 * @param yAmount The amount in y to move the text */
	public void translate (float xAmount, float yAmount) {
		if (xAmount == 0 && yAmount == 0) return;
		if (integer) {
			xAmount = (int)xAmount;
			yAmount = (int)yAmount;
		}
		x += xAmount;
		y += yAmount;
		float[] vertices = this.vertices;
		for (int i = 0, n = idx; i < n; i += 5) {
			vertices[i] += xAmount;
			vertices[i + 1] += yAmount;
		}
	}

	public void setColor (float color) {
		if (color == this.color) return;
		this.color = color;
		float[] vertices = this.vertices;
		for (int i = 2, n = idx; i < n; i += 5)
			vertices[i] = color;
	}

	public void setColor (Color tint) {
		final float color = tint.toFloatBits();
		if (color == this.color) return;
		this.color = color;
		float[] vertices = this.vertices;
		for (int i = 2, n = idx; i < n; i += 5)
			vertices[i] = color;
	}

	public void setColor (float r, float g, float b, float a) {
		int intBits = ((int)(255 * a) << 24) | ((int)(255 * b) << 16) | ((int)(255 * g) << 8) | ((int)(255 * r));
		float color = NumberUtils.intToFloatColor(intBits);
		if (color == this.color) return;
		this.color = color;
		float[] vertices = this.vertices;
		for (int i = 2, n = idx; i < n; i += 5)
			vertices[i] = color;
	}

	public void draw (SpriteBatch spriteBatch) {
		spriteBatch.draw(font.getRegion().getTexture(), vertices, 0, idx);
	}

	public void draw (SpriteBatch spriteBatch, float alphaModulation) {
		if (alphaModulation == 1) {
			draw(spriteBatch);
			return;
		}
		Color color = getColor();
		float oldAlpha = color.a;
		color.a *= alphaModulation;
		setColor(color);
		draw(spriteBatch);
		color.a = oldAlpha;
		setColor(color);
	}

	public Color getColor () {
		float floatBits = color;
		int intBits = NumberUtils.floatToIntColor(color);
		Color color = tmpColor;
		color.r = (intBits & 0xff) / 255f;
		color.g = ((intBits >>> 8) & 0xff) / 255f;
		color.b = ((intBits >>> 16) & 0xff) / 255f;
		color.a = ((intBits >>> 24) & 0xff) / 255f;
		return color;
	}

	private void reset (int glyphCount) {
		x = 0;
		y = 0;
		idx = 0;

		int vertexCount = glyphCount * 20;
		if (vertices == null || vertices.length < vertexCount) vertices = new float[vertexCount];
	}

	private float addToCache (CharSequence str, float x, float y, int start, int end) {
		float startX = x;
		BitmapFont font = this.font;
		Glyph lastGlyph = null;
		if (font.data.scaleX == 1 && font.data.scaleY == 1) {
			while (start < end) {
				lastGlyph = font.data.getGlyph(str.charAt(start++));
				if (lastGlyph != null) {
					addGlyph(lastGlyph, x + lastGlyph.xoffset, y + lastGlyph.yoffset, lastGlyph.width, lastGlyph.height);
					x += lastGlyph.xadvance;
					break;
				}
			}
			while (start < end) {
				char ch = str.charAt(start++);
				Glyph g = font.data.getGlyph(ch);
				if (g != null) {
					x += lastGlyph.getKerning(ch);
					lastGlyph = g;
					addGlyph(lastGlyph, x + g.xoffset, y + g.yoffset, g.width, g.height);
					x += g.xadvance;
				}
			}
		} else {
			float scaleX = font.data.scaleX, scaleY = font.data.scaleY;
			while (start < end) {
				lastGlyph = font.data.getGlyph(str.charAt(start++));
				if (lastGlyph != null) {
					addGlyph(lastGlyph, //
						x + lastGlyph.xoffset * scaleX, //
						y + lastGlyph.yoffset * scaleY, //
						lastGlyph.width * scaleX, //
						lastGlyph.height * scaleY);
					x += lastGlyph.xadvance * scaleX;
					break;
				}
			}
			while (start < end) {
				char ch = str.charAt(start++);
				Glyph g = font.data.getGlyph(ch);
				if (g != null) {
					x += lastGlyph.getKerning(ch) * scaleX;
					lastGlyph = g;
					addGlyph(lastGlyph, //
						x + g.xoffset * scaleX, //
						y + g.yoffset * scaleY, //
						g.width * scaleX, //
						g.height * scaleY);
					x += g.xadvance * scaleX;
				}
			}
		}
		return x - startX;
	}

	private void addGlyph (Glyph glyph, float x, float y, float width, float height) {
		float x2 = x + width;
		float y2 = y + height;
		final float u = glyph.u;
		final float u2 = glyph.u2;
		final float v = glyph.v;
		final float v2 = glyph.v2;

		final float[] vertices = this.vertices;

		if (integer) {
			x = (int)x;
			y = (int)y;
			x2 = (int)x2;
			y2 = (int)y2;
		}

		vertices[idx++] = x;
		vertices[idx++] = y;
		vertices[idx++] = color;
		vertices[idx++] = u;
		vertices[idx++] = v;

		vertices[idx++] = x;
		vertices[idx++] = y2;
		vertices[idx++] = color;
		vertices[idx++] = u;
		vertices[idx++] = v2;

		vertices[idx++] = x2;
		vertices[idx++] = y2;
		vertices[idx++] = color;
		vertices[idx++] = u2;
		vertices[idx++] = v2;

		vertices[idx++] = x2;
		vertices[idx++] = y;
		vertices[idx++] = color;
		vertices[idx++] = u2;
		vertices[idx++] = v;
	}

	/** Caches a string with the specified position.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link BitmapFont#getCapHeight() cap height}).
	 * @return The bounds of the cached string (the height is the distance from y to the baseline). */
	public TextBounds setText (CharSequence str, float x, float y) {
		return setText(str, x, y, 0, str.length());
	}

	/** Caches a substring with the specified position.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link BitmapFont#getCapHeight() cap height}).
	 * @param start The first character of the string to draw.
	 * @param end The last character of the string to draw (exclusive).
	 * @return The bounds of the cached string (the height is the distance from y to the baseline). */
	public TextBounds setText (CharSequence str, float x, float y, int start, int end) {
		reset(end - start);
		y += font.data.ascent;
		textBounds.width = addToCache(str, x, y, start, end);
		textBounds.height = font.data.capHeight;
		return textBounds;
	}

	/** Caches a string, which may contain newlines (\n), with the specified position.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link BitmapFont#getCapHeight() cap height}).
	 * @return The bounds of the cached string (the height is the distance from y to the baseline of the last line). */
	public TextBounds setMultiLineText (CharSequence str, float x, float y) {
		return setMultiLineText(str, x, y, 0, HAlignment.LEFT);
	}

	/** Caches a string, which may contain newlines (\n), with the specified position and alignment. Each line is aligned
	 * horizontally within a rectangle of the specified width.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link BitmapFont#getCapHeight() cap height}).
	 * @return The bounds of the cached string (the height is the distance from y to the baseline of the last line). */
	public TextBounds setMultiLineText (CharSequence str, float x, float y, float alignmentWidth, HAlignment alignment) {
		BitmapFont font = this.font;

		int length = str.length();
		reset(length);

		y += font.data.ascent;
		float down = font.data.down;

		float maxWidth = 0;
		float startY = y;
		int start = 0;
		int numLines = 0;
		while (start < length) {
			int lineEnd = BitmapFont.indexOf(str, '\n', start);
			float xOffset = 0;
			if (alignment != HAlignment.LEFT) {
				float lineWidth = font.getBounds(str, start, lineEnd).width;
				xOffset = alignmentWidth - lineWidth;
				if (alignment == HAlignment.CENTER) xOffset /= 2;
			}
			float lineWidth = addToCache(str, x + xOffset, y, start, lineEnd);
			maxWidth = Math.max(maxWidth, lineWidth);
			start = lineEnd + 1;
			y += down;
			numLines++;
		}
		textBounds.width = maxWidth;
		textBounds.height = font.data.capHeight + (numLines - 1) * font.data.lineHeight;
		return textBounds;
	}

	/** Caches a string, which may contain newlines (\n), with the specified position. Each line is automatically wrapped to keep it
	 * within a rectangle of the specified width.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link BitmapFont#getCapHeight() cap height}).
	 * @return The bounds of the cached string (the height is the distance from y to the baseline of the last line). */
	public TextBounds setWrappedText (CharSequence str, float x, float y, float wrapWidth) {
		return setWrappedText(str, x, y, wrapWidth, HAlignment.LEFT);
	}

	/** Caches a string, which may contain newlines (\n), with the specified position. Each line is automatically wrapped to keep it
	 * within a rectangle of the specified width, and aligned horizontally within that rectangle.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link BitmapFont#getCapHeight() cap height}).
	 * @return The bounds of the cached string (the height is the distance from y to the baseline of the last line). */
	public TextBounds setWrappedText (CharSequence str, float x, float y, float wrapWidth, HAlignment alignment) {
		BitmapFont font = this.font;

		int length = str.length();
		reset(length);

		y += font.data.ascent;
		float down = font.data.down;

		if (wrapWidth <= 0) wrapWidth = Integer.MAX_VALUE;
		float maxWidth = 0;
		int start = 0;
		int numLines = 0;
		while (start < length) {
			int newLine = BitmapFont.indexOf(str, '\n', start);
			// Eat whitespace at start of line.
			while (start < newLine) {
				if (!BitmapFont.isWhitespace(str.charAt(start))) break;
				start++;
			}
			int lineEnd = start + font.computeVisibleGlyphs(str, start, newLine, wrapWidth);
			int nextStart = lineEnd + 1;
			if (lineEnd < newLine) {
				// Find char to break on.
				while (lineEnd > start) {
					if (BitmapFont.isWhitespace(str.charAt(lineEnd))) break;
					lineEnd--;
				}
				if (lineEnd == start)
					lineEnd = nextStart - 1; // If no characters to break, show all.
				else {
					nextStart = lineEnd;
					// Eat whitespace at end of line.
					while (lineEnd > start) {
						if (!BitmapFont.isWhitespace(str.charAt(lineEnd - 1))) break;
						lineEnd--;
					}
				}
			}
			if (lineEnd > start) {
				float xOffset = 0;
				if (alignment != HAlignment.LEFT) {
					float lineWidth = font.getBounds(str, start, lineEnd).width;
					xOffset = wrapWidth - lineWidth;
					if (alignment == HAlignment.CENTER) xOffset /= 2;
				}
				float lineWidth = addToCache(str, x + xOffset, y, start, lineEnd);
				maxWidth = Math.max(maxWidth, lineWidth);
			}
			start = nextStart;
			y += down;
			numLines++;
		}
		textBounds.width = maxWidth;
		textBounds.height = font.data.capHeight + (numLines - 1) * font.data.lineHeight;
		return textBounds;
	}

	/** Returns the size of the cached string. The height is the distance from the top of most capital letters in the font (the
	 * {@link BitmapFont#getCapHeight() cap height}) to the baseline of the last line of text. */
	public TextBounds getBounds () {
		return textBounds;
	}

	/** Returns the x position of the cached string, relative to the position when the string was cached. */
	public float getX () {
		return x;
	}

	/** Returns the y position of the cached string, relative to the position when the string was cached. */
	public float getY () {
		return y;
	}

	public BitmapFont getFont () {
		return font;
	}

	/** Disposes the underlying BitmapFont of this cache. */
	public void dispose () {
		font.dispose();
	}

	/** Specifies whether to use integer positions or not. Default is to use them so filtering doesn't kick in as badly.
	 * @param use */
	public void setUseIntegerPositions (boolean use) {
		this.integer = use;
	}

	/** @return whether this font uses integer positions for drawing. */
	public boolean usesIntegerPositions () {
		return integer;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3629.java