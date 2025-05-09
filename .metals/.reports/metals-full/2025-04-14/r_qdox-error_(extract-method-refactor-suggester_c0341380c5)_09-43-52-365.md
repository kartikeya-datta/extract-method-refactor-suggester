error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9860.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9860.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9860.java
text:
```scala
i@@f( accountItem.isPopAccount() ) authenticationComboBox.addItem("POP before SMTP");

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

/*
 * PopPanel.java
 *
 * Created on 1. November 2000, 23:39
 */

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.columba.mail.config.AccountItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.config.SmtpItem;
import org.columba.mail.util.MailResourceLoader;

/**
 *
 * @author  freddy
 * @version
 */
public class OutgoingServerPanel
	extends DefaultPanel
	implements ActionListener {

	private JButton okButton;
	private JButton cancelButton;
	private JButton helpButton;

	private JLabel hostLabel;
	private JTextField hostTextField;
	private JLabel portLabel;
	private JTextField portTextField;
	private JRadioButton esmtpRadioButton;
	private JLabel loginLabel;
	private JTextField loginTextField;

	private JCheckBox secureCheckBox;

	private JCheckBox needAuthCheckBox;

	private JCheckBox bccyourselfCheckBox;
	private JLabel bccanotherLabel;
	private JTextField bccanotherTextField;
	private JButton selectButton;
	private JCheckBox storePasswordCheckBox;

	private JLabel authenticationLabel;
	private JComboBox authenticationComboBox;

	private JCheckBox defaultAccountCheckBox;

	private SmtpItem item;
	private AccountItem accountItem;

	public OutgoingServerPanel(AccountItem accountItem) {
		super();

		this.accountItem = accountItem;
		item = accountItem.getSmtpItem();

		initComponents();

		updateComponents(true);

	}

	public String getHost() {
		return hostTextField.getText();
	}

	public String getLogin() {
		return loginTextField.getText();
	}

	public boolean isESmtp() {
		return esmtpRadioButton.isSelected();
	}

	protected void updateComponents(boolean b) {

		if (b) {

			hostTextField.setText(item.get("host"));

			portTextField.setText(item.get("port"));

			loginTextField.setText(item.get("user"));

			storePasswordCheckBox.setSelected(item.getBoolean("save_password"));

			if (!item.get("login_method").equals("NONE")) {
				needAuthCheckBox.setSelected(true);

				storePasswordCheckBox.setEnabled(true);
				loginLabel.setEnabled(true);
				loginTextField.setEnabled(true);
				String loginMethod = item.get("login_method");
				authenticationComboBox.setSelectedItem(loginMethod);

			} else {
				needAuthCheckBox.setSelected(false);

				storePasswordCheckBox.setEnabled(false);
				loginLabel.setEnabled(false);
				loginTextField.setEnabled(false);
				authenticationLabel.setEnabled(false);
				authenticationComboBox.setEnabled(false);

			}

			if (MailConfig.getAccountList().getDefaultAccountUid()
				== accountItem.getInteger("uid")) {
				defaultAccountCheckBox.setEnabled(false);
			} else {
				defaultAccountCheckBox.setEnabled(true);
			}

			if (item.getBoolean("use_default_account"))
				defaultAccountCheckBox.setSelected(true);
			else
				defaultAccountCheckBox.setSelected(false);

			if (defaultAccountCheckBox.isEnabled()
				&& defaultAccountCheckBox.isSelected()) {
				showDefaultAccountWarning();
			} else {
				layoutComponents();
			}

		} else {
			item.set("user", loginTextField.getText());

			item.set("save_password", storePasswordCheckBox.isSelected()); //$NON-NLS-1$

			item.set("port", portTextField.getText());

			item.set("host", hostTextField.getText());

			if (needAuthCheckBox.isSelected()) {

				String loginMethod =
					(String) authenticationComboBox.getSelectedItem();
				item.set("login_method", loginMethod);

			} else {

				item.set("login_method", "NONE"); //$NON-NLS-1$

			}

			item.set(
				"use_default_account",
				defaultAccountCheckBox.isSelected());

		}
	}

	protected void layoutComponents() {
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		GridBagLayout mainLayout = new GridBagLayout();
		GridBagConstraints mainConstraints = new GridBagConstraints();

		mainConstraints.anchor = GridBagConstraints.NORTHWEST;
		mainConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainConstraints.weightx = 1.0;

		setLayout(mainLayout);

		mainConstraints.gridwidth = GridBagConstraints.REMAINDER;
		mainConstraints.insets = new Insets(0, 10, 5, 0);
		mainLayout.setConstraints(defaultAccountCheckBox, mainConstraints);
		add(defaultAccountCheckBox);

		JPanel configPanel = new JPanel();
		Border b1 = BorderFactory.createEtchedBorder();
		Border b2 =
			BorderFactory.createTitledBorder(
				b1,
				MailResourceLoader.getString(
					"dialog",
					"account",
					"configuration"));
		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border border = BorderFactory.createCompoundBorder(b2, emptyBorder);
		configPanel.setBorder(border);
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		configPanel.setLayout(layout);

		mainConstraints.gridwidth = GridBagConstraints.REMAINDER;
		mainConstraints.insets = new Insets(0, 0, 0, 0);
		mainLayout.setConstraints(configPanel, mainConstraints);
		add(configPanel);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.1;
		c.gridwidth = GridBagConstraints.RELATIVE;
		layout.setConstraints(hostLabel, c);
		configPanel.add(hostLabel);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0.9;
		layout.setConstraints(hostTextField, c);
		configPanel.add(hostTextField);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.weightx = 0.1;
		layout.setConstraints(portLabel, c);
		configPanel.add(portLabel);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0.9;
		layout.setConstraints(portTextField, c);
		configPanel.add(portTextField);

		JPanel securityPanel = new JPanel();
		b1 = BorderFactory.createEtchedBorder();
		b2 =
			BorderFactory.createTitledBorder(
				b1,
				MailResourceLoader.getString("dialog", "account", "security"));

		emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		border = BorderFactory.createCompoundBorder(b2, emptyBorder);
		securityPanel.setBorder(border);

		mainConstraints.gridwidth = GridBagConstraints.REMAINDER;
		mainLayout.setConstraints(securityPanel, mainConstraints);
		add(securityPanel);

		layout = new GridBagLayout();
		c = new GridBagConstraints();
		securityPanel.setLayout(layout);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(needAuthCheckBox, c);
		securityPanel.add(needAuthCheckBox);

		JPanel panel = new JPanel();
		panel.setLayout(layout);
		c.insets = new Insets(0, 20, 0, 0);
		layout.setConstraints(panel, c);
		securityPanel.add(panel);

		JPanel panel2 = new JPanel();
		panel2.setLayout(layout);

		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;

		layout.setConstraints(panel2, c);
		panel.add(panel2);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.0;
		c.insets = new Insets(0, 0, 0, 0);
		layout.setConstraints(authenticationLabel, c);
		panel2.add(authenticationLabel);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0.0;
		c.insets = new Insets(0, 5, 0, 0);
		layout.setConstraints(authenticationComboBox, c);
		panel2.add(authenticationComboBox);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 0, 0, 0);
		layout.setConstraints(loginLabel, c);
		panel.add(loginLabel);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		layout.setConstraints(loginTextField, c);
		panel.add(loginTextField);

		c.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(storePasswordCheckBox, c);
		securityPanel.add(storePasswordCheckBox);

		c.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(secureCheckBox, c);
		securityPanel.add(secureCheckBox);

		mainConstraints.gridheight = GridBagConstraints.REMAINDER;
		mainConstraints.weighty = 1.0;
		mainConstraints.fill = GridBagConstraints.VERTICAL;
		Component vglue = Box.createVerticalGlue();
		mainLayout.setConstraints(vglue, mainConstraints);
		add(vglue);
	}

	protected void showDefaultAccountWarning() {

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		GridBagLayout mainLayout = new GridBagLayout();
		GridBagConstraints mainConstraints = new GridBagConstraints();

		setLayout(mainLayout);

		mainConstraints.gridwidth = GridBagConstraints.REMAINDER;
		mainConstraints.anchor = GridBagConstraints.NORTHWEST;
		mainConstraints.weightx = 1.0;
		mainConstraints.insets = new Insets(0, 10, 5, 0);
		mainLayout.setConstraints(defaultAccountCheckBox, mainConstraints);
		add(defaultAccountCheckBox);

		mainConstraints = new GridBagConstraints();
		mainConstraints.weighty = 1.0;
		mainConstraints.gridwidth = GridBagConstraints.REMAINDER;
		/*
		mainConstraints.fill = GridBagConstraints.BOTH;
		mainConstraints.insets = new Insets(0, 0, 0, 0);
		mainConstraints.gridwidth = GridBagConstraints.REMAINDER;
		mainConstraints.weightx = 1.0;
		mainConstraints.weighty = 1.0;
		*/

		JLabel label =
			new JLabel(
				MailResourceLoader.getString(
					"dialog",
					"account",
					"using_default_account_settings"));
		Font newFont = label.getFont().deriveFont(Font.BOLD);
		label.setFont(newFont);
		mainLayout.setConstraints(label, mainConstraints);
		add(label);

	}

	protected void initComponents() {

		defaultAccountCheckBox =
			new JCheckBox(
				MailResourceLoader.getString(
					"dialog",
					"account",
					"use_default_account_settings"));
		defaultAccountCheckBox.setMnemonic(
			MailResourceLoader.getMnemonic(
				"dialog",
				"account",
				"use_default_account_settings"));
		//defaultAccountCheckBox.setEnabled(false);
		defaultAccountCheckBox.setActionCommand("DEFAULT_ACCOUNT");
		defaultAccountCheckBox.addActionListener(this);

		hostLabel = new JLabel(MailResourceLoader.getString("dialog", "account", "host")); //$NON-NLS-1$
		hostLabel.setDisplayedMnemonic(
			MailResourceLoader.getMnemonic("dialog", "account", "host"));
		hostTextField = new JTextField();
		hostLabel.setLabelFor(hostTextField);
		portLabel = new JLabel(MailResourceLoader.getString("dialog", "account", "port")); //$NON-NLS-1$
		portLabel.setDisplayedMnemonic(
			MailResourceLoader.getMnemonic("dialog", "account", "port"));
		portTextField = new JTextField();
		portLabel.setLabelFor(portTextField);

		needAuthCheckBox = new JCheckBox(MailResourceLoader.getString("dialog", "account", "server_needs_authentification")); //$NON-NLS-1$
		needAuthCheckBox.setMnemonic(
			MailResourceLoader.getMnemonic(
				"dialog",
				"account",
				"server_needs_authentification"));
		needAuthCheckBox.setActionCommand("AUTH"); //$NON-NLS-1$
		needAuthCheckBox.addActionListener(this);

		storePasswordCheckBox = new JCheckBox();
		storePasswordCheckBox.setText(MailResourceLoader.getString("dialog", "account", "store_password_in_configuration_file")); //$NON-NLS-1$
		storePasswordCheckBox.setMnemonic(
			MailResourceLoader.getMnemonic(
				"dialog",
				"account",
				"store_password_in_configuration_file"));
		secureCheckBox = new JCheckBox();
		secureCheckBox.setText(MailResourceLoader.getString("dialog", "account", "use_SSL_for_secure_connection")); //$NON-NLS-1$
		secureCheckBox.setMnemonic(MailResourceLoader.getMnemonic("dialog", "account", "use_SSL_for_secure_connection")); //$NON-NLS-1$
		secureCheckBox.setEnabled(false);

		authenticationLabel =
			new JLabel(
				MailResourceLoader.getString(
					"dialog",
					"account",
					"authentication_type"));
		authenticationLabel.setDisplayedMnemonic(
			MailResourceLoader.getMnemonic(
				"dialog",
				"account",
				"authentication_type"));
		authenticationComboBox = new JComboBox();
		authenticationComboBox.addItem("PLAIN");
		authenticationComboBox.addItem("LOGIN");
		authenticationComboBox.addItem("POP before SMTP");
		authenticationComboBox.addActionListener(this);
		authenticationLabel.setLabelFor(authenticationComboBox);

		loginLabel =
			new JLabel(
				MailResourceLoader.getString("dialog", "account", "login"));
		loginLabel.setDisplayedMnemonic(
			MailResourceLoader.getMnemonic("dialog", "account", "login"));
		loginTextField = new JTextField();
		loginLabel.setLabelFor(loginTextField);

	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (e.getSource().equals(authenticationComboBox)) {
			String selection =
				(String) authenticationComboBox.getSelectedItem();

			loginLabel.setEnabled(true);
			loginTextField.setEnabled(true);
			storePasswordCheckBox.setEnabled(true);

		} else if (action.equals("DEFAULT_ACCOUNT")) {
			removeAll();

			if (defaultAccountCheckBox.isSelected()) {
				showDefaultAccountWarning();

			} else {
				layoutComponents();

			}

			revalidate();

		} else {

			if (action.equals("AUTH")) {
				if (needAuthCheckBox.isSelected()) {
					loginLabel.setEnabled(true);
					loginTextField.setEnabled(true);
					storePasswordCheckBox.setEnabled(true);
					authenticationLabel.setEnabled(true);
					authenticationComboBox.setEnabled(true);
				} else {
					loginLabel.setEnabled(false);
					loginTextField.setEnabled(false);
					storePasswordCheckBox.setEnabled(false);
					authenticationLabel.setEnabled(false);
					authenticationComboBox.setEnabled(false);
				}
			}
		}
	}

	public boolean isFinished() {
		boolean result = false;
		String host = getHost();
		boolean esmtp = isESmtp();
		//String sentFolder = getSentFolder();
		//Folder f = MainInterface.treeViewer.getFolder( new TreeNodeList( sentFolder ) );
		if (host.length() == 0) {
			JOptionPane.showMessageDialog(null, MailResourceLoader.getString("dialog", "account", "You_have_to_enter_a_host_name")); //$NON-NLS-1$
		} else if (esmtp == true) {
			String login = getLogin();
			if (login.length() == 0) {

				JOptionPane.showMessageDialog(null, MailResourceLoader.getString("dialog", "account", "You_have_to_enter_a_login_name")); //$NON-NLS-1$
			} else { //saveOutgoingPanel();
				result = true;
			}
		} else {
			//saveOutgoingData();
			result = true;
		}

		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9860.java