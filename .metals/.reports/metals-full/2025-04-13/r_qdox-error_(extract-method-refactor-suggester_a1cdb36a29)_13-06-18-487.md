error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4002.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4002.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4002.java
text:
```scala
c@@tx.error("Type '" + typeName + "' not found among child types of '" + ctx.getPrefixFormatter().format(typeAddress) + "'");

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandHandler;
import org.jboss.as.cli.CommandLineCompleter;
import org.jboss.as.cli.CommandRegistry;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.impl.ArgumentWithValue;
import org.jboss.as.cli.impl.DefaultCompleter;
import org.jboss.as.cli.impl.DefaultCompleter.CandidatesProvider;
import org.jboss.as.cli.operation.OperationRequestAddress;
import org.jboss.as.cli.operation.OperationRequestCompleter;
import org.jboss.as.cli.operation.ParsedCommandLine;
import org.jboss.as.cli.operation.impl.DefaultCallbackHandler;
import org.jboss.as.cli.parsing.ParserUtil;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

/**
 *
 * @author Alexey Loubyansky
 */
public class CommandCommandHandler extends CommandHandlerWithHelp {

    private final ArgumentWithValue action = new ArgumentWithValue(this, new SimpleTabCompleter(new String[]{"add", "list", "remove"}), 0, "--action");
    private final ArgumentWithValue nodePath;
    private final ArgumentWithValue idProperty;
    private final ArgumentWithValue commandName;

    private final CommandRegistry cmdRegistry;

    private DefaultCallbackHandler callback;

    public CommandCommandHandler(CommandRegistry cmdRegistry) {
        super("command", true);
        this.cmdRegistry = cmdRegistry;

        action.addCantAppearAfter(helpArg);

        nodePath = new ArgumentWithValue(this, new CommandLineCompleter(){
            @Override
            public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {
                int offset = 0;
                int result = OperationRequestCompleter.ARG_VALUE_COMPLETER.complete(ctx, buffer, cursor + offset, candidates) - offset;
                if(result < 0) {
                    return result;
                }
                return result;
            }}, "--node-type") {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                return "add".equals(action.getValue(ctx.getParsedCommandLine())) && super.canAppearNext(ctx);
            }
        };

        idProperty = new ArgumentWithValue(this, new DefaultCompleter(new CandidatesProvider(){
            @Override
            public List<String> getAllCandidates(CommandContext ctx) {
                List<Property> props = getNodeProperties(ctx);
                if(props.isEmpty()) {
                    return Collections.emptyList();
                }

                final List<String> candidates = new ArrayList<String>();
                for(Property prop : props) {
                    final ModelNode value = prop.getValue();
                    if(value.has("access-type") && "read-only".equals(value.get("access-type").asString())) {
                        candidates.add(prop.getName());
                    }
                }
                return candidates;
            }}), "--property-id");
        idProperty.addRequiredPreceding(nodePath);

        commandName = new ArgumentWithValue(this, new DefaultCompleter(new CandidatesProvider(){

            private final DefaultCallbackHandler callback = new DefaultCallbackHandler();

            @Override
            public List<String> getAllCandidates(CommandContext ctx) {

                final String actionName = action.getValue(ctx.getParsedCommandLine());
                if(actionName == null) {
                    return Collections.emptyList();
                }

                if (actionName.equals("add")) {
                   final String thePath = nodePath.getValue(ctx.getParsedCommandLine());
                   if (thePath == null) {
                      return Collections.emptyList();
                   }

                   callback.reset();
                   try {
                       ParserUtil.parseOperationRequest(thePath, callback);
                   } catch (CommandFormatException e) {
                       return Collections.emptyList();
                   }

                   OperationRequestAddress typeAddress = callback.getAddress();
                   if (!typeAddress.endsOnType()) {
                       return Collections.emptyList();
                   }
                   return Collections.singletonList(typeAddress.getNodeType());
               }

                if (actionName.equals("remove")) {
                    return getExistingCommands();
                }
                return Collections.emptyList();
            }}), "--command-name") {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                ParsedCommandLine args = ctx.getParsedCommandLine();
                if(isPresent(args)) {
                    return false;
                }
                final String actionStr = action.getValue(args);
                if(actionStr == null) {
                    return false;
                }
                if("add".equals(actionStr)) {
                    return nodePath.isValueComplete(args);
                }
                if("remove".equals(actionStr)) {
                    return true;
                }
                return false;
            }
        };
    }

    /* (non-Javadoc)
     * @see org.jboss.as.cli.handlers.CommandHandlerWithHelp#doHandle(org.jboss.as.cli.CommandContext)
     */
    @Override
    protected void doHandle(CommandContext ctx) throws CommandFormatException {

        final ParsedCommandLine args = ctx.getParsedCommandLine();
        final String action = this.action.getValue(args);
        if(action == null) {
            ctx.error("Command is missing.");
            return;
        }

        if(action.equals("list")) {
            ctx.printColumns(getExistingCommands());
            return;
        }

        if(action.equals("add")) {
            final String nodePath = this.nodePath.getValue(args, true);
            final String propName = this.idProperty.getValue(args, false);
            final String cmdName = this.commandName.getValue(args, true);

            if(!validateInput(ctx, nodePath, propName)) {
                return;
            }

            if(cmdRegistry.getCommandHandler(cmdName) != null) {
                ctx.error("Command '" + cmdName + "' already registered.");
                return;
            }
            cmdRegistry.registerHandler(new GenericTypeOperationHandler(ctx, nodePath, propName), cmdName);
            return;
        }

        if(action.equals("remove")) {
            final String cmdName = this.commandName.getValue(args, true);
            CommandHandler handler = cmdRegistry.getCommandHandler(cmdName);
            if(!(handler instanceof GenericTypeOperationHandler)) {
                ctx.error("Command '" + cmdName + "' is not a generic type command.");
                return;
            }
            cmdRegistry.remove(cmdName);
            return;
        }

        ctx.error("Unexpected action: " + action);
    }

    protected List<String> getExistingCommands() {
        final List<String> commands = new ArrayList<String>();
        for(String cmd : cmdRegistry.getTabCompletionCommands()) {
            if(cmdRegistry.getCommandHandler(cmd) instanceof GenericTypeOperationHandler) {
                commands.add(cmd);
            }
        }
        return commands;
    }

    protected List<Property> getNodeProperties(CommandContext ctx) {
        ModelNode request = initRequest(ctx);
        if(request == null) {
            return Collections.emptyList();
        }
        request.get(Util.OPERATION).set(Util.READ_RESOURCE_DESCRIPTION);
        ModelNode result;
        try {
            result = ctx.getModelControllerClient().execute(request);
        } catch (IOException e) {
            return Collections.emptyList();
        }
        if(!result.hasDefined(Util.RESULT)) {
            return Collections.emptyList();
        }
        result = result.get(Util.RESULT);
        if(!result.hasDefined(Util.ATTRIBUTES)) {
            return Collections.emptyList();
        }
        return result.get(Util.ATTRIBUTES).asPropertyList();
    }

    protected ModelNode initRequest(CommandContext ctx) {
        ModelNode request = new ModelNode();
        ModelNode address = request.get(Util.ADDRESS);

        final String type = nodePath.getValue(ctx.getParsedCommandLine());
        if(callback == null) {
            callback = new DefaultCallbackHandler();
        } else {
            callback.reset();
        }
        try {
            ParserUtil.parseOperationRequest(type, callback);
        } catch (CommandFormatException e) {
            throw new IllegalArgumentException("Failed to parse nodeType: " + e.getMessage());
        }

        OperationRequestAddress typeAddress = callback.getAddress();
        if(!typeAddress.endsOnType()) {
            return null;
        }

        final String typeName = typeAddress.toParentNode().getType();
        for(OperationRequestAddress.Node node : typeAddress) {
            address.add(node.getType(), node.getName());
        }
        address.add(typeName, "?");
        return request;
    }

    protected boolean validateInput(CommandContext ctx, String typePath, String propertyName) {

        ModelNode request = new ModelNode();
        ModelNode address = request.get(Util.ADDRESS);

        if(callback == null) {
            callback = new DefaultCallbackHandler();
        } else {
            callback.reset();
        }

        try {
            ParserUtil.parseOperationRequest(typePath, callback);
        } catch (CommandFormatException e) {
            ctx.error("Failed to validate input: " + e.getLocalizedMessage());
            return false;
        }

        OperationRequestAddress typeAddress = callback.getAddress();
        if(!typeAddress.endsOnType()) {
            ctx.error("Node path '" + typePath + "' doesn't appear to end on a type.");
            return false;
        }

        final String typeName = typeAddress.toParentNode().getType();
        for(OperationRequestAddress.Node node : typeAddress) {
            address.add(node.getType(), node.getName());
        }

        request.get(Util.OPERATION).set(Util.READ_CHILDREN_TYPES);

        ModelNode result;
        try {
            result = ctx.getModelControllerClient().execute(request);
        } catch (IOException e) {
            ctx.error("Failed to validate input: " + e.getLocalizedMessage());
            return false;
        }
        if(!result.hasDefined(Util.RESULT)) {
            ctx.error("Failed to validate input: operation response doesn't contain result info.");
            return false;
        }

        boolean pathValid = false;
        for(ModelNode typeNode : result.get(Util.RESULT).asList()) {
            if(typeNode.asString().equals(typeName)) {
                pathValid = true;
                break;
            }
        }
        if(!pathValid) {
            ctx.error("Type '" + typeName + "' not found amoung child types of '" + ctx.getPrefixFormatter().format(typeAddress) + "'");
            return false;
        }

        address.add(typeName, "?");
        request.get(Util.OPERATION).set(Util.READ_RESOURCE_DESCRIPTION);

        try {
            result = ctx.getModelControllerClient().execute(request);
        } catch (IOException e) {
            ctx.error(e.getLocalizedMessage());
            return false;
        }
        if(!result.hasDefined(Util.RESULT)) {
            ctx.error("Failed to validate input: operation response doesn't contain result info.");
            return false;
        }
        result = result.get(Util.RESULT);
        if(!result.hasDefined("attributes")) {
            ctx.error("Failed to validate input: description of attributes is missing for " + typePath);
            return false;
        }

        if(propertyName != null) {
            for(Property prop : result.get(Util.ATTRIBUTES).asPropertyList()) {
                if(prop.getName().equals(propertyName)) {
                    ModelNode value = prop.getValue();
                    if(value.has(Util.ACCESS_TYPE) && Util.READ_ONLY.equals(value.get(Util.ACCESS_TYPE).asString())) {
                        return true;
                    }
                    ctx.error("Property " + propertyName + " is not read-only.");
                    return false;
                }
            }
        } else {
            return true;
        }
        ctx.error("Property '" + propertyName + "' wasn't found among the properties of " + typePath);
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4002.java