error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17994.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17994.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17994.java
text:
```scala
I@@SharedFile sf = fsc.store(sfs);

package org.eclipse.ecf.example.collab;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.ISharedObjectContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.security.ObjectCallback;
import org.eclipse.ecf.example.collab.share.EclipseCollabSharedObject;
import org.eclipse.ecf.fileshare.IFileShareContainer;
import org.eclipse.ecf.presence.IPresenceContainer;
import org.eclipse.ecf.presence.chat.IChatRoomManager;

public class CollabClient {
	public static final String WORKSPACE_NAME = "<workspace>";
    public static final String GENERIC_CONTAINER_CLIENT_NAME = "org.eclipse.ecf.provider.generic.Client";
	static Hashtable clients = new Hashtable();
	static CollabClient collabClient = new CollabClient();

	PresenceContainerUI presenceContainerUI = null;
	ChatRoomManagerUI chatRoomManagerUI = null;
	
	/**
	 * Create a new container instance, and connect to a remote server or group.
	 * 
	 * @param containerType the container type used to create the new container instance.  Must not be null.
	 * @param uri the uri that is used to create a targetID for connection.  Must not be null.
	 * @param nickname an optional String nickname.  May be null.
	 * @param connectData optional connection data.  May be null.
	 * @param resource the resource that this container instance is associated with.  Must not be null.
	 * @throws Exception
	 */
	public void createAndConnectClient(final String containerType, String uri,
			String nickname, final Object connectData, final IResource resource)
			throws Exception {
		// First create the new container instance
		final IContainer newClient = ContainerFactory
				.getDefault().makeContainer(containerType);
		
		// Get the target namespace, so we can create a target ID of appropriate type
		Namespace targetNamespace = newClient.getConnectNamespace();
		// Create the targetID instance
		ID targetID = IDFactory.getDefault().makeID(targetNamespace, uri);
		
		// Setup username
		String username = setupUsername(targetID,nickname);
		// Create a new client entry to hold onto container once created
		final ClientEntry newClientEntry = new ClientEntry(containerType,
				newClient);
		
		// Setup user interfaces for various container adapter types
		IFileShareContainer fsc = (IFileShareContainer) newClient.getAdapter(IFileShareContainer.class);
		
		if (fsc == null) {
			IChatRoomManager man = (IChatRoomManager) newClient.getAdapter(IChatRoomManager.class);
			if (man != null) {
				chatRoomManagerUI = new ChatRoomManagerUI(man);
				chatRoomManagerUI = chatRoomManagerUI.setup(newClient, targetID, username);
			} else {
			     // Check for IPresenceContainer....if it is, setup presence UI, if not setup shared object container
				IPresenceContainer pc = (IPresenceContainer) newClient
						.getAdapter(IPresenceContainer.class);
				if (pc != null) {
					// Setup presence UI
					presenceContainerUI = new PresenceContainerUI(pc);
					presenceContainerUI.setup(newClient, targetID, username);
				} else {
					// Setup sharedobject container if the new instance supports this
					ISharedObjectContainer sharedObjectContainer = (ISharedObjectContainer) newClient
							.getAdapter(ISharedObjectContainer.class);
					if (sharedObjectContainer != null) {
						new SharedObjectContainerUI(this,sharedObjectContainer).setup(sharedObjectContainer,
								newClientEntry, resource, username);
					}
				}
			}
		}
		
		// Now connect
		try {
			newClient.connect(targetID, getJoinContext(username, connectData));
		} catch (ContainerConnectException e) {
			// If we have a connect exception then we remove any previously added shared object
			EclipseCollabSharedObject so = newClientEntry.getObject();
			if (so != null) so.destroySelf();
			throw e;
		}
		
		/*
		if (fsc != null) {
			// XXX Testing
			IStoreFileDescription sfs = new IStoreFileDescription() {
				public InputStream getLocalStream() throws IOException {
					return new FileInputStream("notice.html");
				}
				public String getRemoteFileName() {
					return "rnotice.html";
				}
				public ISharedFileEventListener getEventListener() {
					return null;
				}
				public Map getProperties() {
					return null;
				}
			};
			ISharedFile sf = fsc.storeSharedFile(sfs);
			try {
				sf.start();
			} catch (SharedFileStartException e) {
				e.printStackTrace();
			}
		}
		*/
		// only add client if the connect was successful
		addClientForResource(newClientEntry, resource);
	}

	public ClientEntry isConnected(IResource project, String type) {
		ClientEntry entry = getClientEntry(project, type);
		return entry;
	}

	protected static void addClientForResource(ClientEntry entry, IResource proj) {
		synchronized (clients) {
			String name = getNameForResource(proj);
			Vector v = (Vector) clients.get(name);
			if (v == null) {
				v = new Vector();
			}
			v.add(entry);
			clients.put(name, v);
		}
	}

	protected static void removeClientForResource(IResource proj, ID targetID) {
		synchronized (clients) {
			String resourceName = getNameForResource(proj);
			Vector v = (Vector) clients.get(resourceName);
			if (v == null)
				return;
			ClientEntry remove = null;
			for (Iterator i = v.iterator(); i.hasNext();) {
				ClientEntry e = (ClientEntry) i.next();
				ID connectedID = e.getConnectedID();
				if (connectedID == null || connectedID.equals(targetID)) {
					remove = e;
				}
			}
			if (remove != null)
				v.remove(remove);
			if (v.size() == 0) {
				clients.remove(resourceName);
			}
		}
	}

	public static String getNameForResource(IResource res) {
		String preName = res.getName().trim();
		if (preName == null || preName.equals("")) {
			preName = WORKSPACE_NAME;
		}
		return preName;
	}

	protected static IResource getWorkspace() throws Exception {
		IWorkspaceRoot ws = ResourcesPlugin.getWorkspace().getRoot();
		return ws;
	}

	protected static Vector getClientEntries(IResource proj) {
		synchronized (clients) {
			return (Vector) clients.get(getNameForResource(proj));
		}
	}

	protected static ClientEntry getClientEntry(IResource proj, ID targetID) {
		synchronized (clients) {
			Vector v = (Vector) getClientEntries(proj);
			if (v == null)
				return null;
			for (Iterator i = v.iterator(); i.hasNext();) {
				ClientEntry e = (ClientEntry) i.next();
				ID connectedID = e.getConnectedID();
				if (connectedID == null)
					continue;
				else if (connectedID.equals(targetID)) {
					return e;
				}
			}
		}
		return null;
	}

	protected static ClientEntry getClientEntry(IResource proj,
			String containerType) {
		synchronized (clients) {
			Vector v = (Vector) getClientEntries(proj);
			if (v == null)
				return null;
			for (Iterator i = v.iterator(); i.hasNext();) {
				ClientEntry e = (ClientEntry) i.next();
				ID connectedID = e.getConnectedID();
				if (connectedID == null)
					continue;
				else {
					String contType = e.getContainerType();
					if (contType.equals(containerType)) {
						return e;
					}
				}
			}
		}
		return null;
	}

	protected static boolean containsEntry(IResource proj, ID targetID) {
		synchronized (clients) {
			Vector v = (Vector) clients.get(getNameForResource(proj));
			if (v == null)
				return false;
			for (Iterator i = v.iterator(); i.hasNext();) {
				ClientEntry e = (ClientEntry) i.next();
				ID connectedID = e.getConnectedID();
				if (connectedID == null)
					continue;
				else if (connectedID.equals(targetID)) {
					return true;
				}
			}
		}
		return false;
	}
    public synchronized static ISharedObjectContainer getContainer(IResource proj) {
        ClientEntry entry = getClientEntry(proj,GENERIC_CONTAINER_CLIENT_NAME);
        if (entry == null) {
            entry = getClientEntry(ResourcesPlugin.getWorkspace().getRoot(),GENERIC_CONTAINER_CLIENT_NAME);
        }
        if (entry != null) {
        	IContainer cont = entry.getContainer();
        	if (cont != null) return (ISharedObjectContainer) cont.getAdapter(ISharedObjectContainer.class);
        	else return null;
        }
        else return null;
    }
	public static CollabClient getDefault() {
		return collabClient;
	}
	protected synchronized void disposeClient(IResource proj, ClientEntry entry) {
		entry.dispose();
		removeClientForResource(proj, entry.getConnectedID());
	}

	protected IConnectContext getJoinContext(final String username,
			final Object password) {
		return new IConnectContext() {
			public CallbackHandler getCallbackHandler() {
				return new CallbackHandler() {
					public void handle(Callback[] callbacks)
							throws IOException, UnsupportedCallbackException {
						if (callbacks == null)
							return;
						for (int i = 0; i < callbacks.length; i++) {
							if (callbacks[i] instanceof NameCallback) {
								NameCallback ncb = (NameCallback) callbacks[i];
								ncb.setName(username);
							} else if (callbacks[i] instanceof ObjectCallback) {
								ObjectCallback ocb = (ObjectCallback) callbacks[i];
								ocb.setObject(password);
							}
						}
					}
				};
			}
		};
	}
	protected String setupUsername(ID targetID, String nickname) throws URISyntaxException {
		String username = null;
		if (nickname != null) {
			username = nickname;
		} else {
			username = targetID.toURI().getUserInfo();
			if (username == null || username.equals(""))
				username = System.getProperty("user.name");
		}
		return username;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17994.java