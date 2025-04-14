error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1693.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1693.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1693.java
text:
```scala
protected S@@tring params = null;

package org.apache.lucene.benchmark.byTask.tasks;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.benchmark.byTask.PerfRunData;
import org.apache.lucene.benchmark.byTask.stats.Points;
import org.apache.lucene.benchmark.byTask.stats.TaskStats;
import org.apache.lucene.benchmark.byTask.utils.Format;

/**
 * A (abstract)  task to be tested for performance.
 * <br>
 * Every performance task extends this class, and provides its own doLogic() method, 
 * which performss the actual task.
 * <br>
 * Tasks performing some work that should be measured for the task, can overide setup() and/or tearDown() and 
 * placed that work there. 
 * <br>
 * Relevant properties: <code>task.max.depth.log</code>.
 */
public abstract class PerfTask implements Cloneable {

  private PerfRunData runData;
  
  // propeties that all tasks have
  private String name;
  private int depth = 0;
  private int maxDepthLogStart = 0;
  private String params = null;
  
  protected static final String NEW_LINE = System.getProperty("line.separator");

  /**
   * Should not be used externally
   */
  private PerfTask() {
    name =  Format.simpleName(getClass());
    if (name.endsWith("Task")) {
      name = name.substring(0,name.length()-4);
    }
  }

  public PerfTask(PerfRunData runData) {
    this();
    this.runData = runData;
    this.maxDepthLogStart = runData.getConfig().get("task.max.depth.log",0);
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  protected Object clone() throws CloneNotSupportedException {
    // tasks having non primitive data structures should overide this.
    // otherwise parallel running of a task sequence might not run crrectly. 
    return super.clone();
  }

  /**
   * Run the task, record statistics.
   * @return number of work items done by this task.
   */
  public final int runAndMaybeStats(boolean reportStats) throws Exception {
    if (reportStats && depth <= maxDepthLogStart && !shouldNeverLogAtStart()) {
      System.out.println("------------> starting task: " + getName());
    }
    if (shouldNotRecordStats() || !reportStats) {
      setup();
      int count = doLogic();
      tearDown();
      return count;
    }
    setup();
    Points pnts = runData.getPoints();
    TaskStats ts = pnts.markTaskStart(this,runData.getConfig().getRoundNumber());
    int count = doLogic();
    pnts.markTaskEnd(ts, count);
    tearDown();
    return count;
  }

  /**
   * Perform the task once (ignoring repetions specification)
   * Return number of work items done by this task.
   * For indexing that can be number of docs added.
   * For warming that can be number of scanned items, etc.
   * @return number of work items done by this task.
   */
  public abstract int doLogic() throws Exception;
  
  /**
   * @return Returns the name.
   */
  public String getName() {
    if (params==null) {
      return name;
    } 
    return new StringBuffer(name).append('(').append(params).append(')').toString();
  }

  /**
   * @param name The name to set.
   */
  protected void setName(String name) {
    this.name = name;
  }

  /**
   * @return Returns the run data.
   */
  public PerfRunData getRunData() {
    return runData;
  }

  /**
   * @return Returns the depth.
   */
  public int getDepth() {
    return depth;
  }

  /**
   * @param depth The depth to set.
   */
  public void setDepth(int depth) {
    this.depth = depth;
  }
  
  // compute a blank string padding for printing this task indented by its depth  
  String getPadding () {
    char c[] = new char[4*getDepth()];
    for (int i = 0; i < c.length; i++) c[i] = ' ';
    return new String(c);
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    String padd = getPadding();
    StringBuffer sb = new StringBuffer(padd);
    sb.append(getName());
    return sb.toString();
  }

  /**
   * @return Returns the maxDepthLogStart.
   */
  int getMaxDepthLogStart() {
    return maxDepthLogStart;
  }

  /**
   * Tasks that should never log at start can overide this.  
   * @return true if this task should never log when it start.
   */
  protected boolean shouldNeverLogAtStart () {
    return false;
  }
  
  /**
   * Tasks that should not record statistics can overide this.  
   * @return true if this task should never record its statistics.
   */
  protected boolean shouldNotRecordStats () {
    return false;
  }

  /**
   * Task setup work that should not be measured for that specific task.
   * By default it does nothing, but tasks can implement this, moving work from 
   * doLogic() to this method. Only the work done in doLogicis measured for this task.
   * Notice that higher level (sequence) tasks containing this task would then 
   * measure larger time than the sum of their contained tasks.
   * @throws Exception 
   */
  public void setup () throws Exception {
  }
  
  /**
   * Task tearDown work that should not be measured for that specific task.
   * By default it does nothing, but tasks can implement this, moving work from 
   * doLogic() to this method. Only the work done in doLogicis measured for this task.
   * Notice that higher level (sequence) tasks containing this task would then 
   * measure larger time than the sum of their contained tasks.
   */
  public void tearDown () throws Exception {
  }

  /**
   * Sub classes that supports parameters must overide this method to return true.
   * @return true iff this task supports command line params.
   */
  public boolean supportsParams () {
    return false;
  }
  
  /**
   * Set the params of this task.
   * @exception UnsupportedOperationException for tasks supporting command line parameters.
   */
  public void setParams(String params) {
    if (!supportsParams()) {
      throw new UnsupportedOperationException(getName()+" does not support command line parameters.");
    }
    this.params = params;
  }
  
  /**
   * @return Returns the Params.
   */
  public String getParams() {
    return params;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1693.java