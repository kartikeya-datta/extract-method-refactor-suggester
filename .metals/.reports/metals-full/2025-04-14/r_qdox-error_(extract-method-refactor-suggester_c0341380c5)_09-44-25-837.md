error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4385.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4385.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4385.java
text:
```scala
n@@ew TableHeaderMouseListener(this, getTableModelSorter());

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.table;

import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import org.columba.core.gui.focus.FocusOwner;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.main.MainInterface;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.folderoptions.FolderOptionsController;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.frame.TreeViewOwner;
import org.columba.mail.gui.table.action.CopyAction;
import org.columba.mail.gui.table.action.CutAction;
import org.columba.mail.gui.table.action.DeleteAction;
import org.columba.mail.gui.table.action.PasteAction;
import org.columba.mail.gui.table.action.ViewMessageAction;
import org.columba.mail.gui.table.dnd.HeaderTableDnd;
import org.columba.mail.gui.table.model.HeaderTableModel;
import org.columba.mail.gui.table.model.MessageNode;
import org.columba.mail.gui.table.model.TableModelChangedEvent;
import org.columba.mail.gui.table.model.TableModelFilter;
import org.columba.mail.gui.table.model.TableModelSorter;
import org.columba.mail.gui.table.model.TableModelThreadedView;
import org.columba.mail.gui.table.model.TableModelUpdateManager;
import org.columba.mail.gui.table.util.MarkAsReadTimer;
import org.columba.mail.gui.view.AbstractMailView;
import org.columba.mail.message.HeaderList;
import org.columba.mail.util.MailResourceLoader;
import org.frappucino.treetable.Tree;

/**
 * Shows the message list. By default, this is the read/unread state if a
 * message, Subject:, Date:, From: and Size headerfields.
 * <p>
 * Folder-specific configuration options are handled by
 * {@link FolderOptionsController}and can be configured by the user in the
 * MessageFolder Options Dialog.
 * 
 * @author fdietz
 */
public class TableController implements FocusOwner, ListSelectionListener {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger
            .getLogger("org.columba.mail.gui.table");

    /**
     * table model
     */
    private HeaderTableModel headerTableModel;

    /**
     * mouse listener responsible for sorting actions fired by the user
     * selecting the column headers of the table
     */
    private HeaderTableMouseListener headerTableMouseListener;

    /**
     * Drag'n'drop handling of messages
     */
    private HeaderTableDnd headerTableDnd;

    /**
     * filter action which should be accessible from the menu only
     */
    private FilterActionListener filterActionListener;

    /**
     * table view
     */
    protected TableView view;

    /**
     * reference to mail framemediator
     */
    protected FrameMediator frameController;

    /**
     * timer which marks a message as read after a certain amount of time.
     */
    protected MarkAsReadTimer markAsReadTimer;

    /**
     * table view context menu
     */
    protected TableMenu menu;

    /**
     * filter model
     */
    protected TableModelFilter tableModelFilteredView;

    /**
     * sorting model
     */
    protected TableModelSorter tableModelSorter;

    /**
     * threaded-view model
     */
    protected TableModelThreadedView tableModelThreadedView;

    /**
     * update manager should handle all update requests
     * <p>
     * Don't update the models directly.
     */
    protected TableModelUpdateManager updateManager;

    /**
     * previously selected rows
     */
    protected int[] previouslySelectedRows;

    /**
     * previously selected folder
     */
    private MessageFolder previouslySelectedFolder;

    /**
     * tooltip mouse handler
     */
    private ColumnHeaderTooltips tips;

    /**
     * Constructor
     * 
     * @param mailFrameController
     *            mail framemediator
     */
    public TableController(FrameMediator frameController) {
        this.frameController = frameController;

        // init table model
        headerTableModel = new HeaderTableModel();

        // init filter model
        tableModelFilteredView = new TableModelFilter(headerTableModel);

        // init threaded-view model
        tableModelThreadedView = new TableModelThreadedView(
                tableModelFilteredView);

        // init sorting model
        tableModelSorter = new TableModelSorter(tableModelThreadedView);

        // now, init update manager
        // -> sorting is applied at the end after all other
        // -> operations like filtering
        updateManager = new TableModelUpdateManager(tableModelSorter);

        // init view
        view = new TableView(headerTableModel, tableModelSorter);

        // pass tree to model, used by the threaded-view
        headerTableModel.setTree((Tree) view.getTree());

        // init mouse listener for the column header
        headerTableMouseListener = new HeaderTableMouseListener(this);
        view.addMouseListener(headerTableMouseListener);

        // not used currently
        filterActionListener = new FilterActionListener(this);

        // create a new markAsReadTimer
        markAsReadTimer = new MarkAsReadTimer(this);

        getView().setTransferHandler(
                new TableViewTransferHandler(getFrameController()));
        getView().setDragEnabled(true);

        // MouseListener sorts table when clicking on a column header
        new TableHeaderMouseListener(getView(), getTableModelSorter());

        // register at focus manager
        MainInterface.focusManager.registerComponent(this);

        // we need this for the focus manager
        getView().getSelectionModel().addListSelectionListener(this);

        JTableHeader header = view.getTableHeader();

        tips = new ColumnHeaderTooltips();
        header.addMouseMotionListener(tips);
    }

    /**
     * Assigns a tooltip for each column
     * <p>
     * Tooltips for columns can be found in
     * org.columba.mail.i18n.header.header.properties.
     * 
     * @see org.columba.mail.folderoptions.ColumnOptionsPlugin
     *  
     */
    public void initTooltips() {
        tips.clear();

        // Assign a tooltip for each of the columns
        for (int c = 0; c < view.getColumnCount(); c++) {
            TableColumn col = view.getColumnModel().getColumn(c);

            // column IDs are all lower case
            String lookup = ((String) col.getIdentifier()).toLowerCase();

            // append "_tooltip"
            lookup = lookup + "_tooltip";

            // get translation
            String s = MailResourceLoader.getString("header", "header", lookup);

            tips.setToolTip(col, s);
        }
    }

    /**
     * Get view of table controller
     * 
     * @return table view
     */
    public TableView getView() {
        return view;
    }

    /**
     * Get table model
     * 
     * @return table model
     */
    public HeaderTableModel getHeaderTableModel() {
        return headerTableModel;
    }

    /**
     * Select messages with UIDs.
     * <p>
     * Message UIDs are converted to {@link MessageNode}objects.
     * 
     * @param uids
     *            array of message UIDs
     */
    public void setSelected(Object[] uids) {
        MessageNode[] nodes = new MessageNode[uids.length];

        for (int i = 0; i < uids.length; i++) {
            nodes[i] = getHeaderTableModel().getMessageNode(uids[i]);
        }

        int[] rows = new int[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            TreePath path = new TreePath(nodes[i].getPath());
            rows[i] = view.getTree().getRowForPath(path);

        }
        view.selectRow(rows[0]);

    }

    /** ************************ actions ******************************* */
    /**
     * create the PopupMenu
     */
    public void createPopupMenu() {
        menu = new TableMenu(frameController);
    }

    /**
     * Get popup menu
     * 
     * @return popup menu
     */
    public JPopupMenu getPopupMenu() {
        return menu;
    }

    /**
     * Method is called if folder data changed.
     * <p>
     * It is responsible for updating the correct underlying model.
     * 
     * @param event
     *            update event
     */
    public void tableChanged(TableModelChangedEvent event) throws Exception {

        // selected rows before updating the model
        // -> used later to restore the selection
        previouslySelectedRows = view.getSelectedRows();

        // folder in which the update occurs
        AbstractFolder folder = event.getSrcFolder();

        if (folder == null) { return; }

        LOG.info("source folder=" + folder.getName());

        // get current selection
        FolderCommandReference[] r = (FolderCommandReference[]) frameController
                .getSelectionManager().getSelection("mail.table");
        MessageFolder srcFolder = (MessageFolder) r[0].getFolder();

        // its always possible that no folder is currenlty selected
        if (srcFolder != null) {
            LOG.info("selected folder=" + srcFolder.getName());
        }

        // make tree visible
        if (getFrameController() instanceof TreeViewOwner) {
            if (srcFolder != null) {
                ((TreeViewOwner) getFrameController()).getTreeController()
                        .getView()
                        .makeVisible(srcFolder.getSelectionTreePath());
            }
        }

        // update infopanel (gray panel below the toolbar)
        // showing total/unread/recent messages count
        if (getFrameController().getView() instanceof AbstractMailView) {
            if (srcFolder != null) {
                ((AbstractMailView) getFrameController().getView())
                        .getFolderInfoPanel().setFolder(srcFolder);
            }
        }

        // only update table if, this folder is the same
        // as the currently selected
        if (!folder.equals(srcFolder)) { return; }

        switch (event.getEventType()) {
        case TableModelChangedEvent.SET:
            {
                updateManager.set(event.getHeaderList());

                break;
            }

        case TableModelChangedEvent.UPDATE:
            {
                updateManager.update();

                break;
            }

        case TableModelChangedEvent.REMOVE:
            {
                updateManager.remove(event.getUids());

                break;
            }

        case TableModelChangedEvent.MARK:
            {
                updateManager.modify(event.getUids());

                break;
            }
        }

        // when marking messages, don't touch selection
        if (event.getEventType() == TableModelChangedEvent.MARK) return;
        if (event.getEventType() == TableModelChangedEvent.SET) return;

        // re-select previous selection
        if (previouslySelectedRows != null) {
            // only re-select if only a single row was formerly selected
            if (previouslySelectedRows.length == 1) {
                view.selectRow(previouslySelectedRows[0]);
            }
        }

    }

    /**
     * Returns the mailFrameController.
     * 
     * @return MailFrameController
     */
    public FrameMediator getFrameController() {
        return frameController;
    }

    /**
     * Returns the markAsReadTimer.
     * 
     * @return MarkAsReadTimer
     */
    public MarkAsReadTimer getMarkAsReadTimer() {
        return markAsReadTimer;
    }

    /**
     * Save folder properties of currently selected folder.
     *  
     */
    public void saveProperties() {
        if (previouslySelectedFolder != null) {
            ((MailFrameMediator) getFrameController())
                    .getFolderOptionsController()
                    .save(previouslySelectedFolder);
        }
    }

    /**
     * Show the headerlist of currently selected folder.
     * <p>
     * Additionally, implements folderoptions plugin entrypoint.
     * 
     * @see org.columba.mail.folder.folderoptions
     * @see org.columba.mail.gui.frame.ViewHeaderListInterface#showHeaderList(org.columba.mail.folder.Folder,
     *      org.columba.mail.message.HeaderList)
     */
    public void showHeaderList(MessageFolder folder, HeaderList headerList)
            throws Exception {

        // save previously selected folder options
        if (previouslySelectedFolder != null) {
            ((MailFrameMediator) getFrameController())
                    .getFolderOptionsController()
                    .save(previouslySelectedFolder);
        }

        // load options of newly selected folder
        ((MailFrameMediator) getFrameController()).getFolderOptionsController()
                .load(folder, FolderOptionsController.STATE_BEFORE);

        // send an update notification to the table model
        TableModelChangedEvent ev = new TableModelChangedEvent(
                TableModelChangedEvent.SET, folder, headerList);
        tableChanged(ev);

        // load options of newly selected folder
        ((MailFrameMediator) getFrameController()).getFolderOptionsController()
                .load(folder, FolderOptionsController.STATE_AFTER);

        // remember previously selected folder
        previouslySelectedFolder = folder;
    }

    /**
     * Show empty messagelist with no elements.
     *  
     */
    public void clear() {

        System.out.println("--> clear");

        //	send an update notification to the table model
        TableModelChangedEvent ev = new TableModelChangedEvent(
                TableModelChangedEvent.SET, previouslySelectedFolder,
                new HeaderList());

        try {
            tableChanged(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** *********** implement getter/setter methods ******************** */
    /**
     * return the table model sorter
     */
    public TableModelSorter getTableModelSorter() {
        return tableModelSorter;
    }

    /**
     * return the threaded view model
     */
    public TableModelThreadedView getTableModelThreadedView() {
        return tableModelThreadedView;
    }

    /**
     * return the filtered view model
     */
    public TableModelFilter getTableModelFilteredView() {
        return tableModelFilteredView;
    }

    /**
     * @return
     */
    public TableModelUpdateManager getUpdateManager() {
        return updateManager;
    }

    /** ******************* FocusOwner interface *********************** */

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#copy()
     */
    public void copy() {
        new CopyAction(getFrameController()).actionPerformed(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#cut()
     */
    public void cut() {
        new CutAction(getFrameController()).actionPerformed(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#delete()
     */
    public void delete() {
        new DeleteAction(getFrameController()).actionPerformed(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#getComponent()
     */
    public JComponent getComponent() {
        return getView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#isCopyActionEnabled()
     */
    public boolean isCopyActionEnabled() {
        if (getView().getSelectedNodes().length > 0) { return true; }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#isCutActionEnabled()
     */
    public boolean isCutActionEnabled() {
        if (getView().getSelectedNodes().length > 0) { return true; }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#isDeleteActionEnabled()
     */
    public boolean isDeleteActionEnabled() {
        if (getView().getSelectedNodes().length > 0) { return true; }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#isPasteActionEnabled()
     */
    public boolean isPasteActionEnabled() {
        if (MainInterface.clipboardManager.getMessageSelection() == null) { return false; }

        if (MainInterface.clipboardManager.getMessageSelection().length > 0) { return true; }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#isRedoActionEnabled()
     */
    public boolean isRedoActionEnabled() {
        // action not supported
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#isSelectAllActionEnabled()
     */
    public boolean isSelectAllActionEnabled() {
        if (getView().getRowCount() > 0) { return true; }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#isUndoActionEnabled()
     */
    public boolean isUndoActionEnabled() {
        // action not supported
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#paste()
     */
    public void paste() {
        new PasteAction(getFrameController()).actionPerformed(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#redo()
     */
    public void redo() {
        // action not supported
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#selectAll()
     */
    public void selectAll() {
        getView().selectAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.core.gui.focus.FocusOwner#undo()
     */
    public void undo() {
        // todo is not supported
    }

    /** ************************* ListSelectionListener interface ************* */

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent arg0) {

        // enable/disable cut/copy/paste/selectall actions
        MainInterface.focusManager.updateActions();

        // if user is currently changing selection, don't do anything
        // -> wait until the final selection is available
        if (arg0.getValueIsAdjusting()) return;

        // skip if no message selected
        if ( getView().getSelectedNodes().length == 0 ) return;
        
        // show message
        new ViewMessageAction(getFrameController()).actionPerformed(null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4385.java