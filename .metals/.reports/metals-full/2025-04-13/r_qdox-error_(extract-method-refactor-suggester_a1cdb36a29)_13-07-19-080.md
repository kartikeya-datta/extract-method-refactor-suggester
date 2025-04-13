error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4991.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4991.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4991.java
text:
```scala
c@@lone.SUB = this.SUB==null ? null : (TestElement) this.SUB.clone();

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

package org.apache.jmeter.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.control.TestFragmentController;

import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public class IncludeController extends GenericController implements ReplaceableController {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 240L;

    private static final String INCLUDE_PATH = "IncludeController.includepath"; //$NON-NLS-1$

    private static  final String prefix =
        JMeterUtils.getPropDefault(
                "includecontroller.prefix", //$NON-NLS-1$
                ""); //$NON-NLS-1$

    private HashTree SUBTREE = null;
    private TestElement SUB = null;

    /**
     * No-arg constructor
     *
     * @see java.lang.Object#Object()
     */
    public IncludeController() {
        super();
    }

    @Override
    public Object clone() {
        // TODO - fix so that this is only called once per test, instead of at every clone
        // Perhaps save previous filename, and only load if it has changed?
        this.resolveReplacementSubTree(null);
        IncludeController clone = (IncludeController) super.clone();
        clone.setIncludePath(this.getIncludePath());
        if (this.SUBTREE != null) {
            if (this.SUBTREE.keySet().size() == 1) {
                Iterator<Object> itr = this.SUBTREE.keySet().iterator();
                while (itr.hasNext()) {
                    this.SUB = (TestElement) itr.next();
                }
            }
            clone.SUBTREE = (HashTree)this.SUBTREE.clone();
            clone.SUB = (TestElement)this.SUB.clone();
        }
        return clone;
    }

    /**
     * In the event an user wants to include an external JMX test plan
     * the GUI would call this.
     * @param jmxfile
     */
    public void setIncludePath(String jmxfile) {
        this.setProperty(INCLUDE_PATH,jmxfile);
    }

    /**
     * return the JMX file path.
     * @return the JMX file path
     */
    public String getIncludePath() {
        return this.getPropertyAsString(INCLUDE_PATH);
    }

    /**
     * The way ReplaceableController works is clone is called first,
     * followed by replace(HashTree) and finally getReplacement().
     */
    public HashTree getReplacementSubTree() {
        return SUBTREE;
    }

    public TestElement getReplacementElement() {
        return SUB;
    }

    public void resolveReplacementSubTree(JMeterTreeNode context) {
        this.SUBTREE = this.loadIncludedElements();
    }

    /**
     * load the included elements using SaveService
     */
    protected HashTree loadIncludedElements() {
        // only try to load the JMX test plan if there is one
        final String includePath = getIncludePath();
        InputStream reader = null;
        HashTree tree = null;
        if (includePath != null && includePath.length() > 0) {
            try {
                String fileName=prefix+includePath;
                File file = new File(fileName);
                final String absolutePath = file.getAbsolutePath();
                log.info("loadIncludedElements -- try to load included module: "+absolutePath);
                if(!file.exists() && !file.isAbsolute()){
                    log.info("loadIncludedElements -failed for: "+absolutePath);
                    file = new File(FileServer.getFileServer().getBaseDir(), includePath);
                    log.info("loadIncludedElements -Attempting to read it from: "+absolutePath);
                    if(!file.exists()){
                        log.error("loadIncludedElements -failed for: "+absolutePath);
                        throw new IOException("loadIncludedElements -failed for: "+absolutePath);
                    }
                }
                
                reader = new FileInputStream(file);
                tree = SaveService.loadTree(reader);
                // filter the tree for a TestFragment.
                tree = getProperBranch(tree);
                removeDisabledItems(tree);
                return tree;
            } catch (NoClassDefFoundError ex) // Allow for missing optional jars
            {
                String msg = ex.getMessage();
                if (msg == null) {
                    msg = "Missing jar file - see log for details";
                }
                log.warn("Missing jar file", ex);
                JMeterUtils.reportErrorToUser(msg);
            } catch (FileNotFoundException ex) {
                String msg = ex.getMessage();
                JMeterUtils.reportErrorToUser(msg);
                log.warn(msg);
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (msg == null) {
                    msg = "Unexpected error - see log for details";
                }
                JMeterUtils.reportErrorToUser(msg);
                log.warn("Unexpected error", ex);
            }
            finally{
                JOrphanUtils.closeQuietly(reader);
            }
        }
        return tree;
    }

    private HashTree getProperBranch(HashTree tree) {
        Iterator<Object> iter = new LinkedList<Object>(tree.list()).iterator();
        while (iter.hasNext()) {
            TestElement item = (TestElement) iter.next();

            //if we found a TestPlan, then we are on our way to the TestFragment
            if (item instanceof TestPlan)
            {
                return getProperBranch(tree.getTree(item));
            }

            if (item instanceof TestFragmentController)
            {
                return tree.getTree(item);
            }
        }
        //return the tree since we didn't find a TestFragment.  This will mimic the 
        //old behavior to import an exact node.
        return tree;
    }


    private void removeDisabledItems(HashTree tree) {
        Iterator<Object> iter = new LinkedList<Object>(tree.list()).iterator();
        while (iter.hasNext()) {
            TestElement item = (TestElement) iter.next();
            if (!item.isEnabled()) {
                //log.info("Removing "+item.toString());
                tree.remove(item);
            } else {
                //log.info("Keeping "+item.toString());
                removeDisabledItems(tree.getTree(item));// Recursive call
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4991.java