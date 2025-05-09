error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10546.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10546.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10546.java
text:
```scala
c@@tx.terminateSession();

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
package org.jboss.as.cli.scriptsupport;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandLineException;
import org.jboss.dmr.ModelNode;

/**
 * This class is intended to be used with JVM-based scripting languages.  It acts as a facade to the CLI public API,
 * providing a single class that can be used to connect, run CLI commands, and disconnect.  It also removes the need
 * to catch checked exceptions.
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2012 Red Hat Inc.
 */
public class CLI {

    private CommandContext ctx;

    private CLI() {} // only allow new instances from newInstance() method.

    /**
     * Create a new CLI instance.
     *
     * @return The CLI instance.
     */
    public static CLI newInstance() {
        return new CLI();
    }

    private void checkAlreadyConnected() {
        if (ctx != null) throw new IllegalStateException("Already connected to server.");
    }

    private void checkNotConnected() {
        if (ctx == null) throw new IllegalStateException("Not connected to server.");
        if (ctx.isTerminated()) throw new IllegalStateException("Session is terminated.");
    }

    /**
     * Return the CLI CommandContext that was created when connected to the server.  This allows a script developer full
     * access to CLI facilities if needed.
     *
     * @return The CommandContext, or <code>null</code> if not connected.
     */
    public CommandContext getCommandContext() {
        return this.ctx;
    }

    /**
     * Connect to the server using the default host and port.
     */
    public void connect() {
        checkAlreadyConnected();
        try {
            ctx = CommandContextFactory.getInstance().newCommandContext();
            ctx.connectController();
        } catch (CliInitializationException e) {
            throw new IllegalStateException("Unable to initialize command context.", e);
        } catch (CommandLineException e) {
            throw new IllegalStateException("Unable to connect to controller.", e);
        }
    }

    /**
     * Connect to the server using the default host and port.
     *
     * @param username The user name for logging in.
     * @param password The password for logging in.
     */
    public void connect(String username, char[] password) {
        checkAlreadyConnected();
        try {
            ctx = CommandContextFactory.getInstance().newCommandContext(username, password);
            ctx.connectController();
        } catch (CliInitializationException e) {
            throw new IllegalStateException("Unable to initialize command context.", e);
        } catch (CommandLineException e) {
            throw new IllegalStateException("Unable to connect to controller.", e);
        }
    }

    /**
     * Connect to the server using a specified host and port.
     *
     * @param controllerHost The host name.
     * @param controllerPort The port.
     * @param username The user name for logging in.
     * @param password The password for logging in.
     */
    public void connect(String controller, String username, char[] password) {
        checkAlreadyConnected();
        try {
            ctx = CommandContextFactory.getInstance().newCommandContext(controller, username, password);
            ctx.connectController();
        } catch (CliInitializationException e) {
            throw new IllegalStateException("Unable to initialize command context.", e);
        } catch (CommandLineException e) {
            throw new IllegalStateException("Unable to connect to controller.", e);
        }
    }

    /**
     * Connect to the server using a specified host and port.
     *
     * @param controllerHost The host name.
     * @param controllerPort The port.
     * @param username The user name for logging in.
     * @param password The password for logging in.
     */
    public void connect(String controllerHost, int controllerPort, String username, char[] password) {
        connect("http-remoting", controllerHost, controllerPort, username, password);
    }

    /**
     * Connect to the server using a specified host and port.
     * @param protocol The protocol
     * @param controllerHost The host name.
     * @param controllerPort The port.
     * @param username The user name for logging in.
     * @param password The password for logging in.
     */
    public void connect(String protocol, String controllerHost, int controllerPort, String username, char[] password) {
        checkAlreadyConnected();
        try {
            ctx = CommandContextFactory.getInstance().newCommandContext(constructUri(protocol, controllerHost, controllerPort), username, password);
            ctx.connectController();
        } catch (CliInitializationException e) {
            throw new IllegalStateException("Unable to initialize command context.", e);
        } catch (CommandLineException e) {
            throw new IllegalStateException("Unable to connect to controller.", e);
        }
    }

    /**
     * Disconnect from the server.
     */
    public void disconnect() {
        try {
            checkNotConnected();
            ctx.disconnectController();
        } finally {
            ctx = null;
        }
    }

    /**
     * Execute a CLI command.  This can be any command that you might execute on the CLI command line, including both
     * server-side operations and local commands such as 'cd' or 'cn'.
     *
     * @param cliCommand A CLI command.
     * @return A result object that provides all information about the execution of the command.
     */
    public Result cmd(String cliCommand) {
        checkNotConnected();
        try {
            ModelNode request = ctx.buildRequest(cliCommand);
            ModelNode response = ctx.getModelControllerClient().execute(request);
            return new Result(cliCommand, request, response);
        } catch (CommandFormatException cfe) {
            // if the command can not be converted to a ModelNode, it might be a local command
            try {
                ctx.handle(cliCommand);
                return new Result(cliCommand, ctx.getExitCode());
            } catch (CommandLineException cle) {
                throw new IllegalArgumentException("Error handling command: " + cliCommand, cle);
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("Unable to send command " + cliCommand + " to server.", ioe);
        }
    }

    private String constructUri(final String protocol, final String host, final int port) {
        try {
            URI uri = new URI(protocol, null, host, port, null, null, null);
            // String the leading '//' if there is no protocol.
            return protocol == null ? uri.toString().substring(2) : uri.toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to construct URI.", e);
        }
    }

    /**
     * The Result class provides all information about an executed CLI command.
     */
    public class Result {
        private String cliCommand;
        private ModelNode request;
        private ModelNode response;

        private boolean isSuccess = false;
        private boolean isLocalCommand = false;

        Result(String cliCommand, ModelNode request, ModelNode response) {
            this.cliCommand = cliCommand;
            this.request = request;
            this.response = response;
            this.isSuccess = response.get("outcome").asString().equals("success");
        }

        Result(String cliCommand, int exitCode) {
            this.cliCommand = cliCommand;
            this.isSuccess = exitCode == 0;
            this.isLocalCommand = true;
        }

        /**
         * Return the original command as a String.
         * @return The original CLI command.
         */
        public String getCliCommand() {
            return this.cliCommand;
        }

        /**
         * If the command resulted in a server-side operation, return the ModelNode representation of the operation.
         *
         * @return The request as a ModelNode, or <code>null</code> if this was a local command.
         */
        public ModelNode getRequest() {
            return this.request;
        }

        /**
         * If the command resulted in a server-side operation, return the ModelNode representation of the response.
         *
         * @return The server response as a ModelNode, or <code>null</code> if this was a local command.
         */
        public ModelNode getResponse() {
            return this.response;
        }

        /**
         * Return true if the command was successful.  For a server-side operation, this is determined by the outcome of the
         * operation on the server side.
         *
         * @return <code>true</code> if the command was successful, <code>false</code> otherwise.
         */
        public boolean isSuccess() {
            return this.isSuccess;
        }

        /**
         * Return true if the command was only executed locally and did not result in a server-side operation.
         *
         * @return <code>true</code> if the command was only executed locally, <code>false</code> otherwise.
         */
        public boolean isLocalCommand() {
            return this.isLocalCommand;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10546.java