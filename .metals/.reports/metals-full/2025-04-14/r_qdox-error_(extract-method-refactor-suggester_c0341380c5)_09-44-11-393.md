error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13909.java
text:
```scala
a@@ssertTrue(processor.supportsReturnType(returnParamNonSimpleType));

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

package org.springframework.web.method.annotation.support;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.TestBean;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Test fixture with {@link ModelAttributeMethodProcessor}.
 * 
 * @author Rossen Stoyanchev
 */
public class ModelAttributeMethodProcessorTests {

	private ModelAttributeMethodProcessor processor;
	
	private MethodParameter paramNamedValidModelAttr;

	private MethodParameter paramErrors;

	private MethodParameter paramInt;

	private MethodParameter paramModelAttr;

	private MethodParameter paramNonSimpleType;
	
	private MethodParameter returnParamNamedModelAttr;
	
	private MethodParameter returnParamNonSimpleType;

	private ModelAndViewContainer mavContainer;

	private NativeWebRequest webRequest;
	
	@Before
	public void setUp() throws Exception {
		processor = new ModelAttributeMethodProcessor(false);

		Method method = ModelAttributeHandler.class.getDeclaredMethod("modelAttribute", 
				TestBean.class, Errors.class, int.class, TestBean.class, TestBean.class);
		
		paramNamedValidModelAttr = new MethodParameter(method, 0);
		paramErrors = new MethodParameter(method, 1);
		paramInt = new MethodParameter(method, 2);
		paramModelAttr = new MethodParameter(method, 3);
		paramNonSimpleType = new MethodParameter(method, 4);
		
		returnParamNamedModelAttr = new MethodParameter(getClass().getDeclaredMethod("annotatedReturnValue"), -1);
		returnParamNonSimpleType = new MethodParameter(getClass().getDeclaredMethod("notAnnotatedReturnValue"), -1);
		
		mavContainer = new ModelAndViewContainer();

		webRequest = new ServletWebRequest(new MockHttpServletRequest());
	}

	@Test
	public void supportedParameters() throws Exception {
		// Only @ModelAttribute arguments
		assertTrue(processor.supportsParameter(paramNamedValidModelAttr));
		assertTrue(processor.supportsParameter(paramModelAttr));
		
		assertFalse(processor.supportsParameter(paramErrors));
		assertFalse(processor.supportsParameter(paramInt));
		assertFalse(processor.supportsParameter(paramNonSimpleType));
	}
	
	@Test
	public void supportedParametersInDefaultResolutionMode() throws Exception {
		processor = new ModelAttributeMethodProcessor(true);
		
		// Only non-simple types, even if not annotated
		assertTrue(processor.supportsParameter(paramNamedValidModelAttr));
		assertTrue(processor.supportsParameter(paramErrors));
		assertTrue(processor.supportsParameter(paramModelAttr));
		assertTrue(processor.supportsParameter(paramNonSimpleType));

		assertFalse(processor.supportsParameter(paramInt));
	}
	
	@Test
	public void supportedReturnTypes() throws Exception {
		processor = new ModelAttributeMethodProcessor(false);
		assertTrue(processor.supportsReturnType(returnParamNamedModelAttr));
		assertFalse(processor.supportsReturnType(returnParamNonSimpleType));
	}
	
	@Test
	public void supportedReturnTypesInDefaultResolutionMode() throws Exception {
		processor = new ModelAttributeMethodProcessor(true);
		assertTrue(processor.supportsReturnType(returnParamNamedModelAttr));
		assertFalse(processor.supportsReturnType(returnParamNonSimpleType));
	}
	
	@Test
	public void validationApplicable() throws Exception {
		assertTrue(processor.isValidationApplicable(null, paramNamedValidModelAttr));
	}
	
	@Test
	public void validationNotApplicable() throws Exception {
		assertFalse(processor.isValidationApplicable(null, paramNonSimpleType));
	}

	@Test
	public void bindExceptionRequired() throws Exception {
		assertTrue(processor.isBindExceptionRequired(null, paramNonSimpleType));
	}

	@Test
	public void bindExceptionNotRequired() throws Exception {
		assertFalse(processor.isBindExceptionRequired(null, paramNamedValidModelAttr));
	}

	@Test
	public void getAttributeFromModel() throws Exception {
		testGetAttributeFromModel("attrName", paramNamedValidModelAttr);
		testGetAttributeFromModel("testBean", paramModelAttr);
		testGetAttributeFromModel("testBean", paramNonSimpleType);
	}

	private void testGetAttributeFromModel(String expectedAttributeName, MethodParameter param) throws Exception {
		Object target = new TestBean();
		mavContainer.addAttribute(expectedAttributeName, target);

		WebDataBinder dataBinder = new WebRequestDataBinder(target);
		WebDataBinderFactory factory = createMock(WebDataBinderFactory.class);
		expect(factory.createBinder(webRequest, target, expectedAttributeName)).andReturn(dataBinder);
		replay(factory);
		
		processor.resolveArgument(param, mavContainer, webRequest, factory);
		
		verify(factory);
	}

	@Test
	public void createAttribute() throws Exception {
		WebDataBinder dataBinder = new WebRequestDataBinder(null);

		WebDataBinderFactory factory = createMock(WebDataBinderFactory.class);
		expect(factory.createBinder((NativeWebRequest) anyObject(), notNull(), eq("attrName"))).andReturn(dataBinder);
		replay(factory);
		
		processor.resolveArgument(paramNamedValidModelAttr, mavContainer, webRequest, factory);
		
		verify(factory);
	}

	@Test
	public void automaticValidation() throws Exception {
		Object target = new TestBean();
		mavContainer.addAttribute("attrName", target);
		
		StubRequestDataBinder dataBinder = new StubRequestDataBinder(target);
		WebDataBinderFactory binderFactory = createMock(WebDataBinderFactory.class);
		expect(binderFactory.createBinder(webRequest, target, "attrName")).andReturn(dataBinder);
		replay(binderFactory);
		
		processor.resolveArgument(paramNamedValidModelAttr, mavContainer, webRequest, binderFactory);

		assertTrue(dataBinder.isBindInvoked());
		assertTrue(dataBinder.isValidateInvoked());
	}
	
	@Test(expected=BindException.class)
	public void bindException() throws Exception {
		Object target = new TestBean();
		mavContainer.getModel().addAttribute(target);

		StubRequestDataBinder dataBinder = new StubRequestDataBinder(target);
		dataBinder.getBindingResult().reject("error");

		WebDataBinderFactory binderFactory = createMock(WebDataBinderFactory.class);
		expect(binderFactory.createBinder(webRequest, target, "testBean")).andReturn(dataBinder);
		replay(binderFactory);
		
		processor.resolveArgument(paramNonSimpleType, mavContainer, webRequest, binderFactory);
	}
	
	@Test
	public void handleAnnotatedReturnValue() throws Exception {
		processor.handleReturnValue("expected", returnParamNamedModelAttr, mavContainer, webRequest);
		assertEquals("expected", mavContainer.getModel().get("modelAttrName"));
	}

	@Test
	public void handleNotAnnotatedReturnValue() throws Exception {
		TestBean testBean = new TestBean("expected");
		processor.handleReturnValue(testBean, returnParamNonSimpleType, mavContainer, webRequest);
		
		assertSame(testBean, mavContainer.getModel().get("testBean"));
	}
	
	private static class StubRequestDataBinder extends WebRequestDataBinder {
		
		private boolean bindInvoked;
		
		private boolean validateInvoked;

		public StubRequestDataBinder(Object target) {
			super(target);
		}

		public boolean isBindInvoked() {
			return bindInvoked;
		}

		public boolean isValidateInvoked() {
			return validateInvoked;
		}

		public void bind(WebRequest request) {
			bindInvoked = true;
		}

		public void validate() {
			validateInvoked = true;
		}
	}

	@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	public @interface Valid {
	}

	@SessionAttributes(types=TestBean.class)
	private static class ModelAttributeHandler {
		@SuppressWarnings("unused")
		public void modelAttribute(@ModelAttribute("attrName") @Valid TestBean annotatedAttr, 
								   Errors errors,
								   int intArg,
								   @ModelAttribute TestBean defaultNameAttr,
								   TestBean notAnnotatedAttr) {
		}
	}

	@SuppressWarnings("unused")
	@ModelAttribute("modelAttrName")
	private String annotatedReturnValue() {
		return null;
	}

	@SuppressWarnings("unused")
	private TestBean notAnnotatedReturnValue() {
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13909.java