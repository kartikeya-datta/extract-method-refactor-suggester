error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5226.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5226.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5226.java
text:
```scala
d@@ata.registerDataLookup("Location.destination", new DataLookup() {

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

import javax.swing.*;

import net.javaprog.ui.wizard.AbstractStep;
import net.javaprog.ui.wizard.DataLookup;
import net.javaprog.ui.wizard.DataModel;

import org.columba.mail.folder.Folder;
import org.columba.mail.gui.tree.util.SelectFolderDialog;
import org.columba.mail.util.MailResourceLoader;
import org.columba.core.gui.util.MultiLineLabel;
import org.columba.core.gui.util.wizard.WizardTextField;
import org.columba.core.main.MainInterface;

class LocationStep extends AbstractStep implements ActionListener {
        protected File[] sourceFiles;
        protected Folder destinationFolder;
	protected JButton sourceButton;
	protected JButton destinationButton;

	public LocationStep(DataModel data) {
		super(MailResourceLoader.getString(
                                    "dialog",
                                    "mailboximport",
                                    "location"),
                      MailResourceLoader.getString(
                                    "dialog",
                                    "mailboximport",
                                    "location_description"));
                data.registerDataLookup("Location.source", new DataLookup() {
                        public Object lookupData() {
                                return sourceFiles;
                        }
                });
                data.registerDataLookup("Location.desctination", new DataLookup() {
                        public Object lookupData() {
                                return destinationFolder;
                        }
                });
                setCanGoNext(false);
        }

        protected JComponent createComponent() {
		JComponent component = new JPanel();
		component.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
		component.setLayout(new BoxLayout(component, BoxLayout.Y_AXIS));
		component.add(new MultiLineLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "mailboximport",
                                    "location_text")));
		component.add(Box.createVerticalStrut(40));
		WizardTextField middlePanel = new WizardTextField();
		JLabel sourceLabel = new JLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "mailboximport",
                                    "source"));
		middlePanel.addLabel(sourceLabel);
		sourceButton = new JButton("...");
                sourceLabel.setLabelFor(sourceButton);
		sourceButton.addActionListener(this);
		middlePanel.addTextField(sourceButton);
		middlePanel.addExample(new JLabel());
		JLabel destinationLabel = new JLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "mailboximport",
                                    "destination"));
		middlePanel.addLabel(destinationLabel);
		destinationButton = new JButton("...");
                destinationLabel.setLabelFor(destinationButton);
		destinationButton.addActionListener(this);
		middlePanel.addTextField(destinationButton);
		middlePanel.addExample(new JLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "mailboximport",
                                    "explanation")));
		component.add(middlePanel);
                return component;
        }
        
        public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == sourceButton) {
                        JFileChooser fc = new JFileChooser();
			fc.setMultiSelectionEnabled(true);
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fc.setFileHidingEnabled(false);
			if (fc.showOpenDialog(getComponent()) == JFileChooser.APPROVE_OPTION) {
				sourceFiles = fc.getSelectedFiles();
                                if (sourceFiles.length > 1) {
                                        sourceButton.setText(sourceFiles.length + " " + 
                                                MailResourceLoader.getString(
                                                        "dialog",
                                                        "mailboximport",
                                                        "files"));
                                } else {
                                        sourceButton.setText(sourceFiles[0].getName());
                                }
                                updateCanFinish();
			}
                } else if (source == destinationButton) {
			SelectFolderDialog dialog =
				MainInterface.treeModel.getSelectFolderDialog();
			if (dialog.success()) {
				destinationFolder = dialog.getSelectedFolder();
				destinationButton.setText(destinationFolder.getTreePath());
                                updateCanFinish();
			}
                }
        }
        
        protected void updateCanFinish() {
                setCanFinish(sourceFiles != null && destinationFolder != null);
        }
        
        public void prepareRendering() {}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5226.java