error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8736.java
text:
```scala
r@@eturn item.getBooleanWithDefault("inline_attachments", false);

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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import org.columba.core.config.DefaultItem;
import org.columba.core.gui.util.FontProperties;
import org.columba.core.xml.XmlElement;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.message.MessageController;
import org.columba.mail.gui.message.filter.PGPMessageFilter;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;

/**
 * IViewer for a complete RFC822 message.
 * 
 * @author fdietz
 */
public class Rfc822MessageViewer extends JPanel implements ICustomViewer,
		Scrollable {

	protected AttachmentsViewer attachmentsViewer;

	private InlineAttachmentsViewer inlineAttachmentsViewer;

	private EncryptionStatusViewer securityInformationController;

	private TextViewer bodytextViewer;

	private SpamStatusViewer spamStatusController;

	private HeaderViewer headerController;

	private PGPMessageFilter pgpFilter;

	private MessageController mediator;

	/**
	 *  
	 */
	public Rfc822MessageViewer(MessageController mediator) {
		super();

		this.mediator = mediator;

		initComponents();

		layoutComponents();

	}

	private boolean showAttachmentsInlineEnabled() {
		XmlElement gui = MailConfig.getInstance().get("options").getElement(
				"/options/gui");
		XmlElement messageviewer = gui.getElement("messageviewer");

		if (messageviewer == null) {
			messageviewer = gui.addSubElement("messageviewer");
		}

		DefaultItem item = new DefaultItem(messageviewer);
		return item.getBoolean("inline_attachments", false);
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#view(org.columba.mail.folder.IMailbox,
	 *      java.lang.Object, org.columba.mail.gui.frame.MailFrameMediator)
	 */
	public void view(IMailbox folder, Object uid, MailFrameMediator mediator)
			throws Exception {

		//		 if necessary decrypt/verify message
		IMailFolderCommandReference newRefs = filterMessage(folder, uid);

		// map to new reference
		if (newRefs != null) {
			folder = (AbstractMessageFolder) newRefs.getSourceFolder();
			uid = newRefs.getUids()[0];
		}

		MimeTree mimePartTree = folder.getMimePartTree(uid);
		MimePart mp = chooseBodyPart(mimePartTree);
		if (mp != null)
			getBodytextViewer().view(folder, uid, mp.getAddress(), mediator);

		getHeaderController().view(folder, uid, mediator);

		if (showAttachmentsInlineEnabled())
			inlineAttachmentsViewer.view(folder, uid, mediator);
		else
			attachmentsViewer.view(folder, uid, mediator);

		getSpamStatusViewer().view(folder, uid, mediator);
		getSecurityInformationViewer().view(folder, uid, mediator);
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#updateGUI()
	 */
	public void updateGUI() throws Exception {
		getBodytextViewer().updateGUI();
		getHeaderController().updateGUI();

		if (showAttachmentsInlineEnabled())
			inlineAttachmentsViewer.updateGUI();
		else
			attachmentsViewer.updateGUI();
		getSpamStatusViewer().updateGUI();
		getSecurityInformationViewer().updateGUI();

		layoutComponents();
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#getView()
	 */
	public JComponent getView() {

		return this;
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#isVisible()
	 */
	public boolean isVisible() {
		return true;
	}

	/**
	 * @return Returns the attachmentsViewer.
	 */
	public AttachmentsViewer getAttachmentsViewer() {
		return attachmentsViewer;
	}

	/**
	 * @return Returns the bodytextViewer.
	 */
	public TextViewer getBodytextViewer() {
		return bodytextViewer;
	}

	/**
	 * @return Returns the headerController.
	 */
	public HeaderViewer getHeaderController() {
		return headerController;
	}

	/**
	 * @return Returns the inlineAttachmentsViewer.
	 */
	public InlineAttachmentsViewer getInlineAttachmentsViewer() {
		return inlineAttachmentsViewer;
	}

	/**
	 * @return Returns the pgpFilter.
	 */
	public PGPMessageFilter getPgpFilter() {
		return pgpFilter;
	}

	/**
	 * @return Returns the securityInformationController.
	 */
	public EncryptionStatusViewer getSecurityInformationViewer() {
		return securityInformationController;
	}

	/**
	 * @return Returns the spamStatusController.
	 */
	public SpamStatusViewer getSpamStatusViewer() {
		return spamStatusController;
	}

	public void setAttachmentSelectionReference(MailFolderCommandReference ref) {
		getAttachmentsViewer().setLocalReference(ref);
	}

	public MailFolderCommandReference getAttachmentSelectionReference() {
		return getAttachmentsViewer().getLocalReference();
	}

	private void initComponents() {
		spamStatusController = new SpamStatusViewer(mediator);
		bodytextViewer = new TextViewer(mediator);
		securityInformationController = new EncryptionStatusViewer(mediator);
		headerController = new HeaderViewer(mediator);

		attachmentsViewer = new AttachmentsViewer(mediator);

		inlineAttachmentsViewer = new InlineAttachmentsViewer(mediator);

		pgpFilter = new PGPMessageFilter(mediator.getFrameController(), this);
		pgpFilter.addSecurityStatusListener(securityInformationController);
		pgpFilter.addSecurityStatusListener(headerController.getStatusPanel());

	}

	private void layoutComponents() {

		removeAll();

		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());

		if (spamStatusController.isVisible())
			top.add(spamStatusController.getView(), BorderLayout.NORTH);

		if (headerController.isVisible())
			top.add(headerController.getView(), BorderLayout.CENTER);

		add(top, BorderLayout.NORTH);

		if (!showAttachmentsInlineEnabled())
			add(bodytextViewer, BorderLayout.CENTER);

		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());

		if (securityInformationController.isVisible())
			bottom.add(securityInformationController.getView(),
					BorderLayout.NORTH);

		if (showAttachmentsInlineEnabled())
			bottom.add(inlineAttachmentsViewer, BorderLayout.CENTER);
		else
			bottom.add(attachmentsViewer, BorderLayout.CENTER);

		add(bottom, BorderLayout.SOUTH);
	}

	public void clear() {
		removeAll();
	}

	/**
	 * @see org.columba.mail.gui.message.IMessageController#filterMessage(org.columba.mail.folder.IMailbox,
	 *      java.lang.Object)
	 */
	public IMailFolderCommandReference filterMessage(IMailbox folder, Object uid)
			throws Exception {
		return getPgpFilter().filter(folder, uid);
	}

	public String getSelectedText() {
		return getBodytextViewer().getSelectedText();
	}

	/** ************** Scrollable interface ******************** */

	/**
	 * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	/**
	 * @see javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle,
	 *      int, int)
	 */
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		Font textFont = FontProperties.getTextFont();

		return textFont.getSize() * 3;
	}

	/**
	 * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle,
	 *      int, int)
	 */
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		Font textFont = FontProperties.getTextFont();

		return textFont.getSize() * 10;
	}

	/**
	 * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
	 */
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	/**
	 * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
	 */
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	private MimePart chooseBodyPart(MimeTree mimePartTree) {
		MimePart bodyPart = null;

		XmlElement html = MailConfig.getInstance().getMainFrameOptionsConfig()
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

		return bodyPart;

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
	 * @param mimePartTree
	 */
	private Integer[] getBodyPartAddress(MimeTree mimePartTree) {
		MimePart bodyPart = null;
		XmlElement html = MailConfig.getInstance().getMainFrameOptionsConfig()
				.getRoot().getElement("/options/html");

		// Which Bodypart shall be shown? (html/plain)
		if ((Boolean.valueOf(html.getAttribute("prefer")).booleanValue())
				&& hasHtmlPart(mimePartTree.getRootMimeNode())) {
			bodyPart = mimePartTree.getFirstTextPart("html");
		} else {
			bodyPart = mimePartTree.getFirstTextPart("plain");
		}

		return bodyPart.getAddress();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8736.java