error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7289.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7289.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7289.java
text:
```scala
W@@orkbenchPlugin.log(getClass(), "fill", e); //$NON-NLS-1$

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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

/**
 * A dynamic menu item which supports to switch to other Windows.
 */
public class ReopenEditorMenu extends ContributionItem {
    private IWorkbenchWindow window;

    private EditorHistory history;

    private boolean showSeparator;

    private boolean dirty = true;

    private IMenuListener menuListener = new IMenuListener() {
        public void menuAboutToShow(IMenuManager manager) {
            manager.markDirty();
            dirty = true;
        }
    };

    // the maximum length for a file name; must be >= 4
    private static final int MAX_TEXT_LENGTH = 40;

    // only assign mnemonic to the first nine items 
    private static final int MAX_MNEMONIC_SIZE = 9;

    /**
     * Create a new instance.
     */
    public ReopenEditorMenu(IWorkbenchWindow window, String id,
            boolean showSeparator) {
        super(id);
        this.window = window;
        this.showSeparator = showSeparator;
        history = ((Workbench) window.getWorkbench()).getEditorHistory();
    }

    /**
     * Returns the text for a history item.  This may be truncated to fit
     * within the MAX_TEXT_LENGTH.
     */
    private String calcText(int index, EditorHistoryItem item) {
        StringBuffer sb = new StringBuffer();

        int mnemonic = index + 1;
        sb.append(mnemonic);
        if (mnemonic <= MAX_MNEMONIC_SIZE) {
            sb.insert(sb.length() - (mnemonic + "").length(), '&'); //$NON-NLS-1$
        }
        sb.append(" "); //$NON-NLS-1$

        // IMPORTANT: avoid accessing the item's input since
        // this can require activating plugins.
        // Instead, ask the item for the info, which can
        // consult its memento if it is not restored yet.
        String fileName = item.getName();
        String pathName = item.getToolTipText();
        if (pathName.equals(fileName)) {
            // tool tip text isn't necessarily a path;
            // sometimes it's the same as name, so it shouldn't be treated as a path then
            pathName = ""; //$NON-NLS-1$
        }
        IPath path = new Path(pathName);
        // if last segment in path is the fileName, remove it 
        if (path.segmentCount() > 1
                && path.segment(path.segmentCount() - 1).equals(fileName)) {
            path = path.removeLastSegments(1);
            pathName = path.toString();
        }

        if ((fileName.length() + pathName.length()) <= (MAX_TEXT_LENGTH - 4)) {
            // entire item name fits within maximum length
            sb.append(fileName);
            if (pathName.length() > 0) {
                sb.append("  ["); //$NON-NLS-1$
                sb.append(pathName);
                sb.append("]"); //$NON-NLS-1$
            }
        } else {
            // need to shorten the item name
            int length = fileName.length();
            if (length > MAX_TEXT_LENGTH) {
                // file name does not fit within length, truncate it
                sb.append(fileName.substring(0, MAX_TEXT_LENGTH - 3));
                sb.append("..."); //$NON-NLS-1$
            } else if (length > MAX_TEXT_LENGTH - 7) {
                sb.append(fileName);
            } else {
                sb.append(fileName);
                int segmentCount = path.segmentCount();
                if (segmentCount > 0) {
                    length += 7; // 7 chars are taken for "  [...]"

                    sb.append("  ["); //$NON-NLS-1$

                    // Add first n segments that fit
                    int i = 0;
                    while (i < segmentCount && length < MAX_TEXT_LENGTH) {
                        String segment = path.segment(i);
                        if (length + segment.length() < MAX_TEXT_LENGTH) {
                            sb.append(segment);
                            sb.append(IPath.SEPARATOR);
                            length += segment.length() + 1;
                            i++;
                        } else if (i == 0) {
                            // append at least part of the first segment
                            sb.append(segment.substring(0, MAX_TEXT_LENGTH
                                    - length));
                            length = MAX_TEXT_LENGTH;
                            break;
                        } else {
                            break;
                        }
                    }

                    sb.append("..."); //$NON-NLS-1$

                    i = segmentCount - 1;
                    // Add last n segments that fit
                    while (i > 0 && length < MAX_TEXT_LENGTH) {
                        String segment = path.segment(i);
                        if (length + segment.length() < MAX_TEXT_LENGTH) {
                            sb.append(IPath.SEPARATOR);
                            sb.append(segment);
                            length += segment.length() + 1;
                            i--;
                        } else {
                            break;
                        }
                    }

                    sb.append("]"); //$NON-NLS-1$
                }
            }
        }
        return sb.toString();
    }

    /**
     * Fills the given menu with
     * menu items for all windows.
     */
    public void fill(final Menu menu, int index) {
        if (window.getActivePage() == null
 window.getActivePage().getPerspective() == null) {
            return;
        }

        if (getParent() instanceof MenuManager) {
            ((MenuManager) getParent()).addMenuListener(menuListener);
        }

        int itemsToShow = WorkbenchPlugin.getDefault().getPreferenceStore()
                .getInt(IPreferenceConstants.RECENT_FILES);
        if (itemsToShow == 0) {
            return;
        }

        // Get items.
        EditorHistoryItem[] historyItems = history.getItems();

        int n = Math.min(itemsToShow, historyItems.length);
        if (n <= 0) {
            return;
        }

        if (showSeparator) {
            new MenuItem(menu, SWT.SEPARATOR, index);
            ++index;
        }

        final int menuIndex[] = new int[] { index };

        for (int i = 0; i < n; i++) {
            final EditorHistoryItem item = historyItems[i];
            final int historyIndex = i;
            Platform.run(new SafeRunnable() {
                public void run() throws Exception {
                    String text = calcText(historyIndex, item);
                    MenuItem mi = new MenuItem(menu, SWT.PUSH, menuIndex[0]);
                    ++menuIndex[0];
                    mi.setText(text);
                    mi.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent e) {
                            open(item);
                        }
                    });
                }

                public void handleException(Throwable e) {
                    // just skip the item if there's an error,
                    // e.g. in the calculation of the shortened name
                    WorkbenchPlugin.log("Error in ReopenEditorMenu.fill: " + e); //$NON-NLS-1$
                }
            });
        }
        dirty = false;
    }

    /**
     * Overridden to always return true and force dynamic menu building.
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Overridden to always return true and force dynamic menu building.
     */
    public boolean isDynamic() {
        return true;
    }

    /**
     * Reopens the editor for the given history item.
     */
    private void open(EditorHistoryItem item) {
        IWorkbenchPage page = window.getActivePage();
        if (page != null) {
            try {
                String itemName = item.getName();
                if (!item.isRestored()) {
                    item.restoreState();
                }
                IEditorInput input = item.getInput();
                IEditorDescriptor desc = item.getDescriptor();
                if (input == null || desc == null) {
                    String title = WorkbenchMessages
                            .getString("OpenRecent.errorTitle"); //$NON-NLS-1$
                    String msg = WorkbenchMessages
                            .format(
                                    "OpenRecent.unableToOpen", new String[] { itemName }); //$NON-NLS-1$
                    MessageDialog.openWarning(window.getShell(), title, msg);
                    history.remove(item);
                } else {
                    page.openEditor(input, desc.getId());
                }
            } catch (PartInitException e2) {
                String title = WorkbenchMessages
                        .getString("OpenRecent.errorTitle"); //$NON-NLS-1$
                MessageDialog.openWarning(window.getShell(), title, e2
                        .getMessage());
                history.remove(item);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7289.java