error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8373.java
text:
```scala
protected v@@oid setupViewInterceptors(EJBViewDescription view) {

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

package org.jboss.as.ejb3.component.stateless;


import java.lang.reflect.Method;

import javax.ejb.TransactionManagementType;

import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentConfigurator;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.ComponentInstanceInterceptorFactory;
import org.jboss.as.ee.component.DependencyConfigurator;
import org.jboss.as.ee.component.ViewConfiguration;
import org.jboss.as.ee.component.ViewConfigurator;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ee.component.interceptors.InterceptorOrder;
import org.jboss.as.ejb3.component.ComponentTypeIdentityInterceptorFactory;
import org.jboss.as.ejb3.component.EJBViewDescription;
import org.jboss.as.ejb3.component.MethodIntf;
import org.jboss.as.ejb3.component.pool.PoolConfig;
import org.jboss.as.ejb3.component.pool.PoolConfigService;
import org.jboss.as.ejb3.component.session.SessionBeanComponentDescription;
import org.jboss.as.ejb3.component.session.StatelessRemoteViewInstanceFactory;
import org.jboss.as.ejb3.deployment.EjbJarDescription;
import org.jboss.as.ejb3.tx.TimerCMTTxInterceptorFactory;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.reflect.ClassIndex;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorFactoryContext;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;

/**
 * User: jpai
 */
public class StatelessComponentDescription extends SessionBeanComponentDescription {

    private String poolConfigName;

    /**
     * Construct a new instance.
     *
     * @param componentName        the component name
     * @param componentClassName   the component instance class name
     * @param ejbModuleDescription the module description
     */
    public StatelessComponentDescription(final String componentName, final String componentClassName, final EjbJarDescription ejbModuleDescription,
                                         final ServiceName deploymentUnitServiceName) {
        super(componentName, componentClassName, ejbModuleDescription, deploymentUnitServiceName);
    }

    @Override
    public ComponentConfiguration createConfiguration(final ClassIndex classIndex) {
        final ComponentConfiguration statelessComponentConfiguration = new ComponentConfiguration(this, classIndex);
        // setup the component create service
        statelessComponentConfiguration.setComponentCreateServiceFactory(new StatelessComponentCreateServiceFactory());

        // setup the configurator to inject the PoolConfig in the StatelessSessionComponentCreateService
        final StatelessComponentDescription  statelessComponentDescription = (StatelessComponentDescription) statelessComponentConfiguration.getComponentDescription();
        statelessComponentConfiguration.getCreateDependencies().add(new PoolInjectingConfigurator(statelessComponentDescription));

        // add the bmt interceptor
        if (TransactionManagementType.BEAN.equals(this.getTransactionManagementType())) {
            getConfigurators().add(new ComponentConfigurator() {
                @Override
                public void configure(final DeploymentPhaseContext context, final ComponentDescription description, final ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
                    final ComponentInstanceInterceptorFactory slsbBmtInterceptorFactory = new ComponentInstanceInterceptorFactory() {
                        @Override
                        protected Interceptor create(Component component, InterceptorFactoryContext context) {
                            if (!(component instanceof StatelessSessionComponent)) {
                                throw new IllegalArgumentException("Component " + component + " with component class: " + component.getComponentClass() +
                                        " isn't a stateless component");
                            }
                            return new StatelessBMTInterceptor((StatelessSessionComponent) component);
                        }
                    };
                    // add the bmt interceptor factory
                    configuration.addComponentInterceptor(slsbBmtInterceptorFactory, InterceptorOrder.Component.BMT_TRANSACTION_INTERCEPTOR, false);
                    configuration.addTimeoutInterceptor(slsbBmtInterceptorFactory, InterceptorOrder.Component.BMT_TRANSACTION_INTERCEPTOR);
                }
            });
        } else {
            getConfigurators().add(new ComponentConfigurator() {
                @Override
                public void configure(final DeploymentPhaseContext context, final ComponentDescription description, final ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
                    configuration.addTimeoutInterceptor(TimerCMTTxInterceptorFactory.INSTANCE, InterceptorOrder.Component.TIMEOUT_CMT_INTERCEPTOR);
                }
            });
        }


        return statelessComponentConfiguration;
    }

    @Override
    public boolean allowsConcurrentAccess() {
        return false;
    }

    @Override
    public SessionBeanType getSessionBeanType() {
        return SessionBeanComponentDescription.SessionBeanType.STATELESS;
    }

    @Override
    protected void setupViewInterceptors(ViewDescription view) {
        // let super do its job first
        super.setupViewInterceptors(view);

        // add the instance associating interceptor at the start of the interceptor chain
        view.getConfigurators().addFirst(new ViewConfigurator() {
            @Override
            public void configure(DeploymentPhaseContext context, ComponentConfiguration componentConfiguration, ViewDescription description, ViewConfiguration configuration) throws DeploymentUnitProcessingException {

                //add equals/hashCode interceptor
                //add equals/hashCode interceptor
                for (Method method : configuration.getProxyFactory().getCachedMethods()) {
                    if ((method.getName().equals("hashCode") && method.getParameterTypes().length == 0) ||
                            method.getName().equals("equals") && method.getParameterTypes().length == 1 &&
                                    method.getParameterTypes()[0] == Object.class) {
                        configuration.addClientInterceptor(method, ComponentTypeIdentityInterceptorFactory.INSTANCE, InterceptorOrder.Client.EJB_EQUALS_HASHCODE);
                    }
                }

                // add the stateless component instance associating interceptor
                configuration.addViewInterceptor(StatelessComponentInstanceAssociatingFactory.instance(), InterceptorOrder.View.ASSOCIATING_INTERCEPTOR);            }
        });


        if (view instanceof EJBViewDescription) {
            EJBViewDescription ejbViewDescription = (EJBViewDescription) view;
            if(ejbViewDescription.getMethodIntf() == MethodIntf.REMOTE || ejbViewDescription.getMethodIntf() == MethodIntf.REMOTE) {
                view.getConfigurators().add(new ViewConfigurator() {
                    @Override
                    public void configure(final DeploymentPhaseContext context, final ComponentConfiguration componentConfiguration, final ViewDescription description, final ViewConfiguration configuration) throws DeploymentUnitProcessingException {
                        configuration.setViewInstanceFactory(new StatelessRemoteViewInstanceFactory(componentConfiguration.getApplicationName(), componentConfiguration.getModuleName(), componentConfiguration.getComponentDescription().getModuleDescription().getDistinctName(), componentConfiguration.getComponentName()));
                    }
                });
            }
        }

    }

    @Override
    public boolean isTimerServiceApplicable() {
        return true;
    }

    public void setPoolConfigName(final String poolConfigName) {
        this.poolConfigName = poolConfigName;
    }

    public String getPoolConfigName() {
        return this.poolConfigName;
    }

    private class PoolInjectingConfigurator implements DependencyConfigurator<Service<Component>> {

        private final StatelessComponentDescription statelessComponentDescription;

        PoolInjectingConfigurator(final StatelessComponentDescription statelessComponentDescription) {
            this.statelessComponentDescription = statelessComponentDescription;
        }

        @Override
        public void configureDependency(ServiceBuilder<?> serviceBuilder, Service<Component> service) throws DeploymentUnitProcessingException {
            final StatelessSessionComponentCreateService statelessSessionComponentService = (StatelessSessionComponentCreateService) service;
            final String poolName = this.statelessComponentDescription.getPoolConfigName();
            // if no pool name has been explicitly set, then inject the optional "default slsb pool config"
            if (poolName == null) {
                serviceBuilder.addDependency(ServiceBuilder.DependencyType.OPTIONAL, PoolConfigService.DEFAULT_SLSB_POOL_CONFIG_SERVICE_NAME,
                        PoolConfig.class, statelessSessionComponentService.getPoolConfigInjector());
            } else {
                // pool name has been explicitly set so the pool config is a required dependency
                serviceBuilder.addDependency(PoolConfigService.EJB_POOL_CONFIG_BASE_SERVICE_NAME.append(poolName),
                        PoolConfig.class, statelessSessionComponentService.getPoolConfigInjector());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8373.java