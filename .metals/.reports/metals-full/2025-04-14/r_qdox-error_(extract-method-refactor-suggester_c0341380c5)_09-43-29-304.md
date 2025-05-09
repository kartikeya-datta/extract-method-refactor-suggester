error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14917.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14917.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14917.java
text:
```scala
t@@hrow IIOPLogger.ROOT_LOGGER.noSecurityDomainSpecified();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
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

package org.jboss.as.iiop.openjdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PersistentResourceDefinition;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.iiop.openjdk.csiv2.CSIV2IORToSocketInfo;
import org.jboss.as.iiop.openjdk.deployment.IIOPDependencyProcessor;
import org.jboss.as.iiop.openjdk.deployment.IIOPMarkerProcessor;
import org.jboss.as.iiop.openjdk.logging.IIOPLogger;
import org.jboss.as.iiop.openjdk.naming.jndi.JBossCNCtxFactory;
import org.jboss.as.iiop.openjdk.rmi.DelegatingStubFactoryFactory;
import org.jboss.as.iiop.openjdk.security.SocketFactory;
import org.jboss.as.iiop.openjdk.service.CorbaNamingService;
import org.jboss.as.iiop.openjdk.service.CorbaORBService;
import org.jboss.as.iiop.openjdk.service.CorbaPOAService;
import org.jboss.as.iiop.openjdk.service.IORSecConfigMetaDataService;
import org.jboss.as.naming.InitialContext;
import org.jboss.as.network.SocketBinding;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.metadata.ejb.jboss.IORASContextMetaData;
import org.jboss.metadata.ejb.jboss.IORSASContextMetaData;
import org.jboss.metadata.ejb.jboss.IORSecurityConfigMetaData;
import org.jboss.metadata.ejb.jboss.IORTransportConfigMetaData;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.wildfly.security.manager.WildFlySecurityManager;

import com.sun.corba.se.impl.orbutil.ORBConstants;

/**
 * <p>
 * This class implements a {@code ModelAddOperationHandler} that installs the IIOP subsystem services:
 * <ul>
 * <li>{@code CorbaORBService}: responsible for configuring and starting the CORBA {@code ORB}.</li>
 * <li>{@code CorbaPOAService}: responsible for creating and activating CORBA {@code POA}s.</li>
 * <li>{@code CorbaNamingService}: responsible for creating and starting the CORBA naming service.</li>
 * </ul>
 * After the {@code ORB} is created, we create and activate the "RootPOA" and then use this {@code POA} to create the
 * {@code POA}s required by the other services.
 * </p>
 *
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 * @author <a href="mailto:tadamski@redhat.com">Tomasz Adamski</a>
 */
public class IIOPSubsystemAdd extends AbstractAddStepHandler {

    static final IIOPSubsystemAdd INSTANCE = new IIOPSubsystemAdd();

    private static final ServiceName SECURITY_DOMAIN_SERVICE_NAME = ServiceName.JBOSS.append("security").append(
            "security-domain");

    @Override
    protected void performRuntime(final OperationContext context, final ModelNode operation, final ModelNode model,
                                  final ServiceVerificationHandler verificationHandler,
                                  final List<ServiceController<?>> newControllers) throws OperationFailedException {

        // This needs to run after all child resources so that they can detect a fresh state
        context.addStep(new OperationStepHandler() {
            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                final Resource resource = context.readResource(PathAddress.EMPTY_ADDRESS);
                ModelNode node = Resource.Tools.readModel(resource);
                launchServices(context, node, verificationHandler, newControllers);
                // Rollback handled by the parent step
                context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
            }
        }, OperationContext.Stage.RUNTIME);
    }

    protected void launchServices(final OperationContext context, final ModelNode model, final ServiceVerificationHandler verificationHandler,
                                  final List<ServiceController<?>> newControllers) throws OperationFailedException {


        IIOPLogger.ROOT_LOGGER.activatingSubsystem();

        // set the ORBUseDynamicStub system property.
        WildFlySecurityManager.setPropertyPrivileged("org.jboss.com.sun.CORBA.ORBUseDynamicStub", "true");
        // we set the same stub factory to both the static and dynamic stub factory. As there is no way to dynamically change
        // the userDynamicStubs's property at runtime it is possible for the ORB class's <clinit> method to be
        // called before this property is set.
        // TODO: investigate a better way to handle this
        com.sun.corba.se.spi.orb.ORB.getPresentationManager().setStubFactoryFactory(true,
                new DelegatingStubFactoryFactory());
        com.sun.corba.se.spi.orb.ORB.getPresentationManager().setStubFactoryFactory(false,
                new DelegatingStubFactoryFactory());

        // setup naming.
        InitialContext.addUrlContextFactory("corbaloc", JBossCNCtxFactory.INSTANCE);
        InitialContext.addUrlContextFactory("corbaname", JBossCNCtxFactory.INSTANCE);
        InitialContext.addUrlContextFactory("IOR", JBossCNCtxFactory.INSTANCE);
        InitialContext.addUrlContextFactory("iiopname", JBossCNCtxFactory.INSTANCE);
        InitialContext.addUrlContextFactory("iiop", JBossCNCtxFactory.INSTANCE);

        context.addStep(new AbstractDeploymentChainStep() {
            public void execute(DeploymentProcessorTarget processorTarget) {
                processorTarget.addDeploymentProcessor(IIOPExtension.SUBSYSTEM_NAME, Phase.DEPENDENCIES,
                        Phase.DEPENDENCIES_JDKORB, new IIOPDependencyProcessor());
                processorTarget.addDeploymentProcessor(IIOPExtension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_JDKORB,
                        new IIOPMarkerProcessor());
            }
        }, OperationContext.Stage.RUNTIME);

        // get the configured ORB properties.
        Properties props = this.getConfigurationProperties(context, model);

        // setup the ORB initializers using the configured properties.
        this.setupInitializers(props);

        // setup the SSL socket factories, if necessary.
        this.setupSSLFactories(props);

        // create the service that initializes and starts the CORBA ORB.


        CorbaORBService orbService = new CorbaORBService(props);
        final ServiceBuilder<ORB> builder = context.getServiceTarget().addService(CorbaORBService.SERVICE_NAME, orbService);
        org.jboss.as.server.Services.addServerExecutorDependency(builder, orbService.getExecutorInjector(), false);

        // if a security domain has been specified, add a dependency to the domain service.
        String securityDomain = props.getProperty(Constants.SECURITY_SECURITY_DOMAIN);
        if (securityDomain != null && !securityDomain.isEmpty())
            builder.addDependency(SECURITY_DOMAIN_SERVICE_NAME.append(securityDomain));

        // inject the socket bindings that specify IIOP and IIOP/SSL ports.
        String socketBinding = props.getProperty(Constants.ORB_SOCKET_BINDING);
        builder.addDependency(SocketBinding.JBOSS_BINDING_NAME.append(socketBinding), SocketBinding.class,
                orbService.getIIOPSocketBindingInjector());
        String sslSocketBinding = props.getProperty(Constants.ORB_SSL_SOCKET_BINDING);
        builder.addDependency(SocketBinding.JBOSS_BINDING_NAME.append(sslSocketBinding), SocketBinding.class,
                orbService.getIIOPSSLSocketBindingInjector());
        builder.addListener(verificationHandler);
        // set the initial mode and install the service.
        newControllers.add(builder.setInitialMode(ServiceController.Mode.ACTIVE).install());

        // create the service the initializes the Root POA.
        CorbaPOAService rootPOAService = new CorbaPOAService("RootPOA", "poa");
        newControllers.add(context.getServiceTarget().addService(CorbaPOAService.ROOT_SERVICE_NAME, rootPOAService)
                .addDependency(CorbaORBService.SERVICE_NAME, ORB.class, rootPOAService.getORBInjector())
                .addListener(verificationHandler).setInitialMode(ServiceController.Mode.ACTIVE).install());

        // create the service the initializes the interface repository POA.
        final CorbaPOAService irPOAService = new CorbaPOAService("IRPOA", "irpoa", IdAssignmentPolicyValue.USER_ID, null, null,
                LifespanPolicyValue.PERSISTENT, null, null, null);
        newControllers.add(context.getServiceTarget()
                .addService(CorbaPOAService.INTERFACE_REPOSITORY_SERVICE_NAME, irPOAService)
                .addDependency(CorbaPOAService.ROOT_SERVICE_NAME, POA.class, irPOAService.getParentPOAInjector())
                .addListener(verificationHandler).setInitialMode(ServiceController.Mode.ACTIVE).install());

        // create the service that initializes the naming service POA.
        final CorbaPOAService namingPOAService = new CorbaPOAService("Naming", null, IdAssignmentPolicyValue.USER_ID, null,
                null, LifespanPolicyValue.PERSISTENT, null, null, null);
        newControllers.add(context.getServiceTarget()
                .addService(CorbaPOAService.SERVICE_NAME.append("namingpoa"), namingPOAService)
                .addDependency(CorbaPOAService.ROOT_SERVICE_NAME, POA.class, namingPOAService.getParentPOAInjector())
                .addListener(verificationHandler).setInitialMode(ServiceController.Mode.ACTIVE).install());

        // create the CORBA naming service.
        final String rootContext = props.getProperty(Constants.NAMING_ROOT_CONTEXT);
        final CorbaNamingService namingService = new CorbaNamingService(rootContext);
        newControllers.add(context
                .getServiceTarget()
                .addService(CorbaNamingService.SERVICE_NAME, namingService)
                .addDependency(CorbaORBService.SERVICE_NAME, ORB.class, namingService.getORBInjector())
                .addDependency(CorbaPOAService.ROOT_SERVICE_NAME, POA.class, namingService.getRootPOAInjector())
                .addDependency(CorbaPOAService.SERVICE_NAME.append("namingpoa"), POA.class,
                        namingService.getNamingPOAInjector()).addListener(verificationHandler)
                .setInitialMode(ServiceController.Mode.ACTIVE).install());

     // create the IOR security config metadata service.
        IORSecurityConfigMetaData securityConfigMetaData = null;
        if (model.hasDefined(Constants.IOR_SETTINGS)) {
            securityConfigMetaData = this.createIORSecurityConfigMetaData(context,
                    model.get(IORSettingsDefinition.INSTANCE.getPathElement().getKeyValuePair()));
        }
        newControllers.add(context.getServiceTarget().addService(IORSecConfigMetaDataService.SERVICE_NAME,
                new IORSecConfigMetaDataService(securityConfigMetaData))
                .addListener(verificationHandler)
                .setInitialMode(ServiceController.Mode.ACTIVE).install());


        configureClientSecurity(props);

    }

    /**
     * <p>
     * Obtains the subsystem configuration properties from the specified {@code ModelNode}, using default values for undefined
     * properties. If the property has a IIOP equivalent, it is translated into its IIOP counterpart before being added to
     * the returned {@code Properties} object.
     * </p>
     *
     * @param model the {@code ModelNode} that contains the subsystem configuration properties.
     * @return a {@code Properties} instance containing all configured subsystem properties.
     * @throws OperationFailedException if an error occurs while resolving the properties.
     */
    private Properties getConfigurationProperties(OperationContext context, ModelNode model) throws OperationFailedException {
        Properties props = new Properties();

        getResourceProperties(props, ORBDefinition.INSTANCE, context,
                model.get(ORBDefinition.INSTANCE.getPathElement().getKeyValuePair()));
        getResourceProperties(
                props,
                TCPDefinition.INSTANCE,
                context,
                model.get(ORBDefinition.INSTANCE.getPathElement().getKeyValuePair()).get(
                        TCPDefinition.INSTANCE.getPathElement().getKeyValuePair()));
        getResourceProperties(
                props,
                InitializersDefinition.INSTANCE,
                context,
                model.get(ORBDefinition.INSTANCE.getPathElement().getKeyValuePair()).get(
                        InitializersDefinition.INSTANCE.getPathElement().getKeyValuePair()));
        getResourceProperties(props, NamingDefinition.INSTANCE, context,
                model.get(NamingDefinition.INSTANCE.getPathElement().getKeyValuePair()));
        getResourceProperties(props, SecurityDefinition.INSTANCE, context,
                model.get(SecurityDefinition.INSTANCE.getPathElement().getKeyValuePair()));

        // check if the node contains a list of generic properties.
        ModelNode configNode = model.get(Constants.CONFIGURATION);
        if (configNode.hasDefined(Constants.PROPERTIES)) {
            for (Property property : configNode.get(Constants.PROPERTIES).get(Constants.PROPERTY)
                    .asPropertyList()) {
                String name = property.getName();
                String value = property.getValue().get(Constants.PROPERTY_VALUE).asString();
                props.setProperty(name, value);
            }
        }
        return props;
    }

    private void getResourceProperties(final Properties properties, PersistentResourceDefinition resource,
            OperationContext context, ModelNode model) throws OperationFailedException {
        for (AttributeDefinition attrDefinition : resource.getAttributes()) {
            ModelNode resolvedModelAttribute = attrDefinition.resolveModelAttribute(context, model);
            if (resolvedModelAttribute.isDefined()) {
                String name = attrDefinition.getName();
                String value = resolvedModelAttribute.asString();

                String openjdkProperty = PropertiesMap.PROPS_MAP.get(name);
                if (openjdkProperty != null) {
                    name = openjdkProperty;
                }
                properties.setProperty(name, value);
            }
        }
    }

    /**
     * <p>
     * Sets up the ORB initializers according to what has been configured in the subsystem.
     * </p>
     *
     * @param props the subsystem configuration properties.
     */
    private void setupInitializers(Properties props) {
        List<String> orbInitializers = new ArrayList<String>();

        // check which groups of initializers are to be installed.
        String installSecurity = (String) props.remove(Constants.ORB_INIT_SECURITY);
        if (installSecurity.equalsIgnoreCase(Constants.CLIENT)) {
            orbInitializers.addAll(Arrays.asList(IIOPInitializer.SECURITY_CLIENT.getInitializerClasses()));
        } else if (installSecurity.equalsIgnoreCase(Constants.IDENTITY)
 installSecurity.equalsIgnoreCase("on")) {
            orbInitializers.addAll(Arrays.asList(IIOPInitializer.SECURITY_IDENTITY.getInitializerClasses()));
        }

        String installTransaction = (String) props.remove(Constants.ORB_INIT_TRANSACTIONS);
        if (installTransaction.equalsIgnoreCase("on")) {
            orbInitializers.addAll(Arrays.asList(IIOPInitializer.TRANSACTIONS.getInitializerClasses()));
        } else if (installTransaction.equalsIgnoreCase("spec")) {
            orbInitializers.addAll(Arrays.asList(IIOPInitializer.SPEC_TRANSACTIONS.getInitializerClasses()));
        }

        // add the standard opendk initializer plus all configured initializers.
        for (String initializerClass : orbInitializers) {
            props.setProperty(Constants.ORB_INITIALIZER_PREFIX + initializerClass, "");
        }
    }

    /**
     * <p>
     * Sets up the SSL domain socket factories if SSL support has been enabled.
     * </p>
     *
     * @param props the subsystem configuration properties.
     * @throws OperationFailedException if the SSL setup has not been done correctly (SSL support has been turned on but no
     *         security domain has been specified).
     */
    private void setupSSLFactories(final Properties props) throws OperationFailedException {
        boolean supportSSL = "on".equalsIgnoreCase(props.getProperty(Constants.SECURITY_SUPPORT_SSL));

        if (supportSSL) {
            // if SSL is to be used, check if a security domain has been specified.
            String securityDomain = props.getProperty(Constants.SECURITY_SECURITY_DOMAIN);
            if (securityDomain == null || securityDomain.isEmpty())
                throw IIOPMessages.MESSAGES.noSecurityDomainSpecified();

            // add the domain socket factories.
            SocketFactory.setSecurityDomain(securityDomain);
            props.setProperty(ORBConstants.SOCKET_FACTORY_CLASS_PROPERTY, SocketFactory.class.getName());
        }
    }

    private IORSecurityConfigMetaData createIORSecurityConfigMetaData(final OperationContext context, final ModelNode node)
            throws OperationFailedException {

        final IORSecurityConfigMetaData securityConfigMetaData = new IORSecurityConfigMetaData();

        final IORTransportConfigMetaData transportConfigMetaData = IORTransportConfigDefinition.INSTANCE.getTransportConfigMetaData(
                context, node.get(IORTransportConfigDefinition.INSTANCE.getPathElement().getKeyValuePair()));
        if (transportConfigMetaData != null)
            securityConfigMetaData.setTransportConfig(transportConfigMetaData);

        final IORASContextMetaData asContextMetaData = IORASContextDefinition.INSTANCE.getIORASContextMetaData(
                context, node.get(IORASContextDefinition.INSTANCE.getPathElement().getKeyValuePair()));
        if (asContextMetaData != null)
            securityConfigMetaData.setAsContext(asContextMetaData);

        final IORSASContextMetaData sasContextMetaData = IORSASContextDefinition.INSTANCE.getIORSASContextMetaData(
                context, node.get(IORSASContextDefinition.INSTANCE.getPathElement().getKeyValuePair()));
        if (sasContextMetaData != null)
            securityConfigMetaData.setSasContext(sasContextMetaData);

        return securityConfigMetaData;
    }

    private void configureClientSecurity(final Properties props) {
        final SSLConfigValue clientRequiresSSL = SSLConfigValue
                .fromValue(props.getProperty(Constants.SECURITY_CLIENT_REQUIRES));
        CSIV2IORToSocketInfo.setClientTransportConfigMetaData(clientRequiresSSL);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14917.java