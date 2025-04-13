error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2865.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2865.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2865.java
text:
```scala
a@@gentContext.unregisterMBean(AbstractCompendiumHandler.this);

/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.aries.jmx;

import java.util.concurrent.ExecutorService;

import javax.management.StandardMBean;

import org.apache.aries.jmx.agent.JMXAgentContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * <p>
 * Abstract implementation of {@link MBeanHandler} that provides a template with basic tracking of an optional
 * compendium service. MBeanHandler implementations that manage a {@link StandardMBean} that is backed by a single OSGi
 * compendium service should extend this class and implement the {@linkplain #constructInjectMBean(Object)} and
 * {@linkplain #getName()} methods
 * </p>
 * 
 * @see MBeanHandler
 * 
 * @version $Rev$ $Date$
 */
public abstract class AbstractCompendiumHandler extends ServiceTracker implements MBeanHandler {

    protected final JMXAgentContext agentContext;
    protected StandardMBean mbean;
    protected Long trackedId;
    
    /**
     * 
     * @param agentContext
     * @param filter
     */
    protected AbstractCompendiumHandler(JMXAgentContext agentContext, Filter filter) {
        super(agentContext.getBundleContext(), filter, null);
        this.agentContext = agentContext;
    }

    /**
     * 
     * @param agentContext
     * @param clazz
     */
    protected AbstractCompendiumHandler(JMXAgentContext agentContext, String clazz) {
        super(agentContext.getBundleContext(), clazz, null);
        this.agentContext = agentContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
     */
    public Object addingService(ServiceReference reference) {
        Logger logger = agentContext.getLogger();
        Object trackedService = null;
        Long serviceId = (Long) reference.getProperty(Constants.SERVICE_ID);
        //API stipulates versions for compendium services with static ObjectName
        //This shouldn't happen but added as a consistency check
        if (getTrackingCount() > 0) {
            String serviceDescription = (String) ((reference.getProperty(Constants.SERVICE_DESCRIPTION) != null) ? 
                    reference.getProperty(Constants.SERVICE_DESCRIPTION) : reference.getProperty(Constants.OBJECTCLASS));
            logger.log(LogService.LOG_WARNING, "Detected secondary ServiceReference for [" + serviceDescription
                    + "] with " + Constants.SERVICE_ID + " [" + serviceId + "] Only 1 instance will be JMX managed");
        } else {
            logger.log(LogService.LOG_INFO, "Registering MBean with ObjectName [" + getName() + "] for service with "
                    + Constants.SERVICE_ID + " [" + serviceId + "]");
            trackedService = context.getService(reference);
            mbean = constructInjectMBean(trackedService);
            ExecutorService executor = agentContext.getRegistrationExecutor();
            executor.submit(new Runnable() {
                public void run() {
                    agentContext.registerMBean(AbstractCompendiumHandler.this);
                }
            });
            trackedId = serviceId;
        }
        return trackedService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
     */
    public void removedService(ServiceReference reference, Object service) {
        Logger logger = agentContext.getLogger();
        Long serviceID = (Long) reference.getProperty(Constants.SERVICE_ID);
        if (trackedId != null && !trackedId.equals(serviceID)) {
            String serviceDescription = (String) ((reference.getProperty(Constants.SERVICE_DESCRIPTION) != null) ? 
                    reference.getProperty(Constants.SERVICE_DESCRIPTION) : reference.getProperty(Constants.OBJECTCLASS));
            logger.log(LogService.LOG_WARNING, "ServiceReference for [" + serviceDescription + "] with "
                    + Constants.SERVICE_ID + " [" + serviceID + "] is not currently JMX managed");
        } else {
            logger.log(LogService.LOG_INFO, "Unregistering MBean with ObjectName [" + getName() + "] for service with "
                    + Constants.SERVICE_ID + " [" + serviceID + "]"); 
            ExecutorService executor = agentContext.getRegistrationExecutor();
            executor.submit(new Runnable() {
                public void run() {
                    agentContext.unregisterMBean(getName());
                }
            });
            trackedId = null;
            context.ungetService(reference);
        }
    }

    /**
     * Gets the <code>StandardMBean</code> managed by this handler when the backing service is available or null
     * 
     * @see org.apache.aries.jmx.MBeanHandler#getMbean()
     */
    public StandardMBean getMbean() {
        return mbean;
    }

    /**
     * Implement this method to construct an appropriate {@link StandardMBean} instance which is backed by the supplied
     * service tracked by this handler
     * 
     * @param targetService
     *            the compendium service tracked by this handler
     * @return The <code>StandardMBean</code> instance whose registration lifecycle will be managed by this handler
     */
    protected abstract StandardMBean constructInjectMBean(Object targetService);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2865.java