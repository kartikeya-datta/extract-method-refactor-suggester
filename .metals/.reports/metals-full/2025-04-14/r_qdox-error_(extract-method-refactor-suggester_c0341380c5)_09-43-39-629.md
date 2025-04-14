error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9321.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9321.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9321.java
text:
```scala
m@@odel.getObject(null);

package wicket.model;

import java.util.Properties;

import wicket.Component;
import wicket.WicketTestCase;
import wicket.markup.html.basic.Label;

/**
 * Tests the toString() method on the models in the wicket.model package.
 */
public class ModelToStringTest extends WicketTestCase
{
	/**
	 * Construct.
	 * @param name
	 */
	public ModelToStringTest(String name)
	{
		super(name);
	}

	/**
	 * Used for models in testing.
	 */
	private static class InnerPOJO
	{
	}

	/**
	 * Tests the Model.toString() method.
	 */
	public void testModel()
	{
		Model emptyModel = new Model();
		String expected = "Model:classname=[wicket.model.Model]:nestedModel=[null]:object=[null]";
		assertEquals(expected, emptyModel.toString());

		Model stringModel = new Model("foo");
		expected = "Model:classname=[wicket.model.Model]:nestedModel=[null]:object=[foo]";
		assertEquals(expected, stringModel.toString());
	}

	/**
	 * Tests the PropertyModel.toString() method.
	 */
	public void testPropertyModel()
	{
		PropertyModel emptyModel = new PropertyModel("", null);
		String expected = "Model:classname=[wicket.model.PropertyModel]:attached=false:nestedModel=[]:expression=[null]:propertyType=[null]";
		assertEquals(expected, emptyModel.toString());

		Properties properties = new Properties();
		properties.put("name", "foo");
		PropertyModel stringProperty = new PropertyModel(properties, "name");

		expected = "Model:classname=[wicket.model.PropertyModel]:attached=false:nestedModel=[{name=foo}]:expression=[name]:propertyType=[null]";
		assertEquals(expected, stringProperty.toString());

		stringProperty.attach();
		expected = "Model:classname=[wicket.model.PropertyModel]:attached=true:nestedModel=[{name=foo}]:expression=[name]:propertyType=[null]";
		assertEquals(expected, stringProperty.toString());

		InnerPOJO innerPOJO = new InnerPOJO();
		PropertyModel pojoProperty = new PropertyModel(innerPOJO, "pojo", Integer.class);

		expected = "Model:classname=[wicket.model.PropertyModel]:attached=false:nestedModel=["
				+ innerPOJO + "]:expression=[pojo]:propertyType=[class java.lang.Integer]";
		assertEquals(expected, pojoProperty.toString());
	}

	/**
	 * Tests the CompoundPropertyModel.toString() method.
	 */
	public void testCompoundPropertyModel()
	{
		CompoundPropertyModel emptyModel = new CompoundPropertyModel("");
		String expected = "Model:classname=[wicket.model.CompoundPropertyModel]:attached=false:nestedModel=[]";
		assertEquals(expected, emptyModel.toString());

		Properties properties = new Properties();
		properties.put("name", "foo");
		CompoundPropertyModel stringProperty = new CompoundPropertyModel(properties);

		expected = "Model:classname=[wicket.model.CompoundPropertyModel]:attached=false:nestedModel=[{name=foo}]";
		assertEquals(expected, stringProperty.toString());

		stringProperty.attach();
		expected = "Model:classname=[wicket.model.CompoundPropertyModel]:attached=true:nestedModel=[{name=foo}]";
		assertEquals(expected, stringProperty.toString());

		InnerPOJO innerPOJO = new InnerPOJO();
		CompoundPropertyModel pojoProperty = new CompoundPropertyModel(innerPOJO);

		expected = "Model:classname=[wicket.model.CompoundPropertyModel]:attached=false:nestedModel=["
				+ innerPOJO + "]";
		assertEquals(expected, pojoProperty.toString());
	}

	/**
	 * Tests the BoundCompoundPropertyModel.toString() method.
	 */
	public void testBoundCompoundPropertyModel()
	{
		BoundCompoundPropertyModel emptyModel = new BoundCompoundPropertyModel("");
		String expected = "Model:classname=[wicket.model.BoundCompoundPropertyModel]:attached=false:nestedModel=[]:bindings=[]";
		assertEquals(expected, emptyModel.toString());

		Properties properties = new Properties();
		properties.put("name", "foo");
		BoundCompoundPropertyModel stringProperty = new BoundCompoundPropertyModel(properties);

		expected = "Model:classname=[wicket.model.BoundCompoundPropertyModel]:attached=false:nestedModel=[{name=foo}]:bindings=[]";
		assertEquals(expected, stringProperty.toString());

		stringProperty.attach();
		expected = "Model:classname=[wicket.model.BoundCompoundPropertyModel]:attached=true:nestedModel=[{name=foo}]:bindings=[]";
		assertEquals(expected, stringProperty.toString());

		InnerPOJO innerPOJO = new InnerPOJO();
		BoundCompoundPropertyModel pojoProperty = new BoundCompoundPropertyModel(innerPOJO);

		expected = "Model:classname=[wicket.model.BoundCompoundPropertyModel]:attached=false:nestedModel=["
				+ innerPOJO + "]:bindings=[]";
		assertEquals(expected, pojoProperty.toString());

		Component component1 = pojoProperty.bind(new Label("label"), Integer.class);
		expected = "Model:classname=[wicket.model.BoundCompoundPropertyModel]:attached=false:nestedModel=["
				+ innerPOJO
				+ "]:bindings=[Binding(:component=["
				+ component1
				+ "]:expression=[label]:type=[class java.lang.Integer])]";
		assertEquals(expected, pojoProperty.toString());
	}

	/**
	 * Test stub for testing AbstractReadOnlyModel.toString()
	 */
	private static class MyAbstractReadOnlyModel extends AbstractReadOnlyModel
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @see AbstractReadOnlyModel#getObject(Component)
		 */
		public Object getObject(Component component)
		{
			return "FOO";
		}
	}

	/**
	 * Tests AbstractReadOnlyModel.toString().
	 */
	public void testAbstractReadOnlyModel()
	{
		AbstractReadOnlyModel model = new MyAbstractReadOnlyModel();
		String expected = "Model:classname=[" + model.getClass().getName() + "]";
		assertEquals(expected, model.toString());
	}

	/**
	 * Test stub for testing AbstractReadOnlyDetachableModel.toString()
	 */
	private static class MyAbstractReadOnlyDetachableModel extends AbstractReadOnlyDetachableModel
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @return <code>null</code>
		 * @see Model#getNestedModel()
		 */
		public IModel getNestedModel()
		{
			return null;
		}

		protected void onAttach()
		{
		}

		protected void onDetach()
		{
		}

		protected Object onGetObject(Component component)
		{
			return null;
		}
	}

	/**
	 * Tests AbstractReadOnlyModel.toString().
	 */
	public void testAbstractReadOnlyDetachableModel()
	{
		AbstractReadOnlyDetachableModel model = new MyAbstractReadOnlyDetachableModel();
		String expected = "Model:classname=[" + model.getClass().getName() + "]"
				+ ":attached=false";
		assertEquals(expected, model.toString());
	}

	private static final class MyLoadableDetachableModel extends LoadableDetachableModel
	{
		private static final long serialVersionUID = 1L;

		protected Object load()
		{
			return "foo";
		}
	}

	/**
	 * Tests LoadableDetachableModel.toString()
	 */
	public void testLoadableDetachableModel()
	{
		LoadableDetachableModel model = new MyLoadableDetachableModel();
		String expected = "Model:classname=[" + model.getClass().getName() + "]"
				+ ":attached=false" + ":tempModelObject=[null]";
		assertEquals(expected, model.toString());

		model.attach();
		expected = "Model:classname=[" + model.getClass().getName() + "]" + ":attached=true"
				+ ":tempModelObject=[foo]";
		assertEquals(expected, model.toString());

		model.detach();
		expected = "Model:classname=[" + model.getClass().getName() + "]" + ":attached=false"
				+ ":tempModelObject=[null]";
		assertEquals(expected, model.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9321.java