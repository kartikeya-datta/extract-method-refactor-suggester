error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6726.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6726.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6726.java
text:
```scala
h@@ash = deploymentRepository.addDeploymentContent(in);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.domain.controller.operations.deployment;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HASH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INPUT_STREAM_INDEX;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNTIME_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_GROUP;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.jboss.as.controller.BasicOperationResult;
import org.jboss.as.controller.HashUtil;
import org.jboss.as.controller.ModelUpdateOperationHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationResult;
import org.jboss.as.controller.ResultHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.operations.validation.ModelTypeValidator;
import org.jboss.as.controller.operations.validation.ParametersValidator;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.server.controller.descriptions.DeploymentDescription;
import org.jboss.as.server.deployment.api.DeploymentRepository;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;

/**
 * Handles replacement in the runtime of one deployment by another.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class DeploymentFullReplaceHandler implements ModelUpdateOperationHandler, DescriptionProvider {

    public static final String OPERATION_NAME = "full-replace-deployment";

    static final ModelNode getOperation(ModelNode address) {
        return Util.getEmptyOperation(OPERATION_NAME, address);
    }

    private final DeploymentRepository deploymentRepository;

    private final ParametersValidator validator = new ParametersValidator();

    public DeploymentFullReplaceHandler(final DeploymentRepository deploymentRepository) {
        this.deploymentRepository = deploymentRepository;
        this.validator.registerValidator(NAME, new StringLengthValidator(1, Integer.MAX_VALUE, false, false));
        this.validator.registerValidator(RUNTIME_NAME, new StringLengthValidator(1, Integer.MAX_VALUE, true, false));
        this.validator.registerValidator(HASH, new ModelTypeValidator(ModelType.BYTES, true));
        this.validator.registerValidator(INPUT_STREAM_INDEX, new ModelTypeValidator(ModelType.INT, true));
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        return DeploymentDescription.getFullReplaceDeploymentOperation(locale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationResult execute(OperationContext context, ModelNode operation, ResultHandler resultHandler) throws OperationFailedException {

        validator.validate(operation);

        String name = operation.require(NAME).asString();
        String runtimeName = operation.hasDefined(RUNTIME_NAME) ? operation.get(RUNTIME_NAME).asString() : name;
        byte[] hash;
        if (operation.hasDefined(INPUT_STREAM_INDEX) && operation.hasDefined(HASH)) {
            throw new OperationFailedException(new ModelNode().set("Can't pass in both an input-stream-index and a hash"));
        } else if (operation.hasDefined(INPUT_STREAM_INDEX)) {
            InputStream in = getContents(context, operation);
            try {
                hash = deploymentRepository.addDeploymentContent(name, runtimeName, in);
            } catch (IOException e) {
                throw new OperationFailedException(new ModelNode().set(e.toString()));
            }
            // TOTAL HACK!!
            // when we push this to slave DCs, we want to push the hash not the stream
            // So, munge the operation :(
            // Very fragile as this will break in the face of any defensive copying by the controller
            operation.remove(INPUT_STREAM_INDEX);
            operation.get(HASH).set(hash);
        } else if (operation.hasDefined(HASH)) {

            hash = operation.get(HASH).asBytes();
            if (!deploymentRepository.hasDeploymentContent(hash)) {
                throw new OperationFailedException(new ModelNode().set(String.format(
                        "No deployment content with hash %s is available in the deployment content repository.",
                        HashUtil.bytesToHexString(hash))));
            }
        } else {
            throw new OperationFailedException(new ModelNode().set("Neither an attachment or a hash were passed in"));
        }

        ModelNode rootModel = context.getSubModel();
        ModelNode deployments = rootModel.get(DEPLOYMENT);

        ModelNode replaceNode = deployments.hasDefined(name) ? deployments.get(name) : null;
        if (replaceNode == null) {
            throw new OperationFailedException(new ModelNode().set(String.format("No deployment with name %s found", name)));
        }

        ModelNode deployNode = new ModelNode();
        deployNode.get(NAME).set(name);
        deployNode.get(RUNTIME_NAME).set(runtimeName);
        deployNode.get(HASH).set(hash);

        deployments.get(name).set(deployNode);

        if (rootModel.hasDefined(SERVER_GROUP)) {
            for (Property server : rootModel.get(SERVER_GROUP).asPropertyList()) {
                ModelNode serverConfig = server.getValue();
                if (serverConfig.hasDefined(DEPLOYMENT) && serverConfig.get(DEPLOYMENT).hasDefined(name)) {
                    ModelNode groupDeployNode = serverConfig.get(DEPLOYMENT, name);
                    groupDeployNode.get(RUNTIME_NAME).set(runtimeName);
                    groupDeployNode.get(HASH).set(hash);
                }
            }
        }

        ModelNode compensatingOp = operation.clone();
        compensatingOp.get(RUNTIME_NAME).set(replaceNode.get(RUNTIME_NAME).asString());
        compensatingOp.get(HASH).set(replaceNode.get(HASH).asBytes());
        if (operation.hasDefined(INPUT_STREAM_INDEX)) {
            operation.remove(INPUT_STREAM_INDEX);
        }

        resultHandler.handleResultComplete();

        return new BasicOperationResult(compensatingOp);
    }


    private InputStream getContents(OperationContext context, ModelNode operation) {
        int streamIndex = operation.get(INPUT_STREAM_INDEX).asInt();
        if (streamIndex > context.getInputStreams().size() - 1) {
            throw new IllegalArgumentException("Invalid " + INPUT_STREAM_INDEX + "=" + streamIndex + ", the maximum index is " + (context.getInputStreams().size() - 1));
        }

        InputStream in = context.getInputStreams().get(streamIndex);
        if (in == null) {
            throw new IllegalStateException("Null stream at index " + streamIndex);
        }
        return in;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6726.java