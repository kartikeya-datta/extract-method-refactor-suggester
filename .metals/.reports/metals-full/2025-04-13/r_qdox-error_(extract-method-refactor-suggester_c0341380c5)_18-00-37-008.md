error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1432.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1432.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1432.java
text:
```scala
r@@eturn new ByteArrayInputStream( str.getBytes(DEFAULT_CHARSET) );

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

package org.apache.solr.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;


/**
 * Three concrete implementations for ContentStream - one for File/URL/String
 * 
 * @author ryan
 * @version $Id$
 * @since solr 1.2
 */
public abstract class ContentStreamBase implements ContentStream
{
  public static final String DEFAULT_CHARSET = "utf-8";
  
  protected String name;
  protected String sourceInfo;
  protected String contentType;
  protected Long size;
  
  //---------------------------------------------------------------------
  //---------------------------------------------------------------------
  
  public static String getCharsetFromContentType( String contentType )
  {
    if( contentType != null ) {
      int idx = contentType.toLowerCase().indexOf( "charset=" );
      if( idx > 0 ) {
        return contentType.substring( idx + "charset=".length() ).trim();
      }
    }
    return null;
  }
  
  //------------------------------------------------------------------------
  //------------------------------------------------------------------------
  
  /**
   * Construct a <code>ContentStream</code> from a <code>URL</code>
   * 
   * This uses a <code>URLConnection</code> to get the content stream
   * @see  URLConnection
   */
  public static class URLStream extends ContentStreamBase
  {
    private final URL url;
    final URLConnection conn;
    
    public URLStream( URL url ) throws IOException {
      this.url = url; 
      this.conn = this.url.openConnection();
      
      contentType = conn.getContentType();
      name = url.toExternalForm();
      size = new Long( conn.getContentLength() );
      sourceInfo = "url";
    }

    public InputStream getStream() throws IOException {
      return conn.getInputStream();
    }
  }
  
  /**
   * Construct a <code>ContentStream</code> from a <code>File</code>
   */
  public static class FileStream extends ContentStreamBase
  {
    private final File file;
    
    public FileStream( File f ) throws IOException {
      file = f; 
      
      contentType = null; // ??
      name = file.getName();
      size = file.length();
      sourceInfo = file.toURI().toString();
    }

    public InputStream getStream() throws IOException {
      return new FileInputStream( file );
    }

    /**
     * If an charset is defined (by the contentType) use that, otherwise 
     * use a file reader
     */
    @Override
    public Reader getReader() throws IOException {
      String charset = getCharsetFromContentType( contentType );
      return charset == null 
        ? new FileReader( file )
        : new InputStreamReader( getStream(), charset );
    }
  }
  

  /**
   * Construct a <code>ContentStream</code> from a <code>File</code>
   */
  public static class StringStream extends ContentStreamBase
  {
    private final String str;
    
    public StringStream( String str ) {
      this.str = str; 
      
      contentType = null;
      name = null;
      size = new Long( str.length() );
      sourceInfo = "string";
    }

    public InputStream getStream() throws IOException {
      return new ByteArrayInputStream( str.getBytes() );
    }

    /**
     * If an charset is defined (by the contentType) use that, otherwise 
     * use a StringReader
     */
    @Override
    public Reader getReader() throws IOException {
      String charset = getCharsetFromContentType( contentType );
      return charset == null 
        ? new StringReader( str )
        : new InputStreamReader( getStream(), charset );
    }
  }

  /**
   * Base reader implementation.  If the contentType declares a 
   * charset use it, otherwise use "utf-8".
   */
  public Reader getReader() throws IOException {
    String charset = getCharsetFromContentType( getContentType() );
    return charset == null 
      ? new InputStreamReader( getStream(), DEFAULT_CHARSET )
      : new InputStreamReader( getStream(), charset );
  }

  //------------------------------------------------------------------
  // Getters / Setters for overrideable attributes
  //------------------------------------------------------------------

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public String getSourceInfo() {
    return sourceInfo;
  }

  public void setSourceInfo(String sourceInfo) {
    this.sourceInfo = sourceInfo;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1432.java