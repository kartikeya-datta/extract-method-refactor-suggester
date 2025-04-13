error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6021.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6021.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6021.java
text:
```scala
W@@orkbenchHelp.setHelp(this, IWorkbenchHelpContextIds.SHOW_IN_ACTION);

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.registry.IViewDescriptor;
import org.eclipse.ui.part.IShowInSource;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;

/**
 * Action for a particular target in the Show In menu.
 */
public class ShowInAction extends Action {
    private IWorkbenchWindow window;

    private IViewDescriptor desc;

    /**
     * Creates a new <code>ShowInAction</code>.
     */
    protected ShowInAction(IWorkbenchWindow window, IViewDescriptor desc) {
        super(desc.getLabel());
        setImageDescriptor(desc.getImageDescriptor());
        WorkbenchHelp.setHelp(this, IHelpContextIds.SHOW_IN_ACTION);
        this.window = window;
        this.desc = desc;
    }

    /**
     * Shows the current context in this action's view.
     */
    public void run() {
        IWorkbenchPage page = window.getActivePage();
        if (page == null) {
            beep();
            return;
        }

        IWorkbenchPart sourcePart = page.getActivePart();
        if (sourcePart == null) {
            beep();
            return;
        }

        ShowInContext context = getContext(sourcePart);
        if (context == null) {
            beep();
            return;
        }

        try {
            IViewPart view = page.showView(desc.getId());
            IShowInTarget target = getShowInTarget(view);
            if (target != null && target.show(context)) {
                // success
            } else {
                beep();
            }
            ((WorkbenchPage) page).performedShowIn(desc.getId()); // TODO: move back up
        } catch (PartInitException e) {
            WorkbenchPlugin.log(
                    "Error showing view in ShowInAction.run", e.getStatus()); //$NON-NLS-1$
        }
    }

    /**
     * Returns the <code>IShowInSource</code> provided by the source part,
     * or <code>null</code> if it does not provide one.
     * 
     * @param sourcePart the source part
     * @return an <code>IShowInSource</code> or <code>null</code>
     */
    private IShowInSource getShowInSource(IWorkbenchPart sourcePart) {
        if (sourcePart instanceof IShowInSource) {
            return (IShowInSource) sourcePart;
        }
        Object o = sourcePart.getAdapter(IShowInSource.class);
        if (o instanceof IShowInSource) {
            return (IShowInSource) o;
        }
        return null;
    }

    /**
     * Returns the <code>IShowInTarget</code> for the given part,
     * or <code>null</code> if it does not provide one.
     * 
     * @param targetPart the target part
     * @return the <code>IShowInTarget</code> or <code>null</code>
     */
    private IShowInTarget getShowInTarget(IWorkbenchPart targetPart) {
        if (targetPart instanceof IShowInTarget) {
            return (IShowInTarget) targetPart;
        }
        Object o = targetPart.getAdapter(IShowInTarget.class);
        if (o instanceof IShowInTarget) {
            return (IShowInTarget) o;
        }
        return null;
    }

    /**
     * Returns the <code>ShowInContext</code> to show in the selected target,
     * or <code>null</code> if there is no valid context to show.
     * <p>
     * This implementation obtains the context from the <code>IShowInSource</code>
     * of the source part (if provided), or, if the source part is an editor,
     * it creates the context from the editor's input and selection.
     * <p>
     * Subclasses may extend or reimplement.
     * 
     * @return the <code>ShowInContext</code> to show or <code>null</code>
     */
    private ShowInContext getContext(IWorkbenchPart sourcePart) {
        IShowInSource source = getShowInSource(sourcePart);
        if (source != null) {
            ShowInContext context = source.getShowInContext();
            if (context != null) {
                return context;
            }
        } else if (sourcePart instanceof IEditorPart) {
            Object input = ((IEditorPart) sourcePart).getEditorInput();
            ISelectionProvider sp = sourcePart.getSite().getSelectionProvider();
            ISelection sel = sp == null ? null : sp.getSelection();
            return new ShowInContext(input, sel);
        }
        return null;
    }

    /**
     * Generates a system beep.
     */
    private void beep() {
        window.getShell().getDisplay().beep();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6021.java