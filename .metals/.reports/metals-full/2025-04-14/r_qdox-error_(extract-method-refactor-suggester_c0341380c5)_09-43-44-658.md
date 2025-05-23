error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4979.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4979.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4979.java
text:
```scala
m@@avContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, request);

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

import java.lang.annotation.Annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Resolves method arguments annotated with @{@link ModelAttribute}. Or if created in default resolution mode,
 * resolves any non-simple type argument even without an @{@link ModelAttribute}. See the constructor for details.
 *
 * <p>A model attribute argument is obtained from the model or otherwise is created with a default constructor.
 * Data binding and validation are applied through a {@link WebDataBinder} instance. Validation is applied 
 * only when the argument is also annotated with {@code @Valid}.
 *
 * <p>Also handles return values from methods annotated with an @{@link ModelAttribute}. The return value is
 * added to the {@link ModelAndViewContainer}.
 *
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class ModelAttributeMethodProcessor implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler {

	protected Log logger = LogFactory.getLog(this.getClass());
	
	private final boolean useDefaultResolution;
	
	/**
	 * @param useDefaultResolution in default resolution mode a method argument that isn't a simple type, as
	 * defined in {@link BeanUtils#isSimpleProperty(Class)}, is treated as a model attribute even if it doesn't
	 * have an @{@link ModelAttribute} annotation with its name derived from the model attribute type.
	 */
	public ModelAttributeMethodProcessor(boolean useDefaultResolution) {
		this.useDefaultResolution = useDefaultResolution;
	}

	/**
	 * @return true if the parameter is annotated with {@link ModelAttribute} or if it is a
	 * 		simple type without any annotations.
	 */
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
			return true;
		}
		else if (this.useDefaultResolution) {
			return !BeanUtils.isSimpleProperty(parameter.getParameterType());
		}
		else {
			return false;
		}
	}

	/**
	 * Resolves the argument to a model attribute looking up the attribute in the model or instantiating it using its
	 * default constructor. Data binding and optionally validation is then applied through a {@link WebDataBinder}
	 * instance. Validation is invoked optionally when the method parameter is annotated with an {@code @Valid}.
	 *
	 * @throws BindException if data binding and validation result in an error and the next method parameter 
	 * is neither of type {@link Errors} nor {@link BindingResult}.
	 * @throws Exception if a {@link WebDataBinder} could not be created.
	 */
	public final Object resolveArgument(MethodParameter parameter,
										ModelAndViewContainer mavContainer,
										NativeWebRequest request,
										WebDataBinderFactory binderFactory) throws Exception {
		String name = ModelFactory.getNameForParameter(parameter);
		Object target = (mavContainer.containsAttribute(name)) ?
				mavContainer.getAttribute(name) : createAttribute(name, parameter, binderFactory, request);
				
		WebDataBinder binder = binderFactory.createBinder(request, target, name);

		if (binder.getTarget() != null) {
			bindRequestParameters(binder, request);
			
			if (isValidationApplicable(binder.getTarget(), parameter)) {
				binder.validate();
			}
			
			if (binder.getBindingResult().hasErrors()) {
				if (isBindExceptionRequired(binder, parameter)) {
					throw new BindException(binder.getBindingResult());
				}
			}
		}

		mavContainer.addAllAttributes(binder.getBindingResult().getModel());

		return binder.getTarget();
	}

	/**
	 * Creates an instance of the specified model attribute. This method is invoked only if the attribute is
	 * not available in the model. This default implementation uses the no-argument constructor. 
	 * Subclasses can override to provide additional means of creating the model attribute.
	 * 
	 * @param attributeName the name of the model attribute
	 * @param parameter the method argument declaring the model attribute
	 * @param binderFactory a factory for creating {@link WebDataBinder} instances
	 * @param request the current request
	 * @return the created model attribute; never {@code null}
	 * @throws Exception raised in the process of creating the instance
	 */
	protected Object createAttribute(String attributeName, 
									 MethodParameter parameter, 
									 WebDataBinderFactory binderFactory, 
									 NativeWebRequest request) throws Exception {
		return BeanUtils.instantiateClass(parameter.getParameterType());
	}
	
	/**
	 * Bind the request to the target object contained in the provided binder instance.
	 *
	 * @param binder the binder with the target object to apply request values to
	 * @param request the current request
	 */
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		((WebRequestDataBinder) binder).bind(request);
	}

	/**
	 * Whether to validate the given model attribute argument value.
	 * @param argumentValue the validation candidate
	 * @param parameter the method argument declaring the validation candidate
	 * @return {@code true} if validation should be applied, {@code false} otherwise.
	 */
	protected boolean isValidationApplicable(Object argumentValue, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if ("Valid".equals(annot.annotationType().getSimpleName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Whether to raise a {@link BindException} in case of data binding or validation errors.
	 * @param binder the binder on which validation is to be invoked
	 * @param parameter the method argument for which data binding is performed
	 * @return true if the binding or validation errors should result in a {@link BindException}, false otherwise.
	 */
	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		
		return !hasBindingResult;
	}

	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.getMethodAnnotation(ModelAttribute.class) != null;
	}

	public void handleReturnValue(Object returnValue,
								  MethodParameter returnType,
								  ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest) throws Exception {
		if (returnValue != null) {
			String name = ModelFactory.getNameForReturnValue(returnValue, returnType);
			mavContainer.addAttribute(name, returnValue);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4979.java