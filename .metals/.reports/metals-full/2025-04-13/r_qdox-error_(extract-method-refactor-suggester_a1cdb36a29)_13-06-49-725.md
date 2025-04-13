error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6208.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6208.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6208.java
text:
```scala
F@@ileServer.getFileServer().setBaseForScript(f);

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

package org.apache.jmeter.report.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.tree.TreePath;

import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.apache.jmeter.gui.ReportGuiPackage;
import org.apache.jmeter.gui.action.Command;
import org.apache.jmeter.gui.util.ReportFileDialoger;
import org.apache.jmeter.report.gui.tree.ReportTreeNode;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testelement.ReportPlan;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import com.thoughtworks.xstream.XStream;

public class ReportLoad implements Command {
    private static final Logger log = LoggingManager.getLoggerForClass();

    XStream loadService = new XStream();

    private static final Set<String> commands = new HashSet<String>();
    static {
        commands.add("open");
        commands.add("merge");
    }

    public ReportLoad() {
        super();
    }

    public Set<String> getActionNames() {
        return commands;
    }

    public void doAction(ActionEvent e) {
        boolean merging = e.getActionCommand().equals("merge");

        if (!merging) {
            ReportActionRouter.getInstance().doActionNow(
                    new ActionEvent(e.getSource(), e.getID(), "close"));
        }

        JFileChooser chooser = ReportFileDialoger
                .promptToOpenFile(new String[] { ".jmr" });
        if (chooser == null) {
            return;
        }
        boolean isTestPlan = false;
        InputStream reader = null;
        File f = null;
        try {
            f = chooser.getSelectedFile();
            if (f != null) {
                if (merging) {
                    log.info("Merging file: " + f);
                } else {
                    log.info("Loading file: " + f);
                    FileServer.getFileServer().setBasedir(f.getAbsolutePath());
                }
                reader = new FileInputStream(f);
                HashTree tree = SaveService.loadTree(reader);
                isTestPlan = insertLoadedTree(e.getID(), tree);
            }
        } catch (NoClassDefFoundError ex) // Allow for missing optional jars
        {
            String msg = ex.getMessage();
            if (msg == null) {
                msg = "Missing jar file - see log for details";
                log.warn("Missing jar file", ex);
            }
            JMeterUtils.reportErrorToUser(msg);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg == null) {
                msg = "Unexpected error - see log for details";
                log.warn("Unexpected error", ex);
            }
            JMeterUtils.reportErrorToUser(msg);
        } finally {
            JOrphanUtils.closeQuietly(reader);
            ReportGuiPackage.getInstance().updateCurrentGui();
            ReportGuiPackage.getInstance().getMainFrame().repaint();
        }
        // don't change name if merging
        if (!merging && isTestPlan && f != null) {
            ReportGuiPackage.getInstance().setReportPlanFile(f.getAbsolutePath());
        }
    }

    /**
     * Returns a boolean indicating whether the loaded tree was a full test plan
     */
    public boolean insertLoadedTree(int id, HashTree tree) throws Exception,
            IllegalUserActionException {
        // convertTree(tree);
        if (tree == null) {
            throw new Exception("Error in TestPlan - see log file");
        }
        boolean isTestPlan = tree.getArray()[0] instanceof ReportPlan;
        HashTree newTree = ReportGuiPackage.getInstance().addSubTree(tree);
        ReportGuiPackage.getInstance().updateCurrentGui();
        ReportGuiPackage.getInstance().getMainFrame().getTree()
                .setSelectionPath(
                        new TreePath(((ReportTreeNode) newTree.getArray()[0])
                                .getPath()));
        tree = ReportGuiPackage.getInstance().getCurrentSubTree();
        ReportActionRouter.getInstance().actionPerformed(
                new ActionEvent(tree.get(tree.getArray()[tree.size() - 1]), id,
                        ReportCheckDirty.SUB_TREE_LOADED));

        return isTestPlan;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6208.java