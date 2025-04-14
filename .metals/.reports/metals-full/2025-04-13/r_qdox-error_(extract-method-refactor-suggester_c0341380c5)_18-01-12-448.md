error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6856.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6856.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6856.java
text:
```scala
p@@art.getBody().getBytes("ISO_8859_1")),

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
package org.columba.mail.gui.attachment.command;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.columba.core.command.Command;
import org.columba.core.command.DefaultCommandReference;
import org.columba.core.command.Worker;
import org.columba.core.gui.frame.FrameModel;
import org.columba.core.main.MainInterface;
import org.columba.core.util.TempFileStore;
import org.columba.mail.coder.CoderRouter;
import org.columba.mail.coder.Decoder;
import org.columba.mail.command.FolderCommand;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.folder.Folder;
import org.columba.mail.folder.temp.TempFolder;
import org.columba.mail.gui.message.command.ViewMessageCommand;
import org.columba.mail.gui.messageframe.MessageFrameController;
import org.columba.mail.gui.mimetype.MimeTypeViewer;
import org.columba.mail.message.AbstractMessage;
import org.columba.mail.message.MimeHeader;
import org.columba.mail.message.MimePart;

/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class OpenAttachmentCommand extends FolderCommand {

	MimePart part;
	File tempFile;
	// true, if showing a message as attachment
	boolean inline = false;
	TempFolder tempFolder;
	Object tempMessageUid;

	/**
	 * Constructor for OpenAttachmentCommand.
	 * @param references
	 */
	public OpenAttachmentCommand(DefaultCommandReference[] references) {
		super(references);

		priority = Command.REALTIME_PRIORITY;
		commandType = Command.NORMAL_OPERATION;

	}

	/**
	 * @see org.columba.core.command.Command#updateGUI()
	 */
	public void updateGUI() throws Exception {
		MimeHeader header = part.getHeader();

		if (header.getContentType().toLowerCase().indexOf("message") != -1) {
			
			MessageFrameController c = (MessageFrameController) FrameModel.openView("MessageFrame");
			
			FolderCommandReference[] r = new FolderCommandReference[1];
			Object[] uidList = new Object[1];
			uidList[0] = tempMessageUid;
			
			c.setTableSelection(r);
			
			r[0] = new FolderCommandReference(tempFolder, uidList);
			MainInterface.processor.addOp(
						new ViewMessageCommand(c,
						r));
						
			
			
			
			//inline = true;
			//openInlineMessage(part, tempFile);
		} else {
			//inline = false;
			MimeTypeViewer viewer = new MimeTypeViewer();
			viewer.open(header, tempFile);
		}
	}

	/**
	 * @see org.columba.core.command.Command#execute(Worker)
	 */
	public void execute(Worker worker) throws Exception {
		FolderCommandReference[] r = (FolderCommandReference[]) getReferences();
		Folder folder = (Folder) r[0].getFolder();
		Object[] uids = r[0].getUids();

		Integer[] address = r[0].getAddress();

		part = folder.getMimePart(uids[0], address, worker);

		Decoder decoder;
		MimeHeader header;
		tempFile = null;

		header = part.getHeader();

		// If part is Message/Rfc822 we do not need to download anything because
		// we have already parsed the subMessage and can directly access the mime-parts

		if (part.getHeader().getContentType().equals("message")) {
			tempFolder = MainInterface.treeModel.getTempFolder();
			tempMessageUid = tempFolder.addMessage( (AbstractMessage) part.getContent(), worker );
			
			inline = true;
		}
		else
		{
		

			try {
				String filename = part.getHeader().getFileName();
				if (filename != null) {
					tempFile = TempFileStore.createTempFile(filename);
				} else
					tempFile = TempFileStore.createTempFile();

				decoder =
					CoderRouter.getDecoder(header.contentTransferEncoding);

				decoder.decode(
					new ByteArrayInputStream(
						part.getBody().getBytes("US-ASCII")),
					new FileOutputStream(tempFile));

				//decoder.run();

			} catch (Exception ex) {
				ex.printStackTrace();

			}

		}
	}

	/**
	 * @see org.columba.core.command.Command#undo(Worker)
	 */
	public void undo(Worker worker) throws Exception {
	}

	/**
	 * @see org.columba.core.command.Command#redo(Worker)
	 */
	public void redo(Worker worker) throws Exception {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6856.java