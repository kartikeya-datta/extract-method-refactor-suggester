error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5030.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5030.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5030.java
text:
```scala
private final C@@lass<?> invokedBusinessInterface;

/*
 * JBoss, Home of Professional Open Source
 * Copyright (c) 2010, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.ejb3.context.base;

import org.jboss.as.ejb3.context.spi.SessionBeanComponent;
import org.jboss.as.ejb3.context.spi.SessionContext;
import org.jboss.as.ejb3.context.spi.SessionInvocationContext;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.xml.rpc.handler.MessageContext;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

/**
 * @author <a href="cdewolf@redhat.com">Carlo de Wolf</a>
 */
public abstract class BaseSessionInvocationContext extends BaseInvocationContext
        implements SessionInvocationContext {
    private Class<?> invokedBusinessInterface;

    private MessageContext messageContext;
    private Future future;

    public BaseSessionInvocationContext(Class<?> invokedBusinessInterface, Method method, Object[] parameters) {
        super(method, parameters);

        // might be null for non-EJB3 invocations & lifecycle callbacks
        this.invokedBusinessInterface = invokedBusinessInterface;
    }

    public BaseSessionInvocationContext(boolean lifecycleCallback, Class<?> invokedBusinessInterface, Method method, Object[] parameters) {
        super(lifecycleCallback, method, parameters);

        // might be null for non-EJB3 invocations & lifecycle callbacks
        this.invokedBusinessInterface = invokedBusinessInterface;
    }

    public <T> T getBusinessObject(Class<T> businessInterface) throws IllegalStateException {
        // we need an instance attached
        SessionContext ctx = getEJBContext();
        return ctx.getComponent().getBusinessObject(ctx, businessInterface);
    }

    public SessionContext getEJBContext() {
        return (SessionContext) super.getEJBContext();
    }

    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
        SessionContext ctx = getEJBContext();
        return ctx.getComponent().getEJBLocalObject(ctx);
    }

    public EJBObject getEJBObject() throws IllegalStateException {
        SessionContext ctx = getEJBContext();
        return ctx.getComponent().getEJBObject(ctx);
    }

    public Class<?> getInvokedBusinessInterface() throws IllegalStateException {
        if (invokedBusinessInterface == null)
            throw new IllegalStateException("No invoked business interface on " + this);
        return invokedBusinessInterface;
    }

    public SessionBeanComponent getComponent() {
        // for now
        return getEJBContext().getComponent();
    }

    public MessageContext getMessageContext() throws IllegalStateException {
        if (messageContext == null)
            throw new IllegalStateException("No message context on " + this);
        return messageContext;
    }

    public void setFuture(Future future) {
        this.future = future;
    }

    public void setMessageContext(MessageContext messageContext) {
        this.messageContext = messageContext;
    }

    public boolean wasCancelCalled() throws IllegalStateException {
        if (future == null)
            throw new IllegalStateException("No asynchronous invocation in progress");
        return future.isCancelled();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5030.java