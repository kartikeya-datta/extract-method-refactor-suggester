error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1703.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1703.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1703.java
text:
```scala
M@@ARSHALLER_FACTORY = Marshalling.getMarshallerFactory("river", ModuleClassLoader.forModuleName("org.jboss.marshalling.river"));

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

package org.jboss.as.domain.controller;

import org.jboss.as.model.Host;
import org.jboss.logging.Logger;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.ModularClassTable;
import org.jboss.marshalling.Unmarshaller;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.modules.ModuleLoadException;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Protocol used by the domain controller to communicate with server manager instances.
 *
 * @author John E. Bailey
 */
public class ServerManagerConnectionProtocol {
    private static final Logger log = Logger.getLogger("org.jboss.as.domain.controller");
    private static final MarshallerFactory MARSHALLER_FACTORY;
    private static final MarshallingConfiguration CONFIG;

    static {
        try {
            MARSHALLER_FACTORY = Marshalling.getMarshallerFactory("river", ModuleClassLoader.forModuleName("org.jboss.marshalling.jboss-marshalling-river"));
        } catch (ModuleLoadException e) {
            throw new RuntimeException(e);
        }
        final MarshallingConfiguration config = new MarshallingConfiguration();
        config.setClassTable(ModularClassTable.getInstance());
        CONFIG = config;
    }

    /**
     * Outgoing commands to be sent to the server manager.
     */
    enum OutgoingCommand {
        CONFIRM_REGISTRATION((byte) 0) {
            void doExecute(ServerManagerConnection serverManagerConnection, Marshaller marshaller) throws Exception {
            }},
        UPDATE_DOMAIN((byte) 1) {
            void doExecute(final ServerManagerConnection serverManagerConnection, final Marshaller marshaller) throws Exception {
                marshaller.writeObject(serverManagerConnection.getDomainController().getDomain());
            }
        },;

        final Byte command;

        OutgoingCommand(Byte command) {
            this.command = command;
        }

        void execute(final ServerManagerConnection serverManagerConnection, final OutputStream outputStream) throws Exception {
            Marshaller marshaller = null;
            try {
                marshaller = MARSHALLER_FACTORY.createMarshaller(CONFIG);
                marshaller.start(Marshalling.createByteOutput(outputStream));
                marshaller.writeByte(command);
                doExecute(serverManagerConnection, marshaller);
                marshaller.finish();
            } finally {
                safeClose(marshaller);
            }
        }

        abstract void doExecute(final ServerManagerConnection serverManagerConnection, final Marshaller marshaller) throws Exception;
    }

    /**
     * Incoming commands from the server manager.
     */
    enum IncomingCommand {
        REGISTER((byte) 0) {
            protected void execute(final ServerManagerConnection serverManagerConnection, final Unmarshaller unmarshaller) throws Exception {
                log.infof("Server manager registered [%s]", serverManagerConnection.getId());
                final Host hostConfig = unmarshaller.readObject(Host.class);
                serverManagerConnection.setHostConfig(hostConfig);
                serverManagerConnection.confirmRegistration();
                serverManagerConnection.updateDomain();
            }
        },
        UNREGISTER(Byte.MAX_VALUE) {
            protected void execute(final ServerManagerConnection serverManagerConnection, final Unmarshaller unmarshaller) throws Exception {
                serverManagerConnection.unregistered();
                log.infof("Server manager unregistered [%s]", serverManagerConnection.getId());

            }
        },;

        final Byte command;

        IncomingCommand(Byte command) {
            this.command = command;
        }

        static void processNext(final ServerManagerConnection serverManagerConnection, final InputStream inputStream) throws Exception {
            Unmarshaller unmarshaller = null;
            try {
                unmarshaller = MARSHALLER_FACTORY.createUnmarshaller(CONFIG);
                unmarshaller.start(Marshalling.createByteInput(inputStream));
                final byte commandByte = unmarshaller.readByte();
                final IncomingCommand command = IncomingCommand.commandFor(commandByte);
                if (command == null) {
                    throw new RuntimeException("Invalid command byte received: " + commandByte);
                }
                command.execute(serverManagerConnection, unmarshaller); // TODO: How to handle failed commands?
                unmarshaller.finish();
            } finally {
                safeClose(unmarshaller);
            }
        }

        protected abstract void execute(final ServerManagerConnection serverManagerConnection, final Unmarshaller unmarshaller) throws Exception;

        static Map<Byte, IncomingCommand> COMMANDS = new HashMap<Byte, IncomingCommand>();

        static {
            for (IncomingCommand command : values()) {
                COMMANDS.put(command.command, command);
            }
        }

        static IncomingCommand commandFor(final byte commandByte) {
            return COMMANDS.get(Byte.valueOf(commandByte));
        }
    }

    private static void safeClose(final Closeable closeable) {
        if (closeable != null) try {
            closeable.close();
        } catch (Throwable ignored) {
            // todo: log me
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1703.java