error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5623.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5623.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5623.java
text:
```scala
t@@his.indexDirectory = clusterName.value() + "/indices/" + index.name();

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

package org.elasticsearch.index.gateway.cloud;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.cloud.blobstore.CloudBlobStoreService;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.gateway.Gateway;
import org.elasticsearch.gateway.cloud.CloudGateway;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.gateway.IndexGateway;
import org.elasticsearch.index.gateway.IndexShardGateway;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.util.SizeUnit;
import org.elasticsearch.util.SizeValue;
import org.elasticsearch.util.inject.Inject;
import org.elasticsearch.util.settings.Settings;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.domain.Location;

import java.util.Set;

/**
 * @author kimchy (shay.banon)
 */
public class CloudIndexGateway extends AbstractIndexComponent implements IndexGateway {

    private final Gateway gateway;

    private final String indexContainer;

    private final String indexDirectory;

    private final Location location;

    private final SizeValue chunkSize;

    private final BlobStoreContext blobStoreContext;

    @Inject public CloudIndexGateway(Index index, @IndexSettings Settings indexSettings, ClusterName clusterName, CloudBlobStoreService blobStoreService, Gateway gateway) {
        super(index, indexSettings);
        this.blobStoreContext = blobStoreService.context();
        this.gateway = gateway;

        String location = componentSettings.get("location");
        String container = componentSettings.get("container");
        SizeValue chunkSize = componentSettings.getAsSize("chunk_size", null);

        if (gateway instanceof CloudGateway) {
            CloudGateway cloudGateway = (CloudGateway) gateway;
            if (container == null) {
                container = cloudGateway.container();
            }
            if (chunkSize == null) {
                chunkSize = cloudGateway.chunkSize();
            }
        }

        if (chunkSize == null) {
            chunkSize = new SizeValue(1, SizeUnit.GB);
        }

        if (location == null) {
            if (gateway instanceof CloudGateway) {
                CloudGateway cloudGateway = (CloudGateway) gateway;
                this.location = cloudGateway.location();
            } else {
                this.location = null;
            }
        } else {
            Location matchedLocation = null;
            Set<? extends Location> assignableLocations = blobStoreContext.getBlobStore().listAssignableLocations();
            for (Location oLocation : assignableLocations) {
                if (oLocation.getId().equals(location)) {
                    matchedLocation = oLocation;
                    break;
                }
            }
            this.location = matchedLocation;
            if (this.location == null) {
                throw new ElasticSearchIllegalArgumentException("Not a valid location [" + location + "], available locations " + assignableLocations);
            }
        }
        this.indexContainer = container;
        this.indexDirectory = clusterName.value() + "/" + index.name();
        this.chunkSize = chunkSize;

        logger.debug("Using location [{}], container [{}], index_directory [{}], chunk_size [{}]", this.location, this.indexContainer, this.indexDirectory, this.chunkSize);
    }

    public Location indexLocation() {
        return this.location;
    }

    public String indexContainer() {
        return this.indexContainer;
    }

    public String indexDirectory() {
        return this.indexDirectory;
    }

    public SizeValue chunkSize() {
        return this.chunkSize;
    }

    @Override public Class<? extends IndexShardGateway> shardGatewayClass() {
        return CloudIndexShardGateway.class;
    }

    @Override public void close(boolean delete) throws ElasticSearchException {
        if (!delete) {
            return;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5623.java