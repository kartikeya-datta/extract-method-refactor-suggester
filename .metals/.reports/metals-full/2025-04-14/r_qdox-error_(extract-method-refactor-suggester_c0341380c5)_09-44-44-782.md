error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15549.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15549.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,28]

error in qdox parser
file content:
```java
offset: 28
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15549.java
text:
```scala
public static final native i@@nt getNativeHandleFromAWT(Object frame);

/**********************************************************************
 * Copyright (c) 2003-2006 IBM Corp.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.cocoa;

import org.eclipse.swt.internal.*;

public class Cocoa extends Platform {
		
static {
	Library.loadLibrary("swt-cocoa"); //$NON-NLS-1$
	WebInitForCarbon();
}
	
/* Objective-C class ids */
public static final int C_NSHTTPCookieStorage = Cocoa.objc_getClass("NSHTTPCookieStorage"); //$NON-NLS-1$
public static final int C_NSNotificationCenter = Cocoa.objc_getClass("NSNotificationCenter"); //$NON-NLS-1$
public static final int C_NSNumber = Cocoa.objc_getClass("NSNumber"); //$NON-NLS-1$
public static final int C_NSURL = Cocoa.objc_getClass("NSURL"); //$NON-NLS-1$
public static final int C_NSURLRequest = Cocoa.objc_getClass("NSURLRequest"); //$NON-NLS-1$
public static final int C_WebKitDelegate = Cocoa.objc_getClass("WebKitDelegate"); //$NON-NLS-1$
public static final int C_WebDownload = Cocoa.objc_getClass("WebDownload"); //$NON-NLS-1$
public static final int C_WebView = Cocoa.objc_getClass("WebView"); //$NON-NLS-1$
public static final int C_NSStatusBar = Cocoa.objc_getClass("NSStatusBar"); //$NON-NLS-1$
public static final int C_NSImage = Cocoa.objc_getClass("NSImage"); //$NON-NLS-1$
public static final int C_NSGraphicsContext = Cocoa.objc_getClass("NSGraphicsContext"); //$NON-NLS-1$
public static final int C_NSStatusItemImageView = Cocoa.objc_getClass("NSStatusItemImageView"); //$NON-NLS-1$
public static final int C_NSCursor = Cocoa.objc_getClass("NSCursor"); //$NON-NLS-1$
public static final int C_NSWindow = Cocoa.objc_getClass("NSWindow"); //$NON-NLS-1$
public static final int C_NSBitmapImageRep = Cocoa.objc_getClass("NSBitmapImageRep"); //$NON-NLS-1$

