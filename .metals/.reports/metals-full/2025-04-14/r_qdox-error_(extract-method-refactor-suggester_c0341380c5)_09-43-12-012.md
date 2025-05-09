error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10031.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10031.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 690
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10031.java
text:
```scala
public class FixedWindowRollingPolicy extends RollingPolicyBase {

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

p@@ackage org.apache.log4j.rolling;

import org.apache.log4j.rolling.helper.Compress;
import org.apache.log4j.rolling.helper.IntegerTokenConverter;
import org.apache.log4j.rolling.helper.Util;

import java.io.File;


/**
 * When rolling over, <code>FixedWindowRollingPolicy</code> renames files 
 * according to a fixed window algorithm as described below. 
 * 
 * <p>The <b>ActiveFileName</b> property, which is required, represents the name 
 * of the file where current logging output will be written. 
 * The <b>FileNamePattern</b>  option represents the file name pattern for the 
 * archived (rolled over) log files. If present, the <b>FileNamePattern</b> 
 * option must include an integer token, that is the string "%i" somwhere 
 * within the pattern.
 * 
 * <p>Let <em>max</em> and <em>min</em> represent the values of respectively 
 * the <b>MaxIndex</b> and <b>MinIndex</b> options. Let "foo.log" be the value
 * of the <b>ActiveFile</b> option and "foo.%i.log" the value of 
 * <b>FileNamePattern</b>. Then, when rolling over, the file 
 * <code>foo.<em>max</em>.log</code> will be deleted, the file 
 * <code>foo.<em>max-1</em>.log</code> will be renamed as 
 * <code>foo.<em>max</em>.log</code>, the file <code>foo.<em>max-2</em>.log</code> 
 * renamed as <code>foo.<em>max-1</em>.log</code>, and so on, 
 * the file <code>foo.<em>min+1</em>.log</code> renamed as 
 * <code>foo.<em>min+2</em>.log</code>. Lastly, the active file <code>foo.log</code>
 * will be renamed as <code>foo.<em>min</em>.log</code> and a new active file name
 * <code>foo.log</code> will be created.
 * 
 * <p>Given that this rollover algorithm requires as many file renaming 
 * operations as the window size, large window sizes are discouraged. The
 * current implementation will automatically reduce the window size to 12 when
 * larger values are specified by the user.
 * 
 *
 * @author Ceki G&uuml;lc&uuml;
 * @since 1.3
 * */
public class FixedWindowRollingPolicy extends RollingPolicySkeleton {
  int maxIndex;
  int minIndex;
  
  /**
   * It's almost always a bad idea to have a large window size, say over 12. 
   */
  private static int MAX_WINDOW_SIZE = 12;
  
  public FixedWindowRollingPolicy() {
    minIndex = 1;
    maxIndex = 7;
    activeFileName = null;
  }

  public void activateOptions() {
    if (activeFileName == null) {
      getLogger().warn(
        "The ActiveFile name option must be set before using this rolling policy.");
      throw new IllegalStateException(
        "The ActiveFileName option must be set.");
    }

    if (maxIndex < minIndex) {
      getLogger().warn(
        "MaxIndex (" + maxIndex + ") cannot be smaller than MinIndex ("
        + minIndex + ").");
      getLogger().warn("Setting maxIndex to equal minIndex.");
      maxIndex = minIndex;
    }
    
    if((maxIndex-minIndex) > MAX_WINDOW_SIZE) {
      getLogger().warn("Large window sizes are not allowed.");
      maxIndex = minIndex +  MAX_WINDOW_SIZE;
      getLogger().warn("MaxIndex reduced to {}.", new Integer(maxIndex));
    }
    
    if (fileNamePatternStr != null) {
      determineCompressionMode();
    }
    
    IntegerTokenConverter itc = fileNamePattern.getIntegerTokenConverter();

    if (itc == null) {
      throw new IllegalStateException(
        "FileNamePattern [" + fileNamePattern.getPattern()
        + "] does not contain a valid IntegerToken");
    }
  }

  public void rollover() throws RolloverFailure {
    // Inside this method it is guaranteed that the hereto active log fil is closed.
    // If maxIndex <= 0, then there is no file renaming to be done.
    if (maxIndex >= 0) {
      // Delete the oldest file, to keep Windows happy.
      File file = new File(fileNamePattern.convert(maxIndex));

      if (file.exists()) {
        file.delete();
      }

      // Map {(maxIndex - 1), ..., minIndex} to {maxIndex, ..., minIndex+1}
      for (int i = maxIndex - 1; i >= minIndex; i--) {
        
        Util.rename(
          fileNamePattern.convert(i), fileNamePattern.convert(i + 1));
      }

      if (activeFileName != null) {
        //move active file name to min
        switch (compressionMode) {
        case Compress.NONE:
          Util.rename(activeFileName, fileNamePattern.convert(minIndex));
          break;
        case Compress.GZ:
          Compress.GZCompress(
            activeFileName, fileNamePattern.convert(minIndex));
          break;
        
        case Compress.ZIP:
        Compress.ZIPCompress(
          activeFileName, fileNamePattern.convert(minIndex));
        break;
        }
      }
    }
  }

  /**
   * Return the value of the <b>ActiveFile</b> option.
   * 
   * @see {@link setActiveFileName}.
  */
  public String getActiveFileName() {
    // TODO This is clearly bogus.
    return activeFileName;
  }

  public int getMaxIndex() {
    return maxIndex;
  }

  public int getMinIndex() {
    return minIndex;
  }

  public void setMaxIndex(int maxIndex) {
    this.maxIndex = maxIndex;
  }

  public void setMinIndex(int minIndex) {
    this.minIndex = minIndex;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10031.java