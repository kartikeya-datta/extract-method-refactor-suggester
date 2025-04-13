error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8592.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8592.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8592.java
text:
```scala
b@@oolean useChooserDialog = OS.GTK_VERSION >= OS.VERSION (2, 4, 10);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

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
	static final String SEPARATOR = System.getProperty ("file.separator");

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
	boolean useChooserDialog = OS.gtk_check_version (2, 4, 10) == 0;
	if (useChooserDialog) {
		return openChooserDialog ();
	} else {
		return openClassicDialog ();
	}
}
String openChooserDialog () {
	byte [] titleBytes = Converter.wcsToMbcs (null, title, true);
	/*
	* Feature in gtk.  If the hicolor theme is not installed then a warning
	* is displayed whenever a GtkFileChooser is created.  The workaround is
	* to turn off the display of warnings during GtkFileChooser creation.
	* http://bugzilla.gnome.org/show_bug.cgi?id=149931 . 
	*/
	Display display = parent.getDisplay (); 
	boolean oldWarnings = display.getWarnings ();
	display.setWarnings (false);
	int /*long*/ handle = OS.gtk_file_chooser_dialog_new (
		titleBytes,
		parent.topHandle (),
		OS.GTK_FILE_CHOOSER_ACTION_SELECT_FOLDER,
		OS.GTK_STOCK_CANCEL (), OS.GTK_RESPONSE_CANCEL,
		OS.GTK_STOCK_OK (), OS.GTK_RESPONSE_OK,
		0);
	display.setWarnings (oldWarnings);
	OS.g_object_set (handle, OS.show_hidden, 1, 0);
	if (filterPath != null && filterPath.length () > 0) {
		StringBuffer stringBuffer = new StringBuffer ();
		/* filename must be a full path */
		if (!filterPath.startsWith (SEPARATOR)) {
			stringBuffer.append (SEPARATOR);
		}
		stringBuffer.append (filterPath);
		byte [] buffer = Converter.wcsToMbcs (null, stringBuffer.toString (), true);
		OS.gtk_file_chooser_set_current_folder (handle, buffer);
	}
	if (message.length () > 0) {
		byte [] buffer = Converter.wcsToMbcs (null, message, true);
		int /*long*/ box = OS.gtk_hbox_new (false, 0);
		if (box == 0) error (SWT.ERROR_NO_HANDLES);
		int /*long*/ label = OS.gtk_label_new (buffer);
		if (label == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (box, label);
		OS.gtk_widget_show (label);
		OS.gtk_label_set_line_wrap (label, true);
		OS.gtk_label_set_justify (label, OS.GTK_JUSTIFY_CENTER);
		OS.gtk_file_chooser_set_extra_widget (handle, box);
	}
	String answer = null;
	int response = OS.gtk_dialog_run (handle);	
	if (response == OS.GTK_RESPONSE_OK) {
		int /*long*/ folder = OS.gtk_file_chooser_get_current_folder (handle);
		if (folder == 0) {
			OS.gtk_widget_destroy (handle);
			return null;
		}
		int length = OS.strlen (folder);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, folder, length);
		OS.g_free (folder);
		answer = new String (Converter.mbcsToWcs (null, buffer));
		filterPath = answer;
	}
	OS.gtk_widget_destroy (handle);
	return answer;
}
String openClassicDialog () {
	byte [] titleBytes = Converter.wcsToMbcs (null, title, true);
	int /*long*/ handle = OS.gtk_file_selection_new (titleBytes);
	if (parent != null) {
		OS.gtk_window_set_transient_for (handle, parent.topHandle ());
	}
	String answer = null;
	if (filterPath != null) {
		String path = filterPath;
		if (path.length () > 0 && !path.endsWith (SEPARATOR)) {
			path += SEPARATOR;
		}
		int length = path.length ();
		char [] buffer = new char [length + 1];
		path.getChars (0, length, buffer, 0);
		int /*long*/ utf8Ptr = OS.g_utf16_to_utf8 (buffer, -1, null, null, null);
		int /*long*/ fileNamePtr = OS.g_filename_from_utf8 (utf8Ptr, -1, null, null, null);
		OS.gtk_file_selection_set_filename (handle, fileNamePtr);
		OS.g_free (utf8Ptr);
		OS.g_free (fileNamePtr);		
	}
	GtkFileSelection selection = new GtkFileSelection ();
	OS.memmove (selection, handle);
	OS.gtk_file_selection_hide_fileop_buttons (handle);
	int /*long*/ fileListParent = OS.gtk_widget_get_parent (selection.file_list);
	OS.gtk_widget_hide (selection.file_list);
	OS.gtk_widget_hide (fileListParent);
	if (message.length () > 0) {
		byte [] buffer = Converter.wcsToMbcs (null, message, true);
		int /*long*/ labelHandle = OS.gtk_label_new (buffer);
		OS.gtk_label_set_line_wrap (labelHandle, true);		
		OS.gtk_misc_set_alignment (labelHandle, 0.0f, 0.0f);
		OS.gtk_container_add (selection.main_vbox, labelHandle);
		OS.gtk_box_set_child_packing (
			selection.main_vbox, labelHandle, false, false, 0, OS.GTK_PACK_START);
		OS.gtk_widget_show (labelHandle);
	}
	int response = OS.gtk_dialog_run (handle);
	if (response == OS.GTK_RESPONSE_OK) {
		int /*long*/ fileNamePtr = OS.gtk_file_selection_get_filename (handle);
		int /*long*/ utf8Ptr = OS.g_filename_to_utf8 (fileNamePtr, -1, null, null, null);
		int /*long*/ [] items_written = new int /*long*/ [1];
		int /*long*/ utf16Ptr = OS.g_utf8_to_utf16 (utf8Ptr, -1, null, items_written, null);
		int length = (int)/*64*/items_written [0];
		char [] buffer = new char [length];
		OS.memmove (buffer, utf16Ptr, length * 2);
		String osAnswer = new String (buffer);
		OS.g_free (utf16Ptr);
		OS.g_free (utf8Ptr);
		if (osAnswer != null) {
			/* remove trailing separator, unless root directory */
			if (!osAnswer.equals (SEPARATOR) && osAnswer.endsWith (SEPARATOR)) {
				osAnswer = osAnswer.substring (0, osAnswer.length () - 1);
			}
			answer = filterPath = osAnswer;
		}
	}
	OS.gtk_widget_destroy (handle);
	return answer;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8592.java