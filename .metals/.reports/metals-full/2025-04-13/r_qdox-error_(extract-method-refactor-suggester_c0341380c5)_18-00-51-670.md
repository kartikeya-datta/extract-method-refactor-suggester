error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6173.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6173.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6173.java
text:
```scala
t@@hrow new RuntimeException(result.get(FAILURE_DESCRIPTION).toString());

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.as.controller.util;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILDREN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.EXTENSION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INHERITED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT_MAJOR_VERSION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT_MICRO_VERSION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT_MINOR_VERSION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MODEL_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATIONS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROFILE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROXIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.XML_NAMESPACES;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.xnio.IoUtils;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class Tools {

    static final String CORE = "core";
    static final String STANDALONE = "standalone";
    static final String VERSION = "version";

    static ModelNode getAndCheckResult(ModelNode result) {
        if (!result.get(OUTCOME).asString().equals(SUCCESS)) {
            throw new RuntimeException(result.get(FAILURE_DESCRIPTION).asString());
        }
        return result.get(RESULT);
    }

    static ModelVersion createModelVersion(ModelNode node) {
        return ModelVersion.create(
                readVersion(node, MANAGEMENT_MAJOR_VERSION),
                readVersion(node, MANAGEMENT_MINOR_VERSION),
                readVersion(node, MANAGEMENT_MICRO_VERSION));
    }

    static ModelNode readModelVersionFields(ModelNode node) {
        ModelNode version = new ModelNode();
        if (node.hasDefined(MANAGEMENT_MAJOR_VERSION)) {
            version.get(MANAGEMENT_MAJOR_VERSION).set(node.get(MANAGEMENT_MAJOR_VERSION));
        }
        if (node.hasDefined(MANAGEMENT_MINOR_VERSION)) {
            version.get(MANAGEMENT_MINOR_VERSION).set(node.get(MANAGEMENT_MINOR_VERSION));

        }
        if (node.hasDefined(MANAGEMENT_MICRO_VERSION)) {
            version.get(MANAGEMENT_MICRO_VERSION).set(node.get(MANAGEMENT_MICRO_VERSION));
        }
        return version;
    }


    static ModelNode getCurrentModelVersions() throws Exception {
        ModelControllerClient client = ModelControllerClient.Factory.create("localhost", 9999);
        try {
            ModelNode allVersions = new ModelNode();

            ModelNode op = new ModelNode();
            op.get(OP).set(READ_RESOURCE_OPERATION);
            op.get(OP_ADDR).setEmptyList();
            ModelNode result = Tools.getAndCheckResult(client.execute(op));

            allVersions.get(CORE, STANDALONE).set(readModelVersionFields(result));

            op.get(OP_ADDR).add(EXTENSION, "*").add(SUBSYSTEM, "*");
            result = Tools.getAndCheckResult(client.execute(op));

            //Shove it into a tree map to sort the subsystems alphabetically
            TreeMap<String, ModelNode> map = new TreeMap<String, ModelNode>();
            List<ModelNode> subsystemResults = result.asList();
            for (ModelNode subsystemResult : subsystemResults) {
                String subsystemName = PathAddress.pathAddress(subsystemResult.get(OP_ADDR)).getLastElement().getValue();
                map.put(subsystemName, Tools.getAndCheckResult(subsystemResult));
            }

            for (Map.Entry<String, ModelNode> entry : map.entrySet()) {
                allVersions.get(SUBSYSTEM, entry.getKey()).set(readModelVersionFields(entry.getValue()));
                allVersions.get(SUBSYSTEM, entry.getKey()).get(XML_NAMESPACES).set(entry.getValue().get(XML_NAMESPACES));
            }
            return allVersions;
        } finally {
            IoUtils.safeClose(client);
        }
    }

    static ModelNode getCurrentRunningResourceDefinition(PathAddress pathAddress) throws Exception {
        ModelControllerClient client = ModelControllerClient.Factory.create("localhost", 9999);
        try {
            ModelNode op = new ModelNode();
            op.get(OP).set(READ_RESOURCE_DESCRIPTION_OPERATION);
            op.get(OP_ADDR).set(pathAddress.toModelNode());
            op.get(RECURSIVE).set(true);
            op.get(OPERATIONS).set(true);
            op.get(PROXIES).set(false);
            op.get(INHERITED).set(false);

            return Tools.getAndCheckResult(client.execute(op));

        } finally {
            IoUtils.safeClose(client);
        }
    }

    static ModelNode getCurrentRunningDomainResourceDefinition() throws Exception {
        ModelNode node = getCurrentRunningResourceDefinition(PathAddress.EMPTY_ADDRESS);
        ModelNode profile = node.get(CHILDREN).require(PROFILE).require(MODEL_DESCRIPTION).require("*");
        node.get(CHILDREN).require(HOST);
        node.require(CHILDREN).remove(HOST);

        //Get rid of the profile children. Subsystems are handled for the standalone model
        profile.get(CHILDREN).setEmptyList();
        return node;
    }

    static int readVersion(ModelNode node, String name) {
        if (!node.hasDefined(name)) {
            return 0;
        }
        return node.get(name).asInt();
    }

    static void serializeModeNodeToFile(ModelNode modelNode, File file) throws Exception {
        if (file.exists()) {
            file.delete();
        }
        PrintWriter writer = new PrintWriter(file);
        try {
            modelNode.writeString(writer, false);
            System.out.println("Resource definition for running server written to: " + file.getAbsolutePath());
        } finally {
            IoUtils.safeClose(writer);
        }
    }

    static ModelNode loadModelNodeFromFile(File file) throws Exception {
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist " + file);
        }

        StringBuilder sb = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        } finally {
            IoUtils.safeClose(reader);
        }
        return new ModelNode().fromString(sb.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6173.java