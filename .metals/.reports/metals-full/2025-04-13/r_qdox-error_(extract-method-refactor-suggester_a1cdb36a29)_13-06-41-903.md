error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7246.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7246.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7246.java
text:
```scala
L@@ist<RepositoryMetaData> repository = new ArrayList<>();

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

package org.elasticsearch.cluster.metadata;

import com.google.common.collect.ImmutableList;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.loader.SettingsLoader;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains metadata about registered snapshot repositories
 */
public class RepositoriesMetaData implements MetaData.Custom {

    public static final String TYPE = "repositories";

    public static final Factory FACTORY = new Factory();

    private final ImmutableList<RepositoryMetaData> repositories;

    /**
     * Constructs new repository metadata
     *
     * @param repositories list of repositories
     */
    public RepositoriesMetaData(RepositoryMetaData... repositories) {
        this.repositories = ImmutableList.copyOf(repositories);
    }

    /**
     * Returns list of currently registered repositories
     *
     * @return list of repositories
     */
    public ImmutableList<RepositoryMetaData> repositories() {
        return this.repositories;
    }

    /**
     * Returns a repository with a given name or null if such repository doesn't exist
     *
     * @param name name of repository
     * @return repository metadata
     */
    public RepositoryMetaData repository(String name) {
        for (RepositoryMetaData repository : repositories) {
            if (name.equals(repository.name())) {
                return repository;
            }
        }
        return null;
    }

    /**
     * Repository metadata factory
     */
    public static class Factory implements MetaData.Custom.Factory<RepositoriesMetaData> {

        /**
         * {@inheritDoc}
         */
        @Override
        public String type() {
            return TYPE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RepositoriesMetaData readFrom(StreamInput in) throws IOException {
            RepositoryMetaData[] repository = new RepositoryMetaData[in.readVInt()];
            for (int i = 0; i < repository.length; i++) {
                repository[i] = RepositoryMetaData.readFrom(in);
            }
            return new RepositoriesMetaData(repository);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void writeTo(RepositoriesMetaData repositories, StreamOutput out) throws IOException {
            out.writeVInt(repositories.repositories().size());
            for (RepositoryMetaData repository : repositories.repositories()) {
                repository.writeTo(out);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RepositoriesMetaData fromXContent(XContentParser parser) throws IOException {
            XContentParser.Token token;
            List<RepositoryMetaData> repository = new ArrayList<RepositoryMetaData>();
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    String name = parser.currentName();
                    if (parser.nextToken() != XContentParser.Token.START_OBJECT) {
                        throw new ElasticsearchParseException("failed to parse repository [" + name + "], expected object");
                    }
                    String type = null;
                    Settings settings = ImmutableSettings.EMPTY;
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            String currentFieldName = parser.currentName();
                            if ("type".equals(currentFieldName)) {
                                if (parser.nextToken() != XContentParser.Token.VALUE_STRING) {
                                    throw new ElasticsearchParseException("failed to parse repository [" + name + "], unknown type");
                                }
                                type = parser.text();
                            } else if ("settings".equals(currentFieldName)) {
                                if (parser.nextToken() != XContentParser.Token.START_OBJECT) {
                                    throw new ElasticsearchParseException("failed to parse repository [" + name + "], incompatible params");
                                }
                                settings = ImmutableSettings.settingsBuilder().put(SettingsLoader.Helper.loadNestedFromMap(parser.mapOrdered())).build();
                            } else {
                                throw new ElasticsearchParseException("failed to parse repository [" + name + "], unknown field [" + currentFieldName + "]");
                            }
                        } else {
                            throw new ElasticsearchParseException("failed to parse repository [" + name + "]");
                        }
                    }
                    if (type == null) {
                        throw new ElasticsearchParseException("failed to parse repository [" + name + "], missing repository type");
                    }
                    repository.add(new RepositoryMetaData(name, type, settings));
                } else {
                    throw new ElasticsearchParseException("failed to parse repositories");
                }
            }
            return new RepositoriesMetaData(repository.toArray(new RepositoryMetaData[repository.size()]));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void toXContent(RepositoriesMetaData customIndexMetaData, XContentBuilder builder, ToXContent.Params params) throws IOException {
            for (RepositoryMetaData repository : customIndexMetaData.repositories()) {
                toXContent(repository, builder, params);
            }
        }

        /**
         * Serializes information about a single repository
         *
         * @param repository repository metadata
         * @param builder    XContent builder
         * @param params     serialization parameters
         * @throws IOException
         */
        public void toXContent(RepositoryMetaData repository, XContentBuilder builder, ToXContent.Params params) throws IOException {
            builder.startObject(repository.name(), XContentBuilder.FieldCaseConversion.NONE);
            builder.field("type", repository.type());
            builder.startObject("settings");
            for (Map.Entry<String, String> settingEntry : repository.settings().getAsMap().entrySet()) {
                builder.field(settingEntry.getKey(), settingEntry.getValue());
            }
            builder.endObject();

            builder.endObject();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isPersistent() {
            return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7246.java