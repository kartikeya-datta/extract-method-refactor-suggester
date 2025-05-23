error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8975.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8975.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8975.java
text:
```scala
.@@getTime(), 0, info, persistent, primaryKey, TimerState.CREATED);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.ejb3.timerservice;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Calendar;

import javax.ejb.EJBException;
import javax.ejb.ScheduleExpression;

import org.jboss.as.ejb3.timerservice.persistence.CalendarTimerEntity;
import org.jboss.as.ejb3.timerservice.persistence.TimeoutMethod;
import org.jboss.as.ejb3.timerservice.persistence.TimerEntity;
import org.jboss.as.ejb3.timerservice.schedule.CalendarBasedTimeout;
import org.jboss.as.ejb3.timerservice.task.CalendarTimerTask;
import org.jboss.as.ejb3.timerservice.task.TimerTask;

import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

/**
 * Represents a {@link javax.ejb.Timer} which is created out a calendar expression
 *
 * @author Jaikiran Pai
 * @version $Revision: $
 */
public class CalendarTimer extends TimerImpl {


    /**
     * The calendar based timeout for this timer
     */
    private final CalendarBasedTimeout calendarTimeout;

    /**
     * Represents whether this is an auto-timer or a normal
     * programatically created timer
     */
    private final boolean autoTimer;

    private final Method timeoutMethod;

    /**
     * Constructs a {@link CalendarTimer}
     *
     * @param id              The id of this timer
     * @param timerService    The timer service to which this timer belongs
     * @param calendarTimeout The {@link CalendarBasedTimeout} from which this {@link CalendarTimer} is being created
     */
    public CalendarTimer(String id, TimerServiceImpl timerService, CalendarBasedTimeout calendarTimeout, Object primaryKey) {
        this(id, timerService, calendarTimeout, null, true, primaryKey);
    }

    /**
     * Constructs a {@link CalendarTimer}
     *
     * @param id              The id of this timer
     * @param timerService    The timer service to which this timer belongs
     * @param calendarTimeout The {@link CalendarBasedTimeout} from which this {@link CalendarTimer} is being created
     * @param info            The serializable info which will be made available through {@link javax.ejb.Timer#getInfo()}
     * @param persistent      True if this timer is persistent. False otherwise
     */
    public CalendarTimer(String id, TimerServiceImpl timerService, CalendarBasedTimeout calendarTimeout,
                         Serializable info, boolean persistent, Object primaryKey) {
        this(id, timerService, calendarTimeout, info, persistent, null, primaryKey);
    }

    /**
     * Constructs a {@link CalendarTimer}
     *
     * @param id              The id of this timer
     * @param timerService    The timer service to which this timer belongs
     * @param calendarTimeout The {@link CalendarBasedTimeout} from which this {@link CalendarTimer} is being created
     * @param info            The serializable info which will be made available through {@link javax.ejb.Timer#getInfo()}
     * @param persistent      True if this timer is persistent. False otherwise
     * @param timeoutMethod   If this is a non-null value, then this {@link CalendarTimer} is marked as an auto-timer.
     *                        This <code>timeoutMethod</code> is then considered as the name of the timeout method which has to
     *                        be invoked when this timer times out.
     */
    public CalendarTimer(String id, TimerServiceImpl timerService, CalendarBasedTimeout calendarTimeout,
                         Serializable info, boolean persistent, Method timeoutMethod, Object primaryKey) {
        super(id, timerService, calendarTimeout.getFirstTimeout() == null ? null : calendarTimeout.getFirstTimeout()
                .getTime(), 0, info, persistent, primaryKey);
        this.calendarTimeout = calendarTimeout;

        // compute the next timeout (from "now")
        Calendar nextTimeout = this.calendarTimeout.getNextTimeout();
        if (nextTimeout != null) {
            this.nextExpiration = nextTimeout.getTime();
        }
        // set this as an auto-timer if the passed timeout method name
        // is not null
        if (timeoutMethod != null) {
            this.autoTimer = true;
            this.timeoutMethod = timeoutMethod;
        } else {
            this.autoTimer = false;
            this.timeoutMethod = null;
        }
    }

    /**
     * Constructs a {@link CalendarTimer} from a persistent state
     *
     * @param persistedCalendarTimer The persistent state of the calendar timer
     * @param timerService           The timer service to which this timer belongs
     */
    public CalendarTimer(CalendarTimerEntity persistedCalendarTimer, TimerServiceImpl timerService) {
        super(persistedCalendarTimer, timerService);
        this.calendarTimeout = persistedCalendarTimer.getCalendarTimeout();
        // set the next expiration (which will be available in the persistent state)
        this.nextExpiration = persistedCalendarTimer.getNextDate();
        // auto-timer related attributes
        if (persistedCalendarTimer.isAutoTimer()) {
            this.autoTimer = true;
            TimeoutMethod timeoutMethodInfo = persistedCalendarTimer.getTimeoutMethod();
            this.timeoutMethod = this.getTimeoutMethod(timeoutMethodInfo);
            if (this.timeoutMethod == null) {
                throw MESSAGES.failToFindTimeoutMethod(timeoutMethodInfo);
            }
        } else {
            this.autoTimer = false;
            this.timeoutMethod = null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see #getScheduleExpression()
     */
    @Override
    public ScheduleExpression getSchedule() throws IllegalStateException, EJBException {
        this.assertTimerState();
        return this.calendarTimeout.getScheduleExpression();
    }

    /**
     * This method is similar to {@link #getSchedule()}, except that this method does <i>not</i> check the timer state
     * and hence does <i>not</i> throw either {@link IllegalStateException} or {@link javax.ejb.NoSuchObjectLocalException}
     * or {@link javax.ejb.EJBException}.
     *
     * @return
     */
    public ScheduleExpression getScheduleExpression() {
        return this.calendarTimeout.getScheduleExpression();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCalendarTimer() throws IllegalStateException, EJBException {
        this.assertTimerState();
        return true;
    }

    /**
     * Creates and return a new persistent state of this timer
     */
    @Override
    protected TimerEntity createPersistentState() {
        return new CalendarTimerEntity(this);
    }

    /**
     * Returns the {@link CalendarBasedTimeout} corresponding to this
     * {@link CalendarTimer}
     *
     * @return
     */
    public CalendarBasedTimeout getCalendarTimeout() {
        return this.calendarTimeout;
    }

    /**
     * Returns true if this is an auto-timer. Else returns false.
     */
    @Override
    public boolean isAutoTimer() {
        return autoTimer;
    }

    /**
     * Returns the task which handles the timeouts on this {@link CalendarTimer}
     *
     * @see org.jboss.as.ejb3.timerservice.task.CalendarTimerTask
     */
    @Override
    protected TimerTask<?> getTimerTask() {
        return new CalendarTimerTask(this);
    }

    public Method getTimeoutMethod() {
        if (!this.autoTimer) {
            throw MESSAGES.failToInvokegetTimeoutMethod();
        }
        return this.timeoutMethod;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * A {@link javax.ejb.Timer} is equal to another {@link javax.ejb.Timer} if their
     * {@link javax.ejb.TimerHandle}s are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.handle == null) {
            return false;
        }
        if (!(obj instanceof CalendarTimer)) {
            return false;
        }
        CalendarTimer otherTimer = (CalendarTimer) obj;
        return this.handle.equals(otherTimer.getTimerHandle());
    }


    /**
     * Returns the {@link java.lang.reflect.Method}, represented by the {@link TimeoutMethod}
     * <p>
     * Note: This method uses the {@link Thread#getContextClassLoader()} to load the
     * relevant classes while getting the {@link java.lang.reflect.Method}
     * </p>
     *
     * @param timeoutMethodInfo The timeout method
     * @return
     */
    private Method getTimeoutMethod(TimeoutMethod timeoutMethodInfo) {

        String declaringClass = timeoutMethodInfo.getDeclaringClass();
        Class<?> timeoutMethodDeclaringClass = null;
        try {
            timeoutMethodDeclaringClass = Class.forName(declaringClass, false, timedObjectInvoker.getClassLoader());
        } catch (ClassNotFoundException cnfe) {
            throw MESSAGES.failToLoadDeclaringClassOfTimeOut(declaringClass);
        }

        String timeoutMethodName = timeoutMethodInfo.getMethodName();
        String[] timeoutMethodParams = timeoutMethodInfo.getMethodParams();
        // load the method param classes
        Class<?>[] timeoutMethodParamTypes = new Class<?>[]
                {};
        if (timeoutMethodParams != null) {
            timeoutMethodParamTypes = new Class<?>[timeoutMethodParams.length];
            int i = 0;
            for (String paramClassName : timeoutMethodParams) {
                Class<?> methodParamClass = null;
                try {
                    methodParamClass = Class.forName(paramClassName, false, timedObjectInvoker.getClassLoader());
                } catch (ClassNotFoundException cnfe) {
                    throw new RuntimeException("Could not load method param class: " + paramClassName + " of timeout method", cnfe);
                }
                timeoutMethodParamTypes[i++] = methodParamClass;
            }
        }
        // now start looking for the method
        Class<?> klass = timeoutMethodDeclaringClass;
        while (klass != null) {
            Method[] methods = klass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(timeoutMethodName)) {
                    Class<?>[] methodParamTypes = method.getParameterTypes();
                    // param length doesn't match
                    if (timeoutMethodParamTypes.length != methodParamTypes.length) {
                        continue;
                    }
                    boolean match = true;
                    for (int i = 0; i < methodParamTypes.length; i++) {
                        // param type doesn't match
                        if (!timeoutMethodParamTypes[i].equals(methodParamTypes[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        // match found
                        return method;
                    }
                }
            }
            klass = klass.getSuperclass();

        }
        // no match found
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8975.java