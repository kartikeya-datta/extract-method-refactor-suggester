error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1.java
text:
```scala
t@@hrow new NoSuchMethodException(aspectClass.getName() + ".aspectOf(..) is not accessible public static");

/*******************************************************************************
 * Copyright (c) 2005 Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 * initial implementation              Jonas Bonér, Alexandre Vasseur
 *******************************************************************************/
package org.aspectj.lang;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Handles generic aspectOf method when those are not available in the aspects but added later on
 * thru load time weaving.
 * <p/>
 * Aspects.aspectOf(..) is doing reflective calls to the aspect aspectOf, so for better performance
 * consider using preparation of the aspects.
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class Aspects {

    private final static Class[] EMPTY_CLASS_ARRAY = new Class[0];
    private final static Class[] PEROBJECT_CLASS_ARRAY = new Class[]{Object.class};
    private final static Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    private final static String ASPECTOF = "aspectOf";

    /**
     * Returns the singleton aspect
     *
     * @param aspectClass
     * @return
     * @throws NoAspectBoundException if no such aspect
     */
    public static Object aspectOf(Class aspectClass) throws NoAspectBoundException {
        try {
            return getSingletonAspectOf(aspectClass).invoke(null, EMPTY_OBJECT_ARRAY);
        } catch (Exception e) {
            throw new NoAspectBoundException(aspectClass.getName(), e);
        }
    }

    /**
     * Returns the perthis / pertarget aspect
     * @param aspectClass
     * @param perObject
     * @return
     * @throws NoAspectBoundException if no such aspect, or no aspect bound
     */
    public static Object aspectOf(Class aspectClass, Object perObject) throws NoAspectBoundException {
        try {
            return getPerObjectAspectOf(aspectClass).invoke(null, new Object[]{perObject});
        } catch (Exception e) {
            throw new NoAspectBoundException(aspectClass.getName(), e);
        }
    }

    public static Object aspectOf(Class aspectClass, Thread perThread) throws NoAspectBoundException {
        //TODO - how to know it s a real per Thread ?
        // if it is actually a singleton one, we will have it as well...
        try {
            return getSingletonAspectOf(aspectClass).invoke(null, EMPTY_OBJECT_ARRAY);
        } catch (Exception e) {
            throw new NoAspectBoundException(aspectClass.getName(), e);
        }
    }

    private static Method getSingletonAspectOf(Class aspectClass) throws NoSuchMethodException {
        Method method = aspectClass.getDeclaredMethod(ASPECTOF, EMPTY_CLASS_ARRAY);
        return checkAspectOf(method, aspectClass);
    }

    private static Method getPerObjectAspectOf(Class aspectClass) throws NoSuchMethodException {
        Method method = aspectClass.getDeclaredMethod(ASPECTOF, PEROBJECT_CLASS_ARRAY);
        return checkAspectOf(method, aspectClass);
    }

    private static Method checkAspectOf(Method method, Class aspectClass) 
        throws NoSuchMethodException {
        method.setAccessible(true);
        if (!method.isAccessible()
 !Modifier.isPublic(method.getModifiers())
 !Modifier.isStatic(method.getModifiers())) {            
            new NoSuchMethodException(aspectClass.getName() + ".aspectOf(..) is not accessible public static");
        }
        return method;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1.java