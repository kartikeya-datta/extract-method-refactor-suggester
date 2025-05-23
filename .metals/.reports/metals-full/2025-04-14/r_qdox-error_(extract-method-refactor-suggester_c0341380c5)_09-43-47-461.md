error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4918.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4918.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4918.java
text:
```scala
t@@ransferData.length = buffer.length - 1;

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.motif.OS;
import org.eclipse.swt.internal.motif.XTextProperty;

/**
 * The class <code>TextTransfer</code> provides a platform specific mechanism 
 * for converting plain text represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>String</code> containing plain text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String textData = "Hello World";
 * </code></pre>
 */
public class TextTransfer extends ByteArrayTransfer {

	private static TextTransfer _instance = new TextTransfer();
	private static final String COMPOUND_TEXT = "COMPOUND_TEXT";
	private static final String STRING = "STRING";
	private static final int COMPOUND_TEXT_ID = registerType(COMPOUND_TEXT);
	private static final int STRING_ID = registerType(STRING);

private TextTransfer() {}

/**
 * Returns the singleton instance of the TextTransfer class.
 *
 * @return the singleton instance of the TextTransfer class
 */
public static TextTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts plain text
 * represented by a java <code>String</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing text
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData) {
	transferData.result = 0;
	if (!checkText(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	byte[] buffer = Converter.wcsToMbcs (null, string, true);
	if (transferData.type ==  COMPOUND_TEXT_ID) {
		Display display = Display.getCurrent();
		if (display == null) return;
		int xDisplay = display.xDisplay;
		int pBuffer = OS.XtMalloc(buffer.length);
		if (pBuffer == 0) return;
		try {
			OS.memmove(pBuffer, buffer, buffer.length);
			int list = OS.XtMalloc(4);
			if (list == 0) return;
			OS.memmove(list, new int[] {pBuffer}, 4);
			XTextProperty text_prop_return = new XTextProperty();
			int result = OS.XmbTextListToTextProperty (xDisplay, list, 1, OS.XCompoundTextStyle, text_prop_return);
			OS.XtFree(list);
			if (result != 0)return;
			transferData.format = text_prop_return.format;
			transferData.length = text_prop_return.nitems;
			transferData.pValue = text_prop_return.value;
			transferData.type = text_prop_return.encoding;
			transferData.result = 1;
		} finally {
			OS.XtFree(pBuffer);
		}
	}
	if (transferData.type == STRING_ID) {
		int pValue = OS.XtMalloc(buffer.length);
		if (pValue ==  0) return;
		OS.memmove(pValue, buffer, buffer.length);
		transferData.type = STRING_ID;
		transferData.format = 8;
		transferData.length = buffer.length;
		transferData.pValue = pValue;
		transferData.result = 1;
	}
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of plain text to a java <code>String</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String</code> containing text if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) ||  transferData.pValue == 0) return null;
	byte[] buffer = null;
	if (transferData.type == COMPOUND_TEXT_ID) {
		Display display = Display.getCurrent();
		if (display == null) return null;
		int xDisplay = display.xDisplay;
		XTextProperty text_prop = new XTextProperty();
		text_prop.encoding = transferData.type;
		text_prop.format = transferData.format;
		text_prop.nitems = transferData.length;
		text_prop.value = transferData.pValue;
		int[] list_return = new int[1];
		int[] count_return = new int[1];
		int result = OS.XmbTextPropertyToTextList (xDisplay, text_prop, list_return, count_return);
		if (result != 0 || list_return[0] == 0) return null;
		//Note: only handling the first string in list
		int[] ptr = new int[1];
		OS.memmove(ptr, list_return[0], 4);
		int length = OS.strlen(ptr[0]);
		buffer = new byte[length];
		OS.memmove(buffer, ptr[0], length);
		OS.XFreeStringList(list_return[0]);
	}
	if (transferData.type == STRING_ID) {
		int size = transferData.format * transferData.length / 8;
		if (size == 0) return null;
		buffer = new byte[size];
		OS.memmove(buffer, transferData.pValue, size);
	}
	if (buffer == null) return null;
	// convert byte array to a string
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	String string = new String (unicode);
	int end = string.indexOf('\0');
	return (end == -1) ? string : string.substring(0, end);
}

protected int[] getTypeIds() {
	return new int[] {COMPOUND_TEXT_ID, STRING_ID};
}

protected String[] getTypeNames() {
	return new String[] {COMPOUND_TEXT, STRING};
}

boolean checkText(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return checkText(object);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4918.java