error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5081.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5081.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5081.java
text:
```scala
k@@erning.load(Gdx.files.internal(ttfFileRef).read(), font.getSize());


package com.badlogic.gdx.hiero;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.hiero.unicodefont.Glyph;
import com.badlogic.gdx.hiero.unicodefont.GlyphPage;
import com.badlogic.gdx.hiero.unicodefont.UnicodeFont;

/**
 * @author Nathan Sweet <misc@n4te.com>
 */
public class BMFontUtil {
	private final UnicodeFont unicodeFont;

	public BMFontUtil (UnicodeFont unicodeFont) {
		this.unicodeFont = unicodeFont;
	}

	public void save (File outputBMFontFile) throws IOException {
		File outputDir = outputBMFontFile.getParentFile();
		String outputName = outputBMFontFile.getName();
		if (outputName.endsWith(".fnt")) outputName = outputName.substring(0, outputName.length() - 4);

		unicodeFont.loadGlyphs();

		PrintStream out = new PrintStream(new FileOutputStream(new File(outputDir, outputName + ".fnt")));
		Font font = unicodeFont.getFont();
		int pageWidth = unicodeFont.getGlyphPageWidth();
		int pageHeight = unicodeFont.getGlyphPageHeight();
		out.println("info face=\"" + font.getFontName() + "\" size=" + font.getSize() + " bold=" + (font.isBold() ? 1 : 0)
			+ " italic=" + (font.isItalic() ? 1 : 0)
			+ " charset=\"\" unicode=0 stretchH=100 smooth=1 aa=1 padding=0,0,0,0 spacing=1,1");
		out.println("common lineHeight=" + unicodeFont.getLineHeight() + " base=" + unicodeFont.getAscent() + " scaleW="
			+ pageWidth + " scaleH=" + pageHeight + " pages=" + unicodeFont.getGlyphPages().size() + " packed=0");

		int pageIndex = 0, glyphCount = 0;
		for (Iterator pageIter = unicodeFont.getGlyphPages().iterator(); pageIter.hasNext();) {
			GlyphPage page = (GlyphPage)pageIter.next();
			String fileName;
			if (pageIndex == 0 && !pageIter.hasNext())
				fileName = outputName + ".png";
			else
				fileName = outputName + (pageIndex + 1) + ".png";
			out.println("page id=" + pageIndex + " file=\"" + fileName + "\"");
			glyphCount += page.getGlyphs().size();
			pageIndex++;
		}

		out.println("chars count=" + glyphCount);

		// Always output space entry (codepoint 32).
		int[] glyphMetrics = getGlyphMetrics(font, 32);
		int xAdvance = glyphMetrics[1];
		out.println("char id=32   x=0     y=0     width=0     height=0     xoffset=0     yoffset=" + unicodeFont.getAscent()
			+ "    xadvance=" + xAdvance + "     page=0  chnl=0 ");

		pageIndex = 0;
		List allGlyphs = new ArrayList(512);
		for (Iterator pageIter = unicodeFont.getGlyphPages().iterator(); pageIter.hasNext();) {
			GlyphPage page = (GlyphPage)pageIter.next();
			for (Iterator glyphIter = page.getGlyphs().iterator(); glyphIter.hasNext();) {
				Glyph glyph = (Glyph)glyphIter.next();

				glyphMetrics = getGlyphMetrics(font, glyph.getCodePoint());
				int xOffset = glyphMetrics[0];
				xAdvance = glyphMetrics[1];

				out.println("char id=" + glyph.getCodePoint() + "   " + "x=" + (int)(glyph.getU() * pageWidth) + "     y="
					+ (int)(glyph.getV() * pageHeight) + "     width=" + glyph.getWidth() + "     height=" + glyph.getHeight()
					+ "     xoffset=" + xOffset + "     yoffset=" + glyph.getYOffset() + "    xadvance=" + xAdvance + "     page="
					+ pageIndex + "  chnl=0 ");
			}
			allGlyphs.addAll(page.getGlyphs());
			pageIndex++;
		}

		String ttfFileRef = unicodeFont.getFontFile();
		if (ttfFileRef == null)
			System.out.println("Kerning information could not be output because a TTF font file was not specified.");
		else {
			Kerning kerning = new Kerning();
			try {
				kerning.load(Gdx.files.readFile(ttfFileRef, FileType.Internal), font.getSize());
			} catch (IOException ex) {
				System.out.println("Unable to read kerning information from font: " + ttfFileRef);
			}

			Map glyphCodeToCodePoint = new HashMap();
			for (Iterator iter = allGlyphs.iterator(); iter.hasNext();) {
				Glyph glyph = (Glyph)iter.next();
				glyphCodeToCodePoint.put(new Integer(getGlyphCode(font, glyph.getCodePoint())), new Integer(glyph.getCodePoint()));
			}

			List kernings = new ArrayList(256);
			class KerningPair {
				public int firstCodePoint, secondCodePoint, offset;
			}
			for (Iterator iter1 = allGlyphs.iterator(); iter1.hasNext();) {
				Glyph firstGlyph = (Glyph)iter1.next();
				int firstGlyphCode = getGlyphCode(font, firstGlyph.getCodePoint());
				int[] values = kerning.getValues(firstGlyphCode);
				if (values == null) continue;
				for (int i = 0; i < values.length; i++) {
					Integer secondCodePoint = (Integer)glyphCodeToCodePoint.get(new Integer(values[i] & 0xffff));
					if (secondCodePoint == null) continue; // We may not be outputting the second character.
					int offset = values[i] >> 16;
					KerningPair pair = new KerningPair();
					pair.firstCodePoint = firstGlyph.getCodePoint();
					pair.secondCodePoint = secondCodePoint.intValue();
					pair.offset = offset;
					kernings.add(pair);
				}
			}
			out.println("kernings count=" + kerning.getCount());
			for (Iterator iter = kernings.iterator(); iter.hasNext();) {
				KerningPair pair = (KerningPair)iter.next();
				out.println("kerning first=" + pair.firstCodePoint + "  second=" + pair.secondCodePoint + "  amount=" + pair.offset);
			}
		}
		out.close();

		int width = unicodeFont.getGlyphPageWidth();
		int height = unicodeFont.getGlyphPageHeight();
		IntBuffer buffer = BufferUtils.createIntBuffer(width * height);
		BufferedImage pageImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] row = new int[width];

		pageIndex = 0;
		for (Iterator pageIter = unicodeFont.getGlyphPages().iterator(); pageIter.hasNext();) {
			GlyphPage page = (GlyphPage)pageIter.next();
			String fileName;
			if (pageIndex == 0 && !pageIter.hasNext())
				fileName = outputName + ".png";
			else
				fileName = outputName + (pageIndex + 1) + ".png";

			page.getTexture().bind();
			buffer.clear();
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer);
			WritableRaster raster = pageImage.getRaster();
			for (int y = 0; y < height; y++) {
				buffer.get(row);
				raster.setDataElements(0, y, width, 1, row);
			}
			File imageOutputFile = new File(outputDir, fileName);
			ImageIO.write(pageImage, "png", imageOutputFile);

			pageIndex++;
		}
	}

	private int getGlyphCode (Font font, int codePoint) {
		char[] chars = Character.toChars(codePoint);
		GlyphVector vector = font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, Font.LAYOUT_LEFT_TO_RIGHT);
		return vector.getGlyphCode(0);
	}

	private int[] getGlyphMetrics (Font font, int codePoint) {
		// xOffset and xAdvance will be incorrect for unicode characters such as combining marks or non-spacing characters
		// (eg Pnujabi's "\u0A1C\u0A47") that require the context of surrounding glyphs to determine spacing, but thisis the
		// best we can do with the BMFont format.
		char[] chars = Character.toChars(codePoint);
		GlyphVector vector = font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, Font.LAYOUT_LEFT_TO_RIGHT);
		GlyphMetrics metrics = vector.getGlyphMetrics(0);
		int xOffset = vector.getGlyphPixelBounds(0, GlyphPage.renderContext, 0.5f, 0).x - unicodeFont.getPaddingLeft();
		int xAdvance = (int)(metrics.getAdvanceX() + unicodeFont.getPaddingAdvanceX() + unicodeFont.getPaddingLeft() + unicodeFont
			.getPaddingRight());
		return new int[] {xOffset, xAdvance};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5081.java