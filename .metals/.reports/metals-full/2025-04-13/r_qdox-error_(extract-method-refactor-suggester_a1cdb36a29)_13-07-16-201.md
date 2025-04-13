error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4799.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4799.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4799.java
text:
```scala
S@@tring newPath = optionalPathPrefix + "/" + path;

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.elasticsearch.test.rest.support;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.elasticsearch.common.Strings;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public final class FileUtils {

    private static final String YAML_SUFFIX = ".yaml";
    private static final String JSON_SUFFIX = ".json";

    private FileUtils() {

    }

    /**
     * Returns the json files found within the directory provided as argument.
     * Files are looked up in the classpath first, then outside of it if not found.
     */
    public static Set<File> findJsonSpec(String optionalPathPrefix, String path) throws FileNotFoundException {
        File dir = resolveFile(optionalPathPrefix, path, null);

        if (!dir.isDirectory()) {
            throw new FileNotFoundException("file [" + path + "] is not a directory");
        }

        File[] jsonFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(JSON_SUFFIX);
            }
        });

        if (jsonFiles == null || jsonFiles.length == 0) {
            throw new FileNotFoundException("no json files found within [" + path + "]");
        }

        return Sets.newHashSet(jsonFiles);
    }

    /**
     * Returns the yaml files found within the paths provided.
     * Each input path can either be a single file (the .yaml suffix is optional) or a directory.
     * Each path is looked up in the classpath first, then outside of it if not found yet.
     */
    public static Map<String, Set<File>> findYamlSuites(final String optionalPathPrefix, final String... paths) throws FileNotFoundException {
        Map<String, Set<File>> yamlSuites = Maps.newHashMap();
        for (String path : paths) {
            collectFiles(resolveFile(optionalPathPrefix, path, YAML_SUFFIX), YAML_SUFFIX, yamlSuites);
        }
        return yamlSuites;
    }

    private static File resolveFile(String optionalPathPrefix, String path, String optionalFileSuffix) throws FileNotFoundException {
        //try within classpath with and without file suffix (as it could be a single test suite)
        URL resource = findResource(path, optionalFileSuffix);
        if (resource == null) {
            //try within classpath with optional prefix: /rest-spec/test (or /rest-test/api) is optional
            String newPath = optionalPathPrefix + File.separator + path;
            resource = findResource(newPath, optionalFileSuffix);
            if (resource == null) {
                //if it wasn't on classpath we look outside ouf the classpath
                File file = findFile(path, optionalFileSuffix);
                if (!file.exists()) {
                    throw new FileNotFoundException("file [" + path + "] doesn't exist");
                }
                return file;
            }
        }
        return new File(resource.getFile());
    }

    private static URL findResource(String path, String optionalFileSuffix) {
        URL resource = FileUtils.class.getResource(path);
        if (resource == null) {
            //if not found we append the file suffix to the path (as it is optional)
            if (Strings.hasLength(optionalFileSuffix) && !path.endsWith(optionalFileSuffix)) {
                resource = FileUtils.class.getResource(path + optionalFileSuffix);
            }
        }
        return resource;
    }

    private static File findFile(String path, String optionalFileSuffix) {
        File file = new File(path);
        if (!file.exists()) {
            file = new File(path + optionalFileSuffix);
        }
        return file;
    }

    private static void collectFiles(final File file, final String fileSuffix, final Map<String, Set<File>> files) {
        if (file.isFile()) {
            // '.' is uses as separator internally and not expected to be within suite or test names, better replace it
            String groupName = file.getParentFile().getName().replace('.', '_');
            Set<File> filesSet = files.get(groupName);
            if (filesSet == null) {
                filesSet = Sets.newHashSet();
                files.put(groupName, filesSet);
            }
            filesSet.add(file);
        } else if (file.isDirectory()) {
            walkDir(file, fileSuffix, files);
        }
    }

    private static void walkDir(final File dir, final String fileSuffix, final Map<String, Set<File>> files) {
        File[] children = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(fileSuffix);
            }
        });

        for (File file : children) {
            collectFiles(file, fileSuffix, files);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4799.java