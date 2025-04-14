error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5917.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5917.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5917.java
text:
```scala
i@@f(marker != null && !marker.isFirstInvocation()) {

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

package org.jboss.as.ejb3.component.stateful;

import java.lang.reflect.Method;

import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ejb3.EjbMessages;
import org.jboss.as.ejb3.component.EJBComponent;
import org.jboss.as.ejb3.component.Ejb2xViewType;
import org.jboss.ejb.client.SessionID;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;

import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

/**
 * An interceptor which handles a invocation on a {@link javax.ejb.Remove} method of a stateful session bean. This interceptor
 * removes the stateful session once the method completes (either successfully or with an exception). If the remove method
 * was marked with "retainIfException" to true and if the remove method threw a {@link javax.ejb.ApplicationException} then
 * this interceptor does *not* remove the stateful session.
 * <p/>
 * User: Jaikiran Pai
 */
public class StatefulRemoveInterceptor implements Interceptor {

    private final boolean retainIfException;

    public StatefulRemoveInterceptor(final boolean retainIfException) {
        this.retainIfException = retainIfException;
    }

    @Override
    public Object processInvocation(InterceptorContext context) throws Exception {
        final Component component = context.getPrivateData(Component.class);

        //if a session bean is participating in a transaction, it
        //is an error for a client to invoke the remove method
        //on the session object's home or component interface.
         final ComponentView view = context.getPrivateData(ComponentView.class);
        if (view != null) {
            Ejb2xViewType viewType = view.getPrivateData(Ejb2xViewType.class);
            if (viewType != null) {
                //this means it is an EJB 2.x view
                //which is not allowed to remove while enrolled in a TX
                final StatefulTransactionMarker marker = context.getPrivateData(StatefulTransactionMarker.class);
                if(!marker.isFirstInvocation()) {
                    throw EjbMessages.MESSAGES.cannotRemoveWhileParticipatingInTransaction();
                }
            }
        }
        // just log a WARN and throw back the original exception
        if (component instanceof StatefulSessionComponent == false) {
            throw MESSAGES.unexpectedComponent(component, StatefulSessionComponent.class);
        }
        final StatefulSessionComponent statefulComponent = (StatefulSessionComponent) component;
        Object invocationResult = null;
        try {
            // proceed
            invocationResult = context.proceed();
        } catch (Exception e) {
            // Exception caught during call to @Remove method. Handle it appropriately

            // If it's an application exception and if the @Remove method has set "retainIfException" to true
            // then just throw back the exception and don't remove the session instance.
            if (this.isApplicationException(statefulComponent, e.getClass(), context.getMethod()) && this.retainIfException) {
                throw e;
            }
            // otherwise, just remove it and throw back the original exception
            final StatefulSessionComponentInstance statefulComponentInstance = (StatefulSessionComponentInstance) context.getPrivateData(ComponentInstance.class);
            final SessionID sessionId = statefulComponentInstance.getId();
            statefulComponent.removeSession(sessionId);
            throw e;
        }
        final StatefulSessionComponentInstance statefulComponentInstance = (StatefulSessionComponentInstance) context.getPrivateData(ComponentInstance.class);
        final SessionID sessionId = statefulComponentInstance.getId();
        // just remove the session because of a call to @Remove method
        statefulComponent.removeSession(sessionId);
        // return the invocation result
        return invocationResult;
    }

    /**
     * Returns true if the passed <code>exceptionClass</code> is an application exception. Else returns false.
     *
     * @param ejbComponent   The EJB component
     * @param exceptionClass The exception class
     * @return
     */
    private boolean isApplicationException(final EJBComponent ejbComponent, final Class<?> exceptionClass, final Method invokedMethod) {
        return ejbComponent.getApplicationException(exceptionClass, invokedMethod) != null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5917.java