error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12308.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12308.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12308.java
text:
```scala
t@@hrow MESSAGES.failToLoadComponentClass(e, ejbComponentDescription.getComponentClassName());

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

package org.jboss.as.ejb3.deployment.processors.dd;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.InterceptorDescription;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.deployment.EjbDeploymentAttachmentKeys;
import org.jboss.as.ejb3.interceptor.ContainerInterceptorsMetaData;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.invocation.proxy.MethodIdentifier;
import org.jboss.logging.Logger;
import org.jboss.metadata.ejb.spec.EjbJarMetaData;
import org.jboss.metadata.ejb.spec.InterceptorBindingMetaData;
import org.jboss.metadata.ejb.spec.InterceptorBindingsMetaData;
import org.jboss.metadata.ejb.spec.NamedMethodMetaData;
import org.jboss.modules.Module;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

/**
 * A {@link DeploymentUnitProcessor} which processes the container interceptor bindings that are configured the jboss-ejb3.xml
 * deployment descriptor of a deployment
 *
 * @author Jaikiran Pai
 */
public class ContainerInterceptorBindingsDDProcessor implements DeploymentUnitProcessor {

    private static final Logger logger = Logger.getLogger(ContainerInterceptorBindingsDDProcessor.class);

    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final EjbJarMetaData metaData = deploymentUnit.getAttachment(EjbDeploymentAttachmentKeys.EJB_JAR_METADATA);
        if (metaData == null || metaData.getAssemblyDescriptor() == null) {
            return;
        }
        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        final Module module = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
        final DeploymentReflectionIndex index = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);

        // fetch the container-interceptors
        final List<ContainerInterceptorsMetaData> containerInterceptorConfigurations = metaData.getAssemblyDescriptor().getAny(ContainerInterceptorsMetaData.class);
        if (containerInterceptorConfigurations == null || containerInterceptorConfigurations.isEmpty()) {
            return;
        }
        final ContainerInterceptorsMetaData containerInterceptorsMetaData = containerInterceptorConfigurations.get(0);
        if (containerInterceptorsMetaData == null) {
            return;
        }
        final InterceptorBindingsMetaData containerInterceptorBindings = containerInterceptorsMetaData.getInterceptorBindings();
        // no interceptor-binding == nothing to do
        if (containerInterceptorBindings == null || containerInterceptorBindings.isEmpty()) {
            return;
        }
        // we have now found some container interceptors which are bound to certain EJBs, start the real work!

        final Map<String, List<InterceptorBindingMetaData>> bindingsPerEJB = new HashMap<String, List<InterceptorBindingMetaData>>();
        final List<InterceptorBindingMetaData> bindingsForAllEJBs = new ArrayList<InterceptorBindingMetaData>();
        for (final InterceptorBindingMetaData containerInterceptorBinding : containerInterceptorBindings) {
            if (containerInterceptorBinding.getEjbName().equals("*")) {
                // container interceptor bindings that are applicable for all EJBs are *not* expected to specify a method
                // since all EJBs having the same method is not really practical
                if (containerInterceptorBinding.getMethod() != null) {
                    throw MESSAGES.defaultInterceptorsNotBindToMethod();
                }
                if (containerInterceptorBinding.getInterceptorOrder() != null) {
                    throw MESSAGES.defaultInterceptorsNotSpecifyOrder();
                }
                // Make a note that this container interceptor binding is applicable for all EJBs
                bindingsForAllEJBs.add(containerInterceptorBinding);
            } else {
                // fetch existing container interceptor bindings for the EJB, if any.
                List<InterceptorBindingMetaData> bindings = bindingsPerEJB.get(containerInterceptorBinding.getEjbName());
                if (bindings == null) {
                    bindings = new ArrayList<InterceptorBindingMetaData>();
                    bindingsPerEJB.put(containerInterceptorBinding.getEjbName(), bindings);
                }
                // Make a note that the container interceptor binding is applicable for this specific EJB
                bindings.add(containerInterceptorBinding);
            }
        }
        // At this point we now know which container interceptor bindings have been configured for which EJBs.
        // Next, we create InterceptorDescription(s) out of those.
        final List<InterceptorDescription> interceptorDescriptionsForAllEJBs = new ArrayList<InterceptorDescription>();
        // first process container interceptors applicable for all EJBs
        for (InterceptorBindingMetaData binding : bindingsForAllEJBs) {
            if (binding.getInterceptorClasses() != null) {
                for (final String clazz : binding.getInterceptorClasses()) {
                    interceptorDescriptionsForAllEJBs.add(new InterceptorDescription(clazz));
                }
            }
        }
        // Now process container interceptors for each EJB
        for (final ComponentDescription componentDescription : eeModuleDescription.getComponentDescriptions()) {
            if (!(componentDescription instanceof EJBComponentDescription)) {
                continue;
            }
            final EJBComponentDescription ejbComponentDescription = (EJBComponentDescription) componentDescription;
            final Class<?> componentClass;
            try {
                componentClass = module.getClassLoader().loadClass(ejbComponentDescription.getComponentClassName());
            } catch (ClassNotFoundException e) {
                throw MESSAGES.failToLoadComponentClass(ejbComponentDescription.getComponentClassName());
            }
            final List<InterceptorBindingMetaData> bindingsApplicableForCurrentEJB = bindingsPerEJB.get(ejbComponentDescription.getComponentName());
            final Map<Method, List<InterceptorBindingMetaData>> methodInterceptors = new HashMap<Method, List<InterceptorBindingMetaData>>();
            final List<InterceptorBindingMetaData> classLevelBindings = new ArrayList<InterceptorBindingMetaData>();
            // we only want to exclude default and class level interceptors if every binding
            // has the exclude element.
            boolean classLevelExcludeDefaultInterceptors = false;
            Map<Method, Boolean> methodLevelExcludeDefaultInterceptors = new HashMap<Method, Boolean>();
            Map<Method, Boolean> methodLevelExcludeClassInterceptors = new HashMap<Method, Boolean>();

            // if an absolute order has been defined at any level then absolute ordering takes precedence
            boolean classLevelAbsoluteOrder = false;
            final Map<Method, Boolean> methodLevelAbsoluteOrder = new HashMap<Method, Boolean>();
            if (bindingsApplicableForCurrentEJB != null) {
                for (final InterceptorBindingMetaData binding : bindingsApplicableForCurrentEJB) {
                    if (binding.getMethod() == null) {
                        // The container interceptor is expected to be fired for all methods of that EJB
                        classLevelBindings.add(binding);
                        // if even one binding does not say exclude default then we do not exclude
                        if (binding.isExcludeDefaultInterceptors()) {
                            classLevelExcludeDefaultInterceptors = true;
                        }
                        if (binding.isTotalOrdering()) {
                            if (classLevelAbsoluteOrder) {
                                throw MESSAGES.twoEjbBindingsSpecifyAbsoluteOrder(componentClass.toString());
                            } else {
                                classLevelAbsoluteOrder = true;
                            }
                        }
                    } else {
                        // Method level bindings
                        // First find the right method
                        final NamedMethodMetaData methodData = binding.getMethod();
                        final ClassReflectionIndex<?> classIndex = index.getClassIndex(componentClass);
                        Method resolvedMethod = null;
                        if (methodData.getMethodParams() == null) {
                            final Collection<Method> methods = classIndex.getAllMethods(methodData.getMethodName());
                            if (methods.isEmpty()) {
                                throw MESSAGES.failToFindMethodInEjbJarXml(componentClass.getName(), methodData.getMethodName());
                            } else if (methods.size() > 1) {
                                throw MESSAGES.multipleMethodReferencedInEjbJarXml(methodData.getMethodName(), componentClass.getName());
                            }
                            resolvedMethod = methods.iterator().next();
                        } else {
                            final Collection<Method> methods = classIndex.getAllMethods(methodData.getMethodName(), methodData.getMethodParams().size());
                            for (final Method method : methods) {
                                boolean match = true;
                                for (int i = 0; i < method.getParameterTypes().length; ++i) {
                                    if (!method.getParameterTypes()[i].getName().equals(methodData.getMethodParams().get(i))) {
                                        match = false;
                                        break;
                                    }
                                }
                                if (match) {
                                    resolvedMethod = method;
                                    break;
                                }
                            }
                            if (resolvedMethod == null) {
                                throw MESSAGES.failToFindMethodWithParameterTypes(componentClass.getName(), methodData.getMethodName(), methodData.getMethodParams());
                            }
                        }
                        List<InterceptorBindingMetaData> methodSpecificInterceptorBindings = methodInterceptors.get(resolvedMethod);
                        if (methodSpecificInterceptorBindings == null) {
                            methodSpecificInterceptorBindings = new ArrayList<InterceptorBindingMetaData>();
                            methodInterceptors.put(resolvedMethod, methodSpecificInterceptorBindings);
                        }
                        methodSpecificInterceptorBindings.add(binding);
                        if (binding.isExcludeDefaultInterceptors()) {
                            methodLevelExcludeDefaultInterceptors.put(resolvedMethod, true);
                        }
                        if (binding.isExcludeClassInterceptors()) {
                            methodLevelExcludeClassInterceptors.put(resolvedMethod, true);
                        }
                        if (binding.isTotalOrdering()) {
                            if (methodLevelAbsoluteOrder.containsKey(resolvedMethod)) {
                                throw MESSAGES.twoEjbBindingsSpecifyAbsoluteOrder(resolvedMethod.toString());
                            } else {
                                methodLevelAbsoluteOrder.put(resolvedMethod, true);
                            }
                        }
                    }
                }
            }
            // Now we have all the bindings in a format we can use
            // Build the list of default interceptors
            ejbComponentDescription.setDefaultContainerInterceptors(interceptorDescriptionsForAllEJBs);

            if (classLevelExcludeDefaultInterceptors) {
                ejbComponentDescription.setExcludeDefaultContainerInterceptors(true);
            }
            final List<InterceptorDescription> classLevelInterceptors = new ArrayList<InterceptorDescription>();
            if (classLevelAbsoluteOrder) {
                // We have an absolute ordering for the class level interceptors
                for (final InterceptorBindingMetaData binding : classLevelBindings) {
                    // Find the class level container interceptor binding which specifies the total ordering (there will
                    // only be one since we have already validated in an earlier step, then more than one binding cannot
                    // specify a ordering
                    if (binding.isTotalOrdering()) {
                        for (final String interceptor : binding.getInterceptorOrder()) {
                            classLevelInterceptors.add(new InterceptorDescription(interceptor));
                        }
                        break;
                    }
                }
                // We have merged the default interceptors into the class interceptors
                ejbComponentDescription.setExcludeDefaultContainerInterceptors(true);
            } else {
                for (InterceptorBindingMetaData binding : classLevelBindings) {
                    if (binding.getInterceptorClasses() != null) {
                        for (final String interceptor : binding.getInterceptorClasses()) {
                            classLevelInterceptors.add(new InterceptorDescription(interceptor));
                        }
                    }
                }
            }
            // We now know about the class level container interceptors for this EJB
            ejbComponentDescription.setClassLevelContainerInterceptors(classLevelInterceptors);

            // Now process method level container interceptors for the EJB
            for (Map.Entry<Method, List<InterceptorBindingMetaData>> entry : methodInterceptors.entrySet()) {
                final Method method = entry.getKey();
                final List<InterceptorBindingMetaData> methodBindings = entry.getValue();
                boolean totalOrder = methodLevelAbsoluteOrder.containsKey(method);
                final MethodIdentifier methodIdentifier = MethodIdentifier.getIdentifierForMethod(method);

                Boolean excludeDefaultInterceptors = methodLevelExcludeDefaultInterceptors.get(method);
                excludeDefaultInterceptors = excludeDefaultInterceptors == null ? false : excludeDefaultInterceptors;
                if (!excludeDefaultInterceptors) {
                    excludeDefaultInterceptors = ejbComponentDescription.isExcludeDefaultContainerInterceptors() || ejbComponentDescription.isExcludeDefaultContainerInterceptors(methodIdentifier);
                }

                Boolean excludeClassInterceptors = methodLevelExcludeClassInterceptors.get(method);
                excludeClassInterceptors = excludeClassInterceptors == null ? false : excludeClassInterceptors;
                if (!excludeClassInterceptors) {
                    excludeClassInterceptors = ejbComponentDescription.isExcludeClassLevelContainerInterceptors(methodIdentifier);
                }

                final List<InterceptorDescription> methodLevelInterceptors = new ArrayList<InterceptorDescription>();
                if (totalOrder) {
                    // If there is a total order we just use it
                    for (final InterceptorBindingMetaData binding : methodBindings) {
                        if (binding.isTotalOrdering()) {
                            for (final String interceptor : binding.getInterceptorOrder()) {
                                methodLevelInterceptors.add(new InterceptorDescription(interceptor));
                            }
                        }
                    }
                } else {
                    // First add default interceptors and then class level interceptors for the method and finally
                    // the method specific interceptors
                    if (!excludeDefaultInterceptors) {
                        methodLevelInterceptors.addAll(interceptorDescriptionsForAllEJBs);
                    }
                    if (!excludeClassInterceptors) {
                        for (InterceptorDescription interceptor : classLevelInterceptors) {
                            methodLevelInterceptors.add(interceptor);
                        }
                    }
                    for (final InterceptorBindingMetaData binding : methodBindings) {
                        if (binding.getInterceptorClasses() != null) {
                            for (final String interceptor : binding.getInterceptorClasses()) {
                                methodLevelInterceptors.add(new InterceptorDescription(interceptor));
                            }
                        }
                    }

                }
                // We have already taken component and default interceptors into account
                ejbComponentDescription.excludeClassLevelContainerInterceptors(methodIdentifier);
                ejbComponentDescription.excludeDefaultContainerInterceptors(methodIdentifier);
                ejbComponentDescription.setMethodContainerInterceptors(methodIdentifier, methodLevelInterceptors);
            }
        }
    }

    @Override
    public void undeploy(DeploymentUnit context) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12308.java