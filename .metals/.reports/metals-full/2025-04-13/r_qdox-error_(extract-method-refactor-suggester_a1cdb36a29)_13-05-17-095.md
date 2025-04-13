error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8066.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8066.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8066.java
text:
```scala
L@@ist getContextBindings();

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.commands;

import java.util.List;

/**
 * <p>
 * An instance of <code>ICommand</code> is a handle representing a command as
 * defined by the extension point <code>org.eclipse.ui.commands</code>. The
 * identifier of the handle is identifier of the command being represented.
 * </p>
 * <p>
 * An instance of <code>ICommand</code> can be obtained from an instance of
 * <code>ICommandManager</code> for any identifier, whether or not a command
 * with that identifier defined in the plugin registry.
 * </p>
 * <p>
 * The handle-based nature of this API allows it to work well with runtime
 * plugin activation and deactivation, which causes dynamic changes to the
 * plugin registry, and therefore, potentially, dynamic changes to the set of
 * command definitions.
 * </p>
 * <p>
 * This interface is not intended to be extended or implemented by clients.
 * </p>
 * <p>
 * <em>EXPERIMENTAL</em>
 * </p>
 * 
 * @since 3.0
 * @see ICommandListener
 * @see ICommandManager
 * @see IKeySequenceBinding
 */
public interface ICommand extends Comparable {

	/**
	 * Registers an instance of <code>ICommandListener</code> to listen for
	 * changes to attributes of this instance.
	 * 
	 * @param commandListener
	 *            the instance of <code>ICommandListener</code> to register.
	 *            Must not be <code>null</code>. If an attempt is made to
	 *            register an instance of <code>ICommandListener</code> which
	 *            is already registered with this instance, no operation is
	 *            performed.
	 */
	void addCommandListener(ICommandListener commandListener);

	/**
	 * <p>
	 * Returns the list of activity bindings for this handle. This method will
	 * return all activity bindings for this handle's identifier, whether or
	 * not the command represented by this handle is defined.
	 * </p>
	 * <p>
	 * Notification is sent to all registered listeners if this attribute
	 * changes.
	 * </p>
	 * 
	 * @return the list of activity bindings. This list may be empty, but is
	 *         guaranteed not to be <code>null</code>. If this list is not
	 *         empty, it is guaranteed to only contain instances of <code>IActivityBinding</code>.
	 */
	List getActivityBindings();

	/**
	 * <p>
	 * Returns the identifier of the category of the command represented by
	 * this handle.
	 * </p>
	 * <p>
	 * Notification is sent to all registered listeners if this attribute
	 * changes.
	 * </p>
	 * 
	 * @return the identifier of the category of the command represented by
	 *         this handle. May be <code>null</code>.
	 * @throws NotDefinedException
	 *             if the command represented by this handle is not defined.
	 */
	String getCategoryId() throws NotDefinedException;

	/**
	 * <p>
	 * Returns the description of the command represented by this handle,
	 * suitable for display to the user.
	 * </p>
	 * <p>
	 * Notification is sent to all registered listeners if this attribute
	 * changes.
	 * </p>
	 * 
	 * @return the description of the command represented by this handle.
	 *         Guaranteed not to be <code>null</code>.
	 * @throws NotDefinedException
	 *             if the command represented by this handle is not defined.
	 */
	String getDescription() throws NotDefinedException;

	/**
	 * Returns the identifier of this handle.
	 * 
	 * @return the identifier of this handle. Guaranteed not to be <code>null</code>.
	 */
	String getId();

	/**
	 * <p>
	 * Returns the list of image bindings for this handle. This method will
	 * return all image bindings for this handle's identifier, whether or not
	 * the command represented by this handle is defined.
	 * </p>
	 * <p>
	 * Notification is sent to all registered listeners if this attribute
	 * changes.
	 * </p>
	 * 
	 * @return the list of image bindings. This list may be empty, but is
	 *         guaranteed not to be <code>null</code>. If this list is not
	 *         empty, it is guaranteed to only contain instances of <code>IImageBinding</code>.
	 */
	List getImageBindings();

	/**
	 * <p>
	 * Returns the list of key sequence bindings for this handle. This method
	 * will return all key sequence bindings for this handle's identifier,
	 * whether or not the command represented by this handle is defined.
	 * </p>
	 * <p>
	 * Notification is sent to all registered listeners if this attribute
	 * changes.
	 * </p>
	 * 
	 * @return the list of key sequence bindings. This list may be empty, but
	 *         is guaranteed not to be <code>null</code>. If this list is
	 *         not empty, it is guaranteed to only contain instances of <code>IKeySequenceBinding</code>.
	 */
	List getKeySequenceBindings();

	/**
	 * <p>
	 * Returns the name of the command represented by this handle, suitable for
	 * display to the user.
	 * </p>
	 * <p>
	 * Notification is sent to all registered listeners if this attribute
	 * changes.
	 * </p>
	 * 
	 * @return the name of the command represented by this handle. Guaranteed
	 *         not to be <code>null</code>.
	 * @throws NotDefinedException
	 *             if the command represented by this handle is not defined.
	 */
	String getName() throws NotDefinedException;

	/**
	 * <p>
	 * Returns whether or not this command is active. Instances of <code>ICommand</code>
	 * are activated and deactivated by the instance of <code>ICommandManager</code>
	 * from whence they were brokered.
	 * </p>
	 * <p>
	 * Notification is sent to all registered listeners if this attribute
	 * changes.
	 * </p>
	 * 
	 * @return <code>true</code>, iff this command is active.
	 */
	boolean isActive();

	/**
	 * <p>
	 * Returns whether or not the command represented by this handle is
	 * defined.
	 * </p>
	 * <p>
	 * Notification is sent to all registered listeners if this attribute
	 * changes.
	 * </p>
	 * 
	 * @return <code>true</code>, iff the command represented by this handle
	 *         is defined.
	 */
	boolean isDefined();

	/**
	 * <p>
	 * Returns whether or not this command is enabled. Instances of <code>ICommand</code>
	 * are enabled and disabled by the instance of <code>ICommandManager</code>
	 * from whence they were brokered.
	 * </p>
	 * <p>
	 * Notification is sent to all registered listeners if this attribute
	 * changes.
	 * </p>
	 * 
	 * @return <code>true</code>, iff this command is enabled.
	 */
	boolean isEnabled();

	/**
	 * Unregisters an instance of <code>ICommandListener</code> listening for
	 * changes to attributes of this instance.
	 * 
	 * @param commandListener
	 *            the instance of <code>ICommandListener</code> to
	 *            unregister. Must not be <code>null</code>. If an attempt
	 *            is made to unregister an instance of <code>ICommandListener</code>
	 *            which is not already registered with this instance, no
	 *            operation is performed.
	 */
	void removeCommandListener(ICommandListener commandListener);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8066.java