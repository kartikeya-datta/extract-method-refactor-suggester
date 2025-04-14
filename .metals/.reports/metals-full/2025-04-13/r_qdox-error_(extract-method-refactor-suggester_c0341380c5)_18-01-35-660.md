error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7244.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7244.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7244.java
text:
```scala
C@@ategory.getDefaultHierarchy().enable(Level.WARN);

//      Copyright 1996-2000, International Business Machines 
//      Corporation. All Rights Reserved.

package org.apache.log4j.performance;


import org.apache.log4j.Category;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;

import org.apache.log4j.Level;

/**
   Measure the performance of evaluating whether to log or not to log,
   but not actually logging.

   <p>This program takes two arguments, a string which should be
   "true" for testing shipped code performance and "false" for testing
   debug-enabled performance the second argument is the run length of
   the measurement loops.

   <p>The results of the measurement (should) show that

   <ul>

   <p>
   <li>Category evaluation is independent of the length of the category name.

   <p>
   <li>As expected, using the {@link Category#isDebugEnabled
   isDebugEnabled} and {@link Category#isInfoEnabled isInfoEnabled}
   methods eliminates the overhead of message argument construction.

   <p> <li>Message argument construction can be an important slowing
   factor while evaluating whether to log or not.

   </ul>

   @author Ceki G&uuml;lc&uuml;

*/
public class NotLogging {

  static int runLength;

  final static int INITIAL_HASH_SIZE = 101; 

  static String  SHORT_MSG = "Hello World";

  static Category SHORT_CAT = Category.getInstance("A0123456789");
  static Category MEDIUM_CAT= Category.getInstance("A0123456789.B0123456789");
  static Category LONG_CAT  = 
                   Category.getInstance("A0123456789.B0123456789.C0123456789");

  static Category INEXISTENT_SHORT_CAT = Category.getInstance("I0123456789");
  static Category INEXISTENT_MEDIUM_CAT=
                                Category.getInstance("I0123456789.B0123456789");
  static Category INEXISTENT_LONG_CAT= 
                     Category.getInstance("I0123456789.B0123456789.C0123456789");


  static Category[] CAT_ARRAY = new Category[] {SHORT_CAT, MEDIUM_CAT, 
						LONG_CAT, INEXISTENT_SHORT_CAT,
						INEXISTENT_MEDIUM_CAT,
						INEXISTENT_LONG_CAT};

  static
  void  Usage() {
    System.err.println(
      "Usage: java org.apache.log4j.test.NotLogging true|false runLength\n" +
      "true indicates shipped code, false indicates code in development" +
      "  where runLength is an int representing the run length of loops\n"+
      "We suggest that runLength be at least 100'000.");
    System.exit(1);
  }

  public static void main(String argv[]) {

    if(argv.length != 2) {
      Usage();
    }    
    ProgramInit(argv);
    double delta;

    
    System.out.println();
    for(int i = 0; i < CAT_ARRAY.length; i++) {
      delta = SimpleMessage(CAT_ARRAY[i], SHORT_MSG, runLength);
      System.out.println("Simple argument,          " + delta 
			 + " micros. Cat: " + CAT_ARRAY[i].getName());
    }

    System.out.println();
    for(int i = 0; i < CAT_ARRAY.length; i++) {
      delta = FullyOptimizedComplexMessage(CAT_ARRAY[i], runLength);
      System.out.println("Fully optimized complex,  " + delta + 
			 " micros. Cat: " + CAT_ARRAY[i].getName());
    }

    System.out.println();
    for(int i = 0; i < CAT_ARRAY.length; i++) {
      delta = ComplexMessage(CAT_ARRAY[i], runLength);
      System.out.println("Complex message argument, " + delta + 
			 " micros. Cat: " + CAT_ARRAY[i].getName());
    }
    
  }
  
  /**
    Program wide initialization method.  */
  static
  void ProgramInit(String[] args) {

    try {
      runLength = Integer.parseInt(args[1]);      
    }
    catch(java.lang.NumberFormatException e) {
      System.err.println(e);
      Usage();
    }      

    
    ConsoleAppender appender = new ConsoleAppender(new SimpleLayout());
    
    if("false".equals(args[0])) {
      // nothing to do
    } else if ("true".equals(args[0])) {
      System.out.println("Flagging as shipped code.");
      Category.getDefaultHierarchy().disableInfo();
    } else 
      Usage();

    SHORT_CAT.setLevel(Level.INFO);      
    Category.getRoot().setLevel(Level.INFO);

  }    
  

  static
  double SimpleMessage(Category category, String msg, long runLength) { 
    long before = System.currentTimeMillis();
    for(int i = 0; i < runLength; i++) {
      category.debug(msg);
    }
    return (System.currentTimeMillis() - before)*1000.0/runLength;    
  }

  static
  double FullyOptimizedComplexMessage(Category category, long runLength) {    
    long before = System.currentTimeMillis();
    for(int i = 0; i < runLength; i++) {
      if(category.isDebugEnabled())
	category.debug("Message" + i + 
		  " bottles of beer standing on the wall.");
    }    
    return (System.currentTimeMillis() - before)*1000.0/runLength;    
  }

  static
  double ComplexMessage(Category category, long runLength) {    
    long before = System.currentTimeMillis();
    for(int i = 0; i < runLength; i++) {
      category.debug("Message" + i +
		" bottles of beer standing on the wall.");
    }
    return (System.currentTimeMillis() - before)*1000.0/runLength;    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7244.java