error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1043.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1043.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1043.java
text:
```scala
a@@ctivator.getLog().log(new Status(IStatus.INFO, Activator.PLUGIN_ID, IStatus.INFO, NLS.bind(Messages.ECFStart_ERROR_DOCUMENT_SHARE_NOT_CREATED, container.getID()), null));

/****************************************************************************
 * Copyright (c) 20047 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.docshare;

import org.eclipse.core.runtime.*;
import org.eclipse.ecf.core.*;
import org.eclipse.ecf.core.events.*;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.start.IECFStart;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.datashare.IChannelContainerAdapter;
import org.eclipse.osgi.util.NLS;

public class ECFStart implements IECFStart {

	IContainerListener containerListener = new IContainerListener() {

		/* (non-Javadoc)
		 * @see org.eclipse.ecf.core.IContainerListener#handleEvent(org.eclipse.ecf.core.events.IContainerEvent)
		 */
		public void handleEvent(IContainerEvent event) {
			Activator activator = Activator.getDefault();
			if (activator == null)
				return;
			final IContainerManager containerManager = activator.getContainerManager();
			if (containerManager == null)
				return;
			IContainer container = containerManager.getContainer(event.getLocalContainerID());
			if (container == null)
				return;
			if (event instanceof IContainerConnectedEvent || event instanceof IContainerDisconnectedEvent) {
				// connected
				IChannelContainerAdapter cca = (IChannelContainerAdapter) container.getAdapter(IChannelContainerAdapter.class);
				if (cca == null)
					return;
				ID containerID = container.getID();
				if (event instanceof IContainerConnectedEvent) {
					try {
						activator.addDocShare(containerID, cca);
					} catch (ECFException e) {
						activator.getLog().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, IStatus.WARNING, NLS.bind(Messages.ECFStart_ERROR_DOCUMENT_SHARE_NOT_CREATED, container.getID()), null));
					}
				} else if (event instanceof IContainerDisconnectedEvent) {
					// disconnected
					Activator.getDefault().removeDocShare(containerID);
				}
			} else if (event instanceof IContainerDisposeEvent) {
				containerManager.removeListener(containerManagerListener);
				container.removeListener(containerListener);
			}
		}

	};

	IContainerManagerListener containerManagerListener = new IContainerManagerListener() {

		/* (non-Javadoc)
		 * @see org.eclipse.ecf.core.IContainerManagerListener#containerAdded(org.eclipse.ecf.core.IContainer)
		 */
		public void containerAdded(IContainer container) {
			IChannelContainerAdapter cca = (IChannelContainerAdapter) container.getAdapter(IChannelContainerAdapter.class);
			if (cca == null)
				return;
			container.addListener(containerListener);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ecf.core.IContainerManagerListener#containerRemoved(org.eclipse.ecf.core.IContainer)
		 */
		public void containerRemoved(IContainer container) {
			container.removeListener(containerListener);
		}
	};

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.start.IECFStart#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus run(IProgressMonitor monitor) {
		final IContainerManager containerManager = Activator.getDefault().getContainerManager();
		if (containerManager == null)
			return new Status(IStatus.WARNING, Activator.PLUGIN_ID, IStatus.WARNING, Messages.ECFStart_ERROR_CONTAINER_MANAGER_NOT_AVAILABLE, null);
		containerManager.addListener(containerManagerListener);
		return Status.OK_STATUS;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1043.java