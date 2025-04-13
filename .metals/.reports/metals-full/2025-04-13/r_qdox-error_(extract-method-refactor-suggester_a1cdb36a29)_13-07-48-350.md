error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/653.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/653.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,12]

error in qdox parser
file content:
```java
offset: 12
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/653.java
text:
```scala
@exception S@@tandardException Standard Derby policy.

/*

   Derby - Class org.apache.derby.iapi.store.raw.Undoable

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

package org.apache.derby.iapi.store.raw;

import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.services.io.LimitObjectInput;
import java.io.IOException;

/**
	An Undoable operation is an operation that changed the state of the RawStore
	in the context of a transaction and this change can be rolled back.

	@see Transaction#logAndDo
	@see Compensation
*/

public interface Undoable extends Loggable {


	/**
		Generate a loggable which will undo this change, using the optional
		input if necessary.

		<P><B>NOTE</B><BR>Any logical undo logic must be hidden behind generateUndo.
		During recovery redo, it should not depend on any logical undo logic.

		<P>
		There are 3 ways to implement a redo-only log record:
		<NL>
		<LI>Make the log record a Loggable instead of an Undoable, this is the
		cleanest method.
		<LI>If you want to extend a log operation class that is an Undoable,
		you can then either have generateUndo return null - this is preferred -
		(the log operation's undoMe should never be called, so you can put a
		null body there if the super class you are extending does not implement
		a undoMe).
		<LI>Or, have undoMe do nothing - this is least preferred.
		</NL>

		<P>Any resource (e.g., latched page) that is needed for the
		undoable.undoMe() must be acquired in undoable.generateUndo().
		Moreover, that resource must be identified in the compensation
		operation, and reacquired in the compensation.needsRedo() method during
		recovery redo.
		<BR><B>If you do write your own generateUndo or needsRedo, any
		resource you latch or acquire, you must release them in
		Compensation.doMe() or in Compensation.releaseResource().</B>

		<P> To write a generateUndo operation, find the object that needs to be
		rolled back.  Assuming that it is a page, latch it, put together a
		Compensation operation with the undoOp set to this operation, and save
		the page number in the compensation operation, then
		return the Compensation operation to the logging system.

		<P>
		The sequence of events in a rollback of a undoable operation is
		<NL>
		<LI> The logging system calls undoable.generateUndo.  If this returns
		null, then there is nothing to undo.
		<LI> If generateUndo returns a Compensation operation, then the logging
		system will log the Compensation log record and call
		Compenstation.doMe().  (Hopefully, this just calls the undoable's
		undoMe)
		<LI> After the Compensation operation has been applied, the logging
		system will call compensation.releaseResource(). If you do overwrite a
		super class's releaseResource(), it would be prudent to call
		super.releaseResource() first.
		</NL>

		<P> The available() method of in indicates how much data can be read, i.e.
		how much was originally written.

		@param xact	the transaction doing the rollback
		@return the compensation operation that will rollback this change, or
		null if nothing to undo. 

		@exception IOException Can be thrown by any of the methods of ObjectInput.
		@exception StandardException Standard Cloudscape policy.

		@see Loggable#releaseResource
		@see Loggable#needsRedo

	*/
	public Compensation generateUndo(Transaction xact, LimitObjectInput in)
		 throws StandardException, IOException;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/653.java