error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3736.java
text:
```scala
D@@OMConfigurator.configure("xml/stressAsyncAppender.xml");

//  Copyright 2000 Ceki Gulcu.  All Rights Reserved.
//  See the LICENCE file for the terms of distribution.

package org.apache.log4j.test;


import org.apache.log4j.Category;
import org.apache.log4j.FileAppender;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.Priority;
import org.apache.log4j.NDC;


import java.util.Random;
import java.util.Stack;

/**
   Stress test {@link AsyncAppender}.
   
 */
public class StressAsyncAppender extends Thread {

  static Category root = Category.getRoot();  

  static Random random = new Random(101);

  static final int LOOP_LENGTH = 24;
  static final int BRANCHING_FACTOR = 4;
  
  static int maxThreads;  
  static long msgCounter = 0;
  static int threadCounter = 0;  

  static double LOG_2 = Math.log(2);

  static Object lock = new Object();

  
  public 
  static 
  void main(String args[]) {
    if(args.length != 1) {
      usage();
    }

    DOMConfigurator.configure("stressAsyncAppender.xml");

    try {
      maxThreads =  Integer.parseInt(args[0]);
    }
    catch(java.lang.NumberFormatException e) {
      System.err.println(e);
      usage();
    }
    
    while(true) {
      synchronized(lock) {
	// Adding 1 to ensure that at least 1 child is created. 	
	createChildren(randomInt(BRANCHING_FACTOR) + 1);

	// wait until all threads are finished
	try {
	  root.debug("About to wait for notification.");
	  lock.wait();
	  root.debug("Got a notification.");
	}
	catch(InterruptedException e) {
	  root.warn("Unpextected InterruptedException received.", e);
	}
      }
    }
  }


  static
  void usage() {
    System.err.println("Usage: java "+ StressAsyncAppender.class.getName() +
			" MAX_THREADS");
    System.exit(1);
  }


  public
  StressAsyncAppender() {
  }

  public
  void run() {
    int loopLength = StressAsyncAppender.randomInt(LOOP_LENGTH);
    root.debug("In run loop, loopLength = "+loopLength);

    // half of the way, create new childres
    int createIndex = loopLength/2;
      
    for(int i = 0; i <= loopLength; i++) {

      if(i == createIndex)
	createChildren(randomInt(BRANCHING_FACTOR));
      
      synchronized(lock) {      
	root.debug("Message number " + msgCounter++);	
      }
      //delay(1+randomInt(4)*100);
    }    

    synchronized(lock) {
      StressAsyncAppender.threadCounter--;
      root.debug("Exiting run loop. " + threadCounter);
      if(StressAsyncAppender.threadCounter <= 0) {
	root.debug("Notifying [main] thread.");
	lock.notify(); // wake up the main thread
      }
    }

  }

  public
  static
  void createChildren(int n) {
    if (n <= 0)
      return;

    synchronized(lock) {
      n = maxThreadsConstrained(n);    
      root.debug("Creating " + n+ " child StressAsyncAppender threads.");
      for(int i = 0; i < n; i++) {
	root.debug("New StressAsyncAppender, threadCounter = " + (++threadCounter));
	new StressAsyncAppender().start();
      }
    }
  }

  static
  public
  int maxThreadsConstrained(int a) {
    int maxAllowed = StressAsyncAppender.maxThreads - 
                                                 StressAsyncAppender.threadCounter;
    return a <= maxAllowed ? a : maxAllowed;
  }

  /**
     Return a random value in the range
   */
  public
  static
  int randomInt(int n) {
    int r = random.nextInt() % n;
    return r >= 0 ? r : -r;
  }

  
  public
  void delay(long millis) {
    try {
      Thread.currentThread().sleep(millis);
    } catch(Exception e) {}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3736.java