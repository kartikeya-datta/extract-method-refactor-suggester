error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7165.java
text:
```scala
private static final S@@tring OP_PATTERN = "/profile=default/subsystem=io/worker=%s:%s";

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

package org.jboss.as.test.integration.domain.management.cli;

import java.io.IOException;

import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.test.integration.domain.management.util.DomainTestSupport;
import org.jboss.as.test.integration.domain.suites.CLITestSuite;
import org.jboss.as.test.integration.management.base.AbstractCliTestBase;
import org.jboss.as.test.integration.management.util.CLIOpResult;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests related to read ops for wildcard addresses.
 *
 * @author Brian Stansberry (c) 2013 Red Hat Inc.
 */
public class WildCardReadsTestCase extends AbstractCliTestBase {

    private static final String OP_PATTERN = "/profile=default/subsystem=infinispan/cache-container=web/distributed-cache=dist/eviction=%s:%s";
    private static final String READ_OP_DESC_OP = ModelDescriptionConstants.READ_OPERATION_DESCRIPTION_OPERATION + "(name=%s)";
    private static final String READ_RES_DESC_OP = ModelDescriptionConstants.READ_RESOURCE_DESCRIPTION_OPERATION + "(access-control=combined-descriptions,operations=true,recursive=true)";
    private static final String EVICTION = "EVICTION";

    @BeforeClass
    public static void setup() throws Exception {

        CLITestSuite.createSupport(WildCardReadsTestCase.class.getSimpleName());
        AbstractCliTestBase.initCLI(DomainTestSupport.masterAddress);
    }

    @AfterClass
    public static void cleanup() throws Exception {
        AbstractCliTestBase.closeCLI();
        CLITestSuite.stopSupport();
    }

    /**
     * Tests WFLY-2527 added behavior of supporting read-operation-description for
     * wildcard addresses where there is no generic resource registration for the type
     */
    @Test
    public void testLenientReadOperationDescription() throws IOException {
        cli.sendLine(String.format(OP_PATTERN, EVICTION, ModelDescriptionConstants.READ_OPERATION_NAMES_OPERATION));
        CLIOpResult opResult = cli.readAllAsOpResult();
        Assert.assertTrue(opResult.isIsOutcomeSuccess());
        for (ModelNode node : opResult.getResponseNode().get(ModelDescriptionConstants.RESULT).asList()) {
            String opPart = String.format(READ_OP_DESC_OP, node.asString());
            cli.sendLine(String.format(OP_PATTERN, EVICTION, opPart));
            opResult = cli.readAllAsOpResult();
            Assert.assertTrue(opResult.isIsOutcomeSuccess());
            ModelNode specific = opResult.getResponseNode().get(ModelDescriptionConstants.RESULT);
            cli.sendLine(String.format(OP_PATTERN, "*", opPart));
            opResult = cli.readAllAsOpResult();
            Assert.assertTrue(opResult.isIsOutcomeSuccess());
            Assert.assertEquals("mismatch for " + node.asString(), specific, opResult.getResponseNode().get(ModelDescriptionConstants.RESULT));
        }
    }

    /**
     * Tests WFLY-2527 fix.
     */
    @Test
    public void testReadResourceDescriptionNoGenericRegistration() throws IOException {
        cli.sendLine(String.format(OP_PATTERN, EVICTION, READ_RES_DESC_OP));
        CLIOpResult opResult = cli.readAllAsOpResult();
        Assert.assertTrue(opResult.isIsOutcomeSuccess());
        ModelNode specific = opResult.getResponseNode().get(ModelDescriptionConstants.RESULT);
        cli.sendLine(String.format(OP_PATTERN, "*", READ_RES_DESC_OP));
        opResult = cli.readAllAsOpResult();
        Assert.assertTrue(opResult.isIsOutcomeSuccess());
        ModelNode generic = opResult.getResponseNode().get(ModelDescriptionConstants.RESULT);
        Assert.assertEquals(ModelType.LIST, generic.getType());
        Assert.assertEquals(1, generic.asInt());
        Assert.assertEquals(specific, generic.get(0).get(ModelDescriptionConstants.RESULT));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7165.java