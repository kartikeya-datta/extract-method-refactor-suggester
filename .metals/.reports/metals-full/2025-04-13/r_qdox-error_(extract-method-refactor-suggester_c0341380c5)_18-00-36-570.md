error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/11.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/11.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/11.java
text:
```scala
i@@f (currentState != null) currentState.enter(owner);

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

package com.badlogic.gdx.ai.fsm;

import com.badlogic.gdx.ai.msg.Telegram;

/** Default implementation of the {@link StateMachine} interface.
 * 
 * @param <E> is the type of the entity handled by this state machine
 * 
 * @author davebaol */
public class DefaultStateMachine<E> implements StateMachine<E> {

	/** The entity that owns this state machine. */
	protected E owner;

	/** The current state the owner is in. */
	protected State<E> currentState;

	/** The last state the owner was in. */
	private State<E> previousState;

	/** The global state of the owner. Its logic is called every time the FSM is updated. */
	protected State<E> globalState;

	/** Creates a DefaultStateMachine for the specified owner.
	 * @param owner the owner of the state machine */
	public DefaultStateMachine (E owner) {
		this(owner, null, null);
	}

	/** Creates a DefaultStateMachine for the specified owner and initial state.
	 * @param owner the owner of the state machine
	 * @param initialState the initial state */
	public DefaultStateMachine (E owner, State<E> initialState) {
		this(owner, initialState, null);
	}

	/** Creates a DefaultStateMachine for the specified owner, initial state and global state.
	 * @param owner the owner of the state machine
	 * @param initialState the initial state
	 * @param globalState the global state */
	public DefaultStateMachine (E owner, State<E> initialState, State<E> globalState) {
		this.owner = owner;
		this.setInitialState(initialState);
		this.setGlobalState(globalState);
	}

	@Override
	public void setInitialState (State<E> state) {
		this.previousState = null;
		this.currentState = state;
	}

	@Override
	public void setGlobalState (State<E> state) {
		this.globalState = state;
	}

	@Override
	public State<E> getCurrentState () {
		return currentState;
	}

	@Override
	public State<E> getGlobalState () {
		return globalState;
	}

	@Override
	public State<E> getPreviousState () {
		return previousState;
	}

	/** Updates the state machine by invoking first the {@code execute} method of the global state (if any) then the {@code execute}
	 * method of the current state. */
	@Override
	public void update () {
		// Execute the global state (if any)
		if (globalState != null) globalState.update(owner);

		// Execute the current state (if any)
		if (currentState != null) currentState.update(owner);
	}

	@Override
	public void changeState (State<E> newState) {
		// Keep a record of the previous state
		previousState = currentState;

		// Call the exit method of the existing state
		if (currentState != null) currentState.exit(owner);

		// Change state to the new state
		currentState = newState;

		// Call the entry method of the new state
		currentState.enter(owner);
	}

	@Override
	public boolean revertToPreviousState () {
		if (previousState == null) {
			return false;
		}

		changeState(previousState);
		return true;
	}

	/** Indicates whether the state machine is in the given state.
	 * <p>
	 * This implementation assumes states are singletons (typically an enum) so they are compared with the {@code ==} operator
	 * instead of the {@code equals} method.
	 * 
	 * @param state the state to be compared with the current state
	 * @returns true if the current state and the given state are the same object. */
	@Override
	public boolean isInState (State<E> state) {
		return currentState == state;
	}

	/** Handles received telegrams. The telegram is first routed to the current state. If the current state does not deal with the
	 * message, it's routed to the global state's message handler.
	 * 
	 * @param telegram the received telegram
	 * @returns true if telegram has been successfully handled; false otherwise. */
	@Override
	public boolean handleMessage (Telegram telegram) {

		// First see if the current state is valid and that it can handle the message
		if (currentState != null && currentState.onMessage(owner, telegram)) {
			return true;
		}

		// If not, and if a global state has been implemented, send
		// the message to the global state
		if (globalState != null && globalState.onMessage(owner, telegram)) {
			return true;
		}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/11.java