error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8741.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8741.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8741.java
text:
```scala
b@@oolean getStatelessHint();

/*
 * $Id$ $Revision$
 * $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.behavior;

import java.io.Serializable;

import wicket.Component;
import wicket.markup.ComponentTag;

/**
 * Behaviors are kind of plug-ins for Components. They allow to be added to a
 * component and get essential events forwarded by the component. they can be
 * bound to a concrete component (using the bind method is called when the
 * behavior is attached), but they don't need to. They can modify the components
 * markup by changing the rendered ComponentTag. Behaviors can have their own
 * models as well, and they are notified when these are to be detached by the
 * component.
 * <p>
 * It is recommended that you extend from
 * {@link wicket.behavior.AbstractBehavior} instead of directly implementing
 * this interface.
 * </p>
 * 
 * @see wicket.behavior.IBehaviorListener
 * @see wicket.markup.html.IHeaderContributor
 * @see wicket.behavior.AbstractAjaxBehavior
 * @see wicket.AttributeModifier
 * 
 * @author Ralf Ebert
 * @author Eelco Hillenius
 */
public interface IBehavior extends Serializable
{
	/**
	 * Bind this handler to the given component. This method is called by the
	 * host component immediately after this behavior is added to it. This
	 * method is useful if you need to do initialization based on the component
	 * it is attached can you can't wait to do it at render time. Keep in mind
	 * that if you decide to keep a reference to the host component, it is not
	 * thread safe anymore, and should thus only be used in situations where you
	 * do not reuse the behavior for multiple components.
	 * 
	 * @param component
	 *            the component to bind to
	 */
	void bind(Component component);

	/**
	 * Provides for the ability to detach any models this behavior has. This
	 * method is called by the components which have this behavior attached to
	 * them when they are detaching their models themselves (ie after
	 * rendering). Note that if you share a behavior between components, this
	 * method is called multiple times.
	 * 
	 * @param component
	 *            the component that initiates the detachement of this behavior
	 */
	void detachModel(Component component);

	/**
	 * Called any time a component that has this behavior registered is
	 * rendering the component tag.
	 * 
	 * @param component
	 *            the component that renders this tag currently
	 * @param tag
	 *            the tag that is rendered
	 */
	void onComponentTag(Component component, ComponentTag tag);

	/**
	 * Called when a component that has this behavior coupled was rendered.
	 * 
	 * @param component
	 *            the component that has this behavior coupled
	 */
	void rendered(Component component);

	/**
	 * In case an unexpected exception happened anywhere between
	 * onComponentTag() and rendered(), onException() will be called for any
	 * behavior. Typically, if you clean up resources in
	 * {@link #rendered(Component)}, you should do the same in the
	 * implementation of this method.
	 * 
	 * @param component
	 *            the component that has a reference to this behavior and during
	 *            which processing the exception occured
	 * @param exception
	 *            the unexpected exception
	 */
	void exception(Component component, RuntimeException exception);


	/**
	 * This method returns false if the behaviour generates a callback url (for
	 * example ajax behaviours)
	 * 
	 * @return boolean true or false.
	 */
	boolean isStateless();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8741.java