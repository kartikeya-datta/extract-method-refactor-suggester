error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1101.java
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

package org.columba.mail.folder;

import org.columba.core.gui.util.NotifyDialog;
import org.columba.core.main.MainInterface;
import org.columba.core.plugin.PluginHandlerNotFoundException;
import org.columba.core.xml.XmlElement;

import org.columba.mail.config.FolderItem;
import org.columba.mail.main.MailInterface;
import org.columba.mail.plugin.FolderPluginHandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Factory for creating subfolders.
 * Implemented as a singelton. Use {@link #getInstance()}.
 *
 *
 * @author Timo Stich <tstich@users.sourceforge.net>
 */
public class FolderFactory {
    // Groups are separated by at least one WS character
    private static final Pattern groupPattern = Pattern.compile("([^\\s]+)\\s*");
    private static FolderFactory instance;
    private FolderPluginHandler handler;
    private XmlElement folderlistElement;

    // parent directory for mail folders
    // for example: ".columba/mail/"
    private String path = MailInterface.config.getConfigDirectory().getPath();

    protected FolderFactory() {
        // Get the handler
        try {
            handler = (FolderPluginHandler) MainInterface.pluginManager.getHandler(
                    "org.columba.mail.folder");
        } catch (PluginHandlerNotFoundException ex) {
            NotifyDialog d = new NotifyDialog();
            d.showDialog(ex);
        }

        // Get the parentNode
        folderlistElement = handler.getParent();
    }

    /**
 * Singleton - pattern
 *
 * @return the instance of the factory
 */
    public static FolderFactory getInstance() {
        if (instance == null) {
            instance = new FolderFactory();
        }

        return instance;
    }

    /**
 * Gets a list of all possible child foldertypes.
 *
 * @param parent
 * @return a list that contains Strings of foldertypes
 */
    public List getPossibleChilds(AbstractFolder parent) {
        List list = new LinkedList();

        // which parents are possible ?
        FolderItem item = parent.getFolderItem();
        String parentType = item.get("type");

        // the group of the given parent
        String parentGroup = getGroup(parentType);

        // iterate through all foldertypes to find suitable ones
        Iterator it = folderlistElement.getElements().iterator();

        while (it.hasNext()) {
            XmlElement next = (XmlElement) it.next();
            String possibleParents = next.getAttribute("possible_parents");

            if (possibleParents != null) {
                Matcher matcher = groupPattern.matcher(possibleParents);

                while (matcher.find()) {
                    if (matcher.group(1).equals(parentGroup)) {
                        list.add(next.getAttribute("name"));
                    }
                }
            }
        }

        return list;
    }

    /**
 * Creates the default child for the given parent.
 *
 * @param parent the parent folder
 * @return the childfolder
 * @throws Exception
 */
    public AbstractFolder createDefaultChild(AbstractFolder parent, String name)
        throws Exception {
        List possibleChilds = getPossibleChilds(parent);

        if (possibleChilds.size() > 0) {
            String childType = (String) possibleChilds.get(0);

            return createChild(parent, name, childType);
        } else {
            return null;
        }
    }

    /**
 * Creates a subfolder for the given folder with the given type.
 *
 * @param parent the parentfolder
 * @param childType the type of the child (e.g. CachedMHFolder )
 * @return the childfolder
 * @throws Exception
 */
    public AbstractFolder createChild(AbstractFolder parent, String name,
        String childType) throws Exception {
        AbstractFolder child = (AbstractFolder) handler.getPlugin(childType,
                new Object[] { name, childType, path });

        // Add child to parent
        parent.addSubfolder(child);

        return child;
    }

    private String getGroup(String parentType) {
        Iterator it = folderlistElement.getElements().iterator();

        while (it.hasNext()) {
            XmlElement next = (XmlElement) it.next();

            if (next.getAttribute("name").equals(parentType)) {
                return next.getAttribute("group");
            }
        }

        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1101.java