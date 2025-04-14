error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18231.java
text:
```scala
public b@@oolean setText(String html, boolean trusted) {

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
	
	int frame;

	boolean ignoreDispose;

	static {
		NativeClearSessions = new Runnable() {
			public void run() {
//				OS.InternetSetOption (0, OS.INTERNET_OPTION_END_BROWSER_SESSION, 0, 0);
			}
		};
	}

public void create(Composite parent, int style) {
	frame = OS.gcnew_Frame();
	if (frame == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.Frame_NavigationUIVisibility(frame, OS.NavigationUIVisibility_Hidden);
	int parentHandle = browser.handle;
	int children = OS.Panel_Children(parentHandle);
	OS.UIElementCollection_Insert(children, 0, frame);
	OS.GCHandle_Free(children);
	OS.FrameworkElement_Width(frame, OS.FrameworkElement_Width(parentHandle));
	OS.FrameworkElement_Height(frame, OS.FrameworkElement_Height(parentHandle));
	
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
					OS.GCHandle_Free(frame);
					frame = 0;
					break;
				}
				case SWT.Resize: {
					OS.FrameworkElement_Width(frame, OS.FrameworkElement_Width(browser.handle));
					OS.FrameworkElement_Height(frame, OS.FrameworkElement_Height(browser.handle));
					break;
				}
			}
		}
	};
	browser.addListener(SWT.Resize, listener);
	browser.addListener(SWT.Dispose, listener);
}

public boolean back() {
	if (!OS.Frame_CanGoBack(frame)) return false;
	OS.Frame_GoBack(frame);
	return true;
}

public boolean execute(String script) {
//	/* get IHTMLDocument2 */
//	int[] rgdispid = auto.getIDsOfNames(new String[]{"Document"}); //$NON-NLS-1$
//	int dispIdMember = rgdispid[0];
//	Variant pVarResult = auto.getProperty(dispIdMember);
//	if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) return false;
//	OleAutomation document = pVarResult.getAutomation();
//	pVarResult.dispose();
//
//	/* get IHTMLWindow2 */
//	rgdispid = document.getIDsOfNames(new String[]{"parentWindow"}); //$NON-NLS-1$
//	if (rgdispid == null) {
//		/* implies that browser's content is not a IHTMLDocument2 (eg.- acrobat reader) */
//		document.dispose();
//		return false;
//	}
//	dispIdMember = rgdispid[0];
//	pVarResult = document.getProperty(dispIdMember);
//	OleAutomation ihtmlWindow2 = pVarResult.getAutomation();
//	pVarResult.dispose();
//	document.dispose();
//	
//	rgdispid = ihtmlWindow2.getIDsOfNames(new String[] { "execScript", "code" }); //$NON-NLS-1$  //$NON-NLS-2$
//	Variant[] rgvarg = new Variant[1];
//	rgvarg[0] = new Variant(script);
//	int[] rgdispidNamedArgs = new int[1];
//	rgdispidNamedArgs[0] = rgdispid[1];
//	pVarResult = ihtmlWindow2.invoke(rgdispid[0], rgvarg, rgdispidNamedArgs);
//	rgvarg[0].dispose();
//	ihtmlWindow2.dispose();
//	if (pVarResult == null) return false;
//	pVarResult.dispose();
	return true;
}

public boolean forward() {
	if (!OS.Frame_CanGoForward(frame)) return false;
	OS.Frame_GoForward(frame);
	return true;
}

public String getBrowserType () {
	return "ie"; //$NON-NLS-1$
}

public String getUrl() {
	int uri = OS.Frame_Source(frame);
	int str = OS.Object_ToString(uri);
	int charArray = OS.String_ToCharArray(str);
	char[] chars = new char[OS.String_Length(str)];
	OS.memcpy(chars, charArray, chars.length * 2);
	OS.GCHandle_Free(charArray);
	String url = new String(chars);
	OS.GCHandle_Free(str);
	OS.GCHandle_Free(uri);
	return url;
}

public boolean isBackEnabled() {
	return OS.Frame_CanGoBack(frame);
}

public boolean isForwardEnabled() {
	return OS.Frame_CanGoForward(frame);
}

public void refresh() {
	OS.Frame_Refresh(frame);
}

public boolean setText(String html) {
	return true;
//	/*
//	* If the html field is non-null then the about:blank page is already being
//	* loaded, so no Stop or Navigate is required.  Just set the html that is to
//	* be shown.
//	*/
//	boolean blankLoading = this.html != null;
//	this.html = html;
//	if (blankLoading) return true;
//	
//	/*
//	* Navigate to the blank page and insert the given html when
//	* receiving the next DocumentComplete notification.  See the
//	* MSDN article "Loading HTML content from a Stream".
//	* 
//	* Note.  Stop any pending request.  This is required to avoid displaying a
//	* blank page as a result of consecutive calls to setUrl and/or setText.  
//	* The previous request would otherwise render the new html content and
//	* reset the html field before the browser actually navigates to the blank
//	* page as requested below.
//	* 
//	* Feature in Internet Explorer.  Stopping pending requests when no request
//	* is pending causes a default page 'Action cancelled' to be displayed.  The
//	* workaround is to not invoke 'stop' when no request has been set since
//	* that instance was created.
//	*/
//	int[] rgdispid;
//	if (navigate) {
//		/*
//		* Stopping the loading of a page causes DocumentComplete events from previous
//		* requests to be received before the DocumentComplete for this page.  In such
//		* cases we must be sure to not set the html into the browser too soon, since
//		* doing so could result in its page being cleared out by a subsequent
//		* DocumentComplete.  The Browser's ReadyState can be used to determine whether
//		* these extra events will be received or not.
//		*/
//		rgdispid = auto.getIDsOfNames(new String[] { "ReadyState" }); //$NON-NLS-1$
//		Variant pVarResult = auto.getProperty(rgdispid[0]);
//		if (pVarResult == null) return false;
//		delaySetText = pVarResult.getInt() != READYSTATE_COMPLETE;
//		pVarResult.dispose();
//		rgdispid = auto.getIDsOfNames(new String[] { "Stop" }); //$NON-NLS-1$
//		auto.invoke(rgdispid[0]);
//	}
//	rgdispid = auto.getIDsOfNames(new String[] { "Navigate", "URL" }); //$NON-NLS-1$ //$NON-NLS-2$
//	navigate = true;
//	Variant[] rgvarg = new Variant[1];
//	rgvarg[0] = new Variant(ABOUT_BLANK);
//	int[] rgdispidNamedArgs = new int[1];
//	rgdispidNamedArgs[0] = rgdispid[1];
//	Variant pVarResult = auto.invoke(rgdispid[0], rgvarg, rgdispidNamedArgs);
//	rgvarg[0].dispose();
//	if (pVarResult == null) return false;
//	boolean result = pVarResult.getType() == OLE.VT_EMPTY;
//	pVarResult.dispose();
//	return result;
}

public boolean setUrl(String url, String postData, String[] headers) {
	if (url.indexOf(':') == -1) url = "http://" + url;
	int length = url.length ();
	char [] buffer = new char [length + 1];
	url.getChars (0, length, buffer, 0);
	int str = OS.gcnew_String (buffer);
	int uri = OS.gcnew_Uri(str, OS.UriKind_RelativeOrAbsolute);
	OS.GCHandle_Free(str);
	boolean result = OS.Frame_Navigate(frame, uri);
	OS.GCHandle_Free(uri);
	return result;
}

public void stop() {
	OS.Frame_StopLoading (frame);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18231.java