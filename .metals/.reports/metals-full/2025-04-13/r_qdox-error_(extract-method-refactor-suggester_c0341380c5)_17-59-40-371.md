error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2855.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2855.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2855.java
text:
```scala
public S@@et<String> files() {

package org.apache.lucene.index;

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


import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.lucene3x.Lucene3xSegmentInfoFormat;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.TrackingDirectoryWrapper;

/**
 * Information about a segment such as it's name, directory, and files related
 * to the segment.
 *
 * @lucene.experimental
 */
public final class SegmentInfo {
  
  // TODO: remove these from this class, for now this is the representation
  public static final int NO = -1;          // e.g. no norms; no deletes;
  public static final int YES = 1;          // e.g. have norms; have deletes;

  public final String name;				  // unique name in dir
  private int docCount;				  // number of docs in seg
  public final Directory dir;				  // where segment resides

  private boolean isCompoundFile;

  private volatile long sizeInBytes = -1;         // total byte size of all files (computed on demand)

  private Codec codec;

  private Map<String,String> diagnostics;
  
  private Map<String,String> attributes;

  // Tracks the Lucene version this segment was created with, since 3.1. Null
  // indicates an older than 3.0 index, and it's used to detect a too old index.
  // The format expected is "x.y" - "2.x" for pre-3.0 indexes (or null), and
  // specific versions afterwards ("3.0", "3.1" etc.).
  // see Constants.LUCENE_MAIN_VERSION.
  private String version;

  void setDiagnostics(Map<String, String> diagnostics) {
    this.diagnostics = diagnostics;
  }

  public Map<String, String> getDiagnostics() {
    return diagnostics;
  }

  /**
   * Construct a new complete SegmentInfo instance from input.
   * <p>Note: this is public only to allow access from
   * the codecs package.</p>
   */
  public SegmentInfo(Directory dir, String version, String name, int docCount, 
                     boolean isCompoundFile, Codec codec, Map<String,String> diagnostics, Map<String,String> attributes) {
    assert !(dir instanceof TrackingDirectoryWrapper);
    this.dir = dir;
    this.version = version;
    this.name = name;
    this.docCount = docCount;
    this.isCompoundFile = isCompoundFile;
    this.codec = codec;
    this.diagnostics = diagnostics;
    this.attributes = attributes;
  }

  /**
   * Returns total size in bytes of all of files used by
   * this segment.  Note that this will not include any live
   * docs for the segment; to include that use {@link
   * SegmentInfoPerCommit#sizeInBytes()} instead.
   */
  public long sizeInBytes() throws IOException {
    if (sizeInBytes == -1) {
      long sum = 0;
      for (final String fileName : files()) {
        sum += dir.fileLength(fileName);
      }
      sizeInBytes = sum;
    }
    return sizeInBytes;
  }

  /**
   * @deprecated separate norms are not supported in >= 4.0
   */
  @Deprecated
  boolean hasSeparateNorms() {
    return getAttribute(Lucene3xSegmentInfoFormat.NORMGEN_KEY) != null;
  }

  /**
   * Mark whether this segment is stored as a compound file.
   *
   * @param isCompoundFile true if this is a compound file;
   * else, false
   */
  void setUseCompoundFile(boolean isCompoundFile) {
    this.isCompoundFile = isCompoundFile;
  }
  
  /**
   * Returns true if this segment is stored as a compound
   * file; else, false.
   */
  public boolean getUseCompoundFile() {
    return isCompoundFile;
  }

  /** Can only be called once. */
  public void setCodec(Codec codec) {
    assert this.codec == null;
    if (codec == null) {
      throw new IllegalArgumentException("segmentCodecs must be non-null");
    }
    this.codec = codec;
  }

  public Codec getCodec() {
    return codec;
  }

  public int getDocCount() {
    if (this.docCount == -1) {
      throw new IllegalStateException("docCount isn't set yet");
    }
    return docCount;
  }

  // NOTE: leave package private
  void setDocCount(int docCount) {
    if (this.docCount != -1) {
      throw new IllegalStateException("docCount was already set");
    }
    this.docCount = docCount;
  }

  /*
   * Return all files referenced by this SegmentInfo.  The
   * returns List is a locally cached List so you should not
   * modify it.
   */

  public Set<String> files() throws IOException {
    if (setFiles == null) {
      throw new IllegalStateException("files were not computed yet");
    }
    return Collections.unmodifiableSet(setFiles);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return toString(dir, 0);
  }

  /** Used for debugging.  Format may suddenly change.
   *
   *  <p>Current format looks like
   *  <code>_a(3.1):c45/4</code>, which means the segment's
   *  name is <code>_a</code>; it was created with Lucene 3.1 (or
   *  '?' if it's unknown); it's using compound file
   *  format (would be <code>C</code> if not compound); it
   *  has 45 documents; it has 4 deletions (this part is
   *  left off when there are no deletions).</p>
   */
  public String toString(Directory dir, int delCount) {

    StringBuilder s = new StringBuilder();
    s.append(name).append('(').append(version == null ? "?" : version).append(')').append(':');
    char cfs = getUseCompoundFile() ? 'c' : 'C';
    s.append(cfs);

    if (this.dir != dir) {
      s.append('x');
    }
    s.append(docCount);

    if (delCount != 0) {
      s.append('/').append(delCount);
    }

    // TODO: we could append toString of attributes() here?

    return s.toString();
  }

  /** We consider another SegmentInfo instance equal if it
   *  has the same dir and same name. */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj instanceof SegmentInfo) {
      final SegmentInfo other = (SegmentInfo) obj;
      return other.dir == dir && other.name.equals(name);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return dir.hashCode() + name.hashCode();
  }

  /**
   * Used by DefaultSegmentInfosReader to upgrade a 3.0 segment to record its
   * version is "3.0". This method can be removed when we're not required to
   * support 3x indexes anymore, e.g. in 5.0.
   * <p>
   * <b>NOTE:</b> this method is used for internal purposes only - you should
   * not modify the version of a SegmentInfo, or it may result in unexpected
   * exceptions thrown when you attempt to open the index.
   *
   * @lucene.internal
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /** Returns the version of the code which wrote the segment. */
  public String getVersion() {
    return version;
  }

  private Set<String> setFiles;

  public void setFiles(Set<String> files) {
    setFiles = files;
    sizeInBytes = -1;
  }

  public void addFiles(Collection<String> files) {
    setFiles.addAll(files);
  }

  public void addFile(String file) {
    setFiles.add(file);
  }
    
  /**
   * Get a codec attribute value, or null if it does not exist
   */
  public String getAttribute(String key) {
    if (attributes == null) {
      return null;
    } else {
      return attributes.get(key);
    }
  }
  
  /**
   * Puts a codec attribute value.
   * <p>
   * This is a key-value mapping for the field that the codec can use
   * to store additional metadata, and will be available to the codec
   * when reading the segment via {@link #getAttribute(String)}
   * <p>
   * If a value already exists for the field, it will be replaced with 
   * the new value.
   */
  public String putAttribute(String key, String value) {
    if (attributes == null) {
      attributes = new HashMap<String,String>();
    }
    return attributes.put(key, value);
  }
  
  /**
   * @return internal codec attributes map. May be null if no mappings exist.
   */
  public Map<String,String> attributes() {
    return attributes;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2855.java