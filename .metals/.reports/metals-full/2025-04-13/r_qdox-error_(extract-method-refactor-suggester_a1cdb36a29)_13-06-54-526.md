error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14165.java
text:
```scala
n@@ewControllers.add(InitialContextFactoryService.addService(target, verificationHandler));

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

package org.jboss.as.naming.service;

import java.util.List;
import javax.management.MBeanServer;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.NewOperationContext;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.naming.InMemoryNamingStore;
import org.jboss.as.naming.InitialContextFactoryService;
import org.jboss.as.naming.NamingContext;
import org.jboss.as.naming.NamingEventCoordinator;
import org.jboss.as.naming.NamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author Emanuel Muckenhuber
 */
public class NamingSubsystemAdd extends AbstractAddStepHandler {

    private static final Logger log = Logger.getLogger("org.jboss.as.naming");

    static final NamingSubsystemAdd INSTANCE = new NamingSubsystemAdd();

    protected void populateModel(ModelNode operation, ModelNode model) {
        model.setEmptyObject();
    }

    protected void performRuntime(NewOperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) {

        log.info("Activating Naming Subsystem");

        NamingContext.initializeNamingManager();

        final NamingStore namingStore = new InMemoryNamingStore(new NamingEventCoordinator());

        // Create the Naming Service
        final ServiceTarget target = context.getServiceTarget();
        newControllers.add(target.addService(NamingService.SERVICE_NAME, new NamingService(namingStore))
                .addAliases(ContextNames.JAVA_CONTEXT_SERVICE_NAME)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .addListener(verificationHandler)
                .install());

        // Create the java:global namespace
        newControllers.add(addGlobalContextFactory(target, "global", verificationHandler));
        // Create the java:jboss vendor namespace
        newControllers.add(addGlobalContextFactory(target, "jboss", verificationHandler));

        // Create the EE namespace
        newControllers.add(addContextFactory(target, "app", verificationHandler));
        newControllers.add(addContextFactory(target, "module", verificationHandler));
        newControllers.add(addContextFactory(target, "comp", verificationHandler));

        // Provide the {@link InitialContext} as OSGi service
        InitialContextFactoryService.addService(target);

        final JndiView jndiView = new JndiView();
        newControllers.add(target.addService(ServiceName.JBOSS.append("naming", "jndi", "view"), jndiView)
                .addDependency(ServiceBuilder.DependencyType.OPTIONAL, ServiceName.JBOSS.append("mbean", "server"), MBeanServer.class, jndiView.getMBeanServerInjector())
                .addListener(verificationHandler)
                .install());
    }

    private static ServiceController<?> addContextFactory(final ServiceTarget target, final String contextName, final ServiceVerificationHandler verificationHandler) {
        final EEContextService eeContextService = new EEContextService(contextName);
        return target.addService(ContextNames.JAVA_CONTEXT_SERVICE_NAME.append(contextName), eeContextService)
                .addDependency(ContextNames.JAVA_CONTEXT_SERVICE_NAME, NamingStore.class, eeContextService.getJavaContextInjector())
                .addListener(verificationHandler)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
    }

    private static ServiceController<?> addGlobalContextFactory(final ServiceTarget target, final String contextName, final ServiceVerificationHandler verificationHandler) {
        final GlobalContextService eeContextService = new GlobalContextService(contextName);
        return target.addService(ContextNames.JAVA_CONTEXT_SERVICE_NAME.append(contextName), eeContextService)
                .addDependency(ContextNames.JAVA_CONTEXT_SERVICE_NAME, NamingStore.class, eeContextService.getJavaContextInjector())
                .addListener(verificationHandler)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14165.java