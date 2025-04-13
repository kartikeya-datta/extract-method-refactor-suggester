error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5902.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5902.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5902.java
text:
```scala
l@@og.warn("access-control/default is missing operations: " + defaults);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.as.cli.accesscontrol;

import java.util.Iterator;

import org.jboss.as.cli.Util;
import org.jboss.as.cli.operation.OperationRequestAddress;
import org.jboss.as.cli.operation.OperationRequestAddress.Node;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

/**
 * @author Alexey Loubyansky
 *
 */
public class CLIAccessControl {

    private static final Logger log = Logger.getLogger(CLIAccessControl.class);

    public static boolean isExecute(ModelControllerClient client, OperationRequestAddress address, String operation) {
        return isExecute(client, null, address, operation);
    }

    public static boolean isExecute(ModelControllerClient client, String[] parent, OperationRequestAddress address, String operation) {
        final ModelNode accessControl = getAccessControl(client, parent, address, true);
        if(accessControl == null) {
            return false;
        }

        if(!accessControl.has(Util.DEFAULT)) {
            log.warn("access-control is missing defaults: " + accessControl);
            return false;
        }

        final ModelNode defaults = accessControl.get(Util.DEFAULT);
        if(!defaults.has(Util.OPERATIONS)) {
            log.warn("access-control/default is missing operations: " + accessControl);
            return false;
        }

        final ModelNode operations = defaults.get(Util.OPERATIONS);
        if(!operations.has(operation)) {
            if(log.isTraceEnabled()) {
                log.trace("The operation is missing in the description: " + operation);
            }
            return false;
        }

        final ModelNode opAC = operations.get(operation);
        if(!opAC.has(Util.EXECUTE)) {
            log.warn("'execute' is missing for " + operation + " in " + accessControl);
            return false;
        }

        return opAC.get(Util.EXECUTE).asBoolean();
    }

    /**
     * Executed read-resource-description and returns access-control info.
     * Returns null in case there was any kind of problem.
     *
     * @param client
     * @param address
     * @return
     */
    public static ModelNode getAccessControl(ModelControllerClient client, OperationRequestAddress address, boolean operations) {
        return getAccessControl(client, null, address, operations);
    }

    public static ModelNode getAccessControl(ModelControllerClient client, String[] parent, OperationRequestAddress address, boolean operations) {

        if(client == null) {
            return null;
        }

        if(address.endsOnType()) {
            log.debug("The prefix ends on a type.");
            return null;
        }

        final ModelNode request = new ModelNode();
        setAddress(request, parent, address);
        request.get(Util.OPERATION).set(Util.READ_RESOURCE_DESCRIPTION);
        request.get(Util.ACCESS_CONTROL).set(Util.TRIM_DESCRIPTIONS);
        if(operations) {
            request.get(Util.OPERATIONS).set(true);
        }

        final ModelNode response;
        try {
            response = client.execute(request);
        } catch (Exception e) {
            log.warn("Failed to execute " + Util.READ_RESOURCE_DESCRIPTION, e);
            return null;
        }

        if (!Util.isSuccess(response)) {
            log.debug("Failed to execute " + Util.READ_RESOURCE_DESCRIPTION + ": " + response);
            return null;
        }

        if(!response.has(Util.RESULT)) {
            log.warn("Response is missing result for " + Util.READ_RESOURCE_DESCRIPTION + ": " + response);
            return null;
        }

        final ModelNode result = response.get(Util.RESULT);
        if(!result.has(Util.ACCESS_CONTROL)) {
            log.warn("Result is missing access-control for " + Util.READ_RESOURCE_DESCRIPTION + ": " + response);
            return null;
        }

        return result.get(Util.ACCESS_CONTROL);
    }

    private static void setAddress(ModelNode request, String[] parent, OperationRequestAddress address) {
        final ModelNode addressNode = request.get(Util.ADDRESS);
        addressNode.setEmptyList();
        if(parent != null) {
            int i = 0;
            while(i < parent.length) {
                addressNode.add(parent[i++], parent[i++]);
            }
        }

        if(!address.isEmpty()) {
            final Iterator<Node> iterator = address.iterator();
            while (iterator.hasNext()) {
                final OperationRequestAddress.Node node = iterator.next();
                if (node.getName() != null) {
                    addressNode.add(node.getType(), node.getName());
                } else if (iterator.hasNext()) {
                    throw new IllegalArgumentException(
                            "The node name is not specified for type '"
                                    + node.getType() + "'");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5902.java