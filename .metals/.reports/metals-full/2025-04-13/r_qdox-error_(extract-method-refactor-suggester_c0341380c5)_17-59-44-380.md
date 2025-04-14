error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2413.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2413.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[20,1]

error in qdox parser
file content:
```java
offset: 858
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2413.java
text:
```scala
abstract class ElementImpl extends XMLEventImpl {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

p@@ackage org.apache.xerces.stax.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.events.Namespace;

/**
 * @xerces.internal
 * 
 * @author Lucian Holland
 * @author Michael Glavassevich, IBM
 * 
 * @version $Id$
 */
public abstract class ElementImpl extends XMLEventImpl {
    
    /**
     * The qualified name of the element that is being closed.
     */
    private final QName fName;
    
    /**
     * Namespaces declared in the current scope.
     */
    private final List fNamespaces;
    
    /**
     * Constructor.
     */
    ElementImpl(final QName name, final boolean isStartElement, Iterator namespaces, final Location location) {
        super(isStartElement ? START_ELEMENT : END_ELEMENT, location);
        fName = name;
        if (namespaces != null && namespaces.hasNext()) {
            fNamespaces = new ArrayList();
            do {
                Namespace ns = (Namespace) namespaces.next();
                fNamespaces.add(ns);
            }
            while (namespaces.hasNext());
        }
        else {
            fNamespaces = Collections.EMPTY_LIST;
        }
    }
    
    /**
     * @see javax.xml.stream.events.StartElement#getName()
     * @see javax.xml.stream.events.EndElement#getName()
     */
    public final QName getName() {
        return fName;
    }
    
    /**
     * @see javax.xml.stream.events.StartElement#getNamespaces()
     * @see javax.xml.stream.events.EndElement#getNamespaces()
     */
    public final Iterator getNamespaces() {
        return createImmutableIterator(fNamespaces.iterator());
    }
    
    static Iterator createImmutableIterator(Iterator iter) {
        return new NoRemoveIterator(iter);
    }
    
    private static final class NoRemoveIterator implements Iterator {
        
        private final Iterator fWrapped;
        
        public NoRemoveIterator(Iterator wrapped) {
            fWrapped = wrapped;
        }
        
        /**
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return fWrapped.hasNext();
        }

        /**
         * @see java.util.Iterator#next()
         */
        public Object next() {
            return fWrapped.next();
        }

        /**
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException("Attributes iterator is read-only.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2413.java