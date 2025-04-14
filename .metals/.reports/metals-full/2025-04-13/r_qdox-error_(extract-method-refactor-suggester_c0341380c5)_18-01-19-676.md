error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4217.java
text:
```scala
s@@uper("workingSetTypeSelectionPage", WorkbenchMessages.getString("Select"), WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_WIZBAN_WORKINGSET_WIZ)); //$NON-NLS-1$ //$NON-NLS-2$

/******************************************************************************* 
 * Copyright (c) 2000, 2003 IBM Corporation and others. 
 * All rights reserved. This program and the accompanying materials! 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 * 
 * Contributors: 
 *        IBM Corporation - initial API and implementation 
 * 		  Sebastian Davids <sdavids@gmx.de> - Fix for bug 19346 - Dialog font should be
 *        activated and used by other components.
 * *****************************************************************************/
package org.eclipse.ui.internal.dialogs;

import java.util.*;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.*;
import org.eclipse.ui.internal.registry.WorkingSetDescriptor;
import org.eclipse.ui.internal.registry.WorkingSetRegistry;

/**
 * The working set type page is used in the new working set 
 * wizard to select from a list of plugin defined working set 
 * types.
 * 
 * @since 2.0
 */
public class WorkingSetTypePage extends WizardPage {
	private final static int SIZING_SELECTION_WIDGET_WIDTH = 50;
	private final static int SIZING_SELECTION_WIDGET_HEIGHT = 200;

	private TableViewer typesListViewer;
	private Map icons;

	/**
	 * Creates a new instance of the receiver
	 */
	public WorkingSetTypePage() {
		super("workingSetTypeSelectionPage", WorkbenchMessages.getString("Select"), WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_WIZBAN_RESOURCEWORKINGSET_WIZ)); //$NON-NLS-1$ //$NON-NLS-2$
		setDescription(WorkbenchMessages.getString("WorkingSetTypePage.description")); //$NON-NLS-1$				
		icons = new Hashtable();
	}
	/** 
	 * Overrides method in WizardPage
	 * 
	 * @see org.eclipse.jface.wizard.IWizardPage#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}
	/**
	 * Populates the working set types list.
	 */
	private void createContent() {
		WorkingSetRegistry registry = WorkbenchPlugin.getDefault().getWorkingSetRegistry();
		WorkingSetDescriptor[] descriptors = registry.getWorkingSetDescriptors();
		Table table = (Table) typesListViewer.getControl();

		for (int i = 0; i < descriptors.length; i++) {
			TableItem tableItem = new TableItem(table, SWT.NULL);
			ImageDescriptor imageDescriptor = descriptors[i].getIcon();
			
			if (imageDescriptor != null) {
				Image icon = (Image) icons.get(imageDescriptor);
				if (icon == null) {
					icon = imageDescriptor.createImage();
					icons.put(imageDescriptor, icon);
				}
				tableItem.setImage(icon);
			}
			tableItem.setText(descriptors[i].getName());
			tableItem.setData(descriptors[i]);
		}
	}
	/** 
	 * Implements IDialogPage
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Font font = parent.getFont();
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		setControl(composite);

		WorkbenchHelp.setHelp(composite, IHelpContextIds.WORKING_SET_TYPE_PAGE);
		Label typesLabel = new Label(composite, SWT.NONE);
		typesLabel.setText(WorkbenchMessages.getString("WorkingSetTypePage.typesLabel")); //$NON-NLS-1$
		GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
		typesLabel.setLayoutData(data);
		typesLabel.setFont(font);

		typesListViewer = new TableViewer(composite, SWT.BORDER | SWT.MULTI);
		data = new GridData(GridData.FILL_BOTH);
		data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
		data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
		typesListViewer.getTable().setLayoutData(data);
		typesListViewer.getTable().setFont(font);
		typesListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChanged();
			}
		});
		typesListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				handleDoubleClick();
			}
		});		
		createContent();
		setPageComplete(false);
	}
	/** 
	 * Overrides method in DialogPage
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#dispose()
	 */
	public void dispose() {
		Iterator iterator = icons.values().iterator();
		
		while (iterator.hasNext()) {
			Image icon = (Image) iterator.next();
			icon.dispose();
		}
		super.dispose();
	}
	/**
	 * Returns the page id of the selected working set type.
	 * 
	 * @return the page id of the selected working set type.
	 */
	public String getSelection() {
		ISelection selection = typesListViewer.getSelection();
		boolean hasSelection = selection != null && selection.isEmpty() == false;

		if (hasSelection && selection instanceof IStructuredSelection) {
			WorkingSetDescriptor workingSetDescriptor = (WorkingSetDescriptor) ((IStructuredSelection) selection).getFirstElement();
			return workingSetDescriptor.getId();
		}
		return null;
	}
	/**
	 * Called when a working set type is double clicked.
	 */
	private void handleDoubleClick() {
		handleSelectionChanged();
		getContainer().showPage(getNextPage());
	}
	/**
	 * Called when the selection has changed.
	 */
	private void handleSelectionChanged() {
		ISelection selection = typesListViewer.getSelection();
		boolean hasSelection = selection != null && selection.isEmpty() == false;

		setPageComplete(hasSelection);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4217.java