error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2373.java
text:
```scala
f@@older.updateConfiguration();

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

package org.columba.mail.gui.config.account;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.columba.core.gui.util.DialogStore;
import org.columba.core.help.HelpManager;
import org.columba.core.main.MainInterface;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.IdentityItem;
import org.columba.mail.config.SmtpItem;
import org.columba.mail.folder.imap.IMAPRootFolder;
import org.columba.mail.pop3.POP3ServerController;
import org.columba.mail.util.MailResourceLoader;

public class AccountDialog implements ActionListener, ListSelectionListener {
	private JDialog dialog;

	private AccountItem accountItem;

	private IdentityPanel identityPanel;
	private IncomingServerPanel incomingServerPanel;
	private OutgoingServerPanel outgoingServerPanel;
	private SecurityPanel securityPanel;

	//private SpecialFoldersPanel specialFoldersPanel;

	private ReceiveOptionsPanel receiveOptionsPanel;

	//private PanelChooser panelChooser;

	private JPanel selected = null;

	public AccountDialog(AccountItem item) {
		dialog = DialogStore.getDialog();
		dialog.setTitle(
			MailResourceLoader.getString(
				"dialog",
				"account",
				"preferences_for")
				+ " "
				+ item.getName());
		this.accountItem = item;
		createPanels();
		initComponents();

		//panelChooser.addListSelectionListener(this);

		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	protected void createPanels() {

		IdentityItem identityItem = accountItem.getIdentityItem();
		identityPanel = new IdentityPanel(accountItem, identityItem);

		receiveOptionsPanel = new ReceiveOptionsPanel(dialog, accountItem);

		incomingServerPanel =
			new IncomingServerPanel(dialog, accountItem, receiveOptionsPanel);

		outgoingServerPanel = new OutgoingServerPanel(accountItem);

		/*
		specialFoldersPanel = new SpecialFoldersPanel(accountItem,
				accountItem.getSpecialFoldersItem());
		*/

		securityPanel = new SecurityPanel(accountItem.getPGPItem());
	}

	protected void initComponents() {
		dialog.getContentPane().setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JTabbedPane tp = new JTabbedPane();
		tp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

		/*
		IdentityItem identityItem = accountItem.getIdentityItem();
		identityPanel = new IdentityPanel( accountItem, identityItem);
		*/

		tp.add(
			MailResourceLoader.getString("dialog", "account", "identity"),
			identityPanel);
		//$NON-NLS-1$

		String incomingServerPanelTitle =
			MailResourceLoader.getString("dialog", "account", "incomingserver");
		if (accountItem.isPopAccount()) {
			incomingServerPanelTitle += " (POP3)";
		} else {
			incomingServerPanelTitle += " (IMAP4)";
		}
		tp.add(incomingServerPanelTitle, incomingServerPanel);

		tp.add(
			MailResourceLoader.getString("dialog", "account", "receiveoptions"),
			receiveOptionsPanel);

		SmtpItem smtpItem = accountItem.getSmtpItem();
		/*
		outgoingServerPanel = new OutgoingServerPanel( smtpItem);
		*/
		tp.add(
			MailResourceLoader.getString("dialog", "account", "outgoingserver"),
			outgoingServerPanel);
		//$NON-NLS-1$

		/*
		specialFoldersPanel =
			new SpecialFoldersPanel(
				accountItem,
				accountItem.getSpecialFoldersItem());
		*/
		/*
		tp.add(MailResourceLoader.getString(
		                                "dialog",
		                                "account",
		                                "specialfolders"),
			specialFoldersPanel);
		*/
		//$NON-NLS-1$

		/*
		securityPanel = new SecurityPanel( accountItem.getPGPItem());
		*/
		tp.add(
			MailResourceLoader.getString("dialog", "account", "security"),
			securityPanel);
		//$NON-NLS-1$

		mainPanel.add(tp, BorderLayout.CENTER);

		dialog.getContentPane().add(mainPanel, BorderLayout.CENTER);
		dialog.getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
		dialog.getRootPane().registerKeyboardAction(
			this,
			"CANCEL",
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);
		dialog.getRootPane().registerKeyboardAction(
			this,
			"HELP",
			KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	/*
	protected void initComponents()
	{
		dialog.getContentPane().setLayout(new BorderLayout());
		
		
		
		dialog.getContentPane().add( identityPanel, BorderLayout.CENTER );
		selected = identityPanel;
		
		panelChooser = new PanelChooser();
		dialog.getContentPane().add( panelChooser, BorderLayout.WEST );
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder( new WizardTopBorder() );
		bottomPanel.setLayout( new BorderLayout() );
		
		JPanel buttonPanel = createButtonPanel();
		bottomPanel.add( buttonPanel, BorderLayout.CENTER );
		
		dialog.getContentPane().add( bottomPanel, BorderLayout.SOUTH );
		
	}
	*/

	protected JPanel createButtonPanel() {
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		//bottom.setLayout( new BoxLayout( bottom, BoxLayout.X_AXIS ) );
		bottom.setBorder(BorderFactory.createEmptyBorder(17, 12, 11, 11));

		//bottom.add( Box.createHorizontalStrut());

		JButton cancelButton =
			new JButton(MailResourceLoader.getString("global", "cancel"));
		//$NON-NLS-1$ //$NON-NLS-2$
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("CANCEL"); //$NON-NLS-1$

		JButton okButton =
			new JButton(MailResourceLoader.getString("global", "ok"));
		//$NON-NLS-1$ //$NON-NLS-2$
		okButton.addActionListener(this);
		okButton.setActionCommand("OK"); //$NON-NLS-1$
		okButton.setDefaultCapable(true);
		dialog.getRootPane().setDefaultButton(okButton);

		JButton helpButton =
			new JButton(MailResourceLoader.getString("global", "help"));
		// associate with JavaHelp
		HelpManager.enableHelpOnButton(helpButton, "configuring_columba");

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3, 5, 0));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(helpButton);

		//bottom.add( Box.createHorizontalGlue() );

		bottom.add(buttonPanel, BorderLayout.EAST);

		return bottom;
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("OK")) //$NON-NLS-1$
			{

			identityPanel.updateComponents(false);
			incomingServerPanel.updateComponents(false);
			receiveOptionsPanel.updateComponents(false);
			outgoingServerPanel.updateComponents(false);
			securityPanel.updateComponents(false);
			/*
			specialFoldersPanel.updateComponents(false);
			*/

			if (accountItem.isPopAccount()) {

				int uid = accountItem.getUid();
				POP3ServerController c =
					MainInterface.popServerCollection.uidGet(uid);
				c.restartTimer();

				//MainInterface.popServerCollection.enableMailCheckIcon();
			} else {
				// update tree label
				int uid = accountItem.getUid();

				IMAPRootFolder folder =
					(IMAPRootFolder) MainInterface.treeModel.getImapFolder(uid);
				folder.restartTimer();

				//folder.setName(accountItem.getName());

				//folder.restartTimer();

			}

			dialog.setVisible(false);
		} else if (action.equals("CANCEL")) //$NON-NLS-1$
			{
			dialog.setVisible(false);
		} else if (action.equals("HELP")) {
			/*
			URLController c = new URLController();
			try {
				c.open(
					new URL("http://columba.sourceforge.net/phpwiki/index.php/Configure%20Columba"));
			} catch (MalformedURLException mue) {
			}
			*/

		}

	}

	protected void setSelection(JPanel panel) {
		dialog.getContentPane().remove(selected);
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		dialog.validate();
		dialog.repaint();
		selected = panel;
	}

	public void valueChanged(ListSelectionEvent e) {
		JList lsm = (JList) e.getSource();
		switch (lsm.getSelectedIndex()) {
			case 0 :
				setSelection(identityPanel);
				break;
			case 1 :
				setSelection(incomingServerPanel);
				break;
			case 2 :
				setSelection(outgoingServerPanel);
				break;
			case 3 :
				setSelection(securityPanel);
				break;
				/*
				case 4 :
					setSelection(securityPanel);
					break;
				*/
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2373.java