error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1371.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1371.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1371.java
text:
```scala
l@@ocale = availableLocales.get(localeStr);

package org.apache.solr.handler.dataimport;

import static org.apache.solr.handler.dataimport.DataImportHandlerException.SEVERE;
import static org.apache.solr.handler.dataimport.DataImportHandlerException.wrapAndThrow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.WeakHashMap;

import org.apache.solr.handler.dataimport.config.EntityField;
import org.apache.solr.util.DateMathParser;

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

/**
 * <p>Formats values using a given date format. </p>
 * <p>Pass three parameters:
 * <ul>
 *  <li>An {@link EntityField} or a date expression to be parsed with 
 *      the {@link DateMathParser} class  If the value is in a String, 
 *      then it is assumed to be a datemath expression, otherwise it 
 *      resolved using a {@link VariableResolver} instance</li>
 *  <li>A date format see {@link SimpleDateFormat} for the syntax.</li>
 *  <li>The {@link Locale} to parse.  
 *      (optional. Defaults to the Root Locale) </li>
 * </ul>
 * </p>
 */
public class DateFormatEvaluator extends Evaluator {
  
  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  Map<DateFormatCacheKey, SimpleDateFormat> cache = new WeakHashMap<DateFormatCacheKey, SimpleDateFormat>();
  Map<String, Locale> availableLocales = new HashMap<String, Locale>();
  
  class DateFormatCacheKey {
    DateFormatCacheKey(Locale l, String df) {
      this.locale = l;
      this.dateFormat = df;
    }
    Locale locale;
    String dateFormat;
  }
  
  public DateFormatEvaluator() {  
    for (Locale locale : Locale.getAvailableLocales()) {
      availableLocales.put(locale.toString(), locale);
    }
  }
  
  
  @Override
  public String evaluate(String expression, Context context) {
    List<Object> l = parseParams(expression, context.getVariableResolver());
    if (l.size() < 2 || l.size() > 3) {
      throw new DataImportHandlerException(SEVERE, "'formatDate()' must have two or three parameters ");
    }
    Object o = l.get(0);
    Object format = l.get(1);
    if (format instanceof VariableWrapper) {
      VariableWrapper wrapper = (VariableWrapper) format;
      o = wrapper.resolve();
      format = o.toString();
    }
    Locale locale = Locale.ROOT;
    if(l.size()==3) {
      Object localeObj = l.get(2);
      String localeStr = null;
      if (localeObj  instanceof VariableWrapper) {
        VariableWrapper wrapper = (VariableWrapper) localeObj;
        o = wrapper.resolve();
        localeStr = o.toString();
      } else {
        localeStr = localeObj.toString();
      }
      availableLocales.get(localeStr);
      if(locale==null) {
        throw new DataImportHandlerException(SEVERE, "Unsupported locale: " + localeStr);
      }
    }
    String dateFmt = format.toString();
    DateFormatCacheKey dfck = new DateFormatCacheKey(locale, dateFmt);
    SimpleDateFormat sdf = cache.get(dfck);
    if(sdf==null) {
      sdf = new SimpleDateFormat(dateFmt, locale);
      cache.put(dfck, sdf);
    }
    Date date = null;
    if (o instanceof VariableWrapper) {
      VariableWrapper variableWrapper = (VariableWrapper) o;
      Object variableval = variableWrapper.resolve();
      if (variableval instanceof Date) {
        date = (Date) variableval;
      } else {
        String s = variableval.toString();
        try {
          dfck = new DateFormatCacheKey(locale, DEFAULT_DATE_FORMAT);
          sdf = cache.get(dfck);
          if(sdf==null) {
            sdf = new SimpleDateFormat(dfck.dateFormat, dfck.locale);
            cache.put(dfck, sdf);
          }
          date = new SimpleDateFormat(DEFAULT_DATE_FORMAT, locale).parse(s);
        } catch (ParseException exp) {
          wrapAndThrow(SEVERE, exp, "Invalid expression for date");
        }
      }
    } else {
      String datemathfmt = o.toString();
      datemathfmt = datemathfmt.replaceAll("NOW", "");
      try {
        date = getDateMathParser(locale).parseMath(datemathfmt);
      } catch (ParseException e) {
        wrapAndThrow(SEVERE, e, "Invalid expression for date");
      }
    }
    return sdf.format(date);
  }
  static DateMathParser getDateMathParser(Locale l) {
    return new DateMathParser(TimeZone.getDefault(), l) {
      @Override
      public Date getNow() {
        return new Date();
      }
    };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1371.java