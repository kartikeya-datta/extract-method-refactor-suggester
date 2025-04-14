error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1102.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1102.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1102.java
text:
```scala
public v@@oid setText (CharSequence text) {

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
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

/** A button with a child {@link Image} and {@link Label}.
 * @see ImageButton
 * @see TextButton
 * @see Button
 * @author Nathan Sweet */
public class ImageTextButton extends Button {
	private final Image image;
	private final Label label;
	private ImageTextButtonStyle style;

	public ImageTextButton (String text, Skin skin) {
		this(text, skin.get(ImageTextButtonStyle.class));
		setSkin(skin);
	}

	public ImageTextButton (String text, Skin skin, String styleName) {
		this(text, skin.get(styleName, ImageTextButtonStyle.class));
		setSkin(skin);
	}

	public ImageTextButton (String text, ImageTextButtonStyle style) {
		super(style);
		this.style = style;

		defaults().space(3);

		image = new Image();
		image.setScaling(Scaling.fit);
		add(image);

		label = new Label(text, new LabelStyle(style.font, style.fontColor));
		label.setAlignment(Align.center);
		add(label);

		setStyle(style);

		setSize(getPrefWidth(), getPrefHeight());
	}

	public void setStyle (ButtonStyle style) {
		if (!(style instanceof ImageTextButtonStyle)) throw new IllegalArgumentException("style must be a ImageTextButtonStyle.");
		super.setStyle(style);
		this.style = (ImageTextButtonStyle)style;
		if (image != null) updateImage();
		if (label != null) {
			ImageTextButtonStyle textButtonStyle = (ImageTextButtonStyle)style;
			LabelStyle labelStyle = label.getStyle();
			labelStyle.font = textButtonStyle.font;
			labelStyle.fontColor = textButtonStyle.fontColor;
			label.setStyle(labelStyle);
		}
	}

	public ImageTextButtonStyle getStyle () {
		return style;
	}

	private void updateImage () {
		boolean isPressed = isPressed();
		if (isDisabled && style.imageDisabled != null)
			image.setDrawable(style.imageDisabled);
		else if (isPressed && style.imageDown != null)
			image.setDrawable(style.imageDown);
		else if (isChecked && style.imageChecked != null)
			image.setDrawable((style.imageCheckedOver != null && isOver()) ? style.imageCheckedOver : style.imageChecked);
		else if (isOver() && style.imageOver != null)
			image.setDrawable(style.imageOver);
		else if (style.imageUp != null) //
			image.setDrawable(style.imageUp);
	}

	public void draw (Batch batch, float parentAlpha) {
		updateImage();
		Color fontColor;
		if (isDisabled && style.disabledFontColor != null)
			fontColor = style.disabledFontColor;
		else if (isPressed() && style.downFontColor != null)
			fontColor = style.downFontColor;
		else if (isChecked && style.checkedFontColor != null)
			fontColor = (isOver() && style.checkedOverFontColor != null) ? style.checkedOverFontColor : style.checkedFontColor;
		else if (isOver() && style.overFontColor != null)
			fontColor = style.overFontColor;
		else
			fontColor = style.fontColor;
		if (fontColor != null) label.getStyle().fontColor = fontColor;
		super.draw(batch, parentAlpha);
	}

	public Image getImage () {
		return image;
	}

	public Cell getImageCell () {
		return getCell(image);
	}

	public Label getLabel () {
		return label;
	}

	public Cell getLabelCell () {
		return getCell(label);
	}

	public void setText (String text) {
		label.setText(text);
	}

	public CharSequence getText () {
		return label.getText();
	}

	/** The style for an image text button, see {@link ImageTextButton}.
	 * @author Nathan Sweet */
	static public class ImageTextButtonStyle extends TextButtonStyle {
		/** Optional. */
		public Drawable imageUp, imageDown, imageOver, imageChecked, imageCheckedOver, imageDisabled;

		public ImageTextButtonStyle () {
		}

		public ImageTextButtonStyle (Drawable up, Drawable down, Drawable checked, BitmapFont font) {
			super(up, down, checked, font);
		}

		public ImageTextButtonStyle (ImageTextButtonStyle style) {
			super(style);
			if (style.imageUp != null) this.imageUp = style.imageUp;
			if (style.imageDown != null) this.imageDown = style.imageDown;
			if (style.imageOver != null) this.imageOver = style.imageOver;
			if (style.imageChecked != null) this.imageChecked = style.imageChecked;
			if (style.imageCheckedOver != null) this.imageCheckedOver = style.imageCheckedOver;
			if (style.imageDisabled != null) this.imageDisabled = style.imageDisabled;
		}

		public ImageTextButtonStyle (TextButtonStyle style) {
			super(style);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1102.java