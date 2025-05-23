error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10096.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10096.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10096.java
text:
```scala
r@@eturn s.getName();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import org.apache.jmeter.engine.util.ValueReplacer;
import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.junit.stubs.TestSampler;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterVariables;

public class TestWhileController extends JMeterTestCase {
		static {
			// LoggingManager.setPriority("DEBUG","jmeter");
			// LoggingManager.setTarget(new java.io.PrintWriter(System.out));
		}

		public TestWhileController(String name) {
			super(name);
		}

		private JMeterContext jmctx;
		private JMeterVariables jmvars;
		
		public void setUp() {
			jmctx = JMeterContextService.getContext();
			jmctx.setVariables(new JMeterVariables());
			jmvars = jmctx.getVariables();
		}

		private void setLastSampleStatus(boolean status){
			jmvars.put(JMeterThread.LAST_SAMPLE_OK,Boolean.toString(status));
		}

		private void setRunning(TestElement el){
			PropertyIterator pi = el.propertyIterator();
			while(pi.hasNext()){
				pi.next().setRunningVersion(true);
			}
		}

		// Get next sample and its name
		private String nextName(GenericController c) {
			Sampler s = c.next();
			if (s == null) {
				return null;
			} else {
				return s.getPropertyAsString(TestElement.NAME);
			}
		}

		// While (blank), previous sample OK - should loop until false
		public void testBlankPrevOK() throws Exception {
//			log.info("testBlankPrevOK");
			runtestPrevOK("");
		}

		// While (LAST), previous sample OK - should loop until false
		public void testLastPrevOK() throws Exception {
//			log.info("testLASTPrevOK");
			runtestPrevOK("LAST");
		}

		private static final String OTHER = "X"; // Dummy for testing functions

		// While (LAST), previous sample OK - should loop until false
		public void testOtherPrevOK() throws Exception {
//			log.info("testOtherPrevOK");
			runtestPrevOK(OTHER);
		}

		private void runtestPrevOK(String type) throws Exception {
			GenericController controller = new GenericController();
			WhileController while_cont = new WhileController();
			setLastSampleStatus(true);
			while_cont.setCondition(type);
			while_cont.addTestElement(new TestSampler("one"));
			while_cont.addTestElement(new TestSampler("two"));
			while_cont.addTestElement(new TestSampler("three"));
			controller.addTestElement(while_cont);
			controller.addTestElement(new TestSampler("four"));
			controller.initialize();
			assertEquals("one", nextName(controller));
			assertEquals("two", nextName(controller));
			assertEquals("three", nextName(controller));
			assertEquals("one", nextName(controller));
			assertEquals("two", nextName(controller));
			assertEquals("three", nextName(controller));
			assertEquals("one", nextName(controller));
			setLastSampleStatus(false);
			if (type.equals(OTHER)){
				while_cont.setCondition("false");
			}
			assertEquals("two", nextName(controller));
			assertEquals("three", nextName(controller));
			setLastSampleStatus(true);
			if (type.equals(OTHER)) {
				while_cont.setCondition(OTHER);
			}
			assertEquals("one", nextName(controller));
			assertEquals("two", nextName(controller));
			assertEquals("three", nextName(controller));
			setLastSampleStatus(false);
			if (type.equals(OTHER)) {
				while_cont.setCondition("false");
			}
			assertEquals("four", nextName(controller));
			assertNull(nextName(controller));
			setLastSampleStatus(true);
			if (type.equals(OTHER)) {
				while_cont.setCondition(OTHER);
			}
			assertEquals("one", nextName(controller));
		}

		// While (blank), previous sample failed - should run once
		public void testBlankPrevFailed() throws Exception {
//			log.info("testBlankPrevFailed");
			GenericController controller = new GenericController();
			controller.setRunningVersion(true);
			WhileController while_cont = new WhileController();
			setLastSampleStatus(false);
			while_cont.setCondition("");
			while_cont.addTestElement(new TestSampler("one"));
			while_cont.addTestElement(new TestSampler("two"));
			controller.addTestElement(while_cont);
			controller.addTestElement(new TestSampler("three"));
			controller.initialize();
			assertEquals("one", nextName(controller));
			assertEquals("two", nextName(controller));
			assertEquals("three", nextName(controller));
			assertNull(nextName(controller));
			// Run entire test again
			assertEquals("one", nextName(controller));
			assertEquals("two", nextName(controller));
			assertEquals("three", nextName(controller));
			assertNull(nextName(controller));
		}

		public void testVariable1() throws Exception {
			GenericController controller = new GenericController();
			WhileController while_cont = new WhileController();
			setLastSampleStatus(false);
			while_cont.setCondition("${VAR}");
			jmvars.put("VAR", "");
			ValueReplacer vr = new ValueReplacer();
			vr.replaceValues(while_cont);
			setRunning(while_cont);
			controller.addTestElement(new TestSampler("before"));
			controller.addTestElement(while_cont);
			while_cont.addTestElement(new TestSampler("one"));
			while_cont.addTestElement(new TestSampler("two"));
			GenericController simple = new GenericController();
			while_cont.addTestElement(simple);
			simple.addTestElement(new TestSampler("three"));
			simple.addTestElement(new TestSampler("four"));
			controller.addTestElement(new TestSampler("after"));
			controller.initialize();
			for (int i = 1; i <= 3; i++) {
				assertEquals("Loop: "+i,"before", nextName(controller));
				assertEquals("Loop: "+i,"one", nextName(controller));
				assertEquals("Loop: "+i,"two", nextName(controller));
				assertEquals("Loop: "+i,"three", nextName(controller));
				assertEquals("Loop: "+i,"four", nextName(controller));
				assertEquals("Loop: "+i,"after", nextName(controller));
				assertNull("Loop: "+i,nextName(controller));
			}
			jmvars.put("VAR", "LAST"); // Should not enter the loop
			for (int i = 1; i <= 3; i++) {
				assertEquals("Loop: "+i,"before", nextName(controller));
				assertEquals("Loop: "+i,"after", nextName(controller));
				assertNull("Loop: "+i,nextName(controller));
			}
			jmvars.put("VAR", "");
			for (int i = 1; i <= 3; i++) {
				assertEquals("Loop: "+i,"before", nextName(controller));
				if (i==1) {
					assertEquals("Loop: "+i,"one", nextName(controller));
					assertEquals("Loop: "+i,"two", nextName(controller));
					assertEquals("Loop: "+i,"three", nextName(controller));
					jmvars.put("VAR", "LAST"); // Should not enter the loop next time
					assertEquals("Loop: "+i,"four", nextName(controller));
				}
				assertEquals("Loop: "+i,"after", nextName(controller));
				assertNull("Loop: "+i,nextName(controller));
			}
		}

		// Test with SimpleController as first item
		public void testVariable2() throws Exception {
			GenericController controller = new GenericController();
			WhileController while_cont = new WhileController();
			setLastSampleStatus(false);
			while_cont.setCondition("${VAR}");
			jmvars.put("VAR", "");
			ValueReplacer vr = new ValueReplacer();
			vr.replaceValues(while_cont);
			setRunning(while_cont);
			controller.addTestElement(new TestSampler("before"));
			controller.addTestElement(while_cont);
			GenericController simple = new GenericController();
			while_cont.addTestElement(simple);
			simple.addTestElement(new TestSampler("one"));
			simple.addTestElement(new TestSampler("two"));
			while_cont.addTestElement(new TestSampler("three"));
			while_cont.addTestElement(new TestSampler("four"));
			controller.addTestElement(new TestSampler("after"));
			controller.initialize();
			for (int i = 1; i <= 3; i++) {
				assertEquals("Loop: "+i,"before", nextName(controller));
				assertEquals("Loop: "+i,"one", nextName(controller));
				assertEquals("Loop: "+i,"two", nextName(controller));
				assertEquals("Loop: "+i,"three", nextName(controller));
				assertEquals("Loop: "+i,"four", nextName(controller));
				assertEquals("Loop: "+i,"after", nextName(controller));
				assertNull("Loop: "+i,nextName(controller));
			}
			jmvars.put("VAR", "LAST"); // Should not enter the loop
			for (int i = 1; i <= 3; i++) {
				assertEquals("Loop: "+i,"before", nextName(controller));
				assertEquals("Loop: "+i,"after", nextName(controller));
				assertNull("Loop: "+i,nextName(controller));
			}
			jmvars.put("VAR", "");
			for (int i = 1; i <= 3; i++) {
				assertEquals("Loop: "+i,"before", nextName(controller));
				if (i==1){
					assertEquals("Loop: "+i,"one", nextName(controller));
					assertEquals("Loop: "+i,"two", nextName(controller));
					jmvars.put("VAR", "LAST"); // Should not enter the loop next time
					// But should continue to the end of the loop
					assertEquals("Loop: "+i,"three", nextName(controller));
					assertEquals("Loop: "+i,"four", nextName(controller));
				}
				assertEquals("Loop: "+i,"after", nextName(controller));
				assertNull("Loop: "+i,nextName(controller));
			}
		}

		// While LAST, previous sample failed - should not run
		public void testLASTPrevFailed() throws Exception {
//			log.info("testLastPrevFailed");
			runTestPrevFailed("LAST");
		}

		// While False, previous sample failed - should not run
		public void testfalsePrevFailed() throws Exception {
//			log.info("testFalsePrevFailed");
			runTestPrevFailed("False");
		}

		private void runTestPrevFailed(String s) throws Exception {
			GenericController controller = new GenericController();
			WhileController while_cont = new WhileController();
			setLastSampleStatus(false);
			while_cont.setCondition(s);
			while_cont.addTestElement(new TestSampler("one"));
			while_cont.addTestElement(new TestSampler("two"));
			controller.addTestElement(while_cont);
			controller.addTestElement(new TestSampler("three"));
			controller.initialize();
			assertEquals("three", nextName(controller));
			assertNull(nextName(controller));
			assertEquals("three", nextName(controller));
			assertNull(nextName(controller));
		}

		public void testLastFailedBlank() throws Exception{
			runTestLastFailed("");
		}

		public void testLastFailedLast() throws Exception{
			runTestLastFailed("LAST");
		}

		// Should behave the same for blank and LAST because success on input
		private void runTestLastFailed(String s) throws Exception {
			GenericController controller = new GenericController();
			controller.addTestElement(new TestSampler("1"));
			WhileController while_cont = new WhileController();
			controller.addTestElement(while_cont);
			while_cont.setCondition(s);
			GenericController sub = new GenericController();
			while_cont.addTestElement(sub);
			sub.addTestElement(new TestSampler("2"));
			sub.addTestElement(new TestSampler("3"));
			
			controller.addTestElement(new TestSampler("4"));

			setLastSampleStatus(true);
			controller.initialize();
			assertEquals("1", nextName(controller));
			assertEquals("2", nextName(controller));
			setLastSampleStatus(false);
			assertEquals("3", nextName(controller));
			assertEquals("4", nextName(controller));
			assertNull(nextName(controller));
		}

		// Tests for Stack Overflow (bug 33954)
		public void testAlwaysFailOK() throws Exception {
			runTestAlwaysFail(true); // Should be OK
		}

		public void testAlwaysFailBAD() throws Exception {
			runTestAlwaysFail(false);
		}

		private void runTestAlwaysFail(boolean other) {
			LoopController controller = new LoopController();
			controller.setContinueForever(true);
			controller.setLoops(-1);
			WhileController while_cont = new WhileController();
			setLastSampleStatus(false);
			while_cont.setCondition("false");
			while_cont.addTestElement(new TestSampler("one"));
			while_cont.addTestElement(new TestSampler("two"));
			controller.addTestElement(while_cont);
			if (other) {
				controller.addTestElement(new TestSampler("three"));
			}
			controller.initialize();
			try {
				if (other) {
					assertEquals("three", nextName(controller));
				} else {
					assertNull(nextName(controller));
				}
			} catch (StackOverflowError e) {
				// e.printStackTrace();
				fail(e.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10096.java