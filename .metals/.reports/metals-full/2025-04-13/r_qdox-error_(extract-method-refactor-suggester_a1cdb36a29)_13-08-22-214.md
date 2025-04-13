error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3293.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3293.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3293.java
text:
```scala
A@@ddressbookInterface.config.getAddressbookConfig();

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
package org.columba.addressbook.gui.action;

import org.columba.addressbook.folder.AddressbookFolder;
import org.columba.addressbook.folder.ContactCard;
import org.columba.addressbook.folder.GroupListCard;
import org.columba.addressbook.folder.HeaderItem;
import org.columba.addressbook.folder.HeaderItemList;
import org.columba.addressbook.gui.EditGroupDialog;
import org.columba.addressbook.gui.dialog.contact.ContactDialog;
import org.columba.addressbook.gui.frame.AddressbookFrameController;
import org.columba.addressbook.util.AddressbookResourceLoader;

import org.columba.core.action.AbstractColumbaAction;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.gui.util.ImageLoader;

import java.awt.event.ActionEvent;


/**
 * @author frd
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EditPropertiesAction extends AbstractColumbaAction {
    public EditPropertiesAction(FrameMediator frameController) {
        super(frameController,
            AddressbookResourceLoader.getString("menu", "mainframe",
                "menu_file_properties"));

        // tooltip text
        putValue(SHORT_DESCRIPTION,
            AddressbookResourceLoader.getString("menu", "mainframe",
                "menu_file_properties_tooltip").replaceAll("&", ""));

        putValue(TOOLBAR_NAME,
            AddressbookResourceLoader.getString("menu", "mainframe",
                "menu_file_properties_toolbar"));

        // icons
        putValue(SMALL_ICON, ImageLoader.getSmallImageIcon("stock_edit-16.png"));
        putValue(LARGE_ICON, ImageLoader.getImageIcon("stock_edit.png"));
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent evt) {
        AddressbookFrameController addressbookFrameController = (AddressbookFrameController) frameMediator;

        Object uid = addressbookFrameController.getTable().getView()
                                               .getSelectedUid();

        if (uid == null) {
            return;
        }

        HeaderItem item = addressbookFrameController.getTable().getView()
                                                    .getSelectedItem();

        /*
        AddressbookXmlConfig config =
                AddressbookConfig.getAddressbookConfig();
        */
        AddressbookFolder folder = (AddressbookFolder) addressbookFrameController.getTree()
                                                                                 .getView()
                                                                                 .getSelectedFolder();

        if (item.isContact()) {
            ContactCard card = (ContactCard) folder.get(uid);
            ContactDialog dialog = new ContactDialog(addressbookFrameController.getView());

            dialog.updateComponents(card, true);
            dialog.setVisible(true);

            if (dialog.getResult()) {
                dialog.updateComponents(card, false);
                folder.modify(card, uid);

                addressbookFrameController.getTable().getView().setFolder(folder);
            }
        } else {
            GroupListCard card = (GroupListCard) folder.get(uid);

            EditGroupDialog dialog = new EditGroupDialog(addressbookFrameController.getView(),
                    addressbookFrameController, null);


            Object[] uids = card.getUids();
            HeaderItemList members = folder.getHeaderItemList(uids);
            dialog.updateComponents(card, members, true);
			dialog.setVisible(true);

            if (dialog.getResult()) {
                dialog.updateComponents(card, null, false);
                folder.modify(card, uid);
                addressbookFrameController.getTable().getView().setFolder(folder);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3293.java