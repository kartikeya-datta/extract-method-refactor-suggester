error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6067.java
text:
```scala
s@@uper (checkParent (parent), style = checkStyle (style));

/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class implement the browser user interface
 * metaphor.  It allows the user to visualize and navigate through
 * HTML documents.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>MOZILLA</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @since 3.0
 */

public class Browser extends Composite {
	WebBrowser webBrowser;

	static final String PACKAGE_PREFIX = "org.eclipse.swt.browser."; //$NON-NLS-1$
	static final String NO_INPUT_METHOD = "org.eclipse.swt.internal.gtk.noInputMethod"; //$NON-NLS-1$

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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for browser creation</li>
 * </ul>
 * 
 * @see Widget#getStyle
 * 
 * @since 3.0
 */
public Browser (Composite parent, int style) {
	super (checkParent (parent), checkStyle (style));
	String platform = SWT.getPlatform ();
	Display display = parent.getDisplay ();
	if ("gtk".equals (platform)) display.setData (NO_INPUT_METHOD, null); //$NON-NLS-1$
	String className = null;
	if ((style & SWT.MOZILLA) != 0) {
		className = "org.eclipse.swt.browser.Mozilla"; //$NON-NLS-1$
	} else {
		if ("win32".equals (platform) || "wpf".equals (platform)) { //$NON-NLS-1$ $NON-NLS-2$
			className = "org.eclipse.swt.browser.IE"; //$NON-NLS-1$
		} else if ("motif".equals (platform)) { //$NON-NLS-1$
			className = "org.eclipse.swt.browser.Mozilla"; //$NON-NLS-1$
		} else if ("gtk".equals (platform)) { //$NON-NLS-1$
			className = "org.eclipse.swt.browser.Mozilla"; //$NON-NLS-1$
		} else if ("carbon".equals (platform)) { //$NON-NLS-1$
			className = "org.eclipse.swt.browser.Safari"; //$NON-NLS-1$
		} else if ("photon".equals (platform)) { //$NON-NLS-1$
			className = "org.eclipse.swt.browser.Voyager"; //$NON-NLS-1$
		} else {
			dispose ();
			SWT.error (SWT.ERROR_NO_HANDLES);
		}
	}

	try {
		Class clazz = Class.forName (className);
		webBrowser = (WebBrowser)clazz.newInstance ();
	} catch (ClassNotFoundException e) {
	} catch (IllegalAccessException e) {
	} catch (InstantiationException e) {
	}
	if (webBrowser == null) {
		dispose ();
		SWT.error (SWT.ERROR_NO_HANDLES);
	}

	webBrowser.setBrowser (this);
	webBrowser.create (parent, style);
}

static Composite checkParent (Composite parent) {
	String platform = SWT.getPlatform ();
	if (!"gtk".equals (platform)) return parent; //$NON-NLS-1$

	/*
	* Note.  Mozilla provides all IM support needed for text input in web pages.
	* If SWT creates another input method context for the widget it will cause
	* indeterminate results to happen (hangs and crashes). The fix is to prevent 
	* SWT from creating an input method context for the  Browser widget.
	*/
	if (parent != null && !parent.isDisposed ()) {
		Display display = parent.getDisplay ();
		if (display != null) {
			if (display.getThread () == Thread.currentThread ()) {
				display.setData (NO_INPUT_METHOD, "true"); //$NON-NLS-1$
			}
		}
	}
	return parent;
}

static int checkStyle(int style) {
	String platform = SWT.getPlatform ();
	if ((style & SWT.MOZILLA) != 0) {
		if ("carbon".equals (platform)) return style | SWT.EMBEDDED; //$NON-NLS-1$
		if ("motif".equals (platform)) return style | SWT.EMBEDDED; //$NON-NLS-1$
		return style;
	}

	if ("win32".equals (platform)) { //$NON-NLS-1$
		return style & ~SWT.BORDER;
	} else if ("motif".equals (platform)) { //$NON-NLS-1$
		return style | SWT.EMBEDDED;
	}
	return style;
}

/**
 * Clears all session cookies from all current Browser instances.
 * 
 * @since 3.2
 */
public static void clearSessions () {
	WebBrowser.clearSessions ();
}

/**	 
 * Adds the listener to the collection of listeners who will be
 * notified when the window hosting the receiver should be closed.
 * <p>
 * This notification occurs when a javascript command such as
 * <code>window.close</code> gets executed by a <code>Browser</code>.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addCloseWindowListener (CloseWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.addCloseWindowListener (listener);
}

/**	 
 * Adds the listener to the collection of listeners who will be
 * notified when the current location has changed or is about to change.
 * <p>
 * This notification typically occurs when the application navigates
 * to a new location with {@link #setUrl(String)} or when the user
 * activates a hyperlink.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addLocationListener (LocationListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.addLocationListener (listener);
}

/**	 
 * Adds the listener to the collection of listeners who will be
 * notified when a new window needs to be created.
 * <p>
 * This notification occurs when a javascript command such as
 * <code>window.open</code> gets executed by a <code>Browser</code>.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addOpenWindowListener (OpenWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.addOpenWindowListener (listener);
}

/**	 
 * Adds the listener to the collection of listeners who will be
 * notified when a progress is made during the loading of the current 
 * URL or when the loading of the current URL has been completed.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addProgressListener (ProgressListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.addProgressListener (listener);
}

/**	 
 * Adds the listener to the collection of listeners who will be
 * notified when the status text is changed.
 * <p>
 * The status text is typically displayed in the status bar of
 * a browser application.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addStatusTextListener (StatusTextListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.addStatusTextListener (listener);
}

/**	 
 * Adds the listener to the collection of listeners who will be
 * notified when the title of the current document is available
 * or has changed.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addTitleListener (TitleListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.addTitleListener (listener);
}

/**	 
 * Adds the listener to the collection of listeners who will be
 * notified when a window hosting the receiver needs to be displayed
 * or hidden.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addVisibilityWindowListener (VisibilityWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.addVisibilityWindowListener (listener);
}

/**
 * Navigate to the previous session history item.
 *
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see #forward
 * 
 * @since 3.0
 */
public boolean back () {
	checkWidget();
	return webBrowser.back ();
}

protected void checkSubclass () {
	String name = getClass ().getName ();
	int index = name.lastIndexOf ('.');
	if (!name.substring (0, index + 1).equals (PACKAGE_PREFIX)) {
		SWT.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

/**
 * Execute the specified script.
 *
 * <p>
 * Execute a script containing javascript commands in the context of the current document. 
 * 
 * @param script the script with javascript commands
 *  
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the script is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
public boolean execute (String script) {
	checkWidget();
	if (script == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return webBrowser.execute (script);
}

/**
 * Navigate to the next session history item.
 *
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @see #back
 * 
 * @since 3.0
 */
public boolean forward () {
	checkWidget();
	return webBrowser.forward ();
}

/**
 * Returns the current URL.
 *
 * @return the current URL or an empty <code>String</code> if there is no current URL
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see #setUrl
 * 
 * @since 3.0
 */
public String getUrl () {
	checkWidget();
	return webBrowser.getUrl ();
}

/**
 * Returns the JavaXPCOM <code>nsIWebBrowser</code> for the receiver, or <code>null</code>
 * if it is not available.  In order for an <code>nsIWebBrowser</code> to be returned all
 * of the following must be true: <ul>
 *    <li>the receiver's style must be <code>SWT.MOZILLA</code></li>
 *    <li>the classes from JavaXPCOM &gt;= 1.8.1.2 must be resolvable at runtime</li>
 *    <li>the version of the underlying XULRunner must be &gt;= 1.8.1.2</li>
 * </ul> 
 *
 * @return the receiver's JavaXPCOM <code>nsIWebBrowser</code> or <code>null</code>
 * 
 * @since 3.3
 */
public Object getWebBrowser () {
	checkWidget();
	return webBrowser.getWebBrowser ();
}

/**
 * Returns <code>true</code> if the receiver can navigate to the 
 * previous session history item, and <code>false</code> otherwise.
 *
 * @return the receiver's back command enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #back
 */
public boolean isBackEnabled () {
	checkWidget();
	return webBrowser.isBackEnabled ();
}

public boolean isFocusControl () {
	checkWidget();
	if (webBrowser.isFocusControl ()) return true;
	return super.isFocusControl ();
}

/**
 * Returns <code>true</code> if the receiver can navigate to the 
 * next session history item, and <code>false</code> otherwise.
 *
 * @return the receiver's forward command enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #forward
 */
public boolean isForwardEnabled () {
	checkWidget();
	return webBrowser.isForwardEnabled ();
}

/**
 * Refresh the current page.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void refresh () {
	checkWidget();
	webBrowser.refresh ();
}

/**	 
 * Removes the listener from the collection of listeners who will
 * be notified when the window hosting the receiver should be closed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeCloseWindowListener (CloseWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.removeCloseWindowListener (listener);
}

/**	 
 * Removes the listener from the collection of listeners who will
 * be notified when the current location is changed or about to be changed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeLocationListener (LocationListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.removeLocationListener (listener);
}

/**	 
 * Removes the listener from the collection of listeners who will
 * be notified when a new window needs to be created.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeOpenWindowListener (OpenWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.removeOpenWindowListener (listener);
}

/**	 
 * Removes the listener from the collection of listeners who will
 * be notified when a progress is made during the loading of the current 
 * URL or when the loading of the current URL has been completed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeProgressListener (ProgressListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.removeProgressListener (listener);
}

/**	 
 * Removes the listener from the collection of listeners who will
 * be notified when the status text is changed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeStatusTextListener (StatusTextListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.removeStatusTextListener (listener);
}

/**	 
 * Removes the listener from the collection of listeners who will
 * be notified when the title of the current document is available
 * or has changed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeTitleListener (TitleListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.removeTitleListener (listener);
}

/**	 
 * Removes the listener from the collection of listeners who will
 * be notified when a window hosting the receiver needs to be displayed
 * or hidden.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeVisibilityWindowListener (VisibilityWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	webBrowser.removeVisibilityWindowListener (listener);
}

/**
 * Renders HTML.
 * 
 * <p>
 * The html parameter is Unicode encoded since it is a java <code>String</code>.
 * As a result, the HTML meta tag charset should not be set. The charset is implied
 * by the <code>String</code> itself.
 * 
 * @param html the HTML content to be rendered
 *
 * @return true if the operation was successful and false otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the html is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *  
 * @see #setUrl
 * 
 * @since 3.0
 */
public boolean setText (String html) {
	checkWidget();
	if (html == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return webBrowser.setText (html);
}

/**
 * Loads a URL.
 * 
 * @param url the URL to be loaded
 *
 * @return true if the operation was successful and false otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the url is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *  
 * @see #getUrl
 * 
 * @since 3.0
 */
public boolean setUrl (String url) {
	checkWidget();
	if (url == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return webBrowser.setUrl (url);
}

/**
 * Stop any loading and rendering activity.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void stop () {
	checkWidget();
	webBrowser.stop ();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6067.java