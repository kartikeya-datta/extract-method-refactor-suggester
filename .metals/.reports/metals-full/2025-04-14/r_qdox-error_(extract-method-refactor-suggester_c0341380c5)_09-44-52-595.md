error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/620.java
text:
```scala
public A@@ctor getTarget () {


package com.badlogic.gdx.scenes.scene2d;

import com.badlogic.gdx.utils.Pool.Poolable;

/** The base class for all events.
 * @see ActorEvent
 * @see Actor#fire(Event) */
public class Event implements Poolable {
	private Stage stage;
	private Actor targetActor; // deepest actor hit
	private Actor currentTarget; // current actor being notified about event
	private boolean capture;
	private boolean bubbles = true; // true means propagate to target's parents
	private boolean handled; // true means the event was handled (the stage will eat the input)
	private boolean stopped; // true means event propagation was stopped
	private boolean cancelled; // true means any action that this event would cause should not happen

	/** Marks this event has being handled. This does not affect event propagation inside scene2d, but causes the {@link Stage}
	 * event methods to return false, which will eat the event so it is not passed on to the application under the stage. */
	public void handle () {
		handled = true;
	}

	/** Marks this event has being cancelled. This {@link #handle() handles} the event and {@link #stop() stops} the event
	 * propagation. It also cancels any default action that would have been taken by the code that fired the event. Eg, if the
	 * event is for a checkbox being checked, cancelling the event could uncheck the checkbox. */
	public void cancel () {
		cancelled = true;
		stopped = true;
		handled = true;
	}

	/** Marks this event has being stopped. This halts event propagation. Any other listeners on the {@link #getCurrentTarget()
	 * current target} are notified, but after that no other listeners are notified. */
	public void stop () {
		stopped = true;
	}

	public void reset () {
		stage = null;
		targetActor = null;
		currentTarget = null;
		capture = false;
		bubbles = true;
		handled = false;
		stopped = false;
		cancelled = false;
	}

	/** Returns the actor that the event originated from. */
	public Actor getTargetActor () {
		return targetActor;
	}

	public void setTarget (Actor targetActor) {
		this.targetActor = targetActor;
	}

	/** Returns the actor that this listener is attached to. */
	public Actor getCurrentTarget () {
		return currentTarget;
	}

	public void setCurrentTarget (Actor currentTarget) {
		this.currentTarget = currentTarget;
	}

	public boolean getBubbles () {
		return bubbles;
	}

	/** If true, after the event is fired on the target actor, it will also be fired on each of the parent actors, all the way to
	 * the root. */
	public void setBubbles (boolean bubbles) {
		this.bubbles = bubbles;
	}

	/** {@link #handle()} */
	public boolean isHandled () {
		return handled;
	}

	/** @see #stop() */
	public boolean isStopped () {
		return stopped;
	}

	/** @see #cancel() */
	public boolean isCancelled () {
		return cancelled;
	}

	public void setCapture (boolean capture) {
		this.capture = capture;
	}

	/** If true, the event was fired during the capture phase.
	 * @see Actor#fire(Event) */
	public boolean isCapture () {
		return capture;
	}

	public void setStage (Stage stage) {
		this.stage = stage;
	}

	/** The stage for the actor the event was fired on. */
	public Stage getStage () {
		return stage;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/620.java