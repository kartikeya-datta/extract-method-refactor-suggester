error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3627.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3627.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3627.java
text:
```scala
private static final S@@tring FLASH_MAPS_SESSION_ATTRIBUTE = DefaultFlashMapManager.class.getName() + ".FLASH_MAPS";

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

package org.springframework.web.servlet.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.util.UrlPathHelper;

/**
 * A default {@link FlashMapManager} implementation that stores {@link FlashMap}
 * instances in the HTTP session.
 * 
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class DefaultFlashMapManager implements FlashMapManager {

	private static final String FLASH_MAPS_SESSION_ATTRIBUTE = DefaultFlashMapManager.class + ".FLASH_MAPS";

	private static final Log logger = LogFactory.getLog(DefaultFlashMapManager.class);
	
	private int flashTimeout = 180;

	private final UrlPathHelper urlPathHelper = new UrlPathHelper();

	/**
	 * Set the amount of time in seconds after a {@link FlashMap} is saved 
	 * (at request completion) and before it expires. 
	 * <p>The default value is 180 seconds.
	 */
	public void setFlashMapTimeout(int flashTimeout) {
		this.flashTimeout = flashTimeout;
	}

	/**
	 * {@inheritDoc}
	 * <p>An HTTP session is never created by this method.
	 */
	public void requestStarted(HttpServletRequest request) {
		if (request.getAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE) != null) {
			return;
		}
		
		FlashMap inputFlashMap = lookupFlashMap(request);
		if (inputFlashMap != null) {
			request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
		}

		FlashMap outputFlashMap = new FlashMap(this.hashCode());
		request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, outputFlashMap);

		removeExpiredFlashMaps(request);
	}

	/**
	 * Find the "input" FlashMap for the current request target by matching it
	 * to the target request information of all stored FlashMap instances.
	 * @return a FlashMap instance or {@code null}
  	 */
	private FlashMap lookupFlashMap(HttpServletRequest request) {
		List<FlashMap> allFlashMaps = retrieveFlashMaps(request, false);
		if (CollectionUtils.isEmpty(allFlashMaps)) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieved FlashMap(s): " + allFlashMaps);
		}
		List<FlashMap> result = new ArrayList<FlashMap>();
		for (FlashMap flashMap : allFlashMaps) {
			if (isFlashMapForRequest(flashMap, request)) {
				result.add(flashMap);
			}
		}
		if (!result.isEmpty()) {
			Collections.sort(result);
			if (logger.isDebugEnabled()) {
				logger.debug("Found matching FlashMap(s): " + result);
			}
			FlashMap match = result.remove(0);
			allFlashMaps.remove(match);
			return match;
		}
		return null;
	}

	/**
	 * Whether the given FlashMap matches the current request.
	 * The default implementation uses the target request path and query params 
	 * saved in the FlashMap.
	 */
	protected boolean isFlashMapForRequest(FlashMap flashMap, HttpServletRequest request) {
		if (flashMap.getTargetRequestPath() != null) {
			String requestUri = this.urlPathHelper.getRequestUri(request);
			if (!requestUri.equals(flashMap.getTargetRequestPath())
					&& !requestUri.equals(flashMap.getTargetRequestPath() + "/")) {
				return false;
			}
		}
		MultiValueMap<String, String> params = flashMap.getTargetRequestParams();
		for (String key : params.keySet()) {
			for (String value : params.get(key)) {
				if (!value.equals(request.getParameter(key))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Retrieve all FlashMap instances from the current HTTP session.
	 * If {@code allowCreate} is "true" and no flash maps exist yet, a new list
	 * is created and stored as a session attribute.
	 * @param request the current request
	 * @param allowCreate whether to create the session if necessary
	 * @return a List to add FlashMap instances to or {@code null} 
	 * 	assuming {@code allowCreate} is "false".
	 */
	@SuppressWarnings("unchecked")
	protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request, boolean allowCreate) {
		HttpSession session = request.getSession(allowCreate);
		if (session == null) {
			return null;
		}
		List<FlashMap> allFlashMaps = (List<FlashMap>) session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE);
		if (allFlashMaps == null && allowCreate) {
			synchronized (this) {
				allFlashMaps = (List<FlashMap>) session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE);
				if (allFlashMaps == null) {
					allFlashMaps = new CopyOnWriteArrayList<FlashMap>();
					session.setAttribute(FLASH_MAPS_SESSION_ATTRIBUTE, allFlashMaps);
				}
			}
		}
		return allFlashMaps;
	}
	
	/**
	 * Iterate all flash maps and remove expired ones.
	 */
	private void removeExpiredFlashMaps(HttpServletRequest request) {
		List<FlashMap> allMaps = retrieveFlashMaps(request, false);
		if (CollectionUtils.isEmpty(allMaps)) {
			return;
		}
		List<FlashMap> expiredMaps = new ArrayList<FlashMap>();
		for (FlashMap flashMap : allMaps) {
			if (flashMap.isExpired()) {
				if (logger.isDebugEnabled()) {
					logger.debug("Removing expired FlashMap: " + flashMap);
				}
				expiredMaps.add(flashMap);
			}
		}
		if (!expiredMaps.isEmpty()) {
			allMaps.removeAll(expiredMaps);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>An HTTP session is never created if the "output" FlashMap is empty.
	 */
	public void requestCompleted(HttpServletRequest request) {
		FlashMap flashMap = (FlashMap) request.getAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE);
		if (flashMap == null) {
			throw new IllegalStateException("requestCompleted called but \"output\" FlashMap was never created");
		}
		if (!flashMap.isEmpty() && flashMap.isCreatedBy(this.hashCode())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Saving FlashMap=" + flashMap);
			}
			onSaveFlashMap(flashMap, request);
			retrieveFlashMaps(request, true).add(flashMap);
		}
	}
	
	/**
	 * Update a FlashMap before it is stored in the HTTP Session.
	 * <p>The default implementation starts the expiration period and ensures the
	 * target request path is decoded and normalized if it is relative. 
	 * @param flashMap the flash map to be saved
	 * @param request the current request
	 */
	protected void onSaveFlashMap(FlashMap flashMap, HttpServletRequest request) {
		String targetPath = flashMap.getTargetRequestPath();
		flashMap.setTargetRequestPath(decodeAndNormalizePath(targetPath, request));
		flashMap.startExpirationPeriod(this.flashTimeout);
	}

	private String decodeAndNormalizePath(String path, HttpServletRequest request) {
		if (path != null) {
			path = this.urlPathHelper.decodeRequestString(request, path);
			if (path.charAt(0) != '/') {
				String requestUri = this.urlPathHelper.getRequestUri(request);
				path = requestUri.substring(0, requestUri.lastIndexOf('/') + 1) + path;
				path = StringUtils.cleanPath(path);
			}
		}
		return path;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3627.java