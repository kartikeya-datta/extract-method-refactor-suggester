error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13965.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13965.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13965.java
text:
```scala
p@@ublic final static class QueryParameter implements Serializable

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
package org.apache.wicket.request;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.util.lang.Checks;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.Strings;

/**
 * Represents the URL part <b>after Wicket Filter</b>. For example if Wicket Filter is mapped to
 * <code>/app/*</code> then with URL <code>/app/my/url</code> the {@link Url} object would represent
 * part <code>my/url</code>. If Wicket Filter is mapped to <code>/*</code> then with URL
 * <code>/my/url</code> the {@link Url} object would represent <code>my/url</code> (without leading
 * the slash).
 * <p>
 * URL consists of segments and query parameters.
 * <p>
 * Example URLs:
 * 
 * <pre>
 * foo/bar/baz?a=1&amp;b=5    - segments: [&quot;foo&quot;,&quot;bar,&quot;baz], query parameters: [&quot;a&quot;=&quot;1&quot;, &quot;b&quot;=&quot;5&quot;]
 * foo/bar//baz?=4&amp;6      - segments: [&quot;foo&quot;, &quot;bar&quot;, &quot;&quot;, &quot;baz&quot;], query parameters: [&quot;&quot;=&quot;4&quot;, &quot;6&quot;=&quot;&quot;]
 * /foo/bar/              - segments: [&quot;&quot;, &quot;foo&quot;, &quot;bar&quot;, &quot;&quot;]
 * foo/bar//              - segments: [&quot;foo&quot;, &quot;bar&quot;, &quot;&quot;, &quot;&quot;]
 * ?a=b                   - segments: [ ], query parameters: [&quot;a&quot;=&quot;b&quot;]
 * /                      - segments: [&quot;&quot;, &quot;&quot;]   (note that Url represents part after Wicket Filter 
 *                                                - so if Wicket filter is mapped to /* this would be
 *                                                an additional slash, i.e. //
 * </pre>
 * 
 * The Url class takes care of encoding and decoding of the segments and parameters.
 * 
 * @author Matej Knopp
 * @author Igor Vaynberg
 */
public final class Url implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_CHARSET_NAME = "UTF-8";

	private final List<String> segments = new ArrayList<String>();

	private final List<QueryParameter> parameters = new ArrayList<QueryParameter>();

	private String charsetName;
	private transient Charset _charset;

	/**
	 * 
	 * @param qp
	 * @return query parameters
	 */
	private static QueryParameter parseQueryParameter(final String qp, Charset charset)
	{
		if (qp.indexOf('=') == -1)
		{
			return new QueryParameter(decodeParameter(qp, charset), "");
		}
		String parts[] = Strings.split(qp, '=');
		if (parts.length == 0)
		{
			return new QueryParameter("", "");
		}
		else if (parts.length == 1)
		{
			return new QueryParameter("", decodeParameter(parts[0], charset));
		}
		else
		{
			return new QueryParameter(decodeParameter(parts[0], charset), decodeParameter(parts[1],
				charset));
		}
	}


	/**
	 * Parses the given URL string.
	 * 
	 * @param url
	 * @return Url object
	 */
	public static Url parse(String url)
	{
		return parse(url, null);
	}


	/**
	 * Parses the given URL string.
	 * 
	 * @param url
	 * @param charset
	 * @return Url object
	 */
	public static Url parse(String url, Charset charset)
	{
		Checks.argumentNotNull(url, "url");

		Url result = new Url(charset);

		// the url object resolved the charset, use that
		charset = result.getCharset();

		String segments;
		String query;

		int qIndex = url.indexOf('?');

		if (qIndex == -1)
		{
			segments = url;
			query = "";
		}
		else
		{
			segments = url.substring(0, qIndex);
			query = url.substring(qIndex + 1);
		}

		if (segments.length() > 0)
		{

			boolean removeLast = false;
			if (segments.endsWith("/"))
			{
				// we need to append something and remove it after splitting
				// because otherwise the
				// trailing slashes will be lost
				segments += "/x";
				removeLast = true;
			}

			String segmentArray[] = Strings.split(segments, '/');

			if (removeLast)
			{
				segmentArray[segmentArray.length - 1] = null;
			}

			for (String s : segmentArray)
			{
				if (s != null)
				{
					result.segments.add(decodeSegment(s, charset));
				}
			}
		}

		if (query.length() > 0)
		{
			String queryArray[] = Strings.split(query, '&');
			for (String s : queryArray)
			{
				result.parameters.add(parseQueryParameter(s, charset));
			}
		}

		return result;
	};

	public Charset getCharset()
	{
		if (Strings.isEmpty(charsetName))
		{
			charsetName = DEFAULT_CHARSET_NAME;
		}
		if (_charset == null)
		{
			_charset = Charset.forName(charsetName);
		}
		return _charset;
	}

	private void setCharset(Charset charset)
	{
		if (charset == null)
		{
			charsetName = "UTF-8";
		}
		_charset = null;
	}

	/**
	 * Construct.
	 */
	public Url()
	{
	}

	public Url(Charset charset)
	{
		setCharset(charset);
	}


	/**
	 * Construct.
	 * 
	 * @param url
	 *            url being copied
	 */
	public Url(Url url)
	{
		Checks.argumentNotNull(url, "url");

		segments.addAll(url.getSegments());
		parameters.addAll(url.getQueryParameters());
		setCharset(url.getCharset());
	}

	/**
	 * Construct.
	 * 
	 * @param segments
	 * @param parameters
	 */
	public Url(List<String> segments, List<QueryParameter> parameters)
	{
		this(segments, parameters, null);
	}

	/**
	 * Construct.
	 * 
	 * @param segments
	 * @param parameters
	 */
	public Url(List<String> segments, List<QueryParameter> parameters, Charset charset)
	{
		Checks.argumentNotNull(segments, "segments");
		Checks.argumentNotNull(parameters, "parameters");

		this.segments.addAll(segments);
		this.parameters.addAll(parameters);
		setCharset(charset);
	}

	/**
	 * Returns segments of the URL. Segments form the part before query string.
	 * 
	 * @return mutable list of segments
	 */
	public List<String> getSegments()
	{
		return segments;
	}

	/**
	 * Returns query parameters of the URL.
	 * 
	 * @return mutable list of query parameters
	 */
	public List<QueryParameter> getQueryParameters()
	{
		return parameters;
	}

	/**
	 * Returns whether the URL is absolute.
	 * 
	 * @return <code>true</code> if URL is absolute, <code>false</code> otherwise.
	 */
	public boolean isAbsolute()
	{
		return !getSegments().isEmpty() && Strings.isEmpty(getSegments().get(0));
	}

	/**
	 * Convenience method that removes all query parameters with given name.
	 * 
	 * @param name
	 *            query parameter name
	 */
	public void removeQueryParameters(String name)
	{
		for (Iterator<QueryParameter> i = getQueryParameters().iterator(); i.hasNext();)
		{
			QueryParameter param = i.next();
			if (Objects.equal(name, param.getName()))
			{
				i.remove();
			}
		}
	}

	/**
	 * Convenience method that removes <code>count</code> leading segments
	 * 
	 * @param count
	 */
	public void removeLeadingSegments(int count)
	{
		Checks.argumentWithinRange(0, segments.size(), count, "count");
		for (int i = 0; i < count; i++)
		{
			segments.remove(0);
		}
	}

	/**
	 * Convenience method that prepends <code>segments</code> to the segments collection
	 * 
	 * @param newSegments
	 */
	public void prependLeadingSegments(List<String> newSegments)
	{
		Checks.argumentNotNull(newSegments, "segments");
		segments.addAll(0, newSegments);
	}

	/**
	 * Convenience method that removes all query parameters with given name and adds new query
	 * parameter with specified name and value
	 * 
	 * @param name
	 * @param value
	 */
	public void setQueryParameter(String name, Object value)
	{
		removeQueryParameters(name);
		addQueryParameter(name, value);
	}

	/**
	 * Convenience method that removes adds a query parameter with given name
	 * 
	 * @param name
	 * @param value
	 */
	public void addQueryParameter(String name, Object value)
	{
		if (value != null)
		{
			QueryParameter parameter = new QueryParameter(name, value.toString());
			getQueryParameters().add(parameter);
		}
	}

	/**
	 * Returns first query parameter with specified name or null if such query parameter doesn't
	 * exist.
	 * 
	 * @param name
	 * @return query parameter or <code>null</code>
	 */
	public QueryParameter getQueryParameter(String name)
	{
		for (QueryParameter parameter : parameters)
		{
			if (Objects.equal(name, parameter.getName()))
			{
				return parameter;
			}
		}
		return null;
	}

	/**
	 * Returns the value of first query parameter with specified name. Note that this method never
	 * returns <code>null</code>. Not even if the parameter does not exist.
	 * 
	 * @see StringValue#isNull()
	 * 
	 * @param name
	 * @return {@link StringValue} instance wrapping the parameter value
	 */
	public StringValue getQueryParameterValue(String name)
	{
		QueryParameter parameter = getQueryParameter(name);
		if (parameter == null)
		{
			return StringValue.valueOf((String)null);
		}
		else
		{
			return StringValue.valueOf(parameter.getValue());
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj instanceof Url == false)
		{
			return false;
		}
		Url rhs = (Url)obj;

		return getSegments().equals(rhs.getSegments()) &&
			getQueryParameters().equals(rhs.getQueryParameters());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getSegments(), getQueryParameters());
	}

	/**
	 * 
	 * @param string
	 * @return encoded segment
	 */
	private static String encodeSegment(String string, Charset charset)
	{
		return UrlEncoder.PATH_INSTANCE.encode(string, charset);
	}

	/**
	 * 
	 * @param string
	 * @return decoded segment
	 */
	private static String decodeSegment(String string, Charset charset)
	{
		return UrlDecoder.PATH_INSTANCE.decode(string, charset);
	}

	/**
	 * 
	 * @param string
	 * @return encoded parameter
	 */
	private static String encodeParameter(String string, Charset charset)
	{
		return UrlEncoder.QUERY_INSTANCE.encode(string, charset);
	}

	/**
	 * 
	 * @param string
	 * @return decoded parameter
	 */
	private static String decodeParameter(String string, Charset charset)
	{
		return UrlDecoder.QUERY_INSTANCE.decode(string, charset);
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return toString(getCharset());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString(Charset charset)
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String s : getSegments())
		{
			if (!first)
			{
				result.append('/');
			}
			first = false;
			result.append(encodeSegment(s, charset));
		}

		first = true;

		for (QueryParameter p : getQueryParameters())
		{
			if (first)
			{
				result.append("?");
				first = false;
			}
			else
			{
				result.append("&");
			}
			result.append(p.toString(charset));
		}

		return result.toString();
	}

	/**
	 * 
	 * @return true if last segment contains a name and not something like "." or "..".
	 */
	private boolean isLastSegmentReal()
	{
		if (segments.isEmpty())
		{
			return false;
		}
		String last = segments.get(segments.size() - 1);
		return last.length() > 0 && !".".equals(last) && !"..".equals(last);
	}

	/**
	 * @param segments
	 * @return true if last segment is empty
	 */
	private boolean isLastSegmentEmpty(List<String> segments)
	{
		if (segments.isEmpty())
		{
			return false;
		}
		String last = segments.get(segments.size() - 1);
		return last.length() == 0;
	}

	private boolean isLastSegmentEmpty()
	{
		return isLastSegmentEmpty(segments);
	}

	private boolean isAtLeastOnSegmentReal(List<String> segments)
	{
		for (String s : segments)
		{
			if (s.length() > 0 && !".".equals(s) && !"..".equals(s))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Concatenate the specified segments; The segments can be relative - begin with "." or "..".
	 * 
	 * @param segments
	 */
	public void concatSegments(List<String> segments)
	{
		boolean checkedLastSegment = false;

		if (!isAtLeastOnSegmentReal(segments) && !isLastSegmentEmpty(segments))
		{
			segments = new ArrayList<String>(segments);
			segments.add("");
		}

		for (String s : segments)
		{
			if (".".equals(s))
			{
				continue;
			}
			else if ("..".equals(s) && !this.segments.isEmpty())
			{
				this.segments.remove(this.segments.size() - 1);
			}
			else
			{
				if (!checkedLastSegment)
				{
					if (isLastSegmentReal() || isLastSegmentEmpty())
					{
						this.segments.remove(this.segments.size() - 1);
					}
					checkedLastSegment = true;
				}
				this.segments.add(s);
			}
		}

		if (this.segments.size() == 1 && this.segments.get(0).length() == 0)
		{
			this.segments.clear();
		}
	}

	/**
	 * Represents a single query parameter
	 * 
	 * @author Matej Knopp
	 */
	public final static class QueryParameter
	{
		private final String name;
		private final String value;

		/**
		 * Creates new {@link QueryParameter} instance. The <code>name</code> and <code>value</code>
		 * parameters must not be <code>null</code>, though they can be empty strings.
		 * 
		 * @param name
		 *            parameter name
		 * @param value
		 *            parameter value
		 */
		public QueryParameter(String name, String value)
		{
			Checks.argumentNotNull(name, "name");
			Checks.argumentNotNull(value, "value");

			this.name = name;
			this.value = value;
		}

		/**
		 * Returns query parameter name.
		 * 
		 * @return query parameter name
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * Returns query parameter value.
		 * 
		 * @return query parameter value
		 */
		public String getValue()
		{
			return value;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj instanceof QueryParameter == false)
			{
				return false;
			}
			QueryParameter rhs = (QueryParameter)obj;
			return Objects.equal(getName(), rhs.getName()) &&
				Objects.equal(getValue(), rhs.getValue());
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			return Objects.hashCode(getName(), getValue());
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return toString(Charset.forName("UTF-8"));
		}

		public String toString(Charset charset)
		{
			StringBuilder result = new StringBuilder();
			result.append(encodeParameter(getName(), charset));
			if (!Strings.isEmpty(getValue()))
			{
				result.append('=');
				result.append(encodeParameter(getValue(), charset));
			}
			return result.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13965.java