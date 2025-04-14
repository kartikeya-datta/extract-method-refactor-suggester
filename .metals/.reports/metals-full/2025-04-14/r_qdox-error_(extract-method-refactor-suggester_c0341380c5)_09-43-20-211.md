error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9342.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9342.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9342.java
text:
```scala
M@@ap<String, Class<?>> toolAttributes = new HashMap<String, Class<?>>();

/*
 * Copyright 2002-2013 the original author or authors.
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

package org.springframework.web.servlet.view.velocity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.junit.Test;
import org.springframework.context.ApplicationContextException;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.mock.web.test.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.springframework.web.servlet.view.AbstractView;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Dave Syer
 */
public class VelocityViewTests {

	@Test
	public void testNoVelocityConfig() throws Exception {
		VelocityView vv = new VelocityView();
		WebApplicationContext wac = mock(WebApplicationContext.class);
		given(wac.getBeansOfType(VelocityConfig.class, true, false)).willReturn(new HashMap<String, VelocityConfig>());

		vv.setUrl("anythingButNull");
		try {
			vv.setApplicationContext(wac);
			fail();
		}
		catch (ApplicationContextException ex) {
			// Check there's a helpful error message
			assertTrue(ex.getMessage().contains("VelocityConfig"));
		}
	}

	@Test
	public void testNoTemplateName() throws Exception {
		VelocityView vv = new VelocityView();
		try {
			vv.afterPropertiesSet();
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// Check there's a helpful error message
			assertTrue(ex.getMessage().indexOf("url") != -1);
		}
	}

	@Test
	public void testMergeTemplateSucceeds() throws Exception {
		testValidTemplateName(null);
	}

	@Test
	public void testMergeTemplateFailureWithIOException() throws Exception {
		testValidTemplateName(new IOException());
	}

	@Test
	public void testMergeTemplateFailureWithParseErrorException() throws Exception {
		testValidTemplateName(new ParseErrorException(""));
	}

	@Test
	public void testMergeTemplateFailureWithUnspecifiedException() throws Exception {
		testValidTemplateName(new Exception(""));
	}

	@Test
	public void testMergeTemplateFailureWithMethodInvocationException() throws Exception {
		testValidTemplateName(new MethodInvocationException("Bad template", null, "none", "foo.vm", 1, 100));
	}

	/**
	 * @param mergeTemplateFailureException may be null in which case mergeTemplate override will succeed.
	 * If it's non null it will be checked
	 */
	private void testValidTemplateName(final Exception mergeTemplateFailureException) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("foo", "bar");

		final String templateName = "test.vm";

		WebApplicationContext wac = mock(WebApplicationContext.class);
		MockServletContext sc = new MockServletContext();
		sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);

		final Template expectedTemplate = new Template();
		VelocityConfig vc = new VelocityConfig() {
			@Override
			public VelocityEngine getVelocityEngine() {
				return new TestVelocityEngine(templateName, expectedTemplate);
			}
		};
		Map<String, VelocityConfig> configurers = new HashMap<String, VelocityConfig>();
		configurers.put("velocityConfigurer", vc);
		given(wac.getBeansOfType(VelocityConfig.class, true, false)).willReturn(configurers);
		given(wac.getServletContext()).willReturn(sc);
		given(wac.getBean("requestDataValueProcessor",
				RequestDataValueProcessor.class)).willReturn(null);

		HttpServletRequest request = new MockHttpServletRequest();
		final HttpServletResponse expectedResponse = new MockHttpServletResponse();

		VelocityView vv = new VelocityView() {
			@Override
			protected void mergeTemplate(Template template, Context context, HttpServletResponse response) throws Exception {
				assertTrue(template == expectedTemplate);
				assertTrue(context.getKeys().length >= 1);
				assertTrue(context.get("foo").equals("bar"));
				assertTrue(response == expectedResponse);
				if (mergeTemplateFailureException != null) {
					throw mergeTemplateFailureException;
				}
			}
		};
		vv.setUrl(templateName);
		vv.setApplicationContext(wac);

		try {
			vv.render(model, request, expectedResponse);
			if (mergeTemplateFailureException != null) {
				fail();
			}
		}
		catch (Exception ex) {
			assertNotNull(mergeTemplateFailureException);
			assertEquals(ex, mergeTemplateFailureException);
		}
	}

	@Test
	public void testKeepExistingContentType() throws Exception {
		final String templateName = "test.vm";

		WebApplicationContext wac = mock(WebApplicationContext.class);
		MockServletContext sc = new MockServletContext();
		sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);

		final Template expectedTemplate = new Template();
		VelocityConfig vc = new VelocityConfig() {
			@Override
			public VelocityEngine getVelocityEngine() {
				return new TestVelocityEngine(templateName, expectedTemplate);
			}
		};
		Map<String, VelocityConfig> configurers = new HashMap<String, VelocityConfig>();
		configurers.put("velocityConfigurer", vc);
		given(wac.getBeansOfType(VelocityConfig.class, true, false)).willReturn(configurers);
		given(wac.getServletContext()).willReturn(sc);
		given(wac.getBean("requestDataValueProcessor",
				RequestDataValueProcessor.class)).willReturn(null);

		HttpServletRequest request = new MockHttpServletRequest();
		final HttpServletResponse expectedResponse = new MockHttpServletResponse();
		expectedResponse.setContentType("myContentType");

		VelocityView vv = new VelocityView() {
			@Override
			protected void mergeTemplate(Template template, Context context, HttpServletResponse response) {
				assertTrue(template == expectedTemplate);
				assertTrue(response == expectedResponse);
			}
			@Override
			protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
				model.put("myHelper", "myValue");
			}
		};

		vv.setUrl(templateName);
		vv.setApplicationContext(wac);
		vv.render(new HashMap<String, Object>(), request, expectedResponse);

		assertEquals("myContentType", expectedResponse.getContentType());
	}

	@Test
	public void testExposeHelpers() throws Exception {
		final String templateName = "test.vm";

		WebApplicationContext wac = mock(WebApplicationContext.class);
		given(wac.getServletContext()).willReturn(new MockServletContext());

		final Template expectedTemplate = new Template();
		VelocityConfig vc = new VelocityConfig() {
			@Override
			public VelocityEngine getVelocityEngine() {
				return new TestVelocityEngine(templateName, expectedTemplate);
			}
		};
		Map<String, VelocityConfig> configurers = new HashMap<String, VelocityConfig>();
		configurers.put("velocityConfigurer", vc);
		given(wac.getBeansOfType(VelocityConfig.class, true, false)).willReturn(configurers);


		// let it ask for locale
		HttpServletRequest req = mock(HttpServletRequest.class);
		given(req.getAttribute(View.PATH_VARIABLES)).willReturn(null);
		given(req.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE)).willReturn(new AcceptHeaderLocaleResolver());
		given(req.getLocale()).willReturn(Locale.CANADA);

		final HttpServletResponse expectedResponse = new MockHttpServletResponse();

		VelocityView vv = new VelocityView() {
			@Override
			protected void mergeTemplate(Template template, Context context, HttpServletResponse response) throws Exception {
				assertTrue(template == expectedTemplate);
				assertTrue(response == expectedResponse);

				assertEquals("myValue", context.get("myHelper"));
				assertTrue(context.get("math") instanceof MathTool);

				assertTrue(context.get("dateTool") instanceof DateTool);
				DateTool dateTool = (DateTool) context.get("dateTool");
				assertTrue(dateTool.getLocale().equals(Locale.CANADA));

				assertTrue(context.get("numberTool") instanceof NumberTool);
				NumberTool numberTool = (NumberTool) context.get("numberTool");
				assertTrue(numberTool.getLocale().equals(Locale.CANADA));
			}

			@Override
			protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
				model.put("myHelper", "myValue");
			}
		};

		vv.setUrl(templateName);
		vv.setApplicationContext(wac);
		Map<String, Class> toolAttributes = new HashMap<String, Class>();
		toolAttributes.put("math", MathTool.class);
		vv.setToolAttributes(toolAttributes);
		vv.setDateToolAttribute("dateTool");
		vv.setNumberToolAttribute("numberTool");
		vv.setExposeSpringMacroHelpers(false);

		vv.render(new HashMap<String, Object>(), req, expectedResponse);

		assertEquals(AbstractView.DEFAULT_CONTENT_TYPE, expectedResponse.getContentType());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9342.java