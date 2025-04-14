error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1461.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1461.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1461.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.engine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterThreadMonitor;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.threads.TestCompiler;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.ListedHashTree;
import org.apache.jmeter.util.SearchByClass;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/************************************************************
 *  !ToDo (Class description)
 *
 *@author     $Author$
 *@created    $Date$
 *@version    $Revision$
 ***********************************************************/
public class StandardJMeterEngine implements JMeterEngine,JMeterThreadMonitor,Runnable
{
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.engine");
	private static long WAIT_TO_DIE = 5 * 1000; //5 seconds
	Map allThreads;
	boolean running = false;
	ListedHashTree test;
	SearchByClass testListeners;
	String host = null;
	ListenerNotifier notifier;

	/************************************************************
	 *  !ToDo (Constructor description)
	 ***********************************************************/
	public StandardJMeterEngine()
	{
		allThreads = new HashMap();
	}

	public StandardJMeterEngine(String host)
	{
		this();
		this.host = host;
	}

	public void configure(ListedHashTree testTree)
	{
		test = testTree;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}

	protected ListedHashTree getTestTree()
	{
		return test;
	}
	
	protected void compileTree()
	{
		PreCompiler compiler = new PreCompiler();
		getTestTree().traverse(compiler);
	}

	/************************************************************
	 *  !ToDo (Method description)
	 ***********************************************************/
	public void runTest() throws JMeterEngineException
	{
		try
		{
			log.info("Running the test!");
			running = true;
			compileTree();
			List testLevelElements = new LinkedList(getTestTree().list(getTestTree().getArray()[0]));
			removeThreadGroups(testLevelElements);
			SearchByClass searcher = new SearchByClass(ThreadGroup.class);
			testListeners = new SearchByClass(TestListener.class);
			setMode();
			getTestTree().traverse(testListeners);
			getTestTree().traverse(searcher);
			TestCompiler.initialize();
			//for each thread group, generate threads
			// hand each thread the sampler controller
			// and the listeners, and the timer
			JMeterThread[] threads;
			Iterator iter = searcher.getSearchResults().iterator();
			if(iter.hasNext())
			{
				notifyTestListenersOfStart();
			}
			notifier = new ListenerNotifier();
			notifier.start();
			while(iter.hasNext())
			{
				ThreadGroup group = (ThreadGroup)iter.next();
				threads = new JMeterThread[group.getNumThreads()];
				for(int i = 0;running && i < threads.length; i++)
				{
					ListedHashTree threadGroupTree = searcher.getSubTree(group);
					threadGroupTree.add(group,testLevelElements);
					threads[i] = new JMeterThread(cloneTree(threadGroupTree),this,notifier);
					threads[i].setInitialDelay((int)(((float)(group.getRampUp() * 1000) /
							(float)group.getNumThreads()) * (float)i));
					threads[i].setThreadName(group.getName()+"-"+(i+1));
					Thread newThread = new Thread(threads[i]);
					newThread.setName(group.getName()+"-"+(i+1));
					allThreads.put(threads[i],newThread);
					newThread.start();
				}
			}
		}
		catch(Exception err)
		{
			stopTest();
			StringWriter string = new StringWriter();
			PrintWriter writer = new PrintWriter(string);
			err.printStackTrace(writer);
			throw new JMeterEngineException(string.toString());
		}
	}
	
	private void removeThreadGroups(List elements)
	{
		Iterator iter = elements.iterator();
		while(iter.hasNext())
		{
			Object item = iter.next();
			if(item instanceof ThreadGroup)
			{
				iter.remove();
			}
			else if(!(item instanceof TestElement))
			{
				iter.remove();
			}
		}
	}
	
	protected void setMode()
	{
		SearchByClass testPlan = new SearchByClass(TestPlan.class);
		getTestTree().traverse(testPlan);
		Object[] plan = testPlan.getSearchResults().toArray();
		ResultCollector.enableFunctionalMode(((TestPlan)plan[0]).isFunctionalMode());
	}

	protected void notifyTestListenersOfStart()
	{
		Iterator iter = testListeners.getSearchResults().iterator();
		while(iter.hasNext())
		{
			if(host == null)
			{
				((TestListener)iter.next()).testStarted();
			}
			else
			{
				((TestListener)iter.next()).testStarted(host);
			}
		}
	}


	protected void notifyTestListenersOfEnd()
	{
		notifier.stop();
		Iterator iter = testListeners.getSearchResults().iterator();
		while(!notifier.isStopped())
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
			log.debug("Waiting for notifier thread to stop");
		}
		while(iter.hasNext())
		{
			if(host == null)
			{
				((TestListener)iter.next()).testEnded();
			}
			else
			{
				((TestListener)iter.next()).testEnded(host);
			}
		}
	}

	private ListedHashTree cloneTree(ListedHashTree tree)
	{
		TreeCloner cloner = new TreeCloner();
		tree.traverse(cloner);
		return cloner.getClonedTree();
	}

	/************************************************************
	 *  !ToDo (Method description)
	 ***********************************************************/
	public void reset()
	{
		if(running)
		{
			stopTest();
			running = false;
		}
	}

	public synchronized void threadFinished(JMeterThread thread)
	{
		allThreads.remove(thread);
		if(allThreads.size() == 0)
		{
			stopTest();
		}
		/*if(allThreads.size() == 0)
		{
			notifyTestListenersOfEnd();
		}*/
	}

	/************************************************************
	 *  !ToDo (Method description)
	 ***********************************************************/
	public synchronized void stopTest()
	{
		if(running)
		{
			running = false;		
			Thread stopThread = new Thread(this);
			stopThread.start();
		}
	}
	
	public void run()
	{
		tellThreadsToStop();
		try
		{
			Thread.sleep(10 * allThreads.size());
		}
		catch (InterruptedException e)
		{
		}
		verifyThreadsStopped();
		notifyTestListenersOfEnd();	
	}

	private void verifyThreadsStopped()
	{
		Iterator iter = new HashSet(allThreads.keySet()).iterator();
		while(iter.hasNext())
		{
			Thread t = (Thread)allThreads.get(iter.next());
			if(t != null && t.isAlive())
			{
				try
				{
					t.join(WAIT_TO_DIE);
				}
				catch (InterruptedException e)
				{
				}
				if(t.isAlive())
				{
					log.info("Thread won't die: "+t.getName());
				}
			}
			log.debug("finished thread");
		}
	}

	private void tellThreadsToStop()
	{
		Iterator iter = new HashSet(allThreads.keySet()).iterator();
		while(iter.hasNext())
		{
			JMeterThread item = (JMeterThread)iter.next();
			item.stop();
			Thread t = (Thread)allThreads.get(item);
			if(t != null)
			{
				t.interrupt();
			}
			else
			{
				log.warn("Lost thread: "+item.getThreadName());
				allThreads.remove(item);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1461.java