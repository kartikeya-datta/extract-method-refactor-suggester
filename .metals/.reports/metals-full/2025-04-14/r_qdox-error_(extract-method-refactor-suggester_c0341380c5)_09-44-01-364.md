error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13554.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13554.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13554.java
text:
```scala
a@@pplication.beginSheet(panel, parent.view.window (), null, 0, 0);

/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class allow the user to navigate
 * the file system and select or enter a file name.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SAVE, OPEN, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SAVE and OPEN may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#filedialog">FileDialog snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class FileDialog extends Dialog {
	NSSavePanel panel;
	NSPopUpButton popup;
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String [] fileNames = new String[0];	
	String filterPath = "", fileName = "";
	int filterIndex = -1;
	boolean overwrite = false;
	static final char EXTENSION_SEPARATOR = ';';

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
public FileDialog (Shell parent) {
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
 * 
 * @see SWT#SAVE
 * @see SWT#OPEN
 * @see SWT#MULTI
 */
public FileDialog (Shell parent, int style) {
	super (parent, checkStyle (parent, style));
	if (Display.getSheetEnabled ()) {
		if (parent != null && (style & SWT.SHEET) != 0) this.style |= SWT.SHEET;
	}
	checkSubclass ();
}

/**
 * Returns the path of the first file that was
 * selected in the dialog relative to the filter path, or an
 * empty string if no such file has been selected.
 * 
 * @return the relative path of the file
 */
public String getFileName () {
	return fileName;
}

/**
 * Returns a (possibly empty) array with the paths of all files
 * that were selected in the dialog relative to the filter path.
 * 
 * @return the relative paths of the files
 */
public String [] getFileNames () {
	return fileNames;
}

/**
 * Returns the file extensions which the dialog will
 * use to filter the files it shows.
 *
 * @return the file extensions filter
 */
public String [] getFilterExtensions () {
	return filterExtensions;
}

/**
 * Get the 0-based index of the file extension filter
 * which was selected by the user, or -1 if no filter
 * was selected.
 * <p>
 * This is an index into the FilterExtensions array and
 * the FilterNames array.
 * </p>
 *
 * @return index the file extension filter index
 * 
 * @see #getFilterExtensions
 * @see #getFilterNames
 * 
 * @since 3.4
 */
public int getFilterIndex () {
	return filterIndex;
}

/**
 * Returns the names that describe the filter extensions
 * which the dialog will use to filter the files it shows.
 *
 * @return the list of filter names
 */
public String [] getFilterNames () {
	return filterNames;
}

/**
 * Returns the directory path that the dialog will use, or an empty
 * string if this is not set.  File names in this path will appear
 * in the dialog, filtered according to the filter extensions.
 *
 * @return the directory path string
 * 
 * @see #setFilterExtensions
 */
public String getFilterPath () {
	return filterPath;
}

/**
 * Returns the flag that the dialog will use to
 * determine whether to prompt the user for file
 * overwrite if the selected file already exists.
 *
 * @return true if the dialog will prompt for file overwrite, false otherwise
 * 
 * @since 3.4
 */
public boolean getOverwrite () {
	return overwrite;
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the first selected file,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {
	String fullPath = null;
	fileNames = new String [0];
	int /*long*/ method = 0;
	int /*long*/ methodImpl = 0;
	Callback callback = null;
	if ((style & SWT.SAVE) != 0) {
		NSSavePanel savePanel = NSSavePanel.savePanel();
		panel = savePanel;
		if (!overwrite) {
			callback = new Callback(this, "_overwriteExistingFileCheck", 3);
			int /*long*/ proc = callback.getAddress();
			if (proc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
			method = OS.class_getInstanceMethod(OS.class_NSSavePanel, OS.sel_overwriteExistingFileCheck);
			if (method != 0) methodImpl = OS.method_setImplementation(method, proc);
		}
	} else {
		NSOpenPanel openPanel = NSOpenPanel.openPanel();
		openPanel.setAllowsMultipleSelection((style & SWT.MULTI) != 0);
		panel = openPanel;
	}
	panel.setCanCreateDirectories(true);
	OS.objc_msgSend(panel.id, OS.sel_setShowsHiddenFiles_, true);
	int /*long*/ jniRef = 0;
	SWTPanelDelegate delegate = null;
	if (filterExtensions != null && filterExtensions.length != 0) {
		delegate = (SWTPanelDelegate)new SWTPanelDelegate().alloc().init();
		jniRef = OS.NewGlobalRef(this);
		if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.object_setInstanceVariable(delegate.id, Display.SWT_OBJECT, jniRef);
		panel.setDelegate(delegate);
		NSPopUpButton widget = (NSPopUpButton)new NSPopUpButton().alloc();
		widget.initWithFrame(new NSRect(), false);
		widget.setTarget(delegate);
		widget.setAction(OS.sel_sendSelection_);
		NSMenu menu = widget.menu();
		menu.setAutoenablesItems(false);
		for (int i = 0; i < filterExtensions.length; i++) {
			String str = filterExtensions [i];
			if (filterNames != null && filterNames.length > i) {
				str = filterNames [i];
			}
			NSMenuItem nsItem = (NSMenuItem)new NSMenuItem().alloc();
			nsItem.initWithTitle(NSString.stringWith(str), 0, NSString.string());
			menu.addItem(nsItem);
			nsItem.release();
		}
		widget.selectItemAtIndex(0 <= filterIndex && filterIndex < filterExtensions.length ? filterIndex : 0);
		widget.sizeToFit();
		panel.setAccessoryView(widget);
		popup = widget;
	}
	panel.setTitle(NSString.stringWith(title != null ? title : ""));
	NSApplication application = NSApplication.sharedApplication();
	if (parent != null && (style & SWT.SHEET) != 0) {
		application.beginSheet(panel, parent.window, null, 0, 0);
	}
	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	display.setModalDialog(this);
	NSString dir = filterPath != null ? NSString.stringWith(filterPath) : null;
	NSString file = fileName != null ? NSString.stringWith(fileName) : null;
	int /*long*/ response = panel.runModalForDirectory(dir, file);
	if (parent != null && (style & SWT.SHEET) != 0) {
		application.endSheet(panel, 0);
	}
	display.setModalDialog(null);
	if (!overwrite) {
		if (method != 0) OS.method_setImplementation(method, methodImpl);
		if (callback != null) callback.dispose();
	}
	if (popup != null) {
		filterIndex = (int)/*64*/popup.indexOfSelectedItem();
	} else {
		filterIndex = -1;
	}
	if (response == OS.NSFileHandlingPanelOKButton) {
		NSString filename = panel.filename();
		if ((style & SWT.SAVE) != 0) {
			if (filterExtensions != null && filterExtensions.length != 0) {
				if (0 <= filterIndex && filterIndex < filterExtensions.length) {
					/* Append extension if not present */
					NSString ext = filename.pathExtension();
					if (ext == null || ext.length() == 0) {
						String exts = filterExtensions [filterIndex];
						int length = exts.length ();
						int index = exts.indexOf (EXTENSION_SEPARATOR);
						if (index == -1) index = length;
						String filter = exts.substring (0, index).trim ();
						if (!filter.equals ("*") && !filter.equals ("*.*")) {
							if (filter.startsWith ("*.")) filter = filter.substring (2);
							filename = filename.stringByAppendingPathExtension(NSString.stringWith(filter));
						}	
					}
				}
			}
			fullPath = filename.getString();
			fileNames = new String [1];
			fileName = fileNames [0] = filename.lastPathComponent().getString();
			filterPath = filename.stringByDeletingLastPathComponent().getString();
		} else {
			fullPath = filename.getString();
			NSArray filenames = ((NSOpenPanel)panel).filenames();
			int count = (int)/*64*/filenames.count();
			fileNames = new String[count];
			
			for (int i = 0; i < count; i++) {
				filename = new NSString(filenames.objectAtIndex(i));
				NSString filenameOnly = filename.lastPathComponent();
				NSString pathOnly = filename.stringByDeletingLastPathComponent();
				
				if (i == 0) {
					/* Filter path */
					filterPath = pathOnly.getString();

					/* File name */
					fileName = fileNames [0] = filenameOnly.getString();
				} else {									
					if (pathOnly.getString().equals (filterPath)) {
						fileNames [i] = filenameOnly.getString();
					} else {
						fileNames [i] = filename.getString();
					}
				}
			}
		}
	}
	if (popup != null) {
		panel.setAccessoryView(null);
		popup.release();
		popup = null;
	}
	if (delegate != null) {
		panel.setDelegate(null);
		delegate.release();
	}
	if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
	panel = null;
	return fullPath;	
}

int /*long*/ _overwriteExistingFileCheck (int /*long*/ id, int /*long*/ sel, int /*long*/ str) {
	return 1;
}

int /*long*/ panel_shouldShowFilename (int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	NSString path = new NSString(arg1);
	if (filterExtensions != null && filterExtensions.length != 0) {
		NSFileManager manager = NSFileManager.defaultManager();
		int /*long*/ ptr = OS.malloc(1);
		boolean found = manager.fileExistsAtPath(path, ptr);
		byte[] isDirectory = new byte[1];
		OS.memmove(isDirectory, ptr, 1);
		OS.free(ptr);
		if (found) {
			if (isDirectory[0] != 0) {
				return 1;
			} else {
				NSString ext = path.pathExtension();
				if (ext != null) {
					int filterIndex = (int)/*64*/popup.indexOfSelectedItem();
					String extension = ext.getString();
					String extensions = filterExtensions [filterIndex];
					int start = 0, length = extensions.length ();
					while (start < length) {
						int index = extensions.indexOf (EXTENSION_SEPARATOR, start);
						if (index == -1) index = length;
						String filter = extensions.substring (start, index).trim ();
						if (filter.equals ("*") || filter.equals ("*.*")) return 1;
						if (filter.startsWith ("*.")) filter = filter.substring (2);
						if (filter.toLowerCase ().equals(extension.toLowerCase ())) return 1;
						start = index + 1;
					}
				}
				return 0;
			}
		}
	}
	return 1;
}

void sendSelection (int /*long*/ id, int /*long*/ sel, int /*long*/ arg) {
	panel.validateVisibleColumns();
}

/**
 * Set the initial filename which the dialog will
 * select by default when opened to the argument,
 * which may be null.  The name will be prefixed with
 * the filter path when one is supplied.
 * 
 * @param string the file name
 */
public void setFileName (String string) {
	fileName = string;
}

/**
 * Set the file extensions which the dialog will
 * use to filter the files it shows to the argument,
 * which may be null.
 * <p>
 * The strings are platform specific. For example, on
 * some platforms, an extension filter string is typically
 * of the form "*.extension", where "*.*" matches all files.
 * For filters with multiple extensions, use semicolon as
 * a separator, e.g. "*.jpg;*.png".
 * </p>
 *
 * @param extensions the file extension filter
 * 
 * @see #setFilterNames to specify the user-friendly
 * names corresponding to the extensions
 */
public void setFilterExtensions (String [] extensions) {
	filterExtensions = extensions;
}

/**
 * Set the 0-based index of the file extension filter
 * which the dialog will use initially to filter the files
 * it shows to the argument.
 * <p>
 * This is an index into the FilterExtensions array and
 * the FilterNames array.
 * </p>
 *
 * @param index the file extension filter index
 * 
 * @see #setFilterExtensions
 * @see #setFilterNames
 * 
 * @since 3.4
 */
public void setFilterIndex (int index) {
	filterIndex = index;
}

/**
 * Sets the names that describe the filter extensions
 * which the dialog will use to filter the files it shows
 * to the argument, which may be null.
 * <p>
 * Each name is a user-friendly short description shown for
 * its corresponding filter. The <code>names</code> array must
 * be the same length as the <code>extensions</code> array.
 * </p>
 *
 * @param names the list of filter names, or null for no filter names
 * 
 * @see #setFilterExtensions
 */
public void setFilterNames (String [] names) {
	filterNames = names;
}

/**
 * Sets the directory path that the dialog will use
 * to the argument, which may be null. File names in this
 * path will appear in the dialog, filtered according
 * to the filter extensions. If the string is null,
 * then the operating system's default filter path
 * will be used.
 * <p>
 * Note that the path string is platform dependent.
 * For convenience, either '/' or '\' can be used
 * as a path separator.
 * </p>
 *
 * @param string the directory path
 * 
 * @see #setFilterExtensions
 */
public void setFilterPath (String string) {
	filterPath = string;
}

/**
 * Sets the flag that the dialog will use to
 * determine whether to prompt the user for file
 * overwrite if the selected file already exists.
 *
 * @param overwrite true if the dialog will prompt for file overwrite, false otherwise
 * 
 * @since 3.4
 */
public void setOverwrite (boolean overwrite) {
	this.overwrite = overwrite;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13554.java