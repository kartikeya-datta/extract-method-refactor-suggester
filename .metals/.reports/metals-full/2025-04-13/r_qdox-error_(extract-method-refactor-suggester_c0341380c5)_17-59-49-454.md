error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13131.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13131.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13131.java
text:
```scala
r@@eturn new EJBMethodDescription(methodName, (String[]) null);

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

package org.jboss.as.ejb3.deployment.processors.dd;

import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ejb3.component.EJBMethodDescription;
import org.jboss.as.ejb3.component.MethodIntf;
import org.jboss.as.ejb3.component.session.SessionBeanComponentDescription;
import org.jboss.as.ejb3.component.singleton.SingletonComponentDescription;
import org.jboss.as.ejb3.component.stateful.StatefulComponentDescription;
import org.jboss.as.ejb3.component.stateless.StatelessComponentDescription;
import org.jboss.as.ejb3.deployment.EjbJarDescription;
import org.jboss.as.ejb3.deployment.EjbDeploymentAttachmentKeys;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.logging.Logger;
import org.jboss.metadata.ejb.spec.AccessTimeoutMetaData;
import org.jboss.metadata.ejb.spec.BusinessLocalsMetaData;
import org.jboss.metadata.ejb.spec.BusinessRemotesMetaData;
import org.jboss.metadata.ejb.spec.ConcurrentMethodMetaData;
import org.jboss.metadata.ejb.spec.ConcurrentMethodsMetaData;
import org.jboss.metadata.ejb.spec.ContainerTransactionMetaData;
import org.jboss.metadata.ejb.spec.ContainerTransactionsMetaData;
import org.jboss.metadata.ejb.spec.MethodMetaData;
import org.jboss.metadata.ejb.spec.MethodParametersMetaData;
import org.jboss.metadata.ejb.spec.MethodsMetaData;
import org.jboss.metadata.ejb.spec.NamedMethodMetaData;
import org.jboss.metadata.ejb.spec.SessionBean31MetaData;
import org.jboss.metadata.ejb.spec.SessionBeanMetaData;
import org.jboss.metadata.ejb.spec.SessionType;

import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LockType;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagementType;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

/**
 * @author Jaikiran Pai
 */
public class SessionBeanXmlDescriptorProcessor extends AbstractEjbXmlDescriptorProcessor<SessionBeanMetaData> {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(SessionBeanXmlDescriptorProcessor.class);

    @Override
    protected Class<SessionBeanMetaData> getMetaDataType() {
        return SessionBeanMetaData.class;
    }

    /**
     * Processes the passed {@link org.jboss.metadata.ejb.spec.SessionBeanMetaData} and creates appropriate {@link org.jboss.as.ejb3.component.session.SessionBeanComponentDescription} out of it.
     * The {@link org.jboss.as.ejb3.component.session.SessionBeanComponentDescription} is then added to the {@link org.jboss.as.ee.component.EEModuleDescription module description} available
     * in the deployment unit of the passed {@link DeploymentPhaseContext phaseContext}
     *
     * @param sessionBean  The session bean metadata
     * @param phaseContext
     * @throws DeploymentUnitProcessingException
     *
     */
    @Override
    protected void processBeanMetaData(SessionBeanMetaData sessionBean, DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final EjbJarDescription ejbJarDescription = deploymentUnit.getAttachment(EjbDeploymentAttachmentKeys.EJB_JAR_DESCRIPTION);
        // get the module description
        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        final String applicationName = moduleDescription.getApplicationName();

        SessionType sessionType = sessionBean.getSessionType();
        if (sessionType == null) {
            throw new DeploymentUnitProcessingException("Unknown session-type for session bean: " + sessionBean.getName() + " in deployment unit: " + deploymentUnit);
        }
        String beanName = sessionBean.getName();
        String beanClassName = sessionBean.getEjbClass();
        SessionBeanComponentDescription sessionBeanDescription = null;
        switch (sessionType) {
            case Stateless:
                sessionBeanDescription = new StatelessComponentDescription(beanName, beanClassName, ejbJarDescription, deploymentUnit.getServiceName());
                break;
            case Stateful:
                sessionBeanDescription = new StatefulComponentDescription(beanName, beanClassName, ejbJarDescription, deploymentUnit.getServiceName());
                break;
            case Singleton:
                sessionBeanDescription = new SingletonComponentDescription(beanName, beanClassName, ejbJarDescription, deploymentUnit.getServiceName());
                break;
            default:
                throw new IllegalArgumentException("Unknown session bean type: " + sessionType);
        }
        // mapped-name
        sessionBeanDescription.setMappedName(sessionBean.getMappedName());
        // local business interface views
        BusinessLocalsMetaData businessLocals = sessionBean.getBusinessLocals();
        if (businessLocals != null && !businessLocals.isEmpty()) {
            sessionBeanDescription.addLocalBusinessInterfaceViews(businessLocals);
        }
        // remote business interface views
        BusinessRemotesMetaData businessRemotes = sessionBean.getBusinessRemotes();
        if (businessRemotes != null && !businessRemotes.isEmpty()) {
            sessionBeanDescription.addRemoteBusinessInterfaceViews(businessRemotes);
        }
        // tx management type
        if (sessionBean.getTransactionType() != null) {
            sessionBeanDescription.setTransactionManagementType(sessionBean.getTransactionType());
        }
        // CMT Tx attributes
        if (sessionBean.getTransactionType() != TransactionManagementType.BEAN) {
            ContainerTransactionsMetaData containerTransactions = sessionBean.getContainerTransactions();
            if (containerTransactions != null && !containerTransactions.isEmpty()) {
                for (ContainerTransactionMetaData containerTx : containerTransactions) {
                    TransactionAttributeType txAttr = containerTx.getTransAttribute();
                    MethodsMetaData methods = containerTx.getMethods();
                    for (MethodMetaData method : methods) {
                        String methodName = method.getMethodName();
                        MethodIntf methodIntf = this.getMethodIntf(method.getMethodIntf());
                        if (methodName.equals("*")) {
                            sessionBeanDescription.setTransactionAttribute(methodIntf, txAttr);
                        } else {

                            MethodParametersMetaData methodParams = method.getMethodParams();
                            // update the session bean description with the tx attribute info
                            sessionBeanDescription.setTransactionAttribute(methodIntf, txAttr, methodName, this.getMethodParams(methodParams));
                        }
                    }
                }
            }
        }

        // interceptors
        this.processInterceptors(sessionBean, sessionBeanDescription);

        // process EJB3.1 specific session bean description
        if (sessionBean instanceof SessionBean31MetaData) {
            this.processSessionBean31((SessionBean31MetaData) sessionBean, sessionBeanDescription);
        }

        // Add this component description to the module description
        ejbJarDescription.getEEModuleDescription().addComponent(sessionBeanDescription);

    }

    private void processSessionBean31(SessionBean31MetaData sessionBean31MetaData, SessionBeanComponentDescription sessionBeanComponentDescription) {
        // no-interface view
        if (sessionBean31MetaData.isNoInterfaceBean()) {
            sessionBeanComponentDescription.addNoInterfaceView();
        }
        // process singleton bean specific description
        if (sessionBean31MetaData.isSingleton() && sessionBeanComponentDescription instanceof SingletonComponentDescription) {
            this.processSingletonBean(sessionBean31MetaData, (SingletonComponentDescription) sessionBeanComponentDescription);
        }
    }

    private void processSingletonBean(SessionBean31MetaData singletonBeanMetaData, SingletonComponentDescription singletonComponentDescription) {
        Boolean initOnStartup = singletonBeanMetaData.isInitOnStartup();
        if (initOnStartup != null && initOnStartup.booleanValue() == true) {
            singletonComponentDescription.initOnStartup();
        }
        // bean level lock-type
        LockType lockType = singletonBeanMetaData.getLockType();
        singletonComponentDescription.setBeanLevelLockType(lockType);
        // add method level lock type to the description
        ConcurrentMethodsMetaData concurrentMethods = singletonBeanMetaData.getConcurrentMethods();
        if (concurrentMethods != null) {
            for (ConcurrentMethodMetaData concurrentMethod : concurrentMethods) {
                LockType methodLockType = concurrentMethod.getLockType();
                EJBMethodDescription method = this.getEJBMethodDescription(concurrentMethod.getMethod());
                singletonComponentDescription.setLockType(methodLockType, method);
            }
        }

        // concurrency management type
        ConcurrencyManagementType concurrencyManagementType = singletonBeanMetaData.getConcurrencyManagementType();
        if (concurrencyManagementType == ConcurrencyManagementType.BEAN) {
            singletonComponentDescription.beanManagedConcurrency();
        } else {
            singletonComponentDescription.containerManagedConcurrency();
        }

        // bean level access timeout
        // TODO: This should apply to other bean types too (JBoss specific feature) and not just singleton beans
        AccessTimeoutMetaData accessTimeoutMetaData = singletonBeanMetaData.getAccessTimeout();
        if (accessTimeoutMetaData != null) {
            final long timeout = accessTimeoutMetaData.getTimeout();
            final TimeUnit unit = accessTimeoutMetaData.getUnit();
            AccessTimeout accessTimeout = new AccessTimeout() {
                @Override
                public long value() {
                    return timeout;
                }

                @Override
                public TimeUnit unit() {
                    return unit;
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return AccessTimeout.class;
                }
            };
            singletonComponentDescription.setBeanLevelAccessTimeout(accessTimeout);
        }
    }

    private EJBMethodDescription getEJBMethodDescription(NamedMethodMetaData namedMethodMetaData) {
        if (namedMethodMetaData == null) {
            return null;
        }
        String methodName = namedMethodMetaData.getMethodName();
        MethodParametersMetaData methodParams = namedMethodMetaData.getMethodParams();
        if (methodParams == null) {
            return new EJBMethodDescription(methodName, null);
        }
        return new EJBMethodDescription(methodName, methodParams.toArray(new String[methodParams.size()]));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13131.java