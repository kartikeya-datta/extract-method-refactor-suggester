error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6823.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6823.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6823.java
text:
```scala
t@@hrow new IllegalArgumentException("UpdateMessage is incompatible with Cola SynchronizationStrategy"); //$NON-NLS-1$

package org.eclipse.ecf.docshare.cola;

import java.util.*;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.docshare.SynchronizationStrategy;
import org.eclipse.ecf.docshare.messages.UpdateMessage;
import org.eclipse.ecf.internal.docshare.Activator;

public class ColaSynchronizer implements SynchronizationStrategy {

	private List unacknowledgedLocalOperations;
	private final boolean isInitiator;
	private double localOperationsCount;
	private double remoteOperationsCount;

	private static Map sessionStrategies = new HashMap();

	private ColaSynchronizer(ID docshareID) {
		this.isInitiator = Activator.getDefault().getDocShare(docshareID).isInitiator();
		unacknowledgedLocalOperations = new LinkedList();
		localOperationsCount = 0;
		remoteOperationsCount = 0;
	}

	public static ColaSynchronizer getInstanceFor(ID docshareID) {
		if (sessionStrategies.get(docshareID) == null) {
			sessionStrategies.put(docshareID, new ColaSynchronizer(docshareID));
		}
		return (ColaSynchronizer) sessionStrategies.get(docshareID);
	}

	public static void cleanUpFor(ID docshareID) {
		sessionStrategies.remove(docshareID);
	}

	public UpdateMessage registerOutgoingMessage(UpdateMessage localMsg) {
		ColaUpdateMessage colaMsg = new ColaUpdateMessage(localMsg, localOperationsCount, remoteOperationsCount);
		unacknowledgedLocalOperations.add(colaMsg);
		localOperationsCount++;
		return colaMsg;
	}

	/**
	 * Handles proper transformation of incoming <code>ColaUpdateMessage</code>s.
	 * Returned <code>UpdateMessage</code>s can be applied directly to the
	 * shared document. The method implements the concurreny algorithm described
	 * in <code>http://wiki.eclipse.org/RT_Shared_Editing</code>
	 */
	public UpdateMessage transformIncomingMessage(final UpdateMessage remoteMsg) {
		if (!(remoteMsg instanceof ColaUpdateMessage)) {
			throw new IllegalArgumentException("UpdateMessage is incompatible with Cola SynchronizationStrategy");
		}
		ColaUpdateMessage transformedRemote = (ColaUpdateMessage) remoteMsg;
		// TODO this is where the concurrency algorithm is executed
		if (!unacknowledgedLocalOperations.isEmpty()) {
			// remove operations from queue that have been implicitly
			// acknowledged as received on the remote site by the reception of
			// this message
			Iterator queueIterator = unacknowledgedLocalOperations.iterator();
			ColaUpdateMessage localOperation = (ColaUpdateMessage) queueIterator.next();
			while (!unacknowledgedLocalOperations.isEmpty() && transformedRemote.getRemoteOperationsCount() > localOperation.getLocalOperationsCount()) {
				queueIterator.remove();
				if (queueIterator.hasNext()) {
					localOperation = (ColaUpdateMessage) queueIterator.next();
				}
			}// at this point the queue has been freed of operations that
			// don't require to be transformed against
			if (!unacknowledgedLocalOperations.isEmpty()) {
				Iterator queueModIterator = unacknowledgedLocalOperations.iterator();
				while (queueModIterator.hasNext()) {
					// returns new instance
					// clarify operation preference, owner/docshare initiator
					// consistently comes first
					if (this.isInitiator) {
						transformedRemote = transformedRemote.transformForApplicationAtOwnerAgainst(localOperation);
					} else {
						transformedRemote = transformedRemote.transformForApplicationAtParticipantAgainst(localOperation);
					}
					localOperation = (ColaUpdateMessage) queueModIterator.next();
				}
				// TODO unsure whether this is needed or not, need to test
				// transform against last element in the queue
				if (this.isInitiator) {
					transformedRemote = transformedRemote.transformForApplicationAtOwnerAgainst(localOperation);
				} else {
					transformedRemote = transformedRemote.transformForApplicationAtParticipantAgainst(localOperation);
				}
			}
		}
		return transformedRemote;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6823.java