error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2101.java
text:
```scala
r@@eturn ThreadsSubsystemElement.class;

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

package org.jboss.as.threads;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;

import org.jboss.as.model.AbstractSubsystemAdd;
import org.jboss.as.model.AbstractSubsystemElement;
import org.jboss.as.model.AbstractSubsystemUpdate;
import org.jboss.as.model.ChildElement;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.logging.Logger;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ThreadsSubsystemElement extends AbstractSubsystemElement<ThreadsSubsystemElement> {

    private static final long serialVersionUID = -8577568464935736902L;
    private static final Logger log = Logger.getLogger("org.jboss.as.threads");

    private final NavigableMap<String, ThreadFactoryElement> threadFactories = new TreeMap<String, ThreadFactoryElement>();
    private final NavigableMap<String, ChildElement<? extends AbstractExecutorElement<?>>> executors = new TreeMap<String, ChildElement<? extends AbstractExecutorElement<?>>>();

    protected ThreadsSubsystemElement() {
        super(Namespace.CURRENT.getUriString());
    }

    @Override
    protected Class<ThreadsSubsystemElement> getElementClass() {
        return null;
    }

    @Override
    public void writeContent(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {

        for (ThreadFactoryElement tfe : threadFactories.values()) {
            streamWriter.writeStartElement(Element.THREAD_FACTORY.getLocalName());
            tfe.writeContent(streamWriter);
        }
        for (ChildElement<? extends AbstractExecutorElement<?>> childElement : executors.values()) {
            streamWriter.writeStartElement(childElement.getLocalName());
            childElement.getElement().writeContent(streamWriter);
        }

        streamWriter.writeEndElement();
    }

    protected void getUpdates(final List<? super AbstractSubsystemUpdate<ThreadsSubsystemElement, ?>> objects) {
        for (String name : threadFactories.keySet()) {
            objects.add(new ThreadFactoryAdd(name));
        }
        for (String name : executors.keySet()) {
            objects.add(executors.get(name).getElement().getAdd());
        }
    }

    protected boolean isEmpty() {
        return threadFactories.isEmpty() && executors.isEmpty();
    }

    protected ThreadsSubsystemAdd getAdd() {
        return new ThreadsSubsystemAdd();
    }

    protected <P> void applyRemove(final UpdateContext updateContext, final UpdateResultHandler<? super Void, P> resultHandler, final P param) {
        // no operation
    }

    ThreadFactoryElement getThreadFactory(final String name) {
        return threadFactories.get(name);
    }

    void removeThreadFactory(final String name) {
        threadFactories.remove(name);
    }

    ThreadFactoryElement addThreadFactory(final String name) {
        if (threadFactories.containsKey(name)) {
            return null;
        }
        final ThreadFactoryElement element = new ThreadFactoryElement(name);
        threadFactories.put(name, element);
        return element;
    }

    AbstractExecutorElement<?> getExecutor(final String name) {
        final ChildElement<? extends AbstractExecutorElement<?>> childElement = executors.get(name);
        return childElement == null ? null : childElement.getElement();
    }

    void removeExecutor(final String name) {
        executors.remove(name);
    }

    void addExecutor(final String name, final ChildElement<? extends AbstractExecutorElement<?>> element) {
        executors.put(name, element);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2101.java