error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/65.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/65.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/65.java
text:
```scala
public I@@mportWizard() throws Exception{

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
package org.columba.mail.gui.config.mailboximport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import org.columba.core.gui.util.ImageLoader;
import org.columba.core.gui.util.NotifyDialog;
import org.columba.core.gui.util.wizard.DefaultWizardDialog;
import org.columba.core.gui.util.wizard.DefaultWizardPanel;
import org.columba.core.gui.util.wizard.WizardPanelSequence;
import org.columba.core.main.MainInterface;
import org.columba.core.plugin.PluginHandlerNotFoundException;
import org.columba.mail.command.ImportFolderCommandReference;
import org.columba.mail.folder.Folder;
import org.columba.mail.folder.command.ImportMessageCommand;
import org.columba.mail.folder.mailboximport.DefaultMailboxImporter;
import org.columba.mail.gui.tree.util.SelectFolderDialog;
import org.columba.mail.plugin.ImportPluginHandler;

public class ImportWizard
	extends DefaultWizardDialog
	implements ActionListener {

	public ListPanel listPanel;
	public SourcePanel sourcePanel;
	//public ProgressPanel progressPanel;

	//private JDialog dialog;

	private File[] sourceFiles;

	private Folder destFolder;

	private Boolean cancel = Boolean.TRUE;

	protected WizardPanelSequence sequence;

	ImportPluginHandler pluginHandler;

	public ImportWizard() {
		destFolder = (Folder) MainInterface.treeModel.getFolder(101);

		DefaultWizardPanel p = getSequence().getFirstPanel();
		init(p);

		updateWindow(p);
		
	}


	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("FINISH")) {
			if (sourceFiles == null) {
				NotifyDialog dialog = new NotifyDialog();
				dialog.showDialog("You have to specify a source File!");

			} else {

				dialog.setVisible(false);

				finish();
			}
		} else if (action.equals("SOURCE")) {
			JFileChooser fc = new JFileChooser();
			fc.setMultiSelectionEnabled(true);
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fc.setFileHidingEnabled(false);
			
			int returnVal = fc.showOpenDialog(dialog);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				sourceFiles = fc.getSelectedFiles();
				//this is where a real application would open the file.
				sourcePanel.setSource(sourceFiles[0].toString());
			}

		} else if (action.equals("DESTINATION")) {

			SelectFolderDialog dialog =
				MainInterface.treeModel.getSelectFolderDialog();

			if (dialog.success()) {

				destFolder = dialog.getSelectedFolder();
				String path = destFolder.getTreePath();

				sourcePanel.setDestination(path);
			}

		}
	}

	public void finish() {

		String pluginId = listPanel.getSelection();

		pluginHandler = null;
		try {
			pluginHandler =
				(ImportPluginHandler) MainInterface.pluginManager.getHandler(
					"org.columba.mail.import");
		} catch (PluginHandlerNotFoundException ex) {
			NotifyDialog d = new NotifyDialog();
			d.showDialog(ex);
		}

		DefaultMailboxImporter importer = null;

		Object[] args = { destFolder, sourceFiles };
		try {
			importer =
				(DefaultMailboxImporter)
					((ImportPluginHandler) pluginHandler).getPlugin(
					pluginId,
					args);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		ImportFolderCommandReference[] r = new ImportFolderCommandReference[1];
		File[] files = sourceFiles;

		r[0] = new ImportFolderCommandReference(destFolder, files, importer);

		MainInterface.processor.addOp( new ImportMessageCommand(r));
		
		
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.util.wizard.DefaultWizardDialog#getSequence()
	 */
	public WizardPanelSequence getSequence() {

		if (sequence == null) {
			sequence = new WizardPanelSequence();

			listPanel =
				new ListPanel(
					dialog,
					this,
					"Import Mailbox",
					"Choose mailbox type",
					ImageLoader.getSmallImageIcon("stock_preferences.png"),
					true);

			sequence.addPanel(listPanel);

			sourcePanel =
				new SourcePanel(
					dialog,
					this,
					"Import Mailbox",
					"Choose source/destination folder",
					ImageLoader.getSmallImageIcon("stock_preferences.png"),
					true);
			sequence.addPanel(sourcePanel);
		}

		return sequence;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/65.java