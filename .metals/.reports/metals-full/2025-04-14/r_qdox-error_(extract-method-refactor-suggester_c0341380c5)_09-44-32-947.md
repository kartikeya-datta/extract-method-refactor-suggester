error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8318.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8318.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8318.java
text:
```scala
S@@tring[] urls = {"http://www.google.com"};

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.browser;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.*;

public class Browser1 {
	public static boolean passed = false;	
	public static boolean locationChanging = false;
	public static boolean locationChanged = false;
	public static boolean progressCompleted = false;
	
	public static boolean test1(String url) {
		System.out.println("URL Loading - args: "+url+" Expected Event Sequence: Location.changing > Location.changed (top true)> Progress.completed");
		passed = false;
		locationChanging = locationChanged = progressCompleted = false;
				
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Browser browser = new Browser(shell, SWT.NONE);
		browser.addLocationListener(new LocationListener() {
			public void changing(LocationEvent event) {
				System.out.println("changing "+event.location);
				/* certain browsers do send multiple changing events. Safari does this. */
				/* verify the page has not been reported as being loaded */
				passed = !progressCompleted;
				locationChanging = true;
				if (!passed) shell.close();
			}
			public void changed(LocationEvent event) {
				System.out.println("changed "+event.location);
				/* ignore non top frame loading */
				if (!event.top) return;
				/* verify a changed follows at least one changing */
				/* verify the page has not been reported as being loaded */
				passed = locationChanging && !progressCompleted;
				locationChanged = true;
				if (!passed) shell.close();
			}
		});
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}
			public void completed(ProgressEvent event) {
				System.out.println("completed");
				passed = locationChanging && locationChanged && !progressCompleted;
				progressCompleted = true;
				if (!passed) shell.close();
				if (passed) {
					/* wait a little bit more before declaring it a success,
					 * in case bogus events follow this one.
					 */
					new Thread() {
						public void run() {
							System.out.println("timer start");
							try { sleep(2000); } catch (Exception e) {};
							if (!display.isDisposed())
								display.asyncExec(new Runnable(){
									public void run() {
										System.out.println("timer asyncexec shell.close");
										if (!shell.isDisposed()) shell.close();							
									}
								});
							System.out.println("timer over");
						};
					}.start();
				}
			}
		});
		
		shell.open();
		browser.setUrl(url);
		
		boolean timeout = runLoopTimer(display, shell, 600);
		if (timeout) passed = false;
		display.dispose();
		return passed;
	}
	
	public static boolean test2(String url) {
		System.out.println("URL Loading Filtering - args: "+url+" Expected Event Sequence: Location.changing cancel true > no Location.changed, no Progress.completed");
		locationChanging = locationChanged = progressCompleted = false;
		passed = false;
		final String[] locationCancelled = new String[1];
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);
		browser.addLocationListener(new LocationListener() {
			public void changing(LocationEvent event) {
				System.out.println("changing "+event.location);
				passed = !locationChanging && !locationChanged && !progressCompleted;
				locationChanging = true;
				if (!passed) {
					shell.close();
					return;
				}
				event.doit = false;
				new Thread() {
					public void run() {
						System.out.println("timer start");
						try { sleep(2000); } catch (Exception e) {};
						if (!display.isDisposed())
							display.asyncExec(new Runnable(){
								public void run() {
									System.out.println("timer asyncexec shell.close");
									if (!shell.isDisposed()) shell.close();							
								}
							});
						System.out.println("timer over");
					};
				}.start();
			}
			public void changed(LocationEvent event) {
				/*
				 * Feature on Internet Explorer. If there is no current location, IE still fires a DocumentComplete
				 * following the BeforeNavigate2 cancel event. This DocumentComplete event contains an empty URL
				 * since the URL in BeforeNavigate2 was correctly cancelled.
				 * The test considers it is OK to send a Location.changed and a Progress.completed events after
				 * a Location.changing cancel true - at the condition that the current location is empty,
				 * otherwise it is considered that the location was not successfully cancelled. 
				 */
				passed = event.location.length() == 0;
				System.out.println("changed "+event.location+" "+passed);
				locationChanged = true;
			}
		});
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}
			public void completed(ProgressEvent event) {
				/*
				 * Feature on Internet Explorer. If there is no current location, IE still fires a DocumentComplete
				 * following the BeforeNavigate2 cancel event. This DocumentComplete event contains an empty URL
				 * since the URL in BeforeNavigate2 was correctly cancelled.
				 * The test considers it is OK to send a Location.changed and a Progress.completed events after
				 * a Location.changing cancel true - at the condition that the current location is empty,
				 * otherwise it is considered that the location was not successfully cancelled. 
				 */
				String location = browser.getUrl();
				passed = location.length() == 0;
				System.out.println("completed "+passed);
				progressCompleted = true;
			}
		});
		shell.open();
		browser.setUrl(url);
		boolean timeout = runLoopTimer(display, shell, 600);
		if (timeout) passed = false;
		display.dispose();
		return passed;
	}
	
	static boolean runLoopTimer(final Display display, final Shell shell, final int seconds) {
		final boolean[] timeout = {false};
		new Thread() {
			public void run() {
				try {
					for (int i = 0; i < seconds; i++) {
						Thread.sleep(1000);
						if (display.isDisposed() || shell.isDisposed()) return;
					}
				}
				catch (Exception e) {} 
				timeout[0] = true;
				/* wake up the event loop */
				if (!display.isDisposed()) {
					display.asyncExec(new Runnable() {
						public void run() {
							if (!shell.isDisposed()) shell.redraw();						
						}
					});
				}
			}
		}.start();
		while (!timeout[0] && !shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
		return timeout[0];
	}
	
	public static boolean test() {
		int fail = 0;
		String[] urls = {"http://www.eclipse.org", "http://www.google.com", "http://www.ibm.com"};
		for (int i = 0; i < urls.length; i++) {
			boolean result = test1(urls[i]); 
			System.out.print(result ? "." : "E");
			if (!result) fail++; 
		}
		for (int i = 0; i < urls.length; i++) {
			boolean result = test2(urls[i]); 
			System.out.print(result ? "." : "E");
			if (!result) fail++; 
		}
		return fail == 0;
	}
	
	public static void main(String[] argv) {
		System.out.println("\r\nTests Finished. SUCCESS: "+test());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8318.java