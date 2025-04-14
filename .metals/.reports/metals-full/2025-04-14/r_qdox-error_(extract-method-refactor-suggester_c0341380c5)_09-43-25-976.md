error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6897.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6897.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6897.java
text:
```scala
"In-Reply-To",@@ "References", "X-Beenthere", "X-BeenThere"

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
package org.columba.mail.gui.composer.command;

import org.columba.core.command.DefaultCommandReference;
import org.columba.core.command.Worker;
import org.columba.core.xml.XmlElement;

import org.columba.mail.command.FolderCommand;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.composer.MessageBuilderHelper;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.Folder;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.mail.message.ColumbaMessage;

import org.columba.ristretto.coder.EncodedWord;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.LocalMimePart;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;
import org.columba.ristretto.message.io.CharSequenceSource;


/**
 * Reply to mailinglist.
 * <p>
 * Uses the X-BeenThere: headerfield to determine the To: address
 *
 * @author fdizet
 */
public class ReplyToMailingListCommand extends FolderCommand {
    protected ComposerController controller;
    protected ComposerModel model;

    /**
     * Constructor for ReplyToMailingListCommand.
     *
     * @param frameMediator
     * @param references
     */
    public ReplyToMailingListCommand(DefaultCommandReference[] references) {
        super(references);
    }

    public void updateGUI() throws Exception {
        // open composer frame
        controller = new ComposerController();

        // apply model
        controller.setComposerModel(model);

        // model->view update
        controller.updateComponents(true);
    }

    public void execute(Worker worker) throws Exception {
        // get selected folder
        Folder folder = (Folder) ((FolderCommandReference) getReferences()[0]).getFolder();

        // get first selected message
        Object[] uids = ((FolderCommandReference) getReferences()[0]).getUids();

        // create new message object
        ColumbaMessage message = new ColumbaMessage();

        //		get headerfields
        Header header = folder.getHeaderFields(uids[0],
                new String[] {
                    "Subject", "From", "To", "Reply-To", "Message-ID",
                    "In-Reply-To", "References", "X-BeenThere"
                });
        message.setHeader(header);

        // get mimeparts
        MimeTree mimePartTree = folder.getMimePartTree(uids[0]);
        message.setMimePartTree(mimePartTree);

        XmlElement html = MailConfig.getMainFrameOptionsConfig().getRoot()
                                    .getElement("/options/html");

        // Which Bodypart shall be shown? (html/plain)
        MimePart bodyPart = null;

        if (Boolean.valueOf(html.getAttribute("prefer")).booleanValue()) {
            bodyPart = mimePartTree.getFirstTextPart("html");
        } else {
            bodyPart = mimePartTree.getFirstTextPart("plain");
        }

        if (bodyPart == null) {
            bodyPart = new LocalMimePart(new MimeHeader(header));
            ((LocalMimePart) bodyPart).setBody(new CharSequenceSource(
                    "<No Message-Text>"));
        } else {
            bodyPart = folder.getMimePart(uids[0], bodyPart.getAddress());
        }

        message.setBodyPart(bodyPart);

        // create composer model
        model = new ComposerModel();

        // set character set
        bodyPart = message.getBodyPart();

        if (bodyPart != null) {
            String charset = bodyPart.getHeader().getContentParameter("charset");

            if (charset != null) {
                model.setCharsetName(charset);
            }
        }

        // set subject
        model.setSubject(MessageBuilderHelper.createReplySubject(header.get(
                    "Subject")));

        // decode To: headerfield
        String to = MessageBuilderHelper.createToMailinglist(header);

        if (to != null) {
            to = EncodedWord.decode(to).toString();
            model.setTo(to);

            // TODO: automatically add sender to addressbook
            // -> split to-headerfield, there can be more than only one
            // recipients!
            MessageBuilderHelper.addSenderToAddressbook(to);
        }

        // create In-Reply-To:, References: headerfields
        MessageBuilderHelper.createMailingListHeaderItems(header, model);

        // try to good guess the correct account
        Integer accountUid = null;

        if (folder.getAttribute(uids[0], "columba.accountuid") != null) {
            accountUid = (Integer) folder.getAttribute(uids[0],
                    "columba.accountuid");
        }

        String host = null;

        if (folder.getAttribute(uids[0], "columba.host") != null) {
            host = (String) folder.getAttribute(uids[0], "columba.host");
        }

        String address = header.get("To");
        AccountItem accountItem = MessageBuilderHelper.getAccountItem(accountUid,
                host, address);
        model.setAccountItem(accountItem);

        /*
         * original message is sent "inline" - model is setup according to the
         * type of the original message. NB: If the original message was plain
         * text, the message type seen here is always text. If the original
         * message contained html, the message type seen here will depend on
         * the "prefer html" option.
         */
        MimeHeader bodyHeader = message.getBodyPart().getHeader();

        if (bodyHeader.getMimeType().getSubtype().equals("html")) {
            model.setHtml(true);
        } else {
            model.setHtml(false);
        }

        // prepend "> " to every line of the bodytext
        String bodyText = MessageBuilderHelper.createQuotedBodyText(message.getBodyPart(),
                model.isHtml());

        if (bodyText == null) {
            bodyText = "[Error parsing bodytext]";
        }

        model.setBodyText(bodyText);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6897.java