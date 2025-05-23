error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14469.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14469.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14469.java
text:
```scala
S@@tringBuilder sb = new StringBuilder(numChars > 500 ? numChars / 2 : numChars);

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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Adapted from java.net.URLDecoder, but defines instances for query string decoding versus URL path
 * component decoding.
 * <p/>
 * The difference is important because a space is encoded as a + in a query string, but this is a
 * valid value in a path component (and is therefore not decode back to a space).
 * 
 * @author Doug Donohoe
 * @see java.net.URLDecoder
 * @see {@link "http://www.ietf.org/rfc/rfc2396.txt"}
 */
public class UrlDecoder
{
	private final boolean decodePlus;

	/**
	 * Encoder used to decode name or value components of a query string.<br/>
	 * <br/>
	 * 
	 * For example: http://org.acme/notthis/northis/oreventhis?buthis=isokay&asis=thispart
	 */
	public static final UrlDecoder QUERY_INSTANCE = new UrlDecoder(true);

	/**
	 * Encoder used to decode components of a path.<br/>
	 * <br/>
	 * 
	 * For example: http://org.acme/foo/thispart/orthispart?butnot=thispart
	 */
	public static final UrlDecoder PATH_INSTANCE = new UrlDecoder(false);

	/**
	 * Create decoder
	 * 
	 * @param decodePlus
	 *            - whether to decode + to space
	 */
	private UrlDecoder(boolean decodePlus)
	{
		this.decodePlus = decodePlus;
	}

	// /**
	// * Calls decode with the application response request encoding as returned
	// by
	// *
	// Application.get().getRequestCycleSettings().getResponseRequestEncoding()
	// *
	// * @param s
	// * Value to encode
	// * @return String encoded using default Application request/respose
	// encoding
	// */
	// public String decode(String s)
	// {
	// Application app = null;
	//
	// try
	// {
	// app = Application.get();
	// }
	// catch (WicketRuntimeException ignored)
	// {
	// log.warn("No current Application found - defaulting encoding to UTF-8");
	// }
	// return decode(s, app == null ? "UTF-8" : app.getRequestCycleSettings()
	// .getResponseRequestEncoding());
	// }

	/**
	 * @param s
	 *            string to decode
	 * @param enc
	 *            encoding to decode with
	 * @return decoded string
	 * @see java.net.URLDecoder#decode(String, String)
	 */
	public String decode(String s, Charset enc)
	{
		return decode(s, enc.name());
	}


	/**
	 * @param s
	 *            string to decode
	 * @param enc
	 *            encoding to decode with
	 * @return decoded string
	 * @see java.net.URLDecoder#decode(String, String)
	 */
	public String decode(String s, String enc)
	{
		if (s == null)
		{
			return null;
		}

		boolean needToChange = false;
		int numChars = s.length();
		StringBuffer sb = new StringBuffer(numChars > 500 ? numChars / 2 : numChars);
		int i = 0;

		if (enc.length() == 0)
		{
			throw new RuntimeException(new UnsupportedEncodingException(
				"URLDecoder: empty string enc parameter"));
		}

		char c;
		byte[] bytes = null;
		while (i < numChars)
		{
			c = s.charAt(i);
			switch (c)
			{
				case '+' :
					sb.append(decodePlus ? ' ' : '+');
					i++;
					needToChange = true;
					break;

				case '%' :
					/*
					 * Starting with this instance of %, process all consecutive substrings of the
					 * form %xy. Each substring %xy will yield a byte. Convert all consecutive bytes
					 * obtained this way to whatever character(s) they represent in the provided
					 * encoding.
					 */
					try
					{
						// (numChars-i)/3 is an upper bound for the number
						// of remaining bytes
						if (bytes == null)
						{
							bytes = new byte[(numChars - i) / 3];
						}
						int pos = 0;

						while (((i + 2) < numChars) && (c == '%'))
						{
							bytes[pos++] = (byte)Integer.parseInt(s.substring(i + 1, i + 3), 16);
							i += 3;
							if (i < numChars)
							{
								c = s.charAt(i);
							}
						}

						// A trailing, incomplete byte encoding such as
						// "%x" will cause an exception to be thrown
						if ((i < numChars) && (c == '%'))
						{
							throw new IllegalArgumentException(
								"URLDecoder: Incomplete trailing escape (%) pattern");
						}

						try
						{
							sb.append(new String(bytes, 0, pos, enc));
						}
						catch (UnsupportedEncodingException e)
						{
							throw new RuntimeException(e);
						}
					}
					catch (NumberFormatException e)
					{
						throw new IllegalArgumentException(
							"URLDecoder: Illegal hex characters in escape (%) pattern - " +
								e.getMessage());
					}
					needToChange = true;
					break;

				default :
					sb.append(c);
					i++;
					break;
			}
		}

		return (needToChange ? sb.toString() : s);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14469.java