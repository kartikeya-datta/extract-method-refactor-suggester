error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2788.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2788.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2788.java
text:
```scala
a@@ssertJQ(req("json.nl","map", "qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", "q","pixma", SpellCheckComponent.SPELLCHECK_COLLATE, "true")

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

package org.apache.solr.handler.component;

import java.io.File;
import java.util.*;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SpellingParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.spelling.AbstractLuceneSpellChecker;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @since solr 1.3
 */
public class SpellCheckComponentTest extends SolrTestCaseJ4 {
  static String rh = "spellCheckCompRH";


  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig-spellcheckcomponent.xml","schema.xml");
  }
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    assertU(adoc("id", "0", "lowerfilt", "This is a title"));
    assertU((adoc("id", "1", "lowerfilt",
            "The quick reb fox jumped over the lazy brown dogs.")));
    assertU((adoc("id", "2", "lowerfilt", "This is a document")));
    assertU((adoc("id", "3", "lowerfilt", "another document")));
    //bunch of docs that are variants on blue
    assertU((adoc("id", "4", "lowerfilt", "blue")));
    assertU((adoc("id", "5", "lowerfilt", "blud")));
    assertU((adoc("id", "6", "lowerfilt", "boue")));
    assertU((adoc("id", "7", "lowerfilt", "glue")));
    assertU((adoc("id", "8", "lowerfilt", "blee")));
    assertU((adoc("id", "9", "lowerfilt", "pixmaa")));
    assertU((commit()));
  }
  
  @Override
  public void tearDown() throws Exception {
    super.tearDown();
    assertU(delQ("*:*"));
    optimize();
    assertU((commit()));

  }
  
  @Test
  public void testExtendedResultsCount() throws Exception {
    assertJQ(req("qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", SpellCheckComponent.SPELLCHECK_BUILD, "true", "q","bluo", SpellCheckComponent.SPELLCHECK_COUNT,"5", SpellCheckComponent.SPELLCHECK_EXTENDED_RESULTS,"false")
       ,"/spellcheck/suggestions/[0]=='bluo'"
       ,"/spellcheck/suggestions/[1]/numFound==5"
    );

    assertJQ(req("qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", "q","bluo", SpellCheckComponent.SPELLCHECK_COUNT,"3", SpellCheckComponent.SPELLCHECK_EXTENDED_RESULTS,"true")
       ,"/spellcheck/suggestions/[1]/suggestion==[{'word':'blud','freq':1}, {'word':'blue','freq':1}, {'word':'blee','freq':1}]"
    );
  }

  @Test
  public void test() throws Exception {
    assertJQ(req("qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", "q","documemt")
       ,"/spellcheck=={'suggestions':['documemt',{'numFound':1,'startOffset':0,'endOffset':8,'suggestion':['document']}]}"
    );
  }


  @Test
  public void testPerDictionary() throws Exception {
    assertJQ(req("json.nl","map", "qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", SpellCheckComponent.SPELLCHECK_BUILD, "true", "q","documemt"
        , SpellingParams.SPELLCHECK_DICT, "perDict", SpellingParams.SPELLCHECK_PREFIX + ".perDict.foo", "bar", SpellingParams.SPELLCHECK_PREFIX + ".perDict.bar", "foo")
       ,"/spellcheck/suggestions/bar=={'numFound':1, 'startOffset':0, 'endOffset':1, 'suggestion':['foo']}"
       ,"/spellcheck/suggestions/foo=={'numFound':1, 'startOffset':2, 'endOffset':3, 'suggestion':['bar']}"        
    );
  }

  @Test
  public void testCollate() throws Exception {
    assertJQ(req("json.nl","map", "qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", SpellCheckComponent.SPELLCHECK_BUILD, "true", "q","documemt", SpellCheckComponent.SPELLCHECK_COLLATE, "true")
       ,"/spellcheck/suggestions/collation=='document'"
    );
    assertJQ(req("json.nl","map", "qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", "q","documemt lowerfilt:broen^4", SpellCheckComponent.SPELLCHECK_COLLATE, "true")
       ,"/spellcheck/suggestions/collation=='document lowerfilt:brown^4'"
    );
    assertJQ(req("json.nl","map", "qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", "q","documemtsss broens", SpellCheckComponent.SPELLCHECK_COLLATE, "true")
       ,"/spellcheck/suggestions/collation=='document brown'"
    );
    assertJQ(req("json.nl","map", "qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", "q","pixma-a-b-c-d-e-f-g", SpellCheckComponent.SPELLCHECK_COLLATE, "true")
       ,"/spellcheck/suggestions/collation=='pixmaa'"
    );
  }
  

  @Test
  public void testCorrectSpelling() throws Exception {
    // Make sure correct spellings are signaled in the response
    assertJQ(req("json.nl","map", "qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", "q","lowerfilt:lazy lowerfilt:brown", SpellCheckComponent.SPELLCHECK_EXTENDED_RESULTS, "true")
       ,"/spellcheck/suggestions=={'correctlySpelled':true}"
    );
    assertJQ(req("json.nl","map", "qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", "q","lakkle", SpellCheckComponent.SPELLCHECK_EXTENDED_RESULTS, "true")
       ,"/spellcheck/suggestions/correctlySpelled==false"
    );
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testRelativeIndexDirLocation() throws Exception {
    SolrCore core = h.getCore();
    File indexDir = new File(core.getDataDir() + File.separator + "spellchecker1");
    assertTrue(indexDir.exists());
    
    indexDir = new File(core.getDataDir() + File.separator + "spellchecker2");
    assertTrue(indexDir.exists());
    
    indexDir = new File(core.getDataDir() + File.separator + "spellchecker3");
    assertTrue(indexDir.exists());
  }

  @Test
  public void testReloadOnStart() throws Exception {
    assertU(adoc("id", "0", "lowerfilt", "This is a title"));
    assertU(commit());
    SolrQueryRequest request = req("qt", "spellCheckCompRH", "q", "*:*",
        "spellcheck.q", "ttle", "spellcheck", "true", "spellcheck.dictionary",
        "default", "spellcheck.build", "true");
    assertQ(request, "//arr[@name='suggestion'][.='title']");

    NamedList args = new NamedList();
    NamedList spellchecker = new NamedList();
    spellchecker.add(AbstractLuceneSpellChecker.DICTIONARY_NAME, "default");
    spellchecker.add(AbstractLuceneSpellChecker.FIELD, "lowerfilt");
    spellchecker.add(AbstractLuceneSpellChecker.INDEX_DIR, "spellchecker1");
    args.add("spellchecker", spellchecker);

    // TODO: this is really fragile and error prone - find a higher level way to test this.
    SpellCheckComponent checker = new SpellCheckComponent();
    checker.init(args);
    checker.inform(h.getCore());

    request = req("qt", "spellCheckCompRH", "q", "*:*", "spellcheck.q", "ttle",
        "spellcheck", "true", "spellcheck.dictionary", "default",
        "spellcheck.reload", "true");
    ResponseBuilder rb = new ResponseBuilder();
    rb.req = request;
    rb.rsp = new SolrQueryResponse();
    rb.components = new ArrayList(h.getCore().getSearchComponents().values());
    checker.prepare(rb);

    try {
      checker.process(rb);
    } catch (NullPointerException e) {
      fail("NullPointerException due to reload not initializing analyzers");
    }

    rb.req.close();
  }
  
    @SuppressWarnings("unchecked")
    @Test
  public void testRebuildOnCommit() throws Exception {
    SolrQueryRequest req = req("q", "lowerfilt:lucenejavt", "qt", "spellCheckCompRH", "spellcheck", "true");
    String response = h.query(req);
    assertFalse("No suggestions should be returned", response.contains("lucenejava"));
    
    assertU(adoc("id", "11231", "lowerfilt", "lucenejava"));
    assertU("commit", commit());
    
    assertQ(req, "//arr[@name='suggestion'][.='lucenejava']");
  }
    
    @Test
    public void testThresholdTokenFrequency() throws Exception {
    	
  	  	//"document" is in 2 documents but "another" is only in 1.  
  	  	//So with a threshold of 29%, "another" is absent from the dictionary 
  	  	//while "document" is present.
    	
  	  	assertJQ(req("qt",rh, SpellCheckComponent.COMPONENT_NAME, "true", "q","documenq", SpellCheckComponent.SPELLCHECK_DICT, "threshold", SpellCheckComponent.SPELLCHECK_COUNT,"5", SpellCheckComponent.SPELLCHECK_EXTENDED_RESULTS,"true")
  	        ,"/spellcheck/suggestions/[1]/suggestion==[{'word':'document','freq':2}]"
  	    );
  	  	
  	  	//TODO:  how do we make this into a 1-liner using "assertQ()" ???
  	  	SolrCore core = h.getCore();
  	  	SearchComponent speller = core.getSearchComponent("spellcheck");
  	  	assertTrue("speller is null and it shouldn't be", speller != null);
  	  	
  	  	ModifiableSolrParams params = new ModifiableSolrParams();		
  			params.add(SpellCheckComponent.COMPONENT_NAME, "true");
  			params.add(SpellCheckComponent.SPELLCHECK_COUNT, "10");	
  			params.add(SpellCheckComponent.SPELLCHECK_DICT, "threshold");
  			params.add(SpellCheckComponent.SPELLCHECK_EXTENDED_RESULTS,"true");
  			params.add(CommonParams.Q, "anotheq");
  			
  			SolrRequestHandler handler = core.getRequestHandler("spellCheckCompRH");
  			SolrQueryResponse rsp = new SolrQueryResponse();
  			rsp.add("responseHeader", new SimpleOrderedMap());
  			SolrQueryRequest req = new LocalSolrQueryRequest(core, params);
  			handler.handleRequest(req, rsp);
  			req.close();
  			NamedList values = rsp.getValues();
  			NamedList spellCheck = (NamedList) values.get("spellcheck");
  			NamedList suggestions = (NamedList) spellCheck.get("suggestions");
  			assertTrue(suggestions.get("suggestion")==null);
  			assertTrue((Boolean) suggestions.get("correctlySpelled")==false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2788.java