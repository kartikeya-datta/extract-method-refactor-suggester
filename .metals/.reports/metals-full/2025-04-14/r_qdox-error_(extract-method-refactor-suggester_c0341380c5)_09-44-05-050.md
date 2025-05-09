error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/910.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/910.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/910.java
text:
```scala
e@@xecutor = new PooledExecutor(poolSize);

/*
 * Copyright (c) 2000 Peter Antman Tim <peter.antman@tim.se>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jboss.jms.asf;

import java.util.Vector;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.ServerSession;
import javax.jms.ServerSessionPool;
import javax.jms.MessageListener;
import javax.jms.TopicConnection;
import javax.jms.XATopicConnection;
import javax.jms.QueueConnection;
import javax.jms.XAQueueConnection;
import javax.jms.Session;
import javax.jms.XASession;
import javax.jms.XAQueueSession;
import javax.jms.XATopicSession;

import org.jboss.logging.Logger;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;
import EDU.oswego.cs.dl.util.concurrent.BoundedBuffer;

/**
 * StdServerSessionPool.java
 *
 *
 * Created: Thu Dec  7 17:02:03 2000
 *
 * @author 
 * @version
 */

public class StdServerSessionPool implements ServerSessionPool {
	private static final int DEFAULT_POOL_SIZE = 15;
	private int poolSize = DEFAULT_POOL_SIZE;
	private int ack;
	private boolean transacted;
	private MessageListener listener;
	private Connection con;
    

	private Vector sessionPool = new Vector();
    
	boolean isTransacted() {
	return transacted;
	}
    
    
	/**
	 * Minimal constructor, could also have stuff for pool size
	 */
	public StdServerSessionPool(Connection con, boolean transacted, int ack, MessageListener listener) throws JMSException{
	this(con,transacted,ack,listener,DEFAULT_POOL_SIZE);
	}
	public StdServerSessionPool(Connection con, boolean transacted, int ack, MessageListener listener, int maxSession) throws JMSException {
	    this.con= con;
	    this.ack= ack;
	    this.listener= listener;
	    this.transacted= transacted;
	    this.poolSize= maxSession;


		threadGroup = new ThreadGroup("ASF Session Pool Threads");
		executor = new PooledExecutor(new BoundedBuffer(10), poolSize);
		executor.setMinimumPoolSize(0);
		executor.setKeepAliveTime(1000*30);
		executor.waitWhenBlocked();
		executor.setThreadFactory( new ThreadFactory() {
				public Thread newThread(Runnable command) {
					return new Thread( threadGroup, command, "Thread Pool Worker");
				}
 			}
		);

	    
	    init();
	    Logger.debug("Server Session pool set up");
	}
	// --- JMS API for ServerSessionPool

	// implementation of ServerSessionPool.getServerSession
	public ServerSession getServerSession() 
		throws JMSException {
		ServerSession result = null;
	Logger.debug("Leaving out a server session");
		try {
			for (;;) {
		if (sessionPool.size() > 0) {
					result = (ServerSession)sessionPool.remove(0);
					break;
				}
				else {
					try {
						synchronized (sessionPool) {
							sessionPool.wait();
						}
					} catch (InterruptedException exception){
						// ignore the error
					}
				}
			}
		}
		catch (Exception exception) {
			throw new JMSException("Error in getServerSession " + exception); 
		}
		return result;
	}

	// --- Protected messages for StdServerSession to use

	void recycle(StdServerSession session){
		synchronized (sessionPool){
	    sessionPool.addElement(session);
	    sessionPool.notifyAll();
	}
	}
    

    
	/**
	 * Clear the pool, clear out both threads and ServerSessions,
	 * connection.stop() should be run before this method.
	 */
	public void clear() {
	synchronized (sessionPool){
	    // FIXME - is there a runaway condition here. What if a 
	    // ServerSession are taken by a ConnecionConsumer? Should we set 
	    // a flag somehow so that no
	    // ServerSessions are recycled and the ThreadPool don't leve any
	    // more threads out.
	    Logger.debug("Clearing " + sessionPool.size() + " from ServerSessionPool");
			for(Enumeration e = sessionPool.elements() ; e.hasMoreElements() ; ){
		StdServerSession ses = (StdServerSession)e.nextElement();
		// Should we do any thing to the server session?
		ses.close();
	    }
	    sessionPool.clear();
	    executor.shutdownAfterProcessingCurrentlyQueuedTasks();
	    sessionPool.notifyAll();
	}
	}

	// --- Private methods used internally
	
	private void init() throws JMSException{
	for (int index = 0; index < poolSize; index++){
		try {
		    // Here is the meat, that MUST follow the spec
		    Session ses = null;
		    XASession xaSes = null;

		    if (con instanceof XATopicConnection) {
				xaSes = ((XATopicConnection)con).createXATopicSession();
				ses = ((XATopicSession)xaSes).getTopicSession();
		    } else if(con instanceof XAQueueConnection) {
				xaSes = ((XAQueueConnection)con).createXAQueueSession();
				ses = ((XAQueueSession)xaSes).getQueueSession();
		    } else if (con instanceof TopicConnection) {
				ses = ((TopicConnection)con).createTopicSession(transacted, ack);
				Logger.error("WARNING: Using a non-XA TopicConnection.  It will not be able to participate in a Global UOW");
		    } else if(con instanceof QueueConnection) {
				ses = ((QueueConnection)con).createQueueSession(transacted, ack);
				Logger.error("WARNING: Using a non-XA QueueConnection.  It will not be able to participate in a Global UOW");
		    } else {
				Logger.debug("Error in getting session for con: " + con);
				throw new JMSException("Connection was not reconizable: " + con);
		    }
		    
		    // This might not be totala spec compliant since it
		    // says that app server should create as many
		    // message listeners its needs, 
		    Logger.debug("Setting listener for session");
		    ses.setMessageListener(listener);
		    sessionPool.addElement(
					   new StdServerSession(this, ses, xaSes)
					       );   
		}
				catch (JMSException exception){
		    Logger.log("DEBUG Error in adding to pool: " + exception+ " Pool: " + this + " listener: " + listener);
		}
			} 
	}
    
	private PooledExecutor executor;
	private ThreadGroup threadGroup;

	Executor getExecutor() {
		return executor;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/910.java