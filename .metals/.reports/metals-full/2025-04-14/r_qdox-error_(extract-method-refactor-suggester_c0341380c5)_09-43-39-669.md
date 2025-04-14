error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6595.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6595.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6595.java
text:
```scala
S@@hell shell = new Shell(display);

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
package org.eclipse.swt.snippets;

/*
 * Browser example snippet: Examine request and response headers in a Mozilla Browser
 * 
 * IMPORTANT: For this snippet to work properly all of the requirements
 * for using JavaXPCOM in a stand-alone application must be satisfied
 * (see http://www.eclipse.org/swt/faq.php#howusejavaxpcom).
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.3
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.mozilla.interfaces.*;
import org.mozilla.xpcom.Mozilla;

public class Snippet321 {

public static void main(String [] args) {
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setBounds(10,10,200,200);
	shell.setLayout(new FillLayout());
	Browser browser;
	try {
		browser = new Browser(shell, SWT.MOZILLA);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}

	nsIObserver observer = new nsIObserver() {
		public nsISupports queryInterface(String aIID) {
			if (aIID.equals(nsIObserver.NS_IOBSERVER_IID) || aIID.equals(nsIObserver.NS_ISUPPORTS_IID)) {
				return this;
			}
			return null;
		}
		public void observe(nsISupports subject, String topic, String data) {
			nsIHttpChannel channel = (nsIHttpChannel)subject.queryInterface(nsIHttpChannel.NS_IHTTPCHANNEL_IID);
			nsIRequest request = (nsIRequest)subject.queryInterface(nsIRequest.NS_IREQUEST_IID);
			if (topic.equals("http-on-modify-request")) {
				System.out.println("---------------------\nSome Request Header Values for " + request.getName() + ':');
				printRequestHeader(channel, "accept");
				printRequestHeader(channel, "accept-language");
				printRequestHeader(channel, "host");
				printRequestHeader(channel, "user-agent");
			} else {
				/* http-on-examine-response */
				System.out.println("---------------------\n\tSome Response Header Values for " + request.getName() + ':');
				printResponseHeader(channel, "content-length");
				printResponseHeader(channel, "content-type");
				printResponseHeader(channel, "expires");
				printResponseHeader(channel, "server");
			}
		}
		void printRequestHeader(nsIHttpChannel channel, String header) {
			try {
				System.out.println(header + '=' + channel.getRequestHeader(header));
			} catch (Exception e) {
				// the header did not exist, just continue
			}
		}
		void printResponseHeader(nsIHttpChannel channel, String header) {
			try {
				System.out.println('\t' + header + '=' + channel.getResponseHeader(header));
			} catch (Exception e) {
				// the header did not exist, just continue
			}
		}
	};
	nsIObserverService observerService = (nsIObserverService)Mozilla.getInstance().getServiceManager().getServiceByContractID(
		"@mozilla.org/observer-service;1", nsIObserverService.NS_IOBSERVERSERVICE_IID);
	observerService.addObserver(observer, "http-on-modify-request", false);
	observerService.addObserver(observer, "http-on-examine-response", false);

	browser.setUrl("http://www.eclipse.org");
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6595.java