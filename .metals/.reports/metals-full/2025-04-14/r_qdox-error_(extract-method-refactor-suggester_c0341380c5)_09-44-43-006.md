error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4204.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4204.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4204.java
text:
```scala
r@@eturn node.has(name) && node.get(name).isDefined();

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

package org.jboss.as.txn;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.txn.CommonAttributes.BINDING;
import static org.jboss.as.txn.CommonAttributes.COORDINATOR_ENVIRONMENT;
import static org.jboss.as.txn.CommonAttributes.CORE_ENVIRONMENT;
import static org.jboss.as.txn.CommonAttributes.DEFAULT_TIMEOUT;
import static org.jboss.as.txn.CommonAttributes.ENABLE_STATISTICS;
import static org.jboss.as.txn.CommonAttributes.NODE_IDENTIFIER;
import static org.jboss.as.txn.CommonAttributes.OBJECT_STORE;
import static org.jboss.as.txn.CommonAttributes.PATH;
import static org.jboss.as.txn.CommonAttributes.RECOVERY_ENVIRONMENT;
import static org.jboss.as.txn.CommonAttributes.RELATIVE_TO;
import static org.jboss.as.txn.CommonAttributes.SOCKET_PROCESS_ID_MAX_PORTS;
import static org.jboss.as.txn.CommonAttributes.STATUS_BINDING;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.NewExtension;
import org.jboss.as.controller.NewExtensionContext;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.controller.registry.ModelNodeRegistration;
import org.jboss.as.model.ParseUtils;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * @author Emanuel Muckenhuber
 */
public class NewTransactionExtension implements NewExtension {

    public static final String SUBSYSTEM_NAME = "transactions";
    private static final TransactionSubsystemParser parser = new TransactionSubsystemParser();

    /** {@inheritDoc} */
    @Override
    public void initialize(NewExtensionContext context) {
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME);
        final ModelNodeRegistration registration = subsystem.registerSubsystemModel(TransactionSubsystemProviders.SUBSYSTEM);
        registration.registerOperationHandler(ADD, NewTransactionSubsystemAdd.INSTANCE, TransactionSubsystemProviders.SUBSYSTEM_ADD, false);
        subsystem.registerXMLElementWriter(parser);
    }

    /** {@inheritDoc} */
    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(Namespace.CURRENT.getUriString(), parser);
    }

    static class TransactionSubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

        /** {@inheritDoc} */
        @Override
        public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            // no attributes
            if (reader.getAttributeCount() > 0) {
                throw ParseUtils.unexpectedAttribute(reader, 0);
            }

            final ModelNode subsystem = new ModelNode();
            subsystem.get(OP).set(ADD);
            subsystem.get(OP_ADDR).add(SUBSYSTEM, SUBSYSTEM_NAME);

            // elements
            final EnumSet<Element> required = EnumSet.of(Element.RECOVERY_ENVIRONMENT, Element.CORE_ENVIRONMENT);
            final EnumSet<Element> encountered = EnumSet.noneOf(Element.class);
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                switch (Namespace.forUri(reader.getNamespaceURI())) {
                    case TRANSACTIONS_1_0: {
                        final Element element = Element.forName(reader.getLocalName());
                        required.remove(element);
                        if (! encountered.add(element)) {
                            throw ParseUtils.unexpectedElement(reader);
                        }
                        switch (element) {
                            case RECOVERY_ENVIRONMENT: {
                                final ModelNode model = parseRecoveryEnvironmentElement(reader);
                                subsystem.get(CommonAttributes.RECOVERY_ENVIRONMENT).set(model) ;
                                break;
                            }
                            case CORE_ENVIRONMENT: {
                                final ModelNode model = parseCoreEnvironmentElement(reader);
                                subsystem.get(CommonAttributes.CORE_ENVIRONMENT).set(model) ;
                                break;
                            }
                            case COORDINATOR_ENVIRONMENT: {
                                final ModelNode model = parseCoordinatorEnvironmentElement(reader);
                                subsystem.get(CommonAttributes.COORDINATOR_ENVIRONMENT).set(model) ;
                                break;
                            }
                            case OBJECT_STORE: {
                                final ModelNode model = parseObjectStoreEnvironmentElement(reader);
                                subsystem.get(CommonAttributes.OBJECT_STORE).set(model) ;
                                break;
                            }
                            default: {
                                throw ParseUtils.unexpectedElement(reader);
                            }
                        }
                        break;
                    }
                    default: {
                        throw ParseUtils.unexpectedElement(reader);
                    }
                }
            }
            if (! required.isEmpty()) {
                throw ParseUtils.missingRequiredElement(reader, required);
            }
        }

        static ModelNode parseObjectStoreEnvironmentElement(XMLExtendedStreamReader reader) throws XMLStreamException {
            final ModelNode store = new ModelNode();
            final int count = reader.getAttributeCount();
            for (int i = 0; i < count; i ++) {
                final String value = reader.getAttributeValue(i);
                if (reader.getAttributeNamespace(i) != null) {
                    throw ParseUtils.unexpectedAttribute(reader, i);
                } else {
                    final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                    switch (attribute) {
                        case RELATIVE_TO:
                            store.get(RELATIVE_TO).set(value);
                            break;
                        case PATH:
                            store.get(PATH).set(value);
                            break;
                        default:
                            ParseUtils.unexpectedAttribute(reader, i);
                    }
                }
            }
            // Handle elements
            ParseUtils.requireNoContent(reader);
            return store;
        }

        static ModelNode parseCoordinatorEnvironmentElement(XMLExtendedStreamReader reader) throws XMLStreamException {
            final ModelNode coordinator = new ModelNode();
            final int count = reader.getAttributeCount();
            for (int i = 0; i < count; i ++) {
                final String value = reader.getAttributeValue(i);
                if (reader.getAttributeNamespace(i) != null) {
                    throw ParseUtils.unexpectedAttribute(reader, i);
                } else {
                    final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                    switch (attribute) {
                        case ENABLE_STATISTICS:
                            coordinator.get(ENABLE_STATISTICS).set(value);
                            break;
                        case DEFAULT_TIMEOUT:
                            coordinator.get(DEFAULT_TIMEOUT).set(value);
                            break;
                        default:
                            ParseUtils.unexpectedAttribute(reader, i);
                    }
                }
            }
            // Handle elements
            ParseUtils.requireNoContent(reader);
            return coordinator;
        }

        static ModelNode parseCoreEnvironmentElement(XMLExtendedStreamReader reader) throws XMLStreamException {
            final ModelNode env = new ModelNode();
            final int count = reader.getAttributeCount();
            final EnumSet<Attribute> required = EnumSet.of(Attribute.BINDING);
            for (int i = 0; i < count; i ++) {
                final String value = reader.getAttributeValue(i);
                if (reader.getAttributeNamespace(i) != null) {
                    throw ParseUtils.unexpectedAttribute(reader, i);
                } else {
                    final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                    required.remove(attribute);
                    switch (attribute) {
                        case BINDING:
                            env.get(BINDING).set(value);
                            break;
                        case NODE_IDENTIFIER:
                            env.get(NODE_IDENTIFIER).set(value);
                            break;
                        case SOCKET_PROCESS_ID_MAX_PORTS:
                            env.get(SOCKET_PROCESS_ID_MAX_PORTS).set(value);
                            break;
                        default:
                            ParseUtils.unexpectedAttribute(reader, i);
                    }
                }
            }
            if (! required.isEmpty()) {
                ParseUtils.missingRequired(reader, required);
            }
            // Handle elements
            ParseUtils.requireNoContent(reader);
            return env;
        }

        static ModelNode parseRecoveryEnvironmentElement(XMLExtendedStreamReader reader) throws XMLStreamException {
            final ModelNode env = new ModelNode();
            final int count = reader.getAttributeCount();
            for (int i = 0; i < count; i ++) {
                final String value = reader.getAttributeValue(i);
                if (reader.getAttributeNamespace(i) != null) {
                    throw ParseUtils.unexpectedAttribute(reader, i);
                } else {
                    final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                    switch (attribute) {
                        case BINDING:
                            env.get(BINDING).set(value);
                            break;
                        case STATUS_BINDING:
                            env.get(STATUS_BINDING).set(value);
                            break;
                        default:
                            ParseUtils.unexpectedAttribute(reader, i);
                    }
                }
            }
            if(! env.has(BINDING)) {
                ParseUtils.missingRequired(reader, Collections.singleton(Attribute.BINDING));
            }
            // Handle elements
            ParseUtils.requireNoContent(reader);
            return env;
        }

        /** {@inheritDoc} */
        @Override
        public void writeContent(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {

            context.startSubsystemElement(Namespace.CURRENT.getUriString(), false);

            ModelNode node = context.getModelNode();

            if (has(node, CORE_ENVIRONMENT)) {
                writer.writeStartElement(Element.CORE_ENVIRONMENT.getLocalName());
                final ModelNode core = node.get(CORE_ENVIRONMENT);
                if (has(core, BINDING)) {
                    writeAttribute(writer, Attribute.BINDING, core.get(BINDING));
                }
                if (has(core, NODE_IDENTIFIER)) {
                    writeAttribute(writer, Attribute.NODE_IDENTIFIER, core.get(NODE_IDENTIFIER));
                }
                if (has(core, SOCKET_PROCESS_ID_MAX_PORTS)) {
                    writeAttribute(writer, Attribute.SOCKET_PROCESS_ID_MAX_PORTS, core.get(SOCKET_PROCESS_ID_MAX_PORTS));
                }
                writer.writeEndElement();
            }
            if (has(node, RECOVERY_ENVIRONMENT)) {
                writer.writeStartElement(Element.RECOVERY_ENVIRONMENT.getLocalName());
                final ModelNode env = node.get(RECOVERY_ENVIRONMENT);
                if (has(env, BINDING)) {
                    writeAttribute(writer, Attribute.BINDING, env.get(BINDING));
                }
                if (has(env, STATUS_BINDING)) {
                    writeAttribute(writer, Attribute.STATUS_BINDING, env.get(STATUS_BINDING));

                }
                writer.writeEndElement();
            }
            if (has(node, COORDINATOR_ENVIRONMENT)) {
                writer.writeStartElement(Element.COORDINATOR_ENVIRONMENT.getLocalName());
                final ModelNode env = node.get(COORDINATOR_ENVIRONMENT);
                if (has(env, ENABLE_STATISTICS)) {
                    writeAttribute(writer, Attribute.ENABLE_STATISTICS, env.get(ENABLE_STATISTICS));
                }
                if (has(env, DEFAULT_TIMEOUT)) {
                    writeAttribute(writer, Attribute.DEFAULT_TIMEOUT, env.get(DEFAULT_TIMEOUT));
                }
                writer.writeEndElement();
            }
            if (has(node, OBJECT_STORE)) {
                writer.writeStartElement(Element.OBJECT_STORE.getLocalName());
                final ModelNode env = node.get(OBJECT_STORE);
                if (has(env, RELATIVE_TO)) {
                    writeAttribute(writer, Attribute.RELATIVE_TO, env.get(RELATIVE_TO));
                }
                if (has(env, PATH)) {
                    writeAttribute(writer, Attribute.PATH, env.get(PATH));
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }

        private boolean has(ModelNode node, String name) {
            return node.has(name) && node.get(name) != null;
        }

        private void writeAttribute(final XMLExtendedStreamWriter writer, final Attribute attr, final ModelNode value) throws XMLStreamException {
            writer.writeAttribute(attr.getLocalName(), value.asString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4204.java