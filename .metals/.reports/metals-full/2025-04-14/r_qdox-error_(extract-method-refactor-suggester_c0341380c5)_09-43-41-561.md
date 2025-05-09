error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14543.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14543.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14543.java
text:
```scala
c@@tx.printLine("'" + name + "' is already deployed (use " + force.getFullName() + " to force re-deploy).");

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


import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandLineCompleter;
import org.jboss.as.cli.ParsedArguments;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.impl.ArgumentWithValue;
import org.jboss.as.cli.impl.ArgumentWithoutValue;
import org.jboss.as.cli.operation.OperationFormatException;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestBuilder;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author Alexey Loubyansky
 */
public class DeployHandler extends BatchModeCommandHandler {

    private final ArgumentWithoutValue force;
    private final ArgumentWithoutValue l;
    private final ArgumentWithoutValue path;
    private final ArgumentWithoutValue name;
    private final ArgumentWithoutValue rtName;
    private final ArgumentWithValue serverGroups;
    private final ArgumentWithoutValue allServerGroups;
    private final ArgumentWithoutValue disabled;

    public DeployHandler() {
        super("deploy", true);

        SimpleArgumentTabCompleter argsCompleter = (SimpleArgumentTabCompleter) this.getArgumentCompleter();

        l = new ArgumentWithoutValue("-l");
        l.setExclusive(true);
        argsCompleter.addArgument(l);

        FilenameTabCompleter pathCompleter = Util.isWindows() ? WindowsFilenameTabCompleter.INSTANCE : DefaultFilenameTabCompleter.INSTANCE;
        path = new ArgumentWithValue(false, pathCompleter, 0, "--path") {
            @Override
            public String getValue(ParsedArguments args) {
                String value = super.getValue(args);
                if(value != null) {
                    if(value.length() >= 0 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                        value = value.substring(1, value.length() - 1);
                    }
                }
                return value;
            }
        };
        path.addCantAppearAfter(l);
        argsCompleter.addArgument(path);

        force = new ArgumentWithoutValue("--force", "-f");
        force.addRequiredPreceding(path);
        argsCompleter.addArgument(force);

        name = new ArgumentWithValue( new CommandLineCompleter() {
            @Override
            public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

                ParsedArguments args = ctx.getParsedArguments();
                if(path.isPresent(args)) {
                    return -1;
                }

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

            }}, "--name");
        path.addCantAppearAfter(l);
        path.addCantAppearAfter(name);
        //name.addRequiredPreceding(path);
        argsCompleter.addArgument(name);

        rtName = new ArgumentWithValue("--runtime-name");
        rtName.addRequiredPreceding(path);
        argsCompleter.addArgument(rtName);

        allServerGroups = new ArgumentWithoutValue("--all-server-groups")  {
            @Override
            public boolean canAppearNext(CommandContext ctx) {
                if(!ctx.isDomainMode()) {
                    return false;
                }
                return super.canAppearNext(ctx);
            }
        };

        argsCompleter.addArgument(allServerGroups);
        allServerGroups.addRequiredPreceding(path);
        allServerGroups.addRequiredPreceding(name);

        serverGroups = new ArgumentWithValue(false, new CommandLineCompleter() {
            @Override
            public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {
                List<String> allGroups = Util.getServerGroups(ctx.getModelControllerClient());
                if(buffer.isEmpty()) {
                    candidates.addAll(allGroups);
                    Collections.sort(candidates);
                    return 0;
                }

                final String[] groups = buffer.split(",+");

                final String chunk;
                final int lastGroupIndex;
                if(buffer.charAt(buffer.length() - 1) == ',') {
                    lastGroupIndex = groups.length;
                    chunk = null;
                } else {
                    lastGroupIndex = groups.length - 1;
                    chunk = groups[groups.length - 1];
                }

                for(int i = 0; i < lastGroupIndex; ++i) {
                    allGroups.remove(groups[i]);
                }

                final int result;
                if(chunk == null) {
                    candidates.addAll(allGroups);
                    result = buffer.length();
                } else {
                    for(String group : allGroups) {
                        if(group.startsWith(chunk)) {
                            candidates.add(group);
                        }
                    }
                    result = buffer.lastIndexOf(',') + 1;
                }
                Collections.sort(candidates);
                return result;
            }}, "--server-groups") {
            @Override
            public boolean canAppearNext(CommandContext ctx) {
                if(!ctx.isDomainMode()) {
                    return false;
                }
                return super.canAppearNext(ctx);
            }
        };
        argsCompleter.addArgument(serverGroups);
        serverGroups.addRequiredPreceding(path);
        serverGroups.addRequiredPreceding(name);

        serverGroups.addCantAppearAfter(allServerGroups);
        allServerGroups.addCantAppearAfter(serverGroups);

        disabled = new ArgumentWithoutValue("--disabled");
        argsCompleter.addArgument(disabled);
        disabled.addRequiredPreceding(path);
    }

    @Override
    protected void doHandle(CommandContext ctx) {

        ModelControllerClient client = ctx.getModelControllerClient();

        ParsedArguments args = ctx.getParsedArguments();
        boolean l = this.l.isPresent(args);
        if (!args.hasArguments() || l) {
            printList(ctx, Util.getDeployments(client), l);
            return;
        }

        final String path = this.path.getValue(args);
        final File f;
        if(path != null) {
            f = new File(path);
            if(!f.exists()) {
                ctx.printLine("Path " + f.getAbsolutePath() + " doesn't exist.");
                return;
            }
            if(f.isDirectory()) {
                ctx.printLine(f.getAbsolutePath() + " is a directory.");
                return;
            }
        } else {
            f = null;
        }

        String name = this.name.getValue(args);
        if(name == null) {
            if(f == null) {
                ctx.printLine("Either path or --name is requied.");
                return;
            }
            name = f.getName();
        }

        final String runtimeName = rtName.getValue(args);

        if(Util.isDeploymentInRepository(name, client) && f != null) {
            if(force.isPresent(args)) {
                DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();

                ModelNode result;

                // replace
                builder = new DefaultOperationRequestBuilder();
                builder.setOperationName("full-replace-deployment");
                builder.addProperty("name", name);
                if(runtimeName != null) {
                    builder.addProperty("runtime-name", runtimeName);
                }

                FileInputStream is = null;
                try {
                    is = new FileInputStream(f);
                    ModelNode request = builder.buildRequest();
                    OperationBuilder op = OperationBuilder.Factory.create(request);
                    op.addInputStream(is);
                    request.get("content").get(0).get("input-stream-index").set(0);
                    result = client.execute(op.build());
                } catch(Exception e) {
                    ctx.printLine("Failed to replace the deployment: " + e.getLocalizedMessage());
                    return;
                } finally {
                    StreamUtils.safeClose(is);
                }
                if(!Util.isSuccess(result)) {
                    ctx.printLine(Util.getFailureDescription(result));
                    return;
                }

                ctx.printLine("'" + name + "' re-deployed successfully.");
            } else {
                ctx.printLine("'" + name + "' is already deployed (use " + force.getDefaultName() + " to force re-deploy).");
            }

            return;
        } else {

            DefaultOperationRequestBuilder builder;
            ModelNode result;

            // add
            if (f != null) {
                builder = new DefaultOperationRequestBuilder();
                builder.setOperationName("add");
                builder.addNode("deployment", name);
                if (runtimeName != null) {
                    builder.addProperty("runtime-name", runtimeName);
                }

                FileInputStream is = null;
                try {
                    is = new FileInputStream(f);
                    ModelNode request = builder.buildRequest();
                    OperationBuilder op = OperationBuilder.Factory.create(request);
                    op.addInputStream(is);
                    request.get("content").get(0).get("input-stream-index").set(0);
                    result = client.execute(op.build());
                } catch (Exception e) {
                    ctx.printLine("Failed to add the deployment content to the repository: " + e.getLocalizedMessage());
                    return;
                } finally {
                    StreamUtils.safeClose(is);
                }
                if (!Util.isSuccess(result)) {
                    ctx.printLine(Util.getFailureDescription(result));
                    return;
                }
            }

            //deploy
            if (!disabled.isPresent(args)) {
                final ModelNode request;

                if (ctx.isDomainMode()) {

                    final List<String> serverGroups;
                    if (ctx.isDomainMode()) {
                        if(allServerGroups.isPresent(args)) {
                            serverGroups = Util.getServerGroups(client);
                        } else {
                            String serverGroupsStr = this.serverGroups.getValue(args);
                            if(serverGroupsStr == null) {
                                ctx.printLine("Either --all-server-groups or --server-groups must be specified.");
                                return;
                            }
                            serverGroups = Arrays.asList(serverGroupsStr.split(","));
                        }

                        if(serverGroups.isEmpty()) {
                            ctx.printLine("No server group is available.");
                            return;
                        }
                    } else {
                        serverGroups = null;
                    }

                    request = new ModelNode();
                    request.get("operation").set("composite");
                    request.get("address").setEmptyList();
                    ModelNode steps = request.get("steps");

                    for (String serverGroup : serverGroups) {
                        steps.add(Util.configureDeploymentOperation("add", name, serverGroup));
                    }

                    for (String serverGroup : serverGroups) {
                        steps.add(Util.configureDeploymentOperation("deploy", name, serverGroup));
                    }
                } else {
                    builder = new DefaultOperationRequestBuilder();
                    builder.setOperationName("deploy");
                    builder.addNode("deployment", name);
                    try {
                        request = builder.buildRequest();
                    } catch (Exception e) {
                        ctx.printLine("Failed to deploy: " + e.getLocalizedMessage());
                        return;
                    }
                }

                try {
                    result = client.execute(request);
                } catch (Exception e) {
                    ctx.printLine("Failed to deploy: " + e.getLocalizedMessage());
                    return;
                }

                if (!Util.isSuccess(result)) {
                    ctx.printLine(Util.getFailureDescription(result));
                    return;
                }
            }
            ctx.printLine("'" + name + "' deployed successfully.");
        }
    }

    public ModelNode buildRequest(CommandContext ctx) throws OperationFormatException {

        ParsedArguments args = ctx.getParsedArguments();
        if (!args.hasArguments()) {
            throw new OperationFormatException("Required arguments are missing.");
        }

        final String filePath;
        try {
            filePath = path.getValue(args);
        } catch(IllegalArgumentException e) {
            throw new OperationFormatException("Missing required path argument.");
        }
        String name = this.name.getValue(args);
        String runtimeName = rtName.getValue(args);


        File f = new File(filePath);
        if(!f.exists()) {
            throw new OperationFormatException(f.getAbsolutePath() + " doesn't exist.");
        }

        if(name == null) {
            name = f.getName();
        }

        if(Util.isDeploymentInRepository(name, ctx.getModelControllerClient())) {
            if(force.isPresent(args)) {
                DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();

                // replace
                builder = new DefaultOperationRequestBuilder();
                builder.setOperationName("full-replace-deployment");
                builder.addProperty("name", name);
                if(runtimeName != null) {
                    builder.addProperty("runtime-name", runtimeName);
                }

                byte[] bytes = readBytes(f);
                builder.getModelNode().get("bytes").set(bytes);
                return builder.buildRequest();
            } else {
                throw new OperationFormatException("'" + name + "' is already deployed (use -f to force re-deploy).");
            }
        }

        final List<String> serverGroups;
        if (ctx.isDomainMode()) {
            if(allServerGroups.isPresent(args)) {
                serverGroups = Util.getServerGroups(ctx.getModelControllerClient());
            } else {
                String serverGroupsStr = this.serverGroups.getValue(args);
                if(serverGroupsStr == null) {
                    new OperationFormatException("Either --all-server-groups or --server-groups must be specified.");
                }
                serverGroups = Arrays.asList(serverGroupsStr.split(","));
            }

            if(serverGroups.isEmpty()) {
                new OperationFormatException("No server group is available.");
            }
        } else {
            serverGroups = null;
        }

        ModelNode composite = new ModelNode();
        composite.get("operation").set("composite");
        composite.get("address").setEmptyList();
        ModelNode steps = composite.get("steps");

        DefaultOperationRequestBuilder builder;

        // add
        builder = new DefaultOperationRequestBuilder();
        builder.setOperationName("add");
        builder.addNode("deployment", name);
        if (runtimeName != null) {
            builder.addProperty("runtime-name", runtimeName);
        }

        byte[] bytes = readBytes(f);
        builder.getModelNode().get("bytes").set(bytes);
        steps.add(builder.buildRequest());

        if(!disabled.isPresent(args)) {
            // deploy
            if (ctx.isDomainMode()) {
                for (String serverGroup : serverGroups) {
                    steps.add(Util.configureDeploymentOperation("add", name, serverGroup));
                }
                for (String serverGroup : serverGroups) {
                    steps.add(Util.configureDeploymentOperation("deploy", name, serverGroup));
                }
            } else {
                builder = new DefaultOperationRequestBuilder();
                builder.setOperationName("deploy");
                builder.addNode("deployment", name);
                steps.add(builder.buildRequest());
            }
        }
        return composite;
    }

    protected byte[] readBytes(File f) throws OperationFormatException {
        byte[] bytes;
        FileInputStream is = null;
        try {
            is = new FileInputStream(f);
            bytes = new byte[(int) f.length()];
            int read = is.read(bytes);
            if(read != bytes.length) {
                throw new OperationFormatException("Failed to read bytes from " + f.getAbsolutePath() + ": " + read + " from " + f.length());
            }
        } catch (Exception e) {
            throw new OperationFormatException("Failed to read file " + f.getAbsolutePath(), e);
        } finally {
            StreamUtils.safeClose(is);
        }
        return bytes;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14543.java