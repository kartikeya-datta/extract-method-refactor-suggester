error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8871.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8871.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8871.java
text:
```scala
F@@ilterDialog dialog = new FilterDialog( parent);

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.mail.gui.config.filter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.columba.core.config.Config;
import org.columba.core.gui.button.CloseButton;
import org.columba.core.gui.button.HelpButton;
import org.columba.core.main.MainInterface;
import org.columba.mail.filter.Filter;
import org.columba.mail.filter.FilterList;
import org.columba.mail.folder.Folder;
import org.columba.mail.util.MailResourceLoader;

public class ConfigFrame extends JDialog implements ListSelectionListener, ActionListener {
	

	/*
	private JTextField textField;
	private JPanel leftPanel;
	private JTabbedPane rightPanel;
	private JButton addButton;
	private JButton removeButton;
	private JButton editButton;
	private JButton upButton;
	private JButton downButton;
	*/

	//private JButton closeButton;
	private JFrame frame;

	public FilterListTable listView;

	private Config config;

	//private AdapterNode actNode;

	private boolean newAccount = false;

	private int index = -1;

	private FilterList filterList;
	private Filter filter;
	//private JDialog dialog;

	JPanel centerPanel = new JPanel();
	JPanel eastPanel = new JPanel();
	JPanel jPanel1 = new JPanel();

	JTextField nameTextField = new JTextField();
	JLabel nameLabel = new JLabel();

	CloseButton closeButton = new CloseButton();
	HelpButton helpButton = new HelpButton();

	JButton addButton = new JButton();
	JButton removeButton = new JButton();
	JButton editButton = new JButton();
	JButton enableButton = new JButton();
	JButton disableButton = new JButton();
	JButton moveupButton = new JButton();
	JButton movedownButton = new JButton();

	BorderLayout borderLayout3 = new BorderLayout();
	GridLayout gridLayout1 = new GridLayout();
	Folder folder;
	
	public ConfigFrame(Folder folder ) {
		
		super();
		this.folder = folder;
		
		setTitle(
			MailResourceLoader.getString("dialog", "filter", "dialog_title"));
		this.filterList = folder.getFilterList();

		config = MainInterface.config;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		initComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public Filter getSelected() {
		return filter;
	}

	public void setSelected(Filter f) {
		filter = f;
	}

	public void initComponents() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
		getContentPane().add(mainPanel);

		addButton.setText(
			MailResourceLoader.getString("dialog", "filter", "add_filter"));
		addButton.setMnemonic(
			MailResourceLoader.getMnemonic("dialog", "filter", "add_filter"));
		addButton.setActionCommand("ADD");
		addButton.setEnabled(true);
		addButton.addActionListener(this);

		removeButton.setText(
			MailResourceLoader.getString("dialog", "filter", "remove_filter"));
		removeButton.setMnemonic(
			MailResourceLoader.getMnemonic(
				"dialog",
				"filter",
				"remove_filter"));
		removeButton.setActionCommand("REMOVE");
		removeButton.setEnabled(false);
		removeButton.addActionListener(this);

		editButton.setText(
			MailResourceLoader.getString("dialog", "filter", "edit_filter"));
		editButton.setMnemonic(
			MailResourceLoader.getMnemonic("dialog", "filter", "edit_filter"));
		editButton.setActionCommand("EDIT");
		editButton.setEnabled(false);
		editButton.addActionListener(this);

		/*
		enableButton.setText("Enable");
		enableButton.setActionCommand("ENABLE");
		enableButton.addActionListener( this );
		
		disableButton.setText("Disable");
		disableButton.setActionCommand("DISABLE");
		disableButton.addActionListener( this );
		*/

		moveupButton.setText(
			MailResourceLoader.getString("dialog", "filter", "moveup"));
		moveupButton.setMnemonic(
			MailResourceLoader.getMnemonic("dialog", "filter", "moveup"));
		moveupButton.setActionCommand("MOVEUP");
		moveupButton.setEnabled(false);
		moveupButton.addActionListener(this);

		movedownButton.setText(
			MailResourceLoader.getString("dialog", "filter", "movedown"));
		movedownButton.setMnemonic(
			MailResourceLoader.getMnemonic("dialog", "filter", "movedown"));
		movedownButton.setActionCommand("MOVEDOWN");
		movedownButton.setEnabled(false);
		movedownButton.addActionListener(this);

		// top panel

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		//topPanel.setLayout( );

		JPanel topBorderPanel = new JPanel();
		topBorderPanel.setLayout(new BorderLayout());
		//topBorderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		topBorderPanel.add(topPanel, BorderLayout.CENTER);
		//mainPanel.add( topBorderPanel, BorderLayout.NORTH );

		nameLabel.setText("name");
		nameLabel.setEnabled(false);
		topPanel.add(nameLabel);

		topPanel.add(Box.createRigidArea(new java.awt.Dimension(10, 0)));
		topPanel.add(Box.createHorizontalGlue());

		nameTextField.setText("name");
		nameTextField.setEnabled(false);
		topPanel.add(nameTextField);

		Component glue = Box.createVerticalGlue();
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = GridBagConstraints.REMAINDER;
		//c.fill = GridBagConstraints.HORIZONTAL;
		gridBagLayout.setConstraints(glue, c);

		gridBagLayout = new GridBagLayout();
		c = new GridBagConstraints();
		eastPanel.setLayout(gridBagLayout);
		mainPanel.add(eastPanel, BorderLayout.EAST);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(addButton, c);
		eastPanel.add(addButton);

		Component strut1 = Box.createRigidArea(new Dimension(30, 5));
		gridBagLayout.setConstraints(strut1, c);
		eastPanel.add(strut1);

		gridBagLayout.setConstraints(removeButton, c);
		eastPanel.add(removeButton);

		Component strut = Box.createRigidArea(new Dimension(30, 5));
		gridBagLayout.setConstraints(strut, c);
		eastPanel.add(strut);

		gridBagLayout.setConstraints(editButton, c);
		eastPanel.add(editButton);

		strut = Box.createRigidArea(new Dimension(30, 20));
		gridBagLayout.setConstraints(strut, c);
		eastPanel.add(strut);

		/*
		gridBagLayout.setConstraints( enableButton, c );
		eastPanel.add( enableButton );
		
		strut = Box.createRigidArea( new Dimension(30,10) );
		gridBagLayout.setConstraints( strut, c );
		eastPanel.add( strut );
		
		gridBagLayout.setConstraints( disableButton, c );
		eastPanel.add( disableButton );
		
		strut = Box.createRigidArea( new Dimension(30,20) );
		gridBagLayout.setConstraints( strut, c );
		eastPanel.add( strut );
		*/

		gridBagLayout.setConstraints(moveupButton, c);
		eastPanel.add(moveupButton);

		strut = Box.createRigidArea(new Dimension(30, 5));
		gridBagLayout.setConstraints(strut, c);
		eastPanel.add(strut);

		gridBagLayout.setConstraints(movedownButton, c);
		eastPanel.add(movedownButton);

		glue = Box.createVerticalGlue();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		gridBagLayout.setConstraints(glue, c);
		eastPanel.add(glue);

		/*
		c.gridheight = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0;
		gridBagLayout.setConstraints( closeButton, c );
		eastPanel.add( closeButton );
		*/

		// centerpanel

		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		listView = new FilterListTable(filterList, this);
		listView.getSelectionModel().addListSelectionListener(this);
		JScrollPane scrollPane = new JScrollPane(listView);
		scrollPane.setPreferredSize(new Dimension(300, 250));
		scrollPane.getViewport().setBackground(Color.white);
		centerPanel.add(scrollPane);

		mainPanel.add(centerPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(BorderFactory.createCompoundBorder(new WizardTopBorder(),BorderFactory.createEmptyBorder(17,12,11,11)));
		JPanel buttonPanel = new JPanel(new GridLayout(1,2,5,0));
		closeButton = new CloseButton();
		closeButton.setActionCommand("CLOSE"); //$NON-NLS-1$
		closeButton.addActionListener(this);
		buttonPanel.add(closeButton);
		helpButton = new HelpButton();
		buttonPanel.add(helpButton);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(closeButton);
		getRootPane().registerKeyboardAction(this,"CLOSE",KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void valueChanged(ListSelectionEvent e) {

		if (e.getValueIsAdjusting())
			return;

		DefaultListSelectionModel theList =
			(DefaultListSelectionModel) e.getSource();
		if (theList.isSelectionEmpty()) {
			removeButton.setEnabled(false);
			editButton.setEnabled(false);
		} else {
			removeButton.setEnabled(true);
			editButton.setEnabled(true);

			//String value = (String) theList.getSelectedValue();
			index = theList.getAnchorSelectionIndex();

			System.out.println("index: " + index);

			setSelected(filterList.get(index));
		}

	}

	public void showFilterDialog() {

		Filter parent = getSelected();

		if (parent != null) {

			FilterDialog dialog = new FilterDialog(folder.getFilterPluginHandler(), parent);

			/*
			java.awt.Dimension dim = new Dimension( 400,400 );
			
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			
			setLocation(screenSize.width/2 - dim.width/2, screenSize.height/2 - dim.height/2);
			
			showDialog();
			*/

		}

	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("OK")) {
			System.out.println("ok");
		} else if (action.equals("CLOSE")) {
			// FIXME
			//Config.save();

			setVisible(false);
		} else if (action.equals("ADD")) {
			System.out.println("add");

			Filter filter = FilterList.createEmptyFilter();
			filterList.add(filter);

			listView.update();

			setSelected(filter);

			showFilterDialog();

			listView.update();

		} else if (action.equals("REMOVE")) {
			System.out.println("remove");

			filterList.remove(index);

			removeButton.setEnabled(false);
			editButton.setEnabled(false);

			listView.update();

		} else if (action.equals("EDIT")) {
			showFilterDialog();

			listView.update();
		}
	}

	public class WizardTopBorder extends AbstractBorder {
		protected Insets borderInsets = new Insets(2, 0, 0, 0);
		public void paintBorder(
			Component c,
			Graphics g,
			int x,
			int y,
			int w,
			int h) {
			g.setColor(UIManager.getColor("Button.darkShadow"));
			g.drawLine(x, y, x + w - 1, y);
			g.setColor(Color.white);
			g.drawLine(x, y + 1, x + w - 1, y + 1);
		}
		public Insets getBorderInsets(Component c) {
			return borderInsets;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8871.java