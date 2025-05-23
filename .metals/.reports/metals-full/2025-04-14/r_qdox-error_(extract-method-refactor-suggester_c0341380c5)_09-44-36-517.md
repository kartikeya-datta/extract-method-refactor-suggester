error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7154.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7154.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7154.java
text:
```scala
public I@@DocumentChange deserializeRemoteChange(byte[] bytes) throws SerializationException {

/****************************************************************************
 * Copyright (c) 2008 Mustafa K. Isik and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mustafa K. Isik - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.sync.doc.cola;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.internal.sync.Activator;
import org.eclipse.ecf.internal.sync.SyncDebugOptions;
import org.eclipse.ecf.sync.doc.IDocumentChange;
import org.eclipse.ecf.sync.doc.IDocumentChangeMessage;
import org.eclipse.ecf.sync.doc.IDocumentSynchronizationStrategy;
import org.eclipse.ecf.sync.doc.SerializationException;
import org.eclipse.ecf.sync.doc.messages.DocumentChangeMessage;
import org.eclipse.osgi.util.NLS;

public class ColaSynchronizationStrategy implements IDocumentSynchronizationStrategy {

	// <ColaDocumentChangeMessage>
	private final LinkedList unacknowledgedLocalOperations;
	private final boolean isInitiator;
	private long localOperationsCount;
	private long remoteOperationsCount;

	// <DocShare, ColaSynchronizationStrategy>
	private static Map sessionStrategies = new HashMap();

	private ColaSynchronizationStrategy(boolean isInitiator) {
		this.isInitiator = isInitiator;
		unacknowledgedLocalOperations = new LinkedList();
		localOperationsCount = 0;
		remoteOperationsCount = 0;
	}

	public static ColaSynchronizationStrategy getInstanceFor(ID client, boolean isInitiator) {
		if (sessionStrategies.get(client) == null) {
			sessionStrategies.put(client, new ColaSynchronizationStrategy(isInitiator));
		}
		return (ColaSynchronizationStrategy) sessionStrategies.get(client);
	}

	public static void cleanUpFor(ID client) {
		sessionStrategies.remove(client);
	}

	public DocumentChangeMessage registerOutgoingMessage(DocumentChangeMessage localMsg) {
		Trace.entering(Activator.PLUGIN_ID, SyncDebugOptions.METHODS_ENTERING, this.getClass(), "registerOutgoingMessage", localMsg); //$NON-NLS-1$
		final ColaDocumentChangeMessage colaMsg = new ColaDocumentChangeMessage(localMsg, localOperationsCount, remoteOperationsCount);
		if (!colaMsg.isReplacement()) {
			unacknowledgedLocalOperations.add(colaMsg);
			localOperationsCount++;
		}
		Trace.exiting(Activator.PLUGIN_ID, SyncDebugOptions.METHODS_EXITING, this.getClass(), "registerOutgoingMessage", colaMsg); //$NON-NLS-1$
		return colaMsg;
	}

	/**
	 * Handles proper transformation of incoming <code>ColaDocumentChangeMessage</code>s.
	 * Returned <code>DocumentChangeMessage</code>s can be applied directly to the
	 * shared document. The method implements the concurrency algorithm described
	 * in <code>http://wiki.eclipse.org/RT_Shared_Editing</code>
	 * @param remoteMsg 
	 * @return List contains <code>DocumentChangeMessage</code>s ready for sequential application to document
	 */
	public List transformIncomingMessage(final DocumentChangeMessage remoteMsg) {
		if (!(remoteMsg instanceof ColaDocumentChangeMessage)) {
			throw new IllegalArgumentException("DocumentChangeMessage is incompatible with Cola SynchronizationStrategy"); //$NON-NLS-1$
		}
		Trace.entering(Activator.PLUGIN_ID, SyncDebugOptions.METHODS_ENTERING, this.getClass(), "transformIncomingMessage", remoteMsg); //$NON-NLS-1$
		ColaDocumentChangeMessage transformedRemote = (ColaDocumentChangeMessage) remoteMsg;

		final List transformedRemotes = new LinkedList();
		transformedRemotes.add(transformedRemote);

		remoteOperationsCount++;
		//this is where the concurrency algorithm is executed
		if (!unacknowledgedLocalOperations.isEmpty()) {//Do not remove this. It is necessary. The following iterator does not suffice.
			// remove operations from queue that have been implicitly
			// acknowledged as received on the remote site by the reception of
			// this message
			for (final Iterator it = unacknowledgedLocalOperations.iterator(); it.hasNext();) {
				final ColaDocumentChangeMessage unackedLocalOp = (ColaDocumentChangeMessage) it.next();
				if (transformedRemote.getRemoteOperationsCount() > unackedLocalOp.getLocalOperationsCount()) {
					Trace.trace(Activator.PLUGIN_ID, NLS.bind("transformIncomingMessage.removing {0}", unackedLocalOp)); //$NON-NLS-1$
					it.remove();
				} else {
					// the unackowledgedLocalOperations queue is ordered and
					// sorted
					// due to sequential insertion of local ops, thus once a
					// local op with a higher
					// or equal local op count (i.e. remote op count from the
					// remote operation's view)
					// is reached, we can abandon the check for the remaining
					// queue items
					Trace.trace(Activator.PLUGIN_ID, "breaking out of unackedLocalOperations loop"); //$NON-NLS-1$
					break;// exits for-loop
				}
			}

			// at this point the queue has been freed of operations that
			// don't require to be transformed against

			if (!unacknowledgedLocalOperations.isEmpty()) {
				ColaDocumentChangeMessage localOp = (ColaDocumentChangeMessage) unacknowledgedLocalOperations.getFirst();
				Assert.isTrue(transformedRemote.getRemoteOperationsCount() == localOp.getLocalOperationsCount());

				for (final ListIterator unackOpsListIt = unacknowledgedLocalOperations.listIterator(); unackOpsListIt.hasNext();) {
					for (final ListIterator trafoRemotesIt = transformedRemotes.listIterator(); trafoRemotesIt.hasNext();) {
						// returns new instance
						// clarify operation preference, owner/docshare initiator
						// consistently comes first
						localOp = (ColaDocumentChangeMessage) unackOpsListIt.next();
						transformedRemote = (ColaDocumentChangeMessage) trafoRemotesIt.next();
						transformedRemote = transformedRemote.transformAgainst(localOp, isInitiator);

						if (transformedRemote.isSplitUp()) {
							//currently this only happens for a remote deletion that needs to be transformed against a locally applied insertion
							//attention: before applying a list of deletions to docshare, the indices need to be updated/finalized one last time
							//since deletions can only be applied sequentially and every deletion is going to change the underlying document and the
							//respective indices!
							trafoRemotesIt.remove();
							for (final Iterator splitUpIterator = transformedRemote.getSplitUpRepresentation().iterator(); splitUpIterator.hasNext();) {
								trafoRemotesIt.add(splitUpIterator.next());
							}
							//according to the ListIterator documentation it seems so as if the following line is unnecessary, 
							//as a call to next() after the last removal and additions will return what it would have returned anyway 
							//trafoRemotesIt.next();//TODO not sure about the need for this - I want to jump over the two inserted ops and reach the end of this iterator
						}

						//TODO check whether or not this collection shuffling does what it is supposed to, i.e. remove current localop in unack list and add split up representation instead
						if (localOp.isSplitUp()) {
							//local operation has been split up during operational transform --> remove current version and add new versions plus jump over those
							unackOpsListIt.remove();
							for (final Iterator splitUpOpIterator = localOp.getSplitUpRepresentation().iterator(); splitUpOpIterator.hasNext();) {
								unackOpsListIt.add(splitUpOpIterator.next());
							}
							//according to the ListIterator documentation it seems so as if the following line is unnecessary, 
							//as a call to next() after the last removal and additions will return what it would have returned anyway
							//unackOpsListIt.next();//TODO check whether or not this does jump over both inserted operations that replaced the current unack op
						}//end split up localop handling
					}//transformedRemotes List iteration	
				}
			}

		}
		Trace.exiting(Activator.PLUGIN_ID, SyncDebugOptions.METHODS_EXITING, this.getClass(), "transformIncomingMessage", transformedRemote); //$NON-NLS-1$

		//TODO find a cleaner and more OO way of cleaning up the list if it contains multiple deletions:
		if (transformedRemotes.size() > 1) {
			final ColaDocumentChangeMessage firstOp = (ColaDocumentChangeMessage) transformedRemotes.get(0);
			if (firstOp.isDeletion()) {
				//this means all operations in the return list must also be deletions, i.e. modify virtual/optimistic offset for sequential application to document
				final ListIterator deletionFinalizerIt = transformedRemotes.listIterator();
				ColaDocumentChangeMessage previousDel = (ColaDocumentChangeMessage) deletionFinalizerIt.next();//jump over first del-op does not need modification, we know this is OK because of previous size check;
				ColaDocumentChangeMessage currentDel;

				for (; deletionFinalizerIt.hasNext();) {
					currentDel = (ColaDocumentChangeMessage) deletionFinalizerIt.next();
					currentDel.setOffset(currentDel.getOffset() - previousDel.getLengthOfReplacedText());
					previousDel = currentDel;
				}
			}
		}

		return transformedRemotes;
	}

	public String toString() {
		final StringBuffer buf = new StringBuffer("ColaSynchronizationStrategy"); //$NON-NLS-1$
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.sync.doc.IDocumentSynchronizationStrategy#registerLocalChange(org.eclipse.ecf.sync.doc.IDocumentChange)
	 */
	public IDocumentChangeMessage[] registerLocalChange(IDocumentChange localChange) {
		Trace.entering(Activator.PLUGIN_ID, SyncDebugOptions.METHODS_ENTERING, this.getClass(), "registerLocalChange", localChange); //$NON-NLS-1$
		final ColaDocumentChangeMessage colaMsg = new ColaDocumentChangeMessage(new DocumentChangeMessage(localChange.getOffset(), localChange.getLengthOfReplacedText(), localChange.getText()), localOperationsCount, remoteOperationsCount);
		if (!colaMsg.isReplacement()) {
			unacknowledgedLocalOperations.add(colaMsg);
			localOperationsCount++;
		}
		Trace.exiting(Activator.PLUGIN_ID, SyncDebugOptions.METHODS_EXITING, this.getClass(), "registerLocalChange", colaMsg); //$NON-NLS-1$
		return new IDocumentChangeMessage[] {colaMsg};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.sync.doc.IDocumentSynchronizationStrategy#toDocumentChangeMessage(byte[])
	 */
	public IDocumentChange deserializeToDocumentChange(byte[] bytes) throws SerializationException {
		return DocumentChangeMessage.deserialize(bytes);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.sync.doc.IDocumentSynchronizationStrategy#transformRemoteChange(org.eclipse.ecf.sync.doc.IDocumentChangeMessage)
	 */
	public IDocumentChange[] transformRemoteChange(IDocumentChange remoteChange) {
		if (!(remoteChange instanceof DocumentChangeMessage))
			return new IDocumentChange[] {};
		final DocumentChangeMessage m = (DocumentChangeMessage) remoteChange;
		final List l = this.transformIncomingMessage(m);
		return (IDocumentChange[]) l.toArray(new IDocumentChange[] {});
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7154.java