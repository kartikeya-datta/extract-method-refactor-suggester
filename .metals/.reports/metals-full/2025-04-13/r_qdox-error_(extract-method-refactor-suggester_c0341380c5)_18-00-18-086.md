error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2093.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2093.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2093.java
text:
```scala
static final i@@nt S_initWithProc = WebKit.sel_registerName("initWithProc:user_data:"); //$NON-NLS-1$

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
package org.eclipse.swt.browser;

import org.eclipse.swt.internal.*;

class WebKit {
		
static {
	Library.loadLibrary("swt-webkit"); //$NON-NLS-1$
	WebInitForCarbon();
}
	
/* Objective-C class ids */
static final int C_NSNotificationCenter = WebKit.objc_getClass("NSNotificationCenter"); //$NON-NLS-1$
static final int C_NSNumber = WebKit.objc_getClass("NSNumber"); //$NON-NLS-1$
static final int C_NSURL = WebKit.objc_getClass("NSURL"); //$NON-NLS-1$
static final int C_NSURLRequest = WebKit.objc_getClass("NSURLRequest"); //$NON-NLS-1$
static final int C_WebKitDelegate = WebKit.objc_getClass("WebKitDelegate"); //$NON-NLS-1$
	
/* Objective-C method selectors */
static final int S_absoluteString = WebKit.sel_registerName("absoluteString"); //$NON-NLS-1$
static final int S_addObserver_selector_name_object = WebKit.sel_registerName("addObserver:selector:name:object:"); //$NON-NLS-1$
static final int S_alloc = WebKit.sel_registerName("alloc"); //$NON-NLS-1$
static final int S_autorelease = WebKit.sel_registerName("autorelease"); //$NON-NLS-1$
static final int S_canGoBack = WebKit.sel_registerName("canGoBack"); //$NON-NLS-1$
static final int S_canGoForward = WebKit.sel_registerName("canGoForward"); //$NON-NLS-1$
static final int S_dataSource = WebKit.sel_registerName("dataSource"); //$NON-NLS-1$
static final int S_defaultCenter = WebKit.sel_registerName("defaultCenter"); //$NON-NLS-1$
static final int S_goBack = WebKit.sel_registerName("goBack:"); //$NON-NLS-1$
static final int S_goForward = WebKit.sel_registerName("goForward:"); //$NON-NLS-1$
static final int S_handleNotification = WebKit.sel_registerName("handleNotification:"); //$NON-NLS-1$
static final int S_ignore = WebKit.sel_registerName("ignore"); //$NON-NLS-1$
static final int S_initialRequest = WebKit.sel_registerName("initialRequest"); //$NON-NLS-1$
static final int S_initWithProc = WebKit.sel_registerName("initWithProc:"); //$NON-NLS-1$
static final int S_loadHTMLStringbaseURL = WebKit.sel_registerName("loadHTMLString:baseURL:"); //$NON-NLS-1$
static final int S_loadRequest = WebKit.sel_registerName("loadRequest:"); //$NON-NLS-1$
static final int S_mainFrame = WebKit.sel_registerName("mainFrame"); //$NON-NLS-1$
static final int S_name = WebKit.sel_registerName("name"); //$NON-NLS-1$
static final int S_numberWithInt = WebKit.sel_registerName("numberWithInt:"); //$NON-NLS-1$
static final int S_provisionalDataSource = WebKit.sel_registerName("provisionalDataSource"); //$NON-NLS-1$
static final int S_release = WebKit.sel_registerName("release"); //$NON-NLS-1$
static final int S_reload = WebKit.sel_registerName("reload:"); //$NON-NLS-1$
static final int S_retain = WebKit.sel_registerName("retain"); //$NON-NLS-1$
static final int S_removeObserver_name_object = WebKit.sel_registerName("removeObserver:name:object:"); //$NON-NLS-1$
static final int S_requestWithURL = WebKit.sel_registerName("requestWithURL:"); //$NON-NLS-1$
static final int S_request = WebKit.sel_registerName("request"); //$NON-NLS-1$
static final int S_retainCount = WebKit.sel_registerName("retainCount"); //$NON-NLS-1$
static final int S_setFrameLoadDelegate = WebKit.sel_registerName("setFrameLoadDelegate:"); //$NON-NLS-1$
static final int S_setGroupName = WebKit.sel_registerName("setGroupName:"); //$NON-NLS-1$
static final int S_setPolicyDelegate = WebKit.sel_registerName("setPolicyDelegate:"); //$NON-NLS-1$
static final int S_setResourceLoadDelegate = WebKit.sel_registerName("setResourceLoadDelegate:"); //$NON-NLS-1$
static final int S_setStatusText = WebKit.sel_registerName("setStatusText:"); //$NON-NLS-1$
static final int S_setUIDelegate = WebKit.sel_registerName("setUIDelegate:"); //$NON-NLS-1$
static final int S_stopLoading = WebKit.sel_registerName("stopLoading:"); //$NON-NLS-1$
static final int S_stringByEvaluatingJavaScriptFromString = WebKit.sel_registerName("stringByEvaluatingJavaScriptFromString:"); //$NON-NLS-1$
static final int S_takeStringURLFrom = WebKit.sel_registerName("takeStringURLFrom:"); //$NON-NLS-1$
static final int S_use = WebKit.sel_registerName("use"); //$NON-NLS-1$
static final int S_webFrame = WebKit.sel_registerName("webFrame"); //$NON-NLS-1$
static final int S_URL = WebKit.sel_registerName("URL"); //$NON-NLS-1$
static final int S_URLWithString = WebKit.sel_registerName("URLWithString:"); //$NON-NLS-1$
	
/* WebKit */
static final native int HIWebViewCreate(int[] outView);
static final native int HIWebViewGetWebView(int inView);
static final native void WebInitForCarbon();
	
/* OBJ-C runtime primitives */
static final native int objc_getClass(byte[] className);
static final native int objc_msgSend(int object, int selector);
static final native int objc_msgSend(int object, int selector, int arg0);
static final native int objc_msgSend(int object, int selector, int arg0, int arg1);
static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2);
static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2, int arg3);
static final native int sel_registerName(byte[] selectorName);

static int sel_registerName(String selector) {
	byte[] buffer0 = selector.getBytes();
	int length = buffer0.length;
	byte[] buffer = new byte[length+1];
	System.arraycopy(buffer0, 0, buffer, 0, length);
	return WebKit.sel_registerName(buffer);
}
	
static int objc_getClass(String className) {
	byte[] buffer0 = className.getBytes();
	int length = buffer0.length;
	byte[] buffer = new byte[length+1];
	System.arraycopy(buffer0, 0, buffer, 0, length);
	return WebKit.objc_getClass(buffer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2093.java