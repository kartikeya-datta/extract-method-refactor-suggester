error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8050.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8050.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8050.java
text:
```scala
@version $@@Revision: 1.7 $

/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.security;

import java.lang.reflect.Method;
import javax.ejb.EJBContext;

/** An interface describing the requirements for a SecurityInterceptor proxy.
A SecurityProxy allows for the externalization of custom security checks 
on a per-method basis for both the EJB home and remote interface methods.
Custom security checks are those that cannot be described using the
standard EJB deployment time declarative role based security.

@author Scott.Stark@jboss.org
@version $Revision: 1.6 $
*/
public interface SecurityProxy
{
   /** Inform a proxy of the context in which it is operating.
    * @param beanHome The EJB remote home interface class
    * @param beanRemote The EJB remote interface class
    * @param securityMgr The security manager from the security domain
    * @throws InstantiationException
    */
   public void init(Class beanHome, Class beanRemote, Object securityMgr)
      throws InstantiationException;
   /** Inform a proxy of the context in which it is operating.
    * @param beanHome The EJB remote home interface class
    * @param beanRemote The EJB remote interface class
    * @param beanLocalHome The EJB local home interface class, may be null
    * @param beanLocal The EJB local interface class, may be null
    * @param securityMgr The security manager from the security domain
    * @throws InstantiationException
    */
   public void init(Class beanHome, Class beanRemote,
      Class beanLocalHome, Class beanLocal, Object securityMgr)
      throws InstantiationException;
    /** Called prior to any method invocation to set the current EJB context.
    */
    public void setEJBContext(EJBContext ctx);
    /** Called to allow the security proxy to perform any custom security
        checks required for the EJB remote or local home interface method.
    @param m , the EJB home or local home interface method
    @param args , the invocation args
    */
    public void invokeHome(Method m, Object[] args) throws Exception;
    /** Called to allow the security proxy to perform any custom security
        checks required for the EJB remote or local interface method.
    @param m , the EJB remote or local interface method
    @param args , the invocation args
    @param bean, the EJB implementation class instance
    */
    public void invoke(Method m, Object[] args, Object bean) throws Exception;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8050.java