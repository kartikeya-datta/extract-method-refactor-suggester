error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[12,1]

error in qdox parser
file content:
```java
offset: 564
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16057.java
text:
```scala
public interface IServiceConstants {

/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
p@@ackage org.eclipse.ecf.osgi.services.distribution;

public interface ECFServiceConstants {

	/*
	 * service.intents  an optional list of intents provided by the service.
	 * The property advertises capabilities of the service and can be used by
	 * the service consumer in the lookup filter to only select a service that
	 * provides certain qualities of service. The value of this property is of
	 * type String[] and has to be provided by the service as part of the
	 * registration, regardless whether its a local service or a proxy. The
	 * value on the proxy is a union of the value specified by the service
	 * provider, plus any remote-specific intents (see
	 * orgi.remote.require.intents, below), plus any intents which the
	 * Distribution Software adds that describe characteristics of the
	 * Distribution being mechanism. Therefore the value of this property can
	 * vary between the client side proxy and the server side.
	 */
	public static final String SERVICE_INTENTS = "service.intents";

	/*
	 * osgi.remote.interfaces  [ * | interface_name [, interface_name]* ]: A
	 * distribution software implementation may expose a service for remote
	 * access, if and only if the service has indicated its intention as well as
	 * support for remote invocations by setting this service property in its
	 * service registration. The value of this property is of type String[]. If
	 * the list contains only one value, which is set to *, all of the
	 * interfaces specified in the BundleContext.registerService() call are
	 * being exposed remotely. The value can also be set to a comma-separated
	 * list of interface names, which should be a subset of the interfaces
	 * specified in the registerService call. In this case only the specified
	 * interfaces are exposed remotely.
	 */
	public static final String OSGI_REMOTE_INTERFACES = "osgi.remote.interfaces";

	/*
	 * osgi.remote.requires.intents  an optional list of intents that should be
	 * provided when remotely exposing the service. If a DSW implementation
	 * cannot satisfy these intents when exposing the service remotely, it
	 * should not expose the service. The value of this property is of type
	 * String[].
	 */
	public static final String OSGI_REMOTE_REQUIRES_INTENTS = "osgi.remote.requires.intents";

	/*
	 * osgi.remote.configuration.type  service providing side property that
	 * identifies the metadata type of additional metadata, if any, that was
	 * provided with the service, e.g. sca. Multiple types and thus sets of
	 * additional metadata may be provided. The value of this property is of
	 * type String[].
	 */
	public static final String OSGI_REMOTE_CONFIGURATION_TYPE = "osgi.remote.configuration.type";

	/*
	 * osgi.remote  this property is set on client side service proxies
	 * registered in the OSGi Service Registry.
	 */
	public static final String OSGI_REMOTE = "osgi.remote";

	public static final String OSGI_REMOTE_INTERFACES_WILDCARD = "*";

	public static final String ECF_REMOTE_CONFIGURATION_TYPE = "ecf";

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16057.java