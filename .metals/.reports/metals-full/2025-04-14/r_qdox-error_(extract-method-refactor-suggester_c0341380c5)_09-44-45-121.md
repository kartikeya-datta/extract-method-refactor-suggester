error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14381.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14381.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14381.java
text:
```scala
l@@og.debugf("MessagingSubsystemElement.readElement, event=%s", reader.getEventType());

package org.jboss.as.messaging;

import org.jboss.as.model.ParseResult;
import org.jboss.logging.Logger;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLExtendedStreamReader;

import javax.xml.stream.XMLStreamException;

/**
 * The messaging subsystem domain parser
 * @author scott.stark@jboss.org
 * @version $Revision:$
 */
public class MessagingSubsystemParser implements XMLElementReader<ParseResult<? super MessagingSubsystemElement>> {
   /** A thread local for storing the last MessagingSubsystemElement parsed for testing */
   private static final ThreadLocal<MessagingSubsystemElement> LAST_ELEMENT = new ThreadLocal<MessagingSubsystemElement>();
   private static boolean useThreadLocal = false;
   private static final Logger log = Logger.getLogger("org.jboss.as.messaging");

   private MessagingSubsystemParser() {
   }

   private static final MessagingSubsystemParser INSTANCE = new MessagingSubsystemParser();

   /**
    * Get the instance.
    *
    * @return the instance
    */
   public static MessagingSubsystemParser getInstance() {
      return INSTANCE;
   }

   /**
    * For testing only.
    */
   public static void enableThreadLocal(boolean flag) {
      useThreadLocal = flag;
   }
   public static MessagingSubsystemElement getLastSubsystemElement() {
      return LAST_ELEMENT.get();
   }
   public static void clearLastSubsystemElement() {
      LAST_ELEMENT.remove();
   }

   /**
    * {@inheritDoc}
    */
   public void readElement(final XMLExtendedStreamReader reader, final ParseResult<? super MessagingSubsystemElement> result) throws XMLStreamException {
      log.debug("MessagingSubsystemElement.readElement, event="+reader.getEventType());
      MessagingSubsystemElement msp = new MessagingSubsystemElement(reader);
      if(useThreadLocal)
         LAST_ELEMENT.set(msp);
      result.setResult(msp);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14381.java