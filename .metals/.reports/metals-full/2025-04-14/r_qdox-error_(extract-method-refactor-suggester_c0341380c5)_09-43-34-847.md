error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1800.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1800.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1800.java
text:
```scala
S@@hutdownManager.getInstance().register(new Runnable() {

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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.tree;

import java.util.Enumeration;
import java.util.MissingResourceException;

import javax.swing.tree.DefaultTreeModel;

import org.columba.core.config.Config;
import org.columba.core.gui.util.NotifyDialog;
import org.columba.core.plugin.IExtension;
import org.columba.core.plugin.PluginManager;
import org.columba.core.plugin.exception.PluginHandlerNotFoundException;
import org.columba.core.shutdown.ShutdownManager;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.FolderItem;
import org.columba.mail.config.FolderXmlConfig;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.folder.Root;
import org.columba.mail.folder.imap.IMAPRootFolder;
import org.columba.mail.folder.temp.TempFolder;
import org.columba.mail.gui.tree.util.TreeNodeList;
import org.columba.mail.plugin.FolderExtensionHandler;
import org.columba.mail.util.MailResourceLoader;


/**
 * @author freddy
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of
 * type comments go to Window>Preferences>Java>Code Generation.
 */
public class FolderTreeModel extends DefaultTreeModel {
    protected FolderXmlConfig folderXmlConfig;
    protected TempFolder tempFolder;
    private final Class[] FOLDER_ITEM_ARG = new Class[] { FolderItem.class };

    private static FolderTreeModel instance = new FolderTreeModel(MailConfig.getInstance().getFolderConfig());
    
    public FolderTreeModel(FolderXmlConfig folderConfig) {
        super(new Root(folderConfig.getRoot().getElement("tree")));
        this.folderXmlConfig = folderConfig;

        // create temporary folder in "<your-config-folder>/mail/"
        tempFolder = new TempFolder(Config.getInstance().getConfigDirectory() +
                "/mail/");

        createDirectories(((AbstractFolder) getRoot()).getConfiguration().getRoot(),
            (AbstractFolder) getRoot());
        
        // register at shutdownmanager
        // -> when closing Columba, this will automatically save all folder data
        ShutdownManager.getShutdownManager().register(new Runnable() {
            public void run() {
                saveFolder((AbstractFolder) getRoot());
            }

            protected void saveFolder(AbstractFolder parentFolder) {
                AbstractFolder child;

                for (Enumeration e = parentFolder.children();
                        e.hasMoreElements();) {
                    child = (AbstractFolder) e.nextElement();

                    if (child instanceof AbstractMessageFolder) {
                        try {
                            ((AbstractMessageFolder) child).save();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    saveFolder(child);
                }
            }
        });
    }
    
    public static FolderTreeModel getInstance() {
    	return instance;
    }

    public void createDirectories(XmlElement parentTreeNode,
        AbstractFolder parentFolder) {
        int count = parentTreeNode.count();
        XmlElement child;

        if (count > 0) {
            for (int i = 0; i < count; i++) {
                child = parentTreeNode.getElement(i);

                String name = child.getName();

                if (name.equals("folder")) {
                    AbstractFolder folder = add(child, parentFolder);

                    if (folder != null) {
                        createDirectories(child, folder);
                    }
                }
            }
        }
    }

    public AbstractFolder add(XmlElement childNode, AbstractFolder parentFolder) {
        FolderItem item = new FolderItem(childNode);

        if (item == null) {
            return null;
        }

        // i18n stuff
        String name = null;

        //XmlElement.printNode(item.getRoot(), "");
        int uid = item.getInteger("uid");

        try {
            if (uid == 100) {
                name = MailResourceLoader.getString("tree", "localfolders");
            } else if (uid == 101) {
                name = MailResourceLoader.getString("tree", "inbox");
            } else if (uid == 102) {
                name = MailResourceLoader.getString("tree", "drafts");
            } else if (uid == 103) {
                name = MailResourceLoader.getString("tree", "outbox");
            } else if (uid == 104) {
                name = MailResourceLoader.getString("tree", "sent");
            } else if (uid == 105) {
                name = MailResourceLoader.getString("tree", "trash");
            } else if (uid == 106) {
                name = MailResourceLoader.getString("tree", "search");
            } else if (uid == 107) {
                name = MailResourceLoader.getString("tree", "templates");
            } else {
                name = item.getString("property", "name");
            }

            item.setString("property", "name", name);
        } catch (MissingResourceException ex) {
            name = item.getString("property", "name");
        }

        // now instanciate the folder classes
        String type = item.get("type");
        FolderExtensionHandler handler = null;

        try {
            handler = (FolderExtensionHandler) PluginManager.getInstance().getHandler(
            		FolderExtensionHandler.NAME);
        } catch (PluginHandlerNotFoundException ex) {
            NotifyDialog d = new NotifyDialog();
            d.showDialog(ex);
        }

        // parent directory for mail folders
        // for example: ".columba/mail/"
        String path = Config.getInstance().getConfigDirectory() + "/mail/";
        Object[] args = { item, path };
        AbstractFolder folder = null;

        try {
        	IExtension extension = handler.getExtension(type);
        	
            folder = (AbstractFolder) extension.instanciateExtension(args);
            parentFolder.add(folder);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return folder;
    }

    public AbstractFolder getFolder(int uid) {
        AbstractFolder root = (AbstractFolder) getRoot();

        for (Enumeration e = root.breadthFirstEnumeration();
                e.hasMoreElements();) {
            AbstractFolder node = (AbstractFolder) e.nextElement();
            int id = node.getUid();

            if (uid == id) {
                return node;
            }
        }

        return null;
    }

    public AbstractFolder getTrashFolder() {
        return getFolder(105);
    }

    public AbstractFolder getImapFolder(int accountUid) {
        AbstractFolder root = (AbstractFolder) getRoot();

        for (Enumeration e = root.breadthFirstEnumeration();
                e.hasMoreElements();) {
            AbstractFolder node = (AbstractFolder) e.nextElement();
            IFolderItem item = node.getConfiguration();

            if (item == null) {
                continue;
            }

            //if (item.get("type").equals("IMAPRootFolder")) {
            if (node instanceof IMAPRootFolder) {
                int account = item.getInteger("account_uid");

                if (account == accountUid) {
                    int uid = item.getInteger("uid");

                    return getFolder(uid);
                }
            }
        }

        return null;
    }

    public AbstractFolder getFolder(TreeNodeList list) {
        AbstractFolder parentFolder = (AbstractFolder) getRoot();

        if ((list == null) || (list.count() == 0)) {
            return parentFolder;
        }

        AbstractFolder child = parentFolder;

        for (int i = 0; i < list.count(); i++) {
            String str = list.get(i);
            child = findFolder(child, str);
        }

        return child;
    }

    public AbstractFolder findFolder(AbstractFolder parentFolder, String str) {
        AbstractFolder child;

        for (Enumeration e = parentFolder.children(); e.hasMoreElements();) {
            child = (AbstractFolder) e.nextElement();

            if (child.getName().equals(str)) {
                return child;
            }
        }

        return null;
    }


    public TempFolder getTempFolder() {
        return tempFolder;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1800.java