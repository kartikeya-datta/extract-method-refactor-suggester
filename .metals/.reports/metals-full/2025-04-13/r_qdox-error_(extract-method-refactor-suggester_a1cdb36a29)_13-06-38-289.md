error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5500.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5500.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5500.java
text:
```scala
p@@vs.add("portletClass", MyPortlet.class);

/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.portlet.mvc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.mock.web.portlet.MockActionResponse;
import org.springframework.mock.web.portlet.MockPortletConfig;
import org.springframework.mock.web.portlet.MockPortletContext;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.web.portlet.context.ConfigurablePortletApplicationContext;
import org.springframework.web.portlet.context.StaticPortletApplicationContext;

/**
 * Unit tests for the {@link PortletWrappingController} class.
 *
 * @author Mark Fisher
 * @author Rick Evans
 * @author Chris Beams
 */
public final class PortletWrappingControllerTests {

	private static final String RESULT_RENDER_PARAMETER_NAME = "result";
	private static final String PORTLET_WRAPPING_CONTROLLER_BEAN_NAME = "controller";
	private static final String RENDERED_RESPONSE_CONTENT = "myPortlet-view";
	private static final String PORTLET_NAME_ACTION_REQUEST_PARAMETER_NAME = "portletName";


	private PortletWrappingController controller;


	@Before
	public void setUp() {
		ConfigurablePortletApplicationContext applicationContext = new MyApplicationContext();
		MockPortletConfig config = new MockPortletConfig(new MockPortletContext(), "wrappedPortlet");
		applicationContext.setPortletConfig(config);
		applicationContext.refresh();
		controller = (PortletWrappingController) applicationContext.getBean(PORTLET_WRAPPING_CONTROLLER_BEAN_NAME);
	}


	@Test
	public void testActionRequest() throws Exception {
		MockActionRequest request = new MockActionRequest();
		MockActionResponse response = new MockActionResponse();
		request.setParameter("test", "test");
		controller.handleActionRequest(request, response);
		String result = response.getRenderParameter(RESULT_RENDER_PARAMETER_NAME);
		assertEquals("myPortlet-action", result);
	}

	@Test
	public void testRenderRequest() throws Exception {
		MockRenderRequest request = new MockRenderRequest();
		MockRenderResponse response = new MockRenderResponse();
		controller.handleRenderRequest(request, response);
		String result = response.getContentAsString();
		assertEquals(RENDERED_RESPONSE_CONTENT, result);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testActionRequestWithNoParameters() throws Exception {
		MockActionRequest request = new MockActionRequest();
		MockActionResponse response = new MockActionResponse();
		controller.handleActionRequest(request, response);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRejectsPortletClassThatDoesNotImplementPortletInterface() throws Exception {
		PortletWrappingController controller = new PortletWrappingController();
		controller.setPortletClass(String.class);
		controller.afterPropertiesSet();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRejectsIfPortletClassIsNotSupplied() throws Exception {
		PortletWrappingController controller = new PortletWrappingController();
		controller.setPortletClass(null);
		controller.afterPropertiesSet();
	}

	@Test(expected=IllegalStateException.class)
	public void testDestroyingTheControllerPropagatesDestroyToWrappedPortlet() throws Exception {
		final PortletWrappingController controller = new PortletWrappingController();
		controller.setPortletClass(MyPortlet.class);
		controller.afterPropertiesSet();
		// test for destroy() call being propagated via exception being thrown :(
		controller.destroy();
	}

	@Test
	public void testPortletName() throws Exception {
		MockActionRequest request = new MockActionRequest();
		MockActionResponse response = new MockActionResponse();
		request.setParameter(PORTLET_NAME_ACTION_REQUEST_PARAMETER_NAME, "test");
		controller.handleActionRequest(request, response);
		String result = response.getRenderParameter(RESULT_RENDER_PARAMETER_NAME);
		assertEquals("wrappedPortlet", result);
	}

	@Test
	public void testDelegationToMockPortletConfigIfSoConfigured() throws Exception {

		final String BEAN_NAME = "Sixpence None The Richer";

		MockActionRequest request = new MockActionRequest();
		MockActionResponse response = new MockActionResponse();

		PortletWrappingController controller = new PortletWrappingController();
		controller.setPortletClass(MyPortlet.class);
		controller.setUseSharedPortletConfig(false);
		controller.setBeanName(BEAN_NAME);
		controller.afterPropertiesSet();

		request.setParameter(PORTLET_NAME_ACTION_REQUEST_PARAMETER_NAME, "true");
		controller.handleActionRequest(request, response);

		String result = response.getRenderParameter(RESULT_RENDER_PARAMETER_NAME);
		assertEquals(BEAN_NAME, result);
	}


	public static final class MyPortlet implements Portlet {

		private PortletConfig portletConfig;


		public void init(PortletConfig portletConfig) {
			this.portletConfig = portletConfig;
		}

		public void processAction(ActionRequest request, ActionResponse response) throws PortletException {
			if (request.getParameter("test") != null) {
				response.setRenderParameter(RESULT_RENDER_PARAMETER_NAME, "myPortlet-action");
			} else if (request.getParameter(PORTLET_NAME_ACTION_REQUEST_PARAMETER_NAME) != null) {
				response.setRenderParameter(RESULT_RENDER_PARAMETER_NAME, getPortletConfig().getPortletName());
			} else {
				throw new IllegalArgumentException("no request parameters");
			}
		}

		public void render(RenderRequest request, RenderResponse response) throws IOException {
			response.getWriter().write(RENDERED_RESPONSE_CONTENT);
		}

		public PortletConfig getPortletConfig() {
			return this.portletConfig;
		}

		public void destroy() {
			throw new IllegalStateException("Being destroyed...");
		}

	}

	private static final class MyApplicationContext extends StaticPortletApplicationContext {

		public void refresh() throws BeansException {
			MutablePropertyValues pvs = new MutablePropertyValues();
			pvs.addPropertyValue("portletClass", MyPortlet.class);
			registerSingleton(PORTLET_WRAPPING_CONTROLLER_BEAN_NAME, PortletWrappingController.class, pvs);
			super.refresh();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5500.java