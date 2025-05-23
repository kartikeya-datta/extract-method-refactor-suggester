error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15414.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15414.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15414.java
text:
```scala
s@@uper (parent, checkStyle (parent, style));

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.*;
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
	String message = "", filterPath = "";

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
	this (parent, SWT.APPLICATION_MODAL);
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

/**
 * Returns the path which the dialog will use to filter
 * the directories it shows.
 *
 * @return the filter path
 * 
 * @see #setFilterPath
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
	String directoryPath = null;	
	int titlePtr = 0;
	int messagePtr = 0;
	if (title != null) {
		char [] buffer = new char [title.length ()];
		title.getChars (0, buffer.length, buffer, 0);
		titlePtr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	}
	char [] buffer = new char [message.length ()];
	message.getChars (0, buffer.length, buffer, 0);
	messagePtr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	
	NavDialogCreationOptions options = new NavDialogCreationOptions ();
	options.parentWindow = OS.GetControlOwner (parent.handle);
	// NEEDS WORK - no title displayed
	options.windowTitle = options.clientName = titlePtr;
	options.optionFlags = OS.kNavSupportPackages | OS.kNavAllowOpenPackages | OS.kNavAllowInvisibleFiles;
	options.message = messagePtr;
	options.location_h = -1;
	options.location_v = -1;
	int [] outDialog = new int [1];
	// NEEDS WORK - use inFilterProc to handle filtering
	if (OS.NavCreateChooseFolderDialog (options, 0, 0, 0, outDialog) == OS.noErr) {
		if (filterPath != null && filterPath.length () > 0) {
			char [] chars = new char [filterPath.length ()];
			filterPath.getChars (0, chars.length, chars, 0);
			int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
			if (str != 0) {
				int url = OS.CFURLCreateWithFileSystemPath (OS.kCFAllocatorDefault, str, OS.kCFURLPOSIXPathStyle, false);
				if (url != 0) {
					byte [] fsRef = new byte [80];
					if (OS.CFURLGetFSRef (url, fsRef)) {
						AEDesc params = new AEDesc ();
						if (OS.AECreateDesc (OS.typeFSRef, fsRef, fsRef.length, params) == OS.noErr) {
							OS.NavCustomControl (outDialog [0], OS.kNavCtlSetLocation, params);
							OS.AEDisposeDesc (params);
						}
					}
					OS.CFRelease (url);
				}
				OS.CFRelease (str);
			}
		}
		OS.NavDialogRun (outDialog [0]);
		if (OS.NavDialogGetUserAction (outDialog [0]) == OS.kNavUserActionChoose) {
			NavReplyRecord record = new NavReplyRecord ();
			OS.NavDialogGetReply (outDialog [0], record);
			AEDesc selection = new AEDesc ();
			selection.descriptorType = record.selection_descriptorType;
			selection.dataHandle = record.selection_dataHandle;
			int [] count = new int [1];
			OS.AECountItems (selection, count);
			if (count [0] > 0) {
				int [] theAEKeyword = new int [1];
				int [] typeCode = new int [1];
				int maximumSize = 80; // size of FSRef
				int dataPtr = OS.NewPtr (maximumSize);
				int [] actualSize = new int [1];
				int status = OS.AEGetNthPtr (selection, 1, OS.typeFSRef, theAEKeyword, typeCode, dataPtr, maximumSize, actualSize);
				if (status == OS.noErr && typeCode [0] == OS.typeFSRef) {
					byte [] fsRef = new byte [actualSize [0]];
					OS.memmove (fsRef, dataPtr, actualSize [0]);
					int dirUrl = OS.CFURLCreateFromFSRef (OS.kCFAllocatorDefault, fsRef);
					int dirString = OS.CFURLCopyFileSystemPath(dirUrl, OS.kCFURLPOSIXPathStyle);
					OS.CFRelease (dirUrl);						
					int length = OS.CFStringGetLength (dirString);
					buffer= new char [length];
					CFRange range = new CFRange ();
					range.length = length;
					OS.CFStringGetCharacters (dirString, range, buffer);
					OS.CFRelease (dirString);
					filterPath = directoryPath = new String (buffer);
				}
				OS.DisposePtr (dataPtr);
			}
		}
	}
	if (titlePtr != 0) OS.CFRelease (titlePtr);	
	if (messagePtr != 0) OS.CFRelease (messagePtr);
	if (outDialog [0] != 0) OS.NavDialogDispose (outDialog [0]);
	return directoryPath;
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

/**
 * Sets the path that the dialog will use to filter
 * the directories it shows to the argument, which may
 * be null. If the string is null, then the operating
 * system's default filter path will be used.
 * <p>
 * Note that the path string is platform dependent.
 * For convenience, either '/' or '\' can be used
 * as a path separator.
 * </p>
 *
 * @param string the filter path
 */
public void setFilterPath (String string) {
	filterPath = string;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15414.java