/* Objective-C method selectors */
public static final int S_absoluteString = Cocoa.sel_registerName("absoluteString"); //$NON-NLS-1$
public static final int S_addObserver_selector_name_object = Cocoa.sel_registerName("addObserver:selector:name:object:"); //$NON-NLS-1$
public static final int S_alloc = Cocoa.sel_registerName("alloc"); //$NON-NLS-1$
public static final int S_autorelease = Cocoa.sel_registerName("autorelease"); //$NON-NLS-1$
public static final int S_cancel = Cocoa.sel_registerName("cancel"); //$NON-NLS-1$
public static final int S_canGoBack = Cocoa.sel_registerName("canGoBack"); //$NON-NLS-1$
public static final int S_canGoForward = Cocoa.sel_registerName("canGoForward"); //$NON-NLS-1$
public static final int S_canShowMIMEType = Cocoa.sel_registerName("canShowMIMEType:"); //$NON-NLS-1$
public static final int S_chooseFilename = Cocoa.sel_registerName("chooseFilename:"); //$NON-NLS-1$
public static final int S_cookies = Cocoa.sel_registerName("cookies"); //$NON-NLS-1$
public static final int S_count = Cocoa.sel_registerName("count"); //$NON-NLS-1$
public static final int S_dataSource = Cocoa.sel_registerName("dataSource"); //$NON-NLS-1$
public static final int S_defaultCenter = Cocoa.sel_registerName("defaultCenter"); //$NON-NLS-1$
public static final int S_deleteCookie = Cocoa.sel_registerName("deleteCookie:"); //$NON-NLS-1$
public static final int S_download = Cocoa.sel_registerName("download"); //$NON-NLS-1$
public static final int S_goBack = Cocoa.sel_registerName("goBack:"); //$NON-NLS-1$
public static final int S_goForward = Cocoa.sel_registerName("goForward:"); //$NON-NLS-1$
public static final int S_handleNotification = Cocoa.sel_registerName("handleNotification:"); //$NON-NLS-1$
public static final int S_ignore = Cocoa.sel_registerName("ignore"); //$NON-NLS-1$
public static final int S_initialRequest = Cocoa.sel_registerName("initialRequest"); //$NON-NLS-1$
public static final int S_initWithProc = Cocoa.sel_registerName("initWithProc:user_data:"); //$NON-NLS-1$
public static final int S_isSessionOnly = Cocoa.sel_registerName("isSessionOnly"); //$NON-NLS-1$
public static final int S_loadHTMLStringbaseURL = Cocoa.sel_registerName("loadHTMLString:baseURL:"); //$NON-NLS-1$
public static final int S_loadRequest = Cocoa.sel_registerName("loadRequest:"); //$NON-NLS-1$
public static final int S_mainFrame = Cocoa.sel_registerName("mainFrame"); //$NON-NLS-1$
public static final int S_name = Cocoa.sel_registerName("name"); //$NON-NLS-1$
public static final int S_numberWithInt = Cocoa.sel_registerName("numberWithInt:"); //$NON-NLS-1$
public static final int S_objectAtIndex = Cocoa.sel_registerName("objectAtIndex:"); //$NON-NLS-1$
public static final int S_pageTitle = Cocoa.sel_registerName("pageTitle"); //$NON-NLS-1$
public static final int S_provisionalDataSource = Cocoa.sel_registerName("provisionalDataSource"); //$NON-NLS-1$
public static final int S_release = Cocoa.sel_registerName("release"); //$NON-NLS-1$
public static final int S_reload = Cocoa.sel_registerName("reload:"); //$NON-NLS-1$
public static final int S_retain = Cocoa.sel_registerName("retain"); //$NON-NLS-1$
public static final int S_removeObserver_name_object = Cocoa.sel_registerName("removeObserver:name:object:"); //$NON-NLS-1$
public static final int S_removeObserver = Cocoa.sel_registerName("removeObserver:"); //$NON-NLS-1$
public static final int S_requestWithURL = Cocoa.sel_registerName("requestWithURL:"); //$NON-NLS-1$
public static final int S_request = Cocoa.sel_registerName("request"); //$NON-NLS-1$
public static final int S_retainCount = Cocoa.sel_registerName("retainCount"); //$NON-NLS-1$
public static final int S_setDestinationAllowOverwrite = Cocoa.sel_registerName("setDestination:allowOverwrite:"); //$NON-NLS-1$
public static final int S_setDownloadDelegate = Cocoa.sel_registerName("setDownloadDelegate:"); //$NON-NLS-1$
public static final int S_setFrameLoadDelegate = Cocoa.sel_registerName("setFrameLoadDelegate:"); //$NON-NLS-1$
public static final int S_setGroupName = Cocoa.sel_registerName("setGroupName:"); //$NON-NLS-1$
public static final int S_setPolicyDelegate = Cocoa.sel_registerName("setPolicyDelegate:"); //$NON-NLS-1$
public static final int S_setResourceLoadDelegate = Cocoa.sel_registerName("setResourceLoadDelegate:"); //$NON-NLS-1$
public static final int S_setStatusText = Cocoa.sel_registerName("setStatusText:"); //$NON-NLS-1$
public static final int S_setUIDelegate = Cocoa.sel_registerName("setUIDelegate:"); //$NON-NLS-1$
public static final int S_sharedHTTPCookieStorage = Cocoa.sel_registerName("sharedHTTPCookieStorage"); //$NON-NLS-1$
public static final int S_stopLoading = Cocoa.sel_registerName("stopLoading:"); //$NON-NLS-1$
public static final int S_stringByEvaluatingJavaScriptFromString = Cocoa.sel_registerName("stringByEvaluatingJavaScriptFromString:"); //$NON-NLS-1$
public static final int S_takeStringURLFrom = Cocoa.sel_registerName("takeStringURLFrom:"); //$NON-NLS-1$
public static final int S_use = Cocoa.sel_registerName("use"); //$NON-NLS-1$
public static final int S_webFrame = Cocoa.sel_registerName("webFrame"); //$NON-NLS-1$
public static final int S_URL = Cocoa.sel_registerName("URL"); //$NON-NLS-1$
public static final int S_URLWithString = Cocoa.sel_registerName("URLWithString:"); //$NON-NLS-1$
public static final int S_systemStatusBar = Cocoa.sel_registerName("systemStatusBar"); //$NON-NLS-1$
public static final int S_statusItemWithLength = Cocoa.sel_registerName("statusItemWithLength:"); //$NON-NLS-1$
public static final int S_setTitle = Cocoa.sel_registerName("setTitle:"); //$NON-NLS-1$
public static final int S_setHighlightMode = Cocoa.sel_registerName("setHighlightMode:"); //$NON-NLS-1$
public static final int S_setToolTip = Cocoa.sel_registerName("setToolTip:"); //$NON-NLS-1$
public static final int S_setImage = Cocoa.sel_registerName("setImage:"); //$NON-NLS-1$
public static final int S_removeStatusItem = Cocoa.sel_registerName("removeStatusItem:"); //$NON-NLS-1$
public static final int S_initWithSize = Cocoa.sel_registerName("initWithSize:"); //$NON-NLS-1$
public static final int S_initWithFrame = Cocoa.sel_registerName("initWithFrame:"); //$NON-NLS-1$
public static final int S_initWithProc_frame_user_data = Cocoa.sel_registerName("initWithProc:frame:user_data:"); //$NON-NLS-1$
public static final int S_lockFocus = Cocoa.sel_registerName("lockFocus"); //$NON-NLS-1$
public static final int S_unlockFocus = Cocoa.sel_registerName("unlockFocus"); //$NON-NLS-1$
public static final int S_currentContext = Cocoa.sel_registerName("currentContext"); //$NON-NLS-1$
public static final int S_graphicsPort = Cocoa.sel_registerName("graphicsPort"); //$NON-NLS-1$
public static final int S_setLength = Cocoa.sel_registerName("setLength:"); //$NON-NLS-1$
public static final int S_view = Cocoa.sel_registerName("view"); //$NON-NLS-1$
public static final int S_setView = Cocoa.sel_registerName("setView:"); //$NON-NLS-1$
public static final int S_clickCount = Cocoa.sel_registerName("clickCount"); //$NON-NLS-1$
public static final int S_drawStatusBarBackgroundInRect_withHighlight = Cocoa.sel_registerName("drawStatusBarBackgroundInRect:withHighlight:"); //$NON-NLS-1$
public static final int S_drawRect = Cocoa.sel_registerName("drawRect:"); //$NON-NLS-1$
public static final int S_setNeedsDisplay = Cocoa.sel_registerName("setNeedsDisplay:"); //$NON-NLS-1$
public static final int S_getLocation = Cocoa.sel_registerName("getLocation:"); //$NON-NLS-1$
public static final int S_initWithImage_hotSpot = Cocoa.sel_registerName("initWithImage:hotSpot:"); //$NON-NLS-1$
public static final int S_set = Cocoa.sel_registerName("set"); //$NON-NLS-1$
public static final int S_init = Cocoa.sel_registerName("init"); //$NON-NLS-1$
public static final int S_addRepresentation = Cocoa.sel_registerName("addRepresentation:"); //$NON-NLS-1$
public static final int S_initWithBitmapDataPlanes = Cocoa.sel_registerName("initWithBitmapDataPlanes:pixelsWide:pixelsHigh:bitsPerSample:samplesPerPixel:hasAlpha:isPlanar:colorSpaceName:bitmapFormat:bytesPerRow:bitsPerPixel:"); //$NON-NLS-1$
public static final int S_bitmapData = Cocoa.sel_registerName("bitmapData"); //$NON-NLS-1$

