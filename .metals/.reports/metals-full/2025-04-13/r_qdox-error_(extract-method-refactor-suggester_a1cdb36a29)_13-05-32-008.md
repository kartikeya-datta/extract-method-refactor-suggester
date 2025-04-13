error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1781.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1781.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1781.java
text:
```scala
public static final i@@nt CHROME_OPENAS_CHROME = -2147483648;

/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003, 2005 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIWebBrowserChrome extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;

	public static final String NS_IWEBBROWSERCHROME_IID_STR =
		"ba434c60-9d52-11d3-afb0-00a024ffc08c";

	public static final nsID NS_IWEBBROWSERCHROME_IID =
		new nsID(NS_IWEBBROWSERCHROME_IID_STR);

	public nsIWebBrowserChrome(int /*long*/ address) {
		super(address);
	}

	public static final int STATUS_SCRIPT = 1;

	public static final int STATUS_SCRIPT_DEFAULT = 2;

	public static final int STATUS_LINK = 3;

	public int SetStatus(int statusType, char[] status) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), statusType, status);
	}

	public int GetWebBrowser(int /*long*/[] aWebBrowser) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aWebBrowser);
	}

	public int SetWebBrowser(int /*long*/ aWebBrowser) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aWebBrowser);
	}

	public static final int CHROME_DEFAULT = 1;

	public static final int CHROME_WINDOW_BORDERS = 2;

	public static final int CHROME_WINDOW_CLOSE = 4;

	public static final int CHROME_WINDOW_RESIZE = 8;

	public static final int CHROME_MENUBAR = 16;

	public static final int CHROME_TOOLBAR = 32;

	public static final int CHROME_LOCATIONBAR = 64;

	public static final int CHROME_STATUSBAR = 128;

	public static final int CHROME_PERSONAL_TOOLBAR = 256;

	public static final int CHROME_SCROLLBARS = 512;

	public static final int CHROME_TITLEBAR = 1024;

	public static final int CHROME_EXTRA = 2048;

	public static final int CHROME_WITH_SIZE = 4096;

	public static final int CHROME_WITH_POSITION = 8192;

	public static final int CHROME_WINDOW_MIN = 16384;

	public static final int CHROME_WINDOW_POPUP = 32768;

	public static final int CHROME_WINDOW_RAISED = 33554432;

	public static final int CHROME_WINDOW_LOWERED = 67108864;

	public static final int CHROME_CENTER_SCREEN = 134217728;

	public static final int CHROME_DEPENDENT = 268435456;

	public static final int CHROME_MODAL = 536870912;

	public static final int CHROME_OPENAS_DIALOG = 1073741824;

//	public static final int CHROME_OPENAS_CHROME = 2147483648;

	public static final int CHROME_ALL = 4094;

	public int GetChromeFlags(int[] aChromeFlags) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aChromeFlags);
	}

	public int SetChromeFlags(int aChromeFlags) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aChromeFlags);
	}

	public int DestroyBrowserWindow() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress());
	}

	public int SizeBrowserTo(int aCX, int aCY) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aCX, aCY);
	}

	public int ShowAsModal() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress());
	}

	public int IsWindowModal(boolean[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), _retval);
	}

	public int ExitModalEventLoop(int aStatus) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aStatus);
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1781.java