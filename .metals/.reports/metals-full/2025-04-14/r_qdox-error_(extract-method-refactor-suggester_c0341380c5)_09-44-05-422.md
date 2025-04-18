error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5856.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5856.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5856.java
text:
```scala
c@@mdCtx.connectController("http-remoting", "localhost", 9990);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.cli.gui;

import com.sun.tools.jconsole.JConsoleContext;
import com.sun.tools.jconsole.JConsolePlugin;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.MBeanServerConnection;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.impl.ExistingChannelModelControllerClient;
import org.wildfly.security.manager.GetAccessControlContextAction;
import org.jboss.dmr.ModelNode;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.Connection;
import org.jboss.remotingjmx.RemotingMBeanServerConnection;
import org.jboss.threads.JBossThreadFactory;
import org.xnio.IoFuture;
import org.xnio.OptionMap;

import static java.security.AccessController.doPrivileged;

/**
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2012 Red Hat Inc.
 */
public class JConsoleCLIPlugin extends JConsolePlugin {

    private static final int DEFAULT_MAX_THREADS = 6;

    // Global count of created pools
    private static final AtomicInteger executorCount = new AtomicInteger();

    CliGuiContext cliGuiCtx;
    private JPanel jconsolePanel;
    private boolean initComplete = false;
    private boolean isConnected = false;

    @Override
    public Map<String,JPanel> getTabs() {
        Map<String, JPanel> panelMap = new HashMap<String, JPanel>();

        final CommandContext cmdCtx;
        try {
            cmdCtx = CommandContextFactory.getInstance().newCommandContext();
            isConnected = connectCommandContext(cmdCtx);
            if (!isConnected) return panelMap;
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to JBoss AS.", e);
        }

        cliGuiCtx = GuiMain.startEmbedded(cmdCtx);
        JPanel cliGuiPanel = cliGuiCtx.getMainPanel();

        jconsolePanel = new JPanel(new BorderLayout());
        jconsolePanel.add(GuiMain.makeMenuBar(cliGuiCtx), BorderLayout.NORTH);
        jconsolePanel.add(cliGuiPanel, BorderLayout.CENTER);

        panelMap.put(getJBossServerName(), jconsolePanel);
        return panelMap;
    }

    private boolean connectCommandContext(CommandContext cmdCtx) throws Exception {
        JConsoleContext jcCtx = this.getContext();
        MBeanServerConnection mbeanServerConn = jcCtx.getMBeanServerConnection();

        if (mbeanServerConn instanceof RemotingMBeanServerConnection) {
            return connectUsingRemoting(cmdCtx, (RemotingMBeanServerConnection)mbeanServerConn);
        } else {
            try {
                cmdCtx.connectController("localhost", 9999);
            } catch (Exception e) {
                String message = "CLI GUI unable to connect to JBoss AS with localhost:9999 \n";
                message += "Go to Connection -> New Connection and enter a Remote Process \n";
                message += "of the form service:jmx:remoting-jmx://{host_name}:{port}  where \n";
                message += "{host_name} and {port} are the address of the native management \n";
                message += "interface of the AS7 installation being monitored.";
                JOptionPane.showMessageDialog(null, message);
                return false;
            }
        }

        return true;
    }

    private boolean connectUsingRemoting(CommandContext cmdCtx, RemotingMBeanServerConnection rmtMBeanSvrConn)
            throws IOException, CliInitializationException {
        Connection conn = rmtMBeanSvrConn.getConnection();
        Channel channel;
        final IoFuture<Channel> futureChannel = conn.openChannel("management", OptionMap.EMPTY);
        IoFuture.Status result = futureChannel.await(5, TimeUnit.SECONDS);
        if (result == IoFuture.Status.DONE) {
            channel = futureChannel.get();
        } else {
            return false;
        }

        ModelControllerClient modelCtlrClient = ExistingChannelModelControllerClient.createReceiving(channel, createExecutor());
        cmdCtx.bindClient(modelCtlrClient);

        return true;
    }

    private ExecutorService createExecutor() {
        final ThreadGroup group = new ThreadGroup("management-client-thread");
        final ThreadFactory threadFactory = new JBossThreadFactory(group, Boolean.FALSE, null, "%G " + executorCount.incrementAndGet() + "-%t", null, null, doPrivileged(GetAccessControlContextAction.getInstance()));
        return new ThreadPoolExecutor(2, DEFAULT_MAX_THREADS, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }

    @Override
    public SwingWorker<?, ?> newSwingWorker() {
        if (!initComplete && isConnected) {
            initComplete = true;
            configureMyJInternalFrame();
        }

        return null;
    }

    private void configureMyJInternalFrame() {
        ImageIcon icon = new ImageIcon(GuiMain.getJBossIcon());
        Component component = jconsolePanel;

        while (component != null) {
            component = component.getParent();
            if (component instanceof JInternalFrame) {
                JInternalFrame frame = (JInternalFrame)component;
                frame.setFrameIcon(icon);
                return;
            }
        }
    }

    private String getJBossServerName() {
        String serverNamePrefix = "JBoss CLI / ";
        String serverNameCommand = "/:read-attribute(name=name,include-defaults=true)";
        if (!cliGuiCtx.isStandalone()) {
            serverNameCommand = "/host=*" + serverNameCommand;
        }

        try {
            ModelNode result = cliGuiCtx.getExecutor().doCommand(serverNameCommand);
            String outcome = result.get("outcome").asString();
            if (outcome.equals("success") && cliGuiCtx.isStandalone()) {
                return serverNamePrefix + result.get("result").asString();
            } else if (outcome.equals("success")) {
                return serverNamePrefix + result.get("result").asList().get(0).get("result").asString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serverNamePrefix + "<unknown>";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5856.java