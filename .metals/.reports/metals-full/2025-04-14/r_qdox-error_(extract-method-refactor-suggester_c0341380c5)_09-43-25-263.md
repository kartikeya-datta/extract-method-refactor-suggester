error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3951.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3951.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3951.java
text:
```scala
a@@ssertFalse("backward compatibility tests must run in network mode. You probably have a system property overriding the test settings.",

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
package org.elasticsearch.test;

import org.apache.lucene.util.AbstractRandomizedTest;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.cluster.routing.IndexShardRoutingTable;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.regex.Regex;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.discovery.DiscoveryModule;
import org.elasticsearch.discovery.zen.ZenDiscoveryModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.transport.TransportModule;
import org.elasticsearch.transport.TransportService;
import org.elasticsearch.transport.netty.NettyTransport;
import org.junit.Before;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.is;

/**
 * Abstract base class for backwards compatibility tests. Subclasses of this class
 * can run tests against a mixed version cluster. A subset of the nodes in the cluster
 * are started in dedicated process running off a full fledged elasticsearch release.
 * Nodes can be "upgraded" from the "backwards" node to an "new" node where "new" nodes
 * version corresponds to current version.
 * The purpose of this test class is to run tests in scenarios where clusters are in an
 * intermediate state during a rolling upgrade as well as upgrade situations. The clients
 * accessed via #client() are random clients to the nodes in the cluster which might
 * execute requests on the "new" as well as the "old" nodes.
 * <p>
 *   Note: this base class is still experimental and might have bugs or leave external processes running behind.
 * </p>
 * Backwards compatibility tests are disabled by default via {@link org.apache.lucene.util.AbstractRandomizedTest.Backwards} annotation.
 * The following system variables control the test execution:
 * <ul>
 *     <li>
 *          <tt>{@value #TESTS_BACKWARDS_COMPATIBILITY}</tt> enables / disables
 *          tests annotated with {@link org.apache.lucene.util.AbstractRandomizedTest.Backwards} (defaults to
 *          <tt>false</tt>)
 *     </li>
 *     <li>
 *          <tt>{@value #TESTS_BACKWARDS_COMPATIBILITY_VERSION}</tt>
 *          sets the version to run the external nodes from formatted as <i>X.Y.Z</i>.
 *          The tests class will try to locate a release folder <i>elasticsearch-X.Y.Z</i>
 *          within path passed via {@value #TESTS_BACKWARDS_COMPATIBILITY_PATH}
 *          depending on this system variable.
 *     </li>
 *     <li>
 *          <tt>{@value #TESTS_BACKWARDS_COMPATIBILITY_PATH}</tt> the path to the
 *          elasticsearch releases to run backwards compatibility tests against.
 *     </li>
 * </ul>
 *
 */
// the transportClientRatio is tricky here since we don't fully control the cluster nodes
@AbstractRandomizedTest.Backwards
@ElasticsearchIntegrationTest.ClusterScope(minNumDataNodes = 0, maxNumDataNodes = 2, scope = ElasticsearchIntegrationTest.Scope.SUITE, numClientNodes = 0, transportClientRatio = 0.0)
@Ignore
public abstract class ElasticsearchBackwardsCompatIntegrationTest extends ElasticsearchIntegrationTest {

    private static File backwardsCompatibilityPath() {
        String path = System.getProperty(TESTS_BACKWARDS_COMPATIBILITY_PATH);
        String version = System.getProperty(TESTS_BACKWARDS_COMPATIBILITY_VERSION);
        if (path == null || path.isEmpty() || version == null || version.isEmpty()) {
            throw new IllegalArgumentException("Invalid Backwards tests location path:" + path + " version: " + version);
        }
        File file = new File(path, "elasticsearch-" + version);
        if (!file.exists()) {
            throw new IllegalArgumentException("Backwards tests location is missing: " + file.getAbsolutePath());
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Backwards tests location is not a directory: " + file.getAbsolutePath());
        }
        return file;
    }

    public CompositeTestCluster backwardsCluster() {
        return (CompositeTestCluster) cluster();
    }

    protected TestCluster buildTestCluster(Scope scope) throws IOException {
        TestCluster cluster = super.buildTestCluster(scope);
        ExternalNode externalNode = new ExternalNode(backwardsCompatibilityPath(), randomLong(), new SettingsSource() {
            @Override
            public Settings node(int nodeOrdinal) {
                return externalNodeSettings(nodeOrdinal);
            }

            @Override
            public Settings transportClient() {
                return transportClientSettings();
            }
        });
        return new CompositeTestCluster((InternalTestCluster) cluster, between(minExternalNodes(), maxExternalNodes()), externalNode);
    }

    protected int minExternalNodes() {
        return 1;
    }

    protected int maxExternalNodes() {
        return 2;
    }

    @Override
    protected int maximumNumberOfReplicas() {
        return 1;
    }

    @Before
    public final void beforeTest() {
        // 1.0.3 is too flaky - lets get stable first.
        assumeTrue("BWC tests are disabled currently for version [< 1.1.0]", compatibilityVersion().onOrAfter(Version.V_1_1_0));
    }

    protected Settings nodeSettings(int nodeOrdinal) {
        Settings settings = ImmutableSettings.builder()
                .put(TransportModule.TRANSPORT_TYPE_KEY, NettyTransport.class) // run same transport  / disco as external
                .put(DiscoveryModule.DISCOVERY_TYPE_KEY, ZenDiscoveryModule.class)
                .put("node.mode", "network") // we need network mode for this
                .put("gateway.type", "local") // we require local gateway to mimic upgrades of nodes
                .put("discovery.type", "zen") // zen is needed since we start external nodes
                .put(TransportModule.TRANSPORT_SERVICE_TYPE_KEY, TransportService.class.getName())
                .build();

        // verify that the end node setting will have network enabled.
        Tuple<Settings, Environment> finalSettings = InternalSettingsPreparer.prepareSettings(settings, true);
        assertFalse("backward compatibility tests must run in network mode. You probably have a system property overriding the test settings",
                DiscoveryNode.localNode(finalSettings.v1()));
        return settings;
    }

    public void assertAllShardsOnNodes(String index, String pattern) {
        ClusterState clusterState = client().admin().cluster().prepareState().execute().actionGet().getState();
        for (IndexRoutingTable indexRoutingTable : clusterState.routingTable()) {
            for (IndexShardRoutingTable indexShardRoutingTable : indexRoutingTable) {
                for (ShardRouting shardRouting : indexShardRoutingTable) {
                    if (shardRouting.currentNodeId() != null && index.equals(shardRouting.getIndex())) {
                        String name = clusterState.nodes().get(shardRouting.currentNodeId()).name();
                        assertThat("Allocated on new node: " + name, Regex.simpleMatch(pattern, name), is(true));
                    }
                }
            }
        }
    }

    protected Settings externalNodeSettings(int nodeOrdinal) {
        return ImmutableSettings.EMPTY;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3951.java