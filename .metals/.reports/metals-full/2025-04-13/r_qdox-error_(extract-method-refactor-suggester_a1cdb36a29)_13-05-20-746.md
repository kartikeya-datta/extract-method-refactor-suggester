error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6785.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6785.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6785.java
text:
```scala
f@@ont.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);

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
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * <h2>Functionality</h2>
 * A Label displays multi-line text.
 * 
 * <h2>Layout</h2>
 * The (preferred) width and height of a Label are derrived from the bounding box of the text contained in it. Use
 * {@link #setPrefSize(int, int)} to change this programmatically. If the text doesn't fit into the specified area
 * artifacts will be visible.
 * 
 * <h3>Style</h3>
 * A label is a {@link Widget} displaying text via a {@link BitmapFont} and a {@link Color}. The style is defined
 * via an instance of {@link LabelStyle}, which can be either done programmatically or via a {@link Skin}. A label's
 * text will always be rendered starting from the top edge of it's bouding box, define by its width and height, not 
 * the text's bounding box.
 * 
 * A Label's style definition in a skin XML file should look like this:
 * 
 * <pre>
 * {@code 
 * <label name="styleName" 
 *        font="fontName" 
 *        color="colorName"
 *                    />
 * }
 * </pre>
 * 
 * <ul>
 * <li>The <code>name</code> attribute defines the name of the style which you can later use with {@link Skin#newLabel(String, String, String)}.</li>
 * <li>The <code>font</code> attribute references a {@link BitmapFont} by name, to be used to render the label's text.</li>
 * <li>The <code>fontColor</code> attribute references a {@link Color} by name, to be used to render the label's text.</li>
 * </ul> 
 * 
 * @author mzechner
 *
 */
public class Label extends Widget {		
	final LabelStyle style;
	String label;
	final TextBounds bounds = new TextBounds();
	final Vector2 textPos = new Vector2();
	
	public Label(String name, String label, LabelStyle style) {
		super(name, 0, 0);
		this.style = style;
		this.label = label;		
		this.touchable = false;
		layout();
		this.width = prefWidth;
		this.height = prefHeight;
	}	

	@Override
	public void layout() {
		final BitmapFont font = style.font;
		
		bounds.set(font.getMultiLineBounds(label));
		bounds.height -= font.getDescent();
		prefWidth = bounds.width;
		prefHeight = bounds.height;
		textPos.x = 0;
		textPos.y = prefHeight;
		invalidated = false;
	}

	@Override
	protected void draw(SpriteBatch batch, float parentAlpha) {
		final BitmapFont font = style.font;
		final Color fontColor = style.fontColor;
		
		if(invalidated) layout();
		font.setColor(fontColor);		
		font.drawMultiLine(batch, label, x + textPos.x, y + height);		
	}

	@Override
	protected boolean touchDown(float x, float y, int pointer) {
		return false;
	}

	@Override
	protected boolean touchUp(float x, float y, int pointer) {
		return false;
	}

	@Override
	protected boolean touchDragged(float x, float y, int pointer) {
		return false;
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}
	
	public static class LabelStyle {
		public BitmapFont font;
		public Color fontColor;
		
		public LabelStyle(BitmapFont font, Color fontColor) {
			this.font = font;
			this.fontColor = fontColor;
		}
	}	
	
	public void setText(String text) {
		this.label = text;
		invalidateHierarchy();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6785.java