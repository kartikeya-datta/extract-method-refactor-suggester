error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5883.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5883.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5883.java
text:
```scala
t@@hrow new CoreException(new Status(IStatus.ERROR, element.getNamespace(),

/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.components;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.components.framework.ClassIdentifier;
import org.eclipse.ui.internal.components.framework.ComponentException;
import org.eclipse.ui.internal.components.framework.ServiceFactory;
import org.eclipse.ui.internal.components.registry.ComponentRegistry;
import org.eclipse.ui.internal.components.registry.ComponentScope;
import org.eclipse.ui.internal.components.registry.ExtensionPointManager;
import org.eclipse.ui.internal.components.registry.IComponentScope;
import org.eclipse.ui.internal.components.registry.ServiceExtensionPoint;
import org.osgi.framework.Bundle;


/**
 * Creates an IComponent, given a set of local services.
 * 
 * @since 3.1
 */
public class ComponentUtil {
    
    public static final Object[] EMPTY_ARRAY = new Object[0];
    
    private static ComponentRegistry registry;
    private static ExtensionPointManager uiExtensionPoints = null;
    private static ServiceExtensionPoint partServices = null;

    
	private ComponentUtil() {
	}
	
    public static ComponentRegistry getRegistry() {
        if (registry != null) {
            return registry;
        }
        registry = new ComponentRegistry();
        Bundle uiBundle = Platform.getBundle(PlatformUI.PLUGIN_ID);
        uiExtensionPoints = new ExtensionPointManager(uiBundle);
        partServices = new ServiceExtensionPoint(uiExtensionPoints, ComponentUtil.registry);

        return registry;
    }
    
    public static void disposeRegistry() {
      if (partServices != null) {
          partServices.dispose();
          partServices = null;
      }
      
      if (uiExtensionPoints != null) {
          uiExtensionPoints.dispose();
          uiExtensionPoints = null;
      }
      
      registry = null;
    }
    
	public static String getSimpleClassName(String fullyQualifiedClassName) {
	    int idx = fullyQualifiedClassName.lastIndexOf('.') + 1;
	    
	    return fullyQualifiedClassName.substring(idx, fullyQualifiedClassName.length());
	}
	
	public static Throwable getCause(Throwable toQuery) {
	    if (toQuery == null) {
	        return null;
	    }
	    
	    if (toQuery instanceof CoreException) {
	    	Throwable cause = ((CoreException)toQuery).getStatus().getException();
	    	if (cause != null) {
	    		return cause;
	    	}
	    }
	    
	    Throwable cause = toQuery.getCause();
	    if (cause == null) {
	        cause = toQuery;
	    }
	    
	    return cause;
	}
	
    public static Class loadClass(ClassIdentifier type) throws ComponentException {
    	try {
	        Bundle pluginBundle = Platform.getBundle(type.getNamespace());
	        Class result = pluginBundle.loadClass(type.getTypeName());
	        return result;
	    } catch (ClassNotFoundException e) {
	        throw new ComponentException(type.getTypeName(),
	               NLS.bind(ComponentMessages.ComponentUtil_class_not_found,
	                        type.getNamespace(), type.getTypeName()), null);
	    }
    }
    
    public static final ClassIdentifier getClassFromInitializationData(IConfigurationElement config, Object data) throws CoreException {
        
        if (!(data instanceof String)) {
            String msg = NLS.bind(
                    ComponentMessages.ReflectionFactory_missing_data,  config.toString()); 
            
            throw new CoreException(new Status(IStatus.ERROR, config.getNamespace(), Status.OK, 
                    msg, null));
        }
        
        return new ClassIdentifier(config.getNamespace(), (String)data);
    }
    
	public static String getMessage(Throwable toQuery) {
	    String msg = toQuery.getMessage();
	    
	    if (msg == null) {
	        msg = toQuery.toString();
	    }
	    
	    return msg;
	}
	
//	/**
//	 * Creates and initializes a component implementation, given a Class that implements
//	 * IComponent. Returns a fully-initialized component. 
//	 * 
//	 * @param desiredService
//	 * @param otherServices
//	 * @return a fully initialized component
//	 * @throws ComponentException if unable to create or initialize the component
//	 */
//    public static Object createComponentImplementation(Class desiredService, IComponentProvider otherServices) throws ComponentException {
//        try {
//        	Assert.isTrue(IComponent.class.isAssignableFrom(desiredService));
//        	
//        	IComponent component = (IComponent)desiredService.newInstance();
//        	
//        	component.init(otherServices);
//        		        
//	        return component;
//	    } catch (ComponentException e) {
//	        throw new ComponentException(desiredService, e);
//	    } catch (IllegalArgumentException e) {
//	        throw new ComponentException(desiredService, e);
//	    } catch (InstantiationException e) {
//	        throw new ComponentException(desiredService, e);
//	    } catch (IllegalAccessException e) {
//	        throw new ComponentException(desiredService, e);
//	    }
//    }

    public static String getAttribute(IConfigurationElement element, String attributeId) throws CoreException {
        String result = element.getAttribute(attributeId);
        if (result == null) {
            throw new CoreException(new Status(IStatus.ERROR, element.getDeclaringExtension().getNamespace(),
                    IStatus.OK, NLS.bind(ComponentMessages.ComponentUtil_missing_attribute, 
                            new Object[] {element.getName(), attributeId, 
                            element.getDeclaringExtension().getExtensionPointUniqueIdentifier()}), null
                            ));
        }
        
        return result;
    }
    
    public static Throwable getMostSpecificCause(Throwable exception) {
        return getMostSpecificException(getCause(exception));
    }
    
    /**
     * @since 3.1 
     *
     * @param exception
     * @return
     */
    private static Throwable getMostSpecificException(Throwable exception) {
        Throwable cause = getCause(exception);
        if (cause == null || cause == exception) {
            return exception;
        }
        
        return getMostSpecificException(cause);
    }

	/**
	 * @param implementation
	 * @throws ComponentException
	 */
	public static Object createInstance(Class implementation) throws ComponentException {
		try {
			return implementation.newInstance();
		} catch (InstantiationException e) {
			throw new ComponentException(implementation, e);
		} catch (IllegalAccessException e) {
			throw new ComponentException(implementation, e);
		}
	}

    /**
     * Returns meta-information about the component scope with the given ID. 
     * This is intended for introspection without activating plugins. 
     * For example, this could be used to print out a list of all services 
     * available in a particular scope. Clients who want to instantiate services 
     * in the given scope should call <code>getContext</code> instead.
     * 
     * <i>EXPERIMENTAL</i> it is likely that this method will change in the
     * near future.
     * 
     * @param scope scope ID
     * @return an IComponentScope containing meta-information about the given
     * scope ID, or null if the given scope is unknown
     */
    public static IComponentScope getScope(String scope) {
    	return getRegistry().getScope(scope);
    }

    /**
     * Returns a context for the given scope ID. This context can be used
     * to instantiate components from the org.eclipse.core.components.services
     * that are included in the given scope.
     * 
     * <p>
     * The resulting context will be able to create components if:
     * <ul>
     * <li>The component is belongs to the same scope</li>
     * <li>The component is belongs to a scope which is included in the given scope</li>
     * </ul>
     * 
     * <p> 
     * Note that components that are required by the given scope will show up
     * as dependencies in the resulting context but cannot be created by
     * the context.
     * </p>
     * 
     * <p>
     * All dependencies of the given context should be satisfied before
     * attempting to instantiate any objects from the context.
     * </p> 
     *
     * @param scope ID of the scope to return
     * @return a container context for the given scope
     */
    public static ServiceFactory getContext(String scope) {
        IComponentScope s = getScope(scope);
        
        if (s == null) {
            return null;
        }
        
        return ((ComponentScope)s).getContext();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5883.java