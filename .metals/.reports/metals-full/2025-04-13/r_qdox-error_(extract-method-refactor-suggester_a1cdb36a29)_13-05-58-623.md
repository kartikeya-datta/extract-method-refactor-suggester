error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5862.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5862.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5862.java
text:
```scala
c@@tx.connectController("http-remoting", addr, TestSuiteEnvironment.getServerPort());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.test.integration.management.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandLineException;
import org.jboss.as.test.http.Authentication;
import org.jboss.as.test.shared.TestSuiteEnvironment;
import org.jboss.dmr.ModelNode;
import org.junit.Assert;


/**
 *
 * @author Dominik Pospisil <dpospisi@redhat.com>
 * @author Alexey Loubyansky <olubyans@redhat.com>
 */
public class CLIWrapper {

    private final CommandContext ctx;

    private ByteArrayOutputStream consoleOut;

    /**
     * Creates new CLI wrapper.
     *
     * @throws Exception
     */
    public CLIWrapper() throws Exception {
        this(false);
    }

    /**
     * Creates new CLI wrapper. If the connect parameter is set to true the CLI will connect to the server using
     * <code>connect</code> command.
     *
     * @param connect indicates if the CLI should connect to server automatically.
     * @param cliArgs specifies additional CLI command line arguments
     * @throws Exception
     */
    public CLIWrapper(boolean connect) throws Exception {
        this(connect, null);
    }

    /**
     * Creates new CLI wrapper. If the connect parameter is set to true the CLI will connect to the server using
     * <code>connect</code> command.
     *
     * @param connect indicates if the CLI should connect to server automatically.
     * @param cliAddress The default name of the property containing the cli address. If null the value of the {@code node0} property is
     * used, and if that is absent {@code localhost} is used
     * @param cliArgs specifies additional CLI command line arguments
     */
    public CLIWrapper(boolean connect, String cliAddress) throws CliInitializationException {

        consoleOut = new ByteArrayOutputStream();
        final char[] password = getPassword() == null ? null : getPassword().toCharArray();
        System.setProperty("aesh.terminal","org.jboss.aesh.terminal.TestTerminal");
        ctx = CLITestUtil.getCommandContext(
                TestSuiteEnvironment.getServerAddress(), TestSuiteEnvironment.getServerPort(), getUsername(), password,
                createConsoleInput(), consoleOut);

        if (!connect) {
            return;
        }
        Assert.assertTrue(sendConnect(cliAddress));
    }

    protected InputStream createConsoleInput() {
        return null;
    }

    public boolean isConnected() {
        return ctx.getModelControllerClient() != null;
    }

    /**
     * Sends a line with the connect command. This will look for the {@code node0} system property
     * and use that as the address. If the system property is not set {@code localhost} will
     * be used
     */
    public boolean sendConnect() {
        return sendConnect(null);
    }

    /**
     * Sends a line with the connect command.
     * @param cliAddress The address to connect to. If null it will look for the {@code node0} system
     * property and use that as the address. If the system property is not set {@code localhost} will
     * be used
     */
    public boolean sendConnect(String cliAddress) {
        String addr = cliAddress != null ? cliAddress : TestSuiteEnvironment.getServerAddress();
        try {
            ctx.connectController(addr, TestSuiteEnvironment.getServerPort());
            return true;
        } catch (CommandLineException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends command line to CLI.
     *
     * @param line specifies the command line.
     * @param readEcho if set to true reads the echo response form the CLI.
     * @throws Exception
     */
    public boolean sendLine(String line, boolean ignoreError)  {
        consoleOut.reset();
        if(ignoreError) {
            ctx.handleSafe(line);
            return ctx.getExitCode() == 0;
        } else {
            try {
                ctx.handle(line);
            } catch (CommandLineException e) {
                Assert.fail("Failed to execute line '" + line + "': " + e.getLocalizedMessage());
            }
        }
        return true;
    }

    /**
     * Sends command line to CLI.
     *
     * @param line specifies the command line.
     * @throws Exception
     */
    public void sendLine(String line) {
        sendLine(line, false);
    }

    /**
     * Reads the last command's output.
     *
     * @return next line from CLI output
     */
    public String readOutput()  {
        if(consoleOut.size() <= 0) {
            return null;
        }
        return new String(consoleOut.toByteArray());
    }

    /**
     * Consumes all available output from CLI and converts the output to ModelNode operation format
     *
     * @return array of CLI output lines
     */
    public CLIOpResult readAllAsOpResult() throws IOException {
        final String response = readOutput();
        if(response == null) {
            return new CLIOpResult();
        }
        final ModelNode node = ModelNode.fromString(response);
        return new CLIOpResult(node);
    }

    /**
     * Sends quit command to CLI.
     *
     * @throws Exception
     */
    public synchronized void quit() {
        ctx.terminateSession();
    }

    /**
     * Returns CLI status.
     *
     * @return true if and only if the CLI has finished.
     */
    public boolean hasQuit() {
        return ctx.isTerminated();
    }

    protected String getUsername() {
        return Authentication.USERNAME;
    }

    protected String getPassword() {
        return Authentication.PASSWORD;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5862.java