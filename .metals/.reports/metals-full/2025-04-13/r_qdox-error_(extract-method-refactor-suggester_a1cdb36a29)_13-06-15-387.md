error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16383.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16383.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16383.java
text:
```scala
l@@og.error(e.getRootCause().toString());//JDK1.4 getCause()

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
 
package org.apache.jmeter.protocol.jms.client;

import javax.naming.Context;
import javax.naming.NamingException;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicConnection;

import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.engine.event.LoopIterationEvent;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author pete
 *
 * ConnectionFactory is responsible for creating new connections.
 * Eventually, the connection factory should read an external
 * configuration file and create a pool of connections. The current
 * implementation just does the basics. Once the tires get kicked
 * a bit, we can add connection pooling support.
 */
public class ConnectionFactory implements TestListener {

	private static TopicConnectionFactory factory = null;
	private static QueueConnectionFactory qfactory = null;
	static Logger log = LoggingManager.getLoggerForClass();
	
    /**
     * 
     */
    protected ConnectionFactory() {
        super();
    }

	public void testStarted(String test){
	}
	
	public void testEnded(String test){
		testEnded();
	}

	/**
	 * endTest cleans up the client
	 * @see junit.framework.TestListener#endTest(junit.framework.Test)
	 */
	public void testEnded() {
		factory = null;
	}

	/**
	 * startTest sets up the client and gets it ready for the
	 * test. Since async messaging is different than request/
	 * response applications, the connection is created at the
	 * beginning of the test and closed at the end of the test.
	 */
	public void testStarted() {
	}

	public void testIterationStart(LoopIterationEvent event){
	}

	/**
	 * 
	 * @param ctx
	 * @param fac
	 * @return
	 */
	public static synchronized TopicConnectionFactory getTopicConnectionFactory(
	Context ctx, String fac){
		while (factory == null){
			try {
				Object objfac = ctx.lookup(fac);
				if (objfac instanceof TopicConnectionFactory){
					factory = (TopicConnectionFactory)objfac;
				}
			} catch (NamingException e){
				log.error(e.getCause().toString());
			}
		}
		return factory;
	}

	/**
	 * 
	 * @param ctx
	 * @param fac
	 * @return
	 */
	public static synchronized QueueConnectionFactory getQueueConnectionFactory(
	Context ctx, String fac){
		while (qfactory == null){
			try {
				Object objfac = ctx.lookup(fac);
				if (objfac instanceof QueueConnectionFactory){
					qfactory = (QueueConnectionFactory)objfac;
				}
			} catch (NamingException e){
				log.error(e.getMessage());
			}
		}
		return qfactory;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized TopicConnection getTopicConnection(){
		if (factory != null){
			try {
				return factory.createTopicConnection();
			} catch (JMSException e){
				log.error(e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param ctx
	 * @param queueConn
	 * @return
	 */
	public static QueueConnection getQueueConnection(Context ctx, String queueConn){
		if (factory != null){
			try {
				return qfactory.createQueueConnection();
			} catch (JMSException e){
				log.error(e.getMessage());
			}
		}
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16383.java