error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8404.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8404.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8404.java
text:
```scala
i@@mplements TableOwner {

/*
 * Created on Jun 10, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.columba.mail.gui.frame;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.columba.core.config.ViewItem;
import org.columba.core.gui.frame.AbstractFrameView;
import org.columba.core.gui.util.DialogStore;
import org.columba.core.main.MainInterface;
import org.columba.mail.gui.attachment.AttachmentSelectionHandler;
import org.columba.mail.gui.composer.HeaderController;
import org.columba.mail.gui.infopanel.FolderInfoPanel;
import org.columba.mail.gui.table.FilterToolbar;
import org.columba.mail.gui.table.TableController;
import org.columba.mail.gui.table.action.DownAction;
import org.columba.mail.gui.table.action.UpAction;
import org.columba.mail.gui.table.selection.TableSelectionHandler;
import org.columba.mail.gui.tree.TreeController;
import org.columba.mail.gui.tree.selection.TreeSelectionHandler;

/**
 *
 *  Mail frame controller which contains a tree, table and a message
 *  viewer.
 *
 *  @author fdietz
 * 
 */
public class ThreePaneMailFrameController
	extends AbstractMailFrameController
	implements TableOwnerInterface {

	public TreeController treeController;
	public TableController tableController;

	public HeaderController headerController;
	public FilterToolbar filterToolbar;

	public FolderInfoPanel folderInfoPanel;

	/**
	 * @param viewItem
	 */
	public ThreePaneMailFrameController(ViewItem viewItem) {
		super("ThreePaneMail", viewItem);

		TableUpdater.add(this);

	}

	protected void initActions() {

		tableController.getView().getInputMap().put(
			KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
			"UP");
		UpAction upAction = new UpAction(this);
		tableController.getView().getActionMap().put("UP", upAction);

		tableController.getView().getInputMap().put(
			KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
			"DOWN");
		DownAction downAction = new DownAction(this);
		tableController.getView().getActionMap().put("DOWN", downAction);
		//register the markasread timer as selection listener
		tableController
			.getMailFrameController()
			.registerTableSelectionListener(
			tableController.getMarkAsReadTimer());
	}

	public AbstractFrameView createView() {

		MailFrameView view = new MailFrameView(this);

		view.setFolderInfoPanel(folderInfoPanel);

		view.init(
			treeController.getView(),
			tableController.getView(),
			filterToolbar,
			messageController.getView(),
			statusBar);

		//view.pack();

		return view;
	}

	/**
	 * Save window properties and close the window.
	 */
	public void close() {
		tableController.saveColumnConfig();
		super.close(); // this saves view settings and closes the view
	}

	protected void init() {
		super.init();

		treeController = new TreeController(this, MainInterface.treeModel);

		tableController = new TableController(this);

		folderInfoPanel = new FolderInfoPanel();
		//treeController.getTreeSelectionManager().addFolderSelectionListener(folderInfoPanel);

		filterToolbar = new FilterToolbar(tableController);

		new DialogStore((MailFrameView) view);

		// create selection handlers
		TableSelectionHandler tableHandler =
			new TableSelectionHandler(tableController.getView());
		getSelectionManager().addSelectionHandler(tableHandler);

		TreeSelectionHandler treeHandler =
			new TreeSelectionHandler(treeController.getView());
		getSelectionManager().addSelectionHandler(treeHandler);

		getSelectionManager().addSelectionHandler(
			new AttachmentSelectionHandler(attachmentController.getView()));

		/*
		treeController.getTreeSelectionManager().registerSelectionListener(""
			tableController.getTableSelectionManager());
		*/
		
		tableController.createPopupMenu();
		treeController.createPopupMenu();
		attachmentController.createPopupMenu();

		initActions();

	}

	/* *20030831, karlpeder* Not used, close method is used instead
	public void saveAndClose() {
		tableController.saveColumnConfig();
		super.saveAndClose();
	}
	*/

	/* (non-Javadoc)
	 * @see org.columba.mail.gui.frame.AbstractMailFrameController#hasTable()
	 */
	public boolean hasTable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.gui.frame.ViewHeaderListInterface#getTableController()
	 */
	public TableController getTableController() {
		return tableController;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8404.java