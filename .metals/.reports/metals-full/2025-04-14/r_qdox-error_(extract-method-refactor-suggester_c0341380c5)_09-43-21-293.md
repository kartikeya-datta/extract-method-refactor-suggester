error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17534.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17534.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17534.java
text:
```scala
O@@S.objc_msgSend_struct(result, this.id, OS.sel_containerSize);

package org.eclipse.swt.internal.cocoa;

public class NSTextContainer extends NSObject {

public NSTextContainer() {
	super();
}

public NSTextContainer(int id) {
	super(id);
}

public NSSize containerSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_containerSize);
	return result;
}

public boolean containsPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_containsPoint_1, point) != 0;
}

public boolean heightTracksTextView() {
	return OS.objc_msgSend(this.id, OS.sel_heightTracksTextView) != 0;
}

public id initWithContainerSize(NSSize size) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerSize_1, size);
	return result != 0 ? new id(result) : null;
}

public boolean isSimpleRectangularTextContainer() {
	return OS.objc_msgSend(this.id, OS.sel_isSimpleRectangularTextContainer) != 0;
}

public NSLayoutManager layoutManager() {
	int result = OS.objc_msgSend(this.id, OS.sel_layoutManager);
	return result != 0 ? new NSLayoutManager(result) : null;
}

public float lineFragmentPadding() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_lineFragmentPadding);
}

public NSRect lineFragmentRectForProposedRect(NSRect proposedRect, int sweepDirection, int movementDirection, int remainingRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentRectForProposedRect_1sweepDirection_1movementDirection_1remainingRect_1, proposedRect, sweepDirection, movementDirection, remainingRect);
	return result;
}

public void replaceLayoutManager(NSLayoutManager newLayoutManager) {
	OS.objc_msgSend(this.id, OS.sel_replaceLayoutManager_1, newLayoutManager != null ? newLayoutManager.id : 0);
}

public void setContainerSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setContainerSize_1, size);
}

public void setHeightTracksTextView(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHeightTracksTextView_1, flag);
}

public void setLayoutManager(NSLayoutManager layoutManager) {
	OS.objc_msgSend(this.id, OS.sel_setLayoutManager_1, layoutManager != null ? layoutManager.id : 0);
}

public void setLineFragmentPadding(float pad) {
	OS.objc_msgSend(this.id, OS.sel_setLineFragmentPadding_1, pad);
}

public void setTextView(NSTextView textView) {
	OS.objc_msgSend(this.id, OS.sel_setTextView_1, textView != null ? textView.id : 0);
}

public void setWidthTracksTextView(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWidthTracksTextView_1, flag);
}

public NSTextView textView() {
	int result = OS.objc_msgSend(this.id, OS.sel_textView);
	return result != 0 ? new NSTextView(result) : null;
}

public boolean widthTracksTextView() {
	return OS.objc_msgSend(this.id, OS.sel_widthTracksTextView) != 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17534.java