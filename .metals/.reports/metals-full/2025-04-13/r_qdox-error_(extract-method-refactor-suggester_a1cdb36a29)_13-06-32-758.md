error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7315.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7315.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7315.java
text:
```scala
L@@ist<FieldMapper> tempMappers = new ArrayList<>(this.mappers);

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

package org.elasticsearch.index.mapper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.regex.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A class that holds a map of field mappers from name, index name, and full name.
 */
public class FieldMappersLookup implements Iterable<FieldMapper> {

    private volatile ImmutableList<FieldMapper> mappers;
    private volatile ImmutableOpenMap<String, FieldMappers> name;
    private volatile ImmutableOpenMap<String, FieldMappers> indexName;
    private volatile ImmutableOpenMap<String, FieldMappers> fullName;

    public FieldMappersLookup() {
        this.mappers = ImmutableList.of();
        this.fullName = ImmutableOpenMap.of();
        this.name = ImmutableOpenMap.of();
        this.indexName = ImmutableOpenMap.of();
    }

    /**
     * Adds a new set of mappers.
     */
    public void addNewMappers(Iterable<FieldMapper> newMappers) {
        final ImmutableOpenMap.Builder<String, FieldMappers> tempName = ImmutableOpenMap.builder(name);
        final ImmutableOpenMap.Builder<String, FieldMappers> tempIndexName = ImmutableOpenMap.builder(indexName);
        final ImmutableOpenMap.Builder<String, FieldMappers> tempFullName = ImmutableOpenMap.builder(fullName);

        for (FieldMapper fieldMapper : newMappers) {
            FieldMappers mappers = tempName.get(fieldMapper.names().name());
            if (mappers == null) {
                mappers = new FieldMappers(fieldMapper);
            } else {
                mappers = mappers.concat(fieldMapper);
            }
            tempName.put(fieldMapper.names().name(), mappers);

            mappers = tempIndexName.get(fieldMapper.names().indexName());
            if (mappers == null) {
                mappers = new FieldMappers(fieldMapper);
            } else {
                mappers = mappers.concat(fieldMapper);
            }
            tempIndexName.put(fieldMapper.names().indexName(), mappers);

            mappers = tempFullName.get(fieldMapper.names().fullName());
            if (mappers == null) {
                mappers = new FieldMappers(fieldMapper);
            } else {
                mappers = mappers.concat(fieldMapper);
            }
            tempFullName.put(fieldMapper.names().fullName(), mappers);
        }
        this.mappers = ImmutableList.<FieldMapper>builder().addAll(this.mappers).addAll(newMappers).build();
        this.name = tempName.build();
        this.indexName = tempIndexName.build();
        this.fullName = tempFullName.build();
    }

    /**
     * Removes the set of mappers.
     */
    public void removeMappers(Iterable<FieldMapper> mappersToRemove) {
        List<FieldMapper> tempMappers = new ArrayList<FieldMapper>(this.mappers);
        ImmutableOpenMap.Builder<String, FieldMappers> tempName = ImmutableOpenMap.builder(this.name);
        ImmutableOpenMap.Builder<String, FieldMappers> tempIndexName = ImmutableOpenMap.builder(this.indexName);
        ImmutableOpenMap.Builder<String, FieldMappers> tempFullName = ImmutableOpenMap.builder(this.fullName);

        for (FieldMapper mapper : mappersToRemove) {
            FieldMappers mappers = tempName.get(mapper.names().name());
            if (mappers != null) {
                mappers = mappers.remove(mapper);
                if (mappers.isEmpty()) {
                    tempName.remove(mapper.names().name());
                } else {
                    tempName.put(mapper.names().name(), mappers);
                }
            }

            mappers = tempIndexName.get(mapper.names().indexName());
            if (mappers != null) {
                mappers = mappers.remove(mapper);
                if (mappers.isEmpty()) {
                    tempIndexName.remove(mapper.names().indexName());
                } else {
                    tempIndexName.put(mapper.names().indexName(), mappers);
                }
            }

            mappers = tempFullName.get(mapper.names().fullName());
            if (mappers != null) {
                mappers = mappers.remove(mapper);
                if (mappers.isEmpty()) {
                    tempFullName.remove(mapper.names().fullName());
                } else {
                    tempFullName.put(mapper.names().fullName(), mappers);
                }
            }

            tempMappers.remove(mapper);
        }


        this.mappers = ImmutableList.copyOf(tempMappers);
        this.name = tempName.build();
        this.indexName = tempIndexName.build();
        this.fullName = tempFullName.build();
    }

    @Override
    public UnmodifiableIterator<FieldMapper> iterator() {
        return mappers.iterator();
    }

    /**
     * The list of all mappers.
     */
    public ImmutableList<FieldMapper> mappers() {
        return this.mappers;
    }

    /**
     * Is there a mapper (based on unique {@link FieldMapper} identity)?
     */
    public boolean hasMapper(FieldMapper fieldMapper) {
        return mappers.contains(fieldMapper);
    }

    /**
     * Returns the field mappers based on the mapper name.
     */
    public FieldMappers name(String name) {
        return this.name.get(name);
    }

    /**
     * Returns the field mappers based on the mapper index name.
     */
    public FieldMappers indexName(String indexName) {
        return this.indexName.get(indexName);
    }

    /**
     * Returns the field mappers based on the mapper full name.
     */
    public FieldMappers fullName(String fullName) {
        return this.fullName.get(fullName);
    }

    /**
     * Returns a set of the index names of a simple match regex like pattern against full name, name and index name.
     */
    public Set<String> simpleMatchToIndexNames(String pattern) {
        Set<String> fields = Sets.newHashSet();
        for (FieldMapper fieldMapper : mappers) {
            if (Regex.simpleMatch(pattern, fieldMapper.names().fullName())) {
                fields.add(fieldMapper.names().indexName());
            } else if (Regex.simpleMatch(pattern, fieldMapper.names().indexName())) {
                fields.add(fieldMapper.names().indexName());
            } else if (Regex.simpleMatch(pattern, fieldMapper.names().name())) {
                fields.add(fieldMapper.names().indexName());
            }
        }
        return fields;
    }

    /**
     * Returns a set of the full names of a simple match regex like pattern against full name, name and index name.
     */
    public Set<String> simpleMatchToFullName(String pattern) {
        Set<String> fields = Sets.newHashSet();
        for (FieldMapper fieldMapper : mappers) {
            if (Regex.simpleMatch(pattern, fieldMapper.names().fullName())) {
                fields.add(fieldMapper.names().fullName());
            } else if (Regex.simpleMatch(pattern, fieldMapper.names().indexName())) {
                fields.add(fieldMapper.names().fullName());
            } else if (Regex.simpleMatch(pattern, fieldMapper.names().name())) {
                fields.add(fieldMapper.names().fullName());
            }
        }
        return fields;
    }

    /**
     * Tries to find first based on {@link #fullName(String)}, then by {@link #indexName(String)}, and last
     * by {@link #name(String)}.
     */
    @Nullable
    public FieldMappers smartName(String name) {
        FieldMappers fieldMappers = fullName(name);
        if (fieldMappers != null) {
            return fieldMappers;
        }
        fieldMappers = indexName(name);
        if (fieldMappers != null) {
            return fieldMappers;
        }
        return name(name);
    }

    /**
     * Tries to find first based on {@link #fullName(String)}, then by {@link #indexName(String)}, and last
     * by {@link #name(String)} and return the first mapper for it (see {@link org.elasticsearch.index.mapper.FieldMappers#mapper()}).
     */
    @Nullable
    public FieldMapper smartNameFieldMapper(String name) {
        FieldMappers fieldMappers = smartName(name);
        if (fieldMappers == null) {
            return null;
        }
        return fieldMappers.mapper();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7315.java