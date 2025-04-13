error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14514.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14514.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14514.java
text:
```scala
O@@utputStream out, String message);

/*
 * Copyright  2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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

package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.types.Permissions;

/**
 * Handles the portions of {@link JUnitTask} which need to directly access
 * actual JUnit classes, so that junit.jar need not be on Ant's startup classpath.
 * Neither JUnitTask.java nor JUnitTaskMirror.java nor their transitive static
 * deps may import any junit.** classes!
 * Specifically, need to not refer to
 * - JUnitResultFormatter or its subclasses
 * - JUnitVersionHelper
 * - JUnitTestRunner
 * Cf. {@link JUnitTask.SplitLoader#isSplit}
 * Public only to permit access from classes in this package; do not use directly.
 * 
 * @author refactoring tricks by Jesse Glick, real code by others
 * @since 1.7
 * @see "bug #38799"
 */
public interface JUnitTaskMirror {
    
    void addVmExit(JUnitTest test, JUnitResultFormatterMirror formatter,
            OutputStream out, final String message);
    
    JUnitTestRunnerMirror newJUnitTestRunner(JUnitTest test, boolean haltOnError,
            boolean filterTrace, boolean haltOnFailure, boolean showOutput,
            boolean logTestListenerEvents, AntClassLoader classLoader);

    SummaryJUnitResultFormatterMirror newSummaryJUnitResultFormatter();

    public interface JUnitResultFormatterMirror {

        void setOutput(OutputStream outputStream);

    }

    public interface SummaryJUnitResultFormatterMirror extends JUnitResultFormatterMirror {

        void setWithOutAndErr(boolean value);

    }

    public interface JUnitTestRunnerMirror {

        /**
         * Used in formatter arguments as a placeholder for the basename
         * of the output file (which gets replaced by a test specific
         * output file name later).
         *
         * @since Ant 1.6.3
         */
        String IGNORED_FILE_NAME = "IGNORETHIS";

        /**
         * No problems with this test.
         */
        int SUCCESS = 0;
    
        /**
         * Some tests failed.
         */
        int FAILURES = 1;

        /**
         * An error occurred.
         */
        int ERRORS = 2;

        void setPermissions(Permissions perm);
    
        void run();

        void addFormatter(JUnitResultFormatterMirror formatter);
    
        int getRetCode();
    
        void handleErrorFlush(String output);
    
        void handleErrorOutput(String output);
    
        void handleOutput(String output);
    
        int handleInput(byte[] buffer, int offset, int length) throws IOException;
    
        void handleFlush(String output);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14514.java