error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2011.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2011.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2011.java
text:
```scala
i@@f (!target.actor.isAscendantOf(hit)) continue;


package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import javax.swing.tree.DefaultTreeCellEditor.EditorContainer;

/** Manages drag and drop operations through registered drag sources and drop targets.
 * @author Nathan Sweet */
public class DragAndDrop {
	Source source;
	Payload payload;
	Actor dragActor;
	Target target;
	boolean isValidTarget;
	Array<Target> targets = new Array();
	private float tapSquareSize = 8;
	private int button;
	float dragActorX = 14, dragActorY = -20;
	long dragStartTime;
	int dragTime = 250;

	public void addSource (final Source source) {
		DragListener listener = new DragListener() {
			public void dragStart (InputEvent event, float x, float y, int pointer) {
				dragStartTime = System.currentTimeMillis();
				payload = source.dragStart(event, getTouchDownX(), getTouchDownY(), pointer);
				event.stop();
			}

			public void drag (InputEvent event, float x, float y, int pointer) {
				if (payload == null) return;
				Stage stage = event.getStage();

				// Find target.
				Target newTarget = null;
				isValidTarget = false;
				Actor hit = event.getStage().hit(event.getStageX(), event.getStageY(), true); // Prefer touchable actors.
				if (hit == null) hit = event.getStage().hit(event.getStageX(), event.getStageY(), false);
				if (hit != null) {
					for (int i = 0, n = targets.size; i < n; i++) {
						Target target = targets.get(i);
						if (!target.actor.isAscendant(hit)) continue;
						newTarget = target;
						target.actor.stageToLocalCoordinates(Vector2.tmp.set(event.getStageX(), event.getStageY()));
						isValidTarget = target.drag(source, payload, Vector2.tmp.x, Vector2.tmp.y, pointer);
						break;
					}
				}
				if (newTarget != target) {
					if (target != null) target.reset(source, payload);
					target = newTarget;
				}

				// Add/remove and position the drag actor.
				Actor actor = null;
				if (target != null) actor = isValidTarget ? payload.validDragActor : payload.invalidDragActor;
				if (actor == null) actor = payload.dragActor;
				if (actor == null) return;
				if (dragActor != actor) {
					if (dragActor != null) dragActor.remove();
					dragActor = actor;
					stage.addActor(actor);
				}
				float actorX = event.getStageX() + dragActorX;
				float actorY = event.getStageY() + dragActorY - actor.getHeight();
				if (actorX < 0) actorX = 0;
				if (actorY < 0) actorY = 0;
				if (actorX + actor.getWidth() > stage.getWidth()) actorX = stage.getWidth() - actor.getWidth();
				if (actorY + actor.getHeight() > stage.getHeight()) actorY = stage.getHeight() - actor.getHeight();
				actor.setPosition(actorX, actorY);
			}

			public void dragStop (InputEvent event, float x, float y, int pointer) {
				if (payload == null) return;
				if (System.currentTimeMillis() - dragStartTime < dragTime) isValidTarget = false;
				if (dragActor != null) dragActor.remove();
				if (isValidTarget) target.drop(source, payload);
				source.dragStop(event, x, y, pointer, isValidTarget ? target : null);
				if (target != null) target.reset(source, payload);
				DragAndDrop.this.source = null;
				payload = null;
				target = null;
				isValidTarget = false;
				dragActor = null;
			}
		};
		listener.setTapSquareSize(tapSquareSize);
		listener.setButton(button);
		source.actor.addCaptureListener(listener);
	}

	public void addTarget (Target target) {
		targets.add(target);
	}

	/** Sets the distance a touch must travel before being considered a drag. */
	public void setTapSquareSize (float halfTapSquareSize) {
		tapSquareSize = halfTapSquareSize;
	}

	/** Sets the button to listen for, all other buttons are ignored. Default is {@link Buttons#LEFT}. Use -1 for any button. */
	public void setButton (int button) {
		this.button = button;
	}

	public void setDragActorPosition (float dragActorX, float dragActorY) {
		this.dragActorX = dragActorX;
		this.dragActorY = dragActorY;
	}

	public boolean isDragging () {
		return payload != null;
	}

	/** Returns the current drag actor, or null. */
	public Actor getDragActor () {
		return dragActor;
	}

	/** Time in milliseconds that a drag must take before a drop will be considered valid. This ignores an accidental drag and drop
	 * that was meant to be a click. Default is 250. */
	public void setDragTime (int dragMillis) {
		this.dragTime = dragMillis;
	}

	/** A target where a payload can be dragged from.
	 * @author Nathan Sweet */
	static abstract public class Source {
		final Actor actor;

		public Source (Actor actor) {
			if (actor == null) throw new IllegalArgumentException("actor cannot be null.");
			this.actor = actor;
		}

		/** @return May be null. */
		abstract public Payload dragStart (InputEvent event, float x, float y, int pointer);

		/** @param target null if not dropped on a valid target. */
		public void dragStop (InputEvent event, float x, float y, int pointer, Target target) {
		}

		public Actor getActor () {
			return actor;
		}
	}

	/** A target where a payload can be dropped to.
	 * @author Nathan Sweet */
	static abstract public class Target {
		final Actor actor;

		public Target (Actor actor) {
			if (actor == null) throw new IllegalArgumentException("actor cannot be null.");
			this.actor = actor;
		}

		/** Called when the object is dragged over the target. The coordinates are in the target's local coordinate system.
		 * @return true if this is a valid target for the object. */
		abstract public boolean drag (Source source, Payload payload, float x, float y, int pointer);

		/** Called when the object is no longer over the target, whether because the touch was moved or a drop occurred. */
		public void reset (Source source, Payload payload) {
		}

		abstract public void drop (Source source, Payload payload);

		public Actor getActor () {
			return actor;
		}
	}

	/** The payload of a drag and drop operation. Actors can be optionally provided to follow the cursor and change when over a
	 * target. */
	static public class Payload {
		Actor dragActor, validDragActor, invalidDragActor;
		Object object;

		public void setDragActor (Actor dragActor) {
			this.dragActor = dragActor;
		}

		public Actor getDragActor () {
			return dragActor;
		}

		public void setValidDragActor (Actor validDragActor) {
			this.validDragActor = validDragActor;
		}

		public Actor getValidDragActor () {
			return validDragActor;
		}

		public void setInvalidDragActor (Actor invalidDragActor) {
			this.invalidDragActor = invalidDragActor;
		}

		public Actor getInvalidDragActor () {
			return invalidDragActor;
		}

		public Object getObject () {
			return object;
		}

		public void setObject (Object object) {
			this.object = object;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2011.java