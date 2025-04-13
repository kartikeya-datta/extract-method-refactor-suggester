error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17569.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17569.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[237,2]

error in qdox parser
file content:
```java
offset: 7306
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17569.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.markup;

import java.io.Serializable;

import wicket.RequestCycle;
import wicket.model.IDetachableModel;
import wicket.model.IModel;
import wicket.util.value.ValueMap;


/**
 * This class allows a tag attribute of a component
 * to be modified dynamically with a value obtained from a model object.
 * This concept can be used to programatically alter the attributes of components,
 * overriding the values specified in the markup. The two primary uses
 * of this class are to allow overriding of markup attributes based on business
 * logic and to support dynamic localization. The replacement occurs as the
 * component tag is rendered to the wicket.response.
 * <p>
 * The attribute whose value is to be modified must be given on construction
 * of the instance of this class along with the model containing the value to
 * replace with. Optionally a pattern can be supplied that is a regular
 * expression that the existing value must match before the replacement can
 * be carried out.
 * <p>
 * Instances of this class should be added to components via the
 * {@link wicket.Component#add(ComponentTagAttributeModifier)}
 * method after the componet has been constucted.
 *
 * @author Chris Turner
 */
public class ComponentTagAttributeModifier implements Serializable
{
	/** Attribute specification. */
	private String attribute;

	/** the pattern. */
	private String pattern = null;

	/** Modification information. */
	private boolean enabled;

	/** whether to add the attribute if it is not an attribute in the markup. */
	private boolean addAttributeIfNotPresent;

	/** the model that is to be used for the replacement. */
	private IModel replaceModel;

	/**
	 * Create a new attribute modifier with the given attribute name and
	 * model to replace with. The attribute will not be added if it is
	 * not present.
	 *
	 * @param attribute The attribute name to replace the value for
	 * @param replaceModel The model to replace the value with
	 */
	public ComponentTagAttributeModifier(final String attribute, final IModel replaceModel)
	{
		this(attribute, null, false, replaceModel);
	}

	/**
	 * Create a new attribute modifier with the given attribute name and
	 * model to replace with. The additional boolean flag specifies whether
	 * to add the attribute if it is not present.
	 *
	 * @param attribute The attribute name to replace the value for
	 * @param addAttributeIfNotPresent Whether to add the attribute if it is not present
	 * @param replaceModel The model to replace the value with
	 */
	public ComponentTagAttributeModifier(final String attribute,
			final boolean addAttributeIfNotPresent, final IModel replaceModel)
	{
		this(attribute, null, addAttributeIfNotPresent, replaceModel);
	}

	/**
	 * Create a new attribute modifier with the given attribute name and
	 * expected pattern to match plus the model to replace with. A null
	 * pattern will match the attribute regardless of its value.
	 * The attribute will not be added if it is not present.
	 *
	 * @param attribute The attribute name to replace the value for
	 * @param pattern The pattern of the current attribute value to match
	 * @param replaceModel The model to replace the value with
	 */
	public ComponentTagAttributeModifier(final String attribute, final String pattern,
			final IModel replaceModel)
	{
		this(attribute, pattern, false, replaceModel);
	}

	/**
	 * Create a new attribute modifier with the given attribute name and
	 * expected pattern to match plus the model to replace with. A null
	 * pattern will match the attribute regardless of its value.
	 * The additional boolean flag specifies whether to add the attribute
	 * if it is not present.
	 *
	 * @param attribute The attribute name to replace the value for
	 * @param pattern The pattern of the current attribute value to match
	 * @param addAttributeIfNotPresent Whether to add the attribute if it is not present
	 * @param replaceModel The model to replace the value with
	 */
	public ComponentTagAttributeModifier(final String attribute, final String pattern,
			final boolean addAttributeIfNotPresent, final IModel replaceModel)
	{
		if (attribute == null)
		{
			throw new NullPointerException("'attribute' parameter cannot be null");
		}
		if (replaceModel == null)
		{
			throw new NullPointerException("'replaceModel' paramater cannot be null");
		}

		this.attribute = attribute;
		this.pattern = pattern;
		this.enabled = true;
		this.addAttributeIfNotPresent = addAttributeIfNotPresent;
		this.replaceModel = replaceModel;
	}

	/**
	 * Get the name of the attribute whose value is being replaced.
	 *
	 * @return The name of the attribute
	 */
	public final String getAttribute()
	{
		return attribute;
	}

	/**
	 * Get the pattern that the current value must match in order to be
	 * replaced.
	 *
	 * @return The pattern
	 */
	public final String getPattern()
	{
		return pattern;
	}

	/**
	 * Check whether this attribute modifier is enabled or not.
	 *
	 * @return Whether enabled or not
	 */
	public final boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Set whether this attribute modifier is enabled or not.
	 *
	 * @param enabled Whether enabled or not
	 */
	public final void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * Check whether this modifier will add an attribute to the tag
	 * if it is not present in the markup.
	 *
	 * @return Whether the attribute will be added if not present or not
	 */
	public final boolean isAddAttributeIfNotPresent()
	{
		return addAttributeIfNotPresent;
	}

	/**
	 * Get the model that the value will be replaced with.
	 *
	 * @return The model used for replacement
	 */
	public final IModel getReplaceModel()
	{
		if ((replaceModel != null) && (replaceModel instanceof IDetachableModel))
		{
			((IDetachableModel) replaceModel).attach(RequestCycle.get());
		}
		return replaceModel;
	}

	/**
	 * Check the given component tag for an instance of the attribute
	 * to modify and if all criteria are met then replace the value of
	 * this attribute with the value of the contained model object.
	 *
	 * @param tag The tag to replace the attribute value for
	 */
	public void replaceAttibuteValue(final ComponentTag tag)
	{
		if (!enabled)
			return;

		ValueMap attributes = tag.getAttributes();
		if (attributes.containsKey(attribute))
		{
			String value = attributes.get(attribute).toString();
			if (pattern == null || value.matches(pattern))
			{
				attributes.put(attribute, getReplaceModel().getObject());
			}
		}
		else if (addAttributeIfNotPresent)
		{
			attributes.put(attribute, getReplaceModel().getObject());
		}
	}

}@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17569.java