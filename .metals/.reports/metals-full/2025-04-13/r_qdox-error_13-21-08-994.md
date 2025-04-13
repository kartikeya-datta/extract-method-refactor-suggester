error id: <WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1074.java
<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1074.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1074.java
text:
```scala
M@@odelNode result = client.execute(new NewOperationBuilder(update).build());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.test.embedded.demos.client.messaging;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.resource.spi.IllegalStateException;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSession.QueueQuery;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.controller.client.NewModelControllerClient;
import org.jboss.as.controller.client.NewOperationBuilder;
import org.jboss.as.test.modular.utils.ShrinkWrapUtils;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Demo using the AS management API to create and destroy a HornetQ core queue.
 *
 * @author Emanuel Muckenhuber
 * @author Kabir Khan
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MessagingClientTestCase {

    @Deployment(testable = false)
    public static Archive<?> getEmptyDeployment() {
        return ShrinkWrapUtils.createEmptyJavaArchive("messaging-client.jar");
    }

    @Test
    public void testMessagingClient() throws Exception {

        final String queueName = "queue.standalone";

        final ClientSessionFactory sf = createClientSessionFactory("localhost", 5445);
        final NewModelControllerClient client = NewModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999);

        try {
            // Check that the queue does not exists
            if(queueExists(queueName, sf)) {
                throw new IllegalStateException();
            }

            // Create a new core queue using the standalone client
            ModelNode op = new ModelNode();
            op.get("operation").set("add");
            op.get("address").add("subsystem", "messaging");
            op.get("address").add("queue", queueName);
            op.get("queue-address").set(queueName);
            applyUpdate(op, client);
            // Check if the queue exists
            if(! queueExists(queueName, sf)) {
                throw new IllegalStateException();
            }

            ClientSession session = null;
            try {
               session = sf.createSession();
               ClientProducer producer = session.createProducer(queueName);
               ClientMessage message = session.createMessage(false);

               final String propName = "myprop";
               message.putStringProperty(propName, "Hello sent at " + new Date());

               producer.send(message);

               ClientConsumer messageConsumer = session.createConsumer(queueName);
               session.start();

               ClientMessage messageReceived = messageConsumer.receive(1000);
            } finally {
               if (session != null) {
                  session.close();
               }
            }

            op = new ModelNode();
            op.get("operation").set("remove");
            op.get("address").add("subsystem", "messaging");
            op.get("address").add("queue", queueName);
            applyUpdate(op, client);

            // Check that the queue does not exists
//  FIXME - JBAS-9360
//            if(queueExists(queueName, sf)) {
//                throw new IllegalStateException();
//            }
        } finally {
            client.close();
        }
    }

    static void applyUpdate(ModelNode update, final NewModelControllerClient client) throws IOException {
        ModelNode result = client.execute(NewOperationBuilder.Factory.create(update).build());
        if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString())) {
            if (result.hasDefined("result")) {
                System.out.println(result.get("result"));
            }
        }
        else if (result.hasDefined("failure-description")){
            throw new RuntimeException(result.get("failure-description").toString());
        }
        else {
            throw new RuntimeException("Operation not successful; outcome = " + result.get("outcome"));
        }
    }

    static boolean queueExists(final String queueName, final ClientSessionFactory sf) throws HornetQException {
        final ClientSession session = sf.createSession(false, false, false);
        try {
            final QueueQuery query = session.queueQuery(new SimpleString(queueName));
            return query.isExists();
        } finally {
            session.close();
        }
    }

    static ClientSessionFactory createClientSessionFactory(String host, int port) throws Exception {
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("host", host);
        properties.put("port", port);
        final TransportConfiguration configuration = new TransportConfiguration(NettyConnectorFactory.class.getName(), properties);
        return HornetQClient.createServerLocatorWithoutHA(configuration).createSessionFactory();
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
	scala.meta.internal.mtags.MtagsIndexer.index(MtagsIndexer.scala:21)
	scala.meta.internal.mtags.MtagsIndexer.index$(MtagsIndexer.scala:20)
	scala.meta.internal.mtags.JavaMtags.index(JavaMtags.scala:38)
	scala.meta.internal.tvp.IndexedSymbols.javaSymbols(IndexedSymbols.scala:111)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbolsFromPath(IndexedSymbols.scala:120)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$2(IndexedSymbols.scala:146)
	scala.collection.concurrent.TrieMap.getOrElseUpdate(TrieMap.scala:960)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$1(IndexedSymbols.scala:146)
	scala.meta.internal.tvp.IndexedSymbols.withTimer(IndexedSymbols.scala:71)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbols(IndexedSymbols.scala:143)
	scala.meta.internal.tvp.FolderTreeViewProvider.$anonfun$projects$9(MetalsTreeViewProvider.scala:306)
	scala.collection.Iterator$$anon$9.next(Iterator.scala:584)
	scala.collection.Iterator$$anon$10.nextCur(Iterator.scala:594)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:608)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:477)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:601)
	scala.collection.Iterator$$anon$8.hasNext(Iterator.scala:562)
	scala.collection.immutable.List.prependedAll(List.scala:155)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.SeqFactory$Delegate.from(Factory.scala:306)
	scala.collection.immutable.Seq$.from(Seq.scala:42)
	scala.collection.IterableOnceOps.toSeq(IterableOnce.scala:1473)
	scala.collection.IterableOnceOps.toSeq$(IterableOnce.scala:1473)
	scala.collection.AbstractIterator.toSeq(Iterator.scala:1306)
	scala.meta.internal.tvp.ClasspathTreeView.children(ClasspathTreeView.scala:62)
	scala.meta.internal.tvp.FolderTreeViewProvider.getProjectRoot(MetalsTreeViewProvider.scala:390)
	scala.meta.internal.tvp.MetalsTreeViewProvider.$anonfun$children$1(MetalsTreeViewProvider.scala:84)
	scala.collection.immutable.List.map(List.scala:247)
	scala.meta.internal.tvp.MetalsTreeViewProvider.children(MetalsTreeViewProvider.scala:84)
	scala.meta.internal.metals.WorkspaceLspService.$anonfun$treeViewChildren$1(WorkspaceLspService.scala:705)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:687)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:467)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

QDox parse error in <WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1074.java