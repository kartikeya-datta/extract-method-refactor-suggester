error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12574.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12574.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12574.java
text:
```scala
i@@f (ejbMethodSecurityMetaData.isDenyAll()) {

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

package org.jboss.as.ejb3.security;

import java.lang.reflect.Method;
import java.util.Collection;

import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ejb3.component.EJBComponent;
import org.jboss.as.security.service.SimpleSecurityManager;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;
import static org.jboss.as.ejb3.EjbMessages.MESSAGES;
/**
 * EJB authorization interceptor responsible for handling invocation on EJB methods and doing the necessary authorization
 * checks on the invoked method.
 * <p/>
 * User: Jaikiran Pai
 */
public class AuthorizationInterceptor implements Interceptor {

    /**
     * EJB method security metadata
     */
    private final EJBMethodSecurityMetaData ejbMethodSecurityMetaData;

    /**
     * The view class name to which this interceptor is applicable
     */
    private final String viewClassName;

    /**
     * The view method to which this interceptor is applicable
     */
    private final Method viewMethod;

    public AuthorizationInterceptor(final EJBMethodSecurityMetaData ejbMethodSecurityMetaData, final String viewClassName, final Method viewMethod) {
        if (ejbMethodSecurityMetaData == null) {
            throw MESSAGES.ejbMethodSecurityMetaDataIsNull();
        }
        if (viewClassName == null || viewClassName.trim().isEmpty()) {
            throw MESSAGES.viewClassNameIsNull();
        }
        if (viewMethod == null) {
            throw MESSAGES.viewMethodIsNull();
        }
        this.ejbMethodSecurityMetaData = ejbMethodSecurityMetaData;
        this.viewClassName = viewClassName;
        this.viewMethod = viewMethod;
    }

    @Override
    public Object processInvocation(InterceptorContext context) throws Exception {
        final Component component = context.getPrivateData(Component.class);
        if (component instanceof EJBComponent == false) {
            throw MESSAGES.unexpectedComponent(component,EJBComponent.class);
        }
        final Method invokedMethod = context.getMethod();
        final ComponentView componentView = context.getPrivateData(ComponentView.class);
        final String viewClassOfInvokedMethod = componentView.getViewClass().getName();
        // shouldn't really happen if the interceptor was setup correctly. But let's be safe and do a check
        if (!this.viewClassName.equals(viewClassOfInvokedMethod) || !this.viewMethod.equals(invokedMethod)) {
            throw MESSAGES.failProcessInvocation(this.getClass().getName(), invokedMethod,viewClassOfInvokedMethod, viewMethod, viewClassName);
        }
        final EJBComponent ejbComponent = (EJBComponent) component;
        // check @DenyAll/exclude-list
        if (ejbMethodSecurityMetaData.isAccessDenied()) {
            throw MESSAGES.invocationOfMethodNotAllowed(invokedMethod,ejbComponent.getComponentName());
        }
        // If @PermitAll isn't applicable for the method then check the allowed roles
        if (!ejbMethodSecurityMetaData.isPermitAll()) {
            // get allowed roles (if any) for this method invocation
            final Collection<String> allowedRoles = ejbMethodSecurityMetaData.getRolesAllowed();
            if (!allowedRoles.isEmpty()) {
                // call the security API to do authorization check
                final SimpleSecurityManager securityManager = ejbComponent.getSecurityManager();
                if (!securityManager.isCallerInRole(ejbComponent.getSecurityMetaData().getSecurityRoles(), allowedRoles.toArray(new String[allowedRoles.size()]))) {
                    throw MESSAGES.invocationOfMethodNotAllowed(invokedMethod,ejbComponent.getComponentName());
                }
            }
        }
        // successful authorization, let the invocation proceed
        return context.proceed();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12574.java