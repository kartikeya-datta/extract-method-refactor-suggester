error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14622.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14622.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14622.java
text:
```scala
i@@f (securityDomain != null || securityDomainAndApplication != null || application) {

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
package org.jboss.as.connector.subsystems.resourceadapters;

import org.jboss.as.connector.deployers.ra.processors.IronJacamarDeploymentParsingProcessor;
import org.jboss.as.connector.deployers.ra.processors.ParsedRaDeploymentProcessor;
import org.jboss.as.connector.deployers.ra.processors.RaDeploymentParsingProcessor;
import org.jboss.as.connector.deployers.ra.processors.RaNativeProcessor;
import org.jboss.as.connector.logging.ConnectorLogger;
import org.jboss.as.connector.metadata.xmldescriptors.ConnectorXmlDescriptor;
import org.jboss.as.connector.metadata.xmldescriptors.IronJacamarXmlDescriptor;
import org.jboss.as.connector.services.resourceadapters.deployment.InactiveResourceAdapterDeploymentService;
import org.jboss.as.connector.services.resourceadapters.deployment.ResourceAdapterXmlDeploymentService;
import org.jboss.as.connector.util.ConnectorServices;
import org.jboss.as.connector.util.ModelNodeUtil;
import org.jboss.as.connector.util.RaServicesFactory;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.annotation.ResourceRootIndexer;
import org.jboss.as.server.deployment.module.MountHandle;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.dmr.ModelNode;
import org.jboss.jandex.Index;
import org.jboss.jca.common.api.metadata.common.CommonAdminObject;
import org.jboss.jca.common.api.metadata.common.CommonPool;
import org.jboss.jca.common.api.metadata.common.CommonSecurity;
import org.jboss.jca.common.api.metadata.common.CommonTimeOut;
import org.jboss.jca.common.api.metadata.common.CommonValidation;
import org.jboss.jca.common.api.metadata.common.Credential;
import org.jboss.jca.common.api.metadata.common.Extension;
import org.jboss.jca.common.api.metadata.common.FlushStrategy;
import org.jboss.jca.common.api.metadata.common.Recovery;
import org.jboss.jca.common.api.metadata.common.TransactionSupportEnum;
import org.jboss.jca.common.api.metadata.common.v10.CommonConnDef;
import org.jboss.jca.common.api.metadata.resourceadapter.ResourceAdapter;
import org.jboss.jca.common.api.validator.ValidateException;
import org.jboss.jca.common.metadata.common.CommonPoolImpl;
import org.jboss.jca.common.metadata.common.CommonSecurityImpl;
import org.jboss.jca.common.metadata.common.CommonTimeOutImpl;
import org.jboss.jca.common.metadata.common.CommonValidationImpl;
import org.jboss.jca.common.metadata.common.CommonXaPoolImpl;
import org.jboss.jca.common.metadata.common.CredentialImpl;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jboss.as.connector.logging.ConnectorMessages.MESSAGES;
import static org.jboss.as.connector.subsystems.common.pool.Constants.BACKGROUNDVALIDATION;
import static org.jboss.as.connector.subsystems.common.pool.Constants.BACKGROUNDVALIDATIONMILLIS;
import static org.jboss.as.connector.subsystems.common.pool.Constants.BLOCKING_TIMEOUT_WAIT_MILLIS;
import static org.jboss.as.connector.subsystems.common.pool.Constants.IDLETIMEOUTMINUTES;
import static org.jboss.as.connector.subsystems.common.pool.Constants.MAX_POOL_SIZE;
import static org.jboss.as.connector.subsystems.common.pool.Constants.MIN_POOL_SIZE;
import static org.jboss.as.connector.subsystems.common.pool.Constants.POOL_FLUSH_STRATEGY;
import static org.jboss.as.connector.subsystems.common.pool.Constants.POOL_PREFILL;
import static org.jboss.as.connector.subsystems.common.pool.Constants.POOL_USE_STRICT_MIN;
import static org.jboss.as.connector.subsystems.common.pool.Constants.USE_FAST_FAIL;
import static org.jboss.as.connector.subsystems.jca.Constants.DEFAULT_NAME;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.ALLOCATION_RETRY;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.ALLOCATION_RETRY_WAIT_MILLIS;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.APPLICATION;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.BEANVALIDATION_GROUPS;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.BOOTSTRAP_CONTEXT;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.CLASS_NAME;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.ENABLED;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.INTERLEAVING;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.JNDINAME;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.NOTXSEPARATEPOOL;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.NO_RECOVERY;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.PAD_XID;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.RECOVERLUGIN_CLASSNAME;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.RECOVERLUGIN_PROPERTIES;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.RECOVERY_PASSWORD;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.RECOVERY_SECURITY_DOMAIN;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.RECOVERY_USERNAME;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.SAME_RM_OVERRIDE;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.SECURITY_DOMAIN;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.SECURITY_DOMAIN_AND_APPLICATION;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.TRANSACTION_SUPPORT;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.USE_CCM;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.USE_JAVA_CONTEXT;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.WRAP_XA_RESOURCE;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.XA_RESOURCE_TIMEOUT;


public class RaOperationUtil {
    private static final String RAR_EXTENSION = ".rar";
    private static final ServiceName RAR_MODULE = ServiceName.of("rarinsidemodule");


    public static ModifiableResourceAdapter buildResourceAdaptersObject(final OperationContext context, ModelNode operation, String archiveOrModule) throws OperationFailedException {
        Map<String, String> configProperties = new HashMap<String, String>(0);
        List<CommonConnDef> connectionDefinitions = new ArrayList<CommonConnDef>(0);
        List<CommonAdminObject> adminObjects = new ArrayList<CommonAdminObject>(0);
        TransactionSupportEnum transactionSupport = operation.hasDefined(TRANSACTION_SUPPORT.getName()) ? TransactionSupportEnum
                .valueOf(operation.get(TRANSACTION_SUPPORT.getName()).asString()) : null;
        String bootstrapContext = ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, operation, BOOTSTRAP_CONTEXT);
        List<String> beanValidationGroups = null;
        if (operation.hasDefined(BEANVALIDATION_GROUPS.getName())) {
            beanValidationGroups = new ArrayList<String>(operation.get(BEANVALIDATION_GROUPS.getName()).asList().size());
            for (ModelNode beanValidation : operation.get(BEANVALIDATION_GROUPS.getName()).asList()) {
                beanValidationGroups.add(beanValidation.asString());
            }

        }
        ModifiableResourceAdapter ra;
        ra = new ModifiableResourceAdapter(archiveOrModule, transactionSupport, connectionDefinitions,
                adminObjects, configProperties, beanValidationGroups, bootstrapContext);

        return ra;

    }

    public static ModifiableConnDef buildConnectionDefinitionObject(final OperationContext context, final ModelNode recoveryEnvModel, final String poolName,
                                                                    final boolean isXa) throws OperationFailedException, ValidateException {
        Map<String, String> configProperties = new HashMap<String, String>(0);
        String className = ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, recoveryEnvModel, CLASS_NAME);
        String jndiName = ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, recoveryEnvModel, JNDINAME);
        boolean enabled = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, ENABLED);
        boolean useJavaContext = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, USE_JAVA_CONTEXT);
        boolean useCcm = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, USE_CCM);

        int maxPoolSize = ModelNodeUtil.getIntIfSetOrGetDefault(context, recoveryEnvModel, MAX_POOL_SIZE);
        int minPoolSize = ModelNodeUtil.getIntIfSetOrGetDefault(context, recoveryEnvModel, MIN_POOL_SIZE);
        boolean prefill = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, POOL_PREFILL);
        boolean useStrictMin = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, POOL_USE_STRICT_MIN);
        String flushStrategyString = POOL_FLUSH_STRATEGY.resolveModelAttribute(context, recoveryEnvModel).asString();
        final FlushStrategy flushStrategy = FlushStrategy.forName(flushStrategyString);
        Boolean isSameRM  = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, SAME_RM_OVERRIDE);
        boolean interlivng = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, INTERLEAVING);
        boolean padXid = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, PAD_XID);
        boolean wrapXaResource = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, WRAP_XA_RESOURCE);
        boolean noTxSeparatePool = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, NOTXSEPARATEPOOL);



        Integer allocationRetry = ModelNodeUtil.getIntIfSetOrGetDefault(context, recoveryEnvModel, ALLOCATION_RETRY);
        Long allocationRetryWaitMillis = ModelNodeUtil.getLongIfSetOrGetDefault(context, recoveryEnvModel, ALLOCATION_RETRY_WAIT_MILLIS);
        Long blockingTimeoutMillis = ModelNodeUtil.getLongIfSetOrGetDefault(context, recoveryEnvModel, BLOCKING_TIMEOUT_WAIT_MILLIS);
        Long idleTimeoutMinutes = ModelNodeUtil.getLongIfSetOrGetDefault(context, recoveryEnvModel, IDLETIMEOUTMINUTES);
        Integer xaResourceTimeout = ModelNodeUtil.getIntIfSetOrGetDefault(context, recoveryEnvModel, XA_RESOURCE_TIMEOUT);

        CommonTimeOut timeOut = new CommonTimeOutImpl(blockingTimeoutMillis, idleTimeoutMinutes, allocationRetry,
                allocationRetryWaitMillis, xaResourceTimeout);
        CommonPool pool;
        if (isXa) {
            pool = new CommonXaPoolImpl(minPoolSize, maxPoolSize, prefill, useStrictMin, flushStrategy, isSameRM, interlivng, padXid, wrapXaResource, noTxSeparatePool);
        } else {
            pool = new CommonPoolImpl(minPoolSize, maxPoolSize, prefill, useStrictMin, flushStrategy);
        }
        String securityDomain = ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, recoveryEnvModel, SECURITY_DOMAIN);
        String securityDomainAndApplication = ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, recoveryEnvModel, SECURITY_DOMAIN_AND_APPLICATION);

        boolean application = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, APPLICATION);
        CommonSecurity security = null;
        if (securityDomain != null || securityDomainAndApplication != null) {
            security = new CommonSecurityImpl(securityDomain, securityDomainAndApplication, application);
        }
        Long backgroundValidationMillis = ModelNodeUtil.getLongIfSetOrGetDefault(context, recoveryEnvModel, BACKGROUNDVALIDATIONMILLIS);
        boolean backgroundValidation = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, BACKGROUNDVALIDATION);
        boolean useFastFail = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, USE_FAST_FAIL);
        CommonValidation validation = new CommonValidationImpl(backgroundValidation, backgroundValidationMillis, useFastFail);

        final String recoveryUsername = ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, recoveryEnvModel, RECOVERY_USERNAME);

        final String recoveryPassword =  ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, recoveryEnvModel, RECOVERY_PASSWORD);
        final String recoverySecurityDomain = ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, recoveryEnvModel, RECOVERY_SECURITY_DOMAIN);
        boolean noRecovery = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, recoveryEnvModel, NO_RECOVERY);

        Recovery recovery = null;
        if ((recoveryUsername != null && recoveryPassword != null) || recoverySecurityDomain != null) {
            Credential credential = null;
            credential = new CredentialImpl(recoveryUsername, recoveryPassword, recoverySecurityDomain);
            Extension recoverPlugin = ModelNodeUtil.extractExtension(context, recoveryEnvModel, RECOVERLUGIN_CLASSNAME, RECOVERLUGIN_PROPERTIES);
            recovery = new Recovery(credential, recoverPlugin, noRecovery);
        }
        ModifiableConnDef connectionDefinition = new ModifiableConnDef(configProperties, className, jndiName, poolName,
                enabled, useJavaContext, useCcm, pool, timeOut, validation, security, recovery);

        return connectionDefinition;

    }

    public static ModifiableAdminObject buildAdminObjects(final OperationContext context, ModelNode operation, final String poolName) throws OperationFailedException, ValidateException {
        Map<String, String> configProperties = new HashMap<String, String>(0);
        String className = ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, operation, CLASS_NAME);
        String jndiName = ModelNodeUtil.getResolvedStringIfSetOrGetDefault(context, operation, JNDINAME);
        boolean enabled = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, operation, ENABLED);
        boolean useJavaContext = ModelNodeUtil.getBooleanIfSetOrGetDefault(context, operation, USE_JAVA_CONTEXT);

        ModifiableAdminObject adminObject = new ModifiableAdminObject(configProperties, className, jndiName, poolName,
                enabled, useJavaContext);
        return adminObject;
    }


    public static ServiceName restartIfPresent(OperationContext context, final String raName, ServiceVerificationHandler svh) throws OperationFailedException {
        final ServiceName raDeploymentServiceName = ConnectorServices.getDeploymentServiceName(raName);
        if (raDeploymentServiceName != null) {
            final ServiceRegistry registry = context.getServiceRegistry(true);
            ServiceController raServiceController = registry.getService(raDeploymentServiceName);
            final org.jboss.msc.service.ServiceController.Mode originalMode = raServiceController.getMode();
            if (raServiceController != null) {
                if (svh != null) {
                    raServiceController.addListener(ServiceListener.Inheritance.ALL, svh);
                }
                raServiceController.addListener(new AbstractServiceListener() {
                    @Override
                    public void transition(ServiceController controller, ServiceController.Transition transition) {
                        switch (transition) {
                            case STOPPING_to_DOWN:
                                try {
                                    final ServiceController<?> RaxmlController = registry.getService(ServiceName.of(ConnectorServices.RA_SERVICE, raName));
                                    ResourceAdapter raxml = (ResourceAdapter) RaxmlController.getValue();
                                    ((ResourceAdapterXmlDeploymentService) controller.getService()).setRaxml(raxml);
                                    controller.compareAndSetMode(ServiceController.Mode.NEVER, originalMode);
                                } finally {
                                    controller.removeListener(this);
                                }

                        }
                    }

                    @Override
                    public void listenerAdded(ServiceController controller) {
                        controller.setMode(ServiceController.Mode.NEVER);
                    }

                });



            }
        }

        return raDeploymentServiceName;

    }

    public static boolean removeIfActive(OperationContext context, String raName) throws OperationFailedException {
        boolean wasActive = false;
        final ServiceName raDeploymentServiceName = ConnectorServices.getDeploymentServiceName(raName);
        Integer identifier = 0;
        if (raName.contains("->")) {
            identifier = Integer.valueOf(raName.substring(raName.indexOf("->") + 2));
            raName = raName.substring(0, raName.indexOf("->"));
        }
        if (raDeploymentServiceName != null) {
            context.removeService(raDeploymentServiceName);
            ConnectorServices.unregisterDeployment(raName, raDeploymentServiceName);
            wasActive = true;
        }
        ConnectorServices.unregisterResourceIdentifier(raName, identifier);

        return wasActive;

    }

    public static void activate(OperationContext context, String raName, final ServiceVerificationHandler serviceVerificationHandler) throws OperationFailedException {
        ServiceRegistry registry = context.getServiceRegistry(true);

        final ServiceController<?> inactiveRaController = registry.getService(ConnectorServices.INACTIVE_RESOURCE_ADAPTER_SERVICE.append(raName));
        if (inactiveRaController == null) {
            throw new OperationFailedException("rar not yet deployed");
        }
        InactiveResourceAdapterDeploymentService.InactiveResourceAdapterDeployment inactive = (InactiveResourceAdapterDeploymentService.InactiveResourceAdapterDeployment) inactiveRaController.getValue();
        final ServiceController<?> RaxmlController = registry.getService(ServiceName.of(ConnectorServices.RA_SERVICE, raName));

        ResourceAdapter raxml = (ResourceAdapter) RaxmlController.getValue();
        RaServicesFactory.createDeploymentService(inactive.getRegistration(), inactive.getConnectorXmlDescriptor(), inactive.getModule(), inactive.getServiceTarget(), raName, inactive.getDeploymentUnitServiceName(), inactive.getDeployment(), raxml, inactive.getResource(), serviceVerificationHandler);


    }

    public static ServiceName installRaServices(OperationContext context, ServiceVerificationHandler verificationHandler, String name, ModifiableResourceAdapter resourceAdapter, final List<ServiceController<?>> newControllers) {
        final ServiceTarget serviceTarget = context.getServiceTarget();

        final ServiceController<?> resourceAdaptersService = context.getServiceRegistry(false).getService(
                ConnectorServices.RESOURCEADAPTERS_SERVICE);
        if (resourceAdaptersService == null) {
            newControllers.add(serviceTarget.addService(ConnectorServices.RESOURCEADAPTERS_SERVICE,
                    new ResourceAdaptersService()).setInitialMode(ServiceController.Mode.ACTIVE).addListener(verificationHandler).install());
        }
        ServiceName raServiceName = ServiceName.of(ConnectorServices.RA_SERVICE, name);
        String bootStrapCtxName = DEFAULT_NAME;
        if (resourceAdapter.getBootstrapContext() != null && ! resourceAdapter.getBootstrapContext().equals("undefined")) {
            bootStrapCtxName = resourceAdapter.getBootstrapContext();
        }
        final ServiceController<?> service = context.getServiceRegistry(true).getService(raServiceName);
        if (service == null) {
            ResourceAdapterService raService = new ResourceAdapterService(resourceAdapter);
            newControllers.add(serviceTarget.addService(raServiceName, raService).setInitialMode(ServiceController.Mode.ACTIVE)
                    .addDependency(ConnectorServices.RESOURCEADAPTERS_SERVICE, ResourceAdaptersService.ModifiableResourceAdaptors.class, raService.getResourceAdaptersInjector())
                    .addDependency(ConnectorServices.BOOTSTRAP_CONTEXT_SERVICE.append(bootStrapCtxName))
                    .addListener(verificationHandler).install());
        }
        return raServiceName;
    }

    public static void installRaServicesAndDeployFromModule(OperationContext context, ServiceVerificationHandler verificationHandler, String name, ModifiableResourceAdapter resourceAdapter, String fullModuleName, final List<ServiceController<?>> newControllers) throws OperationFailedException{
        ServiceName raServiceName =  installRaServices(context, verificationHandler, name, resourceAdapter, newControllers);
        final boolean resolveProperties = true;
        final ServiceTarget serviceTarget = context.getServiceTarget();
        final String moduleName;


        //load module
        String slot = "main";
        if (fullModuleName.contains(":")) {
            slot = fullModuleName.substring(fullModuleName.indexOf(":") + 1);
            moduleName = fullModuleName.substring(0, fullModuleName.indexOf(":"));
        } else {
            moduleName = fullModuleName;
        }

        Module module;
        try {
            ModuleIdentifier moduleId = ModuleIdentifier.create(moduleName, slot);
            module = Module.getCallerModuleLoader().loadModule(moduleId);
        } catch (ModuleLoadException e) {
            throw new OperationFailedException(MESSAGES.failedToLoadModuleRA(moduleName), e);
        }
        URL path = module.getExportedResource("META-INF/ra.xml");
        Closeable closable = null;
            try {
                VirtualFile child;
                if (path.getPath().contains("!")) {
                    throw new OperationFailedException(MESSAGES.compressedRarNotSupportedInModuleRA(moduleName));
                } else {
                    child = VFS.getChild(path.getPath().split("META-INF")[0]);

                    closable = VFS.mountReal(new File(path.getPath().split("META-INF")[0]), child);
                }
                //final Closeable closable = VFS.mountZip((InputStream) new JarInputStream(new FileInputStream(path.getPath().split("!")[0].split(":")[1])), path.getPath().split("!")[0].split(":")[1], child, TempFileProviderService.provider());

                final MountHandle mountHandle = new MountHandle(closable);
                final ResourceRoot resourceRoot = new ResourceRoot(child, mountHandle);

                final VirtualFile deploymentRoot = resourceRoot.getRoot();
                if (deploymentRoot == null || !deploymentRoot.exists())
                    return;
                ConnectorXmlDescriptor connectorXmlDescriptor = RaDeploymentParsingProcessor.process(resolveProperties, deploymentRoot, null, name);
                IronJacamarXmlDescriptor ironJacamarXmlDescriptor = IronJacamarDeploymentParsingProcessor.process(deploymentRoot, resolveProperties);
                RaNativeProcessor.process(deploymentRoot);
                Map<ResourceRoot, Index> annotationIndexes = new HashMap<ResourceRoot, Index>();
                ResourceRootIndexer.indexResourceRoot(resourceRoot);
                Index index = resourceRoot.getAttachment(Attachments.ANNOTATION_INDEX);
                if (index != null) {
                    annotationIndexes.put(resourceRoot, index);
                }
                if (ironJacamarXmlDescriptor != null) {
                    ConnectorLogger.SUBSYSTEM_RA_LOGGER.forceIJToNull();
                    ironJacamarXmlDescriptor = null;
                }
                final ServiceName deployerServiceName = ConnectorServices.RESOURCE_ADAPTER_DEPLOYER_SERVICE_PREFIX.append(connectorXmlDescriptor.getDeploymentName());
                final ServiceController<?> deployerService = context.getServiceRegistry(true).getService(deployerServiceName);
                if (deployerService == null) {
                    ServiceBuilder builder = ParsedRaDeploymentProcessor.process(connectorXmlDescriptor, ironJacamarXmlDescriptor, module.getClassLoader(), serviceTarget, annotationIndexes, RAR_MODULE.append(name), verificationHandler);
                    newControllers.add(builder.addDependency(raServiceName).setInitialMode(ServiceController.Mode.ACTIVE).install());
                }
                String rarName = resourceAdapter.getArchive();

                if (fullModuleName.equals(rarName)) {

                    ServiceName serviceName = ConnectorServices.INACTIVE_RESOURCE_ADAPTER_SERVICE.append(name);

                    InactiveResourceAdapterDeploymentService service = new InactiveResourceAdapterDeploymentService(connectorXmlDescriptor, module, name, name, RAR_MODULE.append(name), null, serviceTarget, null);
                    newControllers.add(serviceTarget
                            .addService(serviceName, service)
                            .setInitialMode(ServiceController.Mode.ACTIVE).addListener(verificationHandler).install());

                }

            } catch (Exception e) {
                throw new OperationFailedException(MESSAGES.failedToLoadModuleRA(moduleName), e);
            } finally {
                if (closable != null) {
                    try {
                        closable.close();
                    } catch (IOException e) {

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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14622.java