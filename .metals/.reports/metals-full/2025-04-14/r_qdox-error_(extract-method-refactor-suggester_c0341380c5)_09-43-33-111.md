error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3232.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3232.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3232.java
text:
```scala
a@@boutInfo = ((Workbench) PlatformUI.getWorkbench()).getConfigurationInfo().getAboutInfo();

package org.eclipse.ui.internal.dialogs;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All Rights Reserved.
 * Contributors:  Sebastian Davids <sdavids@gmx.de> - Fix for bug 19346 - Dialog
 * font should be activated and used by other components.
 */

import java.net.URL;
import java.text.Collator;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.*;

/**
 * Displays information about the product plugins.
 *
 * @private
 *		This class is internal to the workbench and must not be called outside the workbench
 */
public class AboutPluginsDialog extends ProductInfoDialog {

	/**
	 * Table height in dialog units (value 200).
	 */
	private static final int TABLE_HEIGHT = 200;

	private static final String PLUGININFO = "about.html";	//$NON-NLS-1$
	
	private final static int MORE_ID = IDialogConstants.CLIENT_ID + 1;
	
	private boolean webBrowserOpened = false;

	private Table vendorInfo;
	private Button moreInfo;
	
	private String title;
	private String message;

	private String columnTitles[] =
		{ WorkbenchMessages.getString("AboutPluginsDialog.provider"), //$NON-NLS-1$
		WorkbenchMessages.getString("AboutPluginsDialog.pluginName"), //$NON-NLS-1$
		WorkbenchMessages.getString("AboutPluginsDialog.version"), //$NON-NLS-1$
	};

	private IPluginDescriptor[] info;

	private AboutInfo aboutInfo;
	
	private int lastColumnChosen = 0;	// initially sort by provider
	private boolean reverseSort = false;	// initially sort ascending
	private IPluginDescriptor lastSelection = null;
	
	/**
	 * Constructor for AboutPluginsDialog
	 */
	public AboutPluginsDialog(Shell parentShell) {
		this(
			parentShell, 
			Platform.getPluginRegistry().getPluginDescriptors(),
			null,
			null);
	}

	/**
	 * Constructor for AboutPluginsDialog
	 */
	public AboutPluginsDialog(Shell parentShell, IPluginDescriptor[] descriptors, String title, String msg) {
		super(parentShell);
		info = descriptors;
		this.title = title;
		message = msg;
		sortByProvider();
		aboutInfo = ((Workbench) PlatformUI.getWorkbench()).getAboutInfo();
	}

	/* (non-Javadoc)
	 * Method declared on Dialog.
	 */
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
			case MORE_ID : {
				handleMoreInfoPressed();
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	/* (non-Javadoc)
	 * Method declared on Window.
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		if (title == null) {
			title = aboutInfo.getProductName();
			if (title != null) { 
				title = WorkbenchMessages.format(
						"AboutPluginsDialog.shellTitle",	//$NON-NLS-1$
						new Object[] {title});
			}
		} 
		if (title != null)
			newShell.setText(title);

		WorkbenchHelp.setHelp(
			newShell,
			IHelpContextIds.ABOUT_PLUGINS_DIALOG);
	}
	/**
	 * Add buttons to the dialog's button bar.
	 *
	 * Subclasses should override.
	 *
	 * @param parent the button bar composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	
		moreInfo = createButton(parent, MORE_ID, WorkbenchMessages.getString("AboutPluginsDialog.moreInfo"), false); //$NON-NLS-1$

		// set initial enablement
		moreInfo.setEnabled(tableHasSelection() & selectionHasInfo());

		Label l = new Label(parent, SWT.NONE);
		l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = (GridLayout)parent.getLayout();
		layout.numColumns++;
		layout.makeColumnsEqualWidth = false;
	
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}


	/**
	 * Create the contents of the dialog (above the button bar).
	 *
	 * Subclasses should overide.
	 *
	 * @param the parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	protected Control createDialogArea(Composite parent) {

		Composite outer = (Composite) super.createDialogArea(parent);
		
		if (message != null) {
			Label label = new Label(outer, SWT.NONE);
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			label.setFont(parent.getFont());
			label.setText(message);
		}

		createTable(outer);
		createColumns();

		GridData gridData =
			new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;

		// suggest a height for the table
		gridData.heightHint = convertVerticalDLUsToPixels(TABLE_HEIGHT);
		vendorInfo.setLayoutData(gridData);

		return outer;
	}
	/**
	 * Create the table part of the dialog.
	 *
	 * @param the parent composite to contain the dialog area
	 */
	protected void createTable(Composite parent) {
		vendorInfo =
			new Table(
				parent,
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		vendorInfo.setHeaderVisible(true);
		vendorInfo.setLinesVisible(true);
		vendorInfo.setFont(parent.getFont());
		
		SelectionListener listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setMoreButtonEnablement(e.item != null);
			}
		};
		vendorInfo.addSelectionListener(listener);
	}
	/**
	 * Populate the table with plugin info obtained from the registry.
	 *
	 * @param the parent composite to contain the dialog area
	 */
	protected void createColumns() {
		/* create table headers */
		int[] columnWidths =
			{
				convertHorizontalDLUsToPixels(120),
				convertHorizontalDLUsToPixels(180),
				convertHorizontalDLUsToPixels(105)};
		for (int i = 0; i < columnTitles.length; i++) {
			TableColumn tableColumn = new TableColumn(vendorInfo, SWT.NULL);
			tableColumn.setWidth(columnWidths[i]);
			tableColumn.setText(columnTitles[i]);
			final int columnIndex = i;
			tableColumn.addSelectionListener(new SelectionAdapter() {		
				public void widgetSelected(SelectionEvent e) {
					sort(columnIndex);
				}
			});
		
		}

		/* fill each row of the table with plugin registry info */
		for (int i = 0; i < info.length; i++) {
			String provider = info[i].getProviderName();
			String pluginName = info[i].getLabel();
			String version = info[i].getVersionIdentifier().toString();
			String[] row = { provider, pluginName, version };
			TableItem item = new TableItem(vendorInfo, SWT.NULL);
			item.setText(row);
		}
	}
	/**
	 * Set enablement of moreInfo button based on whether or not 
	 * there is a selection in the table and if there is any additional
	 * info to show for that plugin.
	 *
	 * @param isSelected whether there is a selection in the table
	 */
	protected void setMoreButtonEnablement(boolean isSelected) {
		moreInfo.setEnabled(isSelected && selectionHasInfo());
	}

	/**
	 * Check if the currently selected plugin has additional
	 * information to show.
	 * 
	 * @return true if the selected plugin has additional info available to display
	 */
	private boolean selectionHasInfo(){
			TableItem[] items = vendorInfo.getSelection();
			if (items.length == 0)
				return false;
			int i = vendorInfo.getSelectionIndex();
			IPluginDescriptor desc = info[i];
			URL infoURL = desc.find(new Path(PLUGININFO));
			if (infoURL == null && WorkbenchPlugin.DEBUG) {
				// only report ini problems if the -debug command line argument is used
				WorkbenchPlugin.log("Problem reading plugin info for: " + desc.getLabel()); //$NON-NLS-1$
			} 
			return infoURL != null;
	}

	/**
	 * Create the button to provide more info on the selected plugin.
	 *
	 * @return true if there is an item selected in the table, false otherwise
	 */
	private boolean tableHasSelection() {
		if (vendorInfo == null)
			return false;
		return (vendorInfo.getSelectionCount() > 0);

	}

	/** 
	 * Respond to moreInfo button pressed.
	 * 
	 */
	protected void handleMoreInfoPressed() {
		TableItem[] items = vendorInfo.getSelection();
		if (items.length == 0)
			return;
		int i = vendorInfo.getSelectionIndex();
		IPluginDescriptor desc = info[i];
		openMoreInfo(desc);
	}
	/** 
	 * Open html file containing additional info about the selected
	 * plugin.
	 * 
	 */
	private void openMoreInfo(IPluginDescriptor desc) {
		URL infoURL = desc.find(new Path(PLUGININFO));
		if (infoURL == null) {
			MessageDialog.openError(
				getShell(), 
				WorkbenchMessages.getString("AboutPluginsDialog.errorTitle"), //$NON-NLS-1$
				WorkbenchMessages.format("AboutPluginsDialog.unableToOpenFile", new Object[] {PLUGININFO, desc.getUniqueIdentifier()})); //$NON-NLS-1$
			return;
		}

		openLink(infoURL.toString());
	}
	
	/**
	 * Sort the rows of the table based on the selected column.
	 *
	 * @param column index of table column selected as sort criteria
	 */
	private void sort(int column) {
		// Choose new sort algorithm
		if (lastColumnChosen == column){
			reverseSort = !reverseSort;
		}
		else{
			reverseSort = false;
			lastColumnChosen = column;
		}
		
		if(vendorInfo.getItemCount() <= 1)	return;

		// Remember the last selection
		int idx = vendorInfo.getSelectionIndex();
		if (idx != -1)
			lastSelection = info[idx];
			
		switch (column){
			case 0:
				sortByProvider();
				break;
			case 1:
				sortById();
				break;
			case 2:
				sortByVersion();
				break;
		}

		refreshTable(column);
	}

	/**
	 * Refresh the rows of the table based on the selected column.
	 * Maintain selection from before sort action request.
	 *
	 * @param items the old state table items 
	 */
	private void refreshTable(int col){
		TableItem[] items = vendorInfo.getItems();
		int idx = -1;	// the new index of the selection
		// Create new order of table items
		for(int i = 0; i < items.length; i++) {
			String provider = info[i].getProviderName();
			String pluginName = info[i].getLabel();
			String version = info[i].getVersionIdentifier().toString();
			String [] row = { provider, pluginName, version };
			items[i].setText(row);
		}
		// Maintain the original selection
		if (lastSelection != null){
			String oldId = lastSelection.getUniqueIdentifier();
			for (int k = 0; k < info.length; k++){
				if (oldId.equalsIgnoreCase(info[k].getUniqueIdentifier()))
					idx = k;
			}	
			vendorInfo.setSelection(idx);
			vendorInfo.showSelection();
		}

		moreInfo.setEnabled(tableHasSelection() && selectionHasInfo());
	}
	/**
	 * Sort the rows of the table based on the plugin provider.
	 * Secondary criteria is unique plugin id.
	 */
	private void sortByProvider(){
		/* If sorting in reverse, info array is already sorted forward by
		 * key so the info array simply needs to be reversed.
		 */
		if (reverseSort){
			java.util.List infoList = Arrays.asList(info);
			Collections.reverse(infoList);
			for (int i=0; i< info.length; i++){
				info[i] = (IPluginDescriptor)infoList.get(i);
			}
		}
		else {
			// Sort ascending
			Arrays.sort(info, new Comparator() {
				Collator coll = Collator.getInstance(Locale.getDefault());
				public int compare(Object a, Object b) {
					IPluginDescriptor d1, d2;
					String provider1, provider2, pluginId1, pluginId2;
					d1 = (IPluginDescriptor) a;
					provider1 = d1.getProviderName();
					pluginId1 = d1.getLabel();
					d2 = (IPluginDescriptor) b;
					provider2 = d2.getProviderName();
					pluginId2 = d2.getLabel();
					if (provider1.equals(provider2))
						return coll.compare(pluginId1, pluginId2);
					else
						return coll.compare(provider1, provider2);
				}
			});
		}
	}
	/**
	 * Sort the rows of the table based on unique plugin id.
	 */	
	private void sortById(){
		/* If sorting in reverse, info array is already sorted forward by
		 * key so the info array simply needs to be reversed.
		 */
		if (reverseSort){
			java.util.List infoList = Arrays.asList(info);
			Collections.reverse(infoList);
			for (int i=0; i< info.length; i++){
				info[i] = (IPluginDescriptor)infoList.get(i);
			}
		}
		else {
			// Sort ascending
			Arrays.sort(info, new Comparator() {
				Collator coll = Collator.getInstance(Locale.getDefault());
				public int compare(Object a, Object b) {
					IPluginDescriptor d1, d2;
					String pluginId1, pluginId2;
					d1 = (IPluginDescriptor) a;
					pluginId1 = d1.getLabel();
					d2 = (IPluginDescriptor) b;
					pluginId2 = d2.getLabel();
					return coll.compare(pluginId1, pluginId2);
				}
			});
		}
	
	}
	/**
	 * Sort the rows of the table based on the plugin version.
	 * Secondary criteria is unique plugin id.
	 */
	private void sortByVersion(){
		/* If sorting in reverse, info array is already sorted forward by
		 * key so the info array simply needs to be reversed.
		 */		
		if (reverseSort){
			java.util.List infoList = Arrays.asList(info);
			Collections.reverse(infoList);
			for (int i=0; i< info.length; i++){
				info[i] = (IPluginDescriptor)infoList.get(i);
			}
		}
		else {
			// Sort ascending
			Arrays.sort(info, new Comparator() {
				Collator coll = Collator.getInstance(Locale.getDefault());
				public int compare(Object a, Object b) {
					IPluginDescriptor d1, d2;
					String version1, version2, pluginId1, pluginId2;
					d1 = (IPluginDescriptor) a;
					version1 = d1.getVersionIdentifier().toString();
					pluginId1 = d1.getLabel();
					d2 = (IPluginDescriptor) b;
					version2 = d2.getVersionIdentifier().toString();
					pluginId2 = d2.getLabel();
					if (version1.equals(version2))
						return coll.compare(pluginId1, pluginId2);
					else
						return coll.compare(version1, version2);
				}
			});
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3232.java