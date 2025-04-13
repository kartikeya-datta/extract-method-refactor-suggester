error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16901.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16901.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16901.java
text:
```scala
public i@@nt GetTypeInfoCount(int /*long*/[] pctinfo ){

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
package org.eclipse.swt.internal.ole.win32;

import org.eclipse.swt.internal.win32.*;

public class IDispatch extends IUnknown
{
public IDispatch(int /*long*/ address) {
	super(address);
}
public int GetIDsOfNames(GUID riid, String[] rgszNames, int cNames, int lcid, int[] rgDispId) {

	char[] buffer;
	int size = rgszNames.length;

	// create an array to hold the addresses
	int /*long*/ hHeap = OS.GetProcessHeap();
	int /*long*/ ppNames = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, size * OS.PTR_SIZEOF);
	int /*long*/[] memTracker = new int /*long*/[size];
	
	try {	
		// add the address of each string to the array
		
		for (int i=0; i<size; i++){
			// create a null terminated array of char for each String
			int nameSize = rgszNames[i].length();
			buffer = new char[nameSize +1];
			rgszNames[i].getChars(0, nameSize, buffer, 0);
			// get the address of the start of the array of char
			int /*long*/ pName = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, buffer.length * 2);
			OS.MoveMemory(pName, buffer, buffer.length * 2);
			// copy the address to the array of addresses
			COM.MoveMemory(ppNames + OS.PTR_SIZEOF * i, new int /*long*/[]{pName}, OS.PTR_SIZEOF);
			// keep track of the Global Memory so we can free it
			memTracker[i] = pName;
		}
	
		return COM.VtblCall(5, address, new GUID(), ppNames, cNames, lcid, rgDispId);
		
	} finally {
		// free the memory
		for (int i=0; i<memTracker.length; i++){
			OS.HeapFree(hHeap, 0, memTracker[i]);
		}
		OS.HeapFree(hHeap, 0, ppNames);
	}
}
public int GetTypeInfo(int iTInfo, int lcid, int /*long*/[] ppTInfo ){
	return COM.VtblCall(4, address, iTInfo, lcid, ppTInfo);
}
public int GetTypeInfoCount(int[] pctinfo ){
	return COM.VtblCall(3, address, pctinfo);
}
public int Invoke(int dispIdMember, GUID riid, int lcid, int dwFlags, DISPPARAMS pDispParams, int /*long*/ pVarResult, EXCEPINFO pExcepInfo, int[] pArgErr) {
	return COM.VtblCall(6, address, dispIdMember, riid, lcid, dwFlags, pDispParams, pVarResult, pExcepInfo, pArgErr);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16901.java