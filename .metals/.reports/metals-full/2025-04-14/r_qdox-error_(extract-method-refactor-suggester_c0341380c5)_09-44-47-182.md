error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6851.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6851.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6851.java
text:
```scala
private H@@ashtable sources;

/**
 * This class holds the list of registered DOMImplementations. The contents 
 * of the registry are drawn from the System Property 
 * <code>org.w3c.dom.DOMImplementationSourceList</code>, which must contain a 
 * white-space delimited sequence of the names of classes implementing 
 * <code>DOMImplementationSource</code>.
 * Applications may also register DOMImplementationSource
 * implementations by using a method on this class. They may then
 * query instances of the registry for implementations supporting
 * specific features.</p>
 *
 * <p>This provides an application with an implementation-independent 
 * starting point.
 *
 * @see DOMImplementation
 * @see DOMImplementationSource
 */

package org.w3c.dom;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.ClassLoader;
import java.lang.String;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Hashtable;

import org.w3c.dom.DOMImplementationSource;
import org.w3c.dom.DOMImplementation;

public class DOMImplementationRegistry { 

    // The system property to specify the DOMImplementationSource class names.
    public final static String PROPERTY =
        "org.w3c.dom.DOMImplementationSourceList";

    private Hashtable sources = new Hashtable();

    // deny construction by other classes
    private DOMImplementationRegistry() {
    }

    // deny construction by other classes
    private DOMImplementationRegistry(Hashtable srcs) {
        sources = srcs;
    }


    /* 
     * This method queries the System property
     * <code>org.w3c.dom.DOMImplementationSourceList</code>. If it is
     * able to read and parse the property, it attempts to instantiate
     * classes according to each space-delimited substring. Any
     * exceptions it encounters are thrown to the application. An application
     * must call this method before using the class.
     * @return  an initialized instance of DOMImplementationRegistry
     */ 
    public static DOMImplementationRegistry newInstance() 		
            throws ClassNotFoundException, InstantiationException, 
            IllegalAccessException
    {
        Hashtable sources = new Hashtable();    

        // fetch system property:
        String p = System.getProperty(PROPERTY);
        if (p != null) {
            StringTokenizer st = new StringTokenizer(p);
            while (st.hasMoreTokens()) {
                String sourceName = st.nextToken();
                // Use context class loader, falling back to Class.forName
                // if and only if this fails...
                Object source = getClass(sourceName).newInstance();
                sources.put(sourceName, source);
            }
        }
        return new DOMImplementationRegistry(sources);
    }


    /**
     * Return the first registered implementation that has the desired
     * features, or null if none is found.
     *
     * @param features A string that specifies which features are required.
     *                 This is a space separated list in which each feature is
     *                 specified by its name optionally followed by a space
     *                 and a version number.
     *                 This is something like: "XML 1.0 Traversal Events 2.0"
     * @return An implementation that has the desired features, or
     *   <code>null</code> if this source has none.
     */
    public DOMImplementation getDOMImplementation(String features)
            throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, ClassCastException
    {
        Enumeration names = sources.keys();
        String name = null;
        while(names.hasMoreElements()) {
            name = (String)names.nextElement();
            DOMImplementationSource source =
                (DOMImplementationSource) sources.get(name);

            DOMImplementation impl = source.getDOMImplementation(features);
            if (impl != null) {
                return impl;
            }
        }
        return null;
    }

    /**
     * Register an implementation.
     */
    public void addSource(DOMImplementationSource s)
            throws ClassNotFoundException,
            InstantiationException, IllegalAccessException
    {
        String sourceName = s.getClass().getName(); 
        sources.put(sourceName, s);
    }

    private static Class getClass (String className)
                throws ClassNotFoundException, IllegalAccessException,
                InstantiationException {
        Method m = null;
        ClassLoader cl = null;

        try {
            m = Thread.class.getMethod("getContextClassLoader", null);
        } catch (NoSuchMethodException e) {
            // Assume that we are running JDK 1.1, use the current ClassLoader
            cl = DOMImplementationRegistry.class.getClassLoader();
        }

        if (cl == null ) {
            try {
                cl = (ClassLoader) m.invoke(Thread.currentThread(), null);
            } catch (IllegalAccessException e) {
                // assert(false)
                throw new UnknownError(e.getMessage());
            } catch (InvocationTargetException e) {
                // assert(e.getTargetException() instanceof SecurityException)
                throw new UnknownError(e.getMessage());
            }
        }
        if (cl == null) { 
            // fall back to Class.forName
            return Class.forName(className);
        }
        try { 
            return cl.loadClass(className);
        } catch (ClassNotFoundException e) {
            return Class.forName(className);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6851.java