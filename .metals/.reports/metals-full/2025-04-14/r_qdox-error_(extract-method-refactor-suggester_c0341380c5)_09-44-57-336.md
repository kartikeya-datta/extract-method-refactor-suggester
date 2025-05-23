error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9010.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9010.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9010.java
text:
```scala
 D@@ND.DROP_COPY | DND.DROP_LINK);

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.presentations.PresentationSerializer;
import org.eclipse.ui.presentations.StackPresentation;

/**
 * Represents the area set aside for editor workbooks.
 * This container only accepts EditorStack and PartSash
 * as layout parts.
 *
 * Note no views are allowed within this container.
 */
public class EditorSashContainer extends PartSashContainer {

    private static final String DEFAULT_WORKBOOK_ID = "DefaultEditorWorkbook";//$NON-NLS-1$

    private ArrayList editorWorkbooks = new ArrayList(3);

    private EditorStack activeEditorWorkbook;

    private DropTarget dropTarget;

    public EditorSashContainer(String editorId, WorkbenchPage page, Composite parent) {
        super(editorId, page, parent);

        createDefaultWorkbook();
    }

    
    /**
     * Add an editor to the active workbook.
     */
    public void addEditor(EditorPane pane, EditorStack stack) {
        //EditorStack workbook = getActiveWorkbook();
        stack.add(pane);
    }

    /**
     * Notification that a child layout part has been
     * added to the container. Subclasses may override
     * this method to perform any container specific
     * work.
     */
    protected void childAdded(LayoutPart child) {
    	super.childAdded(child);
    	
        if (child instanceof EditorStack)
            editorWorkbooks.add(child);
    }

    /**
     * Notification that a child layout part has been
     * removed from the container. Subclasses may override
     * this method to perform any container specific
     * work.
     */
    protected void childRemoved(LayoutPart child) {
    	super.childRemoved(child);
    	
        if (child instanceof EditorStack) {
            editorWorkbooks.remove(child);
            if (activeEditorWorkbook == child)
                setActiveWorkbook(null, false);
        }
    }

    protected EditorStack createDefaultWorkbook() {
        EditorStack newWorkbook = EditorStack.newEditorWorkbook(this, page);
        newWorkbook.setID(DEFAULT_WORKBOOK_ID);
        add(newWorkbook);
        return newWorkbook;
    }

    /**
     * Subclasses override this method to specify
     * the composite to use to parent all children
     * layout parts it contains.
     */
    protected Composite createParent(Composite parentWidget) {
        return new Composite(parentWidget, SWT.NONE);
    }

    /**
     * Dispose of the editor area.
     */
    public void dispose() {
        // Free editor workbooks.
        editorWorkbooks.clear();

        // Free rest.
        super.dispose();
    }

    /**
     * Subclasses override this method to dispose
     * of any swt resources created during createParent.
     */
    protected void disposeParent() {
        this.parent.dispose();
    }

    /**
     * Return the editor workbook which is active.
     */
    public EditorStack getActiveWorkbook() {
        if (activeEditorWorkbook == null) {
            if (editorWorkbooks.size() < 1)
                setActiveWorkbook(createDefaultWorkbook(), false);
            else
                setActiveWorkbook((EditorStack) editorWorkbooks.get(0), false);
        }

        return activeEditorWorkbook;
    }

    /**
     * Return the editor workbook id which is active.
     */
    public String getActiveWorkbookID() {
        return getActiveWorkbook().getID();
    }

    /**
     * Return the all the editor workbooks.
     */
    public ArrayList getEditorWorkbooks() {
        return (ArrayList) editorWorkbooks.clone();
    }

    /**
     * Return the all the editor workbooks.
     */
    public int getEditorWorkbookCount() {
        return editorWorkbooks.size();
    }

    /**
     * Return true is the workbook specified
     * is the active one.
     */
    protected boolean isActiveWorkbook(EditorStack workbook) {
        return activeEditorWorkbook == workbook;
    }

    /**
     * Find the sashs around the specified part.
     */
    public void findSashes(LayoutPart pane, PartPane.Sashes sashes) {
        //Find the sashes around the current editor and
        //then the sashes around the editor area.
        super.findSashes(pane, sashes);

        ILayoutContainer container = getContainer();
        if (container != null) {
            container.findSashes(this, sashes);
        }
    }

    /**
     * Remove all the editors
     */
    public void removeAllEditors() {
        EditorStack currentWorkbook = getActiveWorkbook();

        // Iterate over a copy so the original can be modified.	
        Iterator workbooks = ((ArrayList) editorWorkbooks.clone()).iterator();
        while (workbooks.hasNext()) {
            EditorStack workbook = (EditorStack) workbooks.next();
            workbook.removeAll();
            if (workbook != currentWorkbook) {
                remove(workbook);
                workbook.dispose();
            }
        }
    }

    /**
     * Remove an editor from its' workbook.
     */
    public void removeEditor(EditorPane pane) {
        EditorStack workbook = pane.getWorkbook();
        if (workbook == null)
            return;
        workbook.remove(pane);

        // remove the editor workbook if empty
        if (workbook.getItemCount() < 1 /* && editorWorkbooks.size() > 1*/) {
            remove(workbook);
            workbook.dispose();
        }
    }

    /**
     * @see IPersistablePart
     */
    public IStatus restoreState(IMemento memento) {
        MultiStatus result = new MultiStatus(
                PlatformUI.PLUGIN_ID,
                IStatus.OK,
                WorkbenchMessages.RootLayoutContainer_problemsRestoringPerspective, null);

        // Remove the default editor workbook that is
        // initialy created with the editor area.
        if (children != null) {
            EditorStack defaultWorkbook = null;
            for (int i = 0; i < children.size(); i++) {
                LayoutPart child = (LayoutPart) children.get(i);
                if (child.getID() == DEFAULT_WORKBOOK_ID) {
                    defaultWorkbook = (EditorStack) child;
                    if (defaultWorkbook.getItemCount() > 0)
                        defaultWorkbook = null;
                }
            }
            if (defaultWorkbook != null)
                remove(defaultWorkbook);
        }

        // Restore the relationship/layout
        IMemento[] infos = memento.getChildren(IWorkbenchConstants.TAG_INFO);
        Map mapIDtoPart = new HashMap(infos.length);

        for (int i = 0; i < infos.length; i++) {
            // Get the info details.
            IMemento childMem = infos[i];
            String partID = childMem.getString(IWorkbenchConstants.TAG_PART);
            String relativeID = childMem
                    .getString(IWorkbenchConstants.TAG_RELATIVE);
            int relationship = 0;
            int left = 0, right = 0;
            float ratio = 0.5f;
            if (relativeID != null) {
                relationship = childMem.getInteger(
                        IWorkbenchConstants.TAG_RELATIONSHIP).intValue();
                Float ratioFloat = childMem
                        .getFloat(IWorkbenchConstants.TAG_RATIO);
                Integer leftInt = childMem
                        .getInteger(IWorkbenchConstants.TAG_RATIO_LEFT);
                Integer rightInt = childMem
                        .getInteger(IWorkbenchConstants.TAG_RATIO_RIGHT);
                if (leftInt != null && rightInt != null) {
                    left = leftInt.intValue();
                    right = rightInt.intValue();
                } else if (ratioFloat != null) {
                    ratio = ratioFloat.floatValue();
                }
            }

            // Create the part.
            EditorStack workbook = EditorStack.newEditorWorkbook(this, page);
            workbook.setID(partID);
            // 1FUN70C: ITPUI:WIN - Shouldn't set Container when not active
            workbook.setContainer(this);

            IMemento workbookMemento = childMem
                    .getChild(IWorkbenchConstants.TAG_FOLDER);
            if (workbookMemento != null) {
                result.add(workbook.restoreState(workbookMemento));
            }

            // Add the part to the layout
            if (relativeID == null) {
                add(workbook);
            } else {
                LayoutPart refPart = (LayoutPart) mapIDtoPart.get(relativeID);
                if (refPart != null) {
                    //$TODO pass in left and right
                    if (left == 0 || right == 0)
                        add(workbook, relationship, ratio, refPart);
                    else
                        add(workbook, relationship, left, right, refPart);
                } else {
                    WorkbenchPlugin
                            .log("Unable to find part for ID: " + relativeID);//$NON-NLS-1$
                }
            }
            mapIDtoPart.put(partID, workbook);
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

            EditorStack stack = (EditorStack) info.part;
            if (stack != null) {
                IMemento folderMem = childMem
                        .createChild(IWorkbenchConstants.TAG_FOLDER);
                result.add(stack.saveState(folderMem));
            }

            if (info.relative != null) {
                childMem.putString(IWorkbenchConstants.TAG_RELATIVE,
                        info.relative.getID());
                childMem.putInteger(IWorkbenchConstants.TAG_RELATIONSHIP,
                        info.relationship);
                childMem.putInteger(IWorkbenchConstants.TAG_RATIO_LEFT,
                        info.left);
                childMem.putInteger(IWorkbenchConstants.TAG_RATIO_RIGHT,
                        info.right);
                // Note: "ratio" is not used in newer versions of Eclipse, which use "left" 
                // and "right" (above) instead
                childMem.putFloat(IWorkbenchConstants.TAG_RATIO, info
                        .getRatio());
            }
        }
        return result;
    }

    /**
     * Set the editor workbook which is active.
     */
    public void setActiveWorkbook(EditorStack newWorkbook, boolean hasFocus) {
        if (newWorkbook != null) {
            if (newWorkbook.isDisposed())
                return;
            if (!editorWorkbooks.contains(newWorkbook))
                return;
        }
        EditorStack oldWorkbook = activeEditorWorkbook;
        activeEditorWorkbook = newWorkbook;

        if (oldWorkbook != null && oldWorkbook != newWorkbook) {
            oldWorkbook.setActive(StackPresentation.AS_INACTIVE);
        }

        if (newWorkbook != null) {
            if (hasFocus) {
                newWorkbook.setActive(StackPresentation.AS_ACTIVE_FOCUS);
            } else {
                newWorkbook.setActive(StackPresentation.AS_ACTIVE_NOFOCUS);
            }
        }

        updateTabList();
    }

    /**
     * Set the editor workbook which is active.
     */
    public void setActiveWorkbookFromID(String id) {
        for (int i = 0; i < editorWorkbooks.size(); i++) {
            EditorStack workbook = (EditorStack) editorWorkbooks.get(i);
            if (workbook.getID().equals(id))
                setActiveWorkbook(workbook, false);
        }
    }
    
    public EditorStack getWorkbookFromID(String id) {
        for (int i = 0; i < editorWorkbooks.size(); i++) {
            EditorStack workbook = (EditorStack) editorWorkbooks.get(i);
            if (workbook.getID().equals(id)) {
                return workbook;
            }
        }
        
        return null;
    }

    /**
     * Updates the editor area's tab list to include the active
     * editor and its tab.
     */
    public void updateTabList() {
        Composite parent = getParent();
        if (parent != null) { // parent may be null on startup
            EditorStack wb = getActiveWorkbook();
            if (wb == null) {
                parent.setTabList(new Control[0]);
            } else {
                parent.setTabList(wb.getTabList());
            }
        }
    }

    /**
     * @see org.eclipse.ui.internal.LayoutPart#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        super.createControl(parent);

        //let the user drop files/editor input on the editor area
        addDropSupport();
    }

    private void addDropSupport() {
        if (dropTarget == null) {
            WorkbenchWindowConfigurer winConfigurer = ((WorkbenchWindow) page
                    .getWorkbenchWindow()).getWindowConfigurer();

            dropTarget = new DropTarget(getControl(), DND.DROP_DEFAULT
 DND.DROP_COPY);
            dropTarget.setTransfer(winConfigurer.getTransfers());
            if (winConfigurer.getDropTargetListener() != null) {
                dropTarget.addDropListener(winConfigurer
                        .getDropTargetListener());
            }
        }
    }

    /* package */DropTarget getDropTarget() {
        return dropTarget;
    }

    /**
     * @see org.eclipse.ui.internal.LayoutPart#getImportance()
     */
    public boolean isCompressible() {
        //Added for bug 19524
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#isStackType(org.eclipse.ui.internal.LayoutPart)
     */
    public boolean isStackType(LayoutPart toTest) {
        return (toTest instanceof EditorStack);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#isPaneType(org.eclipse.ui.internal.LayoutPart)
     */
    public boolean isPaneType(LayoutPart toTest) {
        return (toTest instanceof EditorPane);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#createStack(org.eclipse.ui.internal.LayoutPart)
     */
    protected PartStack createStack() {
        EditorStack newWorkbook = EditorStack.newEditorWorkbook(this, page);

        return newWorkbook;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#setVisiblePart(org.eclipse.ui.internal.ILayoutContainer, org.eclipse.ui.internal.LayoutPart)
     */
    protected void setVisiblePart(ILayoutContainer container,
            LayoutPart visiblePart) {
        EditorStack refPart = (EditorStack) container;

        refPart.becomeActiveWorkbook(true);
        refPart.setSelection(visiblePart);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#getVisiblePart(org.eclipse.ui.internal.ILayoutContainer)
     */
    protected LayoutPart getVisiblePart(ILayoutContainer container) {
        EditorStack refPart = (EditorStack) container;

        return refPart.getSelection();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartSashContainer#pickPartToZoom()
     */
    public LayoutPart pickPartToZoom() {
        return getActiveWorkbook();
    }
    
    /**
     * Restore the presentation state.  Loop over the workbooks, create the appropriate serializer and pass to the presentation.
     *
     * @param areaMem the memento containing presentation 
     * @return the restoration status
     */
    public IStatus restorePresentationState(IMemento areaMem) {
        for (Iterator i = getEditorWorkbooks().iterator(); i.hasNext();) {
            EditorStack workbook = (EditorStack) i.next();
            IMemento memento = workbook.getSavedPresentationState();
            if (memento == null)
                continue;
            PresentationSerializer serializer = new PresentationSerializer(
                    workbook.getPresentableParts());
            workbook.getPresentation().restoreState(serializer, memento);
        }
        return new Status(IStatus.OK, PlatformUI.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9010.java