error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3908.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3908.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3908.java
text:
```scala
private static C@@ommandProcessor instance = new CommandProcessor();

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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.columba.core.util.Mutex;

/**
 * Scheduler for background threads
 * <p>
 * DefaultProcessor keeps a pool of {@link Worker}instances, which are assigned
 * to {@link Command}, when executed.
 * 
 * @author tstich
 */
public class CommandProcessor implements Runnable {
	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.core.command");

	public final static int MAX_WORKERS = 5;

	List operationQueue;

	List worker;

	private Mutex oneMutex;

	private TaskManager taskManager;

	private int timeStamp;
	
	private static CommandProcessor instance;

	public CommandProcessor() {
		this(true);
	}
	
	public static CommandProcessor getInstance() {
		return instance;
	}

	/**
	 * Constructs a DefaultProcessor.
	 */
	public CommandProcessor(boolean start) {
		operationQueue = new ArrayList(10);

		worker = new ArrayList(MAX_WORKERS);

		// Create the workers
		for (int i = 0; i < MAX_WORKERS; i++) {
			worker.add(new Worker(this));
		}

		oneMutex = new Mutex();

		taskManager = new TaskManager();

		timeStamp = 0;

		if (start)
			new Thread(this).start();
	}

	/**
	 * Add a Command to the Queue. Calls {@link #addOp(Command, int)}with
	 * Command.FIRST_EXECUTION.
	 * 
	 * @param op
	 *            the command to add
	 */
	public void addOp(final Command op) {
		addOp(op, Command.FIRST_EXECUTION);
	}

	/**
	 * Adds a Command to the queue.
	 * 
	 * @param op
	 *            the command
	 * @param operationMode
	 *            the mode in wich the command should be processed
	 */
	public void addOp(final Command op, final int operationMode) {
		try {
			oneMutex.lock();

			int p = operationQueue.size() - 1;
			OperationItem nextOp;

			// Sort in with respect to priority and synchronize:
			// Commands with higher priority will be processed
			// before commands with lower priority.
			// If there is a command that is of type synchronize
			// don't put this command in front.
			while (p != -1) {
				nextOp = (OperationItem) operationQueue.get(p);

				if ((nextOp.getOperation().getPriority() < op.getPriority())
						&& !nextOp.getOperation().isSynchronize()) {
					p--;
				} else {
					break;
				}
			}

			operationQueue.add(p + 1, new OperationItem(op, operationMode));
		} finally {
			oneMutex.release();
		}

		wakeUp();
	}

	/**
	 * Checks if the command can be processed. This is true if all references
	 * are not blocked.
	 * 
	 * @param opItem
	 *            the internal command structure
	 * @return true if the operation will not be blocked
	 */
	private boolean canBeProcessed(final OperationItem opItem) {
		return opItem.getOperation().canBeProcessed();
	}

	/**
	 * Get the next Operation from the queue.
	 * 
	 * @return the next non-blocking operation or null if none found.
	 */
	private OperationItem nextOpItem() {
		OperationItem nextOp = null;
		boolean needToRelease = false;

		for (int i = 0; i < operationQueue.size() && nextOp == null; i++) {
			nextOp = (OperationItem) operationQueue.get(i);

			if ((i != 0) && (nextOp.getOperation().isSynchronize())) {
				nextOp = null;

				// We have to process this command first
				// -> break here!
				break;
			} else {
				try {
					if (!canBeProcessed(nextOp)) {
						nextOp = null;
					}
				} catch (RuntimeException e) {
					// Remove bogus Operation
					operationQueue.remove(nextOp);
					nextOp = null;

					LOG.warning("Operation failed: " + e.getMessage());
				}
			}
		}

		return nextOp;
	}

	/**
	 * Called by the worker to signal that his operation has finished.
	 * 
	 * @param op
	 *            the command the worker has processed
	 * @param w
	 *            the worker himself
	 */
	public void operationFinished(final Command op, final Worker w) {
		boolean needToRelease = false;

		try {
			oneMutex.lock();

			worker.add(w);
		} finally {
			oneMutex.release();
		}

		// notify that a new worker is available
		wakeUp();
	}

	/**
	 * Get an available Worker from the workerpool. Reserve one worker for
	 * Real-Time Priority tasks
	 * 
	 * @param priority
	 * 
	 * @return an available worker or null if none available.
	 */
	Worker getWorker(int priority) {
		Worker result = null;
		if (worker.size() > 1) {
			result = (Worker) worker.remove(0);
		} else if (worker.size() > 0 && priority >= Command.REALTIME_PRIORITY) {
			result = (Worker) worker.remove(0);
		}

		return result;
	}

	/**
	 * Wait until a worker is available or a new command is added.
	 */
	private synchronized void waitForNotify() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized void wakeUp() {
		notifyAll();
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		OperationItem opItem = null;
		Worker worker = null;
		boolean sleep;

		while (true) {
			sleep = startOperation();

			if (sleep) {
				waitForNotify();
				sleep = false;
			}
		}

	}

	/**
	 * @param sleep
	 * @return
	 */
	boolean startOperation() {
		boolean sleep = false;
		try {
			oneMutex.lock();
			OperationItem opItem;
			Worker worker;
			opItem = nextOpItem();
			if (opItem != null) {
				worker = getWorker(opItem.getOperation().getPriority());
				if (worker != null) {
					operationQueue.remove(opItem);

					worker.process(opItem.getOperation(), opItem
							.getOperationMode(), timeStamp++);

					worker.register(taskManager);

					worker.start();
				} else {
					sleep = true;
				}
			} else {
				sleep = true;
			}
		} finally {
			oneMutex.release();
		}
		return sleep;
	}

	/**
	 * Returns the taskManager.
	 * 
	 * @return TaskManager
	 */
	public TaskManager getTaskManager() {
		return taskManager;
	}
}

/**
 * Intern represenation of the Commands.
 * 
 * @author Timo Stich <tstich@users.sourceforge.net>
 */

class OperationItem {
	private Command operation;

	private int operationMode;

	public OperationItem(Command op, int opMode) {
		operation = op;
		operationMode = opMode;
	}

	public Command getOperation() {
		return operation;
	}

	public int getOperationMode() {
		return operationMode;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3908.java