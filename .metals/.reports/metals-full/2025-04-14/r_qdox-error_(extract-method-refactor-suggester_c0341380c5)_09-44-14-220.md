error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7104.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7104.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7104.java
text:
```scala
.@@getClass().getClassLoader());

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.eclipse.ecf.core.provider.ISharedObjectInstantiator;
import org.eclipse.ecf.core.util.AbstractFactory;
import org.eclipse.ecf.internal.core.Trace;

/**
 * Factory for creating {@link ISharedObject} instances. This class provides ECF
 * clients an entry point to constructing {@link ISharedObject} instances. <br>
 */
public class SharedObjectFactory implements ISharedObjectFactory {
	private static Trace debug = Trace.create("containerfactory");
	private static Hashtable sharedobjectdescriptions = new Hashtable();
	protected static ISharedObjectFactory instance = null;
	static {
		instance = new SharedObjectFactory();
	}

	protected SharedObjectFactory() {
	}

	public static ISharedObjectFactory getDefault() {
		return instance;
	}

	private static void trace(String msg) {
		if (Trace.ON && debug != null) {
			debug.msg(msg);
		}
	}

	private static void dumpStack(String msg, Throwable e) {
		if (Trace.ON && debug != null) {
			debug.dumpStack(e, msg);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectFactory#addDescription(org.eclipse.ecf.core.SharedObjectDescription)
	 */
	public SharedObjectDescription addDescription(SharedObjectDescription description) {
		trace("addDescription(" + description + ")");
		return addDescription0(description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectFactory#getDescriptions()
	 */
	public List getDescriptions() {
		return getDescriptions0();
	}

	protected List getDescriptions0() {
		return new ArrayList(sharedobjectdescriptions.values());
	}

	protected SharedObjectDescription addDescription0(SharedObjectDescription n) {
		if (n == null)
			return null;
		return (SharedObjectDescription) sharedobjectdescriptions.put(n.getName(), n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectFactory#containsDescription(org.eclipse.ecf.core.SharedObjectDescription)
	 */
	public boolean containsDescription(SharedObjectDescription scd) {
		return containsDescription0(scd);
	}

	protected boolean containsDescription0(SharedObjectDescription scd) {
		if (scd == null)
			return false;
		return sharedobjectdescriptions.containsKey(scd.getName());
	}

	protected SharedObjectDescription getDescription0(SharedObjectDescription scd) {
		if (scd == null)
			return null;
		return (SharedObjectDescription) sharedobjectdescriptions.get(scd.getName());
	}

	protected SharedObjectDescription getDescription0(String name) {
		if (name == null)
			return null;
		return (SharedObjectDescription) sharedobjectdescriptions.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#getDescriptionByName(java.lang.String)
	 */
	public SharedObjectDescription getDescriptionByName(String name)
			throws SharedObjectInstantiationException {
		trace("getDescriptionByName(" + name + ")");
		SharedObjectDescription res = getDescription0(name);
		if (res == null) {
			throw new SharedObjectInstantiationException(
					"SharedObjectInstantiationException named '" + name + "' not found");
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#createSharedObject(org.eclipse.ecf.core.SharedObjectDescription,
	 *      java.lang.String[], java.lang.Object[])
	 */
	public ISharedObject createSharedObject(SharedObjectDescription desc,
			String[] argTypes, Object[] args)
			throws SharedObjectInstantiationException {
		trace("createSharedObject(" + desc + ","
				+ Trace.convertStringAToString(argTypes) + ","
				+ Trace.convertObjectAToString(args) + ")");
		if (desc == null)
			throw new SharedObjectInstantiationException(
					"SharedObjectDescription cannot be null");
		SharedObjectDescription cd = getDescription0(desc);
		if (cd == null)
			throw new SharedObjectInstantiationException(
					"SharedObjectDescription named '" + desc.getName()
							+ "' not found");
		Class clazzes[] = null;
		ISharedObjectInstantiator instantiator = null;
		try {
			instantiator = (ISharedObjectInstantiator) cd.getInstantiator();
			clazzes = AbstractFactory.getClassesForTypes(argTypes, args, cd
					.getClassLoader());
		} catch (Exception e) {
			SharedObjectInstantiationException newexcept = new SharedObjectInstantiationException(
					"createSharedObject exception with description: " + desc + ": "
							+ e.getClass().getName() + ": " + e.getMessage());
			newexcept.setStackTrace(e.getStackTrace());
			dumpStack("Exception in createSharedObject", newexcept);
			throw newexcept;
		}
		if (instantiator == null)
			throw new SharedObjectInstantiationException(
					"Instantiator for SharedObjectDescription " + cd.getName()
							+ " is null");
		// Ask instantiator to actually create instance
		return (ISharedObject) instantiator.createInstance(desc, clazzes, args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#createSharedObject(java.lang.String)
	 */
	public ISharedObject createSharedObject(String descriptionName)
			throws SharedObjectInstantiationException {
		return createSharedObject(getDescriptionByName(descriptionName), null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#createSharedObject(java.lang.String,
	 *      java.lang.Object[])
	 */
	public ISharedObject createSharedObject(String descriptionName, Object[] args)
			throws SharedObjectInstantiationException {
		return createSharedObject(getDescriptionByName(descriptionName), null, args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#createSharedObject(java.lang.String,
	 *      java.lang.String[], java.lang.Object[])
	 */
	public ISharedObject createSharedObject(String descriptionName, String[] argsTypes,
			Object[] args) throws SharedObjectInstantiationException {
		return createSharedObject(getDescriptionByName(descriptionName), argsTypes,
				args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#removeDescription(org.eclipse.ecf.core.SharedObjectDescription)
	 */
	public SharedObjectDescription removeDescription(SharedObjectDescription scd) {
		trace("removeDescription(" + scd + ")");
		return removeDescription0(scd);
	}

	protected SharedObjectDescription removeDescription0(SharedObjectDescription n) {
		if (n == null)
			return null;
		return (SharedObjectDescription) sharedobjectdescriptions.remove(n.getName());
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7104.java