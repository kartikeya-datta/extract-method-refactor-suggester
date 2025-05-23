error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1606.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1606.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1606.java
text:
```scala
.@@getExtensionHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_CONFIG);

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

package org.columba.core.gui.plugin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;

import net.javaprog.ui.wizard.plaf.basic.SingleSideEtchedBorder;

import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.exception.PluginLoadingFailedException;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.api.plugin.PluginMetadata;
import org.columba.core.config.Config;
import org.columba.core.gui.base.ButtonWithMnemonic;
import org.columba.core.gui.base.InfoViewerDialog;
import org.columba.core.help.HelpManager;
import org.columba.core.io.DirectoryIO;
import org.columba.core.io.ZipFileIO;
import org.columba.core.plugin.PluginManager;
import org.columba.core.resourceloader.GlobalResourceLoader;

/**
 * @author fdietz
 * 
 * This dialog lets you view all installed plugins in a categorized tree view.
 * 
 * There are buttons which let you: - install new plugins - remove plugins -
 * enable/disable plugins - view plugin info (readme.txt in plugin folder)
 */
public class PluginManagerDialog extends JDialog implements ActionListener,
		TreeSelectionListener {
	private static final String RESOURCE_PATH = "org.columba.core.i18n.dialog";

	protected JButton installButton;

	protected JButton removeButton;

	protected JButton optionsButton;

	protected JButton infoButton;

	protected JButton helpButton;

	protected JButton closeButton;

	protected PluginTree table;

	protected IExtensionHandler configHandler;

	protected PluginNode selectedNode;

	public PluginManagerDialog() {
		// modal JDialog
		super((JFrame) null, GlobalResourceLoader.getString(RESOURCE_PATH,
				"pluginmanager", "title"), true);

		try {
			configHandler =  PluginManager.getInstance()
					.getHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_CONFIG);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		initComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	protected void initComponents() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		getContentPane().add(mainPanel);

		installButton = new ButtonWithMnemonic(GlobalResourceLoader.getString(
				RESOURCE_PATH, "pluginmanager", "install"));
		installButton.setActionCommand("INSTALL");
		installButton.addActionListener(this);

		removeButton = new ButtonWithMnemonic(GlobalResourceLoader.getString(
				RESOURCE_PATH, "pluginmanager", "remove"));
		removeButton.setActionCommand("REMOVE");
		removeButton.setEnabled(false);
		removeButton.addActionListener(this);

		optionsButton = new ButtonWithMnemonic(GlobalResourceLoader.getString(
				RESOURCE_PATH, "pluginmanager", "options"));
		optionsButton.setActionCommand("OPTIONS");
		optionsButton.setEnabled(false);
		optionsButton.addActionListener(this);

		infoButton = new ButtonWithMnemonic(GlobalResourceLoader.getString(
				RESOURCE_PATH, "pluginmanager", "info"));
		infoButton.setActionCommand("INFO");
		infoButton.setEnabled(false);
		infoButton.addActionListener(this);

		// top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		// topPanel.setLayout( );
		JPanel topBorderPanel = new JPanel();
		topBorderPanel.setLayout(new BorderLayout());

		// topBorderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5,
		// 0));
		topBorderPanel.add(topPanel);

		// mainPanel.add( topBorderPanel, BorderLayout.NORTH );
		JLabel nameLabel = new JLabel("name");
		nameLabel.setEnabled(false);
		topPanel.add(nameLabel);

		topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		topPanel.add(Box.createHorizontalGlue());

		Component glue = Box.createVerticalGlue();
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = GridBagConstraints.REMAINDER;

		// c.fill = GridBagConstraints.HORIZONTAL;
		gridBagLayout.setConstraints(glue, c);

		gridBagLayout = new GridBagLayout();
		c = new GridBagConstraints();

		JPanel eastPanel = new JPanel(gridBagLayout);
		mainPanel.add(eastPanel, BorderLayout.EAST);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(installButton, c);
		eastPanel.add(installButton);

		Component strut1 = Box.createRigidArea(new Dimension(30, 5));
		gridBagLayout.setConstraints(strut1, c);
		eastPanel.add(strut1);

		gridBagLayout.setConstraints(removeButton, c);
		eastPanel.add(removeButton);

		Component strut = Box.createRigidArea(new Dimension(30, 5));
		gridBagLayout.setConstraints(strut, c);
		eastPanel.add(strut);

		gridBagLayout.setConstraints(optionsButton, c);
		eastPanel.add(optionsButton);

		Component strut3 = Box.createRigidArea(new Dimension(30, 5));
		gridBagLayout.setConstraints(strut3, c);
		eastPanel.add(strut3);

		gridBagLayout.setConstraints(infoButton, c);
		eastPanel.add(infoButton);

		strut = Box.createRigidArea(new Dimension(30, 20));
		gridBagLayout.setConstraints(strut, c);
		eastPanel.add(strut);

		glue = Box.createVerticalGlue();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		gridBagLayout.setConstraints(glue, c);
		eastPanel.add(glue);

		// centerpanel
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

		/*
		 * listView = new FilterListTable(filterList, this);
		 * listView.getSelectionModel().addListSelectionListener(this);
		 * JScrollPane scrollPane = new JScrollPane(listView);
		 * scrollPane.setPreferredSize(new Dimension(300, 250));
		 * scrollPane.getViewport().setBackground(Color.white);
		 * centerPanel.add(scrollPane);
		 */
		table = new PluginTree();
		table.getTree().addTreeSelectionListener(this);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(350, 300));
		scrollPane.getViewport().setBackground(Color.white);
		centerPanel.add(scrollPane);

		mainPanel.add(centerPanel);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 6, 0));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JButton closeButton = new ButtonWithMnemonic(GlobalResourceLoader
				.getString("global", "global", "close"));
		closeButton.setActionCommand("CLOSE");
		closeButton.addActionListener(this);
		buttonPanel.add(closeButton);

		ButtonWithMnemonic helpButton = new ButtonWithMnemonic(
				GlobalResourceLoader.getString("global", "global", "help"));
		buttonPanel.add(helpButton);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(closeButton);
		getRootPane().registerKeyboardAction(this, "CLOSE",
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		// associate with JavaHelp
		HelpManager.getInstance().enableHelpOnButton(helpButton,
				"extending_columba_1");
		HelpManager.getInstance().enableHelpKey(getRootPane(),
				"extending_columba_1");
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("CLOSE")) {
			setVisible(false);
		} else if (action.equals("INFO")) {
			String id = selectedNode.getId();

			URL url = PluginManager.getInstance().getInfoURL(id);
			if (url != null) {
				try {
					new InfoViewerDialog(url);
				} catch (IOException ioe) {
				}
			}
		} else if (action.equals("OPTIONS")) {
			String id = selectedNode.getId();
			id = id.substring(id.lastIndexOf(".") + 1, id.length());

			try {
				ConfigurationDialog dialog = new ConfigurationDialog(id);
				dialog.setVisible(true);
			} catch (PluginHandlerNotFoundException phnfe) {
			} catch (PluginLoadingFailedException plfe) {
			}
		} else if (action.equals("REMOVE")) {
			// get plugin directory
			File directory = PluginManager.getInstance().getPluginMetadata(
					selectedNode.getId()).getDirectory();

			// delete plugin from disk
			DirectoryIO.delete(directory);

			// remove plugin from view
			table.removePluginNode(selectedNode);
		} else if (action.equals("INSTALL")) {
			JFileChooser chooser = new JFileChooser();
			chooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File file) {
					return file.isDirectory()
 file.getName().toLowerCase().endsWith(".zip");
				}

				public String getDescription() {
					return GlobalResourceLoader.getString(RESOURCE_PATH,
							"pluginmanager", "filefilter");
				}
			});
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);

			if (result == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();

				installPlugin(file);
			}
		}
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		selectedNode = (PluginNode) arg0.getPath().getLastPathComponent();

		if (selectedNode == null) {
			return;
		}

		boolean isCategoryFolder = selectedNode.isCategory();

		if (isCategoryFolder) {
			// this is just a folder
			// ->disable all actions
			removeButton.setEnabled(false);
			infoButton.setEnabled(false);

			optionsButton.setEnabled(false);
		} else {
			removeButton.setEnabled(true);
			infoButton.setEnabled(selectedNode.hasInfo());

			if (selectedNode == null) {
				return;
			}

			// if plugin has config extension point
			String id = selectedNode.getId();
			id = id.substring(id.lastIndexOf(".") + 1, id.length());
			optionsButton.setEnabled(configHandler.exists(id));
		}
	}

	/**
	 * Returns the currently selected node or null if none is selected.
	 */
	public PluginNode getSelectedNode() {
		return selectedNode;
	}

	protected void installPlugin(File file) {
		// use user's config folder in his/her home-folder
		File destination = new File(Config.getInstance().getConfigDirectory(),
				"plugins");

		File pluginDirectory;
		try {
			// extract plugin
			ZipFileIO.extract(file, destination);

			pluginDirectory = ZipFileIO.getFirstFile(file);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, GlobalResourceLoader.getString(
					RESOURCE_PATH, "pluginmanager", "errExtract.msg"),
					GlobalResourceLoader.getString(RESOURCE_PATH,
							"pluginmanager", "errExtract.title"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (pluginDirectory != null) {
			String id = PluginManager.getInstance().addPlugin(pluginDirectory);
			PluginMetadata metadata = PluginManager.getInstance()
					.getPluginMetadata(id);

			table.addPlugin(metadata);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1606.java