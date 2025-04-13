error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12405.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12405.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[152,2]

error in qdox parser
file content:
```java
offset: 6867
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12405.java
text:
```scala
import org.eclipse.ecf.core.sharedobject.security.ISharedObjectPolicy;

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.app;

import java.io.FileInputStream;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.security.IConnectPolicy;
import org.eclipse.ecf.core.security.ISharedObjectPolicy;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainerGroupManager;
import org.eclipse.ecf.core.sharedobject.ISharedObjectManager;
import org.eclipse.ecf.core.sharedobject.ReplicaSharedObjectDescription;
import org.eclipse.ecf.provider.generic.SOContainerConfig;
import org.eclipse.ecf.provider.generic.TCPServerSOContainer;
import org.eclipse.ecf.provider.generic.TCPServerSOContainerGroup;

/**
 * An ECF server container implementation that runs as an application.
 * <p>
 * Usage: java org.eclipse.ecf.provider.app.ServerApplication [-c &lt;configfile&gt | &lt;serverid&gt]
 * <p>
 * If -p &lt;configfile&gt is used, the server configuration is loaded and used to setup servers.
 * If &lt;serverid&gt; is omitted or "-" is specified,
 * ecftcp://localhost:3282/server" is used.  The &lt;serverid&gt; must correspond to URI syntax as 
 * defined by <a href="http://www.ietf.org/rfc/rfc2396.txt"><i>RFC&nbsp;2396: Uniform
 * Resource Identifiers (URI): Generic Syntax</i></a>, amended by <a href="http://www.ietf.org/rfc/rfc2732.txt"><i>RFC&nbsp;2732: 
 * Format for Literal IPv6 Addresses in URLs</i></a>
 *  
 */
public class ServerApplication {
    public static final int DEFAULT_KEEPALIVE = TCPServerSOContainer.DEFAULT_KEEPALIVE;
    static TCPServerSOContainerGroup serverGroups[] = null;
    static List servers = new ArrayList();

    static class JoinListener implements IConnectPolicy {
		public PermissionCollection checkConnect(Object addr, ID fromID, ID targetID, String targetGroup, Object joinData) throws SecurityException {
			System.out.println("JOIN Addr="+addr+";From="+fromID+";Group="+targetGroup+";Data="+joinData);
			return null;
		}

		public void refresh() {
			System.out.println("joinpolicy.refresh()");
		}
    	
    }
    static class SharedObjectAddListener implements ISharedObjectPolicy {

		public PermissionCollection checkAddSharedObject(ID fromID, ID toID, ID localID, ReplicaSharedObjectDescription newObject) throws SecurityException {
			System.out.println("CHECKADDSHAREDOBJECT From="+fromID+";To="+toID+";SharedObject="+newObject);
			return null;
		}

		public void refresh() {
			System.out.println("joinpolicy.refresh()");
		}
    	
    }

    public static void main(String args[]) throws Exception {
        // Get server identity
        String serverName = null;
		List connectors = null;
        if (args.length > 0) {
			if (args[0].equals("-c")) {
				ServerConfigParser parser = new ServerConfigParser();
				connectors = parser.load(new FileInputStream(args[1]));
			} else if (!args[0].equals("-"))
                serverName = args[0];
        }
		if (connectors != null) {
			serverGroups = new TCPServerSOContainerGroup[connectors.size()];
			int j=0;
			for(Iterator i=connectors.iterator(); i.hasNext(); ) {
				Connector connect = (Connector) i.next();
				serverGroups[j] = createServerGroup(connect.getHostname(),connect.getPort());
				List groups = connect.getGroups();
				
				for(Iterator g=groups.iterator(); g.hasNext(); ) {
					NamedGroup group = (NamedGroup) g.next();
					TCPServerSOContainer cont = createServerContainer(group.getIDForGroup(),serverGroups[j],group.getName(),connect.getTimeout());
					servers.add(cont);
				}
				System.out.println("Putting server "+connect.getHostname()+" on the air");
				serverGroups[j].putOnTheAir();
				j++;
		        System.out.println("<ctrl>-c to stop server");			
			}
		} else {
	        if (serverName == null) {
	            serverName = TCPServerSOContainer.getDefaultServerURL();
	        }
	        java.net.URI anURL = new java.net.URI(serverName);
	        int port = anURL.getPort();
	        if (port == -1) {
	            port = TCPServerSOContainer.DEFAULT_PORT;
	        }
	        String name = anURL.getPath();
	        if (name == null) {
	            name = TCPServerSOContainer.DEFAULT_NAME;
	        }
			serverGroups = new TCPServerSOContainerGroup[1];
	        // Setup server group
	        serverGroups[0] = new TCPServerSOContainerGroup(anURL.getPort());
	        // Create identity for server
	        ID id = IDFactory.getDefault().createStringID(serverName);
	        // Create server config object with identity and default timeout
	        SOContainerConfig config = new SOContainerConfig(id);
	        // Make server instance
	        System.out.print("Creating ECF server container...");
	        TCPServerSOContainer server = new TCPServerSOContainer(config, serverGroups[0], name,
	                TCPServerSOContainer.DEFAULT_KEEPALIVE);
	        // Setup join policy
	        ((ISharedObjectContainerGroupManager)server).setConnectPolicy(new JoinListener());
	        // Setup add shared object policy
	        ISharedObjectManager manager = server.getSharedObjectManager();
	        manager.setRemoteAddPolicy(new SharedObjectAddListener());

	        serverGroups[0].putOnTheAir();
			servers.add(server);
	        System.out.println("success!");
	        System.out
	                .println("Waiting for JOIN requests at '" + id.getName() + "'...");
	        System.out.println("<ctrl>-c to stop server");			
		}
    }
	
	protected static TCPServerSOContainerGroup createServerGroup(String name, int port) {
		System.out.println("Creating server group named "+name+" to listen on port "+port);
		TCPServerSOContainerGroup group = new TCPServerSOContainerGroup(name,port);
		return group;
	}
	protected static TCPServerSOContainer createServerContainer(String id, TCPServerSOContainerGroup group, String path, int keepAlive) throws IDCreateException {
		System.out.println("  Creating container with id="+id+", group="+path+" keepAlive="+keepAlive);
		ID newServerID = IDFactory.getDefault().createStringID(id);
		SOContainerConfig config = new SOContainerConfig(newServerID);
		return new TCPServerSOContainer(config,group,path,keepAlive);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12405.java