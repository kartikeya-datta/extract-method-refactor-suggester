error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3292.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3292.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3292.java
text:
```scala
a@@ggInfo.getResultDescription().getColumnInfo( 0 ).getType()

/*

   Derby - Class org.apache.derby.impl.sql.execute.GenericAggregator

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

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.services.io.Storable;
import org.apache.derby.iapi.services.loader.ClassFactory;
import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.sql.execute.ExecAggregator;
import org.apache.derby.iapi.sql.execute.ExecRow;
import org.apache.derby.iapi.types.DataValueDescriptor;
import org.apache.derby.iapi.types.UserDataValue;
/**
 * Adaptor that sits between execution layer and aggregates.
 *
 */
class GenericAggregator 
{
	private final AggregatorInfo			aggInfo;
	int						aggregatorColumnId;
	private int						inputColumnId;
	private int						resultColumnId;

	private final ClassFactory		cf;

	/*
	** We cache an aggregator to speed up
	** the instantiation of lots of aggregators.
	*/
	private ExecAggregator		cachedAggregator;

	/**
	 * Constructor:
	 *
	 * @param aggInfo 	information about the user aggregate
	 * @param cf		the class factory. 
	 */
	GenericAggregator
	(
		AggregatorInfo	aggInfo, 
		ClassFactory	cf
	)
	{
		this.aggInfo = aggInfo;
		aggregatorColumnId = aggInfo.getAggregatorColNum();
		inputColumnId = aggInfo.getInputColNum();
		resultColumnId = aggInfo.getOutputColNum();
		this.cf = cf;
	}


	/**
	 * Initialize the aggregator
	 *
	 * @param	row 	the row with the aggregator to be initialized
	 *
	 * @exception StandardException  on error
	 */
	void initialize(ExecRow row)
		throws StandardException
	{
		if (SanityManager.DEBUG)
		{
			SanityManager.ASSERT(row != null, "row is null");
		}

		UserDataValue aggregatorColumn = (UserDataValue) row.getColumn(aggregatorColumnId + 1);

		ExecAggregator ua = (ExecAggregator) aggregatorColumn.getObject();
		if (ua == null)
		{
			ua = getAggregatorInstance();
			aggregatorColumn.setValue(ua);
		}
	}

	/**
	 * Accumulate the aggregate results.  This is the
	 * guts of the aggregation.  We will call the user aggregate
	 * on itself to do the aggregation.
	 *
	 * @param	inputRow 	the row with the input colum
	 * @param	accumulateRow 	the row with the aggregator 
	 *
	 * @exception StandardException  on error
	 */
	void accumulate(ExecRow	inputRow, 
							ExecRow	accumulateRow)
		throws StandardException
	{
		DataValueDescriptor	inputColumn = null;

		if (SanityManager.DEBUG)
		{
			SanityManager.ASSERT((inputRow != null) && (accumulateRow != null), "bad accumulate call");
		}

		DataValueDescriptor aggregatorColumn = accumulateRow.getColumn(aggregatorColumnId + 1);

		inputColumn = inputRow.getColumn(inputColumnId + 1);

		accumulate(inputColumn, aggregatorColumn);
	}

	/**
	 * Accumulate the aggregate results.  This is the
	 * guts of the aggregation.  We will call the user aggregate
	 * on itself to do the aggregation.
	 *
	 * @param	inputRow 	the row with the input colum
	 * @param	accumulateRow 	the row with the aggregator 
	 *
	 * @exception StandardException  on error
	 */
	void accumulate(Object[]	inputRow, 
							Object[]	accumulateRow)
		throws StandardException
	{
		DataValueDescriptor	inputColumn = null;

		if (SanityManager.DEBUG)
		{
			SanityManager.ASSERT((inputRow != null) && (accumulateRow != null), "bad accumulate call");
		}

		DataValueDescriptor aggregatorColumn = (DataValueDescriptor) accumulateRow[aggregatorColumnId];
		inputColumn = (DataValueDescriptor) inputRow[inputColumnId];

		accumulate(inputColumn, aggregatorColumn);
	}

	/**
	 * Accumulate the aggregate results.  This is the
	 * guts of the aggregation.  We will call the user aggregate
	 * on itself to do the aggregation.
	 *
	 * @param	inputColumn 
	 * @param	aggregatorColumn
	 *
	 * @exception StandardException  on error
	 */
	void accumulate(DataValueDescriptor	inputColumn, 
							DataValueDescriptor	aggregatorColumn)
		throws StandardException
	{
		ExecAggregator		ua;

		if (SanityManager.DEBUG)
		{
			/*
			** Just to be on the safe side, confirm that we actually
			** have a Aggregator in this column.
			*/
			if (!(aggregatorColumn instanceof UserDataValue))
			{
				SanityManager.THROWASSERT("accumlator column is not a UserDataValue as "+
					"expected, it is a "+aggregatorColumn.getClass().getName());
			}
		}
		ua = (ExecAggregator) aggregatorColumn.getObject();

		/*
		** If we don't have an aggregator, then we have to
		** create one now.  This happens when the input result
		** set is null.
		*/
		if (ua == null)
		{
			ua = getAggregatorInstance();
		}
	
		ua.accumulate(inputColumn, this);
	}

	/**
	 * Merge the aggregate results.  This is the
	 * guts of the aggregation.  We will call the user aggregate
	 * on itself to do the aggregation.
	 *
	 * @param	inputRow 	the row with the input colum
	 * @param	mergeRow 	the row with the aggregator 
	 *
	 * @exception StandardException  on error
	 */
	void merge(ExecRow	inputRow, 
							ExecRow	mergeRow)
		throws StandardException
	{

		DataValueDescriptor mergeColumn = mergeRow.getColumn(aggregatorColumnId + 1);
		DataValueDescriptor inputColumn = inputRow.getColumn(aggregatorColumnId + 1);

		merge(inputColumn, mergeColumn);
	}

	/**
	 * Merge the aggregate results.  This is the
	 * guts of the aggregation.  We will call the user aggregate
	 * on itself to do the aggregation.
	 *
	 * @param	inputRow 	the row with the input colum
	 * @param	mergeRow 	the row with the aggregator 
	 *
	 * @exception StandardException  on error
	 */
	void merge(Object[]	inputRow, 
							Object[]	mergeRow)
		throws StandardException
	{
		DataValueDescriptor mergeColumn = (DataValueDescriptor) mergeRow[aggregatorColumnId];
		DataValueDescriptor inputColumn = (DataValueDescriptor) inputRow[aggregatorColumnId];

		merge(inputColumn, mergeColumn);
	}

	/**
	 * Get the results of the aggregation and put it
	 * in the result column.
	 *
	 * @param	row	the row with the result and the aggregator
	 *
	 * @exception StandardException on error
	 */
	boolean finish(ExecRow row)
		throws StandardException
	{
		DataValueDescriptor outputColumn = row.getColumn(resultColumnId + 1);
		DataValueDescriptor aggregatorColumn = row.getColumn(aggregatorColumnId + 1);
		/*
		** Just to be on the safe side, confirm that we actually
		** have a Aggregator in aggregatorColumn.
		*/
		if (SanityManager.DEBUG)
		{
			SanityManager.ASSERT(aggregatorColumn != null, "aggregatorColumn is null");
			SanityManager.ASSERT(outputColumn != null, "otuputColumn is null");
			SanityManager.ASSERT(aggregatorColumn instanceof UserDataValue,
				"accumlator column is not a UserDataValue as expected");
		}

		ExecAggregator ua = (ExecAggregator) aggregatorColumn.getObject();

		/*
		** If we don't have an aggregator, then we have to
		** create one now.  This happens when the input result
		** set is null.
		*/
		if (ua == null)
		{
			ua = getAggregatorInstance();
		}	

		/*
		**
		** We are going to copy
		** then entire DataValueDescriptor into the result column.
		** We could call setValue(result.setObject()), but we
		** might loose state (e.g. SQLBit.getObject() returns a
		** byte[] which looses the precision of the bit.  
		**
		*/
		
		DataValueDescriptor result = ua.getResult();
		if (result == null)
			outputColumn.setToNull();
		else
			outputColumn.setValue(result);

		return ua.didEliminateNulls();
	}

	/**
	 * Get a new instance of the aggregator and initialize it.
	 *
	 * @return an exec aggregator
	 *
	 * @exception StandardException on error
	 */
	ExecAggregator getAggregatorInstance()
		throws StandardException
	{
		ExecAggregator aggregatorInstance;
		if (cachedAggregator == null)
		{
			try
			{
				Class aggregatorClass = cf.loadApplicationClass(aggInfo.getAggregatorClassName());
				Object agg = aggregatorClass.newInstance();
				aggregatorInstance = (ExecAggregator)agg;
				cachedAggregator = aggregatorInstance;

				aggregatorInstance.setup
                    (
                     cf,
                     aggInfo.getAggregateName(),
                     aggInfo.getResultDescription().getColumnInfo()[ 0 ].getType()
                     );

			} catch (Exception e)
			{
				throw StandardException.unexpectedUserException(e);
			}
		}
		else
		{
			aggregatorInstance = cachedAggregator.newAggregator();
		}


		return aggregatorInstance;
	}
			
	/////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////

	/**
	 * Return the column id that is being aggregated
	 */
	int getColumnId()
	{
		// Every sort has to have at least one column.
		return aggregatorColumnId;
	}

	DataValueDescriptor getInputColumnValue(ExecRow row)
	    throws StandardException
	{
	    return row.getColumn(inputColumnId + 1);
	}

	/**
	 * Merge two partial aggregations.  This is how the
	 * sorter merges partial aggregates.
	 *
	 * @exception StandardException on error
	 */
	void merge(Storable aggregatorColumnIn,
						Storable aggregatorColumnOut)
		throws StandardException
	{
		ExecAggregator	uaIn;
		ExecAggregator	uaOut;

		if (SanityManager.DEBUG)
		{
			/*
			** Just to be on the safe side, confirm that we actually
			** have a Aggregator in this column.
			*/
			if (!(aggregatorColumnIn instanceof UserDataValue))
			{
				SanityManager.THROWASSERT("aggregatorColumnOut column is not "+
					"a UserAggreator as expected, "+
					"it is a "+aggregatorColumnIn.getClass().getName());
			}
			if (!(aggregatorColumnOut instanceof UserDataValue))
			{
				SanityManager.THROWASSERT("aggregatorColumnIn column is not"+
					" a UserAggreator as expected, "+
					"it is a "+aggregatorColumnOut.getClass().getName());
			}
		}
		uaIn = (ExecAggregator)(((UserDataValue) aggregatorColumnIn).getObject());
		uaOut = (ExecAggregator)(((UserDataValue) aggregatorColumnOut).getObject());

		uaOut.merge(uaIn);
	}

	//////////////////////////////////////////////////////
	//
	// MISC
	//
	//////////////////////////////////////////////////////
	AggregatorInfo getAggregatorInfo()
	{
		return aggInfo;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3292.java