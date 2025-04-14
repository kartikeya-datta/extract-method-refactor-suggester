error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2878.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2878.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2878.java
text:
```scala
r@@s = ps.executeSubStatement(lcc, false, 0L);

/*

   Derby - Class org.apache.derby.impl.sql.execute.ConstraintConstantAction

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

import org.apache.derby.catalog.UUID;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.reference.SQLState;
import org.apache.derby.iapi.services.io.FormatableBitSet;
import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.sql.PreparedStatement;
import org.apache.derby.iapi.sql.ResultSet;
import org.apache.derby.iapi.sql.conn.LanguageConnectionContext;
import org.apache.derby.iapi.sql.dictionary.DataDictionary;
import org.apache.derby.iapi.sql.dictionary.ForeignKeyConstraintDescriptor;
import org.apache.derby.iapi.sql.dictionary.ReferencedKeyConstraintDescriptor;
import org.apache.derby.iapi.sql.dictionary.TableDescriptor;
import org.apache.derby.iapi.sql.execute.ExecRow;
import org.apache.derby.iapi.store.access.ConglomerateController;
import org.apache.derby.iapi.store.access.GroupFetchScanController;
import org.apache.derby.iapi.store.access.ScanController;
import org.apache.derby.iapi.store.access.TransactionController;
import org.apache.derby.iapi.types.DataValueDescriptor;
import org.apache.derby.iapi.types.NumberDataValue;
/**
 *	This class  describes actions that are ALWAYS performed for a
 *	constraint creation at Execution time.
 *
 *	@version 0.1
 */

public abstract class ConstraintConstantAction extends DDLSingleTableConstantAction 
{

	protected	String			constraintName;
	protected	int				constraintType;
	protected	String			tableName;
	protected	String			schemaName;
	protected	UUID			schemaId;
	protected  IndexConstantAction indexAction;

	// CONSTRUCTORS
	/**
	 *	Make one of these puppies.
	 *
	 *  @param constraintName	Constraint name.
	 *  @param constraintType	Constraint type.
	 *  @param tableName		Table name.
	 *  @param tableId			UUID of table.
	 *  @param schemaName		schema that table and constraint lives in.
	 *  @param indexAction		IndexConstantAction for constraint (if necessary)
	 *  RESOLVE - the next parameter should go away once we use UUIDs
	 *			  (Generated constraint names will be based off of uuids)
	 */
	ConstraintConstantAction(
		               String	constraintName,
					   int		constraintType,
		               String	tableName,
					   UUID		tableId,
					   String	schemaName,
					   IndexConstantAction indexAction)
	{
		super(tableId);
		this.constraintName = constraintName;
		this.constraintType = constraintType;
		this.tableName = tableName;
		this.indexAction = indexAction;
		this.schemaName = schemaName;

		if (SanityManager.DEBUG)
		{
			SanityManager.ASSERT(schemaName != null, "Constraint schema name is null");
		}
	}

	// Class implementation

	/**
	 * Get the constraint type.
	 *
	 * @return The constraint type
	 */
	public	int getConstraintType()
	{
		return constraintType;
	}

	/**
	  *	Get the constraint name
	  *
	  *	@return	the constraint name
	  */
    public	String	getConstraintName() { return constraintName; }

	/**
	  *	Get the associated index constant action.
	  *
	  *	@return	the constant action for the backing index
	  */
    public	IndexConstantAction	getIndexAction() { return indexAction; }

	/**
	 * Make sure that the foreign key constraint is valid
	 * with the existing data in the target table.  Open
	 * the table, if there aren't any rows, ok.  If there
	 * are rows, open a scan on the referenced key with
	 * table locking at level 2.  Pass in the scans to
	 * the BulkRIChecker.  If any rows fail, barf.
	 *
	 * @param	tc		transaction controller
	 * @param	dd		data dictionary
	 * @param	fk		foreign key constraint
	 * @param	refcd	referenced key
	 * @param 	indexTemplateRow	index template row
	 *
	 * @exception StandardException on error
	 */
	static void validateFKConstraint
	(
		TransactionController				tc,
		DataDictionary						dd,
		ForeignKeyConstraintDescriptor		fk,
		ReferencedKeyConstraintDescriptor	refcd,
		ExecRow 							indexTemplateRow 
	)
		throws StandardException
	{

		GroupFetchScanController refScan = null;

		GroupFetchScanController fkScan = 
            tc.openGroupFetchScan(
                fk.getIndexConglomerateDescriptor(dd).getConglomerateNumber(),
                false,                       			// hold 
                0, 										// read only
                tc.MODE_TABLE,							// already locked
                tc.ISOLATION_READ_COMMITTED,			// whatever
                (FormatableBitSet)null, 							// retrieve all fields
                (DataValueDescriptor[])null,    	    // startKeyValue
                ScanController.GE,            			// startSearchOp
                null,                         			// qualifier
                (DataValueDescriptor[])null,  			// stopKeyValue
                ScanController.GT             			// stopSearchOp 
                );

		try
		{
			/*
			** If we have no rows, then we are ok.  This will 
			** catch the CREATE TABLE T (x int references P) case
			** (as well as an ALTER TABLE ADD CONSTRAINT where there
			** are no rows in the target table).
			*/	
			if (!fkScan.next())
			{
				fkScan.close();
				return;
			}

			fkScan.reopenScan(
					(DataValueDescriptor[])null,    		// startKeyValue
					ScanController.GE,            			// startSearchOp
					null,                         			// qualifier
					(DataValueDescriptor[])null,  			// stopKeyValue
					ScanController.GT             			// stopSearchOp 
					);

			/*
			** Make sure each row in the new fk has a matching
			** referenced key.  No need to get any special locking
			** on the referenced table because it cannot delete
			** any keys we match because it will block on the table
			** lock on the fk table (we have an ex tab lock on
			** the target table of this ALTER TABLE command).
			** Note that we are doing row locking on the referenced
			** table.  We could speed things up and get table locking
			** because we are likely to be hitting a lot of rows
			** in the referenced table, but we are going to err
			** on the side of concurrency here.
			*/
			refScan = 
                tc.openGroupFetchScan(
					refcd.getIndexConglomerateDescriptor(dd).getConglomerateNumber(),
                        false,                       	// hold 
                        0, 								// read only
                        tc.MODE_RECORD,
                        tc.ISOLATION_READ_COMMITTED,	// read committed is good enough
                        (FormatableBitSet)null, 					// retrieve all fields
                        (DataValueDescriptor[])null,    // startKeyValue
                        ScanController.GE,            	// startSearchOp
                        null,                         	// qualifier
                        (DataValueDescriptor[])null,  	// stopKeyValue
                        ScanController.GT             	// stopSearchOp 
                        );

			RIBulkChecker riChecker = new RIBulkChecker(refScan, 
										fkScan, 
										indexTemplateRow, 	
										true, 				// fail on 1st failure
										(ConglomerateController)null,
										(ExecRow)null);

			int numFailures = riChecker.doCheck();
			if (numFailures > 0)
			{
				StandardException se = StandardException.newException(SQLState.LANG_ADD_FK_CONSTRAINT_VIOLATION, 
									fk.getConstraintName(), 
									fk.getTableDescriptor().getName());
				throw se;
			}
		}
		finally
		{
			if (fkScan != null)
			{
				fkScan.close();
				fkScan = null;
			}
			if (refScan != null)
			{
				refScan.close();
				refScan = null;
			}
		}
	}

	/**
	 * Evaluate a check constraint or not null column constraint.  
	 * Generate a query of the
	 * form SELECT COUNT(*) FROM t where NOT(<check constraint>)
	 * and run it by compiling and executing it.   Will
	 * work ok if the table is empty and query returns null.
	 *
	 * @param constraintName	constraint name
	 * @param constraintText	constraint text
	 * @param td				referenced table
	 * @param lcc				the language connection context
	 * @param isCheckConstraint	the constraint is a check constraint
     *
	 * @return true if null constraint passes, false otherwise
	 *
	 * @exception StandardException if check constraint fails
	 */
	 static boolean validateConstraint
	(
		String							constraintName,
		String							constraintText,
		TableDescriptor					td,
		LanguageConnectionContext		lcc,
		boolean							isCheckConstraint
	)
		throws StandardException
	{
		StringBuffer checkStmt = new StringBuffer();
		/* should not use select sum(not(<check-predicate>) ? 1: 0) because
		 * that would generate much more complicated code and may exceed Java
		 * limits if we have a large number of check constraints, beetle 4347
		 */
		checkStmt.append("SELECT COUNT(*) FROM ");
		checkStmt.append(td.getQualifiedName());
		checkStmt.append(" WHERE NOT(");
		checkStmt.append(constraintText);
		checkStmt.append(")");
	
		ResultSet rs = null;
		try
		{
			PreparedStatement ps = lcc.prepareInternalStatement(checkStmt.toString());

            // This is a substatement; for now, we do not set any timeout
            // for it. We might change this behaviour later, by linking
            // timeout to its parent statement's timeout settings.
			rs = ps.execute(lcc, false, 0L);
			ExecRow row = rs.getNextRow();
			if (SanityManager.DEBUG)
			{
				if (row == null)
				{
					SanityManager.THROWASSERT("did not get any rows back from query: "+checkStmt.toString());
				}
			}

			DataValueDescriptor[] rowArray = row.getRowArray();
			Number value = ((Number)((NumberDataValue)row.getRowArray()[0]).getObject());
			/*
			** Value may be null if there are no rows in the
			** table.
			*/
			if ((value != null) && (value.longValue() != 0))
			{	
				//check constraint violated
				if (isCheckConstraint)
					throw StandardException.newException(SQLState.LANG_ADD_CHECK_CONSTRAINT_FAILED, 
						constraintName, td.getQualifiedName(), value.toString());
				/*
				 * for not null constraint violations exception will be thrown in caller
				 * check constraint will not get here since exception is thrown
				 * above
				 */
				return false;
			}
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
			}
		}
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2878.java