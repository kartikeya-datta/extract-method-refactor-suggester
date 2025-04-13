error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[20,1]

error in qdox parser
file content:
```java
offset: 856
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3736.java
text:
```scala
public final class DelegateElement {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
p@@ackage org.apache.tools.ant.taskdefs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;

/**
 * This task is designed to allow the user to install a different
 * PropertyHelper on the current Project. This task also allows the
 * installation of PropertyHelper delegates on either the newly installed
 * or existing PropertyHelper.
 * @since Ant 1.8
 */
public class PropertyHelperTask extends Task {
    /**
     * Nested delegate for refid usage.
     */
    public class DelegateElement {
        private String refid;

        private DelegateElement() {
        }

        /**
         * Get the refid.
         * @return String
         */
        public String getRefid() {
            return refid;
        }

        /**
         * Set the refid.
         * @param refid the String to set
         */
        public void setRefid(String refid) {
            this.refid = refid;
        }

        private PropertyHelper.Delegate resolve() {
            if (refid == null) {
                throw new BuildException("refid required for generic delegate");
            }
            return (PropertyHelper.Delegate) getProject().getReference(refid);
        }
    }

    private PropertyHelper propertyHelper;
    private List delegates;

    /**
     * Add a new PropertyHelper to be set on the Project.
     * @param propertyHelper the PropertyHelper to set.
     */
    public synchronized void addConfigured(PropertyHelper propertyHelper) {
        if (this.propertyHelper != null) {
            throw new BuildException("Only one PropertyHelper can be installed");
        }
        this.propertyHelper = propertyHelper;
    }

    /**
     * Add a PropertyHelper delegate to the existing or new PropertyHelper.
     * @param delegate the delegate to add.
     */
    public synchronized void addConfigured(PropertyHelper.Delegate delegate) {
        getAddDelegateList().add(delegate);
    }

    /**
     * Add a nested &lt;delegate refid="foo" /&gt; element.
     * @return DelegateElement
     */
    public DelegateElement createDelegate() {
        DelegateElement result = new DelegateElement();
        getAddDelegateList().add(result);
        return result;
    }

    /**
     * Execute the task.
     * @throws BuildException on error.
     */
    public void execute() throws BuildException {
        if (getProject() == null) {
            throw new BuildException("Project instance not set");
        }
        if (propertyHelper == null && delegates == null) {
            throw new BuildException("Either a new PropertyHelper"
                    + " or one or more PropertyHelper delegates are required");
        }
        PropertyHelper ph = propertyHelper;
        if (ph == null) {
            ph = PropertyHelper.getPropertyHelper(getProject());
        } else {
            ph = propertyHelper;
        }
        synchronized (ph) {
            if (delegates != null) {
                for (Iterator iter = delegates.iterator(); iter.hasNext();) {
                    Object o = iter.next();
                    PropertyHelper.Delegate delegate = o instanceof DelegateElement
                            ? ((DelegateElement) o).resolve() : (PropertyHelper.Delegate) o;
                    log("Adding PropertyHelper delegate " + delegate, Project.MSG_DEBUG);
                    ph.add(delegate);
                }
            }
        }
        if (propertyHelper != null) {
            log("Installing PropertyHelper " + propertyHelper, Project.MSG_DEBUG);
            // TODO copy existing properties to new PH?
            getProject().addReference(MagicNames.REFID_PROPERTY_HELPER, propertyHelper);
        }
    }

    private synchronized List getAddDelegateList() {
        if (delegates == null) {
            delegates = new ArrayList();
        }
        return delegates;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3736.java