error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18319.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18319.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18319.java
text:
```scala
private static final T@@hreadLocal localInput = new ThreadLocal() {

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

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

// NOTE: Also looked at using Java 1.4 regexp instead of ORO. The change was
// trivial. Performance did not improve -- at least not significantly.
// Finally decided for ORO following advise from Stefan Bodewig (message
// to jmeter-dev dated 25 Nov 2003 8:52 CET) [Jordi]
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * HtmlParser implementation using regular expressions.
 * <p>
 * This class will find RLs specified in the following ways (where <b>url</b>
 * represents the RL being found:
 * <ul>
 * <li>&lt;img src=<b>url</b> ... &gt;
 * <li>&lt;script src=<b>url</b> ... &gt;
 * <li>&lt;applet code=<b>url</b> ... &gt;
 * <li>&lt;input type=image src=<b>url</b> ... &gt;
 * <li>&lt;body background=<b>url</b> ... &gt;
 * <li>&lt;table background=<b>url</b> ... &gt;
 * <li>&lt;td background=<b>url</b> ... &gt;
 * <li>&lt;tr background=<b>url</b> ... &gt;
 * <li>&lt;applet ... codebase=<b>url</b> ... &gt;
 * <li>&lt;embed src=<b>url</b> ... &gt;
 * <li>&lt;embed codebase=<b>url</b> ... &gt;
 * <li>&lt;object codebase=<b>url</b> ... &gt;
 * <li>&lt;link rel=stylesheet href=<b>url</b>... gt;
 * <li>&lt;bgsound src=<b>url</b> ... &gt;
 * <li>&lt;frame src=<b>url</b> ... &gt;
 * </ul>
 * 
 * <p>
 * This class will take into account the following construct:
 * <ul>
 * <li>&lt;base href=<b>url</b>&gt;
 * </ul>
 * 
 * <p>
 * But not the following:
 * <ul>
 * <li>&lt; ... codebase=<b>url</b> ... &gt;
 * </ul>
 * 
 */
class RegexpHTMLParser extends HTMLParser {
    private static final Logger log = LoggingManager.getLoggerForClass();

	/**
	 * Regexp fragment matching a tag attribute's value (including the equals
	 * sign and any spaces before it). Note it matches unquoted values, which to
	 * my understanding, are not conformant to any of the HTML specifications,
	 * but are still quite common in the web and all browsers seem to understand
	 * them.
	 */
	private static final String VALUE = "\\s*=\\s*(?:\"([^\"]*)\"|'([^']*)'|([^\"'\\s>\\\\][^\\s>]*)(?=[\\s>]))";

	// Note there's 3 capturing groups per value

	/**
	 * Regexp fragment matching the separation between two tag attributes.
	 */
	private static final String SEP = "\\s(?:[^>]*\\s)?";

	/**
	 * Regular expression used against the HTML code to find the URIs of images,
	 * etc.:
	 */
	private static final String REGEXP = 
		      "<(?:" + "!--.*?-->"
		    + "|BASE" + SEP + "HREF" + VALUE
			+ "|(?:IMG|SCRIPT|FRAME|IFRAME|BGSOUND|FRAME)" + SEP + "SRC" + VALUE
			+ "|APPLET" + SEP + "CODE(?:BASE)?"	+ VALUE
			+ "|(?:EMBED|OBJECT)" + SEP + "(?:SRC|CODEBASE)" + VALUE
			+ "|(?:BODY|TABLE|TR|TD)" + SEP + "BACKGROUND" + VALUE
			+ "|[^<]+?STYLE\\s*=['\"].*?URL\\(\\s*['\"](.+?)['\"]\\s*\\)"
			+ "|INPUT(?:" + SEP + "(?:SRC" + VALUE
			+ "|TYPE\\s*=\\s*(?:\"image\"|'image'|image(?=[\\s>])))){2,}"
			+ "|LINK(?:" + SEP + "(?:HREF" + VALUE
			+ "|REL\\s*=\\s*(?:\"stylesheet\"|'stylesheet'|stylesheet(?=[\\s>])))){2,}" + ")";

	// Number of capturing groups possibly containing Base HREFs:
	private static final int NUM_BASE_GROUPS = 3;

	/**
	 * Thread-local input:
	 */
	private static ThreadLocal localInput = new ThreadLocal() {
		protected Object initialValue() {
			return new PatternMatcherInput(new char[0]);
		}
	};

	protected boolean isReusable() {
		return true;
	}

	/**
	 * Make sure to compile the regular expression upon instantiation:
	 */
	protected RegexpHTMLParser() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.protocol.http.parser.HtmlParser#getEmbeddedResourceURLs(byte[],
	 *      java.net.URL)
	 */
	public Iterator getEmbeddedResourceURLs(byte[] html, URL baseUrl, URLCollection urls) {

		Perl5Matcher matcher = JMeterUtils.getMatcher();
		PatternMatcherInput input = (PatternMatcherInput) localInput.get();
		// TODO: find a way to avoid the cost of creating a String here --
		// probably a new PatternMatcherInput working on a byte[] would do
		// better.
		input.setInput(new String(html));
		Pattern pattern=JMeterUtils.getPatternCache().getPattern(
				REGEXP, 
				Perl5Compiler.CASE_INSENSITIVE_MASK 
 Perl5Compiler.SINGLELINE_MASK
 Perl5Compiler.READ_ONLY_MASK);
		
		while (matcher.contains(input, pattern)) {
			MatchResult match = matcher.getMatch();
			String s;
			if (log.isDebugEnabled()) {
				log.debug("match groups " + match.groups() + " " + match.toString());
			}
			// Check for a BASE HREF:
			for (int g = 1; g <= NUM_BASE_GROUPS && g <= match.groups(); g++) {
				s = match.group(g);
				if (s != null) {
					if (log.isDebugEnabled()) {
						log.debug("new baseUrl: " + s + " - " + baseUrl.toString());
					}
					try {
						baseUrl = new URL(baseUrl, s);
					} catch (MalformedURLException e) {
						// Doesn't even look like a URL?
						// Maybe it isn't: Ignore the exception.
						if (log.isDebugEnabled()) {
							log.debug("Can't build base URL from RL " + s + " in page " + baseUrl, e);
						}
					}
				}
			}
			for (int g = NUM_BASE_GROUPS + 1; g <= match.groups(); g++) {
				s = match.group(g);
				if (s != null) {
					if (log.isDebugEnabled()) {
						log.debug("group " + g + " - " + match.group(g));
					}
					urls.addURL(s, baseUrl);
				}
			}
		}
		return urls.iterator();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18319.java