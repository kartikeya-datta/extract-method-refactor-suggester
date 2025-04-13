error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/403.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/403.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/403.java
text:
```scala
public static v@@oid afterClass() {

package org.apache.solr.handler.dataimport;
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

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Tests exception handling during imports in DataImportHandler
 *
 *
 * @since solr 1.4
 */
public class TestErrorHandling extends AbstractDataImportHandlerTestCase {

  //TODO: fix this test to not require FSDirectory.
  static String savedFactory;
  @BeforeClass
  public static void beforeClass() throws Exception {
    savedFactory = System.getProperty("solr.DirectoryFactory");
    System.setProperty("solr.directoryFactory", "solr.MockFSDirectoryFactory");
    initCore("dataimport-solrconfig.xml", "dataimport-schema.xml");
    ignoreException("Unexpected close tag");
  }
  
  @AfterClass
  public static void afterClass() throws Exception {
    if (savedFactory == null) {
      System.clearProperty("solr.directoryFactory");
    } else {
      System.setProperty("solr.directoryFactory", savedFactory);
    }
  }
  
  @Before @Override
  public void setUp() throws Exception {
    super.setUp();
    clearIndex();
    assertU(commit());
  }
  
  public void testMalformedStreamingXml() throws Exception {
    StringDataSource.xml = malformedXml;
    runFullImport(dataConfigWithStreaming);
    assertQ(req("id:1"), "//*[@numFound='1']");
    assertQ(req("id:2"), "//*[@numFound='1']");
  }

  public void testMalformedNonStreamingXml() throws Exception {
    StringDataSource.xml = malformedXml;
    runFullImport(dataConfigWithoutStreaming);
    assertQ(req("id:1"), "//*[@numFound='1']");
    assertQ(req("id:2"), "//*[@numFound='1']");
  }

  public void testAbortOnError() throws Exception {
    StringDataSource.xml = malformedXml;
    runFullImport(dataConfigAbortOnError);
    assertQ(req("*:*"), "//*[@numFound='0']");
  }

  public void testTransformerErrorContinue() throws Exception {
    StringDataSource.xml = wellformedXml;
    List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
    rows.add(createMap("id", "3", "desc", "exception-transformer"));
    MockDataSource.setIterator("select * from foo", rows.iterator());
    runFullImport(dataConfigWithTransformer);
    assertQ(req("*:*"), "//*[@numFound='3']");
  }

  public static class StringDataSource extends DataSource<Reader> {
    public static String xml = "";

    @Override
    public void init(Context context, Properties initProps) {
    }

    @Override
    public Reader getData(String query) {
      return new StringReader(xml);
    }

    @Override
    public void close() {

    }
  }

  public static class ExceptionTransformer extends Transformer {
    @Override
    public Object transformRow(Map<String, Object> row, Context context) {
      throw new RuntimeException("Test exception");
    }
  }

  private String dataConfigWithStreaming = "<dataConfig>\n" +
          "        <dataSource name=\"str\" type=\"TestErrorHandling$StringDataSource\" />" +
          "    <document>\n" +
          "        <entity name=\"node\" dataSource=\"str\" processor=\"XPathEntityProcessor\" url=\"test\" stream=\"true\" forEach=\"/root/node\" onError=\"skip\">\n" +
          "            <field column=\"id\" xpath=\"/root/node/id\" />\n" +
          "            <field column=\"desc\" xpath=\"/root/node/desc\" />\n" +
          "        </entity>\n" +
          "    </document>\n" +
          "</dataConfig>";

  private String dataConfigWithoutStreaming = "<dataConfig>\n" +
          "        <dataSource name=\"str\" type=\"TestErrorHandling$StringDataSource\" />" +
          "    <document>\n" +
          "        <entity name=\"node\" dataSource=\"str\" processor=\"XPathEntityProcessor\" url=\"test\" forEach=\"/root/node\" onError=\"skip\">\n" +
          "            <field column=\"id\" xpath=\"/root/node/id\" />\n" +
          "            <field column=\"desc\" xpath=\"/root/node/desc\" />\n" +
          "        </entity>\n" +
          "    </document>\n" +
          "</dataConfig>";

  private String dataConfigAbortOnError = "<dataConfig>\n" +
          "        <dataSource name=\"str\" type=\"TestErrorHandling$StringDataSource\" />" +
          "    <document>\n" +
          "        <entity name=\"node\" dataSource=\"str\" processor=\"XPathEntityProcessor\" url=\"test\" forEach=\"/root/node\" onError=\"abort\">\n" +
          "            <field column=\"id\" xpath=\"/root/node/id\" />\n" +
          "            <field column=\"desc\" xpath=\"/root/node/desc\" />\n" +
          "        </entity>\n" +
          "    </document>\n" +
          "</dataConfig>";

  private String dataConfigWithTransformer = "<dataConfig>\n" +
          "        <dataSource name=\"str\" type=\"TestErrorHandling$StringDataSource\" />" +
          "<dataSource  type=\"MockDataSource\"/>" +
          "    <document>\n" +
          "        <entity name=\"node\" dataSource=\"str\" processor=\"XPathEntityProcessor\" url=\"test\" forEach=\"/root/node\">\n" +
          "            <field column=\"id\" xpath=\"/root/node/id\" />\n" +
          "            <field column=\"desc\" xpath=\"/root/node/desc\" />\n" +
          "            <entity name=\"child\" query=\"select * from foo\" transformer=\"TestErrorHandling$ExceptionTransformer\" onError=\"continue\">\n" +
          "            </entity>" +
          "        </entity>\n" +
          "    </document>\n" +
          "</dataConfig>";

  private String malformedXml = "<root>\n" +
          "    <node>\n" +
          "        <id>1</id>\n" +
          "        <desc>test1</desc>\n" +
          "    </node>\n" +
          "    <node>\n" +
          "        <id>2</id>\n" +
          "        <desc>test2</desc>\n" +
          "    </node>\n" +
          "    <node>\n" +
          "        <id/>3</id>\n" +
          "        <desc>test3</desc>\n" +
          "    </node>\n" +
          "</root>";

  private String wellformedXml = "<root>\n" +
          "    <node>\n" +
          "        <id>1</id>\n" +
          "        <desc>test1</desc>\n" +
          "    </node>\n" +
          "    <node>\n" +
          "        <id>2</id>\n" +
          "        <desc>test2</desc>\n" +
          "    </node>\n" +
          "    <node>\n" +
          "        <id>3</id>\n" +
          "        <desc>test3</desc>\n" +
          "    </node>\n" +
          "</root>";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/403.java