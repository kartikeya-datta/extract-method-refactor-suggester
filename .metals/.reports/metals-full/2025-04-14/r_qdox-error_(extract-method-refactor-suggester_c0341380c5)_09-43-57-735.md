error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1685.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1685.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1685.java
text:
```scala
r@@eturn ESLoggerFactory.getLogger(getLoggerName(s));

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.elasticsearch.common.logging;

import com.google.common.collect.Lists;
import org.elasticsearch.common.Classes;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.river.RiverName;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;

/**
 * A set of utilities around Logging.
 *
 *
 */
public class Loggers {

    private final static String commonPrefix = System.getProperty("es.logger.prefix", "org.elasticsearch.");

    public static final String SPACE = " ";

    private static boolean consoleLoggingEnabled = true;

    public static void disableConsoleLogging() {
        consoleLoggingEnabled = false;
    }

    public static void enableConsoleLogging() {
        consoleLoggingEnabled = true;
    }

    public static boolean consoleLoggingEnabled() {
        return consoleLoggingEnabled;
    }

    public static ESLogger getLogger(Class clazz, Settings settings, ShardId shardId, String... prefixes) {
        return getLogger(clazz, settings, shardId.index(), Lists.asList(Integer.toString(shardId.id()), prefixes).toArray(new String[0]));
    }

    public static ESLogger getLogger(Class clazz, Settings settings, Index index, String... prefixes) {
        return getLogger(clazz, settings, Lists.asList(SPACE, index.name(), prefixes).toArray(new String[0]));
    }

    public static ESLogger getLogger(Class clazz, Settings settings, RiverName riverName, String... prefixes) {
        List<String> l = Lists.newArrayList();
        l.add(SPACE);
        l.add(riverName.type());
        l.add(riverName.name());
        l.addAll(Lists.newArrayList(prefixes));
        return getLogger(clazz, settings, l.toArray(new String[l.size()]));
    }

    public static ESLogger getLogger(Class clazz, Settings settings, String... prefixes) {
        return getLogger(buildClassLoggerName(clazz), settings, prefixes);
    }

    public static ESLogger getLogger(String loggerName, Settings settings, String... prefixes) {
        List<String> prefixesList = newArrayList();
        if (settings.getAsBoolean("logger.logHostAddress", false)) {
            try {
                prefixesList.add(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                // ignore
            }
        }
        if (settings.getAsBoolean("logger.logHostName", false)) {
            try {
                prefixesList.add(InetAddress.getLocalHost().getHostName());
            } catch (UnknownHostException e) {
                // ignore
            }
        }
        String name = settings.get("name");
        if (name != null) {
            prefixesList.add(name);
        }
        if (prefixes != null && prefixes.length > 0) {
            prefixesList.addAll(asList(prefixes));
        }
        return getLogger(getLoggerName(loggerName), prefixesList.toArray(new String[prefixesList.size()]));
    }

    public static ESLogger getLogger(ESLogger parentLogger, String s) {
        return ESLoggerFactory.getLogger(parentLogger.getPrefix(), getLoggerName(parentLogger.getName() + s));
    }

    public static ESLogger getLogger(String s) {
        return ESLoggerFactory.getLogger(s);
    }

    public static ESLogger getLogger(Class clazz) {
        return ESLoggerFactory.getLogger(getLoggerName(buildClassLoggerName(clazz)));
    }

    public static ESLogger getLogger(Class clazz, String... prefixes) {
        return getLogger(buildClassLoggerName(clazz), prefixes);
    }

    public static ESLogger getLogger(String name, String... prefixes) {
        String prefix = null;
        if (prefixes != null && prefixes.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String prefixX : prefixes) {
                if (prefixX != null) {
                    if (prefixX.equals(SPACE)) {
                        sb.append(" ");
                    } else {
                        sb.append("[").append(prefixX).append("]");
                    }
                }
            }
            if (sb.length() > 0) {
                sb.append(" ");
                prefix = sb.toString();
            }
        }
        return ESLoggerFactory.getLogger(prefix, getLoggerName(name));
    }

    private static String buildClassLoggerName(Class clazz) {
        String name = clazz.getName();
        if (name.startsWith("org.elasticsearch.")) {
            name = Classes.getPackageName(clazz);
        }
        return name;
    }

    private static String getLoggerName(String name) {
        if (name.startsWith("org.elasticsearch.")) {
            name = name.substring("org.elasticsearch.".length());
        }
        return commonPrefix + name;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1685.java