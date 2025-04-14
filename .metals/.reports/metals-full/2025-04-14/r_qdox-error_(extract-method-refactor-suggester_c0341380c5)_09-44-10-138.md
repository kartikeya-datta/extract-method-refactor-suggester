error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4288.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4288.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4288.java
text:
```scala
M@@ap attachments = invocation.aspectAttachments;

/***************************************
 *                                     *
 *  JBoss: The OpenSource J2EE WebOS   *
 *                                     *
 *  Distributable under LGPL license.  *
 *  See terms of license at gnu.org.   *
 *                                     *
 ***************************************/
package org.jboss.aspect.interceptors;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.jboss.aspect.AspectInitizationException;
import org.jboss.aspect.spi.AspectInterceptor;
import org.jboss.aspect.spi.AspectInvocation;
import org.jboss.util.Classes;

/**
 * The AdaptorInterceptor allows you proivde add an adaptor
 * via the Adaptor interface.  
 * 
 * The problem with the Delegating interceptor is that as you add more
 * interfaces to an object you run a higher chance of having method name 
 * clashes.
 * 
 * 
 * This interceptor uses the following configuration attributes:
 * <ul>
 * <li>adaptor  - The interface that the implementation object is exposing 
 *                via the Adaptable interface.  This is a required attribute. 
 * <li>implementation  - class name of the object that will be used to delegate
 *                method calls to.  This is a required attribute.
 * <li>singleton - if set to "true", then the method calls of multiple
 *                aspect object will be directed to a single instance of
 *                the delegate.  This makes the adaptor a singleton. 
 * </ul>
 * 
 * @author <a href="mailto:hchirino@jboss.org">Hiram Chirino</a>
 * 
 */
public class AdaptorInterceptor implements AspectInterceptor, Serializable
{

    public static final Namespace NAMESPACE = Namespace.get(AdaptorInterceptor.class.getName());
    public static final QName ATTR_ADAPTOR = new QName("adaptor", NAMESPACE);
    public static final QName ATTR_IMPLEMENTATION = new QName("implementation", NAMESPACE);
    public static final QName ATTR_SIGLETON = new QName("singleton", NAMESPACE);

    public Object singeltonObject;
    public Class implementingClass;
    public Class adaptorClass;

    static final Method GET_ADAPTER_METHOD;
    static {
        Method m = null;
        try
        {
            m = Adaptor.class.getMethod("getAdapter", new Class[] { Class.class });
        }
        catch (NoSuchMethodException e)
        {
        }
        GET_ADAPTER_METHOD = m;
    }

    /**
     * @see org.jboss.aspect.spi.AspectInterceptor#invoke(AspectInvocation)
     */
    public Object invoke(AspectInvocation invocation) throws Throwable
    {

        if (!adaptorClass.equals(invocation.args[0]))
        {
            if (invocation.isNextIntrestedInMethodCall())
                return invocation.invokeNext();
            return null;
        }

        Object o = null;
        if (singeltonObject != null)
        {
            o = singeltonObject;
        }
        else
        {
            Map attachments = invocation.attachments;
            o = attachments.get(this);
            if (o == null)
            {
                o = implementingClass.newInstance();
                attachments.put(this, o);
            }
        }
        return o;
    }

    /**
     * Builds a Config object for the interceptor.
     * 
     * @see org.jboss.aspect.spi.AspectInterceptor#init(Element)
     */
    public void init(Element xml) throws AspectInitizationException
    {
        try
        {
            String adaptorName = xml.attribute(ATTR_ADAPTOR).getValue();
            String className = xml.attribute(ATTR_IMPLEMENTATION).getValue();

            adaptorClass = Classes.loadClass(adaptorName);
            implementingClass = Classes.loadClass(className);

            String singlton = xml.attribute(ATTR_SIGLETON) == null ? null : xml.attribute(ATTR_SIGLETON).getValue();
            if ("true".equals(singlton))
                singeltonObject = implementingClass.newInstance();

        }
        catch (Exception e)
        {
            throw new AspectInitizationException("Aspect Interceptor missconfigured: ", e);
        }
    }

    /**
     * @see org.jboss.aspect.spi.AspectInterceptor#getInterfaces()
     */
    public Class[] getInterfaces()
    {
        return new Class[] { Adaptor.class };
    }

    public boolean isIntrestedInMethodCall(Method method)
    {
        return GET_ADAPTER_METHOD.equals(method);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4288.java