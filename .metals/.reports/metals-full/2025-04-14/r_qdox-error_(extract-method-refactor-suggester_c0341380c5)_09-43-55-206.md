error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/570.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/570.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/570.java
text:
```scala
public static S@@tring simpleTag(String tag, String... args) {

package org.apache.solr.util;
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

import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.XML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

abstract public class BaseTestHarness {
  private final ThreadLocal<DocumentBuilder> builderTL = new ThreadLocal<DocumentBuilder>();
  private final ThreadLocal<XPath> xpathTL = new ThreadLocal<XPath>();

  public DocumentBuilder getXmlDocumentBuilder() {
    try {
      DocumentBuilder builder = builderTL.get();
      if (builder == null) {
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builderTL.set(builder);
      }
      return builder;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public XPath getXpath() {
    try {
      XPath xpath = xpathTL.get();
      if (xpath == null) {
        xpath = XPathFactory.newInstance().newXPath();
        xpathTL.set(xpath);
      }
      return xpath;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * A helper method which validates a String against an array of XPath test
   * strings.
   *
   * @param xml The xml String to validate
   * @param tests Array of XPath strings to test (in boolean mode) on the xml
   * @return null if all good, otherwise the first test that fails.
   */
  public String validateXPath(String xml, String... tests)
      throws XPathExpressionException, SAXException {

    if (tests==null || tests.length == 0) return null;

    Document document = null;
    try {
      document = getXmlDocumentBuilder().parse(new ByteArrayInputStream
          (xml.getBytes("UTF-8")));
    } catch (UnsupportedEncodingException e1) {
      throw new RuntimeException("Totally weird UTF-8 exception", e1);
    } catch (IOException e2) {
      throw new RuntimeException("Totally weird io exception", e2);
    }

    for (String xp : tests) {
      xp=xp.trim();
      Boolean bool = (Boolean) getXpath().evaluate(xp, document, XPathConstants.BOOLEAN);

      if (!bool) {
        return xp;
      }
    }
    return null;
  }

  /**
   * A helper that creates an xml &lt;doc&gt; containing all of the
   * fields and values specified
   *
   * @param fieldsAndValues 0 and Even numbered args are fields names odds are field values.
   */
  public static StringBuffer makeSimpleDoc(String... fieldsAndValues) {

    try {
      StringWriter w = new StringWriter();
      w.append("<doc>");
      for (int i = 0; i < fieldsAndValues.length; i+=2) {
        XML.writeXML(w, "field", fieldsAndValues[i + 1], "name",
            fieldsAndValues[i]);
      }
      w.append("</doc>");
      return w.getBuffer();
    } catch (IOException e) {
      throw new RuntimeException
          ("this should never happen with a StringWriter", e);
    }
  }

  /**
   * Generates a delete by query xml string
   * @param q Query that has not already been xml escaped
   * @param args The attributes of the delete tag
   */
  public static String deleteByQuery(String q, String... args) {
    try {
      StringWriter r = new StringWriter();
      XML.writeXML(r, "query", q);
      return delete(r.getBuffer().toString(), args);
    } catch(IOException e) {
      throw new RuntimeException
          ("this should never happen with a StringWriter", e);
    }
  }

  /**
   * Generates a delete by id xml string
   * @param id ID that has not already been xml escaped
   * @param args The attributes of the delete tag
   */
  public static String deleteById(String id, String... args) {
    try {
      StringWriter r = new StringWriter();
      XML.writeXML(r, "id", id);
      return delete(r.getBuffer().toString(), args);
    } catch(IOException e) {
      throw new RuntimeException
          ("this should never happen with a StringWriter", e);
    }
  }

  /**
   * Generates a delete xml string
   * @param val text that has not already been xml escaped
   * @param args 0 and Even numbered args are params, Odd numbered args are XML escaped values.
   */
  private static String delete(String val, String... args) {
    try {
      StringWriter r = new StringWriter();
      XML.writeUnescapedXML(r, "delete", val, (Object[]) args);
      return r.getBuffer().toString();
    } catch(IOException e) {
      throw new RuntimeException
          ("this should never happen with a StringWriter", e);
    }
  }

  /**
   * Helper that returns an &lt;optimize&gt; String with
   * optional key/val pairs.
   *
   * @param args 0 and Even numbered args are params, Odd numbered args are values.
   */
  public static String optimize(String... args) {
    return simpleTag("optimize", args);
  }

  private static String simpleTag(String tag, String... args) {
    try {
      StringWriter r = new StringWriter();

      // this is annoying
      if (null == args || 0 == args.length) {
        XML.writeXML(r, tag, null);
      } else {
        XML.writeXML(r, tag, null, (Object[])args);
      }
      return r.getBuffer().toString();
    } catch (IOException e) {
      throw new RuntimeException
          ("this should never happen with a StringWriter", e);
    }
  }

  /**
   * Helper that returns an &lt;commit&gt; String with
   * optional key/val pairs.
   *
   * @param args 0 and Even numbered args are params, Odd numbered args are values.
   */
  public static String commit(String... args) {
    return simpleTag("commit", args);
  }

  /** Reloads the core */
  abstract public void reload() throws Exception;

  /**
   * Processes an "update" (add, commit or optimize) and
   * returns the response as a String.
   * 
   * This method does NOT commit after the request.
   *
   * @param xml The XML of the update
   * @return The XML response to the update
   */
  abstract public String update(String xml);

  /**
   * Validates that an "update" (add, commit or optimize) results in success.
   *
   * :TODO: currently only deals with one add/doc at a time, this will need changed if/when SOLR-2 is resolved
   *
   * @param xml The XML of the update
   * @return null if successful, otherwise the XML response to the update
   */
  public String validateUpdate(String xml) throws SAXException {
    return checkUpdateStatus(xml, "0");
  }

  /**
   * Validates that an "update" (add, commit or optimize) results in success.
   *
   * :TODO: currently only deals with one add/doc at a time, this will need changed if/when SOLR-2 is resolved
   *
   * @param xml The XML of the update
   * @return null if successful, otherwise the XML response to the update
   */
  public String validateErrorUpdate(String xml) throws SAXException {
    try {
      return checkUpdateStatus(xml, "1");
    } catch (SolrException e) {
      // return ((SolrException)e).getMessage();
      return null;  // success
    }
  }

  /**
   * Validates that an "update" (add, commit or optimize) results in success.
   *
   * :TODO: currently only deals with one add/doc at a time, this will need changed if/when SOLR-2 is resolved
   *
   * @param xml The XML of the update
   * @return null if successful, otherwise the XML response to the update
   */
  public String checkUpdateStatus(String xml, String code) throws SAXException {
    try {
      String res = update(xml);
      String valid = validateXPath(res, "//int[@name='status']="+code );
      return (null == valid) ? null : res;
    } catch (XPathExpressionException e) {
      throw new RuntimeException
          ("?!? static xpath has bug?", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/570.java