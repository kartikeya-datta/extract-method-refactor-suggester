error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9690.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9690.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9690.java
text:
```scala
S@@tring bodyMsg = StreamUtils.readCharacterStream(body).toString();

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
package org.columba.mail.composer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.regex.Pattern;

import org.columba.addressbook.facade.IContactFacade;
import org.columba.core.io.StreamUtils;
import org.columba.core.services.ServiceNotFoundException;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.AccountList;
import org.columba.mail.config.MailConfig;
import org.columba.mail.connector.ServiceConnector;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.ristretto.coder.Base64DecoderInputStream;
import org.columba.ristretto.coder.CharsetDecoderInputStream;
import org.columba.ristretto.coder.QuotedPrintableDecoderInputStream;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.StreamableMimePart;

/**
 * 
 * The <code>MessageBuilderHelper</code> class is responsible for creating the
 * information for the <code>ComposerModel</class>class.
 * <p>
 * It generates appropriate header-information, mimeparts and
 * quoted bodytext, etc.
 * <p>
 * These helper class is primarly used by the commands in org.columba.composer.command
 *   *
 *  @author fdietz, tstich
 */
public class MessageBuilderHelper {

	private static final Pattern quotePattern = Pattern.compile("^(.*)$");

	/**
	 * 
	 * Check if the subject headerfield already starts with a pattern like "Re:"
	 * or "Fwd:"
	 * 
	 * @param subject
	 *            A <code>String</code> containing the subject
	 * @param pattern
	 *            A <code>String</code> specifying the pattern to search for.
	 */
	public static boolean isAlreadyReply(String subject, String pattern) {
		if (subject == null) {
			return false;
		}

		if (subject.length() == 0) {
			return false;
		}

		String str = subject.toLowerCase();

		// for example: "Re: this is a subject"
		if (str.startsWith(pattern) == true) {
			return true;
		}

		// for example: "[columba-users]Re: this is a subject"
		int index = str.indexOf(pattern);

		if (index != -1) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * create subject headerfield in using the senders message subject and
	 * prepending "Re:" if not already there
	 * 
	 * @param header
	 *            A <code>ColumbaHeader</code> which contains the headerfields
	 *            of the message we want reply/forward.
	 * 
	 * FIXME (@author fdietz): we need to i18n this!
	 */
	public static String createReplySubject(String subject) {
		// if subject doesn't start already with "Re:" prepend it
		if (!isAlreadyReply(subject, "re:")) {
			subject = "Re: " + subject;
		}

		return subject;
	}

	/**
	 * 
	 * create Subject headerfield in using the senders message subject and
	 * prepending "Fwd:" if not already there
	 * 
	 * @param header
	 *            A <code>ColumbaHeader</code> which contains the headerfields
	 *            of the message we want reply/forward.
	 * 
	 * FIXME (@author fdietz): we need to i18n this!
	 *  
	 */
	public static String createForwardSubject(String subject) {
		// if subject doesn't start already with "Fwd:" prepend it
		if (!isAlreadyReply(subject, "fwd:")) {
			subject = "Fwd: " + subject;
		}

		return subject;
	}

	/**
	 * 
	 * create a To headerfield in using the senders message Reply-To or From
	 * headerfield
	 * 
	 * @param header
	 *            A <code>Header</code> which contains the headerfields of the
	 *            message we want reply/forward.
	 *  
	 */
	public static String createTo(Header header) {
		String replyTo = (String) header.get("Reply-To");
		String from = (String) header.get("From");

		if (replyTo == null) {
			// Reply-To headerfield isn't specified, try to use From instead
			if (from != null) {
				return from;
			} else {
				return "";
			}
		} else {
			return replyTo;
		}
	}

	/**
	 * 
	 * This is for creating the "Reply To All recipients" To headerfield.
	 * 
	 * It is different from the <code>createTo</code> method in that it also
	 * appends the recipients specified in the To headerfield
	 * 
	 * @param header
	 *            A <code>ColumbaHeader</code> which contains the headerfields
	 *            of the message we want reply/forward.
	 *  
	 */
	public static String createToAll(Header header) {
		String sender = "";
		String replyTo = (String) header.get("Reply-To");
		String from = (String) header.get("From");
		String to = (String) header.get("To");
		String cc = (String) header.get("Cc");

		// if Reply-To headerfield isn't specified, try to use from
		if (replyTo == null) {
			sender = from;
		} else {
			sender = replyTo;
		}

		// create To headerfield
		StringBuffer buf = new StringBuffer();
		buf.append(sender);

		if (to != null) {
			buf.append(", ");
			buf.append(to);
		}

		if (cc != null) {
			buf.append(", ");
			buf.append(cc);
		}

		return buf.toString();
	}

	/**
	 * 
	 * This method creates a To headerfield for the "Reply To MailingList"
	 * action. It uses the X-BeenThere headerfield and falls back to Reply-To or
	 * From if needed
	 * 
	 * @param header
	 *            A <code>Header</code> which contains the headerfields of the
	 *            message we want reply/forward.
	 */
	public static String createToMailinglist(Header header) {
		// example: X-BeenThere: columba-devel@lists.sourceforge.net
		String sender = (String) header.get("X-BeenThere");

		if (sender == null) {
			sender = (String) header.get("X-Beenthere");
		}

		if (sender == null) {
			sender = (String) header.get("Reply-To");
		}

		if (sender == null) {
			sender = (String) header.get("From");
		}

		return sender;
	}

	/**
	 * 
	 * Creates In-Reply-To and References headerfields. These are useful for
	 * mailing-list threading.
	 * 
	 * @param header
	 *            A <code>Header</code> which contains the headerfields of the
	 *            message we want reply/forward.
	 * 
	 * @param model
	 *            The <code>ComposerModel</code> we want to pass the
	 *            information to.
	 * 
	 * TODO (@author fdietz): if the References headerfield contains to many
	 * characters, we have to remove some of the first References, before
	 * appending another one. (RFC822 headerfields are not allowed to become
	 * that long)
	 *  
	 */
	public static void createMailingListHeaderItems(Header header,
			ComposerModel model) {
		String messageId = (String) header.get("Message-ID");

		if (messageId == null) {
			messageId = (String) header.get("Message-Id");
		}

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
	 * <p>
	 *  
	 */
	public static AccountItem getAccountItem(Integer accountUid) {
		if (MailConfig.getInstance() == null)
			return null;

		AccountList list = MailConfig.getInstance().getAccountList();
		AccountItem accountItem = null;
		list.getDefaultAccount();

		if (accountUid != null) {
			accountItem = list.uidGet(accountUid.intValue());
		}

		// *20040229, karlpeder* Use default account as fall back
		if (accountItem == null) {
			accountItem = list.getDefaultAccount();
		}

		//if (accountUid != null) {
		//    accountItem = list.getDefaultAccount();
		//}

		return accountItem;
	}

	/**
	 * 
	 * create bodytext
	 * 
	 * @param message
	 *            A <code>Message</code> which contains the bodytext of the
	 *            message we want reply/forward.
	 */
	public static String createBodyText(MimePart mimePart) throws IOException {
		CharSequence bodyText = "";

		StreamableMimePart bodyPart = (StreamableMimePart) mimePart;
		String charsetName = bodyPart.getHeader()
				.getContentParameter("charset");
		int encoding = bodyPart.getHeader().getContentTransferEncoding();

		InputStream body = bodyPart.getInputStream();

		switch (encoding) {
		case MimeHeader.QUOTED_PRINTABLE: {
			body = new QuotedPrintableDecoderInputStream(body);

			break;
		}

		case MimeHeader.BASE64: {
			body = new Base64DecoderInputStream(body);

			break;
		}
		}

		if (charsetName != null) {
			Charset charset;

			try {
				charset = Charset.forName(charsetName);
			} catch (UnsupportedCharsetException e) {
				charset = Charset.forName(System.getProperty("file.encoding"));
			}

			body = new CharsetDecoderInputStream(body, charset);
		}

		String bodyMsg = StreamUtils.readInString(body).toString();
		return bodyMsg;
	}

	/**
	 * 
	 * prepend "> " characters to the bodytext to specify we are quoting
	 * 
	 * @param message
	 *            A <code>Message</code> which contains the bodytext of the
	 *            message we want reply/forward.
	 * @param html
	 *            True for html messages (a different quoting is necessary)
	 * 
	 * TODO (@author fdietz): we should make this configureable
	 *  
	 */
	public static String createQuotedBodyText(CharSequence bodyText,
			boolean html) throws IOException {
		// Quote according model type (text/html)
		String quotedBodyText;

		if (html) {
			// html - quoting is done by inserting a div around the
			// message formattet with a blue line at left edge
			// TODO (@author fdietz): Implement quoting (font color, stylesheet,
			// blockquote???)

			/*
			 * String lcase = bodyText.toLowerCase(); StringBuffer buf = new
			 * StringBuffer(); String quoteStart = " <blockquote> "; String
			 * quoteEnd = " </blockquote> ";
			 * 
			 * int pos = lcase.indexOf(" <body"); pos = lcase.indexOf("> ", pos) +
			 * 1; buf.append(bodyText.substring(0, pos));
			 * buf.append(quoteStart); int end = lcase.indexOf(" </body");
			 * buf.append(bodyText.substring(pos, end)); buf.append(quoteEnd);
			 * buf.append(bodyText.substring(end));
			 * 
			 * ColumbaLogger.log.info("Source:\n" + bodyText);
			 * ColumbaLogger.log.info("Result:\n" + buf.toString());
			 * 
			 * quotedBodyText = buf.toString();
			 */
			quotedBodyText = bodyText.toString();
		} else {
			// plain text
			quotedBodyText = bodyText.toString().replaceAll("(?m)^(.*)$",
					"> $1");
		}

		return quotedBodyText;
	}

	/**
	 * Check if HTML support should be enabled in model.
	 * 
	 * @return true, if enabled. false, otherwise
	 */
	public static boolean isHTMLEnabled() {
		if (MailConfig.getInstance() == null)
			return false;

		// get configuration
		XmlElement optionsElement = MailConfig.getInstance().get(
				"composer_options").getElement("/options");
		XmlElement htmlElement = optionsElement.getElement("html");

		// create html element, if it doesn't exist
		if (htmlElement == null) {
			htmlElement = optionsElement.addSubElement("html");
		}

		// get enable attribute
		String enableHtml = htmlElement.getAttribute("enable", "false");

		return Boolean.valueOf(enableHtml).booleanValue();
	}

	/** ******************** addressbook stuff ********************** */
	/**
	 * 
	 * add automatically every person we'll send a message to the "Collected
	 * Addresses" Addressbook
	 *  
	 */
	public static void addAddressesToAddressbook(Address[] addresses) {
		IContactFacade contactFacade=null;
		try {
			contactFacade = ServiceConnector.getContactFacade();
			for (int i = 0; i < addresses.length; i++) {
				contactFacade.addContactToCollectedAddresses(addresses[i]
						.getMailAddress());

			}
		} catch (ServiceNotFoundException e) {
			e.printStackTrace();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9690.java