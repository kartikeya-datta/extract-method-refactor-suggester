error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/674.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/674.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/674.java
text:
```scala
r@@eturn doNext();

// $Header$
/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.control;

import java.io.Serializable;

import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.junit.stubs.TestSampler;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;

// NOTUSED import org.apache.jorphan.logging.LoggingManager;
// NOTUSED import org.apache.log.Logger;

/**
 * @author Michael Stover
 * @author Thad Smith
 * @version $Revision$
 */
public class LoopController extends GenericController implements Serializable {
	// NOTUSED private static Logger log = LoggingManager.getLoggerForClass();

	private final static String LOOPS = "LoopController.loops";

	private final static String CONTINUE_FOREVER = "LoopController.continue_forever";

	private transient int loopCount = 0;

	public LoopController() {
		setContinueForever(true);
	}

	public void setLoops(int loops) {
		setProperty(new IntegerProperty(LOOPS, loops));
	}

	public void setLoops(String loopValue) {
		setProperty(new StringProperty(LOOPS, loopValue));
	}

	public int getLoops() {
		try {
			JMeterProperty prop = getProperty(LOOPS);
			return Integer.parseInt(prop.getStringValue());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public String getLoopString() {
		return getPropertyAsString(LOOPS);
	}

	/**
	 * Determines whether the loop will return any samples if it is rerun.
	 * 
	 * @param forever
	 *            true if the loop must be reset after ending a run
	 */
	public void setContinueForever(boolean forever) {
		setProperty(new BooleanProperty(CONTINUE_FOREVER, forever));
	}

	public boolean getContinueForever() {
		return getPropertyAsBoolean(CONTINUE_FOREVER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.control.Controller#isDone()
	 */
	public boolean isDone() {
		if (getLoops() != 0) {
			return super.isDone();
		} else {
			return true;
		}
	}

	private boolean endOfLoop() {
		return (getLoops() > -1) && loopCount >= getLoops();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.control.GenericController#nextIsNull()
	 */
	protected Sampler nextIsNull() throws NextIsNullException {
		reInitialize();
		if (endOfLoop()) {
			if (!getContinueForever()) {
				setDone(true);
			} else {
				resetLoopCount();
			}
			return null;
		} else {
			return next();
		}
	}

	protected void incrementLoopCount() {
		loopCount++;
	}

	protected void resetLoopCount() {
		loopCount = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.control.GenericController#getIterCount()
	 */
	protected int getIterCount() {
		return loopCount + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.control.GenericController#reInitialize()
	 */
	protected void reInitialize() {
		setFirst(true);
		resetCurrent();
		incrementLoopCount();
		recoverRunningVersion();
	}

	// /////////////////////// Start of Test code
	// ///////////////////////////////

	public static class Test extends JMeterTestCase {
		public Test(String name) {
			super(name);
		}

		public void testProcessing() throws Exception {
			GenericController controller = new GenericController();
			GenericController sub_1 = new GenericController();
			sub_1.addTestElement(new TestSampler("one"));
			sub_1.addTestElement(new TestSampler("two"));
			controller.addTestElement(sub_1);
			controller.addTestElement(new TestSampler("three"));
			LoopController sub_2 = new LoopController();
			sub_2.setLoops(3);
			GenericController sub_3 = new GenericController();
			sub_2.addTestElement(new TestSampler("four"));
			sub_3.addTestElement(new TestSampler("five"));
			sub_3.addTestElement(new TestSampler("six"));
			sub_2.addTestElement(sub_3);
			sub_2.addTestElement(new TestSampler("seven"));
			controller.addTestElement(sub_2);
			String[] order = new String[] { "one", "two", "three", "four", "five", "six", "seven", "four", "five",
					"six", "seven", "four", "five", "six", "seven" };
			int counter = 15;
			controller.setRunningVersion(true);
			sub_1.setRunningVersion(true);
			sub_2.setRunningVersion(true);
			sub_3.setRunningVersion(true);
			controller.initialize();
			for (int i = 0; i < 2; i++) {
				assertEquals(15, counter);
				counter = 0;
				TestElement sampler = null;
				while ((sampler = controller.next()) != null) {
					assertEquals(order[counter++], sampler.getPropertyAsString(TestElement.NAME));
				}
			}
		}

		public void testLoopZeroTimes() throws Exception {
			LoopController loop = new LoopController();
			loop.setLoops(0);
			loop.addTestElement(new TestSampler("never run"));
			loop.initialize();
			assertNull(loop.next());
		}

		public void testInfiniteLoop() throws Exception {
			LoopController loop = new LoopController();
			loop.setLoops(-1);
			loop.addTestElement(new TestSampler("never run"));
			loop.setRunningVersion(true);
			loop.initialize();
			for (int i = 0; i < 42; i++) {
				assertNotNull(loop.next());
			}
		}

	}
}
 No newline at end of file
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/674.java