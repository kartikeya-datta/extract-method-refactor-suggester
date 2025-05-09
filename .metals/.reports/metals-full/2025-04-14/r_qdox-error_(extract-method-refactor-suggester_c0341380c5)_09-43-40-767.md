error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1130.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1130.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1130.java
text:
```scala
final S@@ocketBindingElement include = new SocketBindingElement(reader, interfaceResolver, this.defaultInterface);

/**
 * 
 */
package org.jboss.as.model.socket;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;

import org.jboss.as.model.AbstractModelElement;
import org.jboss.as.model.AbstractModelUpdate;
import org.jboss.as.model.Attribute;
import org.jboss.as.model.Element;
import org.jboss.as.model.Namespace;
import org.jboss.as.model.RefResolver;
import org.jboss.msc.service.Location;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * A named group of socket binding configurations that can be applied to a
 * server group.
 * 
 * @author Brian Stansberry
 */
public class SocketBindingGroupElement extends AbstractModelElement<SocketBindingGroupElement> implements ServiceActivator {

    private static final long serialVersionUID = -7389975620327080290L;

    private final String name;
    private final String defaultInterface;
    private final NavigableMap<String, SocketBindingGroupIncludeElement> includedGroups = new TreeMap<String, SocketBindingGroupIncludeElement>();
    private final NavigableMap<String, SocketBindingElement> socketBindings = new TreeMap<String, SocketBindingElement>();

    private final RefResolver<String, InterfaceElement> interfaceResolver;
    private final RefResolver<String, SocketBindingGroupElement> includedGroupResolver;
    
    /**
     * Construct a new instance.
     *
     * @param location the declaration location of the element
     * @param name the name of the group. Cannot be <code>null</code>
     * @param defaultInterface the name of the interface to use by default when
     *            nested {@link SocketBindingGroupElement}s do not declare an interface.
     *            Cannot be <code>null</code>
     * @param interfaceResolver {@link RefResolver} to use to resolve references 
     *           to interfaces. May be used safely in the constructor
     *           itself. Cannot be <code>null</code>
     * @param includedGroupResolver {@link RefResolver} to use to resolve references 
     *           to included socket binding groups. Should not be used in the constructor
     *           itself as referenced groups may not have been created yet.
     *           May be <code>null</code>, in which case any nested {@link Element#INCLUDE}
     *           element will result in an 
     *           {@link #unexpectedElement(XMLExtendedStreamReader) unexpected element exception}
     */
    public SocketBindingGroupElement(Location location, final String name, final String defaultInterface, 
            final RefResolver<String, InterfaceElement> interfaceResolver,
            final RefResolver<String, SocketBindingGroupElement> includedGroupResolver) {
        super(location);
        
        if (name == null)
            throw new IllegalArgumentException("name is null");
        this.name = name;
        
        if (interfaceResolver == null)
            throw new IllegalArgumentException("interfaceResolver is null");
        this.interfaceResolver = interfaceResolver;
        
        if (defaultInterface == null)
            throw new IllegalArgumentException("defaultInterface is null");
        
        if (this.interfaceResolver.resolveRef(defaultInterface) == null) {
            throw new IllegalArgumentException("Unknown interface " + defaultInterface);
        }
        this.defaultInterface = defaultInterface;
        
        this.includedGroupResolver = includedGroupResolver;
    }

    /**
     * Construct a new instance.
     *
     * @param reader the reader from which to build this element
     * @param interfaceResolver {@link RefResolver} to use to resolve references 
     *           to interfaces. May be used safely in the constructor
     *           itself
     * @param includedGroupResolver {@link RefResolver} to use to resolve references 
     *           to included socket binding groups. Should not be used in the constructor
     *           itself as referenced groups may not have been created yet.
     *           May be <code>null</code>, in which case any nested {@link Element#INCLUDE}
     *           element will result in an 
     *           {@link #unexpectedElement(XMLExtendedStreamReader) unexpected element exception}
     *           
     * @throws XMLStreamException if an error occurs
     */
    public SocketBindingGroupElement(XMLExtendedStreamReader reader, 
            final RefResolver<String, InterfaceElement> interfaceResolver,
            final RefResolver<String, SocketBindingGroupElement> includedGroupResolver) throws XMLStreamException {
        super(reader);
        
        if (interfaceResolver == null)
            throw new IllegalArgumentException("interfaceResolver is null");
        this.interfaceResolver = interfaceResolver;
        
        this.includedGroupResolver = includedGroupResolver;
        
        // Handle attributes
        String name = null;
        String defIntf = null;
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i ++) {
            final String value = reader.getAttributeValue(i);
            if (reader.getAttributeNamespace(i) != null) {
                throw unexpectedAttribute(reader, i);
            } else {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case NAME: {
                        name = value;
                        break;
                    }
                    case DEFAULT_INTERFACE: {
                        if (this.interfaceResolver.resolveRef(value) == null) {
                            throw new XMLStreamException("Unknown interface " + value + 
                                    " " + attribute.getLocalName() + " must be declared in element " + 
                                    Element.INTERFACES.getLocalName(), reader.getLocation());
                        }
                        defIntf = value;
                        break;
                    }
                    default: throw unexpectedAttribute(reader, i);
                }
            }
        }
        if (name == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
        }
        if (defIntf == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.DEFAULT_INTERFACE));
        }
        this.name = name;
        this.defaultInterface = defIntf;
        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case DOMAIN_1_0: {
                    final Element element = Element.forName(reader.getLocalName());
                    switch (element) {
                        case INCLUDE: {
                            if (includedGroupResolver == null) {
                                throw unexpectedElement(reader);
                            }
                            final SocketBindingGroupIncludeElement include = new SocketBindingGroupIncludeElement(reader);
                            if (includedGroups.containsKey(include.getGroupName())) {
                                throw new XMLStreamException("Included socket-binding-group " + include.getGroupName() + " already declared", reader.getLocation());
                            }
                            includedGroups.put(include.getGroupName(), include);
                            break;
                        }
                        case SOCKET_BINDING: {
                            final SocketBindingElement include = new SocketBindingElement(reader, interfaceResolver);
                            if (socketBindings.containsKey(include.getName())) {
                                throw new XMLStreamException("socket-binding " + include.getName() + " already declared", reader.getLocation());
                            }
                            socketBindings.put(include.getName(), include);
                            break;
                        }
                        default: throw unexpectedElement(reader);
                    }
                    break;
                }
                default: throw unexpectedElement(reader);
            }
        }
    }
    
    /**
     * Gets the name of the socket binding group.
     * 
     * @return the group name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the name of the default interface to use for socket bindings
     * that do not declare an interface.
     * 
     * @return the interface name
     */
    public String getDefaultInterface() {
        return defaultInterface;
    }
    
    public Set<SocketBindingGroupIncludeElement> getIncludedSocketBindingGroups() {
        return Collections.unmodifiableSet(new HashSet<SocketBindingGroupIncludeElement>(includedGroups.values()));
    }
    
    public Set<SocketBindingElement> getAllSocketBindings() {
        if (this.includedGroups.size() == 0)
            return Collections.unmodifiableSet(new HashSet<SocketBindingElement>(socketBindings.values()));
        else {
            Map<String, SocketBindingElement> result = new HashMap<String, SocketBindingElement>();
            for (String groupName : includedGroups.keySet()) {
                SocketBindingGroupElement group = includedGroupResolver.resolveRef(groupName);
                if (group == null) {
                    throw new IllegalStateException("Cannot resolve reference from socket binding group " + name + " to included socket binding group " + groupName);
                }
                Set<SocketBindingElement> included = group.getAllSocketBindings();
                for (SocketBindingElement binding : included) {
                    result.put(binding.getName(), binding);
                }
            }
            result.putAll(socketBindings);
            return Collections.unmodifiableSet(new HashSet<SocketBindingElement>(result.values()));
        }
    }

    /** {@inheritDoc} */
    public void activate(ServiceActivatorContext serviceActivatorContext) {
    	for(SocketBindingElement element : getAllSocketBindings()) {
    		element.activate(serviceActivatorContext);
    	}
    }
    
    /* (non-Javadoc)
     * @see org.jboss.as.model.AbstractModelElement#appendDifference(java.util.Collection, org.jboss.as.model.AbstractModelElement)
     */
    @Override
    protected void appendDifference(Collection<AbstractModelUpdate<SocketBindingGroupElement>> target,
            SocketBindingGroupElement other) {
        // FIXME implement appendDifference
        throw new UnsupportedOperationException("implement me");
    }

    /* (non-Javadoc)
     * @see org.jboss.as.model.AbstractModelElement#elementHash()
     */
    @Override
    public long elementHash() {
        long cksum = name.hashCode() & 0xffffffffL;
        cksum = Long.rotateLeft(cksum, 1) ^ defaultInterface.hashCode() & 0xffffffffL;
        cksum = calculateElementHashOf(includedGroups.values(), cksum);
        cksum = calculateElementHashOf(socketBindings.values(), cksum);
        return cksum;
    }

    /* (non-Javadoc)
     * @see org.jboss.as.model.AbstractModelElement#getElementClass()
     */
    @Override
    protected Class<SocketBindingGroupElement> getElementClass() {
        return SocketBindingGroupElement.class;
    }

    /* (non-Javadoc)
     * @see org.jboss.as.model.AbstractModelElement#writeContent(org.jboss.staxmapper.XMLExtendedStreamWriter)
     */
    @Override
    public void writeContent(XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeAttribute(Attribute.NAME.getLocalName(), name);
        streamWriter.writeAttribute(Attribute.DEFAULT_INTERFACE.getLocalName(), defaultInterface);
        for (SocketBindingGroupIncludeElement included : includedGroups.values()) {
            streamWriter.writeStartElement(Element.INCLUDE.getLocalName());
            included.writeContent(streamWriter);
        }for (SocketBindingElement included : socketBindings.values()) {
            streamWriter.writeStartElement(Element.SOCKET_BINDING.getLocalName());
            included.writeContent(streamWriter);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1130.java