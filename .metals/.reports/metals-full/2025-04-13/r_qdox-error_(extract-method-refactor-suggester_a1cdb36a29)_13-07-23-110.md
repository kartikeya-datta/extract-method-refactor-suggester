error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12515.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12515.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12515.java
text:
```scala
c@@ontext.stepCompleted();

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
package org.jboss.as.server.deployment;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CONTENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HASH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REPLACE_DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNTIME_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TO_REPLACE;
import static org.jboss.as.server.controller.resources.DeploymentAttributes.ENABLED;
import static org.jboss.as.server.deployment.DeploymentHandlerUtils.getContents;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.HashUtil;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.repository.ContentRepository;
import org.jboss.as.server.ServerMessages;
import org.jboss.as.server.controller.resources.DeploymentAttributes;
import org.jboss.as.server.services.security.AbstractVaultReader;
import org.jboss.dmr.ModelNode;

/**
 * Handles replacement in the runtime of one deployment by another.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class DeploymentReplaceHandler implements OperationStepHandler {

    public static final String OPERATION_NAME = REPLACE_DEPLOYMENT;

    private final ContentRepository contentRepository;

    private final AbstractVaultReader vaultReader;

    protected DeploymentReplaceHandler(ContentRepository contentRepository, final AbstractVaultReader vaultReader) {
        assert contentRepository != null : "Null contentRepository";
        this.contentRepository = contentRepository;
        this.vaultReader = vaultReader;
    }

    public static DeploymentReplaceHandler create(ContentRepository contentRepository, final AbstractVaultReader vaultReader) {
        return new DeploymentReplaceHandler(contentRepository, vaultReader);
    }

    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
        for (AttributeDefinition def : DeploymentAttributes.REPLACE_DEPLOYMENT_ATTRIBUTES.values()) {
            def.validateOperation(operation);
        }
        String name = DeploymentAttributes.REPLACE_DEPLOYMENT_ATTRIBUTES.get(NAME).resolveModelAttribute(context, operation).asString();
        String toReplace = DeploymentAttributes.REPLACE_DEPLOYMENT_ATTRIBUTES.get(TO_REPLACE).resolveModelAttribute(context, operation).asString();

        if (name.equals(toReplace)) {
            throw ServerMessages.MESSAGES.cannotReplaceDeployment(OPERATION_NAME, NAME, TO_REPLACE,
                    DeploymentRedeployHandler.OPERATION_NAME, DeploymentFullReplaceHandler.OPERATION_NAME);
        }

        final PathElement deployPath = PathElement.pathElement(DEPLOYMENT, name);
        final PathElement replacePath = PathElement.pathElement(DEPLOYMENT, toReplace);

        final Resource root = context.readResource(PathAddress.EMPTY_ADDRESS);
        if (! root.hasChild(replacePath)) {
            throw ServerMessages.MESSAGES.noSuchDeployment(toReplace);
        }

        final ModelNode replaceNode = context.readResourceForUpdate(PathAddress.pathAddress(replacePath)).getModel();
        final String replacedName = DeploymentAttributes.REPLACE_DEPLOYMENT_ATTRIBUTES.get(RUNTIME_NAME).resolveModelAttribute(context, replaceNode).asString();

        ModelNode deployNode;
        String runtimeName;
        if (!root.hasChild(deployPath)) {
            if (!operation.hasDefined(CONTENT)) {
                throw ServerMessages.MESSAGES.noSuchDeployment(name);
            }
            // else -- the HostController handles a server group replace-deployment like an add, so we do too

            final ModelNode content = operation.require(CONTENT);
            // TODO: JBAS-9020: for the moment overlays are not supported, so there is a single content item
            final ModelNode contentItemNode = content.require(0);
            if (contentItemNode.hasDefined(HASH)) {
                byte[] hash = contentItemNode.require(HASH).asBytes();
                addFromHash(hash);
            } else {
            }
            runtimeName = operation.hasDefined(RUNTIME_NAME) ? DeploymentAttributes.REPLACE_DEPLOYMENT_ATTRIBUTES.get(RUNTIME_NAME).resolveModelAttribute(context, operation).asString() : replacedName;

            // Create the resource
            final Resource deployResource = context.createResource(PathAddress.pathAddress(deployPath));
            deployNode = deployResource.getModel();
            deployNode.get(RUNTIME_NAME).set(runtimeName);

            //TODO Assumes this can only be set by client
            deployNode.get(ModelDescriptionConstants.PERSISTENT).set(true);

            deployNode.get(CONTENT).set(content);

        } else {
            deployNode = context.readResourceForUpdate(PathAddress.pathAddress(deployPath)).getModel();
            if (ENABLED.resolveModelAttribute(context, deployNode).asBoolean()) {
                throw ServerMessages.MESSAGES.deploymentAlreadyStarted(toReplace);
            }
            runtimeName = deployNode.require(RUNTIME_NAME).asString();
        }

        deployNode.get(ENABLED.getName()).set(true);
        replaceNode.get(ENABLED.getName()).set(false);

        final DeploymentHandlerUtil.ContentItem[] contents = getContents(deployNode.require(CONTENT));
        DeploymentHandlerUtil.replace(context, replaceNode, runtimeName, name, replacedName, vaultReader, contents);

        context.completeStep();
    }

    protected void addFromHash(byte[] hash) throws OperationFailedException {
        if (!contentRepository.syncContent(hash)) {
            throw ServerMessages.MESSAGES.noSuchDeploymentContent(HashUtil.bytesToHexString(hash));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12515.java