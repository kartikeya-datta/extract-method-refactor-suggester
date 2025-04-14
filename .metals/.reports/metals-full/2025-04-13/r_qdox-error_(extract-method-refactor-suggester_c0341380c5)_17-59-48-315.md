error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13129.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13129.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13129.java
text:
```scala
a@@ssertRemoveSubsystemResources(servicesC, getIgnoredChildResourcesForRemovalTest());

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

package org.jboss.as.subsystem.test;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.junit.Test;

/**
 * A test routine every subsystem should go through.
 *
 * @author Emanuel Muckenhuber
 */
public abstract class AbstractSubsystemBaseTest extends AbstractSubsystemTest {

    public AbstractSubsystemBaseTest(final String mainSubsystemName, final Extension mainExtension) {
        super(mainSubsystemName, mainExtension);
    }

    public AbstractSubsystemBaseTest(final String mainSubsystemName, final Extension mainExtension, final Comparator<PathAddress> removeOrderComparator) {
        super(mainSubsystemName, mainExtension, removeOrderComparator);
    }

    /**
     * Get the subsystem xml as string.
     *
     * @return the subsystem xml
     * @throws IOException
     */
    protected abstract String getSubsystemXml() throws IOException;


    /**
     * Get the subsystem xml with the given id as a string.
     * <p>
     * This default implementation returns the result of a call to {@link #readResource(String)}.
     * </p>
     *
     * @param configId the id of the xml configuration
     *
     * @return the subsystem xml
     * @throws IOException
     */
    protected String getSubsystemXml(String configId) throws IOException {
        return readResource(configId);
    }

    @Test
    public void testSubsystem() throws Exception {
        standardSubsystemTest(null);
    }

    /**
     * Tests the ability to create a model from an xml configuration, marshal the model back to xml,
     * re-read that marshalled model into a new model that matches the first one, execute a "describe"
     * operation for the model, create yet another model from executing the results of that describe
     * operation, and compare that model to first model.
     *
     * @param configId  id to pass to {@link #getSubsystemXml(String)} to get the configuration; if {@code null}
     *                  {@link #getSubsystemXml()} will be called
     *
     * @throws Exception
     */
    protected void standardSubsystemTest(final String configId) throws Exception {
        standardSubsystemTest(configId, true);
    }

    /**
     * Tests the ability to create a model from an xml configuration, marshal the model back to xml,
     * re-read that marshalled model into a new model that matches the first one, execute a "describe"
     * operation for the model, create yet another model from executing the results of that describe
     * operation, and compare that model to first model.
     *
     * @param configId  id to pass to {@link #getSubsystemXml(String)} to get the configuration; if {@code null}
     *                  {@link #getSubsystemXml()} will be called
     *
     * @param compareXml if {@code true} a comparison of xml output to original input is performed. This can be
     *                   set to {@code false} if the original input is from an earlier xsd and the current
     *                   schema has a different output
     *
     * @throws Exception
     */
    protected void standardSubsystemTest(final String configId, boolean compareXml) throws Exception {
        final AdditionalInitialization additionalInit = createAdditionalInitialization();

        // Parse the subsystem xml and install into the first controller
        final String subsystemXml = configId == null ? getSubsystemXml() : getSubsystemXml(configId);
        final KernelServices servicesA = super.installInController(additionalInit, subsystemXml);
        Assert.assertNotNull(servicesA);
        //Get the model and the persisted xml from the first controller
        final ModelNode modelA = servicesA.readWholeModel();
        validateModel(modelA);

        // Test marshaling
        final String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();


        // validate the the normalized xmls
        String normalizedSubsystem = normalizeXML(subsystemXml);

        if (compareXml) {
            compareXml(configId, normalizedSubsystem, normalizeXML(marshalled));
        }

        //Install the persisted xml from the first controller into a second controller
        final KernelServices servicesB = super.installInController(additionalInit, marshalled);
        final ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        compare(modelA, modelB);

        // Test the describe operation
        final ModelNode operation = createDescribeOperation();
        final ModelNode result = servicesB.executeOperation(operation);
        Assert.assertTrue("the subsystem describe operation has to generate a list of operations to recreate the subsystem",
                !result.hasDefined(ModelDescriptionConstants.FAILURE_DESCRIPTION));
        final List<ModelNode> operations = result.get(ModelDescriptionConstants.RESULT).asList();
        servicesB.shutdown();

        final KernelServices servicesC = super.installInController(additionalInit, operations);
        final ModelNode modelC = servicesC.readWholeModel();

        compare(modelA, modelC);

        assertRemoveSubsystemResources(servicesA, getIgnoredChildResourcesForRemovalTest());
    }

    protected void validateModel(ModelNode model) {
        Assert.assertNotNull(model);
    }

    protected ModelNode createDescribeOperation() {
        final ModelNode address = new ModelNode();
        address.add(ModelDescriptionConstants.SUBSYSTEM, getMainSubsystemName());

        final ModelNode operation = new ModelNode();
        operation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.DESCRIBE);
        operation.get(ModelDescriptionConstants.OP_ADDR).set(address);
        return operation;
    }

    protected AdditionalInitialization createAdditionalInitialization() {
        return AdditionalInitialization.MANAGEMENT;
    }

    /**
     * Returns a set of child resources addresses that should not be removed directly. Rather they should be managed
     * by their parent resource
     *
     * @return the set of child resource addresses
     * @see AbstractSubsystemTest#assertRemoveSubsystemResources(KernelServices, Set)
     */
    protected Set<PathAddress> getIgnoredChildResourcesForRemovalTest() {
        return Collections.<PathAddress>emptySet();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13129.java