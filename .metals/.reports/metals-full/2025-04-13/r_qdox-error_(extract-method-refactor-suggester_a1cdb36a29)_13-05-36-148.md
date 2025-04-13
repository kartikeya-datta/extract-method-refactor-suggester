error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9744.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9744.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9744.java
text:
```scala
public N@@odeInfo(@Nullable String hostname, DiscoveryNode node, ImmutableMap<String, String> serviceAttributes, Settings settings,

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

package org.elasticsearch.action.admin.cluster.node.info;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.action.support.nodes.NodeOperationResponse;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.http.HttpInfo;
import org.elasticsearch.monitor.jvm.JvmInfo;
import org.elasticsearch.monitor.network.NetworkInfo;
import org.elasticsearch.monitor.os.OsInfo;
import org.elasticsearch.monitor.process.ProcessInfo;
import org.elasticsearch.transport.TransportInfo;

import java.io.IOException;
import java.util.Map;

/**
 * Node information (static, does not change over time).
 */
public class NodeInfo extends NodeOperationResponse {

    private ImmutableMap<String, String> serviceAttributes;

    @Nullable
    private String hostname;

    private Settings settings;

    private OsInfo os;

    private ProcessInfo process;

    private JvmInfo jvm;

    private NetworkInfo network;

    private TransportInfo transport;

    private HttpInfo http;

    NodeInfo() {
    }

    public NodeInfo(String hostname, DiscoveryNode node, ImmutableMap<String, String> serviceAttributes, Settings settings,
                    OsInfo os, ProcessInfo process, JvmInfo jvm, NetworkInfo network,
                    TransportInfo transport, @Nullable HttpInfo http) {
        super(node);
        this.hostname = hostname;
        this.serviceAttributes = serviceAttributes;
        this.settings = settings;
        this.os = os;
        this.process = process;
        this.jvm = jvm;
        this.network = network;
        this.transport = transport;
        this.http = http;
    }

    /**
     * System's hostname. <code>null</code> in case of UnknownHostException
     */
    @Nullable
    public String hostname() {
        return this.hostname;
    }

    /**
     * System's hostname. <code>null</code> in case of UnknownHostException
     */
    @Nullable
    public String getHostname() {
        return hostname();
    }

    /**
     * The service attributes of the node.
     */
    public ImmutableMap<String, String> serviceAttributes() {
        return this.serviceAttributes;
    }

    /**
     * The attributes of the node.
     */
    public ImmutableMap<String, String> getServiceAttributes() {
        return serviceAttributes();
    }

    /**
     * The settings of the node.
     */
    public Settings settings() {
        return this.settings;
    }

    /**
     * The settings of the node.
     */
    public Settings getSettings() {
        return settings();
    }

    /**
     * Operating System level information.
     */
    public OsInfo os() {
        return this.os;
    }

    /**
     * Operating System level information.
     */
    public OsInfo getOs() {
        return os();
    }

    /**
     * Process level information.
     */
    public ProcessInfo process() {
        return process;
    }

    /**
     * Process level information.
     */
    public ProcessInfo getProcess() {
        return process();
    }

    /**
     * JVM level information.
     */
    public JvmInfo jvm() {
        return jvm;
    }

    /**
     * JVM level information.
     */
    public JvmInfo getJvm() {
        return jvm();
    }

    /**
     * Network level information.
     */
    public NetworkInfo network() {
        return network;
    }

    /**
     * Network level information.
     */
    public NetworkInfo getNetwork() {
        return network();
    }

    public TransportInfo transport() {
        return transport;
    }

    public TransportInfo getTransport() {
        return transport();
    }

    public HttpInfo http() {
        return http;
    }

    public HttpInfo getHttp() {
        return http();
    }

    public static NodeInfo readNodeInfo(StreamInput in) throws IOException {
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.readFrom(in);
        return nodeInfo;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        if (in.readBoolean()) {
            hostname = in.readUTF();
        }
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        int size = in.readVInt();
        for (int i = 0; i < size; i++) {
            builder.put(in.readUTF(), in.readUTF());
        }
        serviceAttributes = builder.build();
        settings = ImmutableSettings.readSettingsFromStream(in);
        if (in.readBoolean()) {
            os = OsInfo.readOsInfo(in);
        }
        if (in.readBoolean()) {
            process = ProcessInfo.readProcessInfo(in);
        }
        if (in.readBoolean()) {
            jvm = JvmInfo.readJvmInfo(in);
        }
        if (in.readBoolean()) {
            network = NetworkInfo.readNetworkInfo(in);
        }
        if (in.readBoolean()) {
            transport = TransportInfo.readTransportInfo(in);
        }
        if (in.readBoolean()) {
            http = HttpInfo.readHttpInfo(in);
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        if (hostname == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeUTF(hostname);
        }
        out.writeVInt(serviceAttributes.size());
        for (Map.Entry<String, String> entry : serviceAttributes.entrySet()) {
            out.writeUTF(entry.getKey());
            out.writeUTF(entry.getValue());
        }
        ImmutableSettings.writeSettingsToStream(settings, out);
        if (os == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            os.writeTo(out);
        }
        if (process == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            process.writeTo(out);
        }
        if (jvm == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            jvm.writeTo(out);
        }
        if (network == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            network.writeTo(out);
        }
        if (transport == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            transport.writeTo(out);
        }
        if (http == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            http.writeTo(out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9744.java