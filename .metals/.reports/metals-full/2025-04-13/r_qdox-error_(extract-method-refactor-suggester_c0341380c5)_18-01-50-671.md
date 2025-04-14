error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17791.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17791.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[172,80]

error in qdox parser
file content:
```java
offset: 5093
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17791.java
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

/**
 * Immutable class which represents an interval of time with a beginning and an end. The
 * beginning value is inclusive and the end value is exclusive. In other words, the time
 * frame of 1pm to 2pm includes 1pm, but not 2pm. 1:59:59 is the last value in the
 * timeframe.
 * @author Jonathan Locke
 */
public final class TimeFrame implements ITimeFrameSource
{
    // Start and end points of this time frame
    private final Time start;

    private final Time end;

    /**
     * Private constructor to force use of static factory methods
     * @param start The start time
     * @param end The end time
     * @throws IllegalArgumentException Thrown if start time is before end time
     */
    private TimeFrame(final Time start, final Time end)
    {
        check(start, end);
        this.start = start;
        this.end = end;
    }

    /**
     * @return Gets this timeframe
     */
    public TimeFrame get()
    {
        return this;
    }

    /**
     * Creates a time frame for a start and end time
     * @param start The start time
     * @param end The end time
     * @return The time frame
     * @throws IllegalArgumentException Thrown if start time is before end time
     */
    public static TimeFrame valueOf(final Time start, final Time end)
    {
        return new TimeFrame(start, end);
    }

    /**
     * Creates a time frame for a start and duration
     * @param start The start time
     * @param duration The duration
     * @return The time frame
     * @throws IllegalArgumentException Thrown if start time is before end time
     */
    public static TimeFrame valueOf(final Time start, final Duration duration)
    {
        return new TimeFrame(start, start.add(duration));
    }

    /**
     * Returns a timeframe source for a start and end time-of-day. For example, called
     * with 3pm and 5pm as parameters, the timeframe source returned would produce
     * timeframe objects representing 3pm-5pm on whatever day it is when the caller calls
     * the timeframesource interface.
     * @param startTimeOfDay The start time for this time frame each day
     * @param endTimeOfDay The end time for this time frame each day
     * @return A timeframe source which will return the specified timeframe each day
     */
    public static ITimeFrameSource eachDay(final TimeOfDay startTimeOfDay,
            final TimeOfDay endTimeOfDay)
    {
        check(startTimeOfDay, endTimeOfDay);

        return new ITimeFrameSource()
        {
            public TimeFrame get()
            {
                return new TimeFrame(Time.valueOf(startTimeOfDay), Time.valueOf(endTimeOfDay));
            }
        };
    }

    /**
     * Checks consistency of start and end values
     * @param start Start value
     * @param end End value
     */
    private static void check(final AbstractTimeValue start, final AbstractTimeValue end)
    {
        // Throw illegal argument exception if start is less than end
        if (end.lessThan(start))
        {
            throw new IllegalArgumentException("Start time of time frame "
                    + start + " was after end time " + end);
        }
    }

    /**
     * @return The duration of this time frame
     */
    public Duration getDuration()
    {
        return end.subtract(start);
    }

    /**
     * @param time The time to check
     * @return True if this time frame contains the given time
     */
    public boolean contains(final Time time)
    {
        return (start.equals(time) || start.before(time)) && end.after(time);
    }

    /**
     * @param timeframe The timeframe to test
     * @return True if the given timeframe overlaps this one
     */
    public boolean overlaps(final TimeFrame timeframe)
    {
        return contains(timeframe.start)
 contains(timeframe.end) || timeframe.contains(start) || timeframe.contains(end);
    }

    /**
     * @return The start of this time frame
     */
    public Time getStart()
    {
        return start;
    }

    /**
     * @return The end of this time frame
     */
    public Time getEnd()
    {
        return end;
    }

    /**
     * @return String representation of this object
     */
    public String toString()
    {
        return "[start=" + start + ", end=" + end + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17791.java