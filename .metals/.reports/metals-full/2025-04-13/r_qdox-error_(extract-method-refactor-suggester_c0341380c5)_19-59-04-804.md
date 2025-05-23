error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17670.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17670.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[142,2]

error in qdox parser
file content:
```java
offset: 4146
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17670.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.model;

import wicket.RequestCycle;

/**
 * This provide a base class to work with {@link wicket.model.IDetachableModel}.
 * It wraps the actual model objects of components and provides a call back mechanism for
 * reacting on the starting/ ending of a request. doAttach will be called at the first
 * access to this model within a request and - if the model was attached earlier, doDetach
 * will be called at the end of the request. In effect, attachement and detachement is
 * only done when it is actually needed. As the wrapped model object is transient, either
 * attachement should be used to ensure the model onject is again available (after a
 * possible serialization), or extending classes should hold their own instance data
 * representing the model.
 */
public abstract class DetachableModel implements IDetachableModel
{
    /**
     * The wrapped model object. Note that this object is transient to ensure we never
     * serialize it even if the user forgets to set the object to null in their detach()
     * method
     */
    private transient Object object;

    /**
     * Transient flag to prevent multiple detach/attach scenario. We need to maintain this
     * flag as we allow 'null' model values!
     */
    private transient boolean attached = false;

    /**
     * Construct.
     */
    public DetachableModel()
    {
    }

    /**
     * Construct the detachable model with the given model object.
     * @param object the model object
     */
    public DetachableModel(final Object object)
    {
        this.object = object;
    }

    /**
     * Get the model object.
     * @return the model object
     * @see wicket.model.IModel#getObject()
     */
    public Object getObject()
    {
        return object;
    }

    /**
     * Set the model object.
     * @param object the model object
     * @see wicket.model.IModel#setObject(java.lang.Object)
     */
    public void setObject(final Object object)
    {
        this.object = object;
    }

    /**
     * Whether this model has been attached to the current request.
     * @return whether this model has been attached to the current request
     */
    public boolean isAttached()
    {
        return attached;
    }

    /**
     * Attach to the current request.
     * @param cycle the current request cycle
     * @see wicket.model.IDetachableModel#attach(wicket.RequestCycle)
     */
    public final void attach(final RequestCycle cycle)
    {
        if (attached)
        {
            return;
        }

        doAttach(cycle);
        attached = true;
    }

    /**
     * Detach from the current request.
     * @param cycle the current request cycle
     * @see wicket.model.IDetachableModel#detach(wicket.RequestCycle)
     */
    public final void detach(final RequestCycle cycle)
    {
        if (!attached)
        {
            return;
        }

        doDetach(cycle);
        attached = false;
    }

    /**
     * Attach to the current request. Implement this method with custom behaviour, such
     * as loading the model object.
     * @param cycle the current request cycle
     */
    protected abstract void doAttach(final RequestCycle cycle);

    /**
     * Detach from the current request. Implement this method with custom behaviour, such as
     * setting the model object to null.
     * @param cycle the current request cycle
     */
    protected abstract void doDetach(final RequestCycle cycle);

}@@
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17670.java