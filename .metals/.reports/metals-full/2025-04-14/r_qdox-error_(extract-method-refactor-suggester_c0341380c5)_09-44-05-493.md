error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/140.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/140.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/140.java
text:
```scala
b@@oolean remember = item.getBooleanWithDefault("remember_last_selection", true);

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
package org.columba.mail.folderoptions;

import org.columba.core.config.DefaultItem;
import org.columba.core.config.IDefaultItem;
import org.columba.core.xml.XmlElement;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.frame.TableViewOwner;
import org.columba.mail.gui.table.IMessageNode;
import org.columba.mail.gui.table.TableController;
import org.columba.mail.gui.table.TableView;
import org.columba.mail.gui.table.model.HeaderTableModel;

/**
 * Handles selecting message after folder selection changes.
 * <p>
 * This implementation remembers the the selected message, and tries to reselect
 * it again. As default fall back it selects the first or last message,
 * depending on the sorting order.
 * 
 * @author fdietz, waffel
 */
public class SelectionOptionsPlugin extends AbstractFolderOptionsPlugin {

	/**
	 * Constructor
	 * 
	 * @param mediator
	 *            mail frame mediator
	 */
	public SelectionOptionsPlugin(MailFrameMediator mediator) {
		super("selection", "SelectionOptions", mediator);
	}

	/**
	 * 
	 * Save currently selected message.
	 * 
	 * @see org.columba.mail.folderoptions.AbstractFolderOptionsPlugin#saveOptionsToXml(IMailbox)
	 */
	public void saveOptionsToXml(IMailbox folder) {
		XmlElement parent = getConfigNode(folder);
		IDefaultItem item = new DefaultItem(parent);

		TableController tableController = ((TableController)((TableViewOwner) getMediator()).getTableController());

		if (tableController.getSelectedNodes() == null)
			return;

		if (tableController.getSelectedNodes().length == 0)
			return;

		IMessageNode node = tableController.getSelectedNodes()[0];
		if ((node != null) && (folder != null))
			folder.setLastSelection(node.getUid());
	}

	/**
	 * Restore selection.
	 * 
	 * @see org.columba.mail.folderoptions.AbstractFolderOptionsPlugin#loadOptionsFromXml(IMailbox)
	 */
	public void loadOptionsFromXml(IMailbox folder) {
		XmlElement parent = getConfigNode(folder);
		IDefaultItem item = new DefaultItem(parent);

		TableController tableController = ((TableController)((TableViewOwner) getMediator()).getTableController());

		TableView view = tableController.getView();

		// should we re-use the last remembered selection?
		boolean remember = item.getBoolean("remember_last_selection", true);

		// sorting order
		boolean ascending = tableController.getTableModelSorter()
				.getSortingOrder();

		// row count
		int row = view.getTree().getRowCount();

		// row count == 0 --> empty table
		if (row == 0) {
			//  clear message viewer
			///tableController.valueChanged(new ListSelectionEvent(this,-1,-1,false));
			tableController.clearSelection();
			return;
		}

		// if the last selection for the current folder is null, then we show
		// the
		// first/last message in the table and scroll to it.
		if ((!remember) || (folder.getLastSelection() == null)) {
			// changing the selection to the first/last row based on ascending
			// state
			Object uid = null;

			if (ascending) {
				uid = view.selectLastRow();
			} else {
				uid = view.selectFirstRow();
			}

			// no messages in this folder
			if (uid == null) {
				return;
			}

		} else {

			// if a lastSelection for this folder is set
			// getting the last selected uid
			Object[] lastSelUids = { folder.getLastSelection() };

			// no messages in this folder
			if (lastSelUids[0] == null) {
				return;
			}

			Object uid = lastSelUids[0];

			// this message doesn't exit in this folder anymore
			if (((HeaderTableModel)tableController.getHeaderTableModel()).getMessageNode(uid) == null) {

				if (ascending) {
					uid = view.selectLastRow();
				} else {
					uid = view.selectFirstRow();
				}

			} else {

				// selecting the message
				tableController.setSelected(new Object[] { uid });
			}

		}
	}

	/**
	 * @see org.columba.mail.folderoptions.AbstractFolderOptionsPlugin#createDefaultElement()
	 */
	public XmlElement createDefaultElement(boolean global) {
		XmlElement parent = super.createDefaultElement(global);
		parent.addAttribute("remember_last_selection", "true");

		return parent;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/140.java