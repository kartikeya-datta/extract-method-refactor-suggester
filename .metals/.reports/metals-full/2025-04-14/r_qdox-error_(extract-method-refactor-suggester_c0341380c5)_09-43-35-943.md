error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8964.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8964.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8964.java
text:
```scala
f@@ilteredTreeFilter = new WizardPatternFilter();

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardContainer2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.model.AdaptableList;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;

/**
 * New wizard selection tab that allows the user to select a registered 'New'
 * wizard to be launched.
 */
class NewWizardNewPage implements ISelectionChangedListener {

    // id constants
    private static final String DIALOG_SETTING_SECTION_NAME = "NewWizardSelectionPage."; //$NON-NLS-1$

    private final static int SIZING_LISTS_HEIGHT = 200;

    private final static int SIZING_VIEWER_WIDTH = 300;

    private final static String STORE_EXPANDED_CATEGORIES_ID = DIALOG_SETTING_SECTION_NAME
            + "STORE_EXPANDED_CATEGORIES_ID"; //$NON-NLS-1$

    private final static String STORE_SELECTED_ID = DIALOG_SETTING_SECTION_NAME
            + "STORE_SELECTED_ID"; //$NON-NLS-1$

    private NewWizardSelectionPage page;
    
    private FilteredTree filteredTree;
    
    private WizardPatternFilter filteredTreeFilter;

    //Keep track of the wizards we have previously selected
    private Hashtable selectedWizards = new Hashtable();

    private IDialogSettings settings;

    private Button showAllCheck;

    private IWizardCategory wizardCategories;

    private IWizardDescriptor [] primaryWizards;

    private ToolItem helpButton;

    private String wizardHelpHref;

    private CLabel descImageCanvas;

    private Map imageTable = new HashMap();

    private IWizardDescriptor selectedElement;

    private WizardActivityFilter filter = new WizardActivityFilter();

    private boolean needShowAll;

	private boolean projectsOnly;

	private ViewerFilter projectFilter = new WizardTagFilter(new String[] {WorkbenchWizardElement.TAG_PROJECT});

    /**
     * Create an instance of this class
     * @param mainPage 
     * @param wizardCategories 
     * @param primaryWizards 
     * @param projectsOnly 
     */
    public NewWizardNewPage(NewWizardSelectionPage mainPage,
			IWizardCategory wizardCategories,
			IWizardDescriptor[] primaryWizards, boolean projectsOnly) {
        this.page = mainPage;
        this.wizardCategories = wizardCategories;
        this.primaryWizards = primaryWizards;
        this.projectsOnly = projectsOnly;

        trimPrimaryWizards();

        if (this.primaryWizards.length > 0) {
            if (allPrimary(wizardCategories)) {
                this.wizardCategories = null; // dont bother considering the categories as all wizards are primary
                needShowAll = false;
            } else {
                needShowAll = !allActivityEnabled(wizardCategories);
            }
        } else {
            needShowAll = !allActivityEnabled(wizardCategories);
        }
    }

    /**
     * @param category the wizard category
     * @return whether all of the wizards in the category are enabled via activity filtering
     */
    private boolean allActivityEnabled(IWizardCategory category) {
        IWizardDescriptor [] wizards = category.getWizards();
        for (int i = 0; i < wizards.length; i++) {
            IWizardDescriptor wizard = wizards[i];
            if (WorkbenchActivityHelper.filterItem(wizard))
                return false;
        }

        IWizardCategory [] children = category.getCategories();
        for (int i = 0; i < children.length; i++) {
            if (!allActivityEnabled(children[i]))
                return false;
        }

        return true;
    }

    /**
     * Remove all primary wizards that are not in the wizard collection
     */
    private void trimPrimaryWizards() {
        ArrayList newPrimaryWizards = new ArrayList(primaryWizards.length);

        if (wizardCategories == null)
            return;//No categories so nothing to trim

        for (int i = 0; i < primaryWizards.length; i++) {
            if (wizardCategories.findWizard(primaryWizards[i].getId()) != null)
                newPrimaryWizards.add(primaryWizards[i]);
        }

        primaryWizards = (WorkbenchWizardElement[]) newPrimaryWizards
                .toArray(new WorkbenchWizardElement[newPrimaryWizards.size()]);
    }

    /**
     * @param category the wizard category
     * @return whether all wizards in the category are considered primary
     */
    private boolean allPrimary(IWizardCategory category) {
        IWizardDescriptor [] wizards = category.getWizards();
        for (int i = 0; i < wizards.length; i++) {
        	IWizardDescriptor wizard = wizards[i];
            if (!isPrimary(wizard))
                return false;
        }

        IWizardCategory [] children = category.getCategories();
        for (int i = 0; i < children.length; i++) {
            if (!allPrimary(children[i]))
                return false;
        }

        return true;
    }

    /**
     * @param wizard
     * @return whether the given wizard is primary
     */
    private boolean isPrimary(IWizardDescriptor wizard) {
        for (int j = 0; j < primaryWizards.length; j++) {
            if (primaryWizards[j].equals(wizard))
                return true;
        }

        return false;
    }

    /**
     * @since 3.0
     */
    public void activate() {
        page.setDescription(WorkbenchMessages.NewWizardNewPage_description);
    }

    /**
     * Create this tab's visual components
     * 
     * @param parent Composite
     * @return Control
     */
    protected Control createControl(Composite parent) {

        Font wizardFont = parent.getFont();
        // top level group
        Composite outerContainer = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        outerContainer.setLayout(layout);

        Label wizardLabel = new Label(outerContainer, SWT.NONE);
        GridData data = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
        outerContainer.setLayoutData(data);
        wizardLabel.setFont(wizardFont);
        wizardLabel.setText(WorkbenchMessages.NewWizardNewPage_wizardsLabel);    

        Composite innerContainer = new Composite(outerContainer, SWT.NONE);
        layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        innerContainer.setLayout(layout);
        innerContainer.setFont(wizardFont);
        data = new GridData(SWT.FILL, SWT.FILL, true, true);	
        innerContainer.setLayoutData(data);

        filteredTree = createFilteredTree(innerContainer);
        createOptionsButtons(innerContainer);
        
        createImage(innerContainer);

        updateDescription(null);

        // wizard actions pane...create SWT table directly to
        // get single selection mode instead of multi selection.
        restoreWidgetValues();

        return outerContainer;
    }

    /**
     * Create a new FilteredTree in the parent.
     * 
     * @param parent the parent <code>Composite</code>.
     * @since 3.0
     */
    protected FilteredTree createFilteredTree(Composite parent){
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);
        
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.widthHint = SIZING_VIEWER_WIDTH;
        data.horizontalSpan = 2;	 
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;

        boolean needsHint = DialogUtil.inRegularFontMode(parent);

        //Only give a height hint if the dialog is going to be too small
        if (needsHint) {
            data.heightHint = SIZING_LISTS_HEIGHT;
        }
        composite.setLayoutData(data);

        filteredTreeFilter = new WizardPatternFilter(true);
    	FilteredTree filterTree = new FilteredTree(composite, 
    			SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER, filteredTreeFilter);
  	
		final TreeViewer treeViewer = filterTree.getViewer();
		treeViewer.setContentProvider(new WizardContentProvider());
		treeViewer.setLabelProvider(new WorkbenchLabelProvider());
		treeViewer.setSorter(NewWizardCollectionSorter.INSTANCE);
		treeViewer.addSelectionChangedListener(this);

        ArrayList inputArray = new ArrayList();

        for (int i = 0; i < primaryWizards.length; i++) {
            inputArray.add(primaryWizards[i]);
        }

        boolean expandTop = false;

        if (wizardCategories != null) {
            if (wizardCategories.getParent() == null) {
                IWizardCategory [] children = wizardCategories.getCategories();
                for (int i = 0; i < children.length; i++) {
                    inputArray.add(children[i]);
                }
            } else {
                expandTop = true;
                inputArray.add(wizardCategories);
            }
        }

        // ensure the category is expanded.  If there is a remembered expansion it will be set later.
        if (expandTop)
        	treeViewer.setAutoExpandLevel(2);

        AdaptableList input = new AdaptableList(inputArray);

        treeViewer.setInput(input);

		filterTree.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));

        treeViewer.getTree().setFont(parent.getFont());

        treeViewer.addDoubleClickListener(new IDoubleClickListener() {
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
             */
            public void doubleClick(DoubleClickEvent event) {
            	    IStructuredSelection s = (IStructuredSelection) event
						.getSelection();
				selectionChanged(new SelectionChangedEvent(event.getViewer(), s));
				
				Object element = s.getFirstElement();
                if (treeViewer.isExpandable(element)) {
                	treeViewer.setExpandedState(element, !treeViewer
                            .getExpandedState(element));
                } else if (element instanceof WorkbenchWizardElement) {
                    page.advanceToNextPageOrFinish();
                }
            }
        });
        
        treeViewer.addFilter(filter);
        
        if (projectsOnly) 
        	treeViewer.addFilter(projectFilter);

		Dialog.applyDialogFont(filterTree);
		return filterTree;
    }
    
    /**
     * Create the Show All and help buttons at the bottom of the page.
     * 
     * @param parent the parent composite on which to create the widgets
     */
    private void createOptionsButtons(Composite parent){
        if (needShowAll) {
            showAllCheck = new Button(parent, SWT.CHECK);
            GridData data = new GridData();
            showAllCheck.setLayoutData(data);
            showAllCheck.setFont(parent.getFont());
            showAllCheck.setText(WorkbenchMessages.NewWizardNewPage_showAll); 
            showAllCheck.setSelection(false);

            // flipping tabs updates the selected node
            showAllCheck.addSelectionListener(new SelectionAdapter() {

                // the delta of expanded elements between the last 'show all'
                // and the current 'no show all'
                private Object[] delta = new Object[0];

                public void widgetSelected(SelectionEvent e) {
                    boolean showAll = showAllCheck.getSelection();

                    if (showAll) {
                    	filteredTree.getViewer().getControl().setRedraw(false);
                    } else {
                        // get the inital expanded elements when going from show
                        // all-> no show all.
                        // this isnt really the delta yet, we're just reusing
                        // the variable.
                        delta = filteredTree.getViewer().getExpandedElements();
                    }

                    try {
                        if (showAll) {
                        	filteredTree.getViewer().resetFilters();
                        	filteredTree.getViewer().addFilter(filteredTreeFilter);
                            if (projectsOnly) 
                            	filteredTree.getViewer().addFilter(projectFilter);

                            // restore the expanded elements that were present
                            // in the last show all state but not in the 'no
                            // show all' state.
                            Object[] currentExpanded = filteredTree.getViewer()
                                    .getExpandedElements();
                            Object[] expanded = new Object[delta.length
                                    + currentExpanded.length];
                            System.arraycopy(currentExpanded, 0, expanded, 0,
                                    currentExpanded.length);
                            System.arraycopy(delta, 0, expanded,
                                    currentExpanded.length, delta.length);
                            filteredTree.getViewer().setExpandedElements(expanded);
                        } else {
                        	filteredTree.getViewer().addFilter(filter);
                            if (projectsOnly) 
                            	filteredTree.getViewer().addFilter(projectFilter);
                        }
                        filteredTree.getViewer().refresh(false);

                        if (!showAll) {
                            // if we're going from show all -> no show all
                            // record the elements that were expanded in the
                            // 'show all' state but not the 'no show all' state
                            // (because they didnt exist).
                            Object[] newExpanded = filteredTree.getViewer().getExpandedElements();
                            List deltaList = new ArrayList(Arrays.asList(delta));
                            deltaList.removeAll(Arrays.asList(newExpanded));
                        }
                    } finally {
                        if (showAll)
                        	filteredTree.getViewer().getControl().setRedraw(true);
                    }
                }
            });
        }

        Image buttonImage = WorkbenchImages
                .getImage(IWorkbenchGraphicConstants.IMG_LCL_LINKTO_HELP);
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
        helpButton = new ToolItem(toolBar, SWT.NONE);
        helpButton.setImage(buttonImage);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END
 GridData.VERTICAL_ALIGN_END);
        if (!needShowAll)
            data.horizontalSpan = 2;
        toolBar.setLayoutData(data);

        helpButton.addSelectionListener(new SelectionAdapter() {

            /* (non-Javadoc)
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetSelected(SelectionEvent e) {
            	PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(
						wizardHelpHref);
            }
        });
    }
    
    /**
     * Create the image controls.
     * 
     * @param parent the parent <code>Composite</code>.
     * @since 3.0
     */
    private void createImage(Composite parent) {
        descImageCanvas = new CLabel(parent, SWT.NONE);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
 GridData.VERTICAL_ALIGN_BEGINNING);
        descImageCanvas.setLayoutData(data);

        // hook a listener to get rid of cached images.
        descImageCanvas.addDisposeListener(new DisposeListener() {

            /* (non-Javadoc)
             * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
             */
            public void widgetDisposed(DisposeEvent e) {
                for (Iterator i = imageTable.values().iterator(); i.hasNext();) {
                    ((Image) i.next()).dispose();
                }
                imageTable.clear();
            }
        });
    }

    /**
     * Expands the wizard categories in this page's category viewer that were
     * expanded last time this page was used. If a category that was previously
     * expanded no longer exists then it is ignored.
     */
    protected void expandPreviouslyExpandedCategories() {
        String[] expandedCategoryPaths = settings
                .getArray(STORE_EXPANDED_CATEGORIES_ID);
        if (expandedCategoryPaths == null || expandedCategoryPaths.length == 0)
            return;

        List categoriesToExpand = new ArrayList(expandedCategoryPaths.length);

        if (wizardCategories != null) {
            for (int i = 0; i < expandedCategoryPaths.length; i++) {
                IWizardCategory category = wizardCategories
                        .findCategory(new Path(expandedCategoryPaths[i]));
                if (category != null) // ie.- it still exists
                    categoriesToExpand.add(category);
            }
        }

        if (!categoriesToExpand.isEmpty())
        	filteredTree.getViewer().setExpandedElements(categoriesToExpand.toArray());

    }

    /**
     * Returns the single selected object contained in the passed
     * selectionEvent, or <code>null</code> if the selectionEvent contains
     * either 0 or 2+ selected objects.
     */
    protected Object getSingleSelection(IStructuredSelection selection) {
        return selection.size() == 1 ? selection.getFirstElement() : null;
    }

    /**
     * Set self's widgets to the values that they held last time this page was
     * open
     *  
     */
    protected void restoreWidgetValues() {
        expandPreviouslyExpandedCategories();
        selectPreviouslySelected();
    }

    /**
     * Store the current values of self's widgets so that they can be restored
     * in the next instance of self
     *  
     */
    public void saveWidgetValues() {
        storeExpandedCategories();
        storeSelectedCategoryAndWizard();
    }

    /**
     * The user selected either new wizard category(s) or wizard element(s).
     * Proceed accordingly.
     * 
     * @param selectionEvent ISelection
     */
    public void selectionChanged(SelectionChangedEvent selectionEvent) {
        page.setErrorMessage(null);
        page.setMessage(null);

        Object selectedObject = getSingleSelection((IStructuredSelection) selectionEvent
                .getSelection());

        if (selectedObject instanceof IWizardDescriptor) {
            if (selectedObject == selectedElement)
                return;
            updateWizardSelection((IWizardDescriptor) selectedObject);
        } else {
            selectedElement = null;
            page.setHasPages(false);
            page.setCanFinishEarly(false);
            page.selectWizardNode(null);
            updateDescription(null);
        }
    }

    /**
     * Selects the wizard category and wizard in this page that were selected
     * last time this page was used. If a category or wizard that was
     * previously selected no longer exists then it is ignored.
     */
    protected void selectPreviouslySelected() {
        String selectedId = settings.get(STORE_SELECTED_ID);
        if (selectedId == null)
            return;

        if (wizardCategories == null)
            return;

        Object selected = wizardCategories.findCategory(new Path(
                selectedId));

        if (selected == null) {
            selected = wizardCategories.findWizard(selectedId);

            if (selected == null)
                // if we cant find either a category or a wizard, abort.
                return;
        }

        //work around for 62039
        final StructuredSelection selection = new StructuredSelection(selected);
        filteredTree.getViewer().getControl().getDisplay().asyncExec(new Runnable() {
            public void run() {
            	filteredTree.getViewer().setSelection(selection, true);
            }
        });
    }

    /**
     * Set the dialog store to use for widget value storage and retrieval
     * 
     * @param settings IDialogSettings
     */
    public void setDialogSettings(IDialogSettings settings) {
        this.settings = settings;
    }

    /**
     * Stores the collection of currently-expanded categories in this page's
     * dialog store, in order to recreate this page's state in the next
     * instance of this page.
     */
    protected void storeExpandedCategories() {
        Object[] expandedElements = filteredTree.getViewer().getExpandedElements();
        List expandedElementPaths = new ArrayList(expandedElements.length);
        for (int i = 0; i < expandedElements.length; ++i) {
            if (expandedElements[i] instanceof IWizardCategory)
                expandedElementPaths
                        .add(((IWizardCategory) expandedElements[i])
                                .getPath().toString());
        }
        settings.put(STORE_EXPANDED_CATEGORIES_ID,
                (String[]) expandedElementPaths
                        .toArray(new String[expandedElementPaths.size()]));
    }

    /**
     * Stores the currently-selected element in this page's dialog store, in
     * order to recreate this page's state in the next instance of this page.
     */
    protected void storeSelectedCategoryAndWizard() {
        Object selected = getSingleSelection((IStructuredSelection) filteredTree
        		.getViewer().getSelection());

        if (selected != null) {
            if (selected instanceof IWizardCategory)
                settings.put(STORE_SELECTED_ID,
                        ((IWizardCategory) selected).getPath()
                                .toString());
            else
                // else its a wizard
                settings.put(STORE_SELECTED_ID,
                        ((IWizardDescriptor) selected).getId());
        }
    }

    /**
     * Update the current description controls.
     * 
     * @param selectedObject the new wizard
     * @since 3.0
     */
    private void updateDescription(IWizardDescriptor selectedObject) {
        String string = ""; //$NON-NLS-1$
        if (selectedObject != null)
            string = selectedObject.getDescription();

        page.setDescription(string);

        if (selectedObject != null) {
            wizardHelpHref = selectedObject.getHelpHref();
        } else {
            wizardHelpHref = null;
        }

        if (wizardHelpHref != null) {
            helpButton.setEnabled(true);
            helpButton.setToolTipText(WorkbenchMessages.NewWizardNewPage_moreHelp);
        } else {
            helpButton.setEnabled(false);
            helpButton.setToolTipText(WorkbenchMessages.NewWizardNewPage_noHelp);
        }

        if (hasImage(selectedObject)) {
            ImageDescriptor descriptor = null;
            if (selectedObject != null) {
                descriptor = selectedObject.getDescriptionImage();
            }

            if (descriptor != null) {
                Image image = (Image) imageTable.get(descriptor);
                if (image == null) {
                    image = descriptor.createImage(false);
                    imageTable.put(descriptor, image);
                }
                descImageCanvas.setImage(image);
            }
        } else {
            descImageCanvas.setImage(null);
        }

        descImageCanvas.getParent().layout(true);

        IWizardContainer container = page.getWizard().getContainer();
        if (container instanceof IWizardContainer2) {
            ((IWizardContainer2) container).updateSize();
        }
    }

    /**
     * Tests whether the given wizard has an associated image.
     * 
     * @param selectedObject the wizard to test
     * @return whether the given wizard has an associated image
     */
    private boolean hasImage(IWizardDescriptor selectedObject) {
        if (selectedObject == null)
            return false;

        if (selectedObject.getDescriptionImage() != null)
            return true;

        return false;
    }

    /**
     * @param selectedObject
     */
    private void updateWizardSelection(IWizardDescriptor selectedObject) {
        selectedElement = selectedObject;
        WorkbenchWizardNode selectedNode;
        if (selectedWizards.containsKey(selectedObject)) {
            selectedNode = (WorkbenchWizardNode) selectedWizards
                    .get(selectedObject);
        } else {
            selectedNode = new WorkbenchWizardNode(page, selectedObject) {
                public IWorkbenchWizard createWizard() throws CoreException {
                    return wizardElement.createWizard();
                }
            };
            selectedWizards.put(selectedObject, selectedNode);
        }

        page.setCanFinishEarly(selectedObject.canFinishEarly());
        page.setHasPages(selectedObject.hasPages());
        page.selectWizardNode(selectedNode);

        updateDescription(selectedObject);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8964.java