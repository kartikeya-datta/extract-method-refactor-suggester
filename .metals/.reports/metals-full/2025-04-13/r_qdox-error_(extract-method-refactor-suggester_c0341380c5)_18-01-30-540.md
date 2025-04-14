error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9255.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9255.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9255.java
text:
```scala
C@@lass<?> ifc = this.rmiExporter.getServiceInterface();

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.util.Assert;

/**
 * Server-side implementation of {@link RmiInvocationHandler}. An instance
 * of this class exists for each remote object. Automatically created
 * by {@link RmiServiceExporter} for non-RMI service implementations.
 *
 * <p>This is an SPI class, not to be used directly by applications.
 *
 * @author Juergen Hoeller
 * @since 14.05.2003
 * @see RmiServiceExporter
 */
class RmiInvocationWrapper implements RmiInvocationHandler {

	private final Object wrappedObject;

	private final RmiBasedExporter rmiExporter;


	/**
	 * Create a new RmiInvocationWrapper for the given object
	 * @param wrappedObject the object to wrap with an RmiInvocationHandler
	 * @param rmiExporter the RMI exporter to handle the actual invocation
	 */
	public RmiInvocationWrapper(Object wrappedObject, RmiBasedExporter rmiExporter) {
		Assert.notNull(wrappedObject, "Object to wrap is required");
		Assert.notNull(rmiExporter, "RMI exporter is required");
		this.wrappedObject = wrappedObject;
		this.rmiExporter = rmiExporter;
	}


	/**
	 * Exposes the exporter's service interface, if any, as target interface.
	 * @see RmiBasedExporter#getServiceInterface()
	 */
	@Override
	public String getTargetInterfaceName() {
		Class ifc = this.rmiExporter.getServiceInterface();
		return (ifc != null ? ifc.getName() : null);
	}

	/**
	 * Delegates the actual invocation handling to the RMI exporter.
	 * @see RmiBasedExporter#invoke(org.springframework.remoting.support.RemoteInvocation, Object)
	 */
	@Override
	public Object invoke(RemoteInvocation invocation)
		throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		return this.rmiExporter.invoke(invocation, this.wrappedObject);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9255.java