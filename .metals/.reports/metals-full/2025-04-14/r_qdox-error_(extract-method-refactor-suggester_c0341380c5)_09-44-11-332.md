error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8048.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8048.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8048.java
text:
```scala
p@@rocessorTarget.addDeploymentProcessor(Constants.SUBSYSTEM_NAME, Phase.STRUCTURE, Phase.STRUCTURE_REGISTER_JBOSS_ALL_APPCLIENT, new JBossAllXmlParserRegisteringProcessor<JBossClientMetaData>(AppClientJBossAllParser.ROOT_ELEMENT, AppClientJBossAllParser.ATTACHMENT_KEY, new AppClientJBossAllParser()));

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

package org.jboss.as.appclient.subsystem;

import java.io.File;
import java.util.List;

import org.jboss.as.appclient.deployment.ActiveApplicationClientProcessor;
import org.jboss.as.appclient.deployment.AppClientJBossAllParser;
import org.jboss.as.appclient.deployment.ApplicationClientDependencyProcessor;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.appclient.deployment.ApplicationClientManifestProcessor;
import org.jboss.as.appclient.deployment.ApplicationClientParsingDeploymentProcessor;
import org.jboss.as.appclient.deployment.ApplicationClientStartProcessor;
import org.jboss.as.appclient.deployment.ApplicationClientStructureProcessor;
import org.jboss.as.appclient.service.ApplicationClientDeploymentService;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.Services;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.server.deployment.jbossallxml.JBossAllXmlParserRegisteringProcessor;
import org.jboss.dmr.ModelNode;
import org.jboss.metadata.appclient.jboss.JBossClientMetaData;
import org.jboss.msc.service.ServiceController;

import static org.jboss.as.appclient.subsystem.Constants.CONNECTION_PROPERTIES_URL;
import static org.jboss.as.appclient.subsystem.Constants.HOST_URL;

/**
 * Add operation handler for the application client subsystem.
 *
 * @author Stuart Douglas
 */
class AppClientSubsystemAdd extends AbstractBoottimeAddStepHandler {

    static final AppClientSubsystemAdd INSTANCE = new AppClientSubsystemAdd();

    private final String[] EMPTY_STRING = new String[0];

    private AppClientSubsystemAdd() {
        //
    }

    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        for(AttributeDefinition attr : AppClientSubsystemResourceDefinition.ATTRIBUTES) {
            attr.validateAndSet(operation, model);
        }
    }

    protected void performBoottime(final OperationContext context, ModelNode operation, final ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {
        final String deployment = AppClientSubsystemResourceDefinition.DEPLOYMENT.resolveModelAttribute(context, model).asString();
        final File file = new File(AppClientSubsystemResourceDefinition.FILE.resolveModelAttribute(context, model).asString());
        final String hostUrl = model.hasDefined(HOST_URL) ? AppClientSubsystemResourceDefinition.HOST_URL.resolveModelAttribute(context, model).asString() : null;
        final String connectionPropertiesUrl = model.hasDefined(CONNECTION_PROPERTIES_URL) ? AppClientSubsystemResourceDefinition.CONNECTION_PROPERTIES_URL.resolveModelAttribute(context, model).asString() : null;
        final List<String> parameters = AppClientSubsystemResourceDefinition.PARAMETERS.unwrap(context,model);

        context.addStep(new AbstractDeploymentChainStep() {
            protected void execute(DeploymentProcessorTarget processorTarget) {
                if (deployment != null && !deployment.isEmpty()) {
                    processorTarget.addDeploymentProcessor(Constants.SUBSYSTEM_NAME, Phase.STRUCTURE, Phase.STRUCTURE_APP_CLIENT, new ApplicationClientStructureProcessor(deployment));
                }
                processorTarget.addDeploymentProcessor(Constants.SUBSYSTEM_NAME, Phase.STRUCTURE, Phase.STRUCTURE_REGISTER_JBOSS_ALL_XML_PARSER, new JBossAllXmlParserRegisteringProcessor<JBossClientMetaData>(AppClientJBossAllParser.ROOT_ELEMENT, AppClientJBossAllParser.ATTACHMENT_KEY, new AppClientJBossAllParser()));
                processorTarget.addDeploymentProcessor(Constants.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_APP_CLIENT_XML, new ApplicationClientParsingDeploymentProcessor());
                processorTarget.addDeploymentProcessor(Constants.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_APPLICATION_CLIENT_MANIFEST, new ApplicationClientManifestProcessor());
                processorTarget.addDeploymentProcessor(Constants.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_APPLICATION_CLIENT_ACTIVE, new ActiveApplicationClientProcessor(deployment));
                processorTarget.addDeploymentProcessor(Constants.SUBSYSTEM_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_APPLICATION_CLIENT, new ApplicationClientDependencyProcessor());
                processorTarget.addDeploymentProcessor(Constants.SUBSYSTEM_NAME, Phase.INSTALL, Phase.INSTALL_APPLICATION_CLIENT, new ApplicationClientStartProcessor(hostUrl, connectionPropertiesUrl, parameters == null ? new String[0] : parameters.toArray(EMPTY_STRING)));

            }
        }, OperationContext.Stage.RUNTIME);

        final ApplicationClientDeploymentService service = new ApplicationClientDeploymentService(file);

        newControllers.add(
                context.getServiceTarget().addService(ApplicationClientDeploymentService.SERVICE_NAME, service)
                        .addDependency(Services.JBOSS_SERVER_CONTROLLER, ModelController.class, service.getControllerValue())
                        .install());

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8048.java