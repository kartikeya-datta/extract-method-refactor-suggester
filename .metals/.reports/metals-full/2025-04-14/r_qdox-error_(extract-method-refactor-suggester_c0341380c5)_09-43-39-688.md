error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6516.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6516.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6516.java
text:
```scala
r@@equest.addHeader("Accept", "text/html,application/xml;q=0.9,application/xhtml+xml,*/*;q=0.8");

/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.web.servlet.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * @author Arjen Poutsma
 */
public class ContentNegotiatingViewResolverTests {

	private ContentNegotiatingViewResolver viewResolver;

	@Before
	public void createViewResolver() {
		viewResolver = new ContentNegotiatingViewResolver();
	}

	@Test
	public void getMediaTypeFromFilename() {
		assertEquals("Invalid content type", new MediaType("text", "html"),
				viewResolver.getMediaTypeFromFilename("test.html"));
		viewResolver.setMediaTypes(Collections.singletonMap("HTML", "application/xhtml+xml"));
		assertEquals("Invalid content type", new MediaType("application", "xhtml+xml"),
				viewResolver.getMediaTypeFromFilename("test.html"));
	}

	@Test
	public void getMediaTypeFilename() {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test.html?foo=bar");
		List<MediaType> result = viewResolver.getMediaTypes(request);
		assertEquals("Invalid content type", Collections.singletonList(new MediaType("text", "html")), result);
		viewResolver.setMediaTypes(Collections.singletonMap("html", "application/xhtml+xml"));
		result = viewResolver.getMediaTypes(request);
		assertEquals("Invalid content type", Collections.singletonList(new MediaType("application", "xhtml+xml")),
				result);
	}
	
	@Test
	public void getMediaTypeParameter() {
		viewResolver.setFavorParameter(true);
		viewResolver.setMediaTypes(Collections.singletonMap("html", "application/xhtml+xml"));
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
		request.addParameter("format", "html");
		List<MediaType> result = viewResolver.getMediaTypes(request);
		assertEquals("Invalid content type", Collections.singletonList(new MediaType("application", "xhtml+xml")),
				result);
	}

	@Test
	public void getMediaTypeAcceptHeader() {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
		request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		List<MediaType> result = viewResolver.getMediaTypes(request);
		assertEquals("Invalid amount of media types", 4, result.size());
		assertEquals("Invalid content type", new MediaType("text", "html"), result.get(0));
		assertEquals("Invalid content type", new MediaType("application", "xhtml+xml"), result.get(1));
		assertEquals("Invalid content type", new MediaType("application", "xml", Collections.singletonMap("q", "0.9")),
				result.get(2));
		assertEquals("Invalid content type", new MediaType("*", "*", Collections.singletonMap("q", "0.8")),
				result.get(3));
	}

	@Test
	public void getDefaultContentType() {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
		request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		viewResolver.setIgnoreAcceptHeader(true);
		viewResolver.setDefaultContentType(new MediaType("application", "pdf"));
		List<MediaType> result = viewResolver.getMediaTypes(request);
		assertEquals("Invalid amount of media types", 1, result.size());
		assertEquals("Invalid content type", new MediaType("application", "pdf"), result.get(0));
	}

	@Test
	public void resolveViewNameWithDefaultContentType() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
		request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		viewResolver.setIgnoreAcceptHeader(true);
		viewResolver.setDefaultContentType(new MediaType("application", "xml"));

		ViewResolver viewResolverMock1 = createMock(ViewResolver.class);
		ViewResolver viewResolverMock2 = createMock(ViewResolver.class);
		List<ViewResolver> viewResolverMocks = new ArrayList<ViewResolver>();
		viewResolverMocks.add(viewResolverMock1);
		viewResolverMocks.add(viewResolverMock2);
		viewResolver.setViewResolvers(viewResolverMocks);

		View viewMock1 = createMock("application_xml", View.class);
		View viewMock2 = createMock("text_html", View.class);

		String viewName = "view";
		Locale locale = Locale.ENGLISH;

		expect(viewResolverMock1.resolveViewName(viewName, locale)).andReturn(viewMock1);
		expect(viewResolverMock2.resolveViewName(viewName, locale)).andReturn(viewMock2);
		expect(viewMock1.getContentType()).andReturn("application/xml").anyTimes();
		expect(viewMock2.getContentType()).andReturn("text/html;charset=ISO-8859-1").anyTimes();

		replay(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2);

		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", viewMock1, result);

		verify(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2);
	}

	@Test
	public void resolveViewNameAcceptHeader() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
		request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ViewResolver viewResolverMock1 = createMock(ViewResolver.class);
		ViewResolver viewResolverMock2 = createMock(ViewResolver.class);
		List<ViewResolver> viewResolverMocks = new ArrayList<ViewResolver>();
		viewResolverMocks.add(viewResolverMock1);
		viewResolverMocks.add(viewResolverMock2);
		viewResolver.setViewResolvers(viewResolverMocks);

		View viewMock1 = createMock("application_xml", View.class);
		View viewMock2 = createMock("text_html", View.class);

		String viewName = "view";
		Locale locale = Locale.ENGLISH;

		expect(viewResolverMock1.resolveViewName(viewName, locale)).andReturn(viewMock1);
		expect(viewResolverMock2.resolveViewName(viewName, locale)).andReturn(viewMock2);
		expect(viewMock1.getContentType()).andReturn("application/xml").anyTimes();
		expect(viewMock2.getContentType()).andReturn("text/html;charset=ISO-8859-1").anyTimes();

		replay(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2);

		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", viewMock2, result);

		verify(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2);
	}

	@Test
	public void resolveViewNameAcceptHeaderDefaultView() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
		request.addHeader("Accept", "application/json");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ViewResolver viewResolverMock1 = createMock(ViewResolver.class);
		ViewResolver viewResolverMock2 = createMock(ViewResolver.class);
		List<ViewResolver> viewResolverMocks = new ArrayList<ViewResolver>();
		viewResolverMocks.add(viewResolverMock1);
		viewResolverMocks.add(viewResolverMock2);
		viewResolver.setViewResolvers(viewResolverMocks);

		View viewMock1 = createMock("application_xml", View.class);
		View viewMock2 = createMock("text_html", View.class);
		View viewMock3 = createMock("application_json", View.class);

		List<View> defaultViews = new ArrayList<View>();
		defaultViews.add(viewMock3);
		viewResolver.setDefaultViews(defaultViews);

		String viewName = "view";
		Locale locale = Locale.ENGLISH;

		expect(viewResolverMock1.resolveViewName(viewName, locale)).andReturn(viewMock1);
		expect(viewResolverMock2.resolveViewName(viewName, locale)).andReturn(viewMock2);
		expect(viewMock1.getContentType()).andReturn("application/xml").anyTimes();
		expect(viewMock2.getContentType()).andReturn("text/html;charset=ISO-8859-1").anyTimes();
		expect(viewMock3.getContentType()).andReturn("application/json").anyTimes();

		replay(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2, viewMock3);

		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", viewMock3, result);

		verify(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2, viewMock3);
	}

	@Test
	public void resolveViewNameFilename() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test.html");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ViewResolver viewResolverMock1 = createMock(ViewResolver.class);
		ViewResolver viewResolverMock2 = createMock(ViewResolver.class);
		List<ViewResolver> viewResolverMocks = new ArrayList<ViewResolver>();
		viewResolverMocks.add(viewResolverMock1);
		viewResolverMocks.add(viewResolverMock2);
		viewResolver.setViewResolvers(viewResolverMocks);

		View viewMock1 = createMock("application_xml", View.class);
		View viewMock2 = createMock("text_html", View.class);

		String viewName = "view";
		Locale locale = Locale.ENGLISH;

		expect(viewResolverMock1.resolveViewName(viewName, locale)).andReturn(viewMock1);
		expect(viewResolverMock2.resolveViewName(viewName, locale)).andReturn(viewMock2);
		expect(viewMock1.getContentType()).andReturn("application/xml").anyTimes();
		expect(viewMock2.getContentType()).andReturn("text/html;charset=ISO-8859-1").anyTimes();

		replay(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2);

		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", viewMock2, result);

		verify(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2);
	}

	@Test
	public void resolveViewNameFilenameDefaultView() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test.json");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		Map<String, String> mediaTypes = new HashMap<String, String>();
		mediaTypes.put("json", "application/json");
		viewResolver.setMediaTypes(mediaTypes);

		ViewResolver viewResolverMock1 = createMock(ViewResolver.class);
		ViewResolver viewResolverMock2 = createMock(ViewResolver.class);
		List<ViewResolver> viewResolverMocks = new ArrayList<ViewResolver>();
		viewResolverMocks.add(viewResolverMock1);
		viewResolverMocks.add(viewResolverMock2);
		viewResolver.setViewResolvers(viewResolverMocks);

		View viewMock1 = createMock("application_xml", View.class);
		View viewMock2 = createMock("text_html", View.class);
		View viewMock3 = createMock("application_json", View.class);

		List<View> defaultViews = new ArrayList<View>();
		defaultViews.add(viewMock3);
		viewResolver.setDefaultViews(defaultViews);

		String viewName = "view";
		Locale locale = Locale.ENGLISH;

		expect(viewResolverMock1.resolveViewName(viewName, locale)).andReturn(viewMock1);
		expect(viewResolverMock2.resolveViewName(viewName, locale)).andReturn(viewMock2);
		expect(viewMock1.getContentType()).andReturn("application/xml").anyTimes();
		expect(viewMock2.getContentType()).andReturn("text/html;charset=ISO-8859-1").anyTimes();
		expect(viewMock3.getContentType()).andReturn("application/json").anyTimes();

		replay(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2, viewMock3);

		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", viewMock3, result);

		verify(viewResolverMock1, viewResolverMock2, viewMock1, viewMock2, viewMock3);
	}

	@Test
	public void resolveViewContentTypeNull() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
		request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ViewResolver viewResolverMock = createMock(ViewResolver.class);
		viewResolver.setViewResolvers(Collections.singletonList(viewResolverMock));

		View viewMock = createMock("application_xml", View.class);

		String viewName = "view";
		Locale locale = Locale.ENGLISH;

		expect(viewResolverMock.resolveViewName(viewName, locale)).andReturn(viewMock);
		expect(viewMock.getContentType()).andReturn(null).anyTimes();

		replay(viewResolverMock, viewMock);

		View result = viewResolver.resolveViewName(viewName, locale);
		assertNull("Invalid view", result);

		verify(viewResolverMock, viewMock);
	}

	@Test
	public void resolveViewNoMatch() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
		request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ViewResolver viewResolverMock = createMock(ViewResolver.class);
		viewResolver.setViewResolvers(Collections.singletonList(viewResolverMock));

		View viewMock = createMock("application_xml", View.class);

		String viewName = "view";
		Locale locale = Locale.ENGLISH;

		expect(viewResolverMock.resolveViewName(viewName, locale)).andReturn(viewMock);
		expect(viewMock.getContentType()).andReturn("application/pdf").anyTimes();

		replay(viewResolverMock, viewMock);

		View result = viewResolver.resolveViewName(viewName, locale);
		assertNull("Invalid view", result);

		verify(viewResolverMock, viewMock);
	}

	@Test
	public void resolveViewNoMatchUseUnacceptableStatus() throws Exception {
		viewResolver.setUseNotAcceptableStatusCode(true);
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
		request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ViewResolver viewResolverMock = createMock(ViewResolver.class);
		viewResolver.setViewResolvers(Collections.singletonList(viewResolverMock));

		View viewMock = createMock("application_xml", View.class);

		String viewName = "view";
		Locale locale = Locale.ENGLISH;

		expect(viewResolverMock.resolveViewName(viewName, locale)).andReturn(viewMock);
		expect(viewMock.getContentType()).andReturn("application/pdf").anyTimes();

		replay(viewResolverMock, viewMock);

		View result = viewResolver.resolveViewName(viewName, locale);
		assertNotNull("Invalid view", result);
		MockHttpServletResponse response = new MockHttpServletResponse();
		result.render(null, request, response);
		assertEquals("Invalid status code set", 406, response.getStatus());

		verify(viewResolverMock, viewMock);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6516.java