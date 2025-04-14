error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14743.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14743.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14743.java
text:
```scala
final O@@bject existing = context.getContextData().get(EntityBeanComponent.PRIMARY_KEY_CONTEXT_KEY);

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

package org.jboss.as.cmp.component.interceptors;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionSynchronizationRegistry;
import org.jboss.as.cmp.TransactionEntityMap;
import org.jboss.as.cmp.component.CmpEntityBeanComponent;
import org.jboss.as.cmp.component.CmpEntityBeanComponentInstance;
import org.jboss.as.cmp.jdbc.JDBCEntityPersistenceStore;
import org.jboss.as.ee.component.Component;
import org.jboss.as.ejb3.component.entity.EntityBeanComponent;
import org.jboss.as.ejb3.component.entity.interceptors.EntityBeanEjbCreateMethodInterceptorFactory;
import org.jboss.as.ejb3.component.entity.interceptors.EntityBeanHomeCreateInterceptorFactory;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.InterceptorFactoryContext;
import org.jboss.tm.TxUtils;

/**
 * @author John Bailey
 */
public class CmpEntityBeanEjbCreateMethodInterceptorFactory implements InterceptorFactory {

    public static final CmpEntityBeanEjbCreateMethodInterceptorFactory INSTANCE = new CmpEntityBeanEjbCreateMethodInterceptorFactory();

    private CmpEntityBeanEjbCreateMethodInterceptorFactory() {
    }

    public Interceptor create(InterceptorFactoryContext context) {
        final Object existing = context.getContextData().get(EntityBeanEjbCreateMethodInterceptorFactory.EXISTING_ID_CONTEXT_KEY);

        final AtomicReference<Object> primaryKeyReference = new AtomicReference<Object>();
        context.getContextData().put(EntityBeanComponent.PRIMARY_KEY_CONTEXT_KEY, primaryKeyReference);

        final Method ejbCreate = (Method) context.getContextData().get(EntityBeanHomeCreateInterceptorFactory.EJB_CREATE_METHOD_KEY);
        final Method ejbPostCreate = (Method) context.getContextData().get(EntityBeanHomeCreateInterceptorFactory.EJB_POST_CREATE_METHOD_KEY);
        final Object[] params = (Object[]) context.getContextData().get(EntityBeanHomeCreateInterceptorFactory.PARAMETERS_KEY);

        return new Interceptor() {
            public Object processInvocation(final InterceptorContext context) throws Exception {

                if (existing != null) {
                    primaryKeyReference.set(existing);
                    return context.proceed();
                }

                final Component component = context.getPrivateData(Component.class);
                if (!(component instanceof CmpEntityBeanComponent)) {
                    throw new IllegalStateException("Unexpected component: " + component + " Expected " + CmpEntityBeanComponent.class);
                }
                final CmpEntityBeanComponent entityBeanComponent = (CmpEntityBeanComponent) component;
                //grab an unasociated entity bean from the pool
                final CmpEntityBeanComponentInstance instance = (CmpEntityBeanComponentInstance) entityBeanComponent.getPool().get();
                final JDBCEntityPersistenceStore storeManager = entityBeanComponent.getStoreManager();

                //call the ejbCreate method
                try {
                    storeManager.initEntity(instance.getEntityContext());
                    ejbCreate.invoke(instance.getInstance(), params);

                    final Object primaryKey = storeManager.createEntity(context.getMethod(), context.getParameters(), instance.getEntityContext());
                    instance.associate(primaryKey);

                    //now add the instance to the cache, so it is usable
                    //note that we do not release it back to the pool
                    //the cache will do that when it is expired or removed
                    entityBeanComponent.getCache().create(instance);

                    storeManager.postCreateEntity(context.getMethod(), context.getParameters(), instance.getEntityContext());

                    ejbPostCreate.invoke(instance.getInstance(), params);
                    primaryKeyReference.set(primaryKey);

                    if (((CmpEntityBeanComponent) component).getStoreManager().getCmpConfig().isInsertAfterEjbPostCreate()) {
                        storeManager.createEntity(context.getMethod(), context.getParameters(), instance.getEntityContext());
                    }

                    final Transaction transaction = entityBeanComponent.getTransactionManager().getTransaction();
                    if (TxUtils.isActive(transaction)) {
                        TransactionEntityMap.NONE.scheduleSync(transaction, instance.getEntityContext());
                    }
                } catch (DuplicateKeyException e) {
                    throw e;
                } catch (Exception e) {
                    throw new EJBException(e);
                } catch (Throwable t) {
                    final EJBException ex = new EJBException("Failed to create entity - " + component.getComponentClass());
                    ex.initCause(t);
                    throw ex;
                }

                //if a transaction is active we register a sync
                //and if the transaction is rolled back we release the instance back into the pool

                final TransactionSynchronizationRegistry transactionSynchronizationRegistry = entityBeanComponent.getTransactionSynchronizationRegistry();
                if (transactionSynchronizationRegistry.getTransactionKey() != null) {
                    transactionSynchronizationRegistry.registerInterposedSynchronization(new Synchronization() {
                        public void beforeCompletion() {

                        }

                        public void afterCompletion(final int status) {
                            if (status != Status.STATUS_COMMITTED) {
                                //if the transaction is rolled back we release the instance back into the pool
                                //entityBeanComponent.getPool().release(instance);
                            }
                        }
                    });
                }
                return context.proceed();
            }
        };

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14743.java