error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9674.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9674.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9674.java
text:
```scala
private i@@nt LONG_DELAY = 300;

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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ControlAnimator;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.progress.UIJob;

/**
 * Animates a control by sliding it up or down. The animation is
 * achieved using the UI's job functionality and by incrementally 
 * decreasing or increasing the control's y coordinate.
 * 
 * @since 3.2
 */
public class WorkbenchControlAnimator extends ControlAnimator {
	
	private UIJob slideJob;
	private Control control;
	private int endY;
	private boolean finished;
	private boolean inTransition = false;
	
	private int LONG_DELAY = 1000;
	private int SHORT_DELAY = 25;

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.ControlAnimator#setVisible(boolean, org.eclipse.swt.widgets.Control)
	 */
	public void setVisible(boolean visible,Control control) {
		this.control = control;
		finished = false;

		control.setVisible(true);
		
		Rectangle parentBounds = control.getParent().getBounds();
		int bottom = parentBounds.height;
		endY = visible ? bottom - control.getBounds().height
				: bottom;
		
		if(slideJob != null)
			slideJob.cancel();
		
		slideJob = getSlideJob();
		control.addDisposeListener(new DisposeListener(){
			public void widgetDisposed(org.eclipse.swt.events.DisposeEvent e) {
				slideJob = null;
			}
		});		
		
		// Wait before displaying the control to allow for opening 
		// condition to change, but no waiting before closing the control.
		if(getAnimationState() == OPENING && !inTransition){
			slideJob.schedule(LONG_DELAY);
		} else {
			slideJob.schedule(SHORT_DELAY);
		}
		
	}
	
	/**
	 * Creates a job in the UI thread to display or hide the control
	 * by increasing or decreasing the y coordinate and setting the new 
	 * location. The job will continually re-schedule itself until the
	 * the control is no longer in transition and will also stop
	 * if the monitor is canceled or the control is disposed.
	 * 
	 * @return the UIJob responsible for opening or closing the control
	 */
	private UIJob getSlideJob(){
		UIJob newSlideJob = new UIJob("Sliding Message") { //$NON-NLS-1$
			public IStatus runInUIThread(IProgressMonitor monitor) {
				if(!monitor.isCanceled() && !control.isDisposed()){
					Point loc = control.getLocation();
					switch (getAnimationState()) {
					case OPENING:
						loc.y--;
						if (loc.y >= endY) {
							control.setLocation(loc);
						} else {
							finished = true;
							setAnimationState(OPEN);
						}
						break;
					case CLOSING:
						loc.y++;
						if (loc.y <= endY) {
							control.setLocation(loc);
						} else {
							finished = true;
							setAnimationState(CLOSED);
							control.setVisible(false);
						}
						break;
					default:
						break;
					}
					if(!finished) {
						inTransition = true;
						slideJob.schedule(5);					
					} else
						inTransition = false;
					return Status.OK_STATUS;		
				}
				return Status.CANCEL_STATUS;
			}		
		};
		newSlideJob.setSystem(true);
		return newSlideJob;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9674.java