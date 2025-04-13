error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14648.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14648.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14648.java
text:
```scala
n@@ew PatternsRequestCondition(annotation.value(), getUrlPathHelper(), getPathMatcher(), true),

/*
 * Copyright 2002-2011 the original author or authors.
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

package org.springframework.web.servlet.mvc.method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

/**
 * Test fixture with {@link RequestMappingHandlerMapping}.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 */
public class RequestMappingInfoHandlerMappingTests {

	private TestRequestMappingInfoHandlerMapping mapping;

	private Handler handler;

	private HandlerMethod fooMethod;

	private HandlerMethod fooParamMethod;

	private HandlerMethod barMethod;

	private HandlerMethod emptyMethod;

	@Before
	public void setUp() throws Exception {
		handler = new Handler();
		fooMethod = new HandlerMethod(handler, "foo");
		fooParamMethod = new HandlerMethod(handler, "fooParam");
		barMethod = new HandlerMethod(handler, "bar");
		emptyMethod = new HandlerMethod(handler, "empty");

		StaticApplicationContext context = new StaticApplicationContext();
		context.registerSingleton("handler", handler.getClass());

		mapping = new TestRequestMappingInfoHandlerMapping();
		mapping.setApplicationContext(context);
	}

	@Test
	public void getMappingPathPatterns() throws Exception {
		RequestMappingInfo info = new RequestMappingInfo(
				new PatternsRequestCondition("/foo/*", "/foo", "/bar/*", "/bar"), null, null, null, null, null, null);
		Set<String> paths = mapping.getMappingPathPatterns(info);
		HashSet<String> expected = new HashSet<String>(Arrays.asList("/foo/*", "/foo", "/bar/*", "/bar"));

		assertEquals(expected, paths);
	}

	@Test
	public void directMatch() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo");
		HandlerMethod hm = (HandlerMethod) mapping.getHandler(request).getHandler();
		assertEquals(fooMethod.getMethod(), hm.getMethod());
	}

	@Test
	public void globMatch() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/bar");
		HandlerMethod hm = (HandlerMethod) mapping.getHandler(request).getHandler();
		assertEquals(barMethod.getMethod(), hm.getMethod());
	}

	@Test
	public void emptyPathMatch() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "");
		HandlerMethod hm = (HandlerMethod) mapping.getHandler(request).getHandler();
		assertEquals(emptyMethod.getMethod(), hm.getMethod());

		request = new MockHttpServletRequest("GET", "/");
		hm = (HandlerMethod) mapping.getHandler(request).getHandler();
		assertEquals(emptyMethod.getMethod(), hm.getMethod());
	}

	@Test
	public void bestMatch() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo");
		request.setParameter("p", "anything");
		HandlerMethod hm = (HandlerMethod) mapping.getHandler(request).getHandler();
		assertEquals(fooParamMethod.getMethod(), hm.getMethod());
	}
	
	@Test
	public void requestMethodNotAllowed() throws Exception {
		try {
			MockHttpServletRequest request = new MockHttpServletRequest("POST", "/bar");
			mapping.getHandler(request);
			fail("HttpRequestMethodNotSupportedException expected");
		}
		catch (HttpRequestMethodNotSupportedException ex) {
			assertArrayEquals("Invalid supported methods", new String[]{"GET", "HEAD"}, ex.getSupportedMethods());
		}
	}
	
	@Test
	public void mediaTypeNotSupported() throws Exception {
		testMediaTypeNotSupported("/person/1");
		testMediaTypeNotSupported("/person/1/");	// SPR-8462
		testMediaTypeNotSupported("/person/1.json");
	}

	private void testMediaTypeNotSupported(String url) throws Exception {
		try {
			MockHttpServletRequest request = new MockHttpServletRequest("PUT", url);
			request.setContentType("application/json");
			mapping.getHandler(request);
			fail("HttpMediaTypeNotSupportedException expected");
		}
		catch (HttpMediaTypeNotSupportedException ex) {
			assertEquals("Invalid supported consumable media types", 
					Arrays.asList(new MediaType("application", "xml")), ex.getSupportedMediaTypes());
		}
	}
	
	@Test
	public void mediaTypeNotAccepted() throws Exception {
		testMediaTypeNotAccepted("/persons");
		testMediaTypeNotAccepted("/persons/");	// SPR-8462
		testMediaTypeNotAccepted("/persons.json");
	}

	private void testMediaTypeNotAccepted(String url) throws Exception {
		try {
			MockHttpServletRequest request = new MockHttpServletRequest("GET", url);
			request.addHeader("Accept", "application/json");
			mapping.getHandler(request);
			fail("HttpMediaTypeNotAcceptableException expected");
		}
		catch (HttpMediaTypeNotAcceptableException ex) {
			assertEquals("Invalid supported producible media types", 
					Arrays.asList(new MediaType("application", "xml")), ex.getSupportedMediaTypes());
		}
	}

	@Test
	public void uriTemplateVariables() {
		PatternsRequestCondition patterns = new PatternsRequestCondition("/{path1}/{path2}");
		RequestMappingInfo key = new RequestMappingInfo(patterns, null, null, null, null, null, null);
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/1/2");
		String lookupPath = new UrlPathHelper().getLookupPathForRequest(request);

		mapping.handleMatch(key, lookupPath, request);
		
		@SuppressWarnings("unchecked")
		Map<String, String> actual = (Map<String, String>) request.getAttribute(
				HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		assertNotNull(actual);
		assertEquals("1", actual.get("path1"));
		assertEquals("2", actual.get("path2"));
	}
	
	@Test
	public void mappedInterceptors() throws Exception {
		String path = "/foo";
		HandlerInterceptor interceptor = new HandlerInterceptorAdapter() {};
		MappedInterceptor mappedInterceptor = new MappedInterceptor(new String[] {path}, interceptor);

		StaticApplicationContext context = new StaticApplicationContext();
		context.registerSingleton("handler", handler.getClass());

		mapping = new TestRequestMappingInfoHandlerMapping();
		mapping.setInterceptors(new Object[] { mappedInterceptor });
		mapping.setApplicationContext(context);

		HandlerExecutionChain chain = mapping.getHandler(new MockHttpServletRequest("GET", path));
		assertNotNull(chain);
		assertNotNull(chain.getInterceptors());
		assertSame(interceptor, chain.getInterceptors()[0]);

		chain = mapping.getHandler(new MockHttpServletRequest("GET", "/invalid"));
		assertNull(chain);
	}

	@SuppressWarnings("unused")
	@Controller
	private static class Handler {

		@RequestMapping(value = "/foo", method = RequestMethod.GET)
		public void foo() {
		}
		
		@RequestMapping(value = "/foo", method = RequestMethod.GET, params="p")
		public void fooParam() {
		}

		@RequestMapping(value = "/ba*", method = { RequestMethod.GET, RequestMethod.HEAD })
		public void bar() {
		}

		@RequestMapping(value = "")
		public void empty() {
		}

		@RequestMapping(value = "/person/{id}", method = RequestMethod.PUT, consumes="application/xml")
		public void consumes(@RequestBody String text) {
		}

		@RequestMapping(value = "/persons", produces="application/xml")
		public String produces() {
			return "";
		}
	}

	private static class TestRequestMappingInfoHandlerMapping extends RequestMappingInfoHandlerMapping {

		@Override
		protected boolean isHandler(Class<?> beanType) {
			return AnnotationUtils.findAnnotation(beanType, Controller.class) != null;
		}

		@Override
		protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
			RequestMapping annotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
			return new RequestMappingInfo(
					new PatternsRequestCondition(annotation.value(), getUrlPathHelper(), getPathMatcher()),
					new RequestMethodsRequestCondition(annotation.method()),
					new ParamsRequestCondition(annotation.params()),
					new HeadersRequestCondition(annotation.headers()),
					new ConsumesRequestCondition(annotation.consumes(), annotation.headers()),
					new ProducesRequestCondition(annotation.produces(), annotation.headers()), null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14648.java