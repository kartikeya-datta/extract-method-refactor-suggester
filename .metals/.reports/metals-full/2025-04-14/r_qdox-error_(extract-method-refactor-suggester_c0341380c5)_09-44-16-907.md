error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7782.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7782.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7782.java
text:
```scala
public I@@ResourceStream getCacheableResourceStream()

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.request.mapper;

import java.io.Serializable;
import java.util.Locale;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.mapper.parameter.INamedParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;
import org.apache.wicket.request.resource.caching.IStaticCacheableResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.IResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.ResourceUrl;
import org.apache.wicket.request.resource.caching.version.StaticResourceVersion;
import org.apache.wicket.util.IProvider;
import org.apache.wicket.util.ValueProvider;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

/**
 * @author Matej Knopp
 */
public class BasicResourceReferenceMapperTest extends AbstractResourceReferenceMapperTest
{
	private static final IProvider<IResourceCachingStrategy> NO_CACHING = new ValueProvider<IResourceCachingStrategy>(
		NoOpResourceCachingStrategy.INSTANCE);

	/**
	 * Construct.
	 */
	public BasicResourceReferenceMapperTest()
	{
	}

	private final BasicResourceReferenceMapper encoder = new BasicResourceReferenceMapper(
		new PageParametersEncoder(), NO_CACHING)
	{
		@Override
		protected IMapperContext getContext()
		{
			return context;
		}
	};

	/**
	 * 
	 */
	public void testDecode1()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference1");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource1, h.getResource());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals(0, h.getPageParameters().getNamedKeys().size());
	}

	/**
	 * 
	 */
	public void testDecode1A()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference1?en");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource1, h.getResource());
		assertEquals(Locale.ENGLISH, h.getLocale());
		assertEquals(null, h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals(0, h.getPageParameters().getNamedKeys().size());
	}

	/**
	 * 
	 */
	public void testDecode2()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference1?p1=v1&p2=v2");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource1, h.getResource());
		assertEquals(null, h.getLocale());
		assertEquals(null, h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals("v1", h.getPageParameters().get("p1").toString());
		assertEquals("v2", h.getPageParameters().get("p2").toString());
	}

	/**
	 * 
	 */
	public void testDecode2A()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference1?-style&p1=v1&p2=v2");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource1, h.getResource());
		assertEquals(null, h.getLocale());
		assertEquals("style", h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals("v1", h.getPageParameters().get("p1").toString());
		assertEquals("v2", h.getPageParameters().get("p2").toString());
	}

	/**
	 * 
	 */
	public void testDecode3()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference2/name2?en_EN");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource2, h.getResource());
		assertEquals(new Locale("en", "en"), h.getLocale());
		assertEquals(null, h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals(0, h.getPageParameters().getNamedKeys().size());
	}

	/**
	 * 
	 */
	public void testDecode3A()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference2/name2?en_EN-style");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource2, h.getResource());
		assertEquals(new Locale("en", "en"), h.getLocale());
		assertEquals("style", h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals(0, h.getPageParameters().getNamedKeys().size());
	}

	/**
	 * 
	 */
	public void testDecode3B()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference2/name2");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertNull(handler);
	}

	/**
	 * 
	 */
	public void testDecode4()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference2/name2?en_EN&p1=v1&p2=v2");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource2, h.getResource());
		assertEquals(new Locale("en", "en"), h.getLocale());
		assertEquals(null, h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals("v1", h.getPageParameters().get("p1").toString());
		assertEquals("v2", h.getPageParameters().get("p2").toString());
	}

	/**
	 * 
	 */
	public void testDecode5()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference3?-style");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource3, h.getResource());
		assertEquals(null, h.getLocale());
		assertEquals("style", h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals(0, h.getPageParameters().getNamedKeys().size());
	}

	/**
	 * 
	 */
	public void testDecode6()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference3?-style&p1=v1&p2=v2");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource3, h.getResource());
		assertEquals(null, h.getLocale());
		assertEquals("style", h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals("v1", h.getPageParameters().get("p1").toString());
		assertEquals("v2", h.getPageParameters().get("p2").toString());
	}


	/**
	 * 
	 */
	public void testDecode7()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference4?en-style");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource4, h.getResource());
		assertEquals(Locale.ENGLISH, h.getLocale());
		assertEquals("style", h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals(0, h.getPageParameters().getNamedKeys().size());
	}

	/**
	 * 
	 */
	public void testDecode7A()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference4?sk");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertNull(handler);
	}

	/**
	 * 
	 */
	public void testDecode8()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME + "/reference4?en-style&p1=v1&p2=v2");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource4, h.getResource());
		assertEquals(Locale.ENGLISH, h.getLocale());
		assertEquals("style", h.getStyle());
		assertEquals(null, h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals("v1", h.getPageParameters().get("p1").toString());
		assertEquals("v2", h.getPageParameters().get("p2").toString());
	}

	/**
	 * 
	 */
	public void testDecode9()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME +
			"/reference5?en--variation&p1=v1&p2=v2");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource5, h.getResource());
		assertEquals(Locale.ENGLISH, h.getLocale());
		assertEquals(null, h.getStyle());
		assertEquals("variation", h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals("v1", h.getPageParameters().get("p1").toString());
		assertEquals("v2", h.getPageParameters().get("p2").toString());
	}

	/**
	 * 
	 */
	public void testDecode10()
	{
		Url url = Url.parse("wicket/resource/" + CLASS_NAME +
			"/reference6?en-style-variation&p1=v1&p2=v2");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertTrue(handler instanceof ResourceReferenceRequestHandler);
		ResourceReferenceRequestHandler h = (ResourceReferenceRequestHandler)handler;
		assertEquals(resource6, h.getResource());
		assertEquals(Locale.ENGLISH, h.getLocale());
		assertEquals("style", h.getStyle());
		assertEquals("variation", h.getVariation());
		assertEquals(0, h.getPageParameters().getIndexedCount());
		assertEquals("v1", h.getPageParameters().get("p1").toString());
		assertEquals("v2", h.getPageParameters().get("p2").toString());
	}

	/**
	 * 
	 */
	public void testEncode1()
	{
		ResourceReferenceRequestHandler handler = new ResourceReferenceRequestHandler(reference1,
			null);
		Url url = encoder.mapHandler(handler);
		assertEquals("wicket/resource/" + CLASS_NAME + "/reference1", url.toString());
	}

	/**
	 * 
	 */
	public void testEncode2()
	{
		PageParameters parameters = new PageParameters();
		parameters.set(0, "X");
		parameters.add("p1", "v1");
		parameters.add("p2", "v2");
		ResourceReferenceRequestHandler handler = new ResourceReferenceRequestHandler(reference1,
			parameters);

		Url url = encoder.mapHandler(handler);
		assertEquals("wicket/resource/" + CLASS_NAME + "/reference1?p1=v1&p2=v2", url.toString());
	}

	/**
	 * 
	 */
	public void testEncode3()
	{
		ResourceReferenceRequestHandler handler = new ResourceReferenceRequestHandler(reference2,
			null);
		Url url = encoder.mapHandler(handler);
		assertEquals("wicket/resource/" + CLASS_NAME + "/reference2/name2?en_EN", url.toString());
	}

	/**
	 * 
	 */
	public void testEncode4()
	{
		PageParameters parameters = new PageParameters();
		parameters.set(0, "X");
		parameters.add("p1", "v1");
		parameters.add("p2", "v2");
		ResourceReferenceRequestHandler handler = new ResourceReferenceRequestHandler(reference2,
			parameters);

		Url url = encoder.mapHandler(handler);
		assertEquals("wicket/resource/" + CLASS_NAME + "/reference2/name2?en_EN&p1=v1&p2=v2",
			url.toString());
	}

	/**
	 * 
	 */
	public void testEncode5()
	{
		ResourceReferenceRequestHandler handler = new ResourceReferenceRequestHandler(reference3,
			null);
		Url url = encoder.mapHandler(handler);
		assertEquals("wicket/resource/" + CLASS_NAME + "/reference3?-style", url.toString());
	}

	/**
	 * 
	 */
	public void testEncode6()
	{
		PageParameters parameters = new PageParameters();
		parameters.set(0, "X");
		parameters.add("p1", "v1");
		parameters.add("p2", "v2");
		ResourceReferenceRequestHandler handler = new ResourceReferenceRequestHandler(reference3,
			parameters);

		Url url = encoder.mapHandler(handler);
		assertEquals("wicket/resource/" + CLASS_NAME + "/reference3?-style&p1=v1&p2=v2",
			url.toString());
	}

	/**
	 * 
	 */
	public void testEncode7()
	{
		ResourceReferenceRequestHandler handler = new ResourceReferenceRequestHandler(reference4,
			null);
		Url url = encoder.mapHandler(handler);
		assertEquals("wicket/resource/" + CLASS_NAME + "/reference4?en-style", url.toString());
	}

	/**
	 * 
	 */
	public void testEncode8()
	{
		PageParameters parameters = new PageParameters();
		parameters.set(0, "X");
		parameters.add("p1", "v1");
		parameters.add("p2", "v2");
		ResourceReferenceRequestHandler handler = new ResourceReferenceRequestHandler(reference4,
			parameters);

		Url url = encoder.mapHandler(handler);
		assertEquals("wicket/resource/" + CLASS_NAME + "/reference4?en-style&p1=v1&p2=v2",
			url.toString());
	}

	/**
	 * Tests request to url encoding when style is null but variation is not
	 */
	public void testEncode9()
	{
		ResourceReferenceRequestHandler handler = new ResourceReferenceRequestHandler(reference5,
			null);

		Url url = encoder.mapHandler(handler);
		assertEquals("wicket/resource/" + CLASS_NAME + "/reference5?en--variation", url.toString());
	}

	/**
	 * 
	 */
	public void testVersionStringInResourceFilename()
	{
		final IStaticCacheableResource resource = new IStaticCacheableResource()
		{
			public Serializable getCacheKey()
			{
				return null;
			}

			public IResourceStream getResourceStream()
			{
				return new StringResourceStream("foo-bar");
			}

			public void respond(Attributes attributes)
			{
			}
		};

		final PackageResourceReference reference = new PackageResourceReference(getClass(),
			"versioned", Locale.ENGLISH, "style", null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public IResource getResource()
			{
				return resource;
			}
		};

		IResourceCachingStrategy strategy = new FilenameWithVersionResourceCachingStrategy(
			"-version-", new StaticResourceVersion("foobar"));

		INamedParameters params = new PageParameters();
		ResourceUrl url = new ResourceUrl("test.js", params);
		strategy.decorateUrl(url, resource);
		assertEquals("test-version-foobar.js", url.getFileName());
		strategy.undecorateUrl(url);
		assertEquals("test.js", url.getFileName());

		url = new ResourceUrl("test", params);
		strategy.decorateUrl(url, resource);
		assertEquals("test-version-foobar", url.getFileName());
		strategy.undecorateUrl(url);
		assertEquals("test", url.getFileName());

		// this behavior is o.k. since a browser could request an
		// previous version of the resource. for example we
		// could first have 'test-alpha.txt' which would be later replaced
		// by 'test-beta.txt' but in any case will point to
		// internal resource 'test.txt'
		url = new ResourceUrl("test-version-older.txt", params);
		strategy.undecorateUrl(url);
		assertEquals("test.txt", url.getFileName());

		// weird but valid
		url = new ResourceUrl("test-version-.txt", params);
		strategy.undecorateUrl(url);
		assertEquals("test.txt", url.getFileName());

		// weird but valid
		url = new ResourceUrl("test-version--------", params);
		strategy.undecorateUrl(url);
		assertEquals("test", url.getFileName());

		// weird but valid
		url = new ResourceUrl("test-version-1.0.3-alpha.txt", params);
		strategy.undecorateUrl(url);
		assertEquals("test.txt", url.getFileName());

		// check a version that contains a dot which also marks the filename extension
		strategy = new FilenameWithVersionResourceCachingStrategy("-version-",
			new StaticResourceVersion("1.0.4-beta"));
		url = new ResourceUrl("test.txt", params);
		strategy.decorateUrl(url, resource);
		assertEquals("test-version-1.0.4-beta.txt", url.getFileName());
	}

	/**
	 * Tests <a href="https://issues.apache.org/jira/browse/WICKET-3918">WICKET-3918</a>.
	 */
	public void testWicket3918()
	{
		Url url = Url.parse("wicket/resource/org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow/res/");
		IRequestHandler handler = encoder.mapRequest(getRequest(url));
		assertNull(handler);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7782.java