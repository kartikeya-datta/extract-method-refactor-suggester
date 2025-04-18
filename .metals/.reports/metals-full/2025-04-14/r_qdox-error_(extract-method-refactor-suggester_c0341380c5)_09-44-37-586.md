error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14793.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14793.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14793.java
text:
```scala
public static final S@@erviceName SERVICE_NAME = ServiceName.JBOSS.append("persistenceunit");

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

package org.jboss.as.jpa.service;

import org.jboss.as.jpa.config.PersistenceUnitMetadata;
import org.jboss.as.jpa.container.PersistenceUnitSearch;
import org.jboss.as.jpa.hibernate.HibernateAnnotationScanner;
import org.jboss.as.jpa.persistenceprovider.PersistenceProviderAdapterRegistry;
import org.jboss.as.jpa.spi.PersistenceProviderAdaptor;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.modules.Module;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import javax.sql.DataSource;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Persistence Unit service that is created for each deployed persistence unit that will be referenced by the
 * persistence context/unit injector.
 * <p/>
 * The persistence unit scoped
 *
 * @author Scott Marlow
 */
public class PersistenceUnitService implements Service<PersistenceUnitService> {

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("jpa", "persistenceunit");

    private final InjectedValue<Map> properties = new InjectedValue<Map>();

    private final InjectedValue<DataSource> jtaDataSource = new InjectedValue<DataSource>();
    private final InjectedValue<DataSource> nonJtaDataSource = new InjectedValue<DataSource>();

    private EntityManagerFactory entityManagerFactory;
    private PersistenceUnitMetadata pu;

    /**
     * Map of PersistenceUnitService keyed by the scoped persistence unit name.  The scoped name identifies the deployment
     * location.
     * <p/>
     */
    private static ConcurrentHashMap<String, PersistenceUnitService> persistenceServiceBackdoor = new ConcurrentHashMap<String, PersistenceUnitService>();

    public PersistenceUnitService(PersistenceUnitMetadata pu, ResourceRoot resourceRoot) {
        this.pu = pu;
    }

    @Override
    public void start(StartContext context) throws StartException {
        try {
            PersistenceProvider provider = lookupProvider(pu.getPersistenceProviderClassName());

            pu.setJtaDataSource(jtaDataSource.getOptionalValue());
            pu.setNonJtaDataSource(nonJtaDataSource.getOptionalValue());
            this.entityManagerFactory = createContainerEntityManagerFactory(provider);
            register(this);

        } finally {
            pu.setTempClassloader(null);    // release the temp classloader (only needed when creating the EMF)
        }
    }

    @Override
    public void stop(StopContext context) {
        try {
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
                entityManagerFactory = null;
            }
        } finally {
            unregister(this);
        }
    }

    @Override
    public PersistenceUnitService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    /**
     * Creates an entity manager via EntityManagerFactory.createContainerEntityManagerFactory
     *
     * @param scopedPersistenceUnitName identifies the deployment target
     * @return created EntityManager
     */
    public EntityManager createEntityManager(String scopedPersistenceUnitName) {
        return entityManagerFactory.createEntityManager(properties.getValue());
    }

    /**
     * Get the entity manager factory
     *
     * @return
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    private String createScopedName(String appName, DeploymentUnit deploymentUnit, Module module, String persistenceUnitName) {
        // persistenceUnitName must be a simple name
        assert persistenceUnitName.indexOf('/') == -1;
        assert persistenceUnitName.indexOf('#') == -1;

        String modulePath = "";//module.getModuleLoader().toString(); // javaEEModuleInformer.getModulePath(deploymentUnit);
        String unitName = (appName != null ? appName + "/" : "") + modulePath + "#" + persistenceUnitName;
        return "persistence.unit:unitName=" + unitName;
    }

    /**
     * Resolve persistence unit by persistence unit name within the specified deployment.
     * <p/>
     * returns fully qualified (scoped) persistence unit name
     */
    public String resolvePersistenceUnitSupplier(DeploymentUnit deploymentUnit, String persistenceUnitName) {
        return PersistenceUnitSearch.resolvePersistenceUnitSupplier(deploymentUnit, persistenceUnitName);
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
        return PersistenceUnitService.SERVICE_NAME.append(pu.getScopedPersistenceUnitName());
    }

    public static ServiceName getPUServiceName(String scopedPersistenceUnitName) {
        return PersistenceUnitService.SERVICE_NAME.append(scopedPersistenceUnitName);
    }


    public static PersistenceUnitService getPersistenceUnitService(String scopedPersistenceUnitName) {
        return persistenceServiceBackdoor.get(scopedPersistenceUnitName);
    }

    private static void register(PersistenceUnitService service) {

        if (persistenceServiceBackdoor.containsKey(service.pu.getScopedPersistenceUnitName()))
            throw new RuntimeException("Persistence Unit is already registered: " + service.pu.getScopedPersistenceUnitName());
        persistenceServiceBackdoor.put(service.pu.getScopedPersistenceUnitName(), service);
    }

    private static void unregister(PersistenceUnitService service) {
        String name = service.pu.getScopedPersistenceUnitName();
        Object removedValue = persistenceServiceBackdoor.remove(name);
        // if we have a leak due to have the wrong name, we need to know.
        // Also need to know if we attempt to remove the same unit multiple times.
        if (removedValue == null) {
            throw new RuntimeException("Could not remove Persistence Unit Service" + name);
        }
    }

    /**
     * Look up the persistence provider
     *
     * @param providerName
     * @return
     */
    private PersistenceProvider lookupProvider(String providerName) {
        List<PersistenceProvider> providers =
            PersistenceProviderResolverHolder.getPersistenceProviderResolver().getPersistenceProviders();
        for (PersistenceProvider provider : providers) {
            if (provider.getClass().getName().equals(providerName)) {
                return provider;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (PersistenceProvider provider : providers) {
            sb.append(provider.getClass().getName()).append(", ");
        }
        throw new PersistenceException("PersistenceProvider '" + providerName + "' not found in {" + sb.toString() + "}");
    }


    /**
     * Attempt Hack around JBTM-828 by setting the TCCL to include arjuna.
     *
     * @param provider
     * @return EntityManagerFactory
     */
    private EntityManagerFactory createContainerEntityManagerFactory(PersistenceProvider provider) {

        PersistenceProviderAdaptor adaptor = PersistenceProviderAdapterRegistry.getPersistenceProviderAdaptor(pu.getPersistenceProviderClassName());
        adaptor.beforeCreateContainerEntityManagerFactory(pu);
        AccessController.doPrivileged(new SetContextLoaderAction(com.arjuna.ats.jbossatx.jta.TransactionManagerService.class
            .getClassLoader()));
        try {
            return provider.createContainerEntityManagerFactory(pu, properties.getValue());
        } finally {
            try {
                AccessController.doPrivileged(CLEAR_ACTION);
            } finally {
                try {
                    adaptor.afterCreateContainerEntityManagerFactory(pu);
                } finally {
                    pu.setAnnotationIndex(null);    // close reference to Annotation Index (only needed during call to createContainerEntityManagerFactory)
                    pu.setTempClassloader(null);    // close reference to temp classloader (only needed during call to createEntityManagerFactory)

                }
            }
        }
    }

    private static final SetContextLoaderAction CLEAR_ACTION = new SetContextLoaderAction(null);

    private static class SetContextLoaderAction implements PrivilegedAction<Void> {

        private final ClassLoader classLoader;

        public SetContextLoaderAction(final ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public Void run() {
            Thread.currentThread().setContextClassLoader(classLoader);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14793.java