error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7537.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7537.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7537.java
text:
```scala
M@@anagementUtil.rollbackOperationWithResourceNotFound(context, operation);

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

package org.jboss.as.messaging;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.messaging.CommonAttributes.BINDING_NAMES;
import static org.jboss.as.messaging.CommonAttributes.NUMBER_OF_BYTES_PER_PAGE;
import static org.jboss.as.messaging.CommonAttributes.NUMBER_OF_PAGES;
import static org.jboss.as.messaging.CommonAttributes.QUEUE_NAMES;
import static org.jboss.as.messaging.CommonAttributes.ROLES_ATTR_NAME;
import static org.jboss.as.messaging.ManagementUtil.reportListOfString;
import static org.jboss.as.messaging.ManagementUtil.reportRoles;
import static org.jboss.as.messaging.ManagementUtil.reportRolesAsJSON;
import static org.jboss.as.messaging.MessagingMessages.MESSAGES;

import org.hornetq.api.core.management.AddressControl;
import org.hornetq.api.core.management.ResourceNames;
import org.hornetq.core.server.HornetQServer;
import org.jboss.as.controller.AbstractRuntimeOnlyHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;

/**
 * Handles operations and attribute reads supported by a HornetQ {@link org.hornetq.api.core.management.AddressControl}.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class AddressControlHandler extends AbstractRuntimeOnlyHandler {

    public static AddressControlHandler INSTANCE = new AddressControlHandler();

    private AddressControlHandler() {
    }

    @Override
    protected void executeRuntimeStep(OperationContext context, ModelNode operation) throws OperationFailedException {

        final String operationName = operation.require(OP).asString();
        if (READ_ATTRIBUTE_OPERATION.equals(operationName)) {
            handleReadAttribute(context, operation);
        } else if (CoreAddressDefinition.GET_ROLES_AS_JSON.equals(operationName)) {
            handleGetRolesAsJson(context, operation);
        }
    }

    private void handleReadAttribute(OperationContext context, ModelNode operation) {
        final AddressControl addressControl = getAddressControl(context, operation);
        if (addressControl == null) {
            ManagementUtil.rollbackOperationWithNoHandler(context, operation);
            return;
        }

        final String name = operation.require(ModelDescriptionConstants.NAME).asString();

        try {
            if (ROLES_ATTR_NAME.equals(name)) {
                String json = addressControl.getRolesAsJSON();
                reportRoles(context, json);
            } else if (QUEUE_NAMES.equals(name)) {
                String[] queues = addressControl.getQueueNames();
                reportListOfString(context, queues);
            } else if (NUMBER_OF_BYTES_PER_PAGE.equals(name)) {
                long l = addressControl.getNumberOfBytesPerPage();
                context.getResult().set(l);
            } else if (NUMBER_OF_PAGES.equals(name)) {
                int i = addressControl.getNumberOfPages();
                context.getResult().set(i);
            } else if (BINDING_NAMES.equals(name)) {
                String[] bindings = addressControl.getBindingNames();
                reportListOfString(context, bindings);
            } else {
                // Bug
                throw MESSAGES.unsupportedAttribute(name);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            context.getFailureDescription().set(e.getLocalizedMessage());
        }

        context.stepCompleted();
    }

    private void handleGetRolesAsJson(final OperationContext context, final ModelNode operation) {
        final AddressControl addressControl = getAddressControl(context, operation);
        try {
            String json = addressControl.getRolesAsJSON();
            reportRolesAsJSON(context, json);
            context.stepCompleted();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            context.getFailureDescription().set(e.getLocalizedMessage());
        }
    }

    private AddressControl getAddressControl(final OperationContext context, final ModelNode operation) {
        final String addressName = PathAddress.pathAddress(operation.require(OP_ADDR)).getLastElement().getValue();
        final ServiceName hqServiceName = MessagingServices.getHornetQServiceName(PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR)));
        ServiceController<?> hqService = context.getServiceRegistry(false).getService(hqServiceName);
        HornetQServer hqServer = HornetQServer.class.cast(hqService.getValue());
        return AddressControl.class.cast(hqServer.getManagementService().getResource(ResourceNames.CORE_ADDRESS + addressName));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7537.java