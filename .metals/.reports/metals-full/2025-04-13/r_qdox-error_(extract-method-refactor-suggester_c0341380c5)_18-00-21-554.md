error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5104.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5104.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5104.java
text:
```scala
t@@erminate(threadPool);

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.elasticsearch.threadpool;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.util.concurrent.EsThreadPoolExecutor;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.elasticsearch.threadpool.ThreadPool.Names;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.hamcrest.Matchers.*;

/**
 */
public class UpdateThreadPoolSettingsTests extends ElasticsearchTestCase {

    private ThreadPool.Info info(ThreadPool threadPool, String name) {
        for (ThreadPool.Info info : threadPool.info()) {
            if (info.getName().equals(name)) {
                return info;
            }
        }
        return null;
    }

    @Test
    public void testCachedExecutorType() {
        ThreadPool threadPool = new ThreadPool(
                ImmutableSettings.settingsBuilder()
                        .put("threadpool.search.type", "cached")
                        .put("name","testCachedExecutorType").build(), null);

        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("cached"));
        assertThat(info(threadPool, Names.SEARCH).getKeepAlive().minutes(), equalTo(5L));
        assertThat(threadPool.executor(Names.SEARCH), instanceOf(EsThreadPoolExecutor.class));

        // Replace with different type
        threadPool.updateSettings(settingsBuilder().put("threadpool.search.type", "same").build());
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("same"));
        assertThat(threadPool.executor(Names.SEARCH), instanceOf(MoreExecutors.directExecutor().getClass()));

        // Replace with different type again
        threadPool.updateSettings(settingsBuilder()
                .put("threadpool.search.type", "scaling")
                .put("threadpool.search.keep_alive", "10m")
                .build());
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("scaling"));
        assertThat(threadPool.executor(Names.SEARCH), instanceOf(EsThreadPoolExecutor.class));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getCorePoolSize(), equalTo(1));
        // Make sure keep alive value changed
        assertThat(info(threadPool, Names.SEARCH).getKeepAlive().minutes(), equalTo(10L));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getKeepAliveTime(TimeUnit.MINUTES), equalTo(10L));

        // Put old type back
        threadPool.updateSettings(settingsBuilder().put("threadpool.search.type", "cached").build());
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("cached"));
        // Make sure keep alive value reused
        assertThat(info(threadPool, Names.SEARCH).getKeepAlive().minutes(), equalTo(10L));
        assertThat(threadPool.executor(Names.SEARCH), instanceOf(EsThreadPoolExecutor.class));

        // Change keep alive
        Executor oldExecutor = threadPool.executor(Names.SEARCH);
        threadPool.updateSettings(settingsBuilder().put("threadpool.search.keep_alive", "1m").build());
        // Make sure keep alive value changed
        assertThat(info(threadPool, Names.SEARCH).getKeepAlive().minutes(), equalTo(1L));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getKeepAliveTime(TimeUnit.MINUTES), equalTo(1L));
        // Make sure executor didn't change
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("cached"));
        assertThat(threadPool.executor(Names.SEARCH), sameInstance(oldExecutor));

        // Set the same keep alive
        threadPool.updateSettings(settingsBuilder().put("threadpool.search.keep_alive", "1m").build());
        // Make sure keep alive value didn't change
        assertThat(info(threadPool, Names.SEARCH).getKeepAlive().minutes(), equalTo(1L));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getKeepAliveTime(TimeUnit.MINUTES), equalTo(1L));
        // Make sure executor didn't change
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("cached"));
        assertThat(threadPool.executor(Names.SEARCH), sameInstance(oldExecutor));

        threadPool.shutdown();
    }

    @Test
    public void testFixedExecutorType() {
        ThreadPool threadPool = new ThreadPool(settingsBuilder()
                .put("threadpool.search.type", "fixed")
                .put("name","testCachedExecutorType").build(), null);

        assertThat(threadPool.executor(Names.SEARCH), instanceOf(EsThreadPoolExecutor.class));

        // Replace with different type
        threadPool.updateSettings(settingsBuilder()
                .put("threadpool.search.type", "scaling")
                .put("threadpool.search.keep_alive", "10m")
                .put("threadpool.search.min", "2")
                .put("threadpool.search.size", "15")
                .build());
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("scaling"));
        assertThat(threadPool.executor(Names.SEARCH), instanceOf(EsThreadPoolExecutor.class));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getCorePoolSize(), equalTo(2));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getMaximumPoolSize(), equalTo(15));
        assertThat(info(threadPool, Names.SEARCH).getMin(), equalTo(2));
        assertThat(info(threadPool, Names.SEARCH).getMax(), equalTo(15));
        // Make sure keep alive value changed
        assertThat(info(threadPool, Names.SEARCH).getKeepAlive().minutes(), equalTo(10L));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getKeepAliveTime(TimeUnit.MINUTES), equalTo(10L));

        // Put old type back
        threadPool.updateSettings(settingsBuilder()
                .put("threadpool.search.type", "fixed")
                .build());
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("fixed"));
        // Make sure keep alive value is not used
        assertThat(info(threadPool, Names.SEARCH).getKeepAlive(), nullValue());
        // Make sure keep pool size value were reused
        assertThat(info(threadPool, Names.SEARCH).getMin(), equalTo(15));
        assertThat(info(threadPool, Names.SEARCH).getMax(), equalTo(15));
        assertThat(threadPool.executor(Names.SEARCH), instanceOf(EsThreadPoolExecutor.class));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getCorePoolSize(), equalTo(15));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getMaximumPoolSize(), equalTo(15));

        // Change size
        Executor oldExecutor = threadPool.executor(Names.SEARCH);
        threadPool.updateSettings(settingsBuilder().put("threadpool.search.size", "10").build());
        // Make sure size values changed
        assertThat(info(threadPool, Names.SEARCH).getMax(), equalTo(10));
        assertThat(info(threadPool, Names.SEARCH).getMin(), equalTo(10));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getMaximumPoolSize(), equalTo(10));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getCorePoolSize(), equalTo(10));
        // Make sure executor didn't change
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("fixed"));
        assertThat(threadPool.executor(Names.SEARCH), sameInstance(oldExecutor));

        // Change queue capacity
        threadPool.updateSettings(settingsBuilder()
                .put("threadpool.search.queue", "500")
                .build());

        threadPool.shutdown();
    }


    @Test
    public void testScalingExecutorType() {
        ThreadPool threadPool = new ThreadPool(settingsBuilder()
                .put("threadpool.search.type", "scaling")
                .put("threadpool.search.size", 10)
                .put("name","testCachedExecutorType").build(), null);

        assertThat(info(threadPool, Names.SEARCH).getMin(), equalTo(1));
        assertThat(info(threadPool, Names.SEARCH).getMax(), equalTo(10));
        assertThat(info(threadPool, Names.SEARCH).getKeepAlive().minutes(), equalTo(5L));
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("scaling"));
        assertThat(threadPool.executor(Names.SEARCH), instanceOf(EsThreadPoolExecutor.class));

        // Change settings that doesn't require pool replacement
        Executor oldExecutor = threadPool.executor(Names.SEARCH);
        threadPool.updateSettings(settingsBuilder()
                .put("threadpool.search.type", "scaling")
                .put("threadpool.search.keep_alive", "10m")
                .put("threadpool.search.min", "2")
                .put("threadpool.search.size", "15")
                .build());
        assertThat(info(threadPool, Names.SEARCH).getType(), equalTo("scaling"));
        assertThat(threadPool.executor(Names.SEARCH), instanceOf(EsThreadPoolExecutor.class));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getCorePoolSize(), equalTo(2));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getMaximumPoolSize(), equalTo(15));
        assertThat(info(threadPool, Names.SEARCH).getMin(), equalTo(2));
        assertThat(info(threadPool, Names.SEARCH).getMax(), equalTo(15));
        // Make sure keep alive value changed
        assertThat(info(threadPool, Names.SEARCH).getKeepAlive().minutes(), equalTo(10L));
        assertThat(((EsThreadPoolExecutor) threadPool.executor(Names.SEARCH)).getKeepAliveTime(TimeUnit.MINUTES), equalTo(10L));
        assertThat(threadPool.executor(Names.SEARCH), sameInstance(oldExecutor));

        threadPool.shutdown();
    }

    @Test(timeout = 10000)
    public void testShutdownDownNowDoesntBlock() throws Exception {
        ThreadPool threadPool = new ThreadPool(ImmutableSettings.settingsBuilder()
                .put("threadpool.search.type", "cached")
                .put("name","testCachedExecutorType").build(), null);

        final CountDownLatch latch = new CountDownLatch(1);
        Executor oldExecutor = threadPool.executor(Names.SEARCH);
        threadPool.executor(Names.SEARCH).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException ex) {
                    latch.countDown();
                    Thread.currentThread().interrupt();
                }
            }
        });
        threadPool.updateSettings(settingsBuilder().put("threadpool.search.type", "fixed").build());
        assertThat(threadPool.executor(Names.SEARCH), not(sameInstance(oldExecutor)));
        assertThat(((ThreadPoolExecutor) oldExecutor).isShutdown(), equalTo(true));
        assertThat(((ThreadPoolExecutor) oldExecutor).isTerminating(), equalTo(true));
        assertThat(((ThreadPoolExecutor) oldExecutor).isTerminated(), equalTo(false));
        threadPool.shutdownNow();
        latch.await();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5104.java