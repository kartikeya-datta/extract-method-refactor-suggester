error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3366.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3366.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3366.java
text:
```scala
r@@eturn (replaceModel != null) ? replaceModel.getObject() : null;

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
package wicket;

import wicket.behavior.AbstractBehavior;
import wicket.markup.ComponentTag;
import wicket.markup.parser.XmlTag;
import wicket.model.IModel;
import wicket.util.value.ValueMap;

/**
 * This class allows a tag attribute of a component to be modified dynamically
 * with a value obtained from a model object. This concept can be used to
 * programatically alter the attributes of components, overriding the values
 * specified in the markup. The two primary uses of this class are to allow
 * overriding of markup attributes based on business logic and to support
 * dynamic localization. The replacement occurs as the component tag is rendered
 * to the response.
 * <p>
 * The attribute whose value is to be modified must be given on construction of
 * the instance of this class along with the model containing the value to
 * replace with. Optionally a pattern can be supplied that is a regular
 * expression that the existing value must match before the replacement can be
 * carried out.
 * <p>
 * If an attribute is not in the markup, this modifier will add an attribute to
 * the tag only if addAttributeIfNotPresent is true and the replacement value is
 * not null.
 * </p>
 * <p>
 * Instances of this class should be added to components via the
 * {@link wicket.Component#add(AttributeModifier)} method after the component
 * has been constucted.
 * <p>
 * It is possible to create new subclasses of AttributeModifier by overriding
 * the newValue(String, String) method. For example, you could create an
 * AttributeModifier subclass which appends the replacement value like this:
 * <code>
 * 	new AttributeModifier("myAttribute", model)
 *  {
 * 		protected String newValue(final String currentValue, final String replacementValue)
 *      {
 *      	return currentValue + replacementValue;
 *      }
 *  };
 * </code>
 * 
 * @author Chris Turner
 * @author Eelco Hillenius
 * @author Jonathan Locke
 * @author Martijn Dashorst
 * @author Ralf Ebert
 */
public class AttributeModifier extends AbstractBehavior implements IClusterable
{
	/** Marker value to have an attribute without a value added. */
	public static final String VALUELESS_ATTRIBUTE_ADD = "VA_ADD";

	/** Marker value to have an attribute without a value removed. */
	public static final String VALUELESS_ATTRIBUTE_REMOVE = "VA_REMOVE";

	private static final long serialVersionUID = 1L;

	/** Whether to add the attribute if it is not an attribute in the markup. */
	private final boolean addAttributeIfNotPresent;

	/** Attribute specification. */
	private final String attribute;

	/** Modification information. */
	private boolean enabled;

	/** The pattern. */
	private final String pattern;

	/** The model that is to be used for the replacement. */
	private final IModel replaceModel;

	/**
	 * Create a new attribute modifier with the given attribute name and model
	 * to replace with. The additional boolean flag specifies whether to add the
	 * attribute if it is not present.
	 * 
	 * @param attribute
	 *            The attribute name to replace the value for
	 * @param addAttributeIfNotPresent
	 *            Whether to add the attribute if it is not present
	 * @param replaceModel
	 *            The model to replace the value with
	 */
	public AttributeModifier(final String attribute, final boolean addAttributeIfNotPresent,
			final IModel replaceModel)
	{
		this(attribute, null, addAttributeIfNotPresent, replaceModel);
	}

	/**
	 * Create a new attribute modifier with the given attribute name and model
	 * to replace with. The attribute will not be added if it is not present.
	 * 
	 * @param attribute
	 *            The attribute name to replace the value for
	 * @param replaceModel
	 *            The model to replace the value with
	 */
	public AttributeModifier(final String attribute, final IModel replaceModel)
	{
		this(attribute, null, false, replaceModel);
	}

	/**
	 * Create a new attribute modifier with the given attribute name and
	 * expected pattern to match plus the model to replace with. A null pattern
	 * will match the attribute regardless of its value. The additional boolean
	 * flag specifies whether to add the attribute if it is not present.
	 * 
	 * @param attribute
	 *            The attribute name to replace the value for
	 * @param pattern
	 *            The pattern of the current attribute value to match
	 * @param addAttributeIfNotPresent
	 *            Whether to add the attribute if it is not present and the
	 *            replacement value is not null
	 * @param replaceModel
	 *            The model to replace the value with
	 */
	public AttributeModifier(final String attribute, final String pattern,
			final boolean addAttributeIfNotPresent, final IModel replaceModel)
	{
		if (attribute == null)
		{
			throw new IllegalArgumentException("Attribute parameter cannot be null");
		}

		this.attribute = attribute;
		this.pattern = pattern;
		this.enabled = true;
		this.addAttributeIfNotPresent = addAttributeIfNotPresent;
		this.replaceModel = replaceModel;
	}

	/**
	 * Create a new attribute modifier with the given attribute name and
	 * expected pattern to match plus the model to replace with. A null pattern
	 * will match the attribute regardless of its value. The attribute will not
	 * be added if it is not present.
	 * 
	 * @param attribute
	 *            The attribute name to replace the value for
	 * @param pattern
	 *            The pattern of the current attribute value to match
	 * @param replaceModel
	 *            The model to replace the value with
	 */
	public AttributeModifier(final String attribute, final String pattern, final IModel replaceModel)
	{
		this(attribute, pattern, false, replaceModel);
	}

	/**
	 * Detach the model if it was a IDetachableModel Internal method. shouldn't
	 * be called from the outside. If the attribute modifier is shared, the
	 * detach method will be called multiple times.
	 * 
	 * @param component
	 *            the model that initiates the detachement
	 */
	public final void detachModel(Component component)
	{
		if (replaceModel != null)
		{
			replaceModel.detach();
		}
	}

	/**
	 * Made final to support the parameterless variant.
	 * 
	 * @see wicket.behavior.AbstractBehavior#isEnabled(wicket.Component)
	 */
	public boolean isEnabled(Component component)
	{
		return enabled;
	}

	/**
	 * Checks whether this attribute modifier is enabled or not.
	 * 
	 * @return Whether enabled or not
	 * @deprecated
	 */
	public final boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * @see wicket.behavior.IBehavior#onComponentTag(wicket.Component,
	 *      wicket.markup.ComponentTag)
	 */
	public final void onComponentTag(Component component, ComponentTag tag)
	{
		if (tag.getType() != XmlTag.CLOSE)
		{
			replaceAttibuteValue(component, tag);
		}
	}

	/**
	 * Checks the given component tag for an instance of the attribute to modify
	 * and if all criteria are met then replace the value of this attribute with
	 * the value of the contained model object.
	 * 
	 * @param component
	 *            The component
	 * @param tag
	 *            The tag to replace the attribute value for
	 */
	public final void replaceAttibuteValue(final Component component, final ComponentTag tag)
	{
		if (isEnabled(component))
		{
			final ValueMap attributes = tag.getAttributes();
			final Object replacementValue = getReplacementOrNull(component);

			if (VALUELESS_ATTRIBUTE_ADD.equals(replacementValue))
			{
				attributes.put(attribute, null);
			}
			else if (VALUELESS_ATTRIBUTE_REMOVE.equals(replacementValue))
			{
				attributes.remove(attribute);
			}
			else
			{
				if (attributes.containsKey(attribute))
				{
					final String value = toStringOrNull(attributes.get(attribute));
					if (pattern == null || value.matches(pattern))
					{
						final String newValue = newValue(value, toStringOrNull(replacementValue));
						if (newValue != null)
						{
							attributes.put(attribute, newValue);
						}
					}
				}
				else if (addAttributeIfNotPresent)
				{
					final String newValue = newValue(null, toStringOrNull(replacementValue));
					if (newValue != null)
					{
						attributes.put(attribute, newValue);
					}
				}
			}
		}
	}

	/**
	 * Sets whether this attribute modifier is enabled or not.
	 * 
	 * @param enabled
	 *            Whether enabled or not
	 */
	public final void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "[AttributeModifier attribute=" + attribute + ", enabled=" + enabled + ", pattern="
				+ pattern + ", replacementModel=" + replaceModel + "]";
	}

	/**
	 * Gets the replacement model. Allows subclasses access to replace model.
	 * 
	 * @return the replace model of this attribute modifier
	 */
	protected final IModel getReplaceModel()
	{
		return replaceModel;
	}

	/**
	 * Gets the value that should replace the current attribute value. This
	 * gives users the ultimate means to customize what will be used as the
	 * attribute value. For instance, you might decide to append the replacement
	 * value to the current instead of just replacing it as is Wicket's default.
	 * 
	 * @param currentValue
	 *            The current attribute value. This value might be null!
	 * @param replacementValue
	 *            The replacement value. This value might be null!
	 * @return The value that should replace the current attribute value
	 */
	protected String newValue(final String currentValue, final String replacementValue)
	{
		return replacementValue;
	}

	/* gets replacement with null check. */
	private Object getReplacementOrNull(final Component component)
	{
		return (replaceModel != null) ? replaceModel.getObject(component) : null;
	}

	/* gets replacement as a string with null check. */
	private String toStringOrNull(final Object replacementValue)
	{
		return (replacementValue != null) ? replacementValue.toString() : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3366.java