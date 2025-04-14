error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3606.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3606.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3606.java
text:
```scala
.@@setStrategyMap(Collections.singletonMap("/**", new ContentVersionStrategy()));

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.servlet.resource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

/**
 * Unit tests for
 * {@link AppCacheManifestTransfomer}.
 *
 * @author Brian Clozel
 */
public class AppCacheManifestTransformerTests {

	private AppCacheManifestTransfomer transformer;

	private ResourceTransformerChain chain;

	private HttpServletRequest request;

	@Before
	public void setup() {
		this.transformer = new AppCacheManifestTransfomer();
		this.chain = mock(ResourceTransformerChain.class);
		this.request = mock(HttpServletRequest.class);
	}

	@Test
	public void noTransformIfExtensionNoMatch() throws Exception {
		Resource resource = mock(Resource.class);
		when(resource.getFilename()).thenReturn("foobar.file");
		when(this.chain.transform(this.request, resource)).thenReturn(resource);

		Resource result = this.transformer.transform(this.request, resource, this.chain);
		assertEquals(resource, result);
	}

	@Test
	public void syntaxErrorInManifest() throws Exception {
		Resource resource = new ClassPathResource("test/error.manifest", getClass());
		when(this.chain.transform(this.request, resource)).thenReturn(resource);

		Resource result = this.transformer.transform(this.request, resource, this.chain);
		assertEquals(resource, result);
	}

	@Test
	public void transformManifest() throws Exception {

		VersionResourceResolver versionResourceResolver = new VersionResourceResolver();
		versionResourceResolver
				.setVersionStrategyMap(Collections.singletonMap("/**", new ContentBasedVersionStrategy()));

		List<ResourceResolver> resolvers = new ArrayList<ResourceResolver>();
		resolvers.add(versionResourceResolver);
		resolvers.add(new PathResourceResolver());
		ResourceResolverChain resolverChain = new DefaultResourceResolverChain(resolvers);

		List<ResourceTransformer> transformers = new ArrayList<>();
		transformers.add(new CssLinkResourceTransformer());
		this.chain = new DefaultResourceTransformerChain(resolverChain, transformers);

		Resource resource = new ClassPathResource("test/appcache.manifest", getClass());
		Resource result = this.transformer.transform(this.request, resource, this.chain);
		byte[] bytes = FileCopyUtils.copyToByteArray(result.getInputStream());
		String content = new String(bytes, "UTF-8");

		assertThat("should rewrite resource links", content,
				Matchers.containsString("foo-e36d2e05253c6c7085a91522ce43a0b4.css"));
		assertThat("should rewrite resource links", content,
				Matchers.containsString("bar-11e16cf79faee7ac698c805cf28248d2.css"));
		assertThat("should rewrite resource links", content,
				Matchers.containsString("js/bar-bd508c62235b832d960298ca6c0b7645.js"));

		assertThat("should not rewrite external resources", content,
				Matchers.containsString("//example.org/style.css"));
		assertThat("should not rewrite external resources", content,
				Matchers.containsString("http://example.org/image.png"));

		assertThat("should generate fingerprint", content,
				Matchers.containsString("# Hash: 4bf0338bcbeb0a5b3a4ec9ed8864107d"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3606.java