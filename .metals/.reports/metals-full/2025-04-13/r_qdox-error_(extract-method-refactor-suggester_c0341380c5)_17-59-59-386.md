error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/370.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/370.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/370.java
text:
```scala
b@@ind(IndexerSettings.class).toInstance(new IndexerSettings(globalSettings, settings));

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
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

package org.elasticsearch.indexer;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.inject.Modules;
import org.elasticsearch.common.inject.SpawnModules;
import org.elasticsearch.common.settings.NoClassSettingsException;
import org.elasticsearch.common.settings.Settings;

import java.util.Map;

import static org.elasticsearch.common.Strings.*;

/**
 * @author kimchy (shay.banon)
 */
public class IndexerModule extends AbstractModule implements SpawnModules {

    private IndexerName indexerName;

    private final Settings globalSettings;

    private final Map<String, Object> settings;

    public IndexerModule(IndexerName indexerName, Map<String, Object> settings, Settings globalSettings) {
        this.indexerName = indexerName;
        this.globalSettings = globalSettings;
        this.settings = settings;
    }

    @Override public Iterable<? extends Module> spawnModules() {
        return ImmutableList.of(Modules.createModule(loadTypeModule(indexerName.type(), "org.elasticsearch.indexer.", "IndexerModule"), globalSettings));
    }

    @Override protected void configure() {
        bind(Map.class).annotatedWith(IndexerSettings.class).toInstance(settings);
    }

    private Class<? extends Module> loadTypeModule(String type, String prefixPackage, String suffixClassName) {
        String fullClassName = type;
        try {
            return (Class<? extends Module>) globalSettings.getClassLoader().loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            fullClassName = prefixPackage + Strings.capitalize(toCamelCase(type)) + suffixClassName;
            try {
                return (Class<? extends Module>) globalSettings.getClassLoader().loadClass(fullClassName);
            } catch (ClassNotFoundException e1) {
                fullClassName = prefixPackage + toCamelCase(type) + "." + Strings.capitalize(toCamelCase(type)) + suffixClassName;
                try {
                    return (Class<? extends Module>) globalSettings.getClassLoader().loadClass(fullClassName);
                } catch (ClassNotFoundException e2) {
                    fullClassName = prefixPackage + toCamelCase(type).toLowerCase() + "." + Strings.capitalize(toCamelCase(type)) + suffixClassName;
                    try {
                        return (Class<? extends Module>) globalSettings.getClassLoader().loadClass(fullClassName);
                    } catch (ClassNotFoundException e3) {
                        throw new NoClassSettingsException("Failed to load class with value [" + type + "]", e);
                    }
                }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/370.java