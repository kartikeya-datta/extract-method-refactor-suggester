error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2421.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2421.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2421.java
text:
```scala
S@@tring errString = "Not Found";

package org.apache.solr.schema;
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

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.cloud.AbstractFullDistribZkTestBase;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.regex.Pattern;

public class TestCloudManagedSchema extends AbstractFullDistribZkTestBase {

  public TestCloudManagedSchema() {
    super();
  }

  @BeforeClass
  public static void initSysProperties() {
    System.setProperty("managed.schema.mutable", "false");
    System.setProperty("enable.update.log", "true");
  }

  @Override
  protected String getCloudSolrConfig() {
    return "solrconfig-managed-schema.xml";
  }
      
  @Override
  public void doTest() throws Exception {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set(CoreAdminParams.ACTION, CoreAdminParams.CoreAdminAction.STATUS.toString());
    QueryRequest request = new QueryRequest(params);
    request.setPath("/admin/cores");
    int which = r.nextInt(clients.size());
    HttpSolrServer client = (HttpSolrServer)clients.get(which);
    String previousBaseURL = client.getBaseURL();
    // Strip /collection1 step from baseURL - requests fail otherwise
    client.setBaseURL(previousBaseURL.substring(0, previousBaseURL.lastIndexOf("/")));
    NamedList namedListResponse = client.request(request);
    client.setBaseURL(previousBaseURL); // Restore baseURL 
    NamedList status = (NamedList)namedListResponse.get("status");
    NamedList collectionStatus = (NamedList)status.get("collection1");
    String collectionSchema = (String)collectionStatus.get(CoreAdminParams.SCHEMA);
    // Make sure the upgrade to managed schema happened
    assertEquals("Schema resource name differs from expected name", "managed-schema", collectionSchema);
    
    // Make sure "DO NOT EDIT" is in the content of the managed schema
    String fileContent = getFileContentFromZooKeeper("managed-schema");
    assertTrue("Managed schema is missing", fileContent.contains("DO NOT EDIT"));
    
    // Make sure the original non-managed schema is no longer in ZooKeeper
    assertFileNotInZooKeeper("schema.xml");

    // Make sure the renamed non-managed schema is present in ZooKeeper
    fileContent = getFileContentFromZooKeeper("schema.xml.bak");
    assertTrue("schema file doesn't contain '<schema'", fileContent.contains("<schema"));
  }
  
  private String getFileContentFromZooKeeper(String fileName) throws IOException, SolrServerException {
    QueryRequest request = new QueryRequest(params("file", fileName));
    request.setPath("/admin/file");
    RawResponseParser responseParser = new RawResponseParser();
    request.setResponseParser(responseParser);
    int which = r.nextInt(clients.size());
    // For some reason, /admin/file requests work without stripping the /collection1 step from the URL
    // (unlike /admin/cores requests - see above)
    SolrServer client = clients.get(which);
    client.request(request);
    return responseParser.getRawFileContent();   
  }
  
  private class RawResponseParser extends ResponseParser {
    // Stolen from ShowFileRequestHandlerTest
    private String rawFileContent = null;
    String getRawFileContent() { return rawFileContent; }
    @Override
    public String getWriterType() {
      return "mock";//unfortunately this gets put onto params wt=mock but it apparently has no effect
    }
    @Override
    public NamedList<Object> processResponse(InputStream body, String encoding) {
      try {
        rawFileContent = IOUtils.toString(body, encoding);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return null;
    }
    @Override
    public NamedList<Object> processResponse(Reader reader) {
      throw new UnsupportedOperationException("TODO unimplemented");//TODO
    }
  }

  protected final void assertFileNotInZooKeeper(String fileName) throws Exception {
    // Stolen from AbstractBadConfigTestBase
    String errString = "returned non ok status:404, message:Not Found";
    ignoreException(Pattern.quote(errString));
    String rawContent = null;
    try {
      rawContent = getFileContentFromZooKeeper(fileName);
    } catch (Exception e) {
      // short circuit out if we found what we expected
      if (-1 != e.getMessage().indexOf(errString)) return;
      // otherwise, rethrow it, possibly completely unrelated
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, 
                              "Unexpected error, expected error matching: " + errString, e);
    } finally {
      resetExceptionIgnores();
    }
    fail("File '" + fileName + "' was unexpectedly found in ZooKeeper.  Content starts with '" 
        + rawContent.substring(0, 100) + " [...]'");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2421.java