error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6357.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6357.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6357.java
text:
```scala
public v@@oid clearData() {

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
 */
package org.apache.jmeter.visualizers;

import java.io.Serializable;

import org.apache.jmeter.samplers.Clearable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MonitorModel implements Clearable, Serializable, Cloneable {

	// private String name;
	private List listeners;

	private MonitorStats current = new MonitorStats(0, 0, 0, 0, 0, "", "", "", System.currentTimeMillis());

	/**
	 * 
	 */
	public MonitorModel() {
		super();
		listeners = new LinkedList();
	}

	public MonitorModel(MonitorStats stat) {
		this.current = stat;
	}

	public void setName(String name) {
		// this.name = name;
	}

	public String getName() {
		return this.getURL();
	}

	public int getHealth() {
		return this.current.getHealth();
	}

	public int getLoad() {
		return this.current.getLoad();
	}

	public int getCpuload() {
		return this.current.getCpuLoad();
	}

	public int getMemload() {
		return this.current.getMemLoad();
	}

	public int getThreadload() {
		return this.current.getThreadLoad();
	}

	public String getHost() {
		return this.current.getHost();
	}

	public String getPort() {
		return this.current.getPort();
	}

	public String getProtocol() {
		return this.current.getProtocol();
	}

	public long getTimestamp() {
		return this.current.getTimeStamp();
	}

	public String getURL() {
		return this.current.getURL();
	}

	/**
	 * Method will return a formatted date using SimpleDateFormat.
	 * 
	 * @return String
	 */
	public String getTimestampString() {
		Date date = new Date(this.current.getTimeStamp());
		SimpleDateFormat ft = new SimpleDateFormat();
		return ft.format(date);
	}

	/**
	 * Method is used by DefaultMutableTreeNode to get the label for the node.
	 */
	public String toString() {
		return getURL();
	}

	/**
	 * clear will create a new MonitorStats object.
	 */
	public void clear() {
		current = new MonitorStats(0, 0, 0, 0, 0, "", "", "", System.currentTimeMillis());
	}

	/**
	 * notify the listeners with the MonitorModel object.
	 * 
	 * @param model
	 */
	public void notifyListeners(MonitorModel model) {
		for (int idx = 0; idx < listeners.size(); idx++) {
			MonitorListener ml = (MonitorListener) listeners.get(idx);
			ml.addSample(model);
		}
	}

	public void addListener(MonitorListener listener) {
		listeners.add(listener);
	}

	/**
	 * a clone method is provided for convienance. In some cases, it may be
	 * desirable to clone the object.
	 */
	public Object clone() {
		return new MonitorModel(cloneMonitorStats());
	}

	/**
	 * a clone method to clone the stats
	 * 
	 * @return
	 */
	public MonitorStats cloneMonitorStats() {
		return new MonitorStats(current.getHealth(), current.getLoad(), current.getCpuLoad(), current.getMemLoad(),
				current.getThreadLoad(), current.getHost(), current.getPort(), current.getProtocol(), current
						.getTimeStamp());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6357.java