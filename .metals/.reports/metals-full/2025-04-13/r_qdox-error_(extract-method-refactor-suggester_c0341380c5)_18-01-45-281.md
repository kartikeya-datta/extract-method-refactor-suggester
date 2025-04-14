error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12705.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12705.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12705.java
text:
```scala
a@@ction="traceln(\"resolved value = \" + org.jboss.as.clustering.jgroups.subsystem.PropertyResourceDefinition.VALUE.resolveModelAttribute(context,model))")

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
package org.jboss.as.clustering.jgroups.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.model.test.FailedOperationTransformationConfig;
import org.jboss.as.model.test.ModelTestControllerVersion;
import org.jboss.as.model.test.ModelTestUtils;
import org.jboss.as.subsystem.test.AdditionalInitialization;
import org.jboss.as.subsystem.test.KernelServices;
import org.jboss.as.subsystem.test.KernelServicesBuilder;
import org.jboss.byteman.contrib.bmunit.BMRule;
import org.jboss.byteman.contrib.bmunit.BMUnitRunner;
import org.jboss.dmr.ModelNode;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test cases for transformers used in the JGroups subsystem.
 *
 * @author <a href="tomaz.cerar@redhat.com">Tomaz Cerar</a>
 * @author Richard Achmatowicz (c) 2011 Red Hat Inc.
 */

@RunWith(BMUnitRunner.class)
public class JGroupsSubsystemTransformerTestCase extends OperationTestCaseBase {

    protected String getSubsystemXml() throws IOException {
        return "jgroups-transformer_1_1.xml";
    }

    @Test
    public void testTransformerAS712() throws Exception {
        testTransformer_1_1_0(ModelTestControllerVersion.V7_1_2_FINAL);
    }

    @Test
    public void testTransformerAS713() throws Exception {
        testTransformer_1_1_0(ModelTestControllerVersion.V7_1_3_FINAL);
    }

    /**
     * Tests transformation of model from 1.1.1 version into 1.1.0 version.
     *
     * This test does not pass because of an error in the 1.1.0 model:
     * The model entry 'protocols' in /subsystem=jgroups/stack=* is used
     * to store an ordered list of protocol names, but it is not registered
     * as an attribute, where as it is registered in the 1.1.1 model.
     * This breaks the test.
     *
     * @throws Exception
     */
    private void testTransformer_1_1_0(ModelTestControllerVersion controllerVersion) throws Exception {
        ModelVersion version = ModelVersion.create(1, 1, 0);
        KernelServicesBuilder builder = createKernelServicesBuilder(AdditionalInitialization.MANAGEMENT)
                .setSubsystemXmlResource(getSubsystemXml());
        builder.createLegacyKernelServicesBuilder(null, controllerVersion, version)
                .addMavenResourceURL("org.jboss.as:jboss-as-clustering-jgroups:" + controllerVersion.getMavenGavVersion());

        KernelServices mainServices = builder.build();
        Assert.assertTrue(mainServices.isSuccessfulBoot());
        Assert.assertTrue(mainServices.getLegacyServices(version).isSuccessfulBoot());
        checkSubsystemModelTransformation(mainServices, version);
    }

    /**
     * Tests resolution of property expressions during performRuntime()
     *
     * This test uses Byteman to inject code into AbstractAddStepHandler.performRuntime() to
     * resolve the value of an expression and check that expression resolution is working as expected.
     *
     * The test is currently broken due to an outstanding class loading problem with Byteman, but it is included
     * here for re-enabling when the issue is resolved.
     *
     * @throws Exception
     */
    @Ignore
    @Test
    @BMRule(name="Test support for expression resolution",
            targetClass="^org.jboss.as.controller.AbstractAddStepHandler",
            targetMethod="performRuntime",
            targetLocation="AT ENTRY",
            binding="context:OperationContext = $1; operation:ModelNode = $2; model:ModelNode = $3",
            condition="operation.hasDefined(\"name\") AND operation.hasDefined(\"value\")",
            action="traceln(\"resolved value = \" + org.jboss.as.clustering.jgroups.subsystem.PropertyResource.VALUE.resolveModelAttribute(context,model))")
    public void testProtocolStackPropertyResolve() throws Exception {

        // Parse and install the XML into the controller
        String subsystemXml =  getSubsystemXml() ;
        KernelServices services = createKernelServicesBuilder(null).setSubsystemXmlResource(subsystemXml).build();

        // set a property to have an expression and let Byteman intercept the performRuntime call

        // build an ADD command to add a transport property using expression value
        ModelNode operation = getTransportPropertyAddOperation("maximal", "bundler_type", "${the_bundler_type:new}");

        // perform operation on the 1.1.1 model
        ModelNode mainResult = services.executeOperation(operation);
        assertEquals(mainResult.toJSONString(true), SUCCESS, mainResult.get(OUTCOME).asString());
    }

    @Test
    public void testRejectExpressionsAS712() throws Exception {
        testRejectExpressions_1_1_0(ModelTestControllerVersion.V7_1_2_FINAL);
    }

    @Test
    public void testRejectExpressionsAS713() throws Exception {
        testRejectExpressions_1_1_0(ModelTestControllerVersion.V7_1_3_FINAL);
    }

    /**
     * Tests rejection of expressions in 1.1.0 model.
     *
     * @throws Exception
     */
    private void testRejectExpressions_1_1_0(ModelTestControllerVersion controllerVersion) throws Exception {
        // create builder for current subsystem version
        KernelServicesBuilder builder = createKernelServicesBuilder(AdditionalInitialization.MANAGEMENT);

        // create builder for legacy subsystem version
        ModelVersion version_1_1_0 = ModelVersion.create(1, 1, 0);
        builder.createLegacyKernelServicesBuilder(null, controllerVersion, version_1_1_0)
            .addMavenResourceURL("org.jboss.as:jboss-as-clustering-jgroups:" + controllerVersion.getMavenGavVersion())
            //TODO storing the model triggers the weirdness mentioned in SubsystemTestDelegate.LegacyKernelServiceInitializerImpl.install()
            //which is strange since it should be loading it all from the current jboss modules
            //Also this works in several other tests
            .dontPersistXml();

        KernelServices mainServices = builder.build();
        Assert.assertTrue(mainServices.isSuccessfulBoot());
        KernelServices legacyServices = mainServices.getLegacyServices(version_1_1_0);
        Assert.assertNotNull(legacyServices);
        Assert.assertTrue(legacyServices.isSuccessfulBoot());

        //Use the real xml with expressions for testing all the attributes
        PathAddress subsystemAddress = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, JGroupsExtension.SUBSYSTEM_NAME));
        ModelTestUtils.checkFailedTransformedBootOperations(
                mainServices,
                version_1_1_0,
                builder.parseXmlResource("subsystem-jgroups-test.xml"),
                new FailedOperationTransformationConfig()
                    .addFailedAttribute(
                            subsystemAddress.append(PathElement.pathElement("stack"))
                                    .append(PathElement.pathElement("transport")),
                            new FailedOperationTransformationConfig.RejectExpressionsConfig(ModelKeys.SHARED))
                    .addFailedAttribute(
                            subsystemAddress.append(PathElement.pathElement("stack"))
                                .append(PathElement.pathElement("transport"))
                                .append("property"),
                            new FailedOperationTransformationConfig.RejectExpressionsConfig(VALUE))
                    .addFailedAttribute(
                            subsystemAddress.append(PathElement.pathElement("stack"))
                                .append(PathElement.pathElement("protocol"))
                                .append("property"),
                            new FailedOperationTransformationConfig.RejectExpressionsConfig(VALUE)));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12705.java