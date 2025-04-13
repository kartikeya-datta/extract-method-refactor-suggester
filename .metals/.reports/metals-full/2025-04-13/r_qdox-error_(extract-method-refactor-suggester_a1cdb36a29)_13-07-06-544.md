error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5022.java
text:
```scala
private final I@@nterceptorContext context;

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
package org.jboss.as.ejb3.component.session;

import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.as.ee.component.ComponentViewInstance;
import org.jboss.as.ejb3.component.CancellationFlag;
import org.jboss.as.ejb3.context.CurrentInvocationContext;
import org.jboss.as.ejb3.context.base.BaseSessionInvocationContext;
import org.jboss.as.ejb3.context.spi.InvocationContext;
import org.jboss.as.ejb3.context.spi.SessionContext;
import org.jboss.as.ejb3.context.spi.SessionInvocationContext;
import org.jboss.as.ejb3.tx.ApplicationExceptionDetails;
import org.jboss.as.ejb3.tx.TransactionalInvocationContext;
import org.jboss.invocation.ImmediateInterceptorFactory;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;
import org.jboss.invocation.InterceptorFactory;

import javax.ejb.TransactionAttributeType;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Map;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class SessionInvocationContextInterceptor implements Interceptor {

    public static final InterceptorFactory FACTORY = new ImmediateInterceptorFactory(new SessionInvocationContextInterceptor(false));
    public static final InterceptorFactory LIFECYCLE_FACTORY = new ImmediateInterceptorFactory(new SessionInvocationContextInterceptor(true));

    private final boolean lifecycleCallback;

    private SessionInvocationContextInterceptor(final boolean lifecycleCallback) {
        this.lifecycleCallback = lifecycleCallback;
    }

    @Override
    public Object processInvocation(InterceptorContext context) throws Exception {
        final Method invokedMethod = context.getMethod();
        final ComponentViewInstance componentViewInstance = context.getPrivateData(ComponentViewInstance.class);
        // For a lifecycle interception, the ComponentViewInstance (and the invoked business interface) will be null.
        // On a normal method invocation, the invoked business interface will be obtained from the ComponentViewInstance
        final Class<?> invokedBusinessInterface = componentViewInstance == null ? null : componentViewInstance.getViewClass();
        Object[] parameters = context.getParameters();
        SessionInvocationContext sessionInvocationContext = new CustomSessionInvocationContext(lifecycleCallback, context, invokedBusinessInterface, invokedMethod, parameters);
        context.putPrivateData(InvocationContext.class, sessionInvocationContext);
        CurrentInvocationContext.push(sessionInvocationContext);
        try {
            return context.proceed();
        } finally {
            CurrentInvocationContext.pop();
            context.putPrivateData(InvocationContext.class, null);
        }
    }

    protected static class CustomSessionInvocationContext extends BaseSessionInvocationContext implements TransactionalInvocationContext {
        private InterceptorContext context;

        protected CustomSessionInvocationContext(boolean lifecycleCallback, InterceptorContext context, Class<?> invokedBusinessInterface, Method method, Object[] parameters) {
            super(lifecycleCallback, invokedBusinessInterface, method, parameters);

            this.context = context;
        }

        @Override
        public ApplicationExceptionDetails getApplicationException(Class<?> e) {
            return getComponent().getApplicationException(e, getMethod());
        }

        @Override
        public Principal getCallerPrincipal() {
            return getComponent().getCallerPrincipal();
        }

        @Override
        public SessionBeanComponent getComponent() {
            //return (SessionBeanComponent) super.getComponent();
            // this is faster
            return (SessionBeanComponent) context.getPrivateData(Component.class);
        }

        @Override
        public Map<String, Object> getContextData() {
            return context.getContextData();
        }

        @Override
        public SessionContext getEJBContext() {
            return ((SessionBeanComponentInstance) context.getPrivateData(ComponentInstance.class)).getSessionContext();
        }

        @Override
        public TransactionAttributeType getTransactionAttribute() {
            return getComponent().getTransactionAttributeType(getMethod());
        }

        @Override
        public int getTransactionTimeout() {
            return getComponent().getTransactionTimeout(getMethod());
        }

        @Override
        public Object proceed() throws Exception {
            return context.proceed();
        }

        @Override
        public void setParameters(Object[] params) throws IllegalArgumentException, IllegalStateException {
            context.setParameters(params);
        }

        public boolean wasCancelCalled() throws IllegalStateException {
            final CancellationFlag flag = context.getPrivateData(CancellationFlag.class);
            if (flag != null) {
                return flag.get();
            }
            return super.wasCancelCalled();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5022.java