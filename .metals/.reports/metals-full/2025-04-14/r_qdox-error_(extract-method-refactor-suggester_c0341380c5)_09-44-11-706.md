error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4453.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4453.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4453.java
text:
```scala
e@@ntityManager1 = new ExtendedEntityManager(unitName, emf.createEntityManager(properties), synchronizationType);

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

package org.jboss.as.jpa.injectors;

import java.lang.reflect.Proxy;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContextType;
import javax.persistence.SynchronizationType;

import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.jpa.config.ExtendedPersistenceInheritance;
import org.jboss.as.jpa.config.JPADeploymentSettings;
import org.jboss.as.jpa.container.CreatedEntityManagers;
import org.jboss.as.jpa.container.EntityManagerUnwrappedTargetInvocationHandler;
import org.jboss.as.jpa.container.ExtendedEntityManager;
import org.jboss.as.jpa.container.ExtendedPersistenceDeepInheritance;
import org.jboss.as.jpa.container.ExtendedPersistenceShallowInheritance;
import org.jboss.as.jpa.container.TransactionScopedEntityManager;
import org.jboss.as.jpa.service.JPAService;
import org.jboss.as.jpa.service.PersistenceUnitServiceImpl;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.naming.ValueManagedReference;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.value.ImmediateValue;
import org.jipijapa.plugin.spi.PersistenceUnitMetadata;

import static org.jboss.as.jpa.messages.JpaLogger.JPA_LOGGER;
import static org.jboss.as.jpa.messages.JpaMessages.MESSAGES;

/**
 * Represents the PersistenceContext injected into a component.
 *
 * @author Scott Marlow
 */
public class PersistenceContextInjectionSource extends InjectionSource {

    private final PersistenceContextType type;

    private final PersistenceContextJndiInjectable injectable;

    private final ServiceName puServiceName;

    /**
     * Constructor for the PersistenceContextInjectorService
     *
     * @param type              The persistence context type
     * @param properties        The persistence context properties
     * @param puServiceName     represents the deployed persistence.xml that we are going to use.
     * @param serviceRegistry    The MSC service registry which will be used to find the PersistenceContext service
     * @param scopedPuName      the fully scoped reference to the persistence.xml
     * @param injectionTypeName is normally "javax.persistence.EntityManager" but could be a different target class
     *                          for example "org.hibernate.Session" in which case, EntityManager.unwrap(org.hibernate.Session.class is called)
     * @param pu
     * @param jpaDeploymentSettings Optional {@link JPADeploymentSettings} applicable for the persistence context
     */
    public PersistenceContextInjectionSource(final PersistenceContextType type, final SynchronizationType synchronizationType , final Map properties, final ServiceName puServiceName,
                                             final ServiceRegistry serviceRegistry, final String scopedPuName, final String injectionTypeName, final PersistenceUnitMetadata pu, final JPADeploymentSettings jpaDeploymentSettings) {

        this.type = type;
        injectable = new PersistenceContextJndiInjectable(puServiceName, serviceRegistry, this.type, synchronizationType , properties, scopedPuName, injectionTypeName, pu, jpaDeploymentSettings);
        this.puServiceName = puServiceName;
    }

    public void getResourceValue(final ResolutionContext resolutionContext, final ServiceBuilder<?> serviceBuilder, final DeploymentPhaseContext phaseContext, final Injector<ManagedReferenceFactory> injector) throws
        DeploymentUnitProcessingException {
        serviceBuilder.addDependencies(puServiceName);
        injector.inject(injectable);
    }

    public boolean equals(Object other) {
        if (other instanceof PersistenceContextInjectionSource) {
            PersistenceContextInjectionSource source = (PersistenceContextInjectionSource) other;
            return (source.puServiceName.equals(puServiceName));
        }
        return false;
    }

    public int hashCode() {
        return puServiceName.hashCode();
    }

    private static final class PersistenceContextJndiInjectable implements ManagedReferenceFactory {

        private final ServiceName puServiceName;
        private final ServiceRegistry serviceRegistry;
        private final PersistenceContextType type;
        private final SynchronizationType synchronizationType;
        private final Map properties;
        private final String unitName;
        private final String injectionTypeName;
        private final PersistenceUnitMetadata pu;
        private final JPADeploymentSettings jpaDeploymentSettings;

        private static final String ENTITY_MANAGER_CLASS = "javax.persistence.EntityManager";

        public PersistenceContextJndiInjectable(
            final ServiceName puServiceName,
            final ServiceRegistry serviceRegistry,
            final PersistenceContextType type,
            SynchronizationType synchronizationType,
            final Map properties,
            final String unitName,
            final String injectionTypeName,
            final PersistenceUnitMetadata pu,
            final JPADeploymentSettings jpaDeploymentSettings) {

            this.puServiceName = puServiceName;
            this.serviceRegistry = serviceRegistry;
            this.type = type;
            this.properties = properties;
            this.unitName = unitName;
            this.injectionTypeName = injectionTypeName;
            this.pu = pu;
            this.synchronizationType = synchronizationType;
            this.jpaDeploymentSettings = jpaDeploymentSettings;
        }

        @Override
        public ManagedReference getReference() {
            PersistenceUnitServiceImpl service = (PersistenceUnitServiceImpl) serviceRegistry.getRequiredService(puServiceName).getValue();
            EntityManagerFactory emf = service.getEntityManagerFactory();
            EntityManager entityManager;
            boolean standardEntityManager = ENTITY_MANAGER_CLASS.equals(injectionTypeName);

            if (type.equals(PersistenceContextType.TRANSACTION)) {
                entityManager = new TransactionScopedEntityManager(unitName, properties, emf, synchronizationType);
                if (JPA_LOGGER.isDebugEnabled())
                    JPA_LOGGER.debugf("created new TransactionScopedEntityManager for unit name=%s", unitName);
            } else {
                boolean useDeepInheritance = !ExtendedPersistenceInheritance.SHALLOW.equals(JPAService.getDefaultExtendedPersistenceInheritance());
                if (jpaDeploymentSettings != null) {
                    useDeepInheritance = ExtendedPersistenceInheritance.DEEP.equals(jpaDeploymentSettings.getExtendedPersistenceInheritanceType());
                }

                boolean createdNewExtendedPersistence = false;
                ExtendedEntityManager entityManager1;
                // handle PersistenceContextType.EXTENDED
                if (useDeepInheritance) {
                    entityManager1 = ExtendedPersistenceDeepInheritance.INSTANCE.findExtendedPersistenceContext(unitName);
                }
                else {
                    entityManager1 = ExtendedPersistenceShallowInheritance.INSTANCE.findExtendedPersistenceContext(unitName);
                }

                if (entityManager1 == null) {
                    entityManager1 = new ExtendedEntityManager(unitName, emf.createEntityManager(properties));
                    createdNewExtendedPersistence = true;
                    if (JPA_LOGGER.isDebugEnabled())
                        JPA_LOGGER.debugf("created new ExtendedEntityManager for unit name=%s, useDeepInheritance = %b", unitName, useDeepInheritance);

                } else {
                    entityManager1.increaseReferenceCount();
                    if (JPA_LOGGER.isDebugEnabled())
                        JPA_LOGGER.debugf("inherited existing ExtendedEntityManager from SFSB invocation stack, unit name=%s, " +
                                "%d beans sharing ExtendedEntityManager, useDeepInheritance = %b", unitName, entityManager1.getReferenceCount(), useDeepInheritance);
                }

                entityManager = entityManager1;

                // register the EntityManager on TL so that SFSBCreateInterceptor will see it.
                // this is important for creating a new XPC or inheriting existing XPC from SFSBCallStack
                CreatedEntityManagers.registerPersistenceContext(entityManager1);

                if (createdNewExtendedPersistence) {
                    //register the pc so it is accessible to other SFSB's during the creation process
                    if (useDeepInheritance) {
                        ExtendedPersistenceDeepInheritance.INSTANCE.registerExtendedPersistenceContext(unitName, entityManager1);
                    }
                    else {
                        ExtendedPersistenceShallowInheritance.INSTANCE.registerExtendedPersistenceContext(unitName, entityManager1);
                    }
                }

            }

            if (!standardEntityManager) {
                /**
                 * inject non-standard wrapped class (e.g. org.hibernate.Session).
                 * To accomplish this, we will create an instance of the (underlying provider's) entity manager and
                 * invoke the EntityManager.unwrap(TargetInjectionClass).
                 */
                Class extensionClass;
                try {
                    // provider classes should be on application classpath
                    extensionClass = pu.getClassLoader().loadClass(injectionTypeName);
                } catch (ClassNotFoundException e) {
                    throw MESSAGES.cannotLoadFromJpa(e, injectionTypeName);
                }
                // get example of target object
                Object targetValueToInject = entityManager.unwrap(extensionClass);

                // build array of classes that proxy will represent.
                Class[] targetInterfaces = targetValueToInject.getClass().getInterfaces();
                Class[] proxyInterfaces = new Class[targetInterfaces.length + 1];  // include extra element for extensionClass
                boolean alreadyHasInterfaceClass = false;
                for (int interfaceIndex = 0; interfaceIndex < targetInterfaces.length; interfaceIndex++) {
                    Class interfaceClass =  targetInterfaces[interfaceIndex];
                    if (interfaceClass.equals(extensionClass)) {
                        proxyInterfaces = targetInterfaces;                     // targetInterfaces already has all interfaces
                        alreadyHasInterfaceClass = true;
                        break;
                    }
                    proxyInterfaces[1 + interfaceIndex] = interfaceClass;
                }
                if (!alreadyHasInterfaceClass) {
                    proxyInterfaces[0] = extensionClass;
                }

                EntityManagerUnwrappedTargetInvocationHandler entityManagerUnwrappedTargetInvocationHandler =
                    new EntityManagerUnwrappedTargetInvocationHandler(entityManager, extensionClass);
                Object proxyForUnwrappedObject = Proxy.newProxyInstance(
                                    extensionClass.getClassLoader(), //use the target classloader so the proxy has the same scope
                                    proxyInterfaces,
                                    entityManagerUnwrappedTargetInvocationHandler
                                );

                if (JPA_LOGGER.isDebugEnabled())
                    JPA_LOGGER.debugf("injecting entity manager into a '%s' (unit name=%s)", extensionClass.getName(), unitName);

                return new ValueManagedReference(new ImmediateValue<Object>(proxyForUnwrappedObject));
            }

            return new ValueManagedReference(new ImmediateValue<Object>(entityManager));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4453.java