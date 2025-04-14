error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1952.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1952.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1952.java
text:
```scala
I@@ndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);

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

package org.apache.lucene.demo.xmlparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.xml.CorePlusExtensionsParser;
import org.apache.lucene.queryparser.xml.QueryTemplateManager;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;

/**
 * Example servlet that uses the XML queryparser.
 * <p>
 * NOTE: you must provide CSV data in <code>/WEB-INF/data.tsv</code>
 * for the demo to work!
 */
public class FormBasedXmlQueryDemo extends HttpServlet {

  private QueryTemplateManager queryTemplateManager;
  private CorePlusExtensionsParser xmlParser;
  private IndexSearcher searcher;
  private Analyzer analyzer = new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_CURRENT);

  /** for instantiation by the servlet container */
  public FormBasedXmlQueryDemo() {}

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    try {
      openExampleIndex();

      //load servlet configuration settings
      String xslFile = config.getInitParameter("xslFile");
      String defaultStandardQueryParserField = config.getInitParameter("defaultStandardQueryParserField");


      //Load and cache choice of XSL query template using QueryTemplateManager
      queryTemplateManager = new QueryTemplateManager(
          getServletContext().getResourceAsStream("/WEB-INF/" + xslFile));

      //initialize an XML Query Parser for use by all threads
      xmlParser = new CorePlusExtensionsParser(defaultStandardQueryParserField, analyzer);
    } catch (Exception e) {
      throw new ServletException("Error loading query template", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //Take all completed form fields and add to a Properties object
    Properties completedFormFields = new Properties();
    Enumeration<?> pNames = request.getParameterNames();
    while (pNames.hasMoreElements()) {
      String propName = (String) pNames.nextElement();
      String value = request.getParameter(propName);
      if ((value != null) && (value.trim().length() > 0)) {
        completedFormFields.setProperty(propName, value);
      }
    }

    try {
      //Create an XML query by populating template with given user criteria
      org.w3c.dom.Document xmlQuery = queryTemplateManager.getQueryAsDOM(completedFormFields);

      //Parse the XML to produce a Lucene query
      Query query = xmlParser.getQuery(xmlQuery.getDocumentElement());

      //Run the query
      TopDocs topDocs = searcher.search(query, 10);

      //and package the results and forward to JSP
      if (topDocs != null) {
        ScoreDoc[] sd = topDocs.scoreDocs;
        Document[] results = new Document[sd.length];
        for (int i = 0; i < results.length; i++) {
          results[i] = searcher.doc(sd[i].doc);
          request.setAttribute("results", results);
        }
      }
      RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
      dispatcher.forward(request, response);
    }
    catch (Exception e) {
      throw new ServletException("Error processing query", e);
    }
  }

  private void openExampleIndex() throws IOException {
    //Create a RAM-based index from our test data file
    RAMDirectory rd = new RAMDirectory();
    IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_40, analyzer);
    IndexWriter writer = new IndexWriter(rd, iwConfig);
    InputStream dataIn = getServletContext().getResourceAsStream("/WEB-INF/data.tsv");
    BufferedReader br = new BufferedReader(new InputStreamReader(dataIn, IOUtils.CHARSET_UTF_8));
    String line = br.readLine();
    final FieldType textNoNorms = new FieldType(TextField.TYPE_STORED);
    textNoNorms.setOmitNorms(true);
    while (line != null) {
      line = line.trim();
      if (line.length() > 0) {
        //parse row and create a document
        StringTokenizer st = new StringTokenizer(line, "\t");
        Document doc = new Document();
        doc.add(new Field("location", st.nextToken(), textNoNorms));
        doc.add(new Field("salary", st.nextToken(), textNoNorms));
        doc.add(new Field("type", st.nextToken(), textNoNorms));
        doc.add(new Field("description", st.nextToken(), textNoNorms));
        writer.addDocument(doc);
      }
      line = br.readLine();
    }
    writer.close();

    //open searcher
    // this example never closes it reader!
    IndexReader reader = DirectoryReader.open(rd);
    searcher = new IndexSearcher(reader);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1952.java