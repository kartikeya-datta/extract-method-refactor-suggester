error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3479.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3479.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3479.java
text:
```scala
.@@getMiscIcon("signature-nokey.png"));

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

package org.columba.mail.gui.util;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;

import org.columba.core.config.Config;
import org.columba.core.gui.base.ButtonWithMnemonic;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.mail.util.MailResourceLoader;

/**
 * Password dialog prompts user for password.
 * 
 * @author fdietz
 */
public class PasswordDialog extends JDialog implements ActionListener {
	private char[] password;

	private boolean bool = false;

	private JPasswordField passwordField;

	private JCheckBox checkbox;

	private JButton okButton;

	private JButton cancelButton;

	private JButton helpButton;

	public PasswordDialog() {
		super(FrameManager.getInstance().getActiveFrame(), true);

	}

	protected JPanel createButtonPanel() {
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());

		//bottom.setLayout( new BoxLayout( bottom, BoxLayout.X_AXIS ) );
		bottom.setBorder(BorderFactory.createEmptyBorder(17, 12, 11, 11));

		//bottom.add( Box.createHorizontalStrut());
		cancelButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"global", "cancel"));

		//$NON-NLS-1$ //$NON-NLS-2$
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("CANCEL"); //$NON-NLS-1$

		okButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"global", "ok"));

		//$NON-NLS-1$ //$NON-NLS-2$
		okButton.addActionListener(this);
		okButton.setActionCommand("OK"); //$NON-NLS-1$
		okButton.setDefaultCapable(true);
		getRootPane().setDefaultButton(okButton);

		helpButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"global", "help"));

		//$NON-NLS-1$ //$NON-NLS-2$
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3, 5, 0));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(helpButton);

		//bottom.add( Box.createHorizontalGlue() );
		bottom.add(buttonPanel, BorderLayout.EAST);

		return bottom;
	}

	/**
	 * Make dialog visible.
	 * <p>
	 * Note that the emailAddress parameter, needs to be really unique. I
	 * therefore suggest a combination between host and login name, instead.
	 * This way user can see his login name. -> changed method signature!
	 * 
	 * @param login
	 *            login name
	 * @param host
	 *            host name
	 * @param password
	 *            password
	 * @param save
	 *            should the password be saved?
	 */
	public void showDialog(String message, String password, boolean save) {

		/*
		 * JLabel hostLabel = new JLabel(MessageFormat.format(MailResourceLoader
		 * .getString("dialog", "password", "enter_password"), new Object[] {
		 * user, host }));
		 */
		JLabel hostLabel = new JLabel(message);

		passwordField = new JPasswordField(password, 40);

		checkbox = new JCheckBox(MailResourceLoader.getString("dialog",
				"password", "save_password"));
		checkbox.setSelected(save);
		checkbox.setActionCommand("SAVE");
		checkbox.addActionListener(this);

		setTitle(MailResourceLoader.getString("dialog", "password",
				"dialog_title"));

		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		getContentPane().add(centerPanel, BorderLayout.CENTER);

		GridBagLayout mainLayout = new GridBagLayout();
		centerPanel.setLayout(mainLayout);

		GridBagConstraints mainConstraints = new GridBagConstraints();

		JLabel iconLabel = new JLabel(ImageLoader
				.getImageIcon("pgp-signature-nokey.png"));
		mainConstraints.anchor = GridBagConstraints.NORTHWEST;
		mainConstraints.weightx = 1.0;
		mainConstraints.gridwidth = GridBagConstraints.RELATIVE;
		mainConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainLayout.setConstraints(iconLabel, mainConstraints);
		centerPanel.add(iconLabel);

		mainConstraints.gridwidth = GridBagConstraints.REMAINDER;
		mainConstraints.anchor = GridBagConstraints.WEST;
		mainConstraints.insets = new Insets(0, 5, 0, 0);
		mainLayout.setConstraints(hostLabel, mainConstraints);
		centerPanel.add(hostLabel);

		mainConstraints.insets = new Insets(5, 5, 0, 0);
		mainLayout.setConstraints(passwordField, mainConstraints);
		centerPanel.add(passwordField);

		mainConstraints.insets = new Insets(5, 5, 0, 0);
		mainLayout.setConstraints(checkbox, mainConstraints);
		centerPanel.add(checkbox);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());

		JPanel buttonPanel = createButtonPanel();
		bottomPanel.add(buttonPanel, BorderLayout.CENTER);

		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(okButton);
		getRootPane().registerKeyboardAction(this, "CANCEL",
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		requestFocus();
		passwordField.requestFocus();
	}

	public char[] getPassword() {
		return password;
	}

	public boolean success() {
		return bool;
	}

	public boolean getSave() {
		return checkbox.isSelected();
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("OK")) {
			password = passwordField.getPassword();

			bool = true;
			dispose();
		} else if (action.equals("CANCEL")) {
			bool = false;
			dispose();
		} else if (action.equals("SAVE")) {
			if (!checkbox.isSelected()) {
				return;
			} else {
				File configPath = Config.getInstance().getConfigDirectory();
				File defaultConfigPath = Config
						.getDefaultConfigPath();
				while (!configPath.equals(defaultConfigPath)) {
					configPath = configPath.getParentFile();
					if (configPath == null) {
						JOptionPane.showMessageDialog(this, MailResourceLoader
								.getString("dialog", "password",
										"warn_save_msg"), MailResourceLoader
								.getString("dialog", "password",
										"warn_save_title"),
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3479.java