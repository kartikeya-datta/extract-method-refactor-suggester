error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7993.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7993.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7993.java
text:
```scala
r@@eturn genericConverter.read(targetType, contextClass, inputMessage);

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

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/**
 * A base class for resolving method argument values by reading from the body of
 * a request with {@link HttpMessageConverter}s.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public abstract class AbstractMessageConverterMethodArgumentResolver implements HandlerMethodArgumentResolver {

	protected final Log logger = LogFactory.getLog(getClass());

	protected final List<HttpMessageConverter<?>> messageConverters;

	protected final List<MediaType> allSupportedMediaTypes;

	public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters) {
		Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
		this.messageConverters = messageConverters;
		this.allSupportedMediaTypes = getAllSupportedMediaTypes(messageConverters);
	}

	/**
	 * Return the media types supported by all provided message converters sorted
	 * by specificity via {@link MediaType#sortBySpecificity(List)}.
	 */
	private static List<MediaType> getAllSupportedMediaTypes(List<HttpMessageConverter<?>> messageConverters) {
		Set<MediaType> allSupportedMediaTypes = new LinkedHashSet<MediaType>();
		for (HttpMessageConverter<?> messageConverter : messageConverters) {
			allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
		}
		List<MediaType> result = new ArrayList<MediaType>(allSupportedMediaTypes);
		MediaType.sortBySpecificity(result);
		return Collections.unmodifiableList(result);
	}

	/**
	 * Creates the method argument value of the expected parameter type by
	 * reading from the given request.
	 *
	 * @param <T> the expected type of the argument value to be created
	 * @param webRequest the current request
	 * @param methodParam the method argument
	 * @param paramType the type of the argument value to be created
	 * @return the created method argument value
	 * @throws IOException if the reading from the request fails
	 * @throws HttpMediaTypeNotSupportedException if no suitable message converter is found
	 */
	protected <T> Object readWithMessageConverters(NativeWebRequest webRequest,
			MethodParameter methodParam, Type paramType) throws IOException, HttpMediaTypeNotSupportedException {

		HttpInputMessage inputMessage = createInputMessage(webRequest);
		return readWithMessageConverters(inputMessage, methodParam, paramType);
	}

	/**
	 * Creates the method argument value of the expected parameter type by reading
	 * from the given HttpInputMessage.
	 *
	 * @param <T> the expected type of the argument value to be created
	 * @param inputMessage the HTTP input message representing the current request
	 * @param methodParam the method argument
	 * @param targetType the type of object to create, not necessarily the same as
	 * the method parameter type (e.g. for {@code HttpEntity<String>} method
	 * parameter the target type is String)
	 * @return the created method argument value
	 * @throws IOException if the reading from the request fails
	 * @throws HttpMediaTypeNotSupportedException if no suitable message converter is found
	 */
	@SuppressWarnings("unchecked")
	protected <T> Object readWithMessageConverters(HttpInputMessage inputMessage,
			MethodParameter methodParam, Type targetType) throws IOException, HttpMediaTypeNotSupportedException {

				MediaType contentType = inputMessage.getHeaders().getContentType();
				if (contentType == null) {
					contentType = MediaType.APPLICATION_OCTET_STREAM;
				}

				Class<?> contextClass = methodParam.getDeclaringClass();
				Map<TypeVariable, Type> map = GenericTypeResolver.getTypeVariableMap(contextClass);
				Class<T> targetClass = (Class<T>) GenericTypeResolver.resolveType(targetType, map);

				for (HttpMessageConverter<?> converter : this.messageConverters) {
					if (converter instanceof GenericHttpMessageConverter) {
						GenericHttpMessageConverter genericConverter = (GenericHttpMessageConverter) converter;
						if (genericConverter.canRead(targetType, contextClass, contentType)) {
							if (logger.isDebugEnabled()) {
								logger.debug("Reading [" + targetType + "] as \"" +
										contentType + "\" using [" + converter + "]");
							}
							return (T) genericConverter.read(targetType, contextClass, inputMessage);
						}
					}
					if (targetClass != null) {
						if (converter.canRead(targetClass, contentType)) {
							if (logger.isDebugEnabled()) {
								logger.debug("Reading [" + targetClass.getName() + "] as \"" +
										contentType + "\" using [" + converter + "]");
							}
							return ((HttpMessageConverter<T>) converter).read(targetClass, inputMessage);
						}
					}
				}

				throw new HttpMediaTypeNotSupportedException(contentType, allSupportedMediaTypes);
			}

	/**
	 * Creates a new {@link HttpInputMessage} from the given {@link NativeWebRequest}.
	 *
	 * @param webRequest the web request to create an input message from
	 * @return the input message
	 */
	protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		return new ServletServerHttpRequest(servletRequest);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7993.java