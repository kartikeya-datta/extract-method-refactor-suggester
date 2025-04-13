error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8381.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8381.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[161,2]

error in qdox parser
file content:
```java
offset: 6113
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8381.java
text:
```scala
package org.eclipse.ecf.core.sharedobject;

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core;

import java.util.List;

/**
 * Container factory contract {@link SharedObjectFactory} for default implementation.
 */
public interface ISharedObjectFactory {
	/*
	 * Add a SharedObjectTypeDescription to the set of known SharedObjectTypeDescriptions.
	 * 
	 * @param scd the SharedObjectTypeDescription to add to this factory @return
	 * SharedObjectTypeDescription the old description of the same name, null if none
	 * found
	 */
	public SharedObjectTypeDescription addDescription(SharedObjectTypeDescription description);

	/**
	 * Get a collection of the SharedObjectTypeDescriptions currently known to this
	 * factory. This allows clients to query the factory to determine what if
	 * any other SharedObjectTypeDescriptions are currently registered with the
	 * factory, and if so, what they are.
	 * 
	 * @return List of SharedObjectTypeDescription instances
	 */
	public List getDescriptions();

	/**
	 * Check to see if a given named description is already contained by this
	 * factory
	 * 
	 * @param description
	 *            the SharedObjectTypeDescription to look for
	 * @return true if description is already known to factory, false otherwise
	 */
	public boolean containsDescription(SharedObjectTypeDescription description);

	/**
	 * Get the known SharedObjectTypeDescription given it's name.
	 * 
	 * @param name
	 * @return SharedObjectTypeDescription found
	 * @throws SharedObjectCreateException
	 */
	public SharedObjectTypeDescription getDescriptionByName(String name)
			throws SharedObjectCreateException;

	/**
	 * Create ISharedObject instance. Given a SharedObjectTypeDescription object, a String []
	 * of argument types, and an Object [] of parameters, this method will
	 * <p>
	 * <ul>
	 * <li>lookup the known SharedObjectTypeDescriptions to find one of matching name</li>
	 * <li>if found, will retrieve or create an ISharedObjectInstantiator for that
	 * description</li>
	 * <li>Call the ISharedObjectInstantiator.createInstance method to return an
	 * instance of ISharedObject</li>
	 * </ul>
	 * 
	 * @param typeDescription
	 *            the SharedObjectTypeDescription to use to create the instance
	 * @param argTypes
	 *            a String [] defining the types of the args parameter
	 * @param args
	 *            an Object [] of arguments passed to the createInstance method of
	 *            the ISharedObjectInstantiator
	 * @return a valid instance of ISharedObject
	 * @throws SharedObjectCreateException
	 */
	public ISharedObject createSharedObject(SharedObjectTypeDescription typeDescription,
			String[] argTypes, Object[] args)
			throws SharedObjectCreateException;

	/**
	 * Create ISharedObject instance. Given a SharedObjectTypeDescription name, this method
	 * will
	 * <p>
	 * <ul>
	 * <li>lookup the known SharedObjectTypeDescriptions to find one of matching name</li>
	 * <li>if found, will retrieve or create an ISharedObjectInstantiator for that
	 * description</li>
	 * <li>Call the ISharedObjectInstantiator.createInstance method to return an
	 * instance of ISharedObject</li>
	 * </ul>
	 * 
	 * @param descriptionName
	 *            the SharedObjectTypeDescription name to lookup
	 * @return a valid instance of ISharedObject
	 * @throws SharedObjectCreateException
	 */
	public ISharedObject createSharedObject(String descriptionName)
			throws SharedObjectCreateException;

	/**
	 * Create ISharedObject instance. Given a SharedObjectTypeDescription name, this method
	 * will
	 * <p>
	 * <ul>
	 * <li>lookup the known SharedObjectTypeDescriptions to find one of matching name</li>
	 * <li>if found, will retrieve or create an ISharedObjectInstantiator for that
	 * description</li>
	 * <li>Call the ISharedObjectInstantiator.createInstance method to return an
	 * instance of ISharedObject</li>
	 * </ul>
	 * 
	 * @param descriptionName
	 *            the SharedObjectTypeDescription name to lookup
	 * @param args
	 *            the Object [] of arguments passed to the
	 *            ISharedObjectInstantiator.createInstance method
	 * @return a valid instance of IContainer
	 * @throws SharedObjectCreateException 
	 */
	public ISharedObject createSharedObject(String descriptionName, Object[] args)
			throws SharedObjectCreateException;

	/**
	 * Create ISharedObject instance. Given a SharedObjectTypeDescription name, this method
	 * will
	 * <p>
	 * <ul>
	 * <li>lookup the known SharedObjectTypeDescriptions to find one of matching name</li>
	 * <li>if found, will retrieve or create an ISharedObjectInstantiator for that
	 * description</li>
	 * <li>Call the ISharedObjectInstantiator.createInstance method to return an
	 * instance of ISharedObject</li>
	 * </ul>
	 * 
	 * @param descriptionName
	 *            the SharedObjectTypeDescription name to lookup
	 * @param argsTypes
	 *            the String [] of argument types of the following args
	 * @param args
	 *            the Object [] of arguments passed to the
	 *            ISharedObjectInstantiator.createInstance method
	 * @return a valid instance of ISharedObject
	 * @throws SharedObjectCreateException
	 */
	public ISharedObject createSharedObject(String descriptionName, String[] argsTypes,
			Object[] args) throws SharedObjectCreateException;

	/**
	 * Remove given description from set known to this factory.
	 * 
	 * @param scd
	 *            the SharedObjectTypeDescription to remove
	 * @return the removed SharedObjectTypeDescription, null if nothing removed
	 */
	public SharedObjectTypeDescription removeDescription(SharedObjectTypeDescription scd);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8381.java