error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/154.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/154.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/154.java
text:
```scala
p@@rocessorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.STRUCTURE, Phase.STRUCTURE_REGISTER_JBOSS_ALL_XML_PARSER, new JBossAllXmlParserRegisteringProcessor<EjbJarMetaData>(EjbJarJBossAllParser.ROOT_ELEMENT, EjbJarJBossAllParser.ATTACHMENT_KEY, new EjbJarJBossAllParser()));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.ejb3.subsystem;

import com.arjuna.ats.arjuna.common.CoreEnvironmentBean;
import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;

import org.jboss.as.connector.util.ConnectorServices;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.core.security.ServerSecurityManager;
import org.jboss.as.ejb3.cache.impl.backing.clustering.ClusteredBackingCacheEntryStoreSourceService;
import org.jboss.as.ejb3.component.EJBUtilities;
import org.jboss.as.ejb3.deployment.DeploymentRepository;
import org.jboss.as.ejb3.deployment.processors.AnnotatedEJBComponentDescriptionDeploymentUnitProcessor;
import org.jboss.as.ejb3.deployment.processors.ApplicationExceptionAnnotationProcessor;
import org.jboss.as.ejb3.deployment.processors.BusinessViewAnnotationProcessor;
import org.jboss.as.ejb3.deployment.processors.DeploymentRepositoryProcessor;
import org.jboss.as.ejb3.deployment.processors.EJBClientDescriptorMetaDataProcessor;
import org.jboss.as.ejb3.deployment.processors.EJBDefaultPermissionsProcessor;
import org.jboss.as.ejb3.deployment.processors.EJBDefaultSecurityDomainProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbCleanUpProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbClientContextSetupProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbContextJndiBindingProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbDefaultDistinctNameProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbDependencyDeploymentUnitProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbJarJBossAllParser;
import org.jboss.as.ejb3.deployment.processors.EjbJarParsingDeploymentUnitProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbJndiBindingsDeploymentUnitProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbManagementDeploymentUnitProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbRefProcessor;
import org.jboss.as.ejb3.deployment.processors.EjbResourceInjectionAnnotationProcessor;
import org.jboss.as.ejb3.deployment.processors.IIOPJndiBindingProcessor;
import org.jboss.as.ejb3.deployment.processors.ImplicitLocalViewProcessor;
import org.jboss.as.ejb3.deployment.processors.PassivationAnnotationParsingProcessor;
import org.jboss.as.ejb3.deployment.processors.SessionBeanHomeProcessor;
import org.jboss.as.ejb3.deployment.processors.TimerServiceJndiBindingProcessor;
import org.jboss.as.ejb3.deployment.processors.annotation.EjbAnnotationProcessor;
import org.jboss.as.ejb3.deployment.processors.dd.AssemblyDescriptorProcessor;
import org.jboss.as.ejb3.deployment.processors.dd.ContainerInterceptorBindingsDDProcessor;
import org.jboss.as.ejb3.deployment.processors.dd.DeploymentDescriptorInterceptorBindingsProcessor;
import org.jboss.as.ejb3.deployment.processors.dd.DeploymentDescriptorMethodProcessor;
import org.jboss.as.ejb3.deployment.processors.dd.InterceptorClassDeploymentDescriptorProcessor;
import org.jboss.as.ejb3.deployment.processors.dd.SecurityRoleRefDDProcessor;
import org.jboss.as.ejb3.deployment.processors.dd.SessionBeanXmlDescriptorProcessor;
import org.jboss.as.ejb3.deployment.processors.entity.EntityBeanComponentDescriptionFactory;
import org.jboss.as.ejb3.deployment.processors.merging.ApplicationExceptionMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.CacheMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.ClusteredMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.ConcurrencyManagementMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.DeclareRolesMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.DeliveryActiveMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.EjbConcurrencyMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.EjbDependsOnMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.EntityBeanPoolMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.HomeViewMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.InitMethodMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.MessageDrivenBeanPoolMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.MethodPermissionsMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.MissingMethodPermissionsDenyAccessMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.RemoveMethodMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.ResourceAdaptorMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.RunAsMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.SecurityDomainMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.SecurityRolesMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.SessionBeanMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.SessionSynchronizationMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.StartupMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.StatefulTimeoutMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.StatelessSessionBeanPoolMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.TransactionAttributeMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.merging.TransactionManagementMergingProcessor;
import org.jboss.as.ejb3.deployment.processors.security.JaccEjbDeploymentProcessor;
import org.jboss.as.ejb3.iiop.POARegistry;
import org.jboss.as.ejb3.iiop.RemoteObjectSubstitutionService;
import org.jboss.as.ejb3.iiop.stub.DynamicStubFactoryFactory;
import org.jboss.as.ejb3.remote.DefaultEjbClientContextService;
import org.jboss.as.ejb3.remote.EJBRemoteConnectorService;
import org.jboss.as.ejb3.remote.EJBTransactionRecoveryService;
import org.jboss.as.ejb3.remote.LocalEjbReceiver;
import org.jboss.as.ejb3.remote.RegistryCollector;
import org.jboss.as.ejb3.remote.RegistryCollectorService;
import org.jboss.as.ejb3.remote.TCCLEJBClientContextSelectorService;
import org.jboss.as.ejb3.util.ServiceLookupValue;
import org.jboss.as.jacorb.rmi.DelegatingStubFactoryFactory;
import org.jboss.as.jacorb.service.CorbaPOAService;
import org.jboss.as.naming.InitialContext;
import org.jboss.as.network.ClientMapping;
import org.jboss.as.remoting.RemotingServices;
import org.jboss.as.security.service.SimpleSecurityManagerService;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.server.deployment.jbossallxml.JBossAllXmlParserRegisteringProcessor;
import org.jboss.as.txn.service.ArjunaRecoveryManagerService;
import org.jboss.as.txn.service.TxnServices;
import org.jboss.as.txn.service.UserTransactionAccessControlService;
import org.jboss.com.sun.corba.se.impl.javax.rmi.RemoteObjectSubstitutionManager;
import org.jboss.dmr.ModelNode;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.naming.ejb.EjbNamingContextSetup;
import org.jboss.ejb.client.naming.ejb.ejbURLContextFactory;
import org.jboss.jca.core.spi.rar.ResourceAdapterRepository;
import org.jboss.metadata.ejb.spec.EjbJarMetaData;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.remoting3.Endpoint;
import org.omg.PortableServer.POA;

import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import java.util.List;

import org.wildfly.security.manager.WildFlySecurityManager;

import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.DEFAULT_ENTITY_BEAN_INSTANCE_POOL;
import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.DEFAULT_ENTITY_BEAN_OPTIMISTIC_LOCKING;
import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.DEFAULT_MDB_INSTANCE_POOL;
import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.DEFAULT_RESOURCE_ADAPTER_NAME;
import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.DEFAULT_SFSB_CACHE;
import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.DEFAULT_SFSB_PASSIVATION_DISABLED_CACHE;
import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.DEFAULT_SINGLETON_BEAN_ACCESS_TIMEOUT;
import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.DEFAULT_SLSB_INSTANCE_POOL;
import static org.jboss.as.ejb3.subsystem.EJB3SubsystemModel.DEFAULT_STATEFUL_BEAN_ACCESS_TIMEOUT;

/**
 * Add operation handler for the EJB3 subsystem.
 *
 * @author Emanuel Muckenhuber
 */
class EJB3SubsystemAdd extends AbstractBoottimeAddStepHandler {

    private final EJBDefaultSecurityDomainProcessor defaultSecurityDomainDeploymentProcessor;
    private final EJBDefaultPermissionsProcessor ejbDefaultPermissionsProcessor;
    private final MissingMethodPermissionsDenyAccessMergingProcessor missingMethodPermissionsDenyAccessMergingProcessor;

    EJB3SubsystemAdd(final EJBDefaultSecurityDomainProcessor defaultSecurityDomainDeploymentProcessor, EJBDefaultPermissionsProcessor ejbDefaultPermissionsProcessor, final MissingMethodPermissionsDenyAccessMergingProcessor missingMethodPermissionsDenyAccessMergingProcessor) {
        this.defaultSecurityDomainDeploymentProcessor = defaultSecurityDomainDeploymentProcessor;
        this.ejbDefaultPermissionsProcessor = ejbDefaultPermissionsProcessor;
        this.missingMethodPermissionsDenyAccessMergingProcessor = missingMethodPermissionsDenyAccessMergingProcessor;
    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        for (SimpleAttributeDefinition attr : EJB3SubsystemRootResourceDefinition.ATTRIBUTES) {
            attr.validateAndSet(operation, model);
        }
    }

    @Override
    protected void performBoottime(final OperationContext context, ModelNode operation, final ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

        //setup IIOP related stuff
        //This goes here rather than in EJB3IIOPAdd as it affects the server when it is acting as an iiop client
        //setup our dynamic stub factory
        DelegatingStubFactoryFactory.setOverriddenDynamicFactory(new DynamicStubFactoryFactory());

        //setup the substitution service, that translates between ejb proxies and IIOP stubs
        final RemoteObjectSubstitutionService substitutionService = new RemoteObjectSubstitutionService();
        newControllers.add(context.getServiceTarget().addService(RemoteObjectSubstitutionService.SERVICE_NAME, substitutionService)
                .addDependency(DeploymentRepository.SERVICE_NAME, DeploymentRepository.class, substitutionService.getDeploymentRepositoryInjectedValue())
                .install());

        RemoteObjectSubstitutionManager.setRemoteObjectSubstitution(substitutionService);

        //setup ejb: namespace
        EjbNamingContextSetup.setupEjbNamespace();
        //TODO: this is a bit of a hack
        InitialContext.addUrlContextFactory("ejb", new ejbURLContextFactory());
        final boolean appclient = context.getProcessType() == ProcessType.APPLICATION_CLIENT;

        final ModelNode defaultDistinctName = EJB3SubsystemRootResourceDefinition.DEFAULT_DISTINCT_NAME.resolveModelAttribute(context, model);
        final DefaultDistinctNameService defaultDistinctNameService = new DefaultDistinctNameService(defaultDistinctName.isDefined() ? defaultDistinctName.asString() : null);
        newControllers.add(context.getServiceTarget().addService(DefaultDistinctNameService.SERVICE_NAME, defaultDistinctNameService).install());

        // set the default security domain name in the deployment unit processor, configured at the subsytem level
        final ModelNode defaultSecurityDomainModelNode = EJB3SubsystemRootResourceDefinition.DEFAULT_SECURITY_DOMAIN.resolveModelAttribute(context, model);
        final String defaultSecurityDomain = defaultSecurityDomainModelNode.isDefined() ? defaultSecurityDomainModelNode.asString() : null;
        this.defaultSecurityDomainDeploymentProcessor.setDefaultSecurityDomainName(defaultSecurityDomain);

        // set the default security domain name in the deployment unit processor, configured at the subsytem level
        final ModelNode defaultMissingMethod = EJB3SubsystemRootResourceDefinition.DEFAULT_MISSING_METHOD_PERMISSIONS_DENY_ACCESS.resolveModelAttribute(context, model);
        final boolean defaultMissingMethodValue = defaultMissingMethod.asBoolean();
        this.missingMethodPermissionsDenyAccessMergingProcessor.setDenyAccessByDefault(defaultMissingMethodValue);


        final ModelNode disableDefaultPermissions = EJB3SubsystemRootResourceDefinition.DISABLE_DEFAULT_EJB_PERMISSIONS.resolveModelAttribute(context, model);
        final boolean disableDefaultPermissionsValue = disableDefaultPermissions.asBoolean();
        this.ejbDefaultPermissionsProcessor.setEnabled(!disableDefaultPermissionsValue);

        context.addStep(new AbstractDeploymentChainStep() {
            @Override
            protected void execute(DeploymentProcessorTarget processorTarget) {

                //DUP's that are used even for app client deployments
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.STRUCTURE, Phase.STRUCTURE_REGISTER_JBOSS_ALL_EJB, new JBossAllXmlParserRegisteringProcessor<EjbJarMetaData>(EjbJarJBossAllParser.ROOT_ELEMENT, EjbJarJBossAllParser.ATTACHMENT_KEY, new EjbJarJBossAllParser()));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_DEFAULT_DISTINCT_NAME, new EjbDefaultDistinctNameProcessor(defaultDistinctNameService));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_CONTEXT_BINDING, new EjbContextJndiBindingProcessor());
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_DEPLOYMENT, new EjbJarParsingDeploymentUnitProcessor());
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_CREATE_COMPONENT_DESCRIPTIONS, new AnnotatedEJBComponentDescriptionDeploymentUnitProcessor(appclient));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_SESSION_BEAN_DD, new SessionBeanXmlDescriptorProcessor(appclient));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_ANNOTATION_EJB, new EjbAnnotationProcessor());
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_INJECTION_ANNOTATION, new EjbResourceInjectionAnnotationProcessor(appclient));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_ENTITY_BEAN_CREATE_COMPONENT_DESCRIPTIONS, new EntityBeanComponentDescriptionFactory(appclient));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_ASSEMBLY_DESC_DD, new AssemblyDescriptorProcessor());
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_DEFAULT_SECURITY_DOMAIN, EJB3SubsystemAdd.this.defaultSecurityDomainDeploymentProcessor);

                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_EJB_PERMISSIONS, ejbDefaultPermissionsProcessor);
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_EJB, new EjbDependencyDeploymentUnitProcessor());
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_HOME_MERGE, new HomeViewMergingProcessor(appclient));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_REF, new EjbRefProcessor(appclient));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_BUSINESS_VIEW_ANNOTATION, new BusinessViewAnnotationProcessor(appclient));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_ORB_BIND, new IIOPJndiBindingProcessor());
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_JNDI_BINDINGS, new EjbJndiBindingsDeploymentUnitProcessor(appclient));
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_CLIENT_METADATA, new EJBClientDescriptorMetaDataProcessor());


                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.INSTALL, Phase.INSTALL_EJB_CLIENT_CONTEXT, new EjbClientContextSetupProcessor());
                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.INSTALL, Phase.INSTALL_EJB_JACC_PROCESSING, new JaccEjbDeploymentProcessor());

                processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.CLEANUP, Phase.CLEANUP_EJB, new EjbCleanUpProcessor());

                if (!appclient) {
                    // add the metadata parser deployment processor

                    // Process @DependsOn after the @Singletons have been registered.
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_TIMERSERVICE_BINDING, new TimerServiceJndiBindingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_APPLICATION_EXCEPTION_ANNOTATION, new ApplicationExceptionAnnotationProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_DD_INTERCEPTORS, new InterceptorClassDeploymentDescriptorProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_EJB_SECURITY_ROLE_REF_DD, new SecurityRoleRefDDProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_PASSIVATION_ANNOTATION, new PassivationAnnotationParsingProcessor());

                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_IMPLICIT_NO_INTERFACE_VIEW, new ImplicitLocalViewProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_APPLICATION_EXCEPTIONS, new ApplicationExceptionMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_DD_INTERCEPTORS, new DeploymentDescriptorInterceptorBindingsProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_DD_METHOD_RESOLUTION, new DeploymentDescriptorMethodProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_TRANSACTION_MANAGEMENT, new TransactionManagementMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_CONCURRENCY_MANAGEMENT_MERGE, new ConcurrencyManagementMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_CONCURRENCY_MERGE, new EjbConcurrencyMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_TX_ATTR_MERGE, new TransactionAttributeMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_RUN_AS_MERGE, new RunAsMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_RESOURCE_ADAPTER_MERGE, new ResourceAdaptorMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_DELIVERY_ACTIVE_MERGE, new DeliveryActiveMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_REMOVE_METHOD, new RemoveMethodMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_STARTUP_MERGE, new StartupMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_SECURITY_DOMAIN, new SecurityDomainMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_SECURITY_MISSING_METHOD_PERMISSIONS, missingMethodPermissionsDenyAccessMergingProcessor);
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_ROLES, new DeclareRolesMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_METHOD_PERMISSIONS, new MethodPermissionsMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_STATEFUL_TIMEOUT, new StatefulTimeoutMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_SESSION_SYNCHRONIZATION, new SessionSynchronizationMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_INIT_METHOD, new InitMethodMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_SESSION_BEAN, new SessionBeanMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_SECURITY_PRINCIPAL_ROLE_MAPPING_MERGE, new SecurityRolesMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_LOCAL_HOME, new SessionBeanHomeProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_CLUSTERED, new ClusteredMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_CACHE, new CacheMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_SLSB_POOL_NAME_MERGE, new StatelessSessionBeanPoolMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_MDB_POOL_NAME_MERGE, new MessageDrivenBeanPoolMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_ENTITY_POOL_NAME_MERGE, new EntityBeanPoolMergingProcessor());
                    // Add the deployment unit processor responsible for processing the user application specific container interceptors configured in jboss-ejb3.xml
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.POST_MODULE, Phase.POST_MODULE_EJB_USER_APP_SPECIFIC_CONTAINER_INTERCEPTORS, new ContainerInterceptorBindingsDDProcessor());

                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.INSTALL, Phase.INSTALL_DEPENDS_ON_ANNOTATION, new EjbDependsOnMergingProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.INSTALL, Phase.INSTALL_DEPLOYMENT_REPOSITORY, new DeploymentRepositoryProcessor());
                    processorTarget.addDeploymentProcessor(EJB3Extension.SUBSYSTEM_NAME, Phase.INSTALL, Phase.INSTALL_EJB_MANAGEMENT_RESOURCES, new EjbManagementDeploymentUnitProcessor());

                }

            }
        }, OperationContext.Stage.RUNTIME);

        //todo maybe needs EJB3SubsystemRootResourceDefinition.DEFAULT_MDB_INSTANCE_POOL.resolveModelAttribute(context,model).isDefined()
        if (model.hasDefined(DEFAULT_MDB_INSTANCE_POOL)) {
            EJB3SubsystemDefaultPoolWriteHandler.MDB_POOL.updatePoolService(context, model, newControllers);
        }

        if (model.hasDefined(DEFAULT_SLSB_INSTANCE_POOL)) {
            EJB3SubsystemDefaultPoolWriteHandler.SLSB_POOL.updatePoolService(context, model, newControllers);
        }

        if (model.hasDefined(DEFAULT_ENTITY_BEAN_INSTANCE_POOL)) {
            EJB3SubsystemDefaultPoolWriteHandler.ENTITY_BEAN_POOL.updatePoolService(context, model, newControllers);
        }

        if (model.hasDefined(DEFAULT_SFSB_CACHE)) {
            EJB3SubsystemDefaultCacheWriteHandler.SFSB_CACHE.updateCacheService(context, model, newControllers);
        }

        if (model.hasDefined(DEFAULT_SFSB_PASSIVATION_DISABLED_CACHE)) {
            EJB3SubsystemDefaultCacheWriteHandler.SFSB_PASSIVATION_DISABLED_CACHE.updateCacheService(context, model, newControllers);
        }

        EJB3SubsystemDefaultCacheWriteHandler.CLUSTERED_SFSB_CACHE.updateCacheService(context, model, newControllers);

        if (model.hasDefined(DEFAULT_RESOURCE_ADAPTER_NAME)) {
            DefaultResourceAdapterWriteHandler.INSTANCE.updateDefaultAdapterService(context, model, newControllers);
        }

        if (model.hasDefined(DEFAULT_SINGLETON_BEAN_ACCESS_TIMEOUT)) {
            DefaultSingletonBeanAccessTimeoutWriteHandler.INSTANCE.updateOrCreateDefaultSingletonBeanAccessTimeoutService(context, model, newControllers);
        }

        if (model.hasDefined(DEFAULT_STATEFUL_BEAN_ACCESS_TIMEOUT)) {
            DefaultStatefulBeanAccessTimeoutWriteHandler.INSTANCE.updateOrCreateDefaultStatefulBeanAccessTimeoutService(context, model, newControllers);
        }

        if (model.hasDefined(DEFAULT_ENTITY_BEAN_OPTIMISTIC_LOCKING)) {
            EJB3SubsystemDefaultEntityBeanOptimisticLockingWriteHandler.INSTANCE.updateOptimisticLocking(context, model, newControllers);
        }

        final ServiceTarget serviceTarget = context.getServiceTarget();

        newControllers.add(context.getServiceTarget().addService(DeploymentRepository.SERVICE_NAME, new DeploymentRepository()).install());

        addRemoteInvocationServices(context, newControllers, model, appclient);
        // add clustering service
        this.addClusteringServices(context, newControllers, appclient);

        // add user transaction access control service
        final EJB3UserTransactionAccessControlService userTxAccessControlService = new EJB3UserTransactionAccessControlService();
        newControllers.add(context.getServiceTarget().addService(EJB3UserTransactionAccessControlService.SERVICE_NAME, userTxAccessControlService)
                .addDependency(UserTransactionAccessControlService.SERVICE_NAME, UserTransactionAccessControlService.class, userTxAccessControlService.getUserTransactionAccessControlServiceInjector())
                .install());

        if (!appclient) {
            final EJBUtilities utilities = new EJBUtilities();
            newControllers.add(serviceTarget.addService(EJBUtilities.SERVICE_NAME, utilities)
                    .addDependency(ConnectorServices.RA_REPOSITORY_SERVICE, ResourceAdapterRepository.class, utilities.getResourceAdapterRepositoryInjector())
                    .addDependency(SimpleSecurityManagerService.SERVICE_NAME, ServerSecurityManager.class, utilities.getSecurityManagerInjector())
                    .addDependency(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER, TransactionManager.class, utilities.getTransactionManagerInjector())
                    .addDependency(TxnServices.JBOSS_TXN_SYNCHRONIZATION_REGISTRY, TransactionSynchronizationRegistry.class, utilities.getTransactionSynchronizationRegistryInjector())
                    .addDependency(TxnServices.JBOSS_TXN_USER_TRANSACTION, UserTransaction.class, utilities.getUserTransactionInjector())
                    .addListener(verificationHandler)
                    .setInitialMode(ServiceController.Mode.ACTIVE)
                    .install());


            // create the POA Registry use by iiop
            final POARegistry poaRegistry = new POARegistry();
            newControllers.add(context.getServiceTarget().addService(POARegistry.SERVICE_NAME, poaRegistry)
                    .addDependency(CorbaPOAService.ROOT_SERVICE_NAME, POA.class, poaRegistry.getRootPOA())
                    .setInitialMode(ServiceController.Mode.PASSIVE)
                    .addListener(verificationHandler)
                    .install());

            EnableStatisticsWriteHandler.INSTANCE.updateToRuntime(context, model);
        }
    }

    private void addRemoteInvocationServices(final OperationContext context, final List<ServiceController<?>> newControllers,
                                             final ModelNode ejbSubsystemModel, final boolean appclient) throws OperationFailedException {

        final ServiceTarget serviceTarget = context.getServiceTarget();
        // Add the tccl based client context selector
        final TCCLEJBClientContextSelectorService tcclBasedClientContextSelector = new TCCLEJBClientContextSelectorService();
        context.getServiceTarget().addService(TCCLEJBClientContextSelectorService.TCCL_BASED_EJB_CLIENT_CONTEXT_SELECTOR_SERVICE_NAME,
                tcclBasedClientContextSelector).install();

        // EJB client context selector will be locked on the server if it's not application client container
        final boolean lockEJBClientContextSelector = appclient ? false : true;
        //add the default EjbClientContext
        //TODO: This should be managed
        final DefaultEjbClientContextService clientContextService = new DefaultEjbClientContextService(lockEJBClientContextSelector);
        final ServiceBuilder<EJBClientContext> clientContextServiceBuilder = context.getServiceTarget().addService(DefaultEjbClientContextService.DEFAULT_SERVICE_NAME,
                clientContextService).addDependency(TCCLEJBClientContextSelectorService.TCCL_BASED_EJB_CLIENT_CONTEXT_SELECTOR_SERVICE_NAME,
                TCCLEJBClientContextSelectorService.class, clientContextService.getTCCLBasedEJBClientContextSelectorInjector());

        // add the EJB remote tx recovery service
        newControllers.add(serviceTarget.addService(EJBTransactionRecoveryService.SERVICE_NAME, EJBTransactionRecoveryService.INSTANCE)
                .addDependency(ArjunaRecoveryManagerService.SERVICE_NAME, RecoveryManagerService.class, EJBTransactionRecoveryService.INSTANCE.getRecoveryManagerServiceInjector())
                .addDependency(TxnServices.JBOSS_TXN_CORE_ENVIRONMENT, CoreEnvironmentBean.class, EJBTransactionRecoveryService.INSTANCE.getCoreEnvironmentBeanInjector())
                .install());

        if (!appclient) {
            // get the node name
            final String nodeName = WildFlySecurityManager.getPropertyPrivileged(ServerEnvironment.NODE_NAME, null);

            final ServiceLookupValue<Endpoint> endpointValue = new ServiceLookupValue<Endpoint>(context.getServiceRegistry(false), RemotingServices.SUBSYSTEM_ENDPOINT);
            final ServiceLookupValue<EJBRemoteConnectorService> ejbRemoteConnectorServiceValue = new ServiceLookupValue<EJBRemoteConnectorService>(context.getServiceRegistry(false), EJBRemoteConnectorService.SERVICE_NAME);

            //the default spec compliant EJB receiver
            final LocalEjbReceiver byValueLocalEjbReceiver = new LocalEjbReceiver(nodeName, false, endpointValue, ejbRemoteConnectorServiceValue);
            newControllers.add(serviceTarget.addService(LocalEjbReceiver.BY_VALUE_SERVICE_NAME, byValueLocalEjbReceiver)
                    .addDependency(DeploymentRepository.SERVICE_NAME, DeploymentRepository.class, byValueLocalEjbReceiver.getDeploymentRepository())
                    .addDependency(ClusteredBackingCacheEntryStoreSourceService.CLIENT_MAPPING_REGISTRY_COLLECTOR_SERVICE_NAME, RegistryCollector.class, byValueLocalEjbReceiver.getClusterRegistryCollectorInjector())
                    .install());

            //the receiver for invocations that allow pass by reference
            final LocalEjbReceiver byReferenceLocalEjbReceiver = new LocalEjbReceiver(nodeName, true, endpointValue, ejbRemoteConnectorServiceValue);
            newControllers.add(serviceTarget.addService(LocalEjbReceiver.BY_REFERENCE_SERVICE_NAME, byReferenceLocalEjbReceiver)
                    .addDependency(DeploymentRepository.SERVICE_NAME, DeploymentRepository.class, byReferenceLocalEjbReceiver.getDeploymentRepository())
                    .addDependency(ClusteredBackingCacheEntryStoreSourceService.CLIENT_MAPPING_REGISTRY_COLLECTOR_SERVICE_NAME, RegistryCollector.class, byReferenceLocalEjbReceiver.getClusterRegistryCollectorInjector())
                    .install());

            // setup the default local ejb receiver service
            EJBRemoteInvocationPassByValueWriteHandler.INSTANCE.updateDefaultLocalEJBReceiverService(context, ejbSubsystemModel, newControllers);
            // add the default local ejb receiver to the client context
            clientContextServiceBuilder.addDependency(LocalEjbReceiver.DEFAULT_LOCAL_EJB_RECEIVER_SERVICE_NAME, LocalEjbReceiver.class, clientContextService.getDefaultLocalEJBReceiverInjector());
        }
        // install the default EJB client context service
        newControllers.add(clientContextServiceBuilder.install());
    }

    private void addClusteringServices(final OperationContext context, final List<ServiceController<?>> newControllers, final boolean appclient) {
        if (appclient) {
            return;
        }
        final RegistryCollectorService<String, List<ClientMapping>> registryCollectorService = new RegistryCollectorService<String, List<ClientMapping>>();
        final ServiceController<RegistryCollector<String, List<ClientMapping>>> registryCollectorServiceController = context.getServiceTarget().addService(ClusteredBackingCacheEntryStoreSourceService.CLIENT_MAPPING_REGISTRY_COLLECTOR_SERVICE_NAME, registryCollectorService).install();
        newControllers.add(registryCollectorServiceController);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/154.java