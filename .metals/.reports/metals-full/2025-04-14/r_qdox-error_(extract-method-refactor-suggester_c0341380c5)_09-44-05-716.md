error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12028.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12028.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12028.java
text:
```scala
i@@f (browser == null || browser.isDisposed ()) return;

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	int /*long*/ mozillaHandle, embedHandle;
	boolean hasFocus;
	Listener listener;
	static Callback eventCallback;
	static int /*long*/ eventProc;
	static final int STOP_PROPOGATE = 1;

	static boolean IsLinux;
	static {
		String osName = System.getProperty ("os.name").toLowerCase (); //$NON-NLS-1$
		IsLinux = osName.startsWith ("linux"); //$NON-NLS-1$
	}

MozillaDelegate (Browser browser) {
	super ();
	if (!IsLinux) {
		browser.dispose ();
		SWT.error (SWT.ERROR_NO_HANDLES, null, " [Unsupported platform]"); //$NON-NLS-1$
	}
	this.browser = browser;
}

static int /*long*/ eventProc (int /*long*/ handle, int /*long*/ gdkEvent, int /*long*/ pointer) {
	int /*long*/ parent = OS.gtk_widget_get_parent (handle);
	parent = OS.gtk_widget_get_parent (parent);
	if (parent == 0) return 0;
	Widget widget = Display.getCurrent ().findWidget (parent);
	if (widget != null && widget instanceof Browser) {
		return ((Mozilla)((Browser)widget).webBrowser).delegate.gtk_event (handle, gdkEvent, pointer);
	}
	return 0;
}

static Browser findBrowser (int /*long*/ handle) {
	/*
	* Note.  On GTK, Mozilla is embedded into a GtkHBox handle
	* and not directly into the parent Composite handle.
	*/
	int /*long*/ parent = OS.gtk_widget_get_parent (handle);
	Display display = Display.getCurrent ();
	return (Browser)display.findWidget (parent); 
}

static char[] mbcsToWcs (String codePage, byte [] buffer) {
	return Converter.mbcsToWcs (codePage, buffer);
}

static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
	return Converter.wcsToMbcs (codePage, string, terminate);
}

int /*long*/ getHandle () {
	/*
	* Bug in Mozilla Linux GTK.  Embedding Mozilla into a GtkFixed
	* handle causes problems with some Mozilla plug-ins.  For some
	* reason, the Flash plug-in causes the child of the GtkFixed
	* handle to be resized to 1 when the Flash document is loaded.
	* That could be due to gtk_container_resize_children being called
	* by Mozilla - or one of its plug-ins - on the GtkFixed handle,
	* causing the child of the GtkFixed handle to be resized to 1.
	* The workaround is to embed Mozilla into a GtkHBox handle.
	*/
	embedHandle = OS.gtk_hbox_new (false, 0);
	OS.gtk_container_add (browser.handle, embedHandle);
	OS.gtk_widget_show (embedHandle);
	return embedHandle;
}

String getLibraryName () {
	return "libxpcom.so"; //$NON-NLS-1$
}

String getSWTInitLibraryName () {
	return "swt-xpcominit"; //$NON-NLS-1$
}

int /*long*/ gtk_event (int /*long*/ handle, int /*long*/ gdkEvent, int /*long*/ pointer) {
	GdkEvent event = new GdkEvent ();
	OS.memmove (event, gdkEvent, GdkEvent.sizeof);
	if (event.type == OS.GDK_BUTTON_PRESS) {
		if (!hasFocus) browser.setFocus ();
	}

	/* 
	* Stop the propagation of events that are not consumed by Mozilla, before
	* they reach the parent embedder.  These event have already been received.
	*/
	if (pointer == STOP_PROPOGATE) return 1;
	return 0;
}

void handleFocus () {
	if (hasFocus) return;
	hasFocus = true;
	listener = new Listener () {
		public void handleEvent (Event event) {
			if (event.widget == browser) return;
			((Mozilla)browser.webBrowser).Deactivate ();
			hasFocus = false;
			browser.getDisplay ().removeFilter (SWT.FocusIn, this);
			browser.getShell ().removeListener (SWT.Deactivate, this);
			listener = null;
		}
	};
	browser.getDisplay ().addFilter (SWT.FocusIn, listener);
	browser.getShell ().addListener (SWT.Deactivate, listener);
}

void handleMouseDown () {
	int shellStyle = browser.getShell ().getStyle (); 
	if ((shellStyle & SWT.ON_TOP) != 0 && (((shellStyle & SWT.NO_FOCUS) == 0) || ((browser.getStyle () & SWT.NO_FOCUS) == 0))) {
		browser.getDisplay ().asyncExec (new Runnable () {
			public void run () {
				if (browser.isDisposed ()) return;
				((Mozilla)browser.webBrowser).Activate ();
			}
		});
	}
}

boolean hookEnterExit () {
	return false;
}

void init () {
	if (eventCallback == null) {
		eventCallback = new Callback (getClass (), "eventProc", 3); //$NON-NLS-1$
		eventProc = eventCallback.getAddress ();
		if (eventProc == 0) {
			browser.dispose ();
			Mozilla.error (SWT.ERROR_NO_MORE_CALLBACKS);
		}
	}

	/*
	* Feature in Mozilla.  GtkEvents such as key down, key pressed may be consumed
	* by Mozilla and never be received by the parent embedder.  The workaround
	* is to find the top Mozilla gtk widget that receives all the Mozilla GtkEvents,
	* i.e. the first child of the parent embedder. Then hook event callbacks and
	* forward the event to the parent embedder before Mozilla received and consumed
	* them.
	*/
	int /*long*/ list = OS.gtk_container_get_children (embedHandle);
	if (list != 0) {
		mozillaHandle = OS.g_list_data (list);
		OS.g_list_free (list);
		
		if (mozillaHandle != 0) {			
			/* Note. Callback to get events before Mozilla receives and consumes them. */
			OS.g_signal_connect (mozillaHandle, OS.event, eventProc, 0);
			
			/* 
			* Note.  Callback to get the events not consumed by Mozilla - and to block 
			* them so that they don't get propagated to the parent handle twice.  
			* This hook is set after Mozilla and is therefore called after Mozilla's 
			* handler because GTK dispatches events in their order of registration.
			*/
			OS.g_signal_connect (mozillaHandle, OS.key_press_event, eventProc, STOP_PROPOGATE);
			OS.g_signal_connect (mozillaHandle, OS.key_release_event, eventProc, STOP_PROPOGATE);
			OS.g_signal_connect (mozillaHandle, OS.button_press_event, eventProc, STOP_PROPOGATE);
		}
	}
}

boolean needsSpinup () {
	return true;
}

void onDispose (int /*long*/ embedHandle) {
	if (listener != null) {
		browser.getDisplay ().removeFilter (SWT.FocusIn, listener);
		browser.getShell ().removeListener (SWT.Deactivate, listener);
		listener = null;
	}
	browser = null;
}

void setSize (int /*long*/ embedHandle, int width, int height) {
	OS.gtk_widget_set_size_request (embedHandle, width, height);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12028.java