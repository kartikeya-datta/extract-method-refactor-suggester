error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5038.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5038.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5038.java
text:
```scala
private static final L@@ogger logger = Logger.getLogger(CalendarTimerTask.class);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.ejb3.timerservice.mk2.task;

import org.jboss.as.ejb3.timerservice.mk2.CalendarTimer;
import org.jboss.as.ejb3.timerservice.mk2.TimerState;
import org.jboss.as.ejb3.timerservice.spi.MultiTimeoutMethodTimedObjectInvoker;
import org.jboss.as.ejb3.timerservice.spi.TimedObjectInvoker;
import org.jboss.logging.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * CalendarTimerTask
 *
 * @author Jaikiran Pai
 * @version $Revision: $
 */
public class CalendarTimerTask extends TimerTask<CalendarTimer> {

    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(CalendarTimerTask.class);

    /**
     * @param calendarTimer
     */
    public CalendarTimerTask(CalendarTimer calendarTimer) {
        super(calendarTimer);
    }

    @Override
    protected void callTimeout() throws Exception {
        CalendarTimer calendarTimer = this.getTimer();

        // if we have any more schedules remaining, then schedule a new task
        if (calendarTimer.getNextExpiration() != null && !calendarTimer.isInRetry()) {
            calendarTimer.scheduleTimeout();
        }

        // finally invoke the timeout method through the invoker
        if (calendarTimer.isAutoTimer()) {
            TimedObjectInvoker invoker = this.timerService.getInvoker();
            if (!(invoker instanceof MultiTimeoutMethodTimedObjectInvoker)) {
                String msg = "Cannot invoke timeout method because timer: " + calendarTimer
                        + " is an auto timer, but invoker is not of type" + MultiTimeoutMethodTimedObjectInvoker.class;
                logger.error(msg);
                throw new RuntimeException(msg);
            }
            // call the timeout method
            ((MultiTimeoutMethodTimedObjectInvoker) invoker).callTimeout(calendarTimer, calendarTimer.getTimeoutMethod());
        } else {
            this.timerService.getInvoker().callTimeout(calendarTimer);
        }
    }

    @Override
    protected Date calculateNextTimeout() {
        // The next timeout for the calendar timer will have to be computed using the
        // current "nextExpiration"
        Date currentTimeout = this.getTimer().getNextExpiration();
        if (currentTimeout == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(currentTimeout);
        // now compute the next timeout date
        Calendar nextTimeout = this.getTimer().getCalendarTimeout().getNextTimeout(cal);
        if (nextTimeout != null) {
            return nextTimeout.getTime();
        }
        return null;
    }

    @Override
    protected void postTimeoutProcessing() {
        CalendarTimer calendarTimer = this.getTimer();
        TimerState timerState = calendarTimer.getState();
        if (timerState == TimerState.IN_TIMEOUT || timerState == TimerState.RETRY_TIMEOUT) {
            if (calendarTimer.getNextExpiration() == null) {
                calendarTimer.expireTimer();
            } else {
                calendarTimer.setTimerState(TimerState.ACTIVE);
                // persist changes
                timerService.persistTimer(calendarTimer);
            }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5038.java