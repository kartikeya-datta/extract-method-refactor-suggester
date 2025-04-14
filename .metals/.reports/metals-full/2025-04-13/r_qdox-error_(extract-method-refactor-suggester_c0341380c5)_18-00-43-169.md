error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5443.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5443.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5443.java
text:
```scala
S@@tring singletonName = adapterType.getName();

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
package org.eclipse.ecf.core.sharedobject;

import java.util.Map;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ecf.core.AbstractContainerAdapterFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.internal.core.sharedobject.Activator;
import org.eclipse.ecf.internal.core.sharedobject.Messages;
import org.eclipse.ecf.internal.core.sharedobject.SharedObjectDebugOptions;

/**
 * Abstract container adapter factory. This class implements the
 * {@link IAdapterFactory} interface. It checks that the first parameter of the
 * {@link #getAdapter(Object, Class)} method (adaptableObject) is an instance of
 * {@link ISharedObjectContainer}. If it is, then the method
 * {@link #getSharedObjectAdapter(ISharedObjectContainer, Class)} is called with
 * the ISharedObjectContainer and Class passed in as arguments.
 * 
 * @see #getSharedObjectAdapter(ISharedObjectContainer, Class)
 * 
 */
public abstract class AbstractSharedObjectContainerAdapterFactory extends
		AbstractContainerAdapterFactory {

	protected static final int ADD_ADAPTER_ERROR_CODE = 300001;

	protected static final String ADD_ADAPTER_ERROR_MESSAGE = Messages.AbstractSharedObjectContainerAdapterFactory_Exception_Adding_Adapter;

	private static final int CREATE_ADAPTER_ID_ERROR_CODE = 300002;

	private static final String CREATE_ADAPTER_ID_ERROR_MESSAGE = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.AbstractContainerAdapterFactory#getContainerAdapter(org.eclipse.ecf.core.IContainer,
	 *      java.lang.Class)
	 */
	protected Object getContainerAdapter(IContainer container, Class adapterType) {
		if (ISharedObjectContainer.class.isInstance(container))
			return getSharedObjectAdapter((ISharedObjectContainer) container,
					adapterType);
		else
			return null;
	}

	/**
	 * Get the {@link ISharedObject} adapter for given
	 * {@link ISharedObjectContainer}. The resulting {@link ISharedObject} must
	 * <b>also</b> implement the adapterType interface. Once called, this
	 * method will call the following methods in order:
	 * <p>
	 * </p>
	 * {@link #createAdapterID(ISharedObjectContainer, Class)}
	 * <p>
	 * </p>
	 * {@link #createAdapter(ISharedObjectContainer, Class, ID)}
	 * <p>
	 * </p>
	 * {@link #createAdapterProperties(ISharedObjectContainer, Class, ID, ISharedObject)}
	 * 
	 * @param container
	 *            the {@link ISharedObjectContainer} that will hold the new
	 *            {@link ISharedObject} adapter
	 * @param adapterType
	 *            the type that the {@link ISharedObject} must also implement to
	 *            be an adapter
	 * @return ISharedObject adapter. Must also implement adapterType interface
	 *         class
	 */
	protected synchronized ISharedObject getSharedObjectAdapter(
			ISharedObjectContainer container, Class adapterType) {
		// Get adapter ID for given adapter type
		ID adapterID = createAdapterID(container, adapterType);
		if (adapterID == null)
			return null;
		// Check to see if the container already has the given shared object
		// If so then return it
		ISharedObjectManager manager = container.getSharedObjectManager();
		ISharedObject so = manager.getSharedObject(adapterID);
		if (so != null)
			return so;
		// Now create adapter instance since it's not already there
		ISharedObject adapter = createAdapter(container, adapterType, adapterID);
		if (adapter == null)
			return null;
		Map adapterProperties = createAdapterProperties(container, adapterType,
				adapterID, adapter);
		try {
			manager.addSharedObject(adapterID, adapter, adapterProperties);
		} catch (SharedObjectAddException e) {
			Trace.catching(Activator.PLUGIN_ID,
					SharedObjectDebugOptions.EXCEPTIONS_CATCHING,
					AbstractSharedObjectContainerAdapterFactory.class,
					"getSharedObjectAdapter", e); //$NON-NLS-1$
			Activator.getDefault().log(
					new Status(IStatus.ERROR, Activator.getDefault()
							.getBundle().getSymbolicName(),
							ADD_ADAPTER_ERROR_CODE, ADD_ADAPTER_ERROR_MESSAGE,
							e));
			return null;
		}
		return adapter;
	}

	/**
	 * Get properties to associate with new shared object adapter creation
	 * 
	 * @param container
	 *            the container that will contain the new adapter shared object
	 * @param adapterType
	 *            the adapterType for the new shared object
	 * @param sharedObjectID
	 *            the ID for the new shared object adapter
	 * @param sharedObjectAdapter
	 *            the new shared object adapter
	 * @return Map of properties to associated with new shared object adapter.
	 *         If null is returned then no properties will be associated with
	 *         new shared object adapter. This implementation returns null.
	 *         Subclasses may override as appropriate
	 */
	protected Map createAdapterProperties(ISharedObjectContainer container,
			Class adapterType, ID sharedObjectID,
			ISharedObject sharedObjectAdapter) {
		return null;
	}

	/**
	 * Get the adapterID for the given adapterType
	 * 
	 * @param container
	 *            the container the adapter will be added to
	 * @param adapterType
	 *            the type of the adapter
	 * @return ID the ID to use for the adapter. If null is returned, then
	 *         {@link #getSharedObjectAdapter(ISharedObjectContainer, Class)}
	 *         will also return null
	 */
	protected ID createAdapterID(ISharedObjectContainer container,
			Class adapterType) {
		String singletonName = adapterType.getClass().getName();
		try {
			return IDFactory.getDefault().createStringID(singletonName);
		} catch (IDCreateException e) {
			Trace.catching(Activator.PLUGIN_ID,
					SharedObjectDebugOptions.EXCEPTIONS_CATCHING,
					AbstractSharedObjectContainerAdapterFactory.class,
					"getAdapterID", e); //$NON-NLS-1$
			Activator.getDefault().log(
					new Status(IStatus.ERROR, Activator.getDefault()
							.getBundle().getSymbolicName(),
							CREATE_ADAPTER_ID_ERROR_CODE,
							CREATE_ADAPTER_ID_ERROR_MESSAGE, e));
			return null;
		}
	}

	/**
	 * Create an adapter instance that implements {@link ISharedObject} and
	 * adapterType. The resulting instance must implement both
	 * {@link ISharedObject} and adapterType
	 * 
	 * @param container
	 *            the container that will contain the new adapter instance
	 * @param adapterType
	 *            the adapter type. The returned value must implement this
	 *            interface
	 * @param adapterID
	 *            the ID to use for the new adapter
	 * @return ISharedObject the new adapter. If null is returned, then
	 *         {@link #getSharedObjectAdapter(ISharedObjectContainer, Class)}
	 *         will also return null
	 */
	protected abstract ISharedObject createAdapter(
			ISharedObjectContainer container, Class adapterType, ID adapterID);

	public abstract Class[] getAdapterList();

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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5443.java