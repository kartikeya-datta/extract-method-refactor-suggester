error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7053.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7053.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7053.java
text:
```scala
(@@(ComposerModel)getModel()).setCharsetName(e.getValue());

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
package org.columba.mail.gui.composer;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;
import java.util.Vector;

import org.columba.addressbook.folder.Folder;
import org.columba.addressbook.folder.HeaderItem;
import org.columba.addressbook.folder.HeaderItemList;
import org.columba.addressbook.parser.AddressParser;
import org.columba.addressbook.parser.ListParser;
import org.columba.core.config.ViewItem;
import org.columba.core.gui.frame.AbstractFrameController;
import org.columba.core.gui.frame.AbstractFrameView;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.core.util.CharsetEvent;
import org.columba.core.util.CharsetListener;
import org.columba.core.util.CharsetManager;
import org.columba.mail.composer.MessageComposer;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.composer.util.IdentityInfoPanel;
import org.columba.mail.message.Message;
import org.columba.mail.util.AddressCollector;

/**
 * @author frd
 *
 * controller for message composer dialog
 */
public class ComposerController
	extends AbstractFrameController
	implements CharsetListener, ComponentListener, WindowListener {

	private IdentityInfoPanel identityInfoPanel;
	private AttachmentController attachmentController;
	private SubjectController subjectController;
	private PriorityController priorityController;
	private AccountController accountController;
	private EditorController editorController;
	private HeaderController headerController;
	//private MessageComposer messageComposer;
	private CharsetManager charsetManager;
	private ComposerSpellCheck composerSpellCheck;

	private ComposerModel composerModel;

	/*
	Message message;
	AccountItem accountItem;
	String bodytext;
	String charsetName;
	
	Vector attachments;
	
	Vector toList;
	Vector ccList;
	Vector bccList;
	
	boolean signMessage;
	boolean encryptMessage;
	*/

	public ComposerController() {
		super("Composer", new ViewItem(MailConfig.get("composer_options").getElement("/options/gui/view")));

		getView().addWindowListener(this);

		getView().loadWindowPosition();
		
		getView().setVisible(true);
	}

	public ComposerController(
		Message message) {
		this();

		
		composerModel.setMessage(message);

		//getView().addWindowListener(this);
		//this.message = message;
		//composerInterface.viewItem = MailConfig.getComposerOptionsConfig().getViewItem();
		//composerModel = new ComposerModel();
	}
	

	public void charsetChanged(CharsetEvent e) {
		//((ComposerModel)getModel()).setCharsetName(e.getValue());
	}

	public boolean checkState() {
		// update ComposerModel based on user-changes in ComposerView
		updateComponents(false);

		boolean b = subjectController.checkState();
		if (b == false)
			return false;

		b = headerController.checkState();
		if (b == false)
			return false;

		return true;
	}
	/*
	public void saveWindowPosition() {
	
		java.awt.Dimension d = view.getSize();
	
		WindowItem windowItem = composerInterface.viewItem.getWindowItem();
		
		windowItem.set("x", 0);
		windowItem.set("y", 0);
		windowItem.set("width", d.width);
		windowItem.set("height", d.height);
	
		composerInterface.viewItem.set("splitpanes","main",
			view.getMainDividerLocation());
		composerInterface.viewItem.set("splitpanes","header",
			view.getRightDividerLocation());
	
	}
	
	public void loadWindowPosition() {
		WindowItem windowItem = composerInterface.viewItem.getWindowItem();
		
		java.awt.Point point = windowItem.getPoint();
		java.awt.Dimension dim = windowItem.getDimension();
	
		view.setSize(dim);
	
		view.setMainDividerLocation(
			composerInterface.viewItem.getInteger("splitpanes","main"));
		view.setRightDividerLocation(
		composerInterface.viewItem.getInteger("splitpanes","header"));
	}
	
	protected void registerWindowListener() {
		view.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				hideComposerWindow();
			}
		});
	
	}
	
	public ComposerModel getModel() {
		return model;
	}
	*/

	public void updateComponents(boolean b) {
		subjectController.updateComponents(b);

		editorController.updateComponents(b);
		priorityController.updateComponents(b);
		accountController.updateComponents(b);
		attachmentController.updateComponents(b);

		headerController.updateComponents(b);

		headerController.view.getTable().initFocus(subjectController.view);
		initAddressCompletion();
		headerController.appendRow();
	}

	/*
	
	public void showComposerWindow() {
	
		updateComponents(true);
	
		composerInterface.editorController.installListener();
		composerInterface.subjectController.installListener();
		composerInterface.priorityController.installListener();
		//composerInterface.accountController.installListener();
		composerInterface.attachmentController.installListener();
	
		//composerInterface.headerController.installListener();
	
		composerInterface.headerController.view.getTable().initFocus(
			composerInterface.subjectController.view);
			
		if (composerInterface.viewItem.getBoolean("addressbook","enabled") == true )
			showAddressbookWindow();
	
		view.setVisible(true);
	
		
		initAddressCompletion();
		
		
		composerInterface.headerController.appendRow();
	}
	*/

	protected void initAddressCompletion() {
		AddressCollector.clear();

		HeaderItemList list =
			((Folder) MainInterface.addressbookTreeModel.getFolder(101))
				.getHeaderItemList();

		for (int i = 0; i < list.count(); i++) {
			HeaderItem item = list.get(i);

			if (item.contains("displayname"))
				AddressCollector.addAddress((String) item.get("displayname"), item); //$NON-NLS-1$ //$NON-NLS-2$
			if (item.contains("email;internet"))
				AddressCollector.addAddress((String) item.get("email;internet"), item); //$NON-NLS-1$ //$NON-NLS-2$
		}

		list =
			((Folder) MainInterface.addressbookTreeModel.getFolder(102))
				.getHeaderItemList();

		for (int i = 0; i < list.count(); i++) {
			HeaderItem item = list.get(i);

			if (item.contains("displayname"))
				AddressCollector.addAddress((String) item.get("displayname"), item); //$NON-NLS-1$ //$NON-NLS-2$
			if (item.contains("email;internet"))
				AddressCollector.addAddress((String) item.get("email;internet"), item); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	public void hideComposerWindow() {
	
		saveWindowPosition();
		
		if (composerInterface.viewItem.getBoolean("addressbook","enabled") == true)
			hideAddressbookWindow();
	
		view.setVisible(false);
	}
	
	public void showAddressbookWindow() {
		updateAddressbookFrame();
	
		composerInterface.addressbookFrame.setVisible(true);
	}
	
	public void hideAddressbookWindow() {
		composerInterface.addressbookFrame.setVisible(false);
	}
	*/

	public Vector getRCPTVector() {
		Vector output = new Vector();
		Enumeration aktEnum;
		Object aktAdress;

		Vector v =
			ListParser.parseVector(((ComposerModel) getModel()).getToList());
		output.addAll(AddressParser.normalizeRCPTVector(v));
		v = ListParser.parseVector(((ComposerModel) getModel()).getCcList());
		output.addAll(AddressParser.normalizeRCPTVector(v));
		v = ListParser.parseVector(((ComposerModel) getModel()).getBccList());
		output.addAll(AddressParser.normalizeRCPTVector(v));

		return output;
	}

	/*
	protected void updateAddressbookFrame() {
	
		if ((view.getLocation().x
			- composerInterface.addressbookFrame.getSize().width
			< 0)
 (view.getLocation().y < 0)) {
			int x =
				view.getLocation().x
					- composerInterface.addressbookFrame.getSize().width;
			int y = view.getLocation().y;
	
			if (x <= 0)
				x = 0;
			if (y <= 0)
				y = 0;
	
			view.setLocation(
				x + composerInterface.addressbookFrame.getSize().width,
				y);
	
		}
	
		composerInterface.addressbookFrame.setLocation(
			view.getLocation().x
				- composerInterface.addressbookFrame.getSize().width,
			view.getLocation().y);
	
	}*/

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {

		/*		
		if (composerInterface.addressbookFrame.isVisible()) {
			updateAddressbookFrame();
		}*/

	}

	public void componentResized(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		close();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.FrameController#createView()
	 */
	protected AbstractFrameView createView() {
		ComposerView view = new ComposerView(this);

		view.init();
		
		return view;
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.FrameController#initInternActions()
	 */
	protected void initInternActions() {

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
	 * @return CharsetManager
	 */
	public CharsetManager getCharsetManager() {
		return charsetManager;
	}

	/**
	 * @return ComposerSpellCheck
	 */
	public ComposerSpellCheck getComposerSpellCheck() {
		return composerSpellCheck;
	}

	/**
	 * @return EditorController
	 */
	public EditorController getEditorController() {
		return editorController;
	}

	/**
	 * @return HeaderController
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
	 * @return MessageComposer
	 */
	public MessageComposer getMessageComposer() {
		return new MessageComposer(this);
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

	/* (non-Javadoc)
	 * @see org.columba.core.gui.FrameController#init()
	 */
	protected void init() {
		identityInfoPanel = new IdentityInfoPanel();

		attachmentController = new AttachmentController(this);

		headerController = new HeaderController(this);

		subjectController = new SubjectController(this);

		priorityController = new PriorityController(this);

		accountController = new AccountController(this);

		editorController = new EditorController(this);

		//messageComposer = new MessageComposer(this);

		//composerInterface.composerFolder = new TempFolder();

		charsetManager = new CharsetManager();
		charsetManager.addCharsetListener(this);

		composerSpellCheck = new ComposerSpellCheck(this);

		/*
		composerInterface.addressbookFrame =
			AddressBookIC.createAddressbookListFrame(composerInterface);
		
		composerInterface.addressbookFrame.addComponentListener(this);
			*/

		/*
		getView().addComponentListener(this);
		
		//view.setVisible(true);	
		
		registerWindowListener();
		
		int count = MailConfig.getAccountList().count();
		if ( count != 0 ) loadWindowPosition();*/

	}

	/**
	 * @return
	 */
	public ComposerModel getModel() {
		if ( composerModel == null ) composerModel = new ComposerModel();
		
		return composerModel;
	}

	/**
	 * @param model
	 */
	public void setComposerModel(ComposerModel model) {
		composerModel = model;
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.frame.AbstractFrameController#close()
	 */
	public void close() {
	
		
		ColumbaLogger.log.info("closing ComposerController");

		view.saveWindowPosition();

		view.setVisible(false);
	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7053.java