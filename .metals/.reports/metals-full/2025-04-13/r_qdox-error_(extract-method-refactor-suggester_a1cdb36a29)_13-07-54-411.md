error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2662.java
text:
```scala
(@@ratings ? "ratings" : "movies") + ".txt");

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

package org.apache.mahout.cf.taste.example.grouplens;

import org.apache.mahout.cf.taste.impl.common.FastMap;
import org.apache.mahout.cf.taste.impl.common.FileLineIterable;
import org.apache.mahout.cf.taste.impl.common.IOUtils;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.Item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.NoSuchElementException;
import java.nio.charset.Charset;

public final class GroupLensDataModel extends FileDataModel {

  private Map<String, Movie> movieMap;
  private final File moviesFile;

  public GroupLensDataModel() throws IOException {
    this(readResourceToTempFile("/org/apache/mahout/cf/taste/example/grouplens/ratings.dat"),
         readResourceToTempFile("/org/apache/mahout/cf/taste/example/grouplens/movies.dat"));
  }

  /**
   * @param ratingsFile GroupLens ratings.dat file in its native format
   * @param moviesFile GroupLens movies.dat file in its native format
   * @throws IOException if an error occurs while reading or writing files
   */
  public GroupLensDataModel(File ratingsFile, File moviesFile) throws IOException {
    super(convertGLFile(ratingsFile, true));
    this.moviesFile = moviesFile;
  }

  @Override
  protected Item buildItem(String id) {
    Item item = movieMap.get(id);
    if (item == null) {
      throw new NoSuchElementException();
    }
    return item;
  }

  @Override
  protected void reload() {
    File convertedMoviesFile;
    try {
      convertedMoviesFile = convertGLFile(moviesFile, false);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe); // better way?
    }
    movieMap = new FastMap<String, Movie>(5001);
    for (String line : new FileLineIterable(convertedMoviesFile, false)) {
      String[] tokens = line.split(",");
      String id = tokens[0];
      movieMap.put(id, new Movie(id, tokens[1], tokens[2]));
    }
    super.reload();
    movieMap = null;
  }

  private static File convertGLFile(File originalFile, boolean ratings) throws IOException {
    // Now translate the file; remove commas, then convert "::" delimiter to comma
    File resultFile = new File(new File(System.getProperty("java.io.tmpdir")),
                                     "taste." + (ratings ? "ratings" : "movies") + ".txt");
    if (!resultFile.exists()) {
      PrintWriter writer = null;
      try {
        writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(resultFile), Charset.forName("UTF-8")));
        for (String line : new FileLineIterable(originalFile, false)) {
          String convertedLine;
          if (ratings) {
            // toss the last column of data, which is a timestamp we don't want
            convertedLine = line.substring(0, line.lastIndexOf("::")).replace("::", ",");
          } else {
            convertedLine = line.replace(",", "").replace("::", ",");
          }
          writer.println(convertedLine);
        }
        writer.flush();
      } catch (IOException ioe) {
        resultFile.delete();
        throw ioe;
      } finally {
        IOUtils.quietClose(writer);
      }
    }
    return resultFile;
  }

  public static File readResourceToTempFile(String resourceName) throws IOException {
    InputStream is = GroupLensRecommender.class.getResourceAsStream(resourceName);
    if (is == null) {
      // No resource found, try just using the file
      return new File("src/main/java" + resourceName);
    }
    try {
      File tempFile = File.createTempFile("taste", null);
      tempFile.deleteOnExit();
      OutputStream os = new FileOutputStream(tempFile);
      try {
        int bytesRead;
        for (byte[] buffer = new byte[32768]; (bytesRead = is.read(buffer)) > 0;) {
          os.write(buffer, 0, bytesRead);
        }
        os.flush();
        return tempFile;
      } finally {
        IOUtils.quietClose(os);
      }
    } finally {
      IOUtils.quietClose(is);
    }
  }


  @Override
  public String toString() {
    return "GroupLensDataModel";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2662.java