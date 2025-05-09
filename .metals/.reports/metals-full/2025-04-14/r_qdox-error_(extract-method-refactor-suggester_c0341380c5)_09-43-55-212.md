error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16957.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16957.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16957.java
text:
```scala
t@@hreads[i].setThreadName(groupName + " " + (groupCount) + "-" + (i + 1));

/*
 * Copyright 2000-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *  
 */

package org.apache.jmeter.engine;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterThreadMonitor;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.threads.TestCompiler;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.collections.SearchByClass;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 */
public class StandardJMeterEngine implements JMeterEngine, JMeterThreadMonitor,
      Runnable, Serializable
{
   transient private static Logger log = LoggingManager.getLoggerForClass();
   private transient Thread runningThread;
   private static long WAIT_TO_DIE = 5 * 1000; //5 seconds
   private transient Map allThreads;
   private boolean running = false;
   private boolean serialized = false;
   private volatile boolean schcdule_run = false;
   private HashTree test;
   private transient SearchByClass testListeners;
   private String host = null;
   private transient ListenerNotifier notifier;

    // Allow engine and threads to be stopped from outside a thread
    // e.g. from beanshell server
    // Assumes that there is only one instance of the engine
    // at any one time so it is not guaranteed to work ...
    private static transient Map allThreadNames;
    private static StandardJMeterEngine engine;
	private static Map allThreadsSave;
    public static void stopEngineNow()
    {
    	  if (engine != null) // May be null if called from Unit test
    	  engine.stopTest(true);
    }
    public static void stopEngine()
    {
    	  if (engine != null)  // May be null if called from Unit test
    	  	engine.stopTest(false);
    }
    
    /*
     * Allow functions etc to register for testStopped notification
     */
    private static List testList = null;
    public static synchronized void register(TestListener tl)
    {
    	testList.add(tl);
    }
    
    public static boolean stopThread(String threadName)
    {
    	return stopThread(threadName,false);
    }
    public static boolean stopThreadNow(String threadName)
    {
    	return stopThread(threadName,true);
    }
    private static boolean stopThread(String threadName, boolean now)
    {
    	if (allThreadNames == null) return false;// e.g. not yet started
    	JMeterThread thrd;
		try {
    	    thrd = (JMeterThread)allThreadNames.get(threadName);
    	} catch (Exception e) {
    		log.warn("stopThread: "+e);
    		return false;
    	}
    	if (thrd!= null)
    	{
    		thrd.stop();
    		if (now)
    		{
    		    Thread t = (Thread) allThreadsSave.get(thrd);
    		    if (t != null)
    		    {
    		        t.interrupt();
    		    }
    			
    		}
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    // End of code to allow engine to be controlled remotely
    
   public StandardJMeterEngine()
   {
      allThreads = new HashMap();
        engine=this;
        allThreadNames = new HashMap();
        allThreadsSave = allThreads;
   }

   public StandardJMeterEngine(String host)
   {
      this();
      this.host = host;
   }

   public void configure(HashTree testTree)
   {
      test = testTree;
   }

   public void setHost(String host)
   {
      this.host = host;
   }

   protected HashTree getTestTree()
   {
      return test;
   }

   protected void compileTree()
   {
      PreCompiler compiler = new PreCompiler();
      getTestTree().traverse(compiler);
   }

   public void runTest() throws JMeterEngineException
   {
      try
      {
         runningThread = new Thread(this);
         runningThread.start();
      }
      catch (Exception err)
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
      while (iter.hasNext())
      {
         Object item = iter.next();
         if (item instanceof ThreadGroup)
         {
            iter.remove();
         }
         else if (!(item instanceof TestElement))
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
      ResultCollector.enableFunctionalMode(((TestPlan) plan[0])
            .isFunctionalMode());
   }

   protected void notifyTestListenersOfStart()
   {
      Iterator iter = testListeners.getSearchResults().iterator();
      while (iter.hasNext())
      {
         TestListener tl = (TestListener) iter.next();
         if(tl instanceof TestBean)TestBeanHelper.prepare((TestElement)tl);
         if (host == null)
         {
            tl.testStarted();
         }
         else
         {
            tl.testStarted(host);
         }
      }
   }

   protected void notifyTestListenersOfEnd()
   {
      Iterator iter = testListeners.getSearchResults().iterator();
      while (iter.hasNext())
      {
         TestListener tl = (TestListener) iter.next();
         if(tl instanceof TestBean)TestBeanHelper.prepare((TestElement)tl);
         if (host == null)
         {
            tl.testEnded();
         }
         else
         {
            tl.testEnded(host);
         }
      }
      log.info("Test has ended");
   }

   private ListedHashTree cloneTree(ListedHashTree tree)
   {
      TreeCloner cloner = new TreeCloner(true);
      tree.traverse(cloner);
      return cloner.getClonedTree();
   }

   public void reset()
   {
      if (running)
      {
         stopTest();
      }
   }

   public synchronized void threadFinished(JMeterThread thread)
   {
      allThreads.remove(thread);
      log.info("Ending thread " + thread.getThreadNum());
      if (!serialized && allThreads.size() == 0 && !schcdule_run)
      {
         log.info("Stopping test");
         stopTest();
      }
   }

   public synchronized void stopTest()
   {
      Thread stopThread = new Thread(new StopTest());
      stopThread.start();
   }
   
   public synchronized void stopTest(boolean b)
    {
        Thread stopThread = new Thread(new StopTest(b));
        stopThread.start();
    }

   private class StopTest implements Runnable
    {
    	boolean now;
    	private StopTest(){
    		now=true;
    	}
    	private StopTest(boolean b){
    		now=b;
    	}
        public void run()
        {
            if (running)
            {
                running = false;
                if (now){
                	tellThreadsToStop();
                } else {
                	stopAllThreads();
                }
                try
                {
                    Thread.sleep(10 * allThreads.size());
                }
                catch (InterruptedException e)
                {}
                boolean stopped=verifyThreadsStopped();
                if (stopped || now){
                    notifyTestListenersOfEnd();
                }
            }
        }
    }

   public void run()
   {
      log.info("Running the test!");
      running = true;
      testList = new ArrayList();

      SearchByClass testPlan = new SearchByClass(TestPlan.class);
      getTestTree().traverse(testPlan);
      Object[] plan = testPlan.getSearchResults().toArray();
      if (plan.length == 0)
      {
         System.err.println("Could not find the TestPlan!");
         log.error("Could not find the TestPlan!");
         System.exit(1);
      }
      if (((TestPlan) plan[0]).isSerialized())
      {
         serialized = true;
      }
      JMeterContextService.startTest();
      compileTree();
      /** 
       * Notification of test listeners needs to happen after function replacement, but before
       * setting RunningVersion to true.
       */
      testListeners = new SearchByClass(TestListener.class);
      getTestTree().traverse(testListeners);
      Collection col = testListeners.getSearchResults();
      col.addAll(testList);
      testList=null;
      notifyTestListenersOfStart();
      getTestTree().traverse(new TurnElementsOn());
      
      List testLevelElements = new LinkedList(getTestTree().list(
            getTestTree().getArray()[0]));
      removeThreadGroups(testLevelElements);
      SearchByClass searcher = new SearchByClass(ThreadGroup.class);
      setMode();
      getTestTree().traverse(searcher);
      TestCompiler.initialize();
      //for each thread group, generate threads
      // hand each thread the sampler controller
      // and the listeners, and the timer
      JMeterThread[] threads;
      Iterator iter = searcher.getSearchResults().iterator();

      /*
       * Here's where the test really starts. Run a Full GC now: it's no harm
       * at all (just delays test start by a tiny amount) and hitting one too
       * early in the test can impair results for short tests.
       */
      System.gc();

      notifier = new ListenerNotifier();

      schcdule_run = true;
      JMeterContextService.getContext().setSamplingStarted(true);
      int groupCount = 0;
      while (iter.hasNext())
      {
         groupCount++;
         ThreadGroup group = (ThreadGroup) iter.next();
         int numThreads = group.getNumThreads();
         boolean onErrorStopTest = group.getOnErrorStopTest();
         boolean onErrorStopThread = group.getOnErrorStopThread();
         String groupName = group.getName();
         int rampUp = group.getRampUp();
         float perThreadDelay = ((float) (rampUp * 1000) / (float) numThreads);
         threads = new JMeterThread[numThreads];

         log.info("Starting " + numThreads + " threads for group " + groupName
               + ". Ramp up = " + rampUp + ".");

         if (onErrorStopTest)
         {
            log.info("Test will stop on error");
         }
         else if (onErrorStopThread)
         {
            log.info("Thread will stop on error");
         }
         else
         {
            log.info("Continue on error");
         }

         for (int i = 0; running && i < threads.length; i++)
         {
            ListedHashTree threadGroupTree = (ListedHashTree) searcher
                  .getSubTree(group);
            threadGroupTree.add(group, testLevelElements);
            threads[i] = new JMeterThread(cloneTree(threadGroupTree), this,
                  notifier);
            threads[i].setThreadNum(i);
            threads[i].setInitialContext(JMeterContextService.getContext());
            threads[i].setInitialDelay((int) (perThreadDelay * (float) i));
            threads[i].setThreadName(groupName + (groupCount) + "-" + (i + 1));

            scheduleThread(threads[i], group);

            // Set up variables for stop handling
            threads[i].setEngine(this);
            threads[i].setOnErrorStopTest(onErrorStopTest);
            threads[i].setOnErrorStopThread(onErrorStopThread);

            Thread newThread = new Thread(threads[i]);
            newThread.setName(threads[i].getThreadName());
            allThreads.put(threads[i], newThread);
            if (serialized && !iter.hasNext() && i == threads.length - 1) //last
            // thread
            {
               serialized = false;
            }
            newThread.start();
         }
         schcdule_run = false;
         if (serialized)
         {
            while (running && allThreads.size() > 0)
            {
               try
               {
                  Thread.sleep(1000);
               }
               catch (InterruptedException e)
               {
               }
            }
         }
      }
   }

   /**
    * This will schedule the time for the JMeterThread.
    * 
    * @param thread
    * @param group
    */
   private void scheduleThread(JMeterThread thread, ThreadGroup group)
   {
      //if true the Scheduler is enabled
      if (group.getScheduler())
      {
         long now = System.currentTimeMillis();
			//set the start time for the Thread
        	if (group.getDelay() > 0 ){// Duration is  in seconds
				thread.setStartTime(group.getDelay()*1000+now);
        	} else {
        		long start = group.getStartTime();
        		if (start < now) start = now; // Force a sensible start time
				thread.setStartTime(start);
        	}

         //set the endtime for the Thread
            if (group.getDuration() > 0){// Duration is  in seconds
				thread.setEndTime(group.getDuration()*1000+(thread.getStartTime()));
            } else {
				thread.setEndTime(group.getEndTime());
            }

         //Enables the scheduler
         thread.setScheduled(true);
      }
   }

   public synchronized void pauseTest(int milis)
   {
      Iterator iter = new HashSet(allThreads.keySet()).iterator();
      while (iter.hasNext())
      {
         Thread t = (Thread) allThreads.get(iter.next());
         if (t != null && t.isAlive())
         {
            try
            {
               Thread.sleep(milis);
            }
            catch (InterruptedException e)
            {
            }
         }
      }
   }

   private boolean verifyThreadsStopped()
    {
    	boolean stoppedAll=true;
        Iterator iter = new HashSet(allThreads.keySet()).iterator();
        while (iter.hasNext())
        {
            Thread t = (Thread) allThreads.get(iter.next());
            if (t != null && t.isAlive())
            {
                try
                {
                    t.join(WAIT_TO_DIE);
                }
                catch (InterruptedException e)
                {}
                if (t.isAlive())
                {
                	stoppedAll=false;
                    log.info("Thread won't die: " + t.getName());
                }
            }
        }
        return stoppedAll;
    }

   private void tellThreadsToStop()
   {
      Iterator iter = new HashSet(allThreads.keySet()).iterator();
      while (iter.hasNext())
      {
         JMeterThread item = (JMeterThread) iter.next();
         item.stop();
         Thread t = (Thread) allThreads.get(item);
         if (t != null)
         {
            t.interrupt();
         }
         else
         {
            log.warn("Lost thread: " + item.getThreadName());
            allThreads.remove(item);
         }
      }
   }

   public void askThreadsToStop()
    {
    	engine.stopTest(false);
    }
    
    private void stopAllThreads()
	{
		Iterator iter = new HashSet(allThreads.keySet()).iterator();
		while (iter.hasNext())
		{
			JMeterThread item = (JMeterThread) iter.next();
			item.stop();
		}
	}

   // Remote exit
   public void exit()
   {
      // Needs to be run in a separate thread to allow RMI call to return OK
      Thread t = new Thread()
      {
         public void run()
         {
            //log.info("Pausing");
            try
            {
               Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
            }
            log.info("Bye");
            System.exit(0);
         }
      };
      log.info("Starting Closedown");
      t.start();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16957.java