error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4384.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4384.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4384.java
text:
```scala
S@@tringBuilder buf = new StringBuilder();

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

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerFactory;
import org.apache.jmeter.protocol.http.util.ConversionUtils;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.oro.text.PatternCacheLRU;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

// For Junit tests @see TestHtmlParsingUtils

public final class HtmlParsingUtils {
    private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     * Private constructor to prevent instantiation.
     */
    private HtmlParsingUtils() {
    }

    /**
     * Check if anchor matches by checking against:
     * - protocol
     * - domain
     * - path
     * - parameter names
     *
     * @param newLink target to match
     * @param config pattern to match against
     *
     * @return true if target URL matches pattern URL
     */
    public static boolean isAnchorMatched(HTTPSamplerBase newLink, HTTPSamplerBase config)
    {
        String query = null;
        try {
            query = URLDecoder.decode(newLink.getQueryString(), "UTF-8"); // $NON-NLS-1$
        } catch (UnsupportedEncodingException e) {
            // UTF-8 unsupported? You must be joking!
            log.error("UTF-8 encoding not supported!");
            throw new Error("Should not happen: " + e.toString());
        }

        final Arguments arguments = config.getArguments();
        if (query == null && arguments.getArgumentCount() > 0) {
            return false;// failed to convert query, so assume no match
        }

        final Perl5Matcher matcher = JMeterUtils.getMatcher();
        final PatternCacheLRU patternCache = JMeterUtils.getPatternCache();

        if (!isEqualOrMatches(newLink.getProtocol(), config.getProtocol(), matcher, patternCache)){
            return false;
        }

        final String domain = config.getDomain();
        if (domain != null && domain.length() > 0) {
            if (!isEqualOrMatches(newLink.getDomain(), domain, matcher, patternCache)){
                return false;
            }
        }

        final String path = config.getPath();
        if (!newLink.getPath().equals(path)
                && !matcher.matches(newLink.getPath(), patternCache.getPattern("[/]*" + path, // $NON-NLS-1$
                        Perl5Compiler.READ_ONLY_MASK))) {
            return false;
        }

        PropertyIterator iter = arguments.iterator();
        while (iter.hasNext()) {
            Argument item = (Argument) iter.next().getObjectValue();
            final String name = item.getName();
            if (query.indexOf(name + "=") == -1) { // $NON-NLS-1$
                if (!(matcher.contains(query, patternCache.getPattern(name, Perl5Compiler.READ_ONLY_MASK)))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Arguments match if the input name matches the corresponding pattern name
     * and the input value matches the pattern value, where the matching is done
     * first using String equals, and then Regular Expression matching if the equals test fails.
     *
     * @param arg - input Argument
     * @param patternArg - pattern to match against
     * @return true if both name and value match
     */
    public static boolean isArgumentMatched(Argument arg, Argument patternArg) {
        final Perl5Matcher matcher = JMeterUtils.getMatcher();
        final PatternCacheLRU patternCache = JMeterUtils.getPatternCache();
        return
            isEqualOrMatches(arg.getName(), patternArg.getName(), matcher, patternCache)
        &&
            isEqualOrMatches(arg.getValue(), patternArg.getValue(), matcher, patternCache);
    }

    /**
     * Match the input argument against the pattern using String.equals() or pattern matching if that fails.
     *
     * @param arg input string
     * @param pat pattern string
     * @param matcher Perl5Matcher
     * @param cache PatternCache
     *
     * @return true if input matches the pattern
     */
    public static boolean isEqualOrMatches(String arg, String pat, Perl5Matcher matcher, PatternCacheLRU cache){
        return
            arg.equals(pat)

            matcher.matches(arg,cache.getPattern(pat,Perl5Compiler.READ_ONLY_MASK));
    }

    /**
     * Match the input argument against the pattern using String.equals() or pattern matching if that fails
     * using case-insenssitive matching.
     *
     * @param arg input string
     * @param pat pattern string
     * @param matcher Perl5Matcher
     * @param cache PatternCache
     *
     * @return true if input matches the pattern
     */
    public static boolean isEqualOrMatchesCaseBlind(String arg, String pat, Perl5Matcher matcher, PatternCacheLRU cache){
        return
            arg.equalsIgnoreCase(pat)

            matcher.matches(arg,cache.getPattern(pat,Perl5Compiler.READ_ONLY_MASK | Perl5Compiler.CASE_INSENSITIVE_MASK));
    }

    /**
     * Match the input argument against the pattern using String.equals() or pattern matching if that fails
     * using case-insensitive matching.
     *
     * @param arg input string
     * @param pat pattern string
     *
     * @return true if input matches the pattern
     */
    public static boolean isEqualOrMatches(String arg, String pat){
        return isEqualOrMatches(arg, pat, JMeterUtils.getMatcher(), JMeterUtils.getPatternCache());
    }

    /**
     * Match the input argument against the pattern using String.equals() or pattern matching if that fails
     * using case-insensitive matching.
     *
     * @param arg input string
     * @param pat pattern string
     *
     * @return true if input matches the pattern
     */
    public static boolean isEqualOrMatchesCaseBlind(String arg, String pat){
        return isEqualOrMatchesCaseBlind(arg, pat, JMeterUtils.getMatcher(), JMeterUtils.getPatternCache());
    }

    /**
     * Returns <code>tidy</code> as HTML parser.
     *
     * @return a <code>tidy</code> HTML parser
     */
    public static Tidy getParser() {
        log.debug("Start : getParser1");
        Tidy tidy = new Tidy();
        tidy.setCharEncoding(org.w3c.tidy.Configuration.UTF8);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);

        if (log.isDebugEnabled()) {
            log.debug("getParser1 : tidy parser created - " + tidy);
        }

        log.debug("End : getParser1");

        return tidy;
    }

    /**
     * Returns a node representing a whole xml given an xml document.
     *
     * @param text
     *            an xml document
     * @return a node representing a whole xml
     */
    public static Node getDOM(String text) {
        log.debug("Start : getDOM1");

        try {
            Node node = getParser().parseDOM(new ByteArrayInputStream(text.getBytes("UTF-8")), null);// $NON-NLS-1$

            if (log.isDebugEnabled()) {
                log.debug("node : " + node);
            }

            log.debug("End : getDOM1");

            return node;
        } catch (UnsupportedEncodingException e) {
            log.error("getDOM1 : Unsupported encoding exception - " + e);
            log.debug("End : getDOM1");
            throw new RuntimeException("UTF-8 encoding failed");
        }
    }

    public static Document createEmptyDoc() {
        return Tidy.createEmptyDocument();
    }

    /**
     * Create a new Sampler based on an HREF string plus a contextual URL
     * object. Given that an HREF string might be of three possible forms, some
     * processing is required.
     */
    public static HTTPSamplerBase createUrlFromAnchor(String parsedUrlString, URL context) throws MalformedURLException {
        if (log.isDebugEnabled()) {
            log.debug("Creating URL from Anchor: " + parsedUrlString + ", base: " + context);
        }
        URL url = ConversionUtils.makeRelativeURL(context, parsedUrlString);
        HTTPSamplerBase sampler =HTTPSamplerFactory.newInstance();
        sampler.setDomain(url.getHost());
        sampler.setProtocol(url.getProtocol());
        sampler.setPort(url.getPort());
        sampler.setPath(url.getPath());
        sampler.parseArguments(url.getQuery());

        return sampler;
    }

    public static List<HTTPSamplerBase> createURLFromForm(Node doc, URL context) {
        String selectName = null;
        LinkedList<HTTPSamplerBase> urlConfigs = new LinkedList<HTTPSamplerBase>();
        recurseForm(doc, urlConfigs, context, selectName, false);
        /*
         * NamedNodeMap atts = formNode.getAttributes();
         * if(atts.getNamedItem("action") == null) { throw new
         * MalformedURLException(); } String action =
         * atts.getNamedItem("action").getNodeValue(); UrlConfig url =
         * createUrlFromAnchor(action, context); recurseForm(doc, url,
         * selectName,true,formStart);
         */
        return urlConfigs;
    }

    // N.B. Since the tags are extracted from an HTML Form, any values must already have been encoded
    private static boolean recurseForm(Node tempNode, LinkedList<HTTPSamplerBase> urlConfigs, URL context, String selectName,
            boolean inForm) {
        NamedNodeMap nodeAtts = tempNode.getAttributes();
        String tag = tempNode.getNodeName();
        try {
            if (inForm) {
                HTTPSamplerBase url = urlConfigs.getLast();
                if (tag.equalsIgnoreCase("form")) { // $NON-NLS-1$
                    try {
                        urlConfigs.add(createFormUrlConfig(tempNode, context));
                    } catch (MalformedURLException e) {
                        inForm = false;
                    }
                } else if (tag.equalsIgnoreCase("input")) { // $NON-NLS-1$
                    url.addEncodedArgument(getAttributeValue(nodeAtts, "name"),  // $NON-NLS-1$
                            getAttributeValue(nodeAtts, "value")); // $NON-NLS-1$
                } else if (tag.equalsIgnoreCase("textarea")) { // $NON-NLS-1$
                    try {
                        url.addEncodedArgument(getAttributeValue(nodeAtts, "name"),  // $NON-NLS-1$
                                tempNode.getFirstChild().getNodeValue());
                    } catch (NullPointerException e) {
                        url.addArgument(getAttributeValue(nodeAtts, "name"), ""); // $NON-NLS-1$
                    }
                } else if (tag.equalsIgnoreCase("select")) { // $NON-NLS-1$
                    selectName = getAttributeValue(nodeAtts, "name"); // $NON-NLS-1$
                } else if (tag.equalsIgnoreCase("option")) { // $NON-NLS-1$
                    String value = getAttributeValue(nodeAtts, "value"); // $NON-NLS-1$
                    if (value == null) {
                        try {
                            value = tempNode.getFirstChild().getNodeValue();
                        } catch (NullPointerException e) {
                            value = ""; // $NON-NLS-1$
                        }
                    }
                    url.addEncodedArgument(selectName, value);
                }
            } else if (tag.equalsIgnoreCase("form")) { // $NON-NLS-1$
                try {
                    urlConfigs.add(createFormUrlConfig(tempNode, context));
                    inForm = true;
                } catch (MalformedURLException e) {
                    inForm = false;
                }
            }
        } catch (Exception ex) {
            log.warn("Some bad HTML " + printNode(tempNode), ex);
        }
        NodeList childNodes = tempNode.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++) {
            inForm = recurseForm(childNodes.item(x), urlConfigs, context, selectName, inForm);
        }
        return inForm;
    }

    private static String getAttributeValue(NamedNodeMap att, String attName) {
        try {
            return att.getNamedItem(attName).getNodeValue();
        } catch (Exception ex) {
            return ""; // $NON-NLS-1$
        }
    }

    private static String printNode(Node node) {
        StringBuffer buf = new StringBuffer();
        buf.append("<"); // $NON-NLS-1$
        buf.append(node.getNodeName());
        NamedNodeMap atts = node.getAttributes();
        for (int x = 0; x < atts.getLength(); x++) {
            buf.append(" "); // $NON-NLS-1$
            buf.append(atts.item(x).getNodeName());
            buf.append("=\""); // $NON-NLS-1$
            buf.append(atts.item(x).getNodeValue());
            buf.append("\""); // $NON-NLS-1$
        }

        buf.append(">"); // $NON-NLS-1$

        return buf.toString();
    }

    private static HTTPSamplerBase createFormUrlConfig(Node tempNode, URL context) throws MalformedURLException {
        NamedNodeMap atts = tempNode.getAttributes();
        if (atts.getNamedItem("action") == null) { // $NON-NLS-1$
            throw new MalformedURLException();
        }
        String action = atts.getNamedItem("action").getNodeValue(); // $NON-NLS-1$
        HTTPSamplerBase url = createUrlFromAnchor(action, context);
        return url;
    }

    public static void extractStyleURLs(final URL baseUrl, final URLCollection urls, String styleTagStr) {
        Perl5Matcher matcher = JMeterUtils.getMatcher();
        Pattern pattern = JMeterUtils.getPatternCache().getPattern(
                "URL\\(\\s*('|\")(.*)('|\")\\s*\\)", // $NON-NLS-1$
                Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.SINGLELINE_MASK | Perl5Compiler.READ_ONLY_MASK);
        PatternMatcherInput input = null;
        input = new PatternMatcherInput(styleTagStr);
        while (matcher.contains(input, pattern)) {
            MatchResult match = matcher.getMatch();
            // The value is in the second group
            String styleUrl = match.group(2);
            urls.addURL(styleUrl, baseUrl);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4384.java