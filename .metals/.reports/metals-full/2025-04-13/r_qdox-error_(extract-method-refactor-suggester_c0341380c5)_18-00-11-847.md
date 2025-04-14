error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14140.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14140.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14140.java
text:
```scala
public S@@tring asString(Map<String, ?> variables)

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
package org.apache.wicket.util.template;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;


/**
 * Provides the ability to 'decorate' the actual template contents before it is contributed to the
 * header. For example, to embed it inside a JavaScript tag pair.
 * 
 * @author Eelco Hillenius
 * @since 1.2.6
 */
public abstract class TextTemplateDecorator extends TextTemplate
{
	private static final long serialVersionUID = 1L;

	/**
	 * The decorated <code>TextTemplate</code>.
	 */
	protected final TextTemplate decorated;

	/**
	 * Constructor.
	 * 
	 * @param textTemplate
	 *            a <code>TextTemplate</code> to decorate
	 */
	public TextTemplateDecorator(TextTemplate textTemplate)
	{
		if (textTemplate == null)
		{
			throw new IllegalArgumentException("argument textTemplate must be not null");
		}

		decorated = textTemplate;
	}

	/**
	 * Returns the decorated contents as a <code>String</code>.
	 * 
	 * @return the contents decorated with {@link #getBeforeTemplateContents()} and
	 *         {@link #getAfterTemplateContents()}
	 * @see org.apache.wicket.util.template.TextTemplate#asString()
	 */
	@Override
	public String asString()
	{
		StringBuffer b = new StringBuffer();
		b.append(getBeforeTemplateContents());
		b.append(decorated.asString());
		b.append(getAfterTemplateContents());
		return b.toString();
	}

	/**
	 * Returns the decorated contents as a <code>String</code>.
	 * 
	 * @return the contents decorated with {@link #getBeforeTemplateContents()} and
	 *         {@link #getAfterTemplateContents()}.
	 * @see org.apache.wicket.util.template.TextTemplate#asString(java.util.Map)
	 */
	@Override
	public String asString(Map<String, Object> variables)
	{
		StringBuffer b = new StringBuffer();
		b.append(getBeforeTemplateContents());
		b.append(decorated.asString(variables));
		b.append(getAfterTemplateContents());
		return b.toString();
	}

	/**
	 * Retrieves the <code>String</code> to put before the actual template contents, for example:
	 * 
	 * <pre>
	 *    &lt;script type=&quot;text/javascript&quot;&gt;
	 * </pre>
	 * 
	 * @return the <code>String</code> to put before the actual template contents
	 */
	public abstract String getBeforeTemplateContents();

	/**
	 * Retrieves the <code>String</code> to put after the actual template contents, for example:
	 * 
	 * <pre>
	 *    &lt;/script&gt;
	 * </pre>
	 * 
	 * @return the <code>String</code> to put after the actual template contents
	 */
	public abstract String getAfterTemplateContents();

	/**
	 * @see org.apache.wicket.util.resource.AbstractStringResourceStream#close()
	 */
	@Override
	public void close() throws IOException
	{
		decorated.close();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return decorated.equals(obj);
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractStringResourceStream#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return decorated.getContentType();
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractStringResourceStream#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws ResourceStreamNotFoundException
	{
		return decorated.getInputStream();
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractResourceStream#getLocale()
	 */
	@Override
	public Locale getLocale()
	{
		return decorated.getLocale();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return decorated.hashCode();
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractStringResourceStream#lastModifiedTime()
	 */
	@Override
	public Time lastModifiedTime()
	{
		return decorated.lastModifiedTime();
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractResourceStream#setCharset(java.nio.charset.Charset)
	 */
	@Override
	public void setCharset(Charset charset)
	{
		decorated.setCharset(charset);
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractStringResourceStream#setLastModified(org.apache.wicket.util.time.Time)
	 */
	@Override
	public void setLastModified(Time lastModified)
	{
		decorated.setLastModified(lastModified);
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractResourceStream#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale locale)
	{
		decorated.setLocale(locale);
	}

	/**
	 * @see org.apache.wicket.util.template.TextTemplate#getString()
	 */
	@Override
	public String getString()
	{
		return decorated.getString();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return decorated.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14140.java