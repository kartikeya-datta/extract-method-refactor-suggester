error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9265.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9265.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9265.java
text:
```scala
a@@ctionHandler.actionPerformed(new ActionEvent(this, 3333, ActionNames.EDIT)); // $NON-NLS-1$

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

package org.apache.jmeter.gui.tree;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jmeter.gui.action.KeyStrokes;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class JMeterTreeListener implements TreeSelectionListener, MouseListener, KeyListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    // Container endWindow;
    // JPopupMenu pop;
    private TreePath currentPath;

    private ActionListener actionHandler;

    private JMeterTreeModel model;

    private JTree tree;

    /**
     * Constructor for the JMeterTreeListener object.
     */
    public JMeterTreeListener(JMeterTreeModel model) {
        this.model = model;
    }

    public JMeterTreeListener() {
    }

    public void setModel(JMeterTreeModel m) {
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
    public JMeterTreeNode getCurrentNode() {
        if (currentPath != null) {
            if (currentPath.getLastPathComponent() != null) {
                return (JMeterTreeNode) currentPath.getLastPathComponent();
            }
            return (JMeterTreeNode) currentPath.getParentPath().getLastPathComponent();
        }
        return (JMeterTreeNode) model.getRoot();
    }

    public JMeterTreeNode[] getSelectedNodes() {
        TreePath[] paths = tree.getSelectionPaths();
        if (paths == null) {
            return new JMeterTreeNode[] { getCurrentNode() };
        }
        JMeterTreeNode[] nodes = new JMeterTreeNode[paths.length];
        for (int i = 0; i < paths.length; i++) {
            nodes[i] = (JMeterTreeNode) paths[i].getLastPathComponent();
        }

        return nodes;
    }

    public TreePath removedSelectedNode() {
        currentPath = currentPath.getParentPath();
        return currentPath;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        log.debug("value changed, updating currentPath");
        currentPath = e.getNewLeadSelectionPath();
        actionHandler.actionPerformed(new ActionEvent(this, 3333, "edit")); // $NON-NLS-1$
    }

    @Override
    public void mouseClicked(MouseEvent ev) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GuiPackage.getInstance().getMainFrame().repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Get the Main Frame.
        MainFrame mainFrame = GuiPackage.getInstance().getMainFrame();
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
                log.debug("About to display pop-up");
                displayPopUp(e);
            }
        }
    }


    @Override
    public void mouseExited(MouseEvent ev) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (KeyStrokes.matches(e,KeyStrokes.COPY)) {
            ActionRouter actionRouter = ActionRouter.getInstance();
            actionRouter.doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.COPY));
            e.consume();
        } else if (KeyStrokes.matches(e,KeyStrokes.PASTE)) {
            ActionRouter actionRouter = ActionRouter.getInstance();
            actionRouter.doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.PASTE));
            e.consume();
        } else if (KeyStrokes.matches(e,KeyStrokes.CUT)) {
            ActionRouter actionRouter = ActionRouter.getInstance();
            actionRouter.doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.CUT));
            e.consume();
        } else if (KeyStrokes.matches(e,KeyStrokes.DUPLICATE)) {
            ActionRouter actionRouter = ActionRouter.getInstance();
            actionRouter.doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.DUPLICATE));
            e.consume();
        } else if (KeyStrokes.matches(e,KeyStrokes.ALT_UP_ARROW)) {
            ActionRouter actionRouter = ActionRouter.getInstance();
            actionRouter.doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.MOVE_UP));
            e.consume();
        } else if (KeyStrokes.matches(e,KeyStrokes.ALT_DOWN_ARROW)) {
            ActionRouter actionRouter = ActionRouter.getInstance();
            actionRouter.doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.MOVE_DOWN));
            e.consume();
        } else if (KeyStrokes.matches(e,KeyStrokes.ALT_LEFT_ARROW)) {
            ActionRouter actionRouter = ActionRouter.getInstance();
            actionRouter.doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.MOVE_LEFT));
            e.consume();
        } else if (KeyStrokes.matches(e,KeyStrokes.ALT_RIGHT_ARROW)) {
            ActionRouter actionRouter = ActionRouter.getInstance();
            actionRouter.doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.MOVE_RIGHT));
            e.consume();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private boolean isRightClick(MouseEvent e) {
        return e.isPopupTrigger() || (InputEvent.BUTTON2_MASK & e.getModifiers()) > 0 || (InputEvent.BUTTON3_MASK == e.getModifiers());
    }

    private void displayPopUp(MouseEvent e) {
        JPopupMenu pop = getCurrentNode().createPopupMenu();
        GuiPackage.getInstance().displayPopUp(e, pop);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9265.java