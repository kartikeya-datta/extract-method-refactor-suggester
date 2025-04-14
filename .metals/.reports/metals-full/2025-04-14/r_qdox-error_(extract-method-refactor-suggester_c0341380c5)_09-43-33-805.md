error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1731.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1731.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1731.java
text:
```scala
S@@tringBuilder result = new StringBuilder(resultLen);

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
package org.apache.solr.common.util;

/**
 * Static methods for translating Base64 encoded strings to byte arrays
 * and vice-versa. 
 */

public class Base64 {
  /**
   * This array is a lookup table that translates 6-bit positive integer
   * index values into their "Base64 Alphabet" equivalents as specified
   * in Table 1 of RFC 2045.
   */
  private static final char intToBase64[] = {
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
          'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
          'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
  };

  /**
   * This array is a lookup table that translates unicode characters
   * drawn from the "Base64 Alphabet" (as specified in Table 1 of RFC 2045)
   * into their 6-bit positive integer equivalents.  Characters that
   * are not in the Base64 alphabet but fall within the bounds of the
   * array are translated to -1.
   */
  private static final byte base64ToInt[] = {
          -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
          -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
          -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54,
          55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
          5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
          24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34,
          35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
  };

  public static String byteArrayToBase64(byte[] a, int offset, int len) {
    int aLen = len;
    int numFullGroups = aLen / 3;
    int numBytesInPartialGroup = aLen - 3 * numFullGroups;
    int resultLen = 4 * ((aLen + 2) / 3);
    StringBuffer result = new StringBuffer(resultLen);
    char[] intToAlpha = intToBase64;

    // Translate all full groups from byte array elements to Base64
    int inCursor = offset;
    for (int i = 0; i < numFullGroups; i++) {
      int byte0 = a[inCursor++] & 0xff;
      int byte1 = a[inCursor++] & 0xff;
      int byte2 = a[inCursor++] & 0xff;
      result.append(intToAlpha[byte0 >> 2]);
      result.append(intToAlpha[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
      result.append(intToAlpha[(byte1 << 2) & 0x3f | (byte2 >> 6)]);
      result.append(intToAlpha[byte2 & 0x3f]);
    }

    // Translate partial group if present
    if (numBytesInPartialGroup != 0) {
      int byte0 = a[inCursor++] & 0xff;
      result.append(intToAlpha[byte0 >> 2]);
      if (numBytesInPartialGroup == 1) {
        result.append(intToAlpha[(byte0 << 4) & 0x3f]);
        result.append("==");
      } else {
        // assert numBytesInPartialGroup == 2;
        int byte1 = a[inCursor++] & 0xff;
        result.append(intToAlpha[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
        result.append(intToAlpha[(byte1 << 2) & 0x3f]);
        result.append('=');
      }
    }
    return result.toString();
  }

  public static byte[] base64ToByteArray(String s) {
    byte[] alphaToInt = base64ToInt;
    int sLen = s.length();
    int numGroups = sLen / 4;
    if (4 * numGroups != sLen)
      throw new IllegalArgumentException(
              "String length must be a multiple of four.");
    int missingBytesInLastGroup = 0;
    int numFullGroups = numGroups;
    if (sLen != 0) {
      if (s.charAt(sLen - 1) == '=') {
        missingBytesInLastGroup++;
        numFullGroups--;
      }
      if (s.charAt(sLen - 2) == '=')
        missingBytesInLastGroup++;
    }
    byte[] result = new byte[3 * numGroups - missingBytesInLastGroup];

    // Translate all full groups from base64 to byte array elements
    int inCursor = 0, outCursor = 0;
    for (int i = 0; i < numFullGroups; i++) {
      int ch0 = base64toInt(s.charAt(inCursor++), alphaToInt);
      int ch1 = base64toInt(s.charAt(inCursor++), alphaToInt);
      int ch2 = base64toInt(s.charAt(inCursor++), alphaToInt);
      int ch3 = base64toInt(s.charAt(inCursor++), alphaToInt);
      result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));
      result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
      result[outCursor++] = (byte) ((ch2 << 6) | ch3);
    }

    // Translate partial group, if present
    if (missingBytesInLastGroup != 0) {
      int ch0 = base64toInt(s.charAt(inCursor++), alphaToInt);
      int ch1 = base64toInt(s.charAt(inCursor++), alphaToInt);
      result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));

      if (missingBytesInLastGroup == 1) {
        int ch2 = base64toInt(s.charAt(inCursor++), alphaToInt);
        result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
      }
    }
    // assert inCursor == s.length()-missingBytesInLastGroup;
    // assert outCursor == result.length;
    return result;
  }

  /**
   * Translates the specified character, which is assumed to be in the
   * "Base 64 Alphabet" into its equivalent 6-bit positive integer.
   *
   * @throw IllegalArgumentException or ArrayOutOfBoundsException if
   * c is not in the Base64 Alphabet.
   */
  private static int base64toInt(char c, byte[] alphaToInt) {
    int result = alphaToInt[c];
    if (result < 0)
      throw new IllegalArgumentException("Illegal character " + c);
    return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1731.java