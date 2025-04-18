error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12394.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12394.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12394.java
text:
```scala
r@@ealmBuilder.setInitialMode(ServiceController.Mode.ON_DEMAND);

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
package org.jboss.as.domain.management.security;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.AUTHENTICATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.AUTHORIZATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CONNECTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.JAAS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.LDAP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.LOCAL;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROTOCOL;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SECRET;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_IDENTITY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SSL;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TRUSTSTORE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.USER;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.USERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.domain.management.ModelDescriptionConstants.ALIAS;
import static org.jboss.as.domain.management.ModelDescriptionConstants.ALLOWED_USERS;
import static org.jboss.as.domain.management.ModelDescriptionConstants.DEFAULT_USER;
import static org.jboss.as.domain.management.ModelDescriptionConstants.KEYSTORE_PASSWORD;
import static org.jboss.as.domain.management.ModelDescriptionConstants.KEYSTORE_PATH;
import static org.jboss.as.domain.management.ModelDescriptionConstants.KEYSTORE_RELATIVE_TO;
import static org.jboss.as.domain.management.ModelDescriptionConstants.KEY_PASSWORD;
import static org.jboss.as.domain.management.ModelDescriptionConstants.PASSWORD;
import static org.jboss.as.domain.management.ModelDescriptionConstants.RELATIVE_TO;
import static org.jboss.as.domain.management.ModelDescriptionConstants.PLUG_IN;
import static org.jboss.msc.service.ServiceController.Mode.ON_DEMAND;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.security.ServerSecurityManager;
import org.jboss.as.domain.management.CallbackHandlerFactory;
import org.jboss.as.domain.management.connections.ConnectionManager;
import org.jboss.as.domain.management.connections.ldap.LdapConnectionManagerService;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;

/**
 * Handler to add security realm definitions and register the service.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class SecurityRealmAddHandler implements OperationStepHandler {

    public static final SecurityRealmAddHandler INSTANCE = new SecurityRealmAddHandler();

    @Override
    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
        context.createResource(PathAddress.EMPTY_ADDRESS);

        // Add a step validating that we have the correct authentication child resources
        ModelNode validationOp = AuthenticationValidatingHandler.createOperation(operation);
        context.addStep(validationOp, AuthenticationValidatingHandler.INSTANCE, OperationContext.Stage.MODEL);

        context.addStep(new OperationStepHandler() {
            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                // Install another RUNTIME handler to actually install the services. This will run after the
                // RUNTIME handler for any child resources. Doing this will ensure that child resource handlers don't
                // see the installed services and can just ignore doing any RUNTIME stage work
                context.addStep(ServiceInstallStepHandler.INSTANCE, OperationContext.Stage.RUNTIME);
                context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
            }
        }, OperationContext.Stage.RUNTIME);

        context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
    }

    protected void installServices(final OperationContext context, final String realmName, final ModelNode model,
                                   final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers)
            throws OperationFailedException {
        final ModelNode plugIns = model.hasDefined(PLUG_IN) ? model.get(PLUG_IN) : null;
        final ModelNode authentication = model.hasDefined(AUTHENTICATION) ? model.get(AUTHENTICATION) : null;
        final ModelNode authorization = model.hasDefined(AUTHORIZATION) ? model.get(AUTHORIZATION) : null;
        final ModelNode serverIdentities = model.hasDefined(SERVER_IDENTITY) ? model.get(SERVER_IDENTITY) : null;

        final ServiceTarget serviceTarget = context.getServiceTarget();

        final SecurityRealmService securityRealmService = new SecurityRealmService(realmName);
        final ServiceName realmServiceName = SecurityRealmService.BASE_SERVICE_NAME.append(realmName);
        ServiceBuilder<?> realmBuilder = serviceTarget.addService(realmServiceName, securityRealmService);

        ServiceName plugInLoaderName = null;
        ServiceName authenticationName = null;
        ServiceName authorizationName = null;
        ModelNode authTruststore = null;
        if (plugIns != null) {
            plugInLoaderName = addPlugInLoaderService(realmServiceName, plugIns, serviceTarget, newControllers);
        }
        if (authentication != null) {
            // Authentication can have a truststore defined at the same time as a username/password based mechanism.
            //
            // In this case it is expected certificate based authentication will first occur with a fallback to username/password
            // based authentication.
            if (authentication.hasDefined(TRUSTSTORE)) {
                authTruststore = authentication.require(TRUSTSTORE);
                ServiceName ccName = addClientCertService(realmServiceName, serviceTarget, newControllers);
                realmBuilder.addDependency(ccName, CallbackHandlerService.class, securityRealmService.getCallbackHandlerService().injector());
            }
            if (authentication.hasDefined(LOCAL)) {
                ServiceName localName = addLocalService(authentication.require(LOCAL), realmServiceName, serviceTarget,
                        newControllers);
                realmBuilder.addDependency(localName, CallbackHandlerService.class, securityRealmService.getCallbackHandlerService().injector());
            }
            if (authentication.hasDefined(JAAS)) {
                authenticationName = addJaasService(authentication.require(JAAS), realmServiceName, serviceTarget, newControllers, context.isNormalServer());
            } else if (authentication.hasDefined(LDAP)) {
                authenticationName = addLdapService(authentication.require(LDAP), realmServiceName, serviceTarget, newControllers);
            } else if (authentication.hasDefined(PLUG_IN)) {
                authenticationName = addPlugInAuthenticationService(authentication.require(PLUG_IN), realmServiceName,
                        plugInLoaderName, securityRealmService, serviceTarget, newControllers);
            } else if (authentication.hasDefined(PROPERTIES)) {
                authenticationName = addPropertiesAuthenticationService(authentication.require(PROPERTIES), realmServiceName, realmName, serviceTarget, newControllers);
            } else if (authentication.hasDefined(USERS)) {
                authenticationName = addUsersService(context, authentication.require(USERS), realmServiceName, realmName, serviceTarget, newControllers);
            }
        }
        if (authorization != null) {
            if (authorization.hasDefined(PROPERTIES)) {
                authorizationName = addPropertiesAuthorizationService(authorization.require(PROPERTIES), realmServiceName,
                        realmName, serviceTarget, newControllers);
            } else if (authorization.hasDefined(PLUG_IN)) {
                authorizationName = addPlugInAuthorizationService(authorization.require(PLUG_IN), realmServiceName,
                        plugInLoaderName, realmName, serviceTarget, newControllers);
            }
        }
        if (authenticationName != null) {
            realmBuilder.addDependency(authenticationName, CallbackHandlerService.class, securityRealmService.getCallbackHandlerService().injector());
        }
        if (authorizationName != null) {
            realmBuilder.addDependency(authorizationName, SubjectSupplementalService.class, securityRealmService.getSubjectSupplementalInjector());
        }

        ModelNode ssl = null;
        if (serverIdentities != null) {
            if (serverIdentities.hasDefined(SSL)) {
                ssl = serverIdentities.require(SSL);
            }
            if (serverIdentities.hasDefined(SECRET)) {
                ServiceName secretServiceName = addSecretService(context, serverIdentities.require(SECRET), realmServiceName,serviceTarget,newControllers);
                realmBuilder.addDependency(secretServiceName, CallbackHandlerFactory.class,securityRealmService.getSecretCallbackFactory());
            }
        }

        if (ssl != null || authTruststore != null) {
            ServiceName sslServiceName = addSSLService(context, ssl, authTruststore, realmServiceName, serviceTarget, newControllers);
            realmBuilder.addDependency(sslServiceName, SSLIdentityService.class, securityRealmService.getSSLIdentityInjector());
        }

        realmBuilder.setInitialMode(Mode.ACTIVE);
        ServiceController<?> sc = realmBuilder.install();
        if (newControllers != null) {
            newControllers.add(sc);
        }
    }

    private ServiceName addPlugInLoaderService(ServiceName realmServiceName, ModelNode plugInModel,
            ServiceTarget serviceTarget, List<ServiceController<?>> newControllers) {
        ServiceName plugInLoaderName = realmServiceName.append(PlugInLoaderService.SERVICE_SUFFIX);

        PlugInLoaderService loaderService = new PlugInLoaderService(plugInModel);
        ServiceBuilder<PlugInLoaderService> builder = serviceTarget.addService(plugInLoaderName, loaderService);
        newControllers.add(builder.setInitialMode(Mode.ON_DEMAND).install());

        return plugInLoaderName;
    }

    private ServiceName addClientCertService(ServiceName realmServiceName, ServiceTarget serviceTarget,
            List<ServiceController<?>> newControllers) {
        ServiceName clientCertServiceName = realmServiceName.append(ClientCertCallbackHandler.SERVICE_SUFFIX);
        ClientCertCallbackHandler clientCertCallbackHandler = new ClientCertCallbackHandler();

        ServiceBuilder<?> ccBuilder = serviceTarget.addService(clientCertServiceName, clientCertCallbackHandler);

        newControllers.add(ccBuilder.setInitialMode(ON_DEMAND).install());

        return clientCertServiceName;
    }

    private ServiceName addJaasService(ModelNode jaas, ServiceName realmServiceName, ServiceTarget serviceTarget,
            List<ServiceController<?>> newControllers, boolean injectServerManager) {
        ServiceName jaasServiceName = realmServiceName.append(JaasCallbackHandler.SERVICE_SUFFIX);
        JaasCallbackHandler jaasCallbackHandler = new JaasCallbackHandler(jaas.get(NAME).asString());

        ServiceBuilder<?> jaasBuilder = serviceTarget.addService(jaasServiceName, jaasCallbackHandler);
        if (injectServerManager) {
            jaasBuilder.addDependency(ServiceName.JBOSS.append("security", "simple-security-manager"),
                    ServerSecurityManager.class, jaasCallbackHandler.getSecurityManagerValue());
        }

        newControllers.add(jaasBuilder.setInitialMode(ON_DEMAND).install());

        return jaasServiceName;
    }

    private ServiceName addLdapService(ModelNode ldap, ServiceName realmServiceName, ServiceTarget serviceTarget, List<ServiceController<?>> newControllers) {
        ServiceName ldapServiceName = realmServiceName.append(UserLdapCallbackHandler.SERVICE_SUFFIX);
        UserLdapCallbackHandler ldapCallbackHandler = new UserLdapCallbackHandler(ldap);

        ServiceBuilder<?> ldapBuilder = serviceTarget.addService(ldapServiceName, ldapCallbackHandler);
        String connectionManager = ldap.require(CONNECTION).asString();
        ldapBuilder.addDependency(LdapConnectionManagerService.BASE_SERVICE_NAME.append(connectionManager), ConnectionManager.class, ldapCallbackHandler.getConnectionManagerInjector());

        final ServiceController<?> serviceController = ldapBuilder.setInitialMode(ON_DEMAND)
                .install();
        if(newControllers != null) {
            newControllers.add(serviceController);
        }

        return ldapServiceName;
    }

    private ServiceName addLocalService(ModelNode local, ServiceName realmServiceName, ServiceTarget serviceTarget,
            List<ServiceController<?>> newControllers) {
        ServiceName localServiceName = realmServiceName.append(LocalCallbackHandlerService.SERVICE_SUFFIX);

        String defaultUser = local.hasDefined(DEFAULT_USER) ? local.get(DEFAULT_USER).asString() : null;
        String allowedUsers = local.hasDefined(ALLOWED_USERS) ? local.get(ALLOWED_USERS).asString() : null;
        LocalCallbackHandlerService localCallbackHandler = new LocalCallbackHandlerService(defaultUser, allowedUsers);

        ServiceBuilder<?> jaasBuilder = serviceTarget.addService(localServiceName, localCallbackHandler);

        newControllers.add(jaasBuilder.setInitialMode(ON_DEMAND).install());

        return localServiceName;
    }

    private ServiceName addPlugInAuthenticationService(ModelNode plugIn, ServiceName realmServiceName,
            ServiceName plugInLoaderName, SecurityRealmService registry, ServiceTarget serviceTarget,
            List<ServiceController<?>> newControllers) {
        ServiceName plugInServiceName = realmServiceName.append(PlugInAuthenticationCallbackHandler.SERVICE_SUFFIX);

        PlugInAuthenticationCallbackHandler plugInService = new PlugInAuthenticationCallbackHandler(registry.getName(), plugIn);

        ServiceBuilder<CallbackHandlerService> plugInBuilder = serviceTarget.addService(plugInServiceName, plugInService);
        plugInBuilder.addDependency(plugInLoaderName, PlugInLoaderService.class, plugInService.getPlugInLoaderServiceValue());

        newControllers.add(plugInBuilder.setInitialMode(ON_DEMAND).install());

        return plugInServiceName;
    }

    private ServiceName addPropertiesAuthenticationService(ModelNode properties, ServiceName realmServiceName,
            String realmName, ServiceTarget serviceTarget, List<ServiceController<?>> newControllers) {
        ServiceName propsServiceName = realmServiceName.append(PropertiesCallbackHandler.SERVICE_SUFFIX);
        PropertiesCallbackHandler propsCallbackHandler = new PropertiesCallbackHandler(realmName, properties);

        ServiceBuilder<?> propsBuilder = serviceTarget.addService(propsServiceName, propsCallbackHandler);
        if (properties.hasDefined(RELATIVE_TO)) {
            propsBuilder.addDependency(pathName(properties.get(RELATIVE_TO).asString()), String.class, propsCallbackHandler.getRelativeToInjector());
        }

        final ServiceController<?> serviceController = propsBuilder.setInitialMode(ON_DEMAND)
                .install();

        if(newControllers != null) {
            newControllers.add(serviceController);
        }

        return propsServiceName;
    }

    private ServiceName addPropertiesAuthorizationService(ModelNode properties, ServiceName realmServiceName, String realmName,
            ServiceTarget serviceTarget, List<ServiceController<?>> newControllers) {
        ServiceName propsServiceName = realmServiceName.append(PropertiesSubjectSupplemental.SERVICE_SUFFIX);
        PropertiesSubjectSupplemental propsSubjectSupplemental = new PropertiesSubjectSupplemental(properties);

        ServiceBuilder<?> propsBuilder = serviceTarget.addService(propsServiceName, propsSubjectSupplemental);
        if (properties.hasDefined(RELATIVE_TO)) {
            propsBuilder.addDependency(pathName(properties.get(RELATIVE_TO).asString()), String.class,
                    propsSubjectSupplemental.getRelativeToInjector());
        }

        final ServiceController<?> serviceController = propsBuilder.setInitialMode(ON_DEMAND).install();
        if(newControllers != null) {
            newControllers.add(serviceController);
        }

        return propsServiceName;
    }

    private ServiceName addPlugInAuthorizationService(ModelNode properties, ServiceName realmServiceName,
            ServiceName plugInLoaderName, String realmName, ServiceTarget serviceTarget,
            List<ServiceController<?>> newControllers) {
        ServiceName plugInServiceName = realmServiceName.append(PlugInSubjectSupplemental.SERVICE_SUFFIX);
        PlugInSubjectSupplemental plugInSubjectSupplemental = new PlugInSubjectSupplemental(realmName, properties);

        ServiceBuilder<?> plugInBuilder = serviceTarget.addService(plugInServiceName, plugInSubjectSupplemental);
        plugInBuilder.addDependency(plugInLoaderName, PlugInLoaderService.class,
                plugInSubjectSupplemental.getPlugInLoaderServiceValue());

        final ServiceController<?> serviceController = plugInBuilder.setInitialMode(ON_DEMAND).install();
        if (newControllers != null) {
            newControllers.add(serviceController);
        }

        return plugInServiceName;
    }

    private ServiceName addSSLService(OperationContext context, ModelNode ssl, ModelNode trustStore, ServiceName realmServiceName,
            ServiceTarget serviceTarget, List<ServiceController<?>> newControllers) throws OperationFailedException {
        ServiceName sslServiceName = realmServiceName.append(SSLIdentityService.SERVICE_SUFFIX);

        ServiceName keystoreServiceName = null;
        KeyPair pair = null;
        if (ssl != null && ssl.hasDefined(KEYSTORE_PATH)) {
            keystoreServiceName = realmServiceName.append(FileKeystoreService.KEYSTORE_SUFFIX);
            pair = addFileKeystoreService(context, ssl, keystoreServiceName, serviceTarget, newControllers);
        }
        ServiceName truststoreServiceName = null;
        if (trustStore != null) {
            truststoreServiceName = realmServiceName.append(FileKeystoreService.TRUSTSTORE_SUFFIX);
            addFileKeystoreService(context, trustStore, truststoreServiceName, serviceTarget, newControllers);
        }

        String protocol = "TLS";
        if (ssl != null && ssl.hasDefined(PROTOCOL)) {
            protocol = ssl.get(PROTOCOL).asString();
        }
        SSLIdentityService sslIdentityService = new SSLIdentityService(protocol, pair == null ? null : pair.keystorePassword,
                pair == null ? null : pair.keyPassword);

        ServiceBuilder<?> sslBuilder = serviceTarget.addService(sslServiceName, sslIdentityService);

        if (keystoreServiceName != null) {
            sslBuilder.addDependency(keystoreServiceName, KeyStore.class, sslIdentityService.getKeyStoreInjector());
        }
        if (truststoreServiceName != null) {
            sslBuilder.addDependency(truststoreServiceName, KeyStore.class, sslIdentityService.getTrustStoreInjector());
        }

        final ServiceController<?> serviceController = sslBuilder.setInitialMode(ON_DEMAND).install();
        if(newControllers != null) {
            newControllers.add(serviceController);
        }

        return sslServiceName;
    }

    private static class KeyPair {
        private char[] keystorePassword;
        private char[] keyPassword;
    }

    private KeyPair addFileKeystoreService(OperationContext context, ModelNode ssl, ServiceName serviceName,
            ServiceTarget serviceTarget, List<ServiceController<?>> newControllers) throws OperationFailedException {
        char[] keystorePassword = unmaskPassword(context, ssl.require(KEYSTORE_PASSWORD));
        char[] keyPassword = null;
        if (ssl.hasDefined(KEY_PASSWORD)) {
            keyPassword = unmaskPassword(context, ssl.require(KEY_PASSWORD));
        }

        String path = ssl.require(KEYSTORE_PATH).asString();
        String alias = ssl.hasDefined(ALIAS) ? ssl.require(ALIAS).asString() : null;
        FileKeystoreService fileKeystoreService = new FileKeystoreService(path, keystorePassword, alias, keyPassword);

        ServiceBuilder<?> serviceBuilder = serviceTarget.addService(serviceName, fileKeystoreService);
        if (ssl.hasDefined(KEYSTORE_RELATIVE_TO)) {
            serviceBuilder.addDependency(pathName(ssl.require(KEYSTORE_RELATIVE_TO).asString()), String.class,
                    fileKeystoreService.getRelativeToInjector());
        }

        final ServiceController<?> serviceController = serviceBuilder.setInitialMode(ON_DEMAND).install();
        if(newControllers != null) {
            newControllers.add(serviceController);
        }

        KeyPair pair = new KeyPair();
        pair.keystorePassword = keystorePassword;
        pair.keyPassword = keyPassword;
        return pair;
    }

    private ServiceName addSecretService(OperationContext context, ModelNode secret, ServiceName realmServiceName, ServiceTarget serviceTarget, List<ServiceController<?>> newControllers) throws OperationFailedException {
        ServiceName secretServiceName = realmServiceName.append(SecretIdentityService.SERVICE_SUFFIX);

        ModelNode secretValueNode = secret.require(VALUE);
        String resolvedValue = context.resolveExpressions(secretValueNode).asString();

        SecretIdentityService sis = new SecretIdentityService(resolvedValue, secretValueNode.asString().equals(resolvedValue));
        final ServiceController<CallbackHandlerFactory> serviceController = serviceTarget.addService(secretServiceName, sis)
                .setInitialMode(ON_DEMAND)
                .install();
        if(newControllers != null) {
            newControllers.add(serviceController);
        }

        return secretServiceName;
    }

    private ServiceName addUsersService(OperationContext context, ModelNode users, ServiceName realmServiceName, String realmName, ServiceTarget serviceTarget, List<ServiceController<?>> newControllers) throws OperationFailedException {
        ServiceName usersServiceName = realmServiceName.append(UserDomainCallbackHandler.SERVICE_SUFFIX);

        UserDomainCallbackHandler usersCallbackHandler = new UserDomainCallbackHandler(realmName, unmaskUsersPasswords(context, users));

        ServiceBuilder<?> usersBuilder = serviceTarget.addService(usersServiceName, usersCallbackHandler);


        final ServiceController<?> serviceController = usersBuilder.setInitialMode(ServiceController.Mode.ON_DEMAND)
                .install();
        if(newControllers != null) {
            newControllers.add(serviceController);
        }

        return usersServiceName;
    }

    private static ServiceName pathName(String relativeTo) {
        return ServiceName.JBOSS.append("server", "path", relativeTo);
    }

    private char[] unmaskPassword(OperationContext context, ModelNode password) throws OperationFailedException {
        return context.resolveExpressions(password).asString().toCharArray();
    }

    private ModelNode unmaskUsersPasswords(OperationContext context, ModelNode users) throws OperationFailedException {
        users = users.clone();
        for (Property property : users.get(USER).asPropertyList()) {
            ModelNode user = property.getValue();
            if (user.hasDefined(PASSWORD)) {
                //TODO This will be cleaned up once it uses attribute definitions
                user.set(PASSWORD, context.resolveExpressions(user.get(PASSWORD)).asString());
            }
        }
        return users;
    }

    private static class ServiceInstallStepHandler implements OperationStepHandler {

        private static final ServiceInstallStepHandler INSTANCE = new ServiceInstallStepHandler();

        @Override
        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            final List<ServiceController<?>> newControllers = new ArrayList<ServiceController<?>>();
            final String realmName = ManagementUtil.getSecurityRealmName(operation);
            final ModelNode model = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));
            SecurityRealmAddHandler.INSTANCE.installServices(context, realmName, model, new ServiceVerificationHandler(), newControllers);
            context.completeStep(new OperationContext.RollbackHandler() {
                @Override
                public void handleRollback(OperationContext context, ModelNode operation) {
                    for (ServiceController<?> sc : newControllers) {
                        context.removeService(sc);
                    }
                }
            });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12394.java