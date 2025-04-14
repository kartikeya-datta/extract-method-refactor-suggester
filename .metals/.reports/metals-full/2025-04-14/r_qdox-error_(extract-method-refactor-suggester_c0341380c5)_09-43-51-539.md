error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3369.java
text:
```scala
protected S@@tring propertyExpression()

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
package wicket.model;

import wicket.Component;
import wicket.util.lang.PropertyResolver;

/**
 * A PropertyModel is used to dynamically access a model using a "property
 * expression". See {@link PropertyResolver} javadoc for allowed property
 * expressions.
 * <p>
 * For example, take the following bean:
 * 
 * <pre>
 * public class Person
 * {
 * 	private String name;
 * 
 * 	public String getName()
 * 	{
 * 		return name;
 * 	}
 * 
 * 	public void setName(String name)
 * 	{
 * 		this.name = name;
 * 	}
 * }
 * </pre>
 * 
 * We could construct a label that dynamically fetches the name property of the
 * given person object like this:
 * 
 * <pre>
 *     Person person = getSomePerson();
 *     ...
 *     add(new Label(&quot;myLabel&quot;, new PopertyModel(person, &quot;name&quot;));
 * </pre>
 * 
 * Where 'myLabel' is the name of the component, and 'name' is the property
 * expression to get the name property.
 * </p>
 * <p>
 * In the same fashion, we can create form components that work dynamically on
 * the given model object. For instance, we could create a text field that
 * updates the name property of a person like this:
 * 
 * <pre>
 *     add(new TextField(&quot;myTextField&quot;, new PropertyModel(person, &quot;name&quot;));
 * </pre>
 * 
 * </p>
 * <p>
 * To force conversion of property value to a specific type, you can provide
 * constructor argument 'propertyType'. if that is set, that type is used for
 * conversion instead of the type that is figured out by
 * {@link PropertyResolver}. This can be especially useful for when you have a
 * generic property (like Serializable myProp) that you want to be converted to
 * a narrower type (e.g. an Integer). {@link PropertyResolver} sees an incomming
 * string being compatible with the target property, and will then bypass the
 * converter. Hence, to force myProp being converted to and from an integer,
 * propertyType should be set to Integer.
 * </p>
 * 
 * @see wicket.model.IModel
 * @see wicket.model.Model
 * @see wicket.model.AbstractDetachableModel
 * 
 * @author Chris Turner
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public class PropertyModel extends AbstractPropertyModel
{
	private static final long serialVersionUID = 1L;

	/** Property expression for property access. */
	private final String expression;

	/**
	 * If this is set, this type is used for conversion instead of the type that
	 * is figured out by the property expression code. This can be especially
	 * useful for when you have a generic property (like Serializable myProp)
	 * that you want to be converted to a narrower type (e.g. an Integer). The
	 * property expression code sees an incoming string being compatible with
	 * the target property, and will then bypass the converter. Hence, to force
	 * myProp being converted to and from an integer, propertyType should be set
	 * to Integer.
	 */
	private final Class propertyType;

	/**
	 * Construct with a wrapped (IModel) or unwrapped (non-IModel) object and a
	 * property expression that works on the given model. Additional formatting
	 * will be used depending on the configuration setting.
	 * 
	 * @param modelObject
	 *            The model object, which may or may not implement IModel
	 * @param expression
	 *            Property expression for property access
	 */
	public PropertyModel(final Object modelObject, final String expression)
	{
		this(modelObject, expression, null);
	}

	/**
	 * Construct with a wrapped (IModel) or unwrapped (non-IModel) object and a
	 * property expression that works on the given model. Additional formatting
	 * will be used depending on the configuration setting.
	 * 
	 * @param modelObject
	 *            The model object, which may or may not implement IModel
	 * @param expression
	 *            Property expression for property access
	 * @param propertyType
	 *            The type to be used for conversion instead of the type that is
	 *            figured out by the property expression code. This can be
	 *            especially useful for when you have a generic property (like
	 *            Serializable myProp) that you want to be converted to a
	 *            narrower type (e.g. an Integer). The property expression code
	 *            sees an incoming string being compatible with the target
	 *            property, and will then bypass the converter. Hence, to force
	 *            myProp being converted to and from an integer, propertyType
	 *            should be set to Integer.
	 */
	public PropertyModel(final Object modelObject, final String expression, Class propertyType)
	{
		super(modelObject);
		this.expression = expression;
		this.propertyType = propertyType;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append(":expression=[").append(expression).append("]");
		sb.append(":propertyType=[").append(propertyType).append("]");
		return sb.toString();
	}

	/**
	 * @see wicket.model.AbstractPropertyModel#propertyExpression(wicket.Component)
	 */
	protected String propertyExpression(Component component)
	{
		return expression;
	}

	/**
	 * @see wicket.model.AbstractPropertyModel#propertyType(wicket.Component)
	 */
	protected Class propertyType(Component component)
	{
		return propertyType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3369.java