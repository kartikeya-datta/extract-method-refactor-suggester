error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1728.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1728.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1728.java
text:
```scala
i@@njectionTargetDescription.setDeclaredValueClassName(classType.getName());

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.metadata.javaee.spec.ResourceInjectionTargetMetaData;
import org.jboss.metadata.javaee.support.ResourceInjectionMetaDataWithDescriptions;
import org.jboss.modules.Module;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Class that provides common functionality required by processors that process environment information from deployment descriptors.
 *
 * @author Stuart Douglas
 */
public abstract class AbstractDeploymentDescriptorBindingsProcessor  implements DeploymentUnitProcessor {

    @Override
    public final void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final DeploymentDescriptorEnvironment environment = deploymentUnit.getAttachment(Attachments.MODULE_DEPLOYMENT_DESCRIPTOR_ENVIRONMENT);
        final Module module = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
        final DeploymentReflectionIndex deploymentReflectionIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);
        final EEModuleDescription description = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        if(description == null) {
            return;
        }
        if(environment != null) {
            List<BindingDescription> bindings = processDescriptorEntries(deploymentUnit, environment, description, null, module.getClassLoader(), deploymentReflectionIndex);
            description.getBindingsContainer().addBindings(bindings);
        }
        for(final AbstractComponentDescription componentDescription : description.getComponentDescriptions()) {
            if(componentDescription.getDeploymentDescriptorEnvironment() != null) {
                List<BindingDescription> bindings = processDescriptorEntries(deploymentUnit, componentDescription.getDeploymentDescriptorEnvironment(), description, null, module.getClassLoader(), deploymentReflectionIndex);
                componentDescription.addBindings(bindings);
            }
        }
    }

    protected abstract List<BindingDescription> processDescriptorEntries(DeploymentUnit deploymentUnit, DeploymentDescriptorEnvironment environment, EEModuleDescription moduleDescription, AbstractComponentDescription componentDescription, ClassLoader classLoader, DeploymentReflectionIndex deploymentReflectionIndex) throws DeploymentUnitProcessingException;

    @Override
    public void undeploy(DeploymentUnit context) {
    }

    /**
     * Processes the injection targets of a resource binding
     *
     * @param classLoader The module class loader
     * @param deploymentReflectionIndex The deployment reflection index
     * @param entry The resource with injection targets
     * @param description The binding description
     * @param classType The expected type of the injection point, may be null if this is to be inferred from the injection target
     * @return The actual class type of the injection point
     * @throws DeploymentUnitProcessingException If the injection points could not be resolved
     */
    protected Class<?> processInjectionTargets(ClassLoader classLoader, DeploymentReflectionIndex deploymentReflectionIndex, ResourceInjectionMetaDataWithDescriptions entry, BindingDescription description, Class<?> classType) throws DeploymentUnitProcessingException {
        if(entry.getInjectionTargets() != null) {
            for(ResourceInjectionTargetMetaData injectionTarget : entry.getInjectionTargets()) {

                final Class<?> injectionTargetClass;
                try {
                    injectionTargetClass = classLoader.loadClass(injectionTarget.getInjectionTargetClass());
                } catch (ClassNotFoundException e) {
                    throw new DeploymentUnitProcessingException("Could not load " + injectionTarget.getInjectionTargetClass() + " referenced in env-entry injection point ",e);
                }
                final ClassReflectionIndex<?> index = deploymentReflectionIndex.getClassIndex(injectionTargetClass);
                String methodName = "set" + injectionTarget.getInjectionTargetName().substring(0,1).toUpperCase() + injectionTarget.getInjectionTargetName().substring(1);

                boolean methodFound = false;
                Method method = null;
                Field field = null;
                Class<?> injectionTargetType = null;
                String memberName = injectionTarget.getInjectionTargetName();
                Class<?> current = injectionTargetClass;
                while(current != Object.class && current != null && !methodFound) {
                    final Collection<Method> methods = index.getAllMethods(methodName);
                    for(Method m : methods) {
                        if(m.getParameterTypes().length == 1) {
                            if(m.isBridge() || m.isSynthetic()) {
                                continue;
                            }
                            if(methodFound) {
                                throw new DeploymentUnitProcessingException("Two setter methods for " + injectionTarget.getInjectionTargetName() + " on class " + injectionTarget.getInjectionTargetClass() + " found when applying <injection-target> for env-entry");
                            }
                            methodFound = true;
                            method = m;
                            injectionTargetType = m.getParameterTypes()[0];
                            memberName = methodName;
                        }
                    }
                    current = current.getSuperclass();
                }
                if(method == null) {
                    current = injectionTargetClass;
                    while(current != Object.class && current != null && field == null) {
                        field = index.getField(injectionTarget.getInjectionTargetName());
                        if(field != null) {
                            injectionTargetType = field.getType();
                            memberName = injectionTarget.getInjectionTargetName();
                            break;
                        }
                        current = current.getSuperclass();
                    }
                }
                if(field == null && method == null) {
                    throw new DeploymentUnitProcessingException("Could not resolve injection point " + injectionTarget.getInjectionTargetName() + " on class " + injectionTarget.getInjectionTargetClass() + " specified in web.xml");
                }
                if(classType != null) {
                    if(!classType.isAssignableFrom(injectionTargetType)) {
                         throw new DeploymentUnitProcessingException("Injection target " + injectionTarget.getInjectionTargetName() + " on class " + injectionTarget.getInjectionTargetClass() + " is not compatible with the type of injection");
                    }
                } else {
                    classType = injectionTargetType;
                }
                final InjectionTargetDescription injectionTargetDescription = new InjectionTargetDescription();
                injectionTargetDescription.setClassName(injectionTarget.getInjectionTargetClass());
                if(method == null) {
                    injectionTargetDescription.setType(InjectionTargetDescription.Type.FIELD);
                } else {
                    injectionTargetDescription.setType(InjectionTargetDescription.Type.METHOD);
                }
                injectionTargetDescription.setName(memberName);
                injectionTargetDescription.setValueClassName(classType.getName());
                description.getInjectionTargetDescriptions().add(injectionTargetDescription);
            }
        }
        return classType;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1728.java