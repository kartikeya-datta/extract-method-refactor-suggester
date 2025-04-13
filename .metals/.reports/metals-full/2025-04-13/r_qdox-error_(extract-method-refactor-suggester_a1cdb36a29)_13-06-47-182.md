error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/573.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/573.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/573.java
text:
```scala
@param l@@ockForPreviousKey       Lock is for a previous key of a insert.

/*

   Derby - Class org.apache.derby.iapi.store.raw.LockingPolicy

   Copyright 1997, 2004 The Apache Software Foundation or its licensors, as applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.iapi.store.raw;

import org.apache.derby.iapi.services.locks.Latch;

import org.apache.derby.iapi.error.StandardException;

/**
	Any object that implements this interface can be used as a locking
	policy for accessing a container. 
	<P>
	The locking policy must use the defined lock qualifiers 
    (ContainerLock.CIS, RowLock.RS, etc.) and the standard lock manager.
    (A locking policy that just performs no locking wouldn't need to use 
    these :-)
	<P>
	A locking policy must use the object that is an instance of Transaction
    (originally obtained via startTransaction() in RawStoreFactory) as the 
    compatibilitySpace for the LockFactory calls.
	<BR>
	A locking policy must use the passed in transaction as the 
    compatability space and the lock group.
	This chain (group) of locks has the following defined behaviour
		<UL>
		<LI>Locks are released at transaction.commit()
		<LI>Locks are released at transaction.abort()
		</UL>


    <BR>
	MT - Thread Safe

	@see ContainerHandle
	@see RecordHandle
	@see org.apache.derby.iapi.services.locks.LockFactory
	@see org.apache.derby.iapi.services.locks.Lockable

*/

public interface LockingPolicy {

	/**
		No locking what so ever, isolation parameter will be ignored by
		getLockingPolicy().

		@see  	RawStoreFactory
	*/
	static final int MODE_NONE = 0;

	/**
		Record level locking.
	*/
	static final int MODE_RECORD = 1;

	/**
		ContainerHandle level locking.
	*/
	static final int MODE_CONTAINER = 2;

	/**
		Called when a container is opened.

        @param t            Transaction to associate lock with.
        @param container    Container to lock.
        @param waitForLock  Should lock request wait until granted?
        @param forUpdate    Should container be locked for update, or read?

		@return true if the lock was obtained, false if it wasn't. 
        False should only be returned if the waitForLock policy was set to
        "false," and the lock was unavailable.

		@exception StandardException	Standard Cloudscape error policy

		@see ContainerHandle

	*/
	public boolean lockContainer(
    Transaction         t, 
    ContainerHandle     container, 
    boolean             waitForLock,
    boolean             forUpdate)
		throws StandardException;

	/**
		Called when a container is closed.

		@see ContainerHandle
		@see ContainerHandle#close
	*/
	public void unlockContainer(
    Transaction t, 
    ContainerHandle container);

	/**
		Called before a record is fetched.

        @param t            Transaction to associate lock with.
        @param container    Open Container used to get record.  Will be used
                            to row locks by the container they belong to.
        @param record       Record to lock.
        @param waitForLock  Should lock request wait until granted?
        @param forUpdate    Should container be locked for update, or read?


		@exception StandardException	Standard Cloudscape error policy

		@see Page

	*/
	public boolean lockRecordForRead(
    Transaction     t, 
    ContainerHandle container, 
    RecordHandle    record, 
    boolean         waitForLock,
    boolean         forUpdate)
		throws StandardException;


	/**
		Lock a record while holding a page latch.

        @param latch        Latch held.
        @param record       Record to lock.
        @param forUpdate    Should container be locked for update, or read?


		@exception StandardException	Standard Cloudscape error policy

		@see Page

	*/
	public void lockRecordForRead(
		Latch			latch, 
		RecordHandle    record, 
		boolean         forUpdate)
			throws StandardException;

	/**
        Request a write lock which will be released immediately upon grant.

        @param t                        Transaction to associate lock with.
        @param record                   Record to lock.
        @param lockForInsertPreviouskey Lock is for a previous key of a insert.
        @param waitForLock              Should lock request wait until granted?

		@return true if the lock was obtained, false if it wasn't. 
        False should only be returned if the waitForLock argument was set to
        "false," and the lock was unavailable.

		@exception StandardException	Standard Cloudscape error policy

		@see Page
	*/
	public boolean zeroDurationLockRecordForWrite(
    Transaction     t, 
    RecordHandle    record,
    boolean         lockForPreviousKey,
    boolean         waitForLock)
		throws StandardException;

	/**
	    Called before a record is inserted, updated or deleted.

        If zeroDuration is true then lock is released immediately after it
        has been granted.

        @param t             Transaction to associate lock with.
        @param record        Record to lock.
        @param lockForInsert Lock is for an insert.
        @param waitForLock   Should lock request wait until granted?

		@return true if the lock was obtained, false if it wasn't. 
        False should only be returned if the waitForLock argument was set to
        "false," and the lock was unavailable.

		@exception StandardException	Standard Cloudscape error policy

		@see Page
	*/
	public boolean lockRecordForWrite(
    Transaction     t, 
    RecordHandle    record,
    boolean         lockForInsert,
    boolean         waitForLock)
		throws StandardException;

	/**
	    Lock a record for write while holding a page latch.


        @param latch        Page latch held.
        @param record       Record to lock.

		@exception StandardException	Standard Cloudscape error policy

		@see Page
	*/
	public void lockRecordForWrite(
    Latch			latch, 
    RecordHandle    record)
		throws StandardException;
	/**
		Called after a record has been fetched.

		@exception StandardException	Standard Cloudscape error policy

  		@see Page

	*/
	public void unlockRecordAfterRead(
    Transaction     t, 
    ContainerHandle container, 
    RecordHandle    record, 
    boolean         forUpdate,
    boolean         row_qualified)
        throws StandardException;


	/**
		Get the mode of this policy
	*/
	public int getMode();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/573.java