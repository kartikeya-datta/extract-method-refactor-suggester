error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10048.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10048.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10048.java
text:
```scala
(@@Folder) MainInterface.addressbookInterface.treeModel.getFolder(uid);

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

package org.columba.addressbook.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.columba.addressbook.folder.Folder;
import org.columba.addressbook.folder.HeaderItem;
import org.columba.addressbook.folder.HeaderItemList;
import org.columba.addressbook.gui.tree.util.SelectAddressbookFolderDialog;
import org.columba.addressbook.gui.util.AddressbookDNDListView;
import org.columba.addressbook.gui.util.AddressbookListModel;
import org.columba.core.gui.util.CTabbedPane;
import org.columba.mail.gui.composer.ComposerInterface;
import org.columba.core.main.MainInterface;


/**
 * @version 	1.0
 * @author
 */
public class AddressbookPanel
	extends JPanel
	implements ActionListener, ChangeListener
{
	private AddressbookDNDListView addressbook;
	private ComposerInterface composerInterface;

	private JLabel chooseLabel;
	private JButton chooseButton;
	private JButton toButton;
	private JButton ccButton;
	private JButton bccButton;

	private AddressbookListModel[] list;

	private int[] books;

	private CTabbedPane pane;

	public AddressbookPanel(ComposerInterface composerInterface)
	{
		this.composerInterface = composerInterface;

		books = new int[2];
		books[0] = 101;
		books[1] = 102;

		/*
		list = composerInterface.composerHeader.getListModels();
		*/
		init();

	}

	protected void init()
	{
		setLayout(new BorderLayout());

		pane = new CTabbedPane();
		//pane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		pane.addChangeListener(this);

		add(pane, BorderLayout.CENTER);

		pane.add("Personal Addressbook", new JPanel());
		pane.add("Collected Addresses", new JPanel());



	}

	public JPanel createPanel(int uid)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		addressbook = new AddressbookDNDListView();
		addressbook.setAcceptDrop(false);
		JScrollPane scrollPane = new JScrollPane(addressbook);

		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		topPanel.setLayout(new BorderLayout());

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(1, 3, 5, 5));


		toButton = new JButton("To:");
		toButton.setActionCommand("TO");
		toButton.addActionListener(this);
		ccButton = new JButton("Cc:");
		ccButton.setActionCommand("CC");
		ccButton.addActionListener(this);
		bccButton = new JButton("Bcc:");
		bccButton.setActionCommand("BCC");
		bccButton.addActionListener(this);
		labelPanel.add(toButton);
		labelPanel.add(ccButton);
		labelPanel.add(bccButton);


		topPanel.add(labelPanel, BorderLayout.NORTH);

		JPanel middlePanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		middlePanel.setLayout( layout );
		GridBagConstraints c = new GridBagConstraints();

		JLabel containsLabel = new JLabel("contains:");
		containsLabel.setEnabled(false);
		JTextField textField = new JTextField();
		textField.setEnabled(false);

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.insets = new Insets(5,0,0,5);
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.RELATIVE;
		layout.setConstraints(containsLabel, c);
		middlePanel.add( containsLabel );

		c.anchor = GridBagConstraints.EAST;
		c.weightx = 1.0;
		c.insets = new Insets(5,0,0,0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		layout.setConstraints( textField, c);
		middlePanel.add( textField );

		topPanel.add(middlePanel, BorderLayout.CENTER);

		panel.add(topPanel, BorderLayout.NORTH);

		Folder folder =
			MainInterface.addressbookInterface.tree.getFolder(uid);

		setHeaderList(folder.getHeaderItemList());

		return panel;
	}

	public void setHeaderList(HeaderItemList list)
	{
		Vector v = list.getVector();

		addressbook.setListData(v);
	}

	public void actionPerformed(ActionEvent ev)
	{
		String action = ev.getActionCommand();

		if (action.equals("CHOOSE"))
		{
			SelectAddressbookFolderDialog dialog =
				MainInterface
					.addressbookInterface
					.tree
					.getSelectAddressbookFolderDialog();

			Folder selectedFolder = dialog.getSelectedFolder();
			HeaderItemList list = selectedFolder.getHeaderItemList();
			setHeaderList(list);

			chooseButton.setText(selectedFolder.getName());
		}
		else if (action.equals("TO"))
		{
			int[] array = addressbook.getSelectedIndices();
			ListModel model = addressbook.getModel();
			HeaderItem item;

			composerInterface.headerController.cleanupHeaderItemList();

			for (int j = 0; j < array.length; j++)
			{
				item = (HeaderItem) model.getElementAt(array[j]);
				HeaderItem item2 = (HeaderItem) item.clone();
				item2.add("field","To");
				
				try
				{
					composerInterface.headerController.getAddressbookTableModel().addItem(item2);
				}
				catch ( Exception ex )
				{
					ex.printStackTrace();
				}
			}
		}
		else if (action.equals("CC"))
		{
			int[] array = addressbook.getSelectedIndices();
			ListModel model = addressbook.getModel();
			HeaderItem item;
			
			composerInterface.headerController.cleanupHeaderItemList();

			for (int j = 0; j < array.length; j++)
			{
				item = (HeaderItem) model.getElementAt(array[j]);

				HeaderItem item2 = (HeaderItem) item.clone();
				item2.add("field","Cc");
				
				try
				{
					composerInterface.headerController.getAddressbookTableModel().addItem(item2);
				}
				catch ( Exception ex )
				{
					ex.printStackTrace();
				}
			}
		}
		else if (action.equals("BCC"))
		{
			int[] array = addressbook.getSelectedIndices();
			ListModel model = addressbook.getModel();
			HeaderItem item;


			composerInterface.headerController.cleanupHeaderItemList();
			
			for (int j = 0; j < array.length; j++)
			{
				item = (HeaderItem) model.getElementAt(array[j]);

				HeaderItem item2 = (HeaderItem) item.clone();
				item2.add("field","Bcc");
				
				try
				{
					composerInterface.headerController.getAddressbookTableModel().addItem(item2);
				}
				catch ( Exception ex )
				{
					ex.printStackTrace();
				}
			}
		}

	}

	public void stateChanged(ChangeEvent e)
	{
		int index = pane.getSelectedIndex();
		pane.setComponentAt(index, createPanel(books[index]));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10048.java