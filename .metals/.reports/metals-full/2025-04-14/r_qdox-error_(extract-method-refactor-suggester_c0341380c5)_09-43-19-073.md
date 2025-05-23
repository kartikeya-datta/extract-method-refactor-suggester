error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4900.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4900.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4900.java
text:
```scala
.@@getInstance().getExtensionHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_HTMLVIEWER);

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
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.columba.api.exception.PluginException;
import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.core.charset.CharsetOwnerInterface;
import org.columba.core.config.Config;
import org.columba.core.desktop.ColumbaDesktop;
import org.columba.core.gui.frame.DefaultContainer;
import org.columba.core.gui.htmlviewer.IHTMLViewerPlugin;
import org.columba.core.gui.util.FontProperties;
import org.columba.core.io.StreamUtils;
import org.columba.core.logging.Logging;
import org.columba.core.plugin.PluginManager;
import org.columba.core.util.TempFileStore;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.MailConfig;
import org.columba.mail.config.OptionsItem;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.message.MessageController;
import org.columba.mail.gui.message.util.ColumbaURL;
import org.columba.mail.parser.text.HtmlParser;
import org.columba.ristretto.coder.Base64DecoderInputStream;
import org.columba.ristretto.coder.FallbackCharsetDecoderInputStream;
import org.columba.ristretto.coder.QuotedPrintableDecoderInputStream;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;

/**
 * Display message body text.
 * 
 * @author fdietz
 */
public class TextViewer extends JPanel implements IMimePartViewer, Observer,
		CaretListener {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.gui.message.viewer");

	private static final Pattern CIDPattern = Pattern.compile("cid:([^\"]+)",
			Pattern.CASE_INSENSITIVE);

	// stylesheet is created dynamically because
	// user configurable fonts are used
	private String css = "";

	// enable/disable smilies configuration
	private XmlElement smilies;

	private boolean enableSmilies;

	// name of font
	private String name;

	/*
	 * private String body;
	 * 
	 * private URL url;
	 */

	private String body;

	/**
	 * if true, a html message is shown. Otherwise, plain/text
	 */
	private boolean htmlMessage;

	private MessageController mediator;

	private IHTMLViewerPlugin viewerPlugin;

	private IMailbox folder;

	private Object uid;

	private boolean usingJDIC;

	public TextViewer(MessageController mediator) {
		super();

		this.mediator = mediator;

		initHTMLViewerPlugin();

		setLayout(new BorderLayout());

		add(viewerPlugin.getContainer(), BorderLayout.CENTER);

		initConfiguration();

		initStyleSheet();

		// FocusManager.getInstance().registerComponent(new MyFocusOwner());

		if (!usingJDIC)
			viewerPlugin.getComponent()
					.addMouseListener(new URLMouseListener());

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	}

	private void initHTMLViewerPlugin() {
		OptionsItem optionsItem = MailConfig.getInstance().getOptionsItem();
		String selectedBrowser = optionsItem.getStringWithDefault(
				OptionsItem.MESSAGEVIEWER, OptionsItem.SELECTED_BROWSER,
				"Default");

		try {
			viewerPlugin = createHTMLViewerPluginInstance(selectedBrowser);
			// in case of an error -> fall-back to Swing's built-in JTextPane
			if ((viewerPlugin == null) || (viewerPlugin.initialized() == false)) {
				LOG
						.severe("Error while trying to load html viewer -> falling back to default");

				viewerPlugin = createHTMLViewerPluginInstance("Default");
				// usingJDIC = false;
			} // else
			// usingJDIC = true;
		} catch (RuntimeException e) {
			viewerPlugin = createHTMLViewerPluginInstance("Default");
			// usingJDIC = false;
			e.printStackTrace();
		}

	}

	private IHTMLViewerPlugin createHTMLViewerPluginInstance(String pluginId) {
		IHTMLViewerPlugin plugin = null;
		try {

			IExtensionHandler handler =  PluginManager
					.getInstance().getHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_HTMLVIEWER);

			IExtension extension = handler.getExtension(pluginId);
			if (extension == null)
				return null;

			plugin = (IHTMLViewerPlugin) extension.instanciateExtension(null);

			return plugin;
		} catch (PluginHandlerNotFoundException e) {
			LOG.severe("Error while loading viewer plugin: " + e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();
		} catch (PluginException e) {
			LOG.severe("Error while loading viewer plugin: " + e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();
		} catch (Exception e) {
			LOG.severe("Error while loading viewer plugin: " + e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 */
	private void initConfiguration() {
		XmlElement gui = MailConfig.getInstance().get("options").getElement(
				"/options/gui");
		XmlElement messageviewer = gui.getElement("messageviewer");

		if (messageviewer == null) {
			messageviewer = gui.addSubElement("messageviewer");
		}

		messageviewer.addObserver(this);

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

		// register for configuration changes
		Font font = FontProperties.getTextFont();
		name = font.getName();

		XmlElement options = Config.getInstance().get("options").getElement(
				"/options");
		XmlElement gui1 = options.getElement("gui");
		XmlElement fonts = gui1.getElement("fonts");

		if (fonts == null) {
			fonts = gui1.addSubElement("fonts");
		}

		// register interest on configuratin changes
		fonts.addObserver(this);

		// XmlElement selectedBrowser =
		// messageviewer.getElement(OptionsItem.SELECTED_BROWSER);
		// selectedBrowser.addObserver(this);

	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IMimePartViewer#view(org.columba.mail.folder.IMailbox,
	 *      java.lang.Object, java.lang.Integer[],
	 *      org.columba.mail.gui.frame.MailFrameMediator)
	 */
	public void view(IMailbox folder, Object uid, Integer[] address,
			MailFrameMediator mediator) throws Exception {

		this.folder = folder;
		this.uid = uid;

		MimePart bodyPart = null;
		InputStream bodyStream;

		MimeTree mimePartTree = folder.getMimePartTree(uid);

		bodyPart = mimePartTree.getFromAddress(address);

		if (bodyPart == null) {
			bodyStream = new ByteArrayInputStream("<No Message-Text>"
					.getBytes());
		} else {
			// Shall we use the HTML-IViewer?
			htmlMessage = bodyPart.getHeader().getMimeType().getSubtype()
					.equals("html");

			bodyStream = folder.getMimePartBodyStream(uid, bodyPart
					.getAddress());
		}

		bodyStream = MessageParser.decodeBodyStream(bodyPart, bodyStream);

		// Which Charset shall we use ?
		if (!htmlMessage) {
			Charset charset = ((CharsetOwnerInterface) mediator).getCharset();
			charset = MessageParser.extractCharset(charset, bodyPart);

			bodyStream = new FallbackCharsetDecoderInputStream(bodyStream,
					charset);
		}

		// Read Stream in String
		StringBuffer text = StreamUtils.readCharacterStream(bodyStream);

		// if HTML stripping is enabled
		if (isHTMLStrippingEnabled()) {
			// strip HTML message -> remove all HTML tags
			text = new StringBuffer(HtmlParser.stripHtmlTags(text.toString(),
					true));

			htmlMessage = false;
		}

		if (htmlMessage) {
			// this is a HTML message
			body = text.toString();

			// Download any CIDs in the html mail
			body = downloadCIDParts(body, mimePartTree);

		} else {
			// this is a text/plain message

			body = MessageParser.transformTextToHTML(text.toString(), css,
					enableSmilies);

			// setText(body);

		}

	}

	private boolean isHTMLStrippingEnabled() {
		XmlElement html = MailConfig.getInstance().getMainFrameOptionsConfig()
				.getRoot().getElement("/options/html");

		return Boolean.valueOf(html.getAttribute("disable")).booleanValue();
	}

	/**
	 * 
	 * read text-properties from configuration and create a stylesheet for the
	 * html-document
	 * 
	 */
	private void initStyleSheet() {
		// read configuration from options.xml file
		// create css-stylesheet string
		// set font of html-element <P>

		/*
		 * css = "<style type=\"text/css\">\n" + "body {font-family:\"" + name +
		 * "\"; font-size:\"" + size + "pt; \"} \n" + "a { color: blue;
		 * text-decoration: underline }\n" + "font.quoting {color:#949494;} \n" + "</style>\n";
		 */

		css = "<style type=\"text/css\">\n" + "body {font-family:\"" + name
				+ "\";} \n" + "a { color: blue; text-decoration: underline }\n"
				+ "font.quoting {color:#949494;} \n" + "</style>\n";

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

		initStyleSheet();

		// remove old renderer
		remove(viewerPlugin.getContainer());

		// init new renderer
		initHTMLViewerPlugin();

		// add new renderer
		add(viewerPlugin.getContainer(), BorderLayout.CENTER);
	}

	public String getSelectedText() {
		return viewerPlugin.getSelectedText();
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#getView()
	 */
	public JComponent getView() {
		return this;
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#updateGUI()
	 */
	public void updateGUI() throws Exception {
		viewerPlugin.view(body);
	}

	/**
	 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
	 */
	public void caretUpdate(CaretEvent arg0) {
		// FocusManager.getInstance().updateActions();
	}

	private String downloadCIDParts(String body, MimeTree mimeTree) {
		Matcher matcher = CIDPattern.matcher(body);

		if (!matcher.find()) {
			return body;
		}

		StringBuffer modifiedBody = new StringBuffer(body.length());
		File mimePartFile;
		List mimeParts = mimeTree.getAllLeafs();

		MimePart CIDPart = findMimePart(mimeParts, matcher.group(1));
		if (CIDPart != null) {
			mimePartFile = TempFileStore.createTempFile();
			try {
				downloadMimePart(CIDPart, mimePartFile);

				matcher.appendReplacement(modifiedBody, mimePartFile.toURL()
						.toString());
			} catch (Exception e) {
				matcher.appendReplacement(modifiedBody, "missing");
			}
		} else {
			matcher.appendReplacement(modifiedBody, "missing");
		}

		while (matcher.find()) {
			CIDPart = findMimePart(mimeParts, matcher.group(1));
			if (CIDPart != null) {
				mimePartFile = TempFileStore.createTempFile();
				try {
					downloadMimePart(CIDPart, mimePartFile);

					matcher.appendReplacement(modifiedBody, mimePartFile
							.toURL().toString());
				} catch (Exception e) {
					matcher.appendReplacement(modifiedBody, "missing");
				}
			} else {
				matcher.appendReplacement(modifiedBody, "missing");
			}
		}

		matcher.appendTail(modifiedBody);

		return modifiedBody.toString();
	}

	private MimePart findMimePart(List mimeParts, String findCid) {
		MimePart result;
		Iterator it = mimeParts.iterator();
		while (it.hasNext()) {
			result = (MimePart) it.next();

			String cid = result.getHeader().getContentID();
			if (cid != null
					&& cid.substring(1, cid.length() - 1).equalsIgnoreCase(
							findCid)) {
				return result;
			}
		}

		return null;
	}

	private void downloadMimePart(MimePart part, File destFile)
			throws Exception {
		MimeHeader header = part.getHeader();

		InputStream bodyStream = folder.getMimePartBodyStream(uid, part
				.getAddress());

		int encoding = header.getContentTransferEncoding();

		switch (encoding) {
		case MimeHeader.QUOTED_PRINTABLE:
			bodyStream = new QuotedPrintableDecoderInputStream(bodyStream);
			break;

		case MimeHeader.BASE64:
			bodyStream = new Base64DecoderInputStream(bodyStream);
			break;
		default:
		}

		FileOutputStream fileStream = new FileOutputStream(destFile);
		StreamUtils.streamCopy(bodyStream, fileStream);
		fileStream.close();
		bodyStream.close();
	}

	/**
	 * @return Returns the htmlMessage.
	 */
	public boolean isHtmlMessage() {
		return htmlMessage;
	}

	protected void processPopup(MouseEvent ev) {
		// final URL url = extractURL(ev);
		ColumbaURL mailto = extractMailToURL(ev);
		mediator.setSelectedURL(mailto);

		final MouseEvent event = ev;
		// open context-menu
		// -> this has to happen in the awt-event dispatcher thread
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				mediator.getPopupMenu().show(event.getComponent(),
						event.getX(), event.getY());
			}
		});
	}

	protected URL extractURL(MouseEvent event) {
		JEditorPane pane = (JEditorPane) event.getSource();
		HTMLDocument doc = (HTMLDocument) pane.getDocument();

		Element e = doc.getCharacterElement(pane.viewToModel(event.getPoint()));
		AttributeSet a = e.getAttributes();
		AttributeSet anchor = (AttributeSet) a.getAttribute(HTML.Tag.A);

		if (anchor == null) {
			return null;
		}

		URL url = null;

		try {
			url = new URL((String) anchor.getAttribute(HTML.Attribute.HREF));
		} catch (MalformedURLException mue) {
			return null;
		}

		return url;
	}

	/**
	 * this method extracts any url, but if URL's protocol is mailto: then this
	 * method also extracts the corresponding recipient name whatever it may be.
	 * <br>
	 * This "kind of" superseeds the previous extractURL(MouseEvent) method.
	 */
	private ColumbaURL extractMailToURL(MouseEvent event) {

		ColumbaURL url = new ColumbaURL(extractURL(event));
		if (url.getRealURL() == null)
			return null;

		if (!url.getRealURL().getProtocol().equalsIgnoreCase("mailto"))
			return url;

		JEditorPane pane = (JEditorPane) event.getSource();
		HTMLDocument doc = (HTMLDocument) pane.getDocument();

		Element e = doc.getCharacterElement(pane.viewToModel(event.getPoint()));
		try {
			url.setSender(doc.getText(e.getStartOffset(), (e.getEndOffset() - e
					.getStartOffset())));
		} catch (BadLocationException e1) {
			url.setSender("");
		}

		return url;
	}

	class URLMouseListener implements MouseListener {

		public void mousePressed(MouseEvent event) {
			if (event.isPopupTrigger()) {
				processPopup(event);
			}
		}

		public void mouseReleased(MouseEvent event) {
			if (event.isPopupTrigger()) {
				processPopup(event);
			}
		}

		public void mouseEntered(MouseEvent event) {
		}

		public void mouseExited(MouseEvent event) {
		}

		public void mouseClicked(MouseEvent event) {
			if (!SwingUtilities.isLeftMouseButton(event)) {
				return;
			}

			URL url = extractURL(event);

			if (url == null) {
				return;
			}

			mediator.setSelectedURL(new ColumbaURL(url));

			// URLController c = new URLController();

			if (url.getProtocol().equalsIgnoreCase("mailto")) {
				// open composer
				ComposerController controller = new ComposerController();
				new DefaultContainer(controller);

				ComposerModel model = new ComposerModel();
				model.setTo(url.getFile());

				// apply model
				controller.setComposerModel(model);

				controller.updateComponents(true);
			} else {
				ColumbaDesktop.getInstance().browse(url);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4900.java