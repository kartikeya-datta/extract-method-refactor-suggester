error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2750.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2750.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2750.java
text:
```scala
final F@@orm form = new Form<MockModelObject>(page, "form", new CompoundPropertyModel<MockModelObject>(modelObject));

/*
 * $Id: CheckGroupTest.java 5844 2006-05-24 20:53:56 +0000 (Wed, 24 May 2006)
 * joco01 $ $Revision$ $Date: 2006-05-25 22:52:19 +0000 (Thu, 25 May
 * 2006) $
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

package wicket.markup.html.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wicket.EmptyPage;
import wicket.RequestCycle;
import wicket.WicketRuntimeException;
import wicket.WicketTestCase;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;
import wicket.protocol.http.MockPage;

/**
 * Test for RadioGroup and Radio components
 * 
 * @author igor
 * 
 */
public class CheckGroupTest extends WicketTestCase
{

	/**
	 * @param name
	 */
	public CheckGroupTest(String name)
	{
		super(name);
	}

	/**
	 * mock model object with an embedded property used to test compound
	 * property model
	 * 
	 * @author igor
	 * 
	 */
	public static class MockModelObject implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private Set<String> prop1 = new HashSet<String>();
		private String prop2;

		/**
		 * @return prop1
		 */
		public Set<String> getProp1()
		{
			return prop1;
		}

		/**
		 * @param prop1
		 */
		public void setProp1(Set<String> prop1)
		{
			this.prop1 = prop1;
		}

		/**
		 * @return prop2
		 */
		public String getProp2()
		{
			return prop2;
		}

		/**
		 * @param prop2
		 */
		public void setProp2(String prop2)
		{
			this.prop2 = prop2;
		}


	}

	/**
	 * test component form processing
	 */
	public void testFormProcessing()
	{
		// setup some values we will use for testing as well as a test model
		final String check1 = "check1-selection";
		final String check2 = "check2-selection";

		MockModelObject modelObject = new MockModelObject();
		modelObject.setProp2(check2);

		// test model constructors
		List<MockModelObject>list = new ArrayList<MockModelObject>();
		Model<Collection<MockModelObject>> model = new Model<Collection<MockModelObject>>(list);

		final CheckGroup group2 = new CheckGroup<MockModelObject>(new EmptyPage(), "group2", model);
		assertTrue(group2.getModelObject() == list);

		final CheckGroup group3 = new CheckGroup<MockModelObject>(new EmptyPage(), "group3", list);
		assertTrue(group3.getModelObject() == list);


		// set up necessary objects to emulate a form submission

		RequestCycle cycle = application.createRequestCycle();

		MockPage page = new MockPage();

		// create component hierarchy

		final Form form = new Form(page, "form", new CompoundPropertyModel(modelObject));

		final CheckGroup group = new CheckGroup(form, "prop1");

		final WebMarkupContainer container = new WebMarkupContainer(group, "container");

		final Check<String> choice1 = new Check<String>(container, "check1", new Model<String>(check1));
		final Check choice2 = new Check(group, "prop2");


		// test mock form submissions

		modelObject.getProp1().add(check1);

		form.onFormSubmitted();
		assertTrue("running with nothing selected - model must be empty", modelObject.getProp1()
				.size() == 0);

		application.getServletRequest().setParameter(group.getInputName(), choice1.getPath());
		form.onFormSubmitted();
		assertTrue("running with choice1 selected - model must only contain value of check1",
				modelObject.getProp1().size() == 1 && modelObject.getProp1().contains(check1));

		application.getServletRequest().setParameter(group.getInputName(), choice2.getPath());
		form.onFormSubmitted();
		assertTrue("running with choice2 selected - model must only contain value of check2",
				modelObject.getProp1().size() == 1 && modelObject.getProp1().contains(check2));

		// throw in some nulls into the request param to make sure they are
		// ignored
		application.getServletRequest().getParameterMap().put(group.getInputName(),
				new String[] { null, choice1.getPath(), null, choice2.getPath() });
		form.onFormSubmitted();
		assertTrue(
				"running with choice1 and choice2 selected - model must only contain values of check1 and check2",
				modelObject.getProp1().size() == 2 && modelObject.getProp1().contains(check2)
						&& modelObject.getProp1().contains(check1));

		application.getServletRequest().getParameterMap().put(group.getInputName(),
				new String[] { "some weird path to test error" });
		try
		{
			form.onFormSubmitted();
			fail("running with an invalid choice value in the request param, should fail");
		}
		catch (WicketRuntimeException e)
		{

		}


	}

	/**
	 * test component rendering
	 * 
	 * @throws Exception
	 */
	public void testRendering() throws Exception
	{
		executeTest(CheckGroupTestPage1.class, "CheckGroupTestPage1_expected.html");
		executeTest(CheckGroupTestPage2.class, "CheckGroupTestPage2_expected.html");
		executeTest(CheckGroupTestPage3.class, "CheckGroupTestPage3_expected.html");
		executeTest(CheckGroupTestPage4.class, "CheckGroupTestPage4_expected.html");
		try
		{
			executeTest(CheckGroupTestPage5.class, "");
			fail("this will always fail");
		}
		catch (WicketRuntimeException e)
		{
			if (e.getMessage().indexOf(
					"Check component [4:form:check2] cannot find its parent CheckGroup") < 0)
			{
				fail("failed with wrong exception");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2750.java