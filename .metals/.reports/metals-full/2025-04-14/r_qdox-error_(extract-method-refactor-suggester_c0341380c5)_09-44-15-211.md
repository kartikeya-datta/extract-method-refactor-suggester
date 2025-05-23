error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1438.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1438.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1438.java
text:
```scala
i@@f (!descriptions.isEmpty()) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.ee.component;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.invocation.proxy.MethodIdentifier;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.Type;

import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.Interceptors;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Deployment processor responsible for analyzing the annotation index to find all @Interceptors, @ExcludeDefaultInterceptors and
 *
 * @author John Bailey
 */
public class InterceptorsAnnotationParsingProcessor implements DeploymentUnitProcessor {
    private static final DotName INTERCEPTORS_ANNOTATION_NAME = DotName.createSimple(Interceptors.class.getName());
    private static final DotName EXCLUDE_DEFAULT_ANNOTATION_NAME = DotName.createSimple(ExcludeDefaultInterceptors.class.getName());
    private static final DotName EXCLUDE_CLASS_ANNOTATION_NAME = DotName.createSimple(ExcludeClassInterceptors.class.getName());

    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        final CompositeIndex index = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.COMPOSITE_ANNOTATION_INDEX);

        final List<AnnotationInstance> interceptors = index.getAnnotations(INTERCEPTORS_ANNOTATION_NAME);
        for (AnnotationInstance annotation : interceptors) {
            processInterceptors(eeModuleDescription, annotation, index);
        }

        final List<AnnotationInstance> excludeDefaults = index.getAnnotations(EXCLUDE_DEFAULT_ANNOTATION_NAME);
        for (AnnotationInstance annotation : excludeDefaults) {
            processExcludeDefault(eeModuleDescription, annotation, index);
        }

        final List<AnnotationInstance> excludeClasses = index.getAnnotations(EXCLUDE_CLASS_ANNOTATION_NAME);
        for (AnnotationInstance annotation : excludeClasses) {
            processExcludeClass(eeModuleDescription, annotation, index);
        }
    }

    private void processInterceptors(final EEModuleDescription eeModuleDescription, final AnnotationInstance annotation, final CompositeIndex index) throws DeploymentUnitProcessingException {
        final AnnotationTarget target = annotation.target();
        if (target instanceof MethodInfo) {
            processMethodInterceptor(eeModuleDescription, MethodInfo.class.cast(target), annotation, index);
        } else if (target instanceof ClassInfo) {
            processClassInterceptor(eeModuleDescription, ClassInfo.class.cast(target), annotation, index);
        } else {
            throw new DeploymentUnitProcessingException("@Interceptors annotation is only allowed on methods and classes");
        }
    }

    private void processMethodInterceptor(final EEModuleDescription eeModuleDescription, final MethodInfo methodInfo, final AnnotationInstance annotation, final CompositeIndex index) {
        final Collection<ComponentDescription> components = this.getApplicableComponents(index, methodInfo.declaringClass(), eeModuleDescription);
        final AnnotationValue value = annotation.value();
        if (value != null) for (Type interceptorClass : value.asClassArray()) {
            for (ComponentDescription component : components) {
                component.addMethodInterceptor(methodIdentifierFromMethodInfo(methodInfo), new InterceptorDescription(interceptorClass.name().toString()));
            }
        }
    }

    private void processClassInterceptor(final EEModuleDescription eeModuleDescription, final ClassInfo classInfo, final AnnotationInstance annotation, final CompositeIndex index) {
        final Collection<ComponentDescription> components = this.getApplicableComponents(index, classInfo, eeModuleDescription);

        final AnnotationValue value = annotation.value();
        if (value != null) for (Type interceptorClass : value.asClassArray()) {
            for (ComponentDescription component : components) {
                component.addClassInterceptor(new InterceptorDescription(interceptorClass.name().toString()));
            }
        }
    }

    private void processExcludeDefault(final EEModuleDescription eeModuleDescription, final AnnotationInstance annotation, final CompositeIndex index) throws DeploymentUnitProcessingException {
        final AnnotationTarget target = annotation.target();
        if (target instanceof MethodInfo) {
            processMethodExcludeDefault(eeModuleDescription, MethodInfo.class.cast(target), index);
        } else if (target instanceof ClassInfo) {
            processClassExcludeDefault(eeModuleDescription, ClassInfo.class.cast(target), index);
        } else {
            throw new DeploymentUnitProcessingException("@ExcludeDefaultInterceptors annotation is only allowed on methods and classes");
        }
    }

    private void processClassExcludeDefault(EEModuleDescription eeModuleDescription, final ClassInfo classInfo, final CompositeIndex index) {
        final Collection<ComponentDescription> components = this.getApplicableComponents(index, classInfo, eeModuleDescription);
        for (ComponentDescription component : components) {
            component.setExcludeDefaultInterceptors(true);
        }
    }

    private void processMethodExcludeDefault(EEModuleDescription eeModuleDescription, MethodInfo methodInfo, final CompositeIndex index) {
        final Collection<ComponentDescription> components = this.getApplicableComponents(index, methodInfo.declaringClass(), eeModuleDescription);
        for (ComponentDescription component : components) {
            component.excludeDefaultInterceptors(methodIdentifierFromMethodInfo(methodInfo));
        }
    }

    private void processExcludeClass(final EEModuleDescription eeModuleDescription, final AnnotationInstance annotation, final CompositeIndex index) throws DeploymentUnitProcessingException {
        final AnnotationTarget target = annotation.target();
        if (target instanceof MethodInfo) {
            final MethodInfo methodInfo = MethodInfo.class.cast(target);
            final Collection<ComponentDescription> components = this.getApplicableComponents(index, methodInfo.declaringClass(), eeModuleDescription);
            for (ComponentDescription component : components) {
                component.excludeClassInterceptors(methodIdentifierFromMethodInfo(methodInfo));
            }
        } else {
            throw new DeploymentUnitProcessingException("@ExcludeDefaultInterceptors annotation is only allowed on methods");
        }
    }

    public void undeploy(DeploymentUnit context) {
    }

    private static MethodIdentifier methodIdentifierFromMethodInfo(MethodInfo methodInfo) {
        final String[] argTypes = new String[methodInfo.args().length];
        int i = 0;
        for (Type argType : methodInfo.args()) {
            argTypes[i++] = argType.name().toString();
        }
        return MethodIdentifier.getIdentifier(methodInfo.returnType().name().toString(), methodInfo.name(), argTypes);
    }

    /**
     * Returns the applicable components for the passed {@link ClassInfo klass}. If the passed <code>klass</code> represents
     * a component in the passed <code>eeModuleDescription</code>, then a collection containing only that {@link ComponentDescription component}
     * is returned. Else the passed <code>index</code> is used to find any known subclasses of the <code>klass</code> and then return a collection
     * of {@link ComponentDescription components} (if any), corresponding to those subclasses.
     * If there are no {@link ComponentDescription components} found for the passed <code>klass</code>, then this method returns an empty
     * collection.
     *
     * @param index               The {@link CompositeIndex}
     * @param klass               The {@link ClassInfo} for which {@link ComponentDescription components} are being queried
     * @param eeModuleDescription The {@link EEModuleDescription} to which the <code>klass</code> belongs
     * @return
     */
    private Collection<ComponentDescription> getApplicableComponents(final CompositeIndex index, final ClassInfo klass, final EEModuleDescription eeModuleDescription) {
        Set<ComponentDescription> componentDescriptions = new HashSet<ComponentDescription>();
        final List<ComponentDescription> descriptions = eeModuleDescription.getComponentsByClassName(klass.name().toString());
        if (componentDescriptions.isEmpty()) {
            componentDescriptions.addAll(descriptions);
        } else {
            componentDescriptions.addAll(this.getKnownSubClassComponents(index, klass, eeModuleDescription));
        }
        return componentDescriptions;
    }

    /**
     * Returns a collection of {@link ComponentDescription components} corresponding to each of the known subclasses of the
     * passed <code>superClass</code>. If a particular subclass isn't a component, then it is not added to the collection. A
     * subclass is considered as a component, if it is mapped as such in the passed <code>eeModuledescription</code>
     *
     * @param index               The {@link CompositeIndex}
     * @param superClass          The {@link ClassInfo} whose subclasses will be checked for {@link ComponentDescription components}
     * @param eeModuleDescription The {@link EEModuleDescription} to which the <code>superClass</code> belongs
     * @return
     */
    private Collection<ComponentDescription> getKnownSubClassComponents(final CompositeIndex index, final ClassInfo superClass, final EEModuleDescription eeModuleDescription) {
        Set<ClassInfo> subClasses = index.getAllKnownSubclasses(superClass.name());
        if (subClasses == null || subClasses.isEmpty()) {
            return Collections.emptySet();
        }
        Set<ComponentDescription> components = new HashSet<ComponentDescription>();
        for (ClassInfo subClass : subClasses) {
            final List<ComponentDescription> componentDescriptions = eeModuleDescription.getComponentsByClassName(subClass.name().toString());
            components.addAll(componentDescriptions);
        }
        return components;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1438.java