error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6137.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6137.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6137.java
text:
```scala
S@@tring messageId = (String) header.get("Message-ID");

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

package org.columba.mail.composer;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.columba.mail.coder.CoderRouter;
import org.columba.mail.coder.Decoder;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.Message;
import org.columba.mail.message.MimeHeader;
import org.columba.mail.message.MimePart;
import org.columba.mail.parser.BodyTextParser;
import org.columba.mail.parser.Rfc822Parser;

/**
 * 
 * The <code>MessageBuilder</code> class is responsible for creating the
 * information for the <code>ComposerModel</class>class.
 * 
 * It generates appropriate header-information, mimeparts and
 * quoted bodytext 
 *
 * 
 */
public class MessageBuilder {

	public final static int REPLY = 0;
	public final static int REPLY_ALL = 1;
	public final static int REPLY_MAILINGLIST = 2;
	public final static int REPLY_AS_ATTACHMENT = 3;

	public final static int FORWARD = 4;
	public final static int FORWARD_INLINE = 5;

	public final static int OPEN = 6;

	private static MessageBuilder instance;

	public MessageBuilder() {

	}

	public static MessageBuilder getInstance() {
		if (instance == null)
			instance = new MessageBuilder();

		return instance;
	}

	/**
	 * 
	 * Check if the subject headerfield already starts with a pattern
	 * like "Re:" or "Fwd:"
	 * 
	 * @param subject  A <code>String</code> containing the subject
	 * @param pattern A <code>String</code> specifying the pattern
	 *                to search for.
	 **/
	public static boolean isAlreadyReply(String subject, String pattern) {

		if (subject == null)
			return false;

		if (subject.length() == 0)
			return false;

		String str = subject.toLowerCase();

		// for example: "Re: this is a subject"
		if (str.startsWith(pattern) == true)
			return true;

		// for example: "[columba-users]Re: this is a subject"
		int index = str.indexOf(pattern);
		if (index != -1)
			return true;

		return false;
	}

	/**
	 * 
	 * create subject headerfield in using the senders message
	 * subject and prepending "Re:" if not already there
	 * 
	 * @param header A <code>ColumbaHeader</code> which contains
	 *               the headerfields of the message we want
	 * 	             reply/forward.
	 * 
	 * FIXME: we need to i18n this!
	 **/
	private static String createReplySubject(ColumbaHeader header) {
		String subject = (String) header.get("Subject");

		// if subject doesn't start already with "Re:" prepend it
		if (isAlreadyReply(subject, "re:") == false)
			subject = "Re: " + subject;

		return subject;
	}

	/**
	 * 
	 * create Subject headerfield in using the senders message
	 * subject and prepending "Fwd:" if not already there
	 * 
	 * @param header A <code>ColumbaHeader</code> which contains
	 *        the headerfields of the message we want
	 * 	      reply/forward.
	 * 
	 * FIXME: we need to i18n this!
	 * 
	 **/
	private static String createForwardSubject(ColumbaHeader header) {
		String subject = (String) header.get("Subject");

		// if subject doesn't start already with "Fwd:" prepend it
		if (isAlreadyReply(subject, "fwd:") == false)
			subject = "Fwd: " + subject;

		return subject;
	}

	/**
	 * 
	 * create a To headerfield in using the senders message
	 * Reply-To or From headerfield
	 * 
	 * @param header A <code>ColumbaHeader</code> which contains
	 *               the headerfields of the message we want
	 * 	             reply/forward.
	 * 
	 * */
	private static String createTo(ColumbaHeader header) {
		String replyTo = (String) header.get("Reply-To");
		String from = (String) header.get("From");

		if (replyTo == null) {
			// Reply-To headerfield isn't specified, try to use From instead
			if (from != null)
				return from;
			else
				return "";
		} else
			return replyTo;
	}

	/**
	 * 
	 * This is for creating the "Reply To All recipients" 
	 * To headerfield.
	 * 
	 * It is different from the <code>createTo</code> method
	 * in that it also appends the recipients specified in the
	 * To headerfield
	 * 
	 * @param header A <code>ColumbaHeader</code> which contains
	 *               the headerfields of the message we want
	 * 	             reply/forward.
	 * 
	 **/
	private static String createToAll(ColumbaHeader header) {
		String sender = "";
		String replyTo = (String) header.get("Reply-To");
		String from = (String) header.get("From");
		String to = (String) header.get("To");

		// if Reply-To headerfield isn't specified, try to use from
		if (replyTo == null) {
			sender = from;
		} else
			sender = replyTo;

		// create To headerfield
		StringBuffer buf = new StringBuffer();
		buf.append(sender);
		buf.append(",");
		buf.append(to);

		return buf.toString();
	}

	/**
	 * 
	 * This method creates a To headerfield for the
	 * "Reply To MailingList" action. 
	 * It uses the List-Post headerfield and falls back
	 * to Reply-To or From if needed
	 * 
	 * @param header A <code>ColumbaHeader</code> which contains
	 *               the headerfields of the message we want
	 * 	             reply/forward.
	 * */
	private static String createToMailinglist(ColumbaHeader header) {

		String sender = (String) header.get("List-Post");
		if (sender != null) {
			// example: List-Post: <mailto:frd@localhost>
			// we have to remove the "mailto:" substring

			int pos = 0;
			StringBuffer list = new StringBuffer(sender);
			while (pos < list.length()) {
				char ch = list.charAt(pos);
				if (ch == '<') {
					pos++;
					char ch2 = '<';
					while (ch2 != ':') {
						System.out.println("char:" + ch2);
						if (pos >= list.length())
							break;

						ch2 = list.charAt(pos);
						list.deleteCharAt(pos);
					}

					if (list.charAt(pos + 1) == ':')
						list.deleteCharAt(pos + 1);
				}
				pos++;
			}

			sender = list.toString();
		} else
			sender = (String) header.get("Reply-To");

		if (sender == null)
			sender = (String) header.get("From");

		return sender;
	}

	/**
	 * 
	 * Creates In-Reply-To and References headerfields. 
	 * These are useful for mailing-list threading.
	 * 
	 * @param header A <code>ColumbaHeader</code> which contains
	 *               the headerfields of the message we want
	 * 	             reply/forward.
	 * 
	 * @param model  The <code>ComposerModel</code> we want to 
	 *               pass the information to.
	 * 
	 * FIXME: if the References headerfield contains to many 
	 *        characters, we have to remove some of the first
	 *        References, before appending another one.
	 *        (RFC822 headerfields are not allowed to become  
	 *        that long)
	 * 
	 * */
	private static void createMailingListHeaderItems(
		ColumbaHeader header,
		ComposerModel model) {
		String messageId = (String) header.get("Message-Id");
		if (messageId != null) {
			model.setHeaderField("In-Reply-To", messageId);

			String references = (String) header.get("References");
			if (references != null) {
				references = references + " " + messageId;
				model.setHeaderField("References", references);

			}
		}
	}

	/**
	 * 
	 * Search the correct Identity for replying to someone
	 * 
	 * @param header A <code>ColumbaHeader</code> which contains
	 *               the headerfields of the message we want
	 * 	             reply/forward.
	 */
	private static AccountItem getAccountItem(ColumbaHeader header) {
		String host = (String) header.get("columba.host");
		String address = (String) header.get("To");

		// if the Account/Identity is already defined in the columba.host headerfield
		// use it. Otherwise search through every account for the To/email-address
		AccountItem item =
			MailConfig.getAccountList().hostGetAccount(host, address);

		return item;
	}

	/**
	 * 
	 * create bodytext
	 * 
	 * @param message A <code>Message</code> which contains
	 *                the bodytext of the message we want
	 * 	              reply/forward.
	 */
	private static String createBodyText(Message message) {
		String bodyText = "";

		MimePart bodyPart = message.getBodyPart();

		// init decoder with appropriate content-transfer-encoding
		Decoder decoder =
			CoderRouter.getDecoder(
				bodyPart.getHeader().contentTransferEncoding);

		// decode bodytext
		try {

			bodyText = decoder.decode(bodyPart.getBody(), null);
		} catch (UnsupportedEncodingException e) {
		}

		return bodyText;
	}

	/**
	 * 
	 * prepend "> " characters to the bodytext to specify 
	 * we are quoting
	 * 
	 * @param message A <code>Message</code> which contains
	 *                the bodytext of the message we want
	 * 	              reply/forward.
	 * 
	 * FIXME: we should make this configureable
	 * 
	 */
	private static String createQuotedBodyText(Message message) {
		String bodyText = createBodyText(message);

		String quotedBodyText = BodyTextParser.quote(bodyText);

		return quotedBodyText;
	}

	/** 
	 * 
	 * Fill the <code>ComposerModel</code> with headerfields,
	 * bodytext and mimeparts
	 * 
	 * @param message   A <code>Message</code> which contains
	 *                  the headerfield/bodytext/mimeparts of 
	 *                  of the message we want to reply/forward.
	 * 
	 * @param model     The <code>ComposerModel</code> we want to 
	 *                  pass the information to.
	 * 
	 * @param operation an int value specifying the operation-type
	 *                  (for example: MessageBuilder.REPLY, .REPLY_TO_ALL)
	 * 
	 */
	public void createMessage(
		Message message,
		ComposerModel model,
		int operation) {

		ColumbaHeader header = (ColumbaHeader) message.getHeader();

		if ((operation == FORWARD) || (operation == FORWARD_INLINE)) {
			model.setSubject(createForwardSubject(header));
		} else {
			model.setSubject(createReplySubject(header));
		}

		String to = null;
		if (operation == REPLY)
			to = createTo(header);
		else if (operation == REPLY_ALL)
			to = createToAll(header);
		else if (operation == REPLY_MAILINGLIST)
			to = createToMailinglist(header);

		if (to != null) {
			model.setTo(to);
			addSenderToAddressbook(to);
		}

		if (operation != FORWARD)
			createMailingListHeaderItems(header, model);

		if ((operation != FORWARD) && (operation != FORWARD_INLINE)) {
			AccountItem accountItem = getAccountItem(header);
			model.setAccountItem(accountItem);
		}

		if ((operation == REPLY_AS_ATTACHMENT) || (operation == FORWARD)) {
			// append message as mimepart
			if (message.getSource() != null) {
				// initialize MimeHeader as RFC822-compliant-message
				MimeHeader mimeHeader = new MimeHeader();
				mimeHeader.contentType = new String("Message");
				mimeHeader.contentSubtype = new String("Rfc822");

				model.addMimePart(
					new MimePart(mimeHeader, message.getSource()));
			}
		} else {
			// prepend "> " to every line of the bodytext
			String bodyText = createQuotedBodyText(message);
			model.setBodyText(bodyText);
		}

	}

	/** 
	 * 
	 * Fill the <code>ComposerModel</code> with headerfields,
	 * bodytext and mimeparts.
	 * 
	 * This is a special method for the "Open Message in Composer"
	 * action.
	 * 
	 * @param message   The <code>Message</code> we want to edit
	 *                  as new message.
	 * 
	 * @param model     The <code>ComposerModel</code> we want to 
	 *                  pass the information to.
	 * 
	 */
	public static void openMessage(Message message, ComposerModel model) {
		ColumbaHeader header = (ColumbaHeader) message.getHeader();

		// copy every headerfield the original message contains
		Hashtable hashtable = header.getHashtable();
		for (Enumeration e = hashtable.keys(); e.hasMoreElements();) {
			Object key = e.nextElement();

			try {
				model.setHeaderField(
					(String) key,
					(String) header.get((String) key));
			} catch (ClassCastException ex) {
				System.out.println("skipping header item");
			}

		}

		AccountItem accountItem = getAccountItem(header);
		model.setAccountItem(accountItem);

		/* 
		   parse the whole message. this is for:
		    -> creating the MimePart-objects
		    -> decoding the bodytext
		        -> in ordner to add MimeParts to the ComposerModel
		           we need to decode them. the MimeParts become
		           encoded before sending them (quoted-printable
		           or base64). Not decoding them here, would make
		           them become encoded twice times.
		*/
		Message parsedMessage =
			new Rfc822Parser().parse(
				message.getSource(),
				true,
				(ColumbaHeader) message.getHeader(),
				0);

		int count = parsedMessage.getMimePartTree().count();
		Decoder decoder;
		for (int i = 0; i < count; i++) {
			MimePart mp = parsedMessage.getMimePartTree().get(i);
			MimeHeader mimeHeader = mp.getHeader();
			decoder =
				CoderRouter.getDecoder(mimeHeader.contentTransferEncoding);

			String str = "";
			try {
				str = decoder.decode(mp.getBody(), null);

			} catch (UnsupportedEncodingException e) {
			}

			// first MimePart is the bodytext of the message
			if (i == 0)
				model.setBodyText(str);
			else {
				mp.setBody(str);
				model.addMimePart(mp);
			}

		}

	}

	/** 
	 * 
	 * Fill the <code>ComposerModel</code> with headerfields,
	 * bodytext and mimeparts.
	 * 
	 * This is a special method for the "Bounce Message"
	 * action.
	 * 
	 * @param message   The <code>Message</code> we want to edit
	 *                  as new message.
	 * 
	 * @param model     The <code>ComposerModel</code> we want to 
	 *                  pass the information to.
	 * 
	 */
	public static void bounceMessage(Message message, ComposerModel model) {
		ColumbaHeader header = (ColumbaHeader) message.getHeader();

		// copy every headerfield the original message contains
		Hashtable hashtable = header.getHashtable();
		for (Enumeration e = hashtable.keys(); e.hasMoreElements();) {
			Object key = e.nextElement();

			try {
				model.setHeaderField(
					(String) key,
					(String) header.get((String) key));
			} catch (ClassCastException ex) {
				System.out.println("skipping header item");
			}

		}

		model.setSubject("Undelivered Mail Returned to Sender");

		String host = "";
		String sender = (String) message.getHeader().get("Reply-To");
		if (sender == null)
			sender = (String) message.getHeader().get("From");

		System.out.println("[MESSAGE!!!] sender:" + sender);
		// break the sender string to recive the hostname
		// we break on @
		StringTokenizer strToken = new StringTokenizer(sender, "@");
		// the second token is it
		if (strToken.countTokens() == 2) {
			// get the first token (before @) and lett it fall
			strToken.nextToken();
			// get the nextToken (after @) and remember
			host = strToken.nextToken();
			System.out.println("[DEBUG!!!!] host: " + host);
			// here we can start parsing all valid characters for an hostname
			// RFC0288 describes, that we can until we have found an > character, so let use do this
			strToken = new StringTokenizer(host, ">");
			if (strToken.countTokens() > 0) {
				host = strToken.nextToken();
				System.out.println("[DEBUG!!!!] host: " + host);
			} else {
				// there is an Bug and at this time we send this to the commandline
				System.out.println(
					"BUG found: ComposerWorker.Operation.COMPOSER_BOUNCE number 1");
			}
		} else {
			// there is an Bug and at this time we send this to the commandline
			System.out.println(
				"BUG found: ComposerWorker.Operation.COMPOSER_BOUNCE number 2");
		}
		System.out.println("[DEBUG!!!!!] host: " + host);
		// setting the from field (this is one of the real hack for bounce)
		model.setHeaderField("From", "MAILER-DAEMON@" + host);

		model.setTo(sender);

		model.setHeaderField("References", "");

		model.setBodyText("");

	}

	/********************** addressbook stuff ***********************/

	/**
	 *
	 * add automatically every person we'll send a message to
	 * the "Collected Addresses" Addressbook
	 *
	 */
	public void addSenderToAddressbook(String sender) {

		if (sender != null) {
			if (sender.length() > 0) {
				// TODO we need a AddressbookController here to get the selected folder
				
				/*
				org.columba.addressbook.folder.Folder selectedFolder =
					(org.columba.addressbook.folder.Folder) MainInterface.addressbookInterface.treeModel.getFolder(102);
				
				String address = AddressParser.getAddress(sender);
				System.out.println("address:" + address);
				
				if (!selectedFolder.exists(address)) {
					ContactCard card = new ContactCard();
				
					String fn = AddressParser.getDisplayname(sender);
					System.out.println("fn=" + fn);
				
					card.set("fn", fn);
					card.set("displayname", fn);
					card.set("email", "internet", address);
				
					selectedFolder.add(card);
				}
				*/
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6137.java