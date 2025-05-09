error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3070.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3070.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3070.java
text:
```scala
i@@f (item.getString("property", "accessrights").equals("user")) {

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

import java.util.Observable;
import java.util.Observer;

import org.columba.core.action.AbstractColumbaAction;
import org.columba.core.config.DefaultItem;
import org.columba.core.config.IDefaultItem;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.gui.selection.SelectionChangedEvent;
import org.columba.core.gui.selection.ISelectionListener;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.tree.selection.TreeSelectionChangedEvent;


/**
 * @author redsolo
 */
public abstract class AbstractMoveFolderAction extends AbstractColumbaAction
    implements ISelectionListener, Observer {

    private AbstractFolder lastSelectedFolder;

    /**
     * @param frameMediator the frame controller.
     * @param name name of action.
     */
    public AbstractMoveFolderAction(FrameMediator frameMediator, String name) {
        super(frameMediator, name);

        ((MailFrameMediator) frameMediator).registerTreeSelectionListener(this);
        registerSortingObserver();
    }

    /**
     * Returns true if the action enables a folder with the specified index.
     * Implementing methods can rely on that the lastSelectedFolder is not null.
     * @param folderIndex the index of the folder.
     * @return true if it should enable the action; false otherwise.
     */
    protected abstract boolean isActionEnabledByIndex(int folderIndex);

    /**
     * Register for notifications when the Tree node changes.
     */
    private void registerSortingObserver() {
        XmlElement sortElement = MailConfig.getInstance().get("options").getElement("/options/gui/tree/sorting");
        if (sortElement == null) {
            XmlElement treeElement = MailConfig.getInstance().get("options").getElement("/options/gui/tree");
            if (treeElement == null) {
                treeElement = MailConfig.getInstance().get("options").getElement("/options/gui").addSubElement("tree");
            }
            sortElement = treeElement.addSubElement("sorting");
        }
        sortElement.addObserver(this);
    }

    /** {@inheritDoc} */
    public void update(Observable o, Object arg) {
        enableAction();
    }

    /**
     * Enables or disables the action.
     */
    private void enableAction() {
        XmlElement sortElement = MailConfig.getInstance().get("options").getElement("/options/gui/tree/sorting");

        IDefaultItem item = new DefaultItem(sortElement);
        boolean sorted = item.getBoolean("sorted");
        if (sorted) {
            setEnabled(false);
        } else {
            reenableActionFromSelectedFolder();
        }
    }

    /**
     * Enables or disables the action based on the last selected folder.
     */
    private void reenableActionFromSelectedFolder() {
        if (lastSelectedFolder == null) {
            setEnabled(false);
        } else {
            IFolderItem item = lastSelectedFolder.getConfiguration();

            if (item.get("property", "accessrights").equals("user")) {
                int index = lastSelectedFolder.getParent().getIndex(lastSelectedFolder);

                setEnabled(isActionEnabledByIndex(index));
            } else {
                setEnabled(false);
            }
        }
    }

    /**
     * @see org.columba.core.gui.util.ISelectionListener#selectionChanged(org.columba.core.gui.util.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent e) {
        if (((TreeSelectionChangedEvent) e).getSelected().length > 0) {
            AbstractFolder folder = ((TreeSelectionChangedEvent) e).getSelected()[0];
            if ((folder != null) && folder instanceof AbstractMessageFolder) {
                lastSelectedFolder = folder;
            } else {
                lastSelectedFolder = null;
            }
            enableAction();
        } else {
            setEnabled(false);
        }
    }

    /**
     * @return Returns the last selected folder.
     */
    protected AbstractFolder getLastSelectedFolder() {
        return lastSelectedFolder;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3070.java