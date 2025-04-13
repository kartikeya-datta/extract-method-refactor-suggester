error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9504.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9504.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9504.java
text:
```scala
S@@tring quotedBodyText = createQuotedBody(bodyPart.getHeader(), folder, uids, address);

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

import java.io.IOException;
import java.io.InputStream;

import org.columba.core.command.CommandCancelledException;
import org.columba.core.command.DefaultCommandReference;
import org.columba.core.command.WorkerStatusController;
import org.columba.core.io.StreamUtils;
import org.columba.core.xml.XmlElement;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.folder.command.MarkMessageCommand;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.mail.gui.config.template.ChooseTemplateDialog;
import org.columba.mail.main.MailInterface;
import org.columba.mail.message.HeaderList;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;

/**
 * Opens a dialog to ask the user which template to use
 * 
 * @author fdietz
 */
public class ReplyWithTemplateCommand extends ReplyCommand {

    /**
     * @param references
     */
    public ReplyWithTemplateCommand(DefaultCommandReference[] references) {
        super(references);
    }

    public void execute(WorkerStatusController worker) throws Exception {
        // create composer model
        model = new ComposerModel();

        // get selected folder
        MessageFolder folder = (MessageFolder) ((FolderCommandReference) getReferences()[0])
                .getFolder();

        // get first selected message
        Object[] uids = ((FolderCommandReference) getReferences()[0]).getUids();

        // mark message as answered
        FolderCommandReference[] ref = new FolderCommandReference[1];
        ref[0] = new FolderCommandReference(folder, uids);
        ref[0].setMarkVariant(MarkMessageCommand.MARK_AS_ANSWERED);
        MarkMessageCommand c = new MarkMessageCommand(ref);
        c.execute(worker);

        // setup to, references and account
        initHeader(folder, uids);

        // get mimeparts
        MimeTree mimePartTree = folder.getMimePartTree(uids[0]);

        XmlElement html = MailInterface.config.getMainFrameOptionsConfig()
                .getRoot().getElement("/options/html");

        // Which Bodypart shall be shown? (html/plain)
        MimePart bodyPart = null;

        if (Boolean.valueOf(html.getAttribute("prefer")).booleanValue()) {
            bodyPart = mimePartTree.getFirstTextPart("html");
        } else {
            bodyPart = mimePartTree.getFirstTextPart("plain");
        }

        if (bodyPart != null) {
            // setup charset and html
            initMimeHeader(bodyPart);

            StringBuffer bodyText;
            Integer[] address = bodyPart.getAddress();

            String quotedBodyText = createQuotedBody(folder, uids, address);

            // get answer from template
            String templateBody = getTemplateBody();

            model.setBodyText(quotedBodyText + templateBody);
        } else {
            model.setBodyText(getTemplateBody());
        }
    }

    private String getTemplateBody() throws Exception,
            CommandCancelledException, IOException {
        // template folder has uid=107
        MessageFolder templateFolder = (MessageFolder) MailInterface.treeModel
                .getFolder(107);

        // retrieve headerlist of tempate folder
        HeaderList list = templateFolder.getHeaderList();

        // choose template
        ChooseTemplateDialog d = new ChooseTemplateDialog(getFrameMediator().getView().getFrame(), list);

        Object uid = null;

        if (d.isResult()) {
            // user pressed OK
            uid = d.getUid();
        } else {
            throw new CommandCancelledException();
        }

        // get bodytext of template message
        MimeTree tree = templateFolder.getMimePartTree(uid);

        // *20030926, karlpeder* Added html support
        //MimePart mp = tree.getFirstTextPart("plain");
        MimePart mp;

        if (model.isHtml()) {
            mp = tree.getFirstTextPart("html");
        } else {
            mp = tree.getFirstTextPart("text");
        }

        InputStream bodyStream = templateFolder.getMimePartBodyStream(uid, mp
                .getAddress());

        String body = StreamUtils.readInString(bodyStream).toString();

        bodyStream.close();
        return body;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9504.java