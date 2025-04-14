error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10059.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10059.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10059.java
text:
```scala
public final static S@@tring WICKET_MESSAGE_CONTAINER_ID = Component.AUTO_COMPONENT_PREFIX + "message_attr";

/*
 * $Id: WicketMessageTagHandler.java 5771 2006-05-19 12:04:06 +0000 (Fri, 19 May
 * 2006) joco01 $ $Revision$ $Date: 2006-05-19 12:04:06 +0000 (Fri, 19
 * May 2006) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.parser.filter;

import java.text.ParseException;

import wicket.Component;
import wicket.WicketRuntimeException;
import wicket.behavior.AbstractBehavior;
import wicket.behavior.IBehavior;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupElement;
import wicket.markup.parser.AbstractMarkupFilter;
import wicket.util.string.Strings;

/**
 * This is a markup inline filter. It identifies wicket:message attributes and
 * adds an attribute modifier to the component tag that can localize
 * wicket:message="attr-name:i18n-key,attr-name-2:i18n-key-2,..." expressions,
 * replacing values of attributes specified by attr-name with a localizer lookup
 * with key i18n-key. If an attribute being localized has a set value that value
 * will be used as the default value for the localization lookup.
 * 
 * @author Juergen Donnerstag
 * @author Igor Vaynberg
 */
public final class WicketMessageTagHandler extends AbstractMarkupFilter
{
	/** TODO Post 1.2: General: Namespace should not be a constant */
	private final static String WICKET_MESSAGE_ATTRIBUTE_NAME = "wicket:message";

	/** The id automatically assigned to tags with wicket:message attribute but without id */
	public final static String WICKET_MESSAGE_CONTAINER_ID = Component.AUTO_COMPONENT_PREFIX + "message";
	
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
	 * @see wicket.markup.parser.IMarkupFilter#nextTag()
	 * @return The next tag to be processed. Null, if not more tags are
	 *         available
	 */
	public final MarkupElement nextTag() throws ParseException
	{
		// Get the next tag from the next MarkupFilter in the chain
		// If null, no more tags are available
		final ComponentTag tag = nextComponentTag();
		if (tag == null)
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
				
				// There is no point attaching the attributelocalizer to this
				// tag because it will be represented by an auto component and
				// they do not inherit the behaviors from their component tag
				// unlike regular components, instead the attributelocalizer
				// will be added by the code that creates the auto component
			}
			else
			{
				// if this is a component tag we attach a behavior to it that
				// will in turn be attached to the component that is attached to
				// this tag
				tag.addBehavior(ATTRIBUTE_LOCALIZER);
			}
		}

		return tag;
	}

	/**
	 * Attribute localizing behavior. See the javadoc of
	 * {@link WicketMessageTagHandler} for details.
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 */
	private static class AttributeLocalizer extends AbstractBehavior
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @see wicket.behavior.AbstractBehavior#onComponentTag(wicket.Component,
		 *      wicket.markup.ComponentTag)
		 */
		@Override
		public void onComponentTag(final Component component, final ComponentTag tag)
		{
			String expr = tag.getAttributes().getString(WICKET_MESSAGE_ATTRIBUTE_NAME);
			if (!Strings.isEmpty(expr))
			{
				expr = expr.trim();

				String[] attrsAndKeys = expr.split(",");

				for (String attrAndKey : attrsAndKeys)
				{
					int colon = attrAndKey.indexOf(":");
					// make sure the attribute-key pair is valid
					if (attrAndKey.length() < 3 || colon < 1 || colon > attrAndKey.length() - 2)
					{
						throw new WicketRuntimeException(
								"wicket:message attribute contains an invalid value [[" + expr
										+ "]], must be of form (attr:key)+");
					}

					String attr = attrAndKey.substring(0, colon);
					String key = attrAndKey.substring(colon + 1);

					String value;
					// we need to call the proper getString() method based on
					// whether or not we have a default value
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10059.java