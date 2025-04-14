error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/253.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/253.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/253.java
text:
```scala
O@@S.objc_msgSend_stret(result, this.id, OS.sel_minimumSize);

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTabView extends NSView {

public NSTabView() {
	super();
}

public NSTabView(int id) {
	super(id);
}

public void addTabViewItem(NSTabViewItem tabViewItem) {
	OS.objc_msgSend(this.id, OS.sel_addTabViewItem_1, tabViewItem != null ? tabViewItem.id : 0);
}

public boolean allowsTruncatedLabels() {
	return OS.objc_msgSend(this.id, OS.sel_allowsTruncatedLabels) != 0;
}

public NSRect contentRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentRect);
	return result;
}

public int controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public int controlTint() {
	return OS.objc_msgSend(this.id, OS.sel_controlTint);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

public NSFont font() {
	int result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public int indexOfTabViewItem(NSTabViewItem tabViewItem) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfTabViewItem_1, tabViewItem != null ? tabViewItem.id : 0);
}

public int indexOfTabViewItemWithIdentifier(id identifier) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfTabViewItemWithIdentifier_1, identifier != null ? identifier.id : 0);
}

public void insertTabViewItem(NSTabViewItem tabViewItem, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertTabViewItem_1atIndex_1, tabViewItem != null ? tabViewItem.id : 0, index);
}

public NSSize minimumSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_struct(result, this.id, OS.sel_minimumSize);
	return result;
}

public int numberOfTabViewItems() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfTabViewItems);
}

public void removeTabViewItem(NSTabViewItem tabViewItem) {
	OS.objc_msgSend(this.id, OS.sel_removeTabViewItem_1, tabViewItem != null ? tabViewItem.id : 0);
}

public void selectFirstTabViewItem(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectFirstTabViewItem_1, sender != null ? sender.id : 0);
}

public void selectLastTabViewItem(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectLastTabViewItem_1, sender != null ? sender.id : 0);
}

public void selectNextTabViewItem(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectNextTabViewItem_1, sender != null ? sender.id : 0);
}

public void selectPreviousTabViewItem(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectPreviousTabViewItem_1, sender != null ? sender.id : 0);
}

public void selectTabViewItem(NSTabViewItem tabViewItem) {
	OS.objc_msgSend(this.id, OS.sel_selectTabViewItem_1, tabViewItem != null ? tabViewItem.id : 0);
}

public void selectTabViewItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_selectTabViewItemAtIndex_1, index);
}

public void selectTabViewItemWithIdentifier(id identifier) {
	OS.objc_msgSend(this.id, OS.sel_selectTabViewItemWithIdentifier_1, identifier != null ? identifier.id : 0);
}

public NSTabViewItem selectedTabViewItem() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedTabViewItem);
	return result != 0 ? new NSTabViewItem(result) : null;
}

public void setAllowsTruncatedLabels(boolean allowTruncatedLabels) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsTruncatedLabels_1, allowTruncatedLabels);
}

public void setControlSize(int controlSize) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_1, controlSize);
}

public void setControlTint(int controlTint) {
	OS.objc_msgSend(this.id, OS.sel_setControlTint_1, controlTint);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_1, flag);
}

public void setFont(NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1, font != null ? font.id : 0);
}

public void setTabViewType(int tabViewType) {
	OS.objc_msgSend(this.id, OS.sel_setTabViewType_1, tabViewType);
}

public NSTabViewItem tabViewItemAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_tabViewItemAtIndex_1, index);
	return result != 0 ? new NSTabViewItem(result) : null;
}

public NSTabViewItem tabViewItemAtPoint(NSPoint point) {
	int result = OS.objc_msgSend(this.id, OS.sel_tabViewItemAtPoint_1, point);
	return result != 0 ? new NSTabViewItem(result) : null;
}

public NSArray tabViewItems() {
	int result = OS.objc_msgSend(this.id, OS.sel_tabViewItems);
	return result != 0 ? new NSArray(result) : null;
}

public int tabViewType() {
	return OS.objc_msgSend(this.id, OS.sel_tabViewType);
}

public void takeSelectedTabViewItemFromSender(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeSelectedTabViewItemFromSender_1, sender != null ? sender.id : 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/253.java