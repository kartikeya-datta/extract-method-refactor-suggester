error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8361.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8361.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8361.java
text:
```scala
p@@erspBarLocation = apiStore

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

import java.text.Collator;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.themes.IThemeDescriptor;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;

/**
 * The ViewsPreferencePage is the page used to set preferences for the look of the
 * views in the workbench.
 */
public class ViewsPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage {

    private Button showTextOnPerspectiveBar;

    /*
     * change the tab style of the workbench
     */
    private Button showTraditionalStyleTabs;

    private Button enableAnimations;
    
    private Button editorTopButton;

    private Button editorBottomButton;

    private Button viewTopButton;

    private Button viewBottomButton;

    private Button perspLeftButton;

    private Button perspTopLeftButton;

    private Button perspTopRightButton;

    /*
     * No longer supported - removed when confirmed!
     * private Button openFloatButton;
     */
    int editorAlignment;

    int viewAlignment;

    String perspBarLocation;

    static final String EDITORS_TITLE = WorkbenchMessages
            .getString("ViewsPreference.editors"); //$NON-NLS-1$

    private static final String EDITORS_TOP_TITLE = WorkbenchMessages
            .getString("ViewsPreference.editors.top"); //$NON-NLS-1$

    private static final String EDITORS_BOTTOM_TITLE = WorkbenchMessages
            .getString("ViewsPreference.editors.bottom"); //$NON-NLS-1$

    private static final String VIEWS_TITLE = WorkbenchMessages
            .getString("ViewsPreference.views"); //$NON-NLS-1$

    private static final String VIEWS_TOP_TITLE = WorkbenchMessages
            .getString("ViewsPreference.views.top"); //$NON-NLS-1$

    private static final String VIEWS_BOTTOM_TITLE = WorkbenchMessages
            .getString("ViewsPreference.views.bottom"); //$NON-NLS-1$

    private static final String PERSP_TITLE = WorkbenchMessages
            .getString("ViewsPreference.perspectiveBar"); //$NON-NLS-1$

    private static final String PERSP_LEFT_TITLE = WorkbenchMessages
            .getString("ViewsPreference.perspectiveBar.left"); //$NON-NLS-1$

    private static final String PERSP_TOP_LEFT_TITLE = WorkbenchMessages
            .getString("ViewsPreference.perspectiveBar.topLeft"); //$NON-NLS-1$

    private static final String PERSP_TOP_RIGHT_TITLE = WorkbenchMessages
            .getString("ViewsPreference.perspectiveBar.topRight"); //$NON-NLS-1$

    // These constants aren't my favourite idea, but to get this preference done
    // for M9...  A better solution might be to have the presentation factory set
    // its dependant preference defaults on startup.  I've filed bug 63346 to do
    // something about this area.
    private static final String R21PRESENTATION_ID = "org.eclipse.ui.internal.r21presentationFactory"; //$NON-NLS-1$

    private static final String R30PRESENTATION_ID = "org.eclipse.ui.presentations.default"; //$NON-NLS-1$

    /*
     * No longer supported - remove when confirmed!
     * private static final String OVM_FLOAT = WorkbenchMessages.getString("OpenViewMode.float"); //$NON-NLS-1$
     */

    private Combo themeCombo;

    private Combo presentationCombo;

    private IConfigurationElement[] presentationFactories;

    private String currentPresentationFactoryId;

    /**
     * Create a composite that for creating the tab toggle buttons.
     * @param composite Composite
     * @param title String
     */
    private Group createButtonGroup(Composite composite, String title) {
        Group buttonComposite = new Group(composite, SWT.NONE);
        buttonComposite.setText(title);
        buttonComposite.setFont(composite.getFont());
        FormLayout layout = new FormLayout();
        layout.marginWidth = 2;
        layout.marginHeight = 2;
        buttonComposite.setLayout(layout);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
 GridData.GRAB_HORIZONTAL);
        buttonComposite.setLayoutData(data);

        return buttonComposite;

    }

    /**
     * Creates and returns the SWT control for the customized body 
     * of this preference page under the given parent composite.
     * <p>
     * This framework method must be implemented by concrete
     * subclasses.
     * </p>
     *
     * @param parent the parent composite
     * @return the new control
     */
    protected Control createContents(Composite parent) {

        Font font = parent.getFont();

        WorkbenchHelp.setHelp(parent, IWorkbenchHelpContextIds.VIEWS_PREFERENCE_PAGE);

        IPreferenceStore internalStore = PrefUtil.getInternalPreferenceStore();
        IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();

        editorAlignment = internalStore
                .getInt(IPreferenceConstants.EDITOR_TAB_POSITION);
        viewAlignment = internalStore
                .getInt(IPreferenceConstants.VIEW_TAB_POSITION);
        perspBarLocation = apiStore
                .getString(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR);

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setFont(font);

        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        //layout.verticalSpacing = 10;
        composite.setLayout(layout);

        createEditorTabButtonGroup(composite);
        createViewTabButtonGroup(composite);
        createPerspBarTabButtonGroup(composite);

        createPresentationCombo(composite);

        GridData data = new GridData(GridData.GRAB_HORIZONTAL
 GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;

        Label label = new Label(composite, SWT.NONE);
        label.setText(WorkbenchMessages
                .getString("ViewsPreference.currentTheme")); //$NON-NLS-1$
        label.setFont(parent.getFont());
        label.setLayoutData(data);

        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 2;

        themeCombo = new Combo(composite, SWT.READ_ONLY);
        themeCombo.setLayoutData(data);
        themeCombo.setFont(parent.getFont());
        refreshThemeCombo();

        createShowTextOnPerspectiveBarPref(composite);
        createShowTraditionalStyleTabsPref(composite);
        createEnableAnimationsPref(composite);

        return composite;
    }

    private void createPresentationCombo(Composite parent) {
        GridData data = new GridData(GridData.GRAB_HORIZONTAL
 GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;

        Label label = new Label(parent, SWT.NONE);
        label.setText(WorkbenchMessages
                .getString("ViewsPreference.currentPresentation")); //$NON-NLS-1$
        label.setFont(parent.getFont());
        label.setLayoutData(data);

        data = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;

        presentationCombo = new Combo(parent, SWT.READ_ONLY);
        presentationCombo.setFont(parent.getFont());
        presentationCombo.setLayoutData(data);

        refreshPresentationCombo();
    }

    /**
     * Set the two supplied controls to be beside each other.
     */

    private void attachControls(Control leftControl, Control rightControl) {

        FormData leftData = new FormData();
        leftData.left = new FormAttachment(0, 0);

        FormData rightData = new FormData();
        rightData.left = new FormAttachment(leftControl, 5);

        leftControl.setLayoutData(leftData);
        rightControl.setLayoutData(rightData);
    }

    /**
     * Create a composite that contains buttons for selecting tab position for the edit selection. 
     * @param composite Composite
     */
    private void createEditorTabButtonGroup(Composite composite) {

        Font font = composite.getFont();

        Group buttonComposite = createButtonGroup(composite, EDITORS_TITLE);

        this.editorTopButton = new Button(buttonComposite, SWT.RADIO);
        this.editorTopButton.setText(EDITORS_TOP_TITLE);
        this.editorTopButton.setSelection(this.editorAlignment == SWT.TOP);
        this.editorTopButton.setFont(font);

        this.editorTopButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                editorAlignment = SWT.TOP;
            }
        });

        this.editorTopButton.getAccessible().addAccessibleListener(
                new AccessibleAdapter() {
                    public void getName(AccessibleEvent e) {
                        e.result = EDITORS_TITLE;
                    }
                });

        this.editorBottomButton = new Button(buttonComposite, SWT.RADIO);
        this.editorBottomButton.setText(EDITORS_BOTTOM_TITLE);
        this.editorBottomButton
                .setSelection(this.editorAlignment == SWT.BOTTOM);
        this.editorBottomButton.setFont(font);

        this.editorBottomButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                editorAlignment = SWT.BOTTOM;
            }
        });

        attachControls(this.editorTopButton, this.editorBottomButton);

    }

    /**
     * Create a composite that contains buttons for selecting tab position for the view selection. 
     * @param composite Composite
     */
    private void createViewTabButtonGroup(Composite composite) {

        Font font = composite.getFont();

        Group buttonComposite = createButtonGroup(composite, VIEWS_TITLE);
        buttonComposite.setFont(font);

        this.viewTopButton = new Button(buttonComposite, SWT.RADIO);
        this.viewTopButton.setText(VIEWS_TOP_TITLE);
        this.viewTopButton.setSelection(this.viewAlignment == SWT.TOP);
        this.viewTopButton.setFont(font);

        this.viewTopButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                viewAlignment = SWT.TOP;
            }
        });

        this.viewBottomButton = new Button(buttonComposite, SWT.RADIO);
        this.viewBottomButton.setText(VIEWS_BOTTOM_TITLE);
        this.viewBottomButton.setSelection(this.viewAlignment == SWT.BOTTOM);
        this.viewBottomButton.setFont(font);

        this.viewBottomButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                viewAlignment = SWT.BOTTOM;
            }
        });

        attachControls(this.viewTopButton, this.viewBottomButton);
    }

    /**
     * Create a composite that contains buttons for selecting perspective switcher
     * position. 
     * @param composite Composite
     */
    private void createPerspBarTabButtonGroup(Composite composite) {
        Font font = composite.getFont();

        Group buttonComposite = createButtonGroup(composite, PERSP_TITLE);
        buttonComposite.setFont(font);

        perspLeftButton = new Button(buttonComposite, SWT.RADIO);
        perspLeftButton.setText(PERSP_LEFT_TITLE);
        perspLeftButton.setSelection(IWorkbenchPreferenceConstants.LEFT
                .equals(perspBarLocation));
        perspLeftButton.setFont(font);
        perspLeftButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                perspBarLocation = IWorkbenchPreferenceConstants.LEFT;
            }
        });

        perspTopLeftButton = new Button(buttonComposite, SWT.RADIO);
        perspTopLeftButton.setText(PERSP_TOP_LEFT_TITLE);
        perspTopLeftButton.setSelection(IWorkbenchPreferenceConstants.TOP_LEFT
                .equals(perspBarLocation));
        perspTopLeftButton.setFont(font);
        perspTopLeftButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                perspBarLocation = IWorkbenchPreferenceConstants.TOP_LEFT;
            }
        });

        perspTopRightButton = new Button(buttonComposite, SWT.RADIO);
        perspTopRightButton.setText(PERSP_TOP_RIGHT_TITLE);
        perspTopRightButton
                .setSelection(IWorkbenchPreferenceConstants.TOP_RIGHT
                        .equals(perspBarLocation));
        perspTopRightButton.setFont(font);
        perspTopRightButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                perspBarLocation = IWorkbenchPreferenceConstants.TOP_RIGHT;
            }
        });

        FormData leftData = new FormData();
        leftData.left = new FormAttachment(0, 5);

        FormData topLeftData = new FormData();
        topLeftData.left = new FormAttachment(perspLeftButton, 5);

        FormData topRightData = new FormData();
        topRightData.left = new FormAttachment(perspTopLeftButton, 0);

        perspLeftButton.setLayoutData(leftData);
        perspTopLeftButton.setLayoutData(topLeftData);
        perspTopRightButton.setLayoutData(topRightData);
    }

    private void refreshPresentationCombo() {

        // get the active presentation
        presentationCombo.removeAll();
        refreshPresentationFactories();

        int selection = -1;
        for (int i = 0; i < presentationFactories.length; ++i) {
            IConfigurationElement el = presentationFactories[i];
            String name = el.getAttribute(IWorkbenchConstants.TAG_NAME);
            if (!currentPresentationFactoryId.equals(el
                    .getAttribute(IWorkbenchConstants.TAG_ID)))
                presentationCombo.add(name);
            else {
                selection = i;
                presentationCombo.add(WorkbenchMessages.format(
                        "ViewsPreference.currentPresentationFormat", //$NON-NLS-1$
                        new String[] { name }));
            }
        }

        if (selection != -1)
            presentationCombo.select(selection);
    }

    /**
     * Update this page's list of presentation factories.  This should only be used
     * when the presentation combo is refreshed, as this array will be used to set
     * the selection from the combo.
     * @return
     */
    private void refreshPresentationFactories() {
        // update the current selection (used to look for changes on apply)
        currentPresentationFactoryId = Workbench.getInstance()
                .getPresentationId();

        // update the sorted list of factories
        presentationFactories = Platform.getExtensionRegistry()
                .getConfigurationElementsFor(PlatformUI.PLUGIN_ID,
                        IWorkbenchConstants.PL_PRESENTATION_FACTORIES);

        // sort the array by name
        Arrays.sort(presentationFactories, new Comparator() {
            Collator collator = Collator.getInstance(Locale.getDefault());

            public int compare(Object a, Object b) {
                IConfigurationElement el1 = (IConfigurationElement) a;
                IConfigurationElement el2 = (IConfigurationElement) b;
                return collator.compare(el1
                        .getAttribute(IWorkbenchConstants.TAG_NAME), el2
                        .getAttribute(IWorkbenchConstants.TAG_NAME));
            }
        });
    }

    /**
     * Update the preferences associated with the argument presentation factory.
     * 
     * @param presFactoryId
     *            the id given in the presentation factory's xml
     */
    private void updatePresentationPreferences() {
        // There are some preference values associated with the R2.1 presentation that
        // cannot be captured in the presentation factory.  Perhaps the extension
        // point should contain these (a list of attributes?), but for now it is
        // done manually.

        if (presentationCombo == null) {
            // TODO log?
            return;
        }

        int selection = presentationCombo.getSelectionIndex();
        if (selection < 0 || selection >= presentationFactories.length) {
            // TODO log?
            return;
        }

        IConfigurationElement element = presentationFactories[selection];
        String id = element.getAttribute(IWorkbenchConstants.TAG_ID);

        // if it hasn't changed then there's nothing to do
        if (id.equals(currentPresentationFactoryId))
            return;

        // make sure they really want to do this
        int really = new MessageDialog(
                getShell(),
                WorkbenchMessages
                        .getString("ViewsPreference.presentationConfirm.title"), //$NON-NLS-1$
                null,
                WorkbenchMessages
                        .getString("ViewsPreference.presentationConfirm.message"), //$NON-NLS-1$
                MessageDialog.QUESTION,
                new String[] {
                        WorkbenchMessages
                                .getString("ViewsPreference.presentationConfirm.yes"), //$NON-NLS-1$
                        WorkbenchMessages
                                .getString("ViewsPreference.presentationConfirm.no") }, //$NON-NLS-1$
                1).open();
        if (really != 0)
            return;

        currentPresentationFactoryId = id;

        // apply 2.1 prefs if needed
        if (R21PRESENTATION_ID.equals(id))
            setR21Preferences();
        else if (R30PRESENTATION_ID.equals(id))
            setR30Preferences();

        // set the new presentation factory id
        PrefUtil.getAPIPreferenceStore().setValue(
                IWorkbenchPreferenceConstants.PRESENTATION_FACTORY_ID, id);
    }

    private void setR30Preferences() {
        IPreferenceStore internalStore = PrefUtil.getInternalPreferenceStore();
        IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();

        // reset the preferences changed by the 2.1 presentation
        internalStore.setToDefault(IPreferenceConstants.VIEW_TAB_POSITION);
        viewAlignment = internalStore
                .getInt(IPreferenceConstants.VIEW_TAB_POSITION);
        viewTopButton.setSelection(viewAlignment == SWT.TOP);
        viewBottomButton.setSelection(viewAlignment == SWT.BOTTOM);

        apiStore
                .setToDefault(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR);
        perspBarLocation = apiStore
                .getString(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR);
        perspLeftButton.setSelection(IWorkbenchPreferenceConstants.LEFT
                .equals(perspBarLocation));
        perspTopLeftButton.setSelection(IWorkbenchPreferenceConstants.TOP_LEFT
                .equals(perspBarLocation));
        perspTopRightButton
                .setSelection(IWorkbenchPreferenceConstants.TOP_RIGHT
                        .equals(perspBarLocation));

        apiStore
                .setToDefault(IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR);
        showTextOnPerspectiveBar
                .setSelection(apiStore
                        .getBoolean(IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR));

        apiStore
                .setToDefault(IWorkbenchPreferenceConstants.INITIAL_FAST_VIEW_BAR_LOCATION);
    }

    private void setR21Preferences() {
        // view tabs on the bottom
        viewAlignment = SWT.BOTTOM;
        viewTopButton.setSelection(false);
        viewBottomButton.setSelection(true);

        // perspective switcher on the left
        perspBarLocation = IWorkbenchPreferenceConstants.LEFT;
        perspLeftButton.setSelection(true);
        perspTopLeftButton.setSelection(false);
        perspTopRightButton.setSelection(false);

        // turn off text on persp bar
        showTextOnPerspectiveBar.setSelection(false);

        // fast view bar on the left (hidden pref, set it directly)
        PrefUtil.getAPIPreferenceStore().setValue(
                IWorkbenchPreferenceConstants.INITIAL_FAST_VIEW_BAR_LOCATION,
                IWorkbenchPreferenceConstants.LEFT);
    }

    /**
     * 
     */
    private void refreshThemeCombo() {
        themeCombo.removeAll();
        ITheme currentTheme = PlatformUI.getWorkbench().getThemeManager()
                .getCurrentTheme();

        IThemeDescriptor[] descs = WorkbenchPlugin.getDefault()
                .getThemeRegistry().getThemes();
        int selection = 0;
        String themeString = PlatformUI.getWorkbench().getThemeManager()
                .getTheme(IThemeManager.DEFAULT_THEME).getLabel();
        if (currentTheme.getId().equals(IThemeManager.DEFAULT_THEME)) {
            themeString = MessageFormat
                    .format(
                            WorkbenchMessages
                                    .getString("ViewsPreference.currentThemeFormat"), new Object[] { themeString }); //$NON-NLS-1$
        }
        themeCombo.add(themeString);

        for (int i = 0; i < descs.length; i++) {
            themeString = descs[i].getName();
            if (descs[i].getId().equals(currentTheme.getId())) {
                themeString = MessageFormat
                        .format(
                                WorkbenchMessages
                                        .getString("ViewsPreference.currentThemeFormat"), new Object[] { themeString }); //$NON-NLS-1$
                selection = i + 1;
            }
            themeCombo.add(themeString);
        }

        themeCombo.select(selection);
    }

    /**
     * Create the button and text that support setting the preference for showing
     * text labels on the perspective switching bar
     */
    protected void createShowTextOnPerspectiveBarPref(Composite composite) {
        IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();

        showTextOnPerspectiveBar = new Button(composite, SWT.CHECK);
        showTextOnPerspectiveBar.setText(WorkbenchMessages
                .getString("WorkbenchPreference.showTextOnPerspectiveBar")); //$NON-NLS-1$
        showTextOnPerspectiveBar.setFont(composite.getFont());
        showTextOnPerspectiveBar
                .setSelection(apiStore
                        .getBoolean(IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR));
        setButtonLayoutData(showTextOnPerspectiveBar);
    }

    /**
     * Create the button and text that support setting the preference for showing
     * text labels on the perspective switching bar
     */
    protected void createShowTraditionalStyleTabsPref(Composite composite) {
        IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();

        showTraditionalStyleTabs = new Button(composite, SWT.CHECK);
        showTraditionalStyleTabs.setText(WorkbenchMessages
                .getString("ViewsPreference.traditionalTabs")); //$NON-NLS-1$
        showTraditionalStyleTabs.setFont(composite.getFont());
        showTraditionalStyleTabs
                .setSelection(apiStore
                        .getBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
        setButtonLayoutData(showTraditionalStyleTabs);
    }
    
    protected void createEnableAnimationsPref(Composite composite) {
        IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();

        enableAnimations = new Button(composite, SWT.CHECK);
        enableAnimations.setText(WorkbenchMessages
                .getString("ViewsPreference.enableAnimations")); //$NON-NLS-1$
        enableAnimations.setFont(composite.getFont());
        enableAnimations
                .setSelection(apiStore
                        .getBoolean(IWorkbenchPreferenceConstants.ENABLE_ANIMATIONS));
        setButtonLayoutData(enableAnimations);
    }

    /**
     * Returns preference store that belongs to the our plugin.
     *
     * @return the preference store for this plugin
     */
    protected IPreferenceStore doGetPreferenceStore() {
        return WorkbenchPlugin.getDefault().getPreferenceStore();
    }

    /**
     * Initializes this preference page for the given workbench.
     * <p>
     * This method is called automatically as the preference page is being created
     * and initialized. Clients must not call this method.
     * </p>
     *
     * @param workbench the workbench
     */
    public void init(org.eclipse.ui.IWorkbench workbench) {
        //no-op
    }

    /**
     * The default button has been pressed. 
     */
    protected void performDefaults() {
        IPreferenceStore store = getPreferenceStore();
        IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();

        showTextOnPerspectiveBar
                .setSelection(apiStore
                        .getDefaultBoolean(IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR));
        showTraditionalStyleTabs
                .setSelection(apiStore
                        .getDefaultBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
        enableAnimations
        		.setSelection(apiStore
        		        .getDefaultBoolean(IWorkbenchPreferenceConstants.ENABLE_ANIMATIONS));
        
        
        int editorTopValue = store
                .getDefaultInt(IPreferenceConstants.EDITOR_TAB_POSITION);
        editorTopButton.setSelection(editorTopValue == SWT.TOP);
        editorBottomButton.setSelection(editorTopValue == SWT.BOTTOM);
        editorAlignment = editorTopValue;

        int viewTopValue = store
                .getDefaultInt(IPreferenceConstants.VIEW_TAB_POSITION);
        viewTopButton.setSelection(viewTopValue == SWT.TOP);
        viewBottomButton.setSelection(viewTopValue == SWT.BOTTOM);
        viewAlignment = viewTopValue;

        perspBarLocation = store
                .getDefaultString(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR);
        perspLeftButton.setSelection(IWorkbenchPreferenceConstants.LEFT
                .equals(perspBarLocation));
        perspTopLeftButton.setSelection(IWorkbenchPreferenceConstants.TOP_LEFT
                .equals(perspBarLocation));
        perspTopRightButton
                .setSelection(IWorkbenchPreferenceConstants.TOP_RIGHT
                        .equals(perspBarLocation));

        /*
         * No longer supported - remove when confirmed!
         * if (openFloatButton != null) 
         * 	openFloatButton.setSelection(value == IPreferenceConstants.OVM_FLOAT);
         */

        WorkbenchPlugin.getDefault().savePluginPreferences();
        super.performDefaults();
    }

    /**
     *	The user has pressed Ok.  Store/apply this page's values appropriately.
     */
    public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();
        IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();

        // apply the presentation selection first since it might change some of the
        // other values
        updatePresentationPreferences();

        apiStore.setValue(
                IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR,
                showTextOnPerspectiveBar.getSelection());
        apiStore.setValue(
                IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
                showTraditionalStyleTabs.getSelection());
        apiStore.setValue(
                IWorkbenchPreferenceConstants.ENABLE_ANIMATIONS,
                enableAnimations.getSelection());
        
        // store the editor tab value to setting
        store.setValue(IPreferenceConstants.EDITOR_TAB_POSITION,
                editorAlignment);

        // store the view tab value to setting
        store.setValue(IPreferenceConstants.VIEW_TAB_POSITION, viewAlignment);

        // store the persp bar value
        apiStore.setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
                perspBarLocation);

        int idx = themeCombo.getSelectionIndex();
        if (idx == 0) {
            Workbench.getInstance().getThemeManager().setCurrentTheme(
                    IThemeManager.DEFAULT_THEME);
        } else {
            Workbench.getInstance().getThemeManager()
                    .setCurrentTheme(
                            WorkbenchPlugin.getDefault().getThemeRegistry()
                                    .getThemes()[idx - 1].getId());
        }

        refreshThemeCombo();

        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8361.java