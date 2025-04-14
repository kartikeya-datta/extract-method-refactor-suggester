error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8049.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8049.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8049.java
text:
```scala
(@@(TabBehaviour)Tweaklets.get(TabBehaviour.KEY)).setPreferenceVisibility(editorReuseGroup, showMultipleEditorTabs);

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.dialogs;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.EditorHistory;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.tweaklets.TabBehaviour;
import org.eclipse.ui.internal.tweaklets.Tweaklets;
import org.eclipse.ui.internal.util.PrefUtil;

/**
 * The Editors preference page of the workbench.
 */
public class EditorsPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage {
    private static final int REUSE_INDENT = 10;

    protected Composite editorReuseGroup;

    private Button reuseEditors;

    protected Button showMultipleEditorTabs;

    protected Button useIPersistableEditor;

    private Composite editorReuseIndentGroup;

    private Composite editorReuseThresholdGroup;

    private IntegerFieldEditor reuseEditorsThreshold;

    private Group dirtyEditorReuseGroup;

    private Button openNewEditor;

    private Button promptToReuseEditor;

    private IntegerFieldEditor recentFilesEditor;

    private IPropertyChangeListener validityChangeListener = new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
            if (event.getProperty().equals(FieldEditor.IS_VALID)) {
				updateValidState();
			}
        }
    };

	private Button promptWhenStillOpenEditor;

	protected Control createContents(Composite parent) {
        Composite composite = createComposite(parent);

        createEditorHistoryGroup(composite);

        createSpace(composite);
        createShowMultipleEditorTabsPref(composite);
        createUseIPersistablePref(composite);
        createPromptWhenStillOpenPref(composite);
		createEditorReuseGroup(composite);
		((TabBehaviour)Tweaklets.get(TabBehaviour.class)).setPreferenceVisibility(editorReuseGroup, showMultipleEditorTabs);

        updateValidState();

        setHelpContext(parent);

        return composite;
    }

	protected void setHelpContext(Composite parent) {
		// @issue the IDE subclasses this, but should provide its own help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				IWorkbenchHelpContextIds.WORKBENCH_EDITOR_PREFERENCE_PAGE);
	}

    protected void createSpace(Composite parent) {
        WorkbenchPreferencePage.createSpace(parent);
    }

    protected void createShowMultipleEditorTabsPref(Composite composite) {
        showMultipleEditorTabs = new Button(composite, SWT.CHECK);
        showMultipleEditorTabs.setText(WorkbenchMessages.WorkbenchPreference_showMultipleEditorTabsButton);
        showMultipleEditorTabs.setFont(composite.getFont());
        showMultipleEditorTabs.setSelection(getPreferenceStore().getBoolean(
                IPreferenceConstants.SHOW_MULTIPLE_EDITOR_TABS));
        setButtonLayoutData(showMultipleEditorTabs);
    }

    protected void createUseIPersistablePref(Composite composite) {
        useIPersistableEditor = new Button(composite, SWT.CHECK);
        useIPersistableEditor.setText(WorkbenchMessages.WorkbenchPreference_useIPersistableEditorButton);
        useIPersistableEditor.setFont(composite.getFont());
        useIPersistableEditor.setSelection(getPreferenceStore().getBoolean(
                IPreferenceConstants.USE_IPERSISTABLE_EDITORS));
        setButtonLayoutData(useIPersistableEditor);
    }
    
    protected void createPromptWhenStillOpenPref(Composite composite) {
    	promptWhenStillOpenEditor = new Button(composite, SWT.CHECK);
    	promptWhenStillOpenEditor.setText(WorkbenchMessages.WorkbenchPreference_promptWhenStillOpenButton);
    	promptWhenStillOpenEditor.setFont(composite.getFont());
    	promptWhenStillOpenEditor.setSelection(getAPIPreferenceStore().getBoolean(
    			IWorkbenchPreferenceConstants.PROMPT_WHEN_SAVEABLE_STILL_OPEN));
    	setButtonLayoutData(promptWhenStillOpenEditor);
    }
    
    protected Composite createComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
 GridData.HORIZONTAL_ALIGN_FILL));
        composite.setFont(parent.getFont());
        return composite;
    }

    public void init(IWorkbench workbench) {
        // do nothing
    }

    protected void performDefaults() {
        IPreferenceStore store = getPreferenceStore();
        showMultipleEditorTabs
				.setSelection(store
						.getDefaultBoolean(IPreferenceConstants.SHOW_MULTIPLE_EDITOR_TABS));
		useIPersistableEditor
				.setSelection(store
						.getDefaultBoolean(IPreferenceConstants.USE_IPERSISTABLE_EDITORS));
		promptWhenStillOpenEditor
				.setSelection(getAPIPreferenceStore()
						.getDefaultBoolean(IWorkbenchPreferenceConstants.PROMPT_WHEN_SAVEABLE_STILL_OPEN));
        reuseEditors.setSelection(store
                .getDefaultBoolean(IPreferenceConstants.REUSE_EDITORS_BOOLEAN));
        dirtyEditorReuseGroup.setEnabled(reuseEditors.getSelection());
        openNewEditor.setSelection(!store
                .getDefaultBoolean(IPreferenceConstants.REUSE_DIRTY_EDITORS));
        openNewEditor.setEnabled(reuseEditors.getSelection());
        promptToReuseEditor.setSelection(store
                .getDefaultBoolean(IPreferenceConstants.REUSE_DIRTY_EDITORS));
        promptToReuseEditor.setEnabled(reuseEditors.getSelection());
        reuseEditorsThreshold.loadDefault();
        reuseEditorsThreshold.getLabelControl(editorReuseThresholdGroup)
                .setEnabled(reuseEditors.getSelection());
        reuseEditorsThreshold.getTextControl(editorReuseThresholdGroup)
                .setEnabled(reuseEditors.getSelection());
        recentFilesEditor.loadDefault();
    }

    public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();
        store.setValue(IPreferenceConstants.SHOW_MULTIPLE_EDITOR_TABS,
                showMultipleEditorTabs.getSelection());
        store.setValue(IPreferenceConstants.USE_IPERSISTABLE_EDITORS,
                useIPersistableEditor.getSelection());
        getAPIPreferenceStore().setValue(IWorkbenchPreferenceConstants.PROMPT_WHEN_SAVEABLE_STILL_OPEN,
        		promptWhenStillOpenEditor.getSelection());
        
        // store the reuse editors setting
        store.setValue(IPreferenceConstants.REUSE_EDITORS_BOOLEAN, reuseEditors
                .getSelection());
        store.setValue(IPreferenceConstants.REUSE_DIRTY_EDITORS,
                promptToReuseEditor.getSelection());
        reuseEditorsThreshold.store();

        // store the recent files setting
        recentFilesEditor.store();
        
        PrefUtil.savePrefs();
        return super.performOk();
    }

    /**
     * Returns preference store that belongs to the our plugin.
     *
     * @return the preference store for this plugin
     */
    protected IPreferenceStore doGetPreferenceStore() {
        return WorkbenchPlugin.getDefault().getPreferenceStore();
    }

    protected IPreferenceStore getAPIPreferenceStore() {
    	return PrefUtil.getAPIPreferenceStore();
    }
    
    protected void updateValidState() {
        if (!recentFilesEditor.isValid()) {
            setErrorMessage(recentFilesEditor.getErrorMessage());
            setValid(false);
        } else if (!reuseEditorsThreshold.isValid()) {
            setErrorMessage(reuseEditorsThreshold.getErrorMessage());
            setValid(false);
        } else {
            setErrorMessage(null);
            setValid(true);
        }
    }

    /**
     * Create a composite that contains entry fields specifying editor reuse preferences.
     */
    protected void createEditorReuseGroup(Composite composite) {

        Font font = composite.getFont();

        editorReuseGroup = new Composite(composite, SWT.LEFT);
        GridLayout layout = new GridLayout();
        // Line up with other entries in preference page
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        editorReuseGroup.setLayout(layout);
        editorReuseGroup.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        editorReuseGroup.setFont(font);

        reuseEditors = new Button(editorReuseGroup, SWT.CHECK);
        reuseEditors.setText(WorkbenchMessages.WorkbenchPreference_reuseEditors);
        reuseEditors.setLayoutData(new GridData());
        reuseEditors.setFont(font);

        IPreferenceStore store = WorkbenchPlugin.getDefault()
                .getPreferenceStore();
        reuseEditors.setSelection(store
                .getBoolean(IPreferenceConstants.REUSE_EDITORS_BOOLEAN));
        reuseEditors.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                reuseEditorsThreshold
                        .getLabelControl(editorReuseThresholdGroup).setEnabled(
                                reuseEditors.getSelection());
                reuseEditorsThreshold.getTextControl(editorReuseThresholdGroup)
                        .setEnabled(reuseEditors.getSelection());
                dirtyEditorReuseGroup.setEnabled(reuseEditors.getSelection());
                openNewEditor.setEnabled(reuseEditors.getSelection());
                promptToReuseEditor.setEnabled(reuseEditors.getSelection());
            }
        });

        editorReuseIndentGroup = new Composite(editorReuseGroup, SWT.LEFT);
        GridLayout indentLayout = new GridLayout();
        indentLayout.marginWidth = REUSE_INDENT;
        editorReuseIndentGroup.setLayout(indentLayout);
        editorReuseIndentGroup.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

        editorReuseThresholdGroup = new Composite(editorReuseIndentGroup,
                SWT.LEFT);
        editorReuseThresholdGroup.setLayout(new GridLayout());
        editorReuseThresholdGroup.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        editorReuseThresholdGroup.setFont(font);

        reuseEditorsThreshold = new IntegerFieldEditor(
                IPreferenceConstants.REUSE_EDITORS,
                WorkbenchMessages.WorkbenchPreference_reuseEditorsThreshold, editorReuseThresholdGroup); 

        reuseEditorsThreshold.setPreferenceStore(WorkbenchPlugin.getDefault()
                .getPreferenceStore());
        reuseEditorsThreshold.setPage(this);
        reuseEditorsThreshold.setTextLimit(2);
        reuseEditorsThreshold.setErrorMessage(WorkbenchMessages.WorkbenchPreference_reuseEditorsThresholdError); 
        reuseEditorsThreshold
                .setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
        reuseEditorsThreshold.setValidRange(1, 99);
        reuseEditorsThreshold.load();
        reuseEditorsThreshold.getLabelControl(editorReuseThresholdGroup)
                .setEnabled(reuseEditors.getSelection());
        reuseEditorsThreshold.getTextControl(editorReuseThresholdGroup)
                .setEnabled(reuseEditors.getSelection());
        reuseEditorsThreshold.setPropertyChangeListener(validityChangeListener);

        dirtyEditorReuseGroup = new Group(editorReuseIndentGroup, SWT.NONE);
        dirtyEditorReuseGroup.setLayout(new GridLayout());
        dirtyEditorReuseGroup.setLayoutData(new GridData(
                GridData.FILL_HORIZONTAL));
        dirtyEditorReuseGroup.setText(WorkbenchMessages.WorkbenchPreference_reuseDirtyEditorGroupTitle); 
        dirtyEditorReuseGroup.setFont(font);
        dirtyEditorReuseGroup.setEnabled(reuseEditors.getSelection());

        promptToReuseEditor = new Button(dirtyEditorReuseGroup, SWT.RADIO);
        promptToReuseEditor.setText(WorkbenchMessages.WorkbenchPreference_promptToReuseEditor); 
        promptToReuseEditor.setFont(font);
        promptToReuseEditor.setSelection(store
                .getBoolean(IPreferenceConstants.REUSE_DIRTY_EDITORS));
        promptToReuseEditor.setEnabled(reuseEditors.getSelection());

        openNewEditor = new Button(dirtyEditorReuseGroup, SWT.RADIO);
        openNewEditor.setText(WorkbenchMessages.WorkbenchPreference_openNewEditor); 
        openNewEditor.setFont(font);
        openNewEditor.setSelection(!store
                .getBoolean(IPreferenceConstants.REUSE_DIRTY_EDITORS));
        openNewEditor.setEnabled(reuseEditors.getSelection());

    }

    /**
     * Create a composite that contains entry fields specifying editor history preferences.
     */
    protected void createEditorHistoryGroup(Composite composite) {
        Composite groupComposite = new Composite(composite, SWT.LEFT);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        groupComposite.setLayout(layout);
        GridData gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        groupComposite.setLayoutData(gd);
        groupComposite.setFont(composite.getFont());

        recentFilesEditor = new IntegerFieldEditor(
                IPreferenceConstants.RECENT_FILES,
                WorkbenchMessages.WorkbenchPreference_recentFiles, groupComposite); 

        recentFilesEditor.setPreferenceStore(WorkbenchPlugin.getDefault()
                .getPreferenceStore());
        recentFilesEditor.setPage(this);
        recentFilesEditor.setTextLimit(Integer.toString(EditorHistory.MAX_SIZE)
                .length());
        recentFilesEditor
                .setErrorMessage(NLS.bind(WorkbenchMessages.WorkbenchPreference_recentFilesError, new Integer(EditorHistory.MAX_SIZE) )); 
        recentFilesEditor
                .setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
        recentFilesEditor.setValidRange(0, EditorHistory.MAX_SIZE);
        recentFilesEditor.load();
        recentFilesEditor.setPropertyChangeListener(validityChangeListener);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8049.java