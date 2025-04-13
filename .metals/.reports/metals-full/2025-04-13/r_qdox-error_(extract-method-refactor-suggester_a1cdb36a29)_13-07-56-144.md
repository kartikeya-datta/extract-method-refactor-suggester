error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/655.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/655.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/655.java
text:
```scala
p@@anel.add(messageController.getView(), BorderLayout.CENTER);

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

package org.columba.mail.gui.messageframe;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.columba.api.gui.frame.IContentPane;
import org.columba.core.config.ViewItem;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.io.DiskIO;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.frame.AbstractMailFrameController;
import org.columba.mail.gui.frame.TableViewOwner;
import org.columba.mail.gui.frame.ThreePaneMailFrameController;
import org.columba.mail.gui.table.ITableController;
import org.columba.mail.util.MailResourceLoader;

/**
 * Mail frame controller which contains a message viewer only.
 * <p>
 * Note that this frame depends on its parent frame controller for viewing
 * messages.
 * 
 * @see org.columba.mail.gui.action.NextMessageAction
 * @see org.columba.mail.gui.action.PreviousMessageAction
 * @see org.columba.mail.gui.action.NextUnreadMessageAction
 * @see org.columba.mail.gui.action.PreviousMessageAction
 * 
 * @author fdietz
 */
public class MessageFrameController extends AbstractMailFrameController
		implements TableViewOwner, IContentPane {

	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.gui.messageframe");

	private static final int MAX_SUBJECT_LENGTH = 50;

	IMailFolderCommandReference treeReference;

	IMailFolderCommandReference tableReference;

	FixedTableSelectionHandler tableSelectionHandler;

	private ThreePaneMailFrameController parentController;

	/**
	 * @param viewItem
	 */
	public MessageFrameController() {
		super(FrameManager.getInstance().createCustomViewItem("Messageframe"));

		tableSelectionHandler = new FixedTableSelectionHandler(tableReference);
		getSelectionManager().addSelectionHandler(tableSelectionHandler);

	}

	/**
	 * @param parent
	 *            parent frame controller
	 */
	public MessageFrameController(ThreePaneMailFrameController parent) {
		this();

		this.parentController = parent;

	}

	/**
	 * 
	 * @see org.columba.mail.gui.frame.MailFrameInterface#getTableSelection()
	 */
	public IMailFolderCommandReference getTableSelection() {
		return tableReference;
	}

	/**
	 * 
	 * @see org.columba.mail.gui.frame.MailFrameInterface#getTreeSelection()
	 */
	public IMailFolderCommandReference getTreeSelection() {
		return treeReference;
	}

	/**
	 * @param references
	 */
	public void setTreeSelection(IMailFolderCommandReference references) {
		treeReference = references;
	}

	/**
	 * @param references
	 */
	public void setTableSelection(IMailFolderCommandReference references) {
		tableReference = references;

		// TODO: re-enable feature, the following code violates our
		// design, accessing folders is only allowed in Command.execute()
		/*
		 * try {
		 *  // Get the subject from the cached Header AbstractMessageFolder
		 * folder = (AbstractMessageFolder) references .getSourceFolder();
		 * IColumbaHeader header = folder.getHeaderList().get(
		 * references.getUids()[0]); String subject = (String)
		 * header.get("columba.subject");
		 * 
		 * getContainer().getFrame().setTitle(subject); } catch (Exception e) {
		 * LOG.warning(e.toString()); }
		 */

		tableSelectionHandler.setSelection(tableReference);
	}

	/**
	 * @see org.columba.mail.gui.frame.TableViewOwner#getTableController()
	 */
	public ITableController getTableController() {
		if (parentController == null)
			return null;

		// pass it along to parent frame
		return parentController.getTableController();
	}

	/**
	 * @see org.columba.api.gui.frame.IContentPane#getComponent()
	 */
	public JComponent getComponent() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.add(messageController, BorderLayout.CENTER);

		ViewItem viewItem = getViewItem();

		try {
			InputStream is = DiskIO
					.getResourceStream("org/columba/mail/action/messageframe_menu.xml");

			getContainer().extendMenu(this, is);

			File configDirectory = MailConfig.getInstance()
					.getConfigDirectory();
			InputStream is2 = new FileInputStream(new File(configDirectory,
					"messageframe_toolbar.xml"));
			getContainer().extendToolbar(this, is2);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return panel;
	}

	/**
	 * @see org.columba.api.gui.frame.IFrameMediator#getString(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public String getString(String sPath, String sName, String sID) {
		return MailResourceLoader.getString(sPath, sName, sID);
	}

	/**
	 * @see org.columba.api.gui.frame.IFrameMediator#getContentPane()
	 */
	public IContentPane getContentPane() {
		return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/655.java