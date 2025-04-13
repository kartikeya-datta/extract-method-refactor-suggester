error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10352.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10352.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10352.java
text:
```scala
c@@omponentsForViewName = applicationDescription.getComponentsForViewName(typeName, deploymentRoot.getRoot());

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

package org.jboss.as.ejb3.deployment.processors;

import java.util.HashSet;
import java.util.Set;

import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ee.component.EEApplicationDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ee.component.ViewManagedReferenceFactory;
import org.jboss.as.ejb3.EjbMessages;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.component.EJBViewDescription;
import org.jboss.as.ejb3.component.MethodIntf;
import org.jboss.as.ejb3.remote.RemoteViewManagedReferenceFactory;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;

import static org.jboss.as.ee.component.Attachments.EE_APPLICATION_DESCRIPTION;

/**
 * Implementation of {@link InjectionSource} responsible for finding a specific bean instance with a bean name and interface.
 *
 * @author John Bailey
 * @author Stuart Douglas
 */
public class EjbInjectionSource extends InjectionSource {
    private final String beanName;
    private final String typeName;
    private final String bindingName;
    private final DeploymentUnit deploymentUnit;
    private volatile String error = null;
    private volatile ServiceName resolvedViewName;
    private volatile RemoteViewManagedReferenceFactory remoteFactory;
    private volatile boolean resolved = false;

    public EjbInjectionSource(final String beanName, final String typeName, final String bindingName, final DeploymentUnit deploymentUnit) {
        this.beanName = beanName;
        this.typeName = typeName;
        this.bindingName = bindingName;
        this.deploymentUnit = deploymentUnit;
    }

    public EjbInjectionSource(final String typeName, final String bindingName, final DeploymentUnit deploymentUnit) {
        this.bindingName = bindingName;
        this.deploymentUnit = deploymentUnit;
        this.beanName = null;
        this.typeName = typeName;
    }

    public void getResourceValue(final ResolutionContext resolutionContext, final ServiceBuilder<?> serviceBuilder, final DeploymentPhaseContext phaseContext, final Injector<ManagedReferenceFactory> injector) throws DeploymentUnitProcessingException {
        resolve();

        if (error != null) {
            throw new DeploymentUnitProcessingException(error);
        }

        if (remoteFactory != null) {
            //because we are using the ejb: lookup namespace we do not need a dependency
            injector.inject(remoteFactory);
        } else {
            serviceBuilder.addDependency(resolvedViewName, ComponentView.class, new ViewManagedReferenceFactory.Injector(injector));
        }
    }

    /**
     * Checks if this ejb injection has been resolved yet, and if not resolves it.
     */
    private void resolve() {
        if (!resolved) {
            synchronized (this) {
                if (!resolved) {

                    final Set<ViewDescription> views = getViews();

                    final Set<EJBViewDescription> ejbsForViewName = new HashSet<EJBViewDescription>();
                    for (final ViewDescription view : views) {
                        if (view instanceof EJBViewDescription) {
                            ejbsForViewName.add((EJBViewDescription) view);
                        }
                    }


                    if (ejbsForViewName.isEmpty()) {
                        if (beanName == null) {
                            error = EjbMessages.MESSAGES.ejbNotFound(typeName, bindingName);
                        } else {
                            error = EjbMessages.MESSAGES.ejbNotFound(typeName, beanName, bindingName);
                        }
                    } else if (ejbsForViewName.size() > 1) {
                        if (beanName == null) {
                            error = EjbMessages.MESSAGES.moreThanOneEjbFound(typeName, bindingName, ejbsForViewName);
                        } else {
                            error = EjbMessages.MESSAGES.moreThanOneEjbFound(typeName, beanName, bindingName, ejbsForViewName);
                        }
                        error = "More than 1 component found for type '" + typeName + "' and bean name " + beanName + " for binding " + bindingName;
                    } else {
                        final EJBViewDescription description = ejbsForViewName.iterator().next();
                        final EJBViewDescription ejbViewDescription = (EJBViewDescription) description;
                        //for remote interfaces we do not want to use a normal binding
                        //we need to bind the remote proxy factory into JNDI instead to get the correct behaviour

                        if (ejbViewDescription.getMethodIntf() == MethodIntf.REMOTE || ejbViewDescription.getMethodIntf() == MethodIntf.HOME) {
                            final EJBComponentDescription componentDescription = (EJBComponentDescription) description.getComponentDescription();
                            final EEModuleDescription moduleDescription = componentDescription.getModuleDescription();
                            final String earApplicationName = moduleDescription.getEarApplicationName();
                            remoteFactory = new RemoteViewManagedReferenceFactory(earApplicationName, moduleDescription.getModuleName(), moduleDescription.getDistinctName(), componentDescription.getComponentName(), description.getViewClassName(), componentDescription.isStateful());
                        }
                        final ServiceName serviceName = description.getServiceName();
                        resolvedViewName = serviceName;
                    }
                    resolved = true;
                }
            }
        }
    }

    private Set<ViewDescription> getViews() {
        final EEApplicationDescription applicationDescription = deploymentUnit.getAttachment(EE_APPLICATION_DESCRIPTION);
        final ResourceRoot deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);
        final Set<ViewDescription> componentsForViewName;
        if (beanName != null) {
            componentsForViewName = applicationDescription.getComponents(beanName, typeName, deploymentRoot.getRoot());
        } else {
            componentsForViewName = applicationDescription.getComponentsForViewName(typeName);
        }
        return componentsForViewName;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof EjbInjectionSource))
            return false;

        resolve();

        if (error != null) {
            //we can't do a real equals comparison in this case, so just return false
            return false;
        }

        final EjbInjectionSource other = (EjbInjectionSource) o;
        return eq(typeName, other.typeName) && eq(resolvedViewName, other.resolvedViewName);
    }

    public int hashCode() {
        return typeName.hashCode();
    }

    private static boolean eq(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10352.java