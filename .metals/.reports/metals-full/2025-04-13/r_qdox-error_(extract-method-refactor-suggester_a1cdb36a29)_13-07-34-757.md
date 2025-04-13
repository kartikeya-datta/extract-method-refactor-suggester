error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2414.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2414.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[20,1]

error in qdox parser
file content:
```java
offset: 858
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2414.java
text:
```scala
abstract class XMLEventImpl implements XMLEvent {

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

import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.xerces.stax.EmptyLocation;
import org.apache.xerces.stax.ImmutableLocation;

/**
 * @xerces.internal
 * 
 * @author Lucian Holland
 *
 * @version $Id$
 */
public abstract class XMLEventImpl implements XMLEvent {

    /**
     * Constant representing the type of this event. 
     * {@see javax.xml.stream.XMLStreamConstants}
     */
    private int fEventType;
    
    /**
     * Location object for this event.
     */
    private Location fLocation;

    /**
     * Constructor.
     */
    XMLEventImpl(final int eventType, final Location location) {
        fEventType = eventType;
        if (location != null) {
            fLocation = new ImmutableLocation(location);
        }
        else {
            fLocation = EmptyLocation.getInstance();
        }
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#getEventType()
     */
    public final int getEventType() {
        return fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#getLocation()
     */
    public final Location getLocation() {
        return fLocation;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#isStartElement()
     */
    public final boolean isStartElement() {
        return START_ELEMENT == fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#isAttribute()
     */
    public final boolean isAttribute() {
        return ATTRIBUTE == fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#isNamespace()
     */
    public final boolean isNamespace() {
        return NAMESPACE == fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#isEndElement()
     */
    public final boolean isEndElement() {
        return END_ELEMENT == fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#isEntityReference()
     */
    public final boolean isEntityReference() {
        return ENTITY_REFERENCE == fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#isProcessingInstruction()
     */
    public final boolean isProcessingInstruction() {
        return PROCESSING_INSTRUCTION == fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#isCharacters()
     */
    public final boolean isCharacters() {
        return CHARACTERS == fEventType ||
            CDATA == fEventType ||
            SPACE == fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#isStartDocument()
     */
    public final boolean isStartDocument() {
        return START_DOCUMENT == fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#isEndDocument()
     */
    public final boolean isEndDocument() {
        return END_DOCUMENT == fEventType;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#asStartElement()
     */
    public final StartElement asStartElement() {
        return (StartElement) this;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#asEndElement()
     */
    public final EndElement asEndElement() {
        return (EndElement) this;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#asCharacters()
     */
    public final Characters asCharacters() {
        return (Characters) this;
    }

    /**
     * @see javax.xml.stream.events.XMLEvent#getSchemaType()
     */
    public final QName getSchemaType() {
        return null;
    }
    
    public final String toString() {
        final StringWriter writer = new StringWriter();
        try {
            writeAsEncodedUnicode(writer);
        }
        catch (XMLStreamException xse) {}
        return writer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2414.java