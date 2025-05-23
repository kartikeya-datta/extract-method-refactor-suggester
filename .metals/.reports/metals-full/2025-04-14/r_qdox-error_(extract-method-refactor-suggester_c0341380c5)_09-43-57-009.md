error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11398.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11398.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11398.java
text:
```scala
i@@f (prePassivates != null) {

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
package org.jboss.as.ejb3.deployment.processors.dd;

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.interceptors.InterceptorClassDescription;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.component.messagedriven.MessageDrivenComponentDescription;
import org.jboss.as.ejb3.component.stateless.StatelessComponentDescription;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.reflect.ClassIndex;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndexUtil;
import org.jboss.as.server.deployment.reflect.DeploymentClassIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.invocation.proxy.MethodIdentifier;
import org.jboss.metadata.ejb.spec.AroundInvokeMetaData;
import org.jboss.metadata.ejb.spec.AroundInvokesMetaData;
import org.jboss.metadata.ejb.spec.EnterpriseBeanMetaData;
import org.jboss.metadata.ejb.spec.MessageDrivenBeanMetaData;
import org.jboss.metadata.ejb.spec.SessionBeanMetaData;
import org.jboss.metadata.javaee.spec.LifecycleCallbackMetaData;
import org.jboss.metadata.javaee.spec.LifecycleCallbacksMetaData;

import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

/**
 * Deployment descriptor that resolves interceptor methods definined in ejb-jar.xml that could not be resolved at
 * DD parse time.
 *
 * @author Stuart Douglas
 */
public class DeploymentDescriptorMethodProcessor implements DeploymentUnitProcessor {
    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        final DeploymentReflectionIndex reflectionIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);
        final DeploymentClassIndex classIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.CLASS_INDEX);

        if (eeModuleDescription != null) {
            for (final ComponentDescription component : eeModuleDescription.getComponentDescriptions()) {
                if (component instanceof EJBComponentDescription) {
                    try {
                        handleSessionBean((EJBComponentDescription) component, classIndex, reflectionIndex);
                        if (component instanceof StatelessComponentDescription || component instanceof MessageDrivenComponentDescription) {
                            handleStatelessSessionBean((EJBComponentDescription) component, classIndex, reflectionIndex);
                        }
                    } catch (ClassNotFoundException e) {
                        throw MESSAGES.failToLoadComponentClass(e);
                    }
                }
            }
        }


    }

    /**
     * Handles setting up the ejbCreate and ejbRemove methods  for stateless session beans and MDB's
     *
     * @param component       The component
     * @param classIndex      The class index
     * @param reflectionIndex The reflection index
     */
    private void handleStatelessSessionBean(final EJBComponentDescription component, final DeploymentClassIndex classIndex, final DeploymentReflectionIndex reflectionIndex) throws ClassNotFoundException, DeploymentUnitProcessingException {

        final ClassIndex componentClass = classIndex.classIndex(component.getComponentClassName());
        final MethodIdentifier ejbCreateId = MethodIdentifier.getIdentifier(void.class, "ejbCreate");
        final Method ejbCreate = ClassReflectionIndexUtil.findMethod(reflectionIndex, componentClass.getModuleClass(), ejbCreateId);
        if (ejbCreate != null) {
            final InterceptorClassDescription.Builder builder = InterceptorClassDescription.builder();
            builder.setPostConstruct(ejbCreateId);
            component.addInterceptorMethodOverride(ejbCreate.getDeclaringClass().getName(), builder.build());
        }
        final MethodIdentifier ejbRemoveId = MethodIdentifier.getIdentifier(void.class, "ejbRemove");
        final Method ejbRemove = ClassReflectionIndexUtil.findMethod(reflectionIndex, componentClass.getModuleClass(), ejbRemoveId);
        if (ejbRemove != null) {
            final InterceptorClassDescription.Builder builder = InterceptorClassDescription.builder();
            builder.setPreDestroy(ejbRemoveId);
            component.addInterceptorMethodOverride(ejbRemove.getDeclaringClass().getName(), builder.build());
        }
    }

    private void handleSessionBean(final EJBComponentDescription component, final DeploymentClassIndex classIndex, final DeploymentReflectionIndex reflectionIndex) throws ClassNotFoundException, DeploymentUnitProcessingException {

        if (component.getDescriptorData() == null) {
            return;
        }
        final ClassIndex componentClass = classIndex.classIndex(component.getComponentClassName());

        final EnterpriseBeanMetaData metaData = component.getDescriptorData();

        AroundInvokesMetaData aroundInvokes = null;
        if (metaData instanceof SessionBeanMetaData) {
            aroundInvokes = ((SessionBeanMetaData) metaData).getAroundInvokes();
        } else if (metaData instanceof MessageDrivenBeanMetaData) {
            aroundInvokes = ((MessageDrivenBeanMetaData) metaData).getAroundInvokes();
        }

        if (aroundInvokes != null) {
            for (AroundInvokeMetaData aroundInvoke : aroundInvokes) {
                final InterceptorClassDescription.Builder builder = InterceptorClassDescription.builder();
                String methodName = aroundInvoke.getMethodName();
                MethodIdentifier methodIdentifier = MethodIdentifier.getIdentifier(Object.class, methodName, InvocationContext.class);
                builder.setAroundInvoke(methodIdentifier);
                if (aroundInvoke.getClassName() == null || aroundInvoke.getClassName().isEmpty()) {
                    final String className = ClassReflectionIndexUtil.findRequiredMethod(reflectionIndex, componentClass.getModuleClass(), methodIdentifier).getDeclaringClass().getName();
                    component.addInterceptorMethodOverride(className, builder.build());
                } else {
                    component.addInterceptorMethodOverride(aroundInvoke.getClassName(), builder.build());
                }
            }
        }

        // post-construct(s) of the interceptor configured (if any) in the deployment descriptor
        LifecycleCallbacksMetaData postConstructs = metaData.getPostConstructs();
        if (postConstructs != null) {
            for (LifecycleCallbackMetaData postConstruct : postConstructs) {
                final InterceptorClassDescription.Builder builder = InterceptorClassDescription.builder();
                String methodName = postConstruct.getMethodName();
                MethodIdentifier methodIdentifier = MethodIdentifier.getIdentifier(void.class, methodName);
                builder.setPostConstruct(methodIdentifier);
                if (postConstruct.getClassName() == null || postConstruct.getClassName().isEmpty()) {
                    final String className = ClassReflectionIndexUtil.findRequiredMethod(reflectionIndex, componentClass.getModuleClass(), methodIdentifier).getDeclaringClass().getName();
                    component.addInterceptorMethodOverride(className, builder.build());
                } else {
                    component.addInterceptorMethodOverride(postConstruct.getClassName(), builder.build());
                }
            }
        }

        // pre-destroy(s) of the interceptor configured (if any) in the deployment descriptor
        final LifecycleCallbacksMetaData preDestroys = metaData.getPreDestroys();
        if (preDestroys != null) {
            for (final LifecycleCallbackMetaData preDestroy : preDestroys) {
                final InterceptorClassDescription.Builder builder = InterceptorClassDescription.builder();
                final String methodName = preDestroy.getMethodName();
                final MethodIdentifier methodIdentifier = MethodIdentifier.getIdentifier(void.class, methodName);
                builder.setPreDestroy(methodIdentifier);
                if (preDestroy.getClassName() == null || preDestroy.getClassName().isEmpty()) {
                    final String className = ClassReflectionIndexUtil.findRequiredMethod(reflectionIndex, componentClass.getModuleClass(), methodIdentifier).getDeclaringClass().getName();
                    component.addInterceptorMethodOverride(className, builder.build());
                } else {
                    component.addInterceptorMethodOverride(preDestroy.getClassName(), builder.build());
                }
            }
        }

        if (metaData instanceof SessionBeanMetaData) {

            final SessionBeanMetaData sessionBeanMetadata = (SessionBeanMetaData) metaData;
            // pre-passivate(s) of the interceptor configured (if any) in the deployment descriptor
            final LifecycleCallbacksMetaData prePassivates = sessionBeanMetadata.getPrePassivates();
            if (preDestroys != null) {
                for (final LifecycleCallbackMetaData prePassivate : prePassivates) {
                    final InterceptorClassDescription.Builder builder = InterceptorClassDescription.builder();
                    final String methodName = prePassivate.getMethodName();
                    final MethodIdentifier methodIdentifier = MethodIdentifier.getIdentifier(void.class, methodName);
                    builder.setPrePassivate(methodIdentifier);
                    if (prePassivate.getClassName() == null || prePassivate.getClassName().isEmpty()) {
                        final String className = ClassReflectionIndexUtil.findRequiredMethod(reflectionIndex, componentClass.getModuleClass(), methodIdentifier).getDeclaringClass().getName();
                        component.addInterceptorMethodOverride(className, builder.build());
                    } else {
                        component.addInterceptorMethodOverride(prePassivate.getClassName(), builder.build());
                    }
                }
            }

            final LifecycleCallbacksMetaData postActivates = sessionBeanMetadata.getPostActivates();
            if (postActivates != null) {
                for (final LifecycleCallbackMetaData postActivate : postActivates) {
                    final InterceptorClassDescription.Builder builder = InterceptorClassDescription.builder();
                    final String methodName = postActivate.getMethodName();
                    final MethodIdentifier methodIdentifier = MethodIdentifier.getIdentifier(void.class, methodName);
                    builder.setPostActivate(methodIdentifier);
                    if (postActivate.getClassName() == null || postActivate.getClassName().isEmpty()) {
                        final String className = ClassReflectionIndexUtil.findRequiredMethod(reflectionIndex, componentClass.getModuleClass(), methodIdentifier).getDeclaringClass().getName();
                        component.addInterceptorMethodOverride(className, builder.build());
                    } else {
                        component.addInterceptorMethodOverride(postActivate.getClassName(), builder.build());
                    }
                }
            }
        }
    }

    @Override
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11398.java