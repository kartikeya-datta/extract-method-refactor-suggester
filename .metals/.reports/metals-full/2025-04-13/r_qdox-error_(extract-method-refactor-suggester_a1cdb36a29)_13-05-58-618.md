error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15591.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15591.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15591.java
text:
```scala
n@@ew WebComponent(new MockPageWithOneComponent(), "component");

/*
 * $Id: AuthorizationTest.java 5844 2006-05-24 20:53:56 +0000 (Wed, 24 May 2006)
 * joco01 $ $Revision$ $Date: 2006-05-24 20:53:56 +0000 (Wed, 24 May
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

package wicket;

import java.io.Serializable;

import junit.framework.TestCase;
import wicket.authorization.Action;
import wicket.authorization.AuthorizationException;
import wicket.authorization.IAuthorizationStrategy;
import wicket.markup.html.WebComponent;
import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.model.CompoundPropertyModel;
import wicket.util.tester.WicketTester;

/**
 * Authorization tests.
 * 
 * @author hillenius
 */
public class AuthorizationTest extends TestCase
{
	/**
	 * Construct.
	 */
	public AuthorizationTest()
	{
		super();
	}

	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public AuthorizationTest(String name)
	{
		super(name);
	}

	/**
	 * Sets up this test.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Tests that a component can be created when authorization is allowed.
	 * 
	 * @throws Exception
	 */
	public void testCreateAllowedComponent() throws Exception
	{
		new WicketTester();
		new WebComponent(new EmptyPage(), "test");
	}

	/**
	 * Tests that a component cannot be created when authorization is not
	 * allowed.
	 * 
	 * @throws Exception
	 */
	public void testCreateDisallowedComponent() throws Exception
	{
		WicketTester app = new WicketTester();
		app.getSecuritySettings().setAuthorizationStrategy(new DummyAuthorizationStrategy()
		{
			@Override
			public boolean isInstantiationAuthorized(Class c)
			{
				return false;
			}
		});
		try
		{
			new WebComponent(new EmptyPage(), "test");
			// bad: authorization should have failed
			fail("authorization check failed to throw an exception");
		}
		catch (AuthorizationException e)
		{
			// this is good: authorization should have failed
		}
	}

	/**
	 * Test that a component will be rendered when authorization is ok.
	 * 
	 * @throws Exception
	 */
	public void testRenderAllowedComponent() throws Exception
	{
		WicketTester app = new WicketTester();
		app.getSecuritySettings().setAuthorizationStrategy(new DummyAuthorizationStrategy());

		app.startPage(AuthTestPage1.class);
		app.assertRenderedPage(AuthTestPage1.class);
		app.assertLabel("label", "wicked!");
	}

	/**
	 * Test that a component will be rendered when authorization is ok.
	 * 
	 * @throws Exception
	 */
	public void testRenderDisallowedComponent() throws Exception
	{
		WicketTester app = new WicketTester();
		app.getSecuritySettings().setAuthorizationStrategy(new DummyAuthorizationStrategy()
		{
			/**
			 * @see wicket.authorization.IAuthorizationStrategy#isActionAuthorized(wicket.Component,
			 *      wicket.authorization.Action)
			 */
			@Override
			public boolean isActionAuthorized(Component component, Action action)
			{
				if (action == Component.RENDER && component instanceof Label)
				{
					return false;
				}
				return true;
			}
		});
		app.startPage(AuthTestPage1.class);
		app.assertRenderedPage(AuthTestPage1.class);
		app.assertInvisible("label");
	}

	/**
	 * Test that a component will update it's model when authorization is ok.
	 * 
	 * @throws Exception
	 */
	public void testEnabledAllowedComponent() throws Exception
	{
		WicketTester app = new WicketTester();
		app.getSecuritySettings().setAuthorizationStrategy(new DummyAuthorizationStrategy());

		app.startPage(AuthTestPage1.class);
		app.assertRenderedPage(AuthTestPage1.class);
		app.setParameterForNextRequest("form:stringInput", "test");
		app.submitForm("form");
		app.assertRenderedPage(AuthTestPage1.class);
		AuthTestPage1 page = (AuthTestPage1)app.getLastRenderedPage();
		assertTrue(page.isSubmitted());
		Input input = page.getTestModel();
		assertNotNull(input.getStringInput());
		assertEquals("test", input.getStringInput());
	}

	/**
	 * Test that a component will update it's model when authorization is ok.
	 * 
	 * @throws Exception
	 */
	public void testEnabledDisallowedComponent() throws Exception
	{
		WicketTester app = new WicketTester();
		app.getSecuritySettings().setAuthorizationStrategy(new DummyAuthorizationStrategy()
		{
			/**
			 * @see wicket.authorization.IAuthorizationStrategy#isActionAuthorized(wicket.Component,
			 *      wicket.authorization.Action)
			 */
			@Override
			public boolean isActionAuthorized(Component c, Action action)
			{
				if (action == Component.ENABLE && c instanceof TextField
						&& c.getId().equals("stringInput"))
				{
					return false;
				}
				return true;
			}
		});
		app.startPage(AuthTestPage1.class);
		app.assertRenderedPage(AuthTestPage1.class);
		app.setParameterForNextRequest("form:stringInput", "test");
		try
		{
			app.submitForm("form");
			Component component = app.getComponentFromLastRenderedPage("form:stringInput");
			assertEquals("", component.getModelObjectAsString());
		}
		catch (WicketRuntimeException e)
		{
			// good
		}

	}

	/**
	 * noop strategy so we don't have to implement the whole interface every
	 * time.
	 */
	private static class DummyAuthorizationStrategy implements IAuthorizationStrategy
	{
		/**
		 * @see wicket.authorization.IAuthorizationStrategy#isInstantiationAuthorized(java.lang.Class)
		 */
		public boolean isInstantiationAuthorized(Class c)
		{
			return true;
		}

		/**
		 * @see wicket.authorization.IAuthorizationStrategy#isActionAuthorized(
		 *      wicket.Component, wicket.authorization.Action)
		 */
		public boolean isActionAuthorized(Component c, Action action)
		{
			return true;
		}
	}

	/**
	 * Test page for authentication tests.
	 */
	public static class AuthTestPage1 extends WebPage
	{
		private static final long serialVersionUID = 1L;

		private Input input;

		private boolean submitted = false;

		/**
		 * Construct.
		 */
		public AuthTestPage1()
		{
			new Label(this, "label", "wicked!");
			new TestForm(this, "form");

		}

		/**
		 * Gets the test model.
		 * 
		 * @return the test model
		 */
		public Input getTestModel()
		{
			return input;
		}

		/**
		 * Gets whether the form was submitted.
		 * 
		 * @return whether the form was submitted
		 */
		public boolean isSubmitted()
		{
			return submitted;
		}

		/** test form. */
		private class TestForm extends Form<Input>
		{
			private static final long serialVersionUID = 1L;

			/**
			 * Construct.
			 * @param parent
			 * @param id
			 */
			public TestForm(MarkupContainer parent, String id)
			{
				super(parent, id);
				setModel(new CompoundPropertyModel<Input>(input = new Input()));
				new TextField(this, "stringInput");
			}

			/**
			 * @see wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit()
			{
				submitted = true;
			}
		}
	}

	/** simple input holder. */
	private static class Input implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private String stringInput;

		/**
		 * Gets stringInput.
		 * 
		 * @return stringInput
		 */
		public String getStringInput()
		{
			return stringInput;
		}

		/**
		 * Sets stringInput.
		 * 
		 * @param stringInput
		 *            stringInput
		 */
		public void setStringInput(String stringInput)
		{
			this.stringInput = stringInput;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15591.java