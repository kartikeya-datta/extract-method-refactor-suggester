error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3067.java
text:
```scala
n@@ame = ""; //$NON-NLS-1$

/************************************************************************
Copyright (c) 2000, 2003 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/
package org.eclipse.ui.internal.dialogs;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.*;

/**
 * Displays information about the product.
 *
 * @private
 *		This class is internal to the workbench and must not be called outside the workbench
 */
public class AboutDialog extends ProductInfoDialog {
	private	Image 			image;	//image to display on dialog
	private  	AboutInfo     	aboutInfo;
	private 	ArrayList images = new ArrayList();
	private 	StyledText text;
	private final static	int MAX_IMAGE_WIDTH_FOR_TEXT = 250;
	private final static int FEATURES_ID = IDialogConstants.CLIENT_ID + 1;
	private final static int PLUGINS_ID = IDialogConstants.CLIENT_ID + 2;
	private final static int INFO_ID = IDialogConstants.CLIENT_ID + 3;

/**
 * Create an instance of the AboutDialog
 */
public AboutDialog(Shell parentShell) {
	super(parentShell);
	Workbench workbench = (Workbench)PlatformUI.getWorkbench();
	aboutInfo = workbench.getConfigurationInfo().getAboutInfo();
}
/* (non-Javadoc)
 * Method declared on Dialog.
 */
protected void buttonPressed(int buttonId) {
	switch (buttonId) {
		case FEATURES_ID : {
			new AboutFeaturesDialog(getShell()).open();
			return;
		}
		case PLUGINS_ID : {
			new AboutPluginsDialog(getShell()).open();
			return;
		}
		case INFO_ID : {
			BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
				public void run() {
					((Workbench)PlatformUI.getWorkbench()).getConfigurationInfo().openSystemSummaryEditor();
				}
			});
			close();
			return;
		}
	}
	super.buttonPressed(buttonId);
}

public boolean close() {
	//get rid of the image that was displayed on the left-hand side of the Welcome dialog
	if (image != null)
		image.dispose();
	for (int i = 0; i < images.size(); i++) {
		((Image)images.get(i)).dispose();
	}
	return super.close();
}
/* (non-Javadoc)
 * Method declared on Window.
 */
protected void configureShell(Shell newShell) {
	super.configureShell(newShell);
	String name = aboutInfo.getProductName();
	if (name != null)
		newShell.setText(WorkbenchMessages.format("AboutDialog.shellTitle", new Object[] {name})); //$NON-NLS-1$
	WorkbenchHelp.setHelp(newShell, IHelpContextIds.ABOUT_DIALOG);
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
	
	createButton(parent, FEATURES_ID, WorkbenchMessages.getString("AboutDialog.featureInfo"), false); //$NON-NLS-1$
	createButton(parent, PLUGINS_ID, WorkbenchMessages.getString("AboutDialog.pluginInfo"), false); //$NON-NLS-1$
	createButton(parent, INFO_ID, WorkbenchMessages.getString("AboutDialog.systemInfo"), false); //$NON-NLS-1$

	Label l = new Label(parent, SWT.NONE);
	l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	GridLayout layout = (GridLayout)parent.getLayout();
	layout.numColumns++;
	layout.makeColumnsEqualWidth = false;

	Button b = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	b.setFocus();
}
/**
 * Creates and returns the contents of the upper part 
 * of the dialog (above the button bar).
 *
 * Subclasses should overide.
 *
 * @param the parent composite to contain the dialog area
 * @return the dialog area control
 */
protected Control createDialogArea(Composite parent) {
	setHandCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_HAND));
	setBusyCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_WAIT));
	getShell().addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			if (getHandCursor() != null)
				getHandCursor().dispose();
			if (getBusyCursor() != null)
				getBusyCursor().dispose();
		}
	});
	
	ImageDescriptor imageDescriptor =  aboutInfo.getAboutImage();	// may be null
	if (imageDescriptor != null) 
		image = imageDescriptor.createImage();
	if (image == null || image.getBounds().width <= MAX_IMAGE_WIDTH_FOR_TEXT) {
		// show text
		String aboutText = aboutInfo.getAboutText();
		if (aboutText != null) {
			// get an about item
			setItem(scan(aboutText));
		}
	}
						
	// page group
	Composite outer = (Composite)super.createDialogArea(parent);
	outer.setSize(outer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	GridLayout layout = new GridLayout();
	outer.setLayout(layout);
	outer.setLayoutData(new GridData(GridData.FILL_BOTH));

	// the image & text	
	Composite topContainer = new Composite(outer, SWT.NONE);
	layout = new GridLayout();
	layout.numColumns = (image == null || getItem() == null ? 1 : 2);
	layout.marginWidth = 0;
	topContainer.setLayout(layout);
	GridData data = new GridData();
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessHorizontalSpace = true;
	topContainer.setLayoutData(data);

	//image on left side of dialog
	if (image != null) {
		Label imageLabel = new Label(topContainer, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		data.grabExcessHorizontalSpace = false;
		imageLabel.setLayoutData(data);
		imageLabel.setImage(image);
	}
	
	if (getItem() != null) {
		// text on the right
		text = new StyledText(topContainer, SWT.MULTI | SWT.READ_ONLY);
		text.setCaret(null);
		text.setFont(parent.getFont());
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		data.grabExcessHorizontalSpace = true;
		text.setText(getItem().getText());
		text.setLayoutData(data);
		text.setCursor(null);
		text.setBackground(topContainer.getBackground());
		setLinkRanges(text, getItem().getLinkRanges());
		addListeners(text);
	}

	// horizontal bar
	Label bar =  new Label(outer, SWT.HORIZONTAL | SWT.SEPARATOR);
	data = new GridData();
	data.horizontalAlignment = GridData.FILL;
	bar.setLayoutData(data);
	
	// feature images
	Composite featureContainer = new Composite(outer, SWT.NONE);
	RowLayout rowLayout = new RowLayout();
	rowLayout.wrap = true;
	featureContainer.setLayout(rowLayout);
	data = new GridData();
	data.horizontalAlignment = GridData.FILL;
	featureContainer.setLayoutData(data);
	
	final AboutInfo[] infoArray = getFeaturesInfo();
	for (int i = 0; i < infoArray.length; i++) {
		ImageDescriptor desc = infoArray[i].getFeatureImage();
		Image image = null;
		if (desc != null) {
			Button button = new Button(featureContainer, SWT.FLAT | SWT.PUSH);
			button.setData(infoArray[i]);
			image = desc.createImage();
			images.add(image);
			button.setImage(image);
			String name = infoArray[i].getProviderName();
			if (name == null)
				name = "";
			button.setToolTipText(name);
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					AboutFeaturesDialog d = new AboutFeaturesDialog(getShell());
					d.setInitialSelection((AboutInfo)event.widget.getData());
					d.open();
				}
			});
		}
	}
	
	// spacer
	bar =  new Label(outer, SWT.NONE);
	data = new GridData();
	data.horizontalAlignment = GridData.FILL;
	bar.setLayoutData(data);
	
	return outer;
}


/**
 * Returns the feature infos.
 * They are grouped by provider and image.
 */
private AboutInfo[] getFeaturesInfo() {
	AboutInfo[] rawArray = ((Workbench)PlatformUI.getWorkbench()).getConfigurationInfo().getFeaturesInfo();
	// quickly exclude any that do not have a provider name and image
	ArrayList infoList = new ArrayList();
	for (int i = 0; i < rawArray.length; i++) {
		if (rawArray[i].getProviderName() != null &&
			rawArray[i].getFeatureImageName() != null)
			infoList.add(rawArray[i]); 
	}
	AboutInfo[] infoArray = (AboutInfo[])infoList.toArray(new AboutInfo[infoList.size()]);
	
	// now exclude those with duplicate images
	infoList = new ArrayList();
	for (int i = 0; i < infoArray.length; i++) {
		// check for identical provider
		boolean add = true;
		for (int j = 0; j < infoList.size(); j++) {
			AboutInfo current = (AboutInfo)infoList.get(j);
			if (current.getProviderName().equals(infoArray[i].getProviderName())) {
				// check for identical image
				if (current.getFeatureImageName().equals(infoArray[i].getFeatureImageName())) {
					// same name
					// we have to check if the CRC's are identical
					Long crc1 = current.getFeatureImageCRC();
					Long crc2 = infoArray[i].getFeatureImageCRC();
					if (crc1 == null ? false : crc1.equals(crc2)) {
						// duplicate
						add = false;
						break;
					}
				}
			}
		}
		if (add)
			infoList.add(infoArray[i]);
	}	
	infoList.remove(aboutInfo);
	return (AboutInfo[])infoList.toArray(new AboutInfo[infoList.size()]);
}


/**
 * Answer the product text to show on the right side of the dialog.
 */ 
private String getAboutText() {
	return aboutInfo.getAboutText();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3067.java