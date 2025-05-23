error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/386.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/386.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/386.java
text:
```scala
public final b@@oolean incrementToken() {

package org.apache.solr.schema;
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
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.AttributeSource.State;
import org.apache.solr.analysis.SolrAnalyzer;
import org.apache.solr.response.TextResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pre-analyzed field type provides a way to index a serialized token stream,
 * optionally with an independent stored value of a field.
 */
public class PreAnalyzedField extends FieldType {
  private static final Logger LOG = LoggerFactory.getLogger(PreAnalyzedField.class);

  /** Init argument name. Value is a fully-qualified class name of the parser
   * that implements {@link PreAnalyzedParser}.
   */
  public static final String PARSER_IMPL = "parserImpl";
  
  private static final String DEFAULT_IMPL = JsonPreAnalyzedParser.class.getName();

  
  private PreAnalyzedParser parser;
  
  @Override
  protected void init(IndexSchema schema, Map<String, String> args) {
    super.init(schema, args);
    String implName = args.get(PARSER_IMPL);
    if (implName == null) {
      parser = new JsonPreAnalyzedParser();
    } else {
      try {
        Class<?> implClazz = Class.forName(implName);
        if (!PreAnalyzedParser.class.isAssignableFrom(implClazz)) {
          throw new Exception("must implement " + PreAnalyzedParser.class.getName());
        }
        Constructor<?> c = implClazz.getConstructor(new Class<?>[0]);
        parser = (PreAnalyzedParser) c.newInstance(new Object[0]);
      } catch (Exception e) {
        LOG.warn("Can't use the configured PreAnalyzedParser class '" + implName + "' (" +
            e.getMessage() + "), using default " + DEFAULT_IMPL);
        parser = new JsonPreAnalyzedParser();
      }
    }
  }

  @Override
  public Analyzer getAnalyzer() {
    return new SolrAnalyzer() {
      
      @Override
      protected TokenStreamComponents createComponents(String fieldName,
          Reader reader) {
        try {
          return new TokenStreamComponents(new PreAnalyzedTokenizer(reader, parser));
        } catch (IOException e) {
          return null;
        }
      }
      
    };
  }
  
  @Override
  public Analyzer getQueryAnalyzer() {
    return getAnalyzer();
  }

  @Override
  public IndexableField createField(SchemaField field, Object value,
          float boost) {
    IndexableField f = null;
    try {
      f = fromString(field, String.valueOf(value), boost);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return f;
  }

  @Override
  public SortField getSortField(SchemaField field, boolean top) {
    return getStringSort(field, top);
  }

  @Override
  public void write(TextResponseWriter writer, String name, IndexableField f)
          throws IOException {
    writer.writeStr(name, f.stringValue(), true);
  }
  
  /** Utility method to convert a field to a string that is parse-able by this
   * class.
   * @param f field to convert
   * @return string that is compatible with the serialization format
   * @throws IOException
   */
  public String toFormattedString(Field f) throws IOException {
    return parser.toFormattedString(f);
  }
  
  /**
   * This is a simple holder of a stored part and the collected states (tokens with attributes).
   */
  public static class ParseResult {
    public String str;
    public byte[] bin;
    public List<State> states = new LinkedList<State>();
  }
  
  /**
   * Parse the input and return the stored part and the tokens with attributes.
   */
  public static interface PreAnalyzedParser {
    /**
     * Parse input.
     * @param reader input to read from
     * @param parent parent who will own the resulting states (tokens with attributes)
     * @return parse result, with possibly null stored and/or states fields.
     * @throws IOException if a parsing error or IO error occurs
     */
    public ParseResult parse(Reader reader, AttributeSource parent) throws IOException;
    
    /**
     * Format a field so that the resulting String is valid for parsing with {@link #parse(Reader, AttributeSource)}.
     * @param f field instance
     * @return formatted string
     * @throws IOException
     */
    public String toFormattedString(Field f) throws IOException;
  }
  
  
  public IndexableField fromString(SchemaField field, String val, float boost) throws Exception {
    if (val == null || val.trim().length() == 0) {
      return null;
    }
    PreAnalyzedTokenizer parse = new PreAnalyzedTokenizer(new StringReader(val), parser);
    Field f = (Field)super.createField(field, val, boost);
    if (parse.getStringValue() != null) {
      f.setStringValue(parse.getStringValue());
    } else if (parse.getBinaryValue() != null) {
      f.setBytesValue(parse.getBinaryValue());
    } else {
      f.fieldType().setStored(false);
    }
    
    if (parse.hasTokenStream()) {
      f.fieldType().setIndexed(true);
      f.fieldType().setTokenized(true);
      f.setTokenStream(parse);
    }
    return f;
  }
    
  /**
   * Token stream that works from a list of saved states.
   */
  private static class PreAnalyzedTokenizer extends Tokenizer {
    private final List<AttributeSource.State> cachedStates = new LinkedList<AttributeSource.State>();
    private Iterator<AttributeSource.State> it = null;
    private String stringValue = null;
    private byte[] binaryValue = null;
    private PreAnalyzedParser parser;
    
    public PreAnalyzedTokenizer(Reader reader, PreAnalyzedParser parser) throws IOException {
      super(reader);
      this.parser = parser;
      reset(reader);
    }
    
    public boolean hasTokenStream() {
      return !cachedStates.isEmpty();
    }
    
    public String getStringValue() {
      return stringValue;
    }
    
    public byte[] getBinaryValue() {
      return binaryValue;
    }
    
    public final boolean incrementToken() throws IOException {
      // lazy init the iterator
      if (it == null) {
        it = cachedStates.iterator();
      }
    
      if (!it.hasNext()) {
        return false;
      }
      
      AttributeSource.State state = (State) it.next();
      restoreState(state.clone());
      return true;
    }
  
    public final void reset() {
      it = cachedStates.iterator();
    }

    @Override
    public void reset(Reader input) throws IOException {
      super.reset(input);
      cachedStates.clear();
      stringValue = null;
      binaryValue = null;
      ParseResult res = parser.parse(input, this);
      if (res != null) {
        stringValue = res.str;
        binaryValue = res.bin;
        if (res.states != null) {
          cachedStates.addAll(res.states);
        }
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/386.java