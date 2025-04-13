error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8056.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8056.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8056.java
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.commands.*;

public final class Util {

	public final static char EAST = 'E';
	public final static char NORTH = 'N';
	public final static char SOUTH = 'S';
	public final static char WEST = 'W';

	public static String keys(int data) {
		int count = 0;
		StringBuffer stringBuffer = new StringBuffer();		

		if ((data & SWT.ALT) > 0) {
			if (count > 0)
				stringBuffer.append(',');
	
			stringBuffer.append("alt");
			count++;
		}

		if ((data & SWT.COMMAND) > 0) {
			if (count > 0)
				stringBuffer.append(',');
	
			stringBuffer.append("command");
			count++;
		}

		if ((data & SWT.CTRL) > 0) {
			if (count > 0)
				stringBuffer.append(',');
	
			stringBuffer.append("ctrl");
			count++;
		}

		if ((data & SWT.SHIFT) > 0) {
			if (count > 0)
				stringBuffer.append(',');
	
			stringBuffer.append("shift");
			count++;
		}
	
		if (count == 0)
			stringBuffer.append("none");
			
		return stringBuffer.toString();
	}

	public static String recognize(Point[] points, int grid) {
		char c = 0;
		StringBuffer stringBuffer = new StringBuffer();
		int x0 = 0;
		int y0 = 0;

		for (int i = 0; i < points.length; i++) {
			Point point = points[i];

			if (i == 0) {
				x0 = point.getX();
				y0 = point.getY();
				continue;
			}

			int x1 = point.getX();
			int y1 = point.getY();
			int dx = (x1 - x0) / grid;
			int dy = (y1 - y0) / grid;

			if ((dx != 0) || (dy != 0)) {
				if (dx > 0 && c != EAST) {
					stringBuffer.append(c = EAST);
				} else if (dx < 0 && c != WEST) {
					stringBuffer.append(c = WEST);
				} else if (dy > 0 && c != SOUTH) {
					stringBuffer.append(c = SOUTH);
				} else if (dy < 0 && c != NORTH) {
					stringBuffer.append(c = NORTH);
				}

				x0 = x1;
				y0 = y1;
			}
		}

		return stringBuffer.toString();
	}
	
	public static void main(String[] args) {
		final int HEIGHT = 300;
		final int WIDTH = 400;

		Display display = new Display();
		Rectangle bounds = display.getBounds();
		Shell shell = new Shell(display);

		if (bounds.height >= HEIGHT && bounds.width >= WIDTH)
			shell.setBounds((bounds.x + bounds.width - WIDTH) / 2, (bounds.y + bounds.height - HEIGHT) / 2, WIDTH, HEIGHT);

		shell.setText(Util.class.getName());
		shell.open();
		Capture capture = Capture.create();

		capture.addCaptureListener(new CaptureListener() {
			public void gesture(Gesture gesture) {
				System.out.println("Pen: " + gesture.getPen() + "   Keys: " + keys(gesture.getData()) +  "   Points: " + gesture.getPoints().length +
					"   Gesture: " + recognize(gesture.getPoints(), 20));			
			}
		});

		capture.setControl(shell);

		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();

		display.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8056.java