error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8615.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8615.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8615.java
text:
```scala
l@@og.debug("You are using a non-standard namespace name: '{}'", wicketNamespace);

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
package org.apache.wicket.markup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.lang.WicketObjects;
import org.apache.wicket.util.resource.IFixedLocationResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An IResourceStream implementation with specific extensions for markup resource streams.
 * 
 * @author Juergen Donnerstag
 */
public class MarkupResourceStream implements IResourceStream, IFixedLocationResourceStream
{
	private static final long serialVersionUID = 1846489965076612828L;

	private static final Logger log = LoggerFactory.getLogger(MarkupResourceStream.class);

	private static final Pattern DOCTYPE_REGEX = Pattern.compile("!DOCTYPE\\s+(.*)\\s*");

	/** The associated markup resource stream */
	private final IResourceStream resourceStream;

	/** Container info like Class, locale and style which were used to locate the resource */
	private final transient ContainerInfo containerInfo;

	/**
	 * The actual component class the markup is directly associated with. It might be super class of
	 * the component class
	 */
	private final String markupClassName;

	/** The key used to cache the markup resource stream */
	private String cacheKey;

	/** In case of the inherited markup, this is the base markup */
	private transient Markup baseMarkup;

	/** If found in the markup, the <?xml ...?> string */
	private CharSequence xmlDeclaration;

	/** The encoding as found in <?xml ... encoding="" ?>. Null, else */
	private String encoding;

	/**
	 * Wicket namespace: <html
	 * xmlns:wicket="http://wicket.apache.org/dtds.data/wicket-xhtml1.3-strict.dtd>
	 */
	private String wicketNamespace;

	/** == wicket namespace name + ":id" */
	private String wicketId;

	/** HTML5 http://www.w3.org/TR/html5-diff/#doctype */
	private String doctype;

	/**
	 * Construct.
	 * 
	 * @param resourceStream
	 */
	public MarkupResourceStream(final IResourceStream resourceStream)
	{
		this(resourceStream, null, null);
	}

	/**
	 * Construct.
	 * 
	 * @param resourceStream
	 * @param containerInfo
	 * @param markupClass
	 */
	public MarkupResourceStream(final IResourceStream resourceStream,
		final ContainerInfo containerInfo, final Class<?> markupClass)
	{
		this.resourceStream = resourceStream;
		this.containerInfo = containerInfo;
		markupClassName = markupClass == null ? null : markupClass.getName();

		if (resourceStream == null)
		{
			throw new IllegalArgumentException("Parameter 'resourceStream' must not be null");
		}

		setWicketNamespace(MarkupParser.WICKET);
	}

	public String locationAsString()
	{
		if (resourceStream instanceof IFixedLocationResourceStream)
		{
			return ((IFixedLocationResourceStream)resourceStream).locationAsString();
		}
		return null;
	}

	public void close() throws IOException
	{
		resourceStream.close();
	}

	public String getContentType()
	{
		return resourceStream.getContentType();
	}

	public InputStream getInputStream() throws ResourceStreamNotFoundException
	{
		return resourceStream.getInputStream();
	}

	public Locale getLocale()
	{
		return resourceStream.getLocale();
	}

	public Time lastModifiedTime()
	{
		return resourceStream.lastModifiedTime();
	}

	public Bytes length()
	{
		return resourceStream.length();
	}

	public void setLocale(Locale locale)
	{
		resourceStream.setLocale(locale);
	}

	/**
	 * Get the actual component class the markup is directly associated with. Note: it not
	 * necessarily must be the container class.
	 * 
	 * @return The directly associated class
	 */
	public Class<? extends Component> getMarkupClass()
	{
		return WicketObjects.resolveClass(markupClassName);
	}

	/**
	 * Get the container info associated with the markup
	 * 
	 * @return ContainerInfo
	 */
	public ContainerInfo getContainerInfo()
	{
		return containerInfo;
	}

	/**
	 * Gets cacheKey.
	 * 
	 * @return cacheKey
	 */
	public final String getCacheKey()
	{
		return cacheKey;
	}

	/**
	 * Set the cache key
	 * 
	 * @param cacheKey
	 */
	public final void setCacheKey(final String cacheKey)
	{
		this.cacheKey = cacheKey;
	}

	/**
	 * Gets the resource that contains this markup
	 * 
	 * @return The resource where this markup came from
	 */
	public IResourceStream getResource()
	{
		return resourceStream;
	}

	/**
	 * Return the XML declaration string, in case if found in the markup.
	 * 
	 * @return Null, if not found.
	 */
	public String getXmlDeclaration()
	{
		return (xmlDeclaration == null ? null : xmlDeclaration.toString());
	}

	/**
	 * Gets the markup encoding. A markup encoding may be specified in a markup file with an XML
	 * encoding specifier of the form &lt;?xml ... encoding="..." ?&gt;.
	 * 
	 * @return Encoding, or null if not found.
	 */
	public String getEncoding()
	{
		return encoding;
	}

	/**
	 * Get the wicket namespace valid for this specific markup
	 * 
	 * @return wicket namespace
	 */
	public String getWicketNamespace()
	{
		return wicketNamespace;
	}

	/**
	 * 
	 * @return usually it is "wicket:id"
	 */
	final public String getWicketId()
	{
		return wicketId;
	}

	/**
	 * Sets encoding.
	 * 
	 * @param encoding
	 *            encoding
	 */
	final void setEncoding(final String encoding)
	{
		this.encoding = encoding;
	}

	/**
	 * Sets wicketNamespace.
	 * 
	 * @param wicketNamespace
	 *            wicketNamespace
	 */
	public final void setWicketNamespace(final String wicketNamespace)
	{
		this.wicketNamespace = wicketNamespace;
		wicketId = wicketNamespace + ":id";

		if (!MarkupParser.WICKET.equals(wicketNamespace))
		{
			log.info("You are using a non-standard component name: " + wicketNamespace);
		}
	}

	/**
	 * Sets xmlDeclaration.
	 * 
	 * @param xmlDeclaration
	 *            xmlDeclaration
	 */
	final void setXmlDeclaration(final CharSequence xmlDeclaration)
	{
		this.xmlDeclaration = xmlDeclaration;
	}

	/**
	 * Get the resource stream containing the base markup (markup inheritance)
	 * 
	 * @return baseMarkupResource Null, if not base markup
	 */
	public MarkupResourceStream getBaseMarkupResourceStream()
	{
		if (baseMarkup == null)
		{
			return null;
		}
		return baseMarkup.getMarkupResourceStream();
	}

	/**
	 * In case of markup inheritance, the base markup.
	 * 
	 * @param baseMarkup
	 *            The base markup
	 */
	public void setBaseMarkup(Markup baseMarkup)
	{
		this.baseMarkup = baseMarkup;
	}

	/**
	 * In case of markup inheritance, the base markup resource.
	 * 
	 * @return The base markup
	 */
	public Markup getBaseMarkup()
	{
		return baseMarkup;
	}

	public String getStyle()
	{
		return resourceStream.getStyle();
	}

	public String getVariation()
	{
		return resourceStream.getVariation();
	}

	public void setStyle(String style)
	{
		resourceStream.setStyle(style);
	}

	public void setVariation(String variation)
	{
		resourceStream.setVariation(variation);
	}

	@Override
	public String toString()
	{
		if (resourceStream != null)
		{
			return resourceStream.toString();
		}
		else
		{
			return "(unknown resource)";
		}
	}

	/**
	 * Gets doctype.
	 * 
	 * @return The doctype excluding 'DOCTYPE'
	 */
	public final String getDoctype()
	{
		if (doctype == null)
		{
			MarkupResourceStream baseMarkupResourceStream = getBaseMarkupResourceStream();
			if (baseMarkupResourceStream != null)
			{
				doctype = baseMarkupResourceStream.getDoctype();
			}
		}

		return doctype;
	}

	/**
	 * Sets doctype.
	 * 
	 * @param doctype
	 *            doctype
	 */
	public final void setDoctype(final CharSequence doctype)
	{
		if (Strings.isEmpty(doctype) == false)
		{
			String doc = doctype.toString().replaceAll("[\n\r]+", "");
			doc = doc.replaceAll("\\s+", " ");
			Matcher matcher = DOCTYPE_REGEX.matcher(doc);
			if (matcher.matches() == false)
			{
				throw new MarkupException("Invalid DOCTYPE: '" + doctype + "'");
			}
			this.doctype = matcher.group(1).trim();
		}
	}

	/**
	 * @see href http://www.w3.org/TR/html5-diff/#doctype
	 * @return True, if doctype == &lt;!DOCTYPE html&gt;
	 */
	public boolean isHtml5()
	{
		return "html".equalsIgnoreCase(getDoctype());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8615.java