error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5334.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5334.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5334.java
text:
```scala
s@@izeItem = new SystemMenuSize(getSelection());

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Cagatay Kavukcuoglu <cagatayk@acm.org>
 *     - Fix for bug 10025 - Resizing views should not use height ratios
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.internal.presentations.PresentablePart;
import org.eclipse.ui.internal.presentations.PresentationFactoryUtil;
import org.eclipse.ui.internal.presentations.SystemMenuDetach;
import org.eclipse.ui.internal.presentations.SystemMenuFastView;
import org.eclipse.ui.internal.presentations.SystemMenuSize;
import org.eclipse.ui.internal.presentations.UpdatingActionContributionItem;
import org.eclipse.ui.presentations.AbstractPresentationFactory;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.StackPresentation;

/**
 * Manages a set of ViewPanes that are docked into the workbench window. The container for a ViewStack
 * is always a PartSashContainer (or null), and its children are always either PartPlaceholders or ViewPanes.
 * This contains the real behavior and state for stacks of views, although the widgets for the tabs are contributed
 * using a StackPresentation.
 * 
 * TODO: eliminate ViewStack and EditorStack. PartStack should be general enough to handle editors 
 * and views without any specialization for editors and views. The differences should be in the 
 * presentation and in the PartPanes themselves.
 * 
 * TODO: eliminate PartPlaceholder. Placeholders should not be children of the ViewStack.
 *  
 */
public class ViewStack extends PartStack {

    private boolean allowStateChanges;

    private WorkbenchPage page;

    private SystemMenuSize sizeItem = new SystemMenuSize(null);

    private SystemMenuFastView fastViewAction;

    private SystemMenuDetach detachViewAction;
    
    public void addSystemActions(IMenuManager menuManager) {
        appendToGroupIfPossible(menuManager,
                "misc", new UpdatingActionContributionItem(fastViewAction)); //$NON-NLS-1$
        appendToGroupIfPossible(menuManager,
        		"misc", new UpdatingActionContributionItem(detachViewAction)); //$NON-NLS-1$
        sizeItem = new SystemMenuSize((PartPane) getSelection());
        appendToGroupIfPossible(menuManager, "size", sizeItem); //$NON-NLS-1$
    }

    public ViewStack(WorkbenchPage page) {
        this(page, true);
    }

    public ViewStack(WorkbenchPage page, boolean allowsStateChanges) {
        this(page, allowsStateChanges, PresentationFactoryUtil.ROLE_VIEW, null);
    }

    public ViewStack(WorkbenchPage page, boolean allowsStateChanges,
            int appearance, AbstractPresentationFactory factory) {
        super(appearance, factory);

        this.page = page;
        setID(this.toString());
        // Each folder has a unique ID so relative positioning is unambiguous.

        this.allowStateChanges = allowsStateChanges;
        fastViewAction = new SystemMenuFastView(getPresentationSite());
        detachViewAction = new SystemMenuDetach(getPresentationSite());
    }

    protected WorkbenchPage getPage() {
        return page;
    }

    protected boolean canMoveFolder() {
        Perspective perspective = page.getActivePerspective();

        if (perspective == null) {
            // Shouldn't happen -- can't have a ViewStack without a
            // perspective
            return false;
        }

        return !perspective.isFixedLayout();
    }

    protected void updateActions(PresentablePart current) {
        ViewPane pane = null;
        
        if (current != null && current.getPane() instanceof ViewPane) {
            pane = (ViewPane) current.getPane();
        }

        fastViewAction.setPane(current);
        detachViewAction.setPane(pane);
        sizeItem.setPane(pane);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartStack#isMoveable(org.eclipse.ui.presentations.IPresentablePart)
     */
    protected boolean isMoveable(IPresentablePart part) {
        ViewPane pane = (ViewPane) getPaneFor(part);
        Perspective perspective = page.getActivePerspective();
        if (perspective == null) {
            // Shouldn't happen -- can't have a ViewStack without a
            // perspective
            return true;
        }
        return perspective.isMoveable(pane.getViewReference());
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartStack#supportsState(int)
     */
    protected boolean supportsState(int newState) {
        if (page.isFixedLayout())
            return false;
        return allowStateChanges;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartStack#derefPart(org.eclipse.ui.internal.LayoutPart)
     */
    protected void derefPart(LayoutPart toDeref) {
        page.getActivePerspective().getPresentation().derefPart(toDeref);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartStack#allowsDrop(org.eclipse.ui.internal.PartPane)
     */
    protected boolean allowsDrop(PartPane part) {
        return part instanceof ViewPane;
    }

    /**
     * Get the presentation for testing purposes.  This is for testing
     * purposes <b>ONLY</b>.
     * 
     * @return the presentation in use for this view stack
     * @since 3.2
     */
    public StackPresentation getTestPresentation() {
    	return getPresentation();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5334.java