error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1154.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1154.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1091
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1154.java
text:
```scala
public class CreateJmsCFHandler extends BatchModeCommandHandler {

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
p@@ackage org.jboss.as.cli.handlers;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestBuilder;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author Alexey Loubyansky
 */
public class CreateJmsCFHandler extends CommandHandlerWithHelp {

    public CreateJmsCFHandler() {
        super("create-jms-cf", true, new SimpleTabCompleter(new String[]{
                "--help", "name=", "auto-group=", "entries=", "connector=",
                "block-on-acknowledge=", "block-on-durable-send=", "block-on-non-durable-send=",
                "cache-large-message-client=", "call-timeout=",
                "client-failure-check-period=", "client-id=", "confirmation-window-size=",
                "connection-ttl=", "connector=", "consumer-max-rate=",
                "consumer-window-size=", "discovery-group-name=", "dups-ok-batch-size=",
                "failover-on-initial-connection=", "failover-on-server-shutdown=",
                "group-id=", "max-retry-interval=", "min-large-message-size=",
                "pre-acknowledge=", "producer-max-rate=", "producer-window-size=",
                "reconnect-attempts=", "retry-interval=", "retry-interval-multiplier=",
                "scheduled-thread-pool-max-size=", "thread-pool-max-size=",
                "transaction-batch-size=", "use-global-pools="}));
    }

    /* (non-Javadoc)
     * @see org.jboss.as.cli.handlers.CommandHandlerWithHelp#doHandle(org.jboss.as.cli.CommandContext)
     */
    @Override
    protected void doHandle(CommandContext ctx) {

        DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();
        builder.addNode("subsystem", "jms");
        builder.setOperationName("add");

        String name = null;
        String entriesStr = null;
        for(String argName : ctx.getArgumentNames()) {
            if(argName.equals("name")) {
                name = ctx.getNamedArgument(argName);
            } else if(argName.equals("entries")) {
                entriesStr = ctx.getNamedArgument(argName);
            } else {
                builder.addProperty(argName, ctx.getNamedArgument(argName));
            }
        }

        if(name == null) {
            ctx.printLine("Required argument 'name' is missing.");
            return;
        }

        builder.addNode("connection-factory", name);
        ModelNode entriesNode = builder.getModelNode().get("entries");
        if(entriesStr == null) {
            entriesNode.add(name);
        } else {
            String[] split = entriesStr.split(",");
            for(int i = 0; i < split.length; ++i) {
                String entry = split[i].trim();
                if(!entry.isEmpty()) {
                    entriesNode.add(entry);
                }
            }
        }

        ModelControllerClient client = ctx.getModelControllerClient();
        final ModelNode result;
        try {
            ModelNode request = builder.buildRequest();
            result = client.execute(request);
        } catch (Exception e) {
            ctx.printLine("Failed to perform operation: " + e.getLocalizedMessage());
            return;
        }

        if (!Util.isSuccess(result)) {
            ctx.printLine(Util.getFailureDescription(result));
            return;
        }

        ctx.printLine("Created connection factory " + name);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1154.java