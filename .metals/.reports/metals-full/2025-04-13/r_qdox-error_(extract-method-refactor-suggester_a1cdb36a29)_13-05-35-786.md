error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11545.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11545.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11545.java
text:
```scala
.@@setAllowNull(true)

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.wildfly.extension.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PersistentResourceDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.wildfly.extension.undertow.Constants;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Stuart Douglas
 */
public class ReverseProxyHandler extends Handler {

    public static final ReverseProxyHandler INSTANCE = new ReverseProxyHandler();

    public static final AttributeDefinition PROBLEM_SERVER_RETRY = new SimpleAttributeDefinitionBuilder(Constants.PROBLEM_SERVER_RETRY, ModelType.INT)
            .setAllowNull(true)
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(30))
            .build();

    public static final AttributeDefinition STICKY_SESSION_LIFETIME = new SimpleAttributeDefinitionBuilder(Constants.STICKY_SESSION_LIFETIME, ModelType.LONG)
            .setAllowNull(true)
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(120))
            .build();

    public static final AttributeDefinition SESSION_COOKIE_NAMES = new SimpleAttributeDefinitionBuilder(Constants.SESSION_COOKIE_NAMES, ModelType.STRING)
            .setAllowNull(false)
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode("JSESSIONID"))
            .build();

    public static final AttributeDefinition CONNECTIONS_PER_THREAD = new SimpleAttributeDefinitionBuilder(Constants.CONNECTIONS_PER_THREAD, ModelType.INT)
            .setAllowNull(true)
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(10))
            .build();

    public static final AttributeDefinition MAX_REQUEST_TIME = new SimpleAttributeDefinitionBuilder(Constants.MAX_REQUEST_TIME, ModelType.INT)
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    private ReverseProxyHandler() {
        super(Constants.REVERSE_PROXY);
    }

    @Override
    public Collection<AttributeDefinition> getAttributes() {
        return Arrays.asList(CONNECTIONS_PER_THREAD, SESSION_COOKIE_NAMES, STICKY_SESSION_LIFETIME, PROBLEM_SERVER_RETRY);
    }

    @Override
    protected List<? extends PersistentResourceDefinition> getChildren() {
        return Collections.<PersistentResourceDefinition>singletonList(ReverseProxyHandlerHost.INSTANCE);
    }

    @Override
    public HttpHandler createHandler(final OperationContext context, ModelNode model) throws OperationFailedException {

        String sessionCookieNames = SESSION_COOKIE_NAMES.resolveModelAttribute(context, model).asString();
        int connectionsPerThread = CONNECTIONS_PER_THREAD.resolveModelAttribute(context, model).asInt();
        int stickySessionLifetime = STICKY_SESSION_LIFETIME.resolveModelAttribute(context, model).asInt();
        int problemServerRetry = PROBLEM_SERVER_RETRY.resolveModelAttribute(context, model).asInt();
        ModelNode maxTimeNode = MAX_REQUEST_TIME.resolveModelAttribute(context, model);
        int maxTime = -1;
        if (maxTimeNode.isDefined()) {
            maxTime = maxTimeNode.asInt();
        }
        final LoadBalancingProxyClient lb = new LoadBalancingProxyClient()
                .setConnectionsPerThread(connectionsPerThread)
                .setProblemServerRetry(problemServerRetry)
                .setStickeySessionLifetime(stickySessionLifetime * 60);
        String[] sessionIds = sessionCookieNames.split(",");
        for (String id : sessionIds) {
            lb.addSessionCookieName(id);
        }

        ProxyHandler handler = new ProxyHandler(lb, maxTime);
        return handler;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11545.java