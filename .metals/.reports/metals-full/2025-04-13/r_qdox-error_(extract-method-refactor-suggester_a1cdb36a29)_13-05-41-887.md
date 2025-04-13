error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/935.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/935.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/935.java
text:
```scala
I@@nteger ord = classes.get(ARFFType.removeQuotes(data));

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

package org.apache.mahout.utils.vectors.arff;

import com.google.common.collect.Maps;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Holds ARFF information in {@link Map}.
 */
public class MapBackedARFFModel implements ARFFModel {
  
  private static final Pattern QUOTE_PATTERN = Pattern.compile("\"");
  
  private long wordCount = 1;
  
  private String relation;
  
  private final Map<String,Integer> labelBindings;
  private final Map<Integer,String> idxLabel;
  private final Map<Integer,ARFFType> typeMap; // key is the vector index, value is the type
  private final Map<Integer,DateFormat> dateMap;
  private final Map<String,Map<String,Integer>> nominalMap;
  private final Map<String,Long> words;
  
  public MapBackedARFFModel() {
    this(new HashMap<String,Long>(), 1, new HashMap<String,Map<String,Integer>>());
  }
  
  public MapBackedARFFModel(Map<String,Long> words, long wordCount, Map<String,Map<String,Integer>> nominalMap) {
    this.words = words;
    this.wordCount = wordCount;
    labelBindings = Maps.newHashMap();
    idxLabel = Maps.newHashMap();
    typeMap = Maps.newHashMap();
    dateMap = Maps.newHashMap();
    this.nominalMap = nominalMap;
    
  }
  
  @Override
  public String getRelation() {
    return relation;
  }
  
  @Override
  public void setRelation(String relation) {
    this.relation = relation;
  }
  
  /**
   * Convert a piece of String data at a specific spot into a value
   * 
   * @param data
   *          The data to convert
   * @param idx
   *          The position in the ARFF data
   * @return A double representing the data
   */
  @Override
  public double getValue(String data, int idx) {
    ARFFType type = typeMap.get(idx);
    if (type == null) {
      throw new IllegalArgumentException("Attribute type cannot be NULL, attribute index was: " + idx);
    }
    data = QUOTE_PATTERN.matcher(data).replaceAll("");
    data = data.trim();
    double result;
    switch (type) {
      case NUMERIC:
      case INTEGER:
      case REAL:
        result = processNumeric(data);
        break;
      case DATE:
        result = processDate(data, idx);
        break;
      case STRING:
        // may have quotes
        result = processString(data);
        break;
      case NOMINAL:
        String label = idxLabel.get(idx);
        result = processNominal(label, data);
        break;
      default:
        throw new IllegalStateException("Unknown type: " + type);
    }
    return result;
  }
  
  protected double processNominal(String label, String data) {
    double result;
    Map<String,Integer> classes = nominalMap.get(label);
    if (classes != null) {
      Integer ord = classes.get(data);
      if (ord != null) {
        result = ord;
      } else {
        throw new IllegalStateException("Invalid nominal: " + data + " for label: " + label);
      }
    } else {
      throw new IllegalArgumentException("Invalid nominal label: " + label + " Data: " + data);
    }
    
    return result;
  }

  // Not sure how scalable this is going to be
  protected double processString(String data) {
    data = QUOTE_PATTERN.matcher(data).replaceAll("");
    // map it to an long
    Long theLong = words.get(data);
    if (theLong == null) {
      theLong = wordCount++;
      words.put(data, theLong);
    }
    return theLong;
  }
  
  protected static double processNumeric(String data) {
    return Double.parseDouble(data);
  }
  
  protected double processDate(String data, int idx) {
    DateFormat format = dateMap.get(idx);
    if (format == null) {
      format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    }
    double result;
    try {
      Date date = format.parse(data);
      result = date.getTime(); // hmmm, what kind of loss casting long to double?
    } catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }
    return result;
  }
  
  /**
   * The vector attributes (labels in Mahout speak), unmodifiable
   * 
   * @return the map
   */
  @Override
  public Map<String,Integer> getLabelBindings() {
    return Collections.unmodifiableMap(labelBindings);
  }
  
  /**
   * The map of types encountered
   * 
   * @return the map
   */
  public Map<Integer,ARFFType> getTypeMap() {
    return Collections.unmodifiableMap(typeMap);
  }
  
  /**
   * Map of Date formatters used
   * 
   * @return the map
   */
  public Map<Integer,DateFormat> getDateMap() {
    return Collections.unmodifiableMap(dateMap);
  }
  
  /**
   * Map nominals to ids. Should only be modified by calling {@link ARFFModel#addNominal(String, String, int)}
   * 
   * @return the map
   */
  @Override
  public Map<String,Map<String,Integer>> getNominalMap() {
    return nominalMap;
  }
  
  /**
   * Immutable map of words to the long id used for those words
   * 
   * @return The map
   */
  @Override
  public Map<String,Long> getWords() {
    return words;
  }
  
  @Override
  public Integer getNominalValue(String label, String nominal) {
    return nominalMap.get(label).get(nominal);
  }
  
  @Override
  public void addNominal(String label, String nominal, int idx) {
    Map<String,Integer> noms = nominalMap.get(label);
    if (noms == null) {
      noms = Maps.newHashMap();
      nominalMap.put(label, noms);
    }
    noms.put(nominal, idx);
  }
  
  @Override
  public DateFormat getDateFormat(Integer idx) {
    return dateMap.get(idx);
  }
  
  @Override
  public void addDateFormat(Integer idx, DateFormat format) {
    dateMap.put(idx, format);
  }
  
  @Override
  public Integer getLabelIndex(String label) {
    return labelBindings.get(label);
  }
  
  @Override
  public void addLabel(String label, Integer idx) {
    labelBindings.put(label, idx);
    idxLabel.put(idx, label);
  }
  
  @Override
  public ARFFType getARFFType(Integer idx) {
    return typeMap.get(idx);
  }
  
  @Override
  public void addType(Integer idx, ARFFType type) {
    typeMap.put(idx, type);
  }
  
  /**
   * The count of the number of words seen
   * 
   * @return the count
   */
  @Override
  public long getWordCount() {
    return wordCount;
  }
  
  @Override
  public int getLabelSize() {
    return labelBindings.size();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/935.java