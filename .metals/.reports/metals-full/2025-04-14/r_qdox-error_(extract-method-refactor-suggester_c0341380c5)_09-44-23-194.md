error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4511.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4511.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4511.java
text:
```scala
s@@etTitle("Columba v" + org.columba.core.main.MainInterface.version);

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

package org.columba.mail.gui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.columba.core.config.ViewItem;
import org.columba.core.config.WindowItem;
import org.columba.core.gui.statusbar.StatusBar;
import org.columba.core.gui.util.ImageLoader;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.attachment.AttachmentView;
import org.columba.mail.gui.composer.HeaderView;
import org.columba.mail.gui.frame.util.SplitPane;
import org.columba.mail.gui.message.MessageView;
import org.columba.mail.gui.table.FilterToolbar;
import org.columba.mail.gui.table.TableView;
import org.columba.mail.gui.tree.TreeView;
import org.columba.mail.gui.tree.util.FolderInfoPanel;

public class FrameView extends JFrame {
	private MailToolBar toolbar;

	private StatusBar statusBar;
	public JSplitPane mainSplitPane;
	public SplitPane rightSplitPane;

	private JPanel pane;

	private FolderInfoPanel folderInfoPanel;

	public ResourceBundle guiLabels;

	private JPanel tablePanel;

	FilterToolbar filterToolbar;
	HeaderView header;

	public FrameView() {
		//MainInterface.mainFrame = this;
		this.setIconImage(
			ImageLoader.getImageIcon("ColumbaIcon.png").getImage());

		//changeToolbars();
		//MainInterface.frameModel.register(this);

	}

	public void maximize() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);

		// FIXME: this works only with JDK1.4
		// has to be added with org.columba.core.util.Compatibility-class
		//setExtendedState(MAXIMIZED_BOTH);

	}

	public void showAttachmentViewer() {
		rightSplitPane.showAttachmentViewer();
	}

	public void hideAttachmentViewer() {
		rightSplitPane.hideAttachmentViewer();
	}

	public void setFolderInfoPanel(FolderInfoPanel f) {
		this.folderInfoPanel = f;
	}

	public void init(
		TreeView tree,
		TableView table,
		FilterToolbar filterToolbar,
		MessageView message,
		AttachmentView attachment,
		StatusBar statusBar) {

		this.filterToolbar = filterToolbar;

		this.getContentPane().setLayout(new BorderLayout());
		JPanel panel = (JPanel) this.getContentPane();

		setTitle("Columba v" + org.columba.main.MainInterface.version);

		this.getContentPane().add(statusBar, BorderLayout.SOUTH);

		this.statusBar = statusBar;

		mainSplitPane = new JSplitPane();
		//mainSplitPane.setDividerSize(5);

		//mainSplitPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

		this.getContentPane().add(mainSplitPane, BorderLayout.CENTER);

		mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		mainSplitPane.add(new JScrollPane(tree), JSplitPane.LEFT);

		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		//messagePanel.add( header, BorderLayout.NORTH );
		messagePanel.add(message, BorderLayout.CENTER);

		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());

		ViewItem item = MailConfig.getMainFrameOptionsConfig().getViewItem();
		System.out.println(
			"toolbar=" + item.getBoolean("toolbars", "show_filter"));

		if (item.getBoolean("toolbars", "show_filter") == true)
			tablePanel.add(filterToolbar, BorderLayout.NORTH);

		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.getViewport().setBackground(Color.white);
		tablePanel.add(tableScrollPane, BorderLayout.CENTER);
		rightSplitPane =
			new SplitPane(
				tablePanel,
				messagePanel,
				new JScrollPane(attachment));

		mainSplitPane.add(rightSplitPane, JSplitPane.RIGHT);

		pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		// same as menu

		if (item.getBoolean("toolbars", "show_main") == true)
			pane.add(toolbar);

		if (item.getBoolean("toolbars", "show_folderinfo") == true)
			pane.add(folderInfoPanel);

		getContentPane().add(pane, BorderLayout.NORTH);

		int count = MailConfig.getAccountList().count();

		if (count == 0) {
			pack();
			rightSplitPane.setDividerLocation(150);
		} else {
			mainSplitPane.setDividerLocation(
				item.getInteger("splitpanes", "main"));

			rightSplitPane.setDividerLocation(
				item.getInteger("splitpanes", "header"));
		}

	}

	public void setToolBar(MailToolBar toolBar) {
		this.toolbar = toolBar;
	}

	public void hideToolbar(boolean b) {
		pane.remove(toolbar);

		validate();
		repaint();

	}

	public void showToolbar(boolean b) {

		if (b) {
			pane.removeAll();
			pane.add(toolbar);
			pane.add(folderInfoPanel);

			validate();
			repaint();
		} else {

			pane.add(toolbar);
			validate();
			repaint();
		}

	}

	public void hideFolderInfo(boolean b) {

		pane.remove(folderInfoPanel);
		validate();
		repaint();

	}

	public void showFolderInfo(boolean b) {

		if (b) {
			pane.removeAll();
			pane.add(toolbar);
			pane.add(folderInfoPanel);

			validate();
			repaint();
		} else {

			pane.add(folderInfoPanel);

			validate();
			repaint();
		}

	}

	public void showFilterToolbar() {
		tablePanel.add(filterToolbar, BorderLayout.NORTH);
		tablePanel.validate();
		repaint();
	}

	public void hideFilterToolbar() {
		tablePanel.remove(filterToolbar);
		tablePanel.validate();
		repaint();
	}

	public void saveWindowPosition(ViewItem viewItem) {

		java.awt.Dimension d = getSize();

		WindowItem item = viewItem.getWindowItem();

		item.set("x", 0);
		item.set("y", 0);
		item.set("width", d.width);
		item.set("height", d.height);

		viewItem.set("splitpanes", "main", mainSplitPane.getDividerLocation());
		viewItem.set(
			"splitpanes",
			"header",
			rightSplitPane.getDividerLocation());

	}

	public void loadWindowPosition(ViewItem viewItem) {
		int x = viewItem.getInteger("window", "width");
		int y = viewItem.getInteger("window", "height");
		Dimension dim = new Dimension(x, y);
		setSize(dim);
		
		validate();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4511.java