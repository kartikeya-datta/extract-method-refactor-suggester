error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4345.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4345.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4345.java
text:
```scala
i@@f( action != null) action.setEnabled(true);

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

import java.awt.Toolkit;

import javax.swing.Action;

import org.columba.core.command.Command;
import org.columba.core.command.ICommandReference;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.command.WorkerStatusController;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.config.ImapItem;
import org.columba.mail.folder.imap.IMAPFolder;
import org.columba.mail.folder.imap.IMAPRootFolder;

/**
 * Check for new messages in IMAPFolder.
 * 
 * 
 * @author fdietz
 */
public class CheckForNewMessagesCommand extends Command {

	IMAPFolder inboxFolder;

	private Action action;

	public CheckForNewMessagesCommand(Action action, ICommandReference reference) {
		super(reference);
		this.action = action;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.core.command.Command#execute(org.columba.core.command.Worker)
	 */
	public void execute(WorkerStatusController worker) throws Exception {
		// get references
		MailFolderCommandReference r = (MailFolderCommandReference) getReference();

		// get IMAP rootfolder
		IMAPRootFolder srcFolder = (IMAPRootFolder) r.getSourceFolder();

		// register for status events
		((StatusObservableImpl) srcFolder.getObservable()).setWorker(worker);

		// we only check inbox
		inboxFolder = (IMAPFolder) srcFolder.findChildWithName("Inbox", false);

		// Find old numbers
		int total = inboxFolder.getMessageFolderInfo().getExists();
		int recent = inboxFolder.getMessageFolderInfo().getRecent();
		int unseen = inboxFolder.getMessageFolderInfo().getUnseen();

		// check for new headers
		inboxFolder.synchronizeHeaderlist();

		// Get the new numbers
		int newTotal = inboxFolder.getMessageFolderInfo().getExists();
		int newRecent = inboxFolder.getMessageFolderInfo().getRecent();
		int newUnseen = inboxFolder.getMessageFolderInfo().getUnseen();

		// ALP 04/29/03
		// Call updageGUI() if anything has changed
		if ((newRecent != recent) || (newTotal != total)
 (newUnseen != unseen)) {

			ImapItem item = srcFolder.getAccountItem().getImapItem();

			if ((newTotal != total) && (item.getBoolean("enable_sound"))) {
				// the number of "recent" messages has changed, so play a sound
				// of told to for new messages on server
				//	re-enable this feature later, make it a general option
				// not a per-account based one
				// -> playing wav-files should be only optional
				// just play a system beep
				// -> this works better for most people
				// -> java doesn't support sound servers like
				// -> alsa or esound anyway
				Toolkit kit = Toolkit.getDefaultToolkit();
				kit.beep(); //system beep

				/*
				 * String file = item.get("sound_file");
				 * 
				 * ColumbaLogger.log.fine("playing sound file=" + file);
				 * 
				 * if (file.equalsIgnoreCase("default")) {
				 * PlaySound.play("newmail.wav"); } else { try {
				 * PlaySound.play(new URL(file)); } catch (Exception ex) {
				 * ex.printStackTrace(); } } // END else
				 */
			}

			//  END if((newRecent != recent) && (item.getBoolean...
		}

		//  END if (newRecent != recent || newTotal != total ...
	}

	//  END public void execute(Worker worker) throws Exception

	/**
	 * @see org.columba.core.command.Command#updateGUI()
	 */
	public void updateGUI() throws Exception {
		// Reenable the action
		action.setEnabled(true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4345.java