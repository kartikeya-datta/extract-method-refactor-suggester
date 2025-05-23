error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7463.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7463.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7463.java
text:
```scala
H@@ashMap<String,Bucket>topWords=new HashMap<>();

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package org.elasticsearch.search.aggregations.bucket;

import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTerms;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTermsBuilder;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.elasticsearch.cluster.metadata.IndexMetaData.SETTING_NUMBER_OF_REPLICAS;
import static org.elasticsearch.cluster.metadata.IndexMetaData.SETTING_NUMBER_OF_SHARDS;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 */
public class SignificantTermsTests extends ElasticsearchIntegrationTest {

    @Override
    public Settings indexSettings() {
        return ImmutableSettings.builder()
                .put("index.number_of_shards", between(1, 5))
                .put("index.number_of_replicas", between(0, 1))
                .build();
    }

    public static final int MUSIC_CATEGORY=1;
    public static final int OTHER_CATEGORY=2;
    public static final int SNOWBOARDING_CATEGORY=3;
    
    @Before
    public void init() throws Exception {
        assertAcked(prepareCreate("test").setSettings(SETTING_NUMBER_OF_SHARDS, 5, SETTING_NUMBER_OF_REPLICAS, 0).addMapping("fact",
                "_routing", "required=true,path=routing_id", "routing_id", "type=string,index=not_analyzed", "fact_category",
                "type=integer,index=not_analyzed", "description", "type=string,index=analyzed"));
        createIndex("idx_unmapped");

        ensureGreen();
        String data[] = {                    
                    "A\t1\tpaul weller was lead singer of the jam before the style council",
                    "B\t1\tpaul weller left the jam to form the style council",
                    "A\t2\tpaul smith is a designer in the fashion industry",
                    "B\t1\tthe stranglers are a group originally from guildford",
                    "A\t1\tafter disbanding the style council in 1985 paul weller became a solo artist",
                    "B\t1\tjean jaques burnel is a bass player in the stranglers and has a black belt in karate",
                    "A\t1\tmalcolm owen was the lead singer of the ruts",
                    "B\t1\tpaul weller has denied any possibility of a reunion of the jam",
                    "A\t1\tformer frontman of the jam paul weller became the father of twins",
                    "B\t2\tex-england football star paul gascoigne has re-emerged following recent disappearance",
                    "A\t2\tdavid smith has recently denied connections with the mafia",
                    "B\t1\tthe damned's new rose single was considered the first 'punk' single in the UK",
                    "A\t1\tthe sex pistols broke up after a few short years together",
                    "B\t1\tpaul gascoigne was a midfielder for england football team",
                    "A\t3\tcraig kelly became the first world champion snowboarder and has a memorial at baldface lodge",
                    "B\t3\tterje haakonsen has credited craig kelly as his snowboard mentor",
                    "A\t3\tterje haakonsen and craig kelly were some of the first snowboarders sponsored by burton snowboards",
                    "B\t3\tlike craig kelly before him terje won the mt baker banked slalom many times - once riding switch",
                    "A\t3\tterje haakonsen has been a team rider for burton snowboards for over 20 years"                         
            };
            
        for (int i = 0; i < data.length; i++) {
            String[] parts = data[i].split("\t");
            client().prepareIndex("test", "fact", "" + i)
                    .setSource("routing_id", parts[0], "fact_category", parts[1], "description", parts[2]).get();
        }
        client().admin().indices().refresh(new RefreshRequest("test")).get();
    }

    @Test
    public void structuredAnalysis() throws Exception {
        SearchResponse response = client().prepareSearch("test")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(new TermQueryBuilder("_all", "terje"))
                .setFrom(0).setSize(60).setExplain(true)
                .addAggregation(new SignificantTermsBuilder("mySignificantTerms").field("fact_category")
                           .minDocCount(2))
                .execute()
                .actionGet();
        SignificantTerms topTerms = response.getAggregations().get("mySignificantTerms");
        Number topCategory = topTerms.getBuckets().iterator().next().getKeyAsNumber();
        assertTrue(topCategory.equals(new Long(SNOWBOARDING_CATEGORY)));
    }
    
    @Test
    public void unmapped() throws Exception {
        SearchResponse response = client().prepareSearch("idx_unmapped")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(new TermQueryBuilder("_all", "terje"))
                .setFrom(0).setSize(60).setExplain(true)
                .addAggregation(new SignificantTermsBuilder("mySignificantTerms").field("fact_category")
                           .minDocCount(2))
                .execute()
                .actionGet();
        SignificantTerms topTerms = response.getAggregations().get("mySignificantTerms");        
        assertThat(topTerms.getBuckets().size(), equalTo(0));
    }

    @Test
    public void textAnalysis() throws Exception {
        SearchResponse response = client().prepareSearch("test")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(new TermQueryBuilder("_all", "terje"))
                .setFrom(0).setSize(60).setExplain(true)
                .addAggregation(new SignificantTermsBuilder("mySignificantTerms").field("description")
                           .minDocCount(2))
                .execute()
                .actionGet();
        SignificantTerms topTerms = response.getAggregations().get("mySignificantTerms");
        checkExpectedStringTermsFound(topTerms);
    }    

    @Test
    public void partiallyUnmapped() throws Exception {
        SearchResponse response = client().prepareSearch("idx_unmapped","test")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(new TermQueryBuilder("_all", "terje"))
                .setFrom(0).setSize(60).setExplain(true)
                .addAggregation(new SignificantTermsBuilder("mySignificantTerms").field("description")
                           .minDocCount(2))
                .execute()
                .actionGet();
        SignificantTerms topTerms = response.getAggregations().get("mySignificantTerms");
        checkExpectedStringTermsFound(topTerms);
    }


    private void checkExpectedStringTermsFound(SignificantTerms topTerms) {
        HashMap<String,Bucket>topWords=new HashMap<String,Bucket>();
        for (Bucket topTerm : topTerms ){
            topWords.put(topTerm.getKey(),topTerm);
        }
        assertTrue( topWords.containsKey("haakonsen"));
        assertTrue( topWords.containsKey("craig"));
        assertTrue( topWords.containsKey("kelly"));
        assertTrue( topWords.containsKey("burton"));
        assertTrue( topWords.containsKey("snowboards"));
        Bucket kellyTerm=topWords.get("kelly");
        assertEquals(3, kellyTerm.getSubsetDf());
        assertEquals(4, kellyTerm.getSupersetDf());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7463.java