error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3307.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3307.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3307.java
text:
```scala
i@@f (instance != null && destroyMethod != null && destroyMethod.length() > 0) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.container;

import static org.apache.aries.blueprint.utils.ReflectionUtils.getRealCause;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.aries.blueprint.BeanProcessor;
import org.apache.aries.blueprint.ComponentDefinitionRegistry;
import org.apache.aries.blueprint.services.ExtendedBlueprintContainer;
import org.apache.aries.blueprint.Interceptor;
import org.apache.aries.blueprint.di.AbstractRecipe;
import org.apache.aries.blueprint.di.Recipe;
import org.apache.aries.blueprint.proxy.Collaborator;
import org.apache.aries.blueprint.proxy.ProxyUtils;
import org.apache.aries.blueprint.utils.ReflectionUtils;
import org.apache.aries.blueprint.utils.ReflectionUtils.PropertyDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.blueprint.container.ComponentDefinitionException;
import org.osgi.service.blueprint.container.ReifiedType;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A <code>Recipe</code> to create POJOs.
 *
 * @version $Rev$, $Date$
 */
public class BeanRecipe extends AbstractRecipe {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanRecipe.class);

    private final ExtendedBlueprintContainer blueprintContainer;
    private final LinkedHashMap<String,Object> properties = new LinkedHashMap<String,Object>();
    private final Object type;

    private String initMethod;
    private String destroyMethod;
    private List<Recipe> explicitDependencies;
    
    private Recipe factory;
    private String factoryMethod;
    private List<Object> arguments;
    private List<String> argTypes;
    private boolean reorderArguments;
    private final boolean allowsFieldInjection;
    private BeanMetadata interceptorLookupKey;
    

    public BeanRecipe(String name, ExtendedBlueprintContainer blueprintContainer, Object type, boolean allowsFieldInjection) {
        super(name);
        this.blueprintContainer = blueprintContainer;
        this.type = type;
        this.allowsFieldInjection = allowsFieldInjection;
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public Map<String, Object> getProperties() {
        return new LinkedHashMap<String, Object>(properties);
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    public void setFactoryMethod(String method) {
        this.factoryMethod = method;
    }
    
    public void setFactoryComponent(Recipe factory) {
        this.factory = factory;
    }
    
    public void setArgTypes(List<String> argTypes) {
        this.argTypes = argTypes;
    }
    
    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }
    
    public void setReorderArguments(boolean reorder) {
        this.reorderArguments = reorder;
    }
    
    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }
    
    public String getInitMethod() {
        return initMethod;
    }
    
    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }
    
    public String getDestroyMethod() {
        return destroyMethod;
    }

    public List<Recipe> getExplicitDependencies() {
        return explicitDependencies;
    }

    public void setExplicitDependencies(List<Recipe> explicitDependencies) {
        this.explicitDependencies = explicitDependencies;
    }

    public void setInterceptorLookupKey(BeanMetadata metadata) {
    	interceptorLookupKey = metadata;
    }
    
    @Override
    public List<Recipe> getConstructorDependencies() {
        List<Recipe> recipes = new ArrayList<Recipe>();
        if (explicitDependencies != null) {
            recipes.addAll(explicitDependencies);
        }
        if (arguments != null) {
            for (Object argument : arguments) {
                if (argument instanceof Recipe) {
                    recipes.add((Recipe)argument);
                }
            }
        }
        return recipes;
    }
    
    public List<Recipe> getDependencies() {
        List<Recipe> recipes = new ArrayList<Recipe>();
        for (Object o : properties.values()) {
            if (o instanceof Recipe) {
                Recipe recipe = (Recipe) o;
                recipes.add(recipe);
            }
        }
        recipes.addAll(getConstructorDependencies());
        return recipes; 
    }

    private void instantiateExplicitDependencies() {
        if (explicitDependencies != null) {
            for (Recipe recipe : explicitDependencies) {
                recipe.create();
            }
        }
    }

    @Override
    protected Class loadClass(String className) {
        ClassLoader loader = type instanceof Class ? ((Class) type).getClassLoader() : null;
        ReifiedType t = loadType(className, loader);
        return t != null ? t.getRawClass() : null;
    }

    @Override
    protected ReifiedType loadType(String className) {
        return loadType(className, type instanceof Class ? ((Class) type).getClassLoader() : null);
    }

    private Object getInstance() throws ComponentDefinitionException {
        Object instance;
        
        // Instanciate arguments
        List<Object> args = new ArrayList<Object>();
        List<ReifiedType> argTypes = new ArrayList<ReifiedType>();
        if (arguments != null) {
            for (int i = 0; i < arguments.size(); i++) {
                Object arg = arguments.get(i);
                if (arg instanceof Recipe) {
                    args.add(((Recipe) arg).create());
                } else {
                    args.add(arg);
                }
                if (this.argTypes != null) {
                    argTypes.add(this.argTypes.get(i) != null ? loadType(this.argTypes.get(i)) : null);
                }
            }
        }

        if (factory != null) {
            // look for instance method on factory object
            Object factoryObj = factory.create();
            
            // If the factory is a service reference, we need to get hold of the actual proxy for the service
            if (factoryObj instanceof ReferenceRecipe.ServiceProxyWrapper) {
                try {
                    factoryObj = ((ReferenceRecipe.ServiceProxyWrapper) factoryObj).convert(new ReifiedType(Object.class));
                } catch (Exception e) {
                    throw new ComponentDefinitionException("Error when instantiating bean " + getName() + " of class " + getType(), getRealCause(e));
                }
            }
            
            // Map of matching methods
            Map<Method, List<Object>> matches = findMatchingMethods(factoryObj.getClass(), factoryMethod, true, args, argTypes);
            if (matches.size() == 1) {
                try {
                    Map.Entry<Method, List<Object>> match = matches.entrySet().iterator().next();
                    instance = invoke(match.getKey(), factoryObj, match.getValue().toArray());
                } catch (Throwable e) {
                    throw new ComponentDefinitionException("Error when instantiating bean " + getName() + " of class " + getType(), getRealCause(e));
                }
            } else if (matches.size() == 0) {
                throw new ComponentDefinitionException("Unable to find a matching factory method " + factoryMethod + " on class " + factoryObj.getClass().getName() + " for arguments " + args + " when instanciating bean " + getName());
            } else {
                throw new ComponentDefinitionException("Multiple matching factory methods " + factoryMethod + " found on class " + factoryObj.getClass().getName() + " for arguments " + args + " when instanciating bean " + getName() + ": " + matches.keySet());
            }
        } else if (factoryMethod != null) {
            // Map of matching methods
            Map<Method, List<Object>> matches = findMatchingMethods(getType(), factoryMethod, false, args, argTypes);
            if (matches.size() == 1) {
                try {
                    Map.Entry<Method, List<Object>> match = matches.entrySet().iterator().next();
                    instance = invoke(match.getKey(), null, match.getValue().toArray());
                } catch (Throwable e) {
                    throw new ComponentDefinitionException("Error when instanciating bean " + getName() + " of class " + getType(), getRealCause(e));
                }
            } else if (matches.size() == 0) {
                throw new ComponentDefinitionException("Unable to find a matching factory method " + factoryMethod + " on class " + getType().getName() + " for arguments " + args + " when instanciating bean " + getName());
            } else {
                throw new ComponentDefinitionException("Multiple matching factory methods " + factoryMethod + " found on class " + getType().getName() + " for arguments " + args + " when instanciating bean " + getName() + ": " + matches.keySet());
            }
        } else {
            if (getType() == null) {
                throw new ComponentDefinitionException("No factoryMethod nor class is defined for this bean");
            }
            // Map of matching constructors
            Map<Constructor, List<Object>> matches = findMatchingConstructors(getType(), args, argTypes);
            if (matches.size() == 1) {
                try {
                    Map.Entry<Constructor, List<Object>> match = matches.entrySet().iterator().next();
                    instance = newInstance(match.getKey(), match.getValue().toArray());
                } catch (Throwable e) {
                    throw new ComponentDefinitionException("Error when instanciating bean " + getName() + " of class " + getType(), getRealCause(e));
                }
            } else if (matches.size() == 0) {
                throw new ComponentDefinitionException("Unable to find a matching constructor on class " + getType().getName() + " for arguments " + args + " when instanciating bean " + getName());
            } else {
                throw new ComponentDefinitionException("Multiple matching constructors found on class " + getType().getName() + " for arguments " + args + " when instanciating bean " + getName() + ": " + matches.keySet());
            }
        }
        
        return instance;
    }

    private Map<Method, List<Object>> findMatchingMethods(Class type, String name, boolean instance, List<Object> args, List<ReifiedType> types) {
        Map<Method, List<Object>> matches = new HashMap<Method, List<Object>>();
        // Get constructors
        List<Method> methods = new ArrayList<Method>(Arrays.asList(type.getMethods()));
        // Discard any signature with wrong cardinality
        for (Iterator<Method> it = methods.iterator(); it.hasNext();) {
            Method mth = it.next();
            if (!mth.getName().equals(name)) {
                it.remove();
            } else if (mth.getParameterTypes().length != args.size()) {
                it.remove();
            } else if (instance ^ !Modifier.isStatic(mth.getModifiers())) {
                it.remove();
            } else if (mth.isBridge()) {
                it.remove();
            }
        }
        
        // on some JVMs (J9) hidden static methods are returned by Class.getMethods so we need to weed them out
        // to reduce ambiguity
        if (!instance) {
        	methods = applyStaticHidingRules(methods);
        }
        
        // Find a direct match with assignment
        if (matches.size() != 1) {
            Map<Method, List<Object>> nmatches = new HashMap<Method, List<Object>>();
            for (Method mth : methods) {
                boolean found = true;
                List<Object> match = new ArrayList<Object>();
                for (int i = 0; i < args.size(); i++) {
                    ReifiedType argType = new GenericType(mth.getGenericParameterTypes()[i]);
                    if (types.get(i) != null && !argType.getRawClass().equals(types.get(i).getRawClass())) {
                        found = false;
                        break;
                    }
                    if (!AggregateConverter.isAssignable(args.get(i), argType)) {
                        found = false;
                        break;
                    }
                    try {
                        match.add(convert(args.get(i), mth.getGenericParameterTypes()[i]));
                    } catch (Throwable t) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    nmatches.put(mth, match);
                }
            }
            if (nmatches.size() > 0) {
                matches = nmatches;
            }
        }
        // Find a direct match with conversion
        if (matches.size() != 1) {
            Map<Method, List<Object>> nmatches = new HashMap<Method, List<Object>>();
            for (Method mth : methods) {
                boolean found = true;
                List<Object> match = new ArrayList<Object>();
                for (int i = 0; i < args.size(); i++) {
                    ReifiedType argType = new GenericType(mth.getGenericParameterTypes()[i]);
                    if (types.get(i) != null && !argType.getRawClass().equals(types.get(i).getRawClass())) {
                        found = false;
                        break;
                    }
                    try {
                        Object val = convert(args.get(i), argType);
                        match.add(val);
                    } catch (Throwable t) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    nmatches.put(mth, match);
                }
            }
            if (nmatches.size() > 0) {
                matches = nmatches;
            }
        }
        // Start reordering with assignment
        if (matches.size() != 1 && reorderArguments && args.size() > 1) {
            Map<Method, List<Object>> nmatches = new HashMap<Method, List<Object>>();
            for (Method mth : methods) {
                ArgumentMatcher matcher = new ArgumentMatcher(mth.getGenericParameterTypes(), false);
                List<Object> match = matcher.match(args, types);
                if (match != null) {
                    nmatches.put(mth, match);
                }
            }
            if (nmatches.size() > 0) {
                matches = nmatches;
            }
        }
        // Start reordering with conversion
        if (matches.size() != 1 && reorderArguments && args.size() > 1) {
            Map<Method, List<Object>> nmatches = new HashMap<Method, List<Object>>();
            for (Method mth : methods) {
                ArgumentMatcher matcher = new ArgumentMatcher(mth.getGenericParameterTypes(), true);
                List<Object> match = matcher.match(args, types);
                if (match != null) {
                    nmatches.put(mth, match);
                }
            }
            if (nmatches.size() > 0) {
                matches = nmatches;
            }
        }
        
        return matches;
    }
    
    private static List<Method> applyStaticHidingRules(Collection<Method> methods) {
    	List<Method> result = new ArrayList<Method>(methods.size());
    	for (Method m : methods) {
    		boolean toBeAdded = true;

    		Iterator<Method> it = result.iterator();
    		inner: while (it.hasNext()) {
    			Method other = it.next();
    			if (hasIdenticalParameters(m, other)) {
    				Class<?> mClass = m.getDeclaringClass();
    				Class<?> otherClass = other.getDeclaringClass();
    				
    				if (mClass.isAssignableFrom(otherClass)) {
    					toBeAdded = false;
    					break inner;
    				} else if (otherClass.isAssignableFrom(mClass)) {
    					it.remove();
    				}
    			}
    		}
    		
    		if (toBeAdded) result.add(m);
    	}
    	
    	return result;
    }
    
    private static boolean hasIdenticalParameters(Method one, Method two) {
		Class<?>[] oneTypes = one.getParameterTypes();
		Class<?>[] twoTypes = two.getParameterTypes();
    	
		if (oneTypes.length != twoTypes.length) return false;
		
		for (int i=0; i<oneTypes.length; i++) {
			if (!oneTypes[i].equals(twoTypes[i])) return false;
		}
		
		return true;
    }

    private Map<Constructor, List<Object>> findMatchingConstructors(Class type, List<Object> args, List<ReifiedType> types) {
        Map<Constructor, List<Object>> matches = new HashMap<Constructor, List<Object>>();
        // Get constructors
        List<Constructor> constructors = new ArrayList<Constructor>(Arrays.asList(type.getConstructors()));
        // Discard any signature with wrong cardinality
        for (Iterator<Constructor> it = constructors.iterator(); it.hasNext();) {
            if (it.next().getParameterTypes().length != args.size()) {
                it.remove();
            }
        }
        // Find a direct match with assignment
        if (matches.size() != 1) {
            Map<Constructor, List<Object>> nmatches = new HashMap<Constructor, List<Object>>();
            for (Constructor cns : constructors) {
                boolean found = true;
                List<Object> match = new ArrayList<Object>();
                for (int i = 0; i < args.size(); i++) {
                    ReifiedType argType = new GenericType(cns.getGenericParameterTypes()[i]);
                    if (types.get(i) != null && !argType.getRawClass().equals(types.get(i).getRawClass())) {
                        found = false;
                        break;
                    }
                    if (!AggregateConverter.isAssignable(args.get(i), argType)) {
                        found = false;
                        break;
                    }
                    try {
                        match.add(convert(args.get(i), cns.getGenericParameterTypes()[i]));
                    } catch (Throwable t) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    nmatches.put(cns, match);
                }
            }
            if (nmatches.size() > 0) {
                matches = nmatches;
            }
        }
        // Find a direct match with conversion
        if (matches.size() != 1) {
            Map<Constructor, List<Object>> nmatches = new HashMap<Constructor, List<Object>>();
            for (Constructor cns : constructors) {
                boolean found = true;
                List<Object> match = new ArrayList<Object>();
                for (int i = 0; i < args.size(); i++) {
                    ReifiedType argType = new GenericType(cns.getGenericParameterTypes()[i]);
                    if (types.get(i) != null && !argType.getRawClass().equals(types.get(i).getRawClass())) {
                        found = false;
                        break;
                    }
                    try {
                        Object val = convert(args.get(i), argType);
                        match.add(val);
                    } catch (Throwable t) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    nmatches.put(cns, match);
                }
            }
            if (nmatches.size() > 0) {
                matches = nmatches;
            }
        }
        // Start reordering with assignment
        if (matches.size() != 1 && reorderArguments && arguments.size() > 1) {
            Map<Constructor, List<Object>> nmatches = new HashMap<Constructor, List<Object>>();
            for (Constructor cns : constructors) {
                ArgumentMatcher matcher = new ArgumentMatcher(cns.getGenericParameterTypes(), false);
                List<Object> match = matcher.match(args, types);
                if (match != null) {
                    nmatches.put(cns, match);
                }
            }
            if (nmatches.size() > 0) {
                matches = nmatches;
            }
        }
        // Start reordering with conversion
        if (matches.size() != 1 && reorderArguments && arguments.size() > 1) {
            Map<Constructor, List<Object>> nmatches = new HashMap<Constructor, List<Object>>();
            for (Constructor cns : constructors) {
                ArgumentMatcher matcher = new ArgumentMatcher(cns.getGenericParameterTypes(), true);
                List<Object> match = matcher.match(args, types);
                if (match != null) {
                    nmatches.put(cns, match);
                }
            }
            if (nmatches.size() > 0) {
                matches = nmatches;
            }
        }
        return matches;
    }

    /**
     * Returns init method (if any). Throws exception if the init-method was set explicitly on the bean
     * and the method is not found on the instance.
     */
    protected Method getInitMethod(Object instance) throws ComponentDefinitionException {
        Method method = null;        
        if (initMethod != null && initMethod.length() > 0) {
            method = ReflectionUtils.getLifecycleMethod(instance.getClass(), initMethod);
            if (method == null) {
                throw new ComponentDefinitionException("Component '" + getName() + "' does not have init-method: " + initMethod);
            }
        }
        return method;
    }

    /**
     * Returns destroy method (if any). Throws exception if the destroy-method was set explicitly on the bean
     * and the method is not found on the instance.
     */
    public Method getDestroyMethod(Object instance) throws ComponentDefinitionException {
        Method method = null;        
        if (destroyMethod != null && destroyMethod.length() > 0) {
            method = ReflectionUtils.getLifecycleMethod(instance.getClass(), destroyMethod);
            if (method == null) {
                throw new ComponentDefinitionException("Component '" + getName() + "' does not have destroy-method: " + destroyMethod);
            }
        }
        return method;
    }
    
    /**
     * Small helper class, to construct a chain of BeanCreators.
     * <br> 
     * Each bean creator in the chain will return a bean that has been 
     * processed by every BeanProcessor in the chain before it.
     */
    private static class BeanCreatorChain implements BeanProcessor.BeanCreator {
        public enum ChainType{Before,After};
        private final BeanProcessor.BeanCreator parentBeanCreator;
        private final BeanProcessor parentBeanProcessor;
        private final BeanMetadata beanData;
        private final String beanName;        
        private final ChainType when;
        public BeanCreatorChain(BeanProcessor.BeanCreator parentBeanCreator, 
                                BeanProcessor parentBeanProcessor,
                                BeanMetadata beanData,
                                String beanName,
                                ChainType when){
            this.parentBeanCreator = parentBeanCreator;
            this.parentBeanProcessor = parentBeanProcessor;
            this.beanData = beanData;
            this.beanName = beanName;
            this.when = when;
        }

        public Object getBean() {
            Object previousBean = parentBeanCreator.getBean();
            Object processed = null;
            switch(when){
                case Before :
                  processed = parentBeanProcessor.beforeInit(previousBean, beanName, parentBeanCreator, beanData);
                  break;
                case After:
                  processed = parentBeanProcessor.afterInit(previousBean, beanName, parentBeanCreator, beanData);
                  break;
            }
            return processed;
        }   
    }
    
    private Object runBeanProcPreInit(Object obj){
        String beanName = getName();
        BeanMetadata beanData = (BeanMetadata) blueprintContainer
          .getComponentDefinitionRegistry().getComponentDefinition(beanName);        
        List<BeanProcessor> processors = blueprintContainer.getProcessors(BeanProcessor.class);
        
        //The start link of the chain, that provides the 
        //original, unprocessed bean to the head of the chain.
        BeanProcessor.BeanCreator initialBeanCreator = new BeanProcessor.BeanCreator() {            
            public Object getBean() {
                Object obj = getInstance();
                //getinit, getdestroy, addpartial object don't need calling again.
                //however, property injection does.
                setProperties(obj);
                return obj;
            }
        };

        BeanProcessor.BeanCreator currentCreator = initialBeanCreator;
        for(BeanProcessor processor : processors){
            obj = processor.beforeInit(obj, getName(), currentCreator, beanData);
            currentCreator = new BeanCreatorChain(currentCreator, processor, beanData, beanName, BeanCreatorChain.ChainType.Before);
        }
        return obj;
    }
    
    private void runBeanProcInit(Method initMethod, Object obj){
        // call init method
        if (initMethod != null) {
            try {
                invoke(initMethod, obj, (Object[]) null);
            } catch (Throwable t) {
                throw new ComponentDefinitionException("Unable to intialize bean " + getName(), getRealCause(t));
            }
        }   
    }
    
    private Object runBeanProcPostInit(Object obj){
        String beanName = getName();
        BeanMetadata beanData = (BeanMetadata) blueprintContainer
          .getComponentDefinitionRegistry().getComponentDefinition(beanName);        
        List<BeanProcessor> processors = blueprintContainer.getProcessors(BeanProcessor.class);
        
        //The start link of the chain, that provides the 
        //original, unprocessed bean to the head of the chain.
        BeanProcessor.BeanCreator initialBeanCreator = new BeanProcessor.BeanCreator() {            
            public Object getBean() {                                
                Object obj = getInstance();
                //getinit, getdestroy, addpartial object don't need calling again.
                //however, property injection does.
                setProperties(obj);
                //as this is the post init chain, new beans need to go thru 
                //the pre-init chain, and then have init called, before 
                //being passed along the post-init chain.
                obj = runBeanProcPreInit(obj);
                runBeanProcInit(getInitMethod(obj), obj);
                return obj;
            }
        };

        BeanProcessor.BeanCreator currentCreator = initialBeanCreator;
        for(BeanProcessor processor : processors){
            obj = processor.afterInit(obj, getName(), currentCreator, beanData);
            currentCreator = new BeanCreatorChain(currentCreator, processor, beanData, beanName, BeanCreatorChain.ChainType.After);
        }
        return obj;
    }    
    
    private Object addInterceptors(final Object original)
            throws ComponentDefinitionException {

        Object intercepted = null;
        ComponentDefinitionRegistry reg = blueprintContainer
                .getComponentDefinitionRegistry();
        List<Interceptor> interceptors = reg.getInterceptors(interceptorLookupKey);
        if (interceptors != null && interceptors.size() > 0) {
            try {
              Bundle b = FrameworkUtil.getBundle(original.getClass());
              if (b == null) {
                // we have a class from the framework parent, so use our bundle for proxying.
                b = blueprintContainer.getBundleContext().getBundle();
              }
              intercepted = BlueprintExtender.getProxyManager().createInterceptingProxy(b, 
                  ProxyUtils.asList(original.getClass()), original, 
                  new Collaborator(interceptorLookupKey, interceptors));
            } catch (org.apache.aries.proxy.UnableToProxyException e) {
                  Bundle b = blueprintContainer.getBundleContext().getBundle();
                  throw new ComponentDefinitionException("Unable to create proxy for bean " + name + " in bundle " + b.getSymbolicName() + " version " + b.getVersion(), e);
            }
        } else {
            intercepted = original;
        }
        return intercepted;
    }
        
    @Override
    protected Object internalCreate() throws ComponentDefinitionException {
        
        instantiateExplicitDependencies();

        Object obj = getInstance();
                
        // check for init lifecycle method (if any)
        Method initMethod = getInitMethod(obj);
        
        // check for destroy lifecycle method (if any)
        getDestroyMethod(obj);
        
        // Add partially created object to the container
//        if (initMethod == null) {
            addPartialObject(obj);
//        }

        // inject properties
        setProperties(obj);
        
        obj = runBeanProcPreInit(obj);
        
        runBeanProcInit(initMethod, obj);
        
        obj = runBeanProcPostInit(obj);
        
        obj = addInterceptors(obj);
        
        return obj;
    }
    
    @Override
    public void destroy(Object obj) {
        for (BeanProcessor processor : blueprintContainer.getProcessors(BeanProcessor.class)) {
            processor.beforeDestroy(obj, getName());
        }
        try {
            Method method = getDestroyMethod(obj);
            if (method != null) {
                invoke(method, obj, (Object[]) null);
            }
        } catch (ComponentDefinitionException e) {
            // This exception occurs if the destroy method does not exist, so we just output the exception message.
            LOGGER.error(e.getMessage());
        } catch (InvocationTargetException ite) {
          Throwable t = ite.getTargetException();
          BundleContext ctx = blueprintContainer.getBundleContext();
          Bundle b = ctx.getBundle();
          String bundleIdentifier = b.getSymbolicName() + '/' + b.getVersion();
          LOGGER.error("The blueprint bean " + getName() + " in bundle " + bundleIdentifier + " incorrectly threw an exception from its destroy method.", t);
        } catch (Exception e) {
            BundleContext ctx = blueprintContainer.getBundleContext();
            Bundle b = ctx.getBundle();
            String bundleIdentifier = b.getSymbolicName() + '/' + b.getVersion();
            LOGGER.error("An exception occurred while calling the destroy method of the blueprint bean " + getName() + " in bundle " + bundleIdentifier + ".", getRealCause(e));
        }
        for (BeanProcessor processor : blueprintContainer.getProcessors(BeanProcessor.class)) {
            processor.afterDestroy(obj, getName());
        }
    }

    public void setProperties(Object instance) throws ComponentDefinitionException {
        // clone the properties so they can be used again
        Map<String,Object> propertyValues = new LinkedHashMap<String,Object>(properties);
        setProperties(propertyValues, instance, instance.getClass());
    }

    public Class getType() {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof String) {
            return loadClass((String) type);
        } else {
            return null;
        }
    }

    private void setProperties(Map<String, Object> propertyValues, Object instance, Class clazz) {
        // set remaining properties
        for (Map.Entry<String, Object> entry : propertyValues.entrySet()) {
            String propertyName = entry.getKey();
            Object propertyValue = entry.getValue();

            setProperty(instance, clazz, propertyName, propertyValue);
        }

    }

    private void setProperty(Object instance, Class clazz, String propertyName, Object propertyValue) {
        String[] names = propertyName.split("\\.");
        for (int i = 0; i < names.length - 1; i++) {
            PropertyDescriptor pd = getPropertyDescriptor(clazz, names[i]);
            if (pd.allowsGet()) {
                try {
                    instance = pd.get(instance, blueprintContainer);
                } catch (Exception e) {
                    throw new ComponentDefinitionException("Error getting property: " + names[i] + " on bean " + getName() + " when setting property " + propertyName + " on class " + clazz.getName(), getRealCause(e));
                }
                if (instance == null) {
                    throw new ComponentDefinitionException("Error setting compound property " + propertyName + " on bean " + getName() + ". Property " + names[i] + " is null");
                }
                clazz = instance.getClass();
            } else {
                throw new ComponentDefinitionException("No getter for " + names[i] + " property on bean " + getName() + " when setting property " + propertyName + " on class " + clazz.getName());
            }
        }
        
        // Instantiate value
        if (propertyValue instanceof Recipe) {
            propertyValue = ((Recipe) propertyValue).create();
        }

        final PropertyDescriptor pd = getPropertyDescriptor(clazz, names[names.length - 1]);
        if (pd.allowsSet()) {
            try {
                pd.set(instance, propertyValue, blueprintContainer);
            } catch (Exception e) {
                throw new ComponentDefinitionException("Error setting property: " + pd, getRealCause(e));
            }
        } else {
            throw new ComponentDefinitionException("No setter for " + names[names.length - 1] + " property");
        }
    }

    private ReflectionUtils.PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String name) {
        for (ReflectionUtils.PropertyDescriptor pd : ReflectionUtils.getPropertyDescriptors(clazz, allowsFieldInjection)) {
            if (pd.getName().equals(name)) {
                return pd;
            }
        }
        throw new ComponentDefinitionException("Unable to find property descriptor " + name + " on class " + clazz.getName());
    }
        
    private Object invoke(Method method, Object instance, Object... args) throws Exception {
        return ReflectionUtils.invoke(blueprintContainer.getAccessControlContext(), method, instance, args);        
    }
    
    private Object newInstance(Constructor constructor, Object... args) throws Exception {
        return ReflectionUtils.newInstance(blueprintContainer.getAccessControlContext(), constructor, args);         
    }
    
    private static Object UNMATCHED = new Object();

    private class ArgumentMatcher {

        private final List<TypeEntry> entries;
        private final boolean convert;

        public ArgumentMatcher(Type[] types, boolean convert) {
            entries = new ArrayList<TypeEntry>();
            for (Type type : types) {
                entries.add(new TypeEntry(new GenericType(type)));
            }
            this.convert = convert;
        }

        public List<Object> match(List<Object> arguments, List<ReifiedType> forcedTypes) {
            if (find(arguments, forcedTypes)) {
                return getArguments();
            }
            return null;
        }

        private List<Object> getArguments() {
            List<Object> list = new ArrayList<Object>();
            for (TypeEntry entry : entries) {
                if (entry.argument == UNMATCHED) {
                    throw new RuntimeException("There are unmatched types");
                } else {
                    list.add(entry.argument);
                }
            }
            return list;
        }

        private boolean find(List<Object> arguments, List<ReifiedType> forcedTypes) {
            if (entries.size() == arguments.size()) {
                boolean matched = true;
                for (int i = 0; i < arguments.size() && matched; i++) {
                    matched = find(arguments.get(i), forcedTypes.get(i));
                }
                return matched;
            }
            return false;
        }

        private boolean find(Object arg, ReifiedType forcedType) {
            for (TypeEntry entry : entries) {
                Object val = arg;
                if (entry.argument != UNMATCHED) {
                    continue;
                }
                if (forcedType != null) {
                    if (!forcedType.equals(entry.type)) {
                        continue;
                    }
                } else if (arg != null) {
                    if (convert) {
                        try {
                            // TODO: call canConvert instead of convert()
                            val = convert(arg, entry.type);
                        } catch (Throwable t) {
                            continue;
                        }
                    } else {
                        if (!AggregateConverter.isAssignable(arg, entry.type)) {
                            continue;
                        }
                    }
                }
                entry.argument = val;
                return true;
            }
            return false;
        }

    }

    private static class TypeEntry {

        private final ReifiedType type;
        private Object argument;

        public TypeEntry(ReifiedType type) {
            this.type = type;
            this.argument = UNMATCHED;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3307.java