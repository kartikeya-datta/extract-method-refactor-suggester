error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15246.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15246.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15246.java
text:
```scala
s@@tyledText.setText("0\t1\t2\t3\t4\nDrag\tthe\ttab\tmarks\ton\ttop\tto\tchange\tthe\tposition\tof\tthe\ttab\tstops");

/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 * StyledText: Variable tab stops
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class Snippet325 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setText("StyledText: Variable tab stops");
		
		Ruler ruler = new Ruler(shell, SWT.NONE);
		GridData data = new GridData();
		data.heightHint = 10;
		data.horizontalAlignment = SWT.FILL;
		ruler.setLayoutData(data);
		ruler.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		StyledText styledText = new StyledText (shell, SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		styledText.setText("0\t1\t2\t3\t4\nDrag\tthe\ttab\tmarks\ton\ttop\tto\tchange\tthe\tpositoin\tof\tthe\ttab\tstops");
		styledText.setTabStops(new int[] {30, 70, 90, 140});
		styledText.setLayoutData(new GridData(GridData.FILL_BOTH));
		ruler.setEditor(styledText);
		
		shell.setSize(800, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose ();
	}
	
	public static class Ruler extends Canvas {
		StyledText styledText;
		int hover = -1;
		public Ruler (Composite parent, int style) {
			super(parent, style | SWT.DOUBLE_BUFFERED);
			Display display = getDisplay();
			setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			Listener listener = new Listener() {
				public void handleEvent(Event e) {
					switch (e.type) {
						case SWT.Paint:
							handlePaint(e);
							break;
						case SWT.MouseMove:
							if ((e.stateMask & SWT.BUTTON1) != 0) {
								handleDragMark(e);
							} else {
								handleMouseMove(e);
							}
							break;
						case SWT.MouseExit:
							if (hover != -1) redraw();
							hover = -1;
							break;
					}
				}
			};
			addListener(SWT.Paint, listener);
			addListener(SWT.MouseMove, listener);
			addListener(SWT.MouseExit, listener);
		}
		public void setEditor(StyledText editor) {
			styledText = editor;
		}
		void handleMouseMove(Event e) {
			int [] tabs = styledText.getTabsStop();
			int margin = styledText.getLeftMargin() + styledText.getBorderWidth();
			int count = tabs.length;
			int newIndex = -1;
			int x = margin;
			if (x <= e.x && e.x < x + 5) {
				newIndex = 0;
			}
			int i = 0;
			if (newIndex == -1) {
				for (i = 0; i < count; i++) {
					x = tabs[i] + margin;
					if (x <= e.x && e.x <= x +5) {
						newIndex = i + 1;
						break;
					}
				}
			}
			if (newIndex == -1) {
				int lastTabWidth = count > 1 ? tabs[count-1] - tabs[count-2] : tabs[0];
				x += lastTabWidth;
				Rectangle ca = getClientArea();
				while (x < ca.x + ca.width) {
					if (x <= e.x && e.x <= x +5) {
						newIndex = i + 1;
						break;
					}
					x += lastTabWidth;
					i++;
				}
			}
			if (newIndex != hover) {
				hover = newIndex;
				redraw();
			}
		}
		
		void handleDragMark (Event e) {
			if (hover <= 0) return;
			int [] tabs = styledText.getTabsStop();
			int margin = styledText.getLeftMargin() + styledText.getBorderWidth();
			int count = tabs.length;
			int x = e.x - margin;
			int index = hover - 1;
			if (index < count) {
				if (index >= count - 2) {
					int last = tabs[count - 1];
					int secondLast = tabs[count - 2];
					int grow = index == count -1 ? 2 : 1;
					int[] newTabs = new int[count + grow];
					System.arraycopy(tabs, 0, newTabs, 0, count);
					newTabs[count] = last + (last - secondLast);
					if (grow == 2) {
						newTabs[count + 1] = last + 2 * (last - secondLast);
					}
					tabs = newTabs;
				}
			} else {
				int[] newTabs = new int[index + 3];
				System.arraycopy(tabs, 0, newTabs, 0, count);
				int last = tabs[count - 1];
				int diff = last - tabs[count - 2];
				for (int i = count; i < newTabs.length; i++) {
					newTabs[i] = last + diff;
					last += diff;
				}
				tabs = newTabs;
			}
			int min = index > 0 ? tabs [index - 1] + 1 : 0;
			if (x < min) x = min;
			int max = tabs[index + 1] - 1;
			if (x > max) {
				int diff = x - tabs[index];
				for (int i = index + 1; i < count; i++) {
					tabs[i] += diff;
				}
			}
			tabs[index] = x;
			styledText.setTabStops(tabs);
			redraw();
		}
		void handlePaint(Event e) {
			Display display = e.display;
			GC gc = e.gc;
			int [] tabs = styledText.getTabsStop();
			int margin = styledText.getLeftMargin() + styledText.getBorderWidth();
			int x = margin;
			gc.setBackground(display.getSystemColor(hover == 0 ? SWT.COLOR_RED : SWT.COLOR_BLACK));
			gc.fillPolygon(new int[] {x,0, x+5,0, x, 10});
			int i;
			for (i = 0; i < tabs.length; i++) {
				x = tabs[i] + margin;
				if (x > e.x + e.width) break;
				gc.setBackground(display.getSystemColor(hover == i + 1 ? SWT.COLOR_RED : SWT.COLOR_BLACK));
				gc.fillPolygon(new int[] {x,0, x+5,0, x, 10});
			}
			int lastTabWidth = tabs.length > 1 ? tabs[tabs.length-1] - tabs[tabs.length-2] : tabs[0];
			x += lastTabWidth;
			while (x < e.x + e.width) {
				gc.setBackground(display.getSystemColor(hover == ++i ? SWT.COLOR_RED : SWT.COLOR_BLACK));
				gc.fillPolygon(new int[] {x,0, x+5,0, x, 10});
				x += lastTabWidth;
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15246.java