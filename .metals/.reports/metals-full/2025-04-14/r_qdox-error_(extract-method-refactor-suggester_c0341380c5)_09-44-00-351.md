error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9742.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9742.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9742.java
text:
```scala
r@@eturn Transformers.Factory.create(target, resourceRoot, resourceRegistration, ExpressionResolver.DEFAULT, RunningMode.NORMAL, ProcessType.STANDALONE_SERVER);

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

package org.jboss.as.controller.transform.description;

import java.util.Collections;

import org.jboss.as.controller.ExpressionResolver;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.NonResolvingResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.OperationTransformer.TransformedOperation;
import org.jboss.as.controller.transform.ResourceTransformationContext;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.as.controller.transform.TransformationTarget;
import org.jboss.as.controller.transform.TransformationTargetImpl;
import org.jboss.as.controller.transform.TransformerRegistry;
import org.jboss.as.controller.transform.Transformers;
import org.jboss.as.controller.transform.TransformersSubRegistration;
import org.jboss.dmr.ModelNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class RecursiveDiscardAndRemoveTestCase {

    private static PathElement PATH = PathElement.pathElement("toto", "testSubsystem");
    private static PathElement DISCARDED_WILDCARD = PathElement.pathElement("removeall");
    private static PathElement DISCARDED_WILDCARD_ENTRY = PathElement.pathElement("removeall", "entry");
    private static PathElement DISCARDED_SPECIFIC = PathElement.pathElement("remove", "one");
    private static PathElement DISCARDED_CHILD = PathElement.pathElement("child", "one");

    private Resource resourceRoot = Resource.Factory.create();
    private TransformerRegistry registry = TransformerRegistry.Factory.create(null);
    private ManagementResourceRegistration resourceRegistration = ManagementResourceRegistration.Factory.create(ROOT);
    private TransformersSubRegistration transformersSubRegistration;
    private ModelNode resourceModel;

    @Before
    public void setUp() {
        // Cleanup
        resourceRoot = Resource.Factory.create();
        registry = TransformerRegistry.Factory.create(null);
        resourceRegistration = ManagementResourceRegistration.Factory.create(ROOT);
        // test
        final Resource toto = Resource.Factory.create();
        resourceRoot.registerChild(PATH, toto);
        resourceModel = toto.getModel();
        resourceModel.get("subs").set("test");

        Resource wildcard = Resource.Factory.create();
        toto.registerChild(DISCARDED_WILDCARD_ENTRY, wildcard);
        wildcard.getModel().get("wild").set("test");

        Resource wildcardChild = Resource.Factory.create();
        wildcard.registerChild(DISCARDED_CHILD, wildcardChild);
        wildcardChild.getModel().get("wildchild").set("test");

        Resource specific = Resource.Factory.create();
        toto.registerChild(DISCARDED_SPECIFIC, specific);
        specific.getModel().get("spec").set("test");

        Resource specificChild = Resource.Factory.create();
        specific.registerChild(DISCARDED_CHILD, specificChild);
        specificChild.getModel().get("specchild").set("test");

        // Register the description
        transformersSubRegistration = registry.getServerRegistration(ModelVersion.create(1));
    }


    @Test
    public void testDiscardResource() throws Exception {
        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.discardChildResource(DISCARDED_WILDCARD);
        builder.discardChildResource(DISCARDED_SPECIFIC);
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        //Make sure the resource has none of the discarded children
        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertEquals(1, model.keys().size());
        Assert.assertEquals("test", model.get("subs").asString());

        //Sanity check that the subsystem works
        sanityTestNonDiscardedResource();

        //Check that the op gets discarded for the wildcard entry
        discardResourceOperationsTest(PathAddress.pathAddress(PATH, DISCARDED_WILDCARD_ENTRY));

        //Check that the op gets discarded for the specific entry
        discardResourceOperationsTest(PathAddress.pathAddress(PATH, DISCARDED_SPECIFIC));

        //Check that the op gets discarded for the wildcard entry child
        discardResourceOperationsTest(PathAddress.pathAddress(PATH, DISCARDED_WILDCARD_ENTRY, DISCARDED_CHILD));

        //Check that the op gets discarded for the specific entry child
        discardResourceOperationsTest(PathAddress.pathAddress(PATH, DISCARDED_SPECIFIC, DISCARDED_CHILD));
    }

    @Test
    public void testRejectResource() throws Exception {
        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.rejectChildResource(DISCARDED_WILDCARD);
        builder.rejectChildResource(DISCARDED_SPECIFIC);
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        //Make sure the resource has none of the discarded children
        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertEquals(1, model.keys().size());
        Assert.assertEquals("test", model.get("subs").asString());

        //Sanity check that the subsystem works
        sanityTestNonDiscardedResource();

        //Check that the op gets rejected for the wildcard entry
        rejectResourceOperationsTest(PathAddress.pathAddress(PATH, DISCARDED_WILDCARD_ENTRY));

        //Check that the op gets rejected for the specific entry
        rejectResourceOperationsTest(PathAddress.pathAddress(PATH, DISCARDED_SPECIFIC));

        //Check that the op gets rejected for the wildcard entry child
        rejectResourceOperationsTest(PathAddress.pathAddress(PATH, DISCARDED_WILDCARD_ENTRY, DISCARDED_CHILD));

        //Check that the op gets rejected for the specific entry child
        rejectResourceOperationsTest(PathAddress.pathAddress(PATH, DISCARDED_SPECIFIC, DISCARDED_CHILD));
    }

    private void sanityTestNonDiscardedResource() throws Exception {
        ModelNode op = Util.createAddOperation(PathAddress.pathAddress(PATH));
        TransformedOperation transformed = transformOperation(op);
        Assert.assertEquals(op, transformed.getTransformedOperation());
        Assert.assertFalse(transformed.rejectOperation(success()));
        Assert.assertNull(transformed.getFailureDescription());

        op = Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "test", new ModelNode("a"));
        transformed = transformOperation(op);
        Assert.assertEquals(op, transformed.getTransformedOperation());
        Assert.assertFalse(transformed.rejectOperation(success()));
        Assert.assertNull(transformed.getFailureDescription());

        op = Util.getUndefineAttributeOperation(PathAddress.pathAddress(PATH), "test");
        transformed = transformOperation(op);
        Assert.assertEquals(op, transformed.getTransformedOperation());
        Assert.assertFalse(transformed.rejectOperation(success()));
        Assert.assertNull(transformed.getFailureDescription());
    }

    private void discardResourceOperationsTest(PathAddress pathAddress) throws Exception {
        ModelNode op = Util.createAddOperation(pathAddress);
        TransformedOperation transformed = transformOperation(op);
        assertDiscarded(transformed);

        op = Util.getWriteAttributeOperation(pathAddress, "test", new ModelNode("a"));
        transformed = transformOperation(op);
        assertDiscarded(transformed);

        op = Util.getUndefineAttributeOperation(pathAddress, "test");
        transformed = transformOperation(op);
        assertDiscarded(transformed);
    }

    private void rejectResourceOperationsTest(PathAddress pathAddress) throws Exception {
        ModelNode op = Util.createAddOperation(pathAddress);
        TransformedOperation transformed = transformOperation(op);
        assertRejected(op, transformed);

        op = Util.getWriteAttributeOperation(pathAddress, "test", new ModelNode("a"));
        transformed = transformOperation(op);
        assertRejected(op, transformed);

        op = Util.getUndefineAttributeOperation(pathAddress, "test");
        transformed = transformOperation(op);
        assertRejected(op, transformed);
    }

    private Resource transformResource() throws OperationFailedException {
        final TransformationTarget target = create(registry, ModelVersion.create(1));
        final ResourceTransformationContext context = createContext(target);
        return getTransfomers(target).transformResource(context, resourceRoot);
    }

    private OperationTransformer.TransformedOperation transformOperation(final ModelNode operation) throws OperationFailedException {
        final TransformationTarget target = create(registry, ModelVersion.create(1));
        final TransformationContext context = createContext(target);
        return getTransfomers(target).transformOperation(context, operation);
    }

    private ResourceTransformationContext createContext(final TransformationTarget target) {
        return Transformers.Factory.create(target, resourceRoot, resourceRegistration, ExpressionResolver.TEST_RESOLVER, RunningMode.NORMAL, ProcessType.STANDALONE_SERVER);
    }

    private Transformers getTransfomers(final TransformationTarget target) {
        return Transformers.Factory.create(target);
    }

    protected TransformationTarget create(final TransformerRegistry registry, ModelVersion version) {
        return create(registry, version, TransformationTarget.TransformationTargetType.SERVER);
    }

    protected TransformationTarget create(final TransformerRegistry registry, ModelVersion version, TransformationTarget.TransformationTargetType type) {
        return TransformationTargetImpl.create(registry, version, Collections.<PathAddress, ModelVersion>emptyMap(), null, type, null);
    }

    private static final ResourceDefinition ROOT = new SimpleResourceDefinition(PathElement.pathElement("test"), new NonResolvingResourceDescriptionResolver());

    protected void assertRejected(final ModelNode original, final TransformedOperation transformed) {
        Assert.assertNotNull(transformed);
        final ModelNode operation = transformed.getTransformedOperation();
        Assert.assertNotNull(operation);
        Assert.assertEquals(original, operation);
        Assert.assertTrue(original.get(ModelDescriptionConstants.OP_ADDR).equals(operation.get(ModelDescriptionConstants.OP_ADDR)));
        Assert.assertTrue(transformed.rejectOperation(success()));
        Assert.assertNotNull(transformed.getFailureDescription());
    }

    private void assertDiscarded(final TransformedOperation transformed) {
        Assert.assertNull(transformed.getTransformedOperation());
        Assert.assertFalse(transformed.rejectOperation(success()));
        Assert.assertNull(transformed.getFailureDescription());
    }

    private static final ModelNode success() {
        final ModelNode result = new ModelNode();
        result.get(ModelDescriptionConstants.OUTCOME).set(ModelDescriptionConstants.SUCCESS);
        result.get(ModelDescriptionConstants.RESULT);
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9742.java