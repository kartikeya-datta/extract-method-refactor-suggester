error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10544.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10544.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10544.java
text:
```scala
t@@hrow new DeploymentUnitProcessingException(MESSAGES.persistenceUnitNotFound(puName, deploymentUnit));

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

package org.jboss.as.jpa.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContextType;

import org.jboss.as.ee.component.BindingConfiguration;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.DeploymentDescriptorEnvironment;
import org.jboss.as.ee.component.EEApplicationClasses;
import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.ee.component.LookupInjectionSource;
import org.jboss.as.ee.component.ResourceInjectionTarget;
import org.jboss.as.ee.component.deployers.AbstractDeploymentDescriptorBindingsProcessor;
import org.jboss.as.jpa.container.PersistenceUnitSearch;
import org.jboss.as.jpa.injectors.PersistenceContextInjectionSource;
import org.jboss.as.jpa.injectors.PersistenceUnitInjectionSource;
import org.jboss.as.jpa.service.PersistenceUnitServiceImpl;
import org.jboss.as.jpa.spi.PersistenceUnitMetadata;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.metadata.javaee.spec.Environment;
import org.jboss.metadata.javaee.spec.PersistenceContextReferenceMetaData;
import org.jboss.metadata.javaee.spec.PersistenceContextReferencesMetaData;
import org.jboss.metadata.javaee.spec.PersistenceUnitReferenceMetaData;
import org.jboss.metadata.javaee.spec.PersistenceUnitReferencesMetaData;
import org.jboss.metadata.javaee.spec.PropertiesMetaData;
import org.jboss.metadata.javaee.spec.PropertyMetaData;
import org.jboss.metadata.javaee.spec.RemoteEnvironment;
import org.jboss.msc.service.ServiceName;

import static org.jboss.as.jpa.JpaMessages.MESSAGES;

/**
 * Deployment processor responsible for processing persistence unit / context references from deployment descriptors.
 *
 * @author Stuart Douglas
 */
public class PersistenceRefProcessor extends AbstractDeploymentDescriptorBindingsProcessor {


    @Override
    protected List<BindingConfiguration> processDescriptorEntries(DeploymentUnit deploymentUnit, DeploymentDescriptorEnvironment environment, ResourceInjectionTarget resourceInjectionTarget, final ComponentDescription componentDescription, ClassLoader classLoader, DeploymentReflectionIndex deploymentReflectionIndex, final EEApplicationClasses applicationClasses) throws
        DeploymentUnitProcessingException {
        List<BindingConfiguration> bindings = new ArrayList<BindingConfiguration>();
        bindings.addAll(getPersistenceUnitRefs(deploymentUnit, environment, classLoader, deploymentReflectionIndex, resourceInjectionTarget));
        bindings.addAll(getPersistenceContextRefs(deploymentUnit, environment, classLoader, deploymentReflectionIndex, resourceInjectionTarget));
        return bindings;
    }


    /**
     * Resolves persistence-unit-ref
     *
     * @param environment               The environment to resolve the elements for
     * @param classLoader               The deployment class loader
     * @param deploymentReflectionIndex The reflection index
     * @return The bindings for the environment entries
     */
    private List<BindingConfiguration> getPersistenceUnitRefs(DeploymentUnit deploymentUnit, DeploymentDescriptorEnvironment environment, ClassLoader classLoader, DeploymentReflectionIndex deploymentReflectionIndex, ResourceInjectionTarget resourceInjectionTarget) throws
        DeploymentUnitProcessingException {

        final List<BindingConfiguration> bindingConfigurations = new ArrayList<BindingConfiguration>();
        if (environment.getEnvironment() == null) {
            return bindingConfigurations;
        }
        PersistenceUnitReferencesMetaData persistenceUnitRefs = environment.getEnvironment().getPersistenceUnitRefs();

        if (persistenceUnitRefs != null) {
            if (persistenceUnitRefs.size() > 0) {
                JPADeploymentMarker.mark(deploymentUnit);
            }
            for (PersistenceUnitReferenceMetaData puRef : persistenceUnitRefs) {
                String name = puRef.getName();
                String persistenceUnitName = puRef.getPersistenceUnitName();
                String lookup = puRef.getLookupName();

                if (!isEmpty(lookup) && !isEmpty(persistenceUnitName)) {
                    throw MESSAGES.cannotSpecifyBoth("<lookup-name>", lookup, "persistence-unit-name", persistenceUnitName, "<persistence-unit-ref/>", resourceInjectionTarget);
                }
                if (!name.startsWith("java:")) {
                    name = environment.getDefaultContext() + name;
                }

                // our injection (source) comes from the local (ENC) lookup, no matter what.
                LookupInjectionSource injectionSource = new LookupInjectionSource(name);

                //add any injection targets
                processInjectionTargets(resourceInjectionTarget, injectionSource, classLoader, deploymentReflectionIndex, puRef, EntityManagerFactory.class);

                BindingConfiguration bindingConfiguration = null;
                if (!isEmpty(lookup)) {
                    bindingConfiguration = new BindingConfiguration(name, new LookupInjectionSource(lookup));
                } else {
                    InjectionSource puBindingSource = this.getPersistenceUnitBindingSource(deploymentUnit, persistenceUnitName);
                    bindingConfiguration = new BindingConfiguration(name, puBindingSource);
                }
                bindingConfigurations.add(bindingConfiguration);
            }
        }
        return bindingConfigurations;
    }

    /**
     * Resolves persistence-unit-ref
     *
     * @param environment               The environment to resolve the elements for
     * @param classLoader               The deployment class loader
     * @param deploymentReflectionIndex The reflection index
     * @return The bindings for the environment entries
     */
    private List<BindingConfiguration> getPersistenceContextRefs(DeploymentUnit deploymentUnit, DeploymentDescriptorEnvironment environment, ClassLoader classLoader, DeploymentReflectionIndex deploymentReflectionIndex, ResourceInjectionTarget resourceInjectionTarget) throws
        DeploymentUnitProcessingException {

        List<BindingConfiguration> bindingConfigurations = new ArrayList<BindingConfiguration>();
        final RemoteEnvironment remoteEnvironment = environment.getEnvironment();
        if (remoteEnvironment == null) {
            return bindingConfigurations;
        }

        if (remoteEnvironment instanceof Environment) {
            PersistenceContextReferencesMetaData persistenceUnitRefs = ((Environment) remoteEnvironment).getPersistenceContextRefs();

            if (persistenceUnitRefs != null) {
                for (PersistenceContextReferenceMetaData puRef : persistenceUnitRefs) {
                    String name = puRef.getName();
                    String persistenceUnitName = puRef.getPersistenceUnitName();
                    String lookup = puRef.getLookupName();

                    if (!isEmpty(lookup) && !isEmpty(persistenceUnitName)) {
                        throw MESSAGES.cannotSpecifyBoth("<lookup-name>", lookup, "persistence-unit-name", persistenceUnitName, "<persistence-context-ref/>", resourceInjectionTarget);
                    }
                    if (!name.startsWith("java:")) {
                        name = environment.getDefaultContext() + name;
                    }

                    // our injection (source) comes from the local (ENC) lookup, no matter what.
                    LookupInjectionSource injectionSource = new LookupInjectionSource(name);
                    //add any injection targets
                    processInjectionTargets(resourceInjectionTarget, injectionSource, classLoader, deploymentReflectionIndex, puRef, EntityManager.class);

                    BindingConfiguration bindingConfiguration = null;
                    if (!isEmpty(lookup)) {
                        bindingConfiguration = new BindingConfiguration(name, new LookupInjectionSource(lookup));
                    } else {
                        PropertiesMetaData properties = puRef.getProperties();
                        Map map = new HashMap();
                        if (properties != null) {
                            for (PropertyMetaData prop : properties) {
                                map.put(prop.getKey(), prop.getValue());
                            }
                        }
                        PersistenceContextType type = puRef.getPersistenceContextType() == null ? PersistenceContextType.TRANSACTION : puRef.getPersistenceContextType();
                        InjectionSource pcBindingSource = this.getPersistenceContextBindingSource(deploymentUnit, persistenceUnitName, type, map);
                        bindingConfiguration = new BindingConfiguration(name, pcBindingSource);
                    }
                    bindingConfigurations.add(bindingConfiguration);
                }
            }
        }
        return bindingConfigurations;
    }


    private InjectionSource getPersistenceUnitBindingSource(final DeploymentUnit deploymentUnit, final String unitName) throws
        DeploymentUnitProcessingException {
        final String searchName;
        if (isEmpty(unitName)) {
            searchName = null;
        } else {
            searchName = unitName;
        }
        final PersistenceUnitMetadata pu = PersistenceUnitSearch.resolvePersistenceUnitSupplier(deploymentUnit, searchName);
        String scopedPuName = pu.getScopedPersistenceUnitName();
        ServiceName puServiceName = getPuServiceName(scopedPuName);
        return new PersistenceUnitInjectionSource(puServiceName, deploymentUnit, EntityManagerFactory.class.getName(), pu);
    }

    private InjectionSource getPersistenceContextBindingSource(final DeploymentUnit deploymentUnit, final String unitName, PersistenceContextType type, Map properties) throws
        DeploymentUnitProcessingException {
        PersistenceUnitMetadata pu = getPersistenceUnit(deploymentUnit, unitName);
        String scopedPuName = pu.getScopedPersistenceUnitName();
        ServiceName puServiceName = getPuServiceName(scopedPuName);
        return new PersistenceContextInjectionSource(type, properties, puServiceName, deploymentUnit, scopedPuName, EntityManager.class.getName(), pu);
    }

    private PersistenceUnitMetadata getPersistenceUnit(final DeploymentUnit deploymentUnit, final String puName)
        throws DeploymentUnitProcessingException {

        PersistenceUnitMetadata pu = PersistenceUnitSearch.resolvePersistenceUnitSupplier(deploymentUnit, puName);
        if (null == pu) {
            throw new DeploymentUnitProcessingException(MESSAGES.deploymentUnitNotFound(puName, deploymentUnit));
        }
        return pu;
    }

    private ServiceName getPuServiceName(String scopedPuName)
        throws DeploymentUnitProcessingException {

        return PersistenceUnitServiceImpl.getPUServiceName(scopedPuName);
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10544.java