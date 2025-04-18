error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15296.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15296.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15296.java
text:
```scala
b@@yte [] buffer7 = Converter.wcsToMbcs (null, SWT.getMessage ("SWT_Selection"), true);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/**
 * Instances of this class allow the user to navigate
 * the file system and select a directory.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class DirectoryDialog extends Dialog {
	String filterPath = ""; //$NON-NLS-1$
	boolean cancel = true;
	String message = ""; //$NON-NLS-1$
	static final String SEPARATOR = System.getProperty ("file.separator"); //$NON-NLS-1$
	
/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public DirectoryDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}
/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public DirectoryDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}
int activate (int widget, int client, int call) {
	cancel = client == OS.XmDIALOG_CANCEL_BUTTON;
	OS.XtUnmanageChild (widget);
	return 0;
}
/**
 * Returns the path which the dialog will use to filter
 * the directories it shows.
 *
 * @return the filter path
 */
public String getFilterPath () {
	return filterPath;
}
/**
 * Returns the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @return the message
 */
public String getMessage () {
	return message;
}
/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the selected directory,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {
	/* Compute the dialog title */	
	/*
	* Feature in Motif.  It is not possible to set a shell
	* title to an empty string.  The fix is to set the title
	* to be a single space.
	*/
	String string = title;
	if (string.length () == 0) string = " "; //$NON-NLS-1$

	/* Use the character encoding for the default locale */
	byte [] buffer1 = Converter.wcsToMbcs (null, string, true);
	int xmStringPtr1 = OS.XmStringParseText (
		buffer1,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Compute the filter */
	/* Use the character encoding for the default locale */
	byte [] buffer2 = Converter.wcsToMbcs (null, "*", true); //$NON-NLS-1$
	int xmStringPtr2 = OS.XmStringParseText (
		buffer2,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Compute the filter path */
	if (filterPath == null) filterPath = ""; //$NON-NLS-1$
	/* Use the character encoding for the default locale */
	byte [] buffer3 = Converter.wcsToMbcs (null, filterPath, true);
	int xmStringPtr3 = OS.XmStringParseText (
		buffer3,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Use the character encoding for the default locale */
	byte [] buffer7 = Converter.wcsToMbcs (null, "Selection", true);
	int xmStringPtr4 = OS.XmStringParseText (
		buffer7,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Create the dialog */
	Display display = parent.display;
	int [] argList1 = {
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNdialogStyle, OS.XmDIALOG_PRIMARY_APPLICATION_MODAL,
		OS.XmNwidth, OS.XDisplayWidth (display.xDisplay, OS.XDefaultScreen (display.xDisplay)) * 4 / 9,
		OS.XmNdialogTitle, xmStringPtr1,
		OS.XmNpattern, xmStringPtr2,
		OS.XmNdirectory, xmStringPtr3,
		OS.XmNpathMode, OS.XmPATH_MODE_FULL,
		OS.XmNfilterLabelString, xmStringPtr4
	};

	/*
	* Bug in AIX. The dialog does not responde to input, if the parent
	* is not realized.  The fix is to realized the parent.  
	*/
	if (OS.IsAIX) parent.realizeWidget ();
	int parentHandle = parent.shellHandle;
	/*
	* Feature in Linux.  For some reason, the XmCreateFileSelectionDialog()
	* will not accept NULL for the widget name.  This works fine on the other
	* Motif platforms and in the other XmCreate calls on Linux.  The fix is
	* to pass in a NULL terminated string, not a NULL pointer.
	*/
	byte [] name = new byte [] {0};
	int dialog = OS.XmCreateFileSelectionDialog (parentHandle, name, argList1, argList1.length / 2);
	int child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_HELP_BUTTON);
	if (child != 0) OS.XtUnmanageChild (child);
	child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_LIST);
	if (child != 0) {
		int parent2 = OS.XtParent(child);
		if (parent2 !=0) OS.XtUnmanageChild (parent2);
	}
	child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_LIST_LABEL);
	if (child != 0) OS.XtUnmanageChild (child);
	child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_TEXT);
	if (child != 0) OS.XtUnmanageChild (child);
	child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_SELECTION_LABEL);
	if (child != 0) OS.XtUnmanageChild (child);
	OS.XmStringFree (xmStringPtr1);
	OS.XmStringFree (xmStringPtr2);
	OS.XmStringFree (xmStringPtr3);
	OS.XmStringFree (xmStringPtr4);

	/* Add label widget for message text. */
	/* Use the character encoding for the default locale */
	byte [] buffer4 = Converter.wcsToMbcs (null, message, true);
	int xmString1 = OS.XmStringGenerate(buffer4, null, OS.XmCHARSET_TEXT, null);
	int [] argList = {
		OS.XmNlabelType, OS.XmSTRING,
		OS.XmNlabelString, xmString1
	};
	int textArea = OS.XmCreateLabel(dialog, name, argList, argList.length/2);
	OS.XtManageChild(textArea);
	OS.XmStringFree (xmString1);

	/* Hook the callbacks. */
	Callback callback = new Callback (this, "activate", 3); //$NON-NLS-1$
	int address = callback.getAddress ();
	if (address == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.XtAddCallback (dialog, OS.XmNokCallback, address, OS.XmDIALOG_OK_BUTTON);
	OS.XtAddCallback (dialog, OS.XmNcancelCallback, address, OS.XmDIALOG_CANCEL_BUTTON);

	/* Open the dialog and dispatch events. */
	cancel = true;
	OS.XtManageChild (dialog);

	/* Should be a pure OS message loop (no SWT AppContext) */
	while (OS.XtIsRealized (dialog) && OS.XtIsManaged (dialog))
		if (!display.readAndDispatch ()) display.sleep ();

	/* Set the new path, file name and filter. */
	String directoryPath=""; //$NON-NLS-1$
	if (!cancel) {
		int [] argList2 = {OS.XmNdirMask, 0};
		OS.XtGetValues (dialog, argList2, argList2.length / 2);
		int xmString3 = argList2 [1];
		int [] table = new int [] {display.tabMapping, display.crMapping};
		int ptr = OS.XmStringUnparse (
			xmString3,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			table,
			table.length,
			OS.XmOUTPUT_ALL);
		if (ptr != 0) {
			int length = OS.strlen (ptr);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, ptr, length);
			OS.XtFree (ptr);
			/* Use the character encoding for the default locale */
			directoryPath = new String (Converter.mbcsToWcs (null, buffer));
		}
		OS.XmStringFree (xmString3);
		int length = directoryPath.length ();
		if (directoryPath.charAt (length - 1) == '*') {
			directoryPath = directoryPath.substring (0, length - 1);
			length--;
		}
		if (directoryPath.endsWith (SEPARATOR) && !directoryPath.equals (SEPARATOR)) {
			directoryPath = directoryPath.substring (0, length - 1);
		}
		filterPath = directoryPath;
	}

	/* Destroy the dialog and update the display. */
	if (OS.XtIsRealized (dialog)) OS.XtDestroyWidget (dialog);
	callback.dispose ();
	
	if (cancel) return null;
	return directoryPath;
}
/**
 * Sets the path which the dialog will use to filter
 * the directories it shows to the argument, which may be
 * null.
 *
 * @param string the filter path
 */
public void setFilterPath (String string) {
	filterPath = string;
}
/**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @param string the message
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 */
public void setMessage (String string) {
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	message = string;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15296.java