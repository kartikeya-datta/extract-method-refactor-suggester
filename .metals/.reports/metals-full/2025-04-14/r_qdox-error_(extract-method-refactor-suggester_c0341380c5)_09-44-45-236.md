error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3226.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3226.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3226.java
text:
```scala
public C@@lass loadClass(String className, boolean resolve, ScopedURLClassLoader source)

/*
 * JBoss, the OpenSource EJB server
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */

package org.jboss.deployment.scope;

import org.jboss.logging.Log;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;

import java.net.URL;

/**
 * Scope is a manager/mediator that connects several ScopedURLClassLoaders
 * with each other and computes their dependencies. The locks used in the scope
 * implementation are quite coarse-grained, maybe thread-unfriendly, but the
 * rationale is that classloading a) happens not too often (hopefully) in the
 * lifecycle of an application b) will dispatch only in special cases (where applications depliberately
 * share classes) to this scope class and c) is optimized by caching the locations.
 * @author  cgjung
 * @version 0.8
 */

public class Scope {
    
    /** keeps a map of class loaders that participate in this scope */
    final protected Map classLoaders=new java.util.HashMap();
    
    /** keeps a hashtable of dependencies between the classLoaders */
    final protected Map dependencies=new java.util.HashMap();
    
    /** keeps a hashtable of class appearances */
    final protected Map classLocations=new java.util.HashMap();
    
    /** keeps a hashtable of resource appearances */
    final protected Map resourceLocations=new java.util.HashMap();
    
    /** keeps a reference to a logger which which to interact */
    final protected Log log;
    
    /** Creates new Scope */
    public Scope(Log log) {
        this.log=log;
    }
    
    /** registers a classloader in this scope */
    public ScopedURLClassLoader registerClassLoader(ScopedURLClassLoader loader) {
        // must synchronize not to collide with deregistrations and
        // dependency logging
        synchronized(classLoaders) {
            return (ScopedURLClassLoader) classLoaders.put(loader.deployment.getLocalUrl(),loader);
        }
    }
    
    
    /** deRegisters a classloader in this scope
     *  removes all cached data related to this classloader
     */
    public ScopedURLClassLoader deRegisterClassLoader(ScopedURLClassLoader loader) {
        // synchronized not to collide with registrations
        // and dependency logging
        synchronized(classLoaders) {
            // remove class locations
            clearByValue(classLocations,loader);
            // remove resource locations
            clearByValue(resourceLocations,loader);
            // remove dependency annotations
            dependencies.remove(loader);
            // and remove the loader
            return (ScopedURLClassLoader) classLoaders.remove(loader.deployment.getLocalUrl());
        }
    }
    
    /** helper method that will clear all entries from a map
     *  with a dedicated target value.
     */
    protected void clearByValue(Map map, Object value) {
        Iterator values=map.values().iterator();
        while(values.hasNext()) {
            if(values.next().equals(value))
                // uses the very useful remove method of the value iterator!
                values.remove();
        }
    }
    
    /** returns the classLoaders that a particular classLoader is
     *  dependent on. Should be called after locking classLoaders
     */
    public Set getDependentClassLoaders(ScopedURLClassLoader loader) {
        Set result=(Set) dependencies.get(loader);
        if(result==null)
            result=new java.util.HashSet();
        return result;
    }
    
    /** adds a dependency between two classloaders. this can be called
     *  from within application threads that require resource loading.
     *  Should be called after locking classLoaders
     */
    protected boolean addDependency(ScopedURLClassLoader source, ScopedURLClassLoader target) {
        // no rescursions necessary (but not volatile for the code)
        if(source !=null && target!=null && !source.equals(target)) {
            
            Set deps=(Set) dependencies.get(target);
            
            if(deps==null) {
                deps=new java.util.HashSet();
                dependencies.put(target,deps);
            }
            
            log.debug("Adding dependency from deployment "+source+":"+source.deployment.getLocalUrl()+" to deployment "+
                target+":"+target.deployment.getLocalUrl());
            
            return deps.add(source);
        } else
            return false;
    }
    
    /** loads a class on behalf of a given classloader */
    public Class loadClass(String className, ScopedURLClassLoader source, boolean resolve)
    throws ClassNotFoundException {
        
        // short look into the class location cache, is synchronized in
        // case that the relevant target is simultaneously teared down
        synchronized(classLoaders) {
            ScopedURLClassLoader target= (ScopedURLClassLoader)
            classLocations.get(className);
            
            // its there, so log and load it
            if(target!=null) {
                addDependency(source,target);
                // we can be sure that the target loader
                // has the class already in its own cache
                // so this call should not cost much
                return target.loadClass(className,resolve);
            }
            
            // otherwise we do a big lookup
            Iterator allLoaders=classLoaders.values().iterator();
        
            while(allLoaders.hasNext()) {
                target=(ScopedURLClassLoader) allLoaders.next();
            
            // no recursion, please
            if(!target.equals(source)) {
                try{
                    Class foundClass=target.loadClassProperly(className,resolve);
                    classLocations.put(className,target);
                    addDependency(source,target);
                    return foundClass;
                } catch(ClassNotFoundException e) {
                    // proceed with the next loaders in scope
                }
            }
        } // while
        
        // no loader in the scope has been able to load the class
        throw new ClassNotFoundException("could not resolve class "+
        className+" in scope.");
        
        } // sync
    }
    
    /** gets a URL on behalf of a given classloader which may be null
     *  in case that we do not check dependencies */
    public URL getResource(String name, ScopedURLClassLoader source) {
        
        // short look into the resource location cache, is synchronized in
        // case that the relevant target is simultaneously teared down
        synchronized(classLoaders) {
            ScopedURLClassLoader target= (ScopedURLClassLoader)
                resourceLocations.get(name);
            
            // its there, so log and load it
            if(target!=null)
                addDependency(source,target);
        
           // the lock is released here, so that other threads could run too
            if(target!=null)
                return target.getResource(name);
        
            // otherwise we do a big lookup
            Iterator allLoaders=classLoaders.values().iterator();
        
            while(allLoaders.hasNext()) {
                target=(ScopedURLClassLoader) allLoaders.next();
            
            // no recursion, please
            if(!target.equals(source)) {
                    URL foundResource=target.getResourceProperly(name);
                    if(foundResource!=null) {
                        resourceLocations.put(name,target);
                        addDependency(source,target);
                        return foundResource;
                    }
            }
        } // while
        
        // no loader in the scope has been able to load the resource
        return null;
        
        } // sync
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3226.java