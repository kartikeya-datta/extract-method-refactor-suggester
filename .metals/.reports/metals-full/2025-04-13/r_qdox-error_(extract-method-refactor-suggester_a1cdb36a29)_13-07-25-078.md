error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8747.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8747.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8747.java
text:
```scala
s@@uper();

/**
 *
 */
package org.jboss.as.model.socket;

import javax.xml.stream.XMLStreamException;

import org.jboss.as.model.AbstractModelElement;
import org.jboss.as.model.Element;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * Base class for domain model elements that represent
 * {@link InterfaceCriteria the criteria for choosing an IP address} for a
 * {@link InterfaceElement named interface}.
 *
 * @author Brian Stansberry
 */
public abstract class AbstractInterfaceCriteriaElement<T extends AbstractInterfaceCriteriaElement<T>>
    extends AbstractModelElement<T> {

    private static final long serialVersionUID = 396313309912557378L;

    private final Element element;
    private InterfaceCriteria interfaceCriteria;

    /**
     * Creates a new AbstractInterfaceCriteriaElement by parsing an xml stream.
     * Subclasses using this constructor are responsible for calling
     * {@link #setInterfaceCriteria(InterfaceCriteria)} before returning from
     * their constructor.
     *
     * @param reader stream reader used to read the xml
     * @param element the element being read
     *
     * @throws XMLStreamException if an error occurs
     */
    protected AbstractInterfaceCriteriaElement(XMLExtendedStreamReader reader, final Element element) throws XMLStreamException {
        super(reader);
        if (element == null)
            throw new IllegalArgumentException("element is null");
        this.element = element;
    }

    /**
     * Creates a new AbstractInterfaceCriteriaElement by parsing an xml stream
     *
     * @param reader stream reader used to read the xml
     * @param element the element being read
     * @param interfaceCriteria the criteria to use to check whether an network
     *         interface and address is acceptable for use by an interface
     *
     * @throws XMLStreamException if an error occurs
     */
    protected AbstractInterfaceCriteriaElement(XMLExtendedStreamReader reader, final Element element, final InterfaceCriteria interfaceCriteria) throws XMLStreamException {
        this(reader, element);
        setInterfaceCriteria(interfaceCriteria);
    }

    /**
     * Gets the InterfaceCriteria associated with this element.
     *
     * @return the criteria. May be <code>null</code> if this method is invoked
     *                  before any subclass constructor has completed; otherwise
     *                  will not be <code>null</code>
     */
    InterfaceCriteria getInterfaceCriteria() {
        return interfaceCriteria;
    }

    /**
     * Sets the InterfaceCriteria associated with this element.
     *
     * @param the criteria. Cannot be <code>null</code>
     */
    protected final void setInterfaceCriteria(InterfaceCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("criteria is null");
        }
        this.interfaceCriteria = criteria;
    }

    /**
     * Gets the {@link Element} type this object represents.
     *
     * @return the element type. Will not be <code>null</code>
     */
    Element getElement() {
        return element;
    }

    /**
     * {@inheritDoc}
     *
     * This default implementation uses the hash code of the {@link Element}
     * passed to the constructor. This is appropriate for subclasses with
     * no internal state.
     */
    @Override
    public long elementHash() {
        return element.hashCode() & 0xFFFFFFFF;
    }

    /**
     * {@inheritDoc}
     *
     * This default implementation simple writes the end of the element. This
     * is appropriate for subclasses whose element type has no attributes or child elements.
     */
    @Override
    public void writeContent(XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeEndElement();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8747.java