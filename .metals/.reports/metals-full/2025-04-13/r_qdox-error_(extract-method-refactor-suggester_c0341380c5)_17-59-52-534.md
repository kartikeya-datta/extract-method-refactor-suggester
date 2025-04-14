error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7369.java
text:
```scala
final T@@ransformationTarget target = TransformationTargetImpl.create(transformerRegistry, ModelVersion.create(1, 0, 0),subsystemVersions , null, TransformationTarget.TransformationTargetType.SERVER);

package org.jboss.as.controller.transform;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.global.GlobalOperationHandlers;
import org.jboss.as.controller.operations.validation.ModelTypeValidator;
import org.jboss.as.controller.operations.validation.ParametersValidator;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a>
 */
public class ReadTransformedResourceOperation implements OperationStepHandler {

    private final ParametersValidator validator = new ParametersValidator();

    public static DescriptionProvider DESCRIPTION = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return new ModelNode();
        }
    };

    private final TransformerRegistry transformerRegistry;

    public ReadTransformedResourceOperation(final TransformerRegistry transformerRegistry) {
        validator.registerValidator(SUBSYSTEM, new ModelTypeValidator(ModelType.STRING, false));
        this.transformerRegistry = transformerRegistry;
    }

    private ModelNode transformReadResourceResult(final OperationContext context, ModelNode original, String subsystem, final ModelVersion version) throws OperationFailedException {
        ModelNode rootData = original.get(ModelDescriptionConstants.RESULT);

        Map<PathAddress,ModelVersion> subsystemVersions = new HashMap<PathAddress, ModelVersion>();
        subsystemVersions.put(PathAddress.EMPTY_ADDRESS.append(ModelDescriptionConstants.SUBSYSTEM,subsystem),version);

        final TransformationTarget target = TransformationTargetImpl.create(transformerRegistry, ModelVersion.create(1, 0, 0),subsystemVersions , TransformationTarget.TransformationTargetType.SERVER);
        final Transformers transformers = Transformers.Factory.create(target);
        final ResourceTransformationContext ctx = Transformers.Factory.getTransformationContext(target, context);

        final ImmutableManagementResourceRegistration rr = context.getRootResourceRegistration();
        Resource root = TransformerRegistry.modelToResource(rr, rootData, true);
        Resource transformed = transformers.transformResource(ctx, root) ;

        final ModelNode model = Resource.Tools.readModel(transformed);

        return model;
    }

    @Override
    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
        final String subsystem = operation.get(ModelDescriptionConstants.SUBSYSTEM).asString();
        final int major = operation.get(ModelDescriptionConstants.MANAGEMENT_MAJOR_VERSION).asInt();
        final int minor = operation.get(ModelDescriptionConstants.MANAGEMENT_MINOR_VERSION).asInt();
        final int micro = operation.get(ModelDescriptionConstants.MANAGEMENT_MICRO_VERSION).asInt();
        final ModelVersion version= ModelVersion.create(major,minor,micro);
        // Add a step to transform the result of a READ_RESOURCE.
        // Do this first, Stage.IMMEDIATE
        final ModelNode readResourceResult = new ModelNode();
        context.addStep(new OperationStepHandler() {
            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                ModelNode transformed = transformReadResourceResult(context, readResourceResult, subsystem, version);
                context.getResult().set(transformed);
                context.stepCompleted();
            }
        }, OperationContext.Stage.IMMEDIATE);

        // Now add a step to do the READ_RESOURCE, also IMMEDIATE. This will execute *before* the one ^^^
        final ModelNode op = new ModelNode();
        op.get(OP).set(READ_RESOURCE_OPERATION);
        op.get(OP_ADDR).set(PathAddress.EMPTY_ADDRESS.toModelNode());
        op.get(RECURSIVE).set(true);
        context.addStep(readResourceResult, op, GlobalOperationHandlers.READ_RESOURCE, OperationContext.Stage.IMMEDIATE);

        context.stepCompleted();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7369.java