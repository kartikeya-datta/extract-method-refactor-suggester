error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3571.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3571.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,12]

error in qdox parser
file content:
```java
offset: 12
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3571.java
text:
```scala
@exception S@@tandardException Standard Derby error policy

/*

   Derby - Class org.apache.derby.impl.sql.execute.InsertVTIResultSet

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

package org.apache.derby.impl.sql.execute;

import org.apache.derby.iapi.services.sanity.SanityManager;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.sql.conn.LanguageConnectionContext;

import org.apache.derby.iapi.types.DataValueDescriptor;

import org.apache.derby.iapi.sql.execute.CursorResultSet;
import org.apache.derby.iapi.sql.execute.ExecRow;
import org.apache.derby.iapi.sql.execute.NoPutResultSet;

import org.apache.derby.iapi.sql.Activation;
import org.apache.derby.iapi.sql.ResultDescription;

import org.apache.derby.vti.DeferModification;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Properties;

/**
 * Insert the rows from the source into the specified
 * base table. This will cause constraints to be checked
 * and triggers to be executed based on the c's and t's
 * compiled into the insert plan.
 */
class InsertVTIResultSet extends DMLVTIResultSet
{

	private PreparedStatement		ps;
	private VTIResultSet			vtiRS;
	private java.sql.ResultSet		rs;

	private	TemporaryRowHolderImpl	rowHolder;

    /**
	 *
	 * @exception StandardException		Thrown on error
     */
    public InsertVTIResultSet(NoPutResultSet source, 
							  NoPutResultSet vtiRS,
						   Activation activation)
		throws StandardException
    {
		super(source, activation);
		this.vtiRS = (VTIResultSet) vtiRS;
	}
	
	/**
		@exception StandardException Standard Cloudscape error policy
	*/
	protected void openCore() throws StandardException
	{
		/* We must instantiate the VTI on each execution if any of the
		 * parameters contain a ?.
		 */
		if (ps == null) 
		{
			ps = (PreparedStatement) vtiRS.getVTIConstructor().invoke(activation);
		}

        if( ps instanceof DeferModification)
        {
            try
            {
                ((DeferModification) ps).modificationNotify( DeferModification.INSERT_STATEMENT, constants.deferred);
            }
            catch (Throwable t)
            {
                throw StandardException.unexpectedUserException(t);
            }
        }

		ExecRow row = getNextRowCore(sourceResultSet);

		try
		{
			rs = ps.executeQuery();
		}
		catch (Throwable t)
		{
			throw StandardException.unexpectedUserException(t);
		}

		/* Get or re-use the row changer.
		 * NOTE: We need to set ourself as the top result set
		 * if this is not the 1st execution.  (Done in constructor
		 * for 1st execution.)
		 */
		if (! firstExecute)
		{
			lcc.getStatementContext().setTopResultSet(this, subqueryTrackingArray);
		}

		/* The source does not know whether or not we are doing a
		 * deferred mode insert.  If we are, then we must clear the
		 * index scan info from the activation so that the row changer
		 * does not re-use that information (which won't be valid for
		 * a deferred mode insert).
		 */
		if (constants.deferred)
		{
			activation.clearIndexScanInfo();
		}

		if (firstExecute && constants.deferred)
		{
			Properties properties = new Properties();

			/*
			** If deferred we save a copy of the entire row.
			*/
			rowHolder =
				new TemporaryRowHolderImpl(activation, properties,
										   resultDescription);
		}

		while ( row != null )
        {
			/*
			** If we're doing a deferred insert, insert into the temporary
			** conglomerate.  Otherwise, insert directly into the permanent
			** conglomerates using the rowChanger.
			*/
			if (constants.deferred)
			{
				rowHolder.insert(row);
			}
			else
			{
				insertIntoVTI(rs, row);
			}

            rowCount++;

			// No need to do a next on a single row source
			if (constants.singleRowSource)
			{
				row = null;
			}
			else
			{
				row = getNextRowCore(sourceResultSet);
			}
        }

		/*
		** If it's a deferred insert, scan the temporary conglomerate and
		** insert the rows into the permanent conglomerates using rowChanger.
		*/
		if (constants.deferred)
		{
			CursorResultSet tempRS = rowHolder.getResultSet();
			try
			{
				tempRS.open();
				while ((row = tempRS.getNextRow()) != null)
				{
					insertIntoVTI(rs, row);
				}
			} finally
			{
				sourceResultSet.clearCurrentRow();
				tempRS.close();
			}
		}

		if (rowHolder != null)
		{
			rowHolder.close();
			// rowHolder kept across opens
		}
    } // end of normalInsertCore

	private void insertIntoVTI(ResultSet target, ExecRow row)
		throws StandardException
	{
		try
		{
			target.moveToInsertRow();

			DataValueDescriptor[] rowArray = row.getRowArray();
			for (int index = 0; index < rowArray.length; index++)
			{
				DataValueDescriptor dvd = rowArray[index];

				try {
					if (dvd.isNull())
						target.updateNull(index + 1);
					else
						dvd.setInto(target, index + 1);
				} catch (Throwable t) {
					// backwards compatibility - 5.0 and before used
					// updateObject always.
					target.updateObject(index + 1, dvd.getObject());
				}
			}

			target.insertRow();
		}
		catch (Throwable t)
		{
			throw StandardException.unexpectedUserException(t);
		}
	}

	/**
	 * @see org.apache.derby.iapi.sql.ResultSet#cleanUp
	 *
	 * @exception StandardException		Thrown on error
	 */
	public void	cleanUp() throws StandardException
	{
		if (rowHolder != null)
		{
			rowHolder.close();
		}

		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (Throwable t)
			{
				throw StandardException.unexpectedUserException(t);
			}
			rs = null;
		}

		// Close the ps if it needs to be instantiated on each execution
		if (!vtiRS.isReuseablePs() && ps != null)
		{
			try
			{
				ps.close();
				ps = null;
			}
			catch (Throwable t)
			{
				throw StandardException.unexpectedUserException(t);
			}
		}
		super.cleanUp();
	} // end of cleanUp

	// Class implementation

	public void finish() throws StandardException {

		if ((ps != null) && !vtiRS.isReuseablePs())
		{
			try
			{
				ps.close();
				ps = null;
			}
			catch (Throwable t)
			{
				throw StandardException.unexpectedUserException(t);
			}
		}
		super.finish();
	} // end of finish
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3571.java