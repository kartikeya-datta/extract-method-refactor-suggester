error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10680.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10680.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10680.java
text:
```scala
s@@etBoottime = true;

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

package org.jboss.as.server.parsing;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static org.jboss.as.controller.ControllerMessages.MESSAGES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ANY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ARCHIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.BOOT_TIME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CLIENT_MAPPINGS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CONTENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CORE_SERVICE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT_OVERLAY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESTINATION_ADDRESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESTINATION_PORT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ENABLED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HASH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAMESPACES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NOT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PATH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REGULAR_EXPRESSION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RELATIVE_TO;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNTIME_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SCHEMA_LOCATIONS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOURCE_NETWORK;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SYSTEM_PROPERTY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VAULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VAULT_OPTIONS;
import static org.jboss.as.controller.parsing.ParseUtils.duplicateNamedElement;
import static org.jboss.as.controller.parsing.ParseUtils.invalidAttributeValue;
import static org.jboss.as.controller.parsing.ParseUtils.isNoNamespaceAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequired;
import static org.jboss.as.controller.parsing.ParseUtils.parseBoundedIntegerAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.parsePossibleExpression;
import static org.jboss.as.controller.parsing.ParseUtils.requireNamespace;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoAttributes;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoContent;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoNamespaceAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.requireSingleAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedEndElement;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.HashUtil;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.common.NamespaceAddHandler;
import org.jboss.as.controller.operations.common.SchemaLocationAddHandler;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.parsing.Attribute;
import org.jboss.as.controller.parsing.Element;
import org.jboss.as.controller.parsing.Namespace;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.ModelMarshallingContext;
import org.jboss.as.controller.resource.AbstractSocketBindingResourceDefinition;
import org.jboss.as.controller.resource.SocketBindingGroupResourceDefinition;
import org.jboss.as.server.controller.resources.SystemPropertyResourceDefinition;
import org.jboss.as.server.operations.SystemPropertyAddHandler;
import org.jboss.as.server.services.net.LocalDestinationOutboundSocketBindingResourceDefinition;
import org.jboss.as.server.services.net.OutboundSocketBindingResourceDefinition;
import org.jboss.as.server.services.net.RemoteDestinationOutboundSocketBindingResourceDefinition;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * Bits of parsing and marshalling logic that are common across more than one of standalone.xml, domain.xml and host.xml.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public abstract class CommonXml implements XMLElementReader<List<ModelNode>>, XMLElementWriter<ModelMarshallingContext> {

    /**
     * The restricted path names.
     */
    protected static final Set<String> RESTRICTED_PATHS;

    static {

        final HashSet<String> set = new HashSet<String>(10);
        // Define the restricted path names.
        set.add("jboss.home");
        set.add("jboss.home.dir");
        set.add("user.home");
        set.add("user.dir");
        set.add("java.home");
        set.add("jboss.server.base.dir");
        set.add("jboss.server.data.dir");
        set.add("jboss.server.log.dir");
        set.add("jboss.server.temp.dir");
        // NOTE we actually don't create services for the following
        // however the names remain restricted for use in the configuration
        set.add("jboss.modules.dir");
        set.add("jboss.server.deploy.dir");
        set.add("jboss.domain.servers.dir");
        RESTRICTED_PATHS = Collections.unmodifiableSet(set);
    }

    private static final char[] NEW_LINE = new char[]{'\n'};

    protected CommonXml() {
    }

    protected void parseNamespaces(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> nodes) {
        final int namespaceCount = reader.getNamespaceCount();
        for (int i = 0; i < namespaceCount; i++) {
            String prefix = reader.getNamespacePrefix(i);
            // FIXME - remove once STXM-8 is released
            if (prefix != null && prefix.length() > 0) {
                nodes.add(NamespaceAddHandler.getAddNamespaceOperation(address, prefix, reader.getNamespaceURI(i)));
            }
        }
    }

    protected void readHeadComment(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> nodes)
            throws XMLStreamException {
        // TODO STXM-6
    }

    protected void readTailComment(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> nodes)
            throws XMLStreamException {
        // TODO STXM-6
    }

    protected void parseSchemaLocations(final XMLExtendedStreamReader reader, final ModelNode address,
                                        final List<ModelNode> updateList, final int idx) throws XMLStreamException {
        final List<String> elements = reader.getListAttributeValue(idx);
        final List<String> values = new ArrayList<String>();
        for (String element : elements) {
            if (!element.trim().isEmpty()) {
                values.add(element);
            }
        }
        if ((values.size() & 1) != 0) {
            throw invalidAttributeValue(reader, idx);
        }
        final Iterator<String> it = values.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String val = it.next();
            if (key.length() > 0 && val.length() > 0) {
                updateList.add(SchemaLocationAddHandler.getAddSchemaLocationOperation(address, key, val));
            }
        }
    }

    protected void writeSchemaLocation(final XMLExtendedStreamWriter writer, final ModelNode modelNode)
            throws XMLStreamException {
        if (!modelNode.hasDefined(SCHEMA_LOCATIONS)) {
            return;
        }
        final StringBuilder b = new StringBuilder();
        final Iterator<ModelNode> iterator = modelNode.get(SCHEMA_LOCATIONS).asList().iterator();
        while (iterator.hasNext()) {
            final ModelNode location = iterator.next();
            final Property property = location.asProperty();
            b.append(property.getName()).append(' ').append(property.getValue().asString());
            if (iterator.hasNext()) {
                b.append(' ');
            }
        }
        if (b.length() > 0) {
            writer.writeAttribute(Namespace.XML_SCHEMA_INSTANCE.getUriString(), Attribute.SCHEMA_LOCATION.getLocalName(),
                    b.toString());
        }
    }

    protected void writeNamespaces(final XMLExtendedStreamWriter writer, final ModelNode modelNode) throws XMLStreamException {
        final boolean needXsd = modelNode.hasDefined(SCHEMA_LOCATIONS) && modelNode.get(SCHEMA_LOCATIONS).asInt() > 0;
        final boolean hasNamespaces = modelNode.hasDefined(NAMESPACES);
        if (!needXsd && !hasNamespaces) {
            return;
        }

        boolean wroteXsd = false;
        final String xsdUri = Namespace.XML_SCHEMA_INSTANCE.getUriString();
        if (hasNamespaces) {
            for (final Property property : modelNode.get(NAMESPACES).asPropertyList()) {
                final String uri = property.getValue().asString();
                writer.writeNamespace(property.getName(), uri);
                if (!wroteXsd && xsdUri.equals(uri)) {
                    wroteXsd = true;
                }
            }
        }
        if (needXsd && !wroteXsd) {
            writer.writeNamespace("xsd", xsdUri);
        }
    }

    protected static void writeElement(final XMLExtendedStreamWriter writer, final Element element) throws XMLStreamException {
        writer.writeStartElement(element.getLocalName());
    }

    protected void writePaths(final XMLExtendedStreamWriter writer, final ModelNode node) throws XMLStreamException {
        List<Property> paths = node.asPropertyList();

        for (Iterator<Property> it = paths.iterator(); it.hasNext(); ) {
            ModelNode path = it.next().getValue();

            if (!path.isDefined()) {
                //The runtime resources for the hardcoded paths don't appear in the model
                it.remove();
            }
        }

        if (paths.size() > 0) {
            writer.writeStartElement(Element.PATHS.getLocalName());

            for (final Property path : paths) {
                final ModelNode value = path.getValue();
                writer.writeEmptyElement(Element.PATH.getLocalName());
                writer.writeAttribute(Attribute.NAME.getLocalName(), path.getName());
                writer.writeAttribute(Attribute.PATH.getLocalName(), value.get(PATH).asString());
                if (value.has(RELATIVE_TO) && value.get(RELATIVE_TO).isDefined()) {
                    writer.writeAttribute(Attribute.RELATIVE_TO.getLocalName(), value.get(RELATIVE_TO).asString());
                }
            }
            writer.writeEndElement();
        }
    }

    protected void parseFSBaseType(final XMLExtendedStreamReader reader, final ModelNode parent, final boolean isArchive)
            throws XMLStreamException {
        final ModelNode content = parent.get("content").add();
        content.get(ARCHIVE).set(isArchive);
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            if (!isNoNamespaceAttribute(reader, i)) {
                throw unexpectedAttribute(reader, i);
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            final String value = reader.getAttributeValue(i);
            switch (attribute) {
                case PATH:
                    content.get(PATH).set(value);
                    break;
                case RELATIVE_TO:
                    content.get(RELATIVE_TO).set(value);
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        // Handle elements
        requireNoContent(reader);
    }

    protected void parsePaths(final XMLExtendedStreamReader reader, final ModelNode address, final Namespace expectedNs, final List<ModelNode> list,
                              final boolean requirePath) throws XMLStreamException {
        final Set<String> pathNames = new HashSet<String>();
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            requireNamespace(reader, expectedNs);

            switch (element) {
                case PATH: {
                    parsePath(reader, address, list, requirePath, pathNames);
                    break;
                }
                default: {
                    throw unexpectedElement(reader);
                }
            }
        }
    }

    protected void parsePath(final XMLExtendedStreamReader reader, final ModelNode address, final List<ModelNode> list,
                             final boolean requirePath, final Set<String> defined) throws XMLStreamException {
        String name = null;
        String path = null;
        String relativeTo = null;
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (!isNoNamespaceAttribute(reader, i)) {
                throw unexpectedAttribute(reader, i);
            } else {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case NAME: {
                        name = value.trim();
                        if (RESTRICTED_PATHS.contains(value)) {
                            throw MESSAGES.reserved(name, reader.getLocation());
                        }
                        if (!defined.add(name)) {
                            throw MESSAGES.alreadyDefined(name, reader.getLocation());
                        }
                        break;
                    }
                    case PATH: {
                        path = value;
                        break;
                    }
                    case RELATIVE_TO: {
                        relativeTo = value;
                        break;
                    }
                    default: {
                        throw unexpectedAttribute(reader, i);
                    }
                }
            }
        }
        if (name == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
        }
        if (requirePath && path == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.PATH));
        }
        requireNoContent(reader);
        final ModelNode update = new ModelNode();
        update.get(OP_ADDR).set(address).add(ModelDescriptionConstants.PATH, name);
        update.get(OP).set(ADD);
        update.get(NAME).set(name);
        if (path != null)
            update.get(PATH).set(path);
        if (relativeTo != null)
            update.get(RELATIVE_TO).set(relativeTo);
        list.add(update);
    }

    protected void parseSystemProperties(final XMLExtendedStreamReader reader, final ModelNode address, final Namespace expectedNs,
                                         final List<ModelNode> updates, boolean standalone) throws XMLStreamException {

        while (reader.nextTag() != END_ELEMENT) {
            requireNamespace(reader, expectedNs);
            final Element element = Element.forName(reader.getLocalName());
            if (element != Element.PROPERTY) {
                throw unexpectedElement(reader);
            }

            boolean setName = false;
            boolean setValue = false;
            boolean setBoottime = false;
            //Will set OP_ADDR after parsing the NAME attribute
            ModelNode op = Util.getEmptyOperation(SystemPropertyAddHandler.OPERATION_NAME, new ModelNode());
            final int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                final String val = reader.getAttributeValue(i);
                if (!isNoNamespaceAttribute(reader, i)) {
                    throw unexpectedAttribute(reader, i);
                } else {
                    final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));

                    switch (attribute) {
                        case NAME: {
                            if (setName) {
                                throw ParseUtils.duplicateAttribute(reader, NAME);
                            }
                            setName = true;
                            ModelNode addr = new ModelNode().set(address).add(SYSTEM_PROPERTY, val);
                            op.get(OP_ADDR).set(addr);
                            break;
                        }
                        case VALUE: {
                            if (setValue) {
                                throw ParseUtils.duplicateAttribute(reader, VALUE);
                            }
                            setValue = true;
                            SystemPropertyResourceDefinition.VALUE.parseAndSetParameter(val, op, reader);
                            break;
                        }
                        case BOOT_TIME: {
                            if (standalone) {
                                throw unexpectedAttribute(reader, i);
                            }
                            if (setBoottime) {
                                throw ParseUtils.duplicateAttribute(reader, BOOT_TIME);
                            }
                            setValue = true;
                            SystemPropertyResourceDefinition.BOOT_TIME.parseAndSetParameter(val, op, reader);
                            break;
                        }
                        default: {
                            throw unexpectedAttribute(reader, i);
                        }
                    }
                }
            }
            requireNoContent(reader);
            if (!setName) {
                throw ParseUtils.missingRequired(reader, Collections.singleton(NAME));
            }

            updates.add(op);
        }
    }

    protected void parseInterfaceCriteria(final XMLExtendedStreamReader reader, final Namespace expectedNs, final ModelNode interfaceModel)
            throws XMLStreamException {
        // all subsequent elements are criteria elements
        if (reader.nextTag() == END_ELEMENT) {
            return;
        }
        requireNamespace(reader, expectedNs);
        Element element = Element.forName(reader.getLocalName());
        switch (element) {
            case ANY_ADDRESS:
            case ANY_IPV4_ADDRESS:
            case ANY_IPV6_ADDRESS: {
                interfaceModel.get(element.getLocalName()).set(true);
                requireNoContent(reader); // consume this element
                requireNoContent(reader); // consume rest of criteria (no further content allowed)
                return;
            }
        }
        do {
            requireNamespace(reader, expectedNs);
            element = Element.forName(reader.getLocalName());
            switch (element) {
                case ANY:
                    parseCompoundInterfaceCriterion(reader, expectedNs, interfaceModel.get(ANY).setEmptyObject());
                    break;
                case NOT:
                    parseCompoundInterfaceCriterion(reader, expectedNs, interfaceModel.get(NOT).setEmptyObject());
                    break;
                default: {
                    // parseSimpleInterfaceCriterion(reader, criteria.add().set(element.getLocalName(), new
                    // ModelNode()).get(element.getLocalName()));
                    parseSimpleInterfaceCriterion(reader, interfaceModel, false);
                    break;
                }
            }
        } while (reader.nextTag() != END_ELEMENT);
    }

    protected void parseCompoundInterfaceCriterion(final XMLExtendedStreamReader reader, final Namespace expectedNs, final ModelNode subModel)
            throws XMLStreamException {
        requireNoAttributes(reader);
        while (reader.nextTag() != END_ELEMENT) {
            requireNamespace(reader, expectedNs);
            parseSimpleInterfaceCriterion(reader, subModel, true);
        }
    }

    protected void parseContentType(final XMLExtendedStreamReader reader, final ModelNode parent) throws XMLStreamException {
        final ModelNode content = parent.get("content").add();
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            if (!isNoNamespaceAttribute(reader, i)) {
                throw unexpectedAttribute(reader, i);
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            final String value = reader.getAttributeValue(i);
            switch (attribute) {
                case SHA1:
                    try {
                        content.get(HASH).set(HashUtil.hexStringToByteArray(value));
                    } catch (final Exception e) {
                        throw MESSAGES.invalidSha1Value(e, value, attribute.getLocalName(), reader.getLocation());
                    }
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        // Handle elements
        requireNoContent(reader);
    }

    /**
     * Creates the appropriate AbstractInterfaceCriteriaElement for simple criterion.
     * <p/>
     * Note! changes/additions made here will likely need to be added to the corresponding write method that handles the write
     * of the element. Failure to do so will result in a configuration that can be read, but not written out.
     *
     * @throws javax.xml.stream.XMLStreamException
     *          if an error occurs
     * @see {@link #writeInterfaceCriteria(org.jboss.staxmapper.XMLExtendedStreamWriter, org.jboss.dmr.ModelNode, boolean)}
     */
    protected void parseSimpleInterfaceCriterion(final XMLExtendedStreamReader reader, final ModelNode subModel, boolean nested)
            throws XMLStreamException {
        final Element element = Element.forName(reader.getLocalName());
        final String localName = element.getLocalName();
        switch (element) {
            case INET_ADDRESS: {
                requireSingleAttribute(reader, Attribute.VALUE.getLocalName());
                final String value = reader.getAttributeValue(0);
                ModelNode valueNode = parsePossibleExpression(value);
                requireNoContent(reader);
                // todo: validate IP address
                if (nested) {
                    subModel.get(localName).add(valueNode);
                } else {
                    subModel.get(localName).set(valueNode);
                }
                break;
            }
            case LOOPBACK_ADDRESS: {
                requireSingleAttribute(reader, Attribute.VALUE.getLocalName());
                final String value = reader.getAttributeValue(0);
                ModelNode valueNode = parsePossibleExpression(value);
                requireNoContent(reader);
                // todo: validate IP address
                subModel.get(localName).set(valueNode);
                break;
            }
            case LINK_LOCAL_ADDRESS:
            case LOOPBACK:
            case MULTICAST:
            case POINT_TO_POINT:
            case PUBLIC_ADDRESS:
            case SITE_LOCAL_ADDRESS:
            case UP:
            case VIRTUAL: {
                requireNoAttributes(reader);
                requireNoContent(reader);
                subModel.get(localName).set(true);
                break;
            }
            case NIC: {
                requireSingleAttribute(reader, Attribute.NAME.getLocalName());
                final String value = reader.getAttributeValue(0);
                requireNoContent(reader);
                // todo: validate NIC name
                if (nested) {
                    subModel.get(localName).add(value);
                } else {
                    subModel.get(localName).set(value);
                }
                break;
            }
            case NIC_MATCH: {
                requireSingleAttribute(reader, Attribute.PATTERN.getLocalName());
                final String value = reader.getAttributeValue(0);
                requireNoContent(reader);
                // todo: validate pattern
                if (nested) {
                    subModel.get(localName).add(value);
                } else {
                    subModel.get(localName).set(value);
                }
                break;
            }
            case SUBNET_MATCH: {
                requireSingleAttribute(reader, Attribute.VALUE.getLocalName());
                final String value = reader.getAttributeValue(0);
                requireNoContent(reader);

                validateAddressMask(value, reader.getLocation());

                if (nested) {
                    subModel.get(localName).add(value);
                } else {
                    subModel.get(localName).set(value);
                }
                break;
            }
            default:
                throw unexpectedElement(reader);
        }
    }

    private void validateAddressMask(String value, Location location) throws XMLStreamException {
        final String[] split = value.split("/");
        try {
            if (split.length != 2) {
                throw new XMLStreamException(MESSAGES.invalidAddressMaskValue(value), location);
            }
            // todo - possible DNS hit here
            final InetAddress addr = InetAddress.getByName(split[0]);
            // Validate both parts of the split
            addr.getAddress();
            Integer.parseInt(split[1]);

        } catch (final NumberFormatException e) {
            throw new XMLStreamException(MESSAGES.invalidAddressMask(split[1], e.getLocalizedMessage()),
                    location, e);
        } catch (final UnknownHostException e) {
            throw new XMLStreamException(MESSAGES.invalidAddressValue(split[0], e.getLocalizedMessage()),
                    location, e);
        }
    }

    protected void parseInterfaces(final XMLExtendedStreamReader reader, final Set<String> names, final ModelNode address,
                                   final Namespace expectedNs, final List<ModelNode> list, final boolean checkSpecified) throws XMLStreamException {
        requireNoAttributes(reader);

        while (reader.nextTag() != END_ELEMENT) {
            requireNamespace(reader, expectedNs);
            Element element = Element.forName(reader.getLocalName());
            if (Element.INTERFACE != element) {
                throw unexpectedElement(reader);
            }

            // Attributes
            requireSingleAttribute(reader, Attribute.NAME.getLocalName());
            final String name = reader.getAttributeValue(0);
            if (!names.add(name)) {
                throw MESSAGES.duplicateInterfaceDeclaration(reader.getLocation());
            }
            final ModelNode interfaceAdd = new ModelNode();
            interfaceAdd.get(OP_ADDR).set(address).add(ModelDescriptionConstants.INTERFACE, name);
            interfaceAdd.get(OP).set(ADD);

            final ModelNode criteriaNode = interfaceAdd;
            parseInterfaceCriteria(reader, expectedNs, interfaceAdd);

            if (checkSpecified && criteriaNode.getType() != ModelType.STRING && criteriaNode.getType() != ModelType.EXPRESSION
                    && criteriaNode.asInt() == 0) {
                throw unexpectedEndElement(reader);
            }
            list.add(interfaceAdd);
        }
    }

    protected void parseSocketBindingGroupRef(final XMLExtendedStreamReader reader, final ModelNode addOperation,
                                              final SimpleAttributeDefinition socketBindingGroup,
                                              final SimpleAttributeDefinition portOffset) throws XMLStreamException {
        // Handle attributes
        boolean gotRef = false;
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (!isNoNamespaceAttribute(reader, i)) {
                throw ParseUtils.unexpectedAttribute(reader, i);
            } else {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case REF: {
                        socketBindingGroup.parseAndSetParameter(value, addOperation, reader);
                        gotRef = true;
                        break;
                    }
                    case PORT_OFFSET: {
                        portOffset.parseAndSetParameter(value, addOperation, reader);
                        break;
                    }
                    default:
                        throw unexpectedAttribute(reader, i);
                }
            }
        }
        if (!gotRef) {
            throw missingRequired(reader, Collections.singleton(Attribute.REF));
        }

        // Handle elements
        requireNoContent(reader);
    }

    protected String parseSocketBinding(final XMLExtendedStreamReader reader, final Set<String> interfaces,
                                        final ModelNode address, final List<ModelNode> updates) throws XMLStreamException {

        final EnumSet<Attribute> required = EnumSet.of(Attribute.NAME);
        String name = null;

        final ModelNode binding = new ModelNode();
        binding.get(OP_ADDR); // undefined until we parse name
        binding.get(OP).set(ADD);

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (!isNoNamespaceAttribute(reader, i)) {
                throw unexpectedAttribute(reader, i);
            } else {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                required.remove(attribute);
                switch (attribute) {
                    case NAME: {
                        name = value;
                        binding.get(OP_ADDR).set(address).add(SOCKET_BINDING, name);
                        break;
                    }
                    case INTERFACE: {
                        AbstractSocketBindingResourceDefinition.INTERFACE.parseAndSetParameter(value, binding, reader);
                        if (binding.get(AbstractSocketBindingResourceDefinition.INTERFACE.getName()).getType() != ModelType.EXPRESSION
                                && !interfaces.contains(value)) {
                            throw MESSAGES.unknownInterface(value, attribute.getLocalName(),
                                    Element.INTERFACES.getLocalName(), reader.getLocation());
                        }
                        binding.get(INTERFACE).set(value);
                        break;
                    }
                    case PORT: {
                        AbstractSocketBindingResourceDefinition.PORT.parseAndSetParameter(value, binding, reader);
                        break;
                    }
                    case FIXED_PORT: {
                        AbstractSocketBindingResourceDefinition.FIXED_PORT.parseAndSetParameter(value, binding, reader);
                        break;
                    }
                    case MULTICAST_ADDRESS: {
                        AbstractSocketBindingResourceDefinition.MULTICAST_ADDRESS.parseAndSetParameter(value, binding, reader);
                        break;
                    }
                    case MULTICAST_PORT: {
                        AbstractSocketBindingResourceDefinition.MULTICAST_PORT.parseAndSetParameter(value, binding, reader);
                        break;
                    }
                    default:
                        throw unexpectedAttribute(reader, i);
                }
            }
        }

        if (!required.isEmpty()) {
            throw missingRequired(reader, required);
        }

        // Handle elements
        while (reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case CLIENT_MAPPING:
                    binding.get(CLIENT_MAPPINGS).add(parseClientMapping(reader));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        updates.add(binding);
        return name;
    }

    private ModelNode parseClientMapping(XMLExtendedStreamReader reader) throws XMLStreamException {
        final ModelNode mapping = new ModelNode();

        // Ensure all fields exist, even if not defined
        final ModelNode sourceNetwork = mapping.get(SOURCE_NETWORK);
        final ModelNode destination = mapping.get(DESTINATION_ADDRESS);
        final ModelNode destinationPort = mapping.get(DESTINATION_PORT);
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (!isNoNamespaceAttribute(reader, i)) {
                throw unexpectedAttribute(reader, i);
            }

            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case SOURCE_NETWORK:
                    validateAddressMask(value, reader.getLocation());
                    sourceNetwork.set(value);
                    break;
                case DESTINATION_ADDRESS:
                    if (value == null || value.isEmpty()) {
                        throw invalidAttributeValue(reader, i);
                    }
                    // We can't validate the address since the client is allowed to resolve private DNS names
                    destination.set(value);
                    break;
                case DESTINATION_PORT: {
                    destinationPort.set(parseBoundedIntegerAttribute(reader, i, 0, 65535, true));
                    break;
                }
            }
        }
        if (!destination.isDefined()) {
            throw MESSAGES.missingRequiredAttributes(new StringBuilder(DESTINATION_ADDRESS), reader.getLocation());
        }

        requireNoContent(reader);

        return mapping;
    }

    protected String parseOutboundSocketBinding(final XMLExtendedStreamReader reader, final Set<String> interfaces,
                                                final ModelNode address, final List<ModelNode> updates) throws XMLStreamException {

        final EnumSet<Attribute> required = EnumSet.of(Attribute.NAME);
        String outboundSocketBindingName = null;

        final ModelNode outboundSocketBindingAddOperation = new ModelNode();
        outboundSocketBindingAddOperation.get(OP).set(ADD); // address for this ADD operation will be set later, once the local-destination or remote-destination is parsed

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (!isNoNamespaceAttribute(reader, i)) {
                throw unexpectedAttribute(reader, i);
            } else {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                required.remove(attribute);
                switch (attribute) {
                    case NAME: {
                        outboundSocketBindingName = value;
                        break;
                    }
                    case SOURCE_INTERFACE: {
                        OutboundSocketBindingResourceDefinition.SOURCE_INTERFACE.parseAndSetParameter(value, outboundSocketBindingAddOperation, reader);
                        if (!interfaces.contains(value)
                                && outboundSocketBindingAddOperation.get(OutboundSocketBindingResourceDefinition.SOURCE_INTERFACE.getName()).getType() != ModelType.EXPRESSION) {
                            throw MESSAGES.unknownValueForElement(Attribute.SOURCE_INTERFACE.getLocalName(), value,
                                    Element.INTERFACE.getLocalName(), Element.INTERFACES.getLocalName(), reader.getLocation());
                        }
                        break;
                    }
                    case SOURCE_PORT: {
                        OutboundSocketBindingResourceDefinition.SOURCE_PORT.parseAndSetParameter(value, outboundSocketBindingAddOperation, reader);
                        break;
                    }
                    case FIXED_SOURCE_PORT: {
                        OutboundSocketBindingResourceDefinition.FIXED_SOURCE_PORT.parseAndSetParameter(value, outboundSocketBindingAddOperation, reader);
                        break;
                    }
                    default:
                        throw unexpectedAttribute(reader, i);
                }
            }
        }

        if (!required.isEmpty()) {
            throw missingRequired(reader, required);
        }
        // Handle elements
        boolean mutuallyExclusiveElementAlreadyFound = false;
        while (reader.hasNext() && reader.nextTag() != XMLStreamConstants.END_ELEMENT) {
            switch (Element.forName(reader.getLocalName())) {
                case LOCAL_DESTINATION: {
                    if (mutuallyExclusiveElementAlreadyFound) {
                        throw MESSAGES.invalidOutboundSocketBinding(outboundSocketBindingName, Element.LOCAL_DESTINATION.getLocalName(),
                                Element.REMOTE_DESTINATION.getLocalName(), reader.getLocation());
                    } else {
                        mutuallyExclusiveElementAlreadyFound = true;
                    }
                    // parse the local destination outbound socket binding
                    this.parseLocalDestinationOutboundSocketBinding(reader, outboundSocketBindingAddOperation);
                    // set the address of the add operation
                    // /socket-binding-group=<groupname>/local-destination-outbound-socket-binding=<outboundSocketBindingName>
                    final ModelNode addr = address.clone().add(LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING, outboundSocketBindingName);
                    outboundSocketBindingAddOperation.get(OP_ADDR).set(addr);
                    break;
                }
                case REMOTE_DESTINATION: {
                    if (mutuallyExclusiveElementAlreadyFound) {
                        throw MESSAGES.invalidOutboundSocketBinding(outboundSocketBindingName, Element.LOCAL_DESTINATION.getLocalName(),
                                Element.REMOTE_DESTINATION.getLocalName(), reader.getLocation());
                    } else {
                        mutuallyExclusiveElementAlreadyFound = true;
                    }
                    // parse the remote destination outbound socket binding
                    this.parseRemoteDestinationOutboundSocketBinding(reader, outboundSocketBindingAddOperation);
                    // /socket-binding-group=<groupname>/remote-destination-outbound-socket-binding=<outboundSocketBindingName>
                    final ModelNode addr = address.clone().add(REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING, outboundSocketBindingName);
                    outboundSocketBindingAddOperation.get(OP_ADDR).set(addr);
                    break;
                }
                default: {
                    throw unexpectedElement(reader);
                }
            }
        }

        // add the "add" operations to the updates
        updates.add(outboundSocketBindingAddOperation);
        return outboundSocketBindingName;
    }

    private void parseLocalDestinationOutboundSocketBinding(final XMLExtendedStreamReader reader,
                                                            final ModelNode outboundSocketBindingAddOperation) throws XMLStreamException {

        final EnumSet<Attribute> required = EnumSet.of(Attribute.SOCKET_BINDING_REF);

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (!isNoNamespaceAttribute(reader, i)) {
                throw unexpectedAttribute(reader, i);
            } else {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                required.remove(attribute);
                switch (attribute) {
                    case SOCKET_BINDING_REF: {
                        LocalDestinationOutboundSocketBindingResourceDefinition.SOCKET_BINDING_REF.parseAndSetParameter(value, outboundSocketBindingAddOperation, reader);
                        break;
                    }
                    default:
                        throw unexpectedAttribute(reader, i);
                }
            }
        }

        if (!required.isEmpty()) {
            throw missingRequired(reader, required);
        }
        // Handle elements
        requireNoContent(reader);
    }

    private void parseRemoteDestinationOutboundSocketBinding(final XMLExtendedStreamReader reader,
                                                             final ModelNode outboundSocketBindingAddOperation) throws XMLStreamException {

        final EnumSet<Attribute> required = EnumSet.of(Attribute.HOST, Attribute.PORT);

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (!isNoNamespaceAttribute(reader, i)) {
                throw unexpectedAttribute(reader, i);
            } else {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                required.remove(attribute);
                switch (attribute) {
                    case HOST: {
                        RemoteDestinationOutboundSocketBindingResourceDefinition.HOST.parseAndSetParameter(value, outboundSocketBindingAddOperation, reader);
                        break;
                    }
                    case PORT: {
                        RemoteDestinationOutboundSocketBindingResourceDefinition.PORT.parseAndSetParameter(value, outboundSocketBindingAddOperation, reader);
                        break;
                    }
                    default:
                        throw unexpectedAttribute(reader, i);
                }
            }
        }

        if (!required.isEmpty()) {
            throw missingRequired(reader, required);
        }
        // Handle elements
        requireNoContent(reader);
    }

    protected void parseDeployments(final XMLExtendedStreamReader reader, final ModelNode address, final Namespace expectedNs, final List<ModelNode> list,
                                    final Set<Attribute> allowedAttributes, final Set<Element> allowedElements) throws XMLStreamException {
        requireNoAttributes(reader);

        final Set<String> names = new HashSet<String>();

        while (reader.nextTag() != END_ELEMENT) {
            requireNamespace(reader, expectedNs);
            Element deployment = Element.forName(reader.getLocalName());
            if (Element.DEPLOYMENT != deployment) {
                throw unexpectedElement(reader);
            }

            // Handle attributes
            String uniqueName = null;
            String runtimeName = null;
            String startInput = null;
            final int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                final String value = reader.getAttributeValue(i);
                if (!isNoNamespaceAttribute(reader, i)) {
                    throw unexpectedAttribute(reader, i);
                } else {
                    final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                    if (!allowedAttributes.contains(attribute)) {
                        throw unexpectedAttribute(reader, i);
                    }
                    switch (attribute) {
                        case NAME: {
                            if (!names.add(value)) {
                                throw duplicateNamedElement(reader, value);
                            }
                            uniqueName = value;
                            break;
                        }
                        case RUNTIME_NAME: {
                            runtimeName = value;
                            break;
                        }
                        case ENABLED: {
                            startInput = value;
                            break;
                        }
                        default:
                            throw unexpectedAttribute(reader, i);
                    }
                }
            }
            if (uniqueName == null) {
                throw missingRequired(reader, Collections.singleton(Attribute.NAME));
            }
            if (runtimeName == null) {
                throw missingRequired(reader, Collections.singleton(Attribute.RUNTIME_NAME));
            }
            final boolean enabled = startInput == null ? true : Boolean.parseBoolean(startInput);

            final ModelNode deploymentAddress = address.clone().add(DEPLOYMENT, uniqueName);
            final ModelNode deploymentAdd = Util.getEmptyOperation(ADD, deploymentAddress);

            // Handle elements
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                requireNamespace(reader, expectedNs);
                final Element element = Element.forName(reader.getLocalName());
                if (!allowedElements.contains(element)) {
                    throw unexpectedElement(reader);
                }
                switch (element) {
                    case CONTENT:
                        parseContentType(reader, deploymentAdd);
                        break;
                    case FS_ARCHIVE:
                        parseFSBaseType(reader, deploymentAdd, true);
                        break;
                    case FS_EXPLODED:
                        parseFSBaseType(reader, deploymentAdd, false);
                        break;
                    default:
                        throw unexpectedElement(reader);
                }
            }

            deploymentAdd.get(RUNTIME_NAME).set(runtimeName);
            if (allowedAttributes.contains(Attribute.ENABLED)) {
                deploymentAdd.get(ENABLED).set(enabled);
            }
            list.add(deploymentAdd);
        }
    }


    protected void parseDeploymentOverlays(final XMLExtendedStreamReader reader, final Namespace namespace, final ModelNode baseAddress, final List<ModelNode> list) throws XMLStreamException {
        requireNoAttributes(reader);

        while (reader.nextTag() != END_ELEMENT) {
            requireNamespace(reader, namespace);
            final Element element = Element.forName(reader.getLocalName());

            switch (element) {
                case DEPLOYMENT_OVERLAY:
                    parseDeploymentOverlay(reader, baseAddress, list);
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }
    }

    protected void parseDeploymentOverlay(final XMLExtendedStreamReader reader, final ModelNode baseAddress, final List<ModelNode> list) throws XMLStreamException {

        final EnumSet<Attribute> required = EnumSet.of(Attribute.NAME);
        String name = null;
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            required.remove(attribute);
            switch (attribute) {
                case NAME: {
                    name = value;
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }


        if (required.size() > 0) {
            throw missingRequired(reader, required);
        }
        ModelNode addr = baseAddress.clone();
        addr.add(DEPLOYMENT_OVERLAY, name);

        final ModelNode op = new ModelNode();
        op.get(OP).set(ADD);
        op.get(OP_ADDR).set(addr);
        list.add(op);

        while (reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());

            switch (element) {
                case CONTENT:
                    parseContentOverride(name, reader, baseAddress, list);
                    break;
                case DEPLOYMENT:
                    parseDeploymentOverlayDeployment(name, reader, baseAddress, list);
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }
    }

    protected void parseContentOverride(final String name, final XMLExtendedStreamReader reader, final ModelNode baseAddress, final List<ModelNode> list) throws XMLStreamException {

        final EnumSet<Attribute> required = EnumSet.of(Attribute.PATH, Attribute.CONTENT);
        String path = null;
        byte[] content = null;
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            required.remove(attribute);
            switch (attribute) {
                case PATH: {
                    path = value;
                    break;
                }
                case CONTENT: {
                    content = HashUtil.hexStringToByteArray(value);
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        requireNoContent(reader);


        if (required.size() > 0) {
            throw missingRequired(reader, required);
        }

        final ModelNode address = baseAddress.clone();
        address.add(DEPLOYMENT_OVERLAY, name);
        address.add(CONTENT, path);

        final ModelNode op = new ModelNode();
        op.get(OP).set(ADD);
        op.get(OP_ADDR).set(address);
        op.get(CONTENT).get(HASH).set(content);
        list.add(op);

    }


    protected void parseDeploymentOverlayDeployment(final String name, final XMLExtendedStreamReader reader, final ModelNode baseAddress, final List<ModelNode> list) throws XMLStreamException {

        final EnumSet<Attribute> required = EnumSet.of(Attribute.NAME);
        String depName = null;
        boolean regEx = false;
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            required.remove(attribute);
            switch (attribute) {
                case NAME: {
                    depName = value;
                    break;
                }
                case REGULAR_EXPRESSION: {
                    regEx = Boolean.parseBoolean(value);
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        requireNoContent(reader);


        if (required.size() > 0) {
            throw missingRequired(reader, required);
        }

        final ModelNode address = baseAddress.clone();
        address.add(DEPLOYMENT_OVERLAY, name);
        address.add(DEPLOYMENT, depName);

        final ModelNode op = new ModelNode();
        op.get(OP).set(ADD);
        op.get(OP_ADDR).set(address);
        op.get(REGULAR_EXPRESSION).set(regEx);
        list.add(op);

    }

    protected void parseVault(final XMLExtendedStreamReader reader, final ModelNode address, final Namespace expectedNs, final List<ModelNode> list) throws XMLStreamException {
        final int vaultAttribCount = reader.getAttributeCount();

        ModelNode vault = new ModelNode();
        String code = null;

        if (vaultAttribCount > 1) {
            throw unexpectedAttribute(reader, vaultAttribCount);
        }

        for (int i = 0; i < vaultAttribCount; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case CODE: {
                    code = value;
                    vault.get(Attribute.CODE.getLocalName()).set(code);
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        ModelNode vaultAddress = address.clone();
        vaultAddress.add(CORE_SERVICE, VAULT);
        if (code != null) {
            vault.get(Attribute.CODE.getLocalName()).set(code);
        }
        vault.get(OP_ADDR).set(vaultAddress);
        vault.get(OP).set(ADD);

        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            requireNamespace(reader, expectedNs);
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case VAULT_OPTION: {
                    parseModuleOption(reader, vault.get(VAULT_OPTIONS));
                    break;
                }
            }
        }
        list.add(vault);
    }

    protected void parseVaultOption(XMLExtendedStreamReader reader, ModelNode vaultOptions) throws XMLStreamException {
        String name = null;
        String val = null;
        EnumSet<Attribute> required = EnumSet.of(Attribute.NAME, Attribute.VALUE);
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            required.remove(attribute);
            switch (attribute) {
                case NAME: {
                    name = value;
                    break;
                }
                case VALUE: {
                    val = value;
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        if (required.size() > 0) {
            throw missingRequired(reader, required);
        }

        vaultOptions.get(name).set(val);
        requireNoContent(reader);
    }

    protected void parseModuleOption(XMLExtendedStreamReader reader, ModelNode moduleOptions) throws XMLStreamException {
        String name = null;
        String val = null;
        EnumSet<Attribute> required = EnumSet.of(Attribute.NAME, Attribute.VALUE);
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            required.remove(attribute);
            switch (attribute) {
                case NAME: {
                    name = value;
                    break;
                }
                case VALUE: {
                    val = value;
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        if (required.size() > 0) {
            throw missingRequired(reader, required);
        }

        moduleOptions.add(name, val);
        requireNoContent(reader);
    }

    /**
     * Write the interfaces including the criteria elements.
     *
     * @param writer    the xml stream writer
     * @param modelNode the model
     * @throws XMLStreamException
     */
    protected void writeInterfaces(final XMLExtendedStreamWriter writer, final ModelNode modelNode) throws XMLStreamException {
        writer.writeStartElement(Element.INTERFACES.getLocalName());
        final Set<String> interfaces = modelNode.keys();
        for (String ifaceName : interfaces) {
            final ModelNode iface = modelNode.get(ifaceName);
            writer.writeStartElement(Element.INTERFACE.getLocalName());
            writeAttribute(writer, Attribute.NAME, ifaceName);
            // <any-* /> is just handled at the root
            if (iface.get(Element.ANY_ADDRESS.getLocalName()).asBoolean(false)) {
                writer.writeEmptyElement(Element.ANY_ADDRESS.getLocalName());
            } else if (iface.get(Element.ANY_IPV4_ADDRESS.getLocalName()).asBoolean(false)) {
                writer.writeEmptyElement(Element.ANY_IPV4_ADDRESS.getLocalName());
            } else if (iface.get(Element.ANY_IPV6_ADDRESS.getLocalName()).asBoolean(false)) {
                writer.writeEmptyElement(Element.ANY_IPV6_ADDRESS.getLocalName());
            } else {
                // Write the other criteria elements
                writeInterfaceCriteria(writer, iface, false);
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    /**
     * Write the criteria elements, extracting the information of the sub-model.
     *
     * @param writer   the xml stream writer
     * @param subModel the interface model
     * @param nested   whether it the criteria elements are nested as part of <not /> or <any />
     * @throws XMLStreamException
     */
    private void writeInterfaceCriteria(final XMLExtendedStreamWriter writer, final ModelNode subModel, final boolean nested) throws XMLStreamException {
        for (final Property property : subModel.asPropertyList()) {
            if (property.getValue().isDefined()) {
                writeInterfaceCriteria(writer, property, nested);
            }
        }
    }

    private void writeInterfaceCriteria(final XMLExtendedStreamWriter writer, final Property property, final boolean nested) throws XMLStreamException {
        final Element element = Element.forName(property.getName());
        switch (element) {
            case INET_ADDRESS:
                writeInterfaceCriteria(writer, element, Attribute.VALUE, property.getValue(), nested);
                break;
            case LOOPBACK_ADDRESS:
                writeInterfaceCriteria(writer, element, Attribute.VALUE, property.getValue(), false);
                break;
            case LINK_LOCAL_ADDRESS:
            case LOOPBACK:
            case MULTICAST:
            case POINT_TO_POINT:
            case PUBLIC_ADDRESS:
            case SITE_LOCAL_ADDRESS:
            case UP:
            case VIRTUAL: {
                if (property.getValue().asBoolean(false)) {
                    writer.writeEmptyElement(element.getLocalName());
                }
                break;
            }
            case NIC:
                writeInterfaceCriteria(writer, element, Attribute.NAME, property.getValue(), nested);
                break;
            case NIC_MATCH:
                writeInterfaceCriteria(writer, element, Attribute.PATTERN, property.getValue(), nested);
                break;
            case SUBNET_MATCH:
                writeInterfaceCriteria(writer, element, Attribute.VALUE, property.getValue(), nested);
                break;
            case ANY:
            case NOT:
                if (nested) {
                    break;
                }
                writer.writeStartElement(element.getLocalName());
                writeInterfaceCriteria(writer, property.getValue(), true);
                writer.writeEndElement();
                break;
            case NAME:
                // not a criteria element; ignore
                break;
            default: {
                // TODO we perhaps should just log a warning.
                throw MESSAGES.unknownCriteriaInterfaceProperty(property.getName());
            }
        }
    }

    private static void writeInterfaceCriteria(final XMLExtendedStreamWriter writer, final Element element, final Attribute attribute, final ModelNode subModel, boolean asList) throws XMLStreamException {
        if (asList) {
            // Nested criteria elements are represented as list in the model
            writeListAsMultipleElements(writer, element, attribute, subModel);
        } else {
            writeSingleElement(writer, element, attribute, subModel);
        }
    }

    private static void writeSingleElement(final XMLExtendedStreamWriter writer, final Element element, final Attribute attribute, final ModelNode subModel) throws XMLStreamException {
        writer.writeEmptyElement(element.getLocalName());
        writeAttribute(writer, attribute, subModel.asString());
    }

    private static void writeListAsMultipleElements(final XMLExtendedStreamWriter writer, final Element element, Attribute attribute, final ModelNode subModel) throws XMLStreamException {
        final List<ModelNode> list = subModel.asList();
        for (final ModelNode node : list) {
            writer.writeEmptyElement(element.getLocalName());
            writeAttribute(writer, attribute, node.asString());
        }
    }

    protected void writeSocketBindingGroup(XMLExtendedStreamWriter writer, ModelNode bindingGroup, boolean fromServer)
            throws XMLStreamException {

        writer.writeStartElement(Element.SOCKET_BINDING_GROUP.getLocalName());

        SocketBindingGroupResourceDefinition.NAME.marshallAsAttribute(bindingGroup, writer);
        SocketBindingGroupResourceDefinition.DEFAULT_INTERFACE.marshallAsAttribute(bindingGroup, writer);

        if (fromServer) {
            SocketBindingGroupResourceDefinition.PORT_OFFSET.marshallAsAttribute(bindingGroup, writer);
        }
        if (!fromServer) {
            SocketBindingGroupResourceDefinition.INCLUDES.marshallAsElement(bindingGroup, writer);
        }

        if (bindingGroup.hasDefined(SOCKET_BINDING)) {
            ModelNode bindings = bindingGroup.get(SOCKET_BINDING);
            for (String bindingName : bindings.keys()) {
                ModelNode binding = bindings.get(bindingName);
                writer.writeStartElement(Element.SOCKET_BINDING.getLocalName());
                writeAttribute(writer, Attribute.NAME, bindingName);
                AbstractSocketBindingResourceDefinition.INTERFACE.marshallAsAttribute(binding, writer);
                AbstractSocketBindingResourceDefinition.PORT.marshallAsAttribute(binding, writer);
                AbstractSocketBindingResourceDefinition.FIXED_PORT.marshallAsAttribute(binding, writer);
                AbstractSocketBindingResourceDefinition.MULTICAST_ADDRESS.marshallAsAttribute(binding, writer);
                AbstractSocketBindingResourceDefinition.MULTICAST_PORT.marshallAsAttribute(binding, writer);

                // TODO do this in ClientMappingsAttributeDefinition
                ModelNode attr = binding.get(CLIENT_MAPPINGS);
                if (attr.isDefined()) {
                    for (ModelNode mapping : attr.asList()) {
                        writer.writeEmptyElement(Element.CLIENT_MAPPING.getLocalName());

                        attr = mapping.get(SOURCE_NETWORK);
                        if (attr.isDefined()) {
                            writeAttribute(writer, Attribute.SOURCE_NETWORK, attr.asString());
                        }

                        attr = mapping.get(DESTINATION_ADDRESS);
                        if (attr.isDefined()) {
                            writeAttribute(writer, Attribute.DESTINATION_ADDRESS, attr.asString());
                        }

                        attr = mapping.get(DESTINATION_PORT);
                        if (attr.isDefined()) {
                            writeAttribute(writer, Attribute.DESTINATION_PORT, attr.asString());
                        }
                    }
                }

                writer.writeEndElement();

            }
        }
        // outbound-socket-binding (for local destination)
        if (bindingGroup.hasDefined(LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING)) {
            final ModelNode localDestinationOutboundSocketBindings = bindingGroup.get(LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING);
            for (final String outboundSocketBindingName : localDestinationOutboundSocketBindings.keys()) {
                final ModelNode outboundSocketBinding = localDestinationOutboundSocketBindings.get(outboundSocketBindingName);
                // <outbound-socket-binding>
                writer.writeStartElement(Element.OUTBOUND_SOCKET_BINDING.getLocalName());
                // name of the outbound socket binding
                writeAttribute(writer, Attribute.NAME, outboundSocketBindingName);
                // (optional) source interface
                OutboundSocketBindingResourceDefinition.SOURCE_INTERFACE.marshallAsAttribute(outboundSocketBinding, writer);
                // (optional) source port
                OutboundSocketBindingResourceDefinition.SOURCE_PORT.marshallAsAttribute(outboundSocketBinding, writer);
                // (optional) fixedSourcePort
                OutboundSocketBindingResourceDefinition.FIXED_SOURCE_PORT.marshallAsAttribute(outboundSocketBinding, writer);
                // write the <local-destination> element
                writer.writeEmptyElement(Element.LOCAL_DESTINATION.getLocalName());
                // socket-binding-ref
                LocalDestinationOutboundSocketBindingResourceDefinition.SOCKET_BINDING_REF.marshallAsAttribute(outboundSocketBinding, writer);
                // </outbound-socket-binding>
                writer.writeEndElement();
            }
        }
        // outbound-socket-binding (for remote destination)
        if (bindingGroup.hasDefined(REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING)) {
            final ModelNode remoteDestinationOutboundSocketBindings = bindingGroup.get(REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING);
            for (final String outboundSocketBindingName : remoteDestinationOutboundSocketBindings.keys()) {
                final ModelNode outboundSocketBinding = remoteDestinationOutboundSocketBindings.get(outboundSocketBindingName);
                // <outbound-socket-binding>
                writer.writeStartElement(Element.OUTBOUND_SOCKET_BINDING.getLocalName());
                // name of the outbound socket binding
                writeAttribute(writer, Attribute.NAME, outboundSocketBindingName);
                // (optional) source interface
                OutboundSocketBindingResourceDefinition.SOURCE_INTERFACE.marshallAsAttribute(outboundSocketBinding, writer);
                // (optional) source port
                OutboundSocketBindingResourceDefinition.SOURCE_PORT.marshallAsAttribute(outboundSocketBinding, writer);
                // (optional) fixedSourcePort
                OutboundSocketBindingResourceDefinition.FIXED_SOURCE_PORT.marshallAsAttribute(outboundSocketBinding, writer);
                // write the <remote-destination> element
                writer.writeEmptyElement(Element.REMOTE_DESTINATION.getLocalName());
                // destination host
                RemoteDestinationOutboundSocketBindingResourceDefinition.HOST.marshallAsAttribute(outboundSocketBinding, writer);
                // destination port
                RemoteDestinationOutboundSocketBindingResourceDefinition.PORT.marshallAsAttribute(outboundSocketBinding, writer);
                // </outbound-socket-binding>
                writer.writeEndElement();
            }
        }
        // </socket-binding-group>
        writer.writeEndElement();
    }

    protected void writeProperties(final XMLExtendedStreamWriter writer, final ModelNode modelNode, Element element,
                                   boolean standalone) throws XMLStreamException {
        final List<Property> properties = modelNode.asPropertyList();
        if (properties.size() > 0) {
            writer.writeStartElement(element.getLocalName());
            for (Property prop : properties) {
                writer.writeStartElement(Element.PROPERTY.getLocalName());
                writeAttribute(writer, Attribute.NAME, prop.getName());
                ModelNode sysProp = prop.getValue();
                SystemPropertyResourceDefinition.VALUE.marshallAsAttribute(sysProp, writer);
                if (!standalone) {
                    SystemPropertyResourceDefinition.BOOT_TIME.marshallAsAttribute(sysProp, writer);
                }

                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    protected static void writeAttribute(XMLExtendedStreamWriter writer, Attribute attribute, String value)
            throws XMLStreamException {
        writer.writeAttribute(attribute.getLocalName(), value);
    }

    protected static void writeContentItem(final XMLExtendedStreamWriter writer, final ModelNode contentItem)
            throws XMLStreamException {
        if (contentItem.has(HASH)) {
            writeElement(writer, Element.CONTENT);
            writeAttribute(writer, Attribute.SHA1, HashUtil.bytesToHexString(contentItem.require(HASH).asBytes()));
            writer.writeEndElement();
        } else {
            if (contentItem.require(ARCHIVE).asBoolean()) {
                writeElement(writer, Element.FS_ARCHIVE);
            } else {
                writeElement(writer, Element.FS_EXPLODED);
            }
            writeAttribute(writer, Attribute.PATH, contentItem.require(PATH).asString());
            if (contentItem.has(RELATIVE_TO))
                writeAttribute(writer, Attribute.RELATIVE_TO, contentItem.require(RELATIVE_TO).asString());
            writer.writeEndElement();
        }
    }

    protected void writeVault(XMLExtendedStreamWriter writer, ModelNode vault) throws XMLStreamException {
        writer.writeStartElement(Element.VAULT.getLocalName());
        String code = vault.hasDefined(Attribute.CODE.getLocalName()) ? vault.get(Attribute.CODE.getLocalName()).asString() : null;
        if (code != null && !code.isEmpty()) {
            writer.writeAttribute(Attribute.CODE.getLocalName(), code);
        }

        if (vault.hasDefined(VAULT_OPTIONS)) {
            ModelNode properties = vault.get(VAULT_OPTIONS);
            for (Property prop : properties.asPropertyList()) {
                writer.writeEmptyElement(Element.VAULT_OPTION.getLocalName());
                writer.writeAttribute(Attribute.NAME.getLocalName(), prop.getName());
                writer.writeAttribute(Attribute.VALUE.getLocalName(), prop.getValue().asString());
            }
        }
        writer.writeEndElement();
    }

    protected static void writeNewLine(XMLExtendedStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters(NEW_LINE, 0, 1);
    }


    protected void writeDeploymentOverlays(final XMLExtendedStreamWriter writer, final ModelNode modelNode)
            throws XMLStreamException {

        Set<String> names = modelNode.keys();
        if (names.size() > 0) {
            writer.writeStartElement(Element.DEPLOYMENT_OVERLAYS.getLocalName());
            for (String uniqueName : names) {
                final ModelNode contentItem = modelNode.get(uniqueName);
                writer.writeStartElement(Element.DEPLOYMENT_OVERLAY.getLocalName());
                writeAttribute(writer, Attribute.NAME, uniqueName);

                if (contentItem.hasDefined(CONTENT)) {
                    final ModelNode overridesNode = contentItem.get(CONTENT);

                    final Set<String> overrides = overridesNode.keys();
                    for (final String override : overrides) {
                        final ModelNode overrideNode = overridesNode.get(override);
                        final String content = HashUtil.bytesToHexString(overrideNode.require(CONTENT).asBytes());
                        writer.writeStartElement(Element.CONTENT.getLocalName());
                        writeAttribute(writer, Attribute.PATH, override);
                        writeAttribute(writer, Attribute.CONTENT, content);
                        writer.writeEndElement();
                    }
                }

                if (contentItem.hasDefined(DEPLOYMENT)) {
                    final ModelNode deployments = contentItem.get(DEPLOYMENT);
                    Set<String> deploymentNames = deployments.keys();
                    if (deploymentNames.size() > 0) {
                        for (String deploymentName : deploymentNames) {
                            final ModelNode depNode = deployments.get(deploymentName);
                            final boolean regEx = depNode.hasDefined(REGULAR_EXPRESSION) ? depNode.get(REGULAR_EXPRESSION).asBoolean() : false;
                            writer.writeStartElement(Element.DEPLOYMENT.getLocalName());
                            writeAttribute(writer, Attribute.NAME, deploymentName);
                            if (regEx) {
                                writeAttribute(writer, Attribute.REGULAR_EXPRESSION, "true");
                            }
                            writer.writeEndElement();
                        }
                    }
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writeNewLine(writer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10680.java