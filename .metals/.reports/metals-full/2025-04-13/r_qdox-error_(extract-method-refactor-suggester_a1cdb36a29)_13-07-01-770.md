error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6226.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6226.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6226.java
text:
```scala
r@@eturn optimizeQuery(fixNegativeQueryIfNeeded(query));

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.query.json;

import com.google.inject.Inject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.query.QueryParsingException;
import org.elasticsearch.index.query.support.MapperQueryParser;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.util.settings.Settings;

import java.io.IOException;

import static org.elasticsearch.index.query.support.QueryParsers.*;

/**
 * @author kimchy (shay.banon)
 */
public class FieldJsonQueryParser extends AbstractIndexComponent implements JsonQueryParser {

    public static final String NAME = "field";

    private final AnalysisService analysisService;

    @Inject public FieldJsonQueryParser(Index index, @IndexSettings Settings settings, AnalysisService analysisService) {
        super(index, settings);
        this.analysisService = analysisService;
    }

    @Override public String name() {
        return NAME;
    }

    @Override public Query parse(JsonQueryParseContext parseContext) throws IOException, QueryParsingException {
        JsonParser jp = parseContext.jp();

        JsonToken token = jp.nextToken();
        assert token == JsonToken.FIELD_NAME;
        String fieldName = jp.getCurrentName();

        String queryString = null;
        float boost = 1.0f;
        MapperQueryParser.Operator defaultOperator = QueryParser.Operator.OR;
        boolean lowercaseExpandedTerms = true;
        boolean enablePositionIncrements = true;
        int phraseSlop = 0;
        float fuzzyMinSim = FuzzyQuery.defaultMinSimilarity;
        int fuzzyPrefixLength = FuzzyQuery.defaultPrefixLength;
        boolean escape = false;
        Analyzer analyzer = null;
        token = jp.nextToken();
        if (token == JsonToken.START_OBJECT) {
            String currentFieldName = null;
            while ((token = jp.nextToken()) != JsonToken.END_OBJECT) {
                if (token == JsonToken.FIELD_NAME) {
                    currentFieldName = jp.getCurrentName();
                } else {
                    if ("query".equals(currentFieldName)) {
                        queryString = jp.getText();
                    } else if ("boost".equals(currentFieldName)) {
                        boost = jp.getFloatValue();
                    } else if ("enablePositionIncrements".equals(currentFieldName)) {
                        if (token == JsonToken.VALUE_TRUE) {
                            enablePositionIncrements = true;
                        } else if (token == JsonToken.VALUE_FALSE) {
                            enablePositionIncrements = false;
                        } else {
                            enablePositionIncrements = jp.getIntValue() != 0;
                        }
                    } else if ("lowercaseExpandedTerms".equals(currentFieldName)) {
                        if (token == JsonToken.VALUE_TRUE) {
                            lowercaseExpandedTerms = true;
                        } else if (token == JsonToken.VALUE_FALSE) {
                            lowercaseExpandedTerms = false;
                        } else {
                            lowercaseExpandedTerms = jp.getIntValue() != 0;
                        }
                    } else if ("phraseSlop".equals(currentFieldName)) {
                        phraseSlop = jp.getIntValue();
                    } else if ("analyzer".equals(currentFieldName)) {
                        analyzer = analysisService.analyzer(jp.getText());
                    } else if ("defaultOperator".equals(currentFieldName)) {
                        String op = jp.getText();
                        if ("or".equalsIgnoreCase(op)) {
                            defaultOperator = QueryParser.Operator.OR;
                        } else if ("and".equalsIgnoreCase(op)) {
                            defaultOperator = QueryParser.Operator.AND;
                        } else {
                            throw new QueryParsingException(index, "Query default operator [" + op + "] is not allowed");
                        }
                    } else if ("fuzzyMinSim".equals(currentFieldName)) {
                        fuzzyMinSim = jp.getFloatValue();
                    } else if ("fuzzyPrefixLength".equals(currentFieldName)) {
                        fuzzyPrefixLength = jp.getIntValue();
                    } else if ("escape".equals(currentFieldName)) {
                        if (token == JsonToken.VALUE_TRUE) {
                            escape = true;
                        } else if (token == JsonToken.VALUE_FALSE) {
                            escape = false;
                        } else {
                            escape = jp.getIntValue() != 0;
                        }
                    }
                }
            }
            jp.nextToken();
        } else {
            queryString = jp.getText();
            // move to the next token
            jp.nextToken();
        }

        if (analyzer == null) {
            analyzer = parseContext.mapperService().searchAnalyzer();
        }

        if (queryString == null) {
            throw new QueryParsingException(index, "No value specified for term query");
        }

        MapperQueryParser queryParser = new MapperQueryParser(fieldName, analyzer, parseContext.mapperService(), parseContext.filterCache());
        queryParser.setEnablePositionIncrements(enablePositionIncrements);
        queryParser.setLowercaseExpandedTerms(lowercaseExpandedTerms);
        queryParser.setPhraseSlop(phraseSlop);
        queryParser.setDefaultOperator(defaultOperator);
        queryParser.setFuzzyMinSim(fuzzyMinSim);
        queryParser.setFuzzyPrefixLength(fuzzyPrefixLength);

        if (escape) {
            queryString = QueryParser.escape(queryString);
        }

        try {
            Query query = queryParser.parse(queryString);
            query.setBoost(boost);
            return fixNegativeQueryIfNeeded(query);
        } catch (ParseException e) {
            throw new QueryParsingException(index, "Failed to parse query [" + queryString + "]", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6226.java