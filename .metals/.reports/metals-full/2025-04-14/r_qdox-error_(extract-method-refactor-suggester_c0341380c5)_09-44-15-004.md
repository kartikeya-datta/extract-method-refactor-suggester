error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3556.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3556.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3556.java
text:
```scala
"Columba v" + o@@rg.columba.core.main.MainInterface.version);

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.mail.composer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Vector;

import org.columba.addressbook.parser.ListParser;
import org.columba.core.command.WorkerStatusController;
import org.columba.mail.coder.EncodedWordEncoder;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.IdentityItem;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.mail.message.MessageIDGenerator;
import org.columba.mail.message.MimeHeader;
import org.columba.mail.message.MimePart;
import org.columba.mail.message.PgpMimePart;
import org.columba.mail.message.SendableHeader;
import org.columba.mail.util.RFC822Date;

public class MessageComposer {
	private ComposerModel model;
	private ComposerController controller;
	private int accountUid;

	public MessageComposer(ComposerController controller) {
		this.controller = controller;
		model = controller.getModel();
	}

	protected SendableHeader initHeader() {
		SendableHeader header = new SendableHeader();
		EncodedWordEncoder encoder = new EncodedWordEncoder();

		// RFC822 - Header

		// TODO : Add EncodedWord-Support to TO,CC,FROM -> like Subject!		

		if (model.getToList().size() > 0)
			header.set("To", ListParser.parse(model.getToList()));

		if (model.getCcList().size() > 0)
			header.set("Cc", ListParser.parse(model.getCcList()));

		if (model.getBccList().size() > 0)
			header.set("Bcc", ListParser.parse(model.getBccList()));

		try {
			header.set(
				"Subject",
				encoder.encode(model.getSubject(), model.getCharsetName()));
		} catch (UnsupportedEncodingException e) {
		}

		AccountItem item = controller.getModel().getAccountItem();
		IdentityItem identity = item.getIdentityItem();

		header.set("From", identity.get("address"));
		header.set("X-Priority", model.getPriority());

		/*
		String priority = controller.getModel().getPriority();
		
		if (priority != null) {
			header.set("columba.priority", new Integer(priority));
		} else {
			header.set("columba.priority", new Integer(3));
		}
		*/

		header.set("Mime-Version", "1.0");

		String organisation = identity.get("organisation");
		if (organisation.length() > 0)
			header.set("Organisation", organisation);

		// reply-to
		String replyAddress = identity.get("reply_address");
		if (replyAddress.length() > 0)
			header.set("Reply-To", replyAddress);

		String messageID = MessageIDGenerator.generate();
		header.set("Message-Id", messageID);

		String inreply = model.getHeaderField("In-Reply-To");
		if (inreply != null)
			header.set("In-Reply-To", inreply);

		String references = model.getHeaderField("References");
		if (references != null)
			header.set("References", references);

		header.set(
			"X-Mailer",
			"Columba v" + org.columba.main.MainInterface.version);

		// shortFrom
		String shortFrom = controller.getModel().getHeaderField("From");
		if (shortFrom != null) {
			if (shortFrom.indexOf("<") != -1) {
				shortFrom = shortFrom.substring(0, shortFrom.indexOf("<"));
				if (shortFrom.length() > 0) {
					if (shortFrom.startsWith("\""))
						shortFrom =
							shortFrom.substring(1, shortFrom.length() - 1);
					if (shortFrom.endsWith("\""))
						shortFrom =
							shortFrom.substring(0, shortFrom.length() - 1);
				}

			} 

			header.set("columba.from", shortFrom);
		} else {
			header.set("columba.from", new String(""));
		}

		// date
		Date date = new Date();
		header.set("columba.date", date);
		header.set("Date", RFC822Date.toRFC822String(date));

		return header;
	}

	private boolean needQPEncoding(String input) {

		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) > 127)
				return true;
		}

		return false;
	}
	
	protected String getSignature(IdentityItem item) {
		
		File file = new File(item.get("signature_file"));
		StringBuffer strbuf = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			/*
			BufferedReader in =
				new BufferedReader(
					new InputStreamReader(
						new FileInputStream(file),
						model.getCharsetName()));
					*/
			String str;

			while ((str = in.readLine()) != null) {
				strbuf.append(str + "\n");
			}

			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			return "";
		}

		try {
			return new String(strbuf.toString().getBytes(),  model.getCharsetName());
		} catch (UnsupportedEncodingException e) {
		}
		
		return null;
	}


/*
	protected String getSignature(IdentityItem item) {
		
		File file = new File(item.getSignatureFile());
		StringBuffer strbuf = new StringBuffer();
		try {
			//BufferedReader in = new BufferedReader(new FileReader(file));
			BufferedReader in =
				new BufferedReader(
					new InputStreamReader(
						new FileInputStream(file),
						model.getCharsetName()));
			String str;

			while ((str = in.readLine()) != null) {
				strbuf.append(str + "\n");
			}

			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			return "";
		}

		return strbuf.toString();

	}
*/
	private MimePart composeTextMimePart() {
		MimePart bodyPart = new MimePart();
		// Init Mime-Header with Default-Values (text/plain)	

		// Set Default Charset or selected		
		String charsetName = model.getCharsetName();
		bodyPart.getHeader().contentParameter.put("charset", charsetName);

		String body = model.getBodyText();

		AccountItem item = controller.getModel().getAccountItem();
		IdentityItem identity = item.getIdentityItem();
		boolean appendSignature = identity.getBoolean("attach_signature");

		if (appendSignature == true) {
			String signature = getSignature(identity);

			if (signature != null) {
				body = body + "\n\n" + signature;

			}
		}

		if (needQPEncoding(body))
			bodyPart.getHeader().setContentTransferEncoding("quoted-printable");

		if (body.length() == 0) {
			return null;
		}

		bodyPart.setBody(body);

		return bodyPart;
	}

	public SendableMessage compose(WorkerStatusController workerStatusController) {
		this.accountUid = model.getAccountItem().getUid();

		workerStatusController.setDisplayText("Composing Message...");

		MimeTreeRenderer renderer = MimeTreeRenderer.getInstance();
		SendableMessage message = new SendableMessage();
		StringBuffer composedMessage = new StringBuffer();

		SendableHeader header = initHeader();
		MimePart root;

		Vector mimeParts = model.getAttachments();

		MimePart body = composeTextMimePart();
		if (body != null)
			mimeParts.insertElementAt(body, 0);

		// Create Multipart/Mixed if necessary
		if (mimeParts.size() > 1) {
			root = new MimePart(new MimeHeader("multipart", "mixed"));

			for (int i = 0; i < mimeParts.size(); i++) {
				root.addChild((MimePart) mimeParts.get(i));
			}
		} else {
			root = (MimePart) mimeParts.get(0);
		}

		if (model.isSignMessage()) {
			PgpMimePart signPart =
				new PgpMimePart(
					new MimeHeader("multipart", "signed"),
					model.getAccountItem().getPGPItem(),
					PgpMimePart.SIGNED);

			signPart.addChild(root);
			root = signPart;
		}

		header.setRecipients(controller.getRCPTVector());

		String composedBody;

		header.set("columba.attachment", new Boolean(true));

		composedMessage.append(header.getHeader());

		composedMessage.append(renderer.renderMimePart(root, workerStatusController));

		message.setSource( composedMessage.toString());

		// size
		int size = composedMessage.length() / 1024;
		header.set("columba.size", new Integer(size));

		message.setHeader(header);

		message.setAccountUid(accountUid);

		return message;
	}

/*
	private String createBoundary() {
		StringBuffer bound = new StringBuffer();

		bound.append("-----");
		bound.append(System.currentTimeMillis());
		bound.append(model.getHeaderField("From"));

		return bound.toString();
	}

	private String createMimeMultipart(Vector input) {
		StringBuffer output = new StringBuffer();
		bound = createBoundary();
		MimeHeader multiHeader = new MimeHeader("multipart", "mixed");
		multiHeader.contentParameter.put(
			"boundary",
			new String("\"" + bound + "\""));

		output.append(multiHeader.getHeader());
		output.append(
			"\n\tThis is a Mime Multipart Message sent with Columba\n");

		int count = input.size();

		for (int i = 0; i < count; i++) {
			output.append("\n--" + bound + "\n");
			//output.append(((MimePart)input.get(i)).composeToString());
		}

		output.append("\n--" + bound + "--");

		return output.toString();
	}

	private String createEncryptedMimePart(MimePartTree input) {
		if (input.count() > 1)
			return createEncryptedMimeMultipart(input);

		StringBuffer output = new StringBuffer();
		MimePart actPart = input.get(0);

		//return actPart.composeToString();
		return null;
	}

	private String createEncryptedMimeMultipart(MimePartTree input) {

		StringBuffer output = new StringBuffer();
		MimeHeader multiHeader = new MimeHeader("multipart", "encrypted");
		multiHeader.contentParameter.put(
			"protocol",
			"\"application/pgp-encrypted\"");
		bound = bound.substring(2, bound.length());
		multiHeader.contentParameter.put(
			"boundary",
			new String("\"" + bound + "\""));

		output.append(multiHeader.getHeader());
		output.append(
			"\n\tThis is a Mime Multipart Message sent with Columba\n");

		int count = input.count();

		for (int i = 0; i < count; i++) {
			output.append("\n--" + bound + "\n");
			//output.append(input.get(i).composeToString());
		}

		output.append("\n--" + bound + "--");

		return output.toString();
	}
*/
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3556.java