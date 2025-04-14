error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9714.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9714.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9714.java
text:
```scala
d@@ef.getValue().getAttributeMarshaller().marshallAsAttribute(def.getValue(), model, false, writer);

package org.jboss.as.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.parsing.Element;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static org.jboss.as.controller.ControllerMessages.MESSAGES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADDRESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;

/**
 * A representation of a resource as needed by the XML parser.
 *
 * @author Stuart Douglas
 */
public class PersistentResourceXMLDescription {

    protected final PersistentResourceDefinition resourceDefinition;
    protected final String xmlElementName;
    protected final String xmlWrapperElement;
    protected final LinkedHashMap<String, AttributeDefinition> attributes;
    protected final List<PersistentResourceXMLDescription> children;
    protected final boolean useValueAsElementName;
    protected final boolean noAddOperation;
    protected final AdditionalOperationsGenerator additionalOperationsGenerator;
    private boolean flushRequired = true;

    protected PersistentResourceXMLDescription(final PersistentResourceDefinition resourceDefinition, final String xmlElementName, final String xmlWrapperElement, final LinkedHashMap<String, AttributeDefinition> attributes, final List<PersistentResourceXMLDescription> children, final boolean useValueAsElementName, final boolean noAddOperation, final AdditionalOperationsGenerator additionalOperationsGenerator) {
        this.resourceDefinition = resourceDefinition;
        this.xmlElementName = xmlElementName;
        this.xmlWrapperElement = xmlWrapperElement;
        this.attributes = attributes;
        this.children = children;
        this.useValueAsElementName = useValueAsElementName;
        this.noAddOperation = noAddOperation;
        this.additionalOperationsGenerator = additionalOperationsGenerator;
    }

    public void parse(final XMLExtendedStreamReader reader, PathAddress parentAddress, List<ModelNode> list) throws XMLStreamException {
        if (xmlWrapperElement != null) {
            if (reader.getLocalName().equals(xmlWrapperElement)) {
                if (reader.hasNext()) {
                    if (reader.nextTag() == END_ELEMENT) {
                        return;
                    }
                }
            } else {
                throw ParseUtils.unexpectedElement(reader);
            }

        }
        boolean wildcard = resourceDefinition.getPathElement().isWildcard();
        String name = null;
        ModelNode op = Util.createAddOperation();
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attributeName = reader.getAttributeLocalName(i);
            String value = reader.getAttributeValue(i);
            if (wildcard && NAME.equals(attributeName)) {
                name = value;
            } else if (attributes.containsKey(attributeName)) {
                AttributeDefinition def = attributes.get(attributeName);
                if (def instanceof SimpleAttributeDefinition) {
                    ((SimpleAttributeDefinition) def).parseAndSetParameter(value, op, reader);
                } else if (def instanceof StringListAttributeDefinition) {
                    ((StringListAttributeDefinition) def).parseAndSetParameter(value, op, reader);
                } else {
                    throw new IllegalArgumentException("we should know how to handle " + def);
                }
            } else {
                throw ParseUtils.unexpectedAttribute(reader, i);
            }
        }
        for (AttributeDefinition attributeDefinition: attributes.values()){
            if (attributeDefinition instanceof PropertiesAttributeDefinition){
                PropertiesAttributeDefinition attribute = (PropertiesAttributeDefinition) attributeDefinition;
                attribute.parse(reader,op);
                flushRequired = false;
            }
        }
        if (wildcard && name == null) {
            throw MESSAGES.missingRequiredAttributes(new StringBuilder(NAME), reader.getLocation());
        }
        PathElement path = wildcard ? PathElement.pathElement(resourceDefinition.getPathElement().getKey(), name) : resourceDefinition.getPathElement();
        PathAddress address = parentAddress.append(path);
        if(!noAddOperation) {
            op.get(ADDRESS).set(address.toModelNode());
            list.add(op);
        }
        if(additionalOperationsGenerator != null) {
            additionalOperationsGenerator.additionalOperations(address, op, list);
        }
        parseChildren(reader, address, list);
        if (xmlWrapperElement != null) {
            ParseUtils.requireNoContent(reader);
        }
    }

    private Map<String, PersistentResourceXMLDescription> getChildrenMap() {
        Map<String, PersistentResourceXMLDescription> res = new HashMap<>();
        for (PersistentResourceXMLDescription child : children) {
            if (child.xmlWrapperElement != null) {
                res.put(child.xmlWrapperElement, child);
            } else {
                res.put(child.xmlElementName, child);
            }
        }
        return res;
    }

    public void parseChildren(final XMLExtendedStreamReader reader, PathAddress parentAddress, List<ModelNode> list) throws XMLStreamException {
        if (children.size() == 0) {
            if (flushRequired){
                ParseUtils.requireNoContent(reader);
            }
        } else {
            Map<String, PersistentResourceXMLDescription> children = getChildrenMap();
            while (reader.hasNext() && reader.nextTag() != XMLStreamConstants.END_ELEMENT) {
                PersistentResourceXMLDescription child = children.get(reader.getLocalName());
                if (child != null) {
                    child.parse(reader, parentAddress, list);
                } else {
                    throw ParseUtils.unexpectedElement(reader);
                }
            }
        }
    }


    public void persist(XMLExtendedStreamWriter writer, ModelNode model) throws XMLStreamException {
        persist(writer, model, null);
    }

    private void writeStartElement(XMLExtendedStreamWriter writer, String namespaceURI, String localName) throws XMLStreamException {
        if (namespaceURI != null) {
            writer.writeStartElement(namespaceURI, localName);
        } else {
            writer.writeStartElement(localName);
        }
    }

    public void startSubsystemElement(XMLExtendedStreamWriter writer, String namespaceURI, boolean empty) throws XMLStreamException {

        if (writer.getNamespaceContext().getPrefix(namespaceURI) == null) {
            // Unknown namespace; it becomes default
            writer.setDefaultNamespace(namespaceURI);
            if (empty) {
                writer.writeEmptyElement(Element.SUBSYSTEM.getLocalName());
            } else {
                writer.writeStartElement(Element.SUBSYSTEM.getLocalName());
            }
            writer.writeNamespace(null, namespaceURI);
        } else {
            if (empty) {
                writer.writeEmptyElement(namespaceURI, Element.SUBSYSTEM.getLocalName());
            } else {
                writer.writeStartElement(namespaceURI, Element.SUBSYSTEM.getLocalName());
            }
        }

    }

    public void persist(XMLExtendedStreamWriter writer, ModelNode model, String namespaceURI) throws XMLStreamException {
        boolean wildcard = resourceDefinition.getPathElement().isWildcard();
        model = wildcard ? model.get(resourceDefinition.getPathElement().getKey()) : model.get(resourceDefinition.getPathElement().getKeyValuePair());
        boolean isSubsystem = resourceDefinition.getPathElement().getKey().equals(ModelDescriptionConstants.SUBSYSTEM);
        if (!isSubsystem && !model.isDefined() && !useValueAsElementName) {
            return;
        }

        boolean writeWrapper = xmlWrapperElement != null;
        if (writeWrapper) {
            writeStartElement(writer, namespaceURI, xmlWrapperElement);
        }

        if (wildcard) {
            for (Property p : model.asPropertyList()) {
                if (useValueAsElementName) {
                    writeStartElement(writer, namespaceURI, p.getName());
                } else {
                    writeStartElement(writer, namespaceURI, xmlElementName);
                    writer.writeAttribute(NAME, p.getName());
                }
                for (Map.Entry<String, AttributeDefinition> def : attributes.entrySet()) {
                    def.getValue().getAttributeMarshaller().marshallAsAttribute(def.getValue(), p.getValue(), false, writer);
                }
                persistChildren(writer, p.getValue());
                writer.writeEndElement();
            }
        } else {
            if (useValueAsElementName) {
                writeStartElement(writer, namespaceURI, resourceDefinition.getPathElement().getValue());
            } else if (isSubsystem) {
                startSubsystemElement(writer, namespaceURI, children.isEmpty());
            } else {
                writeStartElement(writer, namespaceURI, xmlElementName);

            }
            for (Map.Entry<String, AttributeDefinition> def : attributes.entrySet()) {
                def.getValue().getAttributeMarshaller().marshallAsAttribute(def.getValue(), model, true, writer);
            }
            persistChildren(writer, model);

            // Do not attempt to write end element if the <subsystem/> has no elements!
            if (!isSubsystem || !children.isEmpty()) {
                writer.writeEndElement();
            }
        }

        if (writeWrapper) {
            writer.writeEndElement();
        }
    }

    public void persistChildren(XMLExtendedStreamWriter writer, ModelNode model) throws XMLStreamException {
        for (PersistentResourceXMLDescription child : children) {
            child.persist(writer, model);
        }
    }

    public static PersistentResourceXMLBuilder builder(PersistentResourceDefinition resource) {
        return new PersistentResourceXMLBuilder(resource);
    }

    public static class PersistentResourceXMLBuilder {


        protected final PersistentResourceDefinition resourceDefinition;
        protected String xmlElementName;
        protected String xmlWrapperElement;
        protected boolean useValueAsElementName;
        protected boolean noAddOperation;
        protected AdditionalOperationsGenerator additionalOperationsGenerator;
        protected final LinkedHashMap<String, AttributeDefinition> attributes = new LinkedHashMap<>();
        protected final List<PersistentResourceXMLBuilder> children = new ArrayList<>();

        protected PersistentResourceXMLBuilder(final PersistentResourceDefinition resourceDefinition) {
            this.resourceDefinition = resourceDefinition;
            this.xmlElementName = resourceDefinition.getPathElement().isWildcard() ? resourceDefinition.getPathElement().getKey() : resourceDefinition.getPathElement().getValue();
        }

        public PersistentResourceXMLBuilder addChild(PersistentResourceXMLBuilder builder) {
            this.children.add(builder);
            return this;
        }

        public PersistentResourceXMLBuilder addAttribute(AttributeDefinition attribute) {
            this.attributes.put(attribute.getName(), attribute);
            return this;
        }

        public PersistentResourceXMLBuilder addAttributes(AttributeDefinition... attributes) {
            for (final AttributeDefinition at : attributes) {
                this.attributes.put(at.getName(), at);
            }
            return this;
        }

        public PersistentResourceXMLBuilder addAttributes(Collection<? extends AttributeDefinition> attributes) {
            for (final AttributeDefinition at : attributes) {
                this.attributes.put(at.getName(), at);
            }
            return this;
        }

        public PersistentResourceXMLBuilder setXmlWrapperElement(final String xmlWrapperElement) {
            this.xmlWrapperElement = xmlWrapperElement;
            return this;
        }

        public PersistentResourceXMLBuilder setXmlElementName(final String xmlElementName) {
            this.xmlElementName = xmlElementName;
            return this;
        }

        public PersistentResourceXMLBuilder setUseValueAsElementName(final boolean useValueAsElementName) {
            this.useValueAsElementName = useValueAsElementName;
            return this;
        }

        public PersistentResourceXMLBuilder setNoAddOperation(final boolean noAddOperation) {
            this.noAddOperation = noAddOperation;
            return this;
        }

        public PersistentResourceXMLBuilder setAdditionalOperationsGenerator(final AdditionalOperationsGenerator additionalOperationsGenerator) {
            this.additionalOperationsGenerator = additionalOperationsGenerator;
            return this;
        }

        public PersistentResourceXMLDescription build() {

            List<PersistentResourceXMLDescription> builtChildren = new ArrayList<>();
            for (PersistentResourceXMLBuilder b : children) {
                builtChildren.add(b.build());
            }
            return new PersistentResourceXMLDescription(resourceDefinition, xmlElementName, xmlWrapperElement, attributes, builtChildren, useValueAsElementName, noAddOperation, additionalOperationsGenerator);
        }
    }

    /**
     * Some resources require more operations that just a simple add. This interface provides a hook for these to be plugged in.
     */
    public interface AdditionalOperationsGenerator {

        /**
         * Generates any additional operations required by the resource
         * @param address The address of the resource
         * @param addOperation The add operation for the resource
         * @param operations The operation list
         */
        void additionalOperations(final PathAddress address, final ModelNode addOperation, final List<ModelNode> operations);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9714.java