error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13390.java
text:
```scala
e@@.detail = ACC.STATE_NORMAL | ACC.STATE_FOCUSABLE;

/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
 * SWT accessibility snippet: respond to text-based questions from an AT
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.6
 */
import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet334 {
	final static String text = "line one\nline two\nline three";
	static Display display;
	static Shell shell;
	static Canvas canvas;

public static void main(String[] arg) {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		Label label = new Label(shell, SWT.NONE);
		label.setText("Test:");
		canvas = new Canvas(shell, SWT.MULTI | SWT.BORDER);
		final Caret caret = new Caret (canvas, SWT.NONE);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				gc.drawText(text, 10, 10);
				caret.setBounds (10, 10, 2, gc.getFontMetrics().getHeight());
				Rectangle rect = canvas.getClientArea();
				if (canvas.isFocusControl()) {
					gc.drawFocus(rect.x, rect.y, rect.width, rect.height);
				}
			}
		});
		canvas.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				switch (e.detail) {
					case SWT.TRAVERSE_TAB_NEXT:
					case SWT.TRAVERSE_TAB_PREVIOUS:
						e.doit = true; // enable traversal
						break;
				}
			}
		});
		canvas.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// key listener enables traversal out
			}
		});
		canvas.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				canvas.redraw();
			}
			public void focusLost(FocusEvent e) {
				canvas.redraw();
			}
		});
		canvas.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				canvas.setFocus();
			}
		});
		Accessible acc = canvas.getAccessible();
		acc.addRelation(ACC.RELATION_LABELLED_BY, label.getAccessible());
		acc.addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_TEXT;
			}
			public void getLocation(AccessibleControlEvent e) {
				Rectangle rect = canvas.getBounds();
				Point pt = shell.toDisplay(rect.x, rect.y);
				e.x = pt.x;
				e.y = pt.y;
				e.width = rect.width;
				e.height = rect.height;
			}
			public void getValue(AccessibleControlEvent e) {
				e.result = text;
			}
			public void getFocus(AccessibleControlEvent e) {
				e.childID = ACC.CHILDID_SELF;
			}
			public void getChildCount (AccessibleControlEvent e) {
				e.detail = 0;
			}
			public void getState (AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL | ACC.STATE_FOCUSABLE | ACC.STATE_ENABLED;
				if (canvas.isFocusControl()) e.detail |= ACC.STATE_FOCUSED | ACC.STATE_SELECTABLE;
			}
		});
		acc.addAccessibleTextListener(new AccessibleTextExtendedAdapter() {
			public void getSelectionRange(AccessibleTextEvent e) {
				// select the first 4 characters for testing
				e.offset = 0;
				e.length = 4;
			}
			public void getCaretOffset(AccessibleTextEvent e) {
				e.offset = 0;
			}
			public void getTextBounds(AccessibleTextExtendedEvent e) {
				// for now, assume that start = 0 and end = text.length
				GC gc = new GC(canvas);
				Point extent = gc.textExtent(text);
				gc.dispose();
				Rectangle rect = display.map(canvas, null, 10, 10, extent.x, extent.y);
				e.x = rect.x;
				e.y = rect.y;
				e.width = rect.width;
				e.height = rect.height;
			}
			public void getText(AccessibleTextExtendedEvent e) {
				int start = 0, end = text.length();
				switch (e.type) {
					case ACC.TEXT_BOUNDARY_ALL:
						start = e.start;
						end = e.end; 
						break;
					case ACC.TEXT_BOUNDARY_CHAR:
						start = e.count >= 0 ? e.start + e.count : e.start - e.count;
						start = Math.max(0, Math.min(end, start));
						end = start;
						e.count = start - e.start;
						e.start = start;
						e.end = start;
						break;
					case ACC.TEXT_BOUNDARY_LINE:
						int offset = e.count <= 0 ? e.start : e.end;
						offset = Math.min(offset, text.length());
						int lineCount = 0;
						int index = 0;
						while(index != -1) {
							lineCount ++;
							index = text.indexOf("\n", index);
							if (index != -1) index++;
						}
						e.count = e.count < 0 ? Math.max(e.count, -lineCount) : Math.min(e.count, lineCount);
				 		index = 0;
						int lastIndex = 0;
						String[] lines = new String[lineCount];
						for (int i = 0; i < lines.length; i++) {
							index = text.indexOf("\n", index);
							lines[i] = index != -1 ? 
											text.substring(lastIndex, index) :
											text.substring(lastIndex);
							lastIndex = index;
							index++;
						}
						int len = 0;
						int lineAtOffset = 0;
						for (int i = 0; i < lines.length; i++) {
							len += lines[i].length();
							if (len >= e.offset) {
								lineAtOffset = i;
								break;
							}
						}
						int result = Math.max(0, Math.min(lineCount-1, lineAtOffset + e.count));
						e.count = result -lineAtOffset;
						e.result = lines[result];
						break;
				}
				e.result = text.substring(start, end);
			}
			public void getSelectionCount(AccessibleTextExtendedEvent e) {
				e.count = 1;
			}
			public void getSelection(AccessibleTextExtendedEvent e) {
				// there is only 1 selection, so index = 0
				getSelectionRange(e);
				e.start = e.offset;
				e.end = e.offset + e.length;
			}
			public void getRanges(AccessibleTextExtendedEvent e) {
				// for now, ignore bounding box
				e.start = 0;
				e.end = text.length() - 1;
			}
			public void getCharacterCount(AccessibleTextExtendedEvent e) {
				e.count = text.length();
			}
			public void getVisibleRanges(AccessibleTextExtendedEvent e) {
				e.start = 0;
				e.end = text.length() - 1;
			}
		});
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Button");
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13390.java