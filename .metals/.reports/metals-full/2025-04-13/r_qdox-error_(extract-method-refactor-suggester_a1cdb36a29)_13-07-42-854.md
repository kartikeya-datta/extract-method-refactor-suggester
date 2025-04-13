error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4017.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4017.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4017.java
text:
```scala
c@@lient.sendChat(targetIMUser, "Hi, I'm an IM robot");

/*******************************************************************************
 * Copyright (c) 2004, 2007 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.example.clients.applications;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.example.clients.IMessageReceiver;
import org.eclipse.ecf.example.clients.XMPPChatClient;
import org.eclipse.ecf.presence.im.IChatMessage;

public class ChatSORobotApplication implements IPlatformRunnable,
		IMessageReceiver {

	private boolean running = false;
	private String userName;
	private XMPPChatClient client;
	private String targetIMUser;
	private TrivialSharedObject sharedObject = null;

	public synchronized Object run(Object args) throws Exception {
		if (args instanceof Object[]) {
			Object[] arguments = (Object[]) args;
			int l = arguments.length;
			if (arguments[l-1] instanceof String
					&& arguments[l-2] instanceof String
					&& arguments[l-3] instanceof String
					&& arguments[l-4] instanceof String) {
				userName = (String) arguments[l-4];
				String hostName = (String) arguments[l-3];
				String password = (String) arguments[l-2];
				String targetName = (String) arguments[l-1];
				runRobot(hostName, password, targetName);
				return new Integer(0);
			}
		}

		System.out
				.println("Usage: pass in four arguments (username, hostname, password, targetIMUser)");
		return new Integer(-1);
	}

	private void runRobot(String hostName, String password, String targetIMUser)
			throws ECFException, Exception, InterruptedException {
		// Create client and connect to host
		client = new XMPPChatClient(this);
		client.setupContainer();
		client.setupPresence();

		// Get ISharedObjectContainer adapter
		ISharedObjectContainer socontainer = (ISharedObjectContainer) client
				.getContainer().getAdapter(ISharedObjectContainer.class);
		// Create TrivialSharedObject instance and add to container
		createTrivialSharedObjectForContainer(socontainer);

		// Then connect
		client.doConnect(userName + "@" + hostName, password);

		this.targetIMUser = targetIMUser;
		// Send initial message to target user
		client.sendChatMessage(targetIMUser, "Hi, I'm an IM robot");

		running = true;
		int count = 0;
		// Loop ten times and send ten 'hello there' so messages to targetIMUser
		while (running && count++ < 10) {
			sendSOMessage(count + " hello there");
			wait(10000);
		}
	}

	protected void sendSOMessage(String msg) {
		if (sharedObject != null) {
			sharedObject.sendMessageTo(client.createID(targetIMUser), msg);
		}
	}

	protected void createTrivialSharedObjectForContainer(
			ISharedObjectContainer soContainer) throws ECFException {
		if (soContainer != null) {
			// Create a new GUID for new TrivialSharedObject instance
			ID newID = IDFactory.getDefault().createStringID(
					TrivialSharedObject.class.getName());
			// Create TrivialSharedObject
			sharedObject = new TrivialSharedObject();
			// Add shared object to container
			soContainer.getSharedObjectManager().addSharedObject(newID,
					sharedObject, null);
		}
	}

	public synchronized void handleMessage(IChatMessage chatMessage) {
		// direct message
		// client.sendMessage(from,"gotta run");
		// running = false;
		notifyAll();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4017.java