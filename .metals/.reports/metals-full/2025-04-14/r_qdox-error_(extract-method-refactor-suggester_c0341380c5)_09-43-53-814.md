error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4881.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4881.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4881.java
text:
```scala
private static final i@@nt capacity = JMeterUtils.getPropDefault("asynch.batch.queue.size", 100); // $NON-NLS-1$

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

package org.apache.jmeter.samplers;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterError;
import org.apache.log.Logger;

public class AsynchSampleSender implements SampleSender, Serializable {

    private static final long serialVersionUID = 251L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    // Create unique object as marker for end of queue
    private transient static final SampleEvent FINAL_EVENT = new SampleEvent();
    
    private static final int capacity = JMeterUtils.getPropDefault("asynch.batch.queue.size", 20); // $NON-NLS-1$

    // created by client 
    private final RemoteSampleListener listener;

    static {
        log.info("Using Asynch Remote Sampler for this test run, queue size "+capacity);        
    }

    private transient BlockingQueue<SampleEvent> queue; // created by server in readResolve method
    
    private transient long queueWaits; // how many times we had to wait to queue a sample
    
    private transient long queueWaitTime; // how long we had to wait (nanoSeconds)

    /**
     * Processed by the RMI server code.
     * @throws ObjectStreamException  
     */
    private Object readResolve() throws ObjectStreamException{
        log.debug("Using batch queue size (asynch.batch.queue.size): " + capacity);
        queue = new ArrayBlockingQueue<SampleEvent>(capacity);        
        Worker worker = new Worker(queue, listener);
        worker.setDaemon(true);
        worker.start();
        return this;
    }

    /**
     * @deprecated only for use by test code
     */
    @Deprecated
    public AsynchSampleSender(){
        log.warn("Constructor only intended for use in testing"); // $NON-NLS-1$
        listener = null;
    }

    // Created by SampleSenderFactory
    protected AsynchSampleSender(RemoteSampleListener listener) {
        this.listener = listener;
    }

    public void testEnded() { // probably not used in server mode
        log.debug("Test ended()");
        try {
            listener.testEnded();
        } catch (RemoteException ex) {
            log.warn("testEnded()"+ex);
        }
    }

    public void testEnded(String host) {
        log.debug("Test Ended on " + host);
        try {
            listener.testEnded(host);
            queue.put(FINAL_EVENT);
        } catch (Exception ex) {
            log.warn("testEnded(host)"+ex);
        }
        if (queueWaits > 0) {
            log.info("QueueWaits: "+queueWaits+"; QueueWaitTime: "+queueWaitTime+" (nanoseconds)");            
        }
    }

    public void sampleOccurred(SampleEvent e) {
        try {
            if (!queue.offer(e)){ // we failed to add the element first time
                queueWaits++;
                long t1 = System.nanoTime();
                queue.put(e);
                long t2 = System.nanoTime();
                queueWaitTime += t2-t1;
            }
        } catch (Exception err) {
            log.error("sampleOccurred; failed to queue the sample", err);
        }
    }

    private static class Worker extends Thread {
        
        private final BlockingQueue<SampleEvent> queue;
        
        private final RemoteSampleListener listener;
        
        private Worker(BlockingQueue<SampleEvent> q, RemoteSampleListener l){
            queue = q;
            listener = l;
        }

        @Override
        public void run() {
            try {
                boolean eof = false;
                while (!eof) {
                    List<SampleEvent> l = new ArrayList<SampleEvent>();
                    SampleEvent e = queue.take();
                    while (!(eof = (e == FINAL_EVENT)) && e != null) { // try to process as many as possible
                        l.add(e);
                        e = queue.poll(); // returns null if nothing on queue currently
                    }
                    int size = l.size();
                    if (size > 0) {
                        try {
                            listener.processBatch(l);
                        } catch (RemoteException err) {
                            if (err.getCause() instanceof java.net.ConnectException){
                                throw new JMeterError("Could not return sample",err);
                            }
                            log.error("Failed to return sample", err);
                        }
                    }
                }
            } catch (InterruptedException e) {
            }
            log.debug("Worker ended");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4881.java