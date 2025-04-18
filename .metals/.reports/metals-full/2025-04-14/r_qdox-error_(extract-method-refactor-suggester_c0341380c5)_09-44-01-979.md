error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1295.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1295.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1295.java
text:
```scala
r@@eturn parsedUserQuery == null ? altUserQuery : parsedUserQuery;

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
package org.apache.solr.search;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.DefaultSolrParams;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.util.SolrPluginUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Query parser for dismax queries
 * <p/>
 * <b>Note: This API is experimental and may change in non backward-compatible ways in the future</b>
 *
 * @version $Id$
 */
public class DisMaxQParser extends QParser {

  /**
   * A field we can't ever find in any schema, so we can safely tell DisjunctionMaxQueryParser to use it as our
   * defaultField, and map aliases from it to any field in our schema.
   */
  private static String IMPOSSIBLE_FIELD_NAME = "\uFFFC\uFFFC\uFFFC";

  /**
   * Applies the appropriate default rules for the "mm" param based on the 
   * effective value of the "q.op" param
   *
   * @see QueryParsing#getQueryParserDefaultOperator
   * @see QueryParsing#OP
   * @see DisMaxParams#MM
   */
  public static String parseMinShouldMatch(final IndexSchema schema, 
                                           final SolrParams params) {
    Operator op = QueryParsing.getQueryParserDefaultOperator
      (schema, params.get(QueryParsing.OP));
    return params.get(DisMaxParams.MM, 
                      op.equals(Operator.AND) ? "100%" : "0%");
  }

  public DisMaxQParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
    super(qstr, localParams, params, req);
  }

  protected Map<String, Float> queryFields;
  protected Query parsedUserQuery;


  protected String[] boostParams;
  protected List<Query> boostQueries;
  protected Query altUserQuery;
  protected QParser altQParser;


  public Query parse() throws ParseException {
    SolrParams solrParams = localParams == null ? params : new DefaultSolrParams(localParams, params);
    queryFields = SolrPluginUtils.parseFieldBoosts(solrParams.getParams(DisMaxParams.QF));
    if (0 == queryFields.size()) {
      queryFields.put(req.getSchema().getDefaultSearchFieldName(), 1.0f);
    }
    
    /* the main query we will execute.  we disable the coord because
     * this query is an artificial construct
     */
    BooleanQuery query = new BooleanQuery(true);

    addMainQuery(query, solrParams);
    addBoostQuery(query, solrParams);
    addBoostFunctions(query, solrParams);

    return query;
  }

  protected void addBoostFunctions(BooleanQuery query, SolrParams solrParams) throws ParseException {
    String[] boostFuncs = solrParams.getParams(DisMaxParams.BF);
    if (null != boostFuncs && 0 != boostFuncs.length) {
      for (String boostFunc : boostFuncs) {
        if (null == boostFunc || "".equals(boostFunc)) continue;
        Map<String, Float> ff = SolrPluginUtils.parseFieldBoosts(boostFunc);
        for (String f : ff.keySet()) {
          Query fq = subQuery(f, FunctionQParserPlugin.NAME).parse();
          Float b = ff.get(f);
          if (null != b) {
            fq.setBoost(b);
          }
          query.add(fq, BooleanClause.Occur.SHOULD);
        }
      }
    }
  }

  protected void addBoostQuery(BooleanQuery query, SolrParams solrParams) throws ParseException {
    boostParams = solrParams.getParams(DisMaxParams.BQ);
    //List<Query> boostQueries = SolrPluginUtils.parseQueryStrings(req, boostParams);
    boostQueries = null;
    if (boostParams != null && boostParams.length > 0) {
      boostQueries = new ArrayList<Query>();
      for (String qs : boostParams) {
        if (qs.trim().length() == 0) continue;
        Query q = subQuery(qs, null).parse();
        boostQueries.add(q);
      }
    }
    if (null != boostQueries) {
      if (1 == boostQueries.size() && 1 == boostParams.length) {
        /* legacy logic */
        Query f = boostQueries.get(0);
        if (1.0f == f.getBoost() && f instanceof BooleanQuery) {
          /* if the default boost was used, and we've got a BooleanQuery
           * extract the subqueries out and use them directly
           */
          for (Object c : ((BooleanQuery) f).clauses()) {
            query.add((BooleanClause) c);
          }
        } else {
          query.add(f, BooleanClause.Occur.SHOULD);
        }
      } else {
        for (Query f : boostQueries) {
          query.add(f, BooleanClause.Occur.SHOULD);
        }
      }
    }
  }

  protected void addMainQuery(BooleanQuery query, SolrParams solrParams) throws ParseException {
    Map<String, Float> phraseFields = SolrPluginUtils.parseFieldBoosts(solrParams.getParams(DisMaxParams.PF));
    float tiebreaker = solrParams.getFloat(DisMaxParams.TIE, 0.0f);

    /* a parser for dealing with user input, which will convert
     * things to DisjunctionMaxQueries
     */
    SolrPluginUtils.DisjunctionMaxQueryParser up = getParser(queryFields, DisMaxParams.QS, solrParams, tiebreaker);

    /* for parsing sloppy phrases using DisjunctionMaxQueries */
    SolrPluginUtils.DisjunctionMaxQueryParser pp = getParser(phraseFields, DisMaxParams.PS, solrParams, tiebreaker);

    /* * * Main User Query * * */
    parsedUserQuery = null;
    String userQuery = getString();
    altUserQuery = null;
    if (userQuery == null || userQuery.trim().length() < 1) {
      // If no query is specified, we may have an alternate
      altUserQuery = getAlternateUserQuery(solrParams);
      query.add(altUserQuery, BooleanClause.Occur.MUST);
    } else {
      // There is a valid query string
      userQuery = SolrPluginUtils.partialEscape(SolrPluginUtils.stripUnbalancedQuotes(userQuery)).toString();
      userQuery = SolrPluginUtils.stripIllegalOperators(userQuery).toString();

      parsedUserQuery = getUserQuery(userQuery, up, solrParams);
      query.add(parsedUserQuery, BooleanClause.Occur.MUST);

      Query phrase = getPhraseQuery(userQuery, pp);
      if (null != phrase) {
        query.add(phrase, BooleanClause.Occur.SHOULD);
      }
    }
  }

  protected Query getAlternateUserQuery(SolrParams solrParams) throws ParseException {
    String altQ = solrParams.get(DisMaxParams.ALTQ);
    if (altQ != null) {
      QParser altQParser = subQuery(altQ, null);
      return altQParser.parse();
    } else {
      throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "missing query string");
    }
  }

  protected Query getPhraseQuery(String userQuery, SolrPluginUtils.DisjunctionMaxQueryParser pp) throws ParseException {
    /* * * Add on Phrases for the Query * * */

    /* build up phrase boosting queries */

    /* if the userQuery already has some quotes, strip them out.
     * we've already done the phrases they asked for in the main
     * part of the query, this is to boost docs that may not have
     * matched those phrases but do match looser phrases.
    */
    String userPhraseQuery = userQuery.replace("\"", "");
    return pp.parse("\"" + userPhraseQuery + "\"");
  }

  protected Query getUserQuery(String userQuery, SolrPluginUtils.DisjunctionMaxQueryParser up, SolrParams solrParams)
          throws ParseException {
    String minShouldMatch = parseMinShouldMatch(req.getSchema(), solrParams);
    Query dis = up.parse(userQuery);
    Query query = dis;

    if (dis instanceof BooleanQuery) {
      BooleanQuery t = new BooleanQuery();
      SolrPluginUtils.flattenBooleanQuery(t, (BooleanQuery) dis);
      SolrPluginUtils.setMinShouldMatch(t, minShouldMatch);
      query = t;
    }
    return query;
  }

  protected SolrPluginUtils.DisjunctionMaxQueryParser getParser(Map<String, Float> fields, String paramName,
                                                                SolrParams solrParams, float tiebreaker) {
    int slop = solrParams.getInt(paramName, 0);
    SolrPluginUtils.DisjunctionMaxQueryParser parser = new SolrPluginUtils.DisjunctionMaxQueryParser(this,
            IMPOSSIBLE_FIELD_NAME);
    parser.addAlias(IMPOSSIBLE_FIELD_NAME, tiebreaker, fields);
    parser.setPhraseSlop(slop);
    return parser;
  }

  @Override
  public String[] getDefaultHighlightFields() {
    return queryFields.keySet().toArray(new String[queryFields.keySet().size()]);
  }

  @Override
  public Query getHighlightQuery() throws ParseException {
    return parsedUserQuery;
  }

  public void addDebugInfo(NamedList<Object> debugInfo) {
    super.addDebugInfo(debugInfo);
    debugInfo.add("altquerystring", altUserQuery);
    if (null != boostQueries) {
      debugInfo.add("boost_queries", boostParams);
      debugInfo.add("parsed_boost_queries",
              QueryParsing.toString(boostQueries, req.getSchema()));
    }
    debugInfo.add("boostfuncs", req.getParams().getParams(DisMaxParams.BF));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1295.java