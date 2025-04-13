error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13402.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13402.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13402.java
text:
```scala
s@@uper(environment, authCode);

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
package org.jboss.test.as.protocol.support.server.manager;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jboss.as.model.DomainModel;
import org.jboss.as.process.CommandLineConstants;
import org.jboss.as.process.ProcessOutputStreamHandler.Managed;
import org.jboss.as.server.manager.Main;
import org.jboss.as.server.manager.ServerManager;
import org.jboss.as.server.manager.ServerManagerEnvironment;
import org.jboss.as.server.manager.SystemExiter;
import org.jboss.test.as.protocol.support.process.TestProcessManager;

/**
 * Starts a real server manager instance in-process
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class TestServerManagerProcess extends ServerManager {

    private final Managed managed;

    private final CountDownLatch shutdownLatch = new CountDownLatch(1);
    private volatile CountDownLatch domainModelLatch = new CountDownLatch(1);
    private volatile CountDownLatch downLatch = new CountDownLatch(1);

    public static ServerManager createServerManager(TestProcessManager pm) throws Exception{
        return createServerManager(pm, false);
    }

    public TestServerManagerProcess(Managed managed, ServerManagerEnvironment environment) {
        super(environment);
        this.managed = managed;
    }

    public static TestServerManagerProcess createServerManager(TestProcessManager pm, boolean restart) throws Exception{
        String[] args = new String[] {
                CommandLineConstants.INTERPROCESS_NAME,
                "ServerManager",
                CommandLineConstants.INTERPROCESS_PM_ADDRESS,
                InetAddress.getLocalHost().getHostAddress(),
                CommandLineConstants.INTERPROCESS_PM_PORT,
                String.valueOf(pm.getPort()),
                CommandLineConstants.INTERPROCESS_SM_ADDRESS,
                InetAddress.getLocalHost().getHostAddress(),
                CommandLineConstants.INTERPROCESS_SM_PORT,
                "0"
        };

        if (restart) {
            int length = args.length;
            args = Arrays.copyOf(args,  length + 1);
            args[length] = CommandLineConstants.RESTART_SERVER_MANAGER;
        }

        return TestServerManagerProcess.createServerManager(null, Arrays.asList(args), System.in, System.out, System.err);
    }

    public static TestServerManagerProcess createServerManager(Managed managed, List<String> command, InputStream stdin, PrintStream stdout, PrintStream stderr) throws Exception {
        SystemExiter.initialize(new ServerManagerNoopExiter());

        //The command created by TestProcessManager only contains the args
        String[] args = command.toArray(new String[command.size()]);

        ServerManagerEnvironment config = Main.determineEnvironment(args, System.getProperties(), stdin, stdout, stderr);
        if (config == null) {
            throw new RuntimeException("Could not determine SM environment");
        } else {
            TestServerManagerProcess manager = new TestServerManagerProcess(managed, config);
            manager.start();
            manager.waitForLatch(manager.domainModelLatch);
            return manager;
        }
    }

    @Override
    public void setDomain(final DomainModel domain) {
        super.setDomain(domain);
        domainModelLatch.countDown();
    }


    @Override
    public void stop() {
        super.stop();
        shutdownLatch.countDown();
    }

    public void crashServerManager(int exitCode) {
        managed.processEnded(exitCode);
    }

    @Override
    public void downServer(String downServerName) {
        super.downServer(downServerName);
        downLatch.countDown();
    }

    public void resetDownLatch() {
        downLatch = new CountDownLatch(1);
    }

    public void waitForShutdown() throws InterruptedException {
        waitForLatch(shutdownLatch);
    }

    public void waitForDown() throws InterruptedException {
        waitForLatch(downLatch);
    }

    private void waitForLatch(CountDownLatch latch) throws InterruptedException {
        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("Wait timed out");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13402.java