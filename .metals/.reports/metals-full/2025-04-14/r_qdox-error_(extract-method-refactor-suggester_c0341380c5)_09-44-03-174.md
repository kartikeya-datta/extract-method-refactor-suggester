error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13056.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13056.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1055
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13056.java
text:
```scala
public class BasicTestCase {

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

p@@ackage org.jboss.as.controller.transform.description;

import java.util.Collections;
import java.util.Locale;

import junit.framework.Assert;

import org.jboss.as.controller.ExpressionResolver;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.ResourceTransformationContext;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.as.controller.transform.TransformationTarget;
import org.jboss.as.controller.transform.TransformationTargetImpl;
import org.jboss.as.controller.transform.TransformerRegistry;
import org.jboss.as.controller.transform.Transformers;
import org.jboss.as.controller.transform.TransformersSubRegistration;
import org.jboss.dmr.ModelNode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Emanuel Muckenhuber
 */
public class BasicTestsCase {

    private static PathElement PATH = PathElement.pathElement("toto", "testSubsystem");
    private static PathElement DISCARD = PathElement.pathElement("discard");

    private static PathElement CONFIGURATION_TEST = PathElement.pathElement("configuration", "test");
    private static PathElement TEST_CONFIGURATION = PathElement.pathElement("test", "configuration");

    private static PathElement SETTING_DIRECTORY = PathElement.pathElement("setting", "directory");
    private static PathElement DIRECTORY_SETTING = PathElement.pathElement("directory", "setting");

    private Resource resourceRoot = Resource.Factory.create();
    private TransformerRegistry registry = TransformerRegistry.Factory.create(null);
    private ManagementResourceRegistration resourceRegistration = ManagementResourceRegistration.Factory.create(ROOT);

    private static final TransformationDescription description;

    static {
        // Build
        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);

        builder.getStringAttributeBuilder().rejectExpressions("test").end()
                .addCustomTransformation(new ModelTransformer() {
                    @Override
                    public boolean transform(ModelNode node, PathAddress address, TransformationContext context) throws OperationFailedException {
                        node.get("othertest").set(true);
                        return true;
                    }
                });

        // Discard all
        builder.discardChildResource(DISCARD);

        // configuration=test/setting=directory > test=configuration/directory=setting
        builder.addChildRedirection(CONFIGURATION_TEST, TEST_CONFIGURATION)
                .addChildRedirection(SETTING_DIRECTORY, DIRECTORY_SETTING);

        // Register at the server root
        description = builder.build();
    }

    @Before
    public void setUp() {
        // Cleanup
        resourceRoot = Resource.Factory.create();
        registry = TransformerRegistry.Factory.create(null);
        resourceRegistration = ManagementResourceRegistration.Factory.create(ROOT);
        // test
        final Resource toto = Resource.Factory.create();
        toto.getModel().get("test").set("onetwothree");

        // discard
        final Resource discard = Resource.Factory.create();
        discard.getModel().get("attribute").set("two");
        toto.registerChild(PathElement.pathElement("discard", "one"), discard);

        // configuration
        final Resource configuration = Resource.Factory.create();
        final Resource setting = Resource.Factory.create();
        configuration.registerChild(SETTING_DIRECTORY, setting);
        toto.registerChild(CONFIGURATION_TEST, configuration);

        //
        resourceRoot.registerChild(PATH, toto);

        // Register the description
        final TransformersSubRegistration reg = registry.getServerRegistration(ModelVersion.create(1));
        description.register(reg);

    }

    @Test
    public void testResourceTransformation() throws Exception {

        final ModelNode address = new ModelNode();
        address.add("toto", "testSubsystem");

        final ModelNode node = new ModelNode();
        node.get(ModelDescriptionConstants.OP).set("add");
        node.get(ModelDescriptionConstants.OP_ADDR).set(address);

        final OperationTransformer.TransformedOperation op = transformOperation(node);
        Assert.assertNotNull(op);

        final Resource resource = transformResource();
        System.out.println(Resource.Tools.readModel(resource));

        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        final ModelNode model = toto.getModel();
        Assert.assertTrue(model.hasDefined("othertest"));
        Assert.assertNotNull(toto);
        Assert.assertFalse(toto.hasChild(PathElement.pathElement("discard", "one")));
        Assert.assertFalse(toto.hasChild(CONFIGURATION_TEST));
    }

    @Test
    public void testAddOperation() throws Exception {

        final ModelNode address = new ModelNode();
        address.add("toto", "testSubsystem");

        final ModelNode node = new ModelNode();
        node.get(ModelDescriptionConstants.OP).set("add");
        node.get(ModelDescriptionConstants.OP_ADDR).set(address);
        node.get("test").set("${one:two}");

        OperationTransformer.TransformedOperation op = transformOperation(node);
        Assert.assertTrue(op.rejectOperation(success()));

        node.get("test").set("concrete");
        op = transformOperation(node);
        Assert.assertFalse(op.rejectOperation(success()));
    }

    @Test
    public void testWriteAttribute() throws Exception {

        final ModelNode address = new ModelNode();
        address.add("toto", "testSubsystem");

        final ModelNode node = new ModelNode();
        node.get(ModelDescriptionConstants.OP).set("write-attribute");
        node.get(ModelDescriptionConstants.OP_ADDR).set(address);
        node.get("name").set("test");
        node.get("value").set("${one:two}");

        OperationTransformer.TransformedOperation op = transformOperation(node);
        Assert.assertTrue(op.rejectOperation(success()));

        node.get("value").set("test");
        op = transformOperation(node);
        Assert.assertFalse(op.rejectOperation(success()));
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
        return Transformers.Factory.create(target, resourceRoot, resourceRegistration, ExpressionResolver.DEFAULT, RunningMode.NORMAL, ProcessType.STANDALONE_SERVER);
    }

    private Transformers getTransfomers(final TransformationTarget target) {
        return Transformers.Factory.create(target);
    }

    protected TransformationTarget create(final TransformerRegistry registry, ModelVersion version) {
        return create(registry, version, TransformationTarget.TransformationTargetType.SERVER);
    }

    protected TransformationTarget create(final TransformerRegistry registry, ModelVersion version, TransformationTarget.TransformationTargetType type) {
        return TransformationTargetImpl.create(registry, version, Collections.<PathAddress, ModelVersion>emptyMap(), null, type);
    }

    private static final DescriptionProvider NOOP_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return new ModelNode();
        }
    };

    private static final ResourceDefinition ROOT = new SimpleResourceDefinition(PathElement.pathElement("test"), NOOP_PROVIDER);

    private static final ModelNode success() {
        final ModelNode result = new ModelNode();
        result.get(ModelDescriptionConstants.OUTCOME).set(ModelDescriptionConstants.SUCCESS);
        result.get(ModelDescriptionConstants.RESULT);
        return result;
    }

    private static final ModelNode failed() {
        final ModelNode result = new ModelNode();
        result.get(ModelDescriptionConstants.OUTCOME).set(ModelDescriptionConstants.FAILED);
        result.get(ModelDescriptionConstants.FAILURE_DESCRIPTION).set("failed");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13056.java