error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/655.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/655.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,12]

error in qdox parser
file content:
```java
offset: 12
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/655.java
text:
```scala
@exception S@@tandardException Standard Derby error policy

/*

   Derby - Class org.apache.derby.iapi.store.access.conglomerate.LogicalUndo

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.iapi.store.access.conglomerate;

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.store.raw.LogicalUndoable;
import org.apache.derby.iapi.store.raw.Page;
import org.apache.derby.iapi.store.raw.Transaction;

import org.apache.derby.iapi.services.io.LimitObjectInput;
import java.io.IOException;

/**
		A Logical undo is an undo operation that operates on a different page
		from the page that has the original change.  The reason one would
		need logical undo is when an uncommitted row move from one page to
		another in a nested internal transaction which is committed.  For
		example, an uncommitted insert on a btree may be moved by a later split
		operation to another page, the split operation will have committed.  If
		the insert needs to be rolled back, it can only be found at the new
		page where the split puts it and not at the original page where it is
		inserted. 
		<P>
		The logging and recovery system does not know how to do logical undo.
		Client of the logging system must provide it with a call back function
		so that during undo time (both runtime undo and recovery undo), the
		appropriate page and row can be found so that the logging system can
		apply the log's undo operation.
		<P>
		Any log operation that needs logical undo must implement this
		LogicalUndo interface, which serves the purpose of a callback function
		pointer.  This callback function findUndoInfo is called by log operation
		generateUndo and will be given all the information in the log operation.
		<P>
		FindUndo uses the information in the pageOp to find the correct page
		and record that needs to be rolled back, i.e., a latched page
		(undoPage) and the recordId (undoRID).  It returns the latched
		undoPage, and modifies the pageOp to contain the correct segmentId,
		containerId, pageNumber and recordId etc.  It also need to supply a
		releaseResource() method that the logging system can call to unlatch
		the page and release the container, etc, after the undo has been
		applied.
		<P>
		The logging system will use the information in the undoPackage to put
		together a Compensation operation which has the undoPage number
		and undoRID.  Logical Undo is only called during the generation of a
		CLR, never during recovery redo.
		<P>
		<B>Note: LogicalUndo is a call back function pointer that will be
		written out as part of the log operation, it should not contain any
		non-transient member fields </B>
		<P>
		Details.
		<P>
		LogicalUndo, and LogicalUndoable is the interface used by logical undo
		between the logging system in RawStore and Access.  A log operation
		that needs logical undo should implment LogicalUndoable intead of
		Undoable.  A LogicalUndoable log operation contains a LogicalUndo
		member field, which is a function pointer to an Access function that
		provides the logical undo logic of, say, traversing a btree.  
		<P>
		When called to generateUndo, that LogicalUndoable log operation will
		call LogicalUndo.findUndo instead of relying on the page number and
		recordId that is stored in it during the runtime roll forward
		operation.  <B>The logging system opens the container before it calls
		findUndo, therefore the container where the log operation is applied
		cannot between rollforward and rollback.</B>
		<P>
		In LogicalUndo.findUndo, it can use information stored in
		the LogicalUndoable, such as pageNumber, containerId, to come up with a
		template row.  It can then ask the LogicalUndoable log record
		to restore a row from the log record that fits the template.  Using
		this restored row, LogicalUndo can, e.g., restore the key to the btree
		and traverses the btree.  Once it finds the correct RecordHandle where
		the rollback should go, findUndo should call pageOp.resetRecord and
		return a latched page where the undo should go.
		<P>
		Upon the return of findUndo, the LogicalUndoable log operation should
		have information about the new RecordHandle and the page should be
		return latched.  A compensation operation is then generated with the
		new record location and undoMe is applied on the correct location.
		<P>
		The logging system will unlatch the undoPage when it is done with
		rollback and will close the container.

		@see org.apache.derby.iapi.store.raw.LogicalUndoable
		@see org.apache.derby.iapi.store.raw.Undoable#generateUndo 
*/

public interface LogicalUndo {

	/**
		Find the page and record to undo.  If no logical undo is necessary,
		i.e., row has not moved, then just return the latched page where undo
		should go.  If the record has moved, it has a new recordId on the new
		page, this routine needs to call pageOp.resetRecord with the new
		RecordHandle so that the logging system can update the compensation
		Operation with the new location.

		@param transaction the transaction doing the rollback
		@param pageOp the page operation that supports logical undo.  This
				LogicalUndo function pointer is a field of that pageOperation
		@param in data stored in the log stream that contains the record data
				necessary to restore the row.

		@exception StandardException Standard Cloudscape error policy
		@exception IOException Method may read from InputStream		
	*/
	public Page findUndo(
    Transaction     transaction, 
    LogicalUndoable pageOp,
    LimitObjectInput     in)
        throws StandardException, IOException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/655.java