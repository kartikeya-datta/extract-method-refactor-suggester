error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3470.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3470.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3470.java
text:
```scala
n@@ame = NameParser.getInstance().parseDisplayName(from.getDisplayName());

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

import java.util.Date;

import javax.swing.JOptionPane;

import org.columba.api.command.ICommandReference;
import org.columba.api.command.IWorkerStatusController;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.selection.ISelectionListener;
import org.columba.api.selection.SelectionChangedEvent;
import org.columba.core.command.Command;
import org.columba.core.command.CommandCancelledException;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.command.Worker;
import org.columba.core.context.base.api.IStructureValue;
import org.columba.core.context.semantic.api.ISemanticContext;
import org.columba.core.util.NameParser;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.folder.FolderInconsistentException;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.headercache.SyncHeaderList;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.frame.MessageViewOwner;
import org.columba.mail.gui.message.IMessageController;
import org.columba.mail.gui.message.viewer.MarkAsReadTimer;
import org.columba.mail.gui.table.selection.TableSelectionChangedEvent;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.message.Flags;

/**
 * @author Timo Stich (tstich@users.sourceforge.net)
 * 
 */
public class ViewMessageCommand extends Command implements ISelectionListener {

	private Flags flags;

	private IMailbox srcFolder;

	private Object uid;

	private IFrameMediator mediator;

	private boolean updateGui;

	private IStructureValue value;

	private String subject;

	private String bodyText;

	private Date date;

	private NameParser.Name name;

	private Address from;

	/**
	 * Constructor for ViewMessageCommand.
	 * 
	 * @param references
	 */
	public ViewMessageCommand(IFrameMediator mediator,
			ICommandReference reference) {
		super(reference);

		this.mediator = mediator;
		priority = Command.REALTIME_PRIORITY;
		commandType = Command.NORMAL_OPERATION;

		updateGui = true;

		// Register as listener to the SelectionManger
		// to check for selection changes

		((MailFrameMediator) mediator).registerTableSelectionListener(this);

	}

	/**
	 * @see org.columba.api.command.Command#updateGUI()
	 */
	public void updateGUI() throws Exception {
		((MailFrameMediator) mediator).removeTableSelectionListener(this);

		// Update only if the selection did not change
		if (updateGui) {
			IMessageController messageController = ((MessageViewOwner) mediator)
					.getMessageController();

			// display changes
			messageController.updateGUI();

			
		}

		fillContext();
	}

	private void fillContext() {
		// create identity value
		IStructureValue identity = value.addChild(
				ISemanticContext.CONTEXT_NODE_IDENTITY,
				ISemanticContext.CONTEXT_NAMESPACE_CORE);
		if (name.toString() != null)
			identity.setString(ISemanticContext.CONTEXT_ATTR_DISPLAY_NAME,
					ISemanticContext.CONTEXT_NAMESPACE_CORE, name.toString());
		if (from.getMailAddress() != null)
			identity.setString(ISemanticContext.CONTEXT_ATTR_EMAIL_ADDRESS,
					ISemanticContext.CONTEXT_NAMESPACE_CORE, from
							.getMailAddress());
		if (name.getFirstName() != null)
			identity.setString(ISemanticContext.CONTEXT_ATTR_FIRST_NAME,
					ISemanticContext.CONTEXT_NAMESPACE_CORE, name
							.getFirstName());
		if (name.getLastName() != null)
			identity
					.setString(ISemanticContext.CONTEXT_ATTR_LAST_NAME,
							ISemanticContext.CONTEXT_NAMESPACE_CORE, name
									.getLastName());

		// create message value
		IStructureValue message = value.addChild(
				ISemanticContext.CONTEXT_NODE_MESSAGE,
				ISemanticContext.CONTEXT_NAMESPACE_CORE);
		message.setString(ISemanticContext.CONTEXT_ATTR_SUBJECT,
				ISemanticContext.CONTEXT_NAMESPACE_CORE, subject);
		message.setDate(ISemanticContext.CONTEXT_ATTR_DATE,
				ISemanticContext.CONTEXT_NAMESPACE_CORE, date);

		IMessageController messageController = ((MessageViewOwner) mediator)
				.getMessageController();

		bodyText = messageController.getText();

		message.setString(ISemanticContext.CONTEXT_ATTR_BODY_TEXT,
				ISemanticContext.CONTEXT_NAMESPACE_CORE, bodyText);

		// set value
		mediator.getSemanticContext().setValue(value);
	}

	/**
	 * @see org.columba.api.command.Command#execute(Worker)
	 */
	public void execute(IWorkerStatusController wsc) throws Exception {
		if (!updateGui)
			return;

		// get command reference
		IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();

		// get selected folder
		srcFolder = (IMailbox) r.getSourceFolder();

		// register for status events
		((StatusObservableImpl) srcFolder.getObservable()).setWorker(wsc);

		// get selected message UID
		uid = r.getUids()[0];

		if (!srcFolder.exists(uid)) {
			return;
		}

		try {
			// get flags
			flags = srcFolder.getFlags(uid);

			// get messagecontroller of frame
			IMessageController messageController = ((MessageViewOwner) mediator)
					.getMessageController();

			messageController.showMessage(srcFolder, uid);

			restartMarkAsReadTimer(flags, messageController, r);

			// fill semantic context
			prepareContextData();
		} catch (FolderInconsistentException e) {
			Object[] options = new String[] { MailResourceLoader.getString("",
					"global", "ok").replaceAll("&", ""), };
			int result = JOptionPane.showOptionDialog(null, MailResourceLoader
					.getString("dialog", "error", "message_deleted"), "Error",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null,
					null, null);

			if (result == JOptionPane.YES_OPTION) {
				// Synchronize the complete folder
				SyncHeaderList.sync((AbstractMessageFolder) srcFolder,
						srcFolder.getHeaderList());
			}

			throw new CommandCancelledException();
		}
	}

	private void prepareContextData() throws Exception {

		// create empty value
		value = mediator.getSemanticContext().createValue();

		// from email address
		from = (Address) srcFolder.getAttribute(uid, "columba.from");
		// parse
		name = NameParser.getInstance().parseDisplayName(from.toString());

		subject = (String) srcFolder.getAttribute(uid, "columba.subject");
		date = (Date) srcFolder.getAttribute(uid, "columba.date");

	}

	private void restartMarkAsReadTimer(Flags flags,
			IMessageController messageController, IMailFolderCommandReference r)
			throws Exception {

		if (flags == null)
			return;
		// if the message it not yet seen
		if (!flags.getSeen() && !srcFolder.isReadOnly()) {
			MarkAsReadTimer.getInstance().start(messageController, r);
		}
	}

	/**
	 * @see org.columba.api.selection.ISelectionListener#selectionChanged(org.columba.api.selection.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent e) {

		// old command-specific selection
		IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();

		// get selected folder
		IMailbox folder = (IMailbox) r.getSourceFolder();

		// get selected message UID
		Object[] uid = r.getUids();

		// new selection
		IMailFolder newFolder = ((TableSelectionChangedEvent) e).getFolder();
		Object[] newUid = ((TableSelectionChangedEvent) e).getUids();

		// abort if nothing selected
		if (folder == null)
			return;
		if (newUid == null || newUid.length == 0)
			return;

		// cancel command execution/updateGUI methods, if folder or message
		// selection
		// has been modified
		if (folder.getUid() != newFolder.getUid())
			updateGui = false;

		if (uid[0].equals(newUid[0]) == false)
			updateGui = false;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3470.java