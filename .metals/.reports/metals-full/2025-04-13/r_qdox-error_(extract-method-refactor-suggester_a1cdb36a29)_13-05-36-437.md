error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10861.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10861.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10861.java
text:
```scala
b@@aseUrl = new URL(baseUrl, baseref);

/*
 * Copyright 2003-2005 The Apache Software Foundation.
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

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import org.htmlparser.Node;
import org.htmlparser.NodeReader;
import org.htmlparser.Parser;
import org.htmlparser.scanners.AppletScanner;
import org.htmlparser.scanners.BaseHrefScanner;
import org.htmlparser.scanners.BgSoundScanner;
import org.htmlparser.scanners.BodyScanner;
import org.htmlparser.scanners.FrameScanner;
import org.htmlparser.scanners.InputTagScanner;
import org.htmlparser.scanners.LinkScanner;
import org.htmlparser.scanners.LinkTagScanner;
import org.htmlparser.scanners.ScriptScanner;
import org.htmlparser.tags.AppletTag;
import org.htmlparser.tags.BaseHrefTag;
import org.htmlparser.tags.BgSoundTag;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.FrameTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.LinkTagTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.Tag;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

/**
 * HtmlParser implementation using SourceForge's HtmlParser.
 * 
 * @version $Revision$ updated on $Date$
 */
class HtmlParserHTMLParser extends HTMLParser {
    /** Used to store the Logger (used for debug and error messages). */
	private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String ATT_HREF = "href"; // $NON-NLS-1$
    private static final String STYLESHEET = "stylesheet"; // $NON-NLS-1$
    private static final String ATT_REL = "rel"; // $NON-NLS-1$
    private static final String ATT_SRC = "src"; // $NON-NLS-1$
    private static final String ATT_IS_IMAGE = "image"; // $NON-NLS-1$
    private static final String ATT_TYPE = "type"; // $NON-NLS-1$

    protected HtmlParserHTMLParser() {
		super();
	}

	protected boolean isReusable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.protocol.http.parser.HtmlParser#getEmbeddedResourceURLs(byte[],
	 *      java.net.URL)
	 */
	public Iterator getEmbeddedResourceURLs(byte[] html, URL baseUrl, URLCollection urls) throws HTMLParseException {
		Parser htmlParser = null;
		try {
			String contents = new String(html);
			StringReader reader = new StringReader(contents);
			NodeReader nreader = new NodeReader(reader, contents.length());
			htmlParser = new Parser(nreader, new DefaultParserFeedback());
			addTagListeners(htmlParser);
		} catch (Exception e) {
			throw new HTMLParseException(e);
		}

		// Now parse the DOM tree

		// look for applets

		// This will only work with an Applet .class file.
		// Ideally, this should be upgraded to work with Objects (IE)
		// and archives (.jar and .zip) files as well.

		try {
			// we start to iterate through the elements
			for (NodeIterator e = htmlParser.elements(); e.hasMoreNodes();) {
				Node node = e.nextNode();
				String binUrlStr = null;

				// first we check to see if body tag has a
				// background set and we set the NodeIterator
				// to the child elements inside the body
				if (node instanceof BodyTag) {
					BodyTag body = (BodyTag) node;
					binUrlStr = body.getAttribute("background");
					// if the body tag exists, we get the elements
					// within the body tag. if we don't we won't
					// see the body of the page. The only catch
					// with this is if there are images after the
					// closing body tag, it won't get parsed. If
					// someone puts it outside the body tag, it
					// is probably a mistake. Plus it's bad to
					// have important content after the closing
					// body tag. Peter Lin 10-9-03
					e = body.elements();
				} else if (node instanceof BaseHrefTag) {
					BaseHrefTag baseHref = (BaseHrefTag) node;
					String baseref = baseHref.getBaseUrl();
					try {
						if (!baseref.equals(""))// Bugzilla 30713 // $NON-NLS-1$
						{
							baseUrl = new URL(baseUrl, baseHref.getBaseUrl() + "/"); // $NON-NLS-1$
						}
					} catch (MalformedURLException e1) {
						throw new HTMLParseException(e1);
					}
				} else if (node instanceof ImageTag) {
					ImageTag image = (ImageTag) node;
					binUrlStr = image.getImageURL();
				} else if (node instanceof AppletTag) {
					AppletTag applet = (AppletTag) node;
					binUrlStr = applet.getAppletClass();
				} else if (node instanceof InputTag) {
					InputTag input = (InputTag) node;
					// we check the input tag type for image
					String strType = input.getAttribute(ATT_TYPE);
					if (strType != null && strType.equalsIgnoreCase(ATT_IS_IMAGE)) {
						// then we need to download the binary
						binUrlStr = input.getAttribute(ATT_SRC);
					}
				} else if (node instanceof LinkTag) {
					LinkTag link = (LinkTag) node;
					if (link.getChild(0) instanceof ImageTag) {
						ImageTag img = (ImageTag) link.getChild(0);
						binUrlStr = img.getImageURL();
					}
				} else if (node instanceof ScriptTag) {
					ScriptTag script = (ScriptTag) node;
					binUrlStr = script.getAttribute(ATT_SRC);
				} else if (node instanceof FrameTag) {
					FrameTag tag = (FrameTag) node;
					binUrlStr = tag.getAttribute(ATT_SRC);
				} else if (node instanceof LinkTagTag) {
					LinkTagTag script = (LinkTagTag) node;
					if (script.getAttribute(ATT_REL).equalsIgnoreCase(STYLESHEET)) {
						binUrlStr = script.getAttribute(ATT_HREF);
					}
				} else if (node instanceof FrameTag) {
					FrameTag script = (FrameTag) node;
					binUrlStr = script.getAttribute(ATT_SRC);
				} else if (node instanceof BgSoundTag) {
					BgSoundTag script = (BgSoundTag) node;
					binUrlStr = script.getAttribute(ATT_SRC);
                } else if (node instanceof Tag) {
                    Tag tag = (Tag) node;
                    String tagname=tag.getTagName();
                    if (tagname.equalsIgnoreCase("EMBED")){
                        binUrlStr = tag.getAttribute(ATT_SRC);  
                    }
                }

				if (binUrlStr == null) {
					continue;
				}

				urls.addURL(binUrlStr, baseUrl);
			}
			log.debug("End   : parseNodes");
		} catch (ParserException e) {
			throw new HTMLParseException(e);
		}

		return urls.iterator();
	}

	/**
	 * Returns a node representing a whole xml given an xml document.
	 * 
	 * @param text
	 *            an xml document
	 * @return a node representing a whole xml
	 * 
	 * @throws SAXException
	 *             indicates an error parsing the xml document
	 */
	private static void addTagListeners(Parser parser) {
		log.debug("Start : addTagListeners");
		// add body tag scanner
		parser.addScanner(new BodyScanner());
		// add BaseHRefTag scanner
		parser.addScanner(new BaseHrefScanner());
		// add ImageTag and BaseHrefTag scanners
		LinkScanner linkScanner = new LinkScanner(LinkTag.LINK_TAG_FILTER);
		// parser.addScanner(linkScanner);
		parser.addScanner(linkScanner.createImageScanner(ImageTag.IMAGE_TAG_FILTER));
		parser.addScanner(linkScanner.createBaseHREFScanner("-b")); // $NON-NLS-1$
		// Taken from org.htmlparser.Parser
		// add input tag scanner
		parser.addScanner(new InputTagScanner());
		// add applet tag scanner
		parser.addScanner(new AppletScanner());
		parser.addScanner(new ScriptScanner());
		parser.addScanner(new LinkTagScanner());
		parser.addScanner(new FrameScanner());
		parser.addScanner(new BgSoundScanner());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10861.java