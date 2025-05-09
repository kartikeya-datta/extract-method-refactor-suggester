error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5993.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5993.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5993.java
text:
```scala
c@@onfiguration.addViewInterceptor(method, factory, InterceptorOrder.View.HOME_CREATE_INTERCEPTOR);

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
package org.jboss.as.ejb3.deployment.processors;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.ComponentStartService;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ee.component.DependencyConfigurator;
import org.jboss.as.ee.component.ViewConfiguration;
import org.jboss.as.ee.component.ViewConfigurator;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ee.component.deployers.AbstractComponentConfigProcessor;
import org.jboss.as.ee.component.interceptors.InterceptorOrder;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.component.EJBViewDescription;
import org.jboss.as.ejb3.component.session.SessionBeanComponentDescription;
import org.jboss.as.ejb3.component.session.SessionBeanHomeInterceptorFactory;
import org.jboss.as.ejb3.component.stateful.StatefulComponentDescription;
import org.jboss.as.ejb3.component.stateless.StatelessComponentDescription;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.msc.service.ServiceBuilder;

/**
 * Processor that hooks up home interfaces for session beans
 *
 * @author Stuart Douglas
 */
public class SessionBeanHomeProcessor extends AbstractComponentConfigProcessor {

    @Override
    protected void processComponentConfig(final DeploymentUnit deploymentUnit, final DeploymentPhaseContext phaseContext, final CompositeIndex index, final ComponentDescription componentDescription) throws DeploymentUnitProcessingException {

        if (componentDescription instanceof SessionBeanComponentDescription) {
            final SessionBeanComponentDescription ejbComponentDescription = (SessionBeanComponentDescription) componentDescription;

            //check for EJB's with a local home interface
            if (ejbComponentDescription.getEjbLocalHomeView() != null) {
                final EJBViewDescription view = ejbComponentDescription.getEjbLocalHomeView();
                final EJBViewDescription ejbLocalView = ejbComponentDescription.getEjbLocalView();
                configureHome(phaseContext, componentDescription, ejbComponentDescription, view, ejbLocalView);
            }
            if (ejbComponentDescription.getEjbHomeView() != null) {
                final EJBViewDescription view = ejbComponentDescription.getEjbHomeView();
                final EJBViewDescription ejbRemoteView = ejbComponentDescription.getEjbRemoteView();
                configureHome(phaseContext, componentDescription, ejbComponentDescription, view, ejbRemoteView);
            }
        }
    }

    private void configureHome(final DeploymentPhaseContext phaseContext, final ComponentDescription componentDescription, final SessionBeanComponentDescription ejbComponentDescription, final EJBViewDescription homeView, final EJBViewDescription ejbObjectView) {
        homeView.getConfigurators().add(new ViewConfigurator() {

            @Override
            public void configure(final DeploymentPhaseContext context, final ComponentConfiguration componentConfiguration, final ViewDescription description, final ViewConfiguration configuration) throws DeploymentUnitProcessingException {
                final DeploymentReflectionIndex reflectionIndex = phaseContext.getDeploymentUnit().getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);

                configuration.addClientPostConstructInterceptor(org.jboss.invocation.Interceptors.getTerminalInterceptorFactory(), InterceptorOrder.ClientPostConstruct.TERMINAL_INTERCEPTOR);
                configuration.addClientPreDestroyInterceptor(org.jboss.invocation.Interceptors.getTerminalInterceptorFactory(), InterceptorOrder.ClientPreDestroy.TERMINAL_INTERCEPTOR);

                //loop over methods looking for create methods:
                final ClassReflectionIndex<?> classIndex = reflectionIndex.getClassIndex(configuration.getViewClass());
                for (Method method : classIndex.getMethods()) {
                    if (method.getName().startsWith("create")) {
                        //we have a create method
                        if(ejbObjectView == null) {
                            throw new RuntimeException(componentDescription.getComponentName() + " does not have a EJB 2.x local interface");
                        }
                        final ViewDescription createdView = ejbObjectView;

                        Method initMethod = resolveInitMethod(ejbComponentDescription, method);
                        final SessionBeanHomeInterceptorFactory factory = new SessionBeanHomeInterceptorFactory(initMethod);
                        //add a dependency on the view to create
                        componentConfiguration.getStartDependencies().add(new DependencyConfigurator<ComponentStartService>() {
                            @Override
                            public void configureDependency(final ServiceBuilder<?> serviceBuilder, final ComponentStartService service) throws DeploymentUnitProcessingException {
                                serviceBuilder.addDependency(createdView.getServiceName(), ComponentView.class, factory.getViewToCreate());
                            }
                        });
                        //add the interceptor
                        configuration.addClientInterceptor(method, ViewDescription.CLIENT_DISPATCHER_INTERCEPTOR_FACTORY, InterceptorOrder.Client.CLIENT_DISPATCHER);
                        configuration.addViewInterceptor(method, factory, InterceptorOrder.View.COMPONENT_DISPATCHER);

                    }
                }
            }

        });
    }


    private Method resolveInitMethod(final EJBComponentDescription description, final Method method) throws DeploymentUnitProcessingException {
        if (description instanceof StatelessComponentDescription) {
            return null;
        } else if (description instanceof StatefulComponentDescription) {
            return resolveStatefulInitMethod((StatefulComponentDescription) description, method);
        } else {
            throw new DeploymentUnitProcessingException("Local Home not allowed for " + description);
        }
    }


    private Method resolveStatefulInitMethod(final StatefulComponentDescription description, final Method method) throws DeploymentUnitProcessingException {

        //for a SFSB we need to resolve the corresponding init method for this create method

        Method initMethod = null;
        //first we try and resolve methods that have additiona resolution data associated with them
        for (Map.Entry<Method, String> entry : description.getInitMethods().entrySet()) {
            String name = entry.getValue();
            Method init = entry.getKey();
            if (name != null) {
                if (Arrays.equals(init.getParameterTypes(), method.getParameterTypes())) {
                    if (init.getName().equals(name)) {
                        initMethod = init;
                    }
                }
            }
        }
        //now try and resolve the init methods with no additional resolution data
        if (initMethod == null) {
            for (Map.Entry<Method, String> entry : description.getInitMethods().entrySet()) {
                Method init = entry.getKey();
                if (entry.getValue() == null) {
                    if (Arrays.equals(init.getParameterTypes(), method.getParameterTypes())) {
                        initMethod = init;
                        break;
                    }
                }
            }
        }
        if (initMethod == null) {
            throw new DeploymentUnitProcessingException("Could not resolve corresponding ejbCreate or @Init method for home interface method " + method + " on EJB " + description.getEJBClassName());
        }
        return initMethod;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5993.java