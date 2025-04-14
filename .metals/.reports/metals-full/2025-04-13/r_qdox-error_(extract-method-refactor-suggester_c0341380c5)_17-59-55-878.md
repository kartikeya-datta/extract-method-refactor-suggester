error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/367.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/367.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/367.java
text:
```scala
I@@ResourceStream resourceStream = locator.newResourceStream(containerClass, path, style, locale,

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
package wicket.markup.html.border;

import java.util.List;
import java.util.Locale;

import wicket.Application;
import wicket.Component;
import wicket.IComponentBorder;
import wicket.MarkupContainer;
import wicket.Response;
import wicket.Session;
import wicket.WicketRuntimeException;
import wicket.markup.ComponentTag;
import wicket.markup.ContainerInfo;
import wicket.markup.MarkupElement;
import wicket.markup.MarkupFragment;
import wicket.markup.MarkupResourceStream;
import wicket.markup.parser.filter.WicketTagIdentifier;
import wicket.util.resource.IResourceStream;
import wicket.util.resource.locator.IResourceStreamFactory;

/**
 * @TODO Comment
 * 
 * @author jcompagner
 */
public class MarkupComponentBorder implements IComponentBorder
{
	static
	{
		// register "wicket:border" and "wicket:body"
		WicketTagIdentifier.registerWellKnownTagName(Border.BORDER);
		WicketTagIdentifier.registerWellKnownTagName(Border.BODY);
	}

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @see wicket.IComponentBorder#renderAfter(wicket.Component)
	 */
	public void renderAfter(Component<?> component)
	{
		final String extension;
		if (component instanceof MarkupContainer)
		{
			extension = ((MarkupContainer<?>)component).getMarkupType();
		}
		else
		{
			extension = component.getParent().getMarkupId();
		}
		MarkupFragment markupFragment = findMarkup(extension);
		MarkupFragment childFragment = markupFragment.getWicketFragment(Border.BORDER, true);
		List<MarkupElement> allElementsFlat = childFragment.getAllElementsFlat();
		Response response = component.getResponse();
		boolean render = false;
		for (MarkupElement markupElement : allElementsFlat)
		{
			if (markupElement instanceof ComponentTag)
			{
				ComponentTag tag = (ComponentTag)markupElement;
				if (tag.isWicketBodyTag())
				{
					render = true;
					continue;
				}
				else if (tag.isBorderTag())
				{
					continue;
				}
			}
			if (render)
			{
				response.write(markupElement.toCharSequence());
			}
		}
	}

	/**
	 * 
	 * @see wicket.IComponentBorder#renderBefore(wicket.Component)
	 */
	public void renderBefore(Component<?> component)
	{
		final String extension;
		if (component instanceof MarkupContainer)
		{
			extension = ((MarkupContainer<?>)component).getMarkupType();
		}
		else
		{
			extension = component.getParent().getMarkupId();
		}
		MarkupFragment markupFragment = findMarkup(extension);
		MarkupFragment childFragment = markupFragment.getWicketFragment(Border.BORDER, true);
		
		List<MarkupElement> allElementsFlat = childFragment.getAllElementsFlat();
		Response response = component.getResponse();
		
		for (MarkupElement markupElement : allElementsFlat)
		{
			if (markupElement instanceof ComponentTag)
			{
				ComponentTag ct = (ComponentTag)markupElement;
				if (ct.isWicketBodyTag())
				{
					break;
				}
				else if (ct.isBorderTag())
				{
					continue;
				}
			}
			response.write(markupElement.toCharSequence());
		}
	}

	/**
	 * 
	 * @param extension
	 * @return MarkupFragment
	 */
	@SuppressWarnings("unchecked")
	private MarkupFragment findMarkup(final String extension)
	{
		// Get locator to search for the resource
		final IResourceStreamFactory locator = Application.get().getResourceSettings()
				.getResourceStreamFactory();

		final Session session = Session.get();
		final String style = session.getStyle();
		final Locale locale = session.getLocale();

		MarkupResourceStream markupResourceStream = null;
		Class containerClass = getClass();

		while (containerClass != MarkupComponentBorder.class)
		{
			String path = containerClass.getName().replace('.', '/');
			IResourceStream resourceStream = locator.locate(containerClass, path, style, locale,
					extension);

			// Did we find it already?
			if (resourceStream != null)
			{
				ContainerInfo ci = new ContainerInfo(containerClass, locale, style, null,
						extension, null);
				markupResourceStream = new MarkupResourceStream(resourceStream, ci, containerClass);
			}

			// Walk up the class hierarchy one level, if markup has not
			// yet been found
			containerClass = containerClass.getSuperclass();
		}
		MarkupFragment markup;
		try
		{
			markup = Application.get().getMarkupSettings().getMarkupParserFactory()
					.newMarkupParser(markupResourceStream).readAndParse();
		}
		catch (Exception e)
		{
			throw new WicketRuntimeException(e);
		}

		return markup;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/367.java