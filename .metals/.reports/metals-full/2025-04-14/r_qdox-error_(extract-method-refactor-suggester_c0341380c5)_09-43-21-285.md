error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8571.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8571.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8571.java
text:
```scala
n@@ew Label(composite, SWT.NONE);

package org.eclipse.ui.internal.dialogs;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jface.preference.*;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.*;
/**
 * The ViewsPreferencePage is the page used to set preferences for the look of the
 * views in the workbench.
 */
public class ViewsPreferencePage
	extends PreferencePage
	implements IWorkbenchPreferencePage {
	private Button editorTopButton;
	private Button editorBottomButton;
	private Button viewTopButton;
	private Button viewBottomButton;

	private ColorFieldEditor errorColorEditor;
	private ColorFieldEditor hyperlinkColorEditor;
	private ColorFieldEditor activeHyperlinkColorEditor;
	/*
	 * No longer supported - removed when confirmed!
	 * private Button openFloatButton;
	 */
	private int editorAlignment;
	private int viewAlignment;

	private static final String EDITORS_TITLE = WorkbenchMessages.getString("ViewsPreference.editors"); //$NON-NLS-1$
	private static final String EDITORS_TOP_TITLE = WorkbenchMessages.getString("ViewsPreference.editors.top"); //$NON-NLS-1$
	private static final String EDITORS_BOTTOM_TITLE = WorkbenchMessages.getString("ViewsPreference.editors.bottom"); //$NON-NLS-1$
	private static final String VIEWS_TITLE = WorkbenchMessages.getString("ViewsPreference.views"); //$NON-NLS-1$
	private static final String VIEWS_TOP_TITLE = WorkbenchMessages.getString("ViewsPreference.views.top"); //$NON-NLS-1$
	private static final String VIEWS_BOTTOM_TITLE = WorkbenchMessages.getString("ViewsPreference.views.bottom"); //$NON-NLS-1$
	/*
	 * No longer supported - remove when confirmed!
	 * private static final String OVM_FLOAT = WorkbenchMessages.getString("OpenViewMode.float"); //$NON-NLS-1$
	 */
	private static final String NOTE_LABEL = WorkbenchMessages.getString("Preference.note"); //$NON-NLS-1$
	private static final String APPLY_MESSAGE = WorkbenchMessages.getString("ViewsPreference.applyMessage"); //$NON-NLS-1$
/**
 * Create a composite that for creating the tab toggle buttons.
 * @param composite Composite
 * @param title String
 */
private Group createButtonGroup(Composite composite, String title) {

	Group buttonComposite = new Group(composite, SWT.LEFT);
	buttonComposite.setText(title);
	buttonComposite.setFont(composite.getFont());
	GridLayout layout = new GridLayout();
	buttonComposite.setLayout(layout);
	GridData data =
		new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
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

	WorkbenchHelp.setHelp(parent, IHelpContextIds.VIEWS_PREFERENCE_PAGE);

	IPreferenceStore store = WorkbenchPlugin.getDefault().getPreferenceStore();
	editorAlignment = store.getInt(IPreferenceConstants.EDITOR_TAB_POSITION);
	viewAlignment =	store.getInt(IPreferenceConstants.VIEW_TAB_POSITION);

	Composite composite = new Composite(parent, SWT.NONE);
	composite.setLayoutData(
		new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
	composite.setFont(font);

	GridLayout layout = new GridLayout();
	layout.marginWidth = 0;
	layout.marginHeight = 0;
	layout.verticalSpacing = 10;
	
	composite.setLayout(layout);

	createEditorTabButtonGroup(composite);
	createViewTabButtonGroup(composite);

	Composite messageComposite = new Composite(composite, SWT.NONE);
	GridLayout messageLayout = new GridLayout();
	messageLayout.numColumns = 2;
	messageLayout.marginWidth = 0;
	messageLayout.marginHeight = 0;
	messageComposite.setLayout(messageLayout);
	messageComposite.setLayoutData(
		new GridData(GridData.HORIZONTAL_ALIGN_FILL));
	messageComposite.setFont(font);

	final Label noteLabel = new Label(messageComposite,SWT.BOLD);
	noteLabel.setText(NOTE_LABEL);
	noteLabel.setFont(JFaceResources.getBannerFont());
	noteLabel.setLayoutData(
		new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
	final IPropertyChangeListener fontListener = new IPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent event) {
			if(JFaceResources.BANNER_FONT.equals(event.getProperty())) {
				noteLabel.setFont(JFaceResources.getFont(JFaceResources.BANNER_FONT));
			}
		}
	};
	JFaceResources.getFontRegistry().addListener(fontListener);
	noteLabel.addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent event) {
			JFaceResources.getFontRegistry().removeListener(fontListener);
		}
	});
	
	Label messageLabel = new Label(messageComposite,SWT.NONE);
	messageLabel.setText(APPLY_MESSAGE);
	messageLabel.setFont(font);

	Label spacer = new Label(composite, SWT.NONE);
	
	Group colorComposite = new Group(composite,SWT.NONE);
	colorComposite.setLayout(new GridLayout());
	colorComposite.setText(WorkbenchMessages.getString("ViewsPreference.ColorsTitle")); //$NON-NLS-1$
	colorComposite.setFont(font);
				
	GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
	data.horizontalSpan = 2;
	colorComposite.setLayoutData(data);	
	
	//Add in an intermediate composite to allow for spacing
	Composite spacingComposite = new Composite(colorComposite,SWT.NONE);
	spacingComposite.setLayout(new GridLayout());
	spacingComposite.setFont(font);
	
	errorColorEditor = new ColorFieldEditor(
			JFacePreferences.ERROR_COLOR,
			WorkbenchMessages.getString("ViewsPreference.ErrorText"), //$NON-NLS-1$,
			spacingComposite);
			
	errorColorEditor.setPreferenceStore(doGetPreferenceStore());
	errorColorEditor.load();
	
	hyperlinkColorEditor = new ColorFieldEditor(
			JFacePreferences.HYPERLINK_COLOR,
			WorkbenchMessages.getString("ViewsPreference.HyperlinkText"), //$NON-NLS-1$
			spacingComposite);
			
	hyperlinkColorEditor.setPreferenceStore(doGetPreferenceStore());
	hyperlinkColorEditor.load();
	
	activeHyperlinkColorEditor = new ColorFieldEditor(
			JFacePreferences.ACTIVE_HYPERLINK_COLOR,
			WorkbenchMessages.getString("ViewsPreference.ActiveHyperlinkText"), //$NON-NLS-1$
			spacingComposite);
			
	activeHyperlinkColorEditor.setPreferenceStore(doGetPreferenceStore());
	activeHyperlinkColorEditor.load();
	
	return composite;
}
/**
 * Create a composite that contains buttons for selecting tab position for the edit selection. 
 * @param composite Composite
 * @param store IPreferenceStore
 */
private void createEditorTabButtonGroup(Composite composite) {
	
	Font font = composite.getFont();
	
	Group buttonComposite = createButtonGroup(composite,EDITORS_TITLE);

	this.editorTopButton = new Button(buttonComposite, SWT.RADIO);
	this.editorTopButton.setText(EDITORS_TOP_TITLE);
	this.editorTopButton.setSelection(this.editorAlignment == SWT.TOP);
	this.editorTopButton.setFont(font);

	this.editorTopButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			editorAlignment = SWT.TOP;
		}
	});
	
	this.editorTopButton.getAccessible().addAccessibleListener(new AccessibleAdapter() {
		public void getName(AccessibleEvent e) {
			e.result = EDITORS_TITLE;
		}
	});

	this.editorBottomButton = new Button(buttonComposite, SWT.RADIO);
	this.editorBottomButton.setText(EDITORS_BOTTOM_TITLE);
	this.editorBottomButton.setSelection(this.editorAlignment == SWT.BOTTOM);
	this.editorBottomButton.setFont(font);

	this.editorBottomButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			editorAlignment = SWT.BOTTOM;
		}
	});

}

/**
 * Create a composite that contains buttons for selecting tab position for the view selection. 
 * @param composite Composite
 * @param store IPreferenceStore
 */
private void createViewTabButtonGroup(Composite composite) {
	
	Font font = composite.getFont();

	Group buttonComposite = createButtonGroup(composite,VIEWS_TITLE);
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
public void init(org.eclipse.ui.IWorkbench workbench) {}
/**
 * The default button has been pressed. 
 */
protected void performDefaults() {
	IPreferenceStore store = WorkbenchPlugin.getDefault().getPreferenceStore();
	int editorTopValue =
		store.getDefaultInt(IPreferenceConstants.EDITOR_TAB_POSITION);
	editorTopButton.setSelection(editorTopValue == SWT.TOP);
	editorBottomButton.setSelection(editorTopValue == SWT.BOTTOM);
	editorAlignment = editorTopValue;

	int viewTopValue =
		store.getDefaultInt(IPreferenceConstants.VIEW_TAB_POSITION);
	viewTopButton.setSelection(viewTopValue == SWT.TOP);
	viewBottomButton.setSelection(viewTopValue == SWT.BOTTOM);
	viewAlignment = viewTopValue;
	
	errorColorEditor.loadDefault();
	hyperlinkColorEditor.loadDefault();
	activeHyperlinkColorEditor.loadDefault();
	
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

	// store the editor tab value to setting
	store.setValue(IPreferenceConstants.EDITOR_TAB_POSITION, editorAlignment);

	// store the view tab value to setting
	store.setValue(IPreferenceConstants.VIEW_TAB_POSITION, viewAlignment);
		
	errorColorEditor.store();
	hyperlinkColorEditor.store();
	activeHyperlinkColorEditor.store();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8571.java