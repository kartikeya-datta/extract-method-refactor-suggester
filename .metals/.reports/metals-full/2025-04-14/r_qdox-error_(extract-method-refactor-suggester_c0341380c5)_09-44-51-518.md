error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2709.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2709.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2709.java
text:
```scala
b@@ytes = EMPTY_BYTES;

package org.apache.lucene.util;

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

import java.util.Comparator;
import java.io.UnsupportedEncodingException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Externalizable;
import java.io.IOException;

/** Represents byte[], as a slice (offset + length) into an
 *  existing byte[].
 *
 *  @lucene.experimental */
public final class BytesRef implements Comparable<BytesRef>, Externalizable {

  public static final byte[] EMPTY_BYTES = new byte[0]; 

  /** The contents of the BytesRef. Should never be {@code null}. */
  public byte[] bytes;

  /** Offset of first valid byte. */
  public int offset;

  /** Length of used bytes. */
  public int length;

  public BytesRef() {
    bytes = EMPTY_BYTES;
  }

  /** This instance will directly reference bytes w/o making a copy.
   * bytes should not be null.
   */
  public BytesRef(byte[] bytes, int offset, int length) {
    assert bytes != null;
    this.bytes = bytes;
    this.offset = offset;
    this.length = length;
  }

  /** This instance will directly reference bytes w/o making a copy.
   * bytes should not be null */
  public BytesRef(byte[] bytes) {
    assert bytes != null;
    this.bytes = bytes;
    this.offset = 0;
    this.length = bytes.length;
  }

  public BytesRef(int capacity) {
    this.bytes = new byte[capacity];
  }

  /**
   * @param text Initialize the byte[] from the UTF8 bytes
   * for the provided Sring.  This must be well-formed
   * unicode text, with no unpaired surrogates or U+FFFF.
   */
  public BytesRef(CharSequence text) {
    this();
    copy(text);
  }

  public BytesRef(BytesRef other) {
    this();
    copy(other);
  }

  /* // maybe?
  public BytesRef(BytesRef other, boolean shallow) {
    this();
    if (shallow) {
      offset = other.offset;
      length = other.length;
      bytes = other.bytes;
    } else {
      copy(other);
    }
  }
  */

  /**
   * Copies the UTF8 bytes for this string.
   * 
   * @param text Must be well-formed unicode text, with no
   * unpaired surrogates or invalid UTF16 code units.
   */
  public void copy(CharSequence text) {
    UnicodeUtil.UTF16toUTF8(text, 0, text.length(), this);
  }

  public boolean bytesEquals(BytesRef other) {
    if (length == other.length) {
      int otherUpto = other.offset;
      final byte[] otherBytes = other.bytes;
      final int end = offset + length;
      for(int upto=offset;upto<end;upto++,otherUpto++) {
        if (bytes[upto] != otherBytes[otherUpto]) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Object clone() {
    return new BytesRef(this);
  }

  private boolean sliceEquals(BytesRef other, int pos) {
    if (pos < 0 || length - pos < other.length) {
      return false;
    }
    int i = offset + pos;
    int j = other.offset;
    final int k = other.offset + other.length;
    
    while (j < k) {
      if (bytes[i++] != other.bytes[j++]) {
        return false;
      }
    }
    
    return true;
  }
  
  public boolean startsWith(BytesRef other) {
    return sliceEquals(other, 0);
  }

  public boolean endsWith(BytesRef other) {
    return sliceEquals(other, length - other.length);
  }
  
  /** Calculates the hash code as required by TermsHash during indexing.
   * <p>It is defined as:
   * <pre>
   *  int hash = 0;
   *  for (int i = offset; i &lt; offset + length; i++) {
   *    hash = 31*hash + bytes[i];
   *  }
   * </pre>
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 0;
    final int end = offset + length;
    for(int i=offset;i<end;i++) {
      result = prime * result + bytes[i];
    }
    return result;
  }

  @Override
  public boolean equals(Object other) {
    return this.bytesEquals((BytesRef) other);
  }

  /** Interprets stored bytes as UTF8 bytes, returning the
   *  resulting string */
  public String utf8ToString() {
    try {
      return new String(bytes, offset, length, "UTF-8");
    } catch (UnsupportedEncodingException uee) {
      // should not happen -- UTF8 is presumably supported
      // by all JREs
      throw new RuntimeException(uee);
    }
  }

  /** Returns hex encoded bytes, eg [0x6c 0x75 0x63 0x65 0x6e 0x65] */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    final int end = offset + length;
    for(int i=offset;i<end;i++) {
      if (i > offset) {
        sb.append(' ');
      }
      sb.append(Integer.toHexString(bytes[i]&0xff));
    }
    sb.append(']');
    return sb.toString();
  }

  public void copy(BytesRef other) {
    if (bytes.length < other.length) {
      bytes = new byte[other.length];
    }
    System.arraycopy(other.bytes, other.offset, bytes, 0, other.length);
    length = other.length;
    offset = 0;
  }

  public void grow(int newLength) {
    bytes = ArrayUtil.grow(bytes, newLength);
  }

  /** Unsigned byte order comparison */
  public int compareTo(BytesRef other) {
    if (this == other) return 0;

    final byte[] aBytes = this.bytes;
    int aUpto = this.offset;
    final byte[] bBytes = other.bytes;
    int bUpto = other.offset;

    final int aStop = aUpto + Math.min(this.length, other.length);

    while(aUpto < aStop) {
      int aByte = aBytes[aUpto++] & 0xff;
      int bByte = bBytes[bUpto++] & 0xff;
      int diff = aByte - bByte;
      if (diff != 0) return diff;
    }

    // One is a prefix of the other, or, they are equal:
    return this.length - other.length;
  }

  private final static Comparator<BytesRef> utf8SortedAsUnicodeSortOrder = new UTF8SortedAsUnicodeComparator();

  public static Comparator<BytesRef> getUTF8SortedAsUnicodeComparator() {
    return utf8SortedAsUnicodeSortOrder;
  }

  private static class UTF8SortedAsUnicodeComparator implements Comparator<BytesRef> {
    // Only singleton
    private UTF8SortedAsUnicodeComparator() {};

    public int compare(BytesRef a, BytesRef b) {
      final byte[] aBytes = a.bytes;
      int aUpto = a.offset;
      final byte[] bBytes = b.bytes;
      int bUpto = b.offset;
      
      final int aStop;
      if (a.length < b.length) {
        aStop = aUpto + a.length;
      } else {
        aStop = aUpto + b.length;
      }

      while(aUpto < aStop) {
        int aByte = aBytes[aUpto++] & 0xff;
        int bByte = bBytes[bUpto++] & 0xff;

        int diff = aByte - bByte;
        if (diff != 0) {
          return diff;
        }
      }

      // One is a prefix of the other, or, they are equal:
      return a.length - b.length;
    }    
  }

  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeInt(length);
    if (length > 0) {
      out.write(bytes, offset, length);
    }
  }

  public void readExternal( ObjectInput in ) throws
      IOException, ClassNotFoundException {
    length = in.readInt();
    offset = 0;
    if (length > 0) {
      bytes = new byte[length];
      in.read(bytes, 0, length);
    } else {
      bytes = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2709.java