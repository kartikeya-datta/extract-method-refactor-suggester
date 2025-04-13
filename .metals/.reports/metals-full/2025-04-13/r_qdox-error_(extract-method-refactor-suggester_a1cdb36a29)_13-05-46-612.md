error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8014.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8014.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8014.java
text:
```scala
t@@his(PREFERENCESEXPORTPAGE1);

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.wizards.preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferenceFilter;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.preferences.PreferenceTransferElement;

/**
 * Page 1 of the base preference export Wizard
 * 
 * @since 3.1
 */
public class WizardPreferencesExportPage1 extends WizardPreferencesPage  {

	// constants
	private static final String PREFERENCESEXPORTPAGE1 = "preferencesExportPage1"; // //$NON-NLS-1$

	/**
	 * Create an instance of this class
	 */
	protected WizardPreferencesExportPage1(String name) {
		super(name);
		setTitle(PreferencesMessages.WizardPreferencesExportPage1_exportTitle);
		setDescription(PreferencesMessages.WizardPreferencesExportPage1_exportDescription);
	}

	/**
	 * Create an instance of this class
	 */
	public WizardPreferencesExportPage1() {
		this(PREFERENCESEXPORTPAGE1);//$NON-NLS-1$
	}

	protected String getOutputSuffix() {
    	return ".epf"; //$NON-NLS-1$
    }
	
	/**
	 * Answer the contents of self's destination specification widget
	 * 
	 * @return java.lang.String
	 */
	protected String getDestinationValue() {
		String idealSuffix = getOutputSuffix();
        String destinationText = super.getDestinationValue();

        // only append a suffix if the destination doesn't already have a . in 
        // its last path segment.  
        // Also prevent the user from selecting a directory.  Allowing this will 
        // create a ".epf" file in the directory
        if (destinationText.length() != 0
                && !destinationText.endsWith(File.separator)) {
            int dotIndex = destinationText.lastIndexOf('.');
            if (dotIndex != -1) {
                // the last path seperator index
                int pathSepIndex = destinationText.lastIndexOf(File.separator);
                if (pathSepIndex != -1 && dotIndex < pathSepIndex) {
                    destinationText += idealSuffix;
                }
            } else {
                destinationText += idealSuffix;
            }
        }

        return destinationText;
    }

		
	protected String getAllButtonText() {
		return PreferencesMessages.WizardPreferencesExportPage1_all;
	}

	protected String getChooseButtonText() {
		return PreferencesMessages.WizardPreferencesExportPage1_choose;
	}

	/**
	 * @param composite
	 */
	protected void createTransferArea(Composite composite) {
		createTransfersList(composite);
		createDestinationGroup(composite);
		createOptionsGroup(composite);
	}

	/**
	 * Answer the string to display in self as the destination type
	 * 
	 * @return java.lang.String
	 */
	protected String getDestinationLabel() {
		return PreferencesMessages.WizardPreferencesExportPage1_file;
	}

	/*
	 * return the PreferenceTransgerElements specified
	 */
	protected PreferenceTransferElement[] getTransfers() {
		PreferenceTransferElement[] elements = super.getTransfers();
		PreferenceTransferElement[] returnElements = new PreferenceTransferElement[elements.length];
		IPreferenceFilter[] filters = new IPreferenceFilter[1];
		IPreferenceFilter[] matches;
		IPreferencesService service = Platform.getPreferencesService();
		int count = 0;
		try {
			for (int i = 0; i < elements.length; i++) {
				PreferenceTransferElement element = elements[i];
				filters[0] = element.getFilter();
				matches = service.matches((IEclipsePreferences) service
						.getRootNode().node("instance"), filters); //$NON-NLS-1$
				if (matches.length > 0)
					returnElements[count++] = element;
			}
			elements = new PreferenceTransferElement[count];
			System.arraycopy(returnElements, 0, elements, 0, count);
		} catch (CoreException e) {
			WorkbenchPlugin.log(e.getMessage(), e);
			return new PreferenceTransferElement[0];
		}
		return elements;
	}

	/**
	 * @param transfers
	 * @return <code>true</code> if the transfer was succesful, and
	 *         <code>false</code> otherwise
	 */
	protected boolean transfer(IPreferenceFilter[] transfers) {
		File exportFile = new File(getDestinationValue());
		if (!ensureTargetIsValid(exportFile)) {
			return false;
		}
		FileOutputStream fos = null;
		try {
			if (transfers.length > 0) {
				try {
					fos = new FileOutputStream(exportFile);
				} catch (FileNotFoundException e) {
					WorkbenchPlugin.log(e.getMessage(), e);
					MessageDialog.openError(getControl().getShell(), new String(), e.getLocalizedMessage());
					return false;
				}
				IPreferencesService service = Platform.getPreferencesService();
				try {
					service.exportPreferences(service.getRootNode(), transfers,
							fos);
				} catch (CoreException e) {
					WorkbenchPlugin.log(e.getMessage(), e);
					MessageDialog.openError(getControl().getShell(), new String(), e.getLocalizedMessage());
					return false;
				}
			}
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					WorkbenchPlugin.log(e.getMessage(), e);
					MessageDialog.openError(getControl().getShell(), new String(), e.getLocalizedMessage());
					return false;
				}
		}
		return true;
	}

	protected String getFileDialogTitle() {
		return PreferencesMessages.WizardPreferencesExportPage1_title;
	}

	protected int getFileDialogStyle() {
		return SWT.SAVE;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.wizards.preferences.WizardPreferencesPage#getInvalidDestinationMessage()
	 */
	protected String getInvalidDestinationMessage() {
		return PreferencesMessages.WizardPreferencesExportPage1_noPrefFile;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8014.java