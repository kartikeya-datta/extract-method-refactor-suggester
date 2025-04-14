error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3597.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3597.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3597.java
text:
```scala
private static L@@ogger log;

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

package org.apache.mahout.df.data;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.df.callback.PredictionCallback;
import org.apache.mahout.df.data.Dataset.Attribute;
import org.slf4j.Logger;

/**
 * Helper methods used by the tests
 *
 */
public final class Utils {
  private Utils() {
  }

  private static class LogCallback implements PredictionCallback {
  
    private final Logger log;
  
    private LogCallback(Logger log) {
      this.log = log;
    }
  
    @Override
    public void prediction(int treeId, int instanceId, int prediction) {
      log.info(String.format("treeId:%04d, instanceId:%06d, prediction:%d",
          treeId, instanceId, prediction));
    }
  
  }

  /** Used when generating random CATEGORICAL values */
  private static final int CATEGORICAL_RANGE = 100;

  /**
   * Generates a random list of tokens
   * <ul>
   * <li>each attribute has 50% chance to be NUMERICAL ('N') or CATEGORICAL
   * ('C')</li>
   * <li>10% of the attributes are IGNORED ('I')</li>
   * <li>one randomly chosen attribute becomes the LABEL ('L')</li>
   * </ul>
   * 
   * @param rng Random number generator
   * @param nbTokens number of tokens to generate
   * @return
   */
  public static char[] randomTokens(Random rng, int nbTokens) {
    char[] result = new char[nbTokens];

    for (int token = 0; token < nbTokens; token++) {
      double rand = rng.nextDouble();
      if (rand < 0.1) {
        result[token] = 'I'; // IGNORED
      } else if (rand >= 0.5) {
        result[token] = 'C';
      } else {
        result[token] = 'N'; // NUMERICAL
      } // CATEGORICAL
    }

    // choose the label
    result[rng.nextInt(nbTokens)] = 'L';

    return result;
  }

  /**
   * Generates a space-separated String that contains all the tokens
   * 
   * @param tokens
   * @return
   */
  public static String generateDescriptor(char[] tokens) {
    StringBuilder builder = new StringBuilder();

    for (char token1 : tokens) {
      builder.append(token1).append(' ');
    }

    return builder.toString();
  }

  /**
   * Generates a random descriptor as follows:<br>
   * <ul>
   * <li>each attribute has 50% chance to be NUMERICAL or CATEGORICAL</li>
   * <li>10% of the attributes are IGNORED</li>
   * <li>one randomly chosen attribute becomes the LABEL</li>
   * </ul>
   * 
   * @param nbAttributes
   * @return
   */
  public static String randomDescriptor(Random rng, int nbAttributes) {
    return generateDescriptor(randomTokens(rng, nbAttributes));
  }

  /**
   * generates random data
   * 
   * @param rng Random number generator
   * @param nbAttributes number of attributes
   * @param number of data lines to generate
   * @return
   * @throws Exception 
   */
  public static double[][] randomDoubles(Random rng, int nbAttributes,int number) throws DescriptorException {
    String descriptor = randomDescriptor(rng, nbAttributes);
    Attribute[] attrs = DescriptorUtils.parseDescriptor(descriptor);

    double[][] data = new double[number][];

    for (int index = 0; index < number; index++) {
      data[index] = randomVector(rng, attrs);
    }

    return data;
  }

  /**
   * generates random data based on the given descriptor
   * 
   * @param rng Random number generator
   * @param descriptor attributes description
   * @param number number of data lines to generate
   */
  public static double[][] randomDoubles(Random rng, String descriptor, int number) throws DescriptorException {
    Attribute[] attrs = DescriptorUtils.parseDescriptor(descriptor);

    double[][] data = new double[number][];

    for (int index = 0; index < number; index++) {
      data[index] = randomVector(rng, attrs);
    }

    return data;
  }

  /**
   * Generates random data
   * 
   * @param rng Random number generator
   * @param nbAttributes number of attributes
   * @param size data size
   * @return
   * @throws Exception 
   */
  public static Data randomData(Random rng, int nbAttributes, int size) throws DescriptorException {
    String descriptor = randomDescriptor(rng, nbAttributes);
    double[][] source = randomDoubles(rng, descriptor, size);
    String[] sData = double2String(source);
    Dataset dataset = DataLoader.generateDataset(descriptor, sData);
    
    return DataLoader.loadData(dataset, sData);
  }

  /**
   * generates a random vector based on the given attributes.<br>
   * the attributes' values are generated as follows :<br>
   * <ul>
   * <li>each IGNORED attribute receives a Double.NaN</li>
   * <li>each NUMERICAL attribute receives a random double</li>
   * <li>each CATEGORICAL and LABEL attribute receives a random integer in the
   * range [0, CATEGORICAL_RANGE[</li>
   * </ul>
   * 
   * @param rng
   * @param attrs attributes description
   * @return
   */
  private static double[] randomVector(Random rng, Attribute[] attrs) {
    double[] vector = new double[attrs.length];

    for (int attr = 0; attr < attrs.length; attr++) {
      if (attrs[attr].isIgnored()) {
        vector[attr] = Double.NaN;
      } else if (attrs[attr].isNumerical()) {
        vector[attr] = rng.nextDouble();
      } else {
        // CATEGORICAL or LABEL
        vector[attr] = rng.nextInt(CATEGORICAL_RANGE);
      }
    }

    return vector;
  }

  /**
   * converts a double array to a comma-separated string
   * 
   * @param v double array
   * @return comma-separated string
   */
  private static String double2String(double[] v) {
    StringBuilder builder = new StringBuilder();

    for (double aV : v) {
      builder.append(aV).append(',');
    }

    return builder.toString();
  }

  /**
   * converts an array of double arrays to an array of comma-separated strings
   * 
   * @param source array of double arrays
   * @return array of comma-separated strings
   */
  public static String[] double2String(double[][] source) {
    String[] output = new String[source.length];

    for (int index = 0; index < source.length; index++) {
      output[index] = double2String(source[index]);
    }

    return output;
  }

  /**
   * Generates random data with same label value
   * 
   * @param rng
   * @param descriptor
   * @param number data size
   * @param value label value
   * @return
   * @throws Exception 
   */
  public static double[][] randomDoublesWithSameLabel(Random rng,
      String descriptor, int number, int value) throws DescriptorException {
    int label = findLabel(descriptor);
    
    double[][] source = randomDoubles(rng, descriptor, number);
    
    for (int index = 0; index < number; index++) {
      source[index][label] = value;
    }

    return source;
  }

  /**
   * finds the label attribute's index
   * 
   * @param descriptor
   * @return
   * @throws Exception 
   */
  public static int findLabel(String descriptor) throws DescriptorException {
    Attribute[] attrs = DescriptorUtils.parseDescriptor(descriptor);
    return ArrayUtils.indexOf(attrs, Attribute.LABEL);
  }

  private static void writeDataToFile(String[] sData, Path path) throws IOException {
    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(path.toString()), Charset.forName("UTF-8")));
    try {
      for (String line : sData) {
        output.write(line);
        output.write('\n');
      }
      output.flush();
    } finally {
      output.close();
    }
  
  }

  public static Path writeDataToTestFile(String[] sData) throws IOException {
    Path testData = new Path("testdata/Data");
    FileSystem fs = testData.getFileSystem(new Configuration());
    if (!fs.exists(testData)) {
      fs.mkdirs(testData);
    }
  
    Path path = new Path(testData, "DataLoaderTest.data");
  
    writeDataToFile(sData, path);
  
    return path;
  }

  public static Path writeDatasetToTestFile(Dataset dataset) throws IOException {
    Path testData = new Path("testdata/Dataset");
    FileSystem fs = testData.getFileSystem(new Configuration());
    if (!fs.exists(testData)) {
      fs.mkdirs(testData);
    }
  
    Path datasetPath = new Path(testData, "dataset.info");
    FSDataOutputStream out = fs.create(datasetPath);
  
    try {
      dataset.write(out);
    } finally {
      out.close();
    }
  
    return datasetPath;
  }

  /**
   * Split the data into numMaps splits
   * 
   * @param sData
   * @param numMaps
   * @return
   */
  public static String[][] splitData(String[] sData, int numMaps) {
    int nbInstances = sData.length;
    int partitionSize = nbInstances / numMaps;
  
    String[][] splits = new String[numMaps][];
  
    for (int partition = 0; partition < numMaps; partition++) {
      int from = partition * partitionSize;
      int to = (partition == (numMaps - 1)) ? nbInstances : (partition + 1)
          * partitionSize;
  
      splits[partition] = Arrays.copyOfRange(sData, from, to);
    }
  
    return splits;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3597.java