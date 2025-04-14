error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1655.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1655.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1655.java
text:
```scala
M@@essage message = rowMutationMessage.makeRowMutationMessage(StorageService.readRepairVerbHandler_);

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.*;

import org.apache.cassandra.db.Column;
import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.RowMutation;
import org.apache.cassandra.db.RowMutationMessage;
import org.apache.cassandra.db.SuperColumn;
import org.apache.cassandra.net.EndPoint;
import org.apache.cassandra.net.Header;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.utils.Cachetable;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.ICacheExpungeHook;
import org.apache.cassandra.utils.ICachetable;
import org.apache.cassandra.utils.LogUtil;
import org.apache.log4j.Logger;



/*
 * This class manages the read repairs . This is a singleton class
 * it basically uses the cache table construct to schedule writes that have to be 
 * made for read repairs. 
 * A cachetable is created which wakes up every n  milliseconds specified by 
 * expirationTimeInMillis and calls a global hook fn on pending entries 
 * This fn basically sends the message to the appropriate servers to update them
 * with the latest changes.
 * Author : Avinash Lakshman ( alakshman@facebook.com) & Prashant Malik ( pmalik@facebook.com )
 */
class ReadRepairManager
{
    private static Logger logger_ = Logger.getLogger(ReadRepairManager.class);
	private static final long expirationTimeInMillis = 2000;
	private static Lock lock_ = new ReentrantLock();
	private static ReadRepairManager self_ = null;

	/*
	 * This is the internal class which actually
	 * implements the global hook fn called by the readrepair manager
	 */
	static class ReadRepairPerformer implements
			ICacheExpungeHook<String, Message>
	{
		/*
		 * The hook fn which takes the end point and the row mutation that 
		 * needs to be sent to the end point in order 
		 * to perform read repair.
		 */
		public void callMe(String target,
				Message message)
		{
			String[] pieces = FBUtilities.strip(target, ":");
			EndPoint to = new EndPoint(pieces[0], Integer.parseInt(pieces[1]));
			MessagingService.getMessagingInstance().sendOneWay(message, to);			
		}

	}

	private ICachetable<String, Message> readRepairTable_ = new Cachetable<String, Message>(expirationTimeInMillis, new ReadRepairManager.ReadRepairPerformer());

	protected ReadRepairManager()
	{

	}

	public  static ReadRepairManager instance()
	{
		if (self_ == null)
		{
            lock_.lock();
            try
            {
                if ( self_ == null )
                    self_ = new ReadRepairManager();
            }
            finally
            {
                lock_.unlock();
            }
		}
		return self_;
	}

	/*
	 * Schedules a read repair.
	 * @param target endpoint on whcih the read repair should happen
	 * @param rowMutationMessage the row mutation message that has the repaired row.
	 */
	public void schedule(EndPoint target, RowMutationMessage rowMutationMessage)
	{
        try
        {
            Message message = RowMutationMessage.makeRowMutationMessage(rowMutationMessage, StorageService.readRepairVerbHandler_);
    		String key = target + ":" + message.getMessageId();
    		readRepairTable_.put(key, message);
        }
        catch ( IOException ex )
        {
            logger_.error(LogUtil.throwableToString(ex));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1655.java