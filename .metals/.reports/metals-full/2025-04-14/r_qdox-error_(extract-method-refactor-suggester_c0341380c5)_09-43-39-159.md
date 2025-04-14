error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/839.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/839.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/839.java
text:
```scala
@version $@@Revision: 1.2 $

/*
* JBoss, the OpenSource EJB server
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package org.jboss.ejb;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Element;

import org.jboss.logging.Logger;
import org.jboss.metadata.MetaData;

/** A utility class that manages the handling of the container-interceptors
child element of the container-configuration.

@author Scott_Stark@displayscape.com
@version $Revision: 1.1 $
*/
public class ContainerInterceptors
{
    public static final int BMT = 1;
    public static final int CMT = 2;
    public static final int ANY = 3;
    static final String BMT_VALUE = "Bean";
    static final String CMT_VALUE = "Container";
    static final String ANY_VALUE = "Both";

    /** Given a container-interceptors element of a container-configuration,
    add the indicated interceptors to the container depending on the container
    transcation type and metricsEnabled flag.

    @param container, the container instance to setup.
    @param transType, one of the BMT, CMT or ANY constants.
    @param metricsEnabled, the ContainerFactoryMBean.metricsEnabled flag
    @param element, the container-interceptors element from the container-configuration.
    */
    public static void addInterceptors(Container container, int transType, boolean metricsEnabled, Element element)
    {
        // Get the interceptor stack(either jboss.xml or standardjboss.xml)
        Iterator interceptorElements = MetaData.getChildrenByTagName(element, "interceptor");
        String transaction = stringTransactionValue(transType);
        ClassLoader loader = container.getClassLoader();
        // First build the container interceptor stack from interceptorElements
        ArrayList istack = new ArrayList();
        while( interceptorElements != null && interceptorElements.hasNext() )
        {
            Element ielement = (Element) interceptorElements.next();
            String transAttr = ielement.getAttribute("transaction");
            if( transAttr.length() == 0 || transAttr.equalsIgnoreCase(transaction) )
            {   // The transaction type matches the container bean trans type, check the metricsEnabled
                String metricsAttr = ielement.getAttribute("metricsEnabled");
                boolean metricsInterceptor = metricsAttr.equalsIgnoreCase("true");
                if( metricsEnabled == false && metricsInterceptor == true )
                    continue;

                String className = null;
                try
                {
                    className = MetaData.getElementContent(ielement);
                    Class clazz = loader.loadClass(className);
                    Interceptor interceptor = (Interceptor) clazz.newInstance();
                    istack.add(interceptor);
                }
                catch(Exception e)
                {
                     Logger.warning("Could not load the "+className+" interceptor for this container");
                     Logger.exception(e);
                }
            }
        }

        if( istack.size() == 0 )
            Logger.warning("There are no interceptors configured. Check the standardjboss.xml file");

        // Now add the interceptors to the container
        for(int i = 0; i < istack.size(); i ++)
        {
            Interceptor interceptor = (Interceptor) istack.get(i);
            container.addInterceptor(interceptor);
        }
        // Finally we add the last interceptor from the container
        container.addInterceptor(container.createContainerInterceptor());
    }

    static String stringTransactionValue(int transType)
    {
        String transaction = ANY_VALUE;
        switch( transType )
        {
            case BMT:
                transaction = BMT_VALUE;
            break;
            case CMT:
                transaction = CMT_VALUE;
            break;
        }
        return transaction;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/839.java