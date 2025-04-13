error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8967.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8967.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8967.java
text:
```scala
e@@lse if (getContext().isGroupManager())

/*******************************************************************************
 * Copyright (c) 2004 Peter Nehrer and Composent, Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Peter Nehrer - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.example.sdo.editor;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.ecf.core.ISharedObject;
import org.eclipse.ecf.core.ISharedObjectConfig;
import org.eclipse.ecf.core.ISharedObjectContext;
import org.eclipse.ecf.core.SharedObjectDescription;
import org.eclipse.ecf.core.SharedObjectInitException;
import org.eclipse.ecf.core.events.ISharedObjectActivatedEvent;
import org.eclipse.ecf.core.events.ISharedObjectContainerDepartedEvent;
import org.eclipse.ecf.core.events.ISharedObjectContainerJoinedEvent;
import org.eclipse.ecf.core.events.ISharedObjectDeactivatedEvent;
import org.eclipse.ecf.core.events.ISharedObjectMessageEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.core.util.Event;

/**
 * <p>
 * Tracks explicit data graph publication (or any shared object presence,
 * really) across all containers in a connected group using the server replica
 * to bootstrap new members.
 * </p>
 * <p>
 * It works something like this:
 * </p>
 * <ul>
 * <li>The client should first check if an instance of this shared object
 * already exists in their container. If not, create one and add it.</li>
 * <li>The client should call {@link #add(ID) add()}once published/subscribed
 * to a data graph. The method will block until this object activates (if it
 * hasn't already).</li>
 * <li>Upon activation, the primary instance replicates everywhere. This is to
 * assure that there is a server replica (and a replica in the initial set of
 * connected containers).</li>
 * <li>When a new container joins the group, the server will create a replica
 * in it, initialized with its current graph location table. This is to assure
 * that new members are properly bootstrapped.</li>
 * <li>All replicas broadcast additions/removals of their local data graphs and
 * listen to remote additions/removals in order to keep track of what is
 * published and where.</li>
 * <li>When a container leaves the group, all replicas note it and update their
 * graph location tables. Likewise, when a replica deactivates, it broadcasts
 * its departure so others may update their tables.</li>
 * </ul>
 * <p>
 * It is assumed that the container implementation used with this class is
 * server-centric. That is, there is a server that is always connected before
 * any other container may connect. When the server disconnects, everyone else
 * in effect disconnects.
 * </p>
 * 
 * @author pnehrer
 */
public class PublishedGraphTracker implements ISharedObject {

	private static final ID[] NO_GRAPHS = {};

	private static final int ADD = 0;

	private static final int REMOVE = 1;

	private static final int LEAVE = 2;

	private static final String ARG_TABLE = "table";

	private class Table {

		private final Hashtable graphs = new Hashtable();

		private final Hashtable containers = new Hashtable();

		public synchronized void add(ID containerID, ID graphID) {
			HashSet list = (HashSet) graphs.get(containerID);
			if (list == null) {
				list = new HashSet();
				graphs.put(containerID, list);
			}

			list.add(graphID);
			list = (HashSet) containers.get(containerID);
			if (list == null) {
				list = new HashSet();
				containers.put(graphID, list);
			}

			list.add(containerID);
		}

		public synchronized void remove(ID containerID, ID graphID) {
			HashSet list = (HashSet) graphs.get(containerID);
			if (list != null) {
				list.remove(graphID);
				if (list.isEmpty())
					graphs.remove(containerID);
			}

			list = (HashSet) containers.get(graphID);
			if (list != null) {
				list.remove(containerID);
				if (list.isEmpty())
					containers.remove(graphID);
			}
		}

		public synchronized void remove(ID containerID) {
			HashSet list = (HashSet) graphs.get(containerID);
			if (list != null) {
				for (Iterator i = list.iterator(); i.hasNext();) {
					ID graphID = (ID) i.next();
					list = (HashSet) containers.get(graphID);
					if (list != null) {
						list.remove(containerID);
						if (list.isEmpty())
							containers.remove(graphID);
					}
				}
			}
		}

		public synchronized boolean contains(ID graphID) {
			return containers.containsKey(graphID);
		}

		public synchronized ID[] getGraphs(ID containerID) {
			HashSet list = (HashSet) graphs.get(containerID);
			return list == null ? NO_GRAPHS : (ID[]) list.toArray(new ID[list
					.size()]);
		}

		public synchronized Object createMemento() {
			return new Hashtable[] { graphs, containers };
		}

		public synchronized void load(Object memento) {
			Hashtable[] tables = (Hashtable[]) memento;
			graphs.putAll(tables[0]);
			containers.putAll(tables[1]);
		}
	}

	private final Table table = new Table();

	private ISharedObjectConfig config;

	private ISharedObjectContext context;

	private final Object activationMutex = new Object();

	private boolean activated;

	/**
	 * Adds a graph to the list of published graphs.
	 * 
	 * @param graphID
	 *            identifier of the graph that was published
	 * @throws ECFException
	 */
	public synchronized void add(ID graphID) throws ECFException {
		if (config == null)
			throw new ECFException("Not initialized.");

		// wait to be activated before proceeding
		synchronized (activationMutex) {
			if (!activated)
				try {
					activationMutex.wait(1000);
				} catch (InterruptedException e) {
					throw new ECFException(e);
				}

			if (!activated)
				throw new ECFException("Not activated.");
		}

		// tell everyone a graph was published
		try {
			getContext().sendMessage(null,
					new Object[] { new Integer(ADD), graphID });
		} catch (IOException e) {
			throw new ECFException(e);
		}

		// track it yourself
		handleAdd(getContext().getLocalContainerID(), graphID);
	}

	/**
	 * Answers whether a graph is published (at the time of invocation).
	 * 
	 * @param graphID
	 *            identifier of the graph whose publishing status to return
	 * @return <code>true</code> if the graph is published
	 */
	public synchronized boolean isPublished(ID graphID) {
		return table.contains(graphID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObject#init(org.eclipse.ecf.core.ISharedObjectConfig)
	 */
	public synchronized void init(ISharedObjectConfig initData)
			throws SharedObjectInitException {
		if (config == null)
			config = initData;
		else
			throw new SharedObjectInitException("Already initialized.");

		Map props = (Map) config.getProperties();
		if (props != null) {
			Object memento = props.get(ARG_TABLE);
			if (memento != null)
				table.load(memento);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObject#handleEvent(org.eclipse.ecf.core.util.Event)
	 */
	public void handleEvent(Event event) {
		if (event instanceof ISharedObjectMessageEvent) {
			// track graph additions/removals and peer departures
			// (deactivations)
			ISharedObjectMessageEvent e = (ISharedObjectMessageEvent) event;
			Object[] data = (Object[]) e.getData();
			Integer type = (Integer) data[0];
			switch (type.intValue()) {
			case ADD:
				handleAdd(e.getRemoteContainerID(), (ID) data[1]);
				break;

			case REMOVE:
				handleRemove(e.getRemoteContainerID(), (ID) data[1]);
				break;

			case LEAVE:
				handleLeave(e.getRemoteContainerID());
				break;
			}
		} else if (event instanceof ISharedObjectContainerJoinedEvent) {
			ISharedObjectContainerJoinedEvent e = (ISharedObjectContainerJoinedEvent) event;
			if (e.getJoinedContainerID().equals(
					getContext().getLocalContainerID()))
				// this container joined
				handleJoined();
			else if (getContext().isGroupServer())
				// some other container joined and we're the server
				handleJoined(e.getJoinedContainerID());
		} else if (event instanceof ISharedObjectContainerDepartedEvent) {
			ISharedObjectContainerDepartedEvent e = (ISharedObjectContainerDepartedEvent) event;
			// some other container departed -- same as peer deactivation
			if (!e.getDepartedContainerID().equals(
					getContext().getLocalContainerID()))
				handleLeave(e.getDepartedContainerID());
		} else if (event instanceof ISharedObjectActivatedEvent) {
			ISharedObjectActivatedEvent e = (ISharedObjectActivatedEvent) event;
			if (e.getActivatedID().equals(config.getSharedObjectID()))
				// we're being activated
				handleActivated();
		} else if (event instanceof ISharedObjectDeactivatedEvent) {
			ISharedObjectDeactivatedEvent e = (ISharedObjectDeactivatedEvent) event;
			if (e.getDeactivatedID().equals(config.getSharedObjectID()))
				// we're being deactivated
				handleDeactivated();
			else if (table.contains(e.getDeactivatedID()))
				// a local graph we track is being deactivated
				handleRemoved(e.getDeactivatedID());
		}
	}

	private void handleAdd(ID containerID, ID graphID) {
		table.add(containerID, graphID);
	}

	private void handleRemove(ID containerID, ID graphID) {
		table.remove(containerID, graphID);
	}

	private void handleLeave(ID containerID) {
		table.remove(containerID);
	}

	private void handleJoined() {
		if (config.getHomeContainerID().equals(
				getContext().getLocalContainerID())) {
			// we're the primary copy -- replicate everywhere
			try {
				getContext().sendCreate(
						null,
						new SharedObjectDescription(config.getSharedObjectID(),
								getClass()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handleJoined(ID containerID) {
		Map props = new HashMap(1);
		props.put(ARG_TABLE, table.createMemento());
		try {
			getContext().sendCreate(
					containerID,
					new SharedObjectDescription(config.getSharedObjectID(),
							getClass(), props));
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	private void handleActivated() {
		handleJoined();
		synchronized (activationMutex) {
			activated = true;
			activationMutex.notifyAll();
		}
	}

	private void handleDeactivated() {
		try {
			getContext().sendMessage(null, new Object[] { new Integer(LEAVE) });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		synchronized (activationMutex) {
			activated = false;
			activationMutex.notifyAll();
		}
	}

	private void handleRemoved(ID graphID) {
		try {
			getContext().sendMessage(null,
					new Object[] { new Integer(REMOVE), graphID });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		handleRemove(getContext().getLocalContainerID(), graphID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObject#handleEvents(org.eclipse.ecf.core.util.Event[])
	 */
	public void handleEvents(Event[] events) {
		for (int i = 0; i < events.length; ++i)
			handleEvent(events[i]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObject#dispose(org.eclipse.ecf.core.identity.ID)
	 */
	public synchronized void dispose(ID containerID) {
		config = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.PlatformObject#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	private ISharedObjectContext getContext() {
		if (context == null)
			context = config.getContext();

		return context;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8967.java