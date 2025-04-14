error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4116.java
text:
```scala
protected A@@utoTimer fromAnnotation(final AnnotationInstance annotationInstance, final boolean replacement) {

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.ejb3.deployment.processors.annotation;

import org.jboss.as.ee.metadata.ClassAnnotationInformationFactory;
import org.jboss.as.ejb3.timerservice.AutoTimer;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

import javax.ejb.Schedule;
import javax.ejb.Schedules;
import static org.jboss.as.ejb3.EjbMessages.MESSAGES;
/**
 * {@link org.jboss.as.ee.metadata.ClassAnnotationInformation} for Schedule annotation
 *
 * @author Stuart Douglas
 */
public class ScheduleAnnotationInformationFactory extends ClassAnnotationInformationFactory<Schedule, AutoTimer> {

    public ScheduleAnnotationInformationFactory() {
        super(Schedule.class, Schedules.class);
    }

    @Override
    protected AutoTimer fromAnnotation(final AnnotationInstance annotationInstance) {
        final AutoTimer timer = new AutoTimer();
        for (ScheduleValues schedulePart : ScheduleValues.values()) {
            schedulePart.set(timer, annotationInstance);
        }
        return timer;
    }


    enum ScheduleValues {
        DAY_OF_MONTH("dayOfMonth", "*") {
            protected void setString(final AutoTimer timer, final String value) {
                timer.getScheduleExpression().dayOfMonth(value);
            }
        },

        DAY_OF_WEEK("dayOfWeek", "*") {
            protected void setString(final AutoTimer timer, final String value) {
                timer.getScheduleExpression().dayOfWeek(value);
            }
        },
        HOUR("hour", "0") {
            protected void setString(final AutoTimer timer, final String value) {
                timer.getScheduleExpression().hour(value);
            }
        },
        INFO("info", null) {
            protected void setString(final AutoTimer timer, final String value) {
                timer.getTimerConfig().setInfo(value);
            }
        },
        MINUTE("minute", "0") {
            protected void setString(final AutoTimer timer, final String value) {
                timer.getScheduleExpression().minute(value);
            }
        },
        MONTH("month", "*") {
            protected void setString(final AutoTimer timer, final String value) {
                timer.getScheduleExpression().month(value);
            }
        },
        PERSISTENT("persistent", true) {
            protected void setBoolean(final AutoTimer timer, final boolean value) {
                timer.getTimerConfig().setPersistent(value);
            }
        },
        SECOND("second", "0") {
            protected void setString(final AutoTimer timer, final String value) {
                timer.getScheduleExpression().second(value);
            }
        },
        TIMEZONE("timezone", "") {
            protected void setString(final AutoTimer timer, final String value) {
                timer.getScheduleExpression().timezone(value);
            }
        },
        YEAR("year", "*") {
            protected void setString(final AutoTimer timer, final String value) {
                timer.getScheduleExpression().year(value);
            }
        },;

        private final String name;
        private final String defaultStringValue;
        private final boolean defaultBooleanValue;
        private final boolean booleanValue;

        ScheduleValues(final String name, final String defaultStringValue) {
            this.name = name;
            this.defaultStringValue = defaultStringValue;
            this.defaultBooleanValue = false;
            this.booleanValue = false;
        }

        ScheduleValues(final String name, final boolean defaultBooleanValue) {
            this.name = name;
            this.defaultStringValue = null;
            this.defaultBooleanValue = defaultBooleanValue;
            this.booleanValue = true;
        }

        public void set(final AutoTimer timer, final AnnotationInstance annotationInstance) {
            final AnnotationValue value = annotationInstance.value(name);
            if (booleanValue) {
                if (value == null) {
                    setBoolean(timer, defaultBooleanValue);
                } else {
                    setBoolean(timer, value.asBoolean());
                }
            } else {
                if (value == null) {
                    setString(timer, defaultStringValue);
                } else {
                    setString(timer, value.asString());
                }
            }
        }

        protected void setString(final AutoTimer expression, final String value) {
            throw MESSAGES.shouldBeOverridden();
        }

        protected void setBoolean(final AutoTimer expression, final boolean value) {
            throw MESSAGES.shouldBeOverridden();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4116.java