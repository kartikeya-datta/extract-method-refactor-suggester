error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8097.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8097.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8097.java
text:
```scala
p@@rocessInjectionTargets(moduleDescription, componentDescription, applicationClasses, valueSource, classLoader, deploymentReflectionIndex, serviceRefMD, serviceRefType);

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

package org.jboss.as.webservices.webserviceref;

import static org.jboss.as.ee.utils.InjectionUtils.getInjectionTarget;
import static org.jboss.as.webservices.util.ASHelper.getWSRefRegistry;
import static org.jboss.as.webservices.webserviceref.WSRefUtils.processAnnotatedElement;
import static org.jboss.as.webservices.webserviceref.WSRefUtils.translate;

import java.lang.reflect.AccessibleObject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jboss.as.ee.component.BindingConfiguration;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.DeploymentDescriptorEnvironment;
import org.jboss.as.ee.component.EEApplicationClasses;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.deployers.AbstractDeploymentDescriptorBindingsProcessor;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.as.webservices.util.VirtualFileAdaptor;
import org.jboss.metadata.javaee.spec.ResourceInjectionTargetMetaData;
import org.jboss.metadata.javaee.spec.ServiceReferenceMetaData;
import org.jboss.metadata.javaee.spec.ServiceReferencesMetaData;
import org.jboss.modules.Module;
import org.jboss.wsf.spi.deployment.UnifiedVirtualFile;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedServiceRefMetaData;

/**
 * WebServiceRef DD processor.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
public final class WSRefDDProcessor extends AbstractDeploymentDescriptorBindingsProcessor {

    @Override
    protected List<BindingConfiguration> processDescriptorEntries(final DeploymentUnit unit, final DeploymentDescriptorEnvironment environment, final EEModuleDescription moduleDescription, final ComponentDescription componentDescription, final ClassLoader classLoader, final DeploymentReflectionIndex deploymentReflectionIndex, final EEApplicationClasses applicationClasses) throws DeploymentUnitProcessingException {
        final ServiceReferencesMetaData serviceRefsMD = environment.getEnvironment().getServiceReferences();
        if (serviceRefsMD == null) {
            return Collections.<BindingConfiguration> emptyList();
        }

        final List<BindingConfiguration> bindingDescriptions = new LinkedList<BindingConfiguration>();
        for (final ServiceReferenceMetaData serviceRefMD : serviceRefsMD) {
            final UnifiedServiceRefMetaData serviceRefUMDM = getServiceRef(unit, serviceRefMD);
            final WSRefValueSource valueSource = new WSRefValueSource(serviceRefUMDM);
            final BindingConfiguration bindingConfiguration = new BindingConfiguration(serviceRefUMDM.getServiceRefName(), valueSource);
            bindingDescriptions.add(bindingConfiguration);
            final String serviceRefTypeName = serviceRefUMDM.getServiceRefType();
            final Class<?> serviceRefType = getClass(classLoader, serviceRefTypeName);
            processInjectionTargets(moduleDescription, applicationClasses, valueSource, classLoader, deploymentReflectionIndex, serviceRefMD, serviceRefType);
        }
        return bindingDescriptions;
    }

    private static UnifiedServiceRefMetaData getServiceRef(final DeploymentUnit unit, final ServiceReferenceMetaData serviceRefMD) throws DeploymentUnitProcessingException {
        // construct service ref
        final UnifiedServiceRefMetaData serviceRefUMDM = new UnifiedServiceRefMetaData(getUnifiedVirtualFile(unit));
        translate(serviceRefMD, serviceRefUMDM);
        processWSFeatures(unit, serviceRefMD.getInjectionTargets(), serviceRefUMDM);
        // register it
        final WSReferences wsRefRegistry = getWSRefRegistry(unit);
        wsRefRegistry.add(serviceRefMD.getName(), serviceRefUMDM);
        return serviceRefUMDM;
    }

    private static void processWSFeatures(final DeploymentUnit unit, final Set<ResourceInjectionTargetMetaData> injectionTargets, final UnifiedServiceRefMetaData serviceRefUMDM) throws DeploymentUnitProcessingException {
        if (injectionTargets == null || injectionTargets.size() == 0) return;
        if (injectionTargets.size() > 1) {
           // TODO: We should validate all the injection targets whether they're compatible.
           // This means all the injection targets must be assignable or equivalent.
           // If there are @Addressing, @RespectBinding or @MTOM annotations present on injection targets,
           // these annotations must be equivalent for all the injection targets.
        }
        final Module module = unit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
        final DeploymentReflectionIndex deploymentReflectionIndex = unit.getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);
        final ResourceInjectionTargetMetaData injectionTarget = injectionTargets.iterator().next();
        final String injectionTargetClassName = injectionTarget.getInjectionTargetClass();
        final String injectionTargetName = injectionTarget.getInjectionTargetName();
        final AccessibleObject fieldOrMethod = getInjectionTarget(injectionTargetClassName, injectionTargetName, module.getClassLoader(), deploymentReflectionIndex);
        processAnnotatedElement(fieldOrMethod, serviceRefUMDM);
    }

    private Class<?> getClass(final ClassLoader classLoader, final String className) throws DeploymentUnitProcessingException { // TODO: refactor to common code
        if (!isEmpty(className)) {
            try {
                return classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                throw new DeploymentUnitProcessingException("Could not load class " + className, e);
            }
        }

        return null;
    }

    private static UnifiedVirtualFile getUnifiedVirtualFile(final DeploymentUnit deploymentUnit) { // TODO: refactor to common code
        final ResourceRoot resourceRoot = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.DEPLOYMENT_ROOT);
        return new VirtualFileAdaptor(resourceRoot.getRoot());
    }

    private static boolean isEmpty(final String string) { // TODO: some common class - StringUtils ?
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8097.java