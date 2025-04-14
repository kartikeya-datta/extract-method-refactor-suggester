error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11087.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11087.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11087.java
text:
```scala
c@@ontainerAdapter.restore(prefs);

/****************************************************************************
 * Copyright (c) 2008 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.storage;

import org.eclipse.ecf.core.*;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.storage.*;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.StorageException;

/**
 *
 */
public class ContainerEntry implements IContainerEntry {

	private static final String FACTORY_NAME_KEY = "factoryName"; //$NON-NLS-1$

	ISecurePreferences prefs;
	IIDEntry idEntry;

	ID containerID;

	/**
	 * @param idEntry 
	 */
	public ContainerEntry(IIDEntry idEntry) {
		this.idEntry = idEntry;
		ISecurePreferences prefs = idEntry.getPreferences();
		this.prefs = prefs.node(ContainerStore.CONTAINER_NODE_NAME);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IContainerEntry#createContainer()
	 */
	public IContainer createContainer() throws ContainerCreateException {
		try {
			IContainer container = ContainerFactory.getDefault().createContainer(getFactoryName(), getContainerID());
			IStorableContainerAdapter containerAdapter = (IStorableContainerAdapter) container.getAdapter(IStorableContainerAdapter.class);
			if (containerAdapter != null) {
				containerAdapter.handleRestore(prefs);
			}
			return container;
		} catch (IDCreateException e) {
			throw new ContainerCreateException("Could not create ID for container", e); //$NON-NLS-1$
		} catch (StorageException e) {
			throw new ContainerCreateException("Exception on restore", e); //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IContainerEntry#delete()
	 */
	public void delete() {
		prefs.removeNode();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IContainerEntry#getContainerID()
	 */
	public ID getContainerID() throws IDCreateException {
		if (containerID == null) {
			containerID = idEntry.createID();
		}
		return containerID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IContainerEntry#getFactoryName()
	 */
	public String getFactoryName() throws StorageException {
		return prefs.get(FACTORY_NAME_KEY, ""); //$NON-NLS-1$
	}

	protected void setFactoryName(String factoryName, boolean encrypt) throws StorageException {
		prefs.put(FACTORY_NAME_KEY, factoryName, encrypt);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IContainerEntry#getPreferences()
	 */
	public ISecurePreferences getPreferences() {
		return prefs;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11087.java