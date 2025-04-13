error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12029.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12029.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12029.java
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.GTK;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	Listener listener;
	boolean hasFocus;

	static boolean GtkLoaded, IsLinux;
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
	
	if (!GtkLoaded) {
		GtkLoaded = true;
		try {
			Library.loadLibrary ("swt-gtk"); //$NON-NLS-1$
		} catch (UnsatisfiedLinkError e) {
			browser.dispose ();
			SWT.error (SWT.ERROR_NO_HANDLES, e);
		}
	}

}

static Browser findBrowser (int handle) {
	Display display = Display.getCurrent ();
	Shell[] shells = display.getShells ();
	Browser browser = null;
	for (int i = 0; i < shells.length; i++) {
		browser = findBrowser (shells[i], handle);
		if (browser != null) break;
	}
	return browser; 
}

static Browser findBrowser (Control control, int gtkHandle) {
	if (control instanceof Browser) {
		Browser browser = (Browser)control;
		WebBrowser webBrowser = browser.webBrowser;
		if (webBrowser instanceof Mozilla) {
			if (((Mozilla)webBrowser).embedHandle == gtkHandle) return browser;
		}
	}
	if (control instanceof Composite) {
		Composite composite = (Composite)control;
		Control[] children = composite.getChildren ();
		for (int i = 0; i < children.length; i++) {
			Browser browser = findBrowser (children[i], gtkHandle);
			if (browser != null) return browser;
		}
	}
	return null;
}

static char[] mbcsToWcs (String codePage, byte [] buffer) {
	return Converter.mbcsToWcs (codePage, buffer);
}

static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
	return Converter.wcsToMbcs (codePage, string, terminate);
}

int getHandle() {
	if (Mozilla.BrowserCount == 1) {
		GTK.gtk_init_check (new int[1], null);
		final Display display = browser.getDisplay ();
		display.asyncExec (new Runnable () {
			public void run () {
				if (Mozilla.BrowserCount == 0) return;
				while (GTK.gtk_events_pending () != 0) {
					GTK.gtk_main_iteration ();
				}
				display.timerExec (25, this);
			}
		});
	}
	browser.getShell ().setFocus ();
	int result = GTK.gtk_plug_new (browser.embeddedHandle);
	GTK.gtk_widget_show (result);
	return result;
}

String getLibraryName () {
	return "libxpcom.so"; //$NON-NLS-1$
}

String getSWTInitLibraryName () {
	return "swt-xpcominit"; //$NON-NLS-1$
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
	return true;
}

void init () {
}

boolean needsSpinup () {
	return true;
}

void onDispose (int embedHandle) {
	if (listener != null) {
		browser.getDisplay ().removeFilter (SWT.FocusIn, listener);
		browser.getShell ().removeListener (SWT.Deactivate, listener);
		listener = null;
	}

	GTK.gtk_widget_destroy (embedHandle);
	while (GTK.gtk_events_pending () != 0) {
		GTK.gtk_main_iteration ();
	}

	browser = null;
}

void setSize(int embedHandle, int width, int height) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12029.java