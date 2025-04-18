error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3556.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3556.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,12]

error in qdox parser
file content:
```java
offset: 12
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3556.java
text:
```scala
@exception S@@tandardException Standard Derby policy.

/*

   Derby - Class org.apache.derby.impl.store.raw.data.LogicalUndoOperation

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

package org.apache.derby.impl.store.raw.data;

import org.apache.derby.impl.store.raw.data.BasePage;

import org.apache.derby.iapi.services.io.FormatIdUtil;
import org.apache.derby.iapi.services.io.StoredFormatIds;
import org.apache.derby.iapi.services.sanity.SanityManager;

import org.apache.derby.iapi.store.raw.Compensation;
import org.apache.derby.iapi.store.raw.Loggable;
import org.apache.derby.iapi.store.raw.Transaction;
import org.apache.derby.iapi.store.raw.Undoable;

import org.apache.derby.iapi.store.raw.log.LogInstant;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.services.io.CompressedNumber;
import org.apache.derby.iapi.util.ByteArray;

import java.io.OutputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

import org.apache.derby.iapi.services.io.LimitObjectInput;

/**
	LogicalUndoOperation is a compensation operation that rolls back the change of
	an LogicalUndoable operation.  A LogicalUndoOperation itself is not undo-able, i.e,
	it is loggable but not undoable.

	<PRE>
	@format_id	LOGOP_PAGE_LOGICAL_UNDO
		the formatId is written by FormatIdOutputStream when this object is
		written out by writeObject
	@purpose	undo a logical log operation 
	@upgrade
	@disk_layout
		PageBasicOperation	the super class
		recordId(CompressedInt) the recordId of the changed row (this may not
				be the recordId during rollback if the record moved from one
				page to another) 
		OptionalData	none (compensation operation never have optional data)
	@end_format
	</PRE>

*/
public class LogicalUndoOperation extends PageBasicOperation implements Compensation {

	protected int recordId;				// the record id to call undoOp.undoMe with

	/** The operation to be rolled back */
	transient private	LogicalPageOperation undoOp = null; 

	protected LogicalUndoOperation(BasePage page)
	{
		super(page);
	}

	/** Set up a compensation operation during run time rollback */
	public LogicalUndoOperation(BasePage page, int recordId, LogicalPageOperation op)
	{
		super(page);
		undoOp = op;
		this.recordId = recordId;
	}

	/**
		Return my format identifier.
	*/

	// no-arg constructor, required by Formatable 
	public LogicalUndoOperation() { super(); }

	public int getTypeFormatId() {
		return StoredFormatIds.LOGOP_PAGE_LOGICAL_UNDO;
	}

	/**
		Write this out.
		@exception IOException error writing to log stream
	*/
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		super.writeExternal(out);
		CompressedNumber.writeInt(out, recordId);
	}


	/**
		Read this in
		@exception IOException error reading from log stream
		@exception ClassNotFoundException log stream corrupted
	*/
	public void readExternal(ObjectInput in) 
		 throws IOException, ClassNotFoundException
	{
		super.readExternal(in);
		recordId = CompressedNumber.readInt(in);
	}

	public void restoreMe(Transaction xact, BasePage undoPage,
						  LogInstant CLRinstant, LimitObjectInput in)
	{
		// Not undoable
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT("cannot call restore me on PhysicalUndoOperation");
	}

	/** 
		Compensation methods
	*/

	/** Set up a LogicalOperation during recovery redo. */
	public void setUndoOp(Undoable op)
	{
		if (SanityManager.DEBUG) {
			SanityManager.ASSERT(op instanceof LogicalPageOperation);
		}

		undoOp = (LogicalPageOperation)op;
	}


	/**
		Loggable methods
	*/

	/** Apply the undo operation, in this implementation of the
		RawStore, it can only call the undoMe method of undoOp

		@param xact			the Transaction that is doing the rollback
		@param instant		the log instant of this undo operation
		@param in			optional data

		@exception IOException Can be thrown by any of the methods of ObjectInput.
		@exception StandardException Standard Cloudscape policy.

	 */
	public final void doMe(Transaction xact, LogInstant instant, LimitObjectInput in) 
		 throws StandardException, IOException
	{

		long oldversion = 0;		// sanity check
		LogInstant oldLogInstant = null; // sanity check
		if (SanityManager.DEBUG)
		{
			oldLogInstant = this.page.getLastLogInstant();
			oldversion = this.page.getPageVersion();

			SanityManager.ASSERT(oldversion == this.getPageVersion());
			SanityManager.ASSERT(oldLogInstant == null || instant == null 
 oldLogInstant.lessThan(instant));
		}

		// if this is called during runtime rollback, PageOp.generateUndo found
		// the page and have it latched there.
		// if this is called during recovery redo, this.needsRedo found the page and
		// have it latched here
		//
		// in either case, this.page is the correct page and is latched.
		//
		// recordId is generated by generateUndo and is stored here.  If this
		// is a physical undo, recordId is identical to that which is stored in
		// undoOp.  If this is logical undo, it will be different if this.page
		// is different from the undoOp's page (which is where the rollfoward
		// change went to, and the record might have moved by now).
		//
		undoOp.undoMe(xact, this.page, recordId, instant, in);

		if (SanityManager.DEBUG) {
			SanityManager.ASSERT(oldversion < this.page.getPageVersion());
			SanityManager.ASSERT(instant == null || instant.equals(this.page.getLastLogInstant()));
		}

		releaseResource(xact);
	}

	/* make sure resource found in undoOp is released */
	public void releaseResource(Transaction xact)
	{
		if (undoOp != null)
			undoOp.releaseResource(xact);
		super.releaseResource(xact);
	}

	/* Undo operation is a COMPENSATION log operation */
	public int group()
	{
		return super.group() | Loggable.COMPENSATION | Loggable.RAWSTORE;
	}

	public final ByteArray getPreparedLog() {
		// should never ever write optional data because this implementation of
		// the recovery system  will never read this and pass this on to dome.
		// Instead, the optional data of the undoOp will be used - since
		// this.doMe can only call undoOP.undoMe, this has no use for any
		// optional data.
		return (ByteArray) null;
	}

	/**
	  DEBUG: Print self.
	*/
	public String toString()
	{
		if (SanityManager.DEBUG)
		{
			String str = "CLR : (logical undo) " + super.toString() + 
				" undoRecordId = " + recordId;
			if (undoOp != null)
				str += "\n" + undoOp.toString();
			else
				str += " undo Operation not set";
			return str;	
		}
		else
			return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3556.java