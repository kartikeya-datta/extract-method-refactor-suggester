error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14578.java
text:
```scala
public I@@terator<URL> getEmbeddedResourceURLs(byte[] html, URL baseUrl, URLCollection urls) throws HTMLParseException {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.jmeter.protocol.http.util.ConversionUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.tags.AppletTag;
import org.htmlparser.tags.BaseHrefTag;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.FrameTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

/**
 * HtmlParser implementation using SourceForge's HtmlParser.
 *
 */
class HtmlParserHTMLParser extends HTMLParser {
    private static final Logger log = LoggingManager.getLoggerForClass();

    static{
        org.htmlparser.scanners.ScriptScanner.STRICT = false; // Try to ensure that more javascript code is processed OK ...
    }

    protected HtmlParserHTMLParser() {
        super();
        log.info("Using htmlparser version: "+Parser.getVersion());
    }

    @Override
    protected boolean isReusable() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.protocol.http.parser.HtmlParser#getEmbeddedResourceURLs(byte[],
     *      java.net.URL)
     */
    @Override
    public Iterator getEmbeddedResourceURLs(byte[] html, URL baseUrl, URLCollection urls) throws HTMLParseException {

        if (log.isDebugEnabled()) {
            log.debug("Parsing html of: " + baseUrl);
        }

        Parser htmlParser = null;
        try {
            String contents = new String(html); // TODO - charset?
            htmlParser = new Parser();
            htmlParser.setInputHTML(contents);
        } catch (Exception e) {
            throw new HTMLParseException(e);
        }

        // Now parse the DOM tree
        try {
            // we start to iterate through the elements
            parseNodes(htmlParser.elements(), new URLPointer(baseUrl), urls);
            log.debug("End   : parseNodes");
        } catch (ParserException e) {
            throw new HTMLParseException(e);
        }

        return urls.iterator();
    }

    /*
     * A dummy class to pass the pointer of URL.
     */
    private static class URLPointer {
        private URLPointer(URL newUrl) {
            url = newUrl;
        }
        private URL url;
    }

    /**
     * Recursively parse all nodes to pick up all URL s.
     * @see e the nodes to be parsed
     * @see baseUrl Base URL from which the HTML code was obtained
     * @see urls URLCollection
     */
    private void parseNodes(final NodeIterator e,
            final URLPointer baseUrl, final URLCollection urls)
        throws HTMLParseException, ParserException {
        while(e.hasMoreNodes()) {
            Node node = e.nextNode();
            // a url is always in a Tag.
            if (!(node instanceof Tag)) {
                continue;
            }
            Tag tag = (Tag) node;
            String tagname=tag.getTagName();
            String binUrlStr = null;

            // first we check to see if body tag has a
            // background set
            if (tag instanceof BodyTag) {
                binUrlStr = tag.getAttribute(ATT_BACKGROUND);
            } else if (tag instanceof BaseHrefTag) {
                BaseHrefTag baseHref = (BaseHrefTag) tag;
                String baseref = baseHref.getBaseUrl();
                try {
                    if (!baseref.equals(""))// Bugzilla 30713
                    {
                        baseUrl.url = ConversionUtils.makeRelativeURL(baseUrl.url, baseHref.getBaseUrl());
                    }
                } catch (MalformedURLException e1) {
                    throw new HTMLParseException(e1);
                }
            } else if (tag instanceof ImageTag) {
                ImageTag image = (ImageTag) tag;
                binUrlStr = image.getImageURL();
            } else if (tag instanceof AppletTag) {
                // look for applets

                // This will only work with an Applet .class file.
                // Ideally, this should be upgraded to work with Objects (IE)
                // and archives (.jar and .zip) files as well.
                AppletTag applet = (AppletTag) tag;
                binUrlStr = applet.getAppletClass();
            } else if (tag instanceof InputTag) {
                // we check the input tag type for image
                if (ATT_IS_IMAGE.equalsIgnoreCase(tag.getAttribute(ATT_TYPE))) {
                    // then we need to download the binary
                    binUrlStr = tag.getAttribute(ATT_SRC);
                }
            } else if (tag instanceof LinkTag) {
                LinkTag link = (LinkTag) tag;
                if (link.getChild(0) instanceof ImageTag) {
                    ImageTag img = (ImageTag) link.getChild(0);
                    binUrlStr = img.getImageURL();
                }
            } else if (tag instanceof ScriptTag) {
                binUrlStr = tag.getAttribute(ATT_SRC);
            } else if (tag instanceof FrameTag) {
                binUrlStr = tag.getAttribute(ATT_SRC);
            } else if (tagname.equalsIgnoreCase(TAG_EMBED)
 tagname.equalsIgnoreCase(TAG_BGSOUND)){
                binUrlStr = tag.getAttribute(ATT_SRC);
            } else if (tagname.equalsIgnoreCase(TAG_LINK)) {
                // Putting the string first means it works even if the attribute is null
                if (STYLESHEET.equalsIgnoreCase(tag.getAttribute(ATT_REL))) {
                    binUrlStr = tag.getAttribute(ATT_HREF);
                }
            } else {
                binUrlStr = tag.getAttribute(ATT_BACKGROUND);
            }

            if (binUrlStr != null) {
                urls.addURL(binUrlStr, baseUrl.url);
            }

            // Now look for URLs in the STYLE attribute
            String styleTagStr = tag.getAttribute(ATT_STYLE);
            if(styleTagStr != null) {
                HtmlParsingUtils.extractStyleURLs(baseUrl.url, urls, styleTagStr);
            }

            // second, if the tag was a composite tag,
            // recursively parse its children.
            if (tag instanceof CompositeTag) {
                CompositeTag composite = (CompositeTag) tag;
                parseNodes(composite.elements(), baseUrl, urls);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14578.java