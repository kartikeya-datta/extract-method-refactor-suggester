error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7526.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7526.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7526.java
text:
```scala
g@@d.horizontalAlignment = GridData.FILL;

package org.eclipse.ui.internal.dialogs;

/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import java.text.Collator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.*;
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
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.IHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.misc.Sorter;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class FontPreferencePage
	extends PreferencePage
	implements IWorkbenchPreferencePage {

	Hashtable namesToIds;
	Hashtable idsToFontData;
	List fontList;
	Button changeFontButton;
	Button useDefaultsButton;

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
				
			FontData bestData = 
				JFaceResources.getFontRegistry().
					bestData(fontData,text.getDisplay());
					
			//If there are no specified values then return.
			if(bestData == null)
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

	/*
	 * @see PreferencePage#createContents
	 */
	public Control createContents(Composite parent) {
		WorkbenchHelp.setHelp(getControl(), IHelpContextIds.FONT_PREFERENCE_PAGE);

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
		
		createUseDefaultsControl(
			buttonColumn,
			WorkbenchMessages.getString("FontsPreference.useSystemFont")); //$NON-NLS-1$
		createChangeControl(buttonColumn, JFaceResources.getString("openChange")); //$NON-NLS-1$

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
		
		Font font = firstColumn.getFont();
		
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
		label.setFont(font);
		
		fontList = new List(parent, SWT.BORDER);
		data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		fontList.setLayoutData(data);
		fontList.setFont(font);

		fontList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String selectedFontId = getSelectedFontId();
				if (selectedFontId == null){
					changeFontButton.setEnabled(false);
					useDefaultsButton.setEnabled(false);
				}
				else{
					changeFontButton.setEnabled(true);
					useDefaultsButton.setEnabled(true);
					updateForFont((FontData[]) idsToFontData.get(selectedFontId));
				}
			}
		});

		Set names = namesToIds.keySet();
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

	private String getSelectedFontId() {
		String[] selection = fontList.getSelection();
		if (selection.length == 1)
			return (String) namesToIds.get(selection[0]);
		else
			return null;
	}

	/**
	 * Creates the change button for this field editor.=
	 */
	private void createChangeControl(Composite parent, String changeButtonLabel) {
		changeFontButton = new Button(parent, SWT.PUSH);

		changeFontButton.setText(changeButtonLabel); //$NON-NLS-1$
		changeFontButton.setFont(parent.getFont());
		setButtonLayoutData(changeFontButton);
		
		changeFontButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String selectedFontId = getSelectedFontId();
				if (selectedFontId != null) {
					FontDialog fontDialog = new FontDialog(changeFontButton.getShell());
					FontData[] currentData = (FontData[]) idsToFontData.get(selectedFontId);
					fontDialog.setFontData(currentData[0]);
					FontData font = fontDialog.open();
					if (font != null) {
						FontData[] fonts = new FontData[1];
						fonts[0] = font;
						idsToFontData.put(selectedFontId, fonts);
						updateForFont(fonts);
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

		useDefaultsButton = new Button(parent, SWT.PUSH | SWT.CENTER);
		useDefaultsButton.setText(useSystemLabel); //$NON-NLS-1$
		useDefaultsButton.setFont(parent.getFont());
		setButtonLayoutData(useDefaultsButton);
		
		useDefaultsButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String selectedFontId = getSelectedFontId();
				if (selectedFontId != null) {
					FontData[] defaultFontData = JFaceResources.getDefaultFont().getFontData();
					idsToFontData.put(selectedFontId, defaultFontData);
					updateForFont(defaultFontData);
				}
			}
		});
		
		useDefaultsButton.setEnabled(false);
	}

	/**
	 * Creates the preview control for this field editor.
	 */
	private void createPreviewControl(Composite parent) {
		Label label = new Label(parent, SWT.LEFT);
		label.setText(WorkbenchMessages.getString("FontsPreference.preview")); //$NON-NLS-1$
		label.setFont(parent.getFont());

		previewer = new DefaultPreviewer(parent);
		Control control = previewer.getControl();
		GridData gd = new GridData();
		gd.horizontalAlignment = gd.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = previewer.getPreferredHeight();
		control.setLayoutData(gd);
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
		
		valueControl.setFont(parent.getFont());

		GridData gd =
			new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER);

		gd.grabExcessHorizontalSpace = true;
		valueControl.setLayoutData(gd);
	}

	/**
	 * Updates the value label and the previewer to reflect the
	 * newly selected font.
	 */
	private void updateForFont(FontData[] font) {

		valueControl.setText(StringConverter.asString(font[0]));
		previewer.setFont(font);
	}

	/*
	 * @see IWorkbenchPreferencePage#init
	 */
	public void init(IWorkbench workbench) {

		//Set up the mappings we currently have

		namesToIds = new Hashtable();
		namesToIds.put(
			WorkbenchMessages.getString("FontsPreference.BannerFont"), //$NON-NLS-1$
			JFaceResources.BANNER_FONT);

		namesToIds.put(
			WorkbenchMessages.getString("FontsPreference.TextFont"), //$NON-NLS-1$
			JFaceResources.TEXT_FONT);

		namesToIds.put(
			WorkbenchMessages.getString("FontsPreference.HeaderFont"), //$NON-NLS-1$
			JFaceResources.HEADER_FONT);
		//Commenting out DIALOG_FONT until all of Eclipse uses it	
//		namesToIds.put(
//			WorkbenchMessages.getString("FontsPreference.DialogFont"), //$NON-NLS-1$
//			JFaceResources.DIALOG_FONT);

		//Now set up the fonts

		idsToFontData = new Hashtable();
		idsToFontData.put(
			JFaceResources.BANNER_FONT,
			(JFaceResources.getBannerFont().getFontData()));

		idsToFontData.put(
			JFaceResources.TEXT_FONT,
			(JFaceResources.getTextFont().getFontData()));

		idsToFontData.put(
			JFaceResources.HEADER_FONT,
			(JFaceResources.getHeaderFont().getFontData()));
			
//		idsToFontData.put(
//			JFaceResources.DIALOG_FONT,
//			(JFaceResources.getDialogFont().getFontData()));

	}

	/*
	 * @see IWorkbenchPreferencePage#performDefaults
	*/
	protected void performDefaults() {

		Enumeration fontSettingsEnumerator = idsToFontData.keys();
		String currentSelection = getSelectedFontId();

		while (fontSettingsEnumerator.hasMoreElements()) {
			String preferenceName = (String) fontSettingsEnumerator.nextElement();
			FontData[] defaultData =
				PreferenceConverter.getDefaultFontDataArray(getPreferenceStore(), preferenceName);
				
			//Now we have the defaults ask the registry which to use of these
			//values.
			FontData bestChoice = 
				JFaceResources.getFontRegistry().
					bestData(defaultData,valueControl.getDisplay());
					
			//The default data was empty so use the system default
			if(bestChoice == null)
				defaultData =
					valueControl.getDisplay().
						getSystemFont().getFontData();
			else{
				defaultData = new FontData[1];
				defaultData[0] = bestChoice;
			}
						
			idsToFontData.put(preferenceName, defaultData);
			
			if (preferenceName.equals(currentSelection))
				updateForFont(defaultData);
		}
		super.performDefaults();
	}

	/*
	 * @see IWorkbenchPreferencePage#performDefaults
	*/
	public boolean performOk() {

		Enumeration fontSettingsEnumerator = idsToFontData.keys();
		while (fontSettingsEnumerator.hasMoreElements()) {
			String preferenceName = (String) fontSettingsEnumerator.nextElement();
			PreferenceConverter.setValue(
				getPreferenceStore(),
				preferenceName,
				(FontData[]) idsToFontData.get(preferenceName));
		}
		WorkbenchPlugin.getDefault().savePluginPreferences();
		return super.performOk();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7526.java