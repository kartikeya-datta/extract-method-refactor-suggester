error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15563.java
text:
```scala
r@@eturn "libxpcom.dylib"; //$NON-NLS-1$

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

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.LONG;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	static Hashtable handles = new Hashtable ();
	
MozillaDelegate (Browser browser) {
	super ();
	this.browser = browser;
}

static Browser findBrowser (int handle) {
	LONG value = (LONG)handles.get (new LONG (handle));
	if (value != null) {
		Display display = Display.getCurrent ();
		return (Browser)display.findWidget (value.value);
	}
	return null;
}

static char[] mbcsToWcs (String codePage, byte [] buffer) {
	int encoding = OS.CFStringGetSystemEncoding ();
	int cfstring = OS.CFStringCreateWithBytes (OS.kCFAllocatorDefault, buffer, buffer.length, encoding, false);
	char[] chars = null;
	if (cfstring != 0) {
		int length = OS.CFStringGetLength (cfstring);
		chars = new char [length];
		if (length != 0) {
			CFRange range = new CFRange ();
			range.length = length;
			OS.CFStringGetCharacters (cfstring, range, chars);
		}
		OS.CFRelease (cfstring);
	}
	return chars;
}

public static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
	char[] chars = new char [string.length()];
	string.getChars (0, chars.length, chars, 0);
	int cfstring = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
	byte[] buffer = null;
	if (cfstring != 0) {
		CFRange range = new CFRange ();
		range.length = chars.length;
		int encoding = OS.CFStringGetSystemEncoding ();
		int[] size = new int[1];
		int numChars = OS.CFStringGetBytes (cfstring, range, encoding, (byte)'?', true, null, 0, size);
		buffer = new byte [size[0] + (terminate ? 1 : 0)];
		if (numChars != 0) {
			numChars = OS.CFStringGetBytes (cfstring, range, encoding, (byte)'?', true, buffer, size[0], size);
		}
		OS.CFRelease (cfstring);
	}
	return buffer;
}

int getHandle () {
    int embedHandle = Cocoa.objc_msgSend (Cocoa.C_NSImageView, Cocoa.S_alloc);
	if (embedHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	NSRect r = new NSRect();
	embedHandle = Cocoa.objc_msgSend (embedHandle, Cocoa.S_initWithFrame, r);
	int rc;
	int[] outControl = new int[1];
	if (OS.VERSION >= 0x1050) {
		rc = Cocoa.HICocoaViewCreate(embedHandle, 0, outControl);
	} else {
		try {
			System.loadLibrary("frameembedding");
		} catch (UnsatisfiedLinkError e) {}
		rc = Cocoa.HIJavaViewCreateWithCocoaView(outControl, embedHandle);
	}
	if (rc != OS.noErr || outControl[0] == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int subHIView = outControl[0];
	HILayoutInfo newLayoutInfo = new HILayoutInfo();
	newLayoutInfo.version = 0;
	OS.HIViewGetLayoutInfo(subHIView, newLayoutInfo);
	HISideBinding biding = newLayoutInfo.binding.top;
	biding.toView = 0;
	biding.kind = OS.kHILayoutBindMin;
	biding.offset = 0;
	biding = newLayoutInfo.binding.left;
	biding.toView = 0;
	biding.kind = OS.kHILayoutBindMin;
	biding.offset = 0;
	biding = newLayoutInfo.binding.bottom;
	biding.toView = 0;
	biding.kind = OS.kHILayoutBindMax;
	biding.offset = 0;
	biding = newLayoutInfo.binding.right;
	biding.toView = 0;
	biding.kind = OS.kHILayoutBindMax;
	biding.offset = 0;
	OS.HIViewSetLayoutInfo(subHIView, newLayoutInfo);
	OS.HIViewChangeFeatures(subHIView, OS.kHIViewFeatureIsOpaque, 0);
	OS.HIViewSetVisible(subHIView, true);
	int parentHandle = browser.handle;
	OS.HIViewAddSubview(browser.handle, subHIView);
	CGRect rect = new CGRect();
	OS.HIViewGetFrame(parentHandle, rect);
	rect.x = rect.y = 0;
	OS.HIViewSetFrame(subHIView, rect);
	handles.put (new LONG (embedHandle), new LONG (browser.handle));
	return embedHandle;
}

String getLibraryName () {
	return "libxpcom.dylib";
}

void onDispose (int embedHandle) {
	handles.remove (new LONG (embedHandle));
}

void setSize (int embedHandle, int width, int height) {
	// TODO
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15563.java