error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11267.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11267.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11267.java
text:
```scala
b@@uilder.addDependency(DependencyType.OPTIONAL, getEJBViewMethodSecurityAttributesServiceName(unit, endpoint),

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.as.webservices.service;

import static org.jboss.as.webservices.WSLogger.ROOT_LOGGER;

import java.util.List;

import javax.management.JMException;
import javax.management.MBeanServer;

import org.jboss.as.ejb3.security.service.EJBViewMethodSecurityAttributesService;
import org.jboss.as.security.plugins.SecurityDomainContext;
import org.jboss.as.security.service.SecurityDomainService;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.webservices.metadata.model.EJBEndpoint;
import org.jboss.as.webservices.security.EJBMethodSecurityAttributesAdaptor;
import org.jboss.as.webservices.security.SecurityDomainContextAdaptor;
import org.jboss.as.webservices.util.ASHelper;
import org.jboss.as.webservices.util.WSServices;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceBuilder.DependencyType;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.security.SecurityConstants;
import org.jboss.security.SecurityUtil;
import org.jboss.ws.api.monitoring.RecordProcessor;
import org.jboss.ws.common.ObjectNameFactory;
import org.jboss.ws.common.management.ManagedEndpoint;
import org.jboss.ws.common.monitoring.ManagedRecordProcessor;
import org.jboss.wsf.spi.deployment.Endpoint;
import org.jboss.wsf.spi.deployment.EndpointType;
import org.jboss.wsf.spi.security.EJBMethodSecurityAttributeProvider;

/**
 * WS endpoint service; this is meant for setting the lazy deployment time info into the Endpoint (stuff coming from
 * dependencies upon other AS services that are started during the deployment)
 *
 * @author alessio.soldano@jboss.com
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 * @author <a href="mailto:ema@redhat.com">Jim Ma</a>
 */
public final class EndpointService implements Service<Endpoint> {

    private static final ServiceName MBEAN_SERVER_NAME = ServiceName.JBOSS.append("mbean", "server");
    private final Endpoint endpoint;
    private final ServiceName name;
    private final InjectedValue<SecurityDomainContext> securityDomainContextValue = new InjectedValue<SecurityDomainContext>();
    private final InjectedValue<MBeanServer> mBeanServerValue = new InjectedValue<MBeanServer>();
    private final InjectedValue<EJBViewMethodSecurityAttributesService> ejbMethodSecurityAttributeServiceValue = new InjectedValue<EJBViewMethodSecurityAttributesService>();

    private EndpointService(final Endpoint endpoint, final ServiceName name) {
        this.endpoint = endpoint;
        this.name = name;
    }

    @Override
    public Endpoint getValue() {
        return endpoint;
    }

    public static ServiceName getServiceName(final DeploymentUnit unit, final String endpointName) {
        if (unit.getParent() != null) {
            return WSServices.ENDPOINT_SERVICE.append(unit.getParent().getName()).append(unit.getName()).append(endpointName);
        } else {
            return WSServices.ENDPOINT_SERVICE.append(unit.getName()).append(endpointName);
        }
    }

    @Override
    public void start(final StartContext context) throws StartException {
        ROOT_LOGGER.starting(name);
        endpoint.setSecurityDomainContext(new SecurityDomainContextAdaptor(securityDomainContextValue.getValue()));
        if (EndpointType.JAXWS_EJB3.equals(endpoint.getType())) {
            final EJBViewMethodSecurityAttributesService ejbMethodSecurityAttributeService = ejbMethodSecurityAttributeServiceValue.getValue();
            endpoint.addAttachment(EJBMethodSecurityAttributeProvider.class, new EJBMethodSecurityAttributesAdaptor(ejbMethodSecurityAttributeService));
        }
        final List<RecordProcessor> processors = endpoint.getRecordProcessors();
        for (final RecordProcessor processor : processors) {
            registerRecordProcessor(processor, endpoint);
        }
        registerEndpoint(endpoint);
        endpoint.getLifecycleHandler().start(endpoint);
    }

    @Override
    public void stop(final StopContext context) {
        ROOT_LOGGER.stopping(name);
        endpoint.getLifecycleHandler().stop(endpoint);
        endpoint.setSecurityDomainContext(null);
        unregisterEndpoint(endpoint);
        final List<RecordProcessor> processors = endpoint.getRecordProcessors();
        for (final RecordProcessor processor : processors) {
            unregisterRecordProcessor(processor, endpoint);
        }
    }

    private void registerEndpoint(final Endpoint ep) {
        MBeanServer mbeanServer = mBeanServerValue.getValue();
        if (mbeanServer != null) {
            try {
                ManagedEndpoint jmxEndpoint = new ManagedEndpoint(endpoint, mbeanServer);
                mbeanServer.registerMBean(jmxEndpoint, endpoint.getName());
            } catch (final JMException ex) {
                ROOT_LOGGER.trace("Cannot register endpoint in JMX server", ex);
                ROOT_LOGGER.cannotRegisterEndpoint(endpoint.getShortName());
            }
        } else {
            ROOT_LOGGER.mBeanServerNotAvailable(endpoint.getShortName());
        }
    }

    private void unregisterEndpoint(final Endpoint ep) {
        MBeanServer mbeanServer = mBeanServerValue.getValue();
        if (mbeanServer != null) {
            try {
                mbeanServer.unregisterMBean(endpoint.getName());
            } catch (final JMException ex) {
                ROOT_LOGGER.trace("Cannot unregister endpoint from JMX server", ex);
                ROOT_LOGGER.cannotUnregisterEndpoint(endpoint.getShortName());
            }
        } else {
            ROOT_LOGGER.mBeanServerNotAvailable(endpoint.getShortName());
        }
    }

    private void registerRecordProcessor(final RecordProcessor processor, final Endpoint ep) {
        MBeanServer mbeanServer = mBeanServerValue.getValue();
        if (mbeanServer != null) {
            try {
                mbeanServer.registerMBean(processor,
                        ObjectNameFactory.create(ep.getName() + ",recordProcessor=" + processor.getName()));
            } catch (final JMException ex) {
                ROOT_LOGGER.trace("Cannot register endpoint in JMX server, trying with the default ManagedRecordProcessor", ex);
                try {
                    mbeanServer.registerMBean(new ManagedRecordProcessor(processor),
                            ObjectNameFactory.create(ep.getName() + ",recordProcessor=" + processor.getName()));
                } catch (final JMException e) {
                    ROOT_LOGGER.cannotRegisterRecordProcessor();
                }
            }
        } else {
            ROOT_LOGGER.mBeanServerNotAvailable(processor);
        }
    }

    private void unregisterRecordProcessor(final RecordProcessor processor, final Endpoint ep) {
        MBeanServer mbeanServer = mBeanServerValue.getValue();
        if (mbeanServer != null) {
            try {
                mbeanServer.unregisterMBean(ObjectNameFactory.create(ep.getName() + ",recordProcessor=" + processor.getName()));
            } catch (final JMException e) {
                ROOT_LOGGER.cannotUnregisterRecordProcessor();
            }
        } else {
            ROOT_LOGGER.mBeanServerNotAvailable(processor);
        }
    }

    public Injector<SecurityDomainContext> getSecurityDomainContextInjector() {
        return securityDomainContextValue;
    }

    public Injector<MBeanServer> getMBeanServerInjector() {
        return mBeanServerValue;
    }

    public Injector<EJBViewMethodSecurityAttributesService> getEJBMethodSecurityAttributeServiceInjector() {
        return ejbMethodSecurityAttributeServiceValue;
    }

    public static void install(final ServiceTarget serviceTarget, final Endpoint endpoint, final DeploymentUnit unit) {
        final ServiceName serviceName = getServiceName(unit, endpoint.getShortName());
        final String propContext = endpoint.getName().getKeyProperty(Endpoint.SEPID_PROPERTY_CONTEXT);
        final String propEndpoint = endpoint.getName().getKeyProperty(Endpoint.SEPID_PROPERTY_ENDPOINT);
        final StringBuilder context = new StringBuilder(Endpoint.SEPID_PROPERTY_CONTEXT).append("=").append(propContext);
        final EndpointService service = new EndpointService(endpoint, serviceName);
        final ServiceBuilder<Endpoint> builder = serviceTarget.addService(serviceName, service);
        final ServiceName alias = WSServices.ENDPOINT_SERVICE.append(context.toString()).append(propEndpoint);
        builder.addAliases(alias);
        builder.addDependency(DependencyType.REQUIRED,
                SecurityDomainService.SERVICE_NAME.append(getDeploymentSecurityDomainName(endpoint)),
                SecurityDomainContext.class, service.getSecurityDomainContextInjector());
        builder.addDependency(DependencyType.REQUIRED, WSServices.CONFIG_SERVICE);
        builder.addDependency(DependencyType.OPTIONAL, MBEAN_SERVER_NAME, MBeanServer.class, service.getMBeanServerInjector());
        if (EndpointType.JAXWS_EJB3.equals(endpoint.getType())) {
            builder.addDependency(DependencyType.REQUIRED, getEJBViewMethodSecurityAttributesServiceName(unit, endpoint),
                    EJBViewMethodSecurityAttributesService.class, service.getEJBMethodSecurityAttributeServiceInjector());
        }
        builder.setInitialMode(Mode.ACTIVE);
        builder.install();
    }

    public static void uninstall(final Endpoint endpoint, final DeploymentUnit unit) {
        final ServiceName serviceName = getServiceName(unit, endpoint.getShortName());
        final ServiceController<?> endpointService = WSServices.getContainerRegistry().getService(serviceName);
        if (endpointService != null) {
            endpointService.setMode(Mode.REMOVE);
        }
    }

    private static String getDeploymentSecurityDomainName(final Endpoint ep) {
        JBossWebMetaData metadata = ep.getService().getDeployment().getAttachment(JBossWebMetaData.class);
        String metaDataSecurityDomain = metadata != null ? metadata.getSecurityDomain() : null;
        return metaDataSecurityDomain == null ? SecurityConstants.DEFAULT_APPLICATION_POLICY : SecurityUtil
                .unprefixSecurityDomain(metaDataSecurityDomain.trim());
    }

    private static ServiceName getEJBViewMethodSecurityAttributesServiceName(final DeploymentUnit unit, final Endpoint endpoint) {
        for (EJBEndpoint ep : ASHelper.getJaxwsEjbs(unit)) {
            if (ep.getClassName().equals(endpoint.getTargetBeanName())) {
                return ep.getEJBViewMethodSecurityAttributesService();
            }
        }
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11267.java