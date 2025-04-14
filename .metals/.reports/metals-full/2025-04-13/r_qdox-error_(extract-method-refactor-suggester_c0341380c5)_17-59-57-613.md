error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1163.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1163.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1088
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1163.java
text:
```scala
public class UndeployHandler extends BatchModeCommandHandler {

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


import java.util.Collections;
import java.util.List;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandArgumentCompleter;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestBuilder;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author Alexey Loubyansky
 */
public class UndeployHandler extends CommandHandlerWithHelp {

    public UndeployHandler() {
        super("undeploy", true, new SimpleTabCompleterWithDelegate(new String[]{"--help", "-l"},
                new CommandArgumentCompleter() {
                    @Override
                    public int complete(CommandContext ctx, String buffer,
                            int cursor, List<String> candidates) {

                        int nextCharIndex = 0;
                        while (nextCharIndex < buffer.length()) {
                            if (!Character.isWhitespace(buffer.charAt(nextCharIndex))) {
                                break;
                            }
                            ++nextCharIndex;
                        }

                        if(ctx.getModelControllerClient() != null) {
                            List<String> deployments = Util.getDeployments(ctx.getModelControllerClient());
                            if(deployments.isEmpty()) {
                                return -1;
                            }

                            String opBuffer = buffer.substring(nextCharIndex).trim();
                            if (opBuffer.isEmpty()) {
                                candidates.addAll(deployments);
                            } else {
                                for(String name : deployments) {
                                    if(name.startsWith(opBuffer)) {
                                        candidates.add(name);
                                    }
                                }
                                Collections.sort(candidates);
                            }
                            return nextCharIndex;
                        } else {
                            return -1;
                        }
                    }}));
    }

    @Override
    protected void doHandle(CommandContext ctx) {

        ModelControllerClient client = ctx.getModelControllerClient();
        if(!ctx.hasArguments()) {
            printList(ctx, Util.getDeployments(client));
            return;
        }

        String deployment = null;
        List<String> args = ctx.getArguments();
        if(args.size() > 0) {
            deployment = args.get(0);
        }

        if (deployment == null) {
            printList(ctx, Util.getDeployments(client));
            return;
        }

        DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();

        // undeploy
        builder = new DefaultOperationRequestBuilder();
        builder.setOperationName("undeploy");
        builder.addNode("deployment", deployment);

        ModelNode result;
        try {
            ModelNode request = builder.buildRequest();
            result = client.execute(request);
         } catch(Exception e) {
             ctx.printLine("Failed to undeploy: " + e.getLocalizedMessage());
             return;
         }

         // TODO undeploy may fail if the content failed to deploy but remove should still be executed
         if(!Util.isSuccess(result)) {
             ctx.printLine("Undeploy failed: " + Util.getFailureDescription(result));
             return;
         }

        // remove
        builder = new DefaultOperationRequestBuilder();
        builder.setOperationName("remove");
        builder.addNode("deployment", deployment);
        try {
            ModelNode request = builder.buildRequest();
            result = client.execute(request);
        } catch(Exception e) {
            ctx.printLine("Failed to remove the deployment content from the repository: " + e.getLocalizedMessage());
            return;
        }
        if(!Util.isSuccess(result)) {
            ctx.printLine("Remove failed: " + Util.getFailureDescription(result));
            return;
        }

        ctx.printLine("'" + deployment + "' undeployed successfully.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1163.java