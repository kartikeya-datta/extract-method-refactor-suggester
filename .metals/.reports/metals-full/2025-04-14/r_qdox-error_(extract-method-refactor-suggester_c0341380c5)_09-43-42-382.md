error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3340.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3340.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3340.java
text:
```scala
t@@oolBar = new ToolBar(composite,  SWT.RIGHT | SWT.FLAT);

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

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.misc.Assert;

/**
 * Prefence dialog for the workbench including the ability to load/save
 * preferences.
 */
public class WorkbenchPreferenceDialog extends FilteredPreferenceDialog {
    /**
     * The Load button id.
     */
    private final static int LOAD_ID = IDialogConstants.CLIENT_ID + 1;

    /**
     * The Save button id.
     */
    private final static int SAVE_ID = IDialogConstants.CLIENT_ID + 2;

    /**
     * The dialog settings key for the last used import/export path.
     */
    final static String FILE_PATH_SETTING = "PreferenceImportExportFileSelectionPage.filePath"; //$NON-NLS-1$

    /**
     * There can only ever be one instance of the workbench's preference dialog.
     * This keeps a handle on this instance, so that attempts to create a second
     * dialog should just fail (or return the original instance).
     * 
     * @since 3.1
     */
    private static WorkbenchPreferenceDialog instance = null;

    private static String MODE_ICON = "org.eclipse.ui.internal.dialogs.MODE_ICON";//$NON-NLS-1$

    private static boolean groupedMode = false;

    static {
        ImageDescriptor descriptor = AbstractUIPlugin
                .imageDescriptorFromPlugin(PlatformUI.PLUGIN_ID,
                        "icons/full/obj16/layout_co.gif"); //$NON-NLS-1$
        if (descriptor != null) {
            JFaceResources.getImageRegistry().put(MODE_ICON, descriptor);
        }
        groupedMode = ((WorkbenchPreferenceManager) WorkbenchPlugin
                .getDefault().getPreferenceManager()).getGroups().length > 0;
    }

    private Composite toolBarComposite;

    private ToolBar toolBar;

    private FilteredTree filteredTree;
    
    /**
     * The preference page history.
     * 
     * @since 3.1
     */
    private PreferencePageHistory history;

    private WorkbenchPreferenceGroup currentGroup;

    private CTabItem tab;

    // The id of the last page that was selected
    private static String lastGroupId = null;
    
    private Object pageData;

    /**
     * Creates a workbench preference dialog to a particular preference page.
     * Show the other pages as filtered results using whatever filtering
     * criteria the search uses. It is the responsibility of the caller to then
     * call <code>open()</code>. The call to <code>open()</code> will not
     * return until the dialog closes, so this is the last chance to manipulate
     * the dialog.
     * 
     * @param preferencePageId
     *            The identifier of the preference page to open; may be
     *            <code>null</code>. If it is <code>null</code>, then the
     *            preference page is not selected or modified in any way.
     * @param filteredIds
     *            The ids of the other pages to be highlighted using the same
     *            filtering criterea as search.
     * @return The selected preference page.
     * @since 3.1
     * @see #createDialogOn(String)
     */
    public static final WorkbenchPreferenceDialog createDialogOn(
            final String preferencePageId, String[] filteredIds) {
        WorkbenchPreferenceDialog dialog = createDialogOn(preferencePageId);
        dialog.setSearchResults(filteredIds);
        return dialog;
    }

    /**
     * Creates a workbench preference dialog to a particular preference page. It
     * is the responsibility of the caller to then call <code>open()</code>.
     * The call to <code>open()</code> will not return until the dialog
     * closes, so this is the last chance to manipulate the dialog.
     * 
     * @param preferencePageId
     *            The identifier of the preference page to open; may be
     *            <code>null</code>. If it is <code>null</code>, then the
     *            preference page is not selected or modified in any way.
     * @return The selected preference page.
     * @since 3.1
     */
    public static final WorkbenchPreferenceDialog createDialogOn(
            final String preferencePageId) {
        final WorkbenchPreferenceDialog dialog;

        if (instance == null) {
            /*
             * There is no existing preference dialog, so open a new one with
             * the given selected page.
             */

            // Determine a decent parent shell.
            final IWorkbench workbench = PlatformUI.getWorkbench();
            final IWorkbenchWindow workbenchWindow = workbench
                    .getActiveWorkbenchWindow();
            final Shell parentShell;
            if (workbenchWindow != null) {
                parentShell = workbenchWindow.getShell();
            } else {
                parentShell = null;
            }

            // Create the dialog
            final PreferenceManager preferenceManager = PlatformUI
                    .getWorkbench().getPreferenceManager();
            dialog = new WorkbenchPreferenceDialog(parentShell,
                    preferenceManager);
            if (preferencePageId != null) {
                dialog.setSelectedNode(preferencePageId);
            }
            dialog.create();
            WorkbenchHelp.setHelp(dialog.getShell(),
                    IWorkbenchHelpContextIds.PREFERENCE_DIALOG);

        } else {
            /*
             * There is an existing preference dialog, so let's just select the
             * given preference page.
             */
            dialog = instance;
            if (preferencePageId != null) {
                dialog.setCurrentPageId(preferencePageId);
            }

        }

        // Get the selected node, and return it.
        return dialog;
    }

    /**
     * Creates a new preference dialog under the control of the given preference
     * manager.
     * 
     * @param parentShell
     *            the parent shell
     * @param manager
     *            the preference manager
     */
    public WorkbenchPreferenceDialog(Shell parentShell,
            PreferenceManager manager) {
        super(parentShell, manager);
        Assert
                .isTrue((instance == null),
                        "There cannot be two preference dialogs at once in the workbench."); //$NON-NLS-1$
        instance = this;
        history = new PreferencePageHistory(this);
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    protected void buttonPressed(int buttonId) {
        switch (buttonId) {
        case LOAD_ID: {
            loadPressed();
            return;
        }
        case SAVE_ID: {
            savePressed();
            return;
        }
        }
        super.buttonPressed(buttonId);
    }

    /**
     * Closes the preference dialog. This clears out the singleton instance
     * before calling the super implementation.
     * 
     * @return <code>true</code> if the dialog is (or was already) closed, and
     *         <code>false</code> if it is still open
     * @since 3.1
     */
    public final boolean close() {
        instance = null;
        history.dispose();
        return super.close();
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    protected void createButtonsForButtonBar(Composite parent) {
        parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        createButton(parent, LOAD_ID, WorkbenchMessages
                .getString("WorkbenchPreferenceDialog.load"), false); //$NON-NLS-1$
        createButton(parent, SAVE_ID, WorkbenchMessages
                .getString("WorkbenchPreferenceDialog.save"), false); //$NON-NLS-1$

        Label l = new Label(parent, SWT.NONE);
        l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        l = new Label(parent, SWT.NONE);
        l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        GridLayout layout = (GridLayout) parent.getLayout();
        layout.numColumns += 3;
        layout.makeColumnsEqualWidth = false;

        super.createButtonsForButtonBar(parent);
    }

    /**
     * Handle a request to load preferences
     */
    protected void loadPressed() {
        final IPath filePath = getFilePath(false);
        if (filePath == null)
            return;
        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Runnable#run()
             */
            public void run() {
                importPreferences(filePath);
            }
        });

        close();
    }

    /**
     * Get the file name we are using. Set the button type flag depending on
     * whether it is import or export operation.
     * 
     * @param export
     *            <code>true</code> if an export file name is being looked
     *            for.
     * 
     * @return IPath or <code>null</code> if no selection is mage.
     */
    private IPath getFilePath(boolean export) {

        // Find the closest file/directory to what is currently entered.
        String currentFileName = getFileNameSetting(export);

        // Open a dialog allowing the user to choose.
        FileDialog fileDialog = null;
        if (export)
            fileDialog = new FileDialog(getShell(), SWT.SAVE);
        else
            fileDialog = new FileDialog(getShell(), SWT.OPEN);

        if (currentFileName != null)
            fileDialog.setFileName(currentFileName);
        fileDialog
                .setFilterExtensions(PreferenceImportExportFileSelectionPage.DIALOG_PREFERENCE_EXTENSIONS);
        currentFileName = fileDialog.open();

        if (currentFileName == null)
            return null;

        /*
         * Append the default filename if none was specifed and such a file does
         * not exist.
         */
        String fileName = new File(currentFileName).getName();
        if (fileName.lastIndexOf(".") == -1) { //$NON-NLS-1$
            currentFileName += AbstractPreferenceImportExportPage.PREFERENCE_EXT;
        }
        setFileNameSetting(currentFileName);
        return new Path(currentFileName);

    }

    /**
     * @param currentFileName
     */
    private void setFileNameSetting(String currentFileName) {
        if (currentFileName != null)
            WorkbenchPlugin.getDefault().getDialogSettings().put(
                    WorkbenchPreferenceDialog.FILE_PATH_SETTING,
                    currentFileName);

    }

    /**
     * Return the file name setting or a default value if there isn't one.
     * 
     * @param export
     *            <code>true</code> if an export file name is being looked
     *            for.
     * 
     * @return String if there is a good value to choose. Otherwise return
     *         <code>null</code>.
     */
    private String getFileNameSetting(boolean export) {

        String lastFileName = WorkbenchPlugin.getDefault().getDialogSettings()
                .get(WorkbenchPreferenceDialog.FILE_PATH_SETTING);
        if (lastFileName == null) {
            if (export)
                return System.getProperty("user.dir") + System.getProperty("file.separator") + WorkbenchMessages.getString("ImportExportPages.preferenceFileName") + AbstractPreferenceImportExportPage.PREFERENCE_EXT; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

        } else if ((export) || (new File(lastFileName).exists())) {
            return lastFileName;
        }

        return null;
    }

    /**
     * Handle a request to save preferences
     */
    protected void savePressed() {
        new PreferencesExportDialog(getShell()).open();
        close();
    }

    /**
     * Import a preference file.
     * 
     * @param path
     *            The file path.
     * @return true if successful.
     */
    private boolean importPreferences(IPath path) {
        IStatus status = Preferences.validatePreferenceVersions(path);
        if (status.getSeverity() == IStatus.ERROR) {
            // Show the error and about
            ErrorDialog.openError(getShell(), WorkbenchMessages
                    .getString("WorkbenchPreferenceDialog.loadErrorTitle"), //$NON-NLS-1$
                    WorkbenchMessages.format(
                            "WorkbenchPreferenceDialog.verifyErrorMessage", //$NON-NLS-1$
                            new Object[] { path.toOSString() }), status);
            return false;
        } else if (status.getSeverity() == IStatus.WARNING) {
            // Show the warning and give the option to continue
            int result = PreferenceErrorDialog
                    .openError(
                            getShell(),
                            WorkbenchMessages
                                    .getString("WorkbenchPreferenceDialog.loadErrorTitle"), //$NON-NLS-1$
                            WorkbenchMessages
                                    .format(
                                            "WorkbenchPreferenceDialog.verifyWarningMessage", //$NON-NLS-1$
                                            new Object[] { path.toOSString() }),
                            status);
            if (result != Window.OK) {
                return false;
            }
        }

        try {
            Preferences.importPreferences(path);
        } catch (CoreException e) {
            ErrorDialog.openError(getShell(), WorkbenchMessages
                    .getString("WorkbenchPreferenceDialog.loadErrorTitle"), //$NON-NLS-1$
                    WorkbenchMessages.format(
                            "WorkbenchPreferenceDialog.loadErrorMessage", //$NON-NLS-1$
                            new Object[] { path.toOSString() }), e.getStatus());
            return false;
        }
        return true;
    }

    /**
     * Returns the currently selected page. This can be used in conjuction with
     * <code>createDialogOn</code> to create a dialog, manipulate the
     * preference page, and then display it to the user.
     * 
     * @return The currently selected page; this value may be <code>null</code>
     *         if there is no selected page.
     * @since 3.1
     */
    public final IPreferencePage getCurrentPage() {
        return super.getCurrentPage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.dialogs.FilteredPreferenceDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent) {

        // create a composite with standard margins and spacing
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setBackground(composite.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));

        toolBarComposite = new Composite(composite, SWT.NONE);
        GridLayout toolBarLayout = new GridLayout();
        toolBarLayout.marginHeight = 0;
        toolBarLayout.marginWidth = 0;
        toolBarComposite.setLayout(toolBarLayout);
        toolBarComposite.setBackground(composite.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));
        toolBarComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
 GridData.GRAB_HORIZONTAL));

        if (groupedMode)
            createToolBar(toolBarComposite);

        createDialogContents(composite);

        applyDialogFont(composite);

        return composite;

    }

    /**
     * Create the toolbar for the groups.
     * 
     * @param composite
     */
    private void createToolBar(Composite composite) {

        toolBar = new ToolBar(composite, SWT.HORIZONTAL | SWT.CENTER | SWT.FLAT);
        toolBar.setBackground(composite.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));

        WorkbenchPreferenceGroup[] groups = getGroups();

        for (int i = 0; i < groups.length; i++) {
            final WorkbenchPreferenceGroup group = groups[i];
            ToolItem newItem = new ToolItem(toolBar, SWT.RADIO);
            newItem.setText(group.getName());
            newItem.setImage(group.getImage());
            newItem.setData(group);
            newItem.addSelectionListener(new SelectionAdapter() {
                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                 */
                public void widgetSelected(SelectionEvent e) {
                    groupSelected(group);
                }
            });

        }

        GridData data = new GridData(GridData.GRAB_HORIZONTAL
 GridData.FILL_HORIZONTAL);
        data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
        data.horizontalIndent = IDialogConstants.HORIZONTAL_MARGIN;
        data.verticalIndent = IDialogConstants.VERTICAL_MARGIN;
        toolBar.setLayoutData(data);
    }

    /**
     * Create the contents area of the dialog
     * 
     * @param parent
     */
    private void createDialogContents(Composite parent) {

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setBackground(parent.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;
        int columns = 3;
        layout.numColumns = columns;
        composite.setLayout(layout);
        GridData compositeData = new GridData(GridData.FILL_BOTH);
        //compositeData.horizontalIndent = IDialogConstants.HORIZONTAL_MARGIN;
        composite.setLayoutData(compositeData);
        applyDialogFont(composite);
        composite.setBackground(composite.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));

        Control treeControl = createTreeAreaContents(composite);
        createSash(composite, treeControl);

        Composite pageAreaComposite = new Composite(composite, SWT.NONE);
        pageAreaComposite.setBackground(composite.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));

        pageAreaComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout pageAreaLayout = new GridLayout();
        pageAreaLayout.marginHeight = 0;
        pageAreaLayout.marginWidth = 0;
        pageAreaLayout.horizontalSpacing = 0;

        pageAreaComposite.setLayout(pageAreaLayout);

        // Build the Page container
        setPageContainer(createPageContainer(pageAreaComposite));
        getPageContainer().setLayoutData(new GridData(GridData.FILL_BOTH));
        Label separator = new Label(composite, SWT.HORIZONTAL
 SWT.SEPARATOR);
        separator.setBackground(separator.getDisplay().getSystemColor(SWT.COLOR_CYAN));
        GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gd.horizontalSpan = columns;
        separator.setLayoutData(gd);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#selectSavedItem()
     */
    protected void selectSavedItem() {
        if (showingGroups()) {
            WorkbenchPreferenceGroup[] groups = getGroups();

            if (lastGroupId != null) {
                for (int i = 0; i < groups.length; i++) {
                    if (lastGroupId.equals(groups[i].getId())) {
                        selectAndRevealInToolBar(groups[i]);
                        return;
                    }
                }
            }
            selectAndRevealInToolBar(groups[0]);
        } else {
            getTreeViewer().setInput(getPreferenceManager());
            super.selectSavedItem();

        }
    }

    /**
     * Select the group and reveal it in the toolbar.
     * 
     * @param group
     */
    private void selectAndRevealInToolBar(WorkbenchPreferenceGroup group) {
        selectGroupInToolBar(group);
        groupSelected(group);
    }

    /**
     * A group has been selected. Update the tree viewer.
     * 
     * @param group
     *            tool item.
     */
    private void groupSelected(final WorkbenchPreferenceGroup group) {

        lastGroupId = group.getId();
        currentGroup = group;

        getTreeViewer().setInput(group);
        Object selection = group.getLastSelection();
        if (selection == null)
            selection = group.getPreferenceNodes()[0];
        getTreeViewer().setSelection(new StructuredSelection(selection), true);
    }

    /**
     * @param group
     */
    private void selectGroupInToolBar(final WorkbenchPreferenceGroup group) {
        ToolItem[] items = toolBar.getItems();
        for (int i = 0; i < items.length; i++) {
            if (group.equals(items[i].getData())) {
                items[i].setSelection(true);
                break;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#createPageContainer(org.eclipse.swt.widgets.Composite)
     */
    public Composite createPageContainer(Composite parent) {

      

        CTabFolder parentFolder = new CTabFolder(parent, SWT.BORDER);
        parentFolder.setSingle(true);
        // parentFolder.setSimple(
        // PrefUtil.getAPIPreferenceStore().
        // getBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
        tab = new CTabItem(parentFolder, SWT.BORDER);

        parentFolder.setSelectionForeground(parent.getDisplay().getSystemColor(
                SWT.COLOR_WIDGET_FOREGROUND));
        parentFolder.setSelectionBackground(parent.getDisplay().getSystemColor(
                SWT.COLOR_WIDGET_BACKGROUND));
        // parentFolder.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        tab.setFont(JFaceResources.getFontRegistry().get(
                JFaceResources.BANNER_FONT));

        Control topBar = getContainerToolBar(parentFolder);
        parentFolder.setTopRight(topBar, SWT.RIGHT);
        int height = topBar.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
        parentFolder.setTabHeight(height);

        parentFolder.setLayoutData(new GridData(GridData.FILL_BOTH
 GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
        
        Composite outer = new Composite(parentFolder, SWT.NULL);
        GridLayout layout = new GridLayout();
        outer.setLayout(layout);

        Composite result = new Composite(outer, SWT.NULL);
        result.setLayout(getPageLayout());
        result.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
        tab.setControl(outer);
        parentFolder.setSelection(0);
        return result;
    }

    /**
     * Get the toolbar for the container
     * 
     * @return Control
     */
    private Control getContainerToolBar(Composite composite) {

        ToolBar historyBar = new ToolBar(composite, SWT.HORIZONTAL | SWT.FLAT);
        ToolBarManager historyManager = new ToolBarManager(historyBar);

        history.createHistoryControls(historyBar, historyManager);
        createModeSwitch(historyManager);

        historyManager.update(false);
       
        return historyBar;
    }

    /**
     * Create the button that switches modes.
     * 
     * @param historyManager
     */
    private void createModeSwitch(ToolBarManager historyManager) {

        if (!canShowGroups())
            return;

        IAction modeSwitchAction = new Action("", IAction.AS_PUSH_BUTTON) {//$NON-NLS-1$
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            public void run() {
                groupedMode = !groupedMode;

                if (groupedMode) {
                    if (currentGroup == null)
                        currentGroup = getGroups()[0];
                    createToolBar(toolBarComposite);
                    selectAndRevealInToolBar(currentGroup);
                } else {
                    toolBar.dispose();
                    getTreeViewer().setInput(getPreferenceManager());
                }
                //when switching layouts reset the filter text to reveal all items in the tree
                filteredTree.resetText();
                filteredTree.getViewer().getControl().setFocus();
                getShell().setSize(getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
                getShell().layout(true);

            }
        };

        modeSwitchAction.setToolTipText(WorkbenchMessages.getString("WorkbenchPreferenceDialog.Switch")); //$NON-NLS-1$
        modeSwitchAction.setImageDescriptor(JFaceResources.getImageRegistry()
                .getDescriptor(MODE_ICON));

        historyManager.add(modeSwitchAction);

    }

    /**
     * Set the content and label providers for the treeViewer
     * 
     * @param treeViewer
     */
    private void setContentAndLabelProviders(TreeViewer treeViewer) {
        treeViewer.setContentProvider(new GroupedPreferenceContentProvider(
                showingGroups()));
        treeViewer.setLabelProvider(new GroupedPreferenceLabelProvider());
    }

    /*
     * @see org.eclipse.jface.preference.PreferenceDialog#showPage(org.eclipse.jface.preference.IPreferenceNode)
     * @since 3.1
     */
    protected boolean showPage(IPreferenceNode node) {
        final boolean success = super.showPage(node);
        if (success) {
            history.addHistoryEntry(new PreferenceHistoryEntry(node.getId(),
                    node.getLabelText(), null));
        }
        return success;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#updateTitle()
     */
    public void updateTitle() {
        tab.setText(getCurrentPage().getTitle());
        tab.setImage(getCurrentPage().getImage());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#updateMessage()
     */
    public void updateMessage() {
        // No longer required as the pages do this.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#createTreeViewer(org.eclipse.swt.widgets.Composite)
     */
    protected TreeViewer createTreeViewer(Composite parent) {
        PatternFilter filter = new PatternFilter() {
            public boolean select(Viewer viewer, Object parentElement,
                    Object element) {
                ITreeContentProvider contentProvider = (ITreeContentProvider) getTreeViewer()
                        .getContentProvider();
                boolean match = false;
                if (element instanceof WorkbenchPreferenceGroup) {

                    WorkbenchPreferenceGroup group = (WorkbenchPreferenceGroup) element;
                    Object[] children = contentProvider.getChildren(group);
                    match = match(group.getName())
 (filter(viewer, element, children).length > 0);
                }
                if (element instanceof WorkbenchPreferenceNode) {
                    WorkbenchPreferenceNode node = (WorkbenchPreferenceNode) element;
                    Object[] children = contentProvider.getChildren(node);
                    match = match(node.getLabelText())
 (filter(viewer, element, children).length > 0);
                }

                return match;
            }
        };
        int styleBits = SWT.SINGLE | SWT.H_SCROLL;
        filteredTree = new FilteredTree(parent, styleBits, filter);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalIndent = IDialogConstants.HORIZONTAL_MARGIN;
        filteredTree.setBackground(parent.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));
                
        TreeViewer tree = filteredTree.getViewer();
        filteredTree.setInitialText(WorkbenchMessages
                .getString("WorkbenchPreferenceDialog.FilterMessage")); //$NON-NLS-1$

        setContentAndLabelProviders(tree);

        tree.addSelectionChangedListener(new ISelectionChangedListener() {
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            public void selectionChanged(SelectionChangedEvent event) {
                if (!showingGroups())
                    return;

                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event
                            .getSelection();
                    if (selection.isEmpty())
                        return;
                    Object item = selection.getFirstElement();
                    currentGroup.setLastSelection(item);
                }
            }
        });

        super.addListeners(filteredTree.getViewer());
        return filteredTree.getViewer();
    }

    /**
     * Return whether groups are being shown
     * 
     * @return boolean
     */
    public static boolean showingGroups() {
        return groupedMode;
    }

    /**
     * Return whether or not groups can be shown
     * 
     * @return boolean
     */
    public static boolean canShowGroups() {
        return getGroups().length > 0;
    }

    /**
     * Return the groups in the receiver.
     * 
     * @return WorkbenchPreferenceGroup[]
     */
    protected static WorkbenchPreferenceGroup[] getGroups() {
        return ((WorkbenchPreferenceManager) WorkbenchPlugin.getDefault()
                .getPreferenceManager()).getGroups();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#createTreeAreaContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createTreeAreaContents(Composite parent) {
        Composite leftArea = new Composite(parent, SWT.NONE);
        leftArea.setBackground(parent.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));

        GridLayout leftLayout = new GridLayout();
        leftLayout.numColumns = 1;
        //leftLayout.marginWidth = 0;
        leftLayout.marginHeight = 0;
        leftLayout.horizontalSpacing = 0;
        leftLayout.verticalSpacing = 0;
        leftArea.setLayout(leftLayout);

        // Build the tree an put it into the composite.
        TreeViewer viewer = createTreeViewer(leftArea);
        setTreeViewer(viewer);

        updateTreeFont(JFaceResources.getDialogFont());
        GridData viewerData = new GridData(GridData.FILL_BOTH
 GridData.GRAB_VERTICAL);
        viewer.getControl().getParent().setLayoutData(viewerData);

        layoutTreeAreaControl(leftArea);

        return leftArea;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#createSash(org.eclipse.swt.widgets.Composite,
     *      org.eclipse.swt.widgets.Control)
     */
    protected Sash createSash(Composite composite, Control rightControl) {
        Sash sash = super.createSash(composite, rightControl);
        sash.setBackground(composite.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));
        return sash;
    }

    /**
     * Set the search results of the receiver to be filteredIds.
     * 
     * @param filteredIds
     */
    public void setSearchResults(String[] filteredIds) {

        WorkbenchPreferenceGroup[] groups = getGroups();
        for (int i = 0; i < groups.length; i++) {
            WorkbenchPreferenceGroup group = groups[i];
            group.highlightIds(filteredIds);
        }
    }

    /**
     * Highlight all nodes that match text;
     * 
     * @param text
     */
    protected void highlightHits(String text) {
        WorkbenchPreferenceGroup group = (WorkbenchPreferenceGroup) getTreeViewer()
                .getInput();

        group.highlightHits(text);
        getTreeViewer().refresh();
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

    /**
     * Selects the current page based on the given preference page identifier.
     * If no node can be found, then nothing will change.
     * 
     * @param preferencePageId
     *            The preference page identifier to select; should not be
     *            <code>null</code>.
     */
    public final void setCurrentPageId(final String preferencePageId) {
        final IPreferenceNode node = findNodeMatching(preferencePageId);
        if (node != null) {
            getTreeViewer().setSelection(new StructuredSelection(node));
            showPage(node);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferenceDialog#createPageControl(org.eclipse.jface.preference.IPreferencePage,
     *      org.eclipse.swt.widgets.Composite)
     */
    protected void createPageControl(IPreferencePage page, Composite parent) {
        if (page instanceof PreferencePage)
            ((PreferencePage) page).createControl(parent);
        else
            super.createPageControl(page, parent);
    }

	/**
	 * Set the data to be applied to a page after it is created.
	 * @param pageData Object
	 */
	public void setPageData(Object pageData) {
		this.pageData = pageData;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferenceDialog#createPage(org.eclipse.jface.preference.IPreferenceNode)
	 */
	protected void createPage(IPreferenceNode node) {
		super.createPage(node);
		if(this.pageData == null)
			return;
		//Apply the data if it has been set.
		IPreferencePage page = node.getPage();
		if(page instanceof PreferencePage)
			((PreferencePage) page).applyData(this.pageData);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3340.java