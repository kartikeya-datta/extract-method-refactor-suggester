error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9702.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9702.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9702.java
text:
```scala
s@@uper(controller, MailResourceLoader.getString("menu", "mainframe", "menu_view_sort_tree"),"menu_view_sort_tree");

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
package org.columba.mail.gui.tree.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import org.columba.core.action.IMenu;
import org.columba.core.config.DefaultItem;
import org.columba.core.config.IDefaultItem;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.gui.menu.CRadioButtonMenuItem;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.frame.TreeViewOwner;
import org.columba.mail.gui.tree.TreeController;
import org.columba.mail.gui.tree.comparator.FolderComparator;
import org.columba.mail.gui.tree.comparator.UnreadFolderComparator;
import org.columba.mail.util.MailResourceLoader;

/**
 * Menu items for sorting the folder tree.
 * @author redsolo
 */
public class SortFoldersMenu extends IMenu implements ActionListener {

    private static final String UNSORTED_ACTION = "UNSORTED";
    private static final String ALPHABETIC_ACTION = "ALPHABETIC";
    private static final String UNREAD_ACTION = "UNREAD";
    private static final String DESC_ACTION = "DESC";
    private static final String ASC_ACTION = "ASC";

    private ButtonGroup sortGroup;
    private ButtonGroup orderGroup;

    private JRadioButtonMenuItem unsortedMenuItem;
    private JRadioButtonMenuItem unreadMenuItem;
    private JRadioButtonMenuItem alphaMenuItem;

    private JRadioButtonMenuItem ascendingMenuItem;
    private JRadioButtonMenuItem descendingMenuItem;

    private String activeComparator;

    /**
     * Creates the sort folders submenu.
     * @param controller the controller.
     */
    public SortFoldersMenu(FrameMediator controller) {
        super(controller, MailResourceLoader.getString("menu", "mainframe", "menu_view_sort_tree"));

        createSubMenu();
        loadConfig();
    }

    /**
     * Creates the sub menu.
     */
    private void createSubMenu() {
        removeAll();

        sortGroup = new ButtonGroup();

        unsortedMenuItem = addRadioButtonItem(sortGroup, "menu_view_sort_tree_unsorted", UNSORTED_ACTION);
        alphaMenuItem = addRadioButtonItem(sortGroup, "menu_view_sort_tree_alpha", ALPHABETIC_ACTION);
        unreadMenuItem = addRadioButtonItem(sortGroup, "menu_view_sort_tree_unread", UNREAD_ACTION);

        addSeparator();

        orderGroup = new ButtonGroup();
        ascendingMenuItem = addRadioButtonItem(orderGroup, "menu_view_sort_asc", ASC_ACTION);
        descendingMenuItem = addRadioButtonItem(orderGroup, "menu_view_sort_desc", DESC_ACTION);
    }

    /**
     * Loads the configuration.
     */
    private void loadConfig() {
        XmlElement element = MailConfig.getInstance().get("options").getElement("/options/gui/tree/sorting");

        FolderComparator comparator = null;
        if (element != null) {
            IDefaultItem item = new DefaultItem(element);
            boolean ascending = item.getBooleanWithDefault("ascending", true);
            activeComparator = 
                item.getRoot().getAttribute("comparator", "").toUpperCase();

            if (activeComparator.equals(ALPHABETIC_ACTION)) {
                comparator = new FolderComparator(ascending);
                alphaMenuItem.setSelected(true);
            } else if (activeComparator.equals(UNREAD_ACTION)) {
                comparator = new UnreadFolderComparator(ascending);
                unreadMenuItem.setSelected(true);
            } else {
                unsortedMenuItem.setSelected(true);
                ascendingMenuItem.setEnabled(false);
                descendingMenuItem.setEnabled(false);
            }

            if (ascending) {
                ascendingMenuItem.setSelected(true);
            } else {
                descendingMenuItem.setSelected(true);
            }
        } else {
            comparator = new FolderComparator(true);
            alphaMenuItem.setSelected(true);
            ascendingMenuItem.setSelected(true);
        }

        if (comparator != null) {
            TreeViewOwner mediator = (TreeViewOwner) getFrameMediator();
            ((TreeController)mediator.getTreeController()).getView().setFolderComparator(comparator);
            ((TreeController)mediator.getTreeController()).getView().setSortingEnabled(true);
        }
    }

    /**
     * Saves the config.
     */
    private void saveConfig() {
        XmlElement treeElement = MailConfig.getInstance().get("options").getElement("/options/gui/tree");
        if (treeElement == null) {
            treeElement = MailConfig.getInstance().get("options").getElement("/options/gui").addSubElement("tree");
        }

        XmlElement element = treeElement.getElement("sorting");
        if (element == null) {
            element = treeElement.addSubElement("sorting");
        }

        IDefaultItem item = new DefaultItem(element);
        item.setBoolean("ascending", ascendingMenuItem.isSelected());
        item.setString("comparator", activeComparator.toLowerCase());
        item.setBoolean("sorted", !activeComparator.equals(UNSORTED_ACTION));
        element.notifyObservers();
    }

    /**
     * Adds a new JRadioButtonMenuItem to the menu and group.
     * @param group the button group.
     * @param i18nName the i18n name in the mainframe properties file.
     * @param actionCommand the action command string for the action.
     * @return the newly created menu item.
     */
    private JRadioButtonMenuItem addRadioButtonItem(ButtonGroup group, String i18nName, String actionCommand) {
        String i18n = MailResourceLoader.getString("menu", "mainframe", i18nName);
        CRadioButtonMenuItem headerMenuItem = new CRadioButtonMenuItem(i18n);
        headerMenuItem.setActionCommand(actionCommand);
        headerMenuItem.addActionListener(this);
        group.add(headerMenuItem);
        add(headerMenuItem);
        return headerMenuItem;
    }

    /**
     * Menu actions.
     * @param e the action event.
     */
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        TreeViewOwner mediator = (TreeViewOwner) getFrameMediator();

        if (action.equals(UNSORTED_ACTION)) {

            activeComparator = UNSORTED_ACTION;
            ascendingMenuItem.setEnabled(false);
            descendingMenuItem.setEnabled(false);
            ((TreeController)mediator.getTreeController()).getView().setSortingEnabled(false);
        } else {

            ascendingMenuItem.setEnabled(true);
            descendingMenuItem.setEnabled(true);
            ((TreeController)mediator.getTreeController()).getView().setSortingEnabled(true);
            if (action.equals(ASC_ACTION)) {
            	((TreeController)mediator.getTreeController()).getView().sortAscending(true);
            } else if (action.equals(DESC_ACTION)) {
            	((TreeController)mediator.getTreeController()).getView().sortAscending(false);
            } else {
                activeComparator = action;
                if (action.equals(UNREAD_ACTION)) {
                	((TreeController)mediator.getTreeController()).getView().setFolderComparator(new UnreadFolderComparator(ascendingMenuItem.isSelected()));
                } else if (action.equals(ALPHABETIC_ACTION)) {
                	((TreeController)mediator.getTreeController()).getView().setFolderComparator(new FolderComparator(ascendingMenuItem.isSelected()));
                }
            }
        }
        saveConfig();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9702.java