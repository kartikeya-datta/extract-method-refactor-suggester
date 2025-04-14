error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7129.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7129.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7129.java
text:
```scala
private static final L@@ogger log = LoggingManager.getLoggerForClass();

/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * author Justin Spears jspears@astrology.com
 * 
 * This class provides a few utility methods for dealing with XML/XPath. Might
 * think about creating an interface for the setup, but, works fine now...
 * 
 */
public class XPathUtil {
	transient private static Logger log = LoggingManager.getLoggerForClass();

	private XPathUtil() {
		super();
	}

	private static DocumentBuilderFactory documentBuilderFactory;

	/**
	 * Might
	 * 
	 * @return javax.xml.parsers.DocumentBuilderFactory
	 */
	private static synchronized DocumentBuilderFactory makeDocumentBuilderFactory(boolean validate, boolean whitespace,
			boolean namespace) {
		if (XPathUtil.documentBuilderFactory == null || documentBuilderFactory.isValidating() != validate
 documentBuilderFactory.isNamespaceAware() != namespace
 documentBuilderFactory.isIgnoringElementContentWhitespace() != whitespace) {
			// configure the document builder factory
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(validate);
			documentBuilderFactory.setNamespaceAware(namespace);
			documentBuilderFactory.setIgnoringElementContentWhitespace(whitespace);
		}
		return XPathUtil.documentBuilderFactory;
	}

	/**
	 * Create a DocumentBuilder using the makeDocumentFactory func.
	 * 
	 * @param validate
	 * @param whitespace
	 * @param namespace
	 * @return document builder
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static DocumentBuilder makeDocumentBuilder(boolean validate, boolean whitespace, boolean namespace)
			throws ParserConfigurationException, SAXException {
		DocumentBuilder builder = makeDocumentBuilderFactory(validate, whitespace, namespace).newDocumentBuilder();
		builder.setErrorHandler(new MyErrorHandler(validate, false));
		return builder;
	}

	/**
	 * Utility function to get new Document
	 * 
	 * @param stream
	 *            Document Input stream
	 * @param validate
	 *            Validate Document
	 * @param whitespace
	 *            Element Whitespace
	 * @param namespace
	 *            Is Namespace aware.
	 * @param tolerant
	 *            Is tolerant
	 * @return document
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Document makeDocument(InputStream stream, boolean validate, boolean whitespace, boolean namespace,
			boolean tolerant) throws ParserConfigurationException, SAXException, IOException {
		Document doc;
		if (tolerant) {
			doc = tidyDoc(stream);
			// doc=makeTolerantDocumentBuilder().parse(new
			// InputStreamReader(stream));
		} else {
			doc = makeDocumentBuilder(validate, whitespace, namespace).parse(stream);
		}
		return doc;
	}

	// private static HTMLDocumentBuilder makeTolerantDocumentBuilder()
	// throws ParserConfigurationException, SAXException, IOException {
	// HTMLDocumentBuilder builder = new HTMLDocumentBuilder(
	// new TolerantSaxDocumentBuilder(makeDocumentBuilder(false,false,false)
	// ));
	// return builder;
	// }

	private static Document tidyDoc(InputStream stream) {
		Document doc = null;
		doc = makeTidyParser().parseDOM(stream, null);
		doc.normalize();
		// remove the document declaration cause I think it causes
		// issues this is only needed for JDOM, since I am not
		// using it... But in case we change.
		// Node name = doc.getDoctype();
		// doc.removeChild(name);

		return doc;
	}

	private static Tidy makeTidyParser() {
		Tidy tidy = new Tidy();
		tidy.setCharEncoding(org.w3c.tidy.Configuration.UTF8);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setMakeClean(true);
		tidy.setXmlTags(false); // Input is not valid XML
		// tidy.setShowErrors(1);
		return tidy;
	}

	// Not used
	// public static Document makeDocument(InputStream stream)
	// throws ParserConfigurationException, SAXException, IOException {
	// return makeDocumentBuilder( false, false, false).parse(stream);
	// }

	static class MyErrorHandler implements ErrorHandler {
		private final boolean val, tol;

		private final String type;

		MyErrorHandler(boolean validate, boolean tolerate) {
			val = validate;
			tol = tolerate;
			type = "Val=" + val + " Tol=" + tol;
		}

		public void warning(SAXParseException ex) throws SAXException {
			log.info("Type=" + type + " " + ex);
			if (val && !tol)
				throw new SAXException(ex);
		}

		public void error(SAXParseException ex) throws SAXException {
			log.warn("Type=" + type + " " + ex);
			if (val && !tol)
				throw new SAXException(ex);
		}

		public void fatalError(SAXParseException ex) throws SAXException {
			log.error("Type=" + type + " " + ex);
			if (val && !tol)
				throw new SAXException(ex);
		}
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7129.java