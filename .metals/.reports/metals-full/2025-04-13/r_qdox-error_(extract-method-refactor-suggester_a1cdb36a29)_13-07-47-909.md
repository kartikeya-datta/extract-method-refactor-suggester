error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6782.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6782.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,71]

error in qdox parser
file content:
```java
offset: 71
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6782.java
text:
```scala
"10dlu, 10dlu, max(100;default), 3dlu, max(150dlu;default):grow, 3dlu",@@

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
package org.columba.mail.gui.config.account;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JRadioButton;

import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.gui.util.DefaultFormBuilder;
import org.columba.core.gui.util.MultiLineLabel;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.SpamItem;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.gui.tree.util.SelectFolderDialog;
import org.columba.mail.gui.tree.util.TreeNodeList;
import org.columba.mail.main.MailInterface;
import org.columba.mail.util.MailResourceLoader;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Panel for spam options.
 * 
 * @author fdietz
 *  
 */
public class SpamPanel extends DefaultPanel implements ActionListener {

	private JDialog dialog;

	private AccountItem item;

	private JCheckBox enableCheckBox;

	private JCheckBox addressCheckBox;

	private JCheckBox incomingCheckBox;

	private JCheckBox markCheckBox;

	private JRadioButton incomingTrashRadioButton;

	private JRadioButton incomingMoveToRadioButton;

	private JButton incomingChooseFolderButton;

	private JRadioButton markTrashRadioButton;

	private JRadioButton markMoveToRadioButton;

	private JButton markChooseFolderButton;

	private MultiLineLabel label;

	private FrameMediator mediator;

	public SpamPanel(FrameMediator mediator, AccountItem item) {
		this.item = item;
		this.mediator = mediator;

		initComponents();

		updateComponents(true);
		layoutComponents();
	}

	protected void initComponents() {
		enableCheckBox = new JCheckBox(MailResourceLoader.getString("dialog",
				"account", "enable_filter"));
		enableCheckBox.setActionCommand("ENABLE");
		enableCheckBox.addActionListener(this);

		addressCheckBox = new JCheckBox(MailResourceLoader.getString("dialog",
				"account", "dont_mark_message"));

		incomingCheckBox = new JCheckBox(MailResourceLoader.getString("dialog",
				"account", "move_incoming_messages"));
		incomingCheckBox.setActionCommand("INCOMING");
		incomingCheckBox.addActionListener(this);

		incomingChooseFolderButton = new JButton("Inbox");
		incomingChooseFolderButton.setActionCommand("INCOMING_BUTTON");
		incomingChooseFolderButton.addActionListener(this);

		incomingTrashRadioButton = new JRadioButton(MailResourceLoader
				.getString("dialog", "account", "trash_folder"));
		incomingMoveToRadioButton = new JRadioButton(MailResourceLoader
				.getString("dialog", "account", "move_to"));
		ButtonGroup group = new ButtonGroup();
		group.add(incomingTrashRadioButton);
		group.add(incomingMoveToRadioButton);

		markCheckBox = new JCheckBox(MailResourceLoader.getString("dialog",
				"account", "when_marking_message"));
		markCheckBox.setActionCommand("MARK");
		markCheckBox.addActionListener(this);

		markTrashRadioButton = new JRadioButton(MailResourceLoader.getString(
				"dialog", "account", "move_to_trash_folder"));
		markMoveToRadioButton = new JRadioButton(MailResourceLoader.getString(
				"dialog", "account", "move_to"));
		ButtonGroup group2 = new ButtonGroup();
		group2.add(markTrashRadioButton);
		group2.add(markMoveToRadioButton);

		markChooseFolderButton = new JButton("Inbox");
		markChooseFolderButton.setActionCommand("MARK_BUTTON");
		markChooseFolderButton.addActionListener(this);

		label = new MultiLineLabel(MailResourceLoader.getString("dialog",
				"account", "spam_intro"));
	}

	protected void layoutComponents() {
		//		Create a FormLayout instance.
		FormLayout layout = new FormLayout(
				"10dlu, 10dlu, max(100;default), 3dlu, fill:max(150dlu;default):grow, 3dlu, fill:max(150dlu;default):grow",

				// 2 columns
				""); // rows are added dynamically (no need to define them

		// here)
		DefaultFormBuilder builder = new DefaultFormBuilder(this, layout);
		builder.setLeadingColumnOffset(1);

		// create EmptyBorder between components and dialog-frame
		builder.setDefaultDialogBorder();

		builder.setLeadingColumnOffset(1);

		builder.appendSeparator(MailResourceLoader.getString("dialog",
				"account", "separator_adaptive_spam_filter"));

		builder.append(label, 4);
		builder.nextLine();

		builder.append(enableCheckBox, 4);
		builder.nextLine();

		builder.appendSeparator(MailResourceLoader.getString("dialog",
				"account", "separator_filter_options"));
		builder.nextLine();

		builder.append(addressCheckBox, 4);
		builder.nextLine();

		builder.append(incomingCheckBox, 4);
		builder.nextLine();

		builder.setLeadingColumnOffset(2);

		builder.append(incomingTrashRadioButton, 3);
		builder.nextLine();
		builder.append(incomingMoveToRadioButton, 1);
		builder.append(incomingChooseFolderButton, 2);

		builder.setLeadingColumnOffset(1);

		builder.append(markCheckBox, 4);
		builder.nextLine();

		builder.setLeadingColumnOffset(2);

		builder.append(markTrashRadioButton, 3);
		builder.nextLine();

		builder.append(markMoveToRadioButton, 1);
		builder.append(markChooseFolderButton, 2);
	}

