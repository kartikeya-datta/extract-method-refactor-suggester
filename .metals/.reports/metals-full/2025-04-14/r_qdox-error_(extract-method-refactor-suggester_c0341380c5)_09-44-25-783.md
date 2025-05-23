error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9439.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9439.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9439.java
text:
```scala
m@@odel.setBodyText(StreamUtils.readCharacterStream(

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

package org.columba.mail.gui.composer.command;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Iterator;
import java.util.List;

import org.columba.core.command.Command;
import org.columba.core.command.ICommandReference;
import org.columba.core.command.WorkerStatusController;
import org.columba.core.gui.frame.DefaultContainer;
import org.columba.core.io.StreamUtils;
import org.columba.core.xml.XmlElement;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.composer.MessageBuilderHelper;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.ristretto.coder.Base64DecoderInputStream;
import org.columba.ristretto.coder.CharsetDecoderInputStream;
import org.columba.ristretto.coder.QuotedPrintableDecoderInputStream;
import org.columba.ristretto.io.Source;
import org.columba.ristretto.io.TempSourceFactory;
import org.columba.ristretto.message.BasicHeader;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.LocalMimePart;
import org.columba.ristretto.message.Message;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;
import org.columba.ristretto.message.StreamableMimePart;
import org.columba.ristretto.parser.MessageParser;

/**
 * Open message in composer.
 * 
 * @author fdietz
 */
public class OpenMessageWithComposerCommand extends Command {
	protected ComposerController controller;
	protected ComposerModel model;
	protected AbstractMessageFolder folder;
	protected Object uid;

	/**
	 * Constructor for OpenMessageInComposerCommand.
	 * 
	 * @param frameMediator
	 * @param references
	 */
	public OpenMessageWithComposerCommand(ICommandReference reference) {
		super(reference);
	}

	public void updateGUI() throws Exception {
		// open composer frame
		controller = new ComposerController();
		new DefaultContainer(controller);

		// apply model
		controller.setComposerModel(model);

		// model->view update
		controller.updateComponents(true);
	}

	public void execute(WorkerStatusController worker) throws Exception {
		model = new ComposerModel();

		// get selected folder
		folder = (AbstractMessageFolder) ((MailFolderCommandReference) getReference())
				.getSourceFolder();

		// get selected messages
		Object[] uids = ((MailFolderCommandReference) getReference()).getUids();

		// we only need the first message
		uid = uids[0];

		//TODO (@author fdietz): keep track of progress here

		InputStream messageSourceStream = folder.getMessageSourceStream(uid);
		Source tempSource = TempSourceFactory.createTempSource(
				messageSourceStream, -1);
		messageSourceStream.close();

		Message message = MessageParser.parse(tempSource);

		initHeader(message);
		
		// get message flags
		Flags flags = folder.getFlags(uid);
		model.getMessage().getHeader().setFlags(flags);

		// select the account this mail was received from
		Integer accountUid = (Integer) folder.getAttribute(uids[0],
				"columba.accountuid");
		AccountItem accountItem = MessageBuilderHelper
				.getAccountItem(accountUid);
		model.setAccountItem(accountItem);

		XmlElement html = MailConfig.getInstance().getMainFrameOptionsConfig()
				.getRoot().getElement("/options/html");

		boolean preferHtml = Boolean.valueOf(html.getAttribute("prefer"))
				.booleanValue();

		initBody(message, preferHtml);
		
		// store reference to source message
		model.setSourceReference(new MailFolderCommandReference(folder, new Object[] {uid}));
	}

	private void initBody(Message message, boolean preferHtml) throws Exception {
		MimeTree mimeTree = message.getMimePartTree();

		// Which Bodypart shall be shown? (html/plain)
		LocalMimePart bodyPart = null;

		if (preferHtml) {
			bodyPart = (LocalMimePart) mimeTree.getFirstTextPart("html");
		} else {
			bodyPart = (LocalMimePart) mimeTree.getFirstTextPart("plain");
		}

		if (bodyPart != null) {
			MimeHeader header = bodyPart.getHeader();
			if (header.getMimeType().getSubtype().equals("html")) {
				// html
				model.setHtml(true);
			} else {
				model.setHtml(false);
			}
			InputStream bodyStream = folder.getMimePartBodyStream(uid, bodyPart.getAddress());
			
	        // Do decoding stuff
	        switch( header.getContentTransferEncoding() ) {
	        	case MimeHeader.QUOTED_PRINTABLE : {
	        		bodyStream = new QuotedPrintableDecoderInputStream(bodyStream);
	        		break;
	        	}
	        	
	        	case MimeHeader.BASE64 : {
	        		bodyStream = new Base64DecoderInputStream(bodyStream);
	        	}
	        }
	        String charset = header.getContentParameter("charset");
	        if (charset != null) {
	        	try {
	        		bodyStream = new CharsetDecoderInputStream(bodyStream, Charset.forName(charset));
	        		model.setCharset(Charset.forName(charset));
	        	} catch( UnsupportedCharsetException e ) {
	        		// Stick with the default charset
	        	}
	        }

			model.setBodyText(StreamUtils.readInString(
					bodyStream)
					.toString());

		}

		initAttachments(mimeTree, bodyPart);
	}

	private void initHeader(Message message) {
		Header header = message.getHeader();

		BasicHeader rfcHeader = new BasicHeader(header);

		// set subject
		model.setSubject(rfcHeader.getSubject());

		model.setTo(rfcHeader.getTo());
		
		model.setCc(rfcHeader.getCc());
		model.setBcc(rfcHeader.getBcc());

		// copy every headerfield the original message contains
		model.setHeader(header);
	}

	private void initAttachments(MimeTree collection, MimePart bodyPart) {
		// Get all MimeParts
		List displayedMimeParts = collection.getAllLeafs();

		if (bodyPart != null) {
			MimePart bodyParent = bodyPart.getParent();

			// remove the bodypart from the mimeparts
			// that are added to the attachment viewer
			if (bodyParent != null && bodyParent.getHeader().getMimeType().getSubtype().equals(
			"alternative")) {
				List bodyParts = bodyParent.getChilds();
				displayedMimeParts.removeAll(bodyParts);
			}  else {
				displayedMimeParts.remove(bodyPart);
			}

			Iterator it = displayedMimeParts.iterator();

			while (it.hasNext()) {
				model.addMimePart((StreamableMimePart) it.next());
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9439.java