error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/939.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/939.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/939.java
text:
```scala
d@@elCmd.setId(id.toString());

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

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.DeleteUpdateCommand;
import org.apache.solr.update.RollbackUpdateCommand;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 * <p> Writes documents to SOLR. </p>
 * <p/>
 * <b>This API is experimental and may change in the future.</b>
 *
 * @since solr 1.3
 */
public class SolrWriter extends DIHWriterBase implements DIHWriter {
  private static final Logger log = LoggerFactory.getLogger(SolrWriter.class);

  static final String LAST_INDEX_KEY = "last_index_time";

  private final UpdateRequestProcessor processor;

  SolrQueryRequest req;

  public SolrWriter(UpdateRequestProcessor processor, SolrQueryRequest req) {
    this.processor = processor;
    this.req = req;
  }
  
  @Override
  public void close() {
  	try {
  		processor.finish();
  	} catch (IOException e) {
  		throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
  				"Unable to call finish() on UpdateRequestProcessor", e);
  	}
  }
  @Override
  public boolean upload(SolrInputDocument d) {
    try {
      AddUpdateCommand command = new AddUpdateCommand(req);
      command.solrDoc = d;
      processor.processAdd(command);
    } catch (Exception e) {
      log.warn("Error creating document : " + d, e);
      return false;
    }

    return true;
  }
  
  @Override
  public void deleteDoc(Object id) {
    try {
      log.info("Deleting document: " + id);
      DeleteUpdateCommand delCmd = new DeleteUpdateCommand(req);
      delCmd.id = id.toString();
      processor.processDelete(delCmd);
    } catch (IOException e) {
      log.error("Exception while deleteing: " + id, e);
    }
  }
  	
	@Override
  public void deleteByQuery(String query) {
    try {
      log.info("Deleting documents from Solr with query: " + query);
      DeleteUpdateCommand delCmd = new DeleteUpdateCommand(req);
      delCmd.query = query;
      processor.processDelete(delCmd);
    } catch (IOException e) {
      log.error("Exception while deleting by query: " + query, e);
    }
  }

	@Override
  public void commit(boolean optimize) {
    try {
      CommitUpdateCommand commit = new CommitUpdateCommand(req,optimize);
      processor.processCommit(commit);
    } catch (Throwable t) {
      log.error("Exception while solr commit.", t);
    }
  }

	@Override
  public void rollback() {
    try {
      RollbackUpdateCommand rollback = new RollbackUpdateCommand(req);
      processor.processRollback(rollback);
    } catch (Throwable t) {
      log.error("Exception while solr rollback.", t);
    }
  }

	@Override
  public void doDeleteAll() {
    try {
      DeleteUpdateCommand deleteCommand = new DeleteUpdateCommand(req);
      deleteCommand.query = "*:*";
      processor.processDelete(deleteCommand);
    } catch (IOException e) {
      throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
              "Exception in full dump while deleting all documents.", e);
    }
  }

  static String getResourceAsString(InputStream in) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
    byte[] buf = new byte[1024];
    int sz = 0;
    try {
      while ((sz = in.read(buf)) != -1) {
        baos.write(buf, 0, sz);
      }
    } finally {
      try {
        in.close();
      } catch (Exception e) {

      }
    }
    return new String(baos.toByteArray(), "UTF-8");
  }

  static String getDocCount() {
    if (DocBuilder.INSTANCE.get() != null) {
      return ""
              + (DocBuilder.INSTANCE.get().importStatistics.docCount.get() + 1);
    } else {
      return null;
    }
  }
	@Override
	public void init(Context context) {
		/* NO-OP */		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/939.java