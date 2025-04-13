error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2766.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2766.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2766.java
text:
```scala
e@@lse name = file.getAbsolutePath().substring(rootDir.length() + 1).replace('\\', '/');

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

package org.apache.aries.util.filesystem.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.aries.util.filesystem.IDirectory;
import org.apache.aries.util.filesystem.IFile;

/**
 * An implementation of IFile that represents a java.io.File.
 */
public class FileImpl implements IFile
{
  /** The name of the root directory of the file system */
  protected String rootDir;
  /** This file in the file system */
  protected File file;
  /** The root File in the file system */
  protected File rootDirFile;
  /** The name of this file in the vFS */
  private String name;
  
  /**
   * @param f        this file.
   * @param rootFile the root of the vFS.
   */
  public FileImpl(File f, File rootFile)
  {
    file = f;
    this.rootDirFile = rootFile;
    rootDir = rootFile.getAbsolutePath();
    
    if (f.equals(rootFile)) name = "";
    else name = file.getAbsolutePath().substring(rootDir.length() + 1);
  }
  
  @Override
  public IDirectory convert()
  {
    return null;
  }

  @Override
  public long getLastModified()
  {
    long result = file.lastModified();
    return result;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public IDirectory getParent()
  {
    IDirectory parent = new DirectoryImpl(file.getParentFile(), rootDirFile);
    return parent;
  }

  @Override
  public long getSize()
  {
    long size = file.length();
    return size;
  }

  @Override
  public boolean isDirectory()
  {
    boolean result = file.isDirectory();
    return result;
  }

  @Override
  public boolean isFile()
  {
    boolean result = file.isFile();
    return result;
  }

  @Override
  public InputStream open() throws IOException
  {
    InputStream is = new FileInputStream(file);
    return is;
  }

  @Override
  public IDirectory getRoot()
  {
    IDirectory root = new DirectoryImpl(rootDirFile, rootDirFile);
    return root;
  }

  @Override
  public URL toURL() throws MalformedURLException
  {
    URL result = file.toURI().toURL();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) return false;
    if (obj == this) return true;
    
    if (obj.getClass() == getClass()) {
      return file.equals(((FileImpl)obj).file);
    }
    
    return false;
  }

  @Override
  public int hashCode()
  {
    return file.hashCode();
  }
  
  @Override
  public String toString()
  {
    return file.getAbsolutePath();
  }

  @Override
  public IDirectory convertNested() {
	  if (isDirectory()) return convert();
	  else return FileSystemImpl.getFSRoot(file, getParent());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2766.java