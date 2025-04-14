error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10015.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10015.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10015.java
text:
```scala
final i@@nt COUNT = 25000000;

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
package org.eclipse.swt.tests.junit.performance;


import java.io.*;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.test.performance.PerformanceMeter;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.ImageLoader
 *
 * @see org.eclipse.swt.graphics.ImageLoader
 */
public class Test_org_eclipse_swt_graphics_ImageLoader extends SwtPerformanceTestCase {

public Test_org_eclipse_swt_graphics_ImageLoader(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	final int COUNT = 5000000;
	
	PerformanceMeter meter = createMeter("ImageLoader constr.");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		new ImageLoader();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener() {
	final int COUNT = 5000000;	// 6000000 causes OOM error
	
	ImageLoader loader = new ImageLoader();
	ImageLoaderListener loaderListener = new ImageLoaderListener() {
		public void imageDataLoaded(ImageLoaderEvent e) {
		};
	};
	
	PerformanceMeter meter = createMeter("ImageLoader addImageLoaderListener");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		loader.addImageLoaderListener(loaderListener);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_hasListeners() {
	final int COUNT = 300000000;
	
	ImageLoader loader = new ImageLoader();
	
	PerformanceMeter meter = createMeter("ImageLoader hasListeners - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		loader.hasListeners();	// no listeners
	}
	meter.stop();
	
	disposeMeter(meter);
	
	loader.addImageLoaderListener(new ImageLoaderListener() {
		public void imageDataLoaded(ImageLoaderEvent e) {
		};
	});
	
	meter = createMeter("ImageLoader hasListeners - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		loader.hasListeners();	// has listener
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_loadLjava_io_InputStream() {
	// TODO
}

public void test_loadLjava_lang_String() {
	final int COUNT = 4500;
	
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
	if (isJ2ME()) return;

	ImageLoader loader = new ImageLoader();
	String fileName = getPath(imageFilenames[0] + "." + imageFormats[0]);
	
	PerformanceMeter meter = createMeter("ImageLoader load(String)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		loader.load(fileName);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent() {
	final int COUNT = 16000000;
	
	ImageLoader loader = new ImageLoader();
	ImageLoaderEvent event = new ImageLoaderEvent(loader, null, 0, true);
	loader.addImageLoaderListener(
		new ImageLoaderListener() {
			public void imageDataLoaded(ImageLoaderEvent e) {}
	});
	
	PerformanceMeter meter = createMeter("ImageLoader notifyListeners");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		loader.notifyListeners(event);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener() {
	final int COUNT = 50000;
	
	ImageLoader loader = new ImageLoader();
	ImageLoaderListener[] listeners = new ImageLoaderListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new ImageLoaderListener() {
			public void imageDataLoaded(ImageLoaderEvent e) {}
		};
		loader.addImageLoaderListener(listeners[i]);
	}
	
	PerformanceMeter meter = createMeter("ImageLoader removeImageLoaderListener");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		loader.removeImageLoaderListener(listeners[i]);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_saveLjava_io_OutputStreamI() {
	final int COUNT = 30000;
	
	ImageLoader loader = new ImageLoader();
	boolean jpgSupported = false;
	for (int i=0; i<imageFormats.length; i++) {
		if (imageFormats[i].equals("jpg")) {
			jpgSupported = true;
			break;
		}
	}
	if (!jpgSupported) return;
	
	String filename = imageFilenames[0];
	// must use jpg since save is not implemented yet in png format		
	String filetype = "jpg";
	FileInputStream inStream = null;
	try {
		inStream = new FileInputStream(getPath(filename + "." + filetype));
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}	
	loader.load(inStream);
	try {
		inStream.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	try {
		for (int i = 0; i < imageFormats.length; i++) {
			if (imageFormats[i].equals(filetype)) {

				PerformanceMeter meter = createMeter("ImageLoader save(OutputStream,I) - " + i);
				meter.start();
				for (int j = 0; j < COUNT; j++) {
					loader.save(outStream, i);
				}
				meter.stop();
				
				disposeMeter(meter);
				
				break;
			}
		}
	} finally {
		try {
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public void test_saveLjava_lang_StringI() {
	// TODO
//	final int COUNT = 10000;
//	
//	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
//	if (isJ2ME()) return;
//
//	ImageLoader loader = new ImageLoader();
//	String name = getPath(imageFilenames[0] + "." + imageFormats[0]);
//	InputStream inStream = null;
//	try {
//		inStream = new FileInputStream(name);
//	} catch (FileNotFoundException e) {
//		e.printStackTrace();
//	}	
//	loader.load(inStream);
//	try {
//		inStream.close();
//	} catch (IOException e2) {
//		e2.printStackTrace();
//	}
//
//	OutputStream outStream = new ByteArrayOutputStream();
//	
//	try {
//		PerformanceMeter meter = createMeter("gif");
//		meter.start();
//		for (int i = 0; i < COUNT; i++) {
//			loader.save(outStream, SWT.IMAGE_GIF);
//		}
//		meter.stop();
//		
//		disposeMeter(meter);
//	
//		meter = createMeter("ico");
//		meter.start();
//		for (int i = 0; i < COUNT; i++) {
//			loader.save(outStream, SWT.IMAGE_ICO);
//		}
//		meter.stop();
//		
//		disposeMeter(meter);
//		
//		meter = createMeter("jpg");
//		meter.start();
//		for (int i = 0; i < COUNT; i++) {
//			loader.save(outStream, SWT.IMAGE_JPEG);
//		}
//		meter.stop();
//		
//		disposeMeter(meter);
//	} finally {
//		try {
//			outStream.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_ImageLoader((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener");
	methodNames.addElement("test_hasListeners");
	methodNames.addElement("test_loadLjava_io_InputStream");
	methodNames.addElement("test_loadLjava_lang_String");
	methodNames.addElement("test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent");
	methodNames.addElement("test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener");
	methodNames.addElement("test_saveLjava_io_OutputStreamI");
	methodNames.addElement("test_saveLjava_lang_StringI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener")) test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener();
	else if (getName().equals("test_hasListeners")) test_hasListeners();
	else if (getName().equals("test_loadLjava_io_InputStream")) test_loadLjava_io_InputStream();
	else if (getName().equals("test_loadLjava_lang_String")) test_loadLjava_lang_String();
	else if (getName().equals("test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent")) test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent();
	else if (getName().equals("test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener")) test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener();
	else if (getName().equals("test_saveLjava_io_OutputStreamI")) test_saveLjava_io_OutputStreamI();
	else if (getName().equals("test_saveLjava_lang_StringI")) test_saveLjava_lang_StringI();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10015.java