error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9913.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9913.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9913.java
text:
```scala
F@@olderItem item = folder.getConfiguration();

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

import java.util.logging.Logger;

import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.main.MainInterface;
import org.columba.core.xml.XmlElement;

import org.columba.mail.config.FolderItem;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.infopanel.FolderInfoPanel;
import org.columba.mail.gui.table.command.ViewHeaderListCommand;
import org.columba.mail.gui.tree.util.FolderTreeCellRenderer;

import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;


/**
 * this class shows the the folder hierarchy
 */
public class TreeController implements TreeWillExpandListener {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger.getLogger("org.columba.mail.gui.tree");

    private TreeView folderTree;
    private boolean b = false;
    private TreePath treePath;
    private FolderInfoPanel messageFolderInfoPanel;
    public JScrollPane scrollPane;
    private AbstractFolder oldSelection;
    private FolderTreeMouseListener mouseListener;
    private AbstractFolder selectedFolder;
    private TreeModel model;
    private TreeView view;
    private FrameMediator frameController;
    protected TreeMenu menu;

    public TreeController(FrameMediator frameController,
        TreeModel model) {
        this.model = model;
        this.frameController = frameController;

        view = new TreeView(model);

        view.addTreeWillExpandListener(this);

        mouseListener = new FolderTreeMouseListener(this);

        view.addMouseListener(mouseListener);

        FolderTreeCellRenderer renderer = new FolderTreeCellRenderer();
        view.setCellRenderer(renderer);

        getView().setTransferHandler(new TreeViewTransferHandler());
        getView().setDragEnabled(true);

        /*
getView().getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0),
        "RENAME");
RenameFolderAction action = new RenameFolderAction(mailFrameController);
getView().getActionMap().put("RENAME", action);
*/
    }

    public TreeModel getModel() {
        return model;
    }

    public TreeView getView() {
        return view;
    }

    public void setSelected(MessageFolder folder) {
        view.clearSelection();

        TreePath path = folder.getSelectionTreePath();

        view.requestFocus();
        view.setLeadSelectionPath(path);
        view.setAnchorSelectionPath(path);
        view.expandPath(path);

        this.selectedFolder = folder;

        MainInterface.processor.addOp(new ViewHeaderListCommand(
                getFrameController(),
                ((MailFrameMediator)getFrameController()).getTreeSelection()));
    }

    public void createPopupMenu() {
        menu = new TreeMenu(frameController);
    }

    public JPopupMenu getPopupMenu() {
        return menu;
    }

    public AbstractFolder getSelected() {
        return selectedFolder;
    }

    /**
 * Returns the mailFrameController.
 * @return MailFrameController
 */
    public FrameMediator getFrameController() {
        return frameController;
    }

    /******************** TreeWillExpand Interface *******************************/
    public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
        LOG.info("treeWillExpand=" + e.getPath().toString());

        AbstractFolder treeNode = (AbstractFolder) e.getPath()
                                                    .getLastPathComponent();

        if (treeNode == null) {
            return;
        }

        /*
// fetch new sub folder list
// -> this is a hack for imap folder:
// -> when expanding the IMAPRootFolder the
// -> list of folders gets synchronized
FolderCommandReference[] cr = new FolderCommandReference[1];
cr[0] = new FolderCommandReference(treeNode);

MainInterface.processor.addOp(new FetchSubFolderListCommand(cr));
*/
        // save expanded state
        saveExpandedState(treeNode, e.getPath());
    }

    public void treeWillCollapse(TreeExpansionEvent e) {
        AbstractFolder treeNode = (AbstractFolder) e.getPath()
                                                    .getLastPathComponent();

        if (treeNode == null) {
            return;
        }

        // save expanded state
        saveExpandedState(treeNode, e.getPath());
    }

    private void saveExpandedState(AbstractFolder folder, TreePath path) {
        FolderItem item = folder.getFolderItem();

        XmlElement property = item.getElement("property");

        // Note: we negate the expanded state because this is
        //       a will-expand/collapse listener
        if (!getView().isExpanded(path)) {
            property.addAttribute("expanded", "true");
        } else {
            property.addAttribute("expanded", "false");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9913.java