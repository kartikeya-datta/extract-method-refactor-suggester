error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2920.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2920.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2920.java
text:
```scala
D@@UMP.info( "Dump of integer array:");


package org.apache.log4j.examples;

import org.apache.log4j.Category;
import org.apache.log4j.NDC;

/**
   Example code for log4j to viewed in conjunction with the {@link
   org.apache.log4j.examples.Sort Sort} class.
      
   <p>SortAlgo uses the bubble sort algorithm to sort an integer
   array. See also its <b><a href="doc-files/SortAlgo.java">source
   code</a></b>.

   @author Ceki G&uuml;lc&uuml;
*/
public class SortAlgo {

  final static String className = SortAlgo.class.getName();
  final static Category CAT = Category.getInstance(className);
  final static Category OUTER = Category.getInstance(className + ".OUTER");
  final static Category INNER = Category.getInstance(className + ".INNER");
  final static Category DUMP = Category.getInstance(className + ".DUMP");
  final static Category SWAP = Category.getInstance(className + ".SWAP");

  int[] intArray;

  SortAlgo(int[] intArray) {
    this.intArray = intArray;
  }
    
  void bubbleSort() {
    CAT.info( "Entered the sort method.");

    for(int i = intArray.length -1; i >= 0  ; i--) {
      NDC.push("i=" + i);
      OUTER.debug("in outer loop.");
      for(int j = 0; j < i; j++) {
	NDC.push("j=" + j);
	// It is poor practice to ship code with log staments in tight loops.
	// We do it anyway in this example.
	INNER.debug( "in inner loop.");
         if(intArray[j] > intArray[j+1])
	   swap(j, j+1);
	NDC.pop();
      }
      NDC.pop();
    }
  }  

  void dump() {    
    if(! (this.intArray instanceof int[])) {
      DUMP.error( "Tried to dump an uninitialized array.");
      return;
    }
    DUMP.info( "Dump of interger array:");
    for(int i = 0; i < this.intArray.length; i++) {
      DUMP.info( "Element [" + i + "]=" + this.intArray[i]);
    }    
  }

  void swap(int l, int r) {
    // It is poor practice to ship code with log staments in tight
    // loops or code called potentially millions of times.
    SWAP.debug( "Swapping intArray["+l+"]=" + intArray[l] +
	                     " and intArray["+r+"]=" + intArray[r]);
    int temp = this.intArray[l];
    this.intArray[l] = this.intArray[r];
    this.intArray[r] = temp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2920.java