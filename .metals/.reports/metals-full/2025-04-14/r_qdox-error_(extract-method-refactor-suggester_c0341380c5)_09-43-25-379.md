error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/161.java
text:
```scala
e@@.getMessage() == null ? "" : e.getMessage(), //$NON-NLS-1$,

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Point;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;

import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import org.eclipse.ui.internal.WorkbenchMessages;

/**
 * A wizard node represents a "potential" wizard. Wizard nodes
 * are used by wizard selection pages to allow the user to pick
 * from several available nested wizards.
 * <p>
 * <b>Subclasses</b> simply need to overide method <code>createWizard()</code>,
 * which is responsible for creating an instance of the wizard it represents
 * AND ensuring that this wizard is the "right" type of wizard (eg.-
 * New, Import, etc.).</p>
 */
public abstract class WorkbenchWizardNode implements IWizardNode, IPluginContribution {
	protected WorkbenchWizardSelectionPage parentWizardPage;
	protected IWizard wizard;
	protected WorkbenchWizardElement wizardElement;

	/**
	 * Creates a <code>WorkbenchWizardNode</code> that holds onto a wizard
	 * element.  The wizard element provides information on how to create
	 * the wizard supplied by the ISV's extension.
	 */
	public WorkbenchWizardNode(
		WorkbenchWizardSelectionPage aWizardPage,
		WorkbenchWizardElement element) {
		super();
		this.parentWizardPage = aWizardPage;
		this.wizardElement = element;
	}

	/**
	 * Returns the wizard represented by this wizard node.  <b>Subclasses</b>
	 * must override this method.
	 */
	public abstract IWorkbenchWizard createWizard() throws CoreException;

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizardNode#dispose()
	 */
	public void dispose() {
		// Do nothing since the wizard wasn't created via reflection.
	}

	/**
	 * Returns the current resource selection that is being given to the wizard.
	 */
	protected IStructuredSelection getCurrentResourceSelection() {
		return parentWizardPage.getCurrentResourceSelection();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizardNode#getExtent()
	 */
	public Point getExtent() {
		return new Point(-1, -1);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPluginContribution#getLocalId()
	 */
	public String getLocalId() {
		return wizardElement.getLocalId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPluginContribution#getPluginId()
	 */
	public String getPluginId() {
		return wizardElement.getPluginId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizardNode#getWizard()
	 */
	public IWizard getWizard() {
		if (wizard != null)
			return wizard; // we've already created it

		final IWorkbenchWizard[] workbenchWizard = new IWorkbenchWizard[1];
		final IStatus statuses[] = new IStatus[1];
		// Start busy indicator.
		BusyIndicator.showWhile(parentWizardPage.getShell().getDisplay(), new Runnable() {
			public void run() {
				Platform.run(new SafeRunnable() {

					/**
					 * Add the exception details to status is one happens.
						 */
					public void handleException(Throwable e) {
						statuses[0] =
							new Status(
								IStatus.ERROR,
								wizardElement
									.getConfigurationElement()
									.getDeclaringExtension()
									.getUniqueIdentifier(),
								IStatus.OK,
								e.getMessage(),
								e);
					}
					public void run() {
						try {
							workbenchWizard[0] = createWizard();
							// create instance of target wizard
						} catch (CoreException e) {
							statuses[0] = e.getStatus();
						}
					}
				});
			}
		});

		if (statuses[0] != null) {
			parentWizardPage.setErrorMessage(WorkbenchMessages.getString("WorkbenchWizard.errorMessage")); //$NON-NLS-1$
			ErrorDialog.openError(parentWizardPage.getShell(), WorkbenchMessages.getString("WorkbenchWizard.errorTitle"), //$NON-NLS-1$
			WorkbenchMessages.getString("WorkbenchWizard.errorMessage"), //$NON-NLS-1$
			statuses[0]);
			return null;
		}

		IStructuredSelection currentSelection = getCurrentResourceSelection();

		//Get the adapted version of the selection that works for the
		//wizard node
		currentSelection = wizardElement.adaptedSelection(currentSelection);

		workbenchWizard[0].init(getWorkbench(), currentSelection);

		wizard = workbenchWizard[0];
		return wizard;
	}

	/**
	 * Returns the wizard element.
	 */
	public WorkbenchWizardElement getWizardElement() {
		return wizardElement;
	}

	/**
	 * Returns the current workbench.
	 */
	protected IWorkbench getWorkbench() {
		return parentWizardPage.getWorkbench();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizardNode#isContentCreated()
	 */
	public boolean isContentCreated() {
		return wizard != null;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/161.java