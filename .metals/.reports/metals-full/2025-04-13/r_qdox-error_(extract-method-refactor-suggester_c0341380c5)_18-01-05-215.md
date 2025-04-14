error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1583.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1583.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1583.java
text:
```scala
public static final B@@arcodeFormat DATA_MATRIX = new BarcodeFormat("DATA_MATRIX");

/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing;

import java.util.Hashtable;

/**
 * Enumerates barcode formats known to this package.
 *
 * @author Sean Owen
 */
public final class BarcodeFormat {

  // No, we can't use an enum here. J2ME doesn't support it.

  private static final Hashtable VALUES = new Hashtable();

  /** QR Code 2D barcode format. */
  public static final BarcodeFormat QR_CODE = new BarcodeFormat("QR_CODE");

  /** DataMatrix 2D barcode format. */
  public static final BarcodeFormat DATAMATRIX = new BarcodeFormat("DATAMATRIX");

  /** UPC-E 1D format. */
  public static final BarcodeFormat UPC_E = new BarcodeFormat("UPC_E");

  /** UPC-A 1D format. */
  public static final BarcodeFormat UPC_A = new BarcodeFormat("UPC_A");

  /** EAN-8 1D format. */
  public static final BarcodeFormat EAN_8 = new BarcodeFormat("EAN_8");

  /** EAN-13 1D format. */
  public static final BarcodeFormat EAN_13 = new BarcodeFormat("EAN_13");

  /** UPC/EAN extension format. Not a stand-alone format. */
  public static final BarcodeFormat UPC_EAN_EXTENSION = new BarcodeFormat("UPC_EAN_EXTENSION");

  /** Code 128 1D format. */
  public static final BarcodeFormat CODE_128 = new BarcodeFormat("CODE_128");

  /** Code 39 1D format. */
  public static final BarcodeFormat CODE_39 = new BarcodeFormat("CODE_39");

  /** Code 93 1D format. */
  public static final BarcodeFormat CODE_93 = new BarcodeFormat("CODE_93");
  
  /** CODABAR 1D format. */
  public static final BarcodeFormat CODABAR = new BarcodeFormat("CODABAR");

  /** ITF (Interleaved Two of Five) 1D format. */
  public static final BarcodeFormat ITF = new BarcodeFormat("ITF");

  /** RSS 14 */
  public static final BarcodeFormat RSS14 = new BarcodeFormat("RSS14");

  /** PDF417 format. */
  public static final BarcodeFormat PDF417 = new BarcodeFormat("PDF417");
  
  /** RSS EXPANDED */
  public static final BarcodeFormat RSS_EXPANDED = new BarcodeFormat("RSS_EXPANDED");

  private final String name;

  private BarcodeFormat(String name) {
    this.name = name;
    VALUES.put(name, this);
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public static BarcodeFormat valueOf(String name) {
    if (name == null || name.length() == 0) {
      throw new IllegalArgumentException();
    }
    BarcodeFormat format = (BarcodeFormat) VALUES.get(name);
    if (format == null) {
      throw new IllegalArgumentException();
    }
    return format;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1583.java