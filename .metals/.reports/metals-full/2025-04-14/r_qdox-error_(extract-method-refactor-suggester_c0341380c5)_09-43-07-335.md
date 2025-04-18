error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2327.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2327.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2327.java
text:
```scala
a@@ddress.toNode(Util.PROFILE, profileName);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.cli.ArgumentValueConverter;
import org.jboss.as.cli.CliEvent;
import org.jboss.as.cli.CommandArgument;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandHandler;
import org.jboss.as.cli.CommandLineCompleter;
import org.jboss.as.cli.CommandLineException;
import org.jboss.as.cli.ModelNodeFormatter;
import org.jboss.as.cli.OperationCommand;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.impl.ArgumentWithValue;
import org.jboss.as.cli.impl.ArgumentWithoutValue;
import org.jboss.as.cli.impl.DefaultCompleter;
import org.jboss.as.cli.impl.DefaultCompleter.CandidatesProvider;
import org.jboss.as.cli.operation.OperationFormatException;
import org.jboss.as.cli.operation.OperationRequestAddress;
import org.jboss.as.cli.operation.ParsedCommandLine;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestAddress;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestBuilder;
import org.jboss.as.cli.util.SimpleTable;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;


/**
 *
 * @author Alexey Loubyansky
 */
public class GenericTypeOperationHandler extends BatchModeCommandHandler {

    private static final int DASH_OFFSET = 22;

    protected final String commandName;
    protected final String idProperty;
    protected final String nodeType;
    protected final ArgumentWithValue profile;
    protected final ArgumentWithValue name;
    protected final ArgumentWithValue operation;

    protected final Set<String> excludedOps;

    // help arguments
    protected final ArgumentWithoutValue helpProperties;
    protected final ArgumentWithoutValue helpCommands;

    // these are caching vars
    private final Map<String, CommandArgument> staticArgs = new HashMap<String, CommandArgument>();

    private Map<String, ArgumentValueConverter> propConverters;
    private Map<String, CommandLineCompleter> valueCompleters;

    private Map<String, OperationCommandWithDescription> customHandlers;

    private WritePropertyHandler writePropHandler;
    private Map<String, OperationCommand> opHandlers;

    public GenericTypeOperationHandler(CommandContext ctx, String nodeType, String idProperty) {
        this(ctx, nodeType, idProperty, "read-attribute", "read-children-names", "read-children-resources",
                "read-children-types", "read-operation-description", "read-operation-names",
                "read-resource-description", "validate-address", "write-attribute", "undefine-attribute", "whoami");
    }

    public GenericTypeOperationHandler(CommandContext ctx, String nodeType, String idProperty, String... excludeOperations) {

        super(ctx, "generic-type-operation", true);

        if(nodeType == null || nodeType.isEmpty()) {
            throw new IllegalArgumentException("Node type is " + (nodeType == null ? "null." : "empty."));
        }

        if(nodeType.startsWith("/profile=") || nodeType.startsWith("profile=")) {
            int nextSep = nodeType.indexOf('/', 7);
            if(nextSep < 0) {
                throw new IllegalArgumentException("Failed to determine the path after the profile in '" + nodeType + "'.");
            }
            nodeType = nodeType.substring(nextSep);
            this.nodeType = nodeType;
        } else {
            this.nodeType = nodeType;
        }

        helpArg = new ArgumentWithoutValue(this, "--help", "-h");

        addRequiredPath(nodeType);
        this.commandName = getRequiredType();
        if(this.commandName == null) {
            throw new IllegalArgumentException("The node path doesn't end on a type: '" + nodeType + "'");
        }
        this.idProperty = idProperty;

        if(excludeOperations != null) {
            this.excludedOps = new HashSet<String>(Arrays.asList(excludeOperations));
        } else {
            excludedOps = Collections.emptySet();
        }

        profile = new ArgumentWithValue(this, new DefaultCompleter(new CandidatesProvider(){
            @Override
            public List<String> getAllCandidates(CommandContext ctx) {
                return Util.getNodeNames(ctx.getModelControllerClient(), null, Util.PROFILE);
            }}), "--profile") {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                if(!isDependsOnProfile()) {
                    return false;
                }
                if(!ctx.isDomainMode()) {
                    return false;
                }
                return super.canAppearNext(ctx);
            }
        };
        //profile.addCantAppearAfter(helpArg);

        operation = new ArgumentWithValue(this, new DefaultCompleter(new CandidatesProvider(){
                @Override
                public Collection<String> getAllCandidates(CommandContext ctx) {
                    DefaultOperationRequestAddress address = new DefaultOperationRequestAddress();
                    if(isDependsOnProfile() && ctx.isDomainMode()) {
                        final String profileName = profile.getValue(ctx.getParsedCommandLine());
                        if(profileName == null) {
                            return Collections.emptyList();
                        }
                        address.toNode(Util.PROFILE, profileName);
                    }
                    for(OperationRequestAddress.Node node : getRequiredAddress()) {
                        address.toNode(node.getType(), node.getName());
                    }
                    address.toNode(getRequiredType(), "?");
                    Collection<String> ops = Util.getOperationNames(ctx, address);
                    ops.removeAll(excludedOps);
                    if(customHandlers != null) {
                        if(ops.isEmpty()) {
                            ops = customHandlers.keySet();
                        } else {
                            ops = new HashSet<String>(ops);
                            for(Map.Entry<String,OperationCommandWithDescription> entry : customHandlers.entrySet()) {
                                if(entry.getValue().isAvailable(ctx)) {
                                    ops.add(entry.getKey());
                                } else {
                                    ops.remove(entry.getKey()); // in case custom handler overrides the default op
                                }
                            }
                        }
                    }
                    return ops;
                }}), 0, "--operation") {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                if(isDependsOnProfile() && ctx.isDomainMode() && !profile.isValueComplete(ctx.getParsedCommandLine())) {
                    return false;
                }
                return super.canAppearNext(ctx);
            }
        };
        operation.addCantAppearAfter(helpArg);

        name = new ArgumentWithValue(this, new DefaultCompleter(new DefaultCompleter.CandidatesProvider() {
            @Override
            public List<String> getAllCandidates(CommandContext ctx) {
                ModelControllerClient client = ctx.getModelControllerClient();
                if (client == null) {
                    return Collections.emptyList();
                }
                DefaultOperationRequestAddress address = new DefaultOperationRequestAddress();
                if(isDependsOnProfile() && ctx.isDomainMode()) {
                    final String profileName = profile.getValue(ctx.getParsedCommandLine());
                    if(profile == null) {
                        return Collections.emptyList();
                    }
                    address.toNode("profile", profileName);
                }
                for(OperationRequestAddress.Node node : getRequiredAddress()) {
                    address.toNode(node.getType(), node.getName());
                }
                return Util.getNodeNames(ctx.getModelControllerClient(), address, getRequiredType());
                }
            }), (idProperty == null ? "--name" : "--" + idProperty)) {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                if(isDependsOnProfile() && ctx.isDomainMode() && !profile.isValueComplete(ctx.getParsedCommandLine())) {
                    return false;
                }
                return super.canAppearNext(ctx);
            }
        };
        name.addCantAppearAfter(helpArg);

        helpArg.addCantAppearAfter(name);

        helpProperties = new ArgumentWithoutValue(this, "--properties");
        helpProperties.addRequiredPreceding(helpArg);
        helpProperties.addCantAppearAfter(operation);

        helpCommands = new ArgumentWithoutValue(this, "--commands");
        helpCommands.addRequiredPreceding(helpArg);
        helpCommands.addCantAppearAfter(operation);
        helpCommands.addCantAppearAfter(helpProperties);
        helpProperties.addCantAppearAfter(helpCommands);


        ///
        staticArgs.put(helpArg.getFullName(), helpArg);
        staticArgs.put(helpCommands.getFullName(), helpCommands);
        staticArgs.put(helpProperties.getFullName(), helpProperties);
        staticArgs.put(profile.getFullName(), profile);
        staticArgs.put(name.getFullName(), name);
        staticArgs.put(operation.getFullName(), operation);
    }

    public void addValueConverter(String propertyName, ArgumentValueConverter converter) {
        if(propConverters == null) {
            propConverters = new HashMap<String, ArgumentValueConverter>();
        }
        propConverters.put(propertyName, converter);
    }

    public void addValueCompleter(String propertyName, CommandLineCompleter completer) {
        if(valueCompleters == null) {
            valueCompleters = new HashMap<String, CommandLineCompleter>();
        }
        valueCompleters.put(propertyName, completer);
    }

    public void addHandler(String name, OperationCommandWithDescription handler) {
        if(customHandlers == null) {
            customHandlers = Collections.singletonMap(name, handler);
        } else {
            if (customHandlers.size() == 1) {
                final Map<String, OperationCommandWithDescription> tmp = customHandlers;
                customHandlers = new HashMap<String, OperationCommandWithDescription>();
                customHandlers.putAll(tmp);
            }
            customHandlers.put(name, handler);

        }
    }

    @Override
    public CommandArgument getArgument(CommandContext ctx, String name) {
        final ParsedCommandLine args = ctx.getParsedCommandLine();
        try {
            if(!this.name.isValueComplete(args)) {
                return staticArgs.get(name);
            }
        } catch (CommandFormatException e) {
            return null;
        }
        final String op = operation.getValue(args);
        OperationCommand handler;
        try {
            handler = getHandler(ctx, op);
        } catch (CommandLineException e) {
            return null;
        }
        return handler.getArgument(ctx, name);
    }

    @Override
    public Collection<CommandArgument> getArguments(CommandContext ctx) {
        final ParsedCommandLine args = ctx.getParsedCommandLine();
        try {
            if(!name.isValueComplete(args)) {
                return staticArgs.values();
            }
        } catch (CommandFormatException e) {
            return Collections.emptyList();
        }
        final String op = operation.getValue(args);
        OperationCommand handler;
        try {
            handler = getHandler(ctx, op);
        } catch (CommandLineException e) {
            return Collections.emptyList();
        }
        return handler.getArguments(ctx);
    }

    private OperationCommand getHandler(CommandContext ctx, String op) throws CommandLineException {
        if(op == null) {
            if(writePropHandler == null) {
                writePropHandler = new WritePropertyHandler();
                Iterator<AttributeDescription> props = getNodeProperties(ctx);
                while(props.hasNext()) {
                    final AttributeDescription prop = props.next();
                    if(prop.isWriteAllowed()) {
                        ModelType type = null;
                        CommandLineCompleter valueCompleter = null;
                        ArgumentValueConverter valueConverter = null;
                        if(propConverters != null) {
                            valueConverter = propConverters.get(prop.getName());
                        }
                        if(valueCompleters != null) {
                            valueCompleter = valueCompleters.get(prop.getName());
                        }
                        if(valueConverter == null) {
                            valueConverter = ArgumentValueConverter.DEFAULT;
                            final ModelType propType = prop.getType();
                            if(propType != null) {
                                if(ModelType.BOOLEAN == propType) {
                                    if(valueCompleter == null) {
                                        valueCompleter = SimpleTabCompleter.BOOLEAN;
                                    }
                                } else if(prop.getName().endsWith("properties")) { // TODO this is bad but can't rely on proper descriptions
                                    valueConverter = ArgumentValueConverter.PROPERTIES;
                                } else if(ModelType.LIST == type) {
                                    final ModelNode valueType = prop.getProperty(Util.VALUE_TYPE);
                                    if(valueType != null && valueType.asType() == ModelType.PROPERTY) {
                                        valueConverter = ArgumentValueConverter.PROPERTIES;
                                    } else {
                                        valueConverter = ArgumentValueConverter.LIST;
                                    }
                                }
                            }
                        }
                        final CommandArgument arg = new ArgumentWithValue(GenericTypeOperationHandler.this, valueCompleter, valueConverter, "--" + prop.getName());
                        writePropHandler.addArgument(arg);
                    }
                }
            }
            return writePropHandler;
        } else {
            if(customHandlers != null && customHandlers.containsKey(op)) {
                final OperationCommand opHandler = customHandlers.get(op);
                if(opHandler != null) {
                    return opHandler;
                }
            }

            if(opHandlers != null) {
                OperationCommand opHandler = opHandlers.get(op);
                if(opHandler != null) {
                    return opHandler;
                }
            }

            final ModelNode descr = getOperationDescription(ctx, op);
            if(opHandlers == null) {
                opHandlers = new HashMap<String, OperationCommand>();
            }
            final OpHandler opHandler = new OpHandler(op);
            opHandlers.put(op, opHandler);
            opHandler.addArgument(this.headers);

            if(descr != null && descr.has(Util.REQUEST_PROPERTIES)) {
                final List<Property> propList = descr.get(Util.REQUEST_PROPERTIES).asPropertyList();
                for (Property prop : propList) {
                    final ModelNode propDescr = prop.getValue();
                    ModelType type = null;
                    CommandLineCompleter valueCompleter = null;
                    ArgumentValueConverter valueConverter = null;
                    if(propConverters != null) {
                        valueConverter = propConverters.get(prop.getName());
                    }
                    if(valueCompleters != null) {
                        valueCompleter = valueCompleters.get(prop.getName());
                    }
                    if(valueConverter == null) {
                        valueConverter = ArgumentValueConverter.DEFAULT;
                        if(propDescr.has(Util.TYPE)) {
                            type = propDescr.get(Util.TYPE).asType();
                            if(ModelType.BOOLEAN == type) {
                                if(valueCompleter == null) {
                                    valueCompleter = SimpleTabCompleter.BOOLEAN;
                                }
                            } else if(prop.getName().endsWith("properties")) { // TODO this is bad but can't rely on proper descriptions
                                valueConverter = ArgumentValueConverter.PROPERTIES;
                            } else if(ModelType.LIST == type) {
                                if(propDescr.hasDefined(Util.VALUE_TYPE) && propDescr.get(Util.VALUE_TYPE).asType() == ModelType.PROPERTY) {
                                    valueConverter = ArgumentValueConverter.PROPERTIES;
                                } else {
                                    valueConverter = ArgumentValueConverter.LIST;
                                }
                            }
                        }
                    }
                    final CommandArgument arg = new ArgumentWithValue(GenericTypeOperationHandler.this, valueCompleter, valueConverter, "--" + prop.getName());
                    opHandler.addArgument(arg);
                }
            }
            return opHandler;
        }
    }

    @Override
    public boolean hasArgument(CommandContext ctx, String name) {
        return true;
    }

    @Override
    public boolean hasArgument(CommandContext ctx, int index) {
        return true;
    }

    @Override
    public void addArgument(CommandArgument arg) {
    }

    @Override
    protected void recognizeArguments(CommandContext ctx) throws CommandFormatException {
        // argument validation is performed during request construction
    }

    @Override
    public ModelNode buildRequestWithoutHeaders(CommandContext ctx) throws CommandFormatException {
        final String operation = this.operation.getValue(ctx.getParsedCommandLine());
        OperationCommand opHandler;
        try {
            opHandler = getHandler(ctx, operation);
        } catch (CommandFormatException e) {
            throw e;
        } catch (CommandLineException e) {
            throw new CommandFormatException("Command is not supported or unavailable in the current context", e);
        }
        return opHandler.buildRequest(ctx);
    }

    @Override
    public void cliEvent(CliEvent event, CommandContext ctx) {
        super.cliEvent(event, ctx);
        if(event == CliEvent.DISCONNECTED) {
            this.opHandlers = null;
            this.writePropHandler = null;
        }
    }

    @Override
    protected void handleResponse(CommandContext ctx, ModelNode opResponse, boolean composite) throws CommandFormatException {
        //System.out.println(opResponse);
        if (!Util.isSuccess(opResponse)) {
            throw new CommandFormatException(Util.getFailureDescription(opResponse));
        }
        final StringBuilder buf = formatResponse(ctx, opResponse, composite, null);
        if(buf != null) {
            ctx.printLine(buf.toString());
        }
    }

    protected StringBuilder formatResponse(CommandContext ctx, ModelNode opResponse, boolean composite, StringBuilder buf) throws CommandFormatException {
        if(opResponse.hasDefined(Util.RESULT)) {
            final ModelNode result = opResponse.get(Util.RESULT);
            if(composite) {
                final Set<String> keys;
                try {
                    keys = result.keys();
                } catch(Exception e) {
                    throw new CommandFormatException("Failed to get step results from a composite operation response " + opResponse);
                }
                for(String key : keys) {
                    final ModelNode stepResponse = result.get(key);
                    buf = formatResponse(ctx, stepResponse, false, buf); // TODO nested composite ops aren't expected for now
                }
            } else {
                final ModelNodeFormatter formatter = ModelNodeFormatter.Factory.forType(result.getType());
                if(buf == null) {
                    buf = new StringBuilder();
                }
                formatter.format(buf, 0, result);
            }
        }
        if(opResponse.hasDefined(Util.RESPONSE_HEADERS)) {
            final ModelNode headers = opResponse.get(Util.RESPONSE_HEADERS);
            final Set<String> keys = headers.keys();
            final SimpleTable table = new SimpleTable(2);
            for(String key : keys) {
                table.addLine(new String[]{key + ':', headers.get(key).asString()});
            }
            if(buf == null) {
                buf = new StringBuilder();
            } else {
                buf.append(Util.LINE_SEPARATOR);
            }
            table.append(buf, false);
        }
        return buf;
    }

    @Override
    protected void printHelp(CommandContext ctx) throws CommandLineException {

        ParsedCommandLine args = ctx.getParsedCommandLine();
        if(helpProperties.isPresent(args)) {
            printProperties(ctx, getNodeProperties(ctx));
            return;
        }

        if(helpCommands.isPresent(args)) {
            printSupportedCommands(ctx);
            return;
        }

        final String operationName = operation.getValue(args);
        if(operationName == null) {
            printNodeDescription(ctx);
            return;
        }

/*        if(customHandlers != null && customHandlers.containsKey(operationName)) {
            OperationCommand operationCommand = customHandlers.get(operationName);
            operationCommand.handle(ctx);
            return;
        }
*/
        final ModelNode result = getOperationDescription(ctx, operationName);
        if(!result.hasDefined(Util.DESCRIPTION)) {
            throw new CommandLineException("Operation description is not available.");
        }

        ctx.printLine("\nDESCRIPTION:\n");
        formatText(ctx, result.get(Util.DESCRIPTION).asString(), 2);

        if(result.hasDefined(Util.REQUEST_PROPERTIES)) {
            printProperties(ctx, getAttributeIterator(result.get(Util.REQUEST_PROPERTIES).asPropertyList(), null));
        } else {
            printProperties(ctx, Collections.<AttributeDescription>emptyIterator());
        }
    }

    protected void printProperties(CommandContext ctx, Iterator<AttributeDescription> props) {
        final Map<String, StringBuilder> requiredProps = new LinkedHashMap<String,StringBuilder>();
        requiredProps.put(this.name.getFullName(), new StringBuilder().append("Required argument in commands which identifies the instance to execute the command against."));
        final Map<String, StringBuilder> optionalProps = new LinkedHashMap<String, StringBuilder>();

        String accessType = null;
        while(props.hasNext()) {
            AttributeDescription attr = props.next();
            //final ModelNode value = attr.getValue();

            // filter metrics
            accessType = attr.getAccess();
//            if("metric".equals(accessType)) {
//                continue;
//            }

            final boolean required = attr.getBooleanProperty(Util.REQUIRED);
            final StringBuilder descr = new StringBuilder();

            final ModelType modelType = attr.getType();
            final String type = modelType == null? "no type info" : modelType.toString();
            final String attrDescr = attr.getDescription();
            if (attrDescr != null) {
                descr.append('(');
                descr.append(type);
                if(accessType != null) {
                    descr.append(',').append(accessType);
                }
                descr.append(") ");
                descr.append(attrDescr);
            } else if(descr.length() == 0) {
                descr.append("no description.");
            }

            if(required) {
                if(idProperty != null && idProperty.equals(attr.getName())) {
                    if(descr.charAt(descr.length() - 1) != '.') {
                        descr.append('.');
                    }
                    requiredProps.get(this.name.getFullName()).insert(0, ' ').insert(0, descr.toString());
                } else {
                    requiredProps.put("--" + attr.getName(), descr);
                }
            } else {
                optionalProps.put("--" + attr.getName(), descr);
            }
        }

        ctx.printLine("\n");
        if(accessType == null) {
            ctx.printLine("REQUIRED ARGUMENTS:\n");
        }
        for(String argName : requiredProps.keySet()) {
            formatProperty(ctx, argName, requiredProps.get(argName));
        }

        if(!optionalProps.isEmpty()) {
            if(accessType == null ) {
                ctx.printLine("\n\nOPTIONAL ARGUMENTS:\n");
            }
            for(String argName : optionalProps.keySet()) {
                formatProperty(ctx, argName, optionalProps.get(argName));
            }
        }
    }

    protected void printNodeDescription(CommandContext ctx) throws CommandFormatException {

        int offset = 2;

        ctx.printLine("\nSYNOPSIS\n");

        final StringBuilder buf = new StringBuilder();
        buf.append("  ").append(commandName).append(" --help [--properties | --commands] |\n");
        if(isDependsOnProfile() && ctx.isDomainMode()) {
            for(int i = 0; i <= commandName.length() + offset; ++i) {
                buf.append(' ');
            }
            buf.append("--profile=<profile_name>\n");
        }
        for(int i = 0; i <= commandName.length() + offset; ++i) {
            buf.append(' ');
        }
        buf.append('(').append(name.getFullName()).append("=<resource_id> (--<property>=<value>)*) |\n");
        for(int i = 0; i <= commandName.length() + offset; ++i) {
            buf.append(' ');
        }
        buf.append("(<command> ").append(name.getFullName()).append("=<resource_id> (--<parameter>=<value>)*)");

        buf.append('\n');
        for(int i = 0; i <= commandName.length() + offset; ++i) {
            buf.append(' ');
        }
        buf.append("[--headers={<operation_header> (;<operation_header>)*}]");
        ctx.printLine(buf.toString());

        ctx.printLine("\n\nDESCRIPTION\n");

        buf.setLength(0);
        buf.append("The command is used to manage resources of type ");
        buf.append(this.nodeType);
        buf.append(".");
        formatText(ctx, buf, offset);

        ctx.printLine("\n\nRESOURCE DESCRIPTION\n");

        if(isDependsOnProfile() && ctx.isDomainMode() && profile.getValue(ctx.getParsedCommandLine()) == null) {
            buf.setLength(0);
            buf.append("(Execute '");
            buf.append(commandName).append(" --profile=<profile_name> --help' to include the resource description here.)");
            formatText(ctx, buf, offset);
        } else if(ctx.getModelControllerClient() == null) {
            buf.setLength(0);
            buf.append("(Connection to the controller is required to be able to load the resource description)");
            formatText(ctx, buf, offset);
        } else {
            ModelNode request = initRequest(ctx);
            if(request == null) {
                return;
            }
            request.get(Util.OPERATION).set(Util.READ_RESOURCE_DESCRIPTION);
            ModelNode result = null;
            try {
                result = ctx.getModelControllerClient().execute(request);
                if(!result.hasDefined(Util.RESULT)) {
                    throw new CommandFormatException("Node description is not available.");
                }
                result = result.get(Util.RESULT);
                if(!result.hasDefined(Util.DESCRIPTION)) {
                    throw new CommandFormatException("Node description is not available.");
                }
            } catch (Exception e) {
            }

            buf.setLength(0);
            if(result != null) {
                buf.append(result.get(Util.DESCRIPTION).asString());
            } else {
                buf.append("N/A. Please, open a jira issue at https://issues.jboss.org/browse/WFLY to get this fixed. Thanks!");
            }
            formatText(ctx, buf, offset);
        }

        ctx.printLine("\n\nARGUMENTS\n");

        formatProperty(ctx, "--help", "prints this content.");

        formatProperty(ctx, "--help --properties",
                "prints the list of the resource properties including their access-type " +
                "(read/write/metric), value type, and the description.");

        formatProperty(ctx, "--help --commands",
                "prints the list of the commands available for the resource." +
                " To get the complete description of a specific command (including its parameters, " +
                "their types and descriptions), execute " + commandName + " <command> --help.");


        if(isDependsOnProfile() && ctx.isDomainMode()) {
            formatProperty(ctx, "--profile", "the name of the profile the target resource belongs to.");
        }

        buf.setLength(0);
        if(idProperty == null) {
            buf.append("is the name of the resource that completes the path ").append(nodeType).append(" and ");
        } else {
            buf.append("corresponds to a property of the resource which ");
        }
        buf.append("is used to identify the resource against which the command should be executed.");
        formatProperty(ctx, name.getFullName(), buf);

        formatProperty(ctx, "<property>",
                "property name of the resource whose value should be updated. " +
                "For a complete list of available property names, their types and descriptions, execute " +
                commandName + " --help --properties.");

        formatProperty(ctx, "<command>",
                "command name provided by the resource. For a complete list of available commands execute " +
                commandName + " --help --commands.");

        formatProperty(ctx, "<parameter>",
                "parameter name of the <command> provided by the resource. " +
                "For a complete list of available parameter names of a specific <command>, " +
                "their types and descriptions execute " + commandName + " <command> --help.");

        formatProperty(ctx, "--headers",
                "a list of operation headers separated by a semicolon. For the list of supported " +
                "headers, please, refer to the domain management documentation or use tab-completion.");
    }

    protected void formatText(CommandContext ctx, CharSequence text, int offset) {
        int terminalWidth = ctx.getTerminalWidth();
        if(terminalWidth <= 0) {
            terminalWidth = 80;
        }
        final StringBuilder target = new StringBuilder();
        if(offset >= terminalWidth) {
            target.append(text);
        } else {
            int startIndex = 0;
            while(startIndex < text.length()) {
                if(startIndex > 0) {
                    target.append(Util.LINE_SEPARATOR);
                }
                for(int i = 0; i < offset; ++i) {
                    target.append(' ');
                }
                int endIndex = startIndex + terminalWidth - offset;
                if(endIndex > text.length()) {
                    endIndex = text.length();
                    target.append(text.subSequence(startIndex, endIndex));
                    startIndex = endIndex;
                } else {
                    while(endIndex >= startIndex && !Character.isWhitespace(text.charAt(endIndex))) {
                        --endIndex;
                    }
                    if(endIndex <= startIndex) {
                        endIndex = startIndex + terminalWidth - 2;
                        target.append(text.subSequence(startIndex, endIndex));
                        startIndex = endIndex;
                    } else {
                        target.append(text.subSequence(startIndex, endIndex));
                        startIndex = endIndex + 1;
                    }
                }
            }
        }
        ctx.printLine(target.toString());
    }

    protected void formatProperty(CommandContext ctx, String argName, final CharSequence descr) {

        final StringBuilder prop = new StringBuilder();
        prop.append(' ').append(argName);
        int spaces = DASH_OFFSET - prop.length();
        do {
            prop.append(' ');
            --spaces;
        } while(spaces >= 0);

        int terminalWidth = ctx.getTerminalWidth();
        if(terminalWidth <= 0) {
            terminalWidth = 80;
        }

        int dashIndex = prop.length();
        int textOffset = dashIndex + 3;
        int textLength = terminalWidth - textOffset;
        prop.append(" - ");

        if(descr.length() <= textLength) {
            prop.append(descr);
            prop.append(Util.LINE_SEPARATOR);
        } else {
            int lineStart = 0;
            int lineNo = 1;
            while(lineStart < descr.length()) {
                prop.ensureCapacity(terminalWidth);
                if(lineStart > 0) {
                    if(lineNo == 3 && dashIndex > DASH_OFFSET) {
                        textOffset = DASH_OFFSET + 2;
                        textLength = terminalWidth - textOffset;
                    }
                    for(int i = 0; i < textOffset; ++i) {
                        prop.append(' ');
                    }
                }
                int lastCharIndex = lineStart + textLength;

                if(lastCharIndex >= descr.length()) {
                    lastCharIndex = descr.length();
                    prop.append(descr.subSequence(lineStart, lastCharIndex));
                    lineStart = lastCharIndex;
                } else {
                    while(lastCharIndex >= lineStart && !Character.isWhitespace(descr.charAt(lastCharIndex))) {
                        --lastCharIndex;
                    }
                    if(lastCharIndex <= lineStart) {
                        lastCharIndex = lineStart + textLength;
                        prop.append(descr.subSequence(lineStart, lastCharIndex));
                        lineStart = lastCharIndex;
                    } else {
                        prop.append(descr.subSequence(lineStart, lastCharIndex));
                        lineStart = lastCharIndex + 1;
                    }
                }
                prop.append(Util.LINE_SEPARATOR);
                ++lineNo;
            }
        }
        ctx.printLine(prop.toString());
    }

    protected void printSupportedCommands(CommandContext ctx) throws CommandLineException {
        final List<String> list = getSupportedCommands(ctx);
        list.add("To read the description of a specific command execute '" + this.commandName + " command_name --help'.");
        for(String name : list) {
            ctx.printLine(name);
        }
    }

    protected List<String> getSupportedCommands(CommandContext ctx) throws CommandLineException {
        final ModelNode request = initRequest(ctx);
        request.get(Util.OPERATION).set(Util.READ_OPERATION_NAMES);
        if(ctx.getConfig().isAccessControl()) {
            request.get(Util.ACCESS_CONTROL).set(true);
        }
        ModelNode result;
        try {
            result = ctx.getModelControllerClient().execute(request);
        } catch (IOException e) {
            throw new CommandLineException("Failed to load a list of commands.", e);
        }
        if (!result.hasDefined(Util.RESULT)) {
            throw new CommandLineException("Operation names aren't available.");
        }
        final List<ModelNode> nodeList = result.get(Util.RESULT).asList();
        final List<String> supportedCommands = new ArrayList<String>(nodeList.size());
        if(!nodeList.isEmpty()) {
            for(ModelNode node : nodeList) {
                final String opName = node.asString();
                if(!excludedOps.contains(opName) && (customHandlers == null || !customHandlers.containsKey(opName))) {
                    supportedCommands.add(opName);
                }
            }
        }
        if(customHandlers != null) {
            supportedCommands.addAll(customHandlers.keySet());
        }
        Collections.sort(supportedCommands);
        return supportedCommands;
    }

    protected Iterator<AttributeDescription> getNodeProperties(CommandContext ctx) throws CommandLineException {

        if(ctx.getModelControllerClient() == null) {
            throw new CommandLineException("Failed to load target attributes: not connected");
        }
        ModelNode request = initRequest(ctx);
        if(request == null) {
            return Collections.emptyIterator();
        }
        request.get(Util.OPERATION).set(Util.READ_RESOURCE_DESCRIPTION);
        if(ctx.getConfig().isAccessControl()) {
            request.get(Util.ACCESS_CONTROL).set(Util.COMBINED_DESCRIPTIONS);
        }
        ModelNode result;
        try {
            result = ctx.getModelControllerClient().execute(request);
        } catch (IOException e) {
            return Collections.emptyIterator();
        }
        if(!result.hasDefined(Util.RESULT)) {
            return Collections.emptyIterator();
        }
        result = result.get(Util.RESULT);
        if(!result.hasDefined(Util.ATTRIBUTES)) {
            return Collections.emptyIterator();
        }

        final ModelNode accessControl;
        if(ctx.getConfig().isAccessControl()) {
            if(result.has(Util.ACCESS_CONTROL)) {
                accessControl = result.get(Util.ACCESS_CONTROL);
            } else {
                accessControl = null;
            }
        } else {
            accessControl = null;
        }

        return getAttributeIterator(result.get(Util.ATTRIBUTES).asPropertyList(), accessControl);
    }

    protected Iterator<AttributeDescription> getAttributeIterator(final List<Property> props, ModelNode accessControl) {
        final ModelNode attrAccessControl;
        if(accessControl != null) {
            if(accessControl.has(Util.DEFAULT)) {
                final ModelNode def = accessControl.get(Util.DEFAULT);
                if(def.has(Util.ATTRIBUTES)) {
                    attrAccessControl = def.get(Util.ATTRIBUTES);
                } else {
                    attrAccessControl = null;
                }
            } else {
                attrAccessControl = null;
            }
        } else {
            attrAccessControl = null;
        }
        return new Iterator<AttributeDescription>() {

            final Iterator<Property> properties = props.iterator();
            private Property current;
            private AttributeDescription descr = new AttributeDescription() {

                @Override
                public String getName() {
                    return current.getName();
                }

                @Override
                public ModelType getType() {
                    final ModelNode value = getProperty(Util.TYPE);
                    return value == null ? null : value.asType();
                }

                @Override
                public String getAccess() {
                    if(attrAccessControl != null && attrAccessControl.has(current.getName())) {
                        final ModelNode accessSpec = attrAccessControl.get(current.getName());
                        final StringBuilder buf = new StringBuilder();
                        if(accessSpec.get(Util.READ).asBoolean()) {
                            buf.append(Util.READ).append('-');
                        }
                        if(accessSpec.get(Util.WRITE).asBoolean()) {
                            buf.append(Util.WRITE);
                            if(buf.length() == 5) {
                                buf.append("-only");
                            }
                        } else {
                            buf.append("only");
                        }
                        return buf.toString();
                    } else {
                        final ModelNode value = getProperty(Util.ACCESS_TYPE);
                        return value == null ? null : value.asString();
                    }
                }

                @Override
                public boolean isWriteAllowed() {
                    if(attrAccessControl != null && attrAccessControl.has(current.getName())) {
                        final ModelNode accessSpec = attrAccessControl.get(current.getName());
                        if(accessSpec.get(Util.WRITE).asBoolean()) {
                            return true;
                        }
                        return false;
                    }
                    final ModelNode value = getProperty(Util.ACCESS_TYPE);
                    if(value == null) {
                        return false;
                    }
                    return Util.READ_WRITE.equals(value.asString());
                }

                @Override
                public String getDescription() {
                    final ModelNode value = getProperty(Util.DESCRIPTION);
                    return value == null ? null : value.asString();
                }

                @Override
                public ModelNode getProperty(String name) {
                    if(current.getValue().has(name)) {
                        return current.getValue().get(name);
                    }
                    return null;
                }

                @Override
                public boolean getBooleanProperty(String name) {
                    if(current.getValue().has(name)) {
                        return current.getValue().get(name).asBoolean();
                    }
                    return false;
                }
            };
            @Override
            public boolean hasNext() {
                return properties.hasNext();
            }

            @Override
            public AttributeDescription next() {
                current = properties.next();
                return descr;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    interface AttributeDescription {
        String getName();
        ModelType getType();
        String getAccess();
        boolean isWriteAllowed();
        String getDescription();
        ModelNode getProperty(String name);
        boolean getBooleanProperty(String name);
    }

    protected ModelNode getOperationDescription(CommandContext ctx, String operationName) throws CommandLineException {
        if(customHandlers != null) {
            final OperationCommandWithDescription handler = customHandlers.get(operationName);
            if(handler != null) {
                return handler.getOperationDescription(ctx);
            }
        }
        if(ctx.getModelControllerClient() == null) {
            throw new CommandLineException("Failed to load operation description: not connected");
        }

        ModelNode request = initRequest(ctx);
        if(request == null) {
            return null;
        }
        request.get(Util.OPERATION).set(Util.READ_OPERATION_DESCRIPTION);
        request.get(Util.NAME).set(operationName);
        ModelNode result;
        try {
            result = ctx.getModelControllerClient().execute(request);
        } catch (IOException e) {
            throw new CommandFormatException("Failed to execute read-operation-description.", e);
        }
        if (!result.hasDefined(Util.RESULT)) {
            String msg = Util.getFailureDescription(result);
            if(msg == null) {
                msg = "Failed to load description for '" + operationName + "': " + result;
            }
            throw new CommandLineException(msg);
        }
        return result.get(Util.RESULT);
    }

    protected ModelNode initRequest(CommandContext ctx) throws CommandFormatException {
        ModelNode request = new ModelNode();
        ModelNode address = request.get(Util.ADDRESS);
        if(isDependsOnProfile() && ctx.isDomainMode()) {
            final String profileName = profile.getValue(ctx.getParsedCommandLine());
            if(profileName == null) {
                throw new CommandFormatException("WARNING: --profile argument is required for the complete description.");
            }
            address.add(Util.PROFILE, profileName);
        }
        for(OperationRequestAddress.Node node : getRequiredAddress()) {
            address.add(node.getType(), node.getName());
        }
        address.add(getRequiredType(), "?");
        return request;
    }

    private abstract class ActionHandler implements CommandHandler, OperationCommand {

        protected Map<String, CommandArgument> args = Collections.emptyMap();

        void addArgument(CommandArgument arg) {
            if(arg == null) {
                throw new IllegalArgumentException("Argument can't be null.");
            }
            if(args.isEmpty()) {
                args = new HashMap<String, CommandArgument>();
            }
            args.put(arg.getFullName(), arg);
        }
        @Override
        public boolean isAvailable(CommandContext ctx) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isBatchMode(CommandContext ctx) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void handle(CommandContext ctx) throws CommandLineException {
            throw new UnsupportedOperationException();
        }

        @Override
        public CommandArgument getArgument(CommandContext ctx, String name) {
            return args.get(name);
        }

        @Override
        public boolean hasArgument(CommandContext ctx, String name) {
            return args.containsKey(name);
        }

        @Override
        public boolean hasArgument(CommandContext ctx, int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<CommandArgument> getArguments(CommandContext ctx) {
            return args.values();
        }
    }

    private class WritePropertyHandler extends ActionHandler {

        @Override
        public ModelNode buildRequest(CommandContext ctx) throws CommandFormatException {

            final String name = GenericTypeOperationHandler.this.name.getValue(ctx.getParsedCommandLine(), true);

            final ModelNode composite = new ModelNode();
            composite.get(Util.OPERATION).set(Util.COMPOSITE);
            composite.get(Util.ADDRESS).setEmptyList();
            final ModelNode steps = composite.get(Util.STEPS);

            final ParsedCommandLine args = ctx.getParsedCommandLine();

            final String profile;
            if(isDependsOnProfile() && ctx.isDomainMode()) {
                profile = GenericTypeOperationHandler.this.profile.getValue(args);
                if(profile == null) {
                    throw new OperationFormatException("--profile argument value is missing.");
                }
            } else {
                profile = null;
            }

            final Map<String,CommandArgument> nodeProps = this.args;
            for(String argName : args.getPropertyNames()) {
                if(isDependsOnProfile() && argName.equals("--profile") || GenericTypeOperationHandler.this.name.getFullName().equals(argName)) {
                    continue;
                }

                final ArgumentWithValue arg = (ArgumentWithValue) nodeProps.get(argName);
                if(arg == null) {
                    throw new CommandFormatException("Unrecognized argument name '" + argName + "'");
                }

                DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();
                if (profile != null) {
                    builder.addNode(Util.PROFILE, profile);
                }

                for(OperationRequestAddress.Node node : getRequiredAddress()) {
                    builder.addNode(node.getType(), node.getName());
                }
                builder.addNode(getRequiredType(), name);
                builder.setOperationName(Util.WRITE_ATTRIBUTE);
                final String propName;
                if(argName.charAt(1) == '-') {
                    propName = argName.substring(2);
                } else {
                    propName = argName.substring(1);
                }
                builder.addProperty(Util.NAME, propName);

                final String valueString = args.getPropertyValue(argName);
                ModelNode nodeValue = arg.getValueConverter().fromString(ctx, valueString);
                builder.getModelNode().get(Util.VALUE).set(nodeValue);

                steps.add(builder.buildRequest());
            }

            return composite;
        }
    };

    class OpHandler extends ActionHandler {

        private final String opName;

        OpHandler(String opName) {
            super();
            if(opName == null || opName.isEmpty()) {
                throw new IllegalArgumentException("Operation name must a be non-null non-empty string.");
            }
            this.opName = opName;
        }

        @Override
        public ModelNode buildRequest(CommandContext ctx) throws CommandFormatException {
            final ParsedCommandLine parsedArgs = ctx.getParsedCommandLine();

            final ModelNode request = new ModelNode();
            final ModelNode address = request.get(Util.ADDRESS);
            if(isDependsOnProfile() && ctx.isDomainMode()) {
                final String profile = GenericTypeOperationHandler.this.profile.getValue(parsedArgs);
                if(profile == null) {
                    throw new OperationFormatException("Required argument --profile is missing.");
                }
                address.add(Util.PROFILE, profile);
            }

            final String name = GenericTypeOperationHandler.this.name.getValue(ctx.getParsedCommandLine(), true);

            for(OperationRequestAddress.Node node : getRequiredAddress()) {
                address.add(node.getType(), node.getName());
            }
            address.add(getRequiredType(), name);
            request.get(Util.OPERATION).set(opName);

            for(String argName : parsedArgs.getPropertyNames()) {
                if(isDependsOnProfile() && argName.equals("--profile")) {
                    continue;
                }

                if(this.args.isEmpty()) {
                    if(argName.equals(GenericTypeOperationHandler.this.name.getFullName())) {
                        continue;
                    }
                    throw new CommandFormatException("Command '" + operation + "' is not expected to have arguments other than " + GenericTypeOperationHandler.this.name.getFullName() + ".");
                }

                final ArgumentWithValue arg = (ArgumentWithValue) this.args.get(argName);
                if(arg == null) {
                    if(argName.equals(GenericTypeOperationHandler.this.name.getFullName())) {
                        continue;
                    }
                    throw new CommandFormatException("Unrecognized argument " + argName + " for command '" + opName + "'.");
                }

                final String propName;
                if(argName.charAt(1) == '-') {
                    propName = argName.substring(2);
                } else {
                    propName = argName.substring(1);
                }

                final String valueString = parsedArgs.getPropertyValue(argName);
                ModelNode nodeValue = arg.getValueConverter().fromString(ctx, valueString);
                request.get(propName).set(nodeValue);
            }
            return request;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2327.java