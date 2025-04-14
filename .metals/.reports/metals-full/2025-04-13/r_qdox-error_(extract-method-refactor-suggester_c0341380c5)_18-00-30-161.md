error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9848.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9848.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9848.java
text:
```scala
O@@bject[] uids = inboxFolder.getUids();

/*
 * Created on 06.04.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.columba.mail.gui.messageframe;

import org.columba.core.config.ViewItem;
import org.columba.core.gui.frame.AbstractFrameView;
import org.columba.core.main.MainInterface;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.Folder;
import org.columba.mail.gui.attachment.AttachmentSelectionHandler;
import org.columba.mail.gui.frame.AbstractMailFrameController;
import org.columba.mail.gui.message.command.ViewMessageCommand;

/**
 * @author frd
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class MessageFrameController extends AbstractMailFrameController {

	FolderCommandReference[] treeReference;
	FolderCommandReference[] tableReference;

	FixedTableSelectionHandler tableSelectionHandler;
	
	/**
	 * @param viewItem
	 */
	public MessageFrameController() {
		super(
			"MessageFrame",
			new ViewItem(
				MailConfig.get("options").getElement(
					"/options/gui/messageframe/view")));

		getView().loadWindowPosition();

		getView().setVisible(true);
	}

	protected void init() {
		super.init();

		tableSelectionHandler = new FixedTableSelectionHandler(tableReference);
		getSelectionManager().addSelectionHandler(tableSelectionHandler);

		getSelectionManager().addSelectionHandler(
			new AttachmentSelectionHandler(attachmentController.getView()));
	}

	public void selectInbox() {
		Folder inboxFolder = (Folder) MainInterface.treeModel.getFolder(101);
		try {

			Object[] uids = inboxFolder.getUids(null);
			if (uids.length > 0) {
				Object uid = uids[0];

				Object[] newUids = new Object[1];
				newUids[0] = uid;

				FolderCommandReference[] r = new FolderCommandReference[1];
				r[0] = new FolderCommandReference(inboxFolder, newUids);

				// set tree and table references
				treeReference = new FolderCommandReference[1];
				treeReference[0] = new FolderCommandReference(inboxFolder);

				tableReference = new FolderCommandReference[1];
				tableReference[0] =
					new FolderCommandReference(inboxFolder, newUids);

				// FIXME
				/*
				getSelectionManager().getHandler("mail.table").setSelection(r);
				*/

				MainInterface.processor.addOp(new ViewMessageCommand(this, r));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.frame.AbstractFrameController#createView()
	 */
	public AbstractFrameView createView() {
		MessageFrameView view = new MessageFrameView(this);

		/*
		view.setFolderInfoPanel(folderInfoPanel);
		*/

		view.init(messageController.getView(), statusBar);

		view.pack();

		view.setVisible(true);

		return view;
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.frame.AbstractFrameController#close()
	 */
	public void close() {
		view.saveWindowPosition();

		view.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.frame.AbstractFrameController#saveAndClose()
	 */
	public void saveAndClose() {

		super.saveAndClose();
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.frame.AbstractFrameController#initInternActions()
	 */
	protected void initInternActions() {

	}

	/* (non-Javadoc)
	 * @see org.columba.mail.gui.frame.MailFrameInterface#getTableSelection()
	 */
	public FolderCommandReference[] getTableSelection() {
		return tableReference;
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.gui.frame.MailFrameInterface#getTreeSelection()
	 */
	public FolderCommandReference[] getTreeSelection() {
		return treeReference;
	}

	/**
	 * @param references
	 */
	public void setTreeSelection(FolderCommandReference[] references) {
		treeReference = references;
	}

	/**
	 * @param references
	 */
	public void setTableSelection(FolderCommandReference[] references) {
		tableReference = references;
		
		tableSelectionHandler.setSelection(tableReference);
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.gui.frame.AbstractMailFrameController#hasTable()
	 */
	public boolean hasTable() {
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9848.java