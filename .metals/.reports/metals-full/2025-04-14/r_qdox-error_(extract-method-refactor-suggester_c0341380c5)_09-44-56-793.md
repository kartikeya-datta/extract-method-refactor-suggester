error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6383.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6383.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6383.java
text:
```scala
i@@nt style = item.getIntegerWithDefault("style", 0);

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.net.URL;
import java.text.DateFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.columba.core.config.DefaultItem;
import org.columba.core.config.IDefaultItem;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.io.DiskIO;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.message.MessageController;
import org.columba.mail.gui.message.filter.SecurityStatusEvent;
import org.columba.mail.gui.message.filter.SecurityStatusListener;
import org.columba.mail.gui.util.AddressListRenderer;
import org.columba.mail.parser.text.HtmlParser;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.message.BasicHeader;
import org.columba.ristretto.message.Header;

/**
 * Shows the headers of a RFC822 message in a lightgray box in the top of the
 * message viewer.
 * 
 * @author fdietz
 */
public class HeaderViewer extends JPanel implements ICustomViewer {

	//  contains headerfields which are to be displayed
	private Map map;

	private boolean visible;

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.gui.message.viewer");

	private HeaderTextPane headerTextPane;

	private StatusPanel statusPanel;

	private boolean hasAttachment;

	private MessageController mediator;

	public HeaderViewer(MessageController mediator) {

		this.mediator = mediator;

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 2, 5));

		JPanel panel = new JPanel();

		add(panel, BorderLayout.CENTER);

		panel.setLayout(new BorderLayout());

		panel.setBorder(BorderFactory.createLineBorder(Color.gray));

		headerTextPane = new HeaderTextPane();

		statusPanel = new StatusPanel();

		panel.add(headerTextPane, BorderLayout.CENTER);

		panel.add(statusPanel, BorderLayout.EAST);

		visible = false;

		mediator.addMouseListener(headerTextPane);
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#view(IMailbox,
	 *      java.lang.Object, org.columba.mail.gui.frame.MailFrameMediator)
	 */
	public void view(IMailbox folder, Object uid, MailFrameMediator mediator)
			throws Exception {
		// add headerfields which are about to show up
		XmlElement headerviewerElement = MailConfig.getInstance()
				.get("options").getElement("/options/headerviewer");
		IDefaultItem item = new DefaultItem(headerviewerElement);
		int style = item.getInteger("style", 0);

		map = new LinkedHashMap();
		Header header = null;
		String[] headers = null;
		switch (style) {
		case 0:
			// default
			headers = new String[] { "Subject", "Date", "From", "To",
					"Reply-To", "Cc", "Bcc" };

			// get header from folder
			header = folder.getHeaderFields(uid, headers);

			// transform headers if necessary
			for (int i = 0; i < headers.length; i++) {
				String key = headers[i];
				Object value = transformHeaderField(header, key);
				if (value != null)
					map.put(key, value);
			}

			break;
		case 1:
			// custom headers
			String list = headerviewerElement.getAttribute("headerfields");

			StringTokenizer tok = new StringTokenizer(list, " ");
			headers = new String[tok.countTokens()];

			for (int i = 0; i < headers.length; i++) {
				headers[i] = tok.nextToken();
			}

			// get header from folder
			header = folder.getHeaderFields(uid, headers);

			// transform headers if necessary
			for (int i = 0; i < headers.length; i++) {
				String key = headers[i];
				Object value = transformHeaderField(header, key);
				if (value != null)
					map.put(key, value);
			}

			break;
		case 2:
			// show all headers
			header = folder.getAllHeaderFields(uid);

			// transform headers if necessary
			Enumeration enumeration = header.getKeys();
			map = new LinkedHashMap();
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				Object value = transformHeaderField(header, key);
				if (value != null)
					map.put(key, value);
			}

			break;
		}

		hasAttachment = ((Boolean) folder.getAttribute(uid,
				"columba.attachment")).booleanValue();

		visible = true;

	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#getView()
	 */
	public JComponent getView() {
		return this;
	}

	protected Object transformHeaderField(Header header, String key) {
		BasicHeader bHeader = new BasicHeader(header);
		String str = null;

		//          message doesn't contain this headerfield
		if (header.get(key) == null) {
			return null;
		}

		// headerfield is empty
		if (((String) header.get(key)).length() == 0) {
			return null;
		}

		if (key.equals("Subject")) {
			str = bHeader.getSubject();

			// substitute special characters like:
			//  <,>,&,\t,\n,"
			str = HtmlParser.substituteSpecialCharactersInHeaderfields(str);
		} else if (key.equals("To")) {
			str = AddressListRenderer.renderToHTMLWithLinks(bHeader.getTo())
					.toString();
		} else if (key.equals("Reply-To")) {
			str = AddressListRenderer.renderToHTMLWithLinks(
					bHeader.getReplyTo()).toString();
		} else if (key.equals("Cc")) {
			str = AddressListRenderer.renderToHTMLWithLinks(bHeader.getCc())
					.toString();
		} else if (key.equals("Bcc")) {
			str = AddressListRenderer.renderToHTMLWithLinks(bHeader.getBcc())
					.toString();
		} else if (key.equals("From")) {
			str = AddressListRenderer.renderToHTMLWithLinks(
					new Address[] { (Address) bHeader.getFrom() }).toString();
		} else if (key.equals("Date")) {
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG,
					DateFormat.MEDIUM);
			str = df.format(bHeader.getDate());

			// substitute special characters like:
			//  <,>,&,\t,\n,"
			str = HtmlParser.substituteSpecialCharactersInHeaderfields(str);
		} else {
			str = (String) header.get(key);

			// substitute special characters like:
			//  <,>,&,\t,\n,"
			str = HtmlParser.substituteSpecialCharactersInHeaderfields(str);
		}

		return str;
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#isVisible()
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#updateGUI()
	 */
	public void updateGUI() throws Exception {
		getHeaderTextPane().setHeader(map);
		getStatusPanel().setStatus(hasAttachment);
	}

	/**
	 * @return Returns the headerTextPane.
	 */
	public HeaderTextPane getHeaderTextPane() {
		return headerTextPane;
	}

	/**
	 * @see javax.swing.JComponent#updateUI()
	 */
	public void updateUI() {
		super.updateUI();

		setBackground(Color.white);
	}

	/**
	 * @return Returns the statusPanel.
	 */
	public StatusPanel getStatusPanel() {
		return statusPanel;
	}

	/**
	 * IViewer displays the message headers, including From:, To:, Subject:,
	 * etc.
	 * 
	 * @author fdietz
	 */
	public class HeaderTextPane extends JTextPane {

		/*
		 * *20030720, karlpeder* Adjusted layout of header table to avoid lines
		 * extending the right margin (bug #774117)
		 */

		// background: ebebeb
		// frame: d5d5d5
		private static final String LEFT_COLUMN_PROPERTIES = "border=\"0\" nowrap font=\"dialog\" align=\"right\" valign=\"top\" width=\"5%\"";

		//width=\"65\"";
		private static final String RIGHT_COLUMN_PROPERTIES = "border=\"0\" align=\"left\" valign=\"top\" width=\"90%\"";

		private static final String RIGHT_STATUS_COLUMN_PROPERTIES = "border=\"0\" align=\"right\" valign=\"top\" width=\"90%\"";

		private static final String OUTTER_TABLE_PROPERTIES = "border=\"0\" cellspacing=\"1\" cellpadding=\"1\" "
				+ "align=\"left\" width=\"100%\"";

		// stylesheet is created dynamically because
		// user configurable fonts are used
		private String css = "";

		public HeaderTextPane() {
			setMargin(new Insets(5, 5, 5, 5));
			setEditable(false);

			HTMLEditorKit editorKit = new HTMLEditorKit();

			setEditorKit(editorKit);

			// setup base url in order to be able to display images
			// in html-component
			URL baseUrl = DiskIO.getResourceURL("org/columba/core/images/");
			LOG.info(baseUrl.toString());
			((HTMLDocument) getDocument()).setBase(baseUrl);

			initStyleSheet();
		}

		/**
		 * @see javax.swing.JComponent#updateUI()
		 */
		public void updateUI() {
			super.updateUI();

			// lightgray background
			setBackground(new Color(235, 235, 235));
		}

		/**
		 * 
		 * read text-properties from configuration and create a stylesheet for
		 * the html-document
		 *  
		 */
		protected void initStyleSheet() {

			Font font = UIManager.getFont("Label.font");
			String name = font.getName();
			int size = font.getSize();

			// create css-stylesheet string
			// set font of html-element <TD>
			css = "<style type=\"text/css\"><!--td {font-family:\"" + name
					+ "\"; font-size:\"" + size + "pt\"}--></style>";
		}

		public void setHeader(Map keys) {
			if (keys == null)
				throw new IllegalArgumentException("keys == null");

			//      border #949494
			// background #989898
			// #a0a0a0
			// bright #d5d5d5
			StringBuffer buf = new StringBuffer();

			// prepend HTML-code
			//        buf.append("<HTML><HEAD>" + css + "</HEAD><BODY ><TABLE "
			//                + OUTTER_TABLE_PROPERTIES + ">");

			buf.append("<HTML><HEAD>" + css + "</HEAD><BODY >");

			buf.append("<TABLE " + OUTTER_TABLE_PROPERTIES + ">");

			// for every existing headerfield
			for (Iterator it = keys.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();

				//          create left column
				buf.append("<TR><TD " + LEFT_COLUMN_PROPERTIES + ">");

				// set left column text
				buf.append("<B>" + key + " : </B></TD>");

				// create right column
				buf.append("<TD " + RIGHT_COLUMN_PROPERTIES + ">");

				String value = (String) keys.get(key);

				buf.append(" " + value + "</TD>");

				buf.append("</TR>");

			}

			// close HTML document
			buf.append("</TABLE></BODY></HTML>");

			// display html-text
			setText(buf.toString());
		}
	}

	public class StatusPanel extends JPanel implements SecurityStatusListener {

		private ImageIcon attachment = ImageLoader
				.getImageIcon("stock_attach.png");

		private JLabel attachmentLabel;

		private JLabel decryptionLabel;

		private JPanel leftPanel;

		public StatusPanel() {

			setLayout(new FlowLayout());

			attachmentLabel = new JLabel();
			decryptionLabel = new JLabel();
			add(attachmentLabel);
			add(decryptionLabel);
		}

		public void updateUI() {
			super.updateUI();

			//      lightgray background
			setBackground(new Color(235, 235, 235));
		}

		public void setStatus(boolean hasAttachment) {
			if (hasAttachment) {
				attachmentLabel.setIcon(attachment);

			} else {
				attachmentLabel.setIcon(null);

			}
		}

		/**
		 * @see org.columba.mail.gui.message.filter.SecurityStatusListener#statusUpdate(org.columba.mail.gui.message.filter.SecurityStatusEvent)
		 */
		public void statusUpdate(SecurityStatusEvent event) {

			int status = event.getStatus();

			switch (status) {
			case EncryptionStatusViewer.DECRYPTION_SUCCESS: {
				decryptionLabel.setIcon(ImageLoader
						.getImageIcon("pgp-signature-ok-24.png"));
				decryptionLabel.setToolTipText(MailResourceLoader.getString(
						"menu", "mainframe", "security_decrypt_success"));

				break;
			}

			case EncryptionStatusViewer.DECRYPTION_FAILURE: {
				decryptionLabel.setIcon(ImageLoader
						.getImageIcon("pgp-signature-bad-24.png"));
				decryptionLabel.setToolTipText(MailResourceLoader.getString(
						"menu", "mainframe", "security_encrypt_fail"));

				break;
			}

			case EncryptionStatusViewer.VERIFICATION_SUCCESS: {
				decryptionLabel.setIcon(ImageLoader
						.getImageIcon("pgp-signature-ok-24.png"));
				decryptionLabel.setToolTipText(MailResourceLoader.getString(
						"menu", "mainframe", "security_verify_success"));

				break;
			}

			case EncryptionStatusViewer.VERIFICATION_FAILURE: {
				decryptionLabel.setIcon(ImageLoader
						.getImageIcon("pgp-signature-bad-24.png"));
				decryptionLabel.setToolTipText(MailResourceLoader.getString(
						"menu", "mainframe", "security_verify_fail"));

				break;
			}

			case EncryptionStatusViewer.NO_KEY: {
				decryptionLabel.setIcon(ImageLoader
						.getImageIcon("pgp-signature-nokey-24.png"));
				decryptionLabel.setToolTipText(MailResourceLoader.getString(
						"menu", "mainframe", "security_verify_nokey"));

				break;
			}
			case EncryptionStatusViewer.NOOP: {
				decryptionLabel.setIcon(null);
				decryptionLabel.setToolTipText("");
				break;
			}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6383.java