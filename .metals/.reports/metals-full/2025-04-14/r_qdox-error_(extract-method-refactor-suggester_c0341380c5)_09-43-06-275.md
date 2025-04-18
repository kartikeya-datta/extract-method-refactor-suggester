error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3129.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3129.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3129.java
text:
```scala
b@@uilder.createLegacyKernelServicesBuilder(null, controllerVersion, modelVersion)

/*
 *
 *  JBoss, Home of Professional Open Source.
 *  Copyright 2013, Red Hat, Inc., and individual contributors
 *  as indicated by the @author tags. See the copyright.txt file in the
 *  distribution for a full listing of individual contributors.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * /
 */

package org.jboss.as.cmp.subsystem;

import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.BLOCK_SIZE;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.CREATE_TABLE;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.CREATE_TABLE_DDL;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.DATA_SOURCE;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.DROP_TABLE;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.HILO_KEY_GENERATOR;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.ID_COLUMN;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.JNDI_NAME;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.SEQUENCE_COLUMN;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.SEQUENCE_NAME;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.TABLE_NAME;
import static org.jboss.as.cmp.subsystem.CmpSubsystemModel.UUID_KEY_GENERATOR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.model.test.FailedOperationTransformationConfig;
import org.jboss.as.model.test.ModelTestControllerVersion;
import org.jboss.as.model.test.ModelTestUtils;
import org.jboss.as.subsystem.test.KernelServices;
import org.jboss.as.subsystem.test.KernelServicesBuilder;
import org.jboss.dmr.ModelNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel Fehlhammer
 */
public class CmpKeyGeneratorSubsystem11TestCase extends CmpKeyGeneratorSubsystem10TestCase {

    private static final PathAddress SUBSYSTEM_PATH = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, CmpExtension.SUBSYSTEM_NAME));

    @Override
    protected String getSubsystemXml() throws IOException {
        return readResource("subsystem-cmp-key-generators_1_1.xml");
    }

    @Override
    protected KernelServices standardSubsystemTest(String configId, boolean compareXml) throws Exception {
        return super.standardSubsystemTest(configId, true);
    }

    @Test
    public void testParseSubsystem() throws Exception {
        final List<ModelNode> operations = super.parse(getSubsystemXml("subsystem-cmp-key-generators_1_1.xml"));
        assertEquals(5, operations.size());
        assertOperation(operations.get(0), ADD, PathElement.pathElement(SUBSYSTEM, getMainSubsystemName()));
        assertOperation(operations.get(1), ADD, PathElement.pathElement(UUID_KEY_GENERATOR, "uuid1"));
        final ModelNode uuid2 = operations.get(2);
        assertOperation(uuid2, ADD, PathElement.pathElement(UUID_KEY_GENERATOR, "uuid2"));
        assertEquals("java:jboss/uuid2", uuid2.get(JNDI_NAME).asString());

        final ModelNode hilo1 = operations.get(3);
        assertOperation(hilo1, ADD, PathElement.pathElement(HILO_KEY_GENERATOR, "hilo1"));
        assertEquals("java:/jdbc/DB1", hilo1.get(DATA_SOURCE).asString());
        assertEquals("HILOSEQUENCES1", hilo1.get(TABLE_NAME).asString());
        assertEquals("SEQUENCENAME1", hilo1.get(SEQUENCE_COLUMN).asString());
        assertEquals("HIGHVALUES1", hilo1.get(ID_COLUMN).asString());
        assertEquals("create table HILOSEQUENCES1", hilo1.get(CREATE_TABLE_DDL).asString());
        assertEquals("general1", hilo1.get(SEQUENCE_NAME).asString());
        assertEquals(true, hilo1.get(CREATE_TABLE).asBoolean());
        assertEquals(true, hilo1.get(DROP_TABLE).asBoolean());
        assertEquals(10, hilo1.get(BLOCK_SIZE).asLong());

        final ModelNode hilo2 = operations.get(4);
        assertOperation(hilo2, ADD, PathElement.pathElement(HILO_KEY_GENERATOR, "hilo2"));
        assertEquals("java:jboss/hilo2", hilo2.get(JNDI_NAME).asString());
        assertEquals("java:/jdbc/DB2", hilo2.get(DATA_SOURCE).asString());
        assertEquals("HILOSEQUENCES2", hilo2.get(TABLE_NAME).asString());
        assertEquals("SEQUENCENAME2", hilo2.get(SEQUENCE_COLUMN).asString());
        assertEquals("HIGHVALUES2", hilo2.get(ID_COLUMN).asString());
        assertEquals("create table HILOSEQUENCES2", hilo2.get(CREATE_TABLE_DDL).asString());
        assertEquals("general2", hilo2.get(SEQUENCE_NAME).asString());
        assertEquals(false, hilo2.get(CREATE_TABLE).asBoolean());
        assertEquals(false, hilo2.get(DROP_TABLE).asBoolean());
        assertEquals(11, hilo2.get(BLOCK_SIZE).asLong());
    }

    private void assertOperation(final ModelNode operation, final String operationName, final PathElement lastElement) {
        assertEquals(operationName, operation.get(OP).asString());
        final PathAddress addr = PathAddress.pathAddress(operation.get(OP_ADDR));
        final PathElement element = addr.getLastElement();
        assertEquals(lastElement.getKey(), element.getKey());
        assertEquals(lastElement.getValue(), element.getValue());
    }

    @Test
    public void testTransformers_7_1_2() throws Exception {
        testTransformers_1_0_0(ModelTestControllerVersion.V7_1_2_FINAL);
    }

    @Test
    public void testTransformers_7_1_3() throws Exception {
        testTransformers_1_0_0(ModelTestControllerVersion.V7_1_3_FINAL);
    }

    private void testTransformers_1_0_0(ModelTestControllerVersion controllerVersion) throws Exception {
        ModelVersion modelVersion = ModelVersion.create(1, 0, 0);
        KernelServicesBuilder builder = createKernelServicesBuilder(createAdditionalInitialization());

        builder.createLegacyKernelServicesBuilder(createAdditionalInitialization(), controllerVersion, modelVersion)
                .addMavenResourceURL("org.jboss.as:jboss-as-cmp:" + controllerVersion.getMavenGavVersion())
                .configureReverseControllerCheck(createAdditionalInitialization(), null);

        KernelServices mainServices = builder.build();
        Assert.assertTrue(mainServices.isSuccessfulBoot());
        Assert.assertTrue(mainServices.getLegacyServices(modelVersion).isSuccessfulBoot());

        ModelTestUtils.checkFailedTransformedBootOperations(mainServices, modelVersion, parse(getSubsystemXml()),
                new FailedOperationTransformationConfig()
                        .addFailedAttribute(SUBSYSTEM_PATH.append(CmpSubsystemModel.UUID_KEY_GENERATOR_PATH),
                                new FailedOperationTransformationConfig.NewAttributesConfig(CMPSubsystemRootResourceDefinition.JNDI_NAME))
                        .addFailedAttribute(SUBSYSTEM_PATH.append(CmpSubsystemModel.HILO_KEY_GENERATOR_PATH),
                                new FailedOperationTransformationConfig.NewAttributesConfig(CMPSubsystemRootResourceDefinition.JNDI_NAME))

        );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3129.java