error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7990.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7990.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7990.java
text:
```scala
public b@@oolean setUrl(String url, String postData, String[] headers) {

/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
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
import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.widgets.*;

class IE extends WebBrowser {
	
	int webBrowser, host, jniRef;

	boolean ignoreDispose;

	static {
		NativeClearSessions = new Runnable() {
			public void run() {
//				OS.InternetSetOption (0, OS.INTERNET_OPTION_END_BROWSER_SESSION, 0, 0);
			}
		};
	}

public void create(Composite parent, int style) {
	host = OS.gcnew_WindowsFormsHost();
	if (host == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	webBrowser = OS.gcnew_WebBrowser();
	if (webBrowser == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.WindowsFormsHost_Child(host, webBrowser);
	jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.WebBrowser_ScriptErrorsSuppressed(webBrowser, true);
	int handler = OS.gcnew_WebBrowserNavigatingEventHandler(jniRef, "HandleNavigating");
	OS.WebBrowser_Navigating(webBrowser, handler);
	OS.GCHandle_Free(handler);
	handler = OS.gcnew_WebBrowserProgressChangedEventHandler(jniRef, "HandleProgressChanged");
	OS.WebBrowser_ProgressChanged(webBrowser, handler);
	OS.GCHandle_Free(handler);
	handler = OS.gcnew_EventHandler(jniRef, "HandleStatusTextChanged");
	OS.WebBrowser_StatusTextChanged(webBrowser, handler);
	OS.GCHandle_Free(handler);
	handler = OS.gcnew_EventHandler(jniRef, "HandleDocumentTitleChanged");
	OS.WebBrowser_DocumentTitleChanged(webBrowser, handler);
	OS.GCHandle_Free(handler);
	handler = OS.gcnew_WebBrowserDocumentCompletedEventHandler(jniRef, "HandleDocumentCompleted");
	OS.WebBrowser_DocumentCompleted(webBrowser, handler);
	OS.GCHandle_Free(handler);

	int parentHandle = browser.handle;
	int children = OS.Panel_Children(parentHandle);
	OS.UIElementCollection_Insert(children, 0, host);
	OS.GCHandle_Free(children);
	OS.FrameworkElement_Width(host, OS.FrameworkElement_Width(parentHandle));
	OS.FrameworkElement_Height(host, OS.FrameworkElement_Height(parentHandle));
	
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose: {
					if (ignoreDispose) {
						ignoreDispose = false;
						break;
					}
					ignoreDispose = true;
					browser.notifyListeners (event.type, event);
					event.type = SWT.NONE;
					OS.GCHandle_Free(host);
					OS.GCHandle_Free(webBrowser);
					OS.DeleteGlobalRef(jniRef);
					host = webBrowser = jniRef = 0;
					break;
				}
				case SWT.Resize: {
					OS.FrameworkElement_Width(host, OS.FrameworkElement_Width(browser.handle));
					OS.FrameworkElement_Height(host, OS.FrameworkElement_Height(browser.handle));
					break;
				}
			}
		}
	};
	browser.addListener(SWT.Resize, listener);
	browser.addListener(SWT.Dispose, listener);
}

public boolean back() {
	return OS.WebBrowser_GoBack(webBrowser);
}

public boolean execute(String script) {
//	int document = OS.WebBrowser_Document(frame);
//	int length = script.length ();
//	char [] buffer = new char [length + 1];
//	script.getChars (0, length, buffer, 0);
//	int str = OS.gcnew_String (buffer);
//	int result = OS.HtmlDocument_InvokeScript(document, str);
//	OS.GCHandle_Free(result);
//	OS.GCHandle_Free(str);
//	OS.GCHandle_Free(document);
	return true;
}

public boolean forward() {
	return OS.WebBrowser_GoForward(webBrowser);
}

public String getBrowserType () {
	return "ie"; //$NON-NLS-1$
}

public String getText () {
	// TODO
	return ""; //$NON-NLS-1$
}

public String getUrl() {
	int uri = OS.WebBrowser_Url(webBrowser);
	String url = getUriString (uri);
	if (uri != 0) OS.GCHandle_Free(uri);
	return url;
}

String getUriString(int uri) {
	if (uri == 0) return null;
	int str = OS.Object_ToString(uri);
	int charArray = OS.String_ToCharArray(str);
	char[] chars = new char[OS.String_Length(str)];
	OS.memcpy(chars, charArray, chars.length * 2);
	OS.GCHandle_Free(charArray);
	String url = new String(chars);
	OS.GCHandle_Free(str);
	return url;
}

void HandleDocumentCompleted(int sender, int e) {
	if (webBrowser == 0) return;
	int uri = OS.WebBrowserDocumentCompletedEventArgs_Url(e);
	String url = getUriString (uri);
	if (uri != 0) OS.GCHandle_Free(uri);
	LocationEvent newEvent = new LocationEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	newEvent.location = url;
	newEvent.doit = true;
	for (int i = 0; i < locationListeners.length; i++) {
		locationListeners[i].changing(newEvent);
	}
	if (browser.isDisposed()) return;
	ProgressEvent progressEvent = new ProgressEvent(browser);
	progressEvent.display = browser.getDisplay();
	progressEvent.widget = browser;
	for (int i = 0; i < progressListeners.length; i++) {
		progressListeners[i].completed(progressEvent);
	}
}

void HandleDocumentTitleChanged(int sender, int e) {
	if (webBrowser == 0) return;
	int str = OS.WebBrowser_DocumentTitle(webBrowser);
	String title = "";
	if (str != 0) {
		int charArray = OS.String_ToCharArray(str);
		char[] chars = new char[OS.String_Length(str)];
		OS.memcpy(chars, charArray, chars.length * 2);
		OS.GCHandle_Free(charArray);
		OS.GCHandle_Free(str);
		title = new String(chars);
	}
	TitleEvent newEvent = new TitleEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	newEvent.title = title;
	for (int i = 0; i < titleListeners.length; i++) {
		titleListeners[i].changed(newEvent);
	}
}

void HandleNavigating(int sender, int e) {
	if (webBrowser == 0) return;
	int uri = OS.WebBrowserNavigatingEventArgs_Url(e);
	String url = getUriString (uri);
	if (uri != 0) OS.GCHandle_Free(uri);
	LocationEvent newEvent = new LocationEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	newEvent.location = url;
	newEvent.doit = true;
	for (int i = 0; i < locationListeners.length; i++) {
		locationListeners[i].changing(newEvent);
	}
}

void HandleProgressChanged(int sender, int e) {
	if (webBrowser == 0) return;
	long nProgress = OS.WebBrowserProgressChangedEventArgs_CurrentProgress(e);
	long nProgressMax = OS.WebBrowserProgressChangedEventArgs_MaximumProgress(e);
	if (nProgress != -1) {
		ProgressEvent newEvent = new ProgressEvent(browser);
		newEvent.display = browser.getDisplay();
		newEvent.widget = browser;
		newEvent.current = (int)nProgress;
		newEvent.total = (int)nProgressMax;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].changed(newEvent);
		}
	}
}

void HandleStatusTextChanged(int sender, int e) {
	if (webBrowser == 0) return;
	int str = OS.WebBrowser_StatusText(webBrowser);
	String text = "";
	if (str != 0) {
		int charArray = OS.String_ToCharArray(str);
		char[] chars = new char[OS.String_Length(str)];
		OS.memcpy(chars, charArray, chars.length * 2);
		OS.GCHandle_Free(charArray);
		OS.GCHandle_Free(str);
		text = new String(chars);
	}
	StatusTextEvent newEvent = new StatusTextEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	newEvent.text = text;
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed(newEvent);
	}
}

public boolean isBackEnabled() {
	return OS.WebBrowser_CanGoBack(webBrowser);
}

public boolean isForwardEnabled() {
	return OS.WebBrowser_CanGoForward(webBrowser);
}

public void refresh() {
	OS.WebBrowser_Refresh(webBrowser);
}

public boolean setText(String html) {
	int state = OS.WebBrowser_ReadyState(webBrowser);
	if (!(state == OS.WebBrowserReadyState_Uninitialized || state == OS.WebBrowserReadyState_Complete)) {
		OS.WebBrowser_Stop(webBrowser);
	}
	int length = html.length ();
	char [] buffer = new char [length + 1];
	html.getChars (0, length, buffer, 0);
	int str = OS.gcnew_String (buffer);
	if (str == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	OS.WebBrowser_DocumentText(webBrowser, str);
	OS.GCHandle_Free(str);
	return true;
}

public boolean setUrl(String url) {
	int state = OS.WebBrowser_ReadyState(webBrowser);
	if (!(state == OS.WebBrowserReadyState_Uninitialized || state == OS.WebBrowserReadyState_Complete)) {
		OS.WebBrowser_Stop(webBrowser);
	}
	int length = url.length ();
	char [] buffer = new char [length + 1];
	url.getChars (0, length, buffer, 0);
	int str = OS.gcnew_String (buffer);
	OS.WebBrowser_Navigate(webBrowser, str);
	OS.GCHandle_Free(str);
	return true;
}

public void stop() {
	OS.WebBrowser_Stop(webBrowser);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7990.java