error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10830.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10830.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10830.java
text:
```scala
c@@ontd = new ContainerTypeDescription(CONTAINER_FACTORY_NAME,CONTAINER_FACTORY_CLASS,null);

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.app;

import java.util.HashMap;
import java.util.Random;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.sharedobject.SharedObjectContainerFactory;
import org.eclipse.ecf.provider.generic.GenericContainerInstantiator;
import org.eclipse.ecf.provider.generic.TCPServerSOContainer;

/**
 * An ECF client container implementation that runs as an application.
 * <p>
 * Usage: java org.eclipse.ecf.provider.app.ClientApplication &lt;serverid&gt
 * <p>
 * If &lt;serverid&gt; is omitted or "-" is specified,
 * ecftcp://localhost:3282/server" is used.  
 *  
 */
public class ClientApplication {

	public static final int DEFAULT_WAITTIME = 40000;

	public static final int DEFAULT_TIMEOUT = TCPServerSOContainer.DEFAULT_KEEPALIVE;
	
	public static final String CONTAINER_FACTORY_NAME = GenericContainerInstantiator.class.getName();
	public static final String CONTAINER_FACTORY_CLASS = CONTAINER_FACTORY_NAME;
	
	public static final String COMPOSENT_CONTAINER_NAME = GenericContainerInstantiator.class.getName();

	// Number of clients to create
	static int clientCount = 1;
	// Array of client instances
	ISharedObjectContainer [] sm = new ISharedObjectContainer[clientCount];
	// ServerApplication name to connect to
	String serverName = null;
	// Class names of any sharedObjects to be created.  If null, no sharedObjects created.
	String [] sharedObjectClassNames = null;
	// IDs of sharedObjects created
	ID [] sharedObjects = null;
	
	static ContainerTypeDescription contd = null;
	static Random aRan = new Random();
	public ClientApplication() {
		super();
	}
	
	public void init(String [] args) throws Exception {
		serverName = TCPServerSOContainer.getDefaultServerURL();
		if (args.length > 0) {
			if (!args[0].equals("-")) serverName = args[0]; //$NON-NLS-1$
		}
		if (args.length > 1) {
			sharedObjectClassNames = new String[args.length - 1];
			for (int i = 0; i < args.length - 1; i++) {
				sharedObjectClassNames[i] = args[i + 1];
			}
		}
		// Setup factory descriptions since Eclipse does not do this for us
		contd = new ContainerTypeDescription(ClientApplication.class.getClassLoader(),CONTAINER_FACTORY_NAME,CONTAINER_FACTORY_CLASS,null);
		ContainerFactory.getDefault().addDescription(contd);
		for(int i=0; i < clientCount; i++) {
			sm[i] = createClient();
		}
	}
	
	protected ISharedObjectContainer createClient() throws Exception {
		// Make identity instance for the new container
		ID newContainerID = IDFactory.getDefault().createGUID();
		ISharedObjectContainer result =  SharedObjectContainerFactory.getDefault().createSharedObjectContainer(contd,new Object[] { newContainerID, new Integer(DEFAULT_TIMEOUT)});
		return result;
	}
	
	public void connect(ID server) throws Exception {
		for(int i = 0; i < clientCount; i++) {
			System.out.print("ClientApplication "+sm[i].getID().getName()+" joining "+server.getName()+"..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			sm[i].connect(server,null);
			System.out.println("completed."); //$NON-NLS-1$
		}
	}

	public void disconnect() {
		for(int i = 0; i < clientCount; i++) {
			System.out.print("ClientApplication "+sm[i].getID().getName()+" leaving..."); //$NON-NLS-1$ //$NON-NLS-2$
			sm[i].disconnect();
			System.out.println("completed."); //$NON-NLS-1$
		}
	}
	
	public void createSharedObjects() throws Exception {
		if (sharedObjectClassNames != null) {
			for(int j=0; j < clientCount; j++) {
				ISharedObjectContainer scg = sm[j];
				sharedObjects = new ID[sharedObjectClassNames.length];
				for(int i=0; i < sharedObjectClassNames.length; i++) {
					System.out.println("Creating sharedObject: "+sharedObjectClassNames[i]+" for client "+scg.getID().getName()); //$NON-NLS-1$ //$NON-NLS-2$
					ISharedObject so = (ISharedObject) Class.forName(sharedObjectClassNames[i]).newInstance();
					sharedObjects[i] = IDFactory.getDefault().createStringID(sharedObjectClassNames[i] + "_" +  i); //$NON-NLS-1$
					scg.getSharedObjectManager().addSharedObject(sharedObjects[i], so, new HashMap());
					System.out.println("Created sharedObject for client "+scg.getID().getName()); //$NON-NLS-1$
				}
			}
		}

	}
	public void removeSharedObjects() throws Exception {
		if (sharedObjects == null) return;
		for(int j=0; j < clientCount; j++) {
			for(int i=0; i < sharedObjects.length; i++) {
				System.out.println("Removing sharedObject: "+sharedObjects[i].getName()+" for client "+sm[j].getID().getName()); //$NON-NLS-1$ //$NON-NLS-2$
				sm[j].getSharedObjectManager().removeSharedObject(sharedObjects[i]);
			}
		}
	}
	/**
	 * An ECF client container implementation that runs as an application.
	 * <p>
	 * Usage: java org.eclipse.ecf.provider.app.ClientApplication &lt;serverid&gt
	 * <p>
	 * If &lt;serverid&gt; is omitted or "-" is specified,
	 * ecftcp://localhost:3282/server" is used.  
	 *  
	 */
	public static void main(String[] args) throws Exception {
		ClientApplication st = new ClientApplication();
		st.init(args);
		// Get server id to join
		ID serverID = IDFactory.getDefault().createStringID(st.serverName);
		st.connect(serverID);
		st.createSharedObjects();
		System.out.println("Waiting "+DEFAULT_WAITTIME+" ms..."); //$NON-NLS-1$ //$NON-NLS-2$
		Thread.sleep(DEFAULT_WAITTIME);
		st.removeSharedObjects();
		st.disconnect();
		System.out.println("Exiting."); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10830.java