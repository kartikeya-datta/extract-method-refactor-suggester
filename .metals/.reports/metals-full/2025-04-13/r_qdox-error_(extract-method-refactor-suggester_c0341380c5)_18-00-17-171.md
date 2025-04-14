error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9716.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9716.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9716.java
text:
```scala
M@@ailResourceLoader.getString("dialog","error",recipient);

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
package org.columba.mail.smtp.command;

import java.text.MessageFormat;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.columba.core.command.DefaultCommandReference;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.command.WorkerStatusController;
import org.columba.core.main.MainInterface;
import org.columba.mail.command.ComposerCommandReference;
import org.columba.mail.command.FolderCommand;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.composer.MessageComposer;
import org.columba.mail.composer.SendableMessage;
import org.columba.mail.config.AccountItem;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.folder.command.MarkMessageCommand;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.mail.gui.composer.command.SaveMessageCommand;
import org.columba.mail.gui.util.SendMessageDialog;
import org.columba.mail.main.MailInterface;
import org.columba.mail.pgp.CancelledException;
import org.columba.mail.smtp.SMTPServer;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.message.Flags;
import org.waffel.jscf.JSCFException;

/**
 * 
 * This command is started when the user sends the message after creating it in
 * the composer window.
 * <p>
 * After closing the compser window, it will open a little dialog showing the
 * progress of sending the message.
 * <p>
 * If the user cancelles sending, the composer window will be opened again.
 * 
 * @author fdietz
 */
public class SendMessageCommand extends FolderCommand {
	private SendMessageDialog sendMessageDialog;

	private boolean showComposer = false;

	private ComposerController composerController;

	/**
	 * Constructor for SendMessageCommand.
	 * 
	 * @param frameMediator
	 * @param references
	 */
	public SendMessageCommand(DefaultCommandReference[] references) {
		super(references);
	}

	private void showInvalidRecipientMessage(String recipient)
	{
	  
	  String message = 
	    MailResourceLoader.getString("dialog","error","invalid_recipient");
	  
	  String title = 
	    MailResourceLoader.getString("dialog","error","invalid_recipient_title");
	  message = MessageFormat.format(message,new Object[]{recipient});
		JOptionPane.showMessageDialog(null,
		                             	message,
		                             	title,
		                             	JOptionPane.ERROR_MESSAGE);
	}
	
	/*
	 * validate command parameters.
	 * At the moment only checks if there are any invalid email addresses
	 * 
	 * */
	private boolean validArguments(ComposerCommandReference[] references)
	{

	  String invalidRecipient = null;		
		for(int i=0;i<references.length;i++)
		{
		  
			invalidRecipient = references[i].getComposerController().getModel()
														.getInvalidRecipients();
			
		  if (invalidRecipient != null)
			{

		    //it would be really nice to highlight the invalid recipient
				showInvalidRecipientMessage(invalidRecipient);
				//AFAIK, there's no need to set showComposer to true because
				//composer window is already displayed
				//	open composer view
				//showComposer = true;
				
				return false;

			}
			
		}
		
		return true;
		
	}
	
	/**
	 * @see org.columba.core.command.Command#execute(Worker)
	 */
	public void execute(WorkerStatusController worker) throws Exception {

	  ComposerCommandReference[] r = (ComposerCommandReference[]) getReferences();

		if (!validArguments(r)) 
		  return;
		
		//	display status message
		worker.setDisplayText(MailResourceLoader.getString("statusbar",
				"message", "send_message_compose"));

		// get composer controller
		// -> get all the account information from the controller
		composerController = r[0].getComposerController();

		// close composer view
		if (composerController.getView().getFrame() != null) {
			composerController.getView().getFrame().setVisible(false);
		}

		sendMessageDialog = new SendMessageDialog(worker);

		ComposerModel model = ((ComposerModel) composerController.getModel());

		AccountItem item = model.getAccountItem();

		// sent folder
		MessageFolder sentFolder = (MessageFolder) MailInterface.treeModel
				.getFolder(item.getSpecialFoldersItem().getInteger("sent"));

		// get the SendableMessage object
		SendableMessage message = null;

		try {
			// compose the message suitable for sending
			message = new MessageComposer(model).compose(worker);
		} catch (JSCFException e1) {
			if (e1 instanceof CancelledException) {
				// user cancelled sending operation
				// open composer view
				showComposer = true;

				return;
			} else {
				JOptionPane.showMessageDialog(null, e1.getMessage());

				//	open composer view
				showComposer = true;

				return;
			}
		}

		// display status message
		worker.setDisplayText(MailResourceLoader.getString("statusbar",
				"message", "send_message_connect"));

		// open connection
		SMTPServer server = new SMTPServer(item);

		/*
		 * try { server.openConnection(); } catch (UnknownHostException e2) {
		 * Object[] options = new String[] { MailResourceLoader.getString("",
		 * "global", "ok").replaceAll("&", "") }; int result =
		 * JOptionPane.showOptionDialog(null,
		 * MailResourceLoader.getString("dialog", "error", "unknown_host"),
		 * e2.getLocalizedMessage(), JOptionPane.DEFAULT_OPTION,
		 * JOptionPane.ERROR_MESSAGE, null, options, options[0]);
		 * 
		 * showComposer = true;
		 * 
		 * throw new CommandCancelledException(); } catch (IOException e2) {
		 * e2.printStackTrace();
		 * 
		 * showComposer = true; throw new CommandCancelledException(); }
		 */

		try {
			server.openConnection();
		} catch (Exception e2) {

			//e2.printStackTrace();
			showComposer = true;

			throw e2;
		}

		// successfully connected and autenthenticated to SMTP server
		try {
			// display status message
			worker.setDisplayText(MailResourceLoader.getString("statusbar",
					"message", "send_message"));

			// send message
			server.sendMessage(message, worker);

			// close composer frame
			composerController.close();

			// mark as read
			Flags flags = new Flags();
			flags.setSeen(true);
			message.getHeader().setFlags(flags);

			// save message in Sent folder
			ComposerCommandReference[] ref = new ComposerCommandReference[1];
			ref[0] = new ComposerCommandReference(composerController,
					sentFolder);
			ref[0].setMessage(message);

			SaveMessageCommand c = new SaveMessageCommand(ref);

			MainInterface.processor.addOp(c);

			// -> get source reference of message
			// when replying this is the original sender's message
			// you selected and replied to 
			FolderCommandReference[] ref2 = model.getSourceReference();
			if (ref2 != null) {
				// mark message as answered
				ref2[0].setMarkVariant(MarkMessageCommand.MARK_AS_ANSWERED);
				MarkMessageCommand c1 = new MarkMessageCommand(ref2);
				MainInterface.processor.addOp(c1);
			}

			//	display status message
			worker.setDisplayText(MailResourceLoader.getString("statusbar",
					"message", "send_message_closing"));

			// close connection to server
			server.closeConnection();

			// display status message
			worker.setDisplayText(MailResourceLoader.getString("statusbar",
					"message", "send_message_success"));
		} /*
		   * catch (SMTPException e) { JOptionPane.showMessageDialog(null,
		   * e.getMessage(), "Error while sending", JOptionPane.ERROR_MESSAGE);
		   *  // open composer view showComposer = true; }
		   */catch (Exception e) {
			//e.printStackTrace();

			// open composer view
			showComposer = true;

			throw e;
		}
	}

	public void updateGUI() throws Exception {
	  
	  //can no longer assume that sendMessageDialog has been displayed
	  if (sendMessageDialog != null)
	  {
			// close send message dialog
			sendMessageDialog.setVisible(false);
	  }

		if (showComposer == true
				&& composerController.getView().getFrame() != null) {
			// re-open composer view
			composerController.getView().getFrame().setVisible(true);
			composerController.getView().getFrame().requestFocus();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9716.java