error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5055.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5055.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5055.java
text:
```scala
public U@@rl getClientUrl()

package org.apache.wicket.request.mapper;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.WicketTestCase;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.junit.Test;

public class ResourceMapperTest extends WicketTestCase
{
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final String SHARED_NAME = "test-resource";

	private IRequestMapper mapper;
	private TestResource resource;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		resource = new TestResource();
		tester.getApplication().getSharedResources().add(SHARED_NAME, resource);
		ResourceReference resourceReference = new SharedResourceReference(SHARED_NAME);
		mapper = new ResourceMapper("/test/resource", resourceReference);
		tester.getApplication().getRootRequestMapperAsCompound().add(mapper);
	}

	private Request createRequest(final String url)
	{
		return new Request()
		{
			@Override
			public Url getUrl()
			{
				return Url.parse(url, CHARSET);
			}

			@Override
			public Locale getLocale()
			{
				return null;
			}

			@Override
			public Charset getCharset()
			{
				return CHARSET;
			}

			@Override
			public Url getBaseUrl()
			{
				return getUrl();
			}
		};
	}

	@Test
	public void testInvalidPathIsEmpty()
	{
		IRequestHandler requestHandler = mapper.mapRequest(createRequest(""));
		assertNull(requestHandler);
	}

	@Test
	public void testInvalidPathIsMismatch()
	{
		IRequestHandler requestHandler = mapper.mapRequest(createRequest("test/resourcex"));
		assertNull(requestHandler);
	}

	@Test
	public void testInvalidPathIsTooShort()
	{
		IRequestHandler requestHandler = mapper.mapRequest(createRequest("test"));
		assertNull(requestHandler);
	}

	@Test
	public void testValidPathWithParams()
	{
		Request request = createRequest("test/resource/1/fred");
		IRequestHandler requestHandler = mapper.mapRequest(request);
		assertNotNull(requestHandler);
		assertEquals(ResourceReferenceRequestHandler.class, requestHandler.getClass());
		assertEquals(request.getUrl(), mapper.mapHandler(requestHandler));

		tester.processRequest(requestHandler);
		PageParameters params = resource.pageParameters;
		assertNotNull(params);
		assertEquals(0, params.getAllNamed().size());
		assertEquals(2, params.getIndexedCount());

		StringValue paramId = params.get(0);
		assertNotNull(paramId);
		assertEquals(1, paramId.toInt());

		StringValue paramName = params.get(1);
		assertNotNull(paramName);
		assertEquals("fred", paramName.toString());
	}

	@Test
	public void testValidPathWithParamsAndQueryPath()
	{
		Request request = createRequest("test/resource/1/fred?foo=bar&foo=baz&value=12");
		IRequestHandler requestHandler = mapper.mapRequest(request);
		assertNotNull(requestHandler);
		assertEquals(ResourceReferenceRequestHandler.class, requestHandler.getClass());
		assertEquals(request.getUrl(), mapper.mapHandler(requestHandler));

		tester.processRequest(requestHandler);
		PageParameters params = resource.pageParameters;
		assertNotNull(params);
		assertEquals(3, params.getAllNamed().size());
		assertEquals(2, params.getIndexedCount());

		StringValue paramId = params.get(0);
		assertNotNull(paramId);
		assertEquals(1, paramId.toInt());

		StringValue paramName = params.get(1);
		assertNotNull(paramName);
		assertEquals("fred", paramName.toString());

		List<StringValue> foo = params.getValues("foo");
		assertNotNull(foo.size() == 2);
		assertEquals("bar", foo.get(0).toString(""));
		assertEquals("baz", foo.get(1).toString(""));

		StringValue paramValue = params.get("value");
		assertEquals(12, paramValue.toInt());
	}

	private static class TestResource implements IResource
	{
		private static final long serialVersionUID = -3130204487473856574L;

		public PageParameters pageParameters;

		public void respond(Attributes attributes)
		{
			pageParameters = attributes.getParameters();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5055.java