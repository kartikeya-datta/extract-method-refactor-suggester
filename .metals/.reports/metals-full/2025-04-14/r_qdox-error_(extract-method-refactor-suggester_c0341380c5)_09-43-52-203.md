error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5082.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5082.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5082.java
text:
```scala
B@@ufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.absolute(hieroFileRef).read()));


package com.badlogic.gdx.hiero.unicodefont;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.hiero.unicodefont.effects.ConfigurableEffect;
import com.badlogic.gdx.hiero.unicodefont.effects.ConfigurableEffect.Value;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Holds the settings needed to configure a UnicodeFont.
 * @author Nathan Sweet <misc@n4te.com>
 */
public class HieroSettings {
	private int fontSize = 12;
	private boolean bold = false, italic = false;
	private int paddingTop, paddingLeft, paddingBottom, paddingRight, paddingAdvanceX, paddingAdvanceY;
	private int glyphPageWidth = 512, glyphPageHeight = 512;
	private final List effects = new ArrayList();
	private boolean nativeRendering;

	public HieroSettings () {
	}

	/**
	 * @param hieroFileRef The file system or classpath location of the Hiero settings file.
	 */
	public HieroSettings (String hieroFileRef) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.readFile(hieroFileRef, FileType.Absolute)));
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				line = line.trim();
				if (line.length() == 0) continue;
				String[] pieces = line.split("=", 2);
				String name = pieces[0].trim();
				String value = pieces[1];
				if (name.equals("font.size")) {
					fontSize = Integer.parseInt(value);
				} else if (name.equals("font.bold")) {
					bold = Boolean.parseBoolean(value);
				} else if (name.equals("font.italic")) {
					italic = Boolean.parseBoolean(value);
				} else if (name.equals("pad.top")) {
					paddingTop = Integer.parseInt(value);
				} else if (name.equals("pad.right")) {
					paddingRight = Integer.parseInt(value);
				} else if (name.equals("pad.bottom")) {
					paddingBottom = Integer.parseInt(value);
				} else if (name.equals("pad.left")) {
					paddingLeft = Integer.parseInt(value);
				} else if (name.equals("pad.advance.x")) {
					paddingAdvanceX = Integer.parseInt(value);
				} else if (name.equals("pad.advance.y")) {
					paddingAdvanceY = Integer.parseInt(value);
				} else if (name.equals("glyph.page.width")) {
					glyphPageWidth = Integer.parseInt(value);
				} else if (name.equals("glyph.page.height")) {
					glyphPageHeight = Integer.parseInt(value);
				} else if (name.equals("glyph.native.rendering")) {
					nativeRendering = Boolean.parseBoolean(value);
				} else if (name.equals("effect.class")) {
					try {
						effects.add(Class.forName(value).newInstance());
					} catch (Throwable ex) {
						throw new GdxRuntimeException("Unable to create effect instance: " + value, ex);
					}
				} else if (name.startsWith("effect.")) {
					// Set an effect value on the last added effect.
					name = name.substring(7);
					ConfigurableEffect effect = (ConfigurableEffect)effects.get(effects.size() - 1);
					List values = effect.getValues();
					for (Iterator iter = values.iterator(); iter.hasNext();) {
						Value effectValue = (Value)iter.next();
						if (effectValue.getName().equals(name)) {
							effectValue.setString(value);
							break;
						}
					}
					effect.setValues(values);
				}
			}
			reader.close();
		} catch (Throwable ex) {
			throw new GdxRuntimeException("Unable to load Hiero font file: " + hieroFileRef, ex);
		}
	}

	/**
	 * @see UnicodeFont#getPaddingTop()
	 */
	public int getPaddingTop () {
		return paddingTop;
	}

	/**
	 * @see UnicodeFont#setPaddingTop(int)
	 */
	public void setPaddingTop (int paddingTop) {
		this.paddingTop = paddingTop;
	}

	/**
	 * @see UnicodeFont#getPaddingLeft()
	 */
	public int getPaddingLeft () {
		return paddingLeft;
	}

	/**
	 * @see UnicodeFont#setPaddingLeft(int)
	 */
	public void setPaddingLeft (int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	/**
	 * @see UnicodeFont#getPaddingBottom()
	 */
	public int getPaddingBottom () {
		return paddingBottom;
	}

	/**
	 * @see UnicodeFont#setPaddingBottom(int)
	 */
	public void setPaddingBottom (int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	/**
	 * @see UnicodeFont#getPaddingRight()
	 */
	public int getPaddingRight () {
		return paddingRight;
	}

	/**
	 * @see UnicodeFont#setPaddingRight(int)
	 */
	public void setPaddingRight (int paddingRight) {
		this.paddingRight = paddingRight;
	}

	/**
	 * @see UnicodeFont#getPaddingAdvanceX()
	 */
	public int getPaddingAdvanceX () {
		return paddingAdvanceX;
	}

	/**
	 * @see UnicodeFont#setPaddingAdvanceX(int)
	 */
	public void setPaddingAdvanceX (int paddingAdvanceX) {
		this.paddingAdvanceX = paddingAdvanceX;
	}

	/**
	 * @see UnicodeFont#getPaddingAdvanceY()
	 */
	public int getPaddingAdvanceY () {
		return paddingAdvanceY;
	}

	/**
	 * @see UnicodeFont#setPaddingAdvanceY(int)
	 */
	public void setPaddingAdvanceY (int paddingAdvanceY) {
		this.paddingAdvanceY = paddingAdvanceY;
	}

	/**
	 * @see UnicodeFont#getGlyphPageWidth()
	 */
	public int getGlyphPageWidth () {
		return glyphPageWidth;
	}

	/**
	 * @see UnicodeFont#setGlyphPageWidth(int)
	 */
	public void setGlyphPageWidth (int glyphPageWidth) {
		this.glyphPageWidth = glyphPageWidth;
	}

	/**
	 * @see UnicodeFont#getGlyphPageHeight()
	 */
	public int getGlyphPageHeight () {
		return glyphPageHeight;
	}

	/**
	 * @see UnicodeFont#setGlyphPageHeight(int)
	 */
	public void setGlyphPageHeight (int glyphPageHeight) {
		this.glyphPageHeight = glyphPageHeight;
	}

	/**
	 * @see UnicodeFont#UnicodeFont(String, int, boolean, boolean)
	 * @see UnicodeFont#UnicodeFont(java.awt.Font, int, boolean, boolean)
	 */
	public int getFontSize () {
		return fontSize;
	}

	/**
	 * @see UnicodeFont#UnicodeFont(String, int, boolean, boolean)
	 * @see UnicodeFont#UnicodeFont(java.awt.Font, int, boolean, boolean)
	 */
	public void setFontSize (int fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * @see UnicodeFont#UnicodeFont(String, int, boolean, boolean)
	 * @see UnicodeFont#UnicodeFont(java.awt.Font, int, boolean, boolean)
	 */
	public boolean isBold () {
		return bold;
	}

	/**
	 * @see UnicodeFont#UnicodeFont(String, int, boolean, boolean)
	 * @see UnicodeFont#UnicodeFont(java.awt.Font, int, boolean, boolean)
	 */
	public void setBold (boolean bold) {
		this.bold = bold;
	}

	/**
	 * @see UnicodeFont#UnicodeFont(String, int, boolean, boolean)
	 * @see UnicodeFont#UnicodeFont(java.awt.Font, int, boolean, boolean)
	 */
	public boolean isItalic () {
		return italic;
	}

	/**
	 * @see UnicodeFont#UnicodeFont(String, int, boolean, boolean)
	 * @see UnicodeFont#UnicodeFont(java.awt.Font, int, boolean, boolean)
	 */
	public void setItalic (boolean italic) {
		this.italic = italic;
	}

	/**
	 * @see UnicodeFont#getEffects()
	 */
	public List getEffects () {
		return effects;
	}

	public boolean getNativeRendering () {
		return nativeRendering;
	}

	public void setNativeRendering (boolean nativeRendering) {
		this.nativeRendering = nativeRendering;
	}

	/**
	 * Saves the settings to a file.
	 * @throws IOException if the file could not be saved.
	 */
	public void save (File file) throws IOException {
		PrintStream out = new PrintStream(new FileOutputStream(file));
		out.println("font.size=" + fontSize);
		out.println("font.bold=" + bold);
		out.println("font.italic=" + italic);
		out.println();
		out.println("pad.top=" + paddingTop);
		out.println("pad.right=" + paddingRight);
		out.println("pad.bottom=" + paddingBottom);
		out.println("pad.left=" + paddingLeft);
		out.println("pad.advance.x=" + paddingAdvanceX);
		out.println("pad.advance.y=" + paddingAdvanceY);
		out.println();
		out.println("glyph.native.rendering=" + nativeRendering);
		out.println("glyph.page.width=" + glyphPageWidth);
		out.println("glyph.page.height=" + glyphPageHeight);
		out.println();
		for (Iterator iter = effects.iterator(); iter.hasNext();) {
			ConfigurableEffect effect = (ConfigurableEffect)iter.next();
			out.println("effect.class=" + effect.getClass().getName());
			for (Iterator iter2 = effect.getValues().iterator(); iter2.hasNext();) {
				Value value = (Value)iter2.next();
				out.println("effect." + value.getName() + "=" + value.getString());
			}
			out.println();
		}
		out.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5082.java