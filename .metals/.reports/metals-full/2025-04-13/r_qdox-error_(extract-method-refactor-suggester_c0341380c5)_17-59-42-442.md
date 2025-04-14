error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/574.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/574.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1135
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/574.java
text:
```scala
public class PersistenceUnitServiceImpl implements Service<PersistenceUnitService>, PersistenceUnitService {

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

p@@ackage org.jboss.as.jpa.service;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.jboss.as.jpa.classloader.TempClassLoaderFactoryImpl;
import org.jboss.as.jpa.spi.PersistenceProviderAdaptor;
import org.jboss.as.jpa.spi.PersistenceUnitMetadata;
import org.jboss.as.jpa.spi.PersistenceUnitService;
import org.jboss.as.jpa.subsystem.PersistenceUnitRegistryImpl;
import org.jboss.as.jpa.util.JPAServiceNames;
import org.jboss.as.naming.WritableServiceBasedNamingStore;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

import static org.jboss.as.jpa.JpaLogger.JPA_LOGGER;

/**
 * Persistence Unit service that is created for each deployed persistence unit that will be referenced by the
 * persistence context/unit injector.
 * <p/>
 * The persistence unit scoped
 *
 * @author Scott Marlow
 */
public class PersistenceUnitServiceImpl implements Service<PersistenceUnitServiceImpl>, PersistenceUnitService {
    private final InjectedValue<Map> properties = new InjectedValue<Map>();
    private final InjectedValue<DataSource> jtaDataSource = new InjectedValue<DataSource>();
    private final InjectedValue<DataSource> nonJtaDataSource = new InjectedValue<DataSource>();
    private final InjectedValue<ExecutorService> executorInjector = new InjectedValue<ExecutorService>();

    private final PersistenceProviderAdaptor persistenceProviderAdaptor;
    private final PersistenceProvider persistenceProvider;
    private final PersistenceUnitMetadata pu;
    private final ClassLoader classLoader;
    private final PersistenceUnitRegistryImpl persistenceUnitRegistry;

    private volatile EntityManagerFactory entityManagerFactory;

    public PersistenceUnitServiceImpl(
            final ClassLoader classLoader,
            final PersistenceUnitMetadata pu,
            final PersistenceProviderAdaptor persistenceProviderAdaptor,
            final PersistenceProvider persistenceProvider,
            final PersistenceUnitRegistryImpl persistenceUnitRegistry) {
        this.pu = pu;
        this.persistenceProviderAdaptor = persistenceProviderAdaptor;
        this.persistenceProvider = persistenceProvider;
        this.classLoader = classLoader;
        this.persistenceUnitRegistry = persistenceUnitRegistry;
    }

    @Override
    public void start(final StartContext context) throws StartException {
        final ExecutorService executor = executorInjector.getValue();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    JPA_LOGGER.startingService("Persistence Unit", pu.getScopedPersistenceUnitName());
                    pu.setTempClassLoaderFactory(new TempClassLoaderFactoryImpl(classLoader));
                    pu.setJtaDataSource(jtaDataSource.getOptionalValue());
                    pu.setNonJtaDataSource(nonJtaDataSource.getOptionalValue());
                    WritableServiceBasedNamingStore.pushOwner(context.getController().getServiceContainer().subTarget());
                    entityManagerFactory = createContainerEntityManagerFactory();
                    persistenceUnitRegistry.add(getScopedPersistenceUnitName(), getValue());
                    context.complete();
                } catch (Throwable t) {
                    context.failed(new StartException(t));
                } finally {
                    pu.setTempClassLoaderFactory(null);    // release the temp classloader factory (only needed when creating the EMF)
                    WritableServiceBasedNamingStore.popOwner();
                }
            }
        };
        context.asynchronous();
        executor.execute(task);
    }

    @Override
    public void stop(final StopContext context) {
        final ExecutorService executor = executorInjector.getValue();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                JPA_LOGGER.stoppingService("Persistence Unit", pu.getScopedPersistenceUnitName());
                if (entityManagerFactory != null) {
                    WritableServiceBasedNamingStore.pushOwner(context.getController().getServiceContainer().subTarget());
                    try {
                        entityManagerFactory.close();
                    } catch (Throwable t) {
                        JPA_LOGGER.failedToStopPUService(t, pu.getScopedPersistenceUnitName());
                    } finally {
                        entityManagerFactory = null;
                        pu.setTempClassLoaderFactory(null);
                        WritableServiceBasedNamingStore.popOwner();
                        persistenceUnitRegistry.remove(getScopedPersistenceUnitName());
                    }
                }
                context.complete();
            }
        };
        context.asynchronous();
        executor.execute(task);
    }

    public InjectedValue<ExecutorService> getExecutorInjector() {
        return executorInjector;
    }

    @Override
    public PersistenceUnitServiceImpl getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    /**
     * Get the entity manager factory
     *
     * @return the entity manager factory
     */
    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    public String getScopedPersistenceUnitName() {
        return pu.getScopedPersistenceUnitName();
    }

    public Injector<Map> getPropertiesInjector() {
        return properties;
    }

    public Injector<DataSource> getJtaDataSourceInjector() {
        return jtaDataSource;
    }

    public Injector<DataSource> getNonJtaDataSourceInjector() {
        return nonJtaDataSource;
    }

    /**
     * Returns the Persistence Unit service name used for creation or lookup.
     * The service name contains the unique fully scoped persistence unit name
     *
     * @param pu persistence unit definition
     * @return
     */
    public static ServiceName getPUServiceName(PersistenceUnitMetadata pu) {
        return JPAServiceNames.getPUServiceName(pu.getScopedPersistenceUnitName());
    }

    public static ServiceName getPUServiceName(String scopedPersistenceUnitName) {
        return JPAServiceNames.getPUServiceName(scopedPersistenceUnitName);
    }

    /**
     * Create EE container entity manager factory
     *
     * @return EntityManagerFactory
     */
    private EntityManagerFactory createContainerEntityManagerFactory() {
        persistenceProviderAdaptor.beforeCreateContainerEntityManagerFactory(pu);
        try {
            return persistenceProvider.createContainerEntityManagerFactory(pu, properties.getValue());
        } finally {
            try {
                persistenceProviderAdaptor.afterCreateContainerEntityManagerFactory(pu);
            } finally {
                pu.setAnnotationIndex(null);    // close reference to Annotation Index (only needed during call to createContainerEntityManagerFactory)
                //This is needed if the datasource is restarted
                //pu.setTempClassLoaderFactory(null);    // close reference to temp classloader factory (only needed during call to createEntityManagerFactory)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/574.java