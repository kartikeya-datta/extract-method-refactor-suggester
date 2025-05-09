error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13267.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13267.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13267.java
text:
```scala
c@@ontext.addStep(targetOperation, ModClusterConfigAdd.INSTANCE, OperationContext.Stage.MODEL, true);

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

import org.apache.catalina.connector.Connector;
import org.jboss.as.clustering.msc.AsynchronousService;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.network.SocketBinding;
import org.jboss.as.network.SocketBindingManager;
import org.jboss.as.web.WebServer;
import org.jboss.as.web.WebSubsystemServices;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.modcluster.config.impl.ModClusterConfig;
import org.jboss.modcluster.load.LoadBalanceFactorProvider;
import org.jboss.modcluster.load.impl.DynamicLoadBalanceFactorProvider;
import org.jboss.modcluster.load.impl.SimpleLoadBalanceFactorProvider;
import org.jboss.modcluster.load.metric.LoadMetric;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.jboss.as.modcluster.LoadMetricDefinition.CAPACITY;
import static org.jboss.as.modcluster.LoadMetricDefinition.TYPE;
import static org.jboss.as.modcluster.LoadMetricDefinition.WEIGHT;
import static org.jboss.as.modcluster.ModClusterConfigResourceDefinition.*;
import static org.jboss.as.modcluster.ModClusterExtension.SSL_CONFIGURATION_PATH;
import static org.jboss.as.modcluster.ModClusterLogger.ROOT_LOGGER;
import static org.jboss.as.modcluster.ModClusterSSLResourceDefinition.CA_CERTIFICATE_FILE;
import static org.jboss.as.modcluster.ModClusterSSLResourceDefinition.CA_REVOCATION_URL;
import static org.jboss.as.modcluster.ModClusterSSLResourceDefinition.CERTIFICATE_KEY_FILE;
import static org.jboss.as.modcluster.ModClusterSSLResourceDefinition.CIPHER_SUITE;
import static org.jboss.as.modcluster.ModClusterSSLResourceDefinition.KEY_ALIAS;
import static org.jboss.as.modcluster.ModClusterSSLResourceDefinition.PASSWORD;
import static org.jboss.as.modcluster.ModClusterSSLResourceDefinition.PROTOCOL;

/**
 * The managed subsystem add update.
 *
 * @author Jean-Frederic Clere
 * @author Tomaz Cerar
 */
class ModClusterSubsystemAdd extends AbstractAddStepHandler {
    static final ModClusterSubsystemAdd INSTANCE = new ModClusterSubsystemAdd();

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model,
                                  ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

        final ModelNode fullModel = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));
        final ModelNode modelConfig = fullModel.get(ModClusterExtension.CONFIGURATION_PATH.getKeyValuePair());
        final ModClusterConfig config = getModClusterConfig(context, modelConfig);
        final LoadBalanceFactorProvider loadProvider = getModClusterLoadProvider(context, modelConfig);
        final String connector = CONNECTOR.resolveModelAttribute(context, modelConfig).asString();
        // Add mod_cluster service
        final ModClusterService service = new ModClusterService(config, loadProvider);
        final ServiceBuilder<ModCluster> builder = AsynchronousService.addService(context.getServiceTarget(), ModClusterService.NAME, service, true, true)
                .addDependency(WebSubsystemServices.JBOSS_WEB, WebServer.class, service.getWebServer())
                .addDependency(SocketBindingManager.SOCKET_BINDING_MANAGER, SocketBindingManager.class, service.getBindingManager())
                .addDependency(WebSubsystemServices.JBOSS_WEB_CONNECTOR.append(connector), Connector.class, service.getConnectorInjector())
                .addListener(verificationHandler)
                .setInitialMode(Mode.ACTIVE)
        ;
        final ModelNode bindingRefNode = ADVERTISE_SOCKET.resolveModelAttribute(context, modelConfig);
        final String bindingRef = bindingRefNode.isDefined() ? bindingRefNode.asString() : null;
        if (bindingRef != null) {
            builder.addDependency(SocketBinding.JBOSS_BINDING_NAME.append(bindingRef), SocketBinding.class, service.getBinding());
        }
        newControllers.add(builder.install());
    }

    /*
    this is here so legacy configuration can be supported
     */
    @Override
    protected void populateModel(OperationContext context, ModelNode operation, Resource resource) throws OperationFailedException {
        if (operation.hasDefined(CommonAttributes.MOD_CLUSTER_CONFIG)) {
            PathAddress opAddress = PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR));
            PathAddress parent = opAddress.append(ModClusterExtension.CONFIGURATION_PATH);
            ModelNode targetOperation = Util.createAddOperation(parent);
            for (AttributeDefinition def : ModClusterConfigResourceDefinition.ATTRIBUTES) {
                def.validateAndSet(operation, targetOperation);
            }
            context.addStep(targetOperation, ModClusterConfigAdd.INSTANCE, OperationContext.Stage.IMMEDIATE);
        }
    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {

    }

    private ModClusterConfig getModClusterConfig(final OperationContext context, ModelNode model) throws OperationFailedException {
        ModClusterConfig config = new ModClusterConfig();
        config.setAdvertise(ADVERTISE.resolveModelAttribute(context, model).asBoolean());

        if (model.get(SSL_CONFIGURATION_PATH.getKeyValuePair()).isDefined()) {
            // Add SSL configuration.
            config.setSsl(true);
            final ModelNode ssl = model.get(SSL_CONFIGURATION_PATH.getKeyValuePair());
            ModelNode keyAlias = KEY_ALIAS.resolveModelAttribute(context, ssl);
            ModelNode password = PASSWORD.resolveModelAttribute(context, ssl);
            if (keyAlias.isDefined()) {
                config.setSslKeyAlias(keyAlias.asString());
            }
            if (password.isDefined()) {
                config.setSslTrustStorePassword(password.asString());
                config.setSslKeyStorePassword(password.asString());
            }
            if (ssl.hasDefined(CommonAttributes.CERTIFICATE_KEY_FILE)) {
                config.setSslKeyStore(CERTIFICATE_KEY_FILE.resolveModelAttribute(context, ssl).asString());
            }
            if (ssl.hasDefined(CommonAttributes.CIPHER_SUITE)) {
                config.setSslCiphers(CIPHER_SUITE.resolveModelAttribute(context, ssl).asString());
            }
            if (ssl.hasDefined(CommonAttributes.PROTOCOL)) {
                config.setSslProtocol(PROTOCOL.resolveModelAttribute(context, ssl).asString());
            }
            if (ssl.hasDefined(CommonAttributes.CA_CERTIFICATE_FILE)) {
                config.setSslTrustStore(CA_CERTIFICATE_FILE.resolveModelAttribute(context, ssl).asString());
            }
            if (ssl.hasDefined(CommonAttributes.CA_REVOCATION_URL)) {
                config.setSslCrlFile(CA_REVOCATION_URL.resolveModelAttribute(context, ssl).asString());
            }
        }
        if (model.hasDefined(CommonAttributes.PROXY_LIST)) {
            config.setProxyList(PROXY_LIST.resolveModelAttribute(context, model).asString());
        }
        if (model.hasDefined(CommonAttributes.ADVERTISE_SECURITY_KEY)) {
            config.setAdvertiseSecurityKey(ADVERTISE_SECURITY_KEY.resolveModelAttribute(context, model).asString());
        }
        config.setProxyURL(PROXY_URL.resolveModelAttribute(context, model).asString());
        config.setExcludedContexts(EXCLUDED_CONTEXTS.resolveModelAttribute(context, model).asString().trim());
        config.setAutoEnableContexts(AUTO_ENABLE_CONTEXTS.resolveModelAttribute(context, model).asBoolean());

        config.setStopContextTimeout(STOP_CONTEXT_TIMEOUT.resolveModelAttribute(context, model).asInt());
        config.setStopContextTimeoutUnit(TimeUnit.valueOf(STOP_CONTEXT_TIMEOUT.getMeasurementUnit().getName()));
        //config.setStopContextTimeoutUnit(TimeUnit.SECONDS); //todo use AttributeDefinition.getMeasurementUnit
        // the default value is 20000 = 20 seconds.
        config.setSocketTimeout(SOCKET_TIMEOUT.resolveModelAttribute(context, model).asInt() * 1000);
        config.setStickySession(STICKY_SESSION.resolveModelAttribute(context, model).asBoolean());
        config.setStickySessionRemove(STICKY_SESSION_REMOVE.resolveModelAttribute(context, model).asBoolean());
        config.setStickySessionForce(STICKY_SESSION_FORCE.resolveModelAttribute(context, model).asBoolean());
        config.setWorkerTimeout(WORKER_TIMEOUT.resolveModelAttribute(context, model).asInt());
        config.setMaxAttempts(MAX_ATTEMPTS.resolveModelAttribute(context, model).asInt());
        config.setFlushPackets(FLUSH_PACKETS.resolveModelAttribute(context, model).asBoolean());
        config.setFlushWait(FLUSH_WAIT.resolveModelAttribute(context, model).asInt());
        config.setPing(PING.resolveModelAttribute(context, model).asInt());
        config.setSmax(SMAX.resolveModelAttribute(context, model).asInt());
        config.setTtl(TTL.resolveModelAttribute(context, model).asInt());
        config.setNodeTimeout(NODE_TIMEOUT.resolveModelAttribute(context, model).asInt());

        if (model.hasDefined(CommonAttributes.BALANCER)) {
            config.setBalancer(BALANCER.resolveModelAttribute(context, model).asString());
        }
        if (model.hasDefined(CommonAttributes.LOAD_BALANCING_GROUP)) {
            config.setLoadBalancingGroup(LOAD_BALANCING_GROUP.resolveModelAttribute(context, model).asString());
        }
        return config;
    }

    private LoadBalanceFactorProvider getModClusterLoadProvider(final OperationContext context, ModelNode model) throws OperationFailedException {
        LoadBalanceFactorProvider load = null;
        if (model.hasDefined(CommonAttributes.SIMPLE_LOAD_PROVIDER_FACTOR)) {
            // TODO it seems we don't support that stuff.
            int value = ModClusterConfigResourceDefinition.SIMPLE_LOAD_PROVIDER.resolveModelAttribute(context, model).asInt(1);
            SimpleLoadBalanceFactorProvider myload = new SimpleLoadBalanceFactorProvider();
            myload.setLoadBalanceFactor(value);
            load = myload;
        }

        Set<LoadMetric> metrics = new HashSet<LoadMetric>();
        if (model.get(ModClusterExtension.DYNAMIC_LOAD_PROVIDER_PATH.getKeyValuePair()).isDefined()) {
            final ModelNode node = model.get(ModClusterExtension.DYNAMIC_LOAD_PROVIDER_PATH.getKeyValuePair());
            int decayFactor = DynamicLoadProviderDefinition.DECAY.resolveModelAttribute(context, model).asInt();
            int history = DynamicLoadProviderDefinition.HISTORY.resolveModelAttribute(context, model).asInt();
            if (node.hasDefined(CommonAttributes.LOAD_METRIC)) {
                addLoadMetrics(metrics, node.get(CommonAttributes.LOAD_METRIC), context);
            }
            if (node.hasDefined(CommonAttributes.CUSTOM_LOAD_METRIC)) {
                addLoadMetrics(metrics, node.get(CommonAttributes.CUSTOM_LOAD_METRIC), context);
            }
            if (!metrics.isEmpty()) {
                DynamicLoadBalanceFactorProvider loader = new DynamicLoadBalanceFactorProvider(metrics);
                loader.setDecayFactor(decayFactor);
                loader.setHistory(history);
                load = loader;
            }
        }
        if (load == null) {
            // Use a default one...
            ROOT_LOGGER.useDefaultLoadBalancer();
            SimpleLoadBalanceFactorProvider myload = new SimpleLoadBalanceFactorProvider();
            myload.setLoadBalanceFactor(1);
            load = myload;
        }
        return load;
    }


    private void addLoadMetrics(Set<LoadMetric> metrics, ModelNode nodes, final OperationContext context) throws OperationFailedException {
        for (Property p : nodes.asPropertyList()) {
            ModelNode node = p.getValue();
            double capacity = CAPACITY.resolveModelAttribute(context, node).asDouble();
            int weight = WEIGHT.resolveModelAttribute(context, node).asInt();
            Class<? extends LoadMetric> loadMetricClass = null;
            if (node.hasDefined(CommonAttributes.TYPE)) {
                String type = TYPE.resolveModelAttribute(context, node).asString();
                LoadMetricEnum metric = LoadMetricEnum.forType(type);
                loadMetricClass = (metric != null) ? metric.getLoadMetricClass() : null;
            } else {
                String className = CustomLoadMetricDefinition.CLASS.resolveModelAttribute(context, node).asString();
                try {
                    loadMetricClass = this.getClass().getClassLoader().loadClass(className).asSubclass(LoadMetric.class);
                } catch (ClassNotFoundException e) {
                    ROOT_LOGGER.errorAddingMetrics(e);
                }
            }

            if (loadMetricClass != null) {
                try {
                    LoadMetric metric = loadMetricClass.newInstance();
                    metric.setCapacity(capacity);
                    metric.setWeight(weight);
                    metrics.add(metric);
                } catch (InstantiationException e) {
                    ROOT_LOGGER.errorAddingMetrics(e);
                } catch (IllegalAccessException e) {
                    ROOT_LOGGER.errorAddingMetrics(e);
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13267.java