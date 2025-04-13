error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1635.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1635.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1635.java
text:
```scala
s@@elector.select(1);

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.net;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.apache.log4j.Logger;

public class SelectorManager extends Thread
{
    private static final Logger logger = Logger.getLogger(SelectorManager.class); 

    // the underlying selector used
    protected Selector selector;

    // workaround JDK select/register bug
    Object gate = new Object();

    // The static selector manager which is used by all applications
    private static SelectorManager manager;
    
    // The static UDP selector manager which is used by all applications
    private static SelectorManager udpManager;

    private SelectorManager(String name)
    {
        super(name);

        try
        {
            selector = Selector.open();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        setDaemon(false);
    }

    /**
     * Registers a new channel with the selector, and attaches the given
     * SelectionKeyHandler as the handler for the newly created key. Operations
     * which the hanlder is interested in will be called as available.
     * 
     * @param channel
     *            The channel to regster with the selector
     * @param handler
     *            The handler to use for the callbacks
     * @param ops
     *            The initial interest operations
     * @return The SelectionKey which uniquely identifies this channel
     * @exception IOException if the channel is closed
     */
    public SelectionKey register(SelectableChannel channel,
            SelectionKeyHandler handler, int ops) throws IOException
    {
        assert channel != null;
        assert handler != null;

        synchronized(gate)
        {
            selector.wakeup();
            return channel.register(selector, ops, handler);
        }
    }      

    /**
     * This method starts the socket manager listening for events. It is
     * designed to be started when this thread's start() method is invoked.
     */
    public void run()
    {
        while (true)
        {
            try
            {
                selector.select(100);
                doProcess();
                synchronized(gate) {}
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    protected void doProcess() throws IOException
    {
        SelectionKey[] keys = selector.selectedKeys().toArray(new SelectionKey[0]);

        for (SelectionKey key : keys)
        {
            selector.selectedKeys().remove(key);

            synchronized (key)
            {
                SelectionKeyHandler skh = (SelectionKeyHandler) key.attachment();

                if (skh != null)
                {
                    // accept
                    if (key.isValid() && key.isAcceptable())
                    {
                        skh.accept(key);
                    }

                    // connect
                    if (key.isValid() && key.isConnectable())
                    {
                        skh.connect(key);
                    }

                    // read
                    if (key.isValid() && key.isReadable())
                    {
                        skh.read(key);
                    }

                    // write
                    if (key.isValid() && key.isWritable())
                    {
                        skh.write(key);
                    }
                }
            }
        }
    }

    /**
     * Returns the SelectorManager applications should use.
     * 
     * @return The SelectorManager which applications should use
     */
    public static SelectorManager getSelectorManager()
    {
        synchronized (SelectorManager.class)
        {
            if (manager == null)
            {
                manager = new SelectorManager("TCP Selector Manager");
            }            
        }
        return manager;
    }
    
    public static SelectorManager getUdpSelectorManager()
    {
        synchronized (SelectorManager.class)
        {
            if (udpManager == null)
            {
                udpManager = new SelectorManager("UDP Selector Manager");
            }            
        }
        return udpManager;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1635.java