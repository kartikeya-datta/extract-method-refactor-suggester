error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3444.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3444.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3444.java
text:
```scala
public static final M@@ap<ClassLoader, Set<ClassLoader>> deploymentClassLoaders = new ConcurrentHashMap<ClassLoader, Set<ClassLoader>>();

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
package org.jboss.as.weld.services;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.as.util.security.GetContextClassLoaderAction;
import org.jboss.as.weld.WeldMessages;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.weld.bootstrap.api.Singleton;
import org.jboss.weld.bootstrap.api.SingletonProvider;

import static java.lang.System.getSecurityManager;
import static java.lang.Thread.currentThread;
import static java.security.AccessController.doPrivileged;

/**
 * Singleton Provider that uses the TCCL to figure out the current application.
 *
 * @author Stuart Douglas
 */
public class ModuleGroupSingletonProvider extends SingletonProvider {

    /**
     * Map of the top level class loader to all class loaders in a deployment
     */
    public static Map<ClassLoader, Set<ClassLoader>> deploymentClassLoaders = new ConcurrentHashMap<ClassLoader, Set<ClassLoader>>();

    /**
     * Maps a top level class loader to all CL's in the deployment
     */
    public static void addClassLoaders(ClassLoader topLevel, Set<ClassLoader> allClassLoaders) {
        deploymentClassLoaders.put(topLevel, allClassLoaders);
    }

    /**
     * Removes the class loader mapping
     */
    public static void removeClassLoader(ClassLoader topLevel) {
        deploymentClassLoaders.remove(topLevel);
    }

    @Override
    public <T> Singleton<T> create(Class<? extends T> type) {
        return new TCCLSingleton<T>();
    }

    @SuppressWarnings("unused")
    private static class TCCLSingleton<T> implements Singleton<T> {

        private volatile Map<ClassLoader, T> store = Collections.emptyMap();

        public T get() {
            T instance = store.get(findParentModuleCl(getClassLoader()));
            if (instance == null) {
                throw WeldMessages.MESSAGES.singletonNotSet(getClassLoader());
            }
            return instance;
        }

        public synchronized void set(T object) {
            final Map<ClassLoader, T> store = new IdentityHashMap<ClassLoader, T>(this.store);
            ClassLoader classLoader = getClassLoader();
            store.put(classLoader, object);
            if (deploymentClassLoaders.containsKey(classLoader)) {
                for (ClassLoader cl : deploymentClassLoaders.get(classLoader)) {
                    store.put(cl, object);
                }
            }
            this.store = store;
        }

        public synchronized void clear() {
            ClassLoader classLoader = getClassLoader();
            final Map<ClassLoader, T> store = new IdentityHashMap<ClassLoader, T>(this.store);
            store.remove(classLoader);
            if (deploymentClassLoaders.containsKey(classLoader)) {
                for (ClassLoader cl : deploymentClassLoaders.get(classLoader)) {
                    store.remove(cl);
                }
            }
            this.store = store;
        }

        public boolean isSet() {
            return store.containsKey(findParentModuleCl(getClassLoader()));
        }

        /**
         * If a custom CL is in use we want to get the module CL it delegates to
         * @param classLoader The current CL
         * @returnThe corresponding module CL
         */
        private ClassLoader findParentModuleCl(ClassLoader classLoader) {
            ClassLoader c = classLoader;
            while (c != null && !(c instanceof ModuleClassLoader)) {
                c = c.getParent();
            }
            return c;
        }

        private ClassLoader getClassLoader() {
            return getSecurityManager() == null ? currentThread().getContextClassLoader() : doPrivileged(GetContextClassLoaderAction.getInstance());
        }

        /*
         * Default implementation of the Weld 1.2 API
         *
         * This provides forward binary compatibility with Weld 1.2 (OSGi integration).
         * It is OK to ignore the id parameter as TCCL is still used to identify the singleton in Java EE.
         */
        public T get(String id) {
            return get();
        }

        public boolean isSet(String id) {
            return isSet();
        }

        public void set(String id, T object) {
            set(object);
        }

        public void clear(String id) {
            clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3444.java