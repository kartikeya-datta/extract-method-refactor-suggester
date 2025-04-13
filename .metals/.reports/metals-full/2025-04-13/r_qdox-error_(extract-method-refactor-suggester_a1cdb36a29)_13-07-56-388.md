error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4019.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4019.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4019.java
text:
```scala
c@@onfiguration.addViewInterceptor(new ImmediateInterceptorFactory(new TCCLInterceptor(componentConfiguration.getModuleClassLoader())), InterceptorOrder.View.TCCL_INTERCEPTOR);

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

package org.jboss.as.ee.managedbean.processors;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import javax.annotation.ManagedBean;

import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEApplicationClasses;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.TCCLInterceptor;
import org.jboss.as.ee.component.ViewConfiguration;
import org.jboss.as.ee.component.ViewConfigurator;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ee.component.deployers.EEResourceReferenceProcessorRegistry;
import org.jboss.as.ee.component.interceptors.InterceptorOrder;
import org.jboss.as.ee.managedbean.component.ManagedBeanAssociatingInterceptorFactory;
import org.jboss.as.ee.managedbean.component.ManagedBeanCreateInterceptorFactory;
import org.jboss.as.ee.managedbean.component.ManagedBeanDestroyInterceptorFactory;
import org.jboss.as.ee.managedbean.component.ManagedBeanResourceReferenceProcessor;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.invocation.ImmediateInterceptorFactory;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import static org.jboss.as.ee.EeLogger.ROOT_LOGGER;
import static org.jboss.as.ee.EeMessages.MESSAGES;

/**
 * Deployment unit processor responsible for scanning a deployment to find classes with {@code javax.annotation.ManagedBean} annotations.
 * Note:  This processor only supports JSR-316 compliant managed beans.  So it will not handle complimentary spec additions (ex. EJB).
 *
 * @author John E. Bailey
 */
public class ManagedBeanAnnotationProcessor implements DeploymentUnitProcessor {

    static final DotName MANAGED_BEAN_ANNOTATION_NAME = DotName.createSimple(ManagedBean.class.getName());

    /**
     * Check the deployment annotation index for all classes with the @ManagedBean annotation.  For each class with the
     * annotation, collect all the required information to create a managed bean instance, and attach it to the context.
     *
     * @param phaseContext the deployment unit context
     * @throws DeploymentUnitProcessingException
     *
     */
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final EEResourceReferenceProcessorRegistry registry = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.RESOURCE_REFERENCE_PROCESSOR_REGISTRY);
        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        final EEApplicationClasses applicationClasses = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_APPLICATION_CLASSES_DESCRIPTION);
        final CompositeIndex compositeIndex = deploymentUnit.getAttachment(Attachments.COMPOSITE_ANNOTATION_INDEX);
        if(compositeIndex == null) {
            return;
        }
        final List<AnnotationInstance> instances = compositeIndex.getAnnotations(MANAGED_BEAN_ANNOTATION_NAME);
        if (instances == null || instances.isEmpty()) {
            return;
        }

        for (AnnotationInstance instance : instances) {
            AnnotationTarget target = instance.target();
            if (!(target instanceof ClassInfo)) {
                throw MESSAGES.classOnlyAnnotation("@ManagedBean", target);
            }
            final ClassInfo classInfo = (ClassInfo) target;
            // skip if it's not a valid managed bean class
            if (!assertManagedBeanClassValidity(classInfo)) {
                continue;
            }
            final String beanClassName = classInfo.name().toString();

            // Get the managed bean name from the annotation
            final AnnotationValue nameValue = instance.value();
            final String beanName = nameValue == null || nameValue.asString().isEmpty() ? beanClassName : nameValue.asString();
            final ComponentDescription componentDescription = new ComponentDescription(beanName, beanClassName, moduleDescription, deploymentUnit.getServiceName());

            // Add the view
            ViewDescription viewDescription = new ViewDescription(componentDescription, beanClassName);
            viewDescription.getConfigurators().addFirst(new ViewConfigurator() {
                public void configure(final DeploymentPhaseContext context, final ComponentConfiguration componentConfiguration, final ViewDescription description, final ViewConfiguration configuration) throws DeploymentUnitProcessingException {
                    // Add MB association interceptors
                    final Object contextKey = new Object();
                    configuration.addClientPostConstructInterceptor(new ManagedBeanCreateInterceptorFactory(contextKey), InterceptorOrder.ClientPostConstruct.INSTANCE_CREATE);
                    final ManagedBeanAssociatingInterceptorFactory associatingInterceptorFactory = new ManagedBeanAssociatingInterceptorFactory(contextKey);
                    configuration.addClientInterceptor(associatingInterceptorFactory, InterceptorOrder.Client.ASSOCIATING_INTERCEPTOR);
                    configuration.addClientPreDestroyInterceptor(new ManagedBeanDestroyInterceptorFactory(contextKey), InterceptorOrder.ClientPreDestroy.INSTANCE_DESTROY);
                    configuration.addViewInterceptor(new ImmediateInterceptorFactory(new TCCLInterceptor(componentConfiguration.getModuleClassLoder())), InterceptorOrder.View.TCCL_INTERCEPTOR);
                }
            });
            viewDescription.getBindingNames().addAll(Arrays.asList("java:module/" + beanName, "java:app/" + moduleDescription.getModuleName() + "/" + beanName));
            componentDescription.getViews().add(viewDescription);
            moduleDescription.addComponent(componentDescription);

            // register a EEResourceReferenceProcessor which can process @Resource references to this managed bean.
            registry.registerResourceReferenceProcessor(new ManagedBeanResourceReferenceProcessor(beanClassName));
        }
    }

    public void undeploy(DeploymentUnit context) {
    }

    /**
     * Returns true if the passed <code>managedBeanClass</code> meets the requirements set by the Managed bean spec about
     * bean implementation classes. The passed <code>managedBeanClass</code> must not be an interface and must not be final or abstract.
     * If it passes these requirements then this method returns true. Else it returns false.
     *
     * @param managedBeanClass The session bean class
     * @return
     */
    private static boolean assertManagedBeanClassValidity(final ClassInfo managedBeanClass) {
        final short flags = managedBeanClass.flags();
        final String className = managedBeanClass.name().toString();
        // must *not* be a interface
        if (Modifier.isInterface(flags)) {
            ROOT_LOGGER.invalidManagedBeanInterface("MB.2.1.1", className);
            return false;
        }
        // bean class must *not* be abstract or final
        if (Modifier.isAbstract(flags) || Modifier.isFinal(flags)) {
            ROOT_LOGGER.invalidManagedBeanAbstractOrFinal("MB.2.1.1", className);
            return false;
        }
        // valid class
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4019.java