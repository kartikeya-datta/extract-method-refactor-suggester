error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8412.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8412.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8412.java
text:
```scala
s@@etDescription(WorkbenchMessages.WizardList_description);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.model.AdaptableList;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.WorkbenchViewerSorter;

/**
 * Abstract implementation of a wizard selection page which simply displays a
 * list of specified wizard elements and allows the user to select one to be
 * launched. Subclasses just need to provide a method which creates an
 * appropriate wizard node based upon a user selection.
 */
public abstract class WorkbenchWizardListSelectionPage extends
        WorkbenchWizardSelectionPage implements ISelectionChangedListener,
        IDoubleClickListener {

    // id constants
    private static final String DIALOG_SETTING_SECTION_NAME = "WizardListSelectionPage."; //$NON-NLS-1$

    private final static int SIZING_LISTS_HEIGHT = 200;

    private static final String STORE_SELECTED_WIZARD_ID = DIALOG_SETTING_SECTION_NAME
            + "STORE_SELECTED_WIZARD_ID"; //$NON-NLS-1$

    private TableViewer viewer;

    private String message;

    /**
     * Creates a <code>WorkbenchWizardListSelectionPage</code>.
     * 
     * @param aWorkbench the current workbench
     * @param currentSelection the workbench's current resource selection
     * @param wizardElements the collection of <code>WorkbenchWizardElements</code>
     *            to display for selection
     * @param message the message to display above the selection list
     */
    protected WorkbenchWizardListSelectionPage(IWorkbench aWorkbench,
            IStructuredSelection currentSelection,
            AdaptableList wizardElements, String message) {
        super(
                "singleWizardSelectionPage", aWorkbench, currentSelection, wizardElements); //$NON-NLS-1$
        setDescription(WorkbenchMessages.getString("WizardList.description")); //$NON-NLS-1$
        this.message = message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {

        Font font = parent.getFont();

        // create composite for page.
        Composite outerContainer = new Composite(parent, SWT.NONE);
        outerContainer.setLayout(new GridLayout());
        outerContainer.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
 GridData.HORIZONTAL_ALIGN_FILL));
        outerContainer.setFont(font);

        Label messageLabel = new Label(outerContainer, SWT.NONE);
        messageLabel.setText(message);
        messageLabel.setFont(font);

        createViewer(outerContainer);
        layoutTopControl(viewer.getControl());

        restoreWidgetValues();

        setControl(outerContainer);
    }

    /**
     * Create a new viewer in the parent.
     * 
     * @param parent the parent <code>Composite</code>.
     */
    private void createViewer(Composite parent) {
        //Create a table for the list
        Table table = new Table(parent, SWT.BORDER);
        table.setFont(parent.getFont());

        // the list viewer
        viewer = new TableViewer(table);
        viewer.setContentProvider(new WizardContentProvider());
        viewer.setLabelProvider(new WorkbenchLabelProvider());
        viewer.setSorter(new WorkbenchViewerSorter());
        viewer.addSelectionChangedListener(this);
        viewer.addDoubleClickListener(this);
        viewer.setInput(wizardElements);
    }

    /**
     * Returns an <code>IWizardNode</code> representing the specified
     * workbench wizard which has been selected by the user. <b>Subclasses
     * </b> must override this abstract implementation.
     * 
     * @param element the wizard element that an <code>IWizardNode</code> is
     *            needed for
     * @return org.eclipse.jface.wizards.IWizardNode
     */
    protected abstract IWizardNode createWizardNode(
            WorkbenchWizardElement element);

    /**
     * An item in a viewer has been double-clicked.
     */
    public void doubleClick(DoubleClickEvent event) {
        selectionChanged(new SelectionChangedEvent(event.getViewer(), event
                .getViewer().getSelection()));
        getContainer().showPage(getNextPage());
    }

    /**
     * Layout the top control.
     * 
     * @param control the control.
     * @since 3.0
     */
    private void layoutTopControl(Control control) {
        GridData data = new GridData(GridData.FILL_BOTH);

        int availableRows = DialogUtil.availableRows(control.getParent());

        //Only give a height hint if the dialog is going to be too small
        if (availableRows > 50) {
            data.heightHint = SIZING_LISTS_HEIGHT;
        } else {
            data.heightHint = availableRows * 3;
        }

        control.setLayoutData(data);

    }

    /**
     * Uses the dialog store to restore widget values to the values that they
     * held last time this wizard was used to completion.
     */
    private void restoreWidgetValues() {

        IDialogSettings settings = getDialogSettings();
        if (settings == null)
            return;

        String wizardId = settings.get(STORE_SELECTED_WIZARD_ID);
        WorkbenchWizardElement wizard = findWizard(wizardId);
        if (wizard == null)
            return;

        StructuredSelection selection = new StructuredSelection(wizard);
        viewer.setSelection(selection);
    }

    /**
     * Since Finish was pressed, write widget values to the dialog store so
     * that they will persist into the next invocation of this wizard page
     */
    public void saveWidgetValues() {
        IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
        if (sel.size() > 0) {
            WorkbenchWizardElement selectedWizard = (WorkbenchWizardElement) sel
                    .getFirstElement();
            getDialogSettings().put(STORE_SELECTED_WIZARD_ID,
                    selectedWizard.getId());
        }
    }

    /**
     * Notes the newly-selected wizard element and updates the page
     * accordingly.
     * 
     * @param event the selection changed event
     */
    public void selectionChanged(SelectionChangedEvent event) {
        setErrorMessage(null);
        IStructuredSelection selection = (IStructuredSelection) event
                .getSelection();
        WorkbenchWizardElement currentWizardSelection = (WorkbenchWizardElement) selection
                .getFirstElement();
        if (currentWizardSelection == null) {
            setMessage(null);
            setSelectedNode(null);
            return;
        }

        setSelectedNode(createWizardNode(currentWizardSelection));
        setMessage(currentWizardSelection.getDescription());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8412.java