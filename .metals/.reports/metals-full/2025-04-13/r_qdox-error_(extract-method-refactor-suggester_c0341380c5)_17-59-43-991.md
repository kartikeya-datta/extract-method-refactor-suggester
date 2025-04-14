error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9796.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9796.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9796.java
text:
```scala
p@@rocessorTarget.addDeploymentProcessor(Phase.DEPENDENCIES, Phase.DEPENDENCIES_JPA, new JPADependencyProcessor());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.jpa.subsystem;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.operations.validation.ParametersValidator;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.jpa.persistenceprovider.PersistenceProviderResolverImpl;
import org.jboss.as.jpa.processor.JPAAnnotationParseProcessor;
import org.jboss.as.jpa.processor.JPADependencyProcessor;
import org.jboss.as.jpa.processor.JPAInterceptorProcessor;
import org.jboss.as.jpa.processor.PersistenceProviderProcessor;
import org.jboss.as.jpa.processor.PersistenceRefProcessor;
import org.jboss.as.jpa.processor.PersistenceUnitDeploymentProcessor;
import org.jboss.as.jpa.processor.PersistenceUnitParseProcessor;
import org.jboss.as.jpa.service.JPAService;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceTarget;

import java.util.List;
import java.util.Locale;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;


/**
 * Add the JPA subsystem directive.
 * <p/>
 * TODO:  add subsystem configuration properties
 *
 * @author Scott Marlow
 */

class JPASubSystemAdd extends AbstractBoottimeAddStepHandler implements DescriptionProvider {


    static ModelNode getAddOperation(ModelNode address, ModelNode currentModel) {
        ModelNode addOp = Util.getEmptyOperation(OPERATION_NAME, address);
        addOp.get(CommonAttributes.DEFAULT_DATASOURCE).set(currentModel.get(CommonAttributes.DEFAULT_DATASOURCE));
        return addOp;
    }

    static final String OPERATION_NAME = ADD;
    static final JPASubSystemAdd INSTANCE = new JPASubSystemAdd();

    private ParametersValidator modelValidator = new ParametersValidator();
    private ParametersValidator runtimeValidator = new ParametersValidator();

    private JPASubSystemAdd() {
        modelValidator.registerValidator(CommonAttributes.DEFAULT_DATASOURCE, new StringLengthValidator(0, Integer.MAX_VALUE, false, true));
        runtimeValidator.registerValidator(CommonAttributes.DEFAULT_DATASOURCE, new StringLengthValidator(0, Integer.MAX_VALUE, false, false));
    }


    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        modelValidator.validate(operation);
        final ModelNode defaultDSNode = operation.require(CommonAttributes.DEFAULT_DATASOURCE);
        model.get(CommonAttributes.DEFAULT_DATASOURCE).set(defaultDSNode);
    }

    protected void performBoottime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

        runtimeValidator.validate(operation.resolve());
        context.addStep(new AbstractDeploymentChainStep() {
            protected void execute(DeploymentProcessorTarget processorTarget) {

                // set Hibernate persistence provider as the default provider
                javax.persistence.spi.PersistenceProviderResolverHolder.setPersistenceProviderResolver(
                        PersistenceProviderResolverImpl.getInstance());

                // handles parsing of persistence.xml
                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_PERSISTENCE_UNIT, new PersistenceUnitParseProcessor());
                // handles persistence unit / context annotations in components
                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_PERSISTENCE_ANNOTATION, new JPAAnnotationParseProcessor());
                // injects JPA dependencies into an application
                processorTarget.addDeploymentProcessor(Phase.DEPENDENCIES, Phase.DEPENDENCIES__INJECT_JPA_DEPENDENCIES, new JPADependencyProcessor());
                // handles persistence unit / context references from deployment descriptors
                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, Phase.POST_MODULE_PERSISTENCE_REF, new PersistenceRefProcessor());
                // registers listeners/interceptors on session beans
                processorTarget.addDeploymentProcessor(Phase.INSTALL, Phase.INSTALL_JPA_INTERCEPTORS, new JPAInterceptorProcessor());
                // handles deploying a persistence provider
                processorTarget.addDeploymentProcessor(Phase.INSTALL, Phase.INSTALL_PERSISTENCE_PROVIDER, new PersistenceProviderProcessor());
                // handles pu deployment (starts pu service)
                processorTarget.addDeploymentProcessor(Phase.INSTALL, Phase.INSTALL_PERSISTENTUNIT, new PersistenceUnitDeploymentProcessor());
            }
        }, OperationContext.Stage.RUNTIME);

        final ModelNode defaultDSNode = operation.require(CommonAttributes.DEFAULT_DATASOURCE);
        final String dataSourceName = defaultDSNode.resolve().asString();
        final ServiceTarget target = context.getServiceTarget();
        newControllers.add(JPAService.addService(target, dataSourceName, verificationHandler));
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        return JPADescriptions.getSubsystemAdd(locale);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9796.java