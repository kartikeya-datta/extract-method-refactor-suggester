error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/843.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/843.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/843.java
text:
```scala
public static final S@@tring NAME = "simple";

package org.apache.solr.search;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SimpleParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.parser.QueryParser;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TextField;
import org.apache.solr.util.SolrPluginUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Create a query from the input value that will be parsed by Lucene's SimpleQueryParser.
 * See {@link org.apache.lucene.queryparser.simple.SimpleQueryParser} for details on the exact syntax allowed
 * to be used for queries.
 * <br>
 * The following options may be applied for parsing the query.
 * <ul>
 *   <li>
 *     q.operations - Used to enable specific operations for parsing.  The operations that can be enabled are
 *                    and, not, or, prefix, phrase, precedence, escape, and whitespace.  By default all operations
 *                    are enabled.  All operations can be disabled by passing in an empty string to this parameter.
 *   </li>
 *   <li>
 *     q.op - Used to specify the operator to be used if whitespace is a delimiter. Either 'AND' or 'OR'
 *            can be specified for this parameter.  Any other string will cause an exception to be thrown.
 *            If this parameter is not specified 'OR' will be used by default.
 *   </li>
 *   <li>
 *     qf - The list of query fields and boosts to use when building the simple query.  The format is the following:
 *          <code>fieldA^1.0 fieldB^2.2</code>.  A field can also be specified without a boost by simply listing the
 *          field as <code>fieldA fieldB</code>.  Any field without a boost will default to use a boost of 1.0.
 *   </li>
 *   <li>
 *     df - An override for the default field specified in the schema or a default field if one is not specified
 *          in the schema.  If qf is not specified the default field will be used as the field to run the query
 *          against.
 *   </li>
 * </ul>
 */
public class SimpleQParserPlugin extends QParserPlugin {
  /** The name that can be used to specify this plugin should be used to parse the query. */
  public static String NAME = "simple";

  /** Map of string operators to their int counterparts in SimpleQueryParser. */
  private static final Map<String, Integer> OPERATORS = new HashMap<String, Integer>();

  /* Setup the map of possible operators. */
  static {
    OPERATORS.put(SimpleParams.AND_OPERATOR,         SimpleQueryParser.AND_OPERATOR);
    OPERATORS.put(SimpleParams.NOT_OPERATOR,         SimpleQueryParser.NOT_OPERATOR);
    OPERATORS.put(SimpleParams.OR_OPERATOR,          SimpleQueryParser.OR_OPERATOR);
    OPERATORS.put(SimpleParams.PREFIX_OPERATOR,      SimpleQueryParser.PREFIX_OPERATOR);
    OPERATORS.put(SimpleParams.PHRASE_OPERATOR,      SimpleQueryParser.PHRASE_OPERATOR);
    OPERATORS.put(SimpleParams.PRECEDENCE_OPERATORS, SimpleQueryParser.PRECEDENCE_OPERATORS);
    OPERATORS.put(SimpleParams.ESCAPE_OPERATOR,      SimpleQueryParser.ESCAPE_OPERATOR);
    OPERATORS.put(SimpleParams.WHITESPACE_OPERATOR,  SimpleQueryParser.WHITESPACE_OPERATOR);
    OPERATORS.put(SimpleParams.FUZZY_OPERATOR,       SimpleQueryParser.FUZZY_OPERATOR);
    OPERATORS.put(SimpleParams.NEAR_OPERATOR,        SimpleQueryParser.NEAR_OPERATOR);
  }

  /** No initialization is necessary so this method is empty. */
  @Override
  public void init(NamedList args) {
  }

  /** Returns a QParser that will create a query by using Lucene's SimpleQueryParser. */
  @Override
  public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
    return new SimpleQParser(qstr, localParams, params, req);
  }

  private static class SimpleQParser extends QParser {
    private SimpleQueryParser parser;

    public SimpleQParser (String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {

      super(qstr, localParams, params, req);
      // Some of the parameters may come in through localParams, so combine them with params.
      SolrParams defaultParams = SolrParams.wrapDefaults(localParams, params);

      // This will be used to specify what fields and boosts will be used by SimpleQueryParser.
      Map<String, Float> queryFields = SolrPluginUtils.parseFieldBoosts(defaultParams.get(SimpleParams.QF));

      if (queryFields.isEmpty()) {
        // It qf is not specified setup up the queryFields map to use the defaultField.
        String defaultField = QueryParsing.getDefaultField(req.getSchema(), defaultParams.get(CommonParams.DF));

        if (defaultField == null) {
          // A query cannot be run without having a field or set of fields to run against.
          throw new IllegalStateException("Neither " + SimpleParams.QF + ", " + CommonParams.DF
              + ", nor the default search field are present.");
        }

        queryFields.put(defaultField, 1.0F);
      }
      else {
        for (Map.Entry<String, Float> queryField : queryFields.entrySet()) {
          if (queryField.getValue() == null) {
            // Some fields may be specified without a boost, so default the boost to 1.0 since a null value
            // will not be accepted by SimpleQueryParser.
            queryField.setValue(1.0F);
          }
        }
      }

      // Setup the operations that are enabled for the query.
      int enabledOps = 0;
      String opParam = defaultParams.get(SimpleParams.QO);

      if (opParam == null) {
        // All operations will be enabled.
        enabledOps = -1;
      } else {
        // Parse the specified enabled operations to be used by the query.
        String[] operations = opParam.split(",");

        for (String operation : operations) {
          Integer enabledOp = OPERATORS.get(operation.trim().toUpperCase(Locale.ROOT));

          if (enabledOp != null) {
            enabledOps |= enabledOp;
          }
        }
      }

      // Create a SimpleQueryParser using the analyzer from the schema.
      final IndexSchema schema = req.getSchema();
      parser = new SolrSimpleQueryParser(req.getSchema().getAnalyzer(), queryFields, enabledOps, this, schema);

      // Set the default operator to be either 'AND' or 'OR' for the query.
      QueryParser.Operator defaultOp = QueryParsing.getQueryParserDefaultOperator(req.getSchema(), defaultParams.get(QueryParsing.OP));

      if (defaultOp == QueryParser.Operator.AND) {
        parser.setDefaultOperator(BooleanClause.Occur.MUST);
      }
    }

    @Override
    public Query parse() throws SyntaxError {
      return parser.parse(qstr);
    }

  }

  private static class SolrSimpleQueryParser extends SimpleQueryParser {
    QParser qParser;
    IndexSchema schema;

    public SolrSimpleQueryParser(Analyzer analyzer, Map<String, Float> weights, int flags,
                                 QParser qParser, IndexSchema schema) {
      super(analyzer, weights, flags);
      this.qParser = qParser;
      this.schema = schema;
    }

    @Override
    protected Query newPrefixQuery(String text) {
      BooleanQuery bq = new BooleanQuery(true);

      for (Map.Entry<String, Float> entry : weights.entrySet()) {
        String field = entry.getKey();
        FieldType type = schema.getFieldType(field);
        Query prefix;

        if (type instanceof TextField) {
          // If the field type is a TextField then use the multi term analyzer.
          Analyzer analyzer = ((TextField)type).getMultiTermAnalyzer();
          String term = TextField.analyzeMultiTerm(field, text, analyzer).utf8ToString();
          SchemaField sf = schema.getField(field);
          prefix = sf.getType().getPrefixQuery(qParser, sf, term);
        } else {
          // If the type is *not* a TextField don't do any analysis.
          SchemaField sf = schema.getField(field);
          prefix = type.getPrefixQuery(qParser, sf, text);
        }

        prefix.setBoost(entry.getValue());
        bq.add(prefix, BooleanClause.Occur.SHOULD);
      }

      return simplify(bq);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/843.java