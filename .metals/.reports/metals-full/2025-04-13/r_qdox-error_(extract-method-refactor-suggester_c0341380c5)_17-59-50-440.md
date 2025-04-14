error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9380.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9380.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9380.java
text:
```scala
final S@@howViewDialog dialog = new ShowViewDialog(window,

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.handlers;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.ShowViewDialog;
import org.eclipse.ui.views.IViewDescriptor;

/**
 * Shows the given view. If no view is specified in the parameters, then this
 * opens the view selection dialog.
 * 
 * @since 3.1
 */
public final class ShowViewHandler extends AbstractHandler {

	/**
	 * The name of the parameter providing the view identifier.
	 */
	private static final String PARAMETER_NAME_VIEW_ID = "org.eclipse.ui.views.showView.viewId"; //$NON-NLS-1$
    private boolean makeFast = false;
  
    /**
     * Creates a new ShowViewHandler that will open the view in its default location.
     */
    public ShowViewHandler() {
    }
    
    /**
     * Creates a new ShowViewHandler that will optionally force the view to become
     * a fast view.
     * 
     * @param makeFast if true, the view will be moved to the fast view bar (even if it already
     * exists elsewhere). If false, the view will be shown in its default location. Calling with
     * false is equivalent to using the default constructor.
     */    
    public ShowViewHandler(boolean makeFast) {
        this.makeFast = makeFast;
    }
    
	public final Object execute(final ExecutionEvent event)
			throws ExecutionException {
		// Get the view identifier, if any.
		final Map parameters = event.getParameters();
		final Object value = parameters.get(PARAMETER_NAME_VIEW_ID);
		if (value == null) {
			openOther();
		} else {
            try {
                openView((String) value);
            } catch (PartInitException e) {
                throw new ExecutionException("Part could not be initialized", e); //$NON-NLS-1$
            }
		}

		return null;
	}

	/**
	 * Opens a view selection dialog, allowing the user to chose a view.
	 */
	private final void openOther() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		final IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return;
		}
		
		final ShowViewDialog dialog = new ShowViewDialog(window.getShell(),
				WorkbenchPlugin.getDefault().getViewRegistry());
		dialog.open();
		
		if (dialog.getReturnCode() == Window.CANCEL) {
			return;
		}
		
		final IViewDescriptor[] descriptors = dialog.getSelection();
		for (int i = 0; i < descriptors.length; ++i) {
			try {
                openView(descriptors[i].getId());
			} catch (PartInitException e) {
				ErrorDialog.openError(window.getShell(),
						WorkbenchMessages.ShowView_errorTitle, e.getMessage(),
						e.getStatus());
			}
		}
	}

	/**
	 * Opens the view with the given identifier.
	 * 
	 * @param viewId
	 *            The view to open; must not be <code>null</code>
	 * @throws PartInitException
	 *             If the part could not be initialized.
	 */
	private final void openView(final String viewId) throws PartInitException {
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI
				.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return;
		}

		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		if (activePage == null) {
			return;
		}

        if (makeFast) {
            WorkbenchPage wp = (WorkbenchPage) activePage;
            
            IViewReference ref = wp.findViewReference(viewId);
            
            if (ref == null) {
                IViewPart part = wp.showView(viewId, null, IWorkbenchPage.VIEW_CREATE);
                ref = (IViewReference)wp.getReference(part); 
            }
            
            if (!wp.isFastView(ref)) {
                wp.addFastView(ref);
            }
            wp.activate(ref.getPart(true));
        } else {
            activePage.showView(viewId);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9380.java