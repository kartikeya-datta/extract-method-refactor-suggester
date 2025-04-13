error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7715.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7715.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7715.java
text:
```scala
W@@orkbenchMessages.PropertyDialog_messageTitle,

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * This dialog is created and shown when 'Properties' action is performed while
 * an object is selected. It shows one or more pages registered for object's
 * type.
 */
public class PropertyDialog extends FilteredPreferenceDialog {
    private ISelection selection;

    //The id of the last page that was selected
    private static String lastPropertyId = null;

    
    /**
     * Create a new property dialog.
     * 
     * @param shell the parent shell
     * @param propertyPageId the property page id
     * @param element the adaptable element
     * @return the property dialog
     */
    public static PropertyDialog createDialogOn(Shell shell,final String propertyPageId, IAdaptable element){

		PropertyPageManager pageManager = new PropertyPageManager();
		String title = "";//$NON-NLS-1$
		
		if (element == null)
			return null;
		// load pages for the selection
		// fill the manager with contributions from the matching contributors
		PropertyPageContributorManager.getManager().contribute(pageManager,
				element);
		// testing if there are pages in the manager
		Iterator pages = pageManager.getElements(PreferenceManager.PRE_ORDER)
				.iterator();
		String name = getName(element);
		if (!pages.hasNext()) {
			MessageDialog
					.openInformation(
							shell,
							WorkbenchMessages.PropertyDialog_messageTitle, //$NON-NLS-1$
							NLS.bind(WorkbenchMessages.PropertyDialog_noPropertyMessage,  name)); 
			return null;
		}
		title = NLS.bind(WorkbenchMessages.PropertyDialog_propertyMessage,  name ); 
		PropertyDialog propertyDialog = new PropertyDialog(shell, pageManager,
				new StructuredSelection(element));

		if (propertyPageId != null)
			propertyDialog.setSelectedNode(propertyPageId);
		propertyDialog.create();

		propertyDialog.getShell().setText(title);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(
				propertyDialog.getShell(),
				IWorkbenchHelpContextIds.PROPERTY_DIALOG);

		return propertyDialog;
	
    }
    
    /**
	 * Returns the name of the given element.
	 * 
	 * @param element
	 *            the element
	 * @return the name of the element
	 */
	private static String getName(IAdaptable element) {
		IWorkbenchAdapter adapter = (IWorkbenchAdapter) element
				.getAdapter(IWorkbenchAdapter.class);
		if (adapter != null) 
			return adapter.getLabel(element);
		return "";//$NON-NLS-1$
	}
	
    /**
     * Create an instance of the receiver.
     * @param parentShell
     * @param mng
     * @param selection
     */
    public PropertyDialog(Shell parentShell, PreferenceManager mng,
            ISelection selection) {
        super(parentShell, mng);
        setSelection(selection);
    }

    /**
     * Returns selection in the "Properties" action context.
     * 
     * @return the selection
     */
    public ISelection getSelection() {
        return selection;
    }

    /**
     * Sets the selection that will be used to determine target object.
     * 
     * @param newSelection the new selection
     */
    public void setSelection(ISelection newSelection) {
        selection = newSelection;
    }

    /**
     * Get the name of the selected item preference
     */
    protected String getSelectedNodePreference() {
        return lastPropertyId;
    }

    /**
     * Get the name of the selected item preference
     */
    protected void setSelectedNodePreference(String pageId) {
        lastPropertyId = pageId;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7715.java