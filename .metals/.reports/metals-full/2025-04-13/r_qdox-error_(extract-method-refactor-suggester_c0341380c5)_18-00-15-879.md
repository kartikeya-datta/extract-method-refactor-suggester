error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6664.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6664.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6664.java
text:
```scala
protected C@@lickListener listener;


package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Button extends Table {
	public ButtonStyle style;
	public boolean isChecked;

	ClickListener listener;

	public Button (Skin skin) {
		this(skin.getStyle(ButtonStyle.class), null);
	}

	public Button (ButtonStyle style) {
		this(style, null);
	}

	public Button (Actor child, Skin skin) {
		this(child, skin.getStyle(ButtonStyle.class));
	}

	public Button (Actor child, ButtonStyle style) {
		this(style, null);
		add(child);
	}

	public Button (String text, Skin skin) {
		this(skin.getStyle(ButtonStyle.class), null);
		setText(text);
	}

	public Button (String text, ButtonStyle style) {
		this(style, null);
		setText(text);
	}

	public Button (String text, ButtonStyle style, String name) {
		this(style, name);
		setText(text);
	}

	public Button (ButtonStyle style, String name) {
		super(name);
		setStyle(style);
		super.setClickListener(new ClickListener() {
			public void click (Actor actor) {
				isChecked = !isChecked;
				if (listener != null) listener.click(actor);
			}
		});
	}

	public void setStyle (ButtonStyle style) {
		this.style = style;
		setBackground(isPressed ? style.down : style.up);
	}

	public void setClickListener (ClickListener listener) {
		this.listener = listener;
	}

	public void setText (String text) {
		if (children.isEmpty()) {
			add(new Label(text, style));
			return;
		}
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			if (child instanceof Label) {
				((Label)child).setText(text);
				return;
			}
		}
		throw new GdxRuntimeException("No child label was found.");
	}

	public String getText () {
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			if (child instanceof Label) return ((Label)child).getText();
		}
		throw new GdxRuntimeException("No child label was found.");
	}

	public void draw (SpriteBatch batch, float parentAlpha) {
		float offsetX = 0, offsetY = 0;
		if (isPressed) {
			setBackground(style.down == null ? style.up : style.down);
			offsetX = style.pressedOffsetX;
			offsetY = style.pressedOffsetY;
		} else {
			if (style.checked == null)
				setBackground(style.up);
			else
				setBackground(isChecked ? style.checked : style.up);
			offsetX = style.unpressedOffsetX;
			offsetY = style.unpressedOffsetY;
		}
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			child.x += offsetX;
			child.y += offsetY;
		}
		super.draw(batch, parentAlpha);
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			child.x -= offsetX;
			child.y -= offsetY;
		}
	}

	/** Defines a button style, see {@link Button}
	 * @author mzechner */
	static public class ButtonStyle extends LabelStyle {
		public NinePatch down;
		public NinePatch up;
		public NinePatch checked;
		public float pressedOffsetX, pressedOffsetY;
		public float unpressedOffsetX, unpressedOffsetY;

		public ButtonStyle () {
		}

		public ButtonStyle (NinePatch down, NinePatch up, NinePatch checked, float pressedOffsetX, float pressedOffsetY,
			float unpressedOffsetX, float unpressedOffsetY, BitmapFont font, Color fontColor) {
			super(font, fontColor);
			this.down = down;
			this.up = up;
			this.checked = checked;
			this.pressedOffsetX = pressedOffsetX;
			this.pressedOffsetY = pressedOffsetY;
			this.unpressedOffsetX = unpressedOffsetX;
			this.unpressedOffsetY = unpressedOffsetY;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6664.java