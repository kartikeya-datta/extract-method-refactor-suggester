error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3842.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3842.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3842.java
text:
```scala
private S@@tring DEFAULT_TOKEN = "DEFAULT"; //$NON-NLS-1$

package org.eclipse.ui.internal.dialogs;

/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import java.text.Collator;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.IHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.fonts.FontDefinition;
import org.eclipse.ui.internal.misc.Sorter;

public class FontPreferencePage
	extends PreferencePage
	implements IWorkbenchPreferencePage {

	private Hashtable labelsToDefinitions;
	private Hashtable fontDataSettings;
	private List fontList;
	private Button changeFontButton;
	private Button useSystemButton;
	private Text descriptionText;
	private Font appliedDialogFont;

	private ArrayList dialogFontWidgets = new ArrayList();

	//A token to identify a reset font
	private String DEFAULT_TOKEN = "DEFAULT";

	/**
	 * The label that displays the selected font, or <code>null</code> if none.
	 */
	private Label valueControl;

	/**
	 * The previewer, or <code>null</code> if none.
	 */
	private DefaultPreviewer previewer;

	private static class DefaultPreviewer {
		private Text text;
		private Font font;
		public DefaultPreviewer(Composite parent) {
			text = new Text(parent, SWT.READ_ONLY | SWT.BORDER | SWT.WRAP);
			text.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					if (font != null)
						font.dispose();
				}
			});
		}

		public Control getControl() {
			return text;
		}

		public void setFont(FontData[] fontData) {
			if (font != null)
				font.dispose();

			FontData[] bestData =
				JFaceResources.getFontRegistry().bestDataArray(
					fontData,
					text.getDisplay());

			//If there are no specified values then return.
			if (bestData == null)
				return;

			font = new Font(text.getDisplay(), bestData);
			text.setFont(font);
			//Also set the text here
			text.setText(WorkbenchMessages.getString("FontsPreference.SampleText")); //$NON-NLS-1$
		}
		public int getPreferredHeight() {
			return 120;
		}
	}

	/**
	 * Apply the dialog font to the control and store 
	 * it for later so that it can be used for a later
	 * update.
	 * @param control
	 */
	private void applyDialogFont(Control control) {
		control.setFont(JFaceResources.getDialogFont());
		dialogFontWidgets.add(control);
	}

	/**
	 * Update for a change in the dialog font.
	 * @param newFont
	 */
	private void updateForDialogFontChange(Font newFont) {
		Iterator iterator = dialogFontWidgets.iterator();
		while (iterator.hasNext()) {
			((Control) iterator.next()).setFont(newFont);
		}
	}

	/*
	 * @see PreferencePage#createContents
	 */
	public Control createContents(Composite parent) {
		WorkbenchHelp.setHelp(
			getControl(),
			IHelpContextIds.FONT_PREFERENCE_PAGE);

		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (appliedDialogFont != null)
					appliedDialogFont.dispose();
			}
		});

		Font defaultFont = parent.getFont();

		Composite mainColumn = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.makeColumnsEqualWidth = true;
		mainColumn.setFont(defaultFont);
		mainColumn.setLayout(layout);

		createFontList(mainColumn);

		Composite previewColumn = new Composite(mainColumn, SWT.NULL);
		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		previewColumn.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		previewColumn.setLayoutData(data);
		previewColumn.setFont(defaultFont);

		createPreviewControl(previewColumn);
		createValueControl(previewColumn);

		Composite buttonColumn = new Composite(previewColumn, SWT.NULL);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttonColumn.setLayout(layout);
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		buttonColumn.setLayoutData(data);
		buttonColumn.setFont(defaultFont);

		createUseDefaultsControl(buttonColumn, WorkbenchMessages.getString("FontsPreference.useSystemFont")); //$NON-NLS-1$
		createChangeControl(buttonColumn, JFaceResources.getString("openChange")); //$NON-NLS-1$

		createDescriptionControl(parent);

		return mainColumn;
	}

	/**
	 * Create the preference page.
	 */
	public FontPreferencePage() {
		setPreferenceStore(WorkbenchPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * Create the list of possible fonts.
	 */
	private void createFontList(Composite firstColumn) {

		Composite parent = new Composite(firstColumn, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		parent.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		parent.setLayoutData(data);

		Label label = new Label(parent, SWT.LEFT);
		label.setText(WorkbenchMessages.getString("FontsPreference.fonts")); //$NON-NLS-1$
		applyDialogFont(label);

		fontList = new List(parent, SWT.BORDER);
		data =
			new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		fontList.setLayoutData(data);
		applyDialogFont(fontList);

		fontList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FontDefinition selectedFontDefinition =
					getSelectedFontDefinition();
				if (selectedFontDefinition == null) {
					changeFontButton.setEnabled(false);
					useSystemButton.setEnabled(false);
				} else {
					changeFontButton.setEnabled(true);
					useSystemButton.setEnabled(true);
					updateForSelectedFontDefinition(selectedFontDefinition);
				}
			}
		});

		Set names = labelsToDefinitions.keySet();
		int nameSize = names.size();
		String[] unsortedItems = new String[nameSize];
		names.toArray(unsortedItems);

		Sorter sorter = new Sorter() {
			private Collator collator = Collator.getInstance();

			public boolean compare(Object o1, Object o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				return collator.compare(s1, s2) < 0;
			}
		};

		Object[] sortedItems = sorter.sort(unsortedItems);
		String[] listItems = new String[nameSize];
		System.arraycopy(sortedItems, 0, listItems, 0, nameSize);

		fontList.setItems(listItems);
	}

	/**
	 * Return the id of the currently selected font. Return
	 * null if multiple or none are selected.
	 */

	private FontDefinition getSelectedFontDefinition() {
		String[] selection = fontList.getSelection();
		if (selection.length == 1)
			return (FontDefinition) labelsToDefinitions.get(selection[0]);
		else
			return null;
	}

	/**
	 * Creates the change button for this field editor.=
	 */
	private void createChangeControl(
		Composite parent,
		String changeButtonLabel) {
		changeFontButton = new Button(parent, SWT.PUSH);

		changeFontButton.setText(changeButtonLabel); //$NON-NLS-1$
		applyDialogFont(changeFontButton);
		setButtonLayoutData(changeFontButton);

		changeFontButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FontDefinition definition = getSelectedFontDefinition();
				if (definition != null) {
					FontDialog fontDialog =
						new FontDialog(changeFontButton.getShell());
					FontData[] currentData = getFontDataSetting(definition);
					fontDialog.setFontData(currentData[0]);
					FontData font = fontDialog.open();
					if (font != null) {
						FontData[] fonts = new FontData[1];
						fonts[0] = font;
						fontDataSettings.put(definition.getId(), fonts);
						updateForSelectedFontDefinition(definition);
					}

				}

			}
		});

		changeFontButton.setEnabled(false);
	}

	/**
	 * Creates the Use System Font button for the editor.
	 */
	private void createUseDefaultsControl(
		Composite parent,
		String useSystemLabel) {

		useSystemButton = new Button(parent, SWT.PUSH | SWT.CENTER);
		useSystemButton.setText(useSystemLabel); //$NON-NLS-1$
		applyDialogFont(useSystemButton);
		setButtonLayoutData(useSystemButton);

		useSystemButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FontDefinition definition = getSelectedFontDefinition();
				if (definition != null) {
					FontData[] defaultFontData =
						JFaceResources.getDefaultFont().getFontData();
					fontDataSettings.put(definition.getId(), defaultFontData);
					updateForSelectedFontDefinition(definition);
				}
			}
		});

		useSystemButton.setEnabled(false);
	}

	/**
	 * Creates the preview control for this field editor.
	 */
	private void createPreviewControl(Composite parent) {
		Label label = new Label(parent, SWT.LEFT);
		label.setText(WorkbenchMessages.getString("FontsPreference.preview")); //$NON-NLS-1$
		applyDialogFont(label);

		previewer = new DefaultPreviewer(parent);
		Control control = previewer.getControl();
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = previewer.getPreferredHeight();
		control.setLayoutData(gd);
	}

	/**
		 * Creates the widgets for the description.
		 */
	private void createDescriptionControl(Composite mainComposite) {

		Composite textComposite = new Composite(mainComposite, SWT.NONE);
		textComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout textLayout = new GridLayout();
		textLayout.marginWidth = 0;
		textLayout.marginHeight = 0;
		textComposite.setLayout(textLayout);

		Label descriptionLabel = new Label(textComposite, SWT.NONE);
		descriptionLabel.setText(WorkbenchMessages.getString("FontsPreference.description")); //$NON-NLS-1$
		applyDialogFont(descriptionLabel);

		descriptionText =
			new Text(
				textComposite,
				SWT.MULTI
 SWT.WRAP
 SWT.READ_ONLY
 SWT.BORDER
 SWT.H_SCROLL);
		descriptionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(descriptionText);
	}

	/**
	 * Creates the value control for this field editor. The value control
	 * displays the currently selected font name.
	 */
	private void createValueControl(Composite parent) {
		valueControl = new Label(parent, SWT.CENTER);

		valueControl.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				valueControl = null;
			}
		});

		applyDialogFont(valueControl);

		GridData gd =
			new GridData(
				GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER);

		gd.grabExcessHorizontalSpace = true;
		valueControl.setLayoutData(gd);
	}

	/**
	 * Updates the value label and the previewer to reflect the
	 * newly selected font definition.
	 * @param FontDefinition
	 */
	private void updateForSelectedFontDefinition(FontDefinition definition) {

		FontData[] font = getFontDataSetting(definition);

		valueControl.setText(StringConverter.asString(font[0]));
		previewer.setFont(font);

		String text = definition.getDescription();
		if (text == null || text.length() == 0)
			descriptionText.setText(WorkbenchMessages.getString("PreferencePage.noDescription")); //$NON-NLS-1$
		else
			descriptionText.setText(text);
	}

	/**
	 * Get the current font data setting for the definition.
	 * @param definition
	 * @return FontData[]
	 */
	private FontData[] getFontDataSetting(FontDefinition definition) {
		String fontId = definition.getId();

		Object setting = fontDataSettings.get(fontId);
		if (DEFAULT_TOKEN.equals(setting))
			return getDefaultFont(definition);
		else
			return (FontData[]) setting;

	}

	/**
	 * Return the defualt FontData for the definition.
	 * @param definition
	 * @return FontData[]
	 */
	private FontData[] getDefaultFont(FontDefinition definition) {

		String defaultsTo = definition.getDefaultsTo();
		if (defaultsTo == null) {
			return PreferenceConverter.getDefaultFontDataArray(
				getPreferenceStore(),
				definition.getId());
		} else {
			FontDefinition defaultDefinition = getDefinition(defaultsTo);
			if (defaultDefinition == null)
				return JFaceResources.getDefaultFont().getFontData();
			else
				return getFontDataSetting(defaultDefinition);
		}
	}

	/*
	 * @see IWorkbenchPreferencePage#init
	 */
	public void init(IWorkbench workbench) {

		//Set up the mappings we currently have

		labelsToDefinitions = new Hashtable();
		//Set the user selected values to an empty table
		fontDataSettings = new Hashtable();

		FontDefinition[] definitions = getDefinitions();

		for (int i = 0; i < definitions.length; i++) {
			FontDefinition definition = definitions[i];
			labelsToDefinitions.put(definition.getLabel(), definition);
			Object settingValue;
			if (getPreferenceStore().isDefault(definition.getId()))
				settingValue = DEFAULT_TOKEN;
			else
				settingValue =
					JFaceResources.getFont(definition.getId()).getFontData();
			fontDataSettings.put(definition.getId(), settingValue);
		}

	}

	/*
	 * @see IWorkbenchPreferencePage#performDefaults
	*/
	protected void performDefaults() {

		FontDefinition currentSelection = getSelectedFontDefinition();
		FontDefinition[] definitions = getDefinitions();

		for (int i = 0; i < definitions.length; i++) {
			FontDefinition definition = definitions[i];

			//Put an entry of null in to represent the reset
			fontDataSettings.put(definition.getId(), DEFAULT_TOKEN);

			if (definition.equals(currentSelection)) {
				//Now we have the defaults ask the registry which to use of these
				//values
				updateForSelectedFontDefinition(definition);
			}
		}
		super.performDefaults();
	}

	/*
	 * @see IWorkbenchPreferencePage#performDefaults
	*/
	public boolean performOk() {

		FontDefinition[] definitions = getDefinitions();
		IPreferenceStore store = getPreferenceStore();
		for (int i = 0; i < definitions.length; i++) {
			FontDefinition definition = definitions[i];
			String preferenceId = definition.getId();
			String registryKey = definition.getId();

			Object setValue = fontDataSettings.get(preferenceId);

			if (DEFAULT_TOKEN.equals(setValue)) {
				store.setToDefault(registryKey);
			} else {
				FontData[] newData = (FontData[]) setValue;
				//Don't update the preference store if there has been no change
				if (!newData
					.equals(
						PreferenceConverter.getFontData(store, registryKey))) {
					PreferenceConverter.setValue(store, registryKey, newData);
				}

			}
		}

		return super.performOk();
	}

	/**
	 * Get the font definitions we will be using.
	 * @return FontDefinition[]
	 */
	private FontDefinition[] getDefinitions() {
		return FontDefinition.getDefinitions();
	}

	/**
	 * Get the FontDefinition with the specified registryKey.
	 * @param registryKey
	 * @return FontDefinition
	 */
	private FontDefinition getDefinition(String registryKey) {
		FontDefinition[] definitions = getDefinitions();
		for (int i = 0; i < definitions.length; i++) {
			if (definitions[i].getId().equals(registryKey))
				return definitions[i];
		}
		return null;
	}

	/**
	 * Return whether the definition has a non default setting.
	 * @param definition
	 * @return boolean
	 */
	private boolean hasSetting(FontDefinition definition) {
		return fontDataSettings.get(definition.getId()) instanceof FontData[];
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	protected void performApply() {
		super.performApply();

		//Apply the default font to the dialog.
		Font oldFont = appliedDialogFont;

		FontData[] newData =
			getFontDataSetting(getDefinition(JFaceResources.DIALOG_FONT));

		appliedDialogFont = new Font(getControl().getDisplay(), newData);

		updateForDialogFontChange(appliedDialogFont);
		getApplyButton().setFont(appliedDialogFont);
		getDefaultsButton().setFont(appliedDialogFont);

		if (oldFont != null)
			oldFont.dispose();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3842.java