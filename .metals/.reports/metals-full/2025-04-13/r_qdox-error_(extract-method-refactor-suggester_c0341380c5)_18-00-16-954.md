error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3547.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3547.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3547.java
text:
```scala
S@@ystem.out.println("Could not instantiate Browser: " + e.getMessage());

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.mozilla.interfaces.*;

/*
 * Browser example snippet: Toggle a Mozilla Browser between Design mode and View mode.
 * 
 * IMPORTANT: For this snippet to work properly all of the requirements for running a
 * Mozilla-based Browser instance listed in http://www.eclipse.org/swt/faq.php#howusemozilla
 * must be satisfied.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.3
 */
public class Snippet267 {
	static Browser browser;
	public static void main (String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, true));
		shell.setText("Use Mozilla's Design Mode");
		try {
			browser = new Browser(shell, SWT.MOZILLA);
		} catch (SWTError e) {
			System.out.println("Count not instantiate Browser: " + e.getMessage());
			return;
		}
		browser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));

		final Button offButton = new Button(shell, SWT.RADIO);
		offButton.setText("Design Mode Off");
		offButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (!offButton.getSelection()) return;
				setDesignMode("off");
			}
		});
		final Button onButton = new Button(shell, SWT.RADIO);
		onButton.setText("Design Mode On");
		onButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (!onButton.getSelection()) return;
				boolean success = setDesignMode("on");
				if (!success) {
					onButton.setSelection(false);
					offButton.setSelection(true);
				}
			}
		});
		offButton.setSelection(true);

		browser.setUrl("http://www.google.com");
		shell.setSize(400,400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
	public static boolean setDesignMode(String value) {
		nsIWebBrowser webBrowser = (nsIWebBrowser)browser.getWebBrowser();
		if (webBrowser == null) {
			System.out.println("Could not get the nsIWebBrowser from the Browser widget");
			return false;
		}
		nsIDOMWindow window = webBrowser.getContentDOMWindow();
		nsIDOMDocument document = window.getDocument();
		nsIDOMNSHTMLDocument nsDocument = (nsIDOMNSHTMLDocument)document.queryInterface(nsIDOMNSHTMLDocument.NS_IDOMNSHTMLDOCUMENT_IID);
		nsDocument.setDesignMode(value);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3547.java