error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9323.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9323.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 728
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9323.java
text:
```scala
protected interface PortletRequestMappingPredicate extends Comparable<PortletRequestMappingPredicate> {

/*
 * Copyright 2002-2012 the original author or authors.
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

p@@ackage org.springframework.web.portlet.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.springframework.beans.BeansException;
import org.springframework.util.Assert;

/**
 * Abstract base class for {@link org.springframework.web.portlet.HandlerMapping}
 * implementations that rely on a map which caches handler objects per lookup key.
 * Supports arbitrary lookup keys, and automatically resolves handler bean names
 * into handler bean instances.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see #getLookupKey(javax.portlet.PortletRequest)
 * @see #registerHandler(Object, Object)
 */
public abstract class AbstractMapBasedHandlerMapping<K> extends AbstractHandlerMapping {

	private boolean lazyInitHandlers = false;

	private final Map<K, Object> handlerMap = new HashMap<K, Object>();


	/**
	 * Set whether to lazily initialize handlers. Only applicable to
	 * singleton handlers, as prototypes are always lazily initialized.
	 * Default is false, as eager initialization allows for more efficiency
	 * through referencing the handler objects directly.
	 * <p>If you want to allow your handlers to be lazily initialized,
	 * make them "lazy-init" and set this flag to true. Just making them
	 * "lazy-init" will not work, as they are initialized through the
	 * references from the handler mapping in this case.
	 */
	public void setLazyInitHandlers(boolean lazyInitHandlers) {
		this.lazyInitHandlers = lazyInitHandlers;
	}


	/**
	 * Determines a handler for the computed lookup key for the given request.
	 * @see #getLookupKey
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected Object getHandlerInternal(PortletRequest request) throws Exception {
		K lookupKey = getLookupKey(request);
		Object handler = this.handlerMap.get(lookupKey);
		if (handler != null && logger.isDebugEnabled()) {
			logger.debug("Key [" + lookupKey + "] -> handler [" + handler + "]");
		}
		if (handler instanceof Map) {
			Map<PortletRequestMappingPredicate, Object> predicateMap =
					(Map<PortletRequestMappingPredicate, Object>) handler;
			List<PortletRequestMappingPredicate> filtered = new LinkedList<PortletRequestMappingPredicate>();
			for (PortletRequestMappingPredicate predicate : predicateMap.keySet()) {
				if (predicate.match(request)) {
					filtered.add(predicate);
				}
			}
			if (filtered.isEmpty()) {
				return null;
			}
			Collections.sort(filtered);
			PortletRequestMappingPredicate predicate = filtered.get(0);
			predicate.validate(request);
			return predicateMap.get(predicate);
		}
		return handler;
	}

	/**
	 * Build a lookup key for the given request.
	 * @param request current portlet request
	 * @return the lookup key (never {@code null})
	 * @throws Exception if key computation failed
	 */
	protected abstract K getLookupKey(PortletRequest request) throws Exception;


	/**
	 * Register all handlers specified in the Portlet mode map for the corresponding modes.
	 * @param handlerMap Map with lookup keys as keys and handler beans or bean names as values
	 * @throws BeansException if the handler couldn't be registered
	 */
	protected void registerHandlers(Map<K, ?> handlerMap) throws BeansException {
		Assert.notNull(handlerMap, "Handler Map must not be null");
		for (Map.Entry<K, ?> entry : handlerMap.entrySet()) {
			registerHandler(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Register the given handler instance for the given parameter value.
	 * @param lookupKey the key to map the handler onto
	 * @param handler the handler instance or handler bean name String
	 * (a bean name will automatically be resolved into the corresponding handler bean)
	 * @throws BeansException if the handler couldn't be registered
	 * @throws IllegalStateException if there is a conflicting handler registered
	 */
	protected void registerHandler(K lookupKey, Object handler) throws BeansException, IllegalStateException {
		registerHandler(lookupKey, handler, null);
	}

	/**
	 * Register the given handler instance for the given parameter value.
	 * @param lookupKey the key to map the handler onto
	 * @param handler the handler instance or handler bean name String
	 * (a bean name will automatically be resolved into the corresponding handler bean)
	 * @param predicate a predicate object for this handler (may be {@code null}),
	 * determining a match with the primary lookup key
	 * @throws BeansException if the handler couldn't be registered
	 * @throws IllegalStateException if there is a conflicting handler registered
	 */
	@SuppressWarnings("unchecked")
	protected void registerHandler(K lookupKey, Object handler, PortletRequestMappingPredicate predicate)
			throws BeansException, IllegalStateException {

		Assert.notNull(lookupKey, "Lookup key must not be null");
		Assert.notNull(handler, "Handler object must not be null");
		Object resolvedHandler = handler;

		// Eagerly resolve handler if referencing singleton via name.
		if (!this.lazyInitHandlers && handler instanceof String) {
			String handlerName = (String) handler;
			if (getApplicationContext().isSingleton(handlerName)) {
				resolvedHandler = getApplicationContext().getBean(handlerName);
			}
		}

		// Check for duplicate mapping.
		Object mappedHandler = this.handlerMap.get(lookupKey);
		if (mappedHandler != null && !(mappedHandler instanceof Map)) {
			if (mappedHandler != resolvedHandler) {
				throw new IllegalStateException("Cannot map handler [" + handler + "] to key [" + lookupKey +
						"]: There's already handler [" + mappedHandler + "] mapped.");
			}
		}
		else {
			if (predicate != null) {
				// Add the handler to the predicate map.
				Map<PortletRequestMappingPredicate, Object> predicateMap =
						(Map<PortletRequestMappingPredicate, Object>) mappedHandler;
				if (predicateMap == null) {
					predicateMap = new LinkedHashMap<PortletRequestMappingPredicate, Object>();
					this.handlerMap.put(lookupKey, predicateMap);
				}
				predicateMap.put(predicate, resolvedHandler);
			}
			else {
				// Add the single handler to the map.
				this.handlerMap.put(lookupKey, resolvedHandler);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Mapped key [" + lookupKey + "] onto handler [" + resolvedHandler + "]");
			}
		}
	}


	/**
	 * Predicate interface for determining a match with a given request.
	 */
	protected interface PortletRequestMappingPredicate extends Comparable {

		/**
		 * Determine whether the given request matches this predicate.
		 * @param request current portlet request
		 */
		boolean match(PortletRequest request);

		/**
		 * Validate this predicate's mapping against the current request.
		 * @param request current portlet request
		 * @throws PortletException if validation failed
		 */
		void validate(PortletRequest request) throws PortletException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9323.java