error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9605.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9605.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9605.java
text:
```scala
c@@.getMessageViewerDockable().setTitle(title);

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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.columba.core.config.DefaultItem;
import org.columba.core.config.IDefaultItem;
import org.columba.core.gui.base.AscendingIcon;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.frame.ThreePaneMailFrameController;
import org.columba.mail.gui.message.MessageController;
import org.columba.mail.gui.message.action.AddToAddressbookAction;
import org.columba.mail.gui.message.action.ComposeMessageAction;
import org.columba.mail.gui.message.action.OpenAttachmentAction;
import org.columba.mail.gui.message.action.SaveAsAttachmentAction;
import org.columba.mail.parser.text.HtmlParser;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.message.BasicHeader;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;
import org.columba.ristretto.message.MimeType;
import org.columba.ristretto.message.StreamableMimePart;
import org.frapuccino.dynamicitemlistpanel.DynamicItemListPanel;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Shows the headers of a RFC822 message in a lightgray box in the top of the
 * message viewer.
 * 
 * @author fdietz
 */
public class HeaderViewer extends JPanel {

	// contains headerfields which are to be displayed
	private Map map;

	private boolean visible;

	private HeaderPanel headerTextPane;

	private boolean hasAttachment;

	private AttachmentsViewer attachmentViewer;

	private MessageController mediator;

	// TODO (@author fdietz):  this should be changed into a "real" window
	private JPopupMenu attachmentViewerPopup = new JPopupMenu();

	private static DateFormat DATE_FORMATTER = DateFormat.getDateTimeInstance(
			DateFormat.LONG, DateFormat.MEDIUM);

	private SecurityStatusViewer securityInfoViewer;

	private SpamStatusViewer spamInfoViewer;

	private JPopupMenu attachmentPopup;

	public HeaderViewer(MessageController mediator,
			SecurityStatusViewer securityInfoViewer,
			SpamStatusViewer spamInfoViewer) {

		this.mediator = mediator;
		this.securityInfoViewer = securityInfoViewer;
		this.spamInfoViewer = spamInfoViewer;

		setBorder(new HeaderSeparatorBorder(Color.LIGHT_GRAY));

		// setBackground(UIManager.getColor("TextField.background"));

		setOpaque(false);
		headerTextPane = new HeaderPanel();

		attachmentViewer = new AttachmentsViewer(mediator);

		attachmentViewerPopup.setBorder(new MessageBorder(Color.LIGHT_GRAY, 1,
				true));

		layoutComponents();

		visible = false;
	}

	private void layoutComponents() {
		removeAll();

		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		// top.setBackground(UIManager.getColor("TextField.background"));
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		if (securityInfoViewer.isVisible()) {
			top.add(securityInfoViewer);
		}
		if (spamInfoViewer.isVisible()) {
			top.add(spamInfoViewer);
		}

		if (securityInfoViewer.isVisible() || spamInfoViewer.isVisible())
			add(top, BorderLayout.NORTH);

		add(headerTextPane, BorderLayout.CENTER);

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
		int style = item.getIntegerWithDefault("style", 0);

		map = new LinkedHashMap();
		Header header = null;
		String[] headers = null;
		switch (style) {
		case 0:
			// default
			headers = new String[] { "Subject", "Date", "From", "To", "Cc" };

			// get header from folder
			header = folder.getHeaderFields(uid, headers);

			// transform headers if necessary
			for (int i = 0; i < headers.length; i++) {
				String key = headers[i];
				JComponent[] value = transformHeaderField(header, key);

				JButton trailingItem = new JButton("more...");
				trailingItem = createLinkButton(trailingItem);
				trailingItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						showAddressListDialog();
					}

				});

				DynamicItemListPanel p = new DynamicItemListPanel(2,
						trailingItem, true);
				p.setOpaque(false);

				for (int j = 0; j < value.length; j++) {
					p.addItem(value[j]);
				}

				if (value.length > 0)
					map.put(key, p);
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
				JComponent[] value = transformHeaderField(header, key);

				if (value.length > 0)
					map.put(key, value);
			}

			break;
		case 2:
			// default
			headers = new String[] { "From" };

			// get header from folder
			header = folder.getHeaderFields(uid, headers);

			// transform headers if necessary
			for (int i = 0; i < headers.length; i++) {
				String key = headers[i];
				JComponent[] value = transformHeaderField(header, key);

				JButton trailingItem = new JButton("more...");
				trailingItem = createLinkButton(trailingItem);
				trailingItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						showAddressListDialog();
					}

				});

				DynamicItemListPanel p = new DynamicItemListPanel(2,
						trailingItem, true);
				p.setOpaque(false);

				for (int j = 0; j < value.length; j++) {
					p.addItem(value[j]);
				}

				if (value.length > 0)
					map.put(key, p);
			}

			break;
		}

		hasAttachment = ((Boolean) folder.getAttribute(uid,
				"columba.attachment")).booleanValue();

		if (hasAttachment) {
			JComponent[] value = createAttachmentComponentArray(folder, uid);

			// create a view more button, responsible for
			// opening the attachment viewer popup
			JButton moreButton = createAttachmentMoreButton();

			DynamicItemListPanel p = new DynamicItemListPanel(2, null, true);
			p.setShowLastSeparator(false);
			p.setOpaque(false);

			for (int j = 0; j < value.length; j++) {
				p.addItem(value[j]);
			}

			p.addItem(moreButton);

			// TODO i18n "attachments" label
			if (value.length > 0)
				map.put("Attachments", p);

		}

		headerTextPane.setHeader(map);

		attachmentViewer.view(folder, uid, mediator);

		// hack to support dockable view title update
		// TODO replace with listener pattern
		if (mediator instanceof ThreePaneMailFrameController) {
			final ThreePaneMailFrameController c = (ThreePaneMailFrameController) mediator;
			// get header from folder
			final String title = (String) folder.getAttribute(uid,
					"columba.subject");

			// awt-event-thread
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					c.getMessageViewerPanel().setTitle(title);
				}
			});

		}

		visible = true;

	}

	private JComponent[] createAttachmentComponentArray(final IMailbox folder,
			final Object uid) throws Exception {
		Vector vector = new Vector();

		MimeTree model = folder.getMimePartTree(uid);
		List displayedMimeParts = model.getAllLeafs();

		// remove body part if already shown in text viewer
		removeBodyParts(model, displayedMimeParts);

		// Display resulting MimeParts
		for (int i = 0; i < displayedMimeParts.size(); i++) {
			final StreamableMimePart mp = (StreamableMimePart) displayedMimeParts
					.get(i);

			// create attachment component with text, icon
			// tooltip, context menu and double-click action
			JButton button = createAttachmentItem(mp);

			vector.add(button);
		}

		// // create a view more button, responsible for
		// // opening the attachment viewer popup
		// JButton moreButton = createAttachmentMoreButton();

		// vector.add(moreButton);

		return (JComponent[]) vector.toArray(new JComponent[0]);
	}

	/**
	 * create a view more button, responsible for opening the attachment viewer
	 * popup
	 */
	private JButton createAttachmentMoreButton() {
		ImageIcon icon = new AscendingIcon();
		final JButton moreButton = new JButton(icon);
		moreButton.setMargin(new Insets(0, 1, 0, 0));

		moreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				toggleAttachmentPopupVisibility();
			}

		});

		return moreButton;
	}

	/**
	 * Create attachment component with name, icon, tooltip description, context
	 * menu and double-click action.
	 */
	private JButton createAttachmentItem(final StreamableMimePart mp) {
		MimeHeader header = mp.getHeader();
		MimeType type = header.getMimeType();

		String contentType = type.getType();
		String contentSubtype = type.getSubtype();

		String text = null;

		// Get Text for Icon
		if (header.getFileName() != null) {
			text = header.getFileName();
		} else {
			text = contentType + "/" + contentSubtype;
		}

		// Get Tooltip for Icon
		StringBuffer tooltip = new StringBuffer();
		tooltip.append("<html><body>");

		if (header.getFileName() != null) {
			tooltip.append(header.getFileName());
			tooltip.append(" - ");
		}

		tooltip.append("<i>");

		if (header.getContentDescription() != null) {
			tooltip.append(header.getContentDescription());
		} else {
			tooltip.append(contentType);
			tooltip.append("/");
			tooltip.append(contentSubtype);
		}

		tooltip.append("</i></body></html>");

		ImageIcon image = null;

		image = new AttachmentImageIconLoader().getImageIcon(type.getType(),
				type.getSubtype());

		// scale image
		image = new ImageIcon(image.getImage().getScaledInstance(16, 16,
				Image.SCALE_SMOOTH));

		JButton button = new JButton(text, image);
		button = createLabelButton(button);
		// button.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		button.setToolTipText(tooltip.toString());

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				new OpenAttachmentAction(mediator.getFrameController(), mp
						.getAddress()).actionPerformed(event);

			}
		});

		button.addMouseListener(new PopupListener(createAttachmentPopupMenu(mp
				.getAddress())));

		return button;
	}

	/**
	 * Remove the first mimepart of contentype "text", as it is already shown in
	 * text viewer and should not appear in the attachment list again.
	 */
	private void removeBodyParts(MimeTree model, List displayedMimeParts) {
		// Remove the BodyPart(s) if any
		StreamableMimePart bodyPart = (StreamableMimePart) model
				.getFirstTextPart("plain");

		if (bodyPart != null) {
			MimePart bodyParent = bodyPart.getParent();

			if (bodyParent != null) {
				if (bodyParent.getHeader().getMimeType().getSubtype().equals(
						"alternative")) {
					List bodyParts = bodyParent.getChilds();
					displayedMimeParts.removeAll(bodyParts);
				} else {
					displayedMimeParts.remove(bodyPart);
				}
			} else {
				displayedMimeParts.remove(bodyPart);
			}
		}
	}

	private void toggleAttachmentPopupVisibility() {
		if (attachmentPopup == null) {
			attachmentPopup = new JPopupMenu();

			JPanel panel = createAttachmentViewerPanel();

			attachmentPopup.add(panel);
		}

		if (attachmentPopup.isVisible())
			attachmentPopup.setVisible(false);
		else {
			attachmentPopup.show(this, 0, getHeight() - 1);
		}
	}

	private JPanel createAttachmentViewerPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(HeaderViewer.this.getWidth() - 1,
				(int) mediator.getHeight() / 2));
		panel.setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(new HeaderSeparatorBorder(Color.LIGHT_GRAY));

		// TODO i18n "Close" button
		JButton closeButton = new JButton("Close");
		closeButton.setDefaultCapable(true);
		closeButton.setMnemonic('C');
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				toggleAttachmentPopupVisibility();
			}
		});

		// TODO i18n "Help" button
		JButton helpButton = new JButton("Help");
		helpButton.setMnemonic('h');

		centerPanel.add(attachmentViewer, BorderLayout.CENTER);

		panel.add(centerPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(UIManager.getColor("TextField.background"));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		ButtonBarBuilder builder = new ButtonBarBuilder(bottomPanel);
		// builder.setDefaultButtonBarGapBorder();
		builder.addGlue();
		builder.addGriddedButtons(new JButton[] { closeButton, helpButton });

		panel.add(bottomPanel, BorderLayout.SOUTH);

		add(panel, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Generic Popup listener.
	 */
	class PopupListener extends MouseAdapter {
		private JPopupMenu menu;

		public PopupListener(JPopupMenu menu) {
			this.menu = menu;
		}

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	private JPopupMenu createAttachmentPopupMenu(Integer[] address) {
		JPopupMenu popup = new JPopupMenu();
		popup.add(new OpenAttachmentAction(mediator.getFrameController(),
				address));
		popup.add(new SaveAsAttachmentAction(mediator.getFrameController(),
				address));

		return popup;
	}

	private JPopupMenu createAddressPopupMenu(String emailAddress) {
		JPopupMenu popup = new JPopupMenu();
		popup.add(new ComposeMessageAction(mediator.getFrameController(),
				emailAddress));
		popup.add(new AddToAddressbookAction(mediator.getFrameController(),
				emailAddress));

		return popup;
	}

	/**
	 * Imageloader using a content-type and subtype to determine the image name.
	 * <p>
	 * Automatically falls back to the default icon.
	 */
	class AttachmentImageIconLoader {

		/**
		 * Utility constructor.
		 */
		private AttachmentImageIconLoader() {
		}

		/**
		 * Returns the image icon for the content type.
		 * 
		 * @param contentType
		 *            content type
		 * @param contentSubtype
		 *            content sub type
		 * @return an Image Icon for the content type.
		 */
		public ImageIcon getImageIcon(String contentType, String contentSubtype) {
			StringBuffer buf = new StringBuffer();
			buf.append("mime/gnome-");
			buf.append(contentType);
			buf.append("-");
			buf.append(contentSubtype);
			buf.append(".png");

			ImageIcon icon = ImageLoader.getUnsafeImageIcon(buf.toString());

			if (icon == null) {
				icon = ImageLoader.getUnsafeImageIcon("mime/gnome-"
						+ contentType + ".png");
			}

			if (icon == null) {
				icon = ImageLoader.getUnsafeImageIcon("mime/gnome-text.png");
			}

			return icon;
		}
	}

	/**
	 * @see org.columba.mail.gui.message.viewer.IViewer#getView()
	 */
	public JComponent getView() {
		return this;
	}

	protected JComponent[] transformHeaderField(Header header, String key) {
		if (header == null)
			throw new IllegalArgumentException("header == null");
		if (key == null)
			throw new IllegalArgumentException("key == null");
		BasicHeader bHeader = new BasicHeader(header);
		// message doesn't contain this headerfield
		if (header.get(key) == null) {
			return new JComponent[0];
		}

		// headerfield is empty
		if (((String) header.get(key)).length() == 0) {
			return new JComponent[0];
		}

		if (key.equals("Subject")) {
			String str = bHeader.getSubject();

			// substitute special characters like:
			// <,>,&,\t,\n,"
			str = HtmlParser.substituteSpecialCharactersInHeaderfields(str);
			return new JComponent[] { new JLabel(str) };
		} else if (key.equals("To")) {
			
			return createRecipientComponentArray(bHeader.getTo());
		} else if (key.equals("Reply-To")) {
			

			return createRecipientComponentArray(bHeader.getReplyTo());
		} else if (key.equals("Cc")) {
			
			return createRecipientComponentArray(bHeader.getCc());
		} else if (key.equals("Bcc")) {
			
			return createRecipientComponentArray(bHeader.getBcc());
		} else if (key.equals("From")) {
			
			return createRecipientComponentArray(new Address[] { (Address) bHeader
					.getFrom() });
		} else if (key.equals("Date")) {
			String str = DATE_FORMATTER.format(bHeader.getDate());

			// substitute special characters like:
			// <,>,&,\t,\n,"
			str = HtmlParser.substituteSpecialCharactersInHeaderfields(str);
			return new JComponent[] { new JLabel(str) };
		} else {
			String str = (String) header.get(key);

			// substitute special characters like:
			// <,>,&,\t,\n,"
			str = HtmlParser.substituteSpecialCharactersInHeaderfields(str);
			return new JComponent[] { new JLabel(str) };
		}
	}

	private JComponent[] createRecipientComponentArray(Address[] addressArray) {
		Vector v = new Vector();
		for (int j = 0; j < addressArray.length; j++) {
			final Address adr = addressArray[j];

			JButton button = new JButton();

			StringBuffer result = new StringBuffer();
			String displayName = adr.getDisplayName();

			if ((displayName != null) && (displayName.length() != 0)) {
				result.append(displayName);
				result.append(" ");
				result.append("<" + adr.getMailAddress() + ">");

				button.setText(adr.getDisplayName());

			} else {
				result.append(adr.getMailAddress());
				button.setText(adr.getMailAddress());
			}

			final String label = result.toString();
			button.setToolTipText(label);

			// button.setOpaque(false);
			button = createLinkButton(button);

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					new ComposeMessageAction(mediator.getFrameController(),
							label).actionPerformed(event);

				}
			});

			button.addMouseListener(new PopupListener(
					createAddressPopupMenu(label)));

			v.add(button);
		}
		return (JComponent[]) v.toArray(new JComponent[0]);
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

		headerTextPane.updateGUI();

		layoutComponents();

		revalidate();

		repaint();

		attachmentViewer.updateGUI();
	}

	private void showAddressListDialog() {
		new AddressListDialog(mediator.getFrameController().getContainer()
				.getFrame());

	}

	/**
	 * Shows Subject, From, Date, To message headers and list of attachments.
	 */
	class HeaderPanel extends JPanel {

		private Map keys;

		public HeaderPanel() {
			super();
			setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
		}

		/**
		 * @see javax.swing.JComponent#updateUI()
		 */
		public void updateUI() {

			// setBackground(UIManager.getColor("TextField.background"));
		}

		public void setHeader(Map keys) {
			if (keys == null)
				throw new IllegalArgumentException("keys == null");

			this.keys = keys;
		}

		public void updateGUI() {
			if (keys == null)
				return;

			removeAll();

			// Color backgroundColor =
			// UIManager.getColor("TextField.background");

			FormLayout layout = new FormLayout(
					"right:pref, 3dlu, pref, 3dlu, fill:pref:grow",
					"top:pref:grow");
			DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);

			for (Iterator it = keys.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();

				DynamicItemListPanel value = (DynamicItemListPanel) keys
						.get(key);

				JLabel keyLabel = new JLabel("<html><b>" + key + "</b></html>");

				JLabel separator = new JLabel(":");

				// JButton trailingItem = new JButton("more...");
				//			
				// trailingItem = LinkButton.createLinkButton(trailingItem);
				// trailingItem.addActionListener(new ActionListener() {
				// public void actionPerformed(ActionEvent event) {
				// showAddressListDialog();
				// }
				//
				// });
				//
				// DynamicItemListPanel p = new DynamicItemListPanel(2,
				// trailingItem, true);
				// p.setOpaque(false);
				//
				// for (int i = 0; i < value.length; i++) {
				//					
				// p.addItem(value[i]);
				// }

				builder.append(keyLabel);
				builder.append(separator);
				builder.append(value);

				builder.appendRow(new RowSpec("top:pref:grow"));

			}

			validate();
		}

	}

	private class ListItem {
		String name;

		JComponent component;

		ImageIcon icon;
	}

	private static final Color LINK_COLOR = Color.blue;

	private static final Color LABEL_COLOR = UIManager
			.getColor("Label.foreground");

	private static final Border LINK_BORDER = BorderFactory.createEmptyBorder(
			0, 0, 1, 0);

	public static JButton createLinkButton(JButton button) {
		button.setBorder(LINK_BORDER);
		button.setForeground(LINK_COLOR);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setFocusPainted(false);
		button.setRequestFocusEnabled(false);
		button.setText("<html><u>" + button.getText() + "</u></html>");
		button.setContentAreaFilled(false);
		// button.addMouseListener(new LinkMouseListener());
		return button;
	}

	public static JButton createLabelButton(JButton button) {
		button.setBorder(LINK_BORDER);
		button.setForeground(LABEL_COLOR);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setFocusPainted(false);
		button.setRequestFocusEnabled(false);
		button.setContentAreaFilled(false);
		button.setText("<html><u>" + button.getText() + "</u></html>");
		// button.addMouseListener(new LabelMouseListener());
		return button;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9605.java