error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4921.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4921.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4921.java
text:
```scala
final D@@isplay display = new Display ();

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
 
/*
 * Drag and Drop example snippet: drag leaf items in a tree
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet91 {

public static void main (String [] args) {
	
	final Display display = Display.getDefault ();
	final Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	final Tree tree = new Tree(shell, SWT.BORDER);
	for (int i = 0; i < 3; i++) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("item "+i);
		for (int j = 0; j < 3; j++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText("item "+i+" "+j);
			for (int k = 0; k < 3; k++) {
				TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
				subsubItem.setText("item "+i+" "+j+" "+k);
			}
		}
	}
	
	Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	
	final DragSource source = new DragSource (tree, operations);
	source.setTransfer(types);
	final TreeItem[] dragSourceItem = new TreeItem[1];
	source.addDragListener (new DragSourceListener () {
		public void dragStart(DragSourceEvent event) {
			TreeItem[] selection = tree.getSelection();
			if (selection.length > 0 && selection[0].getItemCount() == 0) {
				event.doit = true;
				dragSourceItem[0] = selection[0];
			} else {
				event.doit = false;
			}
		};
		public void dragSetData (DragSourceEvent event) {
			event.data = dragSourceItem[0].getText();
		}
		public void dragFinished(DragSourceEvent event) {
			if (event.detail == DND.DROP_MOVE)
				dragSourceItem[0].dispose();
				dragSourceItem[0] = null;
		}
	});

	DropTarget target = new DropTarget(tree, operations);
	target.setTransfer(types);
	target.addDropListener (new DropTargetAdapter() {
		public void dragOver(DropTargetEvent event) {
			event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
			if (event.item != null) {
				TreeItem item = (TreeItem)event.item;
				Point pt = display.map(null, tree, event.x, event.y);
				Rectangle bounds = item.getBounds();
				if (pt.y < bounds.y + bounds.height/3) {
					event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
				} else if (pt.y > bounds.y + 2*bounds.height/3) {
					event.feedback |= DND.FEEDBACK_INSERT_AFTER;
				} else {
					event.feedback |= DND.FEEDBACK_SELECT;
				}
			}
		}
		public void drop(DropTargetEvent event) {
			if (event.data == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			String text = (String)event.data;
			if (event.item == null) {
				TreeItem item = new TreeItem(tree, SWT.NONE);
				item.setText(text);
			} else {
				TreeItem item = (TreeItem)event.item;
				Point pt = display.map(null, tree, event.x, event.y);
				Rectangle bounds = item.getBounds();
				TreeItem parent = item.getParentItem();
				if (parent != null) {
					TreeItem[] items = parent.getItems();
					int index = 0;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == item) {
							index = i;
							break;
						}
					}
					if (pt.y < bounds.y + bounds.height/3) {
						TreeItem newItem = new TreeItem(parent, SWT.NONE, index);
						newItem.setText(text);
					} else if (pt.y > bounds.y + 2*bounds.height/3) {
						TreeItem newItem = new TreeItem(parent, SWT.NONE, index+1);
						newItem.setText(text);
					} else {
						TreeItem newItem = new TreeItem(item, SWT.NONE);
						newItem.setText(text);
					}
					
				} else {
					TreeItem[] items = tree.getItems();
					int index = 0;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == item) {
							index = i;
							break;
						}
					}
					if (pt.y < bounds.y + bounds.height/3) {
						TreeItem newItem = new TreeItem(tree, SWT.NONE, index);
						newItem.setText(text);
					} else if (pt.y > bounds.y + 2*bounds.height/3) {
						TreeItem newItem = new TreeItem(tree, SWT.NONE, index+1);
						newItem.setText(text);
					} else {
						TreeItem newItem = new TreeItem(item, SWT.NONE);
						newItem.setText(text);
					}
				}
				
				
			}
		}
	});

	shell.setSize (400, 400);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4921.java