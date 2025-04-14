error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13548.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13548.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13548.java
text:
```scala
r@@eturn calculateElementHashOf(interfaceCriteria.values(), 17L);

/**
 *
 */
package org.jboss.as.model.socket;

import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;

import org.jboss.as.model.AbstractModelUpdate;
import org.jboss.as.model.Element;
import org.jboss.as.model.Namespace;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * Indicates that if a network interface satisfies either any or none of a set of nested
 * criteria, it may be used. Whether the test is for any or none depends on the
 * <code>isAny</code> parameter passed to the constructor.
 *
 * @author Brian Stansberry
 */
public class CompoundCriteriaElement extends AbstractInterfaceCriteriaElement<CompoundCriteriaElement> {

    private static final long serialVersionUID = -649277969243521207L;

    private final NavigableMap<Element, AbstractInterfaceCriteriaElement<?>> interfaceCriteria =
            new TreeMap<Element, AbstractInterfaceCriteriaElement<?>>();
    /**
     * Creates a new CompoundCriteriaElement by parsing an xml stream
     *
     * @param reader stream reader used to read the xml
     * @param isAny true if this type {@link Element#ANY}, false if it is {@link Element#NOT}.
     *
     * @throws XMLStreamException if an error occurs
     */
    public CompoundCriteriaElement(XMLExtendedStreamReader reader, boolean isAny) throws XMLStreamException {
        super(reader, isAny ? Element.ANY : Element.NOT);

        Set<InterfaceCriteria> criteria = new HashSet<InterfaceCriteria>(interfaceCriteria.size());
        for (AbstractInterfaceCriteriaElement<?> element : interfaceCriteria.values()) {
            criteria.add(element.getInterfaceCriteria());
        }

        InterfaceCriteria ours = isAny ? new AnyInterfaceCriteria(criteria) : new NotInterfaceCriteria(criteria);
        setInterfaceCriteria(ours);


        // Handle attributes
        requireNoAttributes(reader);
        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case DOMAIN_1_0: {
                    final Element element = Element.forName(reader.getLocalName());
                    AbstractInterfaceCriteriaElement<?> aice = ParsingUtil.parseSimpleInterfaceCriteria(reader, element);
                    interfaceCriteria.put(aice.getElement(), aice);
                    break;
                }
                default: throw unexpectedElement(reader);
            }
        }
        if (interfaceCriteria.isEmpty()) {
            throw ParsingUtil.missingCriteria(reader, ParsingUtil.SIMPLE_CRITERIA_STRING);
        }
    }

    @Override
    public long elementHash() {
        synchronized (interfaceCriteria) {
            return calculateElementHashOf(interfaceCriteria.values(), 17l);
        }
    }

    @Override
    public void writeContent(XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        synchronized (interfaceCriteria) {
            for (AbstractInterfaceCriteriaElement<?> criteria : interfaceCriteria.values()) {
                streamWriter.writeStartElement(criteria.getElement().getLocalName());
                criteria.writeContent(streamWriter);
            }
        }
        streamWriter.writeEndElement();
    }

    @Override
    protected void appendDifference(Collection<AbstractModelUpdate<CompoundCriteriaElement>> target, CompoundCriteriaElement other) {
        // FIXME implement appendDifference
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    protected Class<CompoundCriteriaElement> getElementClass() {
        return CompoundCriteriaElement.class;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13548.java