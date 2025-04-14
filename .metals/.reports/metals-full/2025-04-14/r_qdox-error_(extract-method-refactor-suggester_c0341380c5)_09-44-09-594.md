error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10002.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10002.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10002.java
text:
```scala
r@@eturn ((ViewStack) container).getSelection();

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
package org.eclipse.ui.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;

/**
 * Represents the top level container.
 */
public class ViewSashContainer extends PartSashContainer {
    public ViewSashContainer(WorkbenchPage page) {
        super("root layout container", page);//$NON-NLS-1$
    }

    /**
     * Gets root container for this part.
     */
    public ViewSashContainer getRootContainer() {
        return this;
    }

    /**
     * Subclasses override this method to specify
     * the composite to use to parent all children
     * layout parts it contains.
     */
    protected Composite createParent(Composite parentWidget) {
        return parentWidget;
    }

    /**
     * Subclasses override this method to dispose
     * of any swt resources created during createParent.
     */
    protected void disposeParent() {
        // do nothing
    }

    /**
     * Get the part control.  This method may return null.
     */
    public Control getControl() {
        return this.parent;
    }

    /**
     * @see IPersistablePart
     */
    public IStatus restoreState(IMemento memento) {
        MultiStatus result = new MultiStatus(
                PlatformUI.PLUGIN_ID,
                IStatus.OK,
                WorkbenchMessages.RootLayoutContainer_problemsRestoringPerspective, null);

        // Read the info elements.
        IMemento[] children = memento.getChildren(IWorkbenchConstants.TAG_INFO);

        // Create a part ID to part hashtable.
        Map mapIDtoPart = new HashMap(children.length);

        // Loop through the info elements.
        for (int i = 0; i < children.length; i++) {
            // Get the info details.
            IMemento childMem = children[i];
            String partID = childMem.getString(IWorkbenchConstants.TAG_PART);
            String relativeID = childMem
                    .getString(IWorkbenchConstants.TAG_RELATIVE);
            int relationship = 0;
            float ratio = 0.0f;
            int left = 0, right = 0;
            if (relativeID != null) {
                relationship = childMem.getInteger(
                        IWorkbenchConstants.TAG_RELATIONSHIP).intValue();

                // Note: the ratio is used for reading pre-3.0 eclipse workspaces. It should be ignored
                // if "left" and "right" are available.
                Float ratioFloat = childMem
                        .getFloat(IWorkbenchConstants.TAG_RATIO);
                Integer leftInt = childMem
                        .getInteger(IWorkbenchConstants.TAG_RATIO_LEFT);
                Integer rightInt = childMem
                        .getInteger(IWorkbenchConstants.TAG_RATIO_RIGHT);
                if (leftInt != null && rightInt != null) {
                    left = leftInt.intValue();
                    right = rightInt.intValue();
                } else {
                    if (ratioFloat != null) {
                        ratio = ratioFloat.floatValue();
                    }
                }
            }
            String strFolder = childMem
                    .getString(IWorkbenchConstants.TAG_FOLDER);

            // Create the part.
            LayoutPart part = null;
            if (strFolder == null)
                part = new PartPlaceholder(partID);
            else {
                ViewStack folder = new ViewStack(page);
                folder.setID(partID);
                result.add(folder.restoreState(childMem
                        .getChild(IWorkbenchConstants.TAG_FOLDER)));
                ContainerPlaceholder placeholder = new ContainerPlaceholder(
                        partID);
                placeholder.setRealContainer(folder);
                part = placeholder;
            }
            // 1FUN70C: ITPUI:WIN - Shouldn't set Container when not active
            part.setContainer(this);

            // Add the part to the layout
            if (relativeID == null) {
                add(part);
            } else {
                LayoutPart refPart = (LayoutPart) mapIDtoPart.get(relativeID);
                if (refPart != null) {
                    if (left != 0)
                        add(part, relationship, left, right, refPart);
                    else
                        add(part, relationship, ratio, refPart);
                } else {
                    WorkbenchPlugin
                            .log("Unable to find part for ID: " + relativeID);//$NON-NLS-1$
                }
            }
            mapIDtoPart.put(partID, part);
        }
        return result;
    }

    /**
     * @see IPersistablePart
     */
    public IStatus saveState(IMemento memento) {
        RelationshipInfo[] relationships = computeRelation();

        MultiStatus result = new MultiStatus(
                PlatformUI.PLUGIN_ID,
                IStatus.OK,
                WorkbenchMessages.RootLayoutContainer_problemsSavingPerspective, null); 

        // Loop through the relationship array.
        for (int i = 0; i < relationships.length; i++) {
            // Save the relationship info ..
            //		private LayoutPart part;
            // 		private int relationship;
            // 		private float ratio;
            // 		private LayoutPart relative;
            RelationshipInfo info = relationships[i];
            IMemento childMem = memento
                    .createChild(IWorkbenchConstants.TAG_INFO);
            childMem.putString(IWorkbenchConstants.TAG_PART, info.part.getID());
            if (info.relative != null) {
                childMem.putString(IWorkbenchConstants.TAG_RELATIVE,
                        info.relative.getID());
                childMem.putInteger(IWorkbenchConstants.TAG_RELATIONSHIP,
                        info.relationship);
                childMem.putInteger(IWorkbenchConstants.TAG_RATIO_LEFT,
                        info.left);
                childMem.putInteger(IWorkbenchConstants.TAG_RATIO_RIGHT,
                        info.right);

                // The ratio is only needed for saving workspaces that can be read by old versions
                // of Eclipse. It is not used in newer versions of Eclipse, which use the "left"
                // and "right" attributes instead.
                childMem.putFloat(IWorkbenchConstants.TAG_RATIO, info
                        .getRatio());
            }

            // Is this part a folder or a placeholder for one?
            ViewStack folder = null;
            if (info.part instanceof ViewStack) {
                folder = (ViewStack) info.part;
            } else if (info.part instanceof ContainerPlaceholder) {
                LayoutPart part = ((ContainerPlaceholder) info.part)
                        .getRealContainer();
                if (part instanceof ViewStack)
                    folder = (ViewStack) part;
            }

            // If this is a folder save the contents.
            if (folder != null) {
                childMem.putString(IWorkbenchConstants.TAG_FOLDER, "true");//$NON-NLS-1$
                IMemento folderMem = childMem
                        .createChild(IWorkbenchConstants.TAG_FOLDER);
                result.add(folder.saveState(folderMem));
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#getDockingRatio(org.eclipse.ui.internal.LayoutPart, org.eclipse.ui.internal.LayoutPart)
     */
    protected float getDockingRatio(LayoutPart dragged, LayoutPart target) {
        if (isStackType(target)) {
            return super.getDockingRatio(dragged, target);
        } else {
            return 0.25f;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#isStackType(org.eclipse.ui.internal.LayoutPart)
     */
    public boolean isStackType(LayoutPart toTest) {
        return (toTest instanceof ViewStack);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#isPaneType(org.eclipse.ui.internal.LayoutPart)
     */
    public boolean isPaneType(LayoutPart toTest) {
        return (toTest instanceof ViewPane);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#createStack(org.eclipse.ui.internal.LayoutPart)
     */
    protected PartStack createStack() {
        ViewStack result = new ViewStack(page);
        return result;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#setVisiblePart(org.eclipse.ui.internal.ILayoutContainer, org.eclipse.ui.internal.LayoutPart)
     */
    protected void setVisiblePart(ILayoutContainer container,
            LayoutPart visiblePart) {
        if (container instanceof ViewStack) {
            ViewStack tabFolder = (ViewStack) container;

            tabFolder.setSelection(visiblePart);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#getVisiblePart(org.eclipse.ui.internal.ILayoutContainer)
     */
    protected LayoutPart getVisiblePart(ILayoutContainer container) {
        return ((ViewStack) container).getVisiblePart();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#derefPart(org.eclipse.ui.internal.LayoutPart)
     */
    protected void derefPart(LayoutPart sourcePart) {
        page.getActivePerspective().getPresentation().derefPart(sourcePart);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#addChild(org.eclipse.ui.internal.PartSashContainer.RelationshipInfo)
     */
    protected void addChild(RelationshipInfo info) {
        LayoutPart child = info.part;

        // Nasty hack: ensure that all views end up inside a tab folder.
        // Since the view title is provided by the tab folder, this ensures
        // that views don't get created without a title tab.
        if (child instanceof ViewPane) {
            ViewStack folder = new ViewStack(page);
            folder.add(child);
            info.part = folder;
        }

        super.addChild(info);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.ILayoutContainer#replace(org.eclipse.ui.internal.LayoutPart, org.eclipse.ui.internal.LayoutPart)
     */
    public void replace(LayoutPart oldChild, LayoutPart newChild) {
        if (!isChild(oldChild)) {
            return;
        }

        // Nasty hack: ensure that all views end up inside a tab folder.
        // Since the view title is provided by the tab folder, this ensures
        // that views don't get created without a title tab.
        if (newChild instanceof ViewPane) {
            ViewStack folder = new ViewStack(page);
            folder.add(newChild);
            newChild = folder;
        }

        super.replace(oldChild, newChild);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10002.java