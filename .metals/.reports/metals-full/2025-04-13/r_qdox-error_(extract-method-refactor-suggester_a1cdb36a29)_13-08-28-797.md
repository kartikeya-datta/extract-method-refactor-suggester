error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9008.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9008.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9008.java
text:
```scala
p@@riority = Command.REALTIME_PRIORITY;

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
package org.columba.mail.gui.message.command;

import javax.swing.JOptionPane;

import org.columba.core.command.Command;
import org.columba.core.command.DefaultCommandReference;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.command.WorkerStatusController;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.main.MainInterface;
import org.columba.mail.command.FolderCommand;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.folder.FolderInconsistentException;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.gui.attachment.AttachmentSelectionHandler;
import org.columba.mail.gui.frame.MessageViewOwner;
import org.columba.mail.gui.frame.TableViewOwner;
import org.columba.mail.gui.message.MessageController;
import org.columba.mail.gui.table.command.ViewHeaderListCommand;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.MimeTree;

/**
 * @author Timo Stich (tstich@users.sourceforge.net)
 *  
 */
public class ViewMessageCommand extends FolderCommand {

	private MimeTree mimePartTree;

	private Flags flags;

	private MessageFolder srcFolder;

	private Object uid;

	/**
	 * Constructor for ViewMessageCommand.
	 * 
	 * @param references
	 */
	public ViewMessageCommand(FrameMediator frame,
			DefaultCommandReference[] references) {
		super(frame, references);

		//priority = Command.REALTIME_PRIORITY;
		commandType = Command.NORMAL_OPERATION;
	}

	/**
	 * @see org.columba.core.command.Command#updateGUI()
	 */
	public void updateGUI() throws Exception {
		
		AttachmentSelectionHandler h = ((AttachmentSelectionHandler) frameMediator
				.getSelectionManager().getHandler("mail.attachment"));

		// this is a hack to notify the attachment selection handler which
		// folder/uid is selected
		// TODO: use listener pattern instead
		h.setMessage(srcFolder, uid);

		MessageController messageController = ((MessageViewOwner) frameMediator)
				.getMessageController();
		// update attachment model
		messageController.getAttachmentController().setMimePartTree(
				mimePartTree);

		// display changes
		messageController.updateGUI();

		// if the message it not yet seen
		if (!(flags.getSeen())) {
			// restart timer which marks the message as read
			// after a user configurable time interval
			((TableViewOwner) frameMediator).getTableController()
					.getMarkAsReadTimer().restart(
							(FolderCommandReference) getReferences()[0]);
		}

	}

	/**
	 * @see org.columba.core.command.Command#execute(Worker)
	 */
	public void execute(WorkerStatusController wsc) throws Exception {
		// get command reference
		FolderCommandReference[] r = (FolderCommandReference[]) getReferences();
		
		// get selected folder
		srcFolder = (MessageFolder) r[0].getFolder();

		// register for status events
		((StatusObservableImpl) srcFolder.getObservable()).setWorker(wsc);
		
		// get selected message UID
		uid = r[0].getUids()[0];

		try {
			// get attachment structure
			mimePartTree = srcFolder.getMimePartTree(uid);

			// get flags
			flags = srcFolder.getFlags(uid);
		} catch (FolderInconsistentException ex) {
			Object[] options = new String[] { MailResourceLoader.getString("",
					"global", "ok").replaceAll("&", ""), };
			int result = JOptionPane.showOptionDialog(null, MailResourceLoader
					.getString("dialog", "error", "message_deleted"), "Error",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
					null, options, options[0]);

			MainInterface.processor.addOp(new ViewHeaderListCommand(
					getFrameMediator(), r));

			return;
		}

		// get messagecontroller of frame
		MessageController messageController = ((MessageViewOwner) frameMediator)
				.getMessageController();

		// if necessary decrypt/verify message
		FolderCommandReference[] newRefs = messageController.getPgpFilter().filter(srcFolder, uid);

		// pass work along to MessageController
		if( newRefs != null ) {
			srcFolder = (MessageFolder)newRefs[0].getFolder();
			uid = newRefs[0].getUids()[0];
			mimePartTree = srcFolder.getMimePartTree(uid);
		} 
		messageController.showMessage(srcFolder, uid);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9008.java