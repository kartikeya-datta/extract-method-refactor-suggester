error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7516.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7516.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7516.java
text:
```scala
c@@.insets = new Insets(2, 2, 2, 2);

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

package org.columba.mail.gui.config.filter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.columba.core.config.Config;
import org.columba.core.config.TableItem;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.gui.util.NotifyDialog;
import org.columba.core.main.MainInterface;
import org.columba.core.plugin.AbstractPluginHandler;
import org.columba.core.plugin.PluginHandlerNotFoundException;
import org.columba.mail.filter.Filter;
import org.columba.mail.filter.FilterCriteria;
import org.columba.mail.filter.FilterRule;
import org.columba.mail.gui.config.filter.plugins.DefaultCriteriaRow;
import org.columba.mail.plugin.AbstractFilterPluginHandler;

public class CriteriaList extends JPanel implements ActionListener {

	private Config config;
	private Filter filter;

	private TableItem v;
	private List list;
	private JPanel panel;
	private AbstractPluginHandler pluginHandler;

	public CriteriaList(Filter filter) {
		super();

		try {

			pluginHandler =
				MainInterface.pluginManager.getHandler(
					"org.columba.mail.filter");
		} catch (PluginHandlerNotFoundException ex) {
			NotifyDialog d = new NotifyDialog();
			d.showDialog(ex);
		}

		this.config = MainInterface.config;
		this.filter = filter;

		list = new Vector();

		panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(panel);
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

		scrollPane.setPreferredSize(new Dimension(500, 100));
		setLayout(new BorderLayout());

		add(scrollPane, BorderLayout.CENTER);

		update();
	}

	public void updateComponents(boolean b) {
		if (!b) {
			for (Iterator it = list.iterator(); it.hasNext();) {
				DefaultCriteriaRow row = (DefaultCriteriaRow)  it.next();
			// for (int i = 0; i < list.size(); i++) {
				// DefaultCriteriaRow row = (DefaultCriteriaRow) list.get(i);
				row.updateComponents(false);
			}
		}
	}

	public void add() {

		FilterRule rule = filter.getFilterRule();
		rule.addEmptyCriteria();

		updateComponents(false);
		update();
	}

	public void remove(int i) {

		FilterRule rule = filter.getFilterRule();

		if (rule.count() > 1) {
			updateComponents(false);
			rule.remove(i);
			update();
		}
	}

	public void update() {
		panel.removeAll();
		list.clear();

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(gridbag);

		FilterRule rule = filter.getFilterRule();

		for (int i = 0; i < rule.count(); i++) {
			FilterCriteria criteria = rule.get(i);
			String type = criteria.getType();
			DefaultCriteriaRow column = null;

			c.fill = GridBagConstraints.NONE;
			//c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = GridBagConstraints.RELATIVE;
			c.gridy = i;
			c.weightx = 1.0;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.insets = new Insets(0, 0, 0, 0);
			c.gridwidth = 1;

			//String className = pluginList.getGuiClassName(type);

			Object[] args = { pluginHandler, this, criteria };

			try {
				column =
					(DefaultCriteriaRow)
						(
							(
								AbstractFilterPluginHandler) pluginHandler)
									.getGuiPlugin(
						type,
						args);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			/*
			if (type.equalsIgnoreCase("Custom Headerfield")) {
				column = new CustomHeaderfieldCriteriaRow(this, criteria);
				gridbag.setConstraints(column, c);
			} else if (type.equalsIgnoreCase("Date")) {
				column = new DateCriteriaRow(this, criteria);
				gridbag.setConstraints(column, c);
			} else if (type.equalsIgnoreCase("Size")) {
				column = new SizeCriteriaRow(this, criteria);
				gridbag.setConstraints(column, c);
			} else if (type.equalsIgnoreCase("Flags")) {
				column = new FlagsCriteriaRow(this, criteria);
				gridbag.setConstraints(column, c);
			} else if (type.equalsIgnoreCase("Priority")) {
				column = new PriorityCriteriaRow(this, criteria);
				gridbag.setConstraints(column, c);
			} else if (type.equalsIgnoreCase("Body")) {
				column = new BodyCriteriaRow(this, criteria);
				gridbag.setConstraints(column, c);
			} else {
				column = new HeaderCriteriaRow(this, criteria);
				
			}
			*/

			// fall-back if error occurs
			if (column == null) {
				try {
					column =
						(DefaultCriteriaRow)
							(
								(
									AbstractFilterPluginHandler) pluginHandler)
										.getGuiPlugin(
							"Subject",
							args);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				criteria.setType("Subject");
			}

			gridbag.setConstraints(column.getContentPane(), c);
			list.add(column);

			panel.add(column.getContentPane());

			JButton addButton =
				new JButton(ImageLoader.getSmallImageIcon("stock_add_16.png"));
			addButton.setActionCommand("ADD");
			addButton.setMargin(new Insets(0, 0, 0, 0));
			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					add();
				}
			});

			/*
			//c.insets = new Insets(1, 2, 1, 2);
			c.gridx = GridBagConstraints.RELATIVE;
			c.anchor = GridBagConstraints.NORTHEAST;
			c.weightx = 1.0;
			gridbag.setConstraints(addButton, c);
			panel.add(addButton);
			*/

			JButton removeButton =
				new JButton(
					ImageLoader.getSmallImageIcon("stock_remove_16.png"));
			removeButton.setMargin(new Insets(0, 0, 0, 0));
			removeButton.setActionCommand(new Integer(i).toString());
			final int index = i;
			removeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					remove(index);
				}
			});

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(0, 2, 2, 2));
			buttonPanel.add(removeButton);
			buttonPanel.add(addButton);

			//c.insets = new Insets(1, 2, 1, 2);
			c.gridx = GridBagConstraints.REMAINDER;
			c.anchor = GridBagConstraints.NORTHEAST;
			gridbag.setConstraints(buttonPanel, c);
			panel.add(buttonPanel);
		}

		c.weighty = 1.0;
		Component box = Box.createVerticalGlue();
		gridbag.setConstraints(box, c);
		panel.add(box);

		validate();
		repaint();
	}

	public void actionPerformed(ActionEvent e) {
		updateComponents(false);
		update();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7516.java