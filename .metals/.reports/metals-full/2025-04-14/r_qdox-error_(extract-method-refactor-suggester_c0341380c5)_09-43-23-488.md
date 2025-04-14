error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8920.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8920.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8920.java
text:
```scala
C@@olumbaLogger.log.info("src=" + srcFolder + " dest=" + destFolder);

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
package org.columba.mail.folder.command;

import org.columba.core.command.Command;
import org.columba.core.command.DefaultCommandReference;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.command.Worker;
import org.columba.core.logging.ColumbaLogger;

import org.columba.mail.command.FolderCommand;
import org.columba.mail.command.FolderCommandAdapter;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.folder.Folder;
import org.columba.mail.gui.frame.TableUpdater;
import org.columba.mail.gui.table.model.TableModelChangedEvent;
import org.columba.mail.main.MailInterface;
import org.columba.mail.util.MailResourceLoader;

import java.io.IOException;

import java.text.MessageFormat;

import javax.swing.JOptionPane;


/**
 * Copy a set of messages from a source to a destination
 * folder.
 * <p>
 * A dialog asks the user the destination folder.
 *
 * @author fdietz
 *
 */
public class CopyMessageCommand extends FolderCommand {
    protected FolderCommandAdapter adapter;
    protected Folder destFolder;

    /**
     * Constructor for CopyMessageCommand.
     * @param frameMediator
     * @param references
     */
    public CopyMessageCommand(DefaultCommandReference[] references) {
        super(references);

        commandType = Command.UNDOABLE_OPERATION;
    }

    public void updateGUI() throws Exception {
        // notify table of changes
        TableModelChangedEvent ev = new TableModelChangedEvent(TableModelChangedEvent.UPDATE,
                destFolder);

        TableUpdater.tableChanged(ev);

        // notify treemodel
        MailInterface.treeModel.nodeChanged(destFolder);
    }

    /**
     * @see org.columba.core.command.Command#execute(Worker)
     */
    public void execute(Worker worker) throws Exception {
        // get references
        FolderCommandReference[] references = (FolderCommandReference[]) getReferences();

        // use wrapper class
        adapter = new FolderCommandAdapter(references);

        // get source references
        FolderCommandReference[] r = adapter.getSourceFolderReferences();

        // get destination foldedr
        destFolder = adapter.getDestinationFolder();

        // for each message
        for (int i = 0; (i < r.length) && !worker.cancelled(); i++) {
            Object[] uids = r[i].getUids();

            // get source folder
            Folder srcFolder = (Folder) r[i].getFolder();

            // register for status events
            ((StatusObservableImpl) srcFolder.getObservable()).setWorker(worker);

            // setting lastSelection for srcFolder to null
            srcFolder.setLastSelection(null);

            ColumbaLogger.log.debug("src=" + srcFolder + " dest=" + destFolder);

            // update status message
            worker.setDisplayText(MessageFormat.format(
                    MailResourceLoader.getString("statusbar", "message",
                        "copy_messages"), new Object[] { destFolder.getName() }));

            // initialize progress bar with total number of messages
            worker.setProgressBarMaximum(uids.length);

            if (srcFolder.getRootFolder().equals(destFolder.getRootFolder())) {
                srcFolder.innerCopy(destFolder, uids);
            } else {
                for (int j = 0; (j < uids.length) && !worker.cancelled();
                        j++) {
                    // if message exists in sourcefolder
                    // FIXME: Check whether the email already
                    // already exists in the destination folder
                    if (srcFolder.exists(uids[j])) {
                        continue;
                    }

                    try {
                        // add source to destination folder
                        destFolder.addMessage(srcFolder.getMessageSourceStream(
                                uids[j]));
                    } catch (IOException ioe) {
                        String[] options = new String[] {
                                MailResourceLoader.getString("statusbar",
                                    "message", "err_copy_messages_retry"),
                                MailResourceLoader.getString("statusbar",
                                    "message", "err_copy_messages_ignore"),
                                MailResourceLoader.getString("", "global",
                                    "cancel")
                            };

                        switch (JOptionPane.showOptionDialog(null,
                            MailResourceLoader.getString("statusbar",
                                "message", "err_copy_messages_msg"),
                            MailResourceLoader.getString("statusbar",
                                "message", "err_copy_messages_title"),
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.ERROR_MESSAGE, null, options, options[0])) {
                        case JOptionPane.YES_OPTION:

                            //retry copy
                            j--;

                            break;

                        case JOptionPane.CANCEL_OPTION:
                            worker.cancel();

                        default:

                            continue;
                        }
                    }

                    // update progress bar
                    worker.setProgressBarValue(j);
                }
            }

            //reset progress bar
            worker.setProgressBarValue(0);
        }

        if (worker.cancelled()) {
            worker.setDisplayText(MailResourceLoader.getString("statusbar",
                    "message", "copy_messages_cancelled"));
        } else {
            // We are done - clear the status message with a delay
            worker.clearDisplayTextWithDelay();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8920.java