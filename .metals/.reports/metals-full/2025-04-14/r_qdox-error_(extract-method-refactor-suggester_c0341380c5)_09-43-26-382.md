error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5216.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5216.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5216.java
text:
```scala
t@@ = (int)newBuffer(len, second);

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

package org.apache.log4j.performance;

/**

   This program compares the cost of creating a new StringBuffer and
   converting it to a String versus keeping the same StringBuffer,
   setting its size to zero and then converting it to String.

   <p>The table below gives some figures.

<p>   <table border="1" cellpadding="4">
   <tr BGCOLOR="#33CCFF">
     <th BGCOLOR="#CCCCCC" rowspan="2">Total Message length
     <th colspan="2" align="center">0
     <th colspan="2" align="center">1
     <th colspan="2" align="center">2
     <th colspan="2" align="center">4
     <th colspan="2" align="center">8
   </tr>

   <tr BGCOLOR="#3366FF">
     <td>New Buffer</td> <td>setLength</td> 
     <td>New Buffer</td> <td>setLength</td>
     <td>New Buffer</td> <td>setLength</td>
     <td>New Buffer</td> <td>setLength</td>
     <td>New Buffer</td> <td>setLength</td>
   </tr>

   <tr align="right"> 
   <td BGCOLOR="#CCCCCC">256 
   <td>33 <td>22 
   <td>34 <td>22
   <td>34 <td>22
   <td>34 <td>22 
   <td>33 <td>23
   </tr>

   <tr align="right"> 
   <td BGCOLOR="#CCCCCC">1024 
   <td>58 <td>41
   <td>59 <td>45
   <td>59 <td>48
   <td>59 <td>51 
   <td>60 <td>44
   </tr>

   <tr align="right"> 
   <td BGCOLOR="#CCCCCC">4096 
   <td>146 <td>132
   <td>138 <td>132
   <td>144 <td>126
   <td>142 <td>132
   <td>136 <td>132 
   </tr>

   <tr align="right"> 
   <td BGCOLOR="#CCCCCC">16384 
   <td>617 <td>593 
   <td>593 <td>609
   <td>601 <td>617
   <td>601 <td>632 
   <td>593 <td>632
   </tr>

   <tr align="right"> 
   <td BGCOLOR="#CCCCCC">65536 
   <td>3218 <td>3281
   <td>3093 <td>3125 
   <td>3125 <td>3156
   <td>3125 <td>3281 
   <td>3062 <td>3562
   </tr>

   <tr align="right"> 
   <td BGCOLOR="#CCCCCC">262144 
   <td>14750 <td>15125
   <td>14000 <td>15500 
   <td>14000 <td>16125 
   <td>14000 <td>18000 
   <td>14000 <td>21375 
   </tr>

   <tr align="right"> 
   <td BGCOLOR="#CCCCCC">1048576 
   <td>87500 <td>80000
   <td>60500 <td>82000 
   <td>57000 <td>93000 
   <td>57500 <td>118500 
   <td>57500 <td>168500 
   </tr>

   <caption ALIGN="BOTTOM">Performance comparisons of new buffer
   creation versus setLength(0) approach for various message sizes and
   secondary loop lengths.
   </caption>
   </table>

   <p>The tests copy a message to a destination string buffer and then
   copy a 256 character buffer to another buffer the number of times
   as specified by the secondary loop length.


   <p>The <code>setLength(0)</code> method is usually faster. However,
   after copying a large string it becomes slow even when copying
   small strings.


   <p>This is due to a peculiarity in the <code>copy</code> method in
   StringBuffer class which creates a character array of the same
   length as the old buffer even if the vast majority of those
   characters are unused. 

   <p>The tests were performed on Linux using IBM's JDK 1.1.8.

   <p>The test script is a crude model of what might happen in
   reality. If you remain unconvinced of its results, then please send
   your alternative measurement scenario.

   

   
*/
public class NewVsSetLen {

  static String s;

  static int BIGBUF_LEN = 1048576;
  static int SBUF_LEN = 256;
  static int RUN_LENGTH = BIGBUF_LEN/4;

  static char[] sbuf = new char[SBUF_LEN];
  static char[] bigbuf = new char[BIGBUF_LEN];

  {
    for(int i = 0; i < SBUF_LEN; i++) {
      sbuf[i] = (char) (i);
    }

    for(int i = 0; i < BIGBUF_LEN; i++) {
      bigbuf[i] = (char) (i);
    }
  }


  static
  public 
  void main(String[] args) {    
 
    int t;

    for(int len = SBUF_LEN; len <= BIGBUF_LEN; len*=4, RUN_LENGTH /= 4) {
      System.out.println("<td>"+len+"\n");
      for(int second = 0; second < 16;) {
	System.out.println("SECOND loop="+second +", RUN_LENGTH="
			   +RUN_LENGTH+", len="+len);
	t = (int)newBuffer(len, second);;

	System.out.print("<td>" + t);
	t = (int)setLen(len, second);
	System.out.println(" <td>" + t + " \n");
	if(second == 0) {
	  second = 1;
	} else {
	  second *= 2;
	}
      }
    }
    
  }

  static
  double newBuffer(int size, int second) {    
    long before = System.currentTimeMillis();

    for(int i = 0; i < RUN_LENGTH; i++) {
      StringBuffer buf = new StringBuffer(SBUF_LEN);
      buf.append(sbuf, 0, sbuf.length);
      buf.append(bigbuf, 0, size);
      s = buf.toString();
    }

    for(int x = 0; x <  second; x++) {
      StringBuffer buf = new StringBuffer(SBUF_LEN);
      buf.append(sbuf, 0, SBUF_LEN);
      s = buf.toString();
    }
    return (System.currentTimeMillis() - before)*1000.0/RUN_LENGTH;    
  }

  static
  double setLen(int size, int second) {
    long before = System.currentTimeMillis();

    StringBuffer buf = new StringBuffer(SBUF_LEN);

    for(int i = 0; i < RUN_LENGTH; i++) {
      buf.append(sbuf, 0, sbuf.length);
      buf.append(bigbuf, 0, size);
      s = buf.toString();
      buf.setLength(0);
    }

    for(int x = 0; x < second; x++) {
      buf.append(sbuf, 0, SBUF_LEN);
      s = buf.toString();
      buf.setLength(0);
    }
    return (System.currentTimeMillis() - before)*1000.0/RUN_LENGTH;    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5216.java