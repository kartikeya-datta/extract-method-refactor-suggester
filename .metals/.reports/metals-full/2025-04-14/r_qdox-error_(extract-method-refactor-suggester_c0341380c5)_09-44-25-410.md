error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8343.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8343.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8343.java
text:
```scala
s@@erviceBuilder.addDependency(ControlPointService.serviceName(deploymentUnit.getParent() == null ? deploymentUnit.getName() : deploymentUnit.getParent().getName(), EJBComponentSuspendDeploymentUnitProcessor.ENTRY_POINT_NAME + deploymentUnit.getName() + "." + componentDescription.getComponentName()), ControlPoint.class, controlPointInjectedValue);

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

package org.jboss.as.ejb3.deployment.processors;

import java.util.Collection;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.BindingConfiguration;
import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentConfigurator;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.DependencyConfigurator;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ejb3.logging.EjbLogger;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.component.EJBViewDescription;
import org.jboss.as.ejb3.component.MethodIntf;
import org.jboss.as.ejb3.component.entity.EntityBeanComponentDescription;
import org.jboss.as.ejb3.component.session.SessionBeanComponentDescription;
import org.jboss.as.ejb3.remote.RemoteViewInjectionSource;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.EjbDeploymentMarker;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.value.InjectedValue;
import org.jboss.msc.value.Values;
import org.wildfly.extension.requestcontroller.ControlPoint;
import org.wildfly.extension.requestcontroller.ControlPointService;
import org.wildfly.extension.requestcontroller.RequestControllerActivationMarker;
import org.wildfly.extension.requestcontroller.RunResult;

/**
 * Sets up JNDI bindings for each of the views exposed by a {@link SessionBeanComponentDescription session bean}
 *
 * @author Jaikiran Pai
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
public class EjbJndiBindingsDeploymentUnitProcessor implements DeploymentUnitProcessor {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(EjbJndiBindingsDeploymentUnitProcessor.class);

    private final boolean appclient;

    public EjbJndiBindingsDeploymentUnitProcessor(final boolean appclient) {
        this.appclient = appclient;
    }

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        // Only process EJB deployments
        if (!EjbDeploymentMarker.isEjbDeployment(deploymentUnit)) {
            return;
        }

        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        final Collection<ComponentDescription> componentDescriptions = eeModuleDescription.getComponentDescriptions();
        if (componentDescriptions != null) {
            for (ComponentDescription componentDescription : componentDescriptions) {
                // process only EJB session beans
                if (componentDescription instanceof SessionBeanComponentDescription || componentDescription instanceof EntityBeanComponentDescription) {
                    this.setupJNDIBindings((EJBComponentDescription) componentDescription, deploymentUnit);
                }
            }
        }
        if (appclient) {
            for (final ComponentDescription component : deploymentUnit.getAttachmentList(Attachments.ADDITIONAL_RESOLVABLE_COMPONENTS)) {
                this.setupJNDIBindings((EJBComponentDescription) component, deploymentUnit);
            }
        }
    }


    /**
     * Sets up jndi bindings for each of the views exposed by the passed <code>sessionBean</code>
     *
     * @param sessionBean    The session bean
     * @param deploymentUnit The deployment unit containing the session bean
     */
    private void setupJNDIBindings(EJBComponentDescription sessionBean, DeploymentUnit deploymentUnit) throws DeploymentUnitProcessingException {
        final Collection<ViewDescription> views = sessionBean.getViews();
        if (views == null || views.isEmpty()) {
            EjbLogger.ROOT_LOGGER.noJNDIBindingsForSessionBean(sessionBean.getEJBName());
            return;
        }

        // In case of EJB bindings, appname == .ear file name/application-name set in the application.xml (if it's an .ear deployment)
        // NOTE: Do NOT use the app name from the EEModuleDescription.getApplicationName() because the Java EE spec has a different and conflicting meaning for app name
        // (where app name == module name in the absence of a .ear). Use EEModuleDescription.getEarApplicationName() instead
        final String applicationName = sessionBean.getModuleDescription().getEarApplicationName();
        final String globalJNDIBaseName = "java:global/" + (applicationName != null ? applicationName + "/" : "") + sessionBean.getModuleName() + "/" + sessionBean.getEJBName();
        final String appJNDIBaseName = "java:app/" + sessionBean.getModuleName() + "/" + sessionBean.getEJBName();
        final String moduleJNDIBaseName = "java:module/" + sessionBean.getEJBName();
        final String remoteExportedJNDIBaseName = "java:jboss/exported/" + (applicationName != null ? applicationName + "/" : "") + sessionBean.getModuleName() + "/" + sessionBean.getEJBName();

        // the base ServiceName which will be used to create the ServiceName(s) for each of the view bindings
        final StringBuilder jndiBindingsLogMessage = new StringBuilder();
        jndiBindingsLogMessage.append("JNDI bindings for session bean named " + sessionBean.getEJBName() + " in deployment unit " + deploymentUnit + " are as follows:\n\n");

        // now create the bindings for each view under the java:global, java:app and java:module namespaces
        EJBViewDescription ejbViewDescription = null;
        for (ViewDescription viewDescription : views) {
            ejbViewDescription = (EJBViewDescription) viewDescription;
            if (appclient && ejbViewDescription.getMethodIntf() != MethodIntf.REMOTE && ejbViewDescription.getMethodIntf() != MethodIntf.HOME) {
                continue;
            }
            if (!ejbViewDescription.hasJNDIBindings()) continue;

            final String viewClassName = ejbViewDescription.getViewClassName();

            // java:global bindings
            final String globalJNDIName = globalJNDIBaseName + "!" + viewClassName;
            registerBinding(sessionBean, viewDescription, globalJNDIName);
            logBinding(jndiBindingsLogMessage, globalJNDIName);

            // java:app bindings
            final String appJNDIName = appJNDIBaseName + "!" + viewClassName;
            registerBinding(sessionBean, viewDescription, appJNDIName);
            logBinding(jndiBindingsLogMessage, appJNDIName);

            // java:module bindings
            final String moduleJNDIName = moduleJNDIBaseName + "!" + viewClassName;
            registerBinding(sessionBean, viewDescription, moduleJNDIName);
            logBinding(jndiBindingsLogMessage, moduleJNDIName);

            // If it a remote or (remote) home view then bind the java:jboss/exported jndi names for the view
            if(ejbViewDescription.getMethodIntf() == MethodIntf.REMOTE || ejbViewDescription.getMethodIntf() == MethodIntf.HOME) {
                final String remoteJNDIName = remoteExportedJNDIBaseName + "!" + viewClassName;
                if(RequestControllerActivationMarker.isRequestControllerEnabled(deploymentUnit)) {
                    registerControlPointBinding(sessionBean, viewDescription, remoteJNDIName, deploymentUnit);
                } else {
                    registerBinding(sessionBean, viewDescription, remoteJNDIName);
                }
                logBinding(jndiBindingsLogMessage, remoteJNDIName);
            }
        }

        // EJB3.1 spec, section 4.4.1 Global JNDI Access states:
        // In addition to the previous requirements, if the bean exposes only one of the
        // applicable client interfaces(or alternatively has only a no-interface view), the container
        // registers an entry for that view with the following syntax :
        //
        // java:global[/<app-name>]/<module-name>/<bean-name>
        //
        // Note that this also applies to java:app and java:module bindings
        // as can be seen by the examples in 4.4.2.1
        if (views.size() == 1) {
            final EJBViewDescription viewDescription = (EJBViewDescription) views.iterator().next();
            if (ejbViewDescription.hasJNDIBindings()) {

                // java:global binding
                registerBinding(sessionBean, viewDescription, globalJNDIBaseName);
                logBinding(jndiBindingsLogMessage, globalJNDIBaseName);

                // java:app binding
                registerBinding(sessionBean, viewDescription, appJNDIBaseName);
                logBinding(jndiBindingsLogMessage, appJNDIBaseName);

                // java:module binding
                registerBinding(sessionBean, viewDescription, moduleJNDIBaseName);
                logBinding(jndiBindingsLogMessage, moduleJNDIBaseName);
            }
        }

        // log the jndi bindings
        logger.info(jndiBindingsLogMessage);
    }

    private void registerBinding(final EJBComponentDescription componentDescription, final ViewDescription viewDescription, final String jndiName) {
        if (appclient) {
            registerRemoteBinding(componentDescription, viewDescription, jndiName);
        } else {
            viewDescription.getBindingNames().add(jndiName);
        }
    }

    private void registerRemoteBinding(final EJBComponentDescription componentDescription, final ViewDescription viewDescription, final String jndiName) {
        final EEModuleDescription moduleDescription = componentDescription.getModuleDescription();
        final InjectedValue<ClassLoader> viewClassLoader = new InjectedValue<ClassLoader>();
        moduleDescription.getBindingConfigurations().add(new BindingConfiguration(jndiName, new RemoteViewInjectionSource(null, moduleDescription.getEarApplicationName(), moduleDescription.getModuleName(), moduleDescription.getDistinctName(), componentDescription.getComponentName(), viewDescription.getViewClassName(), componentDescription.isStateful(), viewClassLoader)));
        componentDescription.getConfigurators().add(new ComponentConfigurator() {
            public void configure(DeploymentPhaseContext context, ComponentDescription description, ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
                viewClassLoader.setValue(Values.immediateValue(configuration.getModuleClassLoader()));
            }
        });
    }
    private void registerControlPointBinding(final EJBComponentDescription componentDescription, final ViewDescription viewDescription, final String jndiName, final DeploymentUnit deploymentUnit) {
        final EEModuleDescription moduleDescription = componentDescription.getModuleDescription();
        final InjectedValue<ClassLoader> viewClassLoader = new InjectedValue<ClassLoader>();
        final InjectedValue<ControlPoint> controlPointInjectedValue = new InjectedValue<>();
        final RemoteViewInjectionSource delegate = new RemoteViewInjectionSource(null, moduleDescription.getEarApplicationName(), moduleDescription.getModuleName(), moduleDescription.getDistinctName(), componentDescription.getComponentName(), viewDescription.getViewClassName(), componentDescription.isStateful(), viewClassLoader);

        componentDescription.getConfigurators().add(new ComponentConfigurator() {
            public void configure(DeploymentPhaseContext context, ComponentDescription description, final ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
                viewClassLoader.setValue(Values.immediateValue(configuration.getModuleClassLoader()));
                configuration.getCreateDependencies().add(new DependencyConfigurator<Service<Component>>() {
                    @Override
                    public void configureDependency(ServiceBuilder<?> serviceBuilder, Service<Component> service) throws DeploymentUnitProcessingException {
                        serviceBuilder.addDependency(ControlPointService.serviceName(deploymentUnit.getParent() == null ? deploymentUnit.getName() : deploymentUnit.getParent().getName(), RemoteEJBComponentSuspendDeploymentUnitProcessor.ENTRY_POINT_NAME + deploymentUnit.getName() + "." + componentDescription.getComponentName()), ControlPoint.class, controlPointInjectedValue);
                    }
                });
            }
        });
        //we need to wrap the injection source to allow graceful shutdown to function, although this is not ideal
        //as it will also reject local lookups as well, although in general local code should never be looking up the
        //exported bindings
        //the other option would be to reject it at the remote naming service level, however then we loose the per-deployment granularity
        final InjectionSource is = new InjectionSource() {

            @Override
            public void getResourceValue(ResolutionContext resolutionContext, ServiceBuilder<?> serviceBuilder, DeploymentPhaseContext phaseContext, Injector<ManagedReferenceFactory> injector) throws DeploymentUnitProcessingException {
                final InjectedValue<ManagedReferenceFactory> delegateInjection = new InjectedValue<>();
                delegate.getResourceValue(resolutionContext, serviceBuilder, phaseContext, delegateInjection);
                injector.inject(new ManagedReferenceFactory() {
                    @Override
                    public ManagedReference getReference() {
                        ControlPoint cp = controlPointInjectedValue.getValue();
                        try {
                            RunResult res = cp.beginRequest();
                            if(res != RunResult.RUN) {
                                throw EjbLogger.ROOT_LOGGER.containerSuspended();
                            }
                            try {
                                return delegateInjection.getValue().getReference();
                            } finally {
                                cp.requestComplete();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        };
        moduleDescription.getBindingConfigurations().add(new BindingConfiguration(jndiName, is));
    }

    private void logBinding(final StringBuilder jndiBindingsLogMessage, final String jndiName) {
        jndiBindingsLogMessage.append("\t");
        jndiBindingsLogMessage.append(jndiName);
        jndiBindingsLogMessage.append("\n");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8343.java