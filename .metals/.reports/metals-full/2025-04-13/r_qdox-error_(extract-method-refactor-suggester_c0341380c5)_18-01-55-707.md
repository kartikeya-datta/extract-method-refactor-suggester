error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15462.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15462.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15462.java
text:
```scala
i@@f (getSharedObject().getConnectedID() != null) {

/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.core.sharedobject;

import java.util.*;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.sharedobject.events.*;
import org.eclipse.ecf.core.util.*;
import org.eclipse.ecf.internal.core.sharedobject.*;

/**
 * Implementation of two-phase commit for transactional replication of shared
 * objects.
 * 
 * @see ISharedObjectTransactionConfig
 * @see ISharedObjectTransactionParticipantsFilter
 * 
 */
public class TwoPhaseCommitEventProcessor implements IEventProcessor, ISharedObjectContainerTransaction {
	BaseSharedObject sharedObject = null;

	byte transactionState = ISharedObjectContainerTransaction.ACTIVE;

	Object lock = new Object();

	List participants = new Vector();

	Map failed = new HashMap();

	int timeout = ISharedObjectTransactionConfig.DEFAULT_TIMEOUT;

	int minFailedToAbort = 0;

	long identifier = 0;

	ISharedObjectTransactionParticipantsFilter participantsFilter = null;

	public TwoPhaseCommitEventProcessor(BaseSharedObject bse, ISharedObjectTransactionConfig config) {
		this.sharedObject = bse;
		if (config == null) {
			config = new TransactionSharedObjectConfiguration();
		}
		this.timeout = config.getTimeout();
		this.participantsFilter = config.getParticipantsFilter();
	}

	protected void trace(String msg) {
		Trace.trace(Activator.PLUGIN_ID, msg);
	}

	protected void traceStack(String msg, Throwable t) {
		Trace.catching(Activator.PLUGIN_ID, SharedObjectDebugOptions.EXCEPTIONS_CATCHING, TwoPhaseCommitEventProcessor.class, "traceStack", t); //$NON-NLS-1$
	}

	protected int getTimeout() {
		return timeout;
	}

	protected int getMinFailedToAbort() {
		return minFailedToAbort;
	}

	protected boolean isPrimary() {
		return getSharedObject().isPrimary();
	}

	protected BaseSharedObject getSharedObject() {
		return sharedObject;
	}

	protected ID getHomeID() {
		return getSharedObject().getHomeContainerID();
	}

	protected void addParticipants(ID[] ids) {
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				trace("addParticipant(" + ids[i] + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				if (!getHomeID().equals(ids[i]))
					participants.add(ids[i]);
			}
		}
	}

	protected void removeParticipant(ID id) {
		if (id != null) {
			trace("removeParticipant(" + id + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			participants.remove(id);
		}
	}

	protected void addFailed(ID remote, Throwable failure) {
		if (remote != null && failure != null) {
			trace("addFailed(" + remote + "," + failure + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failed.put(remote, failure);
		}
	}

	protected ISharedObjectContext getContext() {
		return getSharedObject().getContext();
	}

	/*
	 * Implementation of IEventProcessor. These methods are entry point methods
	 * for BaseSharedObject method dispatch to call
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.util.IEventProcessor#processEvent(org.eclipse.ecf.core.util.Event)
	 */
	public boolean processEvent(Event event) {
		if (event instanceof ISharedObjectActivatedEvent) {
			handleActivated((ISharedObjectActivatedEvent) event);
		} else if (event instanceof IContainerConnectedEvent) {
			handleJoined((IContainerConnectedEvent) event);
		} else if (event instanceof ISharedObjectCreateResponseEvent) {
			handleCreateResponse((ISharedObjectCreateResponseEvent) event);
		} else if (event instanceof IContainerDisconnectedEvent) {
			handleDeparted((IContainerDisconnectedEvent) event);
		} else if (event instanceof ISharedObjectMessageEvent) {
			ISharedObjectMessageEvent some = (ISharedObjectMessageEvent) event;
			Object data = some.getData();
			if (data instanceof ISharedObjectCommitEvent)
				localCommitted();
		}
		// Let other event processors have a shot at this event
		return false;
	}

	protected void handleActivated(ISharedObjectActivatedEvent event) {
		trace("handleActivated(" + event + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		// No other state changes while this is going on
		synchronized (lock) {
			if (isPrimary()) {
				// Primary
				handlePrimaryActivated(event);
			} else {
				handleReplicaActivated(event);
			}
			// Notify any threads waiting on state change
			lock.notifyAll();
		}
	}

	protected void replicateTo(ID[] remotes) {
		getSharedObject().replicateToRemoteContainers(remotes);
	}

	protected void handlePrimaryActivated(ISharedObjectActivatedEvent event) {
		trace("handlePrimaryActivated(" + event + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		// First get current group membership
		if (getContext().getConnectedID() != null) {
			ID[] groupMembers = getContext().getGroupMemberIDs();
			// Now get participants
			ID[] transactionParticipants = null;
			// If there is a participants filter specified then use it and ask
			// it to return an ID [] of participants (given
			// the current group membership
			if (participantsFilter != null) {
				transactionParticipants = participantsFilter.filterParticipants(groupMembers);
			}
			// replicate
			if (transactionParticipants == null) {
				// This means that all current group members should be included
				// as participants
				replicateTo(null);
				transactionParticipants = groupMembers;
			} else {
				// This means the participants filter provided us with an ID []
				// and so we replicate only to that ID []
				replicateTo(transactionParticipants);
			}

			// Add participants to the collection
			addParticipants(transactionParticipants);
			// Now set transaction state to VOTING
			setTransactionState(ISharedObjectContainerTransaction.VOTING);
		} else {
			setTransactionState(ISharedObjectContainerTransaction.COMMITTED);
		}
	}

	private long getNextIdentifier() {
		return identifier++;
	}

	protected void handleReplicaActivated(ISharedObjectActivatedEvent event) {
		trace("handleReplicaActivated(" + event + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			// Try to respond with create success message back to host
			getContext().sendCreateResponse(getHomeID(), null, getNextIdentifier());
			// If above succeeds, we're now in prepared state
			setTransactionState(ISharedObjectContainerTransaction.PREPARED);
		} catch (Exception except) {
			// If throws exception, we're doomed
			traceStack("handleReplicaActivated(" + event + ")", except); //$NON-NLS-1$ //$NON-NLS-2$
			setTransactionState(ISharedObjectContainerTransaction.ABORTED);
		}
	}

	protected void handleJoined(IContainerConnectedEvent event) {
		trace("handleJoined(" + event + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		// If we are primary then this event matters to us
		if (isPrimary()) {
			// If transactionstate is VOTING then we replicate ourselves to
			// participants
			if (getTransactionState() == ISharedObjectContainerTransaction.VOTING) {
				synchronized (lock) {
					// First send replicate message *no matter what state we are
					// in*
					ID[] newMember = new ID[] {event.getTargetID()};
					replicateTo(newMember);
					addParticipants(newMember);
				}
			}
		}
	}

	protected void handleCreateResponse(ISharedObjectCreateResponseEvent event) {
		trace("handleCreateResponse(" + event + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		if (isPrimary()) {
			synchronized (lock) {
				Throwable except = event.getException();
				ID remoteID = event.getRemoteContainerID();
				long ident = event.getSequence();
				if (getTransactionState() == ISharedObjectContainerTransaction.VOTING) {
					if (except == null) {
						removeParticipant(remoteID);
					} else {
						addFailed(remoteID, except);
					}
				} else {
					handleVotingCompletedCreateResponse(remoteID, except, ident);
				}
				lock.notifyAll();
			}
		} else {
			// we don't care as we are note transaction monitor
		}
	}

	protected void handleDeparted(IContainerDisconnectedEvent event) {
		trace("handleDeparted(" + event + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		if (isPrimary()) {
			ID remoteID = event.getTargetID();
			synchronized (lock) {
				if (getTransactionState() == ISharedObjectContainerTransaction.VOTING) {
					addFailed(remoteID, new Exception("Container " + remoteID //$NON-NLS-1$
							+ " left")); //$NON-NLS-1$
				}
				lock.notifyAll();
			}
		} else {
			// we don't care as we are not transaction monitor
		}
	}

	protected void handleVotingCompletedCreateResponse(ID fromID, Throwable e, long identifier1) {
		trace("handleVotingCompletedCreateResponse(" + fromID + "," + e + "," //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ identifier1 + ")"); //$NON-NLS-1$
		// If remote creation was successful, simply send commit message back.
		if (e == null) {
			try {
				getSharedObject().getContext().sendMessage(fromID, new SharedObjectCommitEvent(getSharedObject().getID()));
			} catch (Exception e2) {
				traceStack("Exception in sendCommit to " + fromID, e2); //$NON-NLS-1$
			}
		} else {
			// Too late to vote no
			handlePostCommitFailure(fromID, e, identifier1);
		}
	}

	protected void handlePostCommitFailure(ID fromID, Throwable e, long identifier1) {
		// Do nothing but report
		trace("handlePostCommitFailure(" + fromID + "," + e + "," + identifier1 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ")"); //$NON-NLS-1$
	}

	protected void sendCommit() throws SharedObjectAddAbortException {
		try {
			getContext().sendMessage(null, new SharedObjectCommitEvent(getSharedObject().getID()));
		} catch (Exception e2) {
			doTMAbort(new SharedObjectAddAbortException(Messages.TwoPhaseCommitEventProcessor_Exception_Shared_Object_Add_Abort, e2, getTimeout()));
		}
	}

	public byte getTransactionState() {
		synchronized (lock) {
			return transactionState;
		}
	}

	protected void setTransactionState(byte state) {
		synchronized (lock) {
			transactionState = state;
		}
	}

	public void waitToCommit() throws SharedObjectAddAbortException {
		if (getTransactionState() == ISharedObjectContainerTransaction.COMMITTED)
			return;
		synchronized (lock) {
			long end = System.currentTimeMillis() + getTimeout();
			try {
				while (!isVotingCompleted()) {
					long wait = end - System.currentTimeMillis();
					trace("waitForFinish waiting " + wait + "ms on " //$NON-NLS-1$ //$NON-NLS-2$
							+ getSharedObject().getID());
					if (wait <= 0L)
						throw new SharedObjectAddAbortException(Messages.TwoPhaseCommitEventProcessor_Exception_Commit_Timeout + getSharedObject().getID() + Messages.TwoPhaseCommitEventProcessor_Exception_Timeout_Adding_To + getHomeID(), (Throwable) null, getTimeout());
					// Wait right here
					lock.wait(wait);
				}
			} catch (Exception except) {
				// Aborted for some reason. Clean up and throw
				doTMAbort(except);
			}
			// Success. Send commit to remotes and clean up before returning.
			doTMCommit();
		}
	}

	protected void doTMAbort(Throwable except) throws SharedObjectAddAbortException {
		trace("doTMAbort:" + except); //$NON-NLS-1$
		// Set our own state variable to ABORTED
		setTransactionState(ISharedObjectContainerTransaction.ABORTED);
		// Send destroy message here so all remotes get destroyed, and we remove
		// ourselves from local space as well.
		getSharedObject().destroySelf();
		// throw so caller gets exception and can deal with it
		if (except instanceof SharedObjectAddAbortException)
			throw (SharedObjectAddAbortException) except;
		throw new SharedObjectAddAbortException(Messages.TwoPhaseCommitEventProcessor_Exception_Shared_Object_Add_Abort, except, getTimeout());
	}

	protected void doTMCommit() throws SharedObjectAddAbortException {
		trace("doTMCommit"); //$NON-NLS-1$
		// Make sure we are connected. If so then send commit message
		if (getSharedObject().getGroupID() != null) {
			sendCommit();
		}
		// Call local committed message
		localCommitted();
	}

	protected void localCommitted() {
		trace("localCommitted()"); //$NON-NLS-1$
		// Set state variable to committed.
		setTransactionState(ISharedObjectContainerTransaction.COMMITTED);
		getSharedObject().creationCompleted();
	}

	protected boolean isVotingCompleted() throws SharedObjectAddAbortException {
		// The test here is is we've received any indication of failed
		// participants in the transaction. If so, we throw.
		if (getTransactionState() == ISharedObjectContainerTransaction.COMMITTED)
			return true;
		if (failed.size() > getMinFailedToAbort()) {
			// Abort!
			trace("isVotingCompleted:aborting:failed>" + getMinFailedToAbort() //$NON-NLS-1$
					+ ":failed=" + failed); //$NON-NLS-1$
			throw new SharedObjectAddAbortException(Messages.TwoPhaseCommitEventProcessor_Exception_Shared_Object_Add_Abort, participants, failed, getTimeout());
			// If no problems, and the number of participants to here from is 0,
			// then we're done
		} else if (getTransactionState() == ISharedObjectContainerTransaction.VOTING && participants.size() == 0) {
			// Success!
			trace("isVotingCompleted() returning true"); //$NON-NLS-1$
			return true;
		}
		// Else continue waiting
		trace("isVotingCompleted:false"); //$NON-NLS-1$
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15462.java