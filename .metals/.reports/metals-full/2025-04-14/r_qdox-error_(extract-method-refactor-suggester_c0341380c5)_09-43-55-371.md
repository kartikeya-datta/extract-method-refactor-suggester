error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4892.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4892.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4892.java
text:
```scala
r@@eturn e.isPopupTrigger() || (MouseEvent.BUTTON2_MASK & e.getModifiers()) > 0 || (MouseEvent.BUTTON3_MASK == e.getModifiers());

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.report.gui.tree;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.jmeter.control.gui.ReportGui;
import org.apache.jmeter.gui.ReportGuiPackage;
import org.apache.jmeter.gui.ReportMainFrame;
import org.apache.jmeter.report.gui.action.ReportDragNDrop;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class ReportTreeListener implements TreeSelectionListener, MouseListener, KeyListener, MouseMotionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    // Container endWindow;
    // JPopupMenu pop;
    private TreePath currentPath;

    private ActionListener actionHandler;

    private ReportTreeModel model;

    private JTree tree;

    private boolean dragging = false;

    private ReportTreeNode[] draggedNodes;

    private JLabel dragIcon = new JLabel(JMeterUtils.getImage("leafnode.gif"));

    /**
     * Constructor for the JMeterTreeListener object.
     */
    public ReportTreeListener(ReportTreeModel model) {
        this.model = model;
        dragIcon.validate();
        dragIcon.setVisible(true);
    }

    public ReportTreeListener() {
        dragIcon.validate();
        dragIcon.setVisible(true);
    }

    public void setModel(ReportTreeModel m) {
        model = m;
    }

    /**
     * Sets the ActionHandler attribute of the JMeterTreeListener object.
     *
     * @param ah
     *            the new ActionHandler value
     */
    public void setActionHandler(ActionListener ah) {
        actionHandler = ah;
    }

    /**
     * Sets the JTree attribute of the JMeterTreeListener object.
     *
     * @param tree
     *            the new JTree value
     */
    public void setJTree(JTree tree) {
        this.tree = tree;
    }

    /**
     * Sets the EndWindow attribute of the JMeterTreeListener object.
     *
     * @param window
     *            the new EndWindow value
     */
    public void setEndWindow(Container window) {
        // endWindow = window;
    }

    /**
     * Gets the JTree attribute of the JMeterTreeListener object.
     *
     * @return tree the current JTree value.
     */
    public JTree getJTree() {
        return tree;
    }

    /**
     * Gets the CurrentNode attribute of the JMeterTreeListener object.
     *
     * @return the CurrentNode value
     */
    public ReportTreeNode getCurrentNode() {
        if (currentPath != null) {
            if (currentPath.getLastPathComponent() != null) {
                return (ReportTreeNode) currentPath.getLastPathComponent();
            } else {
                return (ReportTreeNode) currentPath.getParentPath().getLastPathComponent();
            }
        } else {
            return (ReportTreeNode) model.getRoot();
        }
    }

    public ReportTreeNode[] getSelectedNodes() {
        TreePath[] paths = tree.getSelectionPaths();
        if (paths == null) {
            return new ReportTreeNode[] { getCurrentNode() };
        }
        ReportTreeNode[] nodes = new ReportTreeNode[paths.length];
        for (int i = 0; i < paths.length; i++) {
            nodes[i] = (ReportTreeNode) paths[i].getLastPathComponent();
        }

        return nodes;
    }

    public TreePath removedSelectedNode() {
        currentPath = currentPath.getParentPath();
        return currentPath;
    }

    public void valueChanged(TreeSelectionEvent e) {
        log.debug("value changed, updating currentPath");
        currentPath = e.getNewLeadSelectionPath();
        actionHandler.actionPerformed(new ActionEvent(this, 3333, "edit"));
    }

    public void mouseClicked(MouseEvent ev) {
    }

    public void mouseReleased(MouseEvent e) {
        if (dragging && isValidDragAction(draggedNodes, getCurrentNode())) {
            dragging = false;
            JPopupMenu dragNdrop = new JPopupMenu();
            JMenuItem item = new JMenuItem(JMeterUtils.getResString("Insert Before"));
            item.addActionListener(actionHandler);
            item.setActionCommand(ReportDragNDrop.INSERT_BEFORE);
            dragNdrop.add(item);
            item = new JMenuItem(JMeterUtils.getResString("Insert After"));
            item.addActionListener(actionHandler);
            item.setActionCommand(ReportDragNDrop.INSERT_AFTER);
            dragNdrop.add(item);
            item = new JMenuItem(JMeterUtils.getResString("Add as Child"));
            item.addActionListener(actionHandler);
            item.setActionCommand(ReportDragNDrop.ADD);
            dragNdrop.add(item);
            dragNdrop.addSeparator();
            item = new JMenuItem(JMeterUtils.getResString("Cancel"));
            dragNdrop.add(item);
            displayPopUp(e, dragNdrop);
        } else {
            ReportGuiPackage.getInstance().getMainFrame().repaint();
        }
        dragging = false;
    }

    public ReportTreeNode[] getDraggedNodes() {
        return draggedNodes;
    }

    /**
     * Tests if the node is being dragged into one of it's own sub-nodes, or
     * into itself.
     */
    private boolean isValidDragAction(ReportTreeNode[] source, ReportTreeNode dest) {
        boolean isValid = true;
        TreeNode[] path = dest.getPath();
        for (int i = 0; i < path.length; i++) {
            if (contains(source, path[i])) {
                isValid = false;
            }
        }
        return isValid;
    }

    public void mouseEntered(MouseEvent e) {
    }

    private void changeSelectionIfDragging(MouseEvent e) {
        if (dragging) {
            ReportGuiPackage.getInstance().getMainFrame().drawDraggedComponent(dragIcon, e.getX(), e.getY());
            if (tree.getPathForLocation(e.getX(), e.getY()) != null) {
                currentPath = tree.getPathForLocation(e.getX(), e.getY());
                if (!contains(draggedNodes, getCurrentNode())) {
                    tree.setSelectionPath(currentPath);
                }
            }
        }
    }

    private boolean contains(Object[] container, Object item) {
        for (int i = 0; i < container.length; i++) {
            if (container[i] == item) {
                return true;
            }
        }
        return false;
    }

    public void mousePressed(MouseEvent e) {
        // Get the Main Frame.
        ReportMainFrame mainFrame = ReportGuiPackage.getInstance().getMainFrame();
        // Close any Main Menu that is open
        mainFrame.closeMenu();
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        if (tree.getPathForLocation(e.getX(), e.getY()) != null) {
            log.debug("mouse pressed, updating currentPath");
            currentPath = tree.getPathForLocation(e.getX(), e.getY());
        }
        if (selRow != -1) {
            // updateMainMenu(((JMeterGUIComponent)
            // getCurrentNode().getUserObject()).createPopupMenu());
            if (isRightClick(e)) {
                if (tree.getSelectionCount() < 2) {
                    tree.setSelectionPath(currentPath);
                }
                if (getCurrentNode() != null) {
                    log.debug("About to display pop-up");
                    displayPopUp(e);
                }
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (!dragging) {
            dragging = true;
            draggedNodes = getSelectedNodes();
            if (draggedNodes[0].getUserObject() instanceof ReportGui) {
                dragging = false;
            }

        }
        changeSelectionIfDragging(e);
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseExited(MouseEvent ev) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    private boolean isRightClick(MouseEvent e) {
        return (MouseEvent.BUTTON2_MASK & e.getModifiers()) > 0 || (MouseEvent.BUTTON3_MASK == e.getModifiers());
    }

    /*
     * NOTUSED private void updateMainMenu(JPopupMenu menu) { try { MainFrame
     * mainFrame = GuiPackage.getInstance().getMainFrame();
     * mainFrame.setEditMenu(menu); } catch (NullPointerException e) {
     * log.error("Null pointer: JMeterTreeListener.updateMenuItem()", e);
     * log.error("", e); } }
     */

    private void displayPopUp(MouseEvent e) {
        JPopupMenu pop = getCurrentNode().createPopupMenu();
        ReportGuiPackage.getInstance().displayPopUp(e, pop);
    }

    private void displayPopUp(MouseEvent e, JPopupMenu popup) {
        log.warn("Shouldn't be here");
        if (popup != null) {
            popup.pack();
            popup.show(tree, e.getX(), e.getY());
            popup.setVisible(true);
            popup.requestFocus();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4892.java