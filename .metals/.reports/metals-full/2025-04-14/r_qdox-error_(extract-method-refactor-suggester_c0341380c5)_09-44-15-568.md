error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9906.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9906.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9906.java
text:
```scala
e@@ncoding = bodyPart.getHeader().getContentTransferEncoding();

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
package org.columba.mail.gui.message.viewer;

import java.awt.Font;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.columba.core.charset.CharsetOwnerInterface;
import org.columba.core.gui.util.FontProperties;
import org.columba.core.io.DiskIO;
import org.columba.core.io.TempFileStore;
import org.columba.core.main.MainInterface;
import org.columba.core.xml.XmlElement;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.message.util.DocumentParser;
import org.columba.mail.main.MailInterface;
import org.columba.mail.parser.text.HtmlParser;
import org.columba.ristretto.coder.Base64DecoderInputStream;
import org.columba.ristretto.coder.CharsetDecoderInputStream;
import org.columba.ristretto.coder.QuotedPrintableDecoderInputStream;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;

/**
 * Viewer displays message body text.
 * 
 * @author fdietz
 *  
 */
public class MessageBodytextViewer extends JTextPane implements Viewer,
		Observer {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.gui.message.viewer");

	//  parser to transform text to html
	private DocumentParser parser;

	private HTMLEditorKit htmlEditorKit;

	// stylesheet is created dynamically because
	// user configurable fonts are used
	private String css = "";

	//  enable/disable smilies configuration
	private XmlElement smilies;

	private boolean enableSmilies;

	// name of font
	private String name;

	// size of font
	private String size;

	// overwrite look and feel font settings
	private boolean overwrite;

	private String body;

	private URL url;

	/**
	 * if true, a html message is shown. Otherwise, plain/text
	 */
	private boolean htmlMessage;

	public MessageBodytextViewer() {
		super();

		setMargin(new Insets(5, 5, 5, 5));
		setEditable(false);

		setDocument(new AsynchronousHTMLDocument());
		htmlEditorKit = new HTMLEditorKit();
		setEditorKit(htmlEditorKit);

		setContentType("text/html");

		XmlElement gui = MailInterface.config.get("options").getElement(
				"/options/gui");
		XmlElement messageviewer = gui.getElement("messageviewer");

		if (messageviewer == null) {
			messageviewer = gui.addSubElement("messageviewer");
		}

		smilies = messageviewer.getElement("smilies");

		if (smilies == null) {
			smilies = messageviewer.addSubElement("smilies");
		}

		// register as configuration change listener
		smilies.addObserver(this);

		String enable = smilies.getAttribute("enabled", "true");

		if (enable.equals("true")) {
			enableSmilies = true;
		} else {
			enableSmilies = false;
		}

		XmlElement quote = messageviewer.getElement("quote");

		if (quote == null) {
			quote = messageviewer.addSubElement("quote");
		}

		// register as configuration change listener
		quote.addObserver(this);

		// TODO (@author fdietz): use value in initStyleSheet()
		String enabled = quote.getAttribute("enabled", "true");
		String color = quote.getAttribute("color", "0");

		// register for configuration changes
		Font font = FontProperties.getTextFont();
		name = font.getName();
		size = new Integer(font.getSize()).toString();

		XmlElement options = MainInterface.config.get("options").getElement(
				"/options");
		XmlElement gui1 = options.getElement("gui");
		XmlElement fonts = gui1.getElement("fonts");

		if (fonts == null) {
			fonts = gui1.addSubElement("fonts");
		}

		// register interest on configuratin changes
		fonts.addObserver(this);

		initStyleSheet();
	}

	private boolean hasHtmlPart(MimePart mimeTypes) {

		if (mimeTypes.getHeader().getMimeType().equalsIgnoreCase("text/html"))
			return true; //exit immediately

		java.util.List children = mimeTypes.getChilds();

		for (int i = 0; i < children.size(); i++) {
			if (hasHtmlPart(mimeTypes.getChild(i)))
				return true;
		}

		return false;

	}

	/**
	 * @see org.columba.mail.gui.message.viewer.Viewer#getViewer(org.columba.mail.folder.Folder,
	 *      java.lang.Object)
	 */
	public void view(MessageFolder folder, Object uid,
			MailFrameMediator mediator) throws Exception {
		MimePart bodyPart = null;
		InputStream bodyStream;

		MimeTree mimePartTree = folder.getMimePartTree(uid);

		XmlElement html = MailInterface.config.getMainFrameOptionsConfig()
				.getRoot().getElement("/options/html");

		//ensure that there is an HTML part in the email, otherwise JTextPanel
		//throws a RuntimeException

		// Which Bodypart shall be shown? (html/plain)
		if ((Boolean.valueOf(html.getAttribute("prefer")).booleanValue())
				&& hasHtmlPart(mimePartTree.getRootMimeNode())) {
			bodyPart = mimePartTree.getFirstTextPart("html");
		} else {
			bodyPart = mimePartTree.getFirstTextPart("plain");
		}


		if (bodyPart == null) {
			bodyStream = new ByteArrayInputStream("<No Message-Text>"
					.getBytes());
		} else {
			// Shall we use the HTML-Viewer?
			htmlMessage = bodyPart.getHeader().getMimeType().getSubtype().equals(
					"html");

			bodyStream = folder.getMimePartBodyStream(uid, bodyPart
					.getAddress());
		}

		
		
		// Which Charset shall we use ?
		Charset charset = ((CharsetOwnerInterface) mediator).getCharset();

		// no charset specified -> automatic mode
		// -> try to determine charset based on content parameter
		if (charset == null) {
			String charsetName = null;
			
			if( bodyPart != null ) {
				bodyPart.getHeader().getContentParameter("charset");
			}
				

			if (charsetName == null) {
				// There is no charset info -> the default system charset is
				// used
				charsetName = System.getProperty("file.encoding");
				charset = Charset.forName(charsetName);
			} else {
				try {
					charset = Charset.forName(charsetName);
				} catch (UnsupportedCharsetException e) {
					charsetName = System.getProperty("file.encoding");
					charset = Charset.forName(charsetName);
				} catch (IllegalCharsetNameException e) {
					charsetName = System.getProperty("file.encoding");
					charset = Charset.forName(charsetName);
				}
			}

			//((CharsetOwnerInterface) mediator).setCharset(charset);

		}

		// default encoding is plain
		int encoding = MimeHeader.PLAIN; 
			
		if( bodyPart != null ) {
			bodyPart.getHeader().getContentTransferEncoding();
		}

		switch (encoding) {
		case MimeHeader.QUOTED_PRINTABLE: {
			bodyStream = new QuotedPrintableDecoderInputStream(bodyStream);

			break;
		}

		case MimeHeader.BASE64: {
			bodyStream = new Base64DecoderInputStream(bodyStream);

			break;
		}
		}

		bodyStream = new CharsetDecoderInputStream(bodyStream, charset);

		// Read Stream in String
		long startTime = System.currentTimeMillis();

		StringBuffer text = new StringBuffer();
		int next = bodyStream.read();

		while (next != -1) {
			text.append((char) next);
			next = bodyStream.read();
		}

		// if HTML stripping is enabled
		if (Boolean.valueOf(html.getAttribute("disable")).booleanValue()) {
			// strip HTML message -> remove all HTML tags
			setBodyText(HtmlParser.stripHtmlTags(text.toString(), true), false);
			
			htmlMessage = false;
		} else {
			setBodyText(text.toString(), htmlMessage);
		}

		bodyStream.close();

		LOG
				.finer("Needed " + (System.currentTimeMillis() - startTime)
						+ " ms ");
	}

	/**
	 * 
	 * read text-properties from configuration and create a stylesheet for the
	 * html-document
	 *  
	 */
	protected void initStyleSheet() {
		// read configuration from options.xml file
		// create css-stylesheet string
		// set font of html-element <P>
		css = "<style type=\"text/css\"><!-- .bodytext {font-family:\"" + name
				+ "\"; font-size:\"" + size + "pt; \"}"
				+ ".quoting {color:#949494;}; --></style>";
	}

	protected void setBodyText(String bodyText, boolean viewHtml) {

		if (viewHtml) {
			try {
				// this is a HTML message

				body = bodyText;

				// try to fix broken html-strings
				//body = HtmlParser.validateHTMLString(body);
				//LOG.info("validated bodytext:\n" + body);

				// create temporary file
				File inputFile = TempFileStore.createTempFileWithSuffix("html");
				// save bodytext to file
				DiskIO.saveStringInFile(inputFile, body);
				// get URL of file
				url = inputFile.toURL();

				//setPage(url);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// this is a text/plain message
			body = bodyText;

			try {
				// substitute special characters like:
				//  <,>,&,\t,\n
				body = HtmlParser.substituteSpecialCharacters(bodyText);

				// parse for urls and substite with HTML-code
				body = HtmlParser.substituteURL(body);

				// parse for email addresses and substite with HTML-code
				body = HtmlParser.substituteEmailAddress(body);

				// parse for quotings and color the darkgray
				body = parser.markQuotings(body);

				// add smilies
				if (enableSmilies == true) {
					body = parser.addSmilies(body);
				}

				// encapsulate bodytext in html-code
				body = transformToHTML(new StringBuffer(body));

				LOG.finest("validated bodytext:\n" + body);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	/*
	 * 
	 * encapsulate bodytext in HTML code
	 *  
	 */
	protected String transformToHTML(StringBuffer buf) {
		// prepend
		buf.insert(0, "<HTML><HEAD>" + css
				+ "</HEAD><BODY class=\"bodytext\"><P>");

		// append
		buf.append("</P></BODY></HTML>");

		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.gui.config.general.MailOptionsDialog
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1) {
		Font font = FontProperties.getTextFont();
		name = font.getName();
		size = new Integer(font.getSize()).toString();

		initStyleSheet();
	}

	/**
	 * @see javax.swing.text.JTextComponent#copy()
	 */
	public void copy() {
		int start = this.getSelectionStart();
		int stop = this.getSelectionEnd();

		StringWriter htmlSelection = new StringWriter();

		try {
			htmlEditorKit.write(htmlSelection, getDocument(), start, stop
					- start);

			Clipboard clipboard = getToolkit().getSystemClipboard();

			// Conversion of html text to plain
			//TODO (@author karlpeder): make a DataFlavor that can handle HTML text
			StringSelection selection = new StringSelection(HtmlParser
					.htmlToText(htmlSelection.toString()));
			clipboard.setContents(selection, selection);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.Viewer#getView()
	 */
	public JComponent getView() {
		return this;
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.Viewer#updateGUI()
	 */
	public void updateGUI() throws Exception {

		if (!htmlMessage) {
			

			// display bodytext
			setText(body);
		} else {
			// this call has to happen in the awt-event dispatcher thread
			setPage(url);
		}
		
//		 setup base url in order to be able to display images
		// in html-component
		URL baseUrl = DiskIO.getResourceURL("org/columba/core/images/");
		LOG.info(baseUrl.toString());
		((HTMLDocument) getDocument()).setBase(baseUrl);

		// scroll window to the beginning
		setCaretPosition(0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9906.java