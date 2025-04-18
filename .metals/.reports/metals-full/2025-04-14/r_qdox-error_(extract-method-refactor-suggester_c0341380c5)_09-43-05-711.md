error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13028.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13028.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13028.java
text:
```scala
private v@@oid startEngine() {

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
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.jmeter.JMeter;
import org.apache.jmeter.engine.JMeterEngineException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.TreeCloner;
import org.apache.jmeter.engine.util.DisabledComponentRemover;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class Start extends AbstractAction {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final Set commands = new HashSet();

    static {
        commands.add(ActionNames.ACTION_START);
        commands.add(ActionNames.ACTION_STOP);
        commands.add(ActionNames.ACTION_SHUTDOWN);
    }

    private StandardJMeterEngine engine;

    /**
     * Constructor for the Start object.
     */
    public Start() {
    }

    /**
     * Gets the ActionNames attribute of the Start object.
     *
     * @return the ActionNames value
     */
    public Set getActionNames() {
        return commands;
    }

    public void doAction(ActionEvent e) {
        if (e.getActionCommand().equals(ActionNames.ACTION_START)) {
            popupShouldSave(e);
            startEngine();
        } else if (e.getActionCommand().equals(ActionNames.ACTION_STOP)) {
            if (engine != null) {
                log.info("Stopping test");
                GuiPackage.getInstance().getMainFrame().showStoppingMessage("");
                engine.stopTest();
            }
        } else if (e.getActionCommand().equals(ActionNames.ACTION_SHUTDOWN)) {
            if (engine != null) {
                log.info("Shutting test down");
                GuiPackage.getInstance().getMainFrame().showStoppingMessage("");
                engine.askThreadsToStop();
            }
        }
    }

    protected void startEngine() {
        GuiPackage gui = GuiPackage.getInstance();
        HashTree testTree = gui.getTreeModel().getTestPlan();
        JMeter.convertSubTree(testTree);
        DisabledComponentRemover remover = new DisabledComponentRemover(testTree);
        testTree.traverse(remover);
        testTree.add(testTree.getArray()[0], gui.getMainFrame());
        log.debug("test plan before cloning is running version: "
                + ((TestPlan) testTree.getArray()[0]).isRunningVersion());
        TreeCloner cloner = new TreeCloner(false);
        testTree.traverse(cloner);
        engine = new StandardJMeterEngine();
        engine.configure(cloner.getClonedTree());
        try {
            engine.runTest();
        } catch (JMeterEngineException e) {
            JOptionPane.showMessageDialog(gui.getMainFrame(), e.getMessage(), JMeterUtils
                    .getResString("Error Occurred"), JOptionPane.ERROR_MESSAGE);
        }
        log.debug("test plan after cloning and running test is running version: "
                + ((TestPlan) testTree.getArray()[0]).isRunningVersion());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13028.java