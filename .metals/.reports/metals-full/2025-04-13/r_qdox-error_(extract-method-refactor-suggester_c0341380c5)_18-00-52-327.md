error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4794.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4794.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4794.java
text:
```scala
a@@daptor.injectJtaManager(JtaManagerImpl.getInstance());

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

import org.jboss.as.jpa.config.PersistenceProviderDeploymentHolder;
import org.jboss.as.jpa.spi.PersistenceProviderAdaptor;
import org.jboss.as.jpa.transaction.JtaManagerImpl;
import org.jboss.as.server.deployment.Attachable;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.ServicesAttachment;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleClassLoader;

import javax.persistence.spi.PersistenceProvider;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Deploy JPA Persistence providers that are found in the application deployment.
 *
 * @author Scott Marlow
 */
public class PersistenceProviderProcessor implements DeploymentUnitProcessor {
    private static final Logger log = Logger.getLogger("org.jboss.as.jpa");

    /**
     * {@inheritDoc}
     */
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final Module module = deploymentUnit.getAttachment(Attachments.MODULE);

        final ServicesAttachment servicesAttachment = deploymentUnit.getAttachment(Attachments.SERVICES);
        if (module != null && servicesAttachment != null) {
            final ModuleClassLoader classLoader = module.getClassLoader();
            PersistenceProvider provider = null;
            // collect list of persistence providers packaged with the application
            final List<String> providerNames = servicesAttachment.getServiceImplementations(PersistenceProvider.class.getName());
            if (providerNames.size() > 1) {     // TODO: support more than one provider to be packaged, which requires
                                                // knowing which adapter belongs with it.
                throw new DeploymentUnitProcessingException(
                    "only one persistence provider can be packaged with an application " + providerNames);
            }
            for (String providerName : providerNames) {
                try {
                    final Class<? extends PersistenceProvider> providerClass = classLoader.loadClass(providerName).asSubclass(PersistenceProvider.class);
                    final Constructor<? extends PersistenceProvider> constructor = providerClass.getConstructor();
                    provider = constructor.newInstance();
                    log.infof("Deployment has its own Persistence Provider %s ", providerClass);

                } catch (Exception e) {
                    throw new DeploymentUnitProcessingException("Could not deploy application packaged persistence provider '" + providerName+"'", e);
                }
            }

            PersistenceProviderDeploymentHolder holder;
            Attachable topDu = top(deploymentUnit);
            holder = topDu.getAttachment(PersistenceProviderDeploymentHolder.DEPLOYED_PERSISTENCE_PROVIDER);
            if(provider != null) {

                if(holder == null) {
                    holder = new PersistenceProviderDeploymentHolder();
                }
                holder.setProvider(provider);
                String adapterClass = holder.getPersistenceProviderAdaptorClassName();
                if (adapterClass != null) {
                    try {
                        PersistenceProviderAdaptor adaptor = (PersistenceProviderAdaptor)classLoader.loadClass(adapterClass).newInstance();
                        holder.setAdapter(adaptor);
                        adaptor.setJtaManager(JtaManagerImpl.getInstance());
                    } catch (InstantiationException e) {
                        throw new DeploymentUnitProcessingException("could not create instance of adapter class '" +
                            adapterClass +"'", e);
                    } catch (IllegalAccessException e) {
                        throw new DeploymentUnitProcessingException("could not create instance of adapter class '" +
                            adapterClass +"'", e);
                    } catch (ClassNotFoundException e) {
                        throw new DeploymentUnitProcessingException("could not create instance of adapter class '" +
                            adapterClass +"'", e);
                    }
                }
                topDu.putAttachment(PersistenceProviderDeploymentHolder.DEPLOYED_PERSISTENCE_PROVIDER, holder);
            }
        }
    }

    // save in consistent location (top) for all deployments.
    private Attachable top(DeploymentUnit deploymentUnit) {
        while (deploymentUnit.getParent() != null) {
            deploymentUnit = deploymentUnit.getParent();
        }
        return deploymentUnit;
    }

    /**
     * {@inheritDoc}
     */
    public void undeploy(final DeploymentUnit context) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4794.java