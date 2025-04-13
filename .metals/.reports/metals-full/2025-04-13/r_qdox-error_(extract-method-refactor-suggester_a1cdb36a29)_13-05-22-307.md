error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10564.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10564.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10564.java
text:
```scala
i@@f (properties != null && properties.size() > 0) {

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

package org.jboss.as.logging;

import java.util.Arrays;
import java.util.List;
import org.jboss.as.model.AbstractModelRootElement;
import org.jboss.as.model.PropertiesElement;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public abstract class AbstractHandlerElement<E extends AbstractHandlerElement<E>> extends AbstractModelRootElement<E> {

    private static final long serialVersionUID = -8326499785021909600L;

    private static final String[] NONE = new String[0];

    private final String name;
    private String[] subhandlers = NONE;
    private String encoding;
    private Boolean autoflush;
    private String levelName;
    private AbstractFormatterElement<?> formatter;
    private PropertiesElement properties;

    protected AbstractHandlerElement(final String name, final QName elementName) {
        super(elementName);
        this.name = name;
    }

    public List<String> getSubhandlers() {
        return Arrays.asList(subhandlers.clone());
    }

    public String getName() {
        return name;
    }

    public String getEncoding() {
        return encoding;
    }

    public Boolean getAutoflush() {
        return autoflush;
    }

    public String getLevelName() {
        return levelName;
    }

    public AbstractFormatterElement<?> getFormatter() {
        return formatter;
    }

    public PropertiesElement getProperties() {
        return properties;
    }

    @Override
    public void writeContent(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        // Attributes
        writeAttributes(streamWriter);
        // Elements
        writeElements(streamWriter);

        streamWriter.writeEndElement();
    }

    protected void writeElements(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        if (levelName != null) {
            streamWriter.writeEmptyElement("level");
            streamWriter.writeAttribute("name", levelName);
        }
        if (formatter != null) {
            streamWriter.writeStartElement("formatter");
            final QName elementName = formatter.getElementName();
            streamWriter.writeStartElement(elementName.getNamespaceURI(), elementName.getLocalPart());
            formatter.writeContent(streamWriter);
            streamWriter.writeEndElement();
        }
        if (subhandlers != null && subhandlers.length > 0) {
            streamWriter.writeStartElement("subhandlers");
            for (String name : subhandlers) {
                streamWriter.writeEmptyElement("handler");
                streamWriter.writeAttribute("name", name);
            }
            streamWriter.writeEndElement();
        }
        if (properties != null) {
            streamWriter.writeStartElement("properties");
            properties.writeContent(streamWriter);
        }
    }

    protected void writeAttributes(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        if (name != null) {
            streamWriter.writeAttribute("name", name);
        }
        if (autoflush != null) {
            streamWriter.writeAttribute("autoflush", autoflush.toString());
        }
        if (encoding != null) {
            streamWriter.writeAttribute("encoding", encoding.toString());
        }
    }

    void setLevelName(final String levelName) {
        this.levelName = levelName;
    }

    void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    void setAutoflush(final Boolean autoflush) {
        this.autoflush = autoflush;
    }

    void setFormatter(final AbstractFormatterElement<?> formatter) {
        this.formatter = formatter;
    }

    void setProperties(final PropertiesElement properties) {
        this.properties = properties;
    }

    void setSubhandlers(final String[] subhandlers) {
        this.subhandlers = subhandlers.clone();
    }

    AbstractHandlerAdd getAdd() {
        final AbstractHandlerAdd add = createAdd(name);
        add.setLevelName(levelName);
        add.setAutoflush(autoflush);
        add.setEncoding(encoding);
        add.setFormatter(formatter.getSpecification());
        add.setSubhandlers(subhandlers.clone());
        return add;
    }

    abstract AbstractHandlerAdd createAdd(String name);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10564.java