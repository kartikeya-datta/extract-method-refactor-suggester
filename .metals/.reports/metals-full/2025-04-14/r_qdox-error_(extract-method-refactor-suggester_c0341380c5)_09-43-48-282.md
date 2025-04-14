error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4161.java
text:
```scala
m@@ethod.invoke(instance);

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

package org.jboss.as.ee.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.as.naming.ManagedReference;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.InterceptorFactoryContext;
import org.jboss.invocation.Interceptors;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class ManagedReferenceLifecycleMethodInterceptorFactory implements InterceptorFactory {
    private final Object contextKey;
    private final Method method;
    private final boolean changeMethod;
    private final boolean lifecycleMethod;

    /**
     * This is equivalent to calling <code>ManagedReferenceLifecycleMethodInterceptorFactory(Object, java.lang.reflect.Method, boolean, false)</code>
     *
     * @param contextKey
     * @param method       The method for which the interceptor has to be created
     * @param changeMethod True if during the interceptor processing, the {@link org.jboss.invocation.InterceptorContext#getMethod()}
     *                     is expected to return the passed <code>method</code>
     */
    ManagedReferenceLifecycleMethodInterceptorFactory(final Object contextKey, final Method method, final boolean changeMethod) {
        this(contextKey, method, changeMethod, false);
    }

    /**
     * @param contextKey
     * @param method          The method for which the interceptor has to be created
     * @param changeMethod    True if during the interceptor processing, the {@link org.jboss.invocation.InterceptorContext#getMethod()}
     *                        is expected to return the passed <code>method</code>
     * @param lifecycleMethod If the passed <code>method</code> is a lifecycle callback method. False otherwise
     */
    ManagedReferenceLifecycleMethodInterceptorFactory(final Object contextKey, final Method method, final boolean changeMethod, final boolean lifecycleMethod) {
        this.contextKey = contextKey;
        this.method = method;
        this.changeMethod = changeMethod;
        this.lifecycleMethod = lifecycleMethod;
    }

    public Interceptor create(final InterceptorFactoryContext context) {
        @SuppressWarnings("unchecked")
        final AtomicReference<ManagedReference> ref = (AtomicReference<ManagedReference>) context.getContextData().get(contextKey);
        return new ManagedReferenceLifecycleMethodInterceptor(ref, method, changeMethod, this.lifecycleMethod);
    }


    /**
     * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
     */
    static final class ManagedReferenceLifecycleMethodInterceptor implements Interceptor {

        private final AtomicReference<ManagedReference> instanceRef;
        private final Method method;
        private final boolean withContext;
        private final boolean changeMethod;
        private final boolean lifecycleMethod;

        /**
         * @param instanceRef
         * @param method          The method on which the interceptor applies
         * @param changeMethod    True if during the interceptor processing, the {@link org.jboss.invocation.InterceptorContext#getMethod()}
         *                        is expected to return the passed <code>method</code>
         * @param lifecycleMethod If the passed <code>method</code> is a lifecycle callback method. False otherwise
         */
        ManagedReferenceLifecycleMethodInterceptor(final AtomicReference<ManagedReference> instanceRef, final Method method, final boolean changeMethod, final boolean lifecycleMethod) {
            this.changeMethod = changeMethod;
            this.method = method;
            this.lifecycleMethod = lifecycleMethod;
            this.instanceRef = instanceRef;
            withContext = method.getParameterTypes().length == 1;
        }

        /**
         * {@inheritDoc}
         */
        public Object processInvocation(final InterceptorContext context) throws Exception {
            final ManagedReference reference = instanceRef.get();
            final Object instance = reference.getInstance();
            try {
                final Method method = this.method;
                if (withContext) {
                    final Method oldMethod = context.getMethod();
                    try {
                        if (this.lifecycleMethod) {
                            // because InvocationContext#getMethod() is expected to return null for lifecycle methods
                            context.setMethod(null);
                            return method.invoke(instance, context.getInvocationContext());
                        } else if (this.changeMethod) {
                            context.setMethod(method);
                            return method.invoke(instance, context.getInvocationContext());
                        } else {
                            return method.invoke(instance, context.getInvocationContext());
                        }
                    } finally {
                        // reset any changed method on the interceptor context
                        context.setMethod(oldMethod);
                    }
                } else {
                    method.invoke(instance, null);
                    return context.proceed();
                }
            } catch (IllegalAccessException e) {
                final IllegalAccessError n = new IllegalAccessError(e.getMessage());
                n.setStackTrace(e.getStackTrace());
                throw n;
            } catch (InvocationTargetException e) {
                throw Interceptors.rethrow(e.getCause());
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4161.java