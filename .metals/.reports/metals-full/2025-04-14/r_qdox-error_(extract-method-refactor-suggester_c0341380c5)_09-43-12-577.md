error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14342.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14342.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14342.java
text:
```scala
i@@njectors.addFirst(injectionConfiguration.getTarget().createInjectionInterceptorFactory(instanceKey, valueContextKey, managedReferenceFactoryValue, context.getDeploymentUnit(), injectionConfiguration.isOptional()));

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

package org.jboss.as.ee.component;

import static org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.as.ee.component.interceptors.InterceptorClassDescription;
import org.jboss.as.ee.component.interceptors.InterceptorOrder;
import org.jboss.as.ee.component.serialization.WriteReplaceInterface;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.naming.ValueManagedReferenceFactory;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.reflect.ClassIndex;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndex;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndexUtil;
import org.jboss.as.server.deployment.reflect.DeploymentClassIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.as.server.deployment.reflect.ProxyMetadataSource;
import org.jboss.invocation.ImmediateInterceptorFactory;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.InterceptorFactoryContext;
import org.jboss.invocation.Interceptors;
import org.jboss.invocation.proxy.MethodIdentifier;
import org.jboss.invocation.proxy.ProxyConfiguration;
import org.jboss.invocation.proxy.ProxyFactory;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.value.ConstructedValue;
import org.jboss.msc.value.InjectedValue;
import org.jboss.msc.value.Value;

/**
 * A description of a generic Java EE component.  The description is pre-classloading so it references everything by name.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class ComponentDescription {

    private static final DefaultComponentConfigurator FIRST_CONFIGURATOR = new DefaultComponentConfigurator();
    private static final AtomicInteger PROXY_ID = new AtomicInteger(0);
    private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

    private final ServiceName serviceName;
    private ServiceName contextServiceName;
    private final String componentName;
    private final String componentClassName;
    private final EEModuleDescription moduleDescription;
    private final Set<ViewDescription> views = new HashSet<ViewDescription>();

    /**
     * Interceptors methods that have been defined by the deployment descriptor
     */
    private final Map<String, InterceptorClassDescription> interceptorClassOverrides = new HashMap<String, InterceptorClassDescription>();

    private List<InterceptorDescription> classInterceptors = new ArrayList<InterceptorDescription>();
    private List<InterceptorDescription> defaultInterceptors = new ArrayList<InterceptorDescription>();
    private final Map<MethodIdentifier, List<InterceptorDescription>> methodInterceptors = new HashMap<MethodIdentifier, List<InterceptorDescription>>();
    private final Set<MethodIdentifier> methodExcludeDefaultInterceptors = new HashSet<MethodIdentifier>();
    private final Set<MethodIdentifier> methodExcludeClassInterceptors = new HashSet<MethodIdentifier>();
    private Set<InterceptorDescription> allInterceptors;
    private boolean excludeDefaultInterceptors = false;

    private final Map<ServiceName, ServiceBuilder.DependencyType> dependencies = new HashMap<ServiceName, ServiceBuilder.DependencyType>();

    private ComponentNamingMode namingMode = ComponentNamingMode.USE_MODULE;

    private DeploymentDescriptorEnvironment deploymentDescriptorEnvironment;


    // Bindings
    private final List<BindingConfiguration> bindingConfigurations = new ArrayList<BindingConfiguration>();
    //injections that have been set in the components deployment descriptor
    private final Map<String, Map<InjectionTarget, ResourceInjectionConfiguration>> resourceInjections = new HashMap<String, Map<InjectionTarget, ResourceInjectionConfiguration>>();

    private final Deque<ComponentConfigurator> configurators = new ArrayDeque<ComponentConfigurator>();


    /**
     * If this component is deployed in a bean deployment archive this stores the id of the BDA
     */
    private String beanDeploymentArchiveId;

    /**
     * Construct a new instance.
     *
     * @param componentName             the component name
     * @param componentClassName        the component instance class name
     * @param moduleDescription         the EE module description
     * @param deploymentUnitServiceName the service name of the DU containing this component
     */
    public ComponentDescription(final String componentName, final String componentClassName, final EEModuleDescription moduleDescription, final ServiceName deploymentUnitServiceName) {
        this.moduleDescription = moduleDescription;
        if (componentName == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (componentClassName == null) {
            throw new IllegalArgumentException("className is null");
        }
        if (moduleDescription == null) {
            throw new IllegalArgumentException("moduleName is null");
        }
        if (deploymentUnitServiceName == null) {
            throw new IllegalArgumentException("deploymentUnitServiceName is null");
        }
        serviceName = deploymentUnitServiceName.append("component").append(componentName);
        this.componentName = componentName;
        this.componentClassName = componentClassName;
        configurators.addLast(FIRST_CONFIGURATOR);
    }

    public ComponentConfiguration createConfiguration(final ClassIndex classIndex) {
        return new ComponentConfiguration(this, classIndex);
    }

    /**
     * Get the component name.
     *
     * @return the component name
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Set context service name.
     *
     * @param contextServiceName
     */
    public void setContextServiceName(final ServiceName contextServiceName) {
        this.contextServiceName = contextServiceName;
    }

    /**
     * Get the context service name.
     *
     * @return the context service name
     */
    public ServiceName getContextServiceName() {
        if (contextServiceName != null) return contextServiceName;
        if (getNamingMode() == ComponentNamingMode.CREATE) {
            return ContextNames.contextServiceNameOfComponent(getApplicationName(), getModuleName(), getComponentName());
        } else if (getNamingMode() == ComponentNamingMode.USE_MODULE) {
            return ContextNames.contextServiceNameOfModule(getApplicationName(), getModuleName());
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Get the base service name for this component.
     *
     * @return the base service name
     */
    public ServiceName getServiceName() {
        return serviceName;
    }

    /**
     * Get the service name of this components start service
     *
     * @return The start service name
     */
    public ServiceName getStartServiceName() {
        return serviceName.append("START");
    }

    /**
     * Get the service name of this components create service
     *
     * @return The create service name
     */
    public ServiceName getCreateServiceName() {
        return serviceName.append("CREATE");
    }

    /**
     * Get the component instance class name.
     *
     * @return the component class name
     */
    public String getComponentClassName() {
        return componentClassName;
    }

    /**
     * Get the component's module name.
     *
     * @return the module name
     */
    public String getModuleName() {
        return moduleDescription.getModuleName();
    }

    /**
     * Get the component's module's application name.
     *
     * @return the application name
     */
    public String getApplicationName() {
        return moduleDescription.getApplicationName();
    }

    /**
     * Get the list of interceptor classes applied directly to class. These interceptors will have lifecycle methods invoked
     *
     * @return the interceptor classes
     */
    public List<InterceptorDescription> getClassInterceptors() {
        return classInterceptors;
    }

    /**
     * Override the class interceptors with a new set of interceptors
     *
     * @param classInterceptors
     */
    public void setClassInterceptors(List<InterceptorDescription> classInterceptors) {
        this.classInterceptors = classInterceptors;
        this.allInterceptors = null;
    }


    /**
     * @return the components default interceptors
     */
    public List<InterceptorDescription> getDefaultInterceptors() {
        return defaultInterceptors;
    }

    public void setDefaultInterceptors(final List<InterceptorDescription> defaultInterceptors) {
        allInterceptors = null;
        this.defaultInterceptors = defaultInterceptors;
    }

    /**
     * Returns a combined map of class and method level interceptors
     *
     * @return all interceptors on the class
     */
    public Set<InterceptorDescription> getAllInterceptors() {
        if (allInterceptors == null) {
            allInterceptors = new HashSet<InterceptorDescription>();
            allInterceptors.addAll(classInterceptors);
            if (!excludeDefaultInterceptors) {
                allInterceptors.addAll(defaultInterceptors);
            }
            for (List<InterceptorDescription> interceptors : methodInterceptors.values()) {
                allInterceptors.addAll(interceptors);
            }
        }
        return allInterceptors;
    }

    /**
     * @return <code>true</code> if the <code>ExcludeDefaultInterceptors</code> annotation was applied to the class
     */
    public boolean isExcludeDefaultInterceptors() {
        return excludeDefaultInterceptors;
    }

    public void setExcludeDefaultInterceptors(boolean excludeDefaultInterceptors) {
        allInterceptors = null;
        this.excludeDefaultInterceptors = excludeDefaultInterceptors;
    }

    /**
     * @param method The method that has been annotated <code>@ExcludeDefaultInterceptors</code>
     */
    public void excludeDefaultInterceptors(MethodIdentifier method) {
        methodExcludeDefaultInterceptors.add(method);
    }

    public boolean isExcludeDefaultInterceptors(MethodIdentifier method) {
        return methodExcludeDefaultInterceptors.contains(method);
    }

    /**
     * @param method The method that has been annotated <code>@ExcludeClassInterceptors</code>
     */
    public void excludeClassInterceptors(MethodIdentifier method) {
        methodExcludeClassInterceptors.add(method);
    }

    public boolean isExcludeClassInterceptors(MethodIdentifier method) {
        return methodExcludeClassInterceptors.contains(method);
    }

    /**
     * Add a class level interceptor.
     *
     * @param description the interceptor class description
     */
    public void addClassInterceptor(InterceptorDescription description) {
        String name = description.getInterceptorClassName();
        classInterceptors.add(description);
        this.allInterceptors = null;
    }

    /**
     * Returns the {@link InterceptorDescription} for the passed <code>interceptorClassName</code>, if such a class
     * interceptor exists for this component description. Else returns null.
     *
     * @param interceptorClassName The fully qualified interceptor class name
     * @return
     */
    public InterceptorDescription getClassInterceptor(String interceptorClassName) {
        for (InterceptorDescription interceptor : classInterceptors) {
            if (interceptor.getInterceptorClassName().equals(interceptorClassName)) {
                return interceptor;
            }
        }
        return null;
    }

    /**
     * Get the method interceptor configurations.  The key is the method identifier, the value is
     * the set of class names of interceptors to configure on that method.
     *
     * @return the method interceptor configurations
     */
    public Map<MethodIdentifier, List<InterceptorDescription>> getMethodInterceptors() {
        return methodInterceptors;
    }

    /**
     * Add a method interceptor class name.
     *
     * @param method      the method
     * @param description the interceptor descriptor
     */
    public void addMethodInterceptor(MethodIdentifier method, InterceptorDescription description) {
        //we do not add method level interceptors to the set of interceptor classes,
        //as their around invoke annotations
        List<InterceptorDescription> interceptors = methodInterceptors.get(method);
        if (interceptors == null) {
            methodInterceptors.put(method, interceptors = new ArrayList<InterceptorDescription>());
        }
        final String name = description.getInterceptorClassName();
        // add the interceptor class to the EEModuleDescription
        interceptors.add(description);
        this.allInterceptors = null;
    }

    /**
     * Sets the method level interceptors for a method, and marks it as exclude class and default level interceptors.
     * <p/>
     * This is used to set the final interceptor order after it has been modifier by the deployment descriptor
     *
     * @param identifier              the method identifier
     * @param interceptorDescriptions The interceptors
     */
    public void setMethodInterceptors(MethodIdentifier identifier, List<InterceptorDescription> interceptorDescriptions) {
        methodInterceptors.put(identifier, interceptorDescriptions);
        methodExcludeClassInterceptors.add(identifier);
        methodExcludeDefaultInterceptors.add(identifier);
    }

    /**
     * Adds an interceptor class method override, merging it with existing overrides (if any)
     *
     * @param className The class name
     * @param override  The method override
     */
    public void addInterceptorMethodOverride(final String className, final InterceptorClassDescription override) {
        interceptorClassOverrides.put(className, InterceptorClassDescription.merge(interceptorClassOverrides.get(className), override));
    }

    /**
     * Get the naming mode of this component.
     *
     * @return the naming mode
     */
    public ComponentNamingMode getNamingMode() {
        return namingMode;
    }

    /**
     * Set the naming mode of this component.  May not be {@code null}.
     *
     * @param namingMode the naming mode
     */
    public void setNamingMode(final ComponentNamingMode namingMode) {
        if (namingMode == null) {
            throw new IllegalArgumentException("namingMode is null");
        }
        this.namingMode = namingMode;
    }

    /**
     * @return The module description for the component
     */
    public EEModuleDescription getModuleDescription() {
        return moduleDescription;
    }

    /**
     * Add a dependency to this component.  If the same dependency is added multiple times, only the first will
     * take effect.
     *
     * @param serviceName the service name of the dependency
     * @param type        the type of the dependency (required or optional)
     */
    public void addDependency(ServiceName serviceName, ServiceBuilder.DependencyType type) {
        if (serviceName == null) {
            throw new IllegalArgumentException("serviceName is null");
        }
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        final Map<ServiceName, ServiceBuilder.DependencyType> dependencies = this.dependencies;
        final ServiceBuilder.DependencyType dependencyType = dependencies.get(serviceName);
        if (dependencyType == ServiceBuilder.DependencyType.REQUIRED) {
            dependencies.put(serviceName, ServiceBuilder.DependencyType.REQUIRED);
        } else {
            dependencies.put(serviceName, type);
        }
    }

    /**
     * Get the dependency map.
     *
     * @return the dependency map
     */
    public Map<ServiceName, ServiceBuilder.DependencyType> getDependencies() {
        return dependencies;
    }

    public DeploymentDescriptorEnvironment getDeploymentDescriptorEnvironment() {
        return deploymentDescriptorEnvironment;
    }

    public void setDeploymentDescriptorEnvironment(DeploymentDescriptorEnvironment deploymentDescriptorEnvironment) {
        this.deploymentDescriptorEnvironment = deploymentDescriptorEnvironment;
    }


    /**
     * Get the binding configurations for this component.  This list contains bindings which are specific to the
     * component.
     *
     * @return the binding configurations
     */
    public List<BindingConfiguration> getBindingConfigurations() {
        return bindingConfigurations;
    }

    /**
     * Get the list of views which apply to this component.
     *
     * @return the list of views
     */
    public Set<ViewDescription> getViews() {
        return views;
    }

    /**
     * TODO: change this to a per method setting
     *
     * @return true if timer service interceptor chains should be built for this component
     */
    public boolean isTimerServiceApplicable() {
        return false;
    }

    /**
     * Get the configurators for this component.
     *
     * @return the configurators
     */
    public Deque<ComponentConfigurator> getConfigurators() {
        return configurators;
    }

    private boolean isIntercepted() {
        return true;
    }

    private static InterceptorFactory weaved(final Collection<InterceptorFactory> interceptorFactories) {
        return new InterceptorFactory() {
            @Override
            public Interceptor create(InterceptorFactoryContext context) {
                final Interceptor[] interceptors = new Interceptor[interceptorFactories.size()];
                final Iterator<InterceptorFactory> factories = interceptorFactories.iterator();
                for (int i = 0; i < interceptors.length; i++) {
                    interceptors[i] = factories.next().create(context);
                }
                return Interceptors.getWeavedInterceptor(interceptors);
            }
        };
    }

    private static class DefaultComponentConfigurator implements ComponentConfigurator {

        public void configure(final DeploymentPhaseContext context, final ComponentDescription description, final ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
            final DeploymentUnit deploymentUnit = context.getDeploymentUnit();
            final DeploymentReflectionIndex deploymentReflectionIndex = deploymentUnit.getAttachment(REFLECTION_INDEX);
            final Object instanceKey = BasicComponentInstance.INSTANCE_KEY;
            final Module module = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
            final EEApplicationClasses applicationClasses = deploymentUnit.getAttachment(Attachments.EE_APPLICATION_CLASSES_DESCRIPTION);
            final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
            final ProxyMetadataSource proxyReflectionIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.PROXY_REFLECTION_INDEX);
            final DeploymentClassIndex classIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.CLASS_INDEX);

            // Module stuff

            final Deque<InterceptorFactory> instantiators = new ArrayDeque<InterceptorFactory>();
            final Deque<InterceptorFactory> injectors = new ArrayDeque<InterceptorFactory>();
            final Deque<InterceptorFactory> uninjectors = new ArrayDeque<InterceptorFactory>();
            final Deque<InterceptorFactory> destructors = new ArrayDeque<InterceptorFactory>();

            final ClassReflectionIndex<?> componentClassIndex = deploymentReflectionIndex.getClassIndex(configuration.getComponentClass());
            final List<InterceptorFactory> componentUserAroundInvoke = new ArrayList<InterceptorFactory>();
            final List<InterceptorFactory> componentUserAroundTimeout;

            final Map<String, List<InterceptorFactory>> userAroundInvokesByInterceptorClass = new HashMap<String, List<InterceptorFactory>>();
            final Map<String, List<InterceptorFactory>> userAroundTimeoutsByInterceptorClass;

            final Map<String, List<InterceptorFactory>> userPostConstructByInterceptorClass = new HashMap<String, List<InterceptorFactory>>();
            final Map<String, List<InterceptorFactory>> userPreDestroyByInterceptorClass = new HashMap<String, List<InterceptorFactory>>();

            if (description.isTimerServiceApplicable()) {
                componentUserAroundTimeout = new ArrayList<InterceptorFactory>();
                userAroundTimeoutsByInterceptorClass = new HashMap<String, List<InterceptorFactory>>();
            } else {
                componentUserAroundTimeout = null;
                userAroundTimeoutsByInterceptorClass = null;
            }


            // Primary instance
            final ManagedReferenceFactory instanceFactory = configuration.getInstanceFactory();
            if (instanceFactory != null) {
                instantiators.addFirst(new ManagedReferenceInterceptorFactory(instanceFactory, instanceKey));
            } else {
                //use the default constructor if no instanceFactory has been set
                final Constructor<Object> constructor = (Constructor<Object>) componentClassIndex.getConstructor(EMPTY_CLASS_ARRAY);
                if (constructor == null) {
                    throw new DeploymentUnitProcessingException("Could not find default constructor for " + configuration.getComponentClass());
                }
                ValueManagedReferenceFactory factory = new ValueManagedReferenceFactory(new ConstructedValue<Object>(constructor, Collections.<Value<?>>emptyList()));
                instantiators.addFirst(new ManagedReferenceInterceptorFactory(factory, instanceKey));
            }
            destructors.addLast(new ManagedReferenceReleaseInterceptorFactory(instanceKey));

            new ClassDescriptionTraversal(configuration.getComponentClass(), applicationClasses) {
                @Override
                public void handle(Class<?> clazz, EEModuleClassDescription classDescription) throws DeploymentUnitProcessingException {
                    mergeInjectionsForClass(clazz, classDescription, moduleDescription, description, configuration, context, injectors, instanceKey, uninjectors);
                }
            }.run();


            //all interceptors with lifecycle callbacks, in the correct order
            final List<InterceptorDescription> interceptorWithLifecycleCallbacks = new ArrayList<InterceptorDescription>();
            if (!description.isExcludeDefaultInterceptors()) {
                interceptorWithLifecycleCallbacks.addAll(description.getDefaultInterceptors());
            }
            interceptorWithLifecycleCallbacks.addAll(description.getClassInterceptors());

            for (final InterceptorDescription interceptorDescription : description.getAllInterceptors()) {
                final String interceptorClassName = interceptorDescription.getInterceptorClassName();
                final ClassIndex interceptorClass;
                try {
                    interceptorClass = classIndex.classIndex(interceptorClassName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Could not load interceptor class " + interceptorClassName, e);
                }


                //we store the interceptor instance under the class key
                final Object contextKey = interceptorClass.getModuleClass();

                final ClassReflectionIndex<?> interceptorIndex = deploymentReflectionIndex.getClassIndex(interceptorClass.getModuleClass());
                final Constructor<?> constructor = interceptorIndex.getConstructor(EMPTY_CLASS_ARRAY);
                if (constructor == null) {
                    throw new DeploymentUnitProcessingException("No default constructor for interceptor class " + interceptorClassName + " on component " + configuration.getComponentClass());
                }


                instantiators.addFirst(new ManagedReferenceInterceptorFactory(new ValueManagedReferenceFactory(new ConstructedValue(constructor, Collections.<Value<?>>emptyList())), contextKey));
                destructors.addLast(new ManagedReferenceReleaseInterceptorFactory(contextKey));

                final boolean interceptorHasLifecycleCallbacks = interceptorWithLifecycleCallbacks.contains(interceptorDescription);

                new ClassDescriptionTraversal(interceptorClass.getModuleClass(), applicationClasses) {
                    @Override
                    public void handle(final Class<?> clazz, EEModuleClassDescription classDescription) throws DeploymentUnitProcessingException {
                        final ClassReflectionIndex<?> interceptorClassIndex = deploymentReflectionIndex.getClassIndex(clazz);
                        mergeInjectionsForClass(clazz, classDescription, moduleDescription, description, configuration, context, injectors, contextKey, uninjectors);
                        final InterceptorClassDescription interceptorConfig;
                        if (classDescription != null) {
                            interceptorConfig = InterceptorClassDescription.merge(classDescription.getInterceptorClassDescription(), moduleDescription.getInterceptorClassOverride(clazz.getName()));
                        } else {
                            interceptorConfig = InterceptorClassDescription.merge(null, moduleDescription.getInterceptorClassOverride(clazz.getName()));
                        }
                        // Only class level interceptors are processed for postconstruct/predestroy methods.
                        // Method level interceptors aren't supposed to be processed for postconstruct/predestroy lifecycle
                        // methods, as per interceptors spec
                        if (interceptorHasLifecycleCallbacks) {
                            final MethodIdentifier postConstructMethodIdentifier = interceptorConfig.getPostConstruct();
                            if (postConstructMethodIdentifier != null) {
                                final Method method = ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, interceptorClassIndex, postConstructMethodIdentifier);

                                if (isNotOverriden(clazz, method, interceptorIndex, deploymentReflectionIndex)) {
                                    InterceptorFactory interceptorFactory = new ManagedReferenceLifecycleMethodInterceptorFactory(contextKey, method, true, true);
                                    List<InterceptorFactory> userPostConstruct = userPostConstructByInterceptorClass.get(interceptorClassName);
                                    if (userPostConstruct == null) {
                                        userPostConstructByInterceptorClass.put(interceptorClassName, userPostConstruct = new ArrayList<InterceptorFactory>());
                                    }
                                    userPostConstruct.add(interceptorFactory);
                                }
                            }
                            final MethodIdentifier preDestroyMethodIdentifier = interceptorConfig.getPreDestroy();
                            if (preDestroyMethodIdentifier != null) {
                                final Method method = ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, interceptorClassIndex, preDestroyMethodIdentifier);
                                if (isNotOverriden(clazz, method, interceptorIndex, deploymentReflectionIndex)) {
                                    InterceptorFactory interceptorFactory = new ManagedReferenceLifecycleMethodInterceptorFactory(contextKey, method, true, true);
                                    List<InterceptorFactory> userPreDestroy = userPreDestroyByInterceptorClass.get(interceptorClassName);
                                    if (userPreDestroy == null) {
                                        userPreDestroyByInterceptorClass.put(interceptorClassName, userPreDestroy = new ArrayList<InterceptorFactory>());
                                    }
                                    userPreDestroy.add(interceptorFactory);
                                }
                            }
                        }
                        final MethodIdentifier aroundInvokeMethodIdentifier = interceptorConfig.getAroundInvoke();
                        if (aroundInvokeMethodIdentifier != null) {
                            final Method method = ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, interceptorClassIndex, aroundInvokeMethodIdentifier);
                            if (isNotOverriden(clazz, method, interceptorIndex, deploymentReflectionIndex)) {
                                List<InterceptorFactory> interceptors;
                                if ((interceptors = userAroundInvokesByInterceptorClass.get(interceptorClassName)) == null) {
                                    userAroundInvokesByInterceptorClass.put(interceptorClassName, interceptors = new ArrayList<InterceptorFactory>());
                                }
                                interceptors.add(new ManagedReferenceLifecycleMethodInterceptorFactory(contextKey, method, false));
                            }
                        }
                        if (description.isTimerServiceApplicable()) {
                            final MethodIdentifier aroundTimeoutMethodIdentifier = interceptorConfig.getAroundTimeout();
                            if (aroundTimeoutMethodIdentifier != null) {
                                final Method method = ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, interceptorClassIndex, aroundTimeoutMethodIdentifier);
                                if (isNotOverriden(clazz, method, interceptorIndex, deploymentReflectionIndex)) {
                                    List<InterceptorFactory> interceptors;
                                    if ((interceptors = userAroundTimeoutsByInterceptorClass.get(interceptorClassName)) == null) {
                                        userAroundTimeoutsByInterceptorClass.put(interceptorClassName, interceptors = new ArrayList<InterceptorFactory>());
                                    }
                                    interceptors.add(new ManagedReferenceLifecycleMethodInterceptorFactory(contextKey, method, false));
                                }
                            }
                        }
                    }
                }.run();
            }

            final Deque<InterceptorFactory> userPostConstruct = new ArrayDeque<InterceptorFactory>();
            final Deque<InterceptorFactory> userPreDestroy = new ArrayDeque<InterceptorFactory>();

            //now add the lifecycle interceptors in the correct order


            for (final InterceptorDescription interceptorClass : interceptorWithLifecycleCallbacks) {
                if (userPostConstructByInterceptorClass.containsKey(interceptorClass.getInterceptorClassName())) {
                    userPostConstruct.addAll(userPostConstructByInterceptorClass.get(interceptorClass.getInterceptorClassName()));
                }
                if (userPreDestroyByInterceptorClass.containsKey(interceptorClass.getInterceptorClassName())) {
                    userPreDestroy.addAll(userPreDestroyByInterceptorClass.get(interceptorClass.getInterceptorClassName()));
                }
            }


            new ClassDescriptionTraversal(configuration.getComponentClass(), applicationClasses) {
                @Override
                public void handle(final Class<?> clazz, EEModuleClassDescription classDescription) throws DeploymentUnitProcessingException {
                    final ClassReflectionIndex classReflectionIndex = deploymentReflectionIndex.getClassIndex(clazz);

                    final InterceptorClassDescription interceptorConfig = mergeInterceptorConfig(clazz, classDescription, description);

                    final MethodIdentifier componentPostConstructMethodIdentifier = interceptorConfig.getPostConstruct();
                    if (componentPostConstructMethodIdentifier != null) {
                        final Method method = ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, classReflectionIndex, componentPostConstructMethodIdentifier);

                        if (isNotOverriden(clazz, method, componentClassIndex, deploymentReflectionIndex)) {
                            InterceptorFactory interceptorFactory = new ManagedReferenceLifecycleMethodInterceptorFactory(instanceKey, method, true, true);
                            userPostConstruct.addLast(interceptorFactory);
                        }
                    }
                    final MethodIdentifier componentPreDestroyMethodIdentifier = interceptorConfig.getPreDestroy();
                    if (componentPreDestroyMethodIdentifier != null) {
                        final Method method = ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, classReflectionIndex, componentPreDestroyMethodIdentifier);
                        if (isNotOverriden(clazz, method, componentClassIndex, deploymentReflectionIndex)) {
                            InterceptorFactory interceptorFactory = new ManagedReferenceLifecycleMethodInterceptorFactory(instanceKey, method, true, true);
                            userPreDestroy.addLast(interceptorFactory);
                        }
                    }
                    final MethodIdentifier componentAroundInvokeMethodIdentifier = interceptorConfig.getAroundInvoke();
                    if (componentAroundInvokeMethodIdentifier != null) {
                        final Method method = ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, classReflectionIndex, componentAroundInvokeMethodIdentifier);

                        if (isNotOverriden(clazz, method, componentClassIndex, deploymentReflectionIndex)) {
                            componentUserAroundInvoke.add(new ManagedReferenceLifecycleMethodInterceptorFactory(instanceKey, method, false));
                        }
                    }
                    if (description.isTimerServiceApplicable()) {
                        final MethodIdentifier componentAroundTimeoutMethodIdentifier = interceptorConfig.getAroundTimeout();
                        if (componentAroundTimeoutMethodIdentifier != null) {
                            final Method method = ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, classReflectionIndex, componentAroundTimeoutMethodIdentifier);
                            if (isNotOverriden(clazz, method, componentClassIndex, deploymentReflectionIndex)) {
                                componentUserAroundTimeout.add(new ManagedReferenceLifecycleMethodInterceptorFactory(instanceKey, method, false));
                            }
                        }
                    }
                }
            }.run();

            final InterceptorFactory tcclInterceptor = new ImmediateInterceptorFactory(new TCCLInterceptor(module.getClassLoader()));

            // Apply post-construct
            if (!injectors.isEmpty()) {
                configuration.addPostConstructInterceptor(weaved(injectors), InterceptorOrder.ComponentPostConstruct.RESOURCE_INJECTION_INTERCEPTORS);
            }

            if (!instantiators.isEmpty()) {
                configuration.addPostConstructInterceptor(weaved(instantiators), InterceptorOrder.ComponentPostConstruct.INSTANTIATION_INTERCEPTORS);
            }
            if (!userPostConstruct.isEmpty()) {
                configuration.addPostConstructInterceptor(weaved(userPostConstruct), InterceptorOrder.ComponentPostConstruct.USER_INTERCEPTORS);
            }
            configuration.addPostConstructInterceptor(Interceptors.getTerminalInterceptorFactory(), InterceptorOrder.ComponentPostConstruct.TERMINAL_INTERCEPTOR);
            configuration.addPostConstructInterceptor(tcclInterceptor, InterceptorOrder.ComponentPostConstruct.TCCL_INTERCEPTOR);

            // Apply pre-destroy
            if (!uninjectors.isEmpty()) {
                configuration.addPreDestroyInterceptor(weaved(uninjectors), InterceptorOrder.ComponentPreDestroy.UNINJECTION_INTERCEPTORS);
            }
            if (!destructors.isEmpty()) {
                configuration.addPreDestroyInterceptor(weaved(destructors), InterceptorOrder.ComponentPreDestroy.DESTRUCTION_INTERCEPTORS);
            }
            if (!userPreDestroy.isEmpty()) {
                configuration.addPreDestroyInterceptor(weaved(userPreDestroy), InterceptorOrder.ComponentPreDestroy.USER_INTERCEPTORS);
            }

            configuration.addPreDestroyInterceptor(Interceptors.getTerminalInterceptorFactory(), InterceptorOrder.ComponentPreDestroy.TERMINAL_INTERCEPTOR);
            configuration.addPreDestroyInterceptor(tcclInterceptor, InterceptorOrder.ComponentPreDestroy.TCCL_INTERCEPTOR);

            // @AroundInvoke interceptors
            final List<InterceptorDescription> classInterceptors = description.getClassInterceptors();
            final Map<MethodIdentifier, List<InterceptorDescription>> methodInterceptors = description.getMethodInterceptors();

            if (description.isIntercepted()) {

                for (final Method method : configuration.getDefinedComponentMethods()) {

                    //now add the interceptor that initializes and the interceptor that actually invokes to the end of the interceptor chain

                    configuration.addComponentInterceptor(method, Interceptors.getInitialInterceptorFactory(), InterceptorOrder.Component.INITIAL_INTERCEPTOR);
                    configuration.addComponentInterceptor(method, new ManagedReferenceMethodInterceptorFactory(instanceKey, method), InterceptorOrder.Component.TERMINAL_INTERCEPTOR);
                    if (description.isTimerServiceApplicable()) {
                        configuration.addTimeoutInterceptor(method, new ManagedReferenceMethodInterceptorFactory(instanceKey, method), InterceptorOrder.Component.TERMINAL_INTERCEPTOR);
                    }
                    // and also add the tccl interceptor
                    configuration.addComponentInterceptor(method, tcclInterceptor, InterceptorOrder.Component.TCCL_INTERCEPTOR);
                    if (description.isTimerServiceApplicable()) {
                        configuration.addTimeoutInterceptor(method, tcclInterceptor, InterceptorOrder.Component.TCCL_INTERCEPTOR);
                    }


                    final MethodIdentifier identifier = MethodIdentifier.getIdentifier(method.getReturnType(), method.getName(), method.getParameterTypes());

                    final List<InterceptorFactory> userAroundInvokes = new ArrayList<InterceptorFactory>();
                    final List<InterceptorFactory> userAroundTimeouts = new ArrayList<InterceptorFactory>();
                    // first add the default interceptors (if not excluded) to the deque
                    if (!description.isExcludeDefaultInterceptors() && !description.isExcludeDefaultInterceptors(identifier)) {
                        for (InterceptorDescription interceptorDescription : description.getDefaultInterceptors()) {
                            String interceptorClassName = interceptorDescription.getInterceptorClassName();
                            List<InterceptorFactory> aroundInvokes = userAroundInvokesByInterceptorClass.get(interceptorClassName);
                            if (aroundInvokes != null) {
                                userAroundInvokes.addAll(aroundInvokes);
                            }
                            if (description.isTimerServiceApplicable()) {
                                List<InterceptorFactory> aroundTimeouts = userAroundTimeoutsByInterceptorClass.get(interceptorClassName);
                                if (aroundTimeouts != null) {
                                    userAroundTimeouts.addAll(aroundTimeouts);
                                }
                            }
                        }
                    }

                    // now add class level interceptors (if not excluded) to the deque
                    if (!description.isExcludeClassInterceptors(identifier)) {
                        for (InterceptorDescription interceptorDescription : classInterceptors) {
                            String interceptorClassName = interceptorDescription.getInterceptorClassName();
                            List<InterceptorFactory> aroundInvokes = userAroundInvokesByInterceptorClass.get(interceptorClassName);
                            if (aroundInvokes != null) {
                                userAroundInvokes.addAll(aroundInvokes);
                            }
                            if (description.isTimerServiceApplicable()) {
                                List<InterceptorFactory> aroundTimeouts = userAroundTimeoutsByInterceptorClass.get(interceptorClassName);
                                if (aroundTimeouts != null) {
                                    userAroundTimeouts.addAll(aroundTimeouts);
                                }
                            }
                        }
                    }

                    // now add method level interceptors for to the deque so that they are triggered after the class interceptors
                    List<InterceptorDescription> methodLevelInterceptors = methodInterceptors.get(identifier);
                    if (methodLevelInterceptors != null) {
                        for (InterceptorDescription methodLevelInterceptor : methodLevelInterceptors) {
                            String interceptorClassName = methodLevelInterceptor.getInterceptorClassName();
                            List<InterceptorFactory> aroundInvokes = userAroundInvokesByInterceptorClass.get(interceptorClassName);
                            if (aroundInvokes != null) {
                                userAroundInvokes.addAll(aroundInvokes);
                            }
                            if (description.isTimerServiceApplicable()) {
                                List<InterceptorFactory> aroundTimeouts = userAroundTimeoutsByInterceptorClass.get(interceptorClassName);
                                if (aroundTimeouts != null) {
                                    userAroundTimeouts.addAll(aroundTimeouts);
                                }
                            }
                        }
                    }

                    // finally add the component level around invoke to the deque so that it's triggered last
                    userAroundInvokes.addAll(componentUserAroundInvoke);

                    configuration.addComponentInterceptor(method, weaved(userAroundInvokes), InterceptorOrder.Component.USER_INTERCEPTORS);
                    if (description.isTimerServiceApplicable()) {
                        userAroundTimeouts.addAll(componentUserAroundTimeout);
                        configuration.addTimeoutInterceptor(method, weaved(userAroundTimeouts), InterceptorOrder.Component.USER_INTERCEPTORS);
                    }
                }
            }


            //views
            for (ViewDescription view : description.getViews()) {
                Class<?> viewClass;
                try {
                    viewClass = module.getClassLoader().loadClass(view.getViewClassName());
                } catch (ClassNotFoundException e) {
                    throw new DeploymentUnitProcessingException("Could not load view class " + view.getViewClassName() + " for component " + configuration, e);
                }
                final ViewConfiguration viewConfiguration;

                final ProxyConfiguration proxyConfiguration = new ProxyConfiguration();
                proxyConfiguration.setProxyName(viewClass.getName() + "$$$view" + PROXY_ID.incrementAndGet());
                proxyConfiguration.setClassLoader(module.getClassLoader());
                proxyConfiguration.setProtectionDomain(viewClass.getProtectionDomain());
                proxyConfiguration.setMetadataSource(proxyReflectionIndex);
                if (view.isSerializable()) {
                    proxyConfiguration.addAdditionalInterface(Serializable.class);
                    if (view.isUseWriteReplace()) {
                        proxyConfiguration.addAdditionalInterface(WriteReplaceInterface.class);
                    }
                }

                //we define it in the modules class loader to prevent permgen leaks
                if (viewClass.isInterface()) {
                    proxyConfiguration.setSuperClass(Object.class);
                    proxyConfiguration.addAdditionalInterface(viewClass);
                    viewConfiguration = view.createViewConfiguration(viewClass, configuration, new ProxyFactory(proxyConfiguration));
                } else {
                    proxyConfiguration.setSuperClass(viewClass);
                    viewConfiguration = view.createViewConfiguration(viewClass, configuration, new ProxyFactory(proxyConfiguration));
                }
                for (final ViewConfigurator configurator : view.getConfigurators()) {
                    configurator.configure(context, configuration, view, viewConfiguration);
                }
                configuration.getViews().add(viewConfiguration);
            }

            configuration.getStartDependencies().add(new DependencyConfigurator<ComponentStartService>() {
                @Override
                public void configureDependency(final ServiceBuilder<?> serviceBuilder, ComponentStartService service) throws DeploymentUnitProcessingException {
                    for (final Map.Entry<ServiceName, ServiceBuilder.DependencyType> entry : description.getDependencies().entrySet()) {
                        serviceBuilder.addDependency(entry.getValue(), entry.getKey());
                    }

                }
            });
        }

        /**
         * Sets up all resource injections for a class. This takes into account injections that have been specified in the module and component deployment descriptors
         *
         * Note that this does not take superclasses into consideration, only injections on the current class
         *
         *
         * @param clazz The class to perform injection for
         * @param classDescription The class description, may be null
         * @param moduleDescription The module description
         * @param description The component description
         * @param configuration The component configuration
         * @param context The phase context
         * @param injectors The list of injectors for the current component
         * @param instanceKey The key that identifies the instance to inject in the interceptor context
         * @param uninjectors The list of uninjections for the current component
         * @throws DeploymentUnitProcessingException
         */
        private void mergeInjectionsForClass(final Class<?> clazz, final EEModuleClassDescription classDescription, final EEModuleDescription moduleDescription, final ComponentDescription description, final ComponentConfiguration configuration, final DeploymentPhaseContext context, final Deque<InterceptorFactory> injectors, final Object instanceKey, final Deque<InterceptorFactory> uninjectors) throws DeploymentUnitProcessingException {
            final Map<InjectionTarget, ResourceInjectionConfiguration> mergedInjections = new HashMap<InjectionTarget, ResourceInjectionConfiguration>();
            if (classDescription != null) {
                mergedInjections.putAll(classDescription.getInjectionConfigurations());
            }
            mergedInjections.putAll(moduleDescription.getResourceInjections(clazz.getName()));
            mergedInjections.putAll(description.getResourceInjections(clazz.getName()));

            for (final ResourceInjectionConfiguration injectionConfiguration : mergedInjections.values()) {
                final Object valueContextKey = new Object();
                final InjectedValue<ManagedReferenceFactory> managedReferenceFactoryValue = new InjectedValue<ManagedReferenceFactory>();
                configuration.getStartDependencies().add(new InjectedConfigurator(injectionConfiguration, configuration, context, managedReferenceFactoryValue));
                injectors.addFirst(injectionConfiguration.getTarget().createInjectionInterceptorFactory(instanceKey, valueContextKey, managedReferenceFactoryValue, context.getDeploymentUnit()));
                uninjectors.addLast(new ManagedReferenceReleaseInterceptorFactory(valueContextKey));
            }
        }

        private InterceptorClassDescription mergeInterceptorConfig(final Class<?> clazz, final EEModuleClassDescription classDescription, final ComponentDescription description) {
            final InterceptorClassDescription interceptorConfig;
            if (classDescription != null) {
                interceptorConfig = InterceptorClassDescription.merge(classDescription.getInterceptorClassDescription(), description.interceptorClassOverrides.get(clazz.getName()));
            } else {
                interceptorConfig = InterceptorClassDescription.merge(null, description.interceptorClassOverrides.get(clazz.getName()));
            }
            return interceptorConfig;
        }

        private boolean isNotOverriden(final Class<?> clazz, final Method method, final ClassReflectionIndex<?> componentClassIndex, final DeploymentReflectionIndex deploymentReflectionIndex) throws DeploymentUnitProcessingException {
            return Modifier.isPrivate(method.getModifiers()) || ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, componentClassIndex, method).getDeclaringClass() == clazz;
        }
    }

    static class InjectedConfigurator implements DependencyConfigurator<ComponentStartService> {

        private final ResourceInjectionConfiguration injectionConfiguration;
        private final ComponentConfiguration configuration;
        private final DeploymentPhaseContext context;
        private final InjectedValue<ManagedReferenceFactory> managedReferenceFactoryValue;

        InjectedConfigurator(final ResourceInjectionConfiguration injectionConfiguration, final ComponentConfiguration configuration, final DeploymentPhaseContext context, final InjectedValue<ManagedReferenceFactory> managedReferenceFactoryValue) {
            this.injectionConfiguration = injectionConfiguration;
            this.configuration = configuration;
            this.context = context;
            this.managedReferenceFactoryValue = managedReferenceFactoryValue;
        }

        public void configureDependency(final ServiceBuilder<?> serviceBuilder, ComponentStartService service) throws DeploymentUnitProcessingException {
            InjectionSource.ResolutionContext resolutionContext = new InjectionSource.ResolutionContext(
                    configuration.getComponentDescription().getNamingMode() == ComponentNamingMode.USE_MODULE,
                    configuration.getComponentName(),
                    configuration.getModuleName(),
                    configuration.getApplicationName()
            );
            injectionConfiguration.getSource().getResourceValue(resolutionContext, serviceBuilder, context, managedReferenceFactoryValue);
        }
    }

    public String getBeanDeploymentArchiveId() {
        return beanDeploymentArchiveId;
    }

    public void setBeanDeploymentArchiveId(final String beanDeploymentArchiveId) {
        this.beanDeploymentArchiveId = beanDeploymentArchiveId;
    }

    public void addResourceInjection(final ResourceInjectionConfiguration injection) {
        String className = injection.getTarget().getClassName();
        Map<InjectionTarget, ResourceInjectionConfiguration> map = resourceInjections.get(className);
        if (map == null) {
            resourceInjections.put(className, map = new HashMap<InjectionTarget, ResourceInjectionConfiguration>());
        }
        map.put(injection.getTarget(), injection);
    }

    public Map<InjectionTarget, ResourceInjectionConfiguration> getResourceInjections(final String className) {
        Map<InjectionTarget, ResourceInjectionConfiguration> injections = resourceInjections.get(className);
        if (injections == null) {
            return Collections.emptyMap();
        } else {
            return Collections.unmodifiableMap(injections);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14342.java