error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5783.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5783.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5783.java
text:
```scala
m@@onitor.beginTask("", 100);

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.filesystem.ftp;

import java.net.URL;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.ftp.*;

/**
 * 
 */
public class FTPUtil {
	private static final ThreadLocal openClients = new ThreadLocal();

	public static IClient createClient(URL url) throws FtpException {
		IClient client = Ftp.createClient(url);
		client.setDataTransferMode(IClient.PASSIVE_DATA_TRANSFER, true);
		client.setAuthentication(KeyRing.USER, KeyRing.PASS);
		client.setTimeout(5);
		return client;
	}

	public static IClient getClient() {
		return (IClient) openClients.get();
	}

	/**
	 * Use this method to ensure that the same connection is used for multiple commands
	 * issued from the same thread.
	 * @throws FtpException
	 */
	public static void run(URL url, IFtpRunnable runnable, IProgressMonitor monitor) throws FtpException {
		monitor = Policy.monitorFor(monitor);
		IClient openClient = (IClient) openClients.get();
		monitor.beginTask(null, 100);
		//if the wrong client is connected, disconnect before trying again
		if (openClient != null && !openClient.getUrl().equals(url)) {
			openClient.close(new SubProgressMonitor(monitor, 0));
			openClient = null;
		}
		if (openClient == null) {
			openClient = createClient(url);
			openClient.open(new SubProgressMonitor(monitor, 5));
			openClient.changeDirectory(url.getPath(), new SubProgressMonitor(monitor, 5));
			openClients.set(openClient);
		}
		runnable.run(new SubProgressMonitor(monitor, 90));
		//just leave the client open to reuse the connection
	}

	/**
	 * Converts a directory entry to a file info
	 * @param entry
	 * @return The file information for a directory entry
	 */
	public static IFileInfo entryToFileInfo(IDirectoryEntry entry) {
		FileInfo info = new FileInfo();
		info.setExists(true);
		info.setName(entry.getName());
		info.setLastModified(entry.getModTime().getTime());
		info.setLength(entry.getSize());
		info.setDirectory(entry.hasDirectorySemantics());
		return info;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5783.java