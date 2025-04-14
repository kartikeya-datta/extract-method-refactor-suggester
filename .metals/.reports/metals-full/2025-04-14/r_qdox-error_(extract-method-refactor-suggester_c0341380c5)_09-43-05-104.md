error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2238.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2238.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2238.java
text:
```scala
_@@timeZone = timeZone;

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 */
package org.apache.log4j.lf5.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date format manager.
 * Utility class to help manage consistent date formatting and parsing.
 * It may be advantageous to have multiple DateFormatManagers per
 * application.  For example, one for handling the output (formatting) of
 * dates, and another one for handling the input (parsing) of dates.
 *
 * @author Robert Shaw
 * @author Michael J. Sikorsky
 */

// Contributed by ThoughtWorks Inc.
public class DateFormatManager {
  //--------------------------------------------------------------------------
  //   Constants:
  //--------------------------------------------------------------------------

  //--------------------------------------------------------------------------
  //   Protected Variables:
  //--------------------------------------------------------------------------

  //--------------------------------------------------------------------------
  //   Private Variables:
  //--------------------------------------------------------------------------
  private TimeZone _timeZone = null;
  private Locale _locale = null;

  private String _pattern = null;
  private DateFormat _dateFormat = null;

  //--------------------------------------------------------------------------
  //   Constructors:
  //--------------------------------------------------------------------------
  public DateFormatManager() {
    super();
    configure();
  }

  public DateFormatManager(TimeZone timeZone) {
    super();

    _timeZone = timeZone;
    configure();
  }

  public DateFormatManager(Locale locale) {
    super();

    _locale = locale;
    configure();
  }

  public DateFormatManager(String pattern) {
    super();

    _pattern = pattern;
    configure();
  }

  public DateFormatManager(TimeZone timeZone, Locale locale) {
    super();

    _timeZone = timeZone;
    _locale = locale;
    configure();
  }

  public DateFormatManager(TimeZone timeZone, String pattern) {
    super();

    _timeZone = timeZone;
    _pattern = pattern;
    configure();
  }

  public DateFormatManager(Locale locale, String pattern) {
    super();

    _locale = locale;
    _pattern = pattern;
    configure();
  }

  public DateFormatManager(TimeZone timeZone, Locale locale, String pattern) {
    super();

    _timeZone = timeZone;
    _locale = locale;
    _pattern = pattern;
    configure();
  }

  //--------------------------------------------------------------------------
  //   Public Methods:
  //--------------------------------------------------------------------------

  public synchronized TimeZone getTimeZone() {
    if (_timeZone == null) {
      return TimeZone.getDefault();
    } else {
      return _timeZone;
    }
  }

  public synchronized void setTimeZone(TimeZone timeZone) {
    timeZone = timeZone;
    configure();
  }

  public synchronized Locale getLocale() {
    if (_locale == null) {
      return Locale.getDefault();
    } else {
      return _locale;
    }
  }

  public synchronized void setLocale(Locale locale) {
    _locale = locale;
    configure();
  }

  public synchronized String getPattern() {
    return _pattern;
  }

  /**
   * Set the pattern. i.e. "EEEEE, MMMMM d, yyyy hh:mm aaa"
   */
  public synchronized void setPattern(String pattern) {
    _pattern = pattern;
    configure();
  }


  /**
   * This method has been deprecated in favour of getPattern().
   * @deprecated Use getPattern().
   */
  public synchronized String getOutputFormat() {
    return _pattern;
  }

  /**
   * This method has been deprecated in favour of setPattern().
   * @deprecated Use setPattern().
   */
  public synchronized void setOutputFormat(String pattern) {
    _pattern = pattern;
    configure();
  }

  public synchronized DateFormat getDateFormatInstance() {
    return _dateFormat;
  }

  public synchronized void setDateFormatInstance(DateFormat dateFormat) {
    _dateFormat = dateFormat;
    // No reconfiguration necessary!
  }

  public String format(Date date) {
    return getDateFormatInstance().format(date);
  }

  public String format(Date date, String pattern) {
    DateFormat formatter = null;
    formatter = getDateFormatInstance();
    if (formatter instanceof SimpleDateFormat) {
      formatter = (SimpleDateFormat) (formatter.clone());
      ((SimpleDateFormat) formatter).applyPattern(pattern);
    }
    return formatter.format(date);
  }

  /**
   * @throws java.text.ParseException
   */
  public Date parse(String date) throws ParseException {
    return getDateFormatInstance().parse(date);
  }

  /**
   * @throws java.text.ParseException
   */
  public Date parse(String date, String pattern) throws ParseException {
    DateFormat formatter = null;
    formatter = getDateFormatInstance();
    if (formatter instanceof SimpleDateFormat) {
      formatter = (SimpleDateFormat) (formatter.clone());
      ((SimpleDateFormat) formatter).applyPattern(pattern);
    }
    return formatter.parse(date);
  }

  //--------------------------------------------------------------------------
  //   Protected Methods:
  //--------------------------------------------------------------------------

  //--------------------------------------------------------------------------
  //   Private Methods:
  //--------------------------------------------------------------------------
  private synchronized void configure() {
    _dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.FULL,
        DateFormat.FULL,
        getLocale());
    _dateFormat.setTimeZone(getTimeZone());

    if (_pattern != null) {
      ((SimpleDateFormat) _dateFormat).applyPattern(_pattern);
    }
  }

  //--------------------------------------------------------------------------
  //   Nested Top-Level Classes or Interfaces:
  //--------------------------------------------------------------------------

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2238.java