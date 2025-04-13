error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3814.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3814.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3814.java
text:
```scala
t@@ransferData.result = getData(data, formatetc, stgmedium);

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The class <code>HTMLTransfer</code> provides a platform specific mechanism 
 * for converting text in HTML format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.
 * 
 * <p>An example of a java <code>String</code> containing HTML text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String htmlData = "<p>This is a paragraph of text.</p>";
 * </code></pre>
 *
 * @see Transfer
 */
public class HTMLTransfer extends ByteArrayTransfer {

	static HTMLTransfer _instance = new HTMLTransfer();
	static final String HTML_FORMAT = "HTML Format"; //$NON-NLS-1$
	static final int HTML_FORMATID = registerType(HTML_FORMAT);
	static final String NUMBER = "00000000"; //$NON-NLS-1$
	static final String HEADER = "Version:0.9\r\nStartHTML:"+NUMBER+"\r\nEndHTML:"+NUMBER+"\r\nStartFragment:"+NUMBER+"\r\nEndFragment:"+NUMBER+"\r\n";
	static final String PREFIX = "<html><body><!--StartFragment-->"; //$NON-NLS-1$
	static final String SUFFIX = "<!--EndFragment--></body></html>"; //$NON-NLS-1$
	static final String StartFragment = "StartFragment:"; //$NON-NLS-1$
	static final String EndFragment = "EndFragment:"; //$NON-NLS-1$
	
private HTMLTransfer() {}

/**
 * Returns the singleton instance of the HTMLTransfer class.
 *
 * @return the singleton instance of the HTMLTransfer class
 */
public static HTMLTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts HTML-formatted text
 * represented by a java <code>String</code> to a platform specific representation.
 * 
 * @param object a java <code>String</code> containing HTML text
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */
public void javaToNative (Object object, TransferData transferData){
	if (!checkHTML(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	int count = string.length();
	char[] chars = new char[count + 1];
	string.getChars(0, count, chars, 0);
	/* NOTE: CF_HTML uses UTF-8 encoding. */
	int cchMultiByte = OS.WideCharToMultiByte(OS.CP_UTF8, 0, chars, -1, null, 0, null, null);
	if (cchMultiByte == 0) {
		transferData.stgmedium = new STGMEDIUM();
		transferData.result = COM.DV_E_STGMEDIUM;
		return;
	}
	int startHTML = HEADER.length();
	int startFragment = startHTML + PREFIX.length();
	int endFragment = startFragment + cchMultiByte - 1;
	int endHTML = endFragment + SUFFIX.length();
	
	StringBuffer buffer = new StringBuffer(HEADER);
	int maxLength = NUMBER.length();
	//startHTML
	int start = buffer.toString().indexOf(NUMBER);
	String temp = Integer.toString(startHTML);
	buffer.replace(start + maxLength-temp.length(), start + maxLength, temp);
	//endHTML
	start = buffer.toString().indexOf(NUMBER, start);
	temp = Integer.toString(endHTML);
	buffer.replace(start + maxLength-temp.length(), start + maxLength, temp);
	//startFragment
	start = buffer.toString().indexOf(NUMBER, start);
	temp = Integer.toString(startFragment);
	buffer.replace(start + maxLength-temp.length(), start + maxLength, temp);
	//endFragment
	start = buffer.toString().indexOf(NUMBER, start);
	temp = Integer.toString(endFragment);
	buffer.replace(start + maxLength-temp.length(), start + maxLength, temp);
	
	buffer.append(PREFIX);
	buffer.append(string);
	buffer.append(SUFFIX);
	
	count = buffer.length();
	chars = new char[count + 1];
	buffer.getChars(0, count, chars, 0);
	cchMultiByte = OS.WideCharToMultiByte(OS.CP_UTF8, 0, chars, -1, null, 0, null, null);
	int /*long*/ lpMultiByteStr = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, cchMultiByte);
	OS.WideCharToMultiByte(OS.CP_UTF8, 0, chars, -1, lpMultiByteStr, cchMultiByte, null, null);
	transferData.stgmedium = new STGMEDIUM();
	transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
	transferData.stgmedium.unionField = lpMultiByteStr;
	transferData.stgmedium.pUnkForRelease = 0;
	transferData.result = COM.S_OK;
	return;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of HTML text to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing HTML text if the conversion was successful;
 * 		otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) return null;
	IDataObject data = new IDataObject(transferData.pIDataObject);
	data.AddRef();
	STGMEDIUM stgmedium = new STGMEDIUM();
	FORMATETC formatetc = transferData.formatetc;
	stgmedium.tymed = COM.TYMED_HGLOBAL;	
	transferData.result = data.GetData(formatetc, stgmedium);
	data.Release();	
	if (transferData.result != COM.S_OK) return null;
	int /*long*/ hMem = stgmedium.unionField;
	
	try {
		int /*long*/ lpMultiByteStr = OS.GlobalLock(hMem);
		if (lpMultiByteStr == 0) return null;
		try {
			/* NOTE: CF_HTML uses UTF-8 encoding. 
			 * The MSDN documentation for MultiByteToWideChar states that dwFlags must be set to 0 for UTF-8.
			 * Otherwise, the function fails with ERROR_INVALID_FLAGS. */
			int cchWideChar  = OS.MultiByteToWideChar (OS.CP_UTF8, 0, lpMultiByteStr, -1, null, 0);
			if (cchWideChar == 0) return null;
			char[] lpWideCharStr = new char [cchWideChar - 1];
			OS.MultiByteToWideChar (OS.CP_UTF8, 0, lpMultiByteStr, -1, lpWideCharStr, lpWideCharStr.length);
			String string = new String(lpWideCharStr);
			int fragmentStart = 0, fragmentEnd = 0;
			int start = string.indexOf(StartFragment) + StartFragment.length();
			int end = start + 1;
			while (end < string.length()) { 
				String s = string.substring(start, end);
				try {
					fragmentStart = Integer.parseInt(s);
					end++;
				} catch (NumberFormatException e) {
					break;
				}
			}
			start = string.indexOf(EndFragment) + EndFragment.length();
			end = start + 1;
			while (end < string.length()) { 
				String s = string.substring(start, end);
				try {
					fragmentEnd = Integer.parseInt(s);
					end++;
				} catch (NumberFormatException e) {
					break;
				}
			}
			if (fragmentEnd <= fragmentStart || fragmentEnd > OS.strlen(lpMultiByteStr)) return null;
			cchWideChar = OS.MultiByteToWideChar (OS.CP_UTF8, 0, lpMultiByteStr+fragmentStart, fragmentEnd - fragmentStart, lpWideCharStr, lpWideCharStr.length);
			if (cchWideChar == 0) return null;
			String s = new String(lpWideCharStr, 0, cchWideChar);
			/*
			 * Firefox includes <!--StartFragment --> in the fragment, so remove it.
			 */
			String foxStart = "<!--StartFragment -->\r\n"; //$NON-NLS-1$
			int prefix = s.indexOf(foxStart);
			if (prefix != -1) {
				prefix += foxStart.length();
				s = s.substring(prefix);
			}
			return s;
		} finally {
			OS.GlobalUnlock(hMem);
		}
	} finally {
		OS.GlobalFree(hMem);
	}
}
protected int[] getTypeIds(){
	return new int[] {HTML_FORMATID};
}
protected String[] getTypeNames(){
	return new String[] {HTML_FORMAT}; 
}
boolean checkHTML(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}
protected boolean validate(Object object) {
	return checkHTML(object);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3814.java