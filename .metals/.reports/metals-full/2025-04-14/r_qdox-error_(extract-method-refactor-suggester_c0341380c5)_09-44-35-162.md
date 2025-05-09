error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1242.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1242.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1242.java
text:
```scala
n@@ame = AttributedTypeEnum.forType(name);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.wildfly.extension.picketlink.idm.model.parser;

import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.wildfly.extension.picketlink.common.model.ModelElement;
import org.wildfly.extension.picketlink.common.model.XMLElement;
import org.wildfly.extension.picketlink.idm.IDMExtension;
import org.wildfly.extension.picketlink.idm.Namespace;
import org.wildfly.extension.picketlink.idm.model.AttributedTypeEnum;
import org.wildfly.extension.picketlink.idm.model.CredentialHandlerResourceDefinition;
import org.wildfly.extension.picketlink.idm.model.CredentialTypeEnum;
import org.wildfly.extension.picketlink.idm.model.FileStoreResourceDefinition;
import org.wildfly.extension.picketlink.idm.model.IdentityConfigurationResourceDefinition;
import org.wildfly.extension.picketlink.idm.model.JPAStoreResourceDefinition;
import org.wildfly.extension.picketlink.idm.model.LDAPStoreAttributeResourceDefinition;
import org.wildfly.extension.picketlink.idm.model.LDAPStoreMappingResourceDefinition;
import org.wildfly.extension.picketlink.idm.model.LDAPStoreResourceDefinition;
import org.wildfly.extension.picketlink.idm.model.PartitionManagerResourceDefinition;
import org.wildfly.extension.picketlink.idm.model.SupportedTypeResourceDefinition;
import org.wildfly.extension.picketlink.idm.model.SupportedTypesResourceDefinition;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.List;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoAttributes;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;
import static org.wildfly.extension.picketlink.common.model.ModelElement.COMMON_CLASS_NAME;
import static org.wildfly.extension.picketlink.common.model.ModelElement.COMMON_CODE;
import static org.wildfly.extension.picketlink.common.model.ModelElement.COMMON_NAME;
import static org.wildfly.extension.picketlink.common.model.ModelElement.FILE_STORE;
import static org.wildfly.extension.picketlink.common.model.ModelElement.IDENTITY_CONFIGURATION;
import static org.wildfly.extension.picketlink.common.model.ModelElement.IDENTITY_STORE_CREDENTIAL_HANDLER;
import static org.wildfly.extension.picketlink.common.model.ModelElement.JPA_STORE;
import static org.wildfly.extension.picketlink.common.model.ModelElement.LDAP_STORE;
import static org.wildfly.extension.picketlink.common.model.ModelElement.LDAP_STORE_ATTRIBUTE;
import static org.wildfly.extension.picketlink.common.model.ModelElement.LDAP_STORE_MAPPING;
import static org.wildfly.extension.picketlink.common.model.ModelElement.PARTITION_MANAGER;
import static org.wildfly.extension.picketlink.common.model.ModelElement.SUPPORTED_TYPE;
import static org.wildfly.extension.picketlink.common.model.ModelElement.SUPPORTED_TYPES;

/**
 * <p> XML Reader for the subsystem schema, version 1.0. </p>
 *
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 */
public class IDMSubsystemReader_1_0 implements XMLStreamConstants, XMLElementReader<List<ModelNode>> {

    @Override
    public void readElement(XMLExtendedStreamReader reader, List<ModelNode> addOperations) throws XMLStreamException {
        requireNoAttributes(reader);

        Namespace nameSpace = Namespace.forUri(reader.getNamespaceURI());

        ModelNode subsystemNode = createSubsystemRoot();

        addOperations.add(subsystemNode);

        switch (nameSpace) {
            case PICKETLINK_IDENTITY_MANAGEMENT_1_0:
                this.readElement_1_0(reader, subsystemNode, addOperations);
                break;
            default:
                throw unexpectedElement(reader);
        }
    }

    private void readElement_1_0(XMLExtendedStreamReader reader, ModelNode subsystemNode, List<ModelNode> addOperations)
            throws XMLStreamException {
        if (Namespace.PICKETLINK_IDENTITY_MANAGEMENT_1_0 != Namespace.forUri(reader.getNamespaceURI())) {
            throw unexpectedElement(reader);
        }

        while (reader.hasNext() && reader.nextTag() != END_DOCUMENT) {
            if (!reader.isStartElement()) {
                continue;
            }

            // if the current element is supported but is not a model element
            if (XMLElement.forName(reader.getLocalName()) != null) {
                continue;
            }

            ModelElement modelKey = ModelElement.forName(reader.getLocalName());

            if (modelKey == null) {
                throw unexpectedElement(reader);
            }

            switch (modelKey) {
                case PARTITION_MANAGER:
                    parseIdentityManagementConfig(reader, subsystemNode, addOperations);
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }
    }

    private void parseIdentityManagementConfig(final XMLExtendedStreamReader reader, final ModelNode parentNode,
                                                      final List<ModelNode> addOperations) throws XMLStreamException {
        ModelNode identityManagementNode = parseConfig(reader, PARTITION_MANAGER, COMMON_NAME.getName(), parentNode,
                                                              PartitionManagerResourceDefinition.INSTANCE.getAttributes(), addOperations);

        parseElement(new ElementParser() {
            @Override
            public void parse(final XMLExtendedStreamReader reader, final ModelElement element, final ModelNode parentNode,
                                     List<ModelNode> addOperations) throws XMLStreamException {
                switch (element) {
                    case IDENTITY_CONFIGURATION:
                        parseIdentityConfigurationConfig(reader, parentNode, addOperations);
                        break;
                }
            }
        }, PARTITION_MANAGER, identityManagementNode, reader, addOperations);
    }

    private void parseIdentityConfigurationConfig(final XMLExtendedStreamReader reader, final ModelNode parentNode,
                                                         final List<ModelNode> addOperations) throws XMLStreamException {
        ModelNode identityConfigurationNode = parseConfig(reader, IDENTITY_CONFIGURATION, COMMON_NAME.getName(), parentNode,
                                                                 IdentityConfigurationResourceDefinition.INSTANCE.getAttributes(), addOperations);

        parseElement(new ElementParser() {
            @Override
            public void parse(final XMLExtendedStreamReader reader, final ModelElement element, final ModelNode parentNode,
                                     List<ModelNode> addOperations) throws XMLStreamException {
                switch (element) {
                    case JPA_STORE:
                        parseJPAStoreConfig(reader, parentNode, addOperations);
                        break;
                    case FILE_STORE:
                        parseFileStoreConfig(reader, parentNode, addOperations);
                        break;
                    case LDAP_STORE:
                        parseLDAPStoreConfig(reader, addOperations, parentNode);
                        break;
                }
            }
        }, IDENTITY_CONFIGURATION, identityConfigurationNode, reader, addOperations);
    }

    private void parseJPAStoreConfig(final XMLExtendedStreamReader reader, final ModelNode identityConfigurationNode,
                                            final List<ModelNode> addOperations) throws XMLStreamException {
        ModelNode jpaStoreNode = parseConfig(reader, JPA_STORE, null, identityConfigurationNode,
                                                    JPAStoreResourceDefinition.INSTANCE.getAttributes(), addOperations);

        parseElement(new ElementParser() {
            @Override
            public void parse(final XMLExtendedStreamReader reader, final ModelElement element, final ModelNode parentNode,
                                     List<ModelNode> addOperations) throws XMLStreamException {
                switch (element) {
                    case IDENTITY_STORE_CREDENTIAL_HANDLER:
                        parseCredentialHandlerConfig(reader, parentNode, addOperations);
                        break;
                    case SUPPORTED_TYPES:
                        parseSupportedTypesConfig(reader, parentNode, addOperations);
                        break;
                }
            }
        }, JPA_STORE, jpaStoreNode, reader, addOperations);
    }

    private void parseFileStoreConfig(final XMLExtendedStreamReader reader, final ModelNode identityManagementNode,
                                             final List<ModelNode> addOperations) throws XMLStreamException {
        ModelNode fileStoreNode = parseConfig(reader, FILE_STORE, null, identityManagementNode,
                                                     FileStoreResourceDefinition.INSTANCE.getAttributes(), addOperations);

        parseElement(new ElementParser() {
            @Override
            public void parse(final XMLExtendedStreamReader reader, final ModelElement element, final ModelNode parentNode,
                                     List<ModelNode> addOperations) throws XMLStreamException {
                switch (element) {
                    case IDENTITY_STORE_CREDENTIAL_HANDLER:
                        parseCredentialHandlerConfig(reader, parentNode, addOperations);
                        break;
                    case SUPPORTED_TYPES:
                        parseSupportedTypesConfig(reader, parentNode, addOperations);
                        break;
                }
            }
        }, FILE_STORE, fileStoreNode, reader, addOperations);
    }

    private void parseLDAPStoreConfig(final XMLExtendedStreamReader reader, final List<ModelNode> addOperations,
                                             final ModelNode identityManagementNode) throws XMLStreamException {
        ModelNode ldapStoreNode = parseConfig(reader, LDAP_STORE, null, identityManagementNode,
                                                     LDAPStoreResourceDefinition.INSTANCE.getAttributes(), addOperations);

        parseElement(new ElementParser() {
            @Override
            public void parse(final XMLExtendedStreamReader reader, final ModelElement element, final ModelNode parentNode,
                                     List<ModelNode> addOperations) throws XMLStreamException {
                switch (element) {
                    case IDENTITY_STORE_CREDENTIAL_HANDLER:
                        parseCredentialHandlerConfig(reader, parentNode, addOperations);
                        break;
                    case LDAP_STORE_MAPPING:
                        parseLDAPMappingConfig(reader, parentNode, addOperations);
                        break;
                    case SUPPORTED_TYPES:
                        parseSupportedTypesConfig(reader, parentNode, addOperations);
                        break;
                }
            }
        }, LDAP_STORE, ldapStoreNode, reader, addOperations);
    }

    private void parseLDAPMappingConfig(final XMLExtendedStreamReader reader, final ModelNode identityProviderNode,
                                               final List<ModelNode> addOperations) throws XMLStreamException {
        String name = reader.getAttributeValue("", COMMON_CLASS_NAME.getName());

        if (name == null) {
            name = reader.getAttributeValue("", COMMON_CODE.getName());

            if (name != null) {
                name = CredentialTypeEnum.forType(name);
            }
        }

        ModelNode ldapMappingConfig = parseConfig(reader, LDAP_STORE_MAPPING,
                                                         name, identityProviderNode,
                                                         LDAPStoreMappingResourceDefinition.INSTANCE.getAttributes(), addOperations);

        parseElement(new ElementParser() {
            @Override
            public void parse(final XMLExtendedStreamReader reader, final ModelElement element, final ModelNode parentNode,
                                     List<ModelNode> addOperations) throws XMLStreamException {
                switch (element) {
                    case LDAP_STORE_ATTRIBUTE:
                        parseConfig(reader, LDAP_STORE_ATTRIBUTE, LDAPStoreAttributeResourceDefinition.NAME.getName(),
                                       parentNode, LDAPStoreAttributeResourceDefinition.INSTANCE.getAttributes(), addOperations);
                        break;
                }
            }
        }, LDAP_STORE_MAPPING, ldapMappingConfig, reader, addOperations);
    }

    private ModelNode parseCredentialHandlerConfig(XMLExtendedStreamReader reader, ModelNode identityProviderNode,
                                                          List<ModelNode> addOperations) throws XMLStreamException {
        String name = reader.getAttributeValue("", COMMON_CLASS_NAME.getName());

        if (name == null) {
            name = reader.getAttributeValue("", COMMON_CODE.getName());

            if (name != null) {
                name = CredentialTypeEnum.forType(name);
            }
        }

        return parseConfig(reader, IDENTITY_STORE_CREDENTIAL_HANDLER, name,
                                  identityProviderNode, CredentialHandlerResourceDefinition.INSTANCE.getAttributes(), addOperations);
    }

    private String resolveNodeName(XMLExtendedStreamReader reader, SimpleAttributeDefinition primaryAttribute, SimpleAttributeDefinition alternativeAttribute) {
        String name = reader.getAttributeValue("", primaryAttribute.getName());

        if (name == null) {
            name = reader.getAttributeValue("", alternativeAttribute.getName());
        }
        return name;
    }

    private ModelNode parseSupportedTypesConfig(final XMLExtendedStreamReader reader, final ModelNode identityStoreNode,
                                                       final List<ModelNode> addOperations) throws XMLStreamException {
        ModelNode supportedTypesNode = parseConfig(reader, SUPPORTED_TYPES, null, identityStoreNode,
                                                          SupportedTypesResourceDefinition.INSTANCE.getAttributes(), addOperations);

        parseElement(new ElementParser() {
            @Override
            public void parse(final XMLExtendedStreamReader reader, final ModelElement element, final ModelNode parentNode,
                                     List<ModelNode> addOperations) throws XMLStreamException {
                switch (element) {
                    case SUPPORTED_TYPE:
                        String name = reader.getAttributeValue("", COMMON_CLASS_NAME.getName());

                        if (name == null) {
                            name = reader.getAttributeValue("", COMMON_CODE.getName());

                            if (name != null) {
                                name = AttributedTypeEnum.forType(name);
                            }
                        }

                        parseConfig(reader, SUPPORTED_TYPE, name, parentNode,
                                           SupportedTypeResourceDefinition.INSTANCE.getAttributes(), addOperations);
                        break;
                }
            }
        }, SUPPORTED_TYPES, supportedTypesNode, reader, addOperations);

        return supportedTypesNode;
    }

    /**
     * Creates the root subsystem's root address.
     *
     * @return
     */
    private ModelNode createSubsystemRoot() {
        ModelNode subsystemAddress = new ModelNode();

        subsystemAddress.add(ModelDescriptionConstants.SUBSYSTEM, IDMExtension.SUBSYSTEM_NAME);

        subsystemAddress.protect();

        return Util.getEmptyOperation(ADD, subsystemAddress);
    }

    /**
     * Reads a element from the stream considering the parameters.
     *
     * @param reader XMLExtendedStreamReader instance from which the elements are read.
     * @param xmlElement Name of the Model Element to be parsed.
     * @param key Name of the attribute to be used to as the key for the model.
     * @param addOperations List of operations.
     * @param lastNode Parent ModelNode instance.
     * @param attributes AttributeDefinition instances to be used to extract the attributes and populate the resulting model.
     *
     * @return A ModelNode instance populated.
     *
     * @throws javax.xml.stream.XMLStreamException
     */
    private ModelNode parseConfig(XMLExtendedStreamReader reader, ModelElement xmlElement, String key, ModelNode lastNode,
                                         List<SimpleAttributeDefinition> attributes, List<ModelNode> addOperations) throws XMLStreamException {
        if (!reader.getLocalName().equals(xmlElement.getName())) {
            return null;
        }

        ModelNode modelNode = Util.getEmptyOperation(ADD, null);

        for (SimpleAttributeDefinition simpleAttributeDefinition : attributes) {
            simpleAttributeDefinition.parseAndSetParameter(reader.getAttributeValue("", simpleAttributeDefinition.getXmlName()), modelNode, reader);
        }

        String name = xmlElement.getName();

        if (key != null) {
            name = key;

            if (modelNode.hasDefined(key)) {
                name = modelNode.get(key).asString();
            } else {
                String attributeValue = reader.getAttributeValue("", key);

                if (attributeValue != null) {
                    name = attributeValue;
                }
            }
        }

        modelNode.get(ModelDescriptionConstants.OP_ADDR).set(lastNode.clone().get(OP_ADDR).add(xmlElement.getName(), name));

        addOperations.add(modelNode);

        return modelNode;
    }

    private void parseElement(final ElementParser parser, ModelElement parentElement, final ModelNode parentNode,
                                     final XMLExtendedStreamReader reader, final List<ModelNode> addOperations) throws XMLStreamException {
        while (reader.hasNext() && reader.nextTag() != END_DOCUMENT) {
            if (!reader.isStartElement()) {
                if (reader.isEndElement() && reader.getLocalName().equals(parentElement.getName())) {
                    break;
                }
                continue;
            }

            if (reader.getLocalName().equals(parentElement.getName())) {
                continue;
            }

            ModelElement element = ModelElement.forName(reader.getLocalName());

            if (element == null) {
                if (XMLElement.forName(reader.getLocalName()) != null) {
                    continue;
                }

                throw unexpectedElement(reader);
            }

            parser.parse(reader, element, parentNode, addOperations);
        }
    }

    private interface ElementParser {

        void parse(XMLExtendedStreamReader reader, ModelElement element, ModelNode parentNode, List<ModelNode> addOperations)
                throws XMLStreamException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1242.java