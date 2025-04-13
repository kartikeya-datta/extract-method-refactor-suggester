error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6378.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6378.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6378.java
text:
```scala
a@@ssertFalse("The requestHandled flag shouldn't change", mavContainer.isRequestHandled());

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

package org.springframework.web.servlet.mvc.method.annotation.support;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Part;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

/**
 * Test fixture with {@link RequestPartMethodArgumentResolver} and mock {@link HttpMessageConverter}.
 * 
 * @author Rossen Stoyanchev
 */
public class RequestPartMethodArgumentResolverTests {

	private RequestPartMethodArgumentResolver resolver;

	private HttpMessageConverter<SimpleBean> messageConverter;
	
	private MultipartFile multipartFile1;
	private MultipartFile multipartFile2;

	private MethodParameter paramRequestPart;
	private MethodParameter paramNamedRequestPart;
	private MethodParameter paramValidRequestPart;
	private MethodParameter paramMultipartFile;
	private MethodParameter paramMultipartFileList;
	private MethodParameter paramInt;
	private MethodParameter paramMultipartFileNotAnnot;
	private MethodParameter paramServlet30Part;
	private MethodParameter paramRequestParamAnnot;

	private NativeWebRequest webRequest;

	private MockMultipartHttpServletRequest multipartRequest;

	private MockHttpServletResponse servletResponse;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		
		Method method = getClass().getMethod("handle", SimpleBean.class, SimpleBean.class, SimpleBean.class,
				MultipartFile.class, List.class, Integer.TYPE, MultipartFile.class, Part.class, MultipartFile.class);
		
		paramRequestPart = new MethodParameter(method, 0);
		paramRequestPart.initParameterNameDiscovery(new LocalVariableTableParameterNameDiscoverer());
		paramNamedRequestPart = new MethodParameter(method, 1);
		paramValidRequestPart = new MethodParameter(method, 2);
		paramMultipartFile = new MethodParameter(method, 3);
		paramMultipartFileList = new MethodParameter(method, 4);
		paramInt = new MethodParameter(method, 5);
		paramMultipartFileNotAnnot = new MethodParameter(method, 6);
		paramMultipartFileNotAnnot.initParameterNameDiscovery(new LocalVariableTableParameterNameDiscoverer());
		paramServlet30Part = new MethodParameter(method, 7);
		paramServlet30Part.initParameterNameDiscovery(new LocalVariableTableParameterNameDiscoverer());
		paramRequestParamAnnot = new MethodParameter(method, 8);

		messageConverter = createMock(HttpMessageConverter.class);
		expect(messageConverter.getSupportedMediaTypes()).andReturn(Collections.singletonList(MediaType.TEXT_PLAIN));
		replay(messageConverter);

		resolver = new RequestPartMethodArgumentResolver(Collections.<HttpMessageConverter<?>>singletonList(messageConverter));
		reset(messageConverter);
		
		multipartFile1 = new MockMultipartFile("requestPart", "", "text/plain", (byte[]) null);
		multipartFile2 = new MockMultipartFile("requestPart", "", "text/plain", (byte[]) null);
		multipartRequest = new MockMultipartHttpServletRequest();
		multipartRequest.addFile(multipartFile1);
		multipartRequest.addFile(multipartFile2);
		servletResponse = new MockHttpServletResponse();
		webRequest = new ServletWebRequest(multipartRequest, servletResponse);
	}

	@Test
	public void supportsParameter() {
		assertTrue("RequestPart parameter not supported", resolver.supportsParameter(paramRequestPart));
		assertTrue("MultipartFile parameter not supported", resolver.supportsParameter(paramMultipartFileNotAnnot));
		assertTrue("Part parameter not supported", resolver.supportsParameter(paramServlet30Part));
		assertFalse("non-RequestPart parameter supported", resolver.supportsParameter(paramInt));
		assertFalse("@RequestParam args not supported", resolver.supportsParameter(paramRequestParamAnnot));
	}	

	@Test 
	public void resolveMultipartFile() throws Exception {
		Object actual = resolver.resolveArgument(paramMultipartFile, null, webRequest, null);
		assertNotNull(actual);
		assertSame(multipartFile1, actual);
	}

	@Test 
	public void resolveMultipartFileList() throws Exception {
		Object actual = resolver.resolveArgument(paramMultipartFileList, null, webRequest, null);
		assertNotNull(actual);
		assertTrue(actual instanceof List);
		assertEquals(Arrays.asList(multipartFile1, multipartFile2), actual);
	}

	@Test
	public void resolveMultipartFileNotAnnotArgument() throws Exception {
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		MultipartFile expected = new MockMultipartFile("multipartFileNotAnnot", "Hello World".getBytes());
		request.addFile(expected);
		webRequest = new ServletWebRequest(request);

		Object result = resolver.resolveArgument(paramMultipartFileNotAnnot, null, webRequest, null);

		assertTrue(result instanceof MultipartFile);
		assertEquals("Invalid result", expected, result);
	}

	@Test
	public void resolveServlet30PartArgument() throws Exception {
		MockPart expected = new MockPart("servlet30Part", "Hello World".getBytes());
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setContentType("multipart/form-data");
		request.addPart(expected);
		webRequest = new ServletWebRequest(request);

		Object result = resolver.resolveArgument(paramServlet30Part, null, webRequest, null);

		assertTrue(result instanceof Part);
		assertEquals("Invalid result", expected, result);
	}

	@Test
	public void resolveRequestPart() throws Exception {
		testResolveArgument(new SimpleBean("foo"), paramRequestPart);
	}

	@Test
	public void resolveNamedRequestPart() throws Exception {
		testResolveArgument(new SimpleBean("foo"), paramNamedRequestPart);
	}
	
	@Test
	public void resolveRequestPartNotValid() throws Exception {
		try {
			testResolveArgument(new SimpleBean(null), paramValidRequestPart);
			fail("Expected exception");
		} catch (MethodArgumentNotValidException e) {
			assertEquals("requestPart", e.getBindingResult().getObjectName());
			assertEquals(1, e.getBindingResult().getErrorCount());
			assertNotNull(e.getBindingResult().getFieldError("name"));
		}
	}
	
	@Test
	public void resolveRequestPartValid() throws Exception {
		testResolveArgument(new SimpleBean("foo"), paramNamedRequestPart);
	}
	
	@Test
	public void resolveRequestPartRequired() throws Exception {
		try {
			testResolveArgument(null, paramValidRequestPart);
			fail("Expected exception");
		} catch (MissingServletRequestPartException e) {
			assertEquals("requestPart", e.getRequestPartName());
		}
	}

	@Test
	public void resolveRequestPartNotRequired() throws Exception {
		testResolveArgument(new SimpleBean("foo"), paramValidRequestPart);
	}
	
	@Test(expected=MultipartException.class)
	public void notMultipartRequest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		resolver.resolveArgument(paramMultipartFile, new ModelAndViewContainer(), new ServletWebRequest(request), null);
		fail("Expected exception");
	}

	private void testResolveArgument(SimpleBean argValue, MethodParameter parameter) throws IOException, Exception {
		MediaType contentType = MediaType.TEXT_PLAIN;
		multipartRequest.addHeader("Content-Type", contentType.toString());

		expect(messageConverter.canRead(SimpleBean.class, contentType)).andReturn(true);
		expect(messageConverter.read(eq(SimpleBean.class), isA(RequestPartServletServerHttpRequest.class))).andReturn(argValue);
		replay(messageConverter);

		ModelAndViewContainer mavContainer = new ModelAndViewContainer();
		Object actualValue = resolver.resolveArgument(parameter, mavContainer, webRequest, new ValidatingBinderFactory());

		assertEquals("Invalid argument value", argValue, actualValue);
		assertTrue("The ResolveView flag shouldn't change", mavContainer.isResolveView());
		
		verify(messageConverter);
	}	

	private static class SimpleBean {

		@NotNull
		private final String name;

		public SimpleBean(String name) {
			this.name = name;
		}

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}
	}
	
	private final class ValidatingBinderFactory implements WebDataBinderFactory {
		public WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName) throws Exception {
			LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
			validator.afterPropertiesSet();
			WebDataBinder dataBinder = new WebDataBinder(target, objectName);
			dataBinder.setValidator(validator);
			return dataBinder;
		}
	}

	public void handle(@RequestPart SimpleBean requestPart, 
					   @RequestPart(value="requestPart", required=false) SimpleBean namedRequestPart, 
					   @Valid @RequestPart("requestPart") SimpleBean validRequestPart, 
					   @RequestPart("requestPart") MultipartFile multipartFile,
					   @RequestPart("requestPart") List<MultipartFile> multipartFileList,
					   int i,
					   MultipartFile multipartFileNotAnnot,
					   Part servlet30Part,
					   @RequestParam MultipartFile requestParamAnnot) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6378.java