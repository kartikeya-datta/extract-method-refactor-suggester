error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16447.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16447.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16447.java
text:
```scala
b@@uf.append('(');

/*
 * Copyright (C) 2010 ZXing authors
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

/*
 * These authors would like to acknowledge the Spanish Ministry of Industry,
 * Tourism and Trade, for the support in the project TSI020301-2008-2
 * "PIRAmIDE: Personalizable Interactions with Resources on AmI-enabled
 * Mobile Dynamic Environments", led by Treelogic
 * ( http://www.treelogic.com/ ):
 *
 *   http://www.piramidepse.com/
 */

package com.google.zxing.client.result;

import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

/**
 * Parses strings of digits that represent a RSS Extended code.
 * 
 * @author Antonio Manuel Benjumea Conde, Servinform, S.A.
 * @author Agustín Delgado, Servinform, S.A.
 */
final class ExpandedProductResultParser extends ResultParser {

  private ExpandedProductResultParser() {
  }

  // Treat all RSS EXPANDED, in the sense that they are all
  // product barcodes with complementary data.
  public static ExpandedProductParsedResult parse(Result result) {
    BarcodeFormat format = result.getBarcodeFormat();
    if (!(BarcodeFormat.RSS_EXPANDED.equals(format))) {
      // ExtendedProductParsedResult NOT created. Not a RSS Expanded barcode
      return null;
    }
    // Really neither of these should happen:
    String rawText = result.getText();
    if (rawText == null) {
      // ExtendedProductParsedResult NOT created. Input text is NULL
      return null;
    }

    String productID = "-";
    String sscc = "-";
    String lotNumber = "-";
    String productionDate = "-";
    String packagingDate = "-";
    String bestBeforeDate = "-";
    String expirationDate = "-";
    String weight = "-";
    String weightType = "-";
    String weightIncrement = "-";
    String price = "-";
    String priceIncrement = "-";
    String priceCurrency = "-";
    Hashtable uncommonAIs = new Hashtable();

    int i = 0;

    while (i < rawText.length()) {
      String ai = findAIvalue(i, rawText);
      if ("ERROR".equals(ai)) {
        // Error. Code doesn't match with RSS expanded pattern
        // ExtendedProductParsedResult NOT created. Not match with RSS Expanded pattern
        return null;
      }
      i += ai.length() + 2;
      String value = findValue(i, rawText);
      i += value.length();

      if ("00".equals(ai)) {
        sscc = value;
      } else if ("01".equals(ai)) {
        productID = value;
      } else if ("10".equals(ai)) {
        lotNumber = value;
      } else if ("11".equals(ai)) {
        productionDate = value;
      } else if ("13".equals(ai)) {
        packagingDate = value;
      } else if ("15".equals(ai)) {
        bestBeforeDate = value;
      } else if ("17".equals(ai)) {
        expirationDate = value;
      } else if ("3100".equals(ai) || "3101".equals(ai)
 "3102".equals(ai) || "3103".equals(ai)
 "3104".equals(ai) || "3105".equals(ai)
 "3106".equals(ai) || "3107".equals(ai)
 "3108".equals(ai) || "3109".equals(ai)) {
        weight = value;
        weightType = ExpandedProductParsedResult.KILOGRAM;
        weightIncrement = ai.substring(3);
      } else if ("3200".equals(ai) || "3201".equals(ai)
 "3202".equals(ai) || "3203".equals(ai)
 "3204".equals(ai) || "3205".equals(ai)
 "3206".equals(ai) || "3207".equals(ai)
 "3208".equals(ai) || "3209".equals(ai)) {
        weight = value;
        weightType = ExpandedProductParsedResult.POUND;
        weightIncrement = ai.substring(3);
      } else if ("3920".equals(ai) || "3921".equals(ai)
 "3922".equals(ai) || "3923".equals(ai)) {
        price = value;
        priceIncrement = ai.substring(3);
      } else if ("3930".equals(ai) || "3931".equals(ai)
 "3932".equals(ai) || "3933".equals(ai)) {
        if (value.length() < 4) {
          // The value must have more of 3 symbols (3 for currency and
          // 1 at least for the price)
          // ExtendedProductParsedResult NOT created. Not match with RSS Expanded pattern
          return null;
        }
        price = value.substring(3);
        priceCurrency = value.substring(0, 3);
        priceIncrement = ai.substring(3);
      } else {
        // No match with common AIs
        uncommonAIs.put(ai, value);
      }
    }

    return new ExpandedProductParsedResult(productID, sscc, lotNumber,
        productionDate, packagingDate, bestBeforeDate, expirationDate,
        weight, weightType, weightIncrement, price, priceIncrement,
        priceCurrency, uncommonAIs);
  }

  private static String findAIvalue(int i, String rawText) {
    StringBuffer buf = new StringBuffer();
    char c = rawText.charAt(i);
    // First character must be a open parenthesis.If not, ERROR
    if (c != '(') {
      return "ERROR";
    }

    String rawTextAux = rawText.substring(i + 1);

    for (int index = 0; index < rawTextAux.length(); index++) {
      char currentChar = rawTextAux.charAt(index);
      switch (currentChar){
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
          buf.append(currentChar);
          break;
        case ')':
          return buf.toString();
        default:
          return "ERROR";
      }
    }
    return buf.toString();
  }

  private static String findValue(int i, String rawText) {
    StringBuffer buf = new StringBuffer();
    String rawTextAux = rawText.substring(i);

    for (int index = 0; index < rawTextAux.length(); index++) {
      char c = rawTextAux.charAt(index);
      if (c == '(') {
        // We look for a new AI. If it doesn't exist (ERROR), we coninue
        // with the iteration
        if ("ERROR".equals(findAIvalue(index, rawTextAux))) {
          buf.append(c);
        } else {
          break;
        }
      } else {
        buf.append(c);
      }
    }
    return buf.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16447.java