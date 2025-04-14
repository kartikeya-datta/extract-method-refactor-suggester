error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10172.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10172.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10172.java
text:
```scala
i@@f (shell != null && !shell.isDisposed ()) {

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
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class Download {
	XPCOMObject supports;
	XPCOMObject download;
	XPCOMObject progressDialog;
	XPCOMObject webProgressListener;
	nsIHelperAppLauncher helperAppLauncher;
	int refCount = 0;

	Shell shell;
	Label status;
	Button cancel;
	
Download () {
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
	
	download = new XPCOMObject (new int[] {2, 0, 0, 7, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return Init (args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
		public int /*long*/ method4 (int /*long*/[] args) {return GetSource (args[0]);}
		public int /*long*/ method5 (int /*long*/[] args) {return GetTarget (args[0]);}
		public int /*long*/ method6 (int /*long*/[] args) {return GetPersist (args[0]);}
		public int /*long*/ method7 (int /*long*/[] args) {return GetPercentComplete (args[0]);}
		public int /*long*/ method8 (int /*long*/[] args) {return GetDisplayName (args[0]);}
		public int /*long*/ method9 (int /*long*/[] args) {return SetDisplayName (args[0]);}
		public int /*long*/ method10 (int /*long*/[] args) {return GetStartTime (args[0]);}
		public int /*long*/ method11 (int /*long*/[] args) {return GetMIMEInfo (args[0]);}
		public int /*long*/ method12 (int /*long*/[] args) {return GetListener (args[0]);}
		public int /*long*/ method13 (int /*long*/[] args) {return SetListener (args[0]);}
		public int /*long*/ method14 (int /*long*/[] args) {return GetObserver (args[0]);}
		public int /*long*/ method15 (int /*long*/[] args) {return SetObserver (args[0]);}
	};
	
	progressDialog = new XPCOMObject (new int[] {2, 0, 0, 7, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return Init (args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
		public int /*long*/ method4 (int /*long*/[] args) {return GetSource (args[0]);}
		public int /*long*/ method5 (int /*long*/[] args) {return GetTarget (args[0]);}
		public int /*long*/ method6 (int /*long*/[] args) {return GetPersist (args[0]);}
		public int /*long*/ method7 (int /*long*/[] args) {return GetPercentComplete (args[0]);}
		public int /*long*/ method8 (int /*long*/[] args) {return GetDisplayName (args[0]);}
		public int /*long*/ method9 (int /*long*/[] args) {return SetDisplayName (args[0]);}
		public int /*long*/ method10 (int /*long*/[] args) {return GetStartTime (args[0]);}
		public int /*long*/ method11 (int /*long*/[] args) {return GetMIMEInfo (args[0]);}
		public int /*long*/ method12 (int /*long*/[] args) {return GetListener (args[0]);}
		public int /*long*/ method13 (int /*long*/[] args) {return SetListener (args[0]);}
		public int /*long*/ method14 (int /*long*/[] args) {return GetObserver (args[0]);}
		public int /*long*/ method15 (int /*long*/[] args) {return SetObserver (args[0]);}
		public int /*long*/ method16 (int /*long*/[] args) {return Open (args[0]);}
		public int /*long*/ method17 (int /*long*/[] args) {return GetCancelDownloadOnClose (args[0]);}
		public int /*long*/ method18 (int /*long*/[] args) {return SetCancelDownloadOnClose ((int)/*64*/args[0]);}
		public int /*long*/ method19 (int /*long*/[] args) {return GetDialog (args[0]);}
		public int /*long*/ method20 (int /*long*/[] args) {return SetDialog (args[0]);}
	};
	
	webProgressListener = new XPCOMObject (new int[] {2, 0, 0, 4, 6, 3, 4, 3}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return OnStateChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3]);}
		public int /*long*/ method4 (int /*long*/[] args) {return OnProgressChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4], (int)/*64*/args[5]);}
		public int /*long*/ method5 (int /*long*/[] args) {return OnLocationChange (args[0], args[1], args[2]);}
		public int /*long*/ method6 (int /*long*/[] args) {return OnStatusChange (args[0], args[1], (int)/*64*/args[2], args[3]);}
		public int /*long*/ method7 (int /*long*/[] args) {return OnSecurityChange (args[0], args[1], (int)/*64*/args[2]);}
	};
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (download != null) {
		download.dispose ();
		download = null;	
	}
	if (progressDialog != null) {
		progressDialog.dispose ();
		progressDialog = null;
	}
	if (webProgressListener != null) {
		webProgressListener.dispose ();
		webProgressListener = null;
	}
}

int /*long*/ getAddress () {
	return progressDialog.getAddress ();
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);

	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIDownload.NS_IDOWNLOAD_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {download.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIProgressDialog.NS_IPROGRESSDIALOG_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {progressDialog.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {webProgressListener.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
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

/* nsIDownload */

/* Note. The argument startTime is defined as a PRInt64. This translates into two java ints. */
int Init (int /*long*/ aSource, int /*long*/ aTarget, int /*long*/ aDisplayName, int /*long*/ aMIMEInfo, int /*long*/ startTime1, int /*long*/ startTime2, int /*long*/ aPersist) {
	nsIURI source = new nsIURI (aSource);
	int /*long*/ aSpec = XPCOM.nsEmbedCString_new ();
	int rc = source.GetHost (aSpec);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	int length = XPCOM.nsEmbedCString_Length (aSpec);
	int /*long*/ buffer = XPCOM.nsEmbedCString_get (aSpec);
	byte[] dest = new byte[length];
	XPCOM.memmove (dest, buffer, length);
	XPCOM.nsEmbedCString_delete (aSpec);
	String url = new String (dest);

	/*
	* As of mozilla 1.7 the second argument of the nsIDownload interface's 
	* Init function changed from nsILocalFile to nsIURI.  Detect which of
	* these interfaces the second argument implements and act accordingly.  
	*/
	String filename = null;
	nsISupports supports = new nsISupports (aTarget);
	int /*long*/[] result = new int /*long*/[1];
	rc = supports.QueryInterface (nsIURI.NS_IURI_IID, result);
	if (rc == 0) {	/* >= 1.7 */
		nsIURI target = new nsIURI (result[0]);
		result[0] = 0;
		int /*long*/ aPath = XPCOM.nsEmbedCString_new ();
		rc = target.GetPath (aPath);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		length = XPCOM.nsEmbedCString_Length (aPath);
		buffer = XPCOM.nsEmbedCString_get (aPath);
		dest = new byte[length];
		XPCOM.memmove (dest, buffer, length);
		XPCOM.nsEmbedCString_delete (aPath);
		filename = new String (dest);
		int separator = filename.lastIndexOf (System.getProperty ("file.separator"));	//$NON-NLS-1$
		filename = filename.substring (separator + 1);
		target.Release ();
	} else {	/* < 1.7 */
		nsILocalFile target = new nsILocalFile (aTarget);
		int /*long*/ aNativeTarget = XPCOM.nsEmbedCString_new ();
		rc = target.GetNativeLeafName (aNativeTarget);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		length = XPCOM.nsEmbedCString_Length (aNativeTarget);
		buffer = XPCOM.nsEmbedCString_get (aNativeTarget);
		dest = new byte[length];
		XPCOM.memmove (dest, buffer, length);
		XPCOM.nsEmbedCString_delete (aNativeTarget);
		filename = new String (dest);
	}

	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			if (event.widget == cancel) {
				shell.close ();
			}
			if (helperAppLauncher != null) {
				helperAppLauncher.Cancel ();
				helperAppLauncher.Release ();
			}
			shell = null;
			helperAppLauncher = null;
		}
	};
	shell = new Shell (SWT.DIALOG_TRIM);
	String msg = Compatibility.getMessage ("SWT_Download_File", new Object[] {filename}); //$NON-NLS-1$
	shell.setText (msg);
	GridLayout gridLayout = new GridLayout ();
	gridLayout.marginHeight = 15;
	gridLayout.marginWidth = 15;
	gridLayout.verticalSpacing = 20;
	shell.setLayout(gridLayout);
	msg = Compatibility.getMessage ("SWT_Download_Location", new Object[] {filename, url}); //$NON-NLS-1$
	new Label (shell, SWT.SIMPLE).setText (msg);
	status = new Label (shell, SWT.SIMPLE);
	msg = Compatibility.getMessage ("SWT_Download_Started"); //$NON-NLS-1$
	status.setText (msg);
	GridData data = new GridData ();
	data.grabExcessHorizontalSpace = true;
	data.grabExcessVerticalSpace = true;
	status.setLayoutData (data);

	cancel = new Button (shell, SWT.PUSH);
	cancel.setText (SWT.getMessage ("SWT_Cancel")); //$NON-NLS-1$
	data = new GridData ();
	data.horizontalAlignment = GridData.CENTER;
	cancel.setLayoutData (data);
	cancel.addListener (SWT.Selection, listener);
	shell.addListener (SWT.Close, listener);
	shell.pack ();
	shell.open ();
	return XPCOM.NS_OK;
}

int GetSource (int /*long*/ aSource) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetTarget (int /*long*/ aTarget) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetPersist (int /*long*/ aPersist) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetPercentComplete (int /*long*/ aPercentComplete) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetDisplayName (int /*long*/ aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetDisplayName (int /*long*/ aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetStartTime (int /*long*/ aStartTime) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetMIMEInfo (int /*long*/ aMIMEInfo) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetListener (int /*long*/ aListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetListener (int /*long*/ aListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetObserver (int /*long*/ aObserver) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetObserver (int /*long*/ aObserver) {
	if (aObserver != 0) {
		nsISupports supports = new nsISupports (aObserver);
		int /*long*/[] result = new int /*long*/[1];
		int rc = supports.QueryInterface (nsIHelperAppLauncher.NS_IHELPERAPPLAUNCHER_IID, result);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);
		helperAppLauncher = new nsIHelperAppLauncher (result[0]);
	}
	return XPCOM.NS_OK;
}

/* nsIProgressDialog */
int Open (int /*long*/ aParent) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetCancelDownloadOnClose (int /*long*/ aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetCancelDownloadOnClose (int aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetDialog (int /*long*/ aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetDialog (int /*long*/ aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

/* nsIWebProgressListener */

int OnStateChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int aStateFlags, int aStatus) {
	if ((aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
		if (helperAppLauncher != null) helperAppLauncher.Release ();
		helperAppLauncher = null;
		if (shell != null && !shell.isDisposed ()) shell.dispose ();
		shell = null;
	}
	return XPCOM.NS_OK;
}

int OnProgressChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int aCurSelfProgress, int aMaxSelfProgress, int aCurTotalProgress, int aMaxTotalProgress) {
	int currentKBytes = aCurTotalProgress / 1024;
	int totalKBytes = aMaxTotalProgress / 1024;
	if (shell != null & !shell.isDisposed ()) {
		Object[] arguments = {new Integer (currentKBytes), new Integer (totalKBytes)};
		String statusMsg = Compatibility.getMessage ("SWT_Download_Status", arguments); //$NON-NLS-1$
		status.setText (statusMsg);
		shell.layout (true);
		shell.getDisplay ().update ();
	}
	return XPCOM.NS_OK;
}

int OnLocationChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int /*long*/ aLocation) {
	return XPCOM.NS_OK;
}

int OnStatusChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int aStatus, int /*long*/ aMessage) {
	return XPCOM.NS_OK;
}

int OnSecurityChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int state) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10172.java