error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1870.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1870.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1870.java
text:
```scala
final M@@odule module = Module.getCallerModuleLoader().loadModule(modularReference.getModuleIdentifier());

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

package org.jboss.as.naming.context;

import org.jboss.as.naming.ServiceAwareObjectFactory;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceRegistry;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

import static org.jboss.as.naming.util.NamingUtils.namingException;

/**
 * ObjectFactoryBuilder implementation used to support custom object factories being loaded from modules. This class
 * also provides the default object factory implementation.
 *
 * @author John Bailey
 */
public class ObjectFactoryBuilder implements javax.naming.spi.ObjectFactoryBuilder, ObjectFactory {

    public static final ObjectFactoryBuilder INSTANCE = new ObjectFactoryBuilder();

    private volatile ServiceRegistry serviceRegistry;

    private ObjectFactoryBuilder() {
    }

    /**
     * Create an object factory.  If the object parameter is a reference it will attempt to create an {@link javax.naming.spi.ObjectFactory}
     * from the reference.  If the parameter is not a reference, or the reference does not create an {@link javax.naming.spi.ObjectFactory}
     * it will return {@code this} as the {@link javax.naming.spi.ObjectFactory} to use.
     *
     * @param obj The object bound in the naming context
     * @param environment The environment information
     * @return The object factory the object resolves to
     * @throws NamingException If any problems occur
     */
    public ObjectFactory createObjectFactory(final Object obj, Hashtable<?, ?> environment) throws NamingException {
        try {
            if (obj instanceof Reference) {
                return factoryFromReference((Reference) obj, environment);
            }
        } catch(Throwable ignored) {
        }
        return this;
    }

    /**
     * Create an object instance.
     *
     * @param ref Object containing reference information
     * @param name The name relative to nameCtx
     * @param nameCtx The naming context
     * @param environment The environment information
     * @return The object
     * @throws Exception If any error occur
     */
    public Object getObjectInstance(final Object ref, final Name name, final Context nameCtx, final Hashtable<?, ?> environment) throws Exception {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        if(classLoader == null) {
            return ref;
        }
        final String factoriesProp = (String)environment.get(Context.OBJECT_FACTORIES);
        if(factoriesProp != null) {
            final String[] classes = factoriesProp.split(":");
            for(String className : classes) {
                try {
                    final Class<?> factoryClass = classLoader.loadClass(className);
                    final ObjectFactory objectFactory = ObjectFactory.class.cast(factoryClass.newInstance());
                    final Object result = objectFactory.getObjectInstance(ref, name, nameCtx, environment);
                    if(result != null) {
                        return result;
                    }
                } catch(Throwable ignored) {
                }
            }
        }
        return null;
    }

    /**
     * Sets the service registry to use for service references
     *
     */
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    private ObjectFactory factoryFromReference(final Reference reference, final Hashtable<?, ?> environment) throws Exception {
        if (reference instanceof ModularReference) {
            return factoryFromModularReference(ModularReference.class.cast(reference), environment);
        }
        return factoryFromReference(reference, SecurityActions.getContextClassLoader(), environment);
    }

    private ObjectFactory factoryFromModularReference(ModularReference modularReference, final Hashtable<?, ?> environment) throws Exception {
        final Module module = Module.getCurrentModuleLoader().loadModule(modularReference.getModuleIdentifier());
        final ClassLoader classLoader = module.getClassLoader();
        return factoryFromReference(modularReference, classLoader, environment);
    }

    private ObjectFactory factoryFromReference(final Reference reference, final ClassLoader classLoader, final Hashtable<?, ?> environment) throws Exception {
        try {
            final Class<?> factoryClass = classLoader.loadClass(reference.getFactoryClassName());
            ObjectFactory factory = ObjectFactory.class.cast(factoryClass.newInstance());
            if (factory instanceof ServiceAwareObjectFactory) {
                ((ServiceAwareObjectFactory) factory).injectServiceRegistry(serviceRegistry);
            }
            return factory;
        } catch (Throwable t) {
            throw namingException("Failed to create object factory from classloader.", t);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1870.java