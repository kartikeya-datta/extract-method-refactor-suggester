error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8892.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8892.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8892.java
text:
```scala
I@@WizardStep step = history.pop();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.extensions.wizard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wicket.util.collections.ArrayListStack;

/**
 * Default implementation of {@link IWizardModel}.
 * <p>
 * Steps can be added to this model directly using either the
 * {@link #add(IWizardStep) normal add method} or
 * {@link #add(IWizardStep, wicket.extensions.wizard.WizardModel.ICondition) the conditional add method}.
 * </p>
 * 
 * <p>
 * <a href="https://wizard-framework.dev.java.net/">Swing Wizard Framework</a>
 * served as a valuable source of inspiration.
 * </p>
 * 
 * @author Eelco Hillenius
 */
public class WizardModel implements IWizardModel
{
	/**
	 * Interface for conditional displaying of wizard steps.
	 */
	public interface ICondition extends Serializable
	{
		/**
		 * Evaluates the current state and returns whether the step that is
		 * coupled to this condition is available.
		 * 
		 * @return True if the step this condition is coupled to is available,
		 *         false otherwise
		 */
		public boolean evaluate();
	}

	/**
	 * Condition that always evaluates true.
	 */
	public static final ICondition TRUE = new ICondition()
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Always returns true.
		 * 
		 * @return True
		 * 
		 * @see ICondition#evaluate()
		 */
		public boolean evaluate()
		{
			return true;
		}
	};

	private static final long serialVersionUID = 1L;

	/** The currently active step. */
	private IWizardStep activeStep;

	/** Whether cancel functionality is available. */
	private boolean cancelVisible = true;

	/** Conditions with steps. */
	private List<ICondition> conditions = new ArrayList<ICondition>();

	/** State history. */
	private final ArrayListStack<IWizardStep> history = new ArrayListStack<IWizardStep>();

	/** Whether the last button should be shown at all; false by default. */
	private boolean lastVisible = false;

	/** The wizard steps. */
	private List<IWizardStep> steps = new ArrayList<IWizardStep>();

	/** Listeners for {@link IWizardModelListener model events}. */
	private final List<IWizardModelListener> wizardModelListeners = new ArrayList<IWizardModelListener>(
			1);

	/**
	 * Construct.
	 */
	public WizardModel()
	{
	}

	/**
	 * Adds the next step to the wizard. If the {@link WizardStep} implements
	 * {@link ICondition}, then this method is equivalent to calling
	 * {@link #add(IWizardStep, ICondition) add(step, (ICondition)step)}.
	 * 
	 * @param step
	 *            the step to added.
	 */
	public void add(IWizardStep step)
	{
		if (step instanceof ICondition)
		{
			add(step, (ICondition)step);
		}
		else
		{
			add(step, TRUE);
		}
	}

	/**
	 * Adds an optional step to the model. The step will only be displayed if
	 * the specified condition is met.
	 * 
	 * @param step
	 *            The step to add
	 * @param condition
	 *            the {@link ICondition} under which it should be included in
	 *            the wizard.
	 */
	public void add(IWizardStep step, ICondition condition)
	{
		steps.add(step);
		conditions.add(condition);
	}

	/**
	 * Adds a wizard model listener.
	 * 
	 * @param listener
	 *            The listener to add
	 */
	public final void addListener(IWizardModelListener listener)
	{
		this.wizardModelListeners.add(listener);
	}

	/**
	 * This implementation just fires
	 * {@link IWizardModelListener#onCancel() a cancel event}. Though this
	 * isn't a very strong contract, it gives all the power to the user of this
	 * model.
	 * 
	 * @see wicket.extensions.wizard.IWizardModel#cancel()
	 */
	public void cancel()
	{
		fireWizardCancelled();
	}

	/**
	 * This implementation just fires
	 * {@link IWizardModelListener#onFinish() a finish event}. Though this
	 * isn't a very strong contract, it gives all the power to the user of this
	 * model.
	 * 
	 * @see wicket.extensions.wizard.IWizardModel#finish()
	 */
	public void finish()
	{
		fireWizardFinished();
	}

	/**
	 * Gets the current active step the wizard should display.
	 * 
	 * @return the active step.
	 */
	public final IWizardStep getActiveStep()
	{
		return activeStep;
	}

	/**
	 * Gets whether cancel functionality is available.
	 * 
	 * @return Whether cancel functionality is available
	 */
	public boolean isCancelVisible()
	{
		return cancelVisible;
	}

	/**
	 * Checks if the last button should be enabled.
	 * 
	 * @return <tt>true</tt> if the last button should be enabled,
	 *         <tt>false</tt> otherwise.
	 * @see #isLastVisible
	 */
	public final boolean isLastAvailable()
	{
		return allStepsComplete() && !isLastStep(activeStep);
	}

	/**
	 * @see wicket.extensions.wizard.IWizardModel#isLastStep(wicket.extensions.wizard.IWizardStep)
	 */
	public boolean isLastStep(IWizardStep step)
	{
		return findLastStep().equals(step);
	}

	/**
	 * Checks if the last button should be displayed. This method should only
	 * return true if the {@link #isLastAvailable} will return true at any
	 * point. Returning false will prevent the last button from appearing on the
	 * wizard at all.
	 * 
	 * @return <tt>true</tt> if the previou last should be displayed,
	 *         <tt>false</tt> otherwise.
	 */
	public boolean isLastVisible()
	{
		return lastVisible;
	}

	/**
	 * Checks if the next button should be enabled.
	 * 
	 * @return <tt>true</tt> if the next button should be enabled,
	 *         <tt>false</tt> otherwise.
	 */
	public final boolean isNextAvailable()
	{
		return activeStep.isComplete() && !isLastStep(activeStep);
	}

	/**
	 * Checks if the previous button should be enabled.
	 * 
	 * @return <tt>true</tt> if the previous button should be enabled,
	 *         <tt>false</tt> otherwise.
	 */
	public final boolean isPreviousAvailable()
	{
		return !history.isEmpty();
	}

	/**
	 * @see wicket.extensions.wizard.IWizardModel#lastStep()
	 */
	public void lastStep()
	{
		history.push(getActiveStep());
		IWizardStep lastStep = findLastStep();
		setActiveStep(lastStep);
	}

	/**
	 * @see wicket.extensions.wizard.IWizardModel#next()
	 */
	public void next()
	{
		history.push(getActiveStep());
		IWizardStep step = findNextVisibleStep();
		setActiveStep(step);
	}

	/**
	 * @see wicket.extensions.wizard.IWizardModel#previous()
	 */
	public void previous()
	{
		IWizardStep step = (IWizardStep)history.pop();
		setActiveStep(step);
	}

	/**
	 * Removes a wizard model listener.
	 * 
	 * @param listener
	 *            The listener to remove
	 */
	public final void removeListener(IWizardModelListener listener)
	{
		this.wizardModelListeners.remove(listener);
	}

	/**
	 * @see wicket.extensions.wizard.IWizardModel#reset()
	 */
	public void reset()
	{
		history.clear();
		this.activeStep = null;
		setActiveStep(findNextVisibleStep());
	}

	/**
	 * Sets the active step.
	 * 
	 * @param step
	 *            the new active step step.
	 */
	public void setActiveStep(IWizardStep step)
	{
		if (this.activeStep != null && step != null && activeStep.equals(step))
		{
			return;
		}
		this.activeStep = step;

		fireActiveStepChanged(step);
	}

	/**
	 * Sets whether cancel functionality is available.
	 * 
	 * @param cancelVisible
	 *            Whether cancel functionality is available
	 */
	public void setCancelVisible(boolean cancelVisible)
	{
		this.cancelVisible = cancelVisible;
	}

	/**
	 * Configures if the last button should be displayed.
	 * 
	 * @param lastVisible
	 *            <tt>true</tt> to display the last button, <tt>false</tt>
	 *            otherwise.
	 * @see #isLastVisible
	 */
	public void setLastVisible(boolean lastVisible)
	{
		this.lastVisible = lastVisible;
	}

	/**
	 * @see wicket.extensions.wizard.IWizardModel#stepIterator()
	 */
	public final Iterator<IWizardStep> stepIterator()
	{
		return steps.iterator();
	}

	/**
	 * Returns true if all the steps in the wizard return <tt>true</tt> from
	 * {@link IWizardStep#isComplete}. This is primarily used to determine if
	 * the last button can be enabled.
	 * 
	 * @return <tt>true</tt> if all the steps in the wizard are complete,
	 *         <tt>false</tt> otherwise.
	 */
	protected final boolean allStepsComplete()
	{
		for (Iterator<IWizardStep> iterator = stepIterator(); iterator.hasNext();)
		{
			if (!iterator.next().isComplete())
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Finds the last step in this model.
	 * 
	 * @return The last step
	 */
	protected final IWizardStep findLastStep()
	{
		for (int i = conditions.size() - 1; i >= 0; i--)
		{
			ICondition condition = conditions.get(i);
			if (condition.evaluate())
			{
				return steps.get(i);
			}
		}

		throw new IllegalStateException("Wizard contains no visible steps");
	}

	/**
	 * Finds the next visible step based on the active step.
	 * 
	 * @return The next visible step based on the active step
	 */
	protected final IWizardStep findNextVisibleStep()
	{
		int startIndex = (activeStep == null) ? 0 : steps.indexOf(activeStep) + 1;

		for (int i = startIndex; i < conditions.size(); i++)
		{
			ICondition condition = conditions.get(i);
			if (condition.evaluate())
			{
				return steps.get(i);
			}
		}

		throw new IllegalStateException("Wizard contains no more visible steps");
	}

	/**
	 * Notify listeners that the active step has changed.
	 * 
	 * @param step
	 *            The new step
	 */
	protected final void fireActiveStepChanged(IWizardStep step)
	{
		for (IWizardModelListener listener : wizardModelListeners)
		{
			listener.onActiveStepChanged(step);
		}
	}

	/**
	 * Notify listeners that the wizard is finished.
	 */
	protected final void fireWizardCancelled()
	{
		for (IWizardModelListener listener : wizardModelListeners)
		{
			listener.onCancel();
		}
	}

	/**
	 * Notify listeners that the wizard is finished.
	 */
	protected final void fireWizardFinished()
	{
		for (IWizardModelListener listener : wizardModelListeners)
		{
			listener.onFinish();
		}
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8892.java