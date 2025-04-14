error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10742.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10742.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 689
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10742.java
text:
```scala
abstract class AbstractStaxXMLReader extends AbstractXMLReader {

/*
 * Copyright 2002-2009 the original author or authors.
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

p@@ackage org.springframework.util.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

import org.springframework.util.StringUtils;

/**
 * Abstract base class for SAX <code>XMLReader</code> implementations that use StAX as a basis.
 *
 * @author Arjen Poutsma
 * @see #setContentHandler(org.xml.sax.ContentHandler)
 * @see #setDTDHandler(org.xml.sax.DTDHandler)
 * @see #setEntityResolver(org.xml.sax.EntityResolver)
 * @see #setErrorHandler(org.xml.sax.ErrorHandler)
 * @since 3.0
 */
abstract class AbstractStaxXmlReader extends AbstractXmlReader {

	private static final String NAMESPACES_FEATURE_NAME = "http://xml.org/sax/features/namespaces";

	private static final String NAMESPACE_PREFIXES_FEATURE_NAME = "http://xml.org/sax/features/namespace-prefixes";

	private static final String IS_STANDALONE_FEATURE_NAME = "http://xml.org/sax/features/is-standalone";

	private boolean namespacesFeature = true;

	private boolean namespacePrefixesFeature = false;

	private Boolean isStandalone;

	@Override
	public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
		if (NAMESPACES_FEATURE_NAME.equals(name)) {
			return namespacesFeature;
		}
		else if (NAMESPACE_PREFIXES_FEATURE_NAME.equals(name)) {
			return namespacePrefixesFeature;
		}
		else if (IS_STANDALONE_FEATURE_NAME.equals(name)) {
			if (isStandalone != null) {
				return isStandalone;
			}
			else {
				throw new SAXNotSupportedException("startDocument() callback not completed yet");
			}
		}
		else {
			return super.getFeature(name);
		}
	}

	@Override
	public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
		if (NAMESPACES_FEATURE_NAME.equals(name)) {
			this.namespacesFeature = value;
		}
		else if (NAMESPACE_PREFIXES_FEATURE_NAME.equals(name)) {
			this.namespacePrefixesFeature = value;
		}
		else {
			super.setFeature(name, value);
		}
	}

	/** Indicates whether the SAX feature <code>http://xml.org/sax/features/namespaces</code> is turned on. */
	protected boolean hasNamespacesFeature() {
		return namespacesFeature;
	}

	/** Indicates whether the SAX feature <code>http://xml.org/sax/features/namespaces-prefixes</code> is turned on. */
	protected boolean hasNamespacePrefixesFeature() {
		return namespacePrefixesFeature;
	}

	protected void setStandalone(boolean standalone) {
		isStandalone = (standalone) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * Parses the StAX XML reader passed at construction-time. <p/> <strong>Note</strong> that the given
	 * <code>InputSource</code> is not read, but ignored.
	 *
	 * @param ignored is ignored
	 * @throws SAXException A SAX exception, possibly wrapping a <code>XMLStreamException</code>
	 */
	public final void parse(InputSource ignored) throws SAXException {
		parse();
	}

	/**
	 * Parses the StAX XML reader passed at construction-time. <p/> <strong>Note</strong> that the given system identifier
	 * is not read, but ignored.
	 *
	 * @param ignored is ignored
	 * @throws SAXException A SAX exception, possibly wrapping a <code>XMLStreamException</code>
	 */
	public final void parse(String ignored) throws SAXException {
		parse();
	}

	private void parse() throws SAXException {
		try {
			parseInternal();
		}
		catch (XMLStreamException ex) {
			Locator locator = null;
			if (ex.getLocation() != null) {
				locator = new StaxLocator(ex.getLocation());
			}
			SAXParseException saxException = new SAXParseException(ex.getMessage(), locator, ex);
			if (getErrorHandler() != null) {
				getErrorHandler().fatalError(saxException);
			}
			else {
				throw saxException;
			}
		}
	}

	/**
	 * Sets the SAX <code>Locator</code> based on the given StAX <code>Location</code>.
	 *
	 * @param location the location
	 * @see ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	protected void setLocator(Location location) {
		if (getContentHandler() != null) {
			getContentHandler().setDocumentLocator(new StaxLocator(location));
		}
	}

	/** Template-method that parses the StAX reader passed at construction-time. */
	protected abstract void parseInternal() throws SAXException, XMLStreamException;

	/**
	 * Convert a <code>QName</code> to a qualified name, as used by DOM and SAX. The returned string has a format of
	 * <code>prefix:localName</code> if the prefix is set, or just <code>localName</code> if not.
	 *
	 * @param qName the <code>QName</code>
	 * @return the qualified name
	 */
	protected String toQualifiedName(QName qName) {
		String prefix = qName.getPrefix();
		if (!StringUtils.hasLength(prefix)) {
			return qName.getLocalPart();
		}
		else {
			return prefix + ":" + qName.getLocalPart();
		}
	}

	/**
	 * Implementation of the <code>Locator</code> interface that is based on a StAX <code>Location</code>.
	 *
	 * @see Locator
	 * @see Location
	 */
	private static class StaxLocator implements Locator {

		private Location location;

		protected StaxLocator(Location location) {
			this.location = location;
		}

		public String getPublicId() {
			return location.getPublicId();
		}

		public String getSystemId() {
			return location.getSystemId();
		}

		public int getLineNumber() {
			return location.getLineNumber();
		}

		public int getColumnNumber() {
			return location.getColumnNumber();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10742.java