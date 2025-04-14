error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4745.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4745.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4745.java
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

package org.columba.mail.gui.tree.util;

import org.columba.core.gui.util.ImageLoader;

import org.columba.mail.config.FolderItem;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.folder.LocalRootFolder;
import org.columba.mail.folder.imap.IMAPRootFolder;
import org.columba.mail.folder.virtual.VirtualFolder;

import org.columba.ristretto.message.MessageFolderInfo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * This class is used for the mail folder tree.
 */
public class FolderTreeCellRenderer extends DefaultTreeCellRenderer {
    private static Icon expandedFolderIcon;
    private static Icon collapsedFolderIcon;
    private static Icon virtualFolderIcon;
    private static Icon localRootFolderIcon;
    private static Icon imapRootFolderIcon;
    private static Icon inboxIcon;
    private static Icon outboxIcon;
    private static Icon trashIcon;
    
    static {
        collapsedFolderIcon = ImageLoader.getSmallImageIcon("folder-closed.png");
        expandedFolderIcon = ImageLoader.getSmallImageIcon("folder-open.png");
        virtualFolderIcon = ImageLoader.getSmallImageIcon("virtualfolder.png");
        localRootFolderIcon = ImageLoader.getSmallImageIcon("localhost.png");
        imapRootFolderIcon = ImageLoader.getSmallImageIcon("stock_internet-16.png");
        inboxIcon = ImageLoader.getSmallImageIcon("inbox-16.png");
        outboxIcon = ImageLoader.getSmallImageIcon("outbox-16.png");
        trashIcon = ImageLoader.getSmallImageIcon("stock_delete-16.png");
    }
    
    private Font plainFont;
    private Font boldFont;
    private Font italicFont;

    /**
     * Generates a new CellRenderer. In this contructor font and images are set
     * to local variables. The fonts are depended on the current UIManager.
     */
    public FolderTreeCellRenderer() {
        super();

        boldFont = UIManager.getFont("Tree.font");
        boldFont = boldFont.deriveFont(Font.BOLD);
        italicFont = UIManager.getFont("Tree.font");
        italicFont = italicFont.deriveFont(Font.ITALIC);
        plainFont = UIManager.getFont("Tree.font");
    }

    /**
     * The tooltip text and unseen counter for the current folder component
     * are set. If the folder has unseen Messages the folder self is show
     * as bold and the unseen message counter is added to the folder label.
     * The folder gets a tooltip where infos (unseen, recent, total) are
     * set. If the folder is an IMAP folder and not selectable the folder is
     * set to italic with a darkgrey background.
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean isSelected, boolean expanded, boolean leaf, int row,
        boolean hasFocusVar) {
        /* RIYAD: Even though we don't do anything with this value, what it
         * is doing is setting up the selection colors and such as implemented
         * per the default cell rendered.
         */
        super.getTreeCellRendererComponent(tree, value, isSelected, expanded,
            leaf, row, hasFocusVar);

        // setting default Values
        setFont(plainFont);
        setToolTipText("");

        AbstractFolder treeNode = (AbstractFolder) value;
        setText(treeNode.getName());
        setIcon(getFolderIcon(treeNode, expanded));

        if (value instanceof MessageFolder) {
            MessageFolder folder = (MessageFolder) value;

            // getting folder info
            MessageFolderInfo info = folder.getMessageFolderInfo();

            // getting unseen value
            int unseen = info.getUnseen();

            // mark name bold if folder contains any unseen messages
            // the default is alrady set to plain
            if (unseen > 0) {
                setFont(boldFont);
            }

            FolderItem item = folder.getFolderItem();

            if (item != null) {
                String name;
                name = item.get("property", "name");

                // append unseen count to folder name
                if (unseen > 0) {
                    name = name + " (" + unseen + ") ";
                }

                // set tooltip text
                StringBuffer buf = new StringBuffer();
                buf.append("<html><body>&nbsp;Total: " + info.getExists());
                buf.append("<br>&nbsp;Unseen: " + unseen);
                buf.append("<br>&nbsp;Recent: " + info.getRecent());
                buf.append("</body></html>");
                setToolTipText(buf.toString());

                setText(name);

                // important for imap
                // is this folder is not selectable 
                if (!item.getBoolean("selectable", true)) {
                    setFont(italicFont);
                    setForeground(Color.darkGray);
                }
            }
        }
        return this;
    }
    
    /**
     * Returns an icon suitable for the given folder.
     */
    public static Icon getFolderIcon(AbstractFolder node, boolean expanded) {
        if (node instanceof LocalRootFolder) {
            return localRootFolderIcon;
        } else if (node instanceof IMAPRootFolder) {
            return imapRootFolderIcon;
        } else if (node instanceof VirtualFolder) {
            return virtualFolderIcon;
        } else if (node instanceof MessageFolder) {
            MessageFolder folder = (MessageFolder)node;
            if (folder.isInboxFolder()) {
                return inboxIcon;
            } else if (folder.getUid() == 103) {
                // outbox
                return outboxIcon;
            } else if (folder.isTrashFolder()) {
                return trashIcon;
            }
        }
        return expanded ? expandedFolderIcon : collapsedFolderIcon;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4745.java