error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/240.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/240.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/240.java
text:
```scala
l@@ogger.debug("Unable to find opname '" + opName + "' valid operations:" + methodInvokerMap.keySet());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.ejb3.iiop;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBMetaData;
import javax.ejb.HomeHandle;
import javax.management.MBeanException;
import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ejb3.component.entity.EntityBeanComponent;
import org.jboss.as.ejb3.component.stateful.StatefulSessionComponent;
import org.jboss.as.jacorb.csiv2.idl.SASCurrent;
import org.jboss.as.jacorb.rmi.RmiIdlUtil;
import org.jboss.as.jacorb.rmi.marshal.strategy.SkeletonStrategy;
import org.jboss.as.naming.context.NamespaceContextSelector;
import org.jboss.ejb.client.SessionID;
import org.jboss.ejb.iiop.HandleImplIIOP;
import org.jboss.invocation.InterceptorContext;
import org.jboss.logging.Logger;
import org.jboss.marshalling.InputStreamByteInput;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SecurityContextFactory;
import org.jboss.security.SimplePrincipal;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.InterfaceDef;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableServer.Current;
import org.omg.PortableServer.CurrentPackage.NoContext;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.wildfly.security.manager.WildFlySecurityManager;

/**
 * CORBA servant class for the <code>EJBObject</code>s of a given bean. An
 * instance of this class "implements" the bean's set of <code>EJBObject</code>
 * instances by forwarding to the bean container all IIOP invocations on any
 * of the bean's <code>EJBObject</code>s.
 *
 * @author <a href="mailto:reverbel@ime.usp.br">Francisco Reverbel</a>
 * @author Stuart Douglas
 */
public class EjbCorbaServant extends Servant implements InvokeHandler, LocalIIOPInvoker {

    private static final Logger logger = Logger.getLogger(EjbCorbaServant.class);

    /**
     * The injected component view
     */
    private final ComponentView componentView;

    /**
     * The ORB
     */
    private final ORB orb;

    /**
     * Thread-local <code>Current</code> object from which we get the target oid
     * in an incoming IIOP request.
     */
    private final Current poaCurrent;

    /**
     * Mapping from bean methods to <code>SkeletonStrategy</code> instances.
     */
    private final Map<String, SkeletonStrategy> methodInvokerMap;

    /**
     * CORBA repository ids of the RMI-IDL interfaces implemented by the bean
     * (<code>EJBObject</code> instance).
     */
    private final String[] repositoryIds;

    /**
     * CORBA reference to an IR object representing the bean's remote interface.
     */
    private final InterfaceDef interfaceDef;

    /**
     * The security domain for CORBA invocations
     */
    private final String securityDomain;

    /**
     * If true this is the servant for an EJBHome object
     */
    private final boolean home;

    /**
     * <code>HomeHandle</code> for the <code>EJBHome</code>
     * implemented by this servant.
     */
    private volatile HomeHandle homeHandle = null;

    /**
     * The metadata for this
     */
    private volatile EJBMetaData ejbMetaData;

    /**
     * A reference to the SASCurrent, or null if the SAS interceptors are not
     * installed.
     */
    private final SASCurrent sasCurrent;

    /**
     * A reference to the InboundTransactionCurrent, or null if OTS interceptors
     * are not installed.
     */
    private final org.jboss.iiop.tm.InboundTransactionCurrent inboundTxCurrent;

    /**
     * The transaction manager
     */
    private final TransactionManager transactionManager;

    /**
     * Used for serializing EJB id's
     */
    private final MarshallerFactory factory;
    private final MarshallingConfiguration configuration;

    /**
     * The EJB's deployment class loader
     */
    private final ClassLoader classLoader;

    /**
     * Constructs an <code>EjbObjectCorbaServant></code>.
     */
    public EjbCorbaServant(final Current poaCurrent, final Map<String, SkeletonStrategy> methodInvokerMap, final String[] repositoryIds,
                           final InterfaceDef interfaceDef, final ORB orb, final ComponentView componentView, final MarshallerFactory factory, final MarshallingConfiguration configuration, final TransactionManager transactionManager, final ClassLoader classLoader, final boolean home, final String securityDomain) {
        this.poaCurrent = poaCurrent;
        this.methodInvokerMap = methodInvokerMap;
        this.repositoryIds = repositoryIds;
        this.interfaceDef = interfaceDef;
        this.orb = orb;
        this.componentView = componentView;
        this.factory = factory;
        this.configuration = configuration;
        this.transactionManager = transactionManager;
        this.classLoader = classLoader;
        this.home = home;
        this.securityDomain = securityDomain;

        SASCurrent sasCurrent;
        try {
            sasCurrent = (SASCurrent) this.orb.resolve_initial_references("SASCurrent");
        } catch (InvalidName invalidName) {
            sasCurrent = null;
        }
        this.sasCurrent = sasCurrent;
        org.jboss.iiop.tm.InboundTransactionCurrent inboundTxCurrent;
        try {
            inboundTxCurrent = (org.jboss.iiop.tm.InboundTransactionCurrent) this.orb.resolve_initial_references(org.jboss.iiop.tm.InboundTransactionCurrent.NAME);
        } catch (InvalidName invalidName) {
            inboundTxCurrent = null;
        }
        this.inboundTxCurrent = inboundTxCurrent;
    }


    /**
     * Returns an IR object describing the bean's remote interface.
     */
    public org.omg.CORBA.Object _get_interface_def() {
        if (interfaceDef != null)
            return interfaceDef;
        else
            return super._get_interface_def();
    }

    /**
     * Returns an array with the CORBA repository ids of the RMI-IDL interfaces
     * implemented by this servant's <code>EJBObject</code>s.
     */
    public String[] _all_interfaces(POA poa, byte[] objectId) {
        return repositoryIds.clone();
    }

    /**
     * Receives IIOP requests to this servant's <code>EJBObject</code>s
     * and forwards them to the bean container, through the JBoss
     * <code>MBean</code> server.
     */
    public OutputStream _invoke(final String opName, final InputStream in, final ResponseHandler handler) {

        if (logger.isTraceEnabled()) {
            logger.trace("EJBObject invocation: " + opName);
        }

        SkeletonStrategy op = methodInvokerMap.get(opName);
        if (op == null) {
            logger.debugf("Unable to find opname '%s' valid operations:%s", opName, methodInvokerMap.keySet());
            throw new BAD_OPERATION(opName);
        }
        final NamespaceContextSelector selector = componentView.getComponent().getNamespaceContextSelector();
        final ClassLoader oldCl = WildFlySecurityManager.getCurrentContextClassLoaderPrivileged();
        NamespaceContextSelector.pushCurrentSelector(selector);
        try {
            WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(classLoader);
            SecurityContext sc = null;
            org.omg.CORBA_2_3.portable.OutputStream out;
            try {
                Object retVal;

                if (!home && opName.equals("_get_handle")) {
                    retVal = new HandleImplIIOP(orb.object_to_string(_this_object()));
                } else if (home && opName.equals("_get_homeHandle")) {
                    retVal = homeHandle;
                } else if (home && opName.equals("_get_EJBMetaData")) {
                    retVal = ejbMetaData;
                } else {
                    Transaction tx = null;
                    if (inboundTxCurrent != null)
                        tx = inboundTxCurrent.getCurrentTransaction();
                    if (tx != null) {
                        transactionManager.resume(tx);
                    }
                    try {
                        SimplePrincipal principal = null;
                        Object credential = null;

                        if (sasCurrent != null) {
                            final byte[] incomingName = sasCurrent.get_incoming_principal_name();

                            if ( incomingName != null && incomingName.length > 0) {
                                //we have an identity token, which is a trust based mechanism
                                if (incomingName.length > 0) {
                                    String name = new String(incomingName, StandardCharsets.UTF_8);
                                    int domainIndex = name.indexOf('@');
                                    if (domainIndex > 0)
                                        name = name.substring(0, domainIndex);
                                    principal = new SimplePrincipal(name);
                                    //we don't have any real way to establish trust here
                                    //we just use the SASCurrent as a credential, and a custom login
                                    //module can make a decision for us.
                                    credential = sasCurrent;
                                }
                            } else {
                                //the client has just sent a username and password
                                final byte[] username = sasCurrent.get_incoming_username();
                                final byte[] incomingPassword = sasCurrent.get_incoming_password();
                                if(username.length > 0) {
                                    String name = new String(username, StandardCharsets.UTF_8);
                                    int domainIndex = name.indexOf('@');
                                    if (domainIndex > 0) {
                                        name = name.substring(0, domainIndex);
                                    }
                                    principal = new SimplePrincipal(name);
                                    credential = new String(incomingPassword, StandardCharsets.UTF_8).toCharArray();
                                }
                            }

                            if (securityDomain != null) {
                                sc = SecurityContextFactory.createSecurityContext(securityDomain);
                                sc.getUtil().createSubjectInfo(principal, credential, null);
                            }
                        }
                        final Object[] params = op.readParams((org.omg.CORBA_2_3.portable.InputStream) in);

                        if (!home && opName.equals("isIdentical") && params.length == 1) {
                            //handle isIdentical specially
                            Object val = params[0];
                            if (val instanceof org.omg.CORBA.Object) {
                                retVal = handleIsIdentical((org.omg.CORBA.Object) val);
                            } else {
                                retVal = false;
                            }
                        } else {

                            if (sc != null) {
                                setSecurityContextOnAssociation(sc);
                            }
                            try {
                                final InterceptorContext interceptorContext = new InterceptorContext();

                                if (sc != null) {
                                    interceptorContext.putPrivateData(SecurityContext.class, sc);
                                }
                                prepareInterceptorContext(op, params, interceptorContext);
                                retVal = componentView.invoke(interceptorContext);
                            } finally {
                                if (sc != null) {
                                    clearSecurityContextOnAssociation();
                                }
                            }
                        }
                    } finally {
                        if (tx != null) {
                            if (transactionManager.getStatus() != Status.STATUS_NO_TRANSACTION) {
                                transactionManager.suspend();
                            }
                        }
                    }

                }
                out = (org.omg.CORBA_2_3.portable.OutputStream)
                        handler.createReply();
                if (op.isNonVoid()) {
                    op.writeRetval(out, retVal);
                }
            } catch (Exception e) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Exception in EJBObject invocation", e);
                }
                if (e instanceof MBeanException) {
                    e = ((MBeanException) e).getTargetException();
                }
                RmiIdlUtil.rethrowIfCorbaSystemException(e);
                out = (org.omg.CORBA_2_3.portable.OutputStream)
                        handler.createExceptionReply();
                op.writeException(out, e);
            }
            return out;
        } finally {
            NamespaceContextSelector.popCurrentSelector();
            WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(oldCl);
        }
    }

    private void prepareInterceptorContext(final SkeletonStrategy op, final Object[] params, final InterceptorContext interceptorContext) throws IOException, ClassNotFoundException {
        if (!home) {
            if (componentView.getComponent() instanceof StatefulSessionComponent) {
                final SessionID sessionID = (SessionID) unmarshalIdentifier();
                interceptorContext.putPrivateData(SessionID.class, sessionID);
            } else if (componentView.getComponent() instanceof EntityBeanComponent) {
                final Object pk = unmarshalIdentifier();
                interceptorContext.putPrivateData(EntityBeanComponent.PRIMARY_KEY_CONTEXT_KEY, pk);
            }
        }
        interceptorContext.setContextData(new HashMap<String, Object>());
        interceptorContext.setParameters(params);
        interceptorContext.setMethod(op.getMethod());
        interceptorContext.putPrivateData(ComponentView.class, componentView);
        interceptorContext.putPrivateData(Component.class, componentView.getComponent());
    }

    private boolean handleIsIdentical(final org.omg.CORBA.Object val) throws RemoteException {
        //TODO: is this correct?
        return orb.object_to_string(_this_object()).equals(orb.object_to_string(val));
    }

    private Object unmarshalIdentifier() throws IOException, ClassNotFoundException {
        final Object id;
        try {
            final byte[] idData = poaCurrent.get_object_id();
            final Unmarshaller unmarshaller = factory.createUnmarshaller(configuration);
            unmarshaller.start(new InputStreamByteInput(new ByteArrayInputStream(idData)));
            id = unmarshaller.readObject();
            unmarshaller.finish();
        } catch (NoContext noContext) {
            throw new RuntimeException(noContext);
        }
        return id;
    }

    // Implementation of the interface LocalIIOPInvoker ------------------------

    /**
     * Receives intra-VM invocations on this servant's <code>EJBObject</code>s
     * and forwards them to the bean container, through the JBoss
     * <code>MBean</code>
     * server.
     */
    public Object invoke(String opName,
                         Object[] arguments,
                         Transaction tx,
                         Principal identity,
                         Object credential)
            throws Exception {
        if (logger.isTraceEnabled()) {
            logger.trace("EJBObject local invocation: " + opName);
        }

        SkeletonStrategy op = methodInvokerMap.get(opName);
        if (op == null) {
            throw new BAD_OPERATION(opName);
        }
        if (tx != null) {
            transactionManager.resume(tx);
        }
        try {
            final InterceptorContext interceptorContext = new InterceptorContext();
            prepareInterceptorContext(op, arguments, interceptorContext);
            return componentView.invoke(interceptorContext);
        } finally {
            if (tx != null) {
                if (transactionManager.getStatus() != Status.STATUS_NO_TRANSACTION) {
                    transactionManager.suspend();
                }
            }
        }

    }

    public void setHomeHandle(final HomeHandle homeHandle) {
        this.homeHandle = homeHandle;
    }

    public void setEjbMetaData(final EJBMetaData ejbMetaData) {
        this.ejbMetaData = ejbMetaData;
    }


    private static void setSecurityContextOnAssociation(final SecurityContext sc) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {

            @Override
            public Void run() {
                SecurityContextAssociation.setSecurityContext(sc);
                return null;
            }
        });
    }

    private static void clearSecurityContextOnAssociation() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {

            @Override
            public Void run() {
                SecurityContextAssociation.clearSecurityContext();
                return null;
            }
        });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/240.java