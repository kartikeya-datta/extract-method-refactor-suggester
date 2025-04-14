error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3898.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3898.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3898.java
text:
```scala
L@@OGGER.warn("IOException occured", e);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.as.test.integration.logging.syslogserver;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;

/**
 * Socket handler for TCP and TLS syslog server implementations. It handles automatically Octet Counting/Non-Transparent-Framing
 * switch.
 * 
 * @author Josef Cacek
 */
public class TCPSyslogSocketHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(TCPSyslogSocketHandler.class);

    protected SyslogServerIF server = null;
    protected Socket socket = null;
    protected Set<Socket> sockets = null;

    /**
     * Constructor.
     * 
     * @param sockets Set of all registered handlers.
     * @param server Syslog server instance
     * @param socket socket returned from the serverSocket.accept()
     */
    public TCPSyslogSocketHandler(Set<Socket> sockets, SyslogServerIF server, Socket socket) {
        this.sockets = sockets;
        this.server = server;
        this.socket = socket;

        synchronized (this.sockets) {
            this.sockets.add(this.socket);
        }
    }

    public void run() {
        try {
            final BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            int b = -1;
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            boolean firstByte = true;
            boolean octetCounting = false;
            StringBuilder octetLenStr = new StringBuilder();
            do {
                b = bis.read();
                if (firstByte && b >= '1' && b <= '9') {
                    // handle Octet Counting messages (cf. rfc-6587)
                    octetCounting = true;
                }
                firstByte = false;
                if (octetCounting) {
                    if (b != ' ') {
                        octetLenStr.append((char) b);
                    } else {
                        int len = Integer.parseInt(octetLenStr.toString());
                        handleSyslogMessage(IOUtils.toByteArray(bis, len));
                        // reset the stuff
                        octetLenStr = new StringBuilder();
                        firstByte = true;
                        octetCounting = false;
                    }
                } else {
                    // handle Non-Transparent-Framing messages (cf. rfc-6587)
                    switch (b) {
                        case -1:
                        case '\r':
                        case '\n':
                            if (baos.size() > 0) {
                                handleSyslogMessage(baos.toByteArray());
                                baos.reset();
                                firstByte = true;
                            }
                            break;
                        default:
                            baos.write(b);
                            break;
                    }
                }
            } while (b != -1);
        } catch (IOException e) {
            LOGGER.warn("IOException occurred", e);
        } finally {
            IOUtils.closeQuietly(socket);
            sockets.remove(socket);
        }
    }

    /**
     * Parses {@link Rfc5424SyslogEvent} instance from given raw message bytes and sends it to event handlers.
     * 
     * @param rawMsg
     */
    private void handleSyslogMessage(final byte[] rawMsg) {
        final SyslogServerEventIF event = new Rfc5424SyslogEvent(rawMsg, 0, rawMsg.length);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Firing Syslog event: " + event);
        }
        final List eventHandlers = this.server.getConfig().getEventHandlers();
        for (int i = 0; i < eventHandlers.size(); i++) {
            final SyslogServerEventHandlerIF eventHandler = (SyslogServerEventHandlerIF) eventHandlers.get(i);
            eventHandler.event(this.server, event);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3898.java