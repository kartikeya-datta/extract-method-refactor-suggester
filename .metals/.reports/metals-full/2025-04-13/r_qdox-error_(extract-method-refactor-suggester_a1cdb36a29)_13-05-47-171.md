error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7709.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7709.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7709.java
text:
```scala
a@@ssertFalse(objectMapper.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));

/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.web.servlet.config.annotation;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.ViewResolverComposite;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor;
import org.springframework.web.util.UrlPathHelper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * A test fixture with an {@link WebMvcConfigurationSupport} instance.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @author Sebastien Deleuze
 */
public class WebMvcConfigurationSupportTests {


	@Test
	public void requestMappingHandlerMapping() throws Exception {
		ApplicationContext context = initContext(WebConfig.class, ScopedController.class, ScopedProxyController.class);
		RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
		assertEquals(0, handlerMapping.getOrder());

		HandlerExecutionChain chain = handlerMapping.getHandler(new MockHttpServletRequest("GET", "/"));
		assertNotNull(chain);
		assertNotNull(chain.getInterceptors());
		assertEquals(ConversionServiceExposingInterceptor.class, chain.getInterceptors()[0].getClass());

		chain = handlerMapping.getHandler(new MockHttpServletRequest("GET", "/scoped"));
		assertNotNull(chain);

		chain = handlerMapping.getHandler(new MockHttpServletRequest("GET", "/scopedProxy"));
		assertNotNull(chain);
	}

	@Test
	public void emptyViewControllerHandlerMapping() {
		ApplicationContext context = initContext(WebConfig.class);
		String name = "viewControllerHandlerMapping";
		AbstractHandlerMapping handlerMapping = context.getBean(name, AbstractHandlerMapping.class);

		assertNotNull(handlerMapping);
		assertEquals(Integer.MAX_VALUE, handlerMapping.getOrder());
		assertTrue(handlerMapping.getClass().getName().endsWith("EmptyHandlerMapping"));
	}

	@Test
	public void beanNameHandlerMapping() throws Exception {
		ApplicationContext context = initContext(WebConfig.class);
		BeanNameUrlHandlerMapping handlerMapping = context.getBean(BeanNameUrlHandlerMapping.class);
		assertEquals(2, handlerMapping.getOrder());

		HttpServletRequest request = new MockHttpServletRequest("GET", "/testController");
		HandlerExecutionChain chain = handlerMapping.getHandler(request);

		assertNotNull(chain.getInterceptors());
		assertEquals(3, chain.getInterceptors().length);
		assertEquals(ConversionServiceExposingInterceptor.class, chain.getInterceptors()[1].getClass());
		assertEquals(ResourceUrlProviderExposingInterceptor.class, chain.getInterceptors()[2].getClass());
	}

	@Test
	public void emptyResourceHandlerMapping() {
		ApplicationContext context = initContext(WebConfig.class);
		AbstractHandlerMapping handlerMapping = context.getBean("resourceHandlerMapping", AbstractHandlerMapping.class);

		assertNotNull(handlerMapping);
		assertEquals(Integer.MAX_VALUE, handlerMapping.getOrder());
		assertTrue(handlerMapping.getClass().getName().endsWith("EmptyHandlerMapping"));
	}

	@Test
	public void emptyDefaultServletHandlerMapping() {
		ApplicationContext context = initContext(WebConfig.class);
		String name = "defaultServletHandlerMapping";
		AbstractHandlerMapping handlerMapping = context.getBean(name, AbstractHandlerMapping.class);

		assertNotNull(handlerMapping);
		assertEquals(Integer.MAX_VALUE, handlerMapping.getOrder());
		assertTrue(handlerMapping.getClass().getName().endsWith("EmptyHandlerMapping"));
	}

	@Test
	public void requestMappingHandlerAdapter() throws Exception {
		ApplicationContext context = initContext(WebConfig.class);
		RequestMappingHandlerAdapter adapter = context.getBean(RequestMappingHandlerAdapter.class);
		List<HttpMessageConverter<?>> converters = adapter.getMessageConverters();
		assertEquals(9, converters.size());
		for(HttpMessageConverter<?> converter : converters) {
			if(converter instanceof AbstractJackson2HttpMessageConverter) {
				ObjectMapper objectMapper = ((AbstractJackson2HttpMessageConverter)converter).getObjectMapper();
				assertTrue(objectMapper.getDeserializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
				assertTrue(objectMapper.getSerializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
				assertTrue(objectMapper.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
				if(converter instanceof MappingJackson2XmlHttpMessageConverter) {
					assertEquals(XmlMapper.class, objectMapper.getClass());
				}
			}
		}

		ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) adapter.getWebBindingInitializer();
		assertNotNull(initializer);

		ConversionService conversionService = initializer.getConversionService();
		assertNotNull(conversionService);
		assertTrue(conversionService instanceof FormattingConversionService);

		Validator validator = initializer.getValidator();
		assertNotNull(validator);
		assertTrue(validator instanceof LocalValidatorFactoryBean);

		DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(adapter);
		List<Object> interceptors = (List<Object>) fieldAccessor.getPropertyValue("responseBodyAdvice");
		assertEquals(1, interceptors.size());
		assertEquals(JsonViewResponseBodyAdvice.class, interceptors.get(0).getClass());
	}

	@Test
	public void uriComponentsContributor() throws Exception {
		ApplicationContext context = initContext(WebConfig.class);
		CompositeUriComponentsContributor uriComponentsContributor = context.getBean(
				MvcUriComponentsBuilder.MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME,
				CompositeUriComponentsContributor.class);

		assertNotNull(uriComponentsContributor);
	}

	@Test
	public void handlerExceptionResolver() throws Exception {
		ApplicationContext context = initContext(WebConfig.class);
		HandlerExceptionResolverComposite compositeResolver =
			context.getBean("handlerExceptionResolver", HandlerExceptionResolverComposite.class);

		assertEquals(0, compositeResolver.getOrder());

		List<HandlerExceptionResolver> expectedResolvers = compositeResolver.getExceptionResolvers();

		assertEquals(ExceptionHandlerExceptionResolver.class, expectedResolvers.get(0).getClass());
		assertEquals(ResponseStatusExceptionResolver.class, expectedResolvers.get(1).getClass());
		assertEquals(DefaultHandlerExceptionResolver.class, expectedResolvers.get(2).getClass());

		ExceptionHandlerExceptionResolver eher = (ExceptionHandlerExceptionResolver) expectedResolvers.get(0);
		assertNotNull(eher.getApplicationContext());

		DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(eher);
		List<Object> interceptors = (List<Object>) fieldAccessor.getPropertyValue("responseBodyAdvice");
		assertEquals(1, interceptors.size());
		assertEquals(JsonViewResponseBodyAdvice.class, interceptors.get(0).getClass());
	}

	@Test
	public void mvcViewResolver() {
		ApplicationContext context = initContext(WebConfig.class);
		ViewResolverComposite resolver = context.getBean("mvcViewResolver", ViewResolverComposite.class);

		Map<String, ViewResolver> map = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				context, ViewResolver.class, true, false);

		assertNotNull(resolver);
		assertEquals(1, resolver.getViewResolvers().size());
		assertEquals(InternalResourceViewResolver.class, resolver.getViewResolvers().get(0).getClass());
		assertEquals(Ordered.LOWEST_PRECEDENCE, resolver.getOrder());
	}

	@Test
	public void mvcViewResolverWithExistingResolver() throws Exception {
		ApplicationContext context = initContext(WebConfig.class, ViewResolverConfig.class);
		ViewResolverComposite resolver = context.getBean("mvcViewResolver", ViewResolverComposite.class);

		assertNotNull(resolver);
		assertEquals(0, resolver.getViewResolvers().size());
		assertEquals(Ordered.LOWEST_PRECEDENCE, resolver.getOrder());
		assertNull(resolver.resolveViewName("anyViewName", Locale.ENGLISH));
	}

	@Test
	public void defaultPathMatchConfiguration() throws Exception {
		ApplicationContext context = initContext(WebConfig.class);
		UrlPathHelper urlPathHelper = context.getBean(UrlPathHelper.class);
		PathMatcher pathMatcher = context.getBean(PathMatcher.class);

		assertNotNull(urlPathHelper);
		assertNotNull(pathMatcher);
		assertEquals(AntPathMatcher.class, pathMatcher.getClass());
	}


	private ApplicationContext initContext(Class... configClasses) {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setServletContext(new MockServletContext());
		context.register(configClasses);
		context.refresh();
		return context;
	}


	@EnableWebMvc
	@Configuration
	public static class WebConfig {

		@Bean(name="/testController")
		public TestController testController() {
			return new TestController();
		}
	}

	@Configuration
	public static class ViewResolverConfig {

		@Bean
		public ViewResolver beanNameViewResolver() {
			return new BeanNameViewResolver();
		}
	}


	@Controller
	public static class TestController {

		@RequestMapping("/")
		public void handle() {
		}

		@RequestMapping("/foo/{id}/bar/{date}")
		public HttpEntity<Void> methodWithTwoPathVariables(@PathVariable Integer id,
				@DateTimeFormat(iso = ISO.DATE) @PathVariable DateTime date) {
			return null;
		}
	}


	@Controller
	@Scope("prototype")
	public static class ScopedController {

		@RequestMapping("/scoped")
		public void handle() {
		}
	}


	@Controller
	@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)
	public static class ScopedProxyController {

		@RequestMapping("/scopedProxy")
		public void handle() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7709.java