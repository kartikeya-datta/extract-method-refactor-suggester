error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11003.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11003.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11003.java
text:
```scala
d@@ialog.setLocationRelativeTo(GuiMain.getMainWindow());

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.cli.gui;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.text.JTextComponent;
import org.jboss.as.cli.gui.ManagementModelNode.UserObject;
import org.jboss.dmr.ModelNode;

/**
 * JPopupMenu that selects the available operations for a node address.
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2012 Red Hat Inc.
 */
public class OperationMenu extends JPopupMenu {
    private static final String[] genericOps = {"add", "read-operation-description", "read-resource-description", "read-operation-names"};
    private static final List<String> genericOpList = Arrays.asList(genericOps);
    private static final String[] leafOps = {"write-attribute", "undefine-attribute"};
    private static final List<String> leafOpList = Arrays.asList(leafOps);

    private CommandExecutor executor;
    private JTree invoker;

    public OperationMenu(CommandExecutor executor, JTree invoker) {
        this.executor = executor;
        this.invoker = invoker;
        setLightWeightPopupEnabled(true);
        setOpaque(true);
    }

    /**
     * Show the OperationMenu based on the selected node.
     * @param node The selected node.
     * @param x The x position of the selection.
     * @param y The y position of the selection.
     */
    public void show(ManagementModelNode node, int x, int y) {
        removeAll();
        String addressPath = node.addressPath();
        try {
            ModelNode  opNames = executor.doCommand(addressPath + ":read-operation-names");
            if (opNames.get("outcome").asString().equals("failed")) return;

            for (ModelNode name : opNames.get("result").asList()) {
                String strName = name.asString();

                // filter operations
                if (node.isGeneric() && !genericOpList.contains(strName)) continue;
                if (node.isLeaf() && !leafOpList.contains(strName)) continue;
                if (!node.isGeneric() && !node.isLeaf() && strName.equals("add")) continue;

                ModelNode opDescription = getResourceDescription(addressPath, strName);
                add(new OperationAction(node, strName, opDescription));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.show(invoker, x, y);
    }

    private ModelNode getResourceDescription(String addressPath, String name) {
        try {
            return executor.doCommand(addressPath + ":read-operation-description(name=\"" + name + "\")");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Action for a menu selection.  For operations with params, display an Operation Dialog.  For operations
     * without params, just construct the operation and set the command line.
     */
    private class OperationAction extends AbstractAction {

        private ManagementModelNode node;
        private String opName;
        private String addressPath;
        private ModelNode opDescription;
        private String strDescription; // help text

        public OperationAction(ManagementModelNode node, String opName, ModelNode opDescription) {
            super(opName);
            this.node = node;
            this.opName = opName;
            this.addressPath = node.addressPath();
            this.opDescription = opDescription;

            if (opDescription != null) {
                strDescription = opDescription.get("result", "description").asString();
                putValue(Action.SHORT_DESCRIPTION, strDescription);
            }
        }

        public void actionPerformed(ActionEvent ae) {
            JTextComponent cmdText = GuiMain.getCommandLine().getCmdText();
            ModelNode requestProperties = opDescription.get("result", "request-properties");
            if ((requestProperties == null) || (!requestProperties.isDefined()) || requestProperties.asList().isEmpty()) {
                cmdText.setText(addressPath + ":" + opName);
                cmdText.requestFocus();
                return;
            }

            if (node.isLeaf() && opName.equals("undefine-attribute")) {
                UserObject usrObj = (UserObject)node.getUserObject();
                cmdText.setText(addressPath + ":" + opName + "(name=" + usrObj.getName() + ")");
                cmdText.requestFocus();
                return;
            }

            OperationDialog dialog = new OperationDialog(node, opName, strDescription, requestProperties);
            dialog.setLocationRelativeTo(GuiMain.getFrame());
            dialog.setVisible(true);
        }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11003.java