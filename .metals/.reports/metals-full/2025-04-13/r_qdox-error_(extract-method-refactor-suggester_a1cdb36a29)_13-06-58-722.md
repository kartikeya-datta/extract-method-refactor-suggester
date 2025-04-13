error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1950.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1950.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1950.java
text:
```scala
final L@@ogger logger = Logger.getLogger(RollingCalendar.class);

/*
 * Copyright 1999,2004 The Apache Software Foundation.
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

package org.apache.log4j.rolling.helpers;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


/**

/**
 * RollingCalendar is a helper class to DailyRollingFileAppender or similar
 * timed-based rolling policies. Given a periodicity type and the current
 * time, it computes the start of the next interval.
 *
 * @author Ceki G&uuml;lc&uuml;
 *
 * */
public class RollingCalendar extends GregorianCalendar {
  static final Logger logger = Logger.getLogger(RollingCalendar.class);

  // The gmtTimeZone is used only in computeCheckPeriod() method.
  static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");

  // The code assumes that the following constants are in a increasing
  // sequence.
  public static final int TOP_OF_TROUBLE = -1;
  public static final int TOP_OF_SECOND = 0;
  public static final int TOP_OF_MINUTE = 1;
  public static final int TOP_OF_HOUR = 2;
  public static final int HALF_DAY = 3;
  public static final int TOP_OF_DAY = 4;
  public static final int TOP_OF_WEEK = 5;
  public static final int TOP_OF_MONTH = 6;
  int type = TOP_OF_TROUBLE;

  public RollingCalendar() {
    super();
  }

  public RollingCalendar(TimeZone tz, Locale locale) {
    super(tz, locale);
  }

  public void init(String datePattern) {
    type = computeTriggeringPeriod(datePattern);
  }

  public void setType(int type) {
    this.type = type;
  }

  public long getNextCheckMillis(Date now) {
    return getNextCheckDate(now).getTime();
  }

  // This method computes the roll over period by looping over the
  // periods, starting with the shortest, and stopping when the r0 is
  // different from from r1, where r0 is the epoch formatted according
  // the datePattern (supplied by the user) and r1 is the
  // epoch+nextMillis(i) formatted according to datePattern. All date
  // formatting is done in GMT and not local format because the test
  // logic is based on comparisons relative to 1970-01-01 00:00:00
  // GMT (the epoch).
  public int computeTriggeringPeriod(String datePattern) {
    RollingCalendar rollingCalendar =
      new RollingCalendar(GMT_TIMEZONE, Locale.getDefault());

    // set sate to 1970-01-01 00:00:00 GMT
    Date epoch = new Date(0);

    if (datePattern != null) {
      for (int i = TOP_OF_SECOND; i <= TOP_OF_MONTH; i++) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        simpleDateFormat.setTimeZone(GMT_TIMEZONE); // do all date formatting in GMT

        String r0 = simpleDateFormat.format(epoch);
        rollingCalendar.setType(i);

        Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
        String r1 = simpleDateFormat.format(next);

        //System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
        if ((r0 != null) && (r1 != null) && !r0.equals(r1)) {
          return i;
        }
      }
    }

    return TOP_OF_TROUBLE; // Deliberately head for trouble...
  }

  public void printPeriodicity() {
    switch (type) {
    case TOP_OF_SECOND:
      logger.debug("Rollover every second.");

      break;

    case TOP_OF_MINUTE:
      logger.debug("Rollover every minute.");

      break;

    case TOP_OF_HOUR:
      logger.debug("Rollover at the top of every hour.");

      break;

    case HALF_DAY:
      logger.debug("Rollover at midday and midnight.");

      break;

    case TOP_OF_DAY:
      logger.debug("Rollover at midnight.");

      break;

    case TOP_OF_WEEK:
      logger.debug("Rollover at the start of week.");

      break;

    case TOP_OF_MONTH:
      logger.debug("Rollover at start of every month.");

      break;

    default:
      logger.warn("Unknown periodicity.");
    }
  }

  public Date getNextCheckDate(Date now) {
    this.setTime(now);

    switch (type) {
    case TOP_OF_SECOND:
      this.set(Calendar.MILLISECOND, 0);
      this.add(Calendar.SECOND, 1);

      break;

    case TOP_OF_MINUTE:
      this.set(Calendar.SECOND, 0);
      this.set(Calendar.MILLISECOND, 0);
      this.add(Calendar.MINUTE, 1);

      break;

    case TOP_OF_HOUR:
      this.set(Calendar.MINUTE, 0);
      this.set(Calendar.SECOND, 0);
      this.set(Calendar.MILLISECOND, 0);
      this.add(Calendar.HOUR_OF_DAY, 1);

      break;

    case HALF_DAY:
      this.set(Calendar.MINUTE, 0);
      this.set(Calendar.SECOND, 0);
      this.set(Calendar.MILLISECOND, 0);

      int hour = get(Calendar.HOUR_OF_DAY);

      if (hour < 12) {
        this.set(Calendar.HOUR_OF_DAY, 12);
      } else {
        this.set(Calendar.HOUR_OF_DAY, 0);
        this.add(Calendar.DAY_OF_MONTH, 1);
      }

      break;

    case TOP_OF_DAY:
      this.set(Calendar.HOUR_OF_DAY, 0);
      this.set(Calendar.MINUTE, 0);
      this.set(Calendar.SECOND, 0);
      this.set(Calendar.MILLISECOND, 0);
      this.add(Calendar.DATE, 1);

      break;

    case TOP_OF_WEEK:
      this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
      this.set(Calendar.HOUR_OF_DAY, 0);
      this.set(Calendar.SECOND, 0);
      this.set(Calendar.MILLISECOND, 0);
      this.add(Calendar.WEEK_OF_YEAR, 1);

      break;

    case TOP_OF_MONTH:
      this.set(Calendar.DATE, 1);
      this.set(Calendar.HOUR_OF_DAY, 0);
      this.set(Calendar.SECOND, 0);
      this.set(Calendar.MILLISECOND, 0);
      this.add(Calendar.MONTH, 1);

      break;

    default:
      throw new IllegalStateException("Unknown periodicity type.");
    }

    return getTime();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1950.java