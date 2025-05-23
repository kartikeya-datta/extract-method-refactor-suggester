error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12747.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12747.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12747.java
text:
```scala
.@@addDependency(DependencyType.OPTIONAL,

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

package org.jboss.as.security;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.security.Constants.AUDIT_MANAGER_CLASS_NAME;
import static org.jboss.as.security.Constants.AUTHENTICATION_MANAGER_CLASS_NAME;
import static org.jboss.as.security.Constants.AUTHORIZATION_MANAGER_CLASS_NAME;
import static org.jboss.as.security.Constants.DEEP_COPY_SUBJECT_MODE;
import static org.jboss.as.security.Constants.DEFAULT_CALLBACK_HANDLER_CLASS_NAME;
import static org.jboss.as.security.Constants.IDENTITY_TRUST_MANAGER_CLASS_NAME;
import static org.jboss.as.security.Constants.MAPPING_MANAGER_CLASS_NAME;
import static org.jboss.as.security.Constants.SECURITY_DOMAIN;
import static org.jboss.as.security.Constants.SUBJECT_FACTORY_CLASS_NAME;

import javax.security.auth.login.Configuration;

import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.as.clustering.infinispan.subsystem.EmbeddedCacheManagerService;
import org.jboss.as.controller.BasicOperationResult;
import org.jboss.as.controller.ModelAddOperationHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationResult;
import org.jboss.as.controller.ResultHandler;
import org.jboss.as.controller.RuntimeTask;
import org.jboss.as.controller.RuntimeTaskContext;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.naming.NamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.security.context.SecurityDomainJndiInjectable;
import org.jboss.as.security.processors.SecurityDependencyProcessor;
import org.jboss.as.security.service.JaasConfigurationService;
import org.jboss.as.security.service.SecurityBootstrapService;
import org.jboss.as.security.service.SecurityManagementService;
import org.jboss.as.security.service.SubjectFactoryService;
import org.jboss.as.server.BootOperationContext;
import org.jboss.as.server.BootOperationHandler;
import org.jboss.as.server.deployment.Phase;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceBuilder.DependencyType;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.security.ISecurityManagement;
import org.jboss.security.auth.callback.JBossCallbackHandler;
import org.jboss.security.auth.login.XMLLoginConfigImpl;
import org.jboss.security.authentication.JBossCachedAuthenticationManager;
import org.jboss.security.plugins.JBossAuthorizationManager;
import org.jboss.security.plugins.JBossSecuritySubjectFactory;
import org.jboss.security.plugins.audit.JBossAuditManager;
import org.jboss.security.plugins.identitytrust.JBossIdentityTrustManager;
import org.jboss.security.plugins.mapping.JBossMappingManager;

/**
 * Add Security Subsystem Operation.
 *
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 * @author Brian Stansberry
 */
class SecuritySubsystemAdd implements ModelAddOperationHandler, BootOperationHandler {

    private static final Logger log = Logger.getLogger("org.jboss.as.security");

    private static final String AUTHENTICATION_MANAGER = ModuleName.PICKETBOX.getName() + ":" + ModuleName.PICKETBOX.getSlot()
            + ":" + JBossCachedAuthenticationManager.class.getName();

    private static final String CALLBACK_HANDLER = ModuleName.PICKETBOX.getName() + ":" + ModuleName.PICKETBOX.getSlot() + ":"
            + JBossCallbackHandler.class.getName();

    private static final String AUTHORIZATION_MANAGER = ModuleName.PICKETBOX.getName() + ":" + ModuleName.PICKETBOX.getSlot()
            + ":" + JBossAuthorizationManager.class.getName();

    private static final String AUDIT_MANAGER = ModuleName.PICKETBOX.getName() + ":" + ModuleName.PICKETBOX.getSlot() + ":"
            + JBossAuditManager.class.getName();

    private static final String IDENTITY_TRUST_MANAGER = ModuleName.PICKETBOX.getName() + ":" + ModuleName.PICKETBOX.getSlot()
            + ":" + JBossIdentityTrustManager.class.getName();

    private static final String MAPPING_MANAGER = ModuleName.PICKETBOX.getName() + ":" + ModuleName.PICKETBOX.getSlot() + ":"
            + JBossMappingManager.class.getName();

    private static final boolean DEFAULT_DEEP_COPY_OPERATION_MODE = false;

    private static final String SUBJECT_FACTORY = ModuleName.PICKETBOX.getName() + ":" + ModuleName.PICKETBOX.getSlot() + ":"
            + JBossSecuritySubjectFactory.class.getName();

    private static final String CACHE_CONTAINER_NAME = "security";

    static final SecuritySubsystemAdd INSTANCE = new SecuritySubsystemAdd();

    /** Private to ensure singleton. */
    private SecuritySubsystemAdd() {
    }

    @Override
    public OperationResult execute(OperationContext context, final ModelNode operation, final ResultHandler resultHandler) {
        String authenticationManagerClassName = "default";
        String callbackHandlerClassName = "default";
        String subjectFactoryClassName = "default";
        String authorizationManagerClassName = "default";
        String auditManagerClassName = "default";
        String identityTrustManagerClassName = "default";
        String mappingManagerClassName = "default";

        final ModelNode subModel = context.getSubModel();
        if (operation.hasDefined(AUTHENTICATION_MANAGER_CLASS_NAME)) {
            authenticationManagerClassName = operation.get(AUTHENTICATION_MANAGER_CLASS_NAME).asString();
            subModel.get(AUTHENTICATION_MANAGER_CLASS_NAME).set(authenticationManagerClassName);
        }
        final boolean deepCopySubject;
        if (operation.hasDefined(DEEP_COPY_SUBJECT_MODE)) {
            deepCopySubject = operation.get(DEEP_COPY_SUBJECT_MODE).asBoolean();
            subModel.get(DEEP_COPY_SUBJECT_MODE).set(deepCopySubject);
        } else {
            deepCopySubject = DEFAULT_DEEP_COPY_OPERATION_MODE;
        }
        if (operation.hasDefined(DEFAULT_CALLBACK_HANDLER_CLASS_NAME)) {
            callbackHandlerClassName = operation.get(DEFAULT_CALLBACK_HANDLER_CLASS_NAME).asString();
            subModel.get(DEFAULT_CALLBACK_HANDLER_CLASS_NAME).set(callbackHandlerClassName);
        }
        if (operation.hasDefined(SUBJECT_FACTORY_CLASS_NAME)) {
            subjectFactoryClassName = operation.get(SUBJECT_FACTORY_CLASS_NAME).asString();
            subModel.get(SUBJECT_FACTORY_CLASS_NAME).set(subjectFactoryClassName);
        }
        if (operation.hasDefined(AUTHORIZATION_MANAGER_CLASS_NAME)) {
            authorizationManagerClassName = operation.get(AUTHORIZATION_MANAGER_CLASS_NAME).asString();
            subModel.get(AUTHORIZATION_MANAGER_CLASS_NAME).set(authorizationManagerClassName);
        }
        if (operation.hasDefined(AUDIT_MANAGER_CLASS_NAME)) {
            auditManagerClassName = operation.get(AUDIT_MANAGER_CLASS_NAME).asString();
            subModel.get(AUDIT_MANAGER_CLASS_NAME).set(auditManagerClassName);
        }
        if (operation.hasDefined(IDENTITY_TRUST_MANAGER_CLASS_NAME)) {
            identityTrustManagerClassName = operation.get(IDENTITY_TRUST_MANAGER_CLASS_NAME).asString();
            subModel.get(IDENTITY_TRUST_MANAGER_CLASS_NAME).set(identityTrustManagerClassName);
        }
        if (operation.hasDefined(MAPPING_MANAGER_CLASS_NAME)) {
            mappingManagerClassName = operation.get(MAPPING_MANAGER_CLASS_NAME).asString();
            subModel.get(MAPPING_MANAGER_CLASS_NAME).set(mappingManagerClassName);
        }
        subModel.get(SECURITY_DOMAIN).setEmptyObject();

        // add security management service
        final String resolvedAuthenticationManagerClassName;
        if ("default".equals(authenticationManagerClassName)) {
            resolvedAuthenticationManagerClassName = AUTHENTICATION_MANAGER;
        } else {
            resolvedAuthenticationManagerClassName = authenticationManagerClassName;
        }
        final String resolvedCallbackHandlerClassName;
        if ("default".equals(callbackHandlerClassName)) {
            resolvedCallbackHandlerClassName = CALLBACK_HANDLER;
        } else {
            resolvedCallbackHandlerClassName = callbackHandlerClassName;
        }
        final String resolvedAuthorizationManagerClassName;
        if ("default".equals(authorizationManagerClassName)) {
            resolvedAuthorizationManagerClassName = AUTHORIZATION_MANAGER;
        } else {
            resolvedAuthorizationManagerClassName = authorizationManagerClassName;
        }
        final String resolvedAuditManagerClassName;
        if ("default".equals(auditManagerClassName)) {
            resolvedAuditManagerClassName = AUDIT_MANAGER;
        } else {
            resolvedAuditManagerClassName = auditManagerClassName;
        }
        final String resolvedIdentityTrustManagerClassName;
        if ("default".equals(identityTrustManagerClassName)) {
            resolvedIdentityTrustManagerClassName = IDENTITY_TRUST_MANAGER;
        } else {
            resolvedIdentityTrustManagerClassName = identityTrustManagerClassName;
        }
        final String resolvedMappingManagerClassName;
        if ("default".equals(mappingManagerClassName)) {
            resolvedMappingManagerClassName = MAPPING_MANAGER;
        } else {
            resolvedMappingManagerClassName = mappingManagerClassName;
        }
        final String resolvedSubjectFactoryClassName;
        if ("default".equals(subjectFactoryClassName)) {
            resolvedSubjectFactoryClassName = SUBJECT_FACTORY;
        } else {
            resolvedSubjectFactoryClassName = subjectFactoryClassName;
        }

        if (context instanceof BootOperationContext) {
            final BootOperationContext updateContext = (BootOperationContext) context;
            context.getRuntimeContext().setRuntimeTask(new RuntimeTask() {
                public void execute(RuntimeTaskContext context) throws OperationFailedException {
                    updateContext.addDeploymentProcessor(Phase.DEPENDENCIES, Phase.DEPENDENCIES_MODULE,
                            new SecurityDependencyProcessor());
                    log.info("Activating Security Subsystem");

                    final ServiceTarget target = context.getServiceTarget();

                    // add bootstrap service
                    final SecurityBootstrapService bootstrapService = new SecurityBootstrapService();
                    target.addService(SecurityBootstrapService.SERVICE_NAME, bootstrapService)
                            .setInitialMode(ServiceController.Mode.ACTIVE).install();

                    // add service to bind SecurityDomainJndiInjectable to JNDI
                    final SecurityDomainJndiInjectable securityDomainJndiInjectable = new SecurityDomainJndiInjectable();
                    final BinderService binderService = new BinderService("jaas");
                    target.addService(ContextNames.JAVA_CONTEXT_SERVICE_NAME.append("jboss", "jaas"), binderService)
                            .addInjection(binderService.getManagedObjectInjector(), securityDomainJndiInjectable)
                            .addDependency(ContextNames.JAVA_CONTEXT_SERVICE_NAME.append("jboss"), NamingStore.class,
                                    binderService.getNamingStoreInjector())
                            .addDependency(SecurityManagementService.SERVICE_NAME, ISecurityManagement.class,
                                    securityDomainJndiInjectable.getSecurityManagementInjector())
                            .setInitialMode(ServiceController.Mode.ACTIVE).install();

                    // add security management service
                    final SecurityManagementService securityManagementService = new SecurityManagementService(
                            resolvedAuthenticationManagerClassName, deepCopySubject, resolvedCallbackHandlerClassName,
                            resolvedAuthorizationManagerClassName, resolvedAuditManagerClassName,
                            resolvedIdentityTrustManagerClassName, resolvedMappingManagerClassName);
                    target.addService(SecurityManagementService.SERVICE_NAME, securityManagementService)
                            .addDependency(DependencyType.REQUIRED,
                                    EmbeddedCacheManagerService.getServiceName(CACHE_CONTAINER_NAME),
                                    EmbeddedCacheManager.class,
                                    securityManagementService.getAuthenticationCacheManagerInjector())
                            .setInitialMode(ServiceController.Mode.ACTIVE).install();

                    // add subject factory service
                    final SubjectFactoryService subjectFactoryService = new SubjectFactoryService(
                            resolvedSubjectFactoryClassName);
                    target.addService(SubjectFactoryService.SERVICE_NAME, subjectFactoryService)
                            .addDependency(SecurityManagementService.SERVICE_NAME, ISecurityManagement.class,
                                    subjectFactoryService.getSecurityManagementInjector())
                            .setInitialMode(ServiceController.Mode.ACTIVE).install();

                    // add jaas configuration service
                    Configuration loginConfig = XMLLoginConfigImpl.getInstance();
                    final JaasConfigurationService jaasConfigurationService = new JaasConfigurationService(loginConfig);
                    target.addService(JaasConfigurationService.SERVICE_NAME, jaasConfigurationService)
                            .setInitialMode(ServiceController.Mode.ACTIVE).install();

                    resultHandler.handleResultComplete();
                }
            });
        } else {
            resultHandler.handleResultComplete();
        }

        // Create the compensating operation
        final ModelNode compensatingOperation = Util.getResourceRemoveOperation(operation.require(OP_ADDR));
        return new BasicOperationResult(compensatingOperation);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12747.java