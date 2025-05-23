error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3838.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3838.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3838.java
text:
```scala
f@@irst = false; // TODO - should this use setFirst() ?

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * <p>
 * This class is the basis for all the controllers.
 * It also implements SimpleController.
 * </p>
 * <p>
 * The main entry point is next(), which is called by by JMeterThread as follows:
 * </p>
 * <p>
 * <code>while (running && (sampler = controller.next()) != null)</code>
 * </p>
 */
public class GenericController extends AbstractTestElement implements Controller, Serializable {
	private static final Logger log = LoggingManager.getLoggerForClass();

	private transient LinkedList iterationListeners = new LinkedList();

	// May be replaced by RandomOrderController
	protected transient List subControllersAndSamplers = new ArrayList();

	protected transient int current;

	private transient int iterCount;

	private transient boolean done, first;

	/**
	 * Creates a Generic Controller
	 */
	public GenericController() {
	}

	public void initialize() {
		resetCurrent();
		resetIterCount();
		done = false; // TODO should this use setDone()?
		first = true; // TODO should this use setFirst()?
		TestElement elem;
		for (int i = 0; i < subControllersAndSamplers.size(); i++) {
			elem = (TestElement) subControllersAndSamplers.get(i);
			if (elem instanceof Controller) {
				((Controller) elem).initialize();
			}
		}
	}

	/**
	 * Resets the controller:
	 * <ul>
	 * <li>resetCurrent() (i.e. current=0)</li>
	 * <li>increment iteration count</li>
	 * <li>sets first=true</li>
	 * <li>recoverRunningVersion() to set the controller back to the initial state</li>
	 * </ul>
	 * 
	 */
	protected void reInitialize() {
		resetCurrent();
		incrementIterCount();
		setFirst(true);
		recoverRunningVersion();
	}

	/**
	 * <p>
	 * Determines the next sampler to be processed.
	 * </p>
	 * 
	 * <p>
	 * If isDone, returns null.
	 * </p>
	 * 
	 * <p>
	 * Gets the list element using current pointer.
	 * If this is null, calls {@link #nextIsNull()}.
	 * </p>
	 * 
	 * <p>
	 * If the list element is a sampler, calls {@link #nextIsASampler(Sampler)},
	 * otherwise calls {@link #nextIsAController(Controller)}
	 * </p>
	 * 
	 * <p>
	 * If any of the called methods throws NextIsNullException, returns null,
	 * otherwise the value obtained above is returned.
	 * </p>
	 * 
	 * @return the next sampler or null
	 */
	public Sampler next() {
		fireIterEvents();
		if (log.isDebugEnabled()) {
			log.debug("Calling next on: " + this.getClass().getName());
		}
		if (isDone()) {
			return null;
		}
		Sampler returnValue = null;
		try {
			TestElement currentElement = getCurrentElement();
			setCurrentElement(currentElement);
			if (currentElement == null) {
				// incrementCurrent();
				returnValue = nextIsNull();
			} else {
				if (currentElement instanceof Sampler) {
					returnValue = nextIsASampler((Sampler) currentElement);
				} else { // must be a controller
					returnValue = nextIsAController((Controller) currentElement);
				}
			}
		} catch (NextIsNullException e) {
			returnValue = null;
		}
		return returnValue;
	}

	/**
	 * @see org.apache.jmeter.control.Controller#isDone()
	 */
	public boolean isDone() {
		return done;
	}

	protected void setDone(boolean done) {
		this.done = done;
	}

	protected boolean isFirst() {
		return first;
	}

	public void setFirst(boolean b) {
		first = b;
	}

	/**
	 * Called by next() if the element is a Controller,
	 * and returns the next sampler from the controller.
	 * If this is null, then updates the current pointer and makes recursive call to next().
	 * @param controller
	 * @return the next sampler
	 * @throws NextIsNullException
	 */
	protected Sampler nextIsAController(Controller controller) throws NextIsNullException {
		Sampler sampler = controller.next();
		if (sampler == null) {
			currentReturnedNull(controller);
			sampler = next();
		}
		return sampler;
	}

	/**
	 * Increment the current pointer and return the element.
	 * Called by next() if the element is a sampler.
	 * (May be overriden by sub-classes).
	 *  
	 * @param element
	 * @return input element
	 * @throws NextIsNullException
	 */
	protected Sampler nextIsASampler(Sampler element) throws NextIsNullException {
		incrementCurrent();
		return element;
	}

	/**
	 * Called by next() when getCurrentElement() returns null.
	 * Reinitialises the controller.
	 * 
	 * @return null (always, for this class)
	 * @throws NextIsNullException
	 */
	protected Sampler nextIsNull() throws NextIsNullException {
		reInitialize();
		return null;
	}

	/**
	 * If the controller is done, remove it from the list,
	 * otherwise increment to next entry in list.
	 * 
	 * @param c controller
	 */
	protected void currentReturnedNull(Controller c) {
		if (c.isDone()) {
			removeCurrentElement();
		} else {
			incrementCurrent();
		}
	}

	/**
	 * Gets the SubControllers attribute of the GenericController object
	 * 
	 * @return the SubControllers value
	 */
	protected List getSubControllers() {
		return subControllersAndSamplers;
	}

	private void addElement(TestElement child) {
		subControllersAndSamplers.add(child);
	}

	/**
	 * Empty implementation - does nothing.
	 * 
	 * @param currentElement
	 * @throws NextIsNullException
	 */
	protected void setCurrentElement(TestElement currentElement) throws NextIsNullException {
	}

	/**
	 * <p>
	 * Gets the element indicated by the <code>current</code> index, if one exists,
	 * from the <code>subControllersAndSamplers</code> list.
	 * </p>
	 * <p>
	 * If the <code>subControllersAndSamplers</code> list is empty, 
	 * then set done = true, and throw NextIsNullException.
	 * </p>
	 * @return the current element - or null if current index too large
	 * @throws NextIsNullException if list is empty
	 */
	protected TestElement getCurrentElement() throws NextIsNullException {
		if (current < subControllersAndSamplers.size()) {
			return (TestElement) subControllersAndSamplers.get(current);
		} else {
			if (subControllersAndSamplers.size() == 0) {
				setDone(true);
				throw new NextIsNullException();
			}
			return null;
		}
	}

	protected void removeCurrentElement() {
		subControllersAndSamplers.remove(current);
	}

	/**
	 * Increments the current pointer; called by currentReturnedNull to move the
	 * controller on to its next child.
	 */
	protected void incrementCurrent() {
		current++;
	}

	protected void resetCurrent() {
		current = 0;
	}

	public void addTestElement(TestElement child) {
		if (child instanceof Controller || child instanceof Sampler) {
			addElement(child);
		}
	}

	public void addIterationListener(LoopIterationListener lis) {
		/*
		 * A little hack - add each listener to the start of the list - this
		 * ensures that the thread running the show is the first listener and
		 * can modify certain values before other listeners are called.
		 */
		iterationListeners.addFirst(lis);
	}

	protected void fireIterEvents() {
		if (isFirst()) {
			fireIterationStart();
			first = false;
		}
	}

	protected void fireIterationStart() {
		Iterator iter = iterationListeners.iterator();
		LoopIterationEvent event = new LoopIterationEvent(this, getIterCount());
		while (iter.hasNext()) {
			LoopIterationListener item = (LoopIterationListener) iter.next();
			item.iterationStart(event);
		}
	}

	protected int getIterCount() {
		return iterCount;
	}

	protected void incrementIterCount() {
		iterCount++;
	}

	protected void resetIterCount() {
		iterCount = 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3838.java