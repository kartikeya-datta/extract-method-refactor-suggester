error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2099.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2099.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2099.java
text:
```scala
b@@oolean found = false;

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
package org.jboss.as.weld.services.bootstrap;

import static org.jboss.as.weld.util.ResourceInjectionUtilities.getResourceAnnotated;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ee.component.EEApplicationDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.deployment.ContextNames.BindInfo;
import org.jboss.as.weld.WeldMessages;
import org.jboss.as.weld.util.ResourceInjectionUtilities;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.vfs.VirtualFile;
import org.jboss.weld.injection.spi.EjbInjectionServices;
import org.jboss.weld.injection.spi.ResourceReference;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;
import org.jboss.weld.injection.spi.helpers.SimpleResourceReference;
import org.jboss.weld.logging.BeanLogger;
import org.jboss.weld.util.reflection.Reflections;

/**
 * Implementation of EjbInjectionServices.
 *
 * @author Stuart Douglas
 */
public class WeldEjbInjectionServices extends AbstractResourceInjectionServices implements EjbInjectionServices {

    private final EEApplicationDescription applicationDescription;

    private final VirtualFile deploymentRoot;


    public WeldEjbInjectionServices(ServiceRegistry serviceRegistry, EEModuleDescription moduleDescription, final EEApplicationDescription applicationDescription, final VirtualFile deploymentRoot) {
        super(serviceRegistry, moduleDescription);
        if (serviceRegistry == null) {
            throw WeldMessages.MESSAGES.parameterCannotBeNull("serviceRegistry");
        }
        if (moduleDescription == null) {
            throw WeldMessages.MESSAGES.parameterCannotBeNull("moduleDescription");
        }
        if (applicationDescription == null) {
            throw WeldMessages.MESSAGES.parameterCannotBeNull("applicationDescription");
        }
        if (deploymentRoot == null) {
            throw WeldMessages.MESSAGES.parameterCannotBeNull("deploymentRoot");
        }

        this.applicationDescription = applicationDescription;
        this.deploymentRoot = deploymentRoot;
    }

    @Override
    public Object resolveEjb(InjectionPoint injectionPoint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResourceReferenceFactory<Object> registerEjbInjectionPoint(final InjectionPoint injectionPoint) {
        EJB ejb = getResourceAnnotated(injectionPoint).getAnnotation(EJB.class);
        if (ejb == null) {
            throw WeldMessages.MESSAGES.annotationNotFound(EJB.class, injectionPoint.getMember());
        }
        if (injectionPoint.getMember() instanceof Method && ((Method) injectionPoint.getMember()).getParameterTypes().length != 1) {
            throw WeldMessages.MESSAGES.injectionPointNotAJavabean((Method) injectionPoint.getMember());
        }
        if (!ejb.lookup().equals("")) {
            return handleServiceLookup(ejb.lookup(), injectionPoint);
        } else {
            final ViewDescription viewDescription = getViewDescription(ejb, injectionPoint);
            if(viewDescription != null) {
                return handleServiceLookup(viewDescription, injectionPoint);
            } else {

                final String proposedName = ResourceInjectionUtilities.getEjbBindLocation(injectionPoint);
                return new ResourceReferenceFactory<Object>() {
                    @Override
                    public ResourceReference<Object> createResource() {
                        return new SimpleResourceReference<Object>(doLookup(proposedName, null));
                    }
                };
            }
        }
    }

    private ResourceReferenceFactory<Object> handleServiceLookup(ViewDescription viewDescription, InjectionPoint injectionPoint) {
        /*
         * Try to obtain ComponentView eagerly and validate the resource type
         */
        final ComponentView view = getComponentView(viewDescription);
        if (view != null && injectionPoint.getAnnotated().isAnnotationPresent(Produces.class)) {
            Class<?> clazz = view.getViewClass();

            Class<?> injectionPointRawType = Reflections.getRawType(injectionPoint.getType());
            //we just compare names, as for remote views the actual classes may be loaded from different class loaders
            Class<?> c = clazz;
            boolean found = true;
            while (c != null && c != Object.class) {
                if (injectionPointRawType.getName().equals(c.getName())) {
                    found = true;
                    break;
                }
                c = c.getSuperclass();
            }
            if(!found) {
                throw BeanLogger.LOG.invalidResourceProducerType(injectionPoint.getAnnotated(), clazz.getName());
            }
            return new ComponentViewToResourceReferenceFactoryAdapter<Object>(view);
        } else {
            return createLazyResourceReferenceFactory(viewDescription);
        }
    }

    private ComponentView getComponentView(ViewDescription viewDescription) {
        final ServiceController<?> controller = serviceRegistry.getRequiredService(viewDescription.getServiceName());
        return (ComponentView) controller.getValue();
    }

    private ViewDescription getViewDescription(EJB ejb, InjectionPoint injectionPoint) {
        final Set<ViewDescription> viewService;
        if (ejb.beanName().isEmpty()) {
            if (ejb.beanInterface() != Object.class) {
                viewService = applicationDescription.getComponentsForViewName(ejb.beanInterface().getName(), deploymentRoot);
            } else {
                viewService = applicationDescription.getComponentsForViewName(getType(injectionPoint.getType()).getName(), deploymentRoot);
            }
        } else {
            if (ejb.beanInterface() != Object.class) {
                viewService = applicationDescription.getComponents(ejb.beanName(), ejb.beanInterface().getName(), deploymentRoot);
            } else {
                viewService = applicationDescription.getComponents(ejb.beanName(), getType(injectionPoint.getType()).getName(), deploymentRoot);
            }
        }
        if(injectionPoint.getAnnotated().isAnnotationPresent(Produces.class)) {
            if (viewService.isEmpty()) {
                throw WeldMessages.MESSAGES.ejbNotResolved(ejb, injectionPoint.getMember());
            } else if (viewService.size() > 1) {
                throw WeldMessages.MESSAGES.moreThanOneEjbResolved(ejb, injectionPoint.getMember(), viewService);
            }
        } else {
            if (viewService.isEmpty()) {
                return null;
            } else if (viewService.size() > 1) {
                return null;
            }
        }
        return viewService.iterator().next();
    }

    @Override
    protected BindInfo getBindInfo(String result) {
        return ContextNames.bindInfoFor(moduleDescription.getApplicationName(), moduleDescription.getModuleName(), moduleDescription.getModuleName(), result);
    }

    @Override
    public void cleanup() {

    }

    private static Class<?> getType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getType(((ParameterizedType) type).getRawType());
        } else {
            throw WeldMessages.MESSAGES.couldNotDetermineUnderlyingType(type);
        }
    }

    protected ResourceReferenceFactory<Object> createLazyResourceReferenceFactory(final ViewDescription viewDescription) {
        return new ResourceReferenceFactory<Object>() {
            @Override
            public ResourceReference<Object> createResource() {
                final ManagedReference instance;
                try {
                    final ServiceController<?> controller = serviceRegistry.getRequiredService(viewDescription.getServiceName());
                    final ComponentView view = (ComponentView) controller.getValue();
                    instance = view.createInstance();
                    return new ResourceReference<Object>() {
                        @Override
                        public Object getInstance() {
                            return instance.getInstance();
                        }

                        @Override
                        public void release() {
                            instance.release();
                        }
                    };
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }


    public Object doLookup(String jndiName, String mappedName) {
        String name = ResourceInjectionUtilities.getResourceName(jndiName, mappedName);
        try {
            return new InitialContext().lookup(name);
        } catch (NamingException e) {
            throw WeldMessages.MESSAGES.coundNotFindResource(name, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2099.java