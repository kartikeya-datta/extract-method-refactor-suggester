error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6387.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6387.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6387.java
text:
```scala
i@@f(deployment.hasDefined(PERSISTENT) && !deployment.get(PERSISTENT).asBoolean()) {

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

package org.jboss.as.controller.parsing;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PERSISTENT;
import org.jboss.as.controller.operations.common.Util;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoAttributes;
import org.jboss.as.controller.persistence.ModelMarshallingContext;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.logging.Logger;
import org.jboss.modules.ModuleLoader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CONTENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEFAULT_INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ENABLED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.EXTENSION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT_INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PATH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PORT_OFFSET;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROFILE_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNTIME_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SYSTEM_PROPERTY;
import static org.jboss.as.controller.parsing.ParseUtils.isNoNamespaceAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequired;
import static org.jboss.as.controller.parsing.ParseUtils.nextElement;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;

/**
 * A mapper between an AS server's configuration model and XML representations, particularly {@code standalone.xml}.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class StandaloneXml extends CommonXml {

    public StandaloneXml(final ModuleLoader loader) {
        super(loader);
    }

    @Override
    public void readElement(final XMLExtendedStreamReader reader, final List<ModelNode> operationList) throws XMLStreamException {
        final ModelNode address = new ModelNode().setEmptyList();
        if (Namespace.forUri(reader.getNamespaceURI()) != Namespace.DOMAIN_1_0 || Element.forName(reader.getLocalName()) != Element.SERVER) {
            throw unexpectedElement(reader);
        }
        readServerElement(reader, address, operationList);
    }

    private void readServerElement(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> list) throws XMLStreamException {

        parseNamespaces(reader, address, list);

        String serverName = null;

        // attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i ++) {
            switch (Namespace.forUri(reader.getAttributeNamespace(i))) {
                case NONE: {
                    final String value = reader.getAttributeValue(i);
                    final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                    switch (attribute) {
                        case NAME: {
                            serverName = value;
                            break;
                        }
                        default: throw unexpectedAttribute(reader, i);
                    }
                    break;
                }
                case XML_SCHEMA_INSTANCE: {
                    switch (Attribute.forName(reader.getAttributeLocalName(i))) {
                        case SCHEMA_LOCATION: {
                            parseSchemaLocations(reader, address, list, i);
                            break;
                        }
                        case NO_NAMESPACE_SCHEMA_LOCATION: {
                            // todo, jeez
                            break;
                        }
                        default: {
                            throw unexpectedAttribute(reader, i);
                        }
                    }
                    break;
                }
                default: throw unexpectedAttribute(reader, i);
            }
        }

        if (serverName == null) {
            serverName = getDefaultName();
        }
        setServerName(address, list, serverName);

        // elements - sequence

        Element element = nextElement(reader);
        if (element == Element.EXTENSIONS) {
            parseExtensions(reader, address, list);
            element = nextElement(reader);
        }
        // System properties
        if (element == Element.SYSTEM_PROPERTIES) {
            parseSystemProperties(reader, address, list, true);
            element = nextElement(reader);
        }
        if (element == Element.PATHS) {
            parsePaths(reader, address, list, true);
            element = nextElement(reader);
        }
        if (element == Element.MANAGEMENT) {
            parseManagement(reader, address, list);
            element = nextElement(reader);
        }
        if (element == Element.MANAGEMENT_INTERFACES) {
            parseManagementInterfaces(reader, address, list);
            element = nextElement(reader);
        }
        // Single profile
        if (element == Element.PROFILE) {
            parseServerProfile(reader, address, list);
            element = nextElement(reader);
        }
        // Interfaces
        final Set<String> interfaceNames = new HashSet<String>();
        if (element == Element.INTERFACES) {
            parseInterfaces(reader, interfaceNames, address, list, true);
            element = nextElement(reader);
        }
        // Single socket binding group
        if (element == Element.SOCKET_BINDING_GROUP) {
            parseSocketBindingGroup(reader, interfaceNames, address, list);
            element = nextElement(reader);
        }
        if (element == Element.DEPLOYMENTS) {
            parseDeployments(reader, address, list, true);
            element = nextElement(reader);
        }
        if (element != null) {
            throw unexpectedElement(reader);
        }

//        for (;;) {
//            switch (reader.nextTag()) {
//                case START_ELEMENT: {
//                    readHeadComment(reader, address, list);
//                    if (Namespace.forUri(reader.getNamespaceURI()) != Namespace.DOMAIN_1_0) {
//                        throw unexpectedElement(reader);
//                    }
//                    switch (Element.forName(reader.getLocalName())) {
//                        default: throw unexpectedElement(reader);
//                    }
//                }
//                case END_ELEMENT: {
//                    readTailComment(reader, address, list);
//                    return;
//                }
//                default: throw new IllegalStateException();
//            }
//        }
    }

    private void parseSocketBindingGroup(final XMLExtendedStreamReader reader, final Set<String> interfaces, final ModelNode address, final List<ModelNode> updates) throws XMLStreamException {
        final Set<String> socketBindings = new HashSet<String>();

        // Handle attributes
        String name = null;
        String defaultInterface = null;
        String portOffset = null;

        final EnumSet<Attribute> required = EnumSet.of(Attribute.NAME, Attribute.DEFAULT_INTERFACE);
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i ++) {
            final String value = reader.getAttributeValue(i);
            if (!isNoNamespaceAttribute(reader, i)) {
                throw unexpectedAttribute(reader, i);
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case NAME: {
                    name = value;
                    required.remove(attribute);
                    break;
                }
                case DEFAULT_INTERFACE: {
                    defaultInterface = value;
                    required.remove(attribute);
                    break;
                }
                case PORT_OFFSET: {
                    portOffset = value;
                    try {
                        int offset = Integer.parseInt(value);
                        if (offset < 0) {
                            throw new XMLStreamException(portOffset + " is not a valid " +
                                    attribute.getLocalName() + " -- must be greater than zero",
                                    reader.getLocation());
                        }
                    } catch (final NumberFormatException e) {
                        if (!Util.isExpression(value)) {
                            throw new XMLStreamException(portOffset + " is not a valid " +
                                    attribute.getLocalName(), reader.getLocation(), e);
                        }
                    }
                    break;
                }
                default:
                    throw ParseUtils.unexpectedAttribute(reader, i);
            }
        }

        if (! required.isEmpty()) {
            throw missingRequired(reader, required);
        }

        ModelNode groupAddress = address.clone().add(SOCKET_BINDING_GROUP, name);
        ModelNode op = Util.getEmptyOperation(ADD, groupAddress);
        op.get(DEFAULT_INTERFACE).set(defaultInterface);
        op.get(PORT_OFFSET).set(portOffset == null ? "0" : portOffset);

        updates.add(op);

        // Handle elements
        while (reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case DOMAIN_1_0: {
                    final Element element = Element.forName(reader.getLocalName());
                    switch (element) {
                        case SOCKET_BINDING: {
                            // FIXME JBAS-8825
                            final String bindingName = parseSocketBinding(reader, interfaces, groupAddress, defaultInterface, updates);
                            if (socketBindings.contains(bindingName)) {
                                throw new XMLStreamException("socket-binding " + bindingName + " already declared", reader.getLocation());
                            }
                            socketBindings.add(bindingName);
                            break;
                        }
                        default:
                            throw unexpectedElement(reader);
                    }
                    break;
                }
                default:
                    throw unexpectedElement(reader);
            }
        }
    }

    private void parseServerProfile(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> list) throws XMLStreamException {
        // Attributes
        requireNoAttributes(reader);

        // Content
        final Set<String> configuredSubsystemTypes = new HashSet<String>();
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            if (Namespace.forUri(reader.getNamespaceURI()) != Namespace.UNKNOWN) {
                throw unexpectedElement(reader);
            }
            if (Element.forName(reader.getLocalName()) != Element.SUBSYSTEM) {
                throw unexpectedElement(reader);
            }
            if (!configuredSubsystemTypes.add(reader.getNamespaceURI())) {
                throw new XMLStreamException("Duplicate subsystem declaration", reader.getLocation());
            }
            // parse subsystem
            final List<ModelNode> subsystems = new ArrayList<ModelNode>();
            reader.handleAny(subsystems);

            // Process subsystems
            for(final ModelNode update : subsystems) {
                // TODO remove logging
                if(! update.has(OP_ADDR)) {
                    Logger.getLogger("missing address").error(update);
                }
                // Process relative subsystem path address
                final ModelNode subsystemAddress = address.clone();
                for(final Property path : update.get(OP_ADDR).asPropertyList()) {
                    subsystemAddress.add(path.getName(), path.getValue().asString());
                }
                update.get(OP_ADDR).set(subsystemAddress);
                list.add(update);
            }
        }
    }

    private void setServerName(final ModelNode address, final List<ModelNode> operationList, final String value) {
        if (value.length() > 0) {
            final ModelNode update = Util.getWriteAttributeOperation(address, NAME, value);
            operationList.add(update);
        }
    }

    @Override
    public void writeContent(final XMLExtendedStreamWriter writer, final ModelMarshallingContext context) throws XMLStreamException {

        ModelNode modelNode = context.getModelNode();
        writer.writeStartDocument();
        writer.writeStartElement(Element.SERVER.getLocalName());

        if (modelNode.hasDefined(NAME)) {
            writeAttribute(writer, Attribute.NAME, modelNode.get(NAME).asString());
        }

        writer.writeDefaultNamespace(Namespace.CURRENT.getUriString());
        writeNamespaces(writer, modelNode);
        writeSchemaLocation(writer, modelNode);

        if (modelNode.hasDefined(EXTENSION)) {
            writeExtensions(writer, modelNode.get(EXTENSION));
        }

        if (modelNode.hasDefined(SYSTEM_PROPERTY)) {
            writeProperties(writer, modelNode.get(SYSTEM_PROPERTY), Element.SYSTEM_PROPERTIES, true);
        }

        if(modelNode.hasDefined(PATH)) {
            writePaths(writer, modelNode.get(PATH));
        }

        writeManagement(writer, modelNode.get(MANAGEMENT));

        if (modelNode.hasDefined(MANAGEMENT_INTERFACE)) {
            writeManagementInterfaces(writer, modelNode.get(MANAGEMENT_INTERFACE));
        }

        writeServerProfile(writer, context);

        if (modelNode.hasDefined(INTERFACE)) {
            writeInterfaces(writer, modelNode.get(INTERFACE));
        }

        if (modelNode.hasDefined(SOCKET_BINDING_GROUP)) {
            Set<String> groups = modelNode.get(SOCKET_BINDING_GROUP).keys();
            if (groups.size() > 1) {
                throw new IllegalStateException(String.format("Model contains multiple %s nodes", SOCKET_BINDING_GROUP));
            }
            for (String group : groups) {
                writeSocketBindingGroup(writer, modelNode.get(SOCKET_BINDING_GROUP, group), true);
            }
        }

        if (modelNode.hasDefined(DEPLOYMENT)) {
            writeServerDeployments(writer, modelNode.get(DEPLOYMENT));
        }

        writer.writeEndElement();
        writer.writeEndDocument();
    }

    private void writeServerDeployments(final XMLExtendedStreamWriter writer, final ModelNode modelNode)
            throws XMLStreamException {

        Set<String> deploymentNames = modelNode.keys();
        if (deploymentNames.size() > 0) {
            boolean deploymentWritten = false;
            for (String uniqueName : deploymentNames) {
                final ModelNode deployment = modelNode.get(uniqueName);
                if(!deployment.get(PERSISTENT).asBoolean()) {
                    continue;
                }
                if(!deploymentWritten) {
                    writer.writeStartElement(Element.DEPLOYMENTS.getLocalName());
                    deploymentWritten = true;
                }
                final String runtimeName = deployment.get(RUNTIME_NAME).asString();
                boolean enabled = deployment.get(ENABLED).asBoolean();
                writer.writeStartElement(Element.DEPLOYMENT.getLocalName());
                writeAttribute(writer, Attribute.NAME, uniqueName);
                writeAttribute(writer, Attribute.RUNTIME_NAME, runtimeName);
                if (!enabled) {
                    writeAttribute(writer, Attribute.ENABLED, "false");
                }
                final List<ModelNode> contentItems = deployment.require(CONTENT).asList();
                for (ModelNode contentItem : contentItems) {
                    writeContentItem(writer, contentItem);
                }
                writer.writeEndElement();
            }
            if(deploymentWritten) {
                writer.writeEndElement();
            }
        }
    }

    private void writeServerProfile(final XMLExtendedStreamWriter writer, final ModelMarshallingContext context) throws XMLStreamException {

        ModelNode profileNode = context.getModelNode();

        writer.writeStartElement(Element.PROFILE.getLocalName());
        Set<String> subsystemNames = profileNode.get(SUBSYSTEM).keys();
        if (subsystemNames.size() > 0) {
            String defaultNamespace = writer.getNamespaceContext().getNamespaceURI(XMLConstants.DEFAULT_NS_PREFIX);
            for (String subsystemName : subsystemNames) {
                try {
                    ModelNode subsystem = profileNode.get(SUBSYSTEM, subsystemName);
                    XMLElementWriter<SubsystemMarshallingContext> subsystemWriter = context.getSubsystemWriter(subsystemName);
                    if (subsystemWriter != null) { // FIXME -- remove when extensions are doing the registration
                        subsystemWriter.writeContent(writer, new SubsystemMarshallingContext(subsystem, writer));
                    }
                }
                finally {
                    writer.setDefaultNamespace(defaultNamespace);
                }
            }
        }
        writer.writeEndElement();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6387.java