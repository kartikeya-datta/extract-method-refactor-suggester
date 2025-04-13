error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6500.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6500.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6500.java
text:
```scala
e@@vent.getListenerActor().stageToLocalCoordinates(coords);


package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.math.Vector2;

/** Listener for actor input events. */
public class ActorListener implements EventListener {
	public boolean handle (Event e) {
		if (!(e instanceof ActorEvent)) return false;
		ActorEvent event = (ActorEvent)e;

		switch (event.getType()) {
		case keyDown:
			return keyDown(event, event.getKeyCode());
		case keyUp:
			return keyUp(event, event.getKeyCode());
		case keyTyped:
			return keyTyped(event, event.getCharacter());
		case scrolled:
			return scrolled(event, event.getScrollAmount());
		}

		Vector2 coords = Vector2.tmp.set(event.getStageX(), event.getStageY());
		event.getCurrentTarget().stageToLocalCoordinates(coords);

		switch (event.getType()) {
		case touchDown:
			return touchDown(event, coords.x, coords.y, event.getPointer(), event.getButton());
		case touchUp:
			touchUp(event, coords.x, coords.y, event.getPointer(), event.getButton());
			return true;
		case touchDragged:
			touchDragged(event, coords.x, coords.y, event.getPointer());
			return true;
		case mouseMoved:
			mouseMoved(event, coords.x, coords.y);
			return false;
		case enter:
			enter(event, coords.x, coords.y, event.getPointer(), event.getRelatedActor());
			return false;
		case exit:
			exit(event, coords.x, coords.y, event.getPointer(), event.getRelatedActor());
			return false;
		}
		return false;
	}

	/** Called when a mouse button or a finger touch goes down on the actor. If true is returned, this listener will receive all
	 * touchDragged and touchUp events, even those not over this actor, until touchUp is received. Also when true is returned, the
	 * event is {@link Event#handle() handled}.
	 * @see ActorEvent */
	public boolean touchDown (ActorEvent event, float x, float y, int pointer, int button) {
		return false;
	}

	/** Called when a mouse button or a finger touch goes up anywhere, but only if touchDown previously returned true for the mouse
	 * button or touch. The touchUp event is always {@link Event#handle() handled}.
	 * @see ActorEvent */
	public void touchUp (ActorEvent event, float x, float y, int pointer, int button) {
	}

	/** Called when a mouse button or a finger touch is moved anywhere, but only if touchDown previously returned true for the mouse
	 * button or touch. The touchDragged event is always {@link Event#handle() handled}.
	 * @see ActorEvent */
	public void touchDragged (ActorEvent event, float x, float y, int pointer) {
	}

	/** Called any time the mouse is moved when a button is not down. This event only occurs on the desktop. When true is returned,
	 * the event is {@link Event#handle() handled}.
	 * @see ActorEvent */
	public boolean mouseMoved (ActorEvent event, float x, float y) {
		return false;
	}

	/** Called any time the mouse cursor or a finger touch is moved over an actor. On the desktop, this event occurs even when no
	 * mouse buttons are pressed.
	 * @see ActorEvent */
	public void enter (ActorEvent event, float x, float y, int pointer, Actor fromActor) {
	}

	/** Called any time the mouse cursor or a finger touch is moved out of an actor. On the desktop, this event occurs even when no
	 * mouse buttons are pressed.
	 * @see ActorEvent */
	public void exit (ActorEvent event, float x, float y, int pointer, Actor toActor) {
	}

	/** Called when the mouse wheel has been scrolled. When true is returned, the event is {@link Event#handle() handled}. */
	public boolean scrolled (ActorEvent event, int amount) {
		return false;
	}

	/** Called when a key goes down. When true is returned, the event is {@link Event#handle() handled}. */
	public boolean keyDown (ActorEvent event, int keycode) {
		return false;
	}

	/** Called when a key goes up. When true is returned, the event is {@link Event#handle() handled}. */
	public boolean keyUp (ActorEvent event, int keycode) {
		return false;
	}

	/** Called when a key is typed. When true is returned, the event is {@link Event#handle() handled}. */
	public boolean keyTyped (ActorEvent event, char character) {
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6500.java