error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2242.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2242.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2242.java
text:
```scala
r@@eturn new InputStreamReader(new FileInputStream(file), "UTF-8");

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

import java.io.*;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.solr.handler.dataimport.DataImportHandlerException.wrapAndThrow;
import static org.apache.solr.handler.dataimport.DataImportHandlerException.SEVERE;

/**
 * <p>
 * A {@link DataSource} which reads from local files
 * </p>
 * <p>
 * The file is read with the default platform encoding. It can be overriden by
 * specifying the encoding in solrconfig.xml
 * </p>
 * <p/>
 * <p>
 * Refer to <a
 * href="http://wiki.apache.org/solr/DataImportHandler">http://wiki.apache.org/solr/DataImportHandler</a>
 * for more details.
 * </p>
 * <p/>
 * <b>This API is experimental and may change in the future.</b>
 *
 * @since solr 1.3
 */
public class FileDataSource extends DataSource<Reader> {
  public static final String BASE_PATH = "basePath";

  /**
   * The basePath for this data source
   */
  protected String basePath;

  /**
   * The encoding using which the given file should be read
   */
  protected String encoding = null;

  private static final Logger LOG = LoggerFactory.getLogger(FileDataSource.class);

  @Override
  public void init(Context context, Properties initProps) {
    basePath = initProps.getProperty(BASE_PATH);
    if (initProps.get(URLDataSource.ENCODING) != null)
      encoding = initProps.getProperty(URLDataSource.ENCODING);
  }

  /**
   * <p>
   * Returns a reader for the given file.
   * </p>
   * <p>
   * If the given file is not absolute, we try to construct an absolute path
   * using basePath configuration. If that fails, then the relative path is
   * tried. If file is not found a RuntimeException is thrown.
   * </p>
   * <p>
   * <b>It is the responsibility of the calling method to properly close the
   * returned Reader</b>
   * </p>
   */
  @Override
  public Reader getData(String query) {
    File f = getFile(basePath,query);
    try {
      return openStream(f);
    } catch (Exception e) {
      wrapAndThrow(SEVERE,e,"Unable to open File : "+f.getAbsolutePath());
      return null;
    }
  }

  static File getFile(String basePath, String query) {
    try {
      File file0 = new File(query);
      File file = file0;

      if (!file.isAbsolute())
        file = new File(basePath + query);

      if (file.isFile() && file.canRead()) {
        LOG.debug("Accessing File: " + file.toString());
        return file;
      } else if (file != file0)
        if (file0.isFile() && file0.canRead()) {
          LOG.debug("Accessing File0: " + file0.toString());
          return  file0;
        }

      throw new FileNotFoundException("Could not find file: " + query);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Open a {@link java.io.Reader} for the given file name
   *
   * @param file a {@link java.io.File} instance
   * @return a Reader on the given file
   * @throws FileNotFoundException if the File does not exist
   * @throws UnsupportedEncodingException if the encoding is unsupported
   * @since solr 1.4
   */
  protected Reader openStream(File file) throws FileNotFoundException,
          UnsupportedEncodingException {
    if (encoding == null) {
      return new InputStreamReader(new FileInputStream(file));
    } else {
      return new InputStreamReader(new FileInputStream(file), encoding);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2242.java