error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4439.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4439.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4439.java
text:
```scala
.@@getDeleted());

//The contents of this file are subject to the Mozilla Public License Version
//1.1
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
//Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.mail.folder.command;

import java.io.ByteArrayInputStream;

import org.columba.core.command.NullWorkerStatusController;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.folder.AbstractFolderTest;
import org.columba.mail.folder.FolderTstHelper;
import org.columba.mail.folder.MailboxTstFactory;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.MailboxInfo;

/**
 * @author fdietz
 */
public class MarkMessageTest extends AbstractFolderTest {

    private Object uid;

    private ByteArrayInputStream inputStream;

    
    public MarkMessageTest(String arg0) {
        super(arg0);
    }
    
    /**
     * @param arg0
     */
    public MarkMessageTest(MailboxTstFactory factory, String arg0) {
        super(factory, arg0);
    }

    public void testMarkAsReadMessage() throws Exception {

        // create Command reference
        FolderCommandReference[] ref = new FolderCommandReference[1];
        ref[0] = new FolderCommandReference(getSourceFolder(),
                new Object[] {uid});
        ref[0].setMarkVariant(MarkMessageCommand.MARK_AS_READ);

        // create copy command
        MarkMessageCommand command = new MarkMessageCommand(ref);

        // execute command -> use mock object class as worker which does
        // nothing
        command.execute(NullWorkerStatusController.getInstance());

        Flags flags = getSourceFolder().getFlags(uid);

        assertEquals("message should be marked as read", true, flags.getSeen());

        MailboxInfo info = getSourceFolder().getMessageFolderInfo();
        assertEquals("one mark as read message should be in folder", 1, info
                .getExists()
                - info.getUnseen());

    }

    public void testMarkAsFlaggedMessage() throws Exception {

        // create Command reference
        FolderCommandReference[] ref = new FolderCommandReference[2];
        ref[0] = new FolderCommandReference(getSourceFolder(),
                new Object[] { uid});
        ref[0].setMarkVariant(MarkMessageCommand.MARK_AS_FLAGGED);

        // create copy command
        MarkMessageCommand command = new MarkMessageCommand(ref);

        // execute command -> use mock object class as worker which does
        // nothing
        command.execute(NullWorkerStatusController.getInstance());

        Flags flags = getSourceFolder().getFlags(uid);

        assertEquals("message should be marked as flagged", true, flags
                .getFlagged());

    }

    public void testMarkAsExpungedMessage() throws Exception {

        // create Command reference
        FolderCommandReference[] ref = new FolderCommandReference[2];
        ref[0] = new FolderCommandReference(getSourceFolder(),
                new Object[] { uid});
        ref[0].setMarkVariant(MarkMessageCommand.MARK_AS_EXPUNGED);

        // create copy command
        MarkMessageCommand command = new MarkMessageCommand(ref);

        // execute command -> use mock object class as worker which does
        // nothing
        command.execute(NullWorkerStatusController.getInstance());

        Flags flags = getSourceFolder().getFlags(uid);

        assertEquals("message should be marked as expunged", true, flags
                .getExpunged());

    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        // create folders, etc.
        super.setUp();

        // add message "0.eml" as inputstream to folder
        String input = FolderTstHelper.getString(0);
        System.out.println("input=" + input);
        // create stream from string
        inputStream = FolderTstHelper.getByteArrayInputStream(input);
        // add stream to folder
        uid = getSourceFolder().addMessage(inputStream);

    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        // close streams
        inputStream.close();

        // delete folders
        super.tearDown();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4439.java