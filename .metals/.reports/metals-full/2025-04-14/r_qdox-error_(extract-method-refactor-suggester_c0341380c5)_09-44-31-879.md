error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7233.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7233.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7233.java
text:
```scala
e@@ach.testAssumptionInvalid(description, e);

package org.junit.runner.notification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Ignore;
import org.junit.Assume.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;

/**
 * If you write custom runners, you may need to notify JUnit of your progress running tests.
 * Do this by invoking the <code>RunNotifier</code> passed to your implementation of
 * {@link org.junit.runner.Runner#run(RunNotifier)}. Future evolution of this class is likely to 
 * move {@link #fireTestRunStarted(Description)} and {@link #fireTestRunFinished(Result)}
 * to a separate class since they should only be called once per run.
 */
public class RunNotifier {
	private List<RunListener> fListeners= new ArrayList<RunListener>();
	private boolean fPleaseStop= false;
	
	/** Internal use only
	 */
	public void addListener(RunListener listener) {
		fListeners.add(listener);
	}

	/** Internal use only
	 */
	public void removeListener(RunListener listener) {
		fListeners.remove(listener);
	}

	private abstract class SafeNotifier {
		void run() {
			for (Iterator<RunListener> all= fListeners.iterator(); all.hasNext();) {
				try {
					notifyListener(all.next());
				} catch (Exception e) {
					all.remove(); // Remove the offending listener first to avoid an infinite loop
					fireTestFailure(new Failure(Description.TEST_MECHANISM, e));
				}
			}
		}
		
		abstract protected void notifyListener(RunListener each) throws Exception;
	}
	
	/**
	 * Do not invoke. 
	 */
	public void fireTestRunStarted(final Description description) {
		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {
				each.testRunStarted(description);
			};
		}.run();
	}
	
	/**
	 * Do not invoke.
	 */
	public void fireTestRunFinished(final Result result) {
		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {
				each.testRunFinished(result);
			};
		}.run();
	}
	
	/**
	 * Invoke to tell listeners that an atomic test is about to start.
	 * @param description the description of the atomic test (generally a class and method name)
	 * @throws StoppedByUserException thrown if a user has requested that the test run stop
	 */
	public void fireTestStarted(final Description description) throws StoppedByUserException {
		if (fPleaseStop)
			throw new StoppedByUserException();
		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {
				each.testStarted(description);
			};
		}.run();
	}

	/**
	 * Invoke to tell listeners that an atomic test failed.
	 * @param failure the description of the test that failed and the exception thrown
	 */
	public void fireTestFailure(final Failure failure) {
		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {
				each.testFailure(failure);
			};
		}.run();
	}

	/**
	 * Invoke to tell listeners that an atomic test was ignored.
	 * @param description the description of the ignored test
	 */
	public void fireTestIgnored(final Description description) {
		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {
				each.testIgnored(description);
				each.testIgnored(description, getIgnoredReason(description));
			}

			private String getIgnoredReason(final Description description) {
				Ignore annotation= description.getAnnotation(Ignore.class);
				if (annotation == null)
					return null;
				return annotation.value();
			};
		}.run();
	}

	/**
	 * Invoke to tell listeners that an atomic test finished. Always invoke 
	 * {@link #fireTestFinished(Description)} if you invoke {@link #fireTestStarted(Description)} 
	 * as listeners are likely to expect them to come in pairs.
	 * @param description the description of the test that finished
	 */
	public void fireTestFinished(final Description description) {
		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {
				each.testFinished(description);
			};
		}.run();
	}
	
	/**
	 * Ask that the tests run stop before starting the next test. Phrased politely because
	 * the test currently running will not be interrupted. It seems a little odd to put this
	 * functionality here, but the <code>RunNotifier</code> is the only object guaranteed 
	 * to be shared amongst the many runners involved.
	 */
	public void pleaseStop() {
		fPleaseStop= true;
	}

	/**
	 * Internal use only. The Result's listener must be first.
	 */
	public void addFirstListener(RunListener listener) {
		fListeners.add(0, listener);
	}

	public void testAborted(Description description, Throwable cause) {
		fireTestStarted(description);
		fireTestFailure(new Failure(description, cause));
		fireTestFinished(description);
	}

	public void fireTestAssumptionFailed(final Description description,
			final AssumptionViolatedException e) {
		new SafeNotifier() {
			@Override
			protected void notifyListener(RunListener each) throws Exception {
				each.testAssumptionFailed(description, e);
			};
		}.run();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7233.java