error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13403.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13403.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13403.java
text:
```scala
i@@f (!Close.performAction(e)) return;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.gui.util.FileDialoger;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import com.thoughtworks.xstream.converters.ConversionException;

/**
 * Handles the Open (load a new file) and Merge commands.
 *
 */
public class Load implements Command {
	private static final Logger log = LoggingManager.getLoggerForClass();

	private static final boolean expandTree = JMeterUtils.getPropDefault("onload.expandtree", true); //$NON-NLS-1$
	
	private static Set commands = new HashSet();
	static {
		commands.add(ActionNames.OPEN);
		commands.add(ActionNames.MERGE);
	}

	public Load() {
		super();
	}

	public Set getActionNames() {
		return commands;
	}

	public void doAction(ActionEvent e) {
		JFileChooser chooser = FileDialoger.promptToOpenFile(new String[] { ".jmx" }); //$NON-NLS-1$
		if (chooser == null) {
			return;
		}
		InputStream reader = null;
		try {
            File f = chooser.getSelectedFile();
			if (f != null) {
                boolean isTestPlan = false;
                boolean merging = e.getActionCommand().equals(ActionNames.MERGE);

				if (merging) {
					log.info("Merging file: " + f);
				} else {
					log.info("Loading file: " + f);
                    // Close the test plan currently open
					ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.CLOSE));

					FileServer.getFileServer().setBasedir(f.getAbsolutePath());
				}
				reader = new FileInputStream(f);
				HashTree tree = SaveService.loadTree(reader);
				isTestPlan = insertLoadedTree(e.getID(), tree, merging);
                
                // don't change name if merging
                if (!merging && isTestPlan) {
                    GuiPackage.getInstance().setTestPlanFile(f.getAbsolutePath());
                }
			}
		} catch (NoClassDefFoundError ex) // Allow for missing optional jars
		{
            log.warn("Missing jar file", ex);
			String msg = ex.getMessage();
			if (msg == null) {
				msg = "Missing jar file - see log for details";
			}
			JMeterUtils.reportErrorToUser(msg);
        } catch (ConversionException ex) {
            log.warn("Could not convert file "+ex);
            JMeterUtils.reportErrorToUser(SaveService.CEtoString(ex));
		} catch (Exception ex) {
            log.warn("Unexpected error", ex);
			String msg = ex.getMessage();
			if (msg == null) {
				msg = "Unexpected error - see log for details";
			}
			JMeterUtils.reportErrorToUser(msg);
		} finally {
            JOrphanUtils.closeQuietly(reader);
			GuiPackage.getInstance().updateCurrentGui();
			GuiPackage.getInstance().getMainFrame().repaint();
		}
	}

	/**
	 * Returns a boolean indicating whether the loaded tree was a full test plan
	 */
	public boolean insertLoadedTree(int id, HashTree tree, boolean merging) throws Exception, IllegalUserActionException {
		// convertTree(tree);
		if (tree == null) {
			throw new Exception("Error in TestPlan - see log file");
		}
		boolean isTestPlan = tree.getArray()[0] instanceof TestPlan;

		// If we are loading a new test plan, initialize the tree with the testplan node we are loading
		if(isTestPlan && !merging) {
			GuiPackage.getInstance().getTreeModel().clearTestPlan((TestElement)tree.getArray()[0]);
		}

		HashTree newTree = GuiPackage.getInstance().addSubTree(tree);
		GuiPackage.getInstance().updateCurrentGui();
		GuiPackage.getInstance().getMainFrame().getTree().setSelectionPath(
				new TreePath(((JMeterTreeNode) newTree.getArray()[0]).getPath()));
		tree = GuiPackage.getInstance().getCurrentSubTree();
		// Send different event wether we are merging a test plan into another test plan,
		// or loading a testplan from scratch
		ActionEvent actionEvent = null;
		if(!merging) {
			actionEvent = new ActionEvent(tree.get(tree.getArray()[tree.size() - 1]), id, ActionNames.SUB_TREE_LOADED);
		}
		else {
			actionEvent = new ActionEvent(tree.get(tree.getArray()[tree.size() - 1]), id, ActionNames.SUB_TREE_MERGED);
		}

		ActionRouter.getInstance().actionPerformed(actionEvent);
	    if (expandTree) {
			JTree jTree = GuiPackage.getInstance().getMainFrame().getTree();
			   for(int i = 0; i < jTree.getRowCount(); i++) {
			     jTree.expandRow(i);
			   }
	    }

		return isTestPlan;
	}
    
	public boolean insertLoadedTree(int id, HashTree tree) throws Exception, IllegalUserActionException {
		return insertLoadedTree(id, tree, false);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13403.java