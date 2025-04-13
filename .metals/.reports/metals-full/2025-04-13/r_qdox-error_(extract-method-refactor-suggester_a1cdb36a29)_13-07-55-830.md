error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8928.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8928.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8928.java
text:
```scala
private final J@@AXPNamespaceContextWrapper fNamespaceContext;

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

package org.apache.xerces.jaxp.validation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.transform.stax.StAXResult;

import org.apache.xerces.util.JAXPNamespaceContextWrapper;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

/**
 * <p>StAX stream result builder.</p>
 * 
 * @author Michael Glavassevich, IBM
 * @version $Id$
 */
final class StAXStreamResultBuilder implements StAXDocumentHandler {
    
    //
    // Data
    //

    private XMLStreamWriter fStreamWriter;
    private JAXPNamespaceContextWrapper fNamespaceContext;
    private boolean fIgnoreChars;
    private boolean fInCDATA;
    private final QName fAttrName = new QName();
    
    public StAXStreamResultBuilder(JAXPNamespaceContextWrapper context) {
        fNamespaceContext = context;
    }
    
    /*
     * StAXDocumentHandler methods
     */

    public void setStAXResult(StAXResult result) {
        fIgnoreChars = false;
        fInCDATA = false;
        fAttrName.clear();
        fStreamWriter = (result != null) ? result.getXMLStreamWriter() : null;
    }

    public void startDocument(XMLStreamReader reader) throws XMLStreamException {
        String version = reader.getVersion();
        String encoding = reader.getCharacterEncodingScheme();
        fStreamWriter.writeStartDocument(encoding != null ? encoding : "UTF-8",
                version != null ? version : "1.0");
    }

    public void endDocument(XMLStreamReader reader) throws XMLStreamException {
        fStreamWriter.writeEndDocument();
        fStreamWriter.flush();
    }

    public void comment(XMLStreamReader reader) throws XMLStreamException {
        fStreamWriter.writeComment(reader.getText());
    }

    public void processingInstruction(XMLStreamReader reader)
            throws XMLStreamException {
        String data = reader.getPIData();
        if (data != null && data.length() > 0) {
            fStreamWriter.writeProcessingInstruction(reader.getPITarget(), data);
        }
        else {
            fStreamWriter.writeProcessingInstruction(reader.getPITarget());
        }
    }

    public void entityReference(XMLStreamReader reader) throws XMLStreamException {
        fStreamWriter.writeEntityRef(reader.getLocalName());
    }

    public void startDocument(StartDocument event) throws XMLStreamException {
        String version = event.getVersion();
        String encoding = event.getCharacterEncodingScheme();
        fStreamWriter.writeStartDocument(encoding != null ? encoding : "UTF-8",
                version != null ? version : "1.0");
    }

    public void endDocument(EndDocument event) throws XMLStreamException {
        fStreamWriter.writeEndDocument();
        fStreamWriter.flush();
    }

    public void doctypeDecl(DTD event) throws XMLStreamException {
        fStreamWriter.writeDTD(event.getDocumentTypeDeclaration());
    }

    public void characters(Characters event) throws XMLStreamException {
        fStreamWriter.writeCharacters(event.getData());
    }

    public void cdata(Characters event) throws XMLStreamException {
        fStreamWriter.writeCData(event.getData());
    }

    public void comment(Comment event) throws XMLStreamException {
        fStreamWriter.writeComment(event.getText());
    }

    public void processingInstruction(ProcessingInstruction event)
            throws XMLStreamException {
        String data = event.getData();
        if (data != null && data.length() > 0) {
            fStreamWriter.writeProcessingInstruction(event.getTarget(), data);
        }
        else {
            fStreamWriter.writeProcessingInstruction(event.getTarget());
        }
    }

    public void entityReference(EntityReference event) throws XMLStreamException {
        fStreamWriter.writeEntityRef(event.getName());

    }

    public void setIgnoringCharacters(boolean ignore) {
        fIgnoreChars = ignore;
    }
    
    /*
     * XMLDocumentHandler methods
     */

    public void startDocument(XMLLocator locator, String encoding,
            NamespaceContext namespaceContext, Augmentations augs)
            throws XNIException {}

    public void xmlDecl(String version, String encoding, String standalone,
            Augmentations augs) throws XNIException {}

    public void doctypeDecl(String rootElement, String publicId,
            String systemId, Augmentations augs) throws XNIException {}

    public void comment(XMLString text, Augmentations augs) throws XNIException {}

    public void processingInstruction(String target, XMLString data,
            Augmentations augs) throws XNIException {}

    public void startElement(QName element, XMLAttributes attributes,
            Augmentations augs) throws XNIException {
        try {
            if (element.prefix.length() > 0) {
                fStreamWriter.writeStartElement(element.prefix, 
                        element.localpart, element.uri != null ? element.uri : "");
            }
            else if (element.uri != null){
                fStreamWriter.writeStartElement(element.uri, element.localpart);
            }
            else {
                fStreamWriter.writeStartElement(element.localpart);
            }
            int size = fNamespaceContext.getDeclaredPrefixCount();
            final javax.xml.namespace.NamespaceContext nc = fNamespaceContext.getNamespaceContext();
            for (int i = 0; i < size; ++i) {
                String prefix = fNamespaceContext.getDeclaredPrefixAt(i);
                String uri = nc.getNamespaceURI(prefix);
                if (prefix.length() == 0) {
                    fStreamWriter.writeDefaultNamespace(uri != null ? uri : "");
                }
                else {
                    fStreamWriter.writeNamespace(prefix, uri != null ? uri : "");
                }
            }
            size = attributes.getLength();
            for (int i = 0; i < size; ++i) {
                attributes.getName(i, fAttrName);
                if (fAttrName.prefix.length() > 0) {
                    fStreamWriter.writeAttribute(fAttrName.prefix, 
                            fAttrName.uri != null ? fAttrName.uri : "", 
                            fAttrName.localpart, attributes.getValue(i));
                }
                else if (fAttrName.uri != null) {
                    fStreamWriter.writeAttribute(fAttrName.uri, 
                            fAttrName.localpart, attributes.getValue(i));
                }
                else {
                    fStreamWriter.writeAttribute(fAttrName.localpart, attributes.getValue(i));
                }
            }
        }
        catch (XMLStreamException e) {
            throw new XNIException(e);
        }
    }

    public void emptyElement(QName element, XMLAttributes attributes,
            Augmentations augs) throws XNIException {
        startElement(element, attributes, augs);
        endElement(element, augs);
    }

    public void startGeneralEntity(String name,
            XMLResourceIdentifier identifier, String encoding,
            Augmentations augs) throws XNIException {}

    public void textDecl(String version, String encoding, Augmentations augs)
            throws XNIException {}

    public void endGeneralEntity(String name, Augmentations augs)
            throws XNIException {}

    public void characters(XMLString text, Augmentations augs)
        throws XNIException {
        if (!fIgnoreChars) {
            try {
                if (!fInCDATA) {
                    fStreamWriter.writeCharacters(text.ch, text.offset, text.length);
                }
                else {
                    fStreamWriter.writeCData(text.toString());
                }
            }
            catch (XMLStreamException e) {
                throw new XNIException(e);
            }
        }
    }

    public void ignorableWhitespace(XMLString text, Augmentations augs)
            throws XNIException {
        characters(text, augs);
    }

    public void endElement(QName element, Augmentations augs)
            throws XNIException {
        try {
            fStreamWriter.writeEndElement();
        }
        catch (XMLStreamException e) {
            throw new XNIException(e);
        }
    }

    public void startCDATA(Augmentations augs) throws XNIException {
        fInCDATA = true;
    }

    public void endCDATA(Augmentations augs) throws XNIException {
        fInCDATA = false;
    }

    public void endDocument(Augmentations augs) throws XNIException {}

    public void setDocumentSource(XMLDocumentSource source) {}

    public XMLDocumentSource getDocumentSource() {
        return null;
    }

} // StAXStreamResultBuilder
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8928.java