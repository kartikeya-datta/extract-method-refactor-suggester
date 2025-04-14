error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3599.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3599.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3599.java
text:
```scala
+ n@@ew File(tmpdir, "x.xsl").toURI(), "url", "cd.xml");

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
package org.apache.solr.handler.dataimport;

import org.junit.Test;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Test for XPathEntityProcessor
 * </p>
 *
 *
 * @since solr 1.3
 */
public class TestXPathEntityProcessor extends AbstractDataImportHandlerTestCase {
  boolean simulateSlowReader;
  boolean simulateSlowResultProcessor;
  int rowsToRead = -1;
  
  @Test
  public void withFieldsAndXpath() throws Exception {
    File tmpdir = File.createTempFile("test", "tmp", TEMP_DIR);
    tmpdir.delete();
    tmpdir.mkdir();
    tmpdir.deleteOnExit();
    createFile(tmpdir, "x.xsl", xsl.getBytes("UTF-8"), false);
    Map entityAttrs = createMap("name", "e", "url", "cd.xml",
            XPathEntityProcessor.FOR_EACH, "/catalog/cd");
    List fields = new ArrayList();
    fields.add(createMap("column", "title", "xpath", "/catalog/cd/title"));
    fields.add(createMap("column", "artist", "xpath", "/catalog/cd/artist"));
    fields.add(createMap("column", "year", "xpath", "/catalog/cd/year"));
    Context c = getContext(null,
            new VariableResolverImpl(), getDataSource(cdData), Context.FULL_DUMP, fields, entityAttrs);
    XPathEntityProcessor xPathEntityProcessor = new XPathEntityProcessor();
    xPathEntityProcessor.init(c);
    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    while (true) {
      Map<String, Object> row = xPathEntityProcessor.nextRow();
      if (row == null)
        break;
      result.add(row);
    }
    assertEquals(3, result.size());
    assertEquals("Empire Burlesque", result.get(0).get("title"));
    assertEquals("Bonnie Tyler", result.get(1).get("artist"));
    assertEquals("1982", result.get(2).get("year"));
  }

  @Test
  public void testMultiValued() throws Exception  {
    Map entityAttrs = createMap("name", "e", "url", "testdata.xml",
            XPathEntityProcessor.FOR_EACH, "/root");
    List fields = new ArrayList();
    fields.add(createMap("column", "a", "xpath", "/root/a", DataImporter.MULTI_VALUED, "true"));
    Context c = getContext(null,
            new VariableResolverImpl(), getDataSource(testXml), Context.FULL_DUMP, fields, entityAttrs);
    XPathEntityProcessor xPathEntityProcessor = new XPathEntityProcessor();
    xPathEntityProcessor.init(c);
    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    while (true) {
      Map<String, Object> row = xPathEntityProcessor.nextRow();
      if (row == null)
        break;
      result.add(row);
    }
    assertEquals(2, ((List)result.get(0).get("a")).size());
  }

  @Test
  public void testMultiValuedFlatten() throws Exception  {
    Map entityAttrs = createMap("name", "e", "url", "testdata.xml",
            XPathEntityProcessor.FOR_EACH, "/root");
    List fields = new ArrayList();
    fields.add(createMap("column", "a", "xpath", "/root/a" ,"flatten","true"));
    Context c = getContext(null,
            new VariableResolverImpl(), getDataSource(testXmlFlatten), Context.FULL_DUMP, fields, entityAttrs);
    XPathEntityProcessor xPathEntityProcessor = new XPathEntityProcessor();
    xPathEntityProcessor.init(c);
    Map<String, Object> result = null;
    while (true) {
      Map<String, Object> row = xPathEntityProcessor.nextRow();
      if (row == null)
        break;
      result = row;
    }
    assertEquals("1B2", result.get("a"));
  }

  @Test
  public void withFieldsAndXpathStream() throws Exception {
    final Object monitor = new Object();
    final boolean[] done = new boolean[1];
    
    Map entityAttrs = createMap("name", "e", "url", "cd.xml",
        XPathEntityProcessor.FOR_EACH, "/catalog/cd", "stream", "true", "batchSize","1");
    List fields = new ArrayList();
    fields.add(createMap("column", "title", "xpath", "/catalog/cd/title"));
    fields.add(createMap("column", "artist", "xpath", "/catalog/cd/artist"));
    fields.add(createMap("column", "year", "xpath", "/catalog/cd/year"));
    Context c = getContext(null,
        new VariableResolverImpl(), getDataSource(cdData), Context.FULL_DUMP, fields, entityAttrs);
    XPathEntityProcessor xPathEntityProcessor = new XPathEntityProcessor() {
      private int count;
      
      @Override
      protected Map<String, Object> readRow(Map<String, Object> record,
          String xpath) {
        synchronized (monitor) {
          if (simulateSlowReader && !done[0]) {
            try {
              monitor.wait(100);
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        }
        
        return super.readRow(record, xpath);
      }
    };
    
    if (simulateSlowResultProcessor) {
      xPathEntityProcessor.blockingQueueSize = 1;
    }
    xPathEntityProcessor.blockingQueueTimeOut = 1;
    xPathEntityProcessor.blockingQueueTimeOutUnits = TimeUnit.MICROSECONDS;
    
    xPathEntityProcessor.init(c);
    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    while (true) {
      if (rowsToRead >= 0 && result.size() >= rowsToRead) {
        Thread.currentThread().interrupt();
      }
      Map<String, Object> row = xPathEntityProcessor.nextRow();
      if (row == null)
        break;
      result.add(row);
      if (simulateSlowResultProcessor) {
        synchronized (xPathEntityProcessor.publisherThread) {
          if (xPathEntityProcessor.publisherThread.isAlive()) {
            xPathEntityProcessor.publisherThread.wait(1000);
          }
        }
      }
    }
    
    synchronized (monitor) {
      done[0] = true;
      monitor.notify();
    }
    
    // confirm that publisher thread stops.
    xPathEntityProcessor.publisherThread.join(1000);
    assertEquals("Expected thread to stop", false, xPathEntityProcessor.publisherThread.isAlive());
    
    assertEquals(rowsToRead < 0 ? 3 : rowsToRead, result.size());
    
    if (rowsToRead < 0) {
      assertEquals("Empire Burlesque", result.get(0).get("title"));
      assertEquals("Bonnie Tyler", result.get(1).get("artist"));
      assertEquals("1982", result.get(2).get("year"));
    }
  }

  @Test
  public void withFieldsAndXpathStreamContinuesOnTimeout() throws Exception {
    simulateSlowReader = true;
    withFieldsAndXpathStream();
  }
  
  @Test
  public void streamWritesMessageAfterBlockedAttempt() throws Exception {
    simulateSlowResultProcessor = true;
    withFieldsAndXpathStream();
  }
  
  @Test
  public void streamStopsAfterInterrupt() throws Exception {
    simulateSlowResultProcessor = true;
    rowsToRead = 1;
    withFieldsAndXpathStream();
  }
  
  @Test
  public void withDefaultSolrAndXsl() throws Exception {
    File tmpdir = File.createTempFile("test", "tmp", TEMP_DIR);
    tmpdir.delete();
    tmpdir.mkdir();
    tmpdir.deleteOnExit();
    AbstractDataImportHandlerTestCase.createFile(tmpdir, "x.xsl", xsl.getBytes("UTF-8"),
            false);
    Map entityAttrs = createMap("name", "e",
            XPathEntityProcessor.USE_SOLR_ADD_SCHEMA, "true", "xsl", ""
            + new File(tmpdir, "x.xsl").getAbsolutePath(), "url", "cd.xml");
    Context c = getContext(null,
            new VariableResolverImpl(), getDataSource(cdData), Context.FULL_DUMP, null, entityAttrs);
    XPathEntityProcessor xPathEntityProcessor = new XPathEntityProcessor();
    xPathEntityProcessor.init(c);
    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    while (true) {
      Map<String, Object> row = xPathEntityProcessor.nextRow();
      if (row == null)
        break;
      result.add(row);
    }
    assertEquals(3, result.size());
    assertEquals("Empire Burlesque", result.get(0).get("title"));
    assertEquals("Bonnie Tyler", result.get(1).get("artist"));
    assertEquals("1982", result.get(2).get("year"));
  }

  private DataSource<Reader> getDataSource(final String xml) {
    return new DataSource<Reader>() {

      @Override
      public void init(Context context, Properties initProps) {
      }

      @Override
      public void close() {
      }

      @Override
      public Reader getData(String query) {
        return new StringReader(xml);
      }
    };
  }

  private static final String xsl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
          + "<xsl:stylesheet version=\"1.0\"\n"
          + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
          + "<xsl:output version='1.0' method='xml' encoding='UTF-8' indent='yes'/>\n"
          + "\n"
          + "<xsl:template match=\"/\">\n"
          + "  <add> \n"
          + "      <xsl:for-each select=\"catalog/cd\">\n"
          + "      <doc>\n"
          + "      <field name=\"title\"><xsl:value-of select=\"title\"/></field>\n"
          + "      <field name=\"artist\"><xsl:value-of select=\"artist\"/></field>\n"
          + "      <field name=\"country\"><xsl:value-of select=\"country\"/></field>\n"
          + "      <field name=\"company\"><xsl:value-of select=\"company\"/></field>      \n"
          + "      <field name=\"price\"><xsl:value-of select=\"price\"/></field>\n"
          + "      <field name=\"year\"><xsl:value-of select=\"year\"/></field>      \n"
          + "      </doc>\n"
          + "      </xsl:for-each>\n"
          + "    </add>  \n"
          + "</xsl:template>\n" + "</xsl:stylesheet>";

  private static final String cdData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
          + "<?xml-stylesheet type=\"text/xsl\" href=\"solr.xsl\"?>\n"
          + "<catalog>\n"
          + "\t<cd>\n"
          + "\t\t<title>Empire Burlesque</title>\n"
          + "\t\t<artist>Bob Dylan</artist>\n"
          + "\t\t<country>USA</country>\n"
          + "\t\t<company>Columbia</company>\n"
          + "\t\t<price>10.90</price>\n"
          + "\t\t<year>1985</year>\n"
          + "\t</cd>\n"
          + "\t<cd>\n"
          + "\t\t<title>Hide your heart</title>\n"
          + "\t\t<artist>Bonnie Tyler</artist>\n"
          + "\t\t<country>UK</country>\n"
          + "\t\t<company>CBS Records</company>\n"
          + "\t\t<price>9.90</price>\n"
          + "\t\t<year>1988</year>\n"
          + "\t</cd>\n"
          + "\t<cd>\n"
          + "\t\t<title>Greatest Hits</title>\n"
          + "\t\t<artist>Dolly Parton</artist>\n"
          + "\t\t<country>USA</country>\n"
          + "\t\t<company>RCA</company>\n"
          + "\t\t<price>9.90</price>\n"
          + "\t\t<year>1982</year>\n" + "\t</cd>\n" + "</catalog>\t";

  private static final String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><a>1</a><a>2</a></root>";

  private static final String testXmlFlatten = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><a>1<b>B</b>2</a></root>";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3599.java