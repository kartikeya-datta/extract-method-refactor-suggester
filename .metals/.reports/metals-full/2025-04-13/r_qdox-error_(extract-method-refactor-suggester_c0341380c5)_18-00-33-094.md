error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5652.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5652.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5652.java
text:
```scala
e@@vent.gc.drawString(string, event.x - offset, y, true);

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * Table snippet: make text span multiple columns
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet239 {
	
public static void main(String [] args) {
	final Display display = new Display();
	Shell shell = new Shell (display);
	shell.setText("Text spans two columns in a TableItem");
	shell.setLayout (new FillLayout());
	final Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
	table.setHeaderVisible(true);
	int columnCount = 4;
	for (int i=0; i<columnCount; i++) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Column " + i);	
	}
	int itemCount = 8;
	for (int i = 0; i < itemCount; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, "item "+i+" a");
		item.setText(3, "item "+i+" d");
	}	
	/*
	 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
	 * Therefore, it is critical for performance that these methods be
	 * as efficient as possible.
	 */
	final String string = "text that spans two columns";
	GC gc = new GC(table);
	final Point extent = gc.stringExtent(string);
	gc.dispose();
	final Color red = display.getSystemColor(SWT.COLOR_RED);
	Listener paintListener = new Listener() {
		public void handleEvent(Event event) {
			switch(event.type) {		
				case SWT.MeasureItem: {
					if (event.index == 1 || event.index == 2) {
						event.width = extent.x/2;
						event.height = Math.max(event.height, extent.y + 2);
					}
					break;
				}
				case SWT.PaintItem: {
					if (event.index == 1 || event.index == 2) {
						int offset = 0;
						if (event.index == 2) {
							TableColumn column1 = table.getColumn(1);
							offset = column1.getWidth();
						}
						event.gc.setForeground(red);
						int y = event.y + (event.height - extent.y)/2;
						event.gc.drawString(string, event.x - offset, y);
					}
					break;
				}
			}
		}
	};
	table.addListener(SWT.MeasureItem, paintListener);
	table.addListener(SWT.PaintItem, paintListener);
	for (int i = 0; i < columnCount; i++) {
		table.getColumn(i).pack();
	}
	shell.pack();
	shell.open();
	while(!shell.isDisposed()) {
		if(!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5652.java