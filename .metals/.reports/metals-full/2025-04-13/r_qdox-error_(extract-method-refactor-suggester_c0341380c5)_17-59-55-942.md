error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/237.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/237.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/237.java
text:
```scala
r@@eturn new VersionedValue(endpoint.getHostAddress());

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.gms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.Token;
import org.apache.cassandra.io.ICompactSerializer;
import org.apache.cassandra.utils.FBUtilities;


/**
 * This abstraction represents the state associated with a particular node which an
 * application wants to make available to the rest of the nodes in the cluster. 
 * Whenever a piece of state needs to be disseminated to the rest of cluster wrap
 * the state in an instance of <i>ApplicationState</i> and add it to the Gossiper.
 *  
 * e.g. if we want to disseminate load information for node A do the following:
 * 
 *      ApplicationState loadState = new ApplicationState(<string representation of load>);
 *      Gossiper.instance.addApplicationState("LOAD STATE", loadState);
 */

public class VersionedValue implements Comparable<VersionedValue>
{
    public static final ICompactSerializer<VersionedValue> serializer = new VersionedValueSerializer();

    // this must be a char that cannot be present in any token
    public final static char DELIMITER = ',';
    public final static String DELIMITER_STR = new String(new char[] { DELIMITER });

    // values for ApplicationState.STATUS
    public final static String STATUS_BOOTSTRAPPING = "BOOT";
    public final static String STATUS_NORMAL = "NORMAL";
    public final static String STATUS_LEAVING = "LEAVING";
    public final static String STATUS_LEFT = "LEFT";
    public final static String STATUS_MOVING = "MOVING";

    public final static String REMOVING_TOKEN = "removing";
    public final static String REMOVED_TOKEN = "removed";

    // values for ApplicationState.REMOVAL_COORDINATOR
    public final static String REMOVAL_COORDINATOR = "REMOVER";

    public final int version;
    public final String value;

    private VersionedValue(String value, int version)
    {
        this.value = value;
        this.version = version;
    }

    private VersionedValue(String value)
    {
        this.value = value;
        version = VersionGenerator.getNextVersion();
    }

    public int compareTo(VersionedValue value)
    {
        return this.version - value.version;
    }

    @Override
    public String toString()
    {
        return "Value(" + value + "," + version + ")";
    }

    public static class VersionedValueFactory
    {
        IPartitioner partitioner;

        public VersionedValueFactory(IPartitioner partitioner)
        {
            this.partitioner = partitioner;
        }

        public VersionedValue bootstrapping(Token token)
        {
            return new VersionedValue(VersionedValue.STATUS_BOOTSTRAPPING + VersionedValue.DELIMITER + partitioner.getTokenFactory().toString(token));
        }

        public VersionedValue normal(Token token)
        {
            return new VersionedValue(VersionedValue.STATUS_NORMAL + VersionedValue.DELIMITER + partitioner.getTokenFactory().toString(token));
        }

        public VersionedValue load(double load)
        {
            return new VersionedValue(String.valueOf(load));
        }

        public VersionedValue migration(UUID newVersion)
        {
            return new VersionedValue(newVersion.toString());
        }

        public VersionedValue leaving(Token token)
        {
            return new VersionedValue(VersionedValue.STATUS_LEAVING + VersionedValue.DELIMITER + partitioner.getTokenFactory().toString(token));
        }

        public VersionedValue left(Token token)
        {
            return new VersionedValue(VersionedValue.STATUS_LEFT + VersionedValue.DELIMITER + partitioner.getTokenFactory().toString(token));
        }

        public VersionedValue moving(Token token)
        {
            return new VersionedValue(VersionedValue.STATUS_MOVING + VersionedValue.DELIMITER + partitioner.getTokenFactory().toString(token));
        }

        public VersionedValue removingNonlocal(Token token)
        {
            return new VersionedValue(VersionedValue.REMOVING_TOKEN + VersionedValue.DELIMITER + partitioner.getTokenFactory().toString(token));
        }

        public VersionedValue removedNonlocal(Token token)
        {
            return new VersionedValue(VersionedValue.REMOVED_TOKEN + VersionedValue.DELIMITER + partitioner.getTokenFactory().toString(token));
        }

        public VersionedValue removalCoordinator(Token token)
        {
            return new VersionedValue(VersionedValue.REMOVAL_COORDINATOR + VersionedValue.DELIMITER + partitioner.getTokenFactory().toString(token));
        }

        public VersionedValue datacenter(String dcId)
        {
            return new VersionedValue(dcId);
        }

        public VersionedValue rack(String rackId)
        {
            return new VersionedValue(rackId);
        }

        public VersionedValue rpcaddress(InetAddress endpoint)
        {
            return new VersionedValue(endpoint.toString());
        }

        public VersionedValue releaseVersion()
        {
            return new VersionedValue(FBUtilities.getReleaseVersionString());
        }
    }

    private static class VersionedValueSerializer implements ICompactSerializer<VersionedValue>
    {
        public void serialize(VersionedValue value, DataOutputStream dos, int version) throws IOException
        {
            dos.writeUTF(value.value);
            dos.writeInt(value.version);
        }

        public VersionedValue deserialize(DataInputStream dis, int version) throws IOException
        {
            String value = dis.readUTF();
            int valVersion = dis.readInt();
            return new VersionedValue(value, valVersion);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/237.java