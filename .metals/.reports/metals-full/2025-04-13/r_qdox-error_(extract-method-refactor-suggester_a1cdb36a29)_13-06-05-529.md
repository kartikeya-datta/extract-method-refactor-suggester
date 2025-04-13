error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,13]

error in qdox parser
file content:
```java
offset: 13
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2390.java
text:
```scala
@deprecated A@@s of 1.2.12, use {@link #getNext} and {@link #setNext} instead

/*
 * Copyright 1999,2004 The Apache Software Foundation.
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

package org.apache.log4j.spi;

import org.apache.log4j.spi.LoggingEvent;


/**
   Users should extend this class to implement customized logging
   event filtering. Note that {@link org.apache.log4j.Category} and {@link
   org.apache.log4j.AppenderSkeleton}, the parent class of all standard
   appenders, have built-in filtering rules. It is suggested that you
   first use and understand the built-in rules before rushing to write
   your own custom filters.

   <p>This abstract class assumes and also imposes that filters be
   organized in a linear chain. The {@link #decide
   decide(LoggingEvent)} method of each filter is called sequentially,
   in the order of their addition to the chain.

   <p>The {@link #decide decide(LoggingEvent)} method must return one
   of the integer constants {@link #DENY}, {@link #NEUTRAL} or {@link
   #ACCEPT}.

   <p>If the value {@link #DENY} is returned, then the log event is
   dropped immediately without consulting with the remaining
   filters.

   <p>If the value {@link #NEUTRAL} is returned, then the next filter
   in the chain is consulted. If there are no more filters in the
   chain, then the log event is logged. Thus, in the presence of no
   filters, the default behaviour is to log all logging events.

   <p>If the value {@link #ACCEPT} is returned, then the log
   event is logged without consulting the remaining filters.

   <p>The philosophy of log4j filters is largely inspired from the
   Linux ipchains.

   <p>Note that filtering is only supported by the {@link
   org.apache.log4j.xml.DOMConfigurator DOMConfigurator}. The {@link
   org.apache.log4j.PropertyConfigurator PropertyConfigurator} does not
   support filters.

   @author Ceki G&uuml;lc&uuml;
   @since 0.9.0 */
public abstract class Filter extends ComponentBase implements OptionHandler {
  /**
     The log event must be dropped immediately without consulting
     with the remaining filters, if any, in the chain.  */
  public static final int DENY = -1;

  /**
     This filter is neutral with respect to the log event. The
     remaining filters, if any, should be consulted for a final decision.
  */
  public static final int NEUTRAL = 0;

  /**
     The log event must be logged immediately without consulting with
     the remaining filters, if any, in the chain.  */
  public static final int ACCEPT = 1;

  /**
     Points to the next filter in the filter chain.
     
     @deprecated As of 1.2.11, use {@link #getNext} and {@link #setNext} instead
   */
  public Filter next;

  /**
     Usually filters options become active when set. We provide a
     default do-nothing implementation for convenience.
  */
  public void activateOptions() {
  }

  /**
     <p>If the decision is <code>DENY</code>, then the event will be
     dropped. If the decision is <code>NEUTRAL</code>, then the next
     filter, if any, will be invoked. If the decision is ACCEPT then
     the event will be logged without consulting with other filters in
     the chain.

     @param event The LoggingEvent to decide upon.
   */
  public abstract int decide(LoggingEvent event);
 
  /**
   * Set the next filter pointer.
   */ 
  public void setNext(Filter next) {
    this.next = next;
  }
 
  /**
   * Return the pointer to the next filter;
   */ 
  public Filter getNext() {
        return next;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2390.java