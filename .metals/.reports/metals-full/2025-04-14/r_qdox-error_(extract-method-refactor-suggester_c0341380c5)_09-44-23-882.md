error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6420.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6420.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6420.java
text:
```scala
B@@itmap bitmap = Bitmap.createBitmap( rect.width()==0?1:rect.width() + 5, getLineHeight(), AndroidPixmap.getInternalFormat(Font.pixelFormat) );

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
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
package com.badlogic.gdx.backends.android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Font;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;



/**
 * An implementation of {@link Font} for Android. 
 * 
 * @author badlogicgames@gmail.com
 *
 */
final class AndroidFont extends Font
{
	Typeface font;
	Paint paint;
	FontMetrics metrics;

	AndroidFont(Graphics graphics, String fontName, int size, FontStyle style) 
	{
		super( );		
		font = Typeface.create( fontName, getFontStyle( style ) );
		paint = new Paint( );
		paint.setTypeface(font);
		paint.setTextSize(size);
		paint.setAntiAlias(false);
		metrics = paint.getFontMetrics();		
	}

	AndroidFont(Graphics graphics, AssetManager assets, String file, int size,	FontStyle style) 
	{	
		super( );				
		font = Typeface.createFromAsset( assets, file );		
		paint = new Paint( );
		paint.setTypeface(font);
		paint.setTextSize(size);	
		paint.setAntiAlias(false);
		metrics = paint.getFontMetrics();
	}

	private int getFontStyle( FontStyle style )
	{
		if( style == FontStyle.Bold )
			return Typeface.BOLD;
		if( style == FontStyle.BoldItalic )
			return Typeface.BOLD_ITALIC;
		if( style == FontStyle.Italic )
			return Typeface.ITALIC;
		if( style == FontStyle.Plain )
			return Typeface.NORMAL;
		
		return Typeface.NORMAL;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getGlyphAdvance(char character) {
		float[] width = new float[1];
		paint.getTextWidths( "" + character, width );
		return (int)(Math.ceil(width[0]));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pixmap getGlyphBitmap(char character) {
		Rect rect = new Rect();		
		paint.getTextBounds( "" + character, 0, 1, rect );
		Bitmap bitmap = Bitmap.createBitmap( rect.width()==0?1:rect.width() + 5, getLineHeight(), Bitmap.Config.ARGB_8888 );
		Canvas g = new Canvas( bitmap );
		paint.setAntiAlias(true);
		paint.setColor(0x00000000);
		paint.setStyle(Style.FILL);
		g.drawRect( new Rect( 0, 0, rect.width() + 5, getLineHeight()), paint);
		paint.setColor(0xFFFFFFFF);		
		g.drawText( "" + character, 0, -metrics.ascent, paint );
		return new AndroidPixmap( bitmap );
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLineGap() {	
		return (int)(Math.ceil(metrics.leading));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLineHeight() {	
		return (int)Math.ceil(Math.abs(metrics.ascent) + Math.abs(metrics.descent));
	}

	/**
	 * {@inheritDoc}
	 */
	Rect tmpRect = new Rect();
	@Override	
	public int getStringWidth(String text) 
	{				
		paint.getTextBounds(text, 0, text.length(), tmpRect);
		return tmpRect.width();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getGlyphBounds(char character, Rectangle rect) {		
		paint.getTextBounds( "" + character, 0, 1, tmpRect );
		rect.setWidth(tmpRect.width() + 5);
		rect.setHeight(getLineHeight());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6420.java