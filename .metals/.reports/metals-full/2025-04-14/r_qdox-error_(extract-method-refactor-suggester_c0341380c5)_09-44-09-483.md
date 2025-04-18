error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5.java
text:
```scala
.@@getExtensionHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_EXTERNALTOOLS);

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
package org.columba.core.gui.externaltools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.javaprog.ui.wizard.plaf.basic.SingleSideEtchedBorder;

import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.core.gui.base.ButtonWithMnemonic;
import org.columba.core.gui.base.DoubleClickListener;
import org.columba.core.gui.base.InfoViewerDialog;
import org.columba.core.gui.util.DialogHeaderPanel;
import org.columba.core.help.HelpManager;
import org.columba.core.plugin.PluginManager;
import org.columba.core.resourceloader.GlobalResourceLoader;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Shows a list of external tools used in Columba.
 * <p>
 * Should be the central location to configure these tools
 * 
 * @author fdietz
 */
@SuppressWarnings({"serial","serial"})
public class ExternalToolsDialog extends JDialog implements ActionListener,
		ListSelectionListener {

	private static final String RESOURCE_PATH = "org.columba.core.i18n.dialog";

	IExtensionHandler handler;

	protected JButton helpButton;

	protected JButton closeButton;

	protected JButton configButton;

	protected JButton infoButton;

	protected JList list;

	protected String selection;

	/**
	 * @throws java.awt.HeadlessException
	 */
	public ExternalToolsDialog() throws HeadlessException {
		super(new JFrame(), true);

		// TODO (@author fdietz): i18n
		setTitle("External Tools");

		try {
			handler = PluginManager.getInstance()
					.getHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_EXTERNALTOOLS);
		} catch (PluginHandlerNotFoundException e) {
			e.printStackTrace();
		}

		initComponents();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	protected void initComponents() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		// TODO (@author fdietz): i18n
		configButton = new ButtonWithMnemonic("Con&figure...");
		configButton.setActionCommand("CONFIG");
		configButton.addActionListener(this);
		configButton.setEnabled(false);

		// TODO (@author fdietz): i18n
		infoButton = new ButtonWithMnemonic("&Details...");
		infoButton.setActionCommand("INFO");
		infoButton.addActionListener(this);
		infoButton.setEnabled(false);

		String[] ids = handler.getPluginIdList();
		list = new JList(ids);
		list.addListSelectionListener(this);
		list.addMouseListener(new DoubleClickListener() {
			public void doubleClick(MouseEvent ev) {
				actionPerformed(new ActionEvent(list, 0, "CONFIG"));
			}
		});

		getContentPane()
				.add(
						new DialogHeaderPanel(
								GlobalResourceLoader.getString(RESOURCE_PATH,
										"externaltools", "header_title"),
								GlobalResourceLoader.getString(RESOURCE_PATH,
										"externaltools", "header_description"),
								ImageLoader
										.getIcon(IconKeys.PREFERENCES)),
						BorderLayout.NORTH);

		getContentPane().add(createPanel(), BorderLayout.CENTER);
		getContentPane().add(createBottomPanel(), BorderLayout.SOUTH);

		getRootPane().setDefaultButton(closeButton);
		getRootPane().registerKeyboardAction(this, "CLOSE",
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		// associate with JavaHelp
		HelpManager.getInstance().enableHelpOnButton(helpButton,
				"extending_columba_2");
		HelpManager.getInstance().enableHelpKey(getRootPane(),
				"extending_columba_2");
	}

	private JPanel createPanel() {
		JPanel jpanel1 = new JPanel();
		FormLayout formlayout1 = new FormLayout(
				"FILL:DEFAULT:GROW(1.0),3DLU,FILL:DEFAULT:NONE",
				"CENTER:DEFAULT:NONE,1DLU,FILL:DEFAULT:GROW(1.0),3DLU,CENTER:DEFAULT:NONE");
		CellConstraints cc = new CellConstraints();
		jpanel1.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		jpanel1.setLayout(formlayout1);

		JLabel jlabel1 = new JLabel();
		jlabel1.setText("External Tools:");
		jpanel1.add(jlabel1, cc.xy(1, 1));

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(250, 150));
		jpanel1.add(scrollPane, cc.xy(1, 3));

		jpanel1.add(createPanel1(), new CellConstraints(3, 3, 1, 1,
				CellConstraints.DEFAULT, CellConstraints.TOP));

		return jpanel1;
	}

	private JPanel createPanel1() {
		JPanel jpanel1 = new JPanel();
		FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE",
				"CENTER:DEFAULT:NONE,3DLU,CENTER:DEFAULT:NONE,3DLU,CENTER:DEFAULT:NONE");
		CellConstraints cc = new CellConstraints();
		jpanel1.setLayout(formlayout1);

		jpanel1.add(configButton, cc.xy(1, 1));

		jpanel1.add(infoButton, cc.xy(1, 3));

		return jpanel1;
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 6, 0));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		closeButton = new ButtonWithMnemonic(GlobalResourceLoader.getString(
				"global", "global", "close"));
		closeButton.setActionCommand("CLOSE"); //$NON-NLS-1$
		closeButton.addActionListener(this);
		buttonPanel.add(closeButton);

		helpButton = new ButtonWithMnemonic(GlobalResourceLoader.getString(
				"global", "global", "help"));

		buttonPanel.add(helpButton);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);

		return bottomPanel;
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("CLOSE")) {
			setVisible(false);
		} else if (action.equals("CONFIG")) {
			new ExternalToolsWizardLauncher().launchWizard(selection, false);
		} else if (action.equals("INFO")) {
			AbstractExternalToolsPlugin plugin = null;

			try {
				IExtension extension = handler.getExtension(selection);

				plugin = (AbstractExternalToolsPlugin) extension
						.instanciateExtension(null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			String info = plugin.getDescription();
			new InfoViewerDialog(info);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		boolean enabled = !list.isSelectionEmpty();
		configButton.setEnabled(enabled);
		infoButton.setEnabled(enabled);
		selection = (String) list.getSelectedValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5.java