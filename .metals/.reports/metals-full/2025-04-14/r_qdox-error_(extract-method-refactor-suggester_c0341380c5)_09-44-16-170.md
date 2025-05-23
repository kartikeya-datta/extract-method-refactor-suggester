error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14885.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14885.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14885.java
text:
```scala
i@@f(ejbViewDescription.getMethodIntf() == MethodIntf.REMOTE) {

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

package org.jboss.as.ejb3.component.stateful;


import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.TransactionManagementType;

import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentConfigurator;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.ComponentInstanceInterceptorFactory;
import org.jboss.as.ee.component.ViewConfiguration;
import org.jboss.as.ee.component.ViewConfigurator;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ee.component.interceptors.InterceptorOrder;
import org.jboss.as.ee.component.serialization.WriteReplaceInterface;
import org.jboss.as.ejb3.component.DefaultAccessTimeoutService;
import org.jboss.as.ejb3.component.EJBViewDescription;
import org.jboss.as.ejb3.component.MethodIntf;
import org.jboss.as.ejb3.component.session.SessionBeanComponentDescription;
import org.jboss.as.ejb3.deployment.EjbJarDescription;
import org.jboss.as.ejb3.tx.StatefulBMTInterceptor;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.reflect.ClassIndex;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.invocation.ImmediateInterceptorFactory;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.InterceptorFactoryContext;
import org.jboss.invocation.proxy.MethodIdentifier;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceName;

/**
 * User: jpai
 */
public class StatefulComponentDescription extends SessionBeanComponentDescription {

    private static final Logger logger = Logger.getLogger(StatefulComponentDescription.class);

    private Method afterBegin;
    private Method afterCompletion;
    private Method beforeCompletion;
    private final Set<StatefulRemoveMethod> removeMethods = new HashSet<StatefulRemoveMethod>();
    private StatefulTimeoutInfo statefulTimeout;

    private DefaultAccessTimeoutService defaultAccessTimeoutProvider;

    /**
     * Map of init method, to the corresponding home create method on the home interface
     *
     */
    private Map<Method, String> initMethods = new HashMap<Method, String>(0);

    public class StatefulRemoveMethod {
        private final MethodIdentifier methodIdentifier;
        private final boolean retainIfException;

        StatefulRemoveMethod(final MethodIdentifier method, final boolean retainIfException) {
            if (method == null) {
                throw new IllegalArgumentException("@Remove method cannot be null");
            }
            this.methodIdentifier = method;
            this.retainIfException = retainIfException;
        }

        public MethodIdentifier getMethodIdentifier() {
            return methodIdentifier;
        }

        public boolean isRetainIfException() {
            return retainIfException;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StatefulRemoveMethod that = (StatefulRemoveMethod) o;

            if (!methodIdentifier.equals(that.methodIdentifier)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return methodIdentifier.hashCode();
        }
    }

    /**
     * Construct a new instance.
     *
     * @param componentName      the component name
     * @param componentClassName the component instance class name
     * @param ejbJarDescription  the module description
     */
    public StatefulComponentDescription(final String componentName, final String componentClassName, final EjbJarDescription ejbJarDescription,
                                        final ServiceName deploymentUnitServiceName) {
        super(componentName, componentClassName, ejbJarDescription, deploymentUnitServiceName);

        addStatefulSessionSynchronizationInterceptor();
        addInitMethodInvokingInterceptor();
    }

    private void addInitMethodInvokingInterceptor() {
        getConfigurators().addFirst(new ComponentConfigurator() {
            @Override
            public void configure(DeploymentPhaseContext context, ComponentDescription description, ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
                configuration.addPostConstructInterceptor(StatefulInitMethodInterceptorFactory.INSTANCE, InterceptorOrder.ComponentPostConstruct.SFSB_INIT_METHOD);
            }
        });
    }

    private void addStatefulSessionSynchronizationInterceptor() {
        // we must run before the DefaultFirstConfigurator
        getConfigurators().addFirst(new ComponentConfigurator() {
            @Override
            public void configure(DeploymentPhaseContext context, ComponentDescription description, ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
                final InterceptorFactory interceptorFactory = new ComponentInstanceInterceptorFactory() {
                    @Override
                    protected Interceptor create(Component component, InterceptorFactoryContext context) {
                        return new StatefulSessionSynchronizationInterceptor();
                    }
                };
                configuration.addComponentInterceptor(interceptorFactory, InterceptorOrder.Component.SYNCHRONIZATION_INTERCEPTOR, false);
            }
        });

    }

    @Override
    public ComponentConfiguration createConfiguration(final ClassIndex classIndex) {

        final ComponentConfiguration statefulComponentConfiguration = new ComponentConfiguration(this, classIndex);
        // setup the component create service
        statefulComponentConfiguration.setComponentCreateServiceFactory(new StatefulComponentCreateServiceFactory());

        if (getTransactionManagementType() == TransactionManagementType.BEAN) {
            getConfigurators().add(new ComponentConfigurator() {
                @Override
                public void configure(final DeploymentPhaseContext context, final ComponentDescription description, final ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
                    final ComponentInstanceInterceptorFactory bmtComponentInterceptorFactory = new ComponentInstanceInterceptorFactory() {
                        @Override
                        protected Interceptor create(Component component, InterceptorFactoryContext context) {
                            if (!(component instanceof StatefulSessionComponent)) {
                                throw new IllegalArgumentException("Component " + component + " with component class: " + component.getComponentClass() +
                                        " isn't a stateful component");
                            }
                            return new StatefulBMTInterceptor((StatefulSessionComponent) component);
                        }
                    };
                    configuration.addComponentInterceptor(bmtComponentInterceptorFactory, InterceptorOrder.Component.BMT_TRANSACTION_INTERCEPTOR, false);
                }
            });
        }

        return statefulComponentConfiguration;
    }

    @Override
    public boolean allowsConcurrentAccess() {
        return true;
    }

    public Method getAfterBegin() {
        return afterBegin;
    }

    public Method getAfterCompletion() {
        return afterCompletion;
    }

    public Method getBeforeCompletion() {
        return beforeCompletion;
    }

    @Override
    public SessionBeanType getSessionBeanType() {
        return SessionBeanComponentDescription.SessionBeanType.STATEFUL;
    }

    public void setAfterBegin(final Method afterBegin) {
        this.afterBegin = afterBegin;
    }

    public void setAfterCompletion(final  Method afterCompletion) {
        this.afterCompletion = afterCompletion;
    }

    public void setBeforeCompletion(final Method afterCompletion) {
        this.beforeCompletion = afterCompletion;
    }

    @Override
    protected void setupViewInterceptors(EJBViewDescription view) {
        // let super do its job
        super.setupViewInterceptors(view);
        // add the @Remove method interceptor
        this.addRemoveMethodInterceptor(view);
        // setup the instance associating interceptors
        this.addStatefulInstanceAssociatingInterceptor(view);

        this.addViewSerializationInterceptor(view);

        if (view instanceof EJBViewDescription) {
            EJBViewDescription ejbViewDescription = (EJBViewDescription) view;
            if(ejbViewDescription.getMethodIntf() == MethodIntf.REMOTE || ejbViewDescription.getMethodIntf() == MethodIntf.REMOTE) {
                view.getConfigurators().add(new ViewConfigurator() {
                    @Override
                    public void configure(final DeploymentPhaseContext context, final ComponentConfiguration componentConfiguration, final ViewDescription description, final ViewConfiguration configuration) throws DeploymentUnitProcessingException {
                        configuration.setViewInstanceFactory(new StatefulRemoteViewInstanceFactory(componentConfiguration.getApplicationName(), componentConfiguration.getModuleName(), componentConfiguration.getComponentDescription().getModuleDescription().getDistinctName(), componentConfiguration.getComponentName()));
                    }
                });
            }
        }
    }

    @Override
    protected ViewConfigurator getSessionBeanObjectViewConfigurator() {
        return StatefulSessionBeanObjectViewConfigurator.INSTANCE;
    }

    private void addViewSerializationInterceptor(final ViewDescription view) {
        view.setSerializable(true);
        view.setUseWriteReplace(true);
        view.getConfigurators().add(new ViewConfigurator() {
            @Override
            public void configure(final DeploymentPhaseContext context, final ComponentConfiguration componentConfiguration, final ViewDescription description, final ViewConfiguration configuration) throws DeploymentUnitProcessingException {
                final DeploymentReflectionIndex index = context.getDeploymentUnit().getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);
                ClassReflectionIndex<WriteReplaceInterface> classIndex = index.getClassIndex(WriteReplaceInterface.class);
                for(Method method : classIndex.getMethods()) {
                    configuration.addClientInterceptor(method, new WriteReplaceInterceptor.Factory(configuration.getViewServiceName().getCanonicalName()), InterceptorOrder.Client.WRITE_REPLACE);
                }
            }
        });
    }

    public void addRemoveMethod(final MethodIdentifier removeMethod, final boolean retainIfException) {
        if (removeMethod == null) {
            throw new IllegalArgumentException("@Remove method identifier cannot be null");
        }
        this.removeMethods.add(new StatefulRemoveMethod(removeMethod, retainIfException));
    }

    public Set<StatefulRemoveMethod> getRemoveMethods() {
        return Collections.unmodifiableSet(this.removeMethods);
    }

    public StatefulTimeoutInfo getStatefulTimeout() {
        return statefulTimeout;
    }

    public void setStatefulTimeout(final StatefulTimeoutInfo statefulTimeout) {
        this.statefulTimeout = statefulTimeout;
    }

    private void addStatefulInstanceAssociatingInterceptor(final ViewDescription view) {
        view.getConfigurators().add(new ViewConfigurator() {
            @Override
            public void configure(DeploymentPhaseContext context, ComponentConfiguration componentConfiguration, ViewDescription description, ViewConfiguration viewConfiguration) throws DeploymentUnitProcessingException {
                // interceptor factory return an interceptor which sets up the session id on component view instance creation
                InterceptorFactory sessionIdGeneratingInterceptorFactory = StatefulComponentSessionIdGeneratingInterceptorFactory.INSTANCE;

                // add the session id generating interceptor to the start of the *post-construct interceptor chain of the ComponentViewInstance*
                viewConfiguration.addClientPostConstructInterceptor(sessionIdGeneratingInterceptorFactory, InterceptorOrder.ClientPostConstruct.INSTANCE_CREATE);
                viewConfiguration.addClientPreDestroyInterceptor(StatefulComponentInstanceDestroyInterceptorFactory.INSTANCE, InterceptorOrder.ClientPreDestroy.INSTANCE_DESTROY);

                for (Method method : viewConfiguration.getProxyFactory().getCachedMethods()) {
                    if ((method.getName().equals("hashCode") && method.getParameterTypes().length == 0) ||
                            method.getName().equals("equals") && method.getParameterTypes().length == 1 &&
                                    method.getParameterTypes()[0] == Object.class) {
                        viewConfiguration.addClientInterceptor(method, StatefulIdentityInterceptorFactory.INSTANCE, InterceptorOrder.Client.EJB_EQUALS_HASHCODE);
                    }
                }
            }
        });

        view.getConfigurators().add(new ViewConfigurator() {
            @Override
            public void configure(DeploymentPhaseContext context, ComponentConfiguration componentConfiguration, ViewDescription description, ViewConfiguration configuration) throws DeploymentUnitProcessingException {
                // add the instance associating interceptor to the *start of the invocation interceptor chain*
                configuration.addClientInterceptor( StatefulComponentIdInterceptor.Factory.INSTANCE, InterceptorOrder.Client.ASSOCIATING_INTERCEPTOR);
                configuration.addViewInterceptor( StatefulComponentInstanceInterceptor.Factory.INSTANCE, InterceptorOrder.View.ASSOCIATING_INTERCEPTOR);
            }
        });

    }

    private void addRemoveMethodInterceptor(final ViewDescription view) {
        view.getConfigurators().add(new ViewConfigurator() {
            @Override
            public void configure(DeploymentPhaseContext context, ComponentConfiguration componentConfiguration, ViewDescription description, ViewConfiguration configuration) throws DeploymentUnitProcessingException {
                final StatefulComponentDescription statefulComponentDescription = (StatefulComponentDescription) componentConfiguration.getComponentDescription();
                final Set<StatefulRemoveMethod> removeMethods = statefulComponentDescription.getRemoveMethods();
                if (removeMethods.isEmpty()) {
                    return;
                }
                for (final Method viewMethod : configuration.getProxyFactory().getCachedMethods()) {
                    final MethodIdentifier viewMethodIdentifier = MethodIdentifier.getIdentifierForMethod(viewMethod);
                    for (final StatefulRemoveMethod removeMethod : removeMethods) {
                        if (removeMethod.methodIdentifier.equals(viewMethodIdentifier)) {
                            configuration.addViewInterceptor(viewMethod, new ImmediateInterceptorFactory(new StatefulRemoveInterceptor(removeMethod.retainIfException)), InterceptorOrder.View.SESSION_REMOVE_INTERCEPTOR);
                            break;
                        }
                    }
                }
            }
        });
    }

    public void addInitMethod(final Method method, final  String createMethod) {
        initMethods.put(method, createMethod);
    }

    public Map<Method, String> getInitMethods() {
        return Collections.unmodifiableMap(initMethods);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14885.java