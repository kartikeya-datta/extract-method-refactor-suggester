error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13629.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13629.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13629.java
text:
```scala
final L@@ist<Boolean> success = new ArrayList<Boolean>(3);

/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springframework.scheduling.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import junit.framework.TestCase;

import org.springframework.scheduling.TestMethodInvokingTask;

/**
 * @author Juergen Hoeller
 * @since 20.02.2004
 */
public class TimerSupportTests extends TestCase {

	public void testTimerFactoryBean() throws Exception {
		final TestTimerTask timerTask0 = new TestTimerTask();

		TestMethodInvokingTask task1 = new TestMethodInvokingTask();
		MethodInvokingTimerTaskFactoryBean mittfb = new MethodInvokingTimerTaskFactoryBean();
		mittfb.setTargetObject(task1);
		mittfb.setTargetMethod("doSomething");
		mittfb.afterPropertiesSet();
		final TimerTask timerTask1 = mittfb.getObject();

		final TestRunnable timerTask2 = new TestRunnable();

		ScheduledTimerTask[] tasks = new ScheduledTimerTask[3];
		tasks[0] = new ScheduledTimerTask(timerTask0, 0, 10, false);
		tasks[1] = new ScheduledTimerTask(timerTask1, 10, 20, true);
		tasks[2] = new ScheduledTimerTask(timerTask2, 20);

		final List success = new ArrayList(3);
		final Timer timer = new Timer(true) {
			public void schedule(TimerTask task, long delay, long period) {
				if (task == timerTask0 && delay == 0 && period == 10) {
					success.add(Boolean.TRUE);
				}
			}
			public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
				if (task == timerTask1 && delay == 10 && period == 20) {
					success.add(Boolean.TRUE);
				}
			}
			public void schedule(TimerTask task, long delay) {
				if (task instanceof DelegatingTimerTask && delay == 20) {
					success.add(Boolean.TRUE);
				}
			}
			public void cancel() {
				success.add(Boolean.TRUE);
			}
		};

		TimerFactoryBean timerFactoryBean = new TimerFactoryBean() {
			protected Timer createTimer(String name, boolean daemon) {
				return timer;
			}
		};
		try {
			timerFactoryBean.setScheduledTimerTasks(tasks);
			timerFactoryBean.afterPropertiesSet();
			assertTrue(timerFactoryBean.getObject() instanceof Timer);
			timerTask0.run();
			timerTask1.run();
			timerTask2.run();
		}
		finally {
			timerFactoryBean.destroy();
		}

		assertTrue("Correct Timer invocations", success.size() == 4);
		assertTrue("TimerTask0 works", timerTask0.counter == 1);
		assertTrue("TimerTask1 works", task1.counter == 1);
		assertTrue("TimerTask2 works", timerTask2.counter == 1);
	}

	public void testPlainTimerFactoryBean() {
		TimerFactoryBean tfb = new TimerFactoryBean();
		tfb.afterPropertiesSet();
		tfb.destroy();
	}


	private static class TestTimerTask extends TimerTask {

		private int counter = 0;

		public void run() {
			counter++;
		}
	}


	private static class TestRunnable implements Runnable {

		private int counter = 0;

		public void run() {
			counter++;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13629.java