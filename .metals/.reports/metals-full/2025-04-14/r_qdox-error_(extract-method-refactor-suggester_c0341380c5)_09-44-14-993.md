error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7430.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7430.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7430.java
text:
```scala
R@@oleManager.getInstance().enableActivities(selectedWizard.getClass().getName());

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

import java.util.StringTokenizer;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.*;
import org.eclipse.ui.internal.registry.NewWizardsRegistryReader;
import org.eclipse.ui.internal.roles.RoleManager;

/**
 * The new wizard is responsible for allowing the user to choose which
 * new (nested) wizard to run. The set of available new wizards comes
 * from the new extension point.
 */
public class NewWizard extends Wizard {
	private static final String CATEGORY_SEPARATOR = "/"; //$NON-NLS-1$

	private IWorkbench workbench;
	private IStructuredSelection selection;
	private NewWizardSelectionPage mainPage;
	private boolean projectsOnly = false;
	private String categoryId = null;
	/**
	 * Create the wizard pages
	 */
	public void addPages() {
		NewWizardsRegistryReader rdr = new NewWizardsRegistryReader(projectsOnly);
		WizardCollectionElement wizards = (WizardCollectionElement) rdr.getWizards();

		if (categoryId != null) {
			WizardCollectionElement categories = wizards;
			StringTokenizer familyTokenizer = new StringTokenizer(categoryId, CATEGORY_SEPARATOR);
			while (familyTokenizer.hasMoreElements()) {
				categories = getChildWithID(categories, familyTokenizer.nextToken());
				if (categories == null)
					break;
			}
			if (categories != null)
				wizards = categories;
		}

		mainPage = new NewWizardSelectionPage(this.workbench, this.selection, wizards);
		addPage(mainPage);
	}
	/**
	 * Returns the child collection element for the given id
	 */
	private WizardCollectionElement getChildWithID(WizardCollectionElement parent, String id) {
		Object[] children = parent.getChildren();
		for (int i = 0; i < children.length; ++i) {
			WizardCollectionElement currentChild = (WizardCollectionElement) children[i];
			if (currentChild.getId().equals(id))
				return currentChild;
		}
		return null;
	}
	/**
	 * Returns the id of the category of wizards to show
	 * or <code>null</code> to show all categories.
	 * If no entries can be found with this id then all 
	 * categories are shown.
	 * @return String or <code>null</code>.
	 */
	public String getCategoryId() {
		return categoryId;
	}
	/**
	 * Sets the id of the category of wizards to show
	 * or <code>null</code> to show all categories.
	 * If no entries can be found with this id then all
	 * categories are shown.
	 * @param id. String or <code>null</code>.
	 */
	public void setCategoryId(String id) {
		categoryId = id;
	}
	/**
	 *	Lazily create the wizards pages
	 */
	public void init(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		this.workbench = aWorkbench;
		this.selection = currentSelection;

		if (projectsOnly)
			setWindowTitle(WorkbenchMessages.getString("NewProject.title")); //$NON-NLS-1$
		else
			setWindowTitle(WorkbenchMessages.getString("NewWizard.title")); //$NON-NLS-1$
		setDefaultPageImageDescriptor(
			WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_WIZBAN_NEW_WIZ));
		setNeedsProgressMonitor(true);
	}
	/**
	 *	The user has pressed Finish.  Instruct self's pages to finish, and
	 *	answer a boolean indicating success.
	 *
	 *	@return boolean
	 */
	public boolean performFinish() {
		//save our selection state
		mainPage.saveWidgetValues();
		IWizard selectedWizard = mainPage.getSelectedNode().getWizard();
		RoleManager.getInstance().enableRoles(selectedWizard.getClass().getName());

		return true;
	}
	/**
	 * Sets the projects only flag.  If <code>true</code> only projects will
	 * be shown in this wizard.
	 */
	public void setProjectsOnly(boolean b) {
		projectsOnly = b;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7430.java