error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2399.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2399.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2399.java
text:
```scala
.@@createToolBarContributionItem((ToolBarManager) coolItemToolBarMgr);

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

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IContributionManagerOverrides;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.SubContributionManager;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;

import org.eclipse.ui.IActionBars2;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.SubActionBars2;
import org.eclipse.ui.actions.RetargetAction;

/**
 * The action bars for an editor.
 */
public class EditorActionBars extends SubActionBars2 {

    private class Overrides implements IContributionManagerOverrides {

        public Integer getAccelerator(IContributionItem item) {
            return null;
        }

        public String getAcceleratorText(IContributionItem item) {
            return null;
        }

        public Boolean getEnabled(IContributionItem item) {
            if (((item instanceof ActionContributionItem) && (((ActionContributionItem) item)
                    .getAction() instanceof RetargetAction))
 enabledAllowed)
                return null;
            else
                return Boolean.FALSE;
        }

        public String getText(IContributionItem item) {
            return null;
        }
    }

    private IToolBarManager coolItemToolBarMgr = null;

    private IEditorActionBarContributor editorContributor;

    private boolean enabledAllowed = true;

    private IEditorActionBarContributor extensionContributor;

    private int refCount;

    private ToolBarContributionItem toolBarContributionItem = null;

    private String type;

    /**
     * Constructs the EditorActionBars for an editor.
     */
    public EditorActionBars(IActionBars2 parent, String type) {
        super(parent);
        this.type = type;
    }

    /**
     * Activate the contributions.
     */
    public void activate(boolean forceVisibility) {
        setActive(true, forceVisibility);
    }

    /**
     * Add one ref to the bars.
     */
    public void addRef() {
        ++refCount;
    }

    /*
     * (non-Javadoc) Method declared on SubActionBars.
     */
    protected SubMenuManager createSubMenuManager(IMenuManager parent) {
        return new EditorMenuManager(parent);
    }

    /*
     * (non-Javadoc) Method declared on SubActionBars.
     */
    protected SubToolBarManager createSubToolBarManager(IToolBarManager parent) {
        // return null, editor actions are managed by CoolItemToolBarManagers
        return null;
    }

    /**
     * Deactivate the contributions.
     */
    public void deactivate(boolean forceVisibility) {
        setActive(false, forceVisibility);
    }

    /**
     * Dispose the contributions.
     */
    public void dispose() {
        super.dispose();
        if (editorContributor != null) editorContributor.dispose();
        if (extensionContributor != null) extensionContributor.dispose();

        /*
         * Dispose of the contribution item, but also make sure that no one
         * else is holding on to it. In this case, go through the
         * SubCoolBarManager to its parent (the real CoolBarManager), and
         * replace the reference with a placeholder.
         */
        if (toolBarContributionItem != null) {
            // Create a placeholder and place it in the cool bar manager.
            ICoolBarManager coolBarManager = getCoolBarManager();
            if (coolBarManager instanceof SubContributionManager) {
                SubContributionManager subManager = (SubContributionManager) coolBarManager;
                IContributionManager manager = subManager.getParent();
                if (manager instanceof ContributionManager) {
                    final IContributionItem replacementItem = new PlaceholderContributionItem(
                            toolBarContributionItem);
                    ((ContributionManager) manager).replaceItem(replacementItem
                            .getId(), replacementItem);
                }
            }

            // Dispose of the replaced item.
            toolBarContributionItem.dispose();
        }
        toolBarContributionItem = null;
        // Remove actions
        if (coolItemToolBarMgr != null) {
            coolItemToolBarMgr.removeAll();
        }
        coolItemToolBarMgr = null;
    }

    /**
     * Gets the editor contributor
     */
    public IEditorActionBarContributor getEditorContributor() {
        return editorContributor;
    }

    /**
     * Returns the editor type.
     */
    public String getEditorType() {
        return type;
    }

    /**
     * Gets the extension contributor
     */
    public IEditorActionBarContributor getExtensionContributor() {
        return extensionContributor;
    }

    /**
     * Returns the reference count.
     */
    public int getRef() {
        return refCount;
    }

    /**
     * Returns the tool bar manager. If items are added or removed from the
     * manager be sure to call <code>updateActionBars</code>. Overridden to
     * support CoolBars.
     * 
     * @return the tool bar manager
     */
    public IToolBarManager getToolBarManager() {

        // by pass the sub coolBar and use the real cool bar.
        ICoolBarManager coolBarManager = getCastedParent().getCoolBarManager();
        if (coolBarManager == null) { return null; }

        if (toolBarContributionItem == null) {
            IContributionItem foundItem = coolBarManager.find(type);
            if ((foundItem instanceof ToolBarContributionItem)) {
                toolBarContributionItem = (ToolBarContributionItem) foundItem;
                coolItemToolBarMgr = toolBarContributionItem
                        .getToolBarManager();
                if (coolItemToolBarMgr == null) {
                    coolItemToolBarMgr = new ToolBarManager(coolBarManager
                            .getStyle());
                    toolBarContributionItem = new ToolBarContributionItem(
                            coolItemToolBarMgr, type);
                    // Add editor item to group
                    coolBarManager.prependToGroup(
                            IWorkbenchActionConstants.GROUP_EDITOR,
                            toolBarContributionItem);
                }
            } else {
                coolItemToolBarMgr = new ToolBarManager(coolBarManager
                        .getStyle());
                if ((coolBarManager instanceof ContributionManager)
                        && (foundItem instanceof PlaceholderContributionItem)) {
                    PlaceholderContributionItem placeholder = (PlaceholderContributionItem) foundItem;
                    toolBarContributionItem = placeholder
                            .createToolBarContributionItem(coolItemToolBarMgr);
                    // Restore from a placeholder
                    ((ContributionManager) coolBarManager).replaceItem(type,
                            toolBarContributionItem);
                } else {
                    toolBarContributionItem = new ToolBarContributionItem(
                            coolItemToolBarMgr, type);
                    // Add editor item to group
                    coolBarManager.prependToGroup(
                            IWorkbenchActionConstants.GROUP_EDITOR,
                            toolBarContributionItem);
                }
            }
            ((ToolBarManager) coolItemToolBarMgr).setOverrides(new Overrides());
            toolBarContributionItem.setVisible(getActive());
            coolItemToolBarMgr.markDirty();
        }

        return coolItemToolBarMgr;
    }

    /**
     * Returns whether the contribution list is visible. If the visibility is
     * <code>true</code> then each item within the manager appears within the
     * parent manager. Otherwise, the items are not visible.
     * 
     * @return <code>true</code> if the manager is visible
     */
    private boolean isVisible() {
        if (toolBarContributionItem != null)
                return toolBarContributionItem.isVisible();
        return false;
    }

    /**
     * Sets the target part for the action bars. For views this is ignored
     * because each view has its own action vector. For editors this is
     * important because the action vector is shared by editors of the same
     * type.
     */
    public void partChanged(IWorkbenchPart part) {
        super.partChanged(part);
        if (part instanceof IEditorPart) {
            IEditorPart editor = (IEditorPart) part;
            if (editorContributor != null)
                    editorContributor.setActiveEditor(editor);
            if (extensionContributor != null)
                    extensionContributor.setActiveEditor(editor);
        }
    }

    /**
     * Remove one ref to the bars.
     */
    public void removeRef() {
        --refCount;
    }

    /**
     * Activate / Deactivate the contributions.
     * 
     * Workaround for flashing when editor contributes many menu/tool
     * contributions. In this case, the force visibility flag determines if the
     * contributions should be actually made visible/hidden or just change the
     * enablement state.
     */
    private void setActive(boolean set, boolean forceVisibility) {
        basicSetActive(set);
        if (isSubMenuManagerCreated())
                ((EditorMenuManager) getMenuManager()).setVisible(set,
                        forceVisibility);

        if (isSubStatusLineManagerCreated())
                ((SubStatusLineManager) getStatusLineManager()).setVisible(set);

        setVisible(set, forceVisibility);
    }

    /**
     * Sets the editor contributor
     */
    public void setEditorContributor(IEditorActionBarContributor c) {
        editorContributor = c;
    }

    /**
     * Sets the enablement ability of all the items contributed by the editor.
     * 
     * @param enabledAllowed
     *            <code>true</code> if the items may enable
     * @since 2.0
     */
    private void setEnabledAllowed(boolean enabledAllowed) {
        if (this.enabledAllowed == enabledAllowed) return;
        this.enabledAllowed = enabledAllowed;
        if (coolItemToolBarMgr != null) {
            IContributionItem[] items = coolItemToolBarMgr.getItems();
            for (int i = 0; i < items.length; i++) {
                IContributionItem item = items[i];
                item.update(IContributionManagerOverrides.P_ENABLED);
            }
        }
    }

    /**
     * Sets the extension contributor
     */
    public void setExtensionContributor(IEditorActionBarContributor c) {
        extensionContributor = c;
    }

    /**
     * Sets the visibility of the manager. If the visibility is <code>true</code>
     * then each item within the manager appears within the parent manager.
     * Otherwise, the items are not visible.
     * 
     * @param visible
     *            the new visibility
     */
    private void setVisible(boolean visible) {
        if (toolBarContributionItem != null) {
            toolBarContributionItem.setVisible(visible);
            if (toolBarContributionItem.getParent() != null) {
                toolBarContributionItem.getParent().markDirty();
            }
        }
    }

    /**
     * Sets the visibility of the manager. If the visibility is <code>true</code>
     * then each item within the manager appears within the parent manager.
     * Otherwise, the items are not visible if force visibility is <code>true</code>,
     * or grayed out if force visibility is <code>false</code>
     * <p>
     * This is a workaround for the layout flashing when editors contribute
     * large amounts of items.
     * </p>
     * 
     * @param visible
     *            the new visibility
     * @param forceVisibility
	 *            <code>true</code> to change the visibility or <code>false</code> 
	 *            to change just the enablement state. This parameter is ignored 
	 *            if visible is <code>true</code>.
     */
    private void setVisible(boolean visible, boolean forceVisibility) {
        if (visible) {
			setEnabledAllowed(true);
            if (!isVisible()) setVisible(true);
        } else {
            if (forceVisibility)
                // Remove the editor tool bar items
                setVisible(false);
            else
                // Disabled the tool bar items.
                setEnabledAllowed(false);
        }

        ICoolBarManager coolBarManager = getCastedParent().getCoolBarManager();
        if ((coolItemToolBarMgr != null) && (coolBarManager != null)) {
            IContributionItem[] items = coolItemToolBarMgr.getItems();
            for (int i = 0; i < items.length; i++) {
                IContributionItem item = items[i];
				item.setVisible(visible || !forceVisibility);
                coolItemToolBarMgr.markDirty();
                if (!coolBarManager.isDirty()) {
                    coolBarManager.markDirty();
                }
            }
            // Update the manager
            coolItemToolBarMgr.update(false);
            if (toolBarContributionItem != null) {
				toolBarContributionItem.setVisible(visible || !forceVisibility);
            }
            coolBarManager.update(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2399.java