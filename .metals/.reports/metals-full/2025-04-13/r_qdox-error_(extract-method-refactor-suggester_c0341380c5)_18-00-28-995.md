error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2313.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2313.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 574
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2313.java
text:
```scala
public final class Messages extends NLS {

/****************************************************************************
 * Copyright (c) 2007 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

p@@ackage org.eclipse.ecf.internal.provider.jmdns;

import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ecf.internal.provider.jmdns.messages"; //$NON-NLS-1$
	public static String ContainerInstantiator_EXCEPTION_CONTAINER_CREATE;
	public static String ContainerInstantiator_EXCEPTION_GETTING_INETADDRESS;
	public static String ECFStart_WARNING_COULD_NOT_REGISTER_DISCOVERY;
	public static String JMDNSDiscoveryContainer_EXCEPTION_ALREADY_CONNECTED;
	public static String JMDNSDiscoveryContainer_EXCEPTION_CONTAINER_DISPOSED;
	public static String JMDNSDiscoveryContainer_EXCEPTION_CREATE_JMDNS_INSTANCE;
	public static String JMDNSDiscoveryContainer_DISCOVERY_NOT_INITIALIZED;
	public static String JMDNSDiscoveryContainer_EXCEPTION_REGISTER_SERVICE;
	public static String JMDNSDiscoveryContainer_EXCEPTION_SERVICEINFO_INVALID;
	public static String JMDNSDiscoveryContainer_JMDNS_LOCAL_SUFFIX;
	public static String JMDNSDiscoveryContainer_NO_JMDNS_SERVICE_TYPE;
	public static String JMDNSDiscoveryContainer_NO_SERVICE_TYPE;
	public static String JMDNSDiscoveryContainer_SERVICE_NAME_NOT_NULL;
	public static String JMDNSNamespace_EXCEPTION_ID_CREATE_SERVICE_TYPE_CANNOT_BE_EMPTY;
	public static String JMDNSNamespace_EXCEPTION_ID_PARAM_2_WRONG_TYPE;
	public static String JMDNSNamespace_EXCEPTION_ID_WRONG_PARAM_COUNT;
	public static String JMDNSServiceTypeID_EXCEPTION_INVALID_TYPE_ID;
	public static String JMDNSNamespace_EXCEPTION_TYPE_PARAM_NOT_STRING;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
		//
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2313.java