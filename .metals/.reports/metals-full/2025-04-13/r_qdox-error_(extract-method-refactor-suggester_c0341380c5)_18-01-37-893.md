error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15299.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15299.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15299.java
text:
```scala
i@@f (args.length == 1) {

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
 */
package org.apache.jmeter.monitor.util;

import java.util.List;
import java.util.LinkedList;
import org.apache.jmeter.monitor.model.*;
import org.apache.jmeter.visualizers.*;

/**
 *
 * @version $Revision$
 */
public class MemoryBenchmark {

    public static void main(String[] args) {
        if (args.length != 1) {
            int objects = 10000;
            if (args[0] != null) {
                objects = Integer.parseInt(args[0]);
            }
            List objs = new LinkedList();
            ObjectFactory of = ObjectFactory.getInstance();

            long bfree = Runtime.getRuntime().freeMemory();
            long btotal = Runtime.getRuntime().totalMemory();
            long bmax = Runtime.getRuntime().maxMemory();
            System.out.println("Before we create objects:");
            System.out.println("------------------------------");
            System.out.println("free: " + bfree);
            System.out.println("total: " + btotal);
            System.out.println("max: " + bmax);

            for (int idx = 0; idx < objects; idx++) {
                Connector cnn = of.createConnector();
                Workers wkrs = of.createWorkers();
                for (int idz = 0; idz < 26; idz++) {
                    Worker wk0 = of.createWorker();
                    wk0.setCurrentQueryString("/manager/status");
                    wk0.setCurrentUri("http://localhost/manager/status");
                    wk0.setMethod("GET");
                    wk0.setProtocol("http");
                    wk0.setRemoteAddr("?");
                    wk0.setRequestBytesReceived(132);
                    wk0.setRequestBytesSent(18532);
                    wk0.setStage("K");
                    wk0.setVirtualHost("?");
                    wkrs.getWorker().add(wk0);
                }
                cnn.setWorkers(wkrs);

                RequestInfo rqinfo = of.createRequestInfo();
                rqinfo.setBytesReceived(0);
                rqinfo.setBytesSent(434374);
                rqinfo.setErrorCount(10);
                rqinfo.setMaxTime(850);
                rqinfo.setProcessingTime(2634);
                rqinfo.setRequestCount(1002);
                cnn.setRequestInfo(rqinfo);

                ThreadInfo thinfo = of.createThreadInfo();
                thinfo.setCurrentThreadCount(50);
                thinfo.setCurrentThreadsBusy(12);
                thinfo.setMaxSpareThreads(50);
                thinfo.setMaxThreads(150);
                thinfo.setMinSpareThreads(10);
                cnn.setThreadInfo(thinfo);

                Jvm vm = of.createJvm();
                Memory mem = of.createMemory();
                mem.setFree(77280);
                mem.setTotal(134210000);
                mem.setMax(134217728);
                vm.setMemory(mem);

                Status st = of.createStatus();
                st.setJvm(vm);
                st.getConnector().add(cnn);

                MonitorStats mstats = new MonitorStats(Stats.calculateStatus(st), Stats.calculateLoad(st), 0, Stats
                        .calculateMemoryLoad(st), Stats.calculateThreadLoad(st), "localhost", "8080", "http", System
                        .currentTimeMillis());
                MonitorModel monmodel = new MonitorModel(mstats);
                objs.add(monmodel);
            }
            long afree = Runtime.getRuntime().freeMemory();
            long atotal = Runtime.getRuntime().totalMemory();
            long amax = Runtime.getRuntime().maxMemory();
            long delta = ((atotal - afree) - (btotal - bfree));
            System.out.println("After we create objects:");
            System.out.println("------------------------------");
            System.out.println("free: " + afree);
            System.out.println("total: " + atotal);
            System.out.println("max: " + amax);
            System.out.println("------------------------------");
            System.out.println("delta: " + (delta / 1024) + " kilobytes");
            System.out.println("delta: " + (delta / 1024 / 1024) + " megabytes");
            System.out.println("number of objects: " + objects);
            System.out.println("potential number of servers: " + (objects / 1000));

        } else {
            System.out.println("Please provide the number of objects");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15299.java