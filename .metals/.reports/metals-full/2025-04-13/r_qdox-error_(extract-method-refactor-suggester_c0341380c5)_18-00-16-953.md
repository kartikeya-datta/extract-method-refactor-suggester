error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6264.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6264.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6264.java
text:
```scala
e@@xtends ContainerPlugin

/*
 * JBoss, the OpenSource EJB server
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ejb;

import java.util.Collection;

import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;

/**
 *	This is an extension to the ContainerInvoker interface. Although some
 *      implementations of the ContainerInvoker interface may provide access
 *      to local interfaces, others (e.g. which provide remote distribution)
 *      will not. Good example: the JRMP delegates do not need to implement
 *      this interface.
 *
 *	@see ContainerInvoker
 *	@author Daniel OConnor (docodan@mvcsoft.com)
 */
public interface LocalContainerInvoker
   extends ContainerInvoker
{
   // Public --------------------------------------------------------

	/**
	 *	This method is called whenever the EJBLocalHome implementation for this
	 *	container is needed.
	 *
	 * @return     an implementation of the local home interface for this container
	 */
   public EJBLocalHome getEJBLocalHome();

        /**
	 *	This method is called whenever an EJBLocalObject implementation for a stateless
	 *	session bean is needed.
	 *
	 * @return     an implementation of the local interface for this container
	 */
   public EJBLocalObject getStatelessSessionEJBLocalObject();

	/**
	 *	This method is called whenever an EJBLocalObject implementation for a stateful
	 *	session bean is needed.
	 *
	 * @param   id  the id of the session
	 * @return     an implementation of the local interface for this container
	 */
   public EJBLocalObject getStatefulSessionEJBLocalObject(Object id);
      
	/**
	 *	This method is called whenever an EJBLocalObject implementation for an entitybean
	 * is needed.
	 *
	 * @param   id  the primary key of the entity
	 * @return     an implementation of the local interface for this container
	 */
   public EJBLocalObject getEntityEJBLocalObject(Object id);
	/**
	 *	This method is called whenever a collection of EJBLocalObjects for a collection of primary keys
	 * is needed.
	 *
	 * @param   enum  enumeration of primary keys
	 * @return     a collection of EJBLocalObjects implementing the remote interface for this container
	 */
   public Collection getEntityLocalCollection(Collection enum);
   
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6264.java