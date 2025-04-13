error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16526.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16526.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16526.java
text:
```scala
i@@f (true) return;//FIXME AV have an option

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

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;

import org.aspectj.weaver.tools.WeavingAdaptor;

/**
 * Adapter between the generic class pre processor interface and the AspectJ weaver
 * Load time weaving consistency relies on Bcel.setRepository
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class Aj implements ClassPreProcessor {

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
     * @return
     */
    public byte[] preProcess(String className, byte[] bytes, ClassLoader loader) {
        //TODO AV needs to doc that
        if (loader == null || className == null) {
            // skip boot loader or null classes (hibernate)
            return bytes;
        }

        try {
            byte[] weaved = WeaverContainer.getWeaver(loader).weaveClass(className, bytes);
            //FIXME AV make dump optionnal and configurable
            __dump(className, weaved);
            return weaved;
        } catch (Throwable t) {
            //FIXME AV wondering if we should have the option to fail (throw runtime exception) here
            // would make sense at least in test f.e. see TestHelper.handleMessage()
            t.printStackTrace();
            return bytes;
        }
    }

    /**
     * Cache of weaver
     * There is one weaver per classloader
     */
    static class WeaverContainer {

        private static Map weavingAdaptors = new WeakHashMap();

        static WeavingAdaptor getWeaver(ClassLoader loader) {
            synchronized (weavingAdaptors) {
                WeavingAdaptor weavingAdaptor = (WeavingAdaptor) weavingAdaptors.get(loader);
                if (weavingAdaptor == null) {
                    weavingAdaptor = new ClassLoaderWeavingAdaptor(loader);
                    weavingAdaptors.put(loader, weavingAdaptor);
                }
                return weavingAdaptor;
            }
        }
    }

    static void defineClass(ClassLoader loader, String name, byte[] bytes) {
        try {
            //TODO av protection domain, and optimize
            Method defineClass = ClassLoader.class.getDeclaredMethod(
                    "defineClass", new Class[]{
                        String.class, bytes.getClass(), int.class, int.class
                    }
            );
            defineClass.setAccessible(true);
            defineClass.invoke(
                    loader, new Object[]{
                        name,
                        bytes,
                        new Integer(0),
                        new Integer(bytes.length)
                    }
            );
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof LinkageError) {
                ;//is already defined (happens for X$ajcMightHaveAspect interfaces since aspects are reweaved)
            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dump the given bytcode in _dump/...
     *
     * @param name
     * @param b
     * @throws Throwable
     */
    static void __dump(String name, byte[] b) throws Throwable {
        //if (true) return;//FIXME AV have an option
        String className = name.replace('.', '/');
        final File dir;
        if (className.indexOf('/') > 0) {
            dir = new File("_dump" + File.separator + className.substring(0, className.lastIndexOf('/')));
        } else {
            dir = new File("_dump");
        }
        dir.mkdirs();
        String fileName = "_dump" + File.separator + className + ".class";
        FileOutputStream os = new FileOutputStream(fileName);
        os.write(b);
        os.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16526.java