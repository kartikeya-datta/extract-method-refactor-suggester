error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/556.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/556.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/556.java
text:
```scala
r@@eturn BytesRef.deepCopyOf(bytes);

package org.apache.solr.schema;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.collation.ICUCollationKeyAnalyzer;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.apache.solr.common.ResourceLoader;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.response.TextResponseWriter;
import org.apache.solr.search.QParser;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.util.ULocale;

/**
 * Field for collated sort keys. 
 * These can be used for locale-sensitive sort and range queries.
 * <p>
 * This field can be created in two ways: 
 * <ul>
 *  <li>Based upon a system collator associated with a Locale.
 *  <li>Based upon a tailored ruleset.
 * </ul>
 * <p>
 * Using a System collator:
 * <ul>
 *  <li>locale: RFC 3066 locale ID (mandatory)
 *  <li>strength: 'primary','secondary','tertiary', 'quaternary', or 'identical' (optional)
 *  <li>decomposition: 'no', or 'canonical' (optional)
 * </ul>
 * <p>
 * Using a Tailored ruleset:
 * <ul>
 *  <li>custom: UTF-8 text file containing rules supported by RuleBasedCollator (mandatory)
 *  <li>strength: 'primary','secondary','tertiary', 'quaternary', or 'identical' (optional)
 *  <li>decomposition: 'no' or 'canonical' (optional)
 * </ul>
 *
 * @see Collator
 * @see ULocale
 * @see RuleBasedCollator
 */
public class ICUCollationField extends FieldType {
  private Analyzer analyzer;

  @Override
  protected void init(IndexSchema schema, Map<String,String> args) {
    properties |= TOKENIZED; // this ensures our analyzer gets hit
    setup(schema.getResourceLoader(), args);
    super.init(schema, args);
  }
  
  /**
   * Setup the field according to the provided parameters
   */
  private void setup(ResourceLoader loader, Map<String,String> args) {
    String custom = args.remove("custom");
    String localeID = args.remove("locale");
    String strength = args.remove("strength");
    String decomposition = args.remove("decomposition");
    
    if (custom == null && localeID == null)
      throw new SolrException(ErrorCode.SERVER_ERROR, "Either custom or locale is required.");
    
    if (custom != null && localeID != null)
      throw new SolrException(ErrorCode.SERVER_ERROR, "Cannot specify both locale and custom. "
          + "To tailor rules for a built-in language, see the javadocs for RuleBasedCollator. "
          + "Then save the entire customized ruleset to a file, and use with the custom parameter");
    
    final Collator collator;
    
    if (localeID != null) { 
      // create from a system collator, based on Locale.
      collator = createFromLocale(localeID);
    } else { 
      // create from a custom ruleset
      collator = createFromRules(custom, loader);
    }
    
    // set the strength flag, otherwise it will be the default.
    if (strength != null) {
      if (strength.equalsIgnoreCase("primary"))
        collator.setStrength(Collator.PRIMARY);
      else if (strength.equalsIgnoreCase("secondary"))
        collator.setStrength(Collator.SECONDARY);
      else if (strength.equalsIgnoreCase("tertiary"))
        collator.setStrength(Collator.TERTIARY);
      else if (strength.equalsIgnoreCase("quaternary"))
        collator.setStrength(Collator.QUATERNARY);
      else if (strength.equalsIgnoreCase("identical"))
        collator.setStrength(Collator.IDENTICAL);
      else
        throw new SolrException(ErrorCode.SERVER_ERROR, "Invalid strength: " + strength);
    }
    
    // set the decomposition flag, otherwise it will be the default.
    if (decomposition != null) {
      if (decomposition.equalsIgnoreCase("no"))
        collator.setDecomposition(Collator.NO_DECOMPOSITION);
      else if (decomposition.equalsIgnoreCase("canonical"))
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
      else
        throw new SolrException(ErrorCode.SERVER_ERROR, "Invalid decomposition: " + decomposition);
    }
    // we use 4.0 because it ensures we just encode the pure byte[] keys.
    analyzer = new ICUCollationKeyAnalyzer(Version.LUCENE_40, collator);
  }
  
  /**
   * Create a locale from localeID.
   * Then return the appropriate collator for the locale.
   */
  private Collator createFromLocale(String localeID) {
    return Collator.getInstance(new ULocale(localeID));
  }
  
  /**
   * Read custom rules from a file, and create a RuleBasedCollator
   * The file cannot support comments, as # might be in the rules!
   */
  private Collator createFromRules(String fileName, ResourceLoader loader) {
    InputStream input = null;
    try {
     input = loader.openResource(fileName);
     String rules = IOUtils.toString(input, "UTF-8");
     return new RuleBasedCollator(rules);
    } catch (Exception e) {
      // io error or invalid rules
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(input);
    }
  }

  @Override
  public void write(TextResponseWriter writer, String name, IndexableField f) throws IOException {
    writer.writeStr(name, f.stringValue(), true);
  }

  @Override
  public SortField getSortField(SchemaField field, boolean top) {
    return getStringSort(field, top);
  }

  @Override
  public Analyzer getAnalyzer() {
    return analyzer;
  }

  @Override
  public Analyzer getQueryAnalyzer() {
    return analyzer;
  }

  /**
   * analyze the range with the analyzer, instead of the collator.
   * because icu collators are not thread safe, this keeps things 
   * simple (we already have a threadlocal clone in the reused TS)
   */
  private BytesRef analyzeRangePart(String field, String part) {
    TokenStream source;
      
    try {
      source = analyzer.tokenStream(field, new StringReader(part));
      source.reset();
    } catch (IOException e) {
      throw new RuntimeException("Unable to initialize TokenStream to analyze range part: " + part, e);
    }
      
    TermToBytesRefAttribute termAtt = source.getAttribute(TermToBytesRefAttribute.class);
    BytesRef bytes = termAtt.getBytesRef();

    // we control the analyzer here: most errors are impossible
    try {
      if (!source.incrementToken())
        throw new IllegalArgumentException("analyzer returned no terms for range part: " + part);
      termAtt.fillBytesRef();
      assert !source.incrementToken();
    } catch (IOException e) {
      throw new RuntimeException("error analyzing range part: " + part, e);
    }
      
    try {
      source.end();
      source.close();
    } catch (IOException e) {
      throw new RuntimeException("Unable to end & close TokenStream after analyzing range part: " + part, e);
    }
      
    return new BytesRef(bytes);
  }
  
  @Override
  public Query getRangeQuery(QParser parser, SchemaField field, String part1, String part2, boolean minInclusive, boolean maxInclusive) {
    String f = field.getName();
    BytesRef low = part1 == null ? null : analyzeRangePart(f, part1);
    BytesRef high = part2 == null ? null : analyzeRangePart(f, part2);
    return new TermRangeQuery(field.getName(), low, high, minInclusive, maxInclusive);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/556.java