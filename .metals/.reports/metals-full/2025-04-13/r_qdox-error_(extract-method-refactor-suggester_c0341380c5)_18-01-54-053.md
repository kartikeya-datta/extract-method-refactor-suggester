error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8701.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8701.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[87,2]

error in qdox parser
file content:
```java
offset: 2945
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8701.java
text:
```scala
import org.eclipse.ecf.internal.example.collab.ClientPlugin;

/****************************************************************************
* Copyright (c) 2004 Composent, Inc. and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Composent, Inc. - initial API and implementation
*****************************************************************************/

package org.eclipse.ecf.example.collab.share.url;

import java.net.URL;

import org.eclipse.ecf.example.collab.ClientPlugin;
import org.eclipse.help.browser.IBrowser;
import org.eclipse.help.internal.browser.BrowserManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class GetExec {

	protected static void displayURL(String url, boolean external) {
		IBrowser browser = null;
		Shell [] shells = Display.getCurrent().getShells();
		try {
			browser = BrowserManager.getInstance().createBrowser(
					external);
		} catch (SWTError swterror) {
			try {
				if (shells != null && shells.length > 0) {
					MessageDialog.openError(shells[0], "Error in Browser Creation", "Cannot launch browser.  Something is wrong with config for using external browser");
				}
			} catch (Exception e1) {}
			ClientPlugin.log("Cannot create browser for URL: " + url, swterror);
			return;
		}
		try {
			browser.displayURL(url);
		} catch (Exception e) {
			try {
				if (shells != null && shells.length > 0) {
					MessageDialog.openError(shells[0], "Error in URL", "Cannot display URL");
				}
			} catch (Exception e1) {}
			ClientPlugin.log("Cannot display URL: " + url, e);
		} catch (SWTError swterror) {
			
		}
	}
	public static void showURL(final String url,
		final boolean considerInternal) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				displayURL(url, considerInternal);
			}
		});
	}
	public static void openURL(final URL anURL,final boolean internal) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
				try {
					if (internal)
						support.createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR,
								anURL.toExternalForm(), null, null).openURL(anURL);
					else {
						displayURL(anURL.toExternalForm(),false);
					}
				}
				catch (PartInitException e) {
					MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
							"Error in URL", e.getLocalizedMessage());
				}
			}
		});
	}
}
 N@@o newline at end of file
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8701.java