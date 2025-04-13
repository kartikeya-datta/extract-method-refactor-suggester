error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7475.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7475.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7475.java
text:
```scala
@version $@@Revision: 1.4 $

/*
* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package org.jboss.logging.log4j;

import org.apache.log4j.Category;
import org.apache.log4j.spi.CategoryFactory;

/** A custom log4j category that adds support for trace() level logging.
@see #trace(String)
@see #trace(String, Throwable)
@see TracePriority

@author Scott.Stark@jboss.org
@version $Revision: 1.3 $
*/
public class JBossCategory extends Category
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------
   private static CategoryFactory factory = new JBossCategoryFactory();

   // Static --------------------------------------------------------
   /** This method overrides {@link Category#getInstance} by supplying
   its own factory type as a parameter.
   */
   public static Category getInstance(String name)
   {
      return Category.getInstance(name, factory); 
   }
   /** This method overrides {@link Category#getInstance} by supplying
   its own factory type as a parameter.
   */
   public static Category getInstance(Class clazz)
   {
      return Category.getInstance(clazz.getName(), factory); 
   }

  // Constructors --------------------------------------------------
   /** Creates new JBossCategory with the given category name.
    @param name, the category name.
   */
   public JBossCategory(String name)
   {
      super(name);
   }

   /** Check to see if the TRACE priority is enabled for this category.
   @return true if a {@link #trace(String)} method invocation would pass
   the msg to the configured appenders, false otherwise.
   */
   public boolean isTraceEnabled()
   {
      if( hierarchy.isDisabled(TracePriority.TRACE_INT) )
         return false;
      return TracePriority.TRACE.isGreaterOrEqual(this.getChainedPriority());
   }

   /** Issue a log msg with a priority of TRACE.
   Invokes super.log(TracePriority.TRACE, message);
   */
   public void trace(Object message)
   {
      super.log(TracePriority.TRACE, message);
   }
   /** Issue a log msg and throwable with a priority of TRACE.
   Invokes super.log(TracePriority.TRACE, message, t);
   */
   public void trace(Object message, Throwable t)
   {
      super.log(TracePriority.TRACE, message, t);
   }

   // Inner classes -------------------------------------------------
   /** The CategoryFactory implementation for the custom JBossCategory.
   */
   public static class JBossCategoryFactory implements CategoryFactory
   {
      public Category makeNewCategoryInstance(String name)
      {
         return new JBossCategory(name);
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7475.java