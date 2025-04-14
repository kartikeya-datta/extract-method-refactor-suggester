error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10091.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10091.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10091.java
text:
```scala
E@@jbLogger.ROOT_LOGGER.invalidTransactionTypeForSfsbLifecycleMethod(txAttr, methodIdentifier);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.ejb3.tx;

import javax.ejb.TransactionAttributeType;

import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentInterceptorFactory;
import org.jboss.as.ejb3.logging.EjbLogger;
import org.jboss.as.ejb3.component.EJBComponent;
import org.jboss.as.ejb3.component.MethodIntf;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;
import org.jboss.invocation.InterceptorFactoryContext;
import org.jboss.invocation.proxy.MethodIdentifier;

/**
 * Transaction interceptor for Singleton and Stateless beans,
 *
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class LifecycleCMTTxInterceptor extends CMTTxInterceptor implements Interceptor {

    private final TransactionAttributeType transactionAttributeType;
    private final int transactionTimeout;

    public LifecycleCMTTxInterceptor(final TransactionAttributeType transactionAttributeType, final int transactionTimeout) {
        this.transactionAttributeType = transactionAttributeType;
        this.transactionTimeout = transactionTimeout;
    }


    @Override
    public Object processInvocation(InterceptorContext invocation) throws Exception {
        final EJBComponent component = (EJBComponent) invocation.getPrivateData(Component.class);

        switch (transactionAttributeType) {
            case MANDATORY:
                return mandatory(invocation, component);
            case NEVER:
                return never(invocation, component);
            case NOT_SUPPORTED:
                return notSupported(invocation, component);
            case REQUIRED:
                return required(invocation, component, transactionTimeout);
            case REQUIRES_NEW:
                return requiresNew(invocation, component, transactionTimeout);
            case SUPPORTS:
                return supports(invocation, component);
            default:
                throw EjbLogger.ROOT_LOGGER.unknownTxAttributeOnInvocation(transactionAttributeType, invocation);
        }
    }

    /**
     * @author Stuart Douglas
     */
    public static class Factory extends ComponentInterceptorFactory {

        private final MethodIdentifier methodIdentifier;
        /**
         * If this is false it means the bean is a SFSB, and can only be NOT_SUPPORTED or REQUIRES_NEW
         */
        private final boolean treatRequiredAsRequiresNew;

        public Factory(final MethodIdentifier methodIdentifier, final boolean treatRequiredAsRequiresNew) {
            this.methodIdentifier = methodIdentifier;
            this.treatRequiredAsRequiresNew = treatRequiredAsRequiresNew;
        }

        @Override
        protected Interceptor create(Component component, InterceptorFactoryContext context) {
            final EJBComponent ejb = (EJBComponent) component;
            TransactionAttributeType txAttr;
            if (methodIdentifier == null) {
                if(treatRequiredAsRequiresNew) {
                    txAttr = TransactionAttributeType.REQUIRED;
                } else {
                    //for stateful beans we default to NOT_SUPPORTED
                    txAttr = TransactionAttributeType.NOT_SUPPORTED;
                }
            } else {
                txAttr = ejb.getTransactionAttributeType(MethodIntf.BEAN, methodIdentifier, treatRequiredAsRequiresNew ? TransactionAttributeType.REQUIRES_NEW : TransactionAttributeType.NOT_SUPPORTED);
            }
            final int txTimeout;
            if(methodIdentifier == null) {
                txTimeout = -1;
            } else {
                txTimeout = ejb.getTransactionTimeout(MethodIntf.BEAN, methodIdentifier);
            }
            if (treatRequiredAsRequiresNew && txAttr == TransactionAttributeType.REQUIRED) {
                txAttr = TransactionAttributeType.REQUIRES_NEW;
            }
            if(!treatRequiredAsRequiresNew ) {
                if(txAttr != TransactionAttributeType.NOT_SUPPORTED &&
                        txAttr != TransactionAttributeType.REQUIRES_NEW) {
                    EjbLogger.ROOT_LOGGER.invalidTransactionTypeForSfsbLifecycleMethod(txAttr, methodIdentifier, ejb.getComponentClass());
                    txAttr = TransactionAttributeType.NOT_SUPPORTED;
                }
            }
            final LifecycleCMTTxInterceptor interceptor = new LifecycleCMTTxInterceptor(txAttr, txTimeout);
            return interceptor;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10091.java