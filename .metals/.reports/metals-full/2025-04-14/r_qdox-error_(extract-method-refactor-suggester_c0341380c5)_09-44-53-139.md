error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8071.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8071.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8071.java
text:
```scala
"'log'HH'log'")@@;

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.APL file.  */



package org.apache.log4j;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.TestFailure;
import junit.framework.Test;


public class UnitTestDRFA extends TestCase {

  public UnitTestDRFA(String name) {
    super(name);
  }

  public
  void testComputeCheckPeriod() {
    DailyRollingFileAppender drfa = new DailyRollingFileAppender();
    drfa.setName("testComputeCheckPeriod");
    drfa.setOption(DailyRollingFileAppender.DATE_PATTERN_OPTION, 
		   "yyyy-MM-dd.'log'");
    assertEquals(drfa.computeCheckPeriod(), 
		 DailyRollingFileAppender.TOP_OF_DAY);

    drfa.setOption(DailyRollingFileAppender.DATE_PATTERN_OPTION, 
		   "yyyy-MM-dd mm.'log'");
    assertEquals(drfa.computeCheckPeriod(), 
		 DailyRollingFileAppender.TOP_OF_MINUTE);

    drfa.setOption(DailyRollingFileAppender.DATE_PATTERN_OPTION, 
		   "yyyy-MM-dd a.'log'");
    assertEquals(drfa.computeCheckPeriod(), 
		 DailyRollingFileAppender.HALF_DAY);

    drfa.setOption(DailyRollingFileAppender.DATE_PATTERN_OPTION, 
		   "yyyy-MM-dd HH.'log'");
    assertEquals(drfa.computeCheckPeriod(), 
		 DailyRollingFileAppender.TOP_OF_HOUR);

    drfa.setOption(DailyRollingFileAppender.DATE_PATTERN_OPTION, 
		   "yyyy-MM.'log'");
    assertEquals(drfa.computeCheckPeriod(), 
		 DailyRollingFileAppender.TOP_OF_MONTH);

    drfa.setOption(DailyRollingFileAppender.DATE_PATTERN_OPTION, 
		   "HH'log'");
    assertEquals(drfa.computeCheckPeriod(), 
		 DailyRollingFileAppender.TOP_OF_HOUR);


  }


  public
  void testRC1() {  
    RollingCalendar rc = new RollingCalendar();
    rc.setType(DailyRollingFileAppender.TOP_OF_DAY);


    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(Calendar.YEAR, 20);
    c.set(Calendar.MONTH, 2);
    c.set(Calendar.DAY_OF_MONTH, 10); 
    c.set(Calendar.HOUR_OF_DAY, 1);
    c.set(Calendar.MINUTE, 10); 
    c.set(Calendar.SECOND, 10);
    c.set(Calendar.MILLISECOND, 88);
    
    c.setTime(rc.getNextCheckDate(c.getTime()));
    assertEquals(c.get(Calendar.DAY_OF_MONTH), 11);
    assertEquals(c.get(Calendar.HOUR_OF_DAY), 0);
    assertEquals(c.get(Calendar.MINUTE), 0);
    assertEquals(c.get(Calendar.SECOND), 0);
    assertEquals(c.get(Calendar.MILLISECOND), 0);
  }

  public
  void testRC2() {  
    RollingCalendar rc = new RollingCalendar();
    rc.setType(DailyRollingFileAppender.TOP_OF_DAY);


    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(Calendar.YEAR, 20);
    c.set(Calendar.MONTH, Calendar.JANUARY); 
    c.set(Calendar.DATE, 31); 
    c.set(Calendar.HOUR_OF_DAY, 1);
    c.set(Calendar.MINUTE, 10); 
    c.set(Calendar.SECOND, 10);
    c.set(Calendar.MILLISECOND, 88);

    System.out.println(c);
    System.out.println("\n");

    c.setTime(rc.getNextCheckDate(c.getTime()));
    assertEquals(c.get(Calendar.MONTH), Calendar.FEBRUARY);
    assertEquals(c.get(Calendar.DATE), 1);
    assertEquals(c.get(Calendar.HOUR_OF_DAY), 0);
    assertEquals(c.get(Calendar.MINUTE), 0);
    assertEquals(c.get(Calendar.SECOND), 0);
    assertEquals(c.get(Calendar.MILLISECOND), 0);

    System.out.println(c);
  }

  
  public 
  static
  Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(new UnitTestDRFA("testComputeCheckPeriod"));
    suite.addTest(new UnitTestDRFA("testRC1"));
    suite.addTest(new UnitTestDRFA("testRC2"));
    return suite;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8071.java