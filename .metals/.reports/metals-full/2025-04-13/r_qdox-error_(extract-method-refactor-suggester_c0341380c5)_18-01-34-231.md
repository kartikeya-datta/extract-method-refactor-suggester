error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1876.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1876.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1876.java
text:
```scala
i@@f (module == null || description == null) {

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
package org.jboss.as.ee.component.deployers;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.BindingConfiguration;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.DeploymentDescriptorEnvironment;
import org.jboss.as.ee.component.EEApplicationClasses;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.FieldInjectionTarget;
import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.ee.component.InjectionTarget;
import org.jboss.as.ee.component.InterceptorEnvironment;
import org.jboss.as.ee.component.MethodInjectionTarget;
import org.jboss.as.ee.component.ResourceInjectionConfiguration;
import org.jboss.as.ee.component.ResourceInjectionTarget;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.metadata.javaee.spec.ResourceInjectionMetaData;
import org.jboss.metadata.javaee.spec.ResourceInjectionTargetMetaData;
import org.jboss.modules.Module;

import static org.jboss.as.ee.EeMessages.MESSAGES;
import static org.jboss.as.ee.utils.InjectionUtils.getInjectionTarget;

/**
 * Class that provides common functionality required by processors that process environment information from deployment descriptors.
 *
 * @author Stuart Douglas
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
public abstract class AbstractDeploymentDescriptorBindingsProcessor implements DeploymentUnitProcessor {

    private static final Map<Class<?>, Class<?>> BOXED_TYPES;

    static {
        Map<Class<?>, Class<?>> types = new HashMap<Class<?>, Class<?>>();
        types.put(int.class, Integer.class);
        types.put(byte.class, Byte.class);
        types.put(short.class, Short.class);
        types.put(long.class, Long.class);
        types.put(char.class, Character.class);
        types.put(float.class, Float.class);
        types.put(double.class, Double.class);
        types.put(boolean.class, Boolean.class);

        BOXED_TYPES = Collections.unmodifiableMap(types);
    }

    @Override
    public final void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final DeploymentDescriptorEnvironment environment = deploymentUnit.getAttachment(Attachments.MODULE_DEPLOYMENT_DESCRIPTOR_ENVIRONMENT);
        final EEApplicationClasses applicationClasses = deploymentUnit.getAttachment(Attachments.EE_APPLICATION_CLASSES_DESCRIPTION);
        final Module module = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
        final DeploymentReflectionIndex deploymentReflectionIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);
        final EEModuleDescription description = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        if (description == null) {
            return;
        }

        if (environment != null) {
            final List<BindingConfiguration> bindings = processDescriptorEntries(deploymentUnit, environment, description, null, module.getClassLoader(), deploymentReflectionIndex, applicationClasses);
            description.getBindingConfigurations().addAll(bindings);
        }
        for (final ComponentDescription componentDescription : description.getComponentDescriptions()) {
            if (componentDescription.getDeploymentDescriptorEnvironment() != null) {
                final List<BindingConfiguration> bindings = processDescriptorEntries(deploymentUnit, componentDescription.getDeploymentDescriptorEnvironment(), componentDescription, componentDescription, module.getClassLoader(), deploymentReflectionIndex, applicationClasses);
                componentDescription.getBindingConfigurations().addAll(bindings);
            }
        }

        for(final InterceptorEnvironment interceptorEnv : description.getInterceptorEnvironment().values()) {
            final List<BindingConfiguration> bindings = processDescriptorEntries(deploymentUnit, interceptorEnv.getDeploymentDescriptorEnvironment(), interceptorEnv, null, module.getClassLoader(), deploymentReflectionIndex, applicationClasses);
            interceptorEnv.getBindingConfigurations().addAll(bindings);
        }
    }

    protected abstract List<BindingConfiguration> processDescriptorEntries(DeploymentUnit deploymentUnit, DeploymentDescriptorEnvironment environment, ResourceInjectionTarget resourceInjectionTarget, final ComponentDescription componentDescription, ClassLoader classLoader, DeploymentReflectionIndex deploymentReflectionIndex, final EEApplicationClasses applicationClasses) throws DeploymentUnitProcessingException;

    @Override
    public void undeploy(DeploymentUnit context) {
    }

    /**
     * Processes the injection targets of a resource binding
     *
     *
     * @param injectionSource           The injection source for the injection target
     * @param classLoader               The module class loader
     * @param deploymentReflectionIndex The deployment reflection index
     * @param entry                     The resource with injection targets
     * @param classType                 The expected type of the injection point, may be null if this is to be inferred from the injection target
     * @return The actual class type of the injection point
     * @throws DeploymentUnitProcessingException
     *          If the injection points could not be resolved
     */
    protected Class<?> processInjectionTargets(final ResourceInjectionTarget resourceInjectionTarget, InjectionSource injectionSource, ClassLoader classLoader, DeploymentReflectionIndex deploymentReflectionIndex, ResourceInjectionMetaData entry, Class<?> classType) throws DeploymentUnitProcessingException {
        if (entry.getInjectionTargets() != null) {
            for (ResourceInjectionTargetMetaData injectionTarget : entry.getInjectionTargets()) {
                final String injectionTargetClassName = injectionTarget.getInjectionTargetClass();
                final String injectionTargetName = injectionTarget.getInjectionTargetName();
                final AccessibleObject fieldOrMethod = getInjectionTarget(injectionTargetClassName, injectionTargetName, classLoader, deploymentReflectionIndex);
                final Class<?> injectionTargetType = fieldOrMethod instanceof Field ? ((Field) fieldOrMethod).getType() : ((Method) fieldOrMethod).getParameterTypes()[0];
                final String memberName = fieldOrMethod instanceof Field ? ((Field) fieldOrMethod).getName() : ((Method) fieldOrMethod).getName();

                if (classType != null) {
                    if (!injectionTargetType.isAssignableFrom(classType)) {
                        boolean ok = false;
                        if (classType.isPrimitive()) {
                            if (BOXED_TYPES.get(classType).equals(injectionTargetType)) {
                                ok = true;
                            }
                        } else if (injectionTargetType.isPrimitive()) {
                            if (BOXED_TYPES.get(injectionTargetType).equals(classType)) {
                                ok = true;
                            }
                        }
                        if (!ok) {
                            throw MESSAGES.invalidInjectionTarget(injectionTarget.getInjectionTargetName(), injectionTarget.getInjectionTargetClass(), classType);
                        }
                        classType = injectionTargetType;
                    }
                } else {
                    classType = injectionTargetType;
                }
                final InjectionTarget injectionTargetDescription = fieldOrMethod instanceof Field ?
                        new FieldInjectionTarget(injectionTargetClassName, memberName, classType.getName()) :
                        new MethodInjectionTarget(injectionTargetClassName, memberName, classType.getName());

                final ResourceInjectionConfiguration injectionConfiguration = new ResourceInjectionConfiguration(injectionTargetDescription, injectionSource);
                resourceInjectionTarget.addResourceInjection(injectionConfiguration);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1876.java