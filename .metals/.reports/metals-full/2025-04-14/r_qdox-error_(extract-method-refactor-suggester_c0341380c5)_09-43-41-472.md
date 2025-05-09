error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1909.java
text:
```scala
protected W@@orkbenchPreferenceGroup[] getGroups() {

/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceLabelProvider;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import org.eclipse.ui.activities.WorkbenchActivityHelper;

import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * Base class for preference dialogs that will show two tabs of preferences -
 * filtered and unfiltered.
 * 
 * @since 3.0
 */
public abstract class FilteredPreferenceDialog extends PreferenceDialog {

	GroupedPreferenceLabelProvider groupedLabelProvider;
	
	/**
	 * Creates a new preference dialog under the control of the given preference
	 * manager.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param manager
	 *            the preference manager
	 */
	public FilteredPreferenceDialog(Shell parentShell, PreferenceManager manager) {
		super(parentShell, manager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferenceDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		if (getGroups().length > 0)
			return createCategorizedDialogArea(parent);
		return super.createDialogArea(parent);
	}

	/**
	 * Create the dialog area with a spot for the categories
	 * @param parent
	 * @return Control
	 */
	private Control createCategorizedDialogArea(Composite parent) {
		//		 create a composite with standard margins and spacing
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite top = new Composite(composite,SWT.NONE);
		GridLayout topLayout = new GridLayout();
		topLayout.makeColumnsEqualWidth = false;
		topLayout.numColumns = 2;
		top.setLayout(topLayout);
		
		GridData topData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		top.setLayoutData(topData);

		ToolBar toolbar = createToolBar(top);
		
		toolbar.setLayoutData(new GridData());
		
		Control searchArea = createSearchArea(top);
		GridData searchData = new GridData(GridData.FILL_HORIZONTAL);
		searchData.grabExcessHorizontalSpace = true;
		searchData.verticalAlignment = SWT.BOTTOM;
		searchArea.setLayoutData(searchData);

		super.createDialogArea(composite);

		applyDialogFont(composite);
		
		toolbar.getItem(0).setSelection(true);
		
		return composite;
	}


	/**
	 * Create the search area for the receiver.
	 * @param composite
	 * @return Control The control that contains the area.
	 */
	private Control createSearchArea(Composite composite) {
		
		Composite searchArea = new Composite(composite,SWT.NONE);
		
		GridLayout searchLayout = new GridLayout();
		searchLayout.numColumns = 2;
		searchLayout.marginWidth = 0;
		searchLayout.marginHeight = 0;
		searchLayout.makeColumnsEqualWidth = false;
		searchArea.setLayout(searchLayout);
		
		final Text searchText = new Text(searchArea,SWT.BORDER);
		
		GridData textData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		searchText.setLayoutData(textData);
		
		Button searchButton = new Button(searchArea,SWT.PUSH);
		searchButton.setText(WorkbenchMessages.getString("FilteredPreferenceDialog.SearchButton")); //$NON-NLS-1$
		GridData searchData = new GridData(GridData.END);
		searchButton.setLayoutData(searchData);
		
		searchButton.addSelectionListener(new SelectionAdapter(){
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				highlightHits(searchText.getText());
			}
		});
		
		return searchArea;
	}

	/**
	 * Highlight all nodes that match text;
	 * @param text
	 */
	protected void highlightHits(String text) {
		WorkbenchPreferenceGroup group = (WorkbenchPreferenceGroup) getTreeViewer().getInput();
		
		group.highlightHits(text);
		getTreeViewer().refresh();
	}

	/**
	 * Create a generic list viewer for entry selection.
	 * 
	 * @param composite
	 * @return GenericListViewer
	 */
	private ToolBar createToolBar(Composite composite) {

		final ILabelProvider labelProvider = getCategoryLabelProvider();
		
		ToolBar toolbar = new ToolBar(composite,SWT.HORIZONTAL | SWT.RIGHT);
	
		WorkbenchPreferenceGroup[] groups = getGroups();
		
		for (int i = 0; i < groups.length; i++) {
			final WorkbenchPreferenceGroup group = groups[i];
			ToolItem newItem = new ToolItem(toolbar,SWT.RADIO);
			newItem.setText(group.getName());
			newItem.setImage(group.getImage());
			newItem.addSelectionListener(new SelectionAdapter(){
				/* (non-Javadoc)
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				public void widgetSelected(SelectionEvent e) {
					getTreeViewer().setInput(group);
				}
			});
			
		}

		return toolbar;
	}

	/**
	 * Return the label provider for the categories.
	 * @return ILabelProvider
	 */
	private ILabelProvider getCategoryLabelProvider() {
		return new LabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
			 */
			public Image getImage(Object element) {
				return ((WorkbenchPreferenceGroup) element).getImage();
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			public String getText(Object element) {
				return ((WorkbenchPreferenceGroup) element).getName();
			}
		};
	}

	/**
	 * Return a content provider for the categories.
	 * 
	 * @return IContentProvider
	 */
	private IContentProvider getCategoryContentProvider() {
		return new ListContentProvider() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.dialogs.ListContentProvider#getElements(java.lang.Object)
			 */
			public Object[] getElements(Object input) {
				return getGroups();
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferenceDialog#createTreeViewer(org.eclipse.swt.widgets.Composite)
	 */
	protected TreeViewer createTreeViewer(Composite parent) {
		TreeViewer tree = super.createTreeViewer(parent);

		if (hasGroups()) {
			groupedLabelProvider = new GroupedPreferenceLabelProvider();
			tree.setContentProvider(new GroupedPreferenceContentProvider());
			tree.setLabelProvider(groupedLabelProvider);

		} else {
			tree.setContentProvider(new FilteredPreferenceContentProvider());
			tree.setLabelProvider(new PreferenceLabelProvider());
		}
		return tree;
	}

	/**
	 * Differs from super implementation in that if the node is found but should
	 * be filtered based on a call to
	 * <code>WorkbenchActivityHelper.filterItem()</code> then
	 * <code>null</code> is returned.
	 * 
	 * @see org.eclipse.jface.preference.PreferenceDialog#findNodeMatching(java.lang.String)
	 */
	protected IPreferenceNode findNodeMatching(String nodeId) {
		IPreferenceNode node = super.findNodeMatching(nodeId);
		if (WorkbenchActivityHelper.filterItem(node))
			return null;
		return node;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferenceDialog#selectSavedItem()
	 */
	protected void selectSavedItem() {
		if (hasGroups()){
			WorkbenchPreferenceGroup startGroup = getGroups()[0];
			getTreeViewer().setInput(startGroup);
			
			StructuredSelection selection = new StructuredSelection(startGroup.getPreferenceNodes()[0]);
			getTreeViewer().setSelection(selection);
		}
		super.selectSavedItem();
	}

	/**
	 * Return the categories in the receiver.
	 * @return WorkbenchPreferenceGroup[]
	 */
	private WorkbenchPreferenceGroup[] getGroups() {
		return ((WorkbenchPreferenceManager) WorkbenchPlugin.getDefault().getPreferenceManager())
				.getGroups();
	}
	
	

	/**
	 * Return whether or not there are categories.
	 * @return boolean
	 */
	private boolean hasGroups() {
		return getGroups().length > 0;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateMessage()
	 */
	public void updateMessage() {
		if(getCurrentPage() != null)
			super.updateMessage();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateTitle()
	 */
	public void updateTitle() {
		if(getCurrentPage() != null)
			super.updateTitle();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1909.java