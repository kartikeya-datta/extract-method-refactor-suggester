error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17186.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17186.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17186.java
text:
```scala
private M@@ap<Object, Date> profileData = new HashMap<Object, Date>();

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.util.StringUtils;

/**
 * This is a special logger that is designed to profile builds.
 *
 * @since Ant1.8
 */
public class ProfileLogger extends DefaultLogger {

    private Map profileData = new HashMap(); // <Object, Date>

    /**
     * Logs a message to say that the target has started.
     *
     * @param event
     *            An event with any relevant extra information. Must not be
     *            <code>null</code>.
     */
    public void targetStarted(BuildEvent event) {
        Date now = new Date();
        String name = "Target " + event.getTarget().getName();
        logStart(event, now, name);
        profileData.put(event.getTarget(), now);
    }

    /**
     * Logs a message to say that the target has finished.
     *
     * @param event
     *            An event with any relevant extra information. Must not be
     *            <code>null</code>.
     */
    public void targetFinished(BuildEvent event) {
        Date start = (Date) profileData.remove(event.getTarget());
        String name = "Target " + event.getTarget().getName();
        logFinish(event, start, name);
    }

    /**
     * Logs a message to say that the task has started.
     *
     * @param event
     *            An event with any relevant extra information. Must not be
     *            <code>null</code>.
     */
    public void taskStarted(BuildEvent event) {
        String name = event.getTask().getTaskName();
        Date now = new Date();
        logStart(event, now, name);
        profileData.put(event.getTask(), now);
    }

    /**
     * Logs a message to say that the task has finished.
     *
     * @param event
     *            An event with any relevant extra information. Must not be
     *            <code>null</code>.
     */
    public void taskFinished(BuildEvent event) {
        Date start = (Date) profileData.remove(event.getTask());
        String name = event.getTask().getTaskName();
        logFinish(event, start, name);
    }

    private void logFinish(BuildEvent event, Date start, String name) {
        Date now = new Date();
        String msg = null;
        if (start != null) {
            long diff = now.getTime() - start.getTime();
            msg = StringUtils.LINE_SEP + name + ": finished " + now + " ("
                    + diff + "ms)";
        } else {
            msg = StringUtils.LINE_SEP + name + ": finished " + now
                    + " (unknown duration, start not detected)";
        }
        printMessage(msg, out, event.getPriority());
        log(msg);
    }

    private void logStart(BuildEvent event, Date start, String name) {
        String msg = StringUtils.LINE_SEP + name + ": started " + start;
        printMessage(msg, out, event.getPriority());
        log(msg);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17186.java