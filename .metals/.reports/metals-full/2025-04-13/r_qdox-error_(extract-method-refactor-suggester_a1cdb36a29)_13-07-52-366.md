error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/117.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/117.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/117.java
text:
```scala
f@@or(AnnotationInstance annotation : excludeClassAnnotations) {

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
import java.util.List;
import java.util.Map;

/**
 * Deployment processor responsible for analyzing each attached {@link AbstractComponentDescription} instance to configure
 * required method interceptors.
 * <p>
 * This class does not process annotations on interceptors themselves, this is handled by the {@link InterceptorAnnotationParsingProcessor}
 *
 * //TODO: We need to figure out how deployment descriptors are going to override this
 * @author John Bailey
 * @author Stuart Douglas
 */
public class ComponentInterceptorAnnotationParsingProcessor extends AbstractComponentConfigProcessor {
    private static final DotName INTERCEPTORS_ANNOTATION_NAME = DotName.createSimple(Interceptors.class.getName());
    private static final DotName EXCLUDE_DEFAULT_ANNOTATION_NAME = DotName.createSimple(ExcludeDefaultInterceptors.class.getName());
    private static final DotName EXCLUDE_CLASS_ANNOTATION_NAME = DotName.createSimple(ExcludeClassInterceptors.class.getName());

    /**
     * {@inheritDoc} *
     */
    protected void processComponentConfig(final DeploymentUnit deploymentUnit, final DeploymentPhaseContext phaseContext, final CompositeIndex index, final AbstractComponentDescription componentConfiguration) throws DeploymentUnitProcessingException {
        final ClassInfo classInfo = index.getClassByName(DotName.createSimple(componentConfiguration.getComponentClassName()));
        if (classInfo == null) {
            return; // We can't continue without the annotation index info.
        }
        processInterceptorConfigs(classInfo,classInfo.name().toString(), index, componentConfiguration,true);
    }

    private void processInterceptorConfigs(final ClassInfo classInfo, final String actualClassName, final CompositeIndex index, final AbstractComponentDescription componentConfiguration, boolean actualClass) throws DeploymentUnitProcessingException {
        final ClassInfo superClassInfo = index.getClassByName(classInfo.superName());
        final String className = classInfo.name().toString();
        if (superClassInfo != null) {
            processInterceptorConfigs(superClassInfo,actualClassName, index, componentConfiguration,false);
        }

        final Map<DotName, List<AnnotationInstance>> classAnnotations = classInfo.annotations();
        if (classAnnotations == null) {
            return;
        }

        //process the ExcludeDefaultInterceptors annotation
        final List<AnnotationInstance> excludeDefaultAnnotations = classAnnotations.get(EXCLUDE_DEFAULT_ANNOTATION_NAME);
        if(excludeDefaultAnnotations != null ) {
            for(AnnotationInstance annotation : excludeDefaultAnnotations) {
                final AnnotationTarget target = annotation.target();
                if (target instanceof MethodInfo) {
                    final MethodInfo methodInfo = MethodInfo.class.cast(target);
                    final MethodIdentifier methodIdentifier = methodIdentifierFromMethodInfo(methodInfo);
                    componentConfiguration.excludeDefaultInterceptors(methodIdentifier);
                } else {
                    componentConfiguration.setExcludeDefaultInterceptors(true);
                }
            }
        }

        //process the ExcludeClassInterceptors annotation
        final List<AnnotationInstance> excludeClassAnnotations = classAnnotations.get(EXCLUDE_CLASS_ANNOTATION_NAME);
        if(excludeClassAnnotations != null ) {
            for(AnnotationInstance annotation : excludeDefaultAnnotations) {
                final AnnotationTarget target = annotation.target();
                if (target instanceof MethodInfo) {
                    final MethodInfo methodInfo = MethodInfo.class.cast(target);
                    final MethodIdentifier methodIdentifier = methodIdentifierFromMethodInfo(methodInfo);
                    componentConfiguration.excludeClassInterceptors(methodIdentifier);
                } else {
                    throw new DeploymentUnitProcessingException("ExcludeClassInterceptors not applied to method: " + target);
                }
            }
        }

        final List<AnnotationInstance> interceptorAnnotations = classAnnotations.get(INTERCEPTORS_ANNOTATION_NAME);
        if (interceptorAnnotations == null || interceptorAnnotations.isEmpty()) {
            return;
        }

        for (AnnotationInstance annotationInstance : interceptorAnnotations) {

            final AnnotationValue value = annotationInstance.value();
            if (value != null) for (Type interceptorClass : value.asClassArray()) {
                final ClassInfo interceptorClassInfo = index.getClassByName(interceptorClass.name());
                if (interceptorClassInfo == null) {
                    continue; // TODO: Process without index info
                }

                final AnnotationTarget target = annotationInstance.target();
                if (target instanceof MethodInfo) {
                    final MethodInfo methodInfo = MethodInfo.class.cast(target);
                    componentConfiguration.addMethodInterceptor(methodIdentifierFromMethodInfo(methodInfo), new InterceptorDescription(interceptorClassInfo.name().toString()));
                } else {
                    //we do not process @Interceptors on the superclass
                    if(actualClass) {
                        componentConfiguration.addClassInterceptor(new InterceptorDescription(interceptorClassInfo.name().toString()));
                    }
                }
            }
        }
    }

    private static MethodIdentifier methodIdentifierFromMethodInfo(MethodInfo methodInfo) {
        final String[] argTypes = new String[methodInfo.args().length];
        int i = 0;
        for (Type argType : methodInfo.args()) {
            argTypes[i++] = argType.name().toString();
        }
        return MethodIdentifier.getIdentifier(methodInfo.returnType().name().toString(), methodInfo.name(), argTypes);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/117.java