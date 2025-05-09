error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5632.java
text:
```scala
n@@ew ServerGroupRemoveHandler(null).execute(operationContext, operation);

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
package org.jboss.as.domain.controller.operations;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROFILE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class ServerGroupAffectedResourceServerGroupOperationsTestCase extends AbstractOperationTestCase {

    @Test
    public void testAddServerGroupMaster() throws Exception {
        testAddServerGroup(true, false);
    }

    @Test
    public void testAddServerGroupSlave() throws Exception {
        testAddServerGroup(false, false);
    }

    @Test
    public void testAddServerGroupMasterRollback() throws Exception {
        testAddServerGroup(true, true);
    }

    @Test
    public void testAddServerGroupSlaveRollback() throws Exception {
        testAddServerGroup(false, true);
    }

    private void testAddServerGroup(boolean master, boolean rollback) throws Exception {
        testAddServerGroupBadInfo(master, rollback, false, false);
    }

    @Test(expected=OperationFailedException.class)
    public void testAddServerGroupBadProfileMaster() throws Exception {
        testAddServerGroupBadInfo(true, false, true, false);
    }

    @Test
    public void testAddServerGroupBadProfileSlave() throws Exception {
        testAddServerGroupBadInfo(false, false, true, false);
    }

    @Test(expected=OperationFailedException.class)
    public void testAddServerGroupBadProfileMasterRollback() throws Exception {
        //This won't actually get to the rollback part
        testAddServerGroupBadInfo(true, true, true, false);
    }

    @Test
    public void testAddServerGroupBadProfileSlaveRollback() throws Exception {
        testAddServerGroupBadInfo(false, true, true, false);
    }

    @Test(expected=OperationFailedException.class)
    public void testAddServerGroupBadSocketBindingGroupMaster() throws Exception {
        testAddServerGroupBadInfo(true, false, false, true);
    }

    @Test
    public void testAddServerGroupBadSocketBindingGroupSlave() throws Exception {
        testAddServerGroupBadInfo(false, false, false, true);
    }

    @Test(expected=OperationFailedException.class)
    public void testAddServerGroupBadSocketBindingGroupMasterRollback() throws Exception {
        //This won't actually get to the rollback part
        testAddServerGroupBadInfo(true, true, false, true);
    }

    @Test
    public void testAddServerGroupBadSocketBindingGroupSlaveRollback() throws Exception {
        testAddServerGroupBadInfo(false, true, false, true);
    }

    private void testAddServerGroupBadInfo(boolean master, boolean rollback, boolean badProfile, boolean badSocketBindingGroup) throws Exception {
        PathAddress pa = PathAddress.pathAddress(PathElement.pathElement(SERVER_GROUP, "group-three"));
        final MockOperationContext operationContext = getOperationContext(rollback, pa);

        String profileName = badProfile ? "bad-profile" : "profile-two";
        String socketBindingGroupName = badSocketBindingGroup ? "bad-group" : "binding-two";

        final ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).set(pa.toModelNode());
        operation.get(OP).set(ADD);
        operation.get(PROFILE).set(profileName);
        operation.get(SOCKET_BINDING_GROUP).set(socketBindingGroupName);

        new ServerGroupAddHandler(master).execute(operationContext, operation);

        if (master && (badProfile || badSocketBindingGroup)) {
            Assert.fail();
        }

        if (rollback) {
            Assert.assertFalse(operationContext.isReloadRequired());
        } else {
            if (badProfile || badSocketBindingGroup) {
                Assert.assertTrue(operationContext.isReloadRequired());
            } else {
                Assert.assertFalse(operationContext.isReloadRequired());
            }
        }
    }

    @Test
    public void testRemoveServerGroupMaster() throws Exception {
        testRemoveServerGroup(true, false);
    }

    @Test
    public void testRemoveServerGroupSlave() throws Exception {
        testRemoveServerGroup(false, false);
    }

    @Test
    public void testRemoveServerGroupMasterRollback() throws Exception {
        testRemoveServerGroup(true, true);
    }

    @Test
    public void testRemoveServerGroupSlaveRollback() throws Exception {
        testRemoveServerGroup(false, true);
    }

    private void testRemoveServerGroup(boolean master, boolean rollback) throws Exception {
        PathAddress pa = PathAddress.pathAddress(PathElement.pathElement(SERVER_GROUP, "group-one"));
        final MockOperationContext operationContext = getOperationContext(rollback, pa);

        final ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).set(pa.toModelNode());
        operation.get(OP).set(REMOVE);

        ServerGroupRemoveHandler.INSTANCE.execute(operationContext, operation);

        Assert.assertFalse(operationContext.isReloadRequired());
    }

    @Test
    public void testUpdateServerGroupProfileMaster() throws Exception {
        testUpdateServerGroupProfile(true, false, false);
    }

    @Test
    public void testUpdateServerGroupProfileSlave() throws Exception {
        testUpdateServerGroupProfile(false, false, false);

    }

    @Test
    public void testUpdateServerGroupProfileMasterRollback() throws Exception {
        testUpdateServerGroupProfile(true, true, false);
    }

    @Test
    public void testUpdateServerGroupProfileSlaveRollback() throws Exception {
        testUpdateServerGroupProfile(false, true, false);
    }

    @Test(expected=OperationFailedException.class)
    public void testUpdateServerGroupBadProfileMaster() throws Exception {
        testUpdateServerGroupProfile(true, false, true);
    }

    @Test
    public void testUpdateServerGroupBadProfileSlave() throws Exception {
        testUpdateServerGroupProfile(false, false, true);
    }

    @Test(expected=OperationFailedException.class)
    public void testUpdateServerGroupBadProfileMasterRollback() throws Exception {
        testUpdateServerGroupProfile(true, true, true);
    }

    @Test
    public void testUpdateServerGroupBadProfileSlaveRollback() throws Exception {
        testUpdateServerGroupProfile(false, true, true);
    }

    private void testUpdateServerGroupProfile(boolean master, boolean rollback, boolean badProfile) throws Exception {
        PathAddress pa = PathAddress.pathAddress(PathElement.pathElement(SERVER_GROUP, "group-one"));
        final MockOperationContext operationContext = getOperationContext(rollback, pa);

        String profileName = badProfile ? "bad-profile" : "profile-two";

        final ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).set(pa.toModelNode());
        operation.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        operation.get(NAME).set(PROFILE);
        operation.get(VALUE).set(profileName);

        new ServerGroupProfileWriteAttributeHandler(master, null).execute(operationContext, operation);

        if (master && badProfile) {
            //master will throw an exception
            Assert.fail();
        }

        if (rollback) {
            Assert.assertFalse(operationContext.isReloadRequired());
        } else {
            if (badProfile) {
                Assert.assertTrue(operationContext.isReloadRequired());
            } else {
                Assert.assertFalse(operationContext.isReloadRequired());
            }
        }
    }

    @Test
    public void testUpdateServerGroupSocketBindingGroupMaster() throws Exception {
        testUpdateServerGroupSocketBindingGroup(true, false, false);
    }

    @Test
    public void testUpdateServerGroupSocketBindingGroupSlave() throws Exception {
        testUpdateServerGroupSocketBindingGroup(false, false, false);

    }

    @Test
    public void testUpdateServerGroupSocketBindingGroupMasterRollback() throws Exception {
        testUpdateServerGroupSocketBindingGroup(true, true, false);

    }

    @Test
    public void testUpdateServerGroupSocketBindingGroupSlaveRollback() throws Exception {
        testUpdateServerGroupSocketBindingGroup(false, true, false);
    }


    @Test(expected=OperationFailedException.class)
    public void testUpdateServerGroupBadSocketBindingGroupMaster() throws Exception {
        testUpdateServerGroupSocketBindingGroup(true, false, true);
    }

    @Test
    public void testUpdateServerGroupBadSocketBindingGroupSlave() throws Exception {
        testUpdateServerGroupSocketBindingGroup(false, false, true);
    }

    @Test(expected=OperationFailedException.class)
    public void testUpdateServerGroupBadSocketBindingGroupMasterRollback() throws Exception {
        testUpdateServerGroupSocketBindingGroup(true, true, true);
    }

    @Test
    public void testUpdateServerGroupBadSocketBindingGroupSlaveRollback() throws Exception {
        testUpdateServerGroupSocketBindingGroup(false, true, true);
    }

    private void testUpdateServerGroupSocketBindingGroup(boolean master, boolean rollback, boolean badSocketBindingGroup) throws Exception {

        PathAddress pa = PathAddress.pathAddress(PathElement.pathElement(SERVER_GROUP, "group-one"));
        final MockOperationContext operationContext = getOperationContext(rollback, pa);

        String socketBindingGroupName = badSocketBindingGroup ? "bad-group" : "binding-two";

        final ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).set(pa.toModelNode());
        operation.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
        operation.get(NAME).set(SOCKET_BINDING_GROUP);
        operation.get(VALUE).set(socketBindingGroupName);

        new ServerGroupSocketBindingGroupWriteAttributeHandler(master, null).execute(operationContext, operation);

        if (master && badSocketBindingGroup) {
            //master will throw an exception
            Assert.fail();
        }

        if (rollback) {
            Assert.assertFalse(operationContext.isReloadRequired());
        } else {
            if (badSocketBindingGroup) {
                Assert.assertTrue(operationContext.isReloadRequired());
            } else {
                Assert.assertFalse(operationContext.isReloadRequired());
            }
        }
    }

    MockOperationContext getOperationContext(final boolean rollback, final PathAddress operationAddress) {
        final Resource root = createRootResource();
        return new MockOperationContext(root, false, operationAddress, rollback);
    }

    private class MockOperationContext extends AbstractOperationTestCase.MockOperationContext {
        private boolean reloadRequired;
        private boolean rollback;
        private OperationStepHandler nextStep;

        protected MockOperationContext(final Resource root, final boolean booting, final PathAddress operationAddress, final boolean rollback) {
            super(root, booting, operationAddress);
            this.rollback = rollback;
        }

        public void completeStep(ResultHandler resultHandler) {
            if (nextStep != null) {
                stepCompleted();
            } else if (rollback) {
                resultHandler.handleResult(ResultAction.ROLLBACK, this, null);
            }
        }

        public void stepCompleted() {
            if (nextStep != null) {
                try {
                    OperationStepHandler step = nextStep;
                    nextStep = null;
                    step.execute(this, null);
                } catch (OperationFailedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void reloadRequired() {
            reloadRequired = true;
        }

        public boolean isReloadRequired() {
            return reloadRequired;
        }

        public void revertReloadRequired() {
            reloadRequired = false;
        }

        public void addStep(OperationStepHandler step, OperationContext.Stage stage) throws IllegalArgumentException {
            nextStep = step;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5632.java