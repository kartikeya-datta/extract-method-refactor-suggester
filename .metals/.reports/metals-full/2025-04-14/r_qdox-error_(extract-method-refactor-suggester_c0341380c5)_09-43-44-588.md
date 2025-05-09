error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9808.java
text:
```scala
V@@erification.showVerificationResult(result);

/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.verify;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.ui.utils.IContextHelpIds;

/**
 * <p>Displays the <b>XML Signatures</b> view with all discovered signatures in the
 * current XML document. Shows the properties (status, id, type and algorithm) of every signature
 * and enables a rescan of the opened XML document for new signatures. The top info area contains
 * the number of all discovered signatures and the number of valid, invalid and unknown ones.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class SignatureView extends ViewPart {
    /** TableViewer in the view. */
    private TableViewer viewer;
    /** XML Signatures view ID. */
    public static final String ID = "org.eclipse.wst.xml.security.ui.SignatureView"; //$NON-NLS-1$
    /** The system clipboard. */
    private Clipboard clipboard = null;

    /**
     * Sets the input for the view.
     *
     * @param results Input as ArrayList
     */
    public void setInput(final ArrayList<VerificationResult> results) {
        viewer.setLabelProvider(new SignatureLabelProvider());
        viewer.setInput(results);
        updateViewInfo(results);
    }

    /**
     * Disposes the view, frees all system resources.
     */
    public void dispose() {
        if (clipboard != null) {
            clipboard.dispose();
            clipboard = null;
        }
        super.dispose();
    }

    /**
     * Creates the view layout and content.
     *
     * @param parent The parent composite
     */
    public void createPartControl(final Composite parent) {
        viewer = new TableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setLabelProvider(new SignatureLabelProvider());

        getSite().setSelectionProvider(viewer);

        viewer.getTable().setLinesVisible(true);
        viewer.getTable().setHeaderVisible(true);

        TableViewerColumn column = new TableViewerColumn(viewer, SWT.CENTER);
        column.getColumn().setText(""); //$NON-NLS-1$
        column.getColumn().setToolTipText(Messages.signatureStatus);
        column.getColumn().setWidth(50);
        column.getColumn().setMoveable(true);

        SignatureComparator signatureSorter = new SignatureComparator(viewer, column) {
            protected int doCompare(Viewer viewer, Object o1, Object o2) {
                VerificationResult result1 = (VerificationResult) o1;
                VerificationResult result2 = (VerificationResult) o2;

                if (result1 != null && result2 != null) {
                    return result1.getStatus().compareToIgnoreCase(result2.getStatus());
                } else {
                    return 0;
                }
            }
        };

        column = new TableViewerColumn(viewer, SWT.LEFT);
        column.getColumn().setText(Messages.signatureId);
        column.getColumn().setToolTipText(Messages.signatureId);
        column.getColumn().setWidth(200);
        column.getColumn().setMoveable(true);

        new SignatureComparator(viewer, column) {
            protected int doCompare(Viewer viewer, Object o1, Object o2) {
                VerificationResult result1 = (VerificationResult) o1;
                VerificationResult result2 = (VerificationResult) o2;

                if (result1 != null && result2 != null) {
                    return result1.getId().compareToIgnoreCase(result2.getId());
                } else {
                    return 0;
                }
            }
        };

        column = new TableViewerColumn(viewer, SWT.LEFT);
        column.getColumn().setText(Messages.signatureType);
        column.getColumn().setToolTipText(Messages.signatureType);
        column.getColumn().setWidth(100);
        column.getColumn().setMoveable(true);

        new SignatureComparator(viewer, column) {
            protected int doCompare(Viewer viewer, Object o1, Object o2) {
                VerificationResult result1 = (VerificationResult) o1;
                VerificationResult result2 = (VerificationResult) o2;

                if (result1 != null && result2 != null) {
                    return result1.getType().compareToIgnoreCase(result2.getType());
                } else {
                    return 0;
                }
            }
        };

        column = new TableViewerColumn(viewer, SWT.LEFT);
        column.getColumn().setText(Messages.signatureAlgorithm);
        column.getColumn().setToolTipText(Messages.signatureAlgorithm);
        column.getColumn().setWidth(200);
        column.getColumn().setMoveable(true);

        new SignatureComparator(viewer, column) {
            protected int doCompare(Viewer viewer, Object o1, Object o2) {
                VerificationResult result1 = (VerificationResult) o1;
                VerificationResult result2 = (VerificationResult) o2;

                if (result1 != null && result2 != null) {
                    return result1.getAlgorithm().compareToIgnoreCase(result2.getAlgorithm());
                } else {
                    return 0;
                }
            }
        };

        signatureSorter.setSorter(signatureSorter, SignatureComparator.ASC);

        initCopyAndPaste();
        initContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
        updateViewInfo(new ArrayList<VerificationResult>());

        PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), IContextHelpIds.SIGNATURE_VIEW);
    }

    /**
     * Initializes the copy and paste functionality of the XML Signatures View. Verification
     * Results can be copied in a readable String format.
     */
    private void initCopyAndPaste() {
        clipboard = new Clipboard(getSite().getShell().getDisplay());
        IAction copyAction = new Action() {
            public void run() {
                setClipboardData();
            }
        };
        getViewSite().getActionBars().setGlobalActionHandler("copy", copyAction); //$NON-NLS-1$
    }

    /**
     * Copies the selected Verification Result to the clipboard.
     *
     * @return Result was copied to clipboard.
     */
    private boolean setClipboardData() {
        VerificationResult result = getSelectedItem();

        if (result == null) {
            return false;
        }

        String text = result.resultToReadableString();
        TextTransfer textTransfer = TextTransfer.getInstance();
        Transfer[] transfers = new Transfer[] {textTransfer};
        Object[] data = new Object[] {text};
        clipboard.setContents(data, transfers);

        return true;
    }

    /**
     * Determines the selected Verification Result object in the view.
     *
     * @return The selected Verification Result
     */
    private VerificationResult getSelectedItem() {
        ISelection selection = viewer.getSelection();

        if (selection != null && selection instanceof IStructuredSelection) {
            Object o = ((IStructuredSelection) selection).getFirstElement();

            if (o instanceof VerificationResult) {
                return (VerificationResult) o;
            }
        }

        return null;
    }

    /**
     * Context menu available in the view.
     */
    private void initContextMenu() {
        MenuManager manager = new MenuManager();
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        Menu menu = manager.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(manager, viewer);
    }

    /**
     * Adds actions (and icons) to the action bar of the view.
     */
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        bars.getMenuManager().add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        bars.getToolBarManager().add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * Double click on an entry in the view. Shows a dialog with additional signature properties.
     */
    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(final DoubleClickEvent event) {
                VerificationResult result = getSelectedItem();

                if (result != null) {
                    Verification.showVerificationResult(result, getSite().getShell());
                }
            }
        });
    }

    /**
     * Passes the focus request to the viewer's control.
     */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    /**
     * Updates the info text of the view showing how many total signatures are
     * available in the XML document and how many of them are valid, invalid and
     * unknown.
     *
     * @param results Input as ArrayList
     */
    private void updateViewInfo(final ArrayList<VerificationResult> results) {
        int totalSignatures = 0;
        int validSignatures = 0;
        int invalidSignatures = 0;
        int unknownSignatures = 0;

        try {
            totalSignatures = results.size();
            for (VerificationResult result : results) {
                if (VerificationResult.VALID.equals(result.getStatus())) {
                    validSignatures++;
                } else if (VerificationResult.INVALID.equals(result.getStatus())) {
                    invalidSignatures++;
                } else {
                    unknownSignatures++;
                }
            }
        } catch (Exception ex) {
            totalSignatures = 0;
            validSignatures = 0;
            invalidSignatures = 0;
            unknownSignatures = 0;
        }

        setContentDescription(NLS.bind(Messages.signatureInfo,
            new Object[] {totalSignatures, validSignatures, invalidSignatures, unknownSignatures}));
    }

    /**
     * Returns the table viewer in the Signatures view.
     *
     * @return the table viewer in the Signatures view
     */
    public TableViewer getSignaturesViewer() {
        return viewer;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9808.java