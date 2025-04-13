error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/951.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/951.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/951.java
text:
```scala
f@@alse, null, false);

/*

   Derby - Class org.apache.derby.impl.store.raw.xact.InternalXact

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

package org.apache.derby.impl.store.raw.xact;

import org.apache.derby.iapi.reference.SQLState;

import org.apache.derby.iapi.store.raw.Transaction;

import org.apache.derby.iapi.store.raw.log.LogFactory;

import org.apache.derby.iapi.store.raw.data.DataFactory;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.services.sanity.SanityManager;

import org.apache.derby.iapi.types.DataValueFactory;

import java.io.ObjectInput;

/**

	@see Xact

*/
public class InternalXact extends Xact  
{

	/*
	** Constructor
	*/

	protected InternalXact(
    XactFactory         xactFactory, 
    LogFactory          logFactory, 
    DataFactory         dataFactory,
    DataValueFactory    dataValueFactory) 
    {
		super(
            xactFactory, logFactory, dataFactory, dataValueFactory, 
            false, null);

		// always want to hold latches & containers open past the commit/abort
		setPostComplete();
	}

	/*
	** Methods of Transaction
	*/

  
	/**
		Savepoints are not supported in internal transactions.

	    @exception StandardException  A transaction exception is thrown to 
                                      disallow savepoints.

		@see Transaction#setSavePoint
	*/
	public int setSavePoint(String name, Object kindOfSavepoint) 
        throws StandardException 
    {
		throw StandardException.newException(
                SQLState.XACT_NOT_SUPPORTED_IN_INTERNAL_XACT);
	}


	/*
	** Methods of RawTransaction
	*/
	/**
		Internal transactions don't allow logical operations.

		@exception StandardException A transaction exception is thrown to 
                                     disallow logical operations.

		@see org.apache.derby.iapi.store.raw.xact.RawTransaction#recoveryRollbackFirst
	*/
	
	 public void checkLogicalOperationOk() 
         throws StandardException 
     {
		throw StandardException.newException(
                SQLState.XACT_NOT_SUPPORTED_IN_INTERNAL_XACT);
	 }

	/**
		Yes, we do want to be rolled back first in recovery.

		@see org.apache.derby.iapi.store.raw.xact.RawTransaction#recoveryRollbackFirst
	*/
	public boolean recoveryRollbackFirst()
    {
		return true;
	}

	/*
	**	Implementation specific methods
	*/

	/**
	 * @param commitOrAbort to commit or abort
	 *
	 * @exception StandardException on error
	 */
	protected void doComplete(Integer commitOrAbort) 
        throws StandardException 
    {

		// release our latches on an abort
		// keep everything on a commit
		if (commitOrAbort.equals(ABORT))
			super.doComplete(commitOrAbort);
	}

	protected void setIdleState() 
    {

		super.setIdleState();

		// Quiesce mode never denies an internal transaction from going active, don't
		// have to worry about that
		if (countObservers() != 0)
		{
			try
			{
				super.setActiveState();
			}
			catch (StandardException se)
			{
				if (SanityManager.DEBUG)
					SanityManager.THROWASSERT("unexpected exception", se);
			}
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/951.java