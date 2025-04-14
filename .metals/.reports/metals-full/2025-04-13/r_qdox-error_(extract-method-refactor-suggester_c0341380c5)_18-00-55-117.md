error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10523.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10523.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 660
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10523.java
text:
```scala
public class FlashMapManagerTests {

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

p@@ackage org.springframework.web.servlet.support;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.util.WebUtils;

import static org.junit.Assert.*;

/**
 * Test fixture for testing {@link AbstractFlashMapManager} methods.
 *
 * @author Rossen Stoyanchev
 */
public class AbstractFlashMapManagerTests {

	private TestFlashMapManager flashMapManager;

	private MockHttpServletRequest request;

	private MockHttpServletResponse response;

	@Before
	public void setup() {
		this.flashMapManager = new TestFlashMapManager();
		this.request = new MockHttpServletRequest();
		this.response = new MockHttpServletResponse();
	}

	@Test
	public void retrieveAndUpdateMatchByPath() {
		FlashMap flashMap = new FlashMap();
		flashMap.put("key", "value");
		flashMap.setTargetRequestPath("/path");

		this.flashMapManager.setFlashMaps(flashMap);

		this.request.setRequestURI("/path");
		FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertEquals(flashMap, inputFlashMap);
	}

	// SPR-8779

	@Test
	public void retrieveAndUpdateMatchByOriginatingPath() {
		FlashMap flashMap = new FlashMap();
		flashMap.put("key", "value");
		flashMap.setTargetRequestPath("/accounts");

		this.flashMapManager.setFlashMaps(flashMap);

		this.request.setAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE, "/accounts");
		this.request.setRequestURI("/mvc/accounts");
		FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertEquals(flashMap, inputFlashMap);
		assertEquals("Input FlashMap should have been removed", 0, this.flashMapManager.getFlashMaps().size());
	}

	@Test
	public void retrieveAndUpdateMatchWithTrailingSlash() {
		FlashMap flashMap = new FlashMap();
		flashMap.put("key", "value");
		flashMap.setTargetRequestPath("/path");

		this.flashMapManager.setFlashMaps(flashMap);

		this.request.setRequestURI("/path/");
		FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertEquals(flashMap, inputFlashMap);
		assertEquals("Input FlashMap should have been removed", 0, this.flashMapManager.getFlashMaps().size());
	}

	@Test
	public void retrieveAndUpdateMatchByParams() {
		FlashMap flashMap = new FlashMap();
		flashMap.put("key", "value");
		flashMap.addTargetRequestParam("number", "one");

		this.flashMapManager.setFlashMaps(flashMap);

		this.request.setParameter("number", (String) null);
		FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertNull(inputFlashMap);
		assertEquals("FlashMap should not have been removed", 1, this.flashMapManager.getFlashMaps().size());

		this.request.setParameter("number", "two");
		inputFlashMap = this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertNull(inputFlashMap);
		assertEquals("FlashMap should not have been removed", 1, this.flashMapManager.getFlashMaps().size());

		this.request.setParameter("number", "one");
		inputFlashMap = this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertEquals(flashMap, inputFlashMap);
		assertEquals("Input FlashMap should have been removed", 0, this.flashMapManager.getFlashMaps().size());
	}

	// SPR-8798

	@Test
	public void retrieveAndUpdateMatchWithMultiValueParam() {
		FlashMap flashMap = new FlashMap();
		flashMap.put("name", "value");
		flashMap.addTargetRequestParam("id", "1");
		flashMap.addTargetRequestParam("id", "2");

		this.flashMapManager.setFlashMaps(flashMap);

		this.request.setParameter("id", "1");
		FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertNull(inputFlashMap);
		assertEquals("FlashMap should not have been removed", 1, this.flashMapManager.getFlashMaps().size());

		this.request.addParameter("id", "2");
		inputFlashMap = this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertEquals(flashMap, inputFlashMap);
		assertEquals("Input FlashMap should have been removed", 0, this.flashMapManager.getFlashMaps().size());
	}

	@Test
	public void retrieveAndUpdateSortMultipleMatches() {
		FlashMap emptyFlashMap = new FlashMap();

		FlashMap flashMapOne = new FlashMap();
		flashMapOne.put("key1", "value1");
		flashMapOne.setTargetRequestPath("/one");

		FlashMap flashMapTwo = new FlashMap();
		flashMapTwo.put("key1", "value1");
		flashMapTwo.put("key2", "value2");
		flashMapTwo.setTargetRequestPath("/one/two");

		this.flashMapManager.setFlashMaps(emptyFlashMap, flashMapOne, flashMapTwo);

		this.request.setRequestURI("/one/two");
		FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertEquals(flashMapTwo, inputFlashMap);
		assertEquals("Input FlashMap should have been removed", 2, this.flashMapManager.getFlashMaps().size());
	}

	@Test
	public void retrieveAndUpdateRemoveExpired() throws InterruptedException {
		List<FlashMap> flashMaps = new ArrayList<FlashMap>();
		for (int i=0; i < 5; i++) {
			FlashMap expiredFlashMap = new FlashMap();
			expiredFlashMap.startExpirationPeriod(-1);
			flashMaps.add(expiredFlashMap);
		}
		this.flashMapManager.setFlashMaps(flashMaps);
		this.flashMapManager.retrieveAndUpdate(this.request, this.response);

		assertEquals("Expired instances should be removed even if the saved FlashMap is empty",
				0, this.flashMapManager.getFlashMaps().size());
	}

	@Test
	public void saveOutputFlashMapEmpty() throws InterruptedException {
		FlashMap flashMap = new FlashMap();

		this.flashMapManager.saveOutputFlashMap(flashMap, this.request, this.response);
		List<FlashMap> allMaps = this.flashMapManager.getFlashMaps();

		assertNull(allMaps);
	}

	@Test
	public void saveOutputFlashMap() throws InterruptedException {
		FlashMap flashMap = new FlashMap();
		flashMap.put("name", "value");

		this.flashMapManager.setFlashMapTimeout(-1); // expire immediately so we can check expiration started
		this.flashMapManager.saveOutputFlashMap(flashMap, this.request, this.response);
		List<FlashMap> allMaps = this.flashMapManager.getFlashMaps();

		assertNotNull(allMaps);
		assertSame(flashMap, allMaps.get(0));
		assertTrue(flashMap.isExpired());
	}

	@Test
	public void saveOutputFlashMapDecodeTargetPath() throws InterruptedException {
		FlashMap flashMap = new FlashMap();
		flashMap.put("key", "value");

		flashMap.setTargetRequestPath("/once%20upon%20a%20time");
		this.flashMapManager.saveOutputFlashMap(flashMap, this.request, this.response);

		assertEquals("/once upon a time", flashMap.getTargetRequestPath());
	}

	@Test
	public void saveOutputFlashMapNormalizeTargetPath() throws InterruptedException {
		FlashMap flashMap = new FlashMap();
		flashMap.put("key", "value");

		flashMap.setTargetRequestPath(".");
		this.request.setRequestURI("/once/upon/a/time");
		this.flashMapManager.saveOutputFlashMap(flashMap, this.request, this.response);

		assertEquals("/once/upon/a", flashMap.getTargetRequestPath());

		flashMap.setTargetRequestPath("./");
		this.request.setRequestURI("/once/upon/a/time");
		this.flashMapManager.saveOutputFlashMap(flashMap, this.request, this.response);

		assertEquals("/once/upon/a/", flashMap.getTargetRequestPath());

		flashMap.setTargetRequestPath("..");
		this.request.setRequestURI("/once/upon/a/time");
		this.flashMapManager.saveOutputFlashMap(flashMap, this.request, this.response);

		assertEquals("/once/upon", flashMap.getTargetRequestPath());

		flashMap.setTargetRequestPath("../");
		this.request.setRequestURI("/once/upon/a/time");
		this.flashMapManager.saveOutputFlashMap(flashMap, this.request, this.response);

		assertEquals("/once/upon/", flashMap.getTargetRequestPath());

		flashMap.setTargetRequestPath("../../only");
		this.request.setRequestURI("/once/upon/a/time");
		this.flashMapManager.saveOutputFlashMap(flashMap, this.request, this.response);

		assertEquals("/once/only", flashMap.getTargetRequestPath());
	}

	@Test
	public void saveOutputFlashMapDecodeParameters() throws Exception {
		this.request.setCharacterEncoding("UTF-8");

		FlashMap flashMap = new FlashMap();
		flashMap.put("anyKey", "anyValue");

		flashMap.addTargetRequestParam("key", "%D0%90%D0%90");
		flashMap.addTargetRequestParam("key", "%D0%91%D0%91");
		flashMap.addTargetRequestParam("key", "%D0%92%D0%92");
		this.flashMapManager.saveOutputFlashMap(flashMap, this.request, this.response);

		assertEquals(Arrays.asList("\u0410\u0410", "\u0411\u0411", "\u0412\u0412"),
				flashMap.getTargetRequestParams().get("key"));
	}


	private static class TestFlashMapManager extends AbstractFlashMapManager {

		private List<FlashMap> flashMaps;

		public List<FlashMap> getFlashMaps() {
			return this.flashMaps;
		}

		public void setFlashMaps(FlashMap... flashMaps) {
			setFlashMaps(Arrays.asList(flashMaps));
		}

		public void setFlashMaps(List<FlashMap> flashMaps) {
			this.flashMaps = new CopyOnWriteArrayList<FlashMap>(flashMaps);
		}

		@Override
		protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request) {
			return this.flashMaps;
		}

		@Override
		protected void updateFlashMaps(List<FlashMap> flashMaps, HttpServletRequest request,
				HttpServletResponse response) {
			this.flashMaps = flashMaps;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10523.java