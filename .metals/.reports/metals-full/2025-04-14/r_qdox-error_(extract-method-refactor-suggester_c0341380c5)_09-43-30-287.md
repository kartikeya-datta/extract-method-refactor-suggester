error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9402.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9402.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9402.java
text:
```scala
@version $@@Revision: 1.3 $

/*
* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package org.jboss.logging.log4j;

import org.apache.log4j.Priority;

/** Adds a trace priority that is below the standard log4j DEBUG priority.
 This is a custom priority that is 100 below the Priority.DEBUG_INT and
 represents a lower priority useful for logging events that should only
 be displayed when deep debugging is required.
 
 @see org.apache.log4j.Category
 @see org.apache.log4j.Priority
 
 @author Scott.Stark@jboss.org
 @version $Revision: 1.2 $
 */
public class TracePriority extends Priority
{
  // Constants -----------------------------------------------------
   /** The integer representation of the priority, (Priority.DEBUG_INT - 100) */
   public static final int TRACE_INT = Priority.DEBUG_INT - 100;
   /** The TRACE priority object singleton */
   public static final TracePriority TRACE = new TracePriority(TRACE_INT, "TRACE");
  
  // Attributes ----------------------------------------------------

  // Static --------------------------------------------------------
   /** Convert an integer passed as argument to a priority. If the conversion
    fails, then this method returns the specified default.
    @return the Priority object for name if one exists, defaultPriority otherwize.
    */
   public static Priority toPriority(String name, Priority defaultPriority)
   {
      if( name == null )
         return TRACE;
      
      Priority p = TRACE;
      if( name.charAt(0) != 'T' )
         p = Priority.toPriority(name, defaultPriority);
      return p;
   }
   /** Convert an integer passed as argument to a priority. If the conversion
    fails, then this method returns the specified default.
    @return the Priority object for i if one exists, defaultPriority otherwize.
    */
   public static Priority toPriority(int i, Priority defaultPriority)
   {
      Priority p;
      if( i == TRACE_INT )
         p = TRACE;
      else
         p = Priority.toPriority(i);
      return p;
   }

  // Constructors --------------------------------------------------
   protected TracePriority(int level, String strLevel)
   {
      super(level, strLevel, 7);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9402.java