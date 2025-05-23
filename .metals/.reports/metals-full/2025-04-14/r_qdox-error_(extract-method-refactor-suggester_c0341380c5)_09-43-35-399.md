error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3420.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3420.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3420.java
text:
```scala
public static final S@@tring SUBSYSTEM_NAME = "jca";

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
package org.jboss.as.connector.subsystems.jca;

import static org.jboss.as.connector.logging.ConnectorLogger.ROOT_LOGGER;
import static org.jboss.as.connector.subsystems.jca.Constants.ARCHIVE_VALIDATION;
import static org.jboss.as.connector.subsystems.jca.Constants.BEAN_VALIDATION;
import static org.jboss.as.connector.subsystems.jca.Constants.BOOTSTRAP_CONTEXT;
import static org.jboss.as.connector.subsystems.jca.Constants.CACHED_CONNECTION_MANAGER;
import static org.jboss.as.connector.subsystems.jca.Constants.DEFAULT_NAME;
import static org.jboss.as.connector.subsystems.jca.Constants.JCA;
import static org.jboss.as.connector.subsystems.jca.Constants.WORKMANAGER;
import static org.jboss.as.connector.subsystems.jca.Constants.WORKMANAGER_LONG_RUNNING;
import static org.jboss.as.connector.subsystems.jca.Constants.WORKMANAGER_SHORT_RUNNING;
import static org.jboss.as.connector.subsystems.jca.JcaArchiveValidationDefinition.ArchiveValidationParameters;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequiredElement;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoContent;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;

import java.util.EnumSet;
import java.util.List;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.threads.ThreadsParser;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * @author <a href="mailto:stefano.maestri@redhat.com">Stefano Maestri</a>
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public class JcaExtension implements Extension {

    public static String SUBSYSTEM_NAME = "jca";

    private static final int MANAGEMENT_API_MAJOR_VERSION = 1;
    private static final int MANAGEMENT_API_MINOR_VERSION = 2;
    private static final int MANAGEMENT_API_MICRO_VERSION = 0;

    private static final String RESOURCE_NAME = JcaExtension.class.getPackage().getName() + ".LocalDescriptions";


    static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String... keyPrefix) {
            StringBuilder prefix = new StringBuilder(SUBSYSTEM_NAME);
            for (String kp : keyPrefix) {
                prefix.append('.').append(kp);
            }
            return new StandardResourceDescriptionResolver(prefix.toString(), RESOURCE_NAME, JcaExtension.class.getClassLoader(), true, false);
        }

    @Override
    public void initialize(final ExtensionContext context) {
        ROOT_LOGGER.debugf("Initializing Connector Extension");

        final boolean registerRuntimeOnly = context.isRuntimeOnlyRegistrationValid();

        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, MANAGEMENT_API_MAJOR_VERSION,
                MANAGEMENT_API_MINOR_VERSION, MANAGEMENT_API_MICRO_VERSION);

        subsystem.registerSubsystemModel(JcaSubsystemRootDefinition.createInstance(registerRuntimeOnly));

        subsystem.registerXMLElementWriter(ConnectorSubsystemParser.INSTANCE);

        if (context.isRegisterTransformers()) {
            JcaSubsystemRootDefinition.registerTransformers(subsystem);
        }
    }

    @Override
    public void initializeParsers(final ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, Namespace.JCA_1_0.getUriString(), ConnectorSubsystemParser.INSTANCE);
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, Namespace.JCA_1_1.getUriString(), ConnectorSubsystemParser.INSTANCE);
    }

    static final class ConnectorSubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>,
            XMLElementWriter<SubsystemMarshallingContext> {

        static final ConnectorSubsystemParser INSTANCE = new ConnectorSubsystemParser();

        /**
         * {@inheritDoc}
         */
        @Override
        public void writeContent(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {
            context.startSubsystemElement(Namespace.CURRENT.getUriString(), false);
            ModelNode node = context.getModelNode();

            writeArchiveValidation(writer, node);
            writeBeanValidation(writer, node);
            writeWorkManagers(writer, node);
            writeBootstrapContexts(writer, node);
            writeCachedConnectionManager(writer, node);
            writer.writeEndElement();
        }

        private void writeArchiveValidation(XMLExtendedStreamWriter writer, ModelNode parentNode) throws XMLStreamException {
            if (parentNode.hasDefined(ARCHIVE_VALIDATION)) {
                ModelNode node = parentNode.get(ARCHIVE_VALIDATION).get(ARCHIVE_VALIDATION);
                if (ArchiveValidationParameters.ARCHIVE_VALIDATION_ENABLED.getAttribute().isMarshallable(node) ||
                        ArchiveValidationParameters.ARCHIVE_VALIDATION_FAIL_ON_ERROR.getAttribute().isMarshallable(node) ||
                        ArchiveValidationParameters.ARCHIVE_VALIDATION_FAIL_ON_WARN.getAttribute().isMarshallable(node)) {
                    writer.writeEmptyElement(Element.ARCHIVE_VALIDATION.getLocalName());
                    ArchiveValidationParameters.ARCHIVE_VALIDATION_ENABLED.getAttribute().marshallAsAttribute(node, writer);
                    ArchiveValidationParameters.ARCHIVE_VALIDATION_FAIL_ON_ERROR.getAttribute().marshallAsAttribute(node, writer);
                    ArchiveValidationParameters.ARCHIVE_VALIDATION_FAIL_ON_WARN.getAttribute().marshallAsAttribute(node, writer);

                }
            }
        }

        private void writeBeanValidation(XMLExtendedStreamWriter writer, ModelNode parentNode) throws XMLStreamException {
            if (parentNode.hasDefined(BEAN_VALIDATION)) {
                ModelNode node = parentNode.get(BEAN_VALIDATION).get(BEAN_VALIDATION);

                if (JcaBeanValidationDefinition.BeanValidationParameters.BEAN_VALIDATION_ENABLED.getAttribute().isMarshallable(node)) {
                    writer.writeEmptyElement(Element.BEAN_VALIDATION.getLocalName());
                    JcaBeanValidationDefinition.BeanValidationParameters.BEAN_VALIDATION_ENABLED.getAttribute().marshallAsAttribute(node, writer);
                }
            }
        }

        private void writeCachedConnectionManager(XMLExtendedStreamWriter writer, ModelNode parentNode) throws XMLStreamException {
            if (parentNode.hasDefined(CACHED_CONNECTION_MANAGER)) {
                ModelNode node = parentNode.get(CACHED_CONNECTION_MANAGER).get(CACHED_CONNECTION_MANAGER);

                final String name = JcaCachedConnectionManagerDefinition.CcmParameters.INSTALL.getAttribute().getName();
                if (node.hasDefined(name) &&
                        node.get(name).asBoolean()) {
                    writer.writeEmptyElement(Element.CACHED_CONNECTION_MANAGER.getLocalName());
                    JcaCachedConnectionManagerDefinition.CcmParameters.DEBUG.getAttribute().marshallAsAttribute(node, writer);
                    JcaCachedConnectionManagerDefinition.CcmParameters.ERROR.getAttribute().marshallAsAttribute(node, writer);
                }
            }
        }

        private void writeWorkManagers(XMLExtendedStreamWriter writer, ModelNode parentNode) throws XMLStreamException {
            if (parentNode.hasDefined(WORKMANAGER) && parentNode.get(WORKMANAGER).asList().size() != 0) {
                for (Property property : parentNode.get(WORKMANAGER).asPropertyList()) {
                    if ("default".equals(property.getValue().get(NAME).asString())) {
                        writer.writeStartElement(Element.DEFAULT_WORKMANAGER.getLocalName());
                    } else {
                        writer.writeStartElement(Element.WORKMANAGER.getLocalName());
                        JcaWorkManagerDefinition.WmParameters.NAME.getAttribute().marshallAsAttribute(property.getValue(), writer);
                    }
                    for (Property prop : property.getValue().asPropertyList()) {
                        if (WORKMANAGER_LONG_RUNNING.equals(prop.getName()) && prop.getValue().isDefined() && prop.getValue().asPropertyList().size() != 0) {
                            ThreadsParser.getInstance().writeBoundedQueueThreadPool(writer, prop.getValue().asProperty(), Element.LONG_RUNNING_THREADS.getLocalName(), false);
                        }
                        if (WORKMANAGER_SHORT_RUNNING.equals(prop.getName()) && prop.getValue().isDefined() && prop.getValue().asPropertyList().size() != 0) {
                            ThreadsParser.getInstance().writeBoundedQueueThreadPool(writer, prop.getValue().asProperty(), Element.SHORT_RUNNING_THREADS.getLocalName(), false);
                        }
                    }
                    writer.writeEndElement();
                }
            }
        }


        private void writeBootstrapContexts(XMLExtendedStreamWriter writer, ModelNode parentNode) throws XMLStreamException {
            if (parentNode.hasDefined(BOOTSTRAP_CONTEXT) && parentNode.get(BOOTSTRAP_CONTEXT).asList().size() != 0) {

                boolean started = false;

                for (Property property : parentNode.get(BOOTSTRAP_CONTEXT).asPropertyList()) {
                    if (!property.getValue().get(JcaBootstrapContextDefinition.BootstrapCtxParameters.NAME.getAttribute().getName()).asString().equals(DEFAULT_NAME) &&
                            (JcaBootstrapContextDefinition.BootstrapCtxParameters.NAME.getAttribute().isMarshallable(property.getValue()) ||
                                    JcaBootstrapContextDefinition.BootstrapCtxParameters.WORKMANAGER.getAttribute().isMarshallable(property.getValue()))) {
                        if (!started) {
                            writer.writeStartElement(Element.BOOTSTRAP_CONTEXTS.getLocalName());
                            started = true;
                        }
                        writer.writeStartElement(Element.BOOTSTRAP_CONTEXT.getLocalName());
                        JcaBootstrapContextDefinition.BootstrapCtxParameters.NAME.getAttribute().marshallAsAttribute(property.getValue(), writer);
                        JcaBootstrapContextDefinition.BootstrapCtxParameters.WORKMANAGER.getAttribute().marshallAsAttribute(property.getValue(), writer);
                        writer.writeEndElement();
                    }
                }
                if (started) {
                    writer.writeEndElement();
                }
            }
        }

        @Override
        public void readElement(final XMLExtendedStreamReader reader, final List<ModelNode> list) throws XMLStreamException {

            final ModelNode address = new ModelNode();
            address.add(org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM, JCA);
            address.protect();

            final ModelNode subsystem = new ModelNode();
            subsystem.get(OP).set(ADD);
            subsystem.get(OP_ADDR).set(address);
            list.add(subsystem);

            // Handle elements
            final EnumSet<Element> visited = EnumSet.noneOf(Element.class);
            final EnumSet<Element> requiredElement = EnumSet.of(Element.DEFAULT_WORKMANAGER);
            boolean ccmAdded = false;
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {

                switch (Namespace.forUri(reader.getNamespaceURI())) {
                    case JCA_1_1:
                    case JCA_1_0: {
                        final Element element = Element.forName(reader.getLocalName());
                        if (!visited.add(element)) {
                            throw unexpectedElement(reader);
                        }

                        switch (element) {
                            case ARCHIVE_VALIDATION: {
                                list.add(parseArchiveValidation(reader, address));
                                break;
                            }
                            case BEAN_VALIDATION: {
                                list.add(parseBeanValidation(reader, address));
                                break;
                            }
                            case DEFAULT_WORKMANAGER: {
                                parseWorkManager(reader, address, list, subsystem, true);
                                final ModelNode bootstrapContextOperation = new ModelNode();
                                bootstrapContextOperation.get(OP).set(ADD);
                                final ModelNode bootStrapCOntextAddress = address.clone();
                                bootStrapCOntextAddress.add(BOOTSTRAP_CONTEXT, DEFAULT_NAME);
                                bootStrapCOntextAddress.protect();

                                bootstrapContextOperation.get(OP_ADDR).set(bootStrapCOntextAddress);
                                bootstrapContextOperation.get(WORKMANAGER).set(DEFAULT_NAME);
                                bootstrapContextOperation.get(NAME).set(DEFAULT_NAME);
                                list.add(bootstrapContextOperation);

                                requiredElement.remove(Element.DEFAULT_WORKMANAGER);

                                break;
                            }
                            case CACHED_CONNECTION_MANAGER: {
                                list.add(parseCcm(reader, address));
                                ccmAdded = true;
                                break;
                            }
                            case WORKMANAGER: {
                                parseWorkManager(reader, address, list, subsystem, false);
                                // AS7-4434 Multiple work managers are allowed
                                visited.remove(Element.WORKMANAGER);
                                break;
                            }
                            case BOOTSTRAP_CONTEXTS: {
                                parseBootstrapContexts(reader, address, list);
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
            if (!requiredElement.isEmpty()) {
                throw missingRequiredElement(reader, requiredElement);
            }
            if (!ccmAdded) {
                final ModelNode ccmOperation = new ModelNode();
                ccmOperation.get(OP).set(ADD);

                final ModelNode ccmAddress = address.clone();
                ccmAddress.add(CACHED_CONNECTION_MANAGER, CACHED_CONNECTION_MANAGER);
                ccmAddress.protect();

                ccmOperation.get(OP_ADDR).set(ccmAddress);
                list.add(ccmOperation);
            }
        }

        private ModelNode parseArchiveValidation(final XMLExtendedStreamReader reader, final ModelNode parentOperation)
                throws XMLStreamException {
            final ModelNode archiveValidationOperation = new ModelNode();
            archiveValidationOperation.get(OP).set(ADD);

            final ModelNode archiveValidationAddress = parentOperation.clone();
            archiveValidationAddress.add(ARCHIVE_VALIDATION, ARCHIVE_VALIDATION);
            archiveValidationAddress.protect();

            archiveValidationOperation.get(OP_ADDR).set(archiveValidationAddress);


            final int cnt = reader.getAttributeCount();
            for (int i = 0; i < cnt; i++) {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case ENABLED: {
                        String value = rawAttributeText(reader, ArchiveValidationParameters.ARCHIVE_VALIDATION_ENABLED.getAttribute().getXmlName());
                        ArchiveValidationParameters.ARCHIVE_VALIDATION_ENABLED.getAttribute().parseAndSetParameter(value, archiveValidationOperation, reader);
                        break;
                    }
                    case FAIL_ON_ERROR: {
                        String value = rawAttributeText(reader, ArchiveValidationParameters.ARCHIVE_VALIDATION_FAIL_ON_ERROR.getAttribute().getXmlName());
                        ArchiveValidationParameters.ARCHIVE_VALIDATION_FAIL_ON_ERROR.getAttribute().parseAndSetParameter(value, archiveValidationOperation, reader);
                        break;
                    }
                    case FAIL_ON_WARN: {
                        String value = rawAttributeText(reader, ArchiveValidationParameters.ARCHIVE_VALIDATION_FAIL_ON_WARN.getAttribute().getXmlName());
                        ArchiveValidationParameters.ARCHIVE_VALIDATION_FAIL_ON_WARN.getAttribute().parseAndSetParameter(value, archiveValidationOperation, reader);
                        break;
                    }
                    default: {
                        throw unexpectedAttribute(reader, i);
                    }
                }
            }
            // Handle elements
            requireNoContent(reader);

            return archiveValidationOperation;

        }

        private void parseWorkManager(final XMLExtendedStreamReader reader, final ModelNode parentAddress,
                                      final List<ModelNode> list, final ModelNode node, boolean defaultWm) throws XMLStreamException {

            final ModelNode workManagerOperation = new ModelNode();
            workManagerOperation.get(OP).set(ADD);

            final int cnt = reader.getAttributeCount();
            String name = null;
            for (int i = 0; i < cnt; i++) {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case NAME: {
                        name = rawAttributeText(reader, JcaWorkManagerDefinition.WmParameters.NAME.getAttribute().getXmlName());
                        JcaWorkManagerDefinition.WmParameters.NAME.getAttribute().parseAndSetParameter(name, workManagerOperation, reader);
                        break;
                    }
                    default: {
                        throw unexpectedAttribute(reader, i);
                    }
                }
            }

            if (name == null) {
                if (defaultWm) {
                    name = DEFAULT_NAME;
                    workManagerOperation.get(NAME).set(name);
                } else {
                    throw new XMLStreamException("name attribute is mandatory for workmanager element");
                }
            }

            final ModelNode workManagerAddress = parentAddress.clone();
            workManagerAddress.add(WORKMANAGER, name);
            workManagerAddress.protect();

            workManagerOperation.get(OP_ADDR).set(workManagerAddress);
            list.add(workManagerOperation);

            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {

                final Element element = Element.forName(reader.getLocalName());
                Namespace readerNS = Namespace.forUri(reader.getNamespaceURI());
                switch (element) {
                    case LONG_RUNNING_THREADS: {
                        switch (readerNS) {
                            case JCA_1_0: {
                                org.jboss.as.threads.Namespace ns = org.jboss.as.threads.Namespace.THREADS_1_0;
                                ThreadsParser.getInstance().parseBlockingBoundedQueueThreadPool(reader, readerNS.getUriString(),
                                        ns, workManagerAddress, list, WORKMANAGER_LONG_RUNNING, name);
                                break;
                            }
                            default: {
                                org.jboss.as.threads.Namespace ns = org.jboss.as.threads.Namespace.THREADS_1_1;
                                ThreadsParser.getInstance().parseBlockingBoundedQueueThreadPool(reader, readerNS.getUriString(),
                                        ns, workManagerAddress, list, WORKMANAGER_LONG_RUNNING, name);
                            }
                        }
                        break;
                    }
                    case SHORT_RUNNING_THREADS: {
                        switch (readerNS) {
                            case JCA_1_0: {
                                org.jboss.as.threads.Namespace ns = org.jboss.as.threads.Namespace.THREADS_1_0;
                                ThreadsParser.getInstance().parseBlockingBoundedQueueThreadPool(reader, readerNS.getUriString(),
                                        ns, workManagerAddress, list, WORKMANAGER_SHORT_RUNNING, name);
                                break;
                            }
                            default: {
                                org.jboss.as.threads.Namespace ns = org.jboss.as.threads.Namespace.THREADS_1_1;
                                ThreadsParser.getInstance().parseBlockingBoundedQueueThreadPool(reader, readerNS.getUriString(),
                                        ns, workManagerAddress, list, WORKMANAGER_SHORT_RUNNING, name);
                                break;
                            }
                        }
                        break;
                    }
                    default:
                        throw unexpectedElement(reader);
                }


            }

        }


        private ModelNode parseBeanValidation(final XMLExtendedStreamReader reader, final ModelNode parentOperation) throws XMLStreamException {
            final ModelNode beanValidationOperation = new ModelNode();
            beanValidationOperation.get(OP).set(ADD);

            final ModelNode beanValidationAddress = parentOperation.clone();
            beanValidationAddress.add(BEAN_VALIDATION, BEAN_VALIDATION);
            beanValidationAddress.protect();

            beanValidationOperation.get(OP_ADDR).set(beanValidationAddress);


            final int cnt = reader.getAttributeCount();
            for (int i = 0; i < cnt; i++) {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case ENABLED: {
                        String value = rawAttributeText(reader, JcaBeanValidationDefinition.BeanValidationParameters.BEAN_VALIDATION_ENABLED.getAttribute().getXmlName());
                        JcaBeanValidationDefinition.BeanValidationParameters.BEAN_VALIDATION_ENABLED.getAttribute().parseAndSetParameter(value, beanValidationOperation, reader);
                        break;
                    }
                    default: {
                        throw unexpectedAttribute(reader, i);
                    }
                }
            }
            // Handle elements
            requireNoContent(reader);

            return beanValidationOperation;

        }

        private ModelNode parseCcm(final XMLExtendedStreamReader reader, final ModelNode parentOperation) throws XMLStreamException {
            final ModelNode ccmOperation = new ModelNode();
            ccmOperation.get(OP).set(ADD);

            final ModelNode ccmAddress = parentOperation.clone();
            ccmAddress.add(CACHED_CONNECTION_MANAGER, CACHED_CONNECTION_MANAGER);
            ccmAddress.protect();

            ccmOperation.get(OP_ADDR).set(ccmAddress);


            final int cnt = reader.getAttributeCount();
            for (int i = 0; i < cnt; i++) {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case DEBUG: {
                        String value = rawAttributeText(reader, JcaCachedConnectionManagerDefinition.CcmParameters.DEBUG.getAttribute().getXmlName());
                        JcaCachedConnectionManagerDefinition.CcmParameters.DEBUG.getAttribute().parseAndSetParameter(value, ccmOperation, reader);
                        break;
                    }
                    case ERROR: {
                        String value = rawAttributeText(reader, JcaCachedConnectionManagerDefinition.CcmParameters.ERROR.getAttribute().getXmlName());
                        JcaCachedConnectionManagerDefinition.CcmParameters.ERROR.getAttribute().parseAndSetParameter(value, ccmOperation, reader);
                        break;
                    }
                    default: {
                        throw unexpectedAttribute(reader, i);
                    }
                }
            }
            ccmOperation.get(JcaCachedConnectionManagerDefinition.CcmParameters.INSTALL.getAttribute().getName()).set(true);
            // Handle elements
            requireNoContent(reader);

            return ccmOperation;

        }

        private void parseBootstrapContexts(final XMLExtendedStreamReader reader, final ModelNode parentAddress, final List<ModelNode> list) throws XMLStreamException {
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {

                final Element element = Element.forName(reader.getLocalName());

                switch (element) {
                    case BOOTSTRAP_CONTEXT: {
                        ModelNode bootstrapContextOperation = new ModelNode();
                        bootstrapContextOperation.get(OP).set(ADD);

                        final int cnt = reader.getAttributeCount();
                        String name = null;
                        String wmName = null;
                        for (int i = 0; i < cnt; i++) {
                            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                            switch (attribute) {
                                case NAME: {
                                    name = rawAttributeText(reader, JcaBootstrapContextDefinition.BootstrapCtxParameters.NAME.getAttribute().getXmlName());
                                    JcaBootstrapContextDefinition.BootstrapCtxParameters.NAME.getAttribute().parseAndSetParameter(name, bootstrapContextOperation, reader);
                                    break;
                                }
                                case WORKMANAGER: {
                                    wmName = rawAttributeText(reader, JcaBootstrapContextDefinition.BootstrapCtxParameters.WORKMANAGER.getAttribute().getXmlName());
                                    JcaBootstrapContextDefinition.BootstrapCtxParameters.WORKMANAGER.getAttribute().parseAndSetParameter(wmName, bootstrapContextOperation, reader);
                                    break;
                                }
                                default: {
                                    throw unexpectedAttribute(reader, i);
                                }
                            }
                        }

                        if (name == null) {
                            if (DEFAULT_NAME.equals(wmName)) {
                                name = DEFAULT_NAME;
                            } else {
                                throw new XMLStreamException("name attribute is mandatory for workmanager element");
                            }
                        }

                        final ModelNode bootstrapContextAddress = parentAddress.clone();
                        bootstrapContextAddress.add(BOOTSTRAP_CONTEXT, name);
                        bootstrapContextAddress.protect();

                        bootstrapContextOperation.get(OP_ADDR).set(bootstrapContextAddress);

                        // Handle elements
                        requireNoContent(reader);

                        list.add(bootstrapContextOperation);

                        break;
                    }
                    default: {
                        throw unexpectedElement(reader);

                    }
                }
            }
        }

        public String rawAttributeText(XMLStreamReader reader, String attributeName) {
            String attributeString = reader.getAttributeValue("", attributeName) == null ? null : reader.getAttributeValue(
                    "", attributeName)
                    .trim();
            return attributeString;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3420.java