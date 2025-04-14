error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12206.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12206.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12206.java
text:
```scala
.@@addDependency(Services.JBOSS_SERVER_CONTROLLER, ServerController.class, client.getServerControllerInjector())

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

package org.jboss.as.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;

import org.jboss.as.protocol.Connection;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.server.mgmt.domain.HostControllerClient;
import org.jboss.as.server.mgmt.domain.HostControllerConnectionService;
import org.jboss.as.server.mgmt.domain.NewHostControllerServerClient;
import org.jboss.logmanager.Level;
import org.jboss.logmanager.log4j.BridgeRepositorySelector;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.SimpleClassResolver;
import org.jboss.marshalling.Unmarshaller;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.stdio.LoggingOutputStream;
import org.jboss.stdio.NullInputStream;
import org.jboss.stdio.SimpleStdioContextSelector;
import org.jboss.stdio.StdioContext;

/**
 * The main entry point for domain-managed server instances.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class DomainServerMain {

    private DomainServerMain() {
    }

    /**
     * Main entry point.  Reads and executes the command object from standard input.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        // TODO: privileged block
        System.setProperty("log4j.defaultInitOverride", "true");
        new BridgeRepositorySelector().start();

        final InputStream initialInput = System.in;
        final PrintStream initialError = System.err;

        // Install JBoss Stdio to avoid any nasty crosstalk.
        StdioContext.install();
        final StdioContext context = StdioContext.create(
            new NullInputStream(),
            new LoggingOutputStream(org.jboss.logmanager.Logger.getLogger("stdout"), Level.INFO),
            new LoggingOutputStream(org.jboss.logmanager.Logger.getLogger("stderr"), Level.ERROR)
        );
        StdioContext.setStdioContextSelector(new SimpleStdioContextSelector(context));

        final byte[] authKey = new byte[16];
        try {
            StreamUtils.readFully(initialInput, authKey);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            throw new IllegalStateException(); // not reached
        }

        final MarshallerFactory factory = Marshalling.getMarshallerFactory("river", DomainServerMain.class.getClassLoader());
        final Unmarshaller unmarshaller;
        final ByteInput byteInput;
        try {
            final MarshallingConfiguration configuration = new MarshallingConfiguration();
            configuration.setVersion(2);
            configuration.setClassResolver(new SimpleClassResolver(DomainServerMain.class.getClassLoader()));
            unmarshaller = factory.createUnmarshaller(configuration);
            byteInput = Marshalling.createByteInput(initialInput);
            unmarshaller.start(byteInput);
            final ServerTask task = unmarshaller.readObject(ServerTask.class);
            unmarshaller.finish();
            task.run(Arrays.<ServiceActivator>asList(new ServiceActivator() {
                public void activate(final ServiceActivatorContext serviceActivatorContext) {
                    // TODO activate host controller client service
                }
            }));
        } catch (Exception e) {
            e.printStackTrace(initialError);
            System.exit(1);
            throw new IllegalStateException(); // not reached
        }
        for (;;) try {
            unmarshaller.start(byteInput);
            final InetSocketAddress socketAddress = unmarshaller.readObject(InetSocketAddress.class);
            unmarshaller.finish();
            // todo connect to the HC at socketAddress, disconnect from old
            break;
        } catch (InterruptedIOException e) {
            Thread.interrupted();
            // ignore
        } catch (EOFException e) {
            // this means it's time to exit
            break;
        } catch (Exception e) {
            break;
        }
        // Once the input stream is cut off, shut down
        System.exit(0);
        throw new IllegalStateException(); // not reached
    }

    public static final class HostControllerCommunicationActivator implements ServiceActivator, Serializable {
        private static final long serialVersionUID = 6671220116719309952L;
        private final String serverName;
        private final InetSocketAddress managementSocket;

        public HostControllerCommunicationActivator(final String serverName, final InetSocketAddress managementSocket) {
            this.serverName = serverName;
            this.managementSocket = managementSocket;
        }

        public void activate(final ServiceActivatorContext serviceActivatorContext) {
            final ServiceTarget serviceTarget = serviceActivatorContext.getServiceTarget();

            final HostControllerConnectionService smConnection = new HostControllerConnectionService();
            serviceTarget.addService(HostControllerConnectionService.SERVICE_NAME, smConnection)
                .addInjection(smConnection.getSmAddressInjector(), managementSocket)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();

            final NewHostControllerServerClient client = new NewHostControllerServerClient(serverName);
            serviceTarget.addService(HostControllerClient.SERVICE_NAME, client)
                .addDependency(HostControllerConnectionService.SERVICE_NAME, Connection.class, client.getSmConnectionInjector())
                .addDependency(Services.JBOSS_SERVER_CONTROLLER, NewServerController.class, client.getServerControllerInjector())
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12206.java