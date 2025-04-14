error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12937.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12937.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12937.java
text:
```scala
c@@onfiguration.registerReadWriteAttribute(CommonAttributes.EXCLUDED_CONTEXTS, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.modcluster;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.modcluster.ModClusterLogger.ROOT_LOGGER;

import java.util.EnumSet;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.global.WriteAttributeHandlers;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.AttributeAccess.Storage;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.registry.OperationEntry.Flag;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.staxmapper.XMLElementReader;

/**
 * Domain extension used to initialize the mod_cluster subsystem element handlers.
 *
 * @author Jean-Frederic Clere
 */
public class ModClusterExtension implements XMLStreamConstants, Extension {

    public static final String SUBSYSTEM_NAME = "modcluster";

    private static final PathElement sslConfigurationPath = PathElement.pathElement(CommonAttributes.SSL, CommonAttributes.CONFIGURATION);
    private static final PathElement configurationPath = PathElement.pathElement(CommonAttributes.MOD_CLUSTER_CONFIG);

    /** {@inheritDoc} */
    @Override
    public void initialize(ExtensionContext context) {

        EnumSet<Flag> runtimeOnly = EnumSet.of(Flag.RUNTIME_ONLY);
        ROOT_LOGGER.debugf("Activating Mod_cluster Extension");

        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, Namespace.CURRENT.getMajorVersion(), Namespace.CURRENT.getMinorVersion());
        final ManagementResourceRegistration registration = subsystem.registerSubsystemModel(ModClusterSubsystemDescriptionProviders.SUBSYSTEM);
        registration.registerOperationHandler(ModelDescriptionConstants.ADD, ModClusterSubsystemAdd.INSTANCE, ModClusterSubsystemAdd.INSTANCE, false);
        registration.registerOperationHandler(ModelDescriptionConstants.REMOVE, ModClusterSubsystemRemove.INSTANCE, ModClusterSubsystemRemove.INSTANCE, false);
        registration.registerOperationHandler(DESCRIBE, ModClusterSubsystemDescribe.INSTANCE, ModClusterSubsystemDescribe.INSTANCE, false, OperationEntry.EntryType.PRIVATE);

        // The following ops only affect the runtime and not the configuration, so don't register them on the Host Controller
        if (context.isRuntimeOnlyRegistrationValid()) {
            // Proxy related commands.
            registration.registerOperationHandler("list-proxies", ModClusterListProxies.INSTANCE, ModClusterListProxies.INSTANCE, false, runtimeOnly);
            registration.registerOperationHandler("read-proxies-info", ModClusterGetProxyInfo.INSTANCE, ModClusterGetProxyInfo.INSTANCE, false, runtimeOnly);
            registration.registerOperationHandler("read-proxies-configuration", ModClusterGetProxyConfiguration.INSTANCE, ModClusterGetProxyConfiguration.INSTANCE, false, runtimeOnly);

            //These seem to be modifying the state so don't add the runtimeOnly stuff for now
            registration.registerOperationHandler("add-proxy", ModClusterAddProxy.INSTANCE, ModClusterAddProxy.INSTANCE, false);
            registration.registerOperationHandler("remove-proxy", ModClusterRemoveProxy.INSTANCE, ModClusterRemoveProxy.INSTANCE, false);

            // node related operations.
            registration.registerOperationHandler("refresh", ModClusterRefresh.INSTANCE, ModClusterRefresh.INSTANCE, false, runtimeOnly);
            registration.registerOperationHandler("reset", ModClusterReset.INSTANCE, ModClusterReset.INSTANCE, false, runtimeOnly);

            // node (all contexts) related operations.
            registration.registerOperationHandler("enable", ModClusterEnable.INSTANCE, ModClusterEnable.INSTANCE, false);
            registration.registerOperationHandler("disable", ModClusterDisable.INSTANCE, ModClusterDisable.INSTANCE, false);
            registration.registerOperationHandler("stop", ModClusterStop.INSTANCE, ModClusterStop.INSTANCE, false);

            // Context related operations.
            registration.registerOperationHandler("enable-context", ModClusterEnableContext.INSTANCE, ModClusterEnableContext.INSTANCE, false);
            registration.registerOperationHandler("disable-context", ModClusterDisableContext.INSTANCE, ModClusterDisableContext.INSTANCE, false);
            registration.registerOperationHandler("stop-context", ModClusterStopContext.INSTANCE, ModClusterStopContext.INSTANCE, false);
        }

        final ManagementResourceRegistration configuration = registration.registerSubModel(configurationPath, ModClusterSubsystemDescriptionProviders.CONFIGURATION);
        final ManagementResourceRegistration ssl = configuration.registerSubModel(sslConfigurationPath, ModClusterSubsystemDescriptionProviders.SSL);

        // Attributes. (standard)

        configuration.registerReadWriteAttribute(CommonAttributes.ADVERTISE_SOCKET, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.PROXY_LIST, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.PROXY_URL, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.ADVERTISE, null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.BOOLEAN, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.ADVERTISE_SECURITY_KEY, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.EXCLUDED_CONTEXTS, null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.BOOLEAN, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.AUTO_ENABLE_CONTEXTS, null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.BOOLEAN, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.STOP_CONTEXT_TIMEOUT, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.SOCKET_TIMEOUT, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1, true), Storage.CONFIGURATION);

        configuration.registerReadWriteAttribute(CommonAttributes.STICKY_SESSION, null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.BOOLEAN, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.STICKY_SESSION_REMOVE, null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.BOOLEAN, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.STICKY_SESSION_FORCE, null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.BOOLEAN, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.WORKER_TIMEOUT, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.MAX_ATTEMPTS, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.FLUSH_PACKETS, null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.BOOLEAN, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.FLUSH_WAIT, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.PING, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.SMAX, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.TTL, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.NODE_TIMEOUT, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.BALANCER, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.DOMAIN, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);

        // Special Attributes.
        configuration.registerReadWriteAttribute(CommonAttributes.DYNAMIC_LOAD_PROVIDER, null, new WriteDynamicLoadProviderOperationHandler(), Storage.CONFIGURATION);
        configuration.registerReadWriteAttribute(CommonAttributes.SIMPLE_LOAD_PROVIDER, null, new WriteSimpleLoadProviderOperationHandler(), Storage.CONFIGURATION);

        // SSL children.
        ssl.registerOperationHandler(ADD, ModClusterAddSSL.INSTANCE, ModClusterAddSSL.INSTANCE, false);
        ssl.registerOperationHandler(REMOVE, ModClusterRemoveSSL.INSTANCE, ModClusterRemoveSSL.INSTANCE, false);
        ssl.registerReadWriteAttribute(CommonAttributes.KEY_ALIAS, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        ssl.registerReadWriteAttribute(CommonAttributes.PASSWORD, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        ssl.registerReadWriteAttribute(CommonAttributes.CERTIFICATE_KEY_FILE, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        ssl.registerReadWriteAttribute(CommonAttributes.CIPHER_SUITE, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        ssl.registerReadWriteAttribute(CommonAttributes.PROTOCOL, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        ssl.registerReadWriteAttribute(CommonAttributes.VERIFY_CLIENT, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        ssl.registerReadWriteAttribute(CommonAttributes.CA_CERTIFICATE_FILE, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);
        ssl.registerReadWriteAttribute(CommonAttributes.CA_REVOCATION_URL, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1, true), Storage.CONFIGURATION);

        // Metric for the  dynamic-load-provider
        configuration.registerOperationHandler("add-metric", ModClusterAddMetric.INSTANCE, ModClusterAddMetric.INSTANCE, false, runtimeOnly);
        configuration.registerOperationHandler("add-custom-metric", ModClusterAddCustomMetric.INSTANCE, ModClusterAddCustomMetric.INSTANCE, false, runtimeOnly);
        configuration.registerOperationHandler("remove-metric", ModClusterRemoveMetric.INSTANCE, ModClusterRemoveMetric.INSTANCE, false, runtimeOnly);
        configuration.registerOperationHandler("remove-custom-metric", ModClusterRemoveCustomMetric.INSTANCE, ModClusterRemoveCustomMetric.INSTANCE, false, runtimeOnly);

        subsystem.registerXMLElementWriter(new ModClusterSubsystemXMLWriter());
    }

    public static class WriteDynamicLoadProviderOperationHandler implements OperationStepHandler {

        @Override
        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            ModelNode history = operation.get(CommonAttributes.HISTORY);
            ModelNode decay = operation.get(CommonAttributes.DECAY);

            // final ModelNode submodel = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS);
            final ModelNode submodel = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS).getModel();
            final ModelNode currentValue = submodel.get(CommonAttributes.DYNAMIC_LOAD_PROVIDER).clone();
            if (!history.isDefined())
                history = currentValue.get(CommonAttributes.HISTORY);
            submodel.get(CommonAttributes.HISTORY).set(history);
            if (!decay.isDefined())
                decay = currentValue.get(CommonAttributes.DECAY);
            submodel.get(CommonAttributes.DECAY).set(decay);

            submodel.get(CommonAttributes.DYNAMIC_LOAD_PROVIDER).get(CommonAttributes.HISTORY).set(history);
            submodel.get(CommonAttributes.DYNAMIC_LOAD_PROVIDER).get(CommonAttributes.DECAY).set(decay);

            context.completeStep();
        }

    }

    public static class WriteSimpleLoadProviderOperationHandler implements OperationStepHandler {

        @Override
        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            ModelNode factor = operation.get(CommonAttributes.FACTOR);

            final ModelNode submodel = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS).getModel();
            final ModelNode currentValue = submodel.get(CommonAttributes.SIMPLE_LOAD_PROVIDER).clone();
            if (!factor.isDefined())
                factor = currentValue.get(CommonAttributes.HISTORY);
            submodel.get(CommonAttributes.FACTOR).set(factor);

            submodel.get(CommonAttributes.SIMPLE_LOAD_PROVIDER).get(CommonAttributes.FACTOR).set(factor);

            context.completeStep();
        }

    }

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        for (Namespace namespace: Namespace.values()) {
            XMLElementReader<List<ModelNode>> reader = namespace.getXMLReader();
            if (reader != null) {
                context.setSubsystemXmlMapping(SUBSYSTEM_NAME, namespace.getUri(), namespace.getXMLReader());
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12937.java