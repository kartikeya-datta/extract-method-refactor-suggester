error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11899.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11899.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11899.java
text:
```scala
O@@bject[] values = new Object[this.variableNames.size()];

/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.web.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

/**
 * Represents a URI template. A URI template is a URI-like String that contains variables enclosed
 * by braces (<code>{</code>, <code>}</code>), which can be expanded to produce an actual URI.
 *
 * <p>See {@link #expand(Map)}, {@link #expand(Object[])}, and {@link #match(String)} for example usages.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 3.0
 * @see <a href="http://bitworking.org/projects/URI-Templates/">URI Templates</a>
 */
public class UriTemplate implements Serializable {

	/** Captures URI template variable names. */
	private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");

	/** Replaces template variables in the URI template. */
	private static final String VALUE_REGEX = "(.*)";


	private final List<String> variableNames;

	private final Pattern matchPattern;

	private final String uriTemplate;


	/**
	 * Construct a new {@link UriTemplate} with the given URI String.
	 * @param uriTemplate the URI template string
	 */
	public UriTemplate(String uriTemplate) {
		Parser parser = new Parser(uriTemplate);
		this.uriTemplate = uriTemplate;
		this.variableNames = parser.getVariableNames();
		this.matchPattern = parser.getMatchPattern();
	}

	/**
	 * Return the names of the variables in the template, in order.
	 * @return the template variable names
	 */
	public List<String> getVariableNames() {
		return this.variableNames;
	}


	/**
	 * Given the Map of variables, expands this template into a URI. The Map keys represent variable names,
	 * the Map values variable values. The order of variables is not significant.
	 * <p>Example:
	 * <pre class="code">
	 * UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
	 * Map&lt;String, String&gt; uriVariables = new HashMap&lt;String, String&gt;();
	 * uriVariables.put("booking", "42");
	 * uriVariables.put("hotel", "1");
	 * System.out.println(template.expand(uriVariables));
	 * </pre>
	 * will print: <blockquote><code>http://example.com/hotels/1/bookings/42</code></blockquote>
	 * @param uriVariables the map of URI variables
	 * @return the expanded URI
	 * @throws IllegalArgumentException if <code>uriVariables</code> is <code>null</code>;
	 * or if it does not contain values for all the variable names
	 */
	public URI expand(Map<String, ?> uriVariables) {
		Assert.notNull(uriVariables, "'uriVariables' must not be null");
		Object[] values = new String[this.variableNames.size()];
		for (int i = 0; i < this.variableNames.size(); i++) {
			String name = this.variableNames.get(i);
			if (!uriVariables.containsKey(name)) {
				throw new IllegalArgumentException("'uriVariables' Map has no value for '" + name + "'");
			}
			values[i] = uriVariables.get(name);
		}
		return expand(values);
	}

	/**
	 * Given an array of variables, expand this template into a full URI. The array represent variable values.
	 * The order of variables is significant.
	 * <p>Example:
	 * <pre class="code">
	 * UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
	 * System.out.println(template.expand("1", "42));
	 * </pre>
	 * will print: <blockquote><code>http://example.com/hotels/1/bookings/42</code></blockquote>
	 * @param uriVariableValues the array of URI variables
	 * @return the expanded URI
	 * @throws IllegalArgumentException if <code>uriVariables</code> is <code>null</code>
	 * or if it does not contain sufficient variables
	 */
	public URI expand(Object... uriVariableValues) {
		Assert.notNull(uriVariableValues, "'uriVariableValues' must not be null");
		if (uriVariableValues.length != this.variableNames.size()) {
			throw new IllegalArgumentException(
					"Invalid amount of variables values in [" + this.uriTemplate + "]: expected " +
							this.variableNames.size() + "; got " + uriVariableValues.length);
		}
		Matcher matcher = NAMES_PATTERN.matcher(this.uriTemplate);
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		while (matcher.find()) {
			Object uriVariable = uriVariableValues[i++];
			String replacement = Matcher.quoteReplacement(uriVariable != null ? uriVariable.toString() : "");
			matcher.appendReplacement(buffer, replacement);
		}
		matcher.appendTail(buffer);
		return encodeUri(buffer.toString());
	}

	/**
	 * Indicate whether the given URI matches this template.
	 * @param uri the URI to match to
	 * @return <code>true</code> if it matches; <code>false</code> otherwise
	 */
	public boolean matches(String uri) {
		if (uri == null) {
			return false;
		}
		Matcher matcher = this.matchPattern.matcher(uri);
		return matcher.matches();
	}

	/**
	 * Match the given URI to a map of variable values. Keys in the returned map are variable names,
	 * values are variable values, as occurred in the given URI.
	 * <p>Example:
	 * <pre class="code">
	 * UriTemplate template = new UriTemplate("http://example.com/hotels/{hotel}/bookings/{booking}");
	 * System.out.println(template.match("http://example.com/hotels/1/bookings/42"));
	 * </pre>
	 * will print: <blockquote><code>{hotel=1, booking=42}</code></blockquote>
	 * @param uri the URI to match to
	 * @return a map of variable values
	 */
	public Map<String, String> match(String uri) {
		Assert.notNull(uri, "'uri' must not be null");
		Map<String, String> result = new LinkedHashMap<String, String>(this.variableNames.size());
		Matcher matcher = this.matchPattern.matcher(uri);
		if (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String name = this.variableNames.get(i - 1);
				String value = matcher.group(i);
				result.put(name, value);
			}
		}
		return result;
	}

	/**
	 * Encodes the given String as URL.
	 * <p>Defaults to {@link UriUtils#encodeUri(String, String)}.
	 * @param uri the URI to encode
	 * @return the encoded URI
	 */
	protected URI encodeUri(String uri) {
		try {
			String encoded = UriUtils.encodeUri(uri, "UTF-8");
			return new URI(encoded);
		}
		catch (UnsupportedEncodingException ex) {
			// should not happen, UTF-8 is always supported
			throw new IllegalStateException(ex);
		}
		catch (URISyntaxException ex) {
			throw new IllegalArgumentException("Could not create URI from [" + uri + "]: " + ex, ex);
		}
	}

	@Override
	public String toString() {
		return this.uriTemplate;
	}


	/**
	 * Static inner class to parse URI template strings into a matching regular expression.
	 */
	private static class Parser {

		private final List<String> variableNames = new LinkedList<String>();

		private final StringBuilder patternBuilder = new StringBuilder();

		private Parser(String uriTemplate) {
			Assert.hasText(uriTemplate, "'uriTemplate' must not be null");
			Matcher m = NAMES_PATTERN.matcher(uriTemplate);
			int end = 0;
			while (m.find()) {
				this.patternBuilder.append(quote(uriTemplate, end, m.start()));
				this.patternBuilder.append(VALUE_REGEX);
				this.variableNames.add(m.group(1));
				end = m.end();
			}
			this.patternBuilder.append(quote(uriTemplate, end, uriTemplate.length()));
			int lastIdx = this.patternBuilder.length() - 1;
			if (lastIdx >= 0 && this.patternBuilder.charAt(lastIdx) == '/') {
				this.patternBuilder.deleteCharAt(lastIdx);
			}
		}

		private String quote(String fullPath, int start, int end) {
			if (start == end) {
				return "";
			}
			return Pattern.quote(fullPath.substring(start, end));
		}

		private List<String> getVariableNames() {
			return Collections.unmodifiableList(this.variableNames);
		}

		private Pattern getMatchPattern() {
			return Pattern.compile(this.patternBuilder.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11899.java