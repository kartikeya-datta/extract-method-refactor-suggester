error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2834.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2834.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2834.java
text:
```scala
r@@esult.addFrequency(token, reader.docFreq(term));

package org.apache.solr.spelling;

import org.apache.lucene.search.spell.StringDistance;
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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.search.spell.SuggestWord;
import org.apache.lucene.search.spell.SuggestWordFrequencyComparator;
import org.apache.lucene.search.spell.SuggestWordQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.solr.common.params.ShardParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.FieldType;
import org.apache.solr.search.SolrIndexSearcher;


/**
 * Abstract base class for all Lucene-based spell checking implementations.
 * 
 * <p>
 * Refer to <a href="http://wiki.apache.org/solr/SpellCheckComponent">SpellCheckComponent</a>
 * for more details.
 * </p>
 * 
 * @since solr 1.3
 */
public abstract class AbstractLuceneSpellChecker extends SolrSpellChecker {
  public static final Logger log = LoggerFactory.getLogger(AbstractLuceneSpellChecker.class);
  
  public static final String SPELLCHECKER_ARG_NAME = "spellchecker";
  public static final String LOCATION = "sourceLocation";
  public static final String INDEX_DIR = "spellcheckIndexDir";
  public static final String ACCURACY = "accuracy";
  public static final String STRING_DISTANCE = "distanceMeasure";
  public static final String FIELD_TYPE = "fieldType";
  public static final String COMPARATOR_CLASS = "comparatorClass";

  public static final String SCORE_COMP = "score";
  public static final String FREQ_COMP = "freq";

  protected String field;
  protected String fieldTypeName;
  protected org.apache.lucene.search.spell.SpellChecker spellChecker;

  protected String sourceLocation;
  /*
  * The Directory containing the Spell checking index
  * */
  protected Directory index;
  protected Dictionary dictionary;

  public static final int DEFAULT_SUGGESTION_COUNT = 5;
  protected String indexDir;
  protected float accuracy = 0.5f;
  public static final String FIELD = "field";

  protected StringDistance sd;

  @Override
  public String init(NamedList config, SolrCore core) {
    super.init(config, core);
    indexDir = (String) config.get(INDEX_DIR);
    String accuracy = (String) config.get(ACCURACY);
    //If indexDir is relative then create index inside core.getDataDir()
    if (indexDir != null)   {
      if (!new File(indexDir).isAbsolute()) {
        indexDir = core.getDataDir() + File.separator + indexDir;
      }
    }
    sourceLocation = (String) config.get(LOCATION);
    String compClass = (String) config.get(COMPARATOR_CLASS);
    Comparator<SuggestWord> comp = null;
    if (compClass != null){
      if (compClass.equalsIgnoreCase(SCORE_COMP)){
        comp = SuggestWordQueue.DEFAULT_COMPARATOR;
      } else if (compClass.equalsIgnoreCase(FREQ_COMP)){
        comp = new SuggestWordFrequencyComparator();
      } else{//must be a FQCN
        comp = (Comparator<SuggestWord>) core.getResourceLoader().newInstance(compClass);
      }
    } else {
      comp = SuggestWordQueue.DEFAULT_COMPARATOR;
    }
    field = (String) config.get(FIELD);
    String strDistanceName = (String)config.get(STRING_DISTANCE);
    if (strDistanceName != null) {
      sd = (StringDistance) core.getResourceLoader().newInstance(strDistanceName);
      //TODO: Figure out how to configure options.  Where's Spring when you need it?  Or at least BeanUtils...
    } else {
      sd = new LevensteinDistance();
    }
    try {
      initIndex();
      spellChecker = new SpellChecker(index, sd, comp);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (accuracy != null) {
      try {
        this.accuracy = Float.parseFloat(accuracy);
        spellChecker.setAccuracy(this.accuracy);
      } catch (NumberFormatException e) {
        throw new RuntimeException(
                "Unparseable accuracy given for dictionary: " + name, e);
      }
    }
    if (field != null && core.getSchema().getFieldTypeNoEx(field) != null)  {
      analyzer = core.getSchema().getFieldType(field).getQueryAnalyzer();
    }
    fieldTypeName = (String) config.get(FIELD_TYPE);
    if (core.getSchema().getFieldTypes().containsKey(fieldTypeName))  {
      FieldType fieldType = core.getSchema().getFieldTypes().get(fieldTypeName);
      analyzer = fieldType.getQueryAnalyzer();
    }
    if (analyzer == null)   {
      log.info("Using WhitespaceAnalzyer for dictionary: " + name);
      analyzer = new WhitespaceAnalyzer(core.getSolrConfig().luceneMatchVersion);
    }
    return name;
  }

  /**
   * Kept around for back compatibility purposes.
   * 
   * @param tokens          The Tokens to be spell checked.
   * @param reader          The (optional) IndexReader.  If there is not IndexReader, than extendedResults are not possible
   * @param count The maximum number of suggestions to return
   * @param onlyMorePopular  TODO
   * @param extendedResults  TODO
   * @throws IOException
   */
  @Override
  public SpellingResult getSuggestions(Collection<Token> tokens, IndexReader reader, int count, boolean onlyMorePopular, boolean extendedResults) throws IOException {
    return getSuggestions(new SpellingOptions(tokens, reader, count, onlyMorePopular, extendedResults, spellChecker.getAccuracy(), null));
  }

  @Override
  public SpellingResult getSuggestions(SpellingOptions options) throws IOException {
  	boolean shardRequest = false;
  	SolrParams params = options.customParams;
  	if(params!=null)
  	{
  		shardRequest = "true".equals(params.get(ShardParams.IS_SHARD));
  	}
    SpellingResult result = new SpellingResult(options.tokens);
    IndexReader reader = determineReader(options.reader);
    Term term = field != null ? new Term(field, "") : null;
    float theAccuracy = (options.accuracy == Float.MIN_VALUE) ? spellChecker.getAccuracy() : options.accuracy;
    
    int count = Math.max(options.count, AbstractLuceneSpellChecker.DEFAULT_SUGGESTION_COUNT);
    for (Token token : options.tokens) {
      String tokenText = new String(token.buffer(), 0, token.length());
      String[] suggestions = spellChecker.suggestSimilar(tokenText,
              count,
            field != null ? reader : null, //workaround LUCENE-1295
            field,
            options.onlyMorePopular, theAccuracy);
      if (suggestions.length == 1 && suggestions[0].equals(tokenText)) {
      	//These are spelled the same, continue on
        continue;
      }

      if (options.extendedResults == true && reader != null && field != null) {
        term = term.createTerm(tokenText);
        result.add(token, reader.docFreq(term));
        int countLimit = Math.min(options.count, suggestions.length);
        if(countLimit>0)
        {
	        for (int i = 0; i < countLimit; i++) {
	          term = term.createTerm(suggestions[i]);
	          result.add(token, suggestions[i], reader.docFreq(term));
	        }
        } else if(shardRequest) {
        	List<String> suggList = Collections.emptyList();
        	result.add(token, suggList);
        }
      } else {
        if (suggestions.length > 0) {
          List<String> suggList = Arrays.asList(suggestions);
          if (suggestions.length > options.count) {
            suggList = suggList.subList(0, options.count);
          }
          result.add(token, suggList);
        } else if(shardRequest) {
        	List<String> suggList = Collections.emptyList();
        	result.add(token, suggList);
        }
      }
    }
    return result;
  }

  protected IndexReader determineReader(IndexReader reader) {
    return reader;
  }

  @Override
  public void reload(SolrCore core, SolrIndexSearcher searcher) throws IOException {
    spellChecker.setSpellIndex(index);

  }

  /**
   * Initialize the {@link #index} variable based on the {@link #indexDir}.  Does not actually create the spelling index.
   *
   * @throws IOException
   */
  protected void initIndex() throws IOException {
    if (indexDir != null) {
      index = FSDirectory.open(new File(indexDir));
    } else {
      index = new RAMDirectory();
    }
  }

  /*
  * @return the Accuracy used for the Spellchecker
  * */
  public float getAccuracy() {
    return accuracy;
  }

  /*
  * @return the Field used
  *
  * */
  public String getField() {
    return field;
  }

  /*
  *
  * @return the FieldType name.
  * */
  public String getFieldTypeName() {
    return fieldTypeName;
  }


  /*
  * @return the Index directory
  * */
  public String getIndexDir() {
    return indexDir;
  }

  /*
  * @return the location of the source
  * */
  public String getSourceLocation() {
    return sourceLocation;
  }

  public StringDistance getStringDistance() {
    return sd;
  }

  public SpellChecker getSpellChecker() {
    return spellChecker;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2834.java