public static final int NSAlphaFirstBitmapFormat = 1 << 0;
public static final int NSAlphaNonpremultipliedBitmapFormat = 1 << 1;

/* WebKit */
public static final native int HIWebViewCreate(int[] outView);
public static final native int HIWebViewGetWebView(int inView);
public static final native void WebInitForCarbon();

/* OBJ-C runtime primitives */
public static final native int objc_getClass(byte[] className);
public static final native int objc_msgSend(int object, int selector);
public static final native int objc_msgSend(int object, int selector, int arg0);
public static final native int objc_msgSend(int object, int selector, float arg0);
public static final native int objc_msgSend(int object, int selector, NSSize arg0);
public static final native int objc_msgSend(int object, int selector, NSRect arg0);
public static final native int objc_msgSend(int object, int selector, int arg0, NSRect arg1, int arg2);
public static final native int objc_msgSend(int object, int selector, NSRect arg0, int arg1);
public static final native int objc_msgSend(int object, int selector, NSPoint arg0, int arg1);
public static final native int objc_msgSend(int object, int selector, int arg0, NSPoint arg1);
public static final native int objc_msgSend(int object, int selector, NSPoint arg0);
public static final native int objc_msgSend(int object, int selector, int arg0, int arg1);
public static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2);
public static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2, int arg3);
public static final native int objc_msgSend(int object, int selector, int[] arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10);
public static final native int sel_registerName(byte[] selectorName);

public static final native int NSDeviceRGBColorSpace();

public static final native void memcpy(NSRect dest, int src, int size);

static byte [] ascii (String name) {
	int length = name.length ();
	char [] chars = new char [length];
	name.getChars (0, length, chars, 0);
	byte [] buffer = new byte [length + 1];
	for (int i=0; i<length; i++) {
		buffer [i] = (byte) chars [i];
	}
	return buffer;
}

static int sel_registerName(String selector) {
	return Cocoa.sel_registerName(ascii(selector));
}
	
static int objc_getClass(String className) {
	return Cocoa.objc_getClass(ascii(className));
}

public static final native long getNativeHandleFromAWT(Object frame);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15549.java