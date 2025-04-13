error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/673.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/673.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/673.java
text:
```scala
i@@f (name.equalsIgnoreCase("img") || name.equalsIgnoreCase("embed")) {

// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jmeter.protocol.http.parser;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;

/**
 * HtmlParser implementation using JTidy.
 * 
 * @version $Revision$ updated on $Date$
 */
class JTidyHTMLParser extends HTMLParser {
	/** Used to store the Logger (used for debug and error messages). */
	transient private static Logger log = LoggingManager.getLoggerForClass();

	protected JTidyHTMLParser() {
		super();
	}

	protected boolean isReusable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.protocol.http.parser.HTMLParser#getEmbeddedResourceURLs(byte[],
	 *      java.net.URL)
	 */
	public Iterator getEmbeddedResourceURLs(byte[] html, URL baseUrl, URLCollection urls) throws HTMLParseException {
		Document dom = null;
		try {
			dom = (Document) getDOM(html);
		} catch (SAXException se) {
			throw new HTMLParseException(se);
		}

		// Now parse the DOM tree

		scanNodes(dom, urls, baseUrl);

		return urls.iterator();
	}

	/**
	 * Scan nodes recursively, looking for embedded resources
	 * 
	 * @param node -
	 *            initial node
	 * @param urls -
	 *            container for URLs
	 * @param baseUrl -
	 *            used to create absolute URLs
	 * 
	 * @return new base URL
	 */
	private URL scanNodes(Node node, URLCollection urls, URL baseUrl) throws HTMLParseException {
		if (node == null) {
			return baseUrl;
		}

		String name = node.getNodeName();

		int type = node.getNodeType();

		switch (type) {

		case Node.DOCUMENT_NODE:
			scanNodes(((Document) node).getDocumentElement(), urls, baseUrl);
			break;

		case Node.ELEMENT_NODE:

			NamedNodeMap attrs = node.getAttributes();
			if (name.equalsIgnoreCase("base")) {
				String tmp = getValue(attrs, "href");
				if (tmp != null)
					try {
						baseUrl = new URL(baseUrl, tmp);
					} catch (MalformedURLException e) {
						throw new HTMLParseException(e);
					}
				break;
			}

			if (name.equalsIgnoreCase("img")) {
				urls.addURL(getValue(attrs, "src"), baseUrl);
				break;
			}

			if (name.equalsIgnoreCase("applet")) {
				urls.addURL(getValue(attrs, "code"), baseUrl);
				break;
			}
			if (name.equalsIgnoreCase("input")) {
				String src = getValue(attrs, "src");
				String typ = getValue(attrs, "type");
				if ((src != null) && (typ.equalsIgnoreCase("image"))) {
					urls.addURL(src, baseUrl);
				}
				break;
			}
			if (name.equalsIgnoreCase("link") && getValue(attrs, "rel").equalsIgnoreCase("stylesheet")) {
				urls.addURL(getValue(attrs, "href"), baseUrl);
				break;
			}
			if (name.equalsIgnoreCase("script")) {
				urls.addURL(getValue(attrs, "src"), baseUrl);
				break;
			}
			if (name.equalsIgnoreCase("frame")) {
				urls.addURL(getValue(attrs, "src"), baseUrl);
				break;
			}
			String back = getValue(attrs, "background");
			if (back != null) {
				urls.addURL(back, baseUrl);
				break;
			}
			if (name.equalsIgnoreCase("bgsound")) {
				urls.addURL(getValue(attrs, "src"), baseUrl);
				break;
			}
			if (name.equalsIgnoreCase("frame")) {
				urls.addURL(getValue(attrs, "src"), baseUrl);
				break;
			}

			NodeList children = node.getChildNodes();
			if (children != null) {
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					baseUrl = scanNodes(children.item(i), urls, baseUrl);
				}
			}
			break;

		// case Node.TEXT_NODE:
		// break;

		}

		return baseUrl;

	}

	/*
	 * Helper method to get an attribute value, if it exists @param attrs list
	 * of attributs @param attname attribute name @return
	 */
	private String getValue(NamedNodeMap attrs, String attname) {
		String v = null;
		Node n = attrs.getNamedItem(attname);
		if (n != null)
			v = n.getNodeValue();
		return v;
	}

	/**
	 * Returns <code>tidy</code> as HTML parser.
	 * 
	 * @return a <code>tidy</code> HTML parser
	 */
	private static Tidy getTidyParser() {
		log.debug("Start : getParser");
		Tidy tidy = new Tidy();
		tidy.setCharEncoding(org.w3c.tidy.Configuration.UTF8);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		if (log.isDebugEnabled()) {
			log.debug("getParser : tidy parser created - " + tidy);
		}
		log.debug("End   : getParser");
		return tidy;
	}

	/**
	 * Returns a node representing a whole xml given an xml document.
	 * 
	 * @param text
	 *            an xml document (as a byte array)
	 * @return a node representing a whole xml
	 * 
	 * @throws SAXException
	 *             indicates an error parsing the xml document
	 */
	private static Node getDOM(byte[] text) throws SAXException {
		log.debug("Start : getDOM");
		Node node = getTidyParser().parseDOM(new ByteArrayInputStream(text), null);
		if (log.isDebugEnabled()) {
			log.debug("node : " + node);
		}
		log.debug("End   : getDOM");
		return node;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/673.java