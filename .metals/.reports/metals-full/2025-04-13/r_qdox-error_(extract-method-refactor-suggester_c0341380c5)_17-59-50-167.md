error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14026.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14026.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14026.java
text:
```scala
s@@hell.setText("Show results as a bar chart in Tree");

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
 * Table example snippet: Draw a bar graph
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

public class Snippet232 {
	
public static void main(String [] args) {
	final Display display = new Display();		
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	shell.setText("Show results as a bar chart in Table");
	final Tree tree = new Tree(shell, SWT.BORDER);
	tree.setHeaderVisible(true);
	tree.setLinesVisible(true);
	TreeColumn column1 = new TreeColumn(tree, SWT.NONE);
	column1.setText("Bug Status");
	column1.setWidth(100);
	final TreeColumn column2 = new TreeColumn(tree, SWT.NONE);
	column2.setText("Percent");
	column2.setWidth(200);
	String[] states = new String[]{"Resolved", "New", "Won't Fix", "Invalid"};
	String[] teams = new String[] {"UI", "SWT", "OSGI"};
	for (int i=0; i<teams.length; i++) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText(teams[i]);
		for (int j = 0; j < states.length; j++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText(states[j]);	
		}
	}

	/*
	 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
	 * Therefore, it is critical for performance that these methods be
	 * as efficient as possible.
	 */
	tree.addListener(SWT.PaintItem, new Listener() {
		int[] percents = new int[] {50, 30, 5, 15};
		public void handleEvent(Event event) {
			if (event.index == 1) {
				TreeItem item = (TreeItem)event.item;
				TreeItem parent = item.getParentItem();
				if (parent != null) {
					GC gc = event.gc;
					int index = parent.indexOf(item);
					int percent = percents[index];
					Color foreground = gc.getForeground();
					Color background = gc.getBackground();
					gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
					gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
					int width = (column2.getWidth() - 1) * percent / 100;
					gc.fillGradientRectangle(event.x, event.y, width, event.height, true);					
					Rectangle rect2 = new Rectangle(event.x, event.y, width-1, event.height-1);
					gc.drawRectangle(rect2);
					gc.setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
					String text = percent+"%";
					Point size = event.gc.textExtent(text);					
					int offset = Math.max(0, (event.height - size.y) / 2);
					gc.drawText(text, event.x+2, event.y+offset, true);
					gc.setForeground(background);
					gc.setBackground(foreground);
				}
			}
		}
	});		
			
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14026.java