error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9331.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9331.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9331.java
text:
```scala
protected S@@tring[] buildUrlsForHandler(String beanName, Class<?> beanClass) {

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

package org.springframework.web.servlet.mvc.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * Implementation of {@link org.springframework.web.servlet.HandlerMapping} that
 * follows a simple convention for generating URL path mappings from the <i>bean names</i>
 * of registered {@link org.springframework.web.servlet.mvc.Controller} beans
 * as well as {@code @Controller} annotated beans.
 *
 * <p>This is similar to {@link org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping}
 * but doesn't expect bean names to follow the URL convention: It turns plain bean names
 * into URLs by prepending a slash and optionally applying a specified prefix and/or suffix.
 * However, it only does so for well-known {@link #isControllerType controller types},
 * as listed above (analogous to {@link ControllerClassNameHandlerMapping}).
 *
 * @author Juergen Hoeller
 * @since 2.5.3
 * @see ControllerClassNameHandlerMapping
 * @see org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping
 */
public class ControllerBeanNameHandlerMapping extends AbstractControllerUrlHandlerMapping {

	private String urlPrefix = "";

	private String urlSuffix = "";


	/**
	 * Set an optional prefix to prepend to generated URL mappings.
	 * <p>By default this is an empty String. If you want a prefix like
	 * "/myapp/", you can set it for all beans mapped by this mapping.
	 */
	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = (urlPrefix != null ? urlPrefix : "");
	}

	/**
	 * Set an optional suffix to append to generated URL mappings.
	 * <p>By default this is an empty String. If you want a suffix like
	 * ".do", you can set it for all beans mapped by this mapping.
	 */
	public void setUrlSuffix(String urlSuffix) {
		this.urlSuffix = (urlSuffix != null ? urlSuffix : "");
	}


	@Override
	protected String[] buildUrlsForHandler(String beanName, Class beanClass) {
		List<String> urls = new ArrayList<String>();
		urls.add(generatePathMapping(beanName));
		String[] aliases = getApplicationContext().getAliases(beanName);
		for (String alias : aliases) {
			urls.add(generatePathMapping(alias));
		}
		return StringUtils.toStringArray(urls);
	}

	/**
	 * Prepends a '/' if required and appends the URL suffix to the name.
	 */
	protected String generatePathMapping(String beanName) {
		String name = (beanName.startsWith("/") ? beanName : "/" + beanName);
		StringBuilder path = new StringBuilder();
		if (!name.startsWith(this.urlPrefix)) {
			path.append(this.urlPrefix);
		}
		path.append(name);
		if (!name.endsWith(this.urlSuffix)) {
			path.append(this.urlSuffix);
		}
		return path.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9331.java