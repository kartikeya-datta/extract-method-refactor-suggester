error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3511.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3511.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3511.java
text:
```scala
l@@ocale = Locale.ROOT;

package org.apache.solr.handler.dataimport;
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

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * A {@link Transformer} instance which can extract numbers out of strings. It uses
 * {@link NumberFormat} class to parse strings and supports
 * Number, Integer, Currency and Percent styles as supported by
 * {@link NumberFormat} with configurable locales.
 * </p>
 * <p/>
 * <p>
 * Refer to <a
 * href="http://wiki.apache.org/solr/DataImportHandler">http://wiki.apache.org/solr/DataImportHandler</a>
 * for more details.
 * </p>
 * <p/>
 * <b>This API is experimental and may change in the future.</b>
 *
 * @since solr 1.3
 */
public class NumberFormatTransformer extends Transformer {

  private static final Pattern localeRegex = Pattern.compile("^([a-z]{2})-([A-Z]{2})$");

  @Override
  @SuppressWarnings("unchecked")
  public Object transformRow(Map<String, Object> row, Context context) {
    for (Map<String, String> fld : context.getAllEntityFields()) {
      String style = context.replaceTokens(fld.get(FORMAT_STYLE));
      if (style != null) {
        String column = fld.get(DataImporter.COLUMN);
        String srcCol = fld.get(RegexTransformer.SRC_COL_NAME);
        Locale locale = null;
        String localeStr = context.replaceTokens(fld.get(LOCALE));
        if (srcCol == null)
          srcCol = column;
        if (localeStr != null) {
          Matcher matcher = localeRegex.matcher(localeStr);
          if (matcher.find() && matcher.groupCount() == 2) {
            locale = new Locale(matcher.group(1), matcher.group(2));
          } else {
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE, "Invalid Locale specified for field: " + fld);
          }
        } else {
          locale = Locale.getDefault();
        }

        Object val = row.get(srcCol);
        String styleSmall = style.toLowerCase(Locale.ROOT);

        if (val instanceof List) {
          List<String> inputs = (List) val;
          List results = new ArrayList();
          for (String input : inputs) {
            try {
              results.add(process(input, styleSmall, locale));
            } catch (ParseException e) {
              throw new DataImportHandlerException(
                      DataImportHandlerException.SEVERE,
                      "Failed to apply NumberFormat on column: " + column, e);
            }
          }
          row.put(column, results);
        } else {
          if (val == null || val.toString().trim().equals(""))
            continue;
          try {
            row.put(column, process(val.toString(), styleSmall, locale));
          } catch (ParseException e) {
            throw new DataImportHandlerException(
                    DataImportHandlerException.SEVERE,
                    "Failed to apply NumberFormat on column: " + column, e);
          }
        }
      }
    }
    return row;
  }

  private Number process(String val, String style, Locale locale) throws ParseException {
    if (INTEGER.equals(style)) {
      return parseNumber(val, NumberFormat.getIntegerInstance(locale));
    } else if (NUMBER.equals(style)) {
      return parseNumber(val, NumberFormat.getNumberInstance(locale));
    } else if (CURRENCY.equals(style)) {
      return parseNumber(val, NumberFormat.getCurrencyInstance(locale));
    } else if (PERCENT.equals(style)) {
      return parseNumber(val, NumberFormat.getPercentInstance(locale));
    }

    return null;
  }

  private Number parseNumber(String val, NumberFormat numFormat) throws ParseException {
    ParsePosition parsePos = new ParsePosition(0);
    Number num = numFormat.parse(val, parsePos);
    if (parsePos.getIndex() != val.length()) {
      throw new ParseException("illegal number format", parsePos.getIndex());
    }
    return num;
  }

  public static final String FORMAT_STYLE = "formatStyle";

  public static final String LOCALE = "locale";

  public static final String NUMBER = "number";

  public static final String PERCENT = "percent";

  public static final String INTEGER = "integer";

  public static final String CURRENCY = "currency";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3511.java