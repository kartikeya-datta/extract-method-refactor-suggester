error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1292.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1292.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1292.java
text:
```scala
S@@torageService.instance.initClient(0);

/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.apache.cassandra.locator;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.service.StorageService;
import org.junit.Test;

import org.apache.cassandra.utils.FBUtilities;

public class DynamicEndpointSnitchTest
{
    @Test
    public void testSnitch() throws InterruptedException, IOException, ConfigurationException
    {
        // do this because SS needs to be initialized before DES can work properly.
        StorageService.instance.initClient();
        int sleeptime = 150;
        DynamicEndpointSnitch dsnitch = new DynamicEndpointSnitch(new SimpleSnitch());
        InetAddress self = FBUtilities.getBroadcastAddress();
        ArrayList<InetAddress> order = new ArrayList<InetAddress>();
        InetAddress host1 = InetAddress.getByName("127.0.0.1");
        InetAddress host2 = InetAddress.getByName("127.0.0.2");
        InetAddress host3 = InetAddress.getByName("127.0.0.3");

        // first, make all hosts equal
        for (int i = 0; i < 5; i++)
        {
            dsnitch.receiveTiming(host1, 1.0);
            dsnitch.receiveTiming(host2, 1.0);
            dsnitch.receiveTiming(host3, 1.0);
        }

        Thread.sleep(sleeptime);

        order.add(host1);
        order.add(host2);
        order.add(host3);
        assert dsnitch.getSortedListByProximity(self, order).equals(order);

        // make host1 a little worse
        dsnitch.receiveTiming(host1, 2.0);
        Thread.sleep(sleeptime);

        order.clear();
        order.add(host2);
        order.add(host3);
        order.add(host1);
        assert dsnitch.getSortedListByProximity(self, order).equals(order);

        // make host2 as bad as host1
        dsnitch.receiveTiming(host2, 2.0);
        Thread.sleep(sleeptime);

        order.clear();
        order.add(host3);
        order.add(host1);
        order.add(host2);
        assert dsnitch.getSortedListByProximity(self, order).equals(order);

        // make host3 the worst
        for (int i = 0; i < 2; i++)
        {
            dsnitch.receiveTiming(host3, 2.0);
        }
        Thread.sleep(sleeptime);

        order.clear();
        order.add(host1);
        order.add(host2);
        order.add(host3);
        assert dsnitch.getSortedListByProximity(self, order).equals(order);

        // make host3 equal to the others
        for (int i = 0; i < 2; i++)
        {
            dsnitch.receiveTiming(host3, 1.0);
        }
        Thread.sleep(sleeptime);

        order.clear();
        order.add(host1);
        order.add(host2);
        order.add(host3);
        assert dsnitch.getSortedListByProximity(self, order).equals(order);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1292.java