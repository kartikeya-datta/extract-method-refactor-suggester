error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8909.java
text:
```scala
R@@eferenceTypeDelegate bootstrapLoaderDelegate = resolveReflectionTypeDelegate(ty, loader);

/* *******************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Ron Bodkin		Initial implementation
 * ******************************************************************/
package org.aspectj.weaver.ltw;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.bridge.IMessageHandler;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.ICrossReferenceHandler;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.reflect.AnnotationFinder;
import org.aspectj.weaver.reflect.IReflectionWorld;
import org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegateFactory;
import org.aspectj.weaver.reflect.ReflectionWorld;

/**
 * @author adrian
 * @author Ron Bodkin
 * 
 * For use in LT weaving
 * 
 * Backed by both a BcelWorld and a ReflectionWorld
 * 
 * Needs a callback when a woven class is defined
 * This is the trigger for us to ditch the class from
 * Bcel and cache it in the reflective world instead.
 *
 * Create by passing in a classloader, message handler
 */
public class LTWWorld extends BcelWorld implements IReflectionWorld {
	
    private AnnotationFinder annotationFinder;
    private ClassLoader loader; // weavingContext?
    
    protected final static Class concurrentMapClass = makeConcurrentMapClass();
    protected static Map/*<String, WeakReference<ReflectionBasedReferenceTypeDelegate>>*/ bootstrapTypes = makeConcurrentMap();
    
    /**
     * Build a World from a ClassLoader, for LTW support
     */
    public LTWWorld(ClassLoader loader, IMessageHandler handler, ICrossReferenceHandler xrefHandler) {
        super(loader, handler, xrefHandler);
        this.loader = loader;
        
        setBehaveInJava5Way(LangUtil.is15VMOrGreater());
        annotationFinder = ReflectionWorld.makeAnnotationFinderIfAny(loader, this);
    }
    
	public ClassLoader getClassLoader() {
        return this.loader;
    }
    
    //TEST
    //this is probably easier: just mark anything loaded while loading aspects as not
    //expendible... it also fixes a possible bug whereby non-rewoven aspects are deemed expendible
    //<exclude within="org.foo.aspects..*"/>
//    protected boolean isExpendable(ResolvedType type) {
//		return ((type != null) && !loadingAspects && !type.isAspect() && (!type
//				.isPrimitiveType()));
//	}
	
    /**
	 * @Override
	 */
    protected ReferenceTypeDelegate resolveDelegate(ReferenceType ty) {  	
        String name = ty.getName();

        // use reflection delegates for all bootstrap types
        ReferenceTypeDelegate bootstrapLoaderDelegate = resolveIfBootstrapDelegate(ty);
        if (bootstrapLoaderDelegate != null) {
            return bootstrapLoaderDelegate;
        }
        
       	return super.resolveDelegate(ty);
    }

    protected ReferenceTypeDelegate resolveIfBootstrapDelegate(ReferenceType ty) {
        // first check for anything available in the bootstrap loader: these types are just defined from that without allowing nondelegation
    	String name = ty.getName();
        Reference bootRef = (Reference)bootstrapTypes.get(name);
        if (bootRef != null) {        	
        	ReferenceTypeDelegate rtd = (ReferenceTypeDelegate)bootRef.get();
        	if (rtd != null) {
        		return rtd;
        	}
        }

        char fc = name.charAt(0);
        if (fc=='j' || fc=='c' || fc=='o' || fc=='s') { // cheaper than imminent string startsWith tests
	        if (name.startsWith("java") || name.startsWith("com.sun.") || name.startsWith("org.w3c") || 
	        	name.startsWith("sun.") || name.startsWith("org.omg")) {
		        ReferenceTypeDelegate bootstrapLoaderDelegate = resolveReflectionTypeDelegate(ty, null);
		        if (bootstrapLoaderDelegate != null) {
		            // it's always fine to load these bytes: there's no weaving into them
		            // and since the class isn't initialized, all we are doing at this point is loading the bytes
		            //processedRefTypes.put(ty, this); // has no effect - and probably too aggressive if we did store these in the type map            
		        	// should we share these, like we do the BCEL delegates?
		        	bootstrapTypes.put(ty.getName(), new WeakReference(bootstrapLoaderDelegate));
		        }    	
		        return bootstrapLoaderDelegate;
	        }
        }
        return null;
    }
    
    /**
     * Helper method to resolve the delegate from the reflection delegate factory.
     */
    private ReferenceTypeDelegate resolveReflectionTypeDelegate(ReferenceType ty, ClassLoader resolutionLoader) {
        ReferenceTypeDelegate res = ReflectionBasedReferenceTypeDelegateFactory.createDelegate(ty, this, resolutionLoader);
        return res;
    }

    /**
     * Remove this class from the typeMap. Call back to be made from a publishing class loader
     * The class loader should, ideally, make this call on each
     * not yet working
     * 
     * @param clazz
     */
    public void loadedClass(Class clazz) {
    }
    
    private static final long serialVersionUID = 1;

    public AnnotationFinder getAnnotationFinder() {
        return this.annotationFinder;
    }

    /* (non-Javadoc)
     * @see org.aspectj.weaver.reflect.IReflectionWorld#resolve(java.lang.Class)
     */
    public ResolvedType resolve(Class aClass) {
        return ReflectionWorld.resolve(this, aClass); 
    }

    private static Map makeConcurrentMap() {
    	if (concurrentMapClass != null) {
    		try {
    			return (Map)concurrentMapClass.newInstance();
    		} catch (InstantiationException _) {
    		} catch (IllegalAccessException _) {    			
    		}
    		// fall through if exceptions
    	}
    	return Collections.synchronizedMap(new HashMap());
    }
    
    private static Class makeConcurrentMapClass() {
    	String betterChoices[] = { 
    			"java.util.concurrent.ConcurrentHashMap", 
    			"edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap", 
    			"EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap"
    	};
		for (int i = 0; i < betterChoices.length; i++) {
			try {
				return Class.forName(betterChoices[i]);
			} catch (ClassNotFoundException _) {
				// try the next one
			} catch (SecurityException _) {
				// you get one of these if you dare to try to load an undefined class in a 
				// package starting with java like java.util.concurrent
			}
		}
    	return null;
    }
    public boolean isRunMinimalMemory() {
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8909.java