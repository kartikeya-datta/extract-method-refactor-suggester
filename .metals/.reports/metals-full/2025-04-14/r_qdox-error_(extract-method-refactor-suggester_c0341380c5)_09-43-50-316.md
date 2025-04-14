error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1823.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1823.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1823.java
text:
```scala
r@@eturn (OnMessageSubscriber) client_map.get(key);

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
 */

package org.apache.jmeter.protocol.jms.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author pete
 * 
 * ClientPool holds the client instances in an ArrayList. The main purpose of
 * this is to make it easier to clean up all the instances at the end of a test.
 * If we didn't do this, threads might become zombie.
 */
public class ClientPool {

	private static ArrayList clients = new ArrayList();

	private static HashMap client_map = new HashMap();

	/**
	 * Add a ReceiveClient to the ClientPool. This is so that we can make sure
	 * to close all clients and make sure all threads are destroyed.
	 * 
	 * @param client
	 */
	public static void addClient(ReceiveSubscriber client) {
		clients.add(client);
	}

	/**
	 * Add a OnMessageClient to the ClientPool. This is so that we can make sure
	 * to close all clients and make sure all threads are destroyed.
	 * 
	 * @param client
	 */
	public static void addClient(OnMessageSubscriber client) {
		clients.add(client);
	}

	/**
	 * Add a Publisher to the ClientPool. This is so that we can make sure to
	 * close all clients and make sure all threads are destroyed.
	 * 
	 * @param client
	 */
	public static void addClient(Publisher client) {
		clients.add(client);
	}

	/**
	 * Clear all the clients created by either Publish or Subscribe sampler. We
	 * need to do this to make sure all the threads creatd during the test are
	 * destroyed and cleaned up. In some cases, the client provided by the
	 * manufacturer of the JMS server may have bugs and some threads may become
	 * zombie. In those cases, it is not the responsibility of JMeter for those
	 * bugs.
	 */
	public static void clearClient() {
		Iterator itr = clients.iterator();
		while (itr.hasNext()) {
			Object client = itr.next();
			if (client instanceof ReceiveSubscriber) {
				ReceiveSubscriber sub = (ReceiveSubscriber) client;
				if (sub != null) {
					sub.close();
					sub = null;
				}
			} else if (client instanceof Publisher) {
				Publisher pub = (Publisher) client;
				if (pub != null) {
					pub.close();
					pub = null;
				}
			} else if (client instanceof OnMessageSubscriber) {
				OnMessageSubscriber sub = (OnMessageSubscriber) client;
				if (sub != null) {
					sub.close();
					sub = null;
				}
			}
		}
		clients.clear();
		client_map.clear();
	}

	public static void put(Object key, OnMessageSubscriber client) {
		client_map.put(key, client);
	}

	public static void put(Object key, Publisher client) {
		client_map.put(key, client);
	}

	public static Object get(Object key) {
		return client_map.get(key);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1823.java