error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2194.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2194.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2194.java
text:
```scala
private final Q@@Name fAttributeQName = new QName();

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

package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;

/**
 * The DTD validator. The validator implements a document
 * filter: receiving document events from the scanner; validating
 * the content and structure; augmenting the InfoSet, if applicable;
 * and notifying the parser of the information resulting from the
 * validation process.
 * <p> Formerly, this component also handled DTD events and grammar construction.
 * To facilitate the development of a meaningful DTD grammar caching/preparsing
 * framework, this functionality has been moved into the XMLDTDLoader
 * class.  Therefore, this class no longer implements the DTDFilter
 * or DTDContentModelFilter interfaces.
 * <p>
 * This component requires the following features and properties from the
 * component manager that uses it:
 * <ul>
 *  <li>http://xml.org/sax/features/namespaces</li>
 *  <li>http://xml.org/sax/features/validation</li>
 *  <li>http://apache.org/xml/features/validation/dynamic</li>
 *  <li>http://apache.org/xml/properties/internal/symbol-table</li>
 *  <li>http://apache.org/xml/properties/internal/error-reporter</li>
 *  <li>http://apache.org/xml/properties/internal/grammar-pool</li>
 *  <li>http://apache.org/xml/properties/internal/datatype-validator-factory</li>
 * </ul>
 * 
 * @xerces.internal
 *
 * @author Elena Litani, IBM
 * @author Michael Glavassevich, IBM
 *
 * @version $Id$
 */
public class XML11NSDTDValidator extends XML11DTDValidator {

    /** Attribute QName. */
    private QName fAttributeQName = new QName();

    /** Bind namespaces */
    protected final void startNamespaceScope(QName element, XMLAttributes attributes, Augmentations augs)
        throws XNIException {

        // add new namespace context
        fNamespaceContext.pushContext();

        if (element.prefix == XMLSymbols.PREFIX_XMLNS) {
            fErrorReporter.reportError(
                XMLMessageFormatter.XMLNS_DOMAIN,
                "ElementXMLNSPrefix",
                new Object[] { element.rawname },
                XMLErrorReporter.SEVERITY_FATAL_ERROR);
        }

        // search for new namespace bindings
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            String localpart = attributes.getLocalName(i);
            String prefix = attributes.getPrefix(i);
            // when it's of form xmlns="..." or xmlns:prefix="...",
            // it's a namespace declaration. but prefix:xmlns="..." isn't.
            if (prefix == XMLSymbols.PREFIX_XMLNS || prefix == XMLSymbols.EMPTY_STRING
                && localpart == XMLSymbols.PREFIX_XMLNS) {

                // get the internalized value of this attribute
                String uri = fSymbolTable.addSymbol(attributes.getValue(i));

                // 1. "xmlns" can't be bound to any namespace
                if (prefix == XMLSymbols.PREFIX_XMLNS && localpart == XMLSymbols.PREFIX_XMLNS) {
                    fErrorReporter.reportError(
                        XMLMessageFormatter.XMLNS_DOMAIN,
                        "CantBindXMLNS",
                        new Object[] { attributes.getQName(i)},
                        XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }

                // 2. the namespace for "xmlns" can't be bound to any prefix
                if (uri == NamespaceContext.XMLNS_URI) {
                    fErrorReporter.reportError(
                        XMLMessageFormatter.XMLNS_DOMAIN,
                        "CantBindXMLNS",
                        new Object[] { attributes.getQName(i)},
                        XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }

                // 3. "xml" can't be bound to any other namespace than it's own
                if (localpart == XMLSymbols.PREFIX_XML) {
                    if (uri != NamespaceContext.XML_URI) {
                        fErrorReporter.reportError(
                            XMLMessageFormatter.XMLNS_DOMAIN,
                            "CantBindXML",
                            new Object[] { attributes.getQName(i)},
                            XMLErrorReporter.SEVERITY_FATAL_ERROR);
                    }
                }
                // 4. the namespace for "xml" can't be bound to any other prefix
                else {
                    if (uri == NamespaceContext.XML_URI) {
                        fErrorReporter.reportError(
                            XMLMessageFormatter.XMLNS_DOMAIN,
                            "CantBindXML",
                            new Object[] { attributes.getQName(i)},
                            XMLErrorReporter.SEVERITY_FATAL_ERROR);
                    }
                }

                prefix = localpart != XMLSymbols.PREFIX_XMLNS ? localpart : XMLSymbols.EMPTY_STRING;

				// Declare prefix in context. Removing the association between a prefix and a 
				// namespace name is permitted in XML 1.1, so if the uri value is the empty string, 
				// the prefix is being unbound. -- mrglavas
                fNamespaceContext.declarePrefix(prefix, uri.length() != 0 ? uri : null);
            }
        }

        // bind the element
        String prefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
        element.uri = fNamespaceContext.getURI(prefix);
        if (element.prefix == null && element.uri != null) {
            element.prefix = XMLSymbols.EMPTY_STRING;
        }
        if (element.prefix != null && element.uri == null) {
            fErrorReporter.reportError(
                XMLMessageFormatter.XMLNS_DOMAIN,
                "ElementPrefixUnbound",
                new Object[] { element.prefix, element.rawname },
                XMLErrorReporter.SEVERITY_FATAL_ERROR);
        }

        // bind the attributes
        for (int i = 0; i < length; i++) {
            attributes.getName(i, fAttributeQName);
            String aprefix = fAttributeQName.prefix != null ? fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            String arawname = fAttributeQName.rawname;
            if (arawname == XMLSymbols.PREFIX_XMLNS) {
                fAttributeQName.uri = fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
                attributes.setName(i, fAttributeQName);
            } else if (aprefix != XMLSymbols.EMPTY_STRING) {
                fAttributeQName.uri = fNamespaceContext.getURI(aprefix);
                if (fAttributeQName.uri == null) {
                    fErrorReporter.reportError(
                        XMLMessageFormatter.XMLNS_DOMAIN,
                        "AttributePrefixUnbound",
                        new Object[] { element.rawname, arawname, aprefix },
                        XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
                attributes.setName(i, fAttributeQName);
            }
        }

        // verify that duplicate attributes don't exist
        // Example: <foo xmlns:a='NS' xmlns:b='NS' a:attr='v1' b:attr='v2'/>
        int attrCount = attributes.getLength();
        for (int i = 0; i < attrCount - 1; i++) {
            String auri = attributes.getURI(i);
            if (auri == null || auri == NamespaceContext.XMLNS_URI) {
                continue;
            }
            String alocalpart = attributes.getLocalName(i);
            for (int j = i + 1; j < attrCount; j++) {
                String blocalpart = attributes.getLocalName(j);
                String buri = attributes.getURI(j);
                if (alocalpart == blocalpart && auri == buri) {
                    fErrorReporter.reportError(
                        XMLMessageFormatter.XMLNS_DOMAIN,
                        "AttributeNSNotUnique",
                        new Object[] { element.rawname, alocalpart, auri },
                        XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
            }
        }

    } // startNamespaceScope(QName,XMLAttributes)

    /** Handles end element. */
    protected void endNamespaceScope(QName element, Augmentations augs, boolean isEmpty)
        throws XNIException {

        // bind element
        String eprefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
        element.uri = fNamespaceContext.getURI(eprefix);
        if (element.uri != null) {
            element.prefix = eprefix;
        }

        // call handlers
        if (fDocumentHandler != null) {
            if (!isEmpty) {
                fDocumentHandler.endElement(element, augs);
            }
        }

        // pop context
        fNamespaceContext.popContext();

    } // endNamespaceScope(QName,boolean)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2194.java