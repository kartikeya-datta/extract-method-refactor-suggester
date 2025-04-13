error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6170.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6170.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6170.java
text:
```scala
private final L@@ogger log = Logger.getLogger("org.jboss.as.jaxr");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.jaxr.service;

import org.apache.ws.scout.registry.ConnectionFactoryImpl;
import org.jboss.as.jaxr.JAXRConfiguration;
import org.jboss.as.naming.NamingStore;
import org.jboss.as.naming.ServiceBasedNamingStore;
import org.jboss.as.naming.ValueManagedReferenceFactory;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.logging.Logger;
import org.jboss.msc.service.AbstractService;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.ImmediateValue;
import org.jboss.msc.value.InjectedValue;

/**
 * Binds the JAXR ConnectionFactory to JNDI
 *
 * @author Thomas.Diesler@jboss.com
 * @since 17-Nov-2011
 */
public final class JAXRConnectionFactoryService extends AbstractService<Void> {

    static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("jaxr", "connectionfactory");

    // [TODO] AS7-2277 JAXR subsystem i18n
    private final Logger log = Logger.getLogger(JAXRConnectionFactoryService.class);

    private final InjectedValue<NamingStore> injectedJavaContext = new InjectedValue<NamingStore>();
    private final InjectedValue<JAXRConfiguration> injectedConfig = new InjectedValue<JAXRConfiguration>();

    public static ServiceController<?> addService(final ServiceTarget target, final ServiceListener<Object>... listeners) {
        JAXRConnectionFactoryService service = new JAXRConnectionFactoryService();
        ServiceBuilder<?> builder = target.addService(SERVICE_NAME, service);
        builder.addDependency(ContextNames.JAVA_CONTEXT_SERVICE_NAME, NamingStore.class, service.injectedJavaContext);
        builder.addDependency(JAXRConfiguration.SERVICE_NAME, JAXRConfiguration.class, service.injectedConfig);
        builder.addListener(listeners);
        return builder.install();
    }

    // Hide ctor
    private JAXRConnectionFactoryService() {
    }

    @Override
    public void start(final StartContext context) throws StartException {
        JAXRConfiguration config = injectedConfig.getValue();
        if (config.getConnectionFactoryBinding() != null) {
            log.infof("Binding JAXR ConnectionFactory: %s", config.getConnectionFactoryBinding());
            try {
                String jndiName = config.getConnectionFactoryBinding();
                ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(jndiName);
                BinderService binderService = new BinderService(bindInfo.getBindName());
                ImmediateValue value = new ImmediateValue(new ConnectionFactoryImpl());
                binderService.getManagedObjectInjector().inject(new ValueManagedReferenceFactory(value));
                binderService.getNamingStoreInjector().inject((ServiceBasedNamingStore) injectedJavaContext.getValue());
                ServiceBuilder<?> builder = context.getChildTarget().addService(bindInfo.getBinderServiceName(), binderService);
                builder.install();
            } catch (Exception ex) {
                log.errorf(ex, "Cannot bind JAXR ConnectionFactory");
            }
        }
    }

    @Override
    public void stop(final StopContext context) {
        JAXRConfiguration config = injectedConfig.getValue();
        if (config.getConnectionFactoryBinding() != null) {
            log.debugf("Unbind JAXR ConnectionFactory");
            try {
                String jndiName = config.getConnectionFactoryBinding();
                ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(jndiName);
                ServiceContainer serviceContainer = context.getController().getServiceContainer();
                ServiceController<?> service = serviceContainer.getService(bindInfo.getBinderServiceName());
                if (service != null) {
                    service.setMode(ServiceController.Mode.REMOVE);
                }
            } catch (Exception ex) {
                log.errorf(ex, "Cannot unbind JAXR ConnectionFactory");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6170.java