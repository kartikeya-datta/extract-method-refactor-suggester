error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2062.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2062.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2062.java
text:
```scala
final I@@D departedContainerID = ce.getTargetID();

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
package org.eclipse.ecf.example.collab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ecf.core.IContainerListener;
import org.eclipse.ecf.core.events.IContainerEjectedEvent;
import org.eclipse.ecf.core.events.IContainerEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.example.collab.share.EclipseCollabSharedObject;
import org.eclipse.ecf.example.collab.share.SharedObjectEventListener;
import org.eclipse.ecf.example.collab.share.TreeItem;
import org.eclipse.ecf.example.collab.share.User;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class SharedObjectContainerUI {
	public static final String JOIN_TIME_FORMAT = "hh:mm:ss a z";
	public static final String FILE_DIRECTORY = "received_files";
	public static final String ECFDIRECTORY = "ECF_" + FILE_DIRECTORY + "/";
	public static final String COLLAB_SHARED_OBJECT_ID = "chat";
	ISharedObjectContainer soc = null;
	CollabClient collabclient = null;
	
	public SharedObjectContainerUI(CollabClient client, ISharedObjectContainer soc) {
		this.collabclient = client;
		this.soc = soc;
	}

	protected String getSharedFileDirectoryForProject(IResource proj) {
		String eclipseDir = Platform.getLocation().lastSegment();
		if (proj == null)
			return eclipseDir + "/" + ECFDIRECTORY;
		else
			return FILE_DIRECTORY;
	}

	protected User getUserData(String containerType, ID clientID,
			String usernick, IResource project) {
		Vector topElements = new Vector();
		topElements.add(new TreeItem("Project", CollabClient
				.getNameForResource(project)));
		SimpleDateFormat sdf = new SimpleDateFormat(JOIN_TIME_FORMAT);
		topElements.add(new TreeItem("Time", sdf.format(new Date())));
		try {
			String userLang = System.getProperty("user.language");
			topElements.add(new TreeItem("Language", userLang));
		} catch (Exception e) {
		}
		try {
			String timeZone = System.getProperty("user.timezone");
			topElements.add(new TreeItem("Time Zone", timeZone));
		} catch (Exception e) {
		}
		try {
			String osgiVersion = System
					.getProperty("org.osgi.framework.version");
			topElements.add(new TreeItem("OSGI version", osgiVersion));
		} catch (Exception e) {
		}
		try {
			String javaVersion = System.getProperty("java.version");
			topElements.add(new TreeItem("Java", javaVersion));
		} catch (Exception e) {
		}
		try {
			String osName = Platform.getOS();
			topElements.add(new TreeItem("OS", osName));
		} catch (Exception e) {
		}
		return new User(clientID, usernick, topElements);
	}

	void addObjectToClient(ISharedObjectContainer soContainer, ClientEntry client, String username, IResource proj)
			throws Exception {
		IResource project = (proj == null) ? CollabClient.getWorkspace() : proj;
		User user = getUserData(client.getClass().getName(), client
				.getContainer().getID(), username, proj);
		createAndAddSharedObject(soContainer, client, project, user,
				getSharedFileDirectoryForProject(project));
	}

	public void setup(final ISharedObjectContainer soContainer, final ClientEntry newClientEntry,
			final IResource resource, String username) throws Exception {
		addObjectToClient(soContainer, newClientEntry, username, resource);
		soc.addListener(new IContainerListener() {
			public void handleEvent(IContainerEvent evt) {
				if (evt instanceof IContainerDisconnectedEvent) {
					IContainerDisconnectedEvent cd = (IContainerDisconnectedEvent) evt;
					final ID departedContainerID = cd.getTargetID();
					ID connectedID = newClientEntry.getConnectedID();
					if (connectedID == null
 connectedID.equals(departedContainerID)) {
						// This container is done
						if (!newClientEntry.isDisposed()) {
							collabclient.disposeClient(resource, newClientEntry);
						}
					}
				} else if (evt instanceof IContainerEjectedEvent) {
					IContainerEjectedEvent ce = (IContainerEjectedEvent) evt;
					final ID departedContainerID = ce.getGroupID();
					ID connectedID = newClientEntry.getConnectedID();
					if (connectedID == null || connectedID.equals(departedContainerID)) {
						if (!newClientEntry.isDisposed()) {
							collabclient.disposeClient(resource, newClientEntry);
						}
					}
				}
			}
		}, "");
	}

	protected void createAndAddSharedObject(final ISharedObjectContainer soContainer, final ClientEntry client,
			final IResource proj, User user, String fileDir) throws Exception {
		IWorkbenchWindow ww = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		EclipseCollabSharedObject sharedObject = new EclipseCollabSharedObject(
				proj, ww, user, fileDir);
		sharedObject.setListener(new SharedObjectEventListener() {
			public void memberRemoved(ID member) {
				ID groupID = client.getConnectedID();
				if (member.equals(groupID)) {
					if (!client.isDisposed()) {
						collabclient.disposeClient(proj, client);
					}
				}
			}

			public void memberAdded(ID member) {
			}

			public void otherActivated(ID other) {
			}

			public void otherDeactivated(ID other) {
			}

			public void windowClosing() {
				ID groupID = client.getConnectedID();
				CollabClient.removeClientForResource(proj, groupID);
			}
		});
		ID newID = IDFactory.getDefault().createStringID(COLLAB_SHARED_OBJECT_ID);
		soContainer.getSharedObjectManager().addSharedObject(newID,
				sharedObject, new HashMap());
			client.setObject(sharedObject);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2062.java