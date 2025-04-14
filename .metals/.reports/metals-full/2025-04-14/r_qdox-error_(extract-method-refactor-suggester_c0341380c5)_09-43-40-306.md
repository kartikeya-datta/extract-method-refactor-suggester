error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16960.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16960.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16960.java
text:
```scala
l@@ong end = transactionSampleResult.currentTimeInMillis();

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

/*
 *  N.B. Although this is a type of sampler, it is only used by the transaction controller,
 *  and so is in the control package
*/
package org.apache.jmeter.control;


import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

/**
 * Transaction Sampler class to measure transaction times
 * (not exposed a a GUI class, as it is only used internally)
 */
public class TransactionSampler extends AbstractSampler {
    private static final long serialVersionUID = 240L;

    private boolean transactionDone = false;

    private TransactionController transactionController;

    private Sampler subSampler;

    private SampleResult transactionSampleResult;

    private int calls = 0;

    private int noFailingSamples = 0;

    private int totalTime = 0;

    /**
     * @deprecated only for use by test code
     */
    @Deprecated
    public TransactionSampler(){
        //log.warn("Constructor only intended for use in testing");
    }

    public TransactionSampler(TransactionController controller, String name) {
        transactionController = controller;
        setName(name); // ensure name is available for debugging
        transactionSampleResult = new SampleResult();
        transactionSampleResult.setSampleLabel(name);
        // Assume success
        transactionSampleResult.setSuccessful(true);
        transactionSampleResult.sampleStart();
    }

    /**
     * One cannot sample the TransactionSampler directly.
     */
    public SampleResult sample(Entry e) {
        throw new RuntimeException("Cannot sample TransactionSampler directly");
        // It is the JMeterThread which knows how to sample a real sampler
    }

    public Sampler getSubSampler() {
        return subSampler;
    }

    public SampleResult getTransactionResult() {
        return transactionSampleResult;
    }

    public TransactionController getTransactionController() {
        return transactionController;
    }

    public boolean isTransactionDone() {
        return transactionDone;
    }

    public void addSubSamplerResult(SampleResult res) {
        // Another subsample for the transaction
        calls++;
        // The transaction fails if any sub sample fails
        if (!res.isSuccessful()) {
            transactionSampleResult.setSuccessful(false);
            noFailingSamples++;
        }
        // Add the sub result to the transaction result
        transactionSampleResult.addSubResult(res);
        // Add current time to total for later use (exclude pause time)
        totalTime += res.getTime();
    }

    protected void setTransactionDone() {
        this.transactionDone = true;
        // Set the overall status for the transaction sample
        // TODO: improve, e.g. by adding counts to the SampleResult class
        transactionSampleResult.setResponseMessage("Number of samples in transaction : "
                        + calls + ", number of failing samples : "
                        + noFailingSamples);
        if (transactionSampleResult.isSuccessful()) {
            transactionSampleResult.setResponseCodeOK();
        }
        // Bug 50080 (not include pause time when generate parent)
        if (!transactionController.isIncludeTimers()) {
            long end = SampleResult.currentTimeInMs();
            transactionSampleResult.setIdleTime(end
                    - transactionSampleResult.getStartTime() - totalTime);
            transactionSampleResult.setEndTime(end);
        }
    }

    protected void setSubSampler(Sampler subSampler) {
        this.subSampler = subSampler;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16960.java