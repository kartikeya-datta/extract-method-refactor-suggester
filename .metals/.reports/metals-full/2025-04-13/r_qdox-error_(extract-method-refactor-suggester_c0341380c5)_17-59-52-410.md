error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/830.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/830.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/830.java
text:
```scala
public static final S@@tring NAME = "surround";

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


import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.lucene.queryparser.surround.parser.*;
import org.apache.lucene.queryparser.surround.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Plugin for lucene/contrib Surround query parser, bringing SpanQuery support
 * to Solr.
 * <p>
 * &lt;queryParser name="surround"
 * class="org.apache.solr.search.SurroundQParserPlugin" /&gt;
 * <p>
 * Note that the query string is not analyzed in any way
 * 
 * @see QueryParser
 * @since 4.0
 */

public class SurroundQParserPlugin extends QParserPlugin {
  public static String NAME = "surround";

  @Override
  public void init(NamedList args) {
  }

  @Override
  public QParser createParser(String qstr, SolrParams localParams,
      SolrParams params, SolrQueryRequest req) {
    return new SurroundQParser(qstr, localParams, params, req);
  }

}

class SurroundQParser extends QParser {
  protected static final Logger LOG = LoggerFactory .getLogger(SurroundQParser.class);
  static final int DEFMAXBASICQUERIES = 1000;
  static final String MBQParam = "maxBasicQueries";
  
  String sortStr;
  SolrQueryParser lparser;
  int maxBasicQueries;

  public SurroundQParser(String qstr, SolrParams localParams,
      SolrParams params, SolrQueryRequest req) {
    super(qstr, localParams, params, req);
  }

  @Override
  public Query parse()
      throws SyntaxError {
    SrndQuery sq;
    String qstr = getString();
    if (qstr == null)
      return null;
    String mbqparam = getParam(MBQParam);
    if (mbqparam == null) {
      this.maxBasicQueries = DEFMAXBASICQUERIES;
    } else {
      try {
        this.maxBasicQueries = Integer.parseInt(mbqparam);
      } catch (Exception e) {
        LOG.warn("Couldn't parse maxBasicQueries value " + mbqparam +", using default of 1000");
        this.maxBasicQueries = DEFMAXBASICQUERIES;
      }
    }
    // ugh .. colliding ParseExceptions
    try {
      sq = org.apache.lucene.queryparser.surround.parser.QueryParser
          .parse(qstr);
    } catch (org.apache.lucene.queryparser.surround.parser.ParseException pe) {
      throw new SyntaxError(pe);
    }
    
    // so what do we do with the SrndQuery ??
    // processing based on example in LIA Ch 9

    BasicQueryFactory bqFactory = new BasicQueryFactory(this.maxBasicQueries);
    String defaultField = QueryParsing.getDefaultField(getReq().getSchema(),getParam(CommonParams.DF));
    Query lquery = sq.makeLuceneQueryField(defaultField, bqFactory);
    return lquery;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/830.java