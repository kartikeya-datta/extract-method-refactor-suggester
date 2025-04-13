error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13732.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13732.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13732.java
text:
```scala
p@@erformance.tagAsSummary(meter, id, Dimension.ELAPSED_PROCESS);

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.*;
import org.eclipse.test.performance.*;


public class SwtPerformanceTestCase extends TestCase {
	// used to specify verbose mode, if true unimplemented warning messages will 
	// be written to System.out
	public static boolean verbose = false;

	public final static boolean isGTK = SWT.getPlatform().equals("gtk");
	public final static boolean isWindows = SWT.getPlatform().startsWith("win32");
	
	// allow specific image formats to be tested
	public static String[] imageFormats = new String[] {"bmp", "jpg", "gif", "png"};
	public static String[] imageFilenames = new String[] {"folder", "folderOpen", "target"};
	public static String[] transparentImageFilenames = new String[] {"transparent.png"};

	
public SwtPerformanceTestCase(String name) {
	super(name);
}

protected PerformanceMeter createMeter(String id) {
	Performance performance = Performance.getDefault();
	String scenarioId = "org.eclipse.swt.test." + id;
	PerformanceMeter meter = performance.createPerformanceMeter(scenarioId);
	performance.tagAsSummary(meter, id, Dimension.CPU_TIME);
	return meter;
}

protected void disposeMeter(PerformanceMeter meter) {
	try {
		meter.commit();
		Performance.getDefault().assertPerformance(meter);
	} finally {
		meter.dispose();
	}
}

protected String getPath(String fileName) {
	String urlPath;
	
	String pluginPath = System.getProperty("PLUGIN_PATH");
	if (verbose) {
		System.out.println("PLUGIN_PATH <"+pluginPath+">");
	}
	if (pluginPath == null) {
		URL url = getClass().getClassLoader().getResource(fileName);
		if (url == null) {
			fail("URL == null for file " + fileName);
		}
		urlPath = url.getFile();
	} else {
		urlPath = pluginPath + "/data/" + fileName;
	}
	
	if (File.separatorChar != '/') urlPath = urlPath.replace('/', File.separatorChar);	
	if (isWindows && urlPath.indexOf(File.separatorChar) == 0) urlPath = urlPath.substring(1);
	urlPath = urlPath.replaceAll("%20", " ");	
	
	if (verbose) {
		System.out.println("Resolved file name for " + fileName + " = " + urlPath);
	}
	return urlPath;
}

protected boolean isJ2ME() {
	try {
		Compatibility.newFileInputStream("");
	} catch (FileNotFoundException e) {
		return false;
	} catch (IOException e) {
	}
	return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13732.java