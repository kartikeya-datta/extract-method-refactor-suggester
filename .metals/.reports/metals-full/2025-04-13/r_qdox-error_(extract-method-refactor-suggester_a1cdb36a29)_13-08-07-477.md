error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13132.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13132.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13132.java
text:
```scala
i@@f (context.getPrimaryKeyUnchecked() != null && store.isStoreRequired(context)) {

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

package org.jboss.as.cmp.component;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;

import org.jboss.as.cmp.CmpMessages;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.as.cmp.jdbc.JDBCEntityPersistenceStore;
import org.jboss.as.cmp.jdbc.bridge.CMRMessage;
import org.jboss.as.ee.component.BasicComponent;
import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.as.ee.component.interceptors.InvocationType;
import org.jboss.as.ejb3.component.entity.EntityBeanComponentInstance;
import org.jboss.as.ejb3.component.entity.WrappedRemoteException;
import org.jboss.as.ejb3.component.entity.interceptors.InternalInvocationMarker;
import org.jboss.as.naming.ManagedReference;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;

/**
 * @author John Bailey
 */
public class CmpEntityBeanComponentInstance extends EntityBeanComponentInstance {
    private final Interceptor relationshipInterceptor;

    CmpEntityBeanComponentInstance(final BasicComponent component, final AtomicReference<ManagedReference> instanceReference, final Interceptor preDestroyInterceptor, Map<Method, Interceptor> methodInterceptors, final Interceptor relationshipInterceptor) {
        super(component, instanceReference, preDestroyInterceptor, methodInterceptors);
        this.relationshipInterceptor = relationshipInterceptor;
    }

    public CmpEntityBeanComponent getComponent() {
        return (CmpEntityBeanComponent) super.getComponent();
    }

    public CmpEntityBeanContext getEjbContext() {
        return (CmpEntityBeanContext) super.getEjbContext();
    }

    @Override
    public void setupContext(final InterceptorContext interceptorContext) {
        final InvocationType invocationType = interceptorContext.getPrivateData(InvocationType.class);
        try {
            interceptorContext.putPrivateData(InvocationType.class, InvocationType.SET_ENTITY_CONTEXT);
            final CmpEntityBeanContext context = new CmpEntityBeanContext(this);
            setEjbContext(context);
            getInstance().setEntityContext(context);
            getComponent().getStoreManager().activateEntity(context);
        } catch (RemoteException e) {
            throw new WrappedRemoteException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            interceptorContext.putPrivateData(InvocationType.class, invocationType);
        }
    }

    public EntityBean getInstance() {
        final EntityBean instance = super.getInstance();
        if (instance instanceof CmpProxy) {
            CmpProxy.class.cast(instance).setComponentInstance(this);
        }
        return instance;
    }

    @Override
    public boolean isReloadRequired() {
        return !getEjbContext().isValid();
    }

    @Override
    public void setReloadRequired(final boolean reloadRequired) {
        getEjbContext().setValid(!reloadRequired);
    }

    @Override
    public void reload() {
        try {
            final CmpEntityBeanContext entityContext = getEjbContext();
            getComponent().getStoreManager().loadEntity(entityContext);
            entityContext.setValid(true);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void store() {
        try {
            if (!isRemoved()) {

                invokeEjbStore();

                final CmpEntityBeanContext context = getEjbContext();
                final JDBCEntityPersistenceStore store = getComponent().getStoreManager();
                if (context.getPrimaryKey() != null && store.isStoreRequired(context)) {
                    store.storeEntity(context);
                }
            }
        } catch (RemoteException e) {
            throw new WrappedRemoteException(e);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    public void passivate() {
        final JDBCEntityPersistenceStore store = getComponent().getStoreManager();
        try {
            store.passivateEntity(this.getEjbContext());
            clearPrimaryKey();
            setRemoved(false);
        } catch (RemoteException e) {
            throw new WrappedRemoteException(e);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    Object invoke(final CMRMessage message, final Object... params) {
        final InterceptorContext interceptorContext = new InterceptorContext();
        interceptorContext.setParameters(params);
        interceptorContext.putPrivateData(Component.class, getComponent());
        interceptorContext.putPrivateData(ComponentInstance.class, this);
        interceptorContext.putPrivateData(CMRMessage.class, message);
        interceptorContext.putPrivateData(InternalInvocationMarker.class, InternalInvocationMarker.INSTANCE);

        try {
            return relationshipInterceptor.processInvocation(interceptorContext);
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw CmpMessages.MESSAGES.failedToInvokeRelationshipRequest(e);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13132.java