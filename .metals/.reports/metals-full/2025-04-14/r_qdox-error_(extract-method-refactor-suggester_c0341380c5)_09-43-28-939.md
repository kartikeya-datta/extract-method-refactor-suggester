error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/708.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/708.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/708.java
text:
```scala
.@@getString("org.columba.core.i18n.dialog", "error", "no_browser"),

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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.message;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observer;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.columba.core.charset.CharsetEvent;
import org.columba.core.charset.CharsetListener;
import org.columba.core.charset.CharsetOwnerInterface;
import org.columba.core.command.CommandProcessor;
import org.columba.core.gui.frame.DefaultContainer;
import org.columba.core.gui.menu.ColumbaPopupMenu;
import org.columba.core.util.GlobalResourceLoader;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.message.command.ViewMessageCommand;
import org.columba.mail.gui.message.util.ColumbaURL;
import org.columba.mail.gui.message.viewer.Rfc822MessageViewer;
import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;

/**
 * this class shows the messagebody
 */
public class MessageController extends JScrollPane implements
		HyperlinkListener, CharsetListener, IMessageController {
	
	private MailFrameMediator frameController;

	private MouseListener listener;

	private int active;

	private JPanel panel;

	private Rfc822MessageViewer messageViewer;

	private URLObservable urlObservable;

	private ColumbaPopupMenu menu;

	private URLMouseListener mouseListener;

	public MessageController(MailFrameMediator frameMediator) {
		this.frameController = frameMediator;

		mouseListener = new URLMouseListener();

		messageViewer = new Rfc822MessageViewer(this);

		// FIXME: no hardcoded Color.white
		getViewport().setBackground(Color.white);

		setViewportView(messageViewer);

		((CharsetOwnerInterface) getFrameController()).addCharsetListener(this);

		urlObservable = new URLObservable();
	}

	public void clear() {
		messageViewer.clear();

		setViewportView(messageViewer);
	}

	public void hyperlinkUpdate(HyperlinkEvent e) {
	}

	/**
	 * Returns the mailFrameController.
	 * 
	 * @return MailFrameController
	 */
	public MailFrameMediator getFrameController() {
		return frameController;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.core.util.CharsetListener#charsetChanged(org.columba.core.util.CharsetEvent)
	 */
	public void charsetChanged(CharsetEvent e) {
		CommandProcessor.getInstance().addOp(
				new ViewMessageCommand(getFrameController(),
						((MailFrameMediator) getFrameController())
								.getTableSelection()));
	}

	/** ************************ CaretUpdateListener interface **************** */

	/** *********************************************************************** */

	/**
	 * Show message in messages viewer.
	 * <p>
	 * Should be called in Command.execute() or in another background thread.
	 * 
	 * @param folder
	 *            selected folder
	 * @param uid
	 *            selected message UID
	 * @throws Exception
	 */
	public void showMessage(IMailbox folder, Object uid) throws Exception {

		messageViewer.view(folder, uid, (MailFrameMediator) frameController);

	}

	/**
	 * Revalidate message viewer components.
	 * <p>
	 * Call this method after showMessage() to force a repaint():
	 *  
	 */
	public void updateGUI() throws Exception {

		messageViewer.updateGUI();

		getVerticalScrollBar().setValue(0);
	}

	/**
	 * @return Returns the messageViewer.
	 */
	public Rfc822MessageViewer getMessageViewer() {
		return messageViewer;
	}

	public MailFolderCommandReference getAttachmentSelectionReference() {
		return getMessageViewer().getAttachmentsViewer().getLocalReference();
	}

	public void addURLObserver(Observer observer) {
		getUrlObservable().addObserver(observer);
	}

	public String getSelectedText() {
		return getMessageViewer().getSelectedText();
	}

	protected void processPopup(MouseEvent ev) {
		//        final URL url = extractURL(ev);
		ColumbaURL mailto = extractMailToURL(ev);
		urlObservable.setUrl(mailto);

		final MouseEvent event = ev;
		// open context-menu
		// -> this has to happen in the awt-event dispatcher thread
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				getPopupMenu().show(event.getComponent(), event.getX(),
						event.getY());
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
		AttributeSet a = e.getAttributes();
		AttributeSet anchor = (AttributeSet) a.getAttribute(HTML.Tag.A);

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

			getUrlObservable().setUrl(new ColumbaURL(url));

			//URLController c = new URLController();

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
				try {
					Desktop.browse(url);
				} catch (DesktopException e) {
					JOptionPane.showMessageDialog(null, GlobalResourceLoader
							.getString("dialog", "error", "no_browser"),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * @return Returns the urlObservable.
	 */
	public URLObservable getUrlObservable() {
		return urlObservable;
	}

	/**
	 * return the PopupMenu for the message viewer
	 */
	public JPopupMenu getPopupMenu() {
		return menu;
	}

	public void createPopupMenu() {
		if (menu == null)
			menu = new ColumbaPopupMenu(getFrameController(),
					"org/columba/mail/action/message_contextmenu.xml");
	}

	/**
	 * @see org.columba.mail.gui.message.IMessageController#addMouseListener(javax.swing.JTextPane)
	 */
	public void addMouseListener(JTextPane textPane) {
		textPane.addMouseListener(mouseListener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/708.java