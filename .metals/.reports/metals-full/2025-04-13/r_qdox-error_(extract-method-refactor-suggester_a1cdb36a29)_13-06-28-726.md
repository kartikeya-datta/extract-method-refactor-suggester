error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13640.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13640.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13640.java
text:
```scala
c@@onfigValidator.registerValidator(WEBSERVICE_SECURE_PORT, new ModelTypeValidator(ModelType.INT, true, true));

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
package org.jboss.as.webservices.dmr;

import org.jboss.as.controller.BasicOperationResult;
import org.jboss.as.controller.ModelAddOperationHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationResult;
import org.jboss.as.controller.ResultHandler;
import org.jboss.as.controller.RuntimeTask;
import org.jboss.as.controller.RuntimeTaskContext;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.operations.validation.ModelTypeValidator;
import org.jboss.as.controller.operations.validation.ParametersValidator;
import org.jboss.as.server.BootOperationContext;
import org.jboss.as.server.BootOperationHandler;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.webservices.config.ServerConfigImpl;
import org.jboss.as.webservices.deployers.WebServiceRefAnnotationParsingProcessor;
import org.jboss.as.webservices.service.EndpointRegistryService;
import org.jboss.as.webservices.service.ServerConfigService;
import org.jboss.as.webservices.util.ModuleClassLoaderProvider;
import org.jboss.as.webservices.util.WSServices;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceTarget;

import java.net.UnknownHostException;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.webservices.dmr.Constants.*;

/**
 * @author alessio.soldano@jboss.com
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
public class WSSubsystemAdd implements ModelAddOperationHandler, BootOperationHandler {
    private static final Logger log = Logger.getLogger("org.jboss.as.webservices");

    static final WSSubsystemAdd INSTANCE = new WSSubsystemAdd();

    private final ParametersValidator operationValidator = new ParametersValidator();
    private final ParametersValidator configValidator = new ParametersValidator();

    // Private to ensure a singleton.
    private WSSubsystemAdd() {
        operationValidator.registerValidator(CONFIGURATION, new ModelTypeValidator(ModelType.OBJECT));
        configValidator.registerValidator(WEBSERVICE_HOST, new ModelTypeValidator(ModelType.STRING));
        configValidator.registerValidator(MODIFY_SOAP_ADDRESS, new ModelTypeValidator(ModelType.BOOLEAN));
        configValidator.registerValidator(WEBSERVICE_PORT, new ModelTypeValidator(ModelType.INT, true, true));
        configValidator.registerValidator(WEBSERVICE_PORT, new ModelTypeValidator(ModelType.INT, true, true));
    }

    @Override
    public OperationResult execute(final OperationContext context, final ModelNode operation,
            final ResultHandler resultHandler) throws OperationFailedException {
        operationValidator.validate(operation);
        final ModelNode config = operation.require(CONFIGURATION);
        configValidator.validate(config);

        final ModelNode subModel = context.getSubModel();
        subModel.get(CONFIGURATION).set(config);

        if (context instanceof BootOperationContext) {
            final BootOperationContext updateContext = (BootOperationContext) context;
            context.getRuntimeContext().setRuntimeTask(new RuntimeTask() {
                @Override
                public void execute(RuntimeTaskContext context) throws OperationFailedException {
                    log.info("Activating WebServices Extension");
                    ModuleClassLoaderProvider.register();
                    WSServices.saveContainerRegistry(context.getServiceRegistry());

                    ServiceTarget serviceTarget = context.getServiceTarget();
                    ServerConfigImpl serverConfig = createServerConfig(config);
                    ServerConfigService.install(serviceTarget, serverConfig);
                    EndpointRegistryService.install(serviceTarget);

                    // add the DUP for dealing with WS deployments
                    WSDeploymentActivator.activate(updateContext);
                    resultHandler.handleResultComplete();
                }
            });
            updateContext.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_WEB_SERVICE_INJECTION_ANNOTATION, new WebServiceRefAnnotationParsingProcessor());
        } else {
            resultHandler.handleResultComplete();
        }

        // Create the compensating operation
        final ModelNode compensatingOperation = Util.getResourceRemoveOperation(operation.require(OP_ADDR));
        return new BasicOperationResult(compensatingOperation);
    }

    private static ServerConfigImpl createServerConfig(ModelNode configuration) {
        final ServerConfigImpl config = ServerConfigImpl.getInstance();
        try {
            config.setWebServiceHost(configuration.require(WEBSERVICE_HOST).asString());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        config.setModifySOAPAddress(configuration.require(MODIFY_SOAP_ADDRESS).asBoolean());
        if (configuration.hasDefined(WEBSERVICE_PORT)) {
            config.setWebServicePort(configuration.require(WEBSERVICE_PORT).asInt());
        }
        if (configuration.hasDefined(WEBSERVICE_SECURE_PORT)) {
            config.setWebServiceSecurePort(configuration.require(WEBSERVICE_SECURE_PORT).asInt());
        }
        return config;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13640.java