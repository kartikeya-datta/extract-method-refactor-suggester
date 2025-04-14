error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9076.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9076.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9076.java
text:
```scala
.@@getData(), forward);

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
package org.eclipse.ui.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * The <code>NavigationHistoryAction</code> moves navigation history 
 * back and forward.
 */
public class NavigationHistoryAction extends PageEventAction {
    private boolean forward;

    private Menu historyMenu;

    private int MAX_HISTORY_LENGTH = 9;

    private class MenuCreator implements IMenuCreator {
        public void dispose() {
            if (historyMenu != null) {
                for (int i = 0; i < historyMenu.getItemCount(); i++) {
                    MenuItem menuItem = historyMenu.getItem(i);
                    menuItem.setData(null);
                }
                historyMenu.dispose();
                historyMenu = null;
            }
        }

        public Menu getMenu(Menu parent) {
            return null;
        }

        public Menu getMenu(Control parent) {
            dispose();
            historyMenu = new Menu(parent);
            IWorkbenchPage page = getWorkbenchWindow().getActivePage();
            if (page == null) {
				return historyMenu;
			}

            final NavigationHistory history = (NavigationHistory) getWorkbenchWindow()
                    .getActivePage().getNavigationHistory();
            NavigationHistoryEntry[] entries;
            if (forward) {
				entries = history.getForwardEntries();
			} else {
				entries = history.getBackwardEntries();
			}
            int entriesCount[] = new int[entries.length];
            for (int i = 0; i < entriesCount.length; i++) {
				entriesCount[i] = 1;
			}
            entries = colapseEntries(entries, entriesCount);
            for (int i = 0; i < entries.length; i++) {
                if (i > MAX_HISTORY_LENGTH) {
					break;
				}
                String text = entries[i].getHistoryText();
                if (text != null) {
                    MenuItem item = new MenuItem(historyMenu, SWT.NONE);
                    item.setData(entries[i]);
                    if (entriesCount[i] > 1) {
						text = NLS.bind(WorkbenchMessages.NavigationHistoryAction_locations,text, new Integer(entriesCount[i]));
					}
                    item.setText(text);
                    item.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent e) {
                            history
                                    .shiftCurrentEntry((NavigationHistoryEntry) e.widget
                                            .getData());
                        }
                    });
                }
            }
            return historyMenu;
        }
    }

    /**
     * Create a new instance of <code>NavigationHistoryAction</code>
     * 
     * @param window the workbench window this action applies to
     * @param forward if this action should move history forward of backward
     */
    public NavigationHistoryAction(IWorkbenchWindow window, boolean forward) {
        super("", window); //$NON-NLS-1$
        ISharedImages sharedImages = window.getWorkbench().getSharedImages();
        if (forward) {
            setText(WorkbenchMessages.NavigationHistoryAction_forward_text);
            setToolTipText(WorkbenchMessages.NavigationHistoryAction_forward_toolTip);
            // @issue missing action id
            window.getWorkbench().getHelpSystem().setHelp(this,
					IWorkbenchHelpContextIds.NAVIGATION_HISTORY_FORWARD);
            setImageDescriptor(sharedImages
                    .getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
            setDisabledImageDescriptor(sharedImages
                    .getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD_DISABLED));
            setActionDefinitionId("org.eclipse.ui.navigate.forwardHistory"); //$NON-NLS-1$
        } else {
            setText(WorkbenchMessages.NavigationHistoryAction_backward_text); 
            setToolTipText(WorkbenchMessages.NavigationHistoryAction_backward_toolTip);
            // @issue missing action id
            window.getWorkbench().getHelpSystem().setHelp(this,
					IWorkbenchHelpContextIds.NAVIGATION_HISTORY_BACKWARD);
            setImageDescriptor(sharedImages
                    .getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
            setDisabledImageDescriptor(sharedImages
                    .getImageDescriptor(ISharedImages.IMG_TOOL_BACK_DISABLED));
            setActionDefinitionId("org.eclipse.ui.navigate.backwardHistory"); //$NON-NLS-1$
        }
        // WorkbenchHelp.setHelp(this, IHelpContextIds.CLOSE_ALL_PAGES_ACTION);
        setEnabled(false);
        this.forward = forward;
        setMenuCreator(new MenuCreator());
    }

    /* (non-Javadoc)
     * Method declared on PageEventAction.
     */
    public void pageClosed(IWorkbenchPage page) {
        super.pageClosed(page);
        setEnabled(false);
    }

    private NavigationHistoryEntry[] colapseEntries(
            NavigationHistoryEntry[] entries, int entriesCount[]) {
        ArrayList allEntries = new ArrayList(Arrays.asList(entries));
        NavigationHistoryEntry previousEntry = null;
        int i = -1;
        for (Iterator iter = allEntries.iterator(); iter.hasNext();) {
            NavigationHistoryEntry entry = (NavigationHistoryEntry) iter.next();
            if (previousEntry != null) {
                String text = previousEntry.getHistoryText();
                if (text != null) {
                    if (text.equals(entry.getHistoryText())
                            && previousEntry.editorInfo == entry.editorInfo) {
                        iter.remove();
                        entriesCount[i]++;
                        continue;
                    }
                }
            }
            previousEntry = entry;
            i++;
        }
        entries = new NavigationHistoryEntry[allEntries.size()];
        return (NavigationHistoryEntry[]) allEntries.toArray(entries);
    }

    /* (non-Javadoc)
     * Method declared on PageEventAction.
     */
    public void pageActivated(IWorkbenchPage page) {
        super.pageActivated(page);
        NavigationHistory nh = (NavigationHistory) page.getNavigationHistory();
        if (forward) {
            nh.setForwardAction(this);
        } else {
            nh.setBackwardAction(this);
        }
    }

    /* (non-Javadoc)
     * Method declared on IAction.
     */
    public void run() {
        if (getWorkbenchWindow() == null) {
            // action has been disposed
            return;
        }
        IWorkbenchPage page = getActivePage();
        if (page != null) {
            NavigationHistory nh = (NavigationHistory) page
                    .getNavigationHistory();
            if (forward) {
                nh.forward();
            } else {
                nh.backward();
            }
        }
    }

    public void update() {
        // Set the enabled state of the action and set the tool tip text.  The tool tip
        // text is set to reflect the item that one will move back/forward to.
        WorkbenchPage page = (WorkbenchPage) getActivePage();
        if (page == null) {
			return;
		}
        NavigationHistory history = (NavigationHistory) page
                .getNavigationHistory();
        NavigationHistoryEntry[] entries;
        if (forward) {
            setEnabled(history.canForward());
            entries = history.getForwardEntries();
            if (entries.length > 0) {
                NavigationHistoryEntry entry = entries[0];
                String text = NLS.bind(WorkbenchMessages.NavigationHistoryAction_forward_toolTipName, entry.getHistoryText() ); 
                setToolTipText(text);
            } else {
                setToolTipText(WorkbenchMessages.NavigationHistoryAction_forward_toolTip);
            }
        } else {
            setEnabled(history.canBackward());
            entries = history.getBackwardEntries();
            if (entries.length > 0) {
                NavigationHistoryEntry entry = entries[0];
                String text = NLS.bind(WorkbenchMessages.NavigationHistoryAction_backward_toolTipName, entry.getHistoryText() ); 
                setToolTipText(text);
            } else {
                setToolTipText(WorkbenchMessages.NavigationHistoryAction_backward_toolTip); 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9076.java