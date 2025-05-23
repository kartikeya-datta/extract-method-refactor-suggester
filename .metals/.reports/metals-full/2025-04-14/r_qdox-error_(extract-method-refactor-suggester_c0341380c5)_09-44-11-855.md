error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18324.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18324.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18324.java
text:
```scala
private static final L@@ogger log = LoggingManager.getLoggerForClass();

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

package org.apache.jmeter.protocol.jms.client;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class ReceiveSubscriber implements Runnable {

	private static Logger log = LoggingManager.getLoggerForClass();

	private TopicConnection CONN = null;

	private TopicSession SESSION = null;

	private Topic TOPIC = null;

	private TopicSubscriber SUBSCRIBER = null;

	private byte[] RESULT = null;

	private int counter;

	private int loop = 1; // TODO never read

	private StringBuffer buffer = new StringBuffer();

	private volatile boolean RUN = true;
    // Needs to be volatile to ensure value is picked up

	private Thread CLIENTTHREAD = null;

	/**
	 * 
	 */
	public ReceiveSubscriber() {
		super();
	}

	public ReceiveSubscriber(boolean useProps, String jndi, String url, String connfactory, String topic,
			String useAuth, String user, String pwd) {
		Context ctx = initJNDI(useProps, jndi, url, useAuth, user, pwd);
		if (ctx != null) {
			initConnection(ctx, connfactory, topic);
		} else {
			log.error("Could not initialize JNDI Initial Context Factory");
		}
	}

	/**
	 * Initialize the JNDI initial context
	 * 
	 * @param useProps
	 * @param jndi
	 * @param url
	 * @param useAuth
	 * @param user
	 * @param pwd
	 * @return  the JNDI initial context or null
	 */
	public Context initJNDI(boolean useProps, String jndi, String url, String useAuth, String user, String pwd) {
		if (useProps) {
			try {
				return new InitialContext();
			} catch (NamingException e) {
				log.error(e.getMessage());
				return null;
			}
		} else {
			return InitialContextFactory.lookupContext(jndi, url, useAuth, user, pwd);
		}
	}

	/**
	 * Create the connection, session and topic subscriber
	 * 
	 * @param ctx
	 * @param connfactory
	 * @param topic
	 */
	public void initConnection(Context ctx, String connfactory, String topic) {
		try {
			ConnectionFactory.getTopicConnectionFactory(ctx,connfactory);
			this.CONN = ConnectionFactory.getTopicConnection();
			this.TOPIC = InitialContextFactory.lookupTopic(ctx, topic);
			this.SESSION = this.CONN.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			this.SUBSCRIBER = this.SESSION.createSubscriber(this.TOPIC);
			log.info("created the topic connection successfully");
		} catch (JMSException e) {
			log.error("Connection error: " + e.getMessage());
		}
	}

	/**
	 * Set the number of iterations for each call to sample()
	 * 
	 * @param loop
	 */
	public void setLoop(int loop) {
		this.loop = loop;
	}

	/**
	 * Resume will call Connection.start() and begin receiving messages from the
	 * JMS provider.
	 */
	public void resume() {
		if (this.CONN == null) {
			log.error("Connection not set up");
			return;
		}
		try {
			this.CONN.start();
		} catch (JMSException e) {
			log.error("failed to start recieving");
		}
	}

	/**
	 * Get the message as a string
	 * 
	 */
	public String getMessage() {
		return this.buffer.toString();
	}

	/**
	 * Get the message(s) as an array of byte[]
	 * 
	 */
	public byte[] getByteResult() {
		if (this.buffer.length() > 0) {
			this.RESULT = this.buffer.toString().getBytes();
		}
		return this.RESULT;
	}

	/**
	 * close() will stop the connection first. Then it closes the subscriber,
	 * session and connection and sets them to null.
	 */
	public synchronized void close() {
		try {
			this.CONN.stop();
			this.SUBSCRIBER.close();
			this.SESSION.close();
			this.CONN.close();
			this.SUBSCRIBER = null;
			this.SESSION = null;
			this.CONN = null;
			this.RUN = false;
			this.CLIENTTHREAD.interrupt();
			this.CLIENTTHREAD = null;
			this.buffer.setLength(0);
			this.buffer = null;
		} catch (JMSException e) {
			log.error(e.getMessage());
		} catch (Throwable e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Clear will set the buffer to zero and the result objects to null. Clear
	 * should be called at the end of a sample.
	 */
	public void clear() {
		this.buffer.setLength(0);
		this.RESULT = null;
	}

	/**
	 * Increment the count and return the new value
	 * 
	 * @param increment
	 */
	public synchronized int count(int increment) {
		counter += increment;
		return counter;
	}

	/**
	 * Reset will reset the counter and prepare for the next sample() call.
	 * 
	 */
	public synchronized int resetCount() {
		counter = 0;
		return counter;
	}

	/**
	 * start will create a new thread and pass this class. once the thread is
	 * created, it calls Thread.start().
	 */
	public void start() {
		this.CLIENTTHREAD = new Thread(this, "Subscriber2");
		this.CLIENTTHREAD.start();
	}

	/**
	 * run calls listen to begin listening for inboud messages from the
	 * provider.
	 */
	public void run() {
		ReceiveSubscriber.this.listen();
	}

	/**
	 * Listen for inbound messages
	 */
	protected void listen() {
		log.info("Subscriber2.listen() called");
		while (RUN) {
			if (SUBSCRIBER == null) {
				log.error("Subscriber has not been set up");
				break;
			}
			try {
				Message message = this.SUBSCRIBER.receive();
				if (message != null && message instanceof TextMessage) {
					TextMessage msg = (TextMessage) message;
					if (msg.getText().trim().length() > 0) {
						this.buffer.append(msg.getText());
						count(1);
					}
				}
			} catch (JMSException e) {
				log.info("Communication error: " + e.getMessage());
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18324.java