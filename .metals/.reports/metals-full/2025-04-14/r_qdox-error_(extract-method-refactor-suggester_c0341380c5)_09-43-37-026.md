error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1838.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1838.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1838.java
text:
```scala
private static final i@@nt DEFAULT_MIN_PREFS_PER_USER = 1;

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

package org.apache.mahout.cf.taste.hadoop.similarity.item;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.cf.taste.common.TopK;
import org.apache.mahout.cf.taste.hadoop.EntityEntityWritable;
import org.apache.mahout.cf.taste.hadoop.TasteHadoopUtils;
import org.apache.mahout.cf.taste.hadoop.preparation.PreparePreferenceMatrixJob;
import org.apache.mahout.cf.taste.similarity.precompute.SimilarItem;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.RowSimilarityJob;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.VectorSimilarityMeasures;
import org.apache.mahout.math.map.OpenIntLongHashMap;

/**
 * <p>Distributed precomputation of the item-item-similarities for Itembased Collaborative Filtering</p>
 *
 * <p>Preferences in the input file should look like {@code userID,itemID[,preferencevalue]}</p>
 *
 * <p>
 * Preference value is optional to accommodate applications that have no notion of a preference value (that is, the user
 * simply expresses a preference for an item, but no degree of preference).
 * </p>
 *
 * <p>
 * The preference value is assumed to be parseable as a {@code double}. The user IDs and item IDs are
 * parsed as {@code long}s.
 * </p>
 *
 * <p>Command line arguments specific to this class are:</p>
 *
 * <ol>
 * <li>--input (path): Directory containing one or more text files with the preference data</li>
 * <li>--output (path): output path where similarity data should be written</li>
 * <li>--similarityClassname (classname): Name of distributed similarity measure class to instantiate or a predefined
 *  similarity from {@link org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.VectorSimilarityMeasure}</li>
 * <li>--maxSimilaritiesPerItem (integer): Maximum number of similarities considered per item (100)</li>
 * <li>--maxPrefsPerUser (integer): max number of preferences to consider per user, users with more preferences will
 *  be sampled down (1000)</li>
 * <li>--minPrefsPerUser (integer): ignore users with less preferences than this (1)</li>
 * <li>--booleanData (boolean): Treat input data as having no pref values (false)</li>
 * <li>--threshold (double): discard item pairs with a similarity value below this</li>
 * </ol>
 *
 * <p>General command line options are documented in {@link AbstractJob}.</p>
 *
 * <p>Note that because of how Hadoop parses arguments, all "-D" arguments must appear before all other arguments.</p>
 */
public final class ItemSimilarityJob extends AbstractJob {

  public static final String ITEM_ID_INDEX_PATH_STR = ItemSimilarityJob.class.getName() + ".itemIDIndexPathStr";
  public static final String MAX_SIMILARITIES_PER_ITEM = ItemSimilarityJob.class.getName() + ".maxSimilarItemsPerItem";

  private static final int DEFAULT_MAX_SIMILAR_ITEMS_PER_ITEM = 100;
  private static final int DEFAULT_MAX_PREFS_PER_USER = 1000;
  private static final int DEFAULT_MIN_PREFS_PER_USER = 2;

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new ItemSimilarityJob(), args);
  }
  
  @Override
  public int run(String[] args) throws Exception {

    addInputOption();
    addOutputOption();
    addOption("similarityClassname", "s", "Name of distributed similarity measures class to instantiate, " 
        + "alternatively use one of the predefined similarities (" + VectorSimilarityMeasures.list() + ')');
    addOption("maxSimilaritiesPerItem", "m", "try to cap the number of similar items per item to this number "
        + "(default: " + DEFAULT_MAX_SIMILAR_ITEMS_PER_ITEM + ')',
        String.valueOf(DEFAULT_MAX_SIMILAR_ITEMS_PER_ITEM));
    addOption("maxPrefsPerUser", "mppu", "max number of preferences to consider per user, " 
        + "users with more preferences will be sampled down (default: " + DEFAULT_MAX_PREFS_PER_USER + ')',
        String.valueOf(DEFAULT_MAX_PREFS_PER_USER));
    addOption("minPrefsPerUser", "mp", "ignore users with less preferences than this "
        + "(default: " + DEFAULT_MIN_PREFS_PER_USER + ')', String.valueOf(DEFAULT_MIN_PREFS_PER_USER));
    addOption("booleanData", "b", "Treat input as without pref values", String.valueOf(Boolean.FALSE));
    addOption("threshold", "tr", "discard item pairs with a similarity value below this", false);

    Map<String,List<String>> parsedArgs = parseArguments(args);
    if (parsedArgs == null) {
      return -1;
    }

    String similarityClassName = getOption("similarityClassname");
    int maxSimilarItemsPerItem = Integer.parseInt(getOption("maxSimilaritiesPerItem"));
    int maxPrefsPerUser = Integer.parseInt(getOption("maxPrefsPerUser"));
    int minPrefsPerUser = Integer.parseInt(getOption("minPrefsPerUser"));
    boolean booleanData = Boolean.valueOf(getOption("booleanData"));

    double threshold = hasOption("threshold") ?
        Double.parseDouble(getOption("threshold")) : RowSimilarityJob.NO_THRESHOLD;

    Path similarityMatrixPath = getTempPath("similarityMatrix");
    Path prepPath = getTempPath("prepareRatingMatrix");

    AtomicInteger currentPhase = new AtomicInteger();

    if (shouldRunNextPhase(parsedArgs, currentPhase)) {
      ToolRunner.run(getConf(), new PreparePreferenceMatrixJob(), new String[] {
        "--input", getInputPath().toString(),
        "--output", prepPath.toString(),
        "--maxPrefsPerUser", String.valueOf(maxPrefsPerUser),
        "--minPrefsPerUser", String.valueOf(minPrefsPerUser),
        "--booleanData", String.valueOf(booleanData),
        "--tempDir", getTempPath().toString(),
      });
    }

    if (shouldRunNextPhase(parsedArgs, currentPhase)) {
      int numberOfUsers = HadoopUtil.readInt(new Path(prepPath, PreparePreferenceMatrixJob.NUM_USERS),
          getConf());

      ToolRunner.run(getConf(), new RowSimilarityJob(), new String[] {
        "--input", new Path(prepPath, PreparePreferenceMatrixJob.RATING_MATRIX).toString(),
        "--output", similarityMatrixPath.toString(),
        "--numberOfColumns", String.valueOf(numberOfUsers),
        "--similarityClassname", similarityClassName,
        "--maxSimilaritiesPerRow", String.valueOf(maxSimilarItemsPerItem),
        "--excludeSelfSimilarity", String.valueOf(Boolean.TRUE),
        "--threshold", String.valueOf(threshold),
        "--tempDir", getTempPath().toString(),
      });
    }

    if (shouldRunNextPhase(parsedArgs, currentPhase)) {
      Job mostSimilarItems = prepareJob(similarityMatrixPath, getOutputPath(), SequenceFileInputFormat.class,
          MostSimilarItemPairsMapper.class, EntityEntityWritable.class, DoubleWritable.class,
          MostSimilarItemPairsReducer.class, EntityEntityWritable.class, DoubleWritable.class, TextOutputFormat.class);
      Configuration mostSimilarItemsConf = mostSimilarItems.getConfiguration();
      mostSimilarItemsConf.set(ITEM_ID_INDEX_PATH_STR,
          new Path(prepPath, PreparePreferenceMatrixJob.ITEMID_INDEX).toString());
      mostSimilarItemsConf.setInt(MAX_SIMILARITIES_PER_ITEM, maxSimilarItemsPerItem);
      boolean succeeded = mostSimilarItems.waitForCompletion(true);
      if (!succeeded) {
        return -1;
      }
    }

    return 0;
  }

  public static class MostSimilarItemPairsMapper
      extends Mapper<IntWritable,VectorWritable,EntityEntityWritable,DoubleWritable> {

    private OpenIntLongHashMap indexItemIDMap;
    private int maxSimilarItemsPerItem;

    @Override
    protected void setup(Context ctx) {
      Configuration conf = ctx.getConfiguration();
      maxSimilarItemsPerItem = conf.getInt(MAX_SIMILARITIES_PER_ITEM, -1);
      indexItemIDMap = TasteHadoopUtils.readItemIDIndexMap(conf.get(ITEM_ID_INDEX_PATH_STR), conf);

      Preconditions.checkArgument(maxSimilarItemsPerItem > 0, "maxSimilarItemsPerItem was not correctly set!");
    }

    @Override
    protected void map(IntWritable itemIDIndexWritable, VectorWritable similarityVector, Context ctx)
      throws IOException, InterruptedException {

      int itemIDIndex = itemIDIndexWritable.get();

      TopK<SimilarItem> topKMostSimilarItems =
          new TopK<SimilarItem>(maxSimilarItemsPerItem, SimilarItem.COMPARE_BY_SIMILARITY);

      Iterator<Vector.Element> similarityVectorIterator = similarityVector.get().iterateNonZero();

      while (similarityVectorIterator.hasNext()) {
        Vector.Element element = similarityVectorIterator.next();
        topKMostSimilarItems.offer(new SimilarItem(indexItemIDMap.get(element.index()), element.get()));
      }

      long itemID = indexItemIDMap.get(itemIDIndex);
      for (SimilarItem similarItem : topKMostSimilarItems.retrieve()) {
        long otherItemID = similarItem.getItemID();
        if (itemID < otherItemID) {
          ctx.write(new EntityEntityWritable(itemID, otherItemID), new DoubleWritable(similarItem.getSimilarity()));
        } else {
          ctx.write(new EntityEntityWritable(otherItemID, itemID), new DoubleWritable(similarItem.getSimilarity()));
        }
      }
    }
  }

  public static class MostSimilarItemPairsReducer
      extends Reducer<EntityEntityWritable,DoubleWritable,EntityEntityWritable,DoubleWritable> {
    @Override
    protected void reduce(EntityEntityWritable pair, Iterable<DoubleWritable> values, Context ctx)
      throws IOException, InterruptedException {
      ctx.write(pair, values.iterator().next());
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1838.java