error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1819.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1819.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1819.java
text:
```scala
I@@ExtensionHandler handler = PluginManager.getInstance().getExtensionHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_HTMLVIEWER);

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
package org.columba.mail.gui.config.general;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import net.javaprog.ui.wizard.plaf.basic.SingleSideEtchedBorder;

import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.core.gui.base.ButtonWithMnemonic;
import org.columba.core.gui.base.CheckBoxWithMnemonic;
import org.columba.core.gui.base.LabelWithMnemonic;
import org.columba.core.gui.util.DialogHeaderPanel;
import org.columba.core.help.HelpManager;
import org.columba.core.plugin.PluginManager;
import org.columba.mail.config.ComposerItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.config.OptionsItem;
import org.columba.mail.util.MailResourceLoader;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Mail General Options Dialog
 * 
 * @author fdietz
 */
public class MailOptionsDialog extends JDialog implements ActionListener {
	protected JButton okButton;

	protected JButton cancelButton;

	protected JButton helpButton;

	protected CheckBoxWithMnemonic markCheckBox;

	protected JSpinner markSpinner;

	protected CheckBoxWithMnemonic preferHtmlCheckBox;

	protected CheckBoxWithMnemonic disableHtmlCheckBox;

	protected CheckBoxWithMnemonic enableSmiliesCheckBox;

	protected CheckBoxWithMnemonic quotedColorCheckBox;

	protected JButton quotedColorButton;

	protected JCheckBox emptyTrashCheckBox;

	protected CheckBoxWithMnemonic emptySubjectCheckBox;

	protected CheckBoxWithMnemonic sendHtmlMultipartCheckBox;

	protected CheckBoxWithMnemonic showAttachmentsInlineCheckBox;

	private JLabel selectedBrowserLabel;
	
	protected JComboBox selectedBrowserComboBox;
	
	protected LabelWithMnemonic forwardLabel;

	protected JComboBox forwardComboBox;

	public MailOptionsDialog(JFrame frame) {
		super(frame, MailResourceLoader.getString("dialog", "general",
				"dialog_title"), true);

		initComponents();

		layoutComponents();

		updateComponents(true);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void updateComponents(boolean b) {
		OptionsItem optionsItem = MailConfig.getInstance().getOptionsItem();
		ComposerItem composerItem = MailConfig.getInstance().getComposerItem();

		if (b) {

			showAttachmentsInlineCheckBox.setSelected(optionsItem
					.getBooleanWithDefault(OptionsItem.MESSAGEVIEWER,
							OptionsItem.INLINE_ATTACHMENTS_BOOL, false));

			selectedBrowserComboBox.setSelectedItem(optionsItem
					.getStringWithDefault(OptionsItem.MESSAGEVIEWER,
							OptionsItem.SELECTED_BROWSER, "Default"));
			
			int delay = optionsItem.getIntegerWithDefault(
					OptionsItem.MARKASREAD, OptionsItem.DELAY_INT, 2);
			boolean enable = optionsItem.getBooleanWithDefault(
					OptionsItem.MARKASREAD, OptionsItem.ENABLED_BOOL, true);

			markCheckBox.setSelected(enable);
			markSpinner.setValue(new Integer(delay));

			boolean enableSmilies = optionsItem.getBooleanWithDefault(
					OptionsItem.MESSAGEVIEWER_SMILIES,
					OptionsItem.ENABLED_BOOL, true);

			enableSmiliesCheckBox.setSelected(enableSmilies);
			
			boolean preferHtml = optionsItem.getBooleanWithDefault(
					OptionsItem.HTML, OptionsItem.PREFER_BOOL, true);

			preferHtmlCheckBox.setSelected(preferHtml);

			boolean disablehtml = optionsItem.getBooleanWithDefault(
					OptionsItem.HTML, OptionsItem.DISABLE_BOOL, true);

			disableHtmlCheckBox.setSelected(disablehtml);
			
			boolean askSubject = composerItem.getBooleanWithDefault(
					ComposerItem.SUBJECT, ComposerItem.ASK_IF_EMPTY_BOOL, true);

			emptySubjectCheckBox.setSelected(askSubject);

			boolean sendHtml = composerItem.getBooleanWithDefault(
					ComposerItem.HTML, ComposerItem.SEND_AS_MULTIPART, true);

			sendHtmlMultipartCheckBox.setSelected(sendHtml);

			int forwardStyle = composerItem.getIntegerWithDefault(
					ComposerItem.FORWARD, ComposerItem.STYLE, 0);

			forwardComboBox.setSelectedIndex(forwardStyle);

		} else {

			optionsItem.setInteger(OptionsItem.MARKASREAD,
					OptionsItem.DELAY_INT, ((Integer) markSpinner.getValue())
							.intValue());

			optionsItem.setBoolean(OptionsItem.MARKASREAD,
					OptionsItem.ENABLED_BOOL, markCheckBox.isSelected());

			// notify configuration changes listeners
			// @see org.columba.mail.gui.table.util.MarkAsReadTimer
			optionsItem.notifyObservers(OptionsItem.MARKASREAD);

			optionsItem.setBoolean(OptionsItem.MESSAGEVIEWER_SMILIES,
					OptionsItem.ENABLED_BOOL, enableSmiliesCheckBox
							.isSelected());

			optionsItem.setBoolean(OptionsItem.MESSAGEVIEWER,
					OptionsItem.INLINE_ATTACHMENTS_BOOL,
					showAttachmentsInlineCheckBox.isSelected());
				
			optionsItem.setString(OptionsItem.MESSAGEVIEWER,
					OptionsItem.SELECTED_BROWSER,
					(String) selectedBrowserComboBox.getSelectedItem());

			// notify configuration changes listeners
			// @see org.columba.mail.gui.message.TextViewer
			optionsItem.notifyObservers(OptionsItem.SELECTED_BROWSER);
			
			// send notification event
			// @see org.columba.mail.gui.message.TextViewer
			optionsItem.notifyObservers(OptionsItem.MESSAGEVIEWER_SMILIES);

			optionsItem.setBoolean(OptionsItem.HTML, OptionsItem.PREFER_BOOL,
					preferHtmlCheckBox.isSelected());

			optionsItem.setBoolean(OptionsItem.HTML, OptionsItem.DISABLE_BOOL,
					disableHtmlCheckBox.isSelected());

			composerItem.setBoolean(ComposerItem.SUBJECT,
					ComposerItem.ASK_IF_EMPTY_BOOL, emptySubjectCheckBox
							.isSelected());

			// notify listeners
			// @see org.columba.mail.gui.composer.SubjectController
			composerItem.notifyObservers(ComposerItem.SUBJECT);

			composerItem.setBoolean(ComposerItem.HTML,
					ComposerItem.SEND_AS_MULTIPART, sendHtmlMultipartCheckBox
							.isSelected());
			// notify listeners
			composerItem.notifyObservers(ComposerItem.HTML);

			composerItem.setInteger(ComposerItem.FORWARD, ComposerItem.STYLE,
					forwardComboBox.getSelectedIndex());

			// notify listeners
			// @see org.columba.mail.gui.table.action.ForwardAction
			composerItem.notifyObservers(ComposerItem.FORWARD);

		}
	}

	protected void initComponents() {
		// general
		markCheckBox = new CheckBoxWithMnemonic(MailResourceLoader.getString(
				"dialog", "general", "mark_messages_read"));

		markSpinner = new JSpinner();
		markSpinner.setModel(new SpinnerNumberModel(1, 0, 99, 1));

		emptyTrashCheckBox = new CheckBoxWithMnemonic(MailResourceLoader
				.getString("dialog", "general", "empty_trash"));
		emptyTrashCheckBox.setEnabled(false);

		enableSmiliesCheckBox = new CheckBoxWithMnemonic(MailResourceLoader
				.getString("dialog", "general", "enable_smilies"));

		quotedColorCheckBox = new CheckBoxWithMnemonic(MailResourceLoader
				.getString("dialog", "general", "color_quoted_text"));
		quotedColorButton = new JButton("...");
		quotedColorButton.setActionCommand("COLOR");
		quotedColorButton.addActionListener(this);

		preferHtmlCheckBox = new CheckBoxWithMnemonic(MailResourceLoader
				.getString("dialog", "general", "prefer_html"));
		disableHtmlCheckBox = new CheckBoxWithMnemonic(MailResourceLoader
				.getString("dialog", "general", "disable_html"));
		// composer
		emptySubjectCheckBox = new CheckBoxWithMnemonic(MailResourceLoader
				.getString("dialog", "general", "ask_on_empty_subject"));

		sendHtmlMultipartCheckBox = new CheckBoxWithMnemonic(MailResourceLoader
				.getString("dialog", "general", "send_html_multipart"));

		forwardLabel = new LabelWithMnemonic(MailResourceLoader.getString(
				"dialog", "general", "forward_as"));

		String[] items = {
				MailResourceLoader.getString("dialog", "general",
						"forward_as_attachment"),
				MailResourceLoader.getString("dialog", "general",
						"forward_as_quoted") };

		forwardComboBox = new JComboBox(items);

		showAttachmentsInlineCheckBox = new CheckBoxWithMnemonic(
				"Show Attachments &Inline");
		showAttachmentsInlineCheckBox.setActionCommand("ATTACHMENTS_INLINE");
		showAttachmentsInlineCheckBox.addActionListener(this);

		selectedBrowserLabel = new JLabel("Message Renderer");
		Vector<String> v = new Vector<String>();
		try {
			IExtensionHandler handler = PluginManager.getInstance().getHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_HTMLVIEWER);
			Enumeration e = handler.getExtensionEnumeration();
			while (e.hasMoreElements()) {
				IExtension ext = (IExtension) e.nextElement();
				String id = ext.getMetadata().getId();
				v.add(id);
			}
		} catch (PluginHandlerNotFoundException e) {
			e.printStackTrace();
		}
		selectedBrowserComboBox = new JComboBox(v.toArray(new String[0]));
		selectedBrowserComboBox.setSelectedIndex(0);
		selectedBrowserComboBox.setActionCommand("USE_DEFAULT_BROWSER");
		selectedBrowserComboBox.addActionListener(this);
		
		// button panel
		okButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"global", "ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);

		cancelButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"global", "cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);

		helpButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"global", "help"));

		// associate with JavaHelp
		HelpManager.getInstance().enableHelpOnButton(helpButton,
				"configuring_columba_7");
		HelpManager.getInstance().enableHelpKey(getRootPane(),
				"configuring_columba_7");
	}

	protected void layoutComponents() {
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		// Create a FormLayout instance.
		FormLayout layout = new FormLayout(
				"12dlu, default, 3dlu, max(10dlu;default), 3dlu, default",

				// 3 columns
				""); // rows are added dynamically (no need to define them here)

		// create a form builder
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		// create EmptyBorder between components and dialog-frame
		builder.setDefaultDialogBorder();

		// skip the first column
		builder.setLeadingColumnOffset(1);

		// Add components to the panel:
		builder.appendSeparator(MailResourceLoader.getString("dialog",
				"general", "general"));
		builder.nextLine();

		builder.append(preferHtmlCheckBox, 4);
		builder.nextLine();
		builder.append(disableHtmlCheckBox, 4);
		builder.nextLine();
		builder.append(enableSmiliesCheckBox, 4);
		builder.nextLine();
		builder.append(showAttachmentsInlineCheckBox, 4);
		builder.nextLine();

		

		
		// its maybe better to leave this option out of the dialog
		// -> make it configurable in the xml file anyway
		/*
		 * builder.append(quotedColorCheckBox, quotedColorButton);
		 * builder.nextLine();
		 */
		builder.append(markCheckBox, markSpinner);
		builder.nextLine();
		builder.append(selectedBrowserLabel, selectedBrowserComboBox);
		builder.nextLine();

		//builder.nextLine();

		builder.appendSeparator(MailResourceLoader.getString("dialog",
				"general", "composing_messages"));
		builder.nextLine();

		builder.append(emptySubjectCheckBox, 4);
		builder.nextLine();

		builder.append(sendHtmlMultipartCheckBox, 4);
		builder.nextLine();

		builder.append(forwardLabel, forwardComboBox);
		builder.nextLine();

		//layout.setRowGroups(new int[][]{ {1, 3, 5, 7, 9, 11, 13, 15} });
		/*
		 * builder.append(spellLabel, spellButton); builder.nextLine();
		 */
		contentPane.add(builder.getPanel(), BorderLayout.CENTER);

		// init bottom panel with OK, Cancel buttons
		JPanel bottomPanel = new JPanel(new BorderLayout(0, 0));
		bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 6, 0));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		buttonPanel.add(okButton);

		buttonPanel.add(cancelButton);
		buttonPanel.add(helpButton);

		bottomPanel.add(buttonPanel, BorderLayout.EAST);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(okButton);
		getRootPane().registerKeyboardAction(this, "CANCEL",
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		contentPane.add(new DialogHeaderPanel("Mail Options", "Change email-specific options"), BorderLayout.NORTH);
		
	}

	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();

		if (action.equals("OK")) {
			setVisible(false);

			updateComponents(false);
		} else if (action.equals("CANCEL")) {
			setVisible(false);
		} else if (action.equals("COLOR")) {
			//Set up color chooser for setting quoted color
			Color newColor = JColorChooser.showDialog(this, MailResourceLoader
					.getString("dialog", "general", "choose_text_color"), null);

			if (newColor != null) {
				quotedColorButton.setBackground(newColor);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1819.java