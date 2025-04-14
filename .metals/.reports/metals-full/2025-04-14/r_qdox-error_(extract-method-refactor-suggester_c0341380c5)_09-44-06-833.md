error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18266.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18266.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18266.java
text:
```scala
public static final S@@tring DEFAULT_SERVICE_NAME_PREFIX = "svc_"; //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.services.discovery;

public interface RemoteServicePublication extends ServicePublication {

	/**
	 * Discovery OSGi Service Type for publishing/discovering osgiservices
	 */
	public static final String SERVICE_TYPE = "osgiservices"; //$NON-NLS-1$
	/**
	 * Prefix for the default service name. The default service name will be the
	 * DEFAULT_SERVICE_NAME_PREFIX+serviceID (long)
	 */
	public static final String DEFAULT_SERVICE_NAME_PREFIX = "service "; //$NON-NLS-1$

	// Discovery service properties

	/**
	 * Discovery service name property. If the specified service property is
	 * non-null, the service name will override the default value, which is the
	 * DEFAULT_SERVICE_NAME_PREFIX+serviceID as described above. The value
	 * provided for this property must be of type String.
	 */
	public static final String SERVICE_NAME = "ecf.sp.svcname"; //$NON-NLS-1$
	/**
	 * Discovery naming authority property. If the specified serviceproperty is
	 * non-null, the discovery naming authority will override the default value,
	 * which is IServiceID#DEFAULT_NA ("iana"). The value provided for this
	 * property must be of type String.
	 */
	public static final String NAMING_AUTHORITY = "ecf.sp.namingauth"; //$NON-NLS-1$
	/**
	 * Discovery scope property. If the specified service property is non-null,
	 * the discovery scope will override the default value, which is
	 * IServiceID#DEFAULT_SCOPE ("default"). The value provided for this
	 * property must be of type String, which can be split into a String[] using
	 * ';' as the delimiter (e.g. 'scope1;scope2;scope3').
	 */
	public static final String SCOPE = "ecf.sp.scope"; //$NON-NLS-1$
	/**
	 * Discovery protocol property. If the specified service property is
	 * non-null, the discovery protocol will override the default value, which
	 * is IServiceID#DEFAULT_PROTO ("tcp"). The value provided for this property
	 * must be of type String, which can be split into a String[] using ';' as
	 * the delimiter (e.g. 'proto1;proto1;proto1').
	 */
	public static final String SERVICE_PROTOCOL = "ecf.sp.protocol"; //$NON-NLS-1$
	/**
	 * Endpoint container ID property. The value for this property must be of
	 * type byte[] which is a UTF-8 encoding of a String.
	 */
	public static final String ENDPOINT_CONTAINERID = "ecf.sp.cid"; //$NON-NLS-1$
	/**
	 * Endpoint container ID namespace property. The value provided for this
	 * property must be of type String.
	 */
	public static final String ENDPOINT_CONTAINERID_NAMESPACE = "ecf.sp.cns"; //$NON-NLS-1$

	public static final String ENDPOINT_SUPPORTED_CONFIGS = "ecf.sp.ect"; //$NON-NLS-1$

	public static final String ENDPOINT_SERVICE_INTENTS = "ecf.sp.esi"; //$NON-NLS-1$

	/**
	 * Target container ID property. The value for this property must be of type
	 * byte[] which is a UTF-8 encoding of a String.
	 */
	public static final String TARGET_CONTAINERID = "ecf.sp.tid"; //$NON-NLS-1$
	/**
	 * Target container ID namespace property. The value provided for this
	 * property must be of type String.
	 */
	public static final String TARGET_CONTAINERID_NAMESPACE = "ecf.sp.tns"; //$NON-NLS-1$
	/**
	 * Client remote service filter property. The value provided for this
	 * property must be of type String.
	 */
	public static final String REMOTE_SERVICE_FILTER = "ecf.client.filter"; //$NON-NLS-1$

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18266.java