error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2265.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2265.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2265.java
text:
```scala
i@@f (subqueryNode.referencesSessionSchema())

/*

   Derby - Class org.apache.derby.impl.sql.compile.SubqueryList

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

import org.apache.derby.iapi.sql.dictionary.DataDictionary;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.services.sanity.SanityManager;

/**
 * A SubqueryList represents a list of subquerys within a specific clause 
 * (select, where or having) in a DML statement.  It extends QueryTreeNodeVector.
 *
 */

public class SubqueryList extends QueryTreeNodeVector
{
	/**
	 * Add a subquery to the list.
	 *
	 * @param subqueryNode	A SubqueryNode to add to the list
	 *
	 */

	public void addSubqueryNode(SubqueryNode subqueryNode) throws StandardException
	{
		addElement(subqueryNode);
	}

	/**
	 * Preprocess a SubqueryList.  For now, we just preprocess each SubqueryNode
	 * in the list.
	 *
	 * @param	numTables			Number of tables in the DML Statement
	 * @param	outerFromList		FromList from outer query block
	 * @param	outerSubqueryList	SubqueryList from outer query block
	 * @param	outerPredicateList	PredicateList from outer query block
	 *
	 * @exception StandardException		Thrown on error
	 */
	public void preprocess(int numTables,
							FromList outerFromList,
							SubqueryList outerSubqueryList,
							PredicateList outerPredicateList) 
				throws StandardException
	{
		SubqueryNode	subqueryNode;

		int size = size();
		for (int index = 0; index < size; index++)
		{
			subqueryNode = (SubqueryNode) elementAt(index);
			subqueryNode.preprocess(numTables, outerFromList,
									outerSubqueryList,
									outerPredicateList);
		}
	}

	/**
	 * Optimize the subqueries in this list.  
	 *
	 * @param dataDictionary	The data dictionary to use for optimization
	 * @param outerRows			The optimizer's estimate of the number of
	 *							times this subquery will be executed.
	 *
	 * @exception StandardException		Thrown on error
	 */

	public void optimize(DataDictionary dataDictionary, double outerRows)
			throws StandardException
	{
		int size = size();
		for (int index = 0; index < size; index++)
		{
			SubqueryNode	subqueryNode;
			subqueryNode = (SubqueryNode) elementAt(index);
			subqueryNode.optimize(dataDictionary, outerRows);
		}
	}

	/**
	 * Modify the access paths for all subqueries in this list.
	 *
	 * @see ResultSetNode#modifyAccessPaths
	 *
	 * @exception StandardException		Thrown on error
	 */
	public void modifyAccessPaths()
			throws StandardException
	{
		int size = size();
		for (int index = 0; index < size; index++)
		{
			SubqueryNode	subqueryNode;
			subqueryNode = (SubqueryNode) elementAt(index);
			subqueryNode.modifyAccessPaths();
		}
	}

	/**
	 * Search to see if a query references the specifed table name.
	 *
	 * @param name		Table name (String) to search for.
	 * @param baseTable	Whether or not name is for a base table
	 *
	 * @return	true if found, else false
	 *
	 * @exception StandardException		Thrown on error
	 */
	public boolean referencesTarget(String name, boolean baseTable)
		throws StandardException
	{
		int size = size();
		for (int index = 0; index < size; index++)
		{
			SubqueryNode	subqueryNode;

			subqueryNode = (SubqueryNode) elementAt(index);
			if (subqueryNode.isMaterializable())
			{
				continue;
			}

			if (subqueryNode.getResultSet().referencesTarget(name, baseTable))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Return true if the node references SESSION schema tables (temporary or permanent)
	 *
	 * @return	true if references SESSION schema tables, else false
	 *
	 * @exception StandardException		Thrown on error
	 */
	public boolean referencesSessionSchema()
		throws StandardException
	{
		int size = size();
		for (int index = 0; index < size; index++)
		{
			SubqueryNode	subqueryNode;

			subqueryNode = (SubqueryNode) elementAt(index);

			if (subqueryNode.getResultSet().referencesSessionSchema())
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Set the point of attachment in all subqueries in this list.
	 *
	 * @param pointOfAttachment		The point of attachment
	 *
	 * @exception StandardException			Thrown on error
	 */
	public void setPointOfAttachment(int pointOfAttachment)
		throws StandardException
	{
		int size = size();

		for (int index = 0; index < size; index++)
		{
			SubqueryNode	subqueryNode;

			subqueryNode = (SubqueryNode) elementAt(index);
			subqueryNode.setPointOfAttachment(pointOfAttachment);
		}
	}

	/**
	 * Decrement (query block) level (0-based) for 
	 * all of the tables in this subquery list.
	 * This is useful when flattening a subquery.
	 *
	 * @param decrement	The amount to decrement by.
	 */
	void decrementLevel(int decrement)
	{
		int size = size();

		for (int index = 0; index < size; index++)
		{
			((SubqueryNode) elementAt(index)).getResultSet().decrementLevel(decrement);
		}
	}

	/**
     * Mark all of the subqueries in this 
     * list as being part of a having clause,
     * so we can avoid flattenning later.
	 * 
	 */
	public void markHavingSubqueries() {
	    int size = size();
	    
	    for (int index = 0; index < size; index++)
	    {
	        SubqueryNode    subqueryNode;

	        subqueryNode = (SubqueryNode) elementAt(index);
	        subqueryNode.setHavingSubquery(true);
	    }
	}

	/**
	 * Mark all of the subqueries in this list as being part of a where clause
	 * so we can avoid flattening later if needed.
	 */
	public void markWhereSubqueries() {
		int size = size();
		for (int index = 0; index < size; index++)
		{
			SubqueryNode    subqueryNode;

			subqueryNode = (SubqueryNode) elementAt(index);
			subqueryNode.setWhereSubquery(true);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2265.java