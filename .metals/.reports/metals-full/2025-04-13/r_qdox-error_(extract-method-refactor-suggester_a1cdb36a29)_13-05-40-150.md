error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5360.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5360.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5360.java
text:
```scala
final I@@nterceptorFactory interceptorFactory = new ImmediateInterceptorFactory(new NamespaceContextInterceptor(selector, context.getServiceTarget()));

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

import org.jboss.as.ee.component.interceptors.InterceptorOrder;
import org.jboss.as.ee.naming.InjectedEENamespaceContextSelector;
import org.jboss.as.naming.NamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.invocation.ImmediateInterceptorFactory;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;

/**
 * A configurator which adds interceptors to the component which establish the naming context.  The interceptor is
 * added to the beginning of each chain.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class NamespaceConfigurator implements ComponentConfigurator {

    /** {@inheritDoc} */
    public void configure(final DeploymentPhaseContext context, final ComponentDescription description, final ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
        final ComponentNamingMode namingMode = description.getNamingMode();
        final InjectedEENamespaceContextSelector selector = new InjectedEENamespaceContextSelector();
        final String applicationName = configuration.getApplicationName();
        final String moduleName = configuration.getModuleName();
        final String compName = configuration.getComponentName();
        final ServiceName appContextServiceName = ContextNames.contextServiceNameOfApplication(applicationName);
        final ServiceName moduleContextServiceName = ContextNames.contextServiceNameOfModule(applicationName, moduleName);
        final ServiceName compContextServiceName = ContextNames.contextServiceNameOfComponent(applicationName, moduleName, compName);
        final Injector<NamingStore> appInjector = selector.getAppContextInjector();
        final Injector<NamingStore> moduleInjector = selector.getModuleContextInjector();
        final Injector<NamingStore> compInjector = selector.getCompContextInjector();
        final Injector<NamingStore> jbossInjector = selector.getJbossContextInjector();
        final Injector<NamingStore> globalInjector = selector.getGlobalContextInjector();

        configuration.getStartDependencies().add(new DependencyConfigurator<ComponentStartService>() {
            public void configureDependency(final ServiceBuilder<?> serviceBuilder, ComponentStartService service) {
                serviceBuilder.addDependency(appContextServiceName, NamingStore.class, appInjector);
                serviceBuilder.addDependency(moduleContextServiceName, NamingStore.class, moduleInjector);
                if (namingMode == ComponentNamingMode.CREATE) {
                    serviceBuilder.addDependency(compContextServiceName, NamingStore.class, compInjector);
                } else if(namingMode == ComponentNamingMode.USE_MODULE) {
                    serviceBuilder.addDependency(moduleContextServiceName, NamingStore.class, compInjector);
                }
                serviceBuilder.addDependency(ContextNames.GLOBAL_CONTEXT_SERVICE_NAME, NamingStore.class, globalInjector);
                serviceBuilder.addDependency(ContextNames.JBOSS_CONTEXT_SERVICE_NAME, NamingStore.class, jbossInjector);
            }
        });
        final InterceptorFactory interceptorFactory = new ImmediateInterceptorFactory(new NamespaceContextInterceptor(selector));
        configuration.addPostConstructInterceptor(interceptorFactory, InterceptorOrder.ComponentPostConstruct.JNDI_NAMESPACE_INTERCEPTOR);
        configuration.addPreDestroyInterceptor(interceptorFactory, InterceptorOrder.ComponentPreDestroy.JNDI_NAMESPACE_INTERCEPTOR);

        if(description.isTimerServiceApplicable()) {
            configuration.addTimeoutInterceptor(interceptorFactory, InterceptorOrder.Component.JNDI_NAMESPACE_INTERCEPTOR);
        }
        configuration.setNamespaceContextInterceptorFactory(interceptorFactory);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5360.java