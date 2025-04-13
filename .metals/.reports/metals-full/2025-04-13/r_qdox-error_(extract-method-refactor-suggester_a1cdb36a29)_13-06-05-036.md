error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2620.java
text:
```scala
r@@oot.addAppender(new ConsoleAppender(layout, ConsoleAppender.SYSTEM_OUT));

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.APL file.  */

package org.apache.log4j.examples;

import org.apache.log4j.*;
import org.apache.log4j.spi.CategoryFactory;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.LogLog;

/**
   A simple example showing category subclassing. 

   <p>The example should make it clear that subclasses follow the
   hierarchy. You should also try running this example with a <a
   href="doc-files/mycat.bad">bad</a> and <a
   href="doc-files/mycat.good">good</a> configuration file samples.

   <p>See <b><a
   href="doc-files/MyCategory.java">source code</a></b> for more details.
   
 */
public class MyCategory extends Category {

  private static String FQCN = MyCategory.class.getName();

  // It's enough to instantiate a factory once and for all.
  private static MyCategoryFactory myFactory = new MyCategoryFactory();

  /**
     When called wihtout arguments, this program will just print 
     <pre>
       DEBUG [main] some.cat - Hello world.
     </pre>
     and exit.
     
     <b>However, it can be called with a configuration file in XML or
     properties format.

   */
  static public void main(String[] args) {
    
    if(args.length == 0) {
      // Note that the appender is added to root but that the log
      // request is made to an instance of MyCategory. The output still
      // goes to System.out.
      Category root = Category.getRoot();
      Layout layout = new PatternLayout("%p [%t] %c - %m%n");
      root.addAppender(new FileAppender(layout, System.out));
    }
    else if(args.length == 1) {
      if(args[0].endsWith("xml")) {
	DOMConfigurator.configure(args[0]);
      } else {
	PropertyConfigurator.configure(args[0]);
      }
    } else {
      usage("Incorrect number of parameters.");
    }
    try {
      MyCategory c = (MyCategory) MyCategory.getInstance("some.cat");    
      c.debug("Hello");
    } catch(ClassCastException e) {
      LogLog.error("Did you forget to set the factory in the config file?", e);
    }
  }

  static
  void usage(String errMsg) {
    System.err.println(errMsg);
    System.err.println("\nUsage: "+MyCategory.class.getName() + "[configFile]\n"
                + " where *configFile* is an optional configuration file, "+
		       "either in properties or XML format.");
    System.exit(1);
  }

  /**
     Just calls the parent constuctor.
   */
  public MyCategory(String name) {
    super(name);
  }


  /**
     Overrides the standard debug method by appending " world" at the
     end of each message.  */
  public 
  void debug(String message) {
    super.debug(message + " world.");    
  }

  /**
     This method overrides {@link Category#getInstance} by supplying
     its own factory type as a parameter.
  */
  public 
  static
  Category getInstance(String name) {
    return Category.getInstance(name, myFactory); 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2620.java