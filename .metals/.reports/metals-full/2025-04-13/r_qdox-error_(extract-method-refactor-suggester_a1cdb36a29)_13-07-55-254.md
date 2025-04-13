error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1837.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1837.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1837.java
text:
```scala
q@@uery.set(SpellingParams.SPELLCHECK_MAX_COLLATION_TRIES, 10);

package org.apache.solr.client.solrj.response;
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

import junit.framework.Assert;
import org.apache.solr.client.solrj.SolrJettyTestBase;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Collation;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Correction;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SpellingParams;
import org.apache.solr.util.ExternalPaths;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Test for SpellCheckComponent's response in Solrj
 *
 * @version $Id$
 * @since solr 1.3
 */
public class TestSpellCheckResponse extends SolrJettyTestBase {
  @BeforeClass
  public static void beforeTest() throws Exception {
    // createJetty(EXAMPLE_HOME, null, null);
    initCore(ExternalPaths.EXAMPLE_CONFIG, ExternalPaths.EXAMPLE_SCHEMA, ExternalPaths.EXAMPLE_HOME);
    // initCore("solrconfig.xml", "schema.xml", null);
  }
  
  static String field = "name";

  @Test
  public void testSpellCheckResponse() throws Exception {
    getSolrServer();
    SolrInputDocument doc = new SolrInputDocument();
    doc.setField("id", "111");
    doc.setField(field, "Samsung");
    server.add(doc);
    server.commit(true, true);

    SolrQuery query = new SolrQuery("*:*");
    query.set(CommonParams.QT, "/spell");
    query.set("spellcheck", true);
    query.set(SpellingParams.SPELLCHECK_Q, "samsang");
    query.set(SpellingParams.SPELLCHECK_BUILD, true);
    QueryRequest request = new QueryRequest(query);
    SpellCheckResponse response = request.process(server).getSpellCheckResponse();
    Assert.assertEquals("samsung", response.getFirstSuggestion("samsang"));
  }

  @Test
  public void testSpellCheckResponse_Extended() throws Exception {
    getSolrServer();
    SolrInputDocument doc = new SolrInputDocument();
    doc.setField("id", "111");
    doc.setField(field, "Samsung");
    server.add(doc);
    server.commit(true, true);

    SolrQuery query = new SolrQuery("*:*");
    query.set(CommonParams.QT, "/spell");
    query.set("spellcheck", true);
    query.set(SpellingParams.SPELLCHECK_Q, "samsang");
    query.set(SpellingParams.SPELLCHECK_BUILD, true);
    query.set(SpellingParams.SPELLCHECK_EXTENDED_RESULTS, true);
    QueryRequest request = new QueryRequest(query);
    SpellCheckResponse response = request.process(server).getSpellCheckResponse();
    assertEquals("samsung", response.getFirstSuggestion("samsang"));

    SpellCheckResponse.Suggestion sug = response.getSuggestion("samsang");
    List<SpellCheckResponse.Suggestion> sugs = response.getSuggestions();

    assertEquals(sug.getAlternatives().size(), sug.getAlternativeFrequencies().size());
    assertEquals(sugs.get(0).getAlternatives().size(), sugs.get(0).getAlternativeFrequencies().size());

    assertEquals("samsung", sug.getAlternatives().get(0));
    assertEquals("samsung", sugs.get(0).getAlternatives().get(0));

    // basic test if fields were filled in
    assertTrue(sug.getEndOffset()>0);
    assertTrue(sug.getToken().length() > 0);
    assertTrue(sug.getNumFound() > 0);
    // assertTrue(sug.getOriginalFrequency() > 0);

    // Hmmm... the API for SpellCheckResponse could be nicer:
    response.getSuggestions().get(0).getAlternatives().get(0);
  }
  
  @Test
  public void testSpellCheckCollationResponse() throws Exception {
  	getSolrServer();
    SolrInputDocument doc = new SolrInputDocument();
    doc.setField("id", "0");
    doc.setField("name", "faith hope and love");
    server.add(doc);
    doc = new SolrInputDocument();
    doc.setField("id", "1");
    doc.setField("name", "faith hope and loaves");
    server.add(doc);
    doc = new SolrInputDocument();
    doc.setField("id", "2");
    doc.setField("name", "fat hops and loaves");
    server.add(doc);
    doc = new SolrInputDocument();
    doc.setField("id", "3");
    doc.setField("name", "faith of homer");
    server.add(doc);
    doc = new SolrInputDocument();
    doc.setField("id", "4");
    doc.setField("name", "fat of homer");
    server.add(doc);    
    server.commit(true, true);
     
    //Test Backwards Compatibility
    SolrQuery query = new SolrQuery("name:(+fauth +home +loane)");
    query.set(CommonParams.QT, "/spell");
    query.set("spellcheck", true);
    query.set(SpellingParams.SPELLCHECK_BUILD, true);
    query.set(SpellingParams.SPELLCHECK_COUNT, 10);
    query.set(SpellingParams.SPELLCHECK_COLLATE, true);
    QueryRequest request = new QueryRequest(query);
    SpellCheckResponse response = request.process(server).getSpellCheckResponse();
    response = request.process(server).getSpellCheckResponse();
    assertTrue("name:(+faith +homer +loaves)".equals(response.getCollatedResult()));
    
    //Test Expanded Collation Results
    query.set(SpellingParams.SPELLCHECK_COLLATE_EXTENDED_RESULTS, true);
    query.set(SpellingParams.SPELLCHECK_MAX_COLLATION_TRIES, 5);
    query.set(SpellingParams.SPELLCHECK_MAX_COLLATIONS, 2); 
    request = new QueryRequest(query);
    response = request.process(server).getSpellCheckResponse();
    assertTrue("name:(+faith +hope +love)".equals(response.getCollatedResult()) || "name:(+faith +hope +loaves)".equals(response.getCollatedResult()));
    
    List<Collation> collations = response.getCollatedResults();
    assertTrue(collations.size()==2);
    for(Collation collation : collations)
    {
    	assertTrue("name:(+faith +hope +love)".equals(collation.getCollationQueryString()) || "name:(+faith +hope +loaves)".equals(collation.getCollationQueryString()));
      assertTrue(collation.getNumberOfHits()==1);
    	
    	List<Correction> misspellingsAndCorrections = collation.getMisspellingsAndCorrections();
    	assertTrue(misspellingsAndCorrections.size()==3);
    	for(Correction correction : misspellingsAndCorrections)
    	{    	
    		if("fauth".equals(correction.getOriginal()))
    		{
    			assertTrue("faith".equals(correction.getCorrection()));
    		} else if("home".equals(correction.getOriginal()))
    		{
    			assertTrue("hope".equals(correction.getCorrection()));
    		} else if("loane".equals(correction.getOriginal()))
    		{
    			assertTrue("love".equals(correction.getCorrection()) || "loaves".equals(correction.getCorrection()));
    		} else
    		{
    			fail("Original Word Should have been either fauth, home or loane.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1837.java