error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7873.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7873.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7873.java
text:
```scala
r@@eturn (DropTarget)widget.getData(DND.DROP_TARGET_KEY);

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.carbon.DataBrowserCallbacks;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.widgets.*;

/**
 * This class provides a default drag under effect (eg. select, insert, scroll and expand) 
 * when a drag occurs over a <code>Tree</code>.
 * 
 * <p>Classes that wish to provide their own drag under effect for a <code>Tree</code>
 * can extend the <code>TreeDropTargetEffect</code> class and override any applicable methods 
 * in <code>TreeDropTargetEffect</code> to display their own drag under effect.</p>
 *
 * Subclasses that override any methods of this class must call the corresponding
 * <code>super</code> method to get the default drag under effect implementation.
 *
 * <p>The feedback value is either one of the FEEDBACK constants defined in 
 * class <code>DND</code> which is applicable to instances of this class, 
 * or it must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> effect constants. 
 * </p>
 * <p>
 * <dl>
 * <dt><b>Feedback:</b></dt>
 * <dd>FEEDBACK_SELECT, FEEDBACK_INSERT_BEFORE, FEEDBACK_INSERT_AFTER, FEEDBACK_EXPAND, FEEDBACK_SCROLL</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles FEEDBACK_SELECT, FEEDBACK_INSERT_BEFORE or
 * FEEDBACK_INSERT_AFTER may be specified.
 * </p>
 * 
 * @see DropTargetAdapter
 * @see DropTargetEvent
 * 
 * @since 3.3
 */
public class TreeDropTargetEffect extends DropTargetEffect {
	static final int SCROLL_HYSTERESIS = 150; // milli seconds
	static final int EXPAND_HYSTERESIS = 1000; // milli seconds

	int currentEffect = DND.FEEDBACK_NONE;
	TreeItem currentItem;

	TreeItem insertItem = null;
	boolean insertBefore = false;

	TreeItem scrollItem;
	long scrollBeginTime;

	TreeItem expandItem;
	long expandBeginTime;
	
	DataBrowserCallbacks callbacks = null;
	
	int acceptDragProc(int theControl, int itemID, int property, int theRect, int theDrag) {
		return (currentEffect & DND.FEEDBACK_SELECT) != 0 ? 1 : 0;
	}

	static Callback AcceptDragProc;
	static {
		AcceptDragProc = new Callback(TreeDropTargetEffect.class, "AcceptDragProc", 5); //$NON-NLS-1$
		int acceptDragProc = AcceptDragProc.getAddress();
		if (acceptDragProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	}

	static int AcceptDragProc(int theControl, int itemID, int property, int theRect, int theDrag) {
		DropTarget target = FindDropTarget(theControl, theDrag);
		if (target == null) return 0;
		return (target.feedback & DND.FEEDBACK_SELECT) != 0 ? 1 : 0;
	}
	
	static DropTarget FindDropTarget(int theControl, int theDrag) {
		if (theControl == 0) return null;
		Display display = Display.findDisplay(Thread.currentThread());
		if (display == null || display.isDisposed()) return null;
		Widget widget = display.findWidget(theControl);
		if (widget == null) return null;
		return (DropTarget)widget.getData(DropTarget.DROPTARGETID); 
	}

	/**
	 * Creates a new <code>TreeDropTargetEffect</code> to handle the drag under effect on the specified 
	 * <code>Tree</code>.
	 * 
	 * @param tree the <code>Tree</code> over which the user positions the cursor to drop the data
	 */
	public TreeDropTargetEffect(Tree tree) {
		super(tree);
	}

	int checkEffect(int effect) {
		// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
		if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
		if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
		return effect;
	}

	/**
	 * This implementation of <code>dragEnter</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>.
	 * 
	 * For additional information see <code>DropTargetAdapter.dragEnter</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragEnter(event)</code>
	 * to get the default drag under effect implementation.
	 *
	 * @param event  the information associated with the drag enter event
	 * 
	 * @see DropTargetAdapter
	 * @see DropTargetEvent
	 */
	public void dragEnter(DropTargetEvent event) {
		if (callbacks == null) {
			Tree table = (Tree) control;
			DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
			OS.GetDataBrowserCallbacks (table.handle, callbacks);
			callbacks.v1_acceptDragCallback = AcceptDragProc.getAddress();
			OS.SetDataBrowserCallbacks(table.handle, callbacks);
		}
		insertItem = null;
		expandBeginTime = 0;
		expandItem = null;
		scrollBeginTime = 0;
		scrollItem = null;
	}

	/**
	 * This implementation of <code>dragLeave</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>.
	 * 
	 * For additional information see <code>DropTargetAdapter.dragLeave</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragLeave(event)</code>
	 * to get the default drag under effect implementation.
	 *
	 * @param event  the information associated with the drag leave event
	 * 
	 * @see DropTargetAdapter
	 * @see DropTargetEvent
	 */
	public void dragLeave(DropTargetEvent event) {
		Tree tree = (Tree) control;
		if (insertItem != null) {
			setInsertMark(tree, null, false);
			insertItem = null;
		}
		expandBeginTime = 0;
		expandItem = null;
		scrollBeginTime = 0;
		scrollItem = null;
	}

	/**
	 * This implementation of <code>dragOver</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>.
	 * 
	 * For additional information see <code>DropTargetAdapter.dragOver</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragOver(event)</code>
	 * to get the default drag under effect implementation.
	 *
	 * @param event  the information associated with the drag over event
	 * 
	 * @see DropTargetAdapter
	 * @see DropTargetEvent
	 * @see DND#FEEDBACK_SELECT
	 * @see DND#FEEDBACK_INSERT_BEFORE
	 * @see DND#FEEDBACK_INSERT_AFTER
	 * @see DND#FEEDBACK_SCROLL
	 */
	public void dragOver(DropTargetEvent event) {
		Tree tree = (Tree) control;
		TreeItem item = (TreeItem)getItem(tree, event.x, event.y);
		int effect = checkEffect(event.feedback);		
		if ((effect & DND.FEEDBACK_EXPAND) == 0) {
			expandBeginTime = 0;
			expandItem = null;
		} else {
			if (item != null && item.equals(expandItem) && expandBeginTime != 0) {
				if (System.currentTimeMillis() >= expandBeginTime) {
					if (item.getItemCount() > 0 && !item.getExpanded()) {
						Event e = new Event();
						e.x = event.x;
						e.y = event.y;
						e.item = item;
						e.time = (int) System.currentTimeMillis();
						tree.notifyListeners(SWT.Expand, e);
						if (item.isDisposed()) return;
						item.setExpanded(true);
					}
					expandBeginTime = 0;
					expandItem = null;
				}
			} else {
				expandBeginTime = System.currentTimeMillis() + EXPAND_HYSTERESIS;
				expandItem = item;
			}
		}
		
		if ((effect & DND.FEEDBACK_SCROLL) == 0) {
			scrollBeginTime = 0;
			scrollItem = null;
		} else {
			if (item != null && item.equals(scrollItem)  && scrollBeginTime != 0) {
				if (System.currentTimeMillis() >= scrollBeginTime) {
					Rectangle area = tree.getClientArea();
					int headerHeight = tree.getHeaderHeight();
					int itemHeight= tree.getItemHeight();
					Point pt = new Point(event.x, event.y);
					pt = tree.getDisplay().map(null, tree, pt);
					TreeItem nextItem = null;
					if (pt.y < area.y + headerHeight + 2 * itemHeight) {
						nextItem = previousItem(tree, item);
					}
					if (pt.y > area.y + area.height - 2 * itemHeight) {
						nextItem = nextItem(tree, item);
					}
					if (nextItem != null) tree.showItem(nextItem);
					scrollBeginTime = 0;
					scrollItem = null;
				}
			} else {
				scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
				scrollItem = item;
			}
		}
		
		if ((effect & DND.FEEDBACK_INSERT_AFTER) != 0 ||
			(effect & DND.FEEDBACK_INSERT_BEFORE) != 0) {
			if (currentItem != item || 
				 ((effect & DND.FEEDBACK_INSERT_AFTER) != (currentEffect & DND.FEEDBACK_INSERT_AFTER)) ||
				 ((effect & DND.FEEDBACK_INSERT_BEFORE) != (currentEffect & DND.FEEDBACK_INSERT_BEFORE))) { 
				setInsertMark(tree, item, (effect & DND.FEEDBACK_INSERT_BEFORE) != 0);
				currentEffect = effect;
				currentItem = item;
			}
		} else {
			setInsertMark(tree, null, false);
		}
		// save current effect for selection feedback
		((DropTarget)event.widget).feedback = effect;
	}

	void setInsertMark(Tree tree, TreeItem item, boolean before) {
		if (item == insertItem && before == insertBefore) return;
		insertItem = item;
		insertBefore = before;
		tree.setInsertMark(item, before);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7873.java