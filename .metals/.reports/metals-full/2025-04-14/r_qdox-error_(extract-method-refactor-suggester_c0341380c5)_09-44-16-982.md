error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10313.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10313.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10313.java
text:
```scala
i@@f (guid.Equals (nsIHelperAppLauncherDialog.NS_IHELPERAPPLAUNCHERDIALOG_IID)) {

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
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class HelperAppLauncherDialog {
	XPCOMObject supports;
	XPCOMObject helperAppLauncherDialog;
	int refCount = 0;

public HelperAppLauncherDialog () {
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
	
	helperAppLauncherDialog = new XPCOMObject (new int[] {2, 0, 0, 3, 5}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return Show (args[0], args[1], args[2]);}
		public int /*long*/ method4 (int /*long*/[] args) {return PromptForSaveToFile (args[0], args[1], args[2], args[3], args[4]);}
	};		
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (helperAppLauncherDialog != null) {
		helperAppLauncherDialog.dispose ();
		helperAppLauncherDialog = null;	
	}
}

int /*long*/ getAddress () {
	return helperAppLauncherDialog.getAddress ();
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
	if (guid.Equals (XPCOM.NS_IHELPERAPPLAUNCHERDIALOG_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {helperAppLauncherDialog.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	
	XPCOM.memmove (ppvObject, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}
        	
int Release () {
	refCount--;
	/*
	* Note.  This instance lives as long as the download it is binded to.
	* Its reference count is expected to go down to 0 when the download
	* has completed or when it has been cancelled. E.g. when the user
	* cancels the File Dialog, cancels or closes the Download Dialog
	* and when the Download Dialog goes away after the download is completed.
	*/
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

/* nsIHelperAppLauncherDialog */

public int /*long*/ Show (int /*long*/ aLauncher, int /*long*/ aContext, int /*long*/ aReason) {
	/*
	 * The interface for nsIHelperAppLauncher changed as of mozilla 1.8.  Query the received
	 * nsIHelperAppLauncher for the new interface, and if it is not found then fall back to
	 * the old interface. 
	 */
	nsISupports supports = new nsISupports (aLauncher);
	int /*long*/[] result = new int /*long*/[1];
	int rc = supports.QueryInterface (nsIHelperAppLauncher_1_8.NS_IHELPERAPPLAUNCHER_IID, result);
	if (rc == 0) {	/* >= 1.8 */
		nsIHelperAppLauncher_1_8 helperAppLauncher = new nsIHelperAppLauncher_1_8 (aLauncher);
		rc = helperAppLauncher.SaveToDisk (0, false);
		helperAppLauncher.Release ();
		return rc;
	}
	nsIHelperAppLauncher helperAppLauncher = new nsIHelperAppLauncher (aLauncher);	/* < 1.8 */
	return helperAppLauncher.SaveToDisk (0, false);
}

public int /*long*/ PromptForSaveToFile (int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ arg4) {
	int /*long*/ aDefaultFile, aSuggestedFileExtension, _retval;
	boolean hasLauncher = false;

	/*
	* The interface for nsIHelperAppLauncherDialog changed as of mozilla 1.5 when an
	* extra argument was added to the PromptForSaveToFile method (this resulted in all
	* subsequent arguments shifting right).  The workaround is to provide an XPCOMObject 
	* that fits the newer API, and to use the first argument's type to infer whether
	* the old or new nsIHelperAppLauncherDialog interface is being used (and by extension
	* the ordering of the arguments).  In mozilla >= 1.5 the first argument is an
	* nsIHelperAppLauncher. 
	*/
	/*
	 * The interface for nsIHelperAppLauncher changed as of mozilla 1.8, so the first
	 * argument must be queried for both the old and new nsIHelperAppLauncher interfaces. 
	 */
	nsISupports support = new nsISupports (arg0);
	int /*long*/[] result = new int /*long*/[1];
	int rc = support.QueryInterface (nsIHelperAppLauncher_1_8.NS_IHELPERAPPLAUNCHER_IID, result);
	boolean usingMozilla18 = rc == 0;
	if (usingMozilla18) {
		hasLauncher = true;
		nsISupports supports = new nsISupports (result[0]);
		supports.Release ();
	} else {
		result[0] = 0;
		rc = support.QueryInterface (nsIHelperAppLauncher.NS_IHELPERAPPLAUNCHER_IID, result);
		if (rc == 0) {
			hasLauncher = true;
			nsISupports supports = new nsISupports (result[0]);
			supports.Release ();
		}
	}
	result[0] = 0;

	if (hasLauncher) {	/* >= 1.5 */
		aDefaultFile = arg2;
		aSuggestedFileExtension = arg3;
		_retval = arg4;
	} else {			/* 1.4 */
		aDefaultFile = arg1;
		aSuggestedFileExtension = arg2;
		_retval = arg3;
	}

	int length = XPCOM.strlen_PRUnichar (aDefaultFile);
	char[] dest = new char[length];
	XPCOM.memmove (dest, aDefaultFile, length * 2);
	String defaultFile = new String (dest);

	length = XPCOM.strlen_PRUnichar (aSuggestedFileExtension);
	dest = new char[length];
	XPCOM.memmove (dest, aSuggestedFileExtension, length * 2);
	String suggestedFileExtension = new String (dest);

	Shell shell = new Shell ();
	FileDialog fileDialog = new FileDialog (shell, SWT.SAVE);
	fileDialog.setFileName (defaultFile);
	fileDialog.setFilterExtensions (new String[] {suggestedFileExtension});
	String name = fileDialog.open ();
	shell.close ();
	if (name == null) {
		if (hasLauncher) {
			if (usingMozilla18) {
				nsIHelperAppLauncher_1_8 launcher = new nsIHelperAppLauncher_1_8 (arg0);
				rc = launcher.Cancel (XPCOM.NS_BINDING_ABORTED);
			} else {
				nsIHelperAppLauncher launcher = new nsIHelperAppLauncher (arg0);
				rc = launcher.Cancel ();
			}
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			return XPCOM.NS_OK;
		}
		return XPCOM.NS_ERROR_FAILURE;
	}
	nsEmbedString path = new nsEmbedString (name);
	rc = XPCOM.NS_NewLocalFile (path.getAddress (), true, result);
	path.dispose ();
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NULL_POINTER);
	/* Our own nsIDownload has been registered during the Browser initialization. It will be invoked by Mozilla. */
	XPCOM.memmove (_retval, result, C.PTR_SIZEOF);	
	return XPCOM.NS_OK;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10313.java