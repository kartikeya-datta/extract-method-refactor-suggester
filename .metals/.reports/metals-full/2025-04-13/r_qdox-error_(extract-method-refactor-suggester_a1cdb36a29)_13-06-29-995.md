error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9466.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9466.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9466.java
text:
```scala
public M@@oreLikeThisFieldJsonQueryBuilder boostTerms(Boolean boostTerms) {

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

import org.elasticsearch.index.query.QueryBuilderException;
import org.elasticsearch.util.json.JsonBuilder;

import java.io.IOException;

/**
 * @author kimchy (Shay Banon)
 */
public class MoreLikeThisFieldJsonQueryBuilder extends BaseJsonQueryBuilder {

    private final String name;

    private String likeText;
    private float percentTermsToMatch = -1;
    private int minTermFrequency = -1;
    private int maxQueryTerms = -1;
    private String[] stopWords = null;
    private int minDocFreq = -1;
    private int maxDocFreq = -1;
    private int minWordLen = -1;
    private int maxWordLen = -1;
    private Boolean boostTerms = null;
    private float boostTermsFactor = -1;

    public MoreLikeThisFieldJsonQueryBuilder(String name) {
        this.name = name;
    }

    public MoreLikeThisFieldJsonQueryBuilder likeText(String likeText) {
        this.likeText = likeText;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder percentTermsToMatch(float percentTermsToMatch) {
        this.percentTermsToMatch = percentTermsToMatch;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder minTermFrequency(int minTermFrequency) {
        this.minTermFrequency = minTermFrequency;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder maxQueryTerms(int maxQueryTerms) {
        this.maxQueryTerms = maxQueryTerms;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder stopWords(String... stopWords) {
        this.stopWords = stopWords;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder minDocFreq(int minDocFreq) {
        this.minDocFreq = minDocFreq;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder maxDocFreq(int maxDocFreq) {
        this.maxDocFreq = maxDocFreq;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder minWordLen(int minWordLen) {
        this.minWordLen = minWordLen;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder maxWordLen(int maxWordLen) {
        this.maxWordLen = maxWordLen;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder boostTerms(boolean boostTerms) {
        this.boostTerms = boostTerms;
        return this;
    }

    public MoreLikeThisFieldJsonQueryBuilder boostTermsFactor(float boostTermsFactor) {
        this.boostTermsFactor = boostTermsFactor;
        return this;
    }

    @Override protected void doJson(JsonBuilder builder, Params params) throws IOException {
        builder.startObject(MoreLikeThisFieldJsonQueryParser.NAME);
        builder.startObject(name);
        if (likeText == null) {
            throw new QueryBuilderException("moreLikeThisField requires 'likeText' to be provided");
        }
        builder.field("likeText", likeText);
        if (percentTermsToMatch != -1) {
            builder.field("percentTermsToMatch", percentTermsToMatch);
        }
        if (minTermFrequency != -1) {
            builder.field("minTermFrequency", minTermFrequency);
        }
        if (maxQueryTerms != -1) {
            builder.field("maxQueryTerms", maxQueryTerms);
        }
        if (stopWords != null && stopWords.length > 0) {
            builder.startArray("stopWords");
            for (String stopWord : stopWords) {
                builder.string(stopWord);
            }
            builder.endArray();
        }
        if (minDocFreq != -1) {
            builder.field("minDocFreq", minDocFreq);
        }
        if (maxDocFreq != -1) {
            builder.field("maxDocFreq", maxDocFreq);
        }
        if (minWordLen != -1) {
            builder.field("minWordLen", minWordLen);
        }
        if (maxWordLen != -1) {
            builder.field("maxWordLen", maxWordLen);
        }
        if (boostTerms != null) {
            builder.field("boostTerms", boostTerms);
        }
        if (boostTermsFactor != -1) {
            builder.field("boostTermsFactor", boostTermsFactor);
        }
        builder.endObject();
        builder.endObject();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9466.java