	public void updateComponents(boolean b) {
		SpamItem spam = item.getSpamItem();

		if (b) {
			enableCheckBox.setSelected(spam.isEnabled());

			incomingCheckBox.setSelected(spam
					.isMoveIncomingJunkMessagesEnabled());

			MessageFolder folder = (MessageFolder) MailInterface.treeModel
					.getFolder(spam.getIncomingCustomFolder());
			String treePath = folder.getTreePath();
			incomingChooseFolderButton.setText(treePath);

			incomingMoveToRadioButton.setSelected(!spam
					.isIncomingTrashSelected());

			incomingTrashRadioButton
					.setSelected(spam.isIncomingTrashSelected());

			markCheckBox.setSelected(spam.isMoveMessageWhenMarkingEnabled());

			folder = (MessageFolder) MailInterface.treeModel.getFolder(spam
					.getMoveCustomFolder());
			treePath = folder.getTreePath();
			markChooseFolderButton.setText(treePath);

			markMoveToRadioButton.setSelected(!spam.isMoveTrashSelected());

			markTrashRadioButton.setSelected(spam.isMoveTrashSelected());

			addressCheckBox.setSelected(spam.checkAddressbook());

			enableComponents(enableCheckBox.isSelected());

		} else {
			spam.setEnabled(enableCheckBox.isSelected());

			spam.enableMoveIncomingJunkMessage(incomingCheckBox.isSelected());
			spam.enableMoveMessageWhenMarking(markCheckBox.isSelected());

			spam.selectedIncomingTrash(incomingTrashRadioButton.isSelected());
			spam.selectMoveTrash(markTrashRadioButton.isSelected());

			TreeNodeList list = new TreeNodeList(incomingChooseFolderButton
					.getText());
			MessageFolder folder = (MessageFolder) MailInterface.treeModel
					.getFolder(list);

			if (folder == null) {
				// user didn't select any folder
				// -> make Inbox the default folder
				folder = (MessageFolder) MailInterface.treeModel.getFolder(101);
			}

			int uid = folder.getUid();

			spam.setIncomingCustomFolder(uid);

			list = new TreeNodeList(markChooseFolderButton.getText());
			folder = (MessageFolder) MailInterface.treeModel.getFolder(list);

			if (folder == null) {
				// user didn't select any folder
				// -> make Inbox the default folder
				folder = (MessageFolder) MailInterface.treeModel.getFolder(101);
			}

			uid = folder.getUid();

			spam.setMoveCustomFolder(uid);

			spam.enableCheckAddressbook(addressCheckBox.isSelected());

		}
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		String action = arg0.getActionCommand();

		if (action.equals("ENABLE")) {
			enableComponents(enableCheckBox.isSelected());
		} else if (action.equals("INCOMING")) {
			enableIncoming(incomingCheckBox.isSelected());

		} else if (action.equals("MARK")) {
			enableMark(markCheckBox.isSelected());
		} else if (action.equals("MARK_BUTTON")) {
			SelectFolderDialog dialog = new SelectFolderDialog(mediator);

			if (dialog.success()) {
				MessageFolder folder = dialog.getSelectedFolder();

				String treePath = folder.getTreePath();
				markChooseFolderButton.setText(treePath);
			}
		} else if (action.equals("INCOMING_BUTTON")) {
			SelectFolderDialog dialog = new SelectFolderDialog(mediator);

			if (dialog.success()) {
				MessageFolder folder = dialog.getSelectedFolder();

				String treePath = folder.getTreePath();
				incomingChooseFolderButton.setText(treePath);
			}
		}

	}

	private void enableComponents(boolean enable) {

		addressCheckBox.setEnabled(enable);

		incomingCheckBox.setEnabled(enable);
		enableIncoming(enable);

		markCheckBox.setEnabled(enable);
		enableMark(enable);
	}

	private void enableIncoming(boolean enable) {

		incomingChooseFolderButton.setEnabled(incomingCheckBox.isSelected());
		incomingMoveToRadioButton.setEnabled(incomingCheckBox.isSelected());
		incomingTrashRadioButton.setEnabled(incomingCheckBox.isSelected());
	}

	private void enableMark(boolean enable) {

		markChooseFolderButton.setEnabled(markCheckBox.isSelected());
		markMoveToRadioButton.setEnabled(markCheckBox.isSelected());
		markTrashRadioButton.setEnabled(markCheckBox.isSelected());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6782.java