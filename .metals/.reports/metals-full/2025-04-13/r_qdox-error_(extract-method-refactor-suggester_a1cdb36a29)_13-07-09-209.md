error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2575.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2575.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2575.java
text:
```scala
N@@ameValuePair info = ManifestHeaderProcessor.parseBundleSymbolicName(rawSymName);

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

package org.apache.aries.util.manifest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import org.apache.aries.util.filesystem.IFile;
import org.apache.aries.util.filesystem.IOUtils;
import org.apache.aries.util.internal.MessageUtil;
import org.apache.aries.util.manifest.ManifestHeaderProcessor.NameValueMap;
import org.apache.aries.util.manifest.ManifestHeaderProcessor.NameValuePair;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity class to retrieve and represent a bundle manifest (valid or invalid).
 */
public class BundleManifest
{
  private static final String MANIFEST_PATH = "META-INF/MANIFEST.MF";
  private static final Logger _logger = LoggerFactory.getLogger(BundleManifest.class.getName());

  /**
   * Read a manifest from a jar input stream. This will find the manifest even if it is NOT
   * the first file in the archive.
   * 
   * @param is
   * @return
   */
  public static BundleManifest fromBundle(InputStream is) {
    JarInputStream jarIs = null;
    try {
      jarIs = new JarInputStream(is);
      Manifest m = jarIs.getManifest();
      if (m != null)
        return new BundleManifest(m);
      else {
        ZipEntry entry;
        while ((entry = jarIs.getNextEntry()) != null) {
          if (entry.getName().equals(MANIFEST_PATH))
            return new BundleManifest(jarIs);
        }
        
        return null;
      }
    }
    catch (IOException e) {
      _logger.error ("IOException in BundleManifest()", e);
      return null;
    }
    finally {
      IOUtils.close(jarIs);
    }
  }
  
  /**
   * Retrieve a BundleManifest from the given jar file
   * 
   * @param f
   * @return
   */
  public static BundleManifest fromBundle(IFile f) {
    InputStream is = null;
    try {
      if (f.isDirectory()) {
        IFile manFile = f.convert().getFile(MANIFEST_PATH);
        if (manFile != null)
          return new BundleManifest(manFile.open());
        else
          return null;
      } else {
        is = f.open();
        return fromBundle(is);
      }
    } catch (IOException e) {
      _logger.error ("IOException in BundleManifest.fromBundle(IFile)", e);
      return null;
    }
    finally {
      IOUtils.close(is);
    }
  }
  
  /**
   * Retrieve a bundle manifest from the given jar file, which can be exploded or compressed
   * 
   * @param f
   * @return
   */
  public static BundleManifest fromBundle(File f) {
    if (f.isDirectory()) {
      File manifestFile = new File(f, MANIFEST_PATH);
      if (manifestFile.isFile())
        try {
          return new BundleManifest(new FileInputStream(manifestFile));
        }
        catch (IOException e) {
          _logger.error ("IOException in BundleManifest.fromBundle(File)", e);
          return null;
        }
      else
        return null;
    }
    else  if (f.isFile()) {
      try {
        return fromBundle(new FileInputStream(f));
      }
      catch (IOException e) {
        _logger.error ("IOException in BundleManifest.fromBundle(File)", e);
        return null;
      }
    }
    else {
      throw new IllegalArgumentException(MessageUtil.getMessage("UTIL0016E", f.getAbsolutePath()));
    }
  }
  
  private Manifest manifest;
  
  /**
   * Create a BundleManifest object from the InputStream to the manifest (not to the bundle)
   * @param manifestIs
   * @throws IOException
   */
  public BundleManifest(InputStream manifestIs) throws IOException {
    this(ManifestProcessor.parseManifest(manifestIs));
  }
  
  /**
   * Create a BundleManifest object from a common Manifest object
   * @param m
   */
  public BundleManifest(Manifest m) {
    manifest = m;
  }
  
  public String getSymbolicName() {
    String rawSymName = manifest.getMainAttributes().getValue(Constants.BUNDLE_SYMBOLICNAME);

    String result = null;
    if (rawSymName != null) {
      NameValuePair<String, NameValueMap<String, String>> info = ManifestHeaderProcessor.parseBundleSymbolicName(rawSymName);
      result = info.getName();
    }
    
    return result;
  }
  
  public Version getVersion() {
    String specifiedVersion = manifest.getMainAttributes().getValue(Constants.BUNDLE_VERSION);
    Version result = (specifiedVersion == null) ? Version.emptyVersion : new Version(specifiedVersion);
    
    return result;
  }
  
  public String getManifestVersion() {
    return manifest.getMainAttributes().getValue(Constants.BUNDLE_MANIFESTVERSION);
  }
  
  public Attributes getRawAttributes() {
    return manifest.getMainAttributes();
  }
  
  public Manifest getRawManifest() {
    return manifest;
  }
  
  public boolean isValid() {
    return getManifestVersion() != null && getSymbolicName() != null;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2575.java