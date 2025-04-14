error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/572.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/572.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/572.java
text:
```scala
S@@tring entryURL = "jar:" + url + "!/" + getName();

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIESOR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.aries.application.utils.filesystem.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.aries.application.filesystem.IDirectory;
import org.apache.aries.application.filesystem.IFile;
import org.apache.aries.application.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of IFile that represents a file entry in a zip.
 */
public class ZipFileImpl implements IFile
{
  /** A logger */
  private static final Logger logger = LoggerFactory.getLogger("org.apache.aries.application.utils");

  /** The name of the file */
  private String name = "";
  /** The size of the file */
  private final long size;
  /** The last time the file was updated */
  private final long lastModified;
  /** The zip file this is contained in */
  protected File zip;
  /** The entry in the zip this IFile represents */
  protected ZipEntry entry;
  /** The parent directory */
  private ZipDirectory parent;
  /** The URL of the zip file we are looking inside of */
  private final String url;
  
  /**
   * This constructor is used to create a file entry within the zip.
   * 
   * @param zip1    the zip file the entry is in.
   * @param entry1  the entry this IFile represents.
   * @param parent1 the parent directory.
   */
  public ZipFileImpl(File zip1, ZipEntry entry1, ZipDirectory parent1)
  {
    this.zip = zip1;
    this.entry = entry1;
    
    name = entry1.getName();
    
    if (entry1.isDirectory()) name = name.substring(0, name.length() - 1);
    
    lastModified = entry1.getTime();
    size = entry1.getSize();
    
    url = ((ZipFileImpl)parent1).url;
    
    this.parent = parent1;
  }
  
  /**
   * This is called to construct the root directory of the zip.
   * 
   * @param zip1 the zip file this represents.
   * @param fs   the file on the fs.
   * @throws MalformedURLException
   */
  protected ZipFileImpl(File zip1, File fs) throws MalformedURLException
  {
    this.zip = zip1;
    this.entry = null;
    name = "";
    lastModified = fs.lastModified();
    size = fs.length();
    url = fs.toURI().toURL().toExternalForm();
  }

  public IDirectory convert()
  {
    return null;
  }

  public long getLastModified()
  {
    return lastModified;
  }

  public String getName()
  {
    return name;
  }

  public IDirectory getParent()
  {
    return parent;
  }

  public long getSize()
  {
    return size;
  }

  public boolean isDirectory()
  {
    return false;
  }

  public boolean isFile()
  {
    return true;
  }

  public InputStream open() throws IOException
  {
    InputStream is = new SpecialZipInputStream(entry);
    return is;
  }
  
  public IDirectory getRoot()
  {
    IDirectory root = parent.getRoot();
    return root;
  }

  public URL toURL() throws MalformedURLException
  {
    String entryURL = "jar:" + url + "!/" + getParent().getName() + getName();
    URL result = new URL(entryURL);
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) return false;
    if (obj == this) return true;
    
    if (obj.getClass() == getClass()) {
      return toString().equals(obj.toString());
    }
    
    return false;
  }

  @Override
  public int hashCode()
  {
    return toString().hashCode();
  }

  @Override
  public String toString()
  {
    return url.substring(5)+ "/" + name;
  }
  
  ZipFile openZipFile(){
    ZipFile z = null;
    try {
      z = new ZipFile(zip);
    } catch (ZipException e) {
      logger.error ("ZipException in ZipFileImpl.openZipFile", e);
    } catch (IOException e) {
      logger.error ("IOException in ZipFileImpl.openZipFile", e);
    }
    return z;
  }
  
  void closeZipFile(ZipFile z){
    try{
      z.close();
    }
    catch (IOException e) {
      logger.error ("IOException in ZipFileImpl.closeZipFile", e);
    }
  }
  
  /**
   * A simple class to delegate to the InputStream of the constructor
   * and to call close on the zipFile when we close the stream.
   *
   */
  private class SpecialZipInputStream extends InputStream{

    private ZipFile zipFile;
    private InputStream is;
    
    public SpecialZipInputStream(ZipEntry anEntry){
      try{
      this.zipFile = openZipFile();
      this.is = zipFile.getInputStream(anEntry);
      }
      catch (ZipException e) {
        logger.error ("ZipException in SpecialZipInputStream()", e);
      } catch (IOException e) {
        logger.error ("IOException in SpecialZipInputStream()", e);        
      }
    }
    
    @Override
    public int read() throws IOException
    {
      return is.read();
    }
    
    @Override
    public void close() throws IOException{
        //call close on the input stream, probably does nothing
        is.close();
        //call close on the zip file, important for tidying up
        closeZipFile(zipFile);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/572.java