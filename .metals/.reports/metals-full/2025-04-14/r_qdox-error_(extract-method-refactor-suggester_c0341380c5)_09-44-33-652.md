error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5000.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5000.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5000.java
text:
```scala
static L@@evel defaultLevel = Logger.getRootLogger().getLevel();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

package org.apache.log4j;


import org.apache.log4j.Level;
import org.apache.log4j.Category;
import java.util.Random;

/*
  Stress test the Category class.

*/

class StressCategory {

  static Level[] level = new Level[] {Level.DEBUG, 
				      Level.INFO, 
				      Level.WARN,
				      Level.ERROR,
				      Level.FATAL};

  static Level defaultLevel = Category.getRoot().getLevel();
  
  static int LENGTH;
  static String[] names;
  static Category[] cat;
  static CT[] ct;

  static Random random = new Random(10);

  public static void main(String[] args) {
    
    LENGTH = args.length;

    if(LENGTH == 0) {
      System.err.println( "Usage: java " + StressCategory.class.getName() +
			  " name1 ... nameN\n.");      
      System.exit(1);
    }
    if(LENGTH >= 7) {
      System.err.println(
        "This stress test suffers from combinatorial explosion.\n"+
        "Invoking with seven arguments takes about 90 minutes even on fast machines");
    }

    names = new String[LENGTH];
    for(int i=0; i < LENGTH; i++) {
      names[i] = args[i];
    }    
    cat = new Category[LENGTH];
    ct = new CT[LENGTH]; 


    permute(0); 

    // If did not exit, then passed all tests.
  }

  // Loop through all permutations of names[].
  // On each possible permutation call createLoop
  static
  void permute(int n) {
    if(n == LENGTH)
      createLoop(0);
    else
      for(int i = n; i < LENGTH; i++) {
	swap(names, n, i);
	permute(n+1);
	swap(names, n, i);	
      }
  }

  static
  void swap(String[] names, int i, int j) {
    String t = names[i];
    names[i] = names[j];
    names[j] = t;
  }
  
  public
  static
  void permutationDump() {
    System.out.print("Current permutation is - ");
    for(int i = 0; i < LENGTH; i++) {
      System.out.print(names[i] + " ");
    }
    System.out.println();
  }


  // Loop through all possible 3^n combinations of not instantiating, 
  // instantiating and setting/not setting a level.

  static
  void createLoop(int n) {
    if(n == LENGTH) {  
      //System.out.println("..............Creating cat[]...........");
      for(int i = 0; i < LENGTH; i++) {
	if(ct[i] == null)
	  cat[i] = null;
	else {
	  cat[i] = Category.getInstance(ct[i].catstr);
	  cat[i].setLevel(ct[i].level);
	}
      }
      test();
      // Clear hash table for next round
      Hierarchy h = (Hierarchy) LogManager.getLoggerRepository();
      h.clear();
    }
    else {      
      ct[n]  = null;
      createLoop(n+1);  

      ct[n]  = new CT(names[n], null);
      createLoop(n+1);  
      
      int r = random.nextInt(); if(r < 0) r = -r;
      ct[n]  = new CT(names[n], level[r%5]);
      createLoop(n+1);
    }
  }


  static
  void test() {    
    //System.out.println("++++++++++++TEST called+++++++++++++");
    //permutationDump();
    //catDump();

    for(int i = 0; i < LENGTH; i++) {
      if(!checkCorrectness(i)) {
	System.out.println("Failed stress test.");
	permutationDump();
	
	//Hierarchy._default.fullDump();
	ctDump();
	catDump();
	System.exit(1);
      }
    }
  }
  
  static
  void ctDump() {
    for(int j = 0; j < LENGTH; j++) {
       if(ct[j] != null) 
	    System.out.println("ct [" + j + "] = ("+ct[j].catstr+"," + 
			       ct[j].level + ")");
       else 
	 System.out.println("ct [" + j + "] = undefined");
    }
  }
  
  static
  void catDump() {
    for(int j = 0; j < LENGTH; j++) {
      if(cat[j] != null)
	System.out.println("cat[" + j + "] = (" + cat[j].name + "," +
			   cat[j].getLevel() + ")");
      else
	System.out.println("cat[" + j + "] = undefined"); 
    }
  }

  //  static
  //void provisionNodesDump() {
  //for (Enumeration e = CategoryFactory.ht.keys(); e.hasMoreElements() ;) {
  //  CategoryKey key = (CategoryKey) e.nextElement();
  //  Object c = CategoryFactory.ht.get(key);
  //  if(c instanceof  ProvisionNode) 
  //((ProvisionNode) c).dump(key.name);
  //}
  //}
  
  static
  boolean checkCorrectness(int i) {
    CT localCT = ct[i];

    // Can't perform test if category is not instantiated
    if(localCT == null) 
      return true;
    
    // find expected level
    Level expected = getExpectedPrioriy(localCT);

			    
    Level purported = cat[i].getEffectiveLevel();

    if(expected != purported) {
      System.out.println("Expected level for " + localCT.catstr + " is " +
		       expected);
      System.out.println("Purported level for "+ cat[i].name + " is "+purported);
      return false;
    }
    return true;
      
  }

  static
  Level getExpectedPrioriy(CT ctParam) {
    Level level = ctParam.level;
    if(level != null) 
      return level;

    
    String catstr = ctParam.catstr;    
    
    for(int i = catstr.lastIndexOf('.', catstr.length()-1); i >= 0; 
	                              i = catstr.lastIndexOf('.', i-1))  {
      String substr = catstr.substring(0, i);

      // find the level of ct corresponding to substr
      for(int j = 0; j < LENGTH; j++) {	
	if(ct[j] != null && substr.equals(ct[j].catstr)) {
	  Level p = ct[j].level;
	  if(p != null) 
	    return p;	  
	}
      }
    }
    return defaultLevel;
  }

  

  static class CT {
    public String   catstr;
    public Level level;

    CT(String catstr,  Level level) {
      this.catstr = catstr;
      this.level = level;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5000.java