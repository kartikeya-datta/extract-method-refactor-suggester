error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3219.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3219.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3219.java
text:
```scala
protected A@@bstractSubsystemAdd<MockSubsystemElement> getAdd() {

/**
 *
 */
package org.jboss.as.model.base.util;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import java.util.List;
import org.jboss.as.model.AbstractModelElement;
import org.jboss.as.model.AbstractSubsystemAdd;
import org.jboss.as.model.AbstractSubsystemElement;
import org.jboss.as.model.AbstractSubsystemUpdate;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * Mock {@link AbstractModelElement} that can be used in tests of other
 * elements that accept children of type xs:any.
 *
 * @author Brian Stansberry
 */
public class MockSubsystemElement extends AbstractSubsystemElement<MockSubsystemElement> {

    private static final long serialVersionUID = -649277969243521207L;

    public static final String NAMESPACE = "urn:jboss:domain:mock-extension:1.0";

    public static final String ANOTHER_NAMESPACE = "urn:jboss:domain:another-mock-extension:1.0";

    public static final String SUBSYSTEM = "subsystem";

    public static final QName MOCK_ELEMENT_QNAME = new QName(NAMESPACE, SUBSYSTEM);
    public static final QName ANOTHER_MOCK_ELEMENT_QNAME = new QName(ANOTHER_NAMESPACE, SUBSYSTEM);

    public static String getSingleSubsystemXmlContent() {
        return "<subsystem xmlns=\"" + NAMESPACE + "\"/>";
    }

    public static String getAnotherSubsystemXmlContent() {
        return "<subsystem xmlns=\"" + ANOTHER_NAMESPACE + "\"/>";
    }

    public static String getFullXmlContent() {
        return getSingleSubsystemXmlContent() + getAnotherSubsystemXmlContent();
    }

    /**
     * Creates a new MockSubsystemElement
     */
    public MockSubsystemElement(String namespaceURI) {
        super(namespaceURI);
    }

    @Override
    protected Class<MockSubsystemElement> getElementClass() {
        return MockSubsystemElement.class;
    }

    @Override
    public void writeContent(XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeEndElement();
    }

    @Override
    protected void getUpdates(List<? super AbstractSubsystemUpdate<MockSubsystemElement, ?>> list) {
    }

    @Override
    protected boolean isEmpty() {
        return true;
    }

    @Override
    protected AbstractSubsystemAdd getAdd() {
        return null;
    }

    @Override
    protected <P> void applyRemove(final UpdateContext updateContext, final UpdateResultHandler<? super Void, P> resultHandler, final P param) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3219.java