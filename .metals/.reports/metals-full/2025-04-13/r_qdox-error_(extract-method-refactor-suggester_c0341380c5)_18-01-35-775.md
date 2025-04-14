error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18145.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18145.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18145.java
text:
```scala
C@@ompoundPropertyModel<Properties> stringProperty = new CompoundPropertyModel<Properties>(properties);

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
package org.apache.wicket.model;

import java.util.Properties;

import org.apache.wicket.WicketTestCase;


/**
 * Tests the toString() method on the models in the org.apache.wicket.model package.
 */
public class ModelToStringTest extends WicketTestCase
{
	/**
	 * Used for models in testing.
	 */
	private static class InnerPOJO
	{
		@Override
		public String toString()
		{
			return "pojo";
		}
	}

	/**
	 * Test stub for testing AbstractReadOnlyModel.toString()
	 */
	private static class MyAbstractReadOnlyModel extends AbstractReadOnlyModel<String>
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @see AbstractReadOnlyModel#getObject()
		 */
		@Override
		public String getObject()
		{
			return "FOO";
		}
	}

	private static final class MyLoadableDetachableModel extends LoadableDetachableModel<String>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected String load()
		{
			return "foo";
		}
	}

	/**
	 * Tests AbstractReadOnlyModel.toString().
	 */
	public void testAbstractReadOnlyModel()
	{
		AbstractReadOnlyModel<String> model = new MyAbstractReadOnlyModel();
		String expected = "Model:classname=[" + model.getClass().getName() + "]";
		assertEquals(expected, model.toString());
	}

	/**
	 * Tests the BoundCompoundPropertyModel.toString() method.
	 */
	public void testBoundCompoundPropertyModel()
	{
		CompoundPropertyModel<String> emptyModel = new CompoundPropertyModel<String>("");
		String expected = "Model:classname=[org.apache.wicket.model.CompoundPropertyModel]:nestedModel=[]";
		assertEquals(expected, emptyModel.toString());

		Properties properties = new Properties();
		properties.put("name", "foo");
		CompoundPropertyModel<String> stringProperty = new CompoundPropertyModel<String>(properties);

		expected = "Model:classname=[org.apache.wicket.model.CompoundPropertyModel]:nestedModel=[{name=foo}]";
		assertEquals(expected, stringProperty.toString());

		stringProperty.getObject();
		expected = "Model:classname=[org.apache.wicket.model.CompoundPropertyModel]:nestedModel=[{name=foo}]";
		assertEquals(expected, stringProperty.toString());

		InnerPOJO innerPOJO = new InnerPOJO();
		CompoundPropertyModel<InnerPOJO> pojoProperty = new CompoundPropertyModel<InnerPOJO>(
			innerPOJO);

		expected = "Model:classname=[org.apache.wicket.model.CompoundPropertyModel]:nestedModel=[" +
			innerPOJO + "]";
		assertEquals(expected, pojoProperty.toString());
	}

	/**
	 * Tests the CompoundPropertyModel.toString() method.
	 */
	public void testCompoundPropertyModel()
	{
		CompoundPropertyModel<?> emptyModel = new CompoundPropertyModel<String>("");
		String expected = "Model:classname=[org.apache.wicket.model.CompoundPropertyModel]:nestedModel=[]";
		assertEquals(expected, emptyModel.toString());

		Properties properties = new Properties();
		properties.put("name", "foo");
		CompoundPropertyModel<Properties> stringProperty = new CompoundPropertyModel<Properties>(
			properties);

		expected = "Model:classname=[org.apache.wicket.model.CompoundPropertyModel]:nestedModel=[{name=foo}]";
		assertEquals(expected, stringProperty.toString());

		stringProperty.getObject();
		expected = "Model:classname=[org.apache.wicket.model.CompoundPropertyModel]:nestedModel=[{name=foo}]";
		assertEquals(expected, stringProperty.toString());

		InnerPOJO innerPOJO = new InnerPOJO();
		CompoundPropertyModel<InnerPOJO> pojoProperty = new CompoundPropertyModel<InnerPOJO>(
			innerPOJO);

		expected = "Model:classname=[org.apache.wicket.model.CompoundPropertyModel]:nestedModel=[" +
			innerPOJO + "]";
		assertEquals(expected, pojoProperty.toString());
	}

	/**
	 * Tests LoadableDetachableModel.toString()
	 */
	public void testLoadableDetachableModel()
	{
		LoadableDetachableModel<String> model = new MyLoadableDetachableModel();
		assertTrue(model.toString().contains(":attached=false"));
		assertTrue(model.toString().contains(":tempModelObject=[null]"));

		model.getObject();
		assertTrue(model.toString().contains(":attached=true"));
		assertTrue(model.toString().contains(":tempModelObject=[foo]"));

		model.detach();
		assertTrue(model.toString().contains(":attached=false"));
		assertTrue(model.toString().contains(":tempModelObject=[null]"));
	}


	/**
	 * Tests the Model.toString() method.
	 */
	public void testModel()
	{
		Model<?> emptyModel = new Model<String>();
		String expected = "Model:classname=[org.apache.wicket.model.Model]:object=[null]";
		assertEquals(expected, emptyModel.toString());

		Model<String> stringModel = new Model<String>("foo");
		expected = "Model:classname=[org.apache.wicket.model.Model]:object=[foo]";
		assertEquals(expected, stringModel.toString());
	}

	/**
	 * Tests the PropertyModel.toString() method.
	 */
	public void testPropertyModel()
	{
		PropertyModel emptyModel = new PropertyModel("", null);
		String expected = "Model:classname=[org.apache.wicket.model.PropertyModel]:nestedModel=[]:expression=[null]";
		assertEquals(expected, emptyModel.toString());

		Properties properties = new Properties();
		properties.put("name", "foo");
		PropertyModel<String> stringProperty = new PropertyModel<String>(properties, "name");

		expected = "Model:classname=[org.apache.wicket.model.PropertyModel]:nestedModel=[{name=foo}]:expression=[name]";
		assertEquals(expected, stringProperty.toString());

		stringProperty.getObject();
		expected = "Model:classname=[org.apache.wicket.model.PropertyModel]:nestedModel=[{name=foo}]:expression=[name]";
		assertEquals(expected, stringProperty.toString());

		InnerPOJO innerPOJO = new InnerPOJO();
		PropertyModel<?> pojoProperty = new PropertyModel<Object>(innerPOJO, "pojo");

		expected = "Model:classname=[org.apache.wicket.model.PropertyModel]:nestedModel=[pojo]:expression=[pojo]";
		assertEquals(expected, pojoProperty.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18145.java