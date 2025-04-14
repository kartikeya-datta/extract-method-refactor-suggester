error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4127.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4127.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4127.java
text:
```scala
t@@hrow new CommandFormatException("Failed to get the list of the operation properties: \"" + Util.getFailureDescription(outcome) + '\"');

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
package org.jboss.as.cli.handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CancellationException;

import org.jboss.as.cli.CommandArgument;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandHandler;
import org.jboss.as.cli.OperationCommand;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.operation.impl.DefaultCallbackHandler;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

/**
 * The operation request handler.
 *
 * @author Alexey Loubyansky
 */
public class OperationRequestHandler implements CommandHandler, OperationCommand {

    @Override
    public boolean isBatchMode() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.jboss.as.cli.CommandHandler#handle(org.jboss.as.cli.CommandContext)
     */
    @Override
    public void handle(CommandContext ctx) {

        ModelControllerClient client = ctx.getModelControllerClient();
        if(client == null) {
            ctx.printLine("You are disconnected at the moment." +
                    " Type 'connect' to connect to the server" +
                    " or 'help' for the list of supported commands.");
            return;
        }

        ModelNode request = (ModelNode) ctx.get("OP_REQ");
        if(request == null) {
            ctx.printLine("Parsed request isn't available.");
            return;
        }

        try {
            validateRequest(ctx, request);
        } catch(CommandFormatException e) {
            ctx.printLine(e.getLocalizedMessage());
            return;
        }

        try {
            ModelNode result = client.execute(request);
            ctx.printLine(result.toString());
        } catch(NoSuchElementException e) {
            ctx.printLine("ModelNode request is incomplete: " + e.getMessage());
        } catch (CancellationException e) {
            ctx.printLine("The result couldn't be retrieved (perhaps the task was cancelled: " + e.getLocalizedMessage());
        } catch (IOException e) {
            ctx.printLine("Communication error: " + e.getLocalizedMessage());
            ctx.disconnectController();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public boolean isAvailable(CommandContext ctx) {
        return true;
    }

    @Override
    public ModelNode buildRequest(CommandContext ctx) throws CommandFormatException {
        return ((DefaultCallbackHandler)ctx.getParsedCommandLine()).toOperationRequest();
    }

    @Override
    public boolean hasArgument(String name) {
        return false;
    }

    @Override
    public boolean hasArgument(int index) {
        return false;
    }

    @Override
    public List<CommandArgument> getArguments(CommandContext ctx) {
        return Collections.emptyList();
    }

    private void validateRequest(CommandContext ctx, ModelNode request) throws CommandFormatException {

        final ModelControllerClient client = ctx.getModelControllerClient();
        if(client == null) {
            throw new CommandFormatException("No connection to the controller.");
        }

        final Set<String> keys = request.keys();

        if(!keys.contains(Util.OPERATION)) {
            throw new CommandFormatException("Request is missing the operation name.");
        }
        final String operationName = request.get(Util.OPERATION).asString();

        if(!keys.contains(Util.ADDRESS)) {
            throw new CommandFormatException("Request is missing the address part.");
        }
        final ModelNode address = request.get(Util.ADDRESS);

        if(keys.size() == 2) { // no props
            return;
        }

        final ModelNode opDescrReq = new ModelNode();
        opDescrReq.get(Util.ADDRESS).set(address);
        opDescrReq.get(Util.OPERATION).set(Util.READ_OPERATION_DESCRIPTION);
        opDescrReq.get(Util.NAME).set(operationName);

        final ModelNode outcome;
        try {
            outcome = client.execute(opDescrReq);
        } catch(Exception e) {
            throw new CommandFormatException("Failed to perform " + Util.READ_OPERATION_DESCRIPTION + " to validate the request: " + e.getLocalizedMessage());
        }
        if (!Util.isSuccess(outcome)) {
            throw new CommandFormatException("Failed to get the list of supported operation properties.");
        }

        if(!outcome.has(Util.RESULT)) {
            throw new CommandFormatException("Failed to perform " + Util.READ_OPERATION_DESCRIPTION + " to validate the request: result is not available.");
        }
        final ModelNode result = outcome.get(Util.RESULT);
        if(!result.hasDefined(Util.REQUEST_PROPERTIES)) {
            throw new CommandFormatException("Operation '" + operationName + "' does not expect any property.");
        }
        final Set<String> definedProps = result.get("request-properties").keys();
        if(definedProps.isEmpty()) {
            throw new CommandFormatException("Operation '" + operationName + "' does not expect any property.");
        }

        int skipped = 0;
        for(String prop : keys) {
            if(skipped < 2 && (prop.equals(Util.ADDRESS) || prop.equals(Util.OPERATION))) {
                ++skipped;
                continue;
            }
            if(!definedProps.contains(prop)) {
                if(!Util.OPERATION_HEADERS.equals(prop)) {
                    throw new CommandFormatException("'" + prop + "' is not found among the supported properties: " + definedProps);
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4127.java