error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9057.java
text:
```scala
M@@ap<?, ?> variables = new HashMap<Object, Object>();

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

import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.engine.util.ReplaceStringWithFunctions;
import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.junit.stubs.TestSampler;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

public class TestSwitchController extends JMeterTestCase {
//      static {
//           LoggingManager.setPriority("DEBUG","jmeter");
//           LoggingManager.setTarget(new java.io.PrintWriter(System.out));
//      }

        public TestSwitchController(String name) {
            super(name);
        }

        // Get next sample and its name
        private String nextName(GenericController c) {
            Sampler s = c.next();
            String n;
            if (s == null) {
                return null;
            }
            n = s.getName();
            return n;
        }

        public void test() throws Exception {
            runSimpleTests("", "zero");
        }

        public void test0() throws Exception {
            runSimpleTests("0", "zero");
        }

        public void test1() throws Exception {
            runSimpleTests("1", "one");
            runSimpleTests("one", "one"); // Match by name
        }

        public void test2() throws Exception {
            runSimpleTests("2", "two");
            runSimpleTests("two", "two"); // Match by name
        }

        public void test3() throws Exception {
            runSimpleTests("3", "three");
            runSimpleTests("three", "three"); // Match by name
        }

        public void test4() throws Exception {
            runSimpleTests("4", "zero");
        }

        public void testX() throws Exception {
            runSimpleTests("X", null); // should not run any children
            runSimpleTest2("X", "one", "Default"); // should match the default entry
        }

        private void runSimpleTests(String cond, String exp) throws Exception {
            runSimpleTest(cond, exp);
            runSimpleTest2(cond, exp, "one");
        }

        /*
         *  Simple test with single Selection controller
         *  Generic Controller
         *  + Sampler "before"
         *  + Switch Controller
         *  + + Sampler "zero"
         *  + + Sampler "one"
         *  + + Sampler "two"
         *  + + Sampler "three"
         *  + Sampler "after"
         */
        private void runSimpleTest(String cond, String exp) throws Exception {
            GenericController controller = new GenericController();

            SwitchController switch_cont = new SwitchController();
            switch_cont.setSelection(cond);

            controller.addTestElement(new TestSampler("before"));
            controller.addTestElement(switch_cont);

            switch_cont.addTestElement(new TestSampler("zero"));
            switch_cont.addTestElement(new TestSampler("one"));
            switch_cont.addTestElement(new TestSampler("two"));
            switch_cont.addTestElement(new TestSampler("three"));

            controller.addTestElement(new TestSampler("after"));

            controller.initialize();

            for (int i = 1; i <= 3; i++) {
                assertEquals("Loop " + i, "before", nextName(controller));
                if (exp!=null){
                    assertEquals("Loop " + i, exp, nextName(controller));
                }
                assertEquals("Loop " + i, "after", nextName(controller));
                assertNull(nextName(controller));
            }
        }

        // Selection controller with two sub-controllers, but each has only 1
        // child
        /*
         * Controller
         * + Before
         * + Switch (cond)
         * + + zero
         * + + Controller sub_1
         * + + + one
         * + + two
         * + + Controller sub_2
         * + + + three
         * + After
         */
        private void runSimpleTest2(String cond, String exp, String sub1Name) throws Exception {
            GenericController controller = new GenericController();
            GenericController sub_1 = new GenericController();
            GenericController sub_2 = new GenericController();

            SwitchController switch_cont = new SwitchController();
            switch_cont.setSelection(cond);

            switch_cont.addTestElement(new TestSampler("zero"));
            switch_cont.addTestElement(sub_1);
            sub_1.addTestElement(new TestSampler("one"));
            sub_1.setName(sub1Name);

            switch_cont.addTestElement(new TestSampler("two"));

            switch_cont.addTestElement(sub_2);
            sub_2.addTestElement(new TestSampler("three"));
            sub_2.setName("three");

            controller.addTestElement(new TestSampler("before"));
            controller.addTestElement(switch_cont);
            controller.addTestElement(new TestSampler("after"));
            controller.initialize();
            for (int i = 1; i <= 3; i++) {
                assertEquals("Loop="+i,"before", nextName(controller));
                if (exp!=null){
                    assertEquals("Loop="+i,exp, nextName(controller));
                }
                assertEquals("Loop="+i,"after", nextName(controller));
                assertNull("Loop="+i,nextName(controller));
            }
        }

        public void testTest2() throws Exception {
            runTest2("", new String[] { "zero" });
            runTest2("0", new String[] { "zero" });
            runTest2("7", new String[] { "zero" });
            runTest2("5", new String[] { "zero" });
            runTest2("4", new String[] { "six" });
            runTest2("3", new String[] { "five" });
            runTest2("1", new String[] { "one", "two" });
            runTest2("2", new String[] { "three", "four" });
        }

        /*
         * Test: 
         * Before 
         * Selection Controller
         *  - zero (default)
         *  - simple controller 1
         *  - - one
         *  - - two
         *  - simple controller 2
         *  - - three
         *  - - four
         *  - five
         *  - six
         * After
         * 
         * cond  = Switch condition 
         * exp[] = expected results
         */
        private void runTest2(String cond, String exp[]) throws Exception {
            int loops = 3;
            LoopController controller = new LoopController();
            controller.setLoops(loops);
            controller.setContinueForever(false);
            GenericController sub_1 = new GenericController();
            GenericController sub_2 = new GenericController();

            SwitchController switch_cont = new SwitchController();
            switch_cont.setSelection(cond);

            switch_cont.addTestElement(new TestSampler("zero"));
            switch_cont.addTestElement(sub_1);
            sub_1.addTestElement(new TestSampler("one"));
            sub_1.addTestElement(new TestSampler("two"));

            switch_cont.addTestElement(sub_2);
            sub_2.addTestElement(new TestSampler("three"));
            sub_2.addTestElement(new TestSampler("four"));

            switch_cont.addTestElement(new TestSampler("five"));
            switch_cont.addTestElement(new TestSampler("six"));

            controller.addTestElement(new TestSampler("before"));
            controller.addTestElement(switch_cont);
            controller.addTestElement(new TestSampler("after"));
            controller.setRunningVersion(true);
            sub_1.setRunningVersion(true);
            sub_2.setRunningVersion(true);
            switch_cont.setRunningVersion(true);
            controller.initialize();
            for (int i = 1; i <= 3; i++) {
                assertEquals("Loop:" + i, "before", nextName(controller));
                for (int j = 0; j < exp.length; j++) {
                    assertEquals("Loop:" + i, exp[j], nextName(controller));
                }
                assertEquals("Loop:" + i, "after", nextName(controller));
            }
            assertNull("Loops:" + loops, nextName(controller));
        }
        
        /*
         * N.B. Requires ApacheJMeter_functions.jar to be on the classpath,
         * otherwise the function cannot be resolved.
        */
        public void testFunction() throws Exception {
            JMeterContext jmctx = JMeterContextService.getContext();
            Map variables = new HashMap();
            ReplaceStringWithFunctions transformer = new ReplaceStringWithFunctions(new CompoundVariable(), variables);
            jmctx.setVariables(new JMeterVariables());
            JMeterVariables jmvars = jmctx.getVariables();
            jmvars.put("VAR", "100");
            StringProperty prop = new StringProperty(SwitchController.SWITCH_VALUE,"${__counter(TRUE,VAR)}");
            JMeterProperty newProp = transformer.transformValue(prop);
            newProp.setRunningVersion(true);
            
            GenericController controller = new GenericController();

            SwitchController switch_cont = new SwitchController();
            switch_cont.setProperty(newProp);

            controller.addTestElement(new TestSampler("before"));
            controller.addTestElement(switch_cont);

            switch_cont.addTestElement(new TestSampler("0"));
            switch_cont.addTestElement(new TestSampler("1"));
            switch_cont.addTestElement(new TestSampler("2"));
            switch_cont.addTestElement(new TestSampler("3"));

            controller.addTestElement(new TestSampler("after"));

            controller.initialize();

            assertEquals("100",jmvars.get("VAR"));
            
            for (int i = 1; i <= 3; i++) {
                assertEquals("Loop " + i, "before", nextName(controller));
                assertEquals("Loop " + i, ""+i, nextName(controller));
                assertEquals("Loop " + i, ""+i, jmvars.get("VAR"));
                assertEquals("Loop " + i, "after", nextName(controller));
                assertNull(nextName(controller));
            }
            int i = 4;
            assertEquals("Loop " + i, "before", nextName(controller));
            assertEquals("Loop " + i, "0", nextName(controller));
            assertEquals("Loop " + i, ""+i, jmvars.get("VAR"));
            assertEquals("Loop " + i, "after", nextName(controller));
            assertNull(nextName(controller));
            assertEquals("4",jmvars.get("VAR"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9057.java