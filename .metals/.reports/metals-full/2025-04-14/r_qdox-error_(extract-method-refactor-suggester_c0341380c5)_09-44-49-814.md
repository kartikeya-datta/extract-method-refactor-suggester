error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/839.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/839.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/839.java
text:
```scala
public static final S@@tring NAME = "switch";

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
package org.apache.solr.search;

import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.handler.component.SearchHandler; // jdoc

import org.apache.commons.lang.StringUtils;

/**
 * <p>A QParserPlugin that acts like a "switch/case" statement.</p>
 * 
 * <p>
 * QParser's produced by this plugin will take their primary input string, 
 * trimmed and prefixed with "<code>case.</code>", to use as a key to lookup a 
 * "switch case" in the parser's local params.  If a matching local param is 
 * found the resulting param value will then be parsed as a subquery, and 
 * returned as the parse result.
 * </p>
 * <p>
 * The "<code>case</code>" local param can be optionally be specified as a 
 * switch case to match missing (or blank) input strings.  
 * The "<code>default</code>" local param can optionally be specified 
 * as a default case to use if the input string does not match any other 
 * switch case local params.  If <code>default</code> is not specified, 
 * then any input which does not match a switch case local param will result 
 * in a syntax error.
 * </p>
 *
 * <p>
 * In the examples below, the result of each query would be <code>XXX</code>....
 * </p>
 * <pre>
 *  q={!switch case.foo=XXX case.bar=zzz case.yak=qqq}foo
 *  q={!switch case.foo=qqq case.bar=XXX case.yak=zzz} bar  // extra whitespace
 *  q={!switch case.foo=qqq case.bar=zzz default=XXX}asdf   // fallback on default
 *  q={!switch case=XXX case.bar=zzz case.yak=qqq}          // blank input
 * </pre>
 *
 * <p>
 * A practical usage of this QParsePlugin, is in specifying "appends" 
 * <code>fq</code> params in the configuration of a {@link SearchHandler}, to 
 * provide a fixed set of filter options for clients using custom parameter 
 * names. 
 * Using the example configuration below, clients can optionally specify the 
 * custom parameters <code>in_stock</code> and <code>shipping</code> to 
 * override the default filtering behavior, but are limited to the specific 
 * set of legal values (<code>shipping=any|free</code>, 
 * <code>in_stock=yes|no|all</code>).  
 * </p>
 *
 * <pre class="prettyprint">
 * &lt;requestHandler name="/select" class="solr.SearchHandler"&gt;
 *   &lt;lst name="defaults"&gt;
 *     &lt;str name="in_stock"&gt;yes&lt;/str&gt;
 *     &lt;str name="shipping"&gt;any&lt;/str&gt;
 *   &lt;/lst&gt;
 *   &lt;lst name="appends"&gt;
 *     &lt;str name="fq"&gt;{!switch case.all='*:*'
 *                             case.yes='inStock:true'
 *                             case.no='inStock:false'
 *                             v=$in_stock}&lt;/str&gt;
 *     &lt;str name="fq"&gt;{!switch case.any='*:*'
 *                             case.free='shipping_cost:0.0'
 *                             v=$shipping}&lt;/str&gt;
 *   &lt;/lst&gt;
 * &lt;/requestHandler&gt;</pre>
 *
 * <p>
 * A slightly more interesting variant of the <code>shipping</code> example above, would be
 * to combine the switch parser with the frange parser, to allow the client to specify an 
 * arbitrary "max shipping" amount that will be used to build a filter if and only if a 
 * value is specified.  Example:
 * </p>
 * <pre class="prettyprint">
 * &lt;requestHandler name="/select" class="solr.SearchHandler"&gt;
 *   &lt;lst name="invariants"&gt;
 *     &lt;str name="shipping_fq"&gt;{!frange u=$shipping}shipping_cost&lt;/str&gt;
 *   &lt;/lst&gt;
 *   &lt;lst name="defaults"&gt;
 *     &lt;str name="shipping"&gt;any&lt;/str&gt;
 *   &lt;/lst&gt;
 *   &lt;lst name="appends"&gt;
 *     &lt;str name="fq"&gt;{!switch case='*:*'
 *                             case.any='*:*'
 *                             default=$shipping_fq
 *                             v=$shipping}&lt;/str&gt;
 *   &lt;/lst&gt;
 * &lt;/requestHandler&gt;</pre>
 *
 * <p>
 * With the above configuration a client that specifies <code>shipping=any</code>, or 
 * does not specify a <code>shipping</code> param at all, will not have the results 
 * filtered.  But if a client specifies a numeric value (ie: <code>shipping=10</code>, 
 * <code>shipping=5</code>, etc..) then the results will be limited to documents whose 
 * <code>shipping_cost</code> field has a value less then that number.
 * </p>
 *
 * <p>
 * A similar use case would be to combine the switch parser with the bbox parser to 
 * support an optional geographic filter that is applied if and only if the client 
 * specifies a <code>location</code> param containing a lat,lon pair to be used as 
 * the center of the bounding box:
 * </p>
 * <pre class="prettyprint">
 * &lt;requestHandler name="/select" class="solr.SearchHandler"&gt;
 *   &lt;lst name="invariants"&gt;
 *     &lt;str name="bbox_fq"&gt;{!bbox pt=$location sfield=geo d=$dist}&lt;/str&gt;
 *   &lt;/lst&gt;
 *   &lt;lst name="defaults"&gt;
 *     &lt;str name="dist"&gt;100&lt;/str&gt;
 *   &lt;/lst&gt;
 *   &lt;lst name="appends"&gt;
 *     &lt;str name="fq"&gt;{!switch case='*:*' 
 *                             default=$bbox_fq 
 *                             v=$location}&lt;/str&gt;
 *   &lt;/lst&gt;
 * &lt;/requestHandler&gt;</pre>
 */
public class SwitchQParserPlugin extends QParserPlugin {
  public static String NAME = "switch";

  /** 
   * Used as both a local params key to find the "default" if no
   * blank input is provided to the parser, as well as a prefix (followed by 
   * '.' for looking up the switch input.
   */
  public static String SWITCH_CASE = "case";

  /**
   * A local param whose value, if specified, is used if no switch case
   * matches the parser input.  If this param is not specified, and no 
   * switch case matches the parser input, an error is returned.
   */
  public static String SWITCH_DEFAULT = "default";

  @Override
  public void init(NamedList args) {
  }

  @Override
  public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
    return new QParser(qstr, localParams, params, req) {
      QParser subParser;

      @Override
      public Query parse() throws SyntaxError {
        String val = localParams.get(QueryParsing.V);

        // we don't want to wrapDefaults arround params, because then 
        // clients could add their own switch options 
        String subQ = localParams.get(SWITCH_DEFAULT);
        subQ = StringUtils.isBlank(val)
          ? localParams.get(SWITCH_CASE, subQ)
          : localParams.get(SWITCH_CASE + "." + val.trim(), subQ);

        if (null == subQ) {
          throw new SyntaxError("No "+SWITCH_DEFAULT+", and no switch case matching specified query string: \"" + val + "\"");
        }

        subParser = subQuery(subQ, null);
        return subParser.getQuery();
      }

      @Override
      public String[] getDefaultHighlightFields() {
        return subParser.getDefaultHighlightFields();
      }
                                           
      @Override
      public Query getHighlightQuery() throws SyntaxError {
        return subParser.getHighlightQuery();
      }

      @Override
      public void addDebugInfo(NamedList<Object> debugInfo) {
        subParser.addDebugInfo(debugInfo);
      }
    };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/839.java