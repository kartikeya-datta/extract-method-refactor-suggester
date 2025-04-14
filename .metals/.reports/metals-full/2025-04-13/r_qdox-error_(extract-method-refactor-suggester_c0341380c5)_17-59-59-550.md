error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6004.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6004.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6004.java
text:
```scala
r@@eturn OS.objc_msgSend(this.id, OS.sel_length);

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

public class NSString extends NSObject {

public NSString() {
	super();
}

public NSString(int /*long*/ id) {
	super(id);
}

public NSString(id id) {
	super(id);
}

public String getString() {
	char[] buffer = new char[(int)/*64*/length()];
	getCharacters(buffer);
	return new String(buffer);
}

public static NSString stringWith(String str) {
	char[] buffer = new char[str.length()];
	str.getChars(0, buffer.length, buffer, 0);
	return stringWithCharacters(buffer, buffer.length);
}

public short characterAtIndex(int /*long*/ index) {
	return (short)OS.objc_msgSend(this.id, OS.sel_characterAtIndex_, index);
}

public void getCharacters(char[] buffer) {
	OS.objc_msgSend(this.id, OS.sel_getCharacters_, buffer);
}

public void getCharacters(char[] buffer, NSRange aRange) {
	OS.objc_msgSend(this.id, OS.sel_getCharacters_range_, buffer, aRange);
}

public NSString lastPathComponent() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_lastPathComponent);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public int /*long*/ length() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_length);
}

public NSString lowercaseString() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_lowercaseString);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAddingPercentEscapesUsingEncoding(int /*long*/ enc) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByAddingPercentEscapesUsingEncoding_, enc);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingPathComponent(NSString str) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingPathComponent_, str != null ? str.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByDeletingPathExtension() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByDeletingPathExtension);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByReplacingOccurrencesOfString(NSString target, NSString replacement) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByReplacingOccurrencesOfString_withString_, target != null ? target.id : 0, replacement != null ? replacement.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public static NSString stringWithCharacters(char[] characters, int /*long*/ length) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCharacters_length_, characters, length);
	return result != 0 ? new NSString(result) : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6004.java