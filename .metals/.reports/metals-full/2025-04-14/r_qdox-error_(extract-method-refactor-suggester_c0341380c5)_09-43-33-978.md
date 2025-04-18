error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10173.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10173.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10173.java
text:
```scala
s@@uper("", false, ValueConstants.DEFAULT_NONE);

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

import java.beans.PropertyEditor;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.util.WebUtils;

/**
 * Resolves method arguments annotated with @{@link RequestParam}, arguments of 
 * type {@link MultipartFile} in conjunction with Spring's {@link MultipartResolver} 
 * abstraction, and arguments of type {@code javax.servlet.http.Part} in conjunction 
 * with Servlet 3.0 multipart requests. This resolver can also be created in default 
 * resolution mode in which simple types (int, long, etc.) not annotated 
 * with @{@link RequestParam} are also treated as request parameters with the 
 * parameter name derived from the argument name.
 * 
 * <p>If the method parameter type is {@link Map}, the request parameter name is used to 
 * resolve the request parameter String value. The value is then converted to a {@link Map} 
 * via type conversion assuming a suitable {@link Converter} or {@link PropertyEditor} has 
 * been registered. If a request parameter name is not specified with a {@link Map} method 
 * parameter type, the {@link RequestParamMapMethodArgumentResolver} is used instead 
 * providing access to all request parameters in the form of a map.
 * 
 * <p>A {@link WebDataBinder} is invoked to apply type conversion to resolved request header values that 
 * don't yet match the method parameter type.
 * 
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @since 3.1
 * @see RequestParamMapMethodArgumentResolver
 */
public class RequestParamMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

	private final boolean useDefaultResolution;

	/**
	 * @param beanFactory a bean factory to use for resolving  ${...} placeholder and #{...} SpEL expressions 
	 * in default values, or {@code null} if default values are not expected to contain expressions
	 * @param useDefaultResolution in default resolution mode a method argument that is a simple type, as
	 * defined in {@link BeanUtils#isSimpleProperty(Class)}, is treated as a request parameter even if it doesn't have
	 * an @{@link RequestParam} annotation, the request parameter name is derived from the method parameter name.
	 */
	public RequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory, 
											  boolean useDefaultResolution) {
		super(beanFactory);
		this.useDefaultResolution = useDefaultResolution;
	}

	/**
	 * Supports the following:
	 * <ul>
	 * 	<li>@RequestParam-annotated method arguments. 
	 * 		This excludes {@link Map} parameters where the annotation does not specify a name value.
	 * 		See {@link RequestParamMapMethodArgumentResolver} instead for such parameters.
	 * 	<li>Arguments of type {@link MultipartFile} unless annotated with {@link RequestPart}.
	 * 	<li>Arguments of type {@code javax.servlet.http.Part} unless annotated with {@link RequestPart}.
	 * 	<li>In default resolution mode, simple type arguments even if not with @RequestParam.
	 * </ul>
	 */
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> paramType = parameter.getParameterType();
		if (parameter.hasParameterAnnotation(RequestParam.class)) {
			if (Map.class.isAssignableFrom(paramType)) {
				String paramName = parameter.getParameterAnnotation(RequestParam.class).value();
				return StringUtils.hasText(paramName);
			}
			else {
				return true;
			}
		}
		else {
			if (parameter.hasParameterAnnotation(RequestPart.class)) {
				return false;
			}
			else if (MultipartFile.class.equals(paramType) || "javax.servlet.http.Part".equals(paramType.getName())) {
				return true;
			}
			else if (this.useDefaultResolution) {
				return BeanUtils.isSimpleProperty(paramType);
			}
			else {
				return false;
			}
		}
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		RequestParam annotation = parameter.getParameterAnnotation(RequestParam.class);
		return (annotation != null) ? 
				new RequestParamNamedValueInfo(annotation) : 
				new RequestParamNamedValueInfo();
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {

		Object arg;
		
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		MultipartHttpServletRequest multipartRequest = 
			WebUtils.getNativeRequest(servletRequest, MultipartHttpServletRequest.class);

		if (MultipartFile.class.equals(parameter.getParameterType())) {
			assertMultipartRequest(multipartRequest, webRequest);
			arg = multipartRequest.getFile(name);
		}
		else if (isMultipartFileCollection(parameter)) {
			assertMultipartRequest(multipartRequest, webRequest);
			arg = multipartRequest.getFiles(name);
		}
		else if ("javax.servlet.http.Part".equals(parameter.getParameterType().getName())) {
			arg = servletRequest.getPart(name);
		}
		else {
			arg = null;
			if (multipartRequest != null) {
				List<MultipartFile> files = multipartRequest.getFiles(name);
				if (!files.isEmpty()) {
					arg = (files.size() == 1 ? files.get(0) : files);
				}
			}
			if (arg == null) {
				String[] paramValues = webRequest.getParameterValues(name);
				if (paramValues != null) {
					arg = paramValues.length == 1 ? paramValues[0] : paramValues;
				}
			}
		}
		
		return arg;
	}

	private void assertMultipartRequest(MultipartHttpServletRequest multipartRequest, NativeWebRequest request) {
		if (multipartRequest == null) {
			throw new IllegalStateException("Current request is not of type [" + MultipartRequest.class.getName()
					+ "]: " + request + ". Do you have a MultipartResolver configured?");
		}
	}
	
	private boolean isMultipartFileCollection(MethodParameter parameter) {
		Class<?> paramType = parameter.getParameterType();
		if (Collection.class.equals(paramType) || List.class.isAssignableFrom(paramType)){
			Class<?> valueType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
			if (valueType != null && valueType.equals(MultipartFile.class)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void handleMissingValue(String paramName, MethodParameter parameter) throws ServletException {
		throw new MissingServletRequestParameterException(paramName, parameter.getParameterType().getSimpleName());
	}

	private class RequestParamNamedValueInfo extends NamedValueInfo {

		private RequestParamNamedValueInfo() {
			super("", true, ValueConstants.DEFAULT_NONE);
		}
		
		private RequestParamNamedValueInfo(RequestParam annotation) {
			super(annotation.value(), annotation.required(), annotation.defaultValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10173.java