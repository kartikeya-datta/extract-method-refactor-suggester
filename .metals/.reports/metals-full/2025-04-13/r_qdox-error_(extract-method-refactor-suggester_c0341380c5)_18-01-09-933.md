error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5755.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5755.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1078
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5755.java
text:
```scala
public class AbstractLifecycleCapableDescription  {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

p@@ackage org.jboss.as.ee.component;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class used to allow a description to support life-cycle (PostConstruct and PreDestroy) descriptions.
 *
 * @author John Bailey
 */
public class AbstractLifecycleCapableDescription extends AbstractInjectableDescription {
    private final List<InterceptorMethodDescription> postConstructs = new ArrayList<InterceptorMethodDescription>();
    private final List<InterceptorMethodDescription> preDestroys = new ArrayList<InterceptorMethodDescription>();

    private final List<ComponentLifecycle> postConstructsComponentLifecycles = new ArrayList<ComponentLifecycle>();
    private final List<ComponentLifecycle> postDestroysComponentLifecycles = new ArrayList<ComponentLifecycle>();

    /**
     * Adds a post construct ComponentLifecycle
     *
     * @param componentLifecycle The ComponentLifecycle
     */
    public void addPostConstructComponentLifecycle(ComponentLifecycle componentLifecycle) {
        postConstructsComponentLifecycles.add(componentLifecycle);
    }

    /**
     * Get the post-construct component lifecycles.
     *
     * @return the post-construct ComponentLifecycles
     */
    public  List<ComponentLifecycle> getPostConstructComponentLifecycles() {
        return postConstructsComponentLifecycles;
    }

    /**
     * Adds a post destroy ComponentLifecycle
     *
     * @param componentLifecycle The ComponentLifecycle
     */
    public void addPreDestroyComponentLifecycle(ComponentLifecycle componentLifecycle) {
        postDestroysComponentLifecycles.add(componentLifecycle);
    }

    /**
     * Get the post-destroy component lifecycles.
     *
     * @return the post-destroy ComponentLifecycles
     */
    public  List<ComponentLifecycle> getPreDestroyComponentLifecycles() {
        return postDestroysComponentLifecycles;
    }

    /**
     * Adds a post construct method
     *
     * @param methodDescription The method
     */
    public void addPostConstructMethod(InterceptorMethodDescription methodDescription) {
        postConstructs.add(methodDescription);
    }

    /**
     * Get the post-construct lifecycle method configurations.
     *
     * @return the post-construct lifecycle method configurations
     */
    public List<InterceptorMethodDescription> getPostConstructs() {
        return postConstructs;
    }

    /**
     * Adds a pre destroy method
     *
     * @param methodDescription The method
     */
    public void addPreDestroyMethod(InterceptorMethodDescription methodDescription) {
        preDestroys.add(methodDescription);
    }

    /**
     * Get the pre-destroy lifecycle method configurations.
     *
     * @return the pre-destroy lifecycle method configurations
     */
    public List<InterceptorMethodDescription> getPreDestroys() {
        return preDestroys;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5755.java