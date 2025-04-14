error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17261.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17261.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17261.java
text:
```scala
public v@@oid doCommandBySelector(int /*long*/ aSelector) {

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSResponder extends NSObject {

public NSResponder() {
	super();
}

public NSResponder(int /*long*/ id) {
	super(id);
}

public NSResponder(id id) {
	super(id);
}

public boolean acceptsFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_acceptsFirstResponder) != 0;
}

public boolean becomeFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_becomeFirstResponder) != 0;
}

public void doCommandBySelector(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_doCommandBySelector_, aSelector);
}

public void flagsChanged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_flagsChanged_, theEvent != null ? theEvent.id : 0);
}

public void helpRequested(NSEvent eventPtr) {
	OS.objc_msgSend(this.id, OS.sel_helpRequested_, eventPtr != null ? eventPtr.id : 0);
}

public void insertText(id insertString) {
	OS.objc_msgSend(this.id, OS.sel_insertText_, insertString != null ? insertString.id : 0);
}

public void interpretKeyEvents(NSArray eventArray) {
	OS.objc_msgSend(this.id, OS.sel_interpretKeyEvents_, eventArray != null ? eventArray.id : 0);
}

public void keyDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_keyDown_, theEvent != null ? theEvent.id : 0);
}

public void keyUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_keyUp_, theEvent != null ? theEvent.id : 0);
}

public void mouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseDown_, theEvent != null ? theEvent.id : 0);
}

public void mouseDragged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseDragged_, theEvent != null ? theEvent.id : 0);
}

public void mouseEntered(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseEntered_, theEvent != null ? theEvent.id : 0);
}

public void mouseExited(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseExited_, theEvent != null ? theEvent.id : 0);
}

public void mouseMoved(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseMoved_, theEvent != null ? theEvent.id : 0);
}

public void mouseUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseUp_, theEvent != null ? theEvent.id : 0);
}

public void moveUp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveUp_, sender != null ? sender.id : 0);
}

public void otherMouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_otherMouseDown_, theEvent != null ? theEvent.id : 0);
}

public void otherMouseDragged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_otherMouseDragged_, theEvent != null ? theEvent.id : 0);
}

public void otherMouseUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_otherMouseUp_, theEvent != null ? theEvent.id : 0);
}

public void pageDown(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pageDown_, sender != null ? sender.id : 0);
}

public void pageUp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pageUp_, sender != null ? sender.id : 0);
}

public boolean resignFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_resignFirstResponder) != 0;
}

public void rightMouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_rightMouseDown_, theEvent != null ? theEvent.id : 0);
}

public void rightMouseDragged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_rightMouseDragged_, theEvent != null ? theEvent.id : 0);
}

public void rightMouseUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_rightMouseUp_, theEvent != null ? theEvent.id : 0);
}

public void scrollWheel(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_scrollWheel_, theEvent != null ? theEvent.id : 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17261.java