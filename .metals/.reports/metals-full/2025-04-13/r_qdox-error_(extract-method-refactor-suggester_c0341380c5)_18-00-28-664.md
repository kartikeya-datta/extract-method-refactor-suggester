error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2243.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2243.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2243.java
text:
```scala
r@@eturn (new InputStreamReader(blob.getBinaryStream(), "UTF-8"));

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
package org.apache.solr.handler.dataimport;

import static org.apache.solr.handler.dataimport.DataImportHandlerException.SEVERE;
import static org.apache.solr.handler.dataimport.DataImportHandlerException.wrapAndThrow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This can be useful for users who have a DB field containing xml and wish to use a nested {@link XPathEntityProcessor}
 * <p/>
 * The datasouce may be configured as follows
 * <p/>
 * &lt;datasource name="f1" type="FieldReaderDataSource" /&gt;
 * <p/>
 * The enity which uses this datasource must keep the url value as the variable name url="field-name"
 * <p/>
 * The fieldname must be resolvable from {@link VariableResolver}
 * <p/>
 * This may be used with any {@link EntityProcessor} which uses a {@link DataSource}&lt;{@link Reader}&gt; eg: {@link XPathEntityProcessor}
 * <p/>
 * Supports String, BLOB, CLOB data types and there is an extra field (in the entity) 'encoding' for BLOB types
 *
 * @since 1.4
 */
public class FieldReaderDataSource extends DataSource<Reader> {
  private static final Logger LOG = LoggerFactory.getLogger(FieldReaderDataSource.class);
  protected VariableResolver vr;
  protected String dataField;
  private String encoding;
  private EntityProcessorWrapper entityProcessor;

  @Override
  public void init(Context context, Properties initProps) {
    dataField = context.getEntityAttribute("dataField");
    encoding = context.getEntityAttribute("encoding");
    entityProcessor = (EntityProcessorWrapper) context.getEntityProcessor();
    /*no op*/
  }

  @Override
  public Reader getData(String query) {
    Object o = entityProcessor.getVariableResolver().resolve(dataField);
    if (o == null) {
       throw new DataImportHandlerException (SEVERE, "No field available for name : " +dataField);
    }
    if (o instanceof String) {
      return new StringReader((String) o);
    } else if (o instanceof Clob) {
      Clob clob = (Clob) o;
      try {
        //Most of the JDBC drivers have getCharacterStream defined as public
        // so let us just check it
        return readCharStream(clob);
      } catch (Exception e) {
        LOG.info("Unable to get data from CLOB");
        return null;

      }

    } else if (o instanceof Blob) {
      Blob blob = (Blob) o;
      try {
        return getReader(blob);
      } catch (Exception e) {
        LOG.info("Unable to get data from BLOB");
        return null;

      }
    } else {
      return new StringReader(o.toString());
    }

  }

  static Reader readCharStream(Clob clob) {
    try {
      return clob.getCharacterStream();
    } catch (Exception e) {
      wrapAndThrow(SEVERE, e,"Unable to get reader from clob");
      return null;//unreachable
    }
  }

  private Reader getReader(Blob blob)
          throws SQLException, UnsupportedEncodingException {
    if (encoding == null) {
      return (new InputStreamReader(blob.getBinaryStream()));
    } else {
      return (new InputStreamReader(blob.getBinaryStream(), encoding));
    }
  }

  @Override
  public void close() {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2243.java