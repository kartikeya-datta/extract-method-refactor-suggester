error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9707.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9707.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9707.java
text:
```scala
f@@or (AttributeDefinition attribute : BridgeDefinition.ATTRIBUTES) {

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

package org.jboss.as.messaging;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequired;
import static org.jboss.as.controller.parsing.ParseUtils.parsePossibleExpression;
import static org.jboss.as.controller.parsing.ParseUtils.readStringAttributeElement;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoAttributes;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoContent;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoNamespaceAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.requireSingleAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedEndElement;
import static org.jboss.as.messaging.CommonAttributes.ACCEPTOR;
import static org.jboss.as.messaging.CommonAttributes.ADDRESS_SETTING;
import static org.jboss.as.messaging.CommonAttributes.BINDINGS_DIRECTORY;
import static org.jboss.as.messaging.CommonAttributes.BROADCAST_GROUP;
import static org.jboss.as.messaging.CommonAttributes.CONNECTION_FACTORY;
import static org.jboss.as.messaging.CommonAttributes.CONNECTOR;
import static org.jboss.as.messaging.CommonAttributes.CONNECTORS;
import static org.jboss.as.messaging.CommonAttributes.DEFAULT;
import static org.jboss.as.messaging.CommonAttributes.DISCOVERY_GROUP;
import static org.jboss.as.messaging.CommonAttributes.DISCOVERY_GROUP_NAME;
import static org.jboss.as.messaging.CommonAttributes.DISCOVERY_GROUP_REF;
import static org.jboss.as.messaging.CommonAttributes.DIVERT;
import static org.jboss.as.messaging.CommonAttributes.DURABLE;
import static org.jboss.as.messaging.CommonAttributes.ENTRIES;
import static org.jboss.as.messaging.CommonAttributes.FACTORY_CLASS;
import static org.jboss.as.messaging.CommonAttributes.FILTER;
import static org.jboss.as.messaging.CommonAttributes.GROUPING_HANDLER;
import static org.jboss.as.messaging.CommonAttributes.HORNETQ_SERVER;
import static org.jboss.as.messaging.CommonAttributes.INBOUND_CONFIG;
import static org.jboss.as.messaging.CommonAttributes.IN_VM_ACCEPTOR;
import static org.jboss.as.messaging.CommonAttributes.IN_VM_CONNECTOR;
import static org.jboss.as.messaging.CommonAttributes.JMS_BRIDGE;
import static org.jboss.as.messaging.CommonAttributes.JMS_CONNECTION_FACTORIES;
import static org.jboss.as.messaging.CommonAttributes.JMS_DESTINATIONS;
import static org.jboss.as.messaging.CommonAttributes.JMS_QUEUE;
import static org.jboss.as.messaging.CommonAttributes.JMS_TOPIC;
import static org.jboss.as.messaging.CommonAttributes.LIVE_CONNECTOR_REF;
import static org.jboss.as.messaging.CommonAttributes.LOCAL_TX;
import static org.jboss.as.messaging.CommonAttributes.PARAM;
import static org.jboss.as.messaging.CommonAttributes.PARAMS;
import static org.jboss.as.messaging.CommonAttributes.PATH;
import static org.jboss.as.messaging.CommonAttributes.PCF_PASSWORD;
import static org.jboss.as.messaging.CommonAttributes.PCF_USER;
import static org.jboss.as.messaging.CommonAttributes.POOLED_CONNECTION_FACTORY;
import static org.jboss.as.messaging.CommonAttributes.QUEUE_ADDRESS;
import static org.jboss.as.messaging.CommonAttributes.RELATIVE_TO;
import static org.jboss.as.messaging.CommonAttributes.REMOTE_ACCEPTOR;
import static org.jboss.as.messaging.CommonAttributes.REMOTE_CONNECTOR;
import static org.jboss.as.messaging.CommonAttributes.REMOTING_INTERCEPTORS;
import static org.jboss.as.messaging.CommonAttributes.ROLE;
import static org.jboss.as.messaging.CommonAttributes.SECURITY_SETTING;
import static org.jboss.as.messaging.CommonAttributes.SELECTOR;
import static org.jboss.as.messaging.CommonAttributes.SERVER_ID;
import static org.jboss.as.messaging.CommonAttributes.SOCKET_BINDING;
import static org.jboss.as.messaging.CommonAttributes.STATIC_CONNECTORS;
import static org.jboss.as.messaging.CommonAttributes.SUBSYSTEM;
import static org.jboss.as.messaging.CommonAttributes.TRANSACTION;
import static org.jboss.as.messaging.MessagingMessages.MESSAGES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ControllerMessages;
import org.jboss.as.controller.ListAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.messaging.jms.JndiEntriesAttribute;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * The messaging subsystem domain parser
 *
 * @author scott.stark@jboss.org
 * @author Emanuel Muckenhuber
 * @author <a href="mailto:andy.taylor@jboss.com">Andy Taylor</a>
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class MessagingSubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

    private static final MessagingSubsystemParser INSTANCE = new MessagingSubsystemParser();

    public static MessagingSubsystemParser getInstance() {
        return INSTANCE;
    }

    private static final char[] NEW_LINE = new char[]{'\n'};

    private static final EnumSet<Element> SIMPLE_ROOT_RESOURCE_ELEMENTS = EnumSet.noneOf(Element.class);

    static {
        for (AttributeDefinition attr : CommonAttributes.SIMPLE_ROOT_RESOURCE_ATTRIBUTES) {
            SIMPLE_ROOT_RESOURCE_ELEMENTS.add(Element.forName(attr.getXmlName()));
        }
    }

    protected MessagingSubsystemParser() {
        //
    }


    public void readElement(final XMLExtendedStreamReader reader, final List<ModelNode> list) throws XMLStreamException {
        final ModelNode address = new ModelNode();
        address.add(SUBSYSTEM, MessagingExtension.SUBSYSTEM_NAME);
        address.protect();

        final ModelNode subsystemAdd = new ModelNode();
        subsystemAdd.get(OP).set(ADD);
        subsystemAdd.get(OP_ADDR).set(address);
        list.add(subsystemAdd);

        final Namespace schemaVer = Namespace.forUri(reader.getNamespaceURI());
        switch (schemaVer) {
            case MESSAGING_1_0:
                processHornetQServer(reader, address, list, schemaVer);
                break;
            case MESSAGING_1_1:
            case MESSAGING_1_2:
            case MESSAGING_1_3:
                processHornetQServers(reader, address, list);
                break;
            default:
                throw unexpectedElement(reader);
        }

    }

    protected void processHornetQServers(final XMLExtendedStreamReader reader, final ModelNode subsystemAddress, final List<ModelNode> list) throws XMLStreamException {
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Namespace schemaVer = Namespace.forUri(reader.getNamespaceURI());
            switch (schemaVer) {
                case MESSAGING_1_0:
                case UNKNOWN:
                    throw ParseUtils.unexpectedElement(reader);
                default: {
                    final Element element = Element.forName(reader.getLocalName());
                    switch (element) {
                        case HORNETQ_SERVER:
                            processHornetQServer(reader, subsystemAddress, list, schemaVer);
                            break;
                        default:
                            throw ParseUtils.unexpectedElement(reader);
                    }
                }
            }
        }
    }

    protected void processHornetQServer(final XMLExtendedStreamReader reader, final ModelNode subsystemAddress, final List<ModelNode> list, Namespace namespace) throws XMLStreamException {

        String hqServerName = null;
        String elementName = null;
        switch (namespace) {
            case MESSAGING_1_0:
                // We're parsing the 1.0 xsd's <subsystem> element
                requireNoAttributes(reader);
                elementName = ModelDescriptionConstants.SUBSYSTEM;
                break;
            default: {
                final int count = reader.getAttributeCount();
                if (count > 0) {
                    requireSingleAttribute(reader, Attribute.NAME.getLocalName());
                    hqServerName = reader.getAttributeValue(0).trim();
                }
                elementName = CommonAttributes.HORNETQ_SERVER;
            }
        }

        if (hqServerName == null || hqServerName.length() == 0) {
            hqServerName = DEFAULT;
        }

        final ModelNode address = subsystemAddress.clone();
        address.add(HORNETQ_SERVER, hqServerName);
        address.protect();

        final ModelNode operation = new ModelNode();
        operation.get(OP).set(ADD);
        operation.get(OP_ADDR).set(address);
        list.add(operation);

        EnumSet<Element> seen = EnumSet.noneOf(Element.class);
        // Handle elements
        int tag = reader.getEventType();
        String localName = null;
        do {
            tag = reader.nextTag();
            localName = reader.getLocalName();
            final Element element = Element.forName(reader.getLocalName());
            if (!seen.add(element)) {
                throw ParseUtils.duplicateNamedElement(reader, element.getLocalName());
            }

            switch (element) {
                case ACCEPTORS:
                    processAcceptors(reader, address, list);
                    break;
                case ADDRESS_SETTINGS:
                    processAddressSettings(reader, address, list);
                    break;
                case BINDINGS_DIRECTORY:
                    parseDirectory(reader, CommonAttributes.BINDINGS_DIRECTORY, address, list);
                    break;
                case BRIDGES:
                    processBridges(reader, address, list);
                    break;
                case BROADCAST_GROUPS:
                    processBroadcastGroups(reader, address, list);
                    break;
                case CLUSTER_CONNECTIONS:
                    processClusterConnections(reader, address, list);
                    break;
                case CONNECTORS:
                    processConnectors(reader, address, list);
                    break;
                case CONNECTOR_SERVICES:
                    processConnectorServices(reader, address, list);
                    break;
                case DISCOVERY_GROUPS:
                    processDiscoveryGroups(reader, address, list);
                    break;
                case DIVERTS:
                    parseDiverts(reader, address, list);
                    break;
                case FILE_DEPLOYMENT_ENABLED:
                    // This isn't an element in the xsd as there is no filesystem support in AS
                    unhandledElement(reader, element);
                    break;
                case GROUPING_HANDLER:
                    processGroupingHandler(reader, address, list);
                    break;
                case JOURNAL_DIRECTORY:
                    parseDirectory(reader, CommonAttributes.JOURNAL_DIRECTORY, address, list);
                    break;
                case LARGE_MESSAGES_DIRECTORY:
                    parseDirectory(reader, CommonAttributes.LARGE_MESSAGES_DIRECTORY, address, list);
                    break;
                case LIVE_CONNECTOR_REF: {
                    String string = readStringAttributeElement(reader, CommonAttributes.CONNECTOR_NAME);
                    LIVE_CONNECTOR_REF.parseAndSetParameter(string, operation, reader);
                    break;
                }
                case PAGING_DIRECTORY:
                    parseDirectory(reader, CommonAttributes.PAGING_DIRECTORY, address, list);
                    break;
                case REMOTING_INTERCEPTORS:
                    processRemotingInterceptors(reader, operation);
                    break;
                case SECURITY_DOMAIN:
                    handleElementText(reader, element, null, operation);
                    break;
                case SECURITY_SETTINGS: {
                    // process security settings
                    processSecuritySettings(reader, address, list);
                    break;
                }
                case CORE_QUEUES: {
                    parseQueues(reader, address, list);
                    break;
                }
                case CONNECTION_FACTORIES: {
                    processConnectionFactories(reader, address, list);
                    break;
                }
                case JMS_DESTINATIONS: {
                    processJmsDestinations(reader, address, list);
                    break;
                }
                case SCHEDULED_THREAD_POOL_MAX_SIZE:
                case THREAD_POOL_MAX_SIZE: {
                    // Use the "server" variant
                    handleElementText(reader, element, "server", operation);
                    break;
                }
                case HORNETQ_SERVER:
                    // The end of the hornetq-server element
                    if (namespace == Namespace.MESSAGING_1_0) {
                        throw unexpectedEndElement(reader);
                    }
                    break;
                case SUBSYSTEM:
                    // The end of the subsystem element
                    if (namespace != Namespace.MESSAGING_1_0) {
                        throw unexpectedEndElement(reader);
                    }
                    break;
                default:
                    if (SIMPLE_ROOT_RESOURCE_ELEMENTS.contains(element)) {
                        AttributeDefinition attributeDefinition = element.getDefinition();
                        if (attributeDefinition instanceof SimpleAttributeDefinition) {
                            handleElementText(reader, element, operation);
                        } else {
                            // These should be handled in specific case blocks above, e.g. case REMOTING_INTERCEPTORS:
                            throw MESSAGES.unsupportedElement(element.getLocalName());
                        }
                    } else {
                        throw ParseUtils.unexpectedElement(reader);
                    }
            }
        } while (reader.hasNext() && localName.equals(elementName) == false);
    }

    private static void processConnectorServices(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {
        requireNoAttributes(reader);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case CONNECTOR_SERVICE: {
                    processConnectorService(reader, address, updates);
                    break;
                }
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }

    private static void processConnectorService(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {

        requireSingleAttribute(reader, CommonAttributes.NAME);
        String name = reader.getAttributeValue(0);

        final ModelNode serviceAddress = address.clone().add(CommonAttributes.CONNECTOR_SERVICE, name);
        final ModelNode add = org.jboss.as.controller.operations.common.Util.getEmptyOperation(ADD, serviceAddress);
        updates.add(add);

        EnumSet<Element> required = EnumSet.of(Element.FACTORY_CLASS);
        Set<Element> seen = EnumSet.noneOf(Element.class);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            if (!seen.add(element) && element != Element.PARAM) {
                throw ParseUtils.duplicateNamedElement(reader, element.getLocalName());
            }
            required.remove(element);
            switch (element) {
                case FACTORY_CLASS: {
                    handleElementText(reader, element, add);
                    break;
                }
                case PARAM: {
                    String[] attrs = ParseUtils.requireAttributes(reader, Attribute.KEY.getLocalName(), Attribute.VALUE.getLocalName());
                    requireNoContent(reader);
                    final ModelNode paramAdd = org.jboss.as.controller.operations.common.Util.getEmptyOperation(ADD, serviceAddress.clone().add(CommonAttributes.PARAM, attrs[0]));
                    CommonAttributes.VALUE.parseAndSetParameter(attrs[1], paramAdd, reader);
                    updates.add(paramAdd);
                    break;
                }
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }


        if(!required.isEmpty()) {
            missingRequired(reader, required);
        }

    }

    private void processClusterConnections(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {
        requireNoAttributes(reader);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case CLUSTER_CONNECTION: {
                    processClusterConnection(reader, address, updates);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }

    protected void processClusterConnection(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {

        requireSingleAttribute(reader, CommonAttributes.NAME);
        String name = reader.getAttributeValue(0);

        ModelNode clusterConnectionAdd = org.jboss.as.controller.operations.common.Util.getEmptyOperation(ADD, address.clone().add(CommonAttributes.CLUSTER_CONNECTION, name));

        EnumSet<Element> required = EnumSet.of(Element.ADDRESS, Element.CONNECTOR_REF);
        Set<Element> seen = EnumSet.noneOf(Element.class);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            if (!seen.add(element)) {
                throw ParseUtils.duplicateNamedElement(reader, element.getLocalName());
            }
            required.remove(element);
            switch (element) {
                case FORWARD_WHEN_NO_CONSUMERS:
                case MAX_HOPS:
                case CONFIRMATION_WINDOW_SIZE:
                    handleElementText(reader, element, clusterConnectionAdd);
                    break;
                case ADDRESS:  {
                    handleElementText(reader, element, CommonAttributes.CLUSTER_CONNECTION_ADDRESS.getName(), clusterConnectionAdd);
                    break;
                }
                case CONNECTOR_REF:  {
                    // Use the "simple" variant
                    handleElementText(reader, element, "simple", clusterConnectionAdd);
                    break;
                }
                case USE_DUPLICATE_DETECTION:
                case RETRY_INTERVAL:
                    // Use the "cluster" variant
                    handleElementText(reader, element, "cluster", clusterConnectionAdd);
                    break;
                case STATIC_CONNECTORS:
                    checkOtherElementIsNotAlreadyDefined(reader, seen, Element.STATIC_CONNECTORS, Element.DISCOVERY_GROUP_REF);
                    processStaticConnectors(reader, clusterConnectionAdd, true);
                    break;
                case DISCOVERY_GROUP_REF: {
                    checkOtherElementIsNotAlreadyDefined(reader, seen, Element.DISCOVERY_GROUP_REF, Element.STATIC_CONNECTORS);
                    final String groupRef = readStringAttributeElement(reader, DISCOVERY_GROUP_NAME.getXmlName());
                    DISCOVERY_GROUP_NAME.parseAndSetParameter(groupRef, clusterConnectionAdd, reader);
                    break;
                }
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }

        if(!required.isEmpty()) {
            missingRequired(reader, required);
        }

        updates.add(clusterConnectionAdd);
    }

    private void processBridges(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {
        requireNoAttributes(reader);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case BRIDGE: {
                    processBridge(reader, address, updates);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }

    protected void processBridge(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {

        requireSingleAttribute(reader, CommonAttributes.NAME);
        String name = reader.getAttributeValue(0);

        ModelNode bridgeAdd = org.jboss.as.controller.operations.common.Util.getEmptyOperation(ADD, address.clone().add(CommonAttributes.BRIDGE, name));

        EnumSet<Element> required = EnumSet.of(Element.QUEUE_NAME);
        Set<Element> seen = EnumSet.noneOf(Element.class);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            if (!seen.add(element)) {
                throw ParseUtils.duplicateNamedElement(reader, element.getLocalName());
            }
            required.remove(element);
            switch (element) {
                case QUEUE_NAME:
                case HA:
                case TRANSFORMER_CLASS_NAME:
                case RETRY_INTERVAL_MULTIPLIER:
                case FAILOVER_ON_SERVER_SHUTDOWN:
                case CONFIRMATION_WINDOW_SIZE:
                case USER:
                case PASSWORD:
                    handleElementText(reader, element, bridgeAdd);
                    break;
                case FILTER:  {
                    String string = readStringAttributeElement(reader, CommonAttributes.STRING);
                    FILTER.parseAndSetParameter(string, bridgeAdd, reader);
                    break;
                }
                case RETRY_INTERVAL:
                    // Use the "default" variant
                    handleElementText(reader, element, DEFAULT, bridgeAdd);
                    break;
                case FORWARDING_ADDRESS:
                case RECONNECT_ATTEMPTS:
                case USE_DUPLICATE_DETECTION:
                    handleElementText(reader, element, "bridge", bridgeAdd);
                    break;
                case STATIC_CONNECTORS:
                    checkOtherElementIsNotAlreadyDefined(reader, seen, Element.STATIC_CONNECTORS, Element.DISCOVERY_GROUP_REF);
                    processStaticConnectors(reader, bridgeAdd, false);
                    break;
                case DISCOVERY_GROUP_REF: {
                    checkOtherElementIsNotAlreadyDefined(reader, seen, Element.DISCOVERY_GROUP_REF, Element.STATIC_CONNECTORS);
                    final String groupRef = readStringAttributeElement(reader, DISCOVERY_GROUP_NAME.getXmlName());
                    DISCOVERY_GROUP_NAME.parseAndSetParameter(groupRef, bridgeAdd, reader);
                    break;
                }
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }

        checkOnlyOneOfElements(reader, seen, Element.STATIC_CONNECTORS, Element.DISCOVERY_GROUP_REF);

        if(!required.isEmpty()) {
            missingRequired(reader, required);
        }

        updates.add(bridgeAdd);
    }

    protected void processStaticConnectors(XMLExtendedStreamReader reader, ModelNode addOperation, boolean cluster) throws XMLStreamException {

        if (cluster) {

            int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case ALLOW_DIRECT_CONNECTIONS_ONLY: {
                        final String attrValue = reader.getAttributeValue(i);
                        CommonAttributes.ALLOW_DIRECT_CONNECTIONS_ONLY.parseAndSetParameter(attrValue, addOperation, reader);
                        break;
                    }
                    default: {
                        throw ParseUtils.unexpectedAttribute(reader, i);
                    }
                }
            }

        } else {
            requireNoAttributes(reader);
        }

        EnumSet<Element> required = EnumSet.of(Element.CONNECTOR_REF);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            required.remove(element);
            switch (element) {
                case CONNECTOR_REF: {
                    handleElementText(reader, element, cluster ? "cluster-connection" : "bridge", addOperation);
                    break;
                }
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }

        if(!required.isEmpty()) {
            missingRequired(reader, required);
        }
    }

    private void processGroupingHandler(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {

        requireSingleAttribute(reader, CommonAttributes.NAME);
        String name = reader.getAttributeValue(0);

        ModelNode groupingHandlerAdd = org.jboss.as.controller.operations.common.Util.getEmptyOperation(ADD, address.clone().add(CommonAttributes.GROUPING_HANDLER, name));

        EnumSet<Element> required = EnumSet.of(Element.ADDRESS, Element.TYPE);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            required.remove(element);
            switch (element) {
                case TYPE:
                case TIMEOUT: {
                    handleElementText(reader, element, groupingHandlerAdd);
                    break;
                }
                case ADDRESS: {
                    handleElementText(reader, element, CommonAttributes.GROUPING_HANDLER_ADDRESS.getName(), groupingHandlerAdd);
                    break;
                }
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }

        if(!required.isEmpty()) {
            missingRequired(reader, required);
        }

        updates.add(groupingHandlerAdd);
    }

    private void processRemotingInterceptors(XMLExtendedStreamReader reader, ModelNode operation) throws XMLStreamException {
        requireNoAttributes(reader);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case CLASS_NAME: {
                    final String value = reader.getElementText();
                    REMOTING_INTERCEPTORS.parseAndAddParameterElement(value, operation, reader);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }

    static void processBroadcastGroups(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {
        requireNoAttributes(reader);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case BROADCAST_GROUP: {
                    parseBroadcastGroup(reader, address, updates);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }

    private static void parseBroadcastGroup(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {

        String name = null;

        int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String attrValue = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case NAME: {
                    name = attrValue;
                    break;
                }
                default: {
                    throw ParseUtils.unexpectedAttribute(reader, i);
                }
            }
        }
        if(name == null) {
            ParseUtils.missingRequired(reader, Collections.singleton(Attribute.NAME));
        }

        ModelNode broadcastGroupAdd = org.jboss.as.controller.operations.common.Util.getEmptyOperation(ADD, address.clone().add(CommonAttributes.BROADCAST_GROUP, name));

        EnumSet<Element> required = EnumSet.of(Element.GROUP_ADDRESS, Element.GROUP_PORT);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            required.remove(element);
            switch (element) {
                case LOCAL_BIND_ADDRESS:
                case LOCAL_BIND_PORT:
                case GROUP_ADDRESS:
                case GROUP_PORT:
                case SOCKET_BINDING:
                case BROADCAST_PERIOD:
                    handleElementText(reader, element, broadcastGroupAdd);
                    break;
                case CONNECTOR_REF:
                    handleElementText(reader, element, "broadcast-group", broadcastGroupAdd);
                    break;
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }

        if(!required.isEmpty()) {
            missingRequired(reader, required);
        }

        updates.add(broadcastGroupAdd);
    }

    static void processDiscoveryGroups(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {
        requireNoAttributes(reader);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case DISCOVERY_GROUP: {
                    parseDiscoveryGroup(reader, address, updates);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }

    private static void parseDiscoveryGroup(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {

        String name = null;

        int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String attrValue = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case NAME: {
                    name = attrValue;
                    break;
                }
                default: {
                    throw ParseUtils.unexpectedAttribute(reader, i);
                }
            }
        }
        if(name == null) {
            ParseUtils.missingRequired(reader, Collections.singleton(Attribute.NAME));
        }

        ModelNode discoveryGroup = org.jboss.as.controller.operations.common.Util.getEmptyOperation(ADD, address.clone().add(CommonAttributes.DISCOVERY_GROUP, name));

        EnumSet<Element> required = EnumSet.of(Element.GROUP_ADDRESS, Element.GROUP_PORT);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            required.remove(element);
            switch (element) {
                case LOCAL_BIND_ADDRESS:
                case GROUP_ADDRESS:
                case GROUP_PORT:
                case REFRESH_TIMEOUT:
                case SOCKET_BINDING:
                case INITIAL_WAIT_TIMEOUT:
                    handleElementText(reader, element, discoveryGroup);
                    break;
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }

        if(!required.isEmpty()) {
            missingRequired(reader, required);
        }

        updates.add(discoveryGroup);
    }

    void processConnectionFactories(final XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {
       while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
          final Element element = Element.forName(reader.getLocalName());
          switch(element) {
             case CONNECTION_FACTORY:
                processConnectionFactory(reader, address, updates);
               break;
             case POOLED_CONNECTION_FACTORY:
                processPooledConnectionFactory(reader, address, updates);
               break;
             default:
                    throw ParseUtils.unexpectedElement(reader);
          }
       }
    }

   static void processJmsDestinations(final XMLExtendedStreamReader reader, final ModelNode address, List<ModelNode> updates) throws XMLStreamException {
       while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
          final Element element = Element.forName(reader.getLocalName());
          switch(element) {
             case JMS_QUEUE:
                processJMSQueue(reader, address, updates);
               break;
             case JMS_TOPIC:
                processJMSTopic(reader, address, updates);
               break;
             default:
                    throw ParseUtils.unexpectedElement(reader);
          }
       }
    }

    static void processAcceptors(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> updates) throws XMLStreamException {
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            String name = null;
            String socketBinding = null;
            int serverId = 0;

            int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                final String attrValue = reader.getAttributeValue(i);
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case NAME: {
                        name = attrValue;
                        break;
                    }
                    case SOCKET_BINDING: {
                        socketBinding = attrValue;
                        break;
                    }
                    case SERVER_ID: {
                        serverId = Integer.valueOf(attrValue);
                        break;
                    }
                    default: {
                        throw ParseUtils.unexpectedAttribute(reader, i);
                    }
                }
            }
            if(name == null) {
                throw ParseUtils.missingRequired(reader, Collections.singleton(Attribute.NAME));
            }

            final ModelNode acceptorAddress = address.clone();
            final ModelNode operation = new ModelNode();
            operation.get(OP).set(ADD);

            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case ACCEPTOR: {
                    acceptorAddress.add(ACCEPTOR, name);
                    if(socketBinding != null) operation.get(SOCKET_BINDING.getName()).set(socketBinding);
                    parseTransportConfigurationParams(reader, operation, true);
                    break;
                } case NETTY_ACCEPTOR: {
                    acceptorAddress.add(REMOTE_ACCEPTOR, name);
                    if(socketBinding == null) {
                        throw ParseUtils.missingRequired(reader, Collections.singleton(Attribute.SOCKET_BINDING));
                    }
                    operation.get(SOCKET_BINDING.getName()).set(socketBinding);
                    parseTransportConfigurationParams(reader, operation, false);
                    break;
                } case IN_VM_ACCEPTOR: {
                    acceptorAddress.add(IN_VM_ACCEPTOR, name);
                    operation.get(SERVER_ID.getName()).set(serverId);
                    parseTransportConfigurationParams(reader, operation, false);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
            //
            operation.get(OP_ADDR).set(acceptorAddress);
            updates.add(operation);
        }
    }

    static void parseQueues(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> list) throws XMLStreamException {

        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            String name = null;
            int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                final String attrValue = reader.getAttributeValue(i);
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case NAME: {
                        name = attrValue;
                        break;
                    }
                    default: {
                        throw ParseUtils.unexpectedAttribute(reader, i);
                    }
                }
            }
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case QUEUE: {
                    if(name == null) {
                        throw ParseUtils.missingRequired(reader, Collections.singleton(Attribute.NAME.getLocalName()));
                    }
                    final ModelNode op = org.jboss.as.controller.operations.common.Util.getEmptyOperation(ADD, address.clone().add(CommonAttributes.QUEUE, name));
                    parseQueue(reader, op);
                    if(! op.hasDefined(QUEUE_ADDRESS.getName())) {
                        throw ParseUtils.missingRequired(reader, Collections.singleton(Element.ADDRESS.getLocalName()));
                    }
                    list.add(op);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }

    static void parseQueue(final XMLExtendedStreamReader reader, final ModelNode queue) throws XMLStreamException {
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case ADDRESS: {
                    handleElementText(reader, element, QUEUE_ADDRESS.getName(), queue);
                    break;
                } case FILTER: {
                    String string = readStringAttributeElement(reader, CommonAttributes.STRING);
                    FILTER.parseAndSetParameter(string, queue, reader);
                    break;
                } case DURABLE: {
                    handleElementText(reader, element, queue);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }

    static ModelNode processSecuritySettings(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> operations) throws XMLStreamException {
        final ModelNode security = new ModelNode();
        int tag = reader.getEventType();
        String localName = null;
        do {
            tag = reader.nextTag();
            localName = reader.getLocalName();
            final org.jboss.as.messaging.Element element = org.jboss.as.messaging.Element.forName(reader.getLocalName());

            switch (element) {
                case SECURITY_SETTING:
                    final String match = reader.getAttributeValue(0);

                    final ModelNode addr = address.clone();
                    addr.add(SECURITY_SETTING, match);
                    final ModelNode operation = new ModelNode();
                    operation.get(OP).set(ADD);
                    operation.get(OP_ADDR).set(addr);
                    operations.add(operation);

                    parseSecurityRoles(reader, addr, operations);
                    break;
            }
        } while (reader.hasNext() && localName.equals(Element.SECURITY_SETTING.getLocalName()));
        return security;
    }

    static void parseSecurityRoles(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> operations) throws XMLStreamException {

        final Map<String, Set<AttributeDefinition>> permsByRole = new HashMap<String, Set<AttributeDefinition>>();
        int tag = reader.getEventType();
        String localName = null;
        do {
            tag = reader.nextTag();
            localName = reader.getLocalName();
            final Element element = Element.forName(localName);
            if (element != Element.PERMISSION_ELEMENT_NAME) {
                break;
            }

            final Set<Attribute> required = EnumSet.of(Attribute.ROLES_ATTR_NAME, Attribute.TYPE_ATTR_NAME);
            List<String> roles = null;
            AttributeDefinition perm = null;
            final int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                requireNoNamespaceAttribute(reader, i);
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                required.remove(attribute);
                switch (attribute) {
                    case ROLES_ATTR_NAME:
                        roles = reader.getListAttributeValue(i);
                        break;
                    case TYPE_ATTR_NAME:
                        perm = SecurityRoleAdd.ROLE_ATTRIBUTES_BY_XML_NAME.get(reader.getAttributeValue(i));
                        if (perm == null) {
                            throw ControllerMessages.MESSAGES.invalidAttributeValue(reader.getAttributeValue(i),
                                    reader.getAttributeName(i), SecurityRoleAdd.ROLE_ATTRIBUTES_BY_XML_NAME.keySet(),
                                    reader.getLocation());
                        }
                        break;
                    default:
                        throw ParseUtils.unexpectedAttribute(reader, i);
                }
            }

            if (!required.isEmpty()) {
                throw missingRequired(reader, required);
            }

            for (String role : roles) {
                role = role.trim();
                Set<AttributeDefinition> perms = permsByRole.get(role);
                if (perms == null) {
                    perms = new HashSet<AttributeDefinition>();
                    permsByRole.put(role, perms);
                }
                perms.add(perm);
            }
            // Scan to element end
            reader.discardRemainder();
        } while (reader.hasNext());

        for (Map.Entry<String, Set<AttributeDefinition>> entry : permsByRole.entrySet()) {
            final String role = entry.getKey();
            final Set<AttributeDefinition> perms = entry.getValue();

            final ModelNode addr = address.clone();
            addr.add(ROLE, role);

            final ModelNode operation = new ModelNode();
            operation.get(OP).set(ADD);
            operation.get(OP_ADDR).set(addr);

            for (AttributeDefinition perm : SecurityRoleAdd.ROLE_ATTRIBUTES) {
                operation.get(perm.getName()).set(perms.contains(perm));
            }

            operations.add(operation);
        }
    }

    static void processConnectors(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> updates) throws XMLStreamException {
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            String name = null;
            String socketBinding = null;
            int serverId = 0;

            int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                final String attrValue = reader.getAttributeValue(i);
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case NAME: {
                        name = attrValue;
                        break;
                    }
                    case SOCKET_BINDING: {
                        socketBinding = attrValue;
                        break;
                    }
                    case SERVER_ID: {
                        serverId = Integer.valueOf(attrValue);
                        break;
                    }
                    default: {
                        throw ParseUtils.unexpectedAttribute(reader, i);
                    }
                }
            }
            if(name == null) {
                ParseUtils.missingRequired(reader, Collections.singleton(Attribute.NAME));
            }

            final ModelNode connectorAddress = address.clone();
            final ModelNode operation = new ModelNode();
            operation.get(OP).set(ADD);

            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case CONNECTOR: {
                    connectorAddress.add(CONNECTOR, name);
                    if(socketBinding != null) operation.get(SOCKET_BINDING.getName()).set(socketBinding);
                    parseTransportConfigurationParams(reader, operation, true);
                    break;
                } case NETTY_CONNECTOR: {
                    connectorAddress.add(REMOTE_CONNECTOR, name);
                    if(socketBinding == null) {
                        ParseUtils.missingRequired(reader, Collections.singleton(Attribute.SOCKET_BINDING));
                    }
                    operation.get(SOCKET_BINDING.getName()).set(socketBinding);
                    parseTransportConfigurationParams(reader, operation, false);
                    break;
                } case IN_VM_CONNECTOR: {
                    connectorAddress.add(IN_VM_CONNECTOR, name);
                    operation.get(SERVER_ID.getName()).set(serverId);
                    parseTransportConfigurationParams(reader, operation, false);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }

            operation.get(OP_ADDR).set(connectorAddress);
            updates.add(operation);
        }
    }

    static void processAddressSettings(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> operations) throws XMLStreamException {
        final ModelNode settings = new ModelNode();

        String localName = null;
        int tag = reader.getEventType();
        do {
            reader.nextTag();
            localName = reader.getLocalName();
            final Element element = Element.forName(localName);
            switch (element) {
                case ADDRESS_SETTING:
                    // Add address settings
                    final String match = reader.getAttributeValue(0);
                    final ModelNode operation = parseAddressSettings(reader);
                    operation.get(OP).set(ADD);
                    operation.get(OP_ADDR).set(address);
                    operation.get(OP_ADDR).add(CommonAttributes.ADDRESS_SETTING, match);

                    operations.add(operation);
                    break;
            }
        } while (reader.hasNext() && localName.equals(Element.ADDRESS_SETTING.getLocalName()));
    }

    static ModelNode parseAddressSettings(final XMLExtendedStreamReader reader) throws XMLStreamException {
        final ModelNode addressSettingsSpec = new ModelNode();

        String localName;
        do {
            reader.nextTag();
            localName = reader.getLocalName();
            final Element element = Element.forName(localName);

            SWITCH:
            switch (element) {
                case DEAD_LETTER_ADDRESS:
                case EXPIRY_ADDRESS:
                case REDELIVERY_DELAY:
                case MAX_SIZE_BYTES:
                case PAGE_MAX_CACHE_SIZE:
                case PAGE_SIZE_BYTES:
                case MESSAGE_COUNTER_HISTORY_DAY_LIMIT:
                case ADDRESS_FULL_MESSAGE_POLICY:
                case LVQ:
                case MAX_DELIVERY_ATTEMPTS:
                case REDISTRIBUTION_DELAY:
                case SEND_TO_DLA_ON_NO_ROUTE: {
                    handleElementText(reader, element, addressSettingsSpec);
                    break SWITCH;
                } default: {
                    break;
                }
            }
        } while (!reader.getLocalName().equals(Element.ADDRESS_SETTING.getLocalName()) && reader.getEventType() == XMLExtendedStreamReader.END_ELEMENT);

        return addressSettingsSpec;
    }

    static void parseTransportConfigurationParams(final XMLExtendedStreamReader reader, final ModelNode transportConfig, final boolean generic) throws XMLStreamException {
        final ModelNode params = new ModelNode();

        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            int count = reader.getAttributeCount();
            String key = null;
            String value = null;
            for (int n = 0; n < count; n++) {
                String attrName = reader.getAttributeLocalName(n);
                Attribute attribute = Attribute.forName(attrName);
                switch (attribute) {
                    case KEY:
                        key = reader.getAttributeValue(n);
                        break;
                    case VALUE:
                        value = reader.getAttributeValue(n);
                        break;
                    default:
                        throw unexpectedAttribute(reader, n);
                }
            }

            Element element = Element.forName(reader.getLocalName());
            switch(element) {
                case FACTORY_CLASS: {
                    if(! generic) {
                        throw ParseUtils.unexpectedElement(reader);
                    }
                    transportConfig.get(FACTORY_CLASS.getName()).set(reader.getElementText().trim());
                    break;
                }
                case PARAM: {
                    params.add(key, value);
                    ParseUtils.requireNoContent(reader);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
        transportConfig.get(PARAMS).set(params);
    }

    static void parseDirectory(final XMLExtendedStreamReader reader, final String name, final ModelNode address, final List<ModelNode> updates) throws XMLStreamException {
        String path = null;
        String relativeTo = null;
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            final String value = reader.getAttributeValue(i);
            switch (attribute) {
                case RELATIVE_TO:
                    relativeTo = value;
                    break;
                case PATH:
                    path = value;
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        if(path == null) {
            throw ParseUtils.missingRequired(reader, Collections.singleton(Attribute.PATH));
        }
        requireNoContent(reader);
        final ModelNode operation = new ModelNode();
        operation.get(OP).set(ADD);
        operation.get(OP_ADDR).set(address);
        operation.get(OP_ADDR).add(ModelDescriptionConstants.PATH, name);
        operation.get(ModelDescriptionConstants.PATH).set(path);
        if(relativeTo != null) operation.get(ModelDescriptionConstants.RELATIVE_TO).set(relativeTo);

        updates.add(operation);
    }

    private static void parseDiverts(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> list) throws XMLStreamException {
        requireNoAttributes(reader);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case DIVERT: {
                    parseDivert(reader, address, list);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }

    private static void parseDivert(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> list) throws XMLStreamException {

        requireSingleAttribute(reader, CommonAttributes.NAME);
        String name = reader.getAttributeValue(0);

        ModelNode divertAdd = org.jboss.as.controller.operations.common.Util.getEmptyOperation(ADD, address.clone().add(CommonAttributes.DIVERT, name));

        EnumSet<Element> required = EnumSet.of(Element.ADDRESS, Element.FORWARDING_ADDRESS);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            required.remove(element);
            switch (element) {
                case ROUTING_NAME: {
                    handleElementText(reader, element, divertAdd);
                    break;
                }
                case ADDRESS: {
                    handleElementText(reader, element, DivertDefinition.ADDRESS.getName(), divertAdd);
                    break;
                }
                case FORWARDING_ADDRESS: {
                    handleElementText(reader, element, "divert", divertAdd);
                    break;
                }
                case FILTER: {
                    String string = readStringAttributeElement(reader, CommonAttributes.STRING);
                    FILTER.parseAndSetParameter(string, divertAdd, reader);
                    break;
                }
                case TRANSFORMER_CLASS_NAME: {
                    handleElementText(reader, element, divertAdd);
                    break;
                }
                case EXCLUSIVE: {
                    handleElementText(reader, element, divertAdd);
                    break;
                }
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }

        if(!required.isEmpty()) {
            missingRequired(reader, required);
        }

        list.add(divertAdd);
    }

    static void unhandledElement(XMLExtendedStreamReader reader, Element element) throws XMLStreamException {
        throw MESSAGES.ignoringUnhandledElement(element, reader.getLocation().toString());
    }

    static void handleElementText(final XMLExtendedStreamReader reader, final Element element, final ModelNode node) throws XMLStreamException {
        handleElementText(reader, element, null, node);
    }

    static void handleElementText(final XMLExtendedStreamReader reader, final Element element, final String modelName, final ModelNode node) throws XMLStreamException {
        AttributeDefinition attributeDefinition = modelName == null ? element.getDefinition() : element.getDefinition(modelName);
        if (attributeDefinition != null) {
            final String value = reader.getElementText();
            if (attributeDefinition instanceof SimpleAttributeDefinition) {
                ((SimpleAttributeDefinition) attributeDefinition).parseAndSetParameter(value, node, reader);
            } else if (attributeDefinition instanceof ListAttributeDefinition) {
                ((ListAttributeDefinition) attributeDefinition).parseAndAddParameterElement(value, node, reader);
            }
        } else {
            handleElementText(reader, element, node, ModelType.STRING, true, false);
        }
    }

    static void skipElementText(final XMLExtendedStreamReader reader) throws XMLStreamException {
        reader.getElementText();
    }

    /** @deprecated use AttributeDefinition */
    @Deprecated
    static void handleElementText(final XMLExtendedStreamReader reader, final Element element, final ModelNode node, final ModelType expectedType,
                                  final boolean allowNull, final boolean allowExpression) throws XMLStreamException {
        final String value = reader.getElementText();
        if(value != null && value.length() > 0) {
            ModelNode toSet = node.get(element.getLocalName());
            ModelNode modelValue = allowExpression ? parsePossibleExpression(value.trim()) : new ModelNode().set(value.trim());
            if (!allowExpression || modelValue.getType() != ModelType.EXPRESSION) {
                toSet.set(modelValue);
            }
            else {
                try {
                    switch (expectedType) {
                        case BOOLEAN:
                            toSet.set(modelValue.asBoolean());
                            break;
                        case BIG_DECIMAL:
                            toSet.set(modelValue.asBigDecimal());
                            break;
                        case BIG_INTEGER:
                            toSet.set(modelValue.asBigInteger());
                            break;
                        case BYTES:
                            toSet.set(modelValue.asBytes());
                            break;
                        case DOUBLE:
                            toSet.set(modelValue.asDouble());
                            break;
                        case INT:
                            toSet.set(modelValue.asInt());
                            break;
                        case LONG:
                            toSet.set(modelValue.asLong());
                            break;
                        case STRING:
                            toSet.set(modelValue.asString());
                            break;
                        default:
                            throw new XMLStreamException(MESSAGES.illegalValue(value, element.getLocalName()), reader.getLocation());
                    }
                } catch (IllegalArgumentException iae) {
                    throw new XMLStreamException(MESSAGES.illegalValue(value, element.getLocalName(), expectedType), reader.getLocation());
                }
            }
        } else if (!allowNull) {
            throw new XMLStreamException(MESSAGES.illegalValue(value, element.getLocalName()), reader.getLocation());
        }
    }

    public Namespace getExpectedNamespace() {
        return Namespace.MESSAGING_1_1;
    }

    public void writeContent(final XMLExtendedStreamWriter writer, final SubsystemMarshallingContext context) throws XMLStreamException {
        context.startSubsystemElement(getExpectedNamespace().getUriString(), false);
        final ModelNode node = context.getModelNode();

        final ModelNode servers = node.require(HORNETQ_SERVER);
        boolean first = true;
        for (Property prop : servers.asPropertyList()) {
            writeHornetQServer(writer, prop.getName(), prop.getValue());
            if (!first) {
                writeNewLine(writer);
            } else {
                first = false;
            }
        }
        writer.writeEndElement();
    }

    protected void writeHornetQServer(final XMLExtendedStreamWriter writer, final String serverName, final ModelNode node) throws XMLStreamException {

        writer.writeStartElement(Element.HORNETQ_SERVER.getLocalName());

        if (!DEFAULT.equals(serverName)) {
            writer.writeAttribute(Attribute.NAME.getLocalName(), serverName);
        }

        for (AttributeDefinition simpleAttribute : CommonAttributes.SIMPLE_ROOT_RESOURCE_ATTRIBUTES) {
            simpleAttribute.marshallAsElement(node, writer);
        }

        final ModelNode paths = node.get(ModelDescriptionConstants.PATH);
        if (paths.hasDefined(CommonAttributes.PAGING_DIRECTORY)) {
            writeDirectory(writer, Element.PAGING_DIRECTORY, node.get(ModelDescriptionConstants.PATH));
        }
        if (paths.hasDefined(BINDINGS_DIRECTORY)) {
            writeDirectory(writer, Element.BINDINGS_DIRECTORY, node.get(ModelDescriptionConstants.PATH));
        }
        if (paths.hasDefined(CommonAttributes.JOURNAL_DIRECTORY)) {
            writeDirectory(writer, Element.JOURNAL_DIRECTORY, node.get(ModelDescriptionConstants.PATH));
        }
        if (paths.hasDefined(CommonAttributes.LARGE_MESSAGES_DIRECTORY)) {
            writeDirectory(writer, Element.LARGE_MESSAGES_DIRECTORY, node.get(ModelDescriptionConstants.PATH));
        }
        // New line after the simpler elements
        writeNewLine(writer);

        writeConnectors(writer, node);

        writeAcceptors(writer, node);

        if (node.hasDefined(BROADCAST_GROUP)) {
            writeBroadcastGroups(writer, node.get(BROADCAST_GROUP));
        }
        if (node.hasDefined(DISCOVERY_GROUP)) {
            writeDiscoveryGroups(writer, node.get(DISCOVERY_GROUP));
        }
        if (node.hasDefined(DIVERT)) {
            writeDiverts(writer, node.get(DIVERT));
        }
        if (node.hasDefined(CommonAttributes.QUEUE)) {
            writeQueues(writer, node.get(CommonAttributes.QUEUE));
        }
        if (node.hasDefined(CommonAttributes.BRIDGE)) {
            writeBridges(writer, node.get(CommonAttributes.BRIDGE));
        }
        if (node.hasDefined(CommonAttributes.CLUSTER_CONNECTION)) {
            writeClusterConnections(writer, node.get(CommonAttributes.CLUSTER_CONNECTION));
        }

        if (node.hasDefined(CommonAttributes.GROUPING_HANDLER)) {
            writeGroupingHandler(writer, node.get(GROUPING_HANDLER));
        }

        if (node.hasDefined(CommonAttributes.SECURITY_SETTING)) {
            writeSecuritySettings(writer, node.get(CommonAttributes.SECURITY_SETTING));
        }

        if (node.hasDefined(ADDRESS_SETTING)) {
            writeAddressSettings(writer, node.get(ADDRESS_SETTING));
        }

        if (node.hasDefined(CommonAttributes.CONNECTOR_SERVICE)) {
            writeConnectorServices(writer, node.get(CommonAttributes.CONNECTOR_SERVICE));
        }

        if (node.hasDefined(CONNECTION_FACTORY) || node.hasDefined(POOLED_CONNECTION_FACTORY)) {
           ModelNode cf = node.get(CONNECTION_FACTORY);
           ModelNode pcf = node.get(POOLED_CONNECTION_FACTORY);
           boolean hasCf = cf.isDefined() && cf.keys().size() > 0;
           boolean hasPcf = cf.isDefined() && cf.keys().size() > 0;
           if (hasCf || hasPcf) {
               writer.writeStartElement(JMS_CONNECTION_FACTORIES);
               if (hasCf) {
                   writeConnectionFactories(writer, cf);
               }
               if (hasPcf) {
                   writePooledConnectionFactories(writer, pcf);
               }
               writer.writeEndElement();
               writeNewLine(writer);
           }
        }

        if (node.has(JMS_QUEUE) || node.has(JMS_TOPIC)) {
           ModelNode queue = node.get(JMS_QUEUE);
           ModelNode topic = node.get(JMS_TOPIC);
           boolean hasQueue = queue.isDefined() && queue.keys().size() > 0;
           boolean hasTopic = topic.isDefined() && topic.keys().size() > 0;
           if (hasQueue || hasTopic) {
               writer.writeStartElement(JMS_DESTINATIONS);
               if (hasQueue) {
                   writeJmsQueues(writer, node.get(JMS_QUEUE));
               }
               if (hasTopic) {
                   writeTopics(writer, node.get(JMS_TOPIC));
               }
               writer.writeEndElement();
           }
        }

        writer.writeEndElement();
    }

    private void writeConnectorServices(XMLExtendedStreamWriter writer, ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            writer.writeStartElement(Element.CONNECTOR_SERVICES.getLocalName());
            for(final Property property : node.asPropertyList()) {
                writer.writeStartElement(Element.CONNECTOR_SERVICE.getLocalName());
                writer.writeAttribute(Attribute.NAME.getLocalName(), property.getName());
                final ModelNode service = property.getValue();
                for (AttributeDefinition attribute : CommonAttributes.CONNECTOR_SERVICE_ATTRIBUTES) {
                    attribute.marshallAsElement(property.getValue(), writer);
                }
                if (service.hasDefined(CommonAttributes.PARAM)) {
                    for (Property param : service.get(CommonAttributes.PARAM).asPropertyList()) {
                        writer.writeEmptyElement(Element.PARAM.getLocalName());
                        writer.writeAttribute(Attribute.KEY.getLocalName(), param.getName());
                        writer.writeAttribute(Attribute.VALUE.getLocalName(), param.getValue().get(CommonAttributes.VALUE.getName()).asString());
                    }
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    private void writeBridges(XMLExtendedStreamWriter writer, ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            writer.writeStartElement(Element.BRIDGES.getLocalName());
            for(final Property property : node.asPropertyList()) {
                writer.writeStartElement(Element.BRIDGE.getLocalName());
                writer.writeAttribute(Attribute.NAME.getLocalName(), property.getName());
                final ModelNode bridge = property.getValue();
                for (AttributeDefinition attribute : CommonAttributes.BRIDGE_ATTRIBUTES) {
                    if (CommonAttributes.FILTER == attribute) {
                        writeFilter(writer, property.getValue());
                    } else if (attribute == CommonAttributes.DISCOVERY_GROUP_NAME) {
                        if (CommonAttributes.DISCOVERY_GROUP_NAME.isMarshallable(bridge)) {
                            writer.writeStartElement(Element.DISCOVERY_GROUP_REF.getLocalName());
                            CommonAttributes.DISCOVERY_GROUP_NAME.marshallAsAttribute(bridge, writer);
                            writer.writeEndElement();
                        }
                    } else {
                        attribute.marshallAsElement(property.getValue(), writer);
                    }
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    private void writeClusterConnections(XMLExtendedStreamWriter writer, ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            writer.writeStartElement(Element.CLUSTER_CONNECTIONS.getLocalName());
            for(final Property property : node.asPropertyList()) {
                writer.writeStartElement(Element.CLUSTER_CONNECTION.getLocalName());
                writer.writeAttribute(Attribute.NAME.getLocalName(), property.getName());
                final ModelNode cluster = property.getValue();
                for (AttributeDefinition attribute : CommonAttributes.CLUSTER_CONNECTION_ATTRIBUTES) {
                    if (attribute == CommonAttributes.ALLOW_DIRECT_CONNECTIONS_ONLY) {
                        // we nest it in static-connectors
                        continue;
                    }
                    if (attribute == ConnectorRefsAttribute.CLUSTER_CONNECTION_CONNECTORS) {
                        if (ConnectorRefsAttribute.CLUSTER_CONNECTION_CONNECTORS.isMarshallable(cluster)) {
                            writer.writeStartElement(Element.STATIC_CONNECTORS.getLocalName());
                            CommonAttributes.ALLOW_DIRECT_CONNECTIONS_ONLY.marshallAsAttribute(cluster, writer);
                            ConnectorRefsAttribute.CLUSTER_CONNECTION_CONNECTORS.marshallAsElement(cluster, writer);
                            writer.writeEndElement();
                        } else if (CommonAttributes.ALLOW_DIRECT_CONNECTIONS_ONLY.isMarshallable(cluster)) {
                            writer.writeEmptyElement(Element.STATIC_CONNECTORS.getLocalName());
                            CommonAttributes.ALLOW_DIRECT_CONNECTIONS_ONLY.marshallAsAttribute(cluster, writer);
                        }
                    }
                    else if (attribute == CommonAttributes.DISCOVERY_GROUP_NAME) {
                        if (CommonAttributes.DISCOVERY_GROUP_NAME.isMarshallable(cluster)) {
                            writer.writeStartElement(Element.DISCOVERY_GROUP_REF.getLocalName());
                            CommonAttributes.DISCOVERY_GROUP_NAME.marshallAsAttribute(cluster, writer);
                            writer.writeEndElement();
                        }
                    } else {
                        attribute.marshallAsElement(property.getValue(), writer);
                    }
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    private void writeGroupingHandler(XMLExtendedStreamWriter writer, ModelNode node) throws XMLStreamException {

        boolean wroteHandler = false;
        for (Property handler : node.asPropertyList()) {
            if (wroteHandler) {
                throw MESSAGES.multipleChildrenFound(GROUPING_HANDLER);
            } else {
                wroteHandler = true;
            }
            writer.writeStartElement(Element.GROUPING_HANDLER.getLocalName());
            writer.writeAttribute(Attribute.NAME.getLocalName(), handler.getName());
            final ModelNode resourceModel = handler.getValue();
            for (AttributeDefinition attr : CommonAttributes.GROUPING_HANDLER_ATTRIBUTES) {
                attr.marshallAsElement(resourceModel, writer);
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    static void writeAcceptors(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        if (node.hasDefined(ACCEPTOR) || node.hasDefined(REMOTE_ACCEPTOR) || node.hasDefined(IN_VM_ACCEPTOR)) {
            writer.writeStartElement(Element.ACCEPTORS.getLocalName());
            if(node.hasDefined(ACCEPTOR)) {
                for(final Property property : node.get(ACCEPTOR).asPropertyList()) {
                    writer.writeStartElement(Element.ACCEPTOR.getLocalName());
                    writeAcceptorAndConnectorContent(writer, property);
                    writer.writeEndElement();
                }
            }
            if(node.hasDefined(REMOTE_ACCEPTOR)) {
                for(final Property property : node.get(REMOTE_ACCEPTOR).asPropertyList()) {
                    writer.writeStartElement(Element.NETTY_ACCEPTOR.getLocalName());
                    writeAcceptorAndConnectorContent(writer, property);
                    writer.writeEndElement();
                }
            }
            if(node.hasDefined(IN_VM_ACCEPTOR)) {
                for(final Property property : node.get(IN_VM_ACCEPTOR).asPropertyList()) {
                    writer.writeStartElement(Element.IN_VM_ACCEPTOR.getLocalName());
                    writeAcceptorAndConnectorContent(writer, property);
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    static void writeConnectors(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        if (node.hasDefined(CONNECTOR) || node.hasDefined(REMOTE_CONNECTOR) || node.hasDefined(IN_VM_CONNECTOR)) {
            writer.writeStartElement(Element.CONNECTORS.getLocalName());
            if(node.hasDefined(CONNECTOR)) {
                for(final Property property : node.get(CONNECTOR).asPropertyList()) {
                    writer.writeStartElement(Element.CONNECTOR.getLocalName());
                    writeAcceptorAndConnectorContent(writer, property);
                    writer.writeEndElement();
                }
            }
            if(node.hasDefined(REMOTE_CONNECTOR)) {
                for(final Property property : node.get(REMOTE_CONNECTOR).asPropertyList()) {
                    writer.writeStartElement(Element.NETTY_CONNECTOR.getLocalName());
                    writeAcceptorAndConnectorContent(writer, property);
                    writer.writeEndElement();
                }
            }
            if(node.hasDefined(IN_VM_CONNECTOR)) {
                for(final Property property : node.get(IN_VM_CONNECTOR).asPropertyList()) {
                    writer.writeStartElement(Element.IN_VM_CONNECTOR.getLocalName());
                    writeAcceptorAndConnectorContent(writer, property);
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    static void writeAcceptorAndConnectorContent(final XMLExtendedStreamWriter writer, final Property property) throws XMLStreamException {
        writer.writeAttribute(Attribute.NAME.getLocalName(), property.getName());
        final ModelNode value = property.getValue();

        if (value.hasDefined(SOCKET_BINDING.getName())) {
            writeAttribute(writer, Attribute.SOCKET_BINDING, value.get(SOCKET_BINDING.getName()));
        }
        if (value.hasDefined(SERVER_ID.getName())) {
            writeAttribute(writer, Attribute.SERVER_ID, value.get(SERVER_ID.getName()));
        }

        CommonAttributes.FACTORY_CLASS.marshallAsElement(value, writer);

        if (value.hasDefined(PARAM)) {
            for(final Property parameter : value.get(PARAM).asPropertyList()) {
                writer.writeStartElement(Element.PARAM.getLocalName());
                writer.writeAttribute(Attribute.KEY.getLocalName(), parameter.getName());
                writeAttribute(writer, Attribute.VALUE, parameter.getValue().get(VALUE));
                writer.writeEndElement();
            }
        }
    }

    private void writeSecuritySettings(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            writer.writeStartElement(Element.SECURITY_SETTINGS.getLocalName());

            for (Property matchRoles : properties) {
                writer.writeStartElement(Element.SECURITY_SETTING.getLocalName());
                writer.writeAttribute(Attribute.MATCH.getLocalName(), matchRoles.getName());

                if (matchRoles.getValue().hasDefined(ROLE)) {

                    ArrayList<String> send = new ArrayList<String>();
                    ArrayList<String> consume = new ArrayList<String>();
                    ArrayList<String> createDurableQueue = new ArrayList<String>();
                    ArrayList<String> deleteDurableQueue = new ArrayList<String>();
                    ArrayList<String> createNonDurableQueue = new ArrayList<String>();
                    ArrayList<String> deleteNonDurableQueue = new ArrayList<String>();
                    ArrayList<String> manageRoles = new ArrayList<String>();

                    for (Property rolePerms : matchRoles.getValue().get(ROLE).asPropertyList()) {
                        final String role = rolePerms.getName();
                        final ModelNode perms = rolePerms.getValue();
                        if (perms.get(SecurityRoleAdd.SEND.getName()).asBoolean(false)) {
                            send.add(role);
                        }
                        if (perms.get(SecurityRoleAdd.CONSUME.getName()).asBoolean(false)) {
                            consume.add(role);
                        }
                        if (perms.get(SecurityRoleAdd.CREATE_DURABLE_QUEUE.getName()).asBoolean(false)) {
                            createDurableQueue.add(role);
                        }
                        if (perms.get(SecurityRoleAdd.DELETE_DURABLE_QUEUE.getName()).asBoolean(false)) {
                            deleteDurableQueue.add(role);
                        }
                        if (perms.get(SecurityRoleAdd.CREATE_NON_DURABLE_QUEUE.getName()).asBoolean(false)) {
                            createNonDurableQueue.add(role);
                        }
                        if (perms.get(SecurityRoleAdd.DELETE_NON_DURABLE_QUEUE.getName()).asBoolean(false)) {
                            deleteNonDurableQueue.add(role);
                        }
                        if (perms.get(SecurityRoleAdd.MANAGE.getName()).asBoolean(false)) {
                            manageRoles.add(role);
                        }
                    }

                    writePermission(writer, SecurityRoleAdd.SEND.getXmlName(), send);
                    writePermission(writer, SecurityRoleAdd.CONSUME.getXmlName(), consume);
                    writePermission(writer, SecurityRoleAdd.CREATE_DURABLE_QUEUE.getXmlName(), createDurableQueue);
                    writePermission(writer, SecurityRoleAdd.DELETE_DURABLE_QUEUE.getXmlName(), deleteDurableQueue);
                    writePermission(writer, SecurityRoleAdd.CREATE_NON_DURABLE_QUEUE.getXmlName(), createNonDurableQueue);
                    writePermission(writer, SecurityRoleAdd.DELETE_NON_DURABLE_QUEUE.getXmlName(), deleteNonDurableQueue);
                    writePermission(writer, SecurityRoleAdd.MANAGE.getXmlName(), manageRoles);
                }

                writer.writeEndElement();
            }

            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    private void writePermission(final XMLExtendedStreamWriter writer, final String type, final List<String> roles) throws XMLStreamException {
        if (roles.size() == 0) {
            return;
        }
        writer.writeStartElement(Element.PERMISSION_ELEMENT_NAME.getLocalName());
        StringBuilder sb = new StringBuilder();
        for (String role : roles) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(role);
        }
        writer.writeAttribute(Attribute.TYPE_ATTR_NAME.getLocalName(), type);
        writer.writeAttribute(Attribute.ROLES_ATTR_NAME.getLocalName(), sb.toString());
        writer.writeEndElement();
    }

    private void writeAddressSettings(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            writer.writeStartElement(Element.ADDRESS_SETTINGS.getLocalName());
            for (Property matchSetting : properties) {
                writer.writeStartElement(Element.ADDRESS_SETTING.getLocalName());
                writer.writeAttribute(Attribute.MATCH.getLocalName(), matchSetting.getName());
                final ModelNode setting = matchSetting.getValue();
                CommonAttributes.DEAD_LETTER_ADDRESS.marshallAsElement(setting, writer);
                CommonAttributes.EXPIRY_ADDRESS.marshallAsElement(setting, writer);
                CommonAttributes.REDELIVERY_DELAY.marshallAsElement(setting, writer);
                CommonAttributes.MAX_DELIVERY_ATTEMPTS.marshallAsElement(setting, writer);
                CommonAttributes.MAX_SIZE_BYTES.marshallAsElement(setting, writer);
                CommonAttributes.PAGE_SIZE_BYTES.marshallAsElement(setting, writer);
                CommonAttributes.PAGE_MAX_CACHE_SIZE.marshallAsElement(setting, writer);
                CommonAttributes.ADDRESS_FULL_MESSAGE_POLICY.marshallAsElement(setting, writer);
                CommonAttributes.MESSAGE_COUNTER_HISTORY_DAY_LIMIT.marshallAsElement(setting, writer);
                CommonAttributes.LVQ.marshallAsElement(setting, writer);
                CommonAttributes.REDISTRIBUTION_DELAY.marshallAsElement(setting, writer);
                CommonAttributes.SEND_TO_DLA_ON_NO_ROUTE.marshallAsElement(setting, writer);
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    private void writeQueues(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            writer.writeStartElement(Element.CORE_QUEUES.getLocalName());
            for (Property queueProp : properties) {
                writer.writeStartElement(Element.QUEUE.getLocalName());
                writer.writeAttribute(Attribute.NAME.getLocalName(), queueProp.getName());
                final ModelNode queue = queueProp.getValue();
                QUEUE_ADDRESS.marshallAsElement(queue, writer);
                writeFilter(writer, queue);
                DURABLE.marshallAsElement(queue, writer);

                writer.writeEndElement();
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    private void writeBroadcastGroups(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            writer.writeStartElement(Element.BROADCAST_GROUPS.getLocalName());
            for(final Property property : properties) {
                writer.writeStartElement(Element.BROADCAST_GROUP.getLocalName());
                writer.writeAttribute(Attribute.NAME.getLocalName(), property.getName());
                for (AttributeDefinition attribute : BroadcastGroupDefinition.ATTRIBUTES) {
                    attribute.marshallAsElement(property.getValue(), writer);
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    private void writeDiscoveryGroups(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            writer.writeStartElement(Element.DISCOVERY_GROUPS.getLocalName());
            for(final Property property : properties) {
                writer.writeStartElement(Element.DISCOVERY_GROUP.getLocalName());
                writer.writeAttribute(Attribute.NAME.getLocalName(), property.getName());
                for (AttributeDefinition attribute : DiscoveryGroupDefinition.ATTRIBUTES) {
                    attribute.marshallAsElement(property.getValue(), writer);
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    private void writeDiverts(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            writer.writeStartElement(Element.DIVERTS.getLocalName());
            for(final Property property : properties) {
                writer.writeStartElement(Element.DIVERT.getLocalName());
                writer.writeAttribute(Attribute.NAME.getLocalName(), property.getName());
                for (AttributeDefinition attribute : DivertDefinition.ATTRIBUTES) {
                    if (CommonAttributes.FILTER == attribute) {
                        writeFilter(writer, property.getValue());
                    } else {
                        attribute.marshallAsElement(property.getValue(), writer);
                    }
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writeNewLine(writer);
        }
    }

    private void writeFilter(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        if (node.hasDefined(CommonAttributes.FILTER.getName())) {
            writer.writeEmptyElement(CommonAttributes.FILTER.getXmlName());
            writer.writeAttribute(CommonAttributes.STRING, node.get(CommonAttributes.FILTER.getName()).asString());
        }
    }

    static void writeDirectory(final XMLExtendedStreamWriter writer, final Element element, final ModelNode node) throws XMLStreamException {
        final String localName = element.getLocalName();
        if(node.has(localName)) {
            final String path = node.get(localName).has(PATH.getName()) ? node.get(localName, PATH.getName()).asString() : null;
            final String relativeTo = node.get(localName).hasDefined(RELATIVE_TO.getName()) ? node.get(localName, RELATIVE_TO.getName()).asString() : null;
            if(path != null || relativeTo != null) {
                writer.writeEmptyElement(localName);
                if(path != null) writer.writeAttribute(PATH.getName(), path);
                if(relativeTo != null) writer.writeAttribute(RELATIVE_TO.getName(), relativeTo);
            }
        }
    }


    private void writeConnectionFactories(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            for (Property prop : properties) {
                final String name = prop.getName();
                final ModelNode factory = prop.getValue();
                if (factory.isDefined()) {
                   writer.writeStartElement(Element.CONNECTION_FACTORY.getLocalName());
                   writer.writeAttribute(Attribute.NAME.getLocalName(), name);

                   writeRegularConnectionFactoryAttributes(writer, name, factory);
                   writeCommonConnectionFactoryAttributes(writer, name, factory);
                }
            }
        }
    }

    private void writePooledConnectionFactories(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            for (Property prop : properties) {
                final String name = prop.getName();
                final ModelNode factory = prop.getValue();
                if (factory.isDefined()) {
                   writer.writeStartElement(Element.POOLED_CONNECTION_FACTORY.getLocalName());

                   writer.writeAttribute(Attribute.NAME.getLocalName(), name);

                   writePooledConnectionFactoryAttributes(writer, name, factory);
                   writeCommonConnectionFactoryAttributes(writer, name, factory);
                }
            }
        }
    }

    protected void writeRegularConnectionFactoryAttributes(XMLExtendedStreamWriter writer, String name, ModelNode factory) throws XMLStreamException {
        CommonAttributes.CONNECTION_FACTORY_TYPE.marshallAsElement(factory, writer);
    }

    protected void writePooledConnectionFactoryAttributes(XMLExtendedStreamWriter writer, String name, ModelNode factory) throws XMLStreamException {
        PCF_USER.marshallAsElement(factory, writer);
        PCF_PASSWORD.marshallAsElement(factory, writer);

        if(factory.hasDefined(INBOUND_CONFIG)) {
            final ModelNode inboundConfigs = factory.get(INBOUND_CONFIG);
            if (inboundConfigs.getType() == ModelType.LIST) {
                writer.writeStartElement(Element.INBOUND_CONFIG.getLocalName());
                for (ModelNode config : inboundConfigs.asList()) {
                    if (config.isDefined()) {
                        CommonAttributes.USE_JNDI.marshallAsElement(config, writer);
                        CommonAttributes.JNDI_PARAMS.marshallAsElement(config, writer);
                        CommonAttributes.USE_LOCAL_TX.marshallAsElement(config, writer);
                        CommonAttributes.SETUP_ATTEMPTS.marshallAsElement(config, writer);
                        CommonAttributes.SETUP_INTERVAL.marshallAsElement(config, writer);
                    }
                }
                writer.writeEndElement();
            }
        }

        if(factory.hasDefined(TRANSACTION)) {
            writer.writeStartElement(Element.TRANSACTION.getLocalName());
            writeTransactionTypeAttribute(writer, Element.MODE, factory.get(TRANSACTION));
            writer.writeEndElement();
        }
    }

    protected void writeCommonConnectionFactoryAttributes(XMLExtendedStreamWriter writer, String name, ModelNode factory) throws XMLStreamException {
        if (CommonAttributes.DISCOVERY_GROUP_NAME.isMarshallable(factory)) {
            writer.writeStartElement(Element.DISCOVERY_GROUP_REF.getLocalName());
            CommonAttributes.DISCOVERY_GROUP_NAME.marshallAsAttribute(factory, writer);
            writer.writeEndElement();
        }

        // write the element for compatibility sake but it is deprecated
        CommonAttributes.DISCOVERY_INITIAL_WAIT_TIMEOUT.marshallAsElement(factory, writer);

        if (factory.hasDefined(CONNECTOR)) {
            writer.writeStartElement(Element.CONNECTORS.getLocalName());
            for (Property connProp : factory.get(CONNECTOR).asPropertyList()) {
                writer.writeStartElement(Element.CONNECTOR_REF.getLocalName());
                writer.writeAttribute(Attribute.CONNECTOR_NAME.getLocalName(), connProp.getName());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }

        JndiEntriesAttribute.CONNECTION_FACTORY.marshallAsElement(factory, writer);

        CommonAttributes.HA.marshallAsElement(factory, writer);
        CommonAttributes.CLIENT_FAILURE_CHECK_PERIOD.marshallAsElement(factory, writer);
        CommonAttributes.CONNECTION_TTL.marshallAsElement(factory, writer);
        CommonAttributes.CALL_TIMEOUT.marshallAsElement(factory, writer);
        CommonAttributes.CONSUMER_WINDOW_SIZE.marshallAsElement(factory, writer);
        CommonAttributes.CONSUMER_MAX_RATE.marshallAsElement(factory, writer);
        CommonAttributes.CONFIRMATION_WINDOW_SIZE.marshallAsElement(factory, writer);
        CommonAttributes.PRODUCER_WINDOW_SIZE.marshallAsElement(factory, writer);
        CommonAttributes.PRODUCER_MAX_RATE.marshallAsElement(factory, writer);
        CommonAttributes.CACHE_LARGE_MESSAGE_CLIENT.marshallAsElement(factory, writer);
        CommonAttributes.MIN_LARGE_MESSAGE_SIZE.marshallAsElement(factory, writer);
        CommonAttributes.CLIENT_ID.marshallAsElement(factory, writer);
        CommonAttributes.DUPS_OK_BATCH_SIZE.marshallAsElement(factory, writer);
        CommonAttributes.TRANSACTION_BATCH_SIZE.marshallAsElement(factory, writer);
        CommonAttributes.BLOCK_ON_ACK.marshallAsElement(factory, writer);
        CommonAttributes.BLOCK_ON_NON_DURABLE_SEND.marshallAsElement(factory, writer);
        CommonAttributes.BLOCK_ON_DURABLE_SEND.marshallAsElement(factory, writer);
        CommonAttributes.AUTO_GROUP.marshallAsElement(factory, writer);
        CommonAttributes.PRE_ACK.marshallAsElement(factory, writer);
        CommonAttributes.RETRY_INTERVAL.marshallAsElement(factory, writer);
        CommonAttributes.RETRY_INTERVAL_MULTIPLIER.marshallAsElement(factory, writer);
        CommonAttributes.MAX_RETRY_INTERVAL.marshallAsElement(factory, writer);
        CommonAttributes.CONNECTION_FACTORY_RECONNECT_ATTEMPTS.marshallAsElement(factory, writer);
        CommonAttributes.FAILOVER_ON_INITIAL_CONNECTION.marshallAsElement(factory, writer);
        CommonAttributes.FAILOVER_ON_SERVER_SHUTDOWN.marshallAsElement(factory, writer);
        CommonAttributes.LOAD_BALANCING_CLASS_NAME.marshallAsElement(factory, writer);
        CommonAttributes.USE_GLOBAL_POOLS.marshallAsElement(factory, writer);
        CommonAttributes.CONNECTION_SCHEDULED_THREAD_POOL_MAX_SIZE.marshallAsElement(factory, writer);
        CommonAttributes.CONNECTION_THREAD_POOL_MAX_SIZE.marshallAsElement(factory, writer);
        CommonAttributes.GROUP_ID.marshallAsElement(factory, writer);

        writer.writeEndElement();
    }

    private void writeJmsQueues(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            for (Property prop : properties) {
                final String name = prop.getName();
                final ModelNode queue = prop.getValue();
                if (queue.isDefined()) {
                    writer.writeStartElement(Element.JMS_QUEUE.getLocalName());
                    writer.writeAttribute(Attribute.NAME.getLocalName(), name);
                    ENTRIES.marshallAsElement(queue, writer);
                    DURABLE.marshallAsElement(queue, writer);
                    SELECTOR.marshallAsElement(queue, writer);
                    writer.writeEndElement();
                }
            }
        }
    }

    private void writeTopics(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> properties = node.asPropertyList();
        if (!properties.isEmpty()) {
            for (Property prop : properties) {
                final String name = prop.getName();
                final ModelNode topic = prop.getValue();
                if (topic.isDefined()) {
                    writer.writeStartElement(Element.JMS_TOPIC.getLocalName());
                    writer.writeAttribute(Attribute.NAME.getLocalName(), name);
                    ENTRIES.marshallAsElement(topic, writer);
                    writer.writeEndElement();
                }
            }
        }
    }

    private void writeTransactionTypeAttribute(final XMLExtendedStreamWriter writer, final Element attr, final ModelNode value) throws XMLStreamException {
        writer.writeAttribute(attr.getLocalName(), value.asString());
    }

    static void writeAttribute(final XMLExtendedStreamWriter writer, final Attribute attr, final ModelNode value) throws XMLStreamException {
        writer.writeAttribute(attr.getLocalName(), value.asString());
    }


    static void processJMSTopic(final XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {

        final String name = reader.getAttributeValue(0);
        if(name == null) {
            ParseUtils.missingRequired(reader, Collections.singleton("name"));
        }

        final ModelNode topic = new ModelNode();
        topic.get(OP).set(ADD);
        topic.get(OP_ADDR).set(address).add(JMS_TOPIC, name);

        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch(element) {
                case ENTRY: {
                    final String entry = readStringAttributeElement(reader, CommonAttributes.NAME);
                    ENTRIES.parseAndAddParameterElement(entry, topic, reader);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
        updates.add(topic);
    }

    static void processJMSQueue(final XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {

        requireSingleAttribute(reader, CommonAttributes.NAME);
        final String name = reader.getAttributeValue(0);

        final ModelNode queue = new ModelNode();
        queue.get(OP).set(ADD);
        queue.get(OP_ADDR).set(address).add(JMS_QUEUE, name);

        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch(element) {
                case ENTRY: {
                    final String entry = readStringAttributeElement(reader, CommonAttributes.NAME);
                    ENTRIES.parseAndAddParameterElement(entry, queue, reader);
                    break;
                } case SELECTOR: {
                    if(queue.has(SELECTOR.getName())) {
                        throw ParseUtils.duplicateNamedElement(reader, Element.SELECTOR.getLocalName());
                    }
                    requireSingleAttribute(reader, CommonAttributes.STRING);
                    final String selector = readStringAttributeElement(reader, CommonAttributes.STRING);
                    SELECTOR.parseAndSetParameter(selector, queue, reader);
                    break;
                } case DURABLE: {
                    if(queue.has(DURABLE.getName())) {
                        throw ParseUtils.duplicateNamedElement(reader, Element.DURABLE.getLocalName());
                    }
                    handleElementText(reader, element, queue);
                    break;
                } default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
       updates.add(queue);
    }

    void processConnectionFactory(final XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {

        requireSingleAttribute(reader, CommonAttributes.NAME);
        final String name = reader.getAttributeValue(0);

        final ModelNode connectionFactory = new ModelNode();
        connectionFactory.get(OP).set(ADD);
        connectionFactory.get(OP_ADDR).set(address).add(CONNECTION_FACTORY, name);

        updates.add(createConnectionFactory(reader, connectionFactory, false));
    }

    void processPooledConnectionFactory(final XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> updates) throws XMLStreamException {
        final String name = reader.getAttributeValue(0);
        if(name == null) {
            ParseUtils.missingRequired(reader, Collections.singleton("name"));
        }

        final ModelNode connectionFactory = new ModelNode();
        connectionFactory.get(OP).set(ADD);
        connectionFactory.get(OP_ADDR).set(address).add(POOLED_CONNECTION_FACTORY, name);

        updates.add(createConnectionFactory(reader, connectionFactory, true));
    }

    static ModelNode processJmsConnectors(final XMLExtendedStreamReader reader) throws XMLStreamException {
        final ModelNode connectors = new ModelNode();
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            String name = null;
            int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                final String value = reader.getAttributeValue(i);
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case CONNECTOR_NAME: {
                        name = value.trim();
                        break;
                    } case BACKUP_CONNECTOR_NAME: {
                        MessagingLogger.ROOT_LOGGER.deprecatedXMLAttribute(attribute.toString());
                        break;
                    } default: {
                        throw ParseUtils.unexpectedAttribute(reader, i);
                    }
                }
            }
            if(name == null) {
                throw ParseUtils.missingRequired(reader, Collections.singleton(Attribute.CONNECTOR_NAME));
            }
            final Element element = Element.forName(reader.getLocalName());
            if(element != Element.CONNECTOR_REF) {
                throw ParseUtils.unexpectedElement(reader);
            }
            ParseUtils.requireNoContent(reader);

            // create the connector node
            connectors.get(name);
        }
        return connectors;
    }

    protected ModelNode createConnectionFactory(XMLExtendedStreamReader reader, ModelNode connectionFactory, boolean pooled) throws XMLStreamException
    {
        Set<Element> seen = EnumSet.noneOf(Element.class);
        while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            if (!seen.add(element)) {
                throw ParseUtils.duplicateNamedElement(reader, element.getLocalName());
            }
            switch(element) {
                // =========================================================
                // elements common to regular & pooled connection factories
                case DISCOVERY_GROUP_REF: {
                    checkOtherElementIsNotAlreadyDefined(reader, seen, Element.DISCOVERY_GROUP_REF, Element.CONNECTORS);
                    final String groupRef = readStringAttributeElement(reader, DISCOVERY_GROUP_NAME.getXmlName());
                    DISCOVERY_GROUP_NAME.parseAndSetParameter(groupRef, connectionFactory, reader);
                    break;
                } case CONNECTORS: {
                    checkOtherElementIsNotAlreadyDefined(reader, seen, Element.CONNECTORS, Element.DISCOVERY_GROUP_REF);
                    connectionFactory.get(CONNECTOR).set(processJmsConnectors(reader));
                    break;
                } case ENTRIES: {
                    while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                        final Element local = Element.forName(reader.getLocalName());
                        if(local != Element.ENTRY ) {
                            throw ParseUtils.unexpectedElement(reader);
                        }
                        final String entry = readStringAttributeElement(reader, CommonAttributes.NAME);
                        JndiEntriesAttribute.CONNECTION_FACTORY.parseAndAddParameterElement(entry, connectionFactory, reader);
                    }
                    break;
                }
                case HA:
                case CLIENT_FAILURE_CHECK_PERIOD:
                case CONNECTION_TTL:
                case CALL_TIMEOUT:
                case CONSUMER_WINDOW_SIZE:
                case CONSUMER_MAX_RATE:
                case CONFIRMATION_WINDOW_SIZE:
                case PRODUCER_WINDOW_SIZE:
                case PRODUCER_MAX_RATE:
                case CACHE_LARGE_MESSAGE_CLIENT:
                case MIN_LARGE_MESSAGE_SIZE:
                case CLIENT_ID:
                case DUPS_OK_BATCH_SIZE:
                case TRANSACTION_BATH_SIZE:
                case BLOCK_ON_ACK:
                case BLOCK_ON_NON_DURABLE_SEND:
                case BLOCK_ON_DURABLE_SEND:
                case AUTO_GROUP:
                case PRE_ACK:
                case RETRY_INTERVAL_MULTIPLIER:
                case MAX_RETRY_INTERVAL:
                case FAILOVER_ON_INITIAL_CONNECTION:
                case FAILOVER_ON_SERVER_SHUTDOWN:
                case LOAD_BALANCING_CLASS_NAME:
                case USE_GLOBAL_POOLS:
                case GROUP_ID:
                    handleElementText(reader, element, connectionFactory);
                    break;
                case RETRY_INTERVAL:
                    // Use the "default" variant
                    handleElementText(reader, element, DEFAULT, connectionFactory);
                    break;
                case RECONNECT_ATTEMPTS:
                case SCHEDULED_THREAD_POOL_MAX_SIZE:
                case THREAD_POOL_MAX_SIZE:
                    // Use the "connection" variant
                    handleElementText(reader, element, "connection", connectionFactory);
                    break;
                case DISCOVERY_INITIAL_WAIT_TIMEOUT:
                    MessagingLogger.ROOT_LOGGER.deprecatedXMLElement(element.toString());
                    skipElementText(reader);
                    break;
                // end of common elements
                // =========================================================

                // =========================================================
                // elements specific to regular (non-pooled) connection factories
                case CONNECTION_FACTORY_TYPE:
                    if(pooled) {
                        throw unexpectedElement(reader);
                    }
                    handleElementText(reader, element, connectionFactory);
                    break;
                // end of regular CF elements
                // =========================================================

                // =========================================================
                // elements specific to pooled connection factories
                case INBOUND_CONFIG: {
                    if(!pooled) {
                        throw unexpectedElement(reader);
                    }
                    while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                        final Element local = Element.forName(reader.getLocalName());
                        switch (local) {
                            case USE_JNDI:
                            case JNDI_PARAMS:
                            case USE_LOCAL_TX:
                            case SETUP_ATTEMPTS:
                            case SETUP_INTERVAL:
                                handleElementText(reader, local, connectionFactory);
                                break;
                            default:
                                throw unexpectedElement(reader);
                        }
                    }
                    break;
                } case TRANSACTION: {
                    if(!pooled) {
                        throw ParseUtils.unexpectedElement(reader);
                    }
                    final String txType = reader.getAttributeValue(0);
                    if( txType != null) {
                        connectionFactory.get(TRANSACTION).set(txType);
                    }
                    ParseUtils.requireNoContent(reader);
                    break;
                }
                case USER:
                    if(!pooled) {
                        throw unexpectedElement(reader);
                    }
                    // Element name is overloaded, handleElementText can not be used, we must use the correct attribute
                    CommonAttributes.PCF_USER.parseAndSetParameter(reader.getElementText(), connectionFactory, reader);
                    break;
                case PASSWORD:
                    if(!pooled) {
                        throw unexpectedElement(reader);
                    }
                    // Element name is overloaded, handleElementText can not be used, we must use the correct attribute
                    CommonAttributes.PCF_PASSWORD.parseAndSetParameter(reader.getElementText(), connectionFactory, reader);
                    break;
                // end of pooled CF elements
                // =========================================================
                default: {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }

        checkOnlyOneOfElements(reader, seen, Element.CONNECTORS, Element.DISCOVERY_GROUP_REF);

        return connectionFactory;
    }

    protected static void writeNewLine(XMLExtendedStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters(NEW_LINE, 0, 1);
    }

    protected void checkOtherElementIsNotAlreadyDefined(XMLStreamReader reader, Set<Element> seen, Element currentElement, Element otherElement) throws XMLStreamException {
        if (seen.contains(otherElement)) {
            throw new XMLStreamException(MESSAGES.illegalElement(currentElement.getLocalName(), otherElement.getLocalName()), reader.getLocation());
        }
    }

    /**
     * Check one and only one of the 2 elements has been defined
     */
    protected void checkOnlyOneOfElements(XMLExtendedStreamReader reader, Set<Element> seen, Element element1, Element element2) throws XMLStreamException {
        if (!seen.contains(element1) && !seen.contains(element2)) {
            throw new XMLStreamException(MESSAGES.required(element1.getLocalName(), element2.getLocalName()), reader.getLocation());
        }
        if (seen.contains(element1) && seen.contains(element2)) {
            throw new XMLStreamException(MESSAGES.onlyOneRequired(element1.getLocalName(), element2.getLocalName()), reader.getLocation());
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9707.java