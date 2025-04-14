error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3531.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3531.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3531.java
text:
```scala
i@@nt colon = attrAndKey.lastIndexOf(":");

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
package org.apache.wicket.markup.parser.filter;

import java.text.ParseException;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.util.string.Strings;


/**
 * This is a markup inline filter and a component resolver. It identifies wicket:message attributes
 * and adds an attribute modifier to the component tag that can localize
 * wicket:message="attr-name:i18n-key,attr-name-2:i18n-key-2,..." expressions, replacing values of
 * attributes specified by attr-name with a localizer lookup with key i18n-key. If an attribute
 * being localized has a set value that value will be used as the default value for the localization
 * lookup. This handler also resolves and localizes raw markup with wicket:message attribute.
 * 
 * @author Juergen Donnerstag
 * @author Igor Vaynberg
 */
public final class WicketMessageTagHandler extends AbstractMarkupFilter
		implements
			IComponentResolver
{
	/** */
	private static final long serialVersionUID = 1L;

	/** TODO Post 1.2: General: Namespace should not be a constant */
	private final static String WICKET_MESSAGE_ATTRIBUTE_NAME = "wicket:message";

	/**
	 * The id automatically assigned to tags with wicket:message attribute but without id
	 */
	public final static String WICKET_MESSAGE_CONTAINER_ID = "_message_attr_";

	/** singleton instance of {@link AttributeLocalizer} */
	public static final IBehavior ATTRIBUTE_LOCALIZER = new AttributeLocalizer();

	/**
	 * Construct.
	 */
	public WicketMessageTagHandler()
	{
	}

	/**
	 * 
	 * @see org.apache.wicket.markup.parser.IMarkupFilter#nextTag()
	 * @return The next tag to be processed. Null, if not more tags are available
	 */
	public final MarkupElement nextTag() throws ParseException
	{
		// Get the next tag from the next MarkupFilter in the chain
		// If null, no more tags are available
		final ComponentTag tag = nextComponentTag();
		if ((tag == null) || tag.isClose())
		{
			return tag;
		}

		final String wicketMessageAttribute = tag.getAttributes().getString(
				WICKET_MESSAGE_ATTRIBUTE_NAME);

		if ((wicketMessageAttribute != null) && (wicketMessageAttribute.trim().length() > 0))
		{
			// check if this tag is raw markup
			if (tag.getId() == null)
			{
				// if this is a raw tag we need to set the id to something so
				// that wicket will not merge this as raw markup and instead
				// pass it on to a resolver
				tag.setId(WICKET_MESSAGE_CONTAINER_ID);
				tag.setAutoComponentTag(true);
				tag.setModified(true);
			}
			tag.addBehavior(new AttributeLocalizer());
		}

		return tag;
	}

	/**
	 * Attribute localizing behavior. See the javadoc of {@link WicketMessageTagHandler} for
	 * details.
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 */
	public static class AttributeLocalizer extends AbstractBehavior
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @see org.apache.wicket.behavior.AbstractBehavior#onComponentTag(org.apache.wicket.Component,
		 *      org.apache.wicket.markup.ComponentTag)
		 */
		public void onComponentTag(final Component component, final ComponentTag tag)
		{
			String expr = tag.getAttributes().getString(WICKET_MESSAGE_ATTRIBUTE_NAME);
			if (!Strings.isEmpty(expr))
			{
				expr = expr.trim();

				String[] attrsAndKeys = expr.split(",");

				for (int i = 0; i < attrsAndKeys.length; i++)
				{
					String attrAndKey = attrsAndKeys[i];
					int colon = attrAndKey.indexOf(":");
					// make sure the attribute-key pair is valid
					if (attrAndKey.length() < 3 || colon < 1 || colon > attrAndKey.length() - 2)
					{
						throw new WicketRuntimeException(
								"wicket:message attribute contains an invalid value [[" + expr +
										"]], must be of form (attr:key)+");
					}

					String attr = attrAndKey.substring(0, colon);
					String key = attrAndKey.substring(colon + 1);

					// we need to call the proper getString() method based on
					// whether or not we have a default value
					final String value;
					if (tag.getAttributes().containsKey(attr))
					{
						value = component.getString(key, null, tag.getAttributes().getString(attr));
					}
					else
					{
						value = component.getString(key);
					}
					tag.put(attr, value);
				}
			}
		}
	}

	/**
	 * 
	 * @see org.apache.wicket.markup.resolver.IComponentResolver#resolve(org.apache.wicket.MarkupContainer,
	 *      org.apache.wicket.markup.MarkupStream, org.apache.wicket.markup.ComponentTag)
	 */
	public boolean resolve(MarkupContainer container, MarkupStream markupStream, ComponentTag tag)
	{
		// localize any raw markup that has wicket:message attrs

		if (WICKET_MESSAGE_CONTAINER_ID.equals(tag.getId()))
		{
			Component wc = null;
			if (tag.isOpenClose())
			{
				wc = new WebComponent(WICKET_MESSAGE_CONTAINER_ID +
						container.getPage().getAutoIndex());
			}
			else
			{
				wc = new WebMarkupContainer(WICKET_MESSAGE_CONTAINER_ID +
						container.getPage().getAutoIndex());
			}

			container.autoAdd(wc, markupStream);
			return true;
		}
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3531.java