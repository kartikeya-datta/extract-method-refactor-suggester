error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6234.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6234.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6234.java
text:
```scala
i@@f (telegram.getTimestamp() < currentTime) break;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.ai.msg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.Agent;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

/** The MessageDispatcher is a singleton in charge of the creation, dispatch, and management of telegrams.
 * 
 * @author davebaol */
public class MessageDispatcher {

	private static final String LOG_TAG = MessageDispatcher.class.getSimpleName();

	private static final float NANOS_PER_SEC = 1000000000f;

	private static final MessageDispatcher instance = new MessageDispatcher();

	private static final long START = TimeUtils.nanoTime();

	private PriorityQueue<Telegram> queue = new PriorityQueue<Telegram>();

	private final Pool<Telegram> pool;

	private long timeGranularity;

	private boolean debugEnabled;

	/** Don't let anyone else instantiate this class */
	private MessageDispatcher () {
		this.pool = new Pool<Telegram>(64) {
			protected Telegram newObject () {
				return new Telegram();
			}
		};
		setTimeGranularity(0.25f);
	}

	/** Returns the singleton instance of the message dispatcher. */
	public static MessageDispatcher getInstance () {
		return instance;
	}

	/** Returns the current time in nanoseconds.
	 * <p>
	 * This implementation returns the value of the system timer minus a constant value determined when this class was loaded the
	 * first time in order to ensure it takes increasing values (for 2 ^ 63 nanoseconds, i.e. 292 years) since the time stamp is
	 * used to order the telegrams in the queue. */
	public static long getCurrentTime () {
		return TimeUtils.nanoTime() - START;
	}

	/** Returns the time granularity. */
	public float getTimeGranularity () {
		return timeGranularity / NANOS_PER_SEC;
	}

	/** Sets the time granularity. Delayed telegrams having the same sender, recipient and message type are considered identical
	 * when they belong to the same time slot. If time granularity is greater than 0 identical telegrams are not doubled into the
	 * queue. This prevents many similar telegrams from bunching up in the queue and being delivered en masse, thus flooding an
	 * agent with identical messages. To eliminate time granularity just set it to 0. */
	public void setTimeGranularity (float timeGranularity) {
		boolean uniqueness = timeGranularity <= 0;
		this.timeGranularity = uniqueness ? 0 : (long)(timeGranularity * NANOS_PER_SEC);
		this.queue.setUniqueness(uniqueness);

	}

	/** Returns true if debug mode is on; false otherwise. */
	public boolean isDebugEnabled () {
		return debugEnabled;
	}

	/** Sets debug mode on/off. */
	public void setDebugEnabled (boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	/** Removes all the telegrams from the queue and releases them to the internal pool. */
	public void clear () {
		for (int i = 0; i < queue.size(); i++) {
			pool.free(queue.get(i));
		}
		queue.clear();
	}

	/** Given a message, a receiver, a sender and any time delay, this method routes the message to the correct agent (if no delay)
	 * or stores in the message queue to be dispatched at the correct time. */
	public void dispatchMessage (float delay, Agent sender, Agent receiver, int msg, Object extraInfo) {

		// Make sure the receiver is valid
		if (receiver == null) {
			if (debugEnabled) {
				Gdx.app.log(LOG_TAG, "Warning! No Receiver specified");
			}
			return;
		}

		// Get a telegram from the pool
		Telegram telegram = pool.obtain();
		telegram.sender = sender;
		telegram.receiver = receiver;
		telegram.message = msg;
		telegram.extraInfo = extraInfo;

		// If there is no delay, route telegram immediately
		if (delay <= 0.0f) {
			if (debugEnabled)
				Gdx.app.log(LOG_TAG, "Instant telegram dispatched at time: " + getCurrentTime() + " by " + sender + " for "
					+ receiver + ". Msg is " + msg);

			// Send the telegram to the recipient
			discharge(telegram);
		} else {
			// Set the timestamp for the delayed telegram
			long currentTime = getCurrentTime();
			telegram.setTimestamp(currentTime + (long)(delay * NANOS_PER_SEC), timeGranularity);

			// Put it in the queue
			queue.add(telegram);

			if (debugEnabled) {
				Gdx.app.log(LOG_TAG, "Delayed telegram from " + sender + " recorded at time " + getCurrentTime() + " for " + receiver
					+ ". Msg is " + msg);
			}
		}
	}

	/** Dispatches any telegrams with a timestamp that has expired. Any dispatched telegrams are removed from the queue.
	 * <p>
	 * This method must be called each time through the main game loop. */
	public void dispatchDelayedMessages () {
		if (queue.size() == 0) return;

		// Get current time
		long currentTime = getCurrentTime();

		// Now peek at the queue to see if any telegrams need dispatching.
		// remove all telegrams from the front of the queue that have gone
		// past their time stamp.
		do {
			// Read the telegram from the front of the queue
			final Telegram telegram = queue.peek();
			if (telegram.getTimestamp() > currentTime) break;

			if (debugEnabled) {
				Gdx.app.log(LOG_TAG, "Queued telegram ready for dispatch: Sent to " + telegram.receiver + ". Msg is "
					+ telegram.message);
			}

			// Send the telegram to the recipient
			discharge(telegram);

			// Remove it from the queue
			queue.poll();
		} while (queue.size() > 0);

	}

	/** This method is used by DispatchMessage or DispatchDelayedMessages. This method calls the message handling member function of
	 * the receiving agent with the newly created telegram. */
	private void discharge (Telegram telegram) {
		if (!telegram.receiver.handleMessage(telegram)) {
			// Telegram could not be handled
			if (debugEnabled) Gdx.app.log(LOG_TAG, "Message not handled");
		}

		pool.free(telegram);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6234.java