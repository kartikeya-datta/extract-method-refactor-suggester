error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/987.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/987.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/987.java
text:
```scala
i@@f (capGlyph == null) capGlyph = getFirstGlyph();

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

package com.badlogic.gdx.graphics.g2d;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;

/** Renders bitmap fonts. The font consists of 2 files: an image file or {@link TextureRegion} containing the glyphs and a file in
 * the AngleCode BMFont text format that describes where each glyph is on the image. Currently only a single image of glyphs is
 * supported.<br>
 * <br>
 * Text is drawn using a {@link SpriteBatch}. Text can be cached in a {@link BitmapFontCache} for faster rendering of static text,
 * which saves needing to compute the location of each glyph each frame.<br>
 * <br>
 * * The texture for a BitmapFont loaded from a file is managed. {@link #dispose()} must be called to free the texture when no
 * longer needed. A BitmapFont loaded using a {@link TextureRegion} is managed if the region's texture is managed. Disposing the
 * BitmapFont disposes the region's texture, which may not be desirable if the texture is still being used elsewhere.<br>
 * <br>
 * The code is based on Matthias Mann's TWL BitmapFont class. Thanks for sharing, Matthias! :)
 * @author Nathan Sweet
 * @author Matthias Mann */
public class BitmapFont implements Disposable {
	static private final int LOG2_PAGE_SIZE = 9;
	static private final int PAGE_SIZE = 1 << LOG2_PAGE_SIZE;
	static private final int PAGES = 0x10000 / PAGE_SIZE;

	static final char[] xChars = {'x', 'e', 'a', 'o', 'n', 's', 'r', 'c', 'u', 'm', 'v', 'w', 'z'};
	static final char[] capChars = {'M', 'N', 'B', 'D', 'C', 'E', 'F', 'K', 'A', 'G', 'H', 'I', 'J', 'L', 'O', 'P', 'Q', 'R', 'S',
		'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	TextureRegion region;
	private final TextBounds textBounds = new TextBounds();
	private float color = Color.WHITE.toFloatBits();
	private Color tempColor = new Color(1, 1, 1, 1);
	private boolean flipped;
	private boolean integer = true;
	final BitmapFontData data;

	public static class BitmapFontData {
		String imagePath;
		final FileHandle fontFile;
		final boolean flipped;
		final float lineHeight;
		float capHeight = 1;
		float ascent;
		float descent;
		float down;
		float scaleX = 1, scaleY = 1;

		final Glyph[][] glyphs = new Glyph[PAGES][];
		float spaceWidth;
		float xHeight = 1;

		public BitmapFontData (FileHandle fontFile, boolean flip) {
			this.fontFile = fontFile;
			this.flipped = flip;
			BufferedReader reader = new BufferedReader(new InputStreamReader(fontFile.read()), 512);
			try {
				reader.readLine(); // info

				String line = reader.readLine();
				if (line == null) throw new GdxRuntimeException("Invalid font file: " + fontFile);
				String[] common = line.split(" ", 4);
				if (common.length < 4) throw new GdxRuntimeException("Invalid font file: " + fontFile);

				if (!common[1].startsWith("lineHeight=")) throw new GdxRuntimeException("Invalid font file: " + fontFile);
				lineHeight = Integer.parseInt(common[1].substring(11));

				if (!common[2].startsWith("base=")) throw new GdxRuntimeException("Invalid font file: " + fontFile);
				int baseLine = Integer.parseInt(common[2].substring(5));

				line = reader.readLine();
				if (line == null) throw new GdxRuntimeException("Invalid font file: " + fontFile);
				String[] pageLine = line.split(" ", 4);
				if (!pageLine[2].startsWith("file=")) throw new GdxRuntimeException("Invalid font file: " + fontFile);
				String imgFilename = null;
				if (pageLine[2].endsWith("\"")) {
					imgFilename = pageLine[2].substring(6, pageLine[2].length() - 1);
				} else {
					imgFilename = pageLine[2].substring(5, pageLine[2].length());
				}
				imagePath = fontFile.parent().child(imgFilename).path().replaceAll("\\\\", "/");
				descent = 0;

				while (true) {
					line = reader.readLine();
					if (line == null) break;
					if (line.startsWith("kernings ")) break;
					if (!line.startsWith("char ")) continue;

					Glyph glyph = new Glyph();

					StringTokenizer tokens = new StringTokenizer(line, " =");
					tokens.nextToken();
					tokens.nextToken();
					int ch = Integer.parseInt(tokens.nextToken());
					if (ch <= Character.MAX_VALUE)
						setGlyph(ch, glyph);
					else
						continue;
					tokens.nextToken();
					glyph.srcX = Integer.parseInt(tokens.nextToken());
					tokens.nextToken();
					glyph.srcY = Integer.parseInt(tokens.nextToken());
					tokens.nextToken();
					glyph.width = Integer.parseInt(tokens.nextToken());
					tokens.nextToken();
					glyph.height = Integer.parseInt(tokens.nextToken());
					tokens.nextToken();
					glyph.xoffset = Integer.parseInt(tokens.nextToken());
					tokens.nextToken();
					if (flip)
						glyph.yoffset = Integer.parseInt(tokens.nextToken());
					else
						glyph.yoffset = -(glyph.height + Integer.parseInt(tokens.nextToken()));
					tokens.nextToken();
					glyph.xadvance = Integer.parseInt(tokens.nextToken());
					descent = Math.min(baseLine + glyph.yoffset, descent);
				}

				while (true) {
					line = reader.readLine();
					if (line == null) break;
					if (!line.startsWith("kerning ")) break;

					StringTokenizer tokens = new StringTokenizer(line, " =");
					tokens.nextToken();
					tokens.nextToken();
					int first = Integer.parseInt(tokens.nextToken());
					tokens.nextToken();
					int second = Integer.parseInt(tokens.nextToken());
					if (first < 0 || first > Character.MAX_VALUE || second < 0 || second > Character.MAX_VALUE) continue;
					Glyph glyph = getGlyph((char)first);
					tokens.nextToken();
					int amount = Integer.parseInt(tokens.nextToken());
					glyph.setKerning(second, amount);
				}

				Glyph spaceGlyph = getGlyph(' ');
				if (spaceGlyph == null) {
					spaceGlyph = new Glyph();
					Glyph xadvanceGlyph = getGlyph('l');
					if (xadvanceGlyph == null) xadvanceGlyph = getFirstGlyph();
					spaceGlyph.xadvance = xadvanceGlyph.xadvance;
					setGlyph(' ', spaceGlyph);
				}
				spaceWidth = spaceGlyph != null ? spaceGlyph.xadvance + spaceGlyph.width : 1;

				Glyph xGlyph = null;
				for (int i = 0; i < xChars.length; i++) {
					xGlyph = getGlyph(xChars[i]);
					if (xGlyph != null) break;
				}
				if (xGlyph == null) xGlyph = getFirstGlyph();
				xHeight = xGlyph.height;

				Glyph capGlyph = null;
				for (int i = 0; i < capChars.length; i++) {
					capGlyph = getGlyph(capChars[i]);
					if (capGlyph != null) break;
				}
				if (xGlyph == null) xGlyph = getFirstGlyph();
				capHeight = capGlyph.height;

				ascent = baseLine - capHeight;
				down = -lineHeight;
				if (flip) {
					ascent = -ascent;
					down = -down;
				}
			} catch (Exception ex) {
				throw new GdxRuntimeException("Error loading font file: " + fontFile, ex);
			} finally {
				try {
					reader.close();
				} catch (IOException ignored) {
				}
			}
		}

		private void setGlyph (int ch, Glyph glyph) {
			Glyph[] page = glyphs[ch / PAGE_SIZE];
			if (page == null) glyphs[ch / PAGE_SIZE] = page = new Glyph[PAGE_SIZE];
			page[ch & PAGE_SIZE - 1] = glyph;
		}

		private Glyph getFirstGlyph () {
			for (Glyph[] page : this.glyphs) {
				if (page == null) continue;
				for (Glyph glyph : page) {
					if (glyph == null) continue;
					return glyph;
				}
			}
			throw new GdxRuntimeException("No glyphs found!");
		}

		public Glyph getGlyph (char ch) {
			Glyph[] page = glyphs[ch / PAGE_SIZE];
			if (page != null) return page[ch & PAGE_SIZE - 1];
			return null;
		}

		public String getImagePath () {
			return imagePath;
		}

		public FileHandle getFontFile () {
			return fontFile;
		}
	}

	/** Creates a BitmapFont using the default 15pt Arial font included in the libgdx JAR file. This is convenient to easily display
	 * text without bothering with generating a bitmap font. */
	public BitmapFont () {
		this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"),
			Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), false, true);
	}

	/** Creates a BitmapFont using the default 15pt Arial font included in the libgdx JAR file. This is convenient to easily display
	 * text without bothering with generating a bitmap font.
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public BitmapFont (boolean flip) {
		this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"),
			Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), flip, true);
	}

	/** Creates a BitmapFont with the glyphs relative to the specified region.
	 * @param region The texture region containing the glyphs. The glyphs must be relative to the lower left corner (ie, the region
	 *           should not be flipped).
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public BitmapFont (FileHandle fontFile, TextureRegion region, boolean flip) {
		this(new BitmapFontData(fontFile, flip), region, true);
	}

	/** Creates a BitmapFont from a BMFont file. The image file name is read from the BMFont file and the image is loaded from the
	 * same directory.
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public BitmapFont (FileHandle fontFile, boolean flip) {
		this(new BitmapFontData(fontFile, flip), null, true);
	}

	/** Creates a BitmapFont from a BMFont file, using the specified image for glyphs. Any image specified in the BMFont file is
	 * ignored.
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public BitmapFont (FileHandle fontFile, FileHandle imageFile, boolean flip) {
		this(fontFile, imageFile, flip, true);
	}

	/** Creates a BitmapFont from a BMFont file, using the specified image for glyphs. Any image specified in the BMFont file is
	 * ignored.
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner.
	 * @param integer If true, rendering positions will be at integer values to avoid filtering artifacts.s */
	public BitmapFont (FileHandle fontFile, FileHandle imageFile, boolean flip, boolean integer) {
		this(new BitmapFontData(fontFile, flip), new TextureRegion(new Texture(imageFile, false)), integer);
	}

	public BitmapFont (BitmapFontData data, TextureRegion region, boolean integer) {
		this.region = region == null ? new TextureRegion(new Texture(Gdx.files.internal(data.imagePath), false)) : region;
		this.flipped = data.flipped;
		this.integer = integer;
		this.data = data;
		load(data);
	}

	private void load (BitmapFontData data) {
		float invTexWidth = 1.0f / region.getTexture().getWidth();
		float invTexHeight = 1.0f / region.getTexture().getHeight();
		float u = region.u;
		float v = region.v;

		for (Glyph[] page : data.glyphs) {
			if (page == null) continue;
			for (Glyph glyph : page) {
				if (glyph == null) continue;
				glyph.u = u + glyph.srcX * invTexWidth;
				glyph.u2 = u + (glyph.srcX + glyph.width) * invTexWidth;
				if (data.flipped) {
					glyph.v = v + glyph.srcY * invTexHeight;
					glyph.v2 = v + (glyph.srcY + glyph.height) * invTexHeight;
				} else {
					glyph.v2 = v + glyph.srcY * invTexHeight;
					glyph.v = v + (glyph.srcY + glyph.height) * invTexHeight;
				}
			}
		}
	}

	/** Draws a string at the specified position.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline). Note the same TextBounds
	 *         instance is used for all methods that return TextBounds. */
	public TextBounds draw (SpriteBatch spriteBatch, CharSequence str, float x, float y) {
		return draw(spriteBatch, str, x, y, 0, str.length());
	}

	/** Draws a substring at the specified position.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @param start The first character of the string to draw.
	 * @param end The last character of the string to draw (exclusive).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline). Note the same TextBounds
	 *         instance is used for all methods that return TextBounds. */
	public TextBounds draw (SpriteBatch spriteBatch, CharSequence str, float x, float y, int start, int end) {
		float batchColor = spriteBatch.color;
		spriteBatch.setColor(color);
		final Texture texture = region.getTexture();
		y += data.ascent;
		float startX = x;
		Glyph lastGlyph = null;
		if (data.scaleX == 1 && data.scaleY == 1) {
			if (integer) {
				y = (int)y;
				x = (int)x;
			}
			while (start < end) {
				lastGlyph = data.getGlyph(str.charAt(start++));
				if (lastGlyph != null) {
					spriteBatch.draw(texture, //
						x + lastGlyph.xoffset, y + lastGlyph.yoffset, //
						lastGlyph.width, lastGlyph.height, //
						lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					x += lastGlyph.xadvance;
					break;
				}
			}
			while (start < end) {
				char ch = str.charAt(start++);
				Glyph g = data.getGlyph(ch);
				if (g == null) continue;
				x += lastGlyph.getKerning(ch);
				if (integer) x = (int)x;
				lastGlyph = g;
				spriteBatch.draw(texture, //
					x + lastGlyph.xoffset, y + lastGlyph.yoffset, //
					lastGlyph.width, lastGlyph.height, //
					lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				x += g.xadvance;
			}
		} else {
			float scaleX = this.data.scaleX, scaleY = this.data.scaleY;
			while (start < end) {
				lastGlyph = data.getGlyph(str.charAt(start++));
				if (lastGlyph != null) {
					if (!integer) {
						spriteBatch.draw(texture, //
							x + lastGlyph.xoffset * scaleX, //
							y + lastGlyph.yoffset * scaleY, //
							lastGlyph.width * scaleX, //
							lastGlyph.height * scaleY, //
							lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					} else {
						spriteBatch.draw(texture, //
							(int)(x + lastGlyph.xoffset * scaleX), //
							(int)(y + lastGlyph.yoffset * scaleY), //
							(int)(lastGlyph.width * scaleX), //
							(int)(lastGlyph.height * scaleY), //
							lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					}
					x += lastGlyph.xadvance * scaleX;
					break;
				}
			}
			while (start < end) {
				char ch = str.charAt(start++);
				Glyph g = data.getGlyph(ch);
				if (g == null) continue;
				x += lastGlyph.getKerning(ch) * scaleX;
				lastGlyph = g;
				if (!integer) {
					spriteBatch.draw(texture, //
						x + lastGlyph.xoffset * scaleX, //
						y + lastGlyph.yoffset * scaleY, //
						lastGlyph.width * scaleX, //
						lastGlyph.height * scaleY, //
						lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				} else {
					spriteBatch.draw(texture, //
						(int)(x + lastGlyph.xoffset * scaleX), //
						(int)(y + lastGlyph.yoffset * scaleY), //
						(int)(lastGlyph.width * scaleX), //
						(int)(lastGlyph.height * scaleY), //
						lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				}
				x += g.xadvance * scaleX;
			}
		}
		spriteBatch.setColor(batchColor);
		textBounds.width = x - startX;
		textBounds.height = data.capHeight;
		return textBounds;
	}

	/** Draws a string, which may contain newlines (\n), at the specified position.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline of the last line). Note the
	 *         same TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds drawMultiLine (SpriteBatch spriteBatch, CharSequence str, float x, float y) {
		return drawMultiLine(spriteBatch, str, x, y, 0, HAlignment.LEFT);
	}

	/** Draws a string, which may contain newlines (\n), at the specified position and alignment. Each line is aligned horizontally
	 * within a rectangle of the specified width.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline of the last line). Note the
	 *         same TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds drawMultiLine (SpriteBatch spriteBatch, CharSequence str, float x, float y, float alignmentWidth,
		HAlignment alignment) {
		float batchColor = spriteBatch.color;
		float down = this.data.down;
		int start = 0;
		int numLines = 0;
		int length = str.length();
		float maxWidth = 0;
		while (start < length) {
			int lineEnd = indexOf(str, '\n', start);
			float xOffset = 0;
			if (alignment != HAlignment.LEFT) {
				float lineWidth = getBounds(str, start, lineEnd).width;
				xOffset = alignmentWidth - lineWidth;
				if (alignment == HAlignment.CENTER) xOffset = xOffset / 2;
			}
			float lineWidth = draw(spriteBatch, str, x + xOffset, y, start, lineEnd).width;
			maxWidth = Math.max(maxWidth, lineWidth);
			start = lineEnd + 1;
			y += down;
			numLines++;
		}
		spriteBatch.setColor(batchColor);

		textBounds.width = maxWidth;
		textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
		return textBounds;
	}

	/** Draws a string, which may contain newlines (\n), with the specified position. Each line is automatically wrapped to keep it
	 * within a rectangle of the specified width.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline of the last line). Note the
	 *         same TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds drawWrapped (SpriteBatch spriteBatch, CharSequence str, float x, float y, float wrapWidth) {
		return drawWrapped(spriteBatch, str, x, y, wrapWidth, HAlignment.LEFT);
	}

	/** Draws a string, which may contain newlines (\n), with the specified position. Each line is automatically wrapped to keep it
	 * within a rectangle of the specified width, and aligned horizontally within that rectangle.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline of the last line). Note the
	 *         same TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds drawWrapped (SpriteBatch spriteBatch, CharSequence str, float x, float y, float wrapWidth,
		HAlignment alignment) {
		float batchColor = spriteBatch.color;
		float down = this.data.down;
		int start = 0;
		int numLines = 0;
		int length = str.length();
		float maxWidth = 0;
		while (start < length) {
			int lineEnd = start + computeVisibleGlyphs(str, start, indexOf(str, '\n', start), wrapWidth);
			int nextLineStart;
			if (lineEnd < length) {
				int originalLineEnd = lineEnd;
				while (lineEnd > start) {
					char ch = str.charAt(lineEnd);
					if (ch == ' ' || ch == '\n') break;
					lineEnd--;
				}
				if (lineEnd == start) {
					lineEnd = originalLineEnd;
					if (lineEnd == start) lineEnd++;
					nextLineStart = lineEnd;
				} else
					nextLineStart = lineEnd + 1; // Eat space or newline.
			} else {
				if (lineEnd == start) lineEnd++;
				nextLineStart = length;
			}
			float xOffset = 0;
			if (alignment != HAlignment.LEFT) {
				float lineWidth = getBounds(str, start, lineEnd).width;
				xOffset = wrapWidth - lineWidth;
				if (alignment == HAlignment.CENTER) xOffset /= 2;
			}
			float lineWidth = draw(spriteBatch, str, x + xOffset, y, start, lineEnd).width;
			maxWidth = Math.max(maxWidth, lineWidth);
			start = nextLineStart;
			y += down;
			numLines++;
		}
		spriteBatch.setColor(batchColor);
		textBounds.width = maxWidth;
		textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
		return textBounds;
	}

	/** Returns the size of the specified string. The height is the distance from the top of most capital letters in the font (the
	 * {@link #getCapHeight() cap height}) to the baseline. Note the same TextBounds instance is used for all methods that return
	 * TextBounds. */
	public TextBounds getBounds (CharSequence str) {
		return getBounds(str, 0, str.length());
	}

	/** Returns the size of the specified substring. The height is the distance from the top of most capital letters in the font
	 * (the {@link #getCapHeight() cap height}) to the baseline. Note the same TextBounds instance is used for all methods that
	 * return TextBounds.
	 * @param start The first character of the string.
	 * @param end The last character of the string (exclusive). */
	public TextBounds getBounds (CharSequence str, int start, int end) {
		int width = 0;
		Glyph lastGlyph = null;
		while (start < end) {
			lastGlyph = data.getGlyph(str.charAt(start++));
			if (lastGlyph != null) {
				width = lastGlyph.xadvance;
				break;
			}
		}
		while (start < end) {
			char ch = str.charAt(start++);
			Glyph g = data.getGlyph(ch);
			if (g != null) {
				width += lastGlyph.getKerning(ch);
				lastGlyph = g;
				width += g.xadvance;
			}
		}
		textBounds.width = width * data.scaleX;
		textBounds.height = data.capHeight;
		return textBounds;
	}

	/** Returns the size of the specified string, which may contain newlines. The height is the distance from the top of most
	 * capital letters in the font (the {@link #getCapHeight() cap height}) to the baseline of the last line of text. Note the same
	 * TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds getMultiLineBounds (CharSequence str) {
		int start = 0;
		float maxWidth = 0;
		int numLines = 0;
		int length = str.length();
		while (start < length) {
			int lineEnd = indexOf(str, '\n', start);
			float lineWidth = getBounds(str, start, lineEnd).width;
			maxWidth = Math.max(maxWidth, lineWidth);
			start = lineEnd + 1;
			numLines++;
		}
		textBounds.width = maxWidth;
		textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
		return textBounds;
	}

	/** Returns the size of the specified string, which may contain newlines and is wrapped to keep it within a rectangle of the
	 * specified width. The height is the distance from the top of most capital letters in the font (the {@link #getCapHeight() cap
	 * height}) to the baseline of the last line of text. Note the same TextBounds instance is used for all methods that return
	 * TextBounds. */
	public TextBounds getWrappedBounds (CharSequence str, float wrapWidth) {
		int start = 0;
		int numLines = 0;
		int length = str.length();
		float maxWidth = 0;
		while (start < length) {
			int lineEnd = start + computeVisibleGlyphs(str, start, indexOf(str, '\n', start), wrapWidth);
			int nextLineStart;
			if (lineEnd < length) {
				int originalLineEnd = lineEnd;
				while (lineEnd > start) {
					char ch = str.charAt(lineEnd);
					if (ch == ' ' || ch == '\n') break;
					lineEnd--;
				}
				if (lineEnd == start) {
					lineEnd = originalLineEnd;
					if (lineEnd == start) lineEnd++;
					nextLineStart = lineEnd;
				} else
					nextLineStart = lineEnd + 1; // Eat space or newline.
			} else {
				if (lineEnd == start) lineEnd++;
				nextLineStart = length;
			}
			float lineWidth = getBounds(str, start, lineEnd).width;
			maxWidth = Math.max(maxWidth, lineWidth);
			start = nextLineStart;
			numLines++;
		}
		textBounds.width = maxWidth;
		textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
		return textBounds;
	}

	/** Computes the glyph advances for the given character sequence and stores them in the provided {@link FloatArray}. The
	 * FloatArray is cleared. This will add an additional element at the end.
	 * @param str the character sequence
	 * @param glyphAdvances the glyph advances output array.
	 * @param glyphPositions the glyph positions output array. */
	public void computeGlyphAdvancesAndPositions (CharSequence str, FloatArray glyphAdvances, FloatArray glyphPositions) {
		glyphAdvances.clear();
		glyphPositions.clear();
		int index = 0;
		int end = str.length();
		int width = 0;
		Glyph lastGlyph = null;
		if (data.scaleX == 1) {
			for (; index < end; index++) {
				char ch = str.charAt(index);
				Glyph g = data.getGlyph(ch);
				if (g != null) {
					if (lastGlyph != null) width += lastGlyph.getKerning(ch);
					lastGlyph = g;
					glyphAdvances.add(g.xadvance);
					glyphPositions.add(width);
					width += g.xadvance;
				}
			}
			glyphAdvances.add(0);
			glyphPositions.add(width);
		} else {
			float scaleX = this.data.scaleX;
			for (; index < end; index++) {
				char ch = str.charAt(index);
				Glyph g = data.getGlyph(ch);
				if (g != null) {
					if (lastGlyph != null) width += lastGlyph.getKerning(ch) * scaleX;
					lastGlyph = g;
					glyphAdvances.add(g.xadvance * scaleX);
					glyphPositions.add(width);
					width += g.xadvance;
				}
			}
			glyphAdvances.add(0);
			glyphPositions.add(width);
		}
	}

	/** Returns the number of glyphs from the substring that can be rendered in the specified width.
	 * @param start The first character of the string.
	 * @param end The last character of the string (exclusive). */
	public int computeVisibleGlyphs (CharSequence str, int start, int end, float availableWidth) {
		int index = start;
		int width = 0;
		Glyph lastGlyph = null;
		if (data.scaleX == 1) {
			for (; index < end; index++) {
				char ch = str.charAt(index);
				Glyph g = data.getGlyph(ch);
				if (g != null) {
					if (lastGlyph != null) width += lastGlyph.getKerning(ch);
					lastGlyph = g;
					if (width + g.xadvance > availableWidth) break;
					width += g.xadvance;
				}
			}
		} else {
			float scaleX = this.data.scaleX;
			for (; index < end; index++) {
				char ch = str.charAt(index);
				Glyph g = data.getGlyph(ch);
				if (g != null) {
					if (lastGlyph != null) width += lastGlyph.getKerning(ch) * scaleX;
					lastGlyph = g;
					if (width + g.xadvance * scaleX > availableWidth) break;
					width += g.xadvance * scaleX;
				}
			}
		}
		return index - start;
	}

	public void setColor (float color) {
		this.color = color;
	}

	public void setColor (Color tint) {
		this.color = tint.toFloatBits();
	}

	public void setColor (float r, float g, float b, float a) {
		int intBits = (int)(255 * a) << 24 | (int)(255 * b) << 16 | (int)(255 * g) << 8 | (int)(255 * r);
		color = NumberUtils.intBitsToFloat((intBits & 0xfeffffff));
	}

	/** Returns the color of this font. Changing the returned color will have no affect, {@link #setColor(Color)} or
	 * {@link #setColor(float, float, float, float)} must be used. */
	public Color getColor () {
		int intBits = NumberUtils.floatToRawIntBits(color);
		Color color = this.tempColor;
		color.r = (intBits & 0xff) / 255f;
		color.g = ((intBits >>> 8) & 0xff) / 255f;
		color.b = ((intBits >>> 16) & 0xff) / 255f;
		color.a = ((intBits >>> 24) & 0xff) / 255f;
		return color;
	}

	public void setScale (float scaleX, float scaleY) {
		data.spaceWidth = data.spaceWidth / this.data.scaleX * scaleX;
		data.xHeight = data.xHeight / this.data.scaleY * scaleY;
		data.capHeight = data.capHeight / this.data.scaleY * scaleY;
		data.ascent = data.ascent / this.data.scaleY * scaleY;
		data.descent = data.descent / this.data.scaleY * scaleY;
		data.down = data.down / this.data.scaleY * scaleY;
		data.scaleX = scaleX;
		data.scaleY = scaleY;
	}

	/** Scales the font by the specified amount in both directions.<br>
	 * <br>
	 * Note that smoother scaling can be achieved if the texture backing the BitmapFont is using {@link TextureFilter#Linear}. The
	 * default is Nearest, so use a BitmapFont constructor that takes a {@link TextureRegion}. */
	public void setScale (float scaleXY) {
		setScale(scaleXY, scaleXY);
	}

	/** Sets the font's scale relative to the current scale. */
	public void scale (float amount) {
		setScale(data.scaleX + amount, data.scaleY + amount);
	}

	public float getScaleX () {
		return data.scaleX;
	}

	public float getScaleY () {
		return data.scaleY;
	}

	public TextureRegion getRegion () {
		return region;
	}

	/** Returns the line height, which is the distance from one line of text to the next. */
	public float getLineHeight () {
		return data.lineHeight;
	}

	/** Returns the width of the space character. */
	public float getSpaceWidth () {
		return data.spaceWidth;
	}

	/** Returns the x-height, which is the distance from the top of most lowercase characters to the baseline. */
	public float getXHeight () {
		return data.xHeight;
	}

	/** Returns the cap height, which is the distance from the top of most uppercase characters to the baseline. Since the drawing
	 * position is the cap height of the first line, the cap height can be used to get the location of the baseline. */
	public float getCapHeight () {
		return data.capHeight;
	}

	/** Returns the ascent, which is the distance from the cap height to the top of the tallest glyph. */
	public float getAscent () {
		return data.ascent;
	}

	/** Returns the descent, which is the distance from the bottom of the glyph that extends the lowest to the baseline. This number
	 * is negative. */
	public float getDescent () {
		return data.descent;
	}

	/** Returns true if this BitmapFont has been flipped for use with a y-down coordinate system. */
	public boolean isFlipped () {
		return flipped;
	}

	/** Disposes the texture used by this BitmapFont's region. */
	public void dispose () {
		region.getTexture().dispose();
	}

	/** Makes the specified glyphs fixed width. This can be useful to make the numbers in a font fixed width. Eg, when horizontally
	 * centering a score or loading percentage text, it will not jump around as different numbers are shown. */
	public void setFixedWidthGlyphs (CharSequence glyphs) {
		int maxAdvance = 0;
		for (int index = 0, end = glyphs.length(); index < end; index++) {
			Glyph g = data.getGlyph(glyphs.charAt(index));
			if (g != null && g.xadvance > maxAdvance) maxAdvance = g.xadvance;
		}
		for (int index = 0, end = glyphs.length(); index < end; index++) {
			Glyph g = data.getGlyph(glyphs.charAt(index));
			if (g == null) continue;
			g.xoffset += (maxAdvance - g.xadvance) / 2;
			g.xadvance = maxAdvance;
			g.kerning = null;
		}
	}

	/** @param character
	 * @return whether the given character is contained in this font. */
	public boolean containsCharacter (char character) {
		return data.getGlyph(character) != null;
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

	public BitmapFontData getData () {
		return data;
	}

	static class Glyph {
		public int srcX;
		public int srcY;
		int width, height;
		float u, v, u2, v2;
		int xoffset, yoffset;
		int xadvance;
		byte[][] kerning;

		int getKerning (char ch) {
			if (kerning != null) {
				byte[] page = kerning[ch >>> LOG2_PAGE_SIZE];
				if (page != null) return page[ch & PAGE_SIZE - 1];
			}
			return 0;
		}

		void setKerning (int ch, int value) {
			if (kerning == null) kerning = new byte[PAGES][];
			byte[] page = kerning[ch >>> LOG2_PAGE_SIZE];
			if (page == null) kerning[ch >>> LOG2_PAGE_SIZE] = page = new byte[PAGE_SIZE];
			page[ch & PAGE_SIZE - 1] = (byte)value;
		}
	}

	static int indexOf (CharSequence text, char ch, int start) {
		final int n = text.length();
		for (; start < n; start++)
			if (text.charAt(start) == ch) return start;
		return n;
	}

	static public class TextBounds {
		public float width;
		public float height;

		public TextBounds () {
		}

		public TextBounds (TextBounds bounds) {
			set(bounds);
		}

		public void set (TextBounds bounds) {
			width = bounds.width;
			height = bounds.height;
		}
	}

	static public enum HAlignment {
		LEFT, CENTER, RIGHT
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/987.java