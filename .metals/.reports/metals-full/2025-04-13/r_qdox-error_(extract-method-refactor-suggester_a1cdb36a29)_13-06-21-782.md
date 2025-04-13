error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16523.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16523.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 594
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16523.java
text:
```scala
public class OutgoingTest extends ContainerAbstractTestCase {

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

p@@ackage org.eclipse.ecf.tests.filetransfer;

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.filetransfer.IFileTransferListener;
import org.eclipse.ecf.filetransfer.IIncomingFileTransferRequestListener;
import org.eclipse.ecf.filetransfer.IOutgoingFileTransferContainerAdapter;
import org.eclipse.ecf.filetransfer.events.IFileTransferEvent;
import org.eclipse.ecf.filetransfer.events.IFileTransferRequestEvent;
import org.eclipse.ecf.tests.ContainerAbstractTestCase;

/**
 * 
 */
public class OutgoingFileTransferTest extends ContainerAbstractTestCase {

	private static final String TESTSRCPATH = "test.src";
	private static final String TESTSRCFILE = TESTSRCPATH + "/test.txt";

	private static final String TESTTARGETPATH = "test.target";

	static final String XMPP_CONTAINER = "ecf.xmpp.smack";

	protected IOutgoingFileTransferContainerAdapter adapter0, adapter1 = null;

	protected String getClientContainerName() {
		return XMPP_CONTAINER;
	}

	protected IOutgoingFileTransferContainerAdapter getOutgoingFileTransfer(
			int client) {
		IContainer c = getClient(client);
		if (c != null)
			return (IOutgoingFileTransferContainerAdapter) c
					.getAdapter(IOutgoingFileTransferContainerAdapter.class);
		else
			return null;
	}

	protected IFileTransferListener getFileTransferListener(final String prefix) {
		return new IFileTransferListener() {
			public void handleTransferEvent(IFileTransferEvent event) {
				System.out.println(prefix+".handleTransferEvent("+event+")");
			}};
	}
	
	protected IIncomingFileTransferRequestListener requestListener = new IIncomingFileTransferRequestListener() {

		public void handleFileTransferRequest(IFileTransferRequestEvent event) {
			System.out.println("receiver.handleFileTransferRequest(" + event + ")");
			try {
				File dir = new File(TESTTARGETPATH);
				dir.mkdirs();
				File f = new File(dir, event
						.getFileTransferInfo().getFile().getName());
				FileOutputStream fos = new FileOutputStream(f);
				event.accept(fos,receiverTransferListener);
				//event.accept(f);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				fail("exception calling accept for receive file transfer");
			}
		}

	};

	protected IFileTransferListener senderTransferListener = getFileTransferListener("sender");
	protected IFileTransferListener receiverTransferListener = getFileTransferListener("receiver");
	
	protected ID getServerConnectID(int client) {
		IContainer container = getClient(client);
		Namespace connectNamespace = container.getConnectNamespace();
		String username = getUsername(client);
		try {
			return IDFactory.getDefault().createID(connectNamespace, username);
		} catch (IDCreateException e) {
			e.printStackTrace(System.err);
			fail("Could not create server connect ID");
			return null;
		}
	}

	public void testTwoClientsToSendAndReceive() throws Exception {
		// Setup two clients.  Client 0 is the receiver, client 1 is the sender
		setClientCount(2);
		clients = createClients();
		adapter0 = getOutgoingFileTransfer(0);
		adapter0.addListener(requestListener);
		adapter1 = getOutgoingFileTransfer(1);
		for (int i = 0; i < 2; i++) {
			connectClient(i);
		}

		adapter1.sendOutgoingRequest(getServerConnectID(0), new File(
				TESTSRCFILE), senderTransferListener, null);
		
		sleep(20000);
		
		disconnectClients();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16523.java