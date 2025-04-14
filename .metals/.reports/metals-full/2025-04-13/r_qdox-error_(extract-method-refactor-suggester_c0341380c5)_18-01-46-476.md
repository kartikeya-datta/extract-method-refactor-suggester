error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10765.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10765.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10765.java
text:
```scala
r@@eturn JAVA_CONTEXT_SERVICE_NAME.append(context);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.ee.naming;

import org.jboss.as.naming.deployment.JndiName;
import org.jboss.msc.service.ServiceName;

/**
 * @author John Bailey
 */
public class ContextNames extends org.jboss.as.naming.deployment.ContextNames{
    /**
     * Jndi name for java:global namespace
     */
    public static final JndiName GLOBAL_CONTEXT_NAME = JndiName.of("java:global");

    /**
     * ServiceName for java:global namespace
     */
    public static final ServiceName GLOBAL_CONTEXT_SERVICE_NAME = JAVA_CONTEXT_SERVICE_NAME.append("global");

    /**
     * Jndi name for java:app namespace
     */
    public static final JndiName APPLICATION_CONTEXT_NAME = JndiName.of("java:app");

    /**
     * Parent ServiceName for java:app namespace
     */
    public static final ServiceName APPLICATION_CONTEXT_SERVICE_NAME = JAVA_CONTEXT_SERVICE_NAME.append("app");

    /**
     * Jndi name for java:module namespace
     */
    public static final JndiName MODULE_CONTEXT_NAME = JndiName.of("java:module");

    /**
     * Parent ServiceName for java:module namespace
     */
    public static final ServiceName MODULE_CONTEXT_SERVICE_NAME = JAVA_CONTEXT_SERVICE_NAME.append("module");

    /**
     * Jndi name for java:comp namespace
     */
    public static final JndiName COMPONENT_CONTEXT_NAME = JndiName.of("java:comp");

    /**
     * Parent ServiceName for java:comp namespace
     */
    public static final ServiceName COMPONENT_CONTEXT_SERVICE_NAME = JAVA_CONTEXT_SERVICE_NAME.append("comp");

    /**
     * Get the base service name of a component's JNDI namespace.
     *
     * @param app the application name (must not be {@code null})
     * @param module the module name (must not be {@code null})
     * @param comp the component name (must not be {@code null})
     * @return the base service name
     */
    public static ServiceName contextServiceNameOfComponent(String app, String module, String comp) {
        return COMPONENT_CONTEXT_SERVICE_NAME.append(app, module, comp);
    }

    /**
     * Get the base service name of a module's JNDI namespace.
     *
     * @param app the application name (must not be {@code null})
     * @param module the module name (must not be {@code null})
     * @return the base service name
     */
    public static ServiceName contextServiceNameOfModule(String app, String module) {
        return MODULE_CONTEXT_SERVICE_NAME.append(app, module);
    }

    /**
     * Get the base service name of an application's JNDI namespace.
     *
     * @param app the application name (must not be {@code null})
     * @return the base service name
     */
    public static ServiceName contextServiceNameOfApplication(String app) {
        return APPLICATION_CONTEXT_SERVICE_NAME.append(app);
    }

    /**
     * Get the service name of a context, or {@code null} if there is no service mapping for the context name.
     *
     * @param app the application name
     * @param module the module name
     * @param comp the component name
     * @param context the context to check
     * @return the service name or {@code null} if there is no service
     */
    public static ServiceName serviceNameOfContext(String app, String module, String comp, String context) {
        if (context.startsWith("java:")) {
            final String namespace;
            final int i = context.indexOf('/');
            if (i == -1) {
                namespace = context.substring(5);
            } else {
                namespace = context.substring(5, i);
            }
            if (namespace.equals("global")) {
                return GLOBAL_CONTEXT_SERVICE_NAME.append(context.substring(12));
            } else if (namespace.equals("app")) {
                return contextServiceNameOfApplication(app).append(context.substring(9));
            } else if (namespace.equals("module")) {
                return contextServiceNameOfModule(app, module).append(context.substring(12));
            } else if (namespace.equals("comp")) {
                return contextServiceNameOfComponent(app, module, comp).append(context.substring(10));
            } else {
                return null;
            }
        } else {
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10765.java