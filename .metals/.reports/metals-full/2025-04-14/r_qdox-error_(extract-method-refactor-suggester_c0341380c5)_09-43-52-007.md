error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1269.java
text:
```scala
i@@f (userIDs.length > 3) {

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

package org.apache.mahout.cf.taste.impl.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.mahout.cf.taste.common.NoSuchItemException;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveArrayIterator;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

import com.google.common.base.Preconditions;

/**
 * <p>
 * A simple {@link DataModel} which uses given user data as its data source. This implementation
 * is mostly useful for small experiments and is not recommended for contexts where performance is important.
 * </p>
 */
public final class GenericBooleanPrefDataModel extends AbstractDataModel {
  
  private final long[] userIDs;
  private final FastByIDMap<FastIDSet> preferenceFromUsers;
  private final long[] itemIDs;
  private final FastByIDMap<FastIDSet> preferenceForItems;
  private final FastByIDMap<FastByIDMap<Long>> timestamps;
  
  /**
   * <p>
   * Creates a new {@link GenericDataModel} from the given users (and their preferences). This
   * {@link DataModel} retains all this information in memory and is effectively immutable.
   * </p>
   * 
   * @param userData users to include
   */
  public GenericBooleanPrefDataModel(FastByIDMap<FastIDSet> userData) {
    this(userData, null);
  }

  /**
   * <p>
   * Creates a new {@link GenericDataModel} from the given users (and their preferences). This
   * {@link DataModel} retains all this information in memory and is effectively immutable.
   * </p>
   *
   * @param userData users to include
   * @param timestamps optionally, provided timestamps of preferences as milliseconds since the epoch.
   *  User IDs are mapped to maps of item IDs to Long timestamps.
   */
  public GenericBooleanPrefDataModel(FastByIDMap<FastIDSet> userData, FastByIDMap<FastByIDMap<Long>> timestamps) {
    Preconditions.checkArgument(userData != null, "userData is null");

    this.preferenceFromUsers = userData;
    this.preferenceForItems = new FastByIDMap<FastIDSet>();
    FastIDSet itemIDSet = new FastIDSet();
    for (Map.Entry<Long, FastIDSet> entry : preferenceFromUsers.entrySet()) {
      long userID = entry.getKey();
      FastIDSet itemIDs = entry.getValue();
      itemIDSet.addAll(itemIDs);
      LongPrimitiveIterator it = itemIDs.iterator();
      while (it.hasNext()) {
        long itemID = it.nextLong();
        FastIDSet userIDs = preferenceForItems.get(itemID);
        if (userIDs == null) {
          userIDs = new FastIDSet(2);
          preferenceForItems.put(itemID, userIDs);
        }
        userIDs.add(userID);
      }
    }

    this.itemIDs = itemIDSet.toArray();
    itemIDSet = null; // Might help GC -- this is big
    Arrays.sort(itemIDs);

    this.userIDs = new long[userData.size()];
    int i = 0;
    LongPrimitiveIterator it = userData.keySetIterator();
    while (it.hasNext()) {
      userIDs[i++] = it.next();
    }
    Arrays.sort(userIDs);

    this.timestamps = timestamps;
  }
  
  /**
   * <p>
   * Creates a new {@link GenericDataModel} containing an immutable copy of the data from another given
   * {@link DataModel}.
   * </p>
   * 
   * @param dataModel
   *          {@link DataModel} to copy
   * @throws TasteException
   *           if an error occurs while retrieving the other {@link DataModel}'s users
   * @deprecated without direct replacement.
   *  Consider {@link #toDataMap(DataModel)} with {@link #GenericBooleanPrefDataModel(FastByIDMap)}
   */
  @Deprecated
  public GenericBooleanPrefDataModel(DataModel dataModel) throws TasteException {
    this(toDataMap(dataModel));
  }

  /**
   * Exports the simple user IDs and associated item IDs in the data model.
   *
   * @return a {@link FastByIDMap} mapping user IDs to {@link FastIDSet}s representing
   *  that user's associated items
   */
  public static FastByIDMap<FastIDSet> toDataMap(DataModel dataModel) throws TasteException {
    FastByIDMap<FastIDSet> data = new FastByIDMap<FastIDSet>(dataModel.getNumUsers());
    LongPrimitiveIterator it = dataModel.getUserIDs();
    while (it.hasNext()) {
      long userID = it.nextLong();
      data.put(userID, dataModel.getItemIDsFromUser(userID));
    }
    return data;
  }

  public static FastByIDMap<FastIDSet> toDataMap(FastByIDMap<PreferenceArray> data) {
    for (Map.Entry<Long,Object> entry : ((FastByIDMap<Object>) (FastByIDMap<?>) data).entrySet()) {
      PreferenceArray prefArray = (PreferenceArray) entry.getValue();
      int size = prefArray.length();
      FastIDSet itemIDs = new FastIDSet(size);
      for (int i = 0; i < size; i++) {
        itemIDs.add(prefArray.getItemID(i));
      }
      entry.setValue(itemIDs);
    }
    return (FastByIDMap<FastIDSet>) (FastByIDMap<?>) data;
  }
  
  /**
   * This is used mostly internally to the framework, and shouldn't be relied upon otherwise.
   */
  public FastByIDMap<FastIDSet> getRawUserData() {
    return this.preferenceFromUsers;
  }

  /**
   * This is used mostly internally to the framework, and shouldn't be relied upon otherwise.
   */
  public FastByIDMap<FastIDSet> getRawItemData() {
    return this.preferenceForItems;
  }
  
  @Override
  public LongPrimitiveArrayIterator getUserIDs() {
    return new LongPrimitiveArrayIterator(userIDs);
  }
  
  /**
   * @throws NoSuchUserException
   *           if there is no such user
   */
  @Override
  public PreferenceArray getPreferencesFromUser(long userID) throws NoSuchUserException {
    FastIDSet itemIDs = preferenceFromUsers.get(userID);
    if (itemIDs == null) {
      throw new NoSuchUserException(userID);
    }
    PreferenceArray prefArray = new BooleanUserPreferenceArray(itemIDs.size());
    int i = 0;
    LongPrimitiveIterator it = itemIDs.iterator();
    while (it.hasNext()) {
      prefArray.setUserID(i, userID);
      prefArray.setItemID(i, it.nextLong());
      i++;
    }
    return prefArray;
  }
  
  @Override
  public FastIDSet getItemIDsFromUser(long userID) throws TasteException {
    FastIDSet itemIDs = preferenceFromUsers.get(userID);
    if (itemIDs == null) {
      throw new NoSuchUserException(userID);
    }
    return itemIDs;
  }
  
  @Override
  public LongPrimitiveArrayIterator getItemIDs() {
    return new LongPrimitiveArrayIterator(itemIDs);
  }
  
  @Override
  public PreferenceArray getPreferencesForItem(long itemID) throws NoSuchItemException {
    FastIDSet userIDs = preferenceForItems.get(itemID);
    if (userIDs == null) {
      throw new NoSuchItemException(itemID);
    }
    PreferenceArray prefArray = new BooleanItemPreferenceArray(userIDs.size());
    int i = 0;
    LongPrimitiveIterator it = userIDs.iterator();
    while (it.hasNext()) {
      prefArray.setUserID(i, it.nextLong());
      prefArray.setItemID(i, itemID);
      i++;
    }
    return prefArray;
  }
  
  @Override
  public Float getPreferenceValue(long userID, long itemID) throws NoSuchUserException {
    FastIDSet itemIDs = preferenceFromUsers.get(userID);
    if (itemIDs == null) {
      throw new NoSuchUserException(userID);
    }
    if (itemIDs.contains(itemID)) {
      return 1.0f;
    }
    return null;
  }

  @Override
  public Long getPreferenceTime(long userID, long itemID) throws TasteException {
    if (timestamps == null) {
      return null;
    }
    FastByIDMap<Long> itemTimestamps = timestamps.get(userID);
    if (itemTimestamps == null) {
      throw new NoSuchUserException(userID);
    }
    return itemTimestamps.get(itemID);
  }
  
  @Override
  public int getNumItems() {
    return itemIDs.length;
  }
  
  @Override
  public int getNumUsers() {
    return userIDs.length;
  }
  
  @Override
  public int getNumUsersWithPreferenceFor(long itemID) {
    FastIDSet userIDs1 = preferenceForItems.get(itemID);
    return userIDs1 == null ? 0 : userIDs1.size();
  }

  @Override
  public int getNumUsersWithPreferenceFor(long itemID1, long itemID2) {
    FastIDSet userIDs1 = preferenceForItems.get(itemID1);
    if (userIDs1 == null) {
      return 0;
    }
    FastIDSet userIDs2 = preferenceForItems.get(itemID2);
    if (userIDs2 == null) {
      return 0;
    }
    return userIDs1.intersectionSize(userIDs2);
  }
  
  @Override
  public void removePreference(long userID, long itemID) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void setPreference(long userID, long itemID, float value) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void refresh(Collection<Refreshable> alreadyRefreshed) {
  // Does nothing
  }

  @Override
  public boolean hasPreferenceValues() {
    return false;
  }
  
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(200);
    result.append("GenericBooleanPrefDataModel[users:");
    for (int i = 0; i < Math.min(3, userIDs.length); i++) {
      if (i > 0) {
        result.append(',');
      }
      result.append(userIDs[i]);
    }
    if (result.length() > 3) {
      result.append("...");
    }
    result.append(']');
    return result.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1269.java