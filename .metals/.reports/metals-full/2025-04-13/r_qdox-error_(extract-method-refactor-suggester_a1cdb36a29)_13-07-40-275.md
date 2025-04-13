error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16851.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16851.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16851.java
text:
```scala
public static i@@nt getRSSvalue(int[] widths, int maxWidth, boolean noNarrow) {

/*
 * Copyright 2009 ZXing authors
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

package com.google.zxing.oned.rss;

/** Adapted from listings in ISO/IEC 24724 Appendix B and Appendix G. */
public final class RSSUtils {

  private RSSUtils() {}

  static int[] getRSSwidths(int val, int n, int elements, int maxWidth, boolean noNarrow) {
    int[] widths = new int[elements];
    int bar;
    int narrowMask = 0;
    for (bar = 0; bar < elements - 1; bar++) {
      narrowMask |= (1 << bar);
      int elmWidth = 1;
      int subVal;
      while (true) {
        subVal = combins(n - elmWidth - 1, elements - bar - 2);
        if (noNarrow && (narrowMask == 0) &&
            (n - elmWidth - (elements - bar - 1) >= elements - bar - 1)) {
          subVal -= combins(n - elmWidth - (elements - bar), elements - bar - 2);
        }
        if (elements - bar - 1 > 1) {
          int lessVal = 0;
          for (int mxwElement = n - elmWidth - (elements - bar - 2);
               mxwElement > maxWidth;
               mxwElement--) {
            lessVal += combins(n - elmWidth - mxwElement - 1, elements - bar - 3);
          }
          subVal -= lessVal * (elements - 1 - bar);
        } else if (n - elmWidth > maxWidth) {
          subVal--;
        }
        val -= subVal;
        if (val < 0) {
          break;
        }
        elmWidth++;
        narrowMask &= ~(1 << bar);
      }
      val += subVal;
      n -= elmWidth;
      widths[bar] = elmWidth;
    }
    widths[bar] = n;
    return widths;
  }

  static int getRSSvalue(int[] widths, int maxWidth, boolean noNarrow) {
    int elements = widths.length;
    int n = 0;
    for (int i = 0; i < elements; i++) {
      n += widths[i];
    }
    int val = 0;
    int narrowMask = 0;
    for (int bar = 0; bar < elements - 1; bar++) {
      int elmWidth;
      for (elmWidth = 1, narrowMask |= (1 << bar);
           elmWidth < widths[bar];
           elmWidth++, narrowMask &= ~(1 << bar)) {
        int subVal = combins(n - elmWidth - 1, elements - bar - 2);
        if (noNarrow && (narrowMask == 0) &&
            (n - elmWidth - (elements - bar - 1) >= elements - bar - 1)) {
          subVal -= combins(n - elmWidth - (elements - bar),
                            elements - bar - 2);
        }
        if (elements - bar - 1 > 1) {
          int lessVal = 0;
          for (int mxwElement = n - elmWidth - (elements - bar - 2);
               mxwElement > maxWidth; mxwElement--) {
            lessVal += combins(n - elmWidth - mxwElement - 1,
                               elements - bar - 3);
          }
          subVal -= lessVal * (elements - 1 - bar);
        } else if (n - elmWidth > maxWidth) {
          subVal--;
        }
        val += subVal;
      }
      n -= elmWidth;
    }
    return (val);
  }

  static int combins(int n, int r) {
    int maxDenom, minDenom;
    if (n - r > r) {
      minDenom = r;
      maxDenom = n - r;
    } else {
      minDenom = n - r;
      maxDenom = r;
    }
    int val = 1;
    int j = 1;
    for (int i = n; i > maxDenom; i--) {
      val *= i;
      if (j <= minDenom) {
        val /= j;
        j++;
      }
    }
    while (j <= minDenom) {
      val /= j;
      j++;
    }
    return (val);
  }

  static int[] elements(int[] eDist, int N, int K) {
    int[] widths = new int[eDist.length + 2];
    int twoK = K << 1;
    widths[0] = 1;
    int i;
    int minEven = 10;
    int barSum = 1;
    for (i = 1; i < twoK - 2; i += 2) {
      widths[i] = eDist[i - 1] - widths[i - 1];
      widths[i + 1] = eDist[i] - widths[i];
      barSum += widths[i] + widths[i + 1];
      if (widths[i] < minEven) {
        minEven = widths[i];
      }
    }
    widths[twoK - 1] = N - barSum;
    if (widths[twoK - 1] < minEven) {
      minEven = widths[twoK - 1];
    }
    if (minEven > 1) {
      for (i = 0; i < twoK; i += 2) {
        widths[i] += minEven - 1;
        widths[i + 1] -= minEven - 1;
      }
    }
    return widths;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16851.java