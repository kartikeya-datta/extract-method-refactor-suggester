error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15028.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15028.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15028.java
text:
```scala
c@@ase TRANSACTIONS_1_2: {

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
package org.jboss.as.txn.subsystem;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.parsing.ParseUtils.duplicateNamedElement;
import static org.jboss.as.controller.parsing.ParseUtils.missingOneOf;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequired;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequiredElement;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoContent;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoNamespaceAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;

/**
 */
class TransactionSubsystem12Parser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

    public static final TransactionSubsystem12Parser INSTANCE = new TransactionSubsystem12Parser();

    private TransactionSubsystem12Parser() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
        // no attributes
        if (reader.getAttributeCount() > 0) {
            throw unexpectedAttribute(reader, 0);
        }

        final ModelNode address = new ModelNode();
        address.add(ModelDescriptionConstants.SUBSYSTEM, TransactionExtension.SUBSYSTEM_NAME);
        address.protect();

        final ModelNode subsystem = new ModelNode();
        subsystem.get(OP).set(ADD);
        subsystem.get(OP_ADDR).set(address);

        list.add(subsystem);
        final ModelNode logStoreAddress = address.clone();
        final ModelNode logStoreOperation = new ModelNode();
        logStoreOperation.get(OP).set(ADD);
        logStoreAddress.add(LogStoreConstants.LOG_STORE, LogStoreConstants.LOG_STORE);

        logStoreAddress.protect();

        logStoreOperation.get(OP_ADDR).set(logStoreAddress);
        list.add(logStoreOperation);

        // elements
        final EnumSet<Element> required = EnumSet.of(Element.RECOVERY_ENVIRONMENT, Element.CORE_ENVIRONMENT);
        final EnumSet<Element> encountered = EnumSet.noneOf(Element.class);
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case TRANSACTIONS_1_1: {
                    final Element element = Element.forName(reader.getLocalName());
                    required.remove(element);
                    if (!encountered.add(element)) {
                        throw unexpectedElement(reader);
                    }
                    switch (element) {
                        case RECOVERY_ENVIRONMENT: {
                            parseRecoveryEnvironmentElement(reader, subsystem);
                            break;
                        }
                        case CORE_ENVIRONMENT: {
                            parseCoreEnvironmentElement(reader, subsystem);
                            break;
                        }
                        case COORDINATOR_ENVIRONMENT: {
                            parseCoordinatorEnvironmentElement(reader, subsystem);
                            break;
                        }
                        case OBJECT_STORE: {
                            parseObjectStoreEnvironmentElementAndEnrichOperation(reader, subsystem);
                            break;
                        }
                        case JTS: {
                            parseJts(reader, subsystem);
                            break;
                        }
                        case USEHORNETQSTORE: {
                            parseUsehornetqstore(reader, logStoreOperation);
                            subsystem.get(CommonAttributes.USEHORNETQSTORE).set(true);
                            break;
                        }
                        default: {
                            throw unexpectedElement(reader);
                        }
                    }
                    break;
                }
                default: {
                    throw unexpectedElement(reader);
                }
            }
        }
        if (!required.isEmpty()) {
            throw missingRequiredElement(reader, required);
        }
    }

    private void parseJts(final XMLExtendedStreamReader reader, final ModelNode operation) throws XMLStreamException {
        operation.get(CommonAttributes.JTS).set(true);
        requireNoContent(reader);
    }

    private void parseUsehornetqstore(final XMLExtendedStreamReader reader, final ModelNode operation) throws XMLStreamException {
        operation.get(LogStoreConstants.LOG_STORE_TYPE.getName()).set("hornetq");
        requireNoContent(reader);
    }

    static void parseObjectStoreEnvironmentElementAndEnrichOperation(final XMLExtendedStreamReader reader, ModelNode operation) throws XMLStreamException {

        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case RELATIVE_TO:
                    TransactionSubsystemRootResourceDefinition.OBJECT_STORE_RELATIVE_TO.parseAndSetParameter(value, operation, reader);
                    break;
                case PATH:
                    TransactionSubsystemRootResourceDefinition.OBJECT_STORE_PATH.parseAndSetParameter(value, operation, reader);
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        // Handle elements
        requireNoContent(reader);

    }

    static void parseCoordinatorEnvironmentElement(final XMLExtendedStreamReader reader, final ModelNode operation) throws XMLStreamException {

        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case ENABLE_STATISTICS:
                    TransactionSubsystemRootResourceDefinition.ENABLE_STATISTICS.parseAndSetParameter(value, operation, reader);
                    break;
                case ENABLE_TSM_STATUS:
                    TransactionSubsystemRootResourceDefinition.ENABLE_TSM_STATUS.parseAndSetParameter(value, operation, reader);
                    break;
                case DEFAULT_TIMEOUT:
                    TransactionSubsystemRootResourceDefinition.DEFAULT_TIMEOUT.parseAndSetParameter(value, operation, reader);
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        // Handle elements
        requireNoContent(reader);

    }

    /**
     * Handle the core-environment element and children
     *
     * @param reader
     * @return ModelNode for the core-environment
     * @throws javax.xml.stream.XMLStreamException
     *
     */
    static void parseCoreEnvironmentElement(final XMLExtendedStreamReader reader, final ModelNode operation) throws XMLStreamException {

        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case NODE_IDENTIFIER:
                    TransactionSubsystemRootResourceDefinition.NODE_IDENTIFIER.parseAndSetParameter(value, operation, reader);
                    break;
                case PATH:
                    TransactionSubsystemRootResourceDefinition.PATH.parseAndSetParameter(value, operation, reader);
                    break;
                case RELATIVE_TO:
                    TransactionSubsystemRootResourceDefinition.RELATIVE_TO.parseAndSetParameter(value, operation, reader);
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        // elements
        final EnumSet<Element> required = EnumSet.of(Element.PROCESS_ID);
        final EnumSet<Element> encountered = EnumSet.noneOf(Element.class);
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            required.remove(element);
            switch (element) {
                case PROCESS_ID: {
                    if (!encountered.add(element)) {
                        throw duplicateNamedElement(reader, reader.getLocalName());
                    }
                    parseProcessIdEnvironmentElement(reader, operation);
                    break;
                }
                default:
                    throw unexpectedElement(reader);
            }
        }
        if (!required.isEmpty()) {
            throw missingRequiredElement(reader, required);
        }
    }

    /**
     * Handle the process-id child elements
     *
     * @param reader
     * @param coreEnvironmentAdd
     * @return
     * @throws javax.xml.stream.XMLStreamException
     *
     */
    static void parseProcessIdEnvironmentElement(XMLExtendedStreamReader reader, ModelNode coreEnvironmentAdd) throws XMLStreamException {

        // elements
        boolean encountered = false;
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case UUID:
                    if (encountered) {
                        throw unexpectedElement(reader);
                    }
                    encountered = true;
                    coreEnvironmentAdd.get(TransactionSubsystemRootResourceDefinition.PROCESS_ID_UUID.getName()).set(true);
                    requireNoContent(reader);
                    break;
                case SOCKET: {
                    if (encountered) {
                        throw unexpectedElement(reader);
                    }
                    encountered = true;
                    parseSocketProcessIdElement(reader, coreEnvironmentAdd);
                    break;
                }
                default:
                    throw unexpectedElement(reader);
            }
        }

        if (!encountered) {
            throw missingOneOf(reader, EnumSet.of(Element.UUID, Element.SOCKET));
        }
    }

    static void parseSocketProcessIdElement(XMLExtendedStreamReader reader, ModelNode coreEnvironmentAdd) throws XMLStreamException {

        final int count = reader.getAttributeCount();
        final EnumSet<Attribute> required = EnumSet.of(Attribute.BINDING);
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            required.remove(attribute);
            switch (attribute) {
                case BINDING:
                    TransactionSubsystemRootResourceDefinition.PROCESS_ID_SOCKET_BINDING.parseAndSetParameter(value, coreEnvironmentAdd, reader);
                    break;
                case SOCKET_PROCESS_ID_MAX_PORTS:
                    TransactionSubsystemRootResourceDefinition.PROCESS_ID_SOCKET_MAX_PORTS.parseAndSetParameter(value, coreEnvironmentAdd, reader);
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        if (!required.isEmpty()) {
            throw missingRequired(reader, required);
        }
        // Handle elements
        requireNoContent(reader);
    }

    static void parseRecoveryEnvironmentElement(final XMLExtendedStreamReader reader, final ModelNode operation) throws XMLStreamException {

        Set<Attribute> required = EnumSet.of(Attribute.BINDING, Attribute.STATUS_BINDING);
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            required.remove(attribute);
            switch (attribute) {
                case BINDING:
                    TransactionSubsystemRootResourceDefinition.BINDING.parseAndSetParameter(value, operation, reader);
                    break;
                case STATUS_BINDING:
                    TransactionSubsystemRootResourceDefinition.STATUS_BINDING.parseAndSetParameter(value, operation, reader);
                    break;
                case RECOVERY_LISTENER:
                    TransactionSubsystemRootResourceDefinition.RECOVERY_LISTENER.parseAndSetParameter(value, operation, reader);
                    break;
                default:
                    unexpectedAttribute(reader, i);
            }
        }

        if (!required.isEmpty()) {
            throw missingRequired(reader, required);
        }
        // Handle elements
        requireNoContent(reader);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeContent(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {

        context.startSubsystemElement(Namespace.CURRENT.getUriString(), false);

        ModelNode node = context.getModelNode();


        writer.writeStartElement(Element.CORE_ENVIRONMENT.getLocalName());

        TransactionSubsystemRootResourceDefinition.NODE_IDENTIFIER.marshallAsAttribute(node, writer);
        TransactionSubsystemRootResourceDefinition.PATH.marshallAsAttribute(node, writer);
        TransactionSubsystemRootResourceDefinition.RELATIVE_TO.marshallAsAttribute(node, writer);

        writeProcessId(writer, node);

        writer.writeEndElement();

        if (TransactionSubsystemRootResourceDefinition.BINDING.isMarshallable(node) ||
                TransactionSubsystemRootResourceDefinition.STATUS_BINDING.isMarshallable(node) ||
                TransactionSubsystemRootResourceDefinition.RECOVERY_LISTENER.isMarshallable(node)) {
            writer.writeStartElement(Element.RECOVERY_ENVIRONMENT.getLocalName());
            TransactionSubsystemRootResourceDefinition.BINDING.marshallAsAttribute(node, writer);

            TransactionSubsystemRootResourceDefinition.STATUS_BINDING.marshallAsAttribute(node, writer);

            TransactionSubsystemRootResourceDefinition.RECOVERY_LISTENER.marshallAsAttribute(node, writer);

            writer.writeEndElement();
        }
        if (TransactionSubsystemRootResourceDefinition.ENABLE_STATISTICS.isMarshallable(node)
 TransactionSubsystemRootResourceDefinition.ENABLE_TSM_STATUS.isMarshallable(node)
 TransactionSubsystemRootResourceDefinition.DEFAULT_TIMEOUT.isMarshallable(node)) {

            writer.writeStartElement(Element.COORDINATOR_ENVIRONMENT.getLocalName());

            TransactionSubsystemRootResourceDefinition.ENABLE_STATISTICS.marshallAsAttribute(node, writer);
            TransactionSubsystemRootResourceDefinition.ENABLE_TSM_STATUS.marshallAsAttribute(node, writer);
            TransactionSubsystemRootResourceDefinition.DEFAULT_TIMEOUT.marshallAsAttribute(node, writer);

            writer.writeEndElement();
        }

        if (TransactionSubsystemRootResourceDefinition.OBJECT_STORE_RELATIVE_TO.isMarshallable(node)
 TransactionSubsystemRootResourceDefinition.OBJECT_STORE_PATH.isMarshallable(node)) {
            writer.writeStartElement(Element.OBJECT_STORE.getLocalName());
            TransactionSubsystemRootResourceDefinition.OBJECT_STORE_PATH.marshallAsAttribute(node, writer);
            TransactionSubsystemRootResourceDefinition.OBJECT_STORE_RELATIVE_TO.marshallAsAttribute(node, writer);
            writer.writeEndElement();
        }

        if(node.hasDefined(CommonAttributes.JTS) && node.get(CommonAttributes.JTS).asBoolean()) {
            writer.writeStartElement(Element.JTS.getLocalName());
            writer.writeEndElement();
        }

        if(node.hasDefined(CommonAttributes.USEHORNETQSTORE) && node.get(CommonAttributes.USEHORNETQSTORE).asBoolean()) {
            writer.writeStartElement(Element.USEHORNETQSTORE.getLocalName());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    private void writeProcessId(final XMLExtendedStreamWriter writer, final ModelNode value) throws XMLStreamException {
        writer.writeStartElement(Element.PROCESS_ID.getLocalName());
        if (value.get(TransactionSubsystemRootResourceDefinition.PROCESS_ID_UUID.getName()).asBoolean()) {
            writer.writeEmptyElement(Element.UUID.getLocalName());
        } else {
            writer.writeStartElement(Element.SOCKET.getLocalName());
            TransactionSubsystemRootResourceDefinition.PROCESS_ID_SOCKET_BINDING.marshallAsAttribute(value, writer);
            TransactionSubsystemRootResourceDefinition.PROCESS_ID_SOCKET_MAX_PORTS.marshallAsAttribute(value, writer);
            writer.writeEndElement();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15028.java