error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12810.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12810.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12810.java
text:
```scala
private transient T@@estElement searchStart = null;

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

import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.IntegerProperty;

public class InterleaveControl extends GenericController implements Serializable {
	private static final String STYLE = "InterleaveControl.style";// $NON-NLS-1$

	public static final int IGNORE_SUB_CONTROLLERS = 0;

	public static final int USE_SUB_CONTROLLERS = 1;

	private boolean skipNext;

	transient private TestElement searchStart = null;

	private boolean currentReturnedAtLeastOne;

	private boolean stillSame = true;

	/***************************************************************************
	 * Constructor for the InterleaveControl object
	 **************************************************************************/
	public InterleaveControl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.control.GenericController#reInitialize()
	 */
	public void reInitialize() {
		setFirst(true);
		currentReturnedAtLeastOne = false;
		searchStart = null;
		stillSame = true;
		skipNext = false;
		incrementIterCount();
		recoverRunningVersion();
	}

	public void setStyle(int style) {
		setProperty(new IntegerProperty(STYLE, style));
	}

	public int getStyle() {
		return getPropertyAsInt(STYLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.control.Controller#next()
	 */
	public Sampler next() {
		if (isSkipNext()) {
			reInitialize();
			return null;
		}
		return super.next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GenericController#nextIsAController(Controller)
	 */
	protected Sampler nextIsAController(Controller controller) throws NextIsNullException {
		Sampler sampler = controller.next();
		if (sampler == null) {
			currentReturnedNull(controller);
			return next();
		}
		currentReturnedAtLeastOne = true;
		if (getStyle() == IGNORE_SUB_CONTROLLERS) {
			incrementCurrent();
			skipNext = true;
		} else {
			searchStart = null;
		}
		return sampler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.control.GenericController#nextIsASampler(Sampler)
	 */
	protected Sampler nextIsASampler(Sampler element) throws NextIsNullException {
		skipNext = true;
		incrementCurrent();
		return element;
	}

	/**
	 * If the current is null, reset and continue searching. The searchStart
	 * attribute will break us off when we start a repeat.
	 * 
	 * @see org.apache.jmeter.control.GenericController#nextIsNull()
	 */
	protected Sampler nextIsNull() {
		resetCurrent();
		return next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GenericController#setCurrentElement(TestElement)
	 */
	protected void setCurrentElement(TestElement currentElement) throws NextIsNullException {
		// Set the position when next is first called, and don't overwrite
		// until reInitialize is called.
		if (searchStart == null) {
			searchStart = currentElement;
		} else if (searchStart == currentElement && !stillSame) {
			// We've gone through the whole list and are now back at the start
			// point of our search.
			reInitialize();
			throw new NextIsNullException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GenericController#currentReturnedNull(Controller)
	 */
	protected void currentReturnedNull(Controller c) {
		if (c.isDone()) {
			removeCurrentElement();
		} else if (getStyle() == USE_SUB_CONTROLLERS) {
			incrementCurrent();
		}
	}

	/**
	 * @return skipNext
	 */
	protected boolean isSkipNext() {
		return skipNext;
	}

	/**
	 * @param skipNext
	 */
	protected void setSkipNext(boolean skipNext) {
		this.skipNext = skipNext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.control.GenericController#incrementCurrent()
	 */
	protected void incrementCurrent() {
		if (currentReturnedAtLeastOne) {
			skipNext = true;
		}
		stillSame = false;
		super.incrementCurrent();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12810.java