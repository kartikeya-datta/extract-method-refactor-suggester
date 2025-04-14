error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9440.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9440.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9440.java
text:
```scala
r@@eturn wrapSmartNameQuery(mltQuery, smartNameFieldMappers, parseContext.indexCache());

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

import com.google.common.collect.Sets;
import org.apache.lucene.search.Query;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.query.QueryParsingException;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.util.Booleans;
import org.elasticsearch.util.lucene.search.MoreLikeThisQuery;
import org.elasticsearch.util.settings.Settings;

import java.io.IOException;
import java.util.Set;

import static org.elasticsearch.index.query.support.QueryParsers.*;

/**
 * @author kimchy (shay.banon)
 */
public class MoreLikeThisFieldJsonQueryParser extends AbstractIndexComponent implements JsonQueryParser {

    public static final String NAME = "moreLikeThisField";

    public MoreLikeThisFieldJsonQueryParser(Index index, @IndexSettings Settings indexSettings) {
        super(index, indexSettings);
    }

    @Override public String name() {
        return NAME;
    }

    @Override public Query parse(JsonQueryParseContext parseContext) throws IOException, QueryParsingException {
        JsonParser jp = parseContext.jp();

        JsonToken token = jp.nextToken();
        assert token == JsonToken.FIELD_NAME;
        String fieldName = jp.getCurrentName();

        // now, we move after the field name, which starts the object
        token = jp.nextToken();
        assert token == JsonToken.START_OBJECT;


        MoreLikeThisQuery mltQuery = new MoreLikeThisQuery();
        mltQuery.setSimilarity(parseContext.searchSimilarity());

        String currentFieldName = null;
        while ((token = jp.nextToken()) != JsonToken.END_OBJECT) {
            if (token == JsonToken.FIELD_NAME) {
                currentFieldName = jp.getCurrentName();
            } else if (token == JsonToken.VALUE_STRING) {
                if ("likeText".equals(currentFieldName)) {
                    mltQuery.setLikeText(jp.getText());
                } else if ("minTermFrequency".equals(currentFieldName)) {
                    mltQuery.setMinTermFrequency(Integer.parseInt(jp.getText()));
                } else if ("maxQueryTerms".equals(currentFieldName)) {
                    mltQuery.setMaxQueryTerms(Integer.parseInt(jp.getText()));
                } else if ("minDocFreq".equals(currentFieldName)) {
                    mltQuery.setMinDocFreq(Integer.parseInt(jp.getText()));
                } else if ("maxDocFreq".equals(currentFieldName)) {
                    mltQuery.setMaxDocFreq(Integer.parseInt(jp.getText()));
                } else if ("minWordLen".equals(currentFieldName)) {
                    mltQuery.setMinWordLen(Integer.parseInt(jp.getText()));
                } else if ("maxWordLen".equals(currentFieldName)) {
                    mltQuery.setMaxWordLen(Integer.parseInt(jp.getText()));
                } else if ("boostTerms".equals(currentFieldName)) {
                    mltQuery.setBoostTerms(Booleans.parseBoolean(jp.getText(), false));
                } else if ("boostTermsFactor".equals(currentFieldName)) {
                    mltQuery.setBoostTermsFactor(Float.parseFloat(jp.getText()));
                } else if ("percentTermsToMatch".equals(currentFieldName)) {
                    mltQuery.setPercentTermsToMatch(Float.parseFloat(jp.getText()));
                }
            } else if (token == JsonToken.VALUE_NUMBER_INT) {
                if ("minTermFrequency".equals(currentFieldName)) {
                    mltQuery.setMinTermFrequency(jp.getIntValue());
                } else if ("maxQueryTerms".equals(currentFieldName)) {
                    mltQuery.setMaxQueryTerms(jp.getIntValue());
                } else if ("minDocFreq".equals(currentFieldName)) {
                    mltQuery.setMinDocFreq(jp.getIntValue());
                } else if ("maxDocFreq".equals(currentFieldName)) {
                    mltQuery.setMaxDocFreq(jp.getIntValue());
                } else if ("minWordLen".equals(currentFieldName)) {
                    mltQuery.setMinWordLen(jp.getIntValue());
                } else if ("maxWordLen".equals(currentFieldName)) {
                    mltQuery.setMaxWordLen(jp.getIntValue());
                } else if ("boostTerms".equals(currentFieldName)) {
                    mltQuery.setBoostTerms(jp.getIntValue() != 0);
                } else if ("boostTermsFactor".equals(currentFieldName)) {
                    mltQuery.setBoostTermsFactor(jp.getIntValue());
                } else if ("percentTermsToMatch".equals(currentFieldName)) {
                    mltQuery.setPercentTermsToMatch(jp.getIntValue());
                }
            } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
                if ("boostTermsFactor".equals(currentFieldName)) {
                    mltQuery.setBoostTermsFactor(jp.getFloatValue());
                } else if ("percentTermsToMatch".equals(currentFieldName)) {
                    mltQuery.setPercentTermsToMatch(jp.getFloatValue());
                }
            } else if (token == JsonToken.START_ARRAY) {
                if ("stopWords".equals(currentFieldName)) {
                    Set<String> stopWords = Sets.newHashSet();
                    while ((token = jp.nextToken()) != JsonToken.END_ARRAY) {
                        stopWords.add(jp.getText());
                    }
                    mltQuery.setStopWords(stopWords);
                }
            }
        }

        if (mltQuery.getLikeText() == null) {
            throw new QueryParsingException(index, "moreLikeThisField requires 'likeText' to be specified");
        }

        // move to the next end object, to close the field name
        token = jp.nextToken();
        assert token == JsonToken.END_OBJECT;

        MapperService.SmartNameFieldMappers smartNameFieldMappers = parseContext.smartFieldMappers(fieldName);
        if (smartNameFieldMappers != null) {
            if (smartNameFieldMappers.hasMapper()) {
                fieldName = smartNameFieldMappers.mapper().names().indexName();
                mltQuery.setAnalyzer(smartNameFieldMappers.mapper().searchAnalyzer());
            }
        }
        if (mltQuery.getAnalyzer() == null) {
            mltQuery.setAnalyzer(parseContext.mapperService().searchAnalyzer());
        }
        mltQuery.setMoreLikeFields(new String[]{fieldName});
        return wrapSmartNameQuery(mltQuery, smartNameFieldMappers, parseContext.filterCache());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9440.java