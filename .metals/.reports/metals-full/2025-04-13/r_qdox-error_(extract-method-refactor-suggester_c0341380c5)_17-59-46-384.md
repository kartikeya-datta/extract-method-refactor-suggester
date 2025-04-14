error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3936.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3936.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3936.java
text:
```scala
public G@@uestbookTest(final String name)

/*
 * $Id: GuestbookTest.java 5401 2006-04-17 12:38:53 +0000 (Mon, 17 Apr 2006)
 * jdonnerstag $ $Revision$ $Date: 2006-04-17 12:38:53 +0000 (Mon, 17 Apr
 * 2006) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.examples.guestbook;

import junit.framework.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wicket.examples.WicketWebTestCase;

/**
 * jWebUnit test for Hello World.
 */
public class GuestbookTest extends WicketWebTestCase
{
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GuestbookTest.class);

	/**
	 * 
	 * @return Test
	 */
	public static Test suite()
	{
		return suite(GuestbookTest.class);
	}

	/**
	 * Construct.
	 * 
	 * @param name
	 *            name of test
	 */
	public GuestbookTest(String name)
	{
		super(name);
	}

	/**
	 * Sets up the test.
	 * 
	 * @throws Exception
	 */
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Test page.
	 * 
	 * @throws Exception
	 */
	public void test_1() throws Exception
	{
		beginAt("/guestbook");

		this.dumpResponse(System.out);
		assertTitleEquals("Wicket Examples - guestbook");
		// this.assertXpathNodeNotPresent("//*[@wicket:id='comments']");
		this.assertElementNotPresent("comments");

		assertFormPresent("commentForm");
		this.assertFormElementPresent("text");
		this.setFormElement("text", "test-1");
		this.submit();

		this.dumpResponse(System.out);
		assertTitleEquals("Wicket Examples - guestbook");
		assertFormPresent("commentForm");
		this.assertFormElementPresent("text");
		this.assertElementPresent("comments");
		// assertTextInElement() seems to be buggy
		// this.assertTextInElement("text", "test-1");
		this.assertTextPresent("test-1");
		this.setFormElement("text", "test-2");
		this.submit();

		this.dumpResponse(System.out);
		assertTitleEquals("Wicket Examples - guestbook");
		this.assertElementPresent("comments");
		// assertTextInElement() seems to be buggy
		// this.assertTextInElement("text", "test-1");
		this.assertTextPresent("test-1");
		// this.assertTextInElement("text", "test-2");
		this.assertTextPresent("test-2");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3936.java