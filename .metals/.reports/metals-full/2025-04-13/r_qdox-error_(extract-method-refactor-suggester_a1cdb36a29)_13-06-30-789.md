error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17470.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17470.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17470.java
text:
```scala
b@@oolean result = XPCOM.nsID_Equals(ptr, otherPtr) != 0;

/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003, 2004 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsID {
	
	public int m0;
	public short m1;
	public short m2;
	public byte[] m3 = new byte[8];
	public static final int sizeof = 16;

public nsID() {
}

public nsID(String id) {
	Parse(id);
}

public boolean Equals(nsID other) {
	int /*long*/ ptr = XPCOM.nsID_new();
	XPCOM.memmove(ptr, this, nsID.sizeof);
	int /*long*/ otherPtr = XPCOM.nsID_new();
	XPCOM.memmove(otherPtr, other, nsID.sizeof);
	boolean result = XPCOM.nsID_Equals(ptr, otherPtr);
	XPCOM.nsID_delete(ptr);
	XPCOM.nsID_delete(otherPtr);
	return result;
}

public void Parse (String aIDStr) {
	if(aIDStr == null) throw new Error ();
	int i = 0;
	for (; i < 8; i++) m0 = (m0 << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16);
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 13; i++) m1 = (short)((m1 << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 18; i++) m2 = (short)((m2 << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 21; i++) m3[0] = (byte)((m3[0] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 23; i++) m3[1] = (byte)((m3[1] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 26; i++) m3[2] = (byte)((m3[2] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 28; i++) m3[3] = (byte)((m3[3] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 30; i++) m3[4] = (byte)((m3[4] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 32; i++) m3[5] = (byte)((m3[5] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 34; i++) m3[6] = (byte)((m3[6] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 36; i++) m3[7] = (byte)((m3[7] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17470.java