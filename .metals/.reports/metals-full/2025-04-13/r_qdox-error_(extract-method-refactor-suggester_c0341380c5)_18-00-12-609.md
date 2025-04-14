error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/79.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/79.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/79.java
text:
```scala
I@@terator<IWizardStep> stepIterator();

/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision: 5004 $ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.extensions.wizard;

import java.io.Serializable;
import java.util.Iterator;


/**
 * This interface defines the model for wizards. This model knows about the
 * wizard's steps and the transitions between them, and it holds a reference to
 * the currently active step. It might function as a generic state holder for
 * the wizard too, though you might find it more convenient to use the wizard
 * component itself for that, or even an external model.
 * 
 * <p>
 * {@link IWizardModelListener wizard model listeners} can be registered to be
 * notified of important events (changing the active step) using the
 * {@link #addListener(IWizardModelListener) add listener} method.
 * </p>
 * 
 * <p>
 * Typically, you would use
 * {@link WizardModel the default implementation of this interface}, but if you
 * need to do more sophisticated stuff, like branching etc, you can consider
 * creating your own implementation.
 * </p>
 * 
 * <p>
 * <a href="https://wizard-framework.dev.java.net/">Swing Wizard Framework</a>
 * served as a valuable source of inspiration.
 * </p>
 * 
 * @see WizardModel
 * 
 * @author Eelco Hillenius
 */
public interface IWizardModel extends Serializable
{
	/**
	 * Adds a wizard model listener.
	 * 
	 * @param listener
	 *            The wizard model listener to add
	 */
	void addListener(IWizardModelListener listener);

	/**
	 * Cancels further processing. Implementations may clean up and reset the
	 * model. Implementations should notify the registered
	 * {@link IWizardModelListener#onCancel() model listeners}.
	 */
	void cancel();

	/**
	 * Instructs the wizard to finish succesfully. Typically, implementations
	 * check whether this option is available at all. Implementations may clean
	 * up and reset the model. Implementations should notify the registered
	 * {@link IWizardModelListener#onFinish() model listeners}.
	 */
	void finish();

	/**
	 * Gets the current active step the wizard should display.
	 * 
	 * @return the active step.
	 */
	IWizardStep getActiveStep();

	/**
	 * Gets whether the cancel button should be displayed.
	 * 
	 * @return True if the cancel button should be displayed
	 */
	boolean isCancelVisible();

	/**
	 * Checks if the last button should be enabled.
	 * 
	 * @return <tt>true</tt> if the last button should be enabled,
	 *         <tt>false</tt> otherwise.
	 * @see #isLastVisible
	 */
	boolean isLastAvailable();

	/**
	 * Gets whether the specified step is the last step in the wizard.
	 * 
	 * @param step
	 *            the step to check
	 * @return True if its the final step in the wizard, false< otherwise.
	 */
	boolean isLastStep(IWizardStep step);

	/**
	 * Gets whether the last button should be displayed. This method should only
	 * return true if the {@link #isLastAvailable} will return true at any
	 * point. Returning false will prevent the last button from appearing on the
	 * wizard at all.
	 * 
	 * @return True if the last button should be displayed, False otherwise.
	 */
	boolean isLastVisible();


	/**
	 * Gets whether the next button should be enabled.
	 * 
	 * @return True if the next button should be enabled, false otherwise.
	 */
	boolean isNextAvailable();

	/**
	 * Gets whether the previous button should be enabled.
	 * 
	 * @return True if the previous button should be enabled, false otherwise.
	 */
	boolean isPreviousAvailable();

	/**
	 * Takes the model to the last step in the wizard. This method must only be
	 * called if {@link #isLastAvailable} returns <tt>true</tt>.
	 */
	void lastStep();

	/**
	 * Increments the model the the next step. This method must only be called
	 * if {@link #isNextAvailable} returns <tt>true</tt>.
	 */
	void next();

	/**
	 * Takes the model to the previous step.This method must only be called if
	 * {@link #isPreviousAvailable} returns <tt>true</tt>.
	 */
	void previous();

	/**
	 * Removes a wizard model listener.
	 * 
	 * @param listener
	 *            The listener to remove
	 */
	void removeListener(IWizardModelListener listener);

	/**
	 * Resets the model, setting it to the first step.
	 */
	void reset();

	/**
	 * Returns an iterator over all the steps in the model. The iteration order
	 * is not guarenteed to the be the order of traversal.
	 * 
	 * @return an iterator over all the steps of the model
	 */
	Iterator stepIterator();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/79.java