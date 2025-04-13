error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2560.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2560.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2560.java
text:
```scala
r@@eturn IOUtils.toString( input, "UTF-8" );

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

package org.apache.solr.handler.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.RawResponseWriter;
import org.apache.solr.response.SolrQueryResponse;

/**
 * This handler uses the RawResponseWriter to give client access to
 * files inside ${solr.home}/conf
 * 
 * If you want to selectively restrict access some configuration files, you can list
 * these files in the {@link #HIDDEN} invariants.  For example to hide 
 * synonyms.txt and anotherfile.txt, you would register:
 * 
 * <pre>
 * &lt;requestHandler name="/admin/file" class="org.apache.solr.handler.admin.ShowFileRequestHandler" &gt;
 *   &lt;lst name="defaults"&gt;
 *    &lt;str name="echoParams"&gt;explicit&lt;/str&gt;
 *   &lt;/lst&gt;
 *   &lt;lst name="invariants"&gt;
 *    &lt;str name="hidden"&gt;synonyms.txt&lt;/str&gt; 
 *    &lt;str name="hidden"&gt;anotherfile.txt&lt;/str&gt; 
 *   &lt;/lst&gt;
 * &lt;/requestHandler&gt;
 * </pre>
 * 
 * The ShowFileRequestHandler uses the {@link RawResponseWriter} (wt=raw) to return
 * file contents.  If you need to use a different writer, you will need to change 
 * the registered invarient param for wt.
 * 
 * If you want to override the contentType header returned for a given file, you can
 * set it directly using: {@link #USE_CONTENT_TYPE}.  For example, to get a plain text 
 * version of schema.xml, try:
 * <pre>
 *   http://localhost:8983/solr/admin/file?file=schema.xml&contentType=text/plain
 * </pre>
 * 
 * @version $Id$
 * @since solr 1.3
 */
public class ShowFileRequestHandler extends RequestHandlerBase
{
  public static final String HIDDEN = "hidden";
  public static final String USE_CONTENT_TYPE = "contentType";
  
  protected Set<String> hiddenFiles;
  
  private static ShowFileRequestHandler instance;
  public ShowFileRequestHandler()
  {
    super();
    instance = this; // used so that getFileContents can access hiddenFiles
  }

  @Override
  public void init(NamedList args) {
    super.init( args );
    
    // by default, use wt=raw
    ModifiableSolrParams params = new ModifiableSolrParams( invariants );
    if( params.get( CommonParams.WT ) == null ) {
      params.set( CommonParams.WT, "raw" );
    }
    this.invariants = params;
    
    // Build a list of hidden files
    hiddenFiles = new HashSet<String>();
    if( invariants != null ) {
      String[] hidden = invariants.getParams( HIDDEN );
      if( hidden != null ) {
        for( String s : hidden ) {
          hiddenFiles.add( s.toUpperCase(Locale.ENGLISH) );
        }
      }
    }
  }
  
  public Set<String> getHiddenFiles()
  {
    return hiddenFiles;
  }
  
  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws IOException 
  {
    File adminFile = null;
    
    final SolrResourceLoader loader = req.getCore().getResourceLoader();
    File configdir = new File( loader.getConfigDir() );
    if (!configdir.exists()) {
      // TODO: maybe we should just open it this way to start with?
      try {
        configdir = new File( loader.getClassLoader().getResource(loader.getConfigDir()).toURI() );
      } catch (URISyntaxException e) {
        throw new SolrException( ErrorCode.FORBIDDEN, "Can not access configuration directory!");
      }
    }
    String fname = req.getParams().get("file", null);
    if( fname == null ) {
      adminFile = configdir;
    }
    else {
      fname = fname.replace( '\\', '/' ); // normalize slashes
      if( hiddenFiles.contains( fname.toUpperCase(Locale.ENGLISH) ) ) {
        throw new SolrException( ErrorCode.FORBIDDEN, "Can not access: "+fname );
      }
      if( fname.indexOf( ".." ) >= 0 ) {
        throw new SolrException( ErrorCode.FORBIDDEN, "Invalid path: "+fname );  
      }
      adminFile = new File( configdir, fname );
    }
    
    // Make sure the file exists, is readable and is not a hidden file
    if( !adminFile.exists() ) {
      throw new SolrException( ErrorCode.BAD_REQUEST, "Can not find: "+adminFile.getName() 
          + " ["+adminFile.getAbsolutePath()+"]" );
    }
    if( !adminFile.canRead() || adminFile.isHidden() ) {
      throw new SolrException( ErrorCode.BAD_REQUEST, "Can not show: "+adminFile.getName() 
          + " ["+adminFile.getAbsolutePath()+"]" );
    }
    
    // Show a directory listing
    if( adminFile.isDirectory() ) {
      
      int basePath = configdir.getAbsolutePath().length() + 1;
      NamedList<SimpleOrderedMap<Object>> files = new SimpleOrderedMap<SimpleOrderedMap<Object>>();
      for( File f : adminFile.listFiles() ) {
        String path = f.getAbsolutePath().substring( basePath );
        path = path.replace( '\\', '/' ); // normalize slashes
        if( hiddenFiles.contains( path.toUpperCase(Locale.ENGLISH) ) ) {
          continue; // don't show 'hidden' files
        }
        if( f.isHidden() || f.getName().startsWith( "." ) ) {
          continue; // skip hidden system files...
        }
        
        SimpleOrderedMap<Object> fileInfo = new SimpleOrderedMap<Object>();
        files.add( path, fileInfo );
        if( f.isDirectory() ) {
          fileInfo.add( "directory", true ); 
        }
        else {
          // TODO? content type
          fileInfo.add( "size", f.length() );
        }
        fileInfo.add( "modified", new Date( f.lastModified() ) );
      }
      rsp.add( "files", files );
    }
    else {
      // Include the file contents
      ContentStreamBase content = new ContentStreamBase.FileStream( adminFile );
      content.setContentType( req.getParams().get( USE_CONTENT_TYPE ) );
  
      rsp.add( RawResponseWriter.CONTENT, content );
    }
    rsp.setHttpCaching(false);
  }
  
  /**
   * This is a utility function that lets you get the contents of an admin file
   * 
   * It is only used so that we can get rid of "/admin/get-file.jsp" and include
   * "admin-extra.html" in "/admin/index.html" using jsp scriptlets
   * 
   * @deprecated This functionality is implemented in
   *             {@link #handleRequestBody(SolrQueryRequest, SolrQueryResponse)}.
   */
  @Deprecated
  public static String getFileContents( String path )
  {
    if( instance != null && instance.hiddenFiles != null ) {
      if( instance.hiddenFiles.contains( path ) ) {
        return ""; // ignore it...
      }
    }
    try {
      SolrCore core = SolrCore.getSolrCore();
      InputStream input = core.getResourceLoader().openResource(path);
      return IOUtils.toString( input );
    }
    catch( Exception ex ) {} // ignore it
    return "";
  }

  //////////////////////// SolrInfoMBeans methods //////////////////////

  @Override
  public String getDescription() {
    return "Admin Get File -- view config files directly";
  }

  @Override
  public String getVersion() {
      return "$Revision$";
  }

  @Override
  public String getSourceId() {
    return "$Id$";
  }

  @Override
  public String getSource() {
    return "$URL$";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2560.java