error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5042.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5042.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5042.java
text:
```scala
s@@uccess &= references[i].tryToGetLock(this);

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.core.command;

import java.lang.reflect.Array;

import org.columba.core.gui.FrameController;
import org.columba.core.util.Lock;

/**
 * @author Timo Stich (tstich@users.sourceforge.net)
 * 
 */
public abstract class Command {

	// Command Types

	// Commands that can be undone, e.g. move message
	// line for constructor:
	// commandType = Command.UNDOABLE_OPERATION;
	public static final int UNDOABLE_OPERATION = 0;

	// Commands that can not be undone but previous commands
	// can be undone, e.g. view message (default) 
	// line for constructor:
	// commandType = Command.NORMAL_OPERATION;
	public static final int NORMAL_OPERATION = 1;

	// Commands that can not be undone and previous commands
	// cannot be undone anymore, e.g. delete message from trash
	// line for constructor:
	// commandType = Command.NO_UNDO_OPERATION;
	public static final int NO_UNDO_OPERATION = 2;

	// Priorities

	// Commands that are started by an automated process, e.g. auto-check
	// for new messages
	public static final int DAEMON_PRIORITY = -10;

	// Normal priority for e.g. copying (default)
	public static final int NORMAL_PRIORITY = 0;

	// Commands that the user waits for to finish, e.g. view message
	public static final int REALTIME_PRIORITY = 10;

	// Never Use this!! - internally highest priority
	public static final int DEFINETLY_NEXT_OPERATION_PRIORITY = 20;

	// Never use these!!! - for internal state control only

	public static final int FIRST_EXECUTION = 0;
	public static final int UNDO = 1;
	public static final int REDO = 2;

	protected int priority;
	protected int commandType;
	protected boolean synchronize;

	protected int timeStamp;

	protected Lock[] folderLocks;
	private DefaultCommandReference[] references;
	private DefaultCommandReference[] undoReferences;

	protected FrameController frameController;

	public Command(DefaultCommandReference[] references) {
		this.references = references;

		commandType = NORMAL_OPERATION;
		priority = NORMAL_PRIORITY;
	}

	public Command(
		FrameController frameController,
		DefaultCommandReference[] references) {
		this.references = references;
		this.frameController = frameController;

		commandType = NORMAL_OPERATION;
		priority = NORMAL_PRIORITY;
	}

	public void process(Worker worker, int operationMode) throws Exception {
		setTimeStamp(worker.getTimeStamp());

		switch (operationMode) {
			case FIRST_EXECUTION :
				execute(worker);
				break;
			case UNDO :
				undo(worker);
				break;
			case REDO :
				redo(worker);
				break;
		}
	}

	public void updateGUI() throws Exception {
	}

	/**
	 * Command must implement this method
	 * Executes the Command when run the first time
	 *
	 * @param worker
	 * @throws Exception
	 */
	public abstract void execute(Worker worker) throws Exception;

	/**
	 * Command must implement this method
	 * Undos the command after command was executed or redone.
	 * 
	 * @param worker
	 * @throws Exception
	 */
	public void undo(Worker worker) throws Exception {
	}

	/**
	 * Command must implement this method
	 * Redos the command after command was undone.
	 * 
	 * @param worker
	 * @throws Exception
	 */
	public void redo(Worker worker) throws Exception {
	}

	public boolean canBeProcessed(int operationMode) {
		DefaultCommandReference[] references = getReferences(operationMode);
		int size = Array.getLength(references);

		boolean success = true;

		for (int i = 0;(i < size) && success; i++) {
			success &= references[i].tryToGetLock();
		}

		if (!success) {
			releaseAllFolderLocks(operationMode);
		}

		return success;
	}

	public void releaseAllFolderLocks(int operationMode) {
		DefaultCommandReference[] references = getReferences(operationMode);
		int size = Array.getLength(references);

		for (int i = 0; i < size; i++) {
			references[i].releaseLock();
		}
	}

	/************* Methods for interacting with the Operator *************/

	public DefaultCommandReference[] getReferences(int operationMode) {
		if (operationMode == UNDO)
			return getUndoReferences();
		else
			return getReferences();
	}

	public int getCommandType() {
		return commandType;
	}

	public int getPriority() {
		return priority;
	}

	public void incPriority() {
		priority++;
	}

	public boolean isSynchronize() {
		return synchronize;
	}

	public void setSynchronize(boolean synchronize) {
		this.synchronize = synchronize;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Sets the undoReferences.
	 * @param undoReferences The undoReferences to set
	 */
	public void setUndoReferences(DefaultCommandReference[] undoReferences) {
		this.undoReferences = undoReferences;
	}

	/**
	 * Returns the timeStamp.
	 * @return int
	 */
	public int getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Returns the frameController.
	 * @return FrameController
	 */
	/*
	public FrameController getFrameController() {
		return frameController;
	}
	*/

	/**
	 * Sets the timeStamp.This method is for testing only!
	 * @param timeStamp The timeStamp to set
	 */
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Returns the references.
	 * @return DefaultCommandReference[]
	 */
	public DefaultCommandReference[] getReferences() {
		return references;
	}

	/**
	 * Returns the undoReferences.
	 * @return DefaultCommandReference[]
	 */
	public DefaultCommandReference[] getUndoReferences() {
		return undoReferences;
	}

	public void finish() throws Exception {
		updateGUI();
	}

	/**
	 * Returns the frameController.
	 * @return FrameController
	 */
	public FrameController getFrameController() {
		return frameController;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5042.java