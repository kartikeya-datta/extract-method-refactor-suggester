error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/593.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/593.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/593.java
text:
```scala
private static v@@oid processOtherUser(Object id,

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

package org.apache.mahout.cf.taste.impl.eval;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.RandomUtils;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;
import org.apache.mahout.cf.taste.impl.common.RunningAverageAndStdDev;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverageAndStdDev;
import org.apache.mahout.cf.taste.impl.common.FastSet;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUser;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Item;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.User;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * <p>For each {@link User}, these implementation determine the top <code>n</code> preferences,
 * then evaluate the IR statistics based on a {@link DataModel} that does not have these values.
 * This number <code>n</code> is the "at" value, as in "precision at 5". For example, this would mean precision
 * evaluated by removing the top 5 preferences for a {@link User} and then finding the percentage of those 5
 * {@link Item}s included in the top 5 recommendations for that user.</p>
 */
public final class GenericRecommenderIRStatsEvaluator implements RecommenderIRStatsEvaluator {

  private static final Logger log = LoggerFactory.getLogger(GenericRecommenderIRStatsEvaluator.class);

  /**
   * Pass as "relevanceThreshold" argument to
   * {@link #evaluate(RecommenderBuilder, DataModel, Rescorer, int, double, double)} to have it attempt
   * to compute a reasonable threshold. Note that this will impact performance.
   */
  public static final double CHOOSE_THRESHOLD = Double.NaN;

  private final Random random;

  public GenericRecommenderIRStatsEvaluator() {
    random = RandomUtils.getRandom();
  }

  @Override
  public IRStatistics evaluate(RecommenderBuilder recommenderBuilder,
                               DataModel dataModel,
                               Rescorer<Item> rescorer,
                               int at,
                               double relevanceThreshold,
                               double evaluationPercentage) throws TasteException {

    if (recommenderBuilder == null) {
      throw new IllegalArgumentException("recommenderBuilder is null");
    }
    if (dataModel == null) {
      throw new IllegalArgumentException("dataModel is null");
    }
    if (at < 1) {
      throw new IllegalArgumentException("at must be at least 1");
    }
    if (Double.isNaN(evaluationPercentage) || evaluationPercentage <= 0.0 || evaluationPercentage > 1.0) {
      throw new IllegalArgumentException("Invalid evaluationPercentage: " + evaluationPercentage);
    }
    if (Double.isNaN(relevanceThreshold)) {
      throw new IllegalArgumentException("Invalid relevanceThreshold: " + evaluationPercentage);
    }

    int numItems = dataModel.getNumItems();
    RunningAverage precision = new FullRunningAverage();
    RunningAverage recall = new FullRunningAverage();
    RunningAverage fallOut = new FullRunningAverage();
    for (User user : dataModel.getUsers()) {
      if (random.nextDouble() < evaluationPercentage) {
        Object id = user.getID();
        Collection<Item> relevantItems = new FastSet<Item>(at);
        Preference[] prefs = user.getPreferencesAsArray();
        double theRelevanceThreshold = Double.isNaN(relevanceThreshold) ? computeThreshold(prefs) : relevanceThreshold;
        for (Preference pref : prefs) {
          if (pref.getValue() >= theRelevanceThreshold) {
            relevantItems.add(pref.getItem());
          }
        }
        int numRelevantItems = relevantItems.size();
        if (numRelevantItems > 0) {
          List<User> trainingUsers = new ArrayList<User>(dataModel.getNumUsers());
          for (User user2 : dataModel.getUsers()) {
            processOtherUser(id, relevantItems, trainingUsers, user2);
          }
          DataModel trainingModel = new GenericDataModel(trainingUsers);
          Recommender recommender = recommenderBuilder.buildRecommender(trainingModel);

          try {
            trainingModel.getUser(id);
          } catch (NoSuchUserException nsee) {
            continue; // Oops we excluded all prefs for the user -- just move on
          }

          int intersectionSize = 0;
          List<RecommendedItem> recommendedItems = recommender.recommend(id, at, rescorer);
          for (RecommendedItem recommendedItem : recommendedItems) {
            if (relevantItems.contains(recommendedItem.getItem())) {
              intersectionSize++;
            }
          }
          int numRecommendedItems = recommendedItems.size();
          if (numRecommendedItems > 0) {
            precision.addDatum((double) intersectionSize / (double) numRecommendedItems);
          }
          recall.addDatum((double) intersectionSize / (double) numRelevantItems);
          if (numRelevantItems < prefs.length) {
            fallOut.addDatum((double) (numRecommendedItems - intersectionSize) /
                             (double) (numItems - numRelevantItems));
          }

          log.info("Precision/recall/fall-out: {} / {} / {}", new Object[] {
              precision.getAverage(), recall.getAverage(), fallOut.getAverage()
          });
        }
      }
    }

    return new IRStatisticsImpl(precision.getAverage(), recall.getAverage(), fallOut.getAverage());
  }

  private void processOtherUser(Object id,
                                Collection<Item> relevantItems,
                                Collection<User> trainingUsers,
                                User user2) {
    if (id.equals(user2.getID())) {
      List<Preference> trainingPrefs = new ArrayList<Preference>();
      Preference[] prefs2 = user2.getPreferencesAsArray();
      for (Preference pref : prefs2) {
        if (!relevantItems.contains(pref.getItem())) {
          trainingPrefs.add(pref);
        }
      }
      if (!trainingPrefs.isEmpty()) {
        User trainingUser = new GenericUser<String>(id.toString(), trainingPrefs);
        trainingUsers.add(trainingUser);
      }
    } else {
      trainingUsers.add(user2);
    }
  }

  private static double computeThreshold(Preference[] prefs) {
    if (prefs.length < 2) {
      // Not enough data points -- return a threshold that allows everything
      return Double.NEGATIVE_INFINITY;
    }
    RunningAverageAndStdDev stdDev = new FullRunningAverageAndStdDev();
    for (Preference pref : prefs) {
      stdDev.addDatum(pref.getValue());
    }
    return stdDev.getAverage() + stdDev.getStandardDeviation();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/593.java