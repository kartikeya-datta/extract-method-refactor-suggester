error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10806.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10806.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10806.java
text:
```scala
final C@@lassInfo classInfo = index.getClassByName(DotName.createSimple(componentConfiguration.getComponentClassName()));

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

package org.jboss.as.ee.component.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.interceptor.MethodInterceptorAllFilter;
import org.jboss.as.ee.component.interceptor.MethodInterceptorConfiguration;
import org.jboss.as.ee.component.interceptor.MethodInterceptorMatchFilter;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.Type;

/**
 * Deployment processor responsible for analyzing each attached {@link org.jboss.as.ee.component.ComponentConfiguration} instance to configure
 * required method interceptors.
 *
 * @author John Bailey
 */
public class InterceptorAnnotationParsingProcessor extends AbstractComponentConfigProcessor {
    private static final DotName INTERCEPTORS_ANNOTATION_NAME = DotName.createSimple(Interceptors.class.getName());
    private static final DotName AROUND_INVOKE_ANNOTATION_NAME = DotName.createSimple(AroundInvoke.class.getName());

    /** {@inheritDoc} **/
    protected void processComponentConfig(final DeploymentUnit deploymentUnit, final DeploymentPhaseContext phaseContext, final CompositeIndex index, final ComponentConfiguration componentConfiguration) {
        final ClassInfo classInfo = index.getClassByName(DotName.createSimple(componentConfiguration.getBeanClass()));
        componentConfiguration.addMethodInterceptorConfigs(getInterceptorConfigs(classInfo, index));
    }

    private List<MethodInterceptorConfiguration> getInterceptorConfigs(final ClassInfo classInfo, final CompositeIndex index) {
        final List<MethodInterceptorConfiguration> interceptorConfigurations = new ArrayList<MethodInterceptorConfiguration>();
        final List<MethodInterceptorConfiguration> methodLevelInterceptorConfigurations = new ArrayList<MethodInterceptorConfiguration>();
        final List<MethodInterceptorConfiguration> componentDefinedInterceptors = new ArrayList<MethodInterceptorConfiguration>();
        getInterceptorConfigs(classInfo, index, interceptorConfigurations, methodLevelInterceptorConfigurations, componentDefinedInterceptors);
        interceptorConfigurations.addAll(methodLevelInterceptorConfigurations);
        interceptorConfigurations.addAll(componentDefinedInterceptors);
        return interceptorConfigurations;
    }

    private void getInterceptorConfigs(final ClassInfo classInfo, final CompositeIndex index, final List<MethodInterceptorConfiguration> classLevelInterceptorConfigurations, final List<MethodInterceptorConfiguration> methodLevelInterceptorConfigurations, final List<MethodInterceptorConfiguration> componentDefinedInterceptors) {
        final ClassInfo superClassInfo = index.getClassByName(classInfo.superName());
        if (superClassInfo != null) {
            getInterceptorConfigs(superClassInfo, index, classLevelInterceptorConfigurations, methodLevelInterceptorConfigurations, componentDefinedInterceptors);
        }

        final Map<DotName, List<AnnotationInstance>> classAnnotations = classInfo.annotations();
        if (classAnnotations == null) {
            return;
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

                final MethodInfo aroundInvokeMethod = getAroundInvokeMethod(interceptorClassInfo);
                validateArgumentType(classInfo, aroundInvokeMethod);

                final AnnotationTarget target = annotationInstance.target();
                if (target instanceof MethodInfo) {
                    final MethodInfo methodInfo = MethodInfo.class.cast(target);
                    final List<String> argTypes = new ArrayList<String>(methodInfo.args().length);
                    for (Type argType : methodInfo.args()) {
                        argTypes.add(argType.name().toString());
                    }
                    methodLevelInterceptorConfigurations.add(new MethodInterceptorConfiguration(interceptorClassInfo.name().toString(), aroundInvokeMethod.name(), new MethodInterceptorMatchFilter(methodInfo.name(), argTypes.toArray(new String[argTypes.size()]))));
                } else {
                    classLevelInterceptorConfigurations.add(new MethodInterceptorConfiguration(interceptorClassInfo.name().toString(), aroundInvokeMethod.name(), MethodInterceptorAllFilter.INSTANCE));
                }
            }
        }

        //Look for any @AroundInvoke methods on bean class
        final MethodInfo methodInfo = getAroundInvokeMethod(classInfo);
        if (methodInfo != null) {
            componentDefinedInterceptors.add(new MethodInterceptorConfiguration(classInfo.name().toString(), methodInfo.name(), MethodInterceptorAllFilter.INSTANCE));
        }
    }

    private MethodInfo getAroundInvokeMethod(final ClassInfo classInfo) {
        final Map<DotName, List<AnnotationInstance>> classAnnotations = classInfo.annotations();
        final List<AnnotationInstance> instances = classAnnotations.get(AROUND_INVOKE_ANNOTATION_NAME);
        if (instances == null || instances.isEmpty()) {
            return null;
        }

        if (instances.size() > 1) {
            throw new IllegalArgumentException("Only one method may be annotated with " + AROUND_INVOKE_ANNOTATION_NAME + " per interceptor.");
        }

        final AnnotationTarget target = instances.get(0).target();
        if (!(target instanceof MethodInfo)) {
            throw new IllegalArgumentException(AROUND_INVOKE_ANNOTATION_NAME + " is only valid on method targets.");
        }
        return MethodInfo.class.cast(target);
    }

    private void validateArgumentType(final ClassInfo classInfo, final MethodInfo methodInfo) {
        final Type[] args = methodInfo.args();
        switch (args.length) {
            case 0:
                throw new IllegalArgumentException("Invalid argument signature.  Methods annotated with " + AROUND_INVOKE_ANNOTATION_NAME + " must have a single InvocationContext argument.");
            case 1:
                if (!InvocationContext.class.getName().equals(args[0].name().toString())) {
                    throw new IllegalArgumentException("Invalid argument type.  Methods annotated with " + AROUND_INVOKE_ANNOTATION_NAME + " must have a single InvocationContext argument.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid number of arguments for method " + methodInfo.name() + " annotated with " + AROUND_INVOKE_ANNOTATION_NAME + " on class " + classInfo.name());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10806.java