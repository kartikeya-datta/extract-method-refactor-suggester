error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9968.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9968.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,4]

error in qdox parser
file content:
```java
offset: 4
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9968.java
text:
```scala
 c@@ompletionProposalKind > CompletionProposal.METHOD_NAME_REFERENCE) {

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core;

import org.eclipse.jdt.core.compiler.IProblem;

/**
 * Abstract base class for a completion requestor which is passed completion
 * proposals as they are generated in response to a code assist request.
 * <p>
 * This class is intended to be subclassed by clients.
 * </p>
 * <p>
 * The code assist engine normally invokes methods on completion
 * requestors in the following sequence:
 * <pre>
 * requestor.beginReporting();
 * requestor.accept(proposal_1);
 * requestor.accept(proposal_2);
 * ...
 * requestor.endReporting();
 * </pre>
 * If, however, the engine is unable to offer completion proposals
 * for whatever reason, <code>completionFailure</code> is called
 * with a problem object describing why completions were unavailable.
 * In this case, the sequence of calls is:
 * <pre>
 * requestor.beginReporting();
 * requestor.completionFailure(problem);
 * requestor.endReporting();
 * </pre>
 * In either case, the bracketing <code>beginReporting</code>
 * <code>endReporting</code> calls are always made.
 * </p>
 * <p>
 * The class was introduced in 3.0 as a more evolvable replacement
 * for the <code>ICompletionRequestor</code> interface.
 * </p>
 * 
 * @see ICodeAssist
 * @since 3.0
 */
public abstract class CompletionRequestor {

	/**
	 * The set of CompletionProposal kinds that this requestor
	 * ignores; <code>0</code> means the set is empty.
	 * 1 << completionProposalKind
	 */
	private int ignoreSet = 0;

	/**
	 * Creates a new completion requestor.
	 * The requestor is interested in all kinds of completion
	 * proposals; none will be ignored.
	 */
	public CompletionRequestor() {
		// do nothing
	}

	/**
	 * Returns whether the given kind of completion proposal is ignored.
	 * 
	 * @param completionProposalKind one of the kind constants declared
	 * on <code>CompletionProposal</code>
	 * @return <code>true</code> if the given kind of completion proposal
	 * is ignored by this requestor, and <code>false</code> if it is of
	 * interest
	 * @see #setIgnored(int, boolean)
	 * @see CompletionProposal#getKind()
	 */
	public final boolean isIgnored(int completionProposalKind) {
		if (completionProposalKind < CompletionProposal.ANONYMOUS_CLASS_DECLARATION
 completionProposalKind > CompletionProposal.VARIABLE_DECLARATION) {
				throw new IllegalArgumentException();
		}
		return 0 != (this.ignoreSet & (1 << completionProposalKind));
	}
	
	/**
	 * Sets whether the given kind of completion proposal is ignored.
	 * 
	 * @param completionProposalKind one of the kind constants declared
	 * on <code>CompletionProposal</code>
	 * @param ignore <code>true</code> if the given kind of completion proposal
	 * is ignored by this requestor, and <code>false</code> if it is of
	 * interest
	 * @see #isIgnored(int)
	 * @see CompletionProposal#getKind()
	 */
	public final void setIgnored(int completionProposalKind, boolean ignore) {
		if (completionProposalKind < CompletionProposal.ANONYMOUS_CLASS_DECLARATION
 completionProposalKind > CompletionProposal.VARIABLE_DECLARATION) {
				throw new IllegalArgumentException();
		}
		if (ignore) {
			this.ignoreSet |= (1 << completionProposalKind);
		} else {
			this.ignoreSet &= ~(1 << completionProposalKind);
		}
	}
	
	/**
	 * Pro forma notification sent before reporting a batch of
	 * completion proposals.
	 * <p>
	 * The default implementation of this method does nothing.
	 * Clients may override.
	 * </p>
	 */
	public void beginReporting() {
		// do nothing
	}

	/**
	 * Pro forma notification sent after reporting a batch of
	 * completion proposals.
	 * <p>
	 * The default implementation of this method does nothing.
	 * Clients may override.
	 * </p>
	 */
	public void endReporting() {
		// do nothing
	}

	/**
	 * Notification of failure to produce any completions.
	 * The problem object explains what prevented completing.
	 * <p>
	 * The default implementation of this method does nothing.
	 * Clients may override to receive this kind of notice.
	 * </p>
	 * 
	 * @param problem the problem object
	 */
	public void completionFailure(IProblem problem) {
		// default behavior is to ignore
	}

	/**
	 * Proposes a completion. Has no effect if the kind of proposal
	 * is being ignored by this requestor. Callers should consider
	 * checking {@link #isIgnored(int)} before avoid creating proposal
	 * objects that would only be ignored.
	 * <p>
	 * Similarly, implementers should check 
	 * {@link #isIgnored(int) isIgnored(proposal.getKind())} 
	 * and ignore proposals that have been declared as uninteresting.
	 * The proposal object passed in only valid for the duration of
	 * this call; implementors must not hang on to these objects.
	 * 
	 * @param proposal the completion proposal
	 * @exception IllegalArgumentException if the proposal is null
	 */
	public abstract void accept(CompletionProposal proposal);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9968.java