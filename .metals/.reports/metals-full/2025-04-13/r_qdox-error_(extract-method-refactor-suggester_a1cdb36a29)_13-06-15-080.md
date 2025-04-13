error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10928.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10928.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10928.java
text:
```scala
private S@@tring unmaskPassword(OperationContext context, ModelNode model) throws OperationFailedException {

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

import static org.jboss.as.modcluster.ModClusterLogger.ROOT_LOGGER;

import java.util.List;
import java.util.Locale;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.network.SocketBinding;
import org.jboss.as.network.SocketBindingManager;
import org.jboss.as.web.WebServer;
import org.jboss.as.web.WebSubsystemServices;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;

/**
 * The managed subsystem add update.
 *
 * @author Jean-Frederic Clere
 */
class ModClusterSubsystemAdd extends AbstractAddStepHandler implements DescriptionProvider {

    static final ModClusterSubsystemAdd INSTANCE = new ModClusterSubsystemAdd();
    static final PathElement SSLPath = PathElement.pathElement(CommonAttributes.SSL, CommonAttributes.CONFIGURATION);
    static final PathElement confPath = PathElement.pathElement(CommonAttributes.MOD_CLUSTER_CONFIG, CommonAttributes.CONFIGURATION);
    @Override
    protected void populateModel(final ModelNode operation, final Resource resource) {
         if (operation.hasDefined(CommonAttributes.MOD_CLUSTER_CONFIG)) {
            ModelNode configuration;
            if (operation.get(CommonAttributes.MOD_CLUSTER_CONFIG).hasDefined(CommonAttributes.CONFIGURATION))
                configuration = operation.get(CommonAttributes.MOD_CLUSTER_CONFIG).get(CommonAttributes.CONFIGURATION);
            else
                configuration = operation.get(CommonAttributes.MOD_CLUSTER_CONFIG);
            resource.registerChild(confPath, Resource.Factory.create());
            final Resource conf = resource.getChild(confPath);
            for(final String attribute : configuration.keys()) {
                if (attribute.equals(CommonAttributes.SSL)) {
                    conf.registerChild(SSLPath, Resource.Factory.create());
                    final Resource ssl = conf.getChild(SSLPath);
                    ModelNode sslnode;
                    if (configuration.get(attribute).hasDefined(CommonAttributes.CONFIGURATION))
                        sslnode = configuration.get(attribute).get(CommonAttributes.CONFIGURATION);
                    else
                        sslnode = configuration.get(attribute);
                    populateConf(ssl.getModel(), sslnode);
                }
                else
                    conf.getModel().get(attribute).set(configuration.get(attribute));
            }
        }
    }

    static void populateConf(final ModelNode subModel, final ModelNode operation) {
        for(final String attribute : operation.keys()) {
            if(operation.hasDefined(attribute)) {
                subModel.get(attribute).set(operation.get(attribute));
            }
        }
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {
        String bindingRef = null;
        ModelNode node = operation.get(CommonAttributes.MOD_CLUSTER_CONFIG);
        if (operation.hasDefined(CommonAttributes.MOD_CLUSTER_CONFIG)) {
            if (operation.get(CommonAttributes.MOD_CLUSTER_CONFIG).hasDefined(CommonAttributes.CONFIGURATION))
                node = operation.get(CommonAttributes.MOD_CLUSTER_CONFIG).get(CommonAttributes.CONFIGURATION);
            if (node.hasDefined(CommonAttributes.ADVERTISE_SOCKET)) {
                bindingRef = node.get(CommonAttributes.ADVERTISE_SOCKET).asString();
            }
        }
        try {

            //Get the unmasked password
            // Add mod_cluster service
            final ModClusterService service = new ModClusterService(unmaskPassword(context, model), node.clone());
            final ServiceBuilder<ModCluster> serviceBuilder = context.getServiceTarget().addService(ModClusterService.NAME, service)
                    // .addListener(new ResultHandler.ServiceStartListener(resultHandler))
                    .addDependency(WebSubsystemServices.JBOSS_WEB, WebServer.class, service.getWebServer())
                    .addDependency(SocketBindingManager.SOCKET_BINDING_MANAGER, SocketBindingManager.class, service.getBindingManager())
                    .addListener(verificationHandler)
                    .setInitialMode(Mode.ACTIVE);
             if (bindingRef != null)
                serviceBuilder.addDependency(SocketBinding.JBOSS_BINDING_NAME.append(bindingRef), SocketBinding.class, service.getBinding());

            newControllers.add(serviceBuilder.install());
        } catch (Throwable t) {
            ROOT_LOGGER.debugf("Error: %s", t);
            throw new OperationFailedException(new ModelNode().set(t.getLocalizedMessage()));
        }
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        return ModClusterSubsystemDescriptions.getSubsystemAddDescription(locale);
    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) {
        // TODO Auto-generated method stub

    }

    private String unmaskPassword(OperationContext context, ModelNode model) {
        if (!model.hasDefined(CommonAttributes.SSL)) {
            return null;
        }
        if (!model.get(CommonAttributes.SSL).hasDefined(CommonAttributes.PASSWORD)) {
            return null;
        }
        return context.resolveExpressions(model.get(CommonAttributes.SSL, CommonAttributes.PASSWORD)).toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10928.java