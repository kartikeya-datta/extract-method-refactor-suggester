error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4084.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4084.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4084.java
text:
```scala
c@@lient.connect(null); // TODO - FIXME

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.protocol.mgmt;

import java.io.DataInput;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.jboss.as.protocol.ProtocolChannelClient;
import org.jboss.as.protocol.mgmt.support.ChannelServer;
import org.jboss.as.protocol.old.ProtocolUtils;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.CloseHandler;
import org.jboss.remoting3.OpenListener;
import org.junit.Test;
import org.xnio.IoUtils;

/**
 * Not really a test, more a util to play with how to shut down tests
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class CloseChannels {


    @Test
    public void testChannelClose() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        ChannelServer.Configuration serverConfig = new ChannelServer.Configuration();
        serverConfig.setBindAddress(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 6999));
        serverConfig.setEndpointName("Test");
        serverConfig.setUriScheme("testing");
        serverConfig.setExecutor(executor);
        ChannelServer server = ChannelServer.create(serverConfig);
        server.addChannelOpenListener("channel", new OpenListener() {

            @Override
            public void registrationTerminated() {
            }

            @Override
            public void channelOpened(Channel channel) {
                System.out.println("Opened channel");
                final ManagementChannel protocolChannel = new ManagementChannelFactory(new ManagementOperationHandler() {
                    @Override
                    public ManagementRequestHandler getRequestHandler(byte id) {
                        return new ManagementRequestHandler() {
                            int i;
                            @Override
                            protected void readRequest(DataInput input) throws IOException {
                                System.out.println("Reading request");
                                ProtocolUtils.expectHeader(input, 11);
                                i = input.readInt();
                            }

                            @Override
                            protected void writeResponse(FlushableDataOutput output) throws IOException {
                                System.out.println("Writing response " + i);
                                output.write(22);
                                output.writeInt(i);
                            }

                        };
                    }
                }).create("channel", channel);
                protocolChannel.startReceiving();
                channel.addCloseHandler(new CloseHandler<Channel>() {
                    @Override
                    public void handleClose(Channel closed) {
                        System.out.println("server close handler!!!");
                    }
                });
            }
        });
        try {


            for (int i = 0 ; i < 1000 ; i++) {
                ProtocolChannelClient.Configuration<ManagementChannel> clientConfig = new ProtocolChannelClient.Configuration<ManagementChannel>();
                clientConfig.setEndpointName("Test");
                clientConfig.setExecutor(executor);
                clientConfig.setUri(new URI("testing://127.0.0.1:6999"));
                clientConfig.setUriScheme("testing");
                clientConfig.setChannelFactory(new ManagementChannelFactory());
                ProtocolChannelClient<ManagementChannel> client = ProtocolChannelClient.create(clientConfig);
                final int val = i;
                client.connect();
                System.out.println("Opening channel");
                final ManagementChannel clientChannel = client.openChannel("channel");
                clientChannel.addCloseHandler(new CloseHandler<Channel>() {

                    @Override
                    public void handleClose(Channel closed) {
                        System.out.println("client close handler");
                    }
                });
                clientChannel.startReceiving();
                try {
                    int result = new ManagementRequest<Integer>() {
                        @Override
                        protected byte getRequestCode() {
                            return 66; //Doesn't matter in this case
                        }

                        @Override
                        protected void writeRequest(int protocolVersion, FlushableDataOutput output) throws IOException {
                            System.out.println("Writing request");
                            output.write(11);
                            output.writeInt(val);
                        }

                        protected Integer readResponse(DataInput input) throws IOException {
                            System.out.println("Reading response");
                            ProtocolUtils.expectHeader(input, 22);
                            return input.readInt();
                        }

                    }.executeForResult(executor, ManagementClientChannelStrategy.create(clientChannel));
                    Assert.assertEquals(val, result);
                } finally {
                    IoUtils.safeClose(client);
                }
            }
        } finally {
            server.close();
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
            executor.shutdownNow();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4084.java