error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6780.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6780.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6780.java
text:
```scala
r@@eturn DataTransferMessages.getString("FileExport.toDirectory"); //$NON-NLS-1$

package org.eclipse.ui.wizards.datatransfer;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;
import org.eclipse.ui.help.WorkbenchHelp;

/**
 *	Page 1 of the base resource export-to-file-system Wizard
 */
/*package*/ class WizardFileSystemResourceExportPage1 extends WizardExportResourcesPage implements Listener {

	// widgets
	private Combo				destinationNameField;
	private Button				destinationBrowseButton;
	protected Button			overwriteExistingFilesCheckbox;
	protected Button			createDirectoryStructureButton;
	protected Button			createSelectionOnlyButton;

	// constants
	private static final int	SIZING_TEXT_FIELD_WIDTH = 250;

	// dialog store id constants
	private static final String STORE_DESTINATION_NAMES_ID =
		"WizardFileSystemResourceExportPage1.STORE_DESTINATION_NAMES_ID";//$NON-NLS-1$
	private static final String STORE_OVERWRITE_EXISTING_FILES_ID =
		"WizardFileSystemResourceExportPage1.STORE_OVERWRITE_EXISTING_FILES_ID";//$NON-NLS-1$
	private static final String STORE_CREATE_STRUCTURE_ID =
		"WizardFileSystemResourceExportPage1.STORE_CREATE_STRUCTURE_ID";//$NON-NLS-1$
	//messages
	private static final String DESTINATION_EMPTY_MESSAGE = DataTransferMessages.getString("FileExport.destinationEmpty"); //$NON-NLS-1$
	private static final String SELECT_DESTINATION_MESSAGE = DataTransferMessages.getString("DataTransfer.selectDestination"); //$NON-NLS-1$
/**
 *	Create an instance of this class
 */
protected WizardFileSystemResourceExportPage1(String name, IStructuredSelection selection) {
	super(name, selection);
}
/**
 *	Create an instance of this class
 */
public WizardFileSystemResourceExportPage1(IStructuredSelection selection) {
	this("fileSystemExportPage1", selection);//$NON-NLS-1$
	setTitle(DataTransferMessages.getString("DataTransfer.fileSystemTitle")); //$NON-NLS-1$
	setDescription(DataTransferMessages.getString("FileExport.exportLocalFileSystem")); //$NON-NLS-1$
}
/**
 *	Add the passed value to self's destination widget's history
 *
 *	@param value java.lang.String
 */
protected void addDestinationItem(String value) {
	destinationNameField.add(value);
}
/** (non-Javadoc)
 * Method declared on IDialogPage.
 */
public void createControl(Composite parent) {
	super.createControl(parent);
	giveFocusToDestination();
	WorkbenchHelp.setHelp(getControl(), IDataTransferHelpContextIds.FILE_SYSTEM_EXPORT_WIZARD_PAGE);
}
/**
 *	Create the export destination specification widgets
 *
 *	@param parent org.eclipse.swt.widgets.Composite
 */
protected void createDestinationGroup(Composite parent) {
	
	Font font = parent.getFont();
	// destination specification group
	Composite destinationSelectionGroup = new Composite(parent, SWT.NONE);
	GridLayout layout = new GridLayout();
	layout.numColumns = 3;
	destinationSelectionGroup.setLayout(layout);
	destinationSelectionGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	destinationSelectionGroup.setFont(font);

	Label destinationLabel = new Label(destinationSelectionGroup, SWT.NONE);
	destinationLabel.setText(getDestinationLabel());
	destinationLabel.setFont(font);

	// destination name entry field
	destinationNameField = new Combo(destinationSelectionGroup, SWT.SINGLE | SWT.BORDER);
	destinationNameField.addListener(SWT.Modify, this);
	destinationNameField.addListener(SWT.Selection, this);
	GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	data.widthHint = SIZING_TEXT_FIELD_WIDTH;
	destinationNameField.setLayoutData(data);
	destinationNameField.setFont(font);

	// destination browse button
	destinationBrowseButton = new Button(destinationSelectionGroup, SWT.PUSH);
	destinationBrowseButton.setText(DataTransferMessages.getString("DataTransfer.browse")); //$NON-NLS-1$
	destinationBrowseButton.addListener(SWT.Selection, this);
	destinationBrowseButton.setFont(font);
	setButtonLayoutData(destinationBrowseButton);

	new Label(parent, SWT.NONE);	// vertical spacer
}

/**
 * Create the buttons in the options group.
 */

protected void createOptionsGroupButtons(Group optionsGroup) {
	
	Font font = optionsGroup.getFont();
	createOverwriteExisting(optionsGroup, font);

	createDirectoryStructureOptions(optionsGroup, font);
}

/**
 * Create the buttons for the group that determine if the entire or
 * selected directory structure should be created.
 * @param optionsGroup
 * @param font
 */
protected void createDirectoryStructureOptions(Group optionsGroup, Font font) {
	// create directory structure radios
	createDirectoryStructureButton = new Button(optionsGroup, SWT.RADIO | SWT.LEFT);
	createDirectoryStructureButton.setText(
		DataTransferMessages.getString("FileExport.createDirectoryStructure")); //$NON-NLS-1$
	createDirectoryStructureButton.setSelection(false);
	createDirectoryStructureButton.setFont(font);
	
	
	// create directory structure radios
	createSelectionOnlyButton = new Button(optionsGroup, SWT.RADIO | SWT.LEFT);
	createSelectionOnlyButton.setText(
		DataTransferMessages.getString("FileExport.createSelectedDirectories"));//$NON-NLS-1$}
	createSelectionOnlyButton.setSelection(true);
	createSelectionOnlyButton.setFont(font);
}

/**
 * Create the button for checking if we should ask if we are going to
 * overwrite existing files.
 * @param optionsGroup
 * @param font
 */
protected void createOverwriteExisting(Group optionsGroup, Font font) {
	// overwrite... checkbox
	overwriteExistingFilesCheckbox = new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
	overwriteExistingFilesCheckbox.setText(
		DataTransferMessages.getString("ExportFile.overwriteExisting")); //$NON-NLS-1$
	overwriteExistingFilesCheckbox.setFont(font);
}

/**
 * Attempts to ensure that the specified directory exists on the local file system.
 * Answers a boolean indicating success.
 *
 * @return boolean
 * @param directory java.io.File
 */
protected boolean ensureDirectoryExists(File directory) {
	if (!directory.exists()) {
		if (!queryYesNoQuestion(DataTransferMessages.getString("DataTransfer.createTargetDirectory"))) //$NON-NLS-1$
			return false;

		if (!directory.mkdirs()) {
			displayErrorDialog(DataTransferMessages.getString("DataTransfer.directoryCreationError")); //$NON-NLS-1$
			giveFocusToDestination();
			return false;
		}
	}

	return true;
}
/**
 *	If the target for export does not exist then attempt to create it.
 *	Answer a boolean indicating whether the target exists (ie.- if it
 *	either pre-existed or this method was able to create it)
 *
 *	@return boolean
 */
protected boolean ensureTargetIsValid(File targetDirectory) {
	if (targetDirectory.exists() && !targetDirectory.isDirectory()) {
		displayErrorDialog(DataTransferMessages.getString("FileExport.directoryExists")); //$NON-NLS-1$
		giveFocusToDestination();
		return false;
	}

	return ensureDirectoryExists(targetDirectory);
}
/**
 *  Set up and execute the passed Operation.  Answer a boolean indicating success.
 *
 *  @return boolean
 */
protected boolean executeExportOperation(FileSystemExportOperation op) {
	op.setCreateLeadupStructure(createDirectoryStructureButton.getSelection());
	op.setOverwriteFiles(overwriteExistingFilesCheckbox.getSelection());
	
	try {
		getContainer().run(true, true, op);
	} catch (InterruptedException e) {
		return false;
	} catch (InvocationTargetException e) {
		displayErrorDialog(e.getTargetException());
		return false;
	}

	IStatus status = op.getStatus();
	if (!status.isOK()) {
		ErrorDialog.openError(getContainer().getShell(),
			DataTransferMessages.getString("DataTransfer.exportProblems"), //$NON-NLS-1$
			null,   // no special message
			status);
		return false;
	}
	
	return true;
}
/**
 *	The Finish button was pressed.  Try to do the required work now and answer
 *	a boolean indicating success.  If false is returned then the wizard will
 *	not close.
 *
 *	@return boolean
 */
public boolean finish() {
	if (!ensureTargetIsValid(new File(getDestinationValue())))
		return false;

	List resourcesToExport = getWhiteCheckedResources();

	//Save dirty editors if possible but do not stop if not all are saved
	saveDirtyEditors();
	// about to invoke the operation so save our state
	saveWidgetValues();

	if (resourcesToExport.size() > 0)
		return executeExportOperation(
			new FileSystemExportOperation(
				null,
				resourcesToExport,
				getDestinationValue(),
				this));

	MessageDialog.openInformation(
		getContainer().getShell(),
		DataTransferMessages.getString("DataTransfer.information"), //$NON-NLS-1$
		DataTransferMessages.getString("FileExport.noneSelected")); //$NON-NLS-1$

	return false;
}
/**
 *	Answer the string to display in self as the destination type
 *
 *	@return java.lang.String
 */
protected String getDestinationLabel() {
	return DataTransferMessages.getString("DataTransfer.directory"); //$NON-NLS-1$
}
/**
 *	Answer the contents of self's destination specification widget
 *
 *	@return java.lang.String
 */
protected String getDestinationValue() {
	return destinationNameField.getText().trim();
}
/**
 *	Set the current input focus to self's destination entry field
 */
protected void giveFocusToDestination() {
	destinationNameField.setFocus();
}
/**
 *	Open an appropriate destination browser so that the user can specify a source
 *	to import from
 */
protected void handleDestinationBrowseButtonPressed() {
	DirectoryDialog dialog =
		new DirectoryDialog(getContainer().getShell(), SWT.SAVE);
	dialog.setMessage(SELECT_DESTINATION_MESSAGE);
	dialog.setFilterPath(getDestinationValue());
	String selectedDirectoryName = dialog.open();

	if (selectedDirectoryName != null) {
		setErrorMessage(null);
		setDestinationValue(selectedDirectoryName);
	}
}
/**
 * Handle all events and enablements for widgets in this page
 * @param e Event
 */
public void handleEvent(Event e) {
	Widget source = e.widget;

	if (source == destinationBrowseButton)
		handleDestinationBrowseButtonPressed();

	updatePageCompletion();
}
/**
 *	Hook method for saving widget values for restoration by the next instance
 *	of this class.
 */
protected void internalSaveWidgetValues() {
	// update directory names history
	IDialogSettings settings = getDialogSettings();
	if (settings != null) {
		String[] directoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
		if (directoryNames == null)
			directoryNames = new String[0];

		directoryNames = addToHistory(directoryNames, getDestinationValue());
		settings.put(STORE_DESTINATION_NAMES_ID, directoryNames);

		// options
		settings.put(
			STORE_OVERWRITE_EXISTING_FILES_ID,
			overwriteExistingFilesCheckbox.getSelection());

		settings.put(
			STORE_CREATE_STRUCTURE_ID,
			createDirectoryStructureButton.getSelection());

	}
}
/**
 *	Hook method for restoring widget values to the values that they held
 *	last time this wizard was used to completion.
 */
protected void restoreWidgetValues() {
	IDialogSettings settings = getDialogSettings();
	if (settings != null) {
		String[] directoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
		if (directoryNames == null)
			return; // ie.- no settings stored

		// destination
		setDestinationValue(directoryNames[0]);
		for (int i = 0; i < directoryNames.length; i++)
			addDestinationItem(directoryNames[i]);

		// options
		overwriteExistingFilesCheckbox.setSelection(
			settings.getBoolean(STORE_OVERWRITE_EXISTING_FILES_ID));

		boolean createDirectories = settings.getBoolean(STORE_CREATE_STRUCTURE_ID);
		createDirectoryStructureButton.setSelection(createDirectories);
		createSelectionOnlyButton.setSelection(!createDirectories);
	}
}
/**
 *	Set the contents of the receivers destination specification widget to
 *	the passed value
 *
 */
protected void setDestinationValue(String value) {
	destinationNameField.setText(value);
}
/**
 *	Answer a boolean indicating whether the receivers destination specification
 *	widgets currently all contain valid values.
 */
protected boolean validateDestinationGroup() {
	String destinationValue = getDestinationValue();
	if (destinationValue.length() == 0) {
		setMessage(DESTINATION_EMPTY_MESSAGE);
		return false;
	} 
	
	String conflictingContainer = getConflictingContainerNameFor(destinationValue);
	if(conflictingContainer == null)
		setErrorMessage(""); //$NON-NLS-1$
	else {
		setErrorMessage(
			DataTransferMessages.format(
				"FileExport.conflictingContainer",  //$NON-NLS-1$
				new Object[] {conflictingContainer}));
		giveFocusToDestination();
		return false;
	}
	
	return true;
}

/**
 * Returns the name of a container with a location that encompasses targetDirectory.
 * Returns null if there is no conflict.
 * 
 * @param targetDirectory the path of the directory to check.
 * @return the conflicting container name or <code>null</code>
 */
protected String getConflictingContainerNameFor(String targetDirectory){
	
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	IPath testPath = new Path(targetDirectory);
	
	if(root.getLocation().isPrefixOf(testPath))
		return DataTransferMessages.getString("FileExport.rootName"); //$NON-NLS-1$
	
	IProject[] projects = root.getProjects();
	
	for(int i = 0; i < projects.length; i++){
		if(projects[i].getLocation().isPrefixOf(testPath))
			return projects[i].getName();
	}
	
	return null;
	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6780.java