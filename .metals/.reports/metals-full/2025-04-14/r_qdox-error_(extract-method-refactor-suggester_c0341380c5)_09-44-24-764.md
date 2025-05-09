error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5578.java
text:
```scala
f@@Title.setText(filterTitle);

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation 
 *     Sebastian Davids <sdavids@gmx.de> - Fix for bug 19346 - Dialog font should be
 *       activated and used by other components.
 *******************************************************************************/
package org.eclipse.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.registry.EditorRegistry;

/**
 * The TypeFilteringDialog is a SelectionDialog that allows the user to select a
 * file editor.
 */
public class TypeFilteringDialog extends SelectionDialog {
    Button addTypesButton;

    Collection initialSelections;

    // the visual selection widget group
    CheckboxTableViewer listViewer;

    // sizing constants
    private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;

    private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

    private final static String TYPE_DELIMITER = WorkbenchMessages.TypesFiltering_typeDelimiter; 

    //Define a title for the filter entry field.
    private String filterTitle = WorkbenchMessages.TypesFiltering_otherExtensions; 

    Text userDefinedText;

    IFileEditorMapping[] currentInput;

    /**
     *  Creates a type filtering dialog using the supplied entries. Set the
     * initial selections to those whose extensions match the preselections.
     * @param parentShell The shell to parent the dialog from.
     * @param preselections
     *            of String - a Collection of String to define the preselected
     *            types
     */
    public TypeFilteringDialog(Shell parentShell, Collection preselections) {
        super(parentShell);
        setTitle(WorkbenchMessages.TypesFiltering_title); 
        this.initialSelections = preselections;
        setMessage(WorkbenchMessages.TypesFiltering_message); 
    }

    /**
     * Creates a type filtering dialog using the supplied entries. Set the
     * initial selections to those whose extensions match the preselections.
     * 
     * @param parentShell The shell to parent the dialog from.
     * @param preselections
     *            of String - a Collection of String to define the preselected
     *            types
     * @param filterText -
     *            the title of the text entry field for other extensions.
     */
    public TypeFilteringDialog(Shell parentShell, Collection preselections,
            String filterText) {
        this(parentShell, preselections);
        this.filterTitle = filterText;
    }

    /**
     * Add the selection and deselection buttons to the dialog.
     * 
     * @param composite
     *            org.eclipse.swt.widgets.Composite
     */
    private void addSelectionButtons(Composite composite) {
        Composite buttonComposite = new Composite(composite, SWT.RIGHT);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        buttonComposite.setLayout(layout);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END
 GridData.GRAB_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        composite.setData(data);
        Button selectButton = createButton(buttonComposite,
                IDialogConstants.SELECT_ALL_ID, WorkbenchMessages.WizardTransferPage_selectAll, false);
        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                listViewer.setAllChecked(true);
            }
        };
        selectButton.addSelectionListener(listener);
        Button deselectButton = createButton(buttonComposite,
                IDialogConstants.DESELECT_ALL_ID, WorkbenchMessages.WizardTransferPage_deselectAll, false); 
        listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                listViewer.setAllChecked(false);
            }
        };
        deselectButton.addSelectionListener(listener);
    }

    /**
     * Add the currently-specified extensions to result.
     * @param result
     */
    private void addUserDefinedEntries(List result) {
        StringTokenizer tokenizer = new StringTokenizer(userDefinedText
                .getText(), TYPE_DELIMITER);
        //Allow the *. and . prefix and strip out the extension
        while (tokenizer.hasMoreTokens()) {
            String currentExtension = tokenizer.nextToken().trim();
            if (!currentExtension.equals("")) { //$NON-NLS-1$
                if (currentExtension.startsWith("*."))//$NON-NLS-1$
                    result.add(currentExtension.substring(2));
                else {
                    if (currentExtension.startsWith("."))//$NON-NLS-1$
                        result.add(currentExtension.substring(1));
                    else
                        result.add(currentExtension);
                }
            }
        }
    }

    /**
     * Visually checks the previously-specified elements in this dialog's list
     * viewer.
     */
    private void checkInitialSelections() {
        IFileEditorMapping editorMappings[] = ((EditorRegistry) PlatformUI
				.getWorkbench().getEditorRegistry()).getUnifiedMappings();
        ArrayList selectedMappings = new ArrayList();
        for (int i = 0; i < editorMappings.length; i++) {
            IFileEditorMapping mapping = editorMappings[i];
            //Check for both extension and label matches
            if (this.initialSelections.contains(mapping.getExtension())) {
                listViewer.setChecked(mapping, true);
                selectedMappings.add(mapping.getExtension());
            } else {
                if (this.initialSelections.contains(mapping.getLabel())) {
                    listViewer.setChecked(mapping, true);
                    selectedMappings.add(mapping.getLabel());
                }
            }
        }
        //Now add in the ones not selected to the user defined list
        Iterator initialIterator = this.initialSelections.iterator();
        StringBuffer entries = new StringBuffer();
        while (initialIterator.hasNext()) {
            String nextExtension = (String) initialIterator.next();
            if (!selectedMappings.contains(nextExtension)) {
                entries.append(nextExtension);
                //Only add a comma if we are not at the end
                if (initialIterator.hasNext())
                    entries.append(',');
            }
        }
        this.userDefinedText.setText(entries.toString());
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(shell,
				IWorkbenchHelpContextIds.TYPE_FILTERING_DIALOG);
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent) {
        // page group
        Composite composite = (Composite) super.createDialogArea(parent);
        createMessageArea(composite);
        listViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
        data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
        listViewer.getTable().setLayoutData(data);
        listViewer.getTable().setFont(parent.getFont());
        listViewer.setLabelProvider(FileEditorMappingLabelProvider.INSTANCE);
        listViewer
                .setContentProvider(FileEditorMappingContentProvider.INSTANCE);
        listViewer.setSorter(new ViewerSorter());
        addSelectionButtons(composite);
        createUserEntryGroup(composite);
        initializeViewer();
        // initialize page
        if (this.initialSelections != null && !this.initialSelections.isEmpty())
            checkInitialSelections();
        return composite;
    }

    /**
     * Create the group that shows the user defined entries for the dialog.
     * 
     * @param parent
     *            the parent this is being created in.
     */
    private void createUserEntryGroup(Composite parent) {
        Font font = parent.getFont();
        // destination specification group
        Composite userDefinedGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        userDefinedGroup.setLayout(layout);
        userDefinedGroup.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
        Label fTitle = new Label(userDefinedGroup, SWT.NONE);
        fTitle.setFont(font);
        fTitle.setText(filterTitle); //$NON-NLS-1$
        // user defined entry field
        userDefinedText = new Text(userDefinedGroup, SWT.SINGLE | SWT.BORDER);
        userDefinedText.setFont(font);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
 GridData.GRAB_HORIZONTAL);
        userDefinedText.setLayoutData(data);
    }

    /**
     * Return the input to the dialog.
     * @return IFileEditorMapping[]
     */
    private IFileEditorMapping[] getInput() {
        //Filter the mappings to be just those with a wildcard extension
        if (currentInput == null) {
            List wildcardEditors = new ArrayList();
            IFileEditorMapping[] allMappings = ((EditorRegistry)PlatformUI.getWorkbench()
                    .getEditorRegistry()).getUnifiedMappings();
            for (int i = 0; i < allMappings.length; i++) {
                if (allMappings[i].getName().equals("*"))//$NON-NLS-1$
                    wildcardEditors.add(allMappings[i]);
            }
            currentInput = new IFileEditorMapping[wildcardEditors.size()];
            wildcardEditors.toArray(currentInput);
        }
        return currentInput;
    }

    /**
     * Initializes this dialog's viewer after it has been laid out.
     */
    private void initializeViewer() {
        listViewer.setInput(getInput());
    }

    /**
     * The <code>TypeFilteringDialog</code> implementation of this
     * <code>Dialog</code> method builds a list of the selected elements for
     * later retrieval by the client and closes this dialog.
     */
    protected void okPressed() {
        // Get the input children.
        IFileEditorMapping[] children = getInput();
        List list = new ArrayList();
        // Build a list of selected children.
        for (int i = 0; i < children.length; ++i) {
            IFileEditorMapping element = children[i];
            if (listViewer.getChecked(element))
                list.add(element.getExtension());
        }
        addUserDefinedEntries(list);
        setResult(list);
        super.okPressed();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5578.java