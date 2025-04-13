error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7944.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7944.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,4]

error in qdox parser
file content:
```java
offset: 4
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7944.java
text:
```scala
 n@@amespaceURI.equals(LOG4J_NS) || namespaceURI.equals(LS_NS)) {

/*
 * Copyright 1999,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j.joran.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Constants;
import org.apache.log4j.spi.ErrorItem;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;

import java.io.ByteArrayInputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Collects all configuration significant elements from
 * an XML parse.
 *
 * @author Curt Arnold
 */
public final class JoranDocument extends DefaultHandler {
  public static final String LOG4J_NS = "http://jakarta.apache.org/log4j/";
  public static final String LS_NS = "http://logging.apache.org/";
  private final List errorList;
  private final List events = new ArrayList(20);
  private SAXParseException fatalError;
  private Locator location;

  public JoranDocument(final List errorList) {
    this.errorList = errorList;
  }

  public void error(final SAXParseException spe) {
    errorReport(spe);
  }

  public void fatalError(final SAXParseException spe) {
    if (fatalError == null) {
      fatalError = spe;
    }
    errorReport(spe);
  }

  public void warning(final SAXParseException spe) {
    errorReport(spe);
  }

  private void errorReport(final SAXParseException spe) {
    int line = spe.getLineNumber();
    ErrorItem errorItem = new ErrorItem("Parsing warning", spe);
    errorItem.setLineNumber(line);
    errorItem.setColNumber(spe.getColumnNumber());
    errorList.add(errorItem);
  }

  public void startElement(
    final String namespaceURI, final String localName, final String qName,
    final Attributes attributes) {
    if (
      (namespaceURI == null) || (namespaceURI.length() == 0)
 namespaceURI.equals(LOG4J_NS) || namespaceURI.equals(LS_NS)) {
      events.add(new StartElementEvent(localName, location, attributes));
    }
  }

  public void endElement(
    final String namespaceURI, final String localName, final String qName) {
    if (
      (namespaceURI == null) || (namespaceURI.length() == 0)
 namespaceURI.equals(LOG4J_NS)) {
      events.add(new EndElementEvent(localName, location));
    }
  }

  public void replay(final ContentHandler handler) throws SAXException {
    if (fatalError != null) {
      throw fatalError;
    }
    LocatorImpl replayLocation = new LocatorImpl();
    handler.setDocumentLocator(replayLocation);
    for (Iterator iter = events.iterator(); iter.hasNext();) {
      ElementEvent event = (ElementEvent) iter.next();
      event.replay(handler, replayLocation);
    }
  }

  public InputSource resolveEntity(
    final String publicId, final String systemId) throws SAXException {
    //
    //   if log4j.dtd is requested then
    //       return an empty input source.
    //   We aren't validating and do not need anything from
    //       the dtd and do not want a failure if it isn't present.
    if ((systemId != null) && systemId.endsWith("log4j.dtd")) {
       Logger logger = LogManager.getLogger(this.getClass().getName());
       logger.warn("The 'log4j.dtd' is no longer used nor needed.");
       logger.warn("See {}#log4j_dtd for more details.", Constants.CODES_HREF);
      return new InputSource(new ByteArrayInputStream(new byte[0]));
    }
    return super.resolveEntity(publicId, systemId);
  }

  public void setDocumentLocator(Locator location) {
    this.location = location;
  }

  private abstract static class ElementEvent {
    private String localName;
    private Locator location;

    ElementEvent(final String localName, final Locator location) {
      this.localName = localName;
      if (location != null) {
        this.location = new LocatorImpl(location);
      }
    }

    public final String getLocalName() {
      return localName;
    }

    public void replay(
      final ContentHandler handler, final LocatorImpl replayLocation)
      throws SAXException {
      if (location != null) {
        replayLocation.setPublicId(location.getPublicId());
        replayLocation.setColumnNumber(location.getColumnNumber());
        replayLocation.setLineNumber(location.getLineNumber());
        replayLocation.setSystemId(location.getSystemId());
      }
    }
  }

  private static class EndElementEvent extends ElementEvent {
    public EndElementEvent(final String localName, final Locator location) {
      super(localName, location);
    }

    public void replay(
      final ContentHandler handler, final LocatorImpl replayLocation)
      throws SAXException {
      super.replay(handler, replayLocation);
      handler.endElement(
        JoranDocument.LOG4J_NS, getLocalName(), getLocalName());
    }
  }

  private static class StartElementEvent extends ElementEvent {
    private Attributes attributes;

    public StartElementEvent(
      final String localName, final Locator location,
      final Attributes attributes) {
      super(localName, location);
      this.attributes = new AttributesImpl(attributes);
    }

    public void replay(
      final ContentHandler handler, final LocatorImpl replayLocation)
      throws SAXException {
      super.replay(handler, replayLocation);
      handler.startElement(
        JoranDocument.LOG4J_NS, getLocalName(), getLocalName(), attributes);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7944.java