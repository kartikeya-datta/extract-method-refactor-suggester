error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3053.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3053.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3053.java
text:
```scala
S@@endableMessage message = outboxFolder.getSendableMessage(uids[i]);

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
package org.columba.mail.smtp.command;

import org.columba.core.command.DefaultCommandReference;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.command.Worker;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.main.MainInterface;

import org.columba.mail.command.FolderCommand;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.composer.SendableMessage;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.Folder;
import org.columba.mail.folder.command.MoveMessageCommand;
import org.columba.mail.folder.outbox.OutboxFolder;
import org.columba.mail.folder.outbox.SendListManager;
import org.columba.mail.main.MailInterface;
import org.columba.mail.smtp.SMTPServer;
import org.columba.mail.util.MailResourceLoader;

import org.columba.ristretto.smtp.SMTPException;

import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;


/**
 * @author fdietz
 *
 * Send all messages in folder Outbox
 *
 */
public class SendAllMessagesCommand extends FolderCommand {
    protected SendListManager sendListManager = new SendListManager();
    protected OutboxFolder outboxFolder;

    /**
     * Constructor for SendAllMessagesCommand.
     *
     *
     * @param frameMediator
     * @param references
     */
    public SendAllMessagesCommand(FrameMediator frameMediator,
        DefaultCommandReference[] references) {
        super(frameMediator, references);
    }

    /**
     * @see org.columba.core.command.Command#execute(Worker)
     */
    public void execute(Worker worker) throws Exception {
        FolderCommandReference[] r = (FolderCommandReference[]) getReferences();

        // display status message
        worker.setDisplayText(MailResourceLoader.getString("statusbar",
                "message", "send_message"));

        // get Outbox folder from reference
        outboxFolder = (OutboxFolder) r[0].getFolder();

        // get UID list of messages
        Object[] uids = outboxFolder.getUids();

        // save every message in a list
        for (int i = 0; i < uids.length; i++) {
            if (outboxFolder.exists(uids[i]) == true) {
                SendableMessage message = (SendableMessage) outboxFolder.getMessage(uids[i]);
                sendListManager.add(message);
            }
        }

        int actAccountUid = -1;
        List sentList = new Vector();
        boolean open = false;
        SMTPServer smtpServer = null;
        Folder sentFolder = null;

        // send all messages 
        while (sendListManager.hasMoreMessages()) {
            SendableMessage message = sendListManager.getNextMessage();

            // get account information from message
            if (message.getAccountUid() != actAccountUid) {
                actAccountUid = message.getAccountUid();

                AccountItem accountItem = MailConfig.getAccountList().uidGet(actAccountUid);

                // Sent folder
                sentFolder = (Folder) MailInterface.treeModel.getFolder(Integer.parseInt(
                            accountItem.getSpecialFoldersItem().get("sent")));

                // open connection to SMTP server
                smtpServer = new SMTPServer(accountItem);

                open = smtpServer.openConnection();

                //				show interest on status information
                ((StatusObservableImpl) smtpServer.getObservable()).setWorker(worker);
            }

            // if success, send message
            if (open) {
                try {
                    smtpServer.sendMessage(message, worker);

                    sentList.add(message.getHeader().get("columba.uid"));
                } catch (SMTPException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Error while sending", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // we are done - clear status text with a delay
        // (if this is not done, the initial text will stay in 
        // case no messages were sent)
        worker.clearDisplayTextWithDelay();

        // move all successfully send messages to the Sent folder
        if (sentList.size() > 0) {
            moveToSentFolder(sentList, sentFolder);
            sentList.clear();
        }
    }

    /**
     *
     * Move all send messages to the Sent folder
     *
     * @param v                list of SendableMessage objects
     *
     * @param sentFolder        Sent folder
     */
    protected void moveToSentFolder(List v, Folder sentFolder) {
        FolderCommandReference[] r = new FolderCommandReference[2];

        // source folder
        r[0] = new FolderCommandReference(outboxFolder, v.toArray());

        // destination folder
        r[1] = new FolderCommandReference(sentFolder);

        // start move command
        MoveMessageCommand c = new MoveMessageCommand(r);

        MainInterface.processor.addOp(c);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3053.java