error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4601.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4601.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4601.java
text:
```scala
R@@OOT_LOGGER.debugf("Looking up %s in jndi context: %s", namespaceStrippedJndiName, jndiContext);

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
package org.jboss.as.ejb3.component;

import static org.jboss.as.ejb3.logging.EjbLogger.ROOT_LOGGER;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.Map;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagementType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.as.core.security.ServerSecurityManager;
import org.jboss.as.ee.component.BasicComponent;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ejb3.component.allowedmethods.AllowedMethodsInformation;
import org.jboss.as.ejb3.component.invocationmetrics.InvocationMetrics;
import org.jboss.as.ejb3.context.CurrentInvocationContext;
import org.jboss.as.ejb3.logging.EjbLogger;
import org.jboss.as.ejb3.remote.EJBRemoteTransactionsRepository;
import org.jboss.as.ejb3.security.EJBSecurityMetaData;
import org.jboss.as.ejb3.timerservice.TimerServiceImpl;
import org.jboss.as.ejb3.tx.ApplicationExceptionDetails;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.naming.context.NamespaceContextSelector;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.as.server.suspend.ServerActivityCallback;
import org.jboss.ejb.client.EJBClient;
import org.jboss.ejb.client.EJBHomeLocator;
import org.jboss.invocation.InterceptorContext;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.proxy.MethodIdentifier;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.wildfly.extension.requestcontroller.ControlPoint;
import org.wildfly.security.manager.WildFlySecurityManager;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public abstract class EJBComponent extends BasicComponent implements ServerActivityCallback {
    private static final Logger log = Logger.getLogger(EJBComponent.class);

    private static final ApplicationExceptionDetails APPLICATION_EXCEPTION = new ApplicationExceptionDetails("java.lang.Exception", true, false);

    private final Map<MethodTransactionAttributeKey, TransactionAttributeType> txAttrs;
    private final Map<MethodTransactionAttributeKey, Integer> txTimeouts;

    private final EJBUtilities utilities;
    private final boolean isBeanManagedTransaction;
    private final Map<Class<?>, ApplicationExceptionDetails> applicationExceptions;
    private final EJBSecurityMetaData securityMetaData;
    private final Map<String, ServiceName> viewServices;
    private final ServiceName ejbLocalHomeViewServiceName;
    private final ServiceName ejbHomeViewServiceName;
    private final ServiceName ejbObjectViewServiceName;
    private final ServiceName ejbLocalObjectViewServiceName;

    private final TimerService timerService;
    private final Map<Method, InterceptorFactory> timeoutInterceptors;
    private final Method timeoutMethod;
    private final String applicationName;
    private final String earApplicationName;
    private final String moduleName;
    private final String distinctName;
    private final String policyContextID;
    private final EJBRemoteTransactionsRepository ejbRemoteTransactionsRepository;

    private final InvocationMetrics invocationMetrics = new InvocationMetrics();
    private final TransactionManager transactionManager;
    private final TransactionSynchronizationRegistry transactionSynchronizationRegistry;
    private final UserTransaction userTransaction;
    private final ServerSecurityManager serverSecurityManager;
    private final ControlPoint controlPoint;
    private final AtomicBoolean exceptionLoggingEnabled;

    private final PrivilegedAction<Principal> getCaller = new PrivilegedAction<Principal>() {
        @Override
        public Principal run() {
            return serverSecurityManager.getCallerPrincipal();
        }
    };

    /**
     * Construct a new instance.
     *
     * @param ejbComponentCreateService the component configuration
     */
    protected EJBComponent(final EJBComponentCreateService ejbComponentCreateService) {
        super(ejbComponentCreateService);


        this.applicationExceptions = Collections.unmodifiableMap(ejbComponentCreateService.getApplicationExceptions().getApplicationExceptions());

        this.utilities = ejbComponentCreateService.getEJBUtilities();
        final Map<MethodTransactionAttributeKey, TransactionAttributeType> txAttrs = ejbComponentCreateService.getTxAttrs();
        if (txAttrs == null || txAttrs.isEmpty()) {
            this.txAttrs = Collections.emptyMap();
        } else {
            this.txAttrs = txAttrs;
        }
        final Map<MethodTransactionAttributeKey, Integer> txTimeouts = ejbComponentCreateService.getTxTimeouts();
        if (txTimeouts == null || txTimeouts.isEmpty()) {
            this.txTimeouts = Collections.emptyMap();
        } else {
            this.txTimeouts = txTimeouts;
        }
        isBeanManagedTransaction = TransactionManagementType.BEAN.equals(ejbComponentCreateService.getTransactionManagementType());

        // security metadata
        this.securityMetaData = ejbComponentCreateService.getSecurityMetaData();
        this.viewServices = ejbComponentCreateService.getViewServices();
        this.timerService = ejbComponentCreateService.getTimerService();
        this.timeoutMethod = ejbComponentCreateService.getTimeoutMethod();
        this.ejbLocalHomeViewServiceName = ejbComponentCreateService.getEjbLocalHome();
        this.ejbHomeViewServiceName = ejbComponentCreateService.getEjbHome();
        this.applicationName = ejbComponentCreateService.getApplicationName();
        this.earApplicationName = ejbComponentCreateService.getEarApplicationName();
        this.distinctName = ejbComponentCreateService.getDistinctName();
        this.policyContextID = ejbComponentCreateService.getPolicyContextID();
        this.moduleName = ejbComponentCreateService.getModuleName();
        this.ejbObjectViewServiceName = ejbComponentCreateService.getEjbObject();
        this.ejbLocalObjectViewServiceName = ejbComponentCreateService.getEjbLocalObject();

        this.ejbRemoteTransactionsRepository = ejbComponentCreateService.getEJBRemoteTransactionsRepository();
        this.timeoutInterceptors = Collections.unmodifiableMap(ejbComponentCreateService.getTimeoutInterceptors());
        this.transactionManager = ejbComponentCreateService.getTransactionManager();
        this.transactionSynchronizationRegistry = ejbComponentCreateService.getTransactionSynchronizationRegistry();
        this.userTransaction = ejbComponentCreateService.getUserTransaction();
        this.serverSecurityManager = ejbComponentCreateService.getServerSecurityManager();
        this.controlPoint = ejbComponentCreateService.getControlPoint();
        this.exceptionLoggingEnabled = ejbComponentCreateService.getExceptionLoggingEnabled();
    }

    protected <T> T createViewInstanceProxy(final Class<T> viewInterface, final Map<Object, Object> contextData) {
        if (viewInterface == null)
            throw EjbLogger.ROOT_LOGGER.viewInterfaceCannotBeNull();
        if (viewServices.containsKey(viewInterface.getName())) {
            final ServiceName serviceName = viewServices.get(viewInterface.getName());
            return createViewInstanceProxy(viewInterface, contextData, serviceName);
        } else {
            throw EjbLogger.ROOT_LOGGER.viewNotFound(viewInterface.getName(), this.getComponentName());
        }
    }

    protected <T> T createViewInstanceProxy(final Class<T> viewInterface, final Map<Object, Object> contextData, final ServiceName serviceName) {
        final ServiceController<?> serviceController = currentServiceContainer().getRequiredService(serviceName);
        final ComponentView view = (ComponentView) serviceController.getValue();
        final ManagedReference instance;
        try {
            if(WildFlySecurityManager.isChecking()) {
                instance = WildFlySecurityManager.doUnchecked(new PrivilegedExceptionAction<ManagedReference>() {
                    @Override
                    public ManagedReference run() throws Exception {
                        return view.createInstance(contextData);
                    }
                });
            } else {
                instance = view.createInstance(contextData);
            }
        } catch (Exception e) {
            //TODO: do we need to let the exception propagate here?
            throw new RuntimeException(e);
        }
        return viewInterface.cast(instance.getInstance());
    }

    private static ServiceContainer currentServiceContainer() {
        if(System.getSecurityManager() == null) {
            return CurrentServiceContainer.getServiceContainer();
        }
        return AccessController.doPrivileged(CurrentServiceContainer.GET_ACTION);
    }

    public ApplicationExceptionDetails getApplicationException(Class<?> exceptionClass, Method invokedMethod) {
        ApplicationExceptionDetails applicationException = this.applicationExceptions.get(exceptionClass);
        if (applicationException != null) {
            return applicationException;
        }
        // Check if the super class of the passed exception class, is an application exception.
        Class<?> superClass = exceptionClass.getSuperclass();
        while (superClass != null && !(superClass.equals(Exception.class) || superClass.equals(Object.class))) {
            applicationException = this.applicationExceptions.get(superClass);
            // check whether the "inherited" attribute is set. A subclass of an application exception
            // is an application exception only if the inherited attribute on the parent application exception
            // is set to true.
            if (applicationException != null) {
                if (applicationException.isInherited()) {
                    return applicationException;
                }
                // Once we find a super class which is an application exception,
                // we just stop there (no need to check the grand super class), irrespective of whether the "inherited"
                // is true or false
                return null; // not an application exception, so return null
            }
            // move to next super class
            superClass = superClass.getSuperclass();
        }
        // AS7-1317: examine the throws clause of the method
        // An unchecked-exception is only an application exception if annotated (or described) as such.
        // (see EJB 3.1 FR 14.2.1)
        if (RuntimeException.class.isAssignableFrom(exceptionClass) || Error.class.isAssignableFrom(exceptionClass))
            return null;
        if (invokedMethod != null) {
            final Class<?>[] exceptionTypes = invokedMethod.getExceptionTypes();
            for (Class<?> type : exceptionTypes) {
                if (type.isAssignableFrom(exceptionClass))
                    return APPLICATION_EXCEPTION;
            }
        }
        // not an application exception, so return null.
        return null;
    }

    public Principal getCallerPrincipal() {
        if(WildFlySecurityManager.isChecking()) {
            return WildFlySecurityManager.doUnchecked(getCaller);
        } else {
            return this.serverSecurityManager.getCallerPrincipal();
        }
    }

    protected TransactionAttributeType getCurrentTransactionAttribute() {

        final InterceptorContext invocation = CurrentInvocationContext.get();
        final MethodIntf methodIntf = MethodIntfHelper.of(invocation);
        return getTransactionAttributeType(methodIntf, invocation.getMethod());
    }

    public EJBHome getEJBHome() throws IllegalStateException {
        if (ejbHomeViewServiceName == null) {
            throw EjbLogger.ROOT_LOGGER.beanHomeInterfaceIsNull(getComponentName());
        }
        final ServiceController<?> serviceController = currentServiceContainer().getRequiredService(ejbHomeViewServiceName);
        final ComponentView view = (ComponentView) serviceController.getValue();
        final String locatorAppName = earApplicationName == null ? "" : earApplicationName;
        return EJBClient.createProxy(new EJBHomeLocator<EJBHome>((Class<EJBHome>) view.getViewClass(), locatorAppName, moduleName, getComponentName(), distinctName));
    }

    public Class<?> getEjbObjectType() {
        if (ejbObjectViewServiceName == null) {
            return null;
        }
        final ServiceController<?> serviceController = currentServiceContainer().getRequiredService(ejbObjectViewServiceName);
        final ComponentView view = (ComponentView) serviceController.getValue();
        return view.getViewClass();
    }

    public Class<?> getEjbLocalObjectType() {
        if (ejbLocalObjectViewServiceName == null) {
            return null;
        }
        final ServiceController<?> serviceController = currentServiceContainer().getRequiredService(ejbLocalObjectViewServiceName);
        final ComponentView view = (ComponentView) serviceController.getValue();
        return view.getViewClass();
    }

    public EJBLocalHome getEJBLocalHome() throws IllegalStateException {
        if (ejbLocalHomeViewServiceName == null) {
            throw EjbLogger.ROOT_LOGGER.beanLocalHomeInterfaceIsNull(getComponentName());
        }
        return createViewInstanceProxy(EJBLocalHome.class, Collections.emptyMap(), ejbLocalHomeViewServiceName);
    }

    public boolean getRollbackOnly() throws IllegalStateException {
        if (isBeanManagedTransaction()) {
            throw EjbLogger.ROOT_LOGGER.failToCallgetRollbackOnly();
        }
        try {
            TransactionManager tm = this.getTransactionManager();

            // The getRollbackOnly method should be used only in the context of a transaction.
            if (tm.getTransaction() == null) {
                throw EjbLogger.ROOT_LOGGER.failToCallgetRollbackOnlyOnNoneTransaction();
            }

            // EJBTHREE-805, consider an asynchronous rollback due to timeout
            // This is counter to EJB 3.1 where an asynchronous call does not inherit the transaction context!

            int status = tm.getStatus();
            if (log.isTraceEnabled()) {
                ROOT_LOGGER.trace("Current transaction status is " + status);
            }
            switch (status) {
                case Status.STATUS_COMMITTED:
                case Status.STATUS_ROLLEDBACK:
                    throw EjbLogger.ROOT_LOGGER.failToCallgetRollbackOnlyAfterTxcompleted();
                case Status.STATUS_MARKED_ROLLBACK:
                case Status.STATUS_ROLLING_BACK:
                    return true;
            }
            return false;
        } catch (SystemException se) {
            ROOT_LOGGER.getTxManagerStatusFailed(se);
            return true;
        }
    }

    public ServerSecurityManager getSecurityManager() {
        return this.serverSecurityManager;
    }

    public TimerService getTimerService() throws IllegalStateException {
        return timerService;
    }

    public TransactionAttributeType getTransactionAttributeType(final MethodIntf methodIntf, final Method method) {
        return getTransactionAttributeType(methodIntf, MethodIdentifier.getIdentifierForMethod(method));
    }

    public TransactionAttributeType getTransactionAttributeType(final MethodIntf methodIntf, final MethodIdentifier method) {
        return getTransactionAttributeType(methodIntf, method, TransactionAttributeType.REQUIRED);
    }

    public TransactionAttributeType getTransactionAttributeType(final MethodIntf methodIntf, final MethodIdentifier method, TransactionAttributeType defaultType) {
        TransactionAttributeType txAttr = txAttrs.get(new MethodTransactionAttributeKey(methodIntf, method));
        //fall back to type bean if not found
        if (txAttr == null && methodIntf != MethodIntf.BEAN) {
            txAttr = txAttrs.get(new MethodTransactionAttributeKey(MethodIntf.BEAN, method));
        }
        if (txAttr == null)
            return defaultType;
        return txAttr;
    }
    public TransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
        return this.transactionSynchronizationRegistry;
    }

    public int getTransactionTimeout(final MethodIntf methodIntf, final Method method) {
        return getTransactionTimeout(methodIntf, MethodIdentifier.getIdentifierForMethod(method));
    }

    public int getTransactionTimeout(final MethodIntf methodIntf, final MethodIdentifier method) {
        Integer txTimeout = txTimeouts.get(new MethodTransactionAttributeKey(methodIntf, method));
        if (txTimeout == null && methodIntf != MethodIntf.BEAN) {
            txTimeout = txTimeouts.get(new MethodTransactionAttributeKey(MethodIntf.BEAN, method));
        }
        if (txTimeout == null)
            return -1;

        return txTimeout;
    }

    public UserTransaction getUserTransaction() throws IllegalStateException {
        return this.userTransaction;
    }

    public boolean isBeanManagedTransaction() {
        return isBeanManagedTransaction;
    }

    public boolean isCallerInRole(final String roleName) throws IllegalStateException {
        if (WildFlySecurityManager.isChecking()) {
            return WildFlySecurityManager.doUnchecked(new PrivilegedAction<Boolean>() {
                public Boolean run() {
                    return serverSecurityManager.isCallerInRole(getComponentName(), policyContextID, securityMetaData.getSecurityRoles(), securityMetaData.getSecurityRoleLinks(), roleName);
                }
            });
        } else {
            return this.serverSecurityManager.isCallerInRole(getComponentName(), policyContextID, securityMetaData.getSecurityRoles(), securityMetaData.getSecurityRoleLinks(), roleName);
        }
    }

    public boolean isStatisticsEnabled() {
        return utilities.isStatisticsEnabled();
    }

    public Object lookup(String name) throws IllegalArgumentException {
        if (name == null) {
            throw EjbLogger.ROOT_LOGGER.jndiNameCannotBeNull();
        }
        final NamespaceContextSelector namespaceContextSelector = NamespaceContextSelector.getCurrentSelector();
        if (namespaceContextSelector == null) {
            throw EjbLogger.ROOT_LOGGER.noNamespaceContextSelectorAvailable(name);
        }
        Context jndiContext = null;
        String namespaceStrippedJndiName = name;
        // get the appropriate JNDI context and strip the lookup jndi name of the component namespace prefix
        if (name.startsWith("java:app/")) {
            jndiContext = namespaceContextSelector.getContext("app");
            namespaceStrippedJndiName = name.substring("java:app/".length());
        } else if (name.startsWith("java:module/")) {
            jndiContext = namespaceContextSelector.getContext("module");
            namespaceStrippedJndiName = name.substring("java:module/".length());
        } else if (name.startsWith("java:comp/")) {
            jndiContext = namespaceContextSelector.getContext("comp");
            namespaceStrippedJndiName = name.substring("java:comp/".length());
        } else if (!name.startsWith("java:")) { // if it *doesn't* start with java: prefix, then default it to java:comp
            jndiContext = namespaceContextSelector.getContext("comp");
            // no need to strip the name since it doesn't start with java: prefix.
            // Also prefix the "env/" to the jndi name, since a lookup without a java: namespace prefix is effectively
            // a lookup under java:comp/env/<jndi-name>
            namespaceStrippedJndiName = "env/" + name;
        } else if (name.startsWith("java:global/")) {
            // Do *not* strip the jndi name of the prefix because java:global is a global context and doesn't specifically
            // belong to the component's ENC, and hence *isn't* a component ENC relative name and has to be looked up
            // with the absolute name (including the java:global prefix)
            try {
                jndiContext = new InitialContext();
            } catch (NamingException ne) {
                throw EjbLogger.ROOT_LOGGER.failToLookupJNDI(name, ne);
            }
        } else {
            throw EjbLogger.ROOT_LOGGER.failToLookupJNDINameSpace(name);
        }
        ROOT_LOGGER.debug("Looking up " + namespaceStrippedJndiName + " in jndi context: " + jndiContext);
        try {
            return jndiContext.lookup(namespaceStrippedJndiName);
        } catch (NamingException ne) {
            throw EjbLogger.ROOT_LOGGER.failToLookupStrippedJNDI(namespaceContextSelector, jndiContext, ne);
        }
    }

    public void setRollbackOnly() throws IllegalStateException {
        if (isBeanManagedTransaction()) {
            throw EjbLogger.ROOT_LOGGER.failToCallSetRollbackOnlyOnNoneCMB();
        }
        try {
            // get the transaction manager
            TransactionManager tm = getTransactionManager();
            // check if there's a tx in progress. If not, then it's an error to call setRollbackOnly()
            if (tm.getTransaction() == null) {
                throw EjbLogger.ROOT_LOGGER.failToCallSetRollbackOnlyWithNoTx();
            }
            // set rollback
            tm.setRollbackOnly();
        } catch (SystemException se) {
            ROOT_LOGGER.setRollbackOnlyFailed(se);
        }
    }

    public EJBSecurityMetaData getSecurityMetaData() {
        return this.securityMetaData;
    }

    public Method getTimeoutMethod() {
        return timeoutMethod;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getEarApplicationName() {
        return this.earApplicationName;
    }

    public String getDistinctName() {
        return distinctName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public ServiceName getEjbLocalObjectViewServiceName() {
        return ejbLocalObjectViewServiceName;
    }

    public ServiceName getEjbLocalHomeViewServiceName() {
        return ejbLocalHomeViewServiceName;
    }

    public ServiceName getEjbHomeViewServiceName() {
        return ejbHomeViewServiceName;
    }

    public ServiceName getEjbObjectViewServiceName() {
        return ejbObjectViewServiceName;
    }

    public Map<Method, InterceptorFactory> getTimeoutInterceptors() {
        return timeoutInterceptors;
    }

    /**
     * Returns the {@link EJBRemoteTransactionsRepository} if there is at least one remote view (either
     * ejb3.x business remote, ejb2.x remote component or home view) is exposed. Else returns null.
     *
     * @return
     */
    public EJBRemoteTransactionsRepository getEjbRemoteTransactionsRepository() {
        return this.ejbRemoteTransactionsRepository;
    }

    public AllowedMethodsInformation getAllowedMethodsInformation() {
        return isBeanManagedTransaction() ? AllowedMethodsInformation.INSTANCE_BMT : AllowedMethodsInformation.INSTANCE_CMT;
    }

    public InvocationMetrics getInvocationMetrics() {
        return invocationMetrics;
    }

    public ControlPoint getControlPoint() {
        return this.controlPoint;
    }

    @Override
    public synchronized void start() {
        super.start();
        if (this.controlPoint != null) {
            this.controlPoint.resume();
        }
        if(this.timerService instanceof TimerServiceImpl) {
            ((TimerServiceImpl) this.timerService).activate();
        }
    }

    @Override
    public final void stop() {
        if (this.controlPoint != null) {
            this.controlPoint.pause(this);
        }
        if(this.timerService instanceof TimerServiceImpl) {
            ((TimerServiceImpl) this.timerService).deactivate();
        }
        this.done();
    }

    @Override
    public void done() {
        super.stop();
    }


    public boolean isExceptionLoggingEnabled() {
        return exceptionLoggingEnabled.get();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4601.java