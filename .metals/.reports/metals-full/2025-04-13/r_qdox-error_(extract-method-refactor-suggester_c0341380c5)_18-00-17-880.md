error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7474.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7474.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7474.java
text:
```scala
M@@ap<String, String> map = new HashMap<>();

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.test.junit.listeners;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.test.junit.annotations.TestLogging;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link RunListener} that allows to change the log level for a specific test method.
 * When a test method is annotated with the {@link org.elasticsearch.test.junit.annotations.TestLogging} annotation, the level for the specified loggers
 * will be internally saved before the test method execution and overridden with the specified ones.
 * At the end of the test method execution the original loggers levels will be restored.
 *
 * Note: This class is not thread-safe. Given the static nature of the logging api, it assumes that tests
 * are never run concurrently in the same jvm. For the very same reason no synchronization has been implemented
 * regarding the save/restore process of the original loggers levels.
 */
public class LoggingListener extends RunListener {

    private Map<String, String> previousLoggingMap;
    private Map<String, String> previousClassLoggingMap;
    private Map<String, String> previousPackageLoggingMap;

    @Override
    public void testRunStarted(Description description) throws Exception {
        previousPackageLoggingMap = processTestLogging( description.getTestClass().getPackage().getAnnotation(TestLogging.class));
        previousClassLoggingMap = processTestLogging(description.getAnnotation(TestLogging.class));
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        previousClassLoggingMap = reset(previousClassLoggingMap);
        previousPackageLoggingMap = reset(previousPackageLoggingMap);
    }

    @Override
    public void testStarted(Description description) throws Exception {
        final TestLogging testLogging = description.getAnnotation(TestLogging.class);
        previousLoggingMap = processTestLogging(testLogging);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        previousLoggingMap = reset(previousLoggingMap);
    }

    private static ESLogger resolveLogger(String loggerName) {
        if (loggerName.equalsIgnoreCase("_root")) {
            return ESLoggerFactory.getRootLogger();
        }
        return Loggers.getLogger(loggerName);
    }

    private Map<String, String> processTestLogging(TestLogging testLogging) {
        if (testLogging == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        final String[] loggersAndLevels = testLogging.value().split(",");
        for (String loggerAndLevel : loggersAndLevels) {
            String[] loggerAndLevelArray = loggerAndLevel.split(":");
            if (loggerAndLevelArray.length >=2) {
                String loggerName = loggerAndLevelArray[0];
                String level = loggerAndLevelArray[1];
                ESLogger esLogger = resolveLogger(loggerName);
                map.put(loggerName, esLogger.getLevel());
                esLogger.setLevel(level);
            }
        }
        return map;
    }

    private Map<String, String> reset(Map<String, String> map) {
        if (map != null) {
            for (Map.Entry<String, String> previousLogger : map.entrySet()) {
                ESLogger esLogger = resolveLogger(previousLogger.getKey());
                esLogger.setLevel(previousLogger.getValue());
            }
        }
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7474.java