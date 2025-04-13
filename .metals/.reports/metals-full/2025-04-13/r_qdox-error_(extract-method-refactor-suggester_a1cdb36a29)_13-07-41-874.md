error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5134.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5134.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5134.java
text:
```scala
c@@ategoryViewer.setInput(activitySupport.getActivityManager());

/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.activities.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.activities.ICategoryActivityBinding;
import org.eclipse.ui.activities.ICategory;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;

/**
 * A simple control provider that will allow the user to toggle on/off the
 * activities bound to categories.
 * 
 * @since 3.0
 */
public class ActivityEnabler {
	private ListViewer activitiesViewer;
	private IWorkbenchActivitySupport activitySupport;

	private CheckboxTableViewer categoryViewer;
	private Set checkedInSession = new HashSet(7),
		uncheckedInSession = new HashSet(7);

	private String lastCategory = null;

	/**
	 * Create a new instance.
	 * 
	 * @param activityManager the activity manager that will be used.
	 */
	public ActivityEnabler(IWorkbenchActivitySupport activityManager) {
		this.activitySupport = activityManager;
	}

	/**
	 * @param categoryId the id to check.
	 * @return whether all activities in the category are enabled.
	 */
	private boolean categoryEnabled(String categoryId) {
		Collection categoryActivities = getCategoryActivities(categoryId);
		Set enabledActivities =
			activitySupport.getActivityManager().getEnabledActivityIds();
		return enabledActivities.containsAll(categoryActivities);
	}

	/**
	 * Create the controls.
	 * 
	 * @param parent the parent in which to create the controls.
	 * @return the composite in which the controls exist.
	 */
	public Control createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(2, true));

		Label label = new Label(mainComposite, SWT.NONE);
		label.setText(ActivityMessages.getString("ActivityEnabler.categories")); //$NON-NLS-1$
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		label = new Label(mainComposite, SWT.NONE);
		label.setText(ActivityMessages.getString("ActivityEnabler.activities")); //$NON-NLS-1$
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		{
			categoryViewer =
				CheckboxTableViewer.newCheckList(mainComposite, SWT.BORDER);
			categoryViewer.getControl().setLayoutData(
				new GridData(GridData.FILL_BOTH));
			categoryViewer.setContentProvider(new CategoryContentProvider());
			categoryViewer.setLabelProvider(
				new CategoryLabelProvider(
					activitySupport.getActivityManager()));
			categoryViewer.setSorter(new ViewerSorter());
			categoryViewer.setInput(activitySupport);
			categoryViewer.setSelection(new StructuredSelection());
			setCategoryStates();
		}

		{
			activitiesViewer = new ListViewer(mainComposite);
			activitiesViewer.getControl().setLayoutData(
				new GridData(GridData.FILL_BOTH));
			activitiesViewer.setContentProvider(new ActivityContentProvider());
			activitiesViewer.setLabelProvider(
				new ActivityLabelProvider(
					activitySupport.getActivityManager()));
			activitiesViewer.setSorter(new ViewerSorter());
			activitiesViewer.setInput(Collections.EMPTY_SET);
			activitiesViewer.getControl().setEnabled(false);
			// read only control
		}

		categoryViewer
			.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection =
					(IStructuredSelection) event.getSelection();
				if (!selection.isEmpty()) {
					String categoryId = (String) selection.getFirstElement();
					// don't reset the input unless we're a differnet category
					if (!categoryId.equals(lastCategory)) {
						lastCategory = categoryId;
						activitiesViewer.setInput(
							getCategoryActivities(categoryId));
					}
				}
			}
		});

		categoryViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object element = event.getElement();
				if (event.getChecked()) {
					if (!uncheckedInSession.remove(element)) {
						checkedInSession.add(element);
					}
				} else {
					if (!checkedInSession.remove(element)) {
						uncheckedInSession.add(element);
					}
				}
			}
		});
		// default select the first category so the right pane will not be
		// empty
		Object firstElement = categoryViewer.getElementAt(0);
		if (firstElement != null) {
			categoryViewer.setSelection(
				new StructuredSelection(firstElement),
				true);
		}

		return mainComposite;
	}

	/**
	 * @param categoryId the id to fetch.
	 * @return all activity ids in the category.
	 */
	private Collection getCategoryActivities(String categoryId) {
		ICategory category =
			activitySupport.getActivityManager().getCategory(categoryId);
		Set activityBindings = category.getCategoryActivityBindings();
		List categoryActivities = new ArrayList(10);
		for (Iterator j = activityBindings.iterator(); j.hasNext();) {
			ICategoryActivityBinding binding =
				(ICategoryActivityBinding) j.next();
			String activityId = binding.getActivityId();
			categoryActivities.add(activityId);
		}
		return categoryActivities;
	}

	/**
	 * Set the enabled category states based on current activity enablement.
	 */
	private void setCategoryStates() {
		Set categories =
			activitySupport.getActivityManager().getDefinedCategoryIds();
		List enabledCategories = new ArrayList(10);
		for (Iterator i = categories.iterator(); i.hasNext();) {
			String categoryId = (String) i.next();
			if (categoryEnabled(categoryId)) {
				enabledCategories.add(categoryId);
			}
		}
		categoryViewer.setCheckedElements(enabledCategories.toArray());
	}

	/**
	 * Update activity enablement based on the check/uncheck actions of the
	 * user in this session. First, any activities that are bound to unchecked
	 * categories are applied and then those that were checked.
	 */
	public void updateActivityStates() {
		Set enabledActivities =
			new HashSet(
				activitySupport.getActivityManager().getEnabledActivityIds());

		for (Iterator i = uncheckedInSession.iterator(); i.hasNext();) {
			String categoryId = (String) i.next();
			enabledActivities.removeAll(getCategoryActivities(categoryId));
		}

		for (Iterator i = checkedInSession.iterator(); i.hasNext();) {
			String categoryId = (String) i.next();
			enabledActivities.addAll(getCategoryActivities(categoryId));
		}

		activitySupport.setEnabledActivityIds(enabledActivities);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5134.java