error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17793.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17793.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[337,80]

error in qdox parser
file content:
```java
offset: 9116
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17793.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.util.time;


import java.text.ParseException;

import java.util.Calendar;

import wicket.util.lang.EnumeratedType;

/**
 * An immutable time of day value in milliseconds.
 * @author Jonathan Locke
 */
public final class TimeOfDay extends AbstractTime
{
	/** serialVersionUID. */
	private static final long serialVersionUID = 981684175051766950L;

	/** Constant for AM time. */
	public static final Meridian AM = new Meridian("AM");

	/** Constant for PM time. */
    public static final Meridian PM = new Meridian("PM");

    /** Constant for midnight. */
    public static final TimeOfDay MIDNIGHT = time(12, 0, AM);

    /** Constant for noon. */
    public static final TimeOfDay NOON = time(12, 0, PM);

    /**
     * Private utility constructor forces use of static factory methods.
     * @param time Time today in milliseconds
     */
    private TimeOfDay(final long time)
    {
        super(time);

        // A time of day value must be less than 1 day of milliseconds
        if (Duration.valueOf(time).greaterThan(Duration.ONE_DAY))
        {
            throw new IllegalArgumentException("Time " + this + " is not a time of day value");
        }
    }

    /**
     * Gets the hour of the day.
     * @return The hour of the day (0-23)
     */
    public int getHour()
    {
        return toHours(getMilliseconds());
    }

    /**
     * Gets the minute.
     * @return The minute (0-59)
     */
    public int getMinute()
    {
        return toMinutes(getMilliseconds()) % 60;
    }

    /**
     * Gets the second.
     * @return The second (0-59)
     */
    public int getSecond()
    {
        return toSeconds(getMilliseconds()) % 60;
    }

    /**
     * Converts to TimeOfDay instance.
     * @param time The time in milliseconds today
     * @return The time of day
     */
    public static TimeOfDay valueOf(final long time)
    {
        return new TimeOfDay(time);
    }

    /**
     * Converts to TimeOfDay instance.
     * @param time A string in h.mma format
     * @return The time of day on the given calendar
     * @throws ParseException
     */
    public static TimeOfDay valueOf(final String time) throws ParseException
    {
        return valueOf(localtime, time);
    }

    /**
     * Converts to TimeOfDay instance.
     * @param calendar The calendar to use when parsing time string
     * @param time A string in h.mma format
     * @return The time of day on the given calendar
     * @throws ParseException
     */
    public static TimeOfDay valueOf(final Calendar calendar, final String time)
            throws ParseException
    {
        synchronized (timeFormat)
        {
            synchronized (calendar)
            {
                timeFormat.setCalendar(calendar);

                return new TimeOfDay(timeFormat.parse(time).getTime());
            }
        }
    }

    /**
     * Converts to TimeOfDay instance.
     * @param duration The duration
     * @return The time of day for the duration since midnight
     */
    public static TimeOfDay valueOf(final Duration duration)
    {
        return new TimeOfDay(duration.getMilliseconds());
    }

    /**
     * Converts to TimeOfDay instance.
     * @param time Time to convert to time of day
     * @return The time of day in the current timezone
     */
    public static TimeOfDay valueOf(final Time time)
    {
        return valueOf(Time.localtime, time);
    }

    /**
     * Converts to TimeOfDay instance.
     * @param calendar The calendar to use when converting time value
     * @param time The time to convert to a time of day
     * @return The time of day for this time
     */
    public static TimeOfDay valueOf(final Calendar calendar, final Time time)
    {
        return militaryTime(time.getHour(calendar), time.getMinute(calendar), time
                .getSecond(calendar));
    }

    /**
     * Gets a time of day value on a 24 hour clock.
     * @param hour The hour (0-23)
     * @param minute The minute (0-59)
     * @param second The second (0-59)
     * @return The time of day
     */
    public static TimeOfDay militaryTime(final int hour, final int minute, final int second)
    {
        if ((hour > 23) || (hour < 0))
        {
            throw new IllegalArgumentException("Hour " + hour + " is not valid");
        }

        if ((minute > 59) || (minute < 0))
        {
            throw new IllegalArgumentException("Minute " + minute + " is not valid");
        }

        if ((second > 59) || (second < 0))
        {
            throw new IllegalArgumentException("Second " + second + " is not valid");
        }

        return valueOf(Duration.hours(hour).add(Duration.minutes(minute)).add(
                Duration.seconds(second)));
    }

    /**
     * Gets a time of day on a 12 hour clock.
     * @param hour The hour (1-12)
     * @param minute The minute (0-59)
     * @param meridian AM/PM
     * @return The time value
     */
    public static TimeOfDay time(final int hour, final int minute, final Meridian meridian)
    {
        return time(hour, minute, 0, meridian);
    }

    /**
     * Gets a time of day on a 12 hour clock.
     * @param hour The hour (1-12)
     * @param minute The minute (0-59)
     * @param second The second (0-59)
     * @param meridian AM/PM
     * @return The time value
     */
    public static TimeOfDay time(final int hour, final int minute, final int second,
            final Meridian meridian)
    {
        if (meridian == PM)
        {
            if (hour == 12)
            {
                return militaryTime(12, minute, second);
            }
            else
            {
                return militaryTime(hour + 12, minute, second);
            }
        }
        else
        {
            if (hour == 12)
            {
                return militaryTime(0, minute, second);
            }
            else
            {
                return militaryTime(hour, minute, second);
            }
        }
    }

    /**
     * Gets the time of day it is now.
     * @return The time of day it is now
     */
    public static TimeOfDay now()
    {
        return valueOf(Time.now());
    }

    /**
     * Gets the time of day it is now on the given calendar.
     * @param calendar The calendar to use
     * @return The time of day it is now on the given calendar
     */
    public static TimeOfDay now(final Calendar calendar)
    {
        return valueOf(calendar, Time.now());
    }

    /**
     * Gets the next occurrence of this time of day in localtime.
     * @return The next occurrence of this time of day in localtime
     */
    public Time next()
    {
        return next(Time.localtime);
    }

    /**
     * Gets the next occurence of this time of day on the given calendar.
     * @param calendar The calendar to use
     * @return The next occurrence of this time of day on the given calendar
     */
    public Time next(final Calendar calendar)
    {
        // Get this time of day today
        final Time timeToday = Time.valueOf(calendar, this);

        // If it has already passed
        if (timeToday.before(Time.now()))
        {
            // Return the time tomorrow
            return timeToday.add(Duration.ONE_DAY);
        }
        else
        {
            // Time hasn't happened yet today
            return timeToday;
        }
    }

    /**
     * Gets miliseconds as seconds.
     * @param milliseconds miliseconds to convert
     * @return converted input
     */
    private int toSeconds(final long milliseconds)
    {
        return (int) (milliseconds / 1000);
    }

    /**
     * Gets miliseconds as minutes.
     * @param milliseconds miliseconds to convert
     * @return converted input
     */
    private int toMinutes(final long milliseconds)
    {
        return toSeconds(milliseconds) / 60;
    }

    /**
     * Gets miliseconds as hours.
     * @param milliseconds miliseconds to convert
     * @return converted input
     */
    private int toHours(final long milliseconds)
    {
        return toMinutes(milliseconds) / 60;
    }

    /** Typesafe AM/PM enum. */
    public static final class Meridian extends EnumeratedType
    {
        /**
         * Construct.
         * @param name the meridian name (value)
         */
        Meridian(final String name)
        {
            super(name);
        }
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17793.java