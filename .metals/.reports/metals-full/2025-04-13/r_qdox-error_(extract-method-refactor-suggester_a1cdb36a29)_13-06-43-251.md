error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8763.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8763.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8763.java
text:
```scala
t@@hrow new IllegalStateException("ParsedResult is undefined");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.demos.domain.host.runner;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PATH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SYSTEM_PROPERTY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;

import java.io.IOException;
import java.net.InetAddress;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.demos.DemoAuthentication;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.dmr.ModelNode;

/**
 * Host specific operations example runner.
 *
 * @author Emanuel Muckenhuber
 */
public class ExampleRunner {
    public static void main(String[] args) throws Exception {
        final ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999, DemoAuthentication.getCallbackHandler());
        try {
            new ExampleRunner().run(client);
        } finally {
            StreamUtils.safeClose(client);
        }
    }

    protected void run(ModelControllerClient client) throws Exception {
        // system properties
        runSysProperties(client);
        // paths
        runPaths(client);
        // interfaces
        runInterface(client);
    }

    void runSysProperties(final ModelControllerClient client) throws Exception {
        final ModelNode address = new ModelNode();
        address.add(HOST, "master");
        address.add(SYSTEM_PROPERTY, "test-property");

        final ModelNode operation = new ModelNode();
        operation.get(OP).set("add");
        operation.get(OP_ADDR).set(address);
        operation.get(VALUE).set("test-value");

        final ModelNode reversing = new ModelNode();
        reversing.get(OP).set("remove");
        reversing.get(OP_ADDR).set(address);

        runOperationAndRollback(operation, reversing, client);
    }

    void runPaths(final ModelControllerClient client) throws Exception {
        final ModelNode address = new ModelNode();
        address.add(HOST, "master");
        address.add(PATH, "temp");

        final ModelNode operation = new ModelNode();
        operation.get(OP).set(ADD);
        operation.get(OP_ADDR).set(address);
        operation.get(NAME).set("temp");
        operation.get(PATH).set("temp");

        final ModelNode reversing = new ModelNode();
        reversing.get(OP).set("remove");
        reversing.get(OP_ADDR).set(address);

        runOperationAndRollback(operation, reversing, client);
    }

    void runInterface(final ModelControllerClient client) throws IOException {
        final ModelNode address = new ModelNode();
        address.add(HOST, "master");
        address.add(INTERFACE, "new");

        final ModelNode operation = new ModelNode();
        operation.get(OP).set(ADD);
        operation.get(OP_ADDR).set(address);
        operation.get(NAME).set("new");
        operation.get("any-address").set(true);

        final ModelNode reversing = new ModelNode();
        reversing.get(OP).set("remove");
        reversing.get(OP_ADDR).set(address);

        runOperationAndRollback(operation, reversing, client);
    }

    void runOperationAndRollback(final ModelNode operation, final ModelNode reversing, final ModelControllerClient client) throws IOException {

        System.out.println("Executing operation:\n" + operation);
        final ModelNode result = client.execute(operation);
        try {
            checkSuccess(result);

            final ModelNode readResource = new ModelNode();
            readResource.get(OP).set(READ_RESOURCE_OPERATION);
            readResource.get(OP_ADDR).set(operation.require(OP_ADDR));

            final ModelNode readResult = client.execute(readResource);
            checkSuccess(readResult);

            System.out.println("Effect on resource is \n" + readResult.get(RESULT));
        } finally {
            System.out.println("Reverting change via \n" + reversing);
            checkSuccess(client.execute(reversing));
        }
    }

    void checkSuccess(final ModelNode result) {
        if (result.hasDefined(OUTCOME) && SUCCESS.equals(result.get(OUTCOME).asString())) {
            return;
        }

        System.out.println("Outcome was not successful:\n" + result);
        if (result.hasDefined("failure-description")) {
            throw new RuntimeException(result.get("failure-description").toString());
        }
        else if (result.hasDefined("domain-failure-description")) {
            throw new RuntimeException(result.get("domain-failure-description").toString());
        }
        else if (result.hasDefined("host-failure-descriptions")) {
            throw new RuntimeException(result.get("host-failure-descriptions").toString());
        }
        else if (result.isDefined()) {
            System.out.println(result);
            throw new RuntimeException("Operation outcome is " + result.get("outcome").asString());
        }
        else {
            throw new IllegalStateException("Result is undefined");
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8763.java