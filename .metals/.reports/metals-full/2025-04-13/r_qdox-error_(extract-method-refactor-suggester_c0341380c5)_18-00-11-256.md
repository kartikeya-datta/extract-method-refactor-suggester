error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8686.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8686.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8686.java
text:
```scala
t@@his.factoryBean.setDefaultContentTypeStrategy(new FixedContentNegotiationStrategy(MediaType.APPLICATION_JSON));

/*
 * Copyright 2002-2014 the original author or authors.
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
package org.springframework.web.accept;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.MediaType;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.Assert.*;

/**
 * Test fixture for {@link ContentNegotiationManagerFactoryBean} tests.
 * @author Rossen Stoyanchev
 */
public class ContentNegotiationManagerFactoryBeanTests {

	private ContentNegotiationManagerFactoryBean factoryBean;

	private NativeWebRequest webRequest;

	private MockHttpServletRequest servletRequest;


	@Before
	public void setup() {
		this.servletRequest = new MockHttpServletRequest();
		this.webRequest = new ServletWebRequest(this.servletRequest);

		this.factoryBean = new ContentNegotiationManagerFactoryBean();
		this.factoryBean.setServletContext(this.servletRequest.getServletContext());
	}


	@Test
	public void defaultSettings() throws Exception {
		this.factoryBean.afterPropertiesSet();
		ContentNegotiationManager manager = this.factoryBean.getObject();

		this.servletRequest.setRequestURI("/flower.gif");

		assertEquals("Should be able to resolve file extensions by default",
				Arrays.asList(MediaType.IMAGE_GIF), manager.resolveMediaTypes(this.webRequest));

		this.servletRequest.setRequestURI("/flower.xyz");

		assertEquals("Should ignore unknown extensions by default",
				Collections.<MediaType>emptyList(), manager.resolveMediaTypes(this.webRequest));

		this.servletRequest.setRequestURI("/flower");
		this.servletRequest.setParameter("format", "gif");

		assertEquals("Should not resolve request parameters by default",
				Collections.<MediaType>emptyList(), manager.resolveMediaTypes(this.webRequest));

		this.servletRequest.setRequestURI("/flower");
		this.servletRequest.addHeader("Accept", MediaType.IMAGE_GIF_VALUE);

		assertEquals("Should resolve Accept header by default",
				Arrays.asList(MediaType.IMAGE_GIF), manager.resolveMediaTypes(this.webRequest));
	}

	@Test
	public void addMediaTypes() throws Exception {
		Map<String, MediaType> mediaTypes = new HashMap<>();
		mediaTypes.put("json", MediaType.APPLICATION_JSON);
		this.factoryBean.addMediaTypes(mediaTypes);

		this.factoryBean.afterPropertiesSet();
		ContentNegotiationManager manager = this.factoryBean.getObject();

		this.servletRequest.setRequestURI("/flower.json");
		assertEquals(Arrays.asList(MediaType.APPLICATION_JSON), manager.resolveMediaTypes(this.webRequest));
	}

	// SPR-10170

	@Test(expected = HttpMediaTypeNotAcceptableException.class)
	public void favorPathExtensionWithUnknownMediaType() throws Exception {
		this.factoryBean.setFavorPathExtension(true);
		this.factoryBean.setIgnoreUnknownPathExtensions(false);
		this.factoryBean.afterPropertiesSet();
		ContentNegotiationManager manager = this.factoryBean.getObject();

		this.servletRequest.setRequestURI("/flower.xyz");
		this.servletRequest.addParameter("format", "json");

		manager.resolveMediaTypes(this.webRequest);
	}

	@Test
	public void favorParameter() throws Exception {
		this.factoryBean.setFavorParameter(true);

		Map<String, MediaType> mediaTypes = new HashMap<>();
		mediaTypes.put("json", MediaType.APPLICATION_JSON);
		this.factoryBean.addMediaTypes(mediaTypes);

		this.factoryBean.afterPropertiesSet();
		ContentNegotiationManager manager = this.factoryBean.getObject();

		this.servletRequest.setRequestURI("/flower");
		this.servletRequest.addParameter("format", "json");

		assertEquals(Arrays.asList(MediaType.APPLICATION_JSON), manager.resolveMediaTypes(this.webRequest));
	}

	// SPR-10170

	@Test(expected = HttpMediaTypeNotAcceptableException.class)
	public void favorParameterWithUnknownMediaType() throws HttpMediaTypeNotAcceptableException {
		this.factoryBean.setFavorParameter(true);
		this.factoryBean.afterPropertiesSet();
		ContentNegotiationManager manager = this.factoryBean.getObject();

		this.servletRequest.setRequestURI("/flower");
		this.servletRequest.setParameter("format", "xyz");

		manager.resolveMediaTypes(this.webRequest);
	}

	@Test
	public void ignoreAcceptHeader() throws Exception {
		this.factoryBean.setIgnoreAcceptHeader(true);
		this.factoryBean.afterPropertiesSet();
		ContentNegotiationManager manager = this.factoryBean.getObject();

		this.servletRequest.setRequestURI("/flower");
		this.servletRequest.addHeader("Accept", MediaType.IMAGE_GIF_VALUE);

		assertEquals(Collections.<MediaType>emptyList(), manager.resolveMediaTypes(this.webRequest));
	}

	@Test
	public void setDefaultContentType() throws Exception {
		this.factoryBean.setDefaultContentType(MediaType.APPLICATION_JSON);
		this.factoryBean.afterPropertiesSet();
		ContentNegotiationManager manager = this.factoryBean.getObject();

		assertEquals(Arrays.asList(MediaType.APPLICATION_JSON), manager.resolveMediaTypes(this.webRequest));

		// SPR-10513

		this.servletRequest.addHeader("Accept", MediaType.ALL_VALUE);

		assertEquals(Arrays.asList(MediaType.APPLICATION_JSON), manager.resolveMediaTypes(this.webRequest));
	}

	// SPR-12286
	@Test
	public void setDefaultContentTypeWithStrategy() throws Exception {
		this.factoryBean.setDefaultContentType(new FixedContentNegotiationStrategy(MediaType.APPLICATION_JSON));
		this.factoryBean.afterPropertiesSet();
		ContentNegotiationManager manager = this.factoryBean.getObject();

		assertEquals(Arrays.asList(MediaType.APPLICATION_JSON), manager.resolveMediaTypes(this.webRequest));

		this.servletRequest.addHeader("Accept", MediaType.ALL_VALUE);
		assertEquals(Arrays.asList(MediaType.APPLICATION_JSON), manager.resolveMediaTypes(this.webRequest));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8686.java