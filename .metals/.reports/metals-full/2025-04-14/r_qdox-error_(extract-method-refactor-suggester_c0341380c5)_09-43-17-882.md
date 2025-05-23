error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5115.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5115.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5115.java
text:
```scala
n@@ewBody = HtmlParser.textToHtml(oldBody, "", null, getCharset().toString());

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

package org.columba.mail.gui.composer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ContainerListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.columba.api.gui.frame.IContentPane;
import org.columba.core.charset.CharsetEvent;
import org.columba.core.charset.CharsetListener;
import org.columba.core.charset.CharsetOwnerInterface;
import org.columba.core.config.ViewItem;
import org.columba.core.gui.base.LabelWithMnemonic;
import org.columba.core.gui.frame.DefaultFrameController;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.io.DiskIO;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.composer.action.SaveAsDraftAction;
import org.columba.mail.gui.composer.html.HtmlEditorController;
import org.columba.mail.gui.composer.html.HtmlToolbar;
import org.columba.mail.gui.composer.text.TextEditorController;
import org.columba.mail.gui.composer.util.IdentityInfoPanel;
import org.columba.mail.parser.text.HtmlParser;
import org.columba.mail.util.MailResourceLoader;
import org.frapuccino.swing.MultipleTransferHandler;

import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * 
 * controller for message composer dialog
 * 
 * @author frd
 */
public class ComposerController extends DefaultFrameController implements
		CharsetOwnerInterface, IContentPane, DocumentListener,
		ItemListener {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.gui.composer");

	private IdentityInfoPanel identityInfoPanel;

	private AttachmentController attachmentController;

	private SubjectController subjectController;

	private PriorityController priorityController;

	private AccountController accountController;

	private AbstractEditorController editorController;

	private HeaderController headerController;

	private ComposerSpellCheck composerSpellCheck;

	private ComposerModel composerModel;

	private Charset charset;

	private EventListenerList listenerList = new EventListenerList();

	/** Buffer for listeners used by addContainerListenerForEditor and createView */
	private List containerListenerBuffer;

	private JSplitPane attachmentSplitPane;

	/** Editor viewer resides in this panel */
	private TextEditorPanel editorPanel;

	private LabelWithMnemonic subjectLabel;

	private LabelWithMnemonic smtpLabel;

	private LabelWithMnemonic priorityLabel;

	private JPanel centerPanel = new FormDebugPanel();

	private JPanel topPanel;

	private HtmlToolbar htmlToolbar;

	private boolean promptOnDialogClosing = true;

	private SignatureView signatureView;

	private boolean attachmentPanelShown;

	public ComposerController() {
		this(new ComposerModel(), FrameManager.getInstance()
				.createCustomViewItem("Composer"));

	}

	public ComposerController(ComposerModel model, ViewItem viewItem) {
		super(viewItem);

		// init model (defaults to empty plain text message)
		composerModel = model;

		// init controllers for different parts of the composer
		identityInfoPanel = new IdentityInfoPanel();
		attachmentController = new AttachmentController(this);
		headerController = new HeaderController(this);
		subjectController = new SubjectController(this);
		getSubjectController().getView().getDocument()
				.addDocumentListener(this);

		priorityController = new PriorityController(this);
		accountController = new AccountController(this);
		accountController.getView().addItemListener(this);
		composerSpellCheck = new ComposerSpellCheck(this);

		signatureView = new SignatureView(this);

		// set default html or text based on stored option
		// ... can be overridden by setting the composer model
		XmlElement optionsElement = MailConfig.getInstance().get(
				"composer_options").getElement("/options");
		XmlElement htmlElement = optionsElement.getElement("html");

		// create default element if not available
		if (htmlElement == null) {
			htmlElement = optionsElement.addSubElement("html");
		}

		String enableHtml = htmlElement.getAttribute("enable", "false");

		// set model based on configuration
		if (enableHtml.equals("true")) {
			getModel().setHtml(true);
		} else {
			getModel().setHtml(false);
		}

		// init controller for the editor depending on message type
		if (getModel().isHtml()) {
			editorController = new HtmlEditorController(this);
		} else {
			editorController = new TextEditorController(this);
		}

		initComponents();

		// add JPanel with useful HTML related actions.
		htmlToolbar = new HtmlToolbar(this);

		layoutComponents();

		showAttachmentPanel();

		// Hack to ensure charset is set correctly at start-up
		XmlElement charsetElement = optionsElement.getElement("charset");

		if (charsetElement != null) {
			String charset = charsetElement.getAttribute("name");

			if (charset != null) {
				try {
					setCharset(Charset.forName(charset));
				} catch (UnsupportedCharsetException ex) {
					// ignore this
				}
			}
		}

		// Setup DnD for the text and attachment list control.
		ComposerAttachmentTransferHandler dndTransferHandler = new ComposerAttachmentTransferHandler(
				attachmentController);
		attachmentController.getView().setDragEnabled(true);
		attachmentController.getView().setTransferHandler(dndTransferHandler);

		JEditorPane editorComponent = (JEditorPane) getEditorController()
				.getComponent();
		MultipleTransferHandler compositeHandler = new MultipleTransferHandler();
		compositeHandler.addTransferHandler(editorComponent
				.getTransferHandler());
		compositeHandler.addTransferHandler(dndTransferHandler);
		editorComponent.setDragEnabled(true);
		editorComponent.setTransferHandler(compositeHandler);

		// getContainer().setContentPane(this);

		/*
		 * if (isAccountInfoPanelVisible()) {
		 * addToolBar(getIdentityInfoPanel()); }
		 */

		// *20030917, karlpeder* If ContainerListeners are waiting to be
		// added, add them now.
		if (containerListenerBuffer != null) {
			LOG.fine("Adding ContainerListeners from buffer");

			Iterator ite = containerListenerBuffer.iterator();

			while (ite.hasNext()) {
				ContainerListener cl = (ContainerListener) ite.next();
				getEditorPanel().addContainerListener(cl);
			}

			containerListenerBuffer = null; // done, the buffer has been emptied
		}

	}

	public IdentityInfoPanel getAccountInfoPanel() {

		return getIdentityInfoPanel();
	}

	/**
	 * Show attachment panel
	 * <p>
	 * Asks the ComposerModel if message contains attachments. If so, show the
	 * attachment panel. Otherwise, hide the attachment panel.
	 */
	public void showAttachmentPanel() {
		if (attachmentPanelShown == getAttachmentController().getView().count() > 0)
			return;

		// remove all components from container
		centerPanel.removeAll();

		// re-add all top components like recipient editor/subject editor
		centerPanel.add(topPanel, BorderLayout.NORTH);

		// if message contains attachments
		if (getAttachmentController().getView().count() > 0) {
			// create scrollapen
			JScrollPane attachmentScrollPane = new JScrollPane(
					getAttachmentController().getView());
			attachmentScrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			attachmentScrollPane.setBorder(BorderFactory.createEmptyBorder(1,
					1, 1, 1));
			// create splitpane containing the bodytext editor and the
			// attachment panel
			attachmentSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
					editorPanel, attachmentScrollPane);
			attachmentSplitPane.setDividerLocation(0.80);
			attachmentSplitPane.setBorder(null);

			// add splitpane to the center
			centerPanel.add(attachmentSplitPane, BorderLayout.CENTER);

			
			//ViewItem viewItem = getViewItem();

			// default value is 200 pixel
			//int pos = viewItem.getIntegerWithDefault("splitpanes","attachment", 200);
			attachmentSplitPane.setDividerLocation(200);

			attachmentPanelShown = true;
		} else {
			// no attachments
			// -> only show bodytext editor
			centerPanel.add(editorPanel, BorderLayout.CENTER);

			attachmentPanelShown = false;
		}

		// re-paint composer-view
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (getContainer() != null)
					getContainer().getFrame().validate();
			}
		});

	}

	/**
	 * @return Returns the attachmentSplitPane.
	 */
	public JSplitPane getAttachmentSplitPane() {
		return attachmentSplitPane;
	}

	/**
	 * init components
	 */
	protected void initComponents() {
		subjectLabel = new LabelWithMnemonic(MailResourceLoader.getString(
				"dialog", "composer", "subject"));
		smtpLabel = new LabelWithMnemonic(MailResourceLoader.getString(
				"dialog", "composer", "identity"));
		priorityLabel = new LabelWithMnemonic(MailResourceLoader.getString(
				"dialog", "composer", "priority"));

		editorPanel = new TextEditorPanel();
	}

	/**
	 * Layout components
	 */
	public void layoutComponents() {
		centerPanel.removeAll();

		topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

		FormLayout layout = new FormLayout(new ColumnSpec[] {
				new ColumnSpec("center:max(pref;50dlu)"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC }, new RowSpec[] {
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW) });
		layout.setRowGroups(new int[][] { { 1, 3, 5, 7, 9 } });
		layout.setColumnGroups(new int[][] { { 1 } });

		topPanel.setLayout(layout);

		CellConstraints c = new CellConstraints();

		topPanel.add(smtpLabel, c.xy(1, 1, CellConstraints.CENTER,
				CellConstraints.DEFAULT));

		topPanel.add(getAccountController().getView(), c.xy(3, 1));
		topPanel.add(priorityLabel, c.xy(5, 1));
		topPanel.add(getPriorityController().getView(), c.xy(7, 1));

		getHeaderController().getView().layoutComponents(topPanel);

		topPanel.add(subjectLabel, c.xy(1, 9, CellConstraints.CENTER,
				CellConstraints.DEFAULT));

		topPanel.add(getSubjectController().getView(), c.xywh(3, 9, 5, 1));

		if (composerModel.isHtml()) {
			// htmlToolbar.setBorder(BorderFactory.createEmptyBorder(0, 5, 5,
			// 0));
			// htmlToolbar.setBorder(UIManager.getBorder("ToolBar"));
			editorPanel.getContentPane().add(htmlToolbar, BorderLayout.NORTH);
		}

		editorPanel.getContentPane()
				.add(getEditorController().getViewUIComponent(),
						BorderLayout.CENTER);

		AccountItem item = (AccountItem) getAccountController().getView()
				.getSelectedItem();
		if (item.getIdentity().getSignature() != null)
			editorPanel.getContentPane().add(signatureView, BorderLayout.SOUTH);

		editorPanel.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				editorController.getComponent().requestFocus();
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

		});

		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		centerPanel.setLayout(new BorderLayout());

		centerPanel.add(topPanel, BorderLayout.NORTH);

		// no attachments
		// -> only show bodytext editor
		centerPanel.add(editorPanel, BorderLayout.CENTER);

		attachmentPanelShown = false;
	}

	/**
	 * Returns a reference to the panel, that holds the editor view. This is
	 * used by the ComposerController when adding a listener to that panel.
	 */
	public JPanel getEditorPanel() {
		return editorPanel.getContentPane();
	}

	/**
	 * Used to update the panel, that holds the editor viewer. This is necessary
	 * e.g. if the ComposerModel is changed to hold another message type (text /
	 * html), which the previous editor can not handle. If so a new editor
	 * controller is created, and thereby a new view.
	 */
	public void setNewEditorView() {

		// update panel
		editorPanel.getContentPane().removeAll();
		editorPanel.getContentPane()
				.add(getEditorController().getViewUIComponent(),
						BorderLayout.CENTER);

		AccountItem item = (AccountItem) getAccountController().getView()
				.getSelectedItem();
		if (item.getIdentity().getSignature() != null)
			editorPanel.getContentPane().add(signatureView, BorderLayout.SOUTH);

		editorPanel.getContentPane().validate();
	}

	public boolean isAccountInfoPanelVisible() {
		// TODO (@author fdietz): fix account info panel check

		/*
		 * return isToolbarEnabled(ACCOUNTINFOPANEL);
		 */

		return true;
	}

	/**
	 * Check if data was entered correctly.
	 * <p>
	 * This includes currently a test for an empty subject and a valid recipient
	 * (to/cc/bcc) list.
	 * 
	 * @return true, if data was entered correctly
	 */
	public boolean checkState() {
		// update ComposerModel based on user-changes in ComposerView
		updateComponents(false);

		if (!subjectController.checkState()) {
			return false;
		}

		return !headerController.checkState();
	}

	public void updateComponents(boolean b) {
		subjectController.updateComponents(b);
		editorController.updateComponents(b);
		priorityController.updateComponents(b);
		accountController.updateComponents(b);
		attachmentController.updateComponents(b);
		headerController.updateComponents(b);

		// show attachment panel if necessary
		if (b)
			showAttachmentPanel();
	}

	/**
	 * @return AccountController
	 */
	public AccountController getAccountController() {
		return accountController;
	}

	/**
	 * @return AttachmentController
	 */
	public AttachmentController getAttachmentController() {
		return attachmentController;
	}

	/**
	 * @return ComposerSpellCheck
	 */
	public ComposerSpellCheck getComposerSpellCheck() {
		return composerSpellCheck;
	}

	/**
	 * @return TextEditorController
	 */
	public AbstractEditorController getEditorController() {
		/*
		 * *20030906, karlpeder* Method signature changed to return an
		 * AbstractEditorController
		 */
		return editorController;
	}

	/**
	 * @return HeaderViewer
	 */
	public HeaderController getHeaderController() {
		return headerController;
	}

	/**
	 * @return IdentityInfoPanel
	 */
	public IdentityInfoPanel getIdentityInfoPanel() {
		return identityInfoPanel;
	}

	/**
	 * @return PriorityController
	 */
	public PriorityController getPriorityController() {
		return priorityController;
	}

	/**
	 * @return SubjectController
	 */
	public SubjectController getSubjectController() {
		return subjectController;
	}

	/**
	 * @see org.columba.core.gui.FrameController#init()
	 */
	protected void init() {

	}

	/**
	 * Returns the composer model
	 * 
	 * @return Composer model
	 */
	public ComposerModel getModel() {
		// if (composerModel == null) // *20030907, karlpeder* initialized in
		// init
		// composerModel = new ComposerModel();
		return composerModel;
	}

	/**
	 * Sets the composer model. If the message type of the new model (html /
	 * text) is different from the message type of the existing, the editor
	 * controller is changed and the view is changed accordingly. <br>
	 * Finally the components are updated according to the new model.
	 * 
	 * @param model
	 *            New composer model
	 */
	public void setComposerModel(ComposerModel model) {
		boolean wasHtml = composerModel.isHtml();
		composerModel = model;

		if (wasHtml != composerModel.isHtml()) {
			// new editor controller needed
			switchEditor(composerModel.isHtml());

			XmlElement optionsElement = MailConfig.getInstance().get(
					"composer_options").getElement("/options");
			XmlElement htmlElement = optionsElement.getElement("html");

			// create default element if not available
			if (htmlElement == null) {
				htmlElement = optionsElement.addSubElement("html");
			}

			// change configuration based on new model
			htmlElement.addAttribute("enable", Boolean.toString(composerModel
					.isHtml()));

			// notify observers - this includes this object - but here it will
			// do nothing, since the model is already setup correctly
			htmlElement.notifyObservers();
		}

		// Update all component according to the new model
		updateComponents(true);
	}

	/**
	 * Private utility for switching btw. html and text. This includes
	 * instantiating a new editor controller and refreshing the editor view
	 * accordingly. <br>
	 * Pre-condition: The caller should set the composer model before calling
	 * this method. If a message was already entered in the UI, then
	 * updateComponents should have been called to synchronize model with view
	 * before switching, else data will be lost. <br>
	 * Post-condition: The caller must call updateComponents afterwards to
	 * display model data using the new controller-view pair
	 * 
	 * @param html
	 *            True if we should switch to html, false for text
	 */
	private void switchEditor(boolean html) {
		if (composerModel.isHtml()) {
			LOG.fine("Switching to html editor");
			editorController.deleteObservers(); // clean up
			editorController = new HtmlEditorController(this);
		} else {
			LOG.fine("Switching to text editor");
			editorController.deleteObservers(); // clean up
			editorController = new TextEditorController(this);
		}

		// an update of the view is also necessary.
		setNewEditorView();
	}

	/**
	 * Register ContainerListener for the panel, that holds the editor view. By
	 * registering as listener it is possible to get information when the editor
	 * changes. <br>
	 * If the view is not yet created, the listener is stored in a buffer - add
	 * then added in createView. This is necessary to handle the timing involved
	 * in setting up the controller-view framework for the composer
	 */
	public void addContainerListenerForEditor(ContainerListener cl) {

		// add listener
		getEditorPanel().addContainerListener(cl);

	}

	/**
	 * Removes a ContainerListener from the panel, that holds the editor view
	 * (previously registered using addContainListenerForEditor)
	 */
	public void removeContainerListenerForEditor(ContainerListener cl) {
		getEditorPanel().removeContainerListener(cl);
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;

		XmlElement optionsElement = MailConfig.getInstance().get(
				"composer_options").getElement("/options");
		XmlElement charsetElement = optionsElement.getElement("charset");

		if (charset == null) {
			optionsElement.removeElement(charsetElement);
		} else {
			if (charsetElement == null) {
				charsetElement = new XmlElement("charset");
				optionsElement.addElement(charsetElement);
			}

			charsetElement.addAttribute("name", charset.name());
		}

		((ComposerModel) getModel()).setCharset(charset);
		fireCharsetChanged(new CharsetEvent(this, charset));
	}

	public void addCharsetListener(CharsetListener l) {
		listenerList.add(CharsetListener.class, l);
	}

	public void removeCharsetListener(CharsetListener l) {
		listenerList.remove(CharsetListener.class, l);
	}

	protected void fireCharsetChanged(CharsetEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CharsetListener.class) {
				((CharsetListener) listeners[i + 1]).charsetChanged(e);
			}
		}
	}

	/**
	 * Used for listenen to the enable html option
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void toggleHtmlMode() {
        XmlElement optionsElement = MailConfig.getInstance().get("composer_options")
        .getElement("/options");
        
        XmlElement htmlElement = optionsElement.getElement("html");

        // switch btw. html and text if necessary
			String enableHtml = htmlElement.getAttribute("enable", "false");
			boolean html = Boolean.valueOf(enableHtml).booleanValue();
			boolean wasHtml = composerModel.isHtml();

			if (html != wasHtml) {
				composerModel.setHtml(html);

				// sync model with the current (old) view
				updateComponents(false);

				// convert body text to comply with new editor format
				String oldBody = composerModel.getBodyText();
				String newBody;

				if (html) {
					LOG.fine("Converting body text to html");
					newBody = HtmlParser.textToHtml(oldBody, "", null);
				} else {
					LOG.fine("Converting body text to text");
					newBody = HtmlParser.htmlToText(oldBody);
				}

				composerModel.setBodyText(newBody);

				// switch editor and resync view with model
				switchEditor(composerModel.isHtml());

				updateComponents(true);
			}

			if (html) {
				editorPanel.getContentPane().add(htmlToolbar,
						BorderLayout.NORTH);
			} else {
				editorPanel.getContentPane().remove(htmlToolbar);
			}

			editorPanel.validate();
		
	}

//	public void savePositions(ViewItem viewItem) {
//		super.savePositions(viewItem);
//
//		viewItem = getViewItem();
//
//		// splitpanes
//		if (attachmentSplitPane != null)
//			viewItem.setInteger("splitpanes", "attachment", attachmentSplitPane
//					.getDividerLocation());
//
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.core.gui.frame.AbstractFrameView#showToolbar()
	 */
	public void showToolbar() {

		/*
		 * boolean b = isToolbarVisible();
		 * 
		 * if (getToolBar() == null) { return; }
		 * 
		 * if (b) { toolbarPane.remove(toolbar); ((IFrameMediator)
		 * frameController) .enableToolbar(MAIN_TOOLBAR, false); } else { if
		 * (isAccountInfoPanelVisible()) { toolbarPane.removeAll();
		 * toolbarPane.add(toolbar); toolbarPane.add(getAccountInfoPanel()); }
		 * else { toolbarPane.add(toolbar); }
		 * 
		 * ((IFrameMediator) frameController).enableToolbar(MAIN_TOOLBAR, true); }
		 * 
		 * validate(); repaint();
		 */
	}

	public void showAccountInfoPanel() {

		/*
		 * boolean b = isAccountInfoPanelVisible();
		 * 
		 * if (b) { toolbarPane.remove(getAccountInfoPanel()); ((IFrameMediator)
		 * frameController).enableToolbar(ACCOUNTINFOPANEL, false); } else {
		 * toolbarPane.add(getAccountInfoPanel());
		 * 
		 * ((IFrameMediator) frameController).enableToolbar(ACCOUNTINFOPANEL,
		 * true); }
		 * 
		 * validate(); repaint();
		 */
	}

	/**
	 * @see org.columba.api.gui.frame.IFrameMediator#close()
	 */
	public void close() {

		// don't prompt user if composer should be closed
		if (isPromptOnDialogClosing() == false)
			return;

		// only prompt user, if composer contains some text
		if (editorController.getViewText().length() == 0) {
			getContainer().getFrame().setVisible(false);

			// close Columba, if composer is only visible frame
			FrameManager.getInstance().close(null);

			return;
		}

		Object[] options = { "Close", "Cancel", "Save" };
		int n = JOptionPane.showOptionDialog(getContainer().getFrame(),
				"Message wasn't sent. Would you like to save your changes?",
				"Warning: Message was modified",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[2]); // default button title

		if (n == 2) {
			// save changes
			new SaveAsDraftAction(ComposerController.this)
					.actionPerformed(null);

			// close composer
			getContainer().getFrame().setVisible(false);

			// close Columba, if composer is only visible frame
			FrameManager.getInstance().close(null);
		} else if (n == 1) {
			// cancel question dialog and don't close composer
		} else {
			// close composer
			getContainer().getFrame().setVisible(false);

			// close Columba, if composer is only visible frame
			FrameManager.getInstance().close(null);
		}

	}

	public class ComposerFocusTraversalPolicy extends FocusTraversalPolicy {

		public Component getComponentAfter(Container focusCycleRoot,
				Component aComponent) {
			if (aComponent.equals(accountController.getView()))
				return priorityController.getView();
			else if (aComponent.equals(priorityController.getView()))
				return headerController.getView().getToComboBox();
			else if (aComponent.equals(headerController.getView()
					.getToComboBox()))
				return headerController.getView().getCcComboBox();
			else if (aComponent.equals(headerController.getView()
					.getCcComboBox()))
				return headerController.getView().getBccComboBox();
			else if (aComponent.equals(headerController.getView()
					.getBccComboBox()))
				return subjectController.getView();
			else if (aComponent.equals(subjectController.getView()))
				return editorController.getComponent();

			return headerController.getView().getToComboBox();
		}

		public Component getComponentBefore(Container focusCycleRoot,
				Component aComponent) {
			if (aComponent.equals(editorController.getComponent()))
				return subjectController.getView();
			else if (aComponent.equals(subjectController.getView()))
				return headerController.getView().getBccComboBox();
			else if (aComponent.equals(headerController.getView()
					.getBccComboBox()))
				return headerController.getView().getCcComboBox();
			else if (aComponent.equals(headerController.getView()
					.getCcComboBox()))
				return headerController.getView().getToComboBox();
			else if (aComponent.equals(headerController.getView()
					.getToComboBox()))
				return priorityController.getView();
			else if (aComponent.equals(priorityController.getView()))
				return accountController.getView();

			return editorController.getComponent();
		}

		public Component getDefaultComponent(Container focusCycleRoot) {
			return headerController.getView().getToComboBox();
		}

		public Component getLastComponent(Container focusCycleRoot) {
			return editorController.getComponent();
		}

		public Component getFirstComponent(Container focusCycleRoot) {
			return accountController.getView();
		}
	}

	/**
	 * @see org.columba.api.gui.frame.IContentPane#getComponent()
	 */
	public JComponent getComponent() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.add(centerPanel, BorderLayout.CENTER);

		try {
			InputStream is = DiskIO
					.getResourceStream("org/columba/mail/action/composer_menu.xml");
			getContainer().extendMenu(this, is);

			File configDirectory = MailConfig.getInstance()
					.getConfigDirectory();
			InputStream is2 = new FileInputStream(new File(configDirectory,
					"composer_toolbar.xml"));
			getContainer().extendToolbar(this, is2);

		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}

		// @author: fdietz
		// disabled identity infopanel because it contains
		// only duplicate information
		// getContainer().setInfoPanel(getIdentityInfoPanel());

		getContainer().getFrame().setFocusTraversalPolicy(
				new ComposerFocusTraversalPolicy());

		// make sure that JFrame is not closed automatically
		// -> we want to prompt the user to save his work
		getContainer().setCloseOperation(false);

		return panel;
	}

	/**
	 * @see org.columba.api.gui.frame.IFrameMediator#getString(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public String getString(String sPath, String sName, String sID) {
		return MailResourceLoader.getString(sPath, sName, sID);
	}

	/**
	 * @see org.columba.api.gui.frame.IFrameMediator#getContentPane()
	 */
	public IContentPane getContentPane() {
		return this;
	}

	/**
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent arg0) {
		Document doc = arg0.getDocument();
		try {
			String subject = doc.getText(0, doc.getLength());

			getContainer().getFrame().setTitle(subject);
		} catch (BadLocationException e) {
		}
	}

	/**
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent arg0) {
		Document doc = arg0.getDocument();
		try {
			String subject = doc.getText(0, doc.getLength());

			getContainer().getFrame().setTitle(subject);
		} catch (BadLocationException e) {
		}
	}

	/**
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent arg0) {
		Document doc = arg0.getDocument();
		try {
			String subject = doc.getText(0, doc.getLength());

			getContainer().getFrame().setTitle(subject);
		} catch (BadLocationException e) {
		}
	}

	/**
	 * @return Returns the promptOnDialogClosing.
	 */
	public boolean isPromptOnDialogClosing() {
		return promptOnDialogClosing;
	}

	/**
	 * @param promptOnDialogClosing
	 *            The promptOnDialogClosing to set.
	 */
	public void setPromptOnDialogClosing(boolean promptOnDialogClosing) {
		this.promptOnDialogClosing = promptOnDialogClosing;
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {

			AccountItem item = (AccountItem) getAccountController().getView()
					.getSelectedItem();
			if (item.getIdentity().getSignature() != null) {
				// show signature viewer
				editorPanel.getContentPane().add(signatureView,
						BorderLayout.SOUTH);
				editorPanel.revalidate();
			} else {
				// hide signature viewer
				editorPanel.getContentPane().remove(signatureView);
				editorPanel.revalidate();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5115.java