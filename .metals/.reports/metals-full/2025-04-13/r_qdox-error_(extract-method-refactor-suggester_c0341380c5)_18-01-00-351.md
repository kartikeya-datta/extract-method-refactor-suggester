error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11908.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11908.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11908.java
text:
```scala
r@@eturn OS.objc_msgSend_bool(this.id, OS.sel_isRunning);

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

public class NSApplication extends NSResponder {

public NSApplication() {
	super();
}

public NSApplication(int /*long*/ id) {
	super(id);
}

public NSApplication(id id) {
	super(id);
}

public void activateIgnoringOtherApps(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_activateIgnoringOtherApps_, flag);
}

public NSEvent currentEvent() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_currentEvent);
	return result != 0 ? new NSEvent(result) : null;
}

public void finishLaunching() {
	OS.objc_msgSend(this.id, OS.sel_finishLaunching);
}

public void hide(id sender) {
	OS.objc_msgSend(this.id, OS.sel_hide_, sender != null ? sender.id : 0);
}

public void hideOtherApplications(id sender) {
	OS.objc_msgSend(this.id, OS.sel_hideOtherApplications_, sender != null ? sender.id : 0);
}

public boolean isRunning() {
	return OS.objc_msgSend(this.id, OS.sel_isRunning) != 0;
}

public NSWindow keyWindow() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_keyWindow);
	return result != 0 ? new NSWindow(result) : null;
}

public NSMenu mainMenu() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_mainMenu);
	return result != 0 ? new NSMenu(result) : null;
}

public NSEvent nextEventMatchingMask(int /*long*/ mask, NSDate expiration, NSString mode, boolean deqFlag) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_nextEventMatchingMask_untilDate_inMode_dequeue_, mask, expiration != null ? expiration.id : 0, mode != null ? mode.id : 0, deqFlag);
	return result != 0 ? new NSEvent(result) : null;
}

public void orderFrontStandardAboutPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontStandardAboutPanel_, sender != null ? sender.id : 0);
}

public NSArray orderedWindows() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_orderedWindows);
	return result != 0 ? new NSArray(result) : null;
}

public void run() {
	OS.objc_msgSend(this.id, OS.sel_run);
}

public int /*long*/ runModalForWindow(NSWindow theWindow) {
	return OS.objc_msgSend(this.id, OS.sel_runModalForWindow_, theWindow != null ? theWindow.id : 0);
}

public void sendEvent(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_sendEvent_, theEvent != null ? theEvent.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, anObject != null ? anObject.id : 0);
}

public static NSApplication sharedApplication() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSApplication, OS.sel_sharedApplication);
	return result != 0 ? new NSApplication(result) : null;
}

public void stop(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stop_, sender != null ? sender.id : 0);
}

public void terminate(id sender) {
	OS.objc_msgSend(this.id, OS.sel_terminate_, sender != null ? sender.id : 0);
}

public void unhideAllApplications(id sender) {
	OS.objc_msgSend(this.id, OS.sel_unhideAllApplications_, sender != null ? sender.id : 0);
}

public NSArray windows() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_windows);
	return result != 0 ? new NSArray(result) : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11908.java