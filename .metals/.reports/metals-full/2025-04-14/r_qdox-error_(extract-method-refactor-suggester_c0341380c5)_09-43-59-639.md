error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12594.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12594.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12594.java
text:
```scala
a@@ssertEquals(false, new DirectFieldAccessor(adapter).getPropertyValue("ignoreDefaultModelOnRedirect"));

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

package org.springframework.web.servlet.config.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.TestBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.method.annotation.support.ModelAttributeMethodProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * A test fixture for {@link WebMvcConfigurationSupport}.
 *
 * @author Rossen Stoyanchev
 */
public class WebMvcConfigurationSupportTests {

	private TestWebMvcConfiguration mvcConfiguration;

	@Before
	public void setUp() {
		mvcConfiguration = new TestWebMvcConfiguration();
	}
	
	@Test
	public void requestMappingHandlerMapping() throws Exception {
		StaticWebApplicationContext cxt = new StaticWebApplicationContext();
		cxt.registerSingleton("controller", TestController.class);
		
		RequestMappingHandlerMapping handlerMapping = mvcConfiguration.requestMappingHandlerMapping();
		assertEquals(0, handlerMapping.getOrder());

		handlerMapping.setApplicationContext(cxt);
		HandlerExecutionChain chain = handlerMapping.getHandler(new MockHttpServletRequest("GET", "/"));
		assertNotNull(chain.getInterceptors());
		assertEquals(ConversionServiceExposingInterceptor.class, chain.getInterceptors()[0].getClass());
	}

	@Test
	public void emptyViewControllerHandlerMapping() {
		AbstractHandlerMapping handlerMapping = (AbstractHandlerMapping) mvcConfiguration.viewControllerHandlerMapping();
		assertNotNull(handlerMapping);
		assertEquals(Integer.MAX_VALUE, handlerMapping.getOrder());
		assertTrue(handlerMapping.getClass().getName().endsWith("EmptyHandlerMapping"));
	}
	
	@Test
	public void beanNameHandlerMapping() throws Exception {
		StaticWebApplicationContext cxt = new StaticWebApplicationContext();
		cxt.registerSingleton("/controller", TestController.class);

		HttpServletRequest request = new MockHttpServletRequest("GET", "/controller");

		BeanNameUrlHandlerMapping handlerMapping = mvcConfiguration.beanNameHandlerMapping();
		assertEquals(2, handlerMapping.getOrder());

		handlerMapping.setApplicationContext(cxt);
		HandlerExecutionChain chain = handlerMapping.getHandler(request);
		assertNotNull(chain.getInterceptors());
		assertEquals(2, chain.getInterceptors().length);
		assertEquals(ConversionServiceExposingInterceptor.class, chain.getInterceptors()[1].getClass());
	}
	
	@Test
	public void emptyResourceHandlerMapping() {
		mvcConfiguration.setApplicationContext(new StaticWebApplicationContext());
		AbstractHandlerMapping handlerMapping = (AbstractHandlerMapping) mvcConfiguration.resourceHandlerMapping();
		assertNotNull(handlerMapping);
		assertEquals(Integer.MAX_VALUE, handlerMapping.getOrder());
		assertTrue(handlerMapping.getClass().getName().endsWith("EmptyHandlerMapping"));
	}
	
	@Test
	public void emptyDefaultServletHandlerMapping() {
		mvcConfiguration.setServletContext(new MockServletContext());
		AbstractHandlerMapping handlerMapping = (AbstractHandlerMapping) mvcConfiguration.defaultServletHandlerMapping();
		assertNotNull(handlerMapping);
		assertEquals(Integer.MAX_VALUE, handlerMapping.getOrder());
		assertTrue(handlerMapping.getClass().getName().endsWith("EmptyHandlerMapping"));
	}
	
	@Test
	public void requestMappingHandlerAdapter() throws Exception {
		RequestMappingHandlerAdapter adapter = mvcConfiguration.requestMappingHandlerAdapter();

		List<HttpMessageConverter<?>> expectedConverters = new ArrayList<HttpMessageConverter<?>>();
		mvcConfiguration.addDefaultHttpMessageConverters(expectedConverters);
		assertEquals(expectedConverters.size(), adapter.getMessageConverters().size());

		ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) adapter.getWebBindingInitializer();
		assertNotNull(initializer);

		ConversionService conversionService = initializer.getConversionService();
		assertNotNull(conversionService);
		assertTrue(conversionService instanceof FormattingConversionService);
		
		Validator validator = initializer.getValidator();
		assertNotNull(validator);
		assertTrue(validator instanceof LocalValidatorFactoryBean);
		
		assertEquals(true, new DirectFieldAccessor(adapter).getPropertyValue("ignoreDefaultModelOnRedirect"));
	}
	
	@Test
	public void handlerExceptionResolver() throws Exception {
		HandlerExceptionResolverComposite compositeResolver = 
			(HandlerExceptionResolverComposite) mvcConfiguration.handlerExceptionResolver();
		
		assertEquals(0, compositeResolver.getOrder());

		List<HandlerExceptionResolver> expectedResolvers = new ArrayList<HandlerExceptionResolver>();
		mvcConfiguration.addDefaultHandlerExceptionResolvers(expectedResolvers);
		assertEquals(expectedResolvers.size(), compositeResolver.getExceptionResolvers().size());
	}
	
	@Test 
	public void webMvcConfigurerExtensionHooks() throws Exception {
		
		StaticWebApplicationContext appCxt = new StaticWebApplicationContext();
		appCxt.setServletContext(new MockServletContext(new FileSystemResourceLoader()));
		appCxt.registerSingleton("controller", TestController.class);

		WebConfig webConfig = new WebConfig();
		webConfig.setApplicationContext(appCxt);
		webConfig.setServletContext(appCxt.getServletContext());
		
		String actual = webConfig.mvcConversionService().convert(new TestBean(), String.class);
		assertEquals("converted", actual);

		RequestMappingHandlerAdapter adapter = webConfig.requestMappingHandlerAdapter();
		assertEquals(1, adapter.getMessageConverters().size());
		
		ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) adapter.getWebBindingInitializer();
		assertNotNull(initializer);
		
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(null, "");
		initializer.getValidator().validate(null, bindingResult);
		assertEquals("invalid", bindingResult.getAllErrors().get(0).getCode());

		@SuppressWarnings("unchecked")
		List<HandlerMethodArgumentResolver> argResolvers= (List<HandlerMethodArgumentResolver>) 
			new DirectFieldAccessor(adapter).getPropertyValue("customArgumentResolvers");
		assertEquals(1, argResolvers.size());

		@SuppressWarnings("unchecked")
		List<HandlerMethodReturnValueHandler> handlers = (List<HandlerMethodReturnValueHandler>) 
			new DirectFieldAccessor(adapter).getPropertyValue("customReturnValueHandlers");
		assertEquals(1, handlers.size());
		
		HandlerExceptionResolverComposite composite = (HandlerExceptionResolverComposite) webConfig.handlerExceptionResolver();
		assertEquals(1, composite.getExceptionResolvers().size());
		
		RequestMappingHandlerMapping rmHandlerMapping = webConfig.requestMappingHandlerMapping();
		rmHandlerMapping.setApplicationContext(appCxt);
		HandlerExecutionChain chain = rmHandlerMapping.getHandler(new MockHttpServletRequest("GET", "/"));
		assertNotNull(chain.getInterceptors());
		assertEquals(2, chain.getInterceptors().length);
		assertEquals(LocaleChangeInterceptor.class, chain.getInterceptors()[0].getClass());
		assertEquals(ConversionServiceExposingInterceptor.class, chain.getInterceptors()[1].getClass());

		AbstractHandlerMapping handlerMapping = (AbstractHandlerMapping) webConfig.viewControllerHandlerMapping();
		handlerMapping.setApplicationContext(appCxt);
		assertNotNull(handlerMapping);
		assertEquals(1, handlerMapping.getOrder());
		HandlerExecutionChain handler = handlerMapping.getHandler(new MockHttpServletRequest("GET", "/path"));
		assertNotNull(handler.getHandler());

		handlerMapping = (AbstractHandlerMapping) webConfig.resourceHandlerMapping();
		handlerMapping.setApplicationContext(appCxt);
		assertNotNull(handlerMapping);
		assertEquals(Integer.MAX_VALUE-1, handlerMapping.getOrder());
		handler = handlerMapping.getHandler(new MockHttpServletRequest("GET", "/resources/foo.gif"));
		assertNotNull(handler.getHandler());

		handlerMapping = (AbstractHandlerMapping) webConfig.defaultServletHandlerMapping();
		handlerMapping.setApplicationContext(appCxt);
		assertNotNull(handlerMapping);
		assertEquals(Integer.MAX_VALUE, handlerMapping.getOrder());
		handler = handlerMapping.getHandler(new MockHttpServletRequest("GET", "/anyPath"));
		assertNotNull(handler.getHandler());
	}

	@Controller
	private static class TestController {
		
		@SuppressWarnings("unused")
		@RequestMapping("/")
		public void handle() {
		}
	}

	private static class TestWebMvcConfiguration extends WebMvcConfigurationSupport {
		
	}
	
	/**
	 * The purpose of this class is to test that an implementation of a {@link WebMvcConfigurer} 
	 * can also apply customizations by extension from {@link WebMvcConfigurationSupport}.
	 */
	private class WebConfig extends WebMvcConfigurationSupport implements WebMvcConfigurer {
		
		@Override
		public void addFormatters(FormatterRegistry registry) {
			registry.addConverter(new Converter<TestBean, String>() {
				public String convert(TestBean source) {
					return "converted";
				}
			});
		}

		@Override
		public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
			converters.add(new MappingJacksonHttpMessageConverter());
		}

		@Override
		public Validator getValidator() {
			return new Validator() {
				public void validate(Object target, Errors errors) {
					errors.reject("invalid");
				}
				public boolean supports(Class<?> clazz) {
					return true;
				}
			};
		}

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(new ModelAttributeMethodProcessor(true));
		}

		@Override
		public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
			returnValueHandlers.add(new ModelAttributeMethodProcessor(true));
		}

		@Override
		public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
			exceptionResolvers.add(new SimpleMappingExceptionResolver());
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(new LocaleChangeInterceptor());
		}

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/path");
		}

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/resources/**").addResourceLocations("src/test/java");
		}

		@Override
		public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
			configurer.enable("default");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12594.java