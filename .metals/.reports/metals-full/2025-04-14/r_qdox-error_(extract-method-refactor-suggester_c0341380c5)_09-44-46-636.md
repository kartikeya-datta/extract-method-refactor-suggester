error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15123.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15123.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15123.java
text:
```scala
m@@odel.addAttribute("cookie", cookie).addAttribute("header", header)

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

package org.springframework.web.servlet.mvc.method.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.lang.reflect.Method;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.TestBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.support.ServletWebArgumentResolverAdapter;

/**
 * Serves as a sandbox to invoke all types of controller methods using all features except for the kitchen sink. 
 * Once a problem has been debugged and understood, tests demonstrating the issue are preferably added to the 
 * appropriate, more fine-grained test fixture.
 * 
 * <p>If you wish to add high-level tests, consider the following other "integration"-style tests:
 * <ul>
 * 	<li>{@link HandlerMethodAdapterAnnotationDetectionTests} 
 * 	<li>{@link HandlerMethodMappingAnnotationDetectionTests}
 * 	<li>{@link ServletHandlerMethodTests}
 * </ul>
 * 
 * @author Rossen Stoyanchev
 */
public class RequestMappingHandlerMethodAdapterIntegrationTests {

	private final Object handler = new Handler();
	
	private RequestMappingHandlerMethodAdapter handlerAdapter;
	
	private MockHttpServletRequest request;
	
	private MockHttpServletResponse response;

	@Before
	public void setup() throws Exception {
		ConfigurableWebBindingInitializer bindingInitializer = new ConfigurableWebBindingInitializer();
		bindingInitializer.setValidator(new StubValidator());
		
		List<HandlerMethodArgumentResolver> customResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		customResolvers.add(new ServletWebArgumentResolverAdapter(new ColorArgumentResolver()));

		GenericWebApplicationContext context = new GenericWebApplicationContext();
		context.refresh();

		handlerAdapter = new RequestMappingHandlerMethodAdapter();
		handlerAdapter.setWebBindingInitializer(bindingInitializer);
		handlerAdapter.setCustomArgumentResolvers(customResolvers);
		handlerAdapter.setApplicationContext(context);
		handlerAdapter.setBeanFactory(context.getBeanFactory());
		handlerAdapter.afterPropertiesSet();

		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		
		// Expose request to the current thread (for SpEL expressions)
		RequestContextHolder.setRequestAttributes(new ServletWebRequest(request));
	}
	
	@After
	public void teardown() {
		RequestContextHolder.resetRequestAttributes();
	}

	@Test
	public void handleMvc() throws Exception {
		
		Class<?>[] parameterTypes = new Class<?>[] { int.class, String.class, String.class, String.class, Map.class,
				Date.class, Map.class, String.class, String.class, TestBean.class, Errors.class, TestBean.class,
				Color.class, HttpServletRequest.class, HttpServletResponse.class, User.class, OtherUser.class,
				Model.class };

		String datePattern = "yyyy.MM.dd";
		String formattedDate = "2011.03.16";
		Date date = new GregorianCalendar(2011, Calendar.MARCH, 16).getTime();

		request.addHeader("Content-Type", "text/plain; charset=utf-8");
		request.addHeader("header", "headerValue");
		request.addHeader("anotherHeader", "anotherHeaderValue");
		request.addParameter("datePattern", datePattern);
		request.addParameter("dateParam", formattedDate);
		request.addParameter("paramByConvention", "paramByConventionValue");
		request.addParameter("age", "25");
		request.setCookies(new Cookie("cookie", "99"));
		request.setContent("Hello World".getBytes("UTF-8"));
		request.setUserPrincipal(new User());
		request.setContextPath("/contextPath");

		System.setProperty("systemHeader", "systemHeaderValue");

		/* Set up path variables as RequestMappingHandlerMethodMapping would... */
		Map<String, String> uriTemplateVars = new HashMap<String, String>();
		uriTemplateVars.put("pathvar", "pathvarValue");
		request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplateVars);

		HandlerMethod handlerMethod = handlerMethod("handleMvc", parameterTypes);
		ModelAndView mav = handlerAdapter.handle(request, response, handlerMethod);
		ModelMap model = mav.getModelMap();

		assertEquals("viewName", mav.getViewName());
		assertEquals(99, model.get("cookie"));
		assertEquals("pathvarValue", model.get("pathvar"));
		assertEquals("headerValue", model.get("header"));
		assertEquals(date, model.get("dateParam"));

		Map<?,?> map = (Map<?,?>) model.get("headerMap");
		assertEquals("headerValue", map.get("header"));
		assertEquals("anotherHeaderValue", map.get("anotherHeader"));
		assertEquals("systemHeaderValue", model.get("systemHeader"));

		map = (Map<?,?>) model.get("paramMap");
		assertEquals(formattedDate, map.get("dateParam"));
		assertEquals("paramByConventionValue", map.get("paramByConvention"));

		assertEquals("/contextPath", model.get("value"));
		
		TestBean modelAttr = (TestBean) model.get("modelAttr");
		assertEquals(25, modelAttr.getAge());
		assertEquals("Set by model method [modelAttr]", modelAttr.getName());
		assertSame(modelAttr, request.getSession().getAttribute("modelAttr"));

		BindingResult bindingResult = (BindingResult) model.get(BindingResult.MODEL_KEY_PREFIX + "modelAttr");
		assertSame(modelAttr, bindingResult.getTarget());
		assertEquals(1, bindingResult.getErrorCount());

		String conventionAttrName = "testBean";
		TestBean modelAttrByConvention = (TestBean) model.get(conventionAttrName);
		assertEquals(25, modelAttrByConvention.getAge());
		assertEquals("Set by model method [modelAttrByConvention]", modelAttrByConvention.getName());
		assertSame(modelAttrByConvention, request.getSession().getAttribute(conventionAttrName));

		bindingResult = (BindingResult) model.get(BindingResult.MODEL_KEY_PREFIX + conventionAttrName);
		assertSame(modelAttrByConvention, bindingResult.getTarget());

		assertTrue(model.get("customArg") instanceof Color);
		assertEquals(User.class, model.get("user").getClass());
		assertEquals(OtherUser.class, model.get("otherUser").getClass());
	}
	
	@Test
	public void handleRequestBody() throws Exception {
		
		Class<?>[] parameterTypes = new Class<?>[] { byte[].class };

		request.addHeader("Content-Type", "text/plain; charset=utf-8");
		request.setContent("Hello Server".getBytes("UTF-8"));

		HandlerMethod handlerMethod = handlerMethod("handleRequestBody", parameterTypes);

		ModelAndView mav = handlerAdapter.handle(request, response, handlerMethod);

		assertNull(mav);
		assertEquals("Handled requestBody=[Hello Server]", new String(response.getContentAsByteArray(), "UTF-8"));
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
	}

	@Test
	public void handleHttpEntity() throws Exception {

		Class<?>[] parameterTypes = new Class<?>[] { HttpEntity.class };

		request.addHeader("Content-Type", "text/plain; charset=utf-8");
		request.setContent("Hello Server".getBytes("UTF-8"));

		HandlerMethod handlerMethod = handlerMethod("handleHttpEntity", parameterTypes);

		ModelAndView mav = handlerAdapter.handle(request, response, handlerMethod);

		assertNull(mav);
		assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
		assertEquals("Handled requestBody=[Hello Server]", new String(response.getContentAsByteArray(), "UTF-8"));
		assertEquals("headerValue", response.getHeader("header"));
	}

	private HandlerMethod handlerMethod(String methodName, Class<?>... paramTypes) throws Exception {
		Method method = handler.getClass().getDeclaredMethod(methodName, paramTypes);
		return new InvocableHandlerMethod(handler, method);
	}

	@SuppressWarnings("unused")
	@SessionAttributes(types=TestBean.class)
	private static class Handler {

		@InitBinder("dateParam")
		public void initBinder(WebDataBinder dataBinder, @RequestParam("datePattern") String datePattern) {			
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
		}

		@ModelAttribute
		public void model(Model model) {
			TestBean modelAttr = new TestBean();
			modelAttr.setName("Set by model method [modelAttr]");
			model.addAttribute("modelAttr", modelAttr);

			modelAttr = new TestBean();
			modelAttr.setName("Set by model method [modelAttrByConvention]");
			model.addAttribute(modelAttr);
			
			model.addAttribute(new OtherUser());
		}

		public String handleMvc(
							@CookieValue("cookie") int cookie, 
						 	@PathVariable("pathvar") String pathvar,
						 	@RequestHeader("header") String header,
						 	@RequestHeader(defaultValue="#{systemProperties.systemHeader}") String systemHeader,
						 	@RequestHeader Map<String, Object> headerMap,
						 	@RequestParam("dateParam") Date dateParam, 
						 	@RequestParam Map<String, Object> paramMap,
						 	String paramByConvention, 
						 	@Value("#{request.contextPath}") String value,
						 	@ModelAttribute("modelAttr") @Valid TestBean modelAttr,
						 	Errors errors,
						 	TestBean modelAttrByConvention,
						 	Color customArg,
						 	HttpServletRequest request,
						 	HttpServletResponse response,
						 	User user,
						 	@ModelAttribute OtherUser otherUser,
						 	Model model) throws Exception {

			model.addAttribute("cookie", cookie).addAttribute("pathvar", pathvar).addAttribute("header", header)
					.addAttribute("systemHeader", systemHeader).addAttribute("headerMap", headerMap)
					.addAttribute("dateParam", dateParam).addAttribute("paramMap", paramMap)
					.addAttribute("paramByConvention", paramByConvention).addAttribute("value", value)
					.addAttribute("customArg", customArg).addAttribute(user);
			
			assertNotNull(request);
			assertNotNull(response);

			return "viewName";
		}
		
		@ResponseStatus(value=HttpStatus.ACCEPTED)
		@ResponseBody
		public String handleRequestBody(@RequestBody byte[] bytes) throws Exception {
			String requestBody = new String(bytes, "UTF-8");
			return "Handled requestBody=[" + requestBody + "]";
		}

		public ResponseEntity<String> handleHttpEntity(HttpEntity<byte[]> httpEntity) throws Exception {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("header", "headerValue");
			String responseBody = "Handled requestBody=[" + new String(httpEntity.getBody(), "UTF-8") + "]";
			return new ResponseEntity<String>(responseBody, responseHeaders, HttpStatus.ACCEPTED);
		}
	}

	private static class StubValidator implements Validator {
		public boolean supports(Class<?> clazz) {
			return true;
		}

		public void validate(Object target, Errors errors) {
			errors.reject("error");
		}
	}

	private static class ColorArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
			return new Color(0);
		}
	}
	
	private static class User implements Principal {
		public String getName() {
			return "user";
		}
	}

	private static class OtherUser implements Principal {
		public String getName() {
			return "other user";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15123.java