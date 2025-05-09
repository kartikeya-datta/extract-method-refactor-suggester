error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5294.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5294.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,65]

error in qdox parser
file content:
```java
offset: 65
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5294.java
text:
```scala
"default, 3dlu, fill:default:grow, 3dlu, default, 3dlu, default",@@

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
package org.columba.mail.gui.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.columba.core.command.CommandProcessor;
import org.columba.core.filter.FilterCriteria;
import org.columba.core.folder.IFolder;
import org.columba.core.gui.selection.ISelectionListener;
import org.columba.core.gui.selection.SelectionChangedEvent;
import org.columba.core.gui.util.ButtonWithMnemonic;
import org.columba.core.gui.util.CTextField;
import org.columba.core.gui.util.ComboMenu;
import org.columba.core.gui.util.ImageLoader;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.filter.MailFilterFactory;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.gui.config.search.SearchFrame;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.table.command.ViewHeaderListCommand;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.util.MailResourceLoader;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Filter toolbar lets you do quick searches on folder contents.
 * 
 * @author fdietz
 */
public class FilterToolbar extends JPanel implements ActionListener,
		ItemListener, ISelectionListener {

	public JButton clearButton;

	private JButton searchButton;

	private ComboMenu criteriaComboMenu;

	private JLabel label;

	private JTextField textField;

	private TableController tableController;

	private IFolder sourceFolder;
	
	private String selectedItem;

	private boolean active;
	
	String[] strs = new String[] { "subject_contains", "from_contains",
			"to_contains", "cc_contains", "bcc_contains", "body_contains",
			"separator", "unread_messages", "flagged_messages",
			"high_priority", "spam_message", "separator", "custom_search" };

	private IFolder selectedFolder;

	public FilterToolbar(TableController headerTableViewer) {
		super();
		this.tableController = headerTableViewer;

		selectedItem = strs[0];

		initComponents();
		layoutComponents();

		
		headerTableViewer.getFrameController().getSelectionManager().registerSelectionListener("mail.tree", this);

		//textField.getDocument().addDocumentListener(new
		// MyDocumentListener());
	}

	private ComboMenu createComboMenu() {
		ComboMenu c = new ComboMenu();
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].equals("separator"))
				c.addSeparator();
			else {
				JRadioButtonMenuItem m = c.addMenuItem(strs[i],
						MailResourceLoader.getString("filter", "filter",
								strs[i]));

				switch (i) {
				case 7:
					m.setIcon(ImageLoader.getSmallImageIcon("mail-new.png"));
					break;
				case 8:
					m.setIcon(ImageLoader
							.getSmallImageIcon("mark-as-important-16.png"));
					break;
				case 9:
					m.setIcon(ImageLoader
							.getSmallImageIcon("priority-high.png"));
					break;
				case 10:
					m.setIcon(ImageLoader.getSmallImageIcon("spam-16.png"));
					break;
				}
			}
		}

		return c;
	}

	public void initComponents() {

		criteriaComboMenu = createComboMenu();
		criteriaComboMenu.addItemListener(this);

		textField = new CTextField();

		textField.addActionListener(this);
		textField.setActionCommand("TEXTFIELD");
		textField.addKeyListener(new MyKeyListener());

		clearButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"menu", "mainframe", "filtertoolbar_clear"));
		clearButton.setToolTipText(MailResourceLoader.getString("menu",
				"mainframe", "filtertoolbar_clear_tooltip"));
		clearButton.setActionCommand("CLEAR");
		clearButton.addActionListener(this);
		clearButton.setEnabled(false);
		
		searchButton = new JButton("Search");
		searchButton.setActionCommand("SEARCH");
		searchButton.addActionListener(this);
		searchButton.setDefaultCapable(true);
		searchButton.setEnabled(false);
	}

	public void layoutComponents() {
		setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));

		FormLayout l = new FormLayout(
				"default, 1dlu, fill:default:grow, 3dlu, default, 3dlu, default",
				"fill:default:grow");
		PanelBuilder b = new PanelBuilder(this, l);

		CellConstraints c = new CellConstraints();

		b.add(criteriaComboMenu, c.xy(1, 1));

		b.add(textField, c.xy(3, 1));

		b.add(searchButton, c.xy(5, 1));

		b.add(clearButton, c.xy(7, 1));

	}

	private void update() throws Exception {

	}

	private int getIndex(String name) {
		for (int i = 0; i < strs.length; i++) {
			if (name.equals(strs[i]))
				return i;
		}

		return -1;
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("SEARCH")) {
			executeSearch();

		} else if (action.equals("CLEAR")) {

			resetToolbar();

			// select search folder
			MailFolderCommandReference r = new MailFolderCommandReference(
					sourceFolder);
			((MailFrameMediator) tableController.getFrameController())
					.setTreeSelection(r);

			((MailFrameMediator) tableController.getFrameController())
					.setTableSelection(r);
			CommandProcessor.getInstance().addOp(
					new ViewHeaderListCommand(tableController
							.getFrameController(), r));
		}

	}

	/**
	 * 
	 */
	private void resetToolbar() {
		active = false;
		clearButton.setEnabled(false);

		textField.setText("");
	}

	/**
	 * Execute search.
	 */
	private void executeSearch() {
		clearButton.setEnabled(true);		
		active = true;
		
		// get selected search criteria
		int index = getIndex(selectedItem);

		// create filter criteria based on selected type
		FilterCriteria c = createFilterCriteria(index);


		if (selectedFolder.getUid() != 106)
			sourceFolder = selectedFolder;

		// set criteria for search folder
		VirtualFolder searchFolder = prepareSearchFolder(c, sourceFolder);

		try {
			// add search to history
			searchFolder.addSearchToHistory();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// select search folder
		/*
		MailFolderCommandReference r = new MailFolderCommandReference(null);
		((MailFrameMediator) tableController.getFrameController())
				.setTreeSelection(r);
			*/
		
		MailFolderCommandReference r = new MailFolderCommandReference(searchFolder);
		((MailFrameMediator) tableController.getFrameController())
				.setTreeSelection(r);
		((MailFrameMediator) tableController.getFrameController())
		.setTableSelection(r);


		CommandProcessor.getInstance().addOp(
				new ViewHeaderListCommand(tableController.getFrameController(),
						r));
	}

	/**
	 * Create new virtual folder with filter criteria settings and selected it.
	 * 
	 * @param c
	 *            filter criteria settings
	 * @return source folder
	 */
	private VirtualFolder prepareSearchFolder(FilterCriteria c, IFolder folder) {
		// get search folder
		VirtualFolder searchFolder = (VirtualFolder) FolderTreeModel
				.getInstance().getFolder(106);

		// remove old filters
		searchFolder.getFilter().getFilterRule().removeAll();

		// add filter criteria
		searchFolder.getFilter().getFilterRule().add(c);

		// don't search in subfolders recursively
		searchFolder.getConfiguration().setString("property", "include_subfolders",
				"false");

		int uid = folder.getUid();

		// set source folder UID
		searchFolder.getConfiguration().setInteger("property", "source_uid", uid);

		searchFolder.deactivate();
		
		return searchFolder;
	}

	/**
	 * Create filter criteria, based on current selection.
	 * 
	 * @param index
	 *            selected criteria
	 * @return newly created filter criteria
	 */
	private FilterCriteria createFilterCriteria(int index) {
		String pattern = textField.getText();
		FilterCriteria c = null;
		switch (index) {

		case 0:
			c = MailFilterFactory.createSubjectContains(pattern);
			break;
		case 1:
			c = MailFilterFactory.createFromContains(pattern);
			break;
		case 2:
			c = MailFilterFactory.createToContains(pattern);
			break;
		case 3:
			c = MailFilterFactory.createCcContains(pattern);
			break;
		case 4:
			c = MailFilterFactory.createBccContains(pattern);
			break;
		case 5:
			c = MailFilterFactory.createBodyContains(pattern);
			break;
		case 7:
			c = MailFilterFactory.createUnreadMessages();
			break;
		case 8:
			c = MailFilterFactory.createFlaggedMessages();
			break;
		case 9:
			c = MailFilterFactory.createHighPriority();
			break;
		case 10:
			c = MailFilterFactory.createSpamMessages();
			break;

		}

		return c;
	}

	public void setPattern(String pattern) {
		textField.setText(pattern);
	}

	/**
	 * Execute search when pressing RETURN in the textfield.
	 * 
	 * @author fdietz
	 */
	class MyKeyListener implements KeyListener {
		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			char ch = e.getKeyChar();

			if (ch == KeyEvent.VK_ENTER) {
				executeSearch();
			}
		}
	}

	/**
	 * Execute search while user is typing pattern in textfield.
	 * 
	 * @author fdietz
	 */
	class MyDocumentListener implements DocumentListener {

		public void insertUpdate(DocumentEvent e) {
			update();
		}

		public void removeUpdate(DocumentEvent e) {
			update();
		}

		public void changedUpdate(DocumentEvent e) {
			//Plain text components don't fire these events
		}

		public void update() {
			if (sourceFolder == null)
				return;

			/*
			 * // get selected search criteria int index =
			 * criteriaComboBox.getSelectedIndex(); // create filter criteria
			 * based on selected type FilterCriteria c =
			 * createFilterCriteria(index); // set criteria for search folder
			 * VirtualFolder searchFolder = prepareSearchFolder(c,
			 * sourceFolder); // select search folder MailFolderCommandReference
			 * r = new MailFolderCommandReference( searchFolder);
			 * ((MailFrameMediator) tableController.getFrameController())
			 * .setTreeSelection(r);
			 */
		}
	}

	/**
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent event) {
		selectedItem = (String) event.getItem();

		// execute custom search
		if (selectedItem.equals("custom_search")) {
			executeCustomSearch();
			criteriaComboMenu.setSelectedItem(0);
			return;
		}

		// enable/disable textfield in-dependency of selected criteria
		int selectedIndex = getIndex(selectedItem);
		if (selectedIndex >= 0 && selectedIndex <= 5) {
			textField.setEnabled(true);
			textField.requestFocus();
		} else {
			textField.setEnabled(false);

			// directly execute search
			executeSearch();
		}

	}

	/**
	 * Open the search dialog, with pre-filled settings.
	 *  
	 */
	private void executeCustomSearch() {
		AbstractMessageFolder searchFolder = (AbstractMessageFolder) FolderTreeModel
				.getInstance().getFolder(106);

		AbstractMessageFolder folder = (AbstractMessageFolder) ((MailFrameMediator) tableController
				.getFrameController()).getTableSelection().getSourceFolder();

		if (folder == null) {
			return;
		}

		SearchFrame frame = new SearchFrame(tableController
				.getFrameController(), searchFolder, folder);
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.selection.ISelectionListener#selectionChanged(org.columba.core.gui.selection.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent e) {
		// get currently selected folder
		IMailFolderCommandReference ref = ((MailFrameMediator) tableController.getFrameController())
				.getTreeSelection();
		
		if( ref != null ) {
			selectedFolder = ref.getSourceFolder();
			searchButton.setEnabled(true);
		} else {
			searchButton.setEnabled(false);
		}
		
		if( active ) {
			resetToolbar();
			active = false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5294.java