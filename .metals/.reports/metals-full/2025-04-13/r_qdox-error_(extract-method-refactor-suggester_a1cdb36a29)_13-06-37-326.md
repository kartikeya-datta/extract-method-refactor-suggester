error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1910.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1910.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1910.java
text:
```scala
r@@eturn new HeapRowLocation();

/*

   Derby - Class org.apache.derby.impl.store.access.heap.HeapRowLocation

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

package org.apache.derby.impl.store.access.heap;

/**

  A heap row location represents the location of a row in the heap.
  <P>
  It's implementad as a wrapper around a raw store record handle.

**/

import org.apache.derby.iapi.services.io.ArrayInputStream;
import org.apache.derby.iapi.services.io.CompressedNumber;

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.store.access.conglomerate.TransactionManager;
import org.apache.derby.iapi.types.CloneableObject;
import org.apache.derby.iapi.types.Orderable;
import org.apache.derby.iapi.types.RowLocation;
import org.apache.derby.iapi.store.raw.RecordHandle;
import org.apache.derby.iapi.store.raw.ContainerHandle;
import org.apache.derby.iapi.store.raw.Transaction;

import org.apache.derby.iapi.services.io.FormatIdUtil;
import org.apache.derby.iapi.services.io.StoredFormatIds;

import org.apache.derby.iapi.services.sanity.SanityManager;

import org.apache.derby.iapi.types.DataValueDescriptor;

import org.apache.derby.iapi.services.cache.ClassSize;

import org.apache.derby.iapi.types.DataType;


import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

/**
 * @format_id ACCESS_HEAP_ROW_LOCATION_V1_ID
 *
 * @purpose   Object used to store the location of a row within a Heap table.  
 *            One of these is stored in every row of a btree secondary index 
 *            built on a heap base table.
 *
 * @upgrade   The type of the btree determines the type of rowlocation stored.
 *            In current btree implementations only one type of rowlocation can
 *            be stored per tree, and it's type is stored in the format id 
 *            array stored in the Conglomerate object.
 *
 * @disk_layout 
 *     page number(CompressedNumber.writeLong())
 *     record id(CompressedNumber.writeInt())
 **/

public class HeapRowLocation extends DataType implements RowLocation
{
	/**
	The HeapRowLocation simply maintains a raw store record handle.
	**/
    private long         pageno;
    private int          recid;
	private RecordHandle rh;

    private static final int BASE_MEMORY_USAGE = ClassSize.estimateBaseFromCatalog( HeapRowLocation.class);
    private static final int RECORD_HANDLE_MEMORY_USAGE
    = ClassSize.estimateBaseFromCatalog( org.apache.derby.impl.store.raw.data.RecordId.class);

    public int estimateMemoryUsage()
    {
        int sz = BASE_MEMORY_USAGE;

        if( null != rh)
            sz += RECORD_HANDLE_MEMORY_USAGE;
        return sz;
    } // end of estimateMemoryUsage

	public String getTypeName() {
		return "RowLocation";
	}

	public void setValueFromResultSet(java.sql.ResultSet resultSet, int colNumber,
		boolean isNullable) {
	}

	public DataValueDescriptor getNewNull() {
		return null;
	}
	public void setValue(Object o) {
	}

	public Object getObject() {
		return null;
	}

	/*
	** Methods of CloneableObject.
	*/
	public Object cloneObject()
	{
		return getClone();
		
	}

	public DataValueDescriptor getClone() {
		return new HeapRowLocation(this);
	}

	public int getLength() {
		return 10;
	}

	public String getString() {
		return toString();
	}

	/*
	** Methods of Orderable (from RowLocation)
	**
	** see description in
	** protocol/Database/Storage/Access/Interface/Orderable.java 
	**
	*/

	public boolean compare(int op,
						   DataValueDescriptor other,
						   boolean orderedNulls,
						   boolean unknownRV)
	{
		// HeapRowLocation should not be null, ignore orderedNulls
		int result = compare(other);

		switch(op)
		{
		case ORDER_OP_LESSTHAN:
			return (result < 0); // this < other
		case ORDER_OP_EQUALS:
			return (result == 0);  // this == other
		case ORDER_OP_LESSOREQUALS:
			return (result <= 0);  // this <= other
		default:

            if (SanityManager.DEBUG)
                SanityManager.THROWASSERT("Unexpected operation");
			return false;
		}
	}

	public int compare(DataValueDescriptor other)
	{
		// REVISIT: do we need this check?
        if (SanityManager.DEBUG)
            SanityManager.ASSERT(other instanceof HeapRowLocation);

		HeapRowLocation arg = (HeapRowLocation) other;
		
		// XXX (nat) assumption is that these HeapRowLocations are
		// never null.  However, if they ever become null, need
		// to add null comparison logic.
        //
        // RESOLVE - change these to be state based
        /*
        if (SanityManager.DEBUG)
            SanityManager.ASSERT(getRecordHandle() != null);
        if (SanityManager.DEBUG)
            SanityManager.ASSERT(arg.getRecordHandle() != null);
        */

		long myPage     = this.pageno;
		long otherPage  = arg.pageno;

		if (myPage < otherPage)
			return -1;
		else if (myPage > otherPage)
			return 1;

		int myRecordId      = this.recid;
		int otherRecordId   = arg.recid;

		if (myRecordId == otherRecordId)
			return 0;
		else if (myRecordId < otherRecordId)
			return -1;
		else
			return 1;
	}

	/*
	** Methods of HeapRowLocation
	*/

	HeapRowLocation(RecordHandle rh)
	{
		setFrom(rh);
	}

	public HeapRowLocation()
	{
        this.pageno = 0; 
        this.recid  = RecordHandle.INVALID_RECORD_HANDLE;
	}

	/* For cloning */
	private HeapRowLocation(HeapRowLocation other)
	{
		this.pageno = other.pageno;
		this.recid = other.recid;
		this.rh = other.rh;
	}

	public RecordHandle getRecordHandle(ContainerHandle ch)
        throws StandardException
	{
		if (rh != null)
			return rh;

		return rh = ch.makeRecordHandle(this.pageno, this.recid);
	}

	void setFrom(RecordHandle rh)
	{
        this.pageno = rh.getPageNumber();
        this.recid  = rh.getId();
		this.rh = rh;
	}

	//public void setFrom(long pageno, int recid)
	//{
    //    this.pageno = pageno;
    //    this.recid  = recid;
	//}

	/*
	 * InternalRowLocation interface
	 */

    /**
     * Return a RecordHandle built from current RowLocation.
     * <p>
     * Build a RecordHandle from the current RowLocation.  The main client
     * of this interface is row level locking secondary indexes which read
     * the RowLocation field from a secondary index row, and then need a
     * RecordHandle built from this RowLocation.
     * <p>
     * The interface is not as generic as one may have wanted in order to
     * store as compressed a version of a RowLocation as possible.  So 
     * if an implementation of a RowLocation does not have the segmentid, 
     * and containerid stored, use the input parameters instead.  If the
     * RowLocation does have the values stored use them and ignore the
     * input parameters.
     * <p>
     * Example:
     * <p>
     * The HeapRowLocation implementation of RowLocation generated by the 
     * Heap class, only stores the page and record id.  The B2I conglomerate
     * implements a secondary index on top of a Heap class.  B2I knows the
     * segmentid and containerid of it's base table, and knows that it can
     * find an InternalRowLocation in a particular column of it's rows.  It
     * uses InternalRowLocation.getRecordHandle() to build a RecordHandle
     * from the InternalRowLocation, and uses it to set a row lock on that
     * row in the btree.
     *
	 * @return The newly allocated RecordHandle.
     *
     * @param segmentid     The segment id to store in RecordHandle.
     * @param containerid   The segment id to store in RecordHandle.
     *
	 * @exception  StandardException  Standard exception policy.
     **/
    /*public RecordHandle getRecordHandle(
    TransactionManager   tran,
    long                 segmentid,
    long                 containerid)
        throws StandardException
    {
        return(
            this.getRecordHandle(
                tran.getRawStoreXact(), segmentid, containerid));
    }
*/

	/*
	 * Storable interface, implies Externalizable, TypedFormat
	 */

	/**
		Return my format identifier.

		@see org.apache.derby.iapi.services.io.TypedFormat#getTypeFormatId
	*/
	public int getTypeFormatId() {
		return StoredFormatIds.ACCESS_HEAP_ROW_LOCATION_V1_ID;
	}

    public boolean isNull()
    {
        return false;
    }

	public void writeExternal(ObjectOutput out) 
        throws IOException
    {
        // Write the page number, compressed
        CompressedNumber.writeLong(out, this.pageno);

        // Write the record id
        CompressedNumber.writeInt(out, this.recid);
    }

	/**
	  @exception java.lang.ClassNotFoundException A class needed to read the
	  stored form of this object could not be found.
	  @see java.io.Externalizable#readExternal
	  */
	public void readExternal(ObjectInput in) 
        throws IOException, ClassNotFoundException
    {
        this.pageno = CompressedNumber.readLong(in);

        this.recid  = CompressedNumber.readInt(in);

		rh = null;
    }
	public void readExternalFromArray(ArrayInputStream in) 
        throws IOException, ClassNotFoundException
    {
        this.pageno = in.readCompressedLong();

        this.recid  = in.readCompressedInt();

		rh = null;
    }

    public void restoreToNull()
    {
		if (SanityManager.DEBUG) 
			SanityManager.THROWASSERT("HeapRowLocation is never null");
    }
	protected void setFrom(DataValueDescriptor theValue)  {
        if (SanityManager.DEBUG)
            SanityManager.THROWASSERT("SHOULD NOT BE CALLED");
	}
	/*
	**		Methods of Object
	*/

	/**
		Implement value equality.
		<BR>
		MT - Thread safe
	*/
	public boolean equals(Object ref) 
    {

		if ((ref instanceof HeapRowLocation))
        {
            HeapRowLocation other = (HeapRowLocation) ref;

            return(
                (this.pageno == other.pageno) && (this.recid == other.recid));
        }
        else
        {
			return false;
        }

	}

	/**
		Return a hashcode based on value.
		<BR>
		MT - thread safe
	*/
	public int hashCode() 
    {
		return ((int) this.pageno) ^ this.recid;
	}

    /*
     * Standard toString() method.
     */
    public String toString()
    {
        String string = 
           "(" + this.pageno + "," + this.recid + ")";
        return(string);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1910.java