error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3634.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3634.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3634.java
text:
```scala
t@@hrow StandardException.newException(SQLState.LANG_INVALID_COL_REF_GROUPED_SELECT_LIST, cr.getSQLColumnName());

/*

   Derby - Class org.apache.derby.impl.sql.compile.VerifyAggregateExpressionsVisitor

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

package	org.apache.derby.impl.sql.compile;

import org.apache.derby.iapi.sql.compile.Visitable; 
import org.apache.derby.iapi.sql.compile.Visitor;

import org.apache.derby.iapi.reference.SQLState;
import org.apache.derby.iapi.error.StandardException;

/**
 * If a RCL (SELECT list) contains an aggregate, then we must verify
 * that the RCL (SELECT list) is valid.  
 * For ungrouped queries,
 * the RCL must be composed entirely of valid aggregate expressions -
 * in this case, no column references outside of an aggregate.
 * For grouped aggregates,
 * the RCL must be composed of grouping columns or valid aggregate
 * expressions - in this case, the only column references allowed outside of
 * an aggregate are grouping columns.
 *
 */
public class VerifyAggregateExpressionsVisitor implements Visitor
{
	private GroupByList groupByList;

	public VerifyAggregateExpressionsVisitor(GroupByList groupByList)
	{
		this.groupByList = groupByList;
	}


	////////////////////////////////////////////////
	//
	// VISITOR INTERFACE
	//
	////////////////////////////////////////////////

	/**
	 * Verify that this expression is ok
	 * for an aggregate query.  
	 *
	 * @param node 	the node to process
	 *
	 * @return me
	 *
	 * @exception StandardException on ColumnReference not
	 * 	in group by list, ValueNode or	
	 * 	JavaValueNode that isn't under an
	 * 	aggregate
	 */
	public Visitable visit(Visitable node)
		throws StandardException
	{
		if (node instanceof ColumnReference)
		{
			ColumnReference cr = (ColumnReference)node;
		
			if (groupByList == null)
			{
				throw StandardException.newException(SQLState.LANG_INVALID_COL_REF_NON_GROUPED_SELECT_LIST, cr.getSQLColumnName());
			}

			if (groupByList.findGroupingColumn(cr) == null)
			{
				throw StandardException.newException(SQLState.LANG_INVALID_GROUPED_SELECT_LIST);
			}
		} 
		
		/*
		** Subqueries are only valid if they do not have
		** correlations and are expression subqueries.  RESOLVE:
		** this permits VARIANT expressions in the subquery --
		** should this be allowed?  may be confusing to
		** users to complain about:
		**
		**	select max(x), (select sum(y).toString() from y) from x
		*/
		else if (node instanceof SubqueryNode)
		{
			SubqueryNode subq = (SubqueryNode)node;
		
			if ((subq.getSubqueryType() != SubqueryNode.EXPRESSION_SUBQUERY) ||
				 subq.hasCorrelatedCRs())
			{
				throw StandardException.newException( (groupByList == null) ?
							SQLState.LANG_INVALID_NON_GROUPED_SELECT_LIST :
							SQLState.LANG_INVALID_GROUPED_SELECT_LIST);
			}

			/*
			** TEMPORARY RESTRICTION: we cannot handle an aggregate
			** in the subquery 
			*/
			HasNodeVisitor visitor = new HasNodeVisitor(AggregateNode.class);
			subq.accept(visitor);
			if (visitor.hasNode())
			{	
				throw StandardException.newException( (groupByList == null) ?
							SQLState.LANG_INVALID_NON_GROUPED_SELECT_LIST :
							SQLState.LANG_INVALID_GROUPED_SELECT_LIST);
			}
		}
		return node;
	}

	/**
	 * Don't visit children under an aggregate, subquery or any node which
	 * is equivalent to any of the group by expressions.
	 *
	 * @param node 	the node to process
	 *
	 * @return true/false
	 * @throws StandardException 
	 */
	public boolean skipChildren(Visitable node) throws StandardException 
	{
		return ((node instanceof AggregateNode) ||
				(node instanceof SubqueryNode) ||
				(node instanceof ValueNode &&
						groupByList != null 
						&& groupByList.findGroupingColumn((ValueNode)node) != null));
	}
	
	public boolean stopTraversal()
	{
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3634.java