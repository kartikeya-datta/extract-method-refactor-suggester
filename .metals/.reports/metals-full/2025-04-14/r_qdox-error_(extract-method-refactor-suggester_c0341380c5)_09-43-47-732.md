error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2632.java
text:
```scala
p@@ropNode.get(OP_ADDR).set(address).add(ModelConstants.PROPERTY, name);

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
package org.jboss.as.osgi.parser;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequired;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoAttributes;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoContent;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoNamespaceAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLExtendedStreamReader;

/**
 * Parse subsystem configuration for namespace {@link Namespace#OSGI_1_1}.
 *
 * @author Thomas.Diesler@jboss.com
 */
class OSGiNamespace11Parser implements Namespace11, XMLStreamConstants, XMLElementReader<List<ModelNode>> {

    static XMLElementReader<List<ModelNode>> INSTANCE = new OSGiNamespace11Parser();

    // hide ctor
    private OSGiNamespace11Parser() {
    }

    @Override
    public void readElement(XMLExtendedStreamReader reader, List<ModelNode> operations) throws XMLStreamException {

        ModelNode address = new ModelNode();
        address.add(SUBSYSTEM, OSGiExtension.SUBSYSTEM_NAME);
        address.protect();

        ModelNode activation = parseActivationAttribute(reader, address);
        operations.add(activation);

        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case OSGI_1_1: {
                    final Element element = Element.forName(reader.getLocalName());
                    switch (element) {
                        case CONFIGURATION: {
                            List<ModelNode> result = parseConfigurations(reader, address);
                            operations.addAll(result);
                            break;
                        }
                        case PROPERTIES: {
                            List<ModelNode> result = parseFrameworkProperties(reader, address, operations);
                            operations.addAll(result);
                            break;
                        }
                        case CAPABILITIES: {
                            List<ModelNode> result = parseCapabilities(reader, address);
                            operations.addAll(result);
                            break;
                        }
                        default:
                            throw unexpectedElement(reader);
                    }
                }
            }
        }
    }

    private ModelNode parseActivationAttribute(XMLExtendedStreamReader reader, ModelNode address) throws XMLStreamException {
        final ModelNode result = new ModelNode();
        result.get(OP).set(ADD);
        result.get(OP_ADDR).set(address);
        switch (Namespace.forUri(reader.getNamespaceURI())) {
            case OSGI_1_1: {
                // Handle attributes
                int count = reader.getAttributeCount();
                for (int i = 0; i < count; i++) {
                    requireNoNamespaceAttribute(reader, i);
                    final String attrValue = reader.getAttributeValue(i);
                    final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                    switch (attribute) {
                        case ACTIVATION: {
                            result.get(ModelConstants.ACTIVATION).set(attrValue);
                            break;
                        }
                        default:
                            throw unexpectedAttribute(reader, i);
                    }
                }
                break;
            }
            default:
                throw unexpectedElement(reader);
        }
        return result;
    }

    private List<ModelNode> parseConfigurations(XMLExtendedStreamReader reader, ModelNode address) throws XMLStreamException {

        // Handle attributes
        String pid = null;
        int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String attrValue = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case PID: {
                    pid = attrValue;
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        if (pid == null)
            throw missingRequired(reader, Collections.singleton(Attribute.PID));

        ModelNode configuration = new ModelNode();
        configuration.get(OP).set(ADD);
        configuration.get(OP_ADDR).set(address).add(ModelConstants.CONFIGURATION, pid);

        List<ModelNode> result = new ArrayList<ModelNode>();
        result.add(configuration);

        ModelNode propNode = configuration.get(ModelConstants.ENTRIES);

        // Handle elements
        while (reader.hasNext() && reader.nextTag() != XMLStreamConstants.END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case OSGI_1_1: {
                    final Element element = Element.forName(reader.getLocalName());
                    if (element == Element.PROPERTY) {
                        // Handle attributes
                        String name = null;
                        String value = null;
                        count = reader.getAttributeCount();
                        for (int i = 0; i < count; i++) {
                            requireNoNamespaceAttribute(reader, i);
                            final String attrValue = reader.getAttributeValue(i);

                            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                            switch (attribute) {
                                case NAME: {
                                    name = attrValue;
                                    // if (configuration.has(name))
                                    // throw new XMLStreamException(MESSAGES.propertyAlreadyExists(name), reader.getLocation());
                                    break;
                                }
                                case VALUE: {
                                    value = attrValue;
                                    break;
                                }
                                default:
                                    throw unexpectedAttribute(reader, i);
                            }
                        }
                        if (name == null)
                            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
                        if (value == null)
                            throw missingRequired(reader, Collections.singleton(Attribute.VALUE));

                        requireNoContent(reader);

                        propNode.get(name).set(value);

                        break;
                    } else {
                        throw unexpectedElement(reader);
                    }
                }
                default:
                    throw unexpectedElement(reader);
            }
        }

        return result;
    }

    private List<ModelNode> parseFrameworkProperties(XMLExtendedStreamReader reader, ModelNode address, List<ModelNode> operations) throws XMLStreamException {

        requireNoAttributes(reader);

        List<ModelNode> result = new ArrayList<ModelNode>();

        // Handle elements
        while (reader.hasNext() && reader.nextTag() != XMLStreamConstants.END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case OSGI_1_1: {
                    final Element element = Element.forName(reader.getLocalName());
                    if (element == Element.PROPERTY) {
                        String name = null;
                        String value = null;
                        int count = reader.getAttributeCount();
                        for (int i = 0; i < count; i++) {
                            requireNoNamespaceAttribute(reader, i);
                            final String attrValue = reader.getAttributeValue(i);
                            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                            switch (attribute) {
                                case NAME: {
                                    name = attrValue;
                                    break;
                                }
                                default:
                                    throw unexpectedAttribute(reader, i);
                            }
                        }
                        if (name == null) {
                            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
                        }
                        value = reader.getElementText().trim();

                        ModelNode propNode = new ModelNode();
                        propNode.get(OP).set(ADD);
                        propNode.get(OP_ADDR).set(address).add(ModelConstants.FRAMEWORK_PROPERTY, name);
                        propNode.get(ModelConstants.VALUE).set(value);

                        result.add(propNode);
                        break;
                    } else {
                        throw unexpectedElement(reader);
                    }
                }
                default:
                    throw unexpectedElement(reader);
            }
        }

        return result;
    }

    private List<ModelNode> parseCapabilities(XMLExtendedStreamReader reader, ModelNode address) throws XMLStreamException {
        List<ModelNode> nodes = new ArrayList<ModelNode>();
        requireNoAttributes(reader);

        // Handle elements
        while (reader.hasNext() && reader.nextTag() != XMLStreamConstants.END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case OSGI_1_1: {
                    final Element element = Element.forName(reader.getLocalName());
                    if (element == Element.CAPABILITY) {
                        String name = null;
                        String start = null;
                        final int count = reader.getAttributeCount();
                        for (int i = 0; i < count; i++) {
                            requireNoNamespaceAttribute(reader, i);
                            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                            switch (attribute) {
                                case NAME: {
                                    name = reader.getAttributeValue(i);
                                    break;
                                }
                                case STARTLEVEL: {
                                    start = reader.getAttributeValue(i);
                                    break;
                                }
                                default:
                                    throw unexpectedAttribute(reader, i);
                            }
                        }
                        if (name == null)
                            throw missingRequired(reader, Collections.singleton(Attribute.NAME));

                        ModelNode moduleNode = new ModelNode();
                        moduleNode.get(OP).set(ADD);
                        moduleNode.get(OP_ADDR).set(address).add(ModelConstants.CAPABILITY, name);
                        if (start != null)
                            moduleNode.get(ModelConstants.STARTLEVEL).set(start);

                        nodes.add(moduleNode);

                        requireNoContent(reader);
                        break;
                    } else {
                        throw unexpectedElement(reader);
                    }
                }
                default:
                    throw unexpectedElement(reader);
            }
        }

        return nodes;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2632.java