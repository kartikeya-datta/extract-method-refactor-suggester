error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15239.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15239.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15239.java
text:
```scala
private final T@@ask task;

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
package org.apache.tools.ant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Helper class for the check of the configuration of a given task.
 * This class provides methods for making assumptions about the task configuration.
 * After collecting all violations with <tt>assert*</tt> and <tt>fail</tt>
 * methods the <tt>checkErrors</tt> will throw a BuildException with all collected
 * messages or does nothing if there wasn't any error.</p>
 *
 * <p>Example:</p>
 *
 * <pre>
 *     public class MyTask extends Task {
 *         ...
 *         public void execute() {
 *             TaskConfigurationChecker checker = TaskConfigurationChecker(this);
 *             checker.assertConfig(
 *                 srcdir != null,
 *                 "Attribute 'srcdir' must be set.
 *             );
 *             checker.assertConfig(
 *                 srcdir.exists(),
 *                 "Srcdir (" + srcdir + ") must exist."
 *             );
 *             if (someComplexCondition()) {
 *                 fail("Complex condition failed.");
 *             }
 *             checker.checkErrors();
 *         }
 *     }
 * </pre>
 *
 * @see <a href="http://martinfowler.com/eaaDev/Notification.html">Notification Pattern</a>
 */
public class TaskConfigurationChecker {

    /** List of all collected error messages. */
    private List/*<String>*/ errors = new ArrayList();

    /** Task for which the configuration should be checked. */
    private Task task;

    /**
     * Constructor.
     * @param task which task should be checked
     */
    public TaskConfigurationChecker(Task task) {
        this.task = task;
    }

    /**
     * Asserts that a condition is true.
     * @param condition     which condition to check
     * @param errormessage  errormessage to throw if a condition failed
     */
    public void assertConfig(boolean condition, String errormessage) {
        if (!condition) {
            errors.add(errormessage);
        }
    }

    /**
     * Registers an error.
     * @param errormessage the message for the registered error
     */
    public void fail(String errormessage) {
        errors.add(errormessage);
    }

    /**
     * Checks if there are any collected errors and throws a BuildException
     * with all messages if there was one or more.
     * @throws BuildException if one or more errors were registered
     */
    public void checkErrors() throws BuildException {
        if (!errors.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Configurationerror on <");
            sb.append(task.getTaskName());
            sb.append(">:");
            sb.append(System.getProperty("line.separator"));
            for (Iterator it = errors.iterator(); it.hasNext();) {
                String msg = (String) it.next();
                sb.append("- ");
                sb.append(msg);
                sb.append(System.getProperty("line.separator"));
            }
            throw new BuildException(sb.toString(), task.getLocation());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15239.java