error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1202.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1202.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1202.java
text:
```scala
private static final i@@nt LINE_WIDTH = 1;

/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @since 3.3
 *
 */
public class DefaultAnimationFeedback {
	private static final int LINE_WIDTH = 2;
	
	private Display display;
	private Shell theShell;
	private Region shellRegion;
	
	private List startRects = new ArrayList();
	private List endRects = new ArrayList();
	
	public DefaultAnimationFeedback() {}

    /**
	 * @param parentShell
	 */
	public void initialize(Shell parentShell, Rectangle startRect, Rectangle endRect) {
		addStartRect(startRect);
		addEndRect(endRect);

		theShell = new Shell(parentShell, SWT.NO_TRIM | SWT.ON_TOP);
		display = theShell.getDisplay();
        Color color = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
        theShell.setBackground(color);

        // Ensure that the background won't show on the initial display
        shellRegion = new Region(display);
        theShell.setRegion(shellRegion);
	}
	
	public void addStartRect(Rectangle rect) {
		if (rect != null) {
			startRects.add(rect);
		}
	}
	
	public void addEndRect(Rectangle rect) {
		if (rect != null) {
			endRects.add(rect);
		}
	}
	
	public void renderStep(double amount) {
		if (shellRegion != null) {
        	shellRegion.dispose();
        	shellRegion = new Region(display);
        }

		// Iterate across the set of start/end rects
        Iterator startIter = startRects.iterator();
        Iterator endIter = endRects.iterator();
        while (startIter.hasNext()) {
            Rectangle start = (Rectangle) startIter.next();
            Rectangle end = (Rectangle) endIter.next();
            
			// Get the bounds of the interpolated rect
			Rectangle curRect = RectangleAnimation.interpolate(start, end, amount);
			
	        Rectangle rect = Geometry.toControl(theShell, curRect);
	        shellRegion.add(rect);
	        rect.x += LINE_WIDTH;
	        rect.y += LINE_WIDTH;
	        rect.width = Math.max(0, rect.width - 2 * LINE_WIDTH);
	        rect.height = Math.max(0, rect.height - 2 * LINE_WIDTH);
	
	        shellRegion.subtract(rect);
        }

        theShell.setRegion(shellRegion);
        
        display.update();
	}

	/**
	 * 
	 */
	public void dispose() {
		theShell.setVisible(false);
		theShell.dispose();
		shellRegion.dispose();
	}

	/**
	 * Perform any initialization you want to have happen -before- the
	 * amination starts
	 */
	public void jobInit() {
    	// Compute the shell's bounds
        Rectangle shellBounds = Geometry.copy((Rectangle) startRects.get(0));
        Iterator startIter = startRects.iterator();
        Iterator endIter = endRects.iterator();
        while (startIter.hasNext()) {
            shellBounds.add((Rectangle) startIter.next());
            shellBounds.add((Rectangle) endIter.next());
        }
        theShell.setBounds(shellBounds);
        
    	// Making the shell visible will be slow on old video cards, so only start
    	// the timer once it is visible.
    	theShell.setVisible(true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1202.java