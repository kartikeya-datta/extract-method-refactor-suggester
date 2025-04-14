error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6387.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6387.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6387.java
text:
```scala
d@@escriptionText = new Text(c, SWT.READ_ONLY | SWT.WRAP | SWT.BORDER);

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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.activities.IActivity;
import org.eclipse.ui.activities.ICategory;
import org.eclipse.ui.activities.ICategoryActivityBinding;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.activities.NotDefinedException;

/**
 * A simple control provider that will allow the user to toggle on/off the
 * activities bound to categories.
 * 
 * @since 3.0
 */
public class ActivityEnabler {

	private static final int ALL = 2;
	private static final int NONE = 0;
	private static final int SOME = 1;

	private IWorkbenchActivitySupport activitySupport;

	private ISelectionChangedListener selectionListener = new ISelectionChangedListener() {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
         */
        public void selectionChanged(SelectionChangedEvent event) {
            Object element = ((IStructuredSelection)event.getSelection()).getFirstElement();
            try {
                if (element instanceof ICategory) {
                    descriptionText.setText(((ICategory)element).getDescription());
				}
                else if (element instanceof IActivity) {
                    descriptionText.setText(((IActivity)element).getDescription());
                }
            }
            catch (NotDefinedException e) {
                descriptionText.setText(""); //$NON-NLS-1$
            }
        }
    };
	
	/**
	 * Listener that manages the grey/check state of categories.
	 */
	private ICheckStateListener checkListener = new ICheckStateListener() {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged(org.eclipse.jface.viewers.CheckStateChangedEvent)
		 */
		public void checkStateChanged(CheckStateChangedEvent event) {
			Set checked = new HashSet(Arrays.asList(dualViewer.getCheckedElements()));
			Object element = event.getElement();
			if (element instanceof ICategory) {
				// clicking on a category should enable/disable all activities within it
				dualViewer.setSubtreeChecked(element, event.getChecked());
				// the state of the category is alwas absolute after clicking on it.  Never gray.
				dualViewer.setGrayed(element, false);
			} else {
				// clicking on an activity can potential change the check/gray state of its category.
				CategorizedActivity proxy = (CategorizedActivity) element;
				Object[] children = provider.getChildren(proxy.getCategory());
				int state = NONE;
				int count = 0;
				for (int i = 0; i < children.length; i++) {
					if (checked.contains(children[i])) {
						count++;
					}
				}

				if (count == children.length) {
					state = ALL;
				} else if (count != 0) {
					state = SOME;
				}

				if (state == NONE) {
					checked.remove(proxy.getCategory());
				} else {
					checked.add(proxy.getCategory());
				}

				dualViewer.setGrayed(proxy.getCategory(), state == SOME);
				dualViewer.setCheckedElements(checked.toArray());
			}
		}
	};

	private CheckboxTreeViewer dualViewer;

	/**
	 * The Set of activities that belong to at least one category.
	 */
	private Set managedActivities = new HashSet(7);

	/**
	 * The content provider.
	 */
	private ActivityCategoryContentProvider provider = new ActivityCategoryContentProvider();
    
	/**
	 * The descriptive text.
	 */
	private Text descriptionText;

	/**
	 * Create a new instance.
	 * 
	 * @param activitySupport the <code>IWorkbenchActivitySupport</code> from 
	 * which to draw the <code>IActivityManager</code>.
	 */
	public ActivityEnabler(IWorkbenchActivitySupport activitySupport) {
		this.activitySupport = activitySupport;
	}

	/**
	 * Create the controls.
	 * 
	 * @param parent the parent in which to create the controls.
	 * @return the composite in which the controls exist.
	 */
	public Control createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(2, false));

		Composite c = new Composite(mainComposite, SWT.NONE);
		c.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		c.setLayout(new GridLayout(1, true));
		
		Label label = new Label(c, SWT.NONE);
		label.setText(ActivityMessages.getString("ActivityEnabler.activities")); //$NON-NLS-1$
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setFont(parent.getFont());
		
		dualViewer = new CheckboxTreeViewer(c);
		dualViewer.setSorter(new ViewerSorter());
		dualViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
		dualViewer.setLabelProvider(new ActivityCategoryLabelProvider());
		dualViewer.setContentProvider(provider);
		dualViewer.setInput(activitySupport.getActivityManager());
		dualViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		dualViewer.getControl().setFont(parent.getFont());

		c = new Composite(mainComposite, SWT.NONE);
		c.setLayoutData(new GridData(GridData.FILL_BOTH));
		c.setLayout(new GridLayout(1, true));
		
		label = new Label(c, SWT.NONE);
		label.setText(ActivityMessages.getString("ActivityEnabler.description")); //$NON-NLS-1$
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setFont(parent.getFont());
		
		descriptionText = new Text(c, SWT.READ_ONLY | SWT.WRAP);
		descriptionText.setFont(parent.getFont());
		descriptionText.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING));
		setInitialStates();

		dualViewer.addCheckStateListener(checkListener);
		dualViewer.addSelectionChangedListener(selectionListener);

		dualViewer.setSelection(new StructuredSelection());
		return mainComposite;
	}

	/**
	 * @param categoryId the id to fetch.
	 * @return return all ids for activities that are in the given in the 
	 * category.
	 */
	private Collection getCategoryActivityIds(String categoryId) {
		ICategory category = activitySupport.getActivityManager().getCategory(categoryId);
		Set activityBindings = category.getCategoryActivityBindings();
		List categoryActivities = new ArrayList(activityBindings.size());
		for (Iterator i = activityBindings.iterator(); i.hasNext();) {
			ICategoryActivityBinding binding = (ICategoryActivityBinding) i.next();
			String activityId = binding.getActivityId();
			categoryActivities.add(activityId);
		}
		return categoryActivities;
	}

	/**
	 * Set the enabled category/activity check/grey states based on initial 
	 * activity enablement.
	 */
	private void setInitialStates() {
		Set enabledActivities = activitySupport.getActivityManager().getEnabledActivityIds();
		Set categories = activitySupport.getActivityManager().getDefinedCategoryIds();
		List checked = new ArrayList(10), grayed = new ArrayList(10);
		for (Iterator i = categories.iterator(); i.hasNext();) {
			String categoryId = (String) i.next();
			ICategory category = activitySupport.getActivityManager().getCategory(categoryId);

			int state = NONE;
			Collection activities = getCategoryActivityIds(categoryId);
			int foundCount = 0;
			for (Iterator j = activities.iterator(); j.hasNext();) {
				String activityId = (String) j.next();
				managedActivities.add(activityId);
				if (enabledActivities.contains(activityId)) {
					IActivity activity =
						activitySupport.getActivityManager().getActivity(activityId);
					checked.add(new CategorizedActivity(category, activity));
					//add activity proxy
					foundCount++;
				}
			}

			if (foundCount == activities.size()) {
				state = ALL;
			} else if (foundCount > 0) {
				state = SOME;
			}

			if (state == NONE) {
				continue;
			}
			checked.add(category);

			if (state == SOME) {
				grayed.add(category);
			}
		}

		dualViewer.setCheckedElements(checked.toArray());
		dualViewer.setGrayedElements(grayed.toArray());
	}

	/**
	 * Update activity enablement based on the check states of activities in the
	 * tree. 
	 */
	public void updateActivityStates() {
		Set enabledActivities =
			new HashSet(activitySupport.getActivityManager().getEnabledActivityIds());

		// remove all but the unmanaged activities (if any).
		enabledActivities.removeAll(managedActivities);

		Object[] checked = dualViewer.getCheckedElements();
		for (int i = 0; i < checked.length; i++) {
			Object element = checked[i];
			if (element instanceof ICategory || dualViewer.getGrayed(element)) {
				continue;
			}
			enabledActivities.add(((IActivity) element).getId());
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6387.java