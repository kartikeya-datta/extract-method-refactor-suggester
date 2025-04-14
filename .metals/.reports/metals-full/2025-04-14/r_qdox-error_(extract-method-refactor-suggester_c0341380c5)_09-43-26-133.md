error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1702.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1702.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1702.java
text:
```scala
public v@@oid addHandlers(List<? extends HandlerMethodReturnValueHandler> returnValueHandlers) {

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

package org.springframework.web.method.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Handles method return values by delegating to a list of registered {@link HandlerMethodReturnValueHandler}s. 
 * Previously resolved return types are cached for faster lookups. 
 * 
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler {
	
	protected final Log logger = LogFactory.getLog(getClass());

	private final List<HandlerMethodReturnValueHandler> returnValueHandlers = 
		new ArrayList<HandlerMethodReturnValueHandler>();

	private final Map<MethodParameter, HandlerMethodReturnValueHandler> returnValueHandlerCache =
		new ConcurrentHashMap<MethodParameter, HandlerMethodReturnValueHandler>();

	/**
	 * Whether the given {@linkplain MethodParameter method return type} is supported by any registered 
	 * {@link HandlerMethodReturnValueHandler}.
	 */
	public boolean supportsReturnType(MethodParameter returnType) {
		return getReturnValueHandler(returnType) != null;
	}

	/**
	 * Iterate over registered {@link HandlerMethodReturnValueHandler}s and invoke the one that supports it.
	 * @exception IllegalStateException if no suitable {@link HandlerMethodReturnValueHandler} is found.
	 */
	public void handleReturnValue(Object returnValue, 
								  MethodParameter returnType, 
								  ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest) throws Exception {
		HandlerMethodReturnValueHandler handler = getReturnValueHandler(returnType);
		if (handler != null) {
			handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
		}
		else {
			throw new IllegalStateException(
					"No suitable HandlerMethodReturnValueHandler found. " +
					"supportsReturnType(MethodParameter) should have been called previously");
		}
	}

	/**
	 * Find a registered {@link HandlerMethodReturnValueHandler} that supports the given return type.
	 */
	private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnType) {
		HandlerMethodReturnValueHandler result = this.returnValueHandlerCache.get(returnType);
		if (result == null) {
			for (HandlerMethodReturnValueHandler returnValueHandler : returnValueHandlers) {
				if (logger.isTraceEnabled()) {
					logger.trace("Testing if return value handler [" + returnValueHandler + "] supports [" +
							returnType.getGenericParameterType() + "]");
				}
				if (returnValueHandler.supportsReturnType(returnType)) {
					result = returnValueHandler;
					this.returnValueHandlerCache.put(returnType, returnValueHandler);
					break;
				}
			}
		}
		return result;
	}	
	
	/**
	 * Add the given {@link HandlerMethodReturnValueHandler}.
	 */
	public void addHandler(HandlerMethodReturnValueHandler returnValuehandler) {
		returnValueHandlers.add(returnValuehandler);
	}

	/**
	 * Add the given {@link HandlerMethodReturnValueHandler}s.
	 */
	public void addHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		if (returnValueHandlers != null) {
			for (HandlerMethodReturnValueHandler handler : returnValueHandlers) {
				this.returnValueHandlers.add(handler);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1702.java