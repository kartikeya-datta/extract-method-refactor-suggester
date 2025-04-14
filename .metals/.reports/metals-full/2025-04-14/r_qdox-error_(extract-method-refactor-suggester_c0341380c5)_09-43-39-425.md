error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8816.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8816.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8816.java
text:
```scala
o@@perationQueue = new Vector(10);

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.columba.core.command;

import java.util.Vector;

import org.columba.core.logging.ColumbaLogger;
import org.columba.core.util.Mutex;

public class DefaultProcessor extends Thread {

	private final static int MAX_WORKERS = 5;

	private Vector operationQueue;
	private Vector worker;

	private Mutex operationMutex;
	private Mutex workerMutex;

	private boolean isBusy;
	
	private UndoManager undoManager;
	
	private TaskManager taskManager;
	
	private int timeStamp;

	public DefaultProcessor() {
		operationQueue = new Vector();

		worker = new Vector();

		for (int i = 0; i < MAX_WORKERS; i++) {
			worker.add(new Worker(this));
		}
		
		isBusy = true;
		
		operationMutex = new Mutex();
		workerMutex = new Mutex();

		taskManager = new TaskManager();
		
		undoManager = new UndoManager( this );
		timeStamp = 0;
		
	}

	public void addOp( Command op ) {
		addOp( op, Command.FIRST_EXECUTION );	
	}

	synchronized void addOp(Command op, int operationMode) {		

		ColumbaLogger.log.debug( "Adding Operation..." );
		
		operationMutex.getMutex();

		int p = operationQueue.size() - 1;
		OperationItem nextOp;

		// Sort in with respect to priority

		while (p != -1) {
			nextOp = (OperationItem) operationQueue.get(p);
			if ((nextOp.operation.getPriority() < op.getPriority()) && !nextOp.operation.isSynchronize())
				p--;
			else
				break;
		}

		operationQueue.insertElementAt(new OperationItem(op,operationMode), p + 1);

		operationMutex.releaseMutex();

		ColumbaLogger.log.debug( "Operation added" );
		
		notify();
	}

	private boolean canBeProcessed(OperationItem opItem) {
		return opItem.operation.canBeProcessed(opItem.operationMode);
	}

	private OperationItem getNextOpItem() {
		OperationItem nextOp = null;

		operationMutex.getMutex();

		for (int i = 0; i < operationQueue.size(); i++) {
			nextOp = (OperationItem) operationQueue.get(i);
			if( ( i!=0 ) && (nextOp.operation.isSynchronize()) ) {
				nextOp = null;
				break;	
			}
			else if (canBeProcessed(nextOp)) {
				operationQueue.remove(i);
				break;
			} else {
				nextOp.operation.incPriority();
				if( nextOp.operation.getPriority() >= Command.DEFINETLY_NEXT_OPERATION_PRIORITY ) {					
					nextOp = null;
					break;
				} else {
					nextOp = null;					
				}
			}
		}
		operationMutex.releaseMutex();

		return nextOp;
	}


	public synchronized void operationFinished(Command op, Worker w) {
		
		workerMutex.getMutex();

		worker.add(w);

		workerMutex.releaseMutex();

		ColumbaLogger.log.debug( "Operation finished" );
		notify();
	}

	private Worker getWorker() {
		Worker result = null;

		workerMutex.getMutex();

		if (worker.size() > 0)
			result = (Worker) worker.remove(0);

		workerMutex.releaseMutex();

		return result;
	}

	private synchronized void waitForNotify() {
		isBusy = false;
		try {
			wait();
		} catch (InterruptedException e) {
		}
		isBusy = true;
		
		ColumbaLogger.log.debug( "Operator woke up" );
	}

	public void run() // Scheduler
	{
		OperationItem opItem = null;
		Worker worker = null;

		while (true) {
			while ((opItem = getNextOpItem()) == null)
				waitForNotify();

			ColumbaLogger.log.debug( "Processing new Operation" );

			while ((worker = getWorker()) == null)
				waitForNotify();

			ColumbaLogger.log.debug( "Found Worker for new Operation" );
			worker.process(opItem.operation, opItem.operationMode, timeStamp++);
			ColumbaLogger.log.debug( "Worker initilized" );
			worker.register(taskManager);
			ColumbaLogger.log.debug( "Starting Worker" );
			worker.start();
			
			ColumbaLogger.log.debug( "New Operation started..." );
		}
	}
	
	public boolean isBusy() {
		return isBusy();
	}
	
	public boolean hasFinishedCommands() {
		boolean result;
		workerMutex.getMutex();
		result = (worker.size() == MAX_WORKERS);
		workerMutex.releaseMutex();
		operationMutex.getMutex();
		result = result && (operationQueue.size() == 0);
		operationMutex.releaseMutex();
		
		return result;
	}
	/**
	 * Returns the undoManager.
	 * @return UndoManager
	 */
	public UndoManager getUndoManager() {
		return undoManager;
	}

	/**
	 * Returns the taskManager.
	 * @return TaskManager
	 */
	public TaskManager getTaskManager() {
		return taskManager;
	}

	/**
	 * Returns the operationQueue. This method is for testing only!
	 * @return Vector
	 */
	public Vector getOperationQueue() {
		return operationQueue;
	}

}

class OperationItem {
	public Command operation;
	public int operationMode;		
	
	public OperationItem( Command op, int opMode ) {
		operation = op;
		operationMode = opMode;	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8816.java