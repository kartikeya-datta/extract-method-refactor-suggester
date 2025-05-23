error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18305.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18305.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18305.java
text:
```scala
private static final S@@et pairing = new HashSet();

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

package org.apache.jmeter.threads;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.control.TransactionSampler;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.timers.Timer;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.HashTreeTraverser;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class TestCompiler implements HashTreeTraverser {
	private static final Logger log = LoggingManager.getLoggerForClass();

	private LinkedList stack = new LinkedList();

	private Map samplerConfigMap = new HashMap();

	private Map transactionControllerConfigMap = new HashMap();
	
	private HashTree testTree;

	/*
	 * This set keeps track of which ObjectPairs have been seen Its purpose is
	 * not entirely clear (please document if you know!) but it is needed,..
	 */
	private static Set pairing = new HashSet();

	//List loopIterListeners = new ArrayList();

	public TestCompiler(HashTree testTree, JMeterVariables vars) {
		this.testTree = testTree;
	}

	/**
	 * Clears the pairing Set Called by StandardJmeterEngine at the start of a
	 * test run.
	 */
	public static void initialize() {
		// synch is probably not needed as only called before run starts
		synchronized (pairing) {
			pairing.clear();
		}
	}

	public SamplePackage configureSampler(Sampler sampler) {
		SamplePackage pack = (SamplePackage) samplerConfigMap.get(sampler);
		pack.setSampler(sampler);
		configureWithConfigElements(sampler, pack.getConfigs());
		runPreProcessors(pack.getPreProcessors());
		return pack;
	}
    
    public SamplePackage configureTransactionSampler(TransactionSampler transactionSampler) {
        TransactionController controller = transactionSampler.getTransactionController();
        SamplePackage pack = (SamplePackage) transactionControllerConfigMap.get(controller);
        pack.setSampler(transactionSampler);
        return pack;
    }

	private void runPreProcessors(List preProcessors) {
		Iterator iter = preProcessors.iterator();
		while (iter.hasNext()) {
			PreProcessor ex = (PreProcessor) iter.next();
			if (log.isDebugEnabled()) {
				log.debug("Running preprocessor: " + ((AbstractTestElement) ex).getName());
			}
			TestBeanHelper.prepare((TestElement) ex);
			ex.process();
		}
	}

	public void done(SamplePackage pack) {
		pack.recoverRunningVersion();
	}

	public void addNode(Object node, HashTree subTree) {
		stack.addLast(node);
	}

	public void subtractNode() {
		log.debug("Subtracting node, stack size = " + stack.size());
		TestElement child = (TestElement) stack.getLast();
		trackIterationListeners(stack);
		if (child instanceof Sampler) {
			saveSamplerConfigs((Sampler) child);
		}
        else if(child instanceof TransactionController) {
            saveTransactionControllerConfigs((TransactionController) child);
        }
		stack.removeLast();
		if (stack.size() > 0) {
			ObjectPair pair = new ObjectPair(child, (TestElement) stack.getLast());
			synchronized (pairing) {// Called from multiple threads
				if (!pairing.contains(pair)) {
					pair.addTestElements();
					pairing.add(pair);
				}
			}
		}
	}

	private void trackIterationListeners(LinkedList p_stack) {
		TestElement child = (TestElement) p_stack.getLast();
		if (child instanceof LoopIterationListener) {
			ListIterator iter = p_stack.listIterator(p_stack.size());
			while (iter.hasPrevious()) {
				TestElement item = (TestElement) iter.previous();
				if (item == child) {
					continue;
				}
				if (item instanceof Controller) {
					TestBeanHelper.prepare(child);
					((Controller) item).addIterationListener((LoopIterationListener) child);
					break;
				}
			}
		}
	}

	public void processPath() {
	}

	private void saveSamplerConfigs(Sampler sam) {
		List configs = new LinkedList();
		List modifiers = new LinkedList();
		List controllers = new LinkedList();
		List responseModifiers = new LinkedList();
		List listeners = new LinkedList();
		List timers = new LinkedList();
		List assertions = new LinkedList();
		LinkedList posts = new LinkedList();
		LinkedList pres = new LinkedList();
		for (int i = stack.size(); i > 0; i--) {
			addDirectParentControllers(controllers, (TestElement) stack.get(i - 1));
			Iterator iter = testTree.list(stack.subList(0, i)).iterator();
			List tempPre = new LinkedList();
			List tempPost = new LinkedList();
			while (iter.hasNext()) {
				TestElement item = (TestElement) iter.next();
				if ((item instanceof ConfigTestElement)) {
					configs.add(item);
				}
				if (item instanceof SampleListener) {
					listeners.add(item);
				}
				if (item instanceof Timer) {
					timers.add(item);
				}
				if (item instanceof Assertion) {
					assertions.add(item);
				}
				if (item instanceof PostProcessor) {
					tempPost.add(item);
				}
				if (item instanceof PreProcessor) {
					tempPre.add(item);
				}
			}
			pres.addAll(0, tempPre);
			posts.addAll(0, tempPost);
		}

		SamplePackage pack = new SamplePackage(configs, modifiers, responseModifiers, listeners, timers, assertions,
				posts, pres, controllers);
		pack.setSampler(sam);
		pack.setRunningVersion(true);
		samplerConfigMap.put(sam, pack);
	}
    
    private void saveTransactionControllerConfigs(TransactionController tc) {
        List configs = new LinkedList();
        List modifiers = new LinkedList();
        List controllers = new LinkedList();
        List responseModifiers = new LinkedList();
        List listeners = new LinkedList();
        List timers = new LinkedList();
        List assertions = new LinkedList();
        LinkedList posts = new LinkedList();
        LinkedList pres = new LinkedList();
        for (int i = stack.size(); i > 0; i--) {
            addDirectParentControllers(controllers, (TestElement) stack.get(i - 1));
            Iterator iter = testTree.list(stack.subList(0, i)).iterator();
            while (iter.hasNext()) {
                TestElement item = (TestElement) iter.next();
                if (item instanceof SampleListener) {
                    listeners.add(item);
                }
                if (item instanceof Assertion) {
                    assertions.add(item);
                }
            }
        }

        SamplePackage pack = new SamplePackage(configs, modifiers, responseModifiers, listeners, timers, assertions,
                posts, pres, controllers);
        pack.setSampler(new TransactionSampler(tc, tc.getName()));
        pack.setRunningVersion(true);
        transactionControllerConfigMap.put(tc, pack);
    }

	/**
	 * @param controllers
	 * @param i
	 */
	private void addDirectParentControllers(List controllers, TestElement maybeController) {
		if (maybeController instanceof Controller) {
			log.debug("adding controller: " + maybeController + " to sampler config");
			controllers.add(maybeController);
		}
	}

	private static class ObjectPair
	{
		TestElement child, parent;

		public ObjectPair(TestElement one, TestElement two) {
			this.child = one;
			this.parent = two;
		}

		public void addTestElements() {
			if (parent instanceof Controller && (child instanceof Sampler || child instanceof Controller)) {
				parent.addTestElement(child);
			}
		}

		public int hashCode() {
			return child.hashCode() + parent.hashCode();
		}

		public boolean equals(Object o) {
			if (o instanceof ObjectPair) {
				return child == ((ObjectPair) o).child && parent == ((ObjectPair) o).parent;
			}
			return false;
		}
	}

	private void configureWithConfigElements(Sampler sam, List configs) {
		Iterator iter = configs.iterator();
		while (iter.hasNext()) {
			ConfigTestElement config = (ConfigTestElement) iter.next();
			sam.addTestElement(config);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18305.java