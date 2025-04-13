error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/311.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/311.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/311.java
text:
```scala
F@@ile tmpFile = _TestUtil.createTempFile("solr.xml", null, TEMP_DIR);

package org.apache.solr.core;

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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;
import org.apache.solr.core.SolrXMLSerializer.SolrCoreXMLDef;
import org.apache.solr.core.SolrXMLSerializer.SolrXMLDef;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class TestSolrXMLSerializer extends LuceneTestCase {
  private static final XPathFactory xpathFactory = XPathFactory.newInstance();
  private static final String defaultCoreNameKey = "defaultCoreName";
  private static final String defaultCoreNameVal = "collection1";
  private static final String peristentKey = "persistent";
  private static final String persistentVal = "true";
  private static final String sharedLibKey = "sharedLib";
  private static final String sharedLibVal = "true";
  private static final String adminPathKey = "adminPath";
  private static final String adminPathVal = "/admin";
  private static final String shareSchemaKey = "admin";
  private static final String shareSchemaVal = "true";
  private static final String instanceDirKey = "instanceDir";
  private static final String instanceDirVal = "core1";
  
  @Test
  public void basicUsageTest() throws Exception {
    SolrXMLSerializer serializer = new SolrXMLSerializer();
    
    SolrXMLDef solrXMLDef = getTestSolrXMLDef(defaultCoreNameKey,
        defaultCoreNameVal, peristentKey, persistentVal, sharedLibKey,
        sharedLibVal, adminPathKey, adminPathVal, shareSchemaKey,
        shareSchemaVal, instanceDirKey, instanceDirVal);
    
    Writer w = new StringWriter();
    try {
      serializer.persist(w, solrXMLDef);
    } finally {
      w.close();
    }
    
    assertResults(((StringWriter) w).getBuffer().toString().getBytes("UTF-8"));
    
    // again with default file
    File tmpFile = _TestUtil.getTempDir("solr.xml");
    
    serializer.persistFile(tmpFile, solrXMLDef);

    assertResults(FileUtils.readFileToString(tmpFile, "UTF-8").getBytes("UTF-8"));
    tmpFile.delete();
  }

  private void assertResults(byte[] bytes)
      throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(bytes));
    Document document;
    try {
//      is.mark(0);
//      System.out.println("SolrXML:" + IOUtils.toString(is, "UTF-8"));
//      is.reset();
      document = builder.parse(is);
    } finally {
      is.close();
    }
    
    assertTrue(exists("/solr[@" + peristentKey + "='" + persistentVal + "']", document));
    assertTrue(exists("/solr[@" + sharedLibKey + "='" + sharedLibVal + "']", document));
    assertTrue(exists("/solr/cores[@" + defaultCoreNameKey + "='" + defaultCoreNameVal + "']", document));
    assertTrue(exists("/solr/cores[@" + adminPathKey + "='" + adminPathVal + "']", document));
    assertTrue(exists("/solr/cores/core[@" + instanceDirKey + "='" + instanceDirVal + "']", document));
  }

  private SolrXMLDef getTestSolrXMLDef(String defaultCoreNameKey,
      String defaultCoreNameVal, String peristentKey, String persistentVal,
      String sharedLibKey, String sharedLibVal, String adminPathKey,
      String adminPathVal, String shareSchemaKey, String shareSchemaVal,
      String instanceDirKey, String instanceDirVal) {
    // <solr attrib="value">
    Map<String,String> rootSolrAttribs = new HashMap<String,String>();
    rootSolrAttribs.put(sharedLibKey, sharedLibVal);
    rootSolrAttribs.put(peristentKey, persistentVal);
    
    // <solr attrib="value"> <cores attrib="value">
    Map<String,String> coresAttribs = new HashMap<String,String>();
    coresAttribs.put(adminPathKey, adminPathVal);
    coresAttribs.put(shareSchemaKey, shareSchemaVal);
    coresAttribs.put(defaultCoreNameKey, defaultCoreNameVal);
    
    SolrXMLDef solrXMLDef = new SolrXMLDef();
    
    // <solr attrib="value"> <cores attrib="value"> <core attrib="value">
    List<SolrCoreXMLDef> solrCoreXMLDefs = new ArrayList<SolrCoreXMLDef>();
    SolrCoreXMLDef coreDef = new SolrCoreXMLDef();
    Map<String,String> coreAttribs = new HashMap<String,String>();
    coreAttribs.put(instanceDirKey, instanceDirVal);
    coreDef.coreAttribs = coreAttribs ;
    coreDef.coreProperties = new Properties();
    solrCoreXMLDefs.add(coreDef);
    
    solrXMLDef.coresDefs = solrCoreXMLDefs ;
    Properties containerProperties = new Properties();
    solrXMLDef.containerProperties = containerProperties ;
    solrXMLDef.solrAttribs = rootSolrAttribs;
    solrXMLDef.coresAttribs = coresAttribs;
    return solrXMLDef;
  }
  
  public static boolean exists(String xpathStr, Node node)
      throws XPathExpressionException {
    XPath xpath = xpathFactory.newXPath();
    return (Boolean) xpath.evaluate(xpathStr, node, XPathConstants.BOOLEAN);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/311.java