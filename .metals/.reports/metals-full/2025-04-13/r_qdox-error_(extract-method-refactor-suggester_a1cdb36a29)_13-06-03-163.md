error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12331.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12331.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 666
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12331.java
text:
```scala
public class ConcurrentBeanWrapperTests {

/*
 * Copyright 2002-2013 the original author or authors.
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

p@@ackage org.springframework.beans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Guillaume Poirier
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 08.03.2004
 */
public final class ConcurrentBeanWrapperTests {

	private final Log logger = LogFactory.getLog(getClass());

	private Set<TestRun> set = Collections.synchronizedSet(new HashSet<TestRun>());

	private Throwable ex = null;

	@Test
	public void testSingleThread() {
		for (int i = 0; i < 100; i++) {
			performSet();
		}
	}

	@Test
	public void testConcurrent() {
		for (int i = 0; i < 10; i++) {
			TestRun run = new TestRun(this);
			set.add(run);
			Thread t = new Thread(run);
			t.setDaemon(true);
			t.start();
		}
		logger.info("Thread creation over, " + set.size() + " still active.");
		synchronized (this) {
			while (!set.isEmpty() && ex == null) {
				try {
					wait();
				}
				catch (InterruptedException e) {
					logger.info(e.toString());
				}
				logger.info(set.size() + " threads still active.");
			}
		}
		if (ex != null) {
			fail(ex.getMessage());
		}
	}

	private static void performSet() {
		TestBean bean = new TestBean();

		Properties p = (Properties) System.getProperties().clone();

		assertTrue("The System properties must not be empty", p.size() != 0);

		for (Iterator<?> i = p.entrySet().iterator(); i.hasNext();) {
			i.next();
			if (Math.random() > 0.9) {
				i.remove();
			}
		}

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			p.store(buffer, null);
		}
		catch (IOException e) {
			// ByteArrayOutputStream does not throw
			// any IOException
		}
		String value = new String(buffer.toByteArray());

		BeanWrapperImpl wrapper = new BeanWrapperImpl(bean);
		wrapper.setPropertyValue("properties", value);
		assertEquals(p, bean.getProperties());
	}


	private static class TestRun implements Runnable {

		private ConcurrentBeanWrapperTests test;

		public TestRun(ConcurrentBeanWrapperTests test) {
			this.test = test;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < 100; i++) {
					performSet();
				}
			}
			catch (Throwable e) {
				test.ex = e;
			}
			finally {
				synchronized (test) {
					test.set.remove(this);
					test.notifyAll();
				}
			}
		}
	}


	@SuppressWarnings("unused")
	private static class TestBean {

		private Properties properties;

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12331.java