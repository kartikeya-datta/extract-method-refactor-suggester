error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9964.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9964.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9964.java
text:
```scala
r@@eturn bitmapFont.computeVisibleGlyphs(str, start, end, width);

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.renderer.AnimationState;
import de.matthiasmann.twl.renderer.Font;
import de.matthiasmann.twl.renderer.FontCache;
import de.matthiasmann.twl.renderer.FontParameter;
import de.matthiasmann.twl.utils.StateExpression;

/**
 * @author Nathan Sweet
 * @author Matthias Mann
 */
class GdxFont implements Font {
	static private final HAlignment[] gdxAlignment = HAlignment.values();

	final GdxRenderer renderer;
	final BitmapFont bitmapFont;
	private final FontState[] fontStates;
	private final float yOffset;

	public GdxFont (GdxRenderer renderer, BitmapFont bitmapFont, Map<String, String> params, Collection<FontParameter> condParams) {
		this.bitmapFont = bitmapFont;
		this.renderer = renderer;
		yOffset = -bitmapFont.getAscent();

		ArrayList<FontState> states = new ArrayList<FontState>();
		for (FontParameter p : condParams) {
			HashMap<String, String> effective = new HashMap<String, String>(params);
			effective.putAll(p.getParams());
			states.add(new FontState(p.getCondition(), effective));
		}
		states.add(new FontState(null, params));
		this.fontStates = states.toArray(new FontState[states.size()]);
	}

	public int drawText (AnimationState as, int x, int y, CharSequence str) {
		return drawText(as, x, y, str, 0, str.length());
	}

	public int drawText (AnimationState as, int x, int y, CharSequence str, int start, int end) {
		FontState fontState = evalFontState(as);
		x += fontState.offsetX;
		y += fontState.offsetY + yOffset;
		bitmapFont.setColor(renderer.getColor(fontState.color));
		return bitmapFont.draw(renderer.batch, str, x, y, start, end).width;
	}

	public int drawMultiLineText (AnimationState as, int x, int y, CharSequence str, int width,
		de.matthiasmann.twl.HAlignment align) {
		FontState fontState = evalFontState(as);
		x += fontState.offsetX;
		y += fontState.offsetY + yOffset;
		bitmapFont.setColor(renderer.getColor(fontState.color));
		return bitmapFont.drawMultiLine(renderer.batch, str, x, y, width, gdxAlignment[align.ordinal()]).width;
	}

	public FontCache cacheText (FontCache cache, CharSequence str) {
		return cacheText(cache, str, 0, str.length());
	}

	public FontCache cacheText (FontCache cache, CharSequence str, int start, int end) {
		if (cache == null) cache = new GdxFontCache();
		GdxFontCache bitmapCache = (GdxFontCache)cache;
		bitmapFont.setColor(com.badlogic.gdx.graphics.Color.WHITE);
		bitmapCache.setText(str, 0, yOffset, start, end);
		return cache;
	}

	public FontCache cacheMultiLineText (FontCache cache, CharSequence str, int width, de.matthiasmann.twl.HAlignment align) {
		if (cache == null) cache = new GdxFontCache();
		GdxFontCache bitmapCache = (GdxFontCache)cache;
		bitmapFont.setColor(com.badlogic.gdx.graphics.Color.WHITE);
		bitmapCache.setMultiLineText(str, 0, yOffset, width, gdxAlignment[align.ordinal()]);
		return cache;
	}

	public void destroy () {
		bitmapFont.dispose();
	}

	public int getBaseLine () {
		return (int)bitmapFont.getCapHeight();
	}

	public int getLineHeight () {
		return (int)bitmapFont.getLineHeight();
	}

	public int getSpaceWidth () {
		return (int)bitmapFont.getSpaceWidth();
	}

	public int getEM () {
		return (int)bitmapFont.getLineHeight();
	}

	public int getEX () {
		return (int)bitmapFont.getXHeight();
	}

	public int computeMultiLineTextWidth (CharSequence str) {
		return bitmapFont.getMultiLineBounds(str).width;
	}

	public int computeTextWidth (CharSequence str) {
		return bitmapFont.getBounds(str).width;
	}

	public int computeTextWidth (CharSequence str, int start, int end) {
		return bitmapFont.getBounds(str, start, end).width;
	}

	public int computeVisibleGlpyhs (CharSequence str, int start, int end, int width) {
		return bitmapFont.computeVisibleGlpyhs(str, start, end, width);
	}

	FontState evalFontState (AnimationState animationState) {
		int i = 0;
		for (int n = fontStates.length - 1; i < n; i++)
			if (fontStates[i].condition.evaluate(animationState)) break;
		return fontStates[i];
	}

	static private class FontState {
		final StateExpression condition;
		final Color color;
		final int offsetX;
		final int offsetY;

		public FontState (StateExpression condition, Map<String, String> params) {
			this.condition = condition;
			String colorStr = params.get("color");
			if (colorStr == null) throw new IllegalArgumentException("Color must be defined.");
			color = Color.parserColor(colorStr);
			if (color == null) throw new IllegalArgumentException("Unknown color name: " + colorStr);
			String value = params.get("offsetX");
			offsetX = value == null ? 0 : Integer.parseInt(value);
			value = params.get("offsetY");
			offsetY = value == null ? 0 : Integer.parseInt(value);
		}
	}

	private class GdxFontCache extends BitmapFontCache implements FontCache {
		public GdxFontCache () {
			super(bitmapFont);
		}

		public void draw (AnimationState as, int x, int y) {
			GdxFont.FontState fontState = evalFontState(as);
			setColor(renderer.getColor(fontState.color));
			setPosition(x + fontState.offsetX, y + fontState.offsetY);
			draw(renderer.batch);
		}

		public int getWidth () {
			return getBounds().width;
		}

		public int getHeight () {
			return getBounds().height;
		}

		public void destroy () {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9964.java