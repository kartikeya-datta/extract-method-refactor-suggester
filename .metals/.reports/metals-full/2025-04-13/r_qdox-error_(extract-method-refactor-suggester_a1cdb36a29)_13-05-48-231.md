error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/219.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/219.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/219.java
text:
```scala
B@@oolean localReceiverPassByValue = null;

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

package org.jboss.as.ee.structure;

import static javax.xml.stream.XMLStreamConstants.ATTRIBUTE;
import static javax.xml.stream.XMLStreamConstants.CDATA;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.COMMENT;
import static javax.xml.stream.XMLStreamConstants.DTD;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.ENTITY_DECLARATION;
import static javax.xml.stream.XMLStreamConstants.ENTITY_REFERENCE;
import static javax.xml.stream.XMLStreamConstants.NAMESPACE;
import static javax.xml.stream.XMLStreamConstants.NOTATION_DECLARATION;
import static javax.xml.stream.XMLStreamConstants.PROCESSING_INSTRUCTION;
import static javax.xml.stream.XMLStreamConstants.SPACE;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.as.ee.EeMessages;
import org.jboss.as.ee.metadata.EJBClientDescriptorMetaData;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLExtendedStreamReader;

/**
 * Parser for urn:jboss:ejb-client:1.0:jboss-ejb-client
 *
 * @author Jaikiran Pai
 */
class EJBClientDescriptor10Parser implements XMLElementReader<EJBClientDescriptorMetaData> {

    public static final String NAMESPACE_1_0 = "urn:jboss:ejb-client:1.0";

    public static final EJBClientDescriptor10Parser INSTANCE = new EJBClientDescriptor10Parser();


    private EJBClientDescriptor10Parser() {
    }


    enum Element {
        CLIENT_CONTEXT,
        EJB_RECEIVERS,
        JBOSS_EJB_CLIENT,
        REMOTING_EJB_RECEIVER,
        // default unknown element
        UNKNOWN;

        private static final Map<QName, Element> elements;

        static {
            Map<QName, Element> elementsMap = new HashMap<QName, Element>();
            elementsMap.put(new QName(NAMESPACE_1_0, "jboss-ejb-client"), Element.JBOSS_EJB_CLIENT);
            elementsMap.put(new QName(NAMESPACE_1_0, "client-context"), Element.CLIENT_CONTEXT);
            elementsMap.put(new QName(NAMESPACE_1_0, "ejb-receivers"), Element.EJB_RECEIVERS);
            elementsMap.put(new QName(NAMESPACE_1_0, "remoting-ejb-receiver"), Element.REMOTING_EJB_RECEIVER);
            elements = elementsMap;
        }

        static Element of(QName qName) {
            QName name;
            if (qName.getNamespaceURI().equals("")) {
                name = new QName(NAMESPACE_1_0, qName.getLocalPart());
            } else {
                name = qName;
            }
            final Element element = elements.get(name);
            return element == null ? UNKNOWN : element;
        }
    }

    enum Attribute {
        EXCLUDE_LOCAL_RECEIVER,
        LOCAL_RECEIVER_PASS_BY_VALUE,
        OUTBOUND_CONNECTION_REF,

        // default unknown attribute
        UNKNOWN;

        private static final Map<QName, Attribute> attributes;

        static {
            Map<QName, Attribute> attributesMap = new HashMap<QName, Attribute>();
            attributesMap.put(new QName("exclude-local-receiver"), EXCLUDE_LOCAL_RECEIVER);
            attributesMap.put(new QName("local-receiver-pass-by-value"), LOCAL_RECEIVER_PASS_BY_VALUE);
            attributesMap.put(new QName("outbound-connection-ref"), OUTBOUND_CONNECTION_REF);
            attributes = attributesMap;
        }

        static Attribute of(QName qName) {
            final Attribute attribute = attributes.get(qName);
            return attribute == null ? UNKNOWN : attribute;
        }
    }

    @Override
    public void readElement(final XMLExtendedStreamReader reader, final EJBClientDescriptorMetaData ejbClientDescriptorMetaData) throws XMLStreamException {
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case END_ELEMENT: {
                    return;
                }
                case START_ELEMENT: {
                    final Element element = Element.of(reader.getName());

                    switch (element) {
                        case CLIENT_CONTEXT:
                            this.parseClientContext(reader, ejbClientDescriptorMetaData);
                            break;
                        default:
                            this.unexpectedElement(reader);
                    }
                    break;
                }
                default: {
                    this.unexpectedContent(reader);
                }
            }
        }
        unexpectedEndOfDocument(reader.getLocation());
    }

    private void parseClientContext(final XMLExtendedStreamReader reader, final EJBClientDescriptorMetaData ejbClientDescriptorMetaData) throws XMLStreamException {
        final Set<Element> visited = EnumSet.noneOf(Element.class);
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case END_ELEMENT: {
                    return;
                }
                case START_ELEMENT: {
                    final Element element = Element.of(reader.getName());
                    if (visited.contains(element)) {
                        this.unexpectedElement(reader);
                    }
                    visited.add(element);
                    switch (element) {
                        case EJB_RECEIVERS:
                            this.parseEJBReceivers(reader, ejbClientDescriptorMetaData);
                            break;
                        default:
                            this.unexpectedElement(reader);
                    }
                    break;
                }
                default: {
                    unexpectedContent(reader);
                }
            }
        }
        unexpectedEndOfDocument(reader.getLocation());
    }

    private void parseEJBReceivers(final XMLExtendedStreamReader reader, final EJBClientDescriptorMetaData ejbClientDescriptorMetaData) throws XMLStreamException {

        // initialize the local-receiver-pass-by-value to the default true
        boolean localReceiverPassByValue = true;

        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final Attribute attribute = Attribute.of(reader.getAttributeName(i));
            final String val = reader.getAttributeValue(i);
            switch (attribute) {
                case EXCLUDE_LOCAL_RECEIVER:
                    final boolean excludeLocalReceiver = Boolean.parseBoolean(val.trim());
                    ejbClientDescriptorMetaData.setExcludeLocalReceiver(excludeLocalReceiver);
                    break;
                case LOCAL_RECEIVER_PASS_BY_VALUE:
                    localReceiverPassByValue = Boolean.parseBoolean(val.trim());
                    break;
                default:
                    unexpectedContent(reader);
            }
        }
        // set the local receiver pass by value into the metadata
        ejbClientDescriptorMetaData.setLocalReceiverPassByValue(localReceiverPassByValue);
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case END_ELEMENT: {
                    return;
                }
                case START_ELEMENT: {
                    final Element element = Element.of(reader.getName());
                    switch (element) {
                        case REMOTING_EJB_RECEIVER:
                            this.parseRemotingReceiver(reader, ejbClientDescriptorMetaData);
                            break;
                        default:
                            this.unexpectedElement(reader);
                    }
                    break;
                }
                default: {
                    unexpectedContent(reader);
                }
            }
        }
        unexpectedEndOfDocument(reader.getLocation());
    }

    private void parseRemotingReceiver(final XMLExtendedStreamReader reader, final EJBClientDescriptorMetaData ejbClientDescriptorMetaData) throws XMLStreamException {
        String outboundConnectionRef = null;
        final Set<Attribute> required = EnumSet.of(Attribute.OUTBOUND_CONNECTION_REF);
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final Attribute attribute = Attribute.of(reader.getAttributeName(i));
            required.remove(attribute);
            switch (attribute) {
                case OUTBOUND_CONNECTION_REF:
                    outboundConnectionRef = reader.getAttributeValue(i).trim();
                    ejbClientDescriptorMetaData.addRemotingReceiverConnectionRef(outboundConnectionRef);
                    break;
                default:
                    unexpectedContent(reader);
            }
        }
        if (!required.isEmpty()) {
            missingAttributes(reader.getLocation(), required);
        }
        // This element is just composed of attributes which we already processed, so no more content
        // is expected
        if (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            unexpectedContent(reader);
        }
    }

    private static void unexpectedEndOfDocument(final Location location) throws XMLStreamException {
        throw EeMessages.MESSAGES.errorParsingEJBClientDescriptor("Unexpected end of document", location);
    }

    private static void missingAttributes(final Location location, final Set<Attribute> required) throws XMLStreamException {
        final StringBuilder b = new StringBuilder("Missing one or more required attributes:");
        for (Attribute attribute : required) {
            b.append(' ').append(attribute);
        }
        throw EeMessages.MESSAGES.errorParsingEJBClientDescriptor(b.toString(), location);
    }

    /**
     * Throws a XMLStreamException for the unexpected element that was encountered during the parse
     *
     * @param reader the stream reader
     * @throws XMLStreamException
     */
    public static void unexpectedElement(final XMLExtendedStreamReader reader) throws XMLStreamException {
        throw EeMessages.MESSAGES.unexpectedElement(reader.getName(), reader.getLocation());
    }

    private static void unexpectedContent(final XMLStreamReader reader) throws XMLStreamException {
        final String kind;
        switch (reader.getEventType()) {
            case ATTRIBUTE:
                kind = "attribute";
                break;
            case CDATA:
                kind = "cdata";
                break;
            case CHARACTERS:
                kind = "characters";
                break;
            case COMMENT:
                kind = "comment";
                break;
            case DTD:
                kind = "dtd";
                break;
            case END_DOCUMENT:
                kind = "document end";
                break;
            case END_ELEMENT:
                kind = "element end";
                break;
            case ENTITY_DECLARATION:
                kind = "entity declaration";
                break;
            case ENTITY_REFERENCE:
                kind = "entity ref";
                break;
            case NAMESPACE:
                kind = "namespace";
                break;
            case NOTATION_DECLARATION:
                kind = "notation declaration";
                break;
            case PROCESSING_INSTRUCTION:
                kind = "processing instruction";
                break;
            case SPACE:
                kind = "whitespace";
                break;
            case START_DOCUMENT:
                kind = "document start";
                break;
            case START_ELEMENT:
                kind = "element start";
                break;
            default:
                kind = "unknown";
                break;
        }
        final StringBuilder b = new StringBuilder("Unexpected content of type '").append(kind).append('\'');
        if (reader.hasName()) {
            b.append(" named '").append(reader.getName()).append('\'');
        }
        if (reader.hasText()) {
            b.append(", text is: '").append(reader.getText()).append('\'');
        }
        throw EeMessages.MESSAGES.errorParsingEJBClientDescriptor(b.toString(), reader.getLocation());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/219.java