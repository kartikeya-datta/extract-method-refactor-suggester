error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/287.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/287.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/287.java
text:
```scala
a@@dd(contentTable = new Table(skin)).expand();


package com.badlogic.gdx.scenes.scene2d.ui;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;

/** Displays a dialog, which is a modal window containing a content table with a button table underneath it. Methods are provided
 * to add a label to the content table and buttons to the button table, but any widgets can be added. When a button is clicked,
 * {@link #result(Object)} is called and the dialog is removed from the stage.
 * @author Nathan Sweet */
public class Dialog extends Window {
	/** The time in seconds that dialogs will fade in and out. Set to zero to disable fading. */
	static public float fadeDuration = 0.4f;

	Table contentTable, buttonTable;
	private Skin skin;
	ObjectMap<Actor, Object> values = new ObjectMap();

	public Dialog (String title, Skin skin) {
		super(title, skin.get(WindowStyle.class));
		this.skin = skin;
		initialize();
	}

	public Dialog (String title, Skin skin, String windowStyleName) {
		super(title, skin.get(windowStyleName, WindowStyle.class));
		this.skin = skin;
		initialize();
	}

	public Dialog (String title, WindowStyle windowStyle) {
		super(title, windowStyle);
		initialize();
	}

	private void initialize () {
		setModal(true);

		defaults().space(6);
		add(contentTable = new Table(skin)).expand().fill();
		row();
		add(buttonTable = new Table(skin));

		contentTable.defaults().space(6);
		buttonTable.defaults().space(6);

		buttonTable.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				while (actor.getParent() != buttonTable)
					actor = actor.getParent();
				result(values.get(actor));
				hide();
			}
		});
	}

	public void draw (SpriteBatch batch, float parentAlpha) {
		Stage stage = getStage();
		if (stage.getKeyboardFocus() == null) stage.setKeyboardFocus(this);
		super.draw(batch, parentAlpha);
	}

	public Table getContentTable () {
		return contentTable;
	}

	public Table getButtonTable () {
		return buttonTable;
	}

	/** Adds a label to the content table. The dialog must have been constructed with a skin to use this method. */
	public Dialog text (String text) {
		if (skin == null)
			throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
		return text(text, skin.get(LabelStyle.class));
	}

	/** Adds a label to the content table. */
	public Dialog text (String text, LabelStyle labelStyle) {
		return text(new Label(text, labelStyle));
	}

	/** Adds the given Label to the content table */
	public Dialog text (Label label) {
		contentTable.add(label);
		return this;
	}

	/** Adds a text button to the button table. Null will be passed to {@link #result(Object)} if this button is clicked. The dialog
	 * must have been constructed with a skin to use this method. */
	public Dialog button (String text) {
		return button(text, null);
	}

	/** Adds a text button to the button table. The dialog must have been constructed with a skin to use this method.
	 * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
	public Dialog button (String text, Object object) {
		if (skin == null)
			throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
		return button(text, object, skin.get(TextButtonStyle.class));
	}

	/** Adds a text button to the button table.
	 * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
	public Dialog button (String text, Object object, TextButtonStyle buttonStyle) {
		return button(new TextButton(text, buttonStyle), object);
	}

	/** Adds the given button to the button table. */
	public Dialog button (Button button) {
		return button(button, null);
	}

	/** Adds the given button to the button table.
	 * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
	public Dialog button (Button button, Object object) {
		buttonTable.add(button);
		setObject(button, object);
		return this;
	}

	/** {@link #pack() Packs} the dialog and adds it to the stage, centered. */
	public Dialog show (Stage stage) {
		stage.setKeyboardFocus(this);
		stage.setScrollFocus(this);
		pack();
		setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
		stage.addActor(this);
		if (fadeDuration > 0) {
			getColor().a = 0;
			addAction(Actions.fadeIn(fadeDuration, Interpolation.fade));
		}
		return this;
	}

	/** Hides the dialog. Called automatically when a button is clicked. The default implementation fades out the dialog over
	 * {@link #fadeDuration} seconds and then removes it from the stage. */
	public void hide () {
		addAction(sequence(fadeOut(fadeDuration, Interpolation.fade), Actions.removeActor()));
	}

	public void setObject (Actor actor, Object object) {
		values.put(actor, object);
	}

	/** If this key is pressed, {@link #result(Object)} is called with the specified object.
	 * @see Keys */
	public Dialog key (final int keycode, final Object object) {
		addListener(new InputListener() {
			public boolean keyDown (InputEvent event, int keycode2) {
				if (keycode == keycode2) {
					result(object);
					hide();
				}
				return false;
			}
		});
		return this;
	}

	/** Called when a button is clicked.
	 * @param object The object specified when the button was added. */
	protected void result (Object object) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/287.java