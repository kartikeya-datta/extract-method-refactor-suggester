error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2097.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2097.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2097.java
text:
```scala
v@@erify(agentContext).unregisterMBean(target);

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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.management.StandardMBean;

import org.apache.aries.jmx.agent.JMXAgentContext;
import org.junit.After;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;

public class CompendiumHandlerTest {

    protected AbstractCompendiumHandler target;

    @After
    public void tearDown(){
        target = null;
    }


    @Test
    public void testAddingServiceWillInitiateMBeanRegistration() throws Exception {
        Bundle mockSystemBundle = mock(Bundle.class);
        when(mockSystemBundle.getSymbolicName()).thenReturn("the.sytem.bundle");

        Object service = new Object();

        ServiceReference reference = mock(ServiceReference.class);
        BundleContext bundleContext = mock(BundleContext.class);
        when(bundleContext.getProperty(Constants.FRAMEWORK_UUID)).thenReturn("some-uuid");
        when(bundleContext.getService(reference)).thenReturn(service);
        when(bundleContext.getBundle(0)).thenReturn(mockSystemBundle);

        Logger agentLogger = mock(Logger.class);
        JMXAgentContext agentContext = mock(JMXAgentContext.class);
        when(agentContext.getBundleContext()).thenReturn(bundleContext);
        when(agentContext.getLogger()).thenReturn(agentLogger);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        when(agentContext.getRegistrationExecutor()).thenReturn(executor);

        AbstractCompendiumHandler concreteHandler = new CompendiumHandler(agentContext, "org.osgi.service.Xxx");
        target = spy(concreteHandler);

        target.addingService(reference);

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);

        //service only got once
        verify(bundleContext).getService(reference);
        //template method is invoked
        verify(target).constructInjectMBean(service);
        //registration is invoked on context
        verify(agentContext).registerMBean(target);

    }

    @Test
    public void testRemovedServiceWillUnregisterMBean() throws Exception{

        Object service = new Object();
        ServiceReference reference = mock(ServiceReference.class);

        BundleContext bundleContext = mock(BundleContext.class);
        Logger agentLogger = mock(Logger.class);
        JMXAgentContext agentContext = mock(JMXAgentContext.class);
        when(agentContext.getBundleContext()).thenReturn(bundleContext);
        when(agentContext.getLogger()).thenReturn(agentLogger);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        when(agentContext.getRegistrationExecutor()).thenReturn(executor);

        AbstractCompendiumHandler concreteHandler = new CompendiumHandler(agentContext, "org.osgi.service.Xxx");
        target = spy(concreteHandler);

        String name = "osgi.compendium:service=xxx,version=1.0";
        doReturn(name).when(target).getName();

        target.removedService(reference, service);

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);

        //service unget
        verify(bundleContext).ungetService(reference);
        //unregister is invoked on context
        verify(agentContext).unregisterMBean(name);

    }



    /*
     * Concrete implementation used for test
     */
    class CompendiumHandler extends AbstractCompendiumHandler {

        protected CompendiumHandler(JMXAgentContext agentContext, Filter filter) {
            super(agentContext, filter);
        }

        protected CompendiumHandler(JMXAgentContext agentContext, String clazz) {
            super(agentContext, clazz);
        }

        protected StandardMBean constructInjectMBean(Object targetService) {
            return null;
        }

        public String getBaseName() {
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2097.java