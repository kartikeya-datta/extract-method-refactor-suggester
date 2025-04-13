error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9373.java
text:
```scala
P@@reCompiler compiler = new PreCompiler(true); // limit the changes to client only test elements

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

package org.apache.jmeter.engine;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Class to run remote tests from the client JMeter and collect remote samples
 */
public class ClientJMeterEngine implements JMeterEngine, Runnable {
	private static final Logger log = LoggingManager.getLoggerForClass();

	RemoteJMeterEngine remote;

	HashTree test;

	SearchByClass testListeners;

	ConvertListeners sampleListeners;

	private String host;

	private static RemoteJMeterEngine getEngine(String h) throws MalformedURLException, RemoteException,
			NotBoundException {
		return (RemoteJMeterEngine) Naming.lookup("//" + h + "/JMeterEngine"); // $NON-NLS-1$ $NON-NLS-2$
	}

	public ClientJMeterEngine(String host) throws MalformedURLException, NotBoundException, RemoteException {
		this(getEngine(host));
		this.host = host;
	}

	public ClientJMeterEngine(RemoteJMeterEngine remote) {
		this.remote = remote;
	}

	protected HashTree getTestTree() {
		return test;
	}

	public void configure(HashTree testTree) {
		test = testTree;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void runTest() {
		log.info("about to run remote test");
		new Thread(this).start();
		log.info("done initiating run command");
	}

	public void stopTest() {
		log.info("about to stop remote test on "+host);
		try {
			remote.stopTest();
		} catch (Exception ex) {
			log.error("", ex); // $NON-NLS-1$
		}
	}

	public void reset() {
		try {
			try {
				remote.reset();
			} catch (java.rmi.ConnectException e) {
				remote = getEngine(host);
				remote.reset();
			}
		} catch (Exception ex) {
			log.error("", ex); // $NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		log.info("running clientengine run method");
		testListeners = new SearchByClass(TestListener.class);
		sampleListeners = new ConvertListeners();
		HashTree testTree = getTestTree();
		PreCompiler compiler = new PreCompiler();
		synchronized(testTree) {
			testTree.traverse(compiler);
			testTree.traverse(new TurnElementsOn());
			testTree.traverse(testListeners);
			testTree.traverse(sampleListeners);
		}
		
		try {
			JMeterContextService.startTest();
			remote.setHost(host);
			log.info("sent host =" + host);
			remote.configure(test);
			log.info("sent test");
			remote.runTest();
			log.info("sent run command");
		} catch (Exception ex) {
			log.error("", ex); // $NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.engine.JMeterEngine#exit()
	 */
	public void exit() {
		log.info("about to exit remote server on "+host);
		try {
			remote.exit();
		} catch (RemoteException e) {
			log.warn("Could not perform remote exit: " + e.toString());
		}
	}

	public void setProperties(Properties p) {
		log.info("Sending properties "+p);
		try {
			remote.setProperties(p);
		} catch (RemoteException e) {
			log.warn("Could not set properties: " + e.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9373.java