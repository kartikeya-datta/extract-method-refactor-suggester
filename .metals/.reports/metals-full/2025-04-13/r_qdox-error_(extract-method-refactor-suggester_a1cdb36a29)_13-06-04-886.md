error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4057.java
text:
```scala
A@@ssert.fail("Should have failed on no required parameter included");

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
package org.jboss.as.test.integration.domain.suites;

import java.io.IOException;

import junit.framework.Assert;
import org.jboss.as.test.integration.management.base.AbstractMgmtTestBase;
import org.jboss.as.test.integration.management.util.MgmtOperationException;
import org.jboss.as.test.integration.management.util.ModelUtil;
import org.jboss.dmr.ModelNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_OPERATION_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALIDATE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;

/**
 * Tests that the validate-operation operation works as it should
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class ValidateOperationOperationTestCase extends AbstractMgmtTestBase {


    private static final String MASTER = "master";
    private static final String SLAVE = "slave";
    private static final String MASTER_SERVER = "main-one";
    private static final String SLAVE_SERVER = "main-three";

    @BeforeClass
    public static void initClient() throws Exception {
        initModelControllerClient("127.0.0.1", 9999);
    }

    @AfterClass
    public static void closeClient() throws Exception {
        closeModelControllerClient();
    }

    @Test
    public void testValidRootOperation() throws IOException, MgmtOperationException {
        ModelNode op = ModelUtil.createOpNode(null, READ_OPERATION_DESCRIPTION_OPERATION);
        op.get(NAME).set("Doesn't matter");
        executeOperation(createValidateOperation(op));
    }

    @Test
    public void testFailedRootOperation() throws IOException {
        ModelNode op = ModelUtil.createOpNode(null, READ_OPERATION_DESCRIPTION_OPERATION);
        executeInvalidOperation(op);
    }

    @Test
    public void testValidDcOperation() throws IOException, MgmtOperationException {
        ModelNode op = ModelUtil.createOpNode("profile=default/subsystem=jmx/remoting-connector=jmx", ADD);
        executeOperation(createValidateOperation(op));
    }

    @Test
    public void testInvalidDcOperation() throws IOException {
        ModelNode op = ModelUtil.createOpNode("profile=default/subsystem=jmx/remoting-connector=jmx", ADD);
        op.get("badata").set("junk");
        executeInvalidOperation(op);
    }

    @Test
    public void testValidMasterHcOperation() throws IOException, MgmtOperationException {
        testValidHcOperation(MASTER);
    }

    @Test
    public void testValidSlaveHcOperation() throws IOException, MgmtOperationException {
        testValidHcOperation(SLAVE);
    }

    public void testValidHcOperation(String host) throws IOException, MgmtOperationException {
        ModelNode op = ModelUtil.createOpNode("host=" + host + "/path=ff", ADD);
        op.get("path").set("/home");
        executeOperation(createValidateOperation(op));
    }

    @Test
    public void testInvalidMasterHcOperation() throws IOException {
        testInvalidHcOperation(MASTER);
    }

    @Test
    public void testInvalidSlaveHcOperation() throws IOException {
        testInvalidHcOperation(SLAVE);
    }

    private void testInvalidHcOperation(String host) throws IOException {
        ModelNode op = ModelUtil.createOpNode("host=" + host + "/path=ff", ADD);
        executeInvalidOperation(op);
    }

    @Test
    public void testValidMasterHcServerOperation() throws IOException, MgmtOperationException {
        testValidServerOperation(MASTER, MASTER_SERVER);
    }

    @Test
    public void testValidSlaveHcServerOperation() throws IOException, MgmtOperationException {
        testValidServerOperation(SLAVE, SLAVE_SERVER);
    }

    private void testValidServerOperation(String host, String server) throws IOException, MgmtOperationException {
        ModelNode op = ModelUtil.createOpNode("host=" + host + "/server=" + server + "/subsystem=jmx/remoting-connector=jmx", ADD);
        executeOperation(createValidateOperation(op));
    }

    @Test
    public void testInvalidMasterHcServerOperation() throws IOException, MgmtOperationException {
        testInvalidServerOperation(MASTER, MASTER_SERVER);
    }

    @Test
    public void testInvalidSlaveHcServerOperation() throws IOException, MgmtOperationException {
        testInvalidServerOperation(SLAVE, SLAVE_SERVER);
    }

    private void testInvalidServerOperation(String host, String server) throws IOException, MgmtOperationException {
        ModelNode op = ModelUtil.createOpNode("host=" + host + "/server=" + server + "/subsystem=jmx/remoting-connector=jmx", ADD);
        op.get("badata").set("junk");
        executeInvalidOperation(op);
    }

    private ModelNode createValidateOperation(ModelNode validatedOperation) throws IOException {
        ModelNode node = ModelUtil.createOpNode(null, VALIDATE_OPERATION);
        node.get(VALUE).set(validatedOperation);
        return node;
    }

    private void executeInvalidOperation(ModelNode operation) throws IOException {
        try {
            executeOperation(createValidateOperation(operation));
            Assert.fail("Should have failed on no required paramter included");
        } catch (MgmtOperationException expected) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4057.java