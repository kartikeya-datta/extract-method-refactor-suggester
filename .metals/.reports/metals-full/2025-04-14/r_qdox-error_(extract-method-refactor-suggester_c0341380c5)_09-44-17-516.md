error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9329.java
text:
```scala
a@@rg = new ArrayList<Object>(servletRequest.getParts());

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

package org.springframework.web.servlet.mvc.method.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

/**
 * Resolves the following method arguments:
 * <ul>
 * 	<li>Annotated with {@code @RequestPart}
 * 	<li>Of type {@link MultipartFile} in conjunction with Spring's
 *  {@link MultipartResolver} abstraction
 * 	<li>Of type {@code javax.servlet.http.Part} in conjunction with
 * 	Servlet 3.0 multipart requests
 * </ul>
 *
 * <p>When a parameter is annotated with {@code @RequestPart} the content of the
 * part is passed through an {@link HttpMessageConverter} to resolve the method
 * argument with the 'Content-Type' of the request part in mind. This is
 * analogous to what @{@link RequestBody} does to resolve an argument based on
 * the content of a regular request.
 *
 * <p>When a parameter is not annotated or the name of the part is not specified,
 * it is derived from the name of the method argument.
 *
 * <p>Automatic validation may be applied if the argument is annotated with
 * {@code @javax.validation.Valid}. In case of validation failure, a
 * {@link MethodArgumentNotValidException} is raised and a 400 response status
 * code returned if {@link DefaultHandlerExceptionResolver} is configured.
 *
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class RequestPartMethodArgumentResolver extends AbstractMessageConverterMethodArgumentResolver {

	public RequestPartMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}

	/**
	 * Supports the following:
	 * <ul>
	 * 	<li>Annotated with {@code @RequestPart}
	 * 	<li>Of type {@link MultipartFile} unless annotated with {@code @RequestParam}.
	 * 	<li>Of type {@code javax.servlet.http.Part} unless annotated with {@code @RequestParam}.
	 * </ul>
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(RequestPart.class)) {
			return true;
		}
		else {
			if (parameter.hasParameterAnnotation(RequestParam.class)){
				return false;
			}
			else if (MultipartFile.class.equals(parameter.getParameterType())) {
				return true;
			}
			else if ("javax.servlet.http.Part".equals(parameter.getParameterType().getName())) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		assertIsMultipartRequest(servletRequest);

		MultipartHttpServletRequest multipartRequest =
			WebUtils.getNativeRequest(servletRequest, MultipartHttpServletRequest.class);

		String partName = getPartName(parameter);
		Object arg;

		if (MultipartFile.class.equals(parameter.getParameterType())) {
			Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
			arg = multipartRequest.getFile(partName);
		}
		else if (isMultipartFileCollection(parameter)) {
			Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
			arg = multipartRequest.getFiles(partName);
		}
		else if ("javax.servlet.http.Part".equals(parameter.getParameterType().getName())) {
			assertIsMultipartRequest(servletRequest);
			arg = servletRequest.getPart(partName);
		}
		else if (isPartCollection(parameter)) {
			assertIsMultipartRequest(servletRequest);
			arg = new ArrayList(servletRequest.getParts());
		}
		else {
			try {
				HttpInputMessage inputMessage = new RequestPartServletServerHttpRequest(servletRequest, partName);
				arg = readWithMessageConverters(inputMessage, parameter, parameter.getParameterType());
				WebDataBinder binder = binderFactory.createBinder(request, arg, partName);
				if (arg != null) {
					validate(binder, parameter);
				}
				mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + partName, binder.getBindingResult());
			}
			catch (MissingServletRequestPartException ex) {
				// handled below
				arg = null;
			}
		}

		RequestPart annot = parameter.getParameterAnnotation(RequestPart.class);
		boolean isRequired = (annot == null || annot.required());

		if (arg == null && isRequired) {
			throw new MissingServletRequestPartException(partName);
		}

		return arg;
	}

	private static void assertIsMultipartRequest(HttpServletRequest request) {
		String contentType = request.getContentType();
		if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) {
			throw new MultipartException("The current request is not a multipart request");
		}
	}

	private String getPartName(MethodParameter parameter) {
		RequestPart annot = parameter.getParameterAnnotation(RequestPart.class);
		String partName = (annot != null) ? annot.value() : "";
		if (partName.length() == 0) {
			partName = parameter.getParameterName();
			Assert.notNull(partName, "Request part name for argument type [" + parameter.getParameterType().getName()
					+ "] not available, and parameter name information not found in class file either.");
		}
		return partName;
	}

	private boolean isMultipartFileCollection(MethodParameter parameter) {
		Class<?> collectionType = getCollectionParameterType(parameter);
		return ((collectionType != null) && collectionType.equals(MultipartFile.class));
	}

	private boolean isPartCollection(MethodParameter parameter) {
		Class<?> collectionType = getCollectionParameterType(parameter);
		return ((collectionType != null) && "javax.servlet.http.Part".equals(collectionType.getName()));
	}

	private Class<?> getCollectionParameterType(MethodParameter parameter) {
		Class<?> paramType = parameter.getParameterType();
		if (Collection.class.equals(paramType) || List.class.isAssignableFrom(paramType)){
			Class<?> valueType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
			if (valueType != null) {
				return valueType;
			}
		}
		return null;
	}

	private void validate(WebDataBinder binder, MethodParameter parameter) throws MethodArgumentNotValidException {

		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if (annot.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = AnnotationUtils.getValue(annot);
				binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
				BindingResult bindingResult = binder.getBindingResult();
				if (bindingResult.hasErrors()) {
					if (isBindExceptionRequired(binder, parameter)) {
						throw new MethodArgumentNotValidException(parameter, bindingResult);
					}
				}
			}
		}
	}

	/**
	 * Whether to raise a {@link MethodArgumentNotValidException} on validation errors.
	 * @param binder the data binder used to perform data binding
	 * @param parameter the method argument
	 * @return {@code true} if the next method argument is not of type {@link Errors}.
	 */
	private boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));

		return !hasBindingResult;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9329.java