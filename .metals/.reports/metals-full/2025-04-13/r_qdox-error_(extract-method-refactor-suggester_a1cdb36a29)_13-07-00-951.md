error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1887.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1887.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1887.java
text:
```scala
(@@(AllocPage)undoPage).undoCompressSpace(

/*

   Derby - Class org.apache.derby.impl.store.raw.data.ChainAllocPageOperation

   Copyright 2005 The Apache Software Foundation or its licensors, as applicable.

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

package org.apache.derby.impl.store.raw.data;

import org.apache.derby.impl.store.raw.data.PhysicalPageOperation;
import org.apache.derby.impl.store.raw.data.BasePage;

import org.apache.derby.iapi.services.io.FormatIdUtil;
import org.apache.derby.iapi.services.io.StoredFormatIds;
import org.apache.derby.iapi.services.sanity.SanityManager;

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.store.raw.ContainerHandle;
import org.apache.derby.iapi.store.raw.data.RawContainerHandle;
import org.apache.derby.iapi.store.raw.Transaction;
import org.apache.derby.iapi.store.raw.log.LogInstant;

import org.apache.derby.iapi.services.io.CompressedNumber;

import java.io.OutputStream;
import java.io.ObjectOutput;
import java.io.IOException;
import org.apache.derby.iapi.services.io.LimitObjectInput;
import java.io.ObjectInput;


/**

Log operation to implement compressing space from a container and returning
it to the operating system.

**/

public final class CompressSpacePageOperation extends PhysicalPageOperation
{
    /**************************************************************************
     * Fields of the class
     **************************************************************************
     */

    /**
     * The new highest page on this allocation page.  The number is the
     * offset of the page in the array of pages maintained by this 
     * allocation page, for instance a value of 0 indicates all page except
     * the first one are to be truncated.  If all pages are truncated then 
     * the offset is set to -1.
     **/
	protected int newHighestPage;	    

    /**
     * The number of allocated pages in this allocation page prior to 
     * the truncate.  Note that all pages from NewHighestPage+1 through
     * newHighestPage+num_pages_truncated should be FREE.
     **/
	protected int num_pages_truncated; 

    /**************************************************************************
     * Constructors for This class:
     **************************************************************************
     */
	public CompressSpacePageOperation(
    AllocPage   allocPage, 
    int         highest_page, 
    int         num_truncated)
		 throws StandardException
	{
		super(allocPage);

        newHighestPage      = highest_page;
        num_pages_truncated = num_truncated;
	}
	
    /**************************************************************************
     * Public Methods of Formatable interface.
     **************************************************************************
     */

	// no-arg constructor, required by Formatable 
	public CompressSpacePageOperation() { super(); }

	public void writeExternal(ObjectOutput out) throws IOException 
	{
		super.writeExternal(out);
		CompressedNumber.writeInt(out, newHighestPage);
		CompressedNumber.writeInt(out, num_pages_truncated);
	}

	/**
		@exception IOException error reading from log stream
		@exception ClassNotFoundException cannot read object from input
	*/
	public void readExternal(ObjectInput in)
		 throws IOException, ClassNotFoundException
	{
		super.readExternal(in);
		newHighestPage      = CompressedNumber.readInt(in);
		num_pages_truncated = CompressedNumber.readInt(in);
	}

	/**
		Return my format identifier.
	*/
	public int getTypeFormatId() {
		return StoredFormatIds.LOGOP_COMPRESS_SPACE;
	}

    /**************************************************************************
     * Public Methods of Loggable interface.
     **************************************************************************
     */

    /**
     * Compress space from container.
     * <p>
     * Compress the indicate space from the container, returning the free
     * pages to the OS.  Update the allocation page to reflect the file
     * change.
     *
     * @param tran      transaction doing the operation.
     * @param instant   log instant for this operation.
     * @param in        unused by this log operation.
     *
	 * @exception  StandardException  Standard exception policy.
     **/
	public final void doMe(
    Transaction         tran, 
    LogInstant          instant, 
    LimitObjectInput    in) 
		 throws StandardException
	{
		if (SanityManager.DEBUG) 
        {
			SanityManager.ASSERT(this.page instanceof AllocPage);
		}

		((AllocPage)page).compressSpace(
             instant, newHighestPage, num_pages_truncated);
	}

    /**************************************************************************
     * Public Methods of Undoable interface.
     **************************************************************************
     */

    /**
     * Compress space undo.
     * <p>
     *
	 * @exception StandardException Thrown by methods I call 
     * @see PhysicalPageOperation#undoMe
     **/
	public void undoMe(
    Transaction         xact, 
    BasePage            undoPage, 
    LogInstant          CLRInstant, 
    LimitObjectInput    in)
		 throws StandardException
	{
		if (SanityManager.DEBUG) 
        {
			SanityManager.ASSERT(undoPage != null, "undo Page null");
			SanityManager.ASSERT(
                undoPage instanceof AllocPage, 
				"undo Page is not an allocPage");
		}

		((AllocPage)undoPage).compressSpace(
             CLRInstant, newHighestPage, num_pages_truncated);
	}

	/*
	 * method to support BeforeImageLogging
	 */
	public void restoreMe(
    Transaction         xact, 
    BasePage            undoPage, 
    LogInstant          CLRinstant, 
    LimitObjectInput    in)
	{
		// nobody should be calling this since there is no corresponding 
        // BI operation.
		if (SanityManager.DEBUG)
			SanityManager.THROWASSERT(
                "cannot call restoreMe on CompressSpaceOperation.");
	}


	/** debug */
	public String toString()
	{
		if (SanityManager.DEBUG)
		{
			String str = super.toString();
			str += " CompressSpaceOperation: " + 
                "newHighestPage = " + newHighestPage +
                ";num_pages_truncated = " + num_pages_truncated +
				" to " + getPageId();

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1887.java