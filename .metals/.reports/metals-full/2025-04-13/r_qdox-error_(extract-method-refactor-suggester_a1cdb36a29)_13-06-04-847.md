error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8019.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8019.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8019.java
text:
```scala
c@@ontext.getEngine().stopTest();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.jmeter.sampler;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Dummy Sampler used to pause or stop a thread or the test;
 * intended for use in Conditional Controllers.
 *
 */
public class TestAction extends AbstractSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();

    // Actions
    public final static int STOP = 0;
    public final static int PAUSE = 1;
    public final static int STOP_NOW = 2;

    // Action targets
    public final static int THREAD = 0;
    // public final static int THREAD_GROUP = 1;
    public final static int TEST = 2;

    // Identifiers
    private final static String TARGET = "ActionProcessor.target"; //$NON-NLS-1$
    private final static String ACTION = "ActionProcessor.action"; //$NON-NLS-1$
    private final static String DURATION = "ActionProcessor.duration"; //$NON-NLS-1$

    public TestAction() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.samplers.Sampler#sample(org.apache.jmeter.samplers.Entry)
     */
    public SampleResult sample(Entry e) {
        JMeterContext context = JMeterContextService.getContext();

        int target = getTarget();
        int action = getAction();
        if (action == PAUSE) {
            pause(getDurationAsString());
        } else if (action == STOP || action == STOP_NOW) {
            if (target == THREAD) {
                log.info("Stopping current thread");
                context.getThread().stop();
//             //Not yet implemented
//            } else if (target==THREAD_GROUP) {
            } else if (target == TEST) {
                if (action == STOP_NOW) {
                    log.info("Stopping all threads now");
                    context.getEngine().askThreadsToStopNow();
                } else {
                    log.info("Stopping all threads");
                    context.getEngine().askThreadsToStop();                    
                }
            }
        }

        return null; // This means no sample is saved
    }

    private void pause(String mili_s) {
        int milis;
        try {
            milis=Integer.parseInt(mili_s);
        } catch (NumberFormatException e){
            log.warn("Could not create number from "+mili_s);
            milis=0;
        }
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
        }
    }

    public void setTarget(int target) {
        setProperty(new IntegerProperty(TARGET, target));
    }

    public int getTarget() {
        return getPropertyAsInt(TARGET);
    }

    public void setAction(int action) {
        setProperty(new IntegerProperty(ACTION, action));
    }

    public int getAction() {
        return getPropertyAsInt(ACTION);
    }

    public void setDuration(String duration) {
        setProperty(new StringProperty(DURATION, duration));
    }

    public String getDurationAsString() {
        return getPropertyAsString(DURATION);
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8019.java