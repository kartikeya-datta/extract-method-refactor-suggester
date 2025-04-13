error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1822.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1822.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1822.java
text:
```scala
C@@opyright (c) 2003 IBM Corporation and others.

/************************************************************************
Copyright (c) 2002 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal.commands.gestures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.internal.commands.*;

public final class Capture {

	public static Capture create() {
		return new Capture();
	}		

	private List captureListeners;
	private boolean capturing;
	private Control control;
	private int data;
	private MouseListener mouseListener;
	private MouseMoveListener mouseMoveListener;
	private int pen;
	private List points;	
	
	private Capture() {
		super();
		captureListeners = new ArrayList();

		mouseListener = new MouseListener() {
			public void mouseDoubleClick(MouseEvent mouseEvent) {
			}

			public void mouseDown(MouseEvent mouseEvent) {
				if (!capturing) {
					capturing = true;
					data = mouseEvent.stateMask;
					pen = mouseEvent.button;
					points.clear();
					points.add(Point.create(mouseEvent.x, mouseEvent.y));
					control.addMouseMoveListener(mouseMoveListener);
				}
			}

			public void mouseUp(MouseEvent mouseEvent) {
				if (capturing && mouseEvent.button == pen) {
					control.removeMouseMoveListener(mouseMoveListener);
					points.add(Point.create(mouseEvent.x, mouseEvent.y));
					Gesture gesture = Gesture.create(data, pen, (Point[]) points.toArray(new Point[points.size()]));
					capturing = false;
					data = 0;
					pen = 0;
					points.clear();
					Iterator iterator = captureListeners.iterator();

					while (iterator.hasNext())
						((CaptureListener) iterator.next()).gesture(gesture);
				}
			}
		};

		mouseMoveListener = new MouseMoveListener() {
			public void mouseMove(MouseEvent mouseEvent) {
				if (capturing)
					points.add(Point.create(mouseEvent.x, mouseEvent.y));
			}
		};	
		
		points = new ArrayList();
	}

	public void addCaptureListener(CaptureListener captureListener) {
		captureListeners.add(captureListener);	
	}

	public Control getControl() {
		return control;	
	}

	public void removeCaptureListener(CaptureListener captureListener) {
		captureListeners.remove(captureListener);			
	}

	public void setControl(Control control) {
		if (this.control != control) {
			if (this.control != null) {
				control.removeMouseMoveListener(mouseMoveListener);
				control.removeMouseListener(mouseListener);
			}
			
			this.control = control;
			capturing = false;
			data = 0;
			pen = 0;
			points.clear();

			if (this.control != null)
				control.addMouseListener(mouseListener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1822.java