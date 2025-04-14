error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14357.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14357.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14357.java
text:
```scala
final W@@eldManagedReferenceFactory factory = new WeldManagedReferenceFactory(componentClass, beanName, interceptorClasses, classLoader, beanDeploymentArchiveId, description.isCDIInterceptorEnabled());

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.weld.deployment.processors;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.InterceptionType;

import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentConfigurator;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.ComponentStartService;
import org.jboss.as.ee.component.DependencyConfigurator;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.InterceptorDescription;
import org.jboss.as.ee.component.interceptors.InterceptorOrder;
import org.jboss.as.ee.component.interceptors.UserInterceptorFactory;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.component.stateful.SerializedCdiInterceptorsKey;
import org.jboss.as.ejb3.component.stateful.StatefulComponentDescription;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.reflect.ClassIndex;
import org.jboss.as.server.deployment.reflect.DeploymentClassIndex;
import org.jboss.as.weld.WeldContainer;
import org.jboss.as.weld.WeldDeploymentMarker;
import org.jboss.as.weld.WeldMessages;
import org.jboss.as.weld.ejb.EjbRequestScopeActivationInterceptor;
import org.jboss.as.weld.ejb.Jsr299BindingsInterceptor;
import org.jboss.as.weld.injection.WeldInjectionInterceptor;
import org.jboss.as.weld.injection.WeldManagedReferenceFactory;
import org.jboss.as.weld.services.WeldService;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;

/**
 * Deployment unit processor that add the {@link org.jboss.as.weld.injection.WeldManagedReferenceFactory} instantiator
 * to components that are part of a bean archive.
 *
 * @author Stuart Douglas
 */
public class WeldComponentIntegrationProcessor implements DeploymentUnitProcessor {

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final DeploymentClassIndex classIndex = deploymentUnit.getAttachment(Attachments.CLASS_INDEX);

        if (!WeldDeploymentMarker.isWeldDeployment(deploymentUnit)) {
            return;
        }


        final DeploymentUnit topLevelDeployment = deploymentUnit.getParent() == null ? deploymentUnit : deploymentUnit.getParent();
        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        final ServiceName weldServiceName = topLevelDeployment.getServiceName().append(WeldService.SERVICE_NAME);

        for (ComponentDescription component : eeModuleDescription.getComponentDescriptions()) {
            final String beanName;
            if (component instanceof EJBComponentDescription) {
                beanName = component.getComponentName();

            } else {
                beanName = null;
            }
            component.getConfigurators().addFirst(new ComponentConfigurator() {
                @Override
                public void configure(final DeploymentPhaseContext context, final ComponentDescription description, final ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
                    final Class<?> componentClass = configuration.getComponentClass();
                    final DeploymentUnit deploymentUnit = context.getDeploymentUnit();
                    final ModuleClassLoader classLoader = deploymentUnit.getAttachment(Attachments.MODULE).getClassLoader();

                    //get the interceptors so they can be injected as well
                    final Set<Class<?>> interceptorClasses = new HashSet<Class<?>>();
                    for (InterceptorDescription interceptorDescription : description.getAllInterceptors()) {
                        try {
                            final ClassIndex index = classIndex.classIndex(interceptorDescription.getInterceptorClassName());
                            interceptorClasses.add(index.getModuleClass());
                        } catch (ClassNotFoundException e) {
                            throw WeldMessages.MESSAGES.couldNotLoadInterceptorClass(interceptorDescription.getInterceptorClassName(), e);
                        }
                    }

                    addWeldIntegration(context.getServiceTarget(), configuration, description, componentClass, beanName, weldServiceName, interceptorClasses, classLoader, description.getBeanDeploymentArchiveId());

                    configuration.addPostConstructInterceptor(new WeldInjectionInterceptor.Factory(configuration, interceptorClasses), InterceptorOrder.ComponentPostConstruct.WELD_INJECTION);

                    //add a context key for weld interceptor replication
                    if (description instanceof StatefulComponentDescription) {
                        configuration.getInterceptorContextKeys().add(SerializedCdiInterceptorsKey.class);
                    }
                }
            });
        }

    }

    /**
     * As the weld based instantiator needs access to the bean manager it is installed as a service.
     */
    private void addWeldIntegration(final ServiceTarget target, final ComponentConfiguration configuration, final ComponentDescription description, final Class<?> componentClass, final String beanName, final ServiceName weldServiceName, final Set<Class<?>> interceptorClasses, final ClassLoader classLoader, final String beanDeploymentArchiveId) {

        final ServiceName serviceName = configuration.getComponentDescription().getServiceName().append("WeldInstantiator");

        final WeldManagedReferenceFactory factory = new WeldManagedReferenceFactory(componentClass, beanName, interceptorClasses, classLoader, beanDeploymentArchiveId);

        ServiceBuilder<WeldManagedReferenceFactory> builder = target.addService(serviceName, factory)
                .addDependency(weldServiceName, WeldContainer.class, factory.getWeldContainer());


        configuration.setInstanceFactory(factory);
        configuration.getStartDependencies().add(new DependencyConfigurator<ComponentStartService>() {
            @Override
            public void configureDependency(final ServiceBuilder<?> serviceBuilder, ComponentStartService service) throws DeploymentUnitProcessingException {
                serviceBuilder.addDependency(serviceName);
            }
        });

        //if this is an ejb add the EJB interceptors
        if (description instanceof EJBComponentDescription) {

            //add interceptor to activate the request scope if required
            final EjbRequestScopeActivationInterceptor.Factory requestFactory = new EjbRequestScopeActivationInterceptor.Factory(weldServiceName);
            configuration.addComponentInterceptor(requestFactory, InterceptorOrder.Component.CDI_REQUEST_SCOPE, false);

            final Jsr299BindingsInterceptor.Factory aroundInvokeFactory = new Jsr299BindingsInterceptor.Factory(description.getBeanDeploymentArchiveId(), beanName, InterceptionType.AROUND_INVOKE, classLoader);
            final Jsr299BindingsInterceptor.Factory aroundTimeoutFactory = new Jsr299BindingsInterceptor.Factory(description.getBeanDeploymentArchiveId(), beanName, InterceptionType.AROUND_TIMEOUT, classLoader);

            builder.addDependency(weldServiceName, WeldContainer.class, aroundTimeoutFactory.getWeldContainer());
            builder.addDependency(weldServiceName, WeldContainer.class, aroundInvokeFactory.getWeldContainer());

            configuration.addComponentInterceptor(new UserInterceptorFactory(aroundInvokeFactory, aroundTimeoutFactory), InterceptorOrder.Component.CDI_INTERCEPTORS, false);

            final Jsr299BindingsInterceptor.Factory preDestroyInterceptor = new Jsr299BindingsInterceptor.Factory(description.getBeanDeploymentArchiveId(), beanName, InterceptionType.PRE_DESTROY, classLoader);
            builder.addDependency(weldServiceName, WeldContainer.class, preDestroyInterceptor.getWeldContainer());
            configuration.addPreDestroyInterceptor(preDestroyInterceptor, InterceptorOrder.ComponentPreDestroy.CDI_INTERCEPTORS);

            if (description.isPassivationApplicable()) {
                final Jsr299BindingsInterceptor.Factory prePassivateInterceptor = new Jsr299BindingsInterceptor.Factory(description.getBeanDeploymentArchiveId(), beanName, InterceptionType.PRE_PASSIVATE, classLoader);
                builder.addDependency(weldServiceName, WeldContainer.class, prePassivateInterceptor.getWeldContainer());
                configuration.addPrePassivateInterceptor(prePassivateInterceptor, InterceptorOrder.ComponentPassivation.CDI_INTERCEPTORS);
                final Jsr299BindingsInterceptor.Factory postActivateInterceptor = new Jsr299BindingsInterceptor.Factory(description.getBeanDeploymentArchiveId(), beanName, InterceptionType.POST_ACTIVATE, classLoader);
                builder.addDependency(weldServiceName, WeldContainer.class, postActivateInterceptor.getWeldContainer());
                configuration.addPostActivateInterceptor(postActivateInterceptor, InterceptorOrder.ComponentPassivation.CDI_INTERCEPTORS);
            }

            final Jsr299BindingsInterceptor.Factory postConstruct = new Jsr299BindingsInterceptor.Factory(description.getBeanDeploymentArchiveId(), beanName, InterceptionType.POST_CONSTRUCT, classLoader);
            builder.addDependency(weldServiceName, WeldContainer.class, postConstruct.getWeldContainer());
            configuration.addPostConstructInterceptor(postConstruct, InterceptorOrder.ComponentPostConstruct.CDI_INTERCEPTORS);

        }

        builder.install();

    }

    @Override
    public void undeploy(DeploymentUnit context) {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14357.java