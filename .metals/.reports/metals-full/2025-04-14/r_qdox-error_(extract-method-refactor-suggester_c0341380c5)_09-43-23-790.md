error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3229.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3229.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3229.java
text:
```scala
public v@@oid build(SolrCore core, SolrIndexSearcher searcher) throws IOException {

package org.apache.solr.spelling;

/*
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.search.spell.StringDistance;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.SpellCheckMergeData;
import org.apache.solr.search.SolrIndexSearcher;

/**
 * <p>This class lets a query be run through multiple spell checkers.
 *    The initial use-case is to use {@link WordBreakSolrSpellChecker}
 *    in conjunction with a "standard" spell checker 
 *    (such as {@link DirectSolrSpellChecker}
 *  </p>
 */
public class ConjunctionSolrSpellChecker extends SolrSpellChecker {
  private StringDistance stringDistance = null;
  private Float accuracy = null;
  private String dictionaryName = null;
  private Analyzer queryAnalyzer = null;
  private List<SolrSpellChecker> checkers = new ArrayList<SolrSpellChecker>();
  private boolean initalized = false;
  
  public void addChecker(SolrSpellChecker checker) {
    if (initalized) {
      throw new IllegalStateException(
          "Need to add checkers before calling init()");
    }
    try {
      if (stringDistance == null) {
        stringDistance = checker.getStringDistance();
      } else if (stringDistance != checker.getStringDistance()) {
        throw new IllegalArgumentException(
            "All checkers need to use the same StringDistance.");
      }
    } catch (UnsupportedOperationException uoe) {
      // ignore
    }
    try {
      if (accuracy == null) {
        accuracy = checker.getAccuracy();
      } else if (accuracy != checker.getAccuracy()) {
        throw new IllegalArgumentException(
            "All checkers need to use the same Accuracy.");
      }
    } catch (UnsupportedOperationException uoe) {
      // ignore
    }
    if (queryAnalyzer == null) {
      queryAnalyzer = checker.getQueryAnalyzer();
    } else if (queryAnalyzer != checker.getQueryAnalyzer()) {
      throw new IllegalArgumentException(
          "All checkers need to use the same Analyzer.");
    }
    checkers.add(checker);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public String init(NamedList config, SolrCore core) {
    for (int i = 0; i < checkers.size(); i++) {
      SolrSpellChecker c = checkers.get(i);
      String dn = c.init(config, core);
      
      //TODO:  in the future, we could develop this further to allow
      //        multiple spellcheckers with per-field dictionaries...
      if (dictionaryName != null && !dictionaryName.equals(dn)) {
        throw new IllegalArgumentException(
            "Cannot have more than one dictionary. (" + dn + " , "
                + dictionaryName + ")");
      }
      dictionaryName = dn;
    }
    if (dictionaryName == null) {
      dictionaryName = DEFAULT_DICTIONARY_NAME;
    }
    initalized = true;
    return dictionaryName;
  }
  
  @Override
  public void build(SolrCore core, SolrIndexSearcher searcher) {
    for (SolrSpellChecker c : checkers) {
      c.build(core, searcher);
    }
  }
  
  @Override
  public SpellingResult getSuggestions(SpellingOptions options)
      throws IOException {
    SpellingResult[] results = new SpellingResult[checkers.size()];
    for (int i = 0; i < checkers.size(); i++) {
      results[i] = checkers.get(i).getSuggestions(options);
    }
    return mergeCheckers(results, options.count);
  }
  
  @Override
  public SpellingResult mergeSuggestions(SpellCheckMergeData mergeData,
      int numSug, int count, boolean extendedResults) {
    SpellingResult[] results = new SpellingResult[checkers.size()];
    for (int i = 0; i < checkers.size(); i++) {
      results[i] = checkers.get(i).mergeSuggestions(mergeData, numSug, count,
          extendedResults);
    }
    return mergeCheckers(results, numSug);
  }
  
  //TODO: This just interleaves the results.  In the future, we might want to let users give each checker its
  //      own weight and use that in combination to score & frequency to sort the results ?
  private SpellingResult mergeCheckers(SpellingResult[] results, int numSug) {
    Map<Token, Integer> combinedTokenFrequency = new HashMap<Token, Integer>();
    Map<Token, List<LinkedHashMap<String, Integer>>> allSuggestions = new LinkedHashMap<Token, List<LinkedHashMap<String, Integer>>>();
    for(SpellingResult result : results) {
    	if(result.getTokenFrequency()!=null) {
        combinedTokenFrequency.putAll(result.getTokenFrequency());
      }
      for(Map.Entry<Token, LinkedHashMap<String, Integer>> entry : result.getSuggestions().entrySet()) {
        List<LinkedHashMap<String, Integer>> allForThisToken = allSuggestions.get(entry.getKey());
        if(allForThisToken==null) {
          allForThisToken = new ArrayList<LinkedHashMap<String, Integer>>();
          allSuggestions.put(entry.getKey(), allForThisToken);
        }
        allForThisToken.add(entry.getValue());
      }
    }    
    SpellingResult combinedResult = new SpellingResult();    
    for(Map.Entry<Token, List<LinkedHashMap<String, Integer>>> entry : allSuggestions.entrySet()) {
      Token original = entry.getKey();      
      List<Iterator<Map.Entry<String,Integer>>> corrIters = new ArrayList<Iterator<Map.Entry<String,Integer>>>(entry.getValue().size());
      for(LinkedHashMap<String, Integer> corrections : entry.getValue()) {
        corrIters.add(corrections.entrySet().iterator());
      }        
      int numberAdded = 0;
      while(numberAdded < numSug) {
        boolean anyData = false;
        for(Iterator<Map.Entry<String,Integer>> iter : corrIters) {
          if(iter.hasNext()) {
            anyData = true;
            Map.Entry<String,Integer> corr = iter.next();
            combinedResult.add(original, corr.getKey(), corr.getValue());
            Integer tokenFrequency = combinedTokenFrequency.get(original);
            if(tokenFrequency!=null) {
              combinedResult.addFrequency(original, tokenFrequency);
            }
            if(++numberAdded==numSug) {
              break;
            }
          }
        }        
        if(!anyData) {
          break;
        }
      }      
    }    
    return combinedResult;
  }
  
  @Override
  public void reload(SolrCore core, SolrIndexSearcher searcher)
      throws IOException {
    for (SolrSpellChecker c : checkers) {
      c.reload(core, searcher);
    }
  }
  
  @Override
  public Analyzer getQueryAnalyzer() {
    return queryAnalyzer;
  }
  
  @Override
  public String getDictionaryName() {
    return dictionaryName;
  }
  
  @Override
  protected float getAccuracy() {
    if (accuracy == null) {
      return super.getAccuracy();
    }
    return accuracy;
  }
  
  @Override
  protected StringDistance getStringDistance() {
    if (stringDistance == null) {
      return super.getStringDistance();
    }
    return stringDistance;
  }
  
  @Override
  public boolean isSuggestionsMayOverlap() {
    return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3229.java