error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2181.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2181.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2181.java
text:
```scala
u@@pdateBestPlanMap(ADD_PLAN, this);

/*

   Derby - Class org.apache.derby.impl.sql.compile.UnionNode

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

package	org.apache.derby.impl.sql.compile;

import org.apache.derby.iapi.services.compiler.MethodBuilder;

import org.apache.derby.iapi.services.sanity.SanityManager;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.sql.compile.Optimizable;
import org.apache.derby.iapi.sql.compile.OptimizablePredicate;
import org.apache.derby.iapi.sql.compile.OptimizablePredicateList;
import org.apache.derby.iapi.sql.compile.Optimizer;
import org.apache.derby.iapi.sql.compile.CostEstimate;
import org.apache.derby.iapi.sql.compile.RowOrdering;
import org.apache.derby.iapi.sql.compile.C_NodeTypes;

import org.apache.derby.iapi.sql.dictionary.ConglomerateDescriptor;

import org.apache.derby.iapi.reference.SQLState;
import org.apache.derby.iapi.reference.ClassName;

import org.apache.derby.impl.sql.compile.ActivationClassBuilder;

import org.apache.derby.iapi.types.DataTypeDescriptor;

import org.apache.derby.iapi.util.JBitSet;
import org.apache.derby.iapi.services.classfile.VMOpcode;

/**
 * A UnionNode represents a UNION in a DML statement.  It contains a boolean
 * telling whether the union operation should eliminate duplicate rows.
 *
 * @author Jeff Lichtman
 */

public class UnionNode extends SetOperatorNode
{
	/* Only optimize it once */
	/* Only call addNewNodes() once */
	private boolean addNewNodesCalled;

	/* Is this a UNION ALL generated for a table constructor -- a VALUES expression with multiple rows. */
	boolean			tableConstructor;

	/* True if this is the top node of a table constructor */
	boolean			topTableConstructor;


	/**
	 * Initializer for a UnionNode.
	 *
	 * @param leftResult		The ResultSetNode on the left side of this union
	 * @param rightResult		The ResultSetNode on the right side of this union
	 * @param all				Whether or not this is a UNION ALL.
	 * @param tableConstructor	Whether or not this is from a table constructor.
	 * @param tableProperties	Properties list associated with the table
	 *
	 * @exception StandardException		Thrown on error
	 */

	public void init(
					Object leftResult,
					Object rightResult,
					Object all,
					Object tableConstructor,
					Object tableProperties)
			throws StandardException
	{
		super.init(leftResult, rightResult, all, tableProperties);

		/* Is this a UNION ALL for a table constructor? */
		this.tableConstructor = ((Boolean) tableConstructor).booleanValue();
    } // end of init

	/**
	 * Mark this as the top node of a table constructor.
	 */
	public void markTopTableConstructor()
	{
		topTableConstructor = true;
	}

	/**
	 * Tell whether this is a UNION for a table constructor.
	 */
	boolean tableConstructor()
	{
		return tableConstructor;
	}

	/**
	 * Check for (and reject) ? parameters directly under the ResultColumns.
	 * This is done for SELECT statements.  Don't reject parameters that
	 * are in a table constructor - these are allowed, as long as the
	 * table constructor is in an INSERT statement or each column of the
	 * table constructor has at least one non-? column.  The latter case
	 * is checked below, in bindExpressions().
	 *
	 * @return	Nothing
	 *
	 * @exception StandardException		Thrown if a ? parameter found
	 *									directly under a ResultColumn
	 */
	public void rejectParameters() throws StandardException
	{
		if ( ! tableConstructor())
			super.rejectParameters();
	}

	/**
	 * Set the type of column in the result column lists of each
	 * source of this union tree to the type in the given result column list
	 * (which represents the result columns for an insert).
	 * This is only for table constructors that appear in insert statements.
	 *
	 * @param typeColumns	The ResultColumnList containing the desired result
	 *						types.
	 *
	 * @exception StandardException		Thrown on error
	 */
	void setTableConstructorTypes(ResultColumnList typeColumns)
			throws StandardException
	{
		if (SanityManager.DEBUG)
		{
			SanityManager.ASSERT(resultColumns.size() <= typeColumns.size(),
				"More columns in ResultColumnList than in base table.");
		}

		ResultSetNode	rsn;

		/*
		** Should only set types of ? parameters to types of result columns
		** if it's a table constructor.
		*/
		if (tableConstructor())
		{
			/* By looping through the union nodes, we avoid recursion */
			for (rsn = this; rsn instanceof UnionNode; )
			{
				UnionNode union = (UnionNode) rsn;

				/*
				** Assume that table constructors are left-deep trees of UnionNodes
				** with RowResultSet nodes on the right.
				*/
				if (SanityManager.DEBUG)
					SanityManager.ASSERT(
						union.rightResultSet instanceof RowResultSetNode,
						"A " + union.rightResultSet.getClass().getName() +
						" is on the right of a union in a table constructor");

				((RowResultSetNode) union.rightResultSet).setTableConstructorTypes(
																typeColumns);

				rsn = union.leftResultSet;
			}

			/* The last node on the left should be a result set node */
			if (SanityManager.DEBUG)
				SanityManager.ASSERT(rsn instanceof RowResultSetNode,
					"A " + rsn.getClass().getName() +
					" is at the left end of a table constructor");

			((RowResultSetNode) rsn).setTableConstructorTypes(typeColumns);
		}
	}

	/*
	 *  Optimizable interface
	 */

	/**
	 * @see org.apache.derby.iapi.sql.compile.Optimizable#optimizeIt
	 *
	 * @exception StandardException		Thrown on error
	 */
	public CostEstimate optimizeIt(Optimizer optimizer,
							OptimizablePredicateList predList,
							CostEstimate outerCost,
							RowOrdering rowOrdering)
			throws StandardException
	{
		/*
		** RESOLVE: Most types of Optimizables only implement estimateCost(),
		** and leave it up to optimizeIt() in FromTable to figure out the
		** total cost of the join.  For unions, though, we want to figure out
		** the best plan for the sources knowing how many outer rows there are -
		** it could affect their strategies significantly.  So we implement
		** optimizeIt() here, which overrides the optimizeIt() in FromTable.
		** This assumes that the join strategy for which this union node is
		** the inner table is a nested loop join, which will not be a valid
		** assumption when we implement other strategies like materialization
		** (hash join can work only on base tables).
		*/

		/* optimize() both resultSets */

		// If we have predicates from an outer block, we want to try
		// to push them down to this node's children.  However, we can't
		// just push the predicates down as they are; instead, we
		// need to scope them for the child result sets first, and
		// then push the scoped versions.  This is all done in the
		// call to pushOptPredicate() here; for more, see that method's
		// definition in SetOperatorNode.  NOTE: If we're considering a
		// hash join then we do not push the predicates because we'll
		// need the predicates to be at this level in order to find
		// out if one of them is an equijoin predicate that can be used
		// for the hash join.
		if ((predList != null) &&
			!getCurrentAccessPath().getJoinStrategy().isHashJoin())
		{
			for (int i = predList.size() - 1; i >= 0; i--) {
				if (pushOptPredicate(predList.getOptPredicate(i)))
					predList.removeOptPredicate(i);
			}
		}

		// It's possible that a call to optimize the left/right will cause
		// a new "truly the best" plan to be stored in the underlying base
		// tables.  If that happens and then we decide to skip that plan
		// (which we might do if the call to "considerCost()" below decides
		// the current path is infeasible or not the best) we need to be
		// able to revert back to the "truly the best" plans that we had
		// saved before we got here.  So with this next call we save the
		// current plans using "this" node as the key.  If needed, we'll
		// then make the call to revert the plans in OptimizerImpl's
		// getNextDecoratedPermutation() method.
		addOrLoadBestPlanMapping(true, this);

		leftResultSet = optimizeSource(
							optimizer,
							leftResultSet,
							getLeftOptPredicateList(),
							outerCost);

		rightResultSet = optimizeSource(
							optimizer,
							rightResultSet,
							getRightOptPredicateList(),
							outerCost);

		CostEstimate costEstimate = getCostEstimate(optimizer);

		/* The cost is the sum of the two child costs */
		costEstimate.setCost(leftResultSet.getCostEstimate().getEstimatedCost(),
							 leftResultSet.getCostEstimate().rowCount(),
							 leftResultSet.getCostEstimate().singleScanRowCount() +
							 rightResultSet.getCostEstimate().singleScanRowCount());

		costEstimate.add(rightResultSet.costEstimate, costEstimate);

		/*
		** Get the cost of this result set in the context of the whole plan.
		*/
		getCurrentAccessPath().
			getJoinStrategy().
				estimateCost(
							this,
							predList,
							(ConglomerateDescriptor) null,
							outerCost,
							optimizer,
							costEstimate
							);

		optimizer.considerCost(this, predList, costEstimate, outerCost);

		return costEstimate;
	}

	/**
	 * DERBY-649: Handle pushing predicates into UnionNodes. It is possible to push
	 * single table predicates that are binaryOperations or inListOperations. 
	 *
	 * Predicates of the form <columnReference> <RELOP> <constant> or <columnReference>
	 * IN <constantList> are currently handled. Since these predicates would allow
	 * optimizer to pick available indices, pushing them provides maximum benifit.
	 *
	 * It should be possible to expand this logic to cover more cases. Even pushing
	 * expressions (like a+b = 10) into SELECTs would improve performance, even if
	 * they don't allow use of index. It would mean evaluating expressions closer to
	 * data and hence could avoid sorting or other overheads that UNION may require.
	 *
	 * Note that the predicates are not removed after pushing. This is to ensure if
	 * pushing is not possible or only partially feasible.
	 *
	 * @param 	predicateList		List of single table predicates to push
	 *
	 * @return	Nothing
	 *
	 * @exception	StandardException		Thrown on error
	 */
	public void pushExpressions(PredicateList predicateList)
					throws StandardException
	{
		// If left or right side is a UnionNode, further push the predicate list
		// Note, it is OK not to push these predicates since they are also evaluated
		// in the ProjectRestrictNode. There are other types of operations possible
		// here in addition to UnionNode or SelectNode, like RowResultSetNode.
		if (leftResultSet instanceof UnionNode)
			((UnionNode)leftResultSet).pushExpressions(predicateList);
		else if (leftResultSet instanceof SelectNode)
			predicateList.pushExpressionsIntoSelect((SelectNode)leftResultSet, true);

		if (rightResultSet instanceof UnionNode)
			((UnionNode)rightResultSet).pushExpressions(predicateList);
		else if (rightResultSet instanceof SelectNode)
			predicateList.pushExpressionsIntoSelect((SelectNode)rightResultSet, true);
	}

	/**
	 * @see Optimizable#modifyAccessPath
	 *
	 * @exception StandardException		Thrown on error
	 */
	public Optimizable modifyAccessPath(JBitSet outerTables) throws StandardException
	{
		Optimizable retOptimizable;
		retOptimizable = super.modifyAccessPath(outerTables);

		/* We only want call addNewNodes() once */
		if (addNewNodesCalled)
		{
			return retOptimizable;
		}
		return (Optimizable) addNewNodes();
	}

	/**
	 * @see ResultSetNode#modifyAccessPaths
	 *
	 * @exception StandardException		Thrown on error
	 */
	public ResultSetNode modifyAccessPaths() throws StandardException
	{
		ResultSetNode retRSN;
		retRSN = super.modifyAccessPaths();

		/* We only want call addNewNodes() once */
		if (addNewNodesCalled)
		{
			return retRSN;
		}
		return addNewNodes();
	}

	/**
	 * Add any new ResultSetNodes that are necessary to the tree.
	 * We wait until after optimization to do this in order to
	 * make it easier on the optimizer.
	 *
	 * @return (Potentially new) head of the ResultSetNode tree.
	 *
	 * @exception StandardException		Thrown on error
	 */
	private ResultSetNode addNewNodes()
		throws StandardException
	{
		ResultSetNode treeTop = this;

		/* Only call addNewNodes() once */
		if (addNewNodesCalled)
		{
			return this;
		}

		addNewNodesCalled = true;

		/* RESOLVE - We'd like to generate any necessary NormalizeResultSets
		 * above our children here, in the tree.  However, doing so causes
		 * the following query to fail because the where clause goes against
		 * the NRS instead of the Union:
		 *		SELECT TABLE_TYPE
		 *		FROM SYS.SYSTABLES, 
		 *			(VALUES ('T','TABLE') ,
		 *				('S','SYSTEM TABLE') , ('V', 'VIEW')) T(TTABBREV,TABLE_TYPE) 
		 *		WHERE TTABBREV=TABLETYPE;
		 * Thus, we are forced to skip over generating the nodes in the tree
		 * and directly generate the execution time code in generate() instead.
		 * This solves the problem for some unknown reason.
		 */

		/* Simple solution (for now) to eliminating duplicates - 
		 * generate a distinct above the union.
		 */
		if (! all)
		{
			/* We need to generate a NormalizeResultSetNode above us if the column
			 * types and lengths don't match.  (We need to do it here, since they
			 * will end up agreeing in the PRN, which will be the immediate
			 * child of the DistinctNode, which means that the NormalizeResultSet
			 * won't get generated above the PRN.)
			 */
			if (! columnTypesAndLengthsMatch())
			{
				treeTop = genNormalizeResultSetNode(this, false);	
			}

			treeTop = (ResultSetNode) getNodeFactory().getNode(
							C_NodeTypes.DISTINCT_NODE,
							treeTop.genProjectRestrict(),
							Boolean.FALSE,
							tableProperties,
							getContextManager());
			/* HACK - propagate our table number up to the new DistinctNode
			 * so that arbitrary hash join will work correctly.  (Otherwise it
			 * could have a problem dividing up the predicate list at the end
			 * of modifyAccessPath() because the new child of the PRN above
			 * us would have a tableNumber of -1 instead of our tableNumber.)
			 */
			((FromTable)treeTop).setTableNumber(tableNumber);
			treeTop.setReferencedTableMap((JBitSet) referencedTableMap.clone());
			all = true;
		}

		/* Generate the OrderByNode if a sort is still required for
		 * the order by.
		 */
		if (orderByList != null)
		{
			treeTop = (ResultSetNode) getNodeFactory().getNode(
											C_NodeTypes.ORDER_BY_NODE,
											treeTop,
											orderByList,
											tableProperties,
											getContextManager());
		}
		return treeTop;
	}

	/**
	 * Convert this object to a String.  See comments in QueryTreeNode.java
	 * for how this should be done for tree printing.
	 *
	 * @return	This object as a String
	 */

	public String toString()
	{
		if (SanityManager.DEBUG)
		{
			return 	"tableConstructor: " + tableConstructor + "\n" + super.toString();
		}
		else
		{
			return "";
		}
	}

	/**
	 * Bind the expressions under this TableOperatorNode.  This means
	 * binding the sub-expressions, as well as figuring out what the
	 * return type is for each expression.
	 *
	 * @return	Nothing
	 *
	 * @exception StandardException		Thrown on error
	 */

	public void bindExpressions(FromList fromListParam)
				throws StandardException
	{
		super.bindExpressions(fromListParam);

		/*
		** Each ? parameter in a table constructor that is not in an insert
		** statement takes its type from the first non-? in its column
		** of the table constructor.  It's an error to have a column that
		** has all ?s.  Do this only for the top of the table constructor
		** list - we don't want to do this for every level of union node
		** in the table constructor.  Also, don't do this for an INSERT -
		** the types of the ? parameters come from the columns being inserted
		** into in that case.
		*/
		if (topTableConstructor && ( ! insertSource) )
		{
			/*
			** Step through all the rows in the table constructor to
			** get the type of the first non-? in each column.
			*/
			DataTypeDescriptor[] types =
				new DataTypeDescriptor[leftResultSet.getResultColumns().size()];
			
			ResultSetNode rsn;
			int numTypes = 0;

			/* By looping through the union nodes, we avoid recursion */
			for (rsn = this; rsn instanceof SetOperatorNode; )
			{
				SetOperatorNode		setOperator = (SetOperatorNode) rsn;

				/*
				** Assume that table constructors are left-deep trees of
				** SetOperatorNodes with RowResultSet nodes on the right.
				*/
				if (SanityManager.DEBUG)
					SanityManager.ASSERT(
					 setOperator.rightResultSet instanceof RowResultSetNode,
					 "A " + setOperator.rightResultSet.getClass().getName() +
					 " is on the right side of a setOperator in a table constructor");

				RowResultSetNode	rrsn =
										(RowResultSetNode) setOperator.rightResultSet;

				numTypes += getParamColumnTypes(types, rrsn);

				rsn = setOperator.leftResultSet;
			}

			/* The last node on the left should be a result set node */
			if (SanityManager.DEBUG)
				SanityManager.ASSERT(rsn instanceof RowResultSetNode);

			numTypes += getParamColumnTypes(types, (RowResultSetNode) rsn);

			/* Are there any columns that are all ? parameters? */
			if (numTypes < types.length)
			{
			  throw StandardException.newException(SQLState.LANG_TABLE_CONSTRUCTOR_ALL_PARAM_COLUMN);
			}

			/*
			** Loop through the nodes again. This time, look for parameter
			** nodes, and give them the type from the type array we just
			** constructed.
			*/
			for (rsn = this; rsn instanceof SetOperatorNode; )
			{
				SetOperatorNode	setOperator = (SetOperatorNode) rsn;
				RowResultSetNode rrsn = (RowResultSetNode) setOperator.rightResultSet;

				setParamColumnTypes(types, rrsn);

				rsn = setOperator.leftResultSet;
			}

			setParamColumnTypes(types, (RowResultSetNode) rsn);
		}
	}

    /**
	 * Generate the code for this UnionNode.
	 *
	 * @exception StandardException		Thrown on error
     */
	public void generate(ActivationClassBuilder acb,
								MethodBuilder mb)
							throws StandardException
	{
		/*  By the time we get here we should be a union all.
		 *  (We created a DistinctNode above us, if needed,
		 *  to eliminate the duplicates earlier.)
		 */
		if (SanityManager.DEBUG)
		{
			SanityManager.ASSERT(all,
				"all expected to be true");
		}

		/* Get the next ResultSet #, so that we can number this ResultSetNode, its
		 * ResultColumnList and ResultSet.
		 */
		assignResultSetNumber();

		// Get our final cost estimate based on the child estimates.
		costEstimate = getFinalCostEstimate();

		// build up the tree.

		acb.pushGetResultSetFactoryExpression(mb); // instance for getUnionResultSet


		/* Generate the left and right ResultSets */
		leftResultSet.generate(acb, mb);

		/* Do we need a NormalizeResultSet above the left ResultSet? */
		if (! resultColumns.isExactTypeAndLengthMatch(leftResultSet.getResultColumns()))
		{
			acb.pushGetResultSetFactoryExpression(mb);
			mb.swap();
			generateNormalizationResultSet(acb, mb, 
													getCompilerContext().getNextResultSetNumber(),
													makeResultDescription()
													);
		}

		rightResultSet.generate(acb, mb);

		/* Do we need a NormalizeResultSet above the right ResultSet? */
		if (! resultColumns.isExactTypeAndLengthMatch(rightResultSet.getResultColumns()))
		{
			acb.pushGetResultSetFactoryExpression(mb);
			mb.swap();
			generateNormalizationResultSet(acb, mb,
													getCompilerContext().getNextResultSetNumber(),
													makeResultDescription()
													);
		}

		/* Generate the UnionResultSet:
		 *	arg1: leftExpression - Expression for leftResultSet
		 *	arg2: rightExpression - Expression for rightResultSet
		 *  arg3: Activation
		 *  arg4: resultSetNumber
		 *  arg5: estimated row count
		 *  arg6: estimated cost
		 *  arg7: close method
		 */


		acb.pushThisAsActivation(mb);
		mb.push(resultSetNumber);
		mb.push(costEstimate.rowCount());
		mb.push(costEstimate.getEstimatedCost());
		closeMethodArgument(acb, mb);

		mb.callMethod(VMOpcode.INVOKEINTERFACE, (String) null, "getUnionResultSet", ClassName.NoPutResultSet, 7);
	}

	/**
	 * @see ResultSetNode#getFinalCostEstimate
	 *
	 * Get the final CostEstimate for this UnionNode.
	 *
	 * @return	The final CostEstimate for this UnionNode, which is
	 *  the sum of the two child costs.
	 */
	public CostEstimate getFinalCostEstimate()
		throws StandardException
	{
		// If we already found it, just return it.
		if (finalCostEstimate != null)
			return finalCostEstimate;

		CostEstimate leftCE = leftResultSet.getFinalCostEstimate();
		CostEstimate rightCE = rightResultSet.getFinalCostEstimate();

		finalCostEstimate = getNewCostEstimate();
		finalCostEstimate.setCost(leftCE.getEstimatedCost(),
							 leftCE.rowCount(),
							 leftCE.singleScanRowCount() +
							 rightCE.singleScanRowCount());

		finalCostEstimate.add(rightCE, finalCostEstimate);
		return finalCostEstimate;
	}

    String getOperatorName()
    {
        return "UNION";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2181.java