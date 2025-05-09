error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6549.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6549.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6549.java
text:
```scala
F@@olderItem item = parent.getConfiguration();

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.tree;

import org.columba.core.xml.XmlElement;

import org.columba.mail.config.FolderItem;
import org.columba.mail.folder.AbstractFolder;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;


/**
 * this class does all the dirty work for the TreeController
 */
public class TreeView extends JTree {
    /** The treepath that was selected before the drag and drop began. */
    private TreePath selectedPathBeforeDrag;

    /** The treepath that is under the mouse pointer in a drag and drop action. */
    private TreePath dropTargetPath;

    /** The component is in a drag and drop action */
    private boolean isInDndMode = false;

    /** A Timer that expands/collapses leafs when the mouse hovers above it.
 * This is only used during Drag and Drop. */
    private Timer dndAutoExpanderTimer;

    /**
 * Constructa a tree view
 * @param model the tree model that this JTree should use.
 */
    public TreeView(javax.swing.tree.TreeModel model) {
        super(model);

        ToolTipManager.sharedInstance().registerComponent(this);

        putClientProperty("JTree.lineStyle", "Angled");

        setShowsRootHandles(true);
        setRootVisible(false);

        //setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        AbstractFolder root = (AbstractFolder) treeModel.getRoot();

        expand(root);

        repaint();

        setDropTarget(new DropHandler());

        dndAutoExpanderTimer = new Timer(1000, new TreeLeafActionListener(this));
        dndAutoExpanderTimer.setRepeats(false);
    }

    /**
 * Expands the specified node so it corresponds to the expanded attribute in the configuration.
 * @param parent node to check if it should be expanded or not.
 */
    public final void expand(AbstractFolder parent) {
        // get configuration from tree.xml file
        FolderItem item = parent.getFolderItem();

        XmlElement property = item.getElement("property");

        if (property != null) {
            String expanded = property.getAttribute("expanded");

            if (expanded == null) {
                expanded = "true";
            }

            // expand folder
            int row = getRowForPath(new TreePath(parent.getPath()));

            if (expanded.equals("true")) {
                expandRow(row);
            }
        }

        // recursivly expand all children
        for (int i = 0; i < parent.getChildCount(); i++) {
            AbstractFolder child = (AbstractFolder) parent.getChildAt(i);
            expand(child);
        }
    }

    /**
 * Returns the tree node that is intended for a drop action.
 * If this method is called during a non-drag-and-drop invocation
 * there is no guarantee what it will return.
 * @return the folder tree node that is targeted for the drop action; null otherwise.
 */
    public AbstractFolder getDropTargetFolder() {
        AbstractFolder node = null;

        if (dropTargetPath != null) {
            node = (AbstractFolder) dropTargetPath.getLastPathComponent();
        }

        return node;
    }

    /**
 * Sets the stored drop target path to null.
 * This should be done after the getDropTargetFolder() has been used in
 * a folder command. 
 */
    void resetDropTargetFolder() {
        dropTargetPath = null;
    }

    /**
 * Returns the tree node that was selected before a drag and drop was initiated.
 * If this method is called during a non-drag-and-drop invocation
 * there is no guarantee what it will return.
 * @return the folder that is being dragged; null if it wasnt initiated in this component.
 */
    public AbstractFolder getSelectedNodeBeforeDragAction() {
        AbstractFolder node = null;

        if (selectedPathBeforeDrag != null) {
            node = (AbstractFolder) selectedPathBeforeDrag.getLastPathComponent();
        }

        return node;
    }

    /**
 * Returns true if the tree is in a Drag and Drop action.
 * @return true if the tree is in a Drag and Drop action; false otherwise.
 */
    public boolean isInDndAction() {
        return isInDndMode;
    }

    /**
 * Sets up this TreeView for Drag and drop action.
 * Stores the selected tree leaf before the action begins, this
 * is used later when the Drag and drop action is completed.
 */
    private void setUpDndAction() {
        isInDndMode = true;
        selectedPathBeforeDrag = getSelectionPath();
    }

    /**
 * Resets this TreeView after a Drag and drop action has occurred.
 * Selects the previous selected tree leaf before the DnD action began.
 */
    private void resetDndAction() {
        dndAutoExpanderTimer.stop();
        setSelectionPath(selectedPathBeforeDrag);
        selectedPathBeforeDrag = null;
        isInDndMode = false;
    }

    /**
 * Our own drop target implementation.
 * This treeview class uses its own drop target since the common drop target in Swing >1.4
 * does not provide a fine grained support for dragging items onto
 * leafs, when some leafs does not accept new items.
 * @author redsolo
 */
    private class DropHandler extends DropTarget {
        private boolean canImport;

        /** The latest mouse location. */
        private Point location;

        /**
 * Our own implementation to ask the transfer handler for each leaf the user moves above.
 * {@inheritDoc}
 */
        public void dragOver(DropTargetDragEvent e) {
            if ((location == null) || (!location.equals(e.getLocation()))) {
                location = e.getLocation();

                TreePath targetPath = getClosestPathForLocation(location.x,
                        location.y);

                if ((dropTargetPath != null) && (targetPath == dropTargetPath)) {
                    return;
                }

                dropTargetPath = targetPath;

                dndAutoExpanderTimer.restart();

                TreeView.this.getSelectionModel().setSelectionPath(dropTargetPath);

                DataFlavor[] flavors = e.getCurrentDataFlavors();

                JComponent c = (JComponent) e.getDropTargetContext()
                                             .getComponent();
                TransferHandler importer = c.getTransferHandler();

                if ((importer != null) && importer.canImport(c, flavors)) {
                    canImport = true;
                } else {
                    canImport = false;
                }

                int dropAction = e.getDropAction();

                if (canImport) {
                    e.acceptDrag(dropAction);
                } else {
                    e.rejectDrag();
                }
            }
        }

        /** {@inheritDoc} */
        public void dragEnter(DropTargetDragEvent e) {
            setUpDndAction();

            DataFlavor[] flavors = e.getCurrentDataFlavors();

            JComponent c = (JComponent) e.getDropTargetContext().getComponent();
            TransferHandler importer = c.getTransferHandler();

            if ((importer != null) && importer.canImport(c, flavors)) {
                canImport = true;
            } else {
                canImport = false;
            }

            int dropAction = e.getDropAction();

            if (canImport) {
                e.acceptDrag(dropAction);
            } else {
                e.rejectDrag();
            }
        }

        /** {@inheritDoc} */
        public void dragExit(DropTargetEvent e) {
            resetDndAction();
            dropTargetPath = null;
        }

        /** {@inheritDoc} */
        public void drop(DropTargetDropEvent e) {
            int dropAction = e.getDropAction();

            JComponent c = (JComponent) e.getDropTargetContext().getComponent();
            TransferHandler importer = c.getTransferHandler();

            if (canImport && (importer != null)) {
                e.acceptDrop(dropAction);

                try {
                    Transferable t = e.getTransferable();
                    e.dropComplete(importer.importData(c, t));
                } catch (RuntimeException re) {
                    e.dropComplete(false);
                }
            } else {
                e.rejectDrop();
            }

            resetDndAction();
        }

        /** {@inheritDoc} */
        public void dropActionChanged(DropTargetDragEvent e) {
            int dropAction = e.getDropAction();

            if (canImport) {
                e.acceptDrag(dropAction);
            } else {
                e.rejectDrag();
            }
        }
    }

    /**
 * An ActionListener that collapses/expands leafs in a tree.
 * @author redsolo
 */
    private class TreeLeafActionListener implements ActionListener {
        private JTree treeParent;

        /**
 * Constructs a leaf listener.
 * @param parent the parent JTree.
 */
        public TreeLeafActionListener(JTree parent) {
            treeParent = parent;
        }

        /** {@inheritDoc} */
        public void actionPerformed(ActionEvent e) {
            // Do nothing if we are hovering over the root node
            if (dropTargetPath != null) {
                if (isExpanded(dropTargetPath)) {
                    collapsePath(dropTargetPath);
                } else {
                    expandPath(dropTargetPath);
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6549.java