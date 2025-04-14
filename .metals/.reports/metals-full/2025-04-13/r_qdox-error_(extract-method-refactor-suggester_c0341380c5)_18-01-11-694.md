error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12300.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12300.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12300.java
text:
```scala
w@@ebBrowserChrome.SetChromeFlags ((int)/*64*/chromeFlags);

/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class WindowCreator2 {
	XPCOMObject supports;
	XPCOMObject windowCreator;
	XPCOMObject windowCreator2;
	int refCount = 0;

public WindowCreator2 () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
	};

	windowCreator = new XPCOMObject (new int[] {2, 0, 0, 3}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return CreateChromeWindow (args[0], args[1], args[2]);}
	};

	windowCreator2 = new XPCOMObject (new int[] {2, 0, 0, 3, 6}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return CreateChromeWindow (args[0], args[1], args[2]);}
		public int /*long*/ method4 (int /*long*/[] args) {return CreateChromeWindow2 (args[0], args[1], args[2], args[3], args[4], args[5]);}
	};
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (windowCreator != null) {
		windowCreator.dispose ();
		windowCreator = null;	
	}

	if (windowCreator2 != null) {
		windowCreator2.dispose ();
		windowCreator2 = null;	
	}
}

int /*long*/ getAddress () {
	return windowCreator.getAddress ();
}

int /*long*/ QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);
	
	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (XPCOM.NS_IWINDOWCREATOR_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {windowCreator.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (XPCOM.NS_IWINDOWCREATOR2_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {windowCreator2.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}

	XPCOM.memmove (ppvObject, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}
        	
int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}
	
/* nsIWindowCreator */

int /*long*/ CreateChromeWindow (int /*long*/ parent, int /*long*/ chromeFlags, int /*long*/ _retval) {
	return CreateChromeWindow2 (parent, chromeFlags, 0, 0, 0, _retval);
}

/* nsIWindowCreator2 */

int /*long*/ CreateChromeWindow2 (int /*long*/ parent, int /*long*/ chromeFlags, int /*long*/ contextFlags, int /*long*/ uri, int /*long*/ cancel, int /*long*/ _retval) {
	if (parent == 0 && (chromeFlags & nsIWebBrowserChrome.CHROME_MODAL) == 0) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}
	Browser src = null; 
	if (parent != 0) {
		nsIWebBrowserChrome browserChromeParent = new nsIWebBrowserChrome (parent);
		int /*long*/[] aWebBrowser = new int /*long*/[1];
		int rc = browserChromeParent.GetWebBrowser (aWebBrowser);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (aWebBrowser[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);

		nsIWebBrowser webBrowser = new nsIWebBrowser (aWebBrowser[0]);
		int /*long*/[] result = new int /*long*/[1];
		rc = webBrowser.QueryInterface (nsIBaseWindow.NS_IBASEWINDOW_IID, result);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);
		webBrowser.Release ();

		nsIBaseWindow baseWindow = new nsIBaseWindow (result[0]);
		result[0] = 0;
		int /*long*/[] aParentNativeWindow = new int /*long*/[1];
		rc = baseWindow.GetParentNativeWindow (aParentNativeWindow);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (aParentNativeWindow[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);
		baseWindow.Release ();

		src = Mozilla.findBrowser (aParentNativeWindow[0]);
	}
	final Browser browser;
	boolean doit = true;
	if ((chromeFlags & nsIWebBrowserChrome.CHROME_MODAL) != 0) {
		/*
		* Mozilla will request a new Browser in a modal window in order to emulate a native
		* dialog that is not available to it (eg.- a print dialog on Linux).  For this
		* reason modal requests are handled here so that the user is not exposed to them.
		*/
		final Shell shell = src == null ?
			new Shell (SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL) :
			new Shell (src.getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setLayout (new FillLayout ());
		browser = new Browser (shell, src == null ? SWT.MOZILLA : src.getStyle () & SWT.MOZILLA);
		browser.addVisibilityWindowListener (new VisibilityWindowListener () {
			public void hide (WindowEvent event) {
			}
			public void show (WindowEvent event) {
				if (event.location != null) shell.setLocation (event.location);
				if (event.size != null) {
					Point size = event.size;
					shell.setSize (shell.computeSize (size.x, size.y));
				}
				shell.open ();
			}
		});
		browser.addCloseWindowListener (new CloseWindowListener () {
			public void close (WindowEvent event) {
				shell.close ();
			}
		});
	} else {
		WindowEvent event = new WindowEvent (src);
		event.display = src.getDisplay ();
		event.widget = src;
		event.required = true;
		for (int i = 0; i < src.webBrowser.openWindowListeners.length; i++) {
			src.webBrowser.openWindowListeners[i].open (event);
		}
		browser = event.browser;
		doit = browser != null && !browser.isDisposed ();
	}
	if (doit) {
		int /*long*/ chromePtr = ((Mozilla)browser.webBrowser).webBrowserChrome.getAddress ();
		nsIWebBrowserChrome webBrowserChrome = new nsIWebBrowserChrome (chromePtr);
		webBrowserChrome.SetChromeFlags (chromeFlags);
		webBrowserChrome.AddRef ();
		XPCOM.memmove (_retval, new int /*long*/[] {chromePtr}, C.PTR_SIZEOF);

		if (uri != 0) {
			nsIURI location = new nsIURI (uri);
			int /*long*/ aSpec = XPCOM.nsEmbedCString_new ();
			if (location.GetSpec (aSpec) == XPCOM.NS_OK) {
				int length = XPCOM.nsEmbedCString_Length (aSpec);
				if (length > 0) {
					int /*long*/ buffer = XPCOM.nsEmbedCString_get (aSpec);
					byte[] dest = new byte[length];
					XPCOM.memmove (dest, buffer, length);
					browser.setUrl (new String (dest));
				}
			}
			XPCOM.nsEmbedCString_delete (aSpec);
		}
	} else {
		if (cancel != 0) {
			C.memmove (cancel, new int[] {1}, 4);	/* PRBool */
		}
	}
	return doit ? XPCOM.NS_OK : XPCOM.NS_ERROR_NOT_IMPLEMENTED;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12300.java