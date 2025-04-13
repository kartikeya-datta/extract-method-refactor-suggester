error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12329.java
text:
```scala
C@@lassLoaderWeavingAdaptor weavingAdaptor = new ClassLoaderWeavingAdaptor();

/*******************************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution and is available at
 * http://eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alexandre Vasseur         initial implementation
 *******************************************************************************/
package org.aspectj.weaver.loadtime;

import java.util.Map;
import java.util.WeakHashMap;

import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.aspectj.weaver.tools.WeavingAdaptor;

/**
 * Adapter between the generic class pre processor interface and the AspectJ weaver
 * Load time weaving consistency relies on Bcel.setRepository
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class Aj implements ClassPreProcessor {

	private IWeavingContext weavingContext;
	
	private static Trace trace = TraceFactory.getTraceFactory().getTrace(Aj.class);
	
	public Aj(){
		this(null);
	}
	
	
	public Aj(IWeavingContext context){
		if (trace.isTraceEnabled()) trace.enter("<init>",this,new Object[] {context});
		this.weavingContext = context;
		if (trace.isTraceEnabled()) trace.exit("<init>");
	}

    /**
     * Initialization
     */
    public void initialize() {
        ;
    }

    /**
     * Weave
     *
     * @param className
     * @param bytes
     * @param loader
     * @return weaved bytes
     */
    public byte[] preProcess(String className, byte[] bytes, ClassLoader loader) {
		if (trace.isTraceEnabled()) trace.enter("preProcess",this,new Object[] {className,bytes,loader});
    	
        //TODO AV needs to doc that
        if (loader == null || className == null) {
            // skip boot loader or null classes (hibernate)
    		if (trace.isTraceEnabled()) trace.exit("preProcess",bytes);
            return bytes;
        }

        try {
            WeavingAdaptor weavingAdaptor = WeaverContainer.getWeaver(loader, weavingContext);
            if (weavingAdaptor == null) {
        		if (trace.isTraceEnabled()) trace.exit("preProcess",bytes);
            	return bytes;
            }
            return weavingAdaptor.weaveClass(className, bytes);
        } catch (Exception t) {
    		trace.error("preProcess",t);
            //FIXME AV wondering if we should have the option to fail (throw runtime exception) here
            // would make sense at least in test f.e. see TestHelper.handleMessage()
            t.printStackTrace();
    		if (trace.isTraceEnabled()) trace.exit("preProcess",bytes);
            return bytes;
        }
    }

    /**
     * Cache of weaver
     * There is one weaver per classloader
     */
    static class WeaverContainer {

        private static Map weavingAdaptors = new WeakHashMap();

        static WeavingAdaptor getWeaver(ClassLoader loader, IWeavingContext weavingContext) {
            ExplicitlyInitializedClassLoaderWeavingAdaptor adaptor = null;
            synchronized(weavingAdaptors) {
                adaptor = (ExplicitlyInitializedClassLoaderWeavingAdaptor) weavingAdaptors.get(loader);
                if (adaptor == null) {
                	String loaderClassName = loader.getClass().getName(); 
                	if (loaderClassName.equals("sun.reflect.DelegatingClassLoader")) {
                		// we don't weave reflection generated types at all! 
                		return null;
                	} else {
	                    // create it and put it back in the weavingAdaptors map but avoid any kind of instantiation
	                    // within the synchronized block
	                    ClassLoaderWeavingAdaptor weavingAdaptor = new ClassLoaderWeavingAdaptor(loader, weavingContext);
	                    adaptor = new ExplicitlyInitializedClassLoaderWeavingAdaptor(weavingAdaptor);
	                    weavingAdaptors.put(loader, adaptor);
                	}
                }
            }
            // perform the initialization
            return adaptor.getWeavingAdaptor(loader, weavingContext);


            // old version
//            synchronized(loader) {//FIXME AV - temp fix for #99861
//                synchronized (weavingAdaptors) {
//                    WeavingAdaptor weavingAdaptor = (WeavingAdaptor) weavingAdaptors.get(loader);
//                    if (weavingAdaptor == null) {
//                        weavingAdaptor = new ClassLoaderWeavingAdaptor(loader, weavingContext);
//                        weavingAdaptors.put(loader, weavingAdaptor);
//                    }
//                    return weavingAdaptor;
//                }
//            }
        }
    }

    static class ExplicitlyInitializedClassLoaderWeavingAdaptor {
        private final ClassLoaderWeavingAdaptor weavingAdaptor;
        private boolean isInitialized;

        public ExplicitlyInitializedClassLoaderWeavingAdaptor(ClassLoaderWeavingAdaptor weavingAdaptor) {
            this.weavingAdaptor = weavingAdaptor;
            this.isInitialized = false;
        }

        private void initialize(ClassLoader loader, IWeavingContext weavingContext) {
            if (!isInitialized) {
                isInitialized = true;
                weavingAdaptor.initialize(loader, weavingContext);
            }
        }

        public ClassLoaderWeavingAdaptor getWeavingAdaptor(ClassLoader loader, IWeavingContext weavingContext) {
            initialize(loader, weavingContext);
            return weavingAdaptor;
        }
    }

    /**
     * Returns a namespace based on the contest of the aspects available
     */
    public String getNamespace (ClassLoader loader) {
        ClassLoaderWeavingAdaptor weavingAdaptor = (ClassLoaderWeavingAdaptor)WeaverContainer.getWeaver(loader, weavingContext);
    	return weavingAdaptor.getNamespace();
    }
    
    /**
     * Check to see if any classes have been generated for a particular classes loader.
     * Calls ClassLoaderWeavingAdaptor.generatedClassesExist()
     * @param loader the class cloder
     * @return       true if classes have been generated.
     */
    public boolean generatedClassesExist(ClassLoader loader){
    	return ((ClassLoaderWeavingAdaptor)WeaverContainer.getWeaver(loader, weavingContext)).generatedClassesExistFor(null);
    }
    
    public void flushGeneratedClasses(ClassLoader loader){
    	((ClassLoaderWeavingAdaptor)WeaverContainer.getWeaver(loader, weavingContext)).flushGeneratedClasses();
    }

}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12